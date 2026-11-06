<script setup>
import { computed } from 'vue'
import { useApsStore } from '../../stores/apsStore'
import { useRouter } from 'vue-router'
import { institutions, allCoursesFlat } from '../../data/institutionsData'

const apsStore = useApsStore()
const router = useRouter()

const hasCalculatedResult = computed(
  () => apsStore.calculatedAps !== null && !apsStore.errorMessage
)

const completedSubjects = computed(() =>
  apsStore.subjects.filter(
    (subject) =>
      subject.subjectCode.trim() && subject.percentage !== null && subject.percentage !== ''
  )
)

const averageMark = computed(() => {
  if (!hasCalculatedResult.value || !completedSubjects.value.length) return 'N/A'
  const total = completedSubjects.value.reduce(
    (sum, subject) => sum + Number(subject.percentage),
    0
  )
  return `${Math.round(total / completedSubjects.value.length)}%`
})

const apsScore = computed(() => (hasCalculatedResult.value ? apsStore.calculatedAps : 'N/A'))
const resultStatus = computed(() => (hasCalculatedResult.value ? 'Calculated' : 'Awaiting input'))
const subjectCountLabel = computed(() =>
  completedSubjects.value.length ? `${completedSubjects.value.length} subjects` : 'N/A'
)

// Connected to Cebe's Institution Data!
const eligibleCourses = computed(() => {
  if (!hasCalculatedResult.value) return []
  return allCoursesFlat
    .filter((c) => Number(apsStore.calculatedAps) >= c.aps)
    .sort((a, b) => b.aps - a.aps)
})

const matchingInstitutionsCount = computed(() => {
  if (!hasCalculatedResult.value) return 'N/A'
  return institutions.filter((i) => Number(apsStore.calculatedAps) >= i.aps).length
})

