import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTopicsDashboard } from '@/hooks/queries'
import { useAuth } from '@/shared/hooks/useAuth'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { TopicIcon } from '@/components/icons/TopicIcon'
import { Sparkles, RefreshCcw, Check, Lock } from 'lucide-react'

type Genre = 'all' | 'tech' | 'science' | 'history'

interface Topic {
  id: string
  name: string
  glyph: string
  tagline: string
  status: 'active' | 'coming_soon'
  chunks: number
  accentStroke: string
  genre: Genre
}

interface TopicData {
  progress: number
  diagnosticCompleted: boolean
  diagnosticCompletedAt: string | null
  totalChunks: number
  totalLessons: number
}

const TOPICS: Topic[] = [
  { id: 'java',       name: 'Java',             glyph: '☕', tagline: 'From zero to job-ready. The complete apprentice-to-archmage pathway.',                               status: 'active',      chunks: 14, accentStroke: 'var(--teal)',   genre: 'tech'    },
  { id: 'psychology', name: 'Psychology',       glyph: '🧠', tagline: 'A self-paced route through foundations, human behaviour, advanced understanding, and academic thinking.', status: 'active', chunks: 11, accentStroke: 'var(--purple)', genre: 'science' },
  { id: 'tailwind',   name: 'Tailwind CSS',     glyph: '🎨', tagline: 'Compose beautiful interfaces with utility classes — no more naming paralysis.',                      status: 'coming_soon', chunks: 4,  accentStroke: 'var(--purple)', genre: 'tech'    },
  { id: 'react',      name: 'React',            glyph: '⚛️', tagline: 'Component-driven UIs. Hooks, state, and the modern frontend — all the way to deployment.',          status: 'coming_soon', chunks: 4,  accentStroke: 'var(--teal)',   genre: 'tech'    },
  { id: 'sql',        name: 'SQL',              glyph: '🗃️', tagline: 'The language of data. SELECT to window functions — the queries every backend dev writes daily.',    status: 'coming_soon', chunks: 8,  accentStroke: 'var(--teal)',   genre: 'tech'    },
  { id: 'sciences',   name: 'Natural Sciences', glyph: '🔬', tagline: 'Scientific method, physics, biology — the laws of the world.',                                      status: 'coming_soon', chunks: 3,  accentStroke: 'var(--teal)',   genre: 'science' },
  { id: 'genealogy',  name: 'Genealogy',        glyph: '🌳', tagline: 'Records, lineages, and DNA — the methods of family history.',                                       status: 'coming_soon', chunks: 3,  accentStroke: 'var(--gold)',   genre: 'history' },
  { id: 'html',       name: 'HTML',             glyph: '📄', tagline: 'The structure of the web. Learn to author the skeleton of every page.',                             status: 'coming_soon', chunks: 8,  accentStroke: 'var(--orange)', genre: 'tech'    },
  { id: 'css',        name: 'CSS',              glyph: '🖌️', tagline: 'Craft beautiful, responsive interfaces from the ground up.',                                        status: 'coming_soon', chunks: 10, accentStroke: 'var(--purple)', genre: 'tech'    },
  { id: 'javascript', name: 'JavaScript',       glyph: '⚡', tagline: 'Bring the web to life. Logic, events, async, and the DOM.',                                         status: 'coming_soon', chunks: 14, accentStroke: 'var(--gold)',   genre: 'tech'    },
  { id: 'python',     name: 'Python',           glyph: '🐍', tagline: 'Versatile, readable, powerful. Data, scripts, and automation.',                                     status: 'coming_soon', chunks: 12, accentStroke: 'var(--teal)',   genre: 'tech'    },
  { id: 'typescript', name: 'TypeScript',       glyph: '🔷', tagline: 'JavaScript with discipline. Types, interfaces, and confidence at scale.',                           status: 'coming_soon', chunks: 10, accentStroke: 'var(--gold)',   genre: 'tech'    },
]

const GENRES: { id: Genre; label: string; glyph: string }[] = [
  { id: 'all',     label: 'All',        glyph: '✦'  },
  { id: 'tech',    label: 'Technology', glyph: '💻' },
  { id: 'science', label: 'Science',    glyph: '🔬' },
  { id: 'history', label: 'History',    glyph: '📜' },
]

const ACTIVE_TOPICS = TOPICS.filter(t => t.status === 'active').map(t => t.id)

