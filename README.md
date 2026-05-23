# 海报设计竞赛管理系统

基于 **Spring Boot 3 + Vue 3** 的全栈竞赛管理平台，面向高校海报设计类赛事，提供竞赛发布、团队报名、作品提交、评委评审、成绩公示等全流程管理能力。

## 功能模块

| 模块 | 说明 |
|------|------|
| 竞赛管理 | 竞赛发布、报名截止、状态流转 |
| 团队管理 | 组队报名、成员邀请 |
| 作品管理 | 海报上传、展示与点赞 |
| 评委管理 | 评委分配、评分录入 |
| 公告 / 新闻 | 赛事通知与新闻发布 |
| 奖项 / 证书 | 奖项设置、获奖证书自动生成 |
| 用户中心 | 注册登录（JWT）、短信验证码、个人信息管理 |
| 管理后台 | 管理员对所有数据的增删改查 |

## 技术栈

### 后端（`poster-design-springboot`）

- **Java 17** + **Spring Boot 3.1.5**
- **MyBatis-Plus** 数据访问层
- **MySQL 8** 关系型数据库
- **Redis Sentinel** 缓存与会话
- **MinIO** 对象存储（海报图片）
- **阿里云 OSS / SMS** 云存储与短信服务
- **JWT** 无状态认证
- **Knife4j（OpenAPI 3）** 接口文档

### 前端（`poster-design-vue`）

- **Vue 3** + **Vite 7**
- **Element Plus** UI 组件库
- **Pinia** 状态管理（持久化）
- **Vue Router 4** 路由
- **ECharts / Vue-ECharts** 数据可视化
- **GSAP + Swiper + fullpage.js** 动效与全屏滚动
- **Tailwind CSS** 样式

## 项目结构

```
poster_design/
├── poster-design-springboot/   # 后端 Maven 多模块项目
│   ├── common/                 # 通用组件（配置、拦截器、工具类）
│   ├── model/                  # 数据模型（Entity、DTO、VO）
│   └── server/                 # 业务实现与接口层
├── poster-design-vue/          # 前端 Vue 3 项目
│   └── src/
│       ├── views/              # 页面视图
│       ├── apis/               # 接口请求封装
│       ├── stores/             # Pinia 状态
│       └── router/             # 路由配置
└── sql/
    └── poster_design.sql       # 数据库初始化脚本
```

## 快速启动

### 环境依赖

- JDK 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8+
- Redis（Sentinel 模式或单机均可）
- MinIO（可选，用于本地文件存储）

### 数据库初始化

```sql
CREATE DATABASE poster_design CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 然后执行 sql/poster_design.sql
```

### 后端配置

复制配置模板并填写真实连接信息（不要提交到版本库）：

```bash
cp poster-design-springboot/server/src/main/resources/application.yaml \
   poster-design-springboot/server/src/main/resources/application-local.yaml
```

在 `application-local.yaml` 中配置以下环境变量或直接修改对应值：

| 环境变量 | 说明 |
|----------|------|
| `DB_HOST` / `DB_PORT` | 数据库地址与端口 |
| `DB_USERNAME` / `DB_PASSWORD` | 数据库账号密码 |
| `REDIS_HOST` / `REDIS_PASSWORD` | Redis 连接信息 |
| `MINIO_ENDPOINT` / `MINIO_ACCESS_KEY` / `MINIO_SECRET_KEY` | MinIO 连接信息 |

### 启动后端

```bash
cd poster-design-springboot
mvn spring-boot:run -pl server
```

### 启动前端

```bash
cd poster-design-vue
npm install
npm run dev
```

## License

[MIT](LICENSE)
