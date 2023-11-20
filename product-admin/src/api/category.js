import request from '@/utils/request'

/**
 * 修改or新增
 */
export const updateCategory = (data) => {
  return request({
    url: '/api/system/category/update',
    method: 'post',
    data
  })
}
/**
 * 删除
 */
export const delCategory = (id) => {
  return request({
    url: '/api/system/category/delete',
    method: 'post',
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
    url: '/api/system/category/deleteBatch',
    method: 'post',
    data
  })
}
