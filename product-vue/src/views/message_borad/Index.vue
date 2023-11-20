<template>
  <div>
    <!-- banner -->
    <div class="message-banner" :style="cover">
      <!-- 弹幕输入框 -->
      <div class="message-container">
        <h1 class="message-title">留言板</h1>
        <div class="message-input-wrapper">
          <el-input class="input" v-model="content" placeholder="说点什么吧" @keyup.enter.native="addToList"
                    @focus="show = true"></el-input>
          <el-button style="opacity: .6;" class="ml-3" round @click="addToList" v-show="show">发送</el-button>
        </div>
      </div>
      <!-- 弹幕列表 -->
      <div class="barrage-container">
<!--        <vue-baberrage-->
<!--            :barrageList="barrageList"-->
<!--            :boxHeight="boxHeight"-->
<!--            :isShow="barrageIsShow"-->
<!--            :lanesCount="lanesCount"-->
<!--            :loop="barrageLoop"-->
<!--            :messageHeight="messageHeight"-->
<!--            :throttleGap="throttleGap">-->
<!--        </vue-baberrage>-->

                <vue-baberrage :barrageList="barrageList">
          <template v-slot:default="slotProps">
                        <span class="barrage-items">
                            <img :src="slotProps.item.avatar" width="30" height="30" style="border-radius:50%"  alt=""/>
                            <span class="ml-2">{{ slotProps.item.nickname }} :</span>
                            <span class="ml-2">{{ slotProps.item.content }}</span>
                        </span>
          </template>
        </vue-baberrage>
      </div>
    </div>
  </div>
</template>

<script>
import { listMessage, addMessage } from '@/api/message'
import {listAll} from "../../api/banner";
export default {
  mounted () {
    // document.title = '留言板'
    this.listMessage()
  },
  data () {
    return {
      show: false,
      img: process.env.VUE_APP_IMG_API,
      content: '',
      count: null,
      timer: null,
      barrageList: [],
      barrageIsShow: true, // 是否展示弹幕
      barrageLoop: true, // 是否循环播放
      boxHeight: 170, // 高度
      messageHeight: 25,		// 消息高度
      lanesCount: 4,			// 泳道数量
      throttleGap: 5000,		// 消息间隔
      user: ''
    }
  },
  methods: {
    addToList () {
      if (this.count) {
        this.$message.error('30秒后才能再次留言')
        return false
      }
      if (this.content.trim() === '') {
        this.$message.error('留言不能为空')
        return false
      }
      this.user = JSON.parse(localStorage.getItem('userInfo'))
      console.log(this.user)
      const message = {
        avatar: this.user ? this.user.avatar : '',
        nickname: this.user ? this.user.nickname : '游客',
        content: this.content,
        userId: this.user.id,
        time: Math.floor(Math.random() * (21 - 10) + 10)
      }
      console.log(message)
      this.content = ''
      addMessage(message).then(res => {
        if (res.code === 200) {
          this.barrageList.push(message)
          console.log(this.barrageList)
          this.$message({
            message: '留言成功',
            type: 'success'
          })
        }
      })
      const TIME_COUNT = 30
      if (!this.timer) {
        this.count = TIME_COUNT
        this.timer = setInterval(() => {
          if (this.count > 0 && this.count <= 30) {
            this.count--
          } else {
            clearInterval(this.timer)
            this.timer = null
          }
        }, 1000)
      }
    },
    listMessage () {
      listMessage().then(res => {
        console.log(res.data)
        this.barrageList = res.data
        console.log(this.barrageList)
      })
    }
  },
  computed: {
    cover () {
      var cover = require('../../../public/img/background.jpg')
      return 'background: url(' + cover + ') center center / cover no-repeat'
    }
  }
}
</script>

<style lang="scss" scoped>
/deep/ .el-input__inner {
  border-radius: 50px;
  opacity: .6;
}

.message-banner {
  position: absolute;
  top: 60px;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #49b1f5;
  animation: header-effect 1s;

  .message-container {
    position: absolute;
    width: 360px;
    top: 35%;
    left: 0;
    right: 0;
    text-align: center;
    z-index: 5;
    margin: 0 auto;
    color: #fff;

    .message-title {
      color: #eee;
      animation: title-scale 1s;
    }

    .message-input-wrapper {
      display: flex;
      justify-content: center;
      height: 2.5rem;
      margin-top: 2rem;

      .ml-3 {
        animation: left-in 1s ease;

        @keyframes left-in {
          0% {
            transform: translateY(-500%);
          }

          100% {
            transform: translateX(0);
          }
        }
      }
    }
  }

  .barrage-container {
    position: absolute;
    top: 50px;
    left: 0;
    right: 0;
    bottom: 0;
    height: calc(100% - 50px);
    width: 100%;

    .barrage-items {
      background: #000;
      border-radius: 100px;
      color: #fff;
      padding: 5px 10px 5px 5px;
      align-items: center;
      display: flex;
      margin-top: 10PX;
    }
  }

}
</style>
