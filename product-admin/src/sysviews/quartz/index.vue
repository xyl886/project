<!--<template>-->
<!--  <div class="app-container">-->
<!--    <el-form v-show="showSearch" ref="queryForm" :model="params" :inline="true" label-width="68px">-->
<!--      <el-form-item label="任务名称" prop="jobName">-->
<!--        <el-input-->
<!--          v-model="params.jobName"-->
<!--          placeholder="请输入任务名称"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleFind"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="任务组名" prop="jobGroup">-->
<!--        <el-select v-model="params.jobGroup" placeholder="请选择任务组名" clearable size="small" @change="handleFind">-->
<!--          <el-option-->
<!--            v-for="dict in jobDictList"-->
<!--            :key="dict.value"-->
<!--            :label="dict.label"-->
<!--            :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="任务状态" prop="status">-->
<!--        <el-select v-model="params.status" placeholder="请选择任务状态" clearable size="small" @change="handleFind">-->
<!--          <el-option-->
<!--            v-for="dict in jobStatusList"-->
<!--            :key="dict.value"-->
<!--            :label="dict.label"-->
<!--            :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleFind">搜索</el-button>-->
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          v-if="canAdd"-->
<!--          type="primary"-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleCreate"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="info"-->
<!--          icon="el-icon-s-operation"-->
<!--          size="mini"-->
<!--          @click="handleJobLog"-->
<!--        >日志</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          v-if="canDeleteBatch"-->
<!--          :disabled="!multipleSelection.length"-->
<!--          type="danger"-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          @click="handleDelete"-->
<!--        >批量删除</el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :show-search.sync="showSearch" @queryTable="fetchJobs" />-->
<!--    </el-row>-->

<!--    <div style="margin-top: 5px">-->
<!--      <el-table border style="width: 100%" :data="tableData" @selection-change="handleSelectionChange">-->
<!--        <el-table-column type="selection" width="55" align="center" />-->
<!--        <el-table-column label="任务名称" width="160" align="center" prop="jobName" :show-overflow-tooltip="true" />-->
<!--        <el-table-column label="任务组名" align="center" prop="jobGroup">-->
<!--          <template slot-scope="scope">-->
<!--            <el-tag v-for="dict in jobDictList" v-if="dict.value === scope.row.jobGroup" :type="dict.style">-->
<!--              {{ dict.label }}-->
<!--            </el-tag>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column label="调用目标字符串" align="center" prop="invokeTarget" :show-overflow-tooltip="true" />-->
<!--        <el-table-column label="cron执行表达式" align="center" prop="cronExpression" :show-overflow-tooltip="true" />-->
<!--        <el-table-column label="状态" align="center">-->
<!--          <template slot-scope="scope">-->
<!--            <el-switch-->
<!--              v-model="scope.row.status"-->
<!--              active-value="0"-->
<!--              inactive-value="1"-->
<!--              @change="handleStatusChange(scope)"-->
<!--            />-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column label="创建者" prop="createBy" align="center" />-->
<!--        <el-table-column label="创建时间" align="center" width="160">-->
<!--          <template slot-scope="scope">-->
<!--            {{ dataFormat(scope.row.createTime) }}-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--          <template slot-scope="scope">-->
<!--            <el-button v-if="canUpdate" size="mini" type="text" @click="handleEdit(scope)">修改</el-button>-->
<!--            <el-button v-if="canDel" size="mini" type="text" @click="remove(scope)">删除</el-button>-->
<!--            <el-dropdown v-if="canUpdate" size="mini" @command="(command) => handleCommand(command, scope.row)">-->
<!--              <el-button-->
<!--                size="mini"-->
<!--                type="text"-->
<!--                style="margin-left: 9px"-->
<!--              >更多</el-button>-->
<!--              <el-dropdown-menu slot="dropdown">-->
<!--                <el-dropdown-item-->
<!--                  command="handleRun"-->
<!--                  icon="el-icon-caret-right"-->
<!--                >执行一次</el-dropdown-item>-->
<!--                <el-dropdown-item-->
<!--                  command="handleView"-->
<!--                  icon="el-icon-view"-->
<!--                >任务详细</el-dropdown-item>-->
<!--                <el-dropdown-item-->
<!--                  command="handleJobLog"-->
<!--                  icon="el-icon-s-operation"-->
<!--                >调度日志</el-dropdown-item>-->
<!--              </el-dropdown-menu>-->
<!--            </el-dropdown>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--      </el-table>-->
<!--    </div>-->

