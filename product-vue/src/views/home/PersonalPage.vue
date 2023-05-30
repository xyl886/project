<template>
  <div class="homepage">
    <div
      class="handhomepage"
      v-if="showPage"
      :style="{
        backgroundImage: 'url(' + bgimg + ')',
        backgroundSize: 'cover',
      }"
    >
      <el-link
        icon="el-icon-edit"
        style="position: absolute; right: 20px; top: 15px; background: none"
        :underline="false"
        @click="dialogVisible = true"
        v-if="HomeID == masteruserinfo.id"
      >更换</el-link
      >
      <div class="information">
        <el-avatar
          shape="circle"
          :size="60"
          style="
            margin-left: 20px;
            float: left;
            border: 2px solid rgba(225, 225, 225, 0.01);
          "
          :src="userImg"
        ></el-avatar>
        <img
          :src="userSex"
          alt=""
          width="20px"
          height="20px"
          v-show="userSexIsNull"
        />
        <h2 class="nickname">{{ UserHomePage.usersName }}的主页</h2>
        <p>{{ UserHomePage.biography }}</p>
        <el-button
          type="primary"
          :icon="Focus_on_icon"
          size="small"
          @click="attention(HomeID)"
          :disabled="!showPage"
          class="follow_btn"
        >{{ Focus_on_but_val }}</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="privateChat"
          :disabled="HomeID == masteruserinfo.id"
          class="chat_btn"
        >私聊</el-button
        >
      </div>
    </div>
    <div class="nav" v-if="showPage">
      <div class="fans">
        <p>粉丝数</p>
        <p>{{ fansCount }}</p>
      </div>
      <div class="textint">
        <el-input
          placeholder="请输入内容"
          v-model="input"
          clearable
          @keyup.enter.native="search"
          maxlength="100"
          style="width: 300px; height: 40px"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="search"
          ></el-button>
        </el-input>
      </div>
    </div>
    <div class="Releasedgoods">
      <div class="title">
        <h2>{{ isTa }}发布的</h2>
        <el-tag
          type=""
          effect="plain"
          size="mini"
          v-if="!isNull"
          style="float: left; margin-top: 3px"
        >{{ goods.length }}
        </el-tag>
        <p>
          <el-link
            :underline="false"
            @click="isorderby"
            v-if="!isNull"
            style="margin-left: 10px"
          >{{ ordertext }}</el-link
          ><el-link
          :underline="false"
          v-if="!isNull && HomeID == masteruserinfo.id"
          @click="isshowcheckbox"
          style="margin-left: 10px"
        >{{ management }}</el-link
        >
          <el-checkbox
            v-if="showcheckbox"
            :indeterminate="isIndeterminate"
            v-model="checkAll"
            @change="handleCheckAllChange"
            style="margin-left: 10px"
          >
            全选
          </el-checkbox>
          <el-button
            type="text"
            icon="el-icon-delete"
            @click="UserDelGoods"
            v-if="showcheckbox && checkList.length > 0"
            style="margin-left: 10px"
          >下架选中的商品</el-button
          >
        </p>
      </div>
      <h3 v-if="isNull">TA还没有发布商品</h3>
      <ul v-if="goods.length != 0">
        <el-checkbox-group
          v-model="checkList"
          @change="handleCheckedCitiesChange"
          style="width: 120%"
        >
          <li v-for="(itmp, index) in goods" :key="index">
            <router-link
              target="_blank"
              :to="{
                name: 'details',
                params: {
                  goodsid: itmp.id,
                  goodsuserid: HomeID,
                },
              }"
            >
              <div>
                <el-checkbox
                  :label="itmp.id"
                  v-if="showcheckbox"
                  style="position: absolute; top: 10px; right: 0px"
                ><br
                /></el-checkbox>
              </div>
              <el-image
                :src="api + itmp.productImageditys"
                style="width: 100%; height: 65%; border-radius: 10px"
                fit="cover"
                lazy
              ></el-image>
              <p>{{ itmp.productTitleditys }}</p>
              <p><span style="font-size: 12px">￥</span>{{ itmp.price }}</p>
              <span> {{ itmp.numberOfViewsditys }}次浏览</span>
            </router-link>
          </li>
        </el-checkbox-group>
      </ul>
    </div>
    <el-dialog
      title="更换壁纸"
      :visible.sync="dialogVisible"
      width="30%"
      ref="tk"
      :close-on-click-modal="false"
    >
      <el-upload
        class="avatar-uploader"
        action=""
        :show-file-list="false"
        accept=".jpg,.png"
        :auto-upload="false"
        :on-change="changeimgurl"
      >
        <img v-if="imageUrl" :src="imageUrl" class="avatar" />
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="ChangeWallPaper">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { MessageBox } from 'element-ui'
// import { UserInfoAsync } from '@/api/Users/index'
// import { selectUserGoods } from '@/api/Commoditys/Commoditys'
// import { follow, isConcern } from '@/api/Fans/fans'
// import crypto from '@/crypto/index'
// import { modifyWallpaper, batchUndercarriage } from '@/api/HomePage/index'
export default {
  name: 'HomePage',
  data () {
    return {
      api: this.$axios.defaults.apiUrl, // 获取当前Api
      HomeID: this.$route.params.userid, // 当前主页的用户id
      UserHomePage: [], // 列表
      goods: [], // 商品列表
      userImg: '', // 用户头像
      userSex: '',
      // userSex: require('../assets/性别男.png'), // 性别
      userSexIsNull: true, // 判断用户是否填写性别
      input: '', // 搜索框内容
      masteruserinfo: this.$storage.getItem('userinfo'), // 当前登录用户id
      isTa: 'TA', // 判断是否进入的是自己的主页
      isNull: true, // 商品是否为空
      isOrderby: false, // 是否排序
      ordertext: '最多浏览', // 排序名称
      showcheckbox: false, // 显示多选框
      checkList: [], // 多选框列表
      checkAll: false, // 是否全选
      isIndeterminate: false, // 没有勾选完时显示的样式
      Focus_on_but_val: '关注', // 关注按钮默认显示内容
      Focus_on_icon: 'el-icon-plus', // 默认显示关注为加号
      isattention: false, // 是否关注
      onemeth_attention: 0, // 第一次执行方法
      isattention_sendbtn: true, // 定时器防止频繁点击
      fansCount: 0, // 粉丝数量
      showPage: false, // 显示背景
      dialogVisible: false, // 弹出框默认状态
      imageUrl: '', // 背景图路径
      bgimg: '', // 最终显示路径
      formData: null, // 新建数据
      management: '管理',
      fansfrom: {
        userId: null,
        fansId: null,
        id: null,
        attentionTime: null,
        userName: null,
        image: null,
        fansState: null
      },
      userGoodsIdList: []
    }
  },
  methods: {
    search () {
      if (this.input != null) {
        this.isNull = false
        this.homePageGoods()
      }
    },
    isorderby () {
      if (this.isOrderby) {
        this.isOrderby = false
        this.ordertext = '最新发布'
      } else {
        this.isOrderby = true
        this.ordertext = '最多浏览'
      }
      this.homePageGoods()
    },
    isshowcheckbox () {
      if (this.showcheckbox) {
        this.showcheckbox = false
        this.management = '管理'
        this.checkList = []
      } else {
        this.showcheckbox = true
        this.management = '取消管理'
      }
    },
    handleCheckedCitiesChange (value) {
      let checkedCount = value.length
      this.checkAll = checkedCount === this.goods.length
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.goods.length
    },
    handleCheckAllChange (val) {
      this.checkList = val ? this.userGoodsIdList : []
      this.isIndeterminate = false
    },
    UserDelGoods () {
      MessageBox.confirm('此操作将永久下架选中的商品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        // .then(() => {
        //   batchUndercarriage(this.checkList + '').then((res) => {
        //     if (res.status === 200) {
        //       this.$message({
        //         type: 'success',
        //         message: '下架成功!'
        //       })
        //       this.homePageGoods()
        //       this.checkList = []
        //       this.isIndeterminate = false
        //       this.isshowcheckbox()
        //     }
        //   })
        // })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消下架'
          })
        })
    },
    attention () {
      this.fansfrom.userId = this.HomeID
      this.fansfrom.fansId = this.masteruserinfo.id
      if (this.isattention_sendbtn) {
        this.isattention_sendbtn = false
        if (this.isattention === true && this.HomeID !== this.masteruserinfo.id) {
          this.fansfrom.fansState = this.isattention_sendbtn
          this.followOrUnfollow(
            'el-icon-plus',
            '关注',
            false,
            '你居然取消关注？？',
            'info'
          )
        } else if (
          this.isattention === false &&
          this.HomeID !== this.masteruserinfo.id
        ) {
          this.fansfrom.fansState = this.isattention_sendbtn
          this.followOrUnfollow(
            'el-icon-check',
            '已关注',
            true,
            '恭喜你,关注成功！',
            'success'
          )
        } else if (this.onemeth_attention === 2) {
          this.$message({
            message: '不能关注自己',
            type: 'error'
          })
        }
        setTimeout(() => {
          // 防止重复点击
          this.isattention_sendbtn = true
        }, 800)
      }
    },
    followOrUnfollow (icon, text, isattention, msg, msgtype) {
      this.fansfrom.fansState = isattention
      this.Focus_on_icon = icon // icon
      this.isattention = isattention
      this.Focus_on_but_val = text // text
      if (this.onemeth_attention === 2) {
        this.followAndUnfollow()
        this.$message({
          message: msg,
          type: msgtype
        })
      }
    },

    followAndUnfollow () {
      // follow(this.fansfrom).then((res) => {
      //   if (res.data.code === 200 && res.data.data) {
      //     this.fansCount++
      //   } else {
      //     this.fansCount--
      //   }
      // })
    },
    IsFans () {
      this.fansfrom.userId = this.HomeID
      this.fansfrom.fansId = this.masteruserinfo.id
      // isConcern(this.fansfrom).then((res) => {
      //   this.isattention = !res.data.data
      //   this.attention()
      //   this.onemeth_attention = 2
      // })
    },
    changeimgurl (file) {
      const isLt2M = file.size / 1024 / 1024 < 1
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 1MB!')
      } else {
        this.imageUrl = URL.createObjectURL(file.raw)
        this.formData = new FormData()
        this.formData.append('file', file.raw)
      }
    },
    ChangeWallPaper () {
      if (this.imageUrl === this.bgimg) {
        return false
      }
      this.formData.append('id', this.HomeID)
      this.formData.append('oldimg', this.UserHomePage.wallpaper)
      // modifyWallpaper(this.formData).then((res) => {
      //   if (res.status === 200) {
      //     this.$message({
      //       message: '修改成功！',
      //       type: 'success'
      //     })
      //     this.userInfo()
      //     this.dialogVisible = false
      //   }
      // })
    },
    userInfo () {
      // UserInfoAsync({ id: this.HomeID }).then((res) => {
      //   if (res.status === 200) {
      //     this.showPage = true
      //     this.UserHomePage = res.data.data
      //     this.userImg = this.api + this.UserHomePage.image
      //     this.fansCount = this.UserHomePage.fansCount
      //     this.bgimg = this.api + this.UserHomePage.wallpaper
      //     if (this.UserHomePage.wallpaper == null) {
      //       this.bgimg = this.api + '/images/BgImage/wallpaper.jpg'
      //     }
      //     this.imageUrl = this.bgimg
      //     if (this.UserHomePage.userSex === '女') {
      //       // this.userSex = require('../assets/性别女.png')
      //     } else if (this.UserHomePage.userSex == null) {
      //       this.userSexIsNull = false
      //     }
      //     if (this.HomeID === this.masteruserinfo.id) {
      //       this.isTa = '我'
      //     }
      //   }
      // })
    },
    homePageGoods () {
      // selectUserGoods({
      //   userId: this.HomeID,
      //   productTitleditys: this.input,
      //   orderType: this.isOrderby
      // }).then((res) => {
      //   if (res.status === 200) {
      //     this.goods = res.data
      //     this.goods.length !== 0 ? (this.isNull = false) : (this.isNull = true)
      //     this.goods.forEach((item) => {
      //       this.userGoodsIdList.push(item.id)
      //     })
      //   } else {
      //     this.isNull = true
      //   }
      // })
    },
    privateChat () {
      let routeUrl = this.$router.resolve({
        path: '/sendTextView'
      })
      let chatObject = {
        id: this.UserHomePage.id,
        username: this.UserHomePage.usersName,
        image: this.UserHomePage.image
      }
      sessionStorage.setItem(crypto.encrypt('chatObject'), crypto.encrypt(JSON.stringify(chatObject)))
      sessionStorage.setItem(crypto.encrypt('uid'), crypto.encrypt(this.masteruserinfo.id.toString()))
      window.open(routeUrl.href, '_blank')
    }
  },
  mounted () {
    // this.$refs.tk.$el.firstChild.style.height = 'auto'
    // this.$refs.tk.$el.firstChild.style.width = 'auto'
    // if (this.$storage.getItem('userinfo') === undefined) {
    //   this.$storage.clear()
    //   this.$router.push('/')
    // }
    this.userInfo()
    this.homePageGoods()
    this.IsFans()
  }
}
</script>

