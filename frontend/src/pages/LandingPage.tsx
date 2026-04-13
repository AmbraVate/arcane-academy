import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import styles from './LandingPage.module.css'

const PILLARS = [
  {
    icon: '🧩',
    name: 'Chunking',
    desc: 'Knowledge broken into tight, meaningful units — each one a stepping stone in a larger graph.',
  },
  {
    icon: '⚡',
    name: 'Encoding',
    desc: 'Multi-phase delivery: hook → story → guided practice. You don\'t just read — you do.',
  },
  {
    icon: '🎯',
    name: 'Active Recall',
    desc: 'Tiered questions force your brain to retrieve, not recognise. Retrieval beats re-reading by 3×.',
  },
  {
    icon: '🔀',
    name: 'Interleaving',
    desc: 'Mixed practice across topics trains transfer. Blocked practice creates illusions of competence.',
  },
  {
    icon: '📅',
    name: 'Spaced Repetition',
    desc: 'SM-2 algorithm schedules every concept at the optimal moment — right before you forget.',
  },
  {
    icon: '🪶',
    name: 'Feynman Technique',
    desc: 'Explain it in plain language. If you can\'t teach it, you don\'t know it yet.',
  },
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
  const ctaDest = user ? '/' : '/login'

  return (
    <div className={styles.page}>
      {/* ── Nav bar ─────────────────────────────────────────── */}
      <header className={styles.header}>
        <div className={styles.brand}>✦ Arcane Academy</div>
        <div className={styles.headerRight}>
          {user ? (
            <button className="btn btn-primary" onClick={() => navigate('/')}>
              Dashboard
            </button>
          ) : (
            <>
              <button className="btn btn-ghost" onClick={() => navigate('/login')}>
                Login
              </button>
              <button className="btn btn-primary" onClick={() => navigate('/register')}>
                Sign Up Free
              </button>
            </>
          )}
        </div>
      </header>

      {/* ── Hero ────────────────────────────────────────────── */}
      <section className={styles.hero}>
        <div className={styles.heroGlow} />
        <div className={styles.heroInner}>
          <div className={styles.badge}>🔬 Science-backed accelerated learning</div>
          <h1 className={styles.heroTitle}>
            Train your mind like<br />
            <span className={styles.heroAccent}>the great polymaths did.</span>
          </h1>
          <p className={styles.heroSub}>
            Arcane Academy combines six research-proven techniques into a single adaptive
            learning system — forging polymaths, one skill at a time.
          </p>
          <div className={styles.heroCta}>
            <button className={`btn btn-primary ${styles.ctaBtn}`} onClick={() => navigate(ctaDest)}>
              {ctaLabel}
            </button>
            {!user && (
              <span className={styles.ctaNote}>Free. No credit card required.</span>
            )}
          </div>

          {/* Stats strip */}
          <div className={styles.statsStrip}>
            <div className={styles.stat}>
              <span className={styles.statNum}>6×</span>
              <span className={styles.statLbl}>faster retention vs passive reading</span>
            </div>
            <div className={styles.statDivider} />
            <div className={styles.stat}>
              <span className={styles.statNum}>11</span>
              <span className={styles.statLbl}>knowledge chunks per track</span>
            </div>
            <div className={styles.statDivider} />
            <div className={styles.stat}>
              <span className={styles.statNum}>SM-2</span>
              <span className={styles.statLbl}>spaced repetition algorithm</span>
            </div>
          </div>
        </div>
      </section>

      {/* ── What is a Polymath ───────────────────────────────── */}
      <section className={styles.section}>
        <div className={styles.sectionInner}>
          <div className={styles.arcaneCard}>
            <div className={styles.arcaneQuote}>
              "A person of wide knowledge or learning."
            </div>
            <div className={styles.arcaneBody}>
              <p>
                Da Vinci mastered painting, anatomy, and engineering. Leibniz invented calculus and
                philosophy simultaneously. Lovelace wove mathematics and poetry into the world's
                first algorithm.
              </p>
              <p>
                They weren't born with superhuman memory. They used structured, deliberate methods
                to encode knowledge deeply — the same methods science has since confirmed as optimal.
              </p>
              <p>
                <strong className={styles.highlight}>Arcane Academy brings those methods to you.</strong>
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* ── 6 Pillars ───────────────────────────────────────── */}
      <section className={styles.section}>
        <div className={styles.sectionInner}>
          <div className={styles.sectionLabel}>The Method</div>
          <h2 className={styles.sectionTitle}>Six pillars of accelerated learning</h2>
          <p className={styles.sectionSub}>
            Every lesson is engineered around cognitive science. Not one technique — all six, working together.
          </p>
          <div className={styles.pillarsGrid}>
            {PILLARS.map((p, i) => (
              <div key={i} className={styles.pillarCard}>
                <div className={styles.pillarIcon}>{p.icon}</div>
                <div className={styles.pillarName}>{p.name}</div>
                <div className={styles.pillarDesc}>{p.desc}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── First Track ─────────────────────────────────────── */}
      <section className={styles.section}>
        <div className={styles.sectionInner}>
          <div className={styles.sectionLabel}>First Track</div>
          <h2 className={styles.sectionTitle}>Java — from zero to job-ready</h2>
          <p className={styles.sectionSub}>
            Our inaugural track takes a complete beginner through every concept needed for
            a first Apprenticeship or Junior Developer role.
          </p>
          <div className={styles.trackPath}>
            {TRACK_STEPS.map((step, i) => (
              <div key={step.num} className={styles.trackStep}>
                <div className={styles.trackNode}>
                  <span className={styles.trackLetter}>{step.num}</span>
                </div>
                <div className={styles.trackLabel}>{step.label}</div>
                {i < TRACK_STEPS.length - 1 && <div className={styles.trackConnector} />}
              </div>
            ))}
          </div>
          <p className={styles.trackNote}>
            Complete the entry diagnostic to skip what you already know and start exactly where you should.
          </p>
        </div>
      </section>

      {/* ── How it works ────────────────────────────────────── */}
      <section className={styles.section}>
        <div className={styles.sectionInner}>
          <div className={styles.sectionLabel}>The Experience</div>
          <h2 className={styles.sectionTitle}>Learning that feels like an adventure</h2>
          <div className={styles.howGrid}>
            <div className={styles.howCard}>
              <div className={styles.howNum}>01</div>
              <div className={styles.howTitle}>Hook</div>
              <p className={styles.howDesc}>
                Every concept opens with a story hook — a question, a puzzle, a surprise
                that primes your brain to receive the new knowledge.
              </p>
            </div>
            <div className={styles.howCard}>
              <div className={styles.howNum}>02</div>
              <div className={styles.howTitle}>Guided by Eldrin</div>
              <p className={styles.howDesc}>
                Archmage Eldrin narrates each lesson with vivid analogies, worked examples,
                and the wisdom of someone who has taught this for a thousand years.
              </p>
            </div>
            <div className={styles.howCard}>
              <div className={styles.howNum}>03</div>
              <div className={styles.howTitle}>Code in the Academy</div>
              <p className={styles.howDesc}>
                Real Java, compiled and run instantly. Guided practice with test feedback
                before you're asked to do it alone.
              </p>
            </div>
            <div className={styles.howCard}>
              <div className={styles.howNum}>04</div>
              <div className={styles.howTitle}>Prove It</div>
              <p className={styles.howDesc}>
                Retrieval checks, daily reviews, and Feynman explanations ensure the
                knowledge is truly yours — not just recently seen.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* ── Final CTA ───────────────────────────────────────── */}
      <section className={styles.finalCta}>
        <div className={styles.finalCtaGlow} />
        <div className={styles.finalCtaInner}>
          <div className={styles.finalCtaGlyph}>🧙</div>
          <h2 className={styles.finalCtaTitle}>Your grimoire awaits.</h2>
          <p className={styles.finalCtaSub}>
            Join the Academy. Master Java. Become the developer you were meant to be.
          </p>
          <button className={`btn btn-primary ${styles.ctaBtn}`} onClick={() => navigate(ctaDest)}>
            {ctaLabel}
          </button>
        </div>
      </section>

      {/* ── Footer ──────────────────────────────────────────── */}
      <footer className={styles.footer}>
        <span>✦ Arcane Academy</span>
        <span className={styles.footerMuted}>Built on science. Wrapped in story.</span>
      </footer>
    </div>
  )
}
