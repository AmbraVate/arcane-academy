import { useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'
import { REFRESH_TOKEN_KEY } from '@/shared/api/client'
import api from '@/shared/api/client'

/**
 * Handles the OAuth2 callback after Google sign-in.
 *
 * The backend no longer sends tokens in the URL (which would expose them in browser history,
 * server logs, and referrer headers). Instead it sends a short-lived opaque code.
 * We exchange that code for the actual auth data via a secure POST to /api/auth/exchange,
 * receiving tokens only in the JSON response body.
 */
export default function OAuthCallbackPage() {
  const [params] = useSearchParams()
  const { loginWithToken } = useAuth()
  const navigate = useNavigate()

  useEffect(() => {
    const code = params.get('code')
    const error = params.get('error')

    if (error === 'account_conflict') {
      navigate('/login?error=account_conflict', { replace: true })
      return
    }

    if (!code) {
      navigate('/login', { replace: true })
      return
    }

    api.post<{
      token: string; refreshToken: string; userId: string; username: string;
      totalXp: number; rank: string; streakDays: number; role: 'USER' | 'ADMIN';
      bypassPaywall: boolean; subscriptionStatus: string; onboardingCompleted: boolean;
    }>(`/api/auth/exchange?code=${encodeURIComponent(code)}`)
      .then(({ data }) => {
        if (data.refreshToken) localStorage.setItem(REFRESH_TOKEN_KEY, data.refreshToken)
        loginWithToken({
          token:           data.token,
          userId:          data.userId,
          username:        data.username,
          totalXp:         data.totalXp,
          rank:            data.rank,
          streakDays:      data.streakDays,
          role:            data.role,
          bypassPaywall:   data.bypassPaywall,
        })
        navigate('/', { replace: true })
      })
      .catch(() => navigate('/login?error=oauth_failed', { replace: true }))
  }, []) // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <div className="flex items-center justify-center min-h-screen bg-bg">
      <div className="bg-card border border-border rounded-[14px] px-10 py-10 text-center max-w-[380px] w-full mx-4">
        <div className="text-[48px] mb-4">✨</div>
        <h1 className="text-[22px] font-bold text-gold m-0 mb-2">Authenticating...</h1>
        <p className="text-muted text-[14px] m-0">Opening the academy gates</p>
      </div>
    </div>
  )
}
