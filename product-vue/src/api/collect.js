import request from '@/utils/request'

/**
 * 收藏
 */
export const addCollect = (postsIds, deleted) => {
  return request({
    url: '/api/collect/add',
    method: 'get',
    params: {
      postsIds: postsIds,
      deleted: deleted
    }
  })
}
export const deleteCollectBatch = (data) => {
  return request({
    url: '/api/collect/deleteBatch',
    method: 'post',
    data
  })
}
/**
 * 分页
 */
export const getPage = (data) => {
  console.log('data' + JSON.stringify(data))
  return request({
    url: '/api/collect/getPage',
    method: 'post',
    data
  })
}
