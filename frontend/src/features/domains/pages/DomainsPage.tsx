import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useDomainsDashboard } from '@/hooks/queries'
import { useAuth } from '@/shared/hooks/useAuth'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { DomainIcon } from '@/components/icons/DomainIcon'
import { Sparkles, RefreshCcw, Check, Lock, ChevronLeft } from 'lucide-react'
import {
  DOMAINS,
  ACTIVE_DOMAIN_IDS,
  SCHOOL_META,
  TRACK_GROUPS,
  schoolHasTrackGroups,
  trackGroupsForSchool,
  type Domain,
  type School,
  type TrackGroup,
} from '../data/domains'

// ── Types ─────────────────────────────────────────────────────────────────────

interface TopicData {
  progress: number
  diagnosticCompleted: boolean
  diagnosticCompletedAt: string | null
  totalChunks: number
  totalLessons: number
}

type NavState =
  | { level: 'schools' }
  | { level: 'track-groups'; school: School }
  | { level: 'domains'; school: School; trackGroupId?: string }

// ── Helpers ───────────────────────────────────────────────────────────────────

function diagnosticExpired(completedAt: string | null): boolean {
  if (!completedAt) return false
  return (Date.now() - new Date(completedAt).getTime()) / (1000 * 60 * 60 * 24) >= 30
}

// ── Sub-components ────────────────────────────────────────────────────────────

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

function Breadcrumb({ nav, onNavigate }: {
  nav: NavState
  onNavigate: (state: NavState) => void
}) {
  if (nav.level === 'schools') return null

  const school = SCHOOL_META[nav.school]

  return (
    <div className="flex items-center gap-2 mb-6 text-[13px] text-muted font-cinzel">
      <button
        onClick={() => onNavigate({ level: 'schools' })}
        className="flex items-center gap-1 hover:text-gold transition-colors"
      >
        <ChevronLeft size={14} />
        Schools
      </button>
      <span className="opacity-40">/</span>
      {nav.level === 'domains' && nav.trackGroupId ? (
        <>
          <button
            onClick={() => onNavigate({ level: 'track-groups', school: nav.school })}
            className="hover:text-gold transition-colors"
          >
            {school.name}
          </button>
          <span className="opacity-40">/</span>
          <span className="text-gold">
            {TRACK_GROUPS.find(tg => tg.id === nav.trackGroupId)?.name}
          </span>
        </>
      ) : (
        <span className="text-gold">{school.name}</span>
      )}
    </div>
  )
}

// ── Schools view ──────────────────────────────────────────────────────────────

function SchoolsView({ onSelect }: { onSelect: (school: School) => void }) {
  const schools = Object.entries(SCHOOL_META) as [School, typeof SCHOOL_META[School]][]
  const activeDomainsBySchool = (school: School) =>
    DOMAINS.filter(d => d.school === school && d.status === 'active').length

  return (
    <>
      <div className="text-center mb-10">
        <h1 className="font-cinzel text-[30px] font-bold text-gold m-0 mb-3">
          Choose Your School
        </h1>
        <p className="text-[15px] text-muted leading-[1.7] max-w-[520px] mx-auto">
          Each school represents a discipline cluster. Select one to explore its tracks and enrol in a pathway.
        </p>
      </div>

      <div className="grid gap-4" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))' }}>
        {schools.map(([id, meta]) => {
          const activeCount = activeDomainsBySchool(id)
          return (
            <button
              key={id}
              onClick={() => onSelect(id)}
              className="text-left bg-card border border-border rounded-[16px] p-6 flex items-start gap-5
                transition-all duration-200 hover:-translate-y-[2px] cursor-pointer"
              style={{
                borderTopWidth: 2,
                borderTopColor: `color-mix(in srgb, ${meta.color} 70%, transparent)`,
              }}
              onMouseEnter={e => {
                (e.currentTarget as HTMLButtonElement).style.borderColor = `color-mix(in srgb, ${meta.color} 45%, transparent)`
                ;(e.currentTarget as HTMLButtonElement).style.boxShadow = `0 8px 28px color-mix(in srgb, ${meta.color} 12%, transparent)`
              }}
              onMouseLeave={e => {
                (e.currentTarget as HTMLButtonElement).style.borderColor = 'var(--border)'
                ;(e.currentTarget as HTMLButtonElement).style.boxShadow = 'none'
                ;(e.currentTarget as HTMLButtonElement).style.borderTopColor = `color-mix(in srgb, ${meta.color} 70%, transparent)`
              }}
            >
              <div
                className="w-12 h-12 flex-shrink-0 rounded-xl flex items-center justify-center text-[22px]"
                style={{
                  background: `color-mix(in srgb, ${meta.color} 12%, transparent)`,
                  border: `1px solid color-mix(in srgb, ${meta.color} 25%, transparent)`,
                }}
              >
                {meta.glyph}
              </div>
              <div className="flex-1 min-w-0">
                <div className="flex items-start justify-between gap-2 mb-1.5">
                  <span className="font-cinzel text-[15px] font-semibold" style={{ color: meta.color }}>
                    {meta.name}
                  </span>
                  {activeCount > 0 && (
                    <span
                      className="flex-shrink-0 text-[10px] font-cinzel px-2 py-0.5 rounded-full"
                      style={{
                        color: meta.color,
                        background: `color-mix(in srgb, ${meta.color} 12%, transparent)`,
                        border: `1px solid color-mix(in srgb, ${meta.color} 25%, transparent)`,
                      }}
                    >
                      {activeCount} active
                    </span>
                  )}
                </div>
                <p className="text-[12px] text-muted leading-[1.6] m-0">{meta.description}</p>
              </div>
            </button>
          )
        })}
      </div>
    </>
  )
}

