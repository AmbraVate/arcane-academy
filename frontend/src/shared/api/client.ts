import axios from 'axios'

const REFRESH_TOKEN_KEY = 'arcane_refresh_token'

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

let isRefreshing = false
let pendingQueue: Array<{ resolve: (token: string) => void; reject: (err: unknown) => void }> = []

function flushQueue(token: string | null, err: unknown) {
  pendingQueue.forEach(p => token ? p.resolve(token) : p.reject(err))
  pendingQueue = []
}

function forceLogout(reason?: string) {
  localStorage.removeItem('arcane_token')
  localStorage.removeItem('arcane_user')
  localStorage.removeItem(REFRESH_TOKEN_KEY)
  window.location.href = reason ? `/login?reason=${reason}` : '/login'
}

api.interceptors.response.use(
  (res) => res,
  async (err) => {
    const status = err.response?.status
    const originalRequest = err.config

    if (status === 401 && !originalRequest._retried) {
      const refreshToken = localStorage.getItem(REFRESH_TOKEN_KEY)
      if (!refreshToken) { forceLogout(); return Promise.reject(err) }

      if (isRefreshing) {
        // Queue this request until the in-flight refresh completes
        return new Promise((resolve, reject) => {
          pendingQueue.push({
            resolve: (token) => {
              originalRequest.headers.Authorization = `Bearer ${token}`
              resolve(api(originalRequest))
            },
            reject,
          })
        })
      }

      originalRequest._retried = true
      isRefreshing = true

      try {
        const { data } = await axios.post('/api/auth/refresh', { refreshToken })
        const newToken: string = data.token
        const newRefresh: string = data.refreshToken
        localStorage.setItem('arcane_token', newToken)
        localStorage.setItem(REFRESH_TOKEN_KEY, newRefresh)
        // Update stored user token too
        const stored = localStorage.getItem('arcane_user')
        if (stored) {
          try {
            const u = JSON.parse(stored)
            localStorage.setItem('arcane_user', JSON.stringify({ ...u, token: newToken }))
          } catch { /* malformed JSON — ignore */ }
        }
        api.defaults.headers.common.Authorization = `Bearer ${newToken}`
        flushQueue(newToken, null)
        originalRequest.headers.Authorization = `Bearer ${newToken}`
        return api(originalRequest)
      } catch (refreshErr) {
        flushQueue(null, refreshErr)
        forceLogout()
        return Promise.reject(refreshErr)
      } finally {
        isRefreshing = false
      }
    }

    if (status === 403) {
      const msg: string = err.response?.data?.message ?? ''
      if (msg.toLowerCase().includes('blocked')) forceLogout('blocked')
    }

    return Promise.reject(err)
  }
)

export { REFRESH_TOKEN_KEY }
export default api
