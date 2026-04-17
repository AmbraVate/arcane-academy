import { useEffect, useRef, useState } from 'react'
import styles from './TailwindEditor.module.css'

interface TailwindEditorProps {
  value: string
  onChange: (html: string) => void
  disabled?: boolean
}

/**
 * Split-pane HTML editor with live Tailwind CDN preview.
 *
 * Left pane  — textarea for raw HTML (the student writes Tailwind classes here)
 * Right pane — sandboxed iframe with Tailwind CDN, updated 300 ms after last keystroke
 */
export default function TailwindEditor({ value, onChange, disabled = false }: TailwindEditorProps) {
  const [previewHtml, setPreviewHtml] = useState(value)
  const debounceRef = useRef<ReturnType<typeof setTimeout>>()

  // Debounce preview updates so the iframe isn't re-built on every keystroke
  useEffect(() => {
    clearTimeout(debounceRef.current)
    debounceRef.current = setTimeout(() => setPreviewHtml(value), 300)
    return () => clearTimeout(debounceRef.current)
  }, [value])

  const srcdoc = `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    body { font-family: sans-serif; }
  </style>
</head>
<body class="p-6 bg-gray-50 min-h-screen">
  ${previewHtml}
</body>
</html>`

  return (
    <div className={styles.editor}>
      {/* ── HTML textarea ── */}
      <div className={styles.pane}>
        <div className={styles.paneLabel}>
          <span className={styles.paneDot} style={{ background: 'var(--teal)' }} />
          HTML
        </div>
        <textarea
          className={styles.textarea}
          value={value}
          onChange={e => onChange(e.target.value)}
          disabled={disabled}
          spellCheck={false}
          autoComplete="off"
          autoCorrect="off"
          autoCapitalize="off"
        />
      </div>

      {/* ── Live preview ── */}
      <div className={styles.pane}>
        <div className={styles.paneLabel}>
          <span className={styles.paneDot} style={{ background: 'var(--purple)' }} />
          Preview
        </div>
        <iframe
          className={styles.iframe}
          srcDoc={srcdoc}
          title="Tailwind live preview"
          sandbox="allow-scripts"
        />
      </div>
    </div>
  )
}
