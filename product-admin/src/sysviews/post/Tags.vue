<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0;">
      <el-form v-show="showSearch" :inline="true">
        <el-form-item label="标签名">
          <el-input
            v-model="page.tagName"
            style="width: 130px"
            size="small"
            filterable
            clearable
            reserve-keyword
            placeholder="请选择标签"
            @change="handleFind"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="query">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleCreate">新增</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            :disabled="!multipleSelection.length"
            type="danger"
            icon="el-icon-delete"
            size="small"
            @click="handleDelete">批量删除</el-button>
        </el-col>
        <RightToolbar :show-search.sync="showSearch" @queryTable="query"/>
      </el-row>
    </el-col>
    <!--列表-->
    <template>
      <el-table
        :data="tableData"
        :style="{width: '100%'}"
        border
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="100"/>
        <el-table-column
          label="标签名"
          prop="tagName"
          width="200">
          <template scope="scope">
            <el-tag type="info">{{ scope.row.tagName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="状态"
          prop="deleted"
          width="200"/>
        <el-table-column
          label="创建时间"
          prop="createTime"
          sortable
          width="250"/>
        <el-table-column
          label="更新时间"
          prop="updateTime"
          sortable
          width="250"/>
        <el-table-column
          label="操作"
          fixed="right"
          header-align="center"
          align="center"
          width="250">
          <template scope="scope">
            <el-button size="small" @click="handleEdit(scope)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDel(scope)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--    编辑表单-->
      <el-dialog :visible.sync="dialogFormVisible" title="编辑">
        <el-form :model="editForm">
          <el-form-item :label-width="'75px'" label="标签名">
            <el-input v-model="editForm.tagName"/>
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
import { list, remove, update, deleteBatch, add } from '../../api/tag'

export default {
  data() {
    return {
      showSearch: true,
      tableData: [],
      editForm: [],
      multipleSelection: [],
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        tagName: null
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
      if (this.editForm.id) {
        const Form = {
          id: this.editForm.id,
          tagName: this.editForm.tagName
        }
        update(Form).then(res => {
          if (res.code === 200) {
            this.editForm = []
            this.$message.success(res.msg)
            this.dialogFormVisible = false
            this.query()
          }
        }).catch((e) => {
          this.$message.error(e)
        })
      } else {
        const Form = {
          tagName: this.editForm.tagName
        }
        add(Form).then(res => {
          if (res.code === 200) {
            this.editForm = []
            this.$message.success(res.msg)
            this.dialogFormVisible = false
            this.query()
          }
        }).catch((e) => {
          this.$message.error(e)
        })
      }
      this.query()
    },
    // 删除
    handleDel(row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        const id = row.row.id
        remove(id).then(res => {
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
