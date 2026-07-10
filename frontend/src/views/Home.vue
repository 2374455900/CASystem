<template>
    <div class="home-page">
        <!-- 动态背景 -->
        <div class="bg-effects">
            <div class="gradient-orb orb-1"></div>
            <div class="gradient-orb orb-2"></div>
            <div class="gradient-orb orb-3"></div>
            <div class="grid-overlay"></div>
            <div class="floating-particles">
                <div v-for="i in 20" :key="i" class="particle" :style="getParticleStyle(i)"></div>
            </div>
        </div>
        <!-- 导航栏 -->
        <nav class="navbar">
            <div class="nav-brand">
                <div class="brand-icon">
                    <svg viewBox="0 0 48 48" fill="none">
                        <defs>
                            <linearGradient id="brandGrad" x1="0" y1="0" x2="48" y2="48">
                                <stop offset="0%" stop-color="#3b82f6" />
                                <stop offset="100%" stop-color="#8b5cf6" />
                            </linearGradient>
                        </defs>
                        <rect x="8" y="8" width="32" height="32" rx="8" stroke="url(#brandGrad)" stroke-width="2"
                            fill="none" />
                        <path d="M16 24L22 30L32 18" stroke="url(#brandGrad)" stroke-width="2.5" stroke-linecap="round"
                            stroke-linejoin="round" />
                    </svg>
                </div>
                <span class="brand-text">CA证书管理系统</span>
            </div>

            <div class="nav-links">
                <router-link to="/home" class="nav-link active">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
                        <polyline points="9 22 9 12 15 12 15 22" />
                    </svg>
                    首页
                </router-link>
                <router-link to="/apply" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                        <line x1="12" y1="8" x2="12" y2="16" />
                        <line x1="8" y1="12" x2="16" y2="12" />
                    </svg>
                    申请证书
                </router-link>
                <router-link to="/certificates" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                        <polyline points="14 2 14 8 20 8" />
                        <line x1="16" y1="13" x2="8" y2="13" />
                        <line x1="16" y1="17" x2="8" y2="17" />
                        <polyline points="10 9 9 9 8 9" />
                    </svg>
                    我的证书
                </router-link>
                <router-link to="/revoke" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="12" cy="12" r="10" />
                        <line x1="15" y1="9" x2="9" y2="15" />
                        <line x1="9" y1="9" x2="15" y2="15" />
                    </svg>
                    撤销证书
                </router-link>
                <router-link to="/keys" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                        <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                    </svg>
                    密钥管理
                </router-link>
                <router-link to="/generate-signature" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                    </svg>
                    签名生成
                </router-link>
                <router-link to="/verify" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                        <polyline points="22 4 12 14.01 9 11.01" />
                    </svg>
                    签名验证
                </router-link>
            </div>

            <div class="nav-user">
                <div class="user-avatar" :style="{ background: getAvatarGradient(user?.id) }" @click="$router.push('/profile')" title="个人中心">
                    {{ user?.username?.charAt(0).toUpperCase() }}
                </div>
                <button class="logout-btn" @click="handleLogout">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
                        <polyline points="16 17 21 12 16 7" />
                        <line x1="21" y1="12" x2="9" y2="12" />
                    </svg>
                </button>
            </div>
        </nav>

        <!-- 主内容 -->
        <main class="main-content">
            <!-- 欢迎横幅 -->
            <section class="welcome-banner">
                <div class="welcome-content">
                    <div class="welcome-text">
                        <div class="welcome-badge">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
                            </svg>
                            <span>安全认证中心</span>
                        </div>
                        <h1>欢迎回来，<span class="username">{{ user?.username }}</span></h1>
                        <p>您的数字证书安全保障中心，为您提供专业的证书管理服务</p>
                    </div>
                    <div class="welcome-stats">
                        <div class="stat-card">
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
                        <div class="stat-card">
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
                        <div class="stat-card">
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
            </section>

            <!-- 快捷操作 -->
            <section class="quick-actions">
                <h2 class="section-title">
                    <span class="title-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <circle cx="12" cy="12" r="10" />
                            <line x1="12" y1="8" x2="12" y2="12" />
                            <line x1="12" y1="16" x2="12.01" y2="16" />
                        </svg>
                    </span>
                    <span>快捷操作</span>
                </h2>
                <div class="action-grid">
                    <div class="action-card primary" @click="$router.push('/apply')">
                        <div class="action-icon">
                            <div class="icon-bg"></div>
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                                <line x1="12" y1="8" x2="12" y2="16" />
                                <line x1="8" y1="12" x2="16" y2="12" />
                            </svg>
                        </div>
                        <div class="action-content">
                            <h3>申请新证书</h3>
                            <p>在线申请数字证书，快速签发，安全可靠</p>
                        </div>
                        <div class="action-arrow">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <line x1="5" y1="12" x2="19" y2="12" />
                                <polyline points="12 5 19 12 12 19" />
                            </svg>
                        </div>
                    </div>

                    <div class="action-card success" @click="$router.push('/certificates')">
                        <div class="action-icon">
                            <div class="icon-bg"></div>
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path
                                    d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                            </svg>
                        </div>
                        <div class="action-content">
                            <h3>查看证书</h3>
                            <p>管理您的所有数字证书，查看详情和下载</p>
                        </div>
                        <div class="action-arrow">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <line x1="5" y1="12" x2="19" y2="12" />
                                <polyline points="12 5 19 12 12 19" />
                            </svg>
                        </div>
                    </div>

                    <div class="action-card warning" @click="$router.push('/revoke')">
                        <div class="action-icon">
                            <div class="icon-bg"></div>
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10" />
                                <line x1="15" y1="9" x2="9" y2="15" />
                                <line x1="9" y1="9" x2="15" y2="15" />
                            </svg>
                        </div>
                        <div class="action-content">
                            <h3>撤销证书</h3>
                            <p>撤销不再使用的证书，确保证书安全</p>
                        </div>
                        <div class="action-arrow">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <line x1="5" y1="12" x2="19" y2="12" />
                                <polyline points="12 5 19 12 12 19" />
                            </svg>
                        </div>
                    </div>
                </div>
            </section>

            <!-- 最近证书 -->
            <section class="recent-section" v-if="certificates.length > 0">
                <div class="section-header">
                    <h2 class="section-title">
                        <span class="title-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10" />
                                <polyline points="12 6 12 12 16 14" />
                            </svg>
                        </span>
                        <span>最近证书</span>
                    </h2>
                    <router-link to="/certificates" class="view-all">
                        <span>查看全部</span>
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <line x1="5" y1="12" x2="19" y2="12" />
                            <polyline points="12 5 19 12 12 19" />
                        </svg>
                    </router-link>
                </div>
                <div class="recent-list">
                    <div v-for="cert in recentCertificates" :key="cert.id" class="recent-item">
                        <div class="item-content">
                            <div class="cert-icon" :class="getStatusClass(cert.state)">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path
                                        d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                                </svg>
                            </div>
                            <div class="cert-info">
                                <h4>{{ cert.organization || '未命名证书' }}</h4>
                                <div class="cert-meta">
                                    <span class="meta-item">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
                                            <line x1="16" y1="2" x2="16" y2="6" />
                                            <line x1="8" y1="2" x2="8" y2="6" />
                                            <line x1="3" y1="10" x2="21" y2="10" />
                                        </svg>
                                        <span>{{ formatDate(cert.applyTime) }}</span>
                                    </span>
                                    <span class="meta-item">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                                            <circle cx="12" cy="7" r="4" />
                                        </svg>
                                        <span>{{ cert.chargePerson || '未知' }}</span>
                                    </span>
                                </div>
                            </div>
                            <div class="cert-status" :class="getStatusClass(cert.state)">
                                <span class="status-dot"></span>
                                <span>{{ cert.state }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- 空状态 -->
            <section class="empty-section" v-else>
                <div class="empty-card">
                    <div class="empty-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                            <polyline points="14 2 14 8 20 8" />
                            <line x1="16" y1="13" x2="8" y2="13" />
                            <line x1="16" y1="17" x2="8" y2="17" />
                            <polyline points="10 9 9 9 8 9" />
                        </svg>
                    </div>
                    <h3>暂无证书</h3>
                    <p>您还没有申请任何数字证书，点击下方按钮开始申请</p>
                    <button @click="$router.push('/apply')" class="btn-primary">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                            <line x1="12" y1="8" x2="12" y2="16" />
                            <line x1="8" y1="12" x2="16" y2="12" />
                        </svg>
                        <span>申请新证书</span>
                    </button>
                </div>
            </section>
        </main>
    </div>
</template>

<script>
import api from '../api'

export default {
    name: 'Home',
    data() {
        return {
            certificates: [],
            certificateCount: 0,
            activeCount: 0,
            revokedCount: 0
        }
    },
    computed: {
        user() {
            return this.$store.state.user
        },
        isAdmin() {
            return this.$store.state.user?.authority === '1'
        },
        recentCertificates() {
            return this.certificates.slice(0, 5)
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
                    this.certificateCount = this.certificates.length
                    this.activeCount = this.certificates.filter(c => c.state === '使用中').length
                    this.revokedCount = this.certificates.filter(c => c.state === '已注销').length
                }
            } catch (error) {
                console.error('加载证书失败', error)
            }
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
        getStatusClass(state) {
            const map = {
                '使用中': 'active',
                '已注销': 'revoked',
                '已过期': 'expired'
            }
            return map[state] || ''
        },
        getAvatarGradient(id) {
            const gradients = [
                'linear-gradient(135deg, #0369A1 0%, #0EA5E9 100%)',
                'linear-gradient(135deg, #059669 0%, #10b981 100%)',
                'linear-gradient(135deg, #dc2626 0%, #ef4444 100%)',
                'linear-gradient(135deg, #2563eb 0%, #3b82f6 100%)'
            ]
            return gradients[(id || 0) % gradients.length]
        },

        getParticleStyle(index) {
            const positions = [
                { top: `${Math.random() * 100}%`, left: `${Math.random() * 100}%` },
            ]
            const pos = positions[Math.floor(Math.random() * positions.length)]
            const delay = Math.random() * 20
            const duration = 15 + Math.random() * 10
            const size = 2 + Math.random() * 4
            return {
                ...pos,
                width: `${size}px`,
                height: `${size}px`,
                animationDelay: `${delay}s`,
                animationDuration: `${duration}s`
            }
        }
    }
}
</script>

