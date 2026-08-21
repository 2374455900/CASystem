<template>
    <div class="app-layout">
        <div class="bg-effects">
            <div class="gradient-orb orb-1"></div>
            <div class="gradient-orb orb-2"></div>
            <div class="gradient-orb orb-3"></div>
            <div class="grid-overlay"></div>
            <div class="floating-particles">
                <div v-for="i in 20" :key="i" class="particle" :style="getParticleStyle(i)"></div>
            </div>
        </div>

        <nav class="navbar">
            <div class="nav-brand">
                <div class="brand-icon">
                    <svg viewBox="0 0 48 48" fill="none">
                        <defs>
                            <linearGradient id="brandGrad" x1="0" y1="0" x2="48" y2="48">
                                <stop offset="0%" stop-color="var(--ca-primary)" />
                                <stop offset="100%" stop-color="#8b5cf6" />
                            </linearGradient>
                        </defs>
                        <rect x="8" y="8" width="32" height="32" rx="8" stroke="url(#brandGrad)" stroke-width="2" fill="none" />
                        <path d="M16 24L22 30L32 18" stroke="url(#brandGrad)" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                </div>
                <span class="brand-text">CA证书管理系统</span>
            </div>

            <div class="nav-links">
                <router-link to="/app/home" class="nav-link" :class="{ active: $route.path === '/app/home' }">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
                        <polyline points="9 22 9 12 15 12 15 22" />
                    </svg>
                    首页
                </router-link>
                <router-link to="/app/apply" class="nav-link" :class="{ active: $route.path === '/app/apply' }">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                        <line x1="12" y1="8" x2="12" y2="16" />
                        <line x1="8" y1="12" x2="16" y2="12" />
                    </svg>
                    申请证书
                </router-link>
                <router-link to="/app/certificates" class="nav-link" :class="{ active: $route.path === '/app/certificates' }">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                        <polyline points="14 2 14 8 20 8" />
                        <line x1="16" y1="13" x2="8" y2="13" />
                        <line x1="16" y1="17" x2="8" y2="17" />
                        <polyline points="10 9 9 9 8 9" />
                    </svg>
                    我的证书
                </router-link>
                <router-link to="/app/revoke" class="nav-link" :class="{ active: $route.path === '/app/revoke' }">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="12" cy="12" r="10" />
                        <line x1="15" y1="9" x2="9" y2="15" />
                        <line x1="9" y1="9" x2="15" y2="15" />
                    </svg>
                    撤销证书
                </router-link>
                <router-link to="/app/keys" class="nav-link" :class="{ active: $route.path === '/app/keys' }">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                        <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                    </svg>
                    密钥管理
                </router-link>
                <router-link to="/app/generate-signature" class="nav-link" :class="{ active: $route.path === '/app/generate-signature' }">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                    </svg>
                    签名生成
                </router-link>
                <router-link to="/app/verify" class="nav-link" :class="{ active: $route.path === '/app/verify' }">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="9 11 12 14 22 4" />
                        <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11" />
                    </svg>
                    签名验证
                </router-link>
            </div>

            <div class="nav-user">
                <router-link to="/app/profile" class="user-avatar" :style="{ background: getAvatarGradient(user?.id) }" title="个人中心">
                    {{ user?.username?.charAt(0).toUpperCase() }}
                </router-link>
                <button class="logout-btn" @click="handleLogout">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
                        <polyline points="16 17 21 12 16 7" />
                        <line x1="21" y1="12" x2="9" y2="12" />
                    </svg>
                </button>
            </div>
        </nav>

        <div class="page-content">
            <router-view />
        </div>
    </div>
</template>

<script>
import store from '../store'

export default {
    name: 'AppLayout',
    computed: {
        user() {
            return store.state.user
        }
    },
    methods: {
        getParticleStyle(i) {
            const left = ((i * 7 + 3) * 137.5) % 100
            const delay = (i * 0.7) % 5
            const size = 4 + (i % 3) * 3
            return {
                left: `${left}%`,
                animationDelay: `${delay}s`,
                width: `${size}px`,
                height: `${size}px`,
                opacity: 0.3 + (i % 5) * 0.1
            }
        },
        getAvatarGradient(id) {
            const colors = [
                'linear-gradient(135deg, var(--ca-primary) 0%, var(--ca-primary-light) 100%)',
                'linear-gradient(135deg, var(--ca-success) 0%, var(--ca-success-light) 100%)',
                'linear-gradient(135deg, var(--ca-warning) 0%, var(--ca-warning-light) 100%)',
                'linear-gradient(135deg, var(--ca-error) 0%, var(--ca-error-light) 100%)',
                'linear-gradient(135deg, var(--ca-info) 0%, var(--ca-info-light) 100%)',
                'linear-gradient(135deg, var(--ca-primary-dark) 0%, var(--ca-primary) 100%)',
                'linear-gradient(135deg, var(--ca-success-dark) 0%, var(--ca-success) 100%)',
                'linear-gradient(135deg, var(--ca-error-dark) 0%, var(--ca-error) 100%)'
            ]
            return colors[(id || 1) % colors.length]
        },
        async handleLogout() {
            try {
                const api = (await import('../api/index')).default
                const response = await api.auth.logout()
                console.log('Logout response:', response)
            } catch (error) {
                console.error('Logout error:', error)
            } finally {
                store.commit('CLEAR_AUTH')
                this.$router.push('/login')
            }
        }
    }
}
</script>

