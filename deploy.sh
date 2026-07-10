#!/bin/bash

# CA System Docker 部署脚本
# 支持: Linux/macOS/WSL

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的信息
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查Docker是否安装
check_docker() {
    print_info "检查 Docker 环境..."
    if ! command -v docker &> /dev/null; then
        print_error "Docker 未安装，请先安装 Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose 未安装，请先安装 Docker Compose"
        exit 1
    fi
    
    # 检查Docker服务是否运行
    if ! docker info &> /dev/null; then
        print_error "Docker 服务未运行，请启动 Docker 服务"
        exit 1
    fi
    
    print_success "Docker 环境检查通过"
}

# 检查.env文件
check_env() {
    print_info "检查环境变量配置..."
    if [ ! -f .env ]; then
        if [ -f .env.example ]; then
            print_warning ".env 文件不存在，从 .env.example 复制"
            cp .env.example .env
            print_warning "请编辑 .env 文件配置您的环境变量后再运行部署"
            exit 1
        else
            print_error ".env.example 文件不存在"
            exit 1
        fi
    fi
    print_success "环境变量配置检查通过"
}

# 显示菜单
show_menu() {
    echo ""
    echo "========================================"
    echo "    CA System Docker 部署管理脚本"
    echo "========================================"
    echo ""
    echo "  1) 首次部署 (构建并启动所有服务)"
    echo "  2) 启动服务"
    echo "  3) 停止服务"
    echo "  4) 重启服务"
    echo "  5) 查看服务状态"
    echo "  6) 查看日志"
    echo "  7) 更新部署 (拉取最新代码并重建)"
    echo "  8) 清理数据 (删除所有数据卷)"
    echo "  9) 备份数据"
    echo "  0) 退出"
    echo ""
    echo "========================================"
}

# 首次部署
deploy() {
    print_info "开始首次部署..."
    
    check_docker
    check_env
    
    # 创建必要的目录
    mkdir -p docker
    
    print_info "构建并启动服务..."
    docker-compose up -d --build
    
    print_info "等待服务启动..."
    sleep 10
    
    # 检查服务状态
    check_services
    
    print_success "部署完成！"
    print_info "前端访问地址: http://localhost"
    print_info "后端API地址: http://localhost:8088"
}

# 启动服务
start_services() {
    print_info "启动服务..."
    docker-compose up -d
    print_success "服务已启动"
}

# 停止服务
stop_services() {
    print_info "停止服务..."
    docker-compose down
    print_success "服务已停止"
}

# 重启服务
restart_services() {
    print_info "重启服务..."
    docker-compose restart
    print_success "服务已重启"
}

# 查看服务状态
check_services() {
    print_info "服务状态:"
    docker-compose ps
}

# 查看日志
view_logs() {
    echo "选择查看日志的服务:"
    echo "  1) 所有服务"
    echo "  2) MySQL"
    echo "  3) 后端 (Backend)"
    echo "  4) 前端 (Frontend)"
    read -p "请输入选项 [1-4]: " log_choice
    
    case $log_choice in
        1)
            docker-compose logs -f
            ;;
        2)
            docker-compose logs -f mysql
            ;;
        3)
            docker-compose logs -f backend
            ;;
        4)
            docker-compose logs -f frontend
            ;;
        *)
            print_error "无效选项"
            ;;
    esac
}

# 更新部署
update_deploy() {
    print_info "更新部署..."
    
    print_info "停止当前服务..."
    docker-compose down
    
    print_info "拉取最新代码..."
    git pull origin main 2>/dev/null || print_warning "非Git仓库或拉取失败，跳过代码更新"
    
    print_info "重新构建并启动服务..."
    docker-compose up -d --build
    
    print_success "更新完成"
}

# 清理数据
cleanup() {
    print_warning "警告: 此操作将删除所有数据，包括数据库数据！"
    read -p "确定要继续吗? (yes/no): " confirm
    
    if [ "$confirm" = "yes" ]; then
        print_info "停止服务并删除数据卷..."
        docker-compose down -v
        print_success "数据已清理"
    else
        print_info "操作已取消"
    fi
}

# 备份数据
backup() {
    local backup_dir="backups/$(date +%Y%m%d_%H%M%S)"
    mkdir -p "$backup_dir"
    
    print_info "开始备份数据到 $backup_dir..."
    
    # 备份数据库
    print_info "备份数据库..."
    docker exec ca-mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD:-root123} ${MYSQL_DATABASE:-ca_system} > "$backup_dir/database.sql" 2>/dev/null || print_warning "数据库备份失败"
    
    # 备份密钥和证书
    print_info "备份密钥和证书..."
    docker run --rm -v ca_keys:/data -v "$(pwd)/$backup_dir:/backup" alpine tar czf /backup/keys.tar.gz -C /data . 2>/dev/null || print_warning "密钥备份失败"
    
    # 备份下载的证书
    print_info "备份下载的证书..."
    docker run --rm -v ca_downloads:/data -v "$(pwd)/$backup_dir:/backup" alpine tar czf /backup/downloads.tar.gz -C /data . 2>/dev/null || print_warning "证书备份失败"
    
    # 备份.env文件
    cp .env "$backup_dir/env_backup" 2>/dev/null || print_warning ".env备份失败"
    
    print_success "备份完成: $backup_dir"
}

# 主程序
main() {
    # 如果直接传入参数，执行对应功能
    if [ $# -gt 0 ]; then
        case $1 in
            deploy)
                deploy
                ;;
            start)
                start_services
                ;;
            stop)
                stop_services
                ;;
            restart)
                restart_services
                ;;
            status)
                check_services
                ;;
            logs)
                view_logs
                ;;
            update)
                update_deploy
                ;;
            cleanup)
                cleanup
                ;;
            backup)
                backup
                ;;
            *)
                echo "用法: $0 {deploy|start|stop|restart|status|logs|update|cleanup|backup}"
                exit 1
                ;;
        esac
        exit 0
    fi
    
    # 交互式菜单
    while true; do
        show_menu
        read -p "请输入选项 [0-9]: " choice
        
        case $choice in
            1)
                deploy
                ;;
            2)
                start_services
                ;;
            3)
                stop_services
                ;;
            4)
                restart_services
                ;;
            5)
                check_services
                ;;
            6)
                view_logs
                ;;
            7)
                update_deploy
                ;;
            8)
                cleanup
                ;;
            9)
                backup
                ;;
            0)
                print_info "退出脚本"
                exit 0
                ;;
            *)
                print_error "无效选项，请重新输入"
                ;;
        esac
        
        echo ""
        read -p "按回车键继续..."
    done
}

# 运行主程序
main "$@"
