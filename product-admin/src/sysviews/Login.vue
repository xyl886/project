<template>
  <el-form :model="loginForm" :rules="rule" ref="loginForm" label-position="left" label-width="0px" class="demo-ruleForm login-container">
    <h3 class="title">系统登录</h3>
    <el-form-item prop="email">
      <el-input type="text" v-model="loginForm.email" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>
    <el-form-item style="width:100%;">
      <el-button type="primary" style="width:100%;" @click.native.prevent="handleSubmit" :loading="logining">登录</el-button>
<!--      <el-button @click.native.prevent="handleReset">重置</el-button>-->
    </el-form-item>
  </el-form>
</template>

<script>

import {userLogin} from "../api/login";

export default {
  data () {
    return {
      logining: false,
      loginForm: {
        email: '1803466516@qq.com',
        password: 'xyl010203',
        emailCode:''
      },
      rule: {
        email: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      checked: true
    }
  },
  methods: {
    handleReset () {
      this.$refs.loginForm.resetFields()
    },
    handleSubmit (ev) {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$store.dispatch('login', this.loginForm).then((res) => {
            if (res.code === 200) {
              this.$notify({
                title: '登录成功',
                message: res.data.nickname + '，欢迎您！',
                type: 'success'
              })
              this.$router.push('/index')
              this.logining = true
              this.$refs.loginForm.resetFields()
            }
          }).finally(() =>
            loading.close()
          )
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}

</script>

<style lang="scss" scoped>
  .login-container {
    /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .title {
      margin: 0px auto 40px auto;
      text-align: center;
      color: #505458;
    }
    .remember {
      margin: 0px 0px 35px 0px;
    }
  }
</style>
