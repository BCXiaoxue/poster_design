<template>
  <div ref="bodyContainer" class="box">
    <div class="headervue"><header-vue></header-vue></div>

    <!-- 轮播图区域 - 移到顶部 -->
    <div
      class="carousel-section"
      v-if="informContent.images && informContent.images.length > 0"
    >
      <swiper
        ref="swiperRef"
        :modules="[Autoplay, EffectCoverflow, Mousewheel]"
        effect="coverflow"
        :coverflowEffect="{
          rotate: 30,
          stretch: 0,
          depth: 100,
          modifier: 1,
          slideShadows: true,
        }"
        :autoplay="{ delay: 3000 }"
        :loop="true"
        :mousewheel="true"
        :centeredSlides="true"
        :slidesPerView="1.3"
        :speed="800"
        class="custom-swiper"
      >
        <swiper-slide v-for="item in informContent.images" :key="item">
          <img :src="item" alt="" class="swiper-image" />
        </swiper-slide>
      </swiper>
    </div>

    <div class="body">
      <div class="center">
        <div class="title">
          {{ informContent.title }}
        </div>
        <div class="content">
          {{ informContent.content }}
        </div>
        <div class="teil">
          <a
            href="javascript:void(0)"
            v-show="!(Id.beforeId == null)"
            class="last"
            @click="last(Id.beforeId)"
            >上一条：{{ Title.beforeTitle }}</a
          >
          <a
            href="javascript:void(0)"
            v-show="!(Id.afterId == null)"
            class="next"
            @click="next(Id.afterId)"
            >下一条：{{ Title.afterTitle }}</a
          >
        </div>
      </div>
      <div class="right">
        <div class="right-title">
          <div class="right-left-title">为你推荐</div>
          <div class="right-right-title">
            <a href="javascript:void(0)" @click="Change">
              <el-icon><RefreshLeft /></el-icon>
              换一批
            </a>
          </div>
        </div>
        <a
          href="javascript:void(0)"
          class="right-centent"
          v-for="item in titleList"
          :key="item.id"
          @click="clickItem(item.id)"
        >
          <img v-lazy :src="item.images[0]" alt="" />
          <div class="right-content-title">
            {{ item.title }}
          </div>
        </a>
      </div>
    </div>
    <!-- 侧边栏 -->
    <div class="side-fixed-menu" v-show="true">
      <div class="side-item" @click="person" v-show="showBackToTop">
        <el-icon :size="24"><User /></el-icon>
        <span class="label">个人主页</span>
      </div>

      <div class="side-item" @click="onClickSignup">
        <el-icon :size="24"><TrophyBase /></el-icon>
        <span class="label">参赛报名</span>
      </div>

      <div class="side-item" @click="inform">
        <el-icon :size="24"><ChatDotRound /></el-icon>
        <span class="label">消息列表</span>
      </div>

      <div class="side-item back-top" v-show="showBackToTop" @click="backToTop">
        <el-icon :size="24"><ArrowUp /></el-icon>
        <span class="label">回到顶部</span>
      </div>
    </div>
    <footer-vue></footer-vue>
  </div>
</template>

<script setup>
import headerVue from "@/libs/header/index.vue";
import footerVue from "@/libs/footer/index.vue";
import { ref, onMounted, watch, onBeforeUnmount, nextTick } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  getAnnunciationById,
  getAnnunciationRandom,
  judgmentAnnunciationById,
} from "@/apis/annunciation.js";
import { queryNewsById, getNewsRandom, judgmentNewsById } from "@/apis/news.js";
import { User, Star, ArrowUp } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/css";
import "swiper/css/effect-coverflow";
import "swiper/css/autoplay";
import "swiper/css/mousewheel";
import { Autoplay, EffectCoverflow, Mousewheel } from "swiper/modules";

const swiperRef = ref(null);
const route = useRoute();
const router = useRouter();

const informContent = ref({});
const Type = ref("");
const Id = ref({
  beforeId: null,
  afterId: null,
});
const Title = ref({
  beforeTitle: "无",
  afterTitle: "无",
});
const competitionId = ref(0);

// 加载内容的方法
const loadData = async (id) => {
  let result;
  if (Type.value == 1) {
    result = await getAnnunciationById(id);
    competitionId.value = result.data.competitionId;
    Id.value = (await judgmentAnnunciationById(id)).data;
    if (Id.value.beforeId != null) {
      Title.value.beforeTitle = (
        await getAnnunciationById(Id.value.beforeId)
      ).data.title;
    }
    if (Id.value.afterId != null) {
      Title.value.afterTitle = (
        await getAnnunciationById(Id.value.afterId)
      ).data.title;
    }
  } else {
    result = await queryNewsById(id);
    competitionId.value = result.data.competitionId;
    Id.value = (await judgmentNewsById(id)).data;
    if (Id.value.beforeId != null) {
      Title.value.beforeTitle = (
        await queryNewsById(Id.value.beforeId)
      ).data.title;
    }
    if (Id.value.afterId != null) {
      Title.value.afterTitle = (
        await queryNewsById(Id.value.afterId)
      ).data.title;
    }
  }

  if (result.code === 1) {
    informContent.value = result.data;
  }
};

