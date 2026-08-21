<template>
    <div class="key-management-page">
        <!-- 主内容 -->

        <main class="main-content">
            <!-- 页面头部 -->
            <div class="page-header">
                <div class="header-glow"></div>
                <div class="header-content">
                    <div class="header-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                            <path
                                d="M21 2l-2 2m-7.61 7.61a5 5 0 1 1-2.12 2.12M21 2l1.41 1.41L13 12.83V21H5v-8.83l9.41-9.41L21 2z" />
                        </svg>
                    </div>
                    <div class="header-text">
                        <h1>密钥管理</h1>
                        <p>管理您的证书密钥对，安全存储和导出</p>
                    </div>
                </div>
            </div>

            <!-- 加载状态 -->
            <div v-if="loading" class="loading-state">
                <div class="spinner-large"></div>
                <p>正在加载密钥数据...</p>
            </div>

            <!-- 空状态 -->
            <div v-else-if="certificates.length === 0" class="empty-state">
                <div class="empty-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                        <path
                            d="M21 2l-2 2m-7.61 7.61a5 5 0 1 1-2.12 2.12M21 2l1.41 1.41L13 12.83V21H5v-8.83l9.41-9.41L21 2z" />
                    </svg>
                </div>
                <h3>暂无密钥</h3>
                <p>您还没有任何证书密钥</p>
                <button @click="$router.push('/apply')" class="btn-primary">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                        <line x1="12" y1="8" x2="12" y2="16" />
                        <line x1="8" y1="12" x2="16" y2="12" />
                    </svg>
                    申请新证书
                </button>
            </div>

            <!-- 密钥卡片列表 -->
            <div v-else class="keys-grid">
                <div v-for="(cert, index) in certificates" :key="index" class="key-card"
                    :style="{ animationDelay: `${index * 0.1}s` }">
                    <div class="card-glow"></div>
                    <div class="card-shine"></div>
                    <div class="card-content">
                        <!-- 卡片头部 -->
                        <div class="card-header">
                            <div class="key-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path
                                        d="M21 2l-2 2m-7.61 7.61a5 5 0 1 1-2.12 2.12M21 2l1.41 1.41L13 12.83V21H5v-8.83l9.41-9.41L21 2z" />
                                </svg>
                            </div>
                            <div class="cert-status" :class="cert.state === '使用中' ? 'active' : 'revoked'">
                                <span class="status-dot"></span>
                                {{ cert.state }}
                            </div>
                        </div>

                        <!-- 证书信息 -->
                        <div class="key-info">
                            <div class="info-row">
                                <span class="info-label">组织机构</span>
                                <span class="info-value">{{ cert.organization }}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">序列号</span>
                                <span class="info-value serial">{{ cert.serialNumber }}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">密钥算法</span>
                                <span class="info-value">{{ cert.keyAlgorithm || 'RSA' }} ({{ cert.keySize || 2048
                                    }}位)</span>
                            </div>
                        </div>

                        <!-- 有效期 -->
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
                        </div>

                        <!-- 操作按钮 -->
                        <div class="card-actions">
                            <button @click="downloadPrivateKey(cert)" class="btn-action download"
                                :disabled="downloading">
                                <svg v-if="!downloading" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                    stroke-width="2">
                                    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                                    <polyline points="7 10 12 15 17 10" />
                                    <line x1="12" y1="15" x2="12" y2="3" />
                                </svg>
                                <span v-else class="spinner-small"></span>
                                {{ downloading ? '获取中...' : '下载私钥' }}
                            </button>
                            <button @click="viewPublicKey(cert)" class="btn-action view">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                                    <circle cx="12" cy="12" r="3" />
                                </svg>
                                查看公钥
                            </button>
                            <button @click="copyPublicKey(cert)" class="btn-action copy">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <rect x="9" y="9" width="13" height="13" rx="2" ry="2" />
                                    <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1" />
                                </svg>
                                复制公钥
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!-- 私钥下载弹窗 -->
        <Transition name="modal">
            <div v-if="downloadDialogVisible" class="modal-overlay" @click.self="handleClose">
                <div class="modal-content warning">
                    <div class="modal-glow"></div>
                    <div class="modal-header">
                        <div class="modal-icon warning">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path
                                    d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
                                <line x1="12" y1="9" x2="12" y2="13" />
                                <line x1="12" y1="17" x2="12.01" y2="17" />
                            </svg>
                        </div>
                        <h2>私钥安全警告</h2>
                        <button class="modal-close" @click="handleClose">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <line x1="18" y1="6" x2="6" y2="18" />
                                <line x1="6" y1="6" x2="18" y2="18" />
                            </svg>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="security-notice">
                            <p class="notice-title">私钥是您的重要身份凭证，请务必：</p>
                            <ul class="notice-list">
                                <li class="notice-item success">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <polyline points="20 6 9 17 4 12" />
                                    </svg>
                                    妥善保存，不要泄露给他人
                                </li>
                                <li class="notice-item success">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <polyline points="20 6 9 17 4 12" />
                                    </svg>
                                    建议立即备份到安全位置
                                </li>
                                <li class="notice-item success">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <polyline points="20 6 9 17 4 12" />
                                    </svg>
                                    不要存储在公共电脑或云端
                                </li>
                                <li class="notice-item danger">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <line x1="18" y1="6" x2="6" y2="18" />
                                        <line x1="6" y1="6" x2="18" y2="18" />
                                    </svg>
                                    不要通过不安全的渠道传输
                                </li>
                            </ul>
                        </div>

                        <div v-if="privateKeyContent" class="private-key-display">
                            <label class="key-label">私钥内容：</label>
                            <textarea readonly v-model="privateKeyContent" class="key-textarea"></textarea>
                            <div class="key-actions">
                                <button @click="copyPrivateKey" class="btn-secondary">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <rect x="9" y="9" width="13" height="13" rx="2" ry="2" />
                                        <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1" />
                                    </svg>
                                    复制私钥
                                </button>
                                <button @click="downloadPrivateKeyFile" class="btn-primary">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                                        <polyline points="7 10 12 15 17 10" />
                                        <line x1="12" y1="15" x2="12" y2="3" />
                                    </svg>
                                    保存为文件
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </Transition>

        <!-- 公钥查看弹窗 -->
        <Transition name="modal">
            <div v-if="publicKeyDialogVisible" class="modal-overlay" @click.self="handleClose">
                <div class="modal-content">
                    <div class="modal-glow"></div>
                    <div class="modal-header">
                        <div class="modal-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path
                                    d="M9 12h6m-6 4h6m2 5H7a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5.586a1 1 0 0 1 .707.293l5.414 5.414a1 1 0 0 1 .293.707V19a2 2 0 0 1-2 2z" />
                            </svg>
                        </div>
                        <h2>公钥详情</h2>
                        <button class="modal-close" @click="handleClose">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <line x1="18" y1="6" x2="6" y2="18" />
                                <line x1="6" y1="6" x2="18" y2="18" />
                            </svg>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div v-if="currentPublicKey" class="public-key-display">
                            <label class="key-label">公钥内容：</label>
                            <textarea readonly v-model="currentPublicKey" class="key-textarea"></textarea>
                            <div class="key-actions">
                                <button @click="copyPublicKeyText" class="btn-secondary">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <rect x="9" y="9" width="13" height="13" rx="2" ry="2" />
                                        <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1" />
                                    </svg>
                                    复制公钥
                                </button>
                                <button @click="downloadPublicKeyFile" class="btn-primary">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                                        <polyline points="7 10 12 15 17 10" />
                                        <line x1="12" y1="15" x2="12" y2="3" />
                                    </svg>
                                    保存为文件
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </Transition>
    </div>
