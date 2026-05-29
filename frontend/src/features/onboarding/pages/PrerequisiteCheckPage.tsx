import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'
import {
  PREREQ_QUIZ_QUESTIONS,
  CSS_PREREQ_TOPICS,
  prereqStorageKey,
  type PrereqStatus,
} from '@/features/onboarding/data/cssPrimer'

// Topic display metadata
const TOPIC_META: Record<string, { name: string; glyph: string }> = {
  tailwind: { name: 'Tailwind CSS', glyph: 'ðŸŽ¨' },
  react:    { name: 'React',        glyph: 'âš›ï¸' },
}

type Stage = 'choice' | 'quiz' | 'quiz-result'

export default function PrerequisiteCheckPage() {
  const { domainId = '' } = useParams<{ domainId: string }>()
  const navigate = useNavigate()
  const { user } = useAuth()

  const meta = TOPIC_META[domainId] ?? { name: domainId, glyph: 'ðŸ“–' }

  const [stage, setStage] = useState<Stage>('choice')
  const [quizAnswers, setQuizAnswers] = useState<(number | null)[]>(
    new Array(PREREQ_QUIZ_QUESTIONS.length).fill(null)
  )
  const [submitted, setSubmitted] = useState(false)

  // If this topic doesn't need a prereq check, skip straight to the topic
  if (!CSS_PREREQ_TOPICS.has(domainId)) {
    navigate(`/topic/${domainId}`, { replace: true })
    return null
  }

  function saveStatus(status: PrereqStatus) {
    if (user) {
      localStorage.setItem(prereqStorageKey(user.userId, domainId), status)
    }
  }

  function goToTopic() {
    navigate(`/topic/${domainId}`, { replace: true })
  }

  function goToPrimer() {
    navigate(`/topic/${domainId}/css-primer`)
  }

  // â”€â”€ Choice stage â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  function handleKnowIt() {
    saveStatus('passed-knowledge')
    goToTopic()
  }

  function handleNeedPrimer() {
    navigate(`/topic/${domainId}/css-primer`)
  }

  function handleTakeQuiz() {
    setStage('quiz')
  }

  // â”€â”€ Quiz stage â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  function handleAnswer(qIndex: number, optionIndex: number) {
    if (submitted) return
    setQuizAnswers(prev => {
      const next = [...prev]
      next[qIndex] = optionIndex
      return next
    })
  }

  function handleSubmitQuiz() {
    const allAnswered = quizAnswers.every(a => a !== null)
    if (!allAnswered) return
    setSubmitted(true)
    setStage('quiz-result')
  }

  const score = submitted
    ? PREREQ_QUIZ_QUESTIONS.filter((q, i) => quizAnswers[i] === q.correctIndex).length
    : 0
  const passed = score >= 3

  function handleQuizContinue() {
    if (passed) {
      saveStatus('passed-quiz')
      goToTopic()
    } else {
      goToPrimer()
    }
  }

  function handleSkipAnyway() {
    saveStatus('skipped')
    goToTopic()
  }

  // â”€â”€ Render â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  return (
    <div className="flex-1 flex items-start justify-center px-6 py-10 overflow-y-auto">
      <div className="max-w-[640px] w-full animate-[fade-up_0.5s_cubic-bezier(0.22,1,0.36,1)_both]">

        {/* â”€â”€ Choice â”€â”€ */}
        {stage === 'choice' && (
          <>
            <div className="text-center mb-8">
              <div className="text-[52px] mb-3">{meta.glyph}</div>
              <h1 className="font-cinzel text-[26px] font-bold text-gold m-0 mb-2">
                Before you begin {meta.name}
              </h1>
              <p className="text-[15px] text-muted leading-[1.7] max-w-[480px] mx-auto m-0">
                {domainId === 'tailwind'
                  ? 'Tailwind CSS maps directly onto HTML class attributes and CSS properties. You\'ll move fastest if you already know the basics: what a class is, what padding and margin do, and how flexbox works.'
                  : 'React is a JavaScript framework for building HTML-based UIs. To write JSX and reason about layout, you\'ll need to understand HTML elements, CSS selectors, and the box domain.'}
              </p>
            </div>

            {/* Info box */}
            <div className="flex items-start gap-3 bg-[rgba(201,162,39,0.06)] border border-[rgba(201,162,39,0.25)] rounded-[10px] px-5 py-4 mb-7">
              <span className="text-[18px] flex-shrink-0 mt-0.5">âš¡</span>
              <p className="text-[13px] text-muted leading-[1.6] m-0">
                <strong className="text-text">What you need:</strong> basic HTML structure (<code>div</code>, <code>class</code>, nesting),
                CSS selectors (<code>.class</code>, <code>#id</code>), and the box model (padding, margin, border).
                Flexbox helps but isn't required day one.
              </p>
            </div>

            <p className="text-[17px] font-bold text-text text-center mb-5">
              How comfortable are you with HTML & CSS?
            </p>

            <div className="flex flex-col gap-3">
              {/* Option A â€” I know it */}
              <button
                className="flex items-center gap-4 px-5 py-5 bg-card border border-[rgba(0,200,150,0.3)]
                  rounded-[14px] cursor-pointer text-left
                  transition-[border-color,transform,box-shadow] duration-150
                  hover:-translate-y-[2px] hover:border-teal hover:shadow-[0_6px_24px_rgba(0,200,150,0.12)]"
                onClick={handleKnowIt}
              >
                <span className="text-[32px] flex-shrink-0">âœ…</span>
                <div>
                  <div className="text-[15px] font-bold text-teal mb-0.5">
                    Yes â€” I know HTML & CSS basics
                  </div>
                  <div className="text-[13px] text-muted leading-[1.5]">
                    I understand elements, class attributes, the box model, and at least a little flexbox.
                    Let's get started.
                  </div>
                </div>
              </button>

              {/* Option B â€” Quiz me */}
              <button
                className="flex items-center gap-4 px-5 py-5 bg-card border border-[rgba(139,92,246,0.3)]
                  rounded-[14px] cursor-pointer text-left
                  transition-[border-color,transform,box-shadow] duration-150
                  hover:-translate-y-[2px] hover:border-purple hover:shadow-[0_6px_24px_rgba(139,92,246,0.12)]"
                onClick={handleTakeQuiz}
              >
                <span className="text-[32px] flex-shrink-0">ðŸ”®</span>
                <div>
                  <div className="text-[15px] font-bold text-purple-light mb-0.5">
                    Not sure â€” quiz me (5 quick questions)
                  </div>
                  <div className="text-[13px] text-muted leading-[1.5]">
                    We'll check the essentials. Takes about 2 minutes.
                    Pass 3/5 and you're ready to begin.
                  </div>
                </div>
              </button>

              {/* Option C â€” Teach me */}
              <button
                className="flex items-center gap-4 px-5 py-5 bg-card border border-[rgba(255,165,0,0.3)]
                  rounded-[14px] cursor-pointer text-left
                  transition-[border-color,transform,box-shadow] duration-150
                  hover:-translate-y-[2px] hover:border-orange hover:shadow-[0_6px_24px_rgba(255,165,0,0.12)]"
                onClick={handleNeedPrimer}
              >
                <span className="text-[32px] flex-shrink-0">ðŸŒ±</span>
                <div>
                  <div className="text-[15px] font-bold text-orange mb-0.5">
                    No â€” show me the essentials first
                  </div>
                  <div className="text-[13px] text-muted leading-[1.5]">
                    A focused 10-minute primer covers everything you need before the first lesson.
                    No coding â€” just reading and a few questions.
                  </div>
                </div>
              </button>
            </div>
          </>
        )}

        {/* â”€â”€ Quiz â”€â”€ */}
        {stage === 'quiz' && (
          <>
            <div className="text-center mb-7">
              <div className="text-[40px] mb-2">ðŸ”®</div>
              <h1 className="font-cinzel text-[22px] font-bold text-gold m-0 mb-1">
                CSS Quick Check
              </h1>
              <p className="text-[14px] text-muted m-0">
                5 questions Â· pass 3 or more to proceed directly
              </p>
            </div>

            <div className="flex flex-col gap-6">
              {PREREQ_QUIZ_QUESTIONS.map((q, qIdx) => {
                const selected = quizAnswers[qIdx]
                return (
                  <div
                    key={q.id}
                    className="bg-card border border-border rounded-[12px] px-5 py-5"
                  >
                    <p className="text-[14px] font-semibold text-text leading-[1.6] m-0 mb-3">
                      <span className="text-muted mr-1.5">{qIdx + 1}.</span>
                      {q.text}
                    </p>
                    <div className="flex flex-col gap-2">
                      {q.options.map((opt, oIdx) => (
                        <button
                          key={oIdx}
                          disabled={submitted}
                          onClick={() => handleAnswer(qIdx, oIdx)}
                          className={[
                            'text-left text-[13px] px-4 py-2.5 rounded-[8px] border transition-colors duration-100',
                            selected === oIdx
                              ? 'border-purple bg-[rgba(139,92,246,0.12)] text-purple-light font-semibold'
                              : 'border-border bg-[rgba(255,255,255,0.03)] text-muted hover:border-[rgba(139,92,246,0.4)] hover:text-text',
                          ].join(' ')}
                        >
                          {opt}
                        </button>
                      ))}
                    </div>
                  </div>
                )
              })}
            </div>

            <button
              disabled={quizAnswers.some(a => a === null)}
              onClick={handleSubmitQuiz}
              className="mt-6 w-full py-3 rounded-[10px] font-cinzel font-bold text-[14px] tracking-wide
                bg-purple text-white transition-opacity duration-150
                hover:enabled:opacity-90 disabled:opacity-40 disabled:cursor-not-allowed"
            >
              Submit Answers
            </button>
          </>
        )}

        {/* â”€â”€ Quiz Result â”€â”€ */}
        {stage === 'quiz-result' && (
          <>
            <div className="text-center mb-7">
              <div className="text-[52px] mb-3">{passed ? 'ðŸŽ‰' : 'ðŸ“š'}</div>
              <h1 className="font-cinzel text-[24px] font-bold m-0 mb-2"
                style={{ color: passed ? 'var(--teal)' : 'var(--gold)' }}>
                {passed ? 'You\'re ready!' : 'A few gaps to fill'}
              </h1>
              <p className="text-[15px] text-muted m-0">
                You got <strong className="text-text">{score} out of {PREREQ_QUIZ_QUESTIONS.length}</strong> correct.
                {passed
                  ? ' That\'s enough to hit the ground running.'
                  : ' The primer will cover the missing pieces in about 10 minutes.'}
              </p>
            </div>

            {/* Per-question breakdown */}
            <div className="flex flex-col gap-3 mb-7">
              {PREREQ_QUIZ_QUESTIONS.map((q, qIdx) => {
                const userAnswer = quizAnswers[qIdx]!
                const correct = userAnswer === q.correctIndex
                return (
                  <div
                    key={q.id}
                    className={[
                      'rounded-[10px] border px-4 py-3.5',
                      correct
                        ? 'border-[rgba(0,200,83,0.3)] bg-[rgba(0,200,83,0.04)]'
                        : 'border-[rgba(220,38,38,0.3)] bg-[rgba(220,38,38,0.04)]',
                    ].join(' ')}
                  >
                    <div className="flex items-start gap-2.5">
                      <span className="text-[15px] flex-shrink-0 mt-0.5">
                        {correct ? 'âœ“' : 'âœ—'}
                      </span>
                      <div className="flex-1">
                        <p className="text-[13px] font-semibold text-text m-0 mb-1 leading-[1.5]">
                          {q.text}
                        </p>
                        {!correct && (
                          <p className="text-[12px] text-muted m-0 mb-0.5">
                            Your answer: <span className="text-red line-through">{q.options[userAnswer]}</span>
                            {' '}â†’ Correct: <span className="text-teal font-semibold">{q.options[q.correctIndex]}</span>
                          </p>
                        )}
                        <p className="text-[12px] text-muted m-0 leading-[1.5] italic">
                          {q.explanation}
                        </p>
                      </div>
                    </div>
                  </div>
                )
              })}
            </div>

            {passed ? (
              <button
                onClick={handleQuizContinue}
                className="w-full py-3.5 rounded-[10px] font-cinzel font-bold text-[14px] tracking-wide
                  bg-teal text-bg transition-opacity hover:opacity-90"
              >
                Begin {meta.name} â†’
              </button>
            ) : (
              <div className="flex flex-col gap-3">
                <button
                  onClick={handleQuizContinue}
                  className="w-full py-3.5 rounded-[10px] font-cinzel font-bold text-[14px] tracking-wide
                    bg-purple text-white transition-opacity hover:opacity-90"
                >
                  ðŸ“š Take the 10-minute CSS Primer
                </button>
                <button
                  onClick={handleSkipAnyway}
                  className="w-full py-2.5 rounded-[10px] text-[13px] text-muted
                    border border-border bg-transparent hover:text-text hover:border-[rgba(255,255,255,0.2)]
                    transition-colors duration-150"
                >
                  Skip primer and dive in anyway
                </button>
              </div>
            )}
          </>
        )}

      </div>
    </div>
  )
}
