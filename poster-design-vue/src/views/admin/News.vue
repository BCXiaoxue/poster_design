<template>
  <div class="news-mgmt-page">
    <el-card class="main-card">
      <!-- 头部区域 -->
      <div class="news-header">
        <div>
          <div class="news-title">新闻管理</div>
          <div class="news-desc">
            管理系统新闻内容，可添加、编辑和发布新闻资讯
          </div>
        </div>
        <el-button type="primary" @click="openCreateDialog" class="create-btn">+ 新建新闻</el-button>
      </div>

      <!-- 统计卡片 -->
      <el-row :gutter="24" class="news-stats">
        <el-col :xs="24" :sm="8" :md="8" class="stat-col">
          <el-card class="news-stat-card stat-blue">
            <div class="stat-label">新闻总数</div>
            <div class="stat-value">{{ total }}</div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" class="stat-col">
          <el-card class="news-stat-card stat-green">
            <div class="stat-label">已发布</div>
            <div class="stat-value">{{ publishedCount }}</div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" class="stat-col">
          <el-card class="news-stat-card stat-orange">
            <div class="stat-label">未发布</div>
            <div class="stat-value">{{ unpublishedCount }}</div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 筛选区域（新增状态筛选） -->
      <div class="news-filters">
        <el-input v-model="getList.title" placeholder="搜索新闻标题/内容" class="filter-search" />
        <!-- 新增状态筛选下拉框 -->
        <el-select v-model="selectedStatus" placeholder="选择状态" class="filter-select" @change="handleSearch">
          <el-option label="全部" value="全部" />
          <el-option label="已发布" value="已发布" />
          <el-option label="未发布" value="未发布" />
        </el-select>
        <el-button type="primary" class="search-btn" @click="handleSearch">搜索</el-button>
      </div>

      <!-- 新闻表格 -->
      <el-table :data="newsList" border stripe class="news-table">
        <el-table-column prop="title" label="新闻标题" min-width="320" />
        <el-table-column prop="uploadTime" label="发布时间" width="160" />
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
      <div class="news-pagination">
        <el-pagination layout="total, sizes, prev, pager, next" :total="total" :page-sizes="[5, 10, 20, 50]"
          :page-size="getList.pageSize" :current-page="getList.pageNo" @current-change="handlePageChange"
          @size-change="handleSizeChange" class="pagination-component" />
      </div>
    </el-card>

    <!-- 查看新闻弹窗 -->
    <el-dialog title="新闻详情" v-model="viewVisible" :width="dialogWidth">
      <div class="view-content">
        <div class="view-item">
          <span class="view-label">标题：</span>
          <span class="view-value">{{ viewData.title }}</span>
        </div>

        <!-- 图片轮播展示区域 -->
        <div class="view-item" v-if="viewData.images && viewData.images.length > 0">
          <span class="view-label">图片：</span>
          <el-carousel height="300px" indicator-position="outside" arrow="always">
            <el-carousel-item v-for="(item, index) in viewData.images" :key="index">
              <img v-lazy :src="item" alt="新闻图片" class="carousel-image" />
            </el-carousel-item>
          </el-carousel>
        </div>

        <div class="view-item">
          <span class="view-label">发布日期：</span>
          <div class="view-value">{{ viewData.uploadTime }}</div>
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

    <!-- 新建/编辑新闻弹窗 -->
    <el-dialog :title="isEdit ? '编辑新闻' : '新建新闻'" v-model="dialogVisible" :width="dialogWidth">
      <el-form ref="formRef" :model="form" label-width="80px" class="news-form" :rules="formRules">
        <el-form-item label="标题" prop="title" class="form-item">
          <el-input v-model="form.title" placeholder="请输入新闻标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content" class="form-item">
          <el-input v-model="form.content" type="textarea" rows="6" placeholder="请输入新闻内容" />
        </el-form-item>
        <el-form-item label="发布日期" prop="uploadTime" class="form-item">
          <el-date-picker v-model="form.uploadTime" type="date" placeholder="选择发布日期" value-format="YYYY-MM-DD"
            class="date-picker" />
        </el-form-item>

        <!-- 图片上传区域 -->
        <el-form-item label="图片" prop="images">
          <div class="upload-container">
            <!-- 图片预览区域 -->
            <div class="preview-container">
              <div v-for="(item, index) in form.images" :key="index" class="image-preview">
                <img v-lazy :src="item" alt="图片预览" class="preview-image" />
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

        <el-form-item label="竞赛" prop="competitionId" class="form-item">
          <el-select v-model="form.competitionId" placeholder="请选择竞赛" class="select-component"
            @click="getCompetitionList">
            <el-option v-for="item in competitionOptions" :key="item.id" :label="item.competitionName"
              :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false" class="dialog-btn">取消</el-button>
        <el-button type="primary" @click="submitNews" class="dialog-btn primary-btn">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getNews,
  updateNews,
  addNews,
  deleteNews,
  getNewsById,
  getCompetitions,
  uploadFig,
  getNewsCount
} from "@/apis/admin/admin-news";

