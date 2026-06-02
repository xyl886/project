import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = token
  return config
})

request.interceptors.response.use(
  res => {
    if (res.data.code === 401) {
      const isAdmin = /^\/system\//.test(res.config.url)
      const redirect = encodeURIComponent(router.currentRoute?.value?.fullPath || '/')
      localStorage.removeItem('token')
      localStorage.removeItem('adminInfo')
      localStorage.removeItem('userInfo')
      router.push(isAdmin ? '/admin/login' : `/login?redirect=${redirect}`)
      ElMessage.error('登录已过期，请重新登录')
      return Promise.reject(new Error(res.data.msg))
    }
    if (res.data.code === 500) {
      ElMessage.error(res.data.msg || '服务异常')
      return Promise.reject(new Error(res.data.msg))
    }
    return res.data
  },
  err => {
    const status = err.response?.status
    if (status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('adminInfo')
      localStorage.removeItem('userInfo')
      const isAdmin = /^\/system\//.test(err.config?.url || '')
      router.push(isAdmin ? '/admin/login' : '/login')
      ElMessage.error('登录已过期，请重新登录')
    } else if (status === 500) {
      ElMessage.error(err.response?.data?.msg || '服务异常')
    } else if (status === 413) {
      ElMessage.error('文件大小超过限制')
    } else {
      ElMessage.error(err.message === 'Network Error' ? '网络连接失败，请检查网络' : '网络错误')
    }
    return Promise.reject(err)
  }
)

export default request
