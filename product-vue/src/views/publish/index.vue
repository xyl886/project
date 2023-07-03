<template>
  <div style="font-size: 14px;display: flex;padding: 20px 20px;background: #f5f7f9;">
    <div style="flex: 1;background-color: #ffffff;border-radius: 5px;">
      <div style="padding: 24px;">
        <el-form ref="form" :rules="rules" :label-position="labelPosition" :model="form" label-width="200px" v-loading="loading">
          <!--          <el-form-item label="内容" prop="content">-->
          <!--            <el-input type="textarea" resize="none" v-model="form.content" clearable></el-input>-->
          <!--          </el-form-item>-->
          <el-form-item label="分类:" prop="school">
            <el-radio-group v-model="form.school">
              <el-radio-button label="1">学习</el-radio-button>
              <el-radio-button label="2">生活</el-radio-button>
              <el-radio-button label="3">娱乐</el-radio-button>
              <el-radio-button label="4">求助</el-radio-button>
              <el-radio-button label="5">就业</el-radio-button>
              <el-radio-button label="6">新闻/公告</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="类型:" prop="postsType">
            <el-radio-group v-model="form.postsType">
              <el-radio label="1">闲置帖</el-radio>
              <el-radio label="2">校园帖</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="标题:" prop="title">
            <el-input v-model="form.title" placeholder="请输入帖子标题，最多 60 字" clearable></el-input>
          </el-form-item>
          <el-form-item label="描述:" prop="description">
            <el-input v-model="form.description" placeholder="请简单介绍帖子的内容" clearable></el-input>
          </el-form-item>
          <el-form-item label="价格:" prop="price" v-show="form.postsType !== '2'">
            <el-input-number v-model="form.price" :controls='false' :min="0" :max="19999" clearable></el-input-number>&nbsp;元
          </el-form-item>
          <el-form-item label="内容:" prop="title">
            <!--            <quill-editor></quill-editor>-->
            <mavon-editor v-model="form.content" />
          </el-form-item>
          <el-form-item label="图片:" :required="form.postsType !== '2'">
            <el-upload
              action=""
              v-model="form.files"
              list-type="picture-card"
              :auto-upload="false"
              :limit="9"
              :before-upload="beforeUpload"
              :on-change="handleChange"
              :on-preview="handlePictureCardPreview"
              :disabled="disabled"
              :on-remove="handleRemove">
              <i class="el-icon-plus"></i>
            </el-upload>
            <div style="font-size: 12px;color: #666;">
              只能上传jpg/png文件,且不超过 2MB,最多上传 9 张图片
            </div>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
          </el-form-item>
          <el-form-item label="标签:">
            <el-tag
              :key="tag"
              v-for="tag in dynamicTags"
              closable
              :disable-transitions="false"
              @close="handleClose(tag)">
              {{tag}}
            </el-tag>
            <el-input
              class="input-new-tag"
              v-if="inputVisible"
              v-model="inputValue"
              ref="saveTagInput"
              size="small"
              @keyup.enter.native="handleInputConfirm"
              @blur="handleInputConfirm">
            </el-input>
            <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 添加标签</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('form')" class="el-icon-position">立即发布</el-button>
            <!--            <el-button @click="saveForm('form')">保存草稿</el-button>-->
            <el-button @click="resetForm('form')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <!--    <div style="width: 500px;background-color: #ffffff;margin: 0 50px 0 30px;">-->
    <!--      <div style="height: 50px;line-height: 50px;border-bottom: 1px solid #EEEEEE;text-align: center;font-weight: 700;font-size: 17px;color: rgb(64, 158, 255);">内容</div>-->
    <!--      <el-input v-model="form.title" clearable></el-input>-->
    <!--      <quill-editor v-model="form.content" :options="editorOptions"></quill-editor>-->
    <!--      <div class="rule" style="margin: 20px 20px;">-->
    <!--        <p>可发三种帖子闲置贴、求购贴、校园贴</p>-->
    <!--        <p>闲置贴：发布自己闲置的物品标题、内容、图片均要填写</p>-->
    <!--        <p>校园贴：分享在校园遇到的趣事,记录校园生活,标题、内容均要填写图片选填</p>-->
    <!--        <p>小提示：</p>-->
    <!--        <p>不允许发布不健康的东西,一经发现永久封号！！！！！</p>-->
    <!--      </div>-->
    <!--      <div style="text-align: center;margin-top: 50px;">-->
    <!--        <img src="../../../public/img/logo.png" style="width: 200px;">-->
    <!--      </div>-->
    <!--      <div>-->
    <!--        <div style="font-size: 40px;font-weight: 700;text-align: center;">TAOLVTC</div>-->
    <!--      </div>-->
    <!--    </div>-->
  </div>
