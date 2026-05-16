<template>
  <div class="message-management-container">
    <div class="content-card">
      <div class="page-header">
        <h2 class="page-title">我的消息</h2>
        <p class="page-subtitle">管理和查看你收到和发送的所有消息</p>
      </div>
      <el-divider class="header-divider" />
      <el-tabs v-model="activeTab" class="info-tabs">
        <!-- 信息分类标签页 -->
        <el-tab-pane
          label="收到的邀请"
          name="received"
          @click="handleReceivedTabClick"
        >
          <!-- 搜索区域（状态筛选） -->
          <el-row class="search-bar">
            <el-col :span="8">
              <el-select
                v-model="receivedSearch.status"
                placeholder="筛选状态"
                clearable
                @change="handleReceivedSearch"
              >
                <el-option label="待处理" value="0" />
                <el-option label="已接受" value="1" />
                <el-option label="已拒绝" value="2" />
                <el-option label="已撤回" value="3" />
              </el-select>
            </el-col>
          </el-row>
          <div class="empty-tip" v-if="pagedReceivedInvites.length === 0">
            <el-empty description="暂无收到的邀请" />
          </div>
          <el-table
            :data="pagedReceivedInvites"
            border
            stripe
            class="info-table"
            :cell-style="{ padding: '12px 0' }"
          >
            <!-- 表格列定义不变 -->
            <el-table-column prop="name" label="邀请人"></el-table-column>
            <el-table-column prop="content" label="邀请内容">
              <template #default="scope">
                <div class="content-cell">{{ scope.row.content }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="time" label="发送时间" width="160" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag
                  :type="statusTypeMap[scope.row.status]?.type || 'default'"
                >
                  {{ statusTypeMap[scope.row.status]?.text || "未知状态" }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180">
              <template #default="scope">
                <div class="operation-group" v-if="scope.row.status === 0">
                  <el-button
                    type="success"
                    size="small"
                    @click="handleAccept(scope.row)"
                    >接受</el-button
                  >
                  <el-button
                    type="danger"
                    size="small"
                    @click="handleReject(scope.row)"
                    >拒绝</el-button
                  >
                </div>
                <div class="operation-group" v-else>
                  <span class="status-text">{{
                    statusTypeMap[scope.row.status]?.text || "未知状态"
                  }}</span>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页控件：使用后端返回的总条数 -->
          <el-pagination
            style="margin-top: 20px; text-align: right"
            @size-change="handleReceivedSizeChange"
            @current-change="handleReceivedCurrentChange"
            :current-page="receivedCurrentPage"
            :page-sizes="[5, 10, 20]"
            :page-size="receivedPageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="receivedTotal"
          >
            <!-- 关键修改：使用后端返回的总条数 -->
          </el-pagination>
        </el-tab-pane>

        <!-- 发送的邀请 -->
        <el-tab-pane label="发送的邀请" name="sent" @click="handleSentTabClick">
          <el-row class="search-bar">
            <el-col :span="8">
              <el-select
                v-model="sentSearch.status"
                placeholder="筛选状态"
                clearable
                @change="handleSentSearch"
              >
                <el-option label="待处理" value="0" />
                <el-option label="已接受" value="1" />
                <el-option label="已拒绝" value="2" />
                <el-option label="已撤回" value="3" />
              </el-select>
            </el-col>
          </el-row>
          <div class="empty-tip" v-if="pagedSentInvites.length === 0">
            <el-empty description="暂无发送的邀请" />
          </div>
          <el-table
            :data="pagedSentInvites"
            border
            stripe
            class="info-table"
            :cell-style="{ padding: '12px 0' }"
          >
            <!-- 表格列定义不变 -->
            <el-table-column prop="name" label="接收人" width="140" />
            <el-table-column prop="content" label="邀请内容">
              <template #default="scope">
                <div class="content-cell">{{ scope.row.content }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="time" label="发送时间" width="160" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag
                  :type="statusTypeMap[scope.row.status]?.type || 'default'"
                >
                  {{ statusTypeMap[scope.row.status]?.text || "未知状态" }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button
                  type="text"
                  size="small"
                  text-color="#ff4d4f"
                  @click="handleWithdraw(scope.row)"
                  :disabled="!canWithdraw(scope.row)"
                >
                  撤回
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页控件：使用后端返回的总条数 -->
          <el-pagination
            style="margin-top: 20px; text-align: right"
            @size-change="handleSentSizeChange"
            @current-change="handleSentCurrentChange"
            :current-page="sentCurrentPage"
            :page-sizes="[5, 10, 20]"
            :page-size="sentPageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="sentTotal"
          >
            <!-- 关键修改：使用后端返回的总条数 -->
          </el-pagination>
        </el-tab-pane>

        <!-- 其他信息 -->
        <el-tab-pane
          label="其他信息"
          name="others"
          @click="handleOthersTabClick"
        >
          <div class="empty-tip" v-if="pagedOtherInfo.length === 0">
            <el-empty description="暂无其他信息" />
          </div>
          <el-table
            :data="pagedOtherInfo"
            border
            stripe
            class="info-table"
            :cell-style="{ padding: '12px 0' }"
          >
            <!-- 表格列定义不变 -->
            <!-- <el-table-column prop="name" label="来源" width="140" /> -->
            <el-table-column prop="content" label="信息内容">
              <template #default="scope">
                <div class="content-cell">{{ scope.row.content }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="time" label="发送时间" width="160" />
          </el-table>
          <!-- 分页控件：使用后端返回的总条数 -->
          <el-pagination
            style="margin-top: 20px; text-align: right"
            @size-change="handleOthersSizeChange"
            @current-change="handleOthersCurrentChange"
            :current-page="othersCurrentPage"
            :page-sizes="[5, 10, 20]"
            :page-size="othersPageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="othersTotal"
          >
            <!-- 关键修改：使用后端返回的总条数 -->
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from "vue";
import {
  getSendInform,
  getReceiveInform,
  updateStatus,
  getUser,
  sendInform,
} from "@/apis/person";
import { ElMessageBox, ElMessage } from "element-plus";
import { usePersonStore } from "@/stores/person";
const personStore = usePersonStore();
// 表单数据
const formData = ref({
  id: "",
  status: "",
  newStatus: "",
  teamId: "",
});

// 页面加载时执行搜索
onMounted(() => {
  // 初始加载收到的邀请
  search1();
  search2();
  search3();
});

// 存储后端返回的总记录数（关键新增）
const receivedTotal = ref(0);
const sentTotal = ref(0);
const othersTotal = ref(0);

// 标签页点击事件处理
const handleReceivedTabClick = () => {
  if (activeTab.value == "received") {
    search1();
  }
};

const handleSentTabClick = () => {
  if (activeTab.value == "sent") {
    search2();
  }
};

const handleOthersTabClick = () => {
  if (activeTab.value == "others") {
    search3();
  }
};

// 搜索函数（分开实现）
const search1 = async () => {
  try {
    // 1. 获取当前用户ID
    const userRes = await getUser();
    if (!userRes || userRes.code !== 1 || !userRes.data?.id) {
      console.error("获取用户ID失败:", userRes?.msg || "用户信息格式错误");
      return;
    }
    const userId = userRes.data.id;

    // 2. 创建查询参数（包含筛选状态和分页参数）
    const createParams = (
      sendId,
      receiveId,
      category,
      status,
      pageNo,
      pageSize
    ) => ({
      sendId,
      receiveId,
      category,
      status, // 传递筛选状态到后端
      pageNo,
      pageSize,
    });

    // 3. 查询收到的邀请（传递筛选状态）
    const receivedParams = createParams(
      null,
      userId,
      0,
      receivedSearch.status, // 筛选状态
      receivedCurrentPage.value,
      receivedPageSize.value
    );
    const receivedRes = await getReceiveInform(receivedParams);
    if (receivedRes.code === 1) {
      receivedInviteList.value = Array.isArray(receivedRes.data?.records)
        ? receivedRes.data.records.map((item) => ({
            ...item,
            status: Number(item.status),
          }))
        : [];
      receivedTotal.value = receivedRes.data?.total || 0; // 保存总条数
    } else {
      console.error("获取收到的邀请失败:", receivedRes.msg || "未知错误");
      receivedInviteList.value = [];
      receivedTotal.value = 0;
    }
  } catch (error) {
    console.error("搜索过程异常:", error);
    receivedInviteList.value = [];
    receivedTotal.value = 0;
  }
};

const search2 = async () => {
  try {
    // 1. 获取当前用户ID
    const userRes = await getUser();
    if (!userRes || userRes.code !== 1 || !userRes.data?.id) {
      console.error("获取用户ID失败:", userRes?.msg || "用户信息格式错误");
      return;
    }
    const userId = userRes.data.id;

    // 2. 创建查询参数（包含筛选状态和分页参数）
    const createParams = (
      sendId,
      receiveId,
      category,
      status,
      pageNo,
      pageSize
    ) => ({
      sendId,
      receiveId,
      category,
      status, // 传递筛选状态到后端
      pageNo,
      pageSize,
    });

    // 4. 查询发送的邀请（传递筛选状态）
    const sentParams = createParams(
      userId,
      null,
      0,
      sentSearch.status, // 筛选状态
      sentCurrentPage.value,
      sentPageSize.value
    );
    const sentRes = await getSendInform(sentParams);
    if (sentRes.code === 1) {
      sentInviteList.value = Array.isArray(sentRes.data?.records)
        ? sentRes.data.records.map((item) => ({
            ...item,
            status: Number(item.status),
          }))
        : [];
      sentTotal.value = sentRes.data?.total || 0; // 保存总条数
    } else {
      console.error("获取发送的邀请失败:", sentRes.msg || "未知错误");
      sentInviteList.value = [];
      sentTotal.value = 0;
    }
  } catch (error) {
    console.error("搜索过程异常:", error);
    sentInviteList.value = [];
    sentTotal.value = 0;
  }
};

const search3 = async () => {
  try {
    // 1. 获取当前用户ID
    const userRes = await getUser();
    if (!userRes || userRes.code !== 1 || !userRes.data?.id) {
      console.error("获取用户ID失败:", userRes?.msg || "用户信息格式错误");
      return;
    }
    const userId = userRes.data.id;

    // 2. 创建查询参数（包含筛选状态和分页参数）
    const createParams = (
      sendId,
      receiveId,
      category,
      status,
      pageNo,
      pageSize
    ) => ({
      sendId,
      receiveId,
      category,
      status, // 传递筛选状态到后端
      pageNo,
      pageSize,
    });

    // 5. 查询其他信息
    const otherParams = createParams(
      userId,
      userId,
      1,
      null,
      othersCurrentPage.value,
      othersPageSize.value
    );
    const otherRes = await getReceiveInform(otherParams);
    if (otherRes.code === 1) {
      otherInfoList.value = Array.isArray(otherRes.data?.records)
        ? otherRes.data.records.map((item) => ({
            ...item,
            status: Number(item.status),
          }))
        : [];
      othersTotal.value = otherRes.data?.total || 0; // 保存总条数
    } else {
      console.error("获取其他信息失败:", otherRes.msg || "未知错误");
      otherInfoList.value = [];
      othersTotal.value = 0;
    }
  } catch (error) {
    console.error("搜索过程异常:", error);
    otherInfoList.value = [];
    othersTotal.value = 0;
  }
};

// 标签页激活状态
const activeTab = ref("received");

// 状态类型映射
const statusTypeMap = {
  0: { text: "待处理", type: "warning" },
  1: { text: "已接受", type: "success" },
  2: { text: "已拒绝", type: "danger" },
  3: { text: "已撤回", type: "info" },
};

// 搜索条件（状态筛选）
const receivedSearch = reactive({
  status: null,
});
const sentSearch = reactive({
  status: null,
});

// 分页参数
const receivedCurrentPage = ref(1);
const receivedPageSize = ref(10);
const sentCurrentPage = ref(1);
const sentPageSize = ref(10);
const othersCurrentPage = ref(1);
const othersPageSize = ref(10);

// 原始数据列表
const receivedInviteList = ref([]);
const sentInviteList = ref([]);
const otherInfoList = ref([]);

// 分页后的数据（无需本地筛选，直接使用接口返回的当前页数据）
const pagedReceivedInvites = computed(() => receivedInviteList.value);
const pagedSentInvites = computed(() => sentInviteList.value);
const pagedOtherInfo = computed(() => otherInfoList.value);

// 筛选事件处理（修改：重置页码并重新搜索）
const handleReceivedSearch = () => {
  receivedCurrentPage.value = 1; // 重置页码
  search1(); // 重新请求后端带筛选条件的数据
};

const handleSentSearch = () => {
  sentCurrentPage.value = 1;
  search2();
};

// 分页事件处理（页码/页大小变化时重新搜索）
const handleReceivedSizeChange = (newSize) => {
  receivedPageSize.value = newSize;
  receivedCurrentPage.value = 1;
  search1();
};

const handleReceivedCurrentChange = (newPage) => {
  receivedCurrentPage.value = newPage;
  search1();
};

const handleSentSizeChange = (newSize) => {
  sentPageSize.value = newSize;
  sentCurrentPage.value = 1;
  search2();
};

const handleSentCurrentChange = (newPage) => {
  sentCurrentPage.value = newPage;
  search2();
};

const handleOthersSizeChange = (newSize) => {
  othersPageSize.value = newSize;
  othersCurrentPage.value = 1;
  search3();
};

const handleOthersCurrentChange = (newPage) => {
  othersCurrentPage.value = newPage;
  search3();
};

// 邀请操作逻辑（不变）
const handleAccept = async (row) => {
  if (row.status === 0) {
    formData.value = {
      id: row.id,
      status: row.status,
      newStatus: 1,
      teamId: row.teamId,
    };
    ElMessageBox.confirm("是否接受对方的邀请？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(async () => {
        const res = await updateStatus(formData.value);
        if (res.code) {
          ElMessage.success("操作成功");
          search1(); // 只更新当前标签页数据
        } else {
          ElMessage.error("操作失败");
        }
      })
      .catch(() => {
        ElMessage.info("已取消操作");
      });
  }
};

const handleReject = async (row) => {
  if (row.status === 0) {
    formData.value = {
      id: row.id,
      status: row.status,
      newStatus: 2,
      teamId: row.teamId,
    };
    ElMessageBox.confirm("是否拒绝对方的邀请？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(async () => {
        const res = await updateStatus(formData.value);
        if (res.code) {
          ElMessage.success("已拒绝");
          search1(); // 只更新当前标签页数据
        }
      })
      .catch(() => {
        ElMessage.info("已取消操作");
      });
  }
};

const handleWithdraw = (row) => {
  if (canWithdraw(row)) {
    formData.value = {
      id: row.id,
      status: row.status,
      newStatus: 3,
      teamId: row.teamId,
    };
    ElMessageBox.confirm("是否撤回邀请？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(async () => {
        const res = await updateStatus(formData.value);
        if (res.code) {
          ElMessage.success("已撤回");
          search2(); // 只更新当前标签页数据
        }
      })
      .catch(() => {
        ElMessage.info("已取消操作");
      });
  }
};

const canWithdraw = (row) => row.status === 0;
</script>

<style scoped>
.message-management-container {
  padding: 0;
  background: transparent;
  width: 100%;
  max-width: none;
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
  padding-top: 24px;
  margin-bottom: 8px;
  padding-left: 8px;
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

.header-divider {
  margin: 0 0 16px 0;
}

.info-tabs {
  margin-bottom: 0;
}

.search-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.search-bar .el-col {
  margin-right: 10px;
}

.empty-tip {
  margin: 40px 0;
  text-align: center;
}

.info-table {
  border-radius: 8px;
  overflow: hidden;
  font-size: 15px;
}

.info-table :deep(.el-table__header) {
  background: #f8f9fa;
  color: #333;
  font-weight: 600;
}

.info-table :deep(.el-table__row) {
  transition: background-color 0.2s;
}

.info-table :deep(.el-table__row:hover) {
  background-color: #f5f7fa;
}

.info-table :deep(.el-table__cell) {
  text-align: center;
  vertical-align: middle;
  padding: 12px 0;
}

.info-table :deep(.el-table__cell .el-button) {
  border-radius: 4px;
  font-weight: 500;
  font-size: 14px;
  padding: 4px 14px;
}

.info-table :deep(.el-table__cell .el-button + .el-button) {
  margin-left: 8px;
}

.pagination-container,
.info-table + .el-pagination {
  display: flex;
  justify-content: flex-start;
  margin-top: 24px;
  background: transparent;
  padding: 0;
  border-radius: 0;
  box-shadow: none;
}

.content-cell {
  line-height: 1.5;
  max-height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.operation-group {
  display: flex;
  gap: 8px;
}

.status-text {
  color: #606266;
  font-size: 14px;
}

/* 移动端适配 */
@media (max-width: 767px) {
  .message-management-container {
    padding: 0 4px;
  }
  .content-card {
    padding: 10px 4px;
    margin-top: 8px;
    border-radius: 10px;
  }
  .page-header {
    padding-top: 10px;
    margin-bottom: 4px;
    padding-left: 0;
  }
  .page-title {
    font-size: 18px;
    margin-bottom: 4px;
  }
  .page-subtitle {
    font-size: 13px;
  }
  .header-divider {
    margin: 0 0 8px 0;
  }
  .info-tabs {
    margin-bottom: 0;
  }
  .search-bar {
    margin-bottom: 8px;
    flex-direction: column;
    align-items: stretch;
  }
  .search-bar .el-col {
    margin-right: 0;
    margin-bottom: 8px;
  }
  .search-bar .el-select {
    width: 100%;
  }
  .empty-tip {
    margin: 12px 0;
  }
  .info-table {
    font-size: 12px;
    overflow-x: auto;
    min-width: 600px;
    min-width: 800px;
    width: 800px;
  }
  .info-table :deep(.el-table__header),
  .info-table :deep(.el-table__row) {
    font-size: 12px;
  }
  .info-table :deep(.el-table__cell) {
    padding: 8px 4px;
  }
  .info-table :deep(.el-table__cell .el-button) {
    font-size: 11px;
    padding: 4px 8px;
    margin-left: 4px;
  }
  .content-cell {
    font-size: 12px;
    max-height: 40px;
    -webkit-line-clamp: 1;
    line-clamp: 1;
  }
  .operation-group {
    gap: 4px;
  }
  .status-text {
    font-size: 12px;
  }
  .pagination-container,
  .info-table + .el-pagination {
    margin-top: 12px;
    justify-content: center;
  }
  .el-pagination {
    font-size: 12px;
  }
  .el-pagination :deep(.el-pagination__total),
  .el-pagination :deep(.el-pagination__sizes),
  .el-pagination :deep(.el-pagination__jump) {
    font-size: 12px;
  }
  :deep(.el-tabs__header) {
    margin-bottom: 8px;
  }
  :deep(.el-tabs__nav-wrap) {
    padding: 0 4px;
  }
  :deep(.el-tabs__item) {
    font-size: 13px;
    padding: 0 8px;
  }
  :deep(.el-tabs__content) {
    padding: 0;
  }
  :deep(.el-tag) {
    font-size: 11px;
    padding: 2px 6px;
  }
}

/* 平板端适配 */
@media (min-width: 768px) and (max-width: 1023px) {
  .content-card {
    padding: 20px 16px;
  }

  .page-title {
    font-size: 20px;
  }

  .info-table {
    font-size: 13px;
  }

  .search-bar .el-col {
    margin-right: 8px;
  }
}
</style>
