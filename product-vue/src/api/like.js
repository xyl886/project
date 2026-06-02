import request from '@/utils/request'

// 点赞帖子
export function likePost(postsId) {
  return request.post('/postsLike/like', null, { params: { postsId } })
}

// 取消点赞
export function dislikePost(postsId) {
  return request.post('/postsLike/dislike', null, { params: { postsId } })
}

// 查询点赞状态
export function getLikeStatus(postsId) {
  return request.get('/postsLike/status', { params: { postsId } })
}

// 获取点赞列表
export function getLikedList(params) {
  return request.post('/postsLike/getPage', params)
}

// 兼容旧接口：切换点赞状态
export function toggleLike(postsId, deleted) {
  return request.post('/postsLike/add', null, { params: { postsId, deleted } })
}
