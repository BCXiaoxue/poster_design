<template>
  <div class="competition-container">
    <!-- 主要内容区域 -->
    <el-main class="main-content">
      <el-card class="content-card">
        <template #header>
          <div class="page-header">
            <h2 class="page-title">{{ pageTitle }}</h2>
            <p class="page-subtitle">查看您参与的所有竞赛信息</p>
          </div>
        </template>

        <!-- 搜索和状态筛选区域 -->
        <div class="search-section">
          <el-row class="filter-row" :gutter="0">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" style="flex:0 0 auto;max-width:none;">
              <el-input v-model="searchKeyword" placeholder="搜索竞赛名称" class="search-input" clearable>
                <template #prefix>
                  <el-icon>
                    <Search />
                  </el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8" :lg="6" style="flex:0 0 auto;max-width:none;">
              <el-select v-model="filterStatus" placeholder="筛选状态" class="status-select" clearable>
                <el-option label="未开始" value="0" />
                <el-option label="招募中" value="1" />
                <el-option label="评审中" value="2" />
                <el-option label="已结束" value="3" />
              </el-select>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8" :lg="6" style="flex:0 0 auto;max-width:none;">
              <el-button type="primary" @click="searchCompetitions" class="search-btn" :icon="Search">
                搜索
              </el-button>
            </el-col>
          </el-row>
        </div>

        <!-- 竞赛表格 -->
        <div class="table-section">
          <el-table :data="competitions" border class="competition-table"
            :header-cell-style="{ background: '#f8f9fa', color: '#333', fontWeight: '600' }"
            :row-class-name="getRowClassName">
            <el-table-column label="竞赛ID" width="140" align="center">
              <template #default="scope">
                <span class="competition-id">{{ formatCompetitionId(scope.row.id) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="competitionName" label="竞赛名称" width="200" show-overflow-tooltip>
              <template #default="scope">
                <div class="competition-name">{{ scope.row.competitionName }}</div>
              </template>
            </el-table-column>
            <el-table-column label="时间" width="220" align="center">
              <template #default="scope">
                <div class="time-info">
                  <div class="time-item">
                    <span class="time-label">开始:</span>
                    <span class="time-value">{{ scope.row.registrationBegin }}</span>
                  </div>
                  <div class="time-item">
                    <span class="time-label">结束:</span>
                    <span class="time-value">{{ scope.row.activityEnd }}</span>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100" align="center">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.competitionStatus)" class="status-tag" effect="light">
                  {{ getStatusText(scope.row.competitionStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="进度" width="160" align="center">
              <template #default="scope">
                <div class="progress-container">
                  <!-- 修复：移除进度条状态，仅保留基础进度显示 -->
                  <el-progress :percentage="calculateProgress(
                    scope.row.registrationBegin,
                    scope.row.activityEnd
                  )" :stroke-width="8" class="progress-bar" />
                </div>
              </template>
            </el-table-column>
            <el-table-column label="参与角色" width="120" align="center">
              <template #default="scope">
                <el-tag type="primary" v-if="scope.row.role === 0" class="role-tag" effect="light">
                  队员
                </el-tag>
                <el-tag type="success" v-if="scope.row.role === 1" class="role-tag" effect="light">
                  队长
                </el-tag>
                <el-tag type="warning" v-if="scope.row.role === 2" class="role-tag" effect="light">
                  评委
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template #default="scope">
                <el-button size="small" type="primary" @click="viewCompetition(scope.row.id)" class="action-btn"
                  :icon="View">
                  查看详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 分页控件 -->
        <div class="pagination-section">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="currentPage" :page-sizes="[5, 10, 20]" :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total" class="pagination">
          </el-pagination>
        </div>
      </el-card>
    </el-main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { searchCompetition } from "@/apis/person";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { Search, View } from '@element-plus/icons-vue';

// 响应式变量
const pageTitle = ref("我的竞赛");
const searchKeyword = ref("");
const filterStatus = ref("");
const currentPage = ref(1);
const pageSize = ref(10);
const competitions = ref([]);
const total = ref(0);
const router = useRouter();

// 表单参数
const formData = ref({
  competitionName: "",
  competitionStatus: "",
  pageNo: 1,
  pageSize: 10,
});

// 初始化加载
onMounted(() => {
  search();
});

// 搜索/分页查询
const search = async () => {
  try {
    formData.value.competitionName = searchKeyword.value;
    formData.value.competitionStatus = filterStatus.value || 4;
    formData.value.pageNo = currentPage.value;
    formData.value.pageSize = pageSize.value;

    const res = await searchCompetition(formData.value);

    if (res && res.code) {
      competitions.value = res.data.records || [];
      total.value = res.data.total || 0;
    } else {
      competitions.value = [];
      total.value = 0;
      ElMessage.error(res?.msg || "获取竞赛信息失败");
    }
  } catch (error) {
    console.error("分页查询失败:", error);
    competitions.value = [];
    total.value = 0;
    ElMessage.error("网络异常，获取竞赛信息失败");
  }
};

// 搜索按钮点击
const searchCompetitions = () => {
  currentPage.value = 1;
  search();
};

// 分页大小变化
const handleSizeChange = (newSize) => {
  pageSize.value = newSize;
  currentPage.value = 1;
  search();
};

// 页码变化
const handleCurrentChange = (newPage) => {
  currentPage.value = newPage;
  search();
};

// 辅助方法
const userId = computed(() => 101);

const formatCompetitionId = (id) => {
  const paddedId = String(id).padStart(7, "0");
  return `C${paddedId}`;
};

// 完善进度计算功能：(当前日期 - 开始日期) / (结束日期 - 开始日期)
// 若分子大于分母，则定为100%
const calculateProgress = (startDate, endDate) => {
  // 验证日期格式
  const start = new Date(startDate);
  const end = new Date(endDate);
  const now = new Date();

  // 日期无效时返回0
  if (isNaN(start.getTime()) || isNaN(end.getTime())) {
    return 0;
  }

  // 计算时间差（毫秒）
  const timeDiffTotal = end.getTime() - start.getTime();
  const timeDiffCurrent = now.getTime() - start.getTime();

  // 总时间差为0时返回0
  if (timeDiffTotal <= 0) {
    return 0;
  }

  // 计算进度百分比
  let progress = (timeDiffCurrent / timeDiffTotal) * 100;

  // 进度限制在0-100之间
  if (progress < 0) progress = 0;
  if (progress > 100) progress = 100;

  return Math.round(progress);
};

const getStatusKey = (statusCode) => {
  const statusMap = { 0: "pending", 1: "recruiting", 2: "ongoing", 3: "ended" };
  return statusMap[statusCode] || "";
};

// 移除进度状态，不影响进度条显示
const getProgressStatus = () => {
  return ""; // 不设置状态，进度条只显示基础样式
};

const getStatusType = (competitionStatus) => {
  const typeMap = {
    pending: "info",
    recruiting: "warning",
    ongoing: "success",
    ended: "danger",
  };
  return typeMap[getStatusKey(competitionStatus)] || "info";
};

const getStatusText = (competitionStatus) => {
  const textMap = {
    pending: "未开始",
    recruiting: "招募中",
    ongoing: "评审中",
    ended: "已结束",
  };
  return textMap[getStatusKey(competitionStatus)] || "未知";
};

const viewCompetition = (id) => {
  router.push(`/competition/${id}`);
};

const getRowClassName = ({ row, rowIndex }) => {
  return rowIndex % 2 === 0 ? 'even-row' : 'odd-row';
};
</script>

<style scoped>
.competition-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 0;
  background: transparent;
  width: 100%;
  max-width: none;
  margin: 0;
}

.main-content {
  padding: 0;
  background: transparent;
  margin: 0;
}

.content-card {
  margin: 0;
  margin-top: 12px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  background: #fff;
  padding: 24px 32px 24px 32px;
}

.page-header {
  margin-bottom: 2px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  color: #222;
  margin: 0 0 4px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.search-section {
  margin-bottom: 10px;
  padding: 8px 0 0 0;
  background: transparent;
  border-radius: 8px;
}

.filter-row {
  margin-bottom: 0;
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.search-input {
  width: 420px !important;
  min-width: 420px !important;
  max-width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.status-select {
  width: 300px;
  min-width: 120px;
  height: 32px;
}

.status-select :deep(.el-input__wrapper) {
  height: 32px;
  min-height: 32px;
  font-size: 14px;
}

.search-btn {
  width: 100%;
  height: 32px;
  border-radius: 6px;
  font-weight: 600;
  font-size: 14px;
  padding: 0 12px;
  min-width: 100px;
}

.table-section {
  margin-bottom: 16px;
}

.competition-table {
  border-radius: 8px;
  overflow: hidden;
}

.competition-table :deep(.el-table__header) {
  background: #f8f9fa;
}

.competition-table :deep(.el-table__row) {
  transition: background-color 0.2s;
}

.competition-table :deep(.el-table__row:hover) {
  background-color: #f5f7fa;
}

.even-row {
  background-color: #fafbfc;
}

.odd-row {
  background-color: #fff;
}

.competition-id {
  font-family: 'Courier New', monospace;
  font-weight: 600;
  color: #409eff;
  font-size: 13px;
}

.competition-name {
  font-weight: 500;
  color: #333;
  line-height: 1.4;
}

.time-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.time-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.time-label {
  font-size: 12px;
  color: #999;
  min-width: 35px;
}

.time-value {
  font-size: 12px;
  color: #333;
  font-weight: 500;
}

.status-tag {
  font-weight: 500;
  border-radius: 4px;
}

.role-tag {
  font-weight: 500;
  border-radius: 4px;
}

.progress-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}

/* 进度条样式优化：移除状态相关样式，仅保留基础进度显示 */
.progress-bar {
  width: 100%;
}

.progress-bar :deep(.el-progress-bar__outer) {
  background-color: #f3f3f3;
  border-radius: 4px;
}

.progress-bar :deep(.el-progress-bar__inner) {
  background-color: #409eff;
  border-radius: 4px;
}

.action-btn {
  border-radius: 4px;
  font-weight: 500;
}

.pagination-section {
  display: flex;
  justify-content: flex-start;
  margin-top: 24px;
}

.pagination {
  background: transparent;
  padding: 0;
  border-radius: 0;
  box-shadow: none;
}

@media (max-width: 768px) {
  .content-card {
    padding: 16px;
  }

  .search-section {
    padding: 16px;
  }

  .page-title {
    font-size: 20px;
  }

  .competition-table :deep(.el-table) {
    font-size: 12px;
  }

  .search-input {
    width: 100% !important;
    min-width: 100% !important;
  }
}

@media (max-width: 767px) {
  .competition-container {
    padding: 0 4px;
  }
  .content-card {
    padding: 10px 4px;
    margin-top: 8px;
    border-radius: 10px;
  }
  .page-header {
    margin-bottom: 4px;
    padding-top: 10px;
    padding-left: 0;
  }
  .page-title {
    font-size: 18px;
    margin-bottom: 4px;
  }
  .page-subtitle {
    font-size: 13px;
  }
  .search-section {
    padding: 8px 0 0 0;
    margin-bottom: 8px;
  }
  .filter-row {
    gap: 8px;
    flex-direction: column;
  }
  .search-input,
  .status-select {
    width: 100% !important;
    min-width: 0 !important;
    margin-bottom: 8px;
  }
  .search-btn {
    min-width: 80px;
    font-size: 13px;
    padding: 0 8px;
    height: 28px;
  }
  .table-section {
    margin-bottom: 8px;
    overflow-x: auto;
  }
  .competition-table {
    min-width: 800px;
    width: 800px;
    overflow-x: auto;
  }
  .competition-table :deep(.el-table__header),
  .competition-table :deep(.el-table__row) {
    font-size: 12px;
  }
  .competition-table :deep(.el-table__cell) {
    padding: 8px 4px;
  }
  .action-btn {
    font-size: 12px;
    padding: 2px 8px;
    margin-right: 4px;
  }
  .pagination-section {
    margin-top: 12px;
    justify-content: center;
  }
  .el-pagination {
    font-size: 12px;
  }
}
</style>
