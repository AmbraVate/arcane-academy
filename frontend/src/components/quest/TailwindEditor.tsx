import { useEffect, useRef, useState } from 'react'
import { cn } from '@/lib/utils'

interface TailwindEditorProps {
  value: string
  onChange: (html: string) => void
  disabled?: boolean
}

type Viewport = 'mobile' | 'tablet' | 'desktop'

const VIEWPORTS: Record<Viewport, { label: string; icon: string; width: number | null; deviceLabel: string }> = {
  mobile:  { label: 'Mobile',  icon: '📱', width: 390,  deviceLabel: '390 px' },
  tablet:  { label: 'Tablet',  icon: '📱', width: 820,  deviceLabel: '820 px' },
  desktop: { label: 'Desktop', icon: '🖥️', width: null, deviceLabel: '100%' },
}

export default function TailwindEditor({ value, onChange, disabled = false }: TailwindEditorProps) {
  const [previewHtml, setPreviewHtml] = useState(value)
  const [viewport, setViewport] = useState<Viewport>('desktop')
  const debounceRef = useRef<ReturnType<typeof setTimeout>>()

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
  <style>html, body { margin: 0; } body { font-family: sans-serif; }</style>
</head>
<body class="p-6 bg-gray-50 min-h-screen">
  ${previewHtml}
</body>
</html>`

  const preset = VIEWPORTS[viewport]

  return (
    <div className="flex h-[420px] border border-border rounded-[10px] overflow-hidden bg-surface max-[640px]:flex-col max-[640px]:h-[600px]">
      {/* HTML pane */}
      <div className="flex-1 flex flex-col min-w-0">
        <div className="flex items-center gap-1.5 px-3 py-1.5 text-[11px] font-semibold tracking-[0.06em] uppercase text-muted bg-card border-b border-border select-none">
          <span className="w-2 h-2 rounded-full bg-teal flex-shrink-0" />
          HTML
        </div>
        <textarea
          className="flex-1 w-full p-3.5 font-mono text-[13px] leading-[1.6] text-text bg-surface border-none outline-none resize-none disabled:opacity-60 disabled:cursor-not-allowed"
          style={{ tabSize: 2 }}
          value={value}
          onChange={e => onChange(e.target.value)}
          disabled={disabled}
          spellCheck={false}
          autoComplete="off"
          autoCorrect="off"
          autoCapitalize="off"
        />
      </div>

      {/* Preview pane */}
      <div className="flex-1 flex flex-col min-w-0 border-l border-border max-[640px]:border-l-0 max-[640px]:border-t">
        <div className="flex items-center gap-1.5 px-3 py-1.5 text-[11px] font-semibold tracking-[0.06em] uppercase text-muted bg-card border-b border-border select-none">
          <span className="w-2 h-2 rounded-full bg-purple flex-shrink-0" />
          <span className="mr-auto">Preview</span>

          {/* Viewport switcher */}
          <div className="inline-flex gap-0.5 p-0.5 bg-surface border border-border rounded-[7px]" role="tablist">
            {(Object.keys(VIEWPORTS) as Viewport[]).map(key => (
              <button
                key={key}
                type="button"
                role="tab"
                aria-selected={viewport === key}
                className={cn(
                  'inline-flex items-center gap-1 px-2 py-[3px] text-[10px] font-semibold tracking-[0.04em] uppercase rounded-[5px]',
                  'border-none cursor-pointer transition-[background,color] duration-150',
                  viewport === key ? 'bg-purple text-white' : 'bg-transparent text-muted hover:text-text',
                )}
                onClick={() => setViewport(key)}
                title={`${VIEWPORTS[key].label} — ${VIEWPORTS[key].deviceLabel}`}
              >
                <span className="text-[12px] leading-none">{VIEWPORTS[key].icon}</span>
                <span>{VIEWPORTS[key].label}</span>
              </button>
            ))}
          </div>

          <span className="text-[10px] text-muted tabular-nums min-w-[52px] text-right">{preset.deviceLabel}</span>
        </div>

        {/* Preview stage */}
        <div
          className="flex-1 flex justify-center items-start p-3 overflow-auto min-h-0"
          style={{
            background: `linear-gradient(var(--card), var(--card)) padding-box,
              repeating-conic-gradient(var(--surface) 0 25%, transparent 0 50%) 0 0 / 16px 16px`,
          }}
        >
          <div
            className={cn(
              'flex bg-[#f9fafb] rounded-sm overflow-hidden',
              preset.width != null
                ? 'self-stretch max-w-full min-h-full border border-border rounded-[14px] shadow-[0_10px_30px_rgba(0,0,0,0.25)]'
                : 'w-full h-full',
            )}
            style={preset.width != null ? { width: preset.width } : undefined}
          >
            <iframe
              className="flex-1 w-full h-full border-none bg-[#f9fafb]"
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
