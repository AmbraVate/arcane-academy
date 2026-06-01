import { useParams, useNavigate } from 'react-router-dom'
import { useDashboard } from '@/hooks/queries'
import { useAuth } from '@/shared/hooks/useAuth'
import type { ModuleHealthDto } from '@/shared/types'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { DomainIcon } from '@/components/icons/DomainIcon'
import { TierIcon } from '@/components/icons/TierIcon'
import { RefreshCcw, Check, MapPin, Lock, BookOpen, Sparkles } from 'lucide-react'

type TopicMeta = {
  name: string
  glyph: string
  tagline: string
  accent: string
  studyOrder?: { label: string; detail: string; tiers: string[] }[]
}

const TOPIC_META: Record<string, TopicMeta> = {
  java: {
    name: 'Software Engineering',
    glyph: '⚙️',
    tagline: 'Build reliable systems — computational thinking, design, and architecture, taught through Java.',
    accent: 'var(--teal)',
  },
  tailwind: {
    name: 'Tailwind CSS',
    glyph: '🎨',
    tagline: 'Compose beautiful interfaces with utility classes — no more naming paralysis.',
    accent: 'var(--purple)',
  },
  react: {
    name: 'React',
    glyph: '⚛️',
    tagline: 'Component-driven UIs. Hooks, state, and the modern frontend — all the way to deployment.',
    accent: 'var(--teal)',
  },
  sql: {
    name: 'SQL',
    glyph: '🗃️',
    tagline: 'The language of data. Read, filter, summarise — every backend dev writes it daily.',
    accent: 'var(--teal)',
  },
  psychology: {
    name: 'Psychology',
    glyph: '🧠',
    tagline: 'From foundations to frontier — the complete undergraduate-to-graduate psychology pathway.',
    accent: 'var(--purple)',
  },
  genealogy: {
    name: 'Genealogy',
    glyph: '🌳',
    tagline: 'From vital records to professional proof — become a skilled genealogical researcher.',
    accent: 'var(--gold)',
  },
  sciences: {
    name: 'Natural Sciences',
    glyph: '🔬',
    tagline: 'From scientific method to frontier research — physics, chemistry, biology, and earth science.',
    accent: 'var(--teal)',
  },
}
const MEM_COLORS: Record<string, string> = {
  GREEN: 'bg-teal', YELLOW: 'bg-orange', RED: 'bg-red',
}

const TIER_ORDER = ['APPRENTICE', 'JUNIOR', 'SENIOR', 'LEAD']
const TIER_LABELS: Record<string, string> = {
  APPRENTICE: 'Apprentice',
  JUNIOR:     'Junior',
  SENIOR:     'Senior',
  LEAD:       'Lead',
  // Legacy fallbacks during migration
  FOUNDATION:   'Foundation',
  ADVANCED:     'Advanced',
  PRACTITIONER: 'Practitioner',
  EXPERT:       'Expert',
  CAPSTONE:     'Capstone',
}
const TIER_DESC: Record<string, string> = {
  APPRENTICE: 'Foundations — core concepts, vocabulary, and the essential knowledge every learner needs.',
  JUNIOR:     'Applied knowledge — practical skills and techniques used in real-world contexts.',
  SENIOR:     'Advanced depth — specialist topics, critical evaluation, and complex synthesis.',
  LEAD:       'Mastery — professional practice, critical perspectives, and leadership in the field.',
  // Legacy fallbacks
  FOUNDATION:   'Core concepts and vocabulary — the solid base every practitioner needs.',
  ADVANCED:     'Deeper theory and analysis — building fluency beyond the fundamentals.',
  PRACTITIONER: 'Applied skills in real-world contexts — bringing knowledge into practice.',
  EXPERT:       'Specialist depth, critical evaluation, and advanced synthesis.',
  CAPSTONE:     'Synthesis projects that integrate everything you have learned.',
}

