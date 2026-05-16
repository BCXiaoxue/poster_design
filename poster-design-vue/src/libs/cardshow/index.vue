<template>
  <swiper
    :modules="[EffectCoverflow, Pagination, Autoplay]"
    effect="coverflow"
    grabCursor
    centeredSlides
    loop
    :slides-per-view="slidesPerView"
    :coverflowEffect="coverflowSettings"
    :autoplay="{ delay: 3000, disableOnInteraction: false }"
    :pagination="{ clickable: true }"
    class="card-carousel"
  >
    <swiper-slide
      v-for="(item, index) in items"
      :key="index"
      class="slide-wrapper"
    >
      <div class="card" @click="onClickCard(item)">
        <div class="img-wrapper">
          <img
            v-lazy
            :src="item.img"
            :alt="item.title"
            @load="item.loading = false"
          />
          <!-- <div v-if="item.loading" class="img-skeleton">Loading...</div> -->
        </div>
        <div class="card-info">
          <h4 class="title">{{ item.title }}</h4>
          <p class="desc">{{ item.desc }}</p>
        </div>
      </div>
    </swiper-slide>
  </swiper>
</template>

<script setup>
import { Swiper, SwiperSlide } from "swiper/vue";
import { EffectCoverflow, Pagination, Autoplay } from "swiper/modules";
import "swiper/css";
import "swiper/css/effect-coverflow";
import "swiper/css/pagination";
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const props = defineProps({
  items: {
    type: Array,
    required: true,
  },
});

const slidesPerView = ref(3);

const coverflowSettings = {
  rotate: 40,
  stretch: 0,
  depth: 200,
  modifier: 1.5,
  slideShadows: false,
};

const onClickCard = (item) => {
  // console.log("点击卡片:", item);
  // router.push(`/competition/${item.id}`);
  router.push({ name: "competition", params: { id: item.id } });
};

onMounted(() => {
  const updateView = () => {
    const width = window.innerWidth;
    if (width < 768) {
      slidesPerView.value = 1.2;
    } else if (width < 1024) {
      slidesPerView.value = 2;
    } else {
      slidesPerView.value = 2.5;
    }
  };
  updateView();
  window.addEventListener("resize", updateView);
});
</script>

<style scoped>
.swiper-slide-shadow-left,
.swiper-slide-shadow-right {
  display: none !important;
}

.card-carousel {
  padding: 50px 0;
  max-width: 100%;
}

.slide-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  perspective: 1000px;
}

.card {
  width: 90%;
  max-width: 400px;
  border-radius: 16px;
  overflow: hidden;
  /* box-shadow: 0 16px 40px rgba(0, 0, 0, 0.2); */
  background: #fff;
  cursor: pointer;
  transition: transform 0.4s ease, box-shadow 0.4s;
}

.card:hover {
  transform: scale(1.06);
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.3);
}

html.dark .card {
  background-color: #444343;
}

.img-wrapper {
  position: relative;
  width: 100%;
  /* height: 200px; */
  background: #f0f0f0;
}
.img-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.3s ease;
}
.img-wrapper img.img-loading {
  opacity: 0;
}
.img-skeleton {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #ddd, #eee);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #aaa;
  font-size: 14px;
}

.card-info {
  padding: 16px;
  text-align: center;
}
.card-info .title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 6px;
}
.card-info .desc {
  font-size: 14px;
  color: #666;
}
</style>
