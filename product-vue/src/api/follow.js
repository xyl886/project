import request from '@/utils/request'

// 切换关注状态
export function toggleFollow(beFollowedUserId, deleted) {
  return request.post('/follow/add', null, { params: { beFollowedUserId, deleted } })
}

// 获取关注/粉丝列表  followType: 1=关注 2=粉丝
export function getFollowPage(params) {
  return request.post('/follow/getPage', params)
}
