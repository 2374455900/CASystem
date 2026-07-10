<template>
  <div class="login-page">
    <div class="bg-decoration">
      <div class="bg-circle bg-circle-1"></div>
      <div class="bg-circle bg-circle-2"></div>
      <div class="bg-circle bg-circle-3"></div>
      <div class="bg-grid"></div>
    </div>

    <div class="login-container">
      <div class="brand-section">
        <div class="brand-content">
          <div class="logo">
            <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="8" y="8" width="32" height="32" rx="8" fill="url(#gradient1)" fill-opacity="0.2"/>
              <rect x="12" y="12" width="24" height="24" rx="6" stroke="url(#gradient1)" stroke-width="2"/>
              <path d="M18 24L22 28L30 20" stroke="url(#gradient1)" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
              <defs>
                <linearGradient id="gradient1" x1="8" y1="8" x2="40" y2="40" gradientUnits="userSpaceOnUse">
                  <stop stop-color="#3b82f6"/>
                  <stop offset="1" stop-color="#8b5cf6"/>
                </linearGradient>
              </defs>
            </svg>
          </div>
          <h1 class="brand-title">CA证书管理系统</h1>
          <p class="brand-subtitle">安全 · 可信 · 专业</p>
          <div class="brand-features">
            <div class="feature-item">
              <span class="feature-icon">🔐</span>
              <span>企业级安全加密</span>
            </div>
            <div class="feature-item">
              <span class="feature-icon">⚡</span>
              <span>秒级证书签发</span>
            </div>
            <div class="feature-item">
              <span class="feature-icon">🛡️</span>
              <span>全生命周期管理</span>
            </div>
          </div>
        </div>
      </div>

      <div class="form-section">
        <div class="form-card">
          <div class="tab-switcher">
            <button :class="['tab-btn', { active: activeTab === 'login' }]" @click="activeTab = 'login'">登录</button>
            <button :class="['tab-btn', { active: activeTab === 'register' }]" @click="activeTab = 'register'">注册</button>
            <div class="tab-indicator" :style="indicatorStyle"></div>
          </div>

          <form v-if="activeTab === 'login'" @submit.prevent="handleLogin" class="auth-form">
            <div class="form-header">
              <h2>欢迎回来</h2>
              <p>请输入您的账号信息</p>
            </div>

            <div class="input-group">
              <label class="input-label">
                <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                  <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"/>
                  <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"/>
                </svg>
                邮箱地址
              </label>
              <input v-model="loginForm.email" type="email" class="input-field" placeholder="请输入邮箱" required />
            </div>

            <div class="input-group">
              <label class="input-label">
                <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd"/>
                </svg>
                密码
              </label>
              <div class="password-input">
                <input v-model="loginForm.password" :type="showPassword ? 'text' : 'password'" class="input-field" placeholder="请输入密码" required />
                <button type="button" class="toggle-password" @click="showPassword = !showPassword">
                  <svg v-if="!showPassword" viewBox="0 0 20 20" fill="currentColor">
                    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                    <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                  </svg>
                  <svg v-else viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.78-1.781zm4.261 4.26l1.514 1.515a2.003 2.003 0 012.45 2.45l1.514 1.514a4 4 0 00-5.478-5.478z" clip-rule="evenodd"/>
                    <path d="M12.454 16.697L9.75 13.992a4 4 0 01-3.742-3.741L2.335 6.578A9.98 9.98 0 00.458 10c1.274 4.057 5.065 7 9.542 7 .847 0 1.669-.105 2.454-.303z"/>
                  </svg>
                </button>
              </div>
            </div>

            <div class="form-options">
              <label class="checkbox-wrapper">
                <input type="checkbox" v-model="rememberMe" />
                <span class="checkmark"></span>
                <span class="checkbox-label">记住我</span>
              </label>
              <a href="#" class="forgot-link" @click.prevent="forgotPassword">忘记密码?</a>
            </div>

            <button type="submit" class="btn btn-primary submit-btn" :disabled="loading">
              <span v-if="!loading">登录</span>
              <span v-else class="loading-spinner"></span>
            </button>

            <transition name="fade">
              <div v-if="errorMessage" class="alert alert-error">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd"/>
                </svg>
                {{ errorMessage }}
              </div>
            </transition>
          </form>

          <form v-else @submit.prevent="handleRegister" class="auth-form">
            <div class="form-header">
              <h2>创建账号</h2>
              <p>开始使用CA证书管理系统</p>
            </div>

            <div class="input-group">
              <label class="input-label">
                <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                  <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"/>
                  <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"/>
                </svg>
                邮箱地址
              </label>
              <input v-model="registerForm.email" type="email" class="input-field" placeholder="请输入邮箱" required />
            </div>

            <div class="input-group">
              <label class="input-label">
                <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"/>
                </svg>
                用户名
              </label>
              <input v-model="registerForm.username" type="text" class="input-field" placeholder="请输入用户名" required />
            </div>

            <div class="input-group">
              <label class="input-label">
                <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd"/>
                </svg>
                密码
              </label>
              <input v-model="registerForm.password" type="password" class="input-field" placeholder="请输入密码 (至少6位)" required />
            </div>

            <div class="input-group">
              <label class="input-label">
                <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd"/>
                </svg>
                确认密码
              </label>
              <input v-model="registerForm.confirmPassword" type="password" class="input-field" placeholder="请再次输入密码" required />
            </div>

            <button type="submit" class="btn btn-primary submit-btn" :disabled="loading">
              <span v-if="!loading">注册</span>
              <span v-else class="loading-spinner"></span>
            </button>

            <transition name="fade">
              <div v-if="errorMessage" class="alert alert-error">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd"/>
                </svg>
                {{ errorMessage }}
              </div>
            </transition>

            <transition name="fade">
              <div v-if="successMessage" class="alert alert-success">
                <svg viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                </svg>
                {{ successMessage }}
              </div>
            </transition>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '../api'

