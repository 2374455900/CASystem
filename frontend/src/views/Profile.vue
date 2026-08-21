<template>
    <div class="profile-page">
        <!-- 主内容 -->

        <main class="main-content">
            <!-- 页面头部 -->
            <div class="page-header">
                <div class="header-glow"></div>
                <div class="header-content">
                    <div class="header-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                            <circle cx="12" cy="7" r="4" />
                        </svg>
                    </div>
                    <div class="header-text">
                        <h1>个人中心</h1>
                        <p>管理您的账户信息和安全设置</p>
                    </div>
                </div>
            </div>

            <div class="content-grid">
                <!-- 左侧：用户信息卡片 -->
                <div class="left-column">
                    <div class="profile-card">
                        <div class="card-glow"></div>
                        <div class="profile-header">
                            <div class="avatar-wrapper">
                                <div class="avatar" :style="{ background: getAvatarGradient(user?.id) }">
                                    {{ user?.username?.charAt(0).toUpperCase() }}
                                </div>
                                <div class="status-badge">
                                    <span class="status-dot"></span>
                                    在线
                                </div>
                            </div>
                            <div class="user-meta">
                                <h2>{{ user?.username }}</h2>
                                <p class="user-email">{{ user?.email }}</p>
                                <div class="user-role">
                                    <span class="role-badge" :class="isAdmin ? 'admin' : 'user'">
                                        {{ isAdmin ? '管理员' : '普通用户' }}
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="profile-details">
                            <div class="detail-item">
                                <div class="detail-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                                        <line x1="9" y1="9" x2="15" y2="9" />
                                        <line x1="9" y1="15" x2="15" y2="15" />
                                    </svg>
                                </div>
                                <div class="detail-content">
                                    <span class="detail-label">用户ID</span>
                                    <span class="detail-value">{{ user?.id }}</span>
                                </div>
                            </div>
                            <div class="detail-item">
                                <div class="detail-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path
                                            d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z" />
                                        <polyline points="22,6 12,13 2,6" />
                                    </svg>
                                </div>
                                <div class="detail-content">
                                    <span class="detail-label">邮箱地址</span>
                                    <span class="detail-value">{{ user?.email }}</span>
                                </div>
                            </div>
                            <div class="detail-item">
                                <div class="detail-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
                                        <line x1="16" y1="2" x2="16" y2="6" />
                                        <line x1="8" y1="2" x2="8" y2="6" />
                                        <line x1="3" y1="10" x2="21" y2="10" />
                                    </svg>
                                </div>
                                <div class="detail-content">
                                    <span class="detail-label">注册时间</span>
                                    <span class="detail-value">{{ formatDate(user?.createdAt) }}</span>
                                </div>
                            </div>
                            <div class="detail-item">
                                <div class="detail-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
                                    </svg>
                                </div>
                                <div class="detail-content">
                                    <span class="detail-label">账户状态</span>
                                    <span class="detail-value status-active">
                                        <span class="status-indicator"></span>
                                        正常
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="profile-actions">
                            <button @click="showChangePassword = true" class="btn-primary">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                                    <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                                </svg>
                                修改密码
                            </button>
                        </div>
                    </div>
                </div>

                <!-- 右侧：统计和操作 -->
                <div class="right-column">
                    <!-- 证书统计 -->
                    <div class="stats-card">
                        <div class="card-shine"></div>
                        <h3 class="card-title">
                            <span class="title-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path
                                        d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                                </svg>
                            </span>
                            证书统计
                        </h3>
                        <div class="stats-grid">
                            <div class="stat-item">
                                <div class="stat-icon total">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                                        <polyline points="14 2 14 8 20 8" />
                                        <line x1="16" y1="13" x2="8" y2="13" />
                                        <line x1="16" y1="17" x2="8" y2="17" />
                                        <polyline points="10 9 9 9 8 9" />
                                    </svg>
                                </div>
                                <div class="stat-info">
                                    <span class="stat-value">{{ certificateCount }}</span>
                                    <span class="stat-label">证书总数</span>
                                </div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-icon active">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                                        <polyline points="22 4 12 14.01 9 11.01" />
                                    </svg>
                                </div>
                                <div class="stat-info">
                                    <span class="stat-value">{{ activeCount }}</span>
                                    <span class="stat-label">有效证书</span>
                                </div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-icon revoked">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <circle cx="12" cy="12" r="10" />
                                        <line x1="15" y1="9" x2="9" y2="15" />
                                        <line x1="9" y1="9" x2="15" y2="15" />
                                    </svg>
                                </div>
                                <div class="stat-info">
                                    <span class="stat-value">{{ revokedCount }}</span>
                                    <span class="stat-label">已撤销</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 快速操作 -->
                    <div class="quick-actions">
                        <h3 class="card-title">
                            <span class="title-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <circle cx="12" cy="12" r="10" />
                                    <line x1="12" y1="8" x2="12" y2="12" />
                                    <line x1="12" y1="16" x2="12.01" y2="16" />
                                </svg>
                            </span>
                            快速操作
                        </h3>
                        <div class="action-list">
                            <router-link to="/apply" class="action-item">
                                <div class="action-icon primary">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                                        <line x1="12" y1="8" x2="12" y2="16" />
                                        <line x1="8" y1="12" x2="16" y2="12" />
                                    </svg>
                                </div>
                                <div class="action-content">
                                    <span class="action-name">申请新证书</span>
                                    <span class="action-desc">在线申请数字证书</span>
                                </div>
                                <svg class="action-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                    stroke-width="2">
                                    <line x1="5" y1="12" x2="19" y2="12" />
                                    <polyline points="12 5 19 12 12 19" />
                                </svg>
                            </router-link>
                            <router-link to="/certificates" class="action-item">
                                <div class="action-icon success">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path
                                            d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                                    </svg>
                                </div>
                                <div class="action-content">
                                    <span class="action-name">查看证书</span>
                                    <span class="action-desc">管理您的证书</span>
                                </div>
                                <svg class="action-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                    stroke-width="2">
                                    <line x1="5" y1="12" x2="19" y2="12" />
                                    <polyline points="12 5 19 12 12 19" />
                                </svg>
                            </router-link>
                            <router-link to="/revoke" class="action-item">
                                <div class="action-icon warning">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <circle cx="12" cy="12" r="10" />
                                        <line x1="15" y1="9" x2="9" y2="15" />
                                        <line x1="9" y1="9" x2="15" y2="15" />
                                    </svg>
                                </div>
                                <div class="action-content">
                                    <span class="action-name">撤销证书</span>
                                    <span class="action-desc">撤销不再使用的证书</span>
                                </div>
                                <svg class="action-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                    stroke-width="2">
                                    <line x1="5" y1="12" x2="19" y2="12" />
                                    <polyline points="12 5 19 12 12 19" />
                                </svg>
                            </router-link>
                        </div>
                    </div>

                    <!-- 安全提示 -->
                    <div class="security-tips">
                        <h3 class="card-title">
                            <span class="title-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
                                </svg>
                            </span>
                            安全提示
                        </h3>
                        <div class="tips-list">
                            <div class="tip-item">
                                <div class="tip-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
                                        <polyline points="9 12 12 15 16 10" />
                                    </svg>
                                </div>
                                <span>定期更换密码可以提高账户安全性</span>
                            </div>
                            <div class="tip-item">
                                <div class="tip-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
                                        <polyline points="9 12 12 15 16 10" />
                                    </svg>
                                </div>
                                <span>及时撤销不再使用的证书</span>
                            </div>
                            <div class="tip-item">
                                <div class="tip-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
                                        <polyline points="9 12 12 15 16 10" />
                                    </svg>
                                </div>
                                <span>妥善保管您的私钥文件</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!-- 修改密码弹窗 -->
        <Transition name="modal">
            <div v-if="showChangePassword" class="modal-overlay" @click.self="showChangePassword = false">
                <div class="modal-content">
                    <div class="modal-glow"></div>
                    <div class="modal-header">
                        <div class="modal-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                                <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                            </svg>
                        </div>
                        <h2>修改密码</h2>
                        <button class="modal-close" @click="showChangePassword = false">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <line x1="18" y1="6" x2="6" y2="18" />
                                <line x1="6" y1="6" x2="18" y2="18" />
                            </svg>
                        </button>
                    </div>
                    <form @submit.prevent="handleChangePassword" class="modal-form">
                        <div class="form-group">
                            <label class="form-label">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                                    <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                                </svg>
                                当前密码
                            </label>
                            <div class="input-wrapper">
                                <input v-model="passwordForm.oldPassword" type="password" placeholder="请输入当前密码" required
                                    class="form-input" />
                                <div class="input-glow"></div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-label">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                                    <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                                </svg>
                                新密码
                            </label>
                            <div class="input-wrapper">
                                <input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" required
                                    class="form-input" />
                                <div class="input-glow"></div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-label">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                                    <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                                </svg>
                                确认新密码
                            </label>
                            <div class="input-wrapper">
                                <input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码"
                                    required class="form-input" />
                                <div class="input-glow"></div>
                            </div>
                        </div>
                        <div class="modal-actions">
                            <button type="button" @click="showChangePassword = false" class="btn-secondary">
                                取消
                            </button>
                            <button type="submit" class="btn-primary">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <polyline points="20 6 9 17 4 12" />
                                </svg>
                                确认修改
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </Transition>
    </div>
