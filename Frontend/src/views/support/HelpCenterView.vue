<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <main class="error-wrapper">
    <div class="error-card">
      <div class="error-icon">⚠️</div>
      <h1>503 Service Unavailable</h1>
      <h2>Support & FAQ Microservice Offline</h2>
      <p class="error-desc">
        The Student Support module (Subsystem Owner: Ulibo Tshomela) is currently unresponsive or pending integration into the master branch.
      </p>
      <div class="terminal">
        <code>
          > Attempting connection to support-api:8082...<br>
          > Error: Connection refused<br>
          > Status: AWOL
        </code>
      </div>
      <button class="btn-primary" @click="router.push('/')">Return to Home</button>
    </div>
  </main>
</template>

<style scoped>
.error-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 140px);
  padding: 20px;
  background-color: var(--vt-c-bg);
}

.error-card {
  background: white;
  padding: 50px 40px;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  max-width: 600px;
  text-align: center;
  border-top: 5px solid #c53030;
}

.error-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

h1 {
  font-size: 32px;
  color: #c53030;
  margin-bottom: 10px;
  font-weight: 800;
}

h2 {
  font-size: 18px;
  color: var(--vt-c-text-main);
  margin-bottom: 20px;
}

.error-desc {
  color: var(--vt-c-text-muted);
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 30px;
}

.terminal {
  background-color: #1a1a1a;
  color: #4af626;
  padding: 15px;
  border-radius: 6px;
  text-align: left;
  font-family: 'Courier New', Courier, monospace;
  font-size: 13px;
  margin-bottom: 30px;
}

.btn-primary {
  background-color: var(--vt-c-primary);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 6px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.2s;
}

.btn-primary:hover {
  background-color: var(--vt-c-primary-dark);
}
</style>
