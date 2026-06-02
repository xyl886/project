import request from '@/utils/request'

// 获取验证码
export function getCaptcha() {
  return request.get('/login/captcha')
}

// 用户登录
export function loginUser(params) {
  return request.post('/login/userLogin', params)
}

// 获取当前用户信息
export function fetchUserDetail() {
  return request.get('/user/detail')
}

// 更新用户信息 (multipart/form-data)
export function updateUserInfo(formData) {
  return request.post('/user/update', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 发送邮箱验证码
export function sendEmailCode(email, type) {
  return request.get('/login/code', { params: { email, type } })
}

// 用户注册
export function registerUser(params) {
  return request.post('/login/userRegister', params)
}

// 搜索用户
export function searchUser(keyword) {
  return request.get('/user/search', { params: { keyword } })
}

// 查看用户主页
export function getUserProfile(id) {
  return request.get('/user/profile', { params: { id } })
}
