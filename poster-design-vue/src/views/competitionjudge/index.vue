<template>
  <header-vue :isRightShow="true" />
  <div ref="bodyContainer" class="body">
    <loading-vue v-if="isLoading" class="loading"></loading-vue>
    <div class="main" v-else>
      <div class="container">
        <!-- 页面头部区域 -->
        <div class="page-header">
          <div class="header-content">
            <div class="title-section">
              <h1 class="page-title">{{ targetList.competitionName }}</h1>
              <div class="title-decoration"></div>
            </div>
            <div class="header-stats">
              <div class="stat-item">
                <div class="stat-value">{{ unscoredTeams.length }}</div>
                <div class="stat-label">待评审</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ scoredTeams.length }}</div>
                <div class="stat-label">已评审</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 竞赛介绍区域 -->
        <div class="intro-section">
          <div class="section-header">
            <el-icon class="section-icon"><Document /></el-icon>
            <h3 class="section-title">竞赛介绍</h3>
          </div>
          <div class="intro-content">
            <p>{{ targetList.introduction }}</p>
          </div>
        </div>

        <!-- 评审区域 -->
        <div class="judge-sections">
          <!-- 待评区 -->
          <div class="judge-section">
            <div class="section-header">
              <el-icon class="section-icon"><Clock /></el-icon>
              <h3 class="section-title">待评区</h3>
              <div class="section-badge">{{ unscoredTeams.length }}</div>
            </div>
            <div class="team-grid">
              <template v-if="unscoredTeams.length > 0">
                <div
                  v-for="team in unscoredTeams"
                  :key="team.teamId"
                  class="team-card pending"
                >
                  <div class="card-header">
                    <div class="team-name">{{ team.teamName }}</div>
                    <div class="status-badge pending">
                      <el-icon><Clock /></el-icon>
                      待评审
                    </div>
                  </div>
                  <div class="card-actions">
                    <el-button
                      type="primary"
                      class="action-btn preview-btn"
                      @click="openPreview(team.works, 0)"
                    >
                      <el-icon><View /></el-icon>
                      查看作品
                    </el-button>
                    <el-button
                      type="success"
                      class="action-btn score-btn"
                      @click="openScore(team)"
                    >
                      <el-icon><Edit /></el-icon>
                      开始打分
                    </el-button>
                  </div>
                </div>
              </template>
              <template v-else>
                <div v-if="scoredTeams.length > 0" class="empty-state success">
                  <div class="empty-icon">
                    <el-icon><Trophy /></el-icon>
                  </div>
                  <h3 class="empty-title">🎉 恭喜您，已完成所有评审！</h3>
                  <p class="empty-desc">
                    所有团队都已评审完毕，感谢您的辛勤工作
                  </p>
                </div>
                <div v-else class="empty-state">
                  <div class="empty-icon">
                    <el-icon><Folder /></el-icon>
                  </div>
                  <h3 class="empty-title">暂无待评审团队</h3>
                  <p class="empty-desc">当前没有需要评审的团队作品</p>
                </div>
              </template>
            </div>
          </div>

          <!-- 已评区 -->
          <div class="judge-section">
            <div class="section-header">
              <el-icon class="section-icon"><Check /></el-icon>
              <h3 class="section-title">已评区</h3>
              <div class="section-badge">{{ scoredTeams.length }}</div>
            </div>
            <div class="team-grid">
              <template v-if="scoredTeams.length > 0">
                <div
                  v-for="team in scoredTeams"
                  :key="team.teamId"
                  class="team-card completed"
                >
                  <div class="card-header">
                    <div class="team-name">{{ team.teamName }}</div>
                    <div class="status-badge completed">
                      <el-icon><Check /></el-icon>
                      已评审
                    </div>
                  </div>
                  <div class="card-actions">
                    <el-button
                      type="primary"
                      class="action-btn preview-btn"
                      @click="openPreview(team.works, 0)"
                    >
                      <el-icon><View /></el-icon>
                      查看作品
                    </el-button>
                    <el-button
                      type="info"
                      class="action-btn result-btn"
                      @click="openInfo(team)"
                    >
                      <el-icon><Document /></el-icon>
                      查看评审结果
                    </el-button>
                  </div>
                </div>
              </template>
              <div v-else class="empty-state">
                <div class="empty-icon">
                  <el-icon><Document /></el-icon>
                </div>
                <h3 class="empty-title">暂无已评审团队</h3>
                <p class="empty-desc">评审结果将在这里显示</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 查看作品弹窗 -->
    <el-dialog
      v-model="previewVisible"
      :width="isMobile ? '95%' : '900px'"
      class="preview-dialog"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div class="preview-content">
        <el-button
          class="nav-btn left"
          @click="prevWork"
          :disabled="currentWorkIndex === 0"
        >
          <el-icon><ArrowLeft /></el-icon>
        </el-button>

        <div class="preview-main">
          <img v-lazy :src="selectedWork.picture" class="preview-image" />
          <div class="preview-info">
            <h3 class="work-title">{{ selectedWork.name }}</h3>
            <p class="work-desc">{{ selectedWork.introduction }}</p>
            <div class="work-counter">
              {{ currentWorkIndex + 1 }} / {{ workList.length }}
            </div>
          </div>
        </div>

        <el-button
          class="nav-btn right"
          @click="nextWork"
          :disabled="currentWorkIndex === workList.length - 1"
        >
          <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
    </el-dialog>

    <!-- 评审结果弹窗 -->
    <el-dialog
      v-model="resultVisible"
      title="评审结果"
      :width="isMobile ? '90%' : '500px'"
      class="result-dialog"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div class="result-content">
        <div class="judge-info">
          <div class="judge-avatar">
            <img
              v-lazy
              v-if="resultData.judgeAvatar"
              :src="resultData.judgeAvatar"
              alt="avatar"
            />
            <el-icon v-else><User /></el-icon>
          </div>
          <div class="judge-details">
            <div class="judge-name">{{ resultData.judgeName }}</div>
            <div class="judge-role">评审专家</div>
          </div>
        </div>
        <div class="result-score">
          <div class="score-label">评分</div>
          <div class="score-value">{{ resultData.score }}</div>
          <div class="score-max">/ 100</div>
        </div>
        <div class="result-comment">
          <div class="comment-label">评语</div>
          <div class="comment-content">{{ resultData.comment }}</div>
        </div>
      </div>
    </el-dialog>

    <!-- 打分弹窗 -->
    <el-dialog
      v-model="scoreVisible"
      :width="isMobile ? '90%' : '500px'"
      title="评审打分"
      class="score-dialog"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div class="score-content">
        <div class="score-section">
          <label class="score-label">创意</label>
          <el-slider
            v-model="score.creativity"
            :min="0"
            :max="100 - score.design - score.expression"
            :step="1"
            show-input
          />
        </div>
        <div class="score-section">
          <label class="score-label">设计</label>
          <el-slider
            v-model="score.design"
            :min="0"
            :max="100 - score.creativity - score.expression"
            :step="1"
            show-input
          />
        </div>
        <div class="score-section">
          <label class="score-label">表现力</label>
          <el-slider
            v-model="score.expression"
            :min="0"
            :max="100 - score.creativity - score.design"
            :step="1"
            show-input
          />
        </div>
        <div class="score-section">
          <label class="score-label">总分</label>
          <el-input-number
            :model-value="totalScore"
            :min="0"
            :max="100"
            class="score-input"
            size="large"
            disabled
          />
        </div>
        <div class="comment-section">
          <label class="comment-label">评语</label>
          <el-input
            type="textarea"
            v-model="comment"
            placeholder="请输入详细的评审意见..."
            :rows="isMobile ? 4 : 6"
            class="comment-input"
            maxlength="500"
            show-word-limit
          />
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="scoreVisible = false" class="cancel-btn"
            >取消</el-button
          >
          <el-button
            type="primary"
            @click="confirmScore"
            :disabled="sendRule"
            class="confirm-btn"
          >
            确认提交
          </el-button>
        </div>
      </template>
    </el-dialog>

    <aside-vue :container="bodyContainer" v-if="isAsideShow" />
    <footer-vue />
  </div>
