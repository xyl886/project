<template>
  <!-- 修改弹窗 -->
  <div style="font-size: 14px;width: 100%;height: 100%;">
    <el-dialog
      title="修改信息"
      @close="close(form.files)"
      :visible.sync="dialogVisible"
      v-loading="loading"
      width="50%"
      :destroy-on-close="true"
      :close-on-click-modal="false"
      center>
      <div style="flex: 1;background-color: #ffffff;border-radius: 5px;">
        <el-form status-icon
                 :rules="rules"
                 ref="form"
                 :model="form"
                 label-width="120px">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入标题" clearable>
            </el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <el-input type="textarea" resize="none" v-model="form.content" placeholder="请输入内容" clearable>
            </el-input>
          </el-form-item>
          <el-form-item label="校区" prop="school">
            <el-radio-group v-model="form.school" >
              <el-radio label="1">官塘校区</el-radio>
              <el-radio label="2">社湾校区</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="图片" :required="form.postsType !== '2'">
            <el-upload
              action=""
              :headers="headers"
              ref="upload"
              list-type="picture-card"
              :auto-upload="false"
              :limit="9"
              :file-list="fileList"
              :before-upload="beforeUpload"
              :on-change="handleChange"
              :on-remove="handleRemove"
              :on-success="handleSuccess"
              :on-preview="handlePictureCardPreview"
              :disabled="disabled"
              :multiple="true">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="filedialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
            <div style="font-size: 12px;color: #666;">
              只能上传jpg/png文件,且不超过 2MB,最多上传 9 张图片
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updateMypost('form')" class="el-icon-position">提交</el-button>
            <el-button @click="resetForm('form')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import {getToken} from '../../utils/auth'
import {updateMypost} from '../../api/posts'

export default {
  name: 'UpdateDialog',
  // import引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    id: '',
    title: '',
    content: '',
    school: '',
    files: []
  },
  data () {
    // 这里存放数据
    return {
      loading: false,
      box: false,
      dialogVisible: false,
      form: {
        id: '',
        title: '',
        content: '',
        school: '',
        files: []
      },
      fileList: [],
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'change' }
        ],
        school: [
          { required: true, message: '请选择校区', trigger: 'change' }
        ],
        files: [
          { required: false, message: '请上传图片', trigger: 'change' }
        ]
      },
      headers: {
        Authorization: 'Bearer ' + getToken(),
        'Content-Type': 'multipart/form-data'
      },
      dialogImageUrl: '',
      filedialogVisible: false
    }
  },
  // 监听属性 类似于data概念
  computed: {
    disabled () {
      return this.loading || this.noMore
    }
  },
  // 监控data中的数据变化
  watch: {
    // 'form.postsType': function () {
    //   if (this.form.postsType == '1') {
    //     this.rules['price'][0]['required'] = true
    //   } else {
    //     this.form.price = null
    //     this.rules['price'][0]['required'] = false
    //   }
    // },
    'form.files': function () {
      console.info(this.form.files.length)
      if (this.form.files.length >= 9) {
        this.disabled = true
        this.$message.warning('最多只能上传9张图片')
      } else {
        this.disabled = false
      }
    }
  },
  // 方法集合
  methods: {
    showDialog () {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.resetFields()
      })
    },
    close () { // 关闭弹窗后清除表单数据
      this.$refs.form.resetFields()
      this.$refs.form.clearValidate()
      this.$refs.upload.clearFiles()
      this.fileList.splice(null, 1)
      this.box = false
    },
    beforeUpload (file) { // 上传前确认大小
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) { // >2MB
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
    },
    handleRemove (file, fileList) { // 移除
      this.form.files = fileList
      if (file.status === 'removed') { // 处理文件被移除的逻辑, 如果文件被移除了，可以在 fileList 中删除该文件
        const index = fileList.indexOf(file)
        if (index !== -1) {
          fileList.splice(index, 1)
        }
      }
    },
    handleChange (file, fileList) { // 更改
      this.form.files = fileList
      console.log('handleChange-file:' + JSON.stringify(file))
    },
    // 图片上传成功的回调
    handleSuccess (res, file, fileList) {
      this.fileList = fileList
      // 将上传成功的文件信息保存到form.images中
      this.fileList.push({
        name: res.name,
        url: res.url,
        uid: res.uid,
        status: 'success'
      })
    },
    handlePictureCardPreview (file) { // 点击预览
      // console.log(file)
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    updateMypost () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          console.log('updateMypost:')
          console.log(this.form.postsType)
          console.log(this.form.files.length)
          // eslint-disable-next-line no-undef
          if (this.form.postsType === '1' && !this.form.files.length && !this.fileList.length) {
            this.$message.warning('至少上传1张图片')
            return false
          }
          this.loading = true
          console.log('this.form.files:' + JSON.stringify(this.form.files))
          const formData = new FormData()
          for (const file of this.form.files) { // 多个文件全部都放到files
            if (file.raw) {
              formData.append('files', file.raw)
              console.log('file.raw:' + JSON.stringify(file.raw))
            }
          }
          formData.append('id', this.form.id)
          formData.append('title', this.form.title)
          formData.append('content', this.form.content)
          formData.append('school', this.form.school)
          console.log('formData:' + JSON.stringify(formData))
          console.log('formData:' + JSON.stringify(formData.get('files')))
          updateMypost(formData).then(res => {
            this.loading = false
            if (res.code === 200) {
              this.posts = res.data
              // 缓存更新
              // store.commit('SET_USER_INFO', res.data)
              this.$message.success(res.msg)
              this.box = false
              this.init()
            }
          }, error => {
            this.loading = false
            return Promise.reject(error)
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm () {
      this.$refs.form.resetFields()
      this.$refs.upload.clearFiles()
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
