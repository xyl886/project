import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/page/index/index'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/',
    name: '主页',
    redirect: '/index'
  },
  {
    path: '/index',
    component: Index,
    children: [{
      path: '',
      name: '主页',
      component: () => import('@/views/home/index')
    }]
  },
  {
    path: '/share',
    component: Index,
    children: [{
      path: '',
      name: '校园分享',
      component: () => import('@/views/share/index')
    }]
  },
  {
    path: '/messageBoard',
    name: '留言板',
    component: Index,
    children: [{
      path: '',
      name: '留言板',
      component: () => import('../views/message_borad/Index')
    }]
  },
  {
    path: '/publish',
    name: '发布帖子',
    component: Index,
    children: [{
      path: '',
      name: '发布帖子',
      component: () => import('@/views/publish/index')
    }]
  },
  {
    path: '/detail',
    name: '帖子详情',
    component: () => import('@/views/posts/detail')
  },
  {
    path: '/Article',
    name: '帖子详情',
    component: () => import('@/views/posts/Article')
  },
  {
    path: '/chat',
    name: '私聊',
    component: () => import('@/views/chat/Chat')
  },
  {
    path: '/about-us',
    component: Index,
    children: [{
      path: '',
      name: '关于我们',
      component: () => import('@/views/about_us/index')
    }]
  },
  {
    path: '/personal',
    component: Index,
    redirect: '/personal/user_info',
    children: [{
      path: 'user_info',
      name: '个人中心',
      component: () => import('@/views/personal/index'),
      redirect: '/personal/user_info',
      children: [
        {
          path: '/personal/my_post',
          name: '我的帖子',
          component: () => import('@/views/personal/myPost/Index.vue')
        },
        {
          path: '/personal/user_info',
          name: '个人资料',
          component: () => import('@/views/personal/user_info.vue')
        },
        {
          path: '/personal/collect',
          name: '我的收藏',
          component: () => import('@/views/personal/collect.vue')
        },
        {
          path: '/personal/follow',
          name: '我的关注',
          component: () => import('@/views/personal/follow.vue')
        },
        {
          path: '/personal/fans',
          name: '我的粉丝',
          component: () => import('@/views/personal/fans.vue')
        },
        {
          path: '/personal/history',
          name: '浏览记录',
          component: () => import('@/views/personal/history.vue')
        }
      ]
    }]
  }

]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter () {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
