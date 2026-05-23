# poster-design-vue

Vue 3 + Vite 前端项目。

## 开发启动

```bash
npm install
npm run dev      # 开发服务器 http://localhost:5173
npm run build    # 生产构建（输出到 dist/）
npm run preview  # 预览生产构建
```

## 目录结构

```
src/
├── views/          # 页面视图
│   ├── main/       # 首页
│   ├── login-register/  # 登录 / 注册
│   ├── competition/     # 竞赛列表与详情
│   ├── competitionjudge/ # 评委评审
│   ├── personal-center/  # 个人中心
│   ├── admin/           # 管理后台
│   ├── inform/          # 公告新闻
│   └── aboutUs/         # 关于我们
├── apis/           # Axios 接口封装
├── stores/         # Pinia 状态管理
├── router/         # Vue Router 路由
├── components/     # 全局公共组件
├── directives/     # 自定义指令
└── utils/          # 工具函数
```

## 接口代理

开发环境默认代理到 `http://localhost:8080`，可在 [vite.config.js](vite.config.js) 中修改 `server.proxy` 配置。
