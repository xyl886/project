<template>
  <div class="body" v-loading="loading">
    <el-card class="box" shadow="hover">
      <div style="line-height: 50px;color: gray;">
        <span style="cursor: pointer;margin-left: 20px;" @click="backFun"><i class="el-icon-arrow-left"></i>返回</span>
      </div>
      <div style="float: right;margin-top: -25px;">
        <el-dropdown v-if="posts.status === 3">
          <div style="display: flex;">
                        <span style="margin-right: 20px;line-height: 20px;font-size: 22px;">
                          <i class="el-icon-more"></i>
                        </span>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              type="primary"
              v-if="userInfo.id === posts.userInfo.id "
              icon="el-icon-edit-outline"
              @click.native="edit(posts)">编辑
            </el-dropdown-item>
            <el-dropdown-item
                icon="el-icon-delete"
                style="color:red;"
                v-if="userInfo.id === posts.userInfo.id"
                @click.native="delMyPost(posts)">删除
            </el-dropdown-item>
            <el-dropdown-item
            icon="el-icon-warning"
            style="color:red;"
            v-if="userInfo.id !== posts.userInfo.id"
            @click.native="reportPost(posts)">
            举报
            </el-dropdown-item>
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
<!--            <el-tag size="small" v-if="userInfo.id === posts.userId" style="margin: 0 5px;">我自己</el-tag>-->
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
             <div style="text-align: right;margin-right: 20px;margin-bottom: 5px">{{posts.browseNum}}次浏览</div>
           <div style="text-align: right;margin-right: 20px;">{{posts.createTime}} 发布</div>
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
      </div>
      <div style="margin-left: 20px">
        <el-tag  size="small">{{posts.categoryName}}</el-tag>
        <el-tag
            size="small"
            type="info"
            v-for="(item2,index) in posts.tags"
            :key="index"
            style="margin:5px">
          {{ item2 }}
        </el-tag>
      </div>
      <div v-if="posts.postsType===1&&posts.status===3" style="width: 90%;border-top: 1px solid rgb(229, 233, 239);height: 40px;margin: 20px;">
          <div class="posts-item-price" v-if="posts.postsType===1 && userInfo.id !== posts.userInfo.id" style="color:#e9384d; display: block;margin: 10px;float: right;">
            <span style="margin-right: 20px"> ¥{{posts.price}}</span>
            <el-button type="primary" @click="chat(userInfo.id)">私聊</el-button>
          </div>
      </div>
    <div v-if="posts.postsType===2&&posts.status===3" style="width: 90%;border-top: 1px solid rgb(229, 233, 239);padding: 20px 5%;margin-top: 20px;">
      <div style="line-height: 20px;padding: 0 0 20px 0;">
        <span style="margin-right:5px">评论数</span>
        <span style="margin-right:5px;color: #ccc;">{{posts.commentNum}}</span>
        <el-button class="new" style="border: none" size="small" :class="{ active: sortType === 'new' }" @click="sortByLatest(posts)" >最新</el-button>|
        <el-button class="hot" style="margin-left:0;border: none" size="small" :class="{ active: sortType === 'hot' }" @click="sortByHot(posts)">最热</el-button>
      </div>
      <el-row>
        <el-col :span="18">
          <el-input type="textarea" :rows="1" resize="none" ref="textarea" v-model="commentContent" placeholder="请输入内容"></el-input>
        </el-col>
        <el-col :span="3">
          <el-popover placement="top" trigger="click" class="popover" style="margin: 20px;">
            <custom-emoji v-if="showEmojiCom" class="emoji-component" @addemoji="addEmoji"/>
            <el-button slot="reference" @click.stop="showEmojiCom = !showEmojiCom" style="line-height:1px;height: 33px;">
              😃
            </el-button>
          </el-popover>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" style="float:right;width: 100px;line-height:1px;height:33px;margin-left: 10px;" @click="addCommentFun(posts)">发布</el-button>
        </el-col>
      </el-row>
    </div>
    <div v-if="posts.postsType===2&&posts.status===3"
         v-for="(item2,index3) in comments"
         :key="item2.id"
         style="padding:0 5%;width: 90%;border-bottom: 1px solid rgb(229, 233, 239);display: flex;">
      <div style="width: 50px;">
        <el-popover
            placement="top-start"
            width="300"
            trigger="hover">
          <el-image fit="cover" style="transition:.2s;height: 80px;width: 300px" :src="item2.userInfo?item2.userInfo.avatar:''"></el-image>
          <el-row>
            <el-col :span="6">
              <el-image :src="item2.userInfo?item2.userInfo.avatar:''" style="width: 60px;height: 60px;border-radius:50%;margin-left: 5px;"></el-image>
            </el-col>
            <el-col :span="18">
              <div style="padding: 15px 0;">{{item2.userInfo.nickname}}
                <i>♂</i>
                <el-tag size="small" type="success">{{item2.userInfo.role}}</el-tag></div>
              <div style="padding: 5px 0"><span>{{item2.userInfo.followNum}}关注 </span> <span>{{item2.userInfo.fansNum}} 粉丝 </span><span>11 获赞 </span></div>
              <div style="padding: 10px 0 5px 0">{{item2.userInfo.remark}}</div>
              <div style="padding: 10px 0" v-if="userInfo.id!==item2.userId">
                <el-button v-if="!item2.follow && userInfo.id!==item2.userId" type="primary" size="mini" @click="addFollowFun(item2,'0')"><i class="el-icon-star-off"></i> +关注 {{item2.followNum}}</el-button>
                <el-button v-if="item2.follow && userInfo.id!==item2.userId" type="info" plain size="mini" @click="addFollowFun(item2,'1')"><i class="el-icon-star-on"></i> 已关注 {{item2.followNum}}</el-button>
                <el-button size="mini">发消息</el-button>
              </div>
            </el-col>
          </el-row>
          <el-image slot="reference" :src="item2.userInfo?item2.userInfo.avatar:''" style="width: 40px;height: 40px;border-radius:50%;"></el-image>
        </el-popover>
      </div>
      <div style="flex: 1;">
        <div style="height: 40px;">
          <div>
            <el-row>
              <el-col :span="12">
                <div style="font-size: 16px;color: #2c3e50;display: flex;padding:0 10px">
                  <span style="padding-right: 5px">{{item2.userInfo.nickname}}</span>