const bodyContainer = ref(null);
const showBackToTop = ref(false);

function onBoxScroll() {
  showBackToTop.value = bodyContainer.value?.scrollTop > 60;
}

onMounted(async () => {
  const id = route.query.id;
  Type.value = route.query.Type;
  await loadData(id);
  Change();
  await nextTick();
  if (swiperRef.value && swiperRef.value.swiper) {
    swiperRef.value.swiper.update();
  }
  if (bodyContainer.value) {
    bodyContainer.value.addEventListener("scroll", onBoxScroll, {
      passive: true,
    });
  }
});

// 监听 id 变化，重新加载内容
watch(
  () => route.query.id,
  async (newId) => {
    if (newId) {
      await loadData(newId);
      backToTop();
    }
  }
);

// 上一条 / 下一条跳转
const last = (id) => {
  router.push({
    path: "/informContent",
    query: { id, Type: Type.value },
  });
};

const next = (id) => {
  router.push({
    path: "/informContent",
    query: { id, Type: Type.value },
  });
};
const titleList = ref([]);
const n = 5;
const Change = async () => {
  let result;

  if (Type.value == 1) {
    result = await getAnnunciationRandom(n);
  } else {
    result = await getNewsRandom(n);
  }

  if (result.code) {
    titleList.value = result.data;
  }
};

const clickItem = (id) => {
  router.push({
    path: "/informContent",
    query: { id: id, Type: Type.value },
  });
};

// 侧边栏
// const props = defineProps({
//   container: Object,
// });

function backToTop() {
  bodyContainer.value?.scrollTo({ top: 0, behavior: "smooth" });
}

onBeforeUnmount(() => {
  if (bodyContainer.value) {
    bodyContainer.value.removeEventListener("scroll", onBoxScroll);
  }
});

const person = () => {
  router.push("/personal/account");
};

const inform = () => {
  if (Type.value == 1) {
    router.push({
      path: "/inform",
      query: { Type: 1 },
    });
  } else {
    router.push({
      path: "/inform",
      query: { Type: 2 },
    });
  }
};

const onClickSignup = () => {
  router.push(`/competition/${competitionId.value}`);
};
</script>

<style scoped>
/* 页面整体背景和主内容卡片化 */
.box {
  min-height: 100vh;
  max-height: 100vh;
  background: #f7f8fa;
  overflow-y: auto;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
}

.custom-swiper {
  width: 100%;
  max-width: 1200px;
  height: 500px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.07);
}

.swiper-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease, opacity 0.6s ease;
  border-radius: 16px;
}

.swiper-slide-active .swiper-image {
  transform: scale(1.05);
}

/* 轮播图区域样式 */
.carousel-section {
  display: flex;
  justify-content: center;
  padding: 40px 0 20px 0;
  background: transparent;
}

.carousel-section .el-carousel {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  box-sizing: border-box;
}

.body {
  display: flex;
  justify-content: center;
  gap: 32px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
  padding: 20px 0 32px 0;
  background: transparent;
}

.headervue {
  background-color: #fff;
}

html.dark .headervue {
  background-color: #121212;
}

html.dark .box {
  background-color: #333;
}

.center {
  flex: 1 1 0;
  min-width: 0;
  max-width: calc(100% - 332px); /* 300px + 32px gap */
  background: #fff;
  border-radius: 24px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
  min-height: 700px;
  padding: 0 40px 40px 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  align-self: flex-start;
}

html.dark .center {
  background-color: #444343;
  color: white;
}

/* 轮播图美化 */
.el-carousel {
  border-radius: 20px;
  overflow: hidden;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.07);
  width: 100%;
  height: 300px;
  min-height: 300px;
  display: block;
}
.el-carousel__item {
  width: 100%;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f7f8fa;
  overflow: hidden;
}
.el-carousel__item img {
  width: auto;
  height: 100%;
  max-width: 100%;
  max-height: 100%;
  display: block;
  object-fit: contain;
  background: #f7f8fa;
  border-radius: 0;
}
/* 标题美化 */
.title {
  text-align: center;
  font-size: 2.1rem;
  font-weight: 600;
  padding: 32px 0 32px 0;
  position: relative;
  color: #222;
  letter-spacing: 1px;
  background: none;
  white-space: pre-line;
  margin-top: 0;
}

html.dark .title {
  color: white;
}

.title::after {
  content: "";
  position: absolute;
  left: 50%;
  bottom: 0;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: #409eff;
  border-radius: 2px;
}
.content {
  margin: 0 0 0 0;
  margin-top: 24px;
  font-size: 1.13rem;
  color: #333;
  line-height: 1.9;
  letter-spacing: 0.2px;
  white-space: pre-line;
  padding: 0 8px;
}

html.dark .content {
  color: white;
}

