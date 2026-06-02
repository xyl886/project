<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <el-icon style="font-size: 22px; margin-right: 8px"><School /></el-icon>
        <span>校园墙管理</span>
      </div>
      <el-menu
        :default-active="route.path"
        router
        background-color="#1d1e2c"
        text-color="rgba(255,255,255,0.65)"
        active-text-color="#fff"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon><span>控制台</span>
        </el-menu-item>
        <el-menu-item index="/admin/posts">
          <el-icon><Document /></el-icon><span>帖子管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon><span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/categories">
          <el-icon><Collection /></el-icon><span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/comments">
          <el-icon><ChatDotSquare /></el-icon><span>评论管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/tags">
          <el-icon><PriceTag /></el-icon><span>标签管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="route.meta.title">{{ route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-button size="small" @click="router.push('/home')" style="margin-right: 12px">
            <el-icon><HomeFilled /></el-icon> 返回前台
          </el-button>
          <el-dropdown trigger="click">
            <span class="user-dropdown">
              <el-avatar :size="32" style="background: #409eff; vertical-align: middle">
                {{ adminName?.[0] || 'A' }}
              </el-avatar>
              <span style="margin-left: 8px; font-weight: 500">{{ adminName || '管理员' }}</span>
              <el-icon style="margin-left: 4px"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAdminStore } from '@/store/admin'

const route = useRoute()
const router = useRouter()
const store = useAdminStore()

const adminName = computed(() => store.adminInfo?.nickname || store.adminInfo?.email || '管理员')

onMounted(async () => {
  // 向后端校验管理员身份，防止本地伪造凭证
  const ok = await store.verifySession()
  if (!ok && !route.meta.noAuth) {
    router.replace('/admin/login')
  }
})

async function handleLogout() {
  await ElMessageBox.confirm('确定退出登录？', '提示')
  store.logout()
  router.replace('/admin/login')
}
</script>

<style scoped>
.sidebar { background: #1d1e2c; overflow-y: auto; overflow-x: hidden; }
.logo {
  height: 60px; display: flex; align-items: center; justify-content: center;
  color: #fff; font-size: 18px; font-weight: bold;
  border-bottom: 1px solid rgba(255,255,255,0.08); letter-spacing: 1px;
}
.el-menu { border-right: none; }
.header {
  background: #fff; display: flex; align-items: center; justify-content: space-between;
  border-bottom: 1px solid #e8e8e8; height: 60px; padding: 0 24px;
}
.header-right { display: flex; align-items: center; }
.user-dropdown {
  display: flex; align-items: center; cursor: pointer;
  padding: 4px 12px; border-radius: 8px; transition: background 0.2s;
}
.user-dropdown:hover { background: #f5f5f5; }
.main-content { background: #f0f2f5; padding: 20px; }
</style>
