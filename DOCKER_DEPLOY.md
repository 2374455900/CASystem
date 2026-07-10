# CA System Docker 部署指南

## 项目结构

```
CaSystem/
├── backend/              # Spring Boot 后端
├── frontend/             # Vue 3 前端
├── docker-compose.yml    # Docker Compose 配置（已整合前后端 Dockerfile）
├── .env.example          # 环境变量示例
└── DOCKER_DEPLOY.md      # 本文件
```

### 快速开始

### 1. 环境要求

- Docker 20.10+
- Docker Compose 2.0+
- Git (可选，用于更新)

### 2. 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑 .env 文件，配置您的数据库密码等信息
```

#### 手动部署（单文件部署）:

```bash
# 仅需 docker-compose.yml 一个文件即可部署

# 1. 构建并启动所有服务
docker-compose up -d --build

# 2. 查看服务状态
docker-compose ps

# 3. 查看日志
docker-compose logs -f
```

> **单文件部署优势**：
> - 无需维护多个 Dockerfile 文件
> - 构建配置集中管理，便于维护
> - 版本控制更简单
> - 部署流程更简洁

## 访问服务

- **前端界面**: <http://localhost>
- **后端API**: <http://localhost:8088>

## 环境变量说明

| 变量名                   | 默认值                    | 说明            |
| --------------------- | ---------------------- | ------------- |
| MYSQL\_ROOT\_PASSWORD | root123                | MySQL root 密码 |
| MYSQL\_DATABASE       | ca\_system             | 数据库名          |
| MYSQL\_USER           | ca\_user               | 数据库用户名        |
| MYSQL\_PASSWORD       | ca\_pass123            | 数据库密码         |
| MYSQL\_PORT           | 3306                   | MySQL 端口      |
| BACKEND\_PORT         | 8088                   | 后端服务端口        |
| FRONTEND\_PORT        | 80                     | 前端服务端口        |
| MAIL\_ENABLED         | false                  | 是否启用邮件通知      |
| MAIL\_FROM            | <noreply@casystem.com> | 发件人地址         |
| MAIL\_SMTP\_HOST      | smtp.qq.com            | SMTP 服务器      |
| MAIL\_SMTP\_PORT      | 587                    | SMTP 端口       |
| MAIL\_USERNAME        | <br />                 | 邮箱用户名         |
| MAIL\_PASSWORD        | <br />                 | 邮箱密码/授权码      |
| REMINDER\_DAYS        | 30,15,7                | 证书到期提醒天数      |

## 数据持久化

Docker Compose 配置了以下数据卷：

- `mysql_data`: MySQL 数据库数据
- `ca_keys`: CA 密钥存储
- `ca_downloads`: 证书下载目录
- `ca_logs`: 应用日志

## 备份与恢复

### 备份

```bash
./deploy.sh backup
# 或
.\deploy.ps1 backup
```

备份文件保存在 `backups/YYYYMMDD_HHMMSS/` 目录下。

### 恢复

```bash
# 1. 停止服务
docker-compose down

# 2. 恢复数据库
docker exec -i ca-mysql mysql -u root -p ca_system < backups/YYYYMMDD_HHMMSS/database.sql

# 3. 恢复密钥和证书
docker run --rm -v ca_keys:/data -v $(pwd)/backups/YYYYMMDD_HHMMSS:/backup alpine sh -c "cd /data && tar xzf /backup/keys.tar.gz"

# 4. 启动服务
docker-compose up -d
```

## 更新部署

```bash
./deploy.sh update
# 或
.\deploy.ps1 update
```

这将拉取最新代码并重新构建镜像。

## 故障排查

### 1. 服务无法启动

```bash
# 查看详细日志
docker-compose logs -f [service_name]

# 检查容器状态
docker-compose ps
```

### 2. 数据库连接失败

- 检查 `.env` 中的数据库配置
- 确认 MySQL 容器已启动: `docker-compose ps mysql`
- 查看 MySQL 日志: `docker-compose logs mysql`

### 3. 端口冲突

修改 `.env` 文件中的端口配置：

```env
MYSQL_PORT=3307
BACKEND_PORT=8089
FRONTEND_PORT=8080
```

### 4. 清理并重新部署

```bash
# 警告：这将删除所有数据！
./deploy.sh cleanup
./deploy.sh deploy
```

## 生产环境建议

1. **修改默认密码**: 务必修改所有默认密码
2. **启用HTTPS**: 配置 SSL 证书
3. **配置防火墙**: 只开放必要的端口
4. **定期备份**: 设置自动备份任务
5. **监控**: 配置日志监控和告警
6. **资源限制**: 在 docker-compose.yml 中添加资源限制

## 技术栈

- **后端**: Spring Boot 3.2.0, Java 17, MySQL 8.0
- **前端**: Vue 3, Vite, Nginx
- **安全**: JWT, Spring Security, BouncyCastle
- **部署**: Docker Compose (单文件部署)

