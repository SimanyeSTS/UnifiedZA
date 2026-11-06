<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <main class="home-wrapper">
    <!-- Hero Section -->
    <section class="hero-section">
      <div class="hero-content">
        <h1>Your Future, <span class="highlight">Unified.</span></h1>
        <p class="hero-subtitle">
          The centralized platform for South African students to calculate APS, discover university programmes, and find NSFAS funding.
        </p>
        <div class="hero-actions">
          <button class="btn-primary-lg" @click="router.push('/marks-recording')">Calculate APS</button>
          <button class="btn-outline-lg" @click="router.push('/search')">Browse Universities</button>
        </div>
      </div>
    </section>

    <!-- Features Grid -->
    <section class="features-section">
      <div class="section-header">
        <h2>Everything you need in one portal</h2>
        <p>Navigate your higher education journey with our integrated tools.</p>
      </div>

      <div class="features-grid">
        <!-- APS Engine -->
        <div class="feature-card" @click="router.push('/marks-recording')">
          <div class="icon-placeholder">📊</div>
          <h3>APS Engine</h3>
          <p>Input your NSC marks to calculate your exact Admission Point Score across different institution standards.</p>
        </div>

        <!-- University Catalog -->
        <div class="feature-card" @click="router.push('/search')">
          <div class="icon-placeholder">🏛️</div>
          <h3>Institution Catalog</h3>
          <p>Explore traditional universities and universities of technology. View campuses, faculties, and entry requirements.</p>
        </div>

        <!-- Funding Matcher -->
        <div class="feature-card" @click="router.push('/bursary')">
          <div class="icon-placeholder">💰</div>
          <h3>Funding Matcher</h3>
          <p>Find NSFAS and private bursary opportunities tailored to your household income and academic performance.</p>
        </div>

        <!-- Application Cart -->
        <div class="feature-card" @click="router.push('/applications-cart')">
          <div class="icon-placeholder">📂</div>
          <h3>Application Cart</h3>
          <p>Track your submitted applications, monitor your acceptance statuses, and manage your saved courses.</p>
        </div>
      </div>
    </section>
  </main>
</template>

<style scoped>
.home-wrapper {
  flex-grow: 1;
  background-color: var(--vt-c-bg);
}

/* Hero Section */
.hero-section {
  background-color: var(--vt-c-primary);
  color: white;
  padding: 80px 20px;
  text-align: center;
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
}

.hero-content h1 {
  font-size: clamp(40px, 5vw, 56px);
  font-weight: 800;
  margin-bottom: 20px;
  line-height: 1.2;
}

.highlight {
  color: #c9dad8;
}

.hero-subtitle {
  font-size: 18px;
  line-height: 1.6;
  margin-bottom: 40px;
  color: #e6eeee;
}

.hero-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.btn-primary-lg {
  background-color: var(--vt-c-secondary);
  color: var(--vt-c-primary);
  padding: 15px 30px;
  border-radius: 8px;
  font-weight: bold;
  font-size: 16px;
  border: none;
  cursor: pointer;
  transition: 0.2s;
}

.btn-primary-lg:hover {
  background-color: white;
}

.btn-outline-lg {
  background-color: transparent;
  color: white;
  padding: 15px 30px;
  border-radius: 8px;
  font-weight: bold;
  font-size: 16px;
  border: 2px solid white;
  cursor: pointer;
  transition: 0.2s;
}

.btn-outline-lg:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* Features Section */
.features-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 60px 20px;
}

.section-header {
  text-align: center;
  margin-bottom: 50px;
}

.section-header h2 {
  font-size: 32px;
  color: var(--vt-c-primary-dark);
  margin-bottom: 10px;
}

.section-header p {
  color: var(--vt-c-text-muted);
  font-size: 16px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.feature-card {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  border: 1px solid #eaeaea;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
  border-color: var(--vt-c-primary);
}

.icon-placeholder {
  font-size: 40px;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 20px;
  color: var(--vt-c-text-main);
  margin-bottom: 15px;
}

.feature-card p {
  color: var(--vt-c-text-muted);
  font-size: 14px;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .hero-actions {
    flex-direction: column;
  }
}
</style>
