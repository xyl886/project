<template>
  <div style="background-color: #d9ecff;">
    <div class="container" style="min-height: 660px">
      <div class="left" style="max-width: 300px;">
        <div style="margin-top: 5px;margin-bottom: 10px">
          <!--          <el-input v-model="input3" placeholder="请输入内容" class="input-with-select">-->
          <!--            <el-button slot="append" icon="el-icon-search"/>-->
          <!--          </el-input>-->
        </div>
        <el-menu>
          <el-menu-item
            v-for="user in userList"
            :key="user.id"
            :class="{ selected: user.selected }">
            <div class="user-info"  @click="selectUser(user)">
              <el-image :src="user.userInfo?user.userInfo.avatar:''" style="width: 50px;height: 50px;border-radius:50%;margin-bottom: 5px"/>
              <div class="list-right">
                <p class="name">{{ user.userInfo.nickname }}</p><span class="time">昨天 11:11</span>
                <p class="lastmsg">{{ user.userInfo.remark }}</p>
              </div>
              <i class="el-icon-delete  delete-btn" @click.stop="handleDelete(user)"/>
            </div>
          </el-menu-item>
        </el-menu>
      </div>
      <div class="right" style="max-width: 1000px;">
        <div v-if="selectedUser">
          <span style="padding:0 0 0 20px;">
            {{ selectedUser.userInfo.nickname }}
          </span>
          <div style="float: right;">
            <el-dropdown>
              <div style="display: flex;">
                <span style="margin-right: 20px;line-height: 20px;font-size: 16px;">
                  <i class="el-icon-more"/>
                </span>
              </div>
              <el-dropdown-menu
                slot="dropdown">
                <el-dropdown-item size="small" type="danger" @click="deleteAllMsgs">删除所有消息</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
        <el-divider style=""/>
        <!--        <div v-if="selectedUser">-->
        <div v-if="selectedUser" id="message-list" style="height: 400px;overflow: scroll;">
          <div
            v-for="message in messageList[this.userInfo.id + selectedUser.beFollowedUserId]"
            :key="message.id"
            style="margin: 5px;padding: 5px 0;line-height: 40px;">
            <div class="time"><span>{{ formatDate(message.sentTime) }}</span></div>
            <div :class="message.sentByMe?'message-right':'message-left'">
              <el-image :src="message?message.fromIdAvatar:''" class="message-avatar"/>
              <div class="content">
                <div class="text" style="border-radius: 10px;">{{ message.message }}</div>
              </div>
            </div>
          </div>
        </div>
        <!--        </div>-->
        <el-divider style="margin: 10px 0"/>
        <div v-if="selectedUser" style="">
          <el-row :gutter="20">
            <el-col :span="2"> <el-button circle><i class="el-icon-picture"/></el-button></el-col>
            <el-col :span="2">
              <el-popover placement="top" trigger="click" class="popover" style="margin: 20px;">
                <custom-emoji v-if="showEmojiCom" class="emoji-component" @addemoji="addEmoji"/>
                <el-button slot="reference" circle @click.stop="showEmojiCom = !showEmojiCom">
                  😃
                </el-button>
              </el-popover>
            </el-col>
          </el-row>
          <div class="message-input">
            <textarea v-model="selectedUserMessage.message" class="textarea" @keyup.enter="sendMsg"/>
          </div>
          <div class="button-container">
            <el-button type="primary" size="mini" style="margin-bottom: 5px;margin-right: 10px" @click="sendMsg">发送</el-button>
          </div>
        </div>
      </div>
    </div>
    <div/>
  </div>
</template>

<script>
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import { Message } from 'element-ui'
import { mapGetters } from 'vuex'
import { formatDate } from '../../utils/date'
import customEmoji from '../../components/emoji/index.vue'
import { getFriendList, listPrivateMessages, deleteAllMsg, deleteFriend } from '../../api/chat'

