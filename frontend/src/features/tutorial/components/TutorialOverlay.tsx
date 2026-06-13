import { useEffect, useRef, useState, useCallback } from 'react'
import { createPortal } from 'react-dom'
import { useNavigate } from 'react-router-dom'
import { useTutorial } from '../context/TutorialContext'

// ── Types ─────────────────────────────────────────────────────────────────────

interface Rect { x: number; y: number; w: number; h: number }

// ── Helpers ───────────────────────────────────────────────────────────────────

function getTargetRect(targetId: string): Rect | null {
  const el = document.querySelector(`[data-tutorial-id="${targetId}"]`)
  if (!el) return null
  const r = el.getBoundingClientRect()
  return { x: r.left, y: r.top, w: r.width, h: r.height }
}

const PAD = 10  // spotlight padding around target

// ── Arrow SVG ─────────────────────────────────────────────────────────────────

function Arrow({ dir }: { dir: 'up' | 'down' | 'left' | 'right' }) {
  const paths: Record<string, string> = {
    up:    'M12 20 L12 4 M5 11 L12 4 L19 11',
    down:  'M12 4 L12 20 M5 13 L12 20 L19 13',
    left:  'M20 12 L4 12 M11 5 L4 12 L11 19',
    right: 'M4 12 L20 12 M13 5 L20 12 L13 19',
  }
  return (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none"
      stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <path d={paths[dir]} />
    </svg>
  )
}

// ── Skip confirmation dialog ───────────────────────────────────────────────────

function SkipDialog({ onConfirm, onCancel }: { onConfirm: () => void; onCancel: () => void }) {
  return (
    <div className="fixed inset-0 z-[10001] flex items-center justify-center">
      <div
        className="rounded-[16px] p-6 max-w-[380px] w-[90vw] text-center"
        style={{ background: 'var(--card)', border: '1px solid var(--border)', boxShadow: '0 20px 60px rgba(0,0,0,0.7)' }}
      >
        <div className="text-[32px] mb-3">⏭️</div>
        <h3 className="font-cinzel text-[16px] text-gold mb-2">Skip Tutorial?</h3>
        <p className="text-[13px] text-muted leading-[1.65] mb-5">
          You can re-enable the tutorial at any time from{' '}
          <strong className="text-text">Profile → Preferences</strong>.
          Changes take effect next time you log in.
        </p>
        <div className="flex gap-3 justify-center">
          <button className="btn btn-ghost text-[13px] px-5 py-2" onClick={onCancel}>
            Continue Tour
          </button>
          <button className="btn btn-primary text-[13px] px-5 py-2" onClick={onConfirm}>
            Skip
          </button>
        </div>
      </div>
    </div>
  )
}

// ── Callout box ───────────────────────────────────────────────────────────────

interface CalloutProps {
  title: string
  body: string
  ctaLabel: string
  stepIndex: number
  totalSteps: number
  rect: Rect | null
  position: string
  onNext: () => void
  onSkip: () => void
}

