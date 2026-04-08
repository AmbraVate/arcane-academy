import axios from 'axios'

// Always use relative URLs in production — nginx proxies /api/* to the backend.
// In local dev (npm run dev), vite.config.ts proxy handles the same routing.
const api = axios.create({
  baseURL: '',
  headers: { 'Content-Type': 'application/json' },
})

// Attach JWT from localStorage on every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('pollymath_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// Redirect to login on 401
api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('pollymath_token')
      localStorage.removeItem('pollymath_user')
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

export default api
