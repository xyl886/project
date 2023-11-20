import request from '@/utils/request'
export function listMessage () {
  return request({
    url: '/api/message/',
    method: 'get'
  })
}
export function addMessage (data) {
  return request({
    url: '/api/message/',
    method: 'post',
    data
  })
}
