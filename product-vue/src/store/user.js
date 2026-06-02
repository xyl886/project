import { defineStore } from 'pinia'
import { ref, watch } from 'vue'
import { loginUser, fetchUserDetail } from '@/api'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  // token ref 变化时同步到 localStorage
  watch(token, (val) => {
    if (val) localStorage.setItem('token', val)
    else localStorage.removeItem('token')
  })

  // 跨标签同步：其它标签页登出时同步清除
  if (typeof window !== 'undefined') {
    window.addEventListener('storage', (e) => {
      if (e.key === 'token' && !e.newValue) {
        token.value = ''
        userInfo.value = null
      }
    })
  }

  async function login(email, password, verCode, verKey) {
    const res = await loginUser({ email, password, verCode, verKey })
    if (res.code === 200) {
      token.value = res.data.accessToken
      userInfo.value = res.data
    }
    return res
  }

  async function fetchUserInfo() {
    const res = await fetchUserDetail()
    if (res.code === 200) userInfo.value = res.data
    return res
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('adminInfo')
  }

  return { userInfo, token, login, fetchUserInfo, logout }
})
