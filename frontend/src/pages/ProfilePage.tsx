import { useEffect, useState } from 'react'
import { useAuth } from '../hooks/useAuth'
import { badgeApi } from '../api/services'
import type { Badge } from '../types'
import styles from './ProfilePage.module.css'

export default function ProfilePage() {
  const { user } = useAuth()
  const [badges, setBadges] = useState<Badge[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    badgeApi.getAll().then(setBadges).finally(() => setLoading(false))
  }, [])

  if (!user) return null

  const earned = badges.filter(b => b.earned)
  const categories = ['LEARNING', 'MASTERY', 'FEYNMAN', 'PATH', 'EXPLORATION', 'XP', 'STREAK']
  const categoryLabels: Record<string, string> = {
    LEARNING: 'Learning Milestones', MASTERY: 'Mastery',
    FEYNMAN: 'Feynman Technique', PATH: 'Learning Path',
    EXPLORATION: 'Exploration', XP: 'XP Thresholds', STREAK: 'Streak Milestones'
  }

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        {/* Profile header */}
        <div className={styles.header}>
          <div className={styles.avatar}>{user.rank === 'Archmage' ? '\uD83E\uDDD9' : '\u2728'}</div>
          <div className={styles.info}>
            <h1 className={styles.username}>{user.username}</h1>
            <div className={styles.stats}>
              <span className={styles.statItem}>
                <span className={styles.statLabel}>Rank</span>
                <span className={styles.statValue}>{user.rank}</span>
              </span>
              <span className={styles.statItem}>
                <span className={styles.statLabel}>Total XP</span>
                <span className={styles.statValue}>{user.totalXp.toLocaleString()}</span>
              </span>
              <span className={styles.statItem}>
                <span className={styles.statLabel}>Streak</span>
                <span className={styles.statValue}>{user.streakDays}d</span>
              </span>
              <span className={styles.statItem}>
                <span className={styles.statLabel}>Badges</span>
                <span className={styles.statValue}>{earned.length}/{badges.length}</span>
              </span>
            </div>
          </div>
        </div>

        {/* Badge grid by category */}
        {loading ? (
          <p className={styles.loading}>Loading badges...</p>
        ) : (
          categories.map(cat => {
            const catBadges = badges.filter(b => b.category === cat)
            if (catBadges.length === 0) return null
            return (
              <section key={cat} className={styles.section}>
                <h2 className={styles.sectionTitle}>{categoryLabels[cat]}</h2>
                <div className={styles.grid}>
                  {catBadges.map(badge => (
                    <div key={badge.id}
                         className={`${styles.badgeCard} ${badge.earned ? styles.earned : styles.locked}`}>
                      <div className={styles.badgeGlyph}>{badge.earned ? badge.glyph : '\uD83D\uDD12'}</div>
                      <div className={styles.badgeName}>{badge.displayName}</div>
                      <div className={styles.badgeDesc}>{badge.description}</div>
                      {badge.earned && badge.earnedAt && (
                        <div className={styles.badgeDate}>
                          {new Date(badge.earnedAt).toLocaleDateString()}
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              </section>
            )
          })
        )}
      </div>
    </div>
  )
}
