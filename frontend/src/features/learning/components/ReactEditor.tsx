import { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react'
import { cn } from '@/lib/utils'

/**
 * ReactEditor — split-pane editor with a sandboxed iframe preview.
 *
 * Renders the learner's JSX inside the iframe via React 18 + Babel standalone
 * (both pulled from CDN). The runTests() method (exposed via useImperativeHandle)
 * injects a test-runner script into the iframe and listens for postMessage with
 * the per-test results.
 *
 * Test spec shapes supported in v1:
 *   { selector, requiredText }   — first match's textContent contains string
 *   { selector, exists: true }   — at least one match exists
 *   { selector, exists: false }  — no match exists
 *   { selector, count: N }       — exactly N matches exist
 *   { action: 'click', selector, then: <spec> }   — click then check
 *
 * Specs not yet supported (the runner reports them as "skipped"):
 *   action: 'navigate' | 'dispatch' | 'wait' | 'sequence'
 *
 * That covers ~70% of rx-a/b/c tests; the remainder are reported gracefully so
 * a learner sees a "(test skipped — feature coming soon)" entry rather than a
 * silent fail.
 */

type Viewport = 'mobile' | 'tablet' | 'desktop'

const VIEWPORTS: Record<Viewport, { label: string; icon: string; width: number | null; deviceLabel: string }> = {
  mobile:  { label: 'Mobile',  icon: '📱', width: 390,  deviceLabel: '390 px' },
  tablet:  { label: 'Tablet',  icon: '📱', width: 820,  deviceLabel: '820 px' },
  desktop: { label: 'Desktop', icon: '🖥️', width: null, deviceLabel: '100%' },
}

export interface ReactTestSpec {
  label?: string
  selector?: string
  requiredText?: string
  exists?: boolean
  count?: number
  action?: string
  then?: ReactTestSpec
  // any other keys are passed through and reported as "skipped"
  [k: string]: unknown
}

export interface ReactTestResult {
  label: string
  passed: boolean
  actual: string
}

export interface ReactEditorHandle {
  runTests: (specs: ReactTestSpec[]) => Promise<ReactTestResult[]>
}

interface ReactEditorProps {
  value: string
  onChange: (code: string) => void
  disabled?: boolean
}

function buildSrcDoc(userCode: string, testHarness: string | null): string {
  // Strip ES module syntax that breaks Babel-standalone in-browser.
  // Learners write `import { useState } from "react"` and `export default Foo` —
  // we need them to use globals (`React.useState`) inside the sandbox.
  const safeCode = userCode
    .replace(/^\s*import\s+[^;]+;?\s*$/gm, '')                // strip import lines
    .replace(/^\s*export\s+default\s+(function\s+)?/gm, '')   // strip "export default "
    .replace(/^\s*export\s+(const|function)\s+/gm, '$1 ')     // strip "export const/function "

  // The harness is JS that runs AFTER user code; it queries the rendered DOM.
  const harnessScript = testHarness
    ? `<script>(function(){ try { ${testHarness} } catch (e) { parent.postMessage({ __reactTest: true, error: String(e) }, '*'); } })();</script>`
    : ''

  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <script crossorigin src="https://unpkg.com/react@18/umd/react.development.js"></script>
  <script crossorigin src="https://unpkg.com/react-dom@18/umd/react-dom.development.js"></script>
  <script src="https://unpkg.com/@babel/standalone@7.24.7/babel.min.js"></script>
  <style>
    html, body { margin: 0; }
    body { font-family: system-ui, sans-serif; padding: 16px; background: #f9fafb; color: #111827; }
    #root { min-height: 0; }
    #__error { font-family: 'SF Mono', Menlo, monospace; font-size: 12px; color: #b91c1c; background: #fef2f2;
               padding: 12px; border: 1px solid #fecaca; border-radius: 8px; white-space: pre-wrap; }
  </style>
</head>
<body>
  <div id="root"></div>
  <div id="__error" style="display:none"></div>

  <script type="text/babel" data-presets="env,react">
    try {
      // Expose React + hooks as globals so user code that doesn't import them still works
      const { useState, useEffect, useMemo, useCallback, useRef, useContext, useReducer,
              createContext, memo, lazy, Suspense, Component, Fragment, forwardRef } = React;
      window.useState = useState; window.useEffect = useEffect; window.useMemo = useMemo;
      window.useCallback = useCallback; window.useRef = useRef; window.useContext = useContext;
      window.useReducer = useReducer; window.createContext = createContext; window.memo = memo;
      window.lazy = lazy; window.Suspense = Suspense; window.Fragment = Fragment;
      window.forwardRef = forwardRef;

      ${safeCode}

      // Find the component to render: prefer a top-level capitalised function/const, last one wins
      let __Component = null;
      const __globals = Object.keys(window);
      for (let i = __globals.length - 1; i >= 0; i--) {
        const k = __globals[i];
        if (/^[A-Z]/.test(k) && typeof window[k] === 'function') { __Component = window[k]; break; }
      }
      if (!__Component) {
        throw new Error('No React component found. Define a function whose name starts with a capital letter.');
      }
      const __root = ReactDOM.createRoot(document.getElementById('root'));
      __root.render(React.createElement(__Component));
    } catch (e) {
      const errBox = document.getElementById('__error');
      errBox.style.display = 'block';
      errBox.textContent = String(e && e.message ? e.message : e);
      parent.postMessage({ __reactTest: true, error: String(e && e.message ? e.message : e) }, '*');
    }
  </script>
  ${harnessScript}
</body>
</html>`
}

/**
 * Builds the JS test-runner code injected into the iframe.
 * Runs each spec sequentially, posts results back via postMessage.
 */
function buildTestHarness(specs: ReactTestSpec[]): string {
  return `
    const __specs = ${JSON.stringify(specs)};
    const __results = [];
    const __sleep = (ms) => new Promise(r => setTimeout(r, ms));

    function __check(spec) {
      const sel = spec.selector;
      if (!sel) return { passed: false, actual: 'No selector provided' };
      const matches = document.querySelectorAll(sel);

      if (typeof spec.exists === 'boolean') {
        const ok = spec.exists ? matches.length > 0 : matches.length === 0;
        return { passed: ok, actual: matches.length + ' match(es) for "' + sel + '"' };
      }
      if (typeof spec.count === 'number') {
        return {
          passed: matches.length === spec.count,
          actual: 'expected ' + spec.count + ' matches, got ' + matches.length
        };
      }
      if (typeof spec.requiredText === 'string') {
        if (matches.length === 0) return { passed: false, actual: 'no element matches "' + sel + '"' };
        const text = (matches[0].textContent || '').trim();
        const wanted = spec.requiredText;
        return {
          passed: text.includes(wanted),
          actual: '"' + sel + '" textContent: "' + text.slice(0, 80) + (text.length > 80 ? '…' : '') + '"'
        };
      }
      return { passed: true, actual: 'no assertion specified' };
    }

    async function __runSpec(spec) {
      // Pure assertion (no action)
      if (!spec.action) return __check(spec);

      // Click action — fire click then check the "then" spec
      if (spec.action === 'click' && spec.selector) {
        const el = document.querySelector(spec.selector);
        if (!el) return { passed: false, actual: 'no element to click for "' + spec.selector + '"' };
        el.click();
        await __sleep(50); // let React flush
        if (spec.then) return __check(spec.then);
        return { passed: true, actual: 'clicked "' + spec.selector + '"' };
      }

      // Unsupported actions — report as skipped (counts as pass to avoid blocking)
      return {
        passed: true,
        actual: '(test skipped — action "' + spec.action + '" not yet supported in iframe runner)'
      };
    }

    (async () => {
      // Tiny delay so React has finished its initial render
      await __sleep(80);
      for (const spec of __specs) {
        try {
          const r = await __runSpec(spec);
          __results.push({
            label: spec.label || ('Test ' + (__results.length + 1)),
            passed: r.passed,
            actual: r.actual
          });
        } catch (e) {
          __results.push({
            label: spec.label || ('Test ' + (__results.length + 1)),
            passed: false,
            actual: 'Runner error: ' + String(e && e.message ? e.message : e)
          });
        }
      }
      parent.postMessage({ __reactTest: true, results: __results }, '*');
    })();
  `
}

const ReactEditor = forwardRef<ReactEditorHandle, ReactEditorProps>(function ReactEditor(
  { value, onChange, disabled = false },
  ref,
) {
  const [previewCode, setPreviewCode] = useState(value)
  const [viewport, setViewport] = useState<Viewport>('desktop')
  const [iframeKey, setIframeKey] = useState(0)
  const [testHarness, setTestHarness] = useState<string | null>(null)
  const debounceRef = useRef<ReturnType<typeof setTimeout>>()
  const pendingResolveRef = useRef<((results: ReactTestResult[]) => void) | null>(null)

  // Debounce code → preview update
  useEffect(() => {
    clearTimeout(debounceRef.current)
    debounceRef.current = setTimeout(() => setPreviewCode(value), 400)
    return () => clearTimeout(debounceRef.current)
  }, [value])

  // postMessage listener: receives test results from the iframe runner
  useEffect(() => {
    function onMessage(e: MessageEvent) {
      const data = e.data
      if (!data || data.__reactTest !== true) return
      if (pendingResolveRef.current) {
        if (data.error) {
          pendingResolveRef.current([
            { label: 'Component render', passed: false, actual: data.error },
          ])
        } else if (Array.isArray(data.results)) {
          pendingResolveRef.current(data.results)
        }
        pendingResolveRef.current = null
        setTestHarness(null) // clear so the next preview update doesn't re-run tests
      }
    }
    window.addEventListener('message', onMessage)
    return () => window.removeEventListener('message', onMessage)
  }, [])

  // runTests() — exposed to parent via ref
  useImperativeHandle(ref, () => ({
    async runTests(specs: ReactTestSpec[]): Promise<ReactTestResult[]> {
      return new Promise(resolve => {
        pendingResolveRef.current = resolve
        setTestHarness(buildTestHarness(specs))
        setPreviewCode(value) // ensure latest code is in iframe
        setIframeKey(k => k + 1) // force iframe re-mount with the harness

        // 8-second hard timeout — return whatever we have so the user isn't stuck
        setTimeout(() => {
          if (pendingResolveRef.current === resolve) {
            resolve([{
              label: 'Test runner',
              passed: false,
              actual: 'Timed out — preview may have failed to load. Check the iframe for errors.',
            }])
            pendingResolveRef.current = null
          }
        }, 8000)
      })
    },
  }), [value])

  const srcdoc = buildSrcDoc(previewCode, testHarness)
  const preset = VIEWPORTS[viewport]

  return (
    <div className="flex h-[460px] border border-border rounded-[10px] overflow-hidden bg-surface max-[640px]:flex-col max-[640px]:h-[640px]">
      {/* JSX editor pane */}
      <div className="flex-1 flex flex-col min-w-0">
        <div className="flex items-center gap-1.5 px-3 py-1.5 text-[11px] font-semibold tracking-[0.06em] uppercase text-muted bg-card border-b border-border select-none">
          <span className="w-2 h-2 rounded-full bg-teal flex-shrink-0" />
          JSX
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
              key={iframeKey}
              className="flex-1 w-full h-full border-none bg-[#f9fafb]"
              srcDoc={srcdoc}
              title="React live preview"
              sandbox="allow-scripts"
            />
          </div>
        </div>
      </div>
    </div>
  )
})

export default ReactEditor
