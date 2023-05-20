<template>
  <div style="font-size: 14px;padding: 10px 0 0 20px;background-color: #f5f7f9;" v-loading="loading">
    <div style="border-bottom: 1px solid #ccc;font-weight: bolder;font-size: 24px;line-height: 50px;">浏览记录</div>
    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane v-for="tab in tabs" :label="tab.label" :name="tab.name" :key="tab.name"></el-tab-pane>
    </el-tabs>
    <HistoryList ref="hs" :postType="activeName" :activeLabel="activeLabel" :removeType="removeName"/>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import HistoryList from './historylist'
export default {
  // import引入的组件需要注入到对象中才能使用
  components: {
    HistoryList
  },
  data () {
    return {
      index: 0,
      activeName: '1',
      tabs: [
        { label: '商品', name: '1' },
        { label: '帖子', name: '2' },
        { label: '点赞', name: '0' }
      ],
      activeLabel: '商品',
      removeList: ['goods_id', 'content_id'],
      removeName: 'goods_id',
      loading: false,
      history: [],
      page: {
        total: 0,
        pageSize: 9,
        currentPage: 1
      }
    }
  },
  mounted () {
    // this.$refs[this.activeName].init(this.activeName)
  },
  methods: {
    handleClick (tab) {
      this.activeLabel = tab.label
      this.removeName = this.removeList[tab.index]
    },
    sizeChange (pageSize) { // 页数
      this.page.pageSize = pageSize
      // this.getPageFun()
    },
    currentChange (currentPage) { // 当前页
      this.page.currentPage = currentPage
      // this.getPageFun()
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 生命周期 - 创建完成（可以访问当前this实例）
  created () {
  },
  beforeCreate () {
  }, // 生命周期 - 创建之前
  beforeMount () {
  }, // 生命周期 - 挂载之前
  beforeUpdate () {
  }, // 生命周期 - 更新之前
  updated () {
  }, // 生命周期 - 更新之后
  beforeDestroy () {
  }, // 生命周期 - 销毁之前
  destroyed () {
  }, // 生命周期 - 销毁完成
  activated () {
  } // 如果页面有keep-alive缓存功能，这个函数会触发
}
</script>
<style scoped>
.follow-box{
  /*display: flex;*/
}
.history-item{
  display: flex;
  height: 40px;
  padding: 10px 10px;
  border-bottom: 1px solid #f0f0f5;
}
.history-item-img{
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 10px 0 0;
}
.history-item-nickname{
  height: 20px;
  line-height: 20px;
}
.history-item-time{
  height: 20px;
  line-height: 20px;
  color: #999;
}
.history-item-but-box{
  flex: 1;
  text-align: right;
  line-height: 40px;
}
</style>
