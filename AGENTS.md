# AGENTS.md — CaSystem

## 项目概览

CA 证书管理系统，仓库包含 **三个独立代码库**：

| 位置 | 技术栈 | 构建方式 | 入口 |
|---|---|---|---|
| `backend/` | Spring Boot 3.2.0 · Java 17 · Jakarta | `mvn clean package -DskipTests` (JAR) | `CaSystemApplication.java` |
| `frontend/` | Vue 3 · Vite 5 | `npm run dev` (端口 3000) | `src/main.js` |
| `./` (根目录) | 遗留 Servlet/JSP · Java 21 · javax | `mvn clean package` (WAR) · cargo-tomcat9x 端口 9090 | `src/main/webapp/` |

**新代码只改 `backend/` + `frontend/`。根目录 `src/` 是正在被替换的旧版。**

## 常用命令

### 后端
```bash
cd backend
mvn spring-boot:run                    # 开发服务器，端口 8088
mvn clean package -DskipTests          # 构建 JAR
mvn test                               # 运行测试（3 个集成测试，需要数据库）
```

### 前端
```bash
cd frontend
npm install                            # 安装依赖
npm run dev                            # 开发服务器，端口 3000，代理 /api -> :8088
npm run build                          # 生产构建到 dist/
npm run lint                           # eslint（无 .eslintrc，使用默认规则）
```

### Docker（推荐部署方式）
```bash
cp .env.example .env                   # 配置环境变量
docker compose up -d --build           # 构建并启动所有服务
docker compose logs -f backend         # 查看后端日志
```

辅助脚本：`./deploy.sh`（Linux）和 `deploy.ps1`（Windows）封装了 docker-compose 操作。

## 架构注意事项

### 数据库名称不一致
`backend/src/main/resources/application.yml` 中数据库名为 `schedule_system`。README 和迁移脚本使用 `ca_system`。**直接连接本地 MySQL 时用 `schedule_system`。**

### JPA 自动 DDL
`spring.jpa.hibernate.ddl-auto=update` —— 实体类自动建表。迁移脚本 `db/migration/V2__*.sql` 到 `V4__*.sql` 是补充性的。**没有 V1 迁移**，基础 schema 是 `ca.sql`。

### API 路径：文档 vs 实际
README 中的 API 路径不准确。前端实际调用的路径：
- 证书列表：`/api/certificates/my`
- 吊销证书：`/api/certificates/revoke`（POST）
- 下载证书：`/api/certificates/download/{serialNumber}`
- 签名/验签：`/api/certificates/generate-signature`、`/api/certificates/verify-signature`（在 `/certificates/` 下，不是 `/signature/`）
- 前端未接入 token 刷新接口
- 前端未调用 `/api/validation/certificate`

### 认证模型
JWT token 存在 `localStorage`（非 httpOnly cookie）。管理员判定：`user.authority === '1'`。前端通过 Vuex + localStorage 持久化认证状态。

### 前端样式冲突
`App.vue` 强制深色背景（`#0a0a0f`），与浅色主题的设计系统 CSS 文件冲突。`ColorSchemeSelector.vue` 存在但未接入任何视图。

## 测试模型
- 后端有 3 个 `@SpringBootTest` 集成测试，位于 `backend/src/test/`
- 它们加载完整的 Spring 上下文，需要数据库连接
- 在 `backend/` 目录执行 `mvn test`
- JUnit 5，`@Transactional` 每个测试后回滚

## 需排除的构建产物
- `target/`、`backend/target/`、`frontend/dist/` 为构建输出
- `*.key`、`*.pem`、`*.p12`、`*.pfx` 为证书/密钥文件（二进制，可能涉密）
- `node_modules/` —— 未修改依赖时跳过安装

## 重要约束
- `backend/pom.xml` 独立于根目录 `pom.xml`（使用 `spring-boot-starter-parent`）
- Docker 使用 `docker-compose.yml` 内联 Dockerfile，**没有独立的 Dockerfile 文件**
- 后端使用 Lombok，注意自动生成的 getter/setter/构造器
- Docker 构建中前端使用 `npm ci`（非 `npm install`）
- ESLint `devDependencies` 存在但无 `.eslintrc`，lint 使用默认规则
