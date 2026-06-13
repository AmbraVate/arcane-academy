import { useState, useCallback } from 'react'
import { CheckCircle, XCircle, RotateCcw, ArrowRight, X } from 'lucide-react'
import { cn } from '@/lib/utils'
import { retentionApi } from '@/shared/api/services'
import type {
  ReviewQueueItem,
  ReviewQuestion,
  ReviewSubmitQuestionResult,
} from '@/shared/types'

interface Props {
  queue: ReviewQueueItem[]
  onClose: () => void
}

interface AnsweredQuestion {
  question: ReviewQuestion
  userAnswer: string
  result: ReviewSubmitQuestionResult | null
}

type ModalPhase = 'ANSWERING' | 'SUBMITTING' | 'RESULTS' | 'DONE'

export function DailyReviewModal({ queue, onClose }: Props) {
  // Flatten all questions from the queue into a single list
  const allItems = queue.flatMap(item =>
    item.questions.map(q => ({ lessonId: item.lessonId, lessonTitle: item.lessonTitle, q }))
  )

  const [currentIdx, setCurrentIdx] = useState(0)
  const [selectedAnswer, setSelectedAnswer] = useState<string | null>(null)
  const [answers, setAnswers] = useState<Array<{ lessonId: string; questionId: string; answer: string }>>([])
  const [phase, setPhase] = useState<ModalPhase>('ANSWERING')
  const [results, setResults] = useState<ReviewSubmitQuestionResult[]>([])
  const [answeredItems, setAnsweredItems] = useState<AnsweredQuestion[]>([])

  const current = allItems[currentIdx]
  const totalQuestions = allItems.length
  const progress = totalQuestions > 0 ? ((currentIdx) / totalQuestions) * 100 : 0

  const handleSelectAnswer = (opt: string) => {
    if (phase !== 'ANSWERING') return
    setSelectedAnswer(opt)
  }

  const handleNext = useCallback(async () => {
    if (!selectedAnswer || !current) return

    const newAnswers = [
      ...answers,
      { lessonId: current.lessonId, questionId: current.q.questionId, answer: selectedAnswer },
    ]
    setAnswers(newAnswers)

    const isLast = currentIdx === totalQuestions - 1

    if (isLast) {
      setPhase('SUBMITTING')

      // Group by lessonId
      const grouped: Record<string, { questionId: string; answer: string }[]> = {}
      for (const a of newAnswers) {
        if (!grouped[a.lessonId]) grouped[a.lessonId] = []
        grouped[a.lessonId].push({ questionId: a.questionId, answer: a.answer })
      }

      try {
        const response = await retentionApi.submitReview({
          lessons: Object.entries(grouped).map(([lessonId, ans]) => ({
            lessonId,
            answers: ans,
          })),
        })
        const flat = response.results.flatMap(r => r.questions)
        setResults(flat)

        // Build answered items for results display
        const answered: AnsweredQuestion[] = newAnswers.map(a => {
          const item = allItems.find(i => i.q.questionId === a.questionId)
          const result = flat.find(r => r.questionId === a.questionId) ?? null
          return {
            question: item!.q,
            userAnswer: a.answer,
            result,
          }
        })
        setAnsweredItems(answered)
      } catch {
        // On error: still show results screen but without server feedback
        const answered: AnsweredQuestion[] = newAnswers.map(a => {
          const item = allItems.find(i => i.q.questionId === a.questionId)
          return { question: item!.q, userAnswer: a.answer, result: null }
        })
        setAnsweredItems(answered)
      }

      setPhase('RESULTS')
    } else {
      setCurrentIdx(currentIdx + 1)
      setSelectedAnswer(null)
    }
  }, [selectedAnswer, current, currentIdx, totalQuestions, answers, allItems])

  const correctCount = answeredItems.filter(i => i.result?.correct === true).length
  const total = answeredItems.length

  if (totalQuestions === 0) return null

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-[rgba(8,6,18,0.85)]">
      <div
        className="relative w-full max-w-[560px] rounded-[16px] border border-border bg-card shadow-[0_24px_80px_rgba(0,0,0,0.6)]"
        style={{ borderTopColor: 'rgba(201,162,39,0.4)', borderTopWidth: 2 }}
      >
        {/* Header */}
        <div className="px-6 pt-5 pb-4 border-b border-border flex items-start justify-between gap-4">
          <div>
            <div className="font-cinzel text-[11px] tracking-[0.2em] text-gold mb-1">
              DAILY REVIEW
            </div>
            <h2 className="font-cinzel text-[18px] font-bold text-text m-0">
              {phase === 'RESULTS' ? 'Session Complete' : 'Retrieval Practice'}
            </h2>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 rounded-[8px] text-muted hover:text-text hover:bg-border/40 transition-colors"
          >
            <X size={16} strokeWidth={2} />
          </button>
        </div>

        {/* Progress bar (only during answering) */}
        {phase === 'ANSWERING' && (
          <div className="px-6 pt-4">
            <div className="flex items-center justify-between mb-1.5">
              <span className="text-[11px] text-muted font-cinzel">
                {currentIdx + 1} of {totalQuestions}
              </span>
              <span className="text-[11px] text-muted font-cinzel">
                {current.lessonTitle}
              </span>
            </div>
            <div className="h-[4px] bg-border rounded-full overflow-hidden">
              <div
                className="h-full rounded-full transition-[width] duration-500"
                style={{
                  width: `${progress}%`,
                  background: 'linear-gradient(90deg, var(--gold), var(--purple-light))',
                }}
              />
            </div>
          </div>
        )}

        {/* Body */}
        <div className="px-6 py-5">

          {/* ANSWERING phase */}
          {phase === 'ANSWERING' && current && (
            <div>
              <p
                className="text-[14px] text-text leading-[1.6] mb-5"
                dangerouslySetInnerHTML={{ __html: current.q.questionText }}
              />
              <div className="flex flex-col gap-2">
                {current.q.options.map(opt => (
                  <button
                    key={opt}
                    onClick={() => handleSelectAnswer(opt)}
                    className={cn(
                      'w-full text-left px-4 py-3 rounded-[10px] border text-[13px] transition-[border-color,background] duration-150',
                      selectedAnswer === opt
                        ? 'border-gold bg-[rgba(201,162,39,0.1)] text-text'
                        : 'border-border bg-surface text-muted hover:border-border/80 hover:text-text',
                    )}
                  >
                    {opt}
                  </button>
                ))}
              </div>
            </div>
          )}

          {/* SUBMITTING phase */}
          {phase === 'SUBMITTING' && (
            <div className="py-8 flex flex-col items-center gap-3 text-center">
              <RotateCcw size={24} className="text-gold animate-spin" strokeWidth={1.75} />
              <p className="text-[13px] text-muted">Calculating your results...</p>
            </div>
          )}

          {/* RESULTS phase */}
          {phase === 'RESULTS' && (
            <div>
              {/* Score summary */}
              <div className="flex items-center justify-center gap-6 mb-5 p-4 rounded-[12px] bg-surface border border-border">
                <div className="text-center">
                  <div
                    className="font-cinzel text-[32px] font-bold"
                    style={{ color: correctCount / total >= 0.7 ? 'var(--teal)' : 'var(--gold)' }}
                  >
                    {correctCount}/{total}
                  </div>
                  <div className="font-cinzel text-[10px] tracking-[0.15em] text-muted mt-0.5">
                    CORRECT
                  </div>
                </div>
              </div>

              {/* Per-question breakdown */}
              <div className="flex flex-col gap-2.5 max-h-[280px] overflow-y-auto pr-1">
                {answeredItems.map((item, idx) => {
                  const correct = item.result?.correct ?? null
                  return (
                    <div
                      key={idx}
                      className={cn(
                        'rounded-[10px] border px-4 py-3',
                        correct === true
                          ? 'border-teal/30 bg-[rgba(45,212,191,0.05)]'
                          : correct === false
                          ? 'border-[rgba(248,113,113,0.3)] bg-[rgba(248,113,113,0.05)]'
                          : 'border-border bg-surface',
                      )}
                    >
                      <div className="flex items-start gap-2.5">
                        <div className="flex-shrink-0 mt-0.5">
                          {correct === true
                            ? <CheckCircle size={14} strokeWidth={2} className="text-teal" />
                            : correct === false
                            ? <XCircle size={14} strokeWidth={2} className="text-[#f87171]" />
                            : <div className="w-3.5 h-3.5 rounded-full border border-border" />}
                        </div>
                        <div className="flex-1 min-w-0">
                          <p
                            className="text-[12px] text-text leading-[1.5] m-0 line-clamp-2"
                            dangerouslySetInnerHTML={{ __html: item.question.questionText }}
                          />
                          {correct === false && item.result?.correctAnswer && (
                            <p className="text-[11px] text-teal mt-1 m-0">
                              Correct: {item.result.correctAnswer}
                            </p>
                          )}
                        </div>
                      </div>
                    </div>
                  )
                })}
              </div>
            </div>
          )}
        </div>

        {/* Footer actions */}
        <div className="px-6 pb-5 flex justify-end gap-3">
          {phase === 'ANSWERING' && (
            <button
              onClick={handleNext}
              disabled={!selectedAnswer}
              className={cn(
                'flex items-center gap-2 px-5 py-2.5 rounded-[9px] font-cinzel text-[13px] font-semibold',
                'border transition-[background,border-color,opacity] duration-150',
                selectedAnswer
                  ? 'border-[rgba(45,212,191,0.4)] bg-[rgba(45,212,191,0.1)] text-teal hover:bg-[rgba(45,212,191,0.16)]'
                  : 'border-border text-muted opacity-40 cursor-not-allowed',
              )}
            >
              {currentIdx === totalQuestions - 1 ? 'Finish' : 'Next'}
              <ArrowRight size={14} strokeWidth={2} />
            </button>
          )}
          {phase === 'RESULTS' && (
            <button
              onClick={onClose}
              className="flex items-center gap-2 px-5 py-2.5 rounded-[9px] font-cinzel text-[13px] font-semibold
                border border-[rgba(201,162,39,0.4)] bg-[rgba(201,162,39,0.08)] text-gold
                hover:bg-[rgba(201,162,39,0.14)] transition-colors"
            >
              Continue Learning
              <ArrowRight size={14} strokeWidth={2} />
            </button>
          )}
        </div>
      </div>
    </div>
  )
}
