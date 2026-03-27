import { useRef, useEffect } from 'react'
import styles from './CodeEditor.module.css'

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

  // Sync scroll between textarea and line numbers
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
    <div className={styles.editor}>
      <div className={styles.lineNums} ref={lnRef} />
      <textarea
        ref={taRef}
        className={styles.textarea}
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
