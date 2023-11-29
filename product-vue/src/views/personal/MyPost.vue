<template>
  <div style="font-size: 14px;padding: 10px 0;background-color: #ffffff;" v-loading="loading">
    <div>
      <el-row style="padding: 20px 0">
<!--        <el-col :span="2">-->
<!--          <el-dropdown @command="handleCommand" style="top: 5px;font-size:12px;line-height:20px;position: relative;">-->
<!--            <span class="el-dropdown-link">-->
<!--              {{ command }}-->
<!--              <i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
<!--            </span>-->
<!--            <el-dropdown-menu slot="dropdown">-->
<!--              <el-dropdown-item command="最多浏览" style="font-size: 12px">最多浏览</el-dropdown-item>-->
<!--              <el-dropdown-item command="最新发布" style="font-size: 12px">最新发布</el-dropdown-item>-->
<!--            </el-dropdown-menu>-->
<!--          </el-dropdown>-->
<!--        </el-col>-->
        <el-col :span="4">
          <!--          <div class="search-box" :class="{ active: InputFocused }"> </div>-->
          <!--            <div class="el-icon-search" @click="handleSearch"></div>-->
          <!--            <input-->
          <!--              v-model="categoryId"-->
          <!--              placeholder="请输入搜索内容"-->
          <!--              @input="handleInput"-->
          <!--              @focus="InputFocused = true"-->
          <!--              @blur="InputFocused = false"-->
          <!--              class="search-box-input"-->
          <!--              type="text"/>-->
          <el-select
            size="mini"
            v-model="categoryId"
            placeholder="请输入搜索内容"
            @input="handleInput"
            @focus="InputFocused = true"
            @blur="InputFocused = false"
            class="search-box-input"
            filterable>
            <el-option
              v-for="item in category"
              :key="item.id"
              :label="item.categoryName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="2" style="margin-left: 15px">
          <div><el-button type="primary" size="mini" @click="handleSearch">查询</el-button></div>
        </el-col>
        <el-col :span="12">
          <div @click="handleClear"><el-button size="mini" @click="resetQuery">重置</el-button></div>
        </el-col>
<!--        <el-col :span="2">-->
<!--          <div><el-button type="primary" size="mini">批量操作</el-button>-->
<!--          </div>-->
<!--        </el-col>-->
      </el-row>
    </div>
<!--    <Waterfall :list="WaterfallPosts"  :gutter="options.gutter"-->
<!--               :has-around-gutter="options.hasAroundGutter" :width="options.width" :breakpoints="options.breakpoints"-->
<!--               :img-selector="options.imgSelector" :background-color="options.backgroundColor"-->
<!--               :animation-effect="options.animationEffect" :animation-duration="options.animationDuration"-->
<!--               :animation-delay="options.animationDelay" :lazyload="options.lazyload" :load-props="options.loadProps">-->
<!--      <template #item="{ item, url, index }">-->
<!--        <div class="card">-->
<!--&lt;!&ndash;          <LazyImg :url="url" :key="item.id" />&ndash;&gt;-->
<!--          <el-image :src="item.coverPath" />-->
<!--          <p class="text">{{item.title}}</p>-->
<!--        </div>-->
<!--      </template>-->
<!--    </Waterfall>-->
    <Waterfall :list="posts" :row-key="options.rowKey" :gutter="options.gutter"
               :has-around-gutter="options.hasAroundGutter" :width="options.width" :breakpoints="options.breakpoints"
               :img-selector="options.imgSelector" :background-color="options.backgroundColor"
               :animation-effect="options.animationEffect" :animation-duration="options.animationDuration"
               :animation-delay="options.animationDelay" :lazyload="options.lazyload" :load-props="options.loadProps">
      <template #item="{ item, url, index }">
        <div class="bg-gray-900 rounded-lg shadow-md overflow-hidden transition-all duration-300 ease-linear hover:shadow-lg hover:shadow-gray-600 group">
          <div class="overflow-hidden">
