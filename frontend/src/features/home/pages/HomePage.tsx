import { useNavigate } from 'react-router-dom'
import { useState, useEffect, useRef } from 'react'
import { useAuth } from '@/shared/hooks/useAuth'
import { useTopicsDashboard, useReviewsDue } from '@/hooks/queries'
import { TopicIcon } from '@/components/icons/TopicIcon'
import { Badge } from '@/components/ui/badge'
import { Lock, Flame, BookOpen, Swords, Trophy, ArrowRight, RotateCcw, LifeBuoy, ChevronDown, Zap } from 'lucide-react'
import { cn } from '@/lib/utils'
import { ACTIVE_TOPICS, ACTIVE_TOPIC_IDS, COMING_SOON_TOPICS, type Topic } from '@/features/topics/data/topics'

/* ── Types ───────────────────────────────────────────────────────────────── */

interface TopicData {
  progress: number
  diagnosticCompleted: boolean
  diagnosticCompletedAt: string | null
  totalChunks: number
  completedChunks: number
  totalLessons: number
}

/* ── Helpers ─────────────────────────────────────────────────────────────── */

function diagnosticExpired(completedAt: string | null): boolean {
  if (!completedAt) return false
  return (Date.now() - new Date(completedAt).getTime()) / 86_400_000 >= 30
}

/* ── How it works ────────────────────────────────────────────────────────── */

const HOW_IT_WORKS = [
  {
    icon: BookOpen,
    color: 'var(--gold)',
    bg: 'rgba(201,162,39,0.08)',
    border: 'rgba(201,162,39,0.2)',
    step: '01',
    title: 'Enrol',
    desc: 'Choose one discipline that calls to you. Every great scholar starts with a single path.',
  },
  {
    icon: Flame,
    color: 'var(--teal)',
    bg: 'rgba(45,212,191,0.08)',
    border: 'rgba(45,212,191,0.2)',
    step: '02',
    title: 'Learn',
    desc: 'Structured lessons with spaced repetition. Science-backed encoding that makes knowledge stick.',
  },
  {
    icon: Swords,
    color: 'var(--purple-light)',
    bg: 'rgba(196,181,253,0.08)',
    border: 'rgba(196,181,253,0.2)',
    step: '03',
    title: 'Battle',
    desc: 'Face boss challenges at each tier. One wrong answer sends you back — mastery is earned, not given.',
  },
  {
    icon: RotateCcw,
    color: '#60a5fa',
    bg: 'rgba(96,165,250,0.08)',
    border: 'rgba(96,165,250,0.2)',
    step: '04',
    title: 'Review',
    desc: 'The academy schedules spaced-repetition reviews at the exact moment before you forget. Revisit your Review queue daily to lock in long-term memory.',
  },
  {
    icon: Trophy,
    color: 'var(--gold)',
    bg: 'rgba(201,162,39,0.08)',
    border: 'rgba(201,162,39,0.2)',
    step: '05',
    title: 'Rise',
    desc: 'Earn XP and ranks from Novice to Lord Magus. Build deep expertise that lasts a lifetime.',
  },
  {
    icon: LifeBuoy,
    color: '#f87171',
    bg: 'rgba(248,113,113,0.08)',
    border: 'rgba(248,113,113,0.2)',
    step: '✦',
    title: 'I\'m Stuck',
    desc: 'Hit a wall? Tap "I\'m stuck" at any point during a lesson. The academy flags it and can offer a re-explanation, a different angle, or a hint — no scholar is left behind.',
  },
]

/* ── Enrolled topic card (large, full-width) ─────────────────────────────── */

