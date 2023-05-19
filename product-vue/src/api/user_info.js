import request from '@/utils/request'

/**
 * 详情
 */
export const detail = () => {
  return request({
    url: '/api/user/detail',
    method: 'get'
  })
}

/**
 * 更新用户信息
 */
export const update = (data) => {
  return request({
    url: '/api/user/update',
    method: 'post',
    data
  })
}
export const updatePwd = (params) => {
  return request({
    url: '/api/user/updateUserPassword',
    method: 'post',
    params: params
  })
}
