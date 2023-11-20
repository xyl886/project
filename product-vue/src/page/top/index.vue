<template>
  <div style="font-size: 14px;display: flex;height: 60px;padding: 0 30px;background-color: #ffffff;">
    <div style="display: flex; cursor: pointer" @click="pathFun('/index')">
      <div>
        <img src="../../../public/img/logo3.png" style="width: 50px;height: 50px;margin: 5px 0;" alt="">
      </div>
      <div style="font-size: 28px;font-weight: 700;line-height: 60px;margin-left: 10px;">
      校园墙
      </div>
    </div>
    <div class="menu" style="margin:0 100px;flex: 1;">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
        <el-menu-item v-for="(item,index) in menus" :index="item.path" :key="item.path" @click="pathFun(item.path)">{{item.name}}</el-menu-item>
      </el-menu>
<!--      <el-dropdown trigger="click" class="dropdown" style="display: none">-->
<!--        <span class="el-dropdown-link"><i class="el-icon-arrow-down el-icon&#45;&#45;right"></i></span>-->
<!--        <el-dropdown-menu :default-active="activeIndex" slot="dropdown">-->
<!--          <el-dropdown-item v-for="(item, index) in menus" :index="item.path" :key="item.path" @click="pathFun(item.path)">{{ item.name }}</el-dropdown-item>-->
<!--        </el-dropdown-menu>-->
<!--      </el-dropdown>-->
    </div>
  <div style="text-align: right">
      <div v-if="!userInfo || !userInfo.id" style="line-height: 60px;">
        <el-button type="primary" plain @click="toLogin">登录</el-button>
      </div>
      <div v-if="userInfo && userInfo.id">
        <el-popover
          placement="bottom-end"
          width="300"
          trigger="hover">
          <el-image fit="cover" style="height: 80px;width: 300px" :src="userInfo?userInfo.avatar:''"></el-image>
          <el-row>
            <el-col :span="6">
              <el-image :src="userInfo?userInfo.avatar:''" style="width: 60px;height: 60px;border-radius:50%;margin-left: 5px;" ></el-image>
            </el-col>
            <el-col :span="18">
              <div style="padding: 15px 0;">{{userInfo.nickname}}
                <i>♂</i>
                <el-tag size="small" type="success">{{userInfo.role}}</el-tag></div>
              <div style="padding: 5px 0"><span>{{userInfo.followNum}} 关注 </span> <span>{{userInfo.fansNum}} 粉丝 </span><span>11 获赞 </span></div>
              <div style="padding: 15px 0">{{userInfo.remark}}</div>
            </el-col>
          </el-row>
          <el-image :src="userInfo.avatar" slot="reference" @click="pathFun('/personal')" style="cursor:pointer;width: 40px;height: 40px;margin: 10px 10px;border-radius: 50%;"></el-image>
        </el-popover>
        <el-dropdown style="top: -25px">
          <div style="display: flex;">
            <span style="line-height: 35px;">
                {{userInfo.nickname}}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="publish">发布帖子</el-dropdown-item>
            <el-dropdown-item @click.native="chat(userInfo.id)">我的消息</el-dropdown-item>
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
import * as path from 'path'

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
        {name: '留言板', path: '/messageBoard'},
        {name: '热搜', path: '/hot'}
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
    handleCommand (command) {
      this.command = command
    },
    path () {
      return path
    },
    pathFun (path) {
      this.$router.push({path: path})
    },
    chat () {
      this.$router.push({path: '/chat'})
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
