import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { X, ChevronRight, ChevronLeft } from 'lucide-react'
import { authApi } from '@/shared/api/services'
import type { Badge } from '@/shared/types'
import { useAuth } from '@/shared/hooks/useAuth'

// ── Step data ────────────────────────────────────────────────────────────────

const PHASES = [
  { label: 'Hook',      color: '#c9a227' },
  { label: 'Explain',   color: '#8b5cf6' },
  { label: 'Practice',  color: '#3b82f6' },
  { label: 'Solo',      color: '#2dd4bf' },
  { label: 'Retrieval', color: '#fb923c' },
  { label: 'Complete',  color: '#4ade80' },
]

const RANKS = [
  { name: 'Novice',     xp: '0',     color: '#6b7280' },
  { name: 'Apprentice', xp: '800',   color: '#8b5cf6' },
  { name: 'Adept',      xp: '2,000', color: '#3b82f6' },
  { name: 'Mage',       xp: '4,000', color: '#2dd4bf' },
  { name: 'Archmage',   xp: '6,500', color: '#c9a227' },
  { name: 'Magus',      xp: '8,000', color: '#fb923c' },
  { name: 'Lord Magus', xp: '11,000',color: '#f87171' },
]

const TOPICS = [
  { glyph: '☕', name: 'Java' },
  { glyph: '🎨', name: 'Tailwind' },
  { glyph: '⚛️', name: 'React' },
  { glyph: '🧠', name: 'Psychology' },
  { glyph: '🌳', name: 'Genealogy' },
  { glyph: '🔬', name: 'Sciences' },
]

// ── Styles ───────────────────────────────────────────────────────────────────

const S = {
  overlay: {
    position: 'fixed' as const, inset: 0,
    background: 'rgba(6, 5, 14, 0.88)',
    backdropFilter: 'blur(6px)',
    WebkitBackdropFilter: 'blur(6px)',
    zIndex: 1000,
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    padding: '16px',
  },
  card: {
    background: 'linear-gradient(160deg, #16132b 0%, #0f0e1f 100%)',
    border: '1px solid rgba(139,92,246,0.25)',
    borderRadius: 20,
    width: '100%', maxWidth: 460,
    padding: '32px 28px 24px',
    position: 'relative' as const,
    boxShadow: '0 24px 80px rgba(0,0,0,0.6), 0 0 0 1px rgba(139,92,246,0.1)',
  },
  skipBtn: {
    position: 'absolute' as const, top: 14, right: 14,
    background: 'transparent', border: 'none',
    color: '#6b7280', cursor: 'pointer',
    display: 'flex', alignItems: 'center', gap: 4,
    fontSize: 12, fontFamily: 'Cinzel, serif',
    padding: '6px 8px', borderRadius: 6,
    transition: 'color .15s',
  },
  dots: {
    display: 'flex', justifyContent: 'center', gap: 6, margin: '24px 0 0',
  },
  nav: {
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    marginTop: 28,
  },
}

// ── Step content components ───────────────────────────────────────────────────

function Step1() {
  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ fontSize: 56, marginBottom: 16, lineHeight: 1 }}>🏰</div>
      <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, letterSpacing: '0.2em', color: '#c9a227', marginBottom: 10 }}>
        YOUR ARCANE JOURNEY BEGINS
      </div>
      <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#e8e0f0', margin: '0 0 14px', lineHeight: 1.25 }}>
        Welcome to the Academy
      </h2>
      <p style={{ fontSize: 14, color: '#a89cc0', lineHeight: 1.7, margin: 0 }}>
        A gamified, multi-discipline learning platform. Master coding, psychology,
        sciences, genealogy and more — guided by an AI mentor, driven by proven memory science.
      </p>
    </div>
  )
}

function Step2() {
  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, letterSpacing: '0.2em', color: '#c9a227', marginBottom: 10 }}>
        CHOOSE YOUR PATH
      </div>
      <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#e8e0f0', margin: '0 0 18px', lineHeight: 1.25 }}>
        Choose Your Discipline
      </h2>
      <div style={{
        display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 8, marginBottom: 18,
      }}>
        {TOPICS.map(t => (
          <div key={t.name} style={{
            background: 'rgba(139,92,246,0.08)', border: '1px solid rgba(139,92,246,0.18)',
            borderRadius: 10, padding: '10px 6px',
            display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4,
          }}>
            <span style={{ fontSize: 22 }}>{t.glyph}</span>
            <span style={{ fontFamily: 'Cinzel, serif', fontSize: 10, color: '#c4b5fd' }}>{t.name}</span>
          </div>
        ))}
      </div>
      <p style={{ fontSize: 13, color: '#a89cc0', lineHeight: 1.65, margin: 0 }}>
        Start with one discipline — completely free. Unlock the full library anytime with a subscription.
      </p>
    </div>
  )
}

