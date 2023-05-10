<template>
  <div v-if="this.path !== '/share'" style="font-size: 14px;border-top: 1px solid #e7e7e7;">
    <div style="height: 60px;text-align: center;line-height: 60px;">
      Powered by
      <span style="color: #409EFF;cursor: pointer;" @click="homePage()">@我的网站</span>
    </div>
  </div>

</template>

<script>
export default {
  data () {
    return {
      path: ''
    }
  },
  mounted () {
    this.path = this.$router.currentRoute.path
  },
  methods: {
    homePage () {
      this.$router.push({path: '/index'})
    }
  }
}
</script>
