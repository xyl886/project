<template>
  <div>
    <el-dialog title="修改密码" :visible.sync="dialogVisible">
      <el-form ref="passwordForm" :model="passwordForm" :rules="passwordRules" label-width="80px">
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input type="password" v-model="passwordForm.currentPassword"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="passwordForm.newPassword"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" v-model="passwordForm.confirmPassword"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updatePassword">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import {updatePwd} from '../../api/user_info'

export default {
  data () {
    return {
      dialogVisible: false,
      passwordForm: {
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        currentPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        confirmPassword: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.passwordForm.newPassword) {
                callback(new Error('两次输入的密码不一致'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    showDialog () {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.passwordForm.resetFields()
      })
    },
    updatePassword () {
      this.$refs.passwordForm.validate(valid => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: '修改中,请稍后。。。',
            spinner: 'el-icon-loading'
          })
          updatePwd(this.passwordForm).then(res => {
            console.log(res)
            if (res.code === 200) {
              console.log('密码修改成功')
              this.$store.dispatch('logout').then((res) => {
                if (res.code === 200) {
                  this.$notify({
                    title: '修改成功',
                    message: '请重新登录',
                    type: 'success'
                  })
                }
              })
              this.dialogVisible = false
            }
          }).finally(() =>
            loading.close()
          )
          // 执行修改密码的操作，例如调用API发送请求
          this.dialogVisible = false
        }
      })
    }
  },
  // import引入的组件需要注入到对象中才能使用
  components: {},

  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
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

</style>