<style scoped>
.app-layout {
    min-height: 100vh;
    position: relative;
    overflow-x: hidden;
}

.bg-effects {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 0;
    pointer-events: none;
}

.gradient-orb {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    opacity: 0.15;
    animation: orbFloat 20s ease-in-out infinite;
}

.orb-1 {
    width: 600px;
    height: 600px;
    background: var(--gradient-primary);
    top: -10%;
    left: -5%;
    animation-delay: 0s;
}

.orb-2 {
    width: 500px;
    height: 500px;
    background: var(--gradient-accent);
    bottom: -5%;
    right: -5%;
    animation-delay: -7s;
}

.orb-3 {
    width: 400px;
    height: 400px;
    background: var(--gradient-accent);
    top: 40%;
    left: 50%;
    animation-delay: -14s;
}

.grid-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image:
        linear-gradient(rgba(var(--ca-primary), 0.03) 1px, transparent 1px),
        linear-gradient(90deg, rgba(var(--ca-primary), 0.03) 1px, transparent 1px);
    background-size: 60px 60px;
}

.floating-particles {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
}

.particle {
    position: absolute;
    background: var(--gradient-primary);
    border-radius: 50%;
    animation: particleFloat 15s ease-in-out infinite;
    bottom: -10px;
}

@keyframes orbFloat {
    0%, 100% { transform: translate(0, 0) scale(1); }
    33% { transform: translate(30px, -30px) scale(1.1); }
    66% { transform: translate(-20px, 20px) scale(0.9); }
}

@keyframes particleFloat {
    0% { transform: translateY(0) rotate(0deg); opacity: 0; }
    10% { opacity: 1; }
    90% { opacity: 1; }
    100% { transform: translateY(-100vh) rotate(720deg); opacity: 0; }
}

.navbar {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 100;
    background: rgba(255, 255, 255, 0.8);
    backdrop-filter: blur(20px);
    -webkit-backdrop-filter: blur(20px);
    border-bottom: 1px solid var(--ca-primary-light, rgba(59, 130, 246, 0.1));
    display: flex;
    align-items: center;
    padding: 0 24px;
    height: 64px;
}

.nav-brand {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-right: 32px;
    flex-shrink: 0;
}

.brand-icon svg {
    width: 36px;
    height: 36px;
}

.brand-text {
    font-size: 18px;
    font-weight: 700;
    background: var(--gradient-primary);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.nav-links {
    display: flex;
    align-items: center;
    gap: 4px;
    flex: 1;
    overflow-x: auto;
}

.nav-link {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 8px 14px;
    border-radius: 8px;
    color: var(--ca-text-tertiary);
    text-decoration: none;
    font-size: 14px;
    font-weight: 500;
    transition: all 0.18s cubic-bezier(0.4, 0, 0.2, 1);
    white-space: nowrap;
}

.nav-link svg {
    width: 18px;
    height: 18px;
    flex-shrink: 0;
    transition: transform 0.18s cubic-bezier(0.4, 0, 0.2, 1);
}

.nav-link:hover {
    background: rgba(59, 130, 246, 0.08);
    color: var(--ca-primary);
}

.nav-link:hover svg {
    transform: translateX(2px);
}

.nav-link.active {
    background: rgba(59, 130, 246, 0.1);
    color: var(--ca-primary);
}

.nav-link.active svg {
    transform: scale(1.1);
}

.nav-user {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-left: 16px;
    flex-shrink: 0;
}

.user-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: transform 0.2s;
    text-decoration: none;
}

.user-avatar:hover {
    transform: scale(1.1);
}

.logout-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 8px;
    border-radius: 8px;
    color: var(--ca-text-tertiary);
    transition: all 0.2s;
    display: flex;
    align-items: center;
}

.logout-btn svg {
    width: 20px;
    height: 20px;
}

.logout-btn:hover {
    background: rgba(239, 68, 68, 0.1);
    color: var(--ca-error);
}

.page-content {
    position: relative;
    z-index: 1;
    padding-top: 80px;
    min-height: 100vh;
}

.bg-effects:hover .gradient-orb,
.bg-effects:hover .particle {
    animation-play-state: paused;
}

@media (prefers-reduced-motion: reduce) {
    .gradient-orb,
    .particle {
        animation: none !important;
    }
}
</style>
