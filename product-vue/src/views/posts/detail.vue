<template>
  <div class="body" v-loading="loading">
    <div class="box" style="">
      <div style="line-height: 50px;color: gray;">
        <span style="cursor: pointer;margin-left: 20px;" @click="backFun"><i class="el-icon-arrow-left"></i>返回</span>
      </div>

      <div style="display: flex;margin-top: 30px;height: 60px;">
        <div style="margin-left: 50px;">
          <el-image :src="posts.userInfo.avatar" style="width: 60px;height: 60px;border-radius: 50%;"></el-image>
        </div>
        <div style="margin-left: 10px;">
          <div style="height: 20px;">
            {{posts.userInfo.nickname}}
          </div>
          <div style="line-height: 40px;">
            <el-button v-if="!posts.collect" type="primary" size="mini" @click="addCollectFun('0')"><i class="el-icon-star-off"></i> 收藏</el-button>
            <el-button v-if="posts.collect" type="warning" size="mini" @click="addCollectFun('1')"><i class="el-icon-star-on"></i> 已收藏</el-button>
            <el-button v-if="!posts.follow && userInfo.id != posts.userId" type="primary" size="mini" @click="addFollowFun('0')"><i class="el-icon-star-off"></i> 关注</el-button>
            <el-button v-if="posts.follow && userInfo.id != posts.userId" type="warning" size="mini" @click="addFollowFun('1')"><i class="el-icon-star-on"></i> 已关注</el-button>
          </div>
        </div>
        <div style="flex: 1;color: #606266;font-size: 12px;opacity: 0.6;">
          <div style="text-align: right;margin-right: 20px;">{{posts.browseNum}}次浏览</div>
          <div style="text-align: right;margin-right: 20px;">
            来自：{{posts.schoolName}}
            |
            {{posts.createTime}}
            发布
          </div>
        </div>
      </div>

      <div style="display: flex;margin: 20px 20px 20px 60px;">
        <div style="width: 50%;">
          <div style="width: 350px;height: 225px;text-align: center;border-radius: 10px;display:table-cell;vertical-align: middle;">
            <el-image :src="bigImgPath" :preview-src-list="posts.imgPath?posts.imgPath.split(','):[]" fit="contain" class="big-img"></el-image>
          </div>
          <div style="margin-top: 10px;">
            <el-image v-for="(item) in posts.imgPath?posts.imgPath.split(','):[]" :key="item" fit="contain" :src="item" @click="selImg(item)" style="width: 80px;height: 80px;border-radius: 5px;margin: 0 10px 10px 0;border: 1px solid #e5e5e5;box-sizing: border-box;" :class="bigImgPath===item?'sel-img':''"></el-image>
          </div>
        </div>
        <div style="width: 50%;text-align: center;">
          <div style="font-size: 20px;font-weight: 700;">{{posts.title}}</div>
          <div class="my-content">
            <div class="textarea"><el-input type="textarea" resize="none" :readonly="true" :autosize="{ minRows: 2, maxRows: 22}" v-model="posts.content"></el-input></div>
            <div class="posts-item-price" style="color:#e9384d; display: block;margin-top: 20px;">
              ¥{{posts.price}}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {getDetail, browse} from '@/api/posts'
import {getStore} from '@/utils/store'
import {addCollect} from '@/api/collect'
import {addFollow} from '@/api/follow'
import { mapGetters } from 'vuex'

export default {
  data () {
    return {
      loading: false,
      id: '',
      posts: {
        userInfo: {
          avatar: ''
        },
        collect: false,
        follow: false
      },
      bigImgPath: ''
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  mounted () {
    let obj = getStore({name: 'posts'})
    if (obj) {
      this.id = obj.id
      this.bigImgPath = obj.coverPath
      this.getDetailFun()
      let that = this
      setTimeout(function () {
        that.browseFun()
      }, 2000)
    }
  },
  methods: {
    backFun () {
      // this.$router.push({path: '/'});
      // history.go(-1)
      history.back()
    },
    selImg (url) {
      this.bigImgPath = url
    },
    getDetailFun () {
      this.loading = true
      getDetail(this.id).then(res => {
        if (res.code === 200) {
          this.posts = res.data
          this.bigImgPath = res.data.coverPath
          console.log(res.data)
          console.log(res.data.coverPath)
        }
        this.loading = false
        // eslint-disable-next-line handle-callback-err
      }, error => {
        this.loading = false
      })
    },
    browseFun () {
      console.log(this.userInfo.id + ',' + this.id)
      browse(this.userInfo.id, this.id).then(res => {
        if (res.code === 200) {
          console.info('浏览次数更新成功')
        }
      })
    },
    addCollectFun (deleted) {
      addCollect(this.id, deleted).then(res => {
        if (res.code === 200) {
          if (deleted === '0') {
            this.posts.collect = true
          } else {
            this.posts.collect = false
          }
          this.$message.success(res.msg)
        }
      })
    },
    addFollowFun (deleted) {
      addFollow(this.posts.userInfo.id, deleted).then(res => {
        if (res.code === 200) {
          if (deleted === '0') {
            this.posts.follow = true
          } else {
            this.posts.follow = false
          }
          this.$message.success(res.msg)
        }
      })
    }
  }
}
</script>

<style>
  .body{
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
    background-size: 100% 100%;
    background-image:  url("../../../public/img/background-detail.jpg");
    background-repeat: no-repeat;
    position:relative;
  }
  .box{
    font-size: 14px;
    width: 65%;
    height: 85%;
    background-color: #ffffff;
    border-radius: 10px;
    position:absolute; /*参照物是父容器*/
    left:50%;
    transform:translateX(-50%) translateY(-50%); /*百分比的参照物是自身*/
    top:50%;   /* 参照物是父容器 */
    overflow: hidden;
  }
  .sel-img{
    border: 2px solid #ff6619!important;
  }
  .my-content .el-textarea__inner{
    border: none!important;
    font-size: 16px!important;
    font-weight: 700!important;
    color: #5b5b5b !important;
    font-family: "微软雅黑 Light",sans-serif;
  }
  .textarea{
  height: 250px;
  }
.posts-item-price{
  height: 100px;
  }
  .big-img{
    max-height: 250px;
  }
</style>