function Callout({ title, body, ctaLabel, stepIndex, totalSteps, rect, position, onNext, onSkip }: CalloutProps) {
  const GAP       = 16
  const CALLOUT_H = 260  // generous estimate - accounts for multi-line body text

  // Compute once; drives both the style calculation and the arrow direction.
  const effectivePos: string = (() => {
    if (position === 'center' || !rect) return 'center'
    const spotY = rect.y - PAD
    const spotH = rect.h + PAD * 2
    const vh    = window.innerHeight

    const fitsBelow = spotY + spotH + GAP + CALLOUT_H <= vh - 8
    const fitsAbove = spotY - GAP - CALLOUT_H >= 8

    if (position === 'bottom') {
      if (!fitsBelow) return fitsAbove ? 'top' : 'center'
    }
    if (position === 'top') {
      if (!fitsAbove) return fitsBelow ? 'bottom' : 'center'
    }
    return position
  })()

  const isCenter = effectivePos === 'center'

  const style: React.CSSProperties = isCenter
    ? {
        position: 'fixed',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        maxWidth: 460,
        width: '90vw',
      }
    : (() => {
        const s: React.CSSProperties = { position: 'fixed', maxWidth: 340, width: '90vw' }
        const spotX = rect!.x - PAD
        const spotY = rect!.y - PAD
        const spotW = rect!.w + PAD * 2
        const spotH = rect!.h + PAD * 2
        const vw    = window.innerWidth

        const centreLeft = Math.max(8, Math.min(vw - 348, spotX + spotW / 2 - 170))

        if (effectivePos === 'bottom') {
          s.top  = spotY + spotH + GAP
          s.left = centreLeft
        } else if (effectivePos === 'top') {
          // Use top (not bottom) so Math.max clamp keeps it inside the viewport
          s.top  = Math.max(8, spotY - GAP - CALLOUT_H)
          s.left = centreLeft
        } else if (effectivePos === 'right') {
          s.top  = Math.max(8, spotY + spotH / 2 - 80)
          s.left = spotX + spotW + GAP
        } else {
          s.top   = Math.max(8, spotY + spotH / 2 - 80)
          s.right = window.innerWidth - spotX + GAP
        }
        return s
      })()

  const arrowDir = effectivePos === 'bottom' ? 'up'
    : effectivePos === 'top'   ? 'down'
    : effectivePos === 'right' ? 'left'
    : effectivePos === 'left'  ? 'right'
    : null

  // Render markdown-lite: **bold** and newlines
  function renderBody(text: string) {
    return text.split('\n').map((line, i) => {
      const parts = line.split(/(\*\*[^*]+\*\*)/)
      return (
        <span key={i}>
          {parts.map((p, j) =>
            p.startsWith('**') ? <strong key={j} className="text-gold">{p.slice(2, -2)}</strong> : p
          )}
          {i < text.split('\n').length - 1 && <br />}
        </span>
      )
    })
  }

  return (
    <div
      style={{
        ...style,
        zIndex: 10000,
        background: 'var(--card)',
        border: '1px solid rgba(139,92,246,0.45)',
        borderRadius: 14,
        padding: '20px 22px',
        boxShadow: '0 8px 40px rgba(0,0,0,0.6), 0 0 0 1px rgba(139,92,246,0.15)',
        animation: 'fade-up 0.25s ease both',
      }}
    >
      {/* Arrow indicator - only shown when callout is anchored to a target */}
      {arrowDir && effectivePos !== 'center' && (
        <div style={{ color: 'var(--purple-light)', marginBottom: 8, display: 'flex', alignItems: 'center', gap: 6 }}>
          <Arrow dir={arrowDir} />
        </div>
      )}

      {/* Title */}
      <div className="font-cinzel" style={{ fontSize: 15, color: 'var(--gold)', marginBottom: 8 }}>
        {title}
      </div>

      {/* Body */}
      <div style={{ fontSize: 13, lineHeight: 1.7, color: 'var(--text)', marginBottom: 16 }}>
        {renderBody(body)}
      </div>

      {/* Progress dots + CTA */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 12 }}>
        {/* Dots */}
        <div style={{ display: 'flex', gap: 5 }}>
          {Array.from({ length: totalSteps }).map((_, i) => (
            <div
              key={i}
              style={{
                width: i === stepIndex ? 16 : 6, height: 6,
                borderRadius: 3,
                background: i === stepIndex ? 'var(--purple)' : i < stepIndex ? 'var(--purple-light)' : 'var(--border)',
                transition: 'all 0.2s',
              }}
            />
          ))}
        </div>

        {/* Skip + CTA buttons */}
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexShrink: 0 }}>
          <button className="btn btn-ghost" style={{ fontSize: 13, padding: '6px 14px' }} onClick={onSkip}>
            Skip
          </button>
          <button className="btn btn-primary" style={{ fontSize: 13, padding: '6px 18px' }} onClick={onNext}>
            {ctaLabel}
          </button>
        </div>
      </div>
    </div>
  )
}

// ── Main overlay ──────────────────────────────────────────────────────────────

