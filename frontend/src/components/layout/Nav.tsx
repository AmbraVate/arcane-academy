import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import styles from './Nav.module.css'

export default function Nav() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  if (!user) return null

  const RANK_FLOORS = [0, 400, 1000, 2000, 4000]
  const rankIdx = RANK_FLOORS.reduce((acc, floor, i) => user.totalXp >= floor ? i : acc, 0)
  const isMaxRank = rankIdx === RANK_FLOORS.length - 1
  const floor = RANK_FLOORS[rankIdx]
  const ceiling = isMaxRank ? null : RANK_FLOORS[rankIdx + 1]
  const xpInRank = user.totalXp - floor
  const xpForRank = ceiling !== null ? ceiling - floor : xpInRank || 1
  const xpPct = ceiling !== null ? Math.min(100, (xpInRank / xpForRank) * 100) : 100
  const level = rankIdx + 1
  const streak = user.streakDays ?? 0

  // Streak is "hot" if >= 3 days, at-risk if 1 day (might break today)
  const streakHot = streak >= 3
  const streakClass = streak === 0
    ? styles.streakZero
    : streakHot
    ? styles.streakHot
    : styles.streakWarm

  function handleLogout() {
    localStorage.removeItem('pollymath_topic')
    logout()
  }

  return (
    <nav className={styles.nav}>
      <div className={styles.brand} onClick={() => navigate('/')}>✦ Pollymath Academy</div>
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
          <span className={styles.xpNum}>{isMaxRank ? `${user.totalXp} xp` : `${xpInRank} / ${xpForRank} xp`}</span>
        </div>

        <div className={styles.rank}>⚗ {user.rank}</div>
        <button className="btn btn-ghost" style={{ padding: '4px 12px', fontSize: 12 }}
          onClick={() => navigate('/profile')}>
          Profile
        </button>
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
