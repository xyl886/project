<template>
  <div style="font-size: 14px;padding: 10px 0 0 20px;background-color: #f5f7f9;" v-loading="loading">
<!--    <div style="border-bottom: 1px solid #ccc;font-weight: bolder;font-size: 24px;line-height: 50px;">浏览记录</div>-->
    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="商品"  name="1" ></el-tab-pane>
      <el-tab-pane label="帖子" name="2" ></el-tab-pane>
      <el-tab-pane label="点赞" name="0" ></el-tab-pane>
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
      activeLabel: '商品',
      removeList: ['goods_id', 'content_id'],
      removeName: 'goods_id',
      loading: false
    }
  },
  mounted () {
  },
  methods: {
    handleClick (tab) {
      this.activeLabel = tab.label
      this.removeName = this.removeList[tab.index]
    },
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
</style>
