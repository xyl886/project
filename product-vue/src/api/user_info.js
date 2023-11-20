import request from '@/utils/request'

export function getCaptcha (data) {
  return request({
    url: '/api/login/Capatha',
    method: 'post',
    data
  })
}
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
/**
 * 更新用户密码
 */
export const updateUserPwd = (params) => {
  return request({
    url: '/api/user/updateUserPwd',
    method: 'post',
    params: params
  })
}
/**
 * 重置密码
 */
export const userReset = (params) => {
  return request({
    url: '/api/user/reset',
    method: 'post',
    params: params
  })
}
