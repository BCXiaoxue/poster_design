<template>
  <div class="spinner-wrapper">
    <el-icon class="is-loading"><Loading /></el-icon>
    <p class="loading-text">{{ text }}</p>
  </div>
</template>

<script setup>
import { Loading } from "@element-plus/icons-vue";

defineProps({
  text: {
    type: String,
    default: "加载中...",
  },
});
</script>

<style scoped>
.spinner-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  color: #999;
  margin-top: 30vh;
}

.is-loading {
  font-size: 48px;
  animation: rotate 1s linear infinite;
}

.loading-text {
  margin-top: 12px;
  font-size: 16px;
  color: #666;
}

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
