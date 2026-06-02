<template>
  <div class="register-wrapper">
    <div class="register-card">
      <div class="register-header">
        <div class="register-logo">📝</div>
        <h2>注册校园墙账号</h2>
        <p>加入校园墙，发现校园新鲜事</p>
      </div>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" size="large"
               @submit.prevent="handleRegister">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" maxlength="20"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="至少6位，字母+数字" show-password/>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="再次输入密码" show-password/>
        </el-form-item>
        <el-form-item label="邮箱验证码" prop="emailCode">
          <div style="display:flex;gap:12px;width:100%">
            <el-input v-model="form.emailCode" placeholder="验证码" maxlength="6" style="flex:1"/>
            <el-button :disabled="sendingCode || codeCooldown > 0" @click="sendCode" style="width:120px">
              {{ codeCooldown > 0 ? `${codeCooldown}s` : '获取验证码' }}
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="图形验证码" prop="verCode">
          <div style="display:flex;gap:12px;width:100%">
            <el-input v-model="form.verCode" placeholder="验证码" maxlength="4" style="flex:1"/>
            <img :src="captchaImg" @click="loadCaptcha"
                 style="width:120px;height:40px;border-radius:6px;cursor:pointer;border:1px solid #dcdfe6"/>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading" style="width:100%">注 册</el-button>
        </el-form-item>
      </el-form>
      <div class="register-footer">
        已有账号？
        <el-link type="primary" @click="$router.push('/login')">立即登录</el-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {getCaptcha, sendEmailCode, registerUser} from '@/api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const sendingCode = ref(false)
const codeCooldown = ref(0)
const captchaImg = ref('')
let timer = null

const form = reactive({
  email: '',
  nickname: '',
  password: '',
  confirmPassword: '',
  emailCode: '',
  verCode: '',
  verKey: ''
})

const rules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { pattern: /^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{6,20}$/, message: '密码需6-20位，包含字母和数字', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (rule, val, cb) => val === form.password ? cb() : cb(new Error('两次密码不一致')), trigger: 'blur' }
  ],
  emailCode: [{ required: true, message: '请输入邮箱验证码', trigger: 'blur' }],
  verCode: [{ required: true, message: '请输入图形验证码', trigger: 'blur' }]
}

async function loadCaptcha() {
  try {
    const res = await getCaptcha()
    if (res.code === 200) {
      form.verKey = res.data.key
      captchaImg.value = res.data.image
    }
  } catch { captchaImg.value = '' }
}

async function sendCode() {
  if (!form.email) { ElMessage.warning('请先输入邮箱'); return }
  sendingCode.value = true
  try {
    const res = await sendEmailCode(form.email, '1')
    if (res.code === 200) {
      ElMessage.success('验证码已发送，请查收邮件')
      codeCooldown.value = 60
      timer = setInterval(() => {
        codeCooldown.value--
        if (codeCooldown.value <= 0) { clearInterval(timer); timer = null }
      }, 1000)
    } else {
      ElMessage.error(res.msg || '发送失败')
    }
  } catch { ElMessage.error('发送失败，请重试') }
  finally { sendingCode.value = false }
}

async function handleRegister() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await registerUser({
      email: form.email,
      password: form.password,
      confirmPassword: form.confirmPassword,
      emailCode: form.emailCode,
      verCode: form.verCode,
      verKey: form.verKey
    })
    if (res.code === 200) {
      ElMessage.success('注册成功')
      router.push('/login')
    } else {
      ElMessage.error(res.msg || '注册失败')
      loadCaptcha()
    }
  } catch { ElMessage.error('注册失败') }
  finally { loading.value = false }
}

onMounted(loadCaptcha)
</script>

<style scoped>
.register-wrapper {
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}
.register-card {
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  width: 460px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, .15);
}
.register-header {
  text-align: center;
  margin-bottom: 32px;
}
.register-logo {
  font-size: 56px;
  margin-bottom: 12px;
}
.register-header h2 {
  font-size: 24px;
  color: #1a1a2e;
  margin-bottom: 8px;
}
.register-header p {
  color: #999;
  font-size: 14px;
}
.register-footer {
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
  color: #999;
}
</style>
