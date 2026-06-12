import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { moduleApi } from '@/shared/api/services'
import { useAuth } from '@/shared/hooks/useAuth'
import type { ModuleDetail, LessonSummary, Topic } from '@/shared/types'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { Lock, Check, Loader2, Layers, ChevronRight } from 'lucide-react'
import PublicBanner from '@/components/layout/PublicBanner'
import CompletionRing from '@/components/ui/CompletionRing'

// ── Topic row ─────────────────────────────────────────────────────────────────

function TopicRow({
  topic,
  topicLessons,
  moduleId,
  navigate,
  forceUnlocked = false,
}: {
  topic: Topic
  topicLessons: LessonSummary[]
  moduleId: string
  navigate: (path: string) => void
  forceUnlocked?: boolean
}) {
  const completed = topicLessons.filter(l => l.status === 'COMPLETE' || l.status === 'SKIPPED').length
  const total     = topicLessons.length
  const pct       = total > 0 ? Math.round((completed / total) * 100) : 0
  const isDone    = pct === 100 && total > 0
  const isLocked  = !forceUnlocked && total > 0 && topicLessons[0].status === 'LOCKED'
  const inProg    = !isLocked && !isDone && completed > 0

  const color = isDone ? 'var(--teal)' : inProg ? 'var(--purple)' : 'var(--border)'

  return (
    <div
      className={cn(
        'flex items-center gap-3.5 bg-card border rounded-[10px] px-4 py-3.5',
        'transition-[border-color,transform] duration-200',
        isLocked
          ? 'opacity-40 cursor-not-allowed border-border saturate-[0.3]'
          : 'cursor-pointer border-border hover:border-purple hover:-translate-y-px',
      )}
      onClick={() => !isLocked && navigate(`/module/${moduleId}/topic/${topic.id}`)}
    >
      {/* Lock / number */}
      <div className={cn(
        'w-7 h-7 rounded-full flex items-center justify-center text-[12px] font-bold flex-shrink-0',
        isDone    ? 'bg-teal text-bg'          :
        isLocked  ? 'bg-surface text-border'   :
        inProg    ? 'bg-purple-dim text-purple-light' : 'bg-surface text-muted',
      )}>
        {isLocked ? <Lock size={12} strokeWidth={2} />
          : isDone ? <Check size={12} strokeWidth={2.5} />
          : <Layers size={12} strokeWidth={1.75} />}
      </div>

      {/* Title + lesson count */}
      <div className="flex-1 min-w-0">
        <div className="text-[14px] font-semibold text-text truncate">{topic.title}</div>
        <div className="text-[11px] text-muted mt-0.5">
          {total} lesson{total !== 1 ? 's' : ''}
          {inProg && <span className="ml-2 text-purple-light font-cinzel text-[10px] uppercase">In Progress</span>}
        </div>
      </div>

      {/* Completion ring */}
      <CompletionRing pct={isLocked ? 0 : pct} color={color} size={38} showCheck />

      {/* Chevron */}
      {!isLocked && (
        <ChevronRight size={14} color="var(--muted)" strokeWidth={1.75} className="flex-shrink-0" />
      )}
    </div>
  )
}

// ── Flat lesson card (fallback when no topics) ────────────────────────────────

const MEM_COLORS: Record<string, string> = {
  GREEN: 'bg-green', YELLOW: 'bg-orange', RED: 'bg-red',
}

function LessonCard({ sc, navigate }: { sc: LessonSummary; navigate: (path: string) => void }) {
  const isLocked = sc.status === 'LOCKED'
  const isDone   = sc.status === 'COMPLETE' || sc.status === 'SKIPPED'
  const memPct   = Math.round(sc.memoryStrength * 100)

  return (
    <div
      className={cn(
        'flex items-center gap-3.5 bg-card border rounded-[10px] px-4 py-3.5',
        'transition-[border-color] duration-200',
        isLocked ? 'opacity-50 cursor-not-allowed border-border' :
        isDone   ? 'cursor-pointer border-l-[3px] border-l-teal border-border hover:border-purple' :
                   'cursor-pointer border-border hover:border-purple',
      )}
      onClick={() => !isLocked && navigate(`/learn/${sc.id}`)}
    >
      {/* Number / status dot */}
      <div className={cn(
        'w-7 h-7 rounded-full flex items-center justify-center text-[12px] font-bold flex-shrink-0',
        isDone   ? 'bg-teal text-bg'        :
        isLocked ? 'bg-surface text-border' : 'bg-surface text-muted',
      )}>
        {isLocked ? <Lock size={12} strokeWidth={2} />
          : isDone ? <Check size={12} strokeWidth={2.5} />
          : sc.sortOrder}
      </div>

      {/* Title */}
      <div className="flex-1 min-w-0">
        <div className="text-[14px] font-semibold text-text truncate">{sc.title}</div>
        <div className="flex items-center gap-2 mt-0.5">
          <Badge variant={isDone ? 'active' : sc.status === 'IN_PROGRESS' ? 'application' : isLocked ? 'gray' : 'green'}>
            {sc.status === 'LOCKED' ? 'Locked' : isDone ? 'Done' : sc.status.replace('_', ' ')}
          </Badge>
          {sc.currentPhase && sc.status === 'IN_PROGRESS' && (
            <span className="text-[10px] text-muted uppercase">{sc.currentPhase.replace('_', ' ')}</span>
          )}
        </div>
      </div>

      {/* Memory circles */}
      {isDone && (
        <div className="flex flex-col items-end gap-1 flex-shrink-0">
          {/* Memory bar */}
          <div className="w-[48px] h-[3px] bg-border rounded-full overflow-hidden">
            <div
              className={cn('h-full rounded-full', MEM_COLORS[sc.healthColor ?? 'GREEN'] ?? 'bg-green')}
              style={{ width: `${memPct}%` }}
            />
          </div>
          <span className="text-[9px] text-muted">{memPct}% mem</span>
        </div>
      )}
    </div>
  )
}

