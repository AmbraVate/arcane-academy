import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useDomainsDashboard } from '@/hooks/queries'
import { useAuth } from '@/shared/hooks/useAuth'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { DomainIcon } from '@/components/icons/DomainIcon'
import { Lock, ChevronLeft, LogIn, AlertTriangle } from 'lucide-react'
import CompletionRing from '@/components/ui/CompletionRing'
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
  totalChunks: number
  totalLessons: number
}

type NavState =
  | { level: 'schools' }
  | { level: 'track-groups'; school: School }
  | { level: 'domains'; school: School; trackGroupId?: string }



// ── Paywall modal ─────────────────────────────────────────────────────────────

function PaywallModal({ onClose, onUpgrade }: { onClose: () => void; onUpgrade: () => void }) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4"
      style={{ background: 'rgba(0,0,0,0.7)', backdropFilter: 'blur(4px)' }}>
      <div className="bg-card border border-border rounded-[16px] p-7 max-w-[420px] w-full shadow-2xl">
        <div className="flex items-center gap-3 mb-4">
          <div className="w-10 h-10 rounded-xl bg-[rgba(201,162,39,0.12)] border border-[rgba(201,162,39,0.3)]
            flex items-center justify-center flex-shrink-0">
            <AlertTriangle size={20} color="var(--gold)" strokeWidth={1.75} />
          </div>
          <h2 className="font-cinzel text-[18px] font-bold text-gold m-0">One Pathway at a Time</h2>
        </div>
        <p className="text-[14px] text-muted leading-[1.7] mb-2">
          You already have an active pathway. The free plan lets you master one discipline deeply before expanding.
        </p>
        <p className="text-[14px] text-muted leading-[1.7] mb-6">
          Unlock unlimited pathways with a subscription — or complete your current path first.
        </p>
        <div className="flex gap-3">
          <button
            onClick={onUpgrade}
            className="flex-1 py-2.5 px-4 rounded-[10px] font-cinzel text-[13px] font-semibold text-bg
              cursor-pointer transition-opacity duration-150 hover:opacity-90"
            style={{ background: 'linear-gradient(135deg, var(--gold), #e8b84b)' }}
          >
            Unlock All Pathways
          </button>
          <button
            onClick={onClose}
            className="flex-1 py-2.5 px-4 rounded-[10px] font-cinzel text-[13px] font-semibold
              bg-surface border border-border text-muted cursor-pointer hover:border-border hover:text-text transition-colors"
          >
            Stay on Current Path
          </button>
        </div>
      </div>
    </div>
  )
}

// ── Breadcrumb ────────────────────────────────────────────────────────────────

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

