import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { authApi } from '@/shared/api/services'
import { useAuth } from '@/shared/hooks/useAuth'
import { ChevronRight, ChevronLeft, Check, X } from 'lucide-react'

// ── Shared styles ────────────────────────────────────────────────────────────

const S = {
  overlay: {
    minHeight: '100vh',
    background: 'radial-gradient(ellipse at 50% 0%, rgba(139,92,246,0.12) 0%, transparent 60%), var(--bg)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: '24px 16px',
  } as React.CSSProperties,
  card: {
    background: 'linear-gradient(160deg, #16132b 0%, #0f0e1f 100%)',
    border: '1px solid rgba(139,92,246,0.25)',
    borderRadius: 20,
    width: '100%',
    maxWidth: 520,
    padding: '36px 32px 28px',
    boxShadow: '0 24px 80px rgba(0,0,0,0.6)',
  } as React.CSSProperties,
}

// ── Step 1: Welcome ───────────────────────────────────────────────────────────

function Step1() {
  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ fontSize: 60, marginBottom: 16, lineHeight: 1 }}>🏰</div>
      <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, letterSpacing: '0.2em', color: '#c9a227', marginBottom: 10 }}>
        YOUR ARCANE JOURNEY BEGINS
      </div>
      <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 24, color: '#e8e0f0', margin: '0 0 16px', lineHeight: 1.3 }}>
        Welcome to Arcane Academy
      </h2>
      <p style={{ fontSize: 14, color: '#a89cc0', lineHeight: 1.75, margin: 0 }}>
        A degree-level, self-paced learning platform for the polymath. Master Software Engineering,
        Psychology, Natural Sciences and more — guided by an AI mentor and powered by memory science.
      </p>
      <p style={{ fontSize: 13, color: '#7b6fa0', lineHeight: 1.7, marginTop: 12 }}>
        This short walkthrough will show you how the Academy works, ending with a taste of a real lesson.
      </p>
    </div>
  )
}

// ── Step 2: How content is organised ─────────────────────────────────────────

const HIERARCHY = [
  { label: 'School',  color: '#c9a227', desc: 'e.g. School of Engineering' },
  { label: 'Domain',  color: '#a78bfa', desc: 'e.g. Software Engineering' },
  { label: 'Tier',    color: '#38bdf8', desc: 'Apprentice → Junior → Senior → Lead' },
  { label: 'Module',  color: '#4ade80', desc: 'e.g. Foundations of Computation' },
  { label: 'Topic',   color: '#fb923c', desc: 'e.g. Computational Thinking' },
  { label: 'Lesson',  color: '#f472b6', desc: 'e.g. What is an Algorithm?' },
]

function Step2() {
  return (
    <div>
      <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, letterSpacing: '0.2em', color: '#c9a227', marginBottom: 10, textAlign: 'center' }}>
        THE STRUCTURE OF KNOWLEDGE
      </div>
      <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#e8e0f0', margin: '0 0 20px', textAlign: 'center', lineHeight: 1.3 }}>
        How the Academy is Organised
      </h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 8, marginBottom: 18 }}>
        {HIERARCHY.map((h, i) => (
          <div key={h.label} style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            <div style={{
              width: 80, flexShrink: 0,
              fontFamily: 'Cinzel, serif', fontSize: 11, color: h.color,
              textAlign: 'right',
            }}>
              {h.label}
            </div>
            <div style={{ width: 12, flexShrink: 0, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <div style={{ width: 8, height: 8, borderRadius: '50%', background: h.color }} />
              {i < HIERARCHY.length - 1 && <div style={{ width: 2, height: 14, background: `${h.color}44`, marginTop: 2 }} />}
            </div>
            <div style={{ fontSize: 12, color: '#8b7fa0' }}>{h.desc}</div>
          </div>
        ))}
      </div>
      <p style={{ fontSize: 12, color: '#6b6080', lineHeight: 1.6, margin: 0, textAlign: 'center', fontStyle: 'italic' }}>
        Start at any School and work down. Java teaches Software Engineering —
        the language is the tool, not the goal.
      </p>
    </div>
  )
}

// ── Step 3: Lesson phases ─────────────────────────────────────────────────────

const PHASES = [
  { label: 'Hook',         color: '#c9a227', desc: 'A curiosity-sparking scenario pulls you in.' },
  { label: 'Learn',        color: '#8b5cf6', desc: 'Deep explanation with worked examples.' },
  { label: 'Checkpoint',   color: '#3b82f6', desc: 'Two quick questions — no grades, just checks.' },
  { label: 'Guided Quest', color: '#2dd4bf', desc: 'Scaffolded steps to practice with support.' },
  { label: 'Solo Quest',   color: '#fb923c', desc: 'Apply it independently — the real test.' },
  { label: 'Retrieval',    color: '#f472b6', desc: 'Recall + explain to lock it into long-term memory.' },
  { label: 'Integrate',    color: '#a78bfa', desc: 'Bridge to another domain — polymath thinking.' },
]

