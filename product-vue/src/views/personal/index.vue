<template>
    <div style="font-size: 14px;">
      <div class="shell"  @mouseenter="handleMouseEnter" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
        <div class="image-container" :style="`transform: translate(${imageMove}px, -8px);`">
          <img src="../../../public/img/user-center-header.png" alt="Image">
        </div>
      </div>
<!--      <div style="height: 100px;background-color: #0c93c7;"></div>-->
      <div style="margin: auto 15%;background-color:#ffffff;display: flex;">
        <div style="width: 200px;border-right: 1px solid #e6e6e6;">
          <div style="font-size: 24px;text-align: center;line-height: 50px;color: #303133;">
            个人中心
          </div>
          <div>
            <el-menu :default-active="activeIndex" mode="vertical" class="el-menu-demo">
              <el-menu-item v-for="(item,index) in menus" :index="item.path" :key="item.path" @click="pathFun(item.path)">
                <div slot="title" style="margin-left: 30px;"><i :class="item.icon"></i>{{item.name}}</div>
              </el-menu-item>
            </el-menu>
          </div>
        </div>
        <router-view style="flex: 1;"></router-view>
      </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  data () {
    return {
      x: 0,
      imageMove: 36,
      activeIndex: '/index',
      menus: [
        {name: '个人资料', path: '/personal/user_info', icon: 'el-icon-user-solid'},
        {name: '我的收藏', path: '/personal/collect', icon: 'el-icon-star-on'},
        {name: '我的关注', path: '/personal/follow', icon: 'el-icon-star-on'},
        {name: '我的粉丝', path: '/personal/fans', icon: 'el-icon-star-on'},
        {name: '浏览记录', path: '/personal/history', icon: 'el-icon-star-on'}
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
