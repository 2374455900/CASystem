<template>
    <div class="apply-page">
        <!-- 主内容 -->

        <main class="main-content">
            <div class="page-header">
                <div class="header-glow"></div>
                <div class="header-content">
                    <div class="header-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                            <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                            <line x1="12" y1="8" x2="12" y2="16" />
                            <line x1="8" y1="12" x2="16" y2="12" />
                        </svg>
                    </div>
                    <div class="header-text">
                        <h1>申请数字证书</h1>
                        <p>填写以下信息，我们将为您生成安全可靠的数字证书</p>
                    </div>
                </div>
            </div>

            <div class="form-wrapper">
                <div class="form-card">
                    <div class="card-shine"></div>
                    <div class="card-glow"></div>
                    <form @submit.prevent="handleSubmit" class="apply-form">
                        <!-- 表单网格 -->
                        <div class="form-grid">
                            <!-- 组织机构名称 -->
                            <div class="form-group"
                                :class="{ 'has-error': validationErrors.organization && formTouched }">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
                                            <polyline points="9 22 9 12 15 12 15 22" />
                                        </svg>
                                    </span>
                                    组织机构名称
                                    <span class="required">*</span>
                                </label>
                                <div class="input-wrapper">
                                    <input v-model="form.organization" @blur="validateField('organization')" type="text"
                                        placeholder="请输入组织机构全称" required class="form-input" />
                                    <div class="input-glow"></div>
                                </div>
                                <div v-if="validationErrors.organization && validationErrors.organization.length > 0 && formTouched" class="field-error">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <circle cx="12" cy="12" r="10" />
                                        <line x1="12" y1="8" x2="12" y2="12" />
                                        <line x1="12" y1="16" x2="12.01" y2="16" />
                                    </svg>
                                    <span>{{ validationErrors.organization[0] }}</span>
                                </div>
                            </div>

                            <!-- 工商注册号 -->
                            <div class="form-group"
                                :class="{ 'has-error': validationErrors.registrationNumber && formTouched }">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <rect x="2" y="3" width="20" height="14" rx="2" ry="2" />
                                            <line x1="8" y1="21" x2="16" y2="21" />
                                            <line x1="12" y1="17" x2="12" y2="21" />
                                        </svg>
                                    </span>
                                    工商注册号
                                    <span class="required">*</span>
                                </label>
                                <div class="input-wrapper">
                                    <input v-model="form.registrationNumber" @blur="validateField('registrationNumber')"
                                        type="text" placeholder="请输入统一社会信用代码" required class="form-input" />
                                    <div class="input-glow"></div>
                                </div>
                                <div v-if="validationErrors.registrationNumber && validationErrors.registrationNumber.length > 0 && formTouched" class="field-error">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <circle cx="12" cy="12" r="10" />
                                        <line x1="12" y1="8" x2="12" y2="12" />
                                        <line x1="12" y1="16" x2="12.01" y2="16" />
                                    </svg>
                                    <span>{{ validationErrors.registrationNumber[0] }}</span>
                                </div>
                            </div>

                            <!-- 法人姓名 -->
                            <div class="form-group">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                                            <circle cx="12" cy="7" r="4" />
                                        </svg>
                                    </span>
                                    法人姓名
                                    <span class="required">*</span>
                                </label>
                                <div class="input-wrapper">
                                    <input v-model="form.juridicalPerson" type="text" placeholder="请输入法人姓名" required
                                        class="form-input" />
                                    <div class="input-glow"></div>
                                </div>
                            </div>

                            <!-- 经办人 -->
                            <div class="form-group">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
                                            <circle cx="9" cy="7" r="4" />
                                            <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
                                            <path d="M16 3.13a4 4 0 0 1 0 7.75" />
                                        </svg>
                                    </span>
                                    经办人
                                    <span class="required">*</span>
                                </label>
                                <div class="input-wrapper">
                                    <input v-model="form.chargePerson" type="text" placeholder="请输入经办人姓名" required
                                        class="form-input" />
                                    <div class="input-glow"></div>
                                </div>
                            </div>

                            <!-- 经办人电话 -->
                            <div class="form-group"
                                :class="{ 'has-error': validationErrors.chargePhone && formTouched }">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path
                                                d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" />
                                        </svg>
                                    </span>
                                    经办人电话
                                    <span class="required">*</span>
                                </label>
                                <div class="input-wrapper">
                                    <input v-model="form.chargePhone" @blur="validateField('chargePhone')"
                                        @input="form.chargePhone = form.chargePhone.replace(/\D/g, '').slice(0, 11)"
                                        type="tel" placeholder="请输入 11 位手机号码" pattern="^1[3-9]\d{9}$" required
                                        class="form-input" />
                                    <div class="input-glow"></div>
                                </div>
                                <div v-if="validationErrors.chargePhone && validationErrors.chargePhone.length > 0 && formTouched" class="field-error">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <circle cx="12" cy="12" r="10" />
                                        <line x1="12" y1="8" x2="12" y2="12" />
                                        <line x1="12" y1="16" x2="12.01" y2="16" />
                                    </svg>
                                    <span>{{ validationErrors.chargePhone[0] }}</span>
                                </div>
                            </div>

                            <!-- 证书有效期 -->
                            <div class="form-group">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10" />
                                            <polyline points="12 6 12 12 16 14" />
                                        </svg>
                                    </span>
                                    证书有效期
                                    <span class="required">*</span>
                                </label>
                                <div class="input-wrapper select-wrapper">
                                    <select v-model="form.validityPeriod" required class="form-input form-select">
                                        <option :value="7">7 天（测试用）</option>
                                        <option :value="30">30 天</option>
                                        <option :value="90">90 天</option>
                                        <option :value="365">1 年（推荐）</option>
                                        <option :value="730">2 年</option>
                                        <option :value="1095">3 年</option>
                                        <option :value="1825">5 年</option>
                                    </select>
                                    <div class="input-glow"></div>
                                    <div class="select-arrow">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="6 9 12 15 18 9" />
                                        </svg>
                                    </div>
                                </div>
                            </div>

                            <!-- 密钥算法 -->
                            <div class="form-group">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                                            <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                                        </svg>
                                    </span>
                                    密钥算法
                                    <span class="required">*</span>
                                </label>
                                <div class="input-wrapper select-wrapper">
                                    <select v-model="form.keyAlgorithm" required class="form-input form-select">
                                        <option value="RSA">RSA（推荐）</option>
                                        <option value="EC">ECC（椭圆曲线）</option>
                                    </select>
                                    <div class="input-glow"></div>
                                    <div class="select-arrow">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="6 9 12 15 18 9" />
                                        </svg>
                                    </div>
                                </div>
                            </div>

                            <!-- 密钥长度 -->
                            <div class="form-group">
                                <label class="form-label">
                                    <span class="label-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z" />
                                            <circle cx="12" cy="12" r="3" />
                                        </svg>
                                    </span>
                                    密钥长度
                                    <span class="required">*</span>
                                </label>
                                <div class="input-wrapper select-wrapper">
                                    <select v-model="form.keySize" required class="form-input form-select">
                                        <option v-if="form.keyAlgorithm === 'RSA'" :value="2048">2048 位（推荐）</option>
                                        <option v-if="form.keyAlgorithm === 'RSA'" :value="3072">3072 位（高安全）</option>
                                        <option v-if="form.keyAlgorithm === 'RSA'" :value="4096">4096 位（最高安全）</option>
                                        <option v-if="form.keyAlgorithm === 'EC'" :value="256">P-256（推荐）</option>
                                        <option v-if="form.keyAlgorithm === 'EC'" :value="384">P-384（高安全）</option>
                                        <option v-if="form.keyAlgorithm === 'EC'" :value="521">P-521（最高安全）</option>
                                    </select>
                                    <div class="input-glow"></div>
                                    <div class="select-arrow">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="6 9 12 15 18 9" />
                                        </svg>
                                    </div>
                                </div>
                                <div class="field-hint">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <circle cx="12" cy="12" r="10" />
                                        <line x1="12" y1="16" x2="12" y2="12" />
                                        <line x1="12" y1="8" x2="12.01" y2="8" />
                                    </svg>
                                    <span>{{ getKeySizeHint() }}</span>
                                </div>
                            </div>
                        </div>

                        <!-- 证书用途说明 -->
                        <div class="usage-notice">
                            <div class="notice-header">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <circle cx="12" cy="12" r="10" />
                                    <line x1="12" y1="16" x2="12" y2="12" />
                                    <line x1="12" y1="8" x2="12.01" y2="8" />
                                </svg>
                                <span>重要提示</span>
                            </div>
                            <div class="notice-content">
                                <p>• 证书签发后，系统将为您自动生成密钥对</p>
                                <p>• 私钥仅下载一次，请务必妥善保存</p>
                                <p>• 建议将私钥备份到安全的存储设备</p>
                                <p>• 不要将私钥分享给他人或上传到云端</p>
                            </div>
                        </div>

                        <!-- 验证码 -->
                        <div class="form-group captcha-group"
                            :class="{ 'has-error': validationErrors.checkCode && formTouched }">
                            <label class="form-label">
                                <span class="label-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                                        <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                                    </svg>
                                </span>
                                验证码
                                <span class="required">*</span>
                            </label>
                            <div class="captcha-wrapper">
                                <div class="input-wrapper captcha-input">
                                    <input v-model="form.checkCode" @blur="validateField('checkCode')" type="text"
                                        placeholder="请输入验证码" required class="form-input" />
                                    <div class="input-glow"></div>
                                </div>
                                <div class="captcha-image" @click="refreshCaptcha" title="点击刷新验证码">
                                    <img :src="captchaImage" alt="验证码" />
                                    <div class="captcha-overlay">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <polyline points="23 4 23 10 17 10" />
                                            <path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10" />
                                        </svg>
                                    </div>
                                </div>
                            </div>
                            <div v-if="validationErrors.checkCode && validationErrors.checkCode.length > 0 && formTouched" class="field-error">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <circle cx="12" cy="12" r="10" />
                                    <line x1="12" y1="8" x2="12" y2="12" />
                                    <line x1="12" y1="16" x2="12.01" y2="16" />
                                </svg>
                                <span>{{ validationErrors.checkCode[0] }}</span>
                            </div>
                        </div>



                        <!-- 提交按钮 -->
                        <button type="submit" class="submit-btn" :disabled="loading">
                            <div class="btn-glow"></div>
                            <span class="btn-content">
                                <svg v-if="!loading" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                    stroke-width="2">
                                    <polyline points="20 6 9 17 4 12" />
                                </svg>
                                <span v-else class="spinner"></span>
                                {{ loading ? '提交中...' : '提交申请' }}
                            </span>
                        </button>

                        <!-- 消息提示 -->
                        <Transition name="message">
                            <div v-if="errorMessage" class="message error">
                                <div class="message-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <circle cx="12" cy="12" r="10" />
                                        <line x1="15" y1="9" x2="9" y2="15" />
                                        <line x1="9" y1="9" x2="15" y2="15" />
                                    </svg>
                                </div>
                                <span>{{ errorMessage }}</span>
                            </div>
                        </Transition>
                        <Transition name="message">
                            <div v-if="successMessage" class="message success">
                                <div class="message-icon">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                                        <polyline points="22 4 12 14.01 9 11.01" />
                                    </svg>
                                </div>
                                <span>{{ successMessage }}</span>
                            </div>
                        </Transition>
                    </form>
                </div>
            </div>
        </main>
    </div>
