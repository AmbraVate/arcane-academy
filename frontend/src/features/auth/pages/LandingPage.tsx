import { Link } from 'react-router-dom'

const ACTIVE_TRACKS = [
  { img: '/Java.png',            name: 'Java',             color: '#f89820', desc: 'Object-oriented programming, data structures, and design patterns.' },
  { img: '/React.png',           name: 'React',            color: '#61dafb', desc: 'Component-driven UIs, hooks, and modern state management.' },
  { img: '/TailwindCSS.png',     name: 'Tailwind CSS',     color: '#38bdf8', desc: 'Utility-first styling with a live browser preview sandbox.' },
  { img: '/Psychology.png',      name: 'Psychology',       color: '#c4b5fd', desc: 'Cognitive, social, and clinical psychology at degree level.' },
  { img: '/NaturalSciences.png', name: 'Natural Sciences', color: '#4ade80', desc: 'Biology, chemistry, and physics from first principles.' },
  { img: '/Genealogy.png',       name: 'Genealogy',        color: '#c9a227', desc: 'Family history research, records, and DNA analysis.' },
]

const COMING_SOON = [
  { img: '/SQL Developer.png', name: 'SQL' },
  { img: '/Javascript.png',    name: 'JavaScript' },
  { img: '/Typescript.png',    name: 'TypeScript' },
  { img: '/Python.png',        name: 'Python' },
  { img: '/HTML.png',          name: 'HTML' },
  { img: '/CSS.png',           name: 'CSS' },
]

const FEATURES = [
  {
    icon: '🔁',
    title: 'Spaced Repetition',
    desc: 'The SM-2 algorithm schedules every review at the exact moment your brain is about to forget — locking knowledge into long-term memory.',
  },
  {
    icon: '🐇',
    title: 'Rabbit Holes',
    desc: 'Spot a term that intrigues you mid-lesson? Save it instantly and dive deep on your own schedule.',
  },
  {
    icon: '🤖',
    title: 'AI Mentor',
    desc: 'An in-lesson AI mentor answers questions, grades written responses, and nudges you toward deeper understanding.',
  },
  {
    icon: '🏆',
    title: 'XP & Ranks',
    desc: 'Earn XP across every topic. Rise through Apprentice, Junior, Senior, and Lead — and climb the global leaderboard.',
  },
]

