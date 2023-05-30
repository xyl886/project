<template>
  <div id="app">
    <div class="my-home" v-if="loadingIndex" style="z-index: 99;">
        <div class="my-home__main">
            <img class="my-home__loading" src="../public/img/loading-spin.svg" alt="loading">
            <div class="my-home__title">
                努力加载中...
            </div>
            <div class="my-home__sub-title d">
                初次加载资源可能需要较多时间 请耐心等待
            </div>
        </div>
    </div>
    <router-view v-if="!loadingIndex"/>
    <Player/>
  </div>
</template>

<script>
import Player from './components/zw-player/player.vue'

export default {
  name: 'App',
  components: {
    Player
  },
  data () {
    return {
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
#app {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
/* #app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
} */

/* 使用css禁止选中文本 */
* {
  moz-user-select: -moz-none;
  -moz-user-select: none;
  -o-user-select: none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

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
