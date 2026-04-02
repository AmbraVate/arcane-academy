import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import styles from './Nav.module.css'

export default function Nav() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  if (!user) return null

  const xpPct = Math.min(100, (user.totalXp / 4000) * 100)
  const level = Math.floor(user.totalXp / 200) + 1
  const streak = user.streakDays ?? 0

  // Streak is "hot" if >= 3 days, at-risk if 1 day (might break today)
  const streakHot = streak >= 3
  const streakClass = streak === 0
    ? styles.streakZero
    : streakHot
    ? styles.streakHot
    : styles.streakWarm

  function handleLogout() {
    localStorage.removeItem('arcane_topic')
    logout()
  }

  return (
    <nav className={styles.nav}>
      <div className={styles.brand} onClick={() => navigate('/')}>✦ Arcane Academy</div>
      <div className={styles.right}>
        {/* Streak indicator */}
        <div className={`${styles.streak} ${streakClass}`} title={`${streak}-day streak`}>
          <span className={`${styles.flame} ${streakHot ? styles.flamePulse : ''}`}>🔥</span>
          <span className={styles.streakNum}>{streak}</span>
        </div>

        {/* XP bar */}
        <div className={styles.xpWrap}>
          <span className={styles.lv}>Lv.{level}</span>
          <div className={styles.xpBar}>
            <div className={styles.xpFill} style={{ width: `${xpPct}%` }} />
          </div>
          <span className={styles.xpNum}>{user.totalXp} xp</span>
        </div>

        <div className={styles.rank}>⚗ {user.rank}</div>
        <button className="btn btn-ghost" style={{ padding: '4px 12px', fontSize: 12 }}
          onClick={() => navigate('/topics')}>
          Topics
        </button>
        <button className="btn btn-ghost" style={{ padding: '4px 12px', fontSize: 12 }} onClick={handleLogout}>
          Logout
        </button>
      </div>
    </nav>
  )
}