function SchoolsView({ onSelect, isPublic }: { onSelect: (school: School) => void; isPublic: boolean }) {
  const schools = Object.entries(SCHOOL_META) as [School, typeof SCHOOL_META[School]][]
  const activeDomainsBySchool = (school: School) =>
    DOMAINS.filter(d => d.school === school && d.status === 'active').length

  return (
    <>
      <div className="text-center mb-10">
        <h1 className="font-cinzel text-[30px] font-bold text-gold m-0 mb-3">
          Arcane Schools
        </h1>
        <p className="text-[15px] text-muted leading-[1.7] max-w-[520px] mx-auto">
          Each school represents a discipline cluster. Select one to explore its pathways.
        </p>
        {isPublic && (
          <p className="text-[13px] text-purple-light mt-2 opacity-80">
            Browse freely — sign in when you're ready to enrol.
          </p>
        )}
      </div>

      <div data-tutorial-id="schools-grid" className="grid gap-4" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))' }}>
        {schools.map(([id, meta]) => {
          const activeCount = activeDomainsBySchool(id)
          return (
            <button
              key={id}
              data-tutorial-id={id === 'engineering-systems' ? 'first-domain-card' : undefined}
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

// ── Track groups / Pathways view ──────────────────────────────────────────────

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
          Pathways
        </h1>
        <p className="text-[15px] text-muted leading-[1.7] max-w-[480px] mx-auto">
          Each pathway covers a focused discipline. Choose the one that matches your goals.
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

// ── Domains / Enrol view ──────────────────────────────────────────────────────

function DomainsView({
  school,
  trackGroupId,
  topicData,
  enrolledTopicIds,
  canBypassPaywall,
  isPublic,
  onTopicClick,
}: {
  school: School
  trackGroupId?: string
  topicData: Record<string, TopicData>
  enrolledTopicIds: Set<string>
  canBypassPaywall: boolean
  isPublic: boolean
  onTopicClick: (topic: Domain) => void
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
            {isPublic ? 'Choose Your Path' : hasActiveEnrollment && !canBypassPaywall ? 'Your Pathways' : 'Choose Your Path'}
          </h1>
          <p className="text-[15px] text-muted leading-[1.7] max-w-[520px] mx-auto">
            {isPublic
              ? 'Enrol in a discipline and begin your journey. Sign in to get started.'
              : hasActiveEnrollment && !canBypassPaywall
              ? 'Continue mastering your chosen discipline — or unlock more paths when you\'re ready.'
              : 'Enrol in a discipline and begin your journey.'}
          </p>
        </div>
      )}

      <div
        className="grid gap-4 mb-10"
        style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))' }}
      >
        {visibleTopics.map((topic, idx) => {
          const active = topic.status === 'active'
          const isEnrolled = enrolledTopicIds.has(topic.id)
          const isPaywalled = active && !isEnrolled && hasActiveEnrollment && !canBypassPaywall && !isPublic
          const progress = topicData[topic.id]?.progress ?? 0

          return (
            <div
              key={topic.id}
              data-tutorial-id={idx === 0 ? 'first-domain-card' : undefined}
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
                  ) : isPublic && active ? (
                    <div className="w-[56px] h-[56px] flex items-center justify-center
                      rounded-full border border-border bg-surface">
                      <LogIn size={18} color="var(--muted)" strokeWidth={1.5} />
                    </div>
                  ) : (
                    <CompletionRing pct={progress} color={topic.accentStroke} size={56} strokeWidth={3.5} fillOpacity={active ? 1 : 0.3} />
                  )}
                  <Badge variant={!active ? 'soon' : isPaywalled ? 'locked' : 'active'}>
                    {!active ? 'Coming Soon' : isPaywalled ? '🔒 Premium' : isPublic ? 'Enrol' : 'Active'}
                  </Badge>
                </div>
              </div>

              <div className="font-cinzel text-[18px] font-bold text-text">{topic.name}</div>
              <div className="text-[13px] text-muted leading-[1.6] flex-1">{topic.tagline}</div>

              <div className="flex items-center justify-between pt-2.5 border-t border-border mt-auto gap-2">
                <span className="text-[11px] text-muted font-cinzel leading-[1.4]">
                  {active && !isPublic && topicData[topic.id]
                    ? <>{topicData[topic.id].totalChunks} modules · {topicData[topic.id].totalLessons} lessons</>
                    : <>{topic.modules > 0 ? `${topic.modules} modules` : 'Pathway'}</>}
                </span>
                {active && (
                  <span className={cn(
                    'text-[13px] font-semibold flex-shrink-0 flex items-center gap-1',
                    isPaywalled ? 'text-gold opacity-70' : isPublic ? 'text-purple-light' : isEnrolled ? 'text-teal' : 'text-gold',
                  )}>
                    {isPaywalled ? <><Lock size={12} strokeWidth={2} /> Unlock</> :
                     isPublic ? <><LogIn size={12} strokeWidth={2} /> Sign in</> :
                     isEnrolled ? 'Continue →' : 'Enrol →'}
                  </span>
                )}
              </div>
            </div>
          )
        })}
      </div>

      {!isPublic && (
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
      )}
    </>
  )
}

