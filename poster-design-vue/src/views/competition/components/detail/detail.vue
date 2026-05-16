<template>
  <div class="detail-overlay">
    <!-- 关闭按钮 -->
    <div class="close-button" @click="onDetail">
      <el-icon><CloseBold /></el-icon>
    </div>

    <div class="content-container">
      <!-- 作品图片区域 -->
      <div class="image-section">
        <img v-lazy class="photo" :src="detailData.picture" alt="poster" />
        <div class="image-overlay">
          <div class="image-info">
            <span class="image-label">作品展示</span>
          </div>
        </div>
      </div>

      <!-- 信息面板 -->
      <div class="info-panel">
        <!-- 标题区域 -->
        <div class="title-section">
          <h1 class="title">{{ detailData.name }}</h1>
          <div class="title-decoration"></div>
        </div>

        <!-- 作者信息 -->
        <div class="author-section">
          <div class="author-avatar">
            <el-icon><UserFilled /></el-icon>
          </div>
          <div class="author-info">
            <span class="author-label">参赛团队</span>
            <span class="author-name">{{ detailData.teamName }}</span>
          </div>
        </div>

        <!-- 作品描述 -->
        <div class="description-section">
          <h3 class="description-title">作品介绍</h3>
          <p class="description">
            {{ detailData.introduction || "暂无作品介绍" }}
          </p>
        </div>

        <!-- 互动区域 -->
        <div class="interaction-section">
          <!-- 点赞功能 -->
          <div class="like-box" @click="toggleLike">
            <div class="like-button" :class="{ liked: isLiked }">
              <el-icon class="like-icon">
                <component :is="isLiked ? 'StarFilled' : 'Star'" />
              </el-icon>
              <span class="like-text">{{ isLiked ? "已点赞" : "点赞" }}</span>
            </div>
            <span class="like-count">{{ likeCount }}</span>
          </div>
        </div>

        <!-- 作品标签 -->
        <div class="tags-section">
          <div class="tag-item">
            <el-icon><Calendar /></el-icon>
            <span>参赛作品</span>
          </div>
          <div class="tag-item">
            <el-icon><Trophy /></el-icon>
            <span>设计大赛</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { getCompetitionDetailById } from "../../../../apis/competition";
import {
  StarFilled,
  Star,
  CloseBold,
  UserFilled,
  Calendar,
  Trophy,
} from "@element-plus/icons-vue";
import { getLikeByUserId, changeLikes } from "../../../../apis/like";
import { usePersonStore } from "@/stores/person.js";

const props = defineProps({
  id: { type: [String, Number], required: true },
});

const router = useRouter();
const personStore = usePersonStore();

const detailData = ref({});
const isLiked = ref(false);
const likeCount = ref(0);
const isLoading = ref(false);

const getDetailData = async () => {
  try {
    const [res, likeRes] = await Promise.all([
      getCompetitionDetailById(props.id),
      getLikeByUserId(props.id),
    ]);
    detailData.value = res.data;
    likeCount.value = likeRes.data.likeCount || 0;
    isLiked.value = likeRes.data.liked === 0 ? false : true;
  } catch (err) {
    console.error("获取详情失败", err);
  }
};

getDetailData();

const onDetail = () => {
  router.back();
};