<!--    &lt;!&ndash;分页区域&ndash;&gt;-->
<!--    <div class="pagination-container" style="float: right;margin-bottom: 1.25rem;margin-top: 1.25rem;">-->
<!--      <el-pagination-->
<!--        background-->
<!--        :current-page="params.pageNo"-->
<!--        :page-size="params.pageSize"-->
<!--        :page-sizes="[10, 20, 30]"-->
<!--        layout="total, sizes,prev, pager, next,jumper"-->
<!--        :total="total"-->
<!--        @size-change="handleSizeChange"-->
<!--        @current-change="handleCurrentChange"-->
<!--      />-->
<!--    </div>-->

<!--    &lt;!&ndash; 添加或修改定时任务对话框 &ndash;&gt;-->
<!--    <el-dialog :title="title" :visible.sync="dialogFormVisible" width="800px" append-to-body>-->
<!--      <el-form ref="dataForm" :model="job" :rules="rules" label-width="120px">-->
<!--        <el-row>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="任务名称" prop="jobName">-->
<!--              <el-input v-model="job.jobName" placeholder="请输入任务名称" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="任务分组" prop="jobGroup">-->
<!--              <el-select v-model="job.jobGroup" placeholder="请选择">-->
<!--                <el-option-->
<!--                  v-for="dict in jobDictList"-->
<!--                  :key="dict.value"-->
<!--                  :label="dict.label"-->
<!--                  :value="dict.value"-->
<!--                />-->
<!--              </el-select>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="24">-->
<!--            <el-form-item prop="invokeTarget">-->
<!--              <span slot="label">-->
<!--                调用方法-->
<!--                <el-tooltip placement="top">-->
<!--                  <div slot="content">-->
<!--                    Bean调用示例：blogQuartz.blogParams('blog')-->
<!--                    <br>Class类调用示例：com.shiyi.quartz.BlogQuartz.blogParams('blog')-->
<!--                    <br>参数说明：支持字符串，布尔类型，长整型，浮点型，整型-->
<!--                  </div>-->
<!--                  <i class="el-icon-question" />-->
<!--                </el-tooltip>-->
<!--              </span>-->
<!--              <el-input v-model="job.invokeTarget" placeholder="请输入调用目标字符串" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="24">-->
<!--            <el-form-item label="cron表达式" prop="cronExpression">-->
<!--              <el-input v-model="job.cronExpression" placeholder="请输入cron执行表达式">-->
<!--                <template slot="append">-->
<!--                  <el-button type="primary" @click="handleShowCron">-->
<!--                    生成表达式-->
<!--                    <i class="el-icon-time el-icon&#45;&#45;right" />-->
<!--                  </el-button>-->
<!--                </template>-->
<!--              </el-input>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="24">-->
<!--            <el-form-item label="错误策略" prop="misfirePolicy">-->
<!--              <el-radio-group v-model="job.misfirePolicy" size="small">-->
<!--                <el-radio-button v-for="item in jobMisfireList" :key="item.value" :label="item.value">{{ item.label }}</el-radio-button>-->
<!--              </el-radio-group>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="是否并发" prop="concurrent">-->
<!--              <el-radio-group v-model="job.concurrent" size="small">-->
<!--                <el-radio-button label="0">允许</el-radio-button>-->
<!--                <el-radio-button label="1">禁止</el-radio-button>-->
<!--              </el-radio-group>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="状态">-->
<!--              <el-radio-group v-model="job.status">-->
<!--                <el-radio-->
<!--                  v-for="dict in jobStatusList"-->
<!--                  :key="dict.value"-->
<!--                  :label="dict.value"-->
<!--                >{{ dict.label }}</el-radio>-->
<!--              </el-radio-group>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogFormVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="submit">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

<!--    <el-dialog title="Cron表达式生成器" :visible.sync="openCron" append-to-body destroy-on-close class="scrollbar">-->
<!--      <crontab :expression="expression" @hide="openCron=false" @fill="crontabFill" />-->
<!--    </el-dialog>-->

