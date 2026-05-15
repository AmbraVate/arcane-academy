import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { useTheme } from '../hooks/useTheme'
import { badgeApi, profileApi, dashboardApi, rabbitHoleTermApi } from '../api/services'
import type { Badge, DashboardDto, RabbitHoleTerm } from '@/types'
import { cn } from '@/lib/utils'

type Tab = 'overview' | 'topics' | 'badges' | 'rabbit-holes' | 'preferences'

const BADGE_CATEGORIES = ['LEARNING', 'MASTERY', 'FEYNMAN', 'PATH', 'EXPLORATION', 'XP', 'STREAK']
const CATEGORY_LABELS: Record<string, string> = {
  LEARNING: 'Learning', MASTERY: 'Mastery', FEYNMAN: 'Feynman',
  PATH: 'Path', EXPLORATION: 'Exploration', XP: 'XP', STREAK: 'Streak',
}

export default function ProfilePage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const { theme, toggleTheme } = useTheme()
  const [tab, setTab] = useState<Tab>('overview')

  const [badges, setBadges] = useState<Badge[]>([])
  const [badgesLoading, setBadgesLoading] = useState(false)
  const [badgeCategoryFilter, setBadgeCategoryFilter] = useState<string>('ALL')

  const [javaDash, setJavaDash] = useState<DashboardDto | null>(null)
  const [dashLoading, setDashLoading] = useState(false)

  const [rabbitHoles, setRabbitHoles] = useState<RabbitHoleTerm[]>([])
  const [rhLoading, setRhLoading] = useState(false)
  const [removingTerm, setRemovingTerm] = useState<string | null>(null)

  const [publicEnabled, setPublicEnabled] = useState<boolean | null>(null)
  const [savingVisibility, setSavingVisibility] = useState(false)

  // Load visibility + overview data on mount
  useEffect(() => {
    profileApi.getVisibility().then(setPublicEnabled).catch(() => setPublicEnabled(false))
  }, [])

  // Lazy-load per tab
  useEffect(() => {
    if (tab === 'badges' && badges.length === 0 && !badgesLoading) {
      setBadgesLoading(true)
      badgeApi.getAll().then(setBadges).finally(() => setBadgesLoading(false))
    }
    if (tab === 'topics' && !javaDash && !dashLoading) {
      setDashLoading(true)
      dashboardApi.get('java').then(setJavaDash).catch(() => {}).finally(() => setDashLoading(false))
    }
    if (tab === 'rabbit-holes' && rabbitHoles.length === 0 && !rhLoading) {
      setRhLoading(true)
      rabbitHoleTermApi.getAll().then(setRabbitHoles).finally(() => setRhLoading(false))
    }
  }, [tab]) // eslint-disable-line react-hooks/exhaustive-deps

  async function toggleVisibility() {
    if (publicEnabled === null || savingVisibility) return
    setSavingVisibility(true)
    try {
      const next = await profileApi.setVisibility(!publicEnabled)
      setPublicEnabled(next)
    } catch { /* keep prior state */ } finally { setSavingVisibility(false) }
  }

  async function removeRabbitHoleTerm(term: string) {
    setRemovingTerm(term)
    try {
      await rabbitHoleTermApi.remove(term)
      setRabbitHoles(prev => prev.filter(r => r.term !== term))
    } catch { /* ignore */ } finally { setRemovingTerm(null) }
  }

  if (!user) return null

  const earned = badges.filter(b => b.earned)
  const filteredBadges = badgeCategoryFilter === 'ALL' ? badges : badges.filter(b => b.category === badgeCategoryFilter)
  const earnedFiltered = filteredBadges.filter(b => b.earned)
  const availableFiltered = filteredBadges.filter(b => !b.earned)

  const tabs: { id: Tab; label: string }[] = [
    { id: 'overview', label: 'Overview' },
    { id: 'topics', label: 'Topics' },
    { id: 'badges', label: `Badges${earned.length ? ` (${earned.length})` : ''}` },
    { id: 'rabbit-holes', label: `Rabbit Holes${rabbitHoles.length ? ` (${rabbitHoles.length})` : ''}` },
    { id: 'preferences', label: 'Preferences' },
  ]

  return (
    <div className="flex-1 overflow-y-auto px-6 py-8 max-[600px]:px-3 max-[600px]:py-5">
      <div className="max-w-[800px] mx-auto">

        {/* Profile header — always visible */}
        <div className="flex items-center gap-6 p-7 bg-card border border-border rounded-[14px] mb-5 max-[600px]:flex-col max-[600px]:text-center">
          <div className="text-[56px] w-20 h-20 flex items-center justify-center bg-purple-dim border-2 border-purple rounded-full flex-shrink-0 max-[480px]:text-[44px] max-[480px]:w-16 max-[480px]:h-16">
            {user.rank === 'Archmage' ? '🧙' : '✨'}
          </div>
          <div className="flex-1">
            <h1 className="font-cinzel text-[24px] text-gold mb-3 max-[480px]:text-[20px]">{user.username}</h1>
            <div className="flex gap-6 flex-wrap max-[600px]:justify-center max-[480px]:gap-4">
              {[
                { label: 'Rank',     value: user.rank },
                { label: 'Total XP', value: user.totalXp.toLocaleString() },
                { label: 'Streak',   value: `${user.streakDays}d` },
                { label: 'Badges',   value: badges.length ? `${earned.length}/${badges.length}` : `${earned.length}` },
              ].map(({ label, value }) => (
                <span key={label} className="flex flex-col gap-0.5">
                  <span className="text-[11px] text-muted uppercase tracking-[1px] font-cinzel">{label}</span>
                  <span className="text-[18px] text-text font-semibold">{value}</span>
                </span>
              ))}
            </div>
          </div>
        </div>

        {/* Tab bar */}
        <div className="flex gap-1 mb-6 border-b border-border pb-0">
          {tabs.map(t => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={cn(
                'px-4 py-2.5 text-[13px] font-cinzel tracking-wide border-b-2 transition-[border-color,color] duration-150 -mb-px',
                tab === t.id
                  ? 'border-gold text-gold'
                  : 'border-transparent text-muted hover:text-text',
              )}
            >
              {t.label}
            </button>
          ))}
        </div>

        {/* Tab: Overview */}
        {tab === 'overview' && (
          <div className="flex flex-col gap-4">
            {/* Visibility toggle */}
            <div className="bg-card border border-border rounded-[12px] px-5 py-4 flex items-center justify-between gap-4 max-[480px]:flex-col max-[480px]:items-start">
              <div>
                <div className="font-cinzel text-[13px] text-text mb-0.5">
                  Public profile {publicEnabled ? <span className="text-green">· On</span> : <span className="text-muted">· Off</span>}
                </div>
                <div className="text-[11px] text-muted leading-snug">
                  {publicEnabled
                    ? `Visible at /u/${user.username} and on leaderboards. Email and auth details are never shown.`
                    : 'Off by default. Turn on to appear on leaderboards and your public profile page.'}
                </div>
              </div>
              <button
                onClick={toggleVisibility}
                disabled={publicEnabled === null || savingVisibility}
                className={cn(
                  'flex-shrink-0 px-4 py-1.5 rounded-[7px] text-[12px] font-cinzel tracking-wide border transition-[background,border-color] duration-150 disabled:opacity-50',
                  publicEnabled
                    ? 'bg-purple-dim border-purple text-purple-light'
                    : 'bg-card border-border text-muted hover:border-purple-dim'
                )}
              >
                {savingVisibility ? 'Saving…' : publicEnabled ? 'Make private' : 'Make public'}
              </button>
            </div>

            {/* Quick nav cards */}
            <div className="grid grid-cols-3 gap-3 max-[480px]:grid-cols-1">
              {[
                { label: 'Topics & Progress', glyph: '📚', tab: 'topics' as Tab },
                { label: 'Badges', glyph: '🏅', tab: 'badges' as Tab },
                { label: 'Rabbit Holes', glyph: '🐇', tab: 'rabbit-holes' as Tab },
              ].map(({ label, glyph, tab: t }) => (
                <button
                  key={t}
                  onClick={() => setTab(t)}
                  className="bg-card border border-border rounded-[12px] p-4 text-center hover:border-purple-dim transition-[border-color] duration-150"
                >
                  <div className="text-[28px] mb-2">{glyph}</div>
                  <div className="text-[12px] text-muted font-cinzel">{label}</div>
                </button>
              ))}
            </div>
          </div>
        )}

        {/* Tab: Topics */}
        {tab === 'topics' && (
          <div>
            {dashLoading && <p className="text-muted italic text-center py-8">Loading topic data…</p>}
            {!dashLoading && javaDash && (
              <TopicCard
                topicId="java"
                glyph="☕"
                name="Java"
                tier={javaDash.currentPath}
                diagnosticCompleted={javaDash.diagnosticCompleted}
                completedSubChunks={javaDash.chunkHealth.reduce((s, c) => s + c.completedSubChunks, 0)}
                totalSubChunks={javaDash.chunkHealth.reduce((s, c) => s + c.totalSubChunks, 0)}
                totalXp={javaDash.totalXp}
                onContinue={() => navigate('/topic/java')}
                onRetakeDiagnostic={() => navigate('/topic/java/diagnostic')}
              />
            )}
            {!dashLoading && !javaDash && (
              <div className="text-center py-10 text-muted italic">
                <p>No topic data found. Start a topic to see your progress here.</p>
                <button className="btn btn-primary mt-4" onClick={() => navigate('/topics')}>Browse Topics →</button>
              </div>
            )}
          </div>
        )}

        {/* Tab: Badges */}
        {tab === 'badges' && (
          <div>
            {badgesLoading && <p className="text-muted italic text-center py-8">Loading badges…</p>}
            {!badgesLoading && (
              <>
                {/* Category filter chips */}
                <div className="flex gap-2 flex-wrap mb-5">
                  {['ALL', ...BADGE_CATEGORIES].map(cat => (
                    <button
                      key={cat}
                      onClick={() => setBadgeCategoryFilter(cat)}
                      className={cn(
                        'px-3 py-1 rounded-[10px] text-[11px] font-cinzel tracking-wide border transition-[background,border-color] duration-150',
                        badgeCategoryFilter === cat
                          ? 'bg-purple text-white border-purple'
                          : 'bg-card border-border text-muted hover:border-purple-dim'
                      )}
                    >
                      {cat === 'ALL' ? 'All' : CATEGORY_LABELS[cat]}
                    </button>
                  ))}
                </div>

                {earnedFiltered.length > 0 && (
                  <section className="mb-7">
                    <h2 className="font-cinzel text-[13px] text-gold tracking-[1px] mb-3 pb-1.5 border-b border-border">
                      ✦ Earned ({earnedFiltered.length})
                    </h2>
                    <BadgeGrid badges={earnedFiltered} />
                  </section>
                )}

                {availableFiltered.length > 0 && (
                  <section>
                    <h2 className="font-cinzel text-[13px] text-muted tracking-[1px] mb-3 pb-1.5 border-b border-border">
                      🔒 Available to Earn ({availableFiltered.length})
                    </h2>
                    <BadgeGrid badges={availableFiltered} />
                  </section>
                )}

                {filteredBadges.length === 0 && (
                  <p className="text-muted italic text-center py-10">No badges in this category yet.</p>
                )}
              </>
            )}
          </div>
        )}

        {/* Tab: Preferences */}
        {tab === 'preferences' && (
          <div className="flex flex-col gap-4">
            <div className="bg-card border border-border rounded-[12px] px-5 py-4 flex items-center justify-between gap-4 max-[480px]:flex-col max-[480px]:items-start">
              <div>
                <div className="font-cinzel text-[13px] text-text mb-0.5">
                  Theme {theme === 'blizzard'
                    ? <span className="text-[#4a9eda]">· Blizzard</span>
                    : <span className="text-purple-light">· Arcane</span>}
                </div>
                <div className="text-[11px] text-muted leading-snug">
                  {theme === 'blizzard'
                    ? 'Stone frame, icicles, and frost atmosphere active. Switch back to the default Arcane look.'
                    : 'The default wizard aesthetic. Switch to Blizzard for a dark fantasy stone-and-ice experience.'}
                </div>
              </div>
              <button
                onClick={toggleTheme}
                className={cn(
                  'flex-shrink-0 px-4 py-1.5 rounded-[7px] text-[12px] font-cinzel tracking-wide border transition-[background,border-color,color] duration-150',
                  theme === 'blizzard'
                    ? 'bg-[rgba(74,158,218,0.12)] border-[rgba(74,158,218,0.4)] text-[#4a9eda] hover:bg-[rgba(74,158,218,0.2)]'
                    : 'bg-purple-dim border-purple text-purple-light hover:bg-purple/20'
                )}
              >
                {theme === 'blizzard' ? '✦ Switch to Arcane' : '❄ Switch to Blizzard'}
              </button>
            </div>
          </div>
        )}

        {/* Tab: Rabbit Holes */}
        {tab === 'rabbit-holes' && (
          <div>
            {rhLoading && <p className="text-muted italic text-center py-8">Loading rabbit holes…</p>}
            {!rhLoading && rabbitHoles.length === 0 && (
              <div className="text-center py-12">
                <div className="text-[48px] mb-4">🐇</div>
                <p className="text-muted text-[14px] leading-[1.7] max-w-[360px] mx-auto">
                  No saved rabbit holes yet. While reading story content, click on highlighted terms to save them here for later exploration.
                </p>
              </div>
            )}
            {!rhLoading && rabbitHoles.length > 0 && (
              <div className="flex flex-col gap-3">
                {rabbitHoles.map(rh => (
                  <RabbitHoleCard
                    key={rh.id}
                    term={rh}
                    removing={removingTerm === rh.term}
                    onRemove={() => removeRabbitHoleTerm(rh.term)}
                  />
                ))}
              </div>
            )}
          </div>
        )}

      </div>
    </div>
  )
}

