<template>
    <div>
      <!-- 登录弹窗 -->
      <el-dialog
        title="User Login"
        :visible.sync="box"
        width="400px"
        :close-on-click-modal="false"
        center>
        <div>
          <el-form class="login-form"
                   status-icon
                   :rules="loginRules"
                   ref="loginForm"
                   :model="loginForm"
                   label-width="0">
            <el-form-item prop="userName">
              <el-input size="small"
                        v-model="loginForm.userName"
                        auto-complete="off"
                        placeholder="用户名">
                <i slot="prefix" class="el-icon-user el-icon--right"/>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input size="small"
                        :type="passwordType"
                        v-model="loginForm.password"
                        auto-complete="off"
                        placeholder="密码">
                <i class="el-icon-view el-input__icon" slot="suffix" @click="showPassword"/>
                <i slot="prefix" class="el-icon-lock el-icon--right"/>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-row :span="24">
                <el-col :span="12">
                  <el-checkbox v-model="loginForm.autoLogin">下次自动登录</el-checkbox>
                </el-col>
                <el-col :span="12" style="text-align: right;">
                  <el-button type="primary"
                             style="width: 100px;"
                             @click.native.prevent="handleLogin"
                             class="login-submit">
                    登录
                  </el-button>
                </el-col>
              </el-row>
            </el-form-item>

          </el-form>
        </div>
      </el-dialog>
    </div>
</template>

<script>
export default {
  name: 'login_modal',
  data () {
    return {
      box: false,
      loginForm: {
        userName: '',
        // 密码
        password: '',
        autoLogin: false
      },
      loginRules: {
        userName: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, message: '密码长度最少为6位', trigger: 'blur'}
        ]
      },
      passwordType: 'password'
    }
  },
  mounted () {
  },
  methods: {
    loginModal () {
      this.box = true
      if (this.$refs.loginForm) {
        this.$refs.loginForm.resetFields()
      }
    },
    showPassword () {
      this.passwordType === ''
        ? (this.passwordType = 'password')
        : (this.passwordType = '')
    },
    handleLogin () { // 登录
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
                message: res.data.userName + '，欢迎您！',
                type: 'success'
              })
              this.box = false
              // this.$router.push({path: '/'});
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

</style>
