import { useEffect, useRef, useState } from 'react'
import styles from './TailwindEditor.module.css'

interface TailwindEditorProps {
  value: string
  onChange: (html: string) => void
  disabled?: boolean
}

type Viewport = 'mobile' | 'tablet' | 'desktop'

// Device presets drive both the iframe width and the `sm:/md:/lg:` breakpoints
// that fire inside it. Widths chosen to land inside each Tailwind breakpoint range.
const VIEWPORTS: Record<Viewport, { label: string; icon: string; width: number | null; deviceLabel: string }> = {
  mobile:  { label: 'Mobile',  icon: '📱', width: 390,  deviceLabel: '390 px' },
  tablet:  { label: 'Tablet',  icon: '📱', width: 820,  deviceLabel: '820 px' },
  desktop: { label: 'Desktop', icon: '🖥️', width: null, deviceLabel: '100%' },
}

/**
 * Split-pane HTML editor with live Tailwind CDN preview.
 *
 * Left pane  — textarea for raw HTML (the student writes Tailwind classes here)
 * Right pane — sandboxed iframe with Tailwind CDN, resizable between
 *              Mobile / Tablet / Desktop to demonstrate responsive utilities.
 */
export default function TailwindEditor({ value, onChange, disabled = false }: TailwindEditorProps) {
  const [previewHtml, setPreviewHtml] = useState(value)
  const [viewport, setViewport] = useState<Viewport>('desktop')
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
    html, body { margin: 0; }
    body { font-family: sans-serif; }
  </style>
</head>
<body class="p-6 bg-gray-50 min-h-screen">
  ${previewHtml}
</body>
</html>`

  const preset = VIEWPORTS[viewport]

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
          <span className={styles.previewTitle}>Preview</span>

          <div className={styles.viewportSwitch} role="tablist" aria-label="Preview viewport">
            {(Object.keys(VIEWPORTS) as Viewport[]).map(key => (
              <button
                key={key}
                type="button"
                role="tab"
                aria-selected={viewport === key}
                className={`${styles.viewportBtn} ${viewport === key ? styles.viewportBtnActive : ''}`}
                onClick={() => setViewport(key)}
                title={`${VIEWPORTS[key].label} — ${VIEWPORTS[key].deviceLabel}`}
              >
                <span className={styles.viewportIcon}>{VIEWPORTS[key].icon}</span>
                <span className={styles.viewportLabel}>{VIEWPORTS[key].label}</span>
              </button>
            ))}
          </div>

          <span className={styles.viewportSize}>{preset.deviceLabel}</span>
        </div>

        <div className={styles.previewStage}>
          <div
            className={`${styles.previewFrame} ${viewport !== 'desktop' ? styles.previewFrameDevice : ''}`}
            style={preset.width != null ? { width: preset.width } : undefined}
          >
            <iframe
              className={styles.iframe}
              srcDoc={srcdoc}
              title="Tailwind live preview"
              sandbox="allow-scripts"
            />
          </div>
        </div>
      </div>
    </div>
  )
}
