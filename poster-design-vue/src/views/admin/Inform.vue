<template>
  <div class="inform-mgmt-page">
    <el-card class="main-card">
      <!-- 头部区域 -->
      <div class="inform-header">
        <div>
          <div class="inform-title">通告管理</div>
          <div class="inform-desc">
            管理系统通告内容，可添加、编辑和发布通告资讯
          </div>
        </div>
        <el-button type="primary" @click="openCreateDialog" class="create-btn">+ 新建通告</el-button>
      </div>

      <!-- 统计卡片 -->
      <el-row :gutter="24" class="inform-stats">
        <el-col :xs="24" :sm="8" :md="8" class="stat-col">
          <el-card class="inform-stat-card stat-blue">
            <div class="stat-label">通告总数</div>
            <div class="stat-value">{{ total }}</div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" class="stat-col">
          <el-card class="inform-stat-card stat-green">
            <div class="stat-label">已发布</div>
            <div class="stat-value">{{ publishedCount }}</div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" class="stat-col">
          <el-card class="inform-stat-card stat-orange">
            <div class="stat-label">未发布</div>
            <div class="stat-value">{{ unpublishedCount }}</div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 筛选区域 -->
      <div class="inform-filters">
        <el-input v-model="getList.title" placeholder="搜索通告标题" class="filter-search" />
        <!-- 新增状态筛选下拉框 -->
        <el-select v-model="selectedStatus" placeholder="选择状态" class="filter-select" @change="handleSearch">
          <el-option label="全部" value="全部" />
          <el-option label="已发布" value="已发布" />
          <el-option label="未发布" value="未发布" />
        </el-select>
        <el-button type="primary" class="search-btn" @click="handleSearch">搜索</el-button>
      </div>

      <!-- 通告表格 -->
      <el-table :data="informList" border stripe class="inform-table">
        <el-table-column prop="title" label="通告标题" min-width="320" />
        <el-table-column prop="uploadTime" label="预发布时间" width="160" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getPublishedStatus(scope.row.uploadTime) === '已发布'
              ? 'success'
              : 'warning'
              ">
              {{ getPublishedStatus(scope.row.uploadTime) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260">
          <template #default="scope">
            <div class="operation-group">
              <el-button type="primary" size="small" @click="openViewDialog(scope.row)">查看</el-button>
              <el-button type="success" size="small" @click="openEditDialog(scope.row)">编辑</el-button>
              <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="inform-pagination">
        <el-pagination layout="total, sizes, prev, pager, next" :total="total" :page-sizes="[5, 10, 20, 50]"
          :page-size="getList.pageSize" :current-page="getList.pageNo" @current-change="handlePageChange"
          @size-change="handleSizeChange" />
      </div>
    </el-card>

    <!-- 查看通告弹窗 -->
    <el-dialog title="通告详情" v-model="viewVisible" :width="dialogWidth">
      <div class="view-content">
        <div class="view-item">
          <span class="view-label">标题：</span>
          <span class="view-value">{{ viewData.title }}</span>
        </div>
        <!-- 图片轮播 -->
        <div class="view-item" v-if="viewData.images && viewData.images.length > 0">
          <span class="view-label">图片：</span>
          <el-carousel height="300px" indicator-position="outside" arrow="always">
            <el-carousel-item v-for="(item, index) in viewData.images" :key="index">
              <img v-lazy :src="item" alt="通告图片" class="carousel-image" />
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="view-item">
          <span class="view-label">发布日期：</span>
          <span class="view-value">{{ viewData.uploadTime }}</span>
        </div>
        <div class="view-item">
          <span class="view-label">状态：</span>
          <el-tag :type="getPublishedStatus(viewData.uploadTime) === '已发布'
            ? 'success'
            : 'warning'
            ">
            {{ getPublishedStatus(viewData.uploadTime) }}
          </el-tag>
        </div>
        <div class="view-item view-content-item">
          <span class="view-label">内容：</span>
          <div class="view-content-text">{{ viewData.content }}</div>
        </div>
      </div>
    </el-dialog>

    <!-- 新建/编辑通告弹窗 -->
    <el-dialog :title="isEdit ? '编辑通告' : '新建通告'" v-model="dialogVisible" :width="dialogWidth">
      <el-form :model="form" label-width="80px" class="inform-form" ref="formRef">
        <el-form-item label="标题" prop="title" :rules="[{ required: true, message: '请输入标题', trigger: 'blur' }]">
          <el-input v-model="form.title" placeholder="请输入通告标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content" :rules="[{ required: true, message: '请输入内容', trigger: 'blur' }]">
          <el-input v-model="form.content" type="textarea" rows="6" placeholder="请输入通告内容" />
        </el-form-item>
        <el-form-item label="发布日期" prop="uploadTime" :rules="[
          { required: true, message: '请选择日期', trigger: 'change' },
        ]">
          <el-date-picker v-model="form.uploadTime" type="date" placeholder="选择发布日期" value-format="YYYY-MM-DD"
            class="date-picker" />
        </el-form-item>
        <!-- 图片上传区域 -->
        <el-form-item label="图片" prop="images">
          <div class="upload-container">
            <!-- 图片预览区域 -->
            <div class="preview-container">
              <div v-for="(item, index) in form.images" :key="index" class="image-preview">
                <img v-lazy :src="item" alt="图片预览" class="preview-image" width="50%" height="50%" />
                <div class="image-actions">
                  <el-button type="danger" size="mini" @click="removeImage(index)">
                    <i class="el-icon-delete"></i>
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 上传按钮 -->
            <el-upload class="upload-btn" action="#" :auto-upload="false" :on-change="handleImageChange"
              :show-file-list="false" :multiple="true" :limit="5" :before-upload="beforeImageUpload" accept="image/*">
              <el-button size="small" type="primary">
                <i class="el-icon-upload"></i> 上传图片
              </el-button>
              <div class="el-upload__tip">
                支持 JPG、PNG 格式，单张不超过 2MB，最多上传 5 张
              </div>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="竞赛" prop="competitionId">
          <el-select v-model="form.competitionId" placeholder="请选择竞赛" clearable @click="getCompetitionList">
            <el-option v-for="item in competitionList" :key="item.id" :value="item.id" :label="item.competitionName" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitInform">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getWithLike,
  getInformById,
  getInform,
  deleteInform,
  getCompetitions,
  updateInform,
  addInform,
  uploadFig,
  getInformCount,
} from "@/apis/admin/admin-inform";

