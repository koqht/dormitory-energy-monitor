<template>
  <div>
    <el-card>
      <template #header>
        <span>用电趋势分析</span>
        <el-select v-model="days" @change="loadChart" style="margin-left:16px;width:120px">
          <el-option :value="7" label="近7天" />
          <el-option :value="15" label="近15天" />
          <el-option :value="30" label="近30天" />
        </el-select>
      </template>
      <div ref="lineChart" style="height:350px"></div>
    </el-card>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <el-card header="用电排行 (近7天)">
          <div ref="barChart" style="height:350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="我的电表信息">
          <el-descriptions :column="1" border v-if="meter">
            <el-descriptions-item label="电表编号">{{ meter.meterNo }}</el-descriptions-item>
            <el-descriptions-item label="当前读数">{{ meter.currentUsage }} kWh</el-descriptions-item>
            <el-descriptions-item label="电压">{{ meter.voltage }} V</el-descriptions-item>
            <el-descriptions-item label="电流">{{ meter.current }} A</el-descriptions-item>
            <el-descriptions-item label="功率">{{ meter.power }} W</el-descriptions-item>
            <el-descriptions-item label="预警阈值">{{ meter.alertThreshold }} kWh/日</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import * as echarts from 'echarts'
import api from '../../api'

const days = ref(7)
const lineChart = ref(null)
const barChart = ref(null)
const meter = ref(null)

const loadChart = async () => {
  const res = await api.get('/api/student/usage/chart?days=' + days.value)
  const chart = echarts.init(lineChart.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: res.data.map(d => d.date) },
    yAxis: { type: 'value', name: 'kWh' },
    series: [{ data: res.data.map(d => d.total), type: 'line', smooth: true, areaStyle: {}, itemStyle: { color: '#409EFF' } }]
  })
}

const loadRanking = async () => {
  const res = await api.get('/api/student/usage/ranking?days=7')
  const chart = echarts.init(barChart.value)
  const sorted = (res.data || []).slice(0, 10)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: sorted.map((_, i) => '宿舍' + (i + 1)) },
    yAxis: { type: 'value', name: 'kWh' },
    series: [{ data: sorted.map(d => d.total), type: 'bar', itemStyle: { color: '#67C23A' } }]
  })
}

onMounted(async () => {
  await loadChart()
  await loadRanking()
  const dash = await api.get('/api/student/dashboard')
  meter.value = dash.data.meter
})
</script>