<!--    &lt;!&ndash; 任务日志详细 &ndash;&gt;-->
<!--    <el-dialog title="任务详细" :visible.sync="openView" width="700px" append-to-body>-->
<!--      <el-form ref="form" :model="job" label-width="120px" size="mini">-->
<!--        <el-row>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="任务编号：">{{ job.jobId }}</el-form-item>-->
<!--            <el-form-item label="任务名称：">{{ job.jobName }}</el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item v-for="item in jobDictList" v-if="item.value === job.jobGroup" label="任务分组：">-->
<!--              {{ item.label }}-->
<!--            </el-form-item>-->
<!--            <el-form-item label="创建时间：">{{ dataFormat(job.createTime) }}</el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="cron表达式：">{{ job.cronExpression }}</el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="下次执行时间：">{{ dataFormat(job.nextValidTime) }}</el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="24">-->
<!--            <el-form-item label="调用目标方法：">{{ job.invokeTarget }}</el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="任务状态：">-->
<!--              <div v-for="item in jobStatusList" v-if="job.status === item.value">{{ item.label }}</div>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="是否并发：">-->
<!--              <div v-if="job.concurrent === '0'">允许</div>-->
<!--              <div v-else-if="job.concurrent === '1'">禁止</div>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <el-form-item label="执行策略：">-->
<!--              <div v-for="item in jobMisfireList" v-if="job.misfirePolicy === item.value">-->
<!--                {{ item.label }}-->
<!--              </div>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="openView = false">关 闭</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
<!--  </div>-->

