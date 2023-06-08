<template>
  <div class="right-side" :style="isShow">
    <div class="setting-container">
      <img
        title="反馈"
        style="width: 22px;height: 22px;margin-top: 3px"
        src="http://img.shiyit.com/fankui_1.png"
       alt=""/>
    </div>
    <i @click="backTop" class="iconfont right-side-icon icon-huidaodingbu" />
  </div>
</template>

<script>
export default {
  mounted () {
    // window.addEventListener('scroll', this.scrollToTop)
  },
  destroyed () {
    // window.removeEventListener('scroll', this.scrollToTop)
  },
  data: function () {
    return {
      isShow: '',
      isOut: 'right-side-out'
    }
  },
  methods: {
    // 回到顶部方法
    backTop () {
      var zd = document.querySelector('.right-side')
      zd.addEventListener('click', function () {
        var time = setInterval(function () {
          let top = pageYOffset
          let step = Math.ceil(top / 50)
          window.scroll(0, top - step)
          if (top == 0) {
            clearInterval(time)
          }
        })
      }, 30)
      window.addEventListener('scroll', function () {
        if (document.documentElement.scrollTop > 0) {
          zd.style.right = 5 + 'px'
        } else {
          zd.style.right = -55 + 'px'
        }
      })
    }
  }
}
</script>

<style scoped>
.right-side {
  z-index: 4;
  position: fixed;
  right: 10px;
  bottom: 85px;
  transition: all 0.5s;
}
.right-side-config-hide {
  transform: translate(35px, 0);
}
.right-side-out {
  animation: rightsideOut 0.3s;
}
.right-side-in {
  transform: translate(0, 0) !important;
  animation: rightsideIn 0.3s;
}
.right-side-icon,
.setting-container {
  display: block;
  margin-bottom: 2px;
  width: 30px;
  height: 30px;
  background-color: #49b1f5;
  color: #fff;
  text-align: center;
  font-size: 16px;
  line-height: 30px;
  cursor: pointer;
}
.right-side-icon:hover,
.setting-container:hover {
  background-color: #ff7242;
}
.setting-container i {
  display: block;
  animation: turn-around 2s linear infinite;
}
@keyframes turn-around {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
}
@keyframes right-sideOut {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(30px, 0);
  }
}
@keyframes right-sideIn {
  0% {
    transform: translate(30px, 0);
  }
  100% {
    transform: translate(0, 0);
  }
}
</style>
