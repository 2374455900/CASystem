# CA System Docker 部署脚本 (PowerShell版本)
# 适用于 Windows PowerShell 和 PowerShell Core

param(
    [Parameter(Position=0)]
    [ValidateSet("deploy", "start", "stop", "restart", "status", "logs", "update", "cleanup", "backup", "menu", "")]
    [string]$Action = ""
)

# 颜色定义
function Write-Info { param($Message) Write-Host "[INFO] $Message" -ForegroundColor Cyan }
function Write-Success { param($Message) Write-Host "[SUCCESS] $Message" -ForegroundColor Green }
function Write-Warning { param($Message) Write-Host "[WARNING] $Message" -ForegroundColor Yellow }
function Write-Error { param($Message) Write-Host "[ERROR] $Message" -ForegroundColor Red }

# 检查Docker环境
function Test-DockerEnvironment {
    Write-Info "检查 Docker 环境..."
    
    if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
        Write-Error "Docker 未安装，请先安装 Docker"
        exit 1
    }
    
    if (-not (Get-Command docker-compose -ErrorAction SilentlyContinue)) {
        # 尝试使用 docker compose (v2)
        if (-not (docker compose version 2>$null)) {
            Write-Error "Docker Compose 未安装，请先安装 Docker Compose"
            exit 1
        }
    }
    
    try {
        $null = docker info 2>$null
    } catch {
        Write-Error "Docker 服务未运行，请启动 Docker 服务"
        exit 1
    }
    
    Write-Success "Docker 环境检查通过"
}

# 检查.env文件
function Test-EnvironmentFile {
    Write-Info "检查环境变量配置..."
    if (-not (Test-Path .env)) {
        if (Test-Path .env.example) {
            Write-Warning ".env 文件不存在，从 .env.example 复制"
            Copy-Item .env.example .env
            Write-Warning "请编辑 .env 文件配置您的环境变量后再运行部署"
            exit 1
        } else {
            Write-Error ".env.example 文件不存在"
            exit 1
        }
    }
    Write-Success "环境变量配置检查通过"
}

# 获取docker-compose命令
function Get-DockerComposeCommand {
    if (Get-Command docker-compose -ErrorAction SilentlyContinue) {
        return "docker-compose"
    } else {
        return "docker compose"
    }
}

$DockerCompose = Get-DockerComposeCommand

# 显示菜单
function Show-Menu {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "    CA System Docker 部署管理脚本" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  1) 首次部署 (构建并启动所有服务)"
    Write-Host "  2) 启动服务"
    Write-Host "  3) 停止服务"
    Write-Host "  4) 重启服务"
    Write-Host "  5) 查看服务状态"
    Write-Host "  6) 查看日志"
    Write-Host "  7) 更新部署 (拉取最新代码并重建)"
    Write-Host "  8) 清理数据 (删除所有数据卷)"
    Write-Host "  9) 备份数据"
    Write-Host "  0) 退出"
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
}

# 首次部署
function Deploy-CASystem {
    Write-Info "开始首次部署..."
    
    Test-DockerEnvironment
    Test-EnvironmentFile
    
    # 创建必要的目录
    New-Item -ItemType Directory -Force -Path docker | Out-Null
    
    Write-Info "构建并启动服务..."
    Invoke-Expression "$DockerCompose up -d --build"
    
    Write-Info "等待服务启动..."
    Start-Sleep -Seconds 10
    
    # 检查服务状态
    Get-ServiceStatus
    
    Write-Success "部署完成！"
    Write-Info "前端访问地址: http://localhost"
    Write-Info "后端API地址: http://localhost:8088"
}

# 启动服务
function Start-Services {
    Write-Info "启动服务..."
    Invoke-Expression "$DockerCompose up -d"
    Write-Success "服务已启动"
}

# 停止服务
function Stop-Services {
    Write-Info "停止服务..."
    Invoke-Expression "$DockerCompose down"
    Write-Success "服务已停止"
}

# 重启服务
function Restart-Services {
    Write-Info "重启服务..."
    Invoke-Expression "$DockerCompose restart"
    Write-Success "服务已重启"
}

# 查看服务状态
function Get-ServiceStatus {
    Write-Info "服务状态:"
    Invoke-Expression "$DockerCompose ps"
}

# 查看日志
function View-Logs {
    Write-Host "选择查看日志的服务:"
    Write-Host "  1) 所有服务"
    Write-Host "  2) MySQL"
    Write-Host "  3) 后端 (Backend)"
    Write-Host "  4) 前端 (Frontend)"
    
    $logChoice = Read-Host "请输入选项 [1-4]"
    
    switch ($logChoice) {
        "1" { Invoke-Expression "$DockerCompose logs -f" }
        "2" { Invoke-Expression "$DockerCompose logs -f mysql" }
        "3" { Invoke-Expression "$DockerCompose logs -f backend" }
        "4" { Invoke-Expression "$DockerCompose logs -f frontend" }
        default { Write-Error "无效选项" }
    }
}

