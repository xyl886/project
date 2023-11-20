<template>
  <footer>
    <div class="container">
      <div class="row">
        <div class="col-md-4">
          <h3>关于我们</h3>
          <p>校园墙是一个社交平台，旨在为学生提供一个分享和交流的空间。</p>
        </div>
      </div>
    </div>
  </footer>
</template>
<!--<template>-->
<!--  <div class="home-wrap">-->
<!--    <div class="btn" @click="which = i" v-for="(item, i) in 3" :key="i">-->
<!--      {{ "样式" + (i + 1) }}-->
<!--    </div>-->
<!--    <template v-for="(item, i) in 3">-->
<!--      <Index-->
<!--          :key="i"-->
<!--          :type="i + 1"-->
<!--          @handleClose="handleClose"-->
<!--          @handleRefresh="handleRefresh"-->
<!--          @handleSucc="handleSucc"-->
<!--          @handleFail="handleFail"-->
<!--          v-if="which === i"-->
<!--          ref="captcha"-->
<!--      ></Index>-->
<!--    </template>-->
<!--  </div>-->
<!--</template>-->
<script>
import Index from '../../components/vuerify/Index.vue'

export default {
  components: {
    Index
  },
  data () {
    return {
      path: '',
      which: -1
    }
  },
  mounted () {
    this.path = this.$router.currentRoute.path
  },
  methods: {
    handleRefresh (args) {
      console.log('刷新', args)
    },
    handleSucc (args) {
      console.log('成功', args)
    },
    handleFail (args) {
      console.log('失败', args)
    },
    handleClose () {
      this.which = -1
      // 重置验证码组件

      this.$nextTick(() => {
        this.$refs.captcha.forEach((captcha) => {
          captcha.handleSort()
        })
      })
    },
    homePage () {
      this.$router.push({path: '/index'})
    }
  }
}
</script>
<style lang="scss" scoped>
.home-wrap {
  padding-top: 25vh;
  .btn {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100px;
    height: 36px;
    margin: 30px auto;
    font-size: 14px;
    color: #fff;
    cursor: pointer;
    border-radius: 50px;
    background: #3c76f4;
    -webkit-tap-highlight-color: transparent;
  }
}
</style>
<style scoped>
footer {
  color: #666;
  padding: 10px 0;
  text-align: center;
}

footer h3 {
  color: #555;
  margin-top: 0;
}

footer p {
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 0;
}

.social-icons {
  list-style: none;
  padding: 0;
  margin: 20px 0 0;
}

.social-icons li {
  display: inline-block;
  margin-right: 10px;
}

.social-icons li:last-child {
  margin-right: 0;
}

.social-icons a {
  display: block;
  width: 40px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  color: #fff;
  background-color: #333;
  border-radius: 50%;
}

.fa {
  font-size: 20px;
}

@media (max-width: 767px) {
  footer {
    padding: 40px 0;
  }

  .social-icons {
    margin: 10px 0 0;
    text-align: center;
  }

  .social-icons li {
    display: inline-block;
    margin: 0 5px;
  }
}
</style>
