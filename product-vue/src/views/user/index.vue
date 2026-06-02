<template>
  <div class="page-container">
    <div class="profile-main">
      <div class="profile-card">
        <div class="profile-bg"></div>
        <div class="profile-head">
          <el-avatar :size="72" :src="store.userInfo?.avatar" class="profile-avatar">{{ (store.userInfo?.nickname || 'U')[0] }}</el-avatar>
          <div class="profile-info">
            <div class="profile-name-row">
              <h2>{{ store.userInfo?.nickname || '未知用户' }}</h2>
              <el-button size="small" plain round @click="openEditProfile">编辑资料</el-button>
            </div>
            <div class="profile-tags">
              <span class="profile-tag">{{ store.userInfo?.email || '' }}</span>
              <span class="profile-tag">{{ {0:'保密',1:'男',2:'女'}[store.userInfo?.gender] || '保密' }}</span>
              <span class="profile-tag" v-if="store.userInfo?.remark">{{ store.userInfo.remark }}</span>
            </div>
          </div>
        </div>
        <div class="profile-stats">
          <div class="stat-item"><strong>{{ store.userInfo?.postCount || 0 }}</strong><span>帖子</span></div>
          <div class="stat-item"><strong>{{ store.userInfo?.followNum || 0 }}</strong><span>关注</span></div>
          <div class="stat-item"><strong>{{ store.userInfo?.fansNum || 0 }}</strong><span>粉丝</span></div>
          <div class="stat-item"><strong>{{ store.userInfo?.likeNum || 0 }}</strong><span>获赞</span></div>
        </div>
      </div>

      <div class="tab-card">
        <el-tabs v-model="activeTab" class="profile-tabs">
          <el-tab-pane label="我的帖子" name="posts">
            <div v-loading="tabLoading" class="tab-body">
              <div v-if="!tabLoading && postsList.length" class="grid-4x3">
                <div v-for="item in postsList.slice(0,12)" :key="item.id" class="grid-card" @click="$router.push(`/post/${item.id}`)">
                  <div class="grid-card-img">
                    <el-image :src="item.coverPath" fit="cover">
                      <template #error><div class="img-placeholder">📄</div></template>
                    </el-image>
                  </div>
                  <div class="grid-card-body">
                    <div class="grid-card-title">{{ item.title }}</div>
                    <div class="grid-card-meta">
                      <span>👁 {{ item.browseNum || 0 }}</span>
                      <span>💬 {{ item.commentNum || 0 }}</span>
                      <span>❤️ {{ item.likeNum || 0 }}</span>
                    </div>
                  </div>
                </div>
              </div>
              <el-empty v-if="!tabLoading && !postsList.length" description="暂无帖子"/>
            </div>
          </el-tab-pane>

          <el-tab-pane label="我的收藏" name="collects">
            <div v-loading="tabLoading" class="tab-body">
              <div v-if="!tabLoading && collectsList.length" class="grid-4x3">
                <div v-for="item in collectsList.slice(0,12)" :key="item.id" class="grid-card" @click="$router.push(`/post/${item.postsId || item.id}`)">
                  <div class="grid-card-img">
                    <el-image :src="item.posts?.coverPath" fit="cover">
                      <template #error><div class="img-placeholder">📄</div></template>
                    </el-image>
                  </div>
                  <div class="grid-card-body">
                    <div class="grid-card-title">{{ item.posts?.title || item.title || '未知帖子' }}</div>
                    <div class="grid-card-meta">
                      <span>👁 {{ item.posts?.browseNum || 0 }}</span>
                      <el-button size="small" text type="danger" @click.stop="cancelCollect(item)">取消收藏</el-button>
                    </div>
                  </div>
                </div>
              </div>
              <el-empty v-if="!tabLoading && !collectsList.length" description="暂无收藏"/>
              <div v-if="hasMore && collectsList.length > 0" style="text-align:center;padding:12px">
                <el-button text size="small" :loading="tabLoading" @click="loadMore">加载更多</el-button>
              </div>
              <div v-if="!hasMore && collectsList.length > 0" style="text-align:center;padding:12px;color:#ccc;font-size:13px">— 没有更多了 —</div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="我的关注" name="following">
            <div v-loading="tabLoading" class="tab-body">
              <div v-for="item in followList" :key="item.id" class="user-item">
                <el-avatar :size="40">{{ item.userInfo?.nickname?.[0] || '?' }}</el-avatar>
                <div class="user-item-info">
                  <span class="user-item-name">{{ item.userInfo?.nickname || '未知' }}</span>
                  <span class="user-item-bio">{{ item.userInfo?.remark || '' }}</span>
                </div>
                <el-button size="small" plain @click.stop="cancelFollow(item)">取消关注</el-button>
              </div>
              <el-empty v-if="!tabLoading && !followList.length" description="暂无关注"/>
            </div>
          </el-tab-pane>

          <el-tab-pane label="我的粉丝" name="fans">
            <div v-loading="tabLoading" class="tab-body">
              <div v-for="item in fansList" :key="item.id" class="user-item">
                <el-avatar :size="40">{{ item.userInfo?.nickname?.[0] || '?' }}</el-avatar>
                <div class="user-item-info">
                  <span class="user-item-name">{{ item.userInfo?.nickname || '未知' }}</span>
                  <span class="user-item-bio">{{ item.userInfo?.remark || '' }}</span>
                </div>
              </div>
              <el-empty v-if="!tabLoading && !fansList.length" description="暂无粉丝"/>
            </div>
          </el-tab-pane>

          <el-tab-pane label="浏览记录" name="history">
            <div v-loading="tabLoading" class="tab-body">
              <div v-if="!tabLoading && historyList.length" class="grid-4x3">
                <div v-for="item in historyList.slice(0,12)" :key="item.id" class="grid-card">
                  <div class="grid-card-img" @click="$router.push(`/post/${item.postsId || item.id}`)">
                    <el-image :src="item.postCoverPath" fit="cover">
                      <template #error><div class="img-placeholder">📄</div></template>
                    </el-image>
                  </div>
                  <div class="grid-card-body">
                    <div class="grid-card-title" @click="$router.push(`/post/${item.postsId || item.id}`)">{{ item.postsTitle || item.title }}</div>
                    <div class="grid-card-meta">
                      <span>{{ item.createTime }}</span>
                      <el-button size="small" text type="danger" @click.stop="removeHistory(item.id)">删除</el-button>
                    </div>
                  </div>
                </div>
              </div>
              <el-empty v-if="!tabLoading && !historyList.length" description="暂无浏览记录"/>
            </div>
          </el-tab-pane>

          <el-tab-pane label="我的点赞" name="likes">
            <div v-loading="tabLoading" class="tab-body">
              <div v-if="!tabLoading && likesList.length" class="grid-4x3">
                <div v-for="item in likesList.slice(0,12)" :key="item.id" class="grid-card" @click="$router.push(`/post/${item.postsId || item.id}`)">
                  <div class="grid-card-img">
                    <el-image :src="item.postCoverPath" fit="cover">
                      <template #error><div class="img-placeholder">📄</div></template>
                    </el-image>
                  </div>
                  <div class="grid-card-body">
                    <div class="grid-card-title">{{ item.postTitle || item.title }}</div>
                    <div class="grid-card-meta"><span>{{ item.createTime }}</span></div>
                  </div>
                </div>
              </div>
              <el-empty v-if="!tabLoading && !likesList.length" description="暂无点赞"/>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>


    <el-dialog v-model="showProfileDialog" title="编辑资料" width="480" destroy-on-close>
      <el-form :model="profileForm" label-width="60px">
        <el-form-item label="头像">
          <el-upload :auto-upload="false" :show-file-list="false" accept="image/*" @change="onAvatarChange">
            <el-avatar :size="72" :src="avatarPreview">{{ store.userInfo?.nickname?.[0] || 'U' }}</el-avatar>
          </el-upload>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="profileForm.nickname" placeholder="请输入昵称"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="profileForm.gender">
            <el-radio :value="0">保密</el-radio>
            <el-radio :value="1">男</el-radio>
            <el-radio :value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="profileForm.remark" type="textarea" :rows="3" placeholder="介绍一下自己..."/>
        </el-form-item>
        <el-form-item label="爱好">
          <el-input v-model="profileForm.hobby" placeholder="你的爱好"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showProfileDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateProfile" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, onMounted, watch, reactive} from 'vue'
