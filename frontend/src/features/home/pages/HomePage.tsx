import {useNavigate, useSearchParams} from 'react-router-dom'
import {useState, useEffect, useRef} from 'react'
import {useAuth} from '@/shared/hooks/useAuth'
import {useTutorial} from '@/features/tutorial/context/TutorialContext'
import {useDomainsDashboard} from '@/hooks/queries'
import {DomainIcon} from '@/components/icons/DomainIcon'
import {Badge} from '@/components/ui/badge'
import {
  Lock,
  Flame,
  BookOpen,
  Swords,
  Trophy,
  ArrowRight,
  RotateCcw,
  LifeBuoy,
  ChevronDown,
  CheckCircle,
  XCircle
} from 'lucide-react'
import {cn} from '@/lib/utils'
import {
  ACTIVE_DOMAINS,
  ACTIVE_DOMAIN_IDS,
  COMING_SOON_DOMAINS,
  type Domain
} from '@/features/domains/data/domains'
import {hasActiveSubscription} from '@/shared/types'
import {UpgradeModal} from '@/features/payment/components/UpgradeModal'

interface TopicData {
  progress: number
  totalChunks: number
  completedChunks: number
  totalLessons: number
}


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


function EnrolledCard({
                        topic, data, onClick,
                      }: {
  topic: Domain
  data: TopicData
  onClick: () => void
}) {
  const completedPct = data.progress
  const modulesDone = data.completedChunks
  const modulesTotal = data.totalChunks
  const lessonsTotal = data.totalLessons

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
        <div
            className="px-6 py-5 flex items-center gap-5 max-[560px]:flex-col max-[560px]:items-start max-[560px]:gap-3">

          {/* Icon */}
          <div className="flex-shrink-0">
            <DomainIcon domainId={topic.id} size={44}/>
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
                  : <>{topic.modules} modules</>}
            </div>
          </div>

          {/* CTA */}
          <div className="flex-shrink-0 max-[560px]:w-full">
            <div
                className="flex items-center gap-2 px-5 py-2.5 rounded-[9px] font-cinzel text-[13px] font-semibold
              border transition-[background,border-color] duration-150
              group-hover:border-[var(--teal)] group-hover:bg-[rgba(45,212,191,0.08)]
              max-[560px]:justify-center"
                style={{borderColor: 'rgba(45,212,191,0.3)', color: 'var(--teal)'}}
            >
              Continue Learning
              <ArrowRight size={14} strokeWidth={2}/>
            </div>
          </div>
        </div>
      </div>
  )
}


function TopicCard({
                     topic,
                     isLocked,
                     canEnrol,
                     onClick,
                   }: {
  topic: Domain
  isLocked: boolean
  canEnrol: boolean
  onClick: () => void
}) {
  const active = topic.status === 'active'
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
          onMouseEnter={e => {
            if (clickable) (e.currentTarget as HTMLDivElement).style.borderColor = `color-mix(in srgb, ${topic.accentStroke} 45%, transparent)`
          }}
          onMouseLeave={e => {
            if (clickable) {
              const el = e.currentTarget as HTMLDivElement
              el.style.borderColor = 'var(--border)'
              el.style.borderTopColor = `color-mix(in srgb, ${topic.accentStroke} 55%, transparent)`
            }
          }}
      >
        <div className="flex items-start justify-between">
          <DomainIcon domainId={topic.id} size={28}/>
          <Badge variant={!active ? 'soon' : isLocked ? 'locked' : 'active'}>
            {!active ? 'Coming Soon' : isLocked ? '🔒 Premium' : 'Active'}
          </Badge>
        </div>

        <div className="font-cinzel text-[15px] font-bold text-text">{topic.name}</div>
        <div
            className="text-[12px] text-muted leading-[1.55] flex-1 line-clamp-2">{topic.tagline}</div>

        <div className="flex items-center justify-between pt-2 border-t border-border mt-auto">
          <span className="font-cinzel text-[10px] text-muted">{topic.modules} modules</span>
          {active && (
              <span className={cn(
                  'text-[12px] font-semibold flex items-center gap-1',
                  isLocked ? 'text-gold opacity-70' : canEnrol ? 'text-gold' : 'text-muted',
              )}>
            {isLocked
                ? <><Lock size={11} strokeWidth={2}/> Unlock</>
                : canEnrol
                    ? 'Enrol →'
                    : null}
          </span>
          )}
        </div>
      </div>
  )
}

