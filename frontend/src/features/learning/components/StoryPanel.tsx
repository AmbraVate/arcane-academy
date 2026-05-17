import { useCallback, useEffect, useRef, useState } from 'react'
import { cn } from '@/lib/utils'
import { rabbitHoleTermApi } from '@/shared/api/services'
import type { StoryBeat } from '@/shared/types'

export default function StoryPanel({
  beats,
  fullPage = false,
  subChunkId,
  topicId,
  rabbitHoleTerms,
}: {
  beats: StoryBeat[]
  fullPage?: boolean
  subChunkId?: string
  topicId?: string
  rabbitHoleTerms?: Record<string, string> | null
}) {
  const containerRef = useRef<HTMLDivElement>(null)
  const [popover, setPopover] = useState<{ x: number; y: number; term: string; description: string } | null>(null)
  const [savedTerms, setSavedTerms] = useState<Set<string>>(new Set())
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    rabbitHoleTermApi.getAll().then(terms => {
      setSavedTerms(new Set(terms.map(t => t.term)))
    }).catch(() => {})
  }, [])

  useEffect(() => {
    const container = containerRef.current
    if (!container || !rabbitHoleTerms) return
    const handleClick = (e: MouseEvent) => {
      const el = (e.target as Element).closest('[data-rh]')
      if (!el) { setPopover(null); return }
      const term = el.getAttribute('data-rh') ?? ''
      const description = rabbitHoleTerms[term] ?? ''
      const rect = el.getBoundingClientRect()
      setPopover({ x: rect.left, y: rect.bottom + 6, term, description })
    }
    container.addEventListener('click', handleClick)
    return () => container.removeEventListener('click', handleClick)
  }, [rabbitHoleTerms])

  useEffect(() => {
    if (!popover) return
    const dismiss = () => setPopover(null)
    document.addEventListener('click', dismiss)
    return () => document.removeEventListener('click', dismiss)
  }, [popover])

  const handleSave = useCallback(async () => {
    if (!popover || !subChunkId || !topicId) return
    setSaving(true)
    try {
      await rabbitHoleTermApi.save(popover.term, popover.description, subChunkId, topicId)
      setSavedTerms(prev => new Set([...prev, popover.term]))
    } finally {
      setSaving(false)
    }
  }, [popover, subChunkId, topicId])

  const handleUnsave = useCallback(async () => {
    if (!popover) return
    setSaving(true)
    try {
      await rabbitHoleTermApi.remove(popover.term)
      setSavedTerms(prev => { const s = new Set(prev); s.delete(popover.term); return s })
    } finally {
      setSaving(false)
    }
  }, [popover])

  return (
    <>
      <div ref={containerRef} className="flex flex-col gap-3">
        {beats.map((beat, i) => {
          if (beat.type === 'narration') return <Narration key={i} text={beat.text} fullPage={fullPage} />
          if (beat.type === 'example')  return <Example key={i} beat={beat} fullPage={fullPage} />
          return <Dialogue key={i} beat={beat} fullPage={fullPage} />
        })}
      </div>

      {popover && (
        <div
          className="fixed z-[400] max-w-[280px] bg-card border border-[rgba(139,92,246,0.4)] rounded-[10px] shadow-[0_6px_24px_rgba(0,0,0,0.5)] p-3.5"
          style={{ left: Math.min(popover.x, window.innerWidth - 288), top: popover.y }}
          onClick={e => e.stopPropagation()}
        >
          <div className="text-[13px] font-bold text-gold mb-1">🐇 {popover.term}</div>
          {popover.description && (
            <p className="text-[12px] text-muted leading-[1.55] mb-2.5">{popover.description}</p>
          )}
          {savedTerms.has(popover.term) ? (
            <button
              className="text-[11px] px-3 py-1.5 rounded-md bg-teal-dim text-teal border border-teal cursor-pointer"
              onClick={handleUnsave} disabled={saving}
            >
              {saving ? '…' : '✓ Saved — Remove'}
            </button>
          ) : (
            <button
              className="text-[11px] px-3 py-1.5 rounded-md bg-purple-dim text-purple-light border border-[rgba(139,92,246,0.4)] cursor-pointer hover:bg-[rgba(139,92,246,0.2)]"
              onClick={handleSave} disabled={saving}
            >
              {saving ? '…' : '🐇 Save to Rabbit Holes'}
            </button>
          )}
        </div>
      )}
    </>
  )
}