<style scoped>
@media screen and (min-width: 1500px) {
  ::v-deep .el-dialog {
    width: 30% !important;
    height: auto !important;
  }
}
@media screen and (min-width: 1200px) and (max-width: 1500px) {
  ::v-deep .el-dialog {
    width: 50% !important;
    height: auto !important;
  }
  .Releasedgoods ul li {
    width: 16% !important;
    height: 220px !important;
  }
}
@media screen and (min-width: 800px) and (max-width: 1200px) {
  ::v-deep .el-dialog {
    width: 70% !important;
    height: auto !important;
  }
  .Releasedgoods ul li {
    width: 22% !important;
    margin-left: 20px !important;
    height: 220px !important;
  }
}
@media screen and (min-width: 500px) and (max-width: 800px) {
  ::v-deep .el-dialog {
    width: 90% !important;
    height: auto !important;
  }
  .Releasedgoods ul li {
    width: 29% !important;
    margin-left: 20px !important;
    height: 220px !important;
  }
  .homepage {
    margin-top: 0px !important;
    border: none !important;
  }
}
@media screen and (max-width: 500px) {
  .homepage {
    margin-top: 0px !important;
    border: none !important;
  }
  .handhomepage {
    margin: 0 !important;
  }
  .el-input--suffix {
    width: 280px !important;
  }
  .information p {
    width: 120px !important;
  }
  .follow_btn {
    top: 40px !important;
    right: 10px !important;
  }
  .chat_btn {
    top: 40px !important;
    right: 100px !important;
  }
  ::v-deep .el-dialog {
    width: 95% !important;
    height: auto !important;
  }
  .Releasedgoods ul li {
    width: 43% !important;
    margin-left: 20px !important;
    height: 220px !important;
  }
  ::v-deep .el-link--inner {
    display: none !important;
  }
}

