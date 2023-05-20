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
