<template>
  <headerVue :isRightShow="false" />
  <div class="common-layout">
    <el-container class="layout-container">
      <!-- 移动端顶部导航栏 -->
      <div v-if="isMobile" class="mobile-nav">
        <el-menu
          :default-active="activeMenu"
          class="mobile-menu"
          mode="horizontal"
          background-color="#fff"
          text-color="#303133"
          active-text-color="#165dff"
          @select="handleMenuSelect"
        >
          <template v-for="(item, idx) in visibleMenus">
            <el-menu-item
              :index="item.index"
              v-if="!item.isDropdown"
              :key="item.index"
            >
              <el-icon class="mobile-menu-icon">
                <component :is="item.icon" />
              </el-icon>
              <span>{{ item.label }}</span>
            </el-menu-item>
          </template>
          <el-menu-item
            v-if="dropdownMenus.length"
            index="__dropdown__"
            disabled
            style="padding: 0"
          >
            <el-dropdown @command="handleDropdownCommand">
              <span
                class="el-dropdown-link"
                style="display: flex; align-items: center"
              >
                <el-icon class="mobile-menu-icon"><MoreFilled /></el-icon>
                <span>更多</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-for="item in dropdownMenus"
                    :key="item.index"
                    :command="item.index"
                  >
                    <el-icon class="mobile-menu-icon">
                      <component :is="item.icon" />
                    </el-icon>
                    <span>{{ item.label }}</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-menu-item>
        </el-menu>
      </div>

      <el-container class="layout-body">
        <!-- 左侧侧边栏（桌面端） -->
        <el-aside
          :width="isMobile ? '0px' : '200px'"
          class="layout-aside-left custom-aside"
          v-show="!isMobile"
        >
          <div class="left-aside">
            <el-menu
              :default-active="$route.path"
              class="aside-menu"
              router
              mode="vertical"
              background-color="#fff"
              text-color="#303133"
              active-text-color="#165dff"
              :collapse-transition="false"
            >
              <el-menu-item index="/personal/account">
                <el-icon class="menu-icon">
                  <Avatar />
                </el-icon>
                <span>个人主页</span>
              </el-menu-item>
              <el-menu-item index="/personal/competition">
                <el-icon class="menu-icon">
                  <Menu />
                </el-icon>
                <span>我的竞赛</span>
              </el-menu-item>
              <el-menu-item index="/personal/group">
                <el-icon class="menu-icon">
                  <Flag />
                </el-icon>
                <span>我的团队</span>
              </el-menu-item>
              <el-menu-item index="/personal/judge">
                <el-icon class="menu-icon">
                  <Notebook />
                </el-icon>
                <span>我的评审</span>
              </el-menu-item>
              <el-menu-item index="/personal/message">
                <el-icon class="menu-icon">
                  <Promotion />
                </el-icon>
                <span>我的消息</span>
              </el-menu-item>
              <el-menu-item index="/personal/honor">
                <el-icon class="menu-icon">
                  <TrendCharts />
                </el-icon>
                <span>我的荣誉</span>
              </el-menu-item>
              <!-- 退出登录 -->
              <el-menu-item index="/personal/setting">
                <el-icon class="menu-icon">
                  <Setting />
                </el-icon>
                <span>设置</span>
              </el-menu-item>
              <el-menu-item @click="logout">
                <el-icon class="menu-icon">
                  <Bell />
                </el-icon>
                <span>退出登录</span>
              </el-menu-item>
            </el-menu>
          </div>
        </el-aside>

        <!-- 主内容区 -->
        <el-main class="layout-main">
          <router-view />
        </el-main>

        <!-- 右侧信息栏（桌面端非平板） -->
        <el-aside
          :width="isMobile ? '0px' : '260px'"
          class="layout-aside-right custom-right-aside"
          v-show="!isMobile && !isTablet"
        >
          <div class="right-aside">
            <!-- 新闻卡片 -->
            <el-card class="aside-card news-card">
              <div class="card-header">
                <h3 class="card-title">新闻</h3>
                <!-- 查看全部 -->
                <el-button type="text" class="more-btn" @click="getAll"
                  >查看全部</el-button
                >
              </div>
              <el-divider />
              <ul v-if="newsList.length" class="notice-list">
                <li
                  v-for="(item, index) in newsList.slice(0, 4)"
                  :key="index"
                  class="notice-item"
                >
                  <div class="notice-dot"></div>
                  <div class="notice-content" @click="onClick1(item.id)">
                    <p class="notice-title">{{ item.title }}</p>
                  </div>
                </li>
              </ul>
              <div v-else class="news-empty">暂无新闻</div>
            </el-card>

            <!-- 通告卡片 -->
            <el-card class="aside-card">
              <div class="card-header">
                <h3 class="card-title">通告</h3>
                <el-button type="text" class="more-btn" @click="getNotices"
                  >查看全部</el-button
                >
              </div>
              <el-divider />
              <ul class="notice-list">
                <li
                  v-for="(item, index) in notices.slice(0, 4)"
                  :key="index"
                  class="notice-item"
                >
                  <div class="notice-dot"></div>
                  <div class="notice-content">
                    <p class="notice-title" @click="onClick(item.id)">
                      {{ item.title }}
                    </p>
                  </div>
                </li>
              </ul>
            </el-card>
          </div>
        </el-aside>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import router from "../../router";
