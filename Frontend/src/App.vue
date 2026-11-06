<script setup>
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { useAuthStore } from './stores/authStore'
import { useCartStore } from './stores/cartStore'
import { useApsStore } from './stores/apsStore'

const authStore = useAuthStore()
const cartStore = useCartStore()
const apsStore = useApsStore()
const router = useRouter()

const handleLogout = () => {
  authStore.logout()
  router.push('/')
}
</script>

<template>
  <div class="app-wrapper">
    <nav class="navbar">
      <div class="logo">
        <RouterLink to="/" class="logo-link">
          <h2>UNIFIED ZA</h2>
        </RouterLink>
      </div>
      <ul class="nav-links">
        <li><RouterLink to="/">Home</RouterLink></li>
        <li><RouterLink to="/marks-recording">Calculate APS</RouterLink></li>
        <li>
          <RouterLink to="/marks-results">
            APS Results
            <span v-if="apsStore.calculatedAps" class="aps-pill">{{ apsStore.calculatedAps }}</span>
          </RouterLink>
        </li>
        <li><RouterLink to="/search">Universities</RouterLink></li>
        <li>
          <RouterLink to="/applications-cart">
            My Applications
            <span v-if="cartStore.savedCourses.length" class="cart-pill">
              {{ cartStore.savedCourses.length }}/3
            </span>
          </RouterLink>
        </li>
        <li><RouterLink to="/bursary">Funding</RouterLink></li>
        <li><RouterLink to="/help">Support</RouterLink></li>

        <template v-if="!authStore.isAuthenticated">
          <!-- <li><RouterLink to="/register">Register</RouterLink></li> -->
          <li><RouterLink to="/login" class="nav-login-btn">Login</RouterLink></li>
        </template>
        <template v-else>
          <li class="user-greeting">Hi, {{ authStore.user.firstName }}</li>
          <li><button @click="handleLogout" class="nav-login-btn logout-btn">Logout</button></li>
        </template>
      </ul>
    </nav>

    <RouterView />

    <footer class="footer">
      <p>UNIFIED ZA &copy; 2026 | Centralised South African Higher Education Application Portal</p>
    </footer>
  </div>
</template>

<style>
.app-wrapper { display: flex; flex-direction: column; min-height: 100vh; }
.navbar { background-color: var(--vt-c-primary); padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px; }
.logo-link { text-decoration: none; }
.logo h2 { color: white; font-size: 20px; letter-spacing: 1px; }
.nav-links { list-style: none; display: flex; gap: 22px; align-items: center; flex-wrap: wrap; }
.nav-links a { color: #c8d8d8; text-decoration: none; font-size: 14px; font-weight: 500; transition: color 0.2s ease; display: inline-flex; align-items: center; gap: 6px; }
.nav-links a:hover, .nav-links a.router-link-active { color: white; }
.aps-pill, .cart-pill { background: var(--vt-c-secondary); color: var(--vt-c-primary-dark); font-size: 11px; font-weight: 800; padding: 2px 7px; border-radius: 10px; }
.user-greeting { color: #ffffff; font-size: 13px; font-weight: bold; background: rgba(255,255,255,0.15); padding: 6px 12px; border-radius: 16px; }
.nav-login-btn { background-color: var(--vt-c-secondary); color: var(--vt-c-primary) !important; padding: 8px 16px; border-radius: 4px; font-weight: bold !important; border: none; cursor: pointer; font-size: 13px; }
.nav-login-btn:hover { background-color: var(--vt-c-secondary-hover); }
.footer { background-color: var(--vt-c-primary); color: white; text-align: center; padding: 15px; font-size: 13px; margin-top: auto; }
</style>
