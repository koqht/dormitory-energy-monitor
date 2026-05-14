<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="6"><el-card><div class="stat-label">今日用电</div><div class="stat-value">{{ data.todayUsage?.toFixed(2) }} kWh</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-label">本月用电</div><div class="stat-value">{{ data.monthUsage }} kWh</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-label">宿舍电表</div><div class="stat-value">{{ data.meter?.currentUsage }} kWh</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-label">宿舍号</div><div class="stat-value">{{ data.dormitory?.buildingNo }}栋{{ data.dormitory?.roomNo }}室</div></el-card></el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <el-card header="近7天用电趋势">
          <div ref="chartRef" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="节能建议">
          <el-alert v-for="(tip, i) in data.suggestions" :key="i" :title="tip" type="info" :closable="false" style="margin-bottom:8px" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <el-card header="最新账单">
          <el-table :data="data.bills" size="small"><el-table-column prop="billMonth" label="月份" /><el-table-column prop="usageKwh" label="用电量(kWh)" /><el-table-column prop="amount" label="金额(元)" /><el-table-column prop="payStatus" label="状态"><template #default="{row}"><el-tag :type="row.payStatus ? 'success' : 'danger'">{{ row.payStatus ? '已缴' : '未缴' }}</el-tag></template></el-table-column></el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="预警信息">
          <el-empty v-if="!data.alerts?.length" description="暂无预警" :image-size="60" />
          <el-timeline v-else>
            <el-timeline-item v-for="a in data.alerts" :key="a.id" :timestamp="a.alertTime" placement="top">
              <el-tag :type="a.alertType === 'high_usage' ? 'danger' : 'warning'">{{ a.alertType === 'high_usage' ? '高用电' : a.alertType }}</el-tag>
              {{ a.alertData }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import * as echarts from 'echarts'
import api from '../../api'

const chartRef = ref(null)
const data = reactive({ todayUsage: 0, monthUsage: 0, meter: {}, dormitory: {}, bills: [], alerts: [], suggestions: [] })

const loadData = async () => {
  const res = await api.get('/api/student/dashboard')
  Object.assign(data, res.data)
}

const loadChart = async () => {
  const res = await api.get('/api/student/usage/chart?days=7')
  const chart = echarts.init(chartRef.value)
  const dates = res.data.map(d => d.date)
  const values = res.data.map(d => d.total)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: dates },
    yAxis: { type: 'value', name: 'kWh' },
    series: [{ data: values, type: 'line', smooth: true, areaStyle: {} }]
  })
}

onMounted(async () => {
  await loadData()
  await loadChart()
})
</script>

<style scoped>
.stat-label { font-size: 13px; color: #909399; margin-bottom: 8px; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; }
</style>
