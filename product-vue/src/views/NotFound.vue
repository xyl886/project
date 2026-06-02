<template>
  <div class="not-found">
    <h1>404</h1>
    <p>页面未找到</p>
    <el-button type="primary" @click="$router.push('/home')">返回首页</el-button>
  </div>
</template>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  gap: 16px;
}
.not-found h1 {
  font-size: 72px;
  color: #409EFF;
  margin: 0;
}
.not-found p {
  font-size: 18px;
  color: #999;
}
</style>
