<template>
  <div style="min-height: 100vh">
    <header-vue :isRightShow="true"></header-vue>
    <loading-vue v-if="isLoading"></loading-vue>
    <div class="body1" v-else>
      <div class="head">
        <div class="title">{{ title }}</div>
        <el-input
          v-model="cskeys.competitionName"
          placeholder="请输入关键字搜索"
          :prefix-icon="Search"
          class="input1"
        >
          <template #append>
            <el-button type="primary" class="button" @click="search"
              ><el-icon><Search /></el-icon> 搜索</el-button
            >
          </template>
        </el-input>
      </div>
      <div class="content">
        <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
        >
          <el-menu-item index="1" style="font-size: 20px"
            >招募中的比赛</el-menu-item
          >
          <el-menu-item index="2" style="font-size: 20px"
            >往届比赛</el-menu-item
          >
          <el-menu-item index="3" style="font-size: 20px"
            >即将进行的比赛</el-menu-item
          >
        </el-menu>

        <div class="list_item" v-for="(item, index) in informList" :key="index">
          <img v-lazy src="..\..\assets\images\leftsj.png" alt="" />
          <div class="list_item_content" @click="clickItem(item.id)">
            {{ item.competitionName }}
          </div>
          <span class="list_item_time"
            >{{ item.registrationBegin }} - {{ item.registrationEnd }}</span
          >
        </div>
      </div>
      <!-- 分页条 -->
      <div class="container">
        <el-pagination
          v-model:current-page="cskeys.pageNo"
          v-model:page-size="cskeys.pageSize"
          :page-sizes="[5, 10, 20, 30, 50]"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    <footer-vue v-show="!isLoading"></footer-vue>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { Calendar, Search } from "@element-plus/icons-vue";
import headerVue from "@/libs/header/index.vue";
import footerVue from "@/libs/footer/index.vue";
import { getCompetitionList } from "../../apis/competition";
import { useRouter, useRoute } from "vue-router";
import loadingVue from "@/libs/loading/index.vue";

const isLoading = ref(true);

const route = useRoute();

//钩子函数
onMounted(() => {
  search();
});

const title = ref("大赛报名");
let cskeys = ref({
  competitionName: "",
  competitionStatus: 1,
  pageNo: 1,
  pageSize: 10,
});
const informList = ref([]);

const search = async () => {
  let result;
  if (activeIndex.value == 1) {
    cskeys.value.competitionStatus = 1;
    result = await getCompetitionList(cskeys.value);
  } else if (activeIndex.value == 2) {
    cskeys.value.competitionStatus = 3;
    result = await getCompetitionList(cskeys.value);
  } else {
    cskeys.value.competitionStatus = 0;
    result = await getCompetitionList(cskeys.value);
  }
  if (result.code == 1) {
    informList.value = result.data.records;
    total.value = result.data.total;
    isLoading.value = false;
  }
};

const updateType = (newType) => {
  router.push({
    path: route.path,
    query: {
      ...route.query,
      Type: newType,
    },
  });
};

const activeIndex = ref(route.query.Type);
const handleSelect = (key, keyPath) => {
  activeIndex.value = key;
  if (key == 1) {
    title.value = "大赛报名";
    updateType(1);
  } else if (key == 2) {
    title.value = "回顾往届大赛";
    updateType(2);
  } else {
    title.value = "大赛预告";
    updateType(3);
  }
  cskeys.value.competitionName = "";
  cskeys.value.pageNo = 1;
  cskeys.value.pageSize = 10;
  search();
};

//分页
const background = ref(true); //背景色
const total = ref(100); //总记录数
const router = useRouter();

//每页展示记录数变化
const handleSizeChange = (val) => {
  search();
};
//页面变化时触发
const handleCurrentChange = (val) => {
  search();
};

const clickItem = (id) => {
  router.push(`/competition/${id}`);
};
</script>

<style scoped>
.body1 {
  background: #f5f5f5;
  text-align: center;
  min-height: 90vh;
}

html.dark .body1 {
  background-color: #333;
}
.head {
  background: #1b78f7;
  text-align: center;
  padding: 55px 0;
  margin-bottom: 40px;
}
html.dark .head {
  background-color: darkblue;
}
.title {
  font-size: 40px;
  color: white;
  font-family: YouSheBiaoTiHei;
  margin: 10px 0;
}
.input1 {
  width: 568px;
  height: 48px;
  margin: 10px 0;
  border-radius: 8px;
  font-size: 16px;
  max-width: 90vw;
  box-sizing: border-box;
}
html.dark .input1 {
  background-color: #444343;
}

:deep(.el-input-group__append) {
  padding: 0;
  /* background-color: white; */
  border: none;
}

.button {
  background: #1b78f7 !important;
  color: white !important;
  width: 88px;
  height: 40px;
  margin: 5px;
  font-size: 18px;
  border: none !important;
  border-radius: 0 8px 8px 0 !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  white-space: nowrap !important;
}

/* 强制覆盖 Element Plus 搜索框样式 */
.input1 .el-input__wrapper {
  border-radius: 8px !important;
  box-shadow: 0 0 0 1px #dcdfe6 inset !important;
}

.input1 .el-input__wrapper:hover {
  box-shadow: 0 0 0 1px #c0c4cc inset !important;
}

.input1 .el-input__wrapper.is-focus {
  box-shadow: 0 0 0 1px #409eff inset !important;
}

