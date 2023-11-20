<template>
  <div>
    <el-dialog title="修改密码" :visible.sync="dialogVisible">
      <el-form ref="passwordForm" :model="passwordForm" :rules="passwordRules" label-width="80px">
        <el-form-item label="用户邮箱">
          <el-input :disabled="true" v-model="userInfo.email"></el-input>
        </el-form-item>
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input type="password" v-model="passwordForm.currentPassword"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" placeholder="请输入新密码，由数字、字母和特殊字符组成，长度在 6 到 20 之间" v-model="passwordForm.newPassword"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" placeholder="请确认密码" v-model="passwordForm.confirmPassword"></el-input>
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
import {updateUserPwd} from '../../api/user_info'
import {mapGetters} from 'vuex'

export default {
  data () {
    return {
      name: 'updatePwd',
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
  computed: {
    ...mapGetters([
      'userInfo'
    ])
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
          updateUserPwd(this.passwordForm).then(res => {
            this.loading = false
            if (res.code === 200) {
              this.$notify({
                message: res.msg,
                type: 'success'
              })
              this.$store.dispatch('logout').then((res) => {
                if (res.code === 200) {
                  this.$router.push({path: '/'})
                }
              })
              this.dialogVisible = false
            }
          })
            .catch(error => {
              this.$notify({
                message: error,
                type: 'error'
              })
              this.dialogVisible = true
            })
            .finally(() =>
              loading.close()
            )
          this.dialogVisible = false
        }
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

</style>
