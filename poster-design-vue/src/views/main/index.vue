<template>
  <div ref="bodyContainer" class="body">
    <div class="header">
      <header-vue :isRightShow="true"></header-vue>
    </div>
    <div class="main">
      <loading-vue v-if="isLoading"></loading-vue>
      <div v-else>
        <carousel-vue></carousel-vue>
        <inform-vue></inform-vue>
        <flipcard-vue :items="flipcards" @card-click="handleCardClick">
          <template #front="{ item }">
            <div class="card-content">
              <img :src="item.src" alt="" class="card-dark" />
              <h3>{{ item.title }}</h3>
            </div>
          </template>
          <template #back="{ item }">
            <div class="card-content">
              <img :src="item.src" alt="" />
              <p>{{ item.detail }}</p>
            </div>
          </template>
        </flipcard-vue>

        <div class="cardshow">
          <h2>优秀作品集</h2>
          <cardshow-vue :items="cards"> </cardshow-vue>
        </div>
        <div class="video-section">
          <h2>精彩回顾</h2>
          <div class="video-wrapper">
            <div v-if="!isPlaying" class="video-cover" @click="playVideo">
              <img v-lazy :src="videoCover" alt="视频封面" class="cover-img" />
              <div class="play-button">▶</div>
            </div>
            <video
              v-else
              ref="videoPlayer"
              class="video-player"
              controls
              autoplay
              @ended="isPlaying = false"
            >
              <source :src="videoSrc" type="video/mp4" />
              您的浏览器不支持视频播放。
            </video>
          </div>
        </div>

        <company-vue></company-vue>
      </div>
    </div>
    <aside-vue :container="bodyContainer" v-if="isAsideShow"></aside-vue>
    <footer-vue></footer-vue>
  </div>
</template>

<script setup>
import headerVue from "@/libs/header/index.vue";
import footerVue from "@/libs/footer/index.vue";
import carouselVue from "./componets/carousel/index.vue";
import informVue from "./componets/inform/index.vue";
import companyVue from "./componets/company/index.vue";
import cardshowVue from "@/libs/cardshow/index.vue";
import asideVue from "./componets/aside/index.vue";
import { ref, onMounted, watch, onBeforeUnmount, nextTick } from "vue";
import { isMobile } from "@/utils/flexible.js";
import { getCompetitionFirstWork } from "../../apis/competition";
import loadingVue from "@/libs/loading/index.vue";
import { getUserInfo } from "@/apis/user";

const bodyContainer = ref(null);
const isAsideShow = ref(true);
const cards = ref([]);
const isLoading = ref(true);

import flipcardVue from "@/libs/flipcard/index.vue";

const defaultImg = "@/assets/images/work1.jpg";

onMounted(async () => {});

const getFirstWork = async () => {
  const res = await getCompetitionFirstWork();
  const rawList = res.data || [];
  isLoading.value = false;
  cards.value = rawList.map((item) => {
    return {
      id: item.competitionId,
      title: item.competitionName + "优秀作品",
      desc: "",
      img: item.worksUrl?.[0] || defaultImg,
    };
  });
};

onMounted(() => {
  getFirstWork();
});

const flipcards = [
  {
    title: "参赛报名",
    desc: "",
    detail: "前往报名",
    src: "https://www.baijun.org/images/feature-icon.png",
  },
  {
    title: "往期回顾",
    desc: "",
    detail: "立刻前往",
    src: "https://www.baijun.org/images/feature-icon-2.png",
  },
  {
    title: "大赛预告",
    desc: "",
    detail: "前往了解",
    src: "https://www.baijun.org/images/feature-icon-3.png",
  },
];

import { useRouter } from "vue-router";
const router = useRouter();

function handleCardClick(item) {
  sessionStorage.setItem("scroll-index", window.scrollY.toString());
  if (item.title === "参赛报名") {
    router.push({
      path: "/competitionlist",
      query: { Type: 1 },
    });
  } else if (item.title === "往期回顾") {
    router.push({
      path: "/competitionlist",
      query: { Type: 2 },
    });
  } else if (item.title === "大赛预告") {
    router.push({
      path: "/competitionlist",
      query: { Type: 3 },
    });
  }
}

watch(
  () => isMobile.value,
  () => {
    isAsideShow.value = !isMobile.value;
  },
  { immediate: true }
);

const scrollKey = "scroll-index";

function saveScroll() {
  if (bodyContainer.value) {
    sessionStorage.setItem(scrollKey, bodyContainer.value.scrollTop.toString());
  }
}

let isBackNav = false;

onMounted(async () => {
  await nextTick();
  if (window.history.state?.back) {
    isBackNav = true;
  }

  if (bodyContainer.value) {
    if (isBackNav) {
      const saved = sessionStorage.getItem(scrollKey);
      if (saved) {
        bodyContainer.value.scrollTo({
          top: parseInt(saved),
          behavior: "auto",
        });
      }
    } else {
      bodyContainer.value.scrollTo({ top: 0, behavior: "auto" });
    }
  }

  window.addEventListener("beforeunload", saveScroll);
});

onBeforeUnmount(() => {
  saveScroll();
  window.removeEventListener("beforeunload", saveScroll);
});
import videoCoverImage from "@/assets/images/team.jpg";
const videoCover = videoCoverImage;
const videoSrc =
  "https://chsi-v.oss-cn-beijing.aliyuncs.com/ncss/cy/2024-index.mp4";
const isPlaying = ref(false);
const videoPlayer = ref(null);

function playVideo() {
  isPlaying.value = true;
  nextTick(() => {
    videoPlayer.value?.play();
  });
}
</script>
<style scoped>
.body {
  min-height: 100vh;
  overflow-y: auto;
}

.header {
  padding-bottom: 15px;
}

.main {
  min-height: 100vh;
  background-color: #f2f6fa;
}

html.dark .main {
  background-color: #333;
}

.cardshow {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 16px;
  text-align: center;
}

.card-dark {
  filter: brightness(0) invert(1);
}

.video-section {
  margin-top: 40px;
  text-align: center;
}

.video-wrapper {
  position: relative;
  width: 90%;
  max-width: 1000px;
  aspect-ratio: 16 / 9;
  margin: 0 auto;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background: #000;
}

.cover-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.video-cover {
  position: relative;
  width: 100%;
  height: 100%;
  display: block;
}

.play-button {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 2.5rem;
  color: white;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  padding: 12px 20px;
  cursor: pointer;
}

.video-player {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  border: none;
  outline: none;
}

@media (max-width: 768px) {
  .video-wrapper {
    width: 100%;
    border-radius: 0;
    box-shadow: none;
  }

  .play-button {
    font-size: 2rem;
    padding: 10px 16px;
  }
}
</style>
