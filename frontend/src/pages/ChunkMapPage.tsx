import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { chunkApi, rabbitHoleApi } from '../api/services'
import type { ChunkDetail, RabbitHoleModule } from '../types'
import styles from './ChunkMapPage.module.css'

export default function ChunkMapPage() {
  const { chunkId } = useParams<{ chunkId: string }>()
  const navigate = useNavigate()
  const [chunk, setChunk] = useState<ChunkDetail | null>(null)
  const [rabbitHoles, setRabbitHoles] = useState<RabbitHoleModule[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!chunkId) return
    chunkApi.getDetail(chunkId)
      .then(setChunk)
      .catch(() => navigate('/'))
      .finally(() => setLoading(false))
    rabbitHoleApi.getForChunk(chunkId)
      .then(setRabbitHoles)
      .catch(() => {})
  }, [chunkId, navigate])

  if (loading) {
    return (
      <div className={styles.loading}>
        <div className={styles.loadingGlyph}>📜</div>
        <p>Loading chunk...</p>
      </div>
    )
  }
  if (!chunk) return null

  const completed = chunk.subChunks.filter(s => s.status === 'COMPLETE' || s.status === 'SKIPPED').length

  return (
    <div className={styles.page}>
      <button className="btn btn-ghost" onClick={() => navigate(`/topics/${chunk.topicId}`)} style={{ marginBottom: 16, fontSize: 12 }}>
        ← Back to Topic
      </button>

      <div className={styles.header}>
        <div className={styles.glyph}>{chunk.glyph}</div>
        <div>
          <h1 className={styles.title}>{chunk.title}</h1>
          <div className={styles.meta}>
            <span className={`chip ${chunk.status === 'COMPLETE' ? 'chip-teal' : chunk.status === 'LOCKED' ? 'chip-gray' : 'chip-purple'}`}>
              {chunk.status}
            </span>
            <span className={styles.count}>{completed}/{chunk.subChunks.length} concepts</span>
          </div>
        </div>
      </div>

      <div className={styles.subChunkList}>
        {chunk.subChunks.map((sc, i) => {
          const isLocked = sc.status === 'NOT_STARTED' && i > 0 &&
            chunk.subChunks[i - 1].status !== 'COMPLETE' &&
            chunk.subChunks[i - 1].status !== 'SKIPPED'
          const isDone = sc.status === 'COMPLETE' || sc.status === 'SKIPPED'

          return (
            <div
              key={sc.id}
              className={`${styles.scCard} ${isLocked ? styles.locked : ''} ${isDone ? styles.done : ''}`}
              onClick={() => !isLocked && navigate(`/learn/${sc.id}`)}
            >
              <div className={styles.scOrder}>{sc.sortOrder}</div>
              <div className={styles.scInfo}>
                <div className={styles.scTitle}>{sc.title}</div>
                <div className={styles.scMeta}>
                  <span className={`chip ${isDone ? 'chip-teal' : sc.status === 'IN_PROGRESS' ? 'chip-purple' : sc.status === 'COMPRESSED' ? 'chip-gold' : 'chip-gray'}`} style={{ fontSize: 10 }}>
                    {sc.status === 'COMPRESSED' ? 'Quick review' : sc.status.replace('_', ' ')}
                  </span>
                  {sc.currentPhase && sc.status === 'IN_PROGRESS' && (
                    <span className={styles.phase}>{sc.currentPhase.replace('_', ' ')}</span>
                  )}
                </div>
              </div>
              <div className={styles.scRight}>
                {isLocked && <span className={styles.lockIcon}>🔒</span>}
                {isDone && <span className={styles.doneIcon}>✓</span>}
                {!isLocked && !isDone && (
                  <span className="chip chip-green" style={{ fontSize: 10 }}>+{sc.xpReward} xp</span>
                )}
                {isDone && sc.memoryStrength !== undefined && (
                  <div className={styles.memBar}>
                    <div
                      className={`${styles.memFill} ${styles[`mem${sc.healthColor}`]}`}
                      style={{ width: `${Math.round(sc.memoryStrength * 100)}%` }}
                    />
                  </div>
                )}
              </div>
            </div>
          )
        })}
      </div>

      {rabbitHoles.length > 0 && (
        <div className={styles.rhSection}>
          <div className={styles.rhHeading}>🐇 Rabbit Holes</div>
          <p className={styles.rhDesc}>Optional deep-dives — explore when curious.</p>
          <div className={styles.rhList}>
            {rabbitHoles.map(rh => (
              <div
                key={rh.id}
                className={styles.rhCard}
                onClick={() => navigate(`/rabbit-hole/${rh.id}`)}
              >
                <span className={styles.rhTitle}>{rh.title}</span>
                <span className={styles.rhArrow}>→</span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
