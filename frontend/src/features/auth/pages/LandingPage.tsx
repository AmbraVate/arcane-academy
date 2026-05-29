import { Link, Navigate } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'

// ── Data ─────────────────────────────────────────────────────────────────────

const TRACKS = [
  { img: '/Java.png',            name: 'Java',             color: '#f89820', desc: 'Object-oriented programming, data structures, algorithms, and design patterns.' },
  { img: '/React.png',           name: 'React',            color: '#61dafb', desc: 'Component-driven UIs, hooks, context, and modern state management.' },
  { img: '/TailwindCSS.png',     name: 'Tailwind CSS',     color: '#38bdf8', desc: 'Utility-first styling with a live in-browser preview sandbox.' },
  { img: '/Psychology.png',      name: 'Psychology',       color: '#c4b5fd', desc: 'Cognitive, social, and clinical psychology explored at degree level.' },
  { img: '/NaturalSciences.png', name: 'Natural Sciences', color: '#4ade80', desc: 'Biology, chemistry, and physics rebuilt from first principles.' },
  { img: '/Genealogy.png',       name: 'Genealogy',        color: '#c9a227', desc: 'Family history research, archival records, and DNA analysis.' },
]

const COMING_SOON = ['SQL', 'JavaScript', 'TypeScript', 'Python', 'HTML', 'CSS']

const COMING_SOON_TAGLINE = 'We\'re building fast — dozens more disciplines are in active development across coding, sciences, humanities, and beyond.'

const PHASES = [
  { label: 'Hook',      color: '#c9a227', desc: 'An immersive story sets the scene' },
  { label: 'Explain',   color: '#8b5cf6', desc: 'Deep conceptual explanation' },
  { label: 'Practice',  color: '#3b82f6', desc: 'Guided scaffolded challenge' },
  { label: 'Solo',      color: '#2dd4bf', desc: 'Rebuild it from memory' },
  { label: 'Retrieval', color: '#fb923c', desc: 'Quiz that locks it in' },
  { label: 'Complete',  color: '#4ade80', desc: 'XP, badge, spaced review scheduled' },
]

const FEATURES = [
  { icon: '🔁', title: 'Spaced Repetition', desc: 'The SM-2 algorithm schedules every review at the exact moment your brain is about to forget — building memories that last decades, not days.' },
  { icon: '🤖', title: 'AI Mentor', desc: 'Archmage Veylan is embedded in every lesson. Ask questions, get Socratic hints, and receive instant AI-graded feedback on your written explanations.' },
  { icon: '🐇', title: 'Rabbit Holes', desc: 'Spot a term mid-lesson that sparks curiosity? Save it to your Curiosity Queue and dive deep whenever you\'re ready.' },
  { icon: '🏆', title: 'XP & Ranks', desc: 'Earn XP for every completed lesson. Rise from Novice through Apprentice, Adept, Mage, Archmage, Magus, and Lord Magus.' },
  { icon: '📊', title: 'Memory Health', desc: 'Visual memory-strength bars show exactly how well each concept is retained. Green means locked in — yellow means it\'s time to review.' },
  { icon: '🎓', title: 'Feynman Technique', desc: 'After each lesson, explain it back in your own words. The AI grades your explanation on accuracy, completeness, and clarity.' },
]

const PRICING = [
  {
    name: 'Free',
    price: '£0',
    period: 'forever',
    color: '#6b7280',
    highlight: false,
    features: ['1 topic of your choice', 'Full 71-lesson depth', 'AI mentor included', 'XP, ranks & badges', 'Spaced review system'],
    cta: 'Start Free',
    href: '/register',
  },
  {
    name: 'Monthly',
    price: '£6.99',
    period: 'per month',
    color: '#3b82f6',
    highlight: false,
    features: ['All topics, unlocked', 'Everything in Free', 'Cancel anytime'],
    cta: 'Start Monthly',
    href: '/register',
  },
  {
    name: 'Annual',
    price: '£49.99',
    period: 'per year',
    badge: 'Best value',
    color: '#8b5cf6',
    highlight: true,
    features: ['All topics, unlocked', 'Everything in Free', '≈ £4.17 / month', 'Cancel anytime'],
    cta: 'Start Free → Upgrade',
    href: '/register',
  },
  {
    name: 'Lifetime',
    price: '£99',
    period: 'one-time',
    color: '#c9a227',
    highlight: false,
    features: ['All topics, forever', 'Everything in Annual', 'All future topics included', 'No recurring charge'],
    cta: 'Get Lifetime Access',
    href: '/register',
  },
]

