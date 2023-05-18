<template>
    <div style="font-size: 14px;overflow:auto">
      <div v-for="(item,index) in posts" :key="item.id" class="share-item">
        <div style="width: 100px;">
          <div>
            <el-image :src="item.userInfo?item.userInfo.avatar:''" style="width: 80px;height: 80px;border-radius: 50%;"></el-image>
          </div>
        </div>
        <div style="flex: 1;">
          <div style="height: 80px;">
            <div style="font-size: 16px;line-height: 40px;">
              {{item.userInfo.nickname}}
              <div style="float: right;margin-top: 5px;"  v-if="item.userId == userInfo.id">
                <el-dropdown>
                  <div style="display: flex;">
                        <span style="line-height: 20px;font-size: 22px;">
                          <i class="el-icon-more"></i>
                        </span>
                  </div>
                  <el-dropdown-menu slot="dropdown">
                    <el-button type="primary" @click="updateModal(item, index)">修改</el-button>
                    <el-dropdown-item style="color:red;" @click.native="delMypost(item)">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown></div>
            </div>
            <div style="color: rgb(168, 176, 183);font-size: 12px;line-height: 30px;">
              {{item.createTime}}来自:{{item.schoolName}}
            </div>
          </div>
          <div class="share-item-content" :key="item.id" @click="detailFun(item)">
            <el-input type="textarea" resize="none" :autosize="true" :readonly="true" v-model="item.title"></el-input>
          </div>
          <div>
            <el-image v-for="(item2,index2) in item.imgPath?item.imgPath.split(','):[]" :key="item2" :preview-src-list="item.imgPath?item.imgPath.split(','):[]" fit="contain" :src="item2" style="width: 175px;height: 110px;border-radius: 5px;margin: 0 10px 10px 0;"></el-image>
          </div>
          <div style="display: flex;margin-top: 20px;">
            <div style="display: flex;cursor: pointer;" @click="likeFun(item)">
              <svg v-if="!item.like" t="1662040941431" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2409" width="20" height="20" class="icon"><path d="M190.193225 471.411583c14.446014 0 26.139334-11.718903 26.139334-26.13831 0-14.44499-11.69332-26.164916-26.139334-26.164916-0.271176 0-0.490164 0.149403-0.73678 0.149403l-62.496379 0.146333c-1.425466-0.195451-2.90005-0.295735-4.373611-0.295735-19.677155 0-35.621289 16.141632-35.621289 36.114522L86.622358 888.550075c0 19.949354 15.96767 35.597753 35.670407 35.597753 1.916653 0 3.808746 0.292666 5.649674 0l61.022819 0.022513c0.099261 0 0.148379 0.048095 0.24764 0.048095 0.097214 0 0.146333-0.048095 0.24457-0.048095l0.73678 0 0-0.148379c13.413498-0.540306 24.174586-11.422144 24.174586-24.960485 0-13.55983-10.760065-24.441669-24.174586-24.981974l0-0.393973-50.949392 0 1.450025-402.275993L190.193225 471.409536z" p-id="2410" data-spm-anchor-id="a313x.7781069.0.i1" fill="#99a2aa" class="selected"></path><path d="M926.52241 433.948343c-19.283182-31.445176-47.339168-44.172035-81.289398-45.546336-1.77032-0.246617-3.536546-0.39295-5.380544-0.39295l-205.447139-0.688685c13.462616-39.059598 22.698978-85.58933 22.698978-129.317251 0-28.349675-3.193739-55.962569-9.041934-82.542948l-0.490164 0.049119c-10.638291-46.578852-51.736315-81.31498-100.966553-81.31498-57.264215 0-95.466282 48.15065-95.466282 106.126063 0 3.241834-0.294712 6.387477 0 9.532097-2.996241 108.386546-91.240027 195.548698-196.23636 207.513194l0 54.881958-0.785899 222.227314 0 229.744521 10.709923 0 500.025271 0.222057 8.746198-0.243547c19.35686 0.049119 30.239721-4.817726 47.803749-16.116049 16.682961-10.761088 29.236881-25.50079 37.490869-42.156122 2.260483-3.341095 4.028757-7.075139 5.106298-11.20111l77.018118-344.324116c1.056052-4.053316 1.348718-8.181333 1.056052-12.160971C943.643346 476.446249 938.781618 453.944769 926.52241 433.948343zM893.82573 486.837924l-82.983993 367.783411-0.099261-0.049119c-2.555196 6.141884-6.879688 11.596106-12.872169 15.427364-4.177136 2.727111-8.773827 4.351098-13.414521 4.964058-1.49812-0.195451-3.046383 0-4.620227 0l-477.028511-0.540306-0.171915-407.408897c89.323375-40.266076 154.841577-79.670527 188.596356-173.661202 0.072655 0.024559 0.124843 0.049119 0.195451 0.072655 2.99931-9.137101 6.313799-20.73423 8.697079-33.164331 5.551436-29.185716 5.258771-58.123792 5.258771-58.123792-4.937452-37.98001 25.940812-52.965306 44.364417-52.965306 25.304316 0.860601 50.263777 33.656541 50.263777 52.326762 0 0 5.600555 27.563776 5.649674 57.190537 0.048095 37.366026-4.6673 56.847729-4.6673 56.847729l-0.466628 0c-5.872754 30.879288-16.214287 60.138682-30.464849 86.964654l0.36839 0.342808c-2.358721 4.815679-3.709485 10.220782-3.709485 15.943111 0 19.922748 19.088754 21.742187 38.765909 21.742187l238.761895 0.270153c0 0 14.666024 0.465604 14.690584 0.465604l0 0.100284c12.132318-0.638543 24.221658 5.207605 31.100322 16.409738 5.504364 9.016351 6.437619 19.6045 3.486404 28.988218L893.82573 486.837924z" p-id="2411" data-spm-anchor-id="a313x.7781069.0.i0" fill="#99a2aa" class="selected"></path><path d="M264.827039 924.31872c0.319272 0.024559 0.441045 0.024559 0.295735-0.024559 0.243547-0.048095 0.367367-0.074701-0.295735-0.074701s-0.539282 0.026606-0.271176 0.074701C264.43409 924.343279 264.532327 924.343279 264.827039 924.31872z" p-id="2412"></path></svg>
              <svg v-if="item.like" t="1662448748424" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2630" width="20" height="20" class="icon"><path d="M64 483.04V872c0 37.216 30.144 67.36 67.36 67.36H192V416.32l-60.64-0.64A67.36 67.36 0 0 0 64 483.04zM857.28 344.992l-267.808 1.696c12.576-44.256 18.944-83.584 18.944-118.208 0-78.56-68.832-155.488-137.568-145.504-60.608 8.8-67.264 61.184-67.264 126.816v59.264c0 76.064-63.84 140.864-137.856 148L256 416.96v522.4h527.552a102.72 102.72 0 0 0 100.928-83.584l73.728-388.96a102.72 102.72 0 0 0-100.928-121.824z" p-id="2631" data-spm-anchor-id="a313x.7781069.0.i6" fill="#00A1D6" class="selected"></path></svg>
              <span style="color: rgb(153, 162, 170);">{{item.likeNum>0?item.likeNum:'点赞'}}</span>
            </div>
            <div style="display: flex;margin-left: 50px;cursor: pointer;" @click="showComment(item)">
              <svg v-if="!item.comment" t="1662040970665" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4314" width="20" height="20" class="icon"><path d="M171.9296 740.7616a1269.76 1269.76 0 0 1-20.8896-36.352 408.7808 408.7808 0 1 1 150.528 157.8496l-19.2-11.2128-100.0448 27.2896a30.6688 30.6688 0 0 1-37.632-37.632l27.2384-99.9424z m62.5664 3.6352l-16.5376 60.672 60.672-16.5376a30.6688 30.6688 0 0 1 23.4496 3.072c10.3936 5.9904 20.736 12.032 31.0272 18.1248a347.4432 347.4432 0 1 0-128-134.2976c3.5328 6.656 12.3392 21.9136 26.1632 45.2608 4.2496 7.168 5.376 15.7184 3.2256 23.7056z m139.8784-67.7888a30.6688 30.6688 0 1 1 39.168-47.1552 152.5248 152.5248 0 0 0 97.9456 35.328c36.352 0 70.6048-12.5952 97.8944-35.328a30.6688 30.6688 0 1 1 39.2192 47.1552c-38.5024 32.0512-87.04 49.5616-137.1136 49.5104a213.8112 213.8112 0 0 1-137.1136-49.5104z" p-id="4315" data-spm-anchor-id="a313x.7781069.0.i5" fill="#99a2aa" class="selected"></path></svg>
              <svg v-if="item.comment" t="1662451658735" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2403" width="20" height="20" class="icon"><path d="M512 85.333333c235.637333 0 426.666667 191.029333 426.666667 426.666667S747.637333 938.666667 512 938.666667a424.778667 424.778667 0 0 1-219.125333-60.501334 2786.56 2786.56 0 0 0-20.053334-11.765333l-104.405333 28.48c-23.893333 6.506667-45.802667-15.413333-39.285333-39.296l28.437333-104.288c-11.008-18.688-18.218667-31.221333-21.802667-37.909333A424.885333 424.885333 0 0 1 85.333333 512C85.333333 276.362667 276.362667 85.333333 512 85.333333z m-102.218667 549.76a32 32 0 1 0-40.917333 49.216A223.178667 223.178667 0 0 0 512 736c52.970667 0 103.189333-18.485333 143.104-51.669333a32 32 0 1 0-40.906667-49.216A159.189333 159.189333 0 0 1 512 672a159.189333 159.189333 0 0 1-102.218667-36.906667z" p-id="2404" data-spm-anchor-id="a313x.7781069.0.i0" fill="#00A1D6" class="selected"></path></svg>
              <span style="color: rgb(153, 162, 170);">{{item.commentNum>0?item.commentNum:'评论'}}</span>
            </div>
          </div>
          <div v-show="item.comment" style="border-top: 1px solid rgb(229, 233, 239);border-bottom: 1px solid rgb(229, 233, 239);padding: 20px 0;margin-top: 20px;">
            <div style="display: flex;">
              <el-input type="textarea" :rows="1" resize="none" ref="textarea" v-model="item.commentContent" placeholder="请输入内容"></el-input>
                <el-popover placement="bottom" trigger="click">
                  <picker
                    class="emoji-mart"
                    :include="['people','Smileys']"
                    hidden :showSearch="false"
                    :showPreview="false"
                    :showCategories="false"
                    @select="addEmoji(item, $event)"
                    ref="picker"/>
                  <el-button slot="reference" style="line-height:1px;height: 33px;">😃</el-button>
                </el-popover>
              <el-button type="primary" style="width: 100px;line-height:1px;height:33px;margin-left: 10px;" @click="addCommentFun(item)">发布</el-button>
            </div>
          </div>

          <div v-show="item.comment" v-for="(item3,index3) in item.comments" :key="item3.id" style="border-bottom: 1px solid rgb(229, 233, 239);display: flex;padding-top: 20px;">
            <div style="width: 50px;">
              <div>
                <el-image :src="item3.userInfo?item3.userInfo.avatar:''" style="width: 40px;height: 40px;border-radius: 50%;"></el-image>
              </div>
            </div>
            <div style="flex: 1;">
              <div style="height: 40px;">
                <div style="display: flex;">
                  <div style="line-height: 20px;color: rgb(235, 115, 80);display: flex;">
                    {{item3.userInfo.nickname}}
                  </div>
                  <div style="flex: 1;text-align: right;" v-if="item3.userId == userInfo.id" >
                    <el-dropdown>
                      <div style="display: flex;">
                        <span style="line-height: 20px;font-size: 22px;">
                          <i class="el-icon-more"></i>
                        </span>
                      </div>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item @click.native="delFun(item,item3)" style="color:red;" >删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown>
                  </div>
                </div>
                <div style="color: rgb(168, 176, 183);font-size: 12px;line-height: 20px;">
                  {{item3.createTime}}
                </div>
              </div>
              <div class="share-item-content">
                <el-input type="textarea" resize="none" :autosize="true" :readonly="true" v-model="item3.content"></el-input>
              </div>
            </div>
          </div>
        </div>
        <div v-if="item.length === 0">
          <el-empty description="暂无数据"></el-empty>
        </div>
      </div>
      <div style="text-align: center;color: rgb(168, 176, 183);">
        <p v-if="loading">加载中...</p>
        <p v-if="!loading&&noMore">没有更多了</p>
      </div>

      <!-- 修改弹窗 -->
      <div style="font-size: 14px;width: 100%;height: 100%;">
      <el-dialog
        title="修改信息"
        @close="close(form.files)"
        :visible.sync="box"
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
                :on-preview="handlePictureCardPreview"
                :disabled="disabled"
                :multiple="true">
                <i class="el-icon-plus"></i>