<style scoped>
.home-page {
    min-height: 100vh;
    background: #F0F9FF;
    position: relative;
    overflow-x: hidden;
}

/* 动态背景 */
.bg-effects {
    position: fixed;
    inset: 0;
    pointer-events: none;
    z-index: 0;
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
    background: linear-gradient(135deg, #0369A1, #0EA5E9);
    top: -200px;
    right: -200px;
    animation-delay: 0s;
}

.orb-2 {
    width: 500px;
    height: 500px;
    background: linear-gradient(135deg, #059669, #10b981);
    bottom: -150px;
    left: -150px;
    animation-delay: -7s;
    animation-direction: reverse;
}

.orb-3 {
    width: 400px;
    height: 400px;
    background: linear-gradient(135deg, #f59e0b, #ef4444);
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    animation-delay: -14s;
    opacity: 0.2;
}

@keyframes float {

    0%,
    100% {
        transform: translate(0, 0) scale(1);
    }

    33% {
        transform: translate(30px, -30px) scale(1.1);
    }

    66% {
        transform: translate(-20px, 20px) scale(0.9);
    }
}

/* 浮动粒子 */
.floating-particles {
    position: absolute;
    inset: 0;
    overflow: hidden;
}

.particle {
    position: absolute;
    background: radial-gradient(circle, rgba(3, 105, 161, 0.4), transparent);
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
    inset: 0;
    background-image:
        linear-gradient(rgba(3, 105, 161, 0.05) 1px, transparent 1px),
        linear-gradient(90deg, rgba(3, 105, 161, 0.05) 1px, transparent 1px);
    background-size: 50px 50px;
}

/* 导航栏 */
.navbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 40px;
    height: 70px;
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
    width: 40px;
    height: 40px;
}

.brand-icon svg {
    width: 100%;
    height: 100%;
}

.brand-text {
    font-size: 18px;
    font-weight: 700;
    background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    white-space: nowrap;
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
    padding: 10px 16px;
    color: #64748b;
    text-decoration: none;
    font-size: 14px;
    font-weight: 500;
    border-radius: 10px;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
    white-space: nowrap;
}

.nav-link::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.1), rgba(139, 92, 246, 0.1));
    transform: scaleX(0);
    transform-origin: left;
    transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.nav-link:hover::before {
    transform: scaleX(1);
}

