<template>
  <div class="ibody">
    <div class="box">
      <div class="infrom left">
        <div class="header">
          <h2>大赛通知</h2>
          <a href="javascript:void(0)" class="more" @click="onClickMore1"
            >查看更多</a
          >
        </div>
        <ul class="notice-list">
          <li v-for="(item, index) in leftList" :key="index">
            <span class="title" @click="onClickInform1(item.id)">{{
              item.title
            }}</span>
            <span class="date">{{ item.uploadTime }}</span>
          </li>
        </ul>
      </div>

      <div class="infrom right">
        <div class="header">
          <h2>要闻速递</h2>
          <a href="javascript:void(0)" class="more" @click="onClickMore2"
            >查看更多</a
          >
        </div>
        <ul class="notice-list">
          <li v-for="(item, index) in rightList" :key="index">
            <span class="title" @click="onClickInform2(item.id)">{{
              item.title
            }}</span>
            <span class="date">{{ item.uploadTime }}</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getInform } from "@/apis/inform.js";
import { getPrize } from "@/apis/inform.js";
import { ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
const router = useRouter();

const leftList = ref([]);
const rightList = ref([]);

const fetchInformList = async (targetListRef, title = "") => {
  const res = await getInform(title, 1, 5);
  if (res.data?.records) {
    targetListRef.value = res.data.records;
  } else {
    ElMessage.error(res.data?.msg || "获取通知信息失败");
  }
};
const fetchPrizeList = async (targetListRef, title = "") => {
  const res = await getPrize(title, 1, 5);
  if (res.data?.records) {
    targetListRef.value = res.data.records;
  } else {
    ElMessage.error(res.data?.msg || "获取获奖信息失败");
  }
};

onMounted(() => {
  fetchInformList(leftList);
  fetchPrizeList(rightList);
});

const onClickMore1 = () => {
  router.push({
    path: "/inform",
    query: { Type: 1 },
  });
};

const onClickMore2 = () => {
  router.push({
    path: "/inform",
    query: { Type: 2 },
  });
};

const onClickInform1 = (id) => {
  router.push({
    path: "/informContent",
    query: { id: id, Type: 1 },
  });
};

const onClickInform2 = (id) => {
  router.push({
    path: "/informContent",
    query: { id: id, Type: 2 },
  });
};
</script>

<style scoped>
.ibody {
  display: flex;
  justify-content: center;
  padding: 40px 0;
  width: 100vw;
  /* background-color: #f9f9f9; */
}

.box {
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 1200px;
  padding: 0 2vw;
  gap: 2vw;
  cursor: pointer;
}

.infrom {
  flex: 1;
  /* background-color: #fff; */
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  padding: 20px;
  box-sizing: border-box;
}

html.dark .infrom {
  background-color: #444343;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 2px solid #eee;
  padding-bottom: 10px;
  margin-bottom: 15px;
}

.header h2 {
  font-size: 20px;
  font-weight: bold;
  margin: 0;
}

.header .more {
  font-size: 14px;
  color: #409eff;
  text-decoration: none;
  cursor: pointer;
}

.header .more:hover {
  text-decoration: underline;
}

.notice-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.notice-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid transparent;
  font-size: 15px;
  /* color: #333; */
  transition: background-color 0.2s;
}

.notice-list li:hover {
  background-color: #f5f7fa;
}

html.dark .notice-list li:hover {
  background-color: #333;
}

.notice-list .title {
  flex: 1;
  margin-right: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.notice-list .date {
  flex-shrink: 0;
  color: #999;
  font-size: 13px;
}

@media (max-width: 768px) {
  .box {
    flex-direction: column;
    gap: 20px;
    padding: 0 16px;
  }

  .infrom {
    width: 100%;
  }

  .header h2 {
    font-size: 18px;
  }

  .notice-list li {
    font-size: 14px;
    padding: 10px 0;
  }

  .notice-list .date {
    font-size: 12px;
  }
}
</style>
