import { useParams, useNavigate } from 'react-router-dom'
import { useModuleDetail } from '@/hooks/queries'
import { useAuth } from '@/shared/hooks/useAuth'
import type { LessonSummary } from '@/shared/types'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { Lock, Check, Loader2, BookOpen, Code2, Zap, Wrench } from 'lucide-react'
import PublicBanner from '@/components/layout/PublicBanner'
import CompletionRing from '@/components/ui/CompletionRing'

// ── Phase ring (green) + memory ring (yellow) ─────────────────────────────────

const PHASE_PCT: Record<string, number> = {
  HOOK: 20, EXPLANATION: 40, GUIDED_PRACTICE: 60,
  SOLO_PRACTICE: 80, RETRIEVAL_CHECK: 90, INTEGRATION: 95, COMPLETE: 100,
}

function LessonRings({ sc }: { sc: LessonSummary }) {
  const isDone  = sc.status === 'COMPLETE' || sc.status === 'SKIPPED'
  const inProg  = sc.status === 'IN_PROGRESS'
  const memPct  = Math.round(sc.memoryStrength * 100)
  const phasePct = isDone ? 100 : inProg && sc.currentPhase ? (PHASE_PCT[sc.currentPhase] ?? 20) : 0

  const phaseColor  = isDone ? 'var(--teal)' : inProg ? 'var(--purple)' : 'var(--border)'
  const memoryColor = memPct > 60 ? 'var(--teal)' : memPct > 30 ? 'var(--orange)' : 'var(--red)'

  return (
    <div className="flex items-center gap-1.5 flex-shrink-0">
      {/* Yellow ring — memory retention (only once a lesson is done) */}
      {isDone && <CompletionRing pct={memPct} color={memoryColor} size={32} showCheck />}
      {/* Green ring — phase / completion progress */}
      <CompletionRing pct={phasePct} color={phaseColor} size={32} showCheck />
    </div>
  )
}

// ── Lesson row ────────────────────────────────────────────────────────────────

function LessonRow({ sc, navigate }: { sc: LessonSummary; navigate: (p?: string) => void }) {
  const isLocked = sc.status === 'LOCKED'
  const isDone   = sc.status === 'COMPLETE' || sc.status === 'SKIPPED'

  return (
    <div
      className={cn(
        'flex items-center gap-3.5 bg-card border rounded-[10px] px-4 py-3.5',
        'transition-[border-color] duration-200',
        isLocked ? 'opacity-45 cursor-not-allowed border-border' :
        isDone   ? 'cursor-pointer border-l-[3px] border-l-teal border-border hover:border-purple' :
                   'cursor-pointer border-border hover:border-purple',
      )}
      onClick={() => !isLocked && navigate(`/learn/${sc.id}`)}
    >
      {/* Number / status */}
      <div className={cn(
        'w-7 h-7 rounded-full flex items-center justify-center text-[12px] font-bold flex-shrink-0',
        isDone   ? 'bg-teal text-bg'        :
        isLocked ? 'bg-surface text-border' : 'bg-surface text-muted',
      )}>
        {isLocked ? <Lock size={12} strokeWidth={2} />
          : isDone ? <Check size={12} strokeWidth={2.5} />
          : sc.sortOrder}
      </div>

      {/* Title + chips */}
      <div className="flex-1 min-w-0">
        <div className="text-[14px] font-semibold text-text truncate">{sc.title}</div>
        <div className="flex items-center gap-2 mt-1 flex-wrap">
          <Badge variant={isDone ? 'active' : sc.status === 'IN_PROGRESS' ? 'application' : isLocked ? 'gray' : 'green'}>
            {sc.status === 'LOCKED'      ? 'Locked'        :
             sc.status === 'IN_PROGRESS' ? sc.currentPhase?.replace(/_/g, ' ') ?? 'In Progress' :
             isDone ? 'Done' : 'Start'}
          </Badge>
          {!isLocked && sc.learningObjectiveCount > 0 && (
            <span className="text-[10px] text-muted flex items-center gap-0.5">
              <BookOpen size={9} strokeWidth={1.75} /> {sc.learningObjectiveCount}
            </span>
          )}
          {!isLocked && sc.practiceType !== 'NONE' && (
            <span className="text-[10px] text-muted flex items-center gap-0.5">
              <Code2 size={9} strokeWidth={1.75} /> Practice
            </span>
          )}
          {!isLocked && sc.hasChallenge && (
            <span className="text-[10px] text-muted flex items-center gap-0.5">
              <Zap size={9} strokeWidth={1.75} /> Challenge
            </span>
          )}
          {!isLocked && sc.hasMiniProject && (
            <span className="text-[10px] text-muted flex items-center gap-0.5">
              <Wrench size={9} strokeWidth={1.75} /> Project
            </span>
          )}
        </div>
      </div>

      {/* Phase + memory rings */}
      {!isLocked && <LessonRings sc={sc} />}
    </div>
  )
}

