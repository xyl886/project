import request from '../utils/request'

export const addReport = (data) => {
  return request({
    url: '/api/system/report/add',
    method: 'post',
    data
  })
}
