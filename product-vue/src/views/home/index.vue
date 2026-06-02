<template>
  <div class="page-container">
    <div class="main-content">
      <div class="home-tabs">
        <el-radio-group v-model="tab" @change="loadPosts" size="large">
          <el-radio-button value="hot">热门</el-radio-button>
          <el-radio-button value="latest">最新</el-radio-button>
          <el-radio-button value="campus">校园</el-radio-button>
          <el-radio-button value="idle">闲置</el-radio-button>
        </el-radio-group>
      </div>
      <div class="category-bar" v-if="tab !== 'hot'">
        <span class="cat-label">分类：</span>
        <span class="cat-item" :class="{ active: !categoryId }" @click="categoryId = null; loadPosts()">全部</span>
        <span v-for="c in categories" :key="c.id" class="cat-item" :class="{ active: categoryId === c.id }"
              @click="categoryId = c.id; loadPosts()">{{ c.categoryName }}</span>
      </div>
      <div v-loading="loading" style="min-height:200px">
        <div v-for="item in posts" :key="item.id" class="post-card" @click="goPost(item.id)">
          <div class="post-header">
            <el-avatar :size="32" :src="item.userInfo?.avatar">{{ (item.userInfo?.nickname || '?')[0] }}</el-avatar>
            <div class="post-user">
              <span class="post-nickname">{{ item.userInfo?.nickname || '匿名' }}</span>
              <span class="post-time">{{ item.createTime }}</span>
            </div>
            <el-tag v-if="item.categoryName" size="small" effect="plain" round>{{ item.categoryName }}</el-tag>
          </div>

          <div v-if="item.coverPath" class="post-cover" @click.stop>
            <el-image :src="item.coverPath" fit="cover" class="cover-img">
              <template #error><div class="img-error">图片加载失败</div></template>
            </el-image>
          </div>

          <div class="post-title">{{ item.title }}</div>
          <div class="post-desc">{{ item.description || item.content }}</div>

          <div v-if="item.tags && item.tags.length" class="post-tags">
            <span v-for="t in item.tags" :key="t" class="tag-badge">{{ t }}</span>
          </div>

          <div class="post-footer">
            <span class="post-stat"><el-icon><View/></el-icon> {{ item.browseNum || 0 }}</span>
            <span class="post-stat"><el-icon><ChatLineRound/></el-icon> {{ item.commentNum || 0 }}</span>
            <span class="post-stat"><el-icon><Star/></el-icon> {{ item.likeNum || 0 }}</span>
          </div>
        </div>
        <el-empty v-if="!loading && !posts.length" description="暂无帖子"/>
        <div ref="sentinel" v-show="tab !== 'hot' && hasMore" style="height:1px"/>
        <div v-if="loadingMore" style="text-align:center;padding:20px;color:#999;font-size:13px">加载中...</div>
        <div v-if="!hasMore && posts.length > 0" style="text-align:center;padding:20px;color:#ccc;font-size:13px">— 没有更多了 —</div>
      </div>
    </div>
    <aside class="sidebar">
      <div v-if="!!userStore.token" class="sidebar-card user-info-card" style="cursor:pointer" @click="$router.push('/user')">
        <div class="user-info-top">
          <el-avatar :size="48" :src="userStore.userInfo?.avatar">{{ (userStore.userInfo?.nickname || 'U')[0] }}</el-avatar>
          <div class="user-info-text">
            <span class="user-info-name">{{ userStore.userInfo?.nickname || '用户' }}</span>
            <span class="user-info-bio">{{ userStore.userInfo?.email || '' }}</span>
          </div>
        </div>
        <div class="user-stats">
          <div><strong>{{ userStore.userInfo?.postCount || 0 }}</strong><span>帖子</span></div>
          <div><strong>{{ userStore.userInfo?.fansNum || 0 }}</strong><span>粉丝</span></div>
          <div><strong>{{ userStore.userInfo?.followNum || 0 }}</strong><span>关注</span></div>
        </div>
      </div>
      <div class="sidebar-card">
        <h3>公告</h3>
        <p class="notice-text">欢迎来到校园墙！在这里你可以发布校园动态、闲置物品交易、学习交流等内容。</p>
      </div>
      <div class="sidebar-card">
        <h3>热门帖子</h3>
        <div v-for="item in hotList" :key="item.id" class="hot-item" @click="goPost(item.id)">
          <span class="hot-rank" :class="{ top: item._rank <= 3 }">{{ item._rank }}</span>
          <span class="hot-title">{{ item.title }}</span>
        </div>
        <el-empty v-if="!hotList.length" description="暂无" :image-size="60"/>
      </div>
      <div class="sidebar-card">
        <h3>站内统计</h3>
        <div class="stats-grid">
          <div><strong>{{ totalPosts }}</strong><span>帖子</span></div>
          <div><strong>{{ totalUsers || '-' }}</strong><span>用户</span></div>
        </div>
      </div>
    </aside>
  </div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, nextTick} from 'vue'
