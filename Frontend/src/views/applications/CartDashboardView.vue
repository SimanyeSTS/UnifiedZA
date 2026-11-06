<script setup>
import { ref, onMounted } from 'vue'
import { useCartStore } from '../../stores/cartStore'
import { useAuthStore } from '../../stores/authStore'
import { useRouter } from 'vue-router'

const cartStore = useCartStore()
const authStore = useAuthStore()
const router = useRouter()

const activeTimelineAppId = ref(null)

onMounted(async () => {
  cartStore.clearMessages()
  await cartStore.loadDashboardData()
})

const handleWithdraw = async (applicationId) => {
  if (confirm('Are you sure you want to withdraw this application? This cannot be undone.')) {
    await cartStore.withdrawApplication(applicationId)
  }
}

const toggleTimeline = async (applicationId) => {
  if (activeTimelineAppId.value === applicationId) {
    activeTimelineAppId.value = null
    return
  }
  activeTimelineAppId.value = applicationId
  await cartStore.fetchApplicationHistory(applicationId)
}

const formatDate = (dateString) => {
  if (!dateString) return 'Pending'
  const options = { year: 'numeric', month: 'long', day: 'numeric' }
  return new Date(dateString).toLocaleDateString('en-ZA', options)
}
</script>

<template>
  <main class="dashboard-wrapper">
    <div class="header-row">
      <div>
        <h1>
          Welcome back,
          {{ authStore.user ? `${authStore.user.firstName} ${authStore.user.lastName}` : 'Student' }}!
        </h1>
        <p class="subtitle">
          Manage your saved university courses, submit applications, and track live admission decisions.
        </p>
      </div>
      <button class="btn-browse" @click="router.push('/search')">
        + Browse More Universities
      </button>
    </div>

    <!-- Stats Cards -->
    <div class="stats-grid">
      <div class="stat-card">
        <span class="stat-label">Courses in Cart</span>
        <span class="stat-value">{{ cartStore.savedCourses.length }} / 3</span>
      </div>
      <div class="stat-card">
        <span class="stat-label">Total Submitted</span>
        <span class="stat-value">{{ cartStore.stats.totalApplications }}</span>
      </div>
      <div class="stat-card">
        <span class="stat-label">Pending Review</span>
        <span class="stat-value text-warning">{{ cartStore.stats.pending }}</span>
      </div>
      <div class="stat-card">
        <span class="stat-label">Accepted Offers</span>
        <span class="stat-value text-success">{{ cartStore.stats.accepted }}</span>
      </div>
    </div>

    <div v-if="cartStore.successMessage" class="success-banner">
      {{ cartStore.successMessage }}
    </div>
    <div v-if="cartStore.errorMessage" class="error-banner">
      {{ cartStore.errorMessage }}
    </div>

    <!-- 1. SAVED COURSES (CART) CRUD PANEL -->
    <div class="panel cart-panel">
      <div class="panel-header">
        <div>
          <h2>Unified Application Cart ({{ cartStore.savedCourses.length }} of 3 max)</h2>
          <p class="panel-sub">Courses you selected from the Universities catalog before final submission.</p>
        </div>
        <button
          v-if="cartStore.savedCourses.length > 0"
          class="btn-submit-all"
          @click="cartStore.submitAllInCart()"
        >
          Submit All ({{ cartStore.savedCourses.length }}) Applications
        </button>
      </div>

      <div v-if="cartStore.savedCourses.length === 0" class="empty-state">
        Your application cart is empty. Head over to the
        <RouterLink to="/search">Universities Catalog</RouterLink> to add programmes!
      </div>

      <div v-else class="saved-grid">
        <div v-for="item in cartStore.savedCourses" :key="item.savedCourseID" class="saved-card">
          <div>
            <h3>{{ item.courseName }}</h3>
            <p class="inst-name">{{ item.institutionName }}</p>
            <span class="meta-tag">Min APS: {{ item.apsRequirement }} | Intake: {{ item.intakeYear }}</span>
          </div>
          <div class="saved-actions">
            <button
              class="btn-apply-single"
              @click="cartStore.submitApplication(item.courseID, item.savedCourseID)"
            >
              Submit Application
            </button>
            <button class="btn-remove" @click="cartStore.removeSavedCourse(item.savedCourseID)">
              Remove
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 2. SUBMITTED APPLICATIONS & STATUS TRACKER TABLE -->
    <div class="panel">
      <div class="panel-header">
        <div>
          <h2>Submitted Applications</h2>
          <p class="panel-sub">Click "Track Status" to view the live stage timeline or "Withdraw" to cancel.</p>
        </div>
      </div>

      <div v-if="cartStore.isLoading" class="empty-state">Loading applications...</div>

      <table v-else class="app-table">
        <thead>
          <tr>
            <th>Course</th>
            <th>Institution</th>
            <th>Application Date</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="cartStore.applications.length === 0">
            <td colspan="5" class="empty-state">You haven't submitted any applications yet.</td>
          </tr>
          <template v-for="app in cartStore.applications" :key="app.applicationID">
            <tr>
              <td class="fw-bold">{{ app.courseName }}</td>
              <td>{{ app.institutionName }}</td>
              <td>{{ formatDate(app.applicationDate) }}</td>
              <td>
                <span :class="['status-badge', 'status-' + app.status.toLowerCase().replace(/\s+/g, '-')]">
                  {{ app.status }}
                </span>
              </td>
              <td class="action-cell">
                <button class="btn-track" @click="toggleTimeline(app.applicationID)">
                  {{ activeTimelineAppId === app.applicationID ? 'Hide Tracker' : 'Track Status' }}
                </button>
                <button class="btn-withdraw" @click="handleWithdraw(app.applicationID)">
                  Withdraw
                </button>
              </td>
            </tr>
            <!-- Expandable Status History Timeline -->
            <tr v-if="activeTimelineAppId === app.applicationID" class="timeline-row">
              <td colspan="5">
                <div class="timeline-box">
                  <h4>Application Status Timeline: {{ app.courseName }}</h4>
                  <div class="timeline-steps">
                    <div
                      v-for="stage in cartStore.statusHistory[app.applicationID] || []"
                      :key="stage.stageOrder"
                      :class="['timeline-step', { completed: stage.isComplete }]"
                    >
                      <div class="step-dot">{{ stage.isComplete ? '✓' : stage.stageOrder }}</div>
                      <div>
                        <strong>{{ stage.stageName }}</strong>
                        <small>{{ stage.stageDate ? formatDate(stage.stageDate) : 'Awaiting stage completion' }}</small>
                      </div>
                    </div>
                  </div>
                </div>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
  </main>
