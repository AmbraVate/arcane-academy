import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { diagnosticApi } from '../api/services'
import styles from './OnboardingPage.module.css'

const TOPIC_META: Record<string, { name: string; glyph: string; question: string }> = {
  tailwind: {
    name: 'Tailwind CSS',
    glyph: '🎨',
    question: 'Have you used Tailwind CSS before?',
  },
}

export default function TopicOnboardingPage() {
  const { topicId } = useParams<{ topicId: string }>()
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)

  const meta = TOPIC_META[topicId ?? ''] ?? { name: topicId, glyph: '📖', question: `Have you studied ${topicId} before?` }

  async function handleNew() {
    setLoading(true)
    try {
      await diagnosticApi.skip(topicId!)
      navigate(`/topic/${topicId}`, { replace: true })
    } catch {
      setLoading(false)
    }
  }

  function handleExperienced() {
    navigate(`/topic/${topicId}/diagnostic`)
  }

  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <div className={styles.glyph}>{meta.glyph}</div>
        <h1 className={styles.title}>{meta.name}</h1>
        <p className={styles.subtitle}>
          Before we chart your path through {meta.name}, let us know where you stand.
        </p>
        <p className={styles.question}>{meta.question}</p>

        <div className={styles.choices}>
          <button
            className={`${styles.choice} ${styles.choiceNew}`}
            onClick={handleNew}
            disabled={loading}
          >
            <span className={styles.choiceGlyph}>🌱</span>
            <span className={styles.choiceTitle}>No, I'm completely new</span>
            <span className={styles.choiceDesc}>
              Start from the very beginning. We'll guide you through every concept step by step.
            </span>
          </button>

          <button
            className={`${styles.choice} ${styles.choiceExp}`}
            onClick={handleExperienced}
            disabled={loading}
          >
            <span className={styles.choiceGlyph}>⚡</span>
            <span className={styles.choiceTitle}>Yes, I have some experience</span>
            <span className={styles.choiceDesc}>
              Take a short diagnostic — we'll skip what you already know and focus on the gaps.
            </span>
          </button>
        </div>
      </div>
    </div>
  )
}
