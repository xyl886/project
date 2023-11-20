<template>
  <el-row class="container">
    <el-col :span="24" class="header">
      <el-col :span="10" :class="collapsed?'logo-collapse-width':'logo-width'" class="logo">
        <img v-if="collapsed" src="../../public/img/logo3.png" style="position: absolute;width: 50px;height: 50px;left: 0;" alt="">
        {{ collapsed?'':sysName }}
      </el-col>
      <el-col :span="10">
        <div class="tools" @click.prevent="collapse">
          <i class="fa fa-align-justify"/>
        </div>
      </el-col>
      <el-col :span="4" class="userinfo">
        <el-dropdown trigger="hover">
          <span class="el-dropdown-link userinfo-inner"><img :src="userInfo.avatar" alt="">{{ userInfo.nickname }}</span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="myMessage">我的消息</el-dropdown-item>
<!--            <el-dropdown-item>设置</el-dropdown-item>-->
            <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
    </el-col>
    <el-col :span="24" class="main">
      <aside :class="collapsed?'menu-collapsed':'menu-expanded'">
        <!--导航菜单-->
        <el-menu
          v-show="!collapsed"
          :default-active="$route.path"
          class="el-menu-vertical-demo"
          unique-opened
          router
          @open="handleOpen"
          @close="handleClose"
          @select="handleSelect">
          <el-menu-item @click="$router.push('/index/echarts')"><i class="el-icon-s-home"/>首页</el-menu-item>
          <template v-for="(item,index) in $router.options.routes" v-if="!item.hidden">
            <el-submenu v-if="!item.leaf" :index="index+''">
              <template slot="title"><i :class="item.icon"/>{{ item.name }}</template>
              <el-menu-item v-for="child in item.children" v-if="!child.hidden" :index="child.path" :key="child.path">{{ child.name }}</el-menu-item>
            </el-submenu>
            <el-menu-item v-if="item.leaf&&item.children.length>0" :index="item.children[0].path"><i :class="item.icon"/>{{ item.children[0].name }}</el-menu-item>
          </template>
        </el-menu>
        <!--导航菜单-折叠后-->
        <ul v-show="collapsed" ref="menuCollapsed" class="el-menu el-menu-vertical-demo collapsed">
          <li v-for="(item,index) in $router.options.routes" v-if="!item.hidden" class="el-submenu item">
            <template v-if="!item.leaf">
              <div class="el-submenu__title" style="padding-left: 20px;" @mouseover="showMenu(index,true)" @mouseout="showMenu(index,false)"><i :class="item.icon"/></div>
              <ul :class="'submenu-hook-'+index" class="el-menu submenu" @mouseover="showMenu(index,true)" @mouseout="showMenu(index,false)">
                <li v-for="child in item.children" v-if="!child.hidden" :key="child.path" :class="$route.path==child.path?'is-active':''" class="el-menu-item" style="padding-left: 40px;" @click="$router.push(child.path)">
                <i :class="child.icon"/>{{ child.name }}</li>
              </ul>
            </template>
            <template v-else>
              <li class="el-submenu">
                <div :class="$route.path==item.children[0].path?'is-active':''" class="el-submenu__title el-menu-item" style="height: 56px;line-height: 56px;padding: 0 20px;" @click="$router.push(item.children[0].path)"><i :class="item.icon"/></div>
              </li>
            </template>
          </li>
        </ul>
      </aside>
      <section class="content-container">
        <div class="grid-content bg-purple-light">
          <el-col :span="24" class="breadcrumb-container">
            <!--            <strong class="title">{{ $route.name }}</strong>-->
            <!--            <el-breadcrumb separator="/" class="breadcrumb-inner">-->
            <!--              <el-breadcrumb-item v-for="item in $route.matched" :key="item.path">-->
            <!--                {{ item.name }}-->
            <!--              </el-breadcrumb-item>-->
            <!--            </el-breadcrumb>-->
          </el-col>
          <el-col :span="24" class="content-wrapper">
            <transition name="fade" mode="out-in">
              <router-view/>
            </transition>
          </el-col>
        </div>
      </section>
    </el-col>
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  data() {
    return {
      sysName: '校园墙后台管理',
      collapsed: false,
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  mounted() {
  },
  methods: {
    myMessage() {
      this.$router.push({ path: '/index/chat' })
    },
    onSubmit() {
      console.log('submit!')
    },
    handleOpen() {
      // console.log('handleOpen');
    },
    handleClose() {
      // console.log('handleClose');
    },
    handleSelect: function(a, b) {
    },
    // 退出登录
    logout() {
      this.$confirm('确定退出登录?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('logout').then((res) => {
          if (res.code === 200) {
            this.$notify({
              title: '退出成功',
              type: 'success'
            })
            this.$router.push({ path: '/login' })
          }
        })
      })
    },
    // 折叠导航栏
    collapse: function() {
      this.collapsed = !this.collapsed
    },
    showMenu(i, status) {
      this.$refs.menuCollapsed
        .getElementsByClassName('submenu-hook-' + i)[0]
        .style.display = status ? 'block' : 'none'
    }
  }
}

</script>

<style lang="scss" scoped>
	.container {
		position: absolute;
		top: 0;
		bottom: 0;
		width: 100%;
		.header {
			height: 60px;
			line-height: 60px;
			background: rgba(0, 0, 0, 0.11);
			color:#fff;
			.userinfo {
				text-align: right;
				padding-right: 35px;
				float: right;
				.userinfo-inner {
					cursor: pointer;
					color:#fff;
					img {
						width: 40px;
						height: 40px;
						border-radius: 20px;
						margin: 10px 0 10px 10px;
						float: right;
					}
				}
			}
			.logo {
				//width:230px;
				height:60px;
				font-size: 22px;
				padding-left:20px;
				padding-right:20px;
				border-color: rgb(0, 166, 255);
				border-right-width: 1px;
				border-right-style: solid;
				img {
					width: 40px;
					float: left;
					margin:5px;
				}
				.txt {
					color:#fff;
				}
			}
			.logo-width{
				width:230px;
			}
			.logo-collapse-width{
				width:60px
			}
			.tools{
				padding: 0 23px;
				width:14px;
				height: 60px;
				line-height: 60px;
				cursor: pointer;
			}
		}
		.main {
			display: flex;
			// background: #324057;
			position: absolute;
			top: 60px;
			bottom: 0;
			overflow: hidden;
			aside {
				flex:0 0 230px;
				width: 230px;
				// position: absolute;
				// top: 0px;
				// bottom: 0px;
				.el-menu{
					height: 100%;
				}
				.collapsed{
					width:60px;
					.item{
						position: relative;
					}
					.submenu{
						position:absolute;
						top:0;
						left:60px;
						z-index:99999;
						height:auto;
						display:none;
					}

				}
			}
			.menu-collapsed{
				flex:0 0 60px;
				width: 60px;
			}
			.menu-expanded{
				flex:0 0 230px;
				width: 230px;
      .el-menu{
        width: 230px!important;
      }
			}
			.content-container {
				// background: #f1f2f7;
				flex:1;
				// position: absolute;
				// right: 0px;
				// top: 0px;
				// bottom: 0px;
				// left: 230px;
				overflow-y: scroll;
				padding: 10px 0 10px 10px;
				.breadcrumb-container {
					//margin-bottom: 15px;
					.title {
						width: 200px;
						float: left;
						color: #475669;
					}
					.breadcrumb-inner {
						float: right;
					}
				}
				.content-wrapper {
					background-color: #fff;
					box-sizing: border-box;
				}
			}
		}
	}
</style>