function Step3() {
  return (
    <div>
      <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, letterSpacing: '0.2em', color: '#c9a227', marginBottom: 10, textAlign: 'center' }}>
        SCIENCE-BACKED LEARNING
      </div>
      <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#e8e0f0', margin: '0 0 18px', textAlign: 'center', lineHeight: 1.3 }}>
        How Every Lesson Works
      </h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 6, marginBottom: 16 }}>
        {PHASES.map((p, i) => (
          <div key={p.label} style={{ display: 'flex', gap: 10, alignItems: 'flex-start' }}>
            <div style={{
              width: 20, height: 20, borderRadius: '50%', flexShrink: 0,
              background: `${p.color}22`, border: `1px solid ${p.color}55`,
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              fontSize: 10, color: p.color, fontFamily: 'Cinzel, serif', fontWeight: 700,
              marginTop: 1,
            }}>
              {i + 1}
            </div>
            <div>
              <span style={{ fontFamily: 'Cinzel, serif', fontSize: 11, color: p.color }}>{p.label}</span>
              <span style={{ fontSize: 12, color: '#6b6080' }}> — {p.desc}</span>
            </div>
          </div>
        ))}
      </div>
      <p style={{ fontSize: 12, color: '#6b6080', textAlign: 'center', margin: 0 }}>
        Each lesson takes 15–25 minutes. Earn XP, build memory strength, rise through the ranks.
      </p>
    </div>
  )
}

// ── Step 4: Demo lesson ───────────────────────────────────────────────────────

type DemoPhase = 'hook' | 'learn' | 'question' | 'done'