import headerVue from "@/libs/header/index.vue";
import {
  Avatar,
  Menu,
  Promotion,
  Setting,
  Notebook,
  Flag,
  TrendCharts,
  Bell,
  MoreFilled,
} from "@element-plus/icons-vue";
import { getNews, getNotice } from "@/apis/person";
import { ref, onMounted, watch, computed, nextTick } from "vue";
import { ElMessageBox, ElMessage } from "element-plus";
import { usePersonStore } from "@/stores/person";
const personStore = usePersonStore();

// 跳转通知详情
const onClick = (id) => {
  router.push({
    path: "/informContent",
    query: { id: id, Type: 1 },
  });
};
// 跳转新闻详情
const onClick1 = (id) => {
  router.push({
    path: "/informContent",
    query: { id: id, Type: 2 },
  });
};
const getAll = () => {
  router.push({
    path: "/inform",
    query: { Type: 2 },
  });
};
const getNotices = () => {
  router.push({
    path: "/inform",
    query: { Type: 1 },
  });
};

// 数据存储
const newsList = ref([]); // 新闻数据
const notices = ref([]); // 通告数据

// 设备状态
const isMobile = ref(false); // 手机设备
const isTablet = ref(false); // 平板设备

// 计算当前激活的菜单项
const activeMenu = computed(() => {
  return router.currentRoute.value.path;
});

// 菜单项数据化
const allMenus = [
  { index: "/personal/account", label: "个人主页", icon: Avatar },
  { index: "/personal/competition", label: "我的竞赛", icon: Menu },
  { index: "/personal/group", label: "我的团队", icon: Flag },
  { index: "/personal/judge", label: "我的评审", icon: Notebook },
  { index: "/personal/message", label: "我的消息", icon: Promotion },
  { index: "/personal/honor", label: "我的荣誉", icon: TrendCharts },
  { index: "/personal/setting", label: "设置", icon: Setting },
  { index: "logout", label: "退出登录", icon: Bell },
];

const visibleMenus = ref([]); // 当前横向可见菜单
const dropdownMenus = ref([]); // 省略号下拉菜单
const menuMaxCount = ref(0); // 横向最大可见菜单数

// 计算横向可见菜单数
const calcMenuCount = () => {
  // 估算每个菜单宽度，超出就进下拉
  const menuWidth = 80; // 估算每个菜单宽度（含icon+padding）
  const moreWidth = 60; // 省略号宽度
  const navWidth =
    document.querySelector(".mobile-nav")?.offsetWidth || window.innerWidth;
  let maxCount = Math.floor((navWidth - moreWidth) / menuWidth);
  if (maxCount < 1) maxCount = 1;
  menuMaxCount.value = maxCount;
  if (allMenus.length > maxCount) {
    visibleMenus.value = allMenus.slice(0, maxCount - 1);
    dropdownMenus.value = allMenus.slice(maxCount - 1);
  } else {
    visibleMenus.value = allMenus;
    dropdownMenus.value = [];
  }
};

// 监听窗口变化
const handleResize = () => {
  if (isMobile.value) {
    nextTick(() => {
      calcMenuCount();
    });
  }
};

// 设备检测逻辑
const checkDeviceSize = () => {
  const width = window.innerWidth;
  isMobile.value = width < 768;
  isTablet.value = width >= 768 && width < 1024;
};

// 移动端菜单选择处理
const handleMenuSelect = (index) => {
  if (index === "logout") {
    logout();
  } else if (index !== "__dropdown__") {
    router.push(index);
  }
};

// 下拉菜单选择
const handleDropdownCommand = (index) => {
  handleMenuSelect(index);
};

//退出登录
const logout = () => {
  ElMessageBox.confirm(
    "是否确认退出登录？用户进行的其他操作不会保存！",
    "提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(() => {
      localStorage.removeItem("loginUser");
      localStorage.removeItem("jwt");
      personStore.resetUser();
      ElMessage.success("退出登录成功");
      router.push("/index");
    })
    .catch(() => {
      ElMessage.info("取消退出登录");
    });
};
// 异步数据加载
const loadData = async () => {
  try {
    // 获取新闻
    const newsRes = await getNews({ pageNo: 1, pageSize: 5 });
    if (newsRes.code) {
      newsList.value = newsRes.data.records;
    }

    // 获取通告
    const noticeRes = await getNotice({ pageNo: 1, pageSize: 5 });
    if (noticeRes.code) {
      notices.value = noticeRes.data.records;
    }
  } catch (error) {
    console.error("数据加载失败:", error);
  }
};