export default function HomePage() {
  const {user} = useAuth()
  const navigate = useNavigate()
  const [searchParams, setSearchParams] = useSearchParams()
  const rawData = useDomainsDashboard(ACTIVE_DOMAIN_IDS)
  const { maybeStart } = useTutorial()

  const [showUpgrade, setShowUpgrade] = useState(false)
  const [paymentBanner, setPaymentBanner] = useState<'success' | 'cancelled' | null>(null)

  // Auto-start tutorial for first-time users (after a short paint delay).
  useEffect(() => {
    if (user) {
      const t = setTimeout(() => maybeStart(), 600)
      return () => clearTimeout(t)
    }
  }, [user, maybeStart])

  // Handle return from Stripe Checkout
  useEffect(() => {
    const payment = searchParams.get('payment')
    if (payment === 'success' || payment === 'cancelled') {
      setPaymentBanner(payment)
      setSearchParams({}, {replace: true})
      const timer = setTimeout(() => setPaymentBanner(null), 6000)
      return () => clearTimeout(timer)
    }
  }, [searchParams, setSearchParams])

  // Normalise dashboard data
  const topicData: Record<string, TopicData> = Object.fromEntries(
      Object.entries(rawData)
      .filter(([, d]) => d != null)
      .map(([id, d]) => [id, {
        progress: Math.round(d!.overallProgress * 100),
        totalChunks: d!.moduleHealth.length,
        completedChunks: d!.moduleHealth.filter(ch => ch.status === 'COMPLETE').length,
        totalLessons: d!.moduleHealth.reduce((s, ch) => s + ch.totalLessons, 0),
      }])
  )

  // Enrolled = has any progress
  const enrolledTopics = ACTIVE_DOMAINS.filter(t => {
    const d = topicData[t.id]
    return d && d.progress > 0
  })
  const hasEnrollments = enrolledTopics.length > 0
  const canUnlock = hasActiveSubscription(user)

  // Unenrolled active topics
  const unenrolledActive = ACTIVE_DOMAINS.filter(t => !enrolledTopics.find(e => e.id === t.id))

  function handleTopicClick(topic: Domain) {
    navigate(`/pathway/${topic.id}`)
  }

  function handleLockedTopicClick() {
    setShowUpgrade(true)
  }

  // Greeting personalisation
  const firstName = user?.username?.split(/[^a-zA-Z]/)[0] ?? 'Scholar'
  const greeting = hasEnrollments ? `Welcome back, ${firstName}` : `Welcome to the Academy, ${firstName}`

  /* ── Render ─────────────────────────────────────────────────────────────── */
  return (
      <div
          className="max-w-[860px] mx-auto px-5 py-8 pb-20 overflow-y-auto max-[600px]:px-4 max-[600px]:py-6">

        {/* Upgrade modal */}
        {showUpgrade && <UpgradeModal onClose={() => setShowUpgrade(false)}/>}

        {/* Payment return banner */}
        {paymentBanner && (
            <div
                className={cn(
                    'mb-6 flex items-center gap-3 px-4 py-3 rounded-[10px] border text-[13px]',
                    paymentBanner === 'success'
                        ? 'border-teal/30 bg-teal/5 text-teal'
                        : 'border-border bg-card text-muted',
                )}
            >
              {paymentBanner === 'success'
                  ? <CheckCircle size={16} strokeWidth={2}/>
                  : <XCircle size={16} strokeWidth={2}/>}
              {paymentBanner === 'success'
                  ? 'Payment successful — your subscription is now active. Welcome to the full Academy!'
                  : 'Checkout cancelled. Your subscription has not changed.'}
            </div>
        )}

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
                         color={(user.streakDays ?? 0) >= 3 ? '#fb923c' : 'var(--muted)'}/>
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

        {/* ── How it works — first-time only ─────────────────────────────────── */}
        {!hasEnrollments && (
            <section className="mb-12">
              <SectionHeading>How the Academy Works</SectionHeading>
              <div className="grid grid-cols-2 gap-3 max-[480px]:grid-cols-1"
                   style={{gridTemplateColumns: 'repeat(auto-fit, minmax(190px, 1fr))'}}>
                {HOW_IT_WORKS.map(item => (
                    <div key={item.step}
                         className="rounded-[12px] border px-4 py-4 flex gap-3 items-start"
                         style={{borderColor: item.border, background: item.bg}}>
                      <div
                          className="flex-shrink-0 w-8 h-8 rounded-[8px] flex items-center justify-center"
                          style={{background: `color-mix(in srgb, ${item.color} 15%, transparent)`}}>
                        <item.icon size={16} strokeWidth={1.75} color={item.color}/>
                      </div>
                      <div>
                        <div className="font-cinzel text-[10px] tracking-[0.15em] mb-0.5"
                             style={{color: item.color}}>
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
        {(unenrolledActive.length > 0 || hasEnrollments) && (
            <section className="mb-10">
              <SectionHeading>
                {hasEnrollments ? 'More Disciplines' : 'Choose Your First Discipline'}
              </SectionHeading>

              {/* When user has enrollments and no subscription: upgrade prompt */}
              {hasEnrollments && !canUnlock && unenrolledActive.length > 0 && (
                  <div
                      onClick={() => setShowUpgrade(true)}
                      className="mb-4 flex items-center gap-3 px-4 py-3.5 rounded-[10px]
                border border-[rgba(201,162,39,0.35)] bg-[rgba(201,162,39,0.05)]
                cursor-pointer hover:bg-[rgba(201,162,39,0.09)] transition-colors"
                  >
                    <span className="text-gold text-[18px] flex-shrink-0">🔒</span>
                    <div className="flex-1 min-w-0">
                      <p className="text-[13px] text-text leading-[1.5] m-0 font-semibold">
                        Unlock all disciplines with a subscription
                      </p>
                      <p className="text-[12px] text-muted leading-[1.5] m-0">
                        Monthly from £6.99 · Annual from £49.99 · Lifetime £99
                      </p>
                    </div>
                    <span className="text-[12px] font-cinzel text-gold whitespace-nowrap">
                View plans →
              </span>
                  </div>
              )}

              <div className="grid gap-3"
                   style={{gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))'}}>
                {unenrolledActive.map(topic => {
                  const isLocked = hasEnrollments && !canUnlock
                  return (
                      <TopicCard
                          key={topic.id}
                          topic={topic}
                          isLocked={isLocked}
                          canEnrol={!isLocked}
                          onClick={isLocked ? handleLockedTopicClick : () => handleTopicClick(topic)}
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
               style={{gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))'}}>
            {COMING_SOON_DOMAINS.slice(0, 6).map(topic => (
                <TopicCard
                    key={topic.id}
                    topic={topic}
                    isLocked={false}
                    canEnrol={false}
                    onClick={() => {
                    }}
                />
            ))}
          </div>
          {COMING_SOON_DOMAINS.length > 6 && (
              <p className="mt-3 text-center font-cinzel text-[11px] text-muted tracking-[0.1em]">
                +{COMING_SOON_DOMAINS.length - 6} more disciplines in development
              </p>
          )}
        </section>

        <ScrollHint/>

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
  const [visible, setVisible] = useState(false)
  const [leaving, setLeaving] = useState(false)
  const sentinelRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    // Walk up to find the nearest scrollable ancestor
    let el: HTMLElement | null = sentinelRef.current?.parentElement ?? null
    while (el) {
      const {overflowY} = window.getComputedStyle(el)
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
    scroller.addEventListener('scroll', onScroll, {passive: true})

    return () => {
      clearTimeout(timer)
      scroller.removeEventListener('scroll', onScroll)
    }
  }, [])

  return (
      <>
        <div ref={sentinelRef}/>
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
            style={{color: 'var(--gold)', opacity: 0.55}}
        >
          MORE AWAITS BELOW
        </span>
          <ChevronDown
              size={18} strokeWidth={1.5}
              className="animate-bounce"
              style={{color: 'var(--gold)', opacity: 0.45}}
          />
        </div>
      </>
  )
}

/* ── Section heading ─────────────────────────────────────────────────────── */

function SectionHeading({children}: { children: React.ReactNode }) {
  return (
      <div className="flex items-center gap-3 mb-4">
        <h2 className="font-cinzel text-[13px] font-semibold tracking-[0.15em] text-gold m-0 whitespace-nowrap">
          {children}
        </h2>
        <div className="flex-1 h-px bg-border"/>
      </div>
  )
}