<!--</template>-->
<!--<script>-->
<!--import { fetchList, add, update, info, remove, change, run, deleteBatch } from '@/api/quartz'-->
<!--import { getDataByDictType } from '@/api/dictData'-->
<!--import Crontab from '@/components/Crontab'-->
<!--import { parseTime } from '@/utils'-->
<!--import { hasAuth } from '@/utils/auth'-->
<!--import { mapGetters } from 'vuex'-->
<!--export default {-->
<!--  components: { Crontab },-->
<!--  data() {-->
<!--    return {-->
<!--      // 加载层信息-->
<!--      loading: [],-->
<!--      tableData: [],-->
<!--      dialogFormVisible: false,-->
<!--      openView: false,-->
<!--      showSearch: true,-->
<!--      jobDictList: [],-->
<!--      jobStatusList: [],-->
<!--      jobMisfireList: [],-->
<!--      jobDictDefaultValue: null,-->
<!--      jobStatusDefaultValue: null,-->
<!--      jobMisfireDefaultValue: null,-->
<!--      isEditForm: 0,-->
<!--      // 是否显示Cron表达式弹出层-->
<!--      openCron: false,-->
<!--      // 传入的表达式-->
<!--      expression: '',-->
<!--      total: null,-->
<!--      multipleSelection: [],-->
<!--      title: null,-->
<!--      job: {},-->
<!--      params: {-->
<!--        jobName: null,-->
<!--        jobGroup: null,-->
<!--        status: null,-->
<!--        pageNo: 1,-->
<!--        pageSize: 10-->
<!--      },-->
<!--      rules:-->
<!--        {-->
<!--          jobName: [-->
<!--            { required: true, message: '任务名称不能为空', trigger: 'blur' }-->
<!--          ],-->
<!--          invokeTarget: [-->
<!--            { required: true, message: '调用目标字符串不能为空', trigger: 'blur' }-->
<!--          ],-->
<!--          cronExpression: [-->
<!--            { required: true, message: 'cron执行表达式不能为空', trigger: 'blur' }-->
<!--          ]-->
<!--        }-->
<!--    }-->
<!--  },-->
<!--  created() {-->
<!--    this.openLoading()-->
<!--    this.getDictList()-->
<!--    this.fetchJobs()-->
<!--  },-->
<!--  computed: {-->
<!--    ...mapGetters([-->
<!--      'pres'-->
<!--    ]),-->
<!--    canAdd: function() {-->
<!--      return hasAuth(this.pres, '/system/job/add')-->
<!--    },-->
<!--    canDel: function() {-->
<!--      return hasAuth(this.pres, '/system/job/delete')-->
<!--    },-->
<!--    canDeleteBatch: function() {-->
<!--      return hasAuth(this.pres, '/system/job/deleteBatch')-->
<!--    },-->
<!--    canUpdate: function() {-->
<!--      return hasAuth(this.pres, '/system/job/update')-->
<!--    }-->
<!--  },-->
<!--  methods: {-->
<!--    getDictList: function() {-->
<!--      const dictTypeList = ['sys_job_group', 'sys_job_status', 'sys_job_misfire']-->
<!--      getDataByDictType(dictTypeList).then(response => {-->
<!--        const dictMap = response.data-->
<!--        this.jobDictList = dictMap.sys_job_group.list-->
<!--        this.jobStatusList = dictMap.sys_job_status.list-->
<!--        this.jobMisfireList = dictMap.sys_job_misfire.list-->
<!--        this.jobDictDefaultValue = dictMap.sys_job_group.defaultValue-->
<!--        this.jobStatusDefaultValue = dictMap.sys_job_status.defaultValue-->
<!--        this.jobMisfireDefaultValue = dictMap.sys_job_misfire.defaultValue-->
<!--      })-->
<!--    },-->
<!--    fetchJobs: function() {-->
<!--      fetchList(this.params).then(res => {-->
<!--        this.tableData = res.data.records-->
<!--        this.total = res.data.total-->
<!--        this.loading.close()-->
<!--      }).catch(err => {-->
<!--        console.log(err)-->
<!--      })-->
<!--    },-->
<!--    submit: function() {-->
<!--      this.$refs['dataForm'].validate((valid) => {-->
<!--        if (valid) {-->
<!--          if (this.isEditForm) {-->
<!--            update(this.job).then(res => {-->
<!--              this.$message.success('OK')-->
<!--              this.fetchJobs()-->
<!--              this.closeDialogForm()-->
<!--            }).catch(err => {-->
<!--              console.error(err)-->
<!--            })-->
<!--          } else {-->
<!--            add(this.job).then(res => {-->
<!--              this.$message.success('OK')-->
<!--              this.fetchJobs()-->
<!--              this.closeDialogForm()-->
<!--            }).catch(err => {-->
<!--              console.error(err)-->
<!--            })-->
<!--          }-->
<!--        } else {-->
<!--          console.error('no submit')-->
<!--        }-->
<!--      })-->
<!--    },-->
<!--    remove: function(scope) {-->
<!--      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {-->
<!--        confirmButtonText: '确定',-->
<!--        cancelButtonText: '取消',-->
<!--        type: 'warning'-->
<!--      }).then(() => {-->
<!--        remove(scope.row.jobId).then(res => {-->
<!--          this.$message.success('OK')-->
<!--          this.fetchJobs()-->
<!--        }).catch(err => {-->
<!--          console.error(err)-->
<!--        })-->
<!--      }).catch(() => {-->
<!--        this.$message({-->
<!--          type: 'info',-->
<!--          message: '取消删除'-->
<!--        })-->
<!--      })-->
<!--    },-->
<!--    handleCreate: function() {-->
<!--      this.isEditForm = 0-->
<!--      this.job = this.getFormObject()-->
<!--      this.title = '新增任务'-->
<!--      this.dialogFormVisible = true-->
<!--      this.$nextTick(() => {-->
<!--        this.$refs['dataForm'].clearValidate()-->
<!--      })-->
<!--    },-->
<!--    handleEdit: function(scope) {-->
<!--      this.title = '修改任务'-->
<!--      info(scope.row.jobId).then(res => {-->
<!--        this.job = res.data-->
<!--        this.isEditForm = 1-->
<!--        this.dialogFormVisible = true-->
<!--        this.$nextTick(() => {-->
<!--          this.$refs['dataForm'].clearValidate()-->
<!--        })-->
<!--      })-->
<!--    },-->
<!--    getFormObject: function() {-->
<!--      return {-->
<!--        jobName: '',-->
<!--        jobGroup: this.jobDictDefaultValue,-->
<!--        invokeTarget: '',-->
<!--        cronExpression: '',-->
<!--        misfirePolicy: this.jobMisfireDefaultValue,-->
<!--        concurrent: 1,-->
<!--        status: this.jobStatusDefaultValue,-->
<!--        remark: this.jobStatusDefaultValue-->
<!--      }-->
<!--    },-->
<!--    /** cron表达式按钮操作 */-->
<!--    handleShowCron() {-->
<!--      this.expression = this.job.cronExpression-->
<!--      this.openCron = true-->
<!--    },-->
<!--    /** 确定后回传值 */-->
<!--    crontabFill(value) {-->
<!--      this.job.cronExpression = value-->
<!--    },-->
<!--    resetQuery: function() {-->
<!--      this.params.jobName = null-->
<!--      this.params.jobGroup = null-->
<!--      this.params.status = null-->
<!--      this.fetchJobs()-->
<!--    },-->
<!--    handleFind: function() {-->
<!--      this.params.pageNo = 1-->
<!--      this.fetchJobs()-->
<!--    },-->
<!--    // 更多操作触发-->
<!--    handleCommand(command, row) {-->
<!--      switch (command) {-->
<!--        case 'handleRun':-->
<!--          this.handleRun(row)-->
<!--          break-->
<!--        case 'handleView':-->
<!--          this.handleView(row)-->
<!--          break-->
<!--        case 'handleJobLog':-->
<!--          this.handleJobLog(row)-->
<!--          break-->
<!--        default:-->
<!--          break-->
<!--      }-->
<!--    },-->
<!--    /* 立即执行一次 */-->
<!--    handleRun: function(row) {-->
<!--      this.$confirm('确认要立即执行一次"' + row.jobName + '"任务吗？').then(function() {-->
<!--        return run(row)-->
<!--      }).then(() => {-->
<!--        this.$message.success('执行成功')-->
<!--      }).catch(err => {-->
<!--        this.$message.error(err)-->
<!--      })-->
<!--    },-->
<!--    /* 立即执行一次 */-->
<!--    handleView: function(row) {-->
<!--      info(row.jobId).then(response => {-->
<!--        this.job = response.data-->
<!--        this.openView = true-->
<!--      })-->
<!--    },-->
<!--    /** 任务日志列表查询 */-->
<!--    handleJobLog: function(row) {-->
<!--      const jobId = row.jobId || null-->
<!--      this.$router.push({ path: '/jobLog', query: { jobId: jobId }})-->
<!--    },-->
<!--    handleStatusChange: function(scope) {-->
<!--      change(scope.row).then(res => {-->
<!--        this.$message.success('OK')-->
<!--      }).catch(err => {-->
<!--        console.error(err)-->
<!--      })-->
<!--    },-->
<!--    handleDelete: function() {-->
<!--      if (!this.multipleSelection.length) {-->
<!--        this.$message.error('请选择要删除的标签')-->
<!--        return false-->
<!--      }-->
<!--      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {-->
<!--        confirmButtonText: '确定',-->
<!--        cancelButtonText: '取消',-->
<!--        type: 'warning'-->
<!--      }).then(() => {-->
<!--        deleteBatch(this.multipleSelection).then(res => {-->
<!--          this.$message.success('OK')-->
<!--          this.fetchJobs()-->
<!--        }).catch(err => {-->
<!--          console.error(err)-->
<!--        })-->
<!--      }).catch(() => {-->
<!--        this.$message({-->
<!--          type: 'info',-->
<!--          message: '取消删除'-->
<!--        })-->
<!--      })-->
<!--    },-->
<!--    closeDialogForm: function() {-->
<!--      this.tag = {}-->
<!--      this.dialogFormVisible = false-->
<!--    },-->
<!--    handleSizeChange: function(val) {-->
<!--      this.params.pageSize = val-->
<!--      this.fetchJobs()-->
<!--    },-->
<!--    handleCurrentChange: function(val) {-->
<!--      this.params.pageNo = val-->
<!--      this.fetchJobs()-->
<!--    },-->
<!--    handleSelectionChange: function(val) {-->
<!--      this.multipleSelection = val.map(item => item.jobId)-->
<!--    },-->
<!--    // 打开加载层-->
<!--    openLoading() {-->
<!--      this.loading = this.$loading({-->
<!--        lock: true,-->
<!--        text: '正在加载中~',-->
<!--        spinner: 'el-icon-loading',-->
<!--        background: 'rgba(0, 0, 0, 0.7)'-->
<!--      })-->
<!--    },-->
<!--    dataFormat: function(time) {-->
<!--      return parseTime(time)-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->
