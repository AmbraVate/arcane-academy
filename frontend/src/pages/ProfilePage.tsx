import { useEffect, useState } from 'react'
import { useAuth } from '../hooks/useAuth'
import { badgeApi } from '../api/services'
import type { Badge } from '@/types'
import { cn } from '@/lib/utils'

export default function ProfilePage() {
  const { user } = useAuth()
  const [badges, setBadges] = useState<Badge[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    badgeApi.getAll().then(setBadges).finally(() => setLoading(false))
  }, [])

  if (!user) return null

  const earned = badges.filter(b => b.earned)
  const categories = ['LEARNING', 'MASTERY', 'FEYNMAN', 'PATH', 'EXPLORATION', 'XP', 'STREAK']
  const categoryLabels: Record<string, string> = {
    LEARNING: 'Learning Milestones', MASTERY: 'Mastery',
    FEYNMAN: 'Feynman Technique', PATH: 'Learning Path',
    EXPLORATION: 'Exploration', XP: 'XP Thresholds', STREAK: 'Streak Milestones',
  }

  return (
    <div className="flex-1 overflow-y-auto px-6 py-8 max-[600px]:px-3 max-[600px]:py-5">
      <div className="max-w-[800px] mx-auto">
        {/* Profile header */}
        <div className="flex items-center gap-6 p-7 bg-card border border-border rounded-[14px] mb-8 max-[600px]:flex-col max-[600px]:text-center">
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
                { label: 'Badges',   value: `${earned.length}/${badges.length}` },
              ].map(({ label, value }) => (
                <span key={label} className="flex flex-col gap-0.5">
                  <span className="text-[11px] text-muted uppercase tracking-[1px] font-cinzel">{label}</span>
                  <span className="text-[18px] text-text font-semibold">{value}</span>
                </span>
              ))}
            </div>
          </div>
        </div>

        {/* Badge sections */}
        {loading ? (
          <p className="text-center text-muted py-10 italic">Loading badges...</p>
        ) : (
          categories.map(cat => {
            const catBadges = badges.filter(b => b.category === cat)
            if (catBadges.length === 0) return null
            return (
              <section key={cat} className="mb-7">
                <h2 className="font-cinzel text-[14px] text-purple-light tracking-[1px] mb-3.5 pb-1.5 border-b border-border">
                  {categoryLabels[cat]}
                </h2>
                <div className="grid gap-3.5" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))' }}>
                  {catBadges.map(badge => (
                    <div
                      key={badge.id}
                      className={cn(
                        'bg-card border rounded-[10px] px-4 py-5 text-center transition-[border-color,transform] duration-300',
                        badge.earned
                          ? 'border-gold-dim hover:border-gold hover:-translate-y-0.5 max-[480px]:px-3 max-[480px]:py-4'
                          : 'border-border opacity-45',
                      )}
                    >
                      <div className="text-[36px] mb-2.5 max-[480px]:text-[28px]">
                        {badge.earned ? badge.glyph : '🔒'}
                      </div>
                      <div className="font-cinzel text-[13px] text-text mb-1.5">{badge.displayName}</div>
                      <div className="text-[12px] text-muted leading-[1.4]">{badge.description}</div>
                      {badge.earned && badge.earnedAt && (
                        <div className="text-[10px] text-gold mt-2 font-cinzel">
                          {new Date(badge.earnedAt).toLocaleDateString()}
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              </section>
            )
          })
        )}
      </div>
    </div>
  )
}