// ── Page ──────────────────────────────────────────────────────────────────────

export default function TopicLessonsPage() {
  const { moduleId, topicId } = useParams<{ moduleId: string; topicId: string }>()
  const navigate = useNavigate()
  const { user } = useAuth()
  const isPublic = !user
  const { data: module, isLoading } = useModuleDetail(moduleId)

  if (isLoading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-muted">
        <Loader2 size={32} className="mb-3 animate-spin" color="var(--muted)" />
        <p>Loading topic...</p>
      </div>
    )
  }

  if (!module) return null

  const topic     = module.topics.find(t => t.id === topicId)
  const lessons   = module.lessons.filter(l => l.topicId === topicId)
  const completed = lessons.filter(l => l.status === 'COMPLETE' || l.status === 'SKIPPED').length

  function handleLessonClick(sc: LessonSummary) {
    if (isPublic) {
      sessionStorage.setItem('arcane-intended-path', `/learn/${sc.id}`)
      navigate('/register')
      return
    }
    if (sc.status !== 'LOCKED') navigate(`/learn/${sc.id}`)
  }

  return (
    <div className="max-w-[700px] mx-auto px-4 py-6 pb-[60px] max-[600px]:px-3 max-[600px]:py-4">
      {/* Back */}
      <button className="btn btn-ghost text-[12px] mb-4" onClick={() => navigate(`/chunk/${moduleId}`)}>
        ← {module.title}
      </button>

      {isPublic && (
        <PublicBanner message="Sign up free to start this lesson and track your progress." />
      )}

      {/* Topic header */}
      <div className="mb-7">
        <div className="flex items-center gap-2 mb-1">
          <span className="text-[11px] text-muted font-cinzel uppercase tracking-[0.08em]">Topic</span>
        </div>
        <h1 className="text-[24px] font-bold text-gold m-0 mb-2 max-[600px]:text-[20px]">
          {topic?.title ?? 'Topic'}
        </h1>
        {topic?.purposeHtml && (
          <div
            className="text-[13px] text-muted leading-[1.7]"
            dangerouslySetInnerHTML={{ __html: topic.purposeHtml }}
          />
        )}
        <div className="flex items-center gap-3 mt-3">
          <span className="text-[12px] text-muted">{completed}/{lessons.length} lessons complete</span>
          {lessons.length > 0 && (
            <div className="flex-1 max-w-[160px] h-1 bg-border rounded-full overflow-hidden">
              <div
                className="h-full bg-teal rounded-full transition-[width] duration-500"
                style={{ width: `${Math.round((completed / lessons.length) * 100)}%` }}
              />
            </div>
          )}
        </div>
      </div>

      {/* Lesson rows */}
      <div className="flex flex-col gap-2">
        {lessons.length > 0
          ? lessons.map(l => (
              <LessonRow
                key={l.id}
                sc={isPublic ? { ...l, status: l.status === 'LOCKED' ? 'LOCKED' : 'NOT_STARTED' } : l}
                navigate={() => handleLessonClick(l)}
              />
            ))
          : (
            <div className="text-center text-muted py-12 text-[14px]">
              No lessons available in this topic yet.
            </div>
          )}
      </div>
    </div>
  )
}
