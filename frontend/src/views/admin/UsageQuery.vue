<template>
  <div>
    <el-card>
      <template #header>
        <span>用电数据查询</span>
        <el-select v-model="days" @change="load" style="margin-left:16px;width:120px">
          <el-option :value="7" label="近7天" /><el-option :value="15" label="近15天" /><el-option :value="30" label="近30天" />
        </el-select>
        <el-select v-model="dormitoryId" @change="load" clearable placeholder="全部宿舍" style="margin-left:12px;width:180px">
          <el-option v-for="d in dormitories" :key="d.id" :label="d.buildingNo + '栋' + d.roomNo + '室'" :value="d.id" />
        </el-select>
      </template>
      <div ref="chart" style="height:400px"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import api from '../../api'

const days = ref(7)
const dormitoryId = ref(null)
const chart = ref(null)
const dormitories = ref([])

const load = async () => {
  const params = { days: days.value }
  if (dormitoryId.value) params.dormitoryId = dormitoryId.value
  const res = await api.get('/api/admin/usage/query', { params })
  const c = echarts.init(chart.value)
  c.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: (res.data || []).map(d => d.date) },
    yAxis: { type: 'value', name: 'kWh' },
    series: [{ data: (res.data || []).map(d => d.total), type: 'line', smooth: true, areaStyle: {}, itemStyle: { color: '#409EFF' } }]
  })
}

onMounted(async () => {
  const list = await api.get('/api/admin/dormitories')
  dormitories.value = list.data || []
  await load()
})
</script>
