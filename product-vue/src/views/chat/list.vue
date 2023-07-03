<template>
  <div class="list">
    <ul>
      <li v-for="item in sessions" :class="{ active: item.id === currentId }" @click="selectSession(item.id)">
        <img class="avatar"  width="30" height="30" :alt="item.user.name" :src="item.user.img">
        <p class="name">{{item.user.name}}</p>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data () {
    return {
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
      ]
    }
  }
}
</script>

<style scoped lang="less">
.list {
    li {
        padding: 12px 15px;
        border-bottom: 1px solid #292C33;
        cursor: pointer;
        transition: background-color .1s;

        &:hover {
            background-color: rgba(255, 255, 255, 0.03);
        }
        &.active {
            background-color: rgba(255, 255, 255, 0.1);
        }
    }
    .avatar, .name {
        vertical-align: middle;
    }
    .avatar {
        border-radius: 2px;
    }
    .name {
        display: inline-block;
        margin: 0 0 0 15px;
    }
}
</style>
