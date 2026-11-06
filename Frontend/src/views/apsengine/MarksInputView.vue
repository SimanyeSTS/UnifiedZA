<script setup>
import { computed, ref, onMounted } from 'vue'
import { useApsStore } from '../../stores/apsStore'
import { useRouter } from 'vue-router'
import {
  examTypes,
  graduationYears,
  provinces,
  schoolsByProvince,
} from '../../data/educationOptions'

const apsStore = useApsStore()
const router = useRouter()

const resultDetails = ref({
  examType: 'NSC Final',
  province: 'Western Cape',
  schoolName: 'Wynberg Boys High School',
  graduationYear: new Date().getFullYear(),
})

const isUploaded = ref(true)

onMounted(async () => {
  await apsStore.calculateAps()
})

const completedSubjectsCount = computed(
  () =>
    apsStore.subjects.filter(
      (subject) =>
        subject.subjectCode.trim() && subject.percentage !== null && subject.percentage !== '',
    ).length,
)

const subjectCountStatus = computed(() => {
  if (completedSubjectsCount.value >= 7) return 'Done'
  return 'Missing'
})

const mathStatus = computed(() => {
  const codes = apsStore.subjects.map((s) => s.subjectCode.trim().toUpperCase())
  const hasMath = codes.some((c) => c === 'MATH' || c === 'MATHEMATICS')
  const hasMathLit = codes.some((c) => c === 'MLIT' || c.includes('LIT'))

  if (hasMath && hasMathLit) return 'Error'
  if (hasMath || hasMathLit) return 'Done'
  return 'Watch'
})

const uploadStatus = computed(() => {
  return isUploaded.value ? 'Done' : 'Missing'
})

const handleCalculate = async () => {
  await apsStore.calculateAps()
}

const handleUpload = async () => {
  if (subjectCountStatus.value !== 'Done') {
    apsStore.errorMessage = 'Add marks for at least 7 subjects before uploading results.'
    return
  }
  await apsStore.calculateAps()
  isUploaded.value = true
}

const proceedToResults = async () => {
  const ok = await apsStore.calculateAps()
  if (ok && apsStore.calculatedAps !== null) {
    isUploaded.value = true
    router.push('/marks-results')
  }
}
</script>

