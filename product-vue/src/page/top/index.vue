<template>
  <div style="font-size: 14px;display: flex;height: 60px;padding: 0 30px;background-color: #ffffff;">
    <div style="display: flex;">
      <div>
        <img src="../../../public/img/logo.png" style="width: 50px;height: 50px;margin: 5px 0;" alt="">
      </div>
      <div style="font-size: 28px;font-weight: 700;line-height: 60px;margin-left: 10px;">
      校园墙
      </div>
    </div>
    <div style="margin-left: 100px;flex: 1;">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
        <el-menu-item v-for="(item,index) in menus" :index="item.path" :key="item.path" @click="pathFun(item.path)">{{item.name}}</el-menu-item>
      </el-menu>
    </div>
  <div style="width: 150px;text-align: right">
      <div v-if="!userInfo || !userInfo.id" style="line-height: 60px;">
        <el-button type="primary" plain @click="toLogin">登录</el-button>
      </div>
      <div v-if="userInfo && userInfo.id">
        <el-dropdown>
          <div style="display: flex;">
            <el-image :src="userInfo.avatar" style="width: 40px;height: 40px;margin: 10px 10px;border-radius: 50%;"></el-image>
            <span style="line-height: 60px;">
                {{userInfo.nickname}}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="publish">发布帖子</el-dropdown-item>
            <el-dropdown-item @click.native="myMessage">我的消息</el-dropdown-item>
            <el-dropdown-item @click.native="updatePwd">修改密码</el-dropdown-item>
            <el-dropdown-item style="color:red;" @click.native="logoutFun">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <UpdatePwd ref="updatePwdDialog"></UpdatePwd>
    <login ref="loginDialog"></login>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import UpdatePwd from '../login/UpdatePwd'
import login from '../login/login.vue'

export default {
  data () {
    return {
      searchText: '',
      select: '1',
      activeIndex: '/index',
      menus: [
        {name: '首页', path: '/index'},
        {name: '校园分享', path: '/share'},
        {name: '个人中心', path: '/personal'},
        {name: '留言板', path: '/boards'},
        {name: '关于我们', path: '/about-us'}
      ]
    }
  },
  components: {
    UpdatePwd,
    login
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
    }, 1000)
  },
  methods: {
    pathFun (path) {
      this.$router.push({path: path})
    },
    homePage () {
      this.$router.push({path: '/index'})
    },
    toLogin () {
      this.$refs.loginDialog.showDialog()
    },
    publish () {
      this.$router.push({path: '/publish'})
    },
    myMessage () {

    },
    updatePwd () {
      this.$refs.updatePwdDialog.showDialog()
    },
    getDataList () {

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
              this.$router.push({path: '/index'})
            }
          })
        })
    }
  }
}
</script>

<style scoped>
.el-select .el-input {
  width: 75px;
}
.input-with-select .el-input-group__prepend {
  background-color: #fff;
}
.input-with-select{
  width: 300px;
  margin-right: 210px;
  float: right;
  margin-top: auto;
}
</style>
