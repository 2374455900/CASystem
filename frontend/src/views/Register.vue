<template>
  <div class="register-container">
    <div class="register-box">
      <div class="register-header">
        <h1>CA证书管理系统</h1>
        <p>创建新账户</p>
      </div>

      <form @submit.prevent="handleSubmit" class="register-form">
        <div class="form-group">
          <input
            v-model="form.email"
            type="email"
            placeholder="邮箱"
            required
          />
        </div>
        <div class="form-group">
          <input
            v-model="form.username"
            type="text"
            placeholder="用户名"
            required
          />
        </div>
        <div class="form-group">
          <input
            v-model="form.password"
            type="password"
            placeholder="密码 (至少6位，包含字母和数字)"
            required
          />
        </div>
        <div class="form-group">
          <input
            v-model="form.confirmPassword"
            type="password"
            placeholder="确认密码"
            required
          />
        </div>
        <button type="submit" class="btn-primary" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>

        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <p v-if="successMessage" class="success-message">{{ successMessage }}</p>

        <div class="form-footer">
          <p>已有账户? <router-link to="/login">立即登录</router-link></p>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import api from '../api'

export default {
  name: 'Register',
  data() {
    return {
      form: {
        email: '',
        username: '',
        password: '',
        confirmPassword: ''
      },
      loading: false,
      errorMessage: '',
      successMessage: ''
    }
  },
  methods: {
    async handleSubmit() {
      if (this.form.password !== this.form.confirmPassword) {
        this.errorMessage = '两次输入的密码不一致'
        return
      }

      this.loading = true
      this.errorMessage = ''
      this.successMessage = ''

      try {
        const response = await api.auth.register(
          this.form.email,
          this.form.username,
          this.form.password
        )

        if (response.code === 200) {
          this.successMessage = '注册成功！即将跳转到登录页面...'
          setTimeout(() => {
            this.$router.push('/login')
          }, 2000)
        } else {
          this.errorMessage = response.message
        }
      } catch (error) {
        this.errorMessage = error.message || '注册失败，请稍后重试'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.register-header p {
  color: #666;
  font-size: 14px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
}

.btn-primary {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: opacity 0.3s;
}

.btn-primary:hover {
  opacity: 0.9;
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error-message {
  color: #e74c3c;
  font-size: 14px;
  margin-top: 15px;
  text-align: center;
}

.success-message {
  color: #27ae60;
  font-size: 14px;
  margin-top: 15px;
  text-align: center;
}

.form-footer {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #666;
}

.form-footer a {
  color: #667eea;
  text-decoration: none;
}
</style>
