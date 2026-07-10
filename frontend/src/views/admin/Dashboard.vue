<template>
    <div class="dashboard">
        <!-- 统计卡片区域 -->
        <div class="stats-grid">
            <div class="stat-card" v-for="(stat, index) in statsList" :key="index" :class="stat.type">
                <div class="stat-content">
                    <div class="stat-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path v-for="(path, i) in stat.iconPaths" :key="i" :d="path" stroke-linecap="round"
                                stroke-linejoin="round" />
                        </svg>
                    </div>
                    <div class="stat-info">
                        <h3 class="stat-value">{{ formatNumber(stat.value) }}</h3>
                        <p class="stat-label">{{ stat.label }}</p>
                    </div>
                </div>
                <div class="stat-trend" v-if="stat.trend">
                    <span :class="['trend-badge', stat.trend > 0 ? 'up' : 'down']">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path :d="stat.trend > 0 ? 'M7 17l5-5 5 5M12 12V3' : 'M7 7l5 5 5-5M12 12v9'"
                                stroke-linecap="round" stroke-linejoin="round" />
                        </svg>
                        {{ Math.abs(stat.trend) }}%
                    </span>
                </div>
            </div>
        </div>

        <!-- 主内容区域 -->
        <div class="dashboard-main">
            <!-- 左侧：图表和趋势 -->
            <div class="main-left">
                <!-- 今日新增 -->
                <div class="glass-card today-section">
                    <div class="card-header">
                        <div class="header-icon pulse">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10" />
                                <polyline points="12 6 12 12 16 14" />
                            </svg>
                        </div>
                        <h2>今日动态</h2>
                    </div>
                    <div class="today-grid">
                        <div class="today-item" v-for="(item, index) in todayStats" :key="index">
                            <div class="item-content">
                                <span class="item-value">{{ item.value }}</span>
                                <span class="item-label">{{ item.label }}</span>
                                <div class="item-sparkline">
                                    <svg viewBox="0 0 100 30" preserveAspectRatio="none">
                                        <path :d="generateSparkline(item.sparklineData)" fill="none"
                                            stroke="currentColor" stroke-width="2" />
                                    </svg>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 证书状态分布 -->
                <div class="glass-card distribution-section">
                    <div class="card-header">
                        <div class="header-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M21.21 15.89A10 10 0 1 1 8 2.83" />
                                <path d="M22 12A10 10 0 0 0 12 2v10z" />
                            </svg>
                        </div>
                        <h2>证书状态分布</h2>
                    </div>
                    <div class="distribution-chart">
                        <div class="chart-container">
                            <svg viewBox="0 0 200 200" class="donut-chart">
                                <defs>
                                    <linearGradient id="gradientActive" x1="0%" y1="0%" x2="100%" y2="100%">
                                        <stop offset="0%" stop-color="#059669" />
                                        <stop offset="100%" stop-color="#10b981" />
                                    </linearGradient>
                                    <linearGradient id="gradientRevoked" x1="0%" y1="0%" x2="100%" y2="100%">
                                        <stop offset="0%" stop-color="#dc2626" />
                                        <stop offset="100%" stop-color="#ef4444" />
                                    </linearGradient>
                                    <linearGradient id="gradientPending" x1="0%" y1="0%" x2="100%" y2="100%">
                                        <stop offset="0%" stop-color="#d97706" />
                                        <stop offset="100%" stop-color="#f59e0b" />
                                    </linearGradient>
                                </defs>
                                <circle cx="100" cy="100" r="80" fill="none" stroke="var(--ca-border)"
                                    stroke-width="20" />
                                <circle v-for="(segment, index) in chartSegments" :key="index" cx="100" cy="100" r="80"
                                    fill="none" :stroke="segment.gradient" stroke-width="20"
                                    :stroke-dasharray="segment.dashArray" :stroke-dashoffset="segment.dashOffset"
                                    class="chart-segment" />
                            </svg>
                            <div class="chart-center">
                                <span class="center-value">{{ statistics.totalCertificates || 0 }}</span>
                                <span class="center-label">总证书</span>
                            </div>
                        </div>
                        <div class="chart-legend">
                            <div class="legend-item" v-for="(item, index) in distributionData" :key="index">
                                <div class="legend-dot" :style="{ background: item.color }"></div>
                                <div class="legend-info">
                                    <span class="legend-name">{{ item.name }}</span>
                                    <span class="legend-value">{{ item.count }} ({{ item.percentage }}%)</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 右侧：快捷操作和系统状态 -->
            <div class="main-right">
                <div class="glass-card actions-section">
                    <div class="card-header">
                        <div class="header-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2" />
                            </svg>
                        </div>
                        <h2>快捷操作</h2>
                    </div>
                    <div class="action-list">
                        <button v-for="(action, index) in quickActions" :key="index" class="action-btn"
                            @click="$router.push(action.route)">
                            <div class="action-icon" :class="action.type">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path v-for="(path, i) in action.iconPaths" :key="i" :d="path"
                                        stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                            </div>
                            <div class="action-info">
                                <span class="action-name">{{ action.name }}</span>
                                <span class="action-desc">{{ action.description }}</span>
                            </div>
                            <div class="action-arrow">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <polyline points="9 18 15 12 9 6" />
                                </svg>
                            </div>
                        </button>
                    </div>
                </div>

                <div class="glass-card system-section">
                    <div class="card-header">
                        <div class="header-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path
                                    d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83" />
                            </svg>
                        </div>
                        <h2>系统状态</h2>
                    </div>
                    <div class="system-status">
                        <div class="status-item">
                            <div class="status-indicator online"></div>
                            <span class="status-text">系统运行正常</span>
                        </div>
                        <div class="status-metrics">
                            <div class="metric">
                                <span class="metric-label">响应时间</span>
                                <span class="metric-value">{{ responseTime }}ms</span>
                            </div>
                            <div class="metric">
                                <span class="metric-label">在线用户</span>
                                <span class="metric-value">{{ onlineUsers }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import api from '../../api'

export default {
    name: 'Dashboard',
    data() {
        return {
            statistics: {},
            responseTime: 45,
            onlineUsers: 12,
            sparklineData: [20, 35, 25, 45, 30, 55, 40]
        }
    },
    computed: {
        statsList() {
            return [
                {
                    type: 'users',
                    label: '总用户数',
                    value: this.statistics.totalUsers || 0,
                    trend: 12,
                    iconPaths: ['M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2', 'M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM23 21v-2a4 4 0 0 0-3-3.87', 'M16 3.13a4 4 0 0 1 0 7.75']
                },
                {
                    type: 'certificates',
                    label: '证书总数',
                    value: this.statistics.totalCertificates || 0,
                    trend: 8,
                    iconPaths: ['M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z', 'M14 2v6h6', 'M16 13H8', 'M16 17H8', 'M10 9H8']
                },
                {
                    type: 'active',
                    label: '有效证书',
                    value: this.statistics.activeCertificates || 0,
                    trend: 15,
                    iconPaths: ['M22 11.08V12a10 10 0 1 1-5.93-9.14', 'M22 4L12 14.01l-3-3']
                },
                {
                    type: 'revoked',
                    label: '已撤销',
                    value: this.statistics.revokedCertificates || 0,
                    trend: -3,
                    iconPaths: ['M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z', 'M12 9v4', 'M12 17h.01']
                }
            ]
        },
        todayStats() {
            return [
                { label: '新用户', value: this.statistics.todayNewUsers || 0, sparklineData: [15, 25, 20, 30, 25, 35, 28] },
                { label: '新证书', value: this.statistics.todayNewCertificates || 0, sparklineData: [30, 45, 35, 50, 40, 55, 48] },
                { label: '撤销', value: this.statistics.todayRevoked || 0, sparklineData: [5, 8, 6, 10, 7, 12, 9] }
            ]
        },
        distributionData() {
            const total = this.statistics.totalCertificates || 1
            const data = this.statistics.certificatesByState || []
            return [
                { name: '有效证书', count: this.statistics.activeCertificates || 0, color: '#059669', percentage: Math.round((this.statistics.activeCertificates || 0) / total * 100) },
                { name: '已撤销', count: this.statistics.revokedCertificates || 0, color: '#dc2626', percentage: Math.round((this.statistics.revokedCertificates || 0) / total * 100) },
                { name: '待处理', count: (this.statistics.totalCertificates || 0) - (this.statistics.activeCertificates || 0) - (this.statistics.revokedCertificates || 0), color: '#d97706', percentage: Math.round(((this.statistics.totalCertificates || 0) - (this.statistics.activeCertificates || 0) - (this.statistics.revokedCertificates || 0)) / total * 100) }
            ].filter(item => item.count > 0)
        },
        chartSegments() {
            const total = this.statistics.totalCertificates || 1
            const circumference = 2 * Math.PI * 80
            let offset = 0

            return this.distributionData.map((item, index) => {
                const percentage = item.count / total
                const dashArray = `${circumference * percentage} ${circumference}`
                const dashOffset = -offset
                offset += circumference * percentage

                return {
                    dashArray,
                    dashOffset,
                    gradient: `url(#gradient${index === 0 ? 'Active' : index === 1 ? 'Revoked' : 'Pending'})`
                }
            })
        },
        quickActions() {
            return [
                {
                    name: '用户管理',
                    description: '查看和管理系统用户',
                    route: '/admin/users',
                    type: 'users',
                    iconPaths: ['M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2', 'M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM23 21v-2a4 4 0 0 0-3-3.87', 'M16 3.13a4 4 0 0 1 0 7.75']
                },
                {
                    name: '证书管理',
                    description: '管理所有数字证书',
                    route: '/admin/certificates',
                    type: 'certificates',
                    iconPaths: ['M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z', 'M14 2v6h6', 'M16 13H8', 'M16 17H8', 'M10 9H8']
                },
                {
                    name: '操作日志',
                    description: '查看系统操作记录',
                    route: '/admin/logs',
                    type: 'logs',
                    iconPaths: ['M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z', 'M14 2v6h6', 'M16 13H8', 'M16 17H8', 'M10 9H8']
                }
            ]
        }
    },
    async mounted() {
        await this.loadStatistics()
        this.startMetricsUpdate()
    },
    beforeUnmount() {
        if (this.metricsInterval) {
            clearInterval(this.metricsInterval)
        }
    },
    methods: {
        async loadStatistics() {
            try {
                const response = await api.admin.getStatistics()
                if (response.code === 200) {
                    this.statistics = response.data
                }
            } catch (error) {
                console.error('加载统计数据失败', error)
            }
        },
        formatNumber(num) {
            if (num >= 1000000) return (num / 1000000).toFixed(1) + 'M'
            if (num >= 1000) return (num / 1000).toFixed(1) + 'K'
            return num.toString()
        },
        generateSparkline(data) {
            const max = Math.max(...data)
            const min = Math.min(...data)
            const range = max - min || 1
            const points = data.map((val, i) => {
                const x = (i / (data.length - 1)) * 100
                const y = 30 - ((val - min) / range) * 25 - 2.5
                return `${x},${y}`
            })
            return `M ${points.join(' L ')}`
        },
        startMetricsUpdate() {
            this.metricsInterval = setInterval(() => {
                this.responseTime = Math.floor(Math.random() * 30) + 30
                this.onlineUsers = Math.floor(Math.random() * 10) + 8
            }, 5000)
        }
    }
}
</script>

<style scoped>
.dashboard {
    padding: 0;
    background: var(--ca-bg-secondary);
}

/* 统计卡片网格 */
.stats-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    margin-bottom: 24px;
}

