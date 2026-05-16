<template>
  <div class="side-fixed-menu" v-show="showAll">
    <div class="side-item" @click="onClickPerson">
      <el-icon :size="24"><User /></el-icon>
      <span class="label">个人主页</span>
    </div>

    <div class="side-item" @click="onClickSignup">
      <el-icon :size="24"><TrophyBase /></el-icon>
      <span class="label">参赛报名</span>
    </div>

    <div class="side-item" @click="">
      <el-icon :size="24"><ChatDotRound /></el-icon>
      <span class="label">消息列表</span>
    </div>

    <div class="side-item back-top" v-show="showBackToTop" @click="backToTop">
      <el-icon :size="24"><ArrowUp /></el-icon>
      <span class="label">回到顶部</span>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { useRouter } from "vue-router";
import { User, Star, ArrowUp } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { onMounted, onBeforeUnmount } from "vue";

const props = defineProps({
  container: Object,
});

const router = useRouter();

const showAll = ref(false);
const showBackToTop = ref(false);

const handleScroll = (el) => {
  showAll.value = el?.scrollTop > 500;
  showBackToTop.value = el?.scrollTop > 600;
};

watch(
  () => props.container,
  (newEl, oldEl) => {
    if (oldEl) oldEl.removeEventListener("scroll", onScroll);
    if (newEl) newEl.addEventListener("scroll", onScroll, { passive: true });
  },
  { immediate: true }
);

function onScroll() {
  handleScroll(props.container);
}

function backToTop() {
  props.container?.scrollTo({ top: 0, behavior: "smooth" });
}

onBeforeUnmount(() => {
  props.container?.removeEventListener("scroll", onScroll);
});

const onClickSignup = () => {
  router.push({
    path: "/competitionlist",
    query: { Type: 1 },
  });
};

const onClickPerson = () => {
  router.push("/personal/account");
};
</script>

<style scoped>
.side-fixed-menu {
  position: fixed;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: 20px;
  z-index: 1000;
  padding: 15px;
  transition: all 0.3s ease;
}

.side-item {
  position: relative;
  color: #666;
  background: #f5f5f5;
  padding: 12px;
  border-radius: 50%;
  box-shadow: 0 2px 6px #0002;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.side-item:hover {
  color: #ffffff;
  background: #ff4d4d;
  transform: translateX(-5px);
  box-shadow: 0 4px 12px #ff4d4d44;
}

.side-item .label {
  position: absolute;
  right: 60px;
  background: #ff4d4d;
  color: #ffffff;
  padding: 6px 12px;
  border-radius: 12px;
  box-shadow: 0 4px 12px #ff4d4d44;
  white-space: nowrap;
  transform: translateX(20px);
  opacity: 0;
  transition: all 0.3s ease;
  pointer-events: none;
}

.side-item:hover .label {
  transform: translateX(0);
  opacity: 1;
  pointer-events: all;
}
</style>
