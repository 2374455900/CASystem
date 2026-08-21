<template>
  <div class="revoke-page">
    <!-- 主内容 -->

    <main class="main-content">
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-glow"></div>
        <div class="header-content">
          <div class="header-icon warning">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <circle cx="12" cy="12" r="10"/>
              <line x1="15" y1="9" x2="9" y2="15"/>
              <line x1="9" y1="9" x2="15" y2="15"/>
            </svg>
          </div>
          <div class="header-text">
            <h1>撤销证书</h1>
            <p>选择要撤销的证书，此操作不可恢复，请谨慎操作</p>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="certificates.length === 0" class="empty-state">
        <div class="empty-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <circle cx="12" cy="12" r="10"/>
            <line x1="15" y1="9" x2="9" y2="15"/>
            <line x1="9" y1="9" x2="15" y2="15"/>
          </svg>
        </div>
        <h3>没有可撤销的证书</h3>
        <p>您当前没有处于"使用中"状态的证书</p>
      </div>

      <!-- 撤销表单 -->
      <div v-else class="revoke-wrapper">
        <div class="revoke-card">
          <div class="card-shine"></div>
          <form @submit.prevent="handleRevoke" class="revoke-form">
            <!-- 证书选择 -->
            <div class="form-section">
              <h3 class="section-title">
                <span class="section-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                    <polyline points="14 2 14 8 20 8"/>
                    <line x1="16" y1="13" x2="8" y2="13"/>
                    <line x1="16" y1="17" x2="8" y2="17"/>
                    <polyline points="10 9 9 9 8 9"/>
                  </svg>
                </span>
                选择证书
              </h3>
              <div class="certificates-list">
                <label 
                  v-for="cert in activeCertificates" 
                  :key="cert.serialNumber"
                  class="cert-option"
                  :class="{ selected: selectedSerial === cert.serialNumber }"
                >
                  <input 
                    type="radio" 
                    v-model="selectedSerial" 
                    :value="cert.serialNumber"
                    name="cert"
                  />
                  <div class="option-content">
                    <div class="option-header">
                      <span class="cert-serial">{{ cert.serialNumber }}</span>
                      <span class="cert-status">使用中</span>
                    </div>
                    <div class="option-info">
                      <span class="info-item">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                          <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                          <polyline points="9 22 9 12 15 12 15 22"/>
                        </svg>
                        {{ cert.organization }}
                      </span>
                      <span class="info-item">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                          <circle cx="12" cy="12" r="10"/>
                          <polyline points="12 6 12 12 16 14"/>
                        </svg>
                        有效期至 {{ formatDate(cert.endTime) }}
                      </span>
                    </div>
                  </div>
                  <div class="option-check">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <polyline points="20 6 9 17 4 12"/>
                    </svg>
                  </div>
                </label>
              </div>
            </div>

            <!-- 验证信息 -->
            <div class="form-section" v-if="selectedSerial">
              <h3 class="section-title">
                <span class="section-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                  </svg>
                </span>
                安全验证
              </h3>
              
              <!-- 密码验证 -->
              <div class="form-group">
                <label class="form-label">
                  验证密码
                  <span class="required">*</span>
                </label>
                <div class="input-wrapper">
                  <input
                    v-model="revokeForm.revokePassword"
                    type="password"
                    placeholder="请输入登录密码进行验证"
                    required
                    class="form-input"
                  />
                  <div class="input-glow"></div>
                </div>
              </div>

              <!-- 验证码 -->
              <div class="form-group captcha-group">
                <label class="form-label">
                  验证码
                  <span class="required">*</span>
                </label>
                <div class="captcha-wrapper">
                  <div class="input-wrapper captcha-input">
                    <input
                      v-model="revokeForm.checkCode"
                      type="text"
                      placeholder="请输入验证码"
                      required
                      class="form-input"
                    />
                    <div class="input-glow"></div>
                  </div>
                  <div class="captcha-image" @click="refreshCaptcha">
                    <img :src="captchaImage" alt="验证码" />
                    <div class="captcha-overlay">
                      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="23 4 23 10 17 10"/>
                        <path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/>
                      </svg>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 警告提示 -->
            <div class="warning-box" v-if="selectedSerial">
              <div class="warning-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
                  <line x1="12" y1="9" x2="12" y2="13"/>
                  <line x1="12" y1="17" x2="12.01" y2="17"/>
                </svg>
              </div>
              <div class="warning-content">
                <h4>警告：此操作不可撤销</h4>
                <p>证书撤销后将立即失效，无法恢复使用。请确保您确实需要撤销此证书。</p>
              </div>
            </div>

            <!-- 提交按钮 -->
            <button 
              type="submit" 
              class="submit-btn" 
              :disabled="loading || !selectedSerial"
            >
              <div class="btn-glow"></div>
              <span class="btn-content">
                <svg v-if="!loading" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <line x1="15" y1="9" x2="9" y2="15"/>
                  <line x1="9" y1="9" x2="15" y2="15"/>
                </svg>
                <span v-else class="spinner"></span>
                {{ loading ? '撤销中...' : '确认撤销' }}
              </span>
            </button>

            <!-- 消息提示 -->
            <Transition name="message">
              <div v-if="errorMessage" class="message error">
                <div class="message-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10"/>
                    <line x1="15" y1="9" x2="9" y2="15"/>
                    <line x1="9" y1="9" x2="15" y2="15"/>
                  </svg>
                </div>
                <span>{{ errorMessage }}</span>
              </div>
            </Transition>
            <Transition name="message">
              <div v-if="successMessage" class="message success">
                <div class="message-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                    <polyline points="22 4 12 14.01 9 11.01"/>
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
  name: 'Revoke',
  data() {
    return {
      certificates: [],
      selectedSerial: '',
      revokeForm: {
        revokePassword: '',
        checkCode: ''
      },
      captchaImage: '',
      loading: false,
      errorMessage: '',
      successMessage: ''
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    },
    activeCertificates() {
      return this.certificates.filter(c => c.state === '使用中')
    }
  },
  async mounted() {
    await this.loadCertificates()
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
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      })
    },

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

    async handleRevoke() {
      this.loading = true
      this.errorMessage = ''
      this.successMessage = ''

      try {
        const response = await api.certificates.revoke({
          serialNumber: this.selectedSerial,
          revokePassword: this.revokeForm.revokePassword,
          checkCode: this.revokeForm.checkCode
        })

        if (response.code === 200) {
          this.successMessage = '证书撤销成功！序列号：' + response.data.serialNumber
          this.selectedSerial = ''
          this.revokeForm.revokePassword = ''
          this.revokeForm.checkCode = ''
          await this.loadCertificates()
        } else {
          this.errorMessage = response.message
          this.refreshCaptcha()
        }
      } catch (error) {
        console.error('撤销证书错误详情:', error)
        this.errorMessage = error.response?.data?.message || error.message || '撤销失败，请稍后重试'
        this.refreshCaptcha()
      } finally {
        this.loading = false
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
.revoke-page {
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
  background: linear-gradient(135deg, #ef4444, #dc2626);
  top: -200px;
  right: -200px;
  animation: float 20s ease-in-out infinite;
}

.orb-2 {
  width: 500px;
  height: 500px;
  background: linear-gradient(135deg, #f59e0b, #d97706);
  bottom: -150px;
  left: -150px;
  animation: float 25s ease-in-out infinite reverse;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(30px, -30px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
}

.orb-3 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation-delay: -14s;
  opacity: 0.15;
}

/* 浮动粒子 */
.floating-particles {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.particle {
  position: absolute;
  background: radial-gradient(circle, rgba(239, 68, 68, 0.4), transparent);
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
    linear-gradient(rgba(239, 68, 68, 0.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(239, 68, 68, 0.05) 1px, transparent 1px);
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
  background: linear-gradient(135deg, #ef4444 0%, #f59e0b 100%);
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
  color: #ef4444;
  background: rgba(239, 68, 68, 0.1);
}

.nav-link.active {
  color: #ef4444;
  background: rgba(239, 68, 68, 0.15);
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
  background: linear-gradient(135deg, #ef4444, #f59e0b);
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
}

.logout-btn {
  width: 38px;
  height: 38px;
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

.logout-btn:hover {
  background: #f1f5f9;
  border-color: #cbd5e1;
  color: #3b82f6;
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
  max-width: 800px;
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
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.3), rgba(245, 158, 11, 0.3));
  filter: blur(60px);
  border-radius: 50%;
}

.header-content {
  position: relative;
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-icon {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.2), rgba(245, 158, 11, 0.2));
  border: 1px solid rgba(239, 68, 68, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ef4444;
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
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #10b981;
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
}

/* 撤销卡片 */
.revoke-wrapper {
  position: relative;
}

.revoke-card {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 24px;
  padding: 40px;
  position: relative;
  overflow: hidden;
}

.card-shine {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(239, 68, 68, 0.03), transparent);
  animation: shine 8s infinite;
}

@keyframes shine {
  0%, 100% { left: -100%; }
  50% { left: 100%; }
}

/* 表单部分 */
.form-section {
  margin-bottom: 32px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 18px;
  font-weight: 600;
  color: #0f172a;
  margin-bottom: 20px;
}

.section-icon {
  width: 24px;
  height: 24px;
  color: #ef4444;
}

.section-icon svg {
  width: 100%;
  height: 100%;
}

/* 证书列表 */
.certificates-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.cert-option {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #f8fafc;
  border: 2px solid #e2e8f0;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cert-option:hover {
  border-color: rgba(239, 68, 68, 0.3);
  background: rgba(239, 68, 68, 0.05);
}

.cert-option.selected {
  border-color: #ef4444;
  background: rgba(239, 68, 68, 0.1);
}

.cert-option input {
  display: none;
}

.option-content {
  flex: 1;
}

.option-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.cert-serial {
  font-family: 'JetBrains Mono', monospace;
  font-size: 14px;
  font-weight: 600;
  color: #0f172a;
}

.cert-status {
  padding: 4px 10px;
  background: rgba(16, 185, 129, 0.15);
  color: #10b981;
  border-radius: 20px;
  font-size: 11px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 4px;
  white-space: nowrap;
}

.option-info {
  display: flex;
  gap: 24px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #64748b;
}

.info-item svg {
  width: 14px;
  height: 14px;
}

.option-check {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  border: 2px solid #cbd5e1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: transparent;
  transition: all 0.3s ease;
}

.option-check svg {
  width: 16px;
  height: 16px;
}

.cert-option.selected .option-check {
  background: #ef4444;
  border-color: #ef4444;
  color: #ffffff;
}

/* 表单组 */
.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #475569;
  margin-bottom: 10px;
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
  transition: all 0.3s ease;
}

.form-input::placeholder {
  color: #94a3b8;
}

.form-input:focus {
  outline: none;
  border-color: #ef4444;
  background: #ffffff;
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
}

.input-glow {
  position: absolute;
  inset: -1px;
  border-radius: 12px;
  background: linear-gradient(135deg, #ef4444, #f59e0b);
  opacity: 0;
  z-index: -1;
  transition: opacity 0.3s ease;
}

.form-input:focus ~ .input-glow {
  opacity: 0.5;
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
}

.captcha-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.captcha-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.captcha-image:hover .captcha-overlay {
  opacity: 1;
}

.captcha-overlay svg {
  width: 24px;
  height: 24px;
  color: #ffffff;
}

/* 警告框 */
.warning-box {
  display: flex;
  gap: 16px;
  padding: 20px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.2);
  border-radius: 12px;
  margin-bottom: 24px;
}

.warning-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: rgba(239, 68, 68, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ef4444;
  flex-shrink: 0;
}

.warning-icon svg {
  width: 24px;
  height: 24px;
}

.warning-content h4 {
  font-size: 15px;
  font-weight: 600;
  color: #ef4444;
  margin-bottom: 6px;
}

.warning-content p {
  font-size: 13px;
  color: #64748b;
  line-height: 1.5;
}

/* 提交按钮 */
.submit-btn {
  position: relative;
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #ef4444, #f59e0b);
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  color: #ffffff;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s ease;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 40px rgba(239, 68, 68, 0.3);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-glow {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transform: translateX(-100%);
  transition: transform 0.6s ease;
}

.submit-btn:hover .btn-glow {
  transform: translateX(100%);
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
  to { transform: rotate(360deg); }
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

  .revoke-card {
    padding: 24px;
  }

  .option-info {
    flex-direction: column;
    gap: 8px;
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