</template>

<script>
import api from '@/api';

export default {
    name: 'KeyManagement',
    data() {
        return {
            certificates: [],
            loading: false,
            downloading: false,
            downloadDialogVisible: false,
            publicKeyDialogVisible: false,
            privateKeyContent: '',
            currentPublicKey: '',
            currentCert: null
        };
    },
    computed: {
        user() {
            return this.$store.state.user;
        }
    },
    mounted() {
        this.loadCertificates();
    },
    methods: {
        getAvatarGradient(id) {
            const gradients = [
                'linear-gradient(135deg, #00d4ff 0%, #0099cc 100%)',
                'linear-gradient(135deg, #b829f7 0%, #7c1fa8 100%)',
                'linear-gradient(135deg, #f7a029 0%, #cc7a00 100%)',
                'linear-gradient(135deg, #29f7a8 0%, #1fa87c 100%)'
            ];
            return gradients[(id || 0) % gradients.length];
        },



        formatDate(dateStr) {
            if (!dateStr) return '-';
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
            return date.toLocaleDateString('zh-CN');
        },

        async loadCertificates() {
            this.loading = true;
            try {
                const response = await api.certificates.getMyCertificates();
                this.certificates = response.data || [];
            } catch (error) {
                this.$message.error('加载证书列表失败：' + error.message);
            } finally {
                this.loading = false;
            }
        },

        async downloadPrivateKey(cert) {
            this.downloading = true;
            try {
                const response = await api.certificates.downloadUserPrivateKey(cert.serialNumber);

                if (response.code === 200) {
                    this.privateKeyContent = response.data.privateKey;
                    this.currentCert = cert;
                    this.downloadDialogVisible = true;
                    this.$message.success('私钥获取成功');
                }
            } catch (error) {
                this.$message.error('下载私钥失败：' + (error.response?.data?.message || error.message));
            } finally {
                this.downloading = false;
            }
        },

        viewPublicKey(cert) {
            this.currentPublicKey = cert.userPublicKey || '该证书没有公钥信息';
            this.currentCert = cert;  // 保存当前证书信息，以便下载时使用序列号
            this.publicKeyDialogVisible = true;
        },

        copyPublicKey(cert) {
            if (!cert.userPublicKey) {
                this.$message.warning('该证书没有公钥信息');
                return;
            }
            this.copyToClipboard(cert.userPublicKey);
            this.$message.success('公钥已复制到剪贴板');
        },

        copyPrivateKey() {
            this.copyToClipboard(this.privateKeyContent);
            this.$message.success('私钥已复制到剪贴板');
        },

        copyPublicKeyText() {
            this.copyToClipboard(this.currentPublicKey);
            this.$message.success('公钥已复制到剪贴板');
        },

        downloadPrivateKeyFile() {
            if (!this.privateKeyContent) return;

            const serialNumber = this.currentCert?.serialNumber || 'unknown';
            const blob = new Blob([this.privateKeyContent], { type: 'text/plain' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `private_key_${serialNumber}.pem`;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);

            this.$message.success('私钥文件已下载');
        },

        downloadPublicKeyFile() {
            if (!this.currentPublicKey) return;

            // 从当前证书中获取序列号，如果没有则尝试从显示内容中查找
            const serialNumber = this.currentCert?.serialNumber || this.findSerialNumberFromKey() || 'unknown';
            const blob = new Blob([this.currentPublicKey], { type: 'text/plain' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `public_key_${serialNumber}.pem`;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);

            this.$message.success('公钥文件已下载');
        },

        findSerialNumberFromKey() {
            // 尝试从当前显示的公钥对应的证书中查找序列号
            const cert = this.certificates.find(c => c.userPublicKey === this.currentPublicKey);
            return cert?.serialNumber;
        },

        copyToClipboard(text) {
            if (navigator.clipboard) {
                navigator.clipboard.writeText(text);
            } else {
                const textarea = document.createElement('textarea');
                textarea.value = text;
                document.body.appendChild(textarea);
                textarea.select();
                document.execCommand('copy');
                document.body.removeChild(textarea);
            }
        },

        handleClose() {
            this.downloadDialogVisible = false;
            this.publicKeyDialogVisible = false;
            this.privateKeyContent = '';
            this.currentPublicKey = '';
        },

        handleLogout() {
            this.$store.dispatch('logout');
            this.$router.push('/login');
        }
    }
};
</script>

<style scoped>
/* 页面基础 */
.key-management-page {
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

/* 导航栏 - 与其他页面一致 */
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
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
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
    width: 42px;
    height: 42px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    font-weight: 600;
    color: #ffffff;
    flex-shrink: 0;
    position: relative;
    z-index: 1;
    cursor: pointer;
    transition: all 0.3s ease;
}

.user-avatar:hover {
    transform: scale(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
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
    padding: 40px;
    max-width: 1400px;
    margin: 0 auto;
}

/* 页面头部 */
.page-header {
    position: relative;
    margin-bottom: 40px;
    animation: headerEntrance 0.8s ease-out;
}

.header-glow {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 400px;
    height: 120px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.4), rgba(139, 92, 246, 0.4));
    filter: blur(80px);
    border-radius: 50%;
    animation: pulse 4s ease-in-out infinite;
}

@keyframes pulse {

    0%,
    100% {
        transform: translate(-50%, -50%) scale(1);
        opacity: 0.5;
    }

    50% {
        transform: translate(-50%, -50%) scale(1.2);
        opacity: 0.8;
    }
}

.header-content {
    position: relative;
    display: flex;
    align-items: center;
    gap: 20px;
}

.header-icon {
    width: 64px;
    height: 64px;
    border-radius: 20px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(139, 92, 246, 0.2));
    border: 1px solid rgba(59, 130, 246, 0.3);
    display: flex;
    align-items: center;
    justify-content: center;
    color: #3b82f6;
    position: relative;
    overflow: hidden;
    animation: iconFloat 6s ease-in-out infinite;
}

