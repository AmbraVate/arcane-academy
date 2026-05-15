import { Link } from 'react-router-dom'

const FEATURES = [
  { icon: '🧠', title: 'Spaced Repetition', desc: 'Science-backed review scheduling keeps knowledge locked in long-term memory.' },
  { icon: '⚔️', title: 'Boss Battles', desc: 'Prove mastery at each tier. One wrong answer sends you back to study.' },
  { icon: '🐇', title: 'Rabbit Holes', desc: 'Spot a term that intrigues you? Save it and dive deep on your own schedule.' },
  { icon: '🏆', title: 'Polymath Ranks', desc: 'Earn XP across Java, SQL, React, and more. Rise from Novice to Lord Magus.' },
]

const TRACKS = [
  { glyph: '☕', name: 'Java', tiers: 'Foundation · Advanced · Practitioner · Expert', color: '#f89820' },
  { glyph: '🗃️', name: 'SQL', tiers: 'Foundation · Practitioner · Expert', color: '#7dd3fc' },
  { glyph: '⚛️', name: 'React', tiers: 'Foundation · Practitioner · Capstone', color: '#61dafb' },
  { glyph: '🎨', name: 'Tailwind CSS', tiers: 'Foundation · Practitioner · Expert', color: '#38bdf8' },
]

export default function LandingPage() {
  return (
    <div style={{ minHeight: '100vh', background: 'var(--bg)', color: 'var(--text)', fontFamily: 'var(--font-crimson-pro)' }}>

      {/* Nav bar */}
      <header style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '18px 32px', borderBottom: '1px solid var(--border)' }}>
        <span style={{ fontFamily: 'var(--font-cinzel)', fontSize: 18, color: 'var(--gold)', letterSpacing: '0.05em' }}>
          ✦ Arcane Academy
        </span>
        <div style={{ display: 'flex', gap: 12 }}>
          <Link to="/login" className="btn btn-ghost" style={{ fontSize: 14 }}>Sign In</Link>
          <Link to="/register" className="btn btn-primary" style={{ fontSize: 14 }}>Enroll Free</Link>
        </div>
      </header>

      {/* Hero */}
      <section style={{
        textAlign: 'center',
        padding: '96px 24px 72px',
        background: 'radial-gradient(ellipse 80% 50% at 50% 0%, rgba(139,92,246,0.18) 0%, transparent 70%)',
      }}>
        <p style={{ fontFamily: 'var(--font-cinzel)', color: 'var(--purple-light)', fontSize: 13, letterSpacing: '0.18em', marginBottom: 20 }}>
          THE POLYMATH'S PATH
        </p>
        <h1 style={{
          fontFamily: 'var(--font-cinzel)',
          fontSize: 'clamp(36px, 6vw, 64px)',
          fontWeight: 700,
          lineHeight: 1.15,
          margin: '0 auto 24px',
          maxWidth: 720,
          background: 'linear-gradient(135deg, var(--gold) 0%, var(--purple-light) 100%)',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
        }}>
          Master Multiple Skills.<br />Forget Nothing.
        </h1>
        <p style={{ fontSize: 18, color: 'var(--muted)', maxWidth: 560, margin: '0 auto 40px', lineHeight: 1.7 }}>
          Arcane Academy combines spaced repetition, retrieval practice, and RPG progression
          to make you genuinely fluent — not just momentarily confident.
        </p>
        <div style={{ display: 'flex', gap: 16, justifyContent: 'center', flexWrap: 'wrap' }}>
          <Link to="/register" className="btn btn-primary" style={{ padding: '14px 32px', fontSize: 16 }}>
            Begin Your Journey →
          </Link>
          <Link to="/login" className="btn btn-ghost" style={{ padding: '14px 28px', fontSize: 16 }}>
            Sign In
          </Link>
        </div>
      </section>

      {/* Tracks */}
      <section style={{ padding: '64px 24px', maxWidth: 960, margin: '0 auto' }}>
        <h2 style={{ fontFamily: 'var(--font-cinzel)', textAlign: 'center', fontSize: 22, marginBottom: 40, color: 'var(--gold)' }}>
          Active Learning Tracks
        </h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 16 }}>
          {TRACKS.map(t => (
            <div key={t.name} style={{
              background: 'var(--card)',
              border: '1px solid var(--border)',
              borderRadius: 12,
              padding: '24px 20px',
              textAlign: 'center',
            }}>
              <div style={{ fontSize: 36, marginBottom: 12 }}>{t.glyph}</div>
              <div style={{ fontFamily: 'var(--font-cinzel)', fontSize: 16, color: t.color, marginBottom: 8 }}>{t.name}</div>
              <div style={{ fontSize: 12, color: 'var(--muted)' }}>{t.tiers}</div>
            </div>
          ))}
        </div>
      </section>

      {/* Features */}
      <section style={{ padding: '64px 24px', background: 'var(--surface)' }}>
        <div style={{ maxWidth: 960, margin: '0 auto' }}>
          <h2 style={{ fontFamily: 'var(--font-cinzel)', textAlign: 'center', fontSize: 22, marginBottom: 40, color: 'var(--gold)' }}>
            Built for Polymaths
          </h2>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: 24 }}>
            {FEATURES.map(f => (
              <div key={f.title} style={{ display: 'flex', gap: 16, alignItems: 'flex-start' }}>
                <span style={{ fontSize: 28, flexShrink: 0 }}>{f.icon}</span>
                <div>
                  <div style={{ fontFamily: 'var(--font-cinzel)', fontSize: 14, color: 'var(--purple-light)', marginBottom: 6 }}>{f.title}</div>
                  <div style={{ fontSize: 14, color: 'var(--muted)', lineHeight: 1.6 }}>{f.desc}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section style={{ padding: '80px 24px', textAlign: 'center' }}>
        <h2 style={{ fontFamily: 'var(--font-cinzel)', fontSize: 24, marginBottom: 16, color: 'var(--text)' }}>
          Ready to become a polymath?
        </h2>
        <p style={{ color: 'var(--muted)', marginBottom: 32, fontSize: 16 }}>
          Free to start. No credit card required.
        </p>
        <Link to="/register" className="btn btn-primary" style={{ padding: '14px 40px', fontSize: 16 }}>
          Enroll in the Academy →
        </Link>
      </section>

      {/* Footer */}
      <footer style={{ borderTop: '1px solid var(--border)', padding: '24px 32px', textAlign: 'center', color: 'var(--muted)', fontSize: 13 }}>
        ✦ Arcane Academy · Built for lifelong learners
      </footer>
    </div>
  )
}
