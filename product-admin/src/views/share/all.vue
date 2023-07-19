<template>
  <div style="font-size: 14px;overflow:auto" @scroll="handleScroll">
    <div class="search">
      <el-input style="line-height: 50px;padding:5px 30px 5px 0;width: 70%" v-model="page.title" name="search" placeholder="请输入帖子标题"></el-input>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
        <el-button style="margin-left: 30px;" icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
    </div>
    <div  v-for="(item,index) in posts"
          :key="item.id"  class="share-item">
      <div style="flex: 1;">
        <div style="height: 80px;">
          <el-row style="padding: 10px 0">
            <el-col :span="3">
              <el-popover
                placement="top-start"
                width="300"
                trigger="click">
                <el-image fit="cover" style="height: 80px;width: 300px" :src="item.userInfo?item.userInfo.avatar:''"></el-image>
                <el-row>
                  <el-col :span="6">
                    <el-image :src="item.userInfo?item.userInfo.avatar:''" style="width: 60px;height: 60px;border-radius:50%;margin-left: 5px;"></el-image>
                  </el-col>
                  <el-col :span="18">
                    <div style="padding: 10px 0;font-size: 12px">{{item.userInfo.nickname}}
                      <i>♂</i>
                      <el-tag size="mini" type="success">{{item.userInfo.role}}</el-tag>
                    </div>
                    <div style="padding: 10px 0;font-size: 12px">
                      <span>{{item.userInfo.followNum}} 关注 </span>
                      <span>{{item.userInfo.fansNum}} 粉丝 </span>
                      <span>11 获赞 </span></div>
                    <div style="padding: 10px 0;font-size: 12px" v-show="item.userInfo.remark">{{item.userInfo.remark}}</div>
                    <div style="padding: 10px 0;font-size: 12px" v-if="userInfo.id!==item.userId">
                      <el-button size="mini" type="primary">+ 关注</el-button>
                      <el-button size="mini">发消息</el-button>
                    </div>
                  </el-col>
                </el-row>
                <el-image slot="reference" :src="item.userInfo?item.userInfo.avatar:''" style="width: 60px;height: 60px;border-radius:50%;margin-left: 20px;"></el-image>
              </el-popover>
            </el-col>
            <el-col :span="21">
              <div style="font-size: 16px;line-height: 40px;">
                <span>{{item.userInfo.nickname}}</span>
                <el-button v-if="!posts.follow && item.userInfo.id!==item.userId" type="primary" size="mini" @click="addFollowFun('0')"><i class="el-icon-star-off"></i> +关注 {{item.userInfo.followNum}}</el-button>
                <el-button v-if="posts.follow && item.userInfo.id!==item.userId" type="info" plain size="mini" @click="addFollowFun('1')"><i class="el-icon-star-on"></i> 已关注 {{item.userInfo.followNum}}</el-button>
                <el-tag type="success">{{item.userInfo.role}}</el-tag>
                <div style="float: right;margin-top: 5px;">
                  <el-dropdown>
                    <div style="display: flex;">
                        <span style="line-height: 20px;font-size: 22px;">
                          <i class="el-icon-more"></i>
                        </span>
                    </div>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item style="color:red;" v-if="item.userId === userInfo.id" @click.native="delMyPost(item)">删除</el-dropdown-item>
                      <el-dropdown-item v-else style="color:red;" @click.native="reportPost(item)">举报</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown></div>
              </div>
            </el-col>
          </el-row>
        </div>
        <el-row>
          <el-col :span="16" style="padding: 0 10px">
            <div class="share-item-content" style="margin-bottom: 16px"  @click="detailFun(item)">
              <el-input style="height: auto" type="textarea" resize="none" :autosize="true" :readonly="true" v-model="item.title"></el-input>
            </div>
            <div class="share-item-content" style="margin-bottom: 16px"  @click="detailFun(item)">
              <span style="color: rgba(0,0,0,.45);">{{item.description}}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <el-image :src="item.coverPath" fit="contain" style="border-radius: 5px;margin: 10px;"></el-image>
          </el-col>
        </el-row>
        <div style="color: rgb(168, 176, 183);font-size: 12px;padding: 0 20px;">
          <el-tag size="small">{{item.schoolName}}</el-tag>
        </div>

        <div style="display: flex;margin-top: 20px;">
          <div style="display: flex;cursor: pointer;" @click="likeFun(item)">
            <span v-if="!item.like"><i class="iconfont icon-icon" style="font-size:20px;"></i></span>
            <span v-if="item.like"><i class="iconfont icon-icon" style="font-size:20px;color: #409EFF;"></i></span>
            <span style="color: rgb(153, 162, 170);width: 40px">{{item.likeNum>0?item.likeNum:'点赞'}}</span>
          </div>
          <div style="display: flex;margin-left: 10px;cursor: pointer;" @click="showComment(item)">
            <span v-if="!item.comment"><i class="iconfont icon-comment" style="font-size:20px;"></i></span>
            <span v-if="item.comment"><i class="iconfont icon-comment" style="font-size:20px;color: #409EFF;"></i></span>
            <span style="color: rgb(153, 162, 170);">
                {{item.comment ? "收起" : (item.commentNum > 0 ? item.commentNum : "评论")}}
              </span>
          </div>
          <div style="color: rgb(168, 176, 183);font-size: 12px;margin-left: 50px;line-height: 20px">
            {{item.createTime}}
          </div>
        </div>
        <div v-show="item.comment" style="border-top: 1px solid rgb(229, 233, 239);padding: 20px 0;margin-top: 20px;">
          <div style="line-height: 20px;padding: 0 0 20px 0;">
            <span style="margin-right:5px">评论数</span>
            <span style="margin-right:5px;color: #ccc;">{{item.commentNum}}</span>
            <el-button class="new" style="border: none" size="small" :class="{ active: sortType === 'new' }" @click="sortByLatest(item)" >最新</el-button>|
            <el-button class="hot" style="margin-left:0;border: none" size="small" :class="{ active: sortType === 'hot' }" @click="sortByHot(item)">最热</el-button>
          </div>
          <el-row >
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
              <el-button type="primary" style="float:right;width: 100px;line-height:1px;height:33px;margin-left: 10px;" @click="addCommentFun(item)">发布</el-button>
            </el-col>
          </el-row>
        </div>
        <div v-show="item.comment" v-for="(item2,index3) in item.comments" :key="item2.id" style="border-bottom: 1px solid rgb(229, 233, 239);display: flex;padding-top: 20px;">
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
                  <div style="padding: 10px 0 5px 0">个人简介</div>
                  <div style="padding: 10px 0" v-if="userInfo.id!==item2.userId">
                    <el-button v-if="!posts.follow && userInfo.id!==item2.userId" type="primary" size="mini" @click="addFollowFun('0')"><i class="el-icon-star-off"></i> +关注 {{item2.followNum}}</el-button>
                    <el-button v-if="posts.follow && userInfo.id!==item2.userId" type="info" plain size="mini" @click="addFollowFun('1')"><i class="el-icon-star-on"></i> 已关注 {{item2.followNum}}</el-button>
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
                      <el-tag style="margin-right: 5px;" size="small">标签一</el-tag>
                      <el-tag type="success" size="small">{{item.userInfo.role}}</el-tag>
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
                            @click.native="delFun(item,item2)"
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
      </div>
      <div v-if="item.length === 0">
        <el-empty description="暂无数据"></el-empty>
      </div>
    </div>
    <div style="text-align: center;color: rgb(168, 176, 183);">
      <p v-if="loading">加载中...</p>
      <p v-if="!loading&&noMore">没有更多了</p>
    </div>
    <Report ref="reportDialog"></Report>
  </div>
