<template>
  <div>
    <el-button @click="toggleBatchMode" v-show="!batchMode">批量操作</el-button>
    <el-button @click="cancelBatch" v-show="batchMode && selectedItems.length > 0">取消收藏</el-button>
    <el-button @click="returnToNormal" v-show="batchMode">返回</el-button>

    <div v-for="item in items" :key="item.id">
      <el-input type="checkbox" v-show="batchMode" :checked="isSelected(item.id)" @change="toggleSelection(item.id)" />
      <span>{{ item.title }}</span>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      batchMode: false,
      items: [
        { id: 1, title: '帖子1' },
        { id: 2, title: '帖子2' },
        { id: 3, title: '帖子3' }
      ],
      selectedItems: []
    }
  },
  methods: {
    toggleBatchMode () {
      this.batchMode = !this.batchMode
      this.selectedItems = []
    },
    toggleSelection (itemId) {
      const index = this.selectedItems.indexOf(itemId)
      if (index > -1) {
        this.selectedItems.splice(index, 1)
      } else {
        this.selectedItems.push(itemId)
      }
    },
    isSelected (itemId) {
      return this.selectedItems.includes(itemId)
    },
    cancelBatch () {
      // 执行取消收藏的逻辑
      // 可以使用 this.selectedItems 数组来获取选中的帖子ID，并发送请求执行取消收藏操作
      console.log('取消收藏选中的帖子:', this.selectedItems)
    },
    returnToNormal () {
      this.batchMode = false
      this.selectedItems = []
    }
  }
}
</script>
