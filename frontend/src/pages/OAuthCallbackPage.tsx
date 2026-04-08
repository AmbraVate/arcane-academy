import { useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import styles from './AuthPage.module.css'

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

    if (token && userId && username) {
      loginWithToken({ token, userId, username, totalXp, rank, streakDays })
      navigate('/topics', { replace: true })
    } else {
      navigate('/login', { replace: true })
    }
  }, [params, loginWithToken, navigate])

  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <div className={styles.glyph}>{'\u2728'}</div>
        <h1 className={styles.title}>Authenticating...</h1>
        <p className={styles.sub}>Opening the academy gates</p>
      </div>
    </div>
  )
}
