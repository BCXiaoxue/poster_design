<template>
  <div class="honor-container">
    <!-- 标题栏 -->
    <div class="honor-header">
      <el-icon class="honor-icon">
        <medal />
      </el-icon>
      <h3 class="honor-title">我的荣誉</h3>
    </div>
    <p class="honor-slogan">每一份荣誉，都是成长的印记</p>

    <!-- 加载状态 -->
    <div class="loading-state" v-if="loading">
      <el-skeleton :count="3" height="200px" animated />
    </div>

    <!-- 荣誉横向列表（带预览和下载） -->
    <div class="honor-list" v-else-if="tableData.length > 0">
      <el-card
        class="honor-card"
        v-for="(item, index) in tableData"
        :key="index"
        shadow="hover"
      >
        <!-- 图片区域（点击预览） -->
        <el-image
          :src="item"
          class="honor-image"
          :preview-src-list="[item]"
          :preview-teleported="true"
          fit="cover"
          :alt="`荣誉证书${index + 1}`"
        >
          <!-- 图片加载失败时使用element-empty显示 -->
          <template #error>
            <el-empty description="图片加载失败" image-size="60" />
          </template>
        </el-image>

        <!-- 信息区域（底部） -->
        <div class="card-info">
          <h4 class="honor-name">{{ `荣誉证书${index + 1}` }}</h4>
          <div class="info-actions">
            <el-button
              size="mini"
              type="primary"
              @click="handleDownload(item, index + 1)"
            >
              <el-icon>
                <download />
              </el-icon>
              下载证书
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 无数据提示（保持element-empty） -->
    <div class="no-data" v-else>
      <el-empty description="暂无荣誉记录，继续加油！" image-size="120">
        <template #bottom>
          <el-button type="primary" size="small">获取更多荣誉</el-button>
        </template>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getAward } from "@/apis/person";
import { ElMessage, ElLoading } from "element-plus";

// 荣誉数据（存储后端返回的URL数组）
const tableData = ref([]);
// 加载状态
const loading = ref(true);

// 获取荣誉数据
const search = async () => {
  loading.value = true;
  try {
    const res = await getAward();
    if (res.code === 1) {
      // 后端code=1表示成功
      tableData.value = res.data || [];
    } else {
      ElMessage.error(res.msg || "获取荣誉失败");
    }
  } catch (error) {
    ElMessage.error("请求出错，请重试");
    console.error("获取荣誉失败:", error);
  } finally {
    loading.value = false;
  }
};

// 处理证书下载
const handleDownload = (url, index) => {
  // 显示加载状态
  const loadingInstance = ElLoading.service({
    lock: false,
    text: "正在准备下载...",
    background: "rgba(255, 255, 255, 0.7)",
  });

  try {
    fetch(url)
      .then((res) => res.blob())
      .then((blob) => {
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = url.split("/").pop();
        link.click();
      });
  } catch (error) {
    console.error("下载失败:", error);
    ElMessage.error("下载失败，网络异常或接口错误");
  } finally {
    // 关闭加载状态
    loadingInstance.close();
  }
};

// 初始化加载
onMounted(() => {
  search();
});
</script>

<style scoped>
/* 容器样式：全宽占据 + 保留上下间距 */
.honor-container {
  padding: 24px 8px;
  background: linear-gradient(135deg, #f8fafc 0%, #ffffff 100%);
  border-radius: 16px;
  margin: 16px 0;
  width: 100%;
}

/* 标题栏保持原有样式 */
.honor-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.honor-icon {
  font-size: 28px;
  color: #ff7d00;
}

.honor-title {
  font-size: 22px;
  font-weight: 600;
  color: #1d2129;
  margin: 0;
}

.honor-slogan {
  font-size: 14px;
  color: #86909c;
  margin: 0 0 24px 0;
  line-height: 1.6;
}

/* 加载状态保持原有样式 */
.loading-state {
  padding: 20px 0;
  display: flex;
  gap: 16px;
  overflow-x: auto;
}

/* 荣誉列表：核心调整！确保一行3张 */
.honor-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  width: 100%;
  /* 缩小间距 */
}

/* 荣誉卡片：精确宽度计算 + 尺寸优化 */
.honor-card {
  flex: 0 0 calc((100% - 32px) / 3);
  /* 3张公式：(100% - 2×16px)/3 */
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
}

/* 卡片Hover效果保持原有样式 */
.honor-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

/* 图片样式：降低高度，适配布局 */
.honor-image {
  width: 100%;
  height: 180px;
  /* 压缩高度 */
  object-fit: cover;
}

/* 信息区域保持原有样式 */
.card-info {
  padding: 16px;
  background-color: #ffffff;
}

.honor-name {
  font-size: 14px;
  font-weight: 500;
  color: #1d2129;
  margin: 0 0 8px 0;
}

.info-actions {
  display: flex;
  justify-content: flex-end;
}

/* 无数据状态保持原有样式 */
.no-data {
  padding: 60px 0;
}

/* 响应式调整（原有逻辑不变） */
@media (max-width: 992px) {
  .honor-card {
    flex: 0 0 calc((100% - 20px) / 2);
    /* 平板一行2张 */
  }
}

@media (max-width: 768px) {
  .honor-card {
    flex: 0 0 100%;
    /* 手机一行1张 */
  }
}

/* 移动端适配 */
@media (max-width: 767px) {
  .honor-container {
    padding: 8px 2px;
    margin: 8px 0;
    border-radius: 12px;
  }

  .honor-header {
    gap: 8px;
    margin-bottom: 6px;
  }

  .honor-icon {
    font-size: 24px;
  }

  .honor-title {
    font-size: 18px;
  }

  .honor-slogan {
    font-size: 13px;
    margin-bottom: 16px;
  }

  .loading-state {
    padding: 16px 0;
    gap: 12px;
  }

  .honor-list {
    gap: 8px;
  }

  .honor-card {
    flex: 0 0 100%;
    border-radius: 8px;
  }

  .honor-image {
    height: 120px;
    border-radius: 8px 8px 0 0;
  }

  .image-error {
    height: 160px;
    font-size: 12px;
  }

  .card-info {
    padding: 8px 4px;
  }
  .info-actions {
    display: flex;
    justify-content: flex-end;
    padding: 0 2px 4px 2px;
  }
  .honor-title {
    font-size: 16px;
  }

  .honor-name {
    font-size: 13px;
    margin-bottom: 6px;
  }

  .info-actions .el-button {
    font-size: 12px;
    padding: 6px 12px;
  }

  .no-data {
    padding: 40px 0;
  }

  .no-data :deep(.el-empty__description) {
    font-size: 13px;
  }

  .no-data :deep(.el-button) {
    font-size: 13px;
  }
}

/* 平板端适配 */
@media (min-width: 768px) and (max-width: 1023px) {
  .honor-container {
    padding: 20px 12px;
  }

  .honor-title {
    font-size: 20px;
  }

  .honor-slogan {
    font-size: 13px;
  }

  .honor-list {
    gap: 10px;
  }

  .honor-card {
    flex: 0 0 calc((100% - 20px) / 2);
  }

  .honor-image {
    height: 170px;
  }

  .image-error {
    height: 170px;
  }
}
</style>