<!--                  <el-tag style="margin-right: 5px;" size="small">标签一</el-tag>-->
                  <el-tag type="success" size="small">{{posts.userInfo.role}}</el-tag>
                </div>
              </el-col>
              <el-col :span="12">
                <div style="flex: 1;text-align: right;">
                  <el-dropdown v-if="userInfo.id">
                    <div style="display: flex;">
                            <span style="line-height: 20px;font-size: 22px;">
                              <i class="el-icon-more"></i>
                            </span>
                    </div>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item
                          v-if="item2.userId == userInfo.id"
                          @click.native="delFun(posts,item2)"
                          style="color:red;">删除
                      </el-dropdown-item>
                      <el-dropdown-item
                          v-else-if="item2.userId!==userInfo.id"
                          @click.native="reportPost(item2)"
                          style="color:red;">举报
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="share-item-content">
          <el-input type="textarea" resize="none" :autosize="true" :readonly="true" v-model="item2.content"></el-input>
        </div>
        <div style="color: rgb(168, 176, 183);font-size: 12px;line-height: 20px;">
          {{item2.createTime}}
          <span v-if="!item2.like" @click="likeCommentFun(item2)"><i class="iconfont icon-icon" style="font-size:14px;"></i></span>
          <span v-if="item2.like" @click="likeCommentFun(item2)"><i class="iconfont icon-icon" style="font-size:14px;color: #409EFF;"></i></span>
          <span style="color: rgb(153, 162, 170);">{{item2.likeNum>0?item2.likeNum:'点赞'}}</span>
        </div>
      </div>
    </div>
    </el-card>
    <UpdateDialog ref="updateDialog" :Form="form" @submit="handlePopupSubmit"/>
    <Report ref="reportDialog"/>
  </div>
</template>

<script>
import {browse, getDetail} from '@/api/posts'
import {addCollect} from '@/api/collect'
import {addFollow} from '@/api/follow'
import {mapGetters} from 'vuex'
import {getStore, setStore} from '../../utils/store'
import {delMyPost} from '../../api/posts'
import UpdateDialog from './UpdateDialog'
import Report from '../../components/Report.vue'
import customEmoji from '../../components/emoji/index.vue'
import {addComment, del, listByPostsId} from '../../api/posts_comment'
import {addFriend} from '../../api/chat'

