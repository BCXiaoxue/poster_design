<template>
  <div class="carousel-wrapper">
    <div class="carousel-container">
      <el-carousel
        height="100%"
        autoplay
        :interval="3500"
        arrow="always"
        indicator-position="outside"
        direction="horizontal"
        :motion-blur="true"
        :draggable="false"
      >
        <el-carousel-item v-for="(item, index) in images" :key="index">
          <img v-lazy :src="item" class="carousel-image" draggable="false" />
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const images = ref([
  "http://122.51.51.36:9000/poster-design/20250714/507c22ad-7f37-410d-ba97-7c45dab503ef-thumb_轮播图1.png",
  "http://122.51.51.36:9000/poster-design/20250714/ba8e5890-7204-44c0-a1c7-eac5371b03cf-thumb_轮播图2.jpg",
  "http://122.51.51.36:9000/poster-design/20250715/0fadb484-798a-4b6b-9784-9b3f8d5d4f09-thumb_轮播图3.png",
  "http://122.51.51.36:9000/poster-design/20250715/2622a460-65ae-4790-98eb-39566f3268b3-thumb_轮播图4.png",
  "https://t2.chei.com.cn/ncss/cy/web/img/banner_top_7th_2_n.jpg",
]);
</script>

<style scoped>
.carousel-wrapper {
  width: 100vw;
  position: relative;
  overflow: hidden;
}

.carousel-container {
  position: relative;
  width: 100%;
  padding-top: 42.857%;
}

.carousel-container .el-carousel {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  user-select: none;
}
</style>
