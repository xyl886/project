<template>
  <div class="create-page">
    <el-card shadow="never" class="create-card">
      <template #header><span style="font-weight: 600">发布帖子</span></template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" style="max-width: 800px">
        <el-form-item label="类型">
          <el-radio-group v-model="form.postsType">
            <el-radio :value="2" border>校园帖</el-radio>
            <el-radio :value="1" border>闲置帖</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="50" show-word-limit/>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div style="border:1px solid #dcdfe6;border-radius:6px;overflow:hidden">
            <div ref="editorRef" style="min-height:360px"></div>
          </div>
        </el-form-item>
        <el-form-item label="标签">
          <el-select v-model="selectedTags" multiple placeholder="选择标签" style="width: 100%">
            <el-option v-for="t in allTags" :key="t.id" :label="t.tagName" :value="t.id"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.postsType === 1" label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0.01" :precision="2" :step="1" placeholder="请输入价格"/>
        </el-form-item>
        <el-form-item label="图片">
          <el-upload
              ref="uploadRef"
              v-model:file-list="fileList"
              list-type="picture-card"
              :auto-upload="false"
              :limit="9"
              :disabled="fileList.length >= 9"
              :on-exceed="() => ElMessage.warning('最多上传9张图片')"
              multiple
          >
            <el-icon>
              <Plus/>
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting" size="large">发布</el-button>
          <el-button @click="$router.back()" size="large">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted, onBeforeUnmount, nextTick} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {addPost, getCategoryListAll, getTagList} from '@/api'
import {useUserStore} from '@/store/user'
import Vditor from 'vditor'
import 'vditor/dist/index.css'

const router = useRouter()
const store = useUserStore()
const formRef = ref(null)
const uploadRef = ref(null)
const fileList = ref([])
const submitting = ref(false)
const categories = ref([])
const allTags = ref([])
const selectedTags = ref([])
const editorRef = ref(null)
let vditor = null

const form = reactive({
  postsType: 2,
  categoryId: null,
  title: '',
  content: '',
  price: null
})

const rules = {
  categoryId: [{required: true, message: '请选择分类'}],
  title: [{required: true, message: '请输入标题'}],
  content: [{required: true, message: '请输入内容'}]
}

onMounted(async () => {
  if (!store.token) {
    ElMessage.warning('请先登录')
    return router.push('/login')
  }
  const [catRes, tagRes] = await Promise.all([getCategoryListAll(), getTagList()])
  if (catRes.code === 200) categories.value = catRes.data || []
  if (tagRes.code === 200) allTags.value = tagRes.data || []

  // 初始化 Vditor 编辑器
  await nextTick()
  vditor = new Vditor(editorRef.value, {
    height: 360,
    mode: 'ir',
    placeholder: '请输入内容...支持 Markdown 语法',
    toolbarConfig: { pin: true },
    cache: { enable: false },
    after: () => {
      if (form.content) vditor.setValue(form.content)
    }
  })
})

onBeforeUnmount(() => {
  vditor?.destroy()
})

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => {
  })
  if (!valid) return

  // 从 Vditor 获取内容
  form.content = vditor ? vditor.getValue() : form.content
  if (!form.content || form.content === '<br>' || form.content.trim() === '') {
    ElMessage.warning('请输入内容')
    return
  }

  submitting.value = true
  try {
    const fd = new FormData()
    fd.append('postsType', form.postsType)
    fd.append('categoryId', form.categoryId)
    fd.append('title', form.title)
    fd.append('content', form.content)
    if (selectedTags.value.length) {
      const names = selectedTags.value.map(id => allTags.value.find(t => t.id === id)?.tagName).filter(Boolean)
      fd.append('tags', names.join(','))
    }
    if (form.postsType === 1 && form.price) fd.append('price', form.price)

    const files = fileList.value
    for (const f of files) {
      if (f.raw) fd.append('files', f.raw)
    }

    const res = await addPost(fd)
    if (res.code === 200) {
      ElMessage.success('发布成功')
      router.push('/home')
    } else {
      ElMessage.error(res.msg || '发布失败')
    }
  } catch (e) {
    ElMessage.error('发布失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.create-page {
  max-width: 900px;
  margin: 24px auto;
  padding: 0 16px;
}

.create-card {
  border-radius: 8px;
}
</style>
