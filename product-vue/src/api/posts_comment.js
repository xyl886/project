import request from '@/utils/request'

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