// ── Track groups view ─────────────────────────────────────────────────────────

function TrackGroupsView({ school, onSelect }: {
  school: School
  onSelect: (trackGroup: TrackGroup) => void
}) {
  const groups = trackGroupsForSchool(school)
  const meta = SCHOOL_META[school]

  return (
    <>
      <div className="text-center mb-10">
        <div
          className="inline-flex items-center gap-2 px-3 py-1 rounded-full text-[11px] font-cinzel mb-4"
          style={{
            color: meta.color,
            background: `color-mix(in srgb, ${meta.color} 10%, transparent)`,
            border: `1px solid color-mix(in srgb, ${meta.color} 25%, transparent)`,
          }}
        >
          <span>{meta.glyph}</span>
          <span>{meta.name}</span>
        </div>
        <h1 className="font-cinzel text-[28px] font-bold text-gold m-0 mb-3">
          Select a Track
        </h1>
        <p className="text-[15px] text-muted leading-[1.7] max-w-[480px] mx-auto">
          Each track covers a focused area of the discipline. Choose the track that matches your goals.
        </p>
      </div>

      <div className="grid gap-4" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))' }}>
        {groups.map(tg => {
          const domains = DOMAINS.filter(d => d.trackGroup === tg.id)
          const activeCount = domains.filter(d => d.status === 'active').length
          const totalCount = domains.length

          return (
            <button
              key={tg.id}
              onClick={() => onSelect(tg)}
              className="text-left bg-card border border-border rounded-[16px] p-6 flex items-start gap-5
                transition-all duration-200 hover:-translate-y-[2px] cursor-pointer"
              style={{
                borderTopWidth: 2,
                borderTopColor: `color-mix(in srgb, ${tg.color} 70%, transparent)`,
              }}
              onMouseEnter={e => {
                (e.currentTarget as HTMLButtonElement).style.borderColor = `color-mix(in srgb, ${tg.color} 45%, transparent)`
                ;(e.currentTarget as HTMLButtonElement).style.boxShadow = `0 8px 28px color-mix(in srgb, ${tg.color} 12%, transparent)`
              }}
              onMouseLeave={e => {
                (e.currentTarget as HTMLButtonElement).style.borderColor = 'var(--border)'
                ;(e.currentTarget as HTMLButtonElement).style.boxShadow = 'none'
                ;(e.currentTarget as HTMLButtonElement).style.borderTopColor = `color-mix(in srgb, ${tg.color} 70%, transparent)`
              }}
            >
              <div
                className="w-12 h-12 flex-shrink-0 rounded-xl flex items-center justify-center text-[22px]"
                style={{
                  background: `color-mix(in srgb, ${tg.color} 12%, transparent)`,
                  border: `1px solid color-mix(in srgb, ${tg.color} 25%, transparent)`,
                }}
              >
                {tg.glyph}
              </div>
              <div className="flex-1 min-w-0">
                <div className="flex items-start justify-between gap-2 mb-1.5">
                  <span className="font-cinzel text-[15px] font-semibold" style={{ color: tg.color }}>
                    {tg.name}
                  </span>
                  <span className="flex-shrink-0 text-[10px] font-cinzel text-muted">
                    {activeCount}/{totalCount} active
                  </span>
                </div>
                <p className="text-[12px] text-muted leading-[1.6] m-0">{tg.description}</p>
              </div>
            </button>
          )
        })}
      </div>
    </>
  )
}

