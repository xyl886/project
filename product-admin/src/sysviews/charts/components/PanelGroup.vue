<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" >
        <div class="card-panel-icon-wrapper icon-money">
          <svg
            t="1634371588983"
            class="icon  card-panel-icon"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="5315"
            width="200"
            height="200"><path d="M896 223.744v315.392c0 17.92-14.336 31.744-31.744 31.744s-31.744-14.336-31.744-31.744V223.744c0-17.92-14.336-31.744-31.744-31.744H224.256c-17.408 0-31.744 14.336-31.744 31.744v640c0 17.408 14.336 31.744 31.744 31.744H471.04c17.92 0 31.744 14.336 31.744 31.744 0 17.408-14.336 31.744-31.744 31.744H224.256c-52.736 0-96.256-43.008-96.256-96.256v-640C128 171.008 171.008 128 224.256 128h576c52.736 0 95.744 43.008 95.744 95.744z m-284.16 291.328c0 17.92-16.384 31.744-36.864 31.744h-281.6c-20.48 0-36.864-14.336-36.864-31.744 0-17.408 16.384-31.744 36.864-31.744h281.6c20.48 0 36.864 14.336 36.864 31.744z m-150.016 184.32c0 17.408-16.384 31.744-36.864 31.744H292.864c-20.48 0-36.864-14.336-36.864-31.744 0-17.92 16.384-31.744 36.864-31.744H424.96c20.48 0 36.864 14.336 36.864 31.744z m242.176-360.96c0 17.92-14.336 31.744-31.744 31.744h-384c-17.92 0-31.744-14.336-31.744-31.744s14.336-31.744 31.744-31.744h384c17.408 0 31.744 14.336 31.744 31.744z" p-id="5316"/><path d="M889.344 737.28c-2.048-7.68-9.216-12.8-17.408-12.288-10.24 0.512-19.968-4.608-25.088-13.312-5.12-8.704-4.608-19.968 1.024-28.672 4.608-6.656 3.584-15.36-2.048-21.504-23.04-24.064-52.224-40.96-84.48-48.64-7.68-2.048-15.872 2.048-19.456 9.216-4.608 9.216-13.824 14.848-24.064 14.848s-19.456-5.632-24.064-14.848c-3.584-7.168-11.776-10.752-19.456-9.216-32.256 8.192-61.44 25.088-84.48 48.64-5.632 5.632-6.656 14.848-2.048 21.504 5.632 8.704 6.144 19.456 1.024 28.16-5.12 8.704-14.848 14.336-25.088 13.312-8.192-0.512-15.36 4.608-17.408 12.288-4.608 15.872-6.656 32.256-6.656 48.64s2.048 33.28 6.656 48.64c2.048 7.68 9.728 12.8 17.408 12.288 10.24-0.512 19.968 4.608 25.088 13.312 5.12 8.704 4.608 19.968-1.024 28.16-4.608 6.656-3.584 15.872 2.048 21.504 23.04 24.064 52.224 40.96 84.48 48.64 7.68 2.048 15.872-2.048 19.456-9.216 4.608-9.216 13.824-14.848 24.064-14.848s19.456 5.632 24.064 14.848c3.072 6.144 9.216 9.728 15.36 9.728 1.536 0 2.56 0 4.096-0.512 32.256-8.192 61.44-25.088 84.48-48.64 5.632-5.632 6.144-14.848 2.048-21.504-5.632-8.704-6.144-19.456-1.024-28.16 5.12-8.704 14.848-14.336 25.088-13.312 8.192 0.512 15.36-4.608 17.408-12.288 4.608-15.872 6.656-32.256 6.656-48.64 0-15.872-2.048-32.256-6.656-48.128z m-45.056 74.24c-15.872 3.072-29.696 12.288-37.888 26.624-8.192 13.824-9.728 30.72-4.096 46.08-12.8 11.264-27.136 19.456-43.008 25.088-10.24-12.288-25.6-19.456-41.472-19.456h-0.512c-16.384 0-31.232 7.168-41.472 19.456-15.872-5.632-30.72-13.824-43.52-25.088 5.632-15.36 4.096-31.744-4.096-46.08s-22.016-23.552-37.888-26.624c-3.072-16.384-3.072-33.28 0-50.176 15.872-3.072 29.696-12.288 37.888-26.624 8.192-13.824 9.728-30.72 4.096-46.08 12.8-11.264 27.136-19.456 43.008-25.088 10.24 12.288 25.6 19.456 41.984 19.456 16.384 0 31.232-7.168 41.984-19.456 15.872 5.632 30.72 13.824 43.52 25.088-5.632 15.36-4.096 31.744 4.096 46.08s22.016 23.552 37.888 26.624c3.072 16.384 3.072 33.28-0.512 50.176z" p-id="5317"/><path d="M717.312 708.096c-43.008 0-78.336 35.328-78.336 78.336s35.328 78.336 78.336 78.336 78.336-35.328 78.336-78.336-34.816-78.336-78.336-78.336z m0 114.176c-19.968 0-35.84-16.384-35.84-35.84 0-19.968 16.384-35.84 35.84-35.84 19.968 0 35.84 16.384 35.84 35.84 0.512 19.968-15.872 35.84-35.84 35.84z" p-id="5318"/></svg>
        </div>
        <div class="card-panel-description" @click="handleSetLineChartPendingData()">
          <div class="card-panel-text">
            待审核
          </div>
          <count-to :start-val="0" :end-val="lineData.pendingPostsCount" :duration="3200" class="card-panel-num" />
        </div>
        <div class="card-panel-description" @click="handleSetLineChartData('/index/post')">
          <div class="card-panel-text">
            帖子数
          </div>
          <count-to :start-val="0" :end-val="lineData.postsCount" :duration="3200" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('/index/category')">
        <div class="card-panel-icon-wrapper icon-shopping">
          <svg t="1649990742540" class="icon card-panel-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1943" width="200" height="200"><path d="M92.161 959.999h839.68c15.553 0 28.16-12.609 28.16-28.16s-12.608-28.16-28.16-28.16h-811.52V92.159c0-15.553-12.608-28.16-28.16-28.16s-28.16 12.608-28.16 28.16v839.68c0 15.552 12.608 28.16 28.16 28.16z m135.803-219.165c32.945 0 59.647-30.339 59.647-67.757 0-9.026-1.567-17.634-4.387-25.51l117.759-153.753a53.084 53.084 0 0 0 17.855 3.092c17.38 0 33.018-8.449 43.919-21.922l111.182 63.334c0.36 37.068 26.92 67.002 59.63 67.002 32.944 0 59.647-30.34 59.647-67.758 0-1.254-0.035-2.496-0.094-3.732l154.882-151.144c7.387 3.723 15.554 5.807 24.154 5.807 32.946 0 59.648-30.333 59.648-67.758 0-37.419-26.702-67.757-59.648-67.757-32.933 0-59.647 30.338-59.647 67.757 0 7.711 1.15 15.112 3.24 22.017L670.74 484.59c-10.196-9.246-23.11-14.787-37.173-14.787-16.215 0-30.912 7.365-41.665 19.297l-113.574-64.602c-2.106-35.248-27.924-63.108-59.491-63.108-32.935 0-59.648 30.34-59.648 67.758 0 10.268 2.022 19.996 5.621 28.717L248.922 609.631c-6.52-2.78-13.581-4.312-20.958-4.312-32.935 0-59.648 30.338-59.648 67.758 0 37.418 26.713 67.757 59.648 67.757z" fill="" p-id="1944"/></svg>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            标签数
          </div>
          <count-to :start-val="0" :end-val="lineData.categoryCount" :duration="3600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('/index/user')">
        <div class="card-panel-icon-wrapper icon-people">
          <svg t="1689836900735" class="icon card-panel-icon" viewBox="0 0 1152 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2659" xmlns:xlink="http://www.w3.org/1999/xlink" width="225" height="200"><path d="M768 770.624v-52.768c70.496-39.712 128-138.784 128-237.824 0-159.072 0-288-192-288s-192 128.928-192 288c0 99.072 57.504 198.112 128 237.824v52.768C422.912 788.384 256 895.04 256 1024h896c0-128.96-166.912-235.648-384-253.376z" p-id="2660"/><path d="M327.2 795.328c55.296-36.16 124.064-63.648 199.776-80.416a362.56 362.56 0 0 1-40.48-59.008c-30.4-55.232-46.496-116.064-46.496-175.904 0-86.048 0-167.296 30.592-233.76 29.696-64.512 83.136-104.48 159.232-119.488C612.896 50.272 567.872 0 448 0 256 0 256 128.928 256 288c0 99.072 57.504 198.112 128 237.824v52.768c-217.088 17.76-384 124.416-384 253.376h279.008c14.528-12.896 30.592-25.184 48.192-36.672z" p-id="2661"/></svg>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            已认证
          </div>
          <count-to :start-val="0" :end-val="lineData.authCount" :duration="2600" class="card-panel-num" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            用户量
          </div>
          <count-to :start-val="0" :end-val="lineData.userCount" :duration="2600" class="card-panel-num" />
        </div>

      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('/index/chat')">
        <div class="card-panel-icon-wrapper icon-chatMessage">
          <svg t="1632628125241" class="icon card-panel-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2452" width="200" height="200"><path d="M511.913993 928.016126c-8.256677 0-16.341341-3.096254-22.705863-9.460776l-95.983874-95.983874c-12.55703-12.55703-12.55703-32.682681 0-45.239711s32.682681-12.55703 45.239711 0l73.450025 73.450025 73.450025-73.450025c6.020494-6.020494 14.105157-9.460776 22.705863-9.460776l223.962372 0c17.717453 0 31.994625-14.277171 31.994625-31.994625L864.026877 223.962372c0-17.545439-14.277171-31.994625-31.994625-31.994625l-639.892491 0c-17.545439 0-31.994625 14.449185-31.994625 31.994625l0 511.913993c0 17.717453 14.449185 31.994625 31.994625 31.994625l95.983874 0c17.717453 0 31.994625 14.277171 31.994625 31.994625s-14.277171 31.994625-31.994625 31.994625l-95.983874 0c-52.980346 0-95.983874-43.003528-95.983874-95.983874L96.155888 223.962372c0-52.980346 43.003528-95.983874 95.983874-95.983874l639.892491 0c52.980346 0 95.983874 43.003528 95.983874 95.983874l0 511.913993c0 52.980346-43.003528 95.983874-95.983874 95.983874L621.142953 831.860239 534.619856 918.55535C528.427348 924.747858 520.17067 928.016126 511.913993 928.016126z" p-id="2453"/><path d="M335.943558 511.913993c-26.490173 0-47.991937-21.501764-47.991937-47.991937s21.501764-47.991937 47.991937-47.991937 47.991937 21.501764 47.991937 47.991937S362.433731 511.913993 335.943558 511.913993z" p-id="2454"/><path d="M527.911305 511.913993c-26.490173 0-47.991937-21.501764-47.991937-47.991937s21.501764-47.991937 47.991937-47.991937 47.991937 21.501764 47.991937 47.991937S554.401478 511.913993 527.911305 511.913993z" p-id="2455"/><path d="M720.051067 511.913993c-26.490173 0-47.991937-21.501764-47.991937-47.991937s21.501764-47.991937 47.991937-47.991937c26.490173 0 47.991937 21.501764 47.991937 47.991937S746.369226 511.913993 720.051067 511.913993z" p-id="2456"/></svg>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            消息
          </div>
          <count-to :start-val="0" :end-val="lineData.commentCount" :duration="3000" class="card-panel-num" />
        </div>

      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import { lineCount } from '../../../api/init'
import router from '../../../router'

export default {
  components: {
    CountTo
  },
  data() {
    return {
      lineData: []
    }
  },
  created() {
    lineCount().then(res => {
      this.lineData = res.data
    })
  },
  methods: {
    handleSetLineChartPendingData() {
      const filter = {
        statusId: 1
      }
      // 使用编程式导航跳转到目标路径，并将筛选条件作为查询参数传递
      router.push({
        path: '/index/post', // 替换为目标组件的路由路径
        query: filter // 筛选条件作为查询参数传递
      })
    },
    handleSetLineChartData(path) {
      if (path !== null) this.$router.push(path)
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }
.icon{
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}
  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-chatMessage {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-chatMessage {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px 26px 26px 0;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
