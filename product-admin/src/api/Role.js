import request from '../utils/request'

export const listRole = (data) => {
  return request({
    url: '/api/system/role/list',
    method: 'post',
    data
  })
}
/**
 * 新增
 */
export const addRole = (data) => {
  return request({
    url: '/api/system/role/add',
    method: 'post',
    data
  })
}
/**
 * 修改
 */
export const updateRole = (data) => {
  return request({
    url: '/api/system/role/update',
    method: 'post',
    data
  })
}
/**
 * 删除
 */
export const deleteRole = (id) => {
  return request({
    url: '/api/system/role/remove',
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
    url: '/api/system/role/deleteBatch',
    method: 'delete',
    data
  })
}
