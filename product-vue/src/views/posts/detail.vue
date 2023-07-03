<template>
  <div class="body" v-loading="loading">
    <div class="box">
      <div style="line-height: 50px;color: gray;">
        <span style="cursor: pointer;margin-left: 20px;" @click="backFun"><i class="el-icon-arrow-left"></i>返回</span>
      </div>
      <div style="float: right;margin-top: -25px;">
        <el-dropdown  v-if="userInfo.id === posts.userInfo.id">
          <div style="display: flex;">
                        <span style="margin-right: 20px;line-height: 20px;font-size: 22px;">
                          <i class="el-icon-more"></i>
                        </span>
          </div>
          <el-dropdown-menu
            slot="dropdown">
            <el-button
              type="primary"
              icon="el-icon-edit-outline"
              @click.native="edit(posts)">编辑</el-button>
            <el-dropdown-item icon="el-icon-delete" style="color:red;" @click.native="delMyPost(posts)">删除</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div style="display: flex;margin-top: 30px;height: 60px;">
        <div style="margin-left: 50px;">
          <el-image :src="posts.userInfo.avatar" style="width: 60px;height: 60px;border-radius: 50%;"></el-image>
        </div>
        <div style="margin: 0 100px 0 0;">
          <div style="height: 20px;margin-bottom: 5px">
            {{posts.userInfo.nickname}}
            <el-tag size="small" v-if="userInfo.id === posts.userId" style="margin: 0 5px;">我自己</el-tag>
            <el-tag type="success" size="small">{{posts.userInfo.role}}</el-tag>
          </div>
          <div style="line-height: 40px;">
            <el-button v-if="!posts.collect" type="primary" size="mini" @click="addCollectFun('0')"><i class="el-icon-star-off"></i> 收藏 {{posts.collectNum}}</el-button>
            <el-button v-if="posts.collect" type="info" plain size="mini"
                       @click="addCollectFun('1')"><i class="el-icon-star-on"></i> 已收藏 {{posts.collectNum}}</el-button>
            <el-button v-if="!posts.follow && userInfo.id !== posts.userId" type="primary" size="mini" @click="addFollowFun('0')"><i class="el-icon-star-off"></i> 关注 {{posts.followNum}}</el-button>
            <el-button v-if="posts.follow && userInfo.id !== posts.userId" type="info" plain size="mini" @click="addFollowFun('1')"><i class="el-icon-star-on"></i> 已关注 {{posts.followNum}}</el-button>
          </div>
        </div>
        <div style="flex: 1;color: #606266;font-size: 12px;margin: 0 0 0 220px;">
           <el-col :span="14">
             <div style="font-size: 20px;font-weight: 700;">{{posts.title}}</div>
           </el-col>
           <el-col :span="10" style="opacity: 0.6;">
             <div style="text-align: right;margin-right: 20px;">{{posts.browseNum}}次浏览</div>
           <div style="text-align: right;margin-right: 20px;">
             <el-tag size="small">{{posts.schoolName}}</el-tag>| {{posts.createTime}} 发布
           </div>
           </el-col>
        </div>
      </div>
      <div style="display: flex;margin: 20px;">
        <div style="width: 40%;padding: 10px;">
          <div style="width: 350px;height: 225px;text-align: center;border-radius: 10px;display:table-cell;vertical-align: middle;">
            <el-image :src="bigImgPath" :preview-src-list="posts.imgPath?posts.imgPath.split(','):[]" fit="contain" class="big-img"></el-image>
          </div>
          <div style="margin-top: 10px;">
            <el-image v-for="(item) in posts.imgPath?posts.imgPath.split(','):[]" :key="item" fit="contain" :src="item" @click="selImg(item)" style="width: 80px;height: 80px;border-radius: 5px;margin: 0 10px 10px 0;border: 1px solid #e5e5e5;box-sizing: border-box;" :class="bigImgPath===item?'sel-img':''"></el-image>
          </div>
        </div>
        <div style="width: 60%;padding:10px;overflow: scroll">
