<template>
  <div class="team-mgmt-page">
    <el-card class="main-card">
      <!-- 头部区域 -->
      <div class="team-header">
        <div>
          <div class="team-title">队伍管理</div>
          <div class="team-desc">
            管理系统队伍内容，可搜索、查看和管理队伍信息
          </div>
        </div>
      </div>
      <!-- 统计卡片区域 -->
      <el-row :gutter="24" class="team-stats">
        <el-col :xs="24" :sm="8" :md="8" class="stat-col">
          <el-card class="team-stat-card stat-blue">
            <div class="stat-label">队伍数量</div>
            <div class="stat-value">{{ total }}</div>
          </el-card>
        </el-col>
        <!-- <el-col :xs="24" :sm="8" :md="8" class="stat-col">
                    <el-card class="team-stat-card stat-green">
                        <div class="stat-label">已评审</div>
                        <div class="stat-value">{{ reviewedCount }}</div>
                    </el-card>
                </el-col>
                <el-col :xs="24" :sm="8" :md="8" class="stat-col">
                    <el-card class="team-stat-card stat-orange">
                        <div class="stat-label">未评审</div>
                        <div class="stat-value">{{ unreviewedCount }}</div>
                    </el-card>
                </el-col> -->
      </el-row>

      <!-- 搜索栏 -->
      <div class="team-filters">
        <el-input
          v-model="getTeam.team"
          placeholder="搜索队名"
          class="filter-search"
          @keyup.enter="search"
        />
        <el-button type="primary" class="search-btn" @click="search"
          >搜索</el-button
        >
      </div>

      <!-- 队伍列表表格 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        border
        stripe
        :loading="loading"
        :empty-text="loading ? '加载中...' : '暂无队伍数据'"
      >
        <el-table-column prop="teamName" label="队名" min-width="200" />
        <el-table-column prop="leaderName" label="队长名" min-width="100" />
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column prop="teacherName" label="指导老师" min-width="120" />
        <el-table-column prop="teamNum" label="队伍人数" min-width="100" />
        <el-table-column label="操作" min-width="180">
          <template #default="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)"
              >查看详情</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination
        layout="total, sizes, prev, pager, next"
        :total="total"
        :page-sizes="[10, 20, 30]"
        :page-size="getTeam.pageSize"
        :current-page="getTeam.pageNo"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        style="margin-top: 16px; text-align: right"
      />

      <!-- 队伍详情对话框 -->
      <el-dialog
        v-model="dialogVisible"
        title="队伍详情"
        width="55%"
        @closed="handleDialogClose"
      >
        <div class="team-detail">
          <!-- 队伍基本信息 -->
          <div class="basic-info">
            <div class="info-item">
              <span class="label">队名：</span>
              <span class="value">{{ teamDetail.team }}</span>
            </div>
            <div class="info-item">
              <span class="label">指导老师：</span>
              <span class="value">{{ teamDetail.teacher }}</span>
            </div>
          </div>
          <!-- 成员列表 -->
          <div class="member-list">
            <h4 class="list-title">
              成员列表（{{ teamDetail.teamUserList?.length || 0 }}人）
            </h4>
            <el-table
              :data="teamDetail.teamUserList"
              border
              stripe
              :style="{ minWidth: '100%' }"
              :empty-text="
                teamDetail.teamUserList?.length === 0 ? '暂无成员' : '加载中...'
              "
            >
              <el-table-column prop="username" label="用户名" width="120" />
              <el-table-column prop="university" label="大学名" width="180" />
              <el-table-column prop="phone" label="联系电话" width="150" />
              <el-table-column label="成员类型" width="100">
                <template #default="scope">
                  <el-tag
                    :type="scope.row.status === 0 ? 'primary' : 'success'"
                    size="small"
                  >
                    {{ scope.row.status === 0 ? "队长" : "队员" }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="头像" width="80">
                <template #default="scope">
                  <el-avatar
                    :src="scope.row.url"
                    size="small"
                    :alt="scope.row.username"
                    @click="previewAvatar(scope.row.url)"
                  />
                </template>
              </el-table-column>
            </el-table>
          </div>
          <!-- 团队参与的竞赛 -->
          <div class="competition-select">
            <el-select
              v-model="myCompetitionId"
              placeholder="请选择竞赛"
              style="width: 200px; margin-right: 10px"
            >
              <el-option
                v-for="item in competitionList"
                :key="item.competitionId"
                :value="item.competitionId"
                :label="item.competitionName"
              >
              </el-option>
            </el-select>
            <el-button type="primary" @click="searchFig">查看作品</el-button>
          </div>
        </div>

        <!-- 对话框底部按钮 -->
        <template #footer>
          <el-button type="primary" @click="dialogVisible = false"
            >关闭</el-button
          >
        </template>
      </el-dialog>

      <!-- 作品详情对话框（修正后） -->
      <el-dialog
        v-model="workDialogVisible"
        title="作品详情"
        width="70%"
        @closed="resetWorkDetail"
      >
        <div class="work-detail-container">
          <!-- 作品列表（显示图片） -->
          <div class="work-list-section">
            <h4 class="section-title">作品信息</h4>
            <el-table
              :data="workDetail.workList"
              border
              stripe
              style="width: 100%"
              :empty-text="
                workDetail.workList.length === 0 ? '暂无作品' : '加载中...'
              "
            >
              <el-table-column prop="name" label="作品名称" width="220" />
              <!-- 修正：用el-image显示作品图片 -->
              <el-table-column label="作品图片" width="150">
                <template #default="scope">
                  <el-image
                    :src="scope.row.workUrl"
                    :alt="scope.row.name"
                    style="
                      width: 100px;
                      height: 100px;
                      object-fit: cover;
                      cursor: pointer;
                    "
                    @click="previewWorkImage(scope.row.workUrl)"
                  />
                </template>
              </el-table-column>
              <el-table-column prop="description" label="作品描述" />
            </el-table>
          </div>

          <!-- 评论列表（有数据时显示） -->
          <div
            class="comment-list-section"
            v-if="workDetail.commentList.length > 0"
          >
            <h4 class="section-title">评委评论</h4>
            <el-table
              :data="workDetail.commentList"
              border
              stripe
              style="width: 100%"
              :empty-text="
                workDetail.commentList.length === 0 ? '暂无评论' : '加载中...'
              "
            >
              <el-table-column prop="judgeName" label="评委名称" width="150" />
              <el-table-column label="评委头像" width="100">
                <template #default="scope">
                  <el-avatar
                    :src="scope.row.judgeAvatar"
                    size="small"
                    :alt="scope.row.judgeName"
                  />
                </template>
              </el-table-column>
              <el-table-column prop="score" label="评分" width="120" />
              <el-table-column prop="comment" label="评论内容" />
            </el-table>
          </div>
        </div>

        <!-- 作品对话框底部按钮 -->
        <template #footer>
          <el-button type="primary" @click="workDialogVisible = false"
            >关闭</el-button
          >
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from "vue";
import {
  getWithLike,
  getTeamById,
  getCompetitionByTeamId,
  getTeamWork,
} from "@/apis/admin/admin-team";
import {
  ElMessage,
  ElDialog,
  ElTable,
  ElTableColumn,
  ElAvatar,
  ElTag,
  ElRate,
  ElImage,
} from "element-plus";

// 搜索与分页参数
const getTeam = reactive({
  team: "", // 搜索关键词（队名）
  pageNo: 1, // 当前页码
  pageSize: 10, // 每页条数
});

// 表格数据
const tableData = ref([]);
const total = ref(0); // 总记录数
const loading = ref(false); // 加载状态

// 统计卡片数据（请根据实际业务逻辑补充统计）
const reviewedCount = computed(
  () => tableData.value.filter((item) => item.reviewed === true).length
);
const unreviewedCount = computed(
  () => tableData.value.filter((item) => item.reviewed === false).length
);

// 竞赛与作品相关参数
const myCompetitionId = ref("");
const myTeamId = ref("");
const competitionList = ref([]);

// 队伍详情对话框状态与数据
const dialogVisible = ref(false);
const teamDetail = ref({
  id: "",
  team: "",
  teacher: "",
  createTime: "",
  teamUserList: [],
});

// 作品详情对话框状态与数据（修正后）
const workDialogVisible = ref(false);
const workDetail = ref({
  workList: [], // 作品列表（后端返回）
  commentList: [], // 评论列表（后端返回）
});

// 初始化加载队伍列表
onMounted(() => {
  search();
});

// 获取队伍列表（支持搜索和分页）
const search = async () => {
  loading.value = true;
  try {
    const res = await getWithLike(getTeam);
    if (res.code) {
      tableData.value = res.data.records || [];
      total.value = res.data.total || 0;
    } else {
      ElMessage.error(res.msg || "获取队伍列表失败");
    }
  } catch (error) {
    ElMessage.error("请求出错，请重试");
    console.error("获取队伍列表失败:", error);
  } finally {
    loading.value = false;
  }
};

// 查看队伍详情
const handleEdit = async (row) => {
  try {
    myTeamId.value = row.id;
    // 获取队伍基本信息
    const teamRes = await getTeamById(row.id);
    if (teamRes.code) {
      teamDetail.value = teamRes.data;
      dialogVisible.value = true;
    } else {
      ElMessage.error(teamRes.msg || "获取队伍详情失败");
    }
    // 获取队伍参与的竞赛
    const compRes = await getCompetitionByTeamId(row.id);
    if (compRes.code) {
      competitionList.value = compRes.data;
    } else {
      ElMessage.error(compRes.msg || "获取竞赛列表失败");
    }
  } catch (error) {
    ElMessage.error("请求出错，请重试");
    console.error("获取队伍详情失败:", error);
  }
};

// 查看作品详情（修正后：解决对话框不显示问题）
const searchFig = async () => {
  // 校验竞赛选择
  if (!myCompetitionId.value) {
    ElMessage.warning("请先选择要查看的竞赛");
    return;
  }

  try {
    const res = await getTeamWork(myTeamId.value, myCompetitionId.value);
    if (res.code) {
      // 修正：赋值整个data对象（包含workList和commentList）
      workDetail.value = res.data;
      workDialogVisible.value = true; // 打开作品对话框
    } else {
      ElMessage.error(res.msg || "获取作品信息失败");
    }
  } catch (error) {
    ElMessage.error("请求出错，请重试");
    console.error("获取作品信息失败:", error);
  }
};

// 预览头像
const previewAvatar = (url) => {
  if (url) {
    window.open(url, "_blank");
  } else {
    ElMessage.warning("暂无头像");
  }
};

// 预览作品图片（新增：点击图片放大）
const previewWorkImage = (url) => {
  if (url) {
    window.open(url, "_blank"); // 新窗口打开大图
  } else {
    ElMessage.warning("暂无作品图片");
  }
};

// 分页切换
const handleCurrentChange = (page) => {
  getTeam.pageNo = page;
  search();
};

// 每页条数切换
const handleSizeChange = (size) => {
  getTeam.pageSize = size;
  getTeam.pageNo = 1;
  search();
};

// 重置队伍详情数据（对话框关闭时）
const handleDialogClose = () => {
  teamDetail.value = {
    id: "",
    team: "",
    teacher: "",
    createTime: "",
    teamUserList: [],
  };
  myCompetitionId.value = ""; // 重置竞赛选择
};

// 重置作品详情数据（对话框关闭时）
const resetWorkDetail = () => {
  workDetail.value = {
    workList: [],
    commentList: [],
  };
};
</script>

<style scoped>
.team-mgmt-page {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}
.main-card {
  padding: 24px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  background-color: #fff;
}
.team-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}
.team-title {
  font-size: 22px;
  font-weight: bold;
  color: #222e3c;
  margin-bottom: 6px;
}
.team-desc {
  color: #888;
  font-size: 14px;
}

