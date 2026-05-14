<template>
  <el-card header="预警管理">
    <el-table :data="alerts" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="dormitoryId" label="宿舍ID" width="100" />
      <el-table-column prop="alertType" label="类型" width="120">
        <template #default="{ row }"><el-tag :type="row.alertType==='high_usage'?'danger':'warning'">{{ row.alertType==='high_usage'?'高用电':row.alertType }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="alertData" label="详情" min-width="200" />
      <el-table-column prop="alertTime" label="时间" width="160" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }"><el-tag :type="row.status ? 'success' : 'danger'">{{ row.status ? '已处理' : '未处理' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button v-if="!row.status" type="primary" size="small" @click="showHandle(row)">处理</el-button>
          <span v-else style="color:#909399">{{ row.handleNote }}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" :total="total" :page-size="10" @current-change="load" layout="prev,next" style="margin-top:16px;justify-content:center" />

    <el-dialog v-model="dialog" title="处理预警" width="400px">
      <el-form><el-form-item label="处理备注"><el-input v-model="note" type="textarea" /></el-form-item></el-form>
      <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="handleAlert">确认处理</el-button></template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../../api'

const alerts = ref([])
const page = ref(1)
const total = ref(0)
const dialog = ref(false)
const note = ref('')
const currentId = ref(null)

const load = async () => {
  const res = await api.get('/api/admin/alerts', { params: { page: page.value } })
  alerts.value = res.data.records || []
  total.value = res.data.total || 0
}

const showHandle = (row) => { currentId.value = row.id; dialog.value = true; note.value = '' }

const handleAlert = async () => {
  await api.put('/api/admin/alerts/' + currentId.value + '/handle', { handleNote: note.value })
  ElMessage.success('处理完成')
  dialog.value = false
  await load()
}

onMounted(load)
</script>
