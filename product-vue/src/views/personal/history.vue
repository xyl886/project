<template>
  <div style="font-size: 14px;padding: 10px 0 0 20px;background-color: #ffffff;" v-loading="loading">
    <div style="border-bottom: 1px solid #ccc;font-weight: bolder;font-size: 24px;line-height: 50px;">浏览记录</div>
    <div class="history-box">
      <div class="history-item" v-for="(item,index) in history" :key="index">
        <div>
          <el-image :src="item.userInfo.avatar" class="history-item-img"></el-image>
        </div>
        <div>
          <div class="history-item-nickname">
            {{item.userInfo.nickname}}
          </div>
          <div class="history-item-time">
            {{item.createTime}} {{item.followStatus === 2?' Ta关注了你':(item.followStatus === 1?'你关注了Ta':'你们已互关')}}
          </div>
        </div>
        <div class="history-item-but-box">
          <el-button round :type="item.followStatus !== 2?'':'primary'" @click="addHistoryFun(item.userInfo,item.followStatus !== 2?'1':'0')">
            {{item.followStatus !== 2?'取消关注':'关注'}}
          </el-button>
        </div>
      </div>
    </div>

    <div v-if="history.length > 0" style="text-align: center;margin: 50px 0 30px 0;">
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

    <div v-if="history.length === 0">
      <el-empty description="暂无数据"></el-empty>
    </div>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from ‘《组件路径》‘;
export default {
  // import引入的组件需要注入到对象中才能使用
  components: {},
  data () {
    return {
      loading: false,
      history: [],
      page: {
        total: 0,
        pageSize: 9,
        currentPage: 1,
        followType: 1
      }
    }
  },
  mounted () {
    this.getPageFun()
  },
  methods: {
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
      // getPage(this.page).then(res => {
      //   this.loading = false
      //   if (res.code === 200) {
      //     this.follows = res.data
      //     this.page.total = res.dataTotal
      //   }
      // }, error => {
      //   this.loading = false
      // })
    },
    addHistoryFun (item, deleted) {
      // addHistory(item.id, deleted).then(res => {
      //   if (res.code === 200) {
      //     this.getPageFun()
      //     this.$message.success(res.msg)
      //   }
      // })
    }
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
.follow-box{
  /*display: flex;*/
}
.history-item{
  display: flex;
  height: 40px;
  padding: 10px 10px;
  border-bottom: 1px solid #f0f0f5;
}
.history-item-img{
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 10px 0 0;
}
.history-item-nickname{
  height: 20px;
  line-height: 20px;
}
.history-item-time{
  height: 20px;
  line-height: 20px;
  color: #999;
}
.history-item-but-box{
  flex: 1;
  text-align: right;
  line-height: 40px;
}
</style>
