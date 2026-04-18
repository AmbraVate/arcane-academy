import { useEffect, useRef } from 'react'
import { cn } from '@/lib/utils'

type OutputLine = { text: string; type: 'normal' | 'success' | 'error' | 'system' }

export default function OutputPanel({ lines }: { lines: OutputLine[] }) {
  const bodyRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (bodyRef.current) {
      bodyRef.current.scrollTop = bodyRef.current.scrollHeight
    }
  }, [lines])

  return (
    <div className="h-[160px] flex-shrink-0 border-t border-border flex flex-col">
      <div className="px-3.5 py-1.5 bg-surface border-b border-border font-cinzel text-[9px] tracking-[1px] text-muted flex-shrink-0">
        Arcane Console
      </div>
      <div ref={bodyRef} className="flex-1 overflow-y-auto px-3.5 py-2 bg-[#07050f] font-mono text-[12px] leading-[1.65]">
        {lines.map((line, i) => (
          <div
            key={i}
            className={cn(
              'mb-px',
              line.type === 'normal'  && 'text-[#94a3b8]',
              line.type === 'success' && 'text-green',
              line.type === 'error'   && 'text-red',
              line.type === 'system'  && 'text-purple-light italic',
            )}
          >
            {line.text}
          </div>
        ))}
      </div>
    </div>
  )
}