.header-icon::before {
    content: '';
    position: absolute;
    inset: -2px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.3), rgba(139, 92, 246, 0.3));
    border-radius: 20px;
    opacity: 0;
    transition: opacity 0.4s ease;
}

.header-icon:hover::before {
    opacity: 1;
}

.header-icon svg {
    width: 32px;
    height: 32px;
    position: relative;
    z-index: 1;
}

@keyframes iconFloat {

    0%,
    100% {
        transform: translateY(0);
    }

    50% {
        transform: translateY(-8px);
    }
}

.header-text h1 {
    font-size: 32px;
    font-weight: 700;
    background: linear-gradient(135deg, #0f172a, #3b82f6);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    margin-bottom: 8px;
}

.header-text p {
    font-size: 16px;
    color: #64748b;
    animation: fadeInUp 0.8s ease-out 0.2s both;
}

@keyframes headerEntrance {
    from {
        opacity: 0;
        transform: translateY(-20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 加载状态 */
.loading-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 80px 20px;
    color: #64748b;
}

.spinner-large {
    width: 48px;
    height: 48px;
    border: 3px solid #e2e8f0;
    border-top-color: #3b82f6;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 16px;
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
    padding: 80px 20px;
    color: #64748b;
}

.empty-icon {
    width: 80px;
    height: 80px;
    margin-bottom: 20px;
    border-radius: 20px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(139, 92, 246, 0.2));
    display: flex;
    align-items: center;
    justify-content: center;
    color: #3b82f6;
    animation: float 6s ease-in-out infinite;
}

.empty-icon svg {
    width: 40px;
    height: 40px;
}

.empty-state h3 {
    font-size: 20px;
    font-weight: 600;
    color: #0f172a;
    margin-bottom: 8px;
}

.empty-state p {
    font-size: 14px;
    margin-bottom: 24px;
}

.btn-primary {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 24px;
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    border: none;
    border-radius: 12px;
    font-size: 14px;
    font-weight: 600;
    color: #ffffff;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.btn-primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(59, 130, 246, 0.4);
}

.btn-primary svg {
    width: 18px;
    height: 18px;
}

/* 密钥卡片网格 */
.keys-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
    gap: 24px;
}

.key-card {
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

.key-card:hover {
    transform: translateY(-6px) scale(1.02);
    border-color: rgba(59, 130, 246, 0.4);
    box-shadow:
        0 12px 24px -1px rgba(0, 0, 0, 0.15),
        0 0 0 1px rgba(59, 130, 246, 0.1),
        0 20px 60px rgba(59, 130, 246, 0.15);
}

.card-glow {
    position: absolute;
    inset: 0;
    background: radial-gradient(circle at 50% 0%, rgba(59, 130, 246, 0.15), transparent 70%);
    opacity: 0;
    transition: opacity 0.4s ease;
    pointer-events: none;
}

.key-card:hover .card-glow {
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

.key-icon {
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

.key-icon::before {
    content: '';
    position: absolute;
    inset: -2px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.3), rgba(139, 92, 246, 0.3));
    border-radius: 12px;
    opacity: 0;
    transition: opacity 0.4s ease;
}

.key-card:hover .key-icon::before {
    opacity: 1;
}

.key-card:hover .key-icon {
    transform: scale(1.1) rotate(5deg);
}

.key-icon svg {
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
    white-space: nowrap;
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

/* 密钥信息 */
.key-info {
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
    gap: 8px;
    font-size: 13px;
    color: #0f172a;
    font-weight: 500;
}

.date-separator {
    color: #cbd5e1;
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

.btn-action.copy {
    background: rgba(16, 185, 129, 0.1);
    color: #10b981;
    border: 1px solid rgba(16, 185, 129, 0.2);
}

.btn-action.copy:hover {
    background: rgba(16, 185, 129, 0.2);
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(16, 185, 129, 0.2);
}

.btn-action:active:not(:disabled) {
    transform: translateY(0) scale(0.98);
}

.btn-action:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
}

.spinner-small {
    width: 16px;
    height: 16px;
    border: 2px solid rgba(255, 255, 255, 0.3);
    border-top-color: #ffffff;
    border-radius: 50%;
    animation: spin 0.8s linear infinite;
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
    max-width: 600px;
    overflow: hidden;
    box-shadow: 0 25px 80px rgba(0, 0, 0, 0.12);
    animation: modalEntrance 0.3s ease-out;
}

.modal-content.warning {
    border-color: rgba(245, 158, 11, 0.3);
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

.modal-content.warning .modal-glow {
    background: radial-gradient(circle, rgba(245, 158, 11, 0.2), transparent 70%);
}

@keyframes modalEntrance {
    from {
        opacity: 0;
        transform: scale(0.9) translateY(20px);
    }

    to {
        opacity: 1;
        transform: scale(1) translateY(0);
    }
}

.modal-header {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 24px 24px 0;
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
    flex-shrink: 0;
}

.modal-icon.warning {
    background: linear-gradient(135deg, rgba(245, 158, 11, 0.2), rgba(239, 68, 68, 0.2));
    color: #f59e0b;
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
    border: none;
    color: #64748b;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
}

.modal-close:hover {
    background: #e2e8f0;
    color: #0f172a;
}

.modal-close svg {
    width: 18px;
    height: 18px;
}

.modal-body {
    padding: 24px;
}

/* 安全提示 */
.security-notice {
    background: #fef3c7;
    border: 1px solid #fcd34d;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 24px;
}

.notice-title {
    font-size: 14px;
    font-weight: 600;
    color: #92400e;
    margin-bottom: 12px;
}

.notice-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.notice-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 0;
    font-size: 13px;
    color: #78350f;
}

.notice-item svg {
    width: 16px;
    height: 16px;
    flex-shrink: 0;
}

.notice-item.success svg {
    color: #16a34a;
}

.notice-item.danger svg {
    color: #dc2626;
}

/* 密钥显示 */
.private-key-display,
.public-key-display {
    margin-top: 20px;
}

.key-label {
    display: block;
    font-size: 13px;
    font-weight: 500;
    color: #64748b;
    margin-bottom: 8px;
}

.key-textarea {
    width: 100%;
    padding: 12px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
    font-family: 'JetBrains Mono', monospace;
    font-size: 12px;
    color: #0f172a;
    resize: none;
    min-height: 160px;
}

.key-actions {
    display: flex;
    gap: 12px;
    margin-top: 16px;
}

.btn-secondary {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 12px;
    background: #f1f5f9;
    border: 1px solid #e2e8f0;
    border-radius: 10px;
    font-size: 14px;
    font-weight: 500;
    color: #475569;
    cursor: pointer;
    transition: all 0.3s ease;
}

.btn-secondary:hover {
    background: #e2e8f0;
    border-color: #cbd5e1;
    transform: translateY(-1px);
}

.btn-secondary svg {
    width: 18px;
    height: 18px;
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
    transform: scale(0.9) translateY(20px);
    opacity: 0;
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

    .header-text h1 {
        font-size: 24px;
    }

    .keys-grid {
        grid-template-columns: 1fr;
    }

    .card-actions {
        flex-direction: column;
    }

    .key-actions {
        flex-direction: column;
    }
}
</style>
