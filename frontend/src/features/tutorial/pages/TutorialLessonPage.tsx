import { safe } from '@/lib/sanitize'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTutorial } from '../context/TutorialContext'
import { ArrowLeft, Check, Eye, EyeOff, Target, Zap, CheckCircle2, Lightbulb } from 'lucide-react'
import { cn } from '@/lib/utils'

// ── Demo lesson: "The Art of Origami" ─────────────────────────────────────────
// Topic is deliberately unaffiliated with any school. Covers all 7 phases so
// learners see the complete lesson structure before starting a real pathway.

const LESSON_TITLE = 'The Art of Origami - How Folding Creates Form'

const PHASES = [
  'HOOK', 'EXPLANATION', 'GUIDED_PRACTICE',
  'SOLO_PRACTICE', 'RETRIEVAL_CHECK', 'INTEGRATION', 'COMPLETE',
] as const
type Phase = typeof PHASES[number]

const PHASE_LABELS: Record<Phase, string> = {
  HOOK: 'Hook', EXPLANATION: 'Explanation', GUIDED_PRACTICE: 'Practice',
  SOLO_PRACTICE: 'Solo', RETRIEVAL_CHECK: 'Retrieval',
  INTEGRATION: 'Integration', COMPLETE: 'Complete',
}

// ── Content ───────────────────────────────────────────────────────────────────

const HOOK_HTML = `
<p>A single sheet of paper - flat, featureless, unassuming.</p>
<p>In the hands of a master, it becomes a crane. A butterfly. A dragon that spans a metre across.</p>
<p>No glue. No scissors. Only folding.</p>
<p>The ancient Japanese art of origami is more than craft. It's a window into a deep truth: <strong>form emerges from constraint</strong>. The rules are few. The possibilities are boundless.</p>
<p>What does it take to turn a blank sheet into something remarkable?</p>
`

const LEARNING_OBJECTIVES = [
  'Identify the two fundamental fold types in origami: valley folds and mountain folds',
  'Explain how a crease pattern encodes the complete structure of a finished model',
  'Describe the mathematical principle behind flat-foldability (Maekawa\'s theorem)',
]

const EXPLANATION_HTML = `
<h3>The Two Fundamental Folds</h3>
<p>Every origami model - no matter how complex - is built from just two basic fold types:</p>
<ul>
  <li><strong>Valley fold</strong> - the paper folds <em>towards you</em>, creating a valley shape (∨).</li>
  <li><strong>Mountain fold</strong> - the paper folds <em>away from you</em>, creating a ridge (∧).</li>
</ul>
<p>That's it. Two operations. Combined in different sequences, they produce everything from a simple boat to a thousand-piece tessellation.</p>
<h3>Crease Patterns</h3>
<p>When origami masters design a new model, they work backwards. They start with the finished form and map out every fold as a <strong>crease pattern</strong> - a flat diagram showing where every fold will go, using dashed lines for valley folds and dot-dash lines for mountain folds.</p>
<p>A crease pattern looks nothing like the finished model. Yet it contains its <em>entire blueprint</em>. An experienced folder can reconstruct any design from its crease pattern alone - like reading a musical score and hearing the symphony.</p>
<h3>The Mathematics of Flat-Foldability</h3>
<p>In 1994, mathematician Jun Maekawa discovered a rule now called <strong>Maekawa's theorem</strong>: at any interior vertex of a flat-foldable crease pattern, the number of mountain and valley folds must differ by exactly two.</p>
<p>This means origami has provable rules. Paper-folding is secretly a branch of geometry.</p>
`

const WHY_IT_MATTERS_HTML = `
<p>Origami principles are used in <strong>space engineering</strong> - NASA has used origami-inspired designs to fold solar panels and telescope mirrors for launch. In <strong>medicine</strong>, origami guides how stents are deployed inside arteries. In <strong>robotics</strong>, self-folding materials that assemble themselves are an active field of research.</p>
<p>Understanding how constraint generates form gives you a design principle that shows up everywhere.</p>
`

const MENTAL_MODEL_HTML = `
<p>Think of origami as a language. The alphabet has two letters: valley and mountain. Every word is a crease sequence. Every poem is a finished model.</p>
<p>Mastery means fluency in that language - seeing a blank sheet and knowing exactly which sequence of two letters will produce a crane.</p>
`

