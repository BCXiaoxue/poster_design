<template>
  <div ref="bodyContainer" class="body">
    <div class="header">
      <header-vue :isRightShow="true"></header-vue>
    </div>

    <div class="main">
      <loading-vue v-if="isLoading"></loading-vue>
      <div class="container" v-else>
        <!-- 页面头部 -->
        <div class="hero-section">
          <div class="hero-content">
            <div class="hero-text">
              <h1 class="hero-title">
                {{ targetList.competitionName }}
              </h1>
              <p class="hero-subtitle">全国大学生海报设计大赛</p>
              <div class="hero-status">
                <span class="status-badge" :class="getStatusClass()">
                  {{ currentStatus }}
                </span>
              </div>
            </div>
            <div class="hero-visual">
              <div class="floating-elements">
                <div class="floating-circle circle-1"></div>
                <div class="floating-circle circle-2"></div>
                <div class="floating-circle circle-3"></div>
              </div>
            </div>
          </div>
        </div>

        <div class="main-card">
          <!-- 竞赛介绍和须知 -->
          <div class="main-section main-section-bg1">
            <div class="section-header">
              <el-icon><InfoFilled /></el-icon>
              <span class="section-title">竞赛介绍</span>
            </div>
            <div class="section-content">
              {{ targetList.introduction || "暂无介绍" }}
            </div>
            <div class="section-divider"></div>
            <div class="section-header">
              <el-icon><Document /></el-icon>
              <span class="section-title">竞赛须知</span>
            </div>
            <div class="section-content">
              {{ targetList.notes || "暂无须知" }}
            </div>
          </div>
          <!-- 关键信息 -->
          <div class="main-section main-section-bg2">
            <div class="section-header">
              <el-icon><User /></el-icon>
              <span class="section-title">参赛人数</span>
            </div>
            <div class="section-content">
              {{ targetList.people || "暂无数据" }}
            </div>
            <div class="section-divider"></div>
            <div class="section-header">
              <el-icon><Calendar /></el-icon>
              <span class="section-title">报名时间</span>
            </div>
            <div class="section-content">
              {{ targetList.registrationBegin || "待定" }} -
              {{ targetList.registrationEnd || "待定" }}
            </div>
          </div>
          <!-- 赛程安排 -->
          <div class="main-section main-section-bg1">
            <div class="section-header">
              <el-icon><Clock /></el-icon>
              <span class="section-title">赛程安排</span>
            </div>
            <div class="section-content">
              {{ targetList.schedule || "暂无安排" }}
            </div>
          </div>
          <!-- 立即报名卡片 -->
          <div v-show="isSignShow" class="signup-section">
            <div class="signup-content">
              <div class="signup-icon-bg">
                <el-icon><Trophy /></el-icon>
              </div>
              <div class="signup-texts">
                <div class="signup-title">准备参赛？</div>
                <div class="signup-desc">点击下方按钮立即报名参加比赛</div>
                <div class="signup-tip">让你的创意闪耀全国！</div>
              </div>
            </div>
            <el-button
              :disabled="isEnlist"
              @click="onClickSignup(route.params.id)"
              class="signup-button"
              :class="{ enlisted: isEnlist }"
            >
              <el-icon v-if="!isEnlist"><Plus /></el-icon>
              <el-icon v-else><Check /></el-icon>
              {{ isEnlist ? "已报名" : "立刻报名" }}
            </el-button>
            <svg class="signup-bg-svg" viewBox="0 0 200 200">
              <circle cx="100" cy="100" r="100" fill="#fff" />
            </svg>
          </div>
          <!-- 图片展示 -->
          <div
            v-if="targetList.images && targetList.images.length > 0"
            class="main-section main-section-bg2"
          >
            <div class="section-header">
              <el-icon><Picture /></el-icon>
              <span class="section-title">相关图片</span>
            </div>
            <div class="images-showcase">
              <div
                v-for="(src, index) in targetList.images"
                :key="index"
                class="showcase-item"
              >
                <img v-lazy :src="src" alt="竞赛图片" class="showcase-image" />
              </div>
            </div>
          </div>
          <!-- 作品展示 -->
          <div v-if="isWorkShow" class="main-section main-section-bg1">
            <div class="section-header">
              <el-icon><Collection /></el-icon>
              <span class="section-title">作品展示</span>
            </div>
            <div class="works-subtitle">查看参赛选手的优秀作品</div>
            <list-vue></list-vue>
          </div>
        </div>
      </div>
    </div>
    <aside-vue :container="bodyContainer" v-if="isAsideShow"></aside-vue>
    <footer-vue></footer-vue>
  </div>