// ── Module map page ───────────────────────────────────────────────────────────

export default function ModuleMapPage() {
  const { moduleId } = useParams<{ moduleId: string }>()
  const navigate = useNavigate()
  const { user } = useAuth()
  const isPublic = !user
  const [chunk, setChunk] = useState<ModuleDetail | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(false)

  useEffect(() => {
    if (!moduleId) return
    setError(false)
    moduleApi.getDetail(moduleId)
      .then(setChunk)
      .catch(() => setError(true))
      .finally(() => setLoading(false))
  }, [moduleId])

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-muted">
        <Loader2 size={32} className="mb-3 animate-spin" color="var(--muted)" />
        <p>Loading module...</p>
      </div>
    )
  }
  if (error || !chunk) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-muted gap-4">
        <p className="text-[14px]">Module not found or failed to load.</p>
        <button className="btn btn-ghost text-[12px]" onClick={() => navigate(-1)}>← Go Back</button>
      </div>
    )
  }

  // Group lessons by topicId
  const lessonsByTopic = new Map<string, LessonSummary[]>()
  for (const lesson of chunk.lessons) {
    if (lesson.topicId) {
      const arr = lessonsByTopic.get(lesson.topicId) ?? []
      arr.push(lesson)
      lessonsByTopic.set(lesson.topicId, arr)
    }
  }

  const hasTopics = chunk.topics.length > 0 && chunk.lessons.some(l => l.topicId != null)
  const completed = chunk.lessons.filter(s => s.status === 'COMPLETE' || s.status === 'SKIPPED').length

  return (
    <div className="max-w-[700px] mx-auto px-4 py-6 pb-[60px] max-[600px]:px-3 max-[600px]:py-4">
      <button className="btn btn-ghost text-[12px] mb-4" onClick={() => navigate(`/pathway/${chunk.domainId}`)}>
        ← Back to Pathway
      </button>

      {isPublic && (
        <PublicBanner message="You're viewing the module structure. Sign up free to start learning." />
      )}

      {/* Module header */}
      <div className="flex items-center gap-4 mb-7 max-[600px]:gap-3">
        <div className="text-[48px] max-[600px]:text-[36px]">{chunk.glyph}</div>
        <div>
          <h1 className="text-[24px] font-bold text-gold m-0 max-[600px]:text-[20px]">{chunk.title}</h1>
          <div className="flex items-center gap-2.5 mt-1.5">
            <Badge variant={chunk.status === 'COMPLETE' ? 'active' : chunk.status === 'LOCKED' ? 'gray' : 'application'}>
              {chunk.status}
            </Badge>
            <span className="text-[12px] text-muted">
              {hasTopics
                ? `${chunk.topics.length} topics · ${chunk.lessons.length} lessons`
                : `${completed}/${chunk.lessons.length} lessons`}
            </span>
          </div>
        </div>
      </div>

      {hasTopics ? (
        /* ── Topic rows ── */
        <div className="flex flex-col gap-2">
          {chunk.topics.map(topic => {
            const topicLessons = lessonsByTopic.get(topic.id) ?? []
            if (topicLessons.length === 0) return null
            // Public users can browse topic structure — lock only comes at lesson-start
            const isTopicLocked = !isPublic && topicLessons.length > 0 && topicLessons[0].status === 'LOCKED'
            return (
              <TopicRow
                key={topic.id}
                topic={{ ...topic }}
                topicLessons={topicLessons}
                moduleId={chunk.id}
                navigate={navigate}
                forceUnlocked={isPublic && isTopicLocked}
              />
            )
          })}
        </div>
      ) : (
        /* ── Flat lesson list fallback ── */
        <div className="flex flex-col gap-2">
          {chunk.lessons.map(sc => (
            <LessonCard key={sc.id} sc={sc} navigate={navigate} />
          ))}
        </div>
      )}
    </div>
  )
}