const MINI_SUMMARY_HTML = `
<ul>
  <li>All origami uses only two folds: <strong>valley</strong> (towards you, ∨) and <strong>mountain</strong> (away from you, ∧).</li>
  <li>A <strong>crease pattern</strong> is the complete flat blueprint of a finished model.</li>
  <li><strong>Maekawa's theorem</strong>: at every interior vertex, mountain and valley counts differ by exactly 2.</li>
</ul>
`

const GUIDED_TASK = 'Using only the concepts from this lesson: in 2-3 sentences, explain how someone could use a crease pattern to fold a model they have never seen before.'

const GUIDED_MODEL_ANSWER = `A crease pattern is a flat diagram containing the complete blueprint of a finished origami model. Each line in the pattern indicates either a valley fold (fold towards you) or a mountain fold (fold away from you). By following the crease pattern fold-by-fold - applying the correct fold type at each line in sequence - a folder can reproduce any model, even one they have never seen, as long as they can read the two fold symbols.`

const SOLO_TASK = 'Without looking back - in your own words, explain: what are the two fundamental fold types in origami, and what does Maekawa\'s theorem say about how they appear at any interior vertex of a flat-foldable model?'

const RETRIEVAL_QUESTIONS = [
  {
    text: 'Which description correctly defines a valley fold in origami?',
    options: [
      'The paper folds away from you, creating a ridge shape',
      'The paper folds towards you, creating a V shape',
      'The paper is cut along a crease line',
      'The paper is glued at an intersection point',
    ],
    correct: 1,
  },
  {
    text: 'What does a crease pattern represent?',
    options: [
      'A photograph of the finished model taken from above',
      'The final colour scheme of the folded paper',
      'A flat diagram showing every fold needed to create a model',
      'A list of tools required for folding',
    ],
    correct: 2,
  },
  {
    text: 'Maekawa\'s theorem states that at any interior vertex of a flat-foldable crease pattern...',
    options: [
      'All folds must be valley folds',
      'The number of mountain and valley folds must differ by exactly two',
      'There must be exactly four creases meeting at that point',
      'The creases must form a right angle',
    ],
    correct: 1,
  },
]

const INTEGRATION_HTML = `
<p>Origami's core insight - that just <em>two rules</em> (valley and mountain) can generate unlimited complexity when combined - is a pattern that appears across many fields.</p>
<p>Computer scientists call this the power of <strong>combinatorial systems</strong>: a small set of rules, iterated in different orders and combinations, produces vast possibility spaces.</p>
<ul>
  <li>The 26 letters of the English alphabet {'->'} millions of books</li>
  <li>The two states of binary (0 and 1) {'->'} every digital image, song, and program</li>
  <li>The four base pairs of DNA (A, T, C, G) {'->'} every living organism on Earth</li>
</ul>
<p>Where else have you noticed this pattern of <strong>"few rules, infinite outcomes"</strong> in the world around you?</p>
`

// ── Helper: Tutorial tip callout ──────────────────────────────────────────────

function TutorialTip({ children, color = 'purple' }: { children: React.ReactNode; color?: 'purple' | 'teal' | 'gold' }) {
  const styles = {
    purple: { bg: 'rgba(139,92,246,0.08)', border: 'rgba(139,92,246,0.2)', label: 'text-purple-light' },
    teal:   { bg: 'rgba(45,212,191,0.08)', border: 'rgba(45,212,191,0.2)', label: 'text-teal' },
    gold:   { bg: 'rgba(201,162,39,0.08)', border: 'rgba(201,162,39,0.2)', label: 'text-gold' },
  }
  const s = styles[color]
  return (
    <div className="mb-5 px-4 py-3 rounded-[10px]" style={{ background: s.bg, border: `1px solid ${s.border}` }}>
      <div className={cn('text-[11px] font-bold uppercase tracking-[0.1em] mb-1.5', s.label)}>Book TUTORIAL TIP</div>
      <div className="text-[13px] text-muted leading-[1.65]">{children}</div>
    </div>
  )
}

// ── Common prose class (matches EncodingPage exactly) ─────────────────────────

