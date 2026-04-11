import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { dashboardApi } from '../api/services'
import type { DashboardDto } from '../types'
import styles from './DashboardPage.module.css'

export default function DashboardPage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [dashboard, setDashboard] = useState<DashboardDto | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    dashboardApi.get()
      .then(setDashboard)
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [])

  if (loading) {
    return (
      <div className={styles.loading}>
        <div className={styles.loadingGlyph}>🧙</div>
        <p>Consulting the Grimoire...</p>
      </div>
    )
  }

  if (!dashboard || !user) return null

  const progressPct = Math.round(dashboard.overallProgress * 100)

  return (
    <div className={styles.page}>
      {/* Hero */}
      <div className={styles.hero}>
        <div className={styles.glyph}>🧙</div>
        <h1 className={styles.heroTitle}>The Java Grimoire</h1>
        <p className={styles.heroSub}>Your path from apprentice to Archmage</p>
        <div className={styles.progressWrap}>
          <div className={styles.progressLabel}>
            <span>Overall mastery</span>
            <span>{progressPct}%</span>
          </div>
          <div className={styles.progressBar}>
            <div className={styles.progressFill} style={{ width: `${progressPct}%` }} />
          </div>
        </div>
      </div>

      {/* Stats row */}
      <div className={styles.statsRow}>
        <div className={styles.statCard}>
          <div className={styles.statVal}>{dashboard.totalXp}</div>
          <div className={styles.statLbl}>total xp</div>
        </div>
        <div className={styles.statCard}>
          <div className={styles.statVal}>{dashboard.rank}</div>
          <div className={styles.statLbl}>rank</div>
        </div>
        <div className={styles.statCard}>
          <div className={`${styles.statVal} ${dashboard.streakAtRisk ? styles.streakAtRisk : ''}`}>
            🔥 {dashboard.streakDays}
          </div>
          <div className={styles.statLbl}>day streak</div>
        </div>
        <div className={styles.statCard}>
          <div className={styles.statVal}>{dashboard.currentPath}</div>
          <div className={styles.statLbl}>path</div>
        </div>
      </div>

      {/* Action cards */}
      <div className={styles.actionRow}>
        {!dashboard.diagnosticCompleted && (
          <div className={styles.actionCard} onClick={() => navigate('/diagnostic')}>
            <div className={styles.actionIcon}>🔮</div>
            <div className={styles.actionTitle}>Take Entry Diagnostic</div>
            <div className={styles.actionDesc}>Find your starting point — skip what you already know</div>
          </div>
        )}
        {dashboard.reviewsDue > 0 && (
          <div className={styles.actionCard} onClick={() => navigate('/review')}>
            <div className={styles.actionIcon}>📖</div>
            <div className={styles.actionTitle}>{dashboard.reviewsDue} Reviews Due</div>
            <div className={styles.actionDesc}>Strengthen fading memories before they slip away</div>
          </div>
        )}
      </div>

      {/* Chunk health grid */}
      <div className={styles.sectionTitle}>Knowledge Map</div>
      <div className={styles.chunkGrid}>
        {dashboard.chunkHealth.map(ch => (
          <div
            key={ch.chunkId}
            className={`${styles.chunkCard} ${styles[`health${ch.healthColor}`]}`}
            onClick={() => navigate(`/chunk/${ch.chunkId}`)}
          >
            <div className={styles.chunkGlyph}>{ch.glyph}</div>
            <div className={styles.chunkTitle}>{ch.chunkTitle}</div>
            <div className={styles.chunkProgress}>
              {ch.completedSubChunks}/{ch.totalSubChunks} concepts
            </div>
            <div className={styles.strengthBar}>
              <div
                className={styles.strengthFill}
                style={{ width: `${Math.round(ch.memoryStrength * 100)}%` }}
              />
            </div>
            <div className={styles.strengthLabel}>
              {Math.round(ch.memoryStrength * 100)}% memory
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
