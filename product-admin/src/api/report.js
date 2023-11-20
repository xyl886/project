import request from '@/utils/request'

/**
 * 查询所有标签
 * @returns {*}
 */
export const list = (data) => {
  return request({
    url: '/api/system/report/list',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/api/system/report/update',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/api/system/report/add',
    method: 'post',
    data
  })
}

export function remove(id) {
  return request({
    url: '/api/system/report/delete',
    method: 'delete',
    params: {
      id: id
    }
  })
}

export function deleteBatch(data) {
  return request({
    url: '/api/system/report/deleteBatch',
    method: 'delete',
    data
  })
}
