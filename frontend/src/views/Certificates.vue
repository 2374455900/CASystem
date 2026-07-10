<template>
    <div class="certificates-page">
        <!-- 动态背景 -->
        <div class="bg-effects">
            <div class="gradient-orb orb-1"></div>
            <div class="gradient-orb orb-2"></div>
            <div class="gradient-orb orb-3"></div>
            <div class="grid-overlay"></div>
            <div class="floating-particles">
                <div v-for="i in 15" :key="i" class="particle" :style="getParticleStyle(i)"></div>
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
                <router-link to="/home" class="nav-link">
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
                <router-link to="/certificates" class="nav-link active">
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
            <!-- 页面头部 -->
            <div class="page-header">
                <div class="header-glow"></div>
                <div class="header-content">
                    <div class="header-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                            <polyline points="14 2 14 8 20 8" />
                            <line x1="16" y1="13" x2="8" y2="13" />
                            <line x1="16" y1="17" x2="8" y2="17" />
                            <polyline points="10 9 9 9 8 9" />
                        </svg>
                    </div>
                    <div class="header-text">
                        <h1>我的证书</h1>
                        <p>管理您的所有数字证书，查看详情和下载</p>
                    </div>
                </div>
                <div class="header-stats">
                    <div class="stat-card">
                        <span class="stat-value">{{ certificates.length }}</span>
                        <span class="stat-label">证书总数</span>
                    </div>
                    <div class="stat-card active">
                        <span class="stat-value">{{ activeCount }}</span>
                        <span class="stat-label">有效证书</span>
                    </div>
                    <div class="stat-card revoked">
                        <span class="stat-value">{{ revokedCount }}</span>
                        <span class="stat-label">已撤销</span>
                    </div>
                </div>
            </div>

            <!-- 加载状态 -->
            <div v-if="loading" class="loading-state">
                <div class="spinner-large"></div>
                <p>正在加载证书数据...</p>
            </div>

            <!-- 空状态 -->
            <div v-else-if="certificates.length === 0" class="empty-state">
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
                <p>您还没有申请任何数字证书</p>
                <button @click="$router.push('/apply')" class="btn-primary">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                        <line x1="12" y1="8" x2="12" y2="16" />
                        <line x1="8" y1="12" x2="16" y2="12" />
                    </svg>
                    申请新证书
                </button>
            </div>

            <!-- 证书列表 -->
            <div v-else class="certificates-grid">
                <div v-for="(cert, index) in certificates" :key="cert.serialNumber" class="certificate-card"
                    :class="{ revoked: cert.state === '已注销' }" :style="{ animationDelay: `${index * 0.1}s` }">
                    <div class="card-glow"></div>
                    <div class="card-shine"></div>
                    <div class="card-content">
                        <!-- 卡片头部 -->
                        <div class="card-header">
                            <div class="cert-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path
                                        d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                                </svg>
                            </div>
                            <div class="cert-status" :class="cert.state === '使用中' ? 'active' : 'revoked'">
                                <span class="status-dot"></span>
                                {{ cert.state }}
                            </div>
                        </div>

                        <!-- 证书信息 -->
                        <div class="cert-info">
                            <div class="info-row">
                                <span class="info-label">序列号</span>
                                <span class="info-value serial">{{ cert.serialNumber }}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">组织机构</span>
                                <span class="info-value">{{ cert.organization }}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">法人</span>
                                <span class="info-value">{{ cert.juridicalPerson }}</span>
                            </div>
                        </div>

                        <!-- 有效期进度 -->
                        <div class="validity-section">
                            <div class="validity-header">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <circle cx="12" cy="12" r="10" />
                                    <polyline points="12 6 12 12 16 14" />
                                </svg>
                                <span>有效期</span>
                            </div>
                            <div class="validity-dates">
                                <span>{{ formatDate(cert.startTime) }}</span>
                                <span class="date-separator">→</span>
                                <span>{{ formatDate(cert.endTime) }}</span>
                            </div>
                            <div class="validity-progress">
                                <div class="progress-bar">
                                    <div class="progress-fill" :style="{ width: getValidityPercentage(cert) + '%' }"
                                        :class="{ 'expiring': getDaysRemaining(cert) <= 30 }"></div>
                                </div>
                                <span class="progress-text" :class="{ 'expiring': getDaysRemaining(cert) <= 30 }">
                                    {{ getDaysRemaining(cert) }} 天剩余
                                </span>
                            </div>
                        </div>

                        <!-- 操作按钮 -->
                        <div class="card-actions">
                            <button @click="viewCert(cert)" class="btn-action view">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                                    <circle cx="12" cy="12" r="3" />
                                </svg>
                                查看详情
                            </button>
                            <button @click="downloadCert(cert.serialNumber)" class="btn-action download"
                                :disabled="cert.state === '已注销'">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                                    <polyline points="7 10 12 15 17 10" />
                                    <line x1="12" y1="15" x2="12" y2="3" />
                                </svg>
                                下载
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!-- 证书详情弹窗 -->
        <Transition name="modal">
            <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
                <div class="modal-content" v-if="selectedCert">
                    <div class="modal-glow"></div>
                    <div class="modal-header">
                        <div class="modal-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path
                                    d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                            </svg>
                        </div>
                        <h2>证书详情</h2>
                        <button class="modal-close" @click="showModal = false">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <line x1="18" y1="6" x2="6" y2="18" />
                                <line x1="6" y1="6" x2="18" y2="18" />
                            </svg>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="detail-status" :class="selectedCert.state === '使用中' ? 'active' : 'revoked'">
                            <span class="status-dot"></span>
                            {{ selectedCert.state }}
                        </div>
                        <div class="detail-grid">
                            <div class="detail-item">
                                <span class="detail-label">证书序列号</span>
                                <span class="detail-value">{{ selectedCert.serialNumber }}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">组织机构</span>
                                <span class="detail-value">{{ selectedCert.organization }}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">工商注册号</span>
                                <span class="detail-value">{{ selectedCert.registrationNumber }}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">法人姓名</span>
                                <span class="detail-value">{{ selectedCert.juridicalPerson }}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">经办人</span>
                                <span class="detail-value">{{ selectedCert.chargePerson }}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">经办人电话</span>
                                <span class="detail-value">{{ selectedCert.chargePhone }}</span>
                            </div>
                            <div class="detail-item full-width">
                                <span class="detail-label">有效期</span>
                                <div class="detail-dates">
                                    <span class="date-badge">{{ formatDate(selectedCert.startTime) }}</span>
                                    <span class="date-arrow">→</span>
                                    <span class="date-badge">{{ formatDate(selectedCert.endTime) }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button @click="showModal = false" class="btn-secondary">关闭</button>
                        <button v-if="selectedCert.state === '使用中'" @click="downloadCert(selectedCert.serialNumber)"
                            class="btn-primary">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                                <polyline points="7 10 12 15 17 10" />
                                <line x1="12" y1="15" x2="12" y2="3" />
                            </svg>
                            下载证书
                        </button>
                    </div>
                </div>
            </div>
        </Transition>
    </div>
</template>

<script>
import api from '../api'

export default {
    name: 'Certificates',
    data() {
        return {
            certificates: [],
            loading: false,
            showModal: false,
            selectedCert: null
        }
    },
    computed: {
        user() {
            return this.$store.state.user
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
        getAvatarGradient(id) {
            const gradients = [
                'linear-gradient(135deg, #00d4ff 0%, #0099cc 100%)',
                'linear-gradient(135deg, #b829f7 0%, #7c1fa8 100%)',
                'linear-gradient(135deg, #f7a029 0%, #cc7a00 100%)',
                'linear-gradient(135deg, #29f7a8 0%, #1fa87c 100%)'
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
        },

        async loadCertificates() {
            this.loading = true
            try {
                const response = await api.certificates.getMyCertificates()
                if (response.code === 200) {
                    this.certificates = response.data
                }
            } catch (error) {
                console.error('加载证书失败', error)
            } finally {
                this.loading = false
            }
        },

        formatDate(dateStr) {
            if (!dateStr) return '-'
            const date = this.parseChineseDate(dateStr)
            if (!date) return '-'
            return date.toLocaleDateString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            })
        },

        // 解析中文日期格式 "yyyy 年 MM 月 dd 日 HH:mm:ss"
        parseChineseDate(dateStr) {
            if (!dateStr || typeof dateStr !== 'string') return null
            // 匹配中文日期格式：2026 年 04 月 13 日 21:15:42
            const match = dateStr.match(/(\d{4}) 年 (\d{2}) 月 (\d{2}) 日 (\d{2}):(\d{2}):(\d{2})/)
            if (!match) {
                // 如果不是中文格式，尝试直接解析
                const directDate = new Date(dateStr)
                if (isNaN(directDate.getTime())) return null
                return directDate
            }
            const [, year, month, day, hour, minute, second] = match
            return new Date(year, parseInt(month) - 1, parseInt(day), parseInt(hour), parseInt(minute), parseInt(second))
        },

        getDaysRemaining(cert) {
            if (!cert || !cert.endTime) return 0
            const endDate = this.parseChineseDate(cert.endTime)
            if (!endDate) return 0
            const now = new Date()
            const diffTime = endDate - now
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
            return Math.max(0, diffDays)
        },

        getValidityPercentage(cert) {
            if (!cert || !cert.startTime || !cert.endTime) return 0
            const startDate = this.parseChineseDate(cert.startTime)
            const endDate = this.parseChineseDate(cert.endTime)
            if (!startDate || !endDate) return 0
            const now = new Date()
            const totalDays = (endDate - startDate) / (1000 * 60 * 60 * 24)
            const remainingDays = (endDate - now) / (1000 * 60 * 60 * 24)
            return Math.max(0, Math.min(100, (remainingDays / totalDays) * 100))
        },

        async downloadCert(serialNumber) {
            try {
                // 使用 axios 获取文件内容，然后创建 blob 下载
                const axios = (await import('axios')).default
                const baseURL = import.meta.env.DEV ? '/api' : (import.meta.env.VITE_API_BASE_URL || '/api')
                const token = this.$store.state.token

                const response = await axios({
                    method: 'get',
                    url: `${baseURL}/certificates/download-file/${serialNumber}`,
                    responseType: 'blob',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                })

                // 创建 blob 并下载
                const blob = new Blob([response.data], { type: 'application/x-pem-file' })
                const url = URL.createObjectURL(blob)
                const a = document.createElement('a')
                a.href = url
                a.download = `certificate_${serialNumber}.cer`
                document.body.appendChild(a)
                a.click()
                document.body.removeChild(a)
                URL.revokeObjectURL(url)

                this.$message.success('证书下载成功')
            } catch (error) {
                this.$message.error('下载失败: ' + (error.message || '未知错误'))
            }
        },

        viewCert(cert) {
            this.selectedCert = cert
            this.showModal = true
        },

        handleLogout() {
            this.$store.dispatch('logout')
            this.$router.push('/login')
        }
    }
}
</script>

<style scoped>
/* 页面基础 */
.certificates-page {
    min-height: 100vh;
    background: #f8fafc;
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
}

.orb-1 {
    width: 600px;
    height: 600px;
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    top: -200px;
    right: -200px;
    animation: float 20s ease-in-out infinite;
}

.orb-2 {
    width: 500px;
    height: 500px;
    background: linear-gradient(135deg, #10b981, #059669);
    bottom: -150px;
    left: -150px;
    animation: float 25s ease-in-out infinite reverse;
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

/* 浮动粒子 */
.floating-particles {
    position: absolute;
    inset: 0;
    overflow: hidden;
}

.particle {
    position: absolute;
    background: radial-gradient(circle, rgba(59, 130, 246, 0.4), transparent);
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
        linear-gradient(rgba(59, 130, 246, 0.05) 1px, transparent 1px),
        linear-gradient(90deg, rgba(59, 130, 246, 0.05) 1px, transparent 1px);
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
    padding: 40px;
    max-width: 1400px;
    margin: 0 auto;
}

/* 页面头部 */
.page-header {
    position: relative;
    margin-bottom: 40px;
}

.header-glow {
    position: absolute;
    top: 50%;
    left: 200px;
    transform: translate(-50%, -50%);
    width: 300px;
    height: 100px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.3), rgba(139, 92, 246, 0.3));
    filter: blur(60px);
    border-radius: 50%;
}

.header-content {
    position: relative;
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 30px;
}

.header-icon {
    width: 60px;
    height: 60px;
    border-radius: 16px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(139, 92, 246, 0.2));
    border: 1px solid rgba(59, 130, 246, 0.3);
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
    font-size: 16px;
    color: #64748b;
}

