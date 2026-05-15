import axios from 'axios'

// Always use relative URLs in production — nginx proxies /api/* to the backend.
// In local dev (npm run dev), vite.config.ts proxy handles the same routing.
const api = axios.create({
  baseURL: '',
  headers: { 'Content-Type': 'application/json' },
})

// Attach JWT from localStorage on every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('arcane_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// Redirect to login on 401 (token expired) or 403 with "Account is blocked"
api.interceptors.response.use(
  (res) => res,
  (err) => {
    const status = err.response?.status
    if (status === 401) {
      localStorage.removeItem('arcane_token')
      localStorage.removeItem('arcane_user')
      window.location.href = '/login'
    } else if (status === 403) {
      // Only force-logout on a blocked-account response (auth-level 403, not resource-level)
      const msg: string = err.response?.data?.message ?? ''
      if (msg.toLowerCase().includes('blocked')) {
        localStorage.removeItem('arcane_token')
        localStorage.removeItem('arcane_user')
        window.location.href = '/login?reason=blocked'
      }
    }
    return Promise.reject(err)
  }
)

export default api
