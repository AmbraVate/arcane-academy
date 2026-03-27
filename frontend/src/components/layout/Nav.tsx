// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import styles from './Nav.module.css'

export default function Nav() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  if (!user) return null

  const xpPct = Math.min(100, (user.totalXp / 4000) * 100)

  function handleLogout() {
    localStorage.removeItem('arcane_topic')
    logout()
  }

  return (
    <nav className={styles.nav}>
      <div className={styles.brand} onClick={() => navigate('/')}>✦ Arcane Academy</div>
      <div className={styles.right}>
        <div className={styles.xpWrap}>
          <span className={styles.lv}>Lv.{Math.floor(user.totalXp / 200) + 1}</span>
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
