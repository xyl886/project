import Vue from 'vue'
import Router from 'vue-router'
import Login from '../sysviews/Login.vue'
import NotFound from '../sysviews/404.vue'
import Home from '../sysviews/Home.vue'

Vue.use(Router)
export const constantRoutes = [
  {
    path: '/',
    name: '主页',
    redirect: '/login',
    hidden: true
  },
  {
    path: '/login',
    component: Login,
    hidden: true
  },
  {
    path: '/404',
    component: NotFound,
    hidden: true
  },
  {
    path: '/index',
    component: Home,
    name: 'Charts',
    iconCls: 'el-icon-postcard',
    children: [
      { path: '/index/echarts', name: '数据统计', component: () => import('../sysviews/charts/echarts.vue') }
    ]
  },
  {
    path: '/index',
    component: Home,
    name: '帖子管理',
    iconCls: 'el-icon-postcard', // 图标样式class
    children: [
      { path: '/index/main', name: '帖子管理', component: () => import('../sysviews/post/Posts.vue') },
      { path: '/index/category', name: '分类管理', component: () => import('../sysviews/post/Category.vue') },
      { path: '/index/tags', name: '标签管理', component: () => import('../sysviews/post/Tags.vue') }
    ]
  },
  {
    path: '/index',
    component: Home,
    name: '系统管理',
    iconCls: 'el-icon-postcard',
    children: [
      { path: '/index/user', name: '用户管理', component: () => import('../sysviews/system/User.vue') },
      { path: '/index/role', name: '角色管理', component: () => import('../sysviews/system/Role.vue') }
    ]
  },
  {
    path: '/index',
    component: Home,
    name: '消息管理',
    iconCls: 'el-icon-message',
    children: [
      { path: '/index/message', name: '消息管理', component: () => import('../sysviews/message/message.vue') },
      { path: '/index/comment', name: '评论管理', component: () => import('../sysviews/message/comment.vue') },
      { path: '/index/feedback', name: '反馈管理', component: () => import('../sysviews/message/feedback.vue') }
    ]
  }
  // {
  //   path: '*',
  //   hidden: true,
  //   redirect: { path: '/404' }
  // }

  // {
  //   path: '/messageBoard',
  //   name: '留言板',
  //   component: Index,
  //   children: [{
  //     path: '',
  //     name: '留言板',
  //     component: () => import('../views/message_borad/Index')
  //   }]
  // },
  // {
  //   path: '/publish',
  //   name: '发布帖子',
  //   component: Index,
  //   children: [{
  //     path: '',
  //     name: '发布帖子',
  //     component: () => import('@/views/publish/index')
  //   }]
  // },
  // {
  //   path: '/detail',
  //   name: '帖子详情',
  //   component: () => import('@/views/posts/detail')
  // },
  // {
  //   path: '/Article',
  //   name: '帖子详情',
  //   component: () => import('@/views/posts/Article')
  // },
  // {
  //   path: '/chat',
  //   name: '私聊',
  //   component: Index,
  //   children: [{
  //     path: '',
  //     name: '私聊',
  //     component: () => import('@/views/chat/Chat')
  //   }]
  // },
  // {
  //   path: '/about-us',
  //   component: Index,
  //   children: [{
  //     path: '',
  //     name: '关于我们',
  //     component: () => import('@/views/about_us/index')
  //   }]
  // },
  // {
  //   path: '/personal',
  //   component: Index,
  //   redirect: '/personal/user_info',
  //   children: [{
  //     path: 'user_info',
  //     name: '个人中心',
  //     component: () => import('@/views/personal/index'),
  //     redirect: '/personal/user_info',
  //     children: [
  //       {
  //         path: '/personal/my_post',
  //         name: '我的帖子',
  //         component: () => import('@/views/personal/myPost/Index.vue')
  //       },
  //       {
  //         path: '/personal/user_info',
  //         name: '个人资料',
  //         component: () => import('@/views/personal/user_info.vue')
  //       },
  //       {
  //         path: '/personal/collect',
  //         name: '我的收藏',
  //         component: () => import('@/views/personal/collect.vue')
  //       },
  //       {
  //         path: '/personal/follow',
  //         name: '我的关注',
  //         component: () => import('@/views/personal/follow.vue')
  //       },
  //       {
  //         path: '/personal/fans',
  //         name: '我的粉丝',
  //         component: () => import('@/views/personal/fans.vue')
  //       },
  //       {
  //         path: '/personal/history',
  //         name: '浏览记录',
  //         component: () => import('@/views/personal/history.vue')
  //       }
  //     ]
  //   }]
  // }

]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
