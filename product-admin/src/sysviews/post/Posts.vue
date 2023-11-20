<template>
  <div>
    <!--查询or添加-->
    <el-form v-show="showSearch" ref="form" :inline="true" :model="page" label-width="68px">
      <el-form-item label="帖子标题">
        <el-input v-model="page.title" clearable style="width: 150px" size="small" placeholder="请输入帖子标题" />
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="page.postsType"
          style="width: 150px"
          size="small"
          filterable
          clearable
          reserve-keyword
          placeholder="请选择帖子类型"
          @change="handleFind">
          <el-option v-for="item in postsType" :key="item.id" :label="item.postsType" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="标签名">
        <el-select
          v-model="page.tagId"
          style="width: 130px"
          size="small"
          filterable
          clearable
          reserve-keyword
          placeholder="请选择标签"
          @change="handleFind">
          <el-option v-for="item in tags" :key="item.id" :label="item.tag_name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="分类名">
        <el-select
          v-model="page.categoryId"
          style="width: 130px"
          size="small"
          filterable
          clearable
          reserve-keyword
          placeholder="请选择分类"
          @change="handleFind">
          <el-option v-for="item in category" :key="item.id" :label="item.categoryName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="发布状态">
        <el-select
          v-model="page.status"
          style="width: 130px"
          size="small"
          filterable
          clearable
          reserve-keyword
          placeholder="是否发布"
          @change="handleFind">
          <el-option v-for="item in publishList" :key="item.statusId" :label="item.postStatus" :value="item.statusId" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleFind">查找</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <RightToolbar :show-search.sync="showSearch" @queryTable="getPageFun"/>
    </el-row>
    <!--    表格-->
    <el-table
      :data="table"
      :default-sort = "{prop: 'create_time', order: 'descending'}"
      border
      style="width: 100%"
      max-height="750">
      <el-table-column
        type="selection"
        width="55"/>
      <!--      <el-table-column-->
      <!--        label="用户昵称"-->
      <!--        width="120">-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-popover trigger="hover" placement="top">-->
      <!--            <p>id: {{ scope.row.userId }}</p>-->
      <!--            <p>姓名: {{ scope.row.userInfo.nickname }}<el-tag size="mini">{{ scope.row.userInfo.role }}</el-tag></p>-->
      <!--            <p>住址: {{ scope.row.userInfo.genderText }}</p>-->
      <!--            <div slot="reference" class="name-wrapper">-->
      <!--              <el-tag size="medium">{{ scope.row.userInfo.nickname }}</el-tag>-->
      <!--            </div>-->
      <!--          </el-popover>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <!--      <el-table-column-->
      <!--        prop="userInfo.avatar"-->
      <!--        label="头像"-->
      <!--        width="120">-->
      <!--        <template slot-scope="scope">-->
      <!--          <img :src="scope.row.userInfo.avatar" style="border-radius: 50%;width: 60px; height: 60px" >-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column
        prop="coverPath"
        label="帖子图片"
        width="200">
        <template slot-scope="scope">
          <img :src="scope.row.coverPath" style="width: 160px; height: 100px" >
      </template></el-table-column>
      <el-table-column
        prop="title"
        label="标题"
        width="200"/>
      <el-table-column
        prop="type"
        label="帖子类型"
        width="120">
        <template slot-scope="scope">
          <el-tag
            disable-transitions>{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="schoolName"
        label="分类"
        width="120">
        <template slot-scope="scope">
          <el-tag
            disable-transitions>{{ scope.row.categoryName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="tags"
        label="标签"
        width="180">
        <template slot-scope="scope">
          <el-tag
            v-for="item in scope.row.tags"
            :key="item"
            type="info"
            style="margin: 5px;border-radius: 14px"
            disable-transitions>{{ item }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="browseNum"
        label="浏览量"
        sortable
        width="120"/>
      <el-table-column
        prop="likeNum"
        label="点赞量"
        sortable
        width="120"/>
      <el-table-column
        prop="commentNum"
        label="评论量"
        sortable
        width="120"/>
      <el-table-column
        prop="createTime"
        label="发布时间"
        sortable
        width="160"/>
      <el-table-column
        prop="postStatus"
        label="状态"
        width="120">
        <template slot-scope="scope">
          <el-tag
            disable-transitions>{{ scope.row.postStatus }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        fixed="right"
        header-align="center"
        align="center"
        width="300">
        <!--        <template slot="header" slot-scope="scope">-->
        <!--          操作-->
        <!--          <el-switch-->
        <!--            v-model="value"-->
        <!--            active-color="#13ce66"-->
        <!--            inactive-color="#ff4949"/>-->
        <!--        </template>-->
        <template slot-scope="scope">
          <el-button
            size="small"
            @click="detailFun(scope.row)">
            查看
          </el-button>
          <el-button
            v-if="scope.row.status === 1"
            type="primary"
            size="small"
            @click.native.prevent="audit(scope)">
            审核
          </el-button>
          <el-button
            v-if="scope.row.status === 1"
            type="warning"
            size="small"
            @click.native.prevent="Reject(scope)">
            拒绝
          </el-button>
          <el-button
            v-else-if="scope.row.status === 4"
            type="danger"
            size="small"
            @click.native.prevent="audit(scope)">
            重新上架
          </el-button>
          <el-button
            v-else-if="scope.row.status === 3"
            type="danger"
            size="small"
            @click.native.prevent="takeDown(scope)">
            下架
          </el-button>
          <el-button
            v-else
            disabled
            size="small"
            type="info">
            {{ scope.row.postStatus }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :modal="posts" :visible="dialogVisible" title="帖子详情" @close="closeDialog">
      <el-descriptions :column="3" :size="size" class="margin-top" border>
        <el-descriptions-item>
          <template slot="label">
            标题
          </template>
          {{ posts.title }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            类型
          </template>
          <el-tag>{{ posts.type }} </el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            分类
          </template>
          {{ posts.categoryName }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            标签
          </template>
          <el-tag
            v-for="item in posts.tags"
            :key="item"
            type="info"
            style="margin: 5px;border-radius: 14px"
            disable-transitions>{{ item }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            提交时间
          </template>
          {{ posts.createTime }}
        </el-descriptions-item>
        <el-descriptions-item :column="2">
          <template slot="label">
            描述
          </template>
          {{ posts.description }}
        </el-descriptions-item>
        <el-descriptions-item :column="3">
          <template slot="label">
            <i class="el-icon-office-building"/>
            内容
          </template>
          {{ posts.content }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
    <div style="text-align: center;margin-top: 10px;">
      <el-pagination
        :current-page.sync="page.currentPage"
        :page-sizes="[10, 20, 40, 80]"
        :page-size="page.pageSize"
        :total="page.total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="sizeChange"
        @current-change="currentChange"/>
    </div>
  </div>
</template>

<script>
import { audit, getPage, listAllCategory, listAllTags } from '../../api/posts'
import { formatDate } from '../../utils/date'

export default {
  data() {
    return {
      showSearch: true,
      value: false,
      loading: false,
      category: [],
      tags: [],
      posts: [],
      dialogVisible: false,
      postsType: [
        { id: 1, postsType: '闲置' },
        { id: 2, postsType: '校园' }
      ],
      publishList: [
        { statusId: 1, postStatus: '审核中' },
        { statusId: 2, postStatus: '未通过' },
        { statusId: 3, postStatus: '已发布' },
        { statusId: 4, postStatus: '已下架' },
        { statusId: 5, postStatus: '已删除' }
      ],
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        postsType: null,
        title: null,
        tagId: null,
        categoryId: null,
        status: null
      },
      text: [],
      table: []
    }
  },
  beforeCreate() {
    const data = { total: 0, pageSize: 10, currentPage: 1, categoryName: null }
    listAllCategory(data).then(res => {
      this.category = res.data
    })
    listAllTags().then(res => {
      this.tags = res.data
      console.log(JSON.stringify(res.data))
    })
  },
  mounted() {
    console.log(this.$route.query)
    this.page.status = this.$route.query ? this.$route.query.statusId : null
    this.getPageFun()
  },
  methods: {
    formatDate,
    Reject(scope) {
      this.$confirm('确定要不通过该帖子吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const post = { id: scope.row.id, status: 2 }
        audit(post).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.getPageFun()
          }
        })
      })
    },
    audit(scope) {
      this.$confirm('确定要通过该帖子吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const post = { id: scope.row.id, status: 3 }
        audit(post).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.getPageFun()
          }
        })
      })
    },
    takeDown(scope) {
      this.$confirm('确定要下架该帖子吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const post = { id: scope.row.id, status: 4 }
        audit(post).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.getPageFun()
          }
        })
      })
    },
    detailFun(posts) {
      this.dialogVisible = true
      this.posts = posts
      console.log(this.posts)
      // setStore({ name: 'posts', content: this.posts })
      // this.$router.push({ path: '/index/post/detail' })
    },
    closeDialog() {
      // 弹窗关闭时的回调
      this.dialogVisible = false
    },
    handleFind() {
      this.getPageFun()
    },
    resetQuery() {
      this.page = {
        total: 0,
        pageSize: 10,
        currentPage: 1,
        postsType: null,
        title: null,
        tagId: null,
        categoryId: null,
        status: null
      }
      this.text = []
      this.getPageFun()
    },
    deleteRow(index, rows) {
      rows.splice(index, 1)
    },
    sizeChange(pageSize) { // 页数
      this.page.pageSize = pageSize
      this.getPageFun()
    },
    currentChange(currentPage) { // 当前页
      this.page.currentPage = currentPage
      this.getPageFun()
    },
    getPageFun() {
      // const id = this.$route.params.id
      // if (id !== undefined) {
      //   this.page.status = id
      // } else {
      //   this.page.status === []
      // }
      this.table = []
      this.loading = true
      console.log(this.page)
      getPage(this.page).then(res => {
        if (res.code === 200) {
          this.table = res.data
          this.page.total = res.dataTotal
          this.loading = false
        }
      }, error => {
        console.log(error)
      })
    }
  }
}
</script>
