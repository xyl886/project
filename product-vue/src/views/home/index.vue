<template>
  <div style="font-size: 14px;margin: 20px 150px;" v-loading="loading">
    <div>
      <el-carousel height="500px" >
        <el-carousel-item v-for="(item,index) in banners" :key="index">
         <el-image :src="item.imgPath" style="width: 100%;height: 500px;"></el-image>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="margin: 20px 0;">
      <h2>闲置帖子列表</h2>
    </div>

    <div class="posts-box" v-for="(item,index) in posts" :key="index">
      <div class="posts-item" v-for="(item2,index2) in item" :key="item2.id" @click="detailFun(item2)">
        <div style="padding: 10px 20px;">
          <div style="text-align: center;">
            <el-image :src="item2.coverPath" style="width: 100%;height: 170px;"></el-image>
          </div>
          <div style="height: 36px;;overflow: hidden;margin-top: 20px;">
            <div style="overflow: hidden;text-overflow: ellipsis;">
              {{item2.title}}
            </div>
          </div>
          <div style="display: flex;margin-top: 20px;">
            <div class="posts-item-price">
              ¥{{item2.price}}
            </div>
            <div class="posts-item-des">
              {{item2.schoolName}}
              |
              {{item2.browseNum}}次浏览
            </div>
          </div>
        </div>
      </div>
    </div>

    <div style="text-align: right;margin-top: 10px;">
      <el-pagination
        background
        :current-page.sync="page.currentPage"
        :page-sizes="[15, 25, 50, 100]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
        @size-change="sizeChange"
        @current-change="currentChange"
      />
    </div>

    <div v-if="posts.length === 0">
      <el-empty description="暂无数据"></el-empty>
    </div>

  </div>

</template>

<script>
import {listAll} from '@/api/banner'
import {getPage} from '@/api/posts'
import {setStore} from '@/utils/store'
export default {
  data () {
    return {
      loading: false,
      banners: [],
      posts: [],
      page: {
        total: 0,
        pageSize: 25,
        currentPage: 1,
        postsType: 1
      }
    }
  },
  mounted () {
    this.listAllFun()
    this.getPageFun()
  },
  methods: {
    listAllFun () {
      this.banners = []
      listAll().then(res => {
        if (res.code === 200) {
          this.banners = res.data
        }
      })
    },
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
      this.posts = []
      getPage(this.page).then(res => {
        this.loading = false
        if (res.code === 200) {
          let count = 0
          let arr = []
          for (let i = 0; i < res.data.length; i++) {
            count++
            if (count <= 5) {
              arr.push(res.data[i])
            }
            if (count === 5 || i === (res.data.length - 1)) {
              this.posts.push(arr)
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
    detailFun (posts) {
      setStore({name: 'posts', content: posts})
      this.$router.push({path: '/detail'})
    }
  }
}
</script>

<style>
  .posts-box{
    display: flex;
  }
  .posts-item{
    width: calc(20% - 10px);
    height: 300px;
    margin: 10px 10px 5px 0;
    background-color: #ffffff;
    cursor: pointer;
    box-shadow: 1px 1px 10px rgba(0,0,0, 0.08);
  }
  .posts-item:hover{
    box-shadow: 1px 1px 10px rgba(0,0,0, 0.2);
  }
  .posts-item:hover img{
    transform: translate(0px, 0px) scale(1.01) rotate(0deg);
  }
  .posts-item-price{
    color: #f30;
    font-size: 16px;
  }
  .posts-item-des{
    color: #666;
    font-size: 12px;
    flex: 1;
    text-align: right;
  }
</style>