.header-stats {
    display: flex;
    gap: 20px;
}

.stat-card {
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 16px;
    padding: 20px 30px;
    min-width: 120px;
    text-align: center;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.stat-card.active {
    background: rgba(16, 185, 129, 0.05);
    border-color: rgba(16, 185, 129, 0.2);
}

.stat-card.active .stat-value {
    color: #10b981;
}

.stat-card.revoked {
    background: rgba(239, 68, 68, 0.05);
    border-color: rgba(239, 68, 68, 0.2);
}

.stat-card.revoked .stat-value {
    color: #ef4444;
}

.stat-value {
    display: block;
    font-size: 28px;
    font-weight: 700;
    color: #0f172a;
    margin-bottom: 4px;
}

.stat-label {
    font-size: 13px;
    color: #64748b;
}

/* 加载状态 */
.loading-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 100px;
    color: #64748b;
}

.spinner-large {
    width: 50px;
    height: 50px;
    border: 3px solid #e2e8f0;
    border-top-color: #3b82f6;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 20px;
}

@keyframes spin {
    to {
        transform: rotate(360deg);
    }
}

/* 空状态 */
.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 100px;
    text-align: center;
}

.empty-icon {
    width: 100px;
    height: 100px;
    border-radius: 24px;
    background: rgba(59, 130, 246, 0.1);
    border: 1px solid rgba(59, 130, 246, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
    color: #3b82f6;
    margin-bottom: 24px;
}

.empty-icon svg {
    width: 48px;
    height: 48px;
}

.empty-state h3 {
    font-size: 24px;
    font-weight: 600;
    color: #0f172a;
    margin-bottom: 8px;
}

.empty-state p {
    font-size: 15px;
    color: #64748b;
    margin-bottom: 24px;
}

.btn-primary {
    display: inline-flex;
    align-items: center;
    gap: 10px;
    padding: 14px 28px;
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    border: none;
    border-radius: 12px;
    font-size: 15px;
    font-weight: 600;
    color: #ffffff;
    cursor: pointer;
    transition: all 0.3s ease;
}

.btn-primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 40px rgba(59, 130, 246, 0.3);
}