<!--          <div style="font-size: 20px;font-weight: 700;">{{posts.title}}</div>-->
          <div class="my-content" style="height: 225px">
              <article
                id="write"
                class="article-content markdown-body"
                v-html="posts.content"
                ref="article"/>
          </div>
        </div>
        <div style="position: absolute;bottom: 0;right: 100px;">
          <div class="posts-item-price" v-if="posts.postsType===1 && userInfo.id !== posts.userInfo.id" style="color:#e9384d; display: block;margin: 10px;float: right;">
            <span style="margin-right: 20px"> ¥{{posts.price}}</span>
            <el-button type="primary" @click="chat(userInfo.id)">想要</el-button>
          </div>
        </div>
      </div>
    </div>
    <UpdateDialog ref="updateDialog" :Form="form"/>
  </div>
</template>

<script>
import {browse, getDetail} from '@/api/posts'
import {addCollect} from '@/api/collect'
import {addFollow} from '@/api/follow'
import {mapGetters} from 'vuex'
import {getStore} from '../../utils/store'
import {delMyPost} from '../../api/posts'
import UpdateDialog from './UpdateDialog'

export default {
  components: {
    UpdateDialog
  },
  data () {
    return {
      loading: false,
      id: '',
      posts: {
        userInfo: {
          avatar: ''
        },
        collect: false,
        follow: false
      },
      form: {
        id: '',
        title: '',
        content: '',
        school: '',
        files: []
      },
      bigImgPath: ''
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  mounted () {
    this.$on('postUpdate', () => {
      this.getDetailFun() // 当帖子信息更新完后，重新获取数据
    })
    let obj = getStore({name: 'posts'})
    console.log(obj)
    if (obj) {
      this.id = obj.id
      console.log(this.id)
      this.bigImgPath = this.posts.imgPath ? this.posts.imgPath.split(',')[0] : ''
      this.selImg(this.bigImgPath)
      this.getDetailFun()
      let that = this
      setTimeout(function () {
        that.browseFun()
      }, 1000)
    }
  },
  methods: {
    backFun () {
      history.back()
    },
    selImg (url) {
      this.bigImgPath = url
    },
    chat () {
      this.$router.push({path: '/chat'})
    },
    getDetailFun () {
      this.loading = true
      getDetail(this.id).then(res => {
        if (res.code === 200) {
          this.posts = res.data
          console.log(this.posts)
          this.markdownToHtml(this.posts.content)
          // console.log(this.posts.content)
          this.bigImgPath = res.data.coverPath
          // console.log(res.data)
          // console.log(this.bigImgPath)
        }
        this.loading = false
        // eslint-disable-next-line handle-callback-err
      }, error => {
        this.loading = false
      })
    },
    edit () {
      this.$refs.updateDialog.showDialog()
      if (this.posts.imgPath) {
        const arr = this.posts.imgPath.split(',')
        console.log('file:' + JSON.stringify(arr))
        this.fileList = []
        for (const file in arr) {
          console.log(arr[file].substring(arr.lastIndexOf('/') + 1)) // 输出: pic1.jpg
          this.fileList.push({
            name: arr[file].substring(arr.lastIndexOf('/') + 1),
            url: arr[file]
          })
          console.log(this.fileList)
        }
      }
      console.log(this.posts)
      if (this.posts.postsType === '2') {
        this.form = {
          postsType: this.posts.postsType,
          id: this.posts.id,
          userId: this.posts.userInfo.id,
          title: this.posts.title,
          content: this.posts.content,
          school: this.posts.school,
          imageList: this.fileList
        }
      } else {
        this.form = {
          postsType: this.posts.postsType,
          id: this.posts.id,
          userId: this.posts.userInfo.id,
          title: this.posts.title,
          content: this.posts.content,
          school: this.posts.school,
          price: this.posts.price,
          imageList: this.fileList
        }
      }
      console.log(this.form)
    },
    delMyPost (item) {
      this.$confirm('确定要删除该帖子吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          delMyPost(item.id, item.userInfo.id).then(res => {
            if (res.code === 200) {
              this.$message.success(res.msg)
              this.$router.push({path: '/index'})
              this.getPageFun()
              this.load()
              this.init()
            }
          })
        })
    },
    markdownToHtml (posts) {
      const MarkdownIt = require('markdown-it')
      const hljs = require('highlight.js')
      const md = new MarkdownIt({
        html: true,
        linkify: true,
        typographer: true,
        highlight: function (str, lang) {
          // 当前时间加随机数生成唯一的id标识
          let d = new Date().getTime()
          if (
            window.performance &&
            typeof window.performance.now === 'function'
          ) {
            d += performance.now()
          }
          const codeIndex = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(
            /[xy]/g,
            function (c) {
              var r = (d + Math.random() * 16) % 16 | 0
              d = Math.floor(d / 16)
              return (c === 'x' ? r : (r & 0x3) | 0x8).toString(16)
            }
          )
          // 复制功能主要使用的是 clipboard.js
          let html = `<button class="copy-btn iconfont iconfuzhi" type="button" data-clipboard-action="copy" data-clipboard-target="#copy${codeIndex}"></button>`
          const linesLength = str.split(/\n/).length - 1
          // 生成行号
          let linesNum = '<span aria-hidden="true" class="line-numbers-rows">'
          for (let index = 0; index < linesLength; index++) {
            linesNum = linesNum + '<span></span>'
          }
          linesNum += '</span>'
          if (lang && hljs.getLanguage(lang)) {
            // highlight.js 高亮代码
            const preCode = hljs.highlight(lang, str, true).value
            html = html + preCode
            if (linesLength) {
              html += '<b class="name">' + lang + '</b>'
            }
            // 将代码包裹在 textarea 中，由于防止textarea渲染出现问题，这里将 "<" 用 "<" 代替，不影响复制功能
            return `<pre class="hljs"><code>${html}</code>${linesNum}</pre><textarea style="position: absolute;top: -9999px;left: -9999px;z-index: -9999;" id="copy${codeIndex}">${str.replace(
              /<\/textarea>/g,
              '</textarea>'
            )}</textarea>`
          }
        }
      })
      // 将markdown替换为html标签
      this.posts.content = md.render(posts)
    },
    browseFun () {
      console.log(this.userInfo.id + ',' + this.id)
      browse(this.userInfo.id, this.id).then(res => {
        if (res.code === 200) {
          console.info('浏览次数更新成功')
        }
      })
    },
    addCollectFun (deleted) {
      addCollect(this.id, deleted).then(res => {
        if (res.code === 200) {
          if (deleted === '0') {
            this.posts.collect = true
          } else {
            this.posts.collect = false
          }
          this.$message.success(res.msg)
        }
      })
    },
    addFollowFun (deleted) {
      addFollow(this.posts.userInfo.id, deleted).then(res => {
        if (res.code === 200) {
          if (deleted === '0') {
            this.posts.follow = true
          } else {
            this.posts.follow = false
          }
          this.$message.success(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
  .body{
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
    background-size: 100% 100%;
    background-image:  url("../../../public/img/background-detail.jpg");
    background-repeat: no-repeat;
    position:absolute;
  }
  .box{
    font-size: 14px;
    width: 90%;
    height: 90%;
    background-color: #ffffff;
    border-radius: 10px;
    position:absolute; /*参照物是父容器*/
    left:50%;
    transform:translateX(-50%) translateY(-50%); /*百分比的参照物是自身*/
    top:50%;   /* 参照物是父容器 */
    overflow: hidden;
  }
  .sel-img{
    border: 2px solid #ff6619!important;
  }
  .my-content .el-textarea__inner{
    border: none!important;
    font-size: 16px!important;
    font-weight: 700!important;
    color: #5b5b5b !important;
    font-family: "微软雅黑 Light",sans-serif;
  }
  .textarea{
  height: 250px;
  }
.posts-item-price{
  height: 50px;
  }
  .big-img{
    max-height: 250px;
  }
</style>
