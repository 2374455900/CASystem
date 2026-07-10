<template>
    <div class="user-management">
        <!-- 页面头部 -->
        <div class="page-header">
            <div class="header-content">
                <div class="header-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
                        <circle cx="9" cy="7" r="4" />
                        <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
                        <path d="M16 3.13a4 4 0 0 1 0 7.75" />
                    </svg>
                </div>
                <div class="header-text">
                    <h1>用户管理</h1>
                    <p>管理系统用户，查看用户详情和账户状态</p>
                </div>
            </div>
            <div class="header-stats">
                <div class="stat-card">
                    <div class="stat-content">
                        <span class="stat-number">{{ total }}</span>
                        <span class="stat-label">总用户</span>
                    </div>
                    <div class="stat-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
                            <circle cx="9" cy="7" r="4" />
                            <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
                            <path d="M16 3.13a4 4 0 0 1 0 7.75" />
                        </svg>
                    </div>
                </div>
            </div>
        </div>

        <!-- 工具栏 -->
        <div class="toolbar">
            <div class="search-section">
                <div class="search-box">
                    <div class="search-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <circle cx="11" cy="11" r="8" />
                            <path d="m21 21-4.35-4.35" />
                        </svg>
                    </div>
                    <input v-model="searchQuery" type="text" placeholder="搜索用户名或邮箱..." @keyup.enter="handleSearch" />
                    <button class="search-btn" @click="handleSearch">
                        <span>搜索</span>
                    </button>
                </div>
                <div class="filter-tags">
                    <button v-for="filter in filters" :key="filter.value"
                        :class="['filter-tag', { active: activeFilter === filter.value }]"
                        @click="activeFilter = filter.value; handleSearch()">
                        {{ filter.label }}
                    </button>
                </div>
            </div>
            <div class="action-section">
                <button @click="exportData" class="btn-export">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                        <polyline points="7 10 12 15 17 10" />
                        <line x1="12" y1="15" x2="12" y2="3" />
                    </svg>
                    <span>导出数据</span>
                </button>
            </div>
        </div>

        <!-- 数据表格 -->
        <div class="table-container">
            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th class="col-checkbox">
                                <label class="checkbox-wrapper">
                                    <input type="checkbox" v-model="selectAll" @change="toggleSelectAll">
                                    <span class="checkmark"></span>
                                </label>
                            </th>
                            <th class="col-user">用户信息</th>
                            <th class="col-email">邮箱</th>
                            <th class="col-role">权限</th>
                            <th class="col-date">注册时间</th>
                            <th class="col-status">状态</th>
                            <th class="col-actions">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="user in users" :key="user.id" :class="{ selected: selectedUsers.includes(user.id) }">
                            <td class="col-checkbox">
                                <label class="checkbox-wrapper">
                                    <input type="checkbox" v-model="selectedUsers" :value="user.id">
                                    <span class="checkmark"></span>
                                </label>
                            </td>
                            <td class="col-user">
                                <div class="user-info">
                                    <div class="user-avatar" :style="{ background: getAvatarGradient(user.id) }">
                                        {{ user.username.charAt(0).toUpperCase() }}
                                    </div>
                                    <div class="user-details">
                                        <span class="user-name">{{ user.username }}</span>
                                        <span class="user-id">ID: {{ user.id }}</span>
                                    </div>
                                </div>
                            </td>
                            <td class="col-email">
                                <div class="email-cell">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z" />
                                        <polyline points="22,6 12,13 2,6" />
                                    </svg>
                                    <span>{{ user.email }}</span>
                                </div>
                            </td>
                            <td class="col-role">
                                <span :class="['role-badge', user.authority === '1' ? 'admin' : 'user']">
                                    <span class="role-dot"></span>
                                    {{ user.authority === '1' ? '管理员' : '普通用户' }}
                                </span>
                            </td>
                            <td class="col-date">
                                <div class="date-cell">
                                    <span class="date-primary">{{ formatDate(user.createdAt) }}</span>
                                    <span class="date-secondary">{{ formatTime(user.createdAt) }}</span>
                                </div>
                            </td>
                            <td class="col-status">
                                <span class="status-badge active">
                                    <span class="status-dot"></span>
                                    正常
                                </span>
                            </td>
                            <td class="col-actions">
                                <div class="action-buttons">
                                    <button @click="viewUser(user)" class="btn-action view" title="查看详情">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                                            <circle cx="12" cy="12" r="3" />
                                        </svg>
                                    </button>
                                    <button @click="deleteUser(user)" class="btn-action delete" title="删除用户"
                                        v-if="user.authority !== '1'">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="3 6 5 6 21 6" />
                                            <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" />
                                        </svg>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- 空状态 -->
            <div v-if="users.length === 0" class="empty-state">
                <div class="empty-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                        <circle cx="12" cy="12" r="10" />
                        <path d="M16 16s-1.5-2-4-2-4 2-4 2" />
                        <line x1="9" y1="9" x2="9.01" y2="9" />
                        <line x1="15" y1="9" x2="15.01" y2="9" />
                    </svg>
                </div>
                <p>暂无用户数据</p>
                <span>尝试调整搜索条件</span>
            </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-wrapper">
            <div class="pagination-info">
                <span>显示 {{ (page - 1) * pageSize + 1 }} - {{ Math.min(page * pageSize, total) }} 条，共 {{ total }} 条</span>
            </div>
            <div class="pagination">
                <button @click="prevPage" :disabled="page === 1" class="page-btn">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="15 18 9 12 15 6" />
                    </svg>
                </button>
                <div class="page-numbers">
                    <button v-for="p in visiblePages" :key="p" :class="['page-number', { active: page === p }]"
                        @click="goToPage(p)">
                        {{ p }}
                    </button>
                </div>
                <button @click="nextPage" :disabled="page >= totalPages" class="page-btn">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="9 18 15 12 9 6" />
                    </svg>
                </button>
            </div>
        </div>

        <!-- 用户详情弹窗 -->
        <Transition name="modal">
            <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
                <div class="modal-content">
                    <div class="modal-header">
                        <div class="modal-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                                <circle cx="12" cy="7" r="4" />
                            </svg>
                        </div>
                        <h2>用户详情</h2>
                        <button class="modal-close" @click="showModal = false">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <line x1="18" y1="6" x2="6" y2="18" />
                                <line x1="6" y1="6" x2="18" y2="18" />
                            </svg>
                        </button>
                    </div>
                    <div class="modal-body" v-if="selectedUser">
                        <div class="user-profile">
                            <div class="profile-avatar" :style="{ background: getAvatarGradient(selectedUser.id) }">
                                {{ selectedUser.username.charAt(0).toUpperCase() }}
                            </div>
                            <div class="profile-info">
                                <h3>{{ selectedUser.username }}</h3>
                                <span :class="['profile-role', selectedUser.authority === '1' ? 'admin' : 'user']">
                                    {{ selectedUser.authority === '1' ? '管理员' : '普通用户' }}
                                </span>
                            </div>
                        </div>
                        <div class="detail-grid">
                            <div class="detail-item">
                                <span class="detail-label">用户 ID</span>
                                <span class="detail-value">{{ selectedUser.id }}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">邮箱地址</span>
                                <span class="detail-value">{{ selectedUser.email }}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">注册时间</span>
                                <span class="detail-value">{{ formatDateTime(selectedUser.createdAt) }}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">账户状态</span>
                                <span class="detail-value">
                                    <span class="status-badge active">
                                        <span class="status-dot"></span>
                                        正常
                                    </span>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button @click="showModal = false" class="btn-primary">关闭</button>
                    </div>
                </div>
            </div>
        </Transition>
    </div>
