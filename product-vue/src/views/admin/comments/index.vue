<template>
  <el-card shadow="never" class="page-card">
    <template #header><div class="card-header"><span style="font-weight: 600">评论管理</span></div></template>
    <el-table :data="list" stripe v-loading="loading" border>
      <el-table-column prop="content" label="评论内容" min-width="350" show-overflow-tooltip />
      <el-table-column prop="nickName" label="评论者" width="130" />
      <el-table-column label="所属帖子" min-width="200" show-overflow-tooltip>
        <template #default="{ row }">{{ row.title || '-' }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="评论时间" width="180" />
      <el-table-column label="操作" width="120" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="danger" plain @click="handleDelete(row)">删除</el-button>
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
import { getCommentList, deleteComment } from '@/api/admin'

const list = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

async function loadList() {
  loading.value = true
  const res = await getCommentList({ pageSize: pageSize.value, currentPage: page.value })
  if (res.code === 200) { list.value = res.data || []; total.value = res.dataTotal || 0 }
  loading.value = false
}

async function handleDelete(row) {
  try { await ElMessageBox.confirm('确定删除该评论？', '提示') } catch { return }
  const res = await deleteComment(row.id)
  if (res.code === 200) { ElMessage.success('删除成功'); await loadList() }
  else { ElMessage.error(res.msg || '删除失败') }
}

onMounted(loadList)
</script>

<style scoped>
.page-card { border-radius: 8px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