</template>

<script setup>
import headerVue from "@/libs/header/index.vue";
import footerVue from "@/libs/footer/index.vue";
import asideVue from "../main/componets/aside/index.vue";
import { getCompetitionById } from "@/apis/competition";
import { getJudgeTeam, sendJudgeInfo, getJudgeFinalInfo } from "@/apis/judge";
import { ElMessage } from "element-plus";
import { ref, onMounted, watch, computed, reactive } from "vue";
import { useRoute } from "vue-router";
import { isMobile } from "@/utils/flexible.js";
import {
  Document,
  Clock,
  Check,
  View,
  Edit,
  Trophy,
  Folder,
  ArrowLeft,
  ArrowRight,
  User,
} from "@element-plus/icons-vue";
import loadingVue from "@/libs/loading/index.vue";

const isLoading = ref(true);
const route = useRoute();
const targetList = ref({});
const teamList = ref([]);
const bodyContainer = ref(null);
const isAsideShow = ref(true);
const teamId = ref(0);

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
    } else {
      ElMessage.error(res.msg || "获取竞赛信息失败");
    }
  } catch (err) {
    ElMessage.error("获取竞赛失败");
    console.error(err);
  }
};

const getJudge = async () => {
  try {
    const res = await getJudgeTeam(route.params.id);
    if (res.code === 1 && res.data) {
      teamList.value = [...res.data];
    } else {
      ElMessage.error(res.msg || "获取评审信息失败");
    }
  } catch (err) {
    ElMessage.error("获取评审失败");
    console.error(err);
  }
};

