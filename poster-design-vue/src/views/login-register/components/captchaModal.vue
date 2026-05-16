<template>
  <div class="captcha-modal">
    <div class="captcha-header">
      <span class="title">请完成安全验证</span>
      <el-icon class="icon" @click="onReset"><Refresh /></el-icon>
      <el-icon class="icon" @click="onClose"><Close /></el-icon>
    </div>
    <div id="captcha" class="captcha-body"></div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import "@/vendor/SliderCaptcha/slidercaptcha.min.css";
import "@/vendor/SliderCaptcha/longbow.slidercaptcha.min.js";
import { ElMessage } from "element-plus";

import { Refresh, Close } from "@element-plus/icons-vue";

const emits = defineEmits(["close", "success"]);

let captcha = null;
onMounted(() => {
  captcha = sliderCaptcha({
    id: "captcha",
    onSuccess() {
      ElMessage.success("检验成功！");
      emits("success");
    },
    onFail() {
      ElMessage.warning("检验失败！");
    },
    verify() {
      return true;
    },
  });
});

const onReset = () => captcha?.reset();
const onClose = () => emits("close");
</script>

<style scoped>
.captcha-modal {
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translateX(-50%);
  width: 340px;
  height: 270px;
  background: #fff;
  border: 1px solid #ddd;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  z-index: 100;
  padding: 12px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.captcha-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 32px;
  margin-bottom: 10px;
}

.title {
  flex: 1;
  font-weight: bold;
  font-size: 14px;
}

.icon {
  cursor: pointer;
  margin-left: 8px;
  color: #666;
}
.icon:hover {
  color: #333;
}

.captcha-body {
  flex: 1;
}
</style>
