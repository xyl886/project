<template>
  <div class="page-container">
    <div class="main-content" v-loading="loading">
      <div v-if="post" class="detail-card">
        <div class="detail-nav">
          <el-button text @click="$router.back()" size="small">
            <el-icon><ArrowLeft/></el-icon> 返回
          </el-button>
        </div>
        <h1 class="detail-title">{{ post.title }}</h1>
        <div class="detail-meta">
          <el-avatar :size="32" :src="post.userInfo?.avatar">{{ (post.userInfo?.nickname || '?')[0] }}</el-avatar>
          <span>{{ post.userInfo?.nickname || '匿名' }}</span>
          <span class="dot">·</span>
          <span>{{ post.createTime }}</span>
          <span class="dot">·</span>
          <el-tag size="small">{{ post.categoryName || '未分类' }}</el-tag>
        </div>
        <div class="detail-content">{{ post.content }}</div>
        <div v-if="post.imgPath" class="detail-imgs">
          <el-image
            v-for="(img, i) in post.imgPath.split(',')" :key="i" :src="img"
            :style="{ width: imgsLength > 2 ? 'calc(33.33% - 8px)' : imgsLength > 1 ? 'calc(50% - 6px)' : '100%', maxHeight: '400px' }"
            fit="cover" :preview-src-list="post.imgPath.split(',')"
          >
            <template #error><div class="img-error">加载失败</div></template>
          </el-image>
        </div>
        <div class="detail-actions">
          <el-button :type="liked ? 'primary' : 'default'" round @click="toggleLike">
            <el-icon><Star/></el-icon> {{ liked ? '已赞' : '点赞' }} {{ post.likeNum || 0 }}
          </el-button>
          <el-button :type="collected ? 'warning' : 'default'" round @click="toggleCollect">
            <el-icon><StarFilled/></el-icon> {{ collected ? '已收藏' : '收藏' }}
          </el-button>
        </div>
        <div class="comment-section">
          <h3>评论 <span class="comment-count">({{ comments.length }})</span></h3>
          <div v-if="!userStore.token" class="comment-login-hint">
            请<el-link type="primary" @click="$router.push('/login')">登录</el-link>后发表评论
          </div>
          <div v-else class="comment-input-wrap">
            <div class="comment-input-area">
              <el-input v-model="commentText" placeholder="写下你的评论..." maxlength="500" :rows="3" type="textarea"
                        show-word-limit @keyup.enter.native="submitComment"/>
              <div class="comment-toolbar">
                <span class="emoji-btn" @click="showEmoji = !showEmoji">😊</span>
                <span v-if="replyTo" class="reply-hint">回复 @{{ replyTo.nickname }}
                  <el-button text size="small" @click="replyTo = null">取消</el-button>
                </span>
                <el-button type="primary" size="small" @click="submitComment" style="margin-left:auto">发表</el-button>
              </div>
              <div v-if="showEmoji" class="emoji-picker">
                <span v-for="e in emojis" :key="e" class="emoji-item" @click="insertEmoji(e)">{{ e }}</span>
              </div>
            </div>
          </div>
          <div v-loading="commentsLoading" element-loading-text="加载评论中...">
            <div v-for="c in rootComments" :key="c.id" class="comment-item">
              <el-avatar :size="28" :src="c.userInfo?.avatar">{{ (c.userInfo?.nickname || '?')[0] }}</el-avatar>
              <div class="comment-body">
                <div class="comment-user">{{ c.userInfo?.nickname || '匿名' }}</div>
                <div class="comment-text">{{ c.content }}</div>
                <div class="comment-actions-row">
                  <span class="comment-time">{{ c.createTime }}</span>
                  <span class="comment-action" @click="startReply(c)">回复</span>
                  <span class="comment-action" @click="toggleCommentLike(c)">
                    {{ c.liked ? '❤️' : '🤍' }} {{ c.likeNum || 0 }}
                  </span>
                  <span v-if="userStore.userInfo?.id === c.userId" class="comment-action danger" @click="deleteComment(c.id)">删除</span>
                </div>
                <!-- 子评论 -->
                <div v-if="childComments(c.id).length" class="sub-comments">
                  <div v-for="sub in childComments(c.id)" :key="sub.id" class="sub-comment-item">
                    <el-avatar :size="22" :src="sub.userInfo?.avatar">{{ (sub.userInfo?.nickname || '?')[0] }}</el-avatar>
                    <div class="sub-comment-body">
                      <div class="comment-user">{{ sub.userInfo?.nickname || '匿名' }}</div>
                      <div class="comment-text">{{ sub.content }}</div>
                      <div class="comment-actions-row">
                        <span class="comment-time">{{ sub.createTime }}</span>
                        <span class="comment-action" @click="startReply(c, sub)">回复</span>
                        <span class="comment-action" @click="toggleCommentLike(sub)">
                          {{ sub.liked ? '❤️' : '🤍' }} {{ sub.likeNum || 0 }}
                        </span>
                        <span v-if="userStore.userInfo?.id === sub.userId" class="comment-action danger" @click="deleteComment(sub.id)">删除</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <el-empty v-if="!comments.length" description="暂无评论"/>
        </div>
      </div>
    </div>
    <aside class="sidebar">
      <div class="sidebar-card">
        <h3>👤 作者信息</h3>
        <div v-if="post" style="display:flex;align-items:center;gap:12px;margin-top:12px;cursor:pointer" @click="$router.push('/user')">
          <el-avatar :size="48" :src="post.userInfo?.avatar">{{ (post.userInfo?.nickname || '?')[0] }}</el-avatar>
          <div>
            <div style="font-weight:600">{{ post.userInfo?.nickname || '匿名' }}</div>
            <div style="font-size:12px;color:#999">{{ post.userId }}号用户</div>
          </div>
        </div>
      </div>
    </aside>
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {ArrowLeft} from '@element-plus/icons-vue'
import {useUserStore} from '@/store/user'
import {getPostDetail, getCommentList, addComment} from '@/api'
import {likePost, dislikePost, toggleCollect as toggleCollectApi, getLikeStatus, getCollectStatus} from '@/api'
import {deleteComment as deleteCommentApi, toggleCommentLike as toggleCommentLikeApi} from '@/api/comment'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const post = ref(null)
const comments = ref([])
const loading = ref(true)
const liked = ref(false)
const collected = ref(false)
const commentText = ref('')
const imgsLength = ref(0)
const commentsLoading = ref(false)
const showEmoji = ref(false)
const replyTo = ref(null)

