import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/login', name: 'Login', component: () => import('../views/login/index.vue'), meta: { noAuth: true, title: '登录' } },
  { path: '/register', name: 'Register', component: () => import('../views/register/index.vue'), meta: { noAuth: true, title: '注册' } },
  { path: '/home', name: 'Home', component: () => import('../views/home/index.vue'), meta: { noAuth: true, title: '首页' } },
  { path: '/post/:id', name: 'PostDetail', component: () => import('../views/postDetail/index.vue'), meta: { noAuth: true, title: '帖子详情' } },
  { path: '/user', name: 'User', component: () => import('../views/user/index.vue'), meta: { title: '个人中心' } },
  { path: '/user/:id', name: 'UserProfile', component: () => import('../views/profile/index.vue'), meta: { noAuth: true, title: '用户主页' } },
  { path: '/search', name: 'Search', component: () => import('../views/search/index.vue'), meta: { noAuth: true, title: '搜索' } },
  { path: '/create', name: 'Create', component: () => import('../views/create/index.vue'), meta: { title: '发布帖子' } },
  { path: '/messages', name: 'Messages', component: () => import('../views/messages/index.vue'), meta: { title: '消息中心' } },

  // Admin routes
  { path: '/admin/login', name: 'AdminLogin', component: () => import('@/views/admin/login/index.vue'), meta: { noAuth: true, title: '管理登录' } },
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAdmin: true },
    children: [
      { path: 'dashboard', name: 'AdminDashboard', component: () => import('@/views/admin/dashboard/index.vue'), meta: { title: '控制台' } },
      { path: 'posts', name: 'AdminPosts', component: () => import('@/views/admin/posts/index.vue'), meta: { title: '帖子管理' } },
      { path: 'users', name: 'AdminUsers', component: () => import('@/views/admin/users/index.vue'), meta: { title: '用户管理' } },
      { path: 'categories', name: 'AdminCategories', component: () => import('@/views/admin/categories/index.vue'), meta: { title: '分类管理' } },
      { path: 'comments', name: 'AdminComments', component: () => import('@/views/admin/comments/index.vue'), meta: { title: '评论管理' } },
      { path: 'tags', name: 'AdminTags', component: () => import('@/views/admin/tags/index.vue'), meta: { title: '标签管理' } },
    ]
  },

  // 404
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('../views/NotFound.vue'), meta: { noAuth: true, title: '页面未找到' } }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 })
})

router.beforeEach((to, from, next) => {
  // 更新页面标题
  document.title = to.meta.title ? `${to.meta.title} - 校园墙` : '校园墙'

  if (to.path.startsWith('/admin')) {
    if (to.meta.noAuth) return next()
    let info = null
    try { info = JSON.parse(localStorage.getItem('adminInfo')) } catch {}
    if (localStorage.getItem('token') && info?.role === 3) return next()
    return next('/admin/login')
  }
  if (!to.meta.noAuth && !localStorage.getItem('token')) return next('/login')
  next()
})

export default router
