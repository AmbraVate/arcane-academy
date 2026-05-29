import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { curiosityApi } from '@/shared/api/services'
import type { CuriosityQueueItem } from '@/shared/types'

export default function CuriosityQueuePage() {
  const navigate = useNavigate()
  const [items, setItems] = useState<CuriosityQueueItem[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    curiosityApi.getAll().then(setItems).catch(console.error).finally(() => setLoading(false))
  }, [])

  async function handleRemove(lessonId: string) {
    await curiosityApi.remove(lessonId)
    setItems(prev => prev.filter(i => i.lessonId !== lessonId))
  }

  if (loading) return <div className="flex items-center justify-center h-[60vh] text-muted"><p>Loading queue...</p></div>

  return (
    <div className="max-w-[600px] mx-auto px-4 py-6 pb-[60px] max-[480px]:px-3 max-[480px]:py-4">
      <button className="btn btn-ghost text-[12px] mb-4" onClick={() => navigate('/topics')}>â† Back to Dashboard</button>
      <h1 className="text-[22px] font-bold text-gold m-0 mb-1.5">ðŸ“Œ Curiosity Queue</h1>
      <p className="text-muted text-[13px] m-0 mb-5">Concepts you've saved for later exploration.</p>

      {items.length === 0 ? (
        <div className="text-center py-10 text-muted">
          <p>Your queue is empty. Save concepts while learning to revisit them later!</p>
        </div>
      ) : (
        <div className="flex flex-col gap-2">
          {items.map(item => (
            <div key={item.id} className="flex items-center justify-between bg-card border border-border rounded-[8px] px-4 py-3 max-[480px]:flex-wrap max-[480px]:gap-2">
              <div className="cursor-pointer flex-1" onClick={() => navigate(`/learn/${item.lessonId}`)}>
                <div className="text-[14px] font-semibold text-text">{item.lessonId}</div>
                <div className="text-[11px] text-muted mt-0.5">Saved {new Date(item.savedAt).toLocaleDateString()}</div>
              </div>
              <div className="flex gap-1.5 flex-shrink-0">
                <button className="btn btn-ghost text-[11px]" onClick={() => navigate(`/learn/${item.lessonId}`)}>Learn â†’</button>
                <button className="btn btn-ghost text-[11px] text-red" onClick={() => handleRemove(item.lessonId)}>Remove</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