.btn-primary svg {
    width: 20px;
    height: 20px;
}

/* 证书网格 */
.certificates-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
    gap: 24px;
}

.certificate-card {
    position: relative;
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 20px;
    overflow: hidden;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow:
        0 4px 6px -1px rgba(0, 0, 0, 0.1),
        0 2px 4px -1px rgba(0, 0, 0, 0.06),
        0 0 0 1px rgba(59, 130, 246, 0.05);
    animation: cardEntrance 0.6s ease-out both;
}

.certificate-card:hover {
    transform: translateY(-6px) scale(1.02);
    border-color: rgba(59, 130, 246, 0.4);
    box-shadow:
        0 12px 24px -1px rgba(0, 0, 0, 0.15),
        0 0 0 1px rgba(59, 130, 246, 0.1),
        0 20px 60px rgba(59, 130, 246, 0.15);
}

.certificate-card.revoked {
    border-color: rgba(239, 68, 68, 0.2);
}

.certificate-card.revoked:hover {
    border-color: rgba(239, 68, 68, 0.4);
    box-shadow:
        0 12px 24px -1px rgba(0, 0, 0, 0.15),
        0 0 0 1px rgba(239, 68, 68, 0.1),
        0 20px 60px rgba(239, 68, 68, 0.15);
}

