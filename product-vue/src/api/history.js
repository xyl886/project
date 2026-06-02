import request from '@/utils/request'

// 获取浏览记录
export function getHistoryPage(params) {
  return request.post('/history/getPage', params)
}

// 清空浏览记录
export function deleteHistory(ids) {
  return request.delete('/history/del', { params: { ids } })
}
