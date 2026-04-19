import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { diagnosticApi } from '../api/services'
import type { ReviewSessionDto, DiagnosticResultDto, AnswerEntry } from '../types'
import QuestionCard from '../components/quest/QuestionCard'
import { Badge } from '@/components/ui/badge'

export default function DiagnosticPage() {
  const navigate = useNavigate()
  const [session, setSession] = useState<ReviewSessionDto | null>(null)
  const [loading, setLoading] = useState(false)
  const [currentQ, setCurrentQ] = useState(0)
  const [answers, setAnswers] = useState<Record<string, string>>({})
  const [result, setResult] = useState<DiagnosticResultDto | null>(null)
  const [submitting, setSubmitting] = useState(false)

  async function handleStart() {
    setLoading(true)
    try { const sess = await diagnosticApi.start(); setSession(sess) }
    catch { /* noop */ } finally { setLoading(false) }
  }

  async function handleSubmit() {
    if (!session) return
    setSubmitting(true)
    try {
      const answerList: AnswerEntry[] = Object.entries(answers).map(([questionId, answer]) => ({ questionId, answer }))
      setResult(await diagnosticApi.submit(answerList))
    } catch { /* noop */ } finally { setSubmitting(false) }
  }

  if (!session && !result) {
    return (
      <div className="flex flex-col items-center justify-center h-[70vh] text-center max-w-[500px] mx-auto px-4 max-[600px]:h-auto max-[600px]:pt-10 max-[600px]:pb-10">
        <div className="text-[56px] mb-4 max-[600px]:text-[48px]">🔮</div>
        <h1 className="text-[28px] text-gold m-0 mb-3 max-[600px]:text-[24px]">Entry Diagnostic</h1>
        <p className="text-muted text-[14px] m-0 mb-2 leading-[1.6]">Answer a few questions to find your starting point. We'll skip concepts you already know and focus on what matters.</p>
        <p className="text-[12px] text-purple-light mb-5">22 questions · ~10 minutes</p>
        <button className="btn btn-primary" onClick={handleStart} disabled={loading}>
          {loading ? 'Preparing...' : 'Begin Diagnostic →'}
        </button>
        <button className="btn btn-ghost mt-2.5 text-[12px]" onClick={() => navigate('/topic/java')}>
          Skip — start from the beginning
        </button>
      </div>
    )
  }

  if (result) {
    const pathColor =
      result.recommendedPath === 'EXPERT' ? 'text-gold' :
      result.recommendedPath === 'PRACTITIONER' ? 'text-purple-light' : 'text-teal'
    return (
      <div className="max-w-[700px] mx-auto px-4 py-6 pb-[60px] max-[600px]:px-3">
        <div className="text-center px-6 py-9 bg-card border border-border rounded-[14px] max-[600px]:px-4 max-[600px]:py-7">
          <div className="text-[48px] text-gold mb-3">✦</div>
          <h2 className="text-[24px] text-gold m-0 mb-3 max-[600px]:text-[20px]">Diagnostic Complete</h2>
          <div className="text-[20px] text-text mb-1.5">
            Your path: <strong className={pathColor}>{result.recommendedPath}</strong>
          </div>
          <div className="text-[13px] text-muted mb-6">
            Overall Score: {Math.round(result.overallScore * 100)}%
          </div>
          <div className="flex flex-col gap-2 max-w-[400px] mx-auto text-left mb-5 max-[600px]:max-w-full">
            {Object.entries(result.chunkRecommendations).map(([chunk, rec]) => (
              <div key={chunk} className="flex justify-between items-center px-3.5 py-2.5 bg-surface border border-border rounded-[8px] hover:border-purple transition-[border-color] duration-150">
                <span className="text-[13px] text-text font-medium">Chunk {chunk}</span>
                <Badge variant={rec === 'SKIP' ? 'active' : rec === 'COMPRESS' ? 'gold' : 'application'}>
                  {rec === 'SKIP' ? '⚡ Skip' : rec === 'COMPRESS' ? '⚡ Quick review' : '📖 Full study'}
                </Badge>
              </div>
            ))}
          </div>
          <button className="btn btn-primary" onClick={() => navigate('/topic/java')}>Go to Dashboard →</button>
        </div>
      </div>
    )
  }

  const question = session!.questions[currentQ]
  const totalQ   = session!.questions.length
  const allAnswered = Object.keys(answers).length === totalQ
  const pct = Math.round((currentQ / totalQ) * 100)

  return (
    <div className="max-w-[700px] mx-auto px-4 py-6 pb-[60px] max-[600px]:px-3">
      <div className="flex items-center mb-2">
        <div className="flex-1 text-[18px] font-bold text-gold">Entry Diagnostic</div>
        <div className="text-[13px] text-muted">{currentQ + 1} / {totalQ}</div>
      </div>
      <div className="h-1 bg-surface rounded-full mb-5 overflow-hidden">
        <div className="h-full rounded-full transition-[width] duration-400"
          style={{ width: `${pct}%`, background: 'linear-gradient(90deg, var(--purple), var(--teal))' }} />
      </div>
      <QuestionCard question={question} index={currentQ}
        answer={answers[question.id] ?? ''}
        onChange={v => setAnswers(prev => ({ ...prev, [question.id]: v }))} />
      <div className="flex justify-between mt-1">
        <button className="btn btn-ghost" disabled={currentQ === 0} onClick={() => setCurrentQ(c => c - 1)}>← Previous</button>
        {currentQ < totalQ - 1 ? (
          <button className="btn btn-primary" onClick={() => setCurrentQ(c => c + 1)}>Next →</button>
        ) : (
          <button className="btn btn-success" onClick={handleSubmit} disabled={!allAnswered || submitting}>
            {submitting ? 'Analyzing...' : '⚡ Submit Diagnostic'}
          </button>
        )}
      </div>
    </div>
  )
}
