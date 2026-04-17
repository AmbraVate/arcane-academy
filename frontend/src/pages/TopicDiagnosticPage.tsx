import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { diagnosticApi } from '../api/services'
import type { ReviewSessionDto, DiagnosticResultDto, AnswerEntry } from '../types'
import QuestionCard from '../components/quest/QuestionCard'
import styles from './DiagnosticPage.module.css'

const TOPIC_META: Record<string, { name: string; glyph: string }> = {
  tailwind: { name: 'Tailwind CSS', glyph: '🎨' },
}

export default function TopicDiagnosticPage() {
  const { topicId } = useParams<{ topicId: string }>()
  const navigate = useNavigate()
  const [session, setSession] = useState<ReviewSessionDto | null>(null)
  const [loading, setLoading] = useState(false)
  const [currentQ, setCurrentQ] = useState(0)
  const [answers, setAnswers] = useState<Record<string, string>>({})
  const [result, setResult] = useState<DiagnosticResultDto | null>(null)
  const [submitting, setSubmitting] = useState(false)

  const meta = TOPIC_META[topicId ?? ''] ?? { name: topicId, glyph: '📖' }

  async function handleStart() {
    setLoading(true)
    try {
      const sess = await diagnosticApi.start(topicId!)
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
      const res = await diagnosticApi.submit(answerList, topicId!)
      setResult(res)
    } catch {
      // error
    } finally {
      setSubmitting(false)
    }
  }

  // Intro
  if (!session && !result) {
    return (
      <div className={styles.intro}>
        <div className={styles.introGlyph}>{meta.glyph}</div>
        <h1>{meta.name} — Entry Diagnostic</h1>
        <p>Answer a few questions to find your starting point. We'll skip concepts you already know.</p>
        <p className={styles.detail}>A few questions · ~5 minutes</p>
        <button className="btn btn-primary" onClick={handleStart} disabled={loading}>
          {loading ? 'Preparing...' : 'Begin Diagnostic →'}
        </button>
        <button className="btn btn-ghost" onClick={() => navigate(`/topic/${topicId}`)} style={{ marginTop: 10, fontSize: 12 }}>
          Skip — start from the beginning
        </button>
      </div>
    )
  }

  // Results
  if (result) {
    const scoreColor = result.overallScore >= 0.8 ? 'var(--teal)' : result.overallScore >= 0.5 ? 'var(--gold)' : 'var(--purple-light)'
    return (
      <div className={styles.page}>
        <div className={styles.resultPanel}>
          <div className={styles.resultGlyph}>✦</div>
          <h2 className={styles.resultTitle}>Diagnostic Complete</h2>
          <div className={styles.overallScore} style={{ color: scoreColor }}>
            Score: {Math.round(result.overallScore * 100)}%
          </div>
          <p style={{ color: 'var(--muted)', fontSize: 14, marginTop: 8 }}>
            {result.overallScore >= 0.8
              ? 'Excellent knowledge! Some chunks have been marked for quick review.'
              : result.overallScore >= 0.5
              ? 'Good foundation. We\'ll focus on the gaps.'
              : 'Starting from scratch — we\'ll build your knowledge step by step.'}
          </p>
          <button className="btn btn-primary" onClick={() => navigate(`/topic/${topicId}`)} style={{ marginTop: 20 }}>
            Start Learning →
          </button>
        </div>
      </div>
    )
  }

  // Question flow
  const question = session!.questions[currentQ]
  const totalQ = session!.questions.length
  const allAnswered = Object.keys(answers).length === totalQ
  const pct = Math.round((currentQ / totalQ) * 100)

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.headerTitle}>{meta.name} — Diagnostic</div>
        <div className={styles.progress}>{currentQ + 1} / {totalQ}</div>
      </div>

      <div className={styles.progressBarWrap}>
        <div className={styles.progressBar} style={{ width: `${pct}%` }} />
      </div>

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
            {submitting ? 'Analyzing...' : '⚡ Submit Diagnostic'}
          </button>
        )}
      </div>
    </div>
  )
}
