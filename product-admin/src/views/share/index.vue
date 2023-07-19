<template>
  <div class="body">
    <div style="font-size: 14px;margin: 0 100px;padding: 20px 0;" v-infinite-scroll="loadFun">
        <el-row>
          <el-col :span="18">
            <div class="share-tab" style="width: 960px; display: inline-block;padding: 0 10px">
          <el-tabs style="min-height: 800px;"  tab-position="left" v-model="activeName" @tab-click="handleClick">
         <el-tab-pane  v-for="tab in Tabs" @tab-click="handleClick" :label="tab.categoryName" :name="tab.id" :key="tab.id">
           <All :ref="tab.id"></All>
         </el-tab-pane>
          </el-tabs>
            </div>
        </el-col>
        <el-col :span="6" style="position: relative;" class="right-search">
          <div class="recommendation" :class="{ 'fixed': isFixed }">
            <div class="title-header"><b>向你推荐</b></div>
            <div class="title"  v-for="(item, index) in recommendationItems" :key="index">
           <span><i class="iconfont icon-comment"></i></span>{{ item.title }}
            </div>
          </div>
        </el-col>
        </el-row>
      </div>
  </div>
</template>

<script>
import All from './all.vue'
import BackToTop from '../../page/top/BackToTop.vue'
import {getToken} from '../../utils/auth'
import {listAllCategory} from '../../api/posts'

export default {
  components: {
    All,
    BackToTop
  },
  data () {
    return {
      isFixed: false,
      activeName: '0',
      tabs: [
        { categoryName: '全部', id: '0' },
        { categoryName: '关注', id: '1', requiresLogin: true }
        // { categoryName: '学习', name: '2' },
        // { categoryName: '生活', name: '3' },
        // { categoryName: '娱乐', name: '4' },
        // { categoryName: '求助', name: '5' },
        // { categoryName: '就业', name: '6' },
        // { categoryName: '新闻/公告', name: '7' },
      ],
      title: '向你推荐',
      recommendationItems: [
        { id: 1, title: '推荐内容1' },
        { id: 2, title: '推荐内容2' },
        { id: 1, title: '推荐内容1' },
        { id: 2, title: '推荐内容2' },
        { id: 1, title: '推荐内容1' },
        { id: 2, title: '推荐内容2' },
        { id: 1, title: '推荐内容1' },
        { id: 2, title: '推荐内容2' },
        { id: 2, title: '推荐内容2' },
        { id: 3, title: '推荐内容3' }
      ],
      school: null
    }
  },
  watch: {
  },
  computed: {
    Tabs () {
      if (getToken()) {
        return this.tabs
      } else {
        return this.tabs.filter(tab => !tab.requiresLogin)
      }
    }
  },
  beforeCreate () {
    listAllCategory().then(res => {
      console.log(res.data)
      this.tabs = this.tabs.concat(res.data)
      console.log(this.tabs)
    })
  },
  mounted () {
    this.$nextTick(() => {
      this.$refs[this.activeName][0].init(this.school)
    })
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy () {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    handleScroll () {
      this.isFixed = document.documentElement.scrollTop > 5
    },
    handleClick (tab, event) {
      if (tab.index !== '0') {
        this.school = tab.index
      } else {
        this.school = null
      }
      this.$refs[tab.name][0].init(this.school)
      console.log(tab.name + ',' + this.school)
    },
    loadFun () {
      this.$refs[this.activeName][0].load()
    }
  }
}
</script>

<style>
  .share-tab .el-tabs__header{
    background-color: #ffffff!important;
    position: fixed;
  }
  .share-tab .el-tabs__content{
    max-width: 800px;
    left: 150px;
  }
  .share-tab .el-tabs__nav{
    height: 880px!important;
    width: 100px!important;
    line-height: 60px!important;
  }
  .share-tab .el-tabs__nav-prev{
    display: none!important;
  }
  .el-tabs--left .el-tabs__nav-wrap.is-left.is-scrollable{
    padding: 0!important;
  }
  .share-tab .el-tabs__active-bar{
    display: none!important;
  }
  .share-tab .el-tabs__item{
    text-align: center!important;
  }
</style>
<style scoped>
.body{
  margin: 0;
  width: 100%;
  height: calc(100%- 120px);
  background-image: url("../../../public/img/back-ground.png");
  background-size: cover;
  background-attachment: fixed;
}
@media screen and (max-width: 1500px) {
  .right-search {
    display: none!important;
  }
}
.search{
  display: inline-block;
  background-color: #ffffff!important;
  position: relative;
  width: 380px;
  height: 60px;
}
.input-with-select{
  width: 200px;
  height: 60px;
  margin-left: -110px;
  float: right;
  line-height: 60px;
  margin-top: auto;
}
.recommendation {
  width: 360px;
  height: 440px;
  background-color: white;
  border-radius: 5px;
  position: relative;
  padding: 10px;
  margin-top: 5px;
}
.fixed{
  position: fixed;
  top: 70px;
}
.title-header{
  font-size: 20px;
}
.title,.title-header{
  padding: 0 10px;
  height: 40px;
  line-height: 40px;
}
</style>