export default function LandingPage() {
  return (
    <div style={{ height: '100vh', overflowY: 'auto', background: 'var(--bg)', color: 'var(--text)', fontFamily: 'Crimson Pro, serif' }}>

      {/* ── Nav ──────────────────────────────────────────────────────────── */}
      <header style={{
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
        padding: '18px 32px', borderBottom: '1px solid var(--border)',
        position: 'sticky', top: 0, zIndex: 50,
        background: 'rgba(14,12,26,0.92)', backdropFilter: 'blur(10px)',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
          <img src="/Arcane.jpg" alt="Arcane Academy" style={{ width: 32, height: 32, borderRadius: 6, objectFit: 'cover' }} />
          <span style={{ fontFamily: 'Cinzel, serif', fontSize: 17, color: 'var(--gold)', letterSpacing: '0.05em' }}>
            Arcane Academy
          </span>
        </div>
        <div style={{ display: 'flex', gap: 10 }}>
          <Link to="/login" className="btn btn-ghost" style={{ fontSize: 13 }}>Sign In</Link>
          <Link to="/register" className="btn btn-primary" style={{ fontSize: 13 }}>Enroll Free</Link>
        </div>
      </header>

      {/* ── Hero ─────────────────────────────────────────────────────────── */}
      <section style={{
        textAlign: 'center',
        padding: '100px 24px 80px',
        background: 'radial-gradient(ellipse 80% 55% at 50% 0%, rgba(139,92,246,0.2) 0%, transparent 70%)',
      }}>
        <p style={{
          fontFamily: 'Cinzel, serif', color: 'var(--purple-light)',
          fontSize: 12, letterSpacing: '0.22em', marginBottom: 22, textTransform: 'uppercase',
        }}>
          The Polymath's Path
        </p>
        <h1 style={{
          fontFamily: 'Cinzel, serif',
          fontSize: 'clamp(34px, 5.5vw, 60px)',
          fontWeight: 700,
          lineHeight: 1.15,
          margin: '0 auto 24px',
          maxWidth: 680,
          background: 'linear-gradient(135deg, var(--gold) 0%, var(--purple-light) 100%)',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
        }}>
          Master Multiple Disciplines.<br />Forget Nothing.
        </h1>
        <p style={{ fontSize: 19, color: 'var(--muted)', maxWidth: 580, margin: '0 auto 44px', lineHeight: 1.75 }}>
          Arcane Academy uses spaced repetition, encoding phases, and an AI mentor
          to make you genuinely fluent across programming, sciences, and humanities.
        </p>
        <div style={{ display: 'flex', gap: 14, justifyContent: 'center', flexWrap: 'wrap' }}>
          <Link to="/register" className="btn btn-primary" style={{ padding: '14px 34px', fontSize: 15 }}>
            Begin Your Journey →
          </Link>
          <Link to="/login" className="btn btn-ghost" style={{ padding: '14px 28px', fontSize: 15 }}>
            Sign In
          </Link>
        </div>
      </section>

      {/* ── Active Tracks ────────────────────────────────────────────────── */}
      <section style={{ padding: '72px 24px 48px', maxWidth: 1040, margin: '0 auto' }}>
        <h2 style={{ fontFamily: 'Cinzel, serif', textAlign: 'center', fontSize: 21, marginBottom: 8, color: 'var(--gold)' }}>
          Active Learning Tracks
        </h2>
        <p style={{ textAlign: 'center', color: 'var(--muted)', fontSize: 15, marginBottom: 44 }}>
          71 sub-chunks per topic · Apprentice → Junior → Senior → Lead
        </p>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 18 }}>
          {ACTIVE_TRACKS.map(t => (
            <div key={t.name} style={{
              background: 'var(--card)',
              border: '1px solid var(--border)',
              borderRadius: 14,
              padding: '28px 24px',
              display: 'flex',
              alignItems: 'flex-start',
              gap: 20,
              transition: 'border-color 0.2s, box-shadow 0.2s',
            }}
              onMouseEnter={e => {
                (e.currentTarget as HTMLDivElement).style.borderColor = t.color + '55'
                ;(e.currentTarget as HTMLDivElement).style.boxShadow = `0 0 20px ${t.color}18`
              }}
              onMouseLeave={e => {
                (e.currentTarget as HTMLDivElement).style.borderColor = 'var(--border)'
                ;(e.currentTarget as HTMLDivElement).style.boxShadow = 'none'
              }}
            >
              <img
                src={t.img}
                alt={t.name}
                style={{ width: 52, height: 52, objectFit: 'contain', flexShrink: 0, borderRadius: 8 }}
              />
              <div>
                <div style={{ fontFamily: 'Cinzel, serif', fontSize: 15, color: t.color, marginBottom: 6 }}>{t.name}</div>
                <div style={{ fontSize: 14, color: 'var(--muted)', lineHeight: 1.6 }}>{t.desc}</div>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* ── Coming Soon ──────────────────────────────────────────────────── */}
      <section style={{ padding: '0 24px 72px', maxWidth: 1040, margin: '0 auto' }}>
        <h3 style={{ fontFamily: 'Cinzel, serif', textAlign: 'center', fontSize: 16, marginBottom: 28, color: 'var(--muted)', letterSpacing: '0.08em' }}>
          Coming Soon
        </h3>
        <div style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap', gap: 16 }}>
          {COMING_SOON.map(t => (
            <div key={t.name} style={{
              display: 'flex', alignItems: 'center', gap: 10,
              background: 'var(--surface)', border: '1px solid var(--border)',
              borderRadius: 10, padding: '12px 18px', opacity: 0.5,
            }}>
              <img src={t.img} alt={t.name} style={{ width: 28, height: 28, objectFit: 'contain', borderRadius: 4 }} />
              <span style={{ fontFamily: 'Cinzel, serif', fontSize: 13, color: 'var(--muted)' }}>{t.name}</span>
            </div>
          ))}
        </div>
      </section>

      {/* ── Features ─────────────────────────────────────────────────────── */}
      <section style={{ padding: '72px 24px', background: 'var(--surface)' }}>
        <div style={{ maxWidth: 960, margin: '0 auto' }}>
          <h2 style={{ fontFamily: 'Cinzel, serif', textAlign: 'center', fontSize: 21, marginBottom: 48, color: 'var(--gold)' }}>
            Built for Polymaths
          </h2>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: 32 }}>
            {FEATURES.map(f => (
              <div key={f.title} style={{ display: 'flex', gap: 18, alignItems: 'flex-start' }}>
                <span style={{ fontSize: 30, flexShrink: 0, lineHeight: 1 }}>{f.icon}</span>
                <div>
                  <div style={{ fontFamily: 'Cinzel, serif', fontSize: 13, color: 'var(--purple-light)', marginBottom: 8, letterSpacing: '0.04em' }}>{f.title}</div>
                  <div style={{ fontSize: 14, color: 'var(--muted)', lineHeight: 1.7 }}>{f.desc}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── CTA ──────────────────────────────────────────────────────────── */}
      <section style={{ padding: '88px 24px', textAlign: 'center' }}>
        <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 26, marginBottom: 16, color: 'var(--text)' }}>
          Ready to become a polymath?
        </h2>
        <p style={{ color: 'var(--muted)', marginBottom: 36, fontSize: 17, lineHeight: 1.6 }}>
          Free to start. No credit card required.
        </p>
        <Link to="/register" className="btn btn-primary" style={{ padding: '15px 44px', fontSize: 15 }}>
          Enroll in the Academy →
        </Link>
      </section>

      {/* ── Footer ───────────────────────────────────────────────────────── */}
      <footer style={{ borderTop: '1px solid var(--border)', padding: '24px 32px', textAlign: 'center', color: 'var(--muted)', fontSize: 13 }}>
        ✦ Arcane Academy · Built for lifelong learners
      </footer>

    </div>
  )
}
