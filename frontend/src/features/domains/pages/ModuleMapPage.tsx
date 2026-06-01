import { useEffect, useState } from 'react'
import { useParams, useNavigate, NavigateFunction } from 'react-router-dom'
import { moduleApi, rabbitHoleApi } from '@/shared/api/services'
import type { ModuleDetail, RabbitHoleModule, LessonSummary, Topic } from '@/shared/types'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { Lock, Check, Rabbit, ArrowRight, Loader2, BookOpen, Code2, Zap, Wrench, Layers } from 'lucide-react'

const MEM_COLORS: Record<string, string> = {
  GREEN: 'bg-green', YELLOW: 'bg-orange', RED: 'bg-red',
}

// ── Lesson card ─────────────────────────────────────────────────────────────

function LessonCard({ sc, navigate }: { sc: LessonSummary; navigate: NavigateFunction }) {
  const isLocked = sc.status === 'LOCKED'
  const isDone   = sc.status === 'COMPLETE' || sc.status === 'SKIPPED'
  return (
    <div
      className={cn(
        'bg-card border rounded-[10px] px-4 py-3.5 transition-[border-color] duration-200',
        'max-[600px]:px-3 max-[600px]:py-3',
        isLocked ? 'opacity-50 cursor-not-allowed border-border' :
        isDone   ? 'cursor-pointer border-l-[3px] border-l-teal border-border hover:border-purple' :
                   'cursor-pointer border-border hover:border-purple',
      )}
      onClick={() => !isLocked && navigate(`/learn/${sc.id}`)}
    >
      {/* ── Top row ── */}
      <div className="flex items-start gap-3.5 max-[600px]:gap-2.5">
        <div className={cn(
          'w-7 h-7 rounded-full flex items-center justify-center text-[13px] font-bold flex-shrink-0 mt-0.5',
          isDone   ? 'bg-teal text-bg'        :
          isLocked ? 'bg-surface text-border' : 'bg-surface text-muted',
        )}>
          {isLocked ? <Lock size={13} strokeWidth={2} />
            : isDone ? <Check size={13} strokeWidth={2.5} />
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
          {!isLocked && !isDone && <Badge variant="green">+{sc.xpReward} xp</Badge>}
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
      {/* ── Chip row ── */}
      {!isLocked && (sc.learningObjectiveCount > 0 || sc.hasChallenge || sc.hasMiniProject || sc.practiceType !== 'NONE') && (
        <div className="flex items-center gap-2 mt-2.5 pl-[46px] flex-wrap max-[600px]:pl-[38px]">
          {sc.learningObjectiveCount > 0 && (
            <span className="text-[10px] text-muted flex items-center gap-1">
              <BookOpen size={10} strokeWidth={1.75} />
              {sc.learningObjectiveCount} objective{sc.learningObjectiveCount !== 1 ? 's' : ''}
            </span>
          )}
          {sc.practiceType !== 'NONE' && (
            <span className="text-[10px] text-muted flex items-center gap-1">
              <Code2 size={10} strokeWidth={1.75} /> Sandbox
            </span>
          )}
          {sc.hasChallenge && (
            <span className="text-[10px] text-muted flex items-center gap-1">
              <Zap size={10} strokeWidth={1.75} /> Challenge
            </span>
          )}
          {sc.hasMiniProject && (
            <span className="text-[10px] text-muted flex items-center gap-1">
              <Wrench size={10} strokeWidth={1.75} /> Mini project
            </span>
          )}
        </div>
      )}
    </div>
  )
}

// ── Topic-grouped lesson list ────────────────────────────────────────────────
// When topics are present, lessons are rendered under a labelled topic header.
// Falls back to a flat list if every lesson's topicId is null.

function TopicGroupedLessons({
  lessons, topics, navigate
}: {
  lessons: LessonSummary[]
  topics: Topic[]
  navigate: NavigateFunction
}) {
  const hasTopicInfo = lessons.some(l => l.topicId != null) && topics.length > 0

  if (!hasTopicInfo) {
    // Flat fallback (pre-Phase 1 content or content without topic assignment)
    return (
      <div className="flex flex-col gap-2">
        {lessons.map(sc => <LessonCard key={sc.id} sc={sc} navigate={navigate} />)}
      </div>
    )
  }

  // Group lessons by topicId; preserve topic sort order from server
  const lessonsByTopic = new Map<string, LessonSummary[]>()
  const ungrouped: LessonSummary[] = []
  for (const lesson of lessons) {
    if (lesson.topicId) {
      const arr = lessonsByTopic.get(lesson.topicId) ?? []
      arr.push(lesson)
      lessonsByTopic.set(lesson.topicId, arr)
    } else {
      ungrouped.push(lesson)
    }
  }

  return (
    <div className="flex flex-col gap-6">
      {topics.map(topic => {
        const topicLessons = lessonsByTopic.get(topic.id) ?? []
        if (topicLessons.length === 0) return null
        return (
          <div key={topic.id}>
            {/* Topic header */}
            <div className="flex items-center gap-2 mb-2.5 px-1">
              <Layers size={13} strokeWidth={1.75} className="text-purple-light flex-shrink-0" />
              <span className="font-cinzel text-[12px] text-purple-light tracking-[0.08em] uppercase">
                {topic.title}
              </span>
              <div className="flex-1 h-px bg-border ml-1" />
            </div>
            <div className="flex flex-col gap-2">
              {topicLessons.map(sc => <LessonCard key={sc.id} sc={sc} navigate={navigate} />)}
            </div>
          </div>
        )
      })}
      {ungrouped.length > 0 && (
        <div className="flex flex-col gap-2">
          {ungrouped.map(sc => <LessonCard key={sc.id} sc={sc} navigate={navigate} />)}
        </div>
      )}
    </div>
  )
}

export default function ModuleMapPage() {
  const { moduleId } = useParams<{ moduleId: string }>()
  const navigate = useNavigate()
  const [chunk, setChunk] = useState<ModuleDetail | null>(null)
  const [rabbitHoles, setRabbitHoles] = useState<RabbitHoleModule[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!moduleId) return
    moduleApi.getDetail(moduleId)
      .then(setChunk)
      .catch(() => navigate('/domains'))
      .finally(() => setLoading(false))
    rabbitHoleApi.getForChunk(moduleId).then(setRabbitHoles).catch(() => {})
  }, [moduleId, navigate])

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-muted">
        <Loader2 size={32} className="mb-3 animate-spin" color="var(--muted)" />
        <p>Loading module...</p>
      </div>
    )
  }
  if (!chunk) return null

  const completed = chunk.lessons.filter(s => s.status === 'COMPLETE' || s.status === 'SKIPPED').length

  return (
    <div className="max-w-[700px] mx-auto px-4 py-6 pb-[60px] max-[600px]:px-3 max-[600px]:py-4">
      <button className="btn btn-ghost text-[12px] mb-4" onClick={() => navigate(`/domain/${chunk.domainId}`)}>
        ← Back to Domain
      </button>

      <div className="flex items-center gap-4 mb-6 max-[600px]:gap-3">
        <div className="text-[48px] max-[600px]:text-[36px]">{chunk.glyph}</div>
        <div>
          <h1 className="text-[24px] font-bold text-gold m-0 max-[600px]:text-[20px]">{chunk.title}</h1>
          <div className="flex items-center gap-2.5 mt-1.5">
            <Badge variant={chunk.status === 'COMPLETE' ? 'active' : chunk.status === 'LOCKED' ? 'gray' : 'application'}>
              {chunk.status}
            </Badge>
            <span className="text-[12px] text-muted">{completed}/{chunk.lessons.length} concepts</span>
          </div>
        </div>
      </div>

      <TopicGroupedLessons lessons={chunk.lessons} topics={chunk.topics ?? []} navigate={navigate} />

      {rabbitHoles.length > 0 && (
        <div className="mt-9">
          <div className="text-[15px] font-bold text-gold mb-1 flex items-center gap-2">
            <Rabbit size={16} color="var(--gold)" strokeWidth={1.75} />
            Rabbit Holes
          </div>
          <p className="text-[12px] text-muted m-0 mb-3">Optional deep-dives — explore when curious.</p>
          <div className="flex flex-col gap-2">
            {rabbitHoles.map(rh => (
              <div
                key={rh.id}
                className="flex items-center justify-between bg-card border border-border rounded-[10px] px-4 py-3 cursor-pointer transition-[border-color] duration-200 hover:border-gold"
                onClick={() => navigate(`/rabbit-hole/${rh.id}`)}
              >
                <span className="text-[14px] font-semibold text-text">{rh.title}</span>
                <ArrowRight size={15} color="var(--muted)" strokeWidth={1.75} />
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
