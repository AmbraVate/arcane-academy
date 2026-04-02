import styles from './AiMentorPanel.module.css'

interface Props {
  feedback: string | null
  loading: boolean
  errorType?: string | null
}

export default function AiMentorPanel({ feedback, loading, errorType }: Props) {
  if (!loading && !feedback) return null

  const icon = errorType === 'COMPILE_ERROR' ? '📜' : errorType === 'RUNTIME_ERROR' ? '⚡' : '🧙'
  const label = loading
    ? 'Master Velan is reading your spell...'
    : errorType === 'COMPILE_ERROR'
    ? 'Master Velan spots a syntax problem:'
    : errorType === 'RUNTIME_ERROR'
    ? 'Master Velan sees what went wrong:'
    : 'Master Velan says:'

  return (
    <div className={styles.panel}>
      <div className={styles.header}>
        <div className={`${styles.dot} ${loading ? styles.pulsing : styles.still}`} />
        <span>{icon} {label}</span>
      </div>
      <div className={styles.body}>
        {loading
          ? <span className={styles.typing}>▌</span>
          : feedback
        }
      </div>
    </div>
  )
}