// 获取列表参数
const getList = ref({
  id: null,
  title: null,
  pageNo: 1,
  pageSize: 10,
});
// 新增状态筛选参数
const selectedStatus = ref('全部');
const competitionList = ref([]); // 存储竞赛列表（包含id和competitionName）
const informList = ref([]); // 通告数据
const total = ref(0); // 总数据条数

// 弹窗控制
const dialogVisible = ref(false);
const viewVisible = ref(false);
const isEdit = ref(false);
const formRef = ref(null); // 表单引用
const form = ref({
  id: "",
  title: "",
  content: "",
  uploadTime: "",
  competitionId: null,
  images: [],
});
const viewData = ref({
  images: [],
});

// 响应式弹窗宽度
const dialogWidth = computed(() => {
  return window.innerWidth < 768 ? "90%" : "700px";
});

// 竞赛列表加载方法（确保优先加载）
const getCompetitionList = async () => {
  try {
    const res1 = await getCompetitions(getC.value);
    if (res1.code) {
      competitionList.value = res1.data || [];
    }
  } catch (err) {
    ElMessage.error("加载竞赛列表失败，请重试");
    competitionList.value = [];
  }
};

// 获取通告列表（修复状态筛选逻辑）
const search = async () => {
  try {
    // 构建查询参数，包含状态筛选
    const queryParams = {
      ...getList.value,
      status: selectedStatus.value === '全部' ? null : selectedStatus.value
    };
    
    const res = await getWithLike(queryParams);
    if (res.code) {
      informList.value = res.data.records || [];
      total.value = res.data.total || 0;
    }
    
    // 获取统计数据
    const res2 = await getInformCount();
    if (res2.code) {
      publishedCount.value = res2.data.publishedCount || 0;
      unpublishedCount.value = res2.data.unpublishedCount || 0;
      // 只有在没有状态筛选时才更新总数为已发布+未发布的数量
      if (selectedStatus.value === '全部') {
        total.value = publishedCount.value + unpublishedCount.value;
      }
    }
  } catch (err) {
    ElMessage.error("加载通告列表失败，请重试");
    informList.value = [];
    total.value = 0;
    publishedCount.value = 0;
    unpublishedCount.value = 0;
  }
};

