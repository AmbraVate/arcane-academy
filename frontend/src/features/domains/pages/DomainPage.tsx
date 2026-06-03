import { useParams, useNavigate } from 'react-router-dom'
import { useDashboard } from '@/hooks/queries'
import { useAuth } from '@/shared/hooks/useAuth'
import type { ModuleHealthDto } from '@/shared/types'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { DomainIcon } from '@/components/icons/DomainIcon'
import { TierIcon } from '@/components/icons/TierIcon'
import { Check, MapPin, Lock, BookOpen } from 'lucide-react'
import CompletionRing from '@/components/ui/CompletionRing'
import PublicBanner from '@/components/layout/PublicBanner'
import { DOMAINS } from '@/features/domains/data/domains'

const TIER_ORDER = ['APPRENTICE', 'JUNIOR', 'SENIOR', 'LEAD']

const TIER_LABELS: Record<string, string> = {
  APPRENTICE: 'Apprentice',
  JUNIOR:     'Junior',
  SENIOR:     'Senior',
  LEAD:       'Lead',
}

const TIER_DESC: Record<string, string> = {
  APPRENTICE: 'Foundations — core concepts, vocabulary, and the essential knowledge every learner needs.',
  JUNIOR:     'Applied knowledge — practical skills and techniques used in real-world contexts.',
  SENIOR:     'Advanced depth — specialist topics, critical evaluation, and complex synthesis.',
  LEAD:       'Mastery — professional practice, critical perspectives, and leadership in the field.',
}

function ChunkCard({ ch, onClick, accent = 'var(--teal)' }: { ch: ModuleHealthDto; onClick: () => void; accent?: string }) {
  const locked = ch.status === 'LOCKED'
  const done   = ch.status === 'COMPLETE'
  const inProg = ch.status === 'IN_PROGRESS'
  const completionPct = ch.totalLessons > 0
    ? Math.round((ch.completedLessons / ch.totalLessons) * 100)
    : 0
  const memPct = Math.round(ch.memoryStrength * 100)

  const ringColor = done
    ? (ch.healthColor === 'GREEN' ? 'var(--teal)' : ch.healthColor === 'YELLOW' ? 'var(--orange)' : 'var(--red)')
    : accent

  return (
    <div
      className={cn(
        'chunk-card bg-card border rounded-[12px] p-4 flex items-start gap-3 cursor-pointer transition-[border-color,transform] duration-200',
        locked ? 'opacity-40 cursor-default saturate-[0.4] border-border' : 'border-border hover:-translate-y-0.5',
      )}
      onMouseEnter={e => { if (!locked) (e.currentTarget as HTMLDivElement).style.borderColor = accent }}
      onMouseLeave={e => { if (!locked) (e.currentTarget as HTMLDivElement).style.borderColor = done ? `color-mix(in srgb, ${accent} 25%, transparent)` : 'var(--border)' }}
      onClick={onClick}
    >
      {/* Lock — only shown when module is locked */}
      {locked && (
        <div className="w-8 flex-shrink-0 flex items-center justify-center pt-0.5">
          <Lock size={18} color="var(--muted)" strokeWidth={1.75} />
        </div>
      )}

      {/* Title + meta */}
      <div className="flex-1 min-w-0">
        <div className="text-[13px] font-bold text-text leading-[1.4] line-clamp-2">{ch.title}</div>
        <div className="flex items-center gap-2 mt-0.5">
          <span className="text-[11px] text-muted">{ch.totalLessons} lessons</span>
          {!locked && inProg && (
            <span className="text-[10px] text-purple-light font-cinzel uppercase tracking-wide">In Progress</span>
          )}
        </div>
        {/* Memory bar — only for completed modules */}
        {done && (
          <div className="flex items-center gap-1.5 mt-1.5">
            <div className="w-[60px] h-[3px] bg-border rounded-full overflow-hidden">
              <div
                className={cn(
                  'h-full rounded-full',
                  ch.healthColor === 'GREEN'  ? 'bg-teal'   :
                  ch.healthColor === 'YELLOW' ? 'bg-orange' : 'bg-red',
                )}
                style={{ width: `${memPct}%` }}
              />
            </div>
            <span className="text-[10px] text-muted">{memPct}% mem</span>
          </div>
        )}
      </div>

      {/* Completion ring */}
      <CompletionRing
        pct={locked ? 0 : done ? 100 : completionPct}
        color={locked ? 'var(--border)' : ringColor}
        size={44}
        strokeWidth={3}
        emptyLabel="—"
      />
    </div>
  )
}

