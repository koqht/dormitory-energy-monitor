<template>
  <el-card>
    <template #header><span>电表管理</span></template>
    <el-table :data="meters" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="meterNo" label="电表编号" />
      <el-table-column prop="dormitoryId" label="绑定宿舍ID" />
      <el-table-column prop="currentUsage" label="当前读数(kWh)" />
      <el-table-column prop="voltage" label="电压(V)" />
      <el-table-column prop="current" label="电流(A)" />
      <el-table-column prop="power" label="功率(W)" />
      <el-table-column prop="alertThreshold" label="预警阈值(kWh)" />
      <el-table-column label="操作" width="220">
        <template #default="{row}">
          <el-button size="small" @click="showBind(row)">绑定宿舍</el-button>
          <el-button size="small" @click="showThreshold(row)">设置阈值</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="bindDialog" title="绑定宿舍" width="300px">
      <el-form><el-form-item label="宿舍"><el-select v-model="bindDormitoryId"><el-option v-for="d in dormitories" :key="d.id" :label="d.buildingNo+'栋'+d.roomNo" :value="d.id" /></el-select></el-form-item></el-form>
      <template #footer><el-button @click="bindDialog=false">取消</el-button><el-button type="primary" @click="doBind">确认</el-button></template>
    </el-dialog>

    <el-dialog v-model="thDialog" title="设置预警阈值" width="300px">
      <el-form><el-form-item label="阈值(kWh/日)"><el-input-number v-model="threshold" :min="1" :max="200" /></el-form-item></el-form>
      <template #footer><el-button @click="thDialog=false">取消</el-button><el-button type="primary" @click="doThreshold">确认</el-button></template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../../api'

const meters = ref([])
const dormitories = ref([])
const bindDialog = ref(false)
const bindDormitoryId = ref(null)
const currentMeterId = ref(null)
const thDialog = ref(false)
const threshold = ref(50)

const load = async () => {
  const [mRes, dRes] = await Promise.all([
    api.get('/api/system/meters'),
    api.get('/api/system/dormitories')
  ])
  meters.value = mRes.data.records || []
  dormitories.value = dRes.data.records || []
}

const showBind = (row) => { currentMeterId.value = row.id; bindDormitoryId.value = row.dormitoryId; bindDialog.value = true }
const doBind = async () => {
  await api.put('/api/system/meters/' + currentMeterId.value + '/bind', { dormitoryId: bindDormitoryId.value })
  ElMessage.success('绑定成功'); bindDialog.value = false; await load()
}

const showThreshold = (row) => { currentMeterId.value = row.id; threshold.value = row.alertThreshold; thDialog.value = true }
const doThreshold = async () => {
  await api.put('/api/system/meters/' + currentMeterId.value + '/threshold', { threshold: threshold.value })
  ElMessage.success('阈值已更新'); thDialog.value = false; await load()
}

onMounted(load)
</script>