</template>

<script>
import {delMyPost, getPage} from '../../api/posts'
import {addLike} from '@/api/posts_like'
import {addComment, del, listByPostsId} from '@/api/posts_comment'
import {mapGetters} from 'vuex'
import customEmoji from '../../components/emoji/index.vue'
import {setStore} from '../../utils/store'
import Report from '../../components/Report.vue'
import {addFollow} from '../../api/follow'
import {addCommentLike} from '../../api/posts_comment'

export default {
  props: {
    searchText: {
      type: String,
      default: ''
    }
  },
  components: {
    Report,
    customEmoji
  },
  data () {
    return {
      // dialogVisible: false,
      loading: false,
      showEmojiCom: false,
      posts: [],
      box: false,
      comments: [],
      commentContent: '',
      sortType: 'new',
      bigImgPath: '',
      page: {
        pageTotal: 0,
        total: 0,
        pageSize: 10,
        currentPage: 1,
        postsType: 2,
        school: 0,
        title: ''
      },
      form: {
        id: '',
        title: '',
        content: '',
        school: '',
        files: []
      }
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ]),
    noMore () {
      return this.page.currentPage >= this.page.pageTotal
    }
  },
  mounted () {
    // 监听滚动事件
    window.addEventListener('scroll', this.handleScroll)
  },
  methods: {
    selImg (url) {
      this.bigImgPath = url
    },
    handlerShowEmoji () {
      this.showEmojiCom = false
    },
    init (school) {
      this.page = {
        pageTotal: 0,
        total: 0,
        pageSize: 10,
        currentPage: 0,
        postsType: 2,
        school: 0
      }
      this.page.school = school
      this.posts = []
      this.getPageFun()
    },
    load () {
      if (this.loading) {
        return
      }
      console.log('page=' + this.page.currentPage)
      if (this.page.currentPage < this.page.pageTotal) {
        this.getPageFun()
      }
    },
    sizeChange (pageSize) { // 页数
      this.page.pageSize = pageSize
      this.getPageFun()
    },
    currentChange (currentPage) { // 当前页
      this.page.currentPage = currentPage
      this.getPageFun()
    },
    addEmoji (emoji = '') {
      console.log(emoji)
      console.log(this.commentContent)
      this.commentContent += emoji
    },
    handleScroll (e) {
      // 判断是否正在加载数据
      if (this.loading) return
      // 判断是否到达底部
      let clientHeight = document.documentElement.clientHeight // 可视区域
      let scrollTop = parseInt(document.documentElement.scrollTop) + 1 // 已滚动的高度
      const scrollHeight = document.documentElement.scrollHeight// 滚动文档高度
      if (scrollTop + clientHeight >= scrollHeight) {
        this.page.currentPage++
        // console.log(this.page.currentPage)
        this.getPageFun()
      }
    },
    resetQuery () {
      this.page.title = null
      this.posts = []
      this.getPageFun()
    },
    handleFind () {
      this.page.currentPage = 1
      this.posts = []
      this.getPageFun()
    },
    getPageFun () {
      this.loading = true
      console.log(this.page.currentPage)
      getPage(this.page).then(res => {
        if (res.code === 200) {
          console.log(res.data)
          if (this.page.currentPage === 1) {
            this.posts = []
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
        console.log(error)
        this.page.currentPage--
        this.loading = false
      })
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
              this.getPageFun()
              this.load()
              this.init()
            }
          })
        })
    },
    reportPost (item) {
      this.$refs.reportDialog.showDialog()
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
            item.like = true
            item.likeNum = parseInt(item.likeNum) + 1
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
      console.log(item.comments)
      this.comments = item.comments
    },
    sortByLatest (item) {
      this.sortType = 'new'
      this.getComment(item)
    },
    sortByHot (item) {
      this.sortType = 'hot'
      this.getComment(item)
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
            this.getComment(item)
          }
        })
        this.commentContent = ''
      }
    },
    getComment (item) {
      listByPostsId(item.id).then(res => {
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
          item['comments'] = res.data
        }
      })
    },
    likeCommentFun (item) {
      let deleted = 0
      if (item.like) { deleted = 1 }
      console.log(item.id)
      addCommentLike(item.id, deleted).then(res => {
        if (res.code === 200) {
          if (item.like) {
            item.likeNum = parseInt(item.likeNum) - 1
            item.like = false
          } else {
            item.like = true
            item.likeNum = parseInt(item.likeNum) + 1
          }
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
    }
  },
  created () {
    // console.log('chatArea created')
    document.addEventListener('click', this.handlerShowEmoji)
  },
  beforeDestroy () {
    // console.log('chatArea BeforeDestroy')
    document.removeEventListener('click', this.handlerShowEmoji)
    // 移除滚动事件监听器
    window.removeEventListener('scroll', this.handleScroll)
  }
}
</script>

<style>
@import '../../../static/iconfont/iconfont.css';
.share-item{
  display: flex;
  background-color: #ffffff;
  border-radius: 10px;
  padding: 20px 20px;
  margin: 10px 0;
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
.new,.hot{
  background-color: transparent;
  border:none;
  margin-right:5px;
  cursor: pointer;
  color: #ccc;
}
.new.active,.hot.active {
  color: #000000;
}
.express-btn .icon {
  width: 24px;
  height: 24px;
}
.emoji-component {
  position: absolute;
  bottom: 0;
  right: 0;
}
.el-popover{
  padding: 0 !important;
}
</style>
