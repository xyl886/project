<template>
    <div style="font-size: 14px;">
<!--      <div style="top:5px;position: relative;overflow-x: hidden;" class="shell" @mouseenter="handleMouseEnter" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">-->
<!--        <div class="image-container" :style="`transform: translate(${imageMove}px, -8px);`">-->
<!--          <img src="../../../public/img/userheader.png" alt="Image">-->
<!--        </div>-->
<!--      </div>-->
      <Header></Header>
      <div style="margin: auto 10%;background-color:#ffffff;">
        <div style="border: 1px solid #e6e6e6;">
            <el-menu :default-active="activeIndex" mode="horizontal" class="el-menu-demo">
              <el-menu-item v-for="(item,index) in menus" :index="item.path" :key="item.path" @click="pathFun(item.path)">
                <div slot="title" style="margin-left: 15px;margin-right: 15px">
                  <span slot="label"><i :class="item.icon"></i></span>
<!--                  <i :class="emijoIconClassNameMap[key]"></i> -->
                  {{item.name}}</div>
              </el-menu-item>
            </el-menu>
            <router-view style="flex: 1;"></router-view>
        </div>
      </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Header from './Header.vue'

export default {
  components: {Header},
  data () {
    return {
      x: 0,
      imageMove: 36,
      activeIndex: '/index',
      menus: [
        {name: '我的帖子', path: '/personal/my_post', icon: 'iconfont icon-wofadetiezi'},
        {name: '个人资料', path: '/personal/user_info', icon: 'iconfont icon-bianjigerenziliao'},
        {name: '我的收藏', path: '/personal/collect', icon: 'iconfont icon-shoucang'},
        {name: '我的关注', path: '/personal/follow', icon: 'iconfont icon-chakantieziguanzhu'},
        {name: '我的粉丝', path: '/personal/fans', icon: 'iconfont icon-wodefensi'},
        {name: '浏览记录', path: '/personal/history', icon: 'iconfont icon-lishijilu_huaban'}
      ]
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  mounted () {
    let that = this
    setInterval(function () { // 定位当前菜单
      that.activeIndex = that.$router.currentRoute.path
    }, 300)
  },
  methods: {
    handleMouseEnter (event) {
      this.x = event.clientX
    },
    handleMouseMove (event) {
      const _x = event.clientX
      const disx = _x - this.x
      this.imageMove = 36 - disx / -20
    },
    handleMouseLeave () {
      this.imageMove = 36
    },
    pathFun (path) {
      this.$router.push({path: path})
    },
    homePage () {
      this.$router.push({path: '/index'})
    },
    toLogin () {
      this.$router.push({path: '/login'})
    },
    personalPage () {
      this.$router.push({path: '/personal'})
    },
    logoutFun () { // 退出登录
      this.$confirm('确定退出登录?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$store.dispatch('logout').then((res) => {
            if (res.code === 200) {
              this.$notify({
                title: '退出成功',
                type: 'success'
              })
            }
          })
        })
    }
  }
}
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
}

body {
  display: flex;
  justify-content: center;
  overflow: hidden;
}

.shell {
  width: 100%;
  height: 162px;
  display: flex;
  justify-content: center;
}

.image-container {
  height: 100%;
}

.image-container img {
  height: 100%;
}
</style>