function Narration({ text, fullPage }: { text: string; fullPage: boolean }) {
  return (
    <div
      className={cn(
        'font-crimson italic text-muted leading-[1.75] pl-2.5 border-l-2 border-border',
        fullPage && 'text-[16px] leading-[1.9] py-3.5 px-5 border-l-[3px] border-purple bg-[rgba(139,92,246,0.05)] rounded-r-lg mb-5 text-text text-[13px]',
        !fullPage && 'text-[13px]',
        '[&_[data-rh]]:cursor-pointer [&_[data-rh]]:underline [&_[data-rh]]:decoration-dotted [&_[data-rh]]:text-gold',
      )}
      dangerouslySetInnerHTML={{ __html: text }}
    />
  )
}

function Example({ beat, fullPage }: { beat: StoryBeat; fullPage: boolean }) {
  return (
    <div
      className={cn(
        'bg-[#09070f] border border-purple-dim border-l-[3px] border-l-purple rounded-r-lg my-1 mb-3 overflow-hidden',
        fullPage && 'border-l-4',
      )}
    >
      {beat.speaker && (
        <div
          className={cn(
            'font-cinzel text-[10px] tracking-[2px] text-purple-light px-3.5 py-[7px] pb-[5px] border-b border-border',
            fullPage && 'text-[12px]',
          )}
        >
          ✦ {beat.speaker}
        </div>
      )}
      <pre
        className={cn(
          'px-3.5 py-3 font-mono text-[12px] leading-[1.7] text-[#e2e8f0] m-0 whitespace-pre overflow-x-auto',
          fullPage && 'text-[13px] leading-[1.8]',
        )}
      >
        <code dangerouslySetInnerHTML={{ __html: beat.text }} />
      </pre>
    </div>
  )
}

function Dialogue({ beat, fullPage }: { beat: StoryBeat; fullPage: boolean }) {
  const avatarBorder =
    beat.cls === 'mentor' ? 'border-gold' :
    beat.cls === 'enemy'  ? 'border-red'  :
    'border-teal'

  const speakerColor =
    beat.sCls === 's-mentor' ? 'text-gold' :
    beat.sCls === 's-enemy'  ? 'text-red'  :
    'text-teal'

  return (
    <div className="flex gap-2.5">
      <div
        className={cn(
          'rounded-full border-2 flex items-center justify-center flex-shrink-0 bg-surface',
          'w-10 h-10 text-[19px]',
          fullPage && 'w-[52px] h-[52px] text-[24px]',
          avatarBorder,
        )}
      >
        {beat.av}
      </div>
      <div
        className={cn(
          'bg-card border border-border rounded-[0_9px_9px_9px] px-[13px] py-[9px] flex-1',
          fullPage && 'px-[22px] py-4 rounded-[0_14px_14px_14px] bg-[rgba(255,255,255,0.03)] border-[rgba(255,255,255,0.08)]',
        )}
      >
        <div className={cn('font-cinzel text-[10px] text-muted mb-1', fullPage && 'text-[11px]', speakerColor)}>
          {beat.speaker}
        </div>
        <div
          className={cn(
            'text-[13px] leading-[1.75] [&_em]:text-teal [&_em]:not-italic [&_em]:font-semibold',
            '[&_[data-rh]]:cursor-pointer [&_[data-rh]]:underline [&_[data-rh]]:decoration-dotted [&_[data-rh]]:text-gold',
            fullPage && 'text-[15px] leading-[1.8]',
          )}
          dangerouslySetInnerHTML={{ __html: beat.text ?? '' }}
        />
      </div>
    </div>
  )
}
