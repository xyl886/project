<template>
  <div style="font-size: 14px;">
    <div style="height: 40px;text-align: center;line-height: 40px;">
      Powered by
      <span style="color: #409EFF;cursor: pointer;" @click="homePage()">@我的网站</span>
    </div>
<!--    <div v-else style="background-color:#CDE6FA;height: 70px;text-align: center;line-height: 70px;">-->
<!--    </div>-->
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
