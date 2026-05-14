import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    children: [
      { path: '', redirect: '/student/dashboard' },
      { path: 'student/dashboard', name: 'StudentDashboard', component: () => import('../views/student/Dashboard.vue') },
      { path: 'student/usage', name: 'StudentUsage', component: () => import('../views/student/Usage.vue') },
      { path: 'student/bills', name: 'StudentBills', component: () => import('../views/student/Bills.vue') },
      { path: 'student/alerts', name: 'StudentAlerts', component: () => import('../views/student/Alerts.vue') },
      { path: 'admin/dashboard', name: 'AdminDashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'admin/usage', name: 'AdminUsage', component: () => import('../views/admin/UsageQuery.vue') },
      { path: 'admin/alerts', name: 'AdminAlerts', component: () => import('../views/admin/AlertManage.vue') },
      { path: 'admin/bills', name: 'AdminBills', component: () => import('../views/admin/BillManage.vue') },
      { path: 'system/dashboard', name: 'SystemDashboard', component: () => import('../views/system/Dashboard.vue') },
      { path: 'system/users', name: 'SystemUsers', component: () => import('../views/system/UserManage.vue') },
      { path: 'system/dormitories', name: 'SystemDormitories', component: () => import('../views/system/DormitoryManage.vue') },
      { path: 'system/meters', name: 'SystemMeters', component: () => import('../views/system/MeterManage.vue') },
    ]
  }
]

const router = createRouter({ history: createWebHashHistory(), routes })

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) return next('/login')
  next()
})

export default router