// 获取列表参数
const getList = ref({
  pageNo: 1,
  pageSize: 10,
  title: "",
});

// 新增：状态筛选参数
const selectedStatus = ref('全部'); // 默认为全部

// 新闻数据
const newsList = ref([]);
const total = ref(0); // 总数据条数（筛选后）
const competitionOptions = ref([]); // 竞赛下拉选项

// 弹窗控制
const dialogVisible = ref(false);
const viewVisible = ref(false);
const isEdit = ref(false);
const formRef = ref(null);
const form = ref({
  id: "",
  title: "",
  content: "",
  uploadTime: "",
  competitionId: null,
  images: [], // 图片数组
});
const viewData = ref({});

// 表单验证规则
const formRules = {
  title: [{ required: true, message: "请输入新闻标题", trigger: "blur" }],
  content: [{ required: true, message: "请输入新闻内容", trigger: "blur" }],
  uploadTime: [
    { required: true, message: "请选择发布日期", trigger: "change" },
  ],
  competitionId: [{ required: true, message: "请选择竞赛", trigger: "change" }],
};

// 响应式弹窗宽度
const dialogWidth = computed(() => {
  return window.innerWidth < 768 ? "90%" : "700px";
});

// 判断新闻状态函数（前端逻辑）
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

const publishedCount = ref(0);
const unpublishedCount = ref(0);

// 获取新闻列表（修复状态筛选逻辑）
const getNewsList = async () => {
  try {
    // 构建查询参数，包含状态筛选
    const queryParams = {
      ...getList.value,
      status: selectedStatus.value === '全部' ? null : selectedStatus.value
    };
    
    const res = await getNews(queryParams);
    if (res.code) {
      newsList.value = res.data.records || [];
      total.value = res.data.total || 0;
    } else {
      ElMessage.error(res.msg || "获取新闻列表失败");
    }
    
    // 获取统计数据
    const resNews = await getNewsCount();
    if (resNews.code) {
      publishedCount.value = resNews.data.publishedCount || 0;
      unpublishedCount.value = resNews.data.unpublishedCount || 0;
      // 只有在没有状态筛选时才更新总数为已发布+未发布的数量
      if (selectedStatus.value === '全部') {
        total.value = publishedCount.value + unpublishedCount.value;
      }
    }
  } catch (err) {
    ElMessage.error("加载新闻列表失败，请重试");
    newsList.value = [];
    total.value = 0;
    publishedCount.value = 0;
    unpublishedCount.value = 0;
  }
};

// 获取竞赛列表
const getCompetitionList = async () => {
  try {
    const res = await getCompetitions({ competitionStatus: 3 });
    if (res.code) {
      competitionOptions.value = res.data.map((item) => ({
        id: item.id,
        competitionName: item.competitionName,
      }));
    }
  } catch (err) {
    ElMessage.error("加载竞赛列表失败");
  }
};

// 搜索处理
const handleSearch = () => {
  getList.value.pageNo = 1; // 重置页码
  getNewsList();
};

// 分页处理 - 页码变化（修复原代码笔误）
const handlePageChange = (page) => {
  getList.value.pageNo = page;
  getNewsList(); // 原代码笔误"getNews极()"修正
};

// 分页处理 - 页大小变化
const handleSizeChange = (size) => {
  getList.value.pageSize = size;
  getList.value.pageNo = 1; // 重置到第一页
  getNewsList();
};

// 打开新建新闻弹窗
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

