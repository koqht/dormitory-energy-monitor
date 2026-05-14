<template>
  <el-card>
    <template #header>
      <span>用户管理</span>
      <el-button type="primary" size="small" @click="showAdd" style="margin-left:16px">新增用户</el-button>
    </template>
    <el-table :data="users" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="studentNo" label="学号/工号" width="110" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="role" label="角色" width="110">
        <template #default="{row}"><el-tag>{{ {student:'学生',admin:'宿管员',super_admin:'系统管理员'}[row.role] }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{row}"><el-tag :type="row.status?'success':'danger'">{{ row.status?'启用':'禁用' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{row}">
          <el-button size="small" @click="showEdit(row)">编辑</el-button>
          <el-button size="small" type="warning" @click="toggle(row)">{{ row.status?'禁用':'启用' }}</el-button>
          <el-button v-if="row.role!=='super_admin'" size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="editId?'编辑用户':'新增用户'" width="450px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="学号/工号"><el-input v-model="form.studentNo" :disabled="!!editId" /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" :placeholder="editId?'不填则不修改':''" /></el-form-item>
        <el-form-item label="角色"><el-select v-model="form.role"><el-option value="student" label="学生" /><el-option value="admin" label="宿管员" /><el-option value="super_admin" label="系统管理员" /></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../../api'

const users = ref([])
const dialog = ref(false)
const editId = ref(null)
const form = ref({ studentNo: '', name: '', phone: '', password: '', role: 'student' })

const load = async () => {
  const res = await api.get('/api/system/users', { params: { page: 1, size: 999 } })
  users.value = res.data.records || []
}

const showAdd = () => { editId.value = null; form.value = { studentNo: '', name: '', phone: '', password: '', role: 'student' }; dialog.value = true }
const showEdit = (row) => { editId.value = row.id; form.value = { ...row, password: '' }; dialog.value = true }

const save = async () => {
  try {
    if (editId.value) {
      await api.put('/api/system/users/' + editId.value, form.value)
    } else {
      await api.post('/api/system/users', form.value)
    }
    ElMessage.success('保存成功')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e.response?.data?.msg || '保存失败')
  }
}

const toggle = async (row) => { await api.put('/api/system/users/' + row.id + '/status'); await load() }

const del = async (id) => {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await api.delete('/api/system/users/' + id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>
