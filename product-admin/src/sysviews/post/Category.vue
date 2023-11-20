<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0;">
      <el-form v-show="showSearch" :inline="true" :model="page" label-width="68px">
        <el-form-item label="分类名">
          <el-select
            v-model="page.categoryName"
            style="width: 130px"
            size="small"
            filterable
            clearable
            reserve-keyword
            placeholder="请选择分类"
            @change="handleFind">
            <el-option v-for="item in category" :key="item.id" :label="item.categoryName" :value="item.categoryName" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
          <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
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
        <RightToolbar :show-search.sync="showSearch" @queryTable="fetchCategory"/>
      </el-row>
    </el-col>
    <!--    编辑表单-->
    <el-dialog :visible.sync="dialogFormVisible" title="编辑">
      <el-form :model="editForm">
        <el-form-item :label-width="'75px'" label="序号">
          <el-input v-model="editForm.id" disabled autocomplete="off"/>
        </el-form-item>
        <el-form-item :label-width="'75px'" label="分类名字">
          <el-input v-model="editForm.categoryName"/>
        </el-form-item>
        <el-form-item :label-width="'75px'" label="图标">
          <el-input v-model="editForm.icon" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="edit">确 定</el-button>
      </div>
    </el-dialog>
    <!--列表-->
    <el-table
      :data="tableData"
      :style="{width: '100%'}"
      :default-sort = "{prop: 'id', order: 'descending'}"
      max-height="490"
      border
      @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55"/>
      <el-table-column
        label="序号"
        prop="id"
        sortable
        width="180"/>
      <el-table-column
        label="分类名字"
        prop="categoryName"
        width="180">
        <template scope="scope">
          <el-tag type="primary">{{ scope.row.categoryName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="图标"
        prop="icon"
        width="180"/>
      <el-table-column
        label="帖子数"
        prop="postCount"
        sortable
        width="120"/>
      <el-table-column
        label="状态"
        prop="deleted"
        width="180"/>
      <el-table-column
        label="创建时间"
        prop="createTime"
        sortable
        width="180"/>
      <el-table-column
        label="更新时间"
        prop="updateTime"
        sortable
        width="180"/>
      <el-table-column
        label="操作"
        fixed="right"
        header-align="center"
        align="center"
        width="150">
        <template scope="scope">
          <el-button slot="reference" size="small" @click="handleEdit(scope)">编辑</el-button>
          <el-button :disabled="scope.row.postCount>0" type="danger" size="small" @click="handleDel(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
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
  </section>
</template>

<script>

import { listAllCategory } from '../../api/posts'
import { delCategory, deleteBatch, updateCategory } from '../../api/category'

export default {
  data() {
    return {
      showSearch: true,
      multipleSelection: [],
      loading: false,
      visible: false,
      dialogFormVisible: false,
      category: [],
      tableData: [{
        id: '',
        categoryName: '',
        icon: '',
        deleted: '',
        createTime: '',
        updateTime: ''
      }],
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        categoryName: null
      },
      // 编辑界面数据
      editForm: {}
    }
  },
  mounted() {
  },
  created() {
    this.fetchCategory()
  },
  methods: {
    handleCreate() {
      this.dialogFormVisible = true
      this.editForm = {
        id: '',
        categoryName: '',
        icon: ''
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
      const Form = {
        id: this.editForm.id,
        categoryName: this.editForm.categoryName,
        icon: this.editForm.icon
      }
      updateCategory(Form).then(res => {
        if (res.code === 200) {
          this.editForm = []
          this.$message.success(res.msg)
          this.dialogFormVisible = false
          this.fetchCategory()
        }
      }).catch((e) => {
        this.$message.error(e)
      })
    },
    resetQuery() {
      this.page.categoryName = null
      this.fetchCategory()
    },
    handleFind() {
      this.page.currentPage = 1
      this.tableData = []
      this.fetchCategory()
    },
    sizeChange(pageSize) { // 页数
      this.page.pageSize = pageSize
      this.fetchCategory()
    },
    handleCurrentChange(currentPage) {
      this.page.currentPage = currentPage
      this.fetchCategory()
    },
    fetchCategory() {
      this.loading = true
      listAllCategory(this.page).then(res => {
        this.tableData = res.data
        this.category = res.data
        this.page.total = res.dataTotal
        this.loading = false
      })
    },
    // 删除
    handleDel(row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        const id = row.row.id
        console.log(id)
        delCategory(id).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
          }
          this.fetchCategory()
        }).catch((e) => {
          this.$message.error(e)
        })
      })
    },
    handleDelete() {
      console.log(this.multipleSelection)
      if (!this.multipleSelection.length) {
        this.$message.error('请选择要删除的分类')
        return false
      }
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatch(this.multipleSelection).then(res => {
          this.$message.success('批量删除分类成功')
          this.fetchCategory()
        }).catch(err => {
          console.log(err)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消删除'
        })
      })
    },
    handleSelectionChange: function(val) {
      this.multipleSelection = val
    }
  }
}

</script>

<style scoped>

</style>
