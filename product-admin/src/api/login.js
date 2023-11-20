import request from '@/utils/request'

export const sendEmailCode = (email, type) => {
  return request({
    url: '/api/login/code',
    method: 'get',
    params: {
      email: email,
      type: type
    }
  })
}

/**
 * 登录
 */
export const userLogin = (params) => {
  return request({
    url: '/api/system/login',
    method: 'get',
    params: params
  })
}

/**
 * 注册
 */
export const userRegister = (params) => {
  return request({
    url: '/api/login/userRegister',
    method: 'get',
    params: params
  })
}

/**
 *退出登录
 *
 */
export const userLogout = () => {
  return request({
    url: '/api/login/userLogout',
    method: 'get'
  })
}
