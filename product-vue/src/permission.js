/**
 * 全站权限配置
 *
 */
import router from './router/index'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie

const whiteList = ['/login', '/index', '/', '/403', '/404', '/detail', '/about-us', '/share']// 白名单路由

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
    next({ path: '/login' })
    NProgress.done()
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
