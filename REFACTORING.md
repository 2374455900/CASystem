# CA系统重构文档

## 项目概述

本项目将原有的基于Java Servlet的传统Web应用重构为现代化的前后端分离架构。后端采用Spring Boot生态系统，前端采用Vue.js框架。

## 重构目标

1. **后端架构现代化** - 从Servlet迁移到Spring Boot
2. **前后端分离** - RESTful API + Vue.js SPA
3. **代码质量提升** - 分层架构、依赖注入、事务管理
4. **安全性增强** - Spring Security + JWT认证
5. **可维护性改善** - 组件化、模块化设计

## 项目结构

```
CaSystem/
├── backend/                    # Spring Boot后端
│   ├── src/main/java/com/kuugae/ca/
│   │   ├── CaSystemApplication.java   # 应用入口
│   │   ├── config/            # 配置类
│   │   │   └── SecurityConfig.java   # 安全配置
│   │   ├── controller/       # REST控制器
│   │   │   ├── AuthController.java
│   │   │   ├── CertificateController.java
│   │   │   ├── CaptchaController.java
│   │   │   └── PublicKeyController.java
│   │   ├── dto/               # 数据传输对象
│   │   │   ├── ApiResponse.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   ├── CertificateApplyRequest.java
│   │   │   └── ...
│   │   ├── entity/            # JPA实体
│   │   │   ├── User.java
│   │   │   ├── Certificate.java
│   │   │   └── Crl.java
│   │   ├── exception/         # 异常处理
│   │   │   ├── BusinessException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── repository/        # 数据访问层
│   │   │   ├── UserRepository.java
│   │   │   ├── CertificateRepository.java
│   │   │   └── CrlRepository.java
│   │   ├── security/          # 安全相关
│   │   │   ├── JwtTokenProvider.java
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── UserPrincipal.java
│   │   ├── service/           # 业务逻辑层
│   │   │   ├── UserService.java
│   │   │   ├── CertificateService.java
│   │   │   ├── CertificateManagementService.java
│   │   │   └── ValidationService.java
│   │   └── utils/             # 工具类
│   │       ├── CertificateGenerator.java
│   │       └── KeyManager.java
│   └── src/main/resources/
│       └── application.yml     # 应用配置
│
└── frontend/                  # Vue.js前端
    ├── src/
    │   ├── api/               # API请求封装
    │   │   └── index.js
    │   ├── assets/            # 静态资源
    │   │   └── styles/
    │   │       └── main.css
    │   ├── router/            # 路由配置
    │   │   └── index.js
    │   ├── store/             # Vuex状态管理
    │   │   └── index.js
    │   ├── views/             # 页面组件
    │   │   ├── Login.vue
    │   │   ├── Register.vue
    │   │   ├── Home.vue
    │   │   ├── Apply.vue
    │   │   ├── Certificates.vue
    │   │   ├── Revoke.vue
    │   │   └── Profile.vue
    │   ├── App.vue
    │   └── main.js
    ├── package.json
    └── vite.config.js
```

## 技术栈

### 后端技术栈

| 技术 | 说明 | 版本 |
|------|------|------|
| Spring Boot | 应用框架 | 3.2.0 |
| Spring Data JPA | 数据访问 | - |
| Spring Security | 安全框架 | - |
| MySQL | 数据库 | 8.0 |
| Druid | 连接池 | 1.2.20 |
| JWT | Token认证 | 0.12.3 |
| BouncyCastle | 证书处理 | 1.78 |
| BCrypt | 密码加密 | 0.4 |

### 前端技术栈

| 技术 | 说明 | 版本 |
|------|------|------|
| Vue.js | 框架 | 3.4.0 |
| Vue Router | 路由管理 | 4.2.5 |
| Vuex | 状态管理 | 4.1.0 |
| Axios | HTTP客户端 | 1.6.2 |
| Vite | 构建工具 | 5.0.0 |

## API设计

### 认证接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/auth/login | 用户登录 |
| POST | /api/auth/register | 用户注册 |
| GET | /api/auth/me | 获取当前用户 |
| POST | /api/auth/logout | 用户登出 |

