<template>
  <div id="app">
    <router-view></router-view>
<!--    <Player/>-->
    <el-tooltip placement="top" content="回到顶部">
      <BackToTop :custom-style="myBackToTopStyle" :visibility-height="300" :back-position="0" transition-name="fade" />
    </el-tooltip>
  </div>
</template>

<script>
import Player from './components/zw-player/player.vue'
import BackToTop from './page/top/BackToTop.vue'

export default {
  name: 'App',
  components: {
    BackToTop,
    Player
  },
  data () {
    return {
      myBackToTopStyle: {
        right: '50px',
        bottom: '50px',
        width: '40px',
        height: '40px',
        'border-radius': '4px',
        'line-height': '45px', // 请保持与高度一致以垂直居中 Please keep consistent with height to center vertically
        background: '#e7eaf1'// 按钮的背景颜色 The background color of the button
      },
      loadingIndex: true
    }
  },
  watch: {},
  created () {
    let that = this // 加载延时loading
    setTimeout(function () {
      that.loadingIndex = false
    }, 500)
  },
  methods: {},
  computed: {}
}
</script>
<style>
html,body{
  margin: 0;
  padding: 0;
}
 #app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

/* 使用css禁止选中文本 */

.product-index .el-main{
  padding: 20px 0 20px 0!important;
}
.product-card .el-card__header{
  padding: 0!important;
}

.el-row>.el-col:not(:last-of-type) .el-card {
  margin-right: 10px!important;
}

</style>
