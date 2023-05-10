import defaultSettings from '@/settings'

const title = defaultSettings.title || '天聊后台运营管理系统'

export default function getPageTitle (pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - 天聊后台运营管理系统`
  }
  return `${title}`
}
