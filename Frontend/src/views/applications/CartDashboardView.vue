<script setup>
import { onMounted } from 'vue'
import { useCartStore } from '../../stores/cartStore'

const cartStore = useCartStore()

onMounted(async () => {
  // Load all data from Sisa's C# API automatically
  await cartStore.loadDashboardData()
})

const handleWithdraw = async (applicationId) => {
  if (confirm("Are you sure you want to withdraw this application? This cannot be undone.")) {
    await cartStore.withdrawApplication(applicationId)
  }
}

const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric' }
  return new Date(dateString).toLocaleDateString(undefined, options)
}
</script>

<template>
  <main class="dashboard-wrapper">
    <div class="header">
      <h1>Welcome back, Sisasenkosi!</h1>
      <p class="subtitle">Track your applications and manage your saved courses</p>
    </div>

    <!-- Stats Cards -->
    <div class="stats-grid">
      <div class="stat-card">
        <span class="stat-label">Total Applications</span>
        <span class="stat-value">{{ cartStore.stats.totalApplications }}</span>
      </div>
      <div class="stat-card">
        <span class="stat-label">Pending</span>
        <span class="stat-value text-warning">{{ cartStore.stats.pending }}</span>
      </div>
      <div class="stat-card">
        <span class="stat-label">Accepted</span>
        <span class="stat-value text-success">{{ cartStore.stats.accepted }}</span>
      </div>
    </div>

    <div v-if="cartStore.errorMessage" class="error-banner">
      {{ cartStore.errorMessage }}
    </div>

    <!-- Applications Table -->
    <div class="panel">
      <div class="panel-header">
        <h2>Recent Applications</h2>
      </div>

      <div v-if="cartStore.isLoading" class="loading-state">
        Loading applications...
      </div>

      <table v-else class="app-table">
        <thead>
          <tr>
            <th>Course</th>
            <th>Institution</th>
            <th>Application Date</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="cartStore.applications.length === 0">
            <td colspan="5" class="empty-state">No applications found.</td>
          </tr>
          <tr v-for="app in cartStore.applications" :key="app.applicationID">
            <td class="fw-bold">{{ app.courseName }}</td>
            <td>{{ app.institutionName }}</td>
            <td>{{ formatDate(app.applicationDate) }}</td>
            <td>
              <span :class="['status-badge', 'status-' + app.status.toLowerCase().replace(' ', '-')]">
                {{ app.status }}
              </span>
            </td>
            <td>
              <button class="btn-withdraw" @click="handleWithdraw(app.applicationID)">
                Withdraw
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Cart Preview -->
    <div class="panel cart-preview">
      <h2>Saved Courses (Cart)</h2>
      <p v-if="cartStore.savedCourses.length === 0" class="empty-state">
        Your cart is currently empty.
      </p>
      <p v-else class="cart-status">
        You have <strong>{{ cartStore.savedCourses.length }}</strong> of 3 courses saved in your cart ready for submission.
      </p>
    </div>
  </main>
</template>

<style scoped>
.dashboard-wrapper {
  max-width: 1000px;
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

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.05);
}

.stat-label {
  display: block;
  font-size: 14px;
  color: var(--vt-c-text-muted);
  margin-bottom: 10px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
}

.text-warning { color: #b7791f; }
.text-success { color: #2f855a; }

/* Panels & Tables */
.panel {
  background: white;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.05);
  margin-bottom: 25px;
}

.panel-header {
  margin-bottom: 20px;
}

.app-table {
  width: 100%;
  border-collapse: collapse;
}

.app-table th {
  text-align: left;
  padding: 12px;
  border-bottom: 2px solid #eee;
  color: var(--vt-c-text-muted);
  font-size: 14px;
}

.app-table td {
  padding: 15px 12px;
  border-bottom: 1px solid #eee;
  font-size: 14px;
}

.fw-bold { font-weight: bold; }
.empty-state { text-align: center; color: var(--vt-c-text-muted); padding: 20px !important; }

/* Badges */
.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
}
.status-pending { background-color: #fef3d9; color: #b7791f; }
.status-accepted { background-color: #dcf5e3; color: #2f855a; }
.status-rejected { background-color: #fde2e2; color: #c53030; }

/* Buttons & Alerts */
.btn-withdraw {
  background: transparent;
  border: 1px solid #c53030;
  color: #c53030;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}
.btn-withdraw:hover { background: #fde2e2; }

.error-banner {
  background-color: #fde2e2;
  color: #c53030;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.cart-preview {
  background-color: #dbe4eb;
}
.cart-status { margin-top: 10px; font-size: 15px; }
</style>