</template>

<script>
import api from '../api'

export default {
    name: 'Profile',
    data() {
        return {
            certificates: [],
            showChangePassword: false,
            passwordForm: {
                oldPassword: '',
                newPassword: '',
                confirmPassword: ''
            }
        }
    },
    computed: {
        user() {
            return this.$store.state.user
        },
        isAdmin() {
            return this.$store.state.user?.authority === '1'
        },
        certificateCount() {
            return this.certificates.length
        },
        activeCount() {
            return this.certificates.filter(c => c.state === '使用中').length
        },
        revokedCount() {
            return this.certificates.filter(c => c.state === '已注销').length
        }
    },
    async mounted() {
        await this.loadCertificates()
    },
    methods: {
        async loadCertificates() {
            try {
                const response = await api.certificates.getMyCertificates()
                if (response.code === 200) {
                    this.certificates = response.data
                }
            } catch (error) {
                console.error('加载证书失败', error)
            }
        },

        async handleChangePassword() {
            if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
                alert('两次输入的密码不一致')
                return
            }
            alert('密码修改功能开发中...')
            this.showChangePassword = false
        },

        handleLogout() {
            this.$store.dispatch('logout')
            this.$router.push('/login')
        },

        formatDate(dateStr) {
            if (!dateStr) return '-'
            // 解析中文日期格式：yyyy 年 MM 月 dd 日 HH:mm:ss
            const match = dateStr.match(/(\d{4}) 年 (\d{2}) 月 (\d{2}) 日 (\d{2}):(\d{2}):(\d{2})/)
            let date
            if (match) {
                const [, year, month, day, hour, minute, second] = match
                date = new Date(year, parseInt(month) - 1, parseInt(day), parseInt(hour), parseInt(minute), parseInt(second))
            } else {
                date = new Date(dateStr)
            }
            if (isNaN(date.getTime())) return '-'
            return date.toLocaleDateString('zh-CN')
        },

        getAvatarGradient(id) {
            const gradients = [
                'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
                'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
                'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
                'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
                'linear-gradient(135deg, #30cfd0 0%, #330867 100%)'
            ]
            return gradients[(id || 0) % gradients.length]
        },


    }
}
</script>

