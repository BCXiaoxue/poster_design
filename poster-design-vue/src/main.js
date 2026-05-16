import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import "./assets/tailwind.css";
import "./style.css";
import router from "./router";
import mDirectives from "./directives";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import "element-plus/theme-chalk/dark/css-vars.css";
import "element-plus/dist/index.css"; // Element Plus 样式
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import ElementPlus from "element-plus";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import { useThemeStore } from "./stores/theme";

// ECharts 相关
import "echarts"; // 主包
import "echarts/renderers"; // 关键：引入渲染器
import VueECharts from "vue-echarts";

const app = createApp(App);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(ElementPlus, {
  locale: zhCn,
});

app.use(router).use(pinia);

// 初始化主题
const themeStore = useThemeStore();
themeStore.initTheme();

app.use(mDirectives).mount("#app");
app.component("v-chart", VueECharts);
