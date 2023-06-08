<template>
  <div style="font-size: 14px;padding: 10px 0 0 20px;background-color: #ffffff;" v-loading="loading">
    <!--    <div style="border-bottom: 1px solid #ccc;font-weight: bolder;font-size: 24px;line-height: 50px;">我的关注</div>-->
    <div>
      <el-row style="padding-bottom: 20px">
        <el-col :span="2">
          <el-dropdown @command="handleCommand" style="top: 10px;position: relative;" >
            <span class="el-dropdown-link">
              {{ command }}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="最多浏览">最多浏览</el-dropdown-item>
              <el-dropdown-item command="最新发布">最新发布</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>
        <el-col :span="4">
          <!--          <div class="search-box" :class="{ active: InputFocused }"> </div>-->
          <!--            <div class="el-icon-search" @click="handleSearch"></div>-->
          <!--            <input-->
          <!--              v-model="searchText"-->
          <!--              placeholder="请输入搜索内容"-->
          <!--              @input="handleInput"-->
          <!--              @focus="InputFocused = true"-->
          <!--              @blur="InputFocused = false"-->
          <!--              class="search-box-input"-->
          <!--              type="text"/>-->
          <el-select
            v-model="searchText"
            placeholder="请输入搜索内容"
            @input="handleInput"
            @focus="InputFocused = true"
            @blur="InputFocused = false"
            class="search-box-input"
            filterable>
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="2" style="margin-left: 15px">
          <div @click="handleClear"><el-button>重置</el-button></div>
        </el-col>
        <el-col :span="12">
          <div><el-button type="primary">查询</el-button>
          </div>
        </el-col>
        <el-col :span="2">
          <div><el-button type="primary">批量操作</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="follow-box">
      <div class="follow-item" v-for="(item,index) in posts" :key="index">
        <div>
          <el-image :src="item.userInfo.avatar" class="follow-item-img"></el-image>
        </div>
        <div>
          <div class="follow-item-nickname">
            {{item.userInfo.nickname}}
          </div>
          <div class="follow-item-time">
            {{item.createTime}} {{item.followStatus === 2?' Ta关注了你':(item.followStatus === 1?'你关注了Ta':'你们已互关')}}
          </div>
        </div>
        <div class="follow-item-but-box">
          <el-button round :type="item.followStatus !== 2?'':'primary'" @click="addFollowFun(item.userInfo,item.followStatus !== 2?'1':'0')">
            {{item.followStatus !== 2?'取消关注':'关注'}}
          </el-button>
        </div>
      </div>
    </div>
    <div v-if="posts.length === 0">
      <el-empty description="您还没有发布过帖子哦！" image-size="100" image="">
        <el-button type="primary" @click.native="publish">发布帖子</el-button>
      </el-empty>
    </div>
    <div style="text-align: center;margin: 50px 0 30px 0;">
      <el-pagination
        background
        :current-page.sync="page.currentPage"
        :page-sizes="[10, 30, 50, 100]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total"
        @size-change="sizeChange"
        @current-change="currentChange"
      />
    </div>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
import {addFollow} from '../../api/follow'
import {getPage} from '../../api/posts'

export default {
  name: '',
  // import引入的组件需要注入到对象中才能使用
  components: {},
  data () {
    // 这里存放数据
    return {
      command: '最近收藏',
      options: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }, {
        value: '选项4',
        label: '龙须面'
      }, {
        value: '选项5',
        label: '北京烤鸭'
      }],
      value: '',
      InputFocused: false,
      searchText: '',
      posts: '',
      loading: false,
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        followType: 1
      }
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  mounted () {
    this.getPageFun()
  },
  methods: {
    handleCommand (command) {
      this.command = command
    },
    handleInput () {
      // 输入框内容变化时触发
    },
    handleSearch () {
      // 点击搜索按钮触发
      if (this.searchText) {
        // 执行搜索操作
        console.log('搜索：', this.searchText)
      }
    },
    handleClear () {
      // 点击清除按钮触发
      this.searchText = ''
    },
    publish () {
      this.$router.push({path: '/publish'})
    },
    sizeChange (pageSize) { // 页数
      this.page.pageSize = pageSize
      this.getPageFun()
    },
    currentChange (currentPage) { // 当前页
      this.page.currentPage = currentPage
      this.getPageFun()
    },
    getPageFun () {
      this.loading = true
      this.follows = []
      getPage(this.page).then(res => {
        this.loading = false
        if (res.code === 200) {
          this.follows = res.data
          this.page.total = res.dataTotal
        }
      }, error => {
        this.$message.error(error)
        this.loading = false
      })
    },
    addFollowFun (item, deleted) {
      addFollow(item.id, deleted).then(res => {
        if (res.code === 200) {
          this.getPageFun()
          this.$message.success(res.msg)
        }
      })
    }
  },
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

<
style >
