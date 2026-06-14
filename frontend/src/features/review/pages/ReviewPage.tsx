import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { reviewApi } from '@/shared/api/services'
import type { ReviewSessionDto, ReviewResultDto, AnswerEntry, Badge } from '@/shared/types'
import QuestionCard from '@/features/learning/components/QuestionCard'
import BadgeToast from '@/shared/components/layout/BadgeToast'
import { motivatingMessage } from '@/features/retention/components/DailyReviewModal'
import { cn } from '@/lib/utils'

export default function ReviewPage() {
  const navigate = useNavigate()
  const [session, setSession] = useState<ReviewSessionDto | null>(null)
  const [loading, setLoading] = useState(true)
  const [currentQ, setCurrentQ] = useState(0)
  const [answers, setAnswers] = useState<Record<string, string>>({})
  const [result, setResult] = useState<ReviewResultDto | null>(null)
  const [submitting, setSubmitting] = useState(false)
  const [newBadges, setNewBadges] = useState<Badge[]>([])

  useEffect(() => {
    reviewApi.getDaily()
      .then(setSession)
      .catch(() => navigate('/topics'))
      .finally(() => setLoading(false))
  }, [navigate])

  async function handleSubmit() {
    if (!session) return
    setSubmitting(true)
    try {
      const answerList: AnswerEntry[] = Object.entries(answers).map(([questionId, answer]) => ({ questionId, answer }))
      const res = await reviewApi.submit(session.sessionId, answerList)
      setResult(res)
      if (res.newBadges?.length) setNewBadges(res.newBadges)
    } catch { /* noop */ } finally {
      setSubmitting(false)
    }
  }

  if (loading) {
    return <div className="flex items-center justify-center h-[60vh] text-muted"><p>Loading review session...</p></div>
  }

  if (!session || session.questions.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-center px-4">
        <div
          className="w-16 h-16 rounded-full flex items-center justify-center mb-5"
          style={{ background: 'rgba(74,222,128,0.12)', border: '1.5px solid rgba(74,222,128,0.3)' }}
        >
          <span style={{ fontSize: 28, lineHeight: 1 }}>&#10003;</span>
        </div>
        <h2 className="font-cinzel text-[20px] font-bold text-text m-0 mb-2">
          You&apos;re all caught up!
        </h2>
        <p className="text-[14px] text-muted m-0 mb-6 max-w-[340px]">
          No reviews due today. Keep learning to add more concepts — they&apos;ll appear here once they&apos;re ready for retrieval practice.
        </p>
        <button className="btn btn-primary" onClick={() => navigate('/topics')}>Continue Learning</button>
      </div>
    )
  }

  if (result) {
    const scoreColor = result.score >= 0.8 ? 'text-green' : result.score >= 0.5 ? 'text-orange' : 'text-red'
    const message = motivatingMessage(result.correct, result.total)
    return (
      <div className="max-w-[700px] mx-auto px-4 py-6 pb-[60px] max-[600px]:px-3 max-[600px]:py-4">
        <div className="text-center mb-7 p-7 bg-card border border-border rounded-[14px] max-[600px]:px-4 max-[600px]:py-5">
          <h2 className="text-[22px] font-bold text-gold m-0 mb-2.5">Review Complete</h2>
          <div className={cn('text-[48px] font-[800] mb-1 max-[600px]:text-[40px]', scoreColor)}>
            {Math.round(result.score * 100)}%
          </div>
          <div className="text-[14px] text-muted mb-3">{result.correct} / {result.total} correct</div>
          <p className="text-[13px] text-text m-0 mb-4">{message}</p>
          <div
            className="rounded-[10px] px-4 py-3"
            style={{ background: 'rgba(45,212,191,0.07)', border: '1px solid rgba(45,212,191,0.2)' }}
          >
            <p className="text-[12px] text-muted m-0">
              Great work today. Your next reviews will be scheduled automatically
              {' '}— <span className="font-medium" style={{ color: '#2dd4bf' }}>come back tomorrow</span> to keep your knowledge sharp.
            </p>
          </div>
        </div>
        <div className="mb-2">
          {session.questions.map((q, i) => (
            <QuestionCard key={q.id} question={q} index={i}
              answer={result.results[i]?.userAnswer ?? ''}
              onChange={() => {}} result={result.results[i]} disabled />
          ))}
        </div>
        <button className="btn btn-primary mt-2" onClick={() => navigate('/topics')}>Back to Dashboard</button>
        {newBadges.length > 0 && <BadgeToast badges={newBadges} onDone={() => setNewBadges([])} />}
      </div>
    )
  }

  const question = session.questions[currentQ]
  const totalQ   = session.questions.length
  const allAnswered = Object.keys(answers).length === totalQ

  return (
    <div className="max-w-[700px] mx-auto px-4 py-6 pb-[60px] max-[600px]:px-3 max-[600px]:py-4">
      {/* Header */}
      <div className="flex items-center gap-2.5 mb-5 flex-wrap max-[480px]:flex-col max-[480px]:items-start max-[600px]:gap-2">
        <button className="btn btn-ghost text-[12px]" onClick={() => navigate('/topics')}>← Back</button>
        <div className="text-[18px] font-bold text-gold max-[600px]:text-[16px]">Daily Review</div>
        <div className="flex gap-[5px] flex-1 flex-wrap max-[480px]:max-h-12 max-[480px]:overflow-hidden max-[600px]:gap-1">
          {session.questions.map((q, i) => (
            <button
              key={q.id}
              className={cn(
                'w-2.5 h-2.5 rounded-full border-[1.5px] cursor-pointer p-0 transition-[background,border-color,transform] duration-150',
                'hover:scale-[1.3] hover:border-purple',
                i === currentQ ? 'bg-purple border-purple scale-[1.2]' : '',
                answers[q.id] ? 'bg-teal-dim border-teal' : 'bg-surface border-border',
                'max-[600px]:w-[9px] max-[600px]:h-[9px]',
              )}
              onClick={() => setCurrentQ(i)}
              title={`Question ${i + 1}`}
            />
          ))}
        </div>
        <div className="text-[12px] text-muted whitespace-nowrap">{currentQ + 1} / {totalQ}</div>
      </div>

      <div className="flex flex-col gap-3">
        <QuestionCard
          question={question} index={currentQ}
          answer={answers[question.id] ?? ''}
          onChange={v => setAnswers(prev => ({ ...prev, [question.id]: v }))}
        />
        <div className="flex justify-between max-[600px]:gap-2">
          <button className="btn btn-ghost" disabled={currentQ === 0} onClick={() => setCurrentQ(c => c - 1)}>← Previous</button>
          {currentQ < totalQ - 1 ? (
            <button className="btn btn-primary" onClick={() => setCurrentQ(c => c + 1)}>Next →</button>
          ) : (
            <button className="btn btn-success" onClick={handleSubmit} disabled={!allAnswered || submitting}>
              {submitting ? 'Submitting...' : 'Submit Review'}
            </button>
          )}
        </div>
      </div>
    </div>
  )
}
