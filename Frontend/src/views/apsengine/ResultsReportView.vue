r<script setup>
import { computed } from 'vue'
import { useApsStore } from '../../stores/apsStore'
import { useRouter } from 'vue-router'

const apsStore = useApsStore()
const router = useRouter()

const hasCalculatedResult = computed(
  () => apsStore.calculatedAps !== null && !apsStore.errorMessage,
)

const completedSubjects = computed(() =>
  apsStore.subjects.filter(
    (subject) =>
      subject.subjectCode.trim() && subject.percentage !== null && subject.percentage !== '',
  ),
)

const averageMark = computed(() => {
  if (!hasCalculatedResult.value || !completedSubjects.value.length) return 'N/A'

  const total = completedSubjects.value.reduce(
    (sum, subject) => sum + Number(subject.percentage),
    0,
  )

  return `${Math.round(total / completedSubjects.value.length)}%`
})

const apsScore = computed(() => (hasCalculatedResult.value ? apsStore.calculatedAps : 'N/A'))
const resultStatus = computed(() => (hasCalculatedResult.value ? 'Calculated' : 'Awaiting input'))
const subjectCountLabel = computed(() =>
  completedSubjects.value.length ? `${completedSubjects.value.length} subjects` : 'N/A',
)

const continueToApplications = () => {
  if (hasCalculatedResult.value) router.push('/applications-cart')
}
</script>

<template>
  <main class="page-wrapper">
    <div class="top-nav">
      <button
        class="back-btn"
        type="button"
        aria-label="Back to marks"
        @click="router.push('/marks-recording')"
      >
        &lt;
      </button>
      <h2 class="screen-title">APS Results</h2>
    </div>
    <p class="subtitle">
      {{
        hasCalculatedResult
          ? 'Your academic results have been processed into an APS score.'
          : 'Calculate your APS from the marks input to see your result here.'
      }}
    </p>

    <div class="desktop-layout">
      <div class="left-column">
        <section class="score-section" aria-labelledby="score-heading">
          <div>
            <p class="eyebrow">Your calculated score</p>
            <h1 id="score-heading" class="aps-score">{{ apsScore }}</h1>
            <p class="score-note">This score is based on your submitted subjects and marks.</p>
          </div>
          <span :class="['status-badge', { 'status-pending': !hasCalculatedResult }]">{{
            resultStatus
          }}</span>
        </section>

        <section class="subjects-section" aria-labelledby="subjects-heading">
          <div class="section-heading">
            <div>
              <h3 id="subjects-heading">Submitted subjects</h3>
              <p>These are the marks included in your APS result.</p>
            </div>
          </div>

          <div v-if="completedSubjects.length" class="subject-list">
            <div
              v-for="subject in completedSubjects"
              :key="subject.subjectCode"
              class="subject-row"
            >
              <span class="subject-name">{{ subject.subjectCode }}</span>
              <span class="subject-mark">{{ subject.percentage }}%</span>
            </div>
          </div>
          <p v-else class="empty-state">No marks have been entered yet.</p>
        </section>

        <section class="recommendations-section" aria-labelledby="recommendations-heading">
          <div class="section-heading">
            <div>
              <h3 id="recommendations-heading">Recommended programmes</h3>
              <p>Programme recommendations will appear when the institution module is connected.</p>
            </div>
            <span class="subject-count">N/A</span>
          </div>

          <div class="recommendation-list">
            <div class="recommendation-row">
              <div>
                <strong>Programme recommendation</strong>
                <p>Waiting for institution data</p>
              </div>
              <span class="placeholder-value">N/A</span>
            </div>
            <div class="recommendation-row">
              <div>
                <strong>Programme recommendation</strong>
                <p>Waiting for institution data</p>
              </div>
              <span class="placeholder-value">N/A</span>
            </div>
            <div class="recommendation-row">
              <div>
                <strong>Programme recommendation</strong>
                <p>Waiting for institution data</p>
              </div>
              <span class="placeholder-value">N/A</span>
            </div>
          </div>
        </section>
      </div>

      <div class="right-column">
        <section class="summary-section" aria-labelledby="summary-heading">
          <div class="section-heading">
            <div>
              <h3 id="summary-heading">Results summary</h3>
              <p>A quick view of the marks used in your APS calculation.</p>
            </div>
            <span class="subject-count">{{ subjectCountLabel }}</span>
          </div>

          <div class="stats-grid">
            <div class="stat-card">
              <span class="stat-label">APS score</span>
              <span class="stat-value">{{ apsScore }}</span>
            </div>
            <div class="stat-card">
              <span class="stat-label">Subjects counted</span>
              <span class="stat-value">{{
                hasCalculatedResult ? completedSubjects.length : 'N/A'
              }}</span>
            </div>
            <div class="stat-card">
              <span class="stat-label">Average mark</span>
              <span class="stat-value">{{ averageMark }}</span>
            </div>
            <div class="stat-card">
              <span class="stat-label">Calculation status</span>
              <span
                class="stat-value status-value"
                :class="{ 'status-pending-text': !hasCalculatedResult }"
              >
                {{ hasCalculatedResult ? 'Complete' : 'N/A' }}
              </span>
            </div>
          </div>
        </section>

        <section class="institution-section" aria-labelledby="institution-heading">
          <div class="section-heading">
            <div>
              <h3 id="institution-heading">Institution metrics</h3>
              <p>Institution comparisons will be calculated by the university module.</p>
            </div>
            <span class="status-badge status-pending">Pending</span>
          </div>

          <div class="institution-metrics">
            <div class="metric-row">
              <span>Institution matches</span>
              <strong>N/A</strong>
            </div>
            <div class="metric-row">
              <span>Ready to apply</span>
              <strong>N/A</strong>
            </div>
            <div class="metric-row">
              <span>Needs review</span>
              <strong>N/A</strong>
            </div>
            <div class="metric-row">
              <span>Estimated application fees</span>
              <strong>N/A</strong>
            </div>
          </div>
        </section>

        <section class="next-step-section">
          <h3>Next step</h3>
          <p>
            {{
              hasCalculatedResult
                ? 'Your APS result is ready. You can return to your marks to make changes or continue when the application module is available.'
                : 'Complete and upload at least seven subjects on the marks screen before continuing.'
            }}
          </p>

          <div class="bottom-actions">
            <button class="btn-outline-lg" type="button" @click="router.push('/marks-recording')">
              Edit marks
            </button>
            <button
              class="btn-solid-lg"
              type="button"
              :disabled="!hasCalculatedResult"
              @click="continueToApplications"
            >
              {{ hasCalculatedResult ? 'Continue' : 'Waiting for result' }}
            </button>
          </div>
        </section>
      </div>
    </div>
  </main>