const emojis = ['😀','😃','😄','😁','😆','😅','🤣','😂','🙂','😊','😇','🥰','😍','🤩','😘','😗','😋','😛','😜','🤪','😝','🤑','🤗','🤭','🤔','🤐','😐','😑','😶','😏','😒','🙄','😬','😮','😯','😲','😳','🥺','😢','😭','😤','😡','🤬','💀','☠️','👍','👎','👊','✊','🤛','🤜','👏','🙌','👐','🤲','🤝','🙏','💪','✌️','🤟','🤘','👌','❤️','🧡','💛','💚','💙','💜','🖤','💔','🔥','⭐','🌟','✨','💡','📌','🎉','🎊','🎈','🎁','💯','✅','❌','❓','❗','🚫','🔞']

const rootComments = computed(() => comments.value.filter(c => !c.parentId))
function childComments(parentId) {
  return comments.value.filter(c => c.parentId === parentId)
}

function insertEmoji(e) {
  commentText.value += e
  showEmoji.value = false
}

function startReply(parent, sub) {
  replyTo.value = { id: parent.id, nickname: sub ? sub.userInfo?.nickname : parent.userInfo?.nickname }
  document.querySelector('.comment-input-area textarea')?.focus()
}

async function submitComment() {
  if (!requireAuth()) return
  if (!commentText.value.trim()) return
  const res = await addComment(route.params.id, commentText.value, replyTo.value?.id)
  if (res.code === 200) {
    ElMessage.success('评论成功')
    commentText.value = ''
    replyTo.value = null
    await loadComments()
  }
}

async function toggleCommentLike(c) {
  if (!requireAuth()) return
  const res = await toggleCommentLikeApi(c.id, c.liked ? 1 : 0)
  if (res.code === 200) {
    c.liked = !c.liked
    c.likeNum += c.liked ? 1 : -1
  }
}

async function loadPost() {
  loading.value = true
  const res = await getPostDetail(route.params.id)
  if (res.code === 200) {
    post.value = res.data
    imgsLength.value = res.data.imgPath ? res.data.imgPath.split(',').length : 0
  }
  loading.value = false
}

async function loadComments() {
  commentsLoading.value = true
  try {
    const res = await getCommentList(route.params.id)
    if (res.code === 200) comments.value = res.data || []
  } finally {
    commentsLoading.value = false
  }
}

