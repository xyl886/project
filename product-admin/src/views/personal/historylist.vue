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
            <el-select
              v-model="searchText"
              placeholder="请输入搜索内容"
              @input="handleInput"
              @focus="InputFocused = true"
              @blur="InputFocused = false"
              class="search-box-input"
              filterable>
              <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
            </el-select>
        </el-col>
        <el-col :span="2">
          <div @click="handleClear"><el-button>重置</el-button></div>
        </el-col>
        <el-col :span="15">
          <div><el-button type="primary">查询</el-button>
          </div>
        </el-col>
        <el-col :span="2">
          <div>
            <el-button
              type="danger"
              plain
              @click="removeAll"
              v-show="postType !== '0'&& page.total!==0"
            >清空历史</el-button>
          </div>
        </el-col>
      </el-row>
      <div v-if="page.total == 0" style="text-align: center">
        <span style="color: #999aaa; font-size: 15px">近期没有浏览记录</span>
      </div>
      <div v-if="postType === '1'">
        <el-timeline>
          <el-timeline-item  :timestamp="item.updateTime"  v-for="(item, index) in historyGoodsList" :key="index" placement="top">
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
      <div v-if="postType === '2'">
        <el-timeline>
          <el-timeline-item  :timestamp="item.updateTime"  v-for="(item, index) in historyPostList" :key="index" placement="top">
            <el-card>
              <el-row type="flex" justify="space-between">
                <el-col :span="3" style="cursor: pointer;" v-show="item.postCoverPath != null">
                  <el-image
                    style="width: 118px; height: 74px; border-radius: 4px"
                    fit="cover"
                    :src="item.postCoverPath"
                  ></el-image>
                </el-col>
                <el-col :span="16">
                  <h3 class="title" :key="item.id" style="cursor: pointer;" @click="detailFun(item.posts)">{{ item.postTitle}}</h3>
                  <div class="user">
                    <el-image  @click="detailFun(item.posts)" :src="item.avatar" style="cursor: pointer;width: 25px;height: 25px;border-radius: 50%;">
                    </el-image>
                    <span class="userName">{{ item.nickname }}|
                    </span><span style="font-size: 12px;text-align: center;color: rgba(0,0,0,0.45)">{{item.schoolName}}</span>
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
      <div v-if="postType === '0'">
        <el-timeline>
          <el-timeline-item  :timestamp="item.updateTime"  v-for="(item, index) in historyLikeList" :key="index" placement="top">
            <el-card>
              <el-row type="flex" justify="space-between">
                <el-col :span="3" style="cursor: pointer;" v-show="item.postCoverPath != null">
                  <el-image
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
                    </span><span style="margin-left: 4px">{{item.schoolName}}</span>
                  </div>
                </el-col>
              </el-row>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
      <div style="text-align: center;color: rgb(168, 176, 183);">
        <p v-if="loading">加载中...</p>
        <p v-if="!loading">没有更多了</p>
      </div>
    </div>
  </div>
</template>

<script>
// import { UserPostLike } from '@/api/PostLike'
import { MessageBox } from 'element-ui'
import {del, getPage} from '../../api/history'
import {setStore} from '../../utils/store'
import {formatDate} from '../../utils/date'

export default {
  name: 'HistoryList',
  props: {
    postType: '',
    removeType: '',
    activeLabel: ''
  },
  data () {
    return {
      command: '最近收藏',
      options: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }, {
        value: '选项4',
        label: '龙须面'
      }, {
        value: '选项5',
        label: '北京烤鸭'
      }],
      value: '',
      InputFocused: false,
      searchText: '',
      loading: false,
      history: [],
      selectedIds: [],
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
  },
  methods: {
    init () {
      this.historyGoodsList = []
      this.historyPostList = []
      this.historyLikeList = []
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
    handleClear () {
      // 点击清除按钮触发
      this.searchText = ''
    },
    getList () {
      this.loading = true
      getPage(this.page).then((res) => {
        this.loading = false
        if (res.code === 200) {
          console.log(res.data)
          this.history = res.data
          for (let i = 0; i < res.data.length; i++) {
            this.history[i].updateTime = formatDate(res.data[i].updateTime)
            if (this.history[i].postType == '1') {
              this.historyGoodsList.push(this.history[i])
            } else if (this.history[i].postType == '2') {
              this.historyPostList.push(this.history[i])
            } else {
              this.historyLikeList.push(this.history[i])
            }
          }
          this.page.total = res.dataTotal
        }
        this.loading = false
      })
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
          this.init()
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
