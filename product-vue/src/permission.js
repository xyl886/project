/**
 * 全站权限配置
 *
 */
import router from './router/index'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth'
import Vue from 'vue' // get token from cookie
Vue.prototype.$bus = new Vue()
const whiteList = ['/index', '/', '/403', '/404', '/detail', '/about-us', '/share']// 白名单路由

router.beforeEach(function (to, from, next) {
  // start progress bar
  if (router.currentRoute.path === to.path) {
    return
  }
  NProgress.start()
  const hasToken = getToken()
  let flag = false
  for (let i = 0; i < whiteList.length; i++) { // 白名单
    if (to.path === whiteList[i]) {
      flag = true
    }
  }
  if (flag || hasToken) {
    next()
    NProgress.done()
  } else {
    Vue.prototype.$bus.$emit('showLoginDialog')
    next(false) // 停止路由导航
    NProgress.done()
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