</template>

<script>
import api from '../../api'

export default {
    name: 'UserManagement',
    data() {
        return {
            users: [],
            searchQuery: '',
            activeFilter: 'all',
            page: 1,
            pageSize: 10,
            total: 0,
            totalPages: 0,
            showModal: false,
            selectedUser: null,
            selectAll: false,
            selectedUsers: [],
            filters: [
                { label: '全部', value: 'all' },
                { label: '管理员', value: 'admin' },
                { label: '普通用户', value: 'user' }
            ]
        }
    },
    computed: {
        visiblePages() {
            const pages = []
            const maxVisible = 5
            let start = Math.max(1, this.page - Math.floor(maxVisible / 2))
            let end = Math.min(this.totalPages, start + maxVisible - 1)

            if (end - start < maxVisible - 1) {
                start = Math.max(1, end - maxVisible + 1)
            }

            for (let i = start; i <= end; i++) {
                pages.push(i)
            }
            return pages
        }
    },
    async mounted() {
        await this.loadUsers()
    },
    methods: {
        async loadUsers() {
            try {
                const params = {
                    page: this.page,
                    pageSize: this.pageSize,
                    keyword: this.searchQuery || undefined
                }
                const response = await api.admin.getUsers(params)
                if (response.code === 200) {
                    this.users = response.data.records
                    this.total = response.data.total
                    this.totalPages = response.data.totalPages
                }
            } catch (error) {
                console.error('加载用户失败', error)
                alert('加载用户失败')
            }
        },
        handleSearch() {
            this.page = 1
            this.loadUsers()
        },
        goToPage(p) {
            this.page = p
            this.loadUsers()
        },
        prevPage() {
            if (this.page > 1) {
                this.page--
                this.loadUsers()
            }
        },
        nextPage() {
            if (this.page < this.totalPages) {
                this.page++
                this.loadUsers()
            }
        },
        viewUser(user) {
            this.selectedUser = user
            this.showModal = true
        },
        async deleteUser(user) {
            if (!confirm(`确定要删除用户 "${user.username}" 吗？此操作不可恢复！`)) {
                return
            }
            try {
                const response = await api.admin.deleteUser(user.id)
                if (response.code === 200) {
                    alert('删除成功')
                    await this.loadUsers()
                } else {
                    alert(response.message || '删除失败')
                }
            } catch (error) {
                console.error('删除用户失败', error)
                alert('删除失败')
            }
        },
        exportData() {
            const csvContent = this.convertToCSV(this.users)
            const blob = new Blob(['\ufeff' + csvContent], { type: 'text/csv;charset=utf-8;' })
            const link = document.createElement('a')
            link.href = URL.createObjectURL(blob)
            link.download = `users_${new Date().toISOString().split('T')[0]}.csv`
            link.click()
        },
        convertToCSV(data) {
            const headers = ['ID', '用户名', '邮箱', '权限', '注册时间']
            const rows = data.map(user => [
                user.id,
                user.username,
                user.email,
                user.authority === '1' ? '管理员' : '普通用户',
                this.formatDateTime(user.createdAt)
            ])
            return [headers.join(','), ...rows.map(r => r.join(','))].join('\n')
        },
        formatDate(dateStr) {
            if (!dateStr) return '-'
            const date = new Date(dateStr)
            return date.toLocaleDateString('zh-CN')
        },
        formatTime(dateStr) {
            if (!dateStr) return '-'
            const date = new Date(dateStr)
            return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
        },
        formatDateTime(dateStr) {
            if (!dateStr) return '-'
            const date = new Date(dateStr)
            return date.toLocaleString('zh-CN')
        },
        getAvatarGradient(id) {
            const gradients = [
                'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
                'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
                'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
                'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
                'linear-gradient(135deg, #30cfd0 0%, #330867 100%)',
                'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)',
                'linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)'
            ]
            return gradients[id % gradients.length]
        },
        toggleSelectAll() {
            if (this.selectAll) {
                this.selectedUsers = this.users.map(u => u.id)
            } else {
                this.selectedUsers = []
            }
        }
    }
}
</script>

