<template>
  <el-card shadow="never" class="page-card">
    <template #header><div class="card-header"><span style="font-weight: 600">用户列表</span></div></template>
    <el-form :model="searchForm" inline style="margin-bottom: 16px">
      <el-form-item label="昵称/邮箱">
        <el-input v-model="searchForm.username" placeholder="搜索用户..." clearable style="width: 200px" @keyup.enter="loadList"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadList">查询</el-button>
        <el-button @click="searchForm.username = ''; loadList()">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="list" stripe v-loading="loading" border>
      <el-table-column label="头像" width="60" align="center">
        <template #default="{ row }"><el-avatar :src="row.avatar" size="small"/></template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="130"/>
      <el-table-column prop="email" label="邮箱" min-width="200"/>
      <el-table-column label="性别" width="70" align="center">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '保密' }}</template>
      </el-table-column>
      <el-table-column label="角色" width="100" align="center">
        <template #default="{ row }">
          <el-tag size="small" :type="row.role === 3 ? 'danger' : row.role === 2 ? 'warning' : 'info'">
            {{ row.role === 3 ? '管理员' : row.role === 2 ? '版主' : '用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{ row.status === 1 ? '正常' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="180"/>
      <el-table-column label="操作" width="120" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" :type="row.status === 1 ? 'warning' : 'success'" plain @click="toggleStatus(row)">
            {{ row.status === 1 ? '禁用' : '启用' }}
          </el-button>
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
import { getUserList, updateUser } from '@/api/admin'

const list = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchForm = ref({ username: '' })

async function loadList() {
  loading.value = true
  const params = { pageSize: pageSize.value, currentPage: page.value }
  if (searchForm.value.username) params.username = searchForm.value.username
  const res = await getUserList(params)
  if (res.code === 200) { list.value = res.data || []; total.value = res.dataTotal || 0 }
  loading.value = false
}

async function toggleStatus(row) {
  const action = row.status === 1 ? '禁用' : '启用'
  try { await ElMessageBox.confirm(`确定${action}用户「${row.nickname}」？`, '提示') } catch { return }
  const res = await updateUser({ id: row.id, status: row.status === 1 ? 0 : 1 })
  if (res.code === 200) {
    ElMessage.success(`${action}成功`)
    await loadList()
  } else {
    ElMessage.error(res.msg || '操作失败')
  }
}

onMounted(loadList)
</script>

<style scoped>
.page-card { border-radius: 8px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
