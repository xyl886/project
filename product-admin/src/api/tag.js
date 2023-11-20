import request from '@/utils/request'

/**
 * 查询所有标签
 * @returns {*}
 */
export const list = (data) => {
  return request({
    url: '/api/system/tags/list',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/api/system/tags/update',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/api/system/tags/add',
    method: 'POST',
    data
  })
}

export function remove(id) {
  return request({
    url: '/api/system/tags/remove',
    method: 'delete',
    params: {
      id: id
    }
  })
}

export function deleteBatch(data) {
  return request({
    url: '/api/system/tags/deleteBatch',
    method: 'delete',
    data
  })
}
