import { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react'

/**
 * REditor — split-pane editor + R output preview.
 *
 * The iframe loads WebR (R compiled to WebAssembly) from the official CDN
 * (https://webr.r-wasm.org/). It initialises an R session, runs the seed
 * code once (loads datasets, attaches packages), executes the learner's R
 * code, and renders the captured output (text from print/cat, returned
 * values, errors) in a styled panel.
 *
 * When runTests() is called, an additional test harness is injected; it runs
 * each spec against a fresh R session (per-spec setup, then user code, then
 * the spec's `expectedExpression` is evaluated — the test passes if it
 * returns TRUE).
 *
 * Test spec shapes supported:
 *   { label, setup?, expectedExpression }
 *     — runs setup, then user code, then evaluates expectedExpression.
 *       Test passes if expectedExpression evaluates to TRUE.
 *   { label, setup?, expectedOutput, matchMode?: 'exact' | 'contains' }
 *     — runs setup, then user code, then compares captured stdout to expectedOutput.
 *
 * Authors usually put `library(...)` + dataset assignment in the first test's
 * setup; subsequent tests inherit if they omit `setup`.
 *
 * Note: WebR is a ~30 MB initial download. We use trampoline mode so no
 * SharedArrayBuffer / cross-origin-isolation headers are required.
 */

export interface RTestSpec {
  label?: string
  /** R code run BEFORE the user code in a fresh session. Loads datasets, attaches packages. */
  setup?: string
  /** R expression that should evaluate to TRUE after the user's code runs. */
  expectedExpression?: string
  /** Expected captured stdout (compared per matchMode). */
  expectedOutput?: string
  matchMode?: 'exact' | 'contains'
  [k: string]: unknown
}

export interface RTestResult {
  label: string
  passed: boolean
  actual: string
}

export interface REditorHandle {
  runTests: (specs: RTestSpec[]) => Promise<RTestResult[]>
}

interface REditorProps {
  value: string
  onChange: (code: string) => void
  setup?: string | null
  disabled?: boolean
}

const WEBR_MODULE_URL = 'https://webr.r-wasm.org/latest/webr.mjs'

function buildSrcDoc(userCode: string, seedR: string, testHarness: string | null): string {
  // Embed code and seed as JSON so quoting is bulletproof.
  const codeJson = JSON.stringify(userCode)
  const seedJson = JSON.stringify(seedR)

  const harnessTag = testHarness
    ? `<script type="module">(async function(){ try { ${testHarness} } catch (e) { parent.postMessage({ __rTest: true, error: String(e && e.message ? e.message : e) }, '*'); } })();</script>`
    : ''

  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <style>
    html, body { margin: 0; }
    body { font-family: system-ui, -apple-system, sans-serif; padding: 12px; background: #0c1322; color: #e2e8f0; font-size: 13px; }
    .out { font-family: 'SF Mono', Menlo, Consolas, monospace; font-size: 12px; line-height: 1.6; white-space: pre-wrap; }
    .meta { color: #94a3b8; font-size: 11.5px; margin: 4px 0 8px; font-style: italic; }
    .err { font-family: 'SF Mono', Menlo, monospace; font-size: 12px; color: #fda4af; background: #1f0d10; padding: 10px 12px; border: 1px solid #7f1d1d; border-radius: 8px; white-space: pre-wrap; }
    .ok { color: #6ee7b7; }
    .ret { color: #a78bfa; }
    .label { color: #94a3b8; font-size: 10.5px; text-transform: uppercase; letter-spacing: 0.04em; margin: 8px 0 4px; }
  </style>
</head>
<body>
  <div id="out"><div class="meta">Loading R engine (WebR)... first load is ~30 MB, please wait.</div></div>

  <script type="module">
    import { WebR } from '${WEBR_MODULE_URL}';

    const out = document.getElementById('out');
    const userCode = ${codeJson};
    const seedR   = ${seedJson};

    function showError(msg) {
      out.innerHTML = '<div class="err">' + (msg || 'Unknown error') + '</div>';
    }

    function escapeHtml(v) {
      if (v === null || v === undefined) return '';
      return String(v).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    }

    let webR;
    try {
      webR = new WebR();
      await webR.init();
    } catch (e) {
      showError('Failed to load R engine: ' + (e && e.message ? e.message : e));
      return;
    }

    // Expose for the test harness
    window.__WEBR = webR;
    window.__seedR = seedR;
    window.__userCode = userCode;

    // Preview run — fresh session, run setup, run user code, capture output.
    try {
      if (seedR && seedR.trim()) {
        await webR.evalR(seedR);
      }
    } catch (e) {
      showError('Setup error: ' + (e && e.message ? e.message : e));
      return;
    }

    if (!userCode || !userCode.trim()) {
      out.innerHTML = '<div class="meta">Type R code on the left to see results here.</div>';
    } else {
      try {
        // captureR returns { result, output } where output is a list of
        // { type: 'stdout'|'stderr', data: string } objects.
        const shelter = await new webR.Shelter();
        try {
          const capture = await shelter.captureR(userCode, { withAutoprint: true });
          const stdoutLines = (capture.output || [])
            .filter(o => o.type === 'stdout' || o.type === 'message')
            .map(o => o.data)
            .join('\\n');
          const stderrLines = (capture.output || [])
            .filter(o => o.type === 'stderr')
            .map(o => o.data)
            .join('\\n');

          let html = '';
          if (stdoutLines.trim()) {
            html += '<div class="label">Output</div><div class="out">' + escapeHtml(stdoutLines) + '</div>';
          }
          if (stderrLines.trim()) {
            html += '<div class="label">Warnings / messages</div><div class="out" style="color:#fcd34d">' + escapeHtml(stderrLines) + '</div>';
          }
          if (!html) {
            html = '<div class="meta">R evaluated successfully (no printed output).</div>';
          }
          out.innerHTML = html;
        } finally {
          await shelter.purge();
        }
      } catch (e) {
        showError('R error: ' + (e && e.message ? e.message : e));
      }
    }
  </script>
  ${harnessTag}
</body>
</html>`
}

/** JS injected into the iframe to run a list of test specs and post results back. */
function buildTestHarness(specs: RTestSpec[]): string {
  const specsJson = JSON.stringify(specs)
  return `
    const __specs = ${specsJson};
    const __sleep = (ms) => new Promise(r => setTimeout(r, ms));

    async function __runOne(webR, sharedSetup, userCode, spec) {
      const setup = (typeof spec.setup === 'string' && spec.setup.trim()) ? spec.setup : sharedSetup;
      const shelter = await new webR.Shelter();
      try {
        // Run setup
        try {
          if (setup && setup.trim()) {
            await shelter.captureR(setup);
          }
        } catch (e) {
          return { label: spec.label || 'Test', passed: false, actual: 'Setup error: ' + (e && e.message ? e.message : e) };
        }

        // Run user code, capturing output
        let userStdout = '';
        try {
          const cap = await shelter.captureR(userCode, { withAutoprint: true });
          userStdout = (cap.output || [])
            .filter(function(o) { return o.type === 'stdout' || o.type === 'message'; })
            .map(function(o) { return o.data; })
            .join('\\n');
        } catch (e) {
          return { label: spec.label || 'Test', passed: false, actual: 'R error: ' + (e && e.message ? e.message : e) };
        }

        // Evaluate the expectation
        if (typeof spec.expectedExpression === 'string' && spec.expectedExpression.trim()) {
          try {
            const r = await shelter.evalR(spec.expectedExpression);
            const js = await r.toJs();
            // toJs() returns { type: 'logical', names: null, values: [true] } for logical vectors
            const v = js && js.values && js.values.length > 0 ? js.values[0] : false;
            return {
              label: spec.label || 'Test',
              passed: v === true,
              actual: v === true ? 'expectation evaluated TRUE' : 'expectation evaluated ' + JSON.stringify(v)
            };
          } catch (e) {
            return { label: spec.label || 'Test', passed: false, actual: 'Expectation eval error: ' + (e && e.message ? e.message : e) };
          }
        }

        if (typeof spec.expectedOutput === 'string') {
          const mode = spec.matchMode || 'contains';
          if (mode === 'exact') {
            const ok = userStdout.trim() === spec.expectedOutput.trim();
            return {
              label: spec.label || 'Test',
              passed: ok,
              actual: ok ? 'output matches exactly' : 'got: ' + userStdout.slice(0, 200)
            };
          } else {
            const ok = userStdout.indexOf(spec.expectedOutput) >= 0;
            return {
              label: spec.label || 'Test',
              passed: ok,
              actual: ok ? 'output contains expected substring' : 'expected substring not found in: ' + userStdout.slice(0, 200)
            };
          }
        }

        return { label: spec.label || 'Test', passed: false, actual: 'Spec needs expectedExpression or expectedOutput' };
      } finally {
        await shelter.purge();
      }
    }

    (async function() {
      // Wait until preview run finishes loading WebR
      let waited = 0;
      while (!window.__WEBR && waited < 60000) { await __sleep(200); waited += 200; }
      if (!window.__WEBR) {
        parent.postMessage({ __rTest: true, error: 'R engine did not load in time (60s)' }, '*');
        return;
      }
      const webR = window.__WEBR;
      const userCode = window.__userCode;
      const sharedSetup = (__specs[0] && typeof __specs[0].setup === 'string') ? __specs[0].setup : window.__seedR;

      const results = [];
      for (const spec of __specs) {
        try {
          results.push(await __runOne(webR, sharedSetup, userCode, spec));
        } catch (e) {
          results.push({
            label: spec.label || 'Test',
            passed: false,
            actual: 'Runner error: ' + (e && e.message ? e.message : e)
          });
        }
      }
      parent.postMessage({ __rTest: true, results: results }, '*');
    })();
  `
}

const REditor = forwardRef<REditorHandle, REditorProps>(function REditor(
  { value, onChange, setup, disabled = false },
  ref,
) {
  const [previewCode, setPreviewCode] = useState(value)
  const [iframeKey, setIframeKey] = useState(0)
  const [testHarness, setTestHarness] = useState<string | null>(null)
  const debounceRef = useRef<ReturnType<typeof setTimeout>>()
  const pendingResolveRef = useRef<((results: RTestResult[]) => void) | null>(null)

  // Debounce code → preview update (R is heavy — longer debounce than SQL)
  useEffect(() => {
    clearTimeout(debounceRef.current)
    debounceRef.current = setTimeout(() => setPreviewCode(value), 900)
    return () => clearTimeout(debounceRef.current)
  }, [value])

  // postMessage listener: receives test results from the iframe runner
  useEffect(() => {
    function onMessage(e: MessageEvent) {
      const data = e.data
      if (!data || data.__rTest !== true) return
      if (pendingResolveRef.current) {
        if (data.error) {
          pendingResolveRef.current([
            { label: 'R runner', passed: false, actual: data.error },
          ])
        } else if (Array.isArray(data.results)) {
          pendingResolveRef.current(data.results)
        }
        pendingResolveRef.current = null
        setTestHarness(null)
      }
    }
    window.addEventListener('message', onMessage)
    return () => window.removeEventListener('message', onMessage)
  }, [])

  useImperativeHandle(ref, () => ({
    async runTests(specs: RTestSpec[]): Promise<RTestResult[]> {
      return new Promise(resolve => {
        pendingResolveRef.current = resolve
        setTestHarness(buildTestHarness(specs))
        setPreviewCode(value)
        setIframeKey(k => k + 1)

        // 90-second hard timeout — WebR is heavy (initial download + R init)
        setTimeout(() => {
          if (pendingResolveRef.current === resolve) {
            resolve([{
              label: 'R runner',
              passed: false,
              actual: 'Timed out — WebR may have failed to load. Check the iframe for errors.',
            }])
            pendingResolveRef.current = null
          }
        }, 90000)
      })
    },
  }), [value])

  const seed = setup ?? ''
  const srcdoc = buildSrcDoc(previewCode, seed, testHarness)

  return (
    <div className="flex h-[460px] border border-border rounded-[10px] overflow-hidden bg-surface max-[640px]:flex-col max-[640px]:h-[640px]">
      {/* R editor pane */}
      <div className="flex-1 flex flex-col min-w-0">
        <div className="flex items-center gap-1.5 px-3 py-1.5 text-[11px] font-semibold tracking-[0.06em] uppercase text-muted bg-card border-b border-border select-none">
          <span className="w-2 h-2 rounded-full bg-teal flex-shrink-0" />
          R
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

      {/* Result pane */}
      <div className="flex-1 flex flex-col min-w-0 border-l border-border max-[640px]:border-l-0 max-[640px]:border-t">
        <div className="flex items-center gap-1.5 px-3 py-1.5 text-[11px] font-semibold tracking-[0.06em] uppercase text-muted bg-card border-b border-border select-none">
          <span className="w-2 h-2 rounded-full bg-purple flex-shrink-0" />
          <span className="mr-auto">Result</span>
          <span className="text-[10px] text-muted tabular-nums">R (WebR)</span>
        </div>
        <iframe
          key={iframeKey}
          className="flex-1 w-full border-none bg-[#0c1322]"
          srcDoc={srcdoc}
          title="R live result"
          sandbox="allow-scripts"
        />
      </div>
    </div>
  )
})

export default REditor
