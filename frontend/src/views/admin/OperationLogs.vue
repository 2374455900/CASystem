<template>
  <div class="operation-logs">
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
          <h1>操作日志</h1>
          <p>实时监控系统操作记录，追踪用户行为轨迹</p>
        </div>
      </div>
      <div class="header-stats">
        <div class="stat-card">
          <div class="stat-content">
            <span class="stat-number">{{ total }}</span>
            <span class="stat-label">总记录</span>
          </div>
          <div class="stat-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
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
          <input v-model="searchQuery" type="text" placeholder="搜索操作人、类型或描述..." @keyup.enter="handleSearch"/>
          <button class="search-btn" @click="handleSearch">
            <span>搜索</span>
          </button>
        </div>
        <div class="filter-tags">
          <button v-for="filter in typeFilters" :key="filter.value"
            :class="['filter-tag', { active: filterType === filter.value }]"
            @click="filterType = filter.value; handleSearch()">
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

    <!-- 时间线 -->
    <div class="timeline-container">
      <div v-if="logs.length === 0" class="empty-state">
        <div class="empty-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <circle cx="12" cy="12" r="10"/>
            <path d="M12 6v6l4 2"/>
          </svg>
        </div>
        <p>暂无操作日志</p>
        <span>系统操作记录将显示在这里</span>
      </div>

      <div v-else class="timeline">
        <div v-for="(group, date) in groupedLogs" :key="date" class="timeline-group">
          <div class="timeline-date">
            <div class="date-badge">
              <span class="date-day">{{ formatDay(date) }}</span>
              <span class="date-month">{{ formatMonth(date) }}</span>
            </div>
            <div class="date-line"></div>
          </div>
          
          <div class="timeline-items">
            <div v-for="log in group" :key="log.id" class="timeline-item"
              :class="{ success: log.result === 'SUCCESS', failed: log.result === 'FAILURE' }">
              <div class="item-marker">
                <div class="marker-dot"></div>
                <div class="marker-line"></div>
              </div>
              
              <div class="item-card">
                <div class="card-header">
                  <div class="operation-type">
                    <div class="type-icon" :class="getOperationTypeClass(log.operationType)">
                      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path v-for="(path, i) in getOperationIcon(log.operationType)" :key="i" :d="path" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </div>
                    <div class="type-info">
                      <span class="type-name">{{ log.operationType }}</span>
                      <span class="type-action">{{ log.targetType || '操作' }}</span>
                    </div>
                  </div>
                  <div class="operation-status" :class="log.result.toLowerCase()">
                    <span class="status-icon">
                      <svg v-if="log.result === 'SUCCESS'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="20 6 9 17 4 12"/>
                      </svg>
                      <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="12" cy="12" r="10"/>
                        <line x1="15" y1="9" x2="9" y2="15"/>
                        <line x1="9" y1="9" x2="15" y2="15"/>
                      </svg>
                    </span>
                    <span class="status-text">{{ log.result === 'SUCCESS' ? '成功' : '失败' }}</span>
                  </div>
                </div>
                
                <div class="card-body">
                  <p class="operation-desc">{{ log.details }}</p>
                </div>
                
                <div class="card-footer">
                  <div class="operator-info">
                    <div class="operator-avatar">{{ log.operator ? log.operator.charAt(0).toUpperCase() : 'A' }}</div>
                    <span class="operator-name">{{ log.operator || 'anonymous' }}</span>
                  </div>
                  <div class="operation-time">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <circle cx="12" cy="12" r="10"/>
                      <polyline points="12 6 12 12 16 14"/>
                    </svg>
                    <span>{{ formatTime(log.timestamp) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="logs.length > 0">
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
          <button v-for="p in visiblePages" :key="p"
            :class="['page-number', { active: page === p }]"
            @click="goToPage(p)">
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
  </div>
</template>

<script>
import api from '../../api'

export default {
  name: 'OperationLogs',
  data() {
    return {
      logs: [],
      searchQuery: '',
      filterType: '',
      filterStatus: '',
      page: 1,
      pageSize: 10,
      total: 0,
      totalPages: 0,
      typeFilters: [
        { label: '全部类型', value: '' },
        { label: '用户管理', value: '用户管理' },
        { label: '证书管理', value: '证书管理' },
        { label: '系统设置', value: '系统设置' }
      ],
      statusFilters: [
        { label: '全部状态', value: '' },
        { label: '成功', value: 'SUCCESS' },
        { label: '失败', value: 'FAILED' }
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
    },
    groupedLogs() {
      const groups = {}
      this.logs.forEach(log => {
        // AuditLog 使用 timestamp 字段
        const date = new Date(log.timestamp).toDateString()
        if (!groups[date]) {
          groups[date] = []
        }
        groups[date].push(log)
      })
      return groups
    }
  },
  watch: {
    searchQuery() {
      this.debounceSearch()
    }
  },
  async mounted() {
    await this.loadLogs()
  },
  methods: {
    debounceSearch() {
      clearTimeout(this.searchTimer)
      this.searchTimer = setTimeout(() => {
        this.handleSearch()
      }, 500)
    },
    handleSearch() {
      this.page = 1
      this.loadLogs()
    },
    async loadLogs() {
      try {
        const params = {
          page: this.page,
          pageSize: this.pageSize,
          operationType: this.filterType || undefined,
          status: this.filterStatus || undefined,
          search: this.searchQuery || undefined
        }
        const response = await api.admin.getLogs(params)
        if (response.code === 200) {
          this.logs = response.data.records
          this.total = response.data.total
          this.totalPages = response.data.totalPages
        }
      } catch (error) {
        console.error('加载日志失败', error)
      }
    },
    goToPage(p) {
      this.page = p
      this.loadLogs()
    },
    prevPage() {
      if (this.page > 1) {
        this.page--
        this.loadLogs()
      }
    },
    nextPage() {
      if (this.page < this.totalPages) {
        this.page++
        this.loadLogs()
      }
    },
    exportData() {
      const headers = ['时间', '操作人', 'IP地址', '类型', '操作对象', '操作结果', '详细描述']
      const rows = this.logs.map(l => [
        this.formatDateTime(l.timestamp),
        l.operator || 'anonymous',
        l.ipAddress || '-',
        l.operationType || '-',
        l.targetType ? `${l.targetType}${l.targetId ? '(' + l.targetId + ')' : ''}` : '-',
        l.result === 'SUCCESS' ? '成功' : (l.result === 'FAILURE' ? '失败' : (l.result || '-')),
        l.details || '-'
      ].map(field => `"${String(field).replace(/"/g, '""')}"`))

      const csv = [headers.join(','), ...rows.map(row => row.join(','))].join('\n')
      const blob = new Blob(['\ufeff' + csv], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `操作日志_${new Date().toISOString().split('T')[0]}.csv`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    },
    // 解析日期字符串（支持多种格式）
    parseDate(dateStr) {
      if (!dateStr) return null
      // 如果是标准 ISO 格式或数字时间戳，直接解析
      if (typeof dateStr === 'number' || dateStr.includes('T') || dateStr.includes('Z')) {
        const date = new Date(dateStr)
        return isNaN(date.getTime()) ? null : date
      }
      // 尝试直接解析
      const date = new Date(dateStr)
      return isNaN(date.getTime()) ? null : date
    },
    formatDateTime(dateStr) {
      const date = this.parseDate(dateStr)
      if (!date) return '-'
      return date.toLocaleString('zh-CN')
    },
    formatDay(dateStr) {
      const date = this.parseDate(dateStr)
      if (!date) return 0
      return date.getDate()
    },
    formatMonth(dateStr) {
      const date = this.parseDate(dateStr)
      if (!date) return '未知'
      const months = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
      return months[date.getMonth()]
    },
    formatTime(dateStr) {
      const date = this.parseDate(dateStr)
      if (!date) return '-'
      return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit', second: '2-digit' })
    },
    getOperationTypeClass(type) {
      const classes = {
        '用户管理': 'user',
        '证书管理': 'cert',
        '系统设置': 'system'
      }
      return classes[type] || 'default'
    },
    getOperationIcon(type) {
      const icons = {
        '用户管理': ['M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2', 'M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM23 21v-2a4 4 0 0 0-3-3.87', 'M16 3.13a4 4 0 0 1 0 7.75'],
        '证书管理': ['M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z', 'M14 2v6h6', 'M16 13H8', 'M16 17H8', 'M10 9H8'],
        '系统设置': ['M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83']
      }
      return icons[type] || ['M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z', 'M14 2v6h6']
    }
  }
}
</script>

<style scoped>
.operation-logs {
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
  width: 40px;
  height: 40px;
  border-radius: var(--ca-radius-md);
  background: var(--ca-bg-hover);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--ca-primary);
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
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.search-box {
  display: flex;
  align-items: center;
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-lg);
  padding: 8px 12px;
  transition: all var(--ca-transition-normal);
}

.search-box:focus-within {
  border-color: var(--ca-primary);
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.search-icon {
  width: 20px;
  height: 20px;
  color: var(--ca-text-tertiary);
  margin-right: 8px;
}

.search-box input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 14px;
  color: var(--ca-text-primary);
  background: transparent;
}

.search-box input::placeholder {
  color: var(--ca-text-tertiary);
}

.search-btn {
  padding: 8px 16px;
  background: var(--ca-gradient-primary);
  color: white;
  border: none;
  border-radius: var(--ca-radius-md);
  font-size: 13px;
  font-weight: 500;
  transition: all var(--ca-transition-fast);
}

.search-btn:hover {
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.4);
  transform: translateY(-1px);
}

.filter-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.filter-tag {
  padding: 6px 14px;
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-full);
  color: var(--ca-text-secondary);
  font-size: 13px;
  cursor: pointer;
  transition: all var(--ca-transition-fast);
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
  box-shadow: var(--ca-shadow-md);
}

.btn-export svg {
  width: 18px;
  height: 18px;
}

/* 时间线容器 */
.timeline-container {
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-2xl);
  padding: 32px;
  margin-bottom: 24px;
  min-height: 400px;
  box-shadow: var(--ca-shadow-sm);
}

