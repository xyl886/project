<template>
    <div style="font-size: 14px;padding: 10px;background-color: #f5f7f9;" v-loading="loading">
<!--      <div style="border-bottom: 1px solid #ccc;font-weight: bolder;font-size: 24px;line-height: 50px;">我的收藏</div>-->
      <div>
        <el-row style="padding:20px 0">
<!--          <el-col :span="2">-->
<!--            <el-dropdown @command="handleCommand" style="top: 10px;position: relative;" >-->
<!--            <span class="el-dropdown-link">-->
<!--              {{ command }}<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
<!--            </span>-->
<!--              <el-dropdown-menu slot="dropdown" size="mini">-->
<!--                <el-dropdown-item command="最近收藏">最近收藏</el-dropdown-item>-->
<!--                <el-dropdown-item command="最多浏览">最多浏览</el-dropdown-item>-->
<!--                <el-dropdown-item command="最新发布">最新发布</el-dropdown-item>-->
<!--              </el-dropdown-menu>-->
<!--            </el-dropdown>-->
<!--          </el-col>-->
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
          <el-col :span="6">
            <div @click="handleClear"><el-button size="mini" @click="resetQuery">重置</el-button></div>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" v-show="isSelect" @click="CheckboxShow" size="mini">取消</el-button>
            <el-button type="primary" style="float: right" @click="CheckboxShow" v-show="!isSelect" size="mini">批量操作</el-button>
            <el-button
              v-show="isSelect"
              @click="Cancel"
              :disabled="this.selected.length === 0"
              class="cancel"
              size="mini">
              取消收藏
            </el-button>
          </el-col>
          <el-col :span="2" style="float: right">
            <span v-if="isSelect" @click="selectAll()">
  <i :class="selected.length === 0 ? 'iconfont icon-xuanzhong' : (selected.length === page.total ? 'iconfont icon-xuanzhong' : 'iconfont icon-weiquanxuan')"
     style="font-size: 25px"></i>
            </span>
          </el-col>
        </el-row>
      </div>
      <Waterfall :list="collects" :row-key="options.rowKey" :gutter="options.gutter"
                 :has-around-gutter="options.hasAroundGutter" :width="options.width" :breakpoints="options.breakpoints"
                 :img-selector="options.imgSelector" :background-color="options.backgroundColor"
                 :animation-effect="options.animationEffect" :animation-duration="options.animationDuration"
                 :animation-delay="options.animationDelay" :lazyload="options.lazyload" :load-props="options.loadProps">
        <template #item="{ item, url, index }">
          <div class="bg-gray-900 rounded-lg shadow-md overflow-hidden transition-all duration-300 ease-linear hover:shadow-lg hover:shadow-gray-600 group">
            <div class="overflow-hidden">
              <el-image :src="url" style="width: 200px;" class="cursor-pointer transition-all duration-300 ease-linear group-hover:scale-105"/>
              <!--            <LazyImg :url="url" class="cursor-pointer transition-all duration-300 ease-linear group-hover:scale-105" />-->
            </div>

            <div class="collect-box-title" @click="detailFun(item)">{{item.posts.title}}
              <el-tag type="primary" size="mini">{{item.posts.categoryName}}</el-tag>
            </div>
            <div  style="padding: 5px">
              <span style="color: #999;">收藏于:{{item.createTime}}</span>
              <el-dropdown style="float: right;" v-if="item.status!==5">
                <span><i class="el-icon-more" style="padding: 5px;"></i></span>
                <el-dropdown-menu >
                  <el-dropdown-item @click.native="cancelCollect(item.postsId)">取消收藏</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
        </template>
      </Waterfall>
