<template>
  <div class="certificate-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14 2 14 8 20 8"/>
            <line x1="16" y1="13" x2="8" y2="13"/>
            <line x1="16" y1="17" x2="8" y2="17"/>
            <polyline points="10 9 9 9 8 9"/>
          </svg>
        </div>
        <div class="header-text">
          <h1>证书管理</h1>
          <p>查看和管理所有数字证书，监控证书状态</p>
        </div>
      </div>
      <div class="header-stats">
        <div class="stat-card">
          <div class="stat-content">
            <span class="stat-number">{{ total }}</span>
            <span class="stat-label">总证书</span>
          </div>
          <div class="stat-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
              <line x1="9" y1="9" x2="15" y2="9"/>
              <line x1="9" y1="15" x2="15" y2="15"/>
              <path d="M12 3v18"/>
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="search-section">
        <div class="search-box">
          <div class="search-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8"/>
              <path d="m21 21-4.35-4.35"/>
            </svg>
          </div>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="搜索用户名或证书序列号..."
            @keyup.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch">
            <span>搜索</span>
          </button>
        </div>
        <div class="filter-tags">
          <button 
            v-for="filter in filters" 
            :key="filter.value"
            :class="['filter-tag', { active: activeFilter === filter.value }]"
            @click="activeFilter = filter.value; handleSearch()"
          >
            {{ filter.label }}
          </button>
        </div>
      </div>
      <div class="action-section">
        <button @click="exportData" class="btn-export">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
            <polyline points="7 10 12 15 17 10"/>
            <line x1="12" y1="15" x2="12" y2="3"/>
          </svg>
          <span>导出数据</span>
        </button>
      </div>
    </div>

    <!-- 证书卡片网格 -->
    <div class="certificates-grid">
      <div 
        v-for="cert in certificates" 
        :key="cert.serialNumber"
        class="certificate-card"
        :class="{ revoked: cert.state === '已撤销' }"
      >
        <div class="card-glow"></div>
        <div class="card-header">
          <div class="cert-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
              <polyline points="14 2 14 8 20 8"/>
              <line x1="16" y1="13" x2="8" y2="13"/>
              <line x1="16" y1="17" x2="8" y2="17"/>
              <polyline points="10 9 9 9 8 9"/>
            </svg>
          </div>
          <div class="cert-status" :class="cert.state === '使用中' ? 'active' : 'revoked'">
            <span class="status-dot"></span>
            {{ cert.state }}
          </div>
        </div>
        
        <div class="card-body">
          <div class="cert-info">
            <div class="info-item">
              <span class="info-label">序列号</span>
              <span class="info-value serial">{{ cert.serialNumber }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">组织机构</span>
              <span class="info-value">{{ cert.organization }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">用户名</span>
              <span class="info-value user">
                <span class="user-avatar">{{ cert.username.charAt(0).toUpperCase() }}</span>
                {{ cert.username }}
              </span>
            </div>
          </div>
          
          <div class="cert-validity">
            <div class="validity-header">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
              <span>有效期</span>
            </div>
            <div class="validity-range">
              <div class="validity-item">
                <span class="validity-label">开始</span>
                <span class="validity-date">{{ formatDate(cert.startTime) }}</span>
              </div>
              <div class="validity-line"></div>
              <div class="validity-item">
                <span class="validity-label">结束</span>
                <span class="validity-date">{{ formatDate(cert.endTime) }}</span>
              </div>
            </div>
            <div class="validity-progress">
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: getValidityPercentage(cert) + '%' }"></div>
              </div>
              <span class="progress-text">{{ getDaysRemaining(cert) }} 天剩余</span>
            </div>
          </div>
        </div>
        
        <div class="card-footer">
          <button class="btn-action view" @click="viewCertificate(cert)">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
              <circle cx="12" cy="12" r="3"/>
            </svg>
            <span>查看详情</span>
          </button>
          <button 
            v-if="cert.state === '使用中'" 
            class="btn-action revoke"
            @click="revokeCertificate(cert)"
          >
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="15" y1="9" x2="9" y2="15"/>
              <line x1="9" y1="9" x2="15" y2="15"/>
            </svg>
            <span>撤销</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="certificates.length === 0" class="empty-state">
      <div class="empty-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
          <line x1="9" y1="9" x2="15" y2="9"/>
          <line x1="9" y1="15" x2="15" y2="15"/>
          <path d="M12 3v18"/>
        </svg>
      </div>
      <p>暂无证书数据</p>
      <span>尝试调整搜索条件</span>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="certificates.length > 0">
      <div class="pagination-info">
        <span>显示 {{ (page - 1) * pageSize + 1 }} - {{ Math.min(page * pageSize, total) }} 条，共 {{ total }} 条</span>
      </div>
      <div class="pagination">
        <button @click="prevPage" :disabled="page === 1" class="page-btn">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="15 18 9 12 15 6"/>
          </svg>
        </button>
        <div class="page-numbers">
          <button 
            v-for="p in visiblePages" 
            :key="p"
            :class="['page-number', { active: page === p }]"
            @click="goToPage(p)"
          >
            {{ p }}
          </button>
        </div>
        <button @click="nextPage" :disabled="page >= totalPages" class="page-btn">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- 证书详情弹窗 -->
    <Transition name="modal">
      <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
        <div class="modal-content" v-if="selectedCert">
          <div class="modal-header">
            <div class="modal-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                <polyline points="14 2 14 8 20 8"/>
              </svg>
            </div>
            <h2>证书详情</h2>
            <button class="modal-close" @click="showModal = false">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>
          <div class="modal-body">
            <div class="cert-detail-header">
              <div class="cert-status-large" :class="selectedCert.state === '使用中' ? 'active' : 'revoked'">
                <span class="status-dot"></span>
                {{ selectedCert.state }}
              </div>
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
                <span class="detail-label">所属用户</span>
                <span class="detail-value">{{ selectedCert.username }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">有效期开始</span>
                <span class="detail-value">{{ formatDateTime(selectedCert.startTime) }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">有效期结束</span>
                <span class="detail-value">{{ formatDateTime(selectedCert.endTime) }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">剩余天数</span>
                <span class="detail-value">{{ getDaysRemaining(selectedCert) }} 天</span>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button @click="showModal = false" class="btn-secondary">关闭</button>
            <button 
              v-if="selectedCert.state === '使用中'" 
              class="btn-danger"
              @click="revokeCertificate(selectedCert); showModal = false"
            >
              撤销证书
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script>
import api from '../../api'

export default {
  name: 'CertificateManagement',
  data() {
    return {
      certificates: [],
      searchQuery: '',
      activeFilter: 'all',
      page: 1,
      pageSize: 9,
      total: 0,
      totalPages: 0,
      showModal: false,
      selectedCert: null,
      filters: [
        { label: '全部', value: 'all' },
        { label: '有效', value: 'active' },
        { label: '已撤销', value: 'revoked' }
      ]
    }
  },
  computed: {
    visiblePages() {
      const pages = []
      const maxVisible = 5
      let start = Math.max(1, this.page - Math.floor(maxVisible / 2))
      let end = Math.min(this.totalPages, start + maxVisible - 1)
      
      if (end - start < maxVisible - 1) {
        start = Math.max(1, end - maxVisible + 1)
      }
      
      for (let i = start; i <= end; i++) {
        pages.push(i)
      }
      return pages
    }
  },
  async mounted() {
    await this.loadCertificates()
  },
  methods: {
    async loadCertificates() {
      try {
        const params = {
          page: this.page,
          pageSize: this.pageSize,
          username: this.searchQuery || undefined
        }
        const response = await api.admin.getCertificates(params)
        if (response.code === 200) {
          this.certificates = response.data.records
          this.total = response.data.total
          this.totalPages = response.data.totalPages
        }
      } catch (error) {
        console.error('加载证书失败', error)
      }
    },
    handleSearch() {
      this.page = 1
      this.loadCertificates()
    },
    goToPage(p) {
      this.page = p
      this.loadCertificates()
    },
    prevPage() {
      if (this.page > 1) {
        this.page--
        this.loadCertificates()
      }
    },
    nextPage() {
      if (this.page < this.totalPages) {
        this.page++
        this.loadCertificates()
      }
    },
    viewCertificate(cert) {
      this.selectedCert = cert
      this.showModal = true
    },
    async revokeCertificate(cert) {
      if (!confirm(`确定要撤销证书 "${cert.serialNumber}" 吗？此操作不可恢复！`)) {
        return
      }
      try {
        const response = await api.admin.revokeCertificate(cert.serialNumber)
        if (response.code === 200) {
          alert('证书已撤销')
          await this.loadCertificates()
        } else {
          alert(response.message || '撤销失败')
        }
      } catch (error) {
        console.error('撤销证书失败', error)
        alert('撤销失败')
      }
    },
    exportData() {
      const csv = ['序列号,组织机构,用户名,状态,有效期开始,有效期结束'].concat(
        this.certificates.map(c => `${c.serialNumber},${c.organization},${c.username},${c.state},${c.startTime},${c.endTime}`)
      ).join('\n')
      const blob = new Blob(['\ufeff' + csv], { type: 'text/csv' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `certificates_${new Date().toISOString().split('T')[0]}.csv`
      link.click()
    },
    // 解析中文日期格式
    parseChineseDate(dateStr) {
      if (!dateStr) return null
      const match = dateStr.match(/(\d{4}) 年 (\d{2}) 月 (\d{2}) 日 (\d{2}):(\d{2}):(\d{2})/)
      if (match) {
        const [, year, month, day, hour, minute, second] = match
        return new Date(year, parseInt(month) - 1, parseInt(day), parseInt(hour), parseInt(minute), parseInt(second))
      }
      const date = new Date(dateStr)
      return isNaN(date.getTime()) ? null : date
    },
    formatDate(dateStr) {
      if (!dateStr) return '-'
      const date = this.parseChineseDate(dateStr)
      if (!date) return '-'
      return date.toLocaleDateString('zh-CN')
    },
    formatDateTime(dateStr) {
      if (!dateStr) return '-'
      const date = this.parseChineseDate(dateStr)
      if (!date) return '-'
      return date.toLocaleString('zh-CN')
    },
    getDaysRemaining(cert) {
      const end = this.parseChineseDate(cert.endTime)
      if (!end) return 0
      const now = new Date()
      const diff = end - now
      return Math.max(0, Math.ceil(diff / (1000 * 60 * 60 * 24)))
    },
    getValidityPercentage(cert) {
      const start = this.parseChineseDate(cert.startTime)
      const end = this.parseChineseDate(cert.endTime)
      if (!start || !end) return 0
      const now = new Date().getTime()
      const total = end.getTime() - start.getTime()
      const elapsed = now - start.getTime()
      return Math.max(0, Math.min(100, (elapsed / total) * 100))
    }
  }
}
</script>

<style scoped>
.certificate-management {
  padding: 0;
  background: var(--ca-bg-secondary);
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 2px solid var(--ca-border);
}

.header-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  width: 72px;
  height: 72px;
  border-radius: var(--ca-radius-xl);
  background: var(--ca-gradient-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 8px 24px rgba(37, 99, 235, 0.3);
  animation: float 4s cubic-bezier(0.4, 0, 0.2, 1) infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

.header-icon svg {
  width: 40px;
  height: 40px;
}

.header-text h1 {
  font-size: 24px;
  font-weight: 700;
  color: var(--ca-text-primary);
  margin-bottom: 4px;
}

.header-text p {
  font-size: 14px;
  color: var(--ca-text-secondary);
}

.header-stats {
  display: flex;
  gap: 12px;
}

.stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 24px;
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-lg);
  box-shadow: var(--ca-shadow-sm);
  transition: all var(--ca-transition-normal);
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--ca-shadow-md);
}

.stat-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  color: var(--ca-text-primary);
  line-height: 1;
}

.stat-label {
  font-size: 12px;
  color: var(--ca-text-tertiary);
  margin-top: 4px;
}

.stat-icon {
  width: 32px;
  height: 32px;
  margin-top: 8px;
  color: var(--ca-primary);
}

.stat-icon svg {
  width: 100%;
  height: 100%;
}

/* 工具栏 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 24px;
  gap: 16px;
}

.search-section {
  flex: 1;
}

.search-box {
  position: relative;
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.search-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--ca-text-tertiary);
  pointer-events: none;
}

.search-icon svg {
  width: 20px;
  height: 20px;
}

.search-box input {
  flex: 1;
  max-width: 400px;
  padding: 12px 16px 12px 48px;
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-lg);
  color: var(--ca-text-primary);
  font-size: 14px;
  transition: all var(--ca-transition-normal);
  box-shadow: var(--ca-shadow-sm);
}

.search-box input:focus {
  outline: none;
  border-color: var(--ca-primary);
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.search-box input::placeholder {
  color: var(--ca-text-tertiary);
}

.search-btn {
  padding: 12px 24px;
  background: var(--ca-gradient-primary);
  border: none;
  border-radius: var(--ca-radius-lg);
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--ca-transition-normal);
  box-shadow: 0 4px 15px rgba(37, 99, 235, 0.3);
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(37, 99, 235, 0.4);
}

.filter-tags {
  display: flex;
  gap: 8px;
}

.filter-tag {
  padding: 8px 16px;
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-md);
  color: var(--ca-text-secondary);
  font-size: 13px;
  cursor: pointer;
  transition: all var(--ca-transition-normal);
}

.filter-tag:hover {
  background: var(--ca-bg-hover);
  border-color: var(--ca-border-hover);
  color: var(--ca-primary);
}

.filter-tag.active {
  background: var(--ca-gradient-primary);
  border-color: var(--ca-primary);
  color: white;
}

.action-section {
  display: flex;
  gap: 12px;
}

.btn-export {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-lg);
  color: var(--ca-text-secondary);
  font-size: 14px;
  cursor: pointer;
  transition: all var(--ca-transition-normal);
  box-shadow: var(--ca-shadow-sm);
}

.btn-export:hover {
  background: var(--ca-bg-hover);
  border-color: var(--ca-primary);
  color: var(--ca-primary);
  transform: translateY(-2px);
}

.btn-export svg {
  width: 18px;
  height: 18px;
}

/* 证书卡片网格 */
.certificates-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.certificate-card {
  position: relative;
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-2xl);
  padding: 24px;
  overflow: hidden;
  transition: all var(--ca-transition-normal);
  box-shadow: var(--ca-shadow-sm);
}

.certificate-card:hover {
  transform: translateY(-4px);
  border-color: var(--ca-primary);
  box-shadow: var(--ca-shadow-xl);
}

.certificate-card.revoked {
  border-left: 4px solid var(--ca-error);
}

.card-glow {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: var(--ca-gradient-primary);
  opacity: 0;
  transition: opacity var(--ca-transition-normal);
}

.certificate-card:hover .card-glow {
  opacity: 1;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.cert-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--ca-radius-lg);
  background: var(--ca-bg-tertiary);
  border: 1px solid var(--ca-border);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--ca-primary);
}

