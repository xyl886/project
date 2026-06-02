import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElIcons from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import './assets/style.css'

const app = createApp(App)
app.use(ElementPlus, { size: 'default' })
app.use(createPinia())
app.use(router)
for (const [key, icon] of Object.entries(ElIcons)) app.component(key, icon)
app.mount('#app')
