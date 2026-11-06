<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../../stores/authStore'
import { useRouter, useRoute } from 'vue-router'

const email = ref('')
const password = ref('')
const rememberMe = ref(false)

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()

const handleLogin = async () => {
  const success = await authStore.login(email.value, password.value)
  if (success) {
    const redirectPath = route.query.redirect || '/search'
    router.push(redirectPath)
  }
}
</script>

<template>
  <main class="main-content">
    <div class="login-card">
      <h1>Welcome Back</h1>
      <p>Log in to save courses and submit your applications</p>

      <div v-if="authStore.errorMessage" class="alert-error">
        {{ authStore.errorMessage }}
      </div>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>Email Address</label>
          <input type="email" v-model="email" placeholder="you@student.ac.za" required />
        </div>

        <div class="form-group">
          <label>Password</label>
          <input type="password" v-model="password" placeholder="At least 8 characters" required />
        </div>

        <div class="form-options">
          <label><input type="checkbox" v-model="rememberMe" /> Remember me</label>
        </div>

        <button type="submit" class="login-btn" :disabled="authStore.isLoading">
          {{ authStore.isLoading ? 'Signing In...' : 'Log In' }}
        </button>
      </form>

      <p class="register-link">
        Don't have an account? <RouterLink to="/register">Register</RouterLink>
      </p>
    </div>
  </main>
</template>

<style scoped>
.main-content { display: flex; justify-content: center; align-items: center; padding: 50px 20px; }
.login-card { background-color: white; padding: 40px; width: 100%; max-width: 420px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.06); }
.login-card h1 { text-align: center; font-size: 24px; color: var(--vt-c-text-main); margin-bottom: 8px; }
.login-card p { text-align: center; font-size: 13px; color: var(--vt-c-text-muted); margin-bottom: 25px; }
.alert-error { background: #fde2e2; color: #c53030; padding: 10px 12px; border-radius: 6px; font-size: 13px; margin-bottom: 18px; }
.form-group { margin-bottom: 20px; }
.form-group label { display: block; font-size: 13px; color: var(--vt-c-text-main); margin-bottom: 5px; font-weight: 600; }
.form-group input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; font-size: 14px; outline: none; }
.form-group input:focus { border-color: var(--vt-c-primary); }
.form-options { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; font-size: 13px; }
.login-btn { width: 100%; padding: 12px; background-color: var(--vt-c-primary); color: white; border: none; border-radius: 4px; font-size: 14px; font-weight: bold; cursor: pointer; }
.login-btn:hover:not(:disabled) { background-color: var(--vt-c-primary-dark); }
.register-link { text-align: center; font-size: 13px; margin-top: 20px; }
.register-link a { color: var(--vt-c-link); text-decoration: none; font-weight: bold; }
</style>
