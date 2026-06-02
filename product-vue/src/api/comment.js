import request from '@/utils/request'

// 获取帖子评论列表
export function getCommentList(postsId) {
  return request.get('/postsComment/listByPostsId', { params: { postsId } })
}

// 发表评论
export function addComment(postsId, content, parentCommentId) {
  const params = { postsId, content }
  if (parentCommentId) params.parentCommentId = parentCommentId
  return request.post('/postsComment/add', null, { params })
}

// 删除评论
export function deleteComment(id) {
  return request.delete('/postsComment/del', { params: { id } })
}

// 评论点赞/取消点赞
export function toggleCommentLike(commentId, deleted) {
  return request.post('/postsComment/addCommentLike', null, { params: { commentId, deleted } })
}