.team-stats {
  margin-bottom: 30px;
}

.stat-col {
  margin-bottom: 16px;
}

.team-stat-card {
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 10px 20px;
  box-sizing: border-box;
}

.team-stat-card:hover {
  transform: translateY(-3px);
}

.stat-blue {
  background: linear-gradient(135deg, #1976d2 60%, #42a5f5 100%);
}

.stat-green {
  background: linear-gradient(135deg, #43a047 60%, #66bb6a 100%);
}

.stat-orange {
  background: linear-gradient(135deg, #ff9800 60%, #ffc107 100%);
}

.stat-label {
  font-size: 16px;
  margin-bottom: 6px;
  opacity: 0.9;
  color: #fff;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #fff;
}

.team-filters {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
  align-items: center;
}

.filter-search {
  flex: 1;
  min-width: 200px;
}

.search-btn {
  padding: 8px 20px;
  white-space: nowrap;
  flex-shrink: 0;
}

/* 队伍详情弹窗美化（去卡片化，增强动态与现代感） */
.team-detail {
  background: linear-gradient(135deg, #fafdff 60%, #e8f0fe 100%);
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(25,118,210,0.04);
  padding: 24px 18px 18px 18px;
}

.basic-info {
  display: flex;
  flex-wrap: wrap;
  gap: 20px 40px;
  padding: 15px 0 10px 0;
  border-bottom: 1.5px dashed #dbeafe;
  margin-bottom: 0;
  background: none;
  box-shadow: none;
}

.info-item .label {
  color: #1976d2;
  font-weight: 600;
  font-size: 15px;
}
.info-item .value {
  color: #333;
  font-size: 15px;
}

.member-list {
  margin-top: 10px;
  padding: 0 0 10px 0;
  background: none;
  border-radius: 0;
  box-shadow: none;
  border: none;
}

.list-title {
  border-left: 3px solid #1976d2;
  padding-left: 8px;
  margin-bottom: 10px;
  font-size: 15px;
  color: #1976d2;
  letter-spacing: 1px;
}

.el-avatar {
  border: none;
  transition: transform 0.25s cubic-bezier(.4,2,.6,1), box-shadow 0.2s;
}
.el-avatar:hover {
  transform: scale(1.18) rotate(-6deg);
  box-shadow: 0 4px 16px rgba(25,118,210,0.13);
}

.competition-select {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin-top: 20px;
  padding-top: 12px;
  border-top: 1px dashed #dbeafe;
  gap: 10px;
}

.competition-select .el-button {
  border-radius: 18px;
  background: linear-gradient(90deg, #42a5f5 0%, #1976d2 100%);
  color: #fff;
  font-weight: 500;
  transition: transform 0.18s, box-shadow 0.18s;
}
.competition-select .el-button:hover {
  transform: scale(1.08);
  box-shadow: 0 2px 10px rgba(25,118,210,0.13);
}

/* 作品详情弹窗美化 */
.work-detail-container {
  background: linear-gradient(135deg, #fafdff 60%, #e8f0fe 100%);
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(25,118,210,0.04);
  padding: 24px 18px 18px 18px;
}

.work-list-section .el-image {
  border-radius: 7px;
  box-shadow: none;
  transition: transform 0.25s cubic-bezier(.4,2,.6,1), box-shadow 0.2s;
}
.work-list-section .el-image:hover {
  transform: scale(1.13) rotate(3deg);
  box-shadow: 0 4px 16px rgba(25,118,210,0.13);
}

.el-table .el-table__cell {
  font-size: 15px;
}

.el-table .el-table__header th {
  background: #f0f4fa;
  color: #1976d2;
  font-weight: 600;
}

.el-table__row:hover > td {
  background: #e3f2fd !important;
  transition: background 0.18s;
}

.section-title {
  border-left: 3px solid #1976d2;
  padding-left: 8px;
  margin-bottom: 10px;
  font-size: 15px;
  color: #1976d2;
  letter-spacing: 1px;
}

.comment-list-section .el-avatar {
  border: none;
}
.comment-list-section .el-table__cell:last-child {
  background: #f6fafd;
  border-radius: 6px;
  padding: 8px 12px;
  color: #444;
  font-size: 15px;
}
</style>
