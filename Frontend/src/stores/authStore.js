// src/stores/authStore.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

const API_BASE = 'https://localhost:7050/api'

export const useAuthStore = defineStore('auth', () => {
  const storedUser = sessionStorage.getItem('unifiedza_user')
  const user = ref(storedUser ? JSON.parse(storedUser) : null)
  const errorMessage = ref('')
  const isLoading = ref(false)

  const isAuthenticated = computed(() => !!user.value && !!user.value.userID)

  const login = async (email, password) => {
    isLoading.value = true
    errorMessage.value = ''
    try {
      const response = await fetch(`${API_BASE}/users/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      })

      const data = await response.json()
      if (!response.ok) {
        throw new Error(data.message || 'Invalid email or password.')
      }

      user.value = data
      sessionStorage.setItem('unifiedza_user', JSON.stringify(data))
      return true
    } catch (error) {
      errorMessage.value = error.message || 'Could not connect to Authentication API.'
      return false
    } finally {
      isLoading.value = false
    }
  }

  const register = async ({ firstName, lastName, email, password }) => {
    isLoading.value = true
    errorMessage.value = ''
    try {
      const response = await fetch(`${API_BASE}/users/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ firstName, lastName, email, password })
      })

      const data = await response.json()
      if (!response.ok) {
        throw new Error(data.message || 'Registration failed.')
      }

      user.value = data
      sessionStorage.setItem('unifiedza_user', JSON.stringify(data))
      return true
    } catch (error) {
      errorMessage.value = error.message || 'Could not connect to Authentication API.'
      return false
    } finally {
      isLoading.value = false
    }
  }

  const logout = () => {
    user.value = null
    sessionStorage.removeItem('unifiedza_user')
  }

  return {
    user,
    errorMessage,
    isLoading,
    isAuthenticated,
    login,
    register,
    logout
  }
})
