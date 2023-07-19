import defaultSettings from '@/settings'
import {diff} from '@/utils/validate'
import {
  setStore,
  getStore,
  removeStore
} from '@/utils/store'

const { showSettings, fixedHeader, sidebarLogo } = defaultSettings

const fistPage = {
  'label': '欢迎',
  'value': '/wel',
  'params': {},
  'query': {},
  'meta': {'title': '欢迎', 'icon': 'dashboard'},
  'group': [],
  'close': false
}
const tagObj = {
  label: '', // 标题名称
  value: '', // 标题的路径
  params: '', // 标题的路径参数
  query: '', // 标题的参数
  meta: {}, // 额外参数
  group: [] // 分组
}

const state = {
  showSettings: showSettings,
  tag: localStorage.getItem('tag') ? JSON.parse(localStorage.getItem('tag')) : tagObj,
  tagList: localStorage.getItem('tagList') ? JSON.parse(localStorage.getItem('tagList')) : [fistPage],
  fixedHeader: fixedHeader,
  sidebarLogo: sidebarLogo,
  isFullScren: false,
  lockPasswd: getStore({name: 'lockPasswd'}) || '',
  isLock: getStore({name: 'isLock'}) || false,
  themeName: getStore({name: 'themeName'}) || 'default'
}

// 处理首个标签
function setFistTag (list) {
  if (list.length === 1) {
    list[0].close = false
  } else {
    list.forEach(ele => {
      if (ele.value === '/wel') {
        ele.close = false
      } else {
        ele.close = true
      }
    })
  }
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    // eslint-disable-next-line no-prototype-builtins
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  },
  ADD_TAG: (state, action) => {
    state.tag = action
    localStorage.setItem('tag', JSON.stringify(state.tag))
    if (state.tagList.some(ele => diff(ele, action))) return
    // 健康浏览器路径屏蔽导航栏tag
    // if(action.value.indexOf('/desk/detail') == -1){
    state.tagList.push(action)
    // }
    setFistTag(state.tagList)
    localStorage.setItem('tagList', JSON.stringify(state.tagList))
  },
  DEL_TAG: (state, action) => {
    state.tagList = state.tagList.filter(item => {
      return !diff(item, action)
    })
    setFistTag(state.tagList)
    localStorage.setItem('tagList', JSON.stringify(state.tagList))
  },
  DEL_ALL_TAG: (state) => {
    state.tagList = [fistPage]
    localStorage.setItem('tagList', JSON.stringify(state.tagList))
  },
  DEL_TAG_OTHER: (state) => {
    state.tagList = state.tagList.filter(item => {
      if (item.value === state.tag.value) {
        return true
      } else if (item.value === '/wel') {
        return true
      }
    })
    setFistTag(state.tagList)
    localStorage.setItem('tagList', JSON.stringify(state.tagList))
  },
  SET_FULLSCREN: (state) => {
    state.isFullScren = !state.isFullScren
  },
  SET_LOCK_PASSWD: (state, lockPasswd) => {
    state.lockPasswd = lockPasswd
    setStore({
      name: 'lockPasswd',
      content: state.lockPasswd,
      type: 'session'
    })
  },
  SET_LOCK: (state) => {
    state.isLock = true
    setStore({
      name: 'isLock',
      content: state.isLock,
      type: 'session'
    })
  },
  CLEAR_LOCK: (state) => {
    state.isLock = false
    state.lockPasswd = ''
    removeStore({
      name: 'lockPasswd',
      type: 'session'
    })
    removeStore({
      name: 'isLock',
      type: 'session'
    })
  },
  SET_THEME_NAME: (state, themeName) => {
    state.themeName = themeName
    setStore({
      name: 'themeName',
      content: state.themeName
    })
  }
}

const actions = {
  changeSetting ({ commit }, data) {
    commit('CHANGE_SETTING', data)
  },
  ADD_TAG ({ commit }, data) {
    commit('ADD_TAG', data)
  },
  DEL_TAG ({ commit }, data) {
    commit('DEL_TAG', data)
  },
  DEL_ALL_TAG ({ commit }, data) {
    commit('DEL_ALL_TAG', data)
  },
  DEL_TAG_OTHER ({ commit }, data) {
    commit('DEL_TAG_OTHER', data)
  },
  SET_FULLSCREN ({ commit }, data) {
    commit('SET_FULLSCREN', data)
  },
  SET_LOCK_PASSWD ({ commit }, data) {
    commit('SET_LOCK_PASSWD', data)
  },
  CLEAR_LOCK ({ commit }, data) {
    commit('CLEAR_LOCK', data)
  },
  SET_THEME_NAME ({ commit }, data) {
    commit('SET_THEME_NAME', data)
  }
}

export default {
  state,
  mutations,
  actions
}