export default function TutorialOverlay() {
  const { isActive, currentStep, stepIndex, totalSteps, next, skip } = useTutorial()
  const navigate = useNavigate()
  const [rect, setRect] = useState<Rect | null>(null)
  const [showSkipDialog, setShowSkipDialog] = useState(false)
  const rafRef = useRef<number>()

  // Track target element bounding rect (updates on scroll / resize)
  const updateRect = useCallback(() => {
    if (!currentStep?.targetId) { setRect(null); return }
    const r = getTargetRect(currentStep.targetId)
    setRect(r)
  }, [currentStep])

  useEffect(() => {
    if (!isActive || !currentStep?.targetId) { setRect(null); return }

    // Poll with rAF so we catch React renders that happen after navigation
    let attempts = 0
    function poll() {
      const r = getTargetRect(currentStep!.targetId!)
      if (r) {
        setRect(r)
        return
      }
      if (++attempts < 40) rafRef.current = requestAnimationFrame(poll)
    }
    poll()
    window.addEventListener('resize', updateRect)
    window.addEventListener('scroll', updateRect, true)
    return () => {
      if (rafRef.current) cancelAnimationFrame(rafRef.current)
      window.removeEventListener('resize', updateRect)
      window.removeEventListener('scroll', updateRect, true)
    }
  }, [isActive, currentStep, updateRect])

  if (!isActive || !currentStep) return null

  const isCenter = currentStep.position === 'center' || !currentStep.targetId
  const svgW = window.innerWidth
  const svgH = window.innerHeight

  const spotX = rect ? rect.x - PAD : 0
  const spotY = rect ? rect.y - PAD : 0
  const spotW = rect ? rect.w + PAD * 2 : 0
  const spotH = rect ? rect.h + PAD * 2 : 0
  const r     = 8 // corner radius of spotlight

  return createPortal(
    <>
      {/* SVG dimming overlay with spotlight cutout */}
      {!isCenter && (
        <svg
          style={{ position: 'fixed', inset: 0, zIndex: 9999, pointerEvents: 'none' }}
          width={svgW} height={svgH}
        >
          <defs>
            <mask id="tut-mask">
              <rect width="100%" height="100%" fill="white" />
              {rect && (
                <rect x={spotX} y={spotY} width={spotW} height={spotH} rx={r} fill="black" />
              )}
            </mask>
          </defs>
          <rect
            width="100%" height="100%"
            fill="rgba(0,0,0,0.72)"
            mask="url(#tut-mask)"
          />
          {/* Glowing border around spotlight */}
          {rect && (
            <rect
              x={spotX} y={spotY} width={spotW} height={spotH} rx={r}
              fill="none"
              stroke="rgba(139,92,246,0.7)"
              strokeWidth="2"
            />
          )}
        </svg>
      )}

      {/* Full dark overlay for center steps */}
      {isCenter && (
        <div style={{ position: 'fixed', inset: 0, zIndex: 9999, background: 'rgba(0,0,0,0.72)', pointerEvents: 'none' }} />
      )}

      {/* Clickthrough blocker (except callout) */}
      <div
        style={{ position: 'fixed', inset: 0, zIndex: 9999 }}
        onClick={(e) => e.stopPropagation()}
      />

      {/* Skip dialog */}
      {showSkipDialog && (
        <SkipDialog
          onConfirm={() => { setShowSkipDialog(false); skip() }}
          onCancel={() => setShowSkipDialog(false)}
        />
      )}

      {/* Callout */}
      <Callout
        title={currentStep.title}
        body={currentStep.body}
        ctaLabel={currentStep.ctaLabel ?? 'Next ->'}
        stepIndex={stepIndex}
        totalSteps={totalSteps}
        rect={isCenter ? null : rect}
        position={currentStep.position ?? 'bottom'}
        onNext={() => {
          if (currentStep.navigateTo && currentStep.id === 'pre-lesson') {
            navigate(currentStep.navigateTo)
          }
          next()
        }}
        onSkip={() => setShowSkipDialog(true)}
      />
    </>,
    document.body,
  )
}
