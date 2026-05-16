<template>
  <div class="admin-layout">
    <!-- 顶部白色横幅 -->
    <div class="top-banner-modern">
      <div class="banner-left">
        <img
          v-lazy
          class="banner-logo"
          src="@/assets/images/logo.png"
          alt="logo"
        />
        <span class="banner-system-name">全国大学生海报设计大赛管理系统</span>
      </div>
      <div class="banner-right">
        <img v-if="userAvatar" :src="userAvatar" class="banner-avatar" alt="用户头像" />
        <span v-else class="banner-avatar">👤</span>
        <span class="banner-username">{{ username }}</span>
        <button class="logout-btn" @click="outlogin">退出登录</button>
      </div>
    </div>
    <aside class="sidebar-modern">
      <ul class="sidebar-menu">
        <li :class="{ active: $route.path === '/admin' || $route.path === '/admin/' }" @click="router.push('/admin')">
          <span class="menu-icon">🏠</span> 首页
        </li>
        <li :class="{ active: $route.path === '/admin/competition' }" @click="router.push('/admin/competition')">
          <span class="menu-icon">📅</span> 竞赛管理
        </li>
        <li :class="{ active: $route.path === '/admin/users' }" @click="router.push('/admin/users')">
          <span class="menu-icon">👥</span> 用户管理
        </li>
        <li :class="{ active: $route.path === '/admin/team' }" @click="router.push('/admin/team')">
          <span class="menu-icon">👨‍👩‍👧‍👦</span> 队伍管理
        </li>
        <li :class="{ active: $route.path === '/admin/inform' }" @click="router.push('/admin/inform')">
          <span class="menu-icon">📢</span> 通告管理
        </li>
        <li :class="{ active: $route.path === '/admin/news' }" @click="router.push('/admin/news')">
          <span class="menu-icon">📰</span> 新闻管理
        </li>
      </ul>
    </aside>
    <div class="main-modern">
      <section class="content">
        <router-view />
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from "vue-router"
import { getUserInfo } from '@/apis/user.js'
import { usePersonStore } from "@/stores/person";

const router = useRouter()
const username = ref('')
const userAvatar = ref('')

onMounted(async () => {
  const result = await getUserInfo();
  if (result.code == 1) {
    username.value = result.data.username;
    userAvatar.value = result.data.avatarUrl;
  }
});

const outlogin = () => {
  localStorage.removeItem('jwt'); // 如果你使用了 token 认证
  const personStore = usePersonStore();
  personStore.resetUser();
  router.push('/login'); // 确保你已经在项目中配置了 Vue Router，并且登录页面的路径是 `/login`
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
  background: #f6f8fa;
}

.top-banner,
.sidebar {
  display: none;
}

.top-banner-modern {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 56px;
  background: #fff;
  color: #222e3c;
  display: flex;
  align-items: center;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border-bottom: 1px solid #f0f0f0;
}

.banner-left {
  display: flex;
  align-items: center;
  margin-left: 12px;
}

.banner-logo {
  width: 32px;
  height: 32px;
  margin-right: 8px;
}

.banner-system-name {
  font-size: 18px;
  font-weight: bold;
  letter-spacing: 1px;
}

.banner-right {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: auto;
  margin-right: 16px;
}

.banner-notify {
  position: relative;
}

.notify-icon {
  font-size: 20px;
  color: #888;
}

.notify-badge {
  position: absolute;
  top: -7px;
  right: -10px;
  background: #ff5252;
  color: #fff;
  font-size: 12px;
  border-radius: 10px;
  padding: 1px 6px;
  font-weight: bold;
}

.banner-avatar {
  font-size: 22px;
  background: #e3f2fd;
  color: #1976d2;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  object-fit: cover;
}

.banner-username {
  font-size: 15px;
  color: #222e3c;
  font-weight: 600;
  margin-left: 6px;
}

.logout-btn {
  margin-left: 12px;
  background: #f5f7fa;
  color: #1976d2;
  border: none;
  border-radius: 8px;
  padding: 6px 16px;
  font-size: 14px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s;
}

