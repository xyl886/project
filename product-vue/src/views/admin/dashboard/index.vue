<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in cardStats" :key="item.label">
        <el-card shadow="never" class="stat-card" :style="{ background: item.bg }">
          <div class="stat-inner">
            <div>
              <div class="stat-label">{{ item.label }}</div>
              <div class="stat-value">{{ item.value }}</div>
            </div>
            <el-icon class="stat-icon" :size="48"><component :is="item.icon" /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header><span style="font-weight: 600">分类帖子统计</span></template>
          <div ref="barChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <template #header><span style="font-weight: 600">帖子贡献趋势</span></template>
          <div ref="lineChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="14">
        <el-card shadow="never">
          <template #header><span style="font-weight: 600">热门帖子排行</span></template>
          <div v-if="hotPosts.length">
            <div v-for="(item, i) in hotPosts" :key="item.id" class="hot-item">
              <span class="hot-rank" :class="{ top: i < 3 }">{{ i + 1 }}</span>
              <span class="hot-title">{{ item.title }}</span>
              <span class="hot-stat">
                <el-icon style="margin-right: 4px"><View /></el-icon>{{ item.browseNum || 0 }}
              </span>
              <span class="hot-stat">
                <el-icon style="margin-right: 4px"><Star /></el-icon>{{ item.likeNum || 0 }}
              </span>
            </div>
          </div>
          <el-empty v-else description="暂无数据" />
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <template #header><span style="font-weight: 600">热门标签</span></template>
          <div ref="wordCloudRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import 'echarts-wordcloud'
import { getLineCount, getHomeInit } from '@/api/admin'

const cardStats = ref([
  { label: '帖子总数', value: 0, icon: 'Document', bg: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
  { label: '用户总数', value: 0, icon: 'User', bg: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
  { label: '评论总数', value: 0, icon: 'ChatDotSquare', bg: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
  { label: '待审核', value: 0, icon: 'Warning', bg: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)' }
])
const hotPosts = ref([])
const barChartRef = ref(null)
const lineChartRef = ref(null)
const wordCloudRef = ref(null)

onMounted(async () => {
  try {
    const countRes = await getLineCount()
    if (countRes.code === 200) {
      const d = countRes.data
      cardStats.value[0].value = d.postsCount || 0
      cardStats.value[1].value = d.userCount || 0
      cardStats.value[2].value = d.commentCount || 0
      cardStats.value[3].value = d.pendingPostsCount || 0
    }

    const initRes = await getHomeInit()
    if (initRes.code === 200) {
    const d = initRes.data

    if (d.categoryPostCountDTO && d.categoryPostCountDTO.length) {
      const barChart = echarts.init(barChartRef.value)
      barChart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 40, right: 20, bottom: 30, top: 20 },
        xAxis: { type: 'category', data: d.categoryPostCountDTO.map(c => c.name || '未知'), axisLabel: { color: '#999' } },
        yAxis: { type: 'value', axisLabel: { color: '#999' } },
        series: [{
          type: 'bar', data: d.categoryPostCountDTO.map(c => c.count || 0),
          itemStyle: { borderRadius: [4, 4, 0, 0], color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#667eea' }, { offset: 1, color: '#764ba2' }]) }
        }]
      })
    }

    if (d.contribute && d.contribute.postContributeCount) {
      const lineChart = echarts.init(lineChartRef.value)
      const data = d.contribute.postContributeCount
      lineChart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 40, right: 20, bottom: 30, top: 20 },
        xAxis: { type: 'category', data: data.map(item => item[0]), axisLabel: { color: '#999', fontSize: 11 } },
        yAxis: { type: 'value', axisLabel: { color: '#999' } },
        series: [{
          type: 'line', data: data.map(item => item[1]), smooth: true,
          lineStyle: { color: '#4facfe', width: 2 },
          areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(79,172,254,0.3)' }, { offset: 1, color: 'rgba(79,172,254,0.02)' }]) },
          itemStyle: { color: '#4facfe' }
        }]
      })
    }

    if (d.posts && d.posts.length) hotPosts.value = d.posts.slice(0, 8)

    if (d.tagVO && d.tagVO.length && wordCloudRef.value) {
      const wordCloud = echarts.init(wordCloudRef.value)
      const max = d.tagVO.length
      wordCloud.setOption({
        tooltip: { show: false },
        series: [{
          type: 'wordCloud', gridSize: 10, sizeRange: [14, 36], rotationRange: [-30, 30], rotationStep: 15, shape: 'circle',
          left: 'center', top: 'center', width: '100%', height: '100%',
          textStyle: { fontFamily: 'sans-serif', color: () => { const colors = ['#667eea', '#764ba2', '#4facfe', '#f093fb', '#fa709a', '#ff6b6b', '#feca57', '#48dbfb']; return colors[Math.floor(Math.random() * colors.length)] } },
          data: d.tagVO.map((tag, i) => ({ name: tag.tagName, value: max - i }))
        }]
      })
    }
    }
  } catch (e) {
    console.error('仪表盘数据加载失败', e)
  }
})
</script>

<style scoped>
.stat-card { border-radius: 12px; border: none; color: #fff; }
.stat-inner { display: flex; justify-content: space-between; align-items: center; }
.stat-label { font-size: 14px; opacity: 0.85; margin-bottom: 8px; }
.stat-value { font-size: 32px; font-weight: 700; }
.stat-icon { opacity: 0.3; }
.hot-item { display: flex; align-items: center; gap: 12px; padding: 12px 0; border-bottom: 1px solid #f5f5f5; }
.hot-item:last-child { border-bottom: none; }
.hot-rank { width: 22px; height: 22px; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 12px; font-weight: 700; color: #999; background: #f5f5f5; flex-shrink: 0; }
.hot-rank.top { background: linear-gradient(135deg, #ff6b6b, #ee5a24); color: #fff; }
.hot-title { flex: 1; font-size: 13px; color: #333; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.hot-title:hover { color: #409eff; cursor: pointer; }
.hot-stat { font-size: 12px; color: #999; display: flex; align-items: center; flex-shrink: 0; }
</style>
