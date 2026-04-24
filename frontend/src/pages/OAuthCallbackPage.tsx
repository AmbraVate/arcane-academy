import { useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

export default function OAuthCallbackPage() {
  const [params] = useSearchParams()
  const { loginWithToken } = useAuth()
  const navigate = useNavigate()

  useEffect(() => {
    const token = params.get('token')
    const userId = params.get('userId')
    const username = params.get('username')
    const totalXp = parseInt(params.get('totalXp') ?? '0', 10)
    const rank = params.get('rank') ?? 'Novice'
    const streakDays = parseInt(params.get('streakDays') ?? '0', 10)
    const role = (params.get('role') as 'USER' | 'ADMIN') ?? 'USER'

    if (token && userId && username) {
      loginWithToken({ token, userId, username, totalXp, rank, streakDays, role })
      navigate('/topics', { replace: true })
    } else {
      navigate('/login', { replace: true })
    }
  }, [params, loginWithToken, navigate])

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
