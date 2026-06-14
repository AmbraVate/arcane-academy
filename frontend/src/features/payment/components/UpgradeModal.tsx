import { useState } from 'react'
import { X, Check, Zap, Crown, Infinity } from 'lucide-react'
import { cn } from '@/lib/utils'
import { paymentsApi, type PlanType } from '@/shared/api/services'

interface Plan {
  id: PlanType
  icon: React.ElementType
  iconColor: string
  badge: string | null
  name: string
  price: string
  period: string
  description: string
  features: string[]
  ctaLabel: string
  highlight: boolean
}

const PLANS: Plan[] = [
  {
    id: 'MONTHLY',
    icon: Zap,
    iconColor: 'var(--teal)',
    badge: null,
    name: 'Monthly',
    price: '£6.99',
    period: 'per month',
    description: 'Full access to all disciplines. Cancel any time.',
    features: [
      'Unlimited disciplines',
      'All tiers — Apprentice to Lead',
      'Spaced-repetition reviews',
      'Badges & leaderboards',
    ],
    ctaLabel: 'Subscribe Monthly',
    highlight: false,
  },
  {
    id: 'ANNUAL',
    icon: Crown,
    iconColor: 'var(--gold)',
    badge: 'Best value',
    name: 'Annual',
    price: '£49.99',
    period: 'per year',
    description: 'Save 40% vs monthly. Billed once a year.',
    features: [
      'Everything in Monthly',
      '40% saving vs monthly',
      'Priority access to new disciplines',
    ],
    ctaLabel: 'Subscribe Annually',
    highlight: true,
  },
  {
    id: 'LIFETIME',
    icon: Infinity,
    iconColor: 'var(--purple-light)',
    badge: 'One-time',
    name: 'Lifetime',
    price: '£99',
    period: 'one-time payment',
    description: 'Pay once, access everything forever.',
    features: [
      'Everything in Annual',
      'Never pay again',
      'All future disciplines included',
    ],
    ctaLabel: 'Get Lifetime Access',
    highlight: false,
  },
]

interface UpgradeModalProps {
  onClose: () => void
}

export function UpgradeModal({ onClose }: UpgradeModalProps) {
  const [loading, setLoading] = useState<PlanType | null>(null)
  const [error, setError] = useState<string | null>(null)

  async function handleSubscribe(planType: PlanType) {
    setError(null)
    setLoading(planType)
    try {
      const checkoutUrl = await paymentsApi.createCheckout(planType)
      window.location.href = checkoutUrl
    } catch {
      setError('Could not start checkout. Please try again.')
      setLoading(null)
    }
  }

  return (
    /* Backdrop */
    <div
      className="fixed inset-0 z-50 flex items-center justify-center p-4"
      style={{ background: 'rgba(0,0,0,0.72)', backdropFilter: 'blur(4px)' }}
      onClick={e => { if (e.target === e.currentTarget) onClose() }}
    >
      <div
        className="relative w-full max-w-[820px] rounded-[18px] border border-border bg-card"
        style={{ boxShadow: '0 24px 80px rgba(0,0,0,0.6)' }}
      >
        {/* Close button */}
        <button
          onClick={onClose}
          className="absolute top-4 right-4 p-1.5 rounded-[8px] text-muted
            hover:text-text hover:bg-white/5 transition-colors"
        >
          <X size={18} />
        </button>

        {/* Header */}
        <div className="px-8 pt-8 pb-6 text-center">
          <p className="font-cinzel text-[11px] tracking-[0.2em] text-gold mb-2">
            THE ARCANE LIBRARY
          </p>
          <h2
            className="font-cinzel text-[clamp(20px,4vw,28px)] font-bold m-0 mb-2"
            style={{
              background: 'linear-gradient(135deg, var(--gold) 0%, var(--purple-light) 100%)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
            }}
          >
            Unlock All Disciplines
          </h2>
          <p className="text-[14px] text-muted max-w-[480px] mx-auto leading-[1.6]">
            Your first discipline is always free. A subscription grants access to every
            subject — Psychology, Genealogy, Natural Sciences, and all future additions.
          </p>
        </div>

        {/* Plan cards */}
        <div className="px-6 pb-6 grid grid-cols-3 gap-4 max-[640px]:grid-cols-1">
          {PLANS.map(plan => (
            <PlanCard
              key={plan.id}
              plan={plan}
              loading={loading === plan.id}
              disabled={loading !== null && loading !== plan.id}
              onSubscribe={() => handleSubscribe(plan.id)}
            />
          ))}
        </div>

        {/* Error */}
        {error && (
          <p className="px-8 pb-4 text-center text-[13px] text-red-400">{error}</p>
        )}

        {/* Footer note */}
        <div className="border-t border-border px-8 py-4 text-center">
          <p className="text-[11px] text-muted leading-[1.6]">
            Payments are processed securely by Stripe. You can cancel or manage your
            subscription at any time from your profile.
            Prices shown in GBP — Stripe may display your local currency at checkout.
          </p>
        </div>
      </div>
    </div>
  )
}

