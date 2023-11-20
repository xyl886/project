<template>
  <div style="font-size: 14px;margin: 20px 150px;" v-loading="loading">
    <div>
      <el-carousel height="500px" :autoplay="true" :interval="3000">
        <el-carousel-item v-for="(item,index) in banners" :key="index">
         <el-image :src="item.imgPath" style="width: 100%;height: 500px;"></el-image>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="margin: 20px 0;">
      <el-row>
        <el-col :span="16"><div class="grid-content bg-purple">
          <h2 style="display: inline-block">闲置帖子</h2></div></el-col>
        <el-col :span="8"><div class="grid-content bg-purple-light">
          <!-- 搜索框 -->
          <div style="display:inline-block;margin-top: 12px;" v-if="$route.path==='/index'">
            <el-input  clearable style="width: 70%" placeholder="请输入内容"  v-model="page.tltle" class="input-with-select">
              <el-select style="min-width:80px;max-width: 200px" v-model="page.categoryId" slot="prepend" placeholder="" value="1">
                <el-option v-for="tabs in tabs" :key="tabs.id" :label="tabs.categoryName" :value="tabs.id"></el-option>
              </el-select>
              <el-button slot="append" icon="el-icon-search" @click="getPageFun()"></el-button>
            </el-input>
            <el-button icon="el-icon-refresh" style="padding: 10px!important;" @click="resetQuery">重置</el-button>
          </div></div></el-col>
      </el-row>
    </div>
    <div class="posts-box" v-for="(item,index) in posts" :key="index">
      <div class="posts-item" v-for="(item2,index2) in item" :key="item2.id" @click="detailFun(item2)">
        <div style="padding: 5px;">
          <div style="text-align: center;">
            <el-image :src="item2.coverPath" style="width: 100%;height: 170px;"></el-image>
          </div>
          <div style="height: 30px;padding: 5px 10px;overflow: hidden;margin-top: 20px;">
            <div style="overflow: hidden;text-overflow: ellipsis;font-size: 15px;color: #18191c;">
              {{item2.title}}
            </div>
          </div>
          <div style="display: flex;margin-top: 15px;">
            <div class="posts-item-price">
              ¥{{item2.price}}
            </div>
            <div class="posts-item-des">
              <el-tag size="small">{{item2.categoryName}}</el-tag>
              <el-tag
                  size="small"
                  v-for="(item2,index) in item.tags"
                  :key="index"
                  style="margin:5px">
                {{ item2 }}
              </el-tag>
              <i class="el-icon-view" style="margin-left: 5px;"/>{{item2.browseNum}}
            </div>
          </div>
        </div>
      </div>
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
import {listAllCategory} from '../../api/posts'
export default {
  data () {
    return {
      select: '',
      loading: false,
      banners: [],
      posts: [],
      tabs: [],
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        postsType: 1,
        categoryId: null,
        title: null,
        status: 3
      }
    }
  },
  watch: {
    'page.categoryId': {
      handler: 'getPageFun', // 监听 page.categoryId 的变化，执行 loadData 方法
      immediate: true // 当组件首次加载时，立即执行一次 loadData 方法
    }
  },
  beforeCreate () {
    const data = {total: 0, pageSize: 10, currentPage: 1, categoryName: null}
    listAllCategory(data).then(res => {
      console.log(res.data)
      this.tabs = res.data
      console.log(this.tabs)
    })
  },
  mounted () {
    this.listAllFun()
    // this.getPageFun()
  },
  methods: {
    resetQuery () {
      this.page.tltle = null
      this.page.categoryId = null
      this.posts = []
      this.getPageFun()
    },
    listAllFun () {
      this.banners = []
      listAll().then(res => {
        if (res.code === 200) {
          this.banners = res.data
          console.log(this.banners)
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
      console.log(this.page)
      getPage(this.page).then(res => {
        this.loading = false
        if (res.code === 200) {
          let count = 0
          let arr = []
          console.log(res.data.length)
          for (let i = 0; i < res.data.length; i++) {
            count++
            if (count <= 5) {
              arr.push(res.data[i])
            }
            if (count === 5 || i === (res.data.length - 1)) {
              this.posts.push(arr)
              console.log(this.posts)
              arr = []
              count = 0
            }
          }
          this.page.total = res.dataTotal
        }
      }, error => {
        console.log(error)
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

<style scoped>
  .el-select__input {
  width: auto !important;
  }
  .posts-box{
    display: flex;
  }
  .posts-item{
    width: calc(20% - 10px);
    height: 300px;
    margin: 5px;
    background-color: #ffffff;
    cursor: pointer;
    transition: .2s;
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
