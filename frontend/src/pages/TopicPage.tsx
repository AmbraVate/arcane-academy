import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { dashboardApi } from '../api/services'
import { useAuth } from '../hooks/useAuth'
import type { DashboardDto, ChunkHealthDto } from '../types'
import styles from './TopicPage.module.css'

const TOPIC_META: Record<string, { name: string; glyph: string; tagline: string; color: string }> = {
  tailwind: {
    name: 'Tailwind CSS',
    glyph: '🎨',
    tagline: 'Compose beautiful interfaces with utility classes — no more naming paralysis.',
    color: 'teal',
  },
}

function ChunkCard({ ch, onClick }: { ch: ChunkHealthDto; onClick: () => void }) {
  const locked = ch.status === 'LOCKED'
  const done = ch.status === 'COMPLETE'
  const pct = Math.round(ch.memoryStrength * 100)
  return (
    <div
      className={`${styles.chunkCard} ${locked ? styles.locked : done ? styles.done : styles.active}`}
      onClick={onClick}
    >
      <div className={styles.chunkTop}>
        <span className={styles.chunkGlyph}>{locked ? '🔒' : ch.glyph}</span>
        <span className={`chip ${done ? 'chip-teal' : ch.status === 'IN_PROGRESS' ? 'chip-purple' : locked ? 'chip-gray' : 'chip-green'}`} style={{ fontSize: 10 }}>
          {done ? 'Complete' : ch.status === 'IN_PROGRESS' ? 'In Progress' : locked ? 'Locked' : 'Start'}
        </span>
      </div>
      <div className={styles.chunkTitle}>{ch.title}</div>
      <div className={styles.chunkMeta}>{ch.totalSubChunks} concepts</div>
      {!locked && (
        <div className={styles.progressRow}>
          <div className={styles.progressBar}>
            <div
              className={styles.progressFill}
              style={{ width: `${Math.round((ch.completedSubChunks / ch.totalSubChunks) * 100)}%` }}
            />
          </div>
          <span className={styles.progressLabel}>{ch.completedSubChunks}/{ch.totalSubChunks}</span>
        </div>
      )}
      {done && (
        <div className={styles.memRow}>
          <div className={styles.memBar}>
            <div className={`${styles.memFill} ${styles[`mem${ch.healthColor}`]}`} style={{ width: `${pct}%` }} />
          </div>
          <span className={styles.memLabel}>{pct}% memory</span>
        </div>
      )}
    </div>
  )
}

export default function TopicPage() {
  const { topicId } = useParams<{ topicId: string }>()
  const navigate = useNavigate()
  const { user } = useAuth()
  const [dashboard, setDashboard] = useState<DashboardDto | null>(null)
  const [loading, setLoading] = useState(true)

  const meta = TOPIC_META[topicId ?? ''] ?? { name: topicId, glyph: '📖', tagline: '', color: 'purple' }

  useEffect(() => {
    if (!topicId) return
    dashboardApi.get(topicId)
      .then(d => {
        if (!d.diagnosticCompleted) {
          navigate(`/topic/${topicId}/diagnostic`, { replace: true })
          return
        }
        setDashboard(d)
      })
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [topicId, navigate])

  if (loading) {
    return (
      <div className={styles.loading}>
        <div className={styles.loadingGlyph}>{meta.glyph}</div>
        <p>Loading {meta.name}...</p>
      </div>
    )
  }

  if (!dashboard || !user) return null

  const progressPct = Math.round(dashboard.overallProgress * 100)

  return (
    <div className={styles.page}>
      {/* Hero */}
      <div className={styles.hero}>
        <button className="btn btn-ghost" onClick={() => navigate('/topics')} style={{ fontSize: 12, alignSelf: 'flex-start' }}>
          ← All Topics
        </button>
        <div className={styles.heroGlyph}>{meta.glyph}</div>
        <h1 className={styles.heroTitle}>{meta.name}</h1>
        <p className={styles.heroTagline}>{meta.tagline}</p>
        <div className={styles.progressWrap}>
          <div className={styles.progressMeta}>
            <span>Overall mastery</span>
            <span>{progressPct}%</span>
          </div>
          <div className={styles.progressBar}>
            <div className={styles.progressFill} style={{ width: `${progressPct}%` }} />
          </div>
        </div>
      </div>

      {/* Action row */}
      {dashboard.reviewsDue > 0 && (
        <div className={styles.actionRow}>
          <div className={styles.actionCard} onClick={() => navigate('/review')}>
            <div className={styles.actionIcon}>📖</div>
            <div className={styles.actionTitle}>{dashboard.reviewsDue} Reviews Due</div>
            <div className={styles.actionDesc}>Strengthen fading memories before they slip away</div>
          </div>
        </div>
      )}

      {/* Chunk grid */}
      <div className={styles.sectionTitle}>Knowledge Chunks</div>
      <div className={styles.chunkGrid}>
        {dashboard.chunkHealth.map(ch => (
          <ChunkCard
            key={ch.chunkId}
            ch={ch}
            onClick={() => ch.status !== 'LOCKED' && navigate(`/chunk/${ch.chunkId}`)}
          />
        ))}
      </div>
    </div>
  )
}