export default {
  name: 'Login',
  data() {
    return {
      activeTab: 'login',
      loginForm: {
        email: '',
        password: ''
      },
      registerForm: {
        email: '',
        username: '',
        password: '',
        confirmPassword: ''
      },
      rememberMe: false,
      showPassword: false,
      loading: false,
      errorMessage: '',
      successMessage: ''
    }
  },
  computed: {
    indicatorStyle() {
      return {
        transform: this.activeTab === 'login' ? 'translateX(0)' : 'translateX(100%)'
      }
    }
  },
  methods: {
    async handleLogin() {
      this.loading = true
      this.errorMessage = ''

      try {
        const response = await api.auth.login(this.loginForm.email, this.loginForm.password)

        if (response.code === 200) {
          await this.$store.dispatch('login', {
            token: response.data.token,
            user: response.data.user
          })
          // 管理员直接跳转至管理员后台，普通用户跳转至用户端前台
          if (response.data.user.authority === '1') {
            this.$router.push('/admin/dashboard')
          } else {
            this.$router.push('/home')
          }
        } else {
          this.errorMessage = response.message
        }
      } catch (error) {
        this.errorMessage = error.message || '登录失败，请稍后重试'
      } finally {
        this.loading = false
      }
    },

    async handleRegister() {
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        this.errorMessage = '两次输入的密码不一致'
        return
      }

      this.loading = true
      this.errorMessage = ''
      this.successMessage = ''

      try {
        const response = await api.auth.register(
          this.registerForm.email,
          this.registerForm.username,
          this.registerForm.password
        )

        if (response.code === 200) {
          this.successMessage = '注册成功！请登录'
          this.registerForm = {
            email: '',
            username: '',
            password: '',
            confirmPassword: ''
          }
          setTimeout(() => {
            this.activeTab = 'login'
          }, 1500)
        } else {
          this.errorMessage = response.message
        }
      } catch (error) {
        this.errorMessage = error.message || '注册失败，请稍后重试'
      } finally {
        this.loading = false
      }
    },

    forgotPassword() {
      this.$router.push('/forgot-password')
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  position: relative;
  overflow: hidden;
  background: #f8fafc;
}

.bg-decoration {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 0;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.4;
}

.bg-circle-1 {
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, #3b82f6 0%, transparent 70%);
  top: -200px;
  right: -100px;
  animation: float 20s ease-in-out infinite;
}

.bg-circle-2 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, #8b5cf6 0%, transparent 70%);
  bottom: -150px;
  left: -100px;
  animation: float 25s ease-in-out infinite reverse;
}

.bg-circle-3 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, #ec4899 0%, transparent 70%);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: pulse 15s ease-in-out infinite;
}

