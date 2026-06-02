<template>
  <div id="app-root">
    <!-- Admin layout: no topbar, rendered by AdminLayout -->
    <template v-if="isAdminRoute">
      <router-view/>
    </template>
    <!-- User layout: topbar + router-view -->
    <template v-else>
      <header class="topbar">
        <div class="topbar-inner">
          <div class="logo" @click="$router.push('/home')">
            <span class="logo-text">校园墙</span>
          </div>
          <nav class="nav-links">
            <router-link to="/home" class="nav-item">首页</router-link>
            <router-link to="/search" class="nav-item">发现</router-link>
          </nav>
          <div class="topbar-search">
            <el-input
              v-model="searchKeyword" placeholder="搜索帖子..."
              size="small" clearable
              :prefix-icon="SearchIcon"
              @keyup.enter="goSearch"
            />
          </div>
          <div class="topbar-right">
            <template v-if="!!store.token">
              <el-badge :value="unreadCount" :hidden="!unreadCount" class="notice-badge">
                <el-icon size="20" style="color:#666;cursor:pointer" @click="$router.push('/messages')"><Bell /></el-icon>
              </el-badge>
              <el-button type="primary" size="small" round @click="$router.push('/create')">
                <el-icon><Plus/></el-icon> 发布
              </el-button>
              <el-dropdown trigger="click" @command="handleCommand">
                <span class="user-avatar">
                  <el-avatar :size="30" :src="store.userInfo?.avatar">
                    {{ (store.userInfo?.nickname || 'U')[0] }}
                  </el-avatar>
                  <span class="username">{{ store.userInfo?.nickname || '用户' }}</span>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                    <el-dropdown-item v-if="isCurrentAdmin" command="admin">管理后台</el-dropdown-item>
                    <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <el-button type="primary" size="small" @click="$router.push('/login')">登录</el-button>
            </template>
          </div>
        </div>
      </header>
      <main class="main-area">
        <router-view/>
      </main>
    </template>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {Bell, Plus, Search as SearchIcon} from '@element-plus/icons-vue'
import {useUserStore} from './store/user'
import {useAdminStore} from './store/admin'
import {useWebSocket} from './utils/websocket'

const route = useRoute()
const router = useRouter()
const store = useUserStore()
const adminStore = useAdminStore()
const searchKeyword = ref('')
const { unreadCount, connect, disconnect } = useWebSocket()

const isAdminRoute = computed(() => route.path.startsWith('/admin'))
const isCurrentAdmin = computed(() => adminStore.isAdmin || store.userInfo?.role === 3)

function goSearch() {
  const kw = searchKeyword.value.trim()
  if (kw) router.push({ path: '/search', query: { q: kw } })
}

onMounted(() => {
  if (!isAdminRoute.value && store.token) {
    store.fetchUserInfo().catch(() => {})
    connect()
  }
})

function handleCommand(cmd) {
  if (cmd === 'profile') router.push('/user')
  else if (cmd === 'admin') router.push('/admin/dashboard')
  else if (cmd === 'logout') {
    store.logout()
    disconnect()
    ElMessage.success('已退出')
    router.push('/login')
  }
}
</script>

<style>
#app-root {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.topbar {
  position: sticky;
  top: 0;
  z-index: 100;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
}

.topbar-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  gap: 24px;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  flex-shrink: 0;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, #409EFF, #36cfc9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: 1px;
}

.nav-links {
  display: flex;
  gap: 4px;
  flex-shrink: 0;
}

.nav-item {
  padding: 7px 16px;
  border-radius: 8px;
  font-size: 15px;
  color: #555;
  text-decoration: none;
  transition: all .2s;
}

.nav-item:hover, .nav-item.router-link-active {
  background: #ecf5ff;
  color: #409EFF;
}

.topbar-search {
  flex: 1;
  max-width: 360px;
  display: flex;
  justify-content: center;
}

.topbar-search .el-input {
  max-width: 280px;
}

.topbar-search .el-input__wrapper {
  border-radius: 20px;
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 14px;
  flex-shrink: 0;
  margin-left: auto;
}

.notice-badge {
  display: flex;
  align-items: center;
}

.user-avatar {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  padding: 3px 10px 3px 3px;
  border-radius: 20px;
  transition: background .2s;
}

.user-avatar:hover {
  background: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #333;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.main-area {
  flex: 1;
}
</style>
