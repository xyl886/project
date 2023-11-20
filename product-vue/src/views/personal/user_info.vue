<template>
    <div style="font-size: 14px;margin: 20px 20px;">
      <div style="display: flex;">
        <div style="width: 100px;height: 100px;">
          <img :src="user.avatar" alt="" style="width: 100px;height: 100px;border-radius: 50%;">
<!--          <el-image :src="user.avatar" ></el-image>-->
        </div>
        <div style="flex: 1;text-align: right;line-height: 100px;">
          <el-button type="primary" icon="el-icon-edit" circle @click="updateModal"></el-button>
        </div>
      </div>
      <div style="margin-top: 40px;">
        <el-row style="margin: 30px 0;">
          <el-col :span="3">
            <div style="font-size: 16px;">昵称</div>
          </el-col>
          <el-col :span="21">
            <div style="font-size: 16px;color: #666;">{{user.nickname}}</div>
          </el-col>
        </el-row>
        <el-row style="margin: 30px 0;">
          <el-col :span="3">
            <div style="font-size: 16px;">性别</div>
          </el-col>
          <el-col :span="21">
            <div style="font-size: 16px;color: #666;">{{user.genderText}}</div>
          </el-col>
        </el-row>
        <el-row style="margin: 30px 0;">
          <el-col :span="3">
            <div style="font-size: 16px;">爱好</div>
          </el-col>
          <el-col :span="21">
            <div style="font-size: 16px;color: #666;">{{user.hobby}}</div>
          </el-col>
        </el-row>
        <el-row style="margin: 30px 0;">
          <el-col :span="3">
            <div style="font-size: 16px;">关注</div>
          </el-col>
          <el-col :span="21">
            <div style="font-size: 16px;color: #666;">{{user.followNum}}</div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="3">
          <div style="font-size: 16px;">粉丝</div>
        </el-col>
          <el-col :span="21">
            <div style="font-size: 16px;color: #666;">{{user.fansNum}}</div>
          </el-col>
        </el-row>
        <el-row style="margin: 30px 0;">
          <el-col :span="3">
            <div style="font-size: 16px;">邮箱</div>
          </el-col>
          <el-col :span="21">
            <div style="font-size: 16px;color: #666;">{{user.email}}</div>
          </el-col>
        </el-row>
        <el-row style="margin: 30px 0;">
          <el-col :span="3">
            <div style="font-size: 16px;">个人简介</div>
          </el-col>
          <el-col :span="21">
            <div style="font-size: 16px;color: #666;">{{user.remark}}</div>
          </el-col>
        </el-row>
      </div>

      <!-- 修改弹窗 -->
      <el-dialog
        title="修改信息"
        top="5vh"
        @close="close(form.files)"
        :visible.sync="box"
        v-loading="loading"
        width="35%"
        :close-on-click-modal="false"
        center>
        <div>
          <el-form status-icon
                   :rules="rules"
                   ref="form"
                   :model="form"
                   label-width="120px">
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="form.nickname" placeholder="请输入昵称" clearable>
              </el-input>
            </el-form-item>

            <el-form-item label="头像：">
              <el-upload
                class="avatar-uploader"
                action=""
                style="border-radius: 50%"
                ref="upload"
                :show-file-list="false"
                :auto-upload="false"
                :before-upload="beforeUpload"
                :on-change="handleChange"
                :on-remove="handleRemove"
              >
                <img  style="border-radius: 50%" v-if="form.avatar" :src="form.avatar" class="avatar" alt="">
                <i v-else class="el-icon-plus avatar-uploader-icon" />
              </el-upload>
            </el-form-item>

            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio label="0">保密</el-radio>
                <el-radio label="1">男</el-radio>
                <el-radio label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="爱好" prop="hobby">
              <el-input v-model="form.hobby" placeholder="请输入爱好" clearable>
              </el-input>
            </el-form-item>
            <el-form-item label="个人简介" prop="remark">
              <el-input type="textarea" resize="none" v-model="form.remark" placeholder="请输入个人简介" clearable>
              </el-input>
            </el-form-item>

            <div style="text-align: center;">
              <el-button type="primary" style="width: 100px;" @click="updateFun">提交</el-button>
            </div>
          </el-form>
        </div>
      </el-dialog>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {detail, update} from '@/api/user_info'
import store from '@/store'

export default {
  data () {
    return {
      loading: false,
      user: {
        avatar: ''
      },
      box: false,
      form: {
        nickname: '',
        gender: '',
        file: null,
        hobby: '',
        remark: ''
      },
      rules: {
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'change' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  mounted () {
    this.user = this.userInfo
    console.log(this.user)
  },
  methods: {
    beforeUpload (file) {
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
    },
    handleRemove (file, fileList) {
      this.form.file = null
      console.info(this.form)
    },
    handleChange (file, fileList) {
      console.info(fileList)
      this.form.file = file
      let URL = window.URL || window.webkitURL
      this.form.avatar = URL.createObjectURL(file.raw)
      console.log('this.form.avatar:' + this.form.avatar)
    },
    detailFun () {
      detail().then(res => {
        if (res.code === 200) {
          this.user = res.data
          console.log(this.user)
        }
      }, error => {
        this.$message.error(error)
      })
    },
    updateModal () {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
      this.form = { ...this.user }
      this.form.gender = this.form.gender + ''
      console.info(this.form)
      this.box = true
    },
    updateFun () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          let formData = new FormData()
          if (this.form.file && this.form.file.raw) {
            formData.append('file', this.form.file.raw)
          }
          formData.append('nickname', this.form.nickname)
          formData.append('gender', this.form.gender)
          formData.append('hobby', this.form.hobby)
          formData.append('remark', this.form.remark)
          if (formData.has('file') || formData.has('nickname') || formData.has('gender') || formData.has('hobby') || formData.has('remark')) {
            update(formData).then(res => {
              this.loading = false
              if (res.code === 200) {
                this.user = res.data
                // 缓存更新
                store.commit('SET_USER_INFO', res.data)
                this.$message.success(res.msg)
                this.$refs.form.resetFields()
                this.box = false
              }
            }, error => {
              console.log('error submit!!', error)
              this.loading = false
            })
          } else {
            console.log('error submit!!')
            this.$message.warning('请填写至少一个字段')
            return false
          }
        } else {
          this.$message.warning('请填写必填字段')
        }
      })
    },
    close () { // 关闭弹窗后清除表单数据
      this.$refs.form.resetFields()
      this.$refs.form.clearValidate()
      this.$refs.upload.clearFiles()
      this.box = false
    }
  }
}
</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
