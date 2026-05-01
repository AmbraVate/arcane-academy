import { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react'

/**
 * SqlEditor — split-pane editor + results-table preview.
 *
 * The iframe loads sql.js (SQLite-WASM) from a CDN, runs the seed SQL once,
 * executes the learner's query, and renders the result rows in a styled table.
 * When runTests() is called, an additional test harness is injected; it runs
 * each spec against a fresh database (per-spec setup, then user query, then
 * comparison) and posts the results back via postMessage.
 *
 * Test spec shapes supported:
 *   { label, setup?, expectedQuery, matchMode: 'rowsExact' | 'rowsAnyOrder' }
 *   { label, setup?, expectedRows: [[...]], expectedColumns?: [...], matchMode: 'rowsExact' | 'rowsAnyOrder' }
 *   { label, setup?, expectedRowCount: N, matchMode: 'rowCount' }
 *   { label, setup?, expectedColumns: [...], matchMode: 'columns' }
 *
 * The first spec's `setup` is also used as the preview-pane seed. If a spec
 * omits `setup`, it inherits the first spec's. Authors usually put the full
 * CREATE+INSERT in the first test and let the rest inherit.
 */

export interface SqlTestSpec {
  label?: string
  setup?: string
  expectedQuery?: string
  expectedRows?: Array<Array<unknown>>
  expectedColumns?: string[]
  expectedRowCount?: number
  matchMode?: 'rowsExact' | 'rowsAnyOrder' | 'rowCount' | 'columns'
  [k: string]: unknown
}

export interface SqlTestResult {
  label: string
  passed: boolean
  actual: string
}

export interface SqlEditorHandle {
  runTests: (specs: SqlTestSpec[]) => Promise<SqlTestResult[]>
}

interface SqlEditorProps {
  value: string
  onChange: (code: string) => void
  setup?: string | null
  disabled?: boolean
}

const SQL_JS_BASE = 'https://cdn.jsdelivr.net/npm/sql.js@1.10.3/dist/'

function buildSrcDoc(userQuery: string, seedSql: string, testHarness: string | null): string {
  // Embed query and seed as JSON so quoting is bulletproof.
  const queryJson = JSON.stringify(userQuery)
  const seedJson = JSON.stringify(seedSql)

  const harnessTag = testHarness
    ? `<script>(function(){ try { ${testHarness} } catch (e) { parent.postMessage({ __sqlTest: true, error: String(e && e.message ? e.message : e) }, '*'); } })();</script>`
    : ''

  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <script src="${SQL_JS_BASE}sql-wasm.js"></script>
  <style>
    html, body { margin: 0; }
    body { font-family: system-ui, -apple-system, sans-serif; padding: 12px; background: #0c1322; color: #e2e8f0; font-size: 13px; }
    table { border-collapse: collapse; width: 100%; max-width: 100%; }
    th, td { border: 1px solid #334155; padding: 5px 9px; text-align: left; vertical-align: top;
             font-family: 'SF Mono', Menlo, Consolas, monospace; font-size: 12px; }
    th { background: #1e293b; color: #94a3b8; font-weight: 600; text-transform: uppercase; letter-spacing: 0.04em; font-size: 10.5px; }
    td { background: #0f172a; }
    .meta { color: #94a3b8; font-size: 11.5px; margin: 4px 0 8px; font-style: italic; }
    .err { font-family: 'SF Mono', Menlo, monospace; font-size: 12px; color: #fda4af; background: #1f0d10; padding: 10px 12px; border: 1px solid #7f1d1d; border-radius: 8px; white-space: pre-wrap; }
    .ok { color: #6ee7b7; }
    .empty { color: #64748b; font-style: italic; padding: 6px 0; }
  </style>
</head>
<body>
  <div id="out"><div class="meta">Loading SQLite engine…</div></div>

  <script>
    (async function() {
      const out = document.getElementById('out');
      const userQuery = ${queryJson};
      const seedSql   = ${seedJson};

      function showError(msg) {
        out.innerHTML = '<div class="err">' + (msg || 'Unknown error') + '</div>';
      }

      function renderResult(columns, values, info) {
        if (!values || values.length === 0) {
          out.innerHTML = '<div class="meta">' + (info || 'Query returned 0 rows.') + '</div>';
          return;
        }
        const head = '<tr>' + columns.map(function(c){ return '<th>' + escapeHtml(c) + '</th>'; }).join('') + '</tr>';
        const body = values.slice(0, 100).map(function(row){
          return '<tr>' + row.map(function(v){ return '<td>' + escapeHtml(v) + '</td>'; }).join('') + '</tr>';
        }).join('');
        const more = values.length > 100 ? '<div class="meta">Showing first 100 of ' + values.length + ' rows.</div>' : '';
        out.innerHTML = '<div class="meta">' + values.length + ' row' + (values.length === 1 ? '' : 's') + (info ? ' — ' + info : '') + '</div>' +
                        '<table>' + head + body + '</table>' + more;
      }

      function escapeHtml(v) {
        if (v === null || v === undefined) return '<span class="empty">NULL</span>';
        return String(v).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
      }

      let SQL;
      try {
        SQL = await initSqlJs({ locateFile: function(f){ return '${SQL_JS_BASE}' + f; } });
      } catch (e) {
        showError('Failed to load SQLite engine: ' + (e && e.message ? e.message : e));
        return;
      }

      // Expose for the test harness
      window.__SQL = SQL;
      window.__seedSql = seedSql;
      window.__userQuery = userQuery;

      // Preview run — fresh DB, run setup, run query, render.
      let db;
      try {
        db = new SQL.Database();
        if (seedSql && seedSql.trim()) db.run(seedSql);
      } catch (e) {
        showError('Setup error: ' + (e && e.message ? e.message : e));
        return;
      }

      if (!userQuery || !userQuery.trim()) {
        out.innerHTML = '<div class="meta">Type a SELECT query to see results here.</div>';
      } else {
        try {
          const result = db.exec(userQuery);
          if (result.length === 0) {
            // Could be a non-SELECT (INSERT/UPDATE/DELETE) — show changes count
            const changes = db.getRowsModified();
            out.innerHTML = '<div class="meta">Statement executed. Rows modified: ' + changes + '.</div>';
          } else {
            // Show the LAST result set (in case multi-statement)
            const last = result[result.length - 1];
            renderResult(last.columns, last.values);
          }
        } catch (e) {
          showError('Query error: ' + (e && e.message ? e.message : e));
        } finally {
          db.close();
        }
      }
    })();
  </script>
  ${harnessTag}
</body>
</html>`
}

/** JS injected into the iframe to run a list of test specs and post results back. */
function buildTestHarness(specs: SqlTestSpec[]): string {
  // Stringify specs into the harness so the iframe sees them as a constant.
  const specsJson = JSON.stringify(specs)
  return `
    const __specs = ${specsJson};
    const __sleep = (ms) => new Promise(r => setTimeout(r, ms));

    function __stringifyRow(row) {
      return JSON.stringify(row.map(function(v){ return v === null || v === undefined ? null : String(v); }));
    }

    function __compareRowsExact(actual, expected) {
      if (actual.length !== expected.length) {
        return { passed: false, actual: 'got ' + actual.length + ' rows, expected ' + expected.length };
      }
      for (let i = 0; i < actual.length; i++) {
        if (__stringifyRow(actual[i]) !== __stringifyRow(expected[i])) {
          return {
            passed: false,
            actual: 'row ' + (i + 1) + ' differs: got ' + __stringifyRow(actual[i]).slice(0, 80) +
                    ', expected ' + __stringifyRow(expected[i]).slice(0, 80)
          };
        }
      }
      return { passed: true, actual: 'all ' + actual.length + ' rows match' };
    }

    function __compareRowsAnyOrder(actual, expected) {
      if (actual.length !== expected.length) {
        return { passed: false, actual: 'got ' + actual.length + ' rows, expected ' + expected.length };
      }
      const a = actual.map(__stringifyRow).slice().sort();
      const b = expected.map(__stringifyRow).slice().sort();
      for (let i = 0; i < a.length; i++) {
        if (a[i] !== b[i]) {
          return { passed: false, actual: 'row sets differ — extra: ' + a[i].slice(0, 80) + '; expected: ' + b[i].slice(0, 80) };
        }
      }
      return { passed: true, actual: 'all ' + actual.length + ' rows match (any order)' };
    }

    function __compareColumns(actual, expected) {
      const a = actual.map(function(c){ return String(c).toLowerCase(); });
      const b = expected.map(function(c){ return String(c).toLowerCase(); });
      if (a.length !== b.length) {
        return { passed: false, actual: 'got ' + a.length + ' columns (' + a.join(',') + '), expected ' + b.length + ' (' + b.join(',') + ')' };
      }
      for (let i = 0; i < a.length; i++) {
        if (a[i] !== b[i]) {
          return { passed: false, actual: 'column ' + (i + 1) + ': got "' + a[i] + '", expected "' + b[i] + '"' };
        }
      }
      return { passed: true, actual: 'columns match: [' + a.join(',') + ']' };
    }

    function __runOne(SQL, sharedSetup, userQuery, spec) {
      const setup = (typeof spec.setup === 'string' && spec.setup.trim()) ? spec.setup : sharedSetup;
      const db = new SQL.Database();
      try {
        if (setup && setup.trim()) db.run(setup);
      } catch (e) {
        return { label: spec.label || 'Test', passed: false, actual: 'Setup error: ' + (e && e.message ? e.message : e) };
      }

      let userResult;
      try {
        const r = db.exec(userQuery);
        userResult = r.length === 0 ? { columns: [], values: [] } : r[r.length - 1];
      } catch (e) {
        db.close();
        return { label: spec.label || 'Test', passed: false, actual: 'Query error: ' + (e && e.message ? e.message : e) };
      }

      const mode = spec.matchMode || (spec.expectedQuery || spec.expectedRows ? 'rowsAnyOrder' : 'rowCount');

      // rowCount mode
      if (mode === 'rowCount') {
        const want = typeof spec.expectedRowCount === 'number' ? spec.expectedRowCount : null;
        if (want === null) {
          db.close();
          return { label: spec.label || 'Test', passed: false, actual: 'rowCount mode requires expectedRowCount' };
        }
        const ok = userResult.values.length === want;
        db.close();
        return {
          label: spec.label || 'Test',
          passed: ok,
          actual: 'got ' + userResult.values.length + ' rows, expected ' + want
        };
      }

      // columns mode — compare column names only
      if (mode === 'columns') {
        if (!spec.expectedColumns) {
          db.close();
          return { label: spec.label || 'Test', passed: false, actual: 'columns mode requires expectedColumns' };
        }
        const c = __compareColumns(userResult.columns, spec.expectedColumns);
        db.close();
        return { label: spec.label || 'Test', passed: c.passed, actual: c.actual };
      }

      // rowsExact / rowsAnyOrder — need an expected row set
      let expected;
      if (spec.expectedQuery) {
        try {
          const r = db.exec(spec.expectedQuery);
          expected = r.length === 0 ? { columns: [], values: [] } : r[r.length - 1];
        } catch (e) {
          db.close();
          return { label: spec.label || 'Test', passed: false, actual: 'Reference query error: ' + (e && e.message ? e.message : e) };
        }
      } else if (spec.expectedRows) {
        expected = { columns: spec.expectedColumns || [], values: spec.expectedRows };
      } else {
        db.close();
        return { label: spec.label || 'Test', passed: false, actual: 'Spec needs expectedQuery or expectedRows' };
      }

      // Optional column-name check first
      if (spec.expectedColumns) {
        const c = __compareColumns(userResult.columns, spec.expectedColumns);
        if (!c.passed) {
          db.close();
          return { label: spec.label || 'Test', passed: false, actual: c.actual };
        }
      }

      const cmp = mode === 'rowsExact'
        ? __compareRowsExact(userResult.values, expected.values)
        : __compareRowsAnyOrder(userResult.values, expected.values);

      db.close();
      return { label: spec.label || 'Test', passed: cmp.passed, actual: cmp.actual };
    }

    (async function() {
      // Wait until preview run finishes loading sql.js
      let waited = 0;
      while (!window.__SQL && waited < 8000) { await __sleep(80); waited += 80; }
      if (!window.__SQL) {
        parent.postMessage({ __sqlTest: true, error: 'SQLite engine did not load in time' }, '*');
        return;
      }
      const SQL = window.__SQL;
      const userQuery = window.__userQuery;
      const sharedSetup = (__specs[0] && typeof __specs[0].setup === 'string') ? __specs[0].setup : window.__seedSql;

      const results = [];
      for (const spec of __specs) {
        try {
          results.push(__runOne(SQL, sharedSetup, userQuery, spec));
        } catch (e) {
          results.push({
            label: spec.label || 'Test',
            passed: false,
            actual: 'Runner error: ' + (e && e.message ? e.message : e)
          });
        }
      }
      parent.postMessage({ __sqlTest: true, results: results }, '*');
    })();
  `
}

const SqlEditor = forwardRef<SqlEditorHandle, SqlEditorProps>(function SqlEditor(
  { value, onChange, setup, disabled = false },
  ref,
) {
  const [previewCode, setPreviewCode] = useState(value)
  const [iframeKey, setIframeKey] = useState(0)
  const [testHarness, setTestHarness] = useState<string | null>(null)
  const debounceRef = useRef<ReturnType<typeof setTimeout>>()
  const pendingResolveRef = useRef<((results: SqlTestResult[]) => void) | null>(null)

  // Debounce code → preview update (heavier than React because sql.js reloads)
  useEffect(() => {
    clearTimeout(debounceRef.current)
    debounceRef.current = setTimeout(() => setPreviewCode(value), 600)
    return () => clearTimeout(debounceRef.current)
  }, [value])

  // postMessage listener: receives test results from the iframe runner
  useEffect(() => {
    function onMessage(e: MessageEvent) {
      const data = e.data
      if (!data || data.__sqlTest !== true) return
      if (pendingResolveRef.current) {
        if (data.error) {
          pendingResolveRef.current([
            { label: 'SQL runner', passed: false, actual: data.error },
          ])
        } else if (Array.isArray(data.results)) {
          pendingResolveRef.current(data.results)
        }
        pendingResolveRef.current = null
        setTestHarness(null) // clear so subsequent edits don't re-run tests
      }
    }
    window.addEventListener('message', onMessage)
    return () => window.removeEventListener('message', onMessage)
  }, [])

  // runTests() — exposed to parent via ref
  useImperativeHandle(ref, () => ({
    async runTests(specs: SqlTestSpec[]): Promise<SqlTestResult[]> {
      return new Promise(resolve => {
        pendingResolveRef.current = resolve
        setTestHarness(buildTestHarness(specs))
        setPreviewCode(value) // ensure latest code is in iframe
        setIframeKey(k => k + 1) // force iframe re-mount with the harness

        // 12-second hard timeout — sql.js takes longer to boot than React+Babel
        setTimeout(() => {
          if (pendingResolveRef.current === resolve) {
            resolve([{
              label: 'SQL runner',
              passed: false,
              actual: 'Timed out — engine may have failed to load. Check the iframe for errors.',
            }])
            pendingResolveRef.current = null
          }
        }, 12000)
      })
    },
  }), [value])

  const seed = setup ?? ''
  const srcdoc = buildSrcDoc(previewCode, seed, testHarness)

  return (
    <div className="flex h-[460px] border border-border rounded-[10px] overflow-hidden bg-surface max-[640px]:flex-col max-[640px]:h-[640px]">
      {/* SQL editor pane */}
      <div className="flex-1 flex flex-col min-w-0">
        <div className="flex items-center gap-1.5 px-3 py-1.5 text-[11px] font-semibold tracking-[0.06em] uppercase text-muted bg-card border-b border-border select-none">
          <span className="w-2 h-2 rounded-full bg-teal flex-shrink-0" />
          SQL
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
          <span className="text-[10px] text-muted tabular-nums">SQLite (sql.js)</span>
        </div>
        <iframe
          key={iframeKey}
          className="flex-1 w-full border-none bg-[#0c1322]"
          srcDoc={srcdoc}
          title="SQL live result"
          sandbox="allow-scripts"
        />
      </div>
    </div>
  )
})

export default SqlEditor
