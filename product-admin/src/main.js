// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Element from 'element-ui'
import store from './store'

import VueQuillEditor from 'vue-quill-editor'
// require styles 引入样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import 'mavon-editor/dist/css/index.css'
import MavonEditor from 'mavon-editor'
import '@/permission' // permission control
import 'font-awesome/css/font-awesome.min.css'
import '@/icons' // icon
Vue.use(Element)
Vue.use(MavonEditor)
Vue.use(VueQuillEditor)
Vue.config.productionTip = false

// Vue.config.errorHandler = (err) => {
//   console.log(err)
// }
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
