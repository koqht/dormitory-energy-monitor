<template>
  <el-card>
    <template #header>
      <span>宿舍管理</span>
      <el-button type="primary" size="small" @click="showAdd" style="margin-left:16px">新增宿舍</el-button>
    </template>
    <el-table :data="dormitories" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="buildingNo" label="楼栋号" />
      <el-table-column prop="roomNo" label="房间号" />
      <el-table-column prop="floor" label="楼层" />
      <el-table-column prop="capacity" label="容量(人)" />
      <el-table-column prop="currentOccupants" label="现住人数" />
      <el-table-column prop="meterId" label="电表ID" />
      <el-table-column label="操作" width="150">
        <template #default="{row}">
          <el-button size="small" @click="showEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="editId?'编辑宿舍':'新增宿舍'" width="400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="楼栋号"><el-input v-model="form.buildingNo" /></el-form-item>
        <el-form-item label="房间号"><el-input v-model="form.roomNo" /></el-form-item>
        <el-form-item label="楼层"><el-input-number v-model="form.floor" :min="1" /></el-form-item>
        <el-form-item label="容量"><el-input-number v-model="form.capacity" :min="1" :max="10" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../../api'

const dormitories = ref([])
const dialog = ref(false)
const editId = ref(null)
const form = ref({ buildingNo: '', roomNo: '', floor: 1, capacity: 4 })

const load = async () => {
  const res = await api.get('/api/system/dormitories')
  dormitories.value = res.data.records || []
}

const showAdd = () => { editId.value = null; form.value = { buildingNo: '', roomNo: '', floor: 1, capacity: 4 }; dialog.value = true }
const showEdit = (row) => { editId.value = row.id; form.value = { ...row }; dialog.value = true }

const save = async () => {
  if (editId.value) { await api.put('/api/system/dormitories/' + editId.value, form.value) }
  else { await api.post('/api/system/dormitories', form.value) }
  ElMessage.success('保存成功'); dialog.value = false; await load()
}

const del = async (id) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await api.delete('/api/system/dormitories/' + id)
  ElMessage.success('删除成功'); await load()
}

onMounted(load)
</script>