onMounted(async () => {
  await getCompetition();
  await getJudge();
  isLoading.value = false;
});

const previewVisible = ref(false);
const scoreVisible = ref(false);
const workList = ref([]);
const selectedWork = ref({});
const currentWorkIndex = ref(0);
const score = reactive({
  creativity: 0,
  design: 0,
  expression: 0,
});
const totalScore = computed(() => {
  return score.creativity + score.design + score.expression;
});
const comment = ref("");

const openPreview = (works, index) => {
  workList.value = works;
  currentWorkIndex.value = index;
  selectedWork.value = works[index];
  previewVisible.value = true;
};

const prevWork = () => {
  if (currentWorkIndex.value > 0) {
    currentWorkIndex.value--;
    selectedWork.value = workList.value[currentWorkIndex.value];
  }
};

const nextWork = () => {
  if (currentWorkIndex.value < workList.value.length - 1) {
    currentWorkIndex.value++;
    selectedWork.value = workList.value[currentWorkIndex.value];
  }
};

const openScore = (team) => {
  teamId.value = team.teamId;
  score.creativity = 0;
  score.design = 0;
  score.expression = 0;
  comment.value = "";
  scoreVisible.value = true;
};

const sendRule = computed(() => {
  return (
    totalScore.value <= 0 ||
    totalScore.value > 100 ||
    comment.value.length === 0
  );
});

import { ElMessageBox } from "element-plus";

const scoredTeams = computed(() => teamList.value.filter((t) => t.isEvaluated));
const unscoredTeams = computed(() =>
  teamList.value.filter((t) => !t.isEvaluated)
);

const sendJudge = async (data) => {
  try {
    const res = await sendJudgeInfo(data);
    if (res.code == 1 && res.data) {
      ElMessage.success("评审成功");
      return true;
    } else {
      ElMessage.error(res.msg || "评审失败");
      return false;
    }
  } catch (err) {
    ElMessage.error("评审失败");
    console.error(err);
    return false;
  }
};

const confirmScore = async () => {
  try {
    await ElMessageBox.confirm(
      "你只能评审一次，提交后将无法更改，确认提交？",
      "确认提示",
      {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      }
    );

    const data = {
      competitionId: route.params.id,
      teamId: teamId.value,
      score: totalScore.value, // 只传总分
      comment: comment.value,
    };

    const result = await sendJudge(data);
    if (result) {
      scoreVisible.value = false;
      await getCompetition();
      await getJudge();
    }
  } catch (err) {
    // 用户取消或提交失败都不会抛出异常
  }
};

const resultVisible = ref(false);
const resultData = ref({
  judgeName: "匿名评委",
  judgeAvatar: "",
  score: 0,
  comment: "",
});

const getJudgeInfo = async (competitionId, teamId) => {
  try {
    const res = await getJudgeFinalInfo(competitionId, teamId);
    if (res.code == 1 && res.data) {
      const data = res.data[0] || {};
      resultData.value = {
        judgeName: data.judgeName || "匿名评委",
        judgeAvatar: data.judgeAvatar || "",
        score: data.score || 0,
        comment: data.comment || "无评语",
      };
      resultVisible.value = true;
    } else {
      ElMessage.error(res.msg || "获取评审失败");
    }
  } catch (err) {
    ElMessage.error("获取评审失败");
    console.error(err);
  }
};

const openInfo = (team) => {
  getJudgeInfo(route.params.id, team.teamId);
};
</script>

<style scoped>
.body {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.main {
  min-height: 90vh;
  padding: 24px 0 40px 0;
}

.loading {
  height: 50vh;
}

.container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
}