.card-glow {
    position: absolute;
    inset: 0;
    background: radial-gradient(circle at 50% 0%, rgba(59, 130, 246, 0.15), transparent 70%);
    opacity: 0;
    transition: opacity 0.4s ease;
    pointer-events: none;
}

.certificate-card:hover .card-glow {
    opacity: 1;
}

.card-shine {
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(59, 130, 246, 0.06), transparent);
    animation: shine 8s infinite;
    pointer-events: none;
}

@keyframes shine {

    0%,
    100% {
        left: -100%;
    }

    50% {
        left: 100%;
    }
}

@keyframes cardEntrance {
    from {
        opacity: 0;
        transform: translateY(30px) scale(0.95);
    }

    to {
        opacity: 1;
        transform: translateY(0) scale(1);
    }
}

.card-content {
    position: relative;
    padding: 24px;
}

/* 卡片头部 */
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.cert-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(139, 92, 246, 0.2));
    display: flex;
    align-items: center;
    justify-content: center;
    color: #3b82f6;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
}

.cert-icon::before {
    content: '';
    position: absolute;
    inset: -2px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.3), rgba(139, 92, 246, 0.3));
    border-radius: 12px;
    opacity: 0;
    transition: opacity 0.4s ease;
}

.certificate-card:hover .cert-icon::before {
    opacity: 1;
}