/* ── Plan card ──────────────────────────────────────────────────────────────── */

function PlanCard({
  plan,
  loading,
  disabled,
  onSubscribe,
}: {
  plan: Plan
  loading: boolean
  disabled: boolean
  onSubscribe: () => void
}) {
  return (
    <div
      className={cn(
        'relative flex flex-col rounded-[14px] border p-5',
        'transition-[border-color,box-shadow] duration-200',
        plan.highlight
          ? 'border-gold/50 bg-[rgba(201,162,39,0.05)]'
          : 'border-border bg-card/50',
      )}
      style={plan.highlight ? {
        boxShadow: '0 0 24px rgba(201,162,39,0.12)',
      } : undefined}
    >
      {/* Best value badge */}
      {plan.badge && (
        <div
          className="absolute -top-3 left-1/2 -translate-x-1/2 px-3 py-0.5 rounded-full
            font-cinzel text-[10px] tracking-[0.1em] whitespace-nowrap"
          style={{
            background: plan.highlight
              ? 'linear-gradient(90deg, var(--gold), color-mix(in srgb, var(--gold) 70%, var(--purple-light)))'
              : 'rgba(196,181,253,0.15)',
            color: plan.highlight ? '#1a1a2e' : 'var(--purple-light)',
            border: plan.highlight ? 'none' : '1px solid rgba(196,181,253,0.3)',
          }}
        >
          {plan.badge.toUpperCase()}
        </div>
      )}

      {/* Icon */}
      <div
        className="w-9 h-9 rounded-[10px] flex items-center justify-center mb-3"
        style={{ background: `color-mix(in srgb, ${plan.iconColor} 15%, transparent)` }}
      >
        <plan.icon size={18} strokeWidth={1.75} color={plan.iconColor} />
      </div>

      {/* Name & price */}
      <div className="font-cinzel text-[15px] font-bold text-text mb-0.5">{plan.name}</div>
      <div className="flex items-baseline gap-1.5 mb-1">
        <span className="font-cinzel text-[22px] font-bold" style={{ color: plan.iconColor }}>
          {plan.price}
        </span>
        <span className="text-[11px] text-muted">{plan.period}</span>
      </div>
      <p className="text-[12px] text-muted leading-[1.55] mb-4">{plan.description}</p>

      {/* Features */}
      <ul className="flex-1 flex flex-col gap-1.5 mb-5">
        {plan.features.map(feat => (
          <li key={feat} className="flex items-center gap-2 text-[12px] text-text/80">
            <Check size={12} strokeWidth={2.5} color="var(--teal)" className="flex-shrink-0" />
            {feat}
          </li>
        ))}
      </ul>

      {/* CTA */}
      <button
        onClick={onSubscribe}
        disabled={loading || disabled}
        className={cn(
          'w-full py-2.5 rounded-[9px] font-cinzel text-[12px] font-semibold',
          'transition-[background,opacity] duration-150',
          loading || disabled ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer',
        )}
        style={plan.highlight ? {
          background: 'linear-gradient(135deg, var(--gold), color-mix(in srgb, var(--gold) 70%, var(--purple-light)))',
          color: '#1a1a2e',
        } : {
          background: `color-mix(in srgb, ${plan.iconColor} 15%, transparent)`,
          border: `1px solid color-mix(in srgb, ${plan.iconColor} 40%, transparent)`,
          color: plan.iconColor,
        }}
      >
        {loading ? 'Redirecting…' : plan.ctaLabel}
      </button>
    </div>
  )
}
