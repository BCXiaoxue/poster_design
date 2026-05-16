import path from "path";
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";

export default defineConfig({
  base: '/',
  plugins: [
    vue(),
    // 自动引入 API，比如 ref、computed、defineComponent 等
    AutoImport({
      resolvers: [ElementPlusResolver()],
      dts: "src/auto-imports.d.ts", // 自动生成类型定义文件
    }),

    // 自动引入组件
    Components({
      resolvers: [ElementPlusResolver()],
      dts: "src/components.d.ts", // 自动生成类型定义文件
    }),
  ],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src"),
    },
  },
  //代理配置
  server: {
    port: 5173, // 反向代理
    proxy: {
      "/api": {
        target: "http://122.51.51.36:8080",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
    hmr: true,
  },
});
