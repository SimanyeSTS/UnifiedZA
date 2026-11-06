<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { institutions } from '../../data/institutionsData'
import { useApsStore } from '../../stores/apsStore'
import { useCartStore } from '../../stores/cartStore'
import { useAuthStore } from '../../stores/authStore'

const apsStore = useApsStore()
const cartStore = useCartStore()
const authStore = useAuthStore()
const router = useRouter()

const searchQuery = ref('')
const selectedProvince = ref('All')
const showEligibleOnly = ref(false)

onMounted(() => {
  cartStore.fetchSavedCourses()
  cartStore.fetchApplications()
})

const userAps = computed(() => apsStore.calculatedAps)

const isCourseInCart = (courseID) =>
  cartStore.savedCourses.some((item) => item.courseID === courseID)

const isCourseApplied = (courseID) =>
  cartStore.applications.some((item) => item.courseID === courseID)

const canQualify = (requiredAps) => {
  if (userAps.value === null) return true
  return Number(userAps.value) >= requiredAps
}

const getApsDiffLabel = (requiredAps) => {
  if (userAps.value === null) return `Min APS: ${requiredAps}`
  const diff = Number(userAps.value) - requiredAps
  if (diff >= 0) return `Qualifies (+${diff} pts)`
  return `Short by ${Math.abs(diff)} pts`
}

const handleAddToCart = async (inst, course) => {
  if (!authStore.isAuthenticated) {
    if (confirm('Please log in or register to save courses to your personal Application Cart. Go to Login now?')) {
      router.push({ path: '/login', query: { redirect: '/search' } })
    }
    return
  }

  await cartStore.addCourseToCart({
    courseID: course.courseID,
    name: course.name,
    institutionName: inst.name,
    institutionShortName: inst.shortName
  })
}

const filteredInstitutions = computed(() => {
  return institutions
    .map((inst) => {
      const courses = showEligibleOnly.value && userAps.value !== null
        ? inst.courses.filter((c) => Number(userAps.value) >= c.aps)
        : inst.courses
      return { ...inst, filteredCourses: courses }
    })
    .filter((inst) => {
      const matchesSearch =
        inst.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        inst.shortName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        inst.filteredCourses.some((c) =>
          c.name.toLowerCase().includes(searchQuery.value.toLowerCase())
        )
      const matchesProvince =
        selectedProvince.value === 'All' || inst.province === selectedProvince.value
      const hasCourses = inst.filteredCourses.length > 0
      return matchesSearch && matchesProvince && hasCourses
    })
})
</script>

