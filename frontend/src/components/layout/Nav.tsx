import { useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'
import { useReviewsDue } from '@/hooks/queries'
import { cn } from '@/lib/utils'
import {
  Flame, Library, RotateCcw, Trophy, User, LogOut,
  Settings, FlaskConical,
} from 'lucide-react'

export default function Nav() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const { data: reviewsDue = 0 } = useReviewsDue()

  if (!user) return null

  const RANK_FLOORS = [0, 800, 2000, 4000, 6500, 8000, 11000]
  const rankIdx   = RANK_FLOORS.reduce((acc, floor, i) => user.totalXp >= floor ? i : acc, 0)
  const isMaxRank = rankIdx === RANK_FLOORS.length - 1
  const floor     = RANK_FLOORS[rankIdx]
  const ceiling   = isMaxRank ? null : RANK_FLOORS[rankIdx + 1]
  const xpInRank  = user.totalXp - floor
  const xpForRank = ceiling !== null ? ceiling - floor : xpInRank || 1
  const xpPct     = ceiling !== null ? Math.min(100, (xpInRank / xpForRank) * 100) : 100
  const level     = rankIdx + 1
  const streak    = user.streakDays ?? 0
  const streakHot = streak >= 3

  const NAV_ITEMS = [
    { label: 'Schools',  Icon: Library,   path: '/schools',    tutorialId: 'nav-domains' },
    { label: 'Review',   Icon: RotateCcw, path: '/review',     tutorialId: 'nav-review',
      badge: reviewsDue > 0 ? reviewsDue : null },
    { label: 'Ranks',    Icon: Trophy,    path: '/leaderboard', tutorialId: undefined },
    { label: 'Profile',  Icon: User,      path: '/profile',    tutorialId: undefined },
  ]

  return (
    <nav className="bg-surface border-b border-border px-4 max-[480px]:px-2 flex items-center justify-between h-[50px] flex-shrink-0 z-10">
      {/* Brand */}
      <div
        className="font-cinzel text-[15px] max-[480px]:text-[13px] max-[480px]:tracking-[1px] text-gold tracking-[2px] cursor-pointer select-none shrink-0"
        onClick={() => navigate('/')}
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

        {/* XP bar */}
        <div className="flex items-center gap-2 max-[600px]:hidden">
          <span className="font-cinzel text-[11px] text-muted">Lv.{level}</span>
          <div className="w-[90px] h-[5px] bg-border rounded-full overflow-hidden max-[768px]:w-14">
            <div
              className="h-full rounded-full transition-[width] duration-600"
              style={{ width: `${xpPct}%`, background: 'linear-gradient(90deg, var(--purple), var(--teal))' }}
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

        <div className="flex items-center gap-1 max-[480px]:gap-0.5">
          {/* Admin shortcut */}
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

          {/* Nav buttons */}
          {NAV_ITEMS.map(({ label, Icon, path, tutorialId, badge }) => {
            const active = location.pathname === path || location.pathname.startsWith(path + '/')
            return (
              <button
                key={path}
                data-tutorial-id={tutorialId}
                className="btn btn-ghost px-2.5 py-1 text-[12px] flex items-center gap-1.5 relative
                           max-[480px]:border-none max-[480px]:bg-transparent max-[480px]:px-2 max-[480px]:py-[5px]"
                style={active ? { background: 'var(--purple-dim)', borderColor: 'var(--purple)' } : undefined}
                onClick={() => navigate(path)}
                title={label}
              >
                <Icon size={15} strokeWidth={1.75} />
                <span className="max-[480px]:hidden">{label}</span>
                {badge != null && (
                  <span className="absolute -top-1 -right-1 bg-purple text-white text-[9px] font-bold w-4 h-4 rounded-full flex items-center justify-center">
                    {badge}
                  </span>
                )}
              </button>
            )
          })}

          {/* Settings */}
          <button
            className="btn btn-ghost px-2.5 py-1 text-[12px] flex items-center gap-1.5
                       max-[480px]:border-none max-[480px]:bg-transparent max-[480px]:px-2 max-[480px]:py-[5px]"
            onClick={() => navigate('/settings')}
            title="Settings"
          >
            <Settings size={15} strokeWidth={1.75} />
            <span className="max-[480px]:hidden">Settings</span>
          </button>

          {/* Logout */}
          <button
            className="btn btn-ghost px-2.5 py-1 text-[12px] flex items-center gap-1.5
                       max-[480px]:border-none max-[480px]:bg-transparent max-[480px]:px-2 max-[480px]:py-[5px]"
            onClick={() => { logout(); navigate('/') }}
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
