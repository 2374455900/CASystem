<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <aside class="admin-sidebar">
      <div class="sidebar-header">
        <div class="sidebar-logo">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
            <circle cx="8.5" cy="8.5" r="1.5"/>
            <polyline points="21 15 16 10 5 21"/>
          </svg>
        </div>
        <div class="sidebar-brand">
          <h2>CA 系统</h2>
          <p>管理后台</p>
        </div>
      </div>

      <nav class="sidebar-nav">
        <router-link to="/admin/dashboard" class="nav-item" :class="{ active: $route.name === 'AdminDashboard' }">
          <div class="nav-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="3" width="7" height="7"/>
              <rect x="14" y="3" width="7" height="7"/>
              <rect x="14" y="14" width="7" height="7"/>
              <rect x="3" y="14" width="7" height="7"/>
            </svg>
          </div>
          <span>数据概览</span>
        </router-link>

        <router-link to="/admin/users" class="nav-item" :class="{ active: $route.name === 'AdminUsers' }">
          <div class="nav-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
              <circle cx="9" cy="7" r="4"/>
              <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
              <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
            </svg>
          </div>
          <span>用户管理</span>
        </router-link>

        <router-link to="/admin/certificates" class="nav-item" :class="{ active: $route.name === 'AdminCertificates' }">
          <div class="nav-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
              <polyline points="14 2 14 8 20 8"/>
              <line x1="16" y1="13" x2="8" y2="13"/>
              <line x1="16" y1="17" x2="8" y2="17"/>
              <polyline points="10 9 9 9 8 9"/>
            </svg>
          </div>
          <span>证书管理</span>
        </router-link>

        <router-link to="/admin/logs" class="nav-item" :class="{ active: $route.name === 'AdminLogs' }">
          <div class="nav-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
              <polyline points="14 2 14 8 20 8"/>
              <line x1="16" y1="13" x2="8" y2="13"/>
              <line x1="16" y1="17" x2="8" y2="17"/>
              <polyline points="10 9 9 9 8 9"/>
            </svg>
          </div>
          <span>操作日志</span>
        </router-link>
      </nav>

      <div class="sidebar-footer">
        <router-link to="/home" class="back-link">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="19" y1="12" x2="5" y2="12"/>
            <polyline points="12 19 5 12 12 5"/>
          </svg>
          <span>返回前台</span>
        </router-link>
      </div>
    </aside>

    <!-- 主内容区 -->
    <div class="admin-main">
      <header class="admin-header">
        <div class="header-title">
          <h1>{{ pageTitle }}</h1>
          <p class="header-subtitle">{{ pageSubtitle }}</p>
        </div>
        <div class="header-user">
          <div class="user-badge">
            <div class="user-avatar">
              {{ user?.username?.charAt(0)?.toUpperCase() || 'A' }}
            </div>
            <div class="user-info">
              <span class="user-name">{{ user?.username || 'Admin' }}</span>
              <span class="user-role">管理员</span>
            </div>
          </div>
          <button class="logout-btn" @click="logout" title="退出登录">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
              <polyline points="16 17 21 12 16 7"/>
              <line x1="21" y1="12" x2="9" y2="12"/>
            </svg>
          </button>
        </div>
      </header>

      <main class="admin-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminLayout',
  computed: {
    user() {
      return this.$store.state.user
    },
    pageTitle() {
      const titles = {
        'AdminDashboard': '数据概览',
        'AdminUsers': '用户管理',
        'AdminCertificates': '证书管理',
        'AdminLogs': '操作日志'
      }
      return titles[this.$route.name] || '管理后台'
    },
    pageSubtitle() {
      const subtitles = {
        'AdminDashboard': '实时监控系统运行状态',
        'AdminUsers': '管理系统用户信息和权限',
        'AdminCertificates': '管理所有数字证书',
        'AdminLogs': '查看系统操作记录'
      }
      return subtitles[this.$route.name] || ''
    }
  },
  methods: {
    logout() {
      this.$store.dispatch('logout')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: var(--ca-bg-secondary);
}

.admin-sidebar {
  width: 260px;
  background: var(--ca-bg-card);
  border-right: 1px solid var(--ca-border);
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 100;
  box-shadow: var(--ca-shadow-sm);
}

.sidebar-header {
  padding: var(--ca-space-xl) var(--ca-space-md);
  border-bottom: 1px solid var(--ca-border);
  display: flex;
  align-items: center;
  gap: var(--ca-space-md);
}

.sidebar-logo {
  width: 48px;
  height: 48px;
}

.sidebar-logo svg {
  width: 100%;
  height: 100%;
}

.sidebar-brand h2 {
  font-size: 18px;
  font-weight: 700;
  color: var(--ca-text-primary);
  margin-bottom: var(--ca-space-xs);
}

.sidebar-brand p {
  font-size: 12px;
  color: var(--ca-text-tertiary);
}

.sidebar-nav {
  flex: 1;
  padding: var(--ca-space-md) var(--ca-space-sm);
  display: flex;
  flex-direction: column;
  gap: var(--ca-space-xs);
}

.nav-item {
  display: flex;
  align-items: center;
  gap: var(--ca-space-md);
  padding: 12px var(--ca-space-md);
  color: var(--ca-text-secondary);
  text-decoration: none;
  border-radius: var(--ca-radius-md);
  transition: all var(--ca-transition-fast);
  font-size: 14px;
  font-weight: 500;
}

.nav-icon {
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-icon svg {
  width: 100%;
  height: 100%;
}

.nav-item:hover {
  color: var(--ca-primary);
  background: rgba(37, 99, 235, 0.08);
}

.nav-item.active {
  color: var(--ca-primary);
  background: rgba(37, 99, 235, 0.08);
  font-weight: 600;
  border-left: 3px solid var(--ca-primary);
}

.sidebar-footer {
  padding: var(--ca-space-md) var(--ca-space-sm);
  border-top: 1px solid var(--ca-border);
}

.back-link {
  display: flex;
  align-items: center;
  gap: var(--ca-space-sm);
  padding: 10px var(--ca-space-md);
  color: var(--ca-text-secondary);
  text-decoration: none;
  border-radius: var(--ca-radius-md);
  font-size: 14px;
  font-weight: 500;
  transition: all var(--ca-transition-fast);
}

.back-link svg {
  width: 18px;
  height: 18px;
}

.back-link:hover {
  color: var(--ca-primary);
  background: var(--ca-bg-hover);
}

.admin-main {
  flex: 1;
  margin-left: 260px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--ca-space-md) var(--ca-space-xl);
  background: var(--ca-bg-card);
  border-bottom: 1px solid var(--ca-border);
  position: sticky;
  top: 0;
  z-index: 99;
  box-shadow: var(--ca-shadow-sm);
}

.header-title h1 {
  font-size: 24px;
  font-weight: 700;
  color: var(--ca-text-primary);
  margin-bottom: var(--ca-space-xs);
}

.header-subtitle {
  font-size: 13px;
  color: var(--ca-text-secondary);
}

.header-user {
  display: flex;
  align-items: center;
  gap: var(--ca-space-md);
}

.user-badge {
  display: flex;
  align-items: center;
  gap: var(--ca-space-sm);
  padding: 8px 16px;
  background: var(--ca-bg-secondary);
  border-radius: var(--ca-radius-md);
  border: 1px solid var(--ca-border);
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--ca-gradient-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
  color: white;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.2);
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--ca-text-primary);
}

.user-role {
  font-size: 11px;
  color: var(--ca-primary);
  font-weight: 500;
}

.logout-btn {
  width: 40px;
  height: 40px;
  border-radius: var(--ca-radius-md);
  background: var(--ca-bg-secondary);
  border: 1px solid var(--ca-border);
  color: var(--ca-text-secondary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--ca-transition-fast);
}

.logout-btn:hover {
  background: var(--ca-bg-hover);
  border-color: var(--ca-border-hover);
  color: var(--ca-error);
}

.logout-btn svg {
  width: 18px;
  height: 18px;
}

.admin-content {
  flex: 1;
  padding: var(--ca-space-xl);
  overflow-y: auto;
  background: var(--ca-bg-secondary);
}

@media (max-width: 768px) {
  .admin-sidebar {
    transform: translateX(-100%);
    transition: transform 0.3s ease;
  }

  .admin-sidebar.open {
    transform: translateX(0);
  }

  .admin-main {
    margin-left: 0;
  }

  .admin-header {
    padding: var(--ca-space-md) var(--ca-space-lg);
  }

  .admin-content {
    padding: var(--ca-space-lg);
  }
}
</style>