function TopicCard({
  topicId, glyph, name, tier, diagnosticCompleted,
  completedSubChunks, totalSubChunks, totalXp,
  onContinue, onRetakeDiagnostic,
}: {
  topicId: string; glyph: string; name: string; tier: string
  diagnosticCompleted: boolean
  completedSubChunks: number; totalSubChunks: number; totalXp: number
  onContinue: () => void; onRetakeDiagnostic: () => void
}) {
  const pct = totalSubChunks > 0 ? Math.round((completedSubChunks / totalSubChunks) * 100) : 0
  const TIER_LABELS: Record<string, string> = {
    FOUNDATION: 'Foundation', ADVANCED: 'Advanced',
    PRACTITIONER: 'Practitioner', EXPERT: 'Expert', CAPSTONE: 'Capstone',
  }
  return (
    <div className="bg-card border border-border rounded-[12px] p-5">
      <div className="flex items-center gap-3 mb-4">
        <span className="text-[32px]">{glyph}</span>
        <div className="flex-1">
          <div className="text-[16px] font-bold text-text">{name}</div>
          <div className="flex items-center gap-2 mt-0.5">
            <span className="chip chip-purple text-[10px]">{TIER_LABELS[tier] ?? tier}</span>
            {diagnosticCompleted
              ? <span className="text-[10px] text-muted">Diagnostic complete</span>
              : <span className="text-[10px] text-muted">No diagnostic taken</span>}
          </div>
        </div>
        <div className="text-right">
          <div className="text-[13px] font-bold text-gold">{totalXp.toLocaleString()} XP</div>
          <div className="text-[11px] text-muted">{completedSubChunks}/{totalSubChunks} lessons</div>
        </div>
      </div>

      {/* Progress bar */}
      <div className="h-1.5 bg-surface rounded-full overflow-hidden mb-4">
        <div className="h-full bg-purple rounded-full transition-[width] duration-500" style={{ width: `${pct}%` }} />
      </div>

      <div className="flex gap-2">
        <button className="btn btn-primary text-[12px] px-4 py-1.5" onClick={onContinue}>Continue →</button>
        <button className="btn btn-ghost text-[11px] px-3 py-1.5" onClick={onRetakeDiagnostic}>Retake Diagnostic</button>
      </div>
    </div>
  )
}

