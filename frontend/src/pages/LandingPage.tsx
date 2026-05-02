import { useEffect, useRef, useState, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

/**
 * Landing — Epic Center-Aligned Layout
 *
 * A clean, centered design with a large serif headline and primary CTA.
 */

// ── Scroll-reveal hook (no library) — toggles `.in` once visible ──────────
function useReveal<T extends HTMLElement>() {
  const ref = useRef<T | null>(null)
  useEffect(() => {
    const el = ref.current
    if (!el || typeof IntersectionObserver === 'undefined') return
    const obs = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          el.classList.add('in')
          obs.unobserve(el)
        }
      },
      { threshold: 0.12, rootMargin: '0px 0px -8% 0px' },
    )
    obs.observe(el)
    return () => obs.disconnect()
  }, [])
  return ref
}

// ── Page ──────────────────────────────────────────────────────────────────
export default function LandingPage() {
  const navigate = useNavigate()
  const { user } = useAuth()

  const ctaDest = user ? '/topics' : '/register'
  const goCta = useCallback(() => navigate(ctaDest), [navigate, ctaDest])
  const goLogin = useCallback(() => navigate('/login'), [navigate])

  // Anchor-scroll for nav links
  const scrollTo = (id: string) => () => {
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }

  // Reveal hooks for each scroll section
  const ref1 = useReveal<HTMLElement>()
  const ref2 = useReveal<HTMLElement>()
  const ref3 = useReveal<HTMLElement>()
  const ref4 = useReveal<HTMLElement>()

  // Stable starfield positions
  const stars = Array.from({ length: 60 }).map((_, i) => ({
    x: (i * 137) % 100,
    y: (i * 73) % 100,
    r: 0.4 + ((i * 7) % 4) / 4,
    delay: (i * 0.13) % 4,
  }))

  return (
    <div
      className="absolute inset-0 overflow-y-auto overflow-x-hidden text-text font-crimson"
      style={{ background: 'var(--bg)' }}
    >
      {/* ── Candle-light background glow ─────────────────────────────────── */}
      <div
        className="fixed inset-0 pointer-events-none"
        style={{
          background:
            'radial-gradient(ellipse at 30% 40%, rgba(201,162,39,0.2) 0%, transparent 50%), radial-gradient(ellipse at 80% 70%, rgba(139,92,246,0.1) 0%, transparent 50%)',
          zIndex: 0,
        }}
      />

      {/* ── Twinkling stars ──────────────────────────────────────────────── */}
      <svg
        className="fixed inset-0 w-full h-full pointer-events-none"
        style={{ zIndex: 1 }}
        aria-hidden="true"
      >
        {stars.map((s, i) => (
          <circle
            key={i}
            className="star"
            cx={`${s.x}%`}
            cy={`${s.y}%`}
            r={s.r}
            fill="#f5d189"
            style={{ animationDelay: `${s.delay}s` }}
          />
        ))}
      </svg>

      {/* ── Sticky-feel header (not actually sticky to keep candle vibe) ── */}
      <header className="relative z-10 flex items-center justify-between px-12 py-[22px] max-md:px-6 max-md:py-4">
        <div className="brand-mark foil-on-dark cursor-pointer" onClick={() => navigate('/')}>
          ✦ ARCANE ACADEMY
        </div>
        <div className="flex items-center gap-3 max-md:hidden">
          <a
            className="font-cinzel text-[12px] tracking-[2px] text-muted cursor-pointer hover:text-text transition-colors"
            onClick={scrollTo('how')}
          >
            HOW IT WORKS
          </a>
          <a
            className="font-cinzel text-[12px] tracking-[2px] text-muted cursor-pointer hover:text-text transition-colors"
            onClick={scrollTo('difference')}
          >
            METHOD
          </a>
          <a
            className="font-cinzel text-[12px] tracking-[2px] text-muted cursor-pointer hover:text-text transition-colors"
            onClick={goLogin}
          >
            ENTER
          </a>
          <button className="btn-seal text-[12px]" onClick={goCta} style={{ padding: '10px 22px' }}>
            BEGIN
          </button>
        </div>
        <button
          className="btn-seal text-[12px] hidden max-md:inline-flex"
          onClick={goCta}
          style={{ padding: '8px 16px' }}
        >
          BEGIN
        </button>
      </header>

      {/* ── HERO TEXT — Center-aligned with orbiting magical embers ────────── */}
      <section className="relative z-[2] flex items-center justify-center min-h-[60vh] px-6 pt-[80px]">
        <div className="text-center max-w-[800px] mx-auto">
          <div className="hero-text-wrapper relative inline-block">
            {/* Orbiting golden magical embers */}
            <div className="ember ember-1" aria-hidden="true"></div>
            <div className="ember ember-2" aria-hidden="true"></div>
            
            <h1
              className="hero-heading text-text foil-on-dark"
              style={{
                fontFamily: 'Cinzel, serif',
                fontWeight: 700,
                lineHeight: 1.1,
                letterSpacing: '0.02em',
              }}
            >
              LEARN ANYTHING.<br />
              FORGET NOTHING.
            </h1>
          </div>
        </div>
      </section>

      {/* ── CTA BUTTON — Separated with vertical spacing ───────────────────── */}
      <section className="relative z-[2] flex items-center justify-center py-[80px] px-6">
        <button className="btn-seal" onClick={goCta} style={{ fontSize: 18, padding: '20px 48px' }}>
          BEGIN YOUR JOURNEY
        </button>
      </section>

      {/* ── HOW A CHUNK WORKS ─────────────────────────────────────────────── */}
      <section ref={ref1} className="grim-reveal relative z-[2] py-[100px] max-md:py-16 px-6">
        <div className="max-w-[1100px] mx-auto">
          <div className="text-center mb-14">
            <div className="ornate-divider">HOW A CHUNK WORKS</div>
            <h2 className="section-title-grimoire foil-on-dark mt-6">Story-encoded learning</h2>
          </div>
          <div className="grid grid-cols-3 gap-6 max-md:grid-cols-1">
            {(
              [
                ['🧩', 'Encode', 'Concepts arrive wrapped in narrative — not lecture slides. Velan, your AI mentor, sets the scene. You meet the idea through metaphor — then write the code.'],
                ['🎯', 'Recall', 'Tomorrow, the Grimoire asks you to retrieve. No notes. No re-reading. Active recall, graded honestly: Again, Hard, Good, Easy.'],
                ['🪶', 'Teach', 'Every chunk ends with you explaining it back, Feynman-style. Teaching cements understanding.'],
              ] as const
            ).map(([icon, title, body]) => (
              <div
                key={title}
                className="text-center"
                style={{
                  padding: 32,
                  background: 'var(--card)',
                  border: '1px solid var(--border)',
                  borderRadius: 12,
                }}
              >
                <div style={{ fontSize: 48, marginBottom: 16 }}>{icon}</div>
                <div
                  className="foil-on-dark"
                  style={{
                    fontFamily: 'Cinzel, serif',
                    fontSize: 24,
                    fontWeight: 600,
                    marginBottom: 12,
                  }}
                >
                  {title}
                </div>
                <div
                  style={{
                    fontFamily: 'Crimson Pro, serif',
                    fontSize: 16,
                    color: 'var(--muted)',
                    lineHeight: 1.6,
                  }}
                >
                  {body}
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── THE DIFFERENCE ────────────────────────────────────────────────── */}
      <section ref={ref2} className="grim-reveal relative z-[2] py-[100px] max-md:py-16 px-6">
        <div className="max-w-[1100px] mx-auto">
          <div className="text-center mb-14">
            <div className="ornate-divider">THE DIFFERENCE</div>
            <h2 className="section-title-grimoire foil-on-dark mt-6">Science-backed retention</h2>
          </div>
          <div className="text-center mb-10">
            <div
              style={{
                fontFamily: 'Crimson Pro, serif',
                fontSize: 20,
                color: 'var(--muted)',
                lineHeight: 1.6,
                maxWidth: 600,
                margin: '0 auto 40px',
              }}
            >
              Every other course you've taken poured information into a leaky bucket.
              We rebuild the bucket — using the six techniques cognitive scientists already know work.
            </div>
          </div>
          <div className="max-w-[900px] mx-auto grid grid-cols-3 gap-4 max-md:grid-cols-1">
            {(
              [
                ['5×', 'retention vs. passive video'],
                ['3×', 'recall vs. re-reading'],
                ['↓60%', 'time-to-mastery on equivalent topics'],
              ] as const
            ).map(([n, l]) => (
              <div
                key={l}
                style={{
                  textAlign: 'center',
                  padding: 20,
                  background: 'var(--card)',
                  border: '1px solid var(--border)',
                  borderRadius: 8,
                }}
              >
                <div
                  className="foil-on-dark"
                  style={{
                    fontFamily: 'Cinzel, serif',
                    fontSize: 38,
                    fontWeight: 700,
                    marginBottom: 6,
                  }}
                >
                  {n}
                </div>
                <div
                  style={{
                    fontFamily: 'Cinzel, serif',
                    fontSize: 11,
                    letterSpacing: 2,
                    color: 'var(--muted)',
                    textTransform: 'uppercase',
                  }}
                >
                  {l}
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── TOPICS ─────────────────────────────────────────────────────── */}
      <section ref={ref3} className="grim-reveal relative z-[2] py-[100px] max-md:py-16 px-6">
        <div className="max-w-[1100px] mx-auto">
          <div className="text-center mb-14">
            <div className="ornate-divider">THE GRIMOIRES</div>
            <h2 className="section-title-grimoire foil-on-dark mt-6">Pick your first tower.</h2>
          </div>
          <div className="grid grid-cols-4 gap-4 max-md:grid-cols-2 max-md:gap-3">
            {(
              [
                ['☕',  'Java',         'OO mastery from `Hello, World!` to concurrent systems.', 'OPEN'],
                ['🎨', 'Tailwind CSS', 'Utility-first styling. Atomic spells, composed.',         'OPEN'],
                ['⚛️', 'React',        'Components, hooks, and the modern frontend grimoire.',    'OPEN'],
                ['🗃️', 'SQL',          'Read, filter, summarise — the language of data.',         'OPEN'],
              ] as const
            ).map(([icon, name, desc, status]) => (
              <button
                key={name}
                onClick={status === 'OPEN' ? goCta : undefined}
                disabled={status !== 'OPEN'}
                className="text-left relative transition-[transform,border-color,background] duration-200"
                style={{
                  background: 'var(--card)',
                  border: '1px solid var(--border)',
                  borderRadius: 10,
                  padding: 24,
                  opacity: status === 'OPEN' ? 1 : 0.65,
                  cursor: status === 'OPEN' ? 'pointer' : 'not-allowed',
                  fontFamily: 'inherit',
                }}
                onMouseEnter={e => {
                  if (status === 'OPEN') {
                    e.currentTarget.style.transform = 'translateY(-3px)'
                    e.currentTarget.style.borderColor = 'var(--gold-foil)'
                  }
                }}
                onMouseLeave={e => {
                  e.currentTarget.style.transform = ''
                  e.currentTarget.style.borderColor = 'var(--border)'
                }}
              >
                <div style={{ fontSize: 40, marginBottom: 14 }}>{icon}</div>
                <div
                  className="foil-on-dark"
                  style={{ fontFamily: 'Cinzel, serif', fontSize: 18, fontWeight: 600 }}
                >
                  {name}
                </div>
                <div
                  style={{
                    fontFamily: 'Crimson Pro, serif',
                    fontSize: 13,
                    color: 'var(--muted)',
                    lineHeight: 1.55,
                    marginTop: 8,
                    fontStyle: status === 'OPEN' ? 'normal' : 'italic',
                  }}
                >
                  {desc}
                </div>
                <div
                  className="absolute"
                  style={{
                    top: 14,
                    right: 14,
                    fontFamily: 'Cinzel, serif',
                    fontSize: 9,
                    letterSpacing: 2,
                    padding: '3px 8px',
                    borderRadius: 4,
                    background: status === 'OPEN' ? 'var(--teal-dim)' : 'var(--border)',
                    color: status === 'OPEN' ? 'var(--teal)' : 'var(--muted)',
                    border: status === 'OPEN' ? '1px solid var(--teal)' : 'none',
                  }}
                >
                  {status}
                </div>
              </button>
            ))}
          </div>
        </div>
      </section>

      {/* ── FINAL CTA ───────────────────────────────────────────────────── */}
      <section
        ref={ref4}
        className="grim-reveal relative z-[2] text-center"
        style={{ padding: '110px 24px 60px' }}
      >
        <div
          className="absolute inset-0 pointer-events-none"
          style={{
            background:
              'radial-gradient(ellipse at center, rgba(201,162,39,0.18) 0%, transparent 60%)',
          }}
        />
        <div className="relative max-w-[720px] mx-auto">
          <div className="ornate-divider">THE THRESHOLD</div>
          <h2
            className="section-title-grimoire foil-on-dark mt-6"
            style={{ fontSize: 'clamp(40px, 6vw, 56px)' }}
          >
            Open the Grimoire.
          </h2>
          <p className="section-sub-grimoire mx-auto" style={{ margin: '20px auto 36px' }}>
            Take the entry diagnostic. The Academy adapts to where you are — and the first chunk is always
            the easiest one you've already half-mastered.
          </p>
          <button className="btn-seal" onClick={goCta} style={{ fontSize: 16, padding: '18px 48px', marginBottom: 24 }}>
            BEGIN YOUR JOURNEY
          </button>
          <div
            className="mt-20"
            style={{
              fontFamily: 'Cinzel, serif',
              fontSize: 11,
              letterSpacing: 4,
              color: 'var(--muted)',
            }}
          >
            BUILT ON SCIENCE · WRAPPED IN STORY
          </div>
        </div>
      </section>
    </div>
  )
}
