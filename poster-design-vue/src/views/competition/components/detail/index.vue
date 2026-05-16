<template>
  <div class="detail">
    <detail-vue :id="$route.params.id" />
  </div>
</template>

<script setup>
import detailVue from "./detail.vue";
</script>
<style scoped>
.detail {
  width: 100%;
  height: 100%;
}
</style>
