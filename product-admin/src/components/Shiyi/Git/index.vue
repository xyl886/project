<template>
  <div>
    <svg-icon class="giteeSvg" icon-class="giteeSvg" style="height: 2.5em;font-size: 18px;cursor: pointer" @click.native="goto" />
  </div>
</template>

<script>
export default {
  name: 'ShiYiGit',
  data() {
    return {
      url: 'https://gitee.com/quequnlong/shiyi-blog'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
<style scoped>

</style>
