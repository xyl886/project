<template>
  <div class="history" v-loading="loading">
      <div class="search-box" :class="{ active: InputActive }">
     <div class="el-icon-search"  @click="handleSearch"></div>
        <input
            type="text"
            v-model="searchText"
            placeholder="请输入搜索内容"
            @input="handleInput"
            @focus="InputActive = true"
            @blur="InputActive = false"
            class="search-box-input"
        />
        <div v-show="searchText" class="el-icon-close" @click="handleClear"></div>
      </div>
    <div class="clea">
      <el-button
        size="mini"
        @click="removeAll"
        v-show="postType !== '0'&& page.total!==0"
        round>清空历史</el-button>
    </div>
    <div class="block">
      <div v-if="page.total == 0" style="text-align: center">
        <span style="color: #999aaa; font-size: 15px">近期没有浏览记录</span>
      </div>
          <el-row type="flex" justify="space-between">
          <el-timeline v-if="postType == '1'">
            <el-timeline-item
              :key="index"
              v-for="(item, index) in historyGoodsList"
              :timestamp="item.updateTime"
              placement="top">
        <el-card shadow="hover" style="width: 750px">
            <el-col :span="4">
              <el-image
                style="cursor: pointer; width: 118px; height: 74px; border-radius: 4px"
                fit="cover"
                v-show="item.postCoverPath != null"
                :key="item.id" @click="detailFun(item.posts)"
                :src="item.postCoverPath">
              </el-image>
            </el-col>
            <el-col :span="12" style="margin: 3px 0 0 10px ;">
         <div class="post-title" style=" cursor: pointer;">
           <h3 :key="item.id" @click="detailFun(item.posts)">{{ item.postTitle}}</h3>
         </div>
              <div class="user">
                <el-image :src="item.avatar" style="cursor: pointer;width: 20px;height: 20px;border-radius: 50%;"></el-image>
                <span class="userName" style="cursor: pointer;font-size: 12px">{{ item.nickname }}</span>
              </div>
            </el-col>
            <el-col :span="4">
              <i class="el-icon-delete" @click="removeOne"></i>
            </el-col>
        </el-card>
        </el-timeline-item>
        </el-timeline>
            <el-timeline v-if="postType == '2'">
              <el-timeline-item
                :key="index"
                v-for="(item, index) in historyPostList"
                :timestamp="item.updateTime"
                placement="top">
                <el-card shadow="hover" style="width: 750px;left: 20px">
                  <el-col :span="4">
                    <el-image
                      style="width: 118px; height: 74px; border-radius: 4px"
                      fit="cover"
                      v-show="item.postCoverPath != null"
                      :key="item.id" @click="detailFun(item.posts)"
                      :src="item.postCoverPath">
                    </el-image>
                  </el-col>
                  <el-col :span="12" style="margin: 3px 0 0 10px ;">
                    <h3 :key="item.id" @click="detailFun(item.posts)">{{ item.postTitle}}</h3>
                    <div class="user">
                      <el-image :src="item.avatar" style="width: 20px;height: 20px;border-radius: 50%;"></el-image>
                      <span class="userName" style="font-size: 12px">{{ item.nickname }}</span>
                    </div>
                  </el-col>
                  <el-col :span="4">
                    <i class="el-icon-delete" @click="removeOne"></i>
                  </el-col>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </el-row>
      <el-timeline v-if="postType == '0'">
        <el-timeline-item
          :key="index"
          v-for="(item, index) in historyLikeList"
          :timestamp="item.updateTime"
          placement="top">
          <el-card shadow="hover" style="width: 750px">
            <el-col :span="4">
              <el-image
                style="width: 118px; height: 74px; border-radius: 4px"
                fit="cover"
                v-show="item.postCoverPath != null"
                :key="item.id" @click="detailFun(item.posts)"
                :src="item.postCoverPath">
              </el-image>
            </el-col>
            <el-col :span="12" style="margin: 3px 0 0 10px ;">
              <h3 :key="item.id" @click="detailFun(item.posts)">{{ item.postTitle}}</h3>
              <div class="user">
                <el-image :src="item.avatar" style="width: 20px;height: 20px;border-radius: 50%;"></el-image>
                <span class="userName" style="font-size: 12px">{{ item.nickname }}</span>
              </div>
            </el-col>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
    <div style="text-align: center;color: rgb(168, 176, 183);">
      <p v-if="loading">加载中...</p>
      <p v-if="!loading&&noMore">没有更多了</p>
    </div>
  </div>