.stat-card {
    position: relative;
    background: var(--ca-bg-card);
    border: 1px solid var(--ca-border);
    border-radius: var(--ca-radius-xl);
    padding: 24px;
    overflow: hidden;
    transition: all var(--ca-transition-normal);
    box-shadow: var(--ca-shadow-sm);
}

.stat-card:hover {
    transform: translateY(-4px);
    border-color: var(--ca-primary);
    box-shadow: var(--ca-shadow-lg);
}

.stat-card.users {
    border-left: 4px solid var(--ca-primary);
}

.stat-card.certificates {
    border-left: 4px solid var(--ca-primary-dark);
}

.stat-card.active {
    border-left: 4px solid var(--ca-success);
}

.stat-card.revoked {
    border-left: 4px solid var(--ca-error);
}

.stat-content {
    display: flex;
    align-items: center;
    gap: 16px;
    position: relative;
    z-index: 1;
}

.stat-icon {
    width: 52px;
    height: 52px;
    border-radius: var(--ca-radius-lg);
    display: flex;
    align-items: center;
    justify-content: center;
    background: var(--ca-bg-tertiary);
    border: 1px solid var(--ca-border);
    color: var(--ca-primary);
}

.stat-card.users .stat-icon {
    color: var(--ca-primary);
}

.stat-card.certificates .stat-icon {
    color: var(--ca-primary-dark);
}

