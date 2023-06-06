<template>
  <div class="chat-container">
    <div class="friend-list">
      <el-row>
        <el-col :span="12">
          <h2>好友列表</h2>
          <el-list v-for="friend in friends" :key="friend.id">
            <el-list-item :class="{ active: friend.id === activeFriendId }" @click="selectFriend(friend.id)">
              {{ friend.name }}
            </el-list-item>
          </el-list>
        </el-col>
        <el-col :span="12">
          <h2>聊天窗口</h2>
          <el-card>
            <el-chat :messages="messages" />
            <el-input v-model="inputMessage" placeholder="请输入消息" />
            <el-button type="primary" @click="sendMessage">发送</el-button>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      friends: [
        { id: 1, name: '好友1' },
        { id: 2, name: '好友2' },
        { id: 3, name: '好友3' }
      ],
      activeFriendId: null,
      messages: [],
      inputMessage: ''
    }
  },
  methods: {
    selectFriend (friendId) {
      this.activeFriendId = friendId
      // 根据friendId获取聊天记录等逻辑
    },
    sendMessage () {
      if (this.activeFriendId && this.inputMessage) {
        const message = {
          sender: '当前用户',
          content: this.inputMessage,
          timestamp: new Date()
        }
        // 将消息发送给当前好友的逻辑
        this.messages.push(message)
        this.inputMessage = ''
      }
    }
  }
}
</script>

<style>
.chat-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.friend-list {
  width: 600px;
}

.active {
  background-color: #e6f7ff;
}

h2 {
  margin-bottom: 20px;
}
</style>
