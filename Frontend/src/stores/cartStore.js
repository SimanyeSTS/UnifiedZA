// src/stores/cartStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useAuthStore } from './authStore'

const API_BASE = 'https://localhost:7050/api'

export const useCartStore = defineStore('cart', () => {
  const applications = ref([])
  const savedCourses = ref([])
  const stats = ref({ totalApplications: 0, pending: 0, accepted: 0 })
  const statusHistory = ref({})
  const errorMessage = ref('')
  const successMessage = ref('')
  const isLoading = ref(false)

  const getActiveUserId = () => {
    const authStore = useAuthStore()
    return authStore.user?.userID || 1
  }

  const clearMessages = () => {
    errorMessage.value = ''
    successMessage.value = ''
  }

  const fetchStats = async () => {
    try {
      const userId = getActiveUserId()
      const response = await fetch(`${API_BASE}/applications/user/${userId}/stats`)
      if (response.ok) {
        stats.value = await response.json()
      }
    } catch (error) {
      console.error('Failed to fetch stats:', error)
    }
  }

  const fetchApplications = async () => {
    isLoading.value = true
    try {
      const userId = getActiveUserId()
      const response = await fetch(`${API_BASE}/applications/user/${userId}`)
      if (response.ok) {
        applications.value = await response.json()
      }
    } catch (error) {
      errorMessage.value = 'Could not load applications from server.'
    } finally {
      isLoading.value = false
    }
  }

  const fetchSavedCourses = async () => {
    try {
      const userId = getActiveUserId()
      const response = await fetch(`${API_BASE}/savedcourses/user/${userId}`)
      if (response.ok) {
        savedCourses.value = await response.json()
      }
    } catch (error) {
      console.error('Could not load saved courses:', error)
    }
  }

  // CREATE: Add a course from Universities screen to Cart (Max 3 enforced)
  const addCourseToCart = async (course) => {
    clearMessages()
    const userId = getActiveUserId()
    try {
      const response = await fetch(`${API_BASE}/savedcourses`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userID: userId, courseID: course.courseID })
      })

      const data = await response.json()
      if (!response.ok) {
        throw new Error(data.message || 'Could not add course to cart.')
      }

      await fetchSavedCourses()
      successMessage.value = `Added "${course.name}" (${course.institutionShortName || course.institutionName}) to your application cart!`
      return true
    } catch (error) {
      errorMessage.value = error.message
      return false
    }
  }

  // DELETE: Remove a saved course from Cart
  const removeSavedCourse = async (savedCourseId) => {
    clearMessages()
    try {
      const response = await fetch(`${API_BASE}/savedcourses/${savedCourseId}`, {
        method: 'DELETE'
      })
      if (!response.ok) {
        const data = await response.json()
        throw new Error(data.message || 'Could not remove course.')
      }
      await fetchSavedCourses()
      successMessage.value = 'Course removed from cart.'
    } catch (error) {
      errorMessage.value = error.message
    }
  }

  // CREATE APPLICATION: Submit a saved course as an official application
  const submitApplication = async (courseID, savedCourseID = null) => {
    clearMessages()
    const userId = getActiveUserId()
    try {
      const response = await fetch(`${API_BASE}/applications`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userID: userId, courseID })
      })

      const data = await response.json()
      if (!response.ok) {
        throw new Error(data.message || 'Could not submit application.')
      }

      if (savedCourseID) {
        await fetch(`${API_BASE}/savedcourses/${savedCourseID}`, { method: 'DELETE' })
      }

      await loadDashboardData()
      successMessage.value = 'Application submitted! Track its progress below.'
      return true
    } catch (error) {
      errorMessage.value = error.message
      return false
    }
  }

  // Submit all courses in Cart at once (Unified Bulk Checkout!)
  const submitAllInCart = async () => {
    clearMessages()
    const userId = getActiveUserId()
    if (!savedCourses.value.length) return

    try {
      for (const item of savedCourses.value) {
        await fetch(`${API_BASE}/applications`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ userID: userId, courseID: item.courseID })
        })
        await fetch(`${API_BASE}/savedcourses/${item.savedCourseID}`, {
          method: 'DELETE'
        })
      }
      await loadDashboardData()
      successMessage.value = 'All cart applications submitted to institutions!'
    } catch (error) {
      errorMessage.value = `Bulk submission error: ${error.message}`
    }
  }

  // DELETE APPLICATION: Withdraw submitted application
  const withdrawApplication = async (applicationId) => {
    clearMessages()
    try {
      const response = await fetch(`${API_BASE}/applications/${applicationId}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        await fetchApplications()
        await fetchStats()
        successMessage.value = 'Application withdrawn.'
      } else {
        const errorData = await response.json()
        throw new Error(errorData.message)
      }
    } catch (error) {
      errorMessage.value = `Withdrawal failed: ${error.message}`
    }
  }

  // READ TIMELINE: Fetch Sisa's StatusHistory stages
  const fetchApplicationHistory = async (applicationId) => {
    try {
      const response = await fetch(`${API_BASE}/applications/${applicationId}/history`)
      if (response.ok) {
        statusHistory.value[applicationId] = await response.json()
      }
    } catch (error) {
      console.error('Could not load history:', error)
    }
  }

  const loadDashboardData = async () => {
    await Promise.all([fetchStats(), fetchApplications(), fetchSavedCourses()])
  }

  return {
    applications,
    savedCourses,
    stats,
    statusHistory,
    errorMessage,
    successMessage,
    isLoading,
    clearMessages,
    fetchStats,
    fetchApplications,
    fetchSavedCourses,
    addCourseToCart,
    removeSavedCourse,
    submitApplication,
    submitAllInCart,
    withdrawApplication,
    fetchApplicationHistory,
    loadDashboardData
  }
})
