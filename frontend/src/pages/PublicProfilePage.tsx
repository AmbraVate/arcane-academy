import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { profileApi, type PublicProfile } from '../api/services'
import { cn } from '@/lib/utils'

/**
 * Read-only profile view at <code>/u/:username</code>.
 *
 * Shown to anyone authenticated. Returns a friendly "not found / private" page
 * if the user hasn't opted in (the API returns 404 in both cases — they look
 * the same to the world to avoid leaking enumeration).
 */
export default function PublicProfilePage() {
  const navigate = useNavigate()
  const { username } = useParams<{ username: string }>()
  const [profile, setProfile] = useState<PublicProfile | null>(null)
  const [loading, setLoading] = useState(true)
  const [notFound, setNotFound] = useState(false)

  useEffect(() => {
    if (!username) return
    setLoading(true)
    profileApi.getPublic(username)
      .then(data => {
        if (data) setProfile(data)
        else setNotFound(true)
      })
      .catch(() => setNotFound(true))
      .finally(() => setLoading(false))
  }, [username])

  if (loading) {
    return <p className="text-muted italic text-center py-10">Loading profile…</p>
  }

  if (notFound || !profile) {
    return (
      <div className="flex-1 overflow-y-auto px-6 py-8">
        <div className="max-w-[600px] mx-auto bg-card border border-border rounded-[12px] px-6 py-10 text-center">
          <div className="text-[32px] mb-3">🌫️</div>
          <h1 className="font-cinzel text-[20px] text-gold mb-2">Profile not visible</h1>
          <p className="text-muted text-[13px] mb-4">
            Either no learner with that name exists, or they haven't opted into public profiles.
          </p>
          <button className="btn btn-ghost" onClick={() => navigate('/leaderboard')}>
            Back to leaderboards
          </button>
        </div>
      </div>
    )
  }

  const memberSinceLabel = new Date(profile.memberSince).toLocaleDateString(undefined, {
    month: 'long', year: 'numeric',
  })

  return (
    <div className="flex-1 overflow-y-auto px-6 py-8 max-[600px]:px-3 max-[600px]:py-5">
      <div className="max-w-[800px] mx-auto">

        {/* Hero — matches the shape of /profile but stripped of editable bits */}
        <div className="flex items-center gap-6 p-7 bg-card border border-border rounded-[14px] mb-6 max-[600px]:flex-col max-[600px]:text-center">
          <div className="text-[56px] w-20 h-20 flex items-center justify-center bg-purple-dim border-2 border-purple rounded-full flex-shrink-0">
            {profile.rank === 'Archmage' ? '🧙' : '✨'}
          </div>
          <div className="flex-1">
            <h1 className="font-cinzel text-[24px] text-gold mb-1 max-[480px]:text-[20px]">
              {profile.username}
            </h1>
            <p className="text-[12px] text-muted mb-3">Member since {memberSinceLabel}</p>
            <div className="flex gap-6 flex-wrap max-[600px]:justify-center max-[480px]:gap-4">
              {[
                { label: 'Rank',   value: profile.rank },
                { label: 'XP',     value: profile.totalXp.toLocaleString() },
                { label: 'Streak', value: `${profile.streakDays}d` },
                { label: 'Badges', value: String(profile.badges.length) },
              ].map(({ label, value }) => (
                <span key={label} className="flex flex-col gap-0.5">
                  <span className="text-[11px] text-muted uppercase tracking-[1px] font-cinzel">{label}</span>
                  <span className="text-[18px] text-text font-semibold">{value}</span>
                </span>
              ))}
            </div>
          </div>
        </div>

        {/* Per-topic breakdown */}
        <section className="mb-6">
          <h2 className="font-cinzel text-[14px] text-purple-light tracking-[1px] mb-3 pb-1.5 border-b border-border">
            Topics
          </h2>
          {profile.topics.length === 0 ? (
            <p className="text-muted italic">No topic XP yet — they're just getting started.</p>
          ) : (
            <div className="grid gap-3" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))' }}>
              {profile.topics.map(topic => (
                <div
                  key={topic.topicId}
                  className="bg-card border border-border rounded-[10px] px-4 py-4"
                  style={{ borderLeft: `3px solid ${topic.accentColor ?? 'var(--purple)'}` }}
                >
                  <div className="flex items-center gap-2 mb-1.5">
                    <span className="text-[18px]">{topic.glyph}</span>
                    <span className="font-cinzel text-[14px] text-text">{topic.name}</span>
                  </div>
                  <div className="text-[18px] font-semibold text-gold">
                    {topic.xpEarned.toLocaleString()}<span className="text-[11px] text-muted ml-1">xp</span>
                  </div>
                  <div className="text-[11px] text-muted">{topic.subChunksCompleted} sub-chunks complete</div>
                </div>
              ))}
            </div>
          )}
        </section>

        {/* Badges */}
        <section>
          <h2 className="font-cinzel text-[14px] text-purple-light tracking-[1px] mb-3 pb-1.5 border-b border-border">
            Badges Earned
          </h2>
          {profile.badges.length === 0 ? (
            <p className="text-muted italic">No badges yet — the journey is just beginning.</p>
          ) : (
            <div className="grid gap-3" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(150px, 1fr))' }}>
              {profile.badges.map(badge => (
                <div
                  key={badge.id}
                  className={cn(
                    'bg-card border border-gold-dim rounded-[10px] px-4 py-4 text-center transition-[border-color,transform] duration-300',
                    'hover:border-gold hover:-translate-y-0.5'
                  )}
                  title={badge.category}
                >
                  <div className="text-[28px] mb-1.5">{badge.glyph}</div>
                  <div className="font-cinzel text-[12px] text-text leading-tight">{badge.displayName}</div>
                  <div className="text-[10px] text-gold mt-1.5 font-cinzel">
                    {new Date(badge.earnedAt).toLocaleDateString(undefined, { month: 'short', year: 'numeric' })}
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>
      </div>
    </div>
  )
}