<style scoped>
.profile-page {
    min-height: 100vh;
    background: #f8fafc;
    position: relative;
    overflow-x: hidden;
}

/* 动态背景 */
.bg-effects {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    pointer-events: none;
    z-index: 0;
    overflow: hidden;
}

.gradient-orb {
    position: absolute;
    border-radius: 50%;
    filter: blur(100px);
    opacity: 0.3;
    animation: float 20s ease-in-out infinite;
}

.orb-1 {
    width: 600px;
    height: 600px;
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    top: -200px;
    right: -200px;
    animation-delay: 0s;
}

.orb-2 {
    width: 500px;
    height: 500px;
    background: linear-gradient(135deg, #10b981, #059669);
    bottom: -150px;
    left: -150px;
    animation-delay: -10s;
}

@keyframes float {

    0%,
    100% {
        transform: translate(0, 0) scale(1);
    }

    25% {
        transform: translate(50px, -50px) scale(1.1);
    }

    50% {
        transform: translate(0, 50px) scale(0.95);
    }

    75% {
        transform: translate(-50px, -25px) scale(1.05);
    }
}

.orb-3 {
    width: 400px;
    height: 400px;
    background: linear-gradient(135deg, #667eea, #764ba2);
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    animation-delay: -14s;
    opacity: 0.2;
}

/* 浮动粒子 */
.floating-particles {
    position: absolute;
    inset: 0;
    overflow: hidden;
}

.particle {
    position: absolute;
    background: radial-gradient(circle, rgba(102, 126, 234, 0.4), transparent);
    border-radius: 50%;
    animation: particleFloat linear infinite;
}

@keyframes particleFloat {
    0% {
        transform: translateY(0) rotate(0deg);
        opacity: 0;
    }

    10% {
        opacity: 1;
    }

    90% {
        opacity: 1;
    }

    100% {
        transform: translateY(-100vh) rotate(360deg);
        opacity: 0;
    }
}

.grid-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image:
        linear-gradient(rgba(102, 126, 234, 0.05) 1px, transparent 1px),
        linear-gradient(90deg, rgba(102, 126, 234, 0.05) 1px, transparent 1px);
    background-size: 50px 50px;
}

/* 导航栏 */
.navbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 40px;
    height: 80px;
    background: #ffffff;
    border-bottom: 1px solid #e2e8f0;
    position: sticky;
    top: 0;
    z-index: 100;
}

