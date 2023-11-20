<template>
  <div class="history" v-loading="loading">
    <div class="block">
      <el-row style="padding-bottom: 20px">
        <el-col :span="4">
<!--          <div class="search-box" :class="{ active: InputFocused }"> </div>-->
<!--            <div class="el-icon-search" @click="handleSearch"></div>-->
<!--            <input-->
<!--              v-model="searchText"-->
<!--              placeholder="请输入搜索内容"-->
<!--              @input="handleInput"-->
<!--              @focus="InputFocused = true"-->
<!--              @blur="InputFocused = false"-->
<!--              class="search-box-input"-->
<!--              type="text"/>-->
<!--            <el-select-->
<!--              v-model="searchText"-->
<!--              placeholder="请输入搜索内容"-->
<!--              @input="handleInput"-->
<!--              @focus="InputFocused = true"-->
<!--              @blur="InputFocused = false"-->
<!--              class="search-box-input"-->
<!--              filterable>-->
<!--            </el-select>-->
            <el-input v-model="page.title" clearable style="width: 150px" size="small" placeholder="请输入内容" />
        </el-col>
        <el-col :span="8">
          <div class="block">
            <el-date-picker
                size="small"
                v-model="dateTime"
                type="daterange"
                value-format = "yyyy-MM-dd HH:mm:ss"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
                @change="handleDateTimeChange">
            </el-date-picker>
          </div>
        </el-col>
        <el-col :span="2">
          <div><el-button size="small" type="primary" @click="handleFind">查询</el-button>
          </div>
        </el-col>
        <el-col :span="6">
          <div><el-button size="small" @click="resetQuery">重置</el-button></div>
        </el-col>
        <el-col :span="2">
          <div>
            <el-button
              type="danger"
              plain
              size="small"
              @click="removeAll"
              v-show="postType !== '0'&& page.total!==0"
            >清空历史</el-button>
          </div>
        </el-col>
      </el-row>
      <div v-if="page.total == 0" style="text-align: center">
        <span style="color: #999aaa; font-size: 15px">近期没有浏览记录</span>
      </div>
      <div>
        <el-timeline>
          <el-timeline-item
              :timestamp="item.updateTime"
              v-for="(item, index) in history"
              :key="item.id"
              placement="top">
            <el-card>
              <el-row type="flex" justify="space-between">
                <el-col :span="3" style="cursor: pointer;" v-show="item.postCoverPath != null">
                  <el-image
                    @click="detailFun(item.posts)"
                    style="width: 118px; height: 74px; border-radius: 4px"
                    fit="cover"
                    :src="item.postCoverPath"
                  ></el-image>
                </el-col>
                <el-col :span="16">
                  <h3 class="title" :key="item.id" style="cursor: pointer;" @click="detailFun(item.posts)">{{ item.postTitle}}</h3>
                  <div class="user">
                    <el-image :src="item.avatar" style="cursor: pointer;width: 25px;height: 25px;border-radius: 50%;">
                    </el-image>
                  <span class="userName">{{ item.nickname }}|
                    </span><div style="margin: 4px;font-size: 12px;text-align: center;color: rgba(0,0,0,0.45)">{{item.schoolName}}</div>
                  </div>
                </el-col>
                <el-col :span="1">
                  <i class="el-icon-delete"
                     style="line-height:75px;cursor: pointer;"
                     @click="remove(item.id,item.userId)"></i>
                </el-col>
              </el-row>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
      <div style="text-align: center;color: rgb(168, 176, 183);">
        <p v-if="loading">加载中...</p>
        <p v-if="!loading&&page.currentPage===pageTotal">没有更多了</p>
      </div>
      <div style="text-align: right;margin-top: 10px;">
        <el-pagination
            background
            :current-page.sync="page.currentPage"
            :page-sizes="[10, 20, 40, 80]"
            :page-size="page.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="page.total"
            @size-change="sizeChange"
            @current-change="currentChange"/>
      </div>
    </div>
  </div>
</template>

<script>
// import { UserPostLike } from '@/api/PostLike'
import { MessageBox } from 'element-ui'
import {del, getLikePage, getPage} from '../../api/history'
import {setStore} from '../../utils/store'
import {formatDate} from '../../utils/date'
import {mapGetters} from 'vuex'