</template>

<script setup>
import headerVue from "@/libs/header/index.vue";
import footerVue from "@/libs/footer/index.vue";
import { getCompetitionById, getCaptainEnlist } from "../../apis/competition";
import { ref, onMounted, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import listVue from "./components/list/index.vue";
import asideVue from "../main/componets/aside/index.vue";
import { isMobile } from "@/utils/flexible.js";
import dayjs from "dayjs";
import loadingVue from "@/libs/loading/index.vue";
import {
  InfoFilled,
  Document,
  User,
  Calendar,
  Clock,
  Picture,
  Collection,
  Plus,
  Check,
  Trophy,
} from "@element-plus/icons-vue";

const isLoading = ref(true);
const targetList = ref({});
const bodyContainer = ref(null);
const isAsideShow = ref(true);
const route = useRoute();
const router = useRouter();
const currentStatus = ref("");
const isSignShow = ref(true);
const isWorkShow = ref(true);
const isEnlist = ref(false);

watch(
  () => isMobile.value,
  () => {
    isAsideShow.value = !isMobile.value;
  },
  { immediate: true }
);

const getCompetition = async () => {
  try {
    const res = await getCompetitionById(route.params.id);
    if (res.code === 1 && res.data) {
      targetList.value = res.data;
      checkStatus();
    } else {
      ElMessage.error(res.msg || "获取竞赛信息失败");
    }
  } catch (error) {
    console.error("接口错误：", error);
    ElMessage.error("请求失败，请稍后再试");
  }
};

const getEnlist = async () => {
  try {
    const res = await getCaptainEnlist(route.params.id);
    if (res.code === 1 && res.data) {
      isEnlist.value = res.data;
    }
  } catch (error) {
    console.error("接口错误：", error);
    ElMessage.error("请求失败，请稍后再试");
  }
};

onMounted(async () => {
  await getCompetition();
  await getEnlist();
  isLoading.value = false;
});

// 判断状态
const checkStatus = () => {
  const now = dayjs();
  const begin = dayjs(targetList.value.registrationBegin);
  const end = dayjs(targetList.value.registrationEnd);
  const activityEnd = dayjs(targetList.value.activityEnd);

  if (now.isBefore(begin)) {
    currentStatus.value = "未开始";
    isSignShow.value = false;
    isWorkShow.value = false;
  } else if (now.isBefore(end) || now.isSame(end)) {
    currentStatus.value = "报名中";
    isSignShow.value = true;
    isWorkShow.value = false;
  } else if (now.isBefore(activityEnd) || now.isSame(activityEnd)) {
    currentStatus.value = "评审中";
    isSignShow.value = false;
    isWorkShow.value = true;
  } else {
    currentStatus.value = "已结束";
    isSignShow.value = false;
    isWorkShow.value = true;
  }
};

// 获取状态样式类
const getStatusClass = () => {
  switch (currentStatus.value) {
    case "报名中":
      return "status-active";
    case "评审中":
      return "status-reviewing";
    case "已结束":
      return "status-ended";
    case "未开始":
      return "status-pending";
    default:
      return "status-default";
  }
};

const onClickSignup = (id) => {
  router.push(`/competition/signup/${id}`);
};
</script>

<style scoped>
.body {
  min-height: 100vh;
  overflow-y: auto;
}

.main {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 50%, #cbd5e1 100%);
  padding: 0;
}

html.dark .main {
  background: linear-gradient(135deg, #333 0%, #333 50%, #333 100%);
}

.container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 1rem;
  background: #f8fafc;
  border-radius: 0;
}

.main-card {
  background: transparent;
  border-radius: 0;
  box-shadow: none;
  padding: 2.5rem 2rem;
  /* margin-bottom: 2rem; */
  display: flex;
  flex-direction: column;
  gap: 0;
  overflow: visible;
  padding-top: 1rem;
}

html.dark .container {
  background-color: #444343;
}

.main-section,
.main-section-bg1,
.main-section-bg2 {
  background: transparent !important;
  border-radius: 0 !important;
  box-shadow: none;
  transition: none;
}

.main-section {
  padding: 2rem 0 1.5rem 0;
  margin: 0;
  border: none;
  box-shadow: none;
  transition: background 0.3s, border-radius 0.3s;
  animation: fadeInSection 0.7s cubic-bezier(0.4, 0, 0.2, 1);
}

.main-section-bg1 {
  background: linear-gradient(180deg, #f8fafc 80%, #f1f5fa 100%);
}

.main-section-bg2 {
  background: linear-gradient(180deg, #f1f5fa 80%, #f8fafc 100%);
}

.main-section:first-child {
  border-top-left-radius: 2rem;
  border-top-right-radius: 2rem;
}

.main-section:last-child {
  border-bottom-left-radius: 2rem;
  border-bottom-right-radius: 2rem;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.5rem;
  font-weight: 700;
  color: #1b78f7;
  margin-bottom: 0.7rem;
  opacity: 0.98;
}

.section-header .el-icon {
  font-size: 2.1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.section-title {
  font-size: 1.45rem;
  color: #1a202c;
  font-weight: 700;
  letter-spacing: 0.5px;
  display: flex;
  align-items: center;
}

html.dark .section-title {
  color: white;
}

.section-content {
  font-size: 1.05rem;
  color: #4a5568;
  line-height: 1.8;
  margin-bottom: 0.5rem;
  white-space: pre-line;
  opacity: 0.97;
}

html.dark .section-content {
  color: white;
}

.section-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, #e2e8f0 60%, transparent);
  margin: 1.2rem 0 1.2rem 0;
  border: none;
  border-top: 1px dashed #e2e8f0;
  opacity: 0.7;
}

.images-showcase {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-top: 1rem;
  align-items: flex-start;
}

.showcase-item {
  background: transparent !important;
  margin: 0;
  padding: 0;
  box-shadow: none !important;
  flex: 0 0 auto;
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  border-radius: 0 !important;
  overflow: visible !important;
}

.showcase-image {
  display: block;
  max-width: 100%;
  max-height: 400px;
  width: auto !important;
  height: auto !important;
  object-fit: unset !important;
  border-radius: 0 !important;
  box-shadow: none !important;
}

.works-subtitle {
  font-size: 1rem;
  color: #64748b;
  margin-bottom: 1rem;
}

/* 英雄区域 */
.hero-section {
  background: linear-gradient(135deg, #1b78f7 0%, #409eff 50%, #66b1ff 100%);
  padding: 3rem 2rem;
  position: relative;
  overflow: hidden;
  max-width: 1280px;
  margin: 0 auto 1rem auto;
  border-radius: 0;
  box-sizing: border-box;
}

html.dark .hero-section {
  background: linear-gradient(135deg, darkblue 0%, #074eb2 50%, #1066bc 100%);
}

.hero-content {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
  gap: 3rem;
}

.hero-text {
  flex: 1;
  color: white;
}

.hero-title {
  font-size: 2.25rem;
  font-weight: 600;
  margin: 0 0 1rem 0;
  line-height: 1.3;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  letter-spacing: -0.5px;
}

.hero-subtitle {
  font-size: 1.125rem;
  margin: 0 0 1.5rem 0;
  opacity: 0.85;
  font-weight: 400;
  letter-spacing: 0.5px;
}

.hero-status {
  display: inline-block;
}

.status-badge {
  padding: 0.5rem 1.5rem;
  border-radius: 2rem;
  font-size: 0.875rem;
  font-weight: 600;
  color: white;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.status-active {
  background: linear-gradient(135deg, #10b981, #059669);
}

.status-ended {
  background: linear-gradient(135deg, #ef4444, #dc2626);
}

.status-pending {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.status-reviewing {
  background: linear-gradient(135deg, #6366f1, #7c3aed);
}

.status-default {
  background: linear-gradient(135deg, #6b7280, #4b5563);
}

.hero-visual {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.floating-elements {
  position: relative;
  width: 200px;
  height: 200px;
}

.floating-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 80px;
  height: 80px;
  top: 10px;
  left: 10px;
  animation-delay: 0s;
}

.circle-2 {
  width: 60px;
  height: 60px;
  top: 80px;
  right: 20px;
  animation-delay: 2s;
}

.circle-3 {
  width: 40px;
  height: 40px;
  bottom: 20px;
  left: 40px;
  animation-delay: 4s;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

/* 信息区域 */
.info-section {
  padding: 3rem 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

/* 信息块 */
.info-block {
  margin-bottom: 3rem;
  background: white;
  border-radius: 1.5rem;
  padding: 2.5rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border-left: 6px solid #1b78f7;
}

.block-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.header-icon {
  width: 3rem;
  height: 3rem;
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border-radius: 0.75rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.25rem;
}

.block-title {
  font-size: 1.75rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0;
}

.content-text {
  color: #4a5568;
  line-height: 1.8;
  font-size: 1.125rem;
  margin: 0;
  white-space: pre-line;
}

.content-section {
  margin-bottom: 2rem;
}

.content-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0 0 1rem 0;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.section-title .el-icon {
  color: #1b78f7;
  font-size: 1.125rem;
}

.content-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, #e2e8f0, transparent);
  margin: 2rem 0;
}

/* 关键信息行 */
.key-info-row {
  display: flex;
  gap: 2rem;
  margin-bottom: 3rem;
}

.key-info-item {
  flex: 1;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-radius: 1rem;
  padding: 2rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.key-info-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.info-icon {
  width: 3rem;
  height: 3rem;
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border-radius: 0.75rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.25rem;
  flex-shrink: 0;
}

.info-details {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.info-label {
  font-size: 0.875rem;
  color: #64748b;
  font-weight: 500;
}

.info-value {
  font-size: 1.125rem;
  color: #1a202c;
  font-weight: 600;
}

/* 赛程安排 */
.schedule-section {
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  border-radius: 1.5rem;
  padding: 2.5rem;
  margin-bottom: 3rem;
  border: 1px solid #e2e8f0;
}

.schedule-header {
  margin-bottom: 2rem;
}

.schedule-title {
  font-size: 1.75rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.schedule-title .el-icon {
  color: #1b78f7;
  font-size: 1.5rem;
}

.timeline {
  position: relative;
  padding-left: 2rem;
}

.timeline::before {
  content: "";
  position: absolute;
  left: 0.5rem;
  top: 0;
  bottom: 0;
  width: 2px;
  background: linear-gradient(180deg, #1b78f7, #409eff);
}

.timeline-item {
  position: relative;
  margin-bottom: 1.5rem;
}

.timeline-dot {
  position: absolute;
  left: -1.5rem;
  top: 0.5rem;
  width: 1rem;
  height: 1rem;
  background: #1b78f7;
  border-radius: 50%;
  border: 3px solid white;
  box-shadow: 0 0 0 3px #1b78f7;
}

.timeline-content {
  background: white;
  padding: 1.5rem;
  border-radius: 0.75rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.timeline-content p {
  margin: 0;
  color: #4a5568;
  line-height: 1.6;
  font-size: 1rem;
}

/* 报名区域 */
.signup-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #2d8cf0 0%, #409eff 100%);
  border-radius: 2.5rem;
  box-shadow: 0 8px 32px rgba(27, 120, 247, 0.15);
  padding: 2.5rem 3rem;
  position: relative;
  overflow: hidden;
  margin: 0 2rem 3rem;
  z-index: 1;
}

html.dark .signup-section {
  background: linear-gradient(135deg, darkblue 0%, #074eb2 100%);
}

.signup-content {
  display: flex;
  align-items: center;
  gap: 2rem;
  z-index: 2;
}

.signup-icon-bg {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #fff 0%, #b3d8ff 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  color: #409eff;
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.15);
}

.signup-texts {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.signup-title {
  font-size: 2rem;
  font-weight: bold;
  color: #fff;
}

.signup-desc {
  font-size: 1.125rem;
  color: #e3f1ff;
  margin-top: 0.5rem;
}

.signup-tip {
  font-size: 1rem;
  color: #fff;
  margin-top: 0.5rem;
  opacity: 0.8;
}

.signup-button {
  font-size: 1.25rem;
  font-weight: 600;
  padding: 1rem 2.5rem;
  border-radius: 2rem;
  background: #fff;
  color: #409eff;
  box-shadow: 0 4px 16px rgba(64, 158, 255, 0.15);
  transition: all 0.2s;
  z-index: 2;
}

.signup-button:hover:not(:disabled) {
  background: #e6f2ff;
  color: #1b78f7;
  transform: translateY(-2px) scale(1.05);
}

.signup-button:disabled,
.signup-button.enlisted {
  background: #10b981;
  color: #fff;
}

.signup-bg-svg {
  position: absolute;
  right: -60px;
  bottom: -60px;
  z-index: 0;
  width: 200px;
  height: 200px;
  opacity: 0.15;
  pointer-events: none;
}

/* 移动端适配 - 平板设备 */
@media (max-width: 1024px) {
  .container {
    padding: 0 0.75rem;
  }

  .main-card {
    padding: 2rem 1.5rem;
  }

  .hero-section {
    padding: 2.5rem 1.5rem;
  }

  .hero-title {
    font-size: 2rem;
  }

  .hero-subtitle {
    font-size: 1rem;
  }

  .floating-elements {
    width: 180px;
    height: 180px;
  }

  .section-header {
    font-size: 1.35rem;
  }

  .section-header .el-icon {
    font-size: 1.9rem;
  }

  .section-title {
    font-size: 1.3rem;
  }

  .section-content {
    font-size: 1rem;
  }
}

/* 移动端适配 - 手机设备 */
@media (max-width: 768px) {
  .container {
    padding: 0 0.5rem;
  }

  .main-card {
    padding: 1.5rem 1rem;
  }

  .hero-section {
    padding: 2rem 1rem;
    margin: 0 auto 0.5rem auto;
  }

  .hero-content {
    flex-direction: column;
    text-align: center;
    gap: 1.5rem;
  }

  .hero-title {
    font-size: 1.75rem;
    margin: 0 0 0.75rem 0;
  }

  .hero-subtitle {
    font-size: 0.875rem;
    margin: 0 0 1rem 0;
  }

  .floating-elements {
    width: 150px;
    height: 150px;
  }

  .floating-circle {
    animation: float 4s ease-in-out infinite;
  }

  .circle-1 {
    width: 60px;
    height: 60px;
  }

  .circle-2 {
    width: 45px;
    height: 45px;
  }

  .circle-3 {
    width: 30px;
    height: 30px;
  }

  .main-section {
    padding: 1.5rem 0 1rem 0;
  }

  .section-header {
    font-size: 1.25rem;
    gap: 0.5rem;
    margin-bottom: 0.5rem;
  }

  .section-header .el-icon {
    font-size: 1.7rem;
  }

  .section-title {
    font-size: 1.2rem;
  }

  .section-content {
    font-size: 0.95rem;
    line-height: 1.7;
  }

  .section-divider {
    margin: 1rem 0;
  }

  .signup-section {
    flex-direction: column;
    align-items: stretch;
    padding: 1.5rem 1rem;
    gap: 1rem;
    margin: 0 0.5rem 2rem;
    border-radius: 1.5rem;
  }

  .signup-content {
    justify-content: center;
    gap: 1rem;
  }

  .signup-icon-bg {
    width: 48px;
    height: 48px;
    font-size: 1.5rem;
  }

  .signup-title {
    font-size: 1.5rem;
  }

  .signup-desc {
    font-size: 1rem;
  }

  .signup-tip {
    font-size: 0.875rem;
  }

  .signup-button {
    font-size: 1rem;
    padding: 0.75rem 1.5rem;
    align-self: center;
    width: 100%;
    max-width: 200px;
  }

  .signup-bg-svg {
    right: -30px;
    bottom: -30px;
    width: 100px;
    height: 100px;
  }

  .images-showcase {
    gap: 0.5rem;
  }

  .showcase-image {
    max-width: 100%;
    height: auto;
  }

  .works-subtitle {
    font-size: 0.9rem;
  }
}

/* 移动端适配 - 小屏手机 */
@media (max-width: 480px) {
  .container {
    padding: 0 0.25rem;
  }

  .main-card {
    padding: 1rem 0.5rem;
  }

  .hero-section {
    padding: 1.5rem 0.5rem;
  }

  .hero-title {
    font-size: 1.5rem;
    margin: 0 0 0.5rem 0;
  }

  .hero-subtitle {
    font-size: 0.8rem;
    margin: 0 0 0.75rem 0;
  }

  .floating-elements {
    width: 120px;
    height: 120px;
  }

  .circle-1 {
    width: 50px;
    height: 50px;
  }

  .circle-2 {
    width: 35px;
    height: 35px;
  }

  .circle-3 {
    width: 25px;
    height: 25px;
  }

  .main-section {
    padding: 1rem 0 0.75rem 0;
  }

  .section-header {
    font-size: 1.1rem;
    gap: 0.4rem;
    margin-bottom: 0.4rem;
  }

  .section-header .el-icon {
    font-size: 1.5rem;
  }

  .section-title {
    font-size: 1.1rem;
  }

  .section-content {
    font-size: 0.9rem;
    line-height: 1.6;
  }

  .section-divider {
    margin: 0.75rem 0;
  }

  .signup-section {
    padding: 1.25rem 0.75rem;
    margin: 0 0.25rem 1.5rem;
    border-radius: 1.25rem;
  }

  .signup-content {
    gap: 0.75rem;
  }

  .signup-icon-bg {
    width: 40px;
    height: 40px;
    font-size: 1.25rem;
  }

  .signup-title {
    font-size: 1.25rem;
  }

  .signup-desc {
    font-size: 0.875rem;
  }

  .signup-tip {
    font-size: 0.8rem;
  }

  .signup-button {
    font-size: 0.9rem;
    padding: 0.6rem 1.25rem;
    max-width: 180px;
  }

  .signup-bg-svg {
    right: -20px;
    bottom: -20px;
    width: 80px;
    height: 80px;
  }

  .images-showcase {
    gap: 0.25rem;
  }

  .works-subtitle {
    font-size: 0.85rem;
  }
}

/* 超小屏设备适配 */
@media (max-width: 360px) {
  .hero-title {
    font-size: 1.3rem;
  }

  .hero-subtitle {
    font-size: 0.75rem;
  }

  .floating-elements {
    width: 100px;
    height: 100px;
  }

  .section-header {
    font-size: 1rem;
  }

  .section-header .el-icon {
    font-size: 1.3rem;
  }

  .section-title {
    font-size: 1rem;
  }

  .section-content {
    font-size: 0.85rem;
  }

  .signup-title {
    font-size: 1.1rem;
  }

  .signup-desc {
    font-size: 0.8rem;
  }

  .signup-button {
    font-size: 0.85rem;
    padding: 0.5rem 1rem;
  }
}

/* 横屏模式适配 */
@media (max-height: 500px) and (orientation: landscape) {
  .hero-section {
    padding: 1rem 1rem;
  }

  .hero-content {
    gap: 1rem;
  }

  .hero-title {
    font-size: 1.5rem;
    margin: 0 0 0.5rem 0;
  }

  .hero-subtitle {
    font-size: 0.8rem;
    margin: 0 0 0.5rem 0;
  }

  .floating-elements {
    width: 100px;
    height: 100px;
  }

  .main-card {
    padding: 1rem 1rem;
  }

  .main-section {
    padding: 1rem 0 0.75rem 0;
  }
}

/* 触摸设备优化 */
@media (hover: none) and (pointer: coarse) {
  .signup-button:hover:not(:disabled) {
    transform: none;
  }

  .showcase-item:hover {
    transform: none;
  }

  .showcase-item:hover .showcase-image {
    transform: none;
  }

  /* 增加触摸目标大小 */
  .signup-button {
    min-height: 44px;
  }

  .section-header {
    min-height: 44px;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInSection {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-section,
.main-section,
.signup-section {
  animation: fadeInUp 0.8s ease-out;
}

.main-section:nth-child(1) {
  animation-delay: 0.1s;
}
.main-section:nth-child(2) {
  animation-delay: 0.2s;
}
.main-section:nth-child(3) {
  animation-delay: 0.3s;
}
.signup-section {
  animation-delay: 0.4s;
}

/* 滚动条样式 */
.container::-webkit-scrollbar {
  width: 6px;
}

.container::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 3px;
}

.container::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border-radius: 3px;
}

.container::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #0f5bb3, #1b78f7);
}

/* 移动端滚动条优化 */
@media (max-width: 768px) {
  .container::-webkit-scrollbar {
    width: 4px;
  }

  .container::-webkit-scrollbar-thumb {
    background: #cbd5e1;
  }
}
</style>