// ── Domains view ──────────────────────────────────────────────────────────────

function DomainsView({
  school,
  trackGroupId,
  topicData,
  enrolledTopicIds,
  canBypassPaywall,
  onTopicClick,
  onDiagnosticClick,
  renderDiagnosticRow,
}: {
  school: School
  trackGroupId?: string
  topicData: Record<string, TopicData>
  enrolledTopicIds: Set<string>
  canBypassPaywall: boolean
  onTopicClick: (topic: Domain) => void
  onDiagnosticClick: (e: React.MouseEvent, domainId: string) => void
  renderDiagnosticRow: (topic: Domain) => React.ReactNode
}) {
  const hasActiveEnrollment = enrolledTopicIds.size > 0
  const meta = trackGroupId
    ? TRACK_GROUPS.find(tg => tg.id === trackGroupId)
    : SCHOOL_META[school]

  const visibleTopics = DOMAINS
    .filter(d => {
      if (d.school !== school) return false
      if (trackGroupId && d.trackGroup !== trackGroupId) return false
      return true
    })
    .sort((a, b) => {
      if (a.status === b.status) return 0
      return a.status === 'active' ? -1 : 1
    })

  return (
    <>
      {meta && (
        <div className="text-center mb-10">
          <div
            className="inline-flex items-center gap-2 px-3 py-1 rounded-full text-[11px] font-cinzel mb-4"
            style={{
              color: (meta as { color: string }).color,
              background: `color-mix(in srgb, ${(meta as { color: string }).color} 10%, transparent)`,
              border: `1px solid color-mix(in srgb, ${(meta as { color: string }).color} 25%, transparent)`,
            }}
          >
            <span>{(meta as { glyph: string }).glyph}</span>
            <span>{meta.name}</span>
          </div>
          <h1 className="font-cinzel text-[28px] font-bold text-gold m-0 mb-3">
            {hasActiveEnrollment && !canBypassPaywall ? 'Your Pathways' : 'Choose Your Path'}
          </h1>
          <p className="text-[15px] text-muted leading-[1.7] max-w-[520px] mx-auto">
            {hasActiveEnrollment && !canBypassPaywall
              ? 'Continue mastering your chosen discipline — or unlock more paths when you\'re ready.'
              : 'Enrol in a discipline and begin your journey.'}
          </p>
        </div>
      )}

      <div
        className="grid gap-4 mb-10"
        style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))' }}
      >
        {visibleTopics.map(topic => {
          const active = topic.status === 'active'
          const isEnrolled = enrolledTopicIds.has(topic.id)
          const isPaywalled = active && !isEnrolled && hasActiveEnrollment && !canBypassPaywall
          const progress = topicData[topic.id]?.progress ?? 0

          return (
            <div
              key={topic.id}
              className={cn(
                'bg-card border border-border rounded-[14px] px-5 py-6 pb-5 flex flex-col gap-2.5',
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
                if (active && !isPaywalled)
                  (e.currentTarget as HTMLDivElement).style.borderColor = `color-mix(in srgb, ${topic.accentStroke} 50%, transparent)`
              }}
              onMouseLeave={e => {
                if (active && !isPaywalled) {
                  const el = e.currentTarget as HTMLDivElement
                  el.style.borderColor = 'var(--border)'
                  el.style.borderTopColor = `color-mix(in srgb, ${topic.accentStroke} 60%, transparent)`
                }
              }}
              onClick={() => { if (!isPaywalled) onTopicClick(topic) }}
            >
              <div className="flex items-start justify-between mb-1">
                <div className="opacity-90">
                  <DomainIcon domainId={topic.id} size={active ? 34 : 30} />
                </div>
                <div className="flex flex-col items-end gap-1.5">
                  {isPaywalled ? (
                    <div className="w-[56px] h-[56px] flex items-center justify-center
                      rounded-full border border-[rgba(201,162,39,0.2)] bg-[rgba(201,162,39,0.05)]">
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

              <div className="font-cinzel text-[18px] font-bold text-text">{topic.name}</div>
              <div className="text-[13px] text-muted leading-[1.6] flex-1">{topic.tagline}</div>

              {active && isEnrolled && renderDiagnosticRow(topic)}

              <div className="flex items-center justify-between pt-2.5 border-t border-border mt-auto gap-2">
                <span className="text-[11px] text-muted font-cinzel leading-[1.4]">
                  {active && topicData[topic.id]
                    ? <>{topicData[topic.id].totalChunks} modules · {topicData[topic.id].totalLessons} lessons</>
                    : <>{topic.modules} modules</>}
                </span>
                {active && (
                  <span className={cn(
                    'text-[13px] font-semibold flex-shrink-0 flex items-center gap-1',
                    isPaywalled ? 'text-gold opacity-70' : isEnrolled ? 'text-teal' : 'text-gold',
                  )}>
                    {isPaywalled ? <><Lock size={12} strokeWidth={2} /> Unlock</> : isEnrolled ? 'Continue →' : 'Enrol →'}
                  </span>
                )}
              </div>
            </div>
          )
        })}
      </div>

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
    </>
  )
}