</template>

<script>
import api from '../api'

export default {
    name: 'Apply',
    data() {
        return {
            form: {
                organization: '',
                registrationNumber: '',
                juridicalPerson: '',
                chargePerson: '',
                chargePhone: '',
                validityPeriod: 365,
                keyAlgorithm: 'RSA',
                keySize: 2048,
                checkCode: ''
            },
            captchaImage: '',
            loading: false,
            submitting: false,
            errorMessage: '',
            successMessage: '',
            validationErrors: {},
            formTouched: false,
            lastSubmittedAt: null
        };
    },
    computed: {
        user() {
            return this.$store.state.user
        }
    },
    mounted() {
        this.refreshCaptcha()
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



        async refreshCaptcha() {
            try {
                const response = await api.captcha.generate()
                if (response.code === 200) {
                    this.captchaImage = response.data.image
                    sessionStorage.setItem('checkCode_session', response.data.code)
                }
            } catch (error) {
                console.error('获取验证码失败', error)
            }
        },

        triggerFileInput() {
            this.$refs.fileInput.click()
        },



        // 实时验证方法
        validateField(field) {
            const value = this.form[field]
            const errors = []

            switch (field) {
                case 'organization':
                    if (!value || value.trim().length === 0) {
                        errors.push('请输入组织机构名称')
                    } else if (value.length < 2) {
                        errors.push('组织机构名称至少 2 个字符')
                    }
                    break

                case 'registrationNumber':
                    if (!value || value.trim().length === 0) {
                        errors.push('请输入工商注册号')
                    } else if (!/^[0-9A-Z]{18}$/.test(value)) {
                        errors.push('工商注册号格式不正确（应为 18 位统一社会信用代码）')
                    }
                    break

                case 'juridicalPerson':
                    if (!value || value.trim().length === 0) {
                        errors.push('请输入法人姓名')
                    } else if (value.length < 2) {
                        errors.push('法人姓名至少 2 个字符')
                    }
                    break

                case 'chargePerson':
                    if (!value || value.trim().length === 0) {
                        errors.push('请输入经办人姓名')
                    } else if (value.length < 2) {
                        errors.push('经办人姓名至少 2 个字符')
                    }
                    break

                case 'chargePhone':
                    if (!value || value.trim().length === 0) {
                        errors.push('请输入经办人电话')
                    } else if (!/^1[3-9]\d{9}$/.test(value)) {
                        errors.push('请输入正确的 11 位手机号码')
                    }
                    break

                case 'checkCode':
                    if (!value || value.trim().length === 0) {
                        errors.push('请输入验证码')
                    } else if (value.toLowerCase() !== sessionStorage.getItem('checkCode_session')?.toLowerCase()) {
                        errors.push('验证码不正确')
                    }
                    break

                case 'keyAlgorithm':
                    if (!value || (value !== 'RSA' && value !== 'EC')) {
                        errors.push('密钥算法必须是 RSA 或 EC')
                    }
                    break

                case 'keySize':
                    if (!value || value < 256) {
                        errors.push('密钥长度最小 256 位')
                    }
                    if (this.form.keyAlgorithm === 'RSA' && (value < 2048 || value > 4096)) {
                        errors.push('RSA 密钥长度必须在 2048-4096 位之间')
                    }
                    if (this.form.keyAlgorithm === 'EC' && (value < 256 || value > 521)) {
                        errors.push('EC 密钥长度必须在 256-521 位之间')
                    }
                    break
            }

            this.validationErrors[field] = errors
            return errors.length === 0
        },

        // 验证所有字段
        validateAllFields() {
            const fields = ['organization', 'registrationNumber', 'juridicalPerson',
                'chargePerson', 'chargePhone', 'checkCode', 'keyAlgorithm', 'keySize']
            let isValid = true

            fields.forEach(field => {
                if (!this.validateField(field)) {
                    isValid = false
                }
            })

            return isValid
        },

        // 显示消息提示
        showMessage(type, message) {
            if (type === 'success') {
                this.successMessage = message
                this.errorMessage = ''
                setTimeout(() => {
                    this.successMessage = ''
                }, 3000)
            } else {
                this.errorMessage = message
                this.successMessage = ''
                setTimeout(() => {
                    this.errorMessage = ''
                }, 3000)
            }
        },

        async handleSubmit() {
            // 防止重复提交
            const now = Date.now()
            if (this.submitting || (this.lastSubmittedAt && now - this.lastSubmittedAt < 3000)) {
                this.showMessage('error', '请勿重复提交')
                return
            }

            this.formTouched = true

            // 验证所有字段
            if (!this.validateAllFields()) {
                this.showMessage('error', '请完善表单信息')
                return
            }

            this.submitting = true
            this.loading = true
            this.lastSubmittedAt = now
            this.errorMessage = ''
            this.successMessage = ''

            try {
                const response = await api.certificates.apply(this.form)

                if (response.code === 200) {
                    const serialNumber = response.data.serialNumber
                    this.showMessage('success', '证书申请成功！序列号：' + serialNumber + '，即将跳转到下载页面')

                    // 延迟跳转，让用户看到成功信息
                    setTimeout(() => {
                        this.$router.push('/certificates')
                    }, 3000)
                } else {
                    // 处理验证错误详情
                    let errorMsg = response.message || '申请失败，请稍后重试'
                    if (response.data && response.data.fieldErrors) {
                        const fieldErrors = response.data.fieldErrors
                        const errorMessages = Object.values(fieldErrors)
                        if (errorMessages.length > 0) {
                            errorMsg = errorMessages.join('；')
                        }
                    }
                    this.showMessage('error', errorMsg)
                    await this.refreshCaptcha()
                    this.form.checkCode = ''
                }
            } catch (error) {
                // 处理验证错误详情
                let errorMsg = error.response?.data?.message || error.message || '申请失败，请稍后重试'
                if (error.response?.data?.data?.fieldErrors) {
                    const fieldErrors = error.response.data.data.fieldErrors
                    const errorMessages = Object.values(fieldErrors)
                    if (errorMessages.length > 0) {
                        errorMsg = errorMessages.join('；')
                    }
                }
                this.showMessage('error', errorMsg)
                await this.refreshCaptcha()
                this.form.checkCode = ''
            } finally {
                this.loading = false
                setTimeout(() => {
                    this.submitting = false
                }, 3000)
            }
        },

        // 获取密钥长度提示
        getKeySizeHint() {
            if (this.form.keyAlgorithm === 'RSA') {
                if (this.form.keySize === 2048) {
                    return '2048 位 RSA 密钥，安全性与性能的平衡选择，适用于大多数场景'
                } else if (this.form.keySize === 3072) {
                    return '3072 位 RSA 密钥，更高的安全性，适用于敏感数据处理'
                } else {
                    return '4096 位 RSA 密钥，最高安全级别，适用于金融、政务等高安全场景'
                }
            } else {
                if (this.form.keySize === 256) {
                    return 'P-256 椭圆曲线密钥，相当于 RSA 3072 位，性能更好，适合移动设备'
                } else if (this.form.keySize === 384) {
                    return 'P-384 椭圆曲线密钥，相当于 RSA 7680 位，高安全性'
                } else {
                    return 'P-521 椭圆曲线密钥，最高安全级别，性能开销较大'
                }
            }
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
.apply-page {
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
    max-width: 900px;
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
    color: #0f172a;
    margin-bottom: 8px;
    background: linear-gradient(135deg, #0f172a, #3b82f6);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
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

/* 表单卡片 */
.form-wrapper {
    position: relative;
}

.form-card {
    background: #ffffff;
    border: 1px solid #e2e8f0;
    border-radius: 24px;
    padding: 40px;
    position: relative;
    overflow: hidden;
    box-shadow:
        0 4px 6px -1px rgba(0, 0, 0, 0.1),
        0 2px 4px -1px rgba(0, 0, 0, 0.06),
        0 0 0 1px rgba(59, 130, 246, 0.05),
        0 20px 40px rgba(59, 130, 246, 0.1);
    animation: cardEntrance 0.6s ease-out;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.form-card:hover {
    box-shadow:
        0 8px 12px -1px rgba(0, 0, 0, 0.15),
        0 4px 8px -1px rgba(0, 0, 0, 0.1),
        0 0 0 1px rgba(59, 130, 246, 0.1),
        0 30px 60px rgba(59, 130, 246, 0.15);
    transform: translateY(-4px);
}

.card-shine {
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(59, 130, 246, 0.05), transparent);
    animation: shine 8s infinite;
    pointer-events: none;
}

.card-glow {
    position: absolute;
    inset: -2px;
    background: linear-gradient(135deg,
            rgba(59, 130, 246, 0.3),
            rgba(139, 92, 246, 0.3),
            rgba(59, 130, 246, 0.3));
    background-size: 200% 200%;
    border-radius: 24px;
    opacity: 0;
    z-index: -1;
    transition: opacity 0.4s ease;
    animation: glowRotate 8s linear infinite;
}

.form-card:hover .card-glow {
    opacity: 0.5;
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

@keyframes glowRotate {
    0% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }

    100% {
        background-position: 0% 50%;
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

/* 表单网格 */
.form-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;
    margin-bottom: 24px;
}

.form-group {
    position: relative;
}

.form-group.captcha-group,
.form-group.file-group {
    grid-column: span 2;
}

.form-label {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 500;
    color: #334155;
    margin-bottom: 10px;
    transition: color 0.3s ease;
}

.form-group:hover .form-label {
    color: #1e293b;
}

.label-icon {
    width: 18px;
    height: 18px;
    color: #3b82f6;
    transition: transform 0.3s ease;
}

.form-group:hover .label-icon {
    transform: scale(1.15) rotate(5deg);
}

.label-icon svg {
    width: 100%;
    height: 100%;
}

.required {
    color: #ef4444;
}

/* 输入框 */
.input-wrapper {
    position: relative;
}

.form-input {
    width: 100%;
    padding: 14px 16px;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    font-size: 15px;
    color: #0f172a;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
}

.form-input::placeholder {
    color: #94a3b8;
    transition: opacity 0.3s ease;
}

.form-input:focus {
    outline: none;
    border-color: #3b82f6;
    background: #ffffff;
    box-shadow:
        0 0 0 3px rgba(59, 130, 246, 0.1),
        0 4px 12px rgba(59, 130, 246, 0.15);
    transform: translateY(-1px);
}

.form-input:focus::placeholder {
    opacity: 0.6;
}

.input-glow {
    position: absolute;
    inset: -1px;
    border-radius: 12px;
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    opacity: 0;
    z-index: -1;
    transition: opacity 0.3s ease;
    filter: blur(8px);
}

.form-input:focus~.input-glow {
    opacity: 0.4;
}

/* 字段错误提示 */
.form-group.has-error .form-input {
    border-color: #ef4444;
}

.form-group.has-error .form-input:focus {
    box-shadow:
        0 0 0 3px rgba(239, 68, 68, 0.1),
        0 4px 12px rgba(239, 68, 68, 0.15);
}

.field-error {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-top: 6px;
    padding: 6px 10px;
    background: rgba(239, 68, 68, 0.1);
    border-radius: 6px;
    font-size: 12px;
    color: #ef4444;
    animation: slideDown 0.3s ease-out;
}

.field-error svg {
    width: 14px;
    height: 14px;
    flex-shrink: 0;
}

/* 字段提示 */
.field-hint {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-top: 8px;
    padding: 8px 12px;
    background: rgba(59, 130, 246, 0.1);
    border-radius: 8px;
    font-size: 12px;
    color: #3b82f6;
    animation: fadeIn 0.3s ease-out;
}

.field-hint svg {
    width: 14px;
    height: 14px;
    flex-shrink: 0;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(-4px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 使用提示框 */
.usage-notice {
    margin-top: 24px;
    padding: 20px;
    background: linear-gradient(135deg, rgba(245, 158, 11, 0.1), rgba(251, 191, 36, 0.1));
    border: 1px solid rgba(245, 158, 11, 0.3);
    border-radius: 16px;
}

.notice-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 16px;
    color: #92400e;
    font-weight: 600;
    font-size: 14px;
}

.notice-header svg {
    width: 18px;
    height: 18px;
}

.notice-content {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.notice-content p {
    font-size: 13px;
    color: #78350f;
    line-height: 1.6;
    padding-left: 20px;
    position: relative;
}

.notice-content p::before {
    content: '';
    position: absolute;
    left: 0;
    top: 8px;
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: #f59e0b;
}

@keyframes slideDown {
    from {
        opacity: 0;
        transform: translateY(-4px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 文件上传错误 */
.file-upload-zone.error {
    border-color: #ef4444;
    background: rgba(239, 68, 68, 0.05);
}

.file-upload-zone.error:hover {
    border-color: #dc2626;
    box-shadow: 0 8px 24px rgba(239, 68, 68, 0.15);
}

/* 下拉选择 */
.select-wrapper {
    position: relative;
}

.form-select {
    appearance: none;
    cursor: pointer;
}

.select-arrow {
    position: absolute;
    right: 16px;
    top: 50%;
    transform: translateY(-50%);
    width: 20px;
    height: 20px;
    color: #64748b;
    pointer-events: none;
}

.select-arrow svg {
    width: 100%;
    height: 100%;
}

/* 验证码 */
.captcha-wrapper {
    display: flex;
    gap: 16px;
}

.captcha-input {
    flex: 1;
}

.captcha-image {
    position: relative;
    width: 140px;
    height: 50px;
    border-radius: 12px;
    overflow: hidden;
    cursor: pointer;
    border: 1px solid #e2e8f0;
    background: #f8fafc;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.captcha-image:hover {
    border-color: #3b82f6;
    transform: scale(1.05);
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.2);
}

.captcha-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: filter 0.3s ease;
}

.captcha-image:hover img {
    filter: brightness(1.1);
}

.captcha-overlay {
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(139, 92, 246, 0.2));
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s ease;
    backdrop-filter: blur(2px);
}

.captcha-image:hover .captcha-overlay {
    opacity: 1;
}

.captcha-overlay svg {
    width: 24px;
    height: 24px;
    color: #3b82f6;
    animation: rotateRefresh 0.6s ease;
}

@keyframes rotateRefresh {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}

/* 文件上传 */
.file-upload-zone {
    position: relative;
    padding: 40px;
    border: 2px dashed #cbd5e1;
    border-radius: 16px;
    background: #f8fafc;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    overflow: hidden;
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

.file-upload-zone.has-file:hover {
    transform: scale(1.02);
    box-shadow: 0 8px 24px rgba(16, 185, 129, 0.2);
}

.file-input {
    position: absolute;
    inset: 0;
    opacity: 0;
    cursor: pointer;
}

.upload-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 16px;
}

.upload-icon {
    width: 60px;
    height: 60px;
    border-radius: 16px;
    background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(139, 92, 246, 0.2));
    display: flex;
    align-items: center;
    justify-content: center;
    color: #3b82f6;
}

.upload-icon svg {
    width: 32px;
    height: 32px;
}

.upload-title {
    font-size: 16px;
    font-weight: 500;
    color: #0f172a;
}

.upload-hint {
    font-size: 13px;
    color: #64748b;
}

.upload-glow {
    position: absolute;
    inset: 0;
    background: radial-gradient(circle at center, rgba(59, 130, 246, 0.1), transparent 70%);
    opacity: 0;
    transition: opacity 0.3s ease;
}

.file-upload-zone:hover .upload-glow {
    opacity: 1;
}

/* 提交按钮 */
.submit-btn {
    position: relative;
    width: 100%;
    padding: 16px;
    margin-top: 32px;
    background: linear-gradient(135deg, #3b82f6, #8b5cf6);
    border: none;
    border-radius: 12px;
    font-size: 16px;
    font-weight: 600;
    color: #ffffff;
    cursor: pointer;
    overflow: hidden;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow:
        0 4px 12px rgba(59, 130, 246, 0.3),
        0 0 0 1px rgba(255, 255, 255, 0.1);
}

.submit-btn::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    transform: translateX(-100%);
    transition: transform 0.6s ease;
}

.submit-btn:hover:not(:disabled)::before {
    transform: translateX(100%);
}

.submit-btn:hover:not(:disabled) {
    transform: translateY(-3px) scale(1.02);
    box-shadow:
        0 8px 24px rgba(59, 130, 246, 0.4),
        0 0 0 1px rgba(255, 255, 255, 0.2);
}

.submit-btn:active:not(:disabled) {
    transform: translateY(-1px) scale(0.98);
}

.submit-btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
}

.btn-content {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
}

.btn-content svg {
    width: 20px;
    height: 20px;
}

.spinner {
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

/* 消息提示 */
.message {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 16px;
    margin-top: 20px;
    border-radius: 12px;
    font-size: 14px;
}

.message.error {
    background: #fef2f2;
    border: 1px solid #fecaca;
    color: #dc2626;
}

.message.success {
    background: #f0fdf4;
    border: 1px solid #bbf7d0;
    color: #16a34a;
}

.message-icon {
    width: 20px;
    height: 20px;
    flex-shrink: 0;
}

.message-icon svg {
    width: 100%;
    height: 100%;
}

/* 消息动画 */
.message-enter-active,
.message-leave-active {
    transition: all 0.3s ease;
}

.message-enter-from,
.message-leave-to {
    opacity: 0;
    transform: translateY(-10px);
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

    .form-card {
        padding: 24px;
    }

    .form-grid {
        grid-template-columns: 1fr;
    }

    .form-group.captcha-group,
    .form-group.file-group {
        grid-column: span 1;
    }

    .captcha-wrapper {
        flex-direction: column;
    }

    .captcha-image {
        width: 100%;
        height: 60px;
    }
}
</style>
