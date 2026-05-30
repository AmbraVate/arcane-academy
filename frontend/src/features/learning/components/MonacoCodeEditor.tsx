import { lazy, Suspense } from 'react'
import type { OnMount } from '@monaco-editor/react'

// Monaco is heavy (~2MB); lazy-load so it never blocks initial page render.
const MonacoEditor = lazy(() => import('@monaco-editor/react'))

interface Props {
  value: string
  onChange: (v: string) => void
  language?: string
  readOnly?: boolean
  disabled?: boolean
  /** Height in pixels or CSS string (default: fills flex container via flex-1). */
  height?: string | number
  /** Minimum height as a CSS string (default: '200px'). */
  minHeight?: string
}

const MONACO_OPTIONS = {
  fontSize: 13,
  fontFamily: "'JetBrains Mono', 'Fira Code', 'Cascadia Code', Consolas, monospace",
  lineHeight: 1.65,
  minimap: { enabled: false },
  scrollBeyondLastLine: false,
  wordWrap: 'off' as const,
  tabSize: 4,
  insertSpaces: true,
  renderLineHighlight: 'line' as const,
  overviewRulerLanes: 0,
  hideCursorInOverviewRuler: true,
  scrollbar: {
    verticalScrollbarSize: 6,
    horizontalScrollbarSize: 6,
  },
  padding: { top: 12, bottom: 12 },
} as const

export default function MonacoCodeEditor({
  value, onChange, language = 'java', readOnly = false, disabled = false,
  height, minHeight = '200px',
}: Props) {
  const isReadOnly = readOnly || disabled

  const handleMount: OnMount = (editor, monaco) => {
    // Arcane Academy dark theme — mirrors the existing CodeEditor palette
    monaco.editor.defineTheme('arcane', {
      base: 'vs-dark',
      inherit: true,
      rules: [
        { token: 'keyword',     foreground: '8b5cf6' },   // purple
        { token: 'string',      foreground: '34d399' },   // teal
        { token: 'comment',     foreground: '4b4568', fontStyle: 'italic' },
        { token: 'type',        foreground: 'c9a227' },   // gold
        { token: 'number',      foreground: 'f472b6' },   // pink
        { token: 'delimiter',   foreground: 'a78bfa' },   // light purple
      ],
      colors: {
        'editor.background':           '#0c0a1e',
        'editor.foreground':           '#e2e8f0',
        'editor.lineHighlightBackground': '#130f2a',
        'editorLineNumber.foreground': '#3d3560',
        'editorLineNumber.activeForeground': '#6d5db0',
        'editor.selectionBackground':  '#8b5cf640',
        'editorCursor.foreground':     '#8b5cf6',
        'editorIndentGuide.background1': '#1e1b38',
        'editorIndentGuide.activeBackground1': '#3d3560',
        'scrollbarSlider.background':  '#2e2a4a80',
        'scrollbarSlider.hoverBackground': '#4a458080',
      },
    })
    monaco.editor.setTheme('arcane')
    editor.updateOptions({ readOnly: isReadOnly })
  }

  return (
    <div
      className="flex-1 min-h-0 overflow-hidden"
      style={{ minHeight }}
    >
      <Suspense fallback={
        <div
          className="flex-1 bg-[#0c0a1e] flex items-center justify-center"
          style={{ minHeight, height: height ?? '100%' }}
        >
          <span className="text-[12px] text-muted animate-pulse">Loading editor…</span>
        </div>
      }>
        <MonacoEditor
          height={height ?? '100%'}
          language={language}
          value={value}
          onChange={v => !isReadOnly && onChange(v ?? '')}
          onMount={handleMount}
          options={{
            ...MONACO_OPTIONS,
            readOnly: isReadOnly,
          }}
        />
      </Suspense>
    </div>
  )
}
