import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { chunkApi } from '@/shared/api/services'
import type { ChunkDetail } from '@/shared/types'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { Lock, Check, Loader2 } from 'lucide-react'

const MEM_COLORS: Record<string, string> = {
  GREEN: 'bg-green', YELLOW: 'bg-orange', RED: 'bg-red',
}

export default function ChunkMapPage() {
  const { chunkId } = useParams<{ chunkId: string }>()
  const navigate = useNavigate()
  const [chunk, setChunk] = useState<ChunkDetail | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!chunkId) return
    chunkApi.getDetail(chunkId)
      .then(setChunk)
      .catch(() => navigate('/topics'))
      .finally(() => setLoading(false))
  }, [chunkId, navigate])

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-muted">
        <Loader2 size={32} className="mb-3 animate-spin" color="var(--muted)" />
        <p>Loading module...</p>
      </div>
    )
  }
  if (!chunk) return null

  const completed = chunk.subChunks.filter(s => s.status === 'COMPLETE' || s.status === 'SKIPPED').length

  return (
    <div className="max-w-[700px] mx-auto px-4 py-6 pb-[60px] max-[600px]:px-3 max-[600px]:py-4">
      <button className="btn btn-ghost text-[12px] mb-4" onClick={() => navigate(`/topic/${chunk.topicId}`)}>
        ← Back to Topic
      </button>

      <div className="flex items-center gap-4 mb-6 max-[600px]:gap-3">
        <div className="text-[48px] max-[600px]:text-[36px]">{chunk.glyph}</div>
        <div>
          <h1 className="text-[24px] font-bold text-gold m-0 max-[600px]:text-[20px]">{chunk.title}</h1>
          <div className="flex items-center gap-2.5 mt-1.5">
            <Badge variant={chunk.status === 'COMPLETE' ? 'active' : chunk.status === 'LOCKED' ? 'gray' : 'application'}>
              {chunk.status}
            </Badge>
            <span className="text-[12px] text-muted">{completed}/{chunk.subChunks.length} concepts</span>
          </div>
        </div>
      </div>

      <div className="flex flex-col gap-2">
        {chunk.subChunks.map(sc => {
          // Use the authoritative backend status — EncodingController + ChunkController
          // both set "LOCKED" for sub-chunks whose sequential prerequisite is not met.
          const isLocked = sc.status === 'LOCKED'
          const isDone   = sc.status === 'COMPLETE' || sc.status === 'SKIPPED'

          return (
            <div
              key={sc.id}
              className={cn(
                'bg-card border rounded-[10px] px-4 py-3.5 transition-[border-color] duration-200',
                'max-[600px]:px-3 max-[600px]:py-3',
                isLocked ? 'opacity-50 cursor-not-allowed border-border' :
                isDone   ? 'cursor-pointer border-l-[3px] border-l-teal border-border hover:border-purple' :
                           'cursor-pointer border-border hover:border-purple',
              )}
              onClick={() => !isLocked && navigate(`/learn/${sc.id}`)}
            >
              {/* ── Top row: number/status + title + XP/memory ── */}
              <div className="flex items-start gap-3.5 max-[600px]:gap-2.5">
                <div className={cn(
                  'w-7 h-7 rounded-full flex items-center justify-center text-[13px] font-bold flex-shrink-0 mt-0.5',
                  isDone   ? 'bg-teal text-bg'        :
                  isLocked ? 'bg-surface text-border' :
                             'bg-surface text-muted',
                )}>
                  {isLocked
                    ? <Lock size={13} strokeWidth={2} />
                    : isDone
                      ? <Check size={13} strokeWidth={2.5} />
                      : sc.sortOrder}
                </div>

                <div className="flex-1 min-w-0">
                  <div className="text-[14px] font-semibold text-text max-[600px]:text-[13px]">{sc.title}</div>
                  <div className="flex items-center gap-2 mt-0.5">
                    <Badge variant={isDone ? 'active' : sc.status === 'IN_PROGRESS' ? 'application' : isLocked ? 'gray' : sc.status === 'COMPRESSED' ? 'gold' : 'green'}>
                      {sc.status === 'LOCKED'     ? 'Locked'       :
                       sc.status === 'COMPRESSED' ? 'Quick review' :
                       isDone ? sc.status.charAt(0) + sc.status.slice(1).toLowerCase() :
                       sc.status.replace('_', ' ')}
                    </Badge>
                    {sc.currentPhase && sc.status === 'IN_PROGRESS' && (
                      <span className="text-[10px] text-muted uppercase">{sc.currentPhase.replace('_', ' ')}</span>
                    )}
                  </div>
                </div>

                <div className="flex flex-col items-end gap-1.5 flex-shrink-0">
                  {!isLocked && !isDone && (
                    <Badge variant="green">+{sc.xpReward} xp</Badge>
                  )}
                  {isDone && sc.memoryStrength !== undefined && (
                    <div className="w-[60px] h-1 bg-surface rounded-full overflow-hidden max-[600px]:w-12">
                      <div
                        className={cn('h-full rounded-full', MEM_COLORS[sc.healthColor ?? 'GREEN'] ?? 'bg-green')}
                        style={{ width: `${Math.round(sc.memoryStrength * 100)}%` }}
                      />
                    </div>
                  )}
                </div>
              </div>

              {/* ── Chip metadata row (only when there's content metadata) ── */}
              {!isLocked && (sc.learningObjectiveCount > 0 || sc.hasChallenge || sc.hasMiniProject || sc.practiceType !== 'NONE') && (
                <div className="flex items-center gap-2 mt-2.5 pl-[46px] flex-wrap max-[600px]:pl-[38px]">
                  {sc.learningObjectiveCount > 0 && (
                    <span className="text-[10px] text-muted flex items-center gap-1">
                      📚 {sc.learningObjectiveCount} objective{sc.learningObjectiveCount !== 1 ? 's' : ''}
                    </span>
                  )}
                  {sc.practiceType !== 'NONE' && (
                    <span className="text-[10px] text-muted flex items-center gap-1">🧪 Sandbox</span>
                  )}
                  {sc.hasChallenge && (
                    <span className="text-[10px] text-muted flex items-center gap-1">⚡ Challenge</span>
                  )}
                  {sc.hasMiniProject && (
                    <span className="text-[10px] text-muted flex items-center gap-1">🏗 Mini project</span>
                  )}
                </div>
              )}
            </div>
          )
        })}
      </div>

    </div>
  )
}
