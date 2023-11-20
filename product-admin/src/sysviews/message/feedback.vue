<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0;">
      <el-form :inline="true">
        <el-form-item>
          <el-input
            v-model="page.content"
            style="width: 160px"
            size="small"
            filterable
            clearable
            reserve-keyword
            placeholder="请输入角色名称"
            @change="handleFind"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="query">查询</el-button>
        </el-form-item>
        <!--        <el-form-item>-->
        <!--          <el-button type="primary" icon="el-icon-search" size="small" @click="handleCreate">新增</el-button>-->
        <!--        </el-form-item>-->
        <el-form-item>
          <el-button
            :disabled="!multipleSelection.length"
            type="danger"
            icon="el-icon-delete"
            size="small"
            @click="handleDelete">批量删除</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!--列表-->
    <template>
      <el-table
        v-loading="loading"
        :data="tableData"
        :style="{width: '100%'}"
        stripe
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="100"/>
        <el-table-column
          label="头像"
          prop="avatar"
          width="140">
          <template slot-scope="scope">
            <img :src="scope.row.avatar" style=" width: 60px; height: 60px;border-radius:10px;" >
          </template>
        </el-table-column>
        <el-table-column
          label="邮箱"
          prop="email"
          width="200"/>
        <el-table-column
          label="昵称"
          prop="nickname"
          width="200"/>
        <el-table-column
          label="帖子标题"
          prop="title"
          width="200"/>
        <el-table-column
          label="内容"
          prop="content"
          width="200"/>
        <el-table-column
          label="提交时间"
          prop="createTime"
          width="150"/>
        <el-table-column
          label="操作"
          fixed="right"
          header-align="center"
          align="center"
          width="250">
          <template scope="scope">
            <!--            <el-button size="small" @click="handleEdit(scope)">编辑</el-button>-->
            <el-button type="danger" size="small" @click="handleDel(scope)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--    编辑表单-->
      <el-dialog :visible.sync="dialogFormVisible" title="编辑">
        <el-form :model="editForm">
          <el-form-item :label-width="'75px'" label="用户角色">
            <el-radio-group v-model="editForm.role">
              <el-radio-button v-for="(option, index) in role" :key="index" :label="option.id">{{ option.roleName }}</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item :label-width="'75px'" label="状态">
            <el-radio-group v-model="editForm.status">
              <el-radio-button v-for="(option, index) in status" :key="index" :label="option.id" type="info">{{ option.userStatus }}</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="edit">确 定</el-button>
        </div>
      </el-dialog>
      <el-col :span="24" class="toolbar">
        <div style="text-align: center;margin-top: 10px;">
          <el-pagination
            :current-page.sync="page.currentPage"
            :page-sizes="[10, 20, 40, 80]"
            :page-size="page.pageSize"
            :total="page.total"
            background
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="sizeChange"
            @current-change="handleCurrentChange"/>
        </div>
      </el-col>
    </template>
  </section>
</template>

<script>
import { deleteBatch, list, remove } from '../../api/report'

export default {
  data() {
    return {
      status: [
        { id: 0, userStatus: '正常' },
        { id: 1, userStatus: '禁用' }
      ],
      role: [],
      tableData: [],
      editForm: [],
      multipleSelection: [],
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        username: null,
        roleId: null
      },
      dialogFormVisible: false,
      loading: false
    }
  },
  mounted() {
  },
  created() {
    this.query()
  },
  methods: {
    sizeChange(pageSize) { // 页数
      this.page.pageSize = pageSize
      this.query()
    },
    handleCurrentChange(currentPage) { // 当前页
      this.page.currentPage = currentPage
      this.query()
    },
    handleFind() {
      this.page.currentPage = 1
      this.tableData = []
      this.query()
    },
    query() {
      this.loading = true
      list(this.page).then(res => {
        if (res.code === 200) {
          this.tableData = res.data
          this.page.total = res.dataTotal
          this.loading = false
        }
      })
    },
    handleCreate() {
      this.dialogFormVisible = true
      this.editForm = {
        tagName: ''
      }
    },
    // 显示编辑界面
    handleEdit(row) {
      this.dialogFormVisible = true
      console.log(row)
      this.editForm = Object.assign({}, row.row)
      console.log(this.editForm)
    },
    // 提交
    edit() {
      this.dialogFormVisible = true
      this.$confirm('确认禁用该用户吗?', '提示', {
        type: 'warning'
      }).then(() => {
        const Form = {
          id: this.editForm.id,
          role: this.editForm.role,
          status: this.editForm.status
        }
        sysUpdate(Form).then(res => {
          if (res.code === 200) {
            this.editForm = []
            this.$message.success(res.msg)
            this.dialogFormVisible = false
            this.query()
          }
        }).catch((e) => {
          this.$message.error(e)
        })
      })

      this.query()
    },
    // 删除
    handleDel(row) {
      this.$confirm('确认禁用该用户吗?', '提示', {
        type: 'warning'
      }).then(() => {
        remove(row.row.id).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
          }
          this.query()
        }).catch((e) => {
          this.$message.error(e)
        })
      })
    },
    // 批量删除
    handleSelectionChange: function(val) {
      this.multipleSelection = val
    },
    handleDelete() {
      if (!this.multipleSelection.length) {
        this.$message.error('请选择要删除的标签')
        return false
      }
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = []
        this.multipleSelection.forEach(item => {
          ids.push(item.id)
        })
        deleteBatch(ids).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.query()
          }
        }).catch(err => {
          console.log(err)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消删除'
        })
      })
    }
  }
}

</script>

<style scoped>
</style>