// 打开编辑新闻弹窗
const openEditDialog = async (row) => {
  try {
    const res = await getNewsById(row.id);
    if (res.code) {
      isEdit.value = true;

      // 处理图片数据
      let images = res.data.images || [];
      if (typeof images === "string") {
        try {
          images = JSON.parse(images);
        } catch (e) {
          images = [];
        }
      } else if (!Array.isArray(images)) {
        images = [];
      }

      form.value = {
        ...res.data,
        images: images,
        competitionId: res.data.competitionId ? Number(res.data.competitionId) : null,
      };
      dialogVisible.value = true;
    } else {
      ElMessage.error(res.msg || "获取新闻详情失败");
    }
  } catch (err) {
    ElMessage.error("获取新闻详情失败");
  }
};

// 打开查看新闻弹窗
const openViewDialog = async (row) => {
  try {
    const res = await getNewsById(row.id);
    if (res.code) {
      // 处理图片数据
      let images = res.data.images || [];
      if (typeof images === "string") {
        try {
          images = JSON.parse(images);
        } catch (e) {
          images = [];
        }
      } else if (!Array.isArray(images)) {
        images = [];
      }

      // 查找竞赛名称
      const competition = competitionOptions.value.find(
        (item) => item.id === res.data.competitionId
      );

      viewData.value = {
        ...res.data,
        images: images,
        competitionName: competition ? competition.competitionName : "无",
      };
      viewVisible.value = true;
    } else {
      ElMessage.error(res.msg || "获取新闻详情失败");
    }
  } catch (err) {
    ElMessage.error("获取新闻详情失败");
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

    const formData = new FormData();
    formData.append("file", file.raw);

    const res = await uploadFig(formData);
    if (res.code && res.data) {
      form.value.images.push(res.data);
      ElMessage.success("图片上传成功");
    } else {
      ElMessage.error(res.msg || "图片上传失败");
    }
  } catch (error) {
    ElMessage.error("图片上传出错：" + error.message);
  }
};

// 提交新闻
const submitNews = async () => {
  // 表单验证
  const valid = await formRef.value.validate();
  if (!valid) return;

  try {
    const submitData = {
      ...form.value,
      images: Array.isArray(form.value.images) ? form.value.images : [],
    };

    let res;
    if (isEdit.value) {
      res = await updateNews(submitData);
    } else {
      res = await addNews(submitData);
    }

    if (res.code) {
      ElMessage.success(isEdit.value ? "新闻编辑成功" : "新闻创建成功");
      dialogVisible.value = false;
      getNewsList();
    } else {
      ElMessage.error(res.msg || "操作失败");
    }
  } catch (err) {
    ElMessage.error("操作失败，请重试");
  }
};

// 删除新闻
const handleDelete = async (id) => {
  ElMessageBox.confirm("确定要删除这条新闻吗？", "确认删除", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      try {
        const res = await deleteNews(id);
        if (res.code) {
          ElMessage.success("新闻删除成功");
          if (newsList.value.length === 1 && getList.value.pageNo > 1) {
            getList.value.pageNo -= 1;
          }
          getNewsList();
        } else {
          ElMessage.error(res.msg || "删除失败");
        }
      } catch (err) {
        ElMessage.error("删除失败，请重试");
      }
    })
    .catch(() => {
      // 取消删除
    });
};

// 组件挂载时加载数据
onMounted(() => {
  getNewsList();
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

/* 原有样式保持不变 */
.news-mgmt-page {
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

.news-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.news-title {
  font-size: 22px;
  font-weight: bold;
  color: #222e3c;
  margin-bottom: 6px;
}

.news-desc {
  color: #888;
  font-size: 14px;
}

.create-btn {
  padding: 8px 20px;
}

.news-stats {
  margin-bottom: 30px;
}

.stat-col {
  margin-bottom: 16px;
}

.news-stat-card {
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 10px 20px;
  box-sizing: border-box;
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

.news-filters {
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

.news-table {
  margin-bottom: 24px;
  width: 100%;
}

.operation-group {
  display: flex;
  gap: 8px;
}

.news-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.pagination-component {
  text-align: right;
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

.news-form {
  margin-top: 10px;
}

.form-item {
  margin-bottom: 20px;
}

.date-picker,
.select-component {
  width: 100%;
}

.dialog-btn {
  margin-left: 10px;
}

.primary-btn {
  margin-left: 10px;
}

@media (max-width: 768px) {
  .news-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .news-filters {
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

  .image-preview {
    width: 80px;
    height: 80px;
  }
}
</style>