<style scoped>
.user-management {
    padding: 0;
    background: var(--ca-bg-secondary);
}

/* 页面头部 */
.page-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 24px;
    padding-bottom: 24px;
    border-bottom: 2px solid var(--ca-border);
}

.header-content {
    display: flex;
    align-items: center;
    gap: 16px;
}

.header-icon {
    width: 72px;
    height: 72px;
    border-radius: var(--ca-radius-xl);
    background: var(--ca-gradient-primary);
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    box-shadow: 0 8px 24px rgba(37, 99, 235, 0.3);
    animation: float 4s cubic-bezier(0.4, 0, 0.2, 1) infinite;
}

@keyframes float {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-8px); }
}

.header-icon svg {
    width: 40px;
    height: 40px;
    color: white;
    stroke: white;
}

.header-text h1 {
    font-size: 24px;
    font-weight: 700;
    color: var(--ca-text-primary);
    margin-bottom: 4px;
    letter-spacing: -0.02em;
}

.header-text p {
    font-size: 14px;
    color: var(--ca-text-secondary);
    line-height: 1.6;
}

.header-stats {
    display: flex;
    gap: 12px;
}

.stat-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 16px 24px;
    background: var(--ca-bg-card);
    border: 1px solid var(--ca-border);
    border-radius: var(--ca-radius-lg);
    box-shadow: var(--ca-shadow-sm);
    transition: all var(--ca-transition-normal);
}

