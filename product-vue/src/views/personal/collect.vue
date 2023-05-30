<template>
    <div style="font-size: 14px;padding: 10px 0 0 20px;background-color: #f5f7f9;" v-loading="loading">
      <div style="border-bottom: 1px solid #ccc;font-weight: bolder;font-size: 24px;line-height: 50px;">我的收藏</div>
      <div class="collect-box" v-for="(item,index) in collects" :key="index">
        <div class="collect-item" v-for="(item2,index2) in item" :key="item2.id">
          <el-card style="padding: 0;">
            <div slot="header" style="cursor: pointer;" @click="detailFun(item2.posts)">
<!--              <div style="text-align: center;">-->
                <el-image style="width: 320px;height: 200px" :src="item2.posts.coverPath" class="collect-box-img">
                  <div slot="error" class="image-slot" style="text-align: center;padding: 25%;font-size: 25px;"
                       v-if="item2.posts.coverPath"><i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
<!--              </div>-->
              <div class="collect-box-title">
                {{item2.posts.title}}
              </div>
            </div>
            <div>
              <span style="color: #999;">收藏于:{{item2.createTime}}</span>
              <el-dropdown style="float: right;">
                <el-button style="padding: 3px 0" type="text"><i class="el-icon-more"></i></el-button>
                <el-dropdown-menu >
                  <el-dropdown-item @click.native="addCollectFun(item2.posts)">取消收藏</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </el-card>
        </div>
      </div>

      <div v-if="collects.length > 0" style="text-align: center;margin: 50px 0 30px 0;">
        <el-pagination
          background
          :current-page.sync="page.currentPage"
          :page-sizes="[9, 18, 27, 45, 81]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
          @size-change="sizeChange"
          @current-change="currentChange"
        />
      </div>
      <div v-if="collects.length === 0">
        <el-empty description="暂无数据"></el-empty>
      </div>
    </div>
</template>

<script>
import {getPage, addCollect} from '@/api/collect'
import {setStore} from '@/utils/store'
import dayjs from 'dayjs'
export default {
  data () {
    return {
      loading: false,
      collects: [],
      page: {
        total: 0,
        pageSize: 9,
        currentPage: 1
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
    formatDate (date) {
      const currentDate = dayjs()
      const targetDate = dayjs(date)
      if (currentDate.day() === targetDate.day()) {
        return targetDate.format('今天 HH:mm')
      } else if (currentDate.year() === targetDate.year()) { // 如果日期的年份与当前年份相同，则只显示月日
        return targetDate.format('M-D')
      } else {
        return targetDate.format('YYYY-M-D')
      }
    },
    getPageFun () {
      this.loading = true
      this.collects = []
      getPage(this.page).then(res => {
        this.loading = false
        if (res.code === 200) {
          console.log(res.data)
          let count = 0
          let arr = []
          for (let i = 0; i < res.data.length; i++) {
            console.log(res.data[i].createTime)
            res.data[i].createTime = this.formatDate(res.data[i].createTime)
            console.log(res.data[i].createTime)
            count++
            if (count <= 3) {
              arr.push(res.data[i])
            }
            if (count === 3 || i === (res.data.length - 1)) {
              this.collects.push(arr)
              console.log(this.collects)
              arr = []
              count = 0
            }
          }
          this.page.total = res.dataTotal
        }
      }, error => {
        this.loading = false
      })
    },
    addCollectFun (item) {
      addCollect(item.id, '1').then(res => {
        if (res.code === 200) {
          this.getPageFun()
          this.$message.success(res.msg)
        }
      })
    },
    detailFun (posts) {
      setStore({name: 'posts', content: posts})
      this.$router.push({path: '/detail'})
    }
  }
}
</script>

<style scoped>
  .collect-box{
    display: flex;
  }
  .collect-item{
    width: calc(33.3% - 10px);
    margin: 10px 10px 5px 0;
  }
  .collect-item:hover{
    box-shadow: 1px 1px 10px rgba(0,0,0, 0.2);
    transform: translate(0px, 0px) scale(1.01) rotate(0deg);
  }
  .collect-item:hover .collect-box-title{
    color: #ff3300;
  }
  .collect-box-img{
    width: 100%;
  }
  .collect-box-title{
    height: 20px;
    overflow: hidden;
  }
</style>
