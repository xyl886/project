<template>
  <el-dialog class="custom-dialog" :visible.sync="dialogVisible"  :width="'450px'">

        <div class="login-modal">
          <div class="title">
            {{formType === 'login'?'登录':(formType==='forget'?'重置密码':'注册')}}
          </div>
          <el-form class="login-form"
                   :rules="currentLoginRules"
                   ref="loginForm"
                   :model="loginForm"
                   label-width="0">

            <el-form-item prop="email">
              <el-input
                placeholder="请输入邮箱"
                prefix-icon="el-icon-user"
                v-model="loginForm.email"
                clearable>
              </el-input>
            </el-form-item>
            <el-form-item prop="password" v-if="loginType === 'emailCode'||formType!=='login'">
              <el-col :span="16">
                <el-input
                  :type="passwordType"
                  placeholder="请输入验证码"
                  prefix-icon="el-icon-lock"
                  v-model="loginForm.emailCode"
                  clearable>
                </el-input>
              </el-col>
              <el-col :span="8">
                <el-button
                  style="float: right;width: 100px"
                  :type="'primary'"
                  :disabled="isSending"
                  @click="sendEmailCode">
                  {{ buttonText }}</el-button>
              </el-col>
            </el-form-item>
            <el-form-item prop="password" v-if="loginType === 'password'||formType==='register'">
              <el-input
                :type="passwordType"
                :placeholder="formType==='forget'?'请输入新密码':'请输入密码'"
                prefix-icon="el-icon-lock"
                v-model="loginForm.password"
                show-password
                clearable>
              </el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword" v-if="formType !== 'login'">
              <el-input
                :type="passwordType"
                placeholder="请再次输入密码"
                prefix-icon="el-icon-lock"
                v-model="loginForm.confirmPassword"
                clearable>
              </el-input>
            </el-form-item>

            <el-form-item v-if="formType==='login'">
              <el-row :span="24">
<!--                <el-col :span="12">-->
<!--                  <el-checkbox v-model="loginForm.rememberPwd"  @change="handleChange">记住密码</el-checkbox>-->
<!--                  <el-popover-->
<!--                    placement="top-start"-->
<!--                    title=""-->
<!--                    width="200"-->
<!--                    trigger="hover"-->
<!--                    content="忘记密码请联系系统管理员">-->
<!--                    <span style="cursor: pointer;color: #1890ff;float: left;" @click="changeModalType('forget')">忘记密码</span>-->
<!--                  </el-popover>-->
<!--                </el-col>-->
                <el-col :span="12" style="float: right">
                  <div v-if="formType === 'login'&&loginType ==='emailCode'" style="font-size: 14px;">
                    <span style="cursor: pointer;color: #1890ff;float: right;" @click="changeLoginType('password')">{{ loginTypeText }}</span>
                  </div>
                  <div v-if="formType === 'login'&&loginType ==='password'" style="font-size: 14px;">
                    <span style="cursor: pointer;color: #1890ff;float: right;" @click="changeLoginType('emailCode')">{{ loginTypeText }}</span>
                  </div>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item>
              <el-button :type="formType === 'login'?'primary':'danger'"
                         style="width: 100%;"
                         @click.native.prevent="handleLogin"
                         class="login-submit">
                {{formType === 'login'?'登录':(formType==='forget'?'重置密码':'注册')}}
              </el-button>
            </el-form-item>
            <el-col :span="24">
            <div v-if="formType === 'login'" style="text-align: center;font-size: 14px;">
              没有账号？<span style="cursor: pointer;color: #df1f20;" @click="changeModalType('register')">免费注册</span>
            </div>
            <div v-if="formType !== 'login'" style="text-align: center;font-size: 14px;">
              已有账号？<span style="cursor: pointer;color: #df1f20;" @click="changeModalType('login')">返回登录</span>
            </div>
            </el-col>
          </el-form>
        </div>
  </el-dialog>
</template>

<script>
import {userRegister} from '@/api/login'
import {mapGetters} from 'vuex'
import Vue from 'vue'
import {sendEmailCode} from '../../api/login'
import {userReset} from '../../api/user_info'

