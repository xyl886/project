<template>
  <div class="history" v-loading="loading" v-infinite-scroll="getList"  infinite-scroll-disabled="disabled">
    <div class="clea">
      <el-button
        size="mini"
        @click="removeAll"
        v-show="postType !== '0'&& page.total!==0"
        round
      >清空记录</el-button>
    </div>
    <div class="block">
      <div v-if="page.total == 0" style="text-align: center">
        <span style="color: #999aaa; font-size: 15px">近期没有浏览记录</span>
      </div>
      <div v-if="postType == '1'"  v-for="(item, index) in historyGoodsList" :key="index">
          <el-card shadow="hover" >
          <el-row type="flex" justify="space-between">
            <el-col :span="3" v-show="item.postCoverPath != null">
              <el-image
                style="width: 118px; height: 74px; border-radius: 4px"
                fit="cover"
                :src="item.postCoverPath"
              ></el-image></el-col>
            <el-col :span="17" style="margin: 3px 0 0 10px ;">
              <h3 :key="item.id" @click="detailFun(item.posts)">{{ item.postTitle}}</h3>
              <div class="user">
                <el-avatar
                  :src="item.avater"
                  :key="item.avater"
                  :size="25"
                ></el-avatar>
                <span class="userName">{{ item.nickname }}</span>
              </div>
            </el-col>
            <el-col :span="3">
              <span class="time">{{ item.updateTime }}</span>
            </el-col>
          </el-row>
        </el-card>
      </div>

      <div v-if="postType == '2'">
        <el-card
          v-for="(item, index) in historyPostList"
          :key="index"
          shadow="hover"
        >
          <el-row type="flex" justify="space-between">
            <el-col :span="3" v-show="item.cam_image != null">
              <el-image
                style="width: 118px; height: 74px; border-radius: 4px"
                fit="cover"
                :src="axiosimagesurl + item.image"
              ></el-image
              ></el-col>
            <el-col :span="17" style="margin: 3px 0 0 10px ;">
              <h3><router-link  target="_blank" :to="{path:'/CampusSharing/CampusSharingContent',query:{postid:item.contentId}}">{{ item.title }}</router-link></h3>
              <div class="user">
                <el-avatar
                  :src="axiosimagesurl + item.image"
                  :key="axiosimagesurl + item.image"
                  :size="25"
                ></el-avatar>

                <span class="userName">{{ item.users_name }}</span>
              </div>
            </el-col>
            <el-col :span="3">
              <span class="time">{{ item.create_time }}</span>
            </el-col>
          </el-row>
        </el-card>
      </div>

      <div v-if="postType == '0'">
        <el-card
          v-for="(item, index) in historyLikeList"
          :key="index"
          shadow="hover"
        >
          <el-row type="flex" justify="space-between">
            <el-col :span="3" v-show="item.cam_image != null">
              <el-image
                style="width: 118px; height: 74px; border-radius: 4px"
                fit="cover"
                :src="axiosimagesurl + item.cam_image"
              ></el-image
              ></el-col>
            <el-col :span="17" style="margin: 3px 0 0 10px ;">
              <h3><router-link target="_blank" :to="{path:'/CampusSharing/CampusSharingContent',query:{postid:item.contentId}}">{{ item.title }}</router-link></h3>
              <div class="user">
                <el-avatar
                  :src="axiosimagesurl + item.image"
                  :key="axiosimagesurl + item.image"
                  :size="25"
                ></el-avatar>

                <span class="userName">{{ item.nickname }}</span>
              </div>
            </el-col>
            <el-col :span="3">
              <span class="time">{{ item.updateTime }}</span>
            </el-col>
          </el-row>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
// import { UserPostLike } from '@/api/PostLike'
import { MessageBox } from 'element-ui'
import {del, getPage} from '../../api/history'
import {setStore} from '../../utils/store'

