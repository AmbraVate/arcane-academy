import { useEffect, useState } from 'react'
import type { Badge } from '@/shared/types'
import { cn } from '@/lib/utils'

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
    <div
      className={cn(
        // Base
        'fixed top-16 right-5 z-[1000] flex items-center gap-3.5 p-4 pr-[22px]',
        'bg-card border border-gold rounded-[12px] shadow-[0_0_24px_rgba(201,162,39,0.25)]',
        'animate-[shimmer_2s_ease-in-out_infinite] transition-[opacity,transform] duration-300',
        // Mobile override via media class - use inline style fallback
        // show/hide
        visible ? 'opacity-100 translate-x-0' : 'opacity-0 translate-x-10',
        // Mobile: bottom position handled below via a wrapper approach
      )}
      style={
        window.innerWidth <= 480
          ? {
              top: 'auto',
              bottom: 16,
              left: 12,
              right: 12,
              padding: '12px 16px',
              gap: 10,
            }
          : undefined
      }
    >
      <div className="text-[36px] flex-shrink-0 max-[480px]:text-[28px]">{badge.glyph}</div>
      <div className="flex flex-col gap-0.5">
        <div className="font-cinzel text-[10px] text-gold uppercase tracking-[2px]">Badge Earned!</div>
        <div className="font-cinzel text-[15px] text-text">{badge.displayName}</div>
        <div className="text-[12px] text-muted">{badge.description}</div>
      </div>
    </div>
  )
}