function EnrolledCard({
  topic, data, onClick,
}: {
  topic: Topic
  data: TopicData
  onClick: () => void
}) {
  const completedPct  = data.progress
  const modulesDone   = data.completedChunks
  const modulesTotal  = data.totalChunks
  const lessonsTotal  = data.totalLessons

  return (
    <div
      onClick={onClick}
      className="group cursor-pointer rounded-[14px] border border-border bg-card
        transition-[border-color,transform,box-shadow] duration-200
        hover:-translate-y-[2px] hover:shadow-[0_8px_32px_rgba(0,0,0,0.35)]"
      style={{
        borderTopColor: `color-mix(in srgb, ${topic.accentStroke} 70%, transparent)`,
        borderTopWidth: 2,
      }}
      onMouseEnter={e => (e.currentTarget.style.borderColor = `color-mix(in srgb, ${topic.accentStroke} 50%, transparent)`)}
      onMouseLeave={e => {
        e.currentTarget.style.borderColor = 'var(--border)'
        e.currentTarget.style.borderTopColor = `color-mix(in srgb, ${topic.accentStroke} 70%, transparent)`
      }}
    >
      <div className="px-6 py-5 flex items-center gap-5 max-[560px]:flex-col max-[560px]:items-start max-[560px]:gap-3">

        {/* Icon */}
        <div className="flex-shrink-0">
          <TopicIcon topicId={topic.id} size={44} />
        </div>

        {/* Info */}
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 mb-0.5">
            <span className="font-cinzel text-[20px] font-bold text-text max-[480px]:text-[17px]">
              {topic.name}
            </span>
            <Badge variant="active">Active</Badge>
          </div>
          <p className="text-[13px] text-muted leading-[1.5] mb-3 line-clamp-1">{topic.tagline}</p>

          {/* Progress bar */}
          <div className="flex items-center gap-3">
            <div className="flex-1 h-[6px] bg-border rounded-full overflow-hidden max-w-[260px]">
              <div
                className="h-full rounded-full transition-[width] duration-700"
                style={{
                  width: `${completedPct}%`,
                  background: `linear-gradient(90deg, ${topic.accentStroke}, color-mix(in srgb, ${topic.accentStroke} 60%, var(--purple-light)))`,
                }}
              />
            </div>
            <span className="font-cinzel text-[11px] text-muted whitespace-nowrap">
              {completedPct}%
            </span>
          </div>

          <div className="mt-1.5 font-cinzel text-[11px] text-muted">
            {modulesTotal > 0
              ? <>{modulesDone} / {modulesTotal} modules · {lessonsTotal} lessons</>
              : <>{topic.chunks} modules</>}
          </div>
        </div>

        {/* CTA */}
        <div className="flex-shrink-0 max-[560px]:w-full">
          <div
            className="flex items-center gap-2 px-5 py-2.5 rounded-[9px] font-cinzel text-[13px] font-semibold
              border transition-[background,border-color] duration-150
              group-hover:border-[var(--teal)] group-hover:bg-[rgba(45,212,191,0.08)]
              max-[560px]:justify-center"
            style={{ borderColor: 'rgba(45,212,191,0.3)', color: 'var(--teal)' }}
          >
            Continue Learning
            <ArrowRight size={14} strokeWidth={2} />
          </div>
        </div>
      </div>
    </div>
  )
}

/* ── Small topic card (unenrolled / locked / coming-soon) ────────────────── */

function TopicCard({
  topic,
  isLocked,
  canEnrol,
  onClick,
}: {
  topic: Topic
  isLocked: boolean
  canEnrol: boolean
  onClick: () => void
}) {
  const active    = topic.status === 'active'
  const clickable = active && !isLocked

  return (
    <div
      onClick={clickable ? onClick : undefined}
      className={cn(
        'rounded-[12px] border border-border bg-card px-4 py-4 flex flex-col gap-2',
        'transition-[border-color,transform,box-shadow] duration-200',
        clickable
          ? 'cursor-pointer hover:-translate-y-[2px] hover:shadow-[0_6px_24px_rgba(0,0,0,0.3)]'
          : isLocked
          ? 'cursor-pointer opacity-60'
          : 'cursor-default opacity-50 saturate-50',
      )}
      style={clickable ? {
        borderTopColor: `color-mix(in srgb, ${topic.accentStroke} 55%, transparent)`,
        borderTopWidth: 2,
      } : undefined}
      onMouseEnter={e => { if (clickable) (e.currentTarget as HTMLDivElement).style.borderColor = `color-mix(in srgb, ${topic.accentStroke} 45%, transparent)` }}
      onMouseLeave={e => {
        if (clickable) {
          const el = e.currentTarget as HTMLDivElement
          el.style.borderColor = 'var(--border)'
          el.style.borderTopColor = `color-mix(in srgb, ${topic.accentStroke} 55%, transparent)`
        }
      }}
    >
      <div className="flex items-start justify-between">
        <TopicIcon topicId={topic.id} size={28} />
        <Badge variant={!active ? 'soon' : isLocked ? 'locked' : 'active'}>
          {!active ? 'Coming Soon' : isLocked ? '🔒 Premium' : 'Active'}
        </Badge>
      </div>

      <div className="font-cinzel text-[15px] font-bold text-text">{topic.name}</div>
      <div className="text-[12px] text-muted leading-[1.55] flex-1 line-clamp-2">{topic.tagline}</div>

      <div className="flex items-center justify-between pt-2 border-t border-border mt-auto">
        <span className="font-cinzel text-[10px] text-muted">{topic.chunks} modules</span>
        {active && (
          <span className={cn(
            'text-[12px] font-semibold flex items-center gap-1',
            isLocked ? 'text-gold opacity-70' : canEnrol ? 'text-gold' : 'text-muted',
          )}>
            {isLocked
              ? <><Lock size={11} strokeWidth={2} /> Unlock</>
              : canEnrol
              ? 'Enrol →'
              : null}
          </span>
        )}
      </div>
    </div>
  )
}