### 证书接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/certificates/apply | 申请证书 |
| POST | /api/certificates/revoke | 撤销证书 |
| GET | /api/certificates/my | 获取我的证书 |
| GET | /api/certificates/{serialNumber} | 获取证书详情 |
| GET | /api/certificates/download/{serialNumber} | 获取下载信息 |

### 公共接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/captcha/generate | 生成验证码 |
| GET | /api/public/key | 获取公钥 |
| GET | /api/public/health | 健康检查 |

## 数据库设计

### users表

| 字段 | 类型 | 描述 |
|------|------|------|
| id | INT | 主键 |
| username | VARCHAR | 用户名 |
| password | VARCHAR | 密码（加密） |
| email | VARCHAR | 邮箱 |
| authority | VARCHAR | 权限 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |
| deleted_at | DATETIME | 删除时间 |

### certificate表

| 字段 | 类型 | 描述 |
|------|------|------|
| id | INT | 主键 |
| serial_number | VARCHAR | 证书序列号 |
| organization | VARCHAR | 组织机构 |
| registration_number | VARCHAR | 工商注册号 |
| file_path | VARCHAR | 证书文件路径 |
| start_time | VARCHAR | 有效期起 |
| end_time | VARCHAR | 有效期止 |
| juridical_person | VARCHAR | 法人 |
| charge_person | VARCHAR | 经办人 |
| charge_phone | VARCHAR | 经办人电话 |
| username | VARCHAR | 所属用户 |
| state | VARCHAR | 状态 |

### crl表

| 字段 | 类型 | 描述 |
|------|------|------|
| id | INT | 主键 |
| serial_number | VARCHAR | 证书序列号 |
| organization | VARCHAR | 组织机构 |
| registration_number | VARCHAR | 工商注册号 |
| start_time | VARCHAR | 有效期起 |
| end_time | VARCHAR | 有效期止 |

## 配置说明

### 后端配置 (application.yml)

```yaml
spring:
  application:
    name: ca-system
  datasource:
    url: jdbc:mysql://localhost:3306/schedule_system
    username: root
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update

server:
  port: 8080

app:
  certificate:
    key-directory: ${KEY_DIRECTORY:C:/keys}
    download-path: ${CERTIFICATE_DOWNLOAD_PATH:./downloads}
```

### 前端配置

前端通过Vite配置代理解决跨域问题：

```javascript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

## 运行说明

### 后端启动

1. 确保MySQL数据库运行并创建数据库
2. 修改`application.yml`中的数据库配置
3. 执行以下命令：

```bash
cd backend
mvn spring-boot:run
```

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

### 环境变量

后端需要配置以下环境变量：

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| KEY_DIRECTORY | 密钥存储目录 | C:/keys |
| CERTIFICATE_DOWNLOAD_PATH | 证书下载路径 | ./downloads |
| DB_PASSWORD | 数据库密码 | - |

## 安全机制

1. **密码加密** - 使用BCrypt加密存储
2. **JWT认证** - Token有效期24小时
3. **CSRF防护** - 禁用CSRF（前后端分离）
4. **CORS配置** - 允许跨域访问
5. **参数校验** - 使用JSR-303校验注解

## 测试

后端单元测试位于`src/test/java`目录：

```bash
cd backend
mvn test
```

## 原业务功能对照

| 原功能 | 新实现 |
|--------|--------|
| LoginServlet | AuthController |
| RegistServlet | AuthController |
| ApplyServlet | CertificateController |
| RevokeCerServlet | CertificateController |
| CertificateInfoServlet | CertificateController |
| CheckCodeServlet | CaptchaController |

## 改进点

1. **分层清晰** - Controller -> Service -> Repository
2. **面向接口** - 使用Repository接口
3. **事务管理** - @Transactional注解
4. **异常统一处理** - GlobalExceptionHandler
5. **响应统一格式** - ApiResponse封装
6. **参数校验** - JSR-303注解
7. **前后端分离** - REST API + SPA
8. **状态管理** - Vuex集中管理
9. **路由守卫** - 登录拦截

## 注意事项

1. 首次运行需要初始化数据库表结构（JPA自动创建）
2. 确保密钥文件存在于配置目录
3. 前端开发服务器默认端口3000
4. 后端API默认端口8080
