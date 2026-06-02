<template>
  <div class="login-wrapper">
    <div class="login-card">
      <div class="login-header">
        <div class="login-logo">📘</div>
        <h2>欢迎来到校园墙</h2>
        <p>登录你的账号，发现校园新鲜事</p>
      </div>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" size="large"
               @submit.prevent="handleLogin">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" prefix-icon="Message"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password/>
        </el-form-item>
        <el-form-item label="验证码" prop="verCode">
          <div style="display:flex;gap:12px;width:100%">
            <el-input v-model="form.verCode" placeholder="验证码" maxlength="4" style="flex:1"/>
            <img :src="captchaImg" @click="loadCaptcha"
                 style="width:120px;height:40px;border-radius:6px;cursor:pointer;border:1px solid #dcdfe6"/>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading" style="width:100%">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        还没有账号？
        <el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {useUserStore} from '@/store/user'
import {getCaptcha} from '@/api'

const route = useRoute()
const router = useRouter()
const store = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const captchaImg = ref('')

const form = ref({email: '', password: '', verCode: '', verKey: ''})
const rules = {
  email: [{required: true, message: '请输入邮箱', trigger: 'blur'}],
  password: [{required: true, message: '请输入密码', trigger: 'blur'}],
  verCode: [{required: true, message: '请输入验证码', trigger: 'blur'}]
}

async function loadCaptcha() {
  try {
    const res = await getCaptcha()
    if (res.code === 200) {
      form.value.verKey = res.data.key
      captchaImg.value = res.data.image
    }
  } catch {
    captchaImg.value = ''
  }
}

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  const res = await store.login(form.value.email, form.value.password, form.value.verCode, form.value.verKey)
  loading.value = false
  if (res.code === 200) {
    ElMessage.success('登录成功')
    const redirect = route.query.redirect || '/home'
    router.replace(redirect)
  } else {
    ElMessage.error(res.msg || '登录失败')
    loadCaptcha()
  }
}

onMounted(loadCaptcha)
</script>

<style scoped>
.login-wrapper {
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  width: 420px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, .15);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-logo {
  font-size: 56px;
  margin-bottom: 12px;
}

.login-header h2 {
  font-size: 24px;
  color: #1a1a2e;
  margin-bottom: 8px;
}

.login-header p {
  color: #999;
  font-size: 14px;
}

.login-footer {
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
  color: #999;
}
</style>
