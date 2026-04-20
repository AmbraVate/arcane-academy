import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import { dashboardApi } from '../../api/services'
import { cn } from '@/lib/utils'

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

  return (
    <nav className="bg-surface border-b border-border px-5 flex items-center justify-between h-[50px] flex-shrink-0 z-10">
      <div
        className="font-cinzel text-[15px] text-gold tracking-[2px] cursor-pointer select-none"
        onClick={() => navigate('/topics')}
      >
        ✦ Arcane Academy
      </div>

      <div className="flex items-center gap-3.5">
        {/* Streak indicator */}
        <div
          className={cn(
            'flex items-center gap-1 px-2.5 py-[3px] rounded-md border cursor-default',
            streak === 0
              ? 'opacity-40 border-border'
              : streakHot
              ? 'border-[#fb923c] bg-[#fb923c22]'
              : 'border-[#fb923c44] bg-[#fb923c11]'
          )}
          title={`${streak}-day streak`}
        >
          <span className={cn('text-[14px] leading-none', streakHot && 'animate-flame-pulse')}>🔥</span>
          <span className="font-cinzel text-[12px] text-orange font-semibold max-[480px]:hidden">{streak}</span>
        </div>

        {/* XP bar */}
        <div className="flex items-center gap-2 max-[480px]:hidden">
          <span className="font-cinzel text-[11px] text-muted max-[600px]:hidden">Lv.{level}</span>
          <div className="w-[90px] h-[5px] bg-border rounded-full overflow-hidden max-[600px]:hidden max-[768px]:w-14">
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

        <div className="bg-purple-dim border border-purple rounded px-[11px] py-[3px] text-[11px] text-purple-light font-cinzel max-[768px]:hidden">
          ⚗ {user.rank}
        </div>

        {/* Nav buttons */}
        {[
          { label: 'Topics',  icon: '📚', path: '/topics' },
          { label: 'Review',  icon: '📖', path: '/review' },
          { label: 'Profile', icon: '👤', path: '/profile' },
        ].map(({ label, icon, path }) => (
          <button
            key={path}
            className="btn btn-ghost px-3 py-1 text-[12px] max-[480px]:px-2 max-[480px]:py-[5px] max-[480px]:text-[16px] max-[480px]:border-none max-[480px]:bg-transparent"
            onClick={() => navigate(path)}
          >
            <span className="max-[480px]:hidden">{label}</span>
            <span className="hidden max-[480px]:inline">{icon}</span>
          </button>
        ))}
        <button
          className="btn btn-ghost px-3 py-1 text-[12px] max-[480px]:px-2 max-[480px]:py-[5px] max-[480px]:text-[16px] max-[480px]:border-none max-[480px]:bg-transparent"
          onClick={logout}
        >
          <span className="max-[480px]:hidden">Logout</span>
          <span className="hidden max-[480px]:inline">⏏</span>
        </button>
      </div>
    </nav>
  )
}
