<template>
  <div class="dashboard">
    <!-- 顶部欢迎区 -->
    <div class="dashboard-header">
      <div class="dashboard-title">欢迎回来，管理员！</div>
      <div class="dashboard-desc">今天是 {{ today }}</div>
    </div>

    <!-- 统计卡片区 -->
    <div class="dashboard-cards">
      <div
        v-for="item in stats"
        :key="item.label"
        :class="['dashboard-card', item.color]"
      >
        <div class="card-icon">{{ item.icon }}</div>
        <div class="card-value">{{ item.value }}</div>
        <div class="card-label">{{ item.label }}</div>
      </div>
    </div>

    <!-- 主体三列区 -->
    <div class="dashboard-main">
      <!-- 左列：近期竞赛 + 图表区 -->
      <div class="dashboard-col-left">
        <div class="dashboard-section">
          <div class="dashboard-section-title">近期竞赛</div>
          <table class="dashboard-table">
            <thead>
              <tr>
                <th>竞赛名称</th>
                <th>截止时间</th>
                <th>参赛人数</th>
                <th>状态</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in recentCompetitions" :key="item.id">
                <td>{{ item.name }}</td>
                <td>{{ item.end }}</td>
                <td>{{ item.works }}</td>
                <td>
                  <span :class="['tag', getStatusClass(item.status)]">{{
                    item.status
                  }}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="dashboard-section dashboard-charts dashboard-charts-grid">
          <div class="dashboard-chart">
            <div class="dashboard-section-title">比赛状态统计</div>
            <v-chart
              :option="matchStatusOption"
              autoresize
              style="height: 220px"
            />
          </div>
          <div class="dashboard-chart">
            <div class="dashboard-section-title">每日增长趋势</div>
            <v-chart
              :option="dailyGrowthOption"
              autoresize
              style="height: 220px"
            />
          </div>
          <div class="dashboard-chart">
            <div class="dashboard-section-title">竞赛分类数量</div>
            <v-chart
              :option="competitionCategoryOption"
              autoresize
              style="height: 220px"
            />
          </div>
          <div class="dashboard-chart">
            <div class="dashboard-section-title">作品分数分布</div>
            <v-chart
              :option="scoreDistOption"
              autoresize
              style="height: 220px"
            />
          </div>
        </div>
      </div>
      <!-- 右列：常用入口、公告、截止日期 -->
      <div class="dashboard-col-right">
        <div class="dashboard-section">
          <div class="dashboard-section-title">常用系统入口</div>
          <div class="dashboard-quick-links">
            <div
              v-for="item in quickLinks"
              :key="item.label"
              class="quick-link-card"
              @click="goTo(item.path)"
            >
              <span class="quick-link-icon">{{ item.icon }}</span>
              <span class="quick-link-label">{{ item.label }}</span>
            </div>
          </div>
        </div>
        <div class="dashboard-section">
          <div class="dashboard-section-title">近期动态 / 公告</div>
          <ul class="news-list">
            <li v-for="item in newsList" :key="item.id">
              <span class="news-type">[{{ item.type }}]</span> {{ item.title }}
              <span class="news-date">({{ item.date }})</span>
            </li>
          </ul>
        </div>
        <div class="dashboard-section">
          <div class="dashboard-section-title">即将到来的截止日期</div>
          <div class="deadline-list">
            <div
              v-for="item in deadlineList"
              :key="item.id"
              class="deadline-item"
            >
              <div class="deadline-icon" :style="{ color: item.color }">
                {{ item.icon }}
              </div>
              <div class="deadline-info">
                <div class="deadline-name">{{ item.name }}</div>
                <div class="deadline-date">
                  {{ item.descLabel }}：{{ item.date }}
                </div>
                <div class="deadline-progress-outer">
                  <div
                    class="deadline-progress-inner"
                    :style="{
                      width: item.progress + '%',
                      background: item.color,
                    }"
                  ></div>
                </div>
                <div class="deadline-remaining">
                  剩余时间：{{ item.remaining }}天
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import VChart from "vue-echarts";
import {
  getWithLike,
  getCompetitionStatusCount,
  getUserTeamWorkTrend,
  getCompetitionCategoryCount,
  getAwardCount,
} from "@/apis/admin.js";
import { getNews } from "@/apis/admin/admin-news.js";
import { getWithLike as getInformList } from "@/apis/admin/admin-inform.js";
import axios from "axios";

const today = new Date().toLocaleDateString();

const stats = ref([
  { label: "用户总数", value: 0, icon: "👤", color: "card-blue" },
  { label: "队伍总数", value: 0, icon: "👥", color: "card-green" },
  { label: "作品总数", value: 0, icon: "🖼️", color: "card-cyan" },
  { label: "新闻总数", value: 0, icon: "📰", color: "card-orange" },
  { label: "通告总数", value: 0, icon: "📢", color: "card-purple" },
  { label: "进行中竞赛", value: 0, icon: "🏆", color: "card-pink" },
]);

const fetchStats = async () => {
  try {
    const jwt = localStorage.getItem("jwt");
    const response = await axios.get("/api/admin/work/count", {
      headers: { Authentication: jwt },
    });
    if (
      response.data &&
      (response.data.code === 0 || response.data.code === 1)
    ) {
      const d = response.data.data || {};
      stats.value = [
        {
          label: "用户总数",
          value: d.userCount ?? 0,
          icon: "👤",
          color: "card-blue",
        },
        {
          label: "队伍总数",
          value: d.teamCount ?? 0,
          icon: "👥",
          color: "card-green",
        },
        {
          label: "作品总数",
          value: d.workCount ?? 0,
          icon: "🖼️",
          color: "card-cyan",
        },
        {
          label: "新闻总数",
          value: d.newsCount ?? 0,
          icon: "📰",
          color: "card-orange",
        },
        {
          label: "通告总数",
          value: d.annunciationCount ?? 0,
          icon: "📢",
          color: "card-purple",
        },
        {
          label: "进行中竞赛",
          value: d.competitionCount ?? 0,
          icon: "🏆",
          color: "card-pink",
        },
      ];
    }
  } catch (e) {
    console.error("获取统计数据失败", e);
  }
};

const deadlineList = ref([]);