.stat-card:hover {
    transform: translateY(-2px);
    box-shadow: var(--ca-shadow-md);
}

.stat-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
}

.stat-number {
    font-size: 28px;
    font-weight: 700;
    color: var(--ca-text-primary);
    line-height: 1;
}

.stat-label {
    font-size: 12px;
    color: var(--ca-text-tertiary);
    margin-top: 4px;
}

.stat-icon {
    width: 32px;
    height: 32px;
    margin-top: 8px;
    color: var(--ca-primary);
}

.stat-icon svg {
    width: 100%;
    height: 100%;
}

/* 工具栏 */
.toolbar {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    margin-bottom: 24px;
    gap: 16px;
}

.search-section {
    flex: 1;
}

.search-box {
    position: relative;
    display: flex;
    gap: 12px;
    margin-bottom: 12px;
}

.search-icon {
    position: absolute;
    left: 16px;
    top: 50%;
    transform: translateY(-50%);
    color: var(--ca-text-tertiary);
    pointer-events: none;
}

.search-icon svg {
    width: 20px;
    height: 20px;
}

.search-box input {
    flex: 1;
    max-width: 400px;
    padding: 12px 16px 12px 48px;
    background: var(--ca-bg-card);
    border: 1px solid var(--ca-border);
    border-radius: var(--ca-radius-lg);
    color: var(--ca-text-primary);
    font-size: 14px;
    transition: all var(--ca-transition-normal);
    box-shadow: var(--ca-shadow-sm);
}

.search-box input:focus {
    outline: none;
    border-color: var(--ca-primary);
    box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.search-box input::placeholder {
    color: var(--ca-text-tertiary);
}

.search-btn {
    padding: 12px 24px;
    background: var(--ca-gradient-primary);
    border: none;
    border-radius: var(--ca-radius-lg);
    color: white;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: all var(--ca-transition-normal);
    box-shadow: 0 4px 15px rgba(37, 99, 235, 0.3);
}

.search-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(37, 99, 235, 0.4);
}

.filter-tags {
    display: flex;
    gap: 8px;
}

.filter-tag {
    padding: 8px 16px;
    background: var(--ca-bg-card);
    border: 1px solid var(--ca-border);
    border-radius: var(--ca-radius-md);
    color: var(--ca-text-secondary);
    font-size: 13px;
    cursor: pointer;
    transition: all var(--ca-transition-normal);
}

