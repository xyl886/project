import request from '@/utils/request'

// 切换收藏状态
export function toggleCollect(postsIds, deleted) {
  return request.post('/collect/add', null, { params: { postsIds, deleted } })
}

// 获取我的收藏列表
export function getCollectPage(params) {
  return request.post('/collect/getPage', params)
}

// 查询收藏状态
export function getCollectStatus(postsId) {
  return request.get('/collect/status', { params: { postsId } })
}
