import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'
import { dashboardApi } from '@/shared/api/services'
import { cn } from '@/lib/utils'
import {
  Flame, Library, RotateCcw, Trophy, User, LogOut,
  Settings, FlaskConical,
} from 'lucide-react'

export default function Nav() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const [reviewsDue, setReviewsDue] = useState(0)

  useEffect(() => {
    if (!user) return
    dashboardApi.getReviewsDue().then(setReviewsDue).catch(() => {})
  }, [user])

  if (!user) return null

  const RANK_FLOORS = [0, 800, 2000, 4000, 6500, 8000, 11000]
  const rankIdx = RANK_FLOORS.reduce((acc, floor, i) => user.totalXp >= floor ? i : acc, 0)
  const isMaxRank = rankIdx === RANK_FLOORS.length - 1
  const floor = RANK_FLOORS[rankIdx]
  const ceiling = isMaxRank ? null : RANK_FLOORS[rankIdx + 1]
  const xpInRank = user.totalXp - floor
  const xpForRank = ceiling !== null ? ceiling - floor : xpInRank || 1
  const xpPct = ceiling !== null ? Math.min(100, (xpInRank / xpForRank) * 100) : 100
  const level = rankIdx + 1
  const streak = user.streakDays ?? 0
  const streakHot = streak >= 3

  const NAV_ITEMS = [
    { label: 'Topics',    Icon: Library,     path: '/topics' },
    { label: 'Review',    Icon: RotateCcw,   path: '/review' },
    { label: 'Ranks',     Icon: Trophy,      path: '/leaderboard' },
    { label: 'Profile',   Icon: User,        path: '/profile' },
  ]

  return (
    <nav className="bg-surface border-b border-border px-4 max-[480px]:px-2 flex items-center justify-between h-[50px] flex-shrink-0 z-10">
      {/* Brand */}
      <div
        className="font-cinzel text-[15px] max-[480px]:text-[13px] max-[480px]:tracking-[1px] text-gold tracking-[2px] cursor-pointer select-none shrink-0"
        onClick={() => navigate('/topics')}
      >
        ✦ Arcane Academy
      </div>

      <div className="flex items-center gap-2 max-[480px]:gap-1 min-w-0 flex-shrink-0">
        {/* Streak */}
        <div
          className={cn(
            'flex items-center gap-1.5 px-2.5 py-[3px] rounded-md border cursor-default',
            streak === 0
              ? 'opacity-40 border-border'
              : streakHot
              ? 'border-[#fb923c] bg-[#fb923c22]'
              : 'border-[#fb923c44] bg-[#fb923c11]'
          )}
          title={`${streak}-day streak`}
        >
          <Flame
            size={14}
            className={cn(streakHot && 'animate-flame-pulse')}
            color={streak === 0 ? 'var(--muted)' : '#fb923c'}
            strokeWidth={1.75}
          />
          <span className="font-cinzel text-[12px] text-orange font-semibold max-[600px]:hidden">{streak}</span>
        </div>

        {/* XP bar — hidden below 600px (all children disappear there anyway) */}
        <div className="flex items-center gap-2 max-[600px]:hidden">
          <span className="font-cinzel text-[11px] text-muted">Lv.{level}</span>
          <div className="w-[90px] h-[5px] bg-border rounded-full overflow-hidden max-[768px]:w-14">
            <div
              className="h-full rounded-full transition-[width] duration-600"
              style={{
                width: `${xpPct}%`,
                background: 'linear-gradient(90deg, var(--purple), var(--teal))',
              }}
            />
          </div>
          <span className="font-cinzel text-[11px] text-muted max-[768px]:hidden">
            {isMaxRank ? `${user.totalXp} xp` : `${xpInRank} / ${xpForRank} xp`}
          </span>
        </div>

        {/* Rank badge */}
        <div className="flex items-center gap-1.5 bg-purple-dim border border-purple rounded px-2.5 py-[3px] max-[768px]:hidden">
          <FlaskConical size={11} color="var(--purple-light)" strokeWidth={1.75} />
          <span className="text-[11px] text-purple-light font-cinzel">{user.rank}</span>
        </div>

        {/* All action buttons share the same responsive style */}
        <div className="flex items-center gap-1 max-[480px]:gap-0.5">
          {/* Admin shortcut — gold-tinted, same collapse behaviour */}
          {user.role === 'ADMIN' && (
            <button
              className="btn btn-ghost px-2.5 py-1 text-[12px] flex items-center gap-1.5
                         max-[480px]:border-none max-[480px]:bg-transparent max-[480px]:px-2 max-[480px]:py-[5px]"
              style={{ borderColor: 'rgba(201,162,39,.3)', color: '#c9a227' }}
              onClick={() => navigate('/admin')}
              title="Admin"
            >
              <Settings size={15} strokeWidth={1.75} />
              <span className="max-[480px]:hidden">Admin</span>
            </button>
          )}

          {/* Nav buttons — icon + label on desktop, icon-only on mobile */}
          {NAV_ITEMS.map(({ label, Icon, path }) => (
            <button
              key={path}
              className="btn btn-ghost px-2.5 py-1 text-[12px] flex items-center gap-1.5
                         max-[480px]:border-none max-[480px]:bg-transparent max-[480px]:px-2 max-[480px]:py-[5px]"
              onClick={() => navigate(path)}
              title={label}
            >
              <Icon size={15} strokeWidth={1.75} />
              <span className="max-[480px]:hidden">{label}</span>
            </button>
          ))}

          {/* Logout */}
          <button
            className="btn btn-ghost px-2.5 py-1 text-[12px] flex items-center gap-1.5
                       max-[480px]:border-none max-[480px]:bg-transparent max-[480px]:px-2 max-[480px]:py-[5px]"
            onClick={logout}
            title="Logout"
          >
            <LogOut size={15} strokeWidth={1.75} />
            <span className="max-[480px]:hidden">Logout</span>
          </button>
        </div>
      </div>
    </nav>
  )
}
