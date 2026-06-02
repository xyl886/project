import request from '@/utils/request'

export function getUserList(params) {
  return request.post('/system/user/list', params)
}

export function getCurrentAdminUser() {
  return request.post('/system/user/getCurrentUserInfo')
}

export function updateUser(data) {
  return request.post('/system/user/update', data)
}
