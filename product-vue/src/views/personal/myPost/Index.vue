<template>
  <div class=''>
    <el-tabs tab-position="left" style="min-height: 800px;" v-model="activeName" @tab-click="handleClick">
      <el-tab-pane v-for="tab in Tabs" @tab-click="handleClick" :label="tab.label" :name="tab.name" :key="tab.name">
        <MyPost :ref="tab.name"></MyPost>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import MyPost from '../MyPost.vue'

export default {
  name: '',
  // import引入的组件需要注入到对象中才能使用
  components: {MyPost},
  // 这里存放数据
  data () {
    return {
      activeName: '1',
      status: 0,
      Tabs: [
        { label: '全部', name: '1' },
        { label: '审核中', name: '2' },
        { label: '未通过', name: '3' },
        { label: '已发布', name: '4' },
        { label: '已下架', name: '5' },
        { label: '回收站', name: '6' }
      ]
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted () {
    this.$refs[1][0].init(null)
  },
  // 方法集合
  methods: {
    handleClick (tab, event) {
      if (tab.index !== '0') {
        this.status = tab.index
      } else {
        this.status = null
      }
      this.$refs[tab.name][0].init(this.status)
      console.log(tab.index + ',' + this.status)
    }
  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created () {
  },
  // 生命周期 - 创建之前
  beforeCreate () {
  },
  // 生命周期 - 挂载之前
  beforeMount () {
  },
  // 生命周期 - 更新之前
  beforeUpdate () {
  },
  // 生命周期 - 更新之后
  updated () {
  },
  // 生命周期 - 销毁之前
  beforeDestroy () {
  },
  // 生命周期 - 销毁完成
  destroyed () {
  },
  // 如果页面有keep-alive缓存功能，这个函数会触发
  activated () {
  }
}
</script>
<style scoped>
</style>
