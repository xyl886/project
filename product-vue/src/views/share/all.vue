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
              <div style="float: right;margin-top: 5px;">
                <el-dropdown>
                  <div style="display: flex;">
                        <span style="line-height: 20px;font-size: 22px;">
                          <i class="el-icon-more"></i>
                        </span>
                  </div>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item style="color:red;" v-if="item.userId === userInfo.id" @click.native="delMyPost(item)">删除</el-dropdown-item>
                    <el-dropdown-item v-else-if="item.userId!==userInfo.id" style="color:red;" @click.native="reportPost(item)">举报</el-dropdown-item>
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
            <span v-if="!item.like"><i class="iconfont icon-icon" style="font-size:20px;"></i></span>
            <span v-if="item.like"><i class="iconfont icon-icon" style="font-size:20px;color: #409EFF;"></i></span>
              <span style="color: rgb(153, 162, 170);">{{item.likeNum>0?item.likeNum:'点赞'}}</span>
            </div>
            <div style="display: flex;margin-left: 50px;cursor: pointer;" @click="showComment(item)">
              <span v-if="!item.comment"><i class="iconfont icon-comment" style="font-size:20px;"></i></span>
              <span v-if="item.comment"><i class="iconfont icon-comment" style="font-size:20px;color: #409EFF;"></i></span>
              <span style="color: rgb(153, 162, 170);">
                {{item.comment ? "收起" : (item.commentNum > 0 ? item.commentNum : "评论")}}
              </span>
            </div>
          </div>
          <div v-show="item.comment" style="border-top: 1px solid rgb(229, 233, 239);border-bottom: 1px solid rgb(229, 233, 239);padding: 20px 0;margin-top: 20px;">
           <el-row >
             <el-col :span="18">
               <el-input type="textarea" :rows="1" resize="none" ref="textarea" v-model="commentContent" placeholder="请输入内容"></el-input>
             </el-col>
             <el-col :span="2">
               <el-popover placement="top" trigger="click" class="popover">
                 <custom-emoji v-if="showEmojiCom" class="emoji-component" @addemoji="addEmoji"/>
                 <el-button slot="reference" @click.stop="showEmojiCom = !showEmojiCom" style="line-height:1px;height: 33px;">
                   😃
                 </el-button>
               </el-popover>
             </el-col>
             <el-col :span="2">
               <el-button type="primary" style="width: 100px;line-height:1px;height:33px;margin-left: 10px;" @click="addCommentFun(item)">发布</el-button>
             </el-col>
            </el-row>
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
                  <div style="flex: 1;text-align: right;"  >
                    <el-dropdown>
                      <div style="display: flex;">
                        <span style="line-height: 20px;font-size: 22px;">
                          <i class="el-icon-more"></i>
                        </span>
                      </div>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item
                          v-if="item3.userId == userInfo.id"
                          @click.native="delFun(item,item3)"
                          style="color:red;">删除
                        </el-dropdown-item>
                        <el-dropdown-item
                          v-else-if="item3.userId!==userInfo.id"
                          @click.native="reportPost(item3)"
                          style="color:red;">举报
                        </el-dropdown-item>
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

export default {
  components: {
    Report, // 注册组件，不能全局挂载
    customEmoji
  },
  data () {
    return {
      // dialogVisible: false,
      loading: false,
      showEmojiCom: false,
      posts: [],
      box: false,
      commentContent: '',
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
    // eslint-disable-next-line vue/no-dupe-keys
    // disabled () {
    //   return this.loading || this.noMore
    // }
  },
  watch: {
  },
  mounted () {
  },
  methods: {
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
    // sizeChange (pageSize) { // 页数
    //   this.page.pageSize = pageSize
    //   this.getPageFun()
    // },
    // currentChange (currentPage) { // 当前页
    //   this.page.currentPage = currentPage
    //   this.getPageFun()
    // },
    addEmoji (emoji = '') {
      console.log(emoji)
      console.log(this.commentContent)
      this.commentContent += emoji
    },
    getPageFun () {
      this.loading = true
      this.page.currentPage++
      console.log(this.page)
      getPage(this.page).then(res => {
        if (res.code === 200) {
          if (this.page.currentPage === 1) {
            this.posts = []
            console.log('getPage:' + this.posts)
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
      this.$router.push({path: '/Article'})
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
      console.info(item)
    },
    addCommentFun (item) {
      this.commentContent = this.commentContent.replace(/\s+/g, '') // 处理评论内容，去除空白字符
      console.log('item.commentContent:' + this.commentContent)
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
        console.log('item.commentContent:' + this.commentContent)
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

  .express-btn .icon {
    width: 24px;
    height: 24px;
  }
  .emoji-component {
    position: absolute;
    bottom: 0;
    right: 0;
    //transform-origin: center top;
    //transform: translateX(-50%);
    //bottom: calc(100% + 10px);
    //left: 50%;
    //top: calc(100% + 10px);
  }
</style>
