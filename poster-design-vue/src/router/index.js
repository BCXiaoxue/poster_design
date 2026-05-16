import { createRouter, createWebHistory } from "vue-router";
import { usePersonStore } from "@/stores/person";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      redirect: "/index",
    },
    {
      path: "/index",
      name: "index",
      component: () => import("@/views/main/index.vue"),
    },
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/login-register/login/index.vue"),
    },
    {
      path: "/forget",
      name: "forget",
      component: () => import("@/views/login-register/forget/index.vue"),
    },
    {
      path: "/register",
      name: "register",
      component: () => import("@/views/login-register/register/index.vue"),
    },
    {
      path: "/inform",
      name: "inform",
      component: () => import("@/views/inform/index.vue"),
    },
    {
      path: "/informContent",
      name: "informContent",
      component: () => import("@/views/inform/content.vue"),
    },
    {
      path: "/competitionlist",
      name: "competitionlist",
      component: () => import("@/views/competitionlist/index.vue"),
    },
    {
      path: "/competition/:id",
      name: "competition",
      component: () => import("@/views/competition/index.vue"),
    },
    {
      path: "/competitionjudge/:id",
      name: "competitionjudge",
      component: () => import("@/views/competitionjudge/index.vue"),
    },
    {
      path: "/aboutus",
      name: "aboutus",
      component: () => import("@/views/aboutUs/index.vue"),
    },
    {
      path: "/admin",
      name: "admin",
      component: () => import("@/views/admin/index.vue"),
      children: [
        {
          path: "",
          name: "admin-dashboard",
          component: () => import("@/views/admin/Dashboard.vue"),
        },
        {
          path: "users",
          name: "admin-users",
          component: () => import("@/views/admin/Users.vue"),
        },
        {
          path: "team",
          name: "admin-team",
          component: () => import("@/views/admin/Team.vue"),
        },
        {
          path: "news",
          name: "admin-news",
          component: () => import("@/views/admin/News.vue"),
        },
        {
          path: "inform",
          name: "admin-inform",
          component: () => import("@/views/admin/Inform.vue"),
        },
        {
          path: "competition",
          name: "admin-competition",
          component: () => import("@/views/admin/Competition.vue"),
        },
        {
          path: "setting",
          name: "admin-setting",
          component: {
            template:
              '<div style="padding:32px;font-size:18px;">系统设置页面（请替换为正式页面）</div>',
          },
        },
      ],
    },
    {
      path: "/competition/detail/:id",
      name: "detail",
      component: () =>
        import("@/views/competition/components/detail/index.vue"),
    },
    {
      path: "/competition/signup/:id",
      name: "signup",
      component: () =>
        import("@/views/competition/components/signup/index.vue"),
    },
    // 田溯开——个人中心
    {
      path: "/personal",
      name: "personal",
      component: () => import("@/views/personal-center/index.vue"),
      children: [
        {
          path: "account",
          name: "account",
          component: () => import("@/views/personal-center/account/index.vue"),
        },
        {
          path: "competition",
          name: "personalCompetition",
          component: () =>
            import("@/views/personal-center/competition/index.vue"),
        },
        {
          path: "group",
          name: "personalGroup",
          component: () => import("@/views/personal-center/group/index.vue"),
        },
        {
          path: "message",
          name: "message",
          component: () => import("@/views/personal-center/message/index.vue"),
        },
        {
          path: "setting",
          name: "setting",
          component: () => import("@/views/personal-center/setting/index.vue"),
        },
        {
          path: "judge",
          name: "judge",
          component: () => import("@/views/personal-center/judge/index.vue"),
        },
        {
          path: "honor",
          name: "honor",
          component: () => import("@/views/personal-center/honor/index.vue"),
        },
      ],
    },
  ],
  scrollBehavior(to, from, savedPosition) {
    //若有保存的滚动位置，则返回它
    if (savedPosition) {
      return savedPosition;
    }
    //否则就滚到顶部
    return { left: 0, top: 0 };
  },
});

router.beforeEach(async (to, from, next) => {
  // 在守卫内部获取 store 实例
  const personStore = usePersonStore();
  // 确保持久化数据已加载
  // await personStore.$hydrate();
  console.log(personStore.authority);
  console.log(to.path);
  if (personStore.authority == 1) {
    if (to.path.startsWith("/admin")) {
      next();
    } else {
      next("/admin");
    }
  }

  // jwt 验证
  const jwt = localStorage.getItem("jwt");
  if (jwt == null) {
    if (to.path == "/index") {
      next();
    } else if (to.path == "/login") {
      next();
    } else if (to.path == "/register") {
      next();
    } else if (to.path == "/forget") {
      next();
    } else if (to.path == "/aboutus") {
      next();
    } else {
      next("/login");
    }
  }

  // 检查是否以 /admin/ 开头
  if (to.path.startsWith("/admin")) {
    if (personStore.authority == 1) {
      next();
    } else if (personStore.authority == 0 || jwt == null) {
      // 已登录但不是管理员，重定向到首页
      next("/index");
    } else {
      next("/login");
    }
  } else {
    next();
  }
});

export default router;
