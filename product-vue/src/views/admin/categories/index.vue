<template>
  <el-card shadow="never" class="page-card">
    <template #header>
      <div class="card-header">
        <span style="font-weight: 600">分类管理</span>
        <el-button type="primary" size="default" @click="editingId = null; form.value = { categoryName: '', description: '' }; showDialog = true">
          <el-icon style="margin-right: 4px"><Plus /></el-icon>新增分类
        </el-button>
      </div>
    </template>
    <el-table :data="list" stripe v-loading="loading" border>
      <el-table-column prop="categoryName" label="分类名称" min-width="150"/>
      <el-table-column prop="description" label="描述" min-width="300" show-overflow-tooltip/>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" plain @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" plain @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="page" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]" :total="total"
      layout="total, sizes, prev, pager, next" background
      style="margin-top: 16px; justify-content: flex-end" @change="loadList"
    />
    <el-dialog v-model="showDialog" :title="editingId ? '编辑分类' : '新增分类'" width="480" destroy-on-close>
      <el-form :model="form" label-width="60px">
        <el-form-item label="名称"><el-input v-model="form.categoryName" placeholder="请输入分类名称"/></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入分类描述"/></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAdd" :loading="adding">{{ editingId ? '保存' : '确定' }}</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCategoryList, addCategory, updateCategory, deleteCategory } from '@/api/admin'

const list = ref([])
const loading = ref(false)
const adding = ref(false)
const showDialog = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const form = ref({ categoryName: '', description: '' })
const editingId = ref(null)

async function loadList() {
  loading.value = true
  const res = await getCategoryList({ categoryName: '', pageSize: pageSize.value, currentPage: page.value })
  if (res.code === 200) { list.value = res.data || []; total.value = res.dataTotal || 0 }
  loading.value = false
}

function handleEdit(row) {
  editingId.value = row.id
  form.value = { categoryName: row.categoryName, description: row.description || '' }
  showDialog.value = true
}

async function handleAdd() {
  if (!form.value.categoryName.trim()) { ElMessage.warning('请输入分类名称'); return }
  adding.value = true
  let res
  if (editingId.value) {
    res = await updateCategory({ id: editingId.value, categoryName: form.value.categoryName, description: form.value.description })
  } else {
    res = await addCategory(form.value)
  }
  adding.value = false
  if (res.code === 200) {
    ElMessage.success(editingId.value ? '修改成功' : '添加成功')
    showDialog.value = false
    editingId.value = null
    form.value = { categoryName: '', description: '' }
    await loadList()
  } else {
    ElMessage.error(res.msg || '操作失败')
  }
}

async function handleDelete(row) {
  try { await ElMessageBox.confirm(`确定删除分类「${row.categoryName}」？`, '提示') } catch { return }
  const res = await deleteCategory(row.id)
  if (res.code === 200) { ElMessage.success('删除成功'); await loadList() }
  else { ElMessage.error(res.msg || '删除失败') }
}

onMounted(loadList)
</script>

<style scoped>
.page-card { border-radius: 8px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
