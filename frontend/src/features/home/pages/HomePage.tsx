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
  XCircle,
  GraduationCap,
  Sparkles,
} from 'lucide-react'
import {cn} from '@/lib/utils'
import {
  ACTIVE_DOMAIN_IDS,
  DOMAINS,
  SCHOOL_META,
  SCHOOL_HERO_IMAGES,
  type Domain,
  type School,
} from '@/features/domains/data/domains'
import {hasActiveSubscription} from '@/shared/types'
import {UpgradeModal} from '@/features/payment/components/UpgradeModal'

/* ── Types ───────────────────────────────────────────────────────────────── */

interface TopicData {
  progress: number
  totalChunks: number
  completedChunks: number
  totalLessons: number
}

/* ── Static data ─────────────────────────────────────────────────────────── */

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
    icon: GraduationCap,
    color: 'var(--purple-light)',
    bg: 'rgba(196,181,253,0.08)',
    border: 'rgba(196,181,253,0.2)',
    step: '03',
    title: 'Prove It',
    desc: 'Each lesson runs you through guided practice, solo recall, and a retrieval quiz - structured phases that turn reading into lasting knowledge.',
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
    step: '*',
    title: "I'm Stuck",
    desc: 'Hit a wall? Tap "I\'m stuck" at any point during a lesson. The academy flags it and can offer a re-explanation, a different angle, or a hint - no scholar is left behind.',
  },
]

/* ── School card data (derived from DOMAINS) ────────────────────────────── */

function buildSchoolCards() {
  const order: School[] = [
    'computing-engineering',
    'mathematics-logic',
    'natural-sciences',
    'mind-neuroscience',
    'history-civilisation',
  ]

  return order.map(schoolId => {
    const meta  = SCHOOL_META[schoolId]
    const paths = DOMAINS.filter(d => d.school === schoolId)
    const hasActive = paths.some(d => d.status === 'active')
    return {schoolId, meta, paths, hasActive}
  })
}

const SCHOOL_CARDS = buildSchoolCards()

/* ── Sub-components ──────────────────────────────────────────────────────── */

