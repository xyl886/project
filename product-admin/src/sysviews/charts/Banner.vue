<template>
  <div class="">
    <!--工具条-->
    <!--    <el-col :span="24" class="toolbar" style="padding-bottom: 0;">-->
    <!--      <el-row :gutter="10" class="mb8">-->
    <!--        <el-col :span="1.5">-->
    <!--          <el-button type="primary" icon="el-icon-" size="small" @click="handleCreate">新增</el-button>-->
    <!--        </el-col>-->
    <!--        <el-col :span="1.5">-->
    <!--          <el-button-->
    <!--            :disabled="!multipleSelection.length"-->
    <!--            type="danger"-->
    <!--            icon="el-icon-delete"-->
    <!--            size="small"-->
    <!--            @click="handleDelete">批量删除</el-button>-->
    <!--        </el-col>-->
    <!--      </el-row>-->
    <!--    </el-col>-->

    <el-row>
      <el-col v-for="(item,index) in banners" :span="8" :key="index" style="width: calc(25%);padding: 20px">
        <el-card :body-style="{ padding: '0px' }" style="">
          <img :src="item.imgPath" :alt="item.remark" style="cursor: pointer" class="image" @click="handleEdit(item)">
          <div style="padding: 14px;">
            <div class="bottom clearfix">
              <time class="time">{{ item.createTime }}</time>
              <el-button style="margin-left:40px;padding:0;border:#ffffff;cursor: pointer" class="el-icon-edit-outline" @click.native="handleEdit(item)"/>
              <el-button style="border:#ffffff;padding:0;cursor: pointer" class="el-icon-delete" @click.native="handleDelete(item)"/>
              <!--              <el-dropdown style="float: right;">-->
              <!--                <span><i class="el-icon-more" style="padding: 5px;"/></span>-->
              <!--                <el-dropdown-menu>-->
              <!--                  <el-dropdown-item />-->
              <!--                  <el-dropdown-item />-->
              <!--                </el-dropdown-menu>-->
              <!--              </el-dropdown>-->
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col style="width: calc(25%);padding: 20px">
        <el-upload
          :before-upload="beforeUpload"
          :on-change="handleChange"
          :on-error="onError"
          :on-success="onSuccess"
          action="http://localhost:8087/api/banner/add"
          class="upload-demo"
          drag>
          <i class="el-icon-upload"/>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
<!--          <div slot="tip" class="el-upload__tip">上传头像图片大小不能超过 2MB!</div>-->
        </el-upload>
      </el-col>
    </el-row>
    <!--    编辑表单-->
    <el-dialog :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="editForm">
        <el-form-item>
          <el-upload
            ref="upload"
            :show-file-list="false"
            :auto-upload="true"
            :before-upload="beforeUpload"
            :on-change="handleChange"
            :on-remove="handleRemove"
            class="avatar-uploader">
            <img v-if="editForm.imgPath" :src="editForm.imgPath" style="text-align: center;width: 100%;display: block;" alt="">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="edit">确 定</el-button>
      </div>
    </el-dialog>
    <el-col :span="24" class="toolbar">
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination
          :current-page.sync="page.currentPage"
          :page-sizes="[10, 20, 40, 80]"
          :page-size="page.pageSize"
          :total="page.total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="sizeChange"
          @current-change="handleCurrentChange"/>
      </div>
    </el-col>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import { listAll, remove, update } from '../../api/banner'

export default {
  name: '',
  // import引入的组件需要注入到对象中才能使用
  components: {},
  // 这里存放数据
  data() {
    return {
      editForm: [],
      dialogFormVisible: false,
      showSearch: true,
      multipleSelection: [],
      currentDate: new Date(),
      banners: [],
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        tagName: null
      }
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 生命周期 - 创建完成（可以访问当前this实例）
  created() {
  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.listAllFun()
  },
  // 生命周期 - 创建之前
  beforeCreate() {
  },
  // 生命周期 - 挂载之前
  beforeMount() {
  },
  // 生命周期 - 更新之前
  beforeUpdate() {
  },
  // 生命周期 - 更新之后
  updated() {
  },
  // 生命周期 - 销毁之前
  beforeDestroy() {
  },
  // 生命周期 - 销毁完成
  destroyed() {
  },
  // 如果页面有keep-alive缓存功能，这个函数会触发
  activated() {
  },
  // 方法集合
  methods: {
    // beforeUpload(file) {
    //   const isLt2M = file.size / 1024 / 1024 < 2
    //   if (!isLt2M) {
    //     this.$message.error('上传头像图片大小不能超过 2MB!')
    //   }
    //   return isLt2M
    // },
    onSuccess(response, file, fileList) {
      // 上传成功的回调方法
      this.$message.success('文件上传成功！')
      this.listAllFun()
    },
    onError(err, file, fileList) {
      // 上传失败的回调方法
      this.$message.error('文件上传失败！')
      this.listAllFun()
    },
    handleRemove(file, fileList) {
      this.editForm.imgPath = null
      this.listAllFun()
      console.info(this.editForm)
    },
    handleChange(file, fileList) {
      console.info(file)
      console.info(fileList)
      this.editForm.file = file
      const URL = window.URL || window.webkitURL
      this.editForm.imgPath = URL.createObjectURL(file.raw)
      console.log('this.form.avatar:' + this.editForm.imgPath)
    },
    // 显示编辑界面
    handleEdit(row) {
      this.dialogFormVisible = true
      console.log(row)
      this.editForm = Object.assign({}, row)
      console.log(this.editForm)
    },
    // 提交
    edit() {
      this.dialogFormVisible = true
      const formData = new FormData()
      console.log(this.editForm)
      if (this.editForm.file) {
        formData.append('file', this.editForm.file.raw)
      }
      formData.append('id', this.editForm.id)
      update(formData).then(res => {
        if (res.code === 200) {
          this.editForm = []
          this.$message.success(res.msg)
          this.dialogFormVisible = false
          this.listAllFun()
        }
      }).catch((e) => {
        this.$message.error(e)
      })
    },
    sizeChange(pageSize) { // 页数
      this.page.pageSize = pageSize
      this.query()
    },
    handleCurrentChange(currentPage) { // 当前页
      this.page.currentPage = currentPage
      this.query()
    },
    handleCreate() {
      this.dialogFormVisible = true
      this.editForm = {
        tagName: ''
      }
    },
    listAllFun() {
      this.banners = []
      listAll().then(res => {
        if (res.code === 200) {
          this.banners = res.data
          console.log(this.banners)
        }
      })
    },
    handleDelete(item) {
      this.$confirm('你将要删除这张图片吗, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove(item.id).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.listAllFun()
          }
        }).catch(err => {
          console.log(err)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消删除'
        })
      })
    }
  }
}
</script>
<style scoped>
/deep/
.el-upload-dragger{
  width: 270px!important;
}
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  height: 120px;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>
