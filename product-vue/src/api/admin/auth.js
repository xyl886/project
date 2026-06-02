import request from '@/utils/request'

export function loginAdmin(params) {
  return request.post('/system/login', params)
}
