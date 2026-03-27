import { useEffect, useRef } from 'react'
import styles from './OutputPanel.module.css'

type OutputLine = { text: string; type: 'normal' | 'success' | 'error' | 'system' }

export default function OutputPanel({ lines }: { lines: OutputLine[] }) {
  const bodyRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (bodyRef.current) {
      bodyRef.current.scrollTop = bodyRef.current.scrollHeight
    }
  }, [lines])

  return (
    <div className={styles.panel}>
      <div className={styles.header}>
        <span>Arcane Console</span>
      </div>
      <div className={styles.body} ref={bodyRef}>
        {lines.map((line, i) => (
          <div key={i} className={`${styles.line} ${styles[line.type]}`}>
            {line.text}
          </div>
        ))}
      </div>
    </div>
  )
}