async function deleteComment(id) {
  try { await ElMessageBox.confirm('确定删除该评论？', '提示') } catch { return }
  const res = await deleteCommentApi(id)
  if (res.code === 200) {
    ElMessage.success('评论已删除')
    await loadComments()
  } else {
    ElMessage.error(res.msg || '删除失败')
  }
}

function requireAuth() {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return false
  }
  return true
}

async function toggleLike() {
  if (!requireAuth()) return
  const res = liked.value ? await dislikePost(route.params.id) : await likePost(route.params.id)
  if (res.code === 200) {
    liked.value = !liked.value;
    post.value.likeNum += liked.value ? 1 : -1
  }
}

async function toggleCollect() {
  if (!requireAuth()) return
  const res = await toggleCollectApi(route.params.id, collected.value ? 1 : 0)
  if (res.code === 200) collected.value = !collected.value
}

async function loadUserStatus() {
  if (!userStore.token) return
  const [likeRes, collectRes] = await Promise.all([
    getLikeStatus(route.params.id).catch(() => {}),
    getCollectStatus(route.params.id).catch(() => {})
  ])
  if (likeRes?.code === 200) liked.value = likeRes.data === true
  if (collectRes?.code === 200) collected.value = collectRes.data === true
}

onMounted(() => {
  loadPost();
  loadComments();
  loadUserStatus()
})
</script>

<style scoped>
.detail-card {
  background: #fff;
  border-radius: 12px;
  padding: 32px;
  border: 1px solid #ebeef5;
}

.detail-title {
  font-size: 24px;
  color: #1a1a2e;
  margin-bottom: 16px;
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #999;
  margin-bottom: 24px;
}

.dot {
  color: #ddd;
}

.detail-content {
  font-size: 15px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 24px;
  white-space: pre-wrap;
}

.detail-imgs {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 24px;
}

.detail-imgs :deep(.el-image) {
  border-radius: 10px;
  overflow: hidden;
  background: #f5f5f5;
  flex: 1;
  min-width: 0;
}

.detail-actions {
  display: flex;
  gap: 12px;
}

.detail-nav {
  margin-bottom: 12px;
}

.comment-login-hint {
  color: #999;
  font-size: 14px;
  padding: 12px 0;
  text-align: center;
}

.img-error {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  background: #f5f5f5;
  color: #ccc;
  font-size: 13px;
}

.comment-actions {
  flex-shrink: 0;
  align-self: flex-start;
}

.comment-section h3 {
  font-size: 16px;
  color: #1a1a2e;
  margin-bottom: 16px;
}

.comment-count {
  color: #bbb;
  font-weight: 400;
  font-size: 14px;
}

.comment-input-wrap {
  margin-bottom: 20px;
}

.comment-input-area {
  width: 100%;
}

.comment-toolbar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
}

.emoji-btn {
  font-size: 22px;
  cursor: pointer;
  transition: transform .2s;
}

.emoji-btn:hover {
  transform: scale(1.2);
}

.emoji-picker {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  padding: 10px;
  background: #f8f9fc;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  margin-top: 8px;
  max-height: 150px;
  overflow-y: auto;
}

.emoji-item {
  font-size: 22px;
  cursor: pointer;
  padding: 2px;
  border-radius: 4px;
  transition: background .15s;
}

.emoji-item:hover {
  background: #e8f4fd;
}

.reply-hint {
  font-size: 12px;
  color: #409EFF;
  background: #ecf5ff;
  padding: 2px 8px;
  border-radius: 4px;
}

.comment-actions-row {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-top: 4px;
}

.comment-action {
  font-size: 12px;
  color: #999;
  cursor: pointer;
}

.comment-action:hover {
  color: #409EFF;
}

.comment-action.danger:hover {
  color: #F56C6C;
}

.sub-comments {
  margin-top: 8px;
  margin-left: 8px;
  padding-left: 12px;
  border-left: 2px solid #f0f0f0;
}

.sub-comment-item {
  display: flex;
  gap: 8px;
  padding: 8px 0;
}

.sub-comment-body {
  flex: 1;
  min-width: 0;
}

.comment-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;
  border-bottom: 1px solid #f5f5f5;
}

.comment-body {
  flex: 1;
}

.comment-user {
  font-size: 13px;
  font-weight: 600;
  color: #409EFF;
}

.comment-text {
  font-size: 14px;
  color: #333;
  margin: 4px 0;
}

.comment-time {
  font-size: 12px;
  color: #bbb;
}
</style>