import {useRouter} from 'vue-router'
import {View, ChatLineRound, Star} from '@element-plus/icons-vue'
import {useUserStore} from '@/store/user'
import {listHotPosts, getPostPage, getCategoryListAll} from '@/api'
import {getLineCount} from '@/api/admin/dashboard'

// Note: getLineCount is used here for sidebar stats (已加入 SaToken 白名单)
// Consider moving to a dedicated stats API if needed

const router = useRouter()
const userStore = useUserStore()
const tab = ref('hot')
const posts = ref([])
const hotList = ref([])
const loading = ref(false)
const loadingMore = ref(false)
const totalPosts = ref(0)
const totalUsers = ref(0)
const categories = ref([])
const categoryId = ref(null)
const pageNum = ref(1)
const hasMore = ref(true)
const sentinel = ref(null)
let observer = null

function getPostsType() {
  if (tab.value === 'campus') return 2
  if (tab.value === 'idle') return 1
  return null
}

async function loadPosts(reset = true) {
  if (reset) {
    pageNum.value = 1
    hasMore.value = true
    posts.value = []
    loading.value = true
  }

  if (tab.value === 'hot') {
    try {
      const res = await listHotPosts()
      if (res.code === 200) {
        posts.value = (res.data || []).map((p, i) => ({...p, _rank: i + 1}))
      }
    } catch {
      posts.value = []
    } finally {
      loading.value = false
      loadingMore.value = false
    }
    return
  }

  const params = { pageSize: 10, currentPage: pageNum.value }
  const postsType = getPostsType()
  if (postsType !== null) params.postsType = postsType
  if (categoryId.value) params.categoryId = categoryId.value

  try {
    const res = await getPostPage(params)
    if (res.code === 200) {
      const records = res.data || []
      if (reset) {
        posts.value = records
      } else {
        posts.value.push(...records)
      }
      hasMore.value = records.length >= params.size
    }
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}

async function loadMore() {
  if (loadingMore.value || !hasMore.value || tab.value === 'hot') return
  loadingMore.value = true
  pageNum.value++
  await loadPosts(false)
}

function setupObserver() {
  observer = new IntersectionObserver(entries => {
    if (entries[0].isIntersecting) loadMore()
  }, { rootMargin: '100px' })
  nextTick(() => {
    if (sentinel.value) observer.observe(sentinel.value)
  })
}

function goPost(id) {
  router.push(`/post/${id}`)
}

onMounted(() => {
  loadPosts()
  listHotPosts().then(r => {
    if (r.code === 200) hotList.value = (r.data || []).slice(0, 8).map((p, i) => ({...p, _rank: i + 1}))
  }).catch(e => console.error('加载热门帖子失败', e))
  getCategoryListAll().then(r => {
    if (r.code === 200) categories.value = r.data || []
  }).catch(e => console.error('加载分类列表失败', e))
  getLineCount().then(r => {
    if (r.code === 200) {
      totalPosts.value = r.data?.postsCount || 0
      totalUsers.value = r.data?.userCount || 0
    }
  }).catch(e => console.error('加载站内统计失败', e))
  setupObserver()
})

onBeforeUnmount(() => observer?.disconnect())
</script>

<style scoped>
.home-tabs {
  margin-bottom: 20px;
}

.category-bar {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 18px;
  padding: 12px 16px;
  background: #fff;
  border-radius: 10px;
  border: 1px solid #ebeef5;
  flex-wrap: wrap;
}

.cat-label {
  font-size: 13px;
  color: #999;
  flex-shrink: 0;
  margin-right: 4px;
}

.cat-item {
  display: inline-block;
  padding: 4px 14px;
  font-size: 13px;
  color: #555;
  background: #f5f7fa;
  border-radius: 20px;
  cursor: pointer;
  transition: all .2s;
}

.cat-item:hover {
  color: #409EFF;
  background: #ecf5ff;
}

.cat-item.active {
  color: #fff;
  background: #409EFF;
}

.post-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
  border: 1px solid #ebeef5;
  cursor: pointer;
  transition: all .25s;
}

