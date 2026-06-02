import request from '@/utils/request'

// 获取帖子详情
export function getPostDetail(id) {
  return request.get('/posts/getDetail', { params: { id } })
}

// 获取帖子列表（分页）
export function getPostPage(params) {
  return request.post('/posts/getPage', params)
}

// 获取热门帖子
export function listHotPosts() {
  return request.get('/posts/listHot')
}

// 搜索帖子
export function searchPosts(keywords) {
  return request.get('/posts/articles/search', { params: { keywords } })
}

// 创建帖子（multipart/form-data）
export function addPost(formData) {
  return request.post('/posts/add', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 分类列表
export function getCategoryListAll() {
  return request.post('/category/listAll', { pageSize: 999, currentPage: 1 })
}

// 标签列表（所有）
export function getTagList() {
  return request.get('/system/tags/webList')
}