.nav-brand {
    display: flex;
    align-items: center;
    gap: 12px;
}

.brand-icon {
    width: 42px;
    height: 42px;
}

.brand-icon svg {
    width: 100%;
    height: 100%;
}

.brand-text {
    font-size: 20px;
    font-weight: 700;
    background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.nav-links {
    display: flex;
    align-items: center;
    gap: 8px;
}

.nav-link {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 18px;
    color: #64748b;
    text-decoration: none;
    font-size: 14px;
    font-weight: 500;
    border-radius: 12px;
    transition: all 0.3s ease;
}

.nav-link svg {
    width: 18px;
    height: 18px;
}

.nav-link:hover {
    color: #3b82f6;
    background: rgba(59, 130, 246, 0.1);
}

.nav-link.active {
    color: #3b82f6;
    background: rgba(59, 130, 246, 0.15);
    font-weight: 600;
}

.nav-user {
    display: flex;
    align-items: center;
    gap: 16px;
}

.user-avatar {
    width: 42px;
    height: 42px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    font-weight: 600;
    color: #ffffff;
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    flex-shrink: 0;
    position: relative;
    z-index: 1;
    cursor: pointer;
    transition: all 0.3s ease;
}

.user-avatar:hover {
    transform: scale(1.1);
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.logout-btn {
    width: 42px;
    height: 42px;
    border-radius: 12px;
    background: rgba(239, 68, 68, 0.1);
    border: 1px solid rgba(239, 68, 68, 0.2);
    color: #ef4444;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
}

.logout-btn:hover {
    background: rgba(239, 68, 68, 0.2);
    transform: translateY(-2px);
    border-color: rgba(239, 68, 68, 0.3);
}

.logout-btn svg {
    width: 18px;
    height: 18px;
}

/* 主内容 */
.main-content {
    position: relative;
    z-index: 1;
    max-width: 1200px;
    margin: 0 auto;
    padding: 40px 20px;
}

/* 页面头部 */
.page-header {
    position: relative;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.1) 0%, rgba(139, 92, 246, 0.1) 100%);
    border-radius: 24px;
    padding: 40px;
    margin-bottom: 40px;
    border: 1px solid #e2e8f0;
    overflow: hidden;
}