.certificate-card:hover .cert-icon {
    transform: scale(1.1) rotate(5deg);
}

.cert-icon svg {
    width: 24px;
    height: 24px;
    position: relative;
    z-index: 1;
}

.cert-status {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
}

.cert-status.active {
    background: rgba(16, 185, 129, 0.15);
    color: #10b981;
}

.cert-status.revoked {
    background: rgba(239, 68, 68, 0.15);
    color: #ef4444;
}

.status-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: currentColor;
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

/* 证书信息 */
.cert-info {
    margin-bottom: 20px;
}

.info-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #f1f5f9;
}

.info-row:last-child {
    border-bottom: none;
}

.info-label {
    font-size: 13px;
    color: #64748b;
}

.info-value {
    font-size: 14px;
    font-weight: 500;
    color: #0f172a;
}

.info-value.serial {
    font-family: 'JetBrains Mono', monospace;
    font-size: 13px;
    color: #3b82f6;
}

/* 有效期 */
.validity-section {
    background: #f8fafc;
    border-radius: 12px;
    padding: 16px;
    margin-bottom: 20px;
    border: 1px solid #e2e8f0;
}

.validity-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 12px;
    color: #64748b;
    font-size: 13px;
}

.validity-header svg {
    width: 16px;
    height: 16px;
}

.validity-dates {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;
    font-size: 13px;
    color: #0f172a;
}

.date-separator {
    color: #cbd5e1;
}

.validity-progress {
    display: flex;
    align-items: center;
    gap: 12px;
}

.progress-bar {
    flex: 1;
    height: 6px;
    background: #e2e8f0;
    border-radius: 3px;
    overflow: hidden;
}

.progress-fill {
    height: 100%;
    background: linear-gradient(90deg, #3b82f6, #8b5cf6);
    border-radius: 3px;
    transition: width 0.5s ease;
}

.progress-fill.expiring {
    background: linear-gradient(90deg, #f59e0b, #ef4444);
}

.progress-text {
    font-size: 12px;
    font-weight: 500;
    color: #3b82f6;
    white-space: nowrap;
}

.progress-text.expiring {
    color: #ef4444;
}

/* 操作按钮 */
.card-actions {
    display: flex;
    gap: 12px;
}

.btn-action {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 12px;
    border-radius: 10px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    border: none;
    position: relative;
    overflow: hidden;
}

.btn-action::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    transform: translateX(-100%);
    transition: transform 0.6s ease;
}

.btn-action:hover::before {
    transform: translateX(100%);
}

.btn-action svg {
    width: 18px;
    height: 18px;
    transition: transform 0.3s ease;
}

.btn-action:hover svg {
    transform: scale(1.15);
}

.btn-action.view {
    background: rgba(59, 130, 246, 0.1);
    color: #3b82f6;
    border: 1px solid rgba(59, 130, 246, 0.2);
}

.btn-action.view:hover {
    background: rgba(59, 130, 246, 0.2);
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(59, 130, 246, 0.2);
}

.btn-action.download {
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    color: #ffffff;
    border: 1px solid rgba(59, 130, 246, 0.3);
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.2);
}

