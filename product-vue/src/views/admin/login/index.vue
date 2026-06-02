<template>
  <div class="login-page">
    <div class="card">
      <h2>校园墙管理后台</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="admin@test.com"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width:100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {useAdminStore} from '@/store/admin'

const router = useRouter()
const store = useAdminStore()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({email: '', password: ''})
const rules = {
  email: [{required: true, message: '请输入邮箱'}],
  password: [{required: true, message: '请输入密码'}]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => {})
  if (!valid) return
  loading.value = true
  const res = await store.login(form.email, form.password)
  loading.value = false
  if (res.code === 200) {
    ElMessage.success('登录成功')
    router.replace('/admin/dashboard')
  } else {
    ElMessage.error(res.msg || '登录失败')
  }
}
</script>

<style scoped>
.login-page {
  height: 100vh; display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.card {
  background: #fff; padding: 40px; border-radius: 8px; width: 400px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}
h2 { text-align: center; margin-bottom: 30px; color: #333; }
</style>