/* ── Daily habit nudge bar ───────────────────────────────────────────────── */

function HabitNudgeBar({
  reviewsDue,
  streakDays,
  streakAtRisk,
  onReview,
  onContinue,
}: {
  reviewsDue: number
  streakDays: number
  streakAtRisk: boolean
  onReview: () => void
  onContinue: () => void
}) {
  const hasReviews  = reviewsDue > 0
  const streakAlive = streakDays >= 1

  // Choose the primary nudge: reviews trump streak which trumps plain continue
  if (hasReviews) {
    return (
      <div className="mb-8 flex items-center gap-3 px-4 py-3.5 rounded-[12px]
        border border-[rgba(96,165,250,0.25)] bg-[rgba(96,165,250,0.05)]
        flex-wrap"
      >
        <RotateCcw size={16} strokeWidth={1.75} className="flex-shrink-0 text-[#60a5fa]" />
        <p className="flex-1 text-[13px] text-muted leading-[1.5] m-0 min-w-0">
          <span className="font-semibold text-text">
            {reviewsDue} {reviewsDue === 1 ? 'concept' : 'concepts'} due for review.
          </span>{' '}
          Revisit them now before the memory fades.
        </p>
        <button
          onClick={onReview}
          className="flex-shrink-0 flex items-center gap-1.5 px-4 py-2 rounded-[8px]
            border border-[rgba(96,165,250,0.4)] bg-[rgba(96,165,250,0.1)]
            font-cinzel text-[12px] font-semibold text-[#60a5fa]
            hover:bg-[rgba(96,165,250,0.18)] transition-colors duration-150"
        >
          Review now <ArrowRight size={12} strokeWidth={2} />
        </button>
      </div>
    )
  }

  if (streakAtRisk && streakAlive) {
    return (
      <div className="mb-8 flex items-center gap-3 px-4 py-3.5 rounded-[12px]
        border border-[rgba(251,146,60,0.3)] bg-[rgba(251,146,60,0.06)]
        flex-wrap"
      >
        <Flame size={16} strokeWidth={1.75} className="flex-shrink-0 text-[#fb923c]" />
        <p className="flex-1 text-[13px] text-muted leading-[1.5] m-0 min-w-0">
          <span className="font-semibold text-text">
            Your {streakDays}-day streak is at risk!
          </span>{' '}
          Complete a lesson today to keep it alive.
        </p>
        <button
          onClick={onContinue}
          className="flex-shrink-0 flex items-center gap-1.5 px-4 py-2 rounded-[8px]
            border border-[rgba(251,146,60,0.35)] bg-[rgba(251,146,60,0.08)]
            font-cinzel text-[12px] font-semibold text-[#fb923c]
            hover:bg-[rgba(251,146,60,0.16)] transition-colors duration-150"
        >
          Keep streak <Flame size={12} strokeWidth={2} />
        </button>
      </div>
    )
  }

  if (streakAlive) {
    return (
      <div className="mb-8 flex items-center gap-3 px-4 py-3.5 rounded-[12px]
        border border-[rgba(45,212,191,0.2)] bg-[rgba(45,212,191,0.04)]
        flex-wrap"
      >
        <Zap size={16} strokeWidth={1.75} className="flex-shrink-0 text-teal" />
        <p className="flex-1 text-[13px] text-muted leading-[1.5] m-0 min-w-0">
          <span className="font-semibold text-text">
            {streakDays}-day streak — keep the momentum going.
          </span>{' '}
          {streakDays >= 7
            ? 'A week of daily study. Knowledge compounds.'
            : 'One lesson a day builds mastery faster than you think.'}
        </p>
        <button
          onClick={onContinue}
          className="flex-shrink-0 flex items-center gap-1.5 px-4 py-2 rounded-[8px]
            border border-[rgba(45,212,191,0.3)] bg-[rgba(45,212,191,0.07)]
            font-cinzel text-[12px] font-semibold text-teal
            hover:bg-[rgba(45,212,191,0.13)] transition-colors duration-150"
        >
          Next lesson <ArrowRight size={12} strokeWidth={2} />
        </button>
      </div>
    )
  }

  // No streak yet — simple next lesson CTA
  return (
    <div className="mb-8 flex items-center gap-3 px-4 py-3.5 rounded-[12px]
      border border-[rgba(45,212,191,0.2)] bg-[rgba(45,212,191,0.04)]
      flex-wrap"
    >
      <BookOpen size={16} strokeWidth={1.75} className="flex-shrink-0 text-teal" />
      <p className="flex-1 text-[13px] text-muted leading-[1.5] m-0 min-w-0">
        <span className="font-semibold text-text">Ready to continue?</span>{' '}
        Pick up where you left off and build your daily habit.
      </p>
      <button
        onClick={onContinue}
        className="flex-shrink-0 flex items-center gap-1.5 px-4 py-2 rounded-[8px]
          border border-[rgba(45,212,191,0.3)] bg-[rgba(45,212,191,0.07)]
          font-cinzel text-[12px] font-semibold text-teal
          hover:bg-[rgba(45,212,191,0.13)] transition-colors duration-150"
      >
        Continue learning <ArrowRight size={12} strokeWidth={2} />
      </button>
    </div>
  )
}

/* ── Page ────────────────────────────────────────────────────────────────── */

export default function HomePage() {
  const { user }     = useAuth()
  const navigate     = useNavigate()
  const rawData      = useTopicsDashboard(ACTIVE_TOPIC_IDS)
  const { data: reviewsDue = 0 } = useReviewsDue()

  // Normalise dashboard data
  const topicData: Record<string, TopicData> = Object.fromEntries(
    Object.entries(rawData)
      .filter(([, d]) => d != null)
      .map(([id, d]) => [id, {
        progress:         Math.round(d!.overallProgress * 100),
        diagnosticCompleted: d!.diagnosticCompleted,
        diagnosticCompletedAt: d!.diagnosticCompletedAt ?? null,
        totalChunks:      d!.chunkHealth.length,
        completedChunks:  d!.chunkHealth.filter(ch => ch.status === 'COMPLETE').length,
        totalLessons:     d!.chunkHealth.reduce((s, ch) => s + ch.totalSubChunks, 0),
      }])
  )

  // Enrolled = has any progress or completed the diagnostic
  const enrolledTopics = ACTIVE_TOPICS.filter(t => {
    const d = topicData[t.id]
    return d && (d.progress > 0 || d.diagnosticCompleted)
  })
  const hasEnrollments   = enrolledTopics.length > 0
  const canBypassPaywall = user?.role === 'ADMIN' || user?.bypassPaywall === true

  // Unenrolled active topics
  const unenrolledActive = ACTIVE_TOPICS.filter(t => !enrolledTopics.find(e => e.id === t.id))

  function handleTopicClick(topic: Topic) {
    const data = topicData[topic.id]
    const needsOnboarding = !data || !data.diagnosticCompleted || diagnosticExpired(data.diagnosticCompletedAt)
    navigate(needsOnboarding ? `/topic/${topic.id}/onboarding` : `/topic/${topic.id}`)
  }

  // Greeting personalisation
  const firstName = user?.username?.split(/[^a-zA-Z]/)[0] ?? 'Scholar'
  const greeting  = hasEnrollments ? `Welcome back, ${firstName}` : `Welcome to the Academy, ${firstName}`

  /* ── Render ─────────────────────────────────────────────────────────────── */
  return (
    <div className="max-w-[860px] mx-auto px-5 py-8 pb-20 overflow-y-auto max-[600px]:px-4 max-[600px]:py-6">

      {/* ── Hero ───────────────────────────────────────────────────────────── */}
      <div className="mb-10">
        <p className="font-cinzel text-[12px] tracking-[0.2em] text-muted mb-2">
          {hasEnrollments ? 'YOUR ACADEMY' : 'THE POLYMATH\'S PATH'}
        </p>
        <h1 className="font-cinzel text-[clamp(22px,5vw,34px)] font-bold m-0 mb-3"
          style={{
            background: 'linear-gradient(135deg, var(--gold) 0%, var(--purple-light) 100%)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
          }}>
          {greeting}
        </h1>

        {/* Stats row */}
        {user && (
          <div className="flex items-center gap-3 flex-wrap">
            {/* Streak */}
            <div className={cn(
              'flex items-center gap-1.5 px-2.5 py-1 rounded-md border font-cinzel text-[11px]',
              (user.streakDays ?? 0) >= 3
                ? 'border-[#fb923c44] bg-[#fb923c11] text-orange'
                : 'border-border text-muted opacity-60',
            )}>
              <Flame size={12} strokeWidth={1.75}
                color={(user.streakDays ?? 0) >= 3 ? '#fb923c' : 'var(--muted)'} />
              {user.streakDays ?? 0}-day streak
            </div>

            {/* XP */}
            <div className="flex items-center gap-1.5 px-2.5 py-1 rounded-md border border-border
              font-cinzel text-[11px] text-muted">
              ✦ {user.totalXp.toLocaleString()} XP
            </div>

            {/* Rank */}
            <div className="flex items-center gap-1.5 px-2.5 py-1 rounded-md border border-purple
              bg-purple-dim font-cinzel text-[11px] text-purple-light">
              {user.rank}
            </div>
          </div>
        )}
      </div>

      {/* ── Daily habit nudge — enrolled users only ─────────────────────────── */}
      {hasEnrollments && (
        <HabitNudgeBar
          reviewsDue={reviewsDue}
          streakDays={user?.streakDays ?? 0}
          streakAtRisk={enrolledTopics.some(t => rawData[t.id]?.streakAtRisk)}
          onReview={() => navigate('/reviews/daily')}
          onContinue={() => {
            const first = enrolledTopics[0]
            if (first) handleTopicClick(first)
          }}
        />
      )}

      {/* ── How it works — first-time only ─────────────────────────────────── */}
      {!hasEnrollments && (
        <section className="mb-12">
          <SectionHeading>How the Academy Works</SectionHeading>
          <div className="grid grid-cols-2 gap-3 max-[480px]:grid-cols-1"
            style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(190px, 1fr))' }}>
            {HOW_IT_WORKS.map(item => (
              <div key={item.step}
                className="rounded-[12px] border px-4 py-4 flex gap-3 items-start"
                style={{ borderColor: item.border, background: item.bg }}>
                <div className="flex-shrink-0 w-8 h-8 rounded-[8px] flex items-center justify-center"
                  style={{ background: `color-mix(in srgb, ${item.color} 15%, transparent)` }}>
                  <item.icon size={16} strokeWidth={1.75} color={item.color} />
                </div>
                <div>
                  <div className="font-cinzel text-[10px] tracking-[0.15em] mb-0.5"
                    style={{ color: item.color }}>
                    {item.step} · {item.title.toUpperCase()}
                  </div>
                  <p className="text-[12px] text-muted leading-[1.6] m-0">{item.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* ── Enrolled topics ─────────────────────────────────────────────────── */}
      {hasEnrollments && (
        <section className="mb-10">
          <SectionHeading>Continue Your Journey</SectionHeading>
          <div className="flex flex-col gap-3">
            {enrolledTopics.map(topic => {
              const data = topicData[topic.id]
              if (!data) return null
              return (
                <EnrolledCard
                  key={topic.id}
                  topic={topic}
                  data={data}
                  onClick={() => handleTopicClick(topic)}
                />
              )
            })}
          </div>
        </section>
      )}

      {/* ── Active unenrolled / paywall ─────────────────────────────────────── */}
      {(unenrolledActive.length > 0 || (hasEnrollments && !canBypassPaywall)) && (
        <section className="mb-10">
          <SectionHeading>
            {hasEnrollments ? 'More Disciplines' : 'Choose Your First Discipline'}
          </SectionHeading>

          {/* When user has enrollment and can't bypass: paywall callout */}
          {hasEnrollments && !canBypassPaywall && unenrolledActive.length > 0 && (
            <div className="mb-4 flex items-start gap-3 px-4 py-3.5 rounded-[10px]
              border border-[rgba(201,162,39,0.25)] bg-[rgba(201,162,39,0.05)]">
              <span className="text-gold text-[16px] flex-shrink-0 mt-0.5">🔒</span>
              <p className="text-[13px] text-muted leading-[1.6] m-0">
                Additional disciplines unlock with a premium subscription — coming soon.
                Master your current path first.
              </p>
            </div>
          )}

          <div className="grid gap-3"
            style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))' }}>
            {unenrolledActive.map(topic => {
              const isLocked = hasEnrollments && !canBypassPaywall
              return (
                <TopicCard
                  key={topic.id}
                  topic={topic}
                  isLocked={isLocked}
                  canEnrol={!isLocked}
                  onClick={() => handleTopicClick(topic)}
                />
              )
            })}
          </div>
        </section>
      )}

      {/* ── Coming soon ─────────────────────────────────────────────────────── */}
      <section>
        <SectionHeading>On the Horizon</SectionHeading>
        <div className="grid gap-3"
          style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))' }}>
          {COMING_SOON_TOPICS.slice(0, 6).map(topic => (
            <TopicCard
              key={topic.id}
              topic={topic}
              isLocked={false}
              canEnrol={false}
              onClick={() => {}}
            />
          ))}
        </div>
        {COMING_SOON_TOPICS.length > 6 && (
          <p className="mt-3 text-center font-cinzel text-[11px] text-muted tracking-[0.1em]">
            +{COMING_SOON_TOPICS.length - 6} more disciplines in development
          </p>
        )}
      </section>

      <ScrollHint />

    </div>
  )
}

/* ── Scroll hint ─────────────────────────────────────────────────────────── */

/**
 * Floats a subtle "more below" indicator over the bottom of the viewport.
 * Finds its own scroll container by walking up the DOM, then hides itself
 * once the user has scrolled down ~80px or the page fits in the viewport.
 */
function ScrollHint() {
  const [visible, setVisible]   = useState(false)
  const [leaving, setLeaving]   = useState(false)
  const sentinelRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    // Walk up to find the nearest scrollable ancestor
    let el: HTMLElement | null = sentinelRef.current?.parentElement ?? null
    while (el) {
      const { overflowY } = window.getComputedStyle(el)
      if ((overflowY === 'auto' || overflowY === 'scroll') && el !== document.body) break
      el = el.parentElement
    }
    const scroller = el ?? document.documentElement

    const checkAndShow = () => {
      if (scroller.scrollHeight - scroller.scrollTop > scroller.clientHeight + 100) {
        setVisible(true)
      }
    }

    // Slight delay so the page has settled before we measure
    const timer = setTimeout(checkAndShow, 700)

    const onScroll = () => {
      if (scroller.scrollTop > 80) {
        setLeaving(true)
        setTimeout(() => setVisible(false), 380)
        scroller.removeEventListener('scroll', onScroll)
      }
    }
    scroller.addEventListener('scroll', onScroll, { passive: true })

    return () => {
      clearTimeout(timer)
      scroller.removeEventListener('scroll', onScroll)
    }
  }, [])

  return (
    <>
      <div ref={sentinelRef} />
      <div
        className="pointer-events-none fixed bottom-0 left-0 right-0 flex flex-col items-center justify-end pb-6 pt-20"
        style={{
          background: 'linear-gradient(to bottom, transparent 0%, rgba(14,12,26,0.7) 50%, rgba(14,12,26,0.96) 100%)',
          opacity: visible && !leaving ? 1 : 0,
          transition: 'opacity 0.5s ease',
          zIndex: 10,
        }}
      >
        <span
          className="font-cinzel text-[9px] tracking-[0.3em] mb-2"
          style={{ color: 'var(--gold)', opacity: 0.55 }}
        >
          MORE AWAITS BELOW
        </span>
        <ChevronDown
          size={18} strokeWidth={1.5}
          className="animate-bounce"
          style={{ color: 'var(--gold)', opacity: 0.45 }}
        />
      </div>
    </>
  )
}

/* ── Section heading ─────────────────────────────────────────────────────── */

function SectionHeading({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex items-center gap-3 mb-4">
      <h2 className="font-cinzel text-[13px] font-semibold tracking-[0.15em] text-gold m-0 whitespace-nowrap">
        {children}
      </h2>
      <div className="flex-1 h-px bg-border" />
    </div>
  )
}
