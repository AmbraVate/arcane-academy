import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { dashboardApi } from '../api/services'
import type { DashboardDto, ChunkHealthDto } from '../types'
import { cn } from '@/lib/utils'

const TIERS = [
  {
    key: 'FOUNDATION',
    label: 'Foundational',
    glyph: '🌱',
    desc: 'Core building blocks — variables, control flow, loops, and methods.',
    labelColor: 'text-teal',
    borderColor: 'border-[rgba(45,212,191,0.25)]',
  },
  {
    key: 'PRACTITIONER',
    label: 'Practitioner',
    glyph: '⚗️',
    desc: 'Applied mastery — arrays, collections, classes, encapsulation, and inheritance.',
    labelColor: 'text-purple-light',
    borderColor: 'border-[rgba(139,92,246,0.25)]',
  },
  {
    key: 'EXPERT',
    label: 'Expert',
    glyph: '🔮',
    desc: 'Advanced craft — polymorphism, exception handling, and core APIs.',
    labelColor: 'text-gold',
    borderColor: 'border-[rgba(201,162,39,0.25)]',
  },
]

const HEALTH_COLORS: Record<string, { border: string; fill: string }> = {
  GREEN:  { border: 'border-[rgba(0,200,83,0.2)]',    fill: 'bg-green' },
  YELLOW: { border: 'border-[rgba(255,165,0,0.2)]',   fill: 'bg-orange' },
  RED:    { border: 'border-[rgba(255,60,60,0.2)]',   fill: 'bg-red' },
  GRAY:   { border: 'border-border',                   fill: 'bg-muted' },
}

function ChunkCard({ ch, onClick }: { ch: ChunkHealthDto; onClick: () => void }) {
  const locked = ch.status === 'LOCKED'
  const health = HEALTH_COLORS[ch.healthColor ?? 'GRAY'] ?? HEALTH_COLORS.GRAY

  return (
    <div
      className={cn(
        'bg-card border rounded-[10px] p-4 transition-[border-color,transform] duration-200',
        locked
          ? 'border-border cursor-not-allowed opacity-45 grayscale-[0.6]'
          : cn('cursor-pointer hover:-translate-y-0.5 hover:border-purple', health.border)
      )}
      onClick={onClick}
    >
      <div className="text-[28px] mb-1.5">{locked ? '🔒' : ch.glyph}</div>
      <div className="text-[13px] font-semibold text-text mb-1.5">{ch.title}</div>
      <div className="text-[11px] text-muted mb-2">
        {locked ? 'Locked' : `${ch.completedSubChunks}/${ch.totalSubChunks} concepts`}
      </div>
      {!locked && (
        <>
          <div className="h-[5px] bg-surface rounded-full overflow-hidden mb-1">
            <div
              className={cn('h-full rounded-full transition-[width] duration-400', health.fill)}
              style={{ width: `${Math.round(ch.memoryStrength * 100)}%` }}
            />
          </div>
          <div className="text-[10px] text-muted">{Math.round(ch.memoryStrength * 100)}% memory</div>
        </>
      )}
    </div>
  )
}