/* 确保搜索按钮在输入框内正确显示 */
.input1 .el-input-group__append .el-button {
  height: 40px !important;
  line-height: 40px !important;
  padding: 0 15px !important;
  margin: 4px !important;
  border-radius: 4px !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .input1 {
    width: 90% !important;
    max-width: 400px !important;
    height: 44px !important;
  }

  .button {
    width: 70px !important;
    height: 36px !important;
    font-size: 14px !important;
    margin: 4px !important;
  }

  .title {
    font-size: 28px !important;
  }

  .head {
    padding: 40px 20px !important;
  }
}

@media (max-width: 480px) {
  .input1 {
    width: 95% !important;
    max-width: 350px !important;
    height: 40px !important;
  }

  .button {
    width: 60px !important;
    height: 32px !important;
    font-size: 12px !important;
    margin: 4px !important;
  }

  .title {
    font-size: 24px !important;
  }

  .head {
    padding: 30px 15px !important;
  }
}
.content {
  background: white;
  width: 1200px;
  margin: 0 auto 32px;
  padding: 8px 64px 10px;
  border-radius: 16px;
  box-shadow: 0 2px 8px 0 rgba(137, 141, 145, 0.06);
}
html.dark .content {
  background-color: #444343;
}
.list_item {
  display: flex;
  align-items: flex-start;
  padding: 32px 0;
  border-bottom: 2px dashed rgba(0, 0, 0, 0.15);
}
html.dark .list_item:hover {
  background-color: #333;
}
.list_item:last-child {
  border-bottom: none;
}
.list_item img {
  width: 24px;
}
.list_item_content {
  font-size: 18px;
  margin-left: 8px;
  cursor: pointer; /* 显示为手型，表示可点击 */
}
.list_item_content:hover {
  color: #4087f7;
}
.list_item_time {
  margin-left: auto;
  color: #888;
  font-size: 28px;
  font-family: "Consolas", "Menlo", "Monaco", monospace;
  background: none;
  border-radius: 0;
  padding: 0 8px;
  font-weight: 400;
  letter-spacing: 1px;
  display: inline-block;
  min-width: 80px;
  text-align: right;
  box-shadow: none;
  transition: none;
}
.container {
  width: fit-content; /* 或者指定具体宽度 */
  margin: 20px auto;
}

@media (min-width: 1200px) {
  .body1 {
    background: #f5f5f5;
    min-height: 90vh;
    padding-bottom: 32px;
  }
  .head {
    background: #1b78f7;
    padding: 64px 0 48px 0;
    margin-bottom: 48px;
  }
  .title {
    font-size: 48px;
    color: #fff;
    font-family: YouSheBiaoTiHei, sans-serif;
    margin: 16px 0 24px 0;
    letter-spacing: 2px;
    text-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  }
  .input1 {
    width: 600px;
    height: 52px;
    margin: 12px 0;
    border-radius: 10px;
    font-size: 18px;
    box-shadow: 0 2px 8px rgba(30, 80, 200, 0.08);
  }
  .content {
    width: 1280px;
    margin: 0 auto 40px;
    padding: 16px 80px 20px 80px;
    border-radius: 20px;
    box-shadow: 0 4px 24px 0 rgba(137, 141, 145, 0.1);
    font-size: 18px;
  }
  .list_item {
    display: flex;
    align-items: flex-start;
    padding: 36px 0;
    border-bottom: 2px dashed rgba(0, 0, 0, 0.1);
    transition: background 0.2s;
  }
  .list_item:hover {
    background: #f3f8ff;
  }
  .list_item img {
    width: 28px;
    margin-top: 2px;
  }
  .list_item_content {
    font-size: 20px;
    margin-left: 12px;
    cursor: pointer;
    transition: color 0.2s;
  }
  .list_item_content:hover {
    color: #1b78f7;
    text-decoration: underline;
  }
  .list_item_time {
    margin-left: auto;
    color: #888;
    font-size: 19px;
    font-family: "Consolas", monospace;
    letter-spacing: 1px;
  }
  .container {
    width: fit-content;
    margin: 32px auto 0 auto;
  }
}

@media (max-width: 700px) {
  html,
  body,
  .body1,
  .content,
  .container {
    width: 100% !important;
    max-width: 100vw !important;
    overflow-x: hidden !important;
    box-sizing: border-box !important;
  }
  .list_item_time {
    max-width: 100%;
    overflow-x: auto;
    box-sizing: border-box;
  }
  .head {
    padding: 24px 0 16px 0;
    margin-bottom: 16px;
  }
  .title {
    font-size: 1.4rem;
    margin: 6px 0;
  }
  .input1 {
    width: 96%;
    min-width: 0;
    max-width: 100%;
    height: 40px;
    font-size: 15px;
    margin: 8px 0;
  }
  .content {
    width: 100%;
    max-width: 100%;
    margin: 0 0 16px 0;
    padding: 8px 4vw 10px 4vw;
    border-radius: 0;
    box-shadow: none;
  }
  .list_item {
    display: block;
    width: 100%;
    padding: 16px 0;
    font-size: 15px;
  }
  .list_item img {
    width: 20px;
  }
  .list_item_content {
    display: block;
    font-size: 15px;
    margin-left: 6px;
    word-break: break-all;
    text-align: left;
    white-space: normal;
    width: 100%;
  }
  .list_item_time {
    display: block;
    margin: 4px 0 0 0;
    font-size: 18px !important;
    color: #888;
    text-align: right;
    padding: 0 4px;
    background: none;
    min-width: 0;
    max-width: 100%;
    white-space: nowrap;
    overflow-x: auto;
  }
  .container {
    width: 100%;
    margin: 12px 0 0 0;
    padding: 0 2vw;
  }
}
</style>
