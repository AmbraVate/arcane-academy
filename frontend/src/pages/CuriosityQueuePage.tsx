import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { curiosityApi } from '../api/services'
import type { CuriosityQueueItem } from '../types'
import styles from './CuriosityQueuePage.module.css'

export default function CuriosityQueuePage() {
  const navigate = useNavigate()
  const [items, setItems] = useState<CuriosityQueueItem[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    curiosityApi.getAll()
      .then(setItems)
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [])

  async function handleRemove(subChunkId: string) {
    await curiosityApi.remove(subChunkId)
    setItems(prev => prev.filter(i => i.subChunkId !== subChunkId))
  }

  if (loading) return <div className={styles.loading}><p>Loading queue...</p></div>

  return (
    <div className={styles.page}>
      <button className="btn btn-ghost" onClick={() => navigate('/')} style={{ marginBottom: 16, fontSize: 12 }}>
        ← Back to Dashboard
      </button>
      <h1 className={styles.title}>📌 Curiosity Queue</h1>
      <p className={styles.desc}>Concepts you've saved for later exploration.</p>

      {items.length === 0 ? (
        <div className={styles.empty}>
          <p>Your queue is empty. Save concepts while learning to revisit them later!</p>
        </div>
      ) : (
        <div className={styles.list}>
          {items.map(item => (
            <div key={item.id} className={styles.item}>
              <div className={styles.itemInfo} onClick={() => navigate(`/learn/${item.subChunkId}`)}>
                <div className={styles.itemId}>{item.subChunkId}</div>
                <div className={styles.itemDate}>Saved {new Date(item.savedAt).toLocaleDateString()}</div>
              </div>
              <div className={styles.itemActions}>
                <button className="btn btn-ghost" style={{ fontSize: 11 }} onClick={() => navigate(`/learn/${item.subChunkId}`)}>
                  Learn →
                </button>
                <button className="btn btn-ghost" style={{ fontSize: 11, color: 'var(--red)' }} onClick={() => handleRemove(item.subChunkId)}>
                  Remove
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