<template>
  <main class="catalog-wrapper">
    <!-- Top Banner: Shows Current APS & Cart Status -->
    <div class="aps-status-banner">
      <div>
        <h2>Institution & Course Eligibility Matcher</h2>
        <p v-if="userAps !== null">
          Matching courses against your calculated APS of <strong>{{ userAps }} points</strong>.
        </p>
        <p v-else>
          You haven't calculated your APS yet.
          <RouterLink to="/marks-recording" class="banner-link">Calculate your APS now</RouterLink>
          to see which programmes you qualify for!
        </p>
      </div>

      <div class="banner-actions">
        <div class="aps-pill-lg">
          <span>Your APS:</span>
          <strong>{{ userAps ?? 'Not Set' }}</strong>
        </div>
        <button class="btn-view-cart" @click="router.push('/applications-cart')">
          Application Cart ({{ cartStore.savedCourses.length }}/3) &rarr;
        </button>
      </div>
    </div>

    <!-- Feedback Banners -->
    <div v-if="cartStore.successMessage" class="alert alert-success">
      {{ cartStore.successMessage }}
      <RouterLink to="/applications-cart" style="margin-left: 10px; font-weight: bold; color: #2f855a;">
        Go to My Applications &rarr;
      </RouterLink>
    </div>
    <div v-if="cartStore.errorMessage" class="alert alert-error">
      {{ cartStore.errorMessage }}
    </div>

    <!-- Search & Filters -->
    <div class="filters">
      <input
        type="search"
        v-model="searchQuery"
        placeholder="Search by university, abbreviation, or programme (e.g. Computer Science)..."
        class="search-input"
      />
      <select v-model="selectedProvince" class="province-select">
        <option value="All">All Provinces</option>
        <option value="Western Cape">Western Cape</option>
        <option value="Gauteng">Gauteng</option>
        <option value="Free State">Free State</option>
      </select>
      <label v-if="userAps !== null" class="eligible-toggle">
        <input type="checkbox" v-model="showEligibleOnly" />
        Show only courses I qualify for
      </label>
    </div>

    <!-- Institutions Grid -->
    <div class="institution-grid">
      <div v-for="inst in filteredInstitutions" :key="inst.id" class="inst-card">
        <div class="inst-header">
          <div>
            <span class="badge">{{ inst.type }}</span>
            <h2>{{ inst.name }} ({{ inst.shortName }})</h2>
            <p class="location">{{ inst.city }}, {{ inst.province }}</p>
          </div>
          <div :class="['aps-badge', userAps !== null && userAps < inst.aps ? 'aps-low' : '']">
            <span class="aps-label">Base APS</span>
            <span class="aps-score">{{ inst.aps }}</span>
          </div>
        </div>

        <p class="desc">{{ inst.description }}</p>

        <div class="courses-section">
          <h4>Available Programmes & APS Comparison</h4>
          <ul class="course-list">
            <li v-for="course in inst.filteredCourses" :key="course.courseID" class="course-item">
              <div class="course-info">
                <span class="course-name">{{ course.name }}</span>
                <small class="course-req">Req: {{ course.requirements }} | Min APS: {{ course.aps }}</small>
              </div>

              <div class="course-actions">
                <span
                  :class="[
                    'stack-badge',
                    userAps === null
                      ? 'stack-neutral'
                      : canQualify(course.aps)
                        ? 'stack-pass'
                        : 'stack-fail'
                  ]"
                >
                  {{ getApsDiffLabel(course.aps) }}
                </span>

                <button
                  v-if="isCourseApplied(course.courseID)"
                  class="btn-cart applied"
                  disabled
                >
                  Applied ✓
                </button>
                <button
                  v-else-if="isCourseInCart(course.courseID)"
                  class="btn-cart in-cart"
                  @click="router.push('/applications-cart')"
                >
                  In Cart &rarr;
                </button>
                <button
                  v-else
                  class="btn-cart"
                  :disabled="userAps !== null && !canQualify(course.aps)"
                  @click="handleAddToCart(inst, course)"
                >
                  {{ userAps !== null && !canQualify(course.aps) ? 'APS Too Low' : '+ Add to Cart' }}
                </button>
              </div>
            </li>
          </ul>
        </div>

        <a :href="inst.website" target="_blank" rel="noopener noreferrer" class="btn-outline">
          Visit Official Website
        </a>
      </div>
    </div>

    <div v-if="filteredInstitutions.length === 0" class="empty-state">
      No institutions or courses found matching your criteria.
    </div>
  </main>
</template>