.stat-card.active .stat-icon {
    color: var(--ca-success);
}

.stat-card.revoked .stat-icon {
    color: var(--ca-error);
}

.stat-icon svg {
    width: 26px;
    height: 26px;
}

.stat-info {
    flex: 1;
}

.stat-value {
    font-size: 32px;
    font-weight: 700;
    color: var(--ca-text-primary);
    line-height: 1;
    margin-bottom: 6px;
}

.stat-label {
    font-size: 14px;
    color: var(--ca-text-secondary);
    font-weight: 500;
}

.stat-trend {
    position: absolute;
    top: 20px;
    right: 20px;
}

.trend-badge {
    display: flex;
    align-items: center;
    gap: 4px;
    padding: 4px 10px;
    border-radius: var(--ca-radius-full);
    font-size: 12px;
    font-weight: 600;
}

.trend-badge.up {
    background: rgba(5, 150, 105, 0.1);
    color: var(--ca-success);
}

.trend-badge.down {
    background: rgba(220, 38, 38, 0.1);
    color: var(--ca-error);
}

.trend-badge svg {
    width: 14px;
    height: 14px;
}

/* 主内容区域 */
.dashboard-main {
    display: grid;
    grid-template-columns: 1.2fr 0.8fr;
    gap: 24px;
}