<!--      <div class="collect-box" v-for="(item,index) in collects" :key="index">-->
<!--        <div class="collect-item" v-for="(item2,index2) in item" :key="item2.id">-->
<!--          <el-card v-if="item2.posts" :body-style="{ padding: '0px' }">-->
<!--            <div class="image-box" style="cursor: pointer;" @click="detailFun(item2.posts)">-->
<!--                <div style="text-align: center;font-size: 25px;overflow: hidden">-->
<!--                  <el-image v-if="item2.posts.coverPath" :src="item2.posts.coverPath" class="collect-box-img">-->
<!--                    <div slot="error" class="image-slot" style="padding:25%;">-->
<!--                      <i class="el-icon-picture-outline"></i>-->
<!--                    </div>-->
<!--                  </el-image>-->
<!--                </div>-->
<!--              <div class="video-info" style="font-size: 12px;">-->
<!--                <p style="position: relative;left: 15px;">浏览:{{item2.posts.browseNum}}</p>-->
<!--                <p style="position: relative;left: 15px;">收藏:{{item2.posts.collectNum}}</p>-->
<!--                <p style="position: relative;left: 15px;">{{item2.userInfo.nickname}}</p>-->
<!--                <p style="position: relative;left: 15px;">发布于:{{item2.posts.createTime}}</p>-->
<!--              </div>-->
<!--            </div>-->
<!--            <div class="collect-box-title"  @click="detailFun(item2.posts)">{{item2.posts ? item2.posts.title : '帖子不见了'}}</div>-->
<!--            <div style="padding: 5px"> <span style="color: #999;">收藏于:{{item2.updateTime}}</span>-->
<!--              <el-dropdown size="small" style="float: right;">-->
<!--                <span><i class="el-icon-more" style="padding: 5px;"></i></span>-->
<!--                <el-dropdown-menu >-->
<!--                  <el-dropdown-item @click.native="cancelCollect(item2.postsId)">取消收藏</el-dropdown-item>-->
<!--                </el-dropdown-menu>-->
<!--              </el-dropdown>-->
<!--            </div>-->
<!--          </el-card>-->
<!--          <el-card v-else :body-style="{ padding: '0px' }">-->
<!--            <el-image class="collect-box-img" src="../../public/images/error.png" style="height: 200px">-->
<!--            </el-image>-->
<!--            <div class="collect-box-title" style="cursor: default">帖子不见了!</div>-->
<!--            <div style="padding: 5px"> <span style="color: #999;">收藏于:{{item2.updateTime}}</span>-->
<!--              <el-dropdown size="small" style="float: right;">-->
<!--                <span><i class="el-icon-more" style="padding: 5px;"></i></span>-->
<!--                <el-dropdown-menu >-->
<!--                  <el-dropdown-item @click.native="cancelCollect(item2.postsId)">取消收藏</el-dropdown-item>-->
<!--                </el-dropdown-menu>-->
<!--              </el-dropdown>-->
<!--            </div>-->
<!--          </el-card>-->
<!--          <div class="checkbox" v-if="isSelect" @click="Checkbox(item2.postsId)">-->
<!--                <span style="display: inline-block;position: absolute;right: 5%;top: 5%;font-size: 25px;">-->
<!--                   <i class="iconfont icon-xuanzhong" style="font-size: 25px"-->
<!--                      :style="selected.includes(item2.postsId)?'color: #00a6ff':''"></i>-->
<!--                </span>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->

      <div v-if="collects.length > 0" style="text-align: center;margin: 50px 0 30px 0;">
        <el-pagination
          background
          :current-page.sync="page.currentPage"
          :page-sizes="[10, 20, 40, 60, 80]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
          @size-change="sizeChange"
          @current-change="currentChange"
        />
      </div>
      <div v-if="collects.length === 0">
        <el-empty description="暂无数据"></el-empty>
      </div>
    </div>
</template>

<script>
import {getPage, addCollect} from '@/api/collect'
import {setStore} from '@/utils/store'
import {MessageBox} from 'element-ui'
import {listAllCategory} from '../../api/posts'
import {deleteCollectBatch} from '../../api/collect'
import {Waterfall} from 'vue-waterfall-plugin'
import loading from '../../../static/assets/loading.png'
import error from '../../../static/assets/error.png'
export default {
  components: {Waterfall},
  data () {
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
        imgSelector: 'posts.coverPath',
        // 加载配置
        loadProps: {
          loading,
          error
        },
        // 是否懒加载
        lazyload: true
      },
      command: '最近收藏',
      value: '',
      myLike: '',
      isSelect: false,
      isAllSelected: false,
      selected: [],
      cancelList: '',
      InputFocused: false,
      categoryId: '',
      loading: false,
      collects: [],
      category: [],
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        categoryId: ''
      }
    }
  },
  mounted () {
    this.listCategory()
    this.getPageFun()
  },
  methods: {
    resetQuery () {
      this.page.categoryId = ''
      this.posts = []
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
      this.collects = []
      getPage(this.page).then(res => {
        this.loading = false
        if (res.code === 200) {
          this.collects.push(...res.data)
          // let count = 0
          // let arr = []
          // for (let i = 0; i < res.data.length; i++) {
          //   res.data[i].createTime = formatDate(res.data[i].createTime)
          //   res.data[i].updateTime = formatDate(res.data[i].updateTime)
          //   count++
          //   if (count <= 5) {
          //     arr.push(res.data[i])
          //   }
          //   if (count === 5 || i === (res.data.length - 1)) {
          //     this.collects.push(arr)
          //     arr = []
          //     count = 0
          //   }
          // }
          this.page.total = res.dataTotal
        }
      }, error => {
        this.$message.error(error)
        this.loading = false
      })
    },
    cancelCollect (item) {
      addCollect(item, '1').then(res => { // 每个单独地取消收藏
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
        this.selected = this.collects.flatMap(
          item => item.map(item2 => item2.postsId)) // 全选按钮未选中，将selected数组设置为所有选项的id
        console.log(this.selected)
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
      // const collectIds = []
      // for (const group of this.collects) {
      //   for (const collect of group) {
      //     collectIds.push(collect.postsId)
      //   }
      // }
      // this.selected = collectIds.join(',')
      console.log(this.selected)
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
        deleteCollectBatch(this.selected).then(res => {
          if (res.code === 200) {
            this.getPageFun()
            this.$message.success(res.msg)
          }
        })
        // this.cancelCollect(this.selected)
        // setTimeout(() => {
        //   this.$message({
        //     type: 'success',
        //     message: '取消成功!'
        //   })
        // }, 1000)
      }).catch(() => {
      })
    }
  }
}
</script>

<style scoped>
@import '../../../static/iconfont/iconfont.css';
/deep/
.waterfall-item{
  position: absolute;
}
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
    width: 100%;
    object-fit: cover;
    height: 200px;
  }
  .collect-box-title{
    height: 40px;
    font-size: 12px;
    padding: 5px;
    overflow: hidden;
    cursor: pointer;
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
