<template>
  <div class="captcha-wrap fixed frowc">
    <div class="type1" v-if="type === 1">
      <div class="title frow">请完成下列验证</div>
      <div class="btn-refresh pointer" @click="handleSort" v-if="!isOver"></div>
      <div class="btn-close pointer" @click="handleClose"></div>
      <div class="box" :style="{ backgroundImage: 'url(' + bg + ')' }">
        <div class="target" :style="type01"></div>
        <div

            class="curr"
            :style="{
            left: left + 'px',
            top: type01.top,
            backgroundImage: 'url(' + bg + ')',
            backgroundPosition: '-' + type01.left + ' -' + type01.top

          }"
            :ref="(el) => el && (elDiv = el)"
        ></div>
        <div class="tips frowc" v-if="isOver">{{ cTips }}</div>
      </div>
      <div class="btn-bar frowc" :class="{ none: isOver }">
        <div class="text" v-if="!isInit">{{ tips1 }}</div>
        <div class="blank" :style="{ width: left + 'px' }" v-else></div>
        <div

            class="btn pointer"
            :style="{ left: left + 'px' }"
            @mousedown="handleStart"
            :ref="(el) => el && (elBtn = el)"
        ></div>
      </div>
    </div>
    <div class="type2" v-else-if="type === 2">
      <div class="title frow">请完成下列验证</div>
      <div class="btn-refresh pointer" @click="handleSort" v-if="!isOver"></div>
      <div class="btn-close pointer" @click="handleClose"></div>
      <div class="box">
        <div class="target hidden" :ref="(el) => el && (elDiv = el)"></div>
        <div

            class="curr"
            :class="{ active: isInit }"
            :style="{
            '--bg': 'url(' + bg + ')',
            '--angle1': 'rotate(' + rotate + 'deg)',
            '--angle2': 'rotate(' + (360 - rotate) + 'deg)'
          }"
        ></div>
        <div class="tips frowc" v-if="isOver">{{ cTips }}</div>
      </div>
      <div class="btn-bar frowc" :class="{ none: isOver }">
        <div class="text" v-if="!isInit">{{ tips2 }}</div>
        <div class="blank" :style="{ width: left + 'px' }" v-else></div>
        <div

            class="btn pointer"
            :style="{ left: left + 'px' }"
            @mousedown="handleStart"
            :ref="(el) => el && (elBtn = el)"
        ></div>
      </div>
    </div>
    <div class="type3" v-else-if="type === 3">
      <div class="title frow">请完成下列验证</div>
      <div class="btn-refresh pointer" @click="handleSort" v-if="!isOver"></div>
      <div class="btn-close pointer" @click="handleClose"></div>
      <div class="box" :style="{ backgroundImage: 'url(' + bg + ')' }">
        <div class="curr frowc hidden" :ref="(el) => el && (elDiv = el)"></div>
        <div

            class="curr frowc pointer"
            :style="item.pos"
            v-for="(item, i) in type03.list"
            :key="i"
            @click="handleClick(item.text, $event)"
        >
          {{ item.text }}
        </div>
        <div class="tips frowc" v-if="isOver">{{ cTips }}</div>
      </div>
      <div class="btn-bar frowc" :class="{ none: isOver }" :ref="(el) => el && (elBtn = el)">
        <div class="text" v-if="!list.length">{{ tips3 }}</div>
        <template v-else>
          <div class="blank frowc" v-for="(item, i) in type03.target" :key="i">{{ list[i] }}</div>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Captcha',
  emits: ['handleClose', 'handleRefresh', 'handleSucc', 'handleFail'],
  props: {
    bg: {
      type: String,
      default: require('./img/img_type01.png')
    },
    type: {
      type: Number,
      default: 1
    },
    range: {
      type: Number,
      default: 5
    },
    tips1: {
      type: String,
      default: '拖动按钮完成上方拼图'
    },
    tips2: {
      type: String,
      default: '拖动按钮使图片旋转到合适位置'
    },
    tips3: {
      type: String,
      default: '请从上图选出成语'
    },
    mData: {
      type: Object,
      default () {
        return {
          list: ['一帆风顺', '两全其美', '三阳开泰', '四通八达', '五谷丰登'],
          text: '混淆文字'
        }
      }
    }
  },
  data () {
    return {
      isInit: false,
      isOver: false,
      overTime: 0,
      left: 0,
      rotate: 0,
      list: [],
      elDiv: null,
      elBtn: null,
      type01: {},
      type02: 0,
      type03: {},
      touch: {}
    }
  },
  computed: {
    cTips () {
      return `本次用时${this.overTime}秒`
    }
  },
  methods: {
    handleSort () {
      let res = null
      let than = 60
      console.log(this.$refs)
      let pWidth = this.$refs.elDiv.parentNode.offsetWidth
      let pHeight = this.$refs.elDiv.parentNode.offsetHeight
      let width = this.$refs.elDiv.offsetWidth
      let height = this.$refs.elDiv.offsetHeight
      let left, top

      than *= 0.8
      pWidth -= width * 2
      left = parseInt(Math.random() * pWidth + width) + 'px'
      top = parseInt(Math.random() * (pHeight - height)) + 'px'

      while (Math.abs(parseInt(this.type01.left) - parseInt(left)) < than) {
        left = parseInt(Math.random() * pWidth + width) + 'px'
      }
      while (Math.abs(parseInt(this.type01.top) - parseInt(top)) < than) {
        top = parseInt(Math.random() * (pHeight - height)) + 'px'
      }

      res = (this.type01 = { left, top })

      this.$emit('handleRefresh', res)
    },
    handleStart (e) {
      if (this.isInit) return
      let touch = e.changedTouches ? e.changedTouches[0] : e
      this.isInit = true
      this.touch.pageX = touch.pageX

      // 解决PC拖拽bug

      if (typeof e.changedTouches === 'undefined') {
        document.onmousemove = (e) => this.handleMove(e)
        document.onmouseup = (e) => {
          this.handleEnd(e)
          document.onmousemove = document.onmouseup = null
        }
      }
    },
    handleMove (e) {
      if (!this.isInit) return
      let touch = e.changedTouches ? e.changedTouches[0] : e
      let diffX = touch.pageX - this.touch.pageX
      this.left = Math.max(0, Math.min(diffX, this.touch.max))

      if (this.type === 2) {
        this.rotate = parseInt((this.left / this.touch.max) * 360 + this.type02)
      }
    },
    handleEnd (e) {
      if (!this.isInit) return
      let isTrusted = e.isTrusted
      let diff, list

      if (this.type === 1) {
        diff = Math.abs(this.left - parseFloat(this.type01.left))
        list = [
          { el: this.$refs.elDiv, target: { left: 0 } },
          { el: this.$refs.elBtn, target: { left: 0 } }
        ]
      } else if (this.type === 2) {
        diff = Math.abs(parseFloat(this.rotate) - 360) % 360
        list = [
          { el: this.$refs.elDiv, target: { rotate: this.type02 } },
          { el: this.$refs.elBtn, target: { left: 0 } }
        ]
      }

      if (diff < this.range) {
        return this.handleOver(isTrusted)
      }

      this.isInit = false

      list.forEach(({ el, target }, index) => {
        if (!index && this.left > 0) {
          this.$emit('handleFail', isTrusted)
        }
        Object.entries(target).forEach(([key, val]) => (this[key] = val))
      })
    },
    handleClick (item, e) {
      let isTrusted = e.isTrusted
      let { target } = this.type03
      let length = this.list.push(item)

      if (length === target.length) {
        if (this.list.join() === target.join()) {
          return this.handleOver(isTrusted)
        }
        setTimeout(this.handleSort, 300)
        this.$emit('handleFail', isTrusted)
      }
    },
    handleOver (isTrusted) {
      this.isOver = true
      this.overTime = ((Date.now() - this.touch.time) / 1000).toFixed(1)
      this.$emit('handleSucc', isTrusted)
    },
    handleClose () {
      this.$emit('handleClose')
    }
  },
  mounted () {
    this.handleSort()
  }
}
</script>
<style lang="scss" scoped>
.fixed {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
}

