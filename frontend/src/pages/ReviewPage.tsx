import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { reviewApi } from '../api/services'
import type { ReviewSessionDto, ReviewResultDto, QuestionDto, AnswerEntry, Badge } from '../types'
import BadgeToast from '../components/layout/BadgeToast'
import styles from './ReviewPage.module.css'

export default function ReviewPage() {
  const navigate = useNavigate()
  const { user, updateXp } = useAuth()
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
      .catch(() => navigate('/'))
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
    } catch {
      // error
    } finally {
      setSubmitting(false)
    }
  }

  if (loading) {
    return <div className={styles.loading}><p>Loading review session...</p></div>
  }

  if (!session || session.questions.length === 0) {
    return (
      <div className={styles.empty}>
        <div className={styles.emptyGlyph}>✦</div>
        <h2>No Reviews Due</h2>
        <p>All your memories are fresh! Check back later.</p>
        <button className="btn btn-primary" onClick={() => navigate('/')}>Back to Dashboard</button>
      </div>
    )
  }

  if (result) {
    return (
      <div className={styles.page}>
        <div className={styles.resultPanel}>
          <h2 className={styles.resultTitle}>Review Complete</h2>
          <div className={styles.resultScore}>
            {Math.round(result.score * 100)}% — {result.correct}/{result.total} correct
          </div>
          <div className={styles.resultList}>
            {result.results.map((r, i) => (
              <div key={i} className={`${styles.qResult} ${r.correct ? styles.correct : styles.wrong}`}>
                <span>{r.correct ? '✓' : '✗'} Q{i + 1}</span>
                {!r.correct && (
                  <div className={styles.qDetail}>
                    <div>Your answer: {r.userAnswer}</div>
                    <div>Correct: {r.correctAnswer}</div>
                    <div className={styles.qExpl} dangerouslySetInnerHTML={{ __html: r.explanationHtml }} />
                  </div>
                )}
              </div>
            ))}
          </div>
          <button className="btn btn-primary" onClick={() => navigate('/')} style={{ marginTop: 16 }}>
            Back to Dashboard
          </button>
        </div>
        {newBadges.length > 0 && <BadgeToast badges={newBadges} onDone={() => setNewBadges([])} />}
      </div>
    )
  }

  const question = session.questions[currentQ]
  const totalQ = session.questions.length
  const allAnswered = Object.keys(answers).length === totalQ

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <button className="btn btn-ghost" onClick={() => navigate('/')} style={{ fontSize: 12 }}>← Back</button>
        <div className={styles.headerTitle}>Daily Review</div>
        <div className={styles.progress}>{currentQ + 1} / {totalQ}</div>
      </div>

      <div className={styles.questionArea}>
        <ReviewQuestionCard
          question={question}
          answer={answers[question.id] ?? ''}
          onChange={v => setAnswers(prev => ({ ...prev, [question.id]: v }))}
        />

        <div className={styles.navButtons}>
          <button className="btn btn-ghost" disabled={currentQ === 0} onClick={() => setCurrentQ(c => c - 1)}>
            ← Previous
          </button>
          {currentQ < totalQ - 1 ? (
            <button className="btn btn-primary" onClick={() => setCurrentQ(c => c + 1)}>
              Next →
            </button>
          ) : (
            <button className="btn btn-success" onClick={handleSubmit} disabled={!allAnswered || submitting}>
              {submitting ? 'Submitting...' : '⚡ Submit Review'}
            </button>
          )}
        </div>
      </div>
    </div>
  )
}

function ReviewQuestionCard({ question, answer, onChange }: {
  question: QuestionDto; answer: string; onChange: (v: string) => void
}) {
  return (
    <div className={styles.questionCard}>
      <div className={styles.qTier}>{question.tier}</div>
      <div className={styles.qHtml} dangerouslySetInnerHTML={{ __html: question.questionHtml }} />
      {question.codeSnippet && (
        <pre className={styles.qCode}><code>{question.codeSnippet}</code></pre>
      )}

      {(question.type === 'MULTIPLE_CHOICE' || question.type === 'TRUE_FALSE') && question.options && (
        <div className={styles.qOptions}>
          {question.options.map(opt => (
            <label key={opt} className={`${styles.qOption} ${answer === opt ? styles.qSelected : ''}`}>
              <input type="radio" name={question.id} checked={answer === opt} onChange={() => onChange(opt)} />
              {opt}
            </label>
          ))}
        </div>
      )}

      {!['MULTIPLE_CHOICE', 'TRUE_FALSE'].includes(question.type) && (
        <textarea className={styles.qInput} placeholder="Your answer..." value={answer} onChange={e => onChange(e.target.value)} rows={3} />
      )}
    </div>
  )
}
