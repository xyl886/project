import request from '@/utils/request'

export function getCategoryList(params) {
  return request.post('/category/listAll', params)
}

export function addCategory(data) {
  return request.post('/category/add', data)
}

export function deleteCategory(id) {
  return request.delete('/category/delete', { params: { id } })
}

export function updateCategory(data) {
  return request.post('/category/update', data)
}