export default {
  components: {
    customEmoji,
    Report,
    UpdateDialog
  },
  data () {
    return {
      loading: false,
      showEmojiCom: false,
      id: '',
      sortType: 'new',
      posts: {
        userInfo: {
          avatar: ''
        },
        collect: false,
        follow: false
      },
      comments: [],
      commentContent: '',
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
    let obj = getStore({name: 'posts'})
    console.log(obj)
    if (obj) {
      this.id = obj.id
      console.log(this.id)
      this.getComment(this.id)
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
    addEmoji (emoji = '') {
      console.log(emoji)
      console.log(this.commentContent)
      this.commentContent += emoji
    },
    sortByLatest (item) {
      this.sortType = 'new'
      this.getComment(item.id)
    },
    sortByHot (item) {
      this.sortType = 'hot'
      this.getComment(item.id)
    },
    addCommentFun (item) {
      this.commentContent = this.commentContent.replace(/\s+/g, '') // 处理评论内容，去除空白字符
      if (!this.commentContent) {
        this.$message.warning('内容为空！')
        return false
      } else {
        addComment(item.id, this.commentContent).then(res => {
          if (res.code === 200) {
            item.commentNum = parseInt(item.commentNum) + 1
            this.$message.success(res.msg)
            this.getComment(item.id)
          }
        })
        this.commentContent = ''
      }
    },
    getComment (id) {
      listByPostsId(id).then(res => {
        if (res.code === 200) {
          if (this.sortType === 'new') {
            res.data.sort((a, b) => {
              return new Date(b.createTime) - new Date(a.createTime)
            })
          } else if (this.sortType === 'hot') {
            res.data.sort((a, b) => {
              return b.likeNum - a.likeNum
            })
          }
          this.comments = res.data
        }
      })
    },
    delFun (item1, item2) {
      this.$confirm('确定要删除该评论吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          del(item2.id).then(res => {
            if (res.code === 200) {
              item1.commentNum = parseInt(item1.commentNum) - 1
              this.$message.success(res.msg)
              this.getComment(item1)
            }
          })
        })
    },
    reportPost (posts) {
      setStore({name: 'posts', content: posts})
      this.$refs.reportDialog.showDialog()
    },
    handlePopupSubmit () {
      this.getDetailFun()
    },
    backFun () {
      history.back()
    },
    selImg (url) {
      this.bigImgPath = url
    },
    chat () {
      // addFollow(this.posts.userInfo.id, '0').then(res => {
      addFriend(this.posts.userInfo.id).then(res => {
        if (res.code === 200) {
          this.$message.success(res.msg)
          this.$router.push({path: '/chat'})
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    getDetailFun () {
      this.loading = true
      getDetail(this.id).then(res => {
        if (res.code === 200) {
          this.posts = res.data
          console.log(this.posts)
          this.markdownToHtml(this.posts.content)
          this.bigImgPath = res.data.coverPath
        }
        this.loading = false
      }, error => {
        console.log(error)
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
            this.posts.collectNum = parseInt(this.posts.collectNum) + 1
          } else {
            this.posts.collect = false
            this.posts.collectNum = parseInt(this.posts.collectNum) - 1
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
    //background-size: auto;
    //background-image:  url("../../../public/img/background-detail.jpg");
    //background-repeat: no-repeat;
    background-color: #fff;
    position:absolute;
  }
  /deep/
  .el-card__body{
    background-color: #fff;
    margin: 0 100px 100px 100px;
  }
  .box{
    font-size: 14px;
    padding: 50px 100px 100px 100px;
    position: relative;
    background-color: #ccc;
    border-radius: 10px;
    //position:absolute; /*参照物是父容器*/
    //transform:translateX(-50%) translateY(-50%); /*百分比的参照物是自身*/
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
  /deep/
  .share-item-content .el-textarea__inner{
    border: none;
    font-size: 16px;
    font-weight: 700;
    color: #444444 ;
    font-family: "微软雅黑 Light",sans-serif;
  }
</style>
