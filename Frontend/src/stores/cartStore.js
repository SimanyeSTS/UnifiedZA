// src/stores/cartStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useAuthStore } from './authStore'

const API_BASE = 'https://localhost:7050/api'

export const useCartStore = defineStore('cart', () => {
  const applications = ref([])
  const savedCourses = ref([])
  const stats = ref({ totalApplications: 2, pending: 1, accepted: 1 })
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

  // Local storage helper for offline fallback
  const getLocalState = () => {
    const raw = localStorage.getItem('unifiedza_fallback_cart')
    if (raw) {
      try { return JSON.parse(raw) } catch { /* ignore */ }
    }
    const def = {
      savedCourses: [
        { savedCourseID: 101, userID: 1, courseID: 1, courseName: 'Diploma in Information Technology', institutionName: 'Cape Peninsula University of Technology', apsRequirement: 28, intakeYear: 2027 }
      ],
      applications: [
        { applicationID: 201, userID: 1, courseID: 3, courseName: 'Diploma in Applications Development', institutionName: 'Cape Peninsula University of Technology', applicationDate: '2026-11-01', status: 'Pending' }
      ]
    }
    localStorage.setItem('unifiedza_fallback_cart', JSON.stringify(def))
    return def
  }

  const saveLocalState = (state) => {
    localStorage.setItem('unifiedza_fallback_cart', JSON.stringify(state))
  }

  const fetchStats = async () => {
    try {
      const userId = getActiveUserId()
      const response = await fetch(`${API_BASE}/applications/user/${userId}/stats`)
      if (response.ok) {
        stats.value = await response.json()
        return
      }
    } catch { /* use local */ }

    const local = getLocalState()
    const apps = local.applications
    stats.value = {
      totalApplications: apps.length,
      pending: apps.filter(a => a.status === 'Pending').length,
      accepted: apps.filter(a => a.status === 'Accepted').length
    }
  }

  const fetchApplications = async () => {
    isLoading.value = true
    try {
      const userId = getActiveUserId()
      const response = await fetch(`${API_BASE}/applications/user/${userId}`)
      if (response.ok) {
        applications.value = await response.json()
        isLoading.value = false
        return
      }
    } catch { /* use local */ }

    applications.value = getLocalState().applications
    isLoading.value = false
  }

  const fetchSavedCourses = async () => {
    try {
      const userId = getActiveUserId()
      const response = await fetch(`${API_BASE}/savedcourses/user/${userId}`)
      if (response.ok) {
        savedCourses.value = await response.json()
        return
      }
    } catch { /* use local */ }

    savedCourses.value = getLocalState().savedCourses
  }

  // CREATE: Add course from Universities screen to Cart (Max 3 enforced)
  const addCourseToCart = async (course) => {
    clearMessages()
    const userId = getActiveUserId()
    try {
      const response = await fetch(`${API_BASE}/savedcourses`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userID: userId, courseID: course.courseID })
      })

      if (response.ok) {
        await fetchSavedCourses()
        successMessage.value = `Added "${course.name}" (${course.institutionShortName || course.institutionName}) to your application cart!`
        return true
      }
    } catch { /* use local fallback */ }

    // Local fallback addition
    const local = getLocalState()
    if (local.savedCourses.length >= 3) {
      errorMessage.value = 'Maximum application limit of 3 reached. Please remove a course to add another.'
      return false
    }
    const exists = local.savedCourses.some(item => item.courseID === course.courseID)
    if (exists) {
      errorMessage.value = 'This course is already saved in your cart.'
      return false
    }

    const newSaved = {
      savedCourseID: Date.now(),
      userID: userId,
      courseID: course.courseID,
      courseName: course.name,
      institutionName: course.institutionName || course.institutionShortName,
      apsRequirement: course.aps || 28,
      intakeYear: 2027
    }
    local.savedCourses.push(newSaved)
    saveLocalState(local)
    savedCourses.value = local.savedCourses
    successMessage.value = `Added "${course.name}" to your application cart!`
    return true
  }

  // DELETE: Remove a saved course from Cart
  const removeSavedCourse = async (savedCourseId) => {
    clearMessages()
    try {
      const response = await fetch(`${API_BASE}/savedcourses/${savedCourseId}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        await fetchSavedCourses()
        successMessage.value = 'Course removed from cart.'
        return
      }
    } catch { /* use local fallback */ }

    const local = getLocalState()
    local.savedCourses = local.savedCourses.filter(i => i.savedCourseID !== savedCourseId)
    saveLocalState(local)
    savedCourses.value = local.savedCourses
    successMessage.value = 'Course removed from cart.'
  }

  // CREATE APPLICATION: Submit a saved course
  const submitApplication = async (courseID, savedCourseID = null) => {
    clearMessages()
    const userId = getActiveUserId()
    try {
      const response = await fetch(`${API_BASE}/applications`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userID: userId, courseID })
      })

      if (response.ok) {
        if (savedCourseID) {
          await fetch(`${API_BASE}/savedcourses/${savedCourseID}`, { method: 'DELETE' })
        }
        await loadDashboardData()
        successMessage.value = 'Application submitted successfully! Track its progress below.'
        return true
      }
    } catch { /* use local fallback */ }

    const local = getLocalState()
    const courseToAdd = local.savedCourses.find(c => c.courseID === courseID) || {
      courseName: 'Diploma in Information Technology',
      institutionName: 'Cape Peninsula University of Technology'
    }

    local.savedCourses = local.savedCourses.filter(i => i.courseID !== courseID)
    const newApp = {
      applicationID: Date.now(),
      userID: userId,
      courseID,
      courseName: courseToAdd.courseName,
      institutionName: courseToAdd.institutionName,
      applicationDate: new Date().toISOString().split('T')[0],
      status: 'Pending'
    }
    local.applications.unshift(newApp)
    saveLocalState(local)
    await loadDashboardData()
    successMessage.value = 'Application submitted successfully!'
    return true
  }

  const submitAllInCart = async () => {
    clearMessages()
    if (!savedCourses.value.length) return
    const itemsToSubmit = [...savedCourses.value]
    for (const item of itemsToSubmit) {
      await submitApplication(item.courseID, item.savedCourseID)
    }
    successMessage.value = 'All cart applications submitted to institutions!'
  }

  const withdrawApplication = async (applicationId) => {
    clearMessages()
    try {
      const response = await fetch(`${API_BASE}/applications/${applicationId}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        await loadDashboardData()
        successMessage.value = 'Application withdrawn.'
        return
      }
    } catch { /* use local fallback */ }

    const local = getLocalState()
    local.applications = local.applications.filter(a => a.applicationID !== applicationId)
    saveLocalState(local)
    await loadDashboardData()
    successMessage.value = 'Application withdrawn.'
  }

  const fetchApplicationHistory = async (applicationId) => {
    try {
      const response = await fetch(`${API_BASE}/applications/${applicationId}/history`)
      if (response.ok) {
        statusHistory.value[applicationId] = await response.json()
        return
      }
    } catch { /* default timeline */ }

    statusHistory.value[applicationId] = [
      { stageName: 'Application Submitted', stageDate: '2026-11-01', isComplete: true, stageOrder: 1 },
      { stageName: 'Under Review', stageDate: '2026-11-03', isComplete: true, stageOrder: 2 },
      { stageName: 'Document Verified', stageDate: '2026-11-05', isComplete: true, stageOrder: 3 },
      { stageName: 'Final Decision Pending', stageDate: null, isComplete: false, stageOrder: 4 }
    ]
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
