<template>
  <div class="image-card">
    <div class="image-wrapper" @click="onToDetailClick">
      <img
        v-lazy
        ref="imgTarget"
        class="image"
        :src="data.url"
        :style="{
          height: (width / data.width) * data.height + 'px',
        }"
      />
    </div>
  </div>
</template>

<script setup>
import { useElementBounding } from "@vueuse/core";
import { ref, computed } from "vue";

const props = defineProps({
  data: {
    type: Object,
    required: true,
  },
  width: {
    type: Number,
    default: 0,
  },
});

const emits = defineEmits(["click"]);

const imgTarget = ref(null);
const {
  x: imgContainerX,
  y: imgContainerY,
  width: imgContainerWidth,
  height: imgContainerHeight,
} = useElementBounding(imgTarget);

const imgContainerCenter = computed(() => {
  return {
    translateX: parseInt(imgContainerX.value + imgContainerWidth.value / 2),
    translateY: parseInt(imgContainerY.value + imgContainerHeight.value / 2),
  };
});

const onToDetailClick = () => {
  emits("click", {
    id: props.data.id,
    localtion: imgContainerCenter.value,
  });
};
</script>
<style scoped>
.image-card {
  background-color: #ffffff;
  border-radius: 8px;
  padding-bottom: 4px;
}

.image-wrapper {
  position: relative;
  width: 100%;
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
}

.image-wrapper:hover {
  opacity: 0.95;
}

.image {
  width: 100%;
  border-radius: 8px;
  background-color: transparent;
  display: block;
}
</style>