// ── Helpers ───────────────────────────────────────────────────────────────────

function GoldRule() {
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 12, margin: '0 auto 14px', maxWidth: 180 }}>
      <div style={{ flex: 1, height: 1, background: 'linear-gradient(to right, transparent, rgba(201,162,39,0.4))' }} />
      <span style={{ color: '#c9a227', fontSize: 12 }}>✦</span>
      <div style={{ flex: 1, height: 1, background: 'linear-gradient(to left, transparent, rgba(201,162,39,0.4))' }} />
    </div>
  )
}

function SectionLabel({ children }: { children: string }) {
  return (
    <p style={{ fontFamily: 'Cinzel, serif', color: 'var(--purple-light)', fontSize: 11, letterSpacing: '0.22em', textTransform: 'uppercase', textAlign: 'center', marginBottom: 14 }}>
      {children}
    </p>
  )
}

// ── Page ─────────────────────────────────────────────────────────────────────

export default function LandingPage() {
  const { user } = useAuth()

  // Safety net: if a logged-in user somehow reaches this component, send them home.
  if (user) return <Navigate to="/" replace />

  return (
    <div style={{ minHeight: '100vh', overflowY: 'auto', background: 'var(--bg)', color: 'var(--text)', fontFamily: 'Crimson Pro, serif' }}>

      {/* ── Sticky nav ───────────────────────────────────────────────────── */}
      <header style={{
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
        padding: 'clamp(12px, 2vw, 16px) clamp(12px, 4vw, 32px)',
        borderBottom: '1px solid rgba(255,255,255,0.06)',
        position: 'sticky', top: 0, zIndex: 50, gap: 8,
        background: 'rgba(10,9,22,0.92)', backdropFilter: 'blur(12px)',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, minWidth: 0 }}>
          <img src="/Arcane.jpg" alt="Arcane Academy" style={{ width: 28, height: 28, borderRadius: 6, objectFit: 'cover', flexShrink: 0 }} />
          <span style={{ fontFamily: 'Cinzel, serif', fontSize: 'clamp(12px, 3.5vw, 16px)', color: 'var(--gold)', letterSpacing: '0.06em', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
            Arcane Academy
          </span>
        </div>
        <div style={{ display: 'flex', gap: 6, alignItems: 'center', flexShrink: 0 }}>
          <Link to="/login" className="btn btn-ghost" style={{ fontSize: 'clamp(11px, 2.5vw, 13px)', padding: 'clamp(6px, 1.5vw, 8px) clamp(8px, 2.5vw, 16px)', whiteSpace: 'nowrap' }}>Sign In</Link>
          <Link to="/register" className="btn btn-primary" style={{ fontSize: 'clamp(11px, 2.5vw, 13px)', padding: 'clamp(6px, 1.5vw, 8px) clamp(10px, 2.5vw, 18px)', whiteSpace: 'nowrap' }}>Start Free →</Link>
        </div>
      </header>

      {/* ── Hero ─────────────────────────────────────────────────────────── */}
      <section style={{
        textAlign: 'center',
        padding: 'clamp(72px, 12vw, 120px) 24px 80px',
        background: 'radial-gradient(ellipse 90% 60% at 50% 0%, rgba(139,92,246,0.18) 0%, transparent 70%)',
        position: 'relative', overflow: 'hidden',
      }}>
        {/* Subtle ambient dots */}
        <div aria-hidden style={{ position: 'absolute', inset: 0, backgroundImage: 'radial-gradient(rgba(139,92,246,0.12) 1px, transparent 1px)', backgroundSize: '32px 32px', pointerEvents: 'none' }} />

        <div style={{ position: 'relative' }}>
          <SectionLabel>The Polymath's Path</SectionLabel>
          <h1 style={{
            fontFamily: 'Cinzel, serif',
            fontSize: 'clamp(30px, 5.5vw, 60px)',
            fontWeight: 700, lineHeight: 1.15,
            margin: '0 auto 22px', maxWidth: 720,
            background: 'linear-gradient(135deg, var(--gold) 0%, var(--purple-light) 60%, #60a5fa 100%)',
            WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent',
          }}>
            Master Multiple Disciplines.<br />Forget Nothing.
          </h1>
          <p style={{ fontSize: 'clamp(16px, 2vw, 20px)', color: 'var(--muted)', maxWidth: 580, margin: '0 auto 20px', lineHeight: 1.75 }}>
            Arcane Academy uses spaced repetition, structured encoding phases, and an AI mentor
            to make you genuinely fluent — across coding, sciences, and humanities.
          </p>

          {/* Social proof */}
          <div style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap', gap: 20, marginBottom: 40 }}>
            {[
              { val: '6',   label: 'Active disciplines' },
              { val: '426', label: 'Structured lessons' },
              { val: 'SM-2',label: 'Memory algorithm' },
              { val: 'Free',label: 'To start' },
            ].map(s => (
              <div key={s.label} style={{ textAlign: 'center' }}>
                <div style={{ fontFamily: 'Cinzel, serif', fontSize: 'clamp(18px, 3vw, 24px)', fontWeight: 700, color: 'var(--gold)' }}>{s.val}</div>
                <div style={{ fontSize: 12, color: 'var(--muted)', letterSpacing: '0.06em', marginTop: 2 }}>{s.label}</div>
              </div>
            ))}
          </div>

          <div style={{ display: 'flex', gap: 12, justifyContent: 'center', flexWrap: 'wrap' }}>
            <Link to="/register" className="btn btn-primary" style={{ padding: '14px 36px', fontSize: 15, fontFamily: 'Cinzel, serif' }}>
              ✦ Begin Your Journey
            </Link>
            <Link to="/login" className="btn btn-ghost" style={{ padding: '14px 28px', fontSize: 15 }}>
              Sign In
            </Link>
          </div>
          <p style={{ marginTop: 14, fontSize: 12, color: 'var(--muted)', letterSpacing: '0.04em' }}>
            No credit card required · Free forever for one topic
          </p>
        </div>
      </section>

      {/* ── Encoding sequence ────────────────────────────────────────────── */}
      <section style={{ padding: 'clamp(56px, 8vw, 88px) 24px', background: 'var(--surface)' }}>
        <div style={{ maxWidth: 860, margin: '0 auto' }}>
          <GoldRule />
          <SectionLabel>How you learn here</SectionLabel>
          <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 'clamp(18px, 3vw, 26px)', textAlign: 'center', marginBottom: 10, color: 'var(--text)' }}>
            The Encoding Sequence
          </h2>
          <p style={{ textAlign: 'center', color: 'var(--muted)', fontSize: 15, maxWidth: 540, margin: '0 auto 44px', lineHeight: 1.7 }}>
            Every lesson follows a six-phase sequence designed to encode knowledge deeply — not just recognise it for the moment.
          </p>

          {/* Phase cards */}
          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(120px, 1fr))',
            gap: 12,
          }}>
            {PHASES.map((p, i) => (
              <div key={p.label} style={{
                background: `${p.color}0d`,
                border: `1px solid ${p.color}30`,
                borderTop: `2px solid ${p.color}`,
                borderRadius: 12,
                padding: '16px 12px',
                textAlign: 'center',
              }}>
                <div style={{ fontFamily: 'Cinzel, serif', fontSize: 10, color: 'var(--muted)', letterSpacing: '0.1em', marginBottom: 6 }}>
                  {String(i + 1).padStart(2, '0')}
                </div>
                <div style={{ fontFamily: 'Cinzel, serif', fontSize: 13, color: p.color, marginBottom: 6, fontWeight: 600 }}>
                  {p.label}
                </div>
                <div style={{ fontSize: 12, color: 'var(--muted)', lineHeight: 1.5 }}>{p.desc}</div>
              </div>
            ))}
          </div>

          <p style={{ textAlign: 'center', fontSize: 13, color: 'var(--muted)', marginTop: 24, fontStyle: 'italic' }}>
            After completion, the SM-2 spaced-repetition algorithm schedules your next review — right before you would forget.
          </p>
        </div>
      </section>

      {/* ── Active tracks ────────────────────────────────────────────────── */}
      <section style={{ padding: 'clamp(56px, 8vw, 88px) 24px', maxWidth: 1040, margin: '0 auto' }}>
        <GoldRule />
        <SectionLabel>What you can learn</SectionLabel>
        <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 'clamp(18px, 3vw, 26px)', textAlign: 'center', marginBottom: 10, color: 'var(--text)' }}>
          Active Learning Tracks
        </h2>
        <p style={{ textAlign: 'center', color: 'var(--muted)', fontSize: 15, marginBottom: 44 }}>
          71 lessons per topic · Apprentice → Junior → Senior → Lead
        </p>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(290px, 1fr))', gap: 16 }}>
          {TRACKS.map(t => (
            <div
              key={t.name}
              style={{
                background: 'var(--card)', border: '1px solid var(--border)',
                borderRadius: 14, padding: '22px 20px',
                display: 'flex', alignItems: 'flex-start', gap: 18,
                transition: 'border-color 0.2s, box-shadow 0.2s, transform 0.2s',
              }}
              onMouseEnter={e => {
                const el = e.currentTarget as HTMLDivElement
                el.style.borderColor = t.color + '55'
                el.style.boxShadow = `0 8px 28px ${t.color}18`
                el.style.transform = 'translateY(-2px)'
              }}
              onMouseLeave={e => {
                const el = e.currentTarget as HTMLDivElement
                el.style.borderColor = 'var(--border)'
                el.style.boxShadow = 'none'
                el.style.transform = 'none'
              }}
            >
              <img src={t.img} alt={t.name} style={{ width: 48, height: 48, objectFit: 'contain', flexShrink: 0, borderRadius: 8 }} />
              <div>
                <div style={{ fontFamily: 'Cinzel, serif', fontSize: 15, color: t.color, marginBottom: 5, fontWeight: 600 }}>{t.name}</div>
                <div style={{ fontSize: 13, color: 'var(--muted)', lineHeight: 1.65 }}>{t.desc}</div>
              </div>
            </div>
          ))}
        </div>

        {/* Coming soon */}
        <div style={{ marginTop: 36, textAlign: 'center' }}>
          <p style={{ fontFamily: 'Cinzel, serif', fontSize: 12, color: 'var(--muted)', letterSpacing: '0.12em', marginBottom: 8 }}>
            COMING SOON
          </p>
          <p style={{ fontSize: 13, color: 'var(--muted)', maxWidth: 480, margin: '0 auto 18px', lineHeight: 1.65, opacity: 0.8 }}>
            {COMING_SOON_TAGLINE}
          </p>
          <div style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap', gap: 10 }}>
            {COMING_SOON.map(name => (
              <div key={name} style={{
                background: 'var(--surface)', border: '1px solid var(--border)',
                borderRadius: 20, padding: '5px 14px',
                fontFamily: 'Cinzel, serif', fontSize: 12, color: 'var(--muted)', opacity: 0.6,
              }}>
                {name}
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── Features ─────────────────────────────────────────────────────── */}
      <section style={{ padding: 'clamp(56px, 8vw, 88px) 24px', background: 'var(--surface)' }}>
        <div style={{ maxWidth: 1000, margin: '0 auto' }}>
          <GoldRule />
          <SectionLabel>Platform features</SectionLabel>
          <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 'clamp(18px, 3vw, 26px)', textAlign: 'center', marginBottom: 48, color: 'var(--text)' }}>
            Built for Polymaths
          </h2>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: 28 }}>
            {FEATURES.map(f => (
              <div key={f.title} style={{
                display: 'flex', gap: 16, alignItems: 'flex-start',
                padding: '20px', borderRadius: 12,
                background: 'rgba(139,92,246,0.04)', border: '1px solid rgba(139,92,246,0.1)',
              }}>
                <div style={{
                  flexShrink: 0, width: 42, height: 42, borderRadius: 10,
                  background: 'rgba(139,92,246,0.12)', border: '1px solid rgba(139,92,246,0.2)',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  fontSize: 20,
                }}>
                  {f.icon}
                </div>
                <div>
                  <div style={{ fontFamily: 'Cinzel, serif', fontSize: 13, color: 'var(--purple-light)', marginBottom: 7, letterSpacing: '0.04em' }}>{f.title}</div>
                  <div style={{ fontSize: 13, color: 'var(--muted)', lineHeight: 1.7 }}>{f.desc}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── Pricing ──────────────────────────────────────────────────────── */}
      <section style={{ padding: 'clamp(56px, 8vw, 88px) 24px' }}>
        <div style={{ maxWidth: 900, margin: '0 auto' }}>
          <GoldRule />
          <SectionLabel>Simple pricing</SectionLabel>
          <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 'clamp(18px, 3vw, 26px)', textAlign: 'center', marginBottom: 10, color: 'var(--text)' }}>
            Start Free. Scale When Ready.
          </h2>
          <p style={{ textAlign: 'center', color: 'var(--muted)', fontSize: 15, marginBottom: 48 }}>
            One topic is free — forever. Unlock everything when you're ready.
          </p>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(210px, 1fr))', gap: 16, alignItems: 'stretch' }}>
            {PRICING.map(plan => (
              <div
                key={plan.name}
                style={{
                  background: plan.highlight ? 'linear-gradient(160deg, #1a1535 0%, #130f2a 100%)' : 'var(--card)',
                  border: `1px solid ${plan.highlight ? plan.color + '55' : 'var(--border)'}`,
                  borderTop: `3px solid ${plan.color}`,
                  borderRadius: 16, padding: '28px 24px',
                  display: 'flex', flexDirection: 'column',
                  boxShadow: plan.highlight ? `0 12px 40px ${plan.color}22` : 'none',
                  position: 'relative',
                }}
              >
                {plan.badge && (
                  <div style={{
                    position: 'absolute', top: -12, left: '50%', transform: 'translateX(-50%)',
                    background: plan.color, color: '#0a0916',
                    fontFamily: 'Cinzel, serif', fontSize: 11, fontWeight: 700,
                    padding: '3px 12px', borderRadius: 20, whiteSpace: 'nowrap',
                  }}>
                    {plan.badge}
                  </div>
                )}

                <div style={{ fontFamily: 'Cinzel, serif', fontSize: 14, color: plan.color, marginBottom: 6 }}>{plan.name}</div>
                <div style={{ marginBottom: 20 }}>
                  <span style={{ fontFamily: 'Cinzel, serif', fontSize: 32, fontWeight: 700, color: 'var(--text)' }}>{plan.price}</span>
                  <span style={{ fontSize: 13, color: 'var(--muted)', marginLeft: 6 }}>{plan.period}</span>
                </div>

                <ul style={{ listStyle: 'none', padding: 0, margin: '0 0 24px', display: 'flex', flexDirection: 'column', gap: 9, flex: 1 }}>
                  {plan.features.map(feat => (
                    <li key={feat} style={{ display: 'flex', alignItems: 'baseline', gap: 8, fontSize: 13, color: 'var(--muted)' }}>
                      <span style={{ color: plan.color, flexShrink: 0, fontSize: 11 }}>✦</span>
                      {feat}
                    </li>
                  ))}
                </ul>

                <Link
                  to={plan.href}
                  style={{
                    display: 'block', textAlign: 'center',
                    padding: '11px 20px', borderRadius: 9,
                    fontFamily: 'Cinzel, serif', fontSize: 13, fontWeight: 600,
                    textDecoration: 'none',
                    background: plan.highlight ? `linear-gradient(135deg, ${plan.color}cc, ${plan.color})` : 'transparent',
                    border: `1px solid ${plan.highlight ? 'transparent' : plan.color + '55'}`,
                    color: plan.highlight ? '#0a0916' : plan.color,
                    transition: 'opacity .15s',
                  }}
                  onMouseEnter={e => ((e.currentTarget as HTMLAnchorElement).style.opacity = '0.85')}
                  onMouseLeave={e => ((e.currentTarget as HTMLAnchorElement).style.opacity = '1')}
                >
                  {plan.cta}
                </Link>
              </div>
            ))}
          </div>

          <p style={{ textAlign: 'center', fontSize: 12, color: 'var(--muted)', marginTop: 28 }}>
            All plans include a 30-day cancellation right
          </p>
        </div>
      </section>

      {/* ── Final CTA ────────────────────────────────────────────────────── */}
      <section style={{
        padding: 'clamp(72px, 10vw, 104px) 24px',
        textAlign: 'center',
        background: 'radial-gradient(ellipse 80% 70% at 50% 100%, rgba(139,92,246,0.14) 0%, transparent 70%)',
      }}>
        <GoldRule />
        <p style={{ fontFamily: 'Cinzel, serif', fontSize: 11, color: 'var(--gold)', letterSpacing: '0.22em', marginBottom: 18 }}>
          YOUR ARCANE JOURNEY AWAITS
        </p>
        <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 'clamp(22px, 4vw, 36px)', marginBottom: 16, color: 'var(--text)', lineHeight: 1.2 }}>
          Ready to become a polymath?
        </h2>
        <p style={{ color: 'var(--muted)', marginBottom: 38, fontSize: 17, lineHeight: 1.65, maxWidth: 480, margin: '0 auto 38px' }}>
          Join thousands of learners mastering code, science, and the human mind — one structured lesson at a time.
        </p>
        <Link to="/register" className="btn btn-primary" style={{ padding: '15px 48px', fontSize: 16, fontFamily: 'Cinzel, serif', letterSpacing: '0.05em' }}>
          ✦ Enroll Free Today
        </Link>
        <p style={{ marginTop: 14, fontSize: 12, color: 'var(--muted)' }}>
          No credit card · One topic, forever free · Takes 30 seconds
        </p>
      </section>

      {/* ── Contact Us ───────────────────────────────────────────────────── */}
      <section style={{ padding: 'clamp(48px, 7vw, 72px) 24px', background: 'var(--surface)' }}>
        <div style={{ maxWidth: 640, margin: '0 auto', textAlign: 'center' }}>
          <GoldRule />
          <SectionLabel>Get in touch</SectionLabel>
          <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 'clamp(18px, 3vw, 24px)', marginBottom: 12, color: 'var(--text)' }}>
            Contact Us
          </h2>
          <p style={{ color: 'var(--muted)', fontSize: 14, lineHeight: 1.75, marginBottom: 28 }}>
            Have a question, need help with your account, or just want to say hello?
            Our support team is happy to assist.
          </p>
          <div style={{
            display: 'inline-flex', alignItems: 'center', gap: 14,
            background: 'var(--card)', border: '1px solid var(--border)',
            borderRadius: 14, padding: '18px 28px',
          }}>
            <div style={{
              width: 44, height: 44, borderRadius: 10, flexShrink: 0,
              background: 'rgba(201,162,39,0.12)', border: '1px solid rgba(201,162,39,0.25)',
              display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 20,
            }}>
              📞
            </div>
            <div style={{ textAlign: 'left' }}>
              <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, color: 'var(--muted)', letterSpacing: '0.12em', marginBottom: 4 }}>
                CUSTOMER SUPPORT
              </div>
              <a href="tel:01616600938" style={{ fontFamily: 'Cinzel, serif', fontSize: 'clamp(16px, 4vw, 22px)', fontWeight: 700, color: 'var(--gold)', textDecoration: 'none', letterSpacing: '0.05em' }}>
                0161 660 0938
              </a>
            </div>
          </div>
        </div>
      </section>

      {/* ── Footer ───────────────────────────────────────────────────────── */}
      <footer style={{
        borderTop: '1px solid var(--border)', padding: '24px 32px',
        display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 12,
        color: 'var(--muted)', fontSize: 13,
      }}>
        <span style={{ fontFamily: 'Cinzel, serif', color: 'var(--gold)', fontSize: 13 }}>✦ Arcane Academy</span>
        <span>Built for lifelong learners</span>
        <div style={{ display: 'flex', gap: 20 }}>
          <Link to="/login"    style={{ color: 'var(--muted)', textDecoration: 'none', fontSize: 13 }}>Sign In</Link>
          <Link to="/register" style={{ color: 'var(--muted)', textDecoration: 'none', fontSize: 13 }}>Register</Link>
        </div>
      </footer>

    </div>
  )
}
