import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'
import { badgeApi, capstoneApi, notesApi, profileApi, rabbitHoleTermApi, stuckReportApi, paymentsApi } from '@/shared/api/services'
import type { UserCapstone, UserNote, MyStuckReport, SubscriptionStatusResponse } from '@/shared/api/services'
import { useTopicsDashboard } from '@/hooks/queries'
import { ACTIVE_TOPICS } from '@/features/topics/data/topics'
import type { Badge, RabbitHoleTerm } from '@/shared/types'
import { useTheme } from '@/hooks/useTheme'
import type { Palette } from '@/hooks/useTheme'
import { cn } from '@/lib/utils'
import { Trash2, ExternalLink, Download, CreditCard, Zap, Crown, Infinity, CheckCircle } from 'lucide-react'
import { UpgradeModal } from '@/features/payment/components/UpgradeModal'

type Tab = 'overview' | 'topics' | 'badges' | 'rabbit-holes' | 'notes' | 'projects' | 'reports' | 'subscription' | 'preferences'

const PALETTES = [
  { id: 'frostmourne', name: 'Frostmourne', swatches: ['#5dc6ff', '#b8eaff', '#1a4f8f'] },
  { id: 'fel',         name: 'Fel',         swatches: ['#92ff35', '#dfffa6', '#1c4710'] },
  { id: 'bloodelf',    name: 'Blood Elf',   swatches: ['#ff4f6a', '#ffd866', '#5e0a1c'] },
  { id: 'arcane',      name: 'Arcane',      swatches: ['#b87bff', '#e8d6ff', '#3a1f7a'] },
  { id: 'bronze',      name: 'Bronze',      swatches: ['#ffb849', '#ffe2a6', '#5a3608'] },
  { id: 'shadowlands', name: 'Shadowlands', swatches: ['#c4a3ff', '#5dc6ff', '#2a1660'] },
  { id: 'naga',        name: 'Naga',        swatches: ['#2dd4bf', '#ff8a65', '#07473d'] },
]

const BADGE_CATEGORIES = ['LEARNING', 'MASTERY', 'FEYNMAN', 'PATH', 'EXPLORATION', 'XP', 'STREAK']
const CATEGORY_LABELS: Record<string, string> = {
  LEARNING: 'Learning', MASTERY: 'Mastery', FEYNMAN: 'Feynman',
  PATH: 'Path', EXPLORATION: 'Exploration', XP: 'XP', STREAK: 'Streak',
}