// 组件挂载时先加载竞赛列表，再加载通告
onMounted(() => {
  search();
});

// 判断通告状态函数
const getPublishedStatus = (uploadTime) => {
  if (!uploadTime) return "未发布";

  const today = new Date();
  const uploadDate = new Date(uploadTime);

  // 设置时间为同一天的00:00:00进行比较
  const todayStart = new Date(
    today.getFullYear(),
    today.getMonth(),
    today.getDate()
  );
  const uploadDateStart = new Date(
    uploadDate.getFullYear(),
    uploadDate.getMonth(),
    uploadDate.getDate()
  );

  return uploadDateStart <= todayStart ? "已发布" : "未发布";
};

// 统计数据
const publishedCount = ref(0);
const unpublishedCount = ref(0);

// 分页处理
const handlePageChange = (page) => {
  getList.value.pageNo = page;
  search();
};

// 每页条数变更处理
const handleSizeChange = (size) => {
  getList.value.pageSize = size;
  getList.value.pageNo = 1; // 重置到第一页
  search();
};

// 搜索处理函数
const handleSearch = () => {
  getList.value.pageNo = 1; // 搜索时重置到第一页
  search();
};

// 打开新建通告弹窗
const openCreateDialog = () => {
  isEdit.value = false;
  form.value = {
    id: "",
    title: "",
    content: "",
    uploadTime: "",
    competitionId: null,
    images: [],
  };
  dialogVisible.value = true;
};

// 打开编辑通告弹窗
const openEditDialog = async (row) => {
  isEdit.value = true;
  try {
    const res = await getInformById(row.id);
    if (!res.code) throw new Error("获取通告详情失败");

    if (competitionList.value.length === 0) {
      await getCompetitionList();
    }

    form.value = {
      ...res.data,
      competitionId: Number(res.data.competitionId),
      images: res.data.images ? [...res.data.images] : [],
    };

    dialogVisible.value = true;
  } catch (err) {
    ElMessage.error(err.message || "打开编辑弹窗失败");
  }
};

// 打开查看通告弹窗
const openViewDialog = async (row) => {
  viewVisible.value = true;
  try {
    const res = await getInformById(row.id);
    if (res.code) {
      viewData.value = res.data;
      viewData.value.images = res.data.images || [];
    }
  } catch (err) {
    ElMessage.error("获取详情失败");
  }
};

// 删除图片
const removeImage = (index) => {
  form.value.images.splice(index, 1);
  ElMessage.success("图片已移除");
};

// 图片上传前的验证
const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith("image/");
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error("只能上传图片文件!");
    return false;
  }
  if (!isLt2M) {
    ElMessage.error("图片大小不能超过 2MB!");
    return false;
  }
  return true;
};

// 处理图片上传
const handleImageChange = async (file) => {
  try {
    // 检查图片数量限制
    if (form.value.images.length >= 5) {
      ElMessage.warning("最多只能上传5张图片");
      return;
    }

    // 创建FormData对象
    const formData = new FormData();
    formData.append("file", file.raw);

    // 调用上传接口
    const res = await uploadFig(formData);

    if (res.code && res.data) {
      // 将上传返回的URL添加到图片列表
      form.value.images.push(res.data);
      ElMessage.success("图片上传成功");
    } else {
      ElMessage.error("图片上传失败");
    }
  } catch (error) {
    ElMessage.error("图片上传出错：" + error.message);
  }
};