</template>

<style scoped>
.page-wrapper {
  max-width: 1200px;
  min-height: 100vh;
  margin: 0 auto;
  padding: 20px;
  background-color: var(--vt-c-secondary);
  color: var(--vt-c-text-main);
  font-family: Arial, sans-serif;
}

.top-nav {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}
.back-btn {
  width: 30px;
  height: 30px;
  margin-right: 15px;
  border: none;
  border-radius: 4px;
  background-color: var(--vt-c-primary);
  color: white;
  font-weight: bold;
  cursor: pointer;
}
.screen-title {
  font-size: 18px;
  font-weight: bold;
}
.subtitle {
  margin-bottom: 25px;
  color: var(--vt-c-text-muted);
  font-size: 13px;
}

.desktop-layout {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

.left-column,
.right-column {
  min-width: 0;
}

.score-section,
.summary-section,
.subjects-section,
.next-step-section {
  margin-bottom: 20px;
  padding: 25px;
  border-radius: 8px;
}
.score-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #dbe4eb;
}
.eyebrow {
  margin-bottom: 8px;
  color: var(--vt-c-text-muted);
  font-size: 12px;
  font-weight: bold;
}
.aps-score {
  margin-bottom: 8px;
  font-size: clamp(42px, 8vw, 64px);
  line-height: 1;
}
.score-note {
  color: var(--vt-c-text-muted);
  font-size: 12px;
}
.status-badge,
.subject-count {
  padding: 7px 12px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: bold;
  white-space: nowrap;
}
.status-badge {
  background-color: #a4babb;
  color: white;
}
.subject-count {
  background-color: #e6eeee;
  color: var(--vt-c-primary-dark);
}

