import { createContext, useContext, useState, useCallback, type ReactNode } from 'react'
import type { User } from '../types'
import { authApi } from '../api/services'

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
    const raw = localStorage.getItem('pollymath_user')
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(loadUser)

  const persist = useCallback((u: User) => {
    localStorage.setItem('pollymath_token', u.token)
    localStorage.setItem('pollymath_user', JSON.stringify(u))
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
    localStorage.removeItem('pollymath_token')
    localStorage.removeItem('pollymath_user')
    setUser(null)
  }, [])

  const updateStreak = useCallback((streakDays: number) => {
    setUser(prev => {
      if (!prev) return prev
      const updated = { ...prev, streakDays }
      localStorage.setItem("pollymath_user", JSON.stringify(updated))
      return updated
    })
  }, [])

  const updateXp = useCallback((xpEarned: number, newRank?: string) => {
    setUser(prev => {
      if (!prev) return prev
      const updated = { ...prev, totalXp: prev.totalXp + xpEarned, rank: newRank ?? prev.rank }
      localStorage.setItem('pollymath_user', JSON.stringify(updated))
      return updated
    })
  }, [])

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