</template>

<script>
import {add} from '@/api/posts'
// import top from '../top/index.vue'
export default {
  data () {
    return {
      loading: false,
      labelPosition: 'left',
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
      form: {
        postsType: '1',
        title: '',
        content: '',
        school: '1',
        price: 0,
        files: []
      },
      rules: {
        postsType: [
          { required: true, message: '请选择类型', trigger: 'change' }
        ],
        title: [
          { required: true, message: '请输入标题', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'change' }
        ],
        school: [
          { required: true, message: '请选择分类', trigger: 'change' }
        ],
        price: [
          { required: false, message: '请输入价格', trigger: 'change' }
        ]
      },
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false
    }
  },
  components: {
  },
  watch: {
    'form.postsType' () {
      if (this.form.postsType === '1') {
        this.rules['price'][0]['required'] = true
      } else {
        this.form.price = null
        this.rules['price'][0]['required'] = false
      }
    },
    'form.files' () {
      // console.info(this.form.files.length)
      if (this.form.files.length >= 9) {
        this.disabled = true
        this.$message.warning('最多只能上传9张图片')
      } else {
        this.disabled = false
      }
    }
  },
  mounted () {
    let that = this
    setInterval(function () { // 定位当前菜单
      that.activeIndex = that.$router.currentRoute.path
    }, 1000)
  },
  methods: {
    backFun () {
      this.$router.push({path: '/'})
    },
    beforeUpload: function (file) {
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
    },
    handleRemove (file, fileList) {
      this.form.files = fileList
      console.info(this.form)
    },
    handleChange (file, fileList) {
      this.form.files = fileList
      console.info(this.form)
    },
    handlePictureCardPreview (file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.form.postsType === '1' && this.form.files.length === 0) {
            this.$message.warning('至少上传1张图片')
            return false
          }
          this.loading = true
          let formData = new FormData()
          for (const file of this.form.files) { // 多个文件全部都放到files
            if (file.raw) {
              formData.append('files', file.raw)
            }
          }
          formData.append('postsType', this.form.postsType)
          formData.append('title', this.form.title)
          formData.append('description', this.form.description)
          formData.append('content', this.form.content)
          formData.append('school', this.form.school)
          formData.append('price', this.form.price)
          add(formData).then(res => {
            this.loading = false
            if (res.code === 200) {
              this.$message.success(res.msg)
              this.backFun()
              // this.resetForm('form');
            }
          }, error => {
            console.log(error)
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    handleClose (tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
    },

    showInput () {
      this.inputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },

    handleInputConfirm () {
      let inputValue = this.inputValue
      if (inputValue) {
        this.dynamicTags.push(inputValue)
      }
      this.inputVisible = false
      this.inputValue = ''
    },
    saveForm (formName) {
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>
<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
<style scoped>
.rule{
  opacity: 1;
  font-size: 16px;
}
.rule:hover{
  -webkit-animation: rule-hover 2s linear 0s 1 normal;
  animation: rule-hover 2s linear 0s 1 normal;
  animation-fill-mode:forwards;
}
/*@keyframes rule-hover {*/
/*  from {opacity: 0.2;}*/
/*  to {opacity: 1;}*/
/*}*/
</style>
