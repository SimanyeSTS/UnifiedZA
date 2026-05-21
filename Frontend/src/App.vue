<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="app-wrapper">
<nav class="navbar">
      <div class="logo">
        <RouterLink to="/" class="logo-link">
          <h2>UNIFIED ZA</h2>
        </RouterLink>
      </div>

      <ul class="nav-links">
        <li><RouterLink to="/">Home</RouterLink></li>

        <li><RouterLink to="/marks-recording">Calculate APS</RouterLink></li>
        <li><RouterLink to="/marks-results">APS Results</RouterLink></li>

        <li><RouterLink to="/search">Universities</RouterLink></li>

        <li><RouterLink to="/applications-cart">My Applications</RouterLink></li>

        <li><RouterLink to="/bursary">Funding</RouterLink></li>

        <li><RouterLink to="/help">Support</RouterLink></li>

        <li><RouterLink to="/register">Register</RouterLink></li>
        <li><RouterLink to="/login" class="nav-login-btn">Login</RouterLink></li>
      </ul>
    </nav>

    <RouterView />

    <footer class="footer">
      <p>UNIFIED ZA &copy; 2026</p>
    </footer>
  </div>
</template>

<style>
.app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.navbar {
  background-color: var(--vt-c-primary);
  padding: 15px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-link {
  text-decoration: none;
}

.logo h2 {
  color: white;
  font-size: 20px;
  letter-spacing: 1px;
}

.nav-links {
  list-style: none;
  display: flex;
  gap: 25px;
  align-items: center;
}


.nav-links a {
  color: #c8d8d8;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: color 0.2s ease;
}

.nav-links a:hover,
.nav-links a.router-link-active {
  color: white;
}

.nav-login-btn {
  background-color: var(--vt-c-secondary);
  color: var(--vt-c-primary) !important;
  padding: 8px 16px;
  border-radius: 4px;
  font-weight: bold !important;
}

.nav-login-btn:hover {
  background-color: var(--vt-c-secondary-hover);
}

.footer {
  background-color: var(--vt-c-primary);
  color: white;
  text-align: center;
  padding: 15px;
  font-size: 13px;
  margin-top: auto;
}
</style>
