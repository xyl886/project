import request from '@/utils/request'

/**
 * 点赞
 */
export const addLike = (postsId, deleted) => {
  return request({
    url: '/api/postsLike/add',
    method: 'get',
    params: {
      postsId: postsId,
      deleted: deleted
    }
  })
}
/**
 * 查询
 */
export const getPostLike = (userId) => {
  return request({
    url: '/api/postsLike/get',
    method: 'get',
    params: {
      userId: userId
    }
  })
}
