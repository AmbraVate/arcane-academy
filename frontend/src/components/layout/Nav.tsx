import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import styles from './Nav.module.css'

export default function Nav() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  if (!user) return null

  const RANK_FLOORS = [0, 800, 2000, 4000, 6500, 8000, 11000]
  const rankIdx = RANK_FLOORS.reduce((acc, floor, i) => user.totalXp >= floor ? i : acc, 0)
  const isMaxRank = rankIdx === RANK_FLOORS.length - 1
  const floor = RANK_FLOORS[rankIdx]
  const ceiling = isMaxRank ? null : RANK_FLOORS[rankIdx + 1]
  const xpInRank = user.totalXp - floor
  const xpForRank = ceiling !== null ? ceiling - floor : xpInRank || 1
  const xpPct = ceiling !== null ? Math.min(100, (xpInRank / xpForRank) * 100) : 100
  const level = rankIdx + 1
  const streak = user.streakDays ?? 0

  const streakHot = streak >= 3
  const streakClass = streak === 0
    ? styles.streakZero
    : streakHot
    ? styles.streakHot
    : styles.streakWarm

  return (
    <nav className={styles.nav}>
      <div className={styles.brand} onClick={() => navigate('/')}>✦ Polymath Academy</div>
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
        <button className={`btn btn-ghost ${styles.navBtn}`} onClick={() => navigate('/review')}>
          <span className={styles.navBtnFull}>Review</span>
          <span className={styles.navBtnIcon}>📖</span>
        </button>
        <button className={`btn btn-ghost ${styles.navBtn}`} onClick={() => navigate('/profile')}>
          <span className={styles.navBtnFull}>Profile</span>
          <span className={styles.navBtnIcon}>👤</span>
        </button>
        <button className={`btn btn-ghost ${styles.navBtn}`} onClick={logout}>
          <span className={styles.navBtnFull}>Logout</span>
          <span className={styles.navBtnIcon}>⏏</span>
        </button>
      </div>
    </nav>
  )
}
