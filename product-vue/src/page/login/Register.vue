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
        <el-form-item prop="password" v-if="loginType === 'emailCode'||formType==='register'">
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
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            v-model="loginForm.password"
            clearable>
          </el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword" v-if="formType === 'register'">
          <el-input
            :type="passwordType"
            placeholder="请再次输入密码"
            prefix-icon="el-icon-lock"
            v-model="loginForm.confirmPassword"
            clearable>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button :type="formType === 'login'?'primary':'danger'"
                     style="width: 100%;"
                     @click.native.prevent="handleLogin"
                     class="login-submit">
            {{formType === 'login'?'登录':(formType==='forget'?'重置密码':'注册')}}
          </el-button>
        </el-form-item>
        <el-row :span="24">
          <!--              <el-col :span="12">-->
          <!--              </el-col>-->
          <el-col :span="24">
            <div v-if="formType === 'login'" style="text-align: center;font-size: 14px;">
              没有账号？<span style="cursor: pointer;color: #df1f20;" @click="changeModalType('register')">免费注册</span>
            </div>
            <div v-if="formType !== 'login'" style="text-align: center;font-size: 14px;">
              已有账号？<span style="cursor: pointer;color: #df1f20;" @click="changeModalType('login')">返回登录</span>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </el-dialog>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
export default {
  name: '',
  // import引入的组件需要注入到对象中才能使用
  components: {},
  data () {
    // 这里存放数据
    return {}
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  methods: {
    showDialog () {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.loginForm.resetFields()
      })
    }
  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created () {

  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted () {

  },
  beforeCreate () {
  }, // 生命周期 - 创建之前
  beforeMount () {
  }, // 生命周期 - 挂载之前
  beforeUpdate () {
  }, // 生命周期 - 更新之前
  updated () {
  }, // 生命周期 - 更新之后
  beforeDestroy () {
  }, // 生命周期 - 销毁之前
  destroyed () {
  }, // 生命周期 - 销毁完成
  activated () {
  } // 如果页面有keep-alive缓存功能，这个函数会触发
}
</script>
<style scoped>

<
style >
