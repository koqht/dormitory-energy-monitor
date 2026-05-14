<template>
  <el-card>
    <template #header>
      <span>账单管理</span>
      <el-button type="primary" size="small" @click="generate" style="margin-left:16px">生成当月账单</el-button>
    </template>
    <el-table :data="bills" stripe>
      <el-table-column prop="billMonth" label="月份" width="120" />
      <el-table-column prop="dormitoryId" label="宿舍ID" width="120" />
      <el-table-column prop="usageKwh" label="用电量(kWh)" />
      <el-table-column prop="amount" label="金额(元)" />
      <el-table-column prop="payStatus" label="缴费状态" width="120">
        <template #default="{ row }"><el-tag :type="row.payStatus ? 'success' : 'danger'">{{ row.payStatus ? '已缴' : '未缴' }}</el-tag></template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" :total="total" :page-size="10" @current-change="load" layout="prev,next" style="margin-top:16px;justify-content:center" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../../api'

const bills = ref([])
const page = ref(1)
const total = ref(0)

const load = async () => {
  const res = await api.get('/api/admin/bills', { params: { page: page.value } })
  bills.value = res.data.records || []
  total.value = res.data.total || 0
}

const generate = async () => {
  const month = new Date().toISOString().slice(0, 7)
  await api.post('/api/admin/bills/generate', { month })
  ElMessage.success('账单生成完成')
  await load()
}

onMounted(load)
</script>