<style scoped>
.catalog-wrapper { max-width: 1200px; margin: 0 auto; padding: 30px 20px; }
.aps-status-banner { background: var(--vt-c-primary); color: white; padding: 25px 30px; border-radius: 10px; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 20px; margin-bottom: 25px; }
.aps-status-banner h2 { font-size: 22px; margin-bottom: 5px; }
.aps-status-banner p { font-size: 14px; color: #e6eeee; }
.banner-link { color: var(--vt-c-secondary); font-weight: bold; text-decoration: underline; }
.banner-actions { display: flex; align-items: center; gap: 15px; }
.aps-pill-lg { background: rgba(255,255,255,0.15); padding: 10px 16px; border-radius: 8px; display: flex; align-items: center; gap: 8px; font-size: 14px; }
.aps-pill-lg strong { font-size: 20px; color: var(--vt-c-secondary); }
.btn-view-cart { background: var(--vt-c-secondary); color: var(--vt-c-primary-dark); border: none; padding: 12px 18px; border-radius: 8px; font-weight: bold; cursor: pointer; font-size: 14px; }
.alert { padding: 14px 18px; border-radius: 8px; margin-bottom: 20px; font-size: 14px; font-weight: 500; }
.alert-success { background: #dcf5e3; color: #2f855a; }
.alert-error { background: #fde2e2; color: #c53030; }
.filters { display: flex; gap: 15px; margin-bottom: 25px; flex-wrap: wrap; align-items: center; }
.search-input { flex: 1; min-width: 260px; padding: 12px 15px; border: 1px solid #ccc; border-radius: 6px; font-size: 14px; }
.province-select { padding: 12px 15px; border: 1px solid #ccc; border-radius: 6px; font-size: 14px; background: white; }
.eligible-toggle { display: flex; align-items: center; gap: 8px; font-size: 13px; font-weight: bold; background: white; padding: 11px 15px; border-radius: 6px; border: 1px solid #ccc; cursor: pointer; }
.institution-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(500px, 1fr)); gap: 25px; }
.inst-card { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); border: 1px solid #eaeaea; display: flex; flex-direction: column; }
.inst-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px; }
.inst-header h2 { font-size: 18px; font-weight: bold; margin: 8px 0 4px 0; color: var(--vt-c-primary-dark); }
.location { font-size: 13px; color: var(--vt-c-text-muted); }
.badge { background: #e6eeee; color: var(--vt-c-primary-dark); padding: 4px 10px; border-radius: 12px; font-size: 11px; font-weight: bold; }
.aps-badge { background: var(--vt-c-primary); color: white; padding: 10px 15px; border-radius: 6px; text-align: center; min-width: 80px; }
.aps-badge.aps-low { background: #c53030; }
.aps-label { display: block; font-size: 10px; text-transform: uppercase; }
.aps-score { display: block; font-size: 22px; font-weight: bold; }
.desc { font-size: 13px; line-height: 1.5; color: var(--vt-c-text-main); margin-bottom: 18px; }
.courses-section { background: #f8f9fa; padding: 15px; border-radius: 6px; margin-bottom: 18px; flex-grow: 1; }
.courses-section h4 { font-size: 12px; margin-bottom: 12px; color: var(--vt-c-text-muted); text-transform: uppercase; }
.course-list { list-style: none; padding: 0; margin: 0; }
.course-item { display: flex; justify-content: space-between; align-items: center; gap: 12px; padding: 10px 0; border-bottom: 1px solid #e5e5e5; }
.course-item:last-child { border-bottom: none; }
.course-info { display: flex; flex-direction: column; gap: 3px; }
.course-name { font-weight: bold; font-size: 13px; color: var(--vt-c-text-main); }
.course-req { font-size: 11px; color: var(--vt-c-text-muted); }
.course-actions { display: flex; align-items: center; gap: 10px; flex-shrink: 0; }
.stack-badge { padding: 4px 8px; border-radius: 12px; font-size: 11px; font-weight: bold; }
.stack-pass { background: #dcf5e3; color: #2f855a; }
.stack-fail { background: #fde2e2; color: #c53030; }
.stack-neutral { background: #e6eeee; color: var(--vt-c-primary-dark); }
.btn-cart { background: var(--vt-c-primary); color: white; border: none; padding: 7px 12px; border-radius: 5px; font-size: 12px; font-weight: bold; cursor: pointer; transition: 0.2s; }
.btn-cart:hover:not(:disabled) { background: var(--vt-c-primary-dark); }
.btn-cart:disabled { background: #ccc; cursor: not-allowed; }
.btn-cart.in-cart { background: #b7791f; }
.btn-cart.applied { background: #2f855a; }
.btn-outline { display: block; text-align: center; padding: 10px; border: 1px solid var(--vt-c-primary); color: var(--vt-c-primary); border-radius: 6px; text-decoration: none; font-weight: bold; font-size: 13px; }
.btn-outline:hover { background: var(--vt-c-primary); color: white; }
@media (max-width: 600px) { .institution-grid { grid-template-columns: 1fr; } .course-item { flex-direction: column; align-items: flex-start; } }
</style>