.header-glow {
    position: absolute;
    top: -50%;
    right: -20%;
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, rgba(59, 130, 246, 0.15) 0%, transparent 70%);
    pointer-events: none;
}

.header-content {
    display: flex;
    align-items: center;
    gap: 20px;
    position: relative;
}

.header-icon {
    width: 64px;
    height: 64px;
    border-radius: 16px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2) 0%, rgba(139, 92, 246, 0.2) 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    color: #3b82f6;
}

.header-icon svg {
    width: 32px;
    height: 32px;
}

.header-text h1 {
    font-size: 32px;
    font-weight: 700;
    color: #0f172a;
    margin-bottom: 8px;
}

.header-text p {
    font-size: 15px;
    color: #64748b;
}

/* 内容网格 */
.content-grid {
    display: grid;
    grid-template-columns: 380px 1fr;
    gap: 24px;
}

/* 左侧列 */
.left-column {
    display: flex;
    flex-direction: column;
}

.profile-card {
    position: relative;
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 24px;
    padding: 32px;
    overflow: hidden;
}

.card-glow {
    position: absolute;
    top: -100px;
    left: -100px;
    width: 300px;
    height: 300px;
    background: radial-gradient(circle, rgba(59, 130, 246, 0.1) 0%, transparent 70%);
    pointer-events: none;
}

.profile-header {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    margin-bottom: 32px;
    padding-bottom: 24px;
    border-bottom: 1px solid #e2e8f0;
}

.avatar-wrapper {
    position: relative;
    margin-bottom: 20px;
    display: inline-block;
}

.avatar {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 40px;
    font-weight: 700;
    color: #ffffff;
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    box-shadow: 0 10px 40px rgba(59, 130, 246, 0.2);
    position: relative;
    z-index: 1;
}

.status-badge {
    position: absolute;
    bottom: 4px;
    right: 4px;
    display: flex;
    align-items: center;
    gap: 4px;
    padding: 4px 10px;
    background: rgba(255, 255, 255, 0.95);
    border: 1px solid rgba(16, 185, 129, 0.3);
    border-radius: 20px;
    font-size: 11px;
    font-weight: 500;
    color: #10b981;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    z-index: 2;
    backdrop-filter: blur(4px);
    white-space: nowrap;
}

.status-badge .status-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: #10b981;
    animation: pulse 2s infinite;
    flex-shrink: 0;
}

@keyframes pulse {

    0%,
    100% {
        opacity: 1;
    }

    50% {
        opacity: 0.5;
    }
}

.user-meta h2 {
    font-size: 22px;
    font-weight: 600;
    color: #0f172a;
    margin-bottom: 6px;
}

.user-email {
    font-size: 14px;
    color: #64748b;
    margin-bottom: 12px;
}

.user-role {
    display: flex;
    justify-content: center;
}

.role-badge {
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
}

.role-badge.admin {
    background: rgba(139, 92, 246, 0.15);
    color: #8b5cf6;
}

.role-badge.user {
    background: rgba(59, 130, 246, 0.15);
    color: #3b82f6;
}

.profile-details {
    margin-bottom: 24px;
}

