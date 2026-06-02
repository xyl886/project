import request from '@/utils/request'

export function getLineCount() {
  return request.get('/home/lineCount')
}

export function getHomeInit() {
  return request.get('/home/init')
}
