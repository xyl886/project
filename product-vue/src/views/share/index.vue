<template>
  <div class="body" v-infinite-scroll="loadFun">
    <div style="font-size: 14px;width: 1400px;margin: auto auto  150px auto;">
      <div class="share-tab" style=" display: inline-block;width: 800px;">
       <el-tabs style="max-width: 800px" v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="全部" name="first">
            <all ref="first"></all>
          </el-tab-pane>
          <el-tab-pane label="官塘校区" name="second">
            <all ref="second"></all>
          </el-tab-pane>
          <el-tab-pane label="社湾校区" name="third">
            <all ref="third"></all>
          </el-tab-pane>
          <el-tab-pane label="我的分享" name="fourth">
            <all ref="fourth"></all>
          </el-tab-pane>
          <el-tab-pane label="我的关注" name="fifth">
            <all ref="fifth"></all>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div class="search">
          <el-input  placeholder="请输入内容" v-model="searchText" class="input-with-select">
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
      </div>
    </div>
  </div>
</template>

<script>
import all from './all.vue'
export default {
  components: {
    all
  },
  data () {
    return {
      searchText: '',
      activeName: 'first',
      school: null
    }
  },
  mounted () {
    this.$refs[this.activeName].init(this.school)
  },
  methods: {
    handleClick (tab, event) {
      if (tab.index !== '0') {
        this.school = tab.index
      } else {
        this.school = null
      }
      this.$refs[tab.name].init(this.school)
      console.log(tab, event)
    },
    loadFun () {
      this.$refs[this.activeName].load()
    }
  }
}
</script>

<style>
  .body{
    margin: 0;
    padding: 20px 0 0 0;
    width: 100%;
    height: calc(100% - 80px);
    background-image: url("../../../public/img/background-detail.jpg");
    background-size: cover;
    background-attachment: fixed;
    overflow: scroll;
  }
  .share-tab .el-tabs__header{
    background-color: #ffffff!important;
    padding: 0 20px!important;
  }
  .share-tab .el-tabs__nav{
    height: 60px!important;
    line-height: 60px!important;
  }
</style>
<style scoped>
.search{
  display: inline-block;
  background-color: #ffffff!important;
  float: right;
  position: relative;
  width: 300px;
  height: 60px;
  top: 0;
  margin-right: 299.5px;
}
.input-with-select{
  width: 200px;
  height: 60px;
  margin-left: -110px;
  float: right;
  line-height: 60px;
  margin-top: auto;
}
</style>
