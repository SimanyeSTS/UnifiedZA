// src/stores/apsStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useApsStore = defineStore('aps', () => {
  const savedSubjects = sessionStorage.getItem('unifiedza_subjects')
  const savedAps = sessionStorage.getItem('unifiedza_aps')

  const subjects = ref(
    savedSubjects
      ? JSON.parse(savedSubjects)
      : [
          { subjectCode: 'ENGHL', percentage: 75 },
          { subjectCode: 'AFRFAL', percentage: 68 },
          { subjectCode: 'MATH', percentage: 72 },
          { subjectCode: 'PHYS', percentage: 70 },
          { subjectCode: 'LFSC', percentage: 74 },
          { subjectCode: 'INFT', percentage: 82 },
          { subjectCode: 'LO', percentage: 85 }
        ]
  )

  const calculatedAps = ref(savedAps !== null ? Number(savedAps) : null)
  const errorMessage = ref('')
  const isLoading = ref(false)

  // Official NSC 7-point conversion scale
  const getPointsForPercentage = (pct) => {
    const p = Number(pct)
    if (p >= 80) return 7
    if (p >= 70) return 6
    if (p >= 60) return 5
    if (p >= 50) return 4
    if (p >= 40) return 3
    if (p >= 30) return 2
    if (p >= 0) return 1
    return 0
  }

  // Exact replica of your Spring Boot ApsCalculationService.java rules
  const computeApsLocally = (completedSubjects) => {
    let hasMath = false
    let hasMathLit = false
    let hasHomeLanguage = false
    let hasFirstAdditional = false
    const validPoints = []

    for (const item of completedSubjects) {
      const code = item.subjectCode.trim().toUpperCase()
      const pct = Number(item.percentage)

      if (Number.isNaN(pct) || pct < 0 || pct > 100) {
        throw new Error(`Invalid percentage for ${code}: must be between 0 and 100.`)
      }

      if (code.includes('HL') || code.includes('HOME')) {
        hasHomeLanguage = true
      }
      if (code.includes('FAL') || code.includes('ADDITIONAL')) {
        hasFirstAdditional = true
      }
      if (code === 'MATH' || code === 'MATHEMATICS' || code === 'MATHS') {
        hasMath = true
      }
      if (code === 'MLIT' || code.includes('LIT')) {
        hasMathLit = true
      }

      if (hasMath && hasMathLit) {
        throw new Error('Validation Error: A student cannot take both Mathematics and Mathematical Literacy.')
      }

      // Exclude Life Orientation from APS sum
      if (code === 'LO' || code.includes('ORIENTATION')) {
        continue
      }

      validPoints.push(getPointsForPercentage(pct))
    }

    if (!hasHomeLanguage || !hasFirstAdditional) {
      throw new Error('Validation Error: Calculation must include at least one Home Language (e.g. ENGHL) and one First Additional Language (e.g. AFRFAL).')
    }

    validPoints.sort((a, b) => b - a)
    return validPoints.slice(0, 6).reduce((sum, pts) => sum + pts, 0)
  }

  const addSubject = () => {
    subjects.value.push({ subjectCode: '', percentage: null })
  }

  const removeSubject = (index) => {
    subjects.value.splice(index, 1)
    calculateAps()
  }

  const calculateAps = async () => {
    const completedSubjects = subjects.value.filter(
      (subject) =>
        subject.subjectCode.trim() && subject.percentage !== null && subject.percentage !== ''
    )

    if (completedSubjects.length < 7) {
      calculatedAps.value = null
      errorMessage.value = 'Add marks for at least 7 subjects before calculating APS.'
      return false
    }

    isLoading.value = true
    errorMessage.value = ''

    try {
      // First validate business rules locally
      const fallbackScore = computeApsLocally(completedSubjects)

      // Try Spring Boot API on port 8080 first
      try {
        const response = await fetch('http://localhost:8080/api/aps/calculate', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(completedSubjects)
        })

        if (response.ok) {
          const apiScore = await response.json()
          calculatedAps.value = Number(apiScore)
        } else {
          // If MySQL table is missing a subject code seed, use the verified calculation
          calculatedAps.value = fallbackScore
        }
      } catch {
        calculatedAps.value = fallbackScore
      }

      sessionStorage.setItem('unifiedza_aps', String(calculatedAps.value))
      sessionStorage.setItem('unifiedza_subjects', JSON.stringify(subjects.value))
      return true
    } catch (error) {
      calculatedAps.value = null
      errorMessage.value = error.message
      return false
    } finally {
      isLoading.value = false
    }
  }

  // Calculate immediately on store initialization if 7 subjects exist
  if (calculatedAps.value === null && subjects.value.length >= 7) {
    calculateAps()
  }

  return {
    subjects,
    calculatedAps,
    errorMessage,
    isLoading,
     getPointsForPercentage,
    addSubject,
    removeSubject,
    calculateAps
  }
})
