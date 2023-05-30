<template>
  <el-dialog
        title="修改信息"
        @close="close(form.files)"
        :visible.sync="dialogVisible"
        v-loading="loading"
        width="50%"
        :top="'2vh'"
        :destroy-on-close="true"
        :close-on-click-modal="false"
        center>
  <el-form
    :model="form"
    :rules="rules"
    ref="form"
    label-width="100px"
    class="demo-ruleForm"
    @submit.native.prevent
    style="height: auto; padding-top: 0 !important"
  >
    <el-form-item label="标题" prop="title">
      <el-input
        v-model="form.title"
        maxlength="30"
        style="max-width: 700px; min-width: 50px"
      ></el-input>
    </el-form-item>
    <el-form-item label="内容" prop="content">
      <el-input
        type="textarea"
        v-model="form.content"
        :autosize="{ minRows: 6, maxRows: 6 }"
        maxlength="600"
        style="max-width: 700px; min-width: 50px"
      ></el-input>
    </el-form-item>
    <el-form-item label="校区" prop="school">
      <el-radio-group v-model="form.school">
        <el-radio-button v-for="tab in tabs" :key="tab.name" :label="tab.name">{{ tab.label }}</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="价格" prop="price">
      <el-input
        v-model="form.price"
        maxlength="13"
        style="max-width: 700px; min-width: 50px"
      ></el-input>
    </el-form-item>
    <el-form-item label="图片" required>
      <el-upload
        accept=".jpg,.png,.jpeg"
        action=""
        list-type="picture-card"
        :on-remove="handleRemove"
        :on-change="handleChange"
        :auto-upload="false"
        :limit="9"
        :fileList="fileList"
        ref="uploadPhoto"
        multiple
      >
        <i class="el-icon-plus"></i>
        <div slot="tip" class="el-upload__tip">
          只能上传jpg/png/jpeg文件,且不超过 2MB,最多上传 9 张图片
        </div>
      </el-upload>
    </el-form-item>
    <el-form-item size="large">
      <el-button
        type="primary"
        @click="submitUpload()"
        :loading="loading"
      >提交修改</el-button
      >
      <el-button @click.native="reset">重置</el-button>
    </el-form-item>
  </el-form>
      </el-dialog>
</template>

<script>
import {updateMypost} from '../../api/posts'
export default {
  props: {
    Form: {
      type: Object,
      default: function () {
        return {}
      }
    }
  },
  data () {
    return {
      dialogVisible: false,
      loading: false,
      tabs: [
        { label: '学习', name: '1' },
        { label: '生活', name: '2' },
        { label: '娱乐', name: '3' },
        { label: '求助', name: '4' },
        { label: '就业', name: '5' },
        { label: '新闻/公告', name: '6' }
      ],
      form: {
        title: '',
        content: '',
        school: '',
        price: '',
        files: []
      },
      fileList: [],
      removeFileList: [],
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          {min: 3, max: 100, message: '长度在 3 到 100 个字符', trigger: 'blur'}
        ],
        content: [{ required: true, message: '请填写内容', trigger: 'blur' },
          {min: 10, max: 600, message: '长度在 10 到 600 个字符', trigger: 'blur'}
        ],
        school: [{ required: true, message: '请选择发布校区', trigger: 'change' }],
        price: [{ required: true, message: '请输入价格 , 价格范围 : 0.01~10000', trigger: 'blur' }]
      }
    }
  },
  // created () {
  //   this.edit()
  // },
  mounted () {
    this.edit()
  },
  methods: {
    showDialog () {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.resetFields()
        this.edit()
      })
    },
    close () { // 关闭弹窗后清除表单数据-->
      this.$refs.form.resetFields()
      this.$refs.form.clearValidate()
      this.$refs.uploadPhoto.clearFiles()
      // this.fileList.splice(null, 1)
      this.box = false
    },
    handleRemove (file, fileList) {
      if (file.status === 'success') {
        this.removeFileList.push(file) // 已经上传的图片可选择移除
      }
      this.fileList = fileList
    },
    handleChange (file, fileList) {
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$refs.uploadPhoto.handleRemove(file)
        this.$message.error('上传图片大小不能超过 2MB!')
        return false
      } else {
        this.fileList = fileList
      }
    },
    submitUpload () {
      this.$refs['form'].validate((valid) => {
        if (!/^([1-9]\d{0,9}|0)(\.\d{1,2})?$/.test(this.form.price) && this.form.price !== '') {
          this.$message({
            showClose: true,
            message: '价格格式错误 , 价格范围 : 0.01~10000',
            type: 'error'
          })
          return false
        }
        if (this.fileList.length === 0) {
          this.$message({
            showClose: true,
            message: '请至少上传一张图片',
            type: 'error'
          })
          return false
        }
        if (valid) {
          this.loading = true
          const formData = new FormData()
          // formData.append('files', null)
          // console.log('this.fileList:' + JSON.stringify(this.fileList))
          for (const file of this.fileList) { // 多个文件全部都放到files
            if (file.raw === undefined) {
              formData.append('files', file.url)
            } else {
              formData.append('newFiles', file.raw) // 新上传的照片
            }
          }
          this.removeFileList.forEach((item) => {
            formData.append('removeFiles', item) // 修改时移除的
          })
          formData.append('id', this.form.id)
          formData.append('userId', this.form.userId)
          formData.append('title', this.form.title)
          formData.append('content', this.form.content)
          formData.append('school', this.form.school)
          formData.append('price', this.form.price)
          console.log(formData)
          updateMypost(formData).then((res) => {
            const timer = setTimeout(() => {
              if (res.status === 200) {
                this.$message({
                  message: '修改成功！',
                  type: 'success'
                })
                this.loading = false
                this.edit()
              } else {
                this.$message({
                  message: '修改失败！',
                  type: 'error'
                })
                clearTimeout(timer)
              }
              this.$emit('back-reference', false)
            }, 500)
          })
        } else {
          return false
        }
      })
    },
    reset () {
      this.removeFileList = []
      this.edit()
    },
    edit () {
      this.fileList = []
      this.form.id = this.Form.id
      this.form.userId = this.Form.userId
      this.form.title = this.Form.title
      this.form.content = this.Form.content
      this.form.school = this.Form.school
      this.form.price = this.Form.price
      this.form.files = this.Form.imageList
      this.fileList = this.Form.imageList
      console.log(this.form)
    }
  }
}
</script>

<style scoped>
</style>