.teil {
  margin-top: 32px;
  padding-bottom: 0;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}
.teil a {
  display: block;
  max-width: 350px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 1rem;
  color: #888;
  background: #eaf4ff;
  border-radius: 12px;
  padding: 8px 16px;
  transition: background 0.2s, color 0.2s;
}

html.dark .teil a {
  background-color: #6d6a6a;
  color: #cccbcb;
}

.last,
.next {
  text-decoration: none;
  color: #888;
}
.last:hover,
.next:hover {
  color: #409eff;
  background: #eaf4ff;
}
/* 右侧推荐区卡片化 */
.right {
  width: 300px;
  flex-shrink: 0;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
  padding: 32px 18px 18px 18px;
  display: flex;
  flex-direction: column;
  gap: 0;
  height: fit-content;
  align-self: flex-start;
  margin-top: 0;
}

html.dark .right {
  background-color: #444343;
  color: white;
}

.right-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1.5px solid #f0f0f0;
  padding-bottom: 10px;
  margin-bottom: 10px;
}
.right-left-title {
  font-size: 1.15rem;
  font-weight: 600;
  /* color: #222; */
}
.right-right-title a {
  color: #bdbdbd;
  font-size: 0.98rem;
  text-decoration: none;
  transition: color 0.2s;
}
.right-right-title a:hover {
  color: #ff9800;
}
.right-centent {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 0;
  text-decoration: none;
  /* color: #222; */
  border-bottom: 1.5px solid #f0f0f0;
  transition: background 0.2s, box-shadow 0.2s;
  border-radius: 10px;
}
.right-centent:last-child {
  border-bottom: none;
}
.right-centent img {
  width: 56px;
  height: 56px;
  object-fit: cover;
  border-radius: 10px;
  background: #f5f5f5;
}
.right-content-title {
  font-size: 1rem;
  color: #333;
  font-weight: 500;
  line-height: 1.3;
  flex: 1;
}

html.dark .right-content-title {
  color: white;
}

.right-centent:hover {
  background: #eaf4ff;
  color: #409eff;
  box-shadow: 0 2px 8px #409eff22;
}

html.dark .right-centent:hover {
  background: #333;
  color: #409eff;
  box-shadow: 0 2px 8px #409eff22;
}
/* 侧边栏美化 */
.side-fixed-menu {
  position: fixed;
  left: 32px;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: 24px;
  z-index: 1000;
  padding: 18px 8px;
  /* background: #fff; */
  /* border-radius: 18px; */
  /* box-shadow: 0 4px 24px rgba(0,0,0,0.08); */
}
.side-item {
  color: #666;
  background: #f7f8fa;
  padding: 14px;
  border-radius: 50%;
  box-shadow: 0 2px 8px #0001;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.side-item:hover {
  color: #fff;
  background: #409eff;
  transform: translateX(-5px) scale(1.08);
  box-shadow: 0 4px 16px #409eff44;
}
.side-item .label {
  position: absolute;
  left: 100%;
  top: 50%;
  transform: translateY(-50%) translateX(10px);
  background: #409eff;
  color: #fff;
  padding: 7px 16px;
  border-radius: 14px;
  box-shadow: 0 4px 12px #409eff44;
  white-space: nowrap;
  opacity: 0;
  pointer-events: none;
  font-size: 1rem;
  transition: opacity 0.3s, transform 0.3s;
  z-index: 10;
}
.side-item:hover .label {
  opacity: 1;
  pointer-events: all;
  transform: translateY(-50%) translateX(24px);
}
/* 响应式优化 */
@media (max-width: 1200px) {
  .carousel-section .el-carousel {
    max-width: 98vw;
    margin: 0 1vw;
  }

  .body {
    flex-direction: column;
    align-items: center;
    gap: 32px;
    padding-left: 1vw;
    padding-right: 1vw;
  }
  .center {
    width: 98vw;
    min-width: 0;
    padding: 18px 4vw 32px 4vw;
  }
  .right {
    width: 98vw;
    margin-top: 0;
    padding: 32px 4vw 18px 4vw;
  }
}

@media (max-width: 700px) {
  .carousel-section {
    padding: 64px 0 8px 0;
  }
  .carousel-section .el-carousel {
    max-width: 100vw;
    padding: 0;
  }
  .body {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
    max-width: 100vw;
    padding: 0 0 16px 0;
  }
  .center {
    width: 100%;
    max-width: 100%;
    box-sizing: border-box;
    border-radius: 0;
    box-shadow: none;
    padding: 0 8px 16px 8px;
    min-height: unset;
  }
  .right {
    width: 100%;
    max-width: 400px;
    margin: 0 auto;
    border-radius: 0;
    box-shadow: none;
    padding: 16px 16px 8px 16px;
    margin-top: 0;
  }
  .title {
    font-size: 1.2rem;
    padding: 16px 0 16px 0;
  }
  .content {
    font-size: 0.98rem;
    padding: 0 2px;
  }
  .side-fixed-menu {
    display: none;
  }
}
</style>
