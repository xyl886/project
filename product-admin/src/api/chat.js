import request from '@/utils/request'

// 得到私聊消息
export const listPrivateMessages = (fromId, toId) => {
  return request({
    url: '/api/message/listMessages',
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
