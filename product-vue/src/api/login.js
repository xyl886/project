import request from '@/utils/request'

/**
 * 登录
 */
export const userLogin = (params) => {
  return request({
    url: '/api/login/userLogin',
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
    method: 'get',
    params: {
    }
  })
}

