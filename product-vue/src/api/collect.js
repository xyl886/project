import request from '@/utils/request'

/**
 * 收藏
 */
export const addCollect = (postsId, deleted) => {
  return request({
    url: '/api/collect/add',
    method: 'get',
    params: {
      postsId: postsId,
      deleted: deleted
    }
  })
}

/**
 * 分页
 */
export const getPage = (data) => {
  return request({
    url: '/api/collect/getPage',
    method: 'post',
    data
  })
}