const quickLinks = [
  { label: "用户管理", path: "/admin/users", icon: "👤" },
  { label: "队伍管理", path: "/admin/team", icon: "👥" },
  { label: "新闻管理", path: "/admin/news", icon: "📰" },
  { label: "通告管理", path: "/admin/inform", icon: "📢" },
  { label: "竞赛管理", path: "/admin/competition", icon: "🏆" },
  { label: "回到首页", path: "/index", icon: "🏠" },
];
const router = useRouter();
const goTo = (path) => router.push(path);

// 获取近期竞赛数据（显示前5个）
const fetchRecentCompetitions = async () => {
  try {
    // 检查是否已登录
    const jwt = localStorage.getItem("jwt");
    if (!jwt) {
      console.log("用户未登录，使用默认数据");
      recentCompetitions.value = [
        {
          id: 1,
          name: "2025海报设计大赛",
          end: "2025-07-30",
          works: 156,
          status: "报名中",
        },
        {
          id: 2,
          name: "科技创新海报设计",
          end: "2025-08-15",
          works: 89,
          status: "未开始",
        },
        {
          id: 3,
          name: "环保主题海报征集",
          end: "2025-08-20",
          works: 234,
          status: "报名中",
        },
        {
          id: 4,
          name: "校园文化节海报赛",
          end: "2025-09-10",
          works: 67,
          status: "未开始",
        },
        {
          id: 5,
          name: "数字艺术设计大赛",
          end: "2025-09-25",
          works: 123,
          status: "评审中",
        },
      ];
      return;
    }

    // 获取所有状态的竞赛
    const allCompetitions = [];

    // 获取未开始的竞赛
    try {
      const response0 = await getWithLike({
        competitionName: "",
        competitionStatus: "0",
      });
      if (response0 && response0.data) {
        allCompetitions.push(...response0.data);
      }
    } catch (error) {
      console.log("获取未开始竞赛失败:", error);
    }

    // 获取报名中的竞赛
    try {
      const response1 = await getWithLike({
        competitionName: "",
        competitionStatus: "1",
      });
      if (response1 && response1.data) {
        allCompetitions.push(...response1.data);
      }
    } catch (error) {
      console.log("获取报名中竞赛失败:", error);
    }

    // 获取评审中的竞赛
    try {
      const response2 = await getWithLike({
        competitionName: "",
        competitionStatus: "2",
      });
      if (response2 && response2.data) {
        allCompetitions.push(...response2.data);
      }
    } catch (error) {
      console.log("获取评审中竞赛失败:", error);
    }

    // 不获取已结束的竞赛，只显示未开始、报名中、评审中的竞赛

    console.log("所有竞赛数据:", allCompetitions);

    if (allCompetitions && allCompetitions.length > 0) {
      // 按截止时间排序，截止时间早的排前面
      const sortedCompetitions = allCompetitions
        .filter((item) => item.activityEnd) // 过滤掉没有截止时间的竞赛
        .sort((a, b) => {
          const dateA = new Date(a.activityEnd);
          const dateB = new Date(b.activityEnd);
          return dateA - dateB; // 升序排列，早的在前
        });

      // 取前5个竞赛
      const competitions = sortedCompetitions.slice(0, 5).map((item) => {
        console.log("竞赛数据项:", item); // 调试日志
        return {
          id: item.id,
          name: item.name || item.competitionName,
          end: item.activityEnd
            ? new Date(item.activityEnd).toLocaleDateString()
            : "未设置",
          works:
            item.competitionPeople ||
            item.teamCount ||
            item.participantCount ||
            item.people ||
            0,
          status: getCompetitionStatus(item.competitionStatus),
        };
      });
      recentCompetitions.value = competitions;

      // 计算截止日期
      const deadlines = calculateDeadlines(sortedCompetitions);
      deadlineList.value = deadlines;
      console.log("计算的截止日期:", deadlines);
    } else {
      console.log("没有获取到竞赛数据，使用默认数据");
      // 使用默认数据
      recentCompetitions.value = [
        {
          id: 1,
          name: "2025海报设计大赛",
          end: "2025-07-30",
          works: 48,
          status: "报名中",
        },
        {
          id: 2,
          name: "科技创新海报设计",
          end: "2025-08-15",
          works: 32,
          status: "未开始",
        },
      ];

      // 使用默认截止日期数据
      deadlineList.value = [
        {
          id: 1,
          name: "2025海报设计大赛",
          descLabel: "作品提交截止时间",
          date: "2025-07-30",
          remaining: 7,
          icon: "📅",
          color: "#ff9800",
          progress: 80,
        },
        {
          id: 2,
          name: "科技创新海报设计",
          descLabel: "比赛开始时间",
          date: "2025-08-01",
          remaining: 15,
          icon: "🚀",
          color: "#43a047",
          progress: 50,
        },
        {
          id: 3,
          name: "环保主题海报征集",
          descLabel: "报名截止时间",
          date: "2025-08-15",
          remaining: 28,
          icon: "📝",
          color: "#1976d2",
          progress: 60,
        },
      ];
    }
  } catch (error) {
    console.error("获取竞赛数据失败:", error);
    console.error("错误详情:", error.response || error.message || error);
    // 如果API调用失败，使用默认数据
    recentCompetitions.value = [
      {
        id: 1,
        name: "2025海报设计大赛",
        end: "2025-07-30",
        works: 156,
        status: "报名中",
      },
      {
        id: 2,
        name: "科技创新海报设计",
        end: "2025-08-15",
        works: 89,
        status: "未开始",
      },
      {
        id: 3,
        name: "环保主题海报征集",
        end: "2025-08-20",
        works: 234,
        status: "报名中",
      },
      {
        id: 4,
        name: "校园文化节海报赛",
        end: "2025-09-10",
        works: 67,
        status: "未开始",
      },
      {
        id: 5,
        name: "数字艺术设计大赛",
        end: "2025-09-25",
        works: 123,
        status: "评审中",
      },
    ];

    // 使用默认截止日期数据
    deadlineList.value = [
      {
        id: 1,
        name: "2025海报设计大赛",
        descLabel: "作品提交截止时间",
        date: "2025-07-30",
        remaining: 7,
        icon: "📅",
        color: "#ff9800",
        progress: 80,
      },
      {
        id: 2,
        name: "科技创新海报设计",
        descLabel: "报名截止时间",
        date: "2025-08-15",
        remaining: 28,
        icon: "📝",
        color: "#1976d2",
        progress: 60,
      },
    ];
  }
};

