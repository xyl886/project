import request from '@/utils/request'

/**
 * 分页
 */
export const getPage = (data) => {
  console.log('data' + JSON.stringify(data))
  return request({
    url: '/api/history/getPage',
    method: 'post',
    data
  })
}

/**
 * 删除记录
 */
export const del = (id, userId) => {
  return request({
    url: '/api/history/del',
    method: 'delete',
    id: id,
    userId: userId
  })
}
