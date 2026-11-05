<script setup>
import { ref, onMounted } from 'vue'
import { useApsStore } from '../../stores/apsStore'

const apsStore = useApsStore()

const searchCriteria = ref({
  householdIncome: 250000,
  baseAps: apsStore.calculatedAps || 30, // Default to calculated APS if available
  fieldOfStudy: 'ALL'
})

const bursaries = ref([])
const isLoading = ref(false)
const errorMessage = ref('')
const hasSearched = ref(false)

const findMatches = async () => {
  isLoading.value = true
  errorMessage.value = ''
  hasSearched.value = true
  bursaries.value = []

  try {
    const { householdIncome, baseAps, fieldOfStudy } = searchCriteria.value
    // Hitting Kgalelo's Spring Boot API on port 8081
    const response = await fetch(
      `http://localhost:8081/api/bursaries/match?householdIncome=${householdIncome}&baseAps=${baseAps}&fieldOfStudy=${encodeURIComponent(fieldOfStudy)}`
    )

    if (!response.ok) {
      throw new Error('Failed to fetch matching bursaries.')
    }

    bursaries.value = await response.json()
  } catch (error) {
    errorMessage.value = error.message || 'Could not connect to the Funding API.'
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  // Automatically run a search if they already calculated their APS
  if (apsStore.calculatedAps !== null) {
    findMatches()
  }
})
</script>

<template>
  <main class="funding-wrapper">
    <div class="header">
      <h1>Bursary & Funding Matcher</h1>
      <p class="subtitle">Find financial aid opportunities tailored to your APS and household income.</p>
    </div>

    <div class="content-grid">
      <!-- Left Column: Search Form -->
      <div class="search-panel">
        <h3>Match Criteria</h3>

        <div class="form-group">
          <label>Annual Household Income (ZAR)</label>
          <input
            type="number"
            v-model="searchCriteria.householdIncome"
            placeholder="e.g. 350000"
            min="0"
          />
        </div>

        <div class="form-group">
          <label>APS Score</label>
          <input
            type="number"
            v-model="searchCriteria.baseAps"
            placeholder="e.g. 35"
            min="0"
            max="60"
          />
        </div>

        <div class="form-group">
          <label>Field of Study</label>
          <input
            type="text"
            v-model="searchCriteria.fieldOfStudy"
            placeholder="e.g. ALL or Information Technology"
          />
        </div>

        <button class="btn-solid" @click="findMatches" :disabled="isLoading">
          {{ isLoading ? 'Searching...' : 'Find Matches' }}
        </button>
      </div>

      <!-- Right Column: Results -->
      <div class="results-panel">
        <h3>Available Matches <span v-if="hasSearched" class="result-count">({{ bursaries.length }})</span></h3>

        <div v-if="errorMessage" class="error-banner">
          {{ errorMessage }}
        </div>

        <div v-else-if="isLoading" class="loading-state">
          Analyzing funding databases...
        </div>

        <div v-else-if="hasSearched && bursaries.length === 0" class="empty-state">
          No matching bursaries found for your criteria. Try adjusting your income or field of study.
        </div>

        <div v-else-if="!hasSearched" class="empty-state">
          Enter your details and click "Find Matches" to see eligible funding.
        </div>

        <div v-else class="bursary-list">
          <div v-for="bursary in bursaries" :key="bursary.bursaryId" class="bursary-card">
            <div class="bursary-header">
              <h4>{{ bursary.title }}</h4>
              <span v-if="bursary.isNsfas" class="badge badge-nsfas">NSFAS Approved</span>
            </div>
            <p class="bursary-desc">{{ bursary.description || 'No description provided.' }}</p>
            <div class="bursary-meta">
              <span><strong>Min APS:</strong> {{ bursary.minApsScore }}</span>
              <span><strong>Max Income:</strong> R{{ bursary.maxHouseholdIncome.toLocaleString() }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
.funding-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.header h1 {
  color: var(--vt-c-text-main);
  margin-bottom: 5px;
}

.subtitle {
  color: var(--vt-c-text-muted);
  margin-bottom: 30px;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 30px;
}

@media (min-width: 768px) {
  .content-grid {
    grid-template-columns: 350px 1fr;
  }
}

/* Search Panel */
.search-panel {
  background: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.05);
  height: fit-content;
}

.search-panel h3 {
  margin-bottom: 20px;
  font-size: 18px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: bold;
  color: var(--vt-c-text-muted);
  margin-bottom: 8px;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 14px;
}

.btn-solid {
  width: 100%;
  background-color: var(--vt-c-primary);
  color: white;
  border: none;
  border-radius: 6px;
  padding: 15px;
  font-weight: bold;
  font-size: 14px;
  cursor: pointer;
  transition: 0.2s;
}

.btn-solid:hover:not(:disabled) {
  background-color: var(--vt-c-primary-dark);
}

.btn-solid:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* Results Panel */
.results-panel {
  background: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.05);
}

.results-panel h3 {
  margin-bottom: 20px;
  font-size: 18px;
  display: flex;
  justify-content: space-between;
}

.result-count {
  color: var(--vt-c-primary);
}

.empty-state, .loading-state {
  text-align: center;
  padding: 40px 20px;
  color: var(--vt-c-text-muted);
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px dashed #ccc;
}

.error-banner {
  background-color: #fde2e2;
  color: #c53030;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

/* Bursary Cards */
.bursary-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.bursary-card {
  border: 1px solid #eaeaea;
  border-radius: 8px;
  padding: 20px;
  background: #fafafa;
  transition: 0.2s;
}

.bursary-card:hover {
  border-color: var(--vt-c-primary);
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.bursary-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.bursary-header h4 {
  font-size: 16px;
  font-weight: bold;
  color: var(--vt-c-text-main);
  margin: 0;
  padding-right: 15px;
}

.bursary-desc {
  font-size: 13px;
  color: var(--vt-c-text-muted);
  line-height: 1.5;
  margin-bottom: 15px;
}

.bursary-meta {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: var(--vt-c-text-main);
  background: white;
  padding: 10px 15px;
  border-radius: 4px;
  border: 1px solid #eee;
}

.badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: bold;
  white-space: nowrap;
}

.badge-nsfas {
  background-color: #2e7d32;
  color: white;
}
</style>
