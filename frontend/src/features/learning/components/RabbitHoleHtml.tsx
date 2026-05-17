import { useEffect, useRef, useState, useCallback, useMemo } from 'react'
import { cn } from '@/lib/utils'
import type { StoryRabbitHoleTerm } from '@/shared/types'
import { rabbitHoleTermApi } from '@/shared/api/services'

interface Popover { term: string; description: string; x: number; y: number }

interface Props {
  html: string
  terms?: StoryRabbitHoleTerm[] | null
  className?: string
  subChunkId?: string
  topicId?: string
}

/** Inject data-rh / data-rh-desc spans into text nodes only (not tag attributes). */
export function annotateTerms(html: string, terms: StoryRabbitHoleTerm[]): string {
  if (!terms.length) return html
  const parts = html.split(/(<[^>]+>)/)
  return parts.map((part, i) => {
    if (i % 2 === 1) return part // it's a tag — skip
    let text = part
    for (const { term, description } of terms) {
      const escaped = term.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
      const re = new RegExp(`\\b(${escaped})\\b`, 'gi')
      const safeDesc = description.replace(/"/g, '&quot;')
      text = text.replace(re, `<span data-rh="${term}" data-rh-desc="${safeDesc}">$1</span>`)
    }
    return text
  }).join('')
}

/**
 * Renders arbitrary HTML with rabbit-hole term annotation and a save-to-queue popover.
 * Pass a `className` for the wrapper div's styling — identical to the plain
 * `dangerouslySetInnerHTML` div it replaces.
 */
export default function RabbitHoleHtml({ html, terms, className, subChunkId, topicId }: Props) {
  const containerRef = useRef<HTMLDivElement>(null)
  const [popover, setPopover] = useState<Popover | null>(null)
  const [savedTerms, setSavedTerms] = useState<Set<string>>(new Set())
  const [saving, setSaving] = useState(false)

  const resolvedTerms = useMemo(() => terms ?? [], [terms])

  const annotatedHtml = useMemo(
    () => resolvedTerms.length ? annotateTerms(html, resolvedTerms) : html,
    [html, resolvedTerms],
  )

  const handleClick = useCallback((e: MouseEvent) => {
    const target = (e.target as HTMLElement).closest('[data-rh]') as HTMLElement | null
    if (!target) { setPopover(null); return }
    const term = target.dataset.rh ?? ''
    const description = target.dataset.rhDesc ?? ''
    const rect = target.getBoundingClientRect()
    setPopover({ term, description, x: rect.left + rect.width / 2, y: rect.bottom + 6 })
    e.stopPropagation()
  }, [])

  useEffect(() => {
    const el = containerRef.current
    if (!el) return
    el.addEventListener('click', handleClick)
    return () => el.removeEventListener('click', handleClick)
  }, [handleClick])

  useEffect(() => {
    const close = () => setPopover(null)
    document.addEventListener('click', close)
    return () => document.removeEventListener('click', close)
  }, [])

  async function handleSave() {
    if (!popover || saving) return
    setSaving(true)
    try {
      await rabbitHoleTermApi.save(popover.term, popover.description, subChunkId ?? '', topicId ?? '')
      setSavedTerms(prev => new Set(prev).add(popover.term))
    } catch { /* ignore */ } finally { setSaving(false) }
  }

  async function handleUnsave() {
    if (!popover || saving) return
    setSaving(true)
    try {
      await rabbitHoleTermApi.remove(popover.term)
      setSavedTerms(prev => { const s = new Set(prev); s.delete(popover.term); return s })
    } catch { /* ignore */ } finally { setSaving(false) }
  }

  return (
    <>
      <div
        ref={containerRef}
        className={cn(
          // Rabbit-hole term styling injected here so any consumer gets it for free
          '[&_[data-rh]]:cursor-pointer [&_[data-rh]]:underline [&_[data-rh]]:decoration-dotted [&_[data-rh]]:text-gold [&_[data-rh]]:transition-opacity [&_[data-rh]]:duration-150 [&_[data-rh]:hover]:opacity-80',
          className,
        )}
        dangerouslySetInnerHTML={{ __html: annotatedHtml }}
      />

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