function Step3() {
  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, letterSpacing: '0.2em', color: '#c9a227', marginBottom: 10 }}>
        SCIENCE-BACKED LEARNING
      </div>
      <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#e8e0f0', margin: '0 0 18px', lineHeight: 1.25 }}>
        Learn · Practice · Remember
      </h2>
      {/* Phase sequence */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flexWrap: 'wrap', gap: 4, marginBottom: 18 }}>
        {PHASES.map((p, i) => (
          <div key={p.label} style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
            <div style={{
              background: `${p.color}18`, border: `1px solid ${p.color}44`,
              borderRadius: 20, padding: '3px 10px',
              fontSize: 11, fontFamily: 'Cinzel, serif', color: p.color,
              whiteSpace: 'nowrap',
            }}>
              {p.label}
            </div>
            {i < PHASES.length - 1 && (
              <span style={{ color: '#4b4670', fontSize: 10 }}>›</span>
            )}
          </div>
        ))}
      </div>
      <p style={{ fontSize: 13, color: '#a89cc0', lineHeight: 1.65, margin: 0 }}>
        Not just reading. Every lesson follows a structured sequence — immersive hook, deep explanation,
        guided practice, solo challenge, and a retrieval check. Built for retention, not just recognition.
      </p>
    </div>
  )
}

function Step4({ badge }: { badge: Badge | null }) {
  if (badge) {
    return (
      <div style={{ textAlign: 'center' }}>
        <div style={{
          width: 72, height: 72, borderRadius: '50%', margin: '0 auto 16px',
          background: 'rgba(201,162,39,0.15)', border: '2px solid rgba(201,162,39,0.4)',
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          fontSize: 36,
          boxShadow: '0 0 32px rgba(201,162,39,0.25)',
        }}>
          {badge.glyph}
        </div>
        <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, letterSpacing: '0.2em', color: '#c9a227', marginBottom: 8 }}>
          BADGE EARNED
        </div>
        <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 20, color: '#e8e0f0', margin: '0 0 8px' }}>
          {badge.displayName}
        </h2>
        <p style={{ fontSize: 13, color: '#a89cc0', lineHeight: 1.6, margin: '0 0 20px' }}>
          {badge.description}
        </p>
        <p style={{ fontSize: 13, color: '#c4b5fd', lineHeight: 1.6, margin: 0 }}>
          Your path begins now. Choose a discipline and take the entry diagnostic to find your starting point.
        </p>
      </div>
    )
  }

  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, letterSpacing: '0.2em', color: '#c9a227', marginBottom: 10 }}>
        RISE THROUGH THE RANKS
      </div>
      <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#e8e0f0', margin: '0 0 18px', lineHeight: 1.25 }}>
        Earn Your Rank
      </h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 6, marginBottom: 18, textAlign: 'left' }}>
        {RANKS.map(r => (
          <div key={r.name} style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <div style={{ width: 8, height: 8, borderRadius: '50%', background: r.color, flexShrink: 0 }} />
            <span style={{ fontFamily: 'Cinzel, serif', fontSize: 12, color: r.color, width: 90 }}>{r.name}</span>
            <span style={{ fontSize: 11, color: '#6b7280' }}>{r.xp} XP</span>
          </div>
        ))}
      </div>
      <p style={{ fontSize: 13, color: '#a89cc0', lineHeight: 1.65, margin: 0 }}>
        Earn XP for every completed lesson. Begin as a Novice — your potential is limitless.
      </p>
    </div>
  )
}

// ── Main component ────────────────────────────────────────────────────────────

interface Props {
  onClose: () => void
}

