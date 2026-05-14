<template>
  <el-card header="预警记录">
    <el-empty v-if="!alerts.length" description="暂无预警信息" />
    <el-timeline v-else>
      <el-timeline-item v-for="a in alerts" :key="a.id" :timestamp="a.alertTime" placement="top" :type="a.status ? 'success' : 'danger'">
        <el-card shadow="hover">
          <div style="display:flex;justify-content:space-between;align-items:center">
            <div>
              <el-tag :type="a.alertType==='high_usage'?'danger':'warning'" size="small">{{ a.alertType==='high_usage' ? '高用电预警' : a.alertType }}</el-tag>
              <span style="margin-left:8px;color:#303133">{{ a.alertData }}</span>
            </div>
            <el-tag :type="a.status ? 'success' : 'warning'">{{ a.status ? '已处理' : '未处理' }}</el-tag>
          </div>
          <div v-if="a.handleNote" style="margin-top:8px;color:#909399;font-size:13px">处理备注: {{ a.handleNote }}</div>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../../api'

const alerts = ref([])

onMounted(async () => {
  const res = await api.get('/api/student/alerts')
  alerts.value = res.data || []
})
</script>