import {useRouter} from 'vue-router'
import {View, ChatLineRound} from '@element-plus/icons-vue'
import {ElMessageBox, ElMessage} from 'element-plus'
import {useUserStore} from '@/store/user'
import {getPostPage, getCollectPage, toggleCollect, updateUserInfo} from '@/api'
import {getFollowPage, toggleFollow} from '@/api/follow'
import {getHistoryPage, deleteHistory} from '@/api/history'
import {getLikeStatus, getLikedList} from '@/api/like'

const router = useRouter()
const store = useUserStore()
const activeTab = ref('posts')
const tabLoading = ref(false)
const saving = ref(false)
const showProfileDialog = ref(false)

// 各 tab 数据
const postsList = ref([])
const collectsList = ref([])
const followList = ref([])
const fansList = ref([])
const historyList = ref([])
const likesList = ref([])

// 分页状态
const pageNum = ref(1)
const hasMore = ref(true)

// 个人资料表单
const profileForm = reactive({
  nickname: '',
  gender: 0,
  remark: '',
  hobby: ''
})
const avatarFile = ref(null)
const avatarPreview = ref('')

function openEditProfile() {
  initProfileForm()
  showProfileDialog.value = true
}

function initProfileForm() {
  const u = store.userInfo
  if (!u) return
  profileForm.nickname = u.nickname || ''
  profileForm.gender = u.gender ?? 0
  profileForm.remark = u.remark || ''
  profileForm.hobby = u.hobby || ''
  avatarPreview.value = u.avatar || ''
}

