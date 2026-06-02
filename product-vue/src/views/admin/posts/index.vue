<template>
  <el-card shadow="never" class="page-card">
    <template #header>
      <div class="card-header"><span style="font-weight: 600">帖子列表</span></div>
    </template>
    <el-table :data="list" stripe v-loading="loading" border>
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip/>
      <el-table-column prop="nickname" label="作者" width="120"/>
      <el-table-column label="类型" width="90" align="center">
        <template #default="{ row }">
          <el-tag size="small" :type="row.postsType === 1 ? 'warning' : 'primary'">
            {{ row.postsType === 1 ? '闲置' : '校园' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag size="small" :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '正常' : '已删' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="browseNum" label="浏览" width="70" align="center"/>
      <el-table-column prop="likeNum" label="点赞" width="70" align="center"/>
      <el-table-column prop="commentNum" label="评论" width="70" align="center"/>
      <el-table-column prop="createTime" label="发布时间" width="180"/>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">查看</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="page" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]" :total="total"
      layout="total, sizes, prev, pager, next" background
      style="margin-top: 16px; justify-content: flex-end" @change="loadList"
    />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPostPage, deletePost } from '@/api/admin'

const list = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

async function loadList() {
  loading.value = true
  const res = await getPostPage({ page: page.value, size: pageSize.value })
  if (res.code === 200) { list.value = res.data || []; total.value = res.dataTotal || 0 }
  loading.value = false
}

function viewDetail(row) {
  window.open(`/post/${row.id}`, '_blank')
}

async function handleDelete(row) {
  try { await ElMessageBox.confirm('确定删除该帖子？', '提示') } catch { return }
  const res = await deletePost(row.id, row.userId)
  if (res.code === 200) { ElMessage.success('删除成功'); await loadList() }
  else { ElMessage.error(res.msg || '删除失败') }
}

onMounted(loadList)
</script>

<style scoped>
.page-card { border-radius: 8px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
