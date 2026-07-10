# CA证书管理系统 (CA Certificate Management System)

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.0-green" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue.js-3.4.0-blue" alt="Vue.js">
  <img src="https://img.shields.io/badge/Java-17-orange" alt="Java">
  <img src="https://img.shields.io/badge/Node.js-20+-brightgreen" alt="Node.js">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue" alt="MySQL">
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License">
</p>

<p align="center">
  <b>企业级数字证书生命周期管理平台</b><br>
  支持证书申请、签发、吊销、验证及数字签名等完整PKI功能
</p>

---

## 📋 目录

- [项目简介](#-项目简介)
- [功能特性](#-功能特性)
- [技术架构](#-技术架构)
- [环境要求](#-环境要求)
- [快速部署](#-快速部署)
- [项目结构](#-项目结构)
- [使用说明](#-使用说明)
- [API文档](#-api文档)
- [注意事项](#-注意事项)
- [常见问题](#-常见问题)
- [贡献指南](#-贡献指南)
- [许可证](#-许可证)

---

## 🎯 项目简介

CA证书管理系统是一个基于现代Web技术栈构建的企业级数字证书生命周期管理平台。系统采用前后端分离架构，实现了完整的PKI（公钥基础设施）功能，包括用户管理、证书申请、证书签发、证书吊销、证书验证、数字签名与验签等核心功能。

### 设计目标

- **安全性**：采用JWT认证、BCrypt密码加密等多重安全机制
- **可扩展性**：模块化设计，支持水平扩展和微服务架构演进
- **易用性**：现代化UI设计，直观的操作流程，完善的错误提示
- **可维护性**：清晰的代码结构，完善的日志记录，详细的审计追踪

### 适用场景

- 企业内部数字证书管理
- 电子政务系统证书服务
- 物联网设备证书颁发
- 开发测试环境证书管理
- 教育培训演示用途

---

## ✨ 功能特性

### 核心功能

| 功能模块 | 功能描述 | 状态 |
|---------|---------|------|
| 🔐 用户管理 | 用户注册、登录、权限管理 | ✅ |
| 📜 证书申请 | 在线填写申请信息，自动生成密钥对 | ✅ |
| ✍️ 证书签发 | CA签名颁发X.509标准证书 | ✅ |
| 🚫 证书吊销 | 支持证书吊销并生成CRL列表 | ✅ |
| ✅ 证书验证 | 验证证书有效性、签名真实性 | ✅ |
| 📝 数字签名 | 文件数字签名与验证 | ✅ |
| 📊 审计日志 | 完整的操作日志记录与查询 | ✅ |
| 📧 到期提醒 | 证书到期邮件提醒服务 | ✅ |
| 🔑 密钥管理 | 用户密钥对生成与管理 | ✅ |

### 安全特性

- **JWT Token认证**：无状态认证机制，支持Token刷新
- **BCrypt密码加密**：安全的密码存储方案
- **审计日志**：记录所有关键操作，支持追溯
- **验证码保护**：登录注册防暴力破解
- **HTTPS支持**：支持SSL/TLS加密传输

---

## 🏗️ 技术架构

### 系统架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        前端层 (Frontend)                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   Vue 3     │  │  Vue Router │  │      Vuex           │  │
│  │  (Composition│  │  (路由管理)  │  │    (状态管理)        │  │
│  │   API)      │  │             │  │                     │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      网关/代理层 (Nginx)                      │
│              反向代理、负载均衡、静态资源服务                   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       后端层 (Backend)                        │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Spring Boot 3.2.0                       │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌─────────┐ │   │
│  │  │Controller│ │  Service │ │Repository│ │  Entity │ │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └─────────┘ │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌─────────┐ │   │
│  │  │ Spring   │ │  Spring  │ │   JWT    │ │Bouncy   │ │   │
│  │  │ Security │ │   Data   │ │  Token   │ │Castle   │ │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └─────────┘ │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      数据层 (Data Layer)                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   MySQL 8.0 │  │   Druid     │  │   JPA/Hibernate     │  │
│  │  (主数据库)  │  │ (连接池)     │  │    (ORM框架)         │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### 技术栈详情

#### 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.2.0 | 核心框架 |
| Spring Security | 6.x | 安全认证 |
| Spring Data JPA | 3.x | 数据访问 |
| MySQL | 8.0 | 关系型数据库 |
| Druid | 1.2.20 | 数据库连接池 |
| Bouncy Castle | 1.78 | 密码学库 |
| JWT | 0.12.3 | Token认证 |
| Lombok | 1.18.x | 代码简化 |
| Maven | 3.9+ | 构建工具 |

#### 前端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue.js | 3.4.0 | 前端框架 |
| Vue Router | 4.2.5 | 路由管理 |
| Vuex | 4.1.0 | 状态管理 |
| Vite | 5.0+ | 构建工具 |
| Axios | 1.6.2 | HTTP客户端 |
| JSEncrypt | 3.3.2 | RSA加密 |

---

## 💻 环境要求

### 开发环境

| 组件 | 最低版本 | 推荐版本 |
|------|---------|---------|
| JDK | 17 | 17 LTS |
| Node.js | 18.x | 20 LTS |
| MySQL | 8.0 | 8.0+ |
| Maven | 3.8 | 3.9+ |
| npm | 9.x | 10.x |

### 运行环境

| 组件 | 要求 |
|------|------|
| 操作系统 | Linux / Windows / macOS |
| Docker | 20.10+ (Docker部署方式) |
| Docker Compose | 2.0+ (Docker部署方式) |
| 内存 | 最低4GB，推荐8GB+ |
| 磁盘 | 最低20GB可用空间 |

### 浏览器兼容性

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

---

## 🚀 快速部署

### 方式一：Docker部署（推荐）

#### 1. 环境准备

确保已安装 Docker 和 Docker Compose：

```bash
# 检查Docker版本
docker --version
docker-compose --version
```

#### 2. 克隆项目

```bash
git clone <repository-url>
cd CaSystem
```

#### 3. 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑 .env 文件，配置必要参数
vim .env
```

#### 4. 启动服务

使用 Docker Compose 直接启动所有服务：

```bash
# 构建并启动所有服务（首次部署）
docker-compose up -d --build

# 仅启动服务（已构建过镜像）
docker-compose up -d
```

#### 5. 验证部署

```bash
# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

访问地址：
- 前端界面：http://localhost
- 后端API：http://localhost:8088

---

### 方式二：手动部署

#### 1. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE ca_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 执行初始化脚本
USE ca_system;
SOURCE backend/src/main/resources/ca.sql;
```

#### 2. 后端部署

```bash
# 进入后端目录
cd backend

# 修改数据库配置
vim src/main/resources/application.yml

# 编译打包
mvn clean package -DskipTests

# 运行应用
java -jar target/ca-system.jar
```

#### 3. 前端部署

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 开发模式运行
npm run dev

# 生产构建
npm run build
```

---

## 📁 项目结构

```
CaSystem/
├── backend/                    # 后端服务
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/kuugae/ca/
│   │   │   │   ├── annotation/     # 自定义注解
│   │   │   │   ├── aspect/         # AOP切面
│   │   │   │   ├── config/         # 配置类
│   │   │   │   ├── controller/     # 控制器层
│   │   │   │   ├── dto/            # 数据传输对象
│   │   │   │   ├── entity/         # 实体类
│   │   │   │   ├── exception/      # 异常处理
│   │   │   │   ├── repository/     # 数据访问层
│   │   │   │   ├── security/       # 安全相关
│   │   │   │   ├── service/        # 业务逻辑层
│   │   │   │   └── utils/          # 工具类
│   │   │   └── resources/
│   │   │       ├── db/migration/   # 数据库迁移脚本
│   │   │       └── application.yml # 应用配置
│   │   └── test/                   # 测试代码
│   └── pom.xml                     # Maven配置
├── frontend/                   # 前端应用
│   ├── src/
│   │   ├── api/                # API接口
│   │   ├── assets/             # 静态资源
│   │   ├── router/             # 路由配置
│   │   ├── store/              # 状态管理
│   │   ├── styles/             # 样式文件
│   │   ├── utils/              # 工具函数
│   │   ├── views/              # 页面组件
│   │   ├── App.vue             # 根组件
│   │   └── main.js             # 入口文件
│   ├── package.json            # npm配置
│   └── vite.config.js          # Vite配置
├── docs/                       # 项目文档
├── docker-compose.yml          # Docker编排配置
├── .env.example                # 环境变量模板
└── README.md                   # 项目说明
```

---

## 📖 使用说明

### 1. 系统初始化

首次访问系统时，需要创建管理员账户：

1. 访问 http://localhost
2. 点击"注册"创建管理员账号
3. 在数据库中将用户权限设置为管理员：
   ```sql
   UPDATE users SET authority = '1' WHERE username = 'admin';
   ```

### 2. 用户操作流程

```
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│  注册   │───▶│  登录   │───▶│ 申请证书 │───▶│ 下载证书 │
└─────────┘    └─────────┘    └─────────┘    └─────────┘
                                                  │
                                                  ▼
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│ 证书验证 │◀───│ 数字签名 │◀───│ 密钥管理 │◀───│ 使用证书 │
└─────────┘    └─────────┘    └─────────┘    └─────────┘
```

### 3. 证书申请流程

1. **登录系统**：使用注册的账号密码登录
2. **进入申请页面**：点击"申请证书"
3. **填写信息**：
   - 组织机构名称
   - 工商注册号
   - 法人姓名
   - 经办人信息
   - 有效期选择
4. **生成密钥**：系统自动生成RSA密钥对
5. **提交申请**：等待系统签发证书
6. **下载证书**：在证书列表中下载证书文件

### 4. 常用命令

#### Docker Compose 命令

```bash
# 首次部署：构建并启动所有服务
docker-compose up -d --build

# 启动所有服务（已构建过镜像）
docker-compose up -d

# 停止服务
docker-compose down

# 停止服务并删除数据卷（谨慎使用）
docker-compose down -v

# 重启服务
docker-compose restart

# 查看所有服务状态
docker-compose ps

# 查看所有服务日志
docker-compose logs -f

# 查看指定服务日志
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql

# 重建并启动服务（代码更新后）
docker-compose up -d --build

# 进入容器内部
docker-compose exec backend sh
docker-compose exec mysql bash

# 备份数据库
docker-compose exec mysql mysqldump -u root -p ca_system > backup.sql
```

#### 后端开发命令

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 打包应用
mvn clean package

# 运行应用
mvn spring-boot:run
```

#### 前端开发命令

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 生产构建
npm run build

# 代码检查
npm run lint

# 预览生产构建
npm run preview
```

---

## 🔌 API文档

### 认证相关

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/auth/register | 用户注册 |
| POST | /api/auth/login | 用户登录 |
| POST | /api/auth/logout | 用户登出 |
| POST | /api/auth/refresh | 刷新Token |

### 证书管理

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/certificates/apply | 申请证书 |
| GET | /api/certificates | 获取证书列表 |
| GET | /api/certificates/{id} | 获取证书详情 |
| POST | /api/certificates/{id}/revoke | 吊销证书 |
| GET | /api/certificates/{id}/download | 下载证书 |

### 签名验证

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/signature/generate | 生成数字签名 |
| POST | /api/signature/verify | 验证数字签名 |
| POST | /api/validation/certificate | 验证证书有效性 |

### 管理员接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/admin/users | 用户管理 |
| GET | /api/admin/audit-logs | 审计日志查询 |
| GET | /api/admin/statistics | 系统统计 |

---

## ⚠️ 注意事项

### 安全注意事项

1. **生产环境部署**
   - 务必修改默认的数据库密码
   - 使用HTTPS协议传输敏感数据
   - 配置防火墙规则，限制不必要的端口访问
   - 定期备份数据库和密钥文件

2. **密钥管理**
   - 私钥文件具有最高安全级别，请妥善保管
   - 建议将密钥存储在专用密钥管理系统(KMS)中
   - 定期轮换密钥对

3. **审计日志**
   - 系统记录所有关键操作，请定期审查
   - 审计日志应存储在安全位置，防止篡改

### 性能注意事项

1. **数据库优化**
   - 建议为常用查询字段添加索引
   - 定期清理过期数据
   - 配置连接池参数以适应实际负载

2. **文件存储**
   - 证书文件建议存储在对象存储服务中
   - 配置CDN加速证书下载

---

## ❓ 常见问题

### Q1: 部署时数据库连接失败？

**解决方案：**
1. 检查MySQL服务是否启动
2. 验证数据库配置信息（用户名、密码、数据库名）
3. 确认MySQL允许远程连接
4. 检查防火墙设置

### Q2: 前端无法访问后端API？

**解决方案：**
1. 确认后端服务已启动
2. 检查前端代理配置（vite.config.js）
3. 验证跨域配置是否正确
4. 查看浏览器控制台网络请求

### Q3: 证书生成失败？

**解决方案：**
1. 检查密钥目录权限
2. 确认Bouncy Castle库已正确加载
3. 查看后端日志获取详细错误信息
4. 验证输入参数是否符合规范

### Q4: 邮件提醒功能不工作？

**解决方案：**
1. 确认MAIL_ENABLED设置为true
2. 检查SMTP服务器配置
3. 验证发件邮箱已开启SMTP服务
4. 查看日志中的邮件发送记录

### Q5: 如何重置管理员密码？

**解决方案：**
```sql
-- 生成新的BCrypt密码（使用在线工具或代码生成）
-- 更新数据库
UPDATE users SET password = '$2a$10$...' WHERE username = 'admin';
```

---

## 🤝 贡献指南

我们欢迎所有形式的贡献，包括但不限于：

- 提交Bug报告
- 提出新功能建议
- 改进文档
- 提交代码修复
- 分享使用经验

### 贡献流程

1. **Fork项目**：Fork本仓库到您的GitHub账号
2. **创建分支**：`git checkout -b feature/your-feature-name`
3. **提交更改**：`git commit -m 'Add some feature'`
4. **推送分支**：`git push origin feature/your-feature-name`
5. **创建Pull Request**：提交PR并描述您的更改

### 代码规范

- 遵循Java代码规范（Google Java Style）
- 前端代码遵循ESLint规则
- 提交信息使用中文或英文，描述清晰
- 新增功能需包含单元测试

---

## 📄 许可证

本项目采用 [MIT License](LICENSE) 开源许可证。

```
MIT License

Copyright (c) 2024 CA Certificate Management System

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 📞 联系方式

如有问题或建议，欢迎通过以下方式联系我们：

- 项目Issues：[GitHub Issues](https://github.com/your-repo/issues)
- 邮箱：support@casystem.com

---

<p align="center">
  <b>Made with ❤️ by CA System Team</b>
</p>