function Step4Demo() {
  const [phase, setPhase] = useState<DemoPhase>('hook')
  const [selected, setSelected] = useState<number | null>(null)
  const [answered, setAnswered] = useState(false)

  const CORRECT = 1 // "A set of step-by-step instructions"

  function submitAnswer() {
    if (selected === null) return
    setAnswered(true)
  }

  if (phase === 'done') {
    return (
      <div style={{ textAlign: 'center' }}>
        <div style={{ fontSize: 52, marginBottom: 12 }}>✨</div>
        <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, letterSpacing: '0.2em', color: '#c9a227', marginBottom: 8 }}>
          LESSON DEMO COMPLETE
        </div>
        <h3 style={{ fontFamily: 'Cinzel, serif', fontSize: 18, color: '#e8e0f0', margin: '0 0 10px' }}>
          You just experienced a real lesson!
        </h3>
        <p style={{ fontSize: 13, color: '#a89cc0', lineHeight: 1.65, margin: 0 }}>
          Every lesson in the Academy follows this same structure — a hook to spark curiosity,
          deep learning content, then practice that cements the knowledge in memory.
        </p>
        <p style={{ fontSize: 13, color: '#c4b5fd', lineHeight: 1.65, marginTop: 10 }}>
          You're ready to begin. Choose your School and start your path.
        </p>
      </div>
    )
  }

  return (
    <div>
      {/* Phase indicator */}
      <div style={{ display: 'flex', gap: 4, marginBottom: 16, justifyContent: 'center' }}>
        {(['hook', 'learn', 'question'] as const).map(p => (
          <div key={p} style={{
            height: 3, borderRadius: 2,
            flex: 1, maxWidth: 60,
            background: p === phase ? '#8b5cf6' : (p === 'hook' && ['learn','question'].includes(phase)) || (p === 'learn' && phase === 'question') ? '#4b4670' : '#2e2850',
            transition: 'background 0.3s',
          }} />
        ))}
      </div>

      {phase === 'hook' && (
        <div>
          <div style={{ fontFamily: 'Cinzel, serif', fontSize: 10, letterSpacing: '0.15em', color: '#c9a227', marginBottom: 8 }}>
            ✦ HOOK
          </div>
          <h3 style={{ fontFamily: 'Cinzel, serif', fontSize: 17, color: '#e8e0f0', margin: '0 0 12px', lineHeight: 1.3 }}>
            A Recipe for a Sandwich
          </h3>
          <p style={{ fontSize: 13, color: '#a89cc0', lineHeight: 1.7, margin: '0 0 12px' }}>
            Imagine you're teaching a robot to make a peanut butter sandwich. You can't say
            "just make it" — the robot has no common sense. You must give it <em>exact, ordered steps</em>:
            take bread, open jar, spread butter, close sandwich.
          </p>
          <p style={{ fontSize: 13, color: '#c4b5fd', lineHeight: 1.7, margin: 0 }}>
            That sequence of precise steps? That's an <strong style={{ color: '#e8e0f0' }}>algorithm</strong>.
            And algorithms are the heartbeat of all software.
          </p>
          <button
            onClick={() => setPhase('learn')}
            style={{
              marginTop: 20, width: '100%',
              background: 'rgba(139,92,246,0.15)', border: '1px solid rgba(139,92,246,0.35)',
              borderRadius: 9, color: '#c4b5fd', fontFamily: 'Cinzel, serif', fontSize: 13,
              padding: '10px 0', cursor: 'pointer',
            }}
          >
            Continue to Learning Content →
          </button>
        </div>
      )}

      {phase === 'learn' && (
        <div>
          <div style={{ fontFamily: 'Cinzel, serif', fontSize: 10, letterSpacing: '0.15em', color: '#8b5cf6', marginBottom: 8 }}>
            ✦ LEARN
          </div>
          <h3 style={{ fontFamily: 'Cinzel, serif', fontSize: 17, color: '#e8e0f0', margin: '0 0 12px', lineHeight: 1.3 }}>
            What is an Algorithm?
          </h3>
          <p style={{ fontSize: 13, color: '#a89cc0', lineHeight: 1.65, margin: '0 0 10px' }}>
            An <strong style={{ color: '#e8e0f0' }}>algorithm</strong> is a finite, ordered set of instructions
            that solves a problem. Every program you've ever used — from a search engine to a video game —
            is built from algorithms.
          </p>
          <div style={{ background: 'rgba(139,92,246,0.08)', border: '1px solid rgba(139,92,246,0.2)', borderRadius: 8, padding: '10px 14px', marginBottom: 10 }}>
            <p style={{ fontSize: 12, color: '#c4b5fd', margin: 0, lineHeight: 1.6, fontFamily: 'monospace' }}>
              1. Open the bread bag<br/>
              2. Take out two slices<br/>
              3. Spread peanut butter on one slice<br/>
              4. Press the slices together<br/>
              5. Done. 🥪
            </p>
          </div>
          <p style={{ fontSize: 12, color: '#6b6080', lineHeight: 1.6, margin: 0 }}>
            <strong style={{ color: '#a89cc0' }}>Key properties:</strong> finite (it ends),
            ordered (sequence matters), unambiguous (no guessing).
          </p>
          <button
            onClick={() => setPhase('question')}
            style={{
              marginTop: 16, width: '100%',
              background: 'rgba(139,92,246,0.15)', border: '1px solid rgba(139,92,246,0.35)',
              borderRadius: 9, color: '#c4b5fd', fontFamily: 'Cinzel, serif', fontSize: 13,
              padding: '10px 0', cursor: 'pointer',
            }}
          >
            Try a Practice Question →
          </button>
        </div>
      )}

      {phase === 'question' && (
        <div>
          <div style={{ fontFamily: 'Cinzel, serif', fontSize: 10, letterSpacing: '0.15em', color: '#2dd4bf', marginBottom: 8 }}>
            ✦ GUIDED PRACTICE
          </div>
          <h3 style={{ fontFamily: 'Cinzel, serif', fontSize: 15, color: '#e8e0f0', margin: '0 0 14px', lineHeight: 1.4 }}>
            Which of these best describes an algorithm?
          </h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 7, marginBottom: 14 }}>
            {[
              'A programming language like Java or Python',
              'A set of step-by-step instructions to solve a problem',
              'A type of computer hardware',
              'A way to store data in memory',
            ].map((option, i) => {
              let bg = 'rgba(255,255,255,0.03)'
              let border = 'rgba(255,255,255,0.1)'
              let color = '#a89cc0'
              if (answered) {
                if (i === CORRECT) { bg = 'rgba(45,212,191,0.1)'; border = '#2dd4bf'; color = '#2dd4bf' }
                else if (i === selected) { bg = 'rgba(248,113,113,0.08)'; border = '#f87171'; color = '#f87171' }
              } else if (i === selected) {
                bg = 'rgba(139,92,246,0.12)'; border = '#8b5cf6'; color = '#c4b5fd'
              }
              return (
                <button
                  key={i}
                  disabled={answered}
                  onClick={() => setSelected(i)}
                  style={{
                    textAlign: 'left', padding: '10px 14px', borderRadius: 8,
                    background: bg, border: `1px solid ${border}`, color,
                    fontSize: 13, cursor: answered ? 'default' : 'pointer',
                    transition: 'all 0.15s',
                    display: 'flex', alignItems: 'center', gap: 8,
                  }}
                >
                  {answered && i === CORRECT && <Check size={14} strokeWidth={2.5} style={{ flexShrink: 0 }} />}
                  {answered && i === selected && i !== CORRECT && <X size={14} strokeWidth={2.5} style={{ flexShrink: 0 }} />}
                  {(!answered || (i !== CORRECT && i !== selected)) && (
                    <span style={{
                      width: 20, height: 20, borderRadius: '50%', flexShrink: 0,
                      background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.15)',
                      display: 'flex', alignItems: 'center', justifyContent: 'center',
                      fontSize: 10, fontFamily: 'Cinzel, serif',
                    }}>
                      {String.fromCharCode(65 + i)}
                    </span>
                  )}
                  {option}
                </button>
              )
            })}
          </div>
          {answered ? (
            <div>
              <p style={{ fontSize: 12, color: '#2dd4bf', margin: '0 0 12px', lineHeight: 1.5 }}>
                {selected === CORRECT
                  ? '✓ Correct! An algorithm is a finite, ordered set of instructions — independent of any programming language.'
                  : `The correct answer is B. An algorithm is the set of instructions; Java is just one way to express it.`}
              </p>
              <button
                onClick={() => setPhase('done')}
                style={{
                  width: '100%',
                  background: 'linear-gradient(135deg, #7c3aed, #8b5cf6)',
                  border: 'none', borderRadius: 9,
                  color: '#fff', fontFamily: 'Cinzel, serif', fontSize: 13, fontWeight: 700,
                  padding: '11px 0', cursor: 'pointer',
                  boxShadow: '0 4px 16px rgba(139,92,246,0.35)',
                }}
              >
                ✦ Finish Demo
              </button>
            </div>
          ) : (
            <button
              onClick={submitAnswer}
              disabled={selected === null}
              style={{
                width: '100%',
                background: selected !== null ? 'rgba(139,92,246,0.2)' : 'rgba(255,255,255,0.04)',
                border: `1px solid ${selected !== null ? 'rgba(139,92,246,0.5)' : 'rgba(255,255,255,0.08)'}`,
                borderRadius: 9, color: selected !== null ? '#c4b5fd' : '#4b4670',
                fontFamily: 'Cinzel, serif', fontSize: 13,
                padding: '10px 0', cursor: selected !== null ? 'pointer' : 'not-allowed',
                transition: 'all 0.15s',
              }}
            >
              Submit Answer
            </button>
          )}
        </div>
      )}
    </div>
  )
}