.frow {
  display: flex;
  align-items: center;
}

.fcol {
  @extend .frow;
  flex-direction: column;
}

.frowc {
  @extend .frow;
  justify-content: center;
  align-items: center;
}

.fcolc {
  @extend .frowc;
  @extend .fcol;
}

.flex {
  flex: 1;
}

.elps {
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.none {
  pointer-events: none;
}

.pointer {
  cursor: pointer;
}

.hidden {
  visibility: hidden;
}
</style>

<style lang="scss" scoped>
.captcha-wrap {
  z-index: 9;
  font-size: 14px;
  color: #333;
  user-select: none;
  backdrop-filter: blur(2px);
  background: rgba(0, 0, 0, 0.3);
  * {
    margin: 0;
    padding: 0;
    -webkit-tap-highlight-color: transparent;
  }
  .title {
    height: 50px;
    font-size: 16px;
  }
  .btn-refresh {
    position: absolute;
    right: 60px;
    top: 5px;
    width: 40px;
    height: 40px;
    background: url(./img/img_refresh01.png) no-repeat center / cover;
  }
  .btn-close {
    position: absolute;
    right: 5px;
    top: 5px;
    width: 40px;
    height: 40px;
    background: url(./img/img_close01.png) no-repeat center / cover;
  }
  .tips {
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    font-size: 18px;
    color: #fff;
    backdrop-filter: blur(2px);
    background: rgba(0, 0, 0, 0.5);
  }
  .type1 {
    position: relative;
    width: 280px;
    padding: 0 20px 20px;
    border-radius: 10px;
    background: #fff;
    $val: 60px;
    .box {
      position: relative;
      width: 100%;
      height: 160px;
      overflow: hidden;
      background: #f8f8f8 no-repeat;
      .target,
      .curr {
        position: absolute;
        left: 0;
        top: 0;
        width: $val;
        height: $val - 10px;
        opacity: 0.8;
        background: #fff no-repeat;
      }
      .curr {
        opacity: 1;
      }
    }
    .btn-bar {
      position: relative;
      margin-top: 10px;
      height: 44px;
      font-size: 12px;
      color: #999;
      background: #f8f8f8;
      .text {
        padding-left: 50px;
      }
      .blank,
      .btn {
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        background: #b7f0d8;
      }
      .btn {
        width: $val;
        border-radius: 5px;
        background: #eee url(./img/img_arrow-right01.png) no-repeat center /
          cover;
      }
    }
  }
  .type2 {
    position: relative;
    width: 280px;
    padding: 0 20px 20px;
    border-radius: 10px;
    background: #fff;
    $val: 60px;
    .box {
      position: relative;
      width: 100%;
      height: 160px;
      .target {
        position: absolute;
        left: 0;
        top: 0;
        width: $val;
        height: $val - 10px;
        background: #fff no-repeat;
      }
      .curr {
        position: relative;
        width: 160px;
        height: 160px;
        margin: 0 auto;
        overflow: hidden;
        border-radius: 50%;
        &.active {
          &::before,
          &::after {
            border-color: transparent;
          }
        }
        &::before {
          content: "";
          position: absolute;
          left: 0;
          top: 0;
          right: 0;
          bottom: 0;
          border-radius: 50%;
          border: 30px solid rgba(0, 0, 0, 0.2);
          transform: var(--angle1);
          transition: border 0.3s;
          background: #fff var(--bg) no-repeat center;
        }
        &::after {
          content: "";
          position: absolute;
          left: 30px;
          top: 30px;
          right: 30px;
          bottom: 30px;
          border: 2px solid #fff;
          border-radius: 50%;
          transform: var(--angle2);
          transition: border 0.3s;
          background: var(--bg) no-repeat center;
        }
      }
    }
    .btn-bar {
      position: relative;
      margin-top: 10px;
      height: 44px;
      font-size: 12px;
      color: #999;
      background: #f8f8f8;
      .text {
        padding-left: 50px;
      }
      .blank,
      .btn {
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        background: #b7f0d8;
      }
      .btn {
        width: $val;
        border-radius: 5px;
        background: #eee url(./img/img_arrow-right01.png) no-repeat center /
          cover;
      }
    }
  }
  .type3 {
    position: relative;
    width: 280px;
    padding: 0 20px 20px;
    border-radius: 10px;
    background: #fff;
    $val: 40px;
    .box {
      position: relative;
      width: 100%;
      height: 160px;
      background: #f8f8f8 no-repeat;
      .curr {
        position: absolute;
        left: 0;
        top: 0;
        width: $val;
        height: $val;
        opacity: 0.8;
        background: #fff no-repeat;
      }
    }
    .btn-bar {
      position: relative;
      margin-top: 10px;
      height: 44px;
      font-size: 12px;
      color: #999;
      background: #f8f8f8;
      .blank {
        flex: 0 0 $val;
        height: $val;
        margin-right: 10px;
        font-size: 16px;
        border: 1px dashed;
        &:last-child {
          margin-right: 0;
        }
      }
    }
  }
}
</style>