export default {
  name: 'Room',
  components: { customEmoji },
  data() {
    return {
      input3: '',
      select: '',
      showEmojiCom: false,
      userList: [],
      groupList: [],
      selectedUser: null,
      message: '',
      stompClient: null,
      messageList: {}, // 使用对象来存储每个用户的聊天记录
      usernameOnlineList: [],
      userNum: 1, // 好友数
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        followType: 1
      },
      selectedUserMessage: {
        user: null,
        message: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  created() {
    this.userInfo = mapGetters(['userInfo'])
    this.connect()
    this.listAllUsers()
    console.log(this.userInfo)
    document.addEventListener('click', this.handlerShowEmoji)
  },
  mounted() {
    this.scrollToBottom()
  },
  beforeDestroy() {
    this.disconnect()
    document.removeEventListener('click', this.handlerShowEmoji)
  },
  methods: {
    handleDelete(user) {
      console.log(user)
      this.$confirm('确认删除与该用户的聊天吗？', '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确认删除逻辑
        const friendUserId = user.beFollowedUserId
        deleteFriend(friendUserId).then((res) => {
          if (res.code === 200) {
            this.listAllUsers()
            this.messageList[this.userInfo.id + this.selectedUserMessage.user.beFollowedUserId] = []
            this.$message.success(res.msg)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
        // 取消删除逻辑
      })
    },
    scrollToBottom() {
      const container = document.getElementById('message-list')
      container.scrollTop = container.scrollHeight - container.clientHeight
    },
    handlerShowEmoji() {
      this.showEmojiCom = false
    },
    addEmoji(emoji = '') {
      console.log(emoji)
      this.selectedUserMessage.message += emoji
      console.log(this.selectedUserMessage.message)
    },
    formatDate,
    //  在发送信息之后，将输入的内容中属于表情的部分替换成emoji图片标签
    //  再经过v-html 渲染成真正的图片
    replaceFace(con) {
      if (con.includes('/:')) {
        const emojis = this.emojis
        for (let i = 0; i < emojis.length; i++) {
          con = con.replace(emojis[i].reg, '<img src="static/emoji/' + emojis[i].file + '"  alt="" style="vertical-align: middle; width: 24px; height: 24px" />')
        }
        return con
      }
      return con
    },
    listAllUsers() {
      // getPage(this.page).then((res) => {
      getFriendList(this.page).then((res) => {
        this.userNum = res.data.length
        console.log(this.userNum)
        // this.usernameOnlineList = response.data.usernameOnlineList;
        this.userList = res.data.filter(
          (user) => user.id !== this.userInfo.id
        )
        this.selectUser(this.userList[0])
        console.log(this.userList[0])
      })
      this.selectedUserMessage.message = ''
    },
    selectUser(user) {
      if (!this.messageList[this.userInfo.id + user.beFollowedUserId]) {
        this.$set(this.messageList, this.userInfo.id + user.beFollowedUserId, [])
      }

      // TODO 展示数据库中存在的信息，也就是聊天记录
      listPrivateMessages(this.userInfo.id, user.beFollowedUserId).then((response) => {
        this.$set(this.messageList, this.userInfo.id + user.beFollowedUserId, response.data)
        console.log(response.data)
      })
      console.log(this.messageList)
      console.log(user)
      this.selectedUser = user
      this.selectedUserMessage.user = user
      this.selectedUserMessage.message = '' // 清空输入框内容
      this.userList.forEach((u) => {
        u.selected = false
      })
      user.selected = true
    },
    sendMsg() {
      if (this.stompClient !== null && this.selectedUserMessage.message !== '') {
        this.stompClient.send('/ClientToServer/privateChat', {},
          JSON.stringify({
            fromId: this.userInfo.id,
            message: this.selectedUserMessage.message,
            toId: this.selectedUserMessage.user.beFollowedUserId
          })
        )
        // if (!this.messageList[this.selectedUserMessage.user.fromUsername]) {
        //   this.$set(
        //     this.messageList,
        //     this.selectedUserMessage.user.fromUsername,
        //     []
        //   );
        // }
        this.messageList[this.userInfo.id + this.selectedUserMessage.user.beFollowedUserId].push({
          fromId: this.userInfo.id,
          fromIdAvatar: this.userInfo.avatar,
          message: this.selectedUserMessage.message,
          sentByMe: true,
          sentTime: Date.now(),
          Id: this.selectedUserMessage.user.beFollowedUserId
        })
        console.log(this.messageList)
        console.log(this.selectedUserMessage.message)
        this.selectedUserMessage.message = '' // 清空输入框内容
      } else {
        this.$message.warning('请输入信息')
      }
    },
    deleteAllMsgs() {
      if (this.messageList[this.userInfo.id + this.selectedUserMessage.user.beFollowedUserId] === '') {
        Message.error('当前没有聊天记录')
        return
      }
      deleteAllMsg(this.userInfo.id, this.selectedUser.beFollowedUserId).then(
        (res) => {
          console.log(res)
          this.messageList[this.userInfo.id + this.selectedUserMessage.user.beFollowedUserId] = []
          Message.success('删除成功')
        }
      )
    },
    connect() {
      const socket = new SockJS('/api/webSocket')
      this.stompClient = Stomp.over(socket)
      this.stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame)
        this.stompClient.subscribe(
          '/topic/ServerToClient.private.' + this.userInfo.id,
          (result) => {
            this.showContent(
              JSON.parse(result.body).message,
              JSON.parse(result.body).fromId,
              JSON.parse(result.body).beFollowedUserId
            )
          })
        this.stompClient.subscribe('/topic/ServerToClient.deleteMsg', (result) => {
          const res = JSON.parse(result.body)
          this.messageList[res.beFollowedUserId + res.fromId] = []
          // if (this.selectedUser !== null) {
          //   this.messageList[this.username + this.selectedUser.beFollowedUserId] = [];
          // }
        })
        this.stompClient.subscribe('/topic/ServerToClient.showUserNumber', (result) => {
          this.userNum = result.body
        })
      })
    },
    disconnect() {
      if (this.stompClient !== null) {
        this.stompClient.disconnect()
      }
      console.log('Disconnected')
    },
    showContent(body, from, to) {
      // 处理接收到的消息
      // 示例代码，根据实际需求进行修改
      // console.log(this.selectedUser);
      if (!this.messageList[to + from]) {
        this.$set(this.messageList, to + from, []) // 初始化选定用户的聊天记录数组
      }
      this.messageList[to + from].push(body) // 将接收到的消息添加到选定用户的聊天记录数组

      // console.log(body);
    }
  }

}
</script>

<style scoped>
.delete-btn {
  visibility: hidden;
}
.user-info:hover .delete-btn {
  visibility: visible;
}
.el-divider{
  margin:10px 0!important;
}
.el-menu-item{
  padding: 5px 10px!important;
  height: 70px!important;
}
.el-menu-item.is-active{
  color: #000;
}
.el-menu-item.selected{
  background-color: #f4f5f7;
}
.container {
  display: flex;
  justify-content: space-between;
  margin:0 100px ;
  padding: 20px 0;
  min-height: calc(100% - 100px);
}

.left, .right {
  flex: 0.5;
  margin: 5px;
  padding: 5px 0;
  background-color: #f4f5f7;
}

.right {
  flex: 2;
}

.bottom {
  margin-top: 20px;
  text-align: center;
}

li {
  cursor: pointer;
  transition: color 0.3s ease;
}

.send-button {
  display: flex;
  justify-content: flex-end;
}

.message-input {
  display: flex;
  align-items: center;
}

.button-container {
  margin-left: 10px; /* 调整间距大小 */
}

.message-container {
  display: flex;
  justify-content: flex-end;
}

.button-container {
  display: flex;
  justify-content: flex-end;
}

.user-info {
  display: flex;
  align-items: center;
}

.button-container {
  margin-left: auto;
}
.message-left,.message-right{
  flex: 0.5;
  background-color: #f4f5f7;
}
.message-right > .content > .text{
  background-color: #409EFF;
}
.message-right > .message-avatar{
  float: right;
}
.message-avatar{
  float:left;
  width: 40px;
  height: 40px;
  border-radius:50%;
  margin-left: 5px;
}
.content >.text{
  min-height: 40px;
  min-width: 40px;
  max-width: 500px;
  display:inline-block;
  background-color: #fff;
  margin-left: 10px;
  margin-right: 5px;
  padding: 0 10px;
}
.message-left{
  text-align: left;
}
.message-right {
  text-align: right;
  flex: 2;
}
.time{
  width: 100%;
  line-height: 12px;
  font-size: 12px;
  margin: 7px auto;
  text-align: center;
}
.time > span{
  line-height: 10px;
  display: inline-block;
  border-radius: 3px;
}
.list-right {
  position: relative;
  flex: 1;
  width: 160px;
  height: 50px;
  margin: 5px;
}

.list-right .name {
  font-size: 16px;
  line-height: 14px;
  width: 50px;
  margin: 0;
  padding: 0;
  display: inline-block;
  vertical-align: top;
}

.list-right>.time {
  margin: 0;
  padding: 0;
  width: 50px;
  vertical-align: top;
  display: inline-block;
  float: right;
  color: #999;
  font-size: 10px;
}

.list-right .lastmsg {
  margin: 0;
  padding: 0;
  position: absolute;
  font-size: 12px;
  width: 130px;
  height: 15px;
  line-height: 15px;
  color: #999;
  bottom: 4px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.textarea{
  box-sizing: border-box;
  padding: 10px;
  height: 110px;
  width: 100%;
  border: none;
  outline: none;
  font-family: "Micrsofot Yahei",sans-serif;
  background-color: #f4f5f7;
  resize: none;
}
</style>