function BadgeGrid({ badges }: { badges: Badge[] }) {
  return (
    <div className="grid gap-3" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))' }}>
      {badges.map(badge => (
        <div
          key={badge.id}
          className={cn(
            'bg-card border rounded-[10px] px-4 py-5 text-center transition-[border-color,transform] duration-300',
            badge.earned
              ? 'border-[rgba(201,162,39,0.3)] hover:border-gold hover:-translate-y-0.5'
              : 'border-border opacity-45',
          )}
        >
          <div className="text-[36px] mb-2.5 max-[480px]:text-[28px]">
            {badge.earned ? badge.glyph : '🔒'}
          </div>
          <div className="font-cinzel text-[12px] text-text mb-1">{badge.displayName}</div>
          <div className="text-[11px] text-muted leading-[1.4]">{badge.description}</div>
          {badge.earned && badge.earnedAt && (
            <div className="text-[10px] text-gold mt-2 font-cinzel">
              {new Date(badge.earnedAt).toLocaleDateString()}
            </div>
          )}
        </div>
      ))}
    </div>
  )
}

function RabbitHoleCard({ term, removing, onRemove }: { term: RabbitHoleTerm; removing: boolean; onRemove: () => void }) {
  const [confirmRemove, setConfirmRemove] = useState(false)

  return (
    <div className="bg-card border border-border rounded-[12px] px-5 py-4 flex items-start gap-4">
      <div className="text-[24px] flex-shrink-0">🐇</div>
      <div className="flex-1 min-w-0">
        <div className="text-[14px] font-bold text-gold mb-0.5">{term.term}</div>
        {term.description && (
          <p className="text-[12px] text-muted leading-[1.55] mb-1.5">{term.description}</p>
        )}
        <div className="text-[10px] text-muted">
          {term.subChunkId && <span>From {term.subChunkId} · </span>}
          {new Date(term.savedAt).toLocaleDateString()}
        </div>
      </div>
      <div className="flex-shrink-0">
        {confirmRemove ? (
          <div className="flex gap-1.5">
            <button
              className="text-[11px] px-2.5 py-1 rounded-md bg-red/20 text-red border border-red cursor-pointer disabled:opacity-50"
              onClick={onRemove} disabled={removing}
            >
              {removing ? '…' : 'Remove'}
            </button>
            <button
              className="text-[11px] px-2.5 py-1 rounded-md bg-card border border-border text-muted cursor-pointer"
              onClick={() => setConfirmRemove(false)}
            >
              Cancel
            </button>
          </div>
        ) : (
          <button
            className="text-[11px] px-2.5 py-1 rounded-md bg-card border border-border text-muted cursor-pointer hover:border-red hover:text-red transition-[border-color,color] duration-150"
            onClick={() => setConfirmRemove(true)}
          >
            🗑
          </button>
        )}
      </div>
    </div>
  )
}
