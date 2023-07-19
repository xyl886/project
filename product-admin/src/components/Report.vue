<template>
  <el-dialog
    title="我要举报"
    :visible.sync="centerDialogVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    ref="tk"
    :show-close="false"
    class="reportDialog"
  >
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="100px"
      class="demo-ruleForm"
      @submit.native.prevent
      style="height: auto"
    >
      <el-form-item prop="ViolateCompassesType">
        <el-radio-group v-model="ruleForm.ViolateCompassesType">
          <el-radio label="违禁商品(出售平台禁止售卖的商品)"></el-radio>
          <el-radio label="欺诈骗钱(在平台上被骗钱财或物品)"></el-radio>
          <el-radio label="假货(假冒他人品牌的商品)"></el-radio>
          <el-radio label="滥发信息(发布的商品存在夸大、引流等情况)"></el-radio>
          <el-radio label="盗版(课程、书籍、软件等盗版商品)"></el-radio>
          <el-radio label="破坏清朗环境(扰乱网络传播秩序等情况)"></el-radio>
          <el-radio label="其他(不属于以上情况)"></el-radio>
          <el-input
            v-model="illegalContent"
            v-if="ruleForm.ViolateCompassesType === '其他(不属于以上情况)'"
            maxlength="30"
          ></el-input>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">

      <el-button type="primary" @click="submitUpload('ruleForm')"
      >确 定</el-button
      >
      <el-button @click="back">取 消</el-button>
    </span>
  </el-dialog>
</template>

<script>

import {addReport} from '../api/report'

export default {
  name: 'Report',
  data () {
    return {
      centerDialogVisible: false,
      ruleForm: {
        ViolateCompassesType: ''
      },
      rules: {
        ViolateCompassesType: [
          { required: true, message: '请选择违规类型', trigger: 'change' }
        ]
      },
      illegalContent: '',
      time: true
    }
  },
  methods: {
    back () {
      this.centerDialogVisible = false
      this.$refs.ruleForm.resetFields()
      this.$store.state.display = this.centerDialogVisible
    },
    showDialog () {
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.$refs.ruleForm.resetFields()
      })
    },
    submitUpload (formName) {
      this.$refs[formName].validate((valid) => {
        if (
          this.ruleForm.ViolateCompassesType === '其他(不属于以上情况)' &&
          this.illegalContent === ''
        ) {
          this.$message({
            showClose: true,
            message: '请填写违规类型',
            type: 'error'
          })
          return false
        } else if (!valid) {
          return false
        }
        if (!this.time) {
          this.$message({
            showClose: true,
            message: '请勿频繁举报,30秒后可再次举报',
            type: 'error'
          })
          return false
        }
        let content = ''
        content = this.ruleForm.ViolateCompassesType
        if (this.ruleForm.ViolateCompassesType === '其他(不属于以上情况)') {
          content = this.illegalContent
        }
        var ReportingInformation = this.$store.state.ReportingInformation

        let form = {
          informerId: ReportingInformation.informerId,
          beAPersonId: ReportingInformation.beApersonId,
          postId: ReportingInformation.postid,
          goodsId: ReportingInformation.goodsid,
          commentId: ReportingInformation.commentid,
          replyId: ReportingInformation.replyid,
          reportContent: content,
          id: null,
          reportTime: null,
          reportNumber: null,
          state: 1
        }
        addReport(form).then((res) => {
          // if (res.status === 200) {
          //   this.$message({
          //     message: res.data.data === true ? '举报成功,耐心等待管理员审核' : res.data.data,
          //     type: 'success'
          //   })
          //   this.back()
          //   this.$refs[formName].resetFields()
          //   this.illegalContent = ''
          // if (res.data > 5) {
          //   this.time = false;
          //   let timer = setTimeout(() => {
          //     this.time = true;
          //     clearInterval(timer);
          //   }, 30000);
          // }
          // }
        })
      })
    }
  },
  computed: {
    display () {
      return this.$store.state.display
    }
  },
  watch: {
    display (newValue, oldValue) {
      this.centerDialogVisible = newValue
    }
  },
  mounted () {
    this.$refs.tk.$el.firstChild.style.height = 'auto'
  }
}
</script>

<style scoped>
@media screen and (min-width: 1500px)  {
  ::v-deep .el-dialog{
    width: 30% !important;
    height: auto !important;
  }
  ::v-deep .el-form-item__content{
    //margin-left: 100px !important;
  }
}
@media screen and (min-width: 1100px) and (max-width:1500px)  {
  ::v-deep .el-dialog{
    width: 37% !important;
    height: auto !important;
  }
  ::v-deep .el-form-item__content{
    margin-left: 80px !important;
  }
}
@media screen and (min-width: 800px) and (max-width: 1100px) {
  ::v-deep .el-dialog{
    width: 50% !important;
    height: auto !important;
  }
  ::v-deep .el-form-item__content{
    margin-left: 50px !important;
  }
}
@media screen and (min-width: 500px) and (max-width: 800px) {
  ::v-deep .el-dialog{
    width: 62% !important;
    height: auto !important;
  }
  ::v-deep .el-form-item__content{
    margin-left: 0 !important;
  }
}
@media screen and (max-width: 500px) {
  ::v-deep .el-dialog{
    width: 95% !important;
    height: auto !important;
  }
  ::v-deep .el-form-item__content{
    margin-left: 0 !important;
  }

}

.el-radio{
  height: 20px !important;
  margin-bottom: 20px !important;
}
</style>