export default {
  name: 'index',
  data () {
    return {
      isSending: false, // 是否正在发送验证码
      remainingTime: 60, // 剩余时间，单位为秒
      dialogVisible: false,
      loginType: 'password',
      formType: 'login',
      passwordType: 'password',
      lastOperationTime: parseInt(localStorage.getItem('lastOperationTime')) || 0, // 上一次记住密码的时间
      loginForm: {
        email: '1803466516@qq.com',
        password: 'xyl010203',
        confirmPassword: '',
        emailCode: '',
        rememberPwd: false
      },
      loginRules: {
        email: [
          { required: true, message: '请输入邮箱', trigger: ['blur', 'change'] },
          { pattern: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/, message: '邮箱格式错误', trigger: ['blur', 'change'] }
        ],
        emailCode: [
          { required: true, message: '请输入验证码', trigger: ['blur', 'change'] },
          { len: 6, message: '验证码长度必须是6位', trigger: 'blur' }
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
    ...mapGetters(['userInfo']),
    loginTypeText () {
      return this.loginType === 'emailCode' ? '密码登录' : '验证码登录'
    },
    buttonText () {
      return this.isSending ? `${this.remainingTime}` : '获取验证码'
    },
    currentLoginRules () {
      if (this.loginType === 'password') {
        return {
          email: this.loginRules.email,
          password: this.loginRules.password
        }
      } else if (this.loginType === 'emailCode') {
        return {
          email: this.loginRules.email,
          emailCode: this.loginRules.emailCode
        }
      } else {
        return {
          email: this.loginRules.email,
          emailCode: this.loginRules.emailCode,
          password: this.loginRules.password,
          confirmPassword: this.loginRules.confirmPassword
        }
      }
    }
  },
  mounted () {
    Vue.prototype.$bus.$on('showLoginDialog', () => {
      this.dialogVisible = true
    })
    // 在组件挂载时，自动记住密码，期限一天,从 localstorage 中获取数据
    // if (this.formType === 'login' && this.loginType === 'password') {
    //   this.loginForm.email = localStorage.getItem('email')
    //   this.loginForm.password = localStorage.getItem('password')
    // } else {
    //   this.loginForm.email = ''
    //   this.loginForm.password = ''
    // }
  },
  watch: {
  },
  methods: {
    // rememberPwd (login) {
    //   const currentTime = Date.now()
    //   const timeInterval = currentTime - this.lastOperationTime
    //   const oneDayInMillis = 24 * 60 * 60 * 1000 // 一天的毫秒数
    //   if (timeInterval < oneDayInMillis) { // 时间间隔不满足一天，不执行操作
    //     return
    //   }
    //   localStorage.setItem('email', login.email)
    //   localStorage.setItem('password', login.password)
    //   this.lastOperationTime = currentTime
    //   localStorage.setItem('lastOperationTime', this.lastOperationTime.toString())
    // },
    sendEmailCode () {
      if (this.isSending) {
        return
      }
      if (!/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(this.loginForm.email)) {
        this.$message.warning('邮箱格式不正确！')
        return
      }
      if (this.loginForm.email) {
        const timer = setInterval(() => {
          if (this.remainingTime === 0) {
            clearInterval(timer)
            this.isSending = false
            this.remainingTime = 60
          } else {
            this.remainingTime--
          }
        }, 1000)
        sendEmailCode(this.loginForm.email, this.formType).then(res => {
          this.$message.warning(res.msg)
          this.isSending = true
        }).error(err => {
          this.$message.error(err)
        })
      } else {
        this.isSending = false
        this.$message.warning('请输入你的邮箱！')
      }
    },
    showDialog () {
      this.dialogVisible = true
      this.formType = 'login'
      this.loginType = 'password'
      this.$nextTick(() => {
        this.$refs.loginForm.resetFields()
      })
    },
    backFun () {
      this.$router.push({path: '/'})
    },
    homePage () {
      this.$router.push({path: '/index'})
    },
    // showPassword () {
    //   this.passwordType === '' ? (this.passwordType = 'password') : (this.passwordType = '')
    // },
    changeLoginType (type) {
      this.loginType = type
      console.log(this.loginType)
      this.$refs.loginForm.resetFields()
      this.loginForm.password = ''
      this.loginForm.emailCode = ''
      this.loginForm.confirmPassword = ''
    },
    changeModalType (type) {
      this.formType = type
      this.isSending = false
      console.log(this.formType)
      this.$refs.loginForm.resetFields()
      this.loginForm.email = ''
      this.loginForm.password = ''
      this.loginForm.emailCode = ''
      this.loginForm.confirmPassword = ''
      if (type === 'register') {
        this.loginForm.email = ''
        this.loginForm.password = ''
        this.loginRules['confirmPassword'][0]['required'] = true
      } else {
        this.loginForm.email = localStorage.getItem('email')
        // this.loginForm.password = localStorage.getItem('password')
        this.loginRules['confirmPassword'][0]['required'] = false
      }
    },
    handleLogin () {
      if (this.formType === 'login') {
        this.login()
      } else if (this.formType === 'register') {
        this.register()
      } else if (this.formType === 'forget') {
        this.forget()
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
              this.dialogVisible = false
              this.$refs.loginForm.resetFields()
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
            console.log(res)
            if (res.code === 200) {
              this.$notify({
                title: '注册成功',
                message: '请登录',
                type: 'success'
              })
              this.dialogVisible = false
            }
          }).finally(() =>
            loading.close()
          )
        }
      })
    },
    forget () {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: '重置中,请稍后。。。',
            spinner: 'el-icon-loading'
          })
          userReset(this.loginForm).then(res => {
            console.log(res)
            if (res.code === 200) {
              this.$notify({
                title: '重置成功',
                message: '请登录',
                type: 'success'
              })
              this.dialogVisible = false
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
  /*#body{*/
  /*  margin: 0;*/
  /*  padding: 0;*/
  /*  width: 100%;*/
  /*  height: 100%;*/
  /*  background-size: 100% 100%;*/
  /*  background-image: linear-gradient(to top, rgba(255, 95, 45, 0.27), rgba(211, 155, 5, 0.2)), url("../../../public/img/login-bg.png");*/
  /*  background-repeat: no-repeat;*/
  /*}*/
  /*.name{*/
  /*  line-height: 50px;*/
  /*  font-size: 30px;*/
  /*  font-weight: 700;*/
  /*  color: #FFFFFF;*/
  /*  margin-left: 10px;*/
  /*}*/
  .login-modal{
    position: relative;
    /*width: 420px;*/
    /*height: 450px;*/
    top: 50%;
    /*margin: -225px auto 0;*/
    background-color: #FFFFFF;
    border-radius: 5px;
  }
  .title{
   padding: 20px;
    /*line-height: 100px;*/
    font-weight: 600;
    text-align: center;
    font-size: 25px;
  }
  .login-form{
    margin: 20px 40px;
  }
  .custom-dialog .el-dialog__wrapper .el-dialog__body {
    padding:20px 20px; /* 根据需要设置新的 padding 值 */
  }
</style>