// ── Page ──────────────────────────────────────────────────────────────────────

export default function DomainsPage() {
  const navigate = useNavigate()
  const { user } = useAuth()
  const isPublic = !user

  const [nav, setNav] = useState<NavState>({ level: 'schools' })
  const [showPaywall, setShowPaywall] = useState(false)

  // Only fetch dashboard data when authenticated
  const rawTopicData = useDomainsDashboard(ACTIVE_DOMAIN_IDS, !isPublic)

  const topicData: Record<string, TopicData> = isPublic
    ? {}
    : Object.fromEntries(
        Object.entries(rawTopicData)
          .filter(([, d]) => d != null)
          .map(([id, d]) => [id, {
            progress: Math.round(d!.overallProgress * 100),
            totalChunks: d!.moduleHealth.length,
            totalLessons: d!.moduleHealth.reduce((sum, ch) => sum + ch.totalLessons, 0),
          }])
      )

  // A domain is "enrolled" once the learner has started any module
  const enrolledTopicIds = new Set(
    isPublic
      ? []
      : ACTIVE_DOMAIN_IDS.filter(id => (topicData[id]?.progress ?? 0) > 0)
  )
  const canBypassPaywall = user?.role === 'ADMIN' || user?.bypassPaywall === true

  function handleTopicClick(topic: Domain) {
    if (topic.status !== 'active') return

    // Authenticated: enforce paywall for second+ enrolment
    if (!isPublic) {
      const isEnrolled = enrolledTopicIds.has(topic.id)
      const hasActiveEnrollment = enrolledTopicIds.size > 0
      if (!isEnrolled && hasActiveEnrollment && !canBypassPaywall) {
        setShowPaywall(true)
        return
      }
    }

    navigate(`/domain/${topic.id}`)
  }

  function handleSchoolSelect(school: School) {
    if (schoolHasTrackGroups(school)) {
      setNav({ level: 'track-groups', school })
    } else {
      setNav({ level: 'domains', school })
    }
  }

  function handleTrackGroupSelect(tg: TrackGroup) {
    const activeDomains = DOMAINS.filter(d => d.trackGroup === tg.id && d.status === 'active')
    if (activeDomains.length === 1) {
      handleTopicClick(activeDomains[0])
    } else {
      setNav({ level: 'domains', school: tg.school, trackGroupId: tg.id })
    }
  }

  return (
    <>
      {showPaywall && (
        <PaywallModal
          onClose={() => setShowPaywall(false)}
          onUpgrade={() => { setShowPaywall(false); navigate('/settings') }}
        />
      )}

      {/* Public header bar when not logged in */}
      {isPublic && (
        <div className="flex items-center justify-between px-6 py-3 border-b border-border bg-surface">
          <span className="font-cinzel text-[15px] text-gold tracking-[2px]">✦ Arcane Academy</span>
          <div className="flex items-center gap-2">
            <button onClick={() => navigate('/login')}
              className="btn btn-ghost text-[13px] px-3 py-1.5 flex items-center gap-1.5">
              <LogIn size={14} strokeWidth={1.75} /> Sign In
            </button>
            <button onClick={() => navigate('/register')}
              className="btn px-3 py-1.5 text-[13px] font-cinzel font-semibold rounded-[8px]"
              style={{ background: 'var(--purple)', color: '#fff', border: '1px solid var(--purple)' }}>
              Join Free
            </button>
          </div>
        </div>
      )}

      <div className="max-w-[960px] mx-auto px-5 py-8 pb-[72px] overflow-y-auto max-[600px]:px-3 max-[600px]:py-5">
        <Breadcrumb nav={nav} onNavigate={setNav} />

        {nav.level === 'schools' && (
          <SchoolsView onSelect={handleSchoolSelect} isPublic={isPublic} />
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
            isPublic={isPublic}
            onTopicClick={handleTopicClick}
          />
        )}
      </div>
    </>
  )
}