// 获取竞赛状态
const getCompetitionStatus = (status) => {
  switch (String(status)) {
    case "0":
      return "未开始";
    case "1":
      return "报名中";
    case "2":
      return "评审中";
    case "3":
      return "已结束";
    default:
      return "未知";
  }
};

// 获取状态样式类
const getStatusClass = (status) => {
  switch (status) {
    case "报名中":
      return "ongoing";
    case "评审中":
      return "reviewing";
    case "已结束":
      return "ended";
    case "未开始":
      return "pending";
    default:
      return "unknown";
  }
};

// 计算截止日期信息
const calculateDeadlines = (competitions) => {
  const today = new Date();
  const deadlines = [];

  console.log("计算截止日期 - 今天日期:", today.toLocaleDateString());

  competitions.forEach((competition) => {
    const name = competition.name || competition.competitionName;
    const status = competition.competitionStatus;

    console.log(`处理竞赛: ${name}, 状态: ${status}`);

    // 计算比赛开始时间（报名开始时间）
    if (competition.registrationBegin) {
      const registrationBegin = new Date(competition.registrationBegin);
      const daysToRegistrationBegin = Math.ceil(
        (registrationBegin - today) / (1000 * 60 * 60 * 24)
      );
      console.log(
        `  报名开始时间: ${competition.registrationBegin}, 剩余天数: ${daysToRegistrationBegin}`
      );
      if (daysToRegistrationBegin > 0 && daysToRegistrationBegin <= 30) {
        deadlines.push({
          id: `begin_${competition.id}`,
          name: name,
          descLabel: "比赛开始时间",
          date: registrationBegin.toLocaleDateString(),
          remaining: daysToRegistrationBegin,
          icon: "🚀",
          color: "#43a047",
          progress: Math.max(
            0,
            Math.min(100, ((30 - daysToRegistrationBegin) / 30) * 100)
          ),
        });
      }
    }
    // 计算报名截止时间
    if (competition.registrationEnd) {
      const registrationEnd = new Date(competition.registrationEnd);
      const daysToRegistrationEnd = Math.ceil(
        (registrationEnd - today) / (1000 * 60 * 60 * 24)
      );
      console.log(
        `  报名截止时间: ${competition.registrationEnd}, 剩余天数: ${daysToRegistrationEnd}`
      );
      if (daysToRegistrationEnd > 0 && daysToRegistrationEnd <= 30) {
        deadlines.push({
          id: `reg_${competition.id}`,
          name: name,
          descLabel: "报名截止时间",
          date: registrationEnd.toLocaleDateString(),
          remaining: daysToRegistrationEnd,
          icon: "📝",
          color: "#1976d2",
          progress: Math.max(
            0,
            Math.min(100, ((30 - daysToRegistrationEnd) / 30) * 100)
          ),
        });
      }
    }
    // 只在评审中状态下显示评审结束时间（活动结束时间）
    if ((status === "2" || status === 2) && competition.activityEnd) {
      const activityEnd = new Date(competition.activityEnd);
      const daysToActivityEnd = Math.ceil(
        (activityEnd - today) / (1000 * 60 * 60 * 24)
      );
      console.log(
        `  评审结束时间: ${
          competition.activityEnd
        }, 剩余天数: ${daysToActivityEnd}, 状态检查: ${
          status === "2" || status === 2
        }`
      );
      if (daysToActivityEnd > 0 && daysToActivityEnd <= 30) {
        deadlines.push({
          id: `judge_${competition.id}`,
          name: name,
          descLabel: "评审结束时间",
          date: activityEnd.toLocaleDateString(),
          remaining: daysToActivityEnd,
          icon: "⚖️",
          color: "#e6a23c",
          progress: Math.max(
            0,
            Math.min(100, ((30 - daysToActivityEnd) / 30) * 100)
          ),
        });
      } else {
        console.log(`    评审结束时间不在30天内，跳过显示`);
      }
    } else {
      console.log(
        `    不显示评审结束时间 - 状态: ${status} (类型: ${typeof status}), 活动结束时间: ${
          competition.activityEnd
        }`
      );
    }
  });
  // 按剩余天数排序，紧急的排在前面
  deadlines.sort((a, b) => a.remaining - b.remaining);
  console.log("最终计算的截止日期列表:", deadlines);
  return deadlines.slice(0, 5); // 取前5个最紧急的
};

