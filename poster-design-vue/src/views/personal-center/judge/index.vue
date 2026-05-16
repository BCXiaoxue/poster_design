<template>
  <div class="judge-management-container">
    <div class="content-card">
      <div class="page-header">
        <h2 class="page-title">我的评审</h2>
        <p class="page-subtitle">管理和查看你参与评审的所有竞赛</p>
      </div>
      <el-divider class="header-divider" />
      <div class="table-section">
        <el-table :data="tableData" border class="judge-table" :header-cell-style="{ background: '#f8f9fa', color: '#333', fontWeight: '600' }" fit highlight-current-row style="width: 100%;">
          <el-table-column prop="competitionName" label="我评审的竞赛" width="320" show-overflow-tooltip />
          <el-table-column label="时间" width="220">
            <template #default="scope">
              <el-tag>{{ scope.row.registrationBegin }}</el-tag>
              <el-tag>{{ scope.row.activityEnd }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120">
            <template #default="scope">
              <el-button
                size="small"
                @click="handleEdit(scope.row)"
                :disabled="isOverdue(scope.row.activityEnd)"
                class="action-btn"
              >
                去评分
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pagination-section">
        <el-pagination
          v-model:current-page="formData.pageNo"
          :page-size="formData.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5, 10, 20, 50]"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          class="pagination"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getJudgeCompetition } from "@/apis/person";
import { ElMessageBox, ElMessage } from "element-plus";
import { useRouter } from "vue-router";

const router = useRouter();
const tableData = ref([]);
const total = ref(0); // 总记录数

const formData = ref({
  id: "",
  competitionName: "",
  competition_status: "",
  pageNo: 1,
  pageSize: 10,
});

// 获取竞赛列表
const search = async () => {
  const res = await getJudgeCompetition(formData.value);
  if (res.code) {
    tableData.value = res.data.records;
    total.value = res.data.total; // 更新总记录数
  } else {
    ElMessageBox.error(res.message || "获取竞赛列表失败");
  }
};

// 分页大小改变
const handleSizeChange = (val) => {
  formData.value.pageSize = val;
  formData.value.pageNo = 1; // 重置到第一页
  search();
};

// 当前页码改变
const handleCurrentChange = (val) => {
  formData.value.pageNo = val;
  search();
};

// 判断是否超过活动结束时间
const isOverdue = (activityEnd) => {
  if (!activityEnd) return false;
  const endTime = new Date(activityEnd).getTime();
  const currentTime = new Date().getTime();
  return currentTime > endTime;
};

// 去评分
const handleEdit = (row) => {
  if (isOverdue(row.activityEnd)) {
    ElMessage.warning("当前活动已结束，无法进行评分操作");
    return;
  }
  ElMessage.success("跳转成功");
  router.push(`/competitionjudge/${row.id}`);
};

onMounted(() => {
  search();
});
</script>

<style scoped>
.judge-management-container {
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
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
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

.table-section {
  margin-bottom: 16px;
}

.judge-table {
  border-radius: 8px;
  overflow: hidden;
  font-size: 15px;
}

.judge-table :deep(.el-table__header) {
  background: #f8f9fa;
  color: #333;
  font-weight: 600;
}

.judge-table :deep(.el-table__row) {
  transition: background-color 0.2s;
}

.judge-table :deep(.el-table__row:hover) {
  background-color: #f5f7fa;
}

.judge-table :deep(.el-table__cell) {
  text-align: center;
  vertical-align: middle;
  padding: 12px 0;
}

.judge-table :deep(.el-table__cell .el-button) {
  border-radius: 4px;
  font-weight: 500;
  font-size: 14px;
  padding: 4px 14px;
}

.judge-table :deep(.el-table__cell .el-tag + .el-tag) {
  margin-left: 8px;
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

/* 移动端适配 */
@media (max-width: 767px) {
  .judge-management-container {
    padding: 0 8px;
  }
  
  .content-card {
    padding: 16px 12px;
    margin-top: 8px;
  }
  
  .page-header {
    padding-top: 16px;
    margin-bottom: 8px;
    padding-left: 0;
  }
  
  .page-title {
    font-size: 18px;
    margin-bottom: 6px;
  }
  
  .page-subtitle {
    font-size: 13px;
  }
  
  .header-divider {
    margin: 0 0 12px 0;
  }
  
  .table-section {
    margin-bottom: 20px;
    overflow-x: auto;
  }
  
  .judge-table {
    min-width: 600px;
    font-size: 12px;
  }
  
  .judge-table :deep(.el-table__header) {
    font-size: 12px;
  }
  
  .judge-table :deep(.el-table__cell) {
    padding: 8px 4px;
  }
  
  .judge-table :deep(.el-table__cell .el-button) {
    font-size: 11px;
    padding: 4px 8px;
  }
  
  .judge-table :deep(.el-table__cell .el-tag) {
    font-size: 10px;
    padding: 2px 6px;
    margin: 2px;
  }
  
  .judge-table :deep(.el-table__cell .el-tag + .el-tag) {
    margin-left: 4px;
  }
  
  .pagination-section {
    margin-top: 16px;
    justify-content: center;
  }
  
  .pagination {
    font-size: 12px;
  }
  
  .pagination :deep(.el-pagination__total) {
    font-size: 12px;
  }
  
  .pagination :deep(.el-pagination__sizes) {
    font-size: 12px;
  }
  
  .pagination :deep(.el-pagination__jump) {
    font-size: 12px;
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
  
  .judge-table {
    font-size: 13px;
  }
}
</style>
