import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { dashboardApi } from '../api/services'
import type { DashboardDto, ChunkHealthDto } from '../types'
import styles from './DashboardPage.module.css'

const TIERS = [
  {
    key: 'FOUNDATION',
    label: 'Foundational',
    glyph: '🌱',
    desc: 'Core building blocks — variables, control flow, loops, and methods.',
    color: 'teal',
  },
  {
    key: 'PRACTITIONER',
    label: 'Practitioner',
    glyph: '⚗️',
    desc: 'Applied mastery — arrays, collections, classes, encapsulation, and inheritance.',
    color: 'purple',
  },
  {
    key: 'EXPERT',
    label: 'Expert',
    glyph: '🔮',
    desc: 'Advanced craft — polymorphism, exception handling, and core APIs.',
    color: 'gold',
  },
]

function ChunkCard({ ch, onClick }: { ch: ChunkHealthDto; onClick: () => void }) {
  const locked = ch.status === 'LOCKED'
  return (
    <div
      className={`${styles.chunkCard} ${locked ? styles.chunkCardLocked : styles[`health${ch.healthColor}`]}`}
      onClick={onClick}
    >
      <div className={styles.chunkGlyph}>{locked ? '🔒' : ch.glyph}</div>
      <div className={styles.chunkTitle}>{ch.title}</div>
      <div className={styles.chunkProgress}>
        {locked ? 'Locked' : `${ch.completedSubChunks}/${ch.totalSubChunks} concepts`}
      </div>
      {!locked && (
        <>
          <div className={styles.strengthBar}>
            <div
              className={styles.strengthFill}
              style={{ width: `${Math.round(ch.memoryStrength * 100)}%` }}
            />
          </div>
          <div className={styles.strengthLabel}>
            {Math.round(ch.memoryStrength * 100)}% memory
          </div>
        </>
      )}
    </div>
  )
}

export default function DashboardPage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [dashboard, setDashboard] = useState<DashboardDto | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    dashboardApi.get()
      .then(d => {
        if (!d.diagnosticCompleted) {
          navigate('/onboarding', { replace: true })
          return
        }
        setDashboard(d)
      })
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [navigate])

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

  const chunksByTier = (tier: string) =>
    dashboard.chunkHealth.filter(ch => (ch.tier ?? 'FOUNDATION') === tier)

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

      {/* Three-tier knowledge map */}
      <div className={styles.sectionTitle}>Knowledge Map</div>

      {TIERS.map(tier => {
        const chunks = chunksByTier(tier.key)
        if (chunks.length === 0) return null
        return (
          <div key={tier.key} className={`${styles.tierSection} ${styles[`tier${tier.key}`]}`}>
            <div className={styles.tierHeader}>
              <span className={styles.tierGlyph}>{tier.glyph}</span>
              <div>
                <div className={styles.tierLabel}>{tier.label}</div>
                <div className={styles.tierDesc}>{tier.desc}</div>
              </div>
              <div className={styles.tierProgress}>
                {chunks.filter(c => c.status === 'COMPLETE').length}/{chunks.length}
              </div>
            </div>
            <div className={styles.chunkGrid}>
              {chunks.map(ch => (
                <ChunkCard
                  key={ch.chunkId}
                  ch={ch}
                  onClick={() => ch.status !== 'LOCKED' && navigate(`/chunk/${ch.chunkId}`)}
                />
              ))}
            </div>
          </div>
        )
      })}
    </div>
  )
}
