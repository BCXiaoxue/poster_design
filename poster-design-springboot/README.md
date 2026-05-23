# poster-design-springboot

Spring Boot 3 后端服务，采用 Maven 多模块结构。

## 模块说明

| 模块 | 说明 |
|------|------|
| `common` | 通用组件：全局配置、拦截器、异常处理、JWT、MinIO/OSS/SMS 工具类 |
| `model` | 数据模型：Entity、DTO、VO、枚举 |
| `server` | 业务逻辑与 REST 接口，Controller → Service → Mapper 三层架构 |

## 接口文档

启动后访问：`http://localhost:8080/doc.html`（Knife4j / OpenAPI 3）

## 配置说明

`server/src/main/resources/application.yaml` 使用环境变量占位符，本地开发请创建 `application-local.yaml` 覆盖敏感配置，该文件已加入 `.gitignore`，不会提交到版本库。

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/poster_design?...
    username: root
    password: your-password

  data:
    redis:
      sentinel:
        master: mymaster
        nodes:
          - localhost:27001
      password: your-redis-password

govy:
  minio:
    endpoint: http://localhost:9000
    access-key: your-access-key
    secret-key: your-secret-key
    bucket-name: poster-design
```
