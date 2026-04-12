import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { reviewApi } from '../api/services'
import type { ReviewSessionDto, ReviewResultDto, AnswerEntry, Badge } from '../types'
import QuestionCard from '../components/quest/QuestionCard'
import BadgeToast from '../components/layout/BadgeToast'
import styles from './ReviewPage.module.css'

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
    const scoreColor = result.score >= 0.8 ? 'var(--green)' : result.score >= 0.5 ? 'var(--orange)' : 'var(--red)'
    return (
      <div className={styles.page}>
        <div className={styles.resultHeader}>
          <div className={styles.resultGlyph}>📖</div>
          <h2 className={styles.resultTitle}>Review Complete</h2>
          <div className={styles.resultScore} style={{ color: scoreColor }}>
            {Math.round(result.score * 100)}%
          </div>
          <div className={styles.resultSub}>{result.correct} / {result.total} correct</div>
        </div>
        <div className={styles.resultCards}>
          {session.questions.map((q, i) => (
            <QuestionCard
              key={q.id} question={q} index={i}
              answer={result.results[i]?.userAnswer ?? ''}
              onChange={() => {}}
              result={result.results[i]}
              disabled
            />
          ))}
        </div>
        <button className="btn btn-primary" onClick={() => navigate('/')} style={{ marginTop: 8 }}>
          Back to Dashboard
        </button>
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
        <div className={styles.progressPips}>
          {session.questions.map((q, i) => (
            <button
              key={q.id}
              className={`${styles.pip} ${i === currentQ ? styles.pipActive : ''} ${answers[q.id] ? styles.pipDone : ''}`}
              onClick={() => setCurrentQ(i)}
              title={`Question ${i + 1}`}
            />
          ))}
        </div>
        <div className={styles.progress}>{currentQ + 1} / {totalQ}</div>
      </div>

      <div className={styles.questionArea}>
        <QuestionCard
          question={question}
          index={currentQ}
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
