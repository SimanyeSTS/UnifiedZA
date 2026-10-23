import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useApsStore = defineStore('aps', () => {
  // State: Start with one empty subject row
  const subjects = ref([{ subjectCode: '', percentage: null }])

  const calculatedAps = ref(null)
  const errorMessage = ref('')
  const isLoading = ref(false)

  // Actions
  const addSubject = () => {
    subjects.value.push({ subjectCode: '', percentage: null })
  }

  const removeSubject = (index) => {
    subjects.value.splice(index, 1)
  }

  const calculateAps = async () => {
    const completedSubjects = subjects.value.filter(
      (subject) =>
        subject.subjectCode.trim() && subject.percentage !== null && subject.percentage !== '',
    )

    if (completedSubjects.length < 7) {
      calculatedAps.value = null
      errorMessage.value = 'Add marks for at least 7 subjects before calculating APS.'
      return false
    }

    isLoading.value = true
    errorMessage.value = ''
    calculatedAps.value = null

    try {
      // POST the subjects array to API
      const response = await fetch('http://localhost:8080/api/aps/calculate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(completedSubjects),
      })

      if (!response.ok) {
        // Capture business logic errors thrown by the backend (e.g., missing Home Language)
        const errorText = await response.text()
        throw new Error(errorText || 'Failed to calculate APS')
      }

      // Store the successful APS calculation
      const data = await response.json()
      calculatedAps.value = data
      return true
    } catch (error) {
      errorMessage.value = error.message
    } finally {
      isLoading.value = false
    }
  }

  return {
    subjects,
    calculatedAps,
    errorMessage,
    isLoading,
    addSubject,
    removeSubject,
    calculateAps,
  }
})
