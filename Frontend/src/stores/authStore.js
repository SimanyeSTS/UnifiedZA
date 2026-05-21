// Chuma's territory

class authStore {
  constructor() {
    this.token = null;
    this.user = null;
  }

  setToken(token) {
    this.token = token;
  }

  setUser(user) {
    this.user = user;
  }

  clearAuth() {
    this.token = null;
    this.user = null;
  }
}

export default new authStore();
