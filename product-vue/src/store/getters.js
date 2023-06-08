const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.my_token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  tagList: state => state.settings.tagList,
  tag: state => state.settings.tag,
  isFullScreen: state => state.settings.isFullScreen,
  lockPasswd: state => state.settings.lockPasswd,
  isLock: state => state.settings.isLock,
  themeName: state => state.settings.themeName,
  userInfo: state => state.user.userInfo
}
export default getters