<!--            <el-image :src="item.coverPath" style="width: 200px;" class="cursor-pointer transition-all duration-300 ease-linear group-hover:scale-105"/>-->
            <LazyImg :url="url" class="cursor-pointer transition-all duration-300 ease-linear group-hover:scale-105" />
          </div>

          <div class="collect-box-title" @click="detailFun(item)">{{item.title}}
            <el-tag type="primary" size="mini">{{item.type}}</el-tag>
          </div>
          <div  style="padding: 5px">
            <span style="color: #999;">发布于:{{item.createTime}}</span>
            <el-dropdown style="float: right;" v-if="item.status!==5">
              <span><i class="el-icon-more" style="padding: 5px;"></i></span>
              <el-dropdown-menu >
                <el-dropdown-item @click.native="handleDelete(item)">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-dropdown style="float: right;" v-if="item.status===5">
              <span><i class="el-icon-more" style="padding: 5px;"></i></span>
              <el-dropdown-menu >
                <el-dropdown-item @click.native="Delete(item)">彻底删除</el-dropdown-item>
                <el-dropdown-item @click.native="reStore(item)">还原</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <!--            <span v-else>帖子已删除！</span>-->
          </div>
        </div>
      </template>
    </Waterfall>
<!--    <div class="collect-box" v-for="(item,index) in posts" :key="index">-->
<!--      <div class="collect-item" v-for="item2 in item" :key="item2.id">-->
<!--        <el-badge :type="item2.status===3?'success':(item2.status===1?'primary':'info')" :value="item2.postStatus" class="item">-->
<!--          <el-card v-if="item2" :body-style="{ padding: '0px' }">-->
<!--          <div class="image-box" style="cursor: pointer;" @click="detailFun(item2)">-->
<!--            <div style="text-align: center;font-size: 25px;">-->
<!--              <el-image v-if="item2.coverPath" :src="item2.coverPath" fit="cover" class="collect-box-img">-->
<!--&lt;!&ndash;                <div slot="error" class="image-slot" style="padding:50% 0;top: 20%;position: relative;">&ndash;&gt;-->
<!--&lt;!&ndash;                  <i class="el-icon-picture-outline"></i>&ndash;&gt;-->
<!--&lt;!&ndash;                </div>&ndash;&gt;-->
<!--              </el-image>-->
<!--            </div>-->
<!--            <div class="video-info" style="font-size: 12px;">-->
<!--              <p style="position: relative;left: 15px;">浏览:{{item2.browseNum}}</p>-->
<!--              <p style="position: relative;left: 15px;">收藏:{{item2.collectNum}}</p>-->
<!--              <p style="position: relative;left: 15px;">{{item2.userInfo.nickname}}</p>-->
<!--              <p style="position: relative;left: 15px;">发布于:{{item2.createTime}}</p>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div class="collect-box-title" @click="detailFun(item2)">{{item2.title}}-->
<!--          <el-tag type="primary" size="mini">{{item2.type}}</el-tag>-->
<!--          </div>-->
<!--          <div  style="padding: 5px">-->
<!--            <span style="color: #999;">发布于:{{item2.createTime}}</span>-->
<!--            <el-dropdown style="float: right;" v-if="item2.status!==5">-->
<!--              <span><i class="el-icon-more" style="padding: 5px;"></i></span>-->
<!--              <el-dropdown-menu >-->
<!--                <el-dropdown-item @click.native="handleDelete(item2)">删除</el-dropdown-item>-->
<!--              </el-dropdown-menu>-->
<!--            </el-dropdown>-->
<!--            <el-dropdown style="float: right;" v-if="item2.status===5">-->
<!--              <span><i class="el-icon-more" style="padding: 5px;"></i></span>-->
<!--              <el-dropdown-menu >-->
<!--                <el-dropdown-item @click.native="Delete(item2)">彻底删除</el-dropdown-item>-->
<!--                <el-dropdown-item @click.native="reStore(item2)">还原</el-dropdown-item>-->
<!--              </el-dropdown-menu>-->
<!--            </el-dropdown>-->
<!--&lt;!&ndash;            <span v-else>帖子已删除！</span>&ndash;&gt;-->
<!--          </div>-->
<!--        </el-card>-->
<!--        </el-badge>-->
<!--        <div class="checkbox" v-if="isSelect" @click="Checkbox(item2.id)">-->
<!--                <span style="display: inline-block;position: absolute;right: 5%;top: 5%;font-size: 25px;">-->
<!--                   <i class="iconfont icon-xuanzhong" style="font-size: 25px"-->
<!--                      :style="selected.includes(item2.id)?'color: #00a6ff':''"></i>-->
<!--                </span>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

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
import {del, delMyPost, getPage, listAllCategory, restore} from '../../api/posts'
import {MessageBox} from 'element-ui'
import {setStore} from '../../utils/store'
import { LazyImg, Waterfall } from 'vue-waterfall-plugin'
import 'vue-waterfall-plugin/dist/style.css'
import loading from '../../../static/assets/loading.png'
import error from '../../../static/assets/error.png'
export default {
  name: '',
  // import引入的组件需要注入到对象中才能使用
  components: {
    LazyImg,
    Waterfall
  },
  data () {
    // 这里存放数据
    return {
      options: {
        // 唯一key值
        rowKey: 'id',
        // 卡片之间的间隙
        gutter: 20,
        // 是否有周围的gutter
        hasAroundGutter: true,
        // 卡片在PC上的宽度
        width: 200,
        // 自定义行显示个数，主要用于对移动端的适配
        breakpoints: {
          1200: {
            // 当屏幕宽度小于等于1200
            rowPerView: 4
          },
          800: {
            // 当屏幕宽度小于等于800
            rowPerView: 3
          },
          500: {
            // 当屏幕宽度小于等于500
            rowPerView: 2
          }
        },
        // 动画效果
        animationEffect: 'animate__fadeInUp',
        // 动画时间
        animationDuration: 1000,
        // 动画延迟
        animationDelay: 300,
        // 背景色
        backgroundColor: '#ffffff',
        // imgSelector
        imgSelector: 'coverPath',
        // 加载配置
        loadProps: {
          loading,
          error
        },
        // 是否懒加载
        lazyload: true
      },
      command: '最新发布',
      category: [],
      myLike: '',
      isSelect: false,
      isAllSelected: false,
      selected: [],
      cancelList: '',
      value: '',
      InputFocused: false,
      categoryId: '',
      posts: [],
      loading: false,
      page: {
        categoryId: -1,
        total: 0,
        pageSize: 10,
        currentPage: 1,
        postsType: null,
        status: null,
        condition: ''
      }
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  mounted () {
  },
  methods: {
    resetQuery () {
      this.page.categoryId = -1
      this.page.condition = ''
      this.posts = []
      this.getPageFun()
    },
    init (status) {
      this.page = {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        postsType: null,
        categoryId: -1,
        pageTotal: 0,
        status: null,
        condition: ''
      }
      this.page.status = status
      console.log(status)
      this.getPageFun()
    },
    listCategory () {
      const data = {total: 0, pageSize: 10, currentPage: 1, categoryName: null}
      listAllCategory(data).then(res => {
        this.category = res.data
      })
    },
    handleCommand (command) {
      this.command = command
      if (this.command === '最新发布') {
        this.page.condition = 'create_time'
      } else if (this.command === '最多浏览') {
        this.page.condition = 'browse_num'
      }
    },
    handleInput () {
      // 输入框内容变化时触发
    },
    handleSearch () {
      // 点击搜索按钮触发
      if (this.categoryId) {
        // 执行搜索操作
        this.page.categoryId = this.categoryId
        this.getPageFun()
      }
    },
    handleClear () {
      // 点击清除按钮触发
      this.categoryId = ''
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
      this.posts = []
      getPage(this.page).then(res => {
        if (res.code === 200) {
          // let count = 0
          // let arr = []
          this.posts.push(...res.data)
          // for (let i = 0; i < res.data.length; i++) {
          //   res.data[i].createTime = formatDate(res.data[i].createTime)
          //   res.data[i].updateTime = formatDate(res.data[i].updateTime)
          //   count++
          //   if (count <= 5) {
          //     arr.push(res.data[i])
          //   }
          //   if (count === 5 || i === (res.data.length - 1)) {
          //     this.posts.push(arr)
          //     arr = []
          //     count = 0
          //   }
          // }
          this.page.total = res.dataTotal
          // this.page.pageTotal = res.pageTotal
          this.loading = false
        } else {
          this.page.currentPage--
        }
      }, error => {
        this.$message.error(error)
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
    handleDelete (item) {
      delMyPost(item.id, item.userId).then(res => {
        if (res.code === 200) {
          this.getPageFun()
          this.$message.success(res.msg)
        }
      })
    },
    Delete (item) {
      del(item.id, item.userId).then(res => {
        if (res.code === 200) {
          this.getPageFun()
          this.$message.success(res.msg)
        }
      })
    },
    reStore (item) {
      restore(item.id, item.userId).then(res => {
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
      this.handleDelete()
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
        this.handleDelete()
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

.collect-item>>>.el-badge__content.is-fixed{
  position: absolute;
  top:auto;
  bottom: 80px!important;
  right: 55px!important;
  border: none;
}
/deep/
.collect-item .el-badge__content.is-fixed{
  border: none;
  border-radius: 0;
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
  height: 160px;
  width: 200px;
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