/** Returns true if the diagnostic was completed more than 30 days ago. */
function diagnosticExpired(completedAt: string | null): boolean {
  if (!completedAt) return false
  const msPerDay = 1000 * 60 * 60 * 24
  return (Date.now() - new Date(completedAt).getTime()) / msPerDay >= 30
}

function ProgressRing({ pct, active, stroke }: { pct: number; active: boolean; stroke: string }) {
  const r = 22
  const circ = 2 * Math.PI * r
  const dash = (pct / 100) * circ
  return (
    <svg width="56" height="56" viewBox="0 0 56 56" className="block overflow-visible">
      <circle cx="28" cy="28" r={r} fill="none" stroke="var(--border)" strokeWidth="3.5" />
      <circle
        cx="28" cy="28" r={r} fill="none"
        stroke={stroke} strokeWidth="3.5" strokeLinecap="round"
        strokeDasharray={`${dash} ${circ}`} strokeDashoffset="0"
        transform="rotate(-90 28 28)"
        style={{ opacity: active ? 1 : 0.3, transition: 'stroke-dasharray 0.6s ease' }}
      />
      <text x="28" y="28" dominantBaseline="central" textAnchor="middle"
        fontSize="9" fontFamily="'Cinzel', serif" fill="var(--muted)" fontWeight="600" letterSpacing="0.02em">
        {Math.round(pct)}%
      </text>
    </svg>
  )
}