// 获取新闻和通告数据
const fetchNewsAndInform = async () => {
  try {
    const allNews = [];

    // 检查是否已登录
    const jwt = localStorage.getItem("jwt");
    if (!jwt) {
      console.log("用户未登录，使用默认新闻数据");
      newsList.value = [
        {
          id: 1,
          type: "新闻",
          title: "2025全国大学生绿色校园海报设计大赛圆满落幕",
          date: "2025-07-08",
        },
        {
          id: 2,
          type: "通告",
          title: "关于暑期系统维护的通知",
          date: "2025-07-06",
        },
        {
          id: 3,
          type: "新闻",
          title: "ICPC海报设计竞赛获奖名单公布",
          date: "2025-07-04",
        },
        {
          id: 4,
          type: "通告",
          title: "新功能上线：作品管理模块",
          date: "2025-07-02",
        },
      ];
      return;
    }

    // 计算日期范围：今天前后半个月
    const today = new Date();
    const halfMonthAgo = new Date(today.getTime() - 15 * 24 * 60 * 60 * 1000);
    const halfMonthLater = new Date(today.getTime() + 15 * 24 * 60 * 60 * 1000);

    // 格式化日期为 YYYY-MM-DD 格式
    const formatDate = (date) => {
      return date.toISOString().split("T")[0];
    };

    const startDate = formatDate(halfMonthAgo);
    const endDate = formatDate(halfMonthLater);

    console.log("日期范围:", startDate, "到", endDate);

    // 获取新闻数据
    try {
      console.log("开始获取新闻数据...");
      const newsResponse = await getNews({
        pageNo: 1,
        pageSize: 50, // 增加页面大小以获取更多数据
        title: "",
        startDate: startDate,
        endDate: endDate,
      });
      console.log("新闻数据响应:", newsResponse);
      if (
        newsResponse &&
        newsResponse.data &&
        newsResponse.data.records &&
        Array.isArray(newsResponse.data.records)
      ) {
        // 过滤日期范围内的新闻
        const filteredNews = newsResponse.data.records.filter((item) => {
          if (!item.uploadTime) return false;
          const uploadDate = new Date(item.uploadTime);
          return uploadDate >= halfMonthAgo && uploadDate <= halfMonthLater;
        });

        const news = filteredNews.map((item) => ({
          id: item.id,
          type: "新闻",
          title: item.title || item.newsTitle,
          date: item.uploadTime
            ? new Date(item.uploadTime).toLocaleDateString()
            : "未知",
          timestamp: item.uploadTime ? new Date(item.uploadTime).getTime() : 0,
        }));
        allNews.push(...news);
        console.log("处理后的新闻数据:", news);
      }
    } catch (error) {
      console.log("获取新闻数据失败:", error);
    }

    // 获取通告数据
    try {
      console.log("开始获取通告数据...");
      const informResponse = await getInformList({
        id: null,
        title: null,
        pageNo: 1,
        pageSize: 50, // 增加页面大小以获取更多数据
      });
      console.log("通告数据响应:", informResponse);
      if (
        informResponse &&
        informResponse.data &&
        informResponse.data.records &&
        Array.isArray(informResponse.data.records)
      ) {
        // 过滤日期范围内的通告
        const filteredInforms = informResponse.data.records.filter((item) => {
          if (!item.uploadTime) return false;
          const uploadDate = new Date(item.uploadTime);
          return uploadDate >= halfMonthAgo && uploadDate <= halfMonthLater;
        });

        const informs = filteredInforms.map((item) => ({
          id: item.id,
          type: "通告",
          title: item.title || item.informTitle,
          date: item.uploadTime
            ? new Date(item.uploadTime).toLocaleDateString()
            : "未知",
          timestamp: item.uploadTime ? new Date(item.uploadTime).getTime() : 0,
        }));
        allNews.push(...informs);
        console.log("处理后的通告数据:", informs);
      }
    } catch (error) {
      console.log("获取通告数据失败:", error);
    }

    console.log("合并后的所有数据:", allNews);

    // 按时间戳排序，最新的在前面
    allNews.sort((a, b) => {
      return b.timestamp - a.timestamp;
    });

    console.log("排序后的数据:", allNews);

    // 取前8个
    newsList.value = allNews.slice(0, 8).map((item) => ({
      id: item.id,
      type: item.type,
      title: item.title,
      date: item.date,
    }));
    console.log("最终设置的新闻列表:", newsList.value);
  } catch (error) {
    console.error("获取新闻和通告数据失败:", error);
    // 使用默认数据
    newsList.value = [
      {
        id: 1,
        type: "新闻",
        title: "2025全国大学生绿色校园海报设计大赛圆满落幕",
        date: "2025-07-08",
      },
      {
        id: 2,
        type: "通告",
        title: "关于暑期系统维护的通知",
        date: "2025-07-06",
      },
      {
        id: 3,
        type: "新闻",
        title: "ICPC海报设计竞赛获奖名单公布",
        date: "2025-07-04",
      },
      {
        id: 4,
        type: "通告",
        title: "新功能上线：作品管理模块",
        date: "2025-07-02",
      },
    ];
  }
};

// 测试API调用
const testAPI = async () => {
  try {
    console.log("=== 开始测试API调用 ===");

    // 直接使用axios调用，避免被响应拦截器过滤
    const response = await axios.get(
      "/api/admin/competition/getCompetitionStatusCount",
      {
        headers: {
          Authentication: localStorage.getItem("jwt"),
        },
      }
    );

    console.log("API测试原始响应:", response);
    console.log("响应数据:", response.data);
    console.log("响应码:", response.data.code);
    console.log("响应数据内容:", response.data.data);
    console.log("=== API测试完成 ===");
  } catch (error) {
    console.error("API测试失败:", error);
    console.error("错误详情:", error.response?.data || error.message);
  }
};

// 获取比赛状态统计数据
const fetchCompetitionStatusCount = async () => {
  console.log("=== 开始获取比赛状态统计数据 ===");

  try {
    const jwt = localStorage.getItem("jwt");
    if (!jwt) {
      console.log("用户未登录，使用默认比赛状态数据");
      matchStatusStats.value = [
        { label: "未开始", value: 4, color: "#909399" },
        { label: "报名中", value: 7, color: "#409eff" },
        { label: "评审中", value: 3, color: "#e6a23c" },
        { label: "已结束", value: 9, color: "#67c23a" },
      ];
      return;
    }

    console.log("用户已登录，开始调用API...");

    // 直接使用axios调用，避免被响应拦截器过滤
    const response = await axios.get(
      "/api/admin/competition/getCompetitionStatusCount",
      {
        headers: {
          Authentication: jwt,
        },
      }
    );

    console.log("API原始响应:", response);
    const responseData = response.data;
    console.log("API响应数据:", responseData);
    console.log("响应码:", responseData.code);
    console.log("响应数据内容:", responseData.data);

    // 检查响应格式 - 修改为接受code为1的情况
    if (responseData && (responseData.code === 0 || responseData.code === 1)) {
      console.log("API调用成功，响应码:", responseData.code);

      if (
        responseData.data &&
        Array.isArray(responseData.data) &&
        responseData.data.length > 0
      ) {
        console.log("API返回的数据数组:", responseData.data);

        // 处理返回的数据，将label按"-"分割成多个状态
        const processedData = [];
        responseData.data.forEach((item, index) => {
          console.log(`处理第${index + 1}项数据:`, item);

          if (item.label && item.value !== undefined) {
            console.log("数据项有效，开始分割处理");

            // 检查数据格式
            console.log("原始label:", item.label);
            console.log("原始value:", item.value);
            console.log("原始color:", item.color);

            const labels = item.label.split(" - ");
            const values = item.value.toString().split(" - ");
            const colors = item.color ? item.color.split(" - ") : [];

            console.log("分割后的数据:", { labels, values, colors });

            // 如果分割后只有一个值，说明数据格式可能是单个状态
            if (labels.length === 1 && values.length === 1) {
              console.log("检测到单个状态数据格式");
              processedData.push({
                label: labels[0].trim(),
                value: parseInt(values[0]) || 0,
                color: colors[0] || "#909399",
              });
            } else {
              // 多个状态的处理
              labels.forEach((label, labelIndex) => {
                const value = parseInt(values[labelIndex] || 0);
                const color = colors[labelIndex] || "#909399";

                processedData.push({
                  label: label.trim(),
                  value: value,
                  color: color,
                });

                console.log(
                  `添加数据项: ${label.trim()} = ${value}, 颜色: ${color}`
                );
              });
            }
          } else {
            console.log("数据项无效，跳过处理");
          }
        });

        if (processedData.length > 0) {
          console.log("最终处理的数据:", processedData);
          matchStatusStats.value = processedData;
        } else {
          console.log("处理后没有有效数据，使用默认数据");
          setDefaultMatchStatusData();
        }
      } else {
        console.log("API返回的数据为空或格式不正确");
        console.log("responseData.data:", responseData.data);
        console.log("使用默认数据");
        setDefaultMatchStatusData();
      }
    } else {
      console.log("API响应码不正确:", responseData?.code);
      console.log("完整响应:", responseData);
      setDefaultMatchStatusData();
    }
  } catch (error) {
    console.error("获取比赛状态统计失败:", error);
    console.error("错误详情:", error.response?.data || error.message);
    setDefaultMatchStatusData();
  }

  console.log("=== 比赛状态数据获取完成 ===");
  console.log("当前数据:", matchStatusStats.value);
};