// 生命周期
onMounted(() => {
  checkDeviceSize();
  loadData();
  window.addEventListener("resize", checkDeviceSize);
  window.addEventListener("resize", handleResize);
  nextTick(() => {
    if (isMobile.value) calcMenuCount();
  });
});

// 路由变化监听
watch(
  () => router.currentRoute.value,
  () => {
    // 路由变化时的处理逻辑
  }
);
</script>

<style scoped>
.common-layout {
  min-height: 100vh;
  background-color: #f7f8fa;
  overflow-x: hidden;
}

html.dark .common-layout {
  background-color: #333;
}

.layout-container {
  min-height: 100vh;
  flex-direction: column;
}

/* 移动端顶部导航栏 */
.mobile-nav {
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  padding: 0 8px;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.mobile-menu {
  border-bottom: none;
  display: flex;
  flex-wrap: nowrap;
  min-width: max-content;
}

.mobile-menu .el-menu-item {
  height: 48px !important;
  line-height: 48px !important;
  font-size: 14px;
  padding: 0 12px;
  white-space: nowrap;
  border-bottom: 2px solid transparent;
}

.mobile-menu .el-menu-item.is-active {
  color: #165dff !important;
  background: transparent !important;
  border-bottom: 2px solid #165dff !important;
}

.mobile-menu-icon {
  font-size: 18px;
  margin-right: 6px;
  vertical-align: middle;
}

.layout-body {
  display: flex;
  flex-direction: row;
  gap: 12px;
  padding: 8px 0;
  align-items: flex-start;
}

.custom-aside {
  background: #fff;
  border-right: 1px solid #f0f0f0;
  min-height: 600px;
}

/* html.dark .custom-aside {
  background-color: #444343;
} */

.aside-menu {
  border-right: none;
  padding-top: 8px;
}

/* html.dark .aside-menu {
  background-color: #444343;
} */

.menu-icon {
  font-size: 22px;
  margin-right: 16px;
}

.el-menu-item {
  height: 56px !important;
  line-height: 56px !important;
  font-size: 17px;
  color: #222;
  border-left: 3px solid transparent;
  transition: background 0.2s, border-color 0.2s;
}

.el-menu-item.is-active {
  color: #165dff !important;
  background: #f6faff !important;
  border-left: 3px solid #165dff !important;
}

.layout-main {
  flex: 1;
  padding: 0;
  background: transparent;
}

.custom-right-aside {
  min-width: 260px;
  width: 260px;
  max-width: 260px;
}

.right-aside {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.aside-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(22, 93, 255, 0.06);
  background: #fff;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2px 12px;
}

.card-title {
  color: black;
  font-size: 16px;
  font-weight: 500;
  margin: 0;
}

.news-empty {
  color: #aaa;
  text-align: center;
  padding: 24px 0;
  font-size: 15px;
}

.notice-list {
  padding: 8px 16px;
  list-style: none;
  margin: 0;
}

.notice-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.notice-dot {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background-color: #409eff;
  margin-right: 12px;
}

.notice-content {
  flex: 1;
}

.notice-title {
  font-size: 14px;
  color: #333;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 96px;
  /* 约6个中文字符宽度 */
  transition: color 0.2s;
  /* 平滑过渡效果 */
}

.notice-title:hover {
  color: #165dff;
  /* 蓝色高亮 */
  cursor: pointer;
}

/* 响应式适配 */
@media (max-width: 767px) {
  .layout-body {
    flex-direction: column;
    gap: 0;
    padding: 0 4px;
  }

  .custom-aside,
  .custom-right-aside {
    display: none;
  }

  .layout-main {
    padding: 8px 4px;
  }

  /* 移动端滚动优化 */
  .mobile-nav {
    position: sticky;
    top: 0;
    z-index: 100;
  }

  /* 隐藏滚动条但保持功能 */
  .mobile-nav::-webkit-scrollbar {
    display: none;
  }

  .mobile-nav {
    -ms-overflow-style: none;
    scrollbar-width: none;
  }
}

/* 平板端适配 */
@media (min-width: 768px) and (max-width: 1023px) {
  .layout-body {
    gap: 8px;
    padding: 8px 4px;
  }

  .custom-right-aside {
    display: none;
  }

  .layout-main {
    padding: 0 4px;
  }
}
</style>
