<template>
    <div class="home-page">
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


    }
}
</script>

<style scoped>
.home-page {
    min-height: 100vh;
    background: var(--color-bg-primary);
    position: relative;
    overflow-x: hidden;
}

.home-page::before {
    content: '';
    position: fixed;
    inset: 0;
    pointer-events: none;
    z-index: 0;
    background: 
        radial-gradient(circle at 20% 20%, var(--color-primary, #0369A1) 0.02% 40%, transparent 40%),
        radial-gradient(circle at 80% 80%, var(--color-accent-start, #8b5cf6) 0.01% 40%, transparent 40%),
        radial-gradient(circle at 50% 50%, var(--color-warning) 0.01% 50%, transparent 50%);
    background-attachment: fixed;
}

@media (prefers-reduced-motion: reduce) {
    .home-page::before {
        background: none;
    }
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(24px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.welcome-banner {
    position: relative;
    background: var(--gradient-primary);
    border-radius: 20px;
    padding: 40px;
    margin-bottom: 32px;
    color: var(--color-text-inverse);
    box-shadow: var(--shadow-xl);
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
    animation: fadeInUp 0.6s cubic-bezier(0.4, 0, 0.2, 1) 0.1s both;
    will-change: transform;
}

.welcome-text h1 {
    font-size: 32px;
    font-weight: 700;
    margin: 12px 0 8px;
    animation: fadeInUp 0.5s cubic-bezier(0.4, 0, 0.2, 1) 0.2s both;
    will-change: transform;
}

.welcome-text p {
    font-size: 15px;
    opacity: 0.95;
    line-height: 1.6;
    animation: fadeInUp 0.5s cubic-bezier(0.4, 0, 0.2, 1) 0.3s both;
}

.welcome-stats {
    display: flex;
    gap: 16px;
    flex-wrap: wrap;
    animation: fadeInUp 0.5s cubic-bezier(0.4, 0, 0.2, 1) 0.4s both;
}

.stat-card {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 16px 20px;
    background: rgba(255, 255, 255, 0.15);
    border-radius: 14px;
    border: 1px solid rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
    cursor: pointer;
    will-change: transform, box-shadow;
}

.stat-card:nth-child(1) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 0.5s both; }
.stat-card:nth-child(2) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 0.55s both; }
.stat-card:nth-child(3) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 0.6s both; }

.stat-card:hover {
    background: rgba(255, 255, 255, 0.2);
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(255, 255, 255, 0.15);
}

.stat-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: transform 0.25s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.25s cubic-bezier(0.4, 0, 0.2, 1);
    will-change: transform, opacity;
}

.stat-icon.total {
    background: rgba(255, 255, 255, 0.2);
    color: var(--color-text-inverse);
}

.stat-icon.active {
    background: rgba(16, 185, 129, 0.3);
    color: var(--color-text-inverse);
}

.stat-icon.revoked {
    background: rgba(239, 68, 68, 0.3);
    color: var(--color-text-inverse);
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
    animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 0.6s both;
}

.stat-label {
    font-size: 13px;
    opacity: 0.9;
    animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 0.65s both;
}

.action-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
}

.action-card {
    position: relative;
    padding: 28px;
    background: var(--color-bg-card);
    border: 1px solid var(--color-border);
    border-radius: 16px;
    transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
    cursor: pointer;
    overflow: hidden;
    will-change: transform, box-shadow, border-color;
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
    transition: opacity 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.action-card.primary::before {
    color: var(--color-primary);
}

.action-card.success::before {
    color: var(--color-success);
}

.action-card.warning::before {
    color: var(--color-error);
}

.action-card:hover::before {
    opacity: 1;
}

.action-card:hover {
    border-color: transparent;
    box-shadow: var(--shadow-lg);
    transform: translateY(-2px);
}

.action-card:nth-child(1) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 0.8s both; }
.action-card:nth-child(2) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 0.85s both; }
.action-card:nth-child(3) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 0.9s both; }

.action-icon {
    position: relative;
    width: 56px;
    height: 56px;
    margin-bottom: 20px;
    transition: transform 0.25s cubic-bezier(0.4, 0, 0.2, 1);
    will-change: transform;
}