// 设置默认的比赛状态数据
const setDefaultMatchStatusData = () => {
  console.log("设置默认比赛状态数据");
  matchStatusStats.value = [
    { label: "未开始", value: 4, color: "#909399" },
    { label: "报名中", value: 7, color: "#409eff" },
    { label: "评审中", value: 3, color: "#e6a23c" },
    { label: "已结束", value: 9, color: "#67c23a" },
  ];
};

// 获取每日用户、队伍、作品增长趋势数据
const fetchDailyGrowthTrend = async () => {
  console.log("=== 开始获取每日增长趋势数据 ===");
  try {
    const jwt = localStorage.getItem("jwt");
    if (!jwt) {
      console.log("用户未登录，使用默认增长趋势数据");
      dailyGrowth.value = [
        { date: "7-2", user: 3, team: 1, work: 2 },
        { date: "7-3", user: 5, team: 2, work: 3 },
        { date: "7-4", user: 4, team: 3, work: 2 },
        { date: "7-5", user: 6, team: 2, work: 4 },
        { date: "7-6", user: 7, team: 4, work: 5 },
        { date: "7-7", user: 8, team: 3, work: 6 },
        { date: "7-8", user: 5, team: 2, work: 3 },
      ];
      return;
    }

    console.log("用户已登录，开始调用每日增长趋势API...");

    // 直接使用axios调用，避免被响应拦截器过滤
    const response = await axios.get("/api/admin/work/getUserTeamWorkTrend", {
      headers: {
        Authentication: jwt,
      },
    });

    console.log("每日增长趋势API原始响应:", response);
    const responseData = response.data;
    console.log("每日增长趋势API响应数据:", responseData);
    console.log("响应码:", responseData.code);
    console.log("响应数据内容:", responseData.data);

    // 检查响应格式 - 接受code为0或1的情况
    if (responseData && (responseData.code === 0 || responseData.code === 1)) {
      console.log("每日增长趋势API调用成功，响应码:", responseData.code);

      if (
        responseData.data &&
        Array.isArray(responseData.data) &&
        responseData.data.length > 0
      ) {
        console.log("API返回的每日增长趋势数据数组:", responseData.data);

        // 处理返回的数据
        const processedData = responseData.data.map((item, index) => {
          console.log(`处理第${index + 1}项增长趋势数据:`, item);

          return {
            date: item.time || item.date || `day-${index + 1}`,
            user: parseInt(item.user) || 0,
            team: parseInt(item.team) || 0,
            work: parseInt(item.work) || 0,
          };
        });

        if (processedData.length > 0) {
          console.log("最终处理的每日增长趋势数据:", processedData);
          dailyGrowth.value = processedData;
        } else {
          console.log("处理后没有有效数据，使用默认数据");
          setDefaultDailyGrowthData();
        }
      } else {
        console.log("API返回的每日增长趋势数据为空或格式不正确");
        console.log("responseData.data:", responseData.data);
        console.log("使用默认数据");
        setDefaultDailyGrowthData();
      }
    } else {
      console.log("每日增长趋势API响应码不正确:", responseData?.code);
      console.log("完整响应:", responseData);
      setDefaultDailyGrowthData();
    }
  } catch (error) {
    console.error("获取每日增长趋势失败:", error);
    console.error("错误详情:", error.response?.data || error.message);
    setDefaultDailyGrowthData();
  }
  console.log("=== 每日增长趋势数据获取完成 ===");
  console.log("当前数据:", dailyGrowth.value);
};

// 设置默认的每日增长趋势数据
const setDefaultDailyGrowthData = () => {
  console.log("设置默认每日增长趋势数据");
  dailyGrowth.value = [
    { date: "7-2", user: 3, team: 1, work: 2 },
    { date: "7-3", user: 5, team: 2, work: 3 },
    { date: "7-4", user: 4, team: 3, work: 2 },
    { date: "7-5", user: 6, team: 2, work: 4 },
    { date: "7-6", user: 7, team: 4, work: 5 },
    { date: "7-7", user: 8, team: 3, work: 6 },
    { date: "7-8", user: 5, team: 2, work: 3 },
  ];
};

