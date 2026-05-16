<template>
  <div>
    <!-- 列表处理 -->
    <!-- <infinite-vue v-model="loading" :isFinished="isFinished" @onLoad="loadMore"> -->
    <waterfall-vue
      :key="'waterfall-infinite'"
      ref="waterfallRef"
      :data="imgData"
      :column="5"
      :picturePreReading="false"
    >
      <template v-slot="{ item, width }">
        <itemVue :data="item" :width="width" @click="onToDetail" />
      </template>
    </waterfall-vue>
    <!-- </infinite-vue> -->
    <transition
      :css="false"
      @before-enter="beforeEnter"
      @enter="enter"
      @leave="leave"
    >
      <detail-vue v-if="isVisibleDetail" :id="currentDetail.id" />
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import waterfallVue from "@/libs/waterfall/index.vue";
import itemVue from "./item.vue";
import { useRoute, useRouter } from "vue-router";
import { getCompetitionById } from "../../../../apis/competition";
import gsap from "gsap";
import { useEventListener } from "@vueuse/core";
import detailVue from "../detail/detail.vue";

const route = useRoute();
const router = useRouter();
const targetList = ref({});
const imgData = ref([]);
const loading = ref(false);
const isFinished = ref(false);
const waterfallRef = ref(null);

// 获取图片宽高
function getImageSize(url) {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.src = url;
    img.onload = () => {
      resolve({ width: img.width, height: img.height });
    };
    img.onerror = reject;
  });
}

const getCompetition = async () => {
  try {
    const res = await getCompetitionById(route.params.id);
    if (res.code === 1 && res.data) {
      targetList.value = res.data;
      const imageList = res.data.workImages;

      // 获取每张图片的宽高并封装成对象
      const promises = imageList.map(async (item) => {
        try {
          const { width, height } = await getImageSize(item.url);
          return {
            id: item.id,
            url: item.url,
            width,
            height,
          };
        } catch (e) {
          return {
            id: item.id,
            url: item.url,
            width: 200,
            height: 200,
          };
        }
      });

      imgData.value = await Promise.all(promises);
    } else {
      ElMessage.error(res.msg || "获取竞赛信息失败");
    }
  } catch (error) {
    console.error("接口错误：", error);
    ElMessage.error("请求失败，请稍后再试");
  }
};

onMounted(() => {
  getCompetition();
});

const isVisibleDetail = ref(false);
const currentDetail = ref({});

// const onToDetail = (item) => {
//   currentDetail.value = item;
//   isVisibleDetail.value = true;
//   router.push(`/competition/detail/${item.id}`);
// };

const onToDetail = (item) => {
  console.log(item);
  history.pushState(null, null, `/competition/detail/${item.id}`);
  currentDetail.value = item;
  isVisibleDetail.value = true;
};

/**
 * 监听浏览器后退按钮事件
 */
useEventListener(window, "popstate", () => {
  isVisibleDetail.value = false;
});

/**
 * 进入动画开始前
 */
const beforeEnter = (el) => {
  gsap.set(el, {
    scaleX: 0,
    scaleY: 0,
    transformOrigin: "0 0",
    translateX: currentDetail.value.localtion?.translateX,
    translateY: currentDetail.value.localtion?.translateY,
    opacity: 0,
  });
};
/**
 * 进入动画执行中
 */
const enter = (el, done) => {
  gsap.to(el, {
    duration: 0.3,
    scaleX: 1,
    scaleY: 1,
    opacity: 1,
    translateX: 0,
    translateY: 0,
    onComplete: done,
  });
};
/**
 * 离开动画执行中
 */
const leave = (el, done) => {
  gsap.to(el, {
    duration: 0.3,
    scaleX: 0,
    scaleY: 0,
    x: currentDetail.value.localtion?.translateX,
    y: currentDetail.value.localtion?.translateY,
    opacity: 0,
  });
};
</script>

<style scoped></style>
