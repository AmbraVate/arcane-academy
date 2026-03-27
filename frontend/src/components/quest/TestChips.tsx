import type { TestCaseLabel } from '../../types'
import styles from './TestChips.module.css'

interface Props {
  labels: TestCaseLabel[]
  results: Map<string, boolean>
}

export default function TestChips({ labels, results }: Props) {
  if (!labels?.length) return null

  return (
    <div className={styles.row}>
      {labels.map(({ label }) => {
        const result = results.get(label)
        const cls = result === undefined
          ? styles.neutral
          : result ? styles.pass : styles.fail

        return (
          <span key={label} className={`${styles.chip} ${cls}`}>
            {result === true && '✓ '}
            {result === false && '✗ '}
            {label}
          </span>
        )
      })}
    </div>
  )
}