function ChunkCard({ ch, onClick, accent = 'var(--teal)' }: { ch: ModuleHealthDto; onClick: () => void; accent?: string }) {
  const locked = ch.status === 'LOCKED'
  const done   = ch.status === 'COMPLETE'
  const pct    = Math.round(ch.memoryStrength * 100)

  return (
    <div
      className={cn(
        'chunk-card bg-card border rounded-[12px] p-[18px] px-4 flex flex-col gap-2 cursor-pointer transition-[border-color,transform] duration-200',
        locked ? 'opacity-50 cursor-default saturate-[0.4] border-border' : 'border-border hover:-translate-y-0.5',
      )}
      style={!locked ? { ['--hover-border' as string]: accent } : undefined}
      onMouseEnter={e => { if (!locked) (e.currentTarget as HTMLDivElement).style.borderColor = accent }}
      onMouseLeave={e => { if (!locked) (e.currentTarget as HTMLDivElement).style.borderColor = done ? `color-mix(in srgb, ${accent} 25%, transparent)` : 'var(--border)' }}
      onClick={onClick}
    >
      <div className="flex items-center justify-between">
        <span className="flex items-center text-[26px] leading-none">
          {locked ? <Lock size={22} color="var(--muted)" strokeWidth={1.75} /> : ch.glyph}
        </span>
        <Badge variant={done ? 'active' : ch.status === 'IN_PROGRESS' ? 'application' : locked ? 'gray' : 'green' as 'active' | 'application' | 'gray'}>
          {done ? 'Complete' : ch.status === 'IN_PROGRESS' ? 'In Progress' : locked ? 'Locked' : 'Start'}
        </Badge>
      </div>
      <div className="text-[15px] font-bold text-text leading-[1.35]">{ch.title}</div>
      <div className="text-[11px] text-muted">{ch.totalLessons} concepts</div>
      {!locked && (
        <div className="flex items-center gap-2 mt-1">
          <div className="flex-1 h-1 bg-border rounded-full overflow-hidden">
            <div className="h-full bg-teal rounded-full" style={{ width: `${Math.round((ch.completedLessons / ch.totalLessons) * 100)}%` }} />
          </div>
          <span className="text-[10px] text-muted flex-shrink-0">{ch.completedLessons}/{ch.totalLessons}</span>
        </div>
      )}
      {done && (
        <div className="flex items-center gap-2 mt-0.5">
          <div className="flex-1 h-[3px] bg-border rounded-full overflow-hidden">
            <div
              className={cn(
                'h-full rounded-full strength-bar-fill',
                ch.healthColor === 'GREEN'  ? 'strength-green bg-teal'   :
                ch.healthColor === 'YELLOW' ? 'strength-yellow bg-orange' : 'strength-red bg-red',
              )}
              style={{ width: `${pct}%` }}
            />
          </div>
          <span className="text-[10px] text-muted flex-shrink-0">{pct}% mem</span>
        </div>
      )}
    </div>
  )
}