export default function DashboardPage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [dashboard, setDashboard] = useState<DashboardDto | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    dashboardApi.get()
      .then(d => {
        if (!d.diagnosticCompleted) {
          navigate('/onboarding', { replace: true })
          return
        }
        setDashboard(d)
      })
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [navigate])

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-muted">
        <div className="text-[48px] mb-3 animate-pulse">🧙</div>
        <p>Consulting the Grimoire...</p>
      </div>
    )
  }

  if (!dashboard || !user) return null

  const progressPct = Math.round(dashboard.overallProgress * 100)
  const chunksByTier = (tier: string) =>
    dashboard.chunkHealth.filter(ch => (ch.tier ?? 'FOUNDATION') === tier)

  return (
    <div className="max-w-[900px] mx-auto px-4 py-6 pb-[60px] max-[480px]:px-3 max-[480px]:py-4 max-[480px]:pb-12">
      {/* Hero */}
      <div className="text-center mb-7">
        <div className="text-[56px] mb-2 max-[480px]:text-[44px]">🧙</div>
        <h1 className="text-[28px] font-bold text-gold m-0 mb-1 max-[480px]:text-[22px]">The Java Grimoire</h1>
        <p className="text-muted text-[14px] m-0 mb-4 max-[480px]:text-[13px]">Your path from apprentice to Archmage</p>
        <div className="max-w-[400px] mx-auto">
          <div className="flex justify-between text-[12px] text-muted mb-1">
            <span>Overall mastery</span>
            <span>{progressPct}%</span>
          </div>
          <div className="h-2 bg-surface rounded-full overflow-hidden">
            <div className="h-full bg-purple rounded-full transition-[width] duration-400"
              style={{ width: `${progressPct}%` }} />
          </div>
        </div>
      </div>

      {/* Stats row */}
      <div className="grid grid-cols-4 gap-3 mb-6 max-[600px]:grid-cols-2">
        {[
          { val: dashboard.totalXp,       lbl: 'total xp',  extra: '' },
          { val: dashboard.rank,           lbl: 'rank',      extra: '' },
          { val: `🔥 ${dashboard.streakDays}`, lbl: 'day streak', extra: dashboard.streakAtRisk ? 'text-orange' : '' },
          { val: dashboard.currentPath,    lbl: 'path',      extra: '' },
        ].map(({ val, lbl, extra }) => (
          <div key={lbl} className="bg-card border border-border rounded-[8px] px-2.5 py-3.5 text-center">
            <div className={cn('text-[20px] font-bold text-text max-[480px]:text-[17px]', extra)}>{val}</div>
            <div className="text-[11px] text-muted uppercase mt-0.5">{lbl}</div>
          </div>
        ))}
      </div>

      {/* Action cards */}
      <div className="flex gap-3 mb-7 flex-wrap max-[600px]:flex-col">
        {!dashboard.diagnosticCompleted && (
          <div
            className="flex-1 min-w-[200px] bg-card border border-border rounded-[10px] p-[18px] cursor-pointer
              transition-[border-color] duration-200 hover:border-purple max-[600px]:min-w-0"
            onClick={() => navigate('/diagnostic')}
          >
            <div className="text-[28px] mb-1.5">🔮</div>
            <div className="text-[15px] font-semibold text-text mb-1">Take Entry Diagnostic</div>
            <div className="text-[12px] text-muted leading-[1.5]">Find your starting point — skip what you already know</div>
          </div>
        )}
        {dashboard.reviewsDue > 0 && (
          <div
            className="flex-1 min-w-[200px] bg-card border border-border rounded-[10px] p-[18px] cursor-pointer
              transition-[border-color] duration-200 hover:border-purple max-[600px]:min-w-0"
            onClick={() => navigate('/review')}
          >
            <div className="text-[28px] mb-1.5">📖</div>
            <div className="text-[15px] font-semibold text-text mb-1">{dashboard.reviewsDue} Reviews Due</div>
            <div className="text-[12px] text-muted leading-[1.5]">Strengthen fading memories before they slip away</div>
          </div>
        )}
      </div>

      {/* Knowledge map */}
      <div className="text-[18px] font-bold text-gold mb-3.5">Knowledge Map</div>

      {TIERS.map(tier => {
        const chunks = chunksByTier(tier.key)
        if (chunks.length === 0) return null
        return (
          <div key={tier.key} className={cn('border rounded-[14px] p-5 mb-5 bg-card', tier.borderColor)}>
            <div className="flex items-start gap-3.5 mb-4">
              <span className="text-[28px] flex-shrink-0 mt-0.5">{tier.glyph}</span>
              <div>
                <div className={cn('font-cinzel text-[14px] font-bold tracking-[0.04em] mb-0.5', tier.labelColor)}>
                  {tier.label}
                </div>
                <div className="text-[12px] text-muted leading-[1.5]">{tier.desc}</div>
              </div>
              <div className="ml-auto font-cinzel text-[13px] text-muted flex-shrink-0 pt-1">
                {chunks.filter(c => c.status === 'COMPLETE').length}/{chunks.length}
              </div>
            </div>
            <div className="grid gap-2.5" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(150px, 1fr))' }}>
              {chunks.map(ch => (
                <ChunkCard
                  key={ch.chunkId}
                  ch={ch}
                  onClick={() => ch.status !== 'LOCKED' && navigate(`/chunk/${ch.chunkId}`)}
                />
              ))}
            </div>
          </div>
        )
      })}
    </div>
  )
}
