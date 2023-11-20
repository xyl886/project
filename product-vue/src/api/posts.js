import request from '@/utils/request'

export const listAllTags = () => {
  return request({
    url: '/api/home/listAllTags',
    method: 'get'
  })
}
/**
 * 查询所有分类
 * @returns {*}
 */
export const listAllCategory = (data) => {
  return request({
    url: '/api/category/listAll',
    method: 'post',
    data
  })
}
export const listHot = () => {
  return request({
    url: '/api/posts/listHot',
    method: 'get'
  })
}
/**
 * 帖子分页
 */
export const getPage = (data) => {
  return request({
    url: '/api/posts/getPage',
    method: 'post',
    data
  })
}

/**
 * 帖子发布
 */
export const add = (data) => {
  return request({
    url: '/api/posts/add',
    method: 'post',
    data
  })
}

/**
 * 帖子详情
 */
export const getDetail = (id) => {
  return request({
    url: '/api/posts/getDetail',
    method: 'get',
    params: {
      id: id
    }
  })
}

/**
 * 帖子浏览
 */
export const browse = (userId, id) => {
  return request({
    url: '/api/posts/browse',
    method: 'get',
    params: {
      userId: userId,
      id: id
    }
  })
}
/**
 * 帖子修改
 */
export const updateMyPost = (data) => {
  return request({
    url: '/api/posts/update',
    method: 'post',
    data
  })
}
// 彻底删除
export const del = (id, userId) => {
  return request({
    url: '/api/posts/delete',
    method: 'delete',
    params: {
      id: id,
      userId: userId
    }
  })
}
// 还原
export const restore = (id, userId) => {
  return request({
    url: '/api/posts/restore',
    method: 'post',
    params: {
      id: id,
      userId: userId
    }
  })
}
/**
 * 帖子删除
 */
export const delMyPost = (id, userId) => {
  return request({
    url: '/api/posts/del',
    method: 'delete',
    params: {
      id: id,
      userId: userId
    }
  })
}
