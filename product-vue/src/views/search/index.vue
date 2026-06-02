<template>
  <div class="page-container">
    <div class="main-content">
      <div class="search-bar-wrap">
        <el-input v-model="keyword" placeholder="搜索帖子标题..." size="large" clearable @keyup.enter="doSearch" class="search-input">
          <template #prefix>
            <el-icon><Search/></el-icon>
          </template>
          <template #append>
            <el-button @click="doSearch">搜索</el-button>
          </template>
        </el-input>
      </div>
      <div v-if="searched" class="search-result-info">找到 <strong>{{ list.length }}</strong> 个结果</div>
      <div v-loading="loading" class="search-results">
        <div v-for="item in list" :key="item.id" class="post-card" @click="$router.push(`/post/${item.id}`)">
          <div class="post-header">
            <el-avatar :size="26">{{ item.nickname?.[0] || '?' }}</el-avatar>
            <span class="post-author">{{ item.nickname || '匿名' }}</span>
          </div>
          <div class="title">{{ item.title }}</div>
          <div class="desc">{{ item.description || item.content }}</div>
          <div class="meta">
            <span class="stat"><el-icon><View/></el-icon> {{ item.browseNum || 0 }}</span>
          </div>
        </div>
        <el-empty v-if="searched && !list.length" description="没有找到相关帖子"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, watch} from 'vue'
import {useRoute} from 'vue-router'
import {Search, View} from '@element-plus/icons-vue'
import {searchPosts} from '@/api'

const route = useRoute()
const keyword = ref('')
const list = ref([])
const loading = ref(false)
const searched = ref(false)

async function doSearch() {
  if (!keyword.value.trim()) return
  searched.value = true
  loading.value = true
  try {
    const res = await searchPosts(keyword.value)
    if (res.code === 200) list.value = res.data || []
  } catch {
    list.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (route.query.q) {
    keyword.value = route.query.q
    doSearch()
  }
})

// 监听 URL 参数变化
watch(() => route.query.q, (val) => {
  if (val && val !== keyword.value) {
    keyword.value = val
    doSearch()
  }
})
</script>

<style scoped>
.search-bar-wrap {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #ebeef5;
  margin-bottom: 16px;
}

.search-input :deep(.el-input-group__append) {
  background: #409EFF;
  border-color: #409EFF;
}

.search-input :deep(.el-input-group__append .el-button) {
  color: #fff;
}

.search-result-info {
  font-size: 14px;
  color: #999;
  margin-bottom: 16px;
}

.search-results {
  min-height: 200px;
}

.post-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 12px;
  border: 1px solid #ebeef5;
  cursor: pointer;
  transition: all .25s;
}

.post-card:hover {
  border-color: transparent;
  box-shadow: 0 4px 20px rgba(0,0,0,.08);
  transform: translateY(-1px);
}

.post-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.post-author {
  font-size: 13px;
  color: #666;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 6px;
}

.desc {
  font-size: 14px;
  color: #888;
  line-height: 1.6;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  margin-bottom: 10px;
}

.meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 13px;
  color: #bbb;
}

.stat {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
