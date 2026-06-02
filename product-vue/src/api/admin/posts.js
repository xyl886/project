import request from '@/utils/request'

export function getPostPage(params) {
  return request.post('/posts/getPage', params)
}

export function deletePost(id, userId) {
  return request.delete('/posts/del', { params: { id, userId } })
}