function EnrolledCard({
  topic, data, onClick,
}: {
  topic: Domain
  data: TopicData
  onClick: () => void
}) {
  const pct           = data.progress
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
        <div className="flex-shrink-0">
          <DomainIcon domainId={topic.id} size={44}/>
        </div>

        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 mb-0.5">
            <span className="font-cinzel text-[20px] font-bold text-text max-[480px]:text-[17px]">
              {topic.name}
            </span>
            <Badge variant="active">Active</Badge>
          </div>
          <p className="text-[13px] text-muted leading-[1.5] mb-3 line-clamp-1">{topic.tagline}</p>

          <div className="flex items-center gap-3">
            <div className="flex-1 h-[6px] bg-border rounded-full overflow-hidden max-w-[260px]">
              <div
                className="h-full rounded-full transition-[width] duration-700"
                style={{
                  width: `${pct}%`,
                  background: `linear-gradient(90deg, ${topic.accentStroke}, color-mix(in srgb, ${topic.accentStroke} 60%, var(--purple-light)))`,
                }}
              />
            </div>
            <span className="font-cinzel text-[11px] text-muted whitespace-nowrap">{pct}%</span>
          </div>

          <div className="mt-1.5 font-cinzel text-[11px] text-muted">
            {modulesTotal > 0
              ? <>{modulesDone} / {modulesTotal} modules / {lessonsTotal} lessons</>
              : <>{topic.modules} modules</>}
          </div>
        </div>

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

function SchoolCard({
  schoolId,
  meta,
  paths,
  hasActive,
  onClick,
}: {
  schoolId: School
  meta: typeof SCHOOL_META[School]
  paths: Domain[]
  hasActive: boolean
  onClick: () => void
}) {
  const activePaths = paths.filter(d => d.status === 'active')
  const soonCount   = paths.filter(d => d.status !== 'active').length

  if (SCHOOL_HERO_IMAGES[schoolId]) {
    return (
      <div
        onClick={hasActive ? onClick : undefined}
        className={cn(
          'group relative overflow-hidden rounded-[14px] border border-border bg-black min-h-[160px] flex flex-col',
          'transition-[border-color,transform,box-shadow] duration-200',
          hasActive
            ? 'cursor-pointer hover:-translate-y-[2px] hover:shadow-[0_8px_40px_rgba(124,58,237,0.35)]'
            : 'opacity-55 cursor-default',
        )}
        style={hasActive ? { borderTopColor: `color-mix(in srgb, ${meta.color} 65%, transparent)`, borderTopWidth: 2 } : undefined}
        onMouseEnter={e => {
          if (hasActive) (e.currentTarget as HTMLDivElement).style.borderColor = `color-mix(in srgb, ${meta.color} 55%, transparent)`
        }}
        onMouseLeave={e => {
          if (hasActive) {
            const el = e.currentTarget as HTMLDivElement
            el.style.borderColor = 'var(--border)'
            el.style.borderTopColor = `color-mix(in srgb, ${meta.color} 65%, transparent)`
          }
        }}
      >
        <img src={SCHOOL_HERO_IMAGES[schoolId]} alt="" aria-hidden="true"
          className="absolute inset-0 w-full h-full object-cover z-0 pointer-events-none
            transition-[filter,transform] duration-500 group-hover:blur-sm group-hover:scale-105" />
        <div className="absolute inset-0 z-0 pointer-events-none transition-colors duration-500
          bg-[rgba(8,6,18,0.40)] group-hover:bg-[rgba(8,6,18,0.80)]" />

        {/* Default: title only */}
        <div className="absolute inset-0 z-20 flex flex-col justify-end px-4 pb-4 pt-3
          transition-opacity duration-300 group-hover:opacity-0 pointer-events-none">
          {activePaths.length > 0 && (
            <span className="inline-block self-start font-cinzel text-[9px] font-semibold px-2 py-0.5 rounded-full mb-2"
              style={{ color: '#fff', background: 'rgba(255,255,255,0.18)', border: '1px solid rgba(255,255,255,0.35)' }}>
              {activePaths.length} active
            </span>
          )}
          <h3 className="font-cinzel text-[16px] font-bold text-white m-0 leading-tight
            [text-shadow:0_0_30px_rgba(124,58,237,0.6),0_2px_8px_rgba(0,0,0,0.9)]">
            {meta.name}
          </h3>
        </div>

        {/* Hover: full content */}
        <div className="relative z-10 flex flex-col gap-2.5 px-4 py-4 pb-3 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex-1">
          <div className="flex items-start justify-between gap-2">
            <span className="font-cinzel text-[13px] font-bold text-white leading-snug">{meta.name}</span>
            {activePaths.length > 0 && (
              <span className="flex-shrink-0 text-[9px] font-cinzel px-2 py-0.5 rounded-full"
                style={{ color: '#fff', background: 'rgba(255,255,255,0.15)', border: '1px solid rgba(255,255,255,0.3)' }}>
                {activePaths.length} active
              </span>
            )}
          </div>
          <p className="text-[11px] text-white/75 leading-[1.5] m-0 flex-1">{meta.description}</p>
          <div className="flex flex-wrap gap-1.5">
            {activePaths.map(d => (
              <span key={d.id} className="inline-flex items-center px-2 py-0.5 rounded-full text-[10px] font-cinzel font-semibold tracking-wide"
                style={{ background: `color-mix(in srgb, ${meta.color} 20%, transparent)`, color: meta.color, border: `1px solid color-mix(in srgb, ${meta.color} 40%, transparent)` }}>
                {d.name}
              </span>
            ))}
            {soonCount > 0 && (
              <span className="inline-flex items-center px-2 py-0.5 rounded-full text-[10px] font-cinzel tracking-wide border border-white/20 text-white/50">
                +{soonCount} coming soon
              </span>
            )}
          </div>
          <div className="mt-auto pt-2.5 border-t border-white/15 flex items-center justify-between">
            <span className="font-cinzel text-[10px] text-white/50 tracking-wide">
              {paths.length} {paths.length === 1 ? 'pathway' : 'pathways'}
            </span>
            <span className="font-cinzel text-[11px] font-semibold flex items-center gap-1" style={{color: meta.color}}>
              Explore <ArrowRight size={11} strokeWidth={2.5}/>
            </span>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div
      onClick={hasActive ? onClick : undefined}
      className={cn(
        'rounded-[14px] border border-border bg-card flex flex-col',
        'transition-[border-color,transform,box-shadow] duration-200',
        hasActive
          ? 'cursor-pointer hover:-translate-y-[2px] hover:shadow-[0_8px_32px_rgba(0,0,0,0.3)]'
          : 'opacity-55 cursor-default',
      )}
      style={hasActive ? {
        borderTopColor: `color-mix(in srgb, ${meta.color} 65%, transparent)`,
        borderTopWidth: 2,
      } : undefined}
      onMouseEnter={e => {
        if (hasActive) (e.currentTarget as HTMLDivElement).style.borderColor = `color-mix(in srgb, ${meta.color} 40%, transparent)`
      }}
      onMouseLeave={e => {
        if (hasActive) {
          const el = e.currentTarget as HTMLDivElement
          el.style.borderColor = 'var(--border)'
          el.style.borderTopColor = `color-mix(in srgb, ${meta.color} 65%, transparent)`
        }
      }}
    >
      {/* Header */}
      <div className="px-4 pt-4 pb-3 flex items-start gap-3">
        <div
          className="flex-shrink-0 w-10 h-10 rounded-[10px] flex items-center justify-center text-[20px]"
          style={{background: `color-mix(in srgb, ${meta.color} 14%, transparent)`}}
        >
          {meta.glyph}
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 flex-wrap mb-0.5">
            <span className="font-cinzel text-[13px] font-bold text-text leading-snug">
              {meta.name}
            </span>
          </div>
          <p className="text-[11px] text-muted leading-[1.5] line-clamp-2">{meta.description}</p>
        </div>
      </div>

      {/* Pathway chips */}
      <div className="px-4 pb-3 flex flex-wrap gap-1.5 min-h-[28px]">
        {activePaths.map(d => (
          <span
            key={d.id}
            className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-cinzel font-semibold tracking-wide"
            style={{
              background: `color-mix(in srgb, ${meta.color} 12%, transparent)`,
              color: meta.color,
              border: `1px solid color-mix(in srgb, ${meta.color} 28%, transparent)`,
            }}
          >
            {d.glyph} {d.name}
          </span>
        ))}
        {soonCount > 0 && (
          <span className="inline-flex items-center px-2 py-0.5 rounded-full text-[10px] font-cinzel tracking-wide border border-border text-muted opacity-60">
            +{soonCount} coming soon
          </span>
        )}
      </div>

      {/* Footer */}
      <div className="mt-auto px-4 py-3 border-t border-border flex items-center justify-between">
        <span className="font-cinzel text-[10px] text-muted tracking-wide">
          {paths.length} {paths.length === 1 ? 'pathway' : 'pathways'}
        </span>
        {hasActive ? (
          <span
            className="font-cinzel text-[11px] font-semibold flex items-center gap-1"
            style={{color: meta.color}}
          >
            Explore <ArrowRight size={11} strokeWidth={2.5}/>
          </span>
        ) : (
          <span className="font-cinzel text-[10px] text-muted italic">In development</span>
        )}
      </div>
    </div>
  )
}

/* ── Page ────────────────────────────────────────────────────────────────── */

export default function HomePage() {
  const {user}    = useAuth()
  const navigate  = useNavigate()
  const [searchParams, setSearchParams] = useSearchParams()
  const rawData   = useDomainsDashboard(ACTIVE_DOMAIN_IDS)
  const {maybeStart} = useTutorial()

  const [showUpgrade,   setShowUpgrade]   = useState(false)
  const [paymentBanner, setPaymentBanner] = useState<'success' | 'cancelled' | null>(null)

  // Auto-start tutorial for first-time users.
  useEffect(() => {
    if (user) {
      const force = user.onboardingCompleted === false
      const t = setTimeout(() => maybeStart(force), 600)
      return () => clearTimeout(t)
    }
  }, [user, maybeStart])

  // Handle return from Stripe Checkout
  useEffect(() => {
    const payment = searchParams.get('payment')
    if (payment === 'success' || payment === 'cancelled') {
      setPaymentBanner(payment as 'success' | 'cancelled')
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
        progress:         Math.round(d!.overallProgress * 100),
        totalChunks:      d!.moduleHealth.length,
        completedChunks:  d!.moduleHealth.filter(ch => ch.status === 'COMPLETE').length,
        totalLessons:     d!.moduleHealth.reduce((s, ch) => s + ch.totalLessons, 0),
      }])
  )

  const enrolledDomains = DOMAINS.filter(d => {
    const data = topicData[d.id]
    return data && data.progress > 0
  })
  const hasEnrollments = enrolledDomains.length > 0
  const canUnlock      = hasActiveSubscription(user)

  const firstName = user?.username?.split(/[^a-zA-Z]/)[0] ?? 'Scholar'
  const greeting  = hasEnrollments
    ? `Welcome back, ${firstName}`
    : `Welcome to the Academy, ${firstName}`

  /* ── Render ──────────────────────────────────────────────────────────── */
  return (
    <div className="max-w-[900px] mx-auto px-5 py-8 pb-20 max-[600px]:px-4 max-[600px]:py-6">

      {showUpgrade && <UpgradeModal onClose={() => setShowUpgrade(false)}/>}

      {/* Payment return banner */}
      {paymentBanner && (
        <div className={cn(
          'mb-6 flex items-center gap-3 px-4 py-3 rounded-[10px] border text-[13px]',
          paymentBanner === 'success'
            ? 'border-teal/30 bg-teal/5 text-teal'
            : 'border-border bg-card text-muted',
        )}>
          {paymentBanner === 'success'
            ? <CheckCircle size={16} strokeWidth={2}/>
            : <XCircle size={16} strokeWidth={2}/>}
          {paymentBanner === 'success'
            ? 'Payment successful - your subscription is now active. Welcome to the full Academy!'
            : 'Checkout cancelled. Your subscription has not changed.'}
        </div>
      )}

      {/* ── Hero ─────────────────────────────────────────────────────────── */}
      <section className="mb-10">
        {/* Academy crest line */}
        <div className="flex items-center gap-3 mb-4">
          <div
            className="flex items-center gap-2 px-3 py-1 rounded-full border"
            style={{borderColor: 'rgba(201,162,39,0.3)', background: 'rgba(201,162,39,0.05)'}}
          >
            <GraduationCap size={13} strokeWidth={1.75} style={{color: 'var(--gold)'}}/>
            <span className="font-cinzel text-[10px] tracking-[0.2em]" style={{color: 'var(--gold)'}}>
              {hasEnrollments ? 'THE ARCANE ACADEMY' : 'THE POLYMATH\'S PATH'}
            </span>
          </div>
        </div>

        <h1
          className="font-cinzel text-[clamp(22px,5vw,36px)] font-bold m-0 mb-3 leading-tight"
          style={{
            background: 'linear-gradient(135deg, var(--gold) 0%, var(--purple-light) 100%)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
          }}
        >
          {greeting}
        </h1>

        {!hasEnrollments && (
          <p className="text-[14px] text-muted leading-[1.7] max-w-[520px] m-0 mb-4">
            Master multiple disciplines through structured pathways, spaced-repetition review, and
            lore-driven challenges. Choose a school and begin your ascent.
          </p>
        )}

        {/* Stats row */}
        {user && (
          <div className="flex items-center gap-3 flex-wrap">
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
            <div className="flex items-center gap-1.5 px-2.5 py-1 rounded-md border border-border font-cinzel text-[11px] text-muted">
              * {user.totalXp.toLocaleString()} XP
            </div>
            <div className="flex items-center gap-1.5 px-2.5 py-1 rounded-md border border-purple bg-purple-dim font-cinzel text-[11px] text-purple-light">
              {user.rank}
            </div>
          </div>
        )}
      </section>

      {/* ── Active pathways ───────────────────────────────────────────────── */}
      {hasEnrollments && (
        <section className="mb-10">
          <SectionHeading>Continue Your Journey</SectionHeading>
          <div className="flex flex-col gap-3">
            {enrolledDomains.map(domain => {
              const data = topicData[domain.id]
              if (!data) return null
              return (
                <EnrolledCard
                  key={domain.id}
                  topic={domain}
                  data={data}
                  onClick={() => navigate(`/pathway/${domain.id}`)}
                />
              )
            })}
          </div>
        </section>
      )}

      {/* ── How it works - shown until user enrolls ────────────────────── */}
      {!hasEnrollments && (
        <section className="mb-12">
          <SectionHeading>How the Academy Works</SectionHeading>
          <div
            className="grid gap-3"
            style={{gridTemplateColumns: 'repeat(auto-fit, minmax(190px, 1fr))'}}>
            {HOW_IT_WORKS.map(item => (
              <div
                key={item.step}
                className="rounded-[12px] border px-4 py-4 flex gap-3 items-start"
                style={{borderColor: item.border, background: item.bg}}
              >
                <div
                  className="flex-shrink-0 w-8 h-8 rounded-[8px] flex items-center justify-center"
                  style={{background: `color-mix(in srgb, ${item.color} 15%, transparent)`}}
                >
                  <item.icon size={16} strokeWidth={1.75} color={item.color}/>
                </div>
                <div>
                  <div className="font-cinzel text-[10px] tracking-[0.15em] mb-0.5" style={{color: item.color}}>
                    {item.step} / {item.title.toUpperCase()}
                  </div>
                  <p className="text-[12px] text-muted leading-[1.6] m-0">{item.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* ── Schools of the Academy ────────────────────────────────────────── */}
      <section className="mb-8">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center gap-3">
            <h2 className="font-cinzel text-[13px] font-semibold tracking-[0.15em] text-gold m-0 whitespace-nowrap">
              {hasEnrollments ? 'EXPLORE MORE SCHOOLS' : 'THE SCHOOLS OF THE ACADEMY'}
            </h2>
            <div className="flex-1 h-px bg-border"/>
          </div>
        </div>

        {/* Paywall banner - only after first enrolment without subscription */}
        {hasEnrollments && !canUnlock && (
          <div
            onClick={() => setShowUpgrade(true)}
            className="mb-4 flex items-center gap-3 px-4 py-3.5 rounded-[10px]
              border border-[rgba(201,162,39,0.35)] bg-[rgba(201,162,39,0.05)]
              cursor-pointer hover:bg-[rgba(201,162,39,0.09)] transition-colors"
          >
            <Lock size={14} strokeWidth={2} className="text-gold flex-shrink-0"/>
            <div className="flex-1 min-w-0">
              <p className="text-[13px] text-text leading-[1.5] m-0 font-semibold">
                Unlock all schools and pathways with a subscription
              </p>
              <p className="text-[12px] text-muted leading-[1.5] m-0">
                Monthly from GBP 6.99 / Annual from GBP 49.99 / Lifetime GBP 99
              </p>
            </div>
            <span className="text-[12px] font-cinzel text-gold whitespace-nowrap">View plans {'->'}</span>
          </div>
        )}

        <div
          className="grid gap-3"
          style={{gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))'}}>
          {SCHOOL_CARDS.map(({schoolId, meta, paths, hasActive}) => (
            <SchoolCard
              key={schoolId}
              schoolId={schoolId}
              meta={meta}
              paths={paths}
              hasActive={hasActive}
              onClick={() => navigate('/schools')}
            />
          ))}
        </div>
      </section>

      {/* ── Academy tagline ───────────────────────────────────────────────── */}
      <div className="mt-10 pt-8 border-t border-border text-center">
        <div className="flex items-center justify-center gap-2 mb-2">
          <Sparkles size={14} strokeWidth={1.75} style={{color: 'var(--gold)', opacity: 0.6}}/>
          <span className="font-cinzel text-[10px] tracking-[0.25em] text-muted">
            MORE SCHOOLS &amp; PATHWAYS IN DEVELOPMENT
          </span>
          <Sparkles size={14} strokeWidth={1.75} style={{color: 'var(--gold)', opacity: 0.6}}/>
        </div>
        <p className="text-[12px] text-muted m-0 opacity-70">
          The Academy grows with you. New disciplines are crafted continuously.
        </p>
      </div>

      <ScrollHint/>
    </div>
  )
}

/* ── Section heading ─────────────────────────────────────────────────────── */

function SectionHeading({children}: {children: React.ReactNode}) {
  return (
    <div className="flex items-center gap-3 mb-4">
      <h2 className="font-cinzel text-[13px] font-semibold tracking-[0.15em] text-gold m-0 whitespace-nowrap">
        {children}
      </h2>
      <div className="flex-1 h-px bg-border"/>
    </div>
  )
}

/* ── Scroll hint ─────────────────────────────────────────────────────────── */

function ScrollHint() {
  const [visible, setVisible] = useState(false)
  const [leaving, setLeaving] = useState(false)
  const sentinelRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
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
          opacity:    visible && !leaving ? 1 : 0,
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