// 获取竞赛分类数量数据
const fetchCompetitionCategoryCount = async () => {
  console.log("=== 开始获取竞赛分类数量数据 ===");

  try {
    // 检查是否已登录
    const jwt = localStorage.getItem("jwt");
    if (!jwt) {
      console.log("用户未登录，使用默认数据");
      setDefaultCompetitionCategoryData();
      return;
    }

    console.log("用户已登录，开始调用竞赛分类数量API...");

    // 直接使用axios调用，避免被响应拦截器过滤
    const response = await axios.get(
      "/api/admin/competition/getCompetitionCategoryCount",
      {
        headers: {
          Authentication: jwt,
        },
      }
    );

    console.log("竞赛分类数量API原始响应:", response);
    const responseData = response.data;
    console.log("竞赛分类数量API响应数据:", responseData);
    console.log("响应码:", responseData.code);
    console.log("响应数据内容:", responseData.data);

    // 检查响应格式 - 接受code为0或1的情况
    if (responseData && (responseData.code === 0 || responseData.code === 1)) {
      console.log("竞赛分类数量API调用成功，响应码:", responseData.code);

      if (
        responseData.data &&
        Array.isArray(responseData.data) &&
        responseData.data.length > 0
      ) {
        console.log("API返回的竞赛分类数量数据数组:", responseData.data);

        // 处理返回的数据
        const processedData = responseData.data.map((item, index) => {
          console.log(`处理第${index + 1}项竞赛分类数据:`, item);

          return {
            category: item.category || `分类${index + 1}`,
            count: parseInt(item.count) || 0,
            color: item.color || getDefaultColor(index),
          };
        });

        if (processedData.length > 0) {
          console.log("最终处理的竞赛分类数量数据:", processedData);
          competitionCategories.value = processedData;
        } else {
          console.log("处理后没有有效数据，使用默认数据");
          setDefaultCompetitionCategoryData();
        }
      } else {
        console.log("API返回的竞赛分类数量数据为空或格式不正确");
        console.log("responseData.data:", responseData.data);
        console.log("使用默认数据");
        setDefaultCompetitionCategoryData();
      }
    } else {
      console.log("竞赛分类数量API响应码不正确:", responseData?.code);
      console.log("完整响应:", responseData);
      setDefaultCompetitionCategoryData();
    }
  } catch (error) {
    console.error("获取竞赛分类数量失败:", error);
    console.error("错误详情:", error.response?.data || error.message);
    setDefaultCompetitionCategoryData();
  }
  console.log("=== 竞赛分类数量数据获取完成 ===");
  console.log("当前数据:", competitionCategories.value);
};

// 设置默认的竞赛分类数量数据
const setDefaultCompetitionCategoryData = () => {
  console.log("设置默认竞赛分类数量数据");
  competitionCategories.value = [
    { category: "海报设计", count: 15, color: "#67c23a" },
    { category: "平面设计", count: 12, color: "#409eff" },
    { category: "UI设计", count: 8, color: "#e6a23c" },
    { category: "插画设计", count: 6, color: "#f56c6c" },
    { category: "品牌设计", count: 4, color: "#909399" },
  ];
};

// 获取默认颜色的辅助函数
const getDefaultColor = (index) => {
  const colors = [
    "#409eff",
    "#67c23a",
    "#e6a23c",
    "#f56c6c",
    "#909399",
    "#9c27b0",
    "#ff9800",
    "#795548",
  ];
  return colors[index % colors.length];
};

// 获取奖项数量数据
const fetchAwardCount = async () => {
  console.log("=== 开始获取奖项数量数据 ===");

  try {
    // 检查是否已登录
    const jwt = localStorage.getItem("jwt");
    if (!jwt) {
      console.log("用户未登录，使用默认数据");
      setDefaultAwardData();
      return;
    }

    console.log("用户已登录，开始调用奖项数量API...");

    // 直接使用axios调用，避免被响应拦截器过滤
    const response = await axios.get("/api/admin/award/getAwardCount", {
      headers: {
        Authentication: jwt,
      },
    });

    console.log("奖项数量API原始响应:", response);
    const responseData = response.data;
    console.log("奖项数量API响应数据:", responseData);
    console.log("响应码:", responseData.code);
    console.log("响应数据内容:", responseData.data);

    // 检查响应格式 - 接受code为0或1的情况
    if (responseData && (responseData.code === 0 || responseData.code === 1)) {
      console.log("奖项数量API调用成功，响应码:", responseData.code);

      if (
        responseData.data &&
        Array.isArray(responseData.data) &&
        responseData.data.length > 0
      ) {
        console.log("API返回的奖项数量数据数组:", responseData.data);

        // 处理返回的数据
        const processedData = responseData.data.map((item, index) => {
          console.log(`处理第${index + 1}项奖项数据:`, item);

          return {
            label: item.label || `奖项${index + 1}`,
            value: parseInt(item.value) || 0,
            color: item.color || getDefaultColor(index),
          };
        });

        if (processedData.length > 0) {
          console.log("最终处理的奖项数量数据:", processedData);
          scoreDist.value = processedData;
        } else {
          console.log("处理后没有有效数据，使用默认数据");
          setDefaultAwardData();
        }
      } else {
        console.log("API返回的奖项数量数据为空或格式不正确");
        console.log("responseData.data:", responseData.data);
        console.log("使用默认数据");
        setDefaultAwardData();
      }
    } else {
      console.log("奖项数量API响应码不正确:", responseData?.code);
      console.log("完整响应:", responseData);
      setDefaultAwardData();
    }
  } catch (error) {
    console.error("获取奖项数量失败:", error);
    console.error("错误详情:", error.response?.data || error.message);
    setDefaultAwardData();
  }
  console.log("=== 奖项数量数据获取完成 ===");
  console.log("当前数据:", scoreDist.value);
};

// 设置默认的奖项数量数据
const setDefaultAwardData = () => {
  console.log("设置默认奖项数量数据");
  scoreDist.value = [
    { label: "80-89分", value: 18, color: "#409eff" },
    { label: "90分以上", value: 12, color: "#409eff" },
    { label: "70-79分", value: 25, color: "#e6a23c" },
    { label: "60-69分", value: 8, color: "#f56c6c" },
    { label: "60分以下", value: 3, color: "#909399" },
  ];
};

// 组件挂载时获取数据
onMounted(() => {
  // 延迟一点时间确保用户信息已加载
  setTimeout(async () => {
    console.log("=== 开始加载Dashboard数据 ===");

    // 先测试API
    await testAPI();

    // 然后获取各种数据
    await fetchStats(); // 获取统计数据
    await fetchRecentCompetitions();
    await fetchNewsAndInform();
    await fetchCompetitionStatusCount();
    await fetchDailyGrowthTrend(); // 获取每日增长趋势数据
    await fetchCompetitionCategoryCount(); // 获取竞赛分类数量数据
    await fetchAwardCount(); // 获取奖项数量数据

    console.log("=== Dashboard数据加载完成 ===");
    console.log("最终比赛状态数据:", matchStatusStats.value);
  }, 500);
});

const recentCompetitions = ref([]);