export default function OnboardingModal({ onClose }: Props) {
  const navigate = useNavigate()
  const { markOnboardingDone } = useAuth()
  const [step, setStep] = useState(0)
  const [badge, setBadge] = useState<Badge | null>(null)
  const [completing, setCompleting] = useState(false)

  const TOTAL = 4
  const isLast = step === TOTAL - 1

  // Prevent body scroll while modal is open
  useEffect(() => {
    document.body.style.overflow = 'hidden'
    return () => { document.body.style.overflow = '' }
  }, [])

  async function handleComplete() {
    if (completing) return
    setCompleting(true)
    try {
      const result = await authApi.completeOnboarding()
      markOnboardingDone()
      if (result.badge) {
        setBadge(result.badge)
        // Stay on step 3 to show the badge earned screen
      } else {
        onClose()
        navigate('/domains')
      }
    } catch {
      // Don't block the user — just close and navigate
      markOnboardingDone()
      onClose()
      navigate('/domains')
    } finally {
      setCompleting(false)
    }
  }

  async function handleSkip() {
    try {
      await authApi.completeOnboarding()
    } catch { /* silent */ }
    markOnboardingDone()
    onClose()
  }

  function handleBadgeDismiss() {
    onClose()
    navigate('/domains')
  }

  const stepContent = [<Step1 />, <Step2 />, <Step3 />, <Step4 badge={badge} />]

  return (
    <div style={S.overlay} onClick={e => { if (e.target === e.currentTarget) handleSkip() }}>
      <div style={S.card}>

        {/* Skip button — hidden on final badge-earned screen */}
        {!(isLast && badge) && (
          <button
            style={S.skipBtn}
            onClick={handleSkip}
            onMouseEnter={e => (e.currentTarget.style.color = '#c4b5fd')}
            onMouseLeave={e => (e.currentTarget.style.color = '#6b7280')}
            aria-label="Skip onboarding"
          >
            <X size={13} strokeWidth={2} />
            Skip
          </button>
        )}

        {/* Animated step content */}
        <div style={{ minHeight: 280 }}>
          {stepContent[step]}
        </div>

        {/* Step dots */}
        <div style={S.dots}>
          {Array.from({ length: TOTAL }).map((_, i) => (
            <div
              key={i}
              style={{
                width: i === step ? 20 : 6, height: 6,
                borderRadius: 3,
                background: i === step ? '#8b5cf6' : i < step ? '#4b4670' : '#2e2850',
                transition: 'all .25s ease',
              }}
            />
          ))}
        </div>

        {/* Navigation */}
        <div style={S.nav}>
          {/* Back */}
          <button
            onClick={() => setStep(s => s - 1)}
            disabled={step === 0}
            style={{
              display: 'flex', alignItems: 'center', gap: 4,
              background: 'transparent', border: 'none',
              color: step === 0 ? '#2e2850' : '#8b7fa0',
              cursor: step === 0 ? 'default' : 'pointer',
              fontFamily: 'Cinzel, serif', fontSize: 12, padding: '8px 0',
              transition: 'color .15s',
            }}
            onMouseEnter={e => { if (step > 0) (e.currentTarget.style.color = '#c4b5fd') }}
            onMouseLeave={e => { if (step > 0) (e.currentTarget.style.color = '#8b7fa0') }}
          >
            <ChevronLeft size={14} strokeWidth={2} />
            Back
          </button>

          {/* Next / Begin */}
          {badge && isLast ? (
            <button
              onClick={handleBadgeDismiss}
              style={{
                display: 'flex', alignItems: 'center', gap: 6,
                background: 'linear-gradient(135deg, #c9a227, #f59e0b)',
                border: 'none', borderRadius: 9,
                color: '#0a0916', fontFamily: 'Cinzel, serif', fontSize: 13,
                fontWeight: 700, padding: '10px 22px', cursor: 'pointer',
                boxShadow: '0 4px 16px rgba(201,162,39,0.3)',
              }}
            >
              ✦ Choose Your Path
              <ChevronRight size={14} strokeWidth={2.5} />
            </button>
          ) : isLast ? (
            <button
              onClick={handleComplete}
              disabled={completing}
              style={{
                display: 'flex', alignItems: 'center', gap: 6,
                background: 'linear-gradient(135deg, #7c3aed, #8b5cf6)',
                border: 'none', borderRadius: 9,
                color: '#fff', fontFamily: 'Cinzel, serif', fontSize: 13,
                fontWeight: 700, padding: '10px 22px', cursor: completing ? 'wait' : 'pointer',
                boxShadow: '0 4px 16px rgba(139,92,246,0.35)',
                opacity: completing ? 0.7 : 1,
              }}
            >
              ✦ Begin Your Journey
              <ChevronRight size={14} strokeWidth={2.5} />
            </button>
          ) : (
            <button
              onClick={() => setStep(s => s + 1)}
              style={{
                display: 'flex', alignItems: 'center', gap: 6,
                background: 'rgba(139,92,246,0.15)', border: '1px solid rgba(139,92,246,0.35)',
                borderRadius: 9, color: '#c4b5fd',
                fontFamily: 'Cinzel, serif', fontSize: 13,
                padding: '9px 20px', cursor: 'pointer',
                transition: 'all .15s',
              }}
              onMouseEnter={e => {
                e.currentTarget.style.background = 'rgba(139,92,246,0.25)'
                e.currentTarget.style.borderColor = 'rgba(139,92,246,0.5)'
              }}
              onMouseLeave={e => {
                e.currentTarget.style.background = 'rgba(139,92,246,0.15)'
                e.currentTarget.style.borderColor = 'rgba(139,92,246,0.35)'
              }}
            >
              Next
              <ChevronRight size={14} strokeWidth={2} />
            </button>
          )}
        </div>
      </div>
    </div>
  )
}