const toggleLike = async () => {
  if (isLoading.value) return; // 防抖
  isLoading.value = true;

  const nextLiked = !isLiked.value;
  const nextLikeCount = likeCount.value + (nextLiked ? 1 : -1);
  isLiked.value = nextLiked;
  likeCount.value = nextLikeCount;
  try {
    await changeLikes({
      liked: nextLiked ? 1 : 0,
      likeCount: nextLikeCount,
      workId: props.id,
    });
  } catch (error) {
    console.error("点赞失败", error);
    isLiked.value = !nextLiked;
    likeCount.value = likeCount.value + (nextLiked ? -1 : 1);
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.detail-overlay {
  position: fixed;
  inset: 0;
  z-index: 999;
  backdrop-filter: blur(20px);
  background: linear-gradient(
    135deg,
    rgba(27, 120, 247, 0.1) 0%,
    rgba(255, 255, 255, 0.95) 100%
  );
  padding: 1rem;
  overflow-y: auto;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-button {
  position: absolute;
  top: 1.5rem;
  right: 1.5rem;
  width: 3rem;
  height: 3rem;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.close-button:hover {
  background: rgba(255, 255, 255, 1);
  transform: scale(1.1);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.close-button .el-icon {
  font-size: 1.5rem;
  color: #666;
}

.content-container {
  width: 100%;
  max-width: 1400px;
  background: white;
  border-radius: 1.5rem;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  max-height: 90vh;
}

html.dark .content-container {
  background-color: #444343;
}

.image-section {
  position: relative;
  width: 100%;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  overflow: hidden;
}

html.dark .image-section {
  background: linear-gradient(135deg, #5b5b5d 0%, #505154 100%);
}

.photo {
  width: 100%;
  max-height: 60vh;
  object-fit: contain;
  display: block;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    180deg,
    rgba(27, 120, 247, 0.1) 0%,
    transparent 50%,
    rgba(0, 0, 0, 0.05) 100%
  );
  pointer-events: none;
}

.image-info {
  position: absolute;
  top: 1.5rem;
  left: 1.5rem;
  background: rgba(255, 255, 255, 0.9);
  padding: 0.5rem 1rem;
  border-radius: 2rem;
  backdrop-filter: blur(10px);
}

.image-label {
  font-size: 0.875rem;
  color: #1b78f7;
  font-weight: 600;
}

.info-panel {
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  overflow-y: auto;
}

.title-section {
  text-align: center;
  margin-bottom: 1rem;
}

.title {
  font-size: 2rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
  line-height: 1.2;
  background: linear-gradient(135deg, #1b78f7 0%, #409eff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-decoration {
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #1b78f7, #409eff);
  margin: 1rem auto 0;
  border-radius: 2px;
}

.author-section {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.5rem;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-radius: 1rem;
  border: 1px solid #e2e8f0;
}

html.dark .author-section {
  background: linear-gradient(135deg, #5b5b5d 0%, #5b5b5d 100%);
}

.author-avatar {
  width: 3rem;
  height: 3rem;
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.25rem;
}

.author-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.author-label {
  font-size: 0.875rem;
  color: #94a0b1;
  font-weight: 500;
}

.author-name {
  font-size: 1.125rem;
  color: #1a202c;
  font-weight: 600;
}

html.dark .author-name {
  color: white;
}

.description-section {
  background: white;
  border-radius: 1rem;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
}

html.dark .description-section {
  background: #5b5b5d;
}

.description-title {
  font-size: 1.25rem;
  color: #1a202c;
  margin: 0 0 1rem 0;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

html.dark .description-title {
  color: white;
}

.description-title::before {
  content: "";
  width: 4px;
  height: 1.25rem;
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border-radius: 2px;
}

.description {
  color: #4a5568;
  line-height: 1.7;
  font-size: 1rem;
  margin: 0;
  text-align: justify;
}

html.dark .description {
  color: white;
}

.interaction-section {
  display: flex;
  gap: 1rem;
  align-items: center;
  justify-content: center;
  padding: 1.5rem;
  background: #f8fafc;
  border-radius: 1rem;
}

html.dark .interaction-section {
  background: #5b5b5d;
}

.like-box {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  cursor: pointer;
  user-select: none;
}

.like-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: white;
  border: 2px solid #e2e8f0;
  border-radius: 2rem;
  transition: all 0.3s ease;
  font-weight: 600;
}

html.dark .like-button {
  background: #818184;
}

.like-button:hover {
  border-color: #1b78f7;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(27, 120, 247, 0.2);
}

.like-button.liked {
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border-color: #1b78f7;
  color: white;
}

.like-icon {
  font-size: 1.25rem;
}

.like-text {
  font-size: 0.875rem;
}

.like-count {
  font-size: 1.125rem;
  font-weight: 700;
  color: #1b78f7;
  min-width: 2rem;
  text-align: center;
}

.tags-section {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  justify-content: center;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  border-radius: 2rem;
  font-size: 0.875rem;
  color: #64748b;
  font-weight: 500;
  border: 1px solid #e2e8f0;
}

.tag-item .el-icon {
  font-size: 1rem;
  color: #1b78f7;
}

/* PC 横向布局 */
@media (min-width: 1024px) {
  .content-container {
    flex-direction: row;
    max-height: 85vh;
  }

  .image-section {
    width: 60%;
    max-height: 85vh;
  }

  .photo {
    height: 85vh;
    max-height: none;
  }

  .info-panel {
    width: 40%;
    padding: 2.5rem;
    max-height: 85vh;
  }

  .title {
    font-size: 2.25rem;
  }
}

/* 移动端适配 */
@media (max-width: 768px) {
  .detail-overlay {
    padding: 0.5rem;
  }

  .close-button {
    top: 1rem;
    right: 1rem;
    width: 2.5rem;
    height: 2.5rem;
  }

  .close-button .el-icon {
    font-size: 1.25rem;
  }

  .content-container {
    border-radius: 1rem;
    max-height: 95vh;
  }

  .info-panel {
    padding: 1.5rem;
    gap: 1rem;
  }

  .title {
    font-size: 1.5rem;
  }

  .author-section {
    padding: 1rem;
  }

  .author-avatar {
    width: 2.5rem;
    height: 2.5rem;
    font-size: 1rem;
  }

  .interaction-section {
    flex-direction: column;
    gap: 0.75rem;
  }

  .like-box {
    width: 100%;
  }

  .like-button {
    width: 100%;
    justify-content: center;
  }

  .tags-section {
    gap: 0.5rem;
  }

  .tag-item {
    padding: 0.375rem 0.75rem;
    font-size: 0.75rem;
  }
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.content-container {
  animation: fadeIn 0.5s ease-out;
}

.like-button {
  animation: fadeIn 0.3s ease-out;
}

/* 滚动条样式 */
.info-panel::-webkit-scrollbar {
  width: 6px;
}

.info-panel::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 3px;
}

.info-panel::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #1b78f7, #409eff);
  border-radius: 3px;
}

.info-panel::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #0f5bb3, #1b78f7);
}
</style>