.summary-section {
  background: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.03);
}
.section-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 15px;
  margin-bottom: 20px;
}
.section-heading h3,
.next-step-section h3 {
  margin-bottom: 6px;
  font-size: 17px;
  font-weight: bold;
}
.section-heading p,
.next-step-section p {
  color: var(--vt-c-text-muted);
  font-size: 12px;
  line-height: 1.5;
}
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
}
.stat-card {
  min-width: 0;
  padding: 15px;
  border: 1px solid #eaeaea;
  border-radius: 6px;
  text-align: center;
}
.stat-label {
  display: block;
  margin-bottom: 7px;
  color: var(--vt-c-text-muted);
  font-size: 11px;
}
.stat-value {
  display: block;
  overflow-wrap: anywhere;
  font-size: 24px;
  font-weight: bold;
}
.status-value {
  font-size: 15px;
  color: #2e7d32;
}
.status-pending {
  background-color: #fff3e0;
  color: #ef6c00;
}

.subjects-section {
  background: white;
}
.recommendations-section,
.institution-section {
  margin-bottom: 20px;
  padding: 25px;
  border-radius: 8px;
  background: white;
}
.recommendations-section {
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.03);
}
.recommendation-list {
  display: grid;
  gap: 10px;
}
.recommendation-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 15px;
  padding: 15px;
  border: 1px solid #eaeaea;
  border-radius: 6px;
}
.recommendation-row strong {
  display: block;
  margin-bottom: 4px;
  font-size: 13px;
}
.recommendation-row p {
  color: var(--vt-c-text-muted);
  font-size: 11px;
}
.placeholder-value {
  color: var(--vt-c-text-muted);
  font-size: 13px;
  font-weight: bold;
  white-space: nowrap;
}
.institution-section {
  background-color: #dbe4eb;
}
.institution-metrics {
  display: grid;
  gap: 0;
  background: white;
  border-radius: 6px;
  padding: 0 15px;
}
.metric-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 15px;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
  font-size: 13px;
}
.metric-row:last-child {
  border-bottom: none;
}
.metric-row strong {
  color: var(--vt-c-text-muted);
  font-size: 13px;
}
.status-pending-text {
  color: var(--vt-c-text-muted);
}
.subject-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px 15px;
}
.subject-row {
  display: flex;
  justify-content: space-between;
  gap: 15px;
  padding: 13px 0;
  border-bottom: 1px solid #eee;
}
.subject-name {
  font-size: 13px;
  font-weight: bold;
  overflow-wrap: anywhere;
}
.subject-mark {
  color: var(--vt-c-primary-dark);
  font-size: 13px;
  font-weight: bold;
}
.empty-state {
  color: var(--vt-c-text-muted);
  font-size: 13px;
}

.next-step-section {
  background-color: #dbe4eb;
}
.next-step-section p {
  max-width: 680px;
  margin-bottom: 20px;
}
.bottom-actions {
  display: flex;
  gap: 15px;
  max-width: 520px;
}
.btn-outline-lg,
.btn-solid-lg {
  flex: 1;
  padding: 13px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: bold;
  cursor: pointer;
}
.btn-outline-lg {
  border: 1px solid #ccc;
  background: white;
  color: var(--vt-c-text-main);
}
.btn-solid-lg {
  border: none;
  background-color: var(--vt-c-primary);
  color: white;
}
.btn-outline-lg:hover {
  border-color: var(--vt-c-primary);
  color: var(--vt-c-primary);
}
.btn-solid-lg:hover {
  background-color: var(--vt-c-primary-dark);
}
.btn-solid-lg:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@media (min-width: 768px) {
  .page-wrapper {
    padding: 40px;
  }
}

@media (min-width: 1024px) {
  .desktop-layout {
    grid-template-columns: 1.5fr 1fr;
    gap: 40px;
    align-items: start;
  }
  .right-column {
    position: sticky;
    top: 40px;
  }
}

@media (max-width: 767px) {
  .score-section {
    align-items: flex-start;
    flex-direction: column;
    gap: 18px;
  }
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 520px) {
  .page-wrapper {
    padding: 15px;
  }
  .score-section,
  .summary-section,
  .subjects-section,
  .recommendations-section,
  .institution-section,
  .next-step-section {
    padding: 18px;
  }
  .section-heading {
    align-items: stretch;
    flex-direction: column;
  }
  .subject-list {
    grid-template-columns: 1fr;
  }
  .bottom-actions {
    flex-direction: column;
    max-width: none;
  }
}
</style>