const competitionCategories = ref([
  { category: "海报设计", count: 15, color: "#67c23a" },
  { category: "平面设计", count: 12, color: "#409eff" },
  { category: "UI设计", count: 8, color: "#e6a23c" },
  { category: "插画设计", count: 6, color: "#f56c6c" },
  { category: "品牌设计", count: 4, color: "#909399" },
]);
const scoreDist = ref([
  { label: "80-89分", value: 18, color: "#67c23a" },
  { label: "90分以上", value: 12, color: "#409eff" },
  { label: "70-79分", value: 25, color: "#e6a23c" },
  { label: "60-69分", value: 8, color: "#f56c6c" },
  { label: "60分以下", value: 3, color: "#909399" },
]);
const matchStatusStats = ref([
  { label: "未开始", value: 4, color: "#909399" },
  { label: "报名中", value: 7, color: "#409eff" },
  { label: "评审中", value: 3, color: "#e6a23c" },
  { label: "已结束", value: 9, color: "#67c23a" },
]);
const dailyGrowth = ref([
  { date: "7-2", user: 3, team: 1, work: 2 },
  { date: "7-3", user: 5, team: 2, work: 3 },
  { date: "7-4", user: 4, team: 3, work: 2 },
  { date: "7-5", user: 6, team: 2, work: 4 },
  { date: "7-6", user: 7, team: 4, work: 5 },
  { date: "7-7", user: 8, team: 3, work: 6 },
  { date: "7-8", user: 5, team: 2, work: 3 },
]);
const newsList = ref([]);

