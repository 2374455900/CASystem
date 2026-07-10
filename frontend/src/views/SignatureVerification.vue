<template>
    <div class="signature-verification-page">
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
                <span class="brand-text">CA 证书管理系统</span>
            </div>

            <div class="nav-links">
                <router-link to="/home" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
                        <polyline points="9 22 9 12 15 12 15 22" />
                    </svg>
                    <span>首页</span>
                </router-link>
                <router-link to="/apply" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                        <line x1="12" y1="8" x2="12" y2="16" />
                        <line x1="8" y1="12" x2="16" y2="12" />
                    </svg>
                    <span>申请证书</span>
                </router-link>
                <router-link to="/certificates" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                        <polyline points="14 2 14 8 20 8" />
                        <line x1="16" y1="13" x2="8" y2="13" />
                        <line x1="16" y1="17" x2="8" y2="17" />
                        <polyline points="10 9 9 9 8 9" />
                    </svg>
                    <span>我的证书</span>
                </router-link>
                <router-link to="/revoke" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="12" cy="12" r="10" />
                        <line x1="15" y1="9" x2="9" y2="15" />
                        <line x1="9" y1="9" x2="15" y2="15" />
                    </svg>
                    <span>撤销证书</span>
                </router-link>
                <router-link to="/keys" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path
                            d="M21 2l-2 2m-7.61 7.61a5 5 0 1 1-2.12 2.12M21 2l1.41 1.41L13 12.83V21H5v-8.83l9.41-9.41L21 2z" />
                    </svg>
                    <span>密钥管理</span>
                </router-link>
                <router-link to="/generate-signature" class="nav-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                    </svg>
                    <span>签名生成</span>
                </router-link>
                <router-link to="/verify" class="nav-link active">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                        <polyline points="22 4 12 14.01 9 11.01" />
                    </svg>
                    <span>签名验证</span>
                </router-link>
            </div>

            <div class="nav-user">
                <div class="user-avatar" :style="{ background: getAvatarGradient(user?.id) }"
                    @click="$router.push('/profile')" title="个人中心">
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
                            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                            <polyline points="22 4 12 14.01 9 11.01" />
                        </svg>
                    </div>
                    <div class="header-text">
                        <h1>签名验证</h1>
                        <p>验证数字签名的真实性和数据完整性</p>
                    </div>
                </div>
            </div>

            <!-- 验证表单区域 -->
            <div class="verification-grid">
                <!-- 左侧：验证表单 -->
                <div class="verification-form">
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
                            <h2>验证签名</h2>
                        </div>

                        <div class="form-body">
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
                                <textarea v-model="form.data" :rows="6" placeholder="请输入要验证的原始数据"
                                    class="form-textarea"></textarea>
                            </div>

                            <!-- 签名数据 -->
                            <div class="form-group">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                                            <line x1="3" y1="9" x2="21" y2="9" />
                                            <line x1="9" y1="21" x2="9" y2="9" />
                                        </svg>
                                    </span>
                                    签名数据
                                </label>
                                <textarea v-model="form.signature" :rows="4" placeholder="请输入 Base64 编码的签名数据"
                                    class="form-textarea"></textarea>
                            </div>

                            <!-- 证书文件上传 -->
                            <div class="form-group">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                                            <polyline points="14 2 14 8 20 8" />
                                        </svg>
                                    </span>
                                    证书文件
                                </label>
                                <div class="file-upload-wrapper">
                                    <input type="file" ref="fileInput" @change="handleFileChange"
                                        accept=".pem,.crt,.cer" class="file-input" />
                                    <div class="file-upload-zone" :class="{ 'has-file': form.certFile }"
                                        @click="$refs.fileInput.click()">
                                        <div class="upload-icon">
                                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
                                                <polyline points="17 8 12 3 7 8" />
                                                <line x1="12" y1="3" x2="12" y2="15" />
                                            </svg>
                                        </div>
                                        <div class="upload-text">
                                            <p v-if="!form.certFile">点击选择或拖拽文件到此处</p>
                                            <p v-else class="file-name">{{ form.certFile.name }}</p>
                                            <span class="upload-hint">支持 PEM/CRT/CER 格式</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- 操作按钮 -->
                            <div class="form-actions">
                                <button @click="verifySignature" class="btn-primary" :disabled="verifying">
                                    <svg v-if="!verifying" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                        stroke-width="2">
                                        <circle cx="11" cy="11" r="8" />
                                        <path d="M21 21l-4.35-4.35" />
                                        <path d="M11 8v4h4" />
                                    </svg>
                                    <span v-else class="spinner-small"></span>
                                    {{ verifying ? '验证中...' : '开始验证' }}
                                </button>
                                <button @click="resetForm" class="btn-secondary">
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

                <!-- 右侧：验证结果 -->
                <div class="verification-result">
                    <div v-if="!verificationResult" class="result-empty">
                        <div class="empty-icon">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                                <circle cx="12" cy="12" r="10" />
                                <line x1="12" y1="8" x2="12" y2="12" />
                                <line x1="12" y1="16" x2="12.01" y2="16" />
                            </svg>
                        </div>
                        <h3>暂无验证结果</h3>
                        <p>请先填写数据并点击验证按钮</p>
                    </div>

                    <div v-else class="result-card">
                        <div class="card-glow"></div>
                        <div class="result-header">
                            <div class="result-icon" :class="verificationResult.isValid ? 'success' : 'error'">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <template v-if="verificationResult.isValid">
                                        <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                                        <path d="M22 4L12 14.01 9 11.01" />
                                    </template>
                                    <template v-else>
                                        <circle cx="12" cy="12" r="10" />
                                        <line x1="15" y1="9" x2="9" y2="15" />
                                        <line x1="9" y1="9" x2="15" y2="15" />
                                    </template>
                                </svg>
                            </div>
                            <div class="result-title">
                                <h2>{{ verificationResult.isValid ? '验证成功' : '验证失败' }}</h2>
                                <p>{{ verificationResult.isValid ? '签名验证通过，数据完整且来源可信' : '签名验证失败，数据可能被篡改或证书不匹配' }}</p>
                            </div>
                        </div>

                        <div class="result-body">
                            <div class="result-section">
                                <h3 class="section-title">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                                        <polyline points="14 2 14 8 20 8" />
                                    </svg>
                                    证书信息
                                </h3>
                                <div class="info-grid">
                                    <div class="info-item">
                                        <span class="info-label">证书主题</span>
                                        <span class="info-value">{{ verificationResult.subject || '未知' }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">证书颁发者</span>
                                        <span class="info-value">{{ verificationResult.issuer || '未知' }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">证书序列号</span>
                                        <span class="info-value serial">{{ verificationResult.serialNumber || '未知'
                                            }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">有效期</span>
                                        <span class="info-value">{{ formatDate(verificationResult.validFrom) }} 至 {{
                                            formatDate(verificationResult.validTo) }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">签名算法</span>
                                        <span class="info-value">{{ verificationResult.signatureAlgorithm || '未知'
                                            }}</span>
                                    </div>
                                </div>
                            </div>

                            <div class="result-section">
                                <h3 class="section-title">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                                        <circle cx="12" cy="12" r="3" />
                                    </svg>
                                    原始数据预览
                                </h3>
                                <textarea readonly v-model="verificationResult.data"
                                    class="data-preview-textarea"></textarea>
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
                                <h3>验证流程</h3>
                                <ol class="step-list">
                                    <li>
                                        <div class="step-number">1</div>
                                        <div class="step-content">
                                            <strong>准备数据</strong>
                                            <span>输入需要验证的原始数据</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="step-number">2</div>
                                        <div class="step-content">
                                            <strong>提供签名</strong>
                                            <span>输入对应的 Base64 编码签名数据</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="step-number">3</div>
                                        <div class="step-content">
                                            <strong>上传证书</strong>
                                            <span>上传包含公钥的证书文件</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="step-number">4</div>
                                        <div class="step-content">
                                            <strong>执行验证</strong>
                                            <span>点击"开始验证"按钮</span>
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
                                        验证合同、文档的数字签名
                                    </li>
                                    <li>
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="20 6 9 17 4 12" />
                                        </svg>
                                        确认数据传输的完整性
                                    </li>
                                    <li>
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="20 6 9 17 4 12" />
                                        </svg>
                                        验证软件、代码的签名认证
                                    </li>
                                    <li>
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="20 6 9 17 4 12" />
                                        </svg>
                                        确认邮件、消息的来源真实性
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
    name: 'SignatureVerification',
    data() {
        return {
            form: {
                data: '',
                signature: '',
                certFile: null
            },
            verifying: false,
            verificationResult: null
        };
    },
    computed: {
        user() {
            return this.$store.state.user;
        }
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

        getParticleStyle(index) {
            const positions = [
                { top: `${Math.random() * 100}%`, left: `${Math.random() * 100}%` },
            ];
            const pos = positions[Math.floor(Math.random() * positions.length)];
            const delay = Math.random() * 20;
            const duration = 15 + Math.random() * 10;
            const size = 2 + Math.random() * 4;
            return {
                ...pos,
                width: `${size}px`,
                height: `${size}px`,
                animationDelay: `${delay}s`,
                animationDuration: `${duration}s`
            };
        },

        formatDate(dateStr) {
            if (!dateStr) return '-';
            try {
                const date = new Date(dateStr);
                return date.toLocaleDateString('zh-CN');
            } catch (e) {
                return dateStr;
            }
        },

        handleFileChange(event) {
            const file = event.target.files[0];
            if (file) {
                this.form.certFile = file;
                if (this.$message) {
                    this.$message.success(`已选择文件：${file.name}`);
                }
            }
        },

        async verifySignature() {
            if (!this.form.data) {
                if (this.$message) {
                    this.$message.warning('请输入原始数据');
                } else {
                    alert('请输入原始数据');
                }
                return;
            }
            if (!this.form.signature) {
                if (this.$message) {
                    this.$message.warning('请输入签名数据');
                } else {
                    alert('请输入签名数据');
                }
                return;
            }
            if (!this.form.certFile) {
                if (this.$message) {
                    this.$message.warning('请上传证书文件');
                } else {
                    alert('请上传证书文件');
                }
                return;
            }

            this.verifying = true;
            this.verificationResult = null;

            // 清理签名数据（移除换行符和空格）
            const cleanSignature = this.form.signature.replace(/\s/g, '');
            
            console.log('验证签名 - 原始数据:', this.form.data);
            console.log('验证签名 - 签名数据(清理后):', cleanSignature.substring(0, 50) + '...');
            console.log('验证签名 - 证书文件:', this.form.certFile.name);

            try {
                const formData = new FormData();
                formData.append('data', this.form.data);
                formData.append('signature', cleanSignature);
                formData.append('certificateFile', this.form.certFile);

                const response = await api.certificates.verifySignature(formData);
                console.log('验证签名 - 响应:', response);

                if (response.code === 200) {
                    // 后端返回的字段名是 'valid'，不是 'isValid'
                    const isValid = response.data.valid === true || response.data.isValid === true;
                    
                    console.log('验证签名 - 后端返回数据:', response.data);
                    
                    this.verificationResult = {
                        isValid: isValid,
                        subject: response.data.certificateSubject || response.data.subject,
                        issuer: response.data.certificateIssuer || response.data.issuer,
                        serialNumber: response.data.certificateSerialNumber || response.data.serialNumber,
                        validFrom: response.data.validFrom,
                        validTo: response.data.validTo,
                        signatureAlgorithm: response.data.signatureAlgorithm,
                        data: this.form.data
                    };
                    
                    console.log('验证签名 - verificationResult:', this.verificationResult);

                    if (this.verificationResult.isValid) {
                        if (this.$message) {
                            this.$message.success('签名验证通过！');
                        } else {
                            alert('签名验证通过！');
                        }
                    } else {
                        if (this.$message) {
                            this.$message.error('签名验证失败！');
                        } else {
                            alert('签名验证失败！');
                        }
                    }
                } else {
                    // 处理非200响应
                    const errorMsg = response.message || '签名验证失败';
                    if (this.$message) {
                        this.$message.error('验证失败：' + errorMsg);
                    } else {
                        alert('验证失败：' + errorMsg);
                    }
                    this.verificationResult = {
                        isValid: false,
                        error: errorMsg
                    };
                }
            } catch (error) {
                console.error('验证签名错误:', error);
                let errorMsg = '未知错误';
                if (error.response && error.response.data) {
                    errorMsg = error.response.data.message || error.response.data;
                } else if (error.message) {
                    errorMsg = error.message;
                }
                if (this.$message) {
                    this.$message.error('验证失败：' + errorMsg);
                } else {
                    alert('验证失败：' + errorMsg);
                }
                this.verificationResult = {
                    isValid: false,
                    error: errorMsg
                };
            } finally {
                this.verifying = false;
            }
        },

        resetForm() {
            this.form = {
                data: '',
                signature: '',
                certFile: null
            };
            this.verificationResult = null;
            if (this.$refs.fileInput) {
                this.$refs.fileInput.value = '';
            }
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
.signature-verification-page {
    min-height: 100vh;
    background: #f8fafc;
    position: relative;
    overflow-x: hidden;
}

/* 动态背景 - 与其他页面一致 */
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

/* 验证网格布局 */
.verification-grid {
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

.card-body {
    padding: 24px;
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

/* 文件上传 */
.file-upload-wrapper {
    position: relative;
}

.file-input {
    position: absolute;
    width: 0;
    height: 0;
    opacity: 0;
}

.file-upload-zone {
    position: relative;
    padding: 40px;
    border: 2px dashed #cbd5e1;
    border-radius: 16px;
    background: #f8fafc;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    display: flex;
    align-items: center;
    gap: 16px;
}

.file-upload-zone::before {
    content: '';
    position: absolute;
    inset: 0;
    background: radial-gradient(circle at center, rgba(59, 130, 246, 0.08), transparent 70%);
    opacity: 0;
    transition: opacity 0.4s ease;
}

.file-upload-zone:hover {
    border-color: #3b82f6;
    background: rgba(59, 130, 246, 0.05);
    transform: scale(1.02);
    box-shadow: 0 8px 24px rgba(59, 130, 246, 0.15);
}

.file-upload-zone:hover::before {
    opacity: 1;
}

.file-upload-zone.has-file {
    border-color: #10b981;
    background: rgba(16, 185, 129, 0.05);
    box-shadow: 0 4px 16px rgba(16, 185, 129, 0.1);
}

.upload-icon {
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

.file-upload-zone.has-file .upload-icon {
    background: linear-gradient(135deg, rgba(16, 185, 129, 0.2), rgba(5, 150, 105, 0.2));
    color: #10b981;
}

.upload-icon svg {
    width: 24px;
    height: 24px;
}

.upload-text {
    flex: 1;
}

.upload-text p {
    font-size: 14px;
    color: #475569;
    margin-bottom: 4px;
}

.file-name {
    font-weight: 600;
    color: #10b981;
}

.upload-hint {
    font-size: 12px;
    color: #94a3b8;
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

.btn-secondary:hover {
    background: #e2e8f0;
    border-color: #cbd5e1;
    transform: translateY(-1px);
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
}

.info-value.serial {
    font-family: 'JetBrains Mono', monospace;
    font-size: 13px;
    color: #3b82f6;
}

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
    min-height: 100px;
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
    .verification-grid {
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
}
</style>
