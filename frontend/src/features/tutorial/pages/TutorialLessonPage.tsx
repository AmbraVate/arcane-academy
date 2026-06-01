import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTutorial } from '../context/TutorialContext'
import { ArrowLeft, Check } from 'lucide-react'
import { cn } from '@/lib/utils'

// ── Dummy lesson content (not related to any real domain) ─────────────────────

const LESSON_TITLE = 'How Memory Actually Works'

const PHASES = ['HOOK', 'EXPLANATION', 'GUIDED PRACTICE', 'COMPLETE'] as const
type Phase = typeof PHASES[number]

const HOOK_HTML = `
<p>Imagine trying to fill a bathtub with water — but the drain is open. You pour and pour, yet the tub never fills up.</p>
<p>That's what reading a textbook the night before an exam is like. You process the information, but without the right techniques, it drains away before it can stick.</p>
<p><strong>What if there was a way to close the drain?</strong></p>
`

const EXPLANATION_HTML = `
<h3>The Two Types of Memory</h3>
<p>Your brain has two stages of memory formation:</p>
<ul>
  <li><strong>Working memory</strong> — holds 4–7 items at once, like RAM in a computer. It's fast but tiny and volatile.</li>
  <li><strong>Long-term memory</strong> — vast and durable, but information only moves there through <em>deliberate effort</em>.</li>
</ul>
<h3>The Spacing Effect</h3>
<p>Research by Hermann Ebbinghaus in the 1880s showed that we forget in a predictable curve. But here's the key insight: <strong>each time you retrieve a memory just before it fades, the forgetting curve resets — and becomes shallower</strong>.</p>
<p>This is called <strong>spaced repetition</strong>, and it's the scientific foundation behind how Arcane Academy schedules your reviews.</p>
<h3>Retrieval Practice</h3>
<p>Simply re-reading notes produces an <em>illusion of knowing</em>. The act that actually cements memory is <strong>retrieval</strong> — forcing your brain to reconstruct information from scratch. This is why every lesson here ends with practice and questions, not just reading.</p>
`

const PRACTICE_QUESTION = 'In your own words: why is retrieval practice more effective than re-reading? (Write 1–2 sentences.)'

const CONCLUSION_HTML = `
<p>Well done! You've just experienced the core structure every Arcane Academy lesson follows:</p>
<ul>
  <li><strong>Hook</strong> — a story or question to spark curiosity</li>
  <li><strong>Explanation</strong> — the core concept, clearly broken down</li>
  <li><strong>Guided Practice</strong> — apply it with scaffolding</li>
  <li><strong>Solo Practice + Retrieval</strong> — rebuild it from memory</li>
</ul>
<p>This structure is deliberately designed around how memory works. Now you're ready to start your first real lesson!</p>
`

// ── Phase labels ───────────────────────────────────────────────────────────────

const PHASE_LABELS: Record<Phase, string> = {
  'HOOK': 'Hook',
  'EXPLANATION': 'Explanation',
  'GUIDED PRACTICE': 'Practice',
  'COMPLETE': 'Complete',
}

// ── Component ─────────────────────────────────────────────────────────────────

