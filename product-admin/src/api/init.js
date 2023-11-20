import request from '../utils/request'

export const lineCount = () => {
  return request({
    url: '/api/home/lineCount',
    method: 'get'
  })
}
export const init = () => {
  return request({
    url: '/api/home/init',
    method: 'get'
  })
}
