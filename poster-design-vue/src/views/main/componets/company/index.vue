<template>
  <div class="sponsor-container">
    <div class="box">
      <h2>企业赞助</h2>
      <div class="row-wrapper" v-for="(src, index) in images" :key="index">
        <div
          class="scroll-track"
          :class="index % 2 === 0 ? 'scroll-left' : 'scroll-right'"
        >
          <!-- 两轮图片用于无缝滚动 -->
          <img
            v-lazy
            v-for="i in 2"
            :key="i"
            :src="src"
            class="sponsor-image"
            alt="企业赞助"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 手动导入所有图片
import company1 from '@/assets/images/company1.png'
import company2 from '@/assets/images/company2.png'
import company3 from '@/assets/images/company3.png'
import company4 from '@/assets/images/company4.png'

const images = [
  company1,
  company2,
  company3,
  company4
]
</script>

<style scoped>
.sponsor-container {
  display: flex;
  justify-content: center;
  width: 100%;
  padding: 20px 0;
  /* background-color: #fff; */
}

.box {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

h2 {
  margin-bottom: 10px;
}

.row-wrapper {
  width: 100%;
  height: 80px;
  overflow: hidden;
  margin-bottom: 20px;
  position: relative;
}

.scroll-track {
  display: flex;
  align-items: center;
  height: 100%;
  width: max-content;
}

.sponsor-image {
  height: 100%;
  margin-right: 40px;
  flex-shrink: 0;
  object-fit: contain;
}

.scroll-left {
  animation: scrollLeft 50s linear infinite;
}

.scroll-right {
  animation: scrollRight 50s linear infinite;
}

@keyframes scrollLeft {
  0% {
    transform: translateX(0%);
  }
  100% {
    transform: translateX(-50%);
  }
}

@keyframes scrollRight {
  0% {
    transform: translateX(-50%);
  }
  100% {
    transform: translateX(0%);
  }
}
</style>
