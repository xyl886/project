<template>
  <div class="body" v-infinite-scroll="loadFun">
    <div style="font-size: 14px;margin: 0 160px;">
      <div class="share-tab" style=" display: inline-block;width: 1200px;">
        <el-row>
          <el-col :span="18">
          <el-tabs style="" v-model="activeName" @tab-click="handleClick">
         <el-tab-pane v-for="tab in tabs" @tab-click="handleClick" :label="tab.label" :name="tab.name" :key="tab.name">
           <All :ref="tab.name"></All>
         </el-tab-pane>
          </el-tabs>
        </el-col>
        <el-col :span="6">
          <div> <el-input v-model="searchText" name="search" slot="label" placeholder="搜索"></el-input></div>
          <div class="recommendation">
            <div style="margin:0 0 5px 15px;"><b>向你推荐</b></div>
            <div class="title"  v-for="(item, index) in recommendationItems" :key="index">
<!--              <img src="../assets/hot.png" width="14" height="14" style="margin:12px 5px 0 0 ;"/>-->
              <router-link class="title"
                           target="_blank"
                           :to="{
                  path: '/CampusSharing/CampusSharingContent',
                  query: { postId: item.id },
                }"
              >{{ item.title }}</router-link
              >
            </div>
          </div>
      </el-col>
        </el-row>
      </div>
    </div>
    <BackToTop></BackToTop>
  </div>
</template>

<script>
import All from './all.vue'
import BackToTop from '../../page/top/BackToTop.vue'

export default {
  components: {
    All,
    BackToTop
  },
  data () {
    return {
      searchText: '',
      activeName: 'first',
      tabs: [
        { label: '全部', name: 'first' },
        { label: '学习', name: 'second' },
        { label: '生活', name: 'third' },
        { label: '娱乐', name: 'fourth' },
        { label: '求助', name: 'fifth' },
        { label: '就业', name: 'sixth' },
        { label: '新闻/公告', name: 'seventh' },
        { label: '我的分享', name: 'eighth' },
        { label: '我的关注', name: 'ninth' }
      ],
      title: '向你推荐',
      recommendationItems: [
        { id: 1, title: '推荐内容1' },
        { id: 2, title: '推荐内容2' },
        { id: 3, title: '推荐内容3' }
        // ... 还可以添加更多推荐内容
      ],
      school: null
    }
  },
  computed: {
    // Tabs () {      , requiresLogin: true
    //   if (getToken()) {
    //     return this.tabs
    //   } else {
    //     return this.tabs.filter(tab => !tab.requiresLogin)
    //   }
    // }
  },
  mounted () {
    this.$nextTick(() => {
      this.$refs[this.activeName][0].init(this.school)
    })
  },
  methods: {
    handleClick (tab, event) {
      if (tab.index !== '0') {
        this.school = tab.index
      } else {
        this.school = null
      }
      this.$refs[tab.name][0].init(this.school)
      console.log(tab, event)
    },
    loadFun () {
      this.$refs[this.activeName][0].load()
    }
  }
}
</script>

<style>
  .body{
    margin: 0;
    padding: 20px 0 0 0;
    width: 100%;
    height: calc(100% - 80px);
    background-image: url("../../../public/img/background-detail.jpg");
    background-size: cover;
    background-attachment: fixed;
    overflow: scroll;
  }
  .share-tab .el-tabs__header{
    background-color: #ffffff!important;
    padding: 0 20px!important;
  }
  .share-tab .el-tabs__nav{
    height: 60px!important;
    line-height: 60px!important;
  }
</style>
<style scoped>
.search{
  display: inline-block;
  background-color: #ffffff!important;
  float: right;
  position: relative;
  width: 300px;
  height: 60px;
  top: 0;
  margin-right: 300px;
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
  width: 353px;
  height: 440px;
  background-color: white;
  border-radius: 5px;
  position: fixed;
  top: 30px;
  margin-left: 10px;
  margin-top: 90px;
  padding: 10px 0 0;
  /*width: 353px;*/
  /*height: 440px;*/
  /*background-color: white;*/
  /*border-radius: 5px;*/
  //position: -webkit-sticky;
  //position: sticky;
  /*top: -10px;*/
  //margin-top: 70px;
  //padding: 10px 0 0;
}
</style>
