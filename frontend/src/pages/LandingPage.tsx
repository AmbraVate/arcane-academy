import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { Button } from '@/components/ui/button'

const PILLARS = [
  { icon: '🧩', name: 'Chunking',          desc: 'Knowledge broken into tight, meaningful units — each one a stepping stone in a larger graph.' },
  { icon: '⚡', name: 'Encoding',           desc: "Multi-phase delivery: hook → story → guided practice. You don't just read — you do." },
  { icon: '🎯', name: 'Active Recall',      desc: 'Tiered questions force your brain to retrieve, not recognise. Retrieval beats re-reading by 3×.' },
  { icon: '🔀', name: 'Interleaving',       desc: 'Mixed practice across topics trains transfer. Blocked practice creates illusions of competence.' },
  { icon: '📅', name: 'Spaced Repetition',  desc: 'SM-2 algorithm schedules every concept at the optimal moment — right before you forget.' },
  { icon: '🪶', name: 'Feynman Technique',  desc: "Explain it in plain language. If you can't teach it, you don't know it yet." },
]

const TRACK_STEPS = [
  { num: 'A', label: 'Variables & Types' },
  { num: 'B', label: 'Control Flow' },
  { num: 'C', label: 'Loops' },
  { num: 'D', label: 'Methods' },
  { num: 'E', label: 'Arrays & Collections' },
  { num: 'F', label: 'Classes & Objects' },
  { num: 'G', label: 'Inheritance' },
  { num: 'H', label: 'Interfaces' },
  { num: 'I', label: 'Polymorphism' },
  { num: 'J', label: 'Exceptions' },
  { num: 'K', label: 'Core APIs' },
]

