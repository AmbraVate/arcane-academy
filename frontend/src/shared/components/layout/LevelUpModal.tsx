import { useEffect, useState } from 'react'
import { cn } from '@/lib/utils'

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
    const t = setTimeout(() => setVisible(true), 50)
    return () => clearTimeout(t)
  }, [])

  function handleClose() {
    setVisible(false)
    setTimeout(onClose, 300)
  }

  return (
    <div
      className={cn(
        'fixed inset-0 bg-black/75 flex items-center justify-center z-[500] transition-opacity duration-300',
        visible ? 'opacity-100' : 'opacity-0'
      )}
      onClick={handleClose}
    >
      <div
        className={cn(
          'bg-card border border-gold-dim rounded-[14px] p-9 px-8 max-w-[380px] w-[90%] text-center',
          'transition-[transform,opacity] duration-[350ms] ease-[cubic-bezier(0.34,1.56,0.64,1)]',
          visible ? 'scale-100 translate-y-0 opacity-100' : 'scale-[0.85] translate-y-5 opacity-0'
        )}
        onClick={e => e.stopPropagation()}
      >
        <div className="text-[56px] mb-3 animate-level-pop">{info.icon}</div>
        <div className="font-cinzel text-[12px] tracking-[3px] text-gold mb-1.5 uppercase">
          Level {newLevel}
        </div>
        <div className="font-cinzel text-[28px] text-gold mb-4">{info.title}</div>
        <div className="text-[15px] leading-[1.7] text-text italic mb-3">{info.lore}</div>
        <div className="text-[12px] text-muted mb-6 font-cinzel tracking-[1px]">
          Keep casting spells to rise further
        </div>
        <button
          className="bg-gold-dim border border-gold text-gold px-6 py-2.5 rounded-[7px] cursor-pointer
            font-cinzel text-[13px] tracking-[1px] transition-[background] duration-200 hover:bg-[#6a4c0e]"
          onClick={handleClose}
        >
          Continue your journey →
        </button>
      </div>
    </div>
  )
}