.nav-link svg {
    width: 18px;
    height: 18px;
    transition: transform 0.3s ease;
    position: relative;
    z-index: 1;
}

.nav-link:hover svg {
    transform: scale(1.2) rotate(5deg);
}

.nav-link span {
    position: relative;
    z-index: 1;
}

.nav-link:hover {
    color: #3b82f6;
}

.nav-link.active {
    color: #3b82f6;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.15), rgba(139, 92, 246, 0.15));
    font-weight: 600;
    box-shadow: 0 2px 8px rgba(59, 130, 246, 0.2);
}

.nav-link.active svg {
    transform: scale(1.1);
}

.nav-user {
    display: flex;
    align-items: center;
    gap: 16px;
}

.user-avatar {
    width: 38px;
    height: 38px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    font-weight: 600;
    color: #ffffff;
    cursor: pointer;
    transition: all 0.3s ease;
}

.user-avatar:hover {
    transform: scale(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.logout-btn {
    width: 38px;
    height: 38px;
    border-radius: 10px;
    background: #f1f5f9;
    border: 1px solid #e2e8f0;
    color: #64748b;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
}

.logout-btn:hover {
    background: #fef2f2;
    border-color: #fecaca;
    color: #ef4444;
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
    padding: 32px 20px;
}

/* 欢迎横幅 */
.welcome-banner {
    position: relative;
    background: linear-gradient(135deg, #0369A1 0%, #0EA5E9 100%);
    border-radius: 20px;
    padding: 40px;
    margin-bottom: 32px;
    color: white;
    box-shadow: 0 10px 30px -10px rgba(3, 105, 161, 0.3);
    overflow: hidden;
}

.welcome-banner::before {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 300px;
    height: 300px;
    background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
    border-radius: 50%;
    transform: translate(50%, -50%);
}

.welcome-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 32px;
    position: relative;
    z-index: 1;
}

.welcome-badge {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 6px 14px;
    background: rgba(255, 255, 255, 0.2);
    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 20px;
    font-size: 13px;
    font-weight: 600;
    backdrop-filter: blur(10px);
}

.welcome-badge svg {
    width: 16px;
    height: 16px;
}

.welcome-text h1 {
    font-size: 32px;
    font-weight: 700;
    margin: 12px 0 8px;
}

.username {
    color: #FDE047;
}

.welcome-text p {
    font-size: 15px;
    opacity: 0.95;
}

.welcome-stats {
    display: flex;
    gap: 16px;
    flex-wrap: wrap;
}

.stat-card {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 16px 20px;
    background: rgba(255, 255, 255, 0.15);
    border-radius: 14px;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    cursor: pointer;
}

.stat-card:hover {
    background: rgba(255, 255, 255, 0.2);
    transform: translateY(-2px);
}

.stat-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.stat-icon.total {
    background: rgba(255, 255, 255, 0.2);
    color: white;
}

.stat-icon.active {
    background: rgba(5, 150, 105, 0.3);
    color: white;
}

.stat-icon.revoked {
    background: rgba(239, 68, 68, 0.3);
    color: white;
}

.stat-icon svg {
    width: 24px;
    height: 24px;
}

.stat-info {
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.stat-value {
    font-size: 24px;
    font-weight: 700;
}

.stat-label {
    font-size: 13px;
    opacity: 0.9;
}

/* 快捷操作 */
.quick-actions {
    margin-bottom: 32px;
}

.section-title {
    font-size: 22px;
    font-weight: 700;
    color: #0C4A6E;
    margin-bottom: 24px;
    display: flex;
    align-items: center;
    gap: 12px;
}

.title-icon {
    width: 32px;
    height: 32px;
    background: linear-gradient(135deg, #0369A1 0%, #0EA5E9 100%);
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
}

.title-icon svg {
    width: 18px;
    height: 18px;
}

.action-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
}

.action-card {
    position: relative;
    padding: 28px;
    background: #ffffff;
    border: 1px solid #E0F2FE;
    border-radius: 16px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    cursor: pointer;
    overflow: hidden;
}

.action-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
    background: linear-gradient(90deg, transparent, currentColor, transparent);
    opacity: 0;
    transition: opacity 0.3s;
}

.action-card.primary::before {
    color: #0369A1;
}

.action-card.success::before {
    color: #059669;
}

.action-card.warning::before {
    color: #DC2626;
}

.action-card:hover::before {
    opacity: 1;
}

.action-card:hover {
    border-color: transparent;
    box-shadow: 0 12px 24px -8px rgba(3, 105, 161, 0.15);
    transform: translateY(-4px);
}

.action-card.primary:hover {
    box-shadow: 0 12px 24px -8px rgba(3, 105, 161, 0.2);
}

.action-card.success:hover {
    box-shadow: 0 12px 24px -8px rgba(5, 150, 105, 0.2);
}

.action-card.warning:hover {
    box-shadow: 0 12px 24px -8px rgba(220, 38, 38, 0.2);
}

.action-icon {
    position: relative;
    width: 56px;
    height: 56px;
    margin-bottom: 20px;
}

.icon-bg {
    position: absolute;
    inset: 0;
    border-radius: 12px;
    opacity: 0.1;
    transition: all 0.3s;
}

.action-card.primary .icon-bg {
    background: #0369A1;
}

.action-card.success .icon-bg {
    background: #059669;
}

.action-card.warning .icon-bg {
    background: #DC2626;
}

.action-card:hover .icon-bg {
    transform: scale(1.1);
}

.action-icon svg {
    position: relative;
    width: 28px;
    height: 28px;
    z-index: 1;
}

.action-card.primary .action-icon svg {
    color: #0369A1;
}

.action-card.success .action-icon svg {
    color: #059669;
}

.action-card.warning .action-icon svg {
    color: #DC2626;
}

.action-content h3 {
    font-size: 18px;
    font-weight: 600;
    color: #0C4A6E;
    margin-bottom: 8px;
}

.action-content p {
    font-size: 14px;
    color: #64748B;
    line-height: 1.5;
}

.action-arrow {
    position: absolute;
    top: 28px;
    right: 28px;
    width: 24px;
    height: 24px;
    color: #94A3B8;
    opacity: 0;
    transform: translateX(-4px);
    transition: all 0.3s;
}

.action-card:hover .action-arrow {
    opacity: 1;
    transform: translateX(0);
    color: #0369A1;
}

/* 最近证书 */
.recent-section {
    margin-bottom: 32px;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.view-all {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 8px 14px;
    color: #0369A1;
    font-size: 14px;
    font-weight: 500;
    text-decoration: none;
    border-radius: 8px;
    transition: all 0.2s;
    cursor: pointer;
}

.view-all:hover {
    background: #F0F9FF;
    color: #0EA5E9;
}

.view-all svg {
    width: 16px;
    height: 16px;
}

.recent-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.recent-item {
    background: #ffffff;
    border: 1px solid #E0F2FE;
    border-radius: 14px;
    padding: 20px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    cursor: pointer;
}

.recent-item:hover {
    border-color: #0EA5E9;
    box-shadow: 0 4px 12px -4px rgba(3, 105, 161, 0.1);
    transform: translateY(-2px);
}

.item-content {
    display: flex;
    align-items: center;
    gap: 16px;
}

.cert-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}

.cert-icon.active {
    background: rgba(5, 150, 105, 0.1);
    color: #059669;
}

.cert-icon.revoked,
.cert-icon.expired {
    background: rgba(220, 38, 38, 0.1);
    color: #DC2626;
}

.cert-icon svg {
    width: 24px;
    height: 24px;
}

.cert-info {
    flex: 1;
    min-width: 0;
}

.cert-info h4 {
    font-size: 16px;
    font-weight: 600;
    color: #0C4A6E;
    margin-bottom: 6px;
}

.cert-meta {
    display: flex;
    gap: 16px;
    flex-wrap: wrap;
}

.meta-item {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 13px;
    color: #64748B;
}

.meta-item svg {
    width: 14px;
    height: 14px;
}

.cert-status {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 13px;
    font-weight: 500;
    flex-shrink: 0;
}

.cert-status.active {
    background: rgba(5, 150, 105, 0.1);
    color: #059669;
}

.cert-status.revoked,
.cert-status.expired {
    background: rgba(220, 38, 38, 0.1);
    color: #DC2626;
}

.status-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: currentColor;
    flex-shrink: 0;
}

.cert-status {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
    white-space: nowrap;
}

/* 空状态 */
.empty-section {
    margin-bottom: 32px;
}

.empty-card {
    background: #ffffff;
    border: 1px solid #E0F2FE;
    border-radius: 20px;
    padding: 60px 40px;
    text-align: center;
}

.empty-icon {
    width: 80px;
    height: 80px;
    margin: 0 auto 24px;
    color: #CBD5E1;
}

.empty-icon svg {
    width: 100%;
    height: 100%;
}

.empty-card h3 {
    font-size: 20px;
    font-weight: 600;
    color: #0C4A6E;
    margin-bottom: 8px;
}

.empty-card p {
    font-size: 15px;
    color: #64748B;
    margin-bottom: 24px;
}

.btn-primary {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 14px 28px;
    background: linear-gradient(135deg, #0369A1 0%, #0EA5E9 100%);
    color: white;
    border: none;
    border-radius: 12px;
    font-size: 15px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 4px 12px rgba(3, 105, 161, 0.2);
}

.btn-primary:hover {
    box-shadow: 0 8px 20px rgba(3, 105, 161, 0.3);
    transform: translateY(-2px);
}

.btn-primary svg {
    width: 20px;
    height: 20px;
}

/* 响应式 */
@media (max-width: 768px) {
    .navbar {
        padding: 0 20px;
        height: 70px;
    }

    .nav-links {
        display: none;
    }

    .welcome-banner {
        padding: 24px;
    }

    .welcome-content {
        flex-direction: column;
        align-items: flex-start;
    }

    .welcome-stats {
        width: 100%;
    }

    .stat-card {
        flex: 1;
    }

    .action-grid {
        grid-template-columns: 1fr;
    }

    .cert-meta {
        flex-direction: column;
        gap: 8px;
    }

    .item-content {
        flex-direction: column;
        align-items: flex-start;
    }

    .cert-status {
        align-self: flex-end;
    }
}
</style>
