<!-- Chuma's territory -->

<script setup>
import { ref } from 'vue'

// Reactive state for the form
const email = ref('')
const password = ref('')
const rememberMe = ref(false)

const handleLogin = () => {
  // Call authStore
  console.log('Attempting login with:', email.value, password.value)
}
</script>

<template>
  <main class="main-content">
    <div class="login-card">
      <h1>Welcome Back</h1>
      <p>Log in to continue your application</p>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>Email Address</label>
          <input type="email" v-model="email" required />
        </div>

        <div class="form-group">
          <label>Password</label>
          <input type="password" v-model="password" required />
        </div>

        <div class="form-options">
          <label><input type="checkbox" v-model="rememberMe" /> Remember me</label>
          <a href="#">Forgot password?</a>
        </div>

        <button type="submit" class="login-btn">Log In</button>
      </form>

      <p class="or-divider">or</p>
      <button class="google-btn">Continue with Google</button>

      <p class="register-link">
        Don't have an account? <RouterLink to="/register">Register</RouterLink>
      </p>
    </div>
  </main>
</template>

<style scoped>
.main-content {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 50px 20px;
}

.login-card {
  background-color: white;
  padding: 40px;
  width: 100%;
  max-width: 420px;
  border-radius: 8px;
}

.login-card h1 { text-align: center; font-size: 24px; color: var(--vt-c-text-main); margin-bottom: 8px; }
.login-card p { text-align: center; font-size: 13px; color: var(--vt-c-text-muted); margin-bottom: 25px; }

.form-group { margin-bottom: 20px; }
.form-group label { display: block; font-size: 13px; color: var(--vt-c-text-main); margin-bottom: 5px; }
.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
}
.form-group input:focus { border-color: var(--vt-c-primary); }

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  font-size: 13px;
}
.form-options a { color: var(--vt-c-link); text-decoration: none; }

.login-btn {
  width: 100%;
  padding: 12px;
  background-color: var(--vt-c-secondary);
  color: var(--vt-c-primary);
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  letter-spacing: 1px;
}
.login-btn:hover { background-color: var(--vt-c-secondary-hover); }

.or-divider { text-align: center; color: var(--vt-c-text-muted); margin: 20px 0; font-size: 13px; }

.google-btn {
  width: 100%;
  padding: 12px;
  background-color: var(--vt-c-primary);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}
.google-btn:hover { background-color: var(--vt-c-primary-dark); }

.register-link { text-align: center; font-size: 13px; margin-top: 20px; }
.register-link a { color: var(--vt-c-link); text-decoration: none; }
</style>