export default {
  name: 'HistoryList',
  props: {
    postType: '',
    removeType: '',
    activeLabel: ''
  },
  data () {
    return {
      loading: false,
      history: [],
      page: {
        total: 0,
        pageSize: 9,
        currentPage: 1,
        postType: '1'
      },
      historyGoodsList: [],
      historyPostList: [],
      historyLikeList: [],
      total: 0,
      listLength: 0
    }
  },
  watch: {
    postType () {
      this.init(this.postType)
    }
  },
  computed: {
    noMore () {
      return this.page.currentPage >= this.page.pageTotal
    },
    // eslint-disable-next-line vue/no-dupe-keys
    disabled () {
      return this.loading || this.noMore
    }
  },
  methods: {
    init (historyName) {
      this.historyGoodsList = []
      this.historyPostList = []
      this.historyLikeList = []
      this.page.pageSize = 9
      this.page.total = 0
      this.page.currentPage = 1
      this.page.postType = historyName
      console.log(this.page)
      this.getList()
    },
    getList () {
      this.loading = true
      getPage(this.page).then((res) => {
        this.loading = false
        if (res.code === 200) {
          console.log(res.data)
          for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].postType == '1') {
              this.historyGoodsList.push(res.data[i])
            } else if (res.data[i].postType == '2') {
              this.historyPostList.push(res.data[i])
            } else {
              this.historyLikeList.push(res.data[i])
            }
          }
          console.log(this.historyGoodsList)
          console.log(this.historyPostList)
          console.log(this.historyLikeList)
          this.page.total = res.dataTotal
        }
        this.listLength = this.historyGoodsList.length
        this.loading = false
      })
    },
    detailFun (item) {
      setStore({ name: 'posts', content: item })
      this.$router.push({path: '/detail'})
    },
    removeAll () {
      MessageBox.confirm(
        '确定清空' + this.activeLabel + '的浏览记录吗',
        '确认提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          cancelButtonClass: 'cancel',
          confirmButtonClass: 'config',
          closeOnClickModal: false,
          closeOnPressEscape: false
        }
      )
        .then(() => {
          del({
            userId: this.$store.state.userId,
            postType: this.postType
          }).then(() => {
            this.init()
          })
        })
        .catch(() => {})
    },
    dateTime (date) {
      if (
        this.$moment(new Date()).format('yyyy-MM-DD') ===
        this.$moment(date).format('yyyy-MM-DD')
      ) {
        return this.$moment(date).format('HH:mm')
      }
      return this.$moment(date).format('yyyy-MM-DD HH:mm')
    }
  },
  created () {
    this.getList()
  }
}
</script>

<style  scoped>
@media screen and (max-width:1100px) {
  .el-col-3{
    margin-right: 40px !important;
  }
}
@media screen and (max-width:870px) {
  .el-col-3{
    margin-right: 70px !important;
  }
  .history{
    width: 450px !important;
  }
}
@media screen and (max-width:550px) {
  .el-col-3{
    margin-right: 50px !important;
  }
  .el-image{
    width: 80px !important;
    height: 60px !important;
  }
  .history{
    width: 390px !important;
  }
  h3{
    max-height: 18px !important;
  }
  .time{
    font-size: 12px !important;
  }
}
@media screen and (max-width:500px) {
  .el-col-3{
    margin-right: 30px !important;
  }
  .el-image{
    width: 70px !important;
    height: 50px !important;
  }
  .history{
    width: 330px !important;
  }
  .el-card {
    width: 330px !important;
    height: 80px !important;
  }
  .user .userName{
    max-width: 70px !important;
    text-overflow:ellipsis;
    overflow: hidden !important;
  }
}
@media screen and (max-width:400px) {
  .el-col-3{
    margin-right: 35px !important;
  }
  .el-image{
    width: 70px !important;
    height: 50px !important;
  }
  .el-card {
    width: 310px !important;
  }
  .time{
    font-size: 12px !important;
  }
  .user .el-avatar{
    width: 20px !important;
    height: 20px !important;
  }
  .history{
    width: 310px !important;
  }
}
@media screen and (max-width:390px) {
  .el-image{
    width: 60px !important;
    height: 50px !important;
  }
  .el-col-3{
    margin-right: 25px !important;
  }
  .user span{
    font-size:12px !important;
  }
  .history{
    width: 300px !important;
  }
  .el-card {
    width: 300px !important;
  }

}
@media screen and (max-width:376px) {
  .history{
    width: 285px !important;
  }
  .el-card {
    width: 285px !important;
  }
}
@media screen and (max-width:360px) {
  .el-col-3{
    margin-right: 25px !important;
  }
  .history{
    width: 280px !important;
  }
  .el-card {
    width: 280px !important;
  }
}
.el-card {
  margin-top: 7px !important;
}
h3{
  display: block;
  overflow: hidden;
}
.history {
  position: relative;
  padding: 0 10px;
  min-height: 760px;
}
.user {
  margin-top: 10px;
  display: flex;
  margin-left:-5px;
}
.block {
  margin-bottom: 35px;
}
.block a{
  color: #222226;
  text-decoration: none;
}
.block a:hover{
  color:  #0b94c4;
}
.user span {
  color: #999aaa;
  margin-left: 5px;
  font-size: 14px;
  line-height: 25px;
}
.block >>> .el-row {
  margin-top: 10px;
}
.block >>> .el-timeline-item__wrapper {
  padding-left: 20px;
}
.time {
  position: absolute;
  bottom: 5%;
  color: #999aaa;
  right: 0;
  font-size: 14px;
}
.block >>> .el-card__body {
  padding: 10px;
}
.clea {
  flex:1;
  text-align: right;
  height: 40px;
}
</style>