.bg-grid {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    linear-gradient(#e2e8f0 1px, transparent 1px),
    linear-gradient(90deg, #e2e8f0 1px, transparent 1px);
  background-size: 50px 50px;
}

.login-container {
  display: flex;
  width: 100%;
  max-width: 1000px;
  background: #ffffff;
  border-radius: 24px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  position: relative;
  z-index: 1;
}

.brand-section {
  flex: 1;
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.brand-section::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  animation: rotate 30s linear infinite;
}

.brand-content {
  position: relative;
  z-index: 1;
  text-align: center;
}

.logo {
  width: 100px;
  height: 100px;
  margin: 0 auto 30px;
}

.logo svg {
  width: 100%;
  height: 100%;
}

.brand-title {
  font-size: 32px;
  font-weight: 700;
  color: #ffffff;
  margin-bottom: 12px;
  letter-spacing: -0.5px;
}

.brand-subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 50px;
  letter-spacing: 4px;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateX(5px);
}

.feature-icon {
  font-size: 24px;
}

.feature-item span:last-child {
  font-size: 14px;
  color: #ffffff;
}

.form-section {
  flex: 1;
  padding: 60px 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ffffff;
}

.form-card {
  width: 100%;
  max-width: 400px;
}

.tab-switcher {
  display: flex;
  position: relative;
  background: #f1f5f9;
  border-radius: 12px;
  padding: 4px;
  margin-bottom: 40px;
}

.tab-btn {
  flex: 1;
  padding: 12px;
  background: transparent;
  border: none;
  color: #64748b;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.3s ease;
  position: relative;
  z-index: 1;
}

.tab-btn.active {
  color: #ffffff;
}

.tab-indicator {
  position: absolute;
  top: 4px;
  left: 4px;
  width: calc(50% - 4px);
  height: calc(100% - 8px);
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
  border-radius: 8px;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.4);
}

.auth-form {
  animation: fadeIn 0.5s ease;
}

.form-header {
  margin-bottom: 30px;
}

.form-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 8px;
}

.form-header p {
  font-size: 14px;
  color: #64748b;
}

.input-group {
  margin-bottom: 24px;
}

.input-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 500;
  color: #334155;
  margin-bottom: 10px;
}

.input-icon {
  width: 18px;
  height: 18px;
  color: #94a3b8;
}

.input-field {
  width: 100%;
  padding: 14px 16px;
  font-size: 14px;
  color: #0f172a;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  transition: all 0.3s ease;
  outline: none;
}

.input-field::placeholder {
  color: #94a3b8;
}

.input-field:hover {
  border-color: #cbd5e1;
  background: #f1f5f9;
}

.input-field:focus {
  border-color: #3b82f6;
  background: #ffffff;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.password-input {
  position: relative;
}

.password-input .input-field {
  padding-right: 50px;
}

.toggle-password {
  position: absolute;
  right: 14px;
  top: 50%;
  transform: translateY(-50%);
  background: transparent;
  border: none;
  color: #94a3b8;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.3s ease;
}

.toggle-password:hover {
  color: #64748b;
}

.toggle-password svg {
  width: 20px;
  height: 20px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.checkbox-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 13px;
  color: #64748b;
}

.checkbox-wrapper input[type="checkbox"] {
  display: none;
}

.checkmark {
  width: 18px;
  height: 18px;
  border: 2px solid #cbd5e1;
  border-radius: 4px;
  position: relative;
  transition: all 0.3s ease;
}

.checkbox-wrapper input:checked + .checkmark {
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
  border-color: transparent;
}

.checkbox-wrapper input:checked + .checkmark::after {
  content: '';
  position: absolute;
  left: 5px;
  top: 2px;
  width: 5px;
  height: 9px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.checkbox-label {
  user-select: none;
}

.forgot-link {
  font-size: 13px;
  color: #3b82f6;
  text-decoration: none;
  transition: color 0.3s ease;
  font-weight: 500;
}

.forgot-link:hover {
  color: #2563eb;
}

.submit-btn {
  width: 100%;
  padding: 16px;
  font-size: 15px;
  font-weight: 600;
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
  color: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.4);
  position: relative;
  overflow: hidden;
}

.submit-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.submit-btn:hover::before {
  left: 100%;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(59, 130, 246, 0.5);
}

.submit-btn:active {
  transform: translateY(0);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.loading-spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

.alert {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border-radius: 10px;
  font-size: 13px;
  margin-top: 16px;
  animation: shake 0.5s ease;
}

.alert svg {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}

.alert-error {
  background: #fef2f2;
  border: 1px solid #fecaca;
  color: #dc2626;
}

.alert-success {
  background: #f0fdf4;
  border: 1px solid #bbf7d0;
  color: #16a34a;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(-20px, 20px);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: translate(-50%, -50%) scale(1);
  }
  50% {
    transform: translate(-50%, -50%) scale(1.1);
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes shake {
  0%, 100% {
    transform: translateX(0);
  }
  10%, 30%, 50%, 70%, 90% {
    transform: translateX(-4px);
  }
  20%, 40%, 60%, 80% {
    transform: translateX(4px);
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