const prose = [
  'text-[15px] leading-[1.8] text-text my-6',
  '[&_p]:m-0 [&_p]:mb-4 [&_p:last-child]:mb-0',
  '[&_strong]:text-gold [&_strong]:font-semibold [&_em]:text-purple-light [&_em]:italic',
  '[&_h3]:text-[17px] [&_h3]:font-bold [&_h3]:text-gold [&_h3]:mt-7 [&_h3]:mb-2.5 [&_h3]:pb-1.5 [&_h3]:border-b [&_h3]:border-[rgba(255,193,7,0.15)]',
  '[&_ul]:pl-5 [&_ul]:mb-4 [&_li]:mb-2 [&_li]:leading-[1.65] [&_li::marker]:text-purple-light',
].join(' ')

// ── Page ──────────────────────────────────────────────────────────────────────

export default function TutorialLessonPage() {
  const navigate   = useNavigate()
  const { complete } = useTutorial()

  const [phase, setPhase]                     = useState<Phase>('HOOK')
  const [guidedAnswer, setGuidedAnswer]       = useState('')
  const [guidedSubmitted, setGuidedSubmitted] = useState(false)
  const [soloAnswer, setSoloAnswer]           = useState('')
  const [soloSubmitted, setSoloSubmitted]     = useState(false)
  const [showHint, setShowHint]               = useState(false)
  const [selected, setSelected]               = useState<Record<number, number>>({})
  const [retrievalDone, setRetrievalDone]     = useState(false)
  const [score, setScore]                     = useState(0)

  function advance() {
    const idx = PHASES.indexOf(phase)
    if (idx < PHASES.length - 1) setPhase(PHASES[idx + 1])
  }

  function submitRetrieval() {
    const correct = RETRIEVAL_QUESTIONS.filter((q, i) => selected[i] === q.correct).length
    setScore(correct)
    setRetrievalDone(true)
  }

  function finishTutorial() {
    complete()
    navigate('/schools')
  }

  const currentOrder = PHASES.indexOf(phase)
  const phaseOrder   = (p: Phase) => PHASES.indexOf(p)

  return (
    <div className="flex flex-col flex-1 overflow-hidden min-h-0">

      {/* ── Header (matches EncodingPage exactly) ── */}
      <div className="flex items-center gap-3 px-4 py-2.5 border-b border-border bg-card flex-shrink-0 max-[480px]:px-2.5 max-[480px]:py-2 max-[480px]:gap-2">
        <button
          className="btn btn-ghost text-[12px] flex items-center gap-1"
          onClick={() => navigate('/schools')}
        >
          <ArrowLeft size={13} strokeWidth={1.75} /> Exit Demo
        </button>

        <div className="flex-1 min-w-0">
          <div className="text-[16px] font-bold text-text truncate max-[480px]:text-[13px]">{LESSON_TITLE}</div>
          <div className="flex gap-1.5 mt-1 overflow-x-auto flex-nowrap scrollbar-none max-[480px]:gap-1">
            {PHASES.map(p => (
              <span key={p} className={cn(
                'text-[10px] px-2 py-0.5 rounded-[10px] bg-surface text-muted whitespace-nowrap flex-shrink-0',
                p === phase && 'bg-purple text-white',
                (phase === 'COMPLETE' || phaseOrder(p) < currentOrder) && p !== phase && 'bg-teal-dim text-teal',
              )}>
                {PHASE_LABELS[p]}
              </span>
            ))}
          </div>
        </div>

        <div
          className="text-[10px] px-2.5 py-1 rounded-full border font-medium flex-shrink-0"
          style={{ borderColor: 'rgba(201,162,39,0.4)', color: 'var(--gold)', background: 'rgba(201,162,39,0.08)' }}
        >
          * DEMO LESSON
        </div>
      </div>

      {/* ── HOOK ── */}
      {phase === 'HOOK' && (
        <div className="flex-1 flex flex-col items-center justify-center px-6 py-10 text-center overflow-y-auto max-[480px]:px-3 max-[480px]:py-5">
          <div
            className="relative max-w-[640px] w-full px-14 py-12 rounded-[20px] overflow-hidden max-[480px]:px-5 max-[480px]:py-7"
            style={{
              background: 'radial-gradient(ellipse at 50% 0%, rgba(139,92,246,.18) 0%, transparent 65%), var(--card)',
              border: '1px solid rgba(139,92,246,.3)',
              boxShadow: '0 0 80px rgba(139,92,246,.12), 0 16px 48px rgba(0,0,0,.4)',
            }}
          >
            <div className="absolute top-0 left-0 right-0 h-0.5"
              style={{ background: 'linear-gradient(90deg, transparent 0%, var(--purple) 25%, var(--teal) 75%, transparent 100%)' }} />
            <div className="absolute bottom-0 left-0 right-0 h-0.5"
              style={{ background: 'linear-gradient(90deg, transparent 0%, var(--teal) 25%, var(--purple) 75%, transparent 100%)' }} />

            <div className="text-[10px] font-semibold tracking-[0.22em] uppercase text-muted mb-3">* DEMO LESSON / HOOK</div>
            <h1 className="font-cinzel text-[22px] font-bold leading-[1.35] mb-2 max-[480px]:text-[17px]"
              style={{ color: '#c9a227', textShadow: '0 0 28px rgba(201,162,39,.3)' }}>
              {LESSON_TITLE}
            </h1>
            <div className="w-24 h-px mx-auto mb-7"
              style={{ background: 'linear-gradient(90deg, transparent, var(--purple) 30%, var(--teal) 70%, transparent)' }} />

            <div className="text-left mb-6 px-4 py-3 rounded-[10px]"
              style={{ background: 'rgba(139,92,246,0.08)', border: '1px solid rgba(139,92,246,0.2)' }}>
              <div className="text-[11px] font-bold text-purple-light uppercase tracking-[0.1em] mb-1.5">Book TUTORIAL TIP</div>
              <div className="text-[13px] text-muted leading-[1.65]">
                Every lesson opens with a <strong className="text-text">Hook</strong> - a story, analogy, or question designed to spark curiosity before the teaching begins.
              </div>
            </div>

            <div
              className="text-[16px] leading-[1.85] text-text text-left [&_p]:m-0 [&_p]:mb-4 [&_p:last-child]:mb-0 [&_strong]:text-gold"
              dangerouslySetInnerHTML={safe(HOOK_HTML)}
            />
          </div>
          <button className="btn btn-primary mt-9 px-8 py-2.5 text-[14px]" onClick={advance}>Begin {'->'}</button>
        </div>
      )}

      {/* ── EXPLANATION ── */}
      {phase === 'EXPLANATION' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <TutorialTip>
            The <strong className="text-text">Explanation</strong> phase breaks down the core concept with worked examples, mental models, and a "Why it matters" section. Real lessons use the same structure.
          </TutorialTip>

          {/* Learning objectives */}
          <div className="mb-6 p-4 rounded-[10px] border border-[rgba(45,212,191,0.25)] bg-[rgba(45,212,191,0.05)]">
            <div className="text-[12px] font-bold text-teal uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
              <Target size={12} strokeWidth={2} /> Learning Objectives
            </div>
            <ul className="m-0 pl-4 space-y-1.5">
              {LEARNING_OBJECTIVES.map((obj, i) => (
                <li key={i} className="text-[13px] text-text leading-[1.6] marker:text-teal">{obj}</li>
              ))}
            </ul>
          </div>

          {/* Main explanation prose */}
          <div className={prose} dangerouslySetInnerHTML={safe(EXPLANATION_HTML)} />

          {/* Why it matters */}
          <div className="mb-5 p-4 rounded-[10px] border border-[rgba(45,212,191,0.2)] bg-[rgba(45,212,191,0.04)]">
            <div className="text-[12px] font-bold text-teal uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
              <Zap size={12} strokeWidth={2} /> Why It Matters
            </div>
            <div
              className="text-[14px] leading-[1.75] text-text [&_p]:m-0 [&_p]:mb-3 [&_p:last-child]:mb-0 [&_strong]:text-teal [&_strong]:font-semibold"
              dangerouslySetInnerHTML={safe(WHY_IT_MATTERS_HTML)}
            />
          </div>

          {/* Mental model */}
          <div className="mb-5 p-4 rounded-[10px] border border-[rgba(255,193,7,0.2)] bg-[rgba(255,193,7,0.04)]">
            <div className="text-[12px] font-bold text-gold uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
              <Lightbulb size={12} strokeWidth={2} /> Mental Model
            </div>
            <div
              className="text-[14px] leading-[1.75] text-text italic [&_p]:m-0 [&_p]:mb-3 [&_p:last-child]:mb-0 [&_strong]:text-gold [&_strong]:not-italic [&_strong]:font-semibold"
              dangerouslySetInnerHTML={safe(MENTAL_MODEL_HTML)}
            />
          </div>

          {/* Mini summary */}
          <div className="mb-6 p-4 rounded-[10px] border border-border bg-surface">
            <div className="text-[12px] font-bold text-muted uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
              <CheckCircle2 size={12} strokeWidth={2} /> Summary
            </div>
            <div
              className="text-[14px] leading-[1.75] text-text [&_p]:m-0 [&_p]:mb-2 [&_p:last-child]:mb-0 [&_ul]:pl-4 [&_ul]:m-0 [&_li]:mb-1.5 [&_li]:leading-[1.6] [&_li::marker]:text-teal [&_strong]:text-text [&_strong]:font-semibold"
              dangerouslySetInnerHTML={safe(MINI_SUMMARY_HTML)}
            />
          </div>

          <button className="btn btn-primary" onClick={advance}>I understand - continue {'->'}</button>
        </div>
      )}

      {/* ── GUIDED PRACTICE ── */}
      {phase === 'GUIDED_PRACTICE' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <TutorialTip color="teal">
            <strong className="text-text">Guided Practice</strong> asks you to apply what you just learned - usually with scaffolding (a clear task, hints, or step-by-step prompts). Writing your answer strengthens the memory trace.
          </TutorialTip>

          <div className="text-[13px] font-bold text-gold mb-2.5 tracking-[0.06em] uppercase">* Guided Practice</div>
          <p className="text-[15px] text-text leading-[1.75] mb-5">{GUIDED_TASK}</p>

          <textarea
            value={guidedAnswer}
            onChange={e => setGuidedAnswer(e.target.value)}
            disabled={guidedSubmitted}
            placeholder="Type your answer here…"
            className="w-full min-h-[130px] p-4 rounded-[10px] border border-border bg-bg text-text text-[14px] leading-[1.7] resize-y outline-none focus:border-purple transition-colors"
          />

          {!guidedSubmitted ? (
            <button
              className="btn btn-primary mt-4"
              disabled={guidedAnswer.trim().length < 20}
              onClick={() => setGuidedSubmitted(true)}
            >
              Submit {'->'}
            </button>
          ) : (
            <div>
              <div className="mt-4 p-4 rounded-[10px]" style={{ background: 'rgba(0,200,83,0.08)', border: '1px solid var(--teal)' }}>
                <div className="text-[13px] font-bold text-teal mb-2">* Model Answer</div>
                <p className="text-[13px] text-muted leading-[1.7] m-0">{GUIDED_MODEL_ANSWER}</p>
              </div>
              <button className="btn btn-primary mt-5" onClick={advance}>Continue {'->'}</button>
            </div>
          )}
        </div>
      )}

      {/* ── SOLO PRACTICE ── */}
      {phase === 'SOLO_PRACTICE' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <TutorialTip color="teal">
            <strong className="text-text">Solo Practice</strong> removes the scaffolding. You rebuild the answer from memory - no starter prompts. This is the phase that cements knowledge. If you're stuck, you can peek at the guided practice for a hint.
          </TutorialTip>

          <div className="text-[13px] font-bold mb-1 tracking-[0.06em] uppercase flex items-center gap-1.5" style={{ color: 'var(--teal)' }}>
            <Target size={13} strokeWidth={1.75} /> Solo Challenge
          </div>
          <p className="text-muted text-[12px] mb-4 leading-[1.6]">
            Rebuild this from memory - no looking back at the explanation.
          </p>

          <p className="text-[15px] text-text leading-[1.75] mb-5">{SOLO_TASK}</p>

          <textarea
            value={soloAnswer}
            onChange={e => setSoloAnswer(e.target.value)}
            disabled={soloSubmitted}
            placeholder="Write your solo response from memory…"
            className="w-full min-h-[130px] p-4 rounded-[10px] border border-border bg-bg text-text text-[14px] leading-[1.7] resize-y outline-none focus:border-teal transition-colors"
          />

          {/* Hint toggle */}
          <div className="mt-4">
            <button
              className="btn btn-ghost text-[12px] px-3 py-1.5 flex items-center gap-1.5"
              onClick={() => setShowHint(h => !h)}
            >
              {showHint
                ? <><EyeOff size={13} strokeWidth={1.75} /> Hide hint</>
                : <><Eye size={13} strokeWidth={1.75} /> Peek at Guided Practice</>}
            </button>
            {showHint && (
              <div className="mt-3 p-4 rounded-[10px] border border-dashed border-[rgba(139,92,246,0.35)] bg-[rgba(139,92,246,0.05)]">
                <div className="text-[11px] font-semibold text-muted uppercase tracking-[0.1em] mb-2.5">Guided Practice reference</div>
                <p className="text-[13px] text-muted leading-[1.7] m-0">{GUIDED_MODEL_ANSWER}</p>
              </div>
            )}
          </div>

          {!soloSubmitted ? (
            <button
              className="btn btn-primary mt-5"
              disabled={soloAnswer.trim().length < 20}
              onClick={() => setSoloSubmitted(true)}
            >
              <Zap size={13} strokeWidth={1.75} className="inline mr-1.5" />Submit
            </button>
          ) : (
            <div className="mt-4 p-3.5 bg-[rgba(0,200,83,0.08)] border border-teal rounded-[8px]">
              <div className="text-[14px] font-bold text-teal mb-2">* Solo Practice Complete!</div>
              <button className="btn btn-primary" onClick={advance}>Continue to Retrieval Check {'->'}</button>
            </div>
          )}
        </div>
      )}

      {/* ── RETRIEVAL CHECK ── */}
      {phase === 'RETRIEVAL_CHECK' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <TutorialTip color="gold">
            The <strong className="text-text">Retrieval Check</strong> is a short quiz that locks learning in. Answering questions - even incorrectly - strengthens memory far more than re-reading. In real lessons, your score determines your spaced-review schedule.
          </TutorialTip>

          <div className="text-[20px] font-bold text-gold mb-1.5">* Retrieval Check</div>
          <p className="text-muted text-[13px] mb-5">Answer these questions to test your understanding.</p>

          {!retrievalDone ? (
            <>
              {RETRIEVAL_QUESTIONS.map((q, qi) => (
                <div key={qi} className="mb-6 p-4 rounded-[10px] border border-border bg-card">
                  <p className="text-[14px] font-semibold text-text mb-3 leading-[1.6]">
                    {qi + 1}. {q.text}
                  </p>
                  <div className="flex flex-col gap-2">
                    {q.options.map((opt, oi) => (
                      <button
                        key={oi}
                        onClick={() => setSelected(s => ({ ...s, [qi]: oi }))}
                        className={cn(
                          'text-left px-4 py-2.5 rounded-[8px] border text-[13px] leading-[1.55] transition-all',
                          selected[qi] === oi
                            ? 'border-purple bg-purple-dim text-purple-light'
                            : 'border-border bg-surface text-muted hover:border-purple hover:text-text',
                        )}
                      >
                        <span className="font-bold mr-2">{String.fromCharCode(65 + oi)}.</span>{opt}
                      </button>
                    ))}
                  </div>
                </div>
              ))}
              <button
                className="btn btn-primary"
                disabled={Object.keys(selected).length < RETRIEVAL_QUESTIONS.length}
                onClick={submitRetrieval}
              >
                Submit Answers
              </button>
            </>
          ) : (
            <div className="mt-2">
              <div className="text-[20px] font-bold text-text mb-1">
                Score: {Math.round((score / RETRIEVAL_QUESTIONS.length) * 100)}% ({score}/{RETRIEVAL_QUESTIONS.length})
              </div>
              <div className={cn('text-[16px] font-semibold mb-5', score >= 2 ? 'text-teal' : 'text-orange')}>
                {score >= 2 ? 'OK Passed!' : '○ Keep reviewing - it will stick!'}
              </div>

              {RETRIEVAL_QUESTIONS.map((q, qi) => {
                const userAns = selected[qi]
                const correct = q.correct
                const isRight = userAns === correct
                return (
                  <div key={qi} className={cn(
                    'mb-4 p-4 rounded-[10px] border',
                    isRight ? 'border-teal bg-[rgba(0,200,83,0.06)]' : 'border-[rgba(248,113,113,0.4)] bg-[rgba(248,113,113,0.05)]',
                  )}>
                    <p className="text-[13px] font-semibold text-text mb-2 leading-[1.55]">{qi + 1}. {q.text}</p>
                    <p className={cn('text-[13px] mb-1', isRight ? 'text-teal' : 'text-red')}>
                      {isRight ? 'OK' : 'x'} Your answer: <em>{q.options[userAns]}</em>
                    </p>
                    {!isRight && (
                      <p className="text-[12px] text-teal">OK Correct: <em>{q.options[correct]}</em></p>
                    )}
                  </div>
                )
              })}

              <button className="btn btn-primary mt-3" onClick={advance}>Continue {'->'}</button>
            </div>
          )}
        </div>
      )}

      {/* ── INTEGRATION ── */}
      {phase === 'INTEGRATION' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <TutorialTip color="purple">
            The <strong className="text-text">Integration</strong> phase asks you to connect what you've learned to the broader world. There's no right answer - its purpose is to stretch the concept beyond its original context and build durable understanding.
          </TutorialTip>

          <div className="text-[20px] font-bold mb-1.5" style={{ color: 'var(--purple-light)' }}>⟁ Integration</div>
          <p className="text-muted text-[13px] mb-5">Connect what you've learned to the wider world.</p>

          <div
            className="text-[15px] leading-[1.8] text-text mb-7
              [&_p]:m-0 [&_p]:mb-4 [&_p:last-child]:mb-0
              [&_strong]:text-gold [&_strong]:font-semibold [&_em]:text-purple-light
              [&_ul]:pl-5 [&_ul]:mb-4 [&_li]:mb-2 [&_li]:leading-[1.65] [&_li::marker]:text-teal"
            dangerouslySetInnerHTML={safe(INTEGRATION_HTML)}
          />

          <div className="bg-surface border border-border rounded-xl p-5 mb-6">
            <p className="text-[13px] text-muted mb-2 font-medium">Reflect before continuing:</p>
            <p className="text-[14px] text-text leading-[1.65]">
              In real lessons, this phase may include a short written reflection or a cross-domain question graded by the AI Mentor. Take a moment to consider the connections above.
            </p>
          </div>

          <button className="btn btn-primary" onClick={advance}>Continue {'->'}</button>
        </div>
      )}

      {/* ── COMPLETE ── */}
      {phase === 'COMPLETE' && (
        <div className="flex-1 flex flex-col items-center justify-center px-6 py-10 text-center overflow-y-auto">
          <div
            className="max-w-[560px] w-full px-10 py-9 rounded-[20px]"
            style={{
              background: 'var(--card)',
              border: '1px solid rgba(45,212,191,0.4)',
              boxShadow: '0 0 60px rgba(45,212,191,0.1)',
            }}
          >
            <div className="text-[52px] mb-4">🎉</div>
            <h2 className="font-cinzel text-[20px] text-gold mb-4">Demo Lesson Complete!</h2>

            <div className="text-[14px] leading-[1.75] text-text text-left mb-6
              [&_p]:m-0 [&_p]:mb-3 [&_p:last-child]:mb-0
              [&_strong]:text-gold [&_strong]:font-semibold
              [&_ul]:pl-5 [&_ul]:mb-3 [&_li]:mb-1.5 [&_li::marker]:text-teal"
            >
              <p>You've just completed every phase of a real Arcane Academy lesson:</p>
              <ul>
                <li><strong>Hook</strong> - a story to spark curiosity</li>
                <li><strong>Explanation</strong> - concept broken down with examples and mental models</li>
                <li><strong>Guided Practice</strong> - apply it with scaffolding</li>
                <li><strong>Solo Practice</strong> - rebuild it from memory</li>
                <li><strong>Retrieval Check</strong> - questions that cement knowledge</li>
                <li><strong>Integration</strong> - connect it to the wider world</li>
              </ul>
              <p>After a real lesson, the FSRS algorithm schedules your next review at the exact moment your brain is about to forget - turning short-term recall into long-term mastery.</p>
            </div>

            <div className="flex items-center justify-center gap-2 text-[12px] text-muted mb-6">
              <Check size={14} className="text-teal" />
              <span>Tour complete! This won't show again.</span>
            </div>

            <button className="btn btn-primary px-8 py-2.5 text-[14px]" onClick={finishTutorial}>
              Start Learning {'->'}
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
