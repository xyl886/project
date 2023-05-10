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
