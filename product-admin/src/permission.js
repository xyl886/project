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

router.beforeEach(function(to, from, next) {
  // start progress bar
  if (router.currentRoute.path === to.path) {
    return
  }
  NProgress.start()
  const hasToken = getToken()
  const flag = true
  if (flag || hasToken) {
    next()
    NProgress.done()
  } else {
    next(false) // 停止路由导航
    NProgress.done()
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