.logout-btn:hover {
  background: #e3e8ee;
}

.sidebar-modern {
  width: 180px;
  background: #fff;
  color: #222e3c;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding-top: 0;
  position: fixed;
  top: 56px;
  left: 0;
  height: calc(100vh - 56px);
  z-index: 9;
  border-right: 1px solid #f0f0f0;
}

.sidebar-logo-area {
  display: flex;
  align-items: center;
  height: 64px;
  width: 100%;
  padding-left: 32px;
  border-bottom: 1px solid #f0f0f0;
}

.sidebar-logo {
  width: 38px;
  height: 38px;
  margin-right: 14px;
}

.sidebar-system-name {
  font-size: 22px;
  font-weight: bold;
  color: #1976d2;
  letter-spacing: 1px;
}

.sidebar-menu {
  list-style: none;
  padding: 0;
  width: 100%;
  margin-top: 16px;
}

.sidebar-menu li {
  font-size: 15px;
  padding: 10px 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 18px;
  transition: background 0.2s, color 0.2s;
  border-left: 4px solid transparent;
}

.sidebar-menu li.active,
.sidebar-menu li:hover {
  background: #f5f7fa;
  color: #1976d2;
  border-left: 3px solid #1976d2;
}

.menu-icon {
  font-size: 26px;
}

.main-modern {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-left: 180px;
  margin-top: 56px;
  min-width: 0;
}

.header {
  height: 60px;
  background: #fff;
  display: flex;
  align-items: center;
  padding: 0 32px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
  font-size: 18px;
}

.content {
  flex: 1;
  padding: 48px 32px 32px 32px;
  overflow-y: auto;
}

.dashboard {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.dashboard-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #222e3c;
}

.dashboard-desc {
  color: #888;
  font-size: 16px;
  margin-bottom: 24px;
}

.dashboard-cards {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

.dashboard-card {
  flex: 1;
  border-radius: 16px;
  color: #fff;
  padding: 24px 24px 18px 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: relative;
  overflow: hidden;
  min-width: 180px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.card-blue {
  background: linear-gradient(135deg, #1976d2 60%, #42a5f5 100%);
}

.card-orange {
  background: linear-gradient(135deg, #ff9800 60%, #ffc107 100%);
}

.card-green {
  background: linear-gradient(135deg, #43a047 60%, #66bb6a 100%);
}

.card-red {
  background: linear-gradient(135deg, #d32f2f 60%, #ff5252 100%);
}

.card-title {
  font-size: 16px;
  margin-bottom: 8px;
  opacity: 0.9;
}

.card-value {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 8px;
}

.card-desc {
  font-size: 13px;
  opacity: 0.85;
}

.card-icon {
  position: absolute;
  right: 18px;
  bottom: 18px;
  font-size: 32px;
  opacity: 0.18;
}

.dashboard-main {
  display: flex;
  gap: 32px;
  flex-wrap: wrap;
}

.dashboard-table-area {
  flex: 2;
  min-width: 340px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 24px;
}

.dashboard-table-title {
  font-size: 17px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #304156;
}

.dashboard-table {
  width: 100%;
  border-collapse: collapse;
}

.dashboard-table th,
.dashboard-table td {
  padding: 10px 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.dashboard-table th {
  background-color: #f5f7fa;
  font-weight: bold;
  color: #555;
}

.dashboard-table tr:last-child td {
  border-bottom: none;
}

.dashboard-chart-area {
  flex: 1.2;
  min-width: 320px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.chart {
  width: 100%;
  height: 240px;
  margin-top: 12px;
}

.tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: bold;
}

.ongoing {
  background-color: #e1f3d8;
  color: #67c23a;
}

.ended {
  background-color: #fde2e2;
  color: #f56c6c;
}

.upcoming {
  background-color: #e6f7ff;
  color: #409eff;
}

.btn-sm {
  background-color: #e0e0e0;
  color: #333;
  padding: 6px 12px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-size: 13px;
  transition: background-color 0.2s;
}

.btn-sm:hover {
  background-color: #d0d0d0;
}
</style>