.timeline {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.timeline-group {
  display: flex;
  gap: 24px;
}

.timeline-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.date-badge {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  background: var(--ca-gradient-primary);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-xl);
  color: white;
  box-shadow: var(--ca-shadow-sm);
}

.date-day {
  font-size: 24px;
  font-weight: 700;
  line-height: 1;
}

.date-month {
  font-size: 11px;
  text-transform: uppercase;
  opacity: 0.9;
}

.date-line {
  flex: 1;
  width: 2px;
  background: var(--ca-border);
  min-height: 40px;
}

.timeline-items {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.timeline-item {
  display: flex;
  gap: 16px;
  position: relative;
  animation: slideIn 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.item-marker {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 20px;
}

.marker-dot {
  width: 12px;
  height: 12px;
  border-radius: var(--ca-radius-full);
  background: var(--ca-border);
  border: 2px solid var(--ca-border);
  transition: all var(--ca-transition-normal);
}

.timeline-item.success .marker-dot {
  background: var(--ca-success);
  border-color: var(--ca-success);
  box-shadow: 0 0 10px rgba(5, 150, 105, 0.4);
}

.timeline-item.failed .marker-dot {
  background: var(--ca-error);
  border-color: var(--ca-error);
  box-shadow: 0 0 10px rgba(220, 38, 38, 0.4);
}

.marker-line {
  flex: 1;
  width: 2px;
  background: var(--ca-border);
  margin-top: 8px;
}

.item-card {
  flex: 1;
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  border-radius: var(--ca-radius-xl);
  padding: 20px;
  transition: all var(--ca-transition-normal);
  box-shadow: var(--ca-shadow-sm);
}

.item-card:hover {
  background: var(--ca-bg-card);
  border-color: var(--ca-primary);
  transform: translateX(4px);
  box-shadow: var(--ca-shadow-md);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.operation-type {
  display: flex;
  align-items: center;
  gap: 12px;
}

.type-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.type-icon.user {
  background: rgba(37, 99, 235, 0.1);
  color: var(--ca-primary);
}

.type-icon.cert {
  background: rgba(139, 92, 246, 0.1);
  color: #a78bfa;
}

.type-icon.system {
  background: rgba(245, 158, 11, 0.1);
  color: var(--ca-warning);
}

.type-icon.default {
  background: rgba(100, 116, 139, 0.1);
  color: var(--ca-text-tertiary);
}

.type-icon svg {
  width: 20px;
  height: 20px;
}

.type-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.type-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--ca-text-primary);
}

.type-action {
  font-size: 12px;
  color: var(--ca-text-tertiary);
}

.operation-status {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: var(--ca-radius-full);
  font-size: 12px;
  font-weight: 500;
}

.operation-status.success {
  background: rgba(5, 150, 105, 0.1);
  color: var(--ca-success);
  border: 1px solid var(--ca-success);
}

.operation-status.failed {
  background: rgba(220, 38, 38, 0.1);
  color: var(--ca-error);
  border: 1px solid var(--ca-error);
}

.status-icon svg {
  width: 14px;
  height: 14px;
}

.card-body {
  margin-bottom: 16px;
}

.operation-desc {
  font-size: 14px;
  color: var(--ca-text-secondary);
  line-height: 1.5;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid var(--ca-border);
}

.operator-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.operator-avatar {
  width: 28px;
  height: 28px;
  border-radius: var(--ca-radius-md);
  background: var(--ca-gradient-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  color: white;
}

.operator-name {
  font-size: 13px;
  color: var(--ca-text-secondary);
}

.operation-time {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--ca-text-tertiary);
}

.operation-time svg {
  width: 14px;
  height: 14px;
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
  color: var(--ca-border);
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
  transition: all var(--ca-transition-fast);
}

.page-btn:hover:not(:disabled) {
  background: var(--ca-bg-hover);
  border-color: var(--ca-border-hover);
  color: var(--ca-primary);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  gap: 4px;
}

.page-number {
  width: 36px;
  height: 36px;
  border-radius: var(--ca-radius-md);
  background: var(--ca-bg-card);
  border: 1px solid var(--ca-border);
  color: var(--ca-text-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  cursor: pointer;
  transition: all var(--ca-transition-fast);
}

.page-number:hover {
  background: var(--ca-bg-hover);
  border-color: var(--ca-border-hover);
  color: var(--ca-primary);
}

.page-number.active {
  background: var(--ca-gradient-primary);
  border-color: var(--ca-primary);
  color: white;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }
  
  .toolbar {
    flex-direction: column;
  }
  
  .search-section {
    width: 100%;
  }
  
  .header-stats {
    width: 100%;
  }
  
  .stat-card {
    flex: 1;
  }
}
</style>
