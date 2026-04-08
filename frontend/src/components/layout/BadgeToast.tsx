import { useEffect, useState } from 'react'
import type { Badge } from '../../types'
import styles from './BadgeToast.module.css'

interface Props {
  badges: Badge[]
  onDone: () => void
}

export default function BadgeToast({ badges, onDone }: Props) {
  const [currentIdx, setCurrentIdx] = useState(0)
  const [visible, setVisible] = useState(true)

  useEffect(() => {
    if (badges.length === 0) { onDone(); return }

    const timer = setTimeout(() => {
      if (currentIdx < badges.length - 1) {
        setVisible(false)
        setTimeout(() => { setCurrentIdx(i => i + 1); setVisible(true) }, 300)
      } else {
        setVisible(false)
        setTimeout(onDone, 300)
      }
    }, 3000)

    return () => clearTimeout(timer)
  }, [currentIdx, badges.length, onDone])

  if (badges.length === 0 || currentIdx >= badges.length) return null

  const badge = badges[currentIdx]

  return (
    <div className={`${styles.toast} ${visible ? styles.show : styles.hide}`}>
      <div className={styles.glyph}>{badge.glyph}</div>
      <div className={styles.content}>
        <div className={styles.label}>Badge Earned!</div>
        <div className={styles.name}>{badge.displayName}</div>
        <div className={styles.desc}>{badge.description}</div>
      </div>
    </div>
  )
}