<template>
  <main class="page-wrapper">
    <!-- Top Navigation / Progress -->
    <div class="top-nav">
      <button class="back-btn" @click="router.push('/')">&lt;</button>
      <h2 class="screen-title">Academic Results</h2>
    </div>

    <div class="progress-container">
      <div class="progress-text">
        <span>Profile &rarr; Results &rarr; APS &rarr; Selection</span>
        <span class="percent">{{ apsStore.calculatedAps ? '75% complete' : '52% complete' }}</span>
      </div>
      <div class="progress-bar">
        <div class="progress-fill" :style="{ width: apsStore.calculatedAps ? '75%' : '52%' }"></div>
      </div>
    </div>

    <!-- Responsive Desktop Grid -->
    <div class="desktop-layout">
      <!-- Left Column: Inputs -->
      <div class="left-column">
        <div class="header-section">
          <div class="header-text">
            <h3>Enter NSC subjects</h3>
            <p>
              Add your latest Grade 12 or trial marks once. Unified ZA will use them to calculate
              APS across institutions.
            </p>
          </div>
          <div class="auto-save-badge">
            <span>Auto-save<br />on exit</span>
          </div>
        </div>

        <!-- Stepper -->
        <div class="stepper-grid">
          <div class="step-card">
            <div class="step-num">1</div>
            Profile
          </div>
          <div class="step-card active">
            <div class="step-num active-num">2</div>
            Results
          </div>
          <div class="step-card" @click="proceedToResults" style="cursor: pointer;">
            <div class="step-num">3</div>
            APS
          </div>
          <div class="step-card" @click="router.push('/search')" style="cursor: pointer;">
            <div class="step-num">4</div>
            Apply
          </div>
        </div>

        <!-- Demographics Grid -->
        <div class="demo-grid">
          <div class="input-group">
            <label for="exam-type">Exam type:</label>
            <select id="exam-type" v-model="resultDetails.examType">
              <option v-for="examType in examTypes" :key="examType">{{ examType }}</option>
            </select>
          </div>
          <div class="input-group">
            <label for="province">Province:</label>
            <select id="province" v-model="resultDetails.province">
              <option v-for="province in provinces" :key="province">{{ province }}</option>
            </select>
          </div>
          <div class="input-group">
            <label for="school-name">School name:</label>
            <input
              id="school-name"
              v-model="resultDetails.schoolName"
              list="south-african-schools"
              placeholder="Search your high school"
            />
            <datalist id="south-african-schools">
              <option
                v-for="school in schoolsByProvince[resultDetails.province]"
                :key="school"
                :value="school"
              />
            </datalist>
          </div>
          <div class="input-group">
            <label for="graduation-year">Graduation year:</label>
            <select id="graduation-year" v-model="resultDetails.graduationYear">
              <option v-for="year in graduationYears" :key="year" :value="year">{{ year }}</option>
            </select>
          </div>
        </div>

        <!-- Subjects Section -->
        <div class="section-header">
          <h3>Subjects and marks</h3>
          <button class="btn-add-subject" @click="apsStore.addSubject">+ Add subject</button>
        </div>

        <div class="subjects-list">
          <div v-for="(subject, index) in apsStore.subjects" :key="index" class="subject-card">
            <div class="subject-card-header">
              <input
                type="text"
                v-model="subject.subjectCode"
                placeholder="Subject Code (e.g. ENGHL, AFRFAL, MATH)"
                class="subject-name-input"
                @input="handleCalculate"
              />
              <div class="score-badge" v-if="subject.percentage !== null && subject.percentage !== ''">
                {{ subject.percentage }}%
              </div>
            </div>
            <div class="subject-card-body">
              <div class="input-group">
                <label>APS Points:</label>
                <input
                  type="text"
                  :value="
                    subject.subjectCode.trim().toUpperCase() === 'LO'
                      ? '0 (Excluded)'
                      : apsStore.getPointsForPercentage(subject.percentage)
                  "
                  disabled
                  style="background: #f4f6f8; font-weight: bold; color: #3a5a5a;"
                />
              </div>
              <div class="input-group">
                <label>Percent (%):</label>
                <input
                  type="number"
                  min="0"
                  max="100"
                  v-model="subject.percentage"
                  placeholder="0"
                  @input="handleCalculate"
                />
              </div>
              <button
                class="btn-remove-sm"
                v-if="apsStore.subjects.length > 1"
                @click="apsStore.removeSubject(index)"
              >
                X
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Column: Status & Preview -->
      <div class="right-column">
        <!-- APS Preview -->
        <div class="aps-preview-box">
          <div class="aps-preview-header">
            <h3>Your APS preview</h3>
            <span class="badge-updated">
              {{ apsStore.calculatedAps === null ? 'Waiting' : 'Updated ✓' }}
            </span>
          </div>
          <div class="aps-grid">
            <div class="aps-card aps-card-primary">
              <span class="uni-label">Calculated APS</span>
              <span class="uni-score">{{ apsStore.calculatedAps ?? 'N/A' }}</span>
            </div>
            <div class="aps-card">
              <span class="uni-label">Subjects counted</span>
              <span class="uni-score">{{ completedSubjectsCount || 'N/A' }}</span>
            </div>
          </div>
        </div>

        <!-- Dynamic Validation -->
        <div class="validation-section">
          <h3>Validation</h3>
          <div class="val-row">
            <span>Minimum 7 subjects added</span>
            <span :class="['badge', 'badge-' + subjectCountStatus.toLowerCase()]">
              {{ subjectCountStatus }}
            </span>
          </div>
          <div class="val-row">
            <span>Math requirement flagged</span>
            <span :class="['badge', 'badge-' + mathStatus.toLowerCase()]">{{ mathStatus }}</span>
          </div>
          <div class="val-row">
            <span>Results upload</span>
            <span :class="['badge', 'badge-' + uploadStatus.toLowerCase()]">
              {{ uploadStatus }}
            </span>
          </div>

          <div class="error-text" v-if="apsStore.errorMessage">{{ apsStore.errorMessage }}</div>
        </div>

        <!-- Actions -->
        <div class="bottom-actions">
          <button
            class="btn-outline"
            @click="handleUpload"
            :disabled="subjectCountStatus !== 'Done'"
          >
            {{ isUploaded ? 'Recalculate APS' : 'Upload results' }}
          </button>
          <button
            class="btn-solid"
            @click="proceedToResults"
            :disabled="apsStore.isLoading || subjectCountStatus !== 'Done' || !!apsStore.errorMessage"
          >
            {{ apsStore.isLoading ? 'Processing...' : 'Continue to APS Results \u2192' }}
          </button>
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
/* Same styling preserved */
.page-wrapper { max-width: 1200px; margin: 0 auto; background-color: var(--vt-c-secondary); padding: 40px; font-family: Arial, sans-serif; color: var(--vt-c-text-main); min-height: 100vh; }
.desktop-layout { display: grid; grid-template-columns: 1.5fr 1fr; gap: 40px; align-items: start; }
.right-column { position: sticky; top: 40px; }
.top-nav { display: flex; align-items: center; margin-bottom: 15px; }
.back-btn { background-color: var(--vt-c-primary); color: white; border: none; border-radius: 4px; width: 30px; height: 30px; font-weight: bold; margin-right: 15px; cursor: pointer; }
.screen-title { font-size: 18px; font-weight: bold; }
.progress-container { margin-bottom: 25px; }
.progress-text { display: flex; justify-content: space-between; font-size: 12px; color: var(--vt-c-text-muted); margin-bottom: 5px; }
.progress-bar { height: 8px; background-color: #d1d9d9; border-radius: 4px; overflow: hidden; }
.progress-fill { height: 100%; background-color: var(--vt-c-primary); transition: width 0.3s ease; }
.header-section { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 25px; }
.header-text h3 { font-size: 20px; font-weight: bold; margin-bottom: 8px; }
.header-text p { font-size: 13px; color: var(--vt-c-text-muted); max-width: 350px; line-height: 1.5; }
.auto-save-badge { background-color: #c9dad8; color: var(--vt-c-primary-dark); font-size: 11px; width: 70px; height: 70px; border-radius: 50%; display: flex; align-items: center; justify-content: center; text-align: center; font-weight: bold; }
.stepper-grid { display: flex; gap: 10px; margin-bottom: 30px; }
.step-card { flex: 1; background: white; border-radius: 6px; padding: 15px 5px; text-align: center; font-size: 12px; font-weight: bold; box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05); }
.step-num { width: 24px; height: 24px; background-color: #e0e0e0; border-radius: 50%; margin: 0 auto 8px auto; display: flex; align-items: center; justify-content: center; color: var(--vt-c-text-main); }
.step-num.active-num { background-color: var(--vt-c-primary); color: white; }
.demo-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; margin-bottom: 30px; }
.input-group label { display: block; font-size: 11px; font-weight: bold; margin-bottom: 5px; color: var(--vt-c-text-muted); }
.input-group select, .input-group input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 6px; font-size: 14px; background: white; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.section-header h3 { font-size: 18px; font-weight: bold; }
.btn-add-subject { background-color: #a4babb; color: white; border: none; padding: 8px 16px; border-radius: 12px; font-size: 13px; font-weight: bold; cursor: pointer; }
.btn-add-subject:hover { background-color: var(--vt-c-primary); }
.subjects-list { margin-bottom: 25px; }
.subject-card { background: white; border-radius: 8px; padding: 20px; margin-bottom: 15px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.03); border: 1px solid #eaeaea; }
.subject-card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.subject-name-input { font-weight: bold; font-size: 16px; border: none; border-bottom: 1px dashed #ccc; outline: none; width: 70%; color: var(--vt-c-primary-dark); }
.score-badge { background-color: #c9dad8; color: var(--vt-c-primary-dark); font-size: 13px; font-weight: bold; width: 42px; height: 42px; border-radius: 50%; display: flex; align-items: center; justify-content: center; }
.subject-card-body { display: flex; gap: 15px; align-items: flex-end; }
.btn-remove-sm { background: #ff4c4c; color: white; border: none; border-radius: 4px; padding: 10px 15px; cursor: pointer; margin-bottom: 2px; font-weight: bold; }
.aps-preview-box { background-color: #dbe4eb; padding: 25px; border-radius: 8px; margin-bottom: 30px; }
.aps-preview-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.aps-preview-header h3 { font-size: 16px; font-weight: bold; }
.badge-updated { background-color: var(--vt-c-primary); color: white; padding: 6px 12px; border-radius: 12px; font-size: 11px; font-weight: bold; }
.aps-grid { display: flex; gap: 15px; }
.aps-card { flex: 1; background: white; border-radius: 6px; padding: 20px 10px; text-align: center; }
.uni-label { display: block; font-size: 12px; color: var(--vt-c-text-muted); margin-bottom: 8px; }
.uni-score { font-size: 28px; font-weight: bold; color: var(--vt-c-primary-dark); }
.validation-section { background: white; padding: 25px; border-radius: 8px; margin-bottom: 30px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.03); }
.validation-section h3 { font-size: 16px; font-weight: bold; margin-bottom: 20px; }
.val-row { display: flex; justify-content: space-between; align-items: center; padding: 15px 0; border-bottom: 1px solid #eee; font-size: 14px; }
.val-row:last-child { border-bottom: none; }
.badge { padding: 6px 12px; border-radius: 12px; font-size: 11px; font-weight: bold; }
.badge-done { background-color: #d4e7d4; color: #2e7d32; }
.badge-watch { background-color: #fff3e0; color: #ef6c00; }
.badge-missing { background-color: #ffebee; color: #c62828; }
.badge-error { background-color: #d32f2f; color: white; }
.error-text { margin-top: 15px; color: #d32f2f; font-size: 13px; font-weight: bold; }
.bottom-actions { display: flex; gap: 15px; margin-bottom: 20px; }
.btn-outline { flex: 1; background: white; color: var(--vt-c-text-main); border: 2px solid #ccc; border-radius: 6px; padding: 15px; font-weight: bold; font-size: 14px; cursor: pointer; }
.btn-outline:not(:disabled):hover { border-color: var(--vt-c-primary); color: var(--vt-c-primary); }
.btn-solid { flex: 1; background-color: var(--vt-c-primary); color: white; border: none; border-radius: 6px; padding: 15px; font-weight: bold; font-size: 14px; cursor: pointer; }
.btn-solid:not(:disabled):hover { background-color: var(--vt-c-primary-dark); }
.btn-solid:disabled { opacity: 0.5; cursor: not-allowed; }
@media (max-width: 900px) { .desktop-layout { grid-template-columns: 1fr; } .demo-grid { grid-template-columns: 1fr 1fr; } }
</style>