.detail-item {
    display: flex;
    align-items: center;
    gap: 14px;
    padding: 14px 0;
    border-bottom: 1px solid #f1f5f9;
}

.detail-item:last-child {
    border-bottom: none;
}

.detail-icon {
    width: 40px;
    height: 40px;
    border-radius: 10px;
    background: #f8fafc;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #64748b;
    flex-shrink: 0;
}

.detail-icon svg {
    width: 20px;
    height: 20px;
}

.detail-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 2px;
}

.detail-label {
    font-size: 12px;
    color: #94a3b8;
}

.detail-value {
    font-size: 14px;
    font-weight: 500;
    color: #0f172a;
}

.detail-value.status-active {
    display: flex;
    align-items: center;
    gap: 6px;
    color: #10b981;
}

.status-indicator {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: #10b981;
}

.profile-actions {
    display: flex;
    gap: 12px;
}

.btn-primary {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 12px 20px;
    background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
    border: none;
    border-radius: 12px;
    color: white;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
}

.btn-primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 30px rgba(59, 130, 246, 0.3);
}

.btn-primary svg {
    width: 18px;
    height: 18px;
}

/* 右侧列 */
.right-column {
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.card-title {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 17px;
    font-weight: 600;
    color: #0f172a;
    margin-bottom: 20px;
}

.title-icon {
    width: 32px;
    height: 32px;
    border-radius: 8px;
    background: rgba(59, 130, 246, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
}

.title-icon svg {
    width: 18px;
    height: 18px;
    color: #3b82f6;
}

/* 统计卡片 */
.stats-card {
    position: relative;
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 20px;
    padding: 24px;
    overflow: hidden;
}

.card-shine {
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(59, 130, 246, 0.03), transparent);
    transition: left 0.6s ease;
}

.stats-card:hover .card-shine {
    left: 100%;
}

.stats-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;
}

.stat-item {
    display: flex;
    align-items: center;
    gap: 14px;
    padding: 16px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 14px;
    transition: all 0.3s ease;
}

.stat-item:hover {
    background: #f1f5f9;
    transform: translateY(-2px);
}

.stat-icon {
    width: 44px;
    height: 44px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.stat-icon svg {
    width: 22px;
    height: 22px;
}

.stat-icon.total {
    background: rgba(59, 130, 246, 0.15);
    color: #3b82f6;
}

.stat-icon.active {
    background: rgba(16, 185, 129, 0.15);
    color: #10b981;
}

.stat-icon.revoked {
    background: rgba(239, 68, 68, 0.15);
    color: #ef4444;
}

.stat-info {
    display: flex;
    flex-direction: column;
}

.stat-value {
    font-size: 24px;
    font-weight: 700;
    color: #0f172a;
}

.stat-label {
    font-size: 12px;
    color: #64748b;
}

/* 快速操作 */
.quick-actions {
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 20px;
    padding: 24px;
}

.action-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.action-item {
    display: flex;
    align-items: center;
    gap: 14px;
    padding: 16px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 14px;
    text-decoration: none;
    transition: all 0.3s ease;
}

.action-item:hover {
    background: #f1f5f9;
    border-color: #cbd5e1;
    transform: translateX(4px);
}

.action-icon {
    width: 44px;
    height: 44px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.action-icon svg {
    width: 22px;
    height: 22px;
}

.action-icon.primary {
    background: rgba(59, 130, 246, 0.15);
    color: #3b82f6;
}

.action-icon.success {
    background: rgba(16, 185, 129, 0.15);
    color: #10b981;
}

.action-icon.warning {
    background: rgba(245, 158, 11, 0.15);
    color: #f59e0b;
}

.action-content {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.action-name {
    font-size: 14px;
    font-weight: 600;
    color: #0f172a;
}

.action-desc {
    font-size: 12px;
    color: #64748b;
}

.action-arrow {
    width: 20px;
    height: 20px;
    color: #cbd5e1;
    transition: all 0.3s ease;
}

.action-item:hover .action-arrow {
    color: #64748b;
    transform: translateX(4px);
}

/* 安全提示 */
.security-tips {
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 20px;
    padding: 24px;
}

.tips-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.tip-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    background: #f8fafc;
    border-radius: 10px;
    font-size: 13px;
    color: #64748b;
}

.tip-icon {
    width: 24px;
    height: 24px;
    border-radius: 6px;
    background: rgba(16, 185, 129, 0.15);
    display: flex;
    align-items: center;
    justify-content: center;
    color: #10b981;
    flex-shrink: 0;
}

.tip-icon svg {
    width: 14px;
    height: 14px;
}

/* 弹窗 */
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    backdrop-filter: blur(10px);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    padding: 20px;
}

.modal-content {
    position: relative;
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 24px;
    width: 100%;
    max-width: 440px;
    overflow: hidden;
}

.modal-glow {
    position: absolute;
    top: -100px;
    right: -100px;
    width: 300px;
    height: 300px;
    background: radial-gradient(circle, rgba(59, 130, 246, 0.1) 0%, transparent 70%);
    pointer-events: none;
}

.modal-header {
    display: flex;
    align-items: center;
    gap: 14px;
    padding: 24px 24px 0;
    position: relative;
}

.modal-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2) 0%, rgba(139, 92, 246, 0.2) 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    color: #3b82f6;
}

