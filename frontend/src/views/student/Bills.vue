<template>
  <el-card header="账单记录">
    <el-table :data="bills" stripe default-expand-all>
      <el-table-column prop="billMonth" label="账单月份" />
      <el-table-column prop="usageKwh" label="用电量(kWh)" />
      <el-table-column prop="unitPrice" label="单价(元/kWh)" />
      <el-table-column prop="amount" label="金额(元)" sortable />
      <el-table-column prop="payStatus" label="缴费状态">
        <template #default="{ row }">
          <el-tag :type="row.payStatus ? 'success' : 'danger'">{{ row.payStatus ? '已缴费' : '未缴费' }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../../api'

const bills = ref([])

onMounted(async () => {
  const res = await api.get('/api/student/bills')
  bills.value = res.data || []
})
</script>
