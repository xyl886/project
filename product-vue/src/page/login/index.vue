<template>
  <div id="body">
    <div style="display: flex;width: 100%;height: 100%;overflow: hidden;">
      <div style="height: 50px;line-height: 50px;display: flex;">
        <el-button type="text" style="margin-left: 10px; color: #000c17" @click="backFun">&lt;返回</el-button>
      </div>
      <div class="login-modal">
        <div class="title">
          {{loginType === 'login'?'登录':(loginType==='forget'?'重置密码':'注册')}}
        </div>
        <el-form class="login-form"
                 :rules="loginRules"
                 ref="loginForm"
                 :model="loginForm"
                 label-width="0">

          <el-form-item prop="phone">
            <el-input
              placeholder="请输入手机号"
              prefix-icon="el-icon-mobile-phone"
              v-model.number="loginForm.phone"
              clearable>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              :type="passwordType"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
              v-model="loginForm.password"
              clearable>
            </el-input>
          </el-form-item>

          <el-form-item prop="confirmPassword" v-show="loginType === 'register'">
            <el-input
              :type="passwordType"
              placeholder="请再次输入密码"
              prefix-icon="el-icon-lock"
              v-model="loginForm.confirmPassword"
              clearable>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-row :span="24">
              <el-col :span="12">
                <el-checkbox v-model="loginForm.rememberPwd" @change="handleChange">记住密码</el-checkbox>
              </el-col>
              <el-col :span="12">
                <el-popover
                  placement="top-start"
                  title=""
                  width="200"
                  trigger="hover"
                  content="忘记密码请联系系统管理员">
                  <span style="color: #1890ff;float: right;" slot="reference">忘记密码</span>
                </el-popover>
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item>
            <el-button :type="loginType === 'login'?'success':'danger'"
                       style="width: 100%;"
                       @click.native.prevent="handleLogin"
                       class="login-submit">
              {{loginType === 'login'?'登录':(loginType==='forget'?'重置密码':'注册')}}
            </el-button>
          </el-form-item>
          <div v-if="loginType === 'login'" style="text-align: center;font-size: 14px;">
            没有账号？<span style="cursor: pointer;color: #df1f20;" @click="changeModalType('register')">免费注册</span>
          </div>
          <div v-if="loginType !== 'login'" style="text-align: center;font-size: 14px;">
            已有账号？<span style="cursor: pointer;color: #df1f20;" @click="changeModalType('login')">返回登录</span>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import {userRegister} from '@/api/login'
import {mapGetters} from 'vuex'

export default {
  name: 'index',
  data () {
    return {
      loginType: 'login',
      passwordType: 'password',
      loginForm: {
        phone: '',
        password: '',
        confirmPassword: '',
        rememberPwd: false
      },
      loginRules: {
        phone: [
          { required: true, message: '请输入手机号', trigger: ['blur', 'change'] },
          { pattern: /^1[3456789]\d{9}$/, message: '手机号格式错误', trigger: ['blur', 'change'] }
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'change'},
          {min: 6, message: '密码长度最少为6位', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: false, message: '请再次输入密码', trigger: 'change'},
          {min: 6, message: '密码长度最少为6位', trigger: 'blur'}
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  mounted () {
    // 在组件挂载时，检查是否已记住密码
    // 从 localstorage 中获取数据并解密
    this.loginForm.phone = localStorage.getItem('phone')
    this.loginForm.password = localStorage.getItem('password')
  },
  watch: {
    // 当“记住密码”复选框被勾选或取消勾选时，保存或清除密码到localStorage中
    'loginForm.rememberPwd' () {
      if (this.loginForm.rememberPwd === true) {
        // 将加密后的数据存储到 localstorage 中
        const encryptedPhone = this.loginForm.phone
        const encryptedPwd = this.loginForm.password
        localStorage.setItem('phone', encryptedPhone)
        localStorage.setItem('password', encryptedPwd)
      } else {
        localStorage.removeItem('phone')
        localStorage.removeItem('password')
      }
    }
  },
  methods: {
    handleChange () {
      console.log(this.loginForm.rememberPwd ? '选中' : '未选中')
    },
    backFun () {
      this.$router.push({path: '/'})
    },
    homePage () {
      this.$router.push({path: '/index'})
    },
    showPassword () {
      this.passwordType === ''
        ? (this.passwordType = 'password')
        : (this.passwordType = '')
    },
    changeModalType (type) {
      this.loginType = type
      this.$refs.loginForm.resetFields()
      if (type === 'login') {
        this.loginRules['confirmPassword'][0]['required'] = false
      } else {
        this.loginRules['confirmPassword'][0]['required'] = true
      }
    },
    handleLogin () {
      if (this.loginType === 'login') {
        this.login()
      } else if (this.loginType === 'register') {
        this.register()
      }
    },
    login () { // 登录
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: '登录中,请稍后。。。',
            spinner: 'el-icon-loading'
          })
          this.$store.dispatch('login', this.loginForm).then((res) => {
            if (res.code === 200) {
              this.$notify({
                title: '登录成功',
                message: res.data.nickname + '，欢迎您！',
                type: 'success'
              })
              this.$router.push({path: '/'})
            }
          }).finally(() =>
            loading.close()
          )
        }
      })
    },
    register () { // 注册
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: '注册中,请稍后。。。',
            spinner: 'el-icon-loading'
          })
          userRegister(this.loginForm).then(res => {
            if (res.code === 200) {
              this.$notify({
                title: '注册成功',
                message: '请登录',
                type: 'success'
              })
            }
          }).finally(() =>
            loading.close()
          )
        }
      })
    }
  }
}
</script>

<style scoped>
  #body{
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
    background-size: 100% 100%;
    background-image: linear-gradient(to top, rgba(255, 95, 45, 0.27), rgba(211, 155, 5, 0.2)), url("../../../public/img/login-bg.png");
    background-repeat: no-repeat;
  }
  .name{
    line-height: 50px;
    font-size: 30px;
    font-weight: 700;
    color: #FFFFFF;
    margin-left: 10px;
  }
  .login-modal{
    position: relative;
    width: 420px;
    height: 450px;
    top: 50%;
    margin: -225px auto 0;
    background-color: #FFFFFF;
    border-radius: 5px;
  }
  .title{
    height: 80px;
    line-height: 100px;
    font-weight: 600;
    text-align: center;
    font-size: 25px;
  }
  .login-form{
    margin: 20px 40px;
  }
</style>