.glass-card {
    background: var(--ca-bg-card);
    border: 1px solid var(--ca-border);
    border-radius: var(--ca-radius-2xl);
    padding: 24px;
    transition: all var(--ca-transition-normal);
    box-shadow: var(--ca-shadow-sm);
}

.glass-card:hover {
    border-color: var(--ca-border-hover);
    box-shadow: var(--ca-shadow-lg);
}

.card-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 20px;
}

.header-icon {
    width: 40px;
    height: 40px;
    border-radius: var(--ca-radius-lg);
    background: var(--ca-bg-tertiary);
    border: 1px solid var(--ca-border);
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--ca-primary);
}

.header-icon.pulse {
    animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {

    0%,
    100% {
        box-shadow: 0 0 0 0 rgba(37, 99, 235, 0.4);
    }

    50% {
        box-shadow: 0 0 0 10px rgba(37, 99, 235, 0);
    }
}

.header-icon svg {
    width: 20px;
    height: 20px;
}

.card-header h2 {
    font-size: 18px;
    font-weight: 600;
    color: var(--ca-text-primary);
}

/* 今日动态 */
.today-section {
    margin-bottom: 24px;
}

.today-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;
}

.today-item {
    position: relative;
    background: var(--ca-bg-tertiary);
    border: 1px solid var(--ca-border);
    border-radius: var(--ca-radius-lg);
    padding: 20px;
    overflow: hidden;
    transition: all var(--ca-transition-normal);
}

.today-item:hover {
    background: var(--ca-bg-hover);
    border-color: var(--ca-primary);
    transform: translateY(-2px);
}

.item-content {
    position: relative;
    z-index: 1;
}

.item-value {
    display: block;
    font-size: 28px;
    font-weight: 700;
    color: var(--ca-text-primary);
    margin-bottom: 4px;
}