</template>

<style scoped>
.dashboard-wrapper { max-width: 1100px; margin: 0 auto; padding: 40px 20px; }
.header-row { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 25px; flex-wrap: wrap; gap: 15px; }
.header-row h1 { color: var(--vt-c-text-main); margin-bottom: 5px; }
.subtitle { color: var(--vt-c-text-muted); font-size: 14px; }
.btn-browse { background: var(--vt-c-primary); color: white; border: none; padding: 12px 18px; border-radius: 6px; font-weight: bold; cursor: pointer; }
.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 25px; }
.stat-card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); }
.stat-label { display: block; font-size: 13px; color: var(--vt-c-text-muted); margin-bottom: 8px; }
.stat-value { font-size: 28px; font-weight: bold; }
.text-warning { color: #b7791f; }
.text-success { color: #2f855a; }
.panel { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); margin-bottom: 25px; }
.cart-panel { border-left: 5px solid var(--vt-c-primary); }
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 10px; }
.panel-sub { font-size: 13px; color: var(--vt-c-text-muted); margin-top: 3px; }
.btn-submit-all { background: #2f855a; color: white; border: none; padding: 10px 18px; border-radius: 6px; font-weight: bold; cursor: pointer; }
.saved-grid { display: grid; gap: 15px; }
.saved-card { display: flex; justify-content: space-between; align-items: center; padding: 16px; border: 1px solid #e5e5e5; border-radius: 8px; background: #fafafa; flex-wrap: wrap; gap: 15px; }
.saved-card h3 { font-size: 15px; margin-bottom: 3px; }
.inst-name { font-size: 13px; color: var(--vt-c-text-muted); margin-bottom: 6px; }
.meta-tag { font-size: 12px; background: #e6eeee; color: var(--vt-c-primary-dark); padding: 3px 8px; border-radius: 4px; font-weight: bold; }
.saved-actions { display: flex; gap: 10px; }
.btn-apply-single { background: var(--vt-c-primary); color: white; border: none; padding: 8px 14px; border-radius: 5px; font-weight: bold; cursor: pointer; font-size: 12px; }
.btn-remove { background: transparent; border: 1px solid #c53030; color: #c53030; padding: 8px 12px; border-radius: 5px; font-weight: bold; cursor: pointer; font-size: 12px; }
.app-table { width: 100%; border-collapse: collapse; }
.app-table th { text-align: left; padding: 12px; border-bottom: 2px solid #eee; color: var(--vt-c-text-muted); font-size: 13px; }
.app-table td { padding: 15px 12px; border-bottom: 1px solid #eee; font-size: 14px; }
.fw-bold { font-weight: bold; }
.action-cell { display: flex; gap: 8px; }
.btn-track { background: #e6eeee; color: var(--vt-c-primary-dark); border: none; padding: 6px 12px; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 12px; }
.btn-withdraw { background: transparent; border: 1px solid #c53030; color: #c53030; padding: 6px 12px; border-radius: 4px; cursor: pointer; font-weight: bold; font-size: 12px; }
.timeline-row td { background: #f8f9fa; padding: 20px; }
.timeline-box h4 { margin-bottom: 15px; font-size: 14px; }
.timeline-steps { display: flex; gap: 25px; flex-wrap: wrap; }
.timeline-step { display: flex; align-items: center; gap: 10px; opacity: 0.5; }
.timeline-step.completed { opacity: 1; }
.step-dot { width: 28px; height: 28px; border-radius: 50%; background: #ccc; color: white; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 12px; }
.timeline-step.completed .step-dot { background: #2f855a; }
.timeline-step strong { display: block; font-size: 13px; }
.timeline-step small { font-size: 11px; color: var(--vt-c-text-muted); }
.status-badge { padding: 5px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; }
.status-pending, .status-application-submitted { background: #fef3d9; color: #b7791f; }
.status-accepted { background: #dcf5e3; color: #2f855a; }
.status-rejected { background: #fde2e2; color: #c53030; }
.success-banner { background: #dcf5e3; color: #2f855a; padding: 14px; border-radius: 6px; margin-bottom: 20px; font-weight: bold; }
.error-banner { background: #fde2e2; color: #c53030; padding: 14px; border-radius: 6px; margin-bottom: 20px; font-weight: bold; }
.empty-state { text-align: center; color: var(--vt-c-text-muted); padding: 25px; }
@media (max-width: 768px) { .stats-grid { grid-template-columns: repeat(2, 1fr); } }
</style>
