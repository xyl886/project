import request from '@/utils/request'

export const audit = (data) => {
  return request({
    url: '/api/system/posts/audit',
    method: 'post',
    data
  })
}
/**
 * 查询所有标签
 * @returns {*}
 */
export const listAllTags = () => {
  return request({
    url: '/api/home/listAllTags',
    method: 'get'
  })
}
export const listAllCategory = (data) => {
  console.log(JSON.stringify(data))
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
  console.log('帖子修改' + JSON.stringify(data))
  return request({
    url: '/api/posts/update',
    method: 'post',
    data
  })
}
/**
 * 帖子删除
 */
export const delMyPost = (id, userId) => {
  // console.log(id, userId)
  return request({
    url: '/api/posts/del',
    method: 'delete',
    params: {
      id: id,
      userId: userId
    }
  })
}
