import { useRef, useEffect } from 'react'

interface Props {
  value: string
  onChange: (v: string) => void
  readOnly?: boolean
}

export default function CodeEditor({ value, onChange, readOnly = false }: Props) {
  const taRef = useRef<HTMLTextAreaElement>(null)
  const lnRef = useRef<HTMLDivElement>(null)

  const lineCount = value.split('\n').length

  useEffect(() => {
    if (!lnRef.current) return
    const lines = Array.from({ length: Math.max(lineCount, 20) }, (_, i) => i + 1)
    lnRef.current.innerHTML = lines.map(n => `<span>${n}</span>`).join('')
  }, [lineCount])

  function handleScroll() {
    if (taRef.current && lnRef.current) {
      lnRef.current.scrollTop = taRef.current.scrollTop
    }
  }

  function handleKeyDown(e: React.KeyboardEvent<HTMLTextAreaElement>) {
    if (e.key === 'Tab') {
      e.preventDefault()
      const ta = taRef.current!
      const start = ta.selectionStart
      const end = ta.selectionEnd
      const newVal = value.substring(0, start) + '    ' + value.substring(end)
      onChange(newVal)
      requestAnimationFrame(() => {
        ta.selectionStart = ta.selectionEnd = start + 4
      })
    }
  }

  return (
    <div className="flex-1 relative flex overflow-hidden min-h-0">
      {/* Line numbers */}
      <div
        ref={lnRef}
        className="w-9 bg-[#09070f] border-r border-border py-3.5 font-mono text-[13px] leading-[1.65] text-[#3d3560] text-right overflow-hidden select-none flex-shrink-0
          [&_span]:block [&_span]:pr-2"
      />
      {/* Editor textarea */}
      <textarea
        ref={taRef}
        className="flex-1 bg-[#0c0a1e] border-none outline-none p-3.5 font-mono text-[13px] leading-[1.65] text-[#e2e8f0] resize-none overflow-y-auto
          selection:bg-[rgba(139,92,246,0.3)]"
        style={{ tabSize: 4 }}
        value={value}
        onChange={e => onChange(e.target.value)}
        onKeyDown={handleKeyDown}
        onScroll={handleScroll}
        spellCheck={false}
        readOnly={readOnly}
        autoComplete="off"
        autoCorrect="off"
        autoCapitalize="off"
      />
    </div>
  )
}
