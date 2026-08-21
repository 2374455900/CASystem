<template>
    <div class="signature-generation-page">
        <!-- 主内容 -->

        <main class="main-content">
            <!-- 页面头部 -->
            <div class="page-header">
                <div class="header-glow"></div>
                <div class="header-content">
                    <div class="header-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                        </svg>
                    </div>
                    <div class="header-text">
                        <h1>签名生成</h1>
                        <p>使用您的数字证书私钥对数据进行数字签名</p>
                    </div>
                </div>
            </div>

            <!-- 签名生成区域 -->
            <div class="generation-grid">
                <!-- 左侧：签名表单 -->
                <div class="generation-form">
                    <div class="form-card">
                        <div class="card-glow"></div>
                        <div class="card-shine"></div>
                        <div class="card-header">
                            <div class="form-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                                </svg>
                            </div>
                            <h2>生成数字签名</h2>
                        </div>

                        <div class="form-body">
                            <!-- 选择证书 -->
                            <div class="form-group">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                                            <polyline points="14 2 14 8 20 8" />
                                        </svg>
                                    </span>
                                    选择证书
                                </label>
                                <div class="select-wrapper">
                                    <select v-model="form.serialNumber" class="form-select" :disabled="loading">
                                        <option value="">请选择要使用的证书</option>
                                        <option v-for="cert in availableCertificates" :key="cert.serialNumber"
                                            :value="cert.serialNumber">
                                            {{ cert.organization }} - {{ cert.serialNumber }}
                                        </option>
                                    </select>
                                    <div class="select-arrow">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="6 9 12 15 18 9" />
                                        </svg>
                                    </div>
                                </div>
                                <span class="form-hint">仅显示"使用中"状态的证书</span>
                            </div>

                            <!-- 原始数据 -->
                            <div class="form-group">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                                            <polyline points="14 2 14 8 20 8" />
                                            <line x1="16" y1="13" x2="8" y2="13" />
                                            <line x1="16" y1="17" x2="8" y2="17" />
                                        </svg>
                                    </span>
                                    原始数据
                                </label>
                                <textarea v-model="form.data" :rows="6" placeholder="请输入需要签名的原始数据" class="form-textarea"
                                    :disabled="loading"></textarea>
                            </div>

                            <!-- 操作按钮 -->
                            <div class="form-actions">
                                <button @click="generateSignature" class="btn-primary"
                                    :disabled="loading || !canGenerate">
                                    <svg v-if="!loading" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                        stroke-width="2">
                                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                                    </svg>
                                    <span v-else class="spinner-small"></span>
                                    {{ loading ? '生成中...' : '生成签名' }}
                                </button>
                                <button @click="resetForm" class="btn-secondary" :disabled="loading">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <polyline points="1 4 1 10 7 10" />
                                        <path d="M3.51 15a9 9 0 1 0 2.13-9.36L1 10" />
                                    </svg>
                                    重置
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 右侧：签名结果 -->
                <div class="generation-result">
                    <div v-if="!signatureResult" class="result-empty">
                        <div class="empty-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                                <circle cx="12" cy="12" r="10" />
                                <line x1="12" y1="8" x2="12" y2="12" />
                                <line x1="12" y1="16" x2="12.01" y2="16" />
                            </svg>
                        </div>
                        <h3>暂无签名结果</h3>
                        <p>请选择证书并输入数据，然后点击生成签名按钮</p>
                    </div>

                    <div v-else class="result-card">
                        <div class="card-glow"></div>
                        <div class="result-header">
                            <div class="result-icon success">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                                    <polyline points="22 4 12 14.01 9 11.01" />
                                </svg>
                            </div>
                            <div class="result-title">
                                <h2>签名生成成功</h2>
                                <p>您的数据已成功签名，可使用下方签名进行验证</p>
                            </div>
                        </div>

                        <div class="result-body">
                            <!-- 签名数据 -->
                            <div class="result-section">
                                <h3 class="section-title">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                                        <line x1="3" y1="9" x2="21" y2="9" />
                                        <line x1="9" y1="21" x2="9" y2="9" />
                                    </svg>
                                    Base64 签名数据
                                </h3>
                                <div class="signature-display">
                                    <textarea readonly v-model="signatureResult.signature" class="signature-textarea"
                                        rows="4"></textarea>
                                    <div class="signature-actions">
                                        <button @click="copySignature" class="btn-copy">
                                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                                <rect x="9" y="9" width="13" height="13" rx="2" ry="2" />
                                                <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1" />
                                            </svg>
                                            复制签名
                                        </button>
                                        <button @click="downloadSignature" class="btn-download">
                                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                                                <polyline points="7 10 12 15 17 10" />
                                                <line x1="12" y1="15" x2="12" y2="3" />
                                            </svg>
                                            下载签名
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <!-- 签名信息 -->
                            <div class="result-section">
                                <h3 class="section-title">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                                        <polyline points="14 2 14 8 20 8" />
                                    </svg>
                                    签名信息
                                </h3>
                                <div class="info-grid">
                                    <div class="info-item">
                                        <span class="info-label">证书主题</span>
                                        <span class="info-value">{{ signatureResult.subject || '未知' }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">证书序列号</span>
                                        <span class="info-value serial">{{ signatureResult.serialNumber || '未知'
                                            }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">签名算法</span>
                                        <span class="info-value">{{ signatureResult.algorithm || 'SHA256withRSA'
                                            }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">数据哈希 (SHA-256)</span>
                                        <span class="info-value hash">{{ signatureResult.dataHash || '未知' }}</span>
                                    </div>
                                </div>
                            </div>

                            <!-- 原始数据预览 -->
                            <div class="result-section">
                                <h3 class="section-title">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                                        <polyline points="14 2 14 8 20 8" />
                                        <line x1="16" y1="13" x2="8" y2="13" />
                                        <line x1="16" y1="17" x2="8" y2="17" />
                                    </svg>
                                    原始数据预览
                                </h3>
                                <textarea readonly v-model="signatureResult.data" class="data-preview-textarea"
                                    rows="3"></textarea>
                            </div>

                            <!-- 验证提示 -->
                            <div class="verify-hint">
                                <div class="hint-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <circle cx="12" cy="12" r="10" />
                                        <line x1="12" y1="16" x2="12" y2="12" />
                                        <line x1="12" y1="8" x2="12.01" y2="8" />
                                    </svg>
                                </div>
                                <div class="hint-content">
                                    <p>其他用户可以使用您的证书文件和上述签名数据在<a @click="$router.push('/verify')">签名验证</a>页面验证此签名的真实性。
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 使用说明 -->
            <div class="help-section">
                <div class="help-card">
                    <div class="card-glow"></div>
                    <div class="help-header">
                        <div class="help-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10" />
                                <line x1="12" y1="16" x2="12" y2="12" />
                                <line x1="12" y1="8" x2="12.01" y2="8" />
                            </svg>
                        </div>
                        <h2>使用说明</h2>
                    </div>
                    <div class="help-body">
                        <div class="help-grid">
                            <div class="help-col">
                                <h3>签名流程</h3>
                                <ol class="step-list">
                                    <li>
                                        <div class="step-number">1</div>
                                        <div class="step-content">
                                            <strong>选择证书</strong>
                                            <span>从下拉列表中选择您的有效证书</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="step-number">2</div>
                                        <div class="step-content">
                                            <strong>输入数据</strong>
                                            <span>输入需要签名的原始数据内容</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="step-number">3</div>
                                        <div class="step-content">
                                            <strong>生成签名</strong>
                                            <span>点击生成签名按钮，系统将使用您的私钥签名</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="step-number">4</div>
                                        <div class="step-content">
                                            <strong>分享签名</strong>
                                            <span>复制签名数据给验证方，同时提供证书文件</span>
                                        </div>
                                    </li>
                                </ol>
                            </div>
                            <!-- <div class="help-col">
                                <h3>应用场景</h3>
                                <ul class="feature-list">
                                    <li>
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="20 6 9 17 4 12" />
                                        </svg>
                                        电子合同签署，确保合同不可否认
                                    </li>
                                    <li>
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="20 6 9 17 4 12" />
                                        </svg>
                                        软件代码签名，证明代码来源
                                    </li>
                                    <li>
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="20 6 9 17 4 12" />
                                        </svg>
                                        重要文档认证，防止数据篡改
                                    </li>
                                    <li>
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="20 6 9 17 4 12" />
                                        </svg>
                                        身份验证，证明消息发送者身份
                                    </li>
                                </ul>
                            </div> -->
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</template>

<script>
import api from '@/api';

export default {
    name: 'SignatureGeneration',
    data() {
        return {
            form: {
                data: '',
                serialNumber: ''
            },
            loading: false,
            certificates: [],
            signatureResult: null
        };
    },
    computed: {
        user() {
            return this.$store.state.user;
        },
        availableCertificates() {
            return this.certificates.filter(cert => cert.state === '使用中');
        },
        canGenerate() {
            return this.form.serialNumber && this.form.data.trim();
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



        async loadCertificates() {
            try {
                const response = await api.certificates.getMyCertificates();
                if (response.code === 200) {
                    this.certificates = response.data || [];
                }
            } catch (error) {
                this.$message.error('加载证书列表失败：' + (error.message || '未知错误'));
            }
        },

        async generateSignature() {
            if (!this.form.serialNumber) {
                this.$message.warning('请选择证书');
                return;
            }
            if (!this.form.data.trim()) {
                this.$message.warning('请输入原始数据');
                return;
            }

            this.loading = true;
            this.signatureResult = null;

            try {
                const response = await api.certificates.generateSignature({
                    data: this.form.data,
                    serialNumber: this.form.serialNumber
                });

                if (response.code === 200) {
                    this.signatureResult = {
                        success: response.data.success,
                        signature: response.data.signature,
                        data: response.data.data,
                        dataHash: response.data.dataHash,
                        serialNumber: response.data.serialNumber,
                        subject: response.data.subject,
                        algorithm: response.data.algorithm
                    };
                    this.$message.success('签名生成成功！');
                } else {
                    this.$message.error(response.message || '签名生成失败');
                }
            } catch (error) {
                this.$message.error('签名生成失败：' + (error.message || error.response?.data?.message || '未知错误'));
            } finally {
                this.loading = false;
            }
        },

        resetForm() {
            this.form = {
                data: '',
                serialNumber: ''
            };
            this.signatureResult = null;
        },

        copySignature() {
            if (!this.signatureResult?.signature) return;

            if (navigator.clipboard) {
                navigator.clipboard.writeText(this.signatureResult.signature);
            } else {
                const textarea = document.createElement('textarea');
                textarea.value = this.signatureResult.signature;
                document.body.appendChild(textarea);
                textarea.select();
                document.execCommand('copy');
                document.body.removeChild(textarea);
            }
            this.$message.success('签名已复制到剪贴板');
        },

        downloadSignature() {
            if (!this.signatureResult?.signature) return;

            const content = `签名数据 (Base64):
${this.signatureResult.signature}

证书序列号: ${this.signatureResult.serialNumber}
证书主题: ${this.signatureResult.subject}
签名算法: ${this.signatureResult.algorithm}
数据哈希: ${this.signatureResult.dataHash}

原始数据:
${this.signatureResult.data}
`;

            const blob = new Blob([content], { type: 'text/plain' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `signature_${this.signatureResult.serialNumber}_${Date.now()}.txt`;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);

            this.$message.success('签名文件已下载');
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
.signature-generation-page {
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
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
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

/* 生成网格布局 */
.generation-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 24px;
    margin-bottom: 24px;
}

/* 表单卡片 */
.form-card,
.result-card {
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

.form-card:hover,
.result-card:hover {
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

.form-card:hover .card-glow,
.result-card:hover .card-glow {
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

.card-header {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 24px 24px 0;
}

.form-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(139, 92, 246, 0.2));
    display: flex;
    align-items: center;
    justify-content: center;
    color: #3b82f6;
}

.form-icon svg {
    width: 24px;
    height: 24px;
}

.card-header h2 {
    font-size: 20px;
    font-weight: 600;
    color: #0f172a;
}

.form-body {
    padding: 24px;
}

/* 表单组 */
.form-group {
    margin-bottom: 20px;
}

.form-label {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 500;
    color: #334155;
    margin-bottom: 10px;
}

.label-icon {
    width: 18px;
    height: 18px;
    color: #3b82f6;
}

.label-icon svg {
    width: 100%;
    height: 100%;
}

.form-hint {
    display: block;
    margin-top: 6px;
    font-size: 12px;
    color: #94a3b8;
}

/* 下拉选择框 */
.select-wrapper {
    position: relative;
}

.form-select {
    width: 100%;
    padding: 14px 40px 14px 16px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    font-size: 14px;
    color: #0f172a;
    appearance: none;
    cursor: pointer;
    transition: all 0.3s ease;
}

.form-select:focus {
    outline: none;
    border-color: #3b82f6;
    background: #ffffff;
    box-shadow:
        0 0 0 3px rgba(59, 130, 246, 0.1),
        0 4px 12px rgba(59, 130, 246, 0.15);
}

.form-select:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

.select-arrow {
    position: absolute;
    right: 16px;
    top: 50%;
    transform: translateY(-50%);
    width: 16px;
    height: 16px;
    color: #64748b;
    pointer-events: none;
}

.select-arrow svg {
    width: 100%;
    height: 100%;
}

/* 文本域 */
.form-textarea {
    width: 100%;
    padding: 14px 16px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    font-size: 14px;
    color: #0f172a;
    resize: vertical;
    font-family: 'JetBrains Mono', monospace;
    transition: all 0.3s ease;
}

.form-textarea:focus {
    outline: none;
    border-color: #3b82f6;
    background: #ffffff;
    box-shadow:
        0 0 0 3px rgba(59, 130, 246, 0.1),
        0 4px 12px rgba(59, 130, 246, 0.15);
}

.form-textarea::placeholder {
    color: #94a3b8;
}

.form-textarea:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

/* 表单按钮 */
.form-actions {
    display: flex;
    gap: 12px;
    margin-top: 24px;
}

.btn-primary {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 14px 24px;
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    border: none;
    border-radius: 12px;
    font-size: 15px;
    font-weight: 600;
    color: #ffffff;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.btn-primary:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(59, 130, 246, 0.4);
}

.btn-primary:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
}

.btn-primary svg {
    width: 20px;
    height: 20px;
}

.btn-secondary {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 14px 24px;
    background: #f1f5f9;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    font-size: 15px;
    font-weight: 600;
    color: #475569;
    cursor: pointer;
    transition: all 0.3s ease;
}

.btn-secondary:hover:not(:disabled) {
    background: #e2e8f0;
    border-color: #cbd5e1;
    transform: translateY(-1px);
}

.btn-secondary:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

.btn-secondary svg {
    width: 20px;
    height: 20px;
}

.spinner-small {
    width: 20px;
    height: 20px;
    border: 2px solid rgba(255, 255, 255, 0.3);
    border-top-color: #ffffff;
    border-radius: 50%;
    animation: spin 0.8s linear infinite;
}

@keyframes spin {
    to {
        transform: rotate(360deg);
    }
}

/* 空结果 */
.result-empty {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 80px 20px;
    color: #64748b;
    height: 100%;
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 20px;
    box-shadow:
        0 4px 6px -1px rgba(0, 0, 0, 0.1),
        0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.result-empty .empty-icon {
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

.result-empty .empty-icon svg {
    width: 40px;
    height: 40px;
}

.result-empty h3 {
    font-size: 20px;
    font-weight: 600;
    color: #0f172a;
    margin-bottom: 8px;
}

.result-empty p {
    font-size: 14px;
}

/* 结果卡片头部 */
.result-header {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 24px 24px 0;
}

.result-icon {
    width: 56px;
    height: 56px;
    border-radius: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}

.result-icon.success {
    background: linear-gradient(135deg, rgba(16, 185, 129, 0.2), rgba(5, 150, 105, 0.2));
    color: #10b981;
}

.result-icon.error {
    background: linear-gradient(135deg, rgba(239, 68, 68, 0.2), rgba(220, 38, 38, 0.2));
    color: #ef4444;
}

.result-icon svg {
    width: 28px;
    height: 28px;
}

.result-title {
    flex: 1;
}

.result-title h2 {
    font-size: 20px;
    font-weight: 600;
    color: #0f172a;
    margin-bottom: 4px;
}

.result-title p {
    font-size: 13px;
    color: #64748b;
}

.result-body {
    padding: 24px;
}

.result-section {
    margin-bottom: 24px;
}

.result-section:last-child {
    margin-bottom: 0;
}

.section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 600;
    color: #475569;
    margin-bottom: 16px;
}

.section-title svg {
    width: 18px;
    height: 18px;
}

/* 签名显示 */
.signature-display {
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    overflow: hidden;
}

.signature-textarea {
    width: 100%;
    padding: 12px;
    background: transparent;
    border: none;
    font-family: 'JetBrains Mono', monospace;
    font-size: 12px;
    color: #0f172a;
    resize: vertical;
    min-height: 100px;
}

.signature-textarea:focus {
    outline: none;
}

.signature-actions {
    display: flex;
    gap: 8px;
    padding: 12px;
    border-top: 1px solid #e2e8f0;
    background: #ffffff;
}

.btn-copy,
.btn-download {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 6px;
    padding: 10px 16px;
    border-radius: 8px;
    font-size: 13px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
    border: none;
}

.btn-copy {
    background: rgba(59, 130, 246, 0.1);
    color: #3b82f6;
}

.btn-copy:hover {
    background: rgba(59, 130, 246, 0.2);
}

.btn-download {
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    color: #ffffff;
}

.btn-download:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.btn-copy svg,
.btn-download svg {
    width: 16px;
    height: 16px;
}

/* 信息网格 */
.info-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 12px;
}

.info-item {
    display: flex;
    flex-direction: column;
    gap: 6px;
    padding: 12px;
    background: #f8fafc;
    border-radius: 10px;
    border: 1px solid #e2e8f0;
}

.info-label {
    font-size: 12px;
    color: #64748b;
    font-weight: 500;
}

.info-value {
    font-size: 14px;
    color: #0f172a;
    font-weight: 500;
    word-break: break-all;
}

.info-value.serial {
    font-family: 'JetBrains Mono', monospace;
    font-size: 13px;
    color: #3b82f6;
}

.info-value.hash {
    font-family: 'JetBrains Mono', monospace;
    font-size: 11px;
    color: #64748b;
}

/* 数据预览 */
.data-preview-textarea {
    width: 100%;
    padding: 12px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
    font-family: 'JetBrains Mono', monospace;
    font-size: 12px;
    color: #0f172a;
    resize: vertical;
    min-height: 80px;
}

/* 验证提示 */
.verify-hint {
    display: flex;
    gap: 12px;
    padding: 16px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.1), rgba(139, 92, 246, 0.1));
    border: 1px solid rgba(59, 130, 246, 0.2);
    border-radius: 12px;
    margin-top: 20px;
}

.hint-icon {
    width: 24px;
    height: 24px;
    color: #3b82f6;
    flex-shrink: 0;
}

.hint-icon svg {
    width: 100%;
    height: 100%;
}

.hint-content {
    flex: 1;
}

.hint-content p {
    font-size: 13px;
    color: #475569;
    line-height: 1.5;
}

.hint-content a {
    color: #3b82f6;
    font-weight: 600;
    cursor: pointer;
    text-decoration: none;
}

.hint-content a:hover {
    text-decoration: underline;
}

/* 帮助区域 */
.help-section {
    margin-top: 24px;
}

.help-card {
    position: relative;
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 20px;
    overflow: hidden;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow:
        0 4px 6px -1px rgba(0, 0, 0, 0.1),
        0 2px 4px -1px rgba(0, 0, 0, 0.06);
    animation: cardEntrance 0.6s ease-out 0.2s both;
}

.help-card:hover {
    transform: translateY(-4px);
    box-shadow:
        0 12px 24px -1px rgba(0, 0, 0, 0.15),
        0 0 0 1px rgba(59, 130, 246, 0.1),
        0 20px 60px rgba(59, 130, 246, 0.15);
}

.help-header {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 24px 24px 0;
}

.help-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(139, 92, 246, 0.2));
    display: flex;
    align-items: center;
    justify-content: center;
    color: #3b82f6;
}

.help-icon svg {
    width: 24px;
    height: 24px;
}

.help-header h2 {
    font-size: 20px;
    font-weight: 600;
    color: #0f172a;
}

.help-body {
    padding: 24px;
}

.help-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 32px;
}

.help-col h3 {
    font-size: 16px;
    font-weight: 600;
    color: #0f172a;
    margin-bottom: 16px;
}

.step-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.step-list li {
    display: flex;
    align-items: flex-start;
    gap: 12px;
    margin-bottom: 16px;
}

.step-number {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    color: #ffffff;
    font-size: 13px;
    font-weight: 700;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}

.step-content {
    display: flex;
    flex-direction: column;
    gap: 2px;
}

.step-content strong {
    font-size: 14px;
    color: #0f172a;
}

.step-content span {
    font-size: 13px;
    color: #64748b;
}

.feature-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.feature-list li {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 10px 0;
    font-size: 14px;
    color: #475569;
}

.feature-list li svg {
    width: 18px;
    height: 18px;
    color: #10b981;
    flex-shrink: 0;
}

/* 响应式 */
@media (max-width: 1024px) {
    .generation-grid {
        grid-template-columns: 1fr;
    }

    .help-grid {
        grid-template-columns: 1fr;
    }

    .info-grid {
        grid-template-columns: 1fr;
    }
}

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

    .form-actions {
        flex-direction: column;
    }

    .signature-actions {
        flex-direction: column;
    }
}
</style>
