<template>
  <div class="flipcard-wrapper">
    <div
      class="flip-card"
      v-for="(item, index) in items"
      :key="index"
      @click="handleClick(item)"
    >
      <div class="flip-inner">
        <div class="flip-front">
          <slot name="front" :item="item" :index="index" />
        </div>
        <div class="flip-back">
          <slot name="back" :item="item" :index="index" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  items: Array,
});

const emit = defineEmits(["card-click"]);

function handleClick(item) {
  emit("card-click", item);
}
</script>

<style scoped>
.flipcard-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  padding: 20px;
}

.flip-card {
  width: 400px;
  height: 200px;
  perspective: 1000px;
  cursor: pointer;
}

.flip-inner {
  position: relative;
  width: 100%;
  height: 100%;
  transition: transform 0.6s;
  transform-style: preserve-3d;
}

.flip-card:hover .flip-inner {
  transform: rotateY(180deg);
}

.flip-front,
.flip-back {
  position: absolute;
  width: 100%;
  height: 100%;
  backface-visibility: hidden;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.flip-front {
  /* background: #ffffff; */
  background: #0c5adb;
  color: white;
}

.flip-back {
  /* background: #fafafa; */
  background: #c0e95c;
  color: black;
  transform: rotateY(180deg);
}
</style>
