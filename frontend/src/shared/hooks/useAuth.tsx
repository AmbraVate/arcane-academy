import { createContext, useContext, useState, useCallback, useEffect, type ReactNode } from 'react'
import type { User } from '@/shared/types'
import { authApi } from '@/shared/api/services'
import { REFRESH_TOKEN_KEY } from '@/shared/api/client'

interface AuthContextValue {
  user: User | null
  login: (email: string, password: string) => Promise<void>
  register: (username: string, email: string, password: string) => Promise<void>
  loginWithToken: (user: User) => void
  logout: () => void
  updateXp: (xpEarned: number, newRank?: string) => void
  updateStreak: (streakDays: number) => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

function loadUser(): User | null {
  try {
    const raw = localStorage.getItem('arcane_user')
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(loadUser)

  const persist = useCallback((u: User) => {
    localStorage.setItem('arcane_token', u.token)
    localStorage.setItem('arcane_user', JSON.stringify(u))
    setUser(u)
  }, [])

  const login = useCallback(async (email: string, password: string) => {
    const u = await authApi.login(email, password)
    persist(u)
  }, [persist])

  const register = useCallback(async (username: string, email: string, password: string) => {
    const u = await authApi.register(username, email, password)
    persist(u)
  }, [persist])

  const loginWithToken = useCallback((u: User) => {
    persist(u)
  }, [persist])

  const logout = useCallback(() => {
    localStorage.removeItem('arcane_token')
    localStorage.removeItem('arcane_user')
    localStorage.removeItem(REFRESH_TOKEN_KEY)
    setUser(null)
  }, [])

  const updateStreak = useCallback((streakDays: number) => {
    setUser(prev => {
      if (!prev) return prev
      const updated = { ...prev, streakDays }
      localStorage.setItem("arcane_user", JSON.stringify(updated))
      return updated
    })
  }, [])

  const updateXp = useCallback((xpEarned: number, newRank?: string) => {
    setUser(prev => {
      if (!prev) return prev
      const updated = { ...prev, totalXp: prev.totalXp + xpEarned, rank: newRank ?? prev.rank }
      localStorage.setItem('arcane_user', JSON.stringify(updated))
      return updated
    })
  }, [])

  // On every page load, silently re-fetch user data from the server so that
  // admin changes (bypassPaywall, role, subscriptionStatus) take effect
  // immediately without requiring the user to log out and back in.
  useEffect(() => {
    const stored = localStorage.getItem('arcane_user')
    if (!stored) return // not logged in — nothing to hydrate
    authApi.me()
      .then(fresh => persist(fresh))
      .catch(() => { /* token expired — the 401 interceptor in client.ts handles logout */ })
  }, []) // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <AuthContext.Provider value={{ user, login, register, loginWithToken, logout, updateXp, updateStreak }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
