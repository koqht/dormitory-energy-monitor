<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card><div class="stat-label">用户总数</div><div class="stat-value">{{ stats.users }}</div></el-card>
      </el-col>
      <el-col :span="8">
        <el-card><div class="stat-label">宿舍总数</div><div class="stat-value">{{ stats.dormitories }}</div></el-card>
      </el-col>
      <el-col :span="8">
        <el-card><div class="stat-label">电表总数</div><div class="stat-value">{{ stats.meters }}</div></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="24">
        <el-card header="系统信息">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="系统名称">高校学生公寓智能电表数据可视化系统</el-descriptions-item>
            <el-descriptions-item label="后端框架">Spring Boot 2.7 + MyBatis-Plus 3.5</el-descriptions-item>
            <el-descriptions-item label="前端框架">Vue 3 + Element Plus + ECharts</el-descriptions-item>
            <el-descriptions-item label="数据库">MySQL 5.7</el-descriptions-item>
            <el-descriptions-item label="电价">0.5 元/kWh</el-descriptions-item>
            <el-descriptions-item label="运行状态" ><el-tag type="success">正常运行</el-tag></el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import api from '../../api'

const stats = reactive({ users: 0, dormitories: 0, meters: 0 })

onMounted(async () => {
  const [users, dorms, meters] = await Promise.all([
    api.get('/api/system/users'),
    api.get('/api/system/dormitories'),
    api.get('/api/system/meters')
  ])
  stats.users = users.data.total || 0
  stats.dormitories = dorms.data.total || 0
  stats.meters = meters.data.total || 0
})
</script>

<style scoped>
.stat-label { font-size: 13px; color: #909399; margin-bottom: 8px; }
.stat-value { font-size: 28px; font-weight: bold; color: #303133; }
</style>