<!--                <img  v-for="i in prefileList" :key="i" :src="i" class="avatar" alt=""/>-->
              </el-upload>
                  <el-dialog :visible.sync="dialogVisible">
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
    </div>
</template>

<script>
import {delMypost, getPage, updateMypost} from '../../api/posts'
import {addLike} from '@/api/posts_like'
import {addComment, del, listByPostsId} from '@/api/posts_comment'
import {mapGetters} from 'vuex'
import {getToken} from '../../utils/auth'
import {Picker} from 'emoji-mart-vue'
import {setStore} from '../../utils/store'

export default {
  components: { // 注册组件，不能全局挂载
    Picker
  },
  data () {
    return {
      loading: false,
      posts: [],
      box: false,
      page: {
        pageTotal: 0,
        total: 0,
        pageSize: 10,
        currentPage: 1,
        postsType: 2,
        school: null
      },
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
      dialogVisible: false
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ]),
    noMore () {
      return this.page.currentPage >= this.page.pageTotal
    },
    // eslint-disable-next-line vue/no-dupe-keys
    disabled () {
      return this.loading || this.noMore
    }
  },
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
  mounted () {
  },
  methods: {
    init (school) {
      this.page = {
        pageTotal: 0,
        total: 0,
        pageSize: 10,
        currentPage: 0,
        postsType: 2,
        school: null
      }
      this.page.school = school
      this.getPageFun()
    },
    load () {
      if (this.loading) {
        return
      }
      // console.info('page=' + this.page.currentPage)
      if (this.page.currentPage < this.page.pageTotal) {
        this.getPageFun()
      }
    },
    addEmoji (item, event) {
      const emojiNative = event.native
      if (item.commentContent === undefined) {
        item.commentContent = ''
        item.commentContent += emojiNative
      }
      item.commentContent += emojiNative
      console.log(item.commentContent)
    },
    sizeChange (pageSize) { // 页数
      this.page.pageSize = pageSize
      this.getPageFun()
    },
    currentChange (currentPage) { // 当前页
      this.page.currentPage = currentPage
      this.getPageFun()
    },
    getPageFun () {
      this.loading = true
      this.page.currentPage++
      getPage(this.page).then(res => {
        if (res.code === 200) {
          if (this.page.currentPage === 1) {
            this.posts = []
            console.log('getPage:')
            console.log(this.posts)
            // console.log(this.form)
          }
          res.data.forEach(ele => {
            ele['comment'] = false
            ele['comments'] = []
            this.posts.push(ele)
          })
          this.page.total = res.dataTotal
          this.page.pageTotal = res.pageTotal
          this.loading = false
        } else {
          this.page.currentPage--
        }
      }, error => {
        this.page.currentPage--
        this.loading = false
      })
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
    // // 图片上传成功的回调
    // handleSuccess (res, file, fileList) {
    //   this.fileList = fileList
    //   // 将上传成功的文件信息保存到form.images中
    //   this.fileList.push({
    //     name: res.name,
    //     url: res.url,
    //     uid: res.uid,
    //     status: 'success'
    //   })
    // },
    handlePictureCardPreview (file) { // 点击预览
      // console.log(file)
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    close () { // 关闭弹窗后清除表单数据
      this.$refs.form.resetFields()
      this.$refs.form.clearValidate()
      this.$refs.upload.clearFiles()
      this.fileList.splice(null, 1)
      this.box = false
    },
    updateModal (item, index) {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
      this.form = this.posts[index]
      this.form.school = this.form.school + ''
      console.log('updateModal:')
      console.log(JSON.stringify(this.fileList))
      if (this.posts[index].imgPath) {
        const arr = this.posts[index].imgPath.split(',')
        console.log('file:' + JSON.stringify(arr))
        this.form.files = []
        this.fileList = []
        for (const file of arr) { // 使用 for...of 遍历数组
          this.form.files.push({
            name: file.name,
            raw: file,
            url: file
          })
          this.fileList.push({
            name: file.name,
            url: file
          })
        }
        // for (const imageUrl of arr) {
        //   const fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1)
        //   console.log(fileName) // 输出: pic1.jpg
        //   fetch(imageUrl)
        //     .then(response => response.blob())
        //     .then(blob => {
        //     // 创建 File 对象
        //       const file = new File([blob], fileName)
        //       // 将文件对象添加到数组中
        //       this.form.files.push({
        //         name: file.name,
        //         raw: file,
        //         url: URL.createObjectURL(file)
        //       })
        //       this.fileList.push({
        //         name: file.name,
        //         url: URL.createObjectURL(file)
        //       })
        //       console.log('this.form.files:' + JSON.stringify(this.form.files))
        //     })
        //     .catch(error => {
        //       console.error('获取文件失败', error)
        //       // 出现错误时终止 Promise 链
        //       return Promise.reject(error)
        //     })
        // }
      } else {
        this.form.files = []
      }
      this.box = true
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
    backFun () {
      this.$router.push({path: '/'})
    },
    delMypost (item) {
      // console.log(item.id, item.userInfo.id)
      delMypost(item.id, item.userInfo.id).then(res => {
        if (res.code === 200) {
          this.$message.success(res.msg)
          this.getPageFun()
          this.load()
          this.init()
        }
      })
    },
    detailFun (posts) {
      setStore({name: 'posts', content: posts})
      this.$router.push({path: '/detail'})
    },
    likeFun (item) {
      let deleted = 0
      if (item.like) {
        deleted = 1
      }
      addLike(item.id, deleted).then(res => {
        if (res.code === 200) {
          if (item.like) {
            item.likeNum = parseInt(item.likeNum) - 1
            item.like = false
          } else {
            item.likeNum = parseInt(item.likeNum) + 1
            item.like = true
          }
        }
      })
    },
    showComment (item) {
      if (!item.comment) {
        item.comment = true
        this.getComment(item)
      } else {
        item.comment = false
      }
      console.info(item)
    },
    addCommentFun (item) {
      // 处理评论内容，去除空白字符
      item.commentContent = item.commentContent.replace(/\s+/g, '')
      console.log('item.commentContent:' + item.commentContent)
      if (!item.commentContent) {
        this.$message.warning('内容为空！')
        return false
      } else {
        addComment(item.id, item.commentContent).then(res => {
          if (res.code === 200) {
            item.commentNum = parseInt(item.commentNum) + 1
            this.$message.success(res.msg)
            this.getComment(item)
          }
        })
        item.commentContent = ''
        console.log('item.commentContent:' + item.commentContent)
      }
    },
    getComment (item) {
      listByPostsId(item.id).then(res => {
        if (res.code === 200) {
          item['comments'] = res.data
        }
      })
    },
    delFun (item1, item2) {
      this.$confirm('确定要删除该评论吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        del(item2.id).then(res => {
          if (res.code === 200) {
            item1.commentNum = parseInt(item1.commentNum) - 1
            this.$message.success(res.msg)
            this.getComment(item1)
          }
        }, error => {
        })
      })
    },
    resetForm () {
      this.$refs.form.resetFields()
      this.$refs.upload.clearFiles()
    }
  }
}
</script>

<style>
  .share-item{
    display: flex;
    background-color: #ffffff;
    border-radius: 10px;
    padding: 20px 20px;
    margin: 20px 0;
  }
  .share-item-content .el-textarea__inner{
    border: none!important;
    font-size: 16px!important;
    font-weight: 700!important;
    color: #444444 !important;
    font-family: "微软雅黑 Light",sans-serif;
  }
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
  .emoji-mart {
    width: 100px;
    font-family: -apple-system, BlinkMacSystemFont, "Helvetica Neue", sans-serif;
    display: -ms-flexbox;
    display: flex;
    -ms-flex-direction: column;
    flex-direction: column;
    height: 420px;
    color: #ffffff !important;
    border: 1px solid #d9d9d9;
    border-radius: 5px;
    background: #fff;
  }
  .express-btn .icon {
    width: 24px;
    height: 24px;
  }
</style>