// ECharts 图表 option
const matchStatusOption = computed(() => {
  console.log("图表配置更新，当前数据:", matchStatusStats.value);

  return {
    tooltip: { trigger: "item" },
    legend: { bottom: 0 },
    series: [
      {
        name: "比赛状态",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 8, borderColor: "#fff", borderWidth: 2 },
        label: { show: true, position: "outside" },
        data: matchStatusStats.value.map((i) => ({
          value: i.value,
          name: i.label,
          itemStyle: { color: i.color },
        })),
      },
    ],
  };
});
const dailyGrowthOption = computed(() => ({
  tooltip: { trigger: "axis" },
  legend: { data: ["用户", "队伍", "作品"] },
  xAxis: { type: "category", data: dailyGrowth.value.map((i) => i.date) },
  yAxis: { type: "value" },
  series: [
    {
      name: "用户",
      type: "line",
      data: dailyGrowth.value.map((i) => i.user),
      itemStyle: { color: "#409eff" },
      smooth: true,
    },
    {
      name: "队伍",
      type: "line",
      data: dailyGrowth.value.map((i) => i.team),
      itemStyle: { color: "#67c23a" },
      smooth: true,
    },
    {
      name: "作品",
      type: "line",
      data: dailyGrowth.value.map((i) => i.work),
      itemStyle: { color: "#e6a23c" },
      smooth: true,
    },
  ],
}));
const competitionCategoryOption = computed(() => ({
  tooltip: { trigger: "item" },
  xAxis: {
    type: "category",
    data: competitionCategories.value.map((i) => i.category),
  },
  yAxis: { type: "value" },
  series: [
    {
      name: "竞赛数量",
      type: "bar",
      data: competitionCategories.value.map((i) => i.count),
      itemStyle: {
        color: (params) => competitionCategories.value[params.dataIndex].color,
      },
      barWidth: "60%",
    },
  ],
}));
const scoreDistOption = computed(() => ({
  tooltip: { trigger: "item" },
  xAxis: { type: "category", data: scoreDist.value.map((i) => i.label) },
  yAxis: { type: "value" },
  series: [
    {
      name: "作品数",
      type: "bar",
      data: scoreDist.value.map((i) => i.value),
      itemStyle: { color: (params) => scoreDist.value[params.dataIndex].color },
    },
  ],
}));
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 32px;
  padding-bottom: 32px;
}
.dashboard-header {
  margin-bottom: 0;
}
.dashboard-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #222e3c;
}
.dashboard-desc {
  color: #888;
  font-size: 16px;
  margin-bottom: 24px;
}
.dashboard-cards {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
  flex-wrap: nowrap;
  overflow-x: hidden;
  padding-bottom: 8px;
}
.dashboard-card {
  flex: 1;
  border-radius: 16px;
  color: #fff;
  padding: 20px 16px 16px 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: relative;
  overflow: hidden;
  min-width: 140px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  transition: transform 0.2s;
}
.dashboard-card:hover {
  transform: translateY(-4px) scale(1.03);
}
.card-blue {
  background: linear-gradient(135deg, #1976d2 60%, #42a5f5 100%);
}
.card-green {
  background: linear-gradient(135deg, #43a047 60%, #66bb6a 100%);
}
.card-cyan {
  background: linear-gradient(135deg, #00bcd4 60%, #4dd0e1 100%);
}
.card-orange {
  background: linear-gradient(135deg, #ff9800 60%, #ffc107 100%);
}
.card-purple {
  background: linear-gradient(135deg, #7e57c2 60%, #b39ddb 100%);
}
.card-pink {
  background: linear-gradient(135deg, #ec407a 60%, #f06292 100%);
}
.card-icon {
  font-size: 28px;
  margin-bottom: 6px;
  opacity: 0.9;
}
.card-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 6px;
}
.card-label {
  font-size: 13px;
  opacity: 0.95;
}

.dashboard-main {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}
.dashboard-col-left,
.dashboard-col-right {
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.dashboard-col-left {
  flex: 1.8;
  min-width: 300px;
}
.dashboard-col-right {
  flex: 1.2;
  min-width: 260px;
  max-width: 420px;
}

.dashboard-charts-grid {
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.dashboard-chart {
  background: #f8f9fa;
  border-radius: 10px;
  padding: 18px 18px 12px 18px;
  margin-bottom: 0;
}
@media (max-width: 1100px) {
  .dashboard-main {
    flex-direction: column;
  }
  .dashboard-col-left,
  .dashboard-col-right {
    min-width: 0;
    max-width: 100%;
  }
}

.dashboard-quick-links {
  display: flex;
  gap: 18px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}
.quick-link-card {
  flex: 1 1 120px;
  min-width: 120px;
  max-width: 180px;
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: box-shadow 0.2s, transform 0.2s;
  padding: 16px 18px;
  font-size: 16px;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.08);
  border: 1px solid #f0f0f0;
}
.quick-link-card:hover {
  box-shadow: 0 4px 16px rgba(33, 150, 243, 0.16);
  transform: translateY(-2px) scale(1.04);
}
.quick-link-icon {
  font-size: 22px;
}
.quick-link-label {
  font-weight: 500;
}
.dashboard-section {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 24px;
  margin-bottom: 12px;
}
.dashboard-section-title {
  font-size: 17px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #304156;
}
.dashboard-table {
  width: 100%;
  border-collapse: collapse;
}
.dashboard-table th,
.dashboard-table td {
  padding: 10px 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}
.dashboard-table th {
  background-color: #f5f7fa;
  font-weight: bold;
  color: #555;
}
.dashboard-table tr:last-child td {
  border-bottom: none;
}
.tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: bold;
}
.ongoing {
  background-color: #e1f3d8;
  color: #67c23a;
}
.ended {
  background-color: #fde2e2;
  color: #f56c6c;
}
.pending {
  background-color: #f0f9ff;
  color: #409eff;
}
.reviewing {
  background-color: #fff7e6;
  color: #e6a23c;
}
.unknown {
  background-color: #f5f5f5;
  color: #909399;
}
.btn-sm {
  background-color: #e0e0e0;
  color: #333;
  padding: 6px 12px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-size: 13px;
  transition: background-color 0.2s;
}
.btn-sm:hover {
  background-color: #d0d0d0;
}
.dashboard-charts {
  display: flex;
  gap: 32px;
  flex-wrap: wrap;
  margin-bottom: 0;
}
.dashboard-chart {
  flex: 1 1 320px;
  min-width: 260px;
  background: #f8f9fa;
  border-radius: 10px;
  padding: 18px 18px 12px 18px;
  margin-bottom: 0;
}
.chart-bar-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.chart-bar-item {
  display: flex;
  align-items: center;
  gap: 8px;
}
.chart-bar-label {
  width: 40px;
  font-size: 13px;
  color: #888;
}
.chart-bar-outer {
  flex: 1;
  background: #e3e8ee;
  border-radius: 6px;
  height: 18px;
  display: flex;
  align-items: center;
  position: relative;
}
.chart-bar-inner {
  height: 100%;
  border-radius: 6px;
  background: #409eff;
  transition: width 0.3s;
}
.chart-bar-value {
  font-size: 13px;
  color: #409eff;
  margin-left: 8px;
}
.chart-pie-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.chart-pie-item {
  display: flex;
  align-items: center;
  gap: 8px;
}
.chart-pie-label {
  width: 60px;
  font-size: 13px;
  color: #888;
}
.chart-pie-bar-outer {
  flex: 1;
  background: #e3e8ee;
  border-radius: 6px;
  height: 16px;
  display: flex;
  align-items: center;
  position: relative;
}
.chart-pie-bar-inner {
  height: 100%;
  border-radius: 6px;
  transition: width 0.3s;
}
.chart-pie-value {
  font-size: 13px;
  margin-left: 8px;
}
.news-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.news-list li {
  font-size: 15px;
  margin-bottom: 8px;
  color: #304156;
}
.news-type {
  color: #409eff;
  font-weight: 500;
  margin-right: 6px;
}
.news-date {
  color: #888;
  font-size: 13px;
  margin-left: 6px;
}
.deadline-list {
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.deadline-item {
  display: flex;
  align-items: flex-start;
  gap: 18px;
}
.deadline-icon {
  font-size: 28px;
  margin-top: 2px;
  flex-shrink: 0;
}
.deadline-info {
  flex: 1;
}
.deadline-name {
  font-size: 15px;
  font-weight: 500;
  margin-bottom: 2px;
}
.deadline-date {
  color: #888;
  font-size: 13px;
  margin-bottom: 4px;
}
.deadline-progress-outer {
  background: #e3e8ee;
  border-radius: 6px;
  height: 10px;
  width: 180px;
  margin: 4px 0 4px 0;
  overflow: hidden;
}
.deadline-progress-inner {
  height: 100%;
  border-radius: 6px;
  transition: width 0.3s;
}
.deadline-remaining {
  color: #1976d2;
  font-size: 13px;
  font-weight: 500;
}
.bar-group-vertical {
  display: flex;
  align-items: flex-end;
  gap: 24px;
  height: 120px;
  margin-top: 16px;
}
.bar-vertical-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.bar-vertical-bar-outer {
  width: 24px;
  height: 100px;
  background: #e3e8ee;
  border-radius: 8px;
  display: flex;
  align-items: flex-end;
  margin-bottom: 6px;
  overflow: hidden;
}
.bar-vertical-bar-inner {
  width: 100%;
  border-radius: 8px;
  transition: height 0.3s;
}
.bar-vertical-label {
  font-size: 13px;
  color: #888;
  margin-top: 2px;
}
.bar-vertical-value {
  font-size: 14px;
  color: #222;
  font-weight: bold;
  margin-top: 2px;
}
.bar-group-multitype {
  display: flex;
  align-items: flex-end;
  gap: 18px;
  height: 120px;
  margin-top: 16px;
}
.bar-multitype-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.bar-multitype-label {
  font-size: 13px;
  color: #888;
  margin-bottom: 2px;
}
.bar-multitype-bars {
  display: flex;
  gap: 2px;
  align-items: flex-end;
  height: 100px;
}
.bar-multitype-bar {
  width: 8px;
  border-radius: 4px;
  margin: 0 1px;
  transition: height 0.3s;
}
.bar-multitype-bar.user {
  background: #409eff;
}
.bar-multitype-bar.team {
  background: #67c23a;
}
.bar-multitype-bar.work {
  background: #e6a23c;
}
.bar-multitype-legend {
  margin-top: 8px;
  font-size: 13px;
  color: #888;
  display: flex;
  gap: 18px;
}
.bar-multitype-legend .user {
  color: #409eff;
}
.bar-multitype-legend .team {
  color: #67c23a;
}
.bar-multitype-legend .work {
  color: #e6a23c;
}
@media (max-width: 900px) {
  .dashboard-cards,
  .dashboard-quick-links,
  .dashboard-charts {
    flex-direction: column;
  }
  .dashboard-card,
  .quick-link-card,
  .dashboard-chart {
    min-width: auto;
    max-width: 100%;
  }
}
</style>