function onAvatarChange(file) {
  avatarFile.value = file.raw
  if (avatarPreview.value && avatarPreview.value.startsWith('blob:')) {
    URL.revokeObjectURL(avatarPreview.value)
  }
  avatarPreview.value = URL.createObjectURL(file.raw)
}

async function handleUpdateProfile() {
  saving.value = true
  try {
    const fd = new FormData()
    fd.append('nickname', profileForm.nickname)
    fd.append('gender', profileForm.gender)
    fd.append('remark', profileForm.remark)
    fd.append('hobby', profileForm.hobby)
    if (avatarFile.value) fd.append('file', avatarFile.value)
    const res = await updateUserInfo(fd)
    if (res.code === 200) {
      ElMessage.success('保存成功')
      await store.fetchUserInfo()
      avatarFile.value = null
    } else {
      ElMessage.error(res.msg || '保存失败')
    }
  } catch {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

async function loadTabData(tab) {
  tabLoading.value = true
  try {
    if (tab === 'posts') {
      const res = await getPostPage({ page: 1, size: 20 })
      if (res.code === 200) postsList.value = res.data || []
    } else if (tab === 'collects') {
      const res = await getCollectPage({ pageSize: 20, currentPage: 1 })
      if (res.code === 200) collectsList.value = res.data || []
    } else if (tab === 'following') {
      const res = await getFollowPage({ followType: 1, pageSize: 20, currentPage: 1 })
      if (res.code === 200) followList.value = res.data || []
    } else if (tab === 'fans') {
      const res = await getFollowPage({ followType: 2, pageSize: 20, currentPage: 1 })
      if (res.code === 200) fansList.value = res.data || []
    } else if (tab === 'history') {
      const res = await getHistoryPage({ pageSize: 20, currentPage: 1 })
      if (res.code === 200) historyList.value = res.data || []
    } else if (tab === 'likes') {
      const res = await getLikedList({ pageSize: 20, currentPage: 1 })
      if (res.code === 200) likesList.value = res.data || []
    }
  } catch {
    // 数据加载失败，清空列表
    postsList.value = []
    collectsList.value = []
    followList.value = []
    fansList.value = []
    historyList.value = []
  } finally {
    tabLoading.value = false
  }
}

async function cancelCollect(item) {
  try { await ElMessageBox.confirm('确定取消收藏？', '提示') } catch { return }
  const res = await toggleCollect(item.postsId || item.id, 1)
  if (res.code === 200) {
    ElMessage.success('已取消收藏')
    collectsList.value = collectsList.value.filter(c => c.id !== item.id)
  }
}

async function cancelFollow(item) {
  try { await ElMessageBox.confirm('确定取消关注？', '提示') } catch { return }
  const res = await toggleFollow(item.beFollowedUserId || item.userId, 1)
  if (res.code === 200) {
    ElMessage.success('已取消关注')
    followList.value = followList.value.filter(f => f.id !== item.id)
  }
}

async function removeHistory(id) {
  try { await ElMessageBox.confirm('确定删除该浏览记录？', '提示') } catch { return }
  const res = await deleteHistory(id)
  if (res.code === 200) {
    ElMessage.success('已删除')
    historyList.value = historyList.value.filter(h => h.id !== id)
  }
}

async function loadMore() {
  pageNum.value++
  const tab = activeTab.value
  tabLoading.value = true
  try {
    if (tab === 'collects') {
      const res = await getCollectPage({ pageSize: 20, currentPage: pageNum.value })
      if (res.code === 200) {
        const data = res.data || []
        collectsList.value.push(...data)
        hasMore.value = data.length >= 20
      }
    }
  } finally { tabLoading.value = false }
}

watch(activeTab, loadTabData)

onMounted(() => {
  if (!store.userInfo) store.fetchUserInfo()
  loadTabData('posts')
})
</script>

<style scoped>
.profile-main {
  flex: 1;
  min-width: 0;
}

.profile-card {
  background: #fff;
  border-radius: 12px;
  border: 1px solid #ebeef5;
  margin-bottom: 16px;
  overflow: hidden;
  position: relative;
}

.profile-bg {
  height: 100px;
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.profile-head {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  padding: 0 28px;
  margin-top: -36px;
  position: relative;
  z-index: 1;
}

.profile-avatar {
  border: 4px solid #fff;
  box-shadow: 0 2px 12px rgba(0,0,0,.1);
  flex-shrink: 0;
}

.profile-info {
  flex: 1;
  min-width: 0;
  padding-top: 16px;
}

.profile-name-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}

.profile-name-row h2 {
  font-size: 20px;
  color: #1a1a2e;
}

.profile-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.profile-tag {
  font-size: 12px;
  color: #888;
  background: #f5f5f5;
  padding: 2px 10px;
  border-radius: 20px;
}

.profile-stats {
  display: flex;
  padding: 16px 28px;
  border-top: 1px solid #f0f0f0;
  margin-top: 16px;
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  border-right: 1px solid #f0f0f0;
}

.stat-item:last-child { border-right: none; }

.stat-item strong {
  font-size: 22px;
  color: #409EFF;
}

.stat-item span {
  font-size: 13px;
  color: #999;
}

.tab-card {
  background: #fff;
  border-radius: 12px;
  border: 1px solid #ebeef5;
  padding: 4px 20px 20px;
}

.profile-tabs {
  margin-top: 0;
}

.tab-body {
  min-height: 160px;
  padding-top: 8px;
}

/* 4×3 网格布局 */
.grid-4x3 {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
}

.grid-card {
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 10px;
  overflow: hidden;
  cursor: pointer;
  transition: all .25s;
}

.grid-card:hover {
  border-color: transparent;
  box-shadow: 0 4px 20px rgba(0,0,0,.08);
  transform: translateY(-2px);
}

.grid-card-img {
  width: 100%;
  aspect-ratio: 16/10;
  overflow: hidden;
  background: #f5f5f5;
}

.grid-card-img :deep(.el-image) {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.img-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  font-size: 32px;
  color: #ccc;
}

.grid-card-body {
  padding: 10px 12px 12px;
}

.grid-card-title {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.grid-card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #bbb;
  gap: 6px;
}

/* 响应式 */
@media (max-width: 900px) {
  .grid-4x3 { grid-template-columns: repeat(3, 1fr); }
}
@media (max-width: 600px) {
  .grid-4x3 { grid-template-columns: repeat(2, 1fr); }
}

/* 关注/粉丝用户卡片 */
.user-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 0;
  border-bottom: 1px solid #f5f5f5;
}

.user-item:last-child {
  border-bottom: none;
}

.user-item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.user-item-name {
  font-size: 15px;
  font-weight: 600;
  color: #1a1a2e;
}

.user-item-bio {
  font-size: 12px;
  color: #bbb;
}

</style>
