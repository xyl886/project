import request from '@/utils/request'

export function getTagList(params) {
  return request.post('/system/tags/list', params)
}

export function addTag(data) {
  return request.post('/system/tags/add', data)
}

export function deleteTag(id) {
  return request.delete('/system/tags/remove', { params: { id } })
}

export function updateTag(data) {
  return request.post('/system/tags/update', data)
}
