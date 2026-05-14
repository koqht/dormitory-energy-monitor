<template>
  <el-container style="min-height:100vh">
    <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar">
      <div class="logo">{{ isCollapse ? '电' : '电表管理系统' }}</div>
      <el-menu :default-active="route.path" router :collapse="isCollapse" background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF">
        <template v-if="role === 'student'">
          <el-menu-item index="/student/dashboard"><el-icon><DataAnalysis /></el-icon><span>我的仪表盘</span></el-menu-item>
          <el-menu-item index="/student/usage"><el-icon><TrendCharts /></el-icon><span>用电图表</span></el-menu-item>
          <el-menu-item index="/student/bills"><el-icon><Tickets /></el-icon><span>账单查询</span></el-menu-item>
          <el-menu-item index="/student/alerts"><el-icon><Bell /></el-icon><span>预警信息</span></el-menu-item>
        </template>
        <template v-else-if="role === 'admin'">
          <el-menu-item index="/admin/dashboard"><el-icon><DataAnalysis /></el-icon><span>数据概览</span></el-menu-item>
          <el-menu-item index="/admin/usage"><el-icon><Search /></el-icon><span>用电查询</span></el-menu-item>
          <el-menu-item index="/admin/alerts"><el-icon><WarningFilled /></el-icon><span>预警处理</span></el-menu-item>
          <el-menu-item index="/admin/bills"><el-icon><Money /></el-icon><span>账单管理</span></el-menu-item>
        </template>
        <template v-else>
          <el-menu-item index="/system/dashboard"><el-icon><DataAnalysis /></el-icon><span>系统概览</span></el-menu-item>
          <el-menu-item index="/system/users"><el-icon><User /></el-icon><span>用户管理</span></el-menu-item>
          <el-menu-item index="/system/dormitories"><el-icon><OfficeBuilding /></el-icon><span>宿舍管理</span></el-menu-item>
          <el-menu-item index="/system/meters"><el-icon><SetUp /></el-icon><span>电表管理</span></el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="topbar">
        <el-button text @click="isCollapse = !isCollapse"><el-icon><Fold /></el-icon></el-button>
        <span style="flex:1;font-weight:bold;color:#303133">{{ roleText }} · {{ user?.name }}</span>
        <el-button text @click="logout">退出登录</el-button>
      </el-header>
      <el-main style="background:#f0f2f5;padding:20px">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const isCollapse = ref(false)
const user = JSON.parse(localStorage.getItem('user') || '{}')
const role = computed(() => user.role)
const roleText = computed(() => ({ student: '学生', admin: '宿管员', super_admin: '系统管理员' })[role.value])

const logout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>

<style scoped>
.sidebar { background: #304156; overflow: hidden; transition: width 0.3s; }
.logo { height: 60px; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 18px; font-weight: bold; border-bottom: 1px solid rgba(255,255,255,0.1); }
.topbar { background: #fff; display: flex; align-items: center; gap: 12px; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
</style>
