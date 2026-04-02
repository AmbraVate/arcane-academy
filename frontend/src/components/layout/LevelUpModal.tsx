import { useEffect, useState } from 'react'
import styles from './LevelUpModal.module.css'

interface Props {
  newLevel: number
  newRank: string
  onClose: () => void
}

const RANK_LORE: Record<string, { title: string; lore: string; icon: string }> = {
  Novice:     { icon: '🌱', title: 'Novice',     lore: 'You have taken your first steps into the arcane arts. The Academy acknowledges your arrival.' },
  Apprentice: { icon: '⚗️', title: 'Apprentice', lore: 'The binding runes hold. Master Velan admits you to the inner study halls.' },
  Adept:      { icon: '📜', title: 'Adept',       lore: 'Your spells compile cleanly and your logic is sound. The senior wizards take notice.' },
  Mage:       { icon: '🔮', title: 'Mage',        lore: 'You have mastered the fundamentals. The Academy grants you access to the restricted tomes.' },
  Archmage:   { icon: '⚡', title: 'Archmage',    lore: 'The highest honour the Academy bestows. You are no longer a student — you are a wizard.' },
}

export default function LevelUpModal({ newLevel, newRank, onClose }: Props) {
  const [visible, setVisible] = useState(false)
  const info = RANK_LORE[newRank] ?? RANK_LORE.Novice

  useEffect(() => {
    // Delay slightly so CSS transition fires
    const t = setTimeout(() => setVisible(true), 50)
    return () => clearTimeout(t)
  }, [])

  function handleClose() {
    setVisible(false)
    setTimeout(onClose, 300)
  }

  return (
    <div className={`${styles.overlay} ${visible ? styles.overlayVisible : ''}`} onClick={handleClose}>
      <div className={`${styles.modal} ${visible ? styles.modalVisible : ''}`} onClick={e => e.stopPropagation()}>
        <div className={styles.glyph}>{info.icon}</div>
        <div className={styles.levelBadge}>Level {newLevel}</div>
        <div className={styles.rankName}>{info.title}</div>
        <div className={styles.lore}>{info.lore}</div>
        <div className={styles.xpLine}>Keep casting spells to rise further</div>
        <button className={styles.btn} onClick={handleClose}>
          Continue your journey →
        </button>
      </div>
    </div>
  )
}