export default function DomainPage() {
  const { domainId } = useParams<{ domainId: string }>()
  const navigate = useNavigate()
  const { user } = useAuth()
  const isPublic = !user
  const { data: dashboard, isLoading } = useDashboard(domainId ?? '', isPublic)

  const domainMeta = DOMAINS.find(d => d.id === domainId)
  const meta = {
    name:        domainMeta?.name     ?? domainId ?? 'Pathway',
    glyph:       domainMeta?.glyph    ?? '📖',
    tagline:     domainMeta?.tagline  ?? '',
    accentStroke: domainMeta?.accentStroke ?? 'var(--teal)',
  }

  if (isLoading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-muted">
        <div className="text-[48px] mb-3">{meta.glyph}</div>
        <p>Loading {meta.name}...</p>
      </div>
    )
  }

  if (!dashboard) return null

  const progressPct = isPublic ? 0 : Math.round(dashboard.overallProgress * 100)

  function handleModuleClick(ch: ModuleHealthDto) {
    if (isPublic) {
      navigate(`/module/${ch.moduleId}`)
      return
    }
    if (ch.status !== 'LOCKED') navigate(`/module/${ch.moduleId}`)
  }

  return (
    <div className="max-w-[900px] mx-auto px-5 py-6 pb-[72px] max-[600px]:px-3 max-[600px]:py-4 max-[480px]:px-2.5">
      {isPublic && <PublicBanner />}
      {/* Hero */}
      <div
        className="flex flex-col items-center text-center px-5 py-8 pb-7 rounded-[16px] mb-6 relative overflow-hidden"
        style={{
          border: `1px solid color-mix(in srgb, ${meta.accentStroke} 25%, transparent)`,
          background: `radial-gradient(ellipse at 50% 0%, color-mix(in srgb, ${meta.accentStroke} 12%, transparent) 0%, transparent 60%), var(--card)`,
        }}
      >
        <button className="btn btn-ghost text-[12px] self-start mb-4" onClick={() => navigate('/schools')}>
          ← Schools
        </button>
        <div className="mb-2.5 flex justify-center">
          <DomainIcon domainId={domainId ?? ''} size={52} />
        </div>
        <h1 className="font-cinzel text-[28px] font-bold m-0 mb-2 max-[600px]:text-[22px]" style={{ color: meta.accentStroke }}>{meta.name}</h1>
        <p className="text-[14px] text-muted leading-[1.7] max-w-[480px] m-0 mb-5">{meta.tagline}</p>
        <div className="w-full max-w-[400px]">
          <div className="flex justify-between text-[12px] text-muted mb-1.5">
            <span>Overall mastery</span><span>{progressPct}%</span>
          </div>
          <div className="h-[6px] bg-border rounded-full overflow-hidden">
            <div className="h-full rounded-full transition-[width] duration-400" style={{ width: `${progressPct}%`, background: meta.accentStroke }} />
          </div>
        </div>
      </div>

      {/* Reviews due — only shown when authenticated */}
      {!isPublic && dashboard.reviewsDue > 0 && (
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
        const byTier = TIER_ORDER.reduce<Record<string, typeof dashboard.moduleHealth>>((acc, t) => {
          acc[t] = dashboard.moduleHealth.filter(ch => ch.tier === t)
          return acc
        }, {})
        const activeTiers = TIER_ORDER.filter(t => (byTier[t]?.length ?? 0) > 0)
        return activeTiers.map(tier => {
          const chunks = byTier[tier]
          const tierComplete = chunks.every(ch => ch.status === 'COMPLETE')
          const tierStarted  = chunks.some(ch => ch.status !== 'LOCKED')
          const placedHere   = dashboard.currentPath === tier

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
              </div>
              <div className="chunk-grid grid gap-2.5" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(min(260px, 100%), 1fr))' }}>
                {chunks.map(ch => (
                  <ChunkCard
                    key={ch.moduleId}
                    ch={ch}
                    accent={meta.accentStroke}
                    onClick={() => handleModuleClick(ch)}
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