// ── Main component ────────────────────────────────────────────────────────────

const STEPS = [
  { title: 'Welcome',    component: Step1 },
  { title: 'Structure',  component: Step2 },
  { title: 'Lessons',    component: Step3 },
  { title: 'Demo',       component: Step4Demo },
]

export default function OnboardingPage() {
  const navigate = useNavigate()
  const { markOnboardingDone } = useAuth()
  const [step, setStep] = useState(0)
  const [completing, setCompleting] = useState(false)

  const isLast = step === STEPS.length - 1
  const StepComponent = STEPS[step].component

  async function handleFinish() {
    if (completing) return
    setCompleting(true)
    try {
      await authApi.completeOnboarding()
    } catch { /* non-blocking */ }
    markOnboardingDone()
    navigate('/schools', { replace: true })
  }

  return (
    <div style={S.overlay}>
      <div style={S.card}>
        {/* Progress dots */}
        <div style={{ display: 'flex', justifyContent: 'center', gap: 6, marginBottom: 28 }}>
          {STEPS.map((s, i) => (
            <div
              key={s.title}
              style={{
                width: i === step ? 22 : 6, height: 6,
                borderRadius: 3,
                background: i === step ? '#8b5cf6' : i < step ? '#4b4670' : '#2e2850',
                transition: 'all 0.25s ease',
              }}
            />
          ))}
        </div>

        {/* Step content */}
        <div style={{ minHeight: 300 }}>
          <StepComponent />
        </div>

        {/* Navigation */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 28 }}>
          <button
            onClick={() => setStep(s => s - 1)}
            disabled={step === 0}
            style={{
              display: 'flex', alignItems: 'center', gap: 4,
              background: 'transparent', border: 'none',
              color: step === 0 ? '#2e2850' : '#8b7fa0',
              cursor: step === 0 ? 'default' : 'pointer',
              fontFamily: 'Cinzel, serif', fontSize: 12, padding: '8px 0',
              transition: 'color 0.15s',
            }}
          >
            <ChevronLeft size={14} strokeWidth={2} />
            Back
          </button>

          {isLast ? (
            <button
              onClick={handleFinish}
              disabled={completing}
              style={{
                display: 'flex', alignItems: 'center', gap: 6,
                background: 'linear-gradient(135deg, #c9a227, #f59e0b)',
                border: 'none', borderRadius: 9,
                color: '#0a0916', fontFamily: 'Cinzel, serif', fontSize: 13, fontWeight: 700,
                padding: '10px 22px', cursor: completing ? 'wait' : 'pointer',
                boxShadow: '0 4px 16px rgba(201,162,39,0.3)',
                opacity: completing ? 0.7 : 1,
              }}
            >
              ✦ Choose Your School
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
                transition: 'all 0.15s',
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