export default function TutorialLessonPage() {
  const navigate = useNavigate()
  const { complete } = useTutorial()
  const [phase, setPhase] = useState<Phase>('HOOK')
  const [answer, setAnswer] = useState('')
  const [submitted, setSubmitted] = useState(false)

  function advance() {
    const idx = PHASES.indexOf(phase)
    if (idx < PHASES.length - 1) setPhase(PHASES[idx + 1])
  }

  function finishTutorial() {
    complete()
    navigate('/domains')
  }

  const phaseOrder = (p: Phase) => PHASES.indexOf(p)
  const currentOrder = phaseOrder(phase)

  return (
    <div className="flex flex-col flex-1 overflow-hidden min-h-0">
      {/* Header */}
      <div className="flex items-center gap-3 px-4 py-2.5 border-b border-border bg-card flex-shrink-0">
        <button className="btn btn-ghost text-[12px] flex items-center gap-1" onClick={() => navigate('/domains')}>
          <ArrowLeft size={13} strokeWidth={1.75} /> Exit Demo
        </button>
        <div className="flex-1 min-w-0">
          <div className="text-[16px] font-bold text-text truncate">{LESSON_TITLE}</div>
          <div className="flex gap-1.5 mt-1 overflow-x-auto flex-nowrap scrollbar-none">
            {PHASES.map(p => (
              <span key={p} className={cn(
                'text-[10px] px-2 py-0.5 rounded-[10px] bg-surface text-muted whitespace-nowrap flex-shrink-0',
                p === phase && 'bg-purple text-white',
                phaseOrder(p) < currentOrder && 'bg-teal-dim text-teal',
              )}>
                {PHASE_LABELS[p]}
              </span>
            ))}
          </div>
        </div>

        {/* Demo badge */}
        <div
          className="text-[10px] px-2.5 py-1 rounded-full border font-medium flex-shrink-0"
          style={{ borderColor: 'rgba(201,162,39,0.4)', color: 'var(--gold)', background: 'rgba(201,162,39,0.08)' }}
        >
          ✦ DEMO LESSON
        </div>
      </div>

      {/* ── HOOK ────────────────────────────────────────────────────────────── */}
      {phase === 'HOOK' && (
        <div className="flex-1 flex flex-col items-center justify-center px-6 py-10 text-center overflow-y-auto">
          <div
            className="relative max-w-[640px] w-full px-12 py-10 rounded-[20px] overflow-hidden"
            style={{
              background: 'radial-gradient(ellipse at 50% 0%, rgba(139,92,246,.18) 0%, transparent 65%), var(--card)',
              border: '1px solid rgba(139,92,246,.3)',
              boxShadow: '0 0 80px rgba(139,92,246,.12), 0 16px 48px rgba(0,0,0,.4)',
            }}
          >
            <div className="absolute top-0 left-0 right-0 h-0.5"
              style={{ background: 'linear-gradient(90deg, transparent 0%, var(--purple) 25%, var(--teal) 75%, transparent 100%)' }} />
            <div className="text-[10px] font-semibold tracking-[0.22em] uppercase text-muted mb-3">
              ✦ DEMO LESSON · HOOK
            </div>
            <h1 className="font-cinzel text-[22px] font-bold leading-[1.35] mb-2"
              style={{ color: '#c9a227', textShadow: '0 0 28px rgba(201,162,39,.3)' }}>
              {LESSON_TITLE}
            </h1>
            <div className="w-24 h-px mx-auto mb-7"
              style={{ background: 'linear-gradient(90deg, transparent, var(--purple) 30%, var(--teal) 70%, transparent)' }} />

            {/* Tutorial callout inside lesson */}
            <div className="text-left mb-6 px-4 py-3 rounded-[10px]"
              style={{ background: 'rgba(139,92,246,0.08)', border: '1px solid rgba(139,92,246,0.2)' }}>
              <div className="text-[11px] font-bold text-purple-light uppercase tracking-[0.1em] mb-1.5">📖 TUTORIAL TIP</div>
              <div className="text-[13px] text-muted leading-[1.65]">
                Every lesson opens with a <strong className="text-text">Hook</strong> — a story, analogy, or question designed to spark curiosity before the teaching begins.
              </div>
            </div>

            <div
              className="text-[16px] leading-[1.85] text-text text-left [&_p]:m-0 [&_p]:mb-4 [&_p:last-child]:mb-0 [&_strong]:text-gold"
              dangerouslySetInnerHTML={{ __html: HOOK_HTML }}
            />
          </div>
          <button className="btn btn-primary mt-9 px-8 py-2.5 text-[14px]" onClick={advance}>
            Begin →
          </button>
        </div>
      )}

      {/* ── EXPLANATION ─────────────────────────────────────────────────────── */}
      {phase === 'EXPLANATION' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border">
          {/* Tutorial callout */}
          <div className="mb-5 px-4 py-3 rounded-[10px]"
            style={{ background: 'rgba(139,92,246,0.08)', border: '1px solid rgba(139,92,246,0.2)' }}>
            <div className="text-[11px] font-bold text-purple-light uppercase tracking-[0.1em] mb-1.5">📖 TUTORIAL TIP</div>
            <div className="text-[13px] text-muted leading-[1.65]">
              The <strong className="text-text">Explanation</strong> phase breaks down the core concept. Real lessons include worked examples, mental models, and "why it matters" sections. You can also toggle <strong className="text-text">Lore mode</strong> in Settings to replace the narrative framing with plain descriptions.
            </div>
          </div>

          <div
            className="text-[15px] leading-[1.8] text-text my-6
              [&_p]:m-0 [&_p]:mb-4 [&_p:last-child]:mb-0
              [&_strong]:text-gold [&_strong]:font-semibold [&_em]:text-purple-light
              [&_h3]:text-[17px] [&_h3]:font-bold [&_h3]:text-gold [&_h3]:mt-7 [&_h3]:mb-2.5 [&_h3]:pb-1.5 [&_h3]:border-b [&_h3]:border-[rgba(255,193,7,0.15)]
              [&_ul]:pl-5 [&_ul]:mb-4 [&_li]:mb-2 [&_li]:leading-[1.65] [&_li::marker]:text-purple-light"
            dangerouslySetInnerHTML={{ __html: EXPLANATION_HTML }}
          />

          <button className="btn btn-primary" onClick={advance}>
            I understand — continue →
          </button>
        </div>
      )}

      {/* ── GUIDED PRACTICE ─────────────────────────────────────────────────── */}
      {phase === 'GUIDED PRACTICE' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border">
          {/* Tutorial callout */}
          <div className="mb-5 px-4 py-3 rounded-[10px]"
            style={{ background: 'rgba(45,212,191,0.08)', border: '1px solid rgba(45,212,191,0.2)' }}>
            <div className="text-[11px] font-bold text-teal uppercase tracking-[0.1em] mb-1.5">📖 TUTORIAL TIP</div>
            <div className="text-[13px] text-muted leading-[1.65]">
              <strong className="text-text">Guided Practice</strong> asks you to apply what you just learned — often with scaffolding (hints, starter code, or step-by-step prompts). After this comes <strong className="text-text">Solo Practice</strong> where you rebuild it from memory.
            </div>
          </div>

          <div className="text-[13px] font-bold text-gold mb-2 tracking-[0.06em] uppercase">
            ✦ Guided Practice
          </div>

          <p className="text-[14px] text-text leading-[1.75] mb-5">{PRACTICE_QUESTION}</p>

          <textarea
            value={answer}
            onChange={e => setAnswer(e.target.value)}
            disabled={submitted}
            placeholder="Type your answer here…"
            className="w-full min-h-[120px] p-4 rounded-[10px] border border-border bg-bg text-text text-[14px] leading-[1.7] resize-y outline-none focus:border-purple transition-colors"
          />

          {!submitted ? (
            <button
              className="btn btn-primary mt-4"
              disabled={answer.trim().length < 10}
              onClick={() => setSubmitted(true)}
            >
              Submit Answer →
            </button>
          ) : (
            <div>
              <div className="mt-4 p-4 rounded-[10px]"
                style={{ background: 'rgba(0,200,83,0.08)', border: '1px solid var(--teal)' }}>
                <div className="text-[14px] font-bold text-teal mb-2">✦ Great answer!</div>
                <p className="text-[13px] text-muted leading-[1.65]">
                  Retrieval practice forces your brain to <em>reconstruct</em> information rather than passively recognise it.
                  This reconstruction strengthens the neural pathway, whereas re-reading just confirms familiarity
                  (the "illusion of knowing").
                </p>
              </div>
              <button className="btn btn-primary mt-5" onClick={advance}>
                Continue →
              </button>
            </div>
          )}
        </div>
      )}

      {/* ── COMPLETE ────────────────────────────────────────────────────────── */}
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
            <h2 className="font-cinzel text-[20px] text-gold mb-3">Demo Complete!</h2>

            <div
              className="text-[14px] leading-[1.75] text-text text-left mb-6
                [&_p]:m-0 [&_p]:mb-3 [&_p:last-child]:mb-0
                [&_strong]:text-gold [&_strong]:font-semibold
                [&_ul]:pl-5 [&_ul]:mb-3 [&_li]:mb-1.5 [&_li::marker]:text-teal"
              dangerouslySetInnerHTML={{ __html: CONCLUSION_HTML }}
            />

            <div className="flex items-center justify-center gap-2 text-[12px] text-muted mb-6">
              <Check size={14} className="text-teal" />
              <span>Tour complete! The tutorial won't show again.</span>
            </div>

            <button className="btn btn-primary px-8 py-2.5 text-[14px]" onClick={finishTutorial}>
              Start Learning →
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