/* 页面头部 */
.page-header {
  background: linear-gradient(135deg, #1b78f7 0%, #409eff 50%, #66b1ff 100%);
  border-radius: 20px;
  padding: 40px 32px;
  margin-bottom: 32px;
  color: white;
  position: relative;
  overflow: hidden;
}

.page-header::before {
  content: "";
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(
    circle,
    rgba(255, 255, 255, 0.1) 0%,
    transparent 70%
  );
  animation: float 6s ease-in-out infinite;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 2;
}

.title-section {
  flex: 1;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 12px 0;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.title-decoration {
  width: 60px;
  height: 4px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 2px;
}

.header-stats {
  display: flex;
  gap: 24px;
}

.stat-item {
  text-align: center;
  background: rgba(255, 255, 255, 0.15);
  padding: 16px 24px;
  border-radius: 12px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

/* 竞赛介绍区域 */
.intro-section {
  background: white;
  border-radius: 16px;
  padding: 32px;
  margin-bottom: 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.section-icon {
  font-size: 24px;
  color: #1b78f7;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #1a202c;
  margin: 0;
}

.section-badge {
  background: linear-gradient(135deg, #1b78f7, #409eff);
  color: white;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  margin-left: auto;
}

.intro-content p {
  font-size: 16px;
  color: #4a5568;
  line-height: 1.8;
  margin: 0;
  white-space: pre-line;
}

/* 评审区域 */
.judge-sections {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.judge-section {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
}

.team-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
}

.team-card {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 2px solid transparent;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.team-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #1b78f7, #409eff);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.team-card:hover::before {
  transform: scaleX(1);
}

.team-card.pending {
  border-color: #fbbf24;
  background: linear-gradient(135deg, #fffbeb 0%, #fef3c7 100%);
}

.team-card.completed {
  border-color: #10b981;
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
}

.team-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.team-name {
  font-size: 18px;
  font-weight: 600;
  color: #1a202c;
}

.status-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.status-badge.pending {
  background: #fbbf24;
  color: #92400e;
}

.status-badge.completed {
  background: #10b981;
  color: white;
}

.card-actions {
  display: flex;
  gap: 12px;
}

.action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  height: 40px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.preview-btn {
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border: none;
}

.preview-btn:hover {
  background: linear-gradient(135deg, #0f5bb3, #1b78f7);
  transform: translateY(-2px);
}

.score-btn {
  background: linear-gradient(135deg, #10b981, #34d399);
  border: none;
}

.score-btn:hover {
  background: linear-gradient(135deg, #059669, #10b981);
  transform: translateY(-2px);
}

.result-btn {
  background: linear-gradient(135deg, #6b7280, #9ca3af);
  border: none;
}

.result-btn:hover {
  background: linear-gradient(135deg, #4b5563, #6b7280);
  transform: translateY(-2px);
}

/* 空状态 */
.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
  color: #6b7280;
}

.empty-state.success {
  color: #fbbf24;
}

.empty-state.success .empty-desc {
  color: #222;
}

.empty-state.success .empty-icon {
  font-size: 72px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 1;
  color: #fbbf24;
}

.empty-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #fbbf24;
}

.empty-desc {
  font-size: 14px;
  margin: 0;
  opacity: 0.8;
  color: #eab308;
}

/* 弹窗样式 */
.preview-dialog :deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

.preview-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #1b78f7, #409eff);
  color: white;
  padding: 20px 24px;
}

.preview-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

.preview-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.preview-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  position: relative;
  padding: 32px;
  background: #f8fafc;
}

.preview-main {
  display: flex;
  align-items: center;
  gap: 32px;
  max-width: 800px;
}

.preview-image {
  width: 400px;
  height: 400px;
  object-fit: contain;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  background: white;
}

.preview-info {
  flex: 1;
  max-width: 300px;
}

.work-title {
  font-size: 20px;
  font-weight: 600;
  color: #1a202c;
  margin: 0 0 12px 0;
}

.work-desc {
  font-size: 14px;
  color: #4a5568;
  line-height: 1.6;
  margin: 0 0 16px 0;
}

.work-counter {
  font-size: 12px;
  color: #6b7280;
  background: #e5e7eb;
  padding: 4px 12px;
  border-radius: 12px;
  display: inline-block;
}

.nav-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1b78f7, #409eff);
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  box-shadow: 0 4px 16px rgba(27, 120, 247, 0.3);
  transition: all 0.3s ease;
}

.nav-btn:hover {
  background: linear-gradient(135deg, #0f5bb3, #1b78f7);
  transform: translateY(-50%) scale(1.1);
}

.nav-btn.left {
  left: -24px;
}

.nav-btn.right {
  right: -24px;
}

.nav-btn:disabled {
  background: #e5e7eb;
  color: #9ca3af;
  cursor: not-allowed;
  transform: translateY(-50%);
}

/* 评审结果弹窗 */
.result-dialog :deep(.el-dialog) {
  border-radius: 16px;
}

.result-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #10b981, #34d399);
  color: white;
  padding: 20px 24px;
}

.result-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

.result-content {
  padding: 24px;
}

.judge-info {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
}

.judge-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1b78f7, #409eff);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  overflow: hidden;
}

.judge-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.judge-details {
  flex: 1;
}

.judge-name {
  font-size: 16px;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 4px;
}

.judge-role {
  font-size: 12px;
  color: #6b7280;
}

.result-score {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 24px;
  padding: 16px;
  background: linear-gradient(135deg, #ecfdf5, #d1fae5);
  border-radius: 12px;
  border: 1px solid #10b981;
}

.score-label {
  font-size: 14px;
  color: #065f46;
  font-weight: 500;
}

.score-value {
  font-size: 32px;
  font-weight: 700;
  color: #10b981;
}

.score-max {
  font-size: 16px;
  color: #6b7280;
}

.result-comment {
  background: #f8fafc;
  border-radius: 12px;
  padding: 16px;
}

.comment-label {
  font-size: 14px;
  color: #374151;
  font-weight: 500;
  margin-bottom: 8px;
  display: block;
}

.comment-content {
  font-size: 14px;
  color: #4b5563;
  line-height: 1.6;
  white-space: pre-line;
}

/* 打分弹窗 */
.score-dialog :deep(.el-dialog) {
  border-radius: 16px;
}

.score-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #f59e0b, #fbbf24);
  color: white;
  padding: 20px 24px;
}

.score-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

.score-content {
  padding: 24px;
}

.score-section,
.comment-section {
  margin-bottom: 24px;
}

.score-label,
.comment-label {
  display: block;
  font-size: 14px;
  color: #374151;
  font-weight: 500;
  margin-bottom: 8px;
}

.score-input {
  width: 100%;
}

.score-input :deep(.el-input-number__decrease),
.score-input :deep(.el-input-number__increase) {
  background: #1b78f7;
  color: white;
  border: none;
}

.score-input :deep(.el-input-number__decrease:hover),
.score-input :deep(.el-input-number__increase:hover) {
  background: #0f5bb3;
}

.comment-input {
  width: 100%;
}

.comment-input :deep(.el-textarea__inner) {
  border-radius: 8px;
  border: 1px solid #d1d5db;
  resize: none;
}

.comment-input :deep(.el-textarea__inner:focus) {
  border-color: #1b78f7;
  box-shadow: 0 0 0 3px rgba(27, 120, 247, 0.1);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  /* background: #f8fafc; */
}

.cancel-btn {
  background: #e5e7eb;
  color: #374151;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  background: #d1d5db;
  transform: translateY(-1px);
}

.confirm-btn {
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.confirm-btn:hover {
  background: linear-gradient(135deg, #0f5bb3, #1b78f7);
  transform: translateY(-1px);
}

.confirm-btn:disabled {
  background: #e5e7eb;
  color: #9ca3af;
  cursor: not-allowed;
  transform: none;
}

/* 移动端适配 - 平板设备 */
@media (max-width: 1024px) {
  .container {
    padding: 0 1.5rem;
  }

  .page-header {
    padding: 2.5rem 2rem;
    margin-bottom: 2rem;
  }

  .page-title {
    font-size: 2.25rem;
  }

  .header-stats {
    gap: 1.5rem;
  }

  .stat-item {
    padding: 1.25rem 1.5rem;
  }

  .stat-value {
    font-size: 2rem;
  }

  .intro-section,
  .judge-section {
    padding: 2rem;
    margin-bottom: 2rem;
  }

  .section-header {
    font-size: 1.35rem;
    gap: 0.75rem;
  }

  .section-header .el-icon {
    font-size: 1.6rem;
  }

  .section-title {
    font-size: 1.35rem;
  }

  .team-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 1.5rem;
  }

  .team-card {
    padding: 1.5rem;
  }

  .team-name {
    font-size: 1.1rem;
  }

  .action-btn {
    padding: 0.6rem 1rem;
    font-size: 0.9rem;
  }

  .preview-image {
    width: 350px;
    height: 350px;
  }

  .work-title {
    font-size: 1.1rem;
  }

  .work-desc {
    font-size: 0.9rem;
  }
}

/* 移动端适配 - 手机设备 */
@media (max-width: 768px) {
  .container {
    padding: 0 1rem;
  }

  .page-header {
    padding: 2rem 1.5rem;
    margin-bottom: 1.5rem;
  }

  .header-content {
    flex-direction: column;
    gap: 1.5rem;
    text-align: center;
  }

  .page-title {
    font-size: 1.875rem;
    margin: 0 0 0.75rem 0;
  }

  .title-decoration {
    width: 50px;
    height: 3px;
  }

  .header-stats {
    gap: 1rem;
  }

  .stat-item {
    padding: 1rem 1.25rem;
  }

  .stat-value {
    font-size: 1.75rem;
  }

  .stat-label {
    font-size: 0.8rem;
  }

  .intro-section,
  .judge-section {
    padding: 1.5rem;
    margin-bottom: 1.5rem;
  }

  .section-header {
    font-size: 1.25rem;
    gap: 0.5rem;
    margin-bottom: 0.75rem;
  }

  .section-header .el-icon {
    font-size: 1.5rem;
  }

  .section-title {
    font-size: 1.25rem;
  }

  .section-badge {
    font-size: 0.75rem;
    padding: 0.25rem 0.75rem;
  }

  .intro-content p {
    font-size: 0.95rem;
    line-height: 1.7;
  }

  .team-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .team-card {
    padding: 1.25rem;
  }

  .card-header {
    margin-bottom: 1rem;
  }

  .team-name {
    font-size: 1rem;
  }

  .status-badge {
    font-size: 0.75rem;
    padding: 0.4rem 0.8rem;
  }

  .card-actions {
    flex-direction: column;
    gap: 0.75rem;
  }

  .action-btn {
    width: 100%;
    padding: 0.75rem 1rem;
    font-size: 0.9rem;
    min-height: 40px;
  }

  .empty-state {
    padding: 3rem 1rem;
  }

  .empty-icon {
    font-size: 3rem;
    margin-bottom: 1rem;
  }

  .empty-title {
    font-size: 1.25rem;
    margin: 0 0 0.5rem 0;
  }

  .empty-desc {
    font-size: 0.9rem;
  }

  /* 弹窗适配 */
  .preview-dialog,
  .result-dialog,
  .score-dialog {
    margin: 1rem;
  }

  .preview-dialog :deep(.el-dialog),
  .result-dialog :deep(.el-dialog),
  .score-dialog :deep(.el-dialog) {
    width: calc(100% - 2rem) !important;
    max-width: none;
    margin: 1rem auto !important;
  }

  .preview-dialog :deep(.el-dialog__header),
  .result-dialog :deep(.el-dialog__header),
  .score-dialog :deep(.el-dialog__header) {
    padding: 1rem 1rem 0.75rem 1rem;
    font-size: 1.1rem;
  }

  .preview-dialog :deep(.el-dialog__body),
  .result-dialog :deep(.el-dialog__body),
  .score-dialog :deep(.el-dialog__body) {
    padding: 0;
  }

  .preview-content {
    padding: 1rem;
  }

  .preview-main {
    flex-direction: column;
    gap: 1rem;
  }

  .preview-image {
    width: 100%;
    max-width: 280px;
    height: auto;
  }

  .preview-info {
    max-width: none;
    text-align: center;
  }

  .work-title {
    font-size: 1.1rem;
    margin: 0 0 0.5rem 0;
  }

  .work-desc {
    font-size: 0.85rem;
    margin: 0 0 0.75rem 0;
  }

  .work-counter {
    font-size: 0.75rem;
    padding: 0.25rem 0.75rem;
  }

  .nav-btn {
    width: 40px;
    height: 40px;
    font-size: 1rem;
  }

  .nav-btn.left {
    left: 0.5rem;
  }

  .nav-btn.right {
    right: 0.5rem;
  }

  .result-content,
  .score-content {
    padding: 1rem;
  }

  .judge-info {
    gap: 0.75rem;
    margin-bottom: 1rem;
    padding: 0.75rem;
  }

  .judge-avatar {
    width: 40px;
    height: 40px;
    font-size: 1rem;
  }

  .judge-name {
    font-size: 0.95rem;
  }

  .judge-role {
    font-size: 0.75rem;
  }

  .result-score {
    gap: 0.5rem;
    margin-bottom: 1rem;
    padding: 0.75rem;
  }

  .score-label {
    font-size: 0.85rem;
  }

  .score-value {
    font-size: 1.75rem;
  }

  .score-max {
    font-size: 0.9rem;
  }

  .result-comment {
    padding: 0.75rem;
  }

  .comment-label {
    font-size: 0.85rem;
    margin-bottom: 0.5rem;
  }

  .comment-content {
    font-size: 0.85rem;
  }

  .score-section,
  .comment-section {
    margin-bottom: 1rem;
  }

  .score-label,
  .comment-label {
    font-size: 0.85rem;
    margin-bottom: 0.5rem;
  }

  .score-input :deep(.el-input-number__decrease),
  .score-input :deep(.el-input-number__increase) {
    min-width: 32px;
    min-height: 32px;
  }

  .comment-input :deep(.el-textarea__inner) {
    font-size: 0.9rem;
  }

  .dialog-footer {
    padding: 0.75rem 1rem;
    gap: 0.75rem;
  }

  .cancel-btn,
  .confirm-btn {
    padding: 0.6rem 1rem;
    font-size: 0.9rem;
    min-height: 40px;
  }
}

/* 移动端适配 - 小屏手机 */
@media (max-width: 480px) {
  .container {
    padding: 0 0.75rem;
  }

  .page-header {
    padding: 1.5rem 1rem;
    margin-bottom: 1rem;
  }

  .page-title {
    font-size: 1.5rem;
    margin: 0 0 0.5rem 0;
  }

  .title-decoration {
    width: 40px;
    height: 2px;
  }

  .header-stats {
    flex-direction: row;
    justify-content: center;
    align-items: center;
    gap: 0.5rem;
  }

  .stat-item {
    padding: 0.75rem 1rem;
  }

  .stat-value {
    font-size: 1.5rem;
  }

  .stat-label {
    font-size: 0.75rem;
  }

  .intro-section,
  .judge-section {
    padding: 1rem;
    margin-bottom: 1rem;
  }

  .section-header {
    font-size: 1.1rem;
    gap: 0.4rem;
    margin-bottom: 0.5rem;
  }

  .section-header .el-icon {
    font-size: 1.3rem;
  }

  .section-title {
    font-size: 1.1rem;
  }

  .section-badge {
    font-size: 0.7rem;
    padding: 0.2rem 0.6rem;
  }

  .intro-content p {
    font-size: 0.9rem;
    line-height: 1.6;
  }

  .team-card {
    padding: 1rem;
  }

  .card-header {
    margin-bottom: 0.75rem;
  }

  .team-name {
    font-size: 0.95rem;
  }

  .status-badge {
    font-size: 0.7rem;
    padding: 0.3rem 0.6rem;
  }

  .card-actions {
    gap: 0.5rem;
  }

  .action-btn {
    padding: 0.6rem 0.8rem;
    font-size: 0.85rem;
    min-height: 36px;
  }

  .empty-state {
    padding: 2rem 0.75rem;
  }

  .empty-icon {
    font-size: 2.5rem;
    margin-bottom: 0.75rem;
  }

  .empty-title {
    font-size: 1.1rem;
    margin: 0 0 0.4rem 0;
  }

  .empty-desc {
    font-size: 0.85rem;
  }

  /* 弹窗小屏优化 */
  .preview-dialog,
  .result-dialog,
  .score-dialog {
    margin: 0.5rem;
  }

  .preview-dialog :deep(.el-dialog),
  .result-dialog :deep(.el-dialog),
  .score-dialog :deep(.el-dialog) {
    width: calc(100% - 1rem) !important;
    margin: 0.5rem auto !important;
  }

  .preview-dialog :deep(.el-dialog__header),
  .result-dialog :deep(.el-dialog__header),
  .score-dialog :deep(.el-dialog__header) {
    padding: 0.75rem 0.75rem 0.5rem 0.75rem;
    font-size: 1rem;
  }

  .preview-content {
    padding: 0.75rem;
  }

  .preview-main {
    gap: 0.75rem;
  }

  .preview-image {
    max-width: 250px;
  }

  .work-title {
    font-size: 1rem;
  }

  .work-desc {
    font-size: 0.8rem;
  }

  .work-counter {
    font-size: 0.7rem;
    padding: 0.2rem 0.6rem;
  }

  .nav-btn {
    width: 36px;
    height: 36px;
    font-size: 0.9rem;
  }

  .nav-btn.left {
    left: 0.25rem;
  }

  .nav-btn.right {
    right: 0.25rem;
  }

  .result-content,
  .score-content {
    padding: 0.75rem;
  }

  .judge-info {
    gap: 0.5rem;
    margin-bottom: 0.75rem;
    padding: 0.5rem;
  }

  .judge-avatar {
    width: 36px;
    height: 36px;
    font-size: 0.9rem;
  }

  .judge-name {
    font-size: 0.9rem;
  }

  .judge-role {
    font-size: 0.7rem;
  }

  .result-score {
    gap: 0.4rem;
    margin-bottom: 0.75rem;
    padding: 0.5rem;
  }

  .score-label {
    font-size: 0.8rem;
  }

  .score-value {
    font-size: 1.5rem;
  }

  .score-max {
    font-size: 0.8rem;
  }

  .result-comment {
    padding: 0.5rem;
  }

  .comment-label {
    font-size: 0.8rem;
    margin-bottom: 0.4rem;
  }

  .comment-content {
    font-size: 0.8rem;
  }

  .score-section,
  .comment-section {
    margin-bottom: 0.75rem;
  }

  .score-label,
  .comment-label {
    font-size: 0.8rem;
    margin-bottom: 0.4rem;
  }

  .score-input :deep(.el-input-number__decrease),
  .score-input :deep(.el-input-number__increase) {
    min-width: 28px;
    min-height: 28px;
  }

  .comment-input :deep(.el-textarea__inner) {
    font-size: 0.85rem;
  }

  .dialog-footer {
    padding: 0.5rem 0.75rem;
    gap: 0.5rem;
  }

  .cancel-btn,
  .confirm-btn {
    padding: 0.5rem 0.75rem;
    font-size: 0.85rem;
    min-height: 36px;
  }
}

/* 超小屏设备适配 */
@media (max-width: 360px) {
  .page-title {
    font-size: 1.3rem;
  }

  .stat-value {
    font-size: 1.3rem;
  }

  .section-header {
    font-size: 1rem;
  }

  .section-header .el-icon {
    font-size: 1.2rem;
  }

  .section-title {
    font-size: 1rem;
  }

  .team-name {
    font-size: 0.9rem;
  }

  .action-btn {
    font-size: 0.8rem;
    padding: 0.5rem 0.6rem;
  }

  .preview-image {
    max-width: 220px;
  }

  .work-title {
    font-size: 0.9rem;
  }

  .work-desc {
    font-size: 0.75rem;
  }

  .nav-btn {
    width: 32px;
    height: 32px;
    font-size: 0.8rem;
  }

  .judge-avatar {
    width: 32px;
    height: 32px;
    font-size: 0.8rem;
  }

  .score-value {
    font-size: 1.3rem;
  }

  .cancel-btn,
  .confirm-btn {
    font-size: 0.8rem;
    padding: 0.4rem 0.6rem;
  }
}

/* 横屏模式适配 */
@media (max-height: 500px) and (orientation: landscape) {
  .page-header {
    padding: 1rem 1.5rem;
  }

  .page-title {
    font-size: 1.5rem;
    margin: 0 0 0.5rem 0;
  }

  .header-stats {
    gap: 1rem;
  }

  .stat-item {
    padding: 0.75rem 1rem;
  }

  .intro-section,
  .judge-section {
    padding: 1rem;
    margin-bottom: 1rem;
  }

  .preview-content {
    padding: 1rem;
  }

  .preview-main {
    gap: 1rem;
  }

  .preview-image {
    max-width: 200px;
  }
}

/* 触摸设备优化 */
@media (hover: none) and (pointer: coarse) {
  .action-btn:hover {
    transform: none;
  }

  .team-card:hover {
    transform: none;
  }

  .nav-btn:hover {
    transform: translateY(-50%);
  }

  /* 增加触摸目标大小 */
  .action-btn {
    min-height: 44px;
  }

  .team-card {
    min-height: 44px;
  }

  .nav-btn {
    min-width: 44px;
    min-height: 44px;
  }

  .preview-dialog :deep(.el-dialog__headerbtn),
  .result-dialog :deep(.el-dialog__headerbtn),
  .score-dialog :deep(.el-dialog__headerbtn) {
    min-width: 44px;
    min-height: 44px;
  }

  .preview-dialog :deep(.el-dialog__headerbtn .el-dialog__close),
  .result-dialog :deep(.el-dialog__headerbtn .el-dialog__close),
  .score-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
    font-size: 1.2rem;
  }

  .score-input :deep(.el-input-number__decrease),
  .score-input :deep(.el-input-number__increase) {
    min-width: 44px;
    min-height: 44px;
  }

  .cancel-btn,
  .confirm-btn {
    min-height: 44px;
    min-width: 80px;
  }
}

/* 移动端滚动条优化 */
.body {
  scrollbar-width: thin;
  scrollbar-color: rgba(27, 120, 247, 0.3) transparent;
}

.body::-webkit-scrollbar {
  width: 4px;
}

.body::-webkit-scrollbar-track {
  background: transparent;
}

.body::-webkit-scrollbar-thumb {
  background: rgba(27, 120, 247, 0.3);
  border-radius: 2px;
}

.body::-webkit-scrollbar-thumb:hover {
  background: rgba(27, 120, 247, 0.5);
}

/* 弹窗内容滚动优化 */
.preview-dialog :deep(.el-dialog__body),
.result-dialog :deep(.el-dialog__body),
.score-dialog :deep(.el-dialog__body) {
  max-height: 70vh;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: rgba(27, 120, 247, 0.3) transparent;
}

.preview-dialog :deep(.el-dialog__body)::-webkit-scrollbar,
.result-dialog :deep(.el-dialog__body)::-webkit-scrollbar,
.score-dialog :deep(.el-dialog__body)::-webkit-scrollbar {
  width: 4px;
}

.preview-dialog :deep(.el-dialog__body)::-webkit-scrollbar-track,
.result-dialog :deep(.el-dialog__body)::-webkit-scrollbar-track,
.score-dialog :deep(.el-dialog__body)::-webkit-scrollbar-track {
  background: transparent;
}

.preview-dialog :deep(.el-dialog__body)::-webkit-scrollbar-thumb,
.result-dialog :deep(.el-dialog__body)::-webkit-scrollbar-thumb,
.score-dialog :deep(.el-dialog__body)::-webkit-scrollbar-thumb {
  background: rgba(27, 120, 247, 0.3);
  border-radius: 2px;
}

.preview-dialog :deep(.el-dialog__body)::-webkit-scrollbar-thumb:hover,
.result-dialog :deep(.el-dialog__body)::-webkit-scrollbar-thumb:hover,
.score-dialog :deep(.el-dialog__body)::-webkit-scrollbar-thumb:hover {
  background: rgba(27, 120, 247, 0.5);
}

/* 移动端动画优化 */
.team-card {
  transition: all 0.2s ease;
}

.team-card:active {
  transform: scale(0.98);
}

.action-btn {
  transition: all 0.2s ease;
}

.action-btn:active {
  transform: scale(0.95);
}

.nav-btn {
  transition: all 0.2s ease;
}

.nav-btn:active {
  transform: translateY(-50%) scale(0.95);
}

.cancel-btn,
.confirm-btn {
  transition: all 0.2s ease;
}

.cancel-btn:active,
.confirm-btn:active {
  transform: scale(0.95);
}
</style>
