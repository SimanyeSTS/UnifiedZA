<!-- Chuma's territory -->

<script setup>
import { ref } from 'vue'

const selectedRole = ref('Student')

const form = ref({
  fullName: '',
  idNumber: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: '',
  province: ''
})

const handleRegister = () => {
  console.log(`Registering new ${selectedRole.value}:`, form.value)
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

      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>Full Name</label>
          <input type="text" v-model="form.fullName" required />
        </div>

        <div class="form-group">
          <label>ID Number</label>
          <input type="text" v-model="form.idNumber" required />
        </div>

        <div class="form-group">
          <label>Email Address</label>
          <input type="email" v-model="form.email" required />
        </div>

        <div class="form-group">
          <label>Phone Number</label>
          <input type="tel" v-model="form.phone" required />
        </div>

        <div class="form-group">
          <label>Password</label>
          <input type="password" v-model="form.password" required />
        </div>

        <div class="form-group">
          <label>Confirm Password</label>
          <input type="password" v-model="form.confirmPassword" required />
        </div>

        <div class="form-group">
          <label>Province</label>
          <input type="text" v-model="form.province" required />
        </div>

        <button type="submit" class="login-btn">CREATE ACCOUNT</button>
      </form>

      <p class="register-link">
        Already have an account? <RouterLink to="/">Log in</RouterLink>
      </p>
    </div>
  </main>
</template>

<style scoped>
.main-content { display: flex; justify-content: center; align-items: center; padding: 50px 20px; }
.login-card { background-color: white; padding: 40px; width: 100%; max-width: 420px; border-radius: 8px; }
.login-card h1 { text-align: center; font-size: 24px; color: var(--vt-c-text-main); margin-bottom: 25px; }

.role-selector { display: flex; margin-bottom: 20px; }
.role-btn {
  flex: 1;
  padding: 10px;
  border: 1px solid var(--vt-c-primary);
  background-color: white;
  color: var(--vt-c-primary);
  font-size: 13px;
  cursor: pointer;
}
.role-btn.active {
  background-color: var(--vt-c-primary);
  color: white;
}

/* Form Styles */
.form-group { margin-bottom: 20px; }
.form-group label { display: block; font-size: 13px; color: var(--vt-c-text-main); margin-bottom: 5px; }
.form-group input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; font-size: 14px; outline: none; }
.form-group input:focus { border-color: var(--vt-c-primary); }

.login-btn { width: 100%; padding: 12px; background-color: var(--vt-c-secondary); color: var(--vt-c-primary); border: none; border-radius: 4px; font-size: 14px; font-weight: bold; cursor: pointer; letter-spacing: 1px; }
.login-btn:hover { background-color: var(--vt-c-secondary-hover); }

.register-link { text-align: center; font-size: 13px; margin-top: 20px; }
.register-link a { color: var(--vt-c-link); text-decoration: none; }
</style>
