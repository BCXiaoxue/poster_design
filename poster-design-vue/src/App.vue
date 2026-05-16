<template>
  <div class="body">
    <router-view></router-view>
  </div>
</template>

<script setup></script>

<style scoped>
.body {
  /* display: fixed; */
  width: 100vw;
  height: 100vh;
  top: 0;
  left: 0;
  overflow-x: hidden;
}
</style>
