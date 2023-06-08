<template>
  <div class="custom-emoji-com" @click.stop="()=>{}">
    <el-tabs :tab-position="tabPosition" style="bottom:0;" type="border-card" class="wrapper">
      <el-tab-pane v-for="(value, key, index) in emojisList" :label="key" :key="index" addable>
        <span slot="label"><i :class="emojiIconClassNameMap[key]"></i> </span>
        <span
          class="emoji-item"
          v-for="(item, index) in value"
          :key="index"
          @click.stop="selectItem(item)">
          {{item}}
        </span>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import emojis from './emoji.json'

export default {
  props: {
    tabPosition: {
      type: String,
      default () {
        return 'bottom'
      }
    }
  },
  data () {
    return {
      emojisList: emojis.data,
      selectedEmoji: '',
      emojiIconClassNameMap: {
        face: 'iconfont icon-tag-faces',
        heart: 'iconfont icon-Heart',
        food: 'iconfont icon-food',
        drink: 'iconfont icon-Colddrinkteacoffeeb',
        animal: 'iconfont icon-icon-test'
      }
    }
  },
  computed: {
  },
  methods: {
    selectItem (item) {
      this.$emit('addemoji', item)
    }
  }
}
</script>

<style lang="scss">
@import '../../../static/iconfont/iconfont.css';
  .custom-emoji-com {
    width: 395px;

    .wrapper {
      transition: all .5s ease;

      .emoji-item {
        cursor: pointer;
        display: inline-block;
        width: 27px;
        height: 27px;
        padding: 3px;
      }
    }
  }
</style>