export default function LandingPage() {
  const navigate = useNavigate()
  const { user } = useAuth()
  const ctaLabel = user ? 'Continue Learning →' : 'Begin Your Journey →'
  const ctaDest  = user ? '/' : '/login'

  return (
    <div className="min-h-full overflow-y-auto bg-bg text-text font-crimson">
      {/* Header */}
      <header className="sticky top-0 z-[100] flex items-center justify-between px-12 py-4 bg-[rgba(13,11,26,0.85)] backdrop-blur-[12px] border-b border-border max-[768px]:px-5 max-[768px]:py-3.5">
        <div className="font-cinzel text-[18px] text-gold tracking-[0.04em]">✦ Arcane Academy</div>
        <div className="flex gap-2.5 items-center">
          {user ? (
            <Button variant="primary" onClick={() => navigate('/')}>Dashboard</Button>
          ) : (
            <>
              <Button variant="ghost" onClick={() => navigate('/login')}>Login</Button>
              <Button variant="primary" onClick={() => navigate('/register')}>Sign Up Free</Button>
            </>
          )}
        </div>
      </header>

      {/* Hero */}
      <section className="relative overflow-hidden px-12 pt-[100px] pb-20 text-center max-[768px]:px-5 max-[768px]:pt-[72px] max-[768px]:pb-14">
        <div className="absolute top-[-180px] left-1/2 -translate-x-1/2 w-[900px] h-[600px] pointer-events-none"
          style={{ background: 'radial-gradient(ellipse at center, rgba(139,92,246,0.22) 0%, transparent 70%)' }} />
        <div className="relative max-w-[780px] mx-auto animate-[fade-up_0.7s_ease_both]">
          <div className="inline-block font-cinzel text-[11px] tracking-[0.08em] text-teal bg-[rgba(45,212,191,0.08)] border border-[rgba(45,212,191,0.25)] rounded-[20px] px-4 py-[5px] mb-7">
            🔬 Science-backed accelerated learning
          </div>
          <h1 className="font-cinzel font-bold leading-[1.1] text-text mb-6" style={{ fontSize: 'clamp(36px, 5vw, 64px)' }}>
            Train your mind like<br />
            <span style={{
              background: 'linear-gradient(135deg, var(--gold) 0%, var(--purple-light) 100%)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
              backgroundClip: 'text',
            }}>
              the great polymaths did.
            </span>
          </h1>
          <p className="text-[20px] text-muted leading-[1.7] max-w-[580px] mx-auto mb-9">
            Arcane Academy combines six research-proven techniques into a single adaptive
            learning system — forging polymaths, one skill at a time.
          </p>
          <div className="flex flex-col items-center gap-2.5 mb-14">
            <Button variant="primary" className="font-cinzel text-[16px] px-10 py-3.5 rounded-[8px] tracking-[0.04em]" onClick={() => navigate(ctaDest)}>
              {ctaLabel}
            </Button>
            {!user && <span className="text-[13px] text-muted">Free. No credit card required.</span>}
          </div>

          {/* Stats strip */}
          <div className="flex items-center justify-center bg-card border border-border rounded-[12px] px-8 py-5 max-[768px]:flex-col max-[768px]:gap-4 max-[768px]:px-5">
            {[
              { num: '6×',  lbl: 'faster retention vs passive reading' },
              null,
              { num: '11',  lbl: 'knowledge chunks per track' },
              null,
              { num: 'SM-2', lbl: 'spaced repetition algorithm' },
            ].map((item, i) =>
              item === null
                ? <div key={i} className="w-px h-12 bg-border mx-0 max-[768px]:w-4/5 max-[768px]:h-px" />
                : <div key={i} className="flex flex-col items-center gap-1 px-7">
                    <span className="font-cinzel text-[28px] font-bold text-gold">{item.num}</span>
                    <span className="text-[12px] text-muted text-center max-w-[120px] leading-[1.4]">{item.lbl}</span>
                  </div>
            )}
          </div>
        </div>
      </section>

      {/* Polymath section */}
      <section className="px-12 py-20 bg-surface max-[768px]:px-5 max-[768px]:py-14">
        <div className="max-w-[960px] mx-auto">
          <div className="bg-card border border-border border-l-4 border-l-purple rounded-[12px] px-12 py-10 max-w-[780px] mx-auto max-[768px]:px-6 max-[768px]:py-7">
            <div className="font-cinzel text-[22px] text-gold mb-7 italic">
              "A person of wide knowledge or learning."
            </div>
            <div className="space-y-4 text-[17px] text-muted leading-[1.8]">
              <p>Da Vinci mastered painting, anatomy, and engineering. Leibniz invented calculus and philosophy simultaneously. Lovelace wove mathematics and poetry into the world's first algorithm.</p>
              <p>They weren't born with superhuman memory. They used structured, deliberate methods to encode knowledge deeply — the same methods science has since confirmed as optimal.</p>
              <p><strong className="text-purple-light not-italic">Arcane Academy brings those methods to you.</strong></p>
            </div>
          </div>
        </div>
      </section>

      {/* Six Pillars */}
      <section className="px-12 py-20 max-[768px]:px-5 max-[768px]:py-14">
        <div className="max-w-[960px] mx-auto">
          <div className="font-cinzel text-[11px] tracking-[0.12em] text-teal uppercase mb-3">The Method</div>
          <h2 className="font-cinzel text-gold mb-4 leading-[1.2]" style={{ fontSize: 'clamp(24px, 3vw, 36px)' }}>
            Six pillars of accelerated learning
          </h2>
          <p className="text-[17px] text-muted leading-[1.7] max-w-[620px] mb-12">
            Every lesson is engineered around cognitive science. Not one technique — all six, working together.
          </p>
          <div className="grid grid-cols-3 gap-4 max-[768px]:grid-cols-2 max-[480px]:grid-cols-1">
            {PILLARS.map((p, i) => (
              <div key={i} className="bg-card border border-border rounded-[12px] px-6 py-7 transition-[border-color,transform] duration-200 hover:border-purple hover:-translate-y-[3px]">
                <div className="text-[32px] mb-3">{p.icon}</div>
                <div className="font-cinzel text-[15px] text-gold mb-2.5">{p.name}</div>
                <div className="text-[14px] text-muted leading-[1.6]">{p.desc}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Track path */}
      <section className="px-12 py-20 bg-surface max-[768px]:px-5 max-[768px]:py-14">
        <div className="max-w-[960px] mx-auto">
          <div className="font-cinzel text-[11px] tracking-[0.12em] text-teal uppercase mb-3">First Track</div>
          <h2 className="font-cinzel text-gold mb-4 leading-[1.2]" style={{ fontSize: 'clamp(24px, 3vw, 36px)' }}>
            Java — from zero to job-ready
          </h2>
          <p className="text-[17px] text-muted leading-[1.7] max-w-[620px] mb-12">
            Our inaugural track takes a complete beginner through every concept needed for a first Apprenticeship or Junior Developer role.
          </p>
          <div className="flex flex-wrap items-start mb-6 max-[480px]:gap-1">
            {TRACK_STEPS.map((step, i) => (
              <div key={step.num} className="flex flex-col items-center flex-shrink-0 relative">
                <div className="w-12 h-12 rounded-full bg-purple-dim border-2 border-purple flex items-center justify-center mb-2 relative z-[1]">
                  <span className="font-cinzel text-[16px] text-purple-light font-bold">{step.num}</span>
                </div>
                <div className="text-[11px] text-muted text-center max-w-[72px] leading-[1.3]">{step.label}</div>
                {i < TRACK_STEPS.length - 1 && (
                  <div
                    className="w-8 h-0.5 self-center mb-7 flex-shrink-0 absolute top-6 left-full max-[768px]:w-5"
                    style={{ background: 'linear-gradient(90deg, var(--purple), var(--purple-dim))' }}
                  />
                )}
              </div>
            ))}
          </div>
          <p className="text-[14px] text-muted italic text-center">
            Complete the entry diagnostic to skip what you already know and start exactly where you should.
          </p>
        </div>
      </section>

      {/* How it works */}
      <section className="px-12 py-20 max-[768px]:px-5 max-[768px]:py-14">
        <div className="max-w-[960px] mx-auto">
          <div className="font-cinzel text-[11px] tracking-[0.12em] text-teal uppercase mb-3">The Experience</div>
          <h2 className="font-cinzel text-gold mb-12 leading-[1.2]" style={{ fontSize: 'clamp(24px, 3vw, 36px)' }}>
            Learning that feels like an adventure
          </h2>
          <div className="grid grid-cols-2 gap-4 max-[768px]:grid-cols-1">
            {[
              { num: '01', title: 'Hook', desc: 'Every concept opens with a story hook — a question, a puzzle, a surprise that primes your brain to receive the new knowledge.' },
              { num: '02', title: 'Guided by Eldrin', desc: 'Archmage Eldrin narrates each lesson with vivid analogies, worked examples, and the wisdom of someone who has taught this for a thousand years.' },
              { num: '03', title: 'Code in the Academy', desc: 'Real Java, compiled and run instantly. Guided practice with test feedback before you\'re asked to do it alone.' },
              { num: '04', title: 'Prove It', desc: 'Retrieval checks, daily reviews, and Feynman explanations ensure the knowledge is truly yours — not just recently seen.' },
            ].map(card => (
              <div key={card.num} className="bg-card border border-border rounded-[12px] px-7 py-8">
                <div className="font-cinzel text-[11px] text-teal tracking-[0.1em] mb-3">{card.num}</div>
                <div className="font-cinzel text-[18px] text-gold mb-3">{card.title}</div>
                <p className="text-[15px] text-muted leading-[1.7]">{card.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Final CTA */}
      <section className="relative overflow-hidden px-12 py-[100px] text-center bg-surface max-[768px]:px-5 max-[768px]:py-[72px]">
        <div className="absolute bottom-[-200px] left-1/2 -translate-x-1/2 w-[800px] h-[600px] pointer-events-none"
          style={{ background: 'radial-gradient(ellipse at center, rgba(139,92,246,0.18) 0%, transparent 70%)' }} />
        <div className="relative max-w-[540px] mx-auto">
          <div className="text-[56px] mb-5">🧙</div>
          <h2 className="font-cinzel text-[40px] text-gold mb-4">Your grimoire awaits.</h2>
          <p className="text-[18px] text-muted leading-[1.7] mb-9">
            Join the Academy. Master Java. Become the developer you were meant to be.
          </p>
          <Button variant="primary" className="font-cinzel text-[16px] px-10 py-3.5 rounded-[8px] tracking-[0.04em]" onClick={() => navigate(ctaDest)}>
            {ctaLabel}
          </Button>
        </div>
      </section>

      {/* Footer */}
      <footer className="flex items-center justify-between px-12 py-6 border-t border-border font-cinzel text-[13px] text-gold max-[768px]:flex-col max-[768px]:gap-2 max-[768px]:px-5 max-[768px]:py-5 max-[768px]:text-center">
        <span>✦ Arcane Academy</span>
        <span className="font-crimson text-muted text-[13px]">Built on science. Wrapped in story.</span>
      </footer>
    </div>
  )
}
