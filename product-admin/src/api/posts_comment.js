import request from '@/utils/request'

export const listComment = (data) => {
  return request({
    url: '/api/system/comment/list',
    method: 'post',
    data
  })
}
/**
 * 删除
 */
export const deleteComment = (id) => {
  return request({
    url: '/api/system/comment/delete',
    method: 'delete',
    params: {
      id: id
    }
  })
}
/**
 * 批量删除
 */
export const deleteBatch = (data) => {
  return request({
    url: '/api/system/comment/deleteBatch',
    method: 'delete',
    data
  })
}

/* -----------------web端---------------------*/

/**
 * 新增评论
 */
export const addComment = (postsId, content) => {
  return request({
    url: '/api/postsComment/add',
    method: 'post',
    params: {
      postsId: postsId,
      content: content
    }
  })
}

/**
 * 评论列表
 */
export const listByPostsId = (postsId) => {
  return request({
    url: '/api/postsComment/listByPostsId',
    method: 'get',
    params: {
      postsId: postsId
    }
  })
}
/**
 * 点赞
 */
export const addCommentLike = (commentId, deleted) => {
  return request({
    url: '/api/postsComment/addCommentLike',
    method: 'get',
    params: {
      commentId: commentId,
      deleted: deleted
    }
  })
}
/**
 * 删除评论
 */
export const del = (id) => {
  return request({
    url: '/api/postsComment/del',
    method: 'get',
    params: {
      id: id
    }
  })
}
