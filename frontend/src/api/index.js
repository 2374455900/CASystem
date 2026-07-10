import axios from 'axios'
import store from '../store'

// 根据环境配置 API 基础 URL
const getBaseURL = () => {
  // 开发环境使用代理，生产环境使用完整 URL
  if (import.meta.env.DEV) {
    return '/api'
  }
  // 生产环境可以从环境变量读取，或使用默认地址
  return import.meta.env.VITE_API_BASE_URL || '/api'
}

const apiClient = axios.create({
  baseURL: getBaseURL(),
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  },
  // 允许跨域携带凭证
  withCredentials: true
})

apiClient.interceptors.request.use(
  config => {
    const token = store.state.token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

apiClient.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response) {
      const { status, data } = error.response

      if (status === 401) {
        store.commit('CLEAR_AUTH')
        window.location.href = '/login'
      }

      return Promise.reject(data)
    }
    return Promise.reject(error)
  }
)

export default {
  auth: {
    login(email, password) {
      return apiClient.post('/auth/login', { email, password })
    },
    register(email, username, password) {
      return apiClient.post('/auth/register', { email, username, password })
    },
    getCurrentUser() {
      return apiClient.get('/auth/me')
    },
    logout() {
      return apiClient.post('/auth/logout')
    }
  },

  certificates: {
    apply(data) {
      return apiClient.post('/certificates/apply', data)
    },
    revoke(data) {
      return apiClient.post('/certificates/revoke', data)
    },
    getMyCertificates() {
      return apiClient.get('/certificates/my')
    },
    getCertificate(serialNumber) {
      return apiClient.get(`/certificates/${serialNumber}`)
    },
    getDownloadInfo(serialNumber) {
      return apiClient.get(`/certificates/download/${serialNumber}`)
    },
    downloadUserPrivateKey(serialNumber) {
      return apiClient.get(`/certificates/download-private-key/${serialNumber}`)
    },
    generateSignature(data) {
      return apiClient.post('/certificates/generate-signature', data)
    },
    verifySignature(data) {
      return apiClient.post('/certificates/verify-signature', data, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
    }
  },

  captcha: {
    generate() {
      return apiClient.get('/captcha/generate')
    }
  },

  public: {
    getPublicKey() {
      return apiClient.get('/public/key')
    },
    health() {
      return apiClient.get('/public/health')
    }
  },

  admin: {
    getUsers(params) {
      return apiClient.get('/admin/users', { params })
    },
    getUserById(id) {
      return apiClient.get(`/admin/users/${id}`)
    },
    deleteUser(id) {
      return apiClient.delete(`/admin/users/${id}`)
    },
    getCertificates(params) {
      return apiClient.get('/admin/certificates', { params })
    },
    getUserCertificates(username) {
      return apiClient.get(`/admin/certificates/user/${username}`)
    },
    getStatistics() {
      return apiClient.get('/admin/statistics')
    },
    getLogs(params) {
      // 使用 audit-logs 接口（AOP 自动记录）
      return apiClient.get('/admin/audit-logs', { params })
    }
  }
}
