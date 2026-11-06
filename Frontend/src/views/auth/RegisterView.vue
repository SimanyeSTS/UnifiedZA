<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../../stores/authStore'
import { useRouter } from 'vue-router'
import { provinces } from '../../data/educationOptions'

const authStore = useAuthStore()
const router = useRouter()

const selectedRole = ref('Student')
const localError = ref('')

const form = ref({
  fullName: '',
  idNumber: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: '',
  province: 'Western Cape'
})

const handleRegister = async () => {
  localError.value = ''
  if (form.value.password.length < 8) {
    localError.value = 'Password must be at least 8 characters long.'
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    localError.value = 'Passwords do not match.'
    return
  }

  const nameParts = form.value.fullName.trim().split(/\s+/)
  const firstName = nameParts[0] || 'Student'
  const lastName = nameParts.slice(1).join(' ') || 'Applicant'

  const success = await authStore.register({
    firstName,
    lastName,
    email: form.value.email.trim(),
    password: form.value.password
  })

  if (success) {
    router.push('/search')
  }
}
</script>

<template>
  <main class="main-content">
    <div class="login-card">
      <h1>Create Your Account</h1>

      <div class="role-selector">
        <button
          type="button"
          :class="['role-btn', { active: selectedRole === 'Student' }]"
          @click="selectedRole = 'Student'"
        >
          Student
        </button>
        <button
          type="button"
          :class="['role-btn', { active: selectedRole === 'University Admin' }]"
          @click="selectedRole = 'University Admin'"
        >
          University Admin
        </button>
      </div>

      <div v-if="localError || authStore.errorMessage" class="alert-error">
        {{ localError || authStore.errorMessage }}
      </div>

      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>Full Name</label>
          <input type="text" v-model="form.fullName" placeholder="e.g. Simanye Somdaka" required />
        </div>

        <div class="form-group">
          <label>ID Number (13 digits)</label>
          <input type="text" v-model="form.idNumber" maxlength="13" placeholder="040109..." required />
        </div>

        <div class="form-group">
          <label>Email Address</label>
          <input type="email" v-model="form.email" placeholder="you@student.ac.za" required />
        </div>

        <div class="form-group">
          <label>Phone Number</label>
          <input type="tel" v-model="form.phone" placeholder="072 000 0000" required />
        </div>

        <div class="form-group">
          <label>Province</label>
          <select v-model="form.province" required>
            <option v-for="prov in provinces" :key="prov" :value="prov">{{ prov }}</option>
          </select>
        </div>

        <div class="form-group">
          <label>Password (min 8 characters)</label>
          <input type="password" v-model="form.password" required />
        </div>

        <div class="form-group">
          <label>Confirm Password</label>
          <input type="password" v-model="form.confirmPassword" required />
        </div>

        <button type="submit" class="login-btn" :disabled="authStore.isLoading">
          {{ authStore.isLoading ? 'CREATING ACCOUNT...' : 'CREATE ACCOUNT' }}
        </button>
      </form>

      <p class="register-link">
        Already have an account? <RouterLink to="/login">Log in</RouterLink>
      </p>
    </div>
  </main>
</template>

<style scoped>
.main-content { display: flex; justify-content: center; align-items: center; padding: 50px 20px; }
.login-card { background-color: white; padding: 40px; width: 100%; max-width: 440px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.06); }
.login-card h1 { text-align: center; font-size: 24px; color: var(--vt-c-text-main); margin-bottom: 25px; }
.role-selector { display: flex; margin-bottom: 20px; }
.role-btn { flex: 1; padding: 10px; border: 1px solid var(--vt-c-primary); background-color: white; color: var(--vt-c-primary); font-size: 13px; cursor: pointer; font-weight: bold; }
.role-btn.active { background-color: var(--vt-c-primary); color: white; }
.alert-error { background: #fde2e2; color: #c53030; padding: 10px 12px; border-radius: 6px; font-size: 13px; margin-bottom: 18px; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; font-size: 13px; color: var(--vt-c-text-main); margin-bottom: 5px; font-weight: 600; }
.form-group input, .form-group select { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; font-size: 14px; outline: none; background: white; }
.form-group input:focus, .form-group select:focus { border-color: var(--vt-c-primary); }
.login-btn { width: 100%; padding: 12px; background-color: var(--vt-c-primary); color: white; border: none; border-radius: 4px; font-size: 14px; font-weight: bold; cursor: pointer; letter-spacing: 1px; }
.login-btn:hover:not(:disabled) { background-color: var(--vt-c-primary-dark); }
.register-link { text-align: center; font-size: 13px; margin-top: 20px; }
.register-link a { color: var(--vt-c-link); text-decoration: none; font-weight: bold; }
</style>
