<template>
    <div>
      <!-- 注册弹窗 -->
      <el-dialog
        title="User Register"
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
            <el-form-item prop="confirmPassword">
              <el-input size="small"
                        :type="passwordType"
                        v-model="loginForm.confirmPassword"
                        auto-complete="off"
                        placeholder="确认密码">
                <i class="el-icon-view el-input__icon" slot="suffix" @click="showPassword"/>
                <i slot="prefix" class="el-icon-lock el-icon--right"/>
              </el-input>
            </el-form-item>
            <el-form-item prop="gender">
              <el-select v-model="loginForm.gender" placeholder="选择性别" clearable style="width: 100%;">
                <el-option v-for="item in genders" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-row :span="24">
                <el-col :span="24" style="text-align: center;">
                  <el-button type="primary"
                             style="width: 100px;"
                             @click.native.prevent="handleLogin"
                             class="login-submit">
                    注册
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
import {userRegister} from '@/api/login'

export default {
  name: 'register_modal',
  data () {
    return {
      box: false,
      loginForm: {
        userName: '',
        // 密码
        password: '',
        confirmPassword: '',
        autoLogin: false
      },
      loginRules: {
        userName: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, message: '密码长度最少为6位', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请输入确认密码', trigger: 'blur'},
          {min: 1, message: '密码长度最少为6位', trigger: 'blur'}
        ],
        gender: [
          {required: true, message: '请选择性别', trigger: 'change'}
        ]
      },
      passwordType: 'password',
      genders: [
        {
          value: '0',
          label: '保密'
        },
        {
          value: '1',
          label: '男'
        },
        {
          value: '2',
          label: '女'
        }
      ]
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
              this.box = false
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