</template>

<script>

import { MessageBox } from 'element-ui'
import {del, getPage} from '../../api/history'
import {setStore} from '../../utils/store'
import dayjs from 'dayjs'

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
      searchText: '',
      InputActive: false,
      InputFocused: false,
      page: {
        total: 0,
        pageSize: 9,
        currentPage: 1,
        postType: '1'
      },
      historyGoodsList: [],
      historyPostList: [],
      historyLikeList: []
      // total: 0,
      // listLength: 0
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
    handleInput () {
      // 输入框内容变化时触发
    },
    handleSearch () {
      // 点击搜索按钮触发
      if (this.searchText) {
        // 执行搜索操作
        console.log('搜索：', this.searchText)
      }
    },
    handleClear () {
      // 点击清除按钮触发
      this.searchText = ''
    },
    // init (historyName) {
    //   this.historyGoodsList = []
    //   this.historyPostList = []
    //   this.historyLikeList = []
    //   this.page.pageSize = 9
    //   this.page.total = 0
    //   this.page.currentPage = 1
    //   this.page.postType = historyName
    //   console.log(this.page)
    //   this.getList()
    //   this.loading = false
    // },
    formatDate (date) {
      const currentDate = dayjs()
      const targetDate = dayjs(date)
      if (currentDate.day() === targetDate.day()) {
        return targetDate.format('今天 HH:mm')
      } else if (currentDate.diff(targetDate, 'day') === 1) { // 当前日期与目标日期相差1天，即为昨天
        return targetDate.format('昨天 HH:mm')
      } else if (currentDate.year() === targetDate.year()) { // 如果日期的年份与当前年份相同，则只显示月日
        return targetDate.format('M-D')
      } else {
        return targetDate.format('YYYY-M-D')
      }
    },
    getList () {
      this.loading = true
      getPage(this.page).then((res) => {
        console.log(this.page)
        this.loading = false
        if (res.code === 200) {
          console.log(res.data)
          for (let i = 0; i < res.data.length; i++) {
            res.data[i].updateTime = this.formatDate(res.data[i].updateTime)
            if (res.data[i].postType == '1') {
              this.historyGoodsList.push(res.data[i])
            } else if (res.data[i].postType == '2') {
              this.historyPostList.push(res.data[i])
            } else {
              this.historyLikeList.push(res.data[i])
            }
          }
          this.page.total = res.dataTotal
        }
      }, error => {
        this.loading = false
        console.log(error)
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
            id: this.postType
          }).then(() => {
            this.init()
          })
        })
        .catch(() => {})
    },
    removeOne () {

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
  position: relative;
  text-align: right;
  height: 40px;
  width: 200px;
  left: 600px;
}
.userName:hover,.post-title:hover{
 color: #007bff;
}
.search-box{
  display: flex;
  position: absolute;
  border-radius: 15px;
  border: 1px solid #ccd0d7;
  width: 160px;
  height: 30px;
  padding: 0 5px;
  align-items: center;
}
.search-box.active {
  border-color: #007bff; /* 修改为你想要的激活状态的边框颜色 */
}
.search-box-input{
  width: 120px;
  height: 17px;
  background-color: #F5F7F9;
  line-height: 17px;
  left: 20px;
  border: none;
  outline: none;
}
.el-icon-search .el-icon-close{
  width: 24px;
  height: 24px;
  display: inline-block;
}
.el-icon-delete{
  vertical-align: top;
  margin:15px;
  color: #99a2aa;
  position: absolute;
  padding-left: 16px;
  right: 0;
  top: 44px;
  cursor: pointer;
}
</style>
