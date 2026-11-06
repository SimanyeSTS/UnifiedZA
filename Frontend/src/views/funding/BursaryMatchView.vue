<script setup>
import { ref, onMounted } from 'vue'
import { useApsStore } from '../../stores/apsStore'

const apsStore = useApsStore()

const searchCriteria = ref({
  householdIncome: 250000,
  baseAps: apsStore.calculatedAps || 30,
  fieldOfStudy: 'ALL'
})

// Full fallback dataset matching Kgalelo's exact schema + extra South African bursaries
const fallbackBursaries = [
  {
    bursaryId: 1,
    title: 'NSFAS Financial Aid Scheme',
    description: 'Full government funding covering tuition, accommodation, transport, and learning materials for undergraduate students.',
    minApsScore: 20,
    maxHouseholdIncome: 350000,
    fieldOfStudy: 'ALL',
    isNsfas: true
  },
  {
    bursaryId: 2,
    title: 'NSFAS General IT & Engineering Bursary',
    description: 'Targeted National Student Financial Aid for Information Technology and scarce skills diplomas and degrees.',
    minApsScore: 24,
    maxHouseholdIncome: 350000,
    fieldOfStudy: 'Information Technology',
    isNsfas: true
  },
  {
    bursaryId: 3,
    title: 'CPUT ICT Sector Merit Bursary',
    description: 'Merit and financial need funding for Applications Development, Multimedia, and IT majors.',
    minApsScore: 28,
    maxHouseholdIncome: 600000,
    fieldOfStudy: 'Information Technology',
    isNsfas: false
  },
  {
    bursaryId: 4,
    title: 'Funza Lushaka Teaching Bursary',
    description: 'Full-cost Department of Basic Education bursary for Bachelor of Education (BEd) students.',
    minApsScore: 28,
    maxHouseholdIncome: 500000,
    fieldOfStudy: 'Education',
    isNsfas: false
  },
  {
    bursaryId: 5,
    title: 'FirstRand & Allan Gray Commerce Fellowship',
    description: 'Comprehensive merit scholarship for BCom, Accounting, and Actuarial Science applicants.',
    minApsScore: 34,
    maxHouseholdIncome: 1000000,
    fieldOfStudy: 'Commerce',
    isNsfas: false
  }
]

const bursaries = ref([])
const isLoading = ref(false)
const hasSearched = ref(false)

const filterLocally = () => {
  const income = Number(searchCriteria.value.householdIncome)
  const aps = Number(searchCriteria.value.baseAps)
  const field = searchCriteria.value.fieldOfStudy.trim().toLowerCase()

  return fallbackBursaries.filter((b) => {
    const incomeMatch = income <= b.maxHouseholdIncome
    const apsMatch = aps >= b.minApsScore
    const fieldMatch =
      field === 'all' ||
      b.fieldOfStudy.toLowerCase() === 'all' ||
      b.fieldOfStudy.toLowerCase().includes(field)
    return incomeMatch && apsMatch && fieldMatch
  })
}

const findMatches = async () => {
  isLoading.value = true
  hasSearched.value = true
  bursaries.value = []

  try {
    const { householdIncome, baseAps, fieldOfStudy } = searchCriteria.value
    const response = await fetch(
      `http://localhost:8081/api/bursaries/match?householdIncome=${householdIncome}&baseAps=${baseAps}&fieldOfStudy=${encodeURIComponent(fieldOfStudy)}`
    )

    if (!response.ok) throw new Error('API fallback')
    const apiData = await response.json()
    bursaries.value = apiData.length > 0 ? apiData : filterLocally()
  } catch {
    // Seamless local calculation if Kgalelo's server isn't reachable
    bursaries.value = filterLocally()
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  findMatches()
})
</script>

