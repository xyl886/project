<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="rule" label-position="left" label-width="0px" class="demo-ruleForm login-container">
      <h3 class="title">系统登录</h3>
      <el-form-item prop="email">
        <el-input v-model="loginForm.email" type="text" auto-complete="off" placeholder="账号"/>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码"/>
      </el-form-item>
      <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button :loading="logining" type="primary" style="width:100%;" @click.native.prevent="handleSubmit">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

export default {
  data() {
    return {
      logining: false,
      loginForm: {
        email: '1803466516@qq.com',
        password: 'xyl010203',
        emailCode: ''
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
    handleReset() {
      this.$refs.loginForm.resetFields()
    },
    handleSubmit() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$store.dispatch('login', this.loginForm).then((res) => {
            if (res.code === 200) {
              this.$notify({
                title: '登录成功',
                message: res.data.nickname + '，欢迎您！',
                type: 'success'
              })
              this.$router.push('/index/echarts')
              this.logining = true
              this.$refs.loginForm.resetFields()
            }
          }).finally(() => {}
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
.login{
  background: url("../../public/img/ocean.jpg");
  width: 100%;
  height: 100%;
  position: absolute;
}
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
