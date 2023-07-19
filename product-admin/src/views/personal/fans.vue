<template>
  <div style="min-height:500px;font-size: 14px;padding: 10px;background-color: #ffffff;" v-loading="loading">
<!--    <div style="border-bottom: 1px solid #ccc;font-weight: bolder;font-size: 24px;line-height: 50px;">我的粉丝</div>-->
    <div class="follow-box">
      <div class="follow-item" v-for="(item,index) in follows" :key="index">
        <div>
          <el-image :src="item.userInfo.avatar" class="follow-item-img"></el-image>
        </div>
        <div>
          <div class="follow-item-nickname">
            {{item.userInfo.nickname}}
          </div>
          <div class="follow-item-time">
            {{item.userInfo.remark}}
<!--            {{item.followStatus === 2?' Ta关注了你':(item.followStatus === 1?'你关注了Ta':'你们已互关')}}-->
          </div>
        </div>
        <div class="follow-item-but-box">
          <el-button size="mini" round :type="item.followStatus !== 2?'':'warning'" @click="addFollowFun(item,item.followStatus !== 2?'1':'0')">
            {{item.followStatus !== 2?(item.followStatus === 1?'已关注':'已互关'):'关注'}}
          </el-button>
        </div>
      </div>
    </div>

    <div v-if="follows.length > 0" style="text-align: center;margin: 50px 0 30px 0;">
      <el-pagination
        background
        :current-page.sync="page.currentPage"
        :page-sizes="[10, 30, 50, 100]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
        @size-change="sizeChange"
        @current-change="currentChange"
      />
    </div>

    <div v-if="follows.length === 0">
      <el-empty description="暂无数据"></el-empty>
    </div>

  </div>
</template>

<script>
import {getPage, addFollow} from '@/api/follow'
export default {
  data () {
    return {
      loading: false,
      follows: [],
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        followType: 2
      }
    }
  },
  mounted () {
    this.getPageFun()
  },
  methods: {
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
      this.follows = []
      getPage(this.page).then(res => {
        this.loading = false
        if (res.code === 200) {
          this.follows = res.data
          this.page.total = res.dataTotal
        }
      }, error => {
        console.log(error)
        this.loading = false
      })
    },
    addFollowFun (item, deleted) {
      addFollow(item.userInfo.id, deleted).then(res => {
        if (res.code === 200) {
          if (deleted === '1') {
            item.followStatus = 2
          } else {
            item.followStatus = 3
          }
          console.info(item)
          // this.getPageFun();
          this.$message.success(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
  .follow-box{
    /*display: flex;*/
  }
  .follow-item{
    display: flex;
    height: 40px;
    padding: 10px 10px;
    border-bottom: 1px solid #f0f0f5;
  }
  .follow-item-img{
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin: 0 10px 0 0;
  }
  .follow-item-nickname{
    height: 20px;
    line-height: 20px;
  }
  .follow-item-time{
    height: 20px;
    line-height: 20px;
    color: #999;
  }
  .follow-item-but-box{
    flex: 1;
    text-align: right;
    line-height: 40px;
  }
</style>
