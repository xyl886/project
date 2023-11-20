import request from '../utils/request'

export function getHot (type) {
  return request({
    url: '/api/home/hot',
    method: 'get',
    params: {
      type: type
    }
  })
}
