import { createRouter, createWebHistory } from 'vue-router'
import store from '../store'

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
        meta: { requiresAuth: false }
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue'),
        meta: { requiresAuth: false }
    },
    {
        path: '/home',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/apply',
        name: 'Apply',
        component: () => import('../views/Apply.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/certificates',
        name: 'Certificates',
        component: () => import('../views/Certificates.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/revoke',
        name: 'Revoke',
        component: () => import('../views/Revoke.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/keys',
        name: 'Keys',
        component: () => import('../views/KeyManagement.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/generate-signature',
        name: 'GenerateSignature',
        component: () => import('../views/SignatureGeneration.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/verify',
        name: 'VerifySignature',
        component: () => import('../views/SignatureVerification.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/admin',
        name: 'Admin',
        component: () => import('../views/admin/AdminLayout.vue'),
        meta: { requiresAuth: true, requiresAdmin: true },
        children: [
            {
                path: '',
                redirect: '/admin/dashboard'
            },
            {
                path: 'dashboard',
                name: 'AdminDashboard',
                component: () => import('../views/admin/Dashboard.vue')
            },
            {
                path: 'users',
                name: 'AdminUsers',
                component: () => import('../views/admin/UserManagement.vue')
            },
            {
                path: 'certificates',
                name: 'AdminCertificates',
                component: () => import('../views/admin/CertificateManagement.vue')
            },
            {
                path: 'logs',
                name: 'AdminLogs',
                component: () => import('../views/admin/OperationLogs.vue')
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = store.state.token
    const user = store.state.user
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
    const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin)

    console.log('Router guard:', { to: to.path, token: !!token, requiresAuth, requiresAdmin })

    if (requiresAuth && !token) {
        console.log('Redirecting to login - no token')
        next('/login')
    } else if (requiresAdmin && user?.authority !== '1') {
        console.log('Redirecting to home - not admin')
        next('/home')
    } else if (!requiresAuth && token && (to.path === '/login' || to.path === '/register')) {
        console.log('Redirecting to home - already logged in')
        next('/home')
    } else {
        next()
    }
})

export default router