.cert-icon svg {
  width: 24px;
  height: 24px;
}

.cert-status {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: var(--ca-radius-full);
  font-size: 12px;
  font-weight: 600;
}

.cert-status.active {
  background: rgba(5, 150, 105, 0.1);
  color: var(--ca-success);
}

.cert-status.revoked {
  background: rgba(220, 38, 38, 0.1);
  color: var(--ca-error);
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: var(--ca-radius-full);
  background: currentColor;
  animation: pulse-dot 2s ease-in-out infinite;
}

@keyframes pulse-dot {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.card-body {
  margin-bottom: 20px;
}

.cert-info {
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid var(--ca-border);
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 13px;
  color: var(--ca-text-tertiary);
}

.info-value {
  font-size: 14px;
  color: var(--ca-text-primary);
  font-weight: 500;
}

.info-value.serial {
  font-family: var(--ca-font-mono);
  font-size: 12px;
  background: var(--ca-bg-tertiary);
  padding: 4px 10px;
  border-radius: var(--ca-radius-sm);
  border: 1px solid var(--ca-border);
}

.info-value.user {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-avatar {
  width: 24px;
  height: 24px;
  border-radius: var(--ca-radius-sm);
  background: var(--ca-gradient-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  color: white;
}

/* 有效期 */
.cert-validity {
  background: var(--ca-bg-tertiary);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-lg);
  padding: 16px;
}

.validity-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  color: var(--ca-text-tertiary);
  font-size: 13px;
}

.validity-header svg {
  width: 16px;
  height: 16px;
}

.validity-range {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.validity-item {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.validity-label {
  font-size: 11px;
  color: var(--ca-text-muted);
}

.validity-date {
  font-size: 13px;
  color: var(--ca-text-primary);
  font-weight: 500;
}

.validity-line {
  flex: 1;
  height: 1px;
  background: var(--ca-border);
  position: relative;
}

.validity-line::after {
  content: '';
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-left: 5px solid var(--ca-border);
  border-top: 3px solid transparent;
  border-bottom: 3px solid transparent;
}

.validity-progress {
  display: flex;
  align-items: center;
  gap: 10px;
}

.progress-bar {
  flex: 1;
  height: 4px;
  background: var(--ca-border);
  border-radius: var(--ca-radius-sm);
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: var(--ca-gradient-success);
  border-radius: var(--ca-radius-sm);
  transition: width var(--ca-transition-normal);
}

.progress-text {
  font-size: 11px;
  color: var(--ca-text-tertiary);
  white-space: nowrap;
}

/* 卡片底部 */
.card-footer {
  display: flex;
  gap: 10px;
}

.btn-action {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 10px;
  border-radius: var(--ca-radius-md);
  border: none;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--ca-transition-normal);
}

.btn-action svg {
  width: 16px;
  height: 16px;
}

.btn-action.view {
  background: rgba(37, 99, 235, 0.1);
  color: var(--ca-primary);
}

.btn-action.view:hover {
  background: rgba(37, 99, 235, 0.2);
  color: var(--ca-primary-dark);
}

.btn-action.revoke {
  background: rgba(220, 38, 38, 0.1);
  color: var(--ca-error);
}

.btn-action.revoke:hover {
  background: rgba(220, 38, 38, 0.2);
  color: var(--ca-error-dark);
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  color: var(--ca-text-tertiary);
}

.empty-icon {
  width: 80px;
  height: 80px;
  margin-bottom: 16px;
  color: var(--ca-text-muted);
}

.empty-icon svg {
  width: 100%;
  height: 100%;
}

.empty-state p {
  font-size: 16px;
  font-weight: 500;
  color: var(--ca-text-secondary);
  margin-bottom: 4px;
}

.empty-state span {
  font-size: 14px;
}

/* 分页 */
.pagination-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 8px;
}

.pagination-info {
  font-size: 14px;
  color: var(--ca-text-tertiary);
}

.pagination {
  display: flex;
  align-items: center;
  gap: 8px;
}

.page-btn {
  width: 40px;
  height: 40px;
  border-radius: var(--ca-radius-md);
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  color: var(--ca-text-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--ca-transition-normal);
  box-shadow: var(--ca-shadow-sm);
}

.page-btn:hover:not(:disabled) {
  background: var(--ca-bg-hover);
  border-color: var(--ca-border-hover);
  color: var(--ca-primary);
}

.page-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.page-btn svg {
  width: 18px;
  height: 18px;
}

.page-numbers {
  display: flex;
  gap: 4px;
}

.page-number {
  min-width: 40px;
  height: 40px;
  padding: 0 12px;
  border-radius: var(--ca-radius-md);
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  color: var(--ca-text-secondary);
  font-size: 14px;
  cursor: pointer;
  transition: all var(--ca-transition-normal);
}

.page-number:hover {
  background: var(--ca-bg-hover);
  border-color: var(--ca-border-hover);
  color: var(--ca-primary);
}

.page-number.active {
  background: var(--ca-gradient-primary);
  border-color: transparent;
  color: white;
}

/* 弹窗 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background: white;
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-2xl);
  width: 100%;
  max-width: 520px;
  overflow: hidden;
  box-shadow: var(--ca-shadow-xl);
}

.modal-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px;
  border-bottom: 1px solid var(--ca-border);
}

.modal-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--ca-radius-lg);
  background: var(--ca-bg-tertiary);
  border: 1px solid var(--ca-border);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--ca-primary);
}

.modal-icon svg {
  width: 24px;
  height: 24px;
}

.modal-header h2 {
  flex: 1;
  font-size: 20px;
  font-weight: 600;
  color: var(--ca-text-primary);
}

.modal-close {
  width: 36px;
  height: 36px;
  border-radius: var(--ca-radius-md);
  background: var(--ca-bg-tertiary);
  border: 1px solid var(--ca-border);
  color: var(--ca-text-tertiary);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--ca-transition-normal);
}

.modal-close:hover {
  background: var(--ca-bg-hover);
  color: var(--ca-text-primary);
  border-color: var(--ca-border-hover);
}

.modal-close svg {
  width: 18px;
  height: 18px;
}

.modal-body {
  padding: 24px;
}

.cert-detail-header {
  margin-bottom: 20px;
}

.cert-status-large {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: var(--ca-radius-full);
  font-size: 14px;
  font-weight: 600;
}

.cert-status-large.active {
  background: rgba(5, 150, 105, 0.1);
  color: var(--ca-success);
}

.cert-status-large.revoked {
  background: rgba(220, 38, 38, 0.1);
  color: var(--ca-error);
}

.detail-grid {
  display: grid;
  gap: 12px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: var(--ca-bg-tertiary);
  border-radius: var(--ca-radius-lg);
  border: 1px solid var(--ca-border);
}

.detail-label {
  font-size: 14px;
  color: var(--ca-text-tertiary);
}

.detail-value {
  font-size: 14px;
  color: var(--ca-text-primary);
  font-weight: 500;
}

.modal-footer {
  display: flex;
  gap: 12px;
  padding: 0 24px 24px;
}

.btn-secondary {
  flex: 1;
  padding: 14px;
  background: var(--ca-bg-tertiary);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-lg);
  color: var(--ca-text-secondary);
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--ca-transition-normal);
}

.btn-secondary:hover {
  background: var(--ca-bg-hover);
  border-color: var(--ca-border-hover);
  color: var(--ca-text-primary);
}

.btn-danger {
  flex: 1;
  padding: 14px;
  background: var(--ca-gradient-error);
  border: none;
  border-radius: var(--ca-radius-lg);
  color: white;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--ca-transition-normal);
  box-shadow: 0 4px 15px rgba(220, 38, 38, 0.3);
}

.btn-danger:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(220, 38, 38, 0.4);
}

/* 弹窗动画 */
.modal-enter-active,
.modal-leave-active {
  transition: all var(--ca-transition-normal);
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-content,
.modal-leave-to .modal-content {
  transform: scale(0.95);
  opacity: 0;
}

/* 响应式 */
@media (max-width: 1200px) {
  .certificates-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 16px;
  }
  
  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-box input {
    max-width: none;
  }
  
  .certificates-grid {
    grid-template-columns: 1fr;
  }
  
  .pagination-wrapper {
    flex-direction: column;
    gap: 16px;
    align-items: center;
  }
  
  .modal-footer {
    flex-direction: column;
  }
}
</style>
