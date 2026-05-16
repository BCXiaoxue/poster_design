<template>
  <div class="users-mgmt-page">
    <el-card class="main-card">
      <!-- 头部区域 -->
      <div class="users-header">
        <div>
          <div class="users-title">用户管理</div>
          <div class="users-desc">
            当前共计{{ userCount.userCount }}名用户，支持分组、搜索、批量管理等功能
          </div>
        </div>
      </div>

      <!-- 统计卡片 -->
      <el-row :gutter="24" class="news-stats">
        <el-col :xs="24" :sm="8" :md="8" class="stat-col">
          <el-card class="news-stat-card stat-blue">
            <div class="stat-label">用户总数</div>
            <div class="stat-value">{{ totalUserCount }}</div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" class="stat-col">
          <el-card class="news-stat-card stat-green">
            <div class="stat-label">普通用户</div>
            <div class="stat-value">{{ userCount.userCount }}</div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" class="stat-col">
          <el-card class="news-stat-card stat-orange">
            <div class="stat-label">管理员</div>
            <div class="stat-value">{{ userCount.adminCount }}</div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 筛选区域 -->
      <div class="users-filters">
        <el-select v-model="searchQuery.status" placeholder="用户状态" class="filter-select" clearable>
          <el-option label="正常" value="0">正常</el-option>
          <el-option label="禁用" value="1">禁用</el-option>
        </el-select>

        <el-select v-model="searchQuery.authority" placeholder="筛选身份" class="filter-select" clearable>
          <el-option label="普通用户" value="0">普通用户</el-option>
          <el-option label="管理员" value="1">管理员</el-option>
        </el-select>
        <el-input v-model="searchQuery.username" placeholder="搜索用户" class="filter-search" />
        <el-button type="primary" class="search-btn" @click="search()">搜索</el-button>
      </div>

      <!-- 用户表格 -->
      <el-table :data="userList" border stripe class="users-table">
        <el-table-column label="用户信息" min-width="280">
          <template #default="scope">
            <div class="user-info-cell">
              <img v-lazy :src="scope.row.avatarUrl" class="user-avatar" />
              <div>
                <div class="user-name">{{ scope.row.username }}</div>
                <div class="user-email">{{ scope.row.email }}</div>
                <div class="user-phone">{{ scope.row.phone }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="性别">
          <template #default="scope">
            <el-tag :type="genderTypeMap[scope.row.gender]">
              {{ scope.row.gender === 1 ? '男' : (scope.row.gender === 2 ? '女' : '未知') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="birthday" label="出生日期" />
        <el-table-column prop="university" label="大学" />
        <el-table-column label="身份">
          <template #default="scope">
            <el-tag :type="scope.row.authority">
              {{ scope.row.authority==0?'普通用户':'管理员' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLogin" label="最后登录时间" />
        <el-table-column label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status == '0' ? 'success' : 'danger'">
              {{ scope.row.status==0 ? '正常' : (scope.row.status==1 ? '禁用' : '未知') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button
              :type="scope.row.status == 0 ? 'danger' : 'success'"
              size="small"
              @click="changeStatus(scope.row.id,scope.row.status)"
            >{{ scope.row.status == 0 ? '禁用' : '解禁' }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="demo-pagination-block">
        <el-pagination
          v-model:current-page="searchQuery.pageNo"
          v-model:page-size="searchQuery.pageSize"
          :page-sizes="[10,50,100,200]"
          :size="size"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 查看用户弹窗 -->
    <el-dialog title="用户详情" v-model="showUserDetail" :width="600">
      <div v-if="currentUser" class="user-detail">
        <div class="detail-row">
          <span class="detail-label">姓名：</span>
          <span class="detail-value">{{ currentUser.name }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">邮箱：</span>
          <span class="detail-value">{{ currentUser.email }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">电话：</span>
          <span class="detail-value">{{ currentUser.phone }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">身份：</span>
          <span class="detail-value">{{ currentUser.identity }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">参赛数量：</span>
          <span class="detail-value">{{ currentUser.competitionCount }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">状态：</span>
          <span class="detail-value">{{ currentUser.status }}</span>
        </div>
      </div>
    </el-dialog>

    <!-- 禁用确认弹窗 -->
    <el-dialog title="确认禁用" v-model="showDisableConfirm" :width="400">
      <p>
        确定要禁用用户
        <span class="user-to-disable">{{ userToDisable.name }}</span> 吗？
      </p>
      <template #footer>
        <el-button @click="showDisableConfirm = false">取消</el-button>
        <el-button type="danger" @click="confirmDisable">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import { ElMessage } from "element-plus";
import { getUserInfo,queryUserByName } from "@/apis/user.js"
import { countUserAll,updateUserStatus,getUserByPermissionAndName } from "@/apis/admin.js"
import { computed } from "vue";

// 用户数据
const userList = ref([]);
const userCount = ref({
  userCount: 0,
  adminCount: 0
})
// 新增：用户总数
const totalUserCount = computed(() => (userCount.value.userCount || 0) + (userCount.value.adminCount || 0));

onMounted(async()=>{
  await search()
  const result=await countUserAll();
  if(result.code){
    userCount.value = result.data
  }
  const result2 = await getUserInfo();
  if(result2.code){
    userId.value = result2.data.id
    // console.log(userId.value);
    
  }
})


const genderTypeMap = {
  1: 'success', // 男 -> success
  2: 'warning', // 女 -> warning
  0: 'info'     // 未知 -> info
};



// 弹窗控制
const showUserDetail = ref(false);
const showDisableConfirm = ref(false);
const currentUser = ref(null);
const userToDisable = ref(null);

// 查看用户详情
const handleView = (user) => {
  currentUser.value = { ...user };
  showUserDetail.value = true;
};

// 处理禁用操作
const handleDisable = (user) => {
  userToDisable.value = { ...user };
  showDisableConfirm.value = true;
};

// 确认禁用
const confirmDisable = () => {
  ElMessage.success(`用户 ${userToDisable.value.name} 已禁用`);
  showDisableConfirm.value = false;

  // 更新用户状态
  const index = userList.value.findIndex(
    (u) => u.id === userToDisable.value.id
  );
  if (index !== -1) {
    userList.value[index].status = "已禁用";
  }
};

// 初始化图表
onMounted(() => {
  nextTick(() => {
    if (window.echarts) {
      const chart = window.echarts.init(
        document.getElementById("usersGrowthChart")
      );
      chart.setOption({
        color: ["#409eff", "#ff9800"],
        tooltip: {},
        legend: { data: ["本月", "上月"] },
        xAxis: { data: ["1月", "2月", "3月", "4月", "5月", "6月"] },
        yAxis: {},
        series: [
          { name: "本月", type: "bar", data: [12, 18, 25, 32, 40, 60] },
          { name: "上月", type: "bar", data: [10, 15, 20, 28, 35, 45] },
        ],
      });

      // 监听窗口大小变化，重绘图表
      window.addEventListener("resize", () => {
        chart.resize();
      });
    }
  });
});

// 分页查询
const size = ref('default')
const background = ref(false)
const disabled = ref(false)
const total = ref(0);
const searchQuery = ref({
  username: '',
  authority: '',
  status: '',
  pageNo: '1',
  pageSize: '10'
});
const handleSizeChange = (val) => {
  searchQuery.value.pageSize = val
  search()
}
const handleCurrentChange = (val) => {
  searchQuery.value.pageNo = val
  search()
}



const search = async () => { 
  const result = await getUserByPermissionAndName(searchQuery.value);
  if(result.code){
    userList.value = result.data.records;
    size.value = result.data.page
    searchQuery.value.pageNo = result.data.current
    searchQuery.value.pageSize = result.data.size
    total.value = result.data.total
  }else{
    userList.value = [];
  }
  
  
};

// 更改状态
const statusData = ref({
  id: "",
  status: ""
});
const changeStatus = async (id,statu) => {
  statusData.value.id = id; 
  statusData.value.status = 1-statu;
  const result = await updateUserStatus(statusData.value);
  if(result.code){
    search();
    ElMessage.success(result.data);
  }else{
    ElMessage.error(result.data);
  }
  

  
};
</script>

<style scoped>
.main-card {
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.users-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.users-title {
  font-size: 22px;
  font-weight: bold;
  color: #222e3c;
  margin-bottom: 4px;
}

.users-desc {
  color: #888;
  font-size: 14px;
}

.users-actions {
  display: flex;
  gap: 12px;
}

.users-stats {
  margin-bottom: 20px;
  padding-bottom: 20px;
}

.stat-col {
  margin-bottom: 15px;
}

.news-stat-card {
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 10px 20px;
  box-sizing: border-box;
  border-radius: 10px;
  color: #fff;
  transition: transform 0.18s;
}
.news-stat-card:hover {
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

.stat-trend {
  font-size: 13px;
  margin-left: 8px;
  font-weight: 500;
}

.stat-trend.up {
  color: #43a047;
}

.stat-trend.down {
  color: #d32f2f;
}

.chart-card {
  background: #f8fafc;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 18px 24px 8px 24px;
  margin-bottom: 20px;
  border: none;
}

.users-chart-title {
  font-size: 16px;
  font-weight: bold;
  color: #304156;
  margin-bottom: 8px;
}

.users-chart {
  width: 100%;
  height: 220px;
}

.users-filters {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
  align-items: center;
}

.filter-select {
  width: 160px;
}

.filter-search {
  flex: 1;
  min-width: 200px;
}

.search-btn {
  white-space: nowrap;
}

.users-table {
  margin-bottom: 20px;
}

.user-info-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.user-name {
  font-weight: bold;
  color: #222e3c;
  font-size: 16px;
}

.user-email {
  color: #888;
  font-size: 13px;
}

.user-phone {
  color: #bdbdbd;
  font-size: 13px;
}

.users-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}

.pagination {
  text-align: right;
}

/* 用户详情弹窗样式 */
.user-detail {
  padding: 10px 0;
}

.detail-row {
  display: flex;
  margin-bottom: 12px;
}

.detail-label {
  color: #888;
  min-width: 80px;
  font-weight: 500;
}

.detail-value {
  flex: 1;
}

/* 禁用确认弹窗样式 */
.user-to-disable {
  font-weight: bold;
  color: #f56c6c;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .users-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .users-stats {
    gap: 10px;
  }

  .filter-select,
  .search-btn {
    width: 100%;
  }
}

/* 分页 */
.demo-pagination-block + .demo-pagination-block {
  margin-top: 10px;
}
.demo-pagination-block .demonstration {
  margin-bottom: 16px;
}
</style>