<template>
  <main class="funding-wrapper">
    <div class="header">
      <h1>Bursary & Funding Matcher</h1>
      <p class="subtitle">
        Automatically matched against your APS ({{ searchCriteria.baseAps }}) and annual household income.
      </p>
    </div>

    <div class="content-grid">
      <div class="search-panel">
        <h3>Match Criteria</h3>

        <div class="form-group">
          <label>Annual Household Income (ZAR)</label>
          <input type="number" v-model="searchCriteria.householdIncome" min="0" step="10000" />
        </div>

        <div class="form-group">
          <label>Your APS Score</label>
          <input type="number" v-model="searchCriteria.baseAps" min="0" max="42" />
        </div>

        <div class="form-group">
          <label>Field of Study</label>
          <select v-model="searchCriteria.fieldOfStudy">
            <option value="ALL">All Fields of Study</option>
            <option value="Information Technology">Information Technology & CS</option>
            <option value="Commerce">Commerce & Accounting</option>
            <option value="Education">Education</option>
          </select>
        </div>

        <button class="btn-solid" @click="findMatches" :disabled="isLoading">
          {{ isLoading ? 'Calculating Eligibility...' : 'Match Bursaries' }}
        </button>
      </div>

      <div class="results-panel">
        <h3>
          Eligible Bursaries
          <span v-if="hasSearched" class="result-count">({{ bursaries.length }} matches)</span>
        </h3>

        <div v-if="bursaries.length === 0" class="empty-state">
          No matching bursaries found for this income and APS combination. Try increasing APS or lowering household income.
        </div>

        <div v-else class="bursary-list">
          <div v-for="bursary in bursaries" :key="bursary.bursaryId" class="bursary-card">
            <div class="bursary-header">
              <h4>{{ bursary.title }}</h4>
              <span v-if="bursary.isNsfas" class="badge badge-nsfas">NSFAS Approved</span>
              <span v-else class="badge badge-merit">Sector Bursary</span>
            </div>
            <p class="bursary-desc">{{ bursary.description }}</p>
            <div class="bursary-meta">
              <span><strong>Min APS Required:</strong> {{ bursary.minApsScore }}</span>
              <span><strong>Max Household Income:</strong> R{{ Number(bursary.maxHouseholdIncome).toLocaleString() }}</span>
              <span><strong>Field:</strong> {{ bursary.fieldOfStudy }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
.funding-wrapper { max-width: 1200px; margin: 0 auto; padding: 40px 20px; }
.header h1 { color: var(--vt-c-text-main); margin-bottom: 5px; }
.subtitle { color: var(--vt-c-text-muted); margin-bottom: 30px; }
.content-grid { display: grid; grid-template-columns: 350px 1fr; gap: 30px; }
.search-panel, .results-panel { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); height: fit-content; }
.search-panel h3, .results-panel h3 { margin-bottom: 20px; font-size: 18px; display: flex; justify-content: space-between; }
.form-group { margin-bottom: 20px; }
.form-group label { display: block; font-size: 13px; font-weight: bold; color: var(--vt-c-text-muted); margin-bottom: 8px; }
.form-group input, .form-group select { width: 100%; padding: 12px; border: 1px solid #ccc; border-radius: 6px; font-size: 14px; background: white; }
.btn-solid { width: 100%; background-color: var(--vt-c-primary); color: white; border: none; border-radius: 6px; padding: 15px; font-weight: bold; font-size: 14px; cursor: pointer; }
.bursary-list { display: flex; flex-direction: column; gap: 15px; }
.bursary-card { border: 1px solid #eaeaea; border-radius: 8px; padding: 20px; background: #fafafa; }
.bursary-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.bursary-desc { font-size: 13px; color: var(--vt-c-text-muted); margin-bottom: 15px; line-height: 1.5; }
.bursary-meta { display: flex; gap: 20px; flex-wrap: wrap; font-size: 13px; background: white; padding: 10px 15px; border-radius: 4px; border: 1px solid #eee; }
.badge { padding: 4px 10px; border-radius: 12px; font-size: 11px; font-weight: bold; }
.badge-nsfas { background-color: #2e7d32; color: white; }
.badge-merit { background-color: var(--vt-c-primary); color: white; }
.empty-state { text-align: center; padding: 40px 20px; color: var(--vt-c-text-muted); }
@media (max-width: 768px) { .content-grid { grid-template-columns: 1fr; } }
</style>
