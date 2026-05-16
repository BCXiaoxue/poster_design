<template>
  <div class="header">
    <div class="box">
      <div class="left" @click="homeClick">
        <img v-lazy src="/src/assets/images/logo.png" alt="logo" class="logo" />
        <div class="title">全国大学生海报设计大赛</div>
      </div>
      <div class="right" v-show="isRightShow">
        <div class="l">
          <ul class="nav">
            <li @click="homeClick">首页</li>
            <li @click="informClick">大赛通知</li>
            <li @click="signupClick">报名入口</li>
            <li @click="aboutusClick">关于我们</li>
          </ul>
        </div>
        <div class="m">
          <el-switch
            v-model="isDark"
            inline-prompt
            active-text="🌙"
            inactive-text="☀️"
            size="default"
            class="theme-switch"
          />
        </div>

        <div class="r">
          <div @click="onClickAvatar">
            <img
              v-lazy
              v-if="userAvatar"
              :src="userAvatar"
              alt="用户头像"
              class="user-avatar"
              @click="avatarClick"
            />
            <div v-else class="login" @click="loginClick">
              <el-icon>
                <UserFilled />
              </el-icon>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="phoneBox" v-show="isRightShow">
      <div class="phone">
        <ul class="nav">
          <li @click="homeClick">首页</li>
          <li @click="informClick">大赛通知</li>
          <li @click="signupClick">报名入口</li>
          <li @click="aboutusClick">关于我们</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { ref, watch, nextTick } from "vue";
import { usePersonStore } from "../../stores/person";
import { useThemeStore } from "@/stores/theme";

const props = defineProps({
  isRightShow: {
    type: Boolean,
    default: true,
  },
});

const router = useRouter();
const personStore = usePersonStore();
const themeStore = useThemeStore();
const userAvatar = ref("");

if (personStore.imgUrl) {
  userAvatar.value = personStore.imgUrl;
}

const homeClick = () => {
  router.push("/index");
};

const informClick = () => {
  router.push({
    path: "/inform",
    query: { Type: 1 },
  });
};

const signupClick = () => {
  router.push({
    path: "/competitionlist",
    query: { Type: 1 },
  });
};

const onClickAvatar = () => {
  let jwt = localStorage.getItem("jwt");
  if (!jwt) {
    router.push("/login");
  } else {
    router.push("/personal/account");
  }
};

const aboutusClick = () => {
  router.push("/aboutus");
};

const isDark = ref(document.documentElement.classList.contains("dark"));
watch(isDark, (newVal) => {
  themeStore.setTheme(newVal ? "dark" : "light");
});
</script>

<style>
:root {
  font-size: clamp(14px, 1.5vw, 18px);
}
</style>
<style scoped>
.header {
  display: flex;
  justify-content: center;
  width: 100vw;
  min-height: 10vh;
}

.box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  padding: 0 2vw;
  box-sizing: border-box;
}

.left {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo {
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 0.7rem;
  object-fit: contain;
  margin-right: 0.625rem;
}

.title {
  font-size: clamp(1rem, 2vw, 1.5rem);
  font-weight: bold;
}

.nav {
  display: flex;
  gap: 1.5rem;
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav li {
  cursor: pointer;
  font-size: clamp(0.875rem, 1.5vw, 1.125rem);
  transition: color 0.3s;
}

.nav li:hover {
  color: #409eff;
}

.theme-switch {
  transform: scale(1.2);
  margin: 0 0.5rem;
}

.right {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex-wrap: nowrap;
}

.l,
.m,
.r {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.l {
  flex: 1;
  justify-content: center;
}

.m {
  margin: 0 1rem;
}

.r {
  justify-content: flex-end;
}

.user-avatar {
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 50%;
  cursor: pointer;
  object-fit: cover;
  border: 2px solid #409eff;
}

.login {
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 50%;
  background-color: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login:hover {
  background-color: #66b1ff;
}

.phoneBox {
  display: none;
}

@media (max-width: 768px) {
  .theme-switch {
    transform: scale(1.1);
    margin: 0 0.25rem;
  }

  .header {
    flex-wrap: wrap;
  }

  .box {
    flex-direction: column;
    align-items: stretch;
    width: 100%;
    max-width: 100%;
    padding: 1rem;
    gap: 1rem;
  }

  .left {
    justify-content: center;
    width: 100%;
    gap: 0.5rem;
  }

  .title {
    font-size: clamp(1rem, 4vw, 1.25rem);
    text-align: center;
  }

  /* 隐藏顶部导航菜单 */
  .l .nav {
    display: none;
  }

  .right {
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    gap: 0.5rem;
    flex-wrap: nowrap;
  }

  .l {
    display: none; /* 隐藏导航列表 */
  }

  .m,
  .r {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-shrink: 0;
  }

  .phoneBox {
    display: block;
    position: relative;
    z-index: 1000;
  }

  .phone {
    display: flex;
    justify-content: center;
    padding: 0.5rem 0;
    position: relative;
    z-index: 10;
    background-color: inherit;
  }

  .phone .nav {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    list-style: none;
    gap: 1rem;
    padding: 0;
    margin: 0;
    width: 100%;
  }

  .phone .nav li {
    font-size: clamp(0.75rem, 3vw, 1rem);
    cursor: pointer;
    transition: color 0.3s;
    white-space: nowrap;
  }

  .phone .nav li:hover {
    color: #409eff;
  }
}
</style>