.filter-tag:hover {
    background: var(--ca-bg-hover);
    border-color: var(--ca-border-hover);
    color: var(--ca-primary);
}

.filter-tag.active {
    background: var(--ca-gradient-primary);
    border-color: var(--ca-primary);
    color: white;
}

.action-section {
    display: flex;
    gap: 12px;
}

.btn-export {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 20px;
    background: var(--ca-bg-card);
    border: 1px solid var(--ca-border);
    border-radius: var(--ca-radius-lg);
    color: var(--ca-text-secondary);
    font-size: 14px;
    cursor: pointer;
    transition: all var(--ca-transition-normal);
    box-shadow: var(--ca-shadow-sm);
}

.btn-export:hover {
    background: var(--ca-bg-hover);
    border-color: var(--ca-primary);
    color: var(--ca-primary);
    transform: translateY(-2px);
}

.btn-export svg {
    width: 18px;
    height: 18px;
}

/* 数据表格 */
.table-container {
    background: var(--ca-bg-card);
    border-radius: var(--ca-radius-2xl);
    box-shadow: var(--ca-shadow-sm);
    overflow: hidden;
    border: 1px solid var(--ca-border);
}

.table-wrapper {
    overflow-x: auto;
}

.data-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 14px;
}

.data-table thead {
    background: var(--ca-bg-tertiary);
}

.data-table th {
    padding: 16px;
    text-align: left;
    font-weight: 600;
    color: var(--ca-text-secondary);
    border-bottom: 2px solid var(--ca-border);
    white-space: nowrap;
}

.data-table td {
    padding: 16px;
    border-bottom: 1px solid var(--ca-border);
    vertical-align: middle;
}

.data-table tbody tr {
    transition: all var(--ca-transition-normal);
}

.data-table tbody tr:hover {
    background: var(--ca-bg-hover);
}

.data-table tbody tr.selected {
    background: rgba(37, 99, 235, 0.05);
}

.col-checkbox {
    width: 60px;
    text-align: center;
}

.col-user {
    width: 200px;
}

.col-email {
    width: 250px;
}

.col-role {
    width: 120px;
}

.col-date {
    width: 160px;
}

.col-status {
    width: 120px;
}

.col-actions {
    width: 140px;
}

/* 用户信息 */
.user-info {
    display: flex;
    align-items: center;
    gap: 12px;
}

.user-avatar {
    width: 40px;
    height: 40px;
    border-radius: var(--ca-radius-md);
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 600;
    font-size: 14px;
    color: white;
    box-shadow: var(--ca-shadow-sm);
}

.user-details {
    display: flex;
    flex-direction: column;
    gap: 2px;
}

.user-name {
    font-weight: 500;
    color: var(--ca-text-primary);
}

.user-id {
    font-size: 12px;
    color: var(--ca-text-tertiary);
}

/* 邮箱单元格 */
.email-cell {
    display: flex;
    align-items: center;
    gap: 8px;
    color: var(--ca-text-secondary);
}

.email-cell svg {
    width: 16px;
    height: 16px;
}

/* 角色标签 */
.role-badge {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 6px 12px;
    border-radius: var(--ca-radius-full);
    font-size: 12px;
    font-weight: 600;
}

.role-badge.admin {
    background: rgba(37, 99, 235, 0.1);
    color: var(--ca-primary);
}

.role-badge.user {
    background: rgba(107, 114, 128, 0.1);
    color: var(--ca-text-secondary);
}

.role-dot {
    width: 6px;
    height: 6px;
    border-radius: var(--ca-radius-full);
    background: currentColor;
}

/* 日期单元格 */
.date-cell {
    display: flex;
    flex-direction: column;
    gap: 2px;
}

.date-primary {
    font-weight: 500;
    color: var(--ca-text-primary);
}