export default function ProfilePage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [searchParams, setSearchParams] = useSearchParams()
  const { theme, blizzardPrefs, blizzardAvailable, toggleTheme, setBlizzardPref } = useTheme()

  // Support ?tab=subscription deep-link (e.g. from Stripe portal return URL)
  const tabParam = searchParams.get('tab') as Tab | null
  const [tab, setTab] = useState<Tab>(tabParam ?? 'overview')

  const [badges, setBadges] = useState<Badge[]>([])
  const [badgesLoading, setBadgesLoading] = useState(false)
  const [badgeCategoryFilter, setBadgeCategoryFilter] = useState<string>('ALL')

  const allTopicDash = useTopicsDashboard(ACTIVE_TOPICS.map(t => t.id))
  const dashLoading = ACTIVE_TOPICS.some(t => allTopicDash[t.id] === undefined)

  const [rabbitHoles, setRabbitHoles] = useState<RabbitHoleTerm[]>([])
  const [rhLoading, setRhLoading] = useState(false)
  const [removingTerm, setRemovingTerm] = useState<string | null>(null)

  const [publicEnabled, setPublicEnabled] = useState<boolean | null>(null)
  const [savingVisibility, setSavingVisibility] = useState(false)

  const [notes, setNotes] = useState<UserNote[]>([])
  const [notesLoading, setNotesLoading] = useState(false)
  const [deletingNoteId, setDeletingNoteId] = useState<string | null>(null)
  const [noteSearch, setNoteSearch] = useState('')

  const [capstones, setCapstones] = useState<UserCapstone[]>([])
  const [capstonesLoading, setCapstonesLoading] = useState(false)
  const [editingCapstone, setEditingCapstone] = useState<UserCapstone | null>(null)

  const [reports, setReports] = useState<MyStuckReport[]>([])
  const [reportsLoading, setReportsLoading] = useState(false)

  const [subscriptionStatus, setSubscriptionStatus] = useState<SubscriptionStatusResponse | null>(null)
  const [subLoading, setSubLoading] = useState(false)
  const [portalLoading, setPortalLoading] = useState(false)
  const [showUpgradeFromProfile, setShowUpgradeFromProfile] = useState(false)

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
    if (tab === 'rabbit-holes' && rabbitHoles.length === 0 && !rhLoading) {
      setRhLoading(true)
      rabbitHoleTermApi.getAll().then(setRabbitHoles).finally(() => setRhLoading(false))
    }
    if (tab === 'notes' && notes.length === 0 && !notesLoading) {
      setNotesLoading(true)
      notesApi.list().then(setNotes).finally(() => setNotesLoading(false))
    }
    if (tab === 'projects' && capstones.length === 0 && !capstonesLoading) {
      setCapstonesLoading(true)
      capstoneApi.list().then(setCapstones).finally(() => setCapstonesLoading(false))
    }
    if (tab === 'reports' && reports.length === 0 && !reportsLoading) {
      setReportsLoading(true)
      stuckReportApi.mine().then(setReports).finally(() => setReportsLoading(false))
    }
    if (tab === 'subscription' && !subscriptionStatus && !subLoading) {
      setSubLoading(true)
      paymentsApi.getStatus().then(setSubscriptionStatus).finally(() => setSubLoading(false))
    }
  }, [tab]) // intentionally omitting derived state to avoid re-fetching on every render

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

  async function deleteNote(noteId: string) {
    setDeletingNoteId(noteId)
    try {
      await notesApi.delete(noteId)
      setNotes(prev => prev.filter(n => n.id !== noteId))
    } catch { /* ignore */ } finally { setDeletingNoteId(null) }
  }

  async function openBillingPortal() {
    setPortalLoading(true)
    try {
      const url = await paymentsApi.createPortal()
      window.location.href = url
    } catch {
      setPortalLoading(false)
    }
  }

  async function deleteCapstone(capstoneId: string) {
    try {
      await capstoneApi.delete(capstoneId)
      setCapstones(prev => prev.filter(c => c.id !== capstoneId))
    } catch { /* ignore */ }
  }

  function downloadCapstoneZip(capstone: UserCapstone) {
    // Build a simple ZIP manually (two-file: README.md + Main.java)
    // For simplicity without jszip dependency, offer files as a download link
    const readme = `# ${capstone.title}\n\n${capstone.description ?? ''}\n\n${capstone.githubUrl ? `GitHub: ${capstone.githubUrl}` : ''}`
    const code = capstone.codeContent ?? ''

    // Create a blob for README
    const readmeBlob = new Blob([readme], { type: 'text/markdown' })
    const readmeUrl = URL.createObjectURL(readmeBlob)
    const a = document.createElement('a')
    a.href = readmeUrl
    a.download = `${capstone.title.replace(/[^a-z0-9]/gi, '-')}-README.md`
    a.click()
    URL.revokeObjectURL(readmeUrl)

    if (code) {
      const codeBlob = new Blob([code], { type: 'text/plain' })
      const codeUrl = URL.createObjectURL(codeBlob)
      const b = document.createElement('a')
      b.href = codeUrl
      b.download = `${capstone.title.replace(/[^a-z0-9]/gi, '-')}-Main.java`
      b.click()
      URL.revokeObjectURL(codeUrl)
    }
  }

  if (!user) return null

  const earned = badges.filter(b => b.earned)
  const filteredBadges = badgeCategoryFilter === 'ALL' ? badges : badges.filter(b => b.category === badgeCategoryFilter)
  const earnedFiltered = filteredBadges.filter(b => b.earned)
  const availableFiltered = filteredBadges.filter(b => !b.earned)

  const filteredNotes = notes.filter(n =>
    !noteSearch || n.title.toLowerCase().includes(noteSearch.toLowerCase()) || n.content.toLowerCase().includes(noteSearch.toLowerCase())
  )

  const tabs: { id: Tab; label: string }[] = [
    { id: 'overview', label: 'Overview' },
    { id: 'topics', label: 'Topics' },
    { id: 'badges', label: `Badges${earned.length ? ` (${earned.length})` : ''}` },
    { id: 'rabbit-holes', label: `Rabbit Holes${rabbitHoles.length ? ` (${rabbitHoles.length})` : ''}` },
    { id: 'notes', label: `Notes${notes.length ? ` (${notes.length})` : ''}` },
    { id: 'projects', label: `Projects${capstones.length ? ` (${capstones.length})` : ''}` },
    { id: 'reports', label: `Reports${reports.length ? ` (${reports.length})` : ''}` },
    { id: 'subscription', label: 'Subscription' },
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

        {/* Tab bar — horizontally scrollable on mobile */}
        <div className="flex mb-6 border-b border-border pb-0 overflow-x-auto scrollbar-none">
          {tabs.map(t => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={cn(
                'px-3 py-2.5 text-[12px] font-cinzel tracking-wide border-b-2 transition-[border-color,color] duration-150 -mb-px whitespace-nowrap flex-shrink-0',
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
            <div className="grid grid-cols-3 gap-3 max-[480px]:grid-cols-2 max-[360px]:grid-cols-1">
              {[
                { label: 'Topics & Progress', glyph: '📚', tab: 'topics' as Tab },
                { label: 'Badges', glyph: '🏅', tab: 'badges' as Tab },
                { label: 'Rabbit Holes', glyph: '🐇', tab: 'rabbit-holes' as Tab },
                { label: 'Notes', glyph: '📝', tab: 'notes' as Tab },
                { label: 'Projects', glyph: '🏗️', tab: 'projects' as Tab },
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
          <div className="flex flex-col gap-4">
            {dashLoading && <p className="text-muted italic text-center py-8">Loading topic data…</p>}
            {!dashLoading && ACTIVE_TOPICS.map(topic => {
              const dash = allTopicDash[topic.id]
              if (!dash) return null
              return (
                <TopicCard
                  key={topic.id}
                  topicId={topic.id}
                  glyph={topic.glyph}
                  name={topic.name}
                  tier={dash.currentPath}
                  diagnosticCompleted={dash.diagnosticCompleted}
                  completedSubChunks={dash.chunkHealth.reduce((s, c) => s + c.completedSubChunks, 0)}
                  totalSubChunks={dash.chunkHealth.reduce((s, c) => s + c.totalSubChunks, 0)}
                  totalXp={dash.totalXp}
                  onContinue={() => navigate(`/topic/${topic.id}`)}
                  onRetakeDiagnostic={() => navigate(`/topic/${topic.id}/diagnostic`)}
                />
              )
            })}
            {!dashLoading && ACTIVE_TOPICS.every(t => !allTopicDash[t.id]) && (
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

        {/* Tab: Notes */}
        {tab === 'notes' && (
          <div>
            {notesLoading && <p className="text-muted italic text-center py-8">Loading notes…</p>}
            {!notesLoading && (
              <>
                {notes.length > 0 && (
                  <div className="mb-4">
                    <input
                      type="text"
                      placeholder="Search notes…"
                      value={noteSearch}
                      onChange={e => setNoteSearch(e.target.value)}
                      className="w-full bg-card border border-border rounded-[10px] px-4 py-2 text-[13px] text-text placeholder:text-muted outline-none focus:border-purple-dim transition-[border-color] duration-150"
                    />
                  </div>
                )}
                {filteredNotes.length === 0 && (
                  <div className="text-center py-12">
                    <div className="text-[48px] mb-4">📝</div>
                    <p className="text-muted text-[14px] leading-[1.7] max-w-[360px] mx-auto">
                      {noteSearch
                        ? 'No notes match your search.'
                        : 'No notes yet. Notes you take during lessons will appear here.'}
                    </p>
                  </div>
                )}
                {filteredNotes.length > 0 && (
                  <div className="flex flex-col gap-3">
                    {filteredNotes.map(note => (
                      <div key={note.id} className="bg-card border border-border rounded-[12px] px-5 py-4">
                        <div className="flex items-start justify-between gap-3 mb-2">
                          <div className="font-cinzel text-[13px] text-gold leading-snug">{note.title}</div>
                          <button
                            onClick={() => deleteNote(note.id)}
                            disabled={deletingNoteId === note.id}
                            className="flex-shrink-0 p-1.5 rounded-[6px] text-muted hover:text-red hover:bg-red/10 transition-colors duration-150 disabled:opacity-40"
                          >
                            <Trash2 size={13} />
                          </button>
                        </div>
                        <p className="text-[12px] text-muted leading-[1.6] whitespace-pre-wrap line-clamp-4">{note.content}</p>
                        <div className="text-[10px] text-muted mt-2">
                          {new Date(note.updatedAt).toLocaleDateString()}
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </>
            )}
          </div>
        )}

        {/* Tab: Projects (Capstones) */}
        {tab === 'projects' && (
          <div>
            {capstonesLoading && <p className="text-muted italic text-center py-8">Loading projects…</p>}
            {!capstonesLoading && capstones.length === 0 && (
              <div className="text-center py-12">
                <div className="text-[48px] mb-4">🏗️</div>
                <p className="text-muted text-[14px] leading-[1.7] max-w-[360px] mx-auto">
                  No saved projects yet. Complete a capstone lesson to save your project here.
                </p>
              </div>
            )}
            {!capstonesLoading && capstones.length > 0 && (
              <div className="flex flex-col gap-4">
                {capstones.map(capstone => (
                  <div key={capstone.id} className="bg-card border border-border rounded-[12px] px-5 py-5">
                    <div className="flex items-start justify-between gap-3 mb-2">
                      <div className="font-cinzel text-[15px] text-gold">{capstone.title}</div>
                      <button
                        onClick={() => deleteCapstone(capstone.id)}
                        className="flex-shrink-0 p-1.5 rounded-[6px] text-muted hover:text-red hover:bg-red/10 transition-colors duration-150"
                      >
                        <Trash2 size={13} />
                      </button>
                    </div>
                    {capstone.description && (
                      <p className="text-[12px] text-muted leading-[1.6] mb-3">{capstone.description}</p>
                    )}
                    <div className="flex flex-wrap gap-2 mb-3">
                      {capstone.githubUrl && (
                        <a
                          href={capstone.githubUrl}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[11px] bg-card border border-border text-muted hover:border-purple-dim hover:text-text transition-colors duration-150"
                        >
                          <ExternalLink size={10} /> GitHub
                        </a>
                      )}
                      {capstone.codeContent && (
                        <button
                          onClick={() => downloadCapstoneZip(capstone)}
                          className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[11px] bg-[rgba(255,193,7,0.08)] border border-[rgba(255,193,7,0.2)] text-gold hover:border-gold transition-colors duration-150"
                        >
                          <Download size={10} /> Download ZIP
                        </button>
                      )}
                    </div>
                    {capstone.adminFeedback && (
                      <div className="mt-2 p-3 rounded-[8px] bg-purple-dim border border-purple text-[12px] text-purple-light leading-[1.6]">
                        <span className="font-cinzel text-[10px] text-purple uppercase tracking-[0.08em] block mb-1">Instructor Feedback</span>
                        {capstone.adminFeedback}
                      </div>
                    )}
                    <div className="text-[10px] text-muted mt-2">
                      Saved {new Date(capstone.createdAt).toLocaleDateString()}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* Tab: Reports (Stuck Reports history) */}
        {tab === 'reports' && (
          <div>
            {reportsLoading && <p className="text-muted italic text-center py-8">Loading reports…</p>}
            {!reportsLoading && reports.length === 0 && (
              <div className="text-center py-12">
                <div className="text-[48px] mb-4">🚩</div>
                <p className="text-muted text-[14px] leading-[1.7] max-w-[360px] mx-auto">
                  No reports yet. Use the "I'm stuck" button during a lesson to get help from the team.
                </p>
              </div>
            )}
            {!reportsLoading && reports.length > 0 && (
              <div className="flex flex-col gap-3">
                {reports.map(report => (
                  <ReportCard key={report.id} report={report} />
                ))}
              </div>
            )}
          </div>
        )}

        {/* Tab: Subscription */}
        {tab === 'subscription' && (
          <div className="flex flex-col gap-4">
            {showUpgradeFromProfile && (
              <UpgradeModal onClose={() => setShowUpgradeFromProfile(false)} />
            )}

            {subLoading ? (
              <div className="text-center py-12 text-muted font-cinzel text-[13px]">Loading…</div>
            ) : subscriptionStatus ? (
              <SubscriptionPanel
                status={subscriptionStatus}
                onManage={openBillingPortal}
                onUpgrade={() => setShowUpgradeFromProfile(true)}
                portalLoading={portalLoading}
              />
            ) : null}
          </div>
        )}

        {/* Tab: Preferences */}
        {tab === 'preferences' && (
          <div className="flex flex-col gap-4">
            {/* Theme toggle */}
            <div className="bg-card border border-border rounded-[12px] px-5 py-4">
              <div className="font-cinzel text-[14px] text-gold mb-3 tracking-wide">Theme</div>
              {blizzardAvailable ? (
                <div className="flex items-center justify-between gap-4">
                  <div>
                    <div className="font-cinzel text-[13px] text-text mb-0.5">
                      {theme === 'blizzard' ? '❄ Blizzard' : '✦ Default (Dark)'}
                    </div>
                    <div className="text-[11px] text-muted leading-snug">
                      {theme === 'blizzard' ? 'Frostbound Academy — Lich-King dark fantasy.' : 'Arcane Academy — purples, golds, dark backgrounds.'}
                    </div>
                  </div>
                  <button onClick={toggleTheme} className="btn btn-ghost flex-shrink-0 text-[12px] px-4 py-1.5">
                    Switch to {theme === 'blizzard' ? 'Default' : 'Blizzard'}
                  </button>
                </div>
              ) : (
                <div className="flex items-center gap-3 text-muted text-[12px] leading-snug">
                  <span className="text-[20px]">❄</span>
                  <span>
                    <strong className="text-text">Blizzard theme is not available on mobile.</strong>
                    <br />Switch to a larger screen to unlock it.
                  </span>
                </div>
              )}
            </div>

            {/* Blizzard preferences — only show when blizzard theme active and available */}
            {blizzardAvailable && theme === 'blizzard' && (
              <>
                {/* Palette picker */}
                <div className="bg-card border border-border rounded-[12px] px-5 py-4">
                  <div className="font-cinzel text-[14px] text-gold mb-1 tracking-wide">Magic Palette</div>
                  <div className="text-[11px] text-muted mb-4">The colour of your spells, sconces &amp; UI accents.</div>
                  <div className="palette-grid">
                    {PALETTES.map(p => (
                      <div
                        key={p.id}
                        className={`palette-tile ${blizzardPrefs.palette === p.id ? 'active' : ''}`}
                        onClick={() => setBlizzardPref('palette', p.id as Palette)}
                      >
                        <div className="swatch-row">
                          {p.swatches.map((c, i) => (
                            <div key={i} className="swatch" style={{ background: c, color: c }} />
                          ))}
                        </div>
                        <div className="name">{p.name}</div>
                        {blizzardPrefs.palette === p.id && <div className="check">✓</div>}
                      </div>
                    ))}
                  </div>
                </div>

                {/* Scene, Font, Snow row */}
                <div className="bg-card border border-border rounded-[12px] px-5 py-4 flex flex-col gap-4">
                  <div className="font-cinzel text-[14px] text-gold tracking-wide">Atmosphere</div>

                  {/* Scene */}
                  <div className="flex items-center justify-between">
                    <div>
                      <div className="font-cinzel text-[12px] text-text tracking-wide">Scene</div>
                      <div className="text-[11px] text-muted">What lies beyond the frame.</div>
                    </div>
                    <div className="seg">
                      <button className={blizzardPrefs.scene === 'castle' ? 'on' : ''} onClick={() => setBlizzardPref('scene', 'castle')}>Citadel</button>
                      <button className={blizzardPrefs.scene === 'void'   ? 'on' : ''} onClick={() => setBlizzardPref('scene', 'void')}>Void</button>
                      <button className={blizzardPrefs.scene === 'aurora' ? 'on' : ''} onClick={() => setBlizzardPref('scene', 'aurora')}>Aurora</button>
                    </div>
                  </div>

                  {/* Font */}
                  <div className="flex items-center justify-between">
                    <div>
                      <div className="font-cinzel text-[12px] text-text tracking-wide">Heading Font</div>
                      <div className="text-[11px] text-muted">Cinzel — engraved. Rune — gothic blackletter.</div>
                    </div>
                    <div className="seg">
                      <button className={blizzardPrefs.font === 'cinzel' ? 'on' : ''} onClick={() => setBlizzardPref('font', 'cinzel')}>Cinzel</button>
                      <button className={blizzardPrefs.font === 'rune'   ? 'on' : ''} onClick={() => setBlizzardPref('font', 'rune')}>Rune</button>
                    </div>
                  </div>

                  {/* Snow toggle */}
                  <div className="flex items-center justify-between">
                    <div>
                      <div className="font-cinzel text-[12px] text-text tracking-wide">Falling Snow</div>
                      <div className="text-[11px] text-muted">Particles drifting across the screen.</div>
                    </div>
                    <div
                      className={`switch ${blizzardPrefs.snow ? 'on' : ''}`}
                      onClick={() => setBlizzardPref('snow', !blizzardPrefs.snow)}
                    />
                  </div>
                </div>
              </>
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
    APPRENTICE: 'Apprentice', JUNIOR: 'Junior', SENIOR: 'Senior', LEAD: 'Lead',
    // Legacy fallbacks during migration
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

// ── Subscription panel ────────────────────────────────────────────────────────

const PLAN_META: Record<string, { icon: React.ElementType; color: string; label: string; description: string }> = {
  FREE:      { icon: Zap,      color: 'var(--muted)',       label: 'Free Plan',     description: 'One discipline included.' },
  MONTHLY:   { icon: Zap,      color: 'var(--teal)',        label: 'Monthly',       description: 'Billed monthly. Cancel any time.' },
  ANNUAL:    { icon: Crown,    color: 'var(--gold)',        label: 'Annual',        description: 'Billed annually. ~40% saving.' },
  LIFETIME:  { icon: Infinity, color: 'var(--purple-light)', label: 'Lifetime',    description: 'One-time purchase. Never expires.' },
  CANCELLED: { icon: Zap,      color: '#f87171',            label: 'Cancelled',    description: 'Access continues until period end.' },
}

function SubscriptionPanel({
  status,
  onManage,
  onUpgrade,
  portalLoading,
}: {
  status: SubscriptionStatusResponse
  onManage: () => void
  onUpgrade: () => void
  portalLoading: boolean
}) {
  const meta = PLAN_META[status.status] ?? PLAN_META.FREE
  const Icon = meta.icon

  const periodEndDate = status.periodEnd
    ? new Date(status.periodEnd).toLocaleDateString('en-GB', { day: 'numeric', month: 'long', year: 'numeric' })
    : null

  return (
    <div className="flex flex-col gap-4">
      {/* Current plan card */}
      <div className="bg-card border border-border rounded-[14px] p-6">
        <div className="flex items-center gap-4 mb-4">
          <div
            className="w-11 h-11 rounded-[12px] flex items-center justify-center flex-shrink-0"
            style={{ background: `color-mix(in srgb, ${meta.color} 15%, transparent)` }}
          >
            <Icon size={20} strokeWidth={1.75} color={meta.color} />
          </div>
          <div className="flex-1">
            <div className="flex items-center gap-2">
              <span className="font-cinzel text-[17px] font-bold text-text">{meta.label}</span>
              {status.active && (
                <span className="flex items-center gap-1 text-[11px] text-teal font-cinzel">
                  <CheckCircle size={11} strokeWidth={2.5} />
                  Active
                </span>
              )}
            </div>
            <p className="text-[12px] text-muted mt-0.5">{meta.description}</p>
          </div>
        </div>

        {/* Period end */}
        {periodEndDate && (
          <div
            className="mb-4 px-4 py-3 rounded-[10px] border text-[13px]"
            style={{
              borderColor: status.status === 'CANCELLED' ? 'rgba(248,113,113,0.25)' : 'rgba(201,162,39,0.25)',
              background:  status.status === 'CANCELLED' ? 'rgba(248,113,113,0.05)' : 'rgba(201,162,39,0.05)',
              color: status.status === 'CANCELLED' ? '#f87171' : 'var(--gold)',
            }}
          >
            {status.status === 'CANCELLED'
              ? `Access ends ${periodEndDate}`
              : `Next renewal: ${periodEndDate}`}
          </div>
        )}

        {/* CTAs */}
        <div className="flex gap-3 flex-wrap">
          {status.status === 'FREE' && (
            <button
              onClick={onUpgrade}
              className="px-5 py-2.5 rounded-[9px] font-cinzel text-[13px] font-semibold cursor-pointer
                transition-[background] duration-150"
              style={{
                background: 'linear-gradient(135deg, var(--gold), color-mix(in srgb, var(--gold) 70%, var(--purple-light)))',
                color: '#1a1a2e',
              }}
            >
              Upgrade to Premium
            </button>
          )}

          {(status.status === 'MONTHLY' || status.status === 'ANNUAL') && status.hasStripeCustomer && (
            <button
              onClick={onManage}
              disabled={portalLoading}
              className="flex items-center gap-2 px-5 py-2.5 rounded-[9px] font-cinzel text-[13px]
                border border-border text-muted hover:border-gold hover:text-gold
                transition-[border-color,color] duration-150 disabled:opacity-50 cursor-pointer"
            >
              <CreditCard size={14} strokeWidth={1.75} />
              {portalLoading ? 'Opening…' : 'Manage Subscription'}
            </button>
          )}

          {status.status === 'CANCELLED' && status.hasStripeCustomer && (
            <>
              <button
                onClick={onUpgrade}
                className="px-5 py-2.5 rounded-[9px] font-cinzel text-[13px] font-semibold cursor-pointer
                  transition-[background] duration-150"
                style={{
                  background: 'linear-gradient(135deg, var(--gold), color-mix(in srgb, var(--gold) 70%, var(--purple-light)))',
                  color: '#1a1a2e',
                }}
              >
                Re-subscribe
              </button>
              <button
                onClick={onManage}
                disabled={portalLoading}
                className="flex items-center gap-2 px-5 py-2.5 rounded-[9px] font-cinzel text-[13px]
                  border border-border text-muted hover:border-muted
                  transition-[border-color,color] duration-150 disabled:opacity-50 cursor-pointer"
              >
                <CreditCard size={14} strokeWidth={1.75} />
                {portalLoading ? 'Opening…' : 'Billing History'}
              </button>
            </>
          )}

          {status.status === 'LIFETIME' && (
            <div className="px-4 py-2 rounded-[9px] border border-[rgba(196,181,253,0.3)]
              bg-[rgba(196,181,253,0.06)] font-cinzel text-[12px] text-purple-light">
              ✦ Lifetime member — no further action needed
            </div>
          )}
        </div>
      </div>

      {/* What's included */}
      <div className="bg-card border border-border rounded-[14px] p-5">
        <div className="font-cinzel text-[12px] tracking-[0.1em] text-muted uppercase mb-3">What's Included</div>
        <ul className="flex flex-col gap-2">
          {[
            ['✦', 'Your first discipline — always free, all tiers'],
            ['🔓', status.active ? 'All disciplines unlocked' : 'Additional disciplines (subscription required)'],
            ['📚', 'Spaced-repetition review system'],
            ['🏆', 'Badges, XP, ranks & leaderboards'],
            ['🧠', 'AI mentor feedback on written practice'],
          ].map(([icon, text]) => (
            <li key={text} className="flex items-center gap-3 text-[13px]">
              <span className="text-[14px] flex-shrink-0">{icon}</span>
              <span className={cn(
                'leading-[1.5]',
                text.includes('subscription required') && !status.active
                  ? 'text-muted'
                  : 'text-text',
              )}>{text}</span>
            </li>
          ))}
        </ul>
      </div>
    </div>
  )
}

const REPORT_STATUS_META: Record<string, { label: string; color: string; bg: string; border: string }> = {
  NEW:      { label: 'Submitted', color: '#f87171', bg: 'rgba(248,113,113,.08)',  border: 'rgba(248,113,113,.25)' },
  REVIEWED: { label: 'In Review', color: '#c9a227', bg: 'rgba(201,162,39,.08)', border: 'rgba(201,162,39,.25)' },
  RESOLVED: { label: 'Resolved',  color: '#4ade80', bg: 'rgba(74,222,128,.06)', border: 'rgba(74,222,128,.2)' },
}

function ReportCard({ report }: { report: MyStuckReport }) {
  const meta = REPORT_STATUS_META[report.status] ?? REPORT_STATUS_META.NEW
  return (
    <div className="bg-card border border-border rounded-[12px] px-5 py-4">
      <div className="flex items-start justify-between gap-3 mb-2">
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 flex-wrap">
            <span
              className="text-[10px] font-cinzel px-2 py-0.5 rounded"
              style={{ background: meta.bg, color: meta.color, border: `1px solid ${meta.border}` }}
            >
              {meta.label}
            </span>
            {report.currentPhase && (
              <span className="text-[10px] text-muted font-cinzel uppercase tracking-wide">{report.currentPhase}</span>
            )}
            <span className="text-[10px] text-muted">{new Date(report.createdAt).toLocaleDateString()}</span>
          </div>
        </div>
      </div>
      {report.subChunkId && (
        <div className="text-[11px] text-muted mb-1.5">Lesson: {report.subChunkId}</div>
      )}
      {report.userMessage && (
        <p className="text-[13px] text-text leading-[1.6] mb-2">{report.userMessage}</p>
      )}
      {report.adminNotes && (
        <div className="mt-2 p-3 rounded-[8px] bg-purple-dim border border-purple">
          <div className="text-[10px] font-cinzel text-purple uppercase tracking-[0.08em] mb-1">Team Response</div>
          <p className="text-[12px] text-purple-light leading-[1.6]">{report.adminNotes}</p>
        </div>
      )}
    </div>
  )
}
