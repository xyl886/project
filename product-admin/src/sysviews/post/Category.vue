<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form v-show="showSearch" ref="form" :inline="true" :model="params" label-width="68px">
        <el-form-item label="文章名称">
          <el-input v-model="params.title" style="width: 150px" size="small" placeholder="请输入文章名称" />
        </el-form-item>
        <el-form-item label="标签名">
          <el-select
            v-model="params.tagId"
            style="width: 130px"
            size="small"
            filterable
            clearable
            reserve-keyword
            placeholder="请选择标签"
            @change="handleFind">
            <el-option v-for="item in tags" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类名">
          <el-select
            v-model="params.categoryId"
            style="width: 130px"
            size="small"
            filterable
            clearable
            reserve-keyword
            placeholder="请选择分类"
            @change="handleFind"
          >
            <el-option v-for="item in category" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布状态">
          <el-select
            v-model="params.isPublish"
            style="width: 130px"
            size="small"
            filterable
            clearable
            reserve-keyword
            placeholder="是否发布"
            @change="handleFind"
          >
            <el-option v-for="(item,index) in publishList" :key="index" :label="item" :value="index" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
          <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表-->
    <el-table v-loading="listLoading" :data="users" highlight-current-row style="width: 100%;" @selection-change="selsChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column type="index" width="60"/>
      <el-table-column prop="name" label="姓名" width="120" sortable/>
      <el-table-column :formatter="formatSex" prop="sex" label="性别" width="100" sortable/>
      <el-table-column prop="age" label="年龄" width="100" sortable/>
      <el-table-column prop="birth" label="生日" width="120" sortable/>
      <el-table-column prop="addr" label="地址" min-width="180" sortable/>
      <el-table-column label="操作" width="150">
        <template scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-pagination :page-size="20" :total="total" layout="prev, pager, next" style="float:right;" @current-change="handleCurrentChange"/>
    </el-col>

    <!--编辑界面-->
    <el-dialog v-model="editFormVisible" :close-on-click-modal="false" title="编辑">
      <el-form ref="editForm" :model="editForm" :rules="editFormRules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="editForm.sex">
            <el-radio :label="1" class="radio">男</el-radio>
            <el-radio :label="0" class="radio">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="editForm.age" :min="0" :max="200"/>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker v-model="editForm.birth" type="date" placeholder="选择日期"/>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="editForm.addr" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="editFormVisible = false">取消</el-button>
        <el-button :loading="editLoading" type="primary" @click.native="editSubmit">提交</el-button>
      </div>
    </el-dialog>

    <!--新增界面-->
    <el-dialog v-model="addFormVisible" :close-on-click-modal="false" title="新增">
      <el-form ref="addForm" :model="addForm" :rules="addFormRules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="addForm.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="addForm.sex">
            <el-radio :label="1" class="radio">男</el-radio>
            <el-radio :label="0" class="radio">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="addForm.age" :min="0" :max="200"/>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker v-model="addForm.birth" type="date" placeholder="选择日期"/>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="addForm.addr" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false">取消</el-button>
        <el-button :loading="addLoading" type="primary" @click.native="addSubmit">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>

export default {
  data() {
    return {
      filters: {
        name: ''
      },
      users: [],
      total: 0,
      page: 1,
      listLoading: false,
      sels: [], // 列表选中列

      editFormVisible: false, // 编辑界面是否显示
      editLoading: false,
      editFormRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ]
      },
      // 编辑界面数据
      editForm: {
        id: 0,
        name: '',
        sex: -1,
        age: 0,
        birth: '',
        addr: ''
      },

      addFormVisible: false, // 新增界面是否显示
      addLoading: false,
      addFormRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ]
      },
      // 新增界面数据
      addForm: {
        name: '',
        sex: -1,
        age: 0,
        birth: '',
        addr: ''
      }

    }
  },
  mounted() {
    this.getUsers()
  },
  methods: {
    // 性别显示转换
    formatSex: function(row, column) {
      return row.sex == 1 ? '男' : row.sex == 0 ? '女' : '未知'
    },
    handleCurrentChange(val) {
      this.page = val
      this.getUsers()
    },
    // 获取用户列表
    getUsers() {
      const para = {
        page: this.page,
        name: this.filters.name
      }
      this.listLoading = true
      // NProgress.start();
      getUserListPage(para).then((res) => {
        this.total = res.data.total
        this.users = res.data.users
        this.listLoading = false
        // NProgress.done();
      })
    },
    // 删除
    handleDel: function(index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        // NProgress.start();
        const para = { id: row.id }
        removeUser(para).then((res) => {
          this.listLoading = false
          // NProgress.done();
          this.$message({
            message: '删除成功',
            type: 'success'
          })
          this.getUsers()
        })
      }).catch(() => {

      })
    },
    // 显示编辑界面
    handleEdit: function(index, row) {
      this.editFormVisible = true
      this.editForm = Object.assign({}, row)
    },
    // 显示新增界面
    handleAdd: function() {
      this.addFormVisible = true
      this.addForm = {
        name: '',
        sex: -1,
        age: 0,
        birth: '',
        addr: ''
      }
    },
    // 编辑
    editSubmit: function() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = true
            // NProgress.start();
            const para = Object.assign({}, this.editForm)
            para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd')
            // editUser(para).then((res) => {
            // 	this.editLoading = false;
            // 	//NProgress.done();
            // 	this.$message({
            // 		message: '提交成功',
            // 		type: 'success'
            // 	});
            // 	this.$refs['editForm'].resetFields();
            // 	this.editFormVisible = false;
            // 	this.getUsers();
            // });
          })
        }
      })
    },
    // 新增
    addSubmit: function() {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.addLoading = true
            // NProgress.start();
            const para = Object.assign({}, this.addForm)
            para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd')
            // addUser(para).then((res) => {
            // 	this.addLoading = false;
            // 	//NProgress.done();
            // 	this.$message({
            // 		message: '提交成功',
            // 		type: 'success'
            // 	});
            // 	this.$refs['addForm'].resetFields();
            // 	this.addFormVisible = false;
            // 	this.getUsers();
            // });
          })
        }
      })
    },
    selsChange: function(sels) {
      this.sels = sels
    },
    // 批量删除
    batchRemove: function() {
      var ids = this.sels.map(item => item.id).toString()
      this.$confirm('确认删除选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        // NProgress.start();
        const para = { ids: ids }
        // batchRemoveUser(para).then((res) => {
        // 	this.listLoading = false;
        // 	//NProgress.done();
        // 	this.$message({
        // 		message: '删除成功',
        // 		type: 'success'
        // 	});
        // 	this.getUsers();
        // });
      }).catch(() => {

      })
    }
  }
}

</script>

<style scoped>

</style>
