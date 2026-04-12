import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { diagnosticApi } from '../api/services'
import styles from './OnboardingPage.module.css'

export default function OnboardingPage() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)

  async function handleNew() {
    setLoading(true)
    try {
      await diagnosticApi.skip()
      navigate('/', { replace: true })
    } catch {
      setLoading(false)
    }
  }

  function handleExperienced() {
    navigate('/diagnostic')
  }

  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <div className={styles.glyph}>🧙</div>
        <h1 className={styles.title}>Welcome to Polymath Academy</h1>
        <p className={styles.subtitle}>
          Before we chart your path through the Java Grimoire, tell us where you stand.
        </p>
        <p className={styles.question}>Have you programmed in Java before?</p>

        <div className={styles.choices}>
          <button
            className={`${styles.choice} ${styles.choiceNew}`}
            onClick={handleNew}
            disabled={loading}
          >
            <span className={styles.choiceGlyph}>🌱</span>
            <span className={styles.choiceTitle}>No, I'm completely new</span>
            <span className={styles.choiceDesc}>
              Start from the very beginning. We'll guide you step by step from zero to job-ready.
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