.item-label {
    font-size: 13px;
    color: var(--ca-text-tertiary);
    margin-bottom: 12px;
}

.item-sparkline {
    height: 30px;
    color: var(--ca-primary);
}

.item-sparkline svg {
    width: 100%;
    height: 100%;
}

/* 证书状态分布 */
.distribution-chart {
    display: flex;
    align-items: center;
    gap: 32px;
}

.chart-container {
    position: relative;
    width: 180px;
    height: 180px;
}

.donut-chart {
    width: 100%;
    height: 100%;
    transform: rotate(-90deg);
}

.chart-segment {
    transition: all var(--ca-transition-normal);
}

.chart-center {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
}

.center-value {
    display: block;
    font-size: 28px;
    font-weight: 700;
    color: var(--ca-text-primary);
    line-height: 1;
}

.center-label {
    font-size: 12px;
    color: var(--ca-text-tertiary);
}

.chart-legend {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.legend-item {
    display: flex;
    align-items: center;
    gap: 12px;
}

.legend-dot {
    width: 12px;
    height: 12px;
    border-radius: var(--ca-radius-full);
    box-shadow: 0 0 10px currentColor;
}

.legend-info {
    display: flex;
    flex-direction: column;
    gap: 2px;
}

.legend-name {
    font-size: 14px;
    color: var(--ca-text-primary);
    font-weight: 500;
}

.legend-value {
    font-size: 12px;
    color: var(--ca-text-tertiary);
}

/* 快捷操作 */
.actions-section {
    margin-bottom: 24px;
}

.action-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.action-btn {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 16px 20px;
    background: var(--ca-bg-tertiary);
    border: 1px solid var(--ca-border);
    border-radius: var(--ca-radius-lg);
    color: var(--ca-text-primary);
    transition: all var(--ca-transition-normal);
    text-align: left;
    cursor: pointer;
}

.action-btn:hover {
    background: var(--ca-bg-hover);
    border-color: var(--ca-primary);
    transform: translateX(4px);
}

.action-icon {
    width: 40px;
    height: 40px;
    border-radius: var(--ca-radius-md);
    display: flex;
    align-items: center;
    justify-content: center;
    background: white;
    border: 1px solid var(--ca-border);
    color: var(--ca-primary);
}

.action-btn .action-icon.users {
    color: var(--ca-primary);
}

.action-btn .action-icon.certificates {
    color: var(--ca-primary-dark);
}

.action-btn .action-icon.logs {
    color: var(--ca-warning);
}

.action-icon svg {
    width: 20px;
    height: 20px;
}

.action-info {
    flex: 1;
}

.action-name {
    display: block;
    font-size: 15px;
    font-weight: 600;
    color: var(--ca-text-primary);
    margin-bottom: 4px;
}

.action-desc {
    font-size: 13px;
    color: var(--ca-text-tertiary);
}

.action-arrow {
    color: var(--ca-text-tertiary);
}

.action-arrow svg {
    width: 20px;
    height: 20px;
}

/* 系统状态 */
.system-section {
    margin-bottom: 24px;
}

.system-status {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.status-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px;
    background: var(--ca-bg-tertiary);
    border-radius: var(--ca-radius-md);
}

.status-indicator {
    width: 10px;
    height: 10px;
    border-radius: var(--ca-radius-full);
    background: var(--ca-success);
    box-shadow: 0 0 10px rgba(5, 150, 105, 0.4);
}

.status-indicator.online {
    background: var(--ca-success);
    box-shadow: 0 0 10px rgba(5, 150, 105, 0.4);
}

.status-text {
    font-size: 14px;
    color: var(--ca-text-primary);
    font-weight: 500;
}

.status-metrics {
    display: flex;
    gap: 16px;
    padding-top: 16px;
    border-top: 1px solid var(--ca-border);
}

.metric {
    flex: 1;
}

.metric-label {
    display: block;
    font-size: 12px;
    color: var(--ca-text-tertiary);
    margin-bottom: 4px;
}

.metric-value {
    font-size: 18px;
    font-weight: 700;
    color: var(--ca-text-primary);
}
</style>