.modal-icon svg {
    width: 24px;
    height: 24px;
}

.modal-header h2 {
    flex: 1;
    font-size: 20px;
    font-weight: 600;
    color: #0f172a;
}

.modal-close {
    width: 36px;
    height: 36px;
    border-radius: 10px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    color: #64748b;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
}

.modal-close:hover {
    background: #f1f5f9;
    color: #3b82f6;
}

.modal-close svg {
    width: 18px;
    height: 18px;
}

.modal-form {
    padding: 24px;
}

.form-group {
    margin-bottom: 20px;
}

.form-label {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 13px;
    font-weight: 500;
    color: #475569;
    margin-bottom: 8px;
}

.form-label svg {
    width: 16px;
    height: 16px;
    color: #94a3b8;
}

.input-wrapper {
    position: relative;
}

.form-input {
    width: 100%;
    padding: 14px 16px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    color: #0f172a;
    font-size: 14px;
    transition: all 0.3s ease;
}

.form-input::placeholder {
    color: #94a3b8;
}

.form-input:focus {
    outline: none;
    border-color: #3b82f6;
    background: #ffffff;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.input-glow {
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 0;
    height: 2px;
    background: linear-gradient(90deg, #3b82f6, #8b5cf6);
    transition: width 0.3s ease;
}

.form-input:focus+.input-glow {
    width: 100%;
}

.modal-actions {
    display: flex;
    gap: 12px;
    margin-top: 24px;
}

.btn-secondary {
    flex: 1;
    padding: 12px 20px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    color: #64748b;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
}

.btn-secondary:hover {
    background: #e2e8f0;
    color: #3b82f6;
}

/* 过渡动画 */
.modal-enter-active,
.modal-leave-active {
    transition: all 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
    opacity: 0;
    transform: scale(0.95);
}

/* 响应式设计 */
@media (max-width: 968px) {
    .content-grid {
        grid-template-columns: 1fr;
    }

    .left-column {
        order: 1;
    }

    .right-column {
        order: 2;
    }
}

@media (max-width: 768px) {
    .navbar {
        padding: 0 20px;
        height: 70px;
    }

    .nav-links {
        display: none;
    }

    .page-header {
        padding: 24px;
    }

    .header-text h1 {
        font-size: 24px;
    }

    .stats-grid {
        grid-template-columns: 1fr;
    }

    .profile-card {
        padding: 24px;
    }

    .avatar {
        width: 80px;
        height: 80px;
        font-size: 32px;
    }
}
</style>
