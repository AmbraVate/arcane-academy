import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'
import {
  CSS_PRIMER_SECTIONS,
  prereqStorageKey,
  type PrimerSection,
  type PrimerQuestion,
} from '@/features/onboarding/data/cssPrimer'

type SectionStage = 'reading' | 'questions' | 'section-done'

function QuestionBlock({
  question,
  qIndex,
  sectionIndex,
  selectedAnswer,
  showResult,
  onAnswer,
}: {
  question: PrimerQuestion
  qIndex: number
  sectionIndex: number
  selectedAnswer: number | null
  showResult: boolean
  onAnswer: (index: number) => void
}) {
  return (
    <div className="bg-card border border-border rounded-[12px] px-5 py-5">
      <p className="text-[14px] font-semibold text-text leading-[1.6] m-0 mb-3">
        <span className="text-muted mr-1.5">{qIndex + 1}.</span>
        {question.text}
      </p>
      <div className="flex flex-col gap-2">
        {question.options.map((opt, oIdx) => {
          let btnClass =
            'text-left text-[13px] px-4 py-2.5 rounded-[8px] border transition-colors duration-100 '

          if (!showResult) {
            btnClass +=
              selectedAnswer === oIdx
                ? 'border-purple bg-[rgba(139,92,246,0.12)] text-purple-light font-semibold'
                : 'border-border bg-[rgba(255,255,255,0.03)] text-muted hover:border-[rgba(139,92,246,0.4)] hover:text-text'
          } else {
            if (oIdx === question.correctIndex) {
              btnClass += 'border-[rgba(0,200,83,0.5)] bg-[rgba(0,200,83,0.08)] text-teal font-semibold'
            } else if (oIdx === selectedAnswer && selectedAnswer !== question.correctIndex) {
              btnClass += 'border-[rgba(220,38,38,0.4)] bg-[rgba(220,38,38,0.06)] text-red line-through'
            } else {
              btnClass += 'border-border bg-transparent text-muted opacity-50'
            }
          }

          return (
            <button
              key={oIdx}
              disabled={showResult}
              onClick={() => onAnswer(oIdx)}
              className={btnClass}
            >
              {opt}
            </button>
          )
        })}
      </div>
      {showResult && (
        <p className="text-[12px] text-muted leading-[1.5] mt-3 italic m-0">
          {question.explanation}
        </p>
      )}
    </div>
  )
}

function SectionView({
  section,
  sectionIndex,
  totalSections,
  onComplete,
}: {
  section: PrimerSection
  sectionIndex: number
  totalSections: number
  onComplete: () => void
}) {
  const [stage, setStage] = useState<SectionStage>('reading')
  const [answers, setAnswers] = useState<(number | null)[]>(
    new Array(section.questions.length).fill(null)
  )
  const [showResults, setShowResults] = useState(false)

  function handleAnswer(qIdx: number, optIdx: number) {
    if (showResults) return
    setAnswers(prev => {
      const next = [...prev]
      next[qIdx] = optIdx
      return next
    })
  }

  function handleCheckAnswers() {
    setShowResults(true)
    setStage('section-done')
  }

  const allAnswered = answers.every(a => a !== null)
  const score = showResults
    ? section.questions.filter((q, i) => answers[i] === q.correctIndex).length
    : 0

  return (
    <div>
      {/* Section header */}
      <div className="flex items-center gap-3 mb-5">
        <span className="text-[28px]">{section.emoji}</span>
        <div>
          <div className="text-[11px] text-muted font-cinzel tracking-widest uppercase mb-0.5">
            Section {sectionIndex + 1} of {totalSections}
          </div>
          <h2 className="font-cinzel text-[20px] font-bold text-gold m-0">
            {section.title}
          </h2>
        </div>
      </div>

      {/* Reading content */}
      {stage === 'reading' && (
        <>
          <div
            className="prose-primer bg-card border border-border rounded-[12px] px-6 py-6 mb-5 text-[14px] text-muted leading-[1.7]"
            dangerouslySetInnerHTML={{ __html: section.explanationHtml }}
          />
          <button
            onClick={() => setStage('questions')}
            className="w-full py-3 rounded-[10px] font-cinzel font-bold text-[14px] tracking-wide
              bg-purple text-white transition-opacity hover:opacity-90"
          >
            Check My Understanding â†’
          </button>
        </>
      )}

      {/* Questions */}
      {(stage === 'questions' || stage === 'section-done') && (
        <>
          {stage === 'questions' && (
            <p className="text-[13px] text-muted mb-4 m-0">
              Answer all questions below, then check your answers.
            </p>
          )}

          <div className="flex flex-col gap-4 mb-5">
            {section.questions.map((q, qIdx) => (
              <QuestionBlock
                key={q.id}
                question={q}
                qIndex={qIdx}
                sectionIndex={sectionIndex}
                selectedAnswer={answers[qIdx]}
                showResult={showResults}
                onAnswer={(optIdx) => handleAnswer(qIdx, optIdx)}
              />
            ))}
          </div>

          {!showResults ? (
            <button
              disabled={!allAnswered}
              onClick={handleCheckAnswers}
              className="w-full py-3 rounded-[10px] font-cinzel font-bold text-[14px] tracking-wide
                bg-purple text-white transition-opacity hover:enabled:opacity-90
                disabled:opacity-40 disabled:cursor-not-allowed"
            >
              Check Answers
            </button>
          ) : (
            <div className="flex flex-col gap-3">
              <div className="flex items-center gap-3 px-5 py-3.5 bg-card border border-border rounded-[10px]">
                <span className="text-[22px]">
                  {score === section.questions.length ? 'ðŸŒŸ' : score >= section.questions.length / 2 ? 'ðŸ‘' : 'ðŸ“–'}
                </span>
                <div>
                  <div className="text-[14px] font-semibold text-text">
                    {score} / {section.questions.length} correct
                  </div>
                  <div className="text-[12px] text-muted">
                    {score === section.questions.length
                      ? 'Perfect â€” you know this section well.'
                      : 'Review the explanations above, then move on.'}
                  </div>
                </div>
              </div>
              <button
                onClick={onComplete}
                className="w-full py-3 rounded-[10px] font-cinzel font-bold text-[14px] tracking-wide
                  bg-teal text-bg transition-opacity hover:opacity-90"
              >
                {sectionIndex + 1 < totalSections ? `Next Section â†’` : `Complete Primer â†’`}
              </button>
            </div>
          )}
        </>
      )}
    </div>
  )
}

