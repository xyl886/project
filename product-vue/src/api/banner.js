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
