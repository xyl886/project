<template>
  <div style="width: 100%;height: 100%;background: #f5f7f9;">
    <top></top>
    <router-view style="min-height: calc(100% - 120px)"></router-view>
    <foot></foot>
  </div>
</template>

<script>
import top from '../top/index.vue'
import foot from '../foot/index.vue'
import BackToTop from '../top/BackToTop.vue'

export default {
  components: {
    BackToTop,
    top,
    foot
  },
  name: 'index',
  data () {
    return {

    }
  },
  mounted () {
  },
  methods: {
  }
}
</script>

<style>
</style>
