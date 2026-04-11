import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { diagnosticApi } from '../api/services'
import type { ReviewSessionDto, DiagnosticResultDto, QuestionDto, AnswerEntry } from '../types'
import styles from './DiagnosticPage.module.css'

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
    try {
      const sess = await diagnosticApi.start()
      setSession(sess)
    } catch {
      // error
    } finally {
      setLoading(false)
    }
  }

  async function handleSubmit() {
    if (!session) return
    setSubmitting(true)
    try {
      const answerList: AnswerEntry[] = Object.entries(answers).map(([questionId, answer]) => ({ questionId, answer }))
      const res = await diagnosticApi.submit(answerList)
      setResult(res)
    } catch {
      // error
    } finally {
      setSubmitting(false)
    }
  }

  // Intro screen
  if (!session && !result) {
    return (
      <div className={styles.intro}>
        <div className={styles.introGlyph}>🔮</div>
        <h1>Entry Diagnostic</h1>
        <p>Answer a few questions to find your starting point. We'll skip concepts you already know and focus on what matters.</p>
        <p className={styles.detail}>22 questions, ~10 minutes</p>
        <button className="btn btn-primary" onClick={handleStart} disabled={loading}>
          {loading ? 'Preparing...' : 'Begin Diagnostic →'}
        </button>
        <button className="btn btn-ghost" onClick={() => navigate('/')} style={{ marginTop: 10, fontSize: 12 }}>
          Skip — start from the beginning
        </button>
      </div>
    )
  }

  // Results screen
  if (result) {
    const recommendations = Object.entries(result.chunkRecommendations)
    return (
      <div className={styles.page}>
        <div className={styles.resultPanel}>
          <div className={styles.resultGlyph}>✦</div>
          <h2>Diagnostic Complete</h2>
          <div className={styles.pathResult}>
            Recommended Path: <strong>{result.recommendedPath}</strong>
          </div>
          <div className={styles.overallScore}>
            Overall Score: {Math.round(result.overallScore * 100)}%
          </div>

          <div className={styles.recList}>
            {recommendations.map(([chunk, rec]) => (
              <div key={chunk} className={`${styles.recItem} ${styles[`rec${rec}`]}`}>
                <span className={styles.recChunk}>Chunk {chunk}</span>
                <span className={`chip ${rec === 'SKIP' ? 'chip-teal' : rec === 'COMPRESS' ? 'chip-gold' : 'chip-purple'}`}>
                  {rec}
                </span>
              </div>
            ))}
          </div>

          <button className="btn btn-primary" onClick={() => navigate('/')} style={{ marginTop: 20 }}>
            Go to Dashboard →
          </button>
        </div>
      </div>
    )
  }

  // Question flow
  const question = session!.questions[currentQ]
  const totalQ = session!.questions.length
  const allAnswered = Object.keys(answers).length === totalQ

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.headerTitle}>Entry Diagnostic</div>
        <div className={styles.progress}>{currentQ + 1} / {totalQ}</div>
      </div>

      <div className={styles.questionCard}>
        <div className={styles.qHtml} dangerouslySetInnerHTML={{ __html: question.questionHtml }} />
        {question.codeSnippet && (
          <pre className={styles.qCode}><code>{question.codeSnippet}</code></pre>
        )}

        {question.options && (
          <div className={styles.qOptions}>
            {question.options.map(opt => (
              <label key={opt} className={`${styles.qOption} ${answers[question.id] === opt ? styles.qSelected : ''}`}>
                <input type="radio" name={question.id} checked={answers[question.id] === opt} onChange={() => setAnswers(prev => ({ ...prev, [question.id]: opt }))} />
                {opt}
              </label>
            ))}
          </div>
        )}

        {!question.options && (
          <textarea className={styles.qInput} placeholder="Your answer..." value={answers[question.id] ?? ''} onChange={e => setAnswers(prev => ({ ...prev, [question.id]: e.target.value }))} rows={3} />
        )}
      </div>

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
            {submitting ? 'Analyzing...' : '⚡ Submit Diagnostic'}
          </button>
        )}
      </div>
    </div>
  )
}