export default {
  name: 'HistoryList',
  props: {
    postType: '',
    removeType: '',
    activeLabel: ''
  },
  data () {
    return {
      dateTime: [],
      command: '最近收藏',
      options: [],
      value: '',
      InputFocused: false,
      searchText: '',
      loading: false,
      history: [],
      selectedIds: [],
      pageTotal: '',
      page: {
        total: 0,
        pageSize: 9,
        currentPage: 1,
        postsType: 1,
        title: '',
        dateTime: '',
        startTime: '',
        endTime: ''
      },
      total: 0,
      listLength: 0
    }
  },
  watch: {
    postType () {
      this.init(this.postsType)
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  methods: {
    handleDateTimeChange (value) {
      if (value && value.length === 2) {
        this.page.startTime = value[0]
        this.page.endTime = value[1]
      } else {
        this.page.startTime = ''
        this.page.endTime = ''
      }
    },
    init (postType) {
      this.history = []
      this.page.postsType = postType
      console.log(this.page)
      this.getList()
    },
    handleCommand (command) {
      this.command = command
    },
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
    resetQuery () {
      this.page.categoryId = 1
      this.page.title = ''
      this.page.startTime = ''
      this.page.endTime = ''
      this.dateTime = ''
      this.getList()
    },
    sizeChange (pageSize) { // 页数
      this.page.pageSize = pageSize
      this.getList()
    },
    currentChange (currentPage) { // 当前页
      this.page.currentPage = currentPage
      this.getList()
    },
    handleFind () {
      this.getList()
    },
    getList () {
      this.loading = true
      this.history = []
      if (this.page.postsType === '0') {
        getLikePage(this.page).then((res) => {
          this.loading = false
          if (res.code === 200) {
            console.log(res.data)
            this.history = res.data
            for (let i = 0; i < res.data.length; i++) {
              this.history[i].updateTime = formatDate(res.data[i].updateTime)
            }
            this.page.total = res.dataTotal
            this.pageTotal = res.pageTotal
          }
          this.loading = false
        })
      } else {
        getPage(this.page).then((res) => {
          this.loading = false
          if (res.code === 200) {
            console.log(res.data)
            this.history = res.data
            for (let i = 0; i < res.data.length; i++) {
              this.history[i].updateTime = formatDate(res.data[i].updateTime)
            }
            this.page.total = res.dataTotal
            this.pageTotal = res.pageTotal
          }
          this.loading = false
        })
      }
    },
    detailFun (item) {
      setStore({ name: 'posts', content: item })
      this.$router.push({path: '/detail'})
    },
    remove (ids, userId) {
      console.log(ids + ',' + userId)
      del(ids, userId)
        .then((res) => {
          if (res.code === 200) {
            this.$message.success(res.msg)
          } else {
            this.$message.warning(res.msg)
          }
          this.getList()
        }).catch((error) => {
          this.$message.error(error)
        })
    },
    removeAll () {
      // console.log(this.history)
      this.selectedIds = this.history.map(history => history.id).join(',')
      console.log(this.selectedIds)
      MessageBox.confirm(
        '确定清空' + this.activeLabel + '的浏览记录吗',
        '确认提示',
        {
          cancelButtonText: '取消',
          confirmButtonText: '确定',
          cancelButtonClass: 'cancel',
          confirmButtonClass: 'config',
          closeOnClickModal: false,
          closeOnPressEscape: false
        }
      ).then(() => {
        const userId = this.history[0].userId
        this.remove(this.selectedIds, userId)
      })
        .catch(() => {
        })
    }
  },
  created () {
    this.getList()
  }
}
</script>

<style  scoped>
.el-timeline{
  padding-left: 0;
}
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
  color: rgba(0, 0, 0, 0.85);
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
.search-box {
  display: flex;
  width: 180px;
  float: left;
  align-items: center;
  border: 1px solid #ccc;
  padding: 4px;
  border-radius: 25px;
  transition: border-color 0.3s;
}
.search-box.active {
  border-color: #007bff; /* 修改为你想要的激活状态的边框颜色 */
}
.search-box-input {
  border: none;
  outline: none;
  width: 145px;
  height: 24px;
  display: inline-block;
  background-color: #f5f7f9;
}
.el-icon-search,
.el-icon-close {
  cursor: pointer;
}
.el-icon-search:hover,
.el-icon-close:hover{
  border-color: #007bff;
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
.clear {
  flex:1;
  text-align: right;
  height: 40px;
}
.userName{
  font-size: 12px;
  color: #000000;
  cursor: pointer;
}
.title:hover,.userName:hover{
  color:#007bff;
}
</style>