// â”€â”€ Main Page â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

const TOPIC_META: Record<string, { name: string; glyph: string }> = {
  tailwind: { name: 'Tailwind CSS', glyph: 'ðŸŽ¨' },
  react:    { name: 'React',        glyph: 'âš›ï¸' },
}

export default function CssPrimerPage() {
  const { domainId = '' } = useParams<{ domainId: string }>()
  const navigate = useNavigate()
  const { user } = useAuth()

  const meta = TOPIC_META[domainId] ?? { name: domainId, glyph: 'ðŸ“–' }

  const [currentSection, setCurrentSection] = useState(0)
  const [done, setDone] = useState(false)

  function handleSectionComplete() {
    if (currentSection + 1 < CSS_PRIMER_SECTIONS.length) {
      setCurrentSection(prev => prev + 1)
      window.scrollTo({ top: 0, behavior: 'smooth' })
    } else {
      // All sections done
      if (user) {
        localStorage.setItem(prereqStorageKey(user.userId, domainId), 'completed-primer')
      }
      setDone(true)
      window.scrollTo({ top: 0, behavior: 'smooth' })
    }
  }

  function handleProceed() {
    navigate(`/domain/${domainId}`, { replace: true })
  }

  return (
    <div className="flex-1 flex items-start justify-center px-6 py-10 overflow-y-auto">
      <div className="max-w-[720px] w-full animate-[fade-up_0.4s_cubic-bezier(0.22,1,0.36,1)_both]">

        {/* â”€â”€ Header â”€â”€ */}
        <div className="text-center mb-8">
          <div className="text-[44px] mb-2">ðŸŒ</div>
          <h1 className="font-cinzel text-[26px] font-bold text-gold m-0 mb-1">
            CSS & HTML Primer
          </h1>
          <p className="text-[14px] text-muted m-0">
            The essentials you need before starting {meta.name} Â· ~10 minutes
          </p>
        </div>

        {/* â”€â”€ Progress bar â”€â”€ */}
        {!done && (
          <div className="mb-8">
            <div className="flex justify-between text-[11px] text-muted font-cinzel mb-2">
              {CSS_PRIMER_SECTIONS.map((s, i) => (
                <span
                  key={s.id}
                  className={i <= currentSection ? 'text-purple-light' : ''}
                >
                  {i + 1}. {s.title.split(':')[0]}
                </span>
              ))}
            </div>
            <div className="h-[4px] bg-border rounded-full overflow-hidden">
              <div
                className="h-full bg-purple rounded-full transition-[width] duration-500"
                style={{
                  width: `${((currentSection) / CSS_PRIMER_SECTIONS.length) * 100}%`,
                }}
              />
            </div>
          </div>
        )}

        {/* â”€â”€ Done screen â”€â”€ */}
        {done ? (
          <div className="text-center animate-[fade-up_0.4s_cubic-bezier(0.22,1,0.36,1)_both]">
            <div className="text-[64px] mb-4">ðŸŽ“</div>
            <h2 className="font-cinzel text-[24px] font-bold text-teal m-0 mb-3">
              Primer Complete
            </h2>
            <p className="text-[15px] text-muted leading-[1.7] max-w-[440px] mx-auto mb-8">
              You now understand HTML structure, CSS selectors, the box model, and flexbox.
              That's everything {meta.name} builds on. Time to put it to work.
            </p>

            {/* Quick-reference card */}
            <div className="text-left bg-card border border-border rounded-[12px] px-5 py-5 mb-8">
              <p className="text-[12px] text-muted font-cinzel tracking-widest uppercase mb-3 m-0">
                Quick Reference
              </p>
              <div className="grid grid-cols-2 gap-2 text-[12px]">
                {[
                  ['HTML elements', '<div>, <p>, <button>'],
                  ['class attribute', '<div class="card">'],
                  ['CSS class selector', '.card { â€¦ }'],
                  ['padding vs margin', 'inside vs outside the border'],
                  ['display: flex', 'lays children in a row'],
                  ['justify-content', 'horizontal spacing in flex'],
                ].map(([label, value]) => (
                  <div key={label} className="flex flex-col gap-0.5">
                    <span className="text-muted">{label}</span>
                    <code className="text-purple-light text-[11px]">{value}</code>
                  </div>
                ))}
              </div>
            </div>

            <button
              onClick={handleProceed}
              className="w-full py-4 rounded-[12px] font-cinzel font-bold text-[15px] tracking-wide
                bg-teal text-bg transition-[opacity,transform] duration-150
                hover:opacity-90 hover:-translate-y-[2px]"
            >
              Begin {meta.glyph} {meta.name} â†’
            </button>
          </div>
        ) : (
          /* â”€â”€ Active section â”€â”€ */
          <SectionView
            key={currentSection}
            section={CSS_PRIMER_SECTIONS[currentSection]}
            sectionIndex={currentSection}
            totalSections={CSS_PRIMER_SECTIONS.length}
            onComplete={handleSectionComplete}
          />
        )}

      </div>
    </div>
  )
}