.btn-action.download:hover:not(:disabled) {
    background: linear-gradient(135deg, #2563eb, #7c3aed);
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(59, 130, 246, 0.3);
}

.btn-action:active:not(:disabled) {
    transform: translateY(0) scale(0.98);
}

.btn-action:disabled {
    opacity: 0.4;
    cursor: not-allowed;
    transform: none;
}

/* 弹窗 */
.modal-overlay {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.6);
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
    max-width: 500px;
    overflow: hidden;
    box-shadow: 0 25px 80px rgba(0, 0, 0, 0.12);
}

.modal-glow {
    position: absolute;
    top: -50%;
    left: 50%;
    transform: translateX(-50%);
    width: 300px;
    height: 300px;
    background: radial-gradient(circle, rgba(59, 130, 246, 0.2), transparent 70%);
    pointer-events: none;
}

.modal-header {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 24px;
    border-bottom: 1px solid #e2e8f0;
}

.modal-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(139, 92, 246, 0.2));
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
    background: #f1f5f9;
    border: 1px solid #e2e8f0;
    color: #64748b;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
}

.modal-close:hover {
    background: #fef2f2;
    border-color: #fecaca;
    color: #ef4444;
}

.modal-close svg {
    width: 18px;
    height: 18px;
}

.modal-body {
    padding: 24px;
}

.detail-status {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 8px 16px;
    border-radius: 20px;
    font-size: 13px;
    font-weight: 600;
    margin-bottom: 24px;
}

.detail-status.active {
    background: rgba(16, 185, 129, 0.15);
    color: #10b981;
}

.detail-status.revoked {
    background: rgba(239, 68, 68, 0.15);
    color: #ef4444;
}

.detail-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
}

.detail-item {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.detail-item.full-width {
    grid-column: span 2;
}

.detail-label {
    font-size: 12px;
    color: #64748b;
}

.detail-value {
    font-size: 14px;
    font-weight: 500;
    color: #0f172a;
}

.detail-dates {
    display: flex;
    align-items: center;
    gap: 12px;
}

.date-badge {
    padding: 8px 16px;
    background: rgba(59, 130, 246, 0.1);
    border: 1px solid rgba(59, 130, 246, 0.2);
    border-radius: 8px;
    font-size: 14px;
    color: #3b82f6;
    font-family: 'JetBrains Mono', monospace;
}

.date-arrow {
    color: #cbd5e1;
}

.modal-footer {
    display: flex;
    gap: 12px;
    padding: 0 24px 24px;
}

.btn-secondary {
    flex: 1;
    padding: 12px;
    background: #f1f5f9;
    border: 1px solid #e2e8f0;
    border-radius: 10px;
    font-size: 14px;
    font-weight: 500;
    color: #334155;
    cursor: pointer;
    transition: all 0.3s ease;
}

.btn-secondary:hover {
    background: #e2e8f0;
    border-color: #cbd5e1;
}

/* 弹窗动画 */
.modal-enter-active,
.modal-leave-active {
    transition: all 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
    opacity: 0;
}

.modal-enter-from .modal-content,
.modal-leave-to .modal-content {
    transform: scale(0.95);
}

/* 响应式 */
@media (max-width: 768px) {
    .navbar {
        padding: 0 20px;
    }

    .nav-links {
        display: none;
    }

    .main-content {
        padding: 20px;
    }

    .header-content {
        flex-direction: column;
        text-align: center;
    }

    .header-stats {
        flex-direction: column;
    }

    .certificates-grid {
        grid-template-columns: 1fr;
    }

    .detail-grid {
        grid-template-columns: 1fr;
    }

    .detail-item.full-width {
        grid-column: span 1;
    }
}
</style>
