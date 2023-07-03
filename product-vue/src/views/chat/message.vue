<script>
export default {
  filters: {
    // 将日期过滤为 hour:minutes
    time (date) {
      if (typeof date === 'string') {
        date = new Date(date)
      }
      return date.getHours() + ':' + date.getMinutes()
    }
  },
  directives: {
    // 发送消息后滚动到底部
    'scroll-bottom' () {
      this.vm.$nextTick(() => {
        this.el.scrollTop = this.el.scrollHeight - this.el.clientHeight
      })
    }
  },
  data () {
    return {
      // 当前用户
      user: {
        name: 'coffce',
        img: 'dist/images/1.jpg'
      },
      // 会话列表
      sessions: [
        {
          id: 1,
          user: {
            name: '示例介绍',
            img: 'dist/images/2.png'
          },
          messages: [
            {
              content: 'Hello，这是一个基于Vue + Vuex + Webpack构建的简单chat示例，聊天记录保存在localStorge, 有什么问题可以通过Github Issue问我。',
              date: 'now'
            }, {
              content: '项目地址: https://github.com/coffcer/vue-chat',
              date: 'now'
            }
          ]
        },
        {
          id: 2,
          user: {
            name: 'webpack',
            img: 'dist/images/3.jpg'
          },
          messages: []
        }
      ],
      // 当前选中的会话
      currentSessionId: 1,
      // 过滤出只包含这个key的会话
      filterKey: ''
    }
  }
}
</script>

<template>
<div class="message" v-scroll-bottom="sessions.messages">
    <ul v-if="sessions">
        <li v-for="item in sessions.messages" :key="item">
            <p class="time">
                <span>{{ item.date | time }}</span>
            </p>
            <div class="main" :class="{ self: item.self }">
                <img class="avatar" width="30" height="30" :src="item.self ? user.img : sessions.user.img"  alt=""/>
                <div class="text">{{ item.content }}</div>
            </div>
        </li>
    </ul>
</div>
</template>

<style lang="less" scoped>
.message {
    padding: 10px 15px;
    overflow-y: scroll;

    li {
        margin-bottom: 15px;
    }
    .time {
        margin: 7px 0;
        text-align: center;

        > span {
            display: inline-block;
            padding: 0 18px;
            font-size: 12px;
            color: #fff;
            border-radius: 2px;
            background-color: #dcdcdc;
        }
    }
    .avatar {
        float: left;
        margin: 0 10px 0 0;
        border-radius: 3px;
    }
    .text {
        display: inline-block;
        position: relative;
        padding: 0 10px;
        max-width: ~'calc(100% - 40px)';
        min-height: 30px;
        line-height: 2.5;
        font-size: 12px;
        text-align: left;
        word-break: break-all;
        background-color: #fafafa;
        border-radius: 4px;

        &:before {
            content: " ";
            position: absolute;
            top: 9px;
            right: 100%;
            border: 6px solid transparent;
            border-right-color: #fafafa;
        }
    }

    .self {
        text-align: right;

        .avatar {
            float: right;
            margin: 0 0 0 10px;
        }
        .text {
            background-color: #b2e281;

            &:before {
                right: inherit;
                left: 100%;
                border-right-color: transparent;
                border-left-color: #b2e281;
            }
        }
    }
}
</style>
