# AGENTS.md — CaSystem

CA 证书管理系统，包含 **两个活跃代码库**：

| 位置             | 技术栈                                    | 构建                             | 入口                      |
| ---------------- | ----------------------------------------- | -------------------------------- | ------------------------- |
| `backend/`     | Spring Boot 3.2.0 · Java 17 · Jakarta   | `mvn clean package -DskipTests` | `CaSystemApplication.java` |
| `frontend/`    | Vue 3 · Vite 5 · Vue Router 4 · Vuex 4  | `npm run dev` (3000), `npm run build` | `src/main.js`          |
| `./`（根目录） | Servlet/JSP · Java 21 · javax           | `mvn clean package` (WAR)         | **不要修改**               |

## 基本规则

- **只改 `backend/` 和 `frontend/`**。根目录 `src/` + `pom.xml` 已遗留。
- `backend/pom.xml` 独立于根 `pom.xml`（使用 `spring-boot-starter-parent`）。
- Docker 构建使用 `docker-compose.yml` 内联 Dockerfile，**没有独立的 Dockerfile**。
- 前端 Docker 内使用 `npm ci`（非 `npm install`）。
- `.env` 和 `*.key/*.pem/*.p12/*.pfx` 等构建产物 **不要提交**。

## 后端

```bash
cd backend
mvn spring-boot:run        # dev服务器，端口 8088，默认 dev profile
mvn clean package          # 构建 JAR
mvn test                   # 测试（3 个集成测试需 MySQL，7 个总测试）
```

- **必须设置 `JWT_SECRET`**，否则启动失败。
- dev profile: `application-dev.yml`（默认），prod: `application-prod.yml`。
- JPA `ddl-auto=update` 自动建表；基础 schema 见 `ca.sql`。
- **管理员**：`user.authority == '1'`。
- Session: `STATELESS`。验证码存于内存 `CaptchaStore`。
- 使用 Lombok：自动生成 getter/setter/构造器。
- 3 个 `@SpringBootTest` 测试需要本地 MySQL。

## 前端

```bash
cd frontend
npm run dev        # 端口 3000，代理 /api → localhost:8088
npm run build      # 输出到 dist/
npm run lint       # ESLint 8 + vue 插件
```

### UI 设计原则

- 风格：**Accessible & Ethical**（安全蓝 #0369A1 为主色，绿色 #16A34A 为强调色）
- **消除无限动画**：背景漂浮效果在 hover 时暂停，支持 `prefers-reduced-motion`
- **动画使用 cubic-bezier(0.4, 0, 0.2, 1)** 获得更流畅过渡
- **色彩严格使用设计系统变量**：`--color-primary`、`--gradient-primary` 等
- **注意**：`Home.vue` 的样式是独立的，`AppLayout.vue` 提供全局导航 + 背景效果

### 路由结构

| 路径                | 父组件        | 认证要求          |
| ------------------- | ------------- | ----------------- |
| `/login`、`/register` | 无            | 不需要            |
| `/app/*`              | `AppLayout.vue` | 需要登录          |
| `/admin/*`            | `AdminLayout.vue` | 需要登录 + 管理员 |

旧路径通过 `redirect` 兼容。

### 认证模型

- JWT 存 `localStorage`（非 httpOnly cookie）。
- Vuex `store.state.token` + `store.state.user` 管理状态，axios 拦截器注入 `Authorization` 头。
- 401 响应触发自动登出。

## Docker 部署

```bash
cp .env.example .env
docker compose up -d --build
```

MySQL 容器加载 `backend/src/main/resources/db/migration/` 初始化数据库。

## 构建产物（.gitignore 已包含）

- `target/`、`backend/target/`、`frontend/dist/`
- `*.key`、`*.pem`、`*.p12`、`*.pfx`、`node_modules/`
- `backend/downloads/`、`.env`