# 更新部署
function Update-Deploy {
    Write-Info "更新部署..."
    
    Write-Info "停止当前服务..."
    Invoke-Expression "$DockerCompose down"
    
    Write-Info "拉取最新代码..."
    try {
        git pull origin main 2>$null
    } catch {
        Write-Warning "非Git仓库或拉取失败，跳过代码更新"
    }
    
    Write-Info "重新构建并启动服务..."
    Invoke-Expression "$DockerCompose up -d --build"
    
    Write-Success "更新完成"
}

# 清理数据
function Clear-Data {
    Write-Warning "警告: 此操作将删除所有数据，包括数据库数据！"
    $confirm = Read-Host "确定要继续吗? (yes/no)"
    
    if ($confirm -eq "yes") {
        Write-Info "停止服务并删除数据卷..."
        Invoke-Expression "$DockerCompose down -v"
        Write-Success "数据已清理"
    } else {
        Write-Info "操作已取消"
    }
}

# 备份数据
function Backup-Data {
    $backupDir = "backups/$(Get-Date -Format 'yyyyMMdd_HHmmss')"
    New-Item -ItemType Directory -Force -Path $backupDir | Out-Null
    
    Write-Info "开始备份数据到 $backupDir..."
    
    # 读取环境变量
    $envContent = Get-Content .env -ErrorAction SilentlyContinue
    $rootPassword = "root123"
    $database = "ca_system"
    
    foreach ($line in $envContent) {
        if ($line -match "^MYSQL_ROOT_PASSWORD=(.+)$") {
            $rootPassword = $matches[1]
        }
        if ($line -match "^MYSQL_DATABASE=(.+)$") {
            $database = $matches[1]
        }
    }
    
    # 备份数据库
    Write-Info "备份数据库..."
    try {
        docker exec ca-mysql mysqldump -u root -p$rootPassword $database > "$backupDir/database.sql" 2>$null
        Write-Success "数据库备份完成"
    } catch {
        Write-Warning "数据库备份失败"
    }
    
    # 备份密钥和证书
    Write-Info "备份密钥和证书..."
    try {
        docker run --rm -v ca_keys:/data -v "$(Resolve-Path $backupDir):/backup" alpine tar czf /backup/keys.tar.gz -C /data . 2>$null
        Write-Success "密钥备份完成"
    } catch {
        Write-Warning "密钥备份失败"
    }
    
    # 备份下载的证书
    Write-Info "备份下载的证书..."
    try {
        docker run --rm -v ca_downloads:/data -v "$(Resolve-Path $backupDir):/backup" alpine tar czf /backup/downloads.tar.gz -C /data . 2>$null
        Write-Success "证书备份完成"
    } catch {
        Write-Warning "证书备份失败"
    }
    
    # 备份.env文件
    try {
        Copy-Item .env "$backupDir/env_backup"
        Write-Success ".env备份完成"
    } catch {
        Write-Warning ".env备份失败"
    }
    
    Write-Success "备份完成: $backupDir"
}

# 主程序
function Main {
    # 如果直接传入参数，执行对应功能
    if ($Action -and $Action -ne "") {
        switch ($Action) {
            "deploy" { Deploy-CASystem }
            "start" { Start-Services }
            "stop" { Stop-Services }
            "restart" { Restart-Services }
            "status" { Get-ServiceStatus }
            "logs" { View-Logs }
            "update" { Update-Deploy }
            "cleanup" { Clear-Data }
            "backup" { Backup-Data }
            default {
                Write-Error "未知操作: $Action"
                Write-Host "用法: .\deploy.ps1 {deploy|start|stop|restart|status|logs|update|cleanup|backup}"
                exit 1
            }
        }
        return
    }
    
    # 交互式菜单
    while ($true) {
        Show-Menu
        $choice = Read-Host "请输入选项 [0-9]"
        
        switch ($choice) {
            "1" { Deploy-CASystem }
            "2" { Start-Services }
            "3" { Stop-Services }
            "4" { Restart-Services }
            "5" { Get-ServiceStatus }
            "6" { View-Logs }
            "7" { Update-Deploy }
            "8" { Clear-Data }
            "9" { Backup-Data }
            "0" { 
                Write-Info "退出脚本"
                exit 0 
            }
            default { Write-Error "无效选项，请重新输入" }
        }
        
        Write-Host ""
        Read-Host "按回车键继续..."
    }
}

# 运行主程序
Main
