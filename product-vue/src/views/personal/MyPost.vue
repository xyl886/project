<template>
  <div style="font-size: 14px;padding: 10px;background-color: #ffffff;" v-loading="loading">
    <div>
      <el-row style="padding: 20px 0">
        <el-col :span="2">
          <el-dropdown @command="handleCommand" style="top: 5px;font-size:12px;line-height:20px;position: relative;" >
            <span class="el-dropdown-link">
              {{ command }}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="最多浏览" style="font-size: 12px">最多浏览</el-dropdown-item>
              <el-dropdown-item command="最新发布" style="font-size: 12px">最新发布</el-dropdown-item>
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
            size="mini"
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
          <div><el-button type="primary" size="mini">查询</el-button></div>
        </el-col>
        <el-col :span="12">
          <div @click="handleClear"><el-button size="mini">重置</el-button></div>
        </el-col>
        <el-col :span="2">
          <div><el-button type="primary" size="mini">批量操作</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="collect-box" v-for="(item,index) in posts" :key="index">
      <div class="collect-item" v-for="item2 in item" :key="item2.id">
        <el-card v-if="item2" :body-style="{ padding: '0px' }">
          <div class="image-box" style="cursor: pointer;" @click="detailFun(item2)">
            <div style="text-align: center;font-size: 25px;">
              <el-image v-if="item2.coverPath" :src="item2.coverPath" fit="cover" class="collect-box-img">
                <div slot="error" class="image-slot" style="padding:50% 0;top: 20%;position: relative;">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </div>
            <div class="video-info" style="font-size: 12px;">
              <p style="position: relative;left: 15px;">浏览:{{item2.browseNum}}</p>
              <p style="position: relative;left: 15px;">收藏:{{item2.collectNum}}</p>
              <p style="position: relative;left: 15px;">{{item2.userInfo.nickname}}</p>
              <p style="position: relative;left: 15px;">发布于:{{item2.createTime}}</p>
            </div>
          </div>
          <div class="collect-box-title" @click="detailFun(item2)">{{item2.title}}</div>
          <div style="padding: 5px">
            <span style="color: #999;">发布于:{{item2.createTime}}</span>
            <el-dropdown style="float: right;">
              <span><i class="el-icon-more" style="padding: 5px;"></i></span>
              <el-dropdown-menu >
                <el-dropdown-item @click.native="cancelCollect(item2)">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-card>
        <el-card v-else>
          <el-image class="collect-box-img">
            <div slot="error" class="image-slot" style="padding:50%;"></div>
          </el-image>
        </el-card>
        <div class="checkbox" v-if="isSelect" @click="Checkbox(item2.id)">
                <span style="display: inline-block;position: absolute;right: 5%;top: 5%;font-size: 25px;">
                   <i class="iconfont icon-xuanzhong" style="font-size: 25px"
                      :style="selected.includes(item2.id)?'color: #00a6ff':''"></i>
                </span>
        </div>
      </div>
    </div>

    <div v-if="posts.length === 0">
      <el-empty description="空空如也！">
        <el-button v-if="page.status===0" type="primary" @click.native="publish">发布帖子</el-button>
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
import {MessageBox} from 'element-ui'
import {setStore} from '../../utils/store'
import {addCollect} from '../../api/collect'
import {formatDate} from '../../utils/date'

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
      myLike: '',
      isSelect: false,
      isAllSelected: false,
      selected: [],
      cancelList: '',
      value: '',
      InputFocused: false,
      searchText: '',
      posts: [],
      loading: false,
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        postsType: null,
        school: -1,
        status: null
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
    init (status) {
      this.page = {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        postsType: null,
        school: -1,
        pageTotal: 0,
        status: null
      }
      this.page.status = status
      this.getPageFun()
    },
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
      console.log(this.page.currentPage)
      this.getPageFun()
    },
    getPageFun () {
      this.loading = true
      console.log(this.page.currentPage)
      this.posts = []
      console.log(this.page)
      getPage(this.page).then(res => {
        if (res.code === 200) {
          console.log(res.data)
          let count = 0
          let arr = []
          for (let i = 0; i < res.data.length; i++) {
            console.log(res.data[i].updateTime)
            res.data[i].createTime = formatDate(res.data[i].createTime)
            res.data[i].updateTime = formatDate(res.data[i].updateTime)
            console.log(res.data[i].updateTime)
            count++
            if (count <= 5) {
              arr.push(res.data[i])
            }
            if (count === 5 || i === (res.data.length - 1)) {
              this.posts.push(arr)
              console.log(this.posts)
              arr = []
              count = 0
            }
          }
          console.log(this.posts)
          this.page.total = res.dataTotal
          // this.page.pageTotal = res.pageTotal
          this.loading = false
        } else {
          this.page.currentPage--
        }
      }, error => {
        console.log(error)
        this.page.currentPage--
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
    },
    cancelCollect (item) {
      addCollect(item.id, '1').then(res => {
        if (res.code === 200) {
          this.getPageFun()
          this.$message.success(res.msg)
        }
      })
    },
    detailFun (posts) {
      setStore({name: 'posts', content: posts})
      this.$router.push({path: '/detail'})
    },
    CheckboxShow () {
      // 显示批量删除
      this.selected = []
      if (this.isSelect) {
        this.isSelect = false
        return false
      }
      this.isSelect = true
    },
    selectAll () {
      if (this.isAllSelected) {
        this.selected = [] // 全选按钮已选中，将selected数组清空
        this.isAllSelected = false
        return false
      } else {
        this.selected = this.collects.flatMap(item => item.map(item2 => item2.id)) // 全选按钮未选中，将selected数组设置为所有选项的id
        this.isAllSelected = true
      }
    },
    Checkbox (index) {
      // 多个商品的选择
      let hash = this.selected.findIndex((item) => {
        return item === index
      })
      if (hash > -1) {
        this.selected.splice(hash, 1)
      } else {
        this.selected.push(index)
      }
    },
    One (index) {
      // 单个商品的取消
      this.selected.push(index)
      this.selected.forEach((item) => {
        this.cancelList.push(this.myLike[item].id)
      })
      this.cancelCollect()
    },
    Cancel () {
      // 弹窗是否确认
      MessageBox.confirm(
        '你将要取消收藏' + this.selected.length + '个商品',
        '确认提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          cancelButtonClass: 'cancel',
          confirmButtonClass: 'config',
          closeOnClickModal: false,
          closeOnPressEscape: false
        }
      ).then(() => {
        this.selected.forEach((item) => {
          this.cancelList.push(this.myLike[item].id)
        })
        this.cancelCollect()
        setTimeout(() => {
          this.$message(
            {
              type: 'success',
              message: '删除成功!'
            },
            1000
          )
        })
      }).catch(() => {})
    }
  },
  created () {
  }, // 生命周期 - 创建完成（可以访问当前this实例）
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
@import '../../../static/iconfont/iconfont.css';
.collect-box{
  display: flex;
}
.collect-item{
  width: calc(20% - 10px);
  margin: 5px;
  position: relative;
  transition: .2s;
}
.collect-item:hover{
  box-shadow: 1px 1px 10px rgba(0,0,0, 0.2);
  transform: translate(0px, 0px) scale(1.01) rotate(0deg);
}
.collect-box-title:hover{
  color: #00a6ff;
}
.collect-box-img{
  height: 200px;
  background-size: cover;
}
.collect-box-title{
  height: 40px;
  font-size: 12px;
  padding: 5px;
  overflow: hidden;
}
.video-info {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 65%;
  background-color: rgba(0, 0, 0, 0.15);
  color: white;
  display: none;
  /* 其他样式设置，如文本居中、字体大小等 */
}
.image-box:hover .video-info {
  display: block;
}
.el-icon-more:hover{
  color: #00a6ff;
}
.checkbox {
  position: absolute;
  top: 0;
//border: #00a6ff 1px solid;
  width: 100%;
  height: 100%;
  z-index: 1; /* 设置较高的层叠顺序 */
}
</style>