.date-secondary {
    font-size: 12px;
    color: var(--ca-text-tertiary);
}

/* 状态标签 */
.status-badge {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 6px 12px;
    border-radius: var(--ca-radius-full);
    font-size: 12px;
    font-weight: 600;
}

.status-badge.active {
    background: rgba(5, 150, 105, 0.1);
    color: var(--ca-success);
}

.status-dot {
    width: 6px;
    height: 6px;
    border-radius: var(--ca-radius-full);
    background: currentColor;
    animation: pulse-dot 2s ease-in-out infinite;
}

@keyframes pulse-dot {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.5; }
}

/* 操作按钮 */
.action-buttons {
    display: flex;
    gap: 8px;
}

.btn-action {
    width: 36px;
    height: 36px;
    border-radius: var(--ca-radius-md);
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all var(--ca-transition-normal);
    color: var(--ca-text-secondary);
}

.btn-action:hover {
    background: var(--ca-bg-hover);
    color: var(--ca-primary);
}

.btn-action.view {
    background: rgba(37, 99, 235, 0.05);
    color: var(--ca-primary);
}

.btn-action.view:hover {
    background: rgba(37, 99, 235, 0.1);
    color: var(--ca-primary-dark);
}

.btn-action.delete {
    background: rgba(220, 38, 38, 0.05);
    color: var(--ca-error);
}

.btn-action.delete:hover {
    background: rgba(220, 38, 38, 0.1);
    color: var(--ca-error-dark);
}

.btn-action svg {
    width: 18px;
    height: 18px;
}

/* 空状态 */
.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 80px 20px;
    color: var(--ca-text-tertiary);
}

.empty-icon {
    width: 80px;
    height: 80px;
    margin-bottom: 16px;
    color: var(--ca-text-muted);
}

.empty-icon svg {
    width: 100%;
    height: 100%;
}

.empty-state p {
    font-size: 16px;
    font-weight: 500;
    color: var(--ca-text-secondary);
    margin-bottom: 4px;
}

.empty-state span {
    font-size: 14px;
}

/* 分页 */
.pagination-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 0;
}

.pagination-info {
    font-size: 14px;
    color: var(--ca-text-tertiary);
}

.pagination {
    display: flex;
    align-items: center;
    gap: 8px;
}

.page-btn {
    width: 40px;
    height: 40px;
    border-radius: var(--ca-radius-md);
    background: var(--ca-bg-card);
    border: 1px solid var(--ca-border);
    color: var(--ca-text-secondary);
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all var(--ca-transition-normal);
    box-shadow: var(--ca-shadow-sm);
}

.page-btn:hover:not(:disabled) {
    background: var(--ca-bg-hover);
    border-color: var(--ca-border-hover);
    color: var(--ca-primary);
}

.page-btn:disabled {
    opacity: 0.4;
    cursor: not-allowed;
}

.page-btn svg {
    width: 18px;
    height: 18px;
}

.page-numbers {
    display: flex;
    gap: 4px;
}

.page-number {
    min-width: 40px;
    height: 40px;
    padding: 0 12px;
    border-radius: var(--ca-radius-md);
    background: var(--ca-bg-card);
    border: 1px solid var(--ca-border);
    color: var(--ca-text-secondary);
    font-size: 14px;
    cursor: pointer;
    transition: all var(--ca-transition-normal);
}

.page-number:hover {
    background: var(--ca-bg-hover);
    border-color: var(--ca-border-hover);
    color: var(--ca-primary);
}

.page-number.active {
    background: var(--ca-gradient-primary);
    border-color: transparent;
    color: white;
}

/* 弹窗 */
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.6);
    backdrop-filter: blur(10px);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    padding: 20px;
}

.modal-content {
    background: white;
    border: 1px solid var(--ca-border);
    border-radius: var(--ca-radius-2xl);
    width: 100%;
    max-width: 520px;
    overflow: hidden;
    box-shadow: var(--ca-shadow-xl);
}