.icon-bg {
    position: absolute;
    inset: 0;
    border-radius: 12px;
    opacity: 0.1;
    transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.action-card.primary .icon-bg {
    background: var(--color-primary);
}

.action-card.success .icon-bg {
    background: var(--color-success);
}

.action-card.warning .icon-bg {
    background: var(--color-error);
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

.action-content h3 {
    font-size: 18px;
    font-weight: 600;
    color: var(--color-text-primary);
    margin-bottom: 8px;
}

.action-content p {
    font-size: 14px;
    color: var(--color-text-secondary);
    line-height: 1.5;
}

.action-arrow {
    position: absolute;
    top: 28px;
    right: 28px;
    width: 24px;
    height: 24px;
    color: var(--color-text-tertiary);
    opacity: 0;
    transform: translateX(-4px);
    transition: all 0.2s ease;
}

.action-card:hover .action-arrow {
    opacity: 1;
    transform: translateX(0);
    color: var(--color-primary);
}

.section-title {
    font-size: 22px;
    font-weight: 700;
    color: var(--color-foreground);
    margin-bottom: 24px;
    display: flex;
    align-items: center;
    gap: 12px;
}

.title-icon {
    width: 32px;
    height: 32px;
    background: var(--gradient-primary);
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--color-text-inverse);
    animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 0.9s both;
    will-change: transform;
}

.title-icon svg {
    width: 18px;
    height: 18px;
    transition: transform 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.recent-section {
    margin-bottom: 32px;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 1s both;
    will-change: transform;
}

.view-all {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 8px 14px;
    color: var(--color-primary);
    font-size: 14px;
    font-weight: 500;
    text-decoration: none;
    border-radius: 8px;
    transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
    cursor: pointer;
    will-change: transform, background-color, color;
}

.view-all:hover {
    background: var(--color-bg-secondary);
    color: var(--color-primary-light);
}

.view-all svg {
    transition: transform 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.view-all:hover svg {
    transform: translateX(2px);
}

.section-title {
    font-size: 22px;
    font-weight: 700;
    color: var(--color-foreground);
    margin-bottom: 24px;
    display: flex;
    align-items: center;
    gap: 12px;
    animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 0.9s both;
}

.recent-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
    animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 1.05s both;
}

.recent-item {
    background: var(--color-bg-card);
    border: 1px solid var(--color-border);
    border-radius: 14px;
    padding: 20px;
    transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
    cursor: pointer;
    will-change: transform, box-shadow, border-color;
}

.recent-item:nth-child(1) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 1.1s both; }
.recent-item:nth-child(2) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 1.15s both; }
.recent-item:nth-child(3) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 1.2s both; }
.recent-item:nth-child(4) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 1.25s both; }
.recent-item:nth-child(5) { animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1) 1.3s both; }

.recent-item:hover {
    border-color: var(--color-primary-light);
    box-shadow: var(--shadow-md);
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
    transition: transform 0.25s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.25s cubic-bezier(0.4, 0, 0.2, 1);
    will-change: transform, opacity;
}

.cert-icon.active {
    background: rgba(16, 185, 129, 0.1);
    color: var(--color-success);
}

.cert-icon.revoked,
.cert-icon.expired {
    background: rgba(239, 68, 68, 0.1);
    color: var(--color-error);
}

.cert-icon svg {
    width: 24px;
    height: 24px;
    transition: transform 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.cert-info {
    flex: 1;
    min-width: 0;
}

.cert-info h4 {
    font-size: 16px;
    font-weight: 600;
    color: var(--color-text-primary);
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
    color: var(--color-text-secondary);
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
    background: rgba(16, 185, 129, 0.1);
    color: var(--color-success);
}

.cert-status.revoked,
.cert-status.expired {
    background: rgba(239, 68, 68, 0.1);
    color: var(--color-error);
}

.status-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: currentColor;
    flex-shrink: 0;
    animation: statusPulse 1.5s cubic-bezier(0.4, 0, 0.2, 1) infinite;
}

@keyframes statusPulse {
    0%, 100% { opacity: 1; transform: scale(1); }
    50% { opacity: 0.4; transform: scale(0.85); }
}

.empty-section {
    margin-bottom: 32px;
}

.empty-card {
    background: var(--color-bg-card);
    border: 1px solid var(--color-border);
    border-radius: 20px;
    padding: 60px 40px;
    text-align: center;
}

.empty-icon {
    width: 80px;
    height: 80px;
    margin: 0 auto 24px;
    color: var(--color-text-tertiary);
}

.empty-icon svg {
    width: 100%;
    height: 100%;
}

.empty-card h3 {
    font-size: 20px;
    font-weight: 600;
    color: var(--color-text-primary);
    margin-bottom: 8px;
}

.empty-card p {
    font-size: 15px;
    color: var(--color-text-secondary);
    margin-bottom: 24px;
}

.btn-primary {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 14px 28px;
    background: var(--gradient-primary);
    color: var(--color-text-inverse);
    border: none;
    border-radius: 12px;
    font-size: 15px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 4px 12px rgba(3, 105, 161, 0.2);
    will-change: transform, box-shadow;
}

.btn-primary:hover {
    box-shadow: 0 8px 20px rgba(3, 105, 161, 0.3);
    transform: translateY(-2px);
}

.btn-primary:active {
    transform: translateY(0);
    transition: none;
}

.btn-primary svg {
    width: 20px;
    height: 20px;
    transition: transform 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

@media (max-width: 768px) {
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

@media (prefers-reduced-motion: reduce) {
    .stat-cards-container,
    .action-grid,
    .recent-list {
        display: none;
    }
    
    .action-card,
    .stat-card,
    .recent-item,
    .view-all {
        transition: none;
    }

    .action-card:hover,
    .stat-card:hover,
    .recent-item:hover,
    .btn-primary:hover,
    .view-all:hover {
        transform: none !important;
        box-shadow: none !important;
    }
    
    .welcome-content,
    .welcome-text h1,
    .welcome-text p,
    .stat-value,
    .stat-label,
    .section-title,
    .title-icon,
    .recent-item,
    .cert-icon,
    .cert-status,
    .btn-primary {
        animation: none !important;
    }
}
</style>