.post-card:hover {
  border-color: transparent;
  box-shadow: 0 4px 20px rgba(0, 0, 0, .08);
  transform: translateY(-1px);
}

.post-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 14px;
}

.post-user {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.post-nickname {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a2e;
}

.post-time {
  font-size: 12px;
  color: #bbb;
}

.post-cover {
  margin-bottom: 14px;
  border-radius: 10px;
  overflow: hidden;
  max-height: 260px;
}

.post-cover :deep(.el-image) {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.img-error {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  background: #f5f5f5;
  color: #ccc;
  font-size: 13px;
  border-radius: 8px;
}

.cover-img {
  display: block;
  width: 100%;
  max-height: 260px;
  object-fit: cover;
}

.post-title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 8px;
  line-height: 1.5;
}

.post-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.7;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.post-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 14px;
}

.tag-badge {
  display: inline-block;
  padding: 2px 10px;
  font-size: 12px;
  color: #409EFF;
  background: #ecf5ff;
  border-radius: 20px;
}

.post-footer {
  display: flex;
  gap: 20px;
  padding-top: 14px;
  border-top: 1px solid #f0f0f0;
}

.post-stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #999;
}

.sidebar-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
  border: 1px solid #ebeef5;
}

.sidebar-card h3 {
  font-size: 15px;
  color: #1a1a2e;
  margin-bottom: 12px;
  padding-bottom: 10px;
  border-bottom: 2px solid #f0f0f0;
}

.notice-text {
  font-size: 13px;
  color: #666;
  line-height: 1.8;
}

/* 用户信息卡 */
.user-info-top {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 16px;
}

.user-info-text {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-info-name {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a2e;
}

.user-info-bio {
  font-size: 12px;
  color: #bbb;
}

.user-stats {
  display: flex;
  justify-content: space-around;
  padding-top: 14px;
  border-top: 1px solid #f0f0f0;
}

.user-stats div {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.user-stats strong {
  font-size: 18px;
  color: #409EFF;
}

.user-stats span {
  font-size: 12px;
  color: #999;
}

.hot-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
}

.hot-item:last-child {
  border-bottom: none;
}

.hot-rank {
  width: 20px;
  height: 20px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  color: #999;
  background: #f5f5f5;
  flex-shrink: 0;
}

.hot-rank.top {
  background: #ff6b6b;
  color: #fff;
}

.hot-title {
  flex: 1;
  font-size: 13px;
  color: #555;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.stats-grid {
  display: flex;
  gap: 24px;
}

.stats-grid div {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stats-grid strong {
  font-size: 22px;
  color: #409EFF;
}

.stats-grid span {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}
</style>
