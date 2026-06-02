import request from '@/utils/request'

export function getCommentList(params) {
  return request.post('/system/comment/list', params)
}

export function deleteComment(id) {
  return request.delete('/system/comment/delete', { params: { id } })
}