const needsReviewCount = computed(() => {
  if (!hasCalculatedResult.value) return 'N/A'
  return allCoursesFlat.length - eligibleCourses.value.length
})
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
          ? 'Your academic results have been processed and matched against South African universities.'
          : 'Calculate your APS from the marks input to see your result here.'
      }}
    </p>

    <div class="desktop-layout">
      <div class="left-column">
        <section class="score-section" aria-labelledby="score-heading">
          <div>
            <p class="eyebrow">Your calculated score</p>
            <h1 id="score-heading" class="aps-score">{{ apsScore }}</h1>
            <p class="score-note">This score is based on your top 6 NSC subjects (Life Orientation excluded).</p>
          </div>
          <span :class="['status-badge', { 'status-pending': !hasCalculatedResult }]">
            {{ resultStatus }}
          </span>
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
              <h3 id="recommendations-heading">Top Recommended Programmes</h3>
              <p>Programmes you immediately qualify for based on your APS of {{ apsScore }}.</p>
            </div>
            <span class="subject-count">{{ eligibleCourses.length }} matches</span>
          </div>

          <div v-if="eligibleCourses.length" class="recommendation-list">
            <div
              v-for="course in eligibleCourses.slice(0, 5)"
              :key="course.courseID"
              class="recommendation-row"
            >
              <div>
                <strong>{{ course.name }}</strong>
                <p>{{ course.institutionName }} ({{ course.faculty }})</p>
              </div>
              <span class="qualify-badge">Min APS: {{ course.aps }} ✓</span>
            </div>
          </div>
          <p v-else class="empty-state">
            Calculate your APS first to see matching university programmes.
          </p>
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
              <p>Live comparison against our 6 partner universities and 21 programmes.</p>
            </div>
            <span class="status-badge">{{ hasCalculatedResult ? 'Matched' : 'Pending' }}</span>
          </div>

          <div class="institution-metrics">
            <div class="metric-row">
              <span>Qualifying Universities</span>
              <strong>{{ matchingInstitutionsCount }} / {{ institutions.length }}</strong>
            </div>
            <div class="metric-row">
              <span>Programmes Ready to Apply</span>
              <strong style="color: #2e7d32;">{{ hasCalculatedResult ? eligibleCourses.length : 'N/A' }}</strong>
            </div>
            <div class="metric-row">
              <span>Programmes Below Requirement</span>
              <strong style="color: #c53030;">{{ needsReviewCount }}</strong>
            </div>
            <div class="metric-row">
              <span>Unified Cart Limit</span>
              <strong>Max 3 Programmes</strong>
            </div>
          </div>
        </section>

        <section class="next-step-section">
          <h3>Next step</h3>
          <p>
            {{
              hasCalculatedResult
                ? 'Your APS result is ready! Proceed to the Universities catalog to see how you stack up and add courses to your application cart.'
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
              @click="router.push('/search')"
            >
              {{ hasCalculatedResult ? 'Compare Universities & Apply' : 'Waiting for result' }}
            </button>
          </div>
        </section>
      </div>
    </div>
  </main>
</template>

<style scoped>
/* Same clean styling with qualify badge */
.page-wrapper { max-width: 1200px; min-height: 100vh; margin: 0 auto; padding: 40px 20px; background-color: var(--vt-c-secondary); color: var(--vt-c-text-main); font-family: Arial, sans-serif; }
.top-nav { display: flex; align-items: center; margin-bottom: 5px; }
.back-btn { width: 30px; height: 30px; margin-right: 15px; border: none; border-radius: 4px; background-color: var(--vt-c-primary); color: white; font-weight: bold; cursor: pointer; }
.screen-title { font-size: 18px; font-weight: bold; }
.subtitle { margin-bottom: 25px; color: var(--vt-c-text-muted); font-size: 13px; }
.desktop-layout { display: grid; grid-template-columns: 1.5fr 1fr; gap: 40px; align-items: start; }
.score-section, .summary-section, .subjects-section, .next-step-section, .recommendations-section, .institution-section { margin-bottom: 20px; padding: 25px; border-radius: 8px; background: white; }
.score-section, .institution-section, .next-step-section { background-color: #dbe4eb; }
.score-section { display: flex; align-items: center; justify-content: space-between; }
.eyebrow { margin-bottom: 8px; color: var(--vt-c-text-muted); font-size: 12px; font-weight: bold; }
.aps-score { margin-bottom: 8px; font-size: 56px; line-height: 1; }
.score-note { color: var(--vt-c-text-muted); font-size: 12px; }
.status-badge, .subject-count { padding: 7px 12px; border-radius: 12px; font-size: 11px; font-weight: bold; background-color: var(--vt-c-primary); color: white; }
.section-heading { display: flex; align-items: flex-start; justify-content: space-between; gap: 15px; margin-bottom: 20px; }
.section-heading h3, .next-step-section h3 { margin-bottom: 6px; font-size: 17px; font-weight: bold; }
.section-heading p, .next-step-section p { color: var(--vt-c-text-muted); font-size: 12px; line-height: 1.5; }
.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; }
.stat-card { padding: 15px; border: 1px solid #eaeaea; border-radius: 6px; text-align: center; }
.stat-label { display: block; margin-bottom: 7px; color: var(--vt-c-text-muted); font-size: 11px; }
.stat-value { display: block; font-size: 22px; font-weight: bold; }
.status-value { font-size: 15px; color: #2e7d32; }
.subject-list { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px 15px; }
.subject-row { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px solid #eee; font-size: 13px; font-weight: bold; }
.recommendation-list { display: grid; gap: 10px; }
.recommendation-row { display: flex; align-items: center; justify-content: space-between; padding: 14px; border: 1px solid #eaeaea; border-radius: 6px; }
.recommendation-row strong { display: block; font-size: 14px; margin-bottom: 3px; }
.recommendation-row p { color: var(--vt-c-text-muted); font-size: 12px; }
.qualify-badge { background: #dcf5e3; color: #2f855a; padding: 5px 10px; border-radius: 12px; font-size: 12px; font-weight: bold; white-space: nowrap; }
.institution-metrics { background: white; border-radius: 6px; padding: 0 15px; }
.metric-row { display: flex; justify-content: space-between; padding: 14px 0; border-bottom: 1px solid #eee; font-size: 13px; }
.metric-row:last-child { border-bottom: none; }
.bottom-actions { display: flex; gap: 15px; margin-top: 15px; }
.btn-outline-lg, .btn-solid-lg { flex: 1; padding: 13px; border-radius: 6px; font-size: 13px; font-weight: bold; cursor: pointer; }
.btn-outline-lg { border: 1px solid #ccc; background: white; color: var(--vt-c-text-main); }
.btn-solid-lg { border: none; background-color: var(--vt-c-primary); color: white; }
@media (max-width: 900px) { .desktop-layout { grid-template-columns: 1fr; } .stats-grid { grid-template-columns: repeat(2, 1fr); } }
</style>
