import request from '@/utils/request'

/**
 * 关注
 */
export const addFollow = (beFollowedUserId, deleted) => {
  return request({
    url: '/api/follow/add',
    method: 'get',
    params: {
      beFollowedUserId: beFollowedUserId,
      deleted: deleted
    }
  })
}

/**
 * 分页
 */
export const getPage = (data) => {
  return request({
    url: '/api/follow/getPage',
    method: 'post',
    data
  })
}
