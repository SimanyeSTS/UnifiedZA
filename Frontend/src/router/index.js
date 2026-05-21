import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/home/HomeView.vue'
import LoginView from '../views/auth/LoginView.vue'
import RegisterView from '../views/auth/RegisterView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue'),
    // },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/marks-recording',
      name: 'marks-input',
      component: () => import('../views/apsengine/MarksInputView.vue'),
    },
    {
      path: '/marks-results',
      name: 'results-report',
      component: () => import('../views/apsengine/ResultsReportView.vue'),
    },
    {
      path: '/search',
      name: 'university-search',
      component: () => import('../views/catalog/UniversitySearchView.vue'),
    },
    {
      path: '/applications-cart',
      name: 'applications',
      component: () => import('../views/applications/CartDashboardView.vue'),
    },
    {
      path: '/bursary',
      name: 'bursary-match',
      component: () => import('../views/funding/BursaryMatchView.vue'),
    },
    {
      path: '/help',
      name: 'help-center',
      component: () => import('../views/support/HelpCenterView.vue'),
    }
  ],
})

export default router