export default function TopicsPage() {
  const navigate = useNavigate()
  const { user } = useAuth()
  const [activeGenre, setActiveGenre] = useState<Genre>('all')
  const rawTopicData = useTopicsDashboard(ACTIVE_TOPICS)

  const topicData: Record<string, TopicData> = Object.fromEntries(
    Object.entries(rawTopicData)
      .filter(([, d]) => d != null)
      .map(([id, d]) => [id, {
        progress: Math.round(d!.overallProgress * 100),
        diagnosticCompleted: d!.diagnosticCompleted,
        diagnosticCompletedAt: d!.diagnosticCompletedAt ?? null,
        totalChunks: d!.chunkHealth.length,
        totalLessons: d!.chunkHealth.reduce((sum, ch) => sum + ch.totalSubChunks, 0),
      }])
  )

  // ── Enrollment & paywall logic ────────────────────────────────────────────
  //
  // A topic is "enrolled" once the user has any engagement: started a
  // diagnostic OR has learning progress. We derive this from the dashboard
  // data so no extra API is needed.
  //
  // Users may freely enrol in one topic. Additional topics are behind a
  // paywall (coming soon). Admins and admin-designated users bypass it.
  const enrolledTopicIds = new Set(
    ACTIVE_TOPICS.filter(id => {
      const d = topicData[id]
      return d != null && (d.progress > 0 || d.diagnosticCompleted)
    })
  )
  const hasActiveEnrollment = enrolledTopicIds.size > 0
  const canBypassPaywall = user?.role === 'ADMIN' || user?.bypassPaywall === true

  // Filter by genre, then sort: active first, coming_soon after
  const visibleTopics = TOPICS
    .filter(t => activeGenre === 'all' || t.genre === activeGenre)
    .sort((a, b) => {
      if (a.status === b.status) return 0
      return a.status === 'active' ? -1 : 1
    })

  function handleTopicClick(topic: Topic) {
    if (topic.status !== 'active') return
    const data = topicData[topic.id]
    const needsOnboarding = !data || !data.diagnosticCompleted || diagnosticExpired(data.diagnosticCompletedAt)
    navigate(needsOnboarding ? `/topic/${topic.id}/onboarding` : `/topic/${topic.id}`)
  }

  function handleDiagnosticClick(e: React.MouseEvent, topicId: string) {
    e.stopPropagation()
    navigate(`/topic/${topicId}/diagnostic`)
  }

  function renderDiagnosticRow(topic: Topic) {
    const data = topicData[topic.id]
    if (!data) return null

    const { diagnosticCompleted, diagnosticCompletedAt } = data
    const expired = diagnosticExpired(diagnosticCompletedAt)

    if (!diagnosticCompleted) {
      return (
        <button
          className="w-full mt-1 flex items-center justify-center gap-1.5 px-3 py-1.5 rounded-[7px]
            bg-[rgba(139,92,246,0.08)] border border-[rgba(139,92,246,0.25)] text-purple-light
            text-[11px] font-semibold font-cinzel tracking-wide
            transition-[background,border-color] duration-150
            hover:bg-[rgba(139,92,246,0.15)] hover:border-purple"
          onClick={e => handleDiagnosticClick(e, topic.id)}
        >
          <Sparkles size={12} strokeWidth={1.75} />
          Take Diagnostic
        </button>
      )
    }

    if (expired) {
      return (
        <button
          className="w-full mt-1 flex items-center justify-center gap-1.5 px-3 py-1.5 rounded-[7px]
            bg-[rgba(201,162,39,0.08)] border border-[rgba(201,162,39,0.25)] text-gold
            text-[11px] font-semibold font-cinzel tracking-wide
            transition-[background,border-color] duration-150
            hover:bg-[rgba(201,162,39,0.15)] hover:border-gold"
          onClick={e => handleDiagnosticClick(e, topic.id)}
        >
          <RefreshCcw size={12} strokeWidth={1.75} />
          Retake Diagnostic
        </button>
      )
    }

    const completedDate = diagnosticCompletedAt
      ? new Date(diagnosticCompletedAt).toLocaleDateString(undefined, { month: 'short', day: 'numeric' })
      : null
    const daysLeft = diagnosticCompletedAt
      ? Math.ceil(30 - (Date.now() - new Date(diagnosticCompletedAt).getTime()) / (1000 * 60 * 60 * 24))
      : null

    return (
      <div className="mt-1 flex items-center gap-1.5 px-3 py-1.5 rounded-[7px]
        bg-[rgba(0,200,83,0.06)] border border-[rgba(0,200,83,0.2)] text-teal text-[11px] font-cinzel">
        <Check size={12} strokeWidth={2.5} />
        <span>Diagnostic done{completedDate ? ` · ${completedDate}` : ''}</span>
        {daysLeft !== null && (
          <span className="ml-auto text-muted text-[10px]">retake in {daysLeft}d</span>
        )}
      </div>
    )
  }

  return (
    <div className="max-w-[960px] mx-auto px-5 py-8 pb-[72px] overflow-y-auto max-[600px]:px-3 max-[600px]:py-5">

      <div className="text-center mb-8">
        <h1 className="font-cinzel text-[32px] font-bold text-gold m-0 mb-3 max-[600px]:text-[24px]">
          Choose Your Path
        </h1>
        <p className="text-[16px] text-muted leading-[1.7] max-w-[560px] mx-auto">
          {hasActiveEnrollment && !canBypassPaywall
            ? 'Continue mastering your chosen discipline — or unlock more paths when you\'re ready.'
            : 'Enrol in a discipline and begin your journey. Master it deeply before expanding your path.'}
        </p>
      </div>

      {/* Genre filter pills */}
      <div className="flex items-center gap-2 mb-8 flex-wrap justify-center">
        {GENRES.map(g => {
          const selected = activeGenre === g.id
          return (
            <button
              key={g.id}
              onClick={() => setActiveGenre(g.id)}
              className={cn(
                'flex items-center gap-1.5 px-4 py-1.5 rounded-full text-[12px] font-semibold font-cinzel tracking-wide transition-all duration-150 border',
                selected
                  ? 'bg-[rgba(201,162,39,0.15)] border-gold text-gold'
                  : 'bg-card border-border text-muted hover:border-[rgba(201,162,39,0.4)] hover:text-text'
              )}
            >
              <span>{g.glyph}</span>
              <span>{g.label}</span>
            </button>
          )
        })}
      </div>

      <div
        className="grid gap-4 mb-12 max-[600px]:gap-2.5 max-[480px]:grid-cols-1"
        style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))' }}
      >
        {visibleTopics.map(topic => {
          const active    = topic.status === 'active'
          const isEnrolled  = enrolledTopicIds.has(topic.id)
          // Lock active-but-unenrolled topics when the user already has an enrolment
          // and cannot bypass the paywall. Coming-soon topics stay in their own state.
          const isPaywalled = active && !isEnrolled && hasActiveEnrollment && !canBypassPaywall
          const progress  = topicData[topic.id]?.progress ?? 0

          return (
            <div
              key={topic.id}
              className={cn(
                'bg-card border border-border rounded-[14px] px-5 py-6 pb-5 flex flex-col gap-2.5',
                'max-[480px]:px-3.5 max-[480px]:py-4',
                'relative overflow-hidden transition-[border-color,transform,box-shadow,opacity] duration-200',
                active && !isPaywalled
                  ? 'cursor-pointer hover:-translate-y-[3px] hover:shadow-[0_8px_32px_rgba(0,0,0,0.3)]'
                  : isPaywalled
                  ? 'cursor-pointer opacity-60'
                  : 'cursor-default opacity-55 saturate-50',
              )}
              style={active && !isPaywalled ? {
                borderTopColor: `color-mix(in srgb, ${topic.accentStroke} 60%, transparent)`,
                borderTopWidth: 2,
              } : undefined}
              onMouseEnter={e => {
                if (active && !isPaywalled) (e.currentTarget as HTMLDivElement).style.borderColor = `color-mix(in srgb, ${topic.accentStroke} 50%, transparent)`
              }}
              onMouseLeave={e => {
                if (active && !isPaywalled) {
                  const el = e.currentTarget as HTMLDivElement
                  el.style.borderColor = 'var(--border)'
                  el.style.borderTopColor = `color-mix(in srgb, ${topic.accentStroke} 60%, transparent)`
                }
              }}
              onClick={() => { if (!isPaywalled) handleTopicClick(topic) }}
            >
              <div className="flex items-start justify-between mb-1">
                <div className="opacity-90">
                  <TopicIcon topicId={topic.id} size={active ? 34 : 30} />
                </div>
                <div className="flex flex-col items-end gap-1.5">
                  {isPaywalled ? (
                    // Lock icon replacing progress ring for paywalled topics
                    <div className="w-[56px] h-[56px] flex items-center justify-center
                      rounded-full border border-[rgba(201,162,39,0.2)]
                      bg-[rgba(201,162,39,0.05)]">
                      <Lock size={20} className="text-gold opacity-60" strokeWidth={1.5} />
                    </div>
                  ) : (
                    <ProgressRing pct={progress} active={active} stroke={topic.accentStroke} />
                  )}
                  <Badge variant={!active ? 'soon' : isPaywalled ? 'locked' : 'active'}>
                    {!active ? 'Coming Soon' : isPaywalled ? '🔒 Premium' : 'Active'}
                  </Badge>
                </div>
              </div>

              <div className="font-cinzel text-[20px] font-bold text-text max-[600px]:text-[16px]">
                {topic.name}
              </div>
              <div className="text-[13px] text-muted leading-[1.6] flex-1">{topic.tagline}</div>

              {/* Diagnostic row — only shown for enrolled topics */}
              {active && isEnrolled && renderDiagnosticRow(topic)}

              <div className="flex items-center justify-between pt-2.5 border-t border-border mt-auto gap-2">
                <span className="text-[11px] text-muted font-cinzel leading-[1.4]">
                  {active && topicData[topic.id]
                    ? <>{topicData[topic.id].totalChunks} modules · {topicData[topic.id].totalLessons} lessons</>
                    : <>{topic.chunks} modules</>}
                </span>

                {/* Action label — three states */}
                {active && (
                  <span className={cn(
                    'text-[13px] font-semibold flex-shrink-0 flex items-center gap-1',
                    isPaywalled ? 'text-gold opacity-70' :
                    isEnrolled  ? 'text-teal' :
                    'text-gold'
                  )}>
                    {isPaywalled ? (
                      <><Lock size={12} strokeWidth={2} /> Unlock</>
                    ) : isEnrolled ? (
                      'Continue →'
                    ) : (
                      'Enrol →'
                    )}
                  </span>
                )}
              </div>
            </div>
          )
        })}
      </div>

      {/* Contextual bottom note */}
      <div className="flex items-start gap-4 bg-card border border-border border-l-[3px] border-l-gold rounded-[10px] px-6 py-5">
        <span className="text-[20px] text-gold flex-shrink-0 mt-0.5">✦</span>
        {hasActiveEnrollment && !canBypassPaywall ? (
          <p className="text-[14px] text-muted leading-[1.7] m-0">
            You have an active enrolment. Complete modules and build deep mastery in your chosen
            discipline — unlock additional paths when you're ready to expand.
          </p>
        ) : (
          <p className="text-[14px] text-muted leading-[1.7] m-0">
            A polymath builds mastery one discipline at a time. Choose your first path wisely —
            depth before breadth is the mark of a true scholar.
          </p>
        )}
      </div>
    </div>
  )
}
