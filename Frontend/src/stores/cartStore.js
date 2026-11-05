import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCartStore = defineStore('cart', () => {
  const applications = ref([])
  const savedCourses = ref([])
  const stats = ref({ totalApplications: 0, pending: 0, accepted: 0 })
  const errorMessage = ref('')
  const isLoading = ref(false)

  // HARDCODED DEMO USER (Bypassing Auth)
  const DEMO_USER_ID = 1

  const fetchStats = async () => {
    try {
      const response = await fetch(`https://localhost:7050/api/applications/user/${DEMO_USER_ID}/stats`)
      if (response.ok) {
        stats.value = await response.json()
      }
    } catch (error) {
      console.error("Failed to fetch stats:", error)
    }
  }

  const fetchApplications = async () => {
    isLoading.value = true
    try {
      const response = await fetch(`https://localhost:7050/api/applications/user/${DEMO_USER_ID}`)
      if (response.ok) {
        applications.value = await response.json()
      }
    } catch (error) {
      errorMessage.value = "Could not load applications."
    } finally {
      isLoading.value = false
    }
  }

  const fetchSavedCourses = async () => {
    try {
      const response = await fetch(`https://localhost:7050/api/savedcourses/user/${DEMO_USER_ID}`)
      if (response.ok) {
        savedCourses.value = await response.json()
      }
    } catch (error) {
      console.error("Could not load saved courses:", error)
    }
  }

  const withdrawApplication = async (applicationId) => {
    try {
      const response = await fetch(`https://localhost:7050/api/applications/${applicationId}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        await fetchApplications()
        await fetchStats()
      } else {
        const errorData = await response.json()
        throw new Error(errorData.message)
      }
    } catch (error) {
      errorMessage.value = `Withdrawal failed: ${error.message}`
    }
  }

  const loadDashboardData = async () => {
    await fetchStats()
    await fetchApplications()
    await fetchSavedCourses()
  }

  return {
    applications,
    savedCourses,
    stats,
    errorMessage,
    isLoading,
    fetchStats,
    fetchApplications,
    fetchSavedCourses,
    withdrawApplication,
    loadDashboardData
  }
})
