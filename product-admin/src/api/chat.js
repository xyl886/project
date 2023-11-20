import request from '@/utils/request'

export const addFriend = (id) => {
  return request({
    url: '/api/friend/addFriend',
    method: 'post',
    params: {
      id: id
    }
  })
}
export const getFriendList = (data) => {
  return request({
    url: '/api/friend/getFriendList',
    method: 'post',
    data
  })
}
export const deleteFriend = (id) => {
  return request({
    url: '/api/friend/deleteFriend',
    method: 'delete',
    params: {
      friendUserId: id
    }
  })
}

// 得到私聊消息
export const listPrivateMessages = (fromId, toId) => {
  return request({
    url: '/api/chat_message/listMessages',
    method: 'get',
    params: {
      fromId: fromId,
      toId: toId
    }
  })
}
// 删除所有私聊消息
export function deleteAllMsg (fromUsername, toUsername) {
  return request({
    url: `/PrivateMessageController/deleteAllMsg/${fromUsername}/${toUsername}`,
    method: 'delete'
  })
}