// ── Page ──────────────────────────────────────────────────────────────────────

export default function DomainsPage() {
  const navigate = useNavigate()
  const { user } = useAuth()
  const [nav, setNav] = useState<NavState>({ level: 'schools' })
  const rawTopicData = useDomainsDashboard(ACTIVE_DOMAIN_IDS)

  const topicData: Record<string, TopicData> = Object.fromEntries(
    Object.entries(rawTopicData)
      .filter(([, d]) => d != null)
      .map(([id, d]) => [id, {
        progress: Math.round(d!.overallProgress * 100),
        diagnosticCompleted: d!.diagnosticCompleted,
        diagnosticCompletedAt: d!.diagnosticCompletedAt ?? null,
        totalChunks: d!.chunkHealth.length,
        totalLessons: d!.chunkHealth.reduce((sum, ch) => sum + ch.totalLessons, 0),
      }])
  )

  const enrolledTopicIds = new Set(
    ACTIVE_DOMAIN_IDS.filter(id => {
      const d = topicData[id]
      return d != null && (d.progress > 0 || d.diagnosticCompleted)
    })
  )
  const canBypassPaywall = user?.role === 'ADMIN' || user?.bypassPaywall === true

  function handleTopicClick(topic: Domain) {
    if (topic.status !== 'active') return
    const data = topicData[topic.id]
    const needsOnboarding = !data || !data.diagnosticCompleted || diagnosticExpired(data.diagnosticCompletedAt)
    navigate(needsOnboarding ? `/domain/${topic.id}/onboarding` : `/domain/${topic.id}`)
  }

  function handleDiagnosticClick(e: React.MouseEvent, domainId: string) {
    e.stopPropagation()
    navigate(`/domain/${domainId}/diagnostic`)
  }

  function renderDiagnosticRow(topic: Domain) {
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

  function handleSchoolSelect(school: School) {
    if (schoolHasTrackGroups(school)) {
      setNav({ level: 'track-groups', school })
    } else {
      setNav({ level: 'domains', school })
    }
  }

  function handleTrackGroupSelect(tg: TrackGroup) {
    setNav({ level: 'domains', school: tg.school, trackGroupId: tg.id })
  }

  return (
    <div className="max-w-[960px] mx-auto px-5 py-8 pb-[72px] overflow-y-auto max-[600px]:px-3 max-[600px]:py-5">
      <Breadcrumb nav={nav} onNavigate={setNav} />

      {nav.level === 'schools' && (
        <SchoolsView onSelect={handleSchoolSelect} />
      )}

      {nav.level === 'track-groups' && (
        <TrackGroupsView school={nav.school} onSelect={handleTrackGroupSelect} />
      )}

      {nav.level === 'domains' && (
        <DomainsView
          school={nav.school}
          trackGroupId={nav.trackGroupId}
          topicData={topicData}
          enrolledTopicIds={enrolledTopicIds}
          canBypassPaywall={canBypassPaywall}
          onTopicClick={handleTopicClick}
          onDiagnosticClick={handleDiagnosticClick}
          renderDiagnosticRow={renderDiagnosticRow}
        />
      )}
    </div>
  )
}
