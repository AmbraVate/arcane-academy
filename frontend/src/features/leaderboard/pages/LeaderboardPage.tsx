import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { leaderboardApi, type LeaderboardEntry } from '@/shared/api/services'
import { MapPin } from 'lucide-react'
import { cn } from '@/lib/utils'

/**
 * Leaderboard page — three boards (per-topic weekly, per-topic all-time, polymath).
 *
 * Privacy note: only users with publicProfileEnabled=true appear here. Users
 * who haven't opted in are not visible in any board, and the empty-state
 * messaging links them to the toggle on /profile.
 */

type Board = 'weekly' | 'all-time' | 'polymath'

const ACTIVE_TOPICS = [
  { id: 'java',     name: 'Java',         glyph: '☕' },
  { id: 'tailwind', name: 'Tailwind',     glyph: '🎨' },
  { id: 'react',    name: 'React',        glyph: '⚛ï¸' },
  { id: 'sql',      name: 'SQL',          glyph: '🗃ï¸' },
] as const

export default function LeaderboardPage() {
  const navigate = useNavigate()
  const [params, setParams] = useSearchParams()

  const initialTopic = params.get('topic') ?? 'java'
  const initialBoard = (params.get('board') as Board) ?? 'weekly'

  const [domainId, setTopicId] = useState<string>(initialTopic)
  const [board, setBoard] = useState<Board>(initialBoard)
  const [rows, setRows] = useState<LeaderboardEntry[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [locationFilter, setLocationFilter] = useState('')

  // Persist current selection in URL so refresh + share-link work.
  useEffect(() => {
    const next = new URLSearchParams()
    next.set('board', board)
    if (board !== 'polymath') next.set('topic', domainId)
    setParams(next, { replace: true })
  }, [board, domainId, setParams])

  useEffect(() => {
    setLoading(true)
    setError(null)
    const fetcher = board === 'polymath'
      ? leaderboardApi.polymath()
      : board === 'weekly'
        ? leaderboardApi.topicWeekly(domainId)
        : leaderboardApi.topicAllTime(domainId)

    fetcher
      .then(setRows)
      .catch(() => setError('Could not load the leaderboard. Try again in a moment.'))
      .finally(() => setLoading(false))
  }, [board, domainId])

  const filteredRows = useMemo(() => {
    if (!locationFilter.trim()) return rows
    const lc = locationFilter.toLowerCase()
    return rows.filter(r => r.location?.toLowerCase().includes(lc))
  }, [rows, locationFilter])

  const subtitle = useMemo(() => {
    if (board === 'polymath') return 'Top polymaths by topic breadth — tie-break on total XP'
    if (board === 'weekly') {
      const monday = currentMondayLabel()
      return `XP earned in the current ISO-week (since ${monday} UTC)`
    }
    return 'XP earned in this topic since signup'
  }, [board])

  return (
    <div className="flex-1 overflow-y-auto px-6 py-8 max-[600px]:px-3 max-[600px]:py-5">
      <div className="max-w-[860px] mx-auto">
        <header className="mb-6">
          <h1 className="font-cinzel text-[26px] text-gold tracking-[1.5px] mb-1">🏆 Leaderboards</h1>
          <p className="text-muted text-[13px]">{subtitle}</p>
        </header>

        {/* Board selector */}
        <div className="flex flex-wrap gap-2 mb-4">
          {(['weekly', 'all-time', 'polymath'] as Board[]).map(b => (
            <button
              key={b}
              onClick={() => setBoard(b)}
              className={cn(
                'px-3.5 py-1.5 rounded-[7px] text-[12px] font-cinzel tracking-wide border',
                'transition-[background,border-color] duration-150',
                board === b
                  ? 'bg-purple-dim border-purple text-purple-light'
                  : 'bg-card border-border text-muted hover:border-purple-dim'
              )}
            >
              {b === 'weekly' ? 'This Week' : b === 'all-time' ? 'All-Time' : 'âš– Polymath'}
            </button>
          ))}
        </div>

        {/* Topic selector — hidden on polymath board */}
        {board !== 'polymath' && (
          <div className="flex flex-wrap gap-2 mb-6">
            {ACTIVE_TOPICS.map(t => (
              <button
                key={t.id}
                onClick={() => setTopicId(t.id)}
                className={cn(
                  'px-3 py-1 rounded-[6px] text-[12px] border transition-[background,border-color] duration-150',
                  domainId === t.id
                    ? 'bg-card border-gold-dim text-gold'
                    : 'bg-card border-border text-muted hover:border-gold-dim'
                )}
              >
                <span className="mr-1.5">{t.glyph}</span>{t.name}
              </button>
            ))}
          </div>
        )}

        {/* Location filter */}
        {!loading && rows.length > 0 && (
          <div className="flex items-center gap-2 mb-4">
            <MapPin size={13} strokeWidth={1.75} className="text-muted flex-shrink-0" />
            <input
              type="text"
              placeholder="Filter by location…"
              value={locationFilter}
              onChange={e => setLocationFilter(e.target.value)}
              className="w-full max-w-[220px] bg-card border border-border rounded-[8px] px-3 py-1.5
                text-[12px] text-text placeholder:text-muted outline-none
                focus:border-purple-dim transition-[border-color] duration-150"
            />
            {locationFilter && (
              <button
                onClick={() => setLocationFilter('')}
                className="text-[11px] text-muted hover:text-text transition-colors duration-150"
              >
                Clear
              </button>
            )}
          </div>
        )}

        {/* Body */}
        {loading && <p className="text-muted italic text-center py-10">Loading rankings…</p>}
        {error && !loading && <p className="text-red text-center py-10">{error}</p>}
        {!loading && !error && rows.length === 0 && <EmptyState onProfile={() => navigate('/profile')} />}
        {!loading && !error && filteredRows.length === 0 && rows.length > 0 && (
          <p className="text-muted italic text-center py-10">No results for "{locationFilter}"</p>
        )}
        {!loading && !error && filteredRows.length > 0 && (
          <ol className="space-y-2">
            {filteredRows.map(row => (
              <li
                key={row.username}
                className={cn(
                  'flex items-center gap-4 px-4 py-3 rounded-[10px] border transition-[border-color,background] duration-150',
                  row.rank === 1 ? 'border-gold bg-[rgba(201,162,39,.05)]'
                    : row.rank === 2 ? 'border-[#9aa3b2] bg-card'
                    : row.rank === 3 ? 'border-[#b87333] bg-card'
                    : 'border-border bg-card'
                )}
              >
                <RankBadge rank={row.rank} />
                <button
                  className="flex-1 text-left hover:underline decoration-purple-light decoration-dotted"
                  onClick={() => navigate(`/u/${row.username}`)}
                  title={`View ${row.username}'s public profile`}
                >
                  <div className="font-cinzel text-[15px] text-text">{row.username}</div>
                  <div className="text-[11px] text-muted flex items-center gap-1 flex-wrap">
                    <span>{row.rankTitle} · 🔥 {row.streakDays}d · 🏅 {row.badgeCount}</span>
                    {row.topicCount >= 0 && <span>· {row.topicCount} topics</span>}
                    {row.location && (
                      <span className="flex items-center gap-0.5">
                        · <MapPin size={10} strokeWidth={1.75} className="inline-block" /> {row.location}
                      </span>
                    )}
                  </div>
                </button>
                <div className="text-right">
                  <div className="font-cinzel text-[18px] text-gold">
                    {row.xpEarned.toLocaleString()}<span className="text-[11px] text-muted ml-1">xp</span>
                  </div>
                  <div className="text-[10px] text-muted">{row.globalXp.toLocaleString()} global</div>
                </div>
              </li>
            ))}
          </ol>
        )}
      </div>
    </div>
  )
}

function RankBadge({ rank }: { rank: number }) {
  const medal = rank === 1 ? '🥇' : rank === 2 ? '🥈' : rank === 3 ? '🥉' : null
  return (
    <div
      className={cn(
        'w-10 h-10 flex items-center justify-center rounded-full font-cinzel text-[14px]',
        medal ? 'text-[24px]' : 'bg-border text-muted'
      )}
    >
      {medal ?? `#${rank}`}
    </div>
  )
}

function EmptyState({ onProfile }: { onProfile: () => void }) {
  return (
    <div className="bg-card border border-border rounded-[12px] px-6 py-10 text-center">
      <div className="text-[32px] mb-2">📜</div>
      <p className="text-text mb-1.5">No public learners on this board yet.</p>
      <p className="text-muted text-[13px] mb-4">
        Leaderboards only show learners who have opted in. Want to appear here?
      </p>
      <button className="btn btn-primary" onClick={onProfile}>
        Enable public profile
      </button>
    </div>
  )
}

function currentMondayLabel(): string {
  const now = new Date()
  // getDay(): 0=Sun, 1=Mon, …, 6=Sat. Distance back to Monday:
  const dayOfWeek = now.getUTCDay()
  const daysSinceMonday = (dayOfWeek + 6) % 7
  const monday = new Date(Date.UTC(now.getUTCFullYear(), now.getUTCMonth(), now.getUTCDate() - daysSinceMonday))
  return monday.toLocaleDateString(undefined, { month: 'short', day: 'numeric' })
}
