<template>
  <section class="chart-container">
    <el-row>
      <el-col :span="24">
        <PanelGroup/>
      </el-col>
      <!--      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">-->
      <!--        <div id="container" style=" width: 100%; height: 300px;" />-->
      <!--      </el-row>-->
      <el-col :span="24" style="padding:0;background:#fff;">
        <el-card>
          <div id="container" style="width:100%; height:300px;"/>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="e-title">帖子浏览量排行</div>
          <el-table :data="Data.posts" style="width: 100%;padding-top: 15px">
            <el-table-column label="标题" width="250">
              <template slot-scope="scope">
                <el-link
                  :underline="false"
                  style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;"
                  @click="onClick(scope.row)">{{ scope.row.title }}</el-link>
                <el-tag v-if="scope.row.postsType===1" type="">闲置</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="浏览量" prop="browseNum" width="100" align="center" />
          </el-table>
        </el-card>
      </el-col>
      <!-- 帖子标签统计 -->
      <el-col :span="8">
        <el-card>
          <div class="e-title" style="text-align: left;">帖子标签统计</div>
          <TagCloud :box-width="350" :speed="600" :random-color="true" style="height:370px;text-align: center"/>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div id="chartPie" style="width:100%; height:400px;"/>
        </el-card>
      </el-col>
      <!--      <el-col :span="12">-->
      <!--        <el-card>-->
      <!--          <div id="chartColumn" style="width:100%; height:400px;"/>-->
      <!--        </el-card>-->
      <!--      </el-col>-->
      <!--      <el-col :span="12">-->
      <!--        <el-card>-->
      <!--          <div id="chartBar" style="width:100%; height:400px;"/>-->
      <!--        </el-card>-->
      <!--      </el-col>-->
    </el-row>
  </section>
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import PanelGroup from './components/PanelGroup.vue'
import TagCloud from './components/TagCloud.vue'
import { init } from '../../api/init'

export default {
  components: { PanelGroup, TagCloud },
  data() {
    return {
      Data: {
        categoryPostCountDTO: [],
        contribute: [],
        posts: [],
        tagVO: []
      },
      category: [],
      chartColumn: null,
      chartBar: null,
      chartLine: null,
      chartPie: null
    }
  },
  mounted() {
    this.drawCharts()
  },
  // updated() {
  //   this.drawCharts()
  // },

  methods: {
    onClick() {

    },
    drawCharts() {
      init().then(res => {
        console.log(res.data)
        this.Data = res.data
        // this.drawColumnChart()
        // this.drawBarChart()
        this.initContributeDate(res.data.contribute.contributeDate, res.data.contribute.postContributeCount)
        this.drawPieChart(
          res.data.categoryPostCountDTO.map(item => `${item.name} (${item.count})`),
          res.data.categoryPostCountDTO.map(item => {
            return {
              name: `${item.name} (${item.count})`,
              value: item.count
            }
          }))
      })
    },
    // 初始化文章贡献度
    initContributeDate: function(contributeDate, postContributeCount) {
      const chart = echarts.init(document.getElementById('container'))
      const option = {
        // 设置背景
        // backgroundColor: '#d0d0d0',
        title: {
          top: 0,
          text: '帖子贡献度',
          subtext: '一年内帖子提交的数量',
          left: 'center',
          textStyle: {
            color: '#000'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: function(params) {
            return (params.data[0] + '<br>帖子数：' + params.data[1])
          }
        },
        visualMap: {
          min: 0,
          max: 100,
          type: 'piecewise',
          orient: 'horizontal',
          left: 'center',
          top: 50
        },
        legend: {
          top: '30',
          left: '100',
          // data: ['帖子数', 'Top 12'],
          textStyle: {
            // 设置字体颜色
            color: '#000'
          }
        },
        calendar: [{
          top: 100,
          left: 'center',
          range: contributeDate,
          splitLine: {
            show: true,
            lineStyle: {
              // 设置月份分割线的颜色
              color: 'rgba(0,0,0,0.06)',
              width: 4,
              type: 'solid'
            }
          },
          yearLabel: { show: false },
          dayLabel: {
            nameMap: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'], // 设置中文显示
            textStyle: {
              // 设置周显示颜色
              color: '#000'
            },
            firstDay: 1 // 从周一开始
          },
          monthLabel: {
            nameMap: 'cn', // 设置中文显示
            textStyle: {
              // 设置月显示颜色
              color: '#000'
            }
          },
          itemStyle: {
            normal: {
              // 设置背景颜色
              color: '#ffffff',
              borderWidth: 1,
              // 设置方块分割线段颜色
              borderColor: '#D3D3D3'
            }
          }
        }],
        series: [
          {
            name: '帖子数',
            type: 'scatter',
            coordinateSystem: 'calendar',
            data: postContributeCount,
            // 根据值设置原点大小
            symbolSize: function(val) {
              if (val[1] === 0) {
                return val[1]
              } else {
                let size = 8 + val[1] * 2
                if (size > 18) {
                  size = 18
                }
                return size
              }
            },
            itemStyle: {
              normal: {
                // 设置圆点颜色
                color: '#2ec7c9'
              }
            }
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', function() {
        chart.resize()
      })
    },

    drawColumnChart() {
      this.chartColumn = echarts.init(document.getElementById('chartColumn'))
      this.chartColumn.setOption({
        title: { text: 'Column Chart' },
        tooltip: {},
        xAxis: {
          data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
        },
        yAxis: {},
        series: [{
          name: '销量',
          type: 'bar',
          data: [5, 20, 36, 10, 10, 20]
        }]
      })
    },
    drawBarChart() {
      this.chartBar = echarts.init(document.getElementById('chartBar'))
      this.chartBar.setOption({
        title: {
          text: 'Bar Chart',
          subtext: '数据来自网络'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['2011年', '2012年']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, 0.01]
        },
        xAxis: {
          type: 'category',
          data: ['巴西', '印尼', '美国', '印度', '中国', '世界人口(万)']
        },
        series: [
          {
            name: '2011年',
            type: 'bar',
            data: [18203, 23489, 29034, 104970, 131744, 630230]
          },
          {
            name: '2012年',
            type: 'bar',
            data: [19325, 23438, 31000, 121594, 134141, 681807]
          }
        ]
      })
    },
    drawLineChart() {
      this.chartLine = echarts.init(document.getElementById('chartLine'))
      this.chartLine.setOption({
        title: {
          text: '帖子统计'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['邮件营销', '联盟广告', '搜索引擎']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '邮件营销',
            type: 'line',
            stack: '总量',
            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: '联盟广告',
            type: 'line',
            stack: '总量',
            data: [220, 182, 191, 234, 290, 330, 310]
          },
          {
            name: '搜索引擎',
            type: 'line',
            stack: '总量',
            data: [820, 932, 901, 934, 1290, 1330, 1320]
          }
        ]
      })
    },
    drawPieChart(categoryName, data) {
      this.chartPie = echarts.init(document.getElementById('chartPie'))
      this.chartPie.setOption({
        title: {
          text: '帖子分类统计',
          x: 'left'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'right',
          data: categoryName
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            center: ['50%', '60%'],
            data: data,
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })
    }
  }
}
</script>

<style scoped>
    .chart-container {
        width: 100%;
        float: left;
    }
    /*.chart div {
        height: 400px;
        float: left;
    }*/

    .el-col {
        padding: 10px;
    }
</style>
