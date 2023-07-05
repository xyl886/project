import request from '@/utils/request'

export function getAllUsers (username) {
  return request({
    url: '/ChatUserController/getAllUsers',
    method: 'get',
    params: {
      username: username
    }
  })
}
// 得到私聊消息
export function listPrivateMessages (fromUsername, toUsername) {
  return request({
    url: `/PrivateMessageController/listPrivateMessages/${fromUsername}/${toUsername}`,
    method: 'get'
  })
}

// 删除所有私聊消息
export function deleteAllMsg (fromUsername, toUsername) {
  return request({
    url: `/PrivateMessageController/deleteAllMsg/${fromUsername}/${toUsername}`,
    method: 'delete'
  })
}
