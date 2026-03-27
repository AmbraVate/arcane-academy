import styles from './AiMentorPanel.module.css'

interface Props {
  feedback: string | null
  loading: boolean
}

export default function AiMentorPanel({ feedback, loading }: Props) {
  if (!loading && !feedback) return null

  return (
    <div className={styles.panel}>
      <div className={styles.header}>
        <div className={`${styles.dot} ${loading ? styles.pulsing : styles.still}`} />
        <span>{loading ? 'Master Velan is thinking...' : 'Master Velan says:'}</span>
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
