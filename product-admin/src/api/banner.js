import request from '@/utils/request'

/**
 * 轮播图
 */
export const listAll = () => {
  return request({
    url: '/api/banner/listAll',
    method: 'get',
    params: {}
  })
}
export function update(data) {
  return request({
    url: '/api/banner/update',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/api/banner/add',
    method: 'POST',
    data
  })
}

export function remove(id) {
  return request({
    url: '/api/banner/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

export function deleteBatch(data) {
  return request({
    url: '/api/banner/deleteBatch',
    method: 'delete',
    data
  })
}
