<template>
  <div class="login-bg">
    <div class="login-card">
      <h2>高校学生公寓智能电表数据可视化系统</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="学号/工号" prop="studentNo">
          <el-input v-model="form.studentNo" placeholder="请输入学号或工号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password @keyup.enter="login" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login" :loading="loading" style="width:100%">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="login-tips">
        <p>测试账号：</p>
        <p>系统管理员: admin001 / admin123</p>
        <p>宿管: staff001 / admin123</p>
        <p>学生: 20210001 / 123456</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import api from '../api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ studentNo: '', password: '' })
const rules = {
  studentNo: [{ required: true, message: '请输入学号/工号' }],
  password: [{ required: true, message: '请输入密码' }]
}

const roleRedirect = {
  student: '/student/dashboard',
  admin: '/admin/dashboard',
  super_admin: '/system/dashboard'
}

const login = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await api.post('/api/auth/login', form)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('user', JSON.stringify(res.data.user))
    ElMessage.success('登录成功')
    router.push(roleRedirect[res.data.user.role] || '/student/dashboard')
  } catch (e) {
    ElMessage.error(e.response?.data?.msg || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-bg {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 420px;
  padding: 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.2);
}
.login-card h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
  font-size: 18px;
}
.login-tips {
  margin-top: 16px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 8px;
  font-size: 12px;
  color: #909399;
  line-height: 1.8;
}
</style>