.homepage {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  margin-top: 10px;
}
.handhomepage {
  width: 100%;
  max-width: 1280px;
  height: 192px;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  background-position: center;
  background-repeat: no-repeat;
  box-sizing: border-box;
  padding-top: 115px;
  position: relative;
  margin: 0 auto;
  background: rgb(233, 233, 233);
}
.information {
  width: 100%;
  height: 80px;
  position: relative;
}
.nickname {
  width: auto;
  height: 22px;
  float: left;
  margin: 5px 0 0 15px;
  color: white;
  font-size: 18px;
}
.information p {
  width: 400px;
  height: 15px;
  font-size: 12px;
  color: white;
  position: absolute;
  left: 95px;
  top: 40%;
  opacity: 0.7;
}
.information img:nth-child(2) {
  margin: 8px 0 0 8px;
}
.Releasedgoods {
  width: 100%;
  max-width: 1280px;
  border-radius: 10px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  box-sizing: border-box;
  margin: 20px auto;
}
.Releasedgoods ul {
  list-style: none;
  width: 100%;
  max-width: 1279.4px;
  height: auto !important;
  max-height: 400px;
  min-height: 100px;
  box-sizing: border-box;
  overflow-y: scroll;
  display: flex;
  flex-wrap: wrap;
  /*justify-content: row;*/
  padding-top: 10px;
}
.Releasedgoods ul li {
  margin-bottom: 20px;
  width: 16.4%;
  height: 260px;
  border-radius: 10px;
  margin-left: 43px;
  float: left;
  transition: 0.1s all linear;
  box-sizing: border-box;
  border: 1px solid rgba(0, 0, 0, 0.05);
  position: relative;
  box-shadow: 0 6px 10px 0 rgb(95 101 105 / 15%);
}
.Releasedgoods ul li a {
  text-decoration: none;
  color: black;
}
.Releasedgoods ul li:hover {
  box-shadow: 0 5px 20px 0 rgba(0, 0, 0, 0.2);
  transition: 0.1s all linear;
  transform: translate(0, -5px);
}
.nav {
  width: 100%;
  max-width: 1280px;
  height: 70px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
  box-sizing: border-box;
  margin: 0 auto;
  position: relative;
}
.nav .fans {
  position: absolute;
  right: 3%;
}
.nav .fans p {
  margin-top: 7px;
  width: 50px;
  text-align: center;
  color: darkgray;
  font-size: 13px;
  user-select: none;
}
.textint {
  width: 100%;
  max-width: 450px;
  height: 50px;
  box-sizing: border-box;
  padding-top: 7px;
  padding-left: 10px;
}
.Releasedgoods ul li p {
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-top: 7px;
  padding: 0 3px;
  box-sizing: border-box;
  height: 20px;
}
.Releasedgoods ul li p:nth-child(4) {
  font-size: 14px;
  font-weight: bold;
  color: red;
}
.Releasedgoods ul li a > span {
  display: inline-block;
  width: 100%;
  height: 20px;
  font-size: 10px;
  color: darkgray;
  padding: 0 7px;
  box-sizing: border-box;
  text-align: right;
}
.Releasedgoods h2 {
  font-size: 18px;
  margin-left: 10px;
  width: 90px;
  float: left;
}
.Releasedgoods h3 {
  text-align: center;
  color: darkgray;
  opacity: 0.3;
  line-height: 270px;
  user-select: none;
}
.title {
  padding-top: 10px;
  width: 100%;
  max-width: 1280px;
  height: 40px;
  box-sizing: border-box;
  padding-left: 5px;
}
.Releasedgoods ul::-webkit-scrollbar {
  /* 滚动条整体样式 */
  width: 1px; /* 高宽分别对应横竖滚动条的尺寸 */
}

.Releasedgoods ul::-webkit-scrollbar-thumb {
  /* 滚动条内滑块的样式 */
  border-radius: 5px;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.11);
  background: #409eff;
}
.Releasedgoods ul::-webkit-scrollbar-track {
  /* 滚动条里面轨道的样式 */
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
  border-radius: 0;
  background: #ffffff;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  margin: 0 auto;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 100%;
  height: auto;
  display: block;
  object-fit: scale-down;
  margin: 0 auto;
}
.follow_btn {
  position: absolute;
  right: 41px;
  top: 30px;
}
.chat_btn {
  position: absolute;
  right: 130px;
  top: 30px;
}
.title .el-button {
  padding: 0 !important;
}
</style>
