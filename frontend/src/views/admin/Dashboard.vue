<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="6"><el-card><div class="stat-label">今日总用电</div><div class="stat-value">{{ data.todayUsage?.toFixed(2) }} kWh</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-label">本月总用电</div><div class="stat-value">{{ data.monthUsage?.toFixed(2) }} kWh</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-label">未处理预警</div><div class="stat-value" style="color:#F56C6C">{{ data.alertCount }}</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-label">宿舍总数</div><div class="stat-value">{{ data.dormitoryCount }}</div></el-card></el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <el-card header="用电排行 TOP10">
          <div ref="topChart" style="height:350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="近30天用电趋势">
          <div ref="trendChart" style="height:350px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import * as echarts from 'echarts'
import api from '../../api'

const topChart = ref(null)
const trendChart = ref(null)
const data = reactive({ todayUsage: 0, monthUsage: 0, alertCount: 0, dormitoryCount: 0 })

onMounted(async () => {
  const res = await api.get('/api/admin/dashboard')
  Object.assign(data, res.data)

  const top = echarts.init(topChart.value)
  const items = res.data.top10 || []
  top.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: items.map(i => i.building_no + '-' + i.room_no) },
    yAxis: { type: 'value', name: 'kWh' },
    series: [{ data: items.map(i => i.total), type: 'bar', itemStyle: { color: '#E6A23C' } }]
  })

  const usageRes = await api.get('/api/admin/usage/query?days=30')
  const trend = echarts.init(trendChart.value)
  const allDates = [...new Set((usageRes.data || []).map(d => d.date))]
  trend.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: allDates },
    yAxis: { type: 'value', name: 'kWh' },
    series: [{ data: (usageRes.data || []).map(d => d.total), type: 'line', smooth: true, areaStyle: {} }]
  })
})
</script>

<style scoped>
.stat-label { font-size: 13px; color: #909399; margin-bottom: 8px; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; }
</style>