.modal-header {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 24px;
    border-bottom: 1px solid var(--ca-border);
}

.modal-icon {
    width: 48px;
    height: 48px;
    border-radius: var(--ca-radius-lg);
    background: var(--ca-bg-tertiary);
    border: 1px solid var(--ca-border);
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--ca-primary);
}

.modal-icon svg {
    width: 24px;
    height: 24px;
}

.modal-header h2 {
    flex: 1;
    font-size: 20px;
    font-weight: 600;
    color: var(--ca-text-primary);
}

.modal-close {
    width: 36px;
    height: 36px;
    border-radius: var(--ca-radius-md);
    background: var(--ca-bg-tertiary);
    border: 1px solid var(--ca-border);
    color: var(--ca-text-tertiary);
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all var(--ca-transition-normal);
}

.modal-close:hover {
    background: var(--ca-bg-hover);
    color: var(--ca-text-primary);
    border-color: var(--ca-border-hover);
}

.modal-close svg {
    width: 18px;
    height: 18px;
}

.modal-body {
    padding: 24px;
}

.user-profile {
    display: flex;
    align-items: center;
    gap: 16px;
    margin-bottom: 24px;
    padding: 20px;
    background: var(--ca-bg-tertiary);
    border-radius: var(--ca-radius-lg);
}

.profile-avatar {
    width: 64px;
    height: 64px;
    border-radius: var(--ca-radius-lg);
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 700;
    font-size: 20px;
    color: white;
    box-shadow: var(--ca-shadow-md);
}

.profile-info {
    flex: 1;
}

.profile-info h3 {
    font-size: 18px;
    font-weight: 600;
    color: var(--ca-text-primary);
    margin-bottom: 4px;
}

.profile-role {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 4px 10px;
    border-radius: var(--ca-radius-full);
    font-size: 12px;
    font-weight: 600;
}

.profile-role.admin {
    background: rgba(37, 99, 235, 0.1);
    color: var(--ca-primary);
}

.profile-role.user {
    background: rgba(107, 114, 128, 0.1);
    color: var(--ca-text-secondary);
}

.detail-grid {
    display: grid;
    gap: 12px;
}

.detail-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
    background: var(--ca-bg-tertiary);
    border-radius: var(--ca-radius-lg);
    border: 1px solid var(--ca-border);
}

.detail-label {
    font-size: 14px;
    color: var(--ca-text-tertiary);
}

.detail-value {
    font-size: 14px;
    color: var(--ca-text-primary);
    font-weight: 500;
}

.modal-footer {
    display: flex;
    gap: 12px;
    padding: 0 24px 24px;
}

.btn-primary {
    flex: 1;
    padding: 14px;
    background: var(--ca-gradient-primary);
    border: none;
    border-radius: var(--ca-radius-lg);
    color: white;
    font-size: 15px;
    font-weight: 600;
    cursor: pointer;
    transition: all var(--ca-transition-normal);
    box-shadow: 0 4px 15px rgba(37, 99, 235, 0.3);
}

.btn-primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(37, 99, 235, 0.4);
}

/* 弹窗动画 */
.modal-enter-active,
.modal-leave-active {
    transition: all var(--ca-transition-normal);
}

.modal-enter-from,
.modal-leave-to {
    opacity: 0;
}

.modal-enter-from .modal-content,
.modal-leave-to .modal-content {
    transform: scale(0.95);
    opacity: 0;
}

/* 响应式 */
@media (max-width: 768px) {
    .page-header {
        flex-direction: column;
        gap: 16px;
    }
    
    .toolbar {
        flex-direction: column;
        align-items: stretch;
    }
    
    .search-box input {
        max-width: none;
    }
    
    .pagination-wrapper {
        flex-direction: column;
        gap: 16px;
        align-items: center;
    }
}
</style>