export default function DomainPage() {
  const { domainId } = useParams<{ domainId: string }>()
  const navigate = useNavigate()
  const { user } = useAuth()
  const { data: dashboard, isLoading } = useDashboard(domainId ?? '')

  const meta = TOPIC_META[domainId ?? ''] ?? { name: domainId, glyph: '📖', tagline: '', accent: 'var(--teal)' }

  if (isLoading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-muted">
        <div className="text-[48px] mb-3">{meta.glyph}</div>
        <p>Loading {meta.name}...</p>
      </div>
    )
  }

  if (!dashboard || !user) return null

  const progressPct = Math.round(dashboard.overallProgress * 100)

  return (
    <div className="max-w-[900px] mx-auto px-5 py-6 pb-[72px] max-[600px]:px-3 max-[600px]:py-4 max-[480px]:px-2.5">
      {/* Hero */}
      <div
        className="flex flex-col items-center text-center px-5 py-8 pb-7 rounded-[16px] mb-6 relative overflow-hidden"
        style={{
          border: `1px solid color-mix(in srgb, ${meta.accent} 25%, transparent)`,
          background: `radial-gradient(ellipse at 50% 0%, color-mix(in srgb, ${meta.accent} 12%, transparent) 0%, transparent 60%), var(--card)`,
        }}
      >
        <button className="btn btn-ghost text-[12px] self-start mb-4" onClick={() => navigate('/domains')}>
          ← All Topics
        </button>
        <div className="mb-2.5 flex justify-center">
          <DomainIcon domainId={domainId ?? ''} size={52} />
        </div>
        <h1 className="font-cinzel text-[28px] font-bold m-0 mb-2 max-[600px]:text-[22px]" style={{ color: meta.accent }}>{meta.name}</h1>
        <p className="text-[14px] text-muted leading-[1.7] max-w-[480px] m-0 mb-5">{meta.tagline}</p>
        <div className="w-full max-w-[400px]">
          <div className="flex justify-between text-[12px] text-muted mb-1.5">
            <span>Overall mastery</span><span>{progressPct}%</span>
          </div>
          <div className="h-[6px] bg-border rounded-full overflow-hidden">
            <div className="h-full rounded-full transition-[width] duration-400" style={{ width: `${progressPct}%`, background: meta.accent }} />
          </div>
        </div>
      </div>

      {meta.studyOrder && (
        <div className="mb-7">
          <div className="font-cinzel text-[14px] font-bold text-gold tracking-[0.08em] uppercase mb-3.5">Suggested Study Order</div>
          <div className="grid gap-3 max-[600px]:grid-cols-1" style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(190px, 1fr))' }}>
            {meta.studyOrder.map((phase, index) => (
              <div
                key={`${phase.label}-${index}`}
                className="bg-card border border-border rounded-[12px] p-4"
                style={{ borderTopColor: `color-mix(in srgb, ${meta.accent} 55%, transparent)`, borderTopWidth: 2 }}
              >
                <div className="text-[11px] text-muted font-cinzel uppercase tracking-[0.08em] mb-1">Phase {index + 1}</div>
                <div className="text-[14px] font-bold text-text mb-1.5">{phase.label}</div>
                <p className="text-[12px] text-muted leading-[1.55] m-0">{phase.detail}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Reviews due */}
      {dashboard.reviewsDue > 0 && (
        <div className="flex gap-3 mb-7 flex-wrap max-[600px]:flex-col">
          <div
            className="flex-1 min-w-[200px] bg-card border border-border rounded-[12px] p-4 px-[18px] flex flex-col gap-1 cursor-pointer transition-[border-color,transform] duration-200 hover:border-teal hover:-translate-y-0.5 max-[600px]:min-w-0"
            onClick={() => navigate('/review')}
          >
            <div className="flex items-center"><BookOpen size={24} color="var(--teal)" strokeWidth={1.75} /></div>
            <div className="text-[15px] font-bold text-text">{dashboard.reviewsDue} Reviews Due</div>
            <div className="text-[12px] text-muted leading-[1.5]">Strengthen fading memories before they slip away</div>
          </div>
        </div>
      )}

      {/* Tier-grouped chunk grid */}
      {(() => {
        const byTier = TIER_ORDER.reduce<Record<string, typeof dashboard.chunkHealth>>((acc, t) => {
          acc[t] = dashboard.chunkHealth.filter(ch => ch.tier === t)
          return acc
        }, {})
        const activeTiers = TIER_ORDER.filter(t => (byTier[t]?.length ?? 0) > 0)
        return activeTiers.map(tier => {
          const chunks = byTier[tier]
          const tierComplete = chunks.every(ch => ch.status === 'COMPLETE')
          const tierStarted  = chunks.some(ch => ch.status !== 'LOCKED')
          const placedHere   = dashboard.currentPath === tier
          const donePct      = Math.round((chunks.filter(ch => ch.status === 'COMPLETE').length / chunks.length) * 100)

          return (
            <div key={tier} className="mb-8">
              <div className="tier-header flex items-center gap-3 mb-3.5 flex-wrap">
                <TierIcon tier={tier} size={20} />
                <div className="flex flex-col gap-0.5 flex-1">
                  <div className="flex items-center gap-2 flex-wrap">
                    <span className="font-cinzel text-[15px] font-bold text-gold tracking-[0.06em] uppercase">
                      {TIER_LABELS[tier]}
                    </span>
                    {placedHere && (
                      <span className="chip chip-purple text-[10px] py-[2px] px-2 flex items-center gap-1">
                        <MapPin size={10} strokeWidth={2} /> Placed here
                      </span>
                    )}
                    {tierComplete && (
                      <span className="chip chip-teal text-[10px] py-[2px] px-2 flex items-center gap-1">
                        <Check size={10} strokeWidth={2.5} /> Complete
                      </span>
                    )}
                    {!tierStarted && !tierComplete && (
                      <span className="chip chip-gray text-[10px] py-[2px] px-2">Locked</span>
                    )}
                  </div>
                  <span className="text-[12px] text-muted">{TIER_DESC[tier]}</span>
                </div>
                <button
                  className="btn btn-ghost text-[11px] py-1 px-3 self-start flex-shrink-0 max-[480px]:hidden"
                  onClick={() => navigate(`/domain/${domainId}/diagnostic`)}
                  title="Retake diagnostic"
                >
                  <Sparkles size={12} strokeWidth={1.75} />
                  Retake Diagnostic
                </button>
              </div>
              <div className="chunk-grid grid gap-3.5 max-[600px]:grid-cols-1" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))' }}>
                {chunks.map(ch => (
                  <ChunkCard
                    key={ch.moduleId}
                    ch={ch}
                    accent={meta.accent}
                    onClick={() => ch.status !== 'LOCKED' && navigate(`/chunk/${ch.moduleId}`)}
                  />
                ))}
              </div>
            </div>
          )
        })
      })()}
    </div>
  )
}