// 提交通告
const submitInform = async () => {
  try {
    // 验证表单
    await formRef.value.validate();

    if (!form.value.title || !form.value.content || !form.value.uploadTime) {
      ElMessage.warning("请填写必填字段");
      return;
    }

    // 准备提交数据
    const submitData = {
      ...form.value,
      competitionId: form.value.competitionId || null,
    };

    let res;
    if (isEdit.value) {
      res = await updateInform(submitData);
      if (res.code) ElMessage.success("通告编辑成功");
    } else {
      res = await addInform(submitData);
      if (res.code) ElMessage.success("通告创建成功");
    }

    // 关闭弹窗并刷新列表
    dialogVisible.value = false;
    search();
  } catch (err) {
    // 表单验证失败会抛出错误，这里不需要额外处理
    if (!err.message.includes("validation")) {
      ElMessage.error("操作失败，请重试");
    }
  }
};

// 删除通告
const handleDelete = async (id) => {
  ElMessageBox.confirm("确定要删除这条通告吗？", "确认删除", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      const res = await deleteInform(id);
      if (res.code) {
        ElMessage.success("删除成功");
        if (informList.value.length === 1 && getList.value.pageNo > 1) {
          getList.value.pageNo -= 1;
        }
        search();
      }
    })
    .catch(() => {
      // 取消删除
    });
};

// 竞赛筛选参数（原代码中遗漏的定义）
const getC = ref({
  competitionName: "",
  competitionStatus: 0,
});
</script>

<style scoped>
/* 图片预览样式 */
.upload-container {
  margin-top: 10px;
}

.preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 15px;
}

.image-preview {
  position: relative;
  width: 100px;
  height: 100px;
  border: 1px dashed #ddd;
  border-radius: 4px;
  overflow: hidden;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 5px;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
}

.carousel-image {
  width: 100%;
  height: 300px;
  object-fit: contain;
}

.inform-mgmt-page {
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

.inform-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.inform-title {
  font-size: 22px;
  font-weight: bold;
  color: #222e3c;
  margin-bottom: 6px;
}

.inform-desc {
  color: #888;
  font-size: 14px;
}

.create-btn {
  padding: 8px 20px;
}

.inform-stats {
  margin-bottom: 30px;
}

.stat-col {
  margin-bottom: 16px;
}

.inform-stat-card {
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 10px 20px;
  box-sizing: border-box;
}

.inform-stat-card:hover {
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

.inform-filters {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
  align-items: center;
}

.filter-select {
  width: 180px;
  flex-shrink: 0;
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

.inform-table {
  margin-bottom: 24px;
  width: 100%;
}

.operation-group {
  display: flex;
  gap: 8px;
}

.status-tag {
  padding: 2px 10px;
}

.inform-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.view-content {
  padding: 10px 0;
}

.view-item {
  margin-bottom: 20px;
  display: flex;
  align-items: flex-start;
}

.view-label {
  color: #888;
  font-weight: 500;
  display: inline-block;
  width: 70px;
  flex-shrink: 0;
  padding-top: 2px;
}

.view-value {
  color: #333;
  flex: 1;
}

.view-content-item {
  margin-top: 24px;
}

.view-content-text {
  color: #333;
  margin-top: 6px;
  line-height: 1.8;
  white-space: pre-line;
  word-break: break-word;
}

.inform-form {
  margin-top: 10px;
}

.date-picker {
  width: 100%;
}

@media (max-width: 768px) {
  .inform-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .inform-filters {
    flex-direction: column;
    align-items: stretch;
    gap: 15px;
  }

  .filter-select,
  .filter-search,
  .search-btn {
    width: 100%;
  }

  .operation-group {
    flex-wrap: wrap;
  }

  /* 响应式调整图片预览 */
  .preview-container {
    justify-content: center;
  }

  .image-preview {
    width: 80px;
    height: 80px;
  }
}
</style>