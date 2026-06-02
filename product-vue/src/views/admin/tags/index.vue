<template>
  <el-card shadow="never" class="page-card">
    <template #header>
      <div class="card-header">
        <span style="font-weight: 600">标签管理</span>
        <el-button type="primary" size="default" @click="showDialog = true">
          <el-icon style="margin-right: 4px"><Plus /></el-icon>新增标签
        </el-button>
      </div>
    </template>
    <el-table :data="list" stripe v-loading="loading" border>
      <el-table-column prop="tagName" label="标签名" min-width="200"/>
      <el-table-column prop="createTime" label="创建时间" width="180"/>
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
    <el-dialog v-model="showDialog" title="新增标签" width="420" destroy-on-close>
      <el-form :model="form" label-width="60px">
        <el-form-item label="名称"><el-input v-model="form.tagName" placeholder="请输入标签名称"/></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAdd" :loading="adding">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getTagList, addTag, deleteTag } from '@/api/admin'

const list = ref([])
const loading = ref(false)
const adding = ref(false)
const showDialog = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const form = ref({ tagName: '' })

async function loadList() {
  loading.value = true
  const res = await getTagList({ tagName: '', pageSize: pageSize.value, currentPage: page.value })
  if (res.code === 200) { list.value = res.data || []; total.value = res.dataTotal || 0 }
  loading.value = false
}

async function handleAdd() {
  if (!form.value.tagName.trim()) { ElMessage.warning('请输入标签名称'); return }
  adding.value = true
  const res = await addTag(form.value)
  adding.value = false
  if (res.code === 200) {
    ElMessage.success('添加成功'); showDialog.value = false
    form.value = { tagName: '' }; await loadList()
  }
}

async function handleDelete(row) {
  try { await ElMessageBox.confirm(`确定删除标签「${row.tagName}」？`, '提示') } catch { return }
  const res = await deleteTag(row.id)
  if (res.code === 200) { ElMessage.success('删除成功'); await loadList() }
  else { ElMessage.error(res.msg || '删除失败') }
}

onMounted(loadList)
</script>

<style scoped>
.page-card { border-radius: 8px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
