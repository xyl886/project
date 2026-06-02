import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { loginAdmin } from '@/api/admin'
import { getCurrentAdminUser } from '@/api/admin/user'
import { useUserStore } from './user'

function safeJsonParse(str) {
  try { return JSON.parse(str) } catch { return null }
}

export const useAdminStore = defineStore('admin', () => {
  const adminInfo = ref(safeJsonParse(localStorage.getItem('adminInfo')))

  const isAdmin = computed(() => adminInfo.value?.role === 3)

  // 跨标签同步
  if (typeof window !== 'undefined') {
    window.addEventListener('storage', (e) => {
      if (e.key === 'adminInfo' && !e.newValue) {
        adminInfo.value = null
      } else if (e.key === 'token' && !e.newValue) {
        adminInfo.value = null
      }
    })
  }

  async function login(email, password) {
    const res = await loginAdmin({ email, password })
    if (res.code === 200) {
      const userStore = useUserStore()
      userStore.token = res.data.accessToken
      adminInfo.value = res.data
      localStorage.setItem('adminInfo', JSON.stringify(res.data))
    }
    return res
  }

  function logout() {
    const userStore = useUserStore()
    userStore.token = ''
    adminInfo.value = null
    localStorage.removeItem('adminInfo')
  }

  /** 向后端校验当前会话是否是管理员，不是则清除凭证 */
  async function verifySession() {
    if (!localStorage.getItem('token')) return false
    try {
      const res = await getCurrentAdminUser()
      if (res.code === 200 && res.data?.role === 3) {
        adminInfo.value = res.data
        localStorage.setItem('adminInfo', JSON.stringify(res.data))
        return true
      }
    } catch {}
    logout()
    return false
  }

  return { adminInfo, isAdmin, login, logout, verifySession }
})
