import { useEffect, useState } from 'react'
import { adminStuckReportApi, type StuckReport } from '@/shared/api/adminServices'
import { AlertTriangle, CheckCircle2, Clock, ChevronDown, ChevronUp } from 'lucide-react'

type Status = StuckReport['status']

const STATUS_META: Record<Status, { label: string; color: string; bg: string; border: string }> = {
  NEW:      { label: 'New',      color: '#f87171', bg: 'rgba(248,113,113,.1)',  border: 'rgba(248,113,113,.3)'  },
  REVIEWED: { label: 'Reviewed', color: '#c9a227', bg: 'rgba(201,162,39,.1)',  border: 'rgba(201,162,39,.3)'  },
  RESOLVED: { label: 'Resolved', color: '#4ade80', bg: 'rgba(74,222,128,.08)', border: 'rgba(74,222,128,.25)' },
}

function StatusBadge({ status }: { status: Status }) {
  const m = STATUS_META[status]
  return (
    <span style={{
      fontSize: 10, fontFamily: 'Cinzel, serif', fontWeight: 600,
      padding: '2px 8px', borderRadius: 4,
      background: m.bg, color: m.color, border: `1px solid ${m.border}`,
    }}>
      {m.label}
    </span>
  )
}

function timeAgo(iso: string) {
  const diff = Date.now() - new Date(iso).getTime()
  const m = Math.floor(diff / 60_000)
  if (m < 1) return 'just now'
  if (m < 60) return `${m}m ago`
  const h = Math.floor(m / 60)
  if (h < 24) return `${h}h ago`
  return `${Math.floor(h / 24)}d ago`
}

function ReportRow({ report, onUpdate }: { report: StuckReport; onUpdate: (r: StuckReport) => void }) {
  const [expanded, setExpanded] = useState(false)
  const [notes, setNotes] = useState(report.adminNotes ?? '')
  const [saving, setSaving] = useState(false)

  async function setStatus(status: Status) {
    setSaving(true)
    try {
      const updated = await adminStuckReportApi.updateStatus(report.id, status, notes || undefined)
      onUpdate(updated)
    } finally { setSaving(false) }
  }

  async function saveNotes() {
    setSaving(true)
    try {
      const updated = await adminStuckReportApi.updateStatus(report.id, report.status, notes)
      onUpdate(updated)
    } finally { setSaving(false) }
  }

  const m = STATUS_META[report.status]

  return (
    <div style={{
      background: '#16132b', border: `1px solid ${m.border}`,
      borderLeft: `3px solid ${m.color}`,
      borderRadius: 10, overflow: 'hidden', marginBottom: 10,
    }}>
      {/* ── Summary row ── */}
      <div
        style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '12px 16px', cursor: 'pointer' }}
        onClick={() => setExpanded(e => !e)}
      >
        <StatusBadge status={report.status} />

        <div style={{ flex: 1, minWidth: 0 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 2 }}>
            <span style={{ fontSize: 13, fontWeight: 600, color: '#e8e0f0' }}>
              {report.username}
            </span>
            <span style={{ fontSize: 11, color: '#8b7fa0' }}>{report.email}</span>
            {report.topicId && (
              <span style={{
                fontSize: 10, padding: '1px 6px', borderRadius: 4,
                background: 'rgba(139,92,246,.15)', color: '#c4b5fd',
                border: '1px solid rgba(139,92,246,.3)',
              }}>
                {report.topicId}
              </span>
            )}
            {report.currentPhase && (
              <span style={{
                fontSize: 10, padding: '1px 6px', borderRadius: 4,
                background: 'rgba(201,162,39,.1)', color: '#c9a227',
                border: '1px solid rgba(201,162,39,.25)',
              }}>
                {report.currentPhase}
              </span>
            )}
          </div>
          <div style={{ fontSize: 12, color: '#8b7fa0', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', maxWidth: 480 }}>
            {report.userMessage ? `"${report.userMessage}"` : <em>No message provided</em>}
          </div>
        </div>

        <div style={{ fontSize: 11, color: '#8b7fa0', whiteSpace: 'nowrap', flexShrink: 0 }}>
          {timeAgo(report.createdAt)}
        </div>

        <div style={{ color: '#8b7fa0', flexShrink: 0 }}>
          {expanded ? <ChevronUp size={14} /> : <ChevronDown size={14} />}
        </div>
      </div>

      {/* ── Expanded detail ── */}
      {expanded && (
        <div style={{ padding: '0 16px 16px', borderTop: '1px solid #1e1a35' }}>

          {/* URL */}
          {report.currentUrl && (
            <div style={{ marginTop: 12 }}>
              <div style={{ fontSize: 10, color: '#8b7fa0', fontFamily: 'Cinzel, serif', marginBottom: 3 }}>PAGE</div>
              <div style={{ fontSize: 11, color: '#c4b5fd', wordBreak: 'break-all' }}>{report.currentUrl}</div>
            </div>
          )}

          {/* Full message */}
          {report.userMessage && (
            <div style={{ marginTop: 10 }}>
              <div style={{ fontSize: 10, color: '#8b7fa0', fontFamily: 'Cinzel, serif', marginBottom: 3 }}>MESSAGE</div>
              <div style={{
                fontSize: 12, color: '#e8e0f0', lineHeight: 1.55,
                background: 'rgba(255,255,255,.03)', borderRadius: 7,
                padding: '8px 12px', border: '1px solid #1e1a35',
              }}>
                {report.userMessage}
              </div>
            </div>
          )}

          {/* User agent */}
          {report.userAgent && (
            <div style={{ marginTop: 10 }}>
              <div style={{ fontSize: 10, color: '#8b7fa0', fontFamily: 'Cinzel, serif', marginBottom: 3 }}>BROWSER</div>
              <div style={{ fontSize: 10, color: '#8b7fa0', wordBreak: 'break-all' }}>{report.userAgent}</div>
            </div>
          )}

          {/* Sub-chunk */}
          {report.subChunkId && (
            <div style={{ marginTop: 10 }}>
              <div style={{ fontSize: 10, color: '#8b7fa0', fontFamily: 'Cinzel, serif', marginBottom: 3 }}>LESSON ID</div>
              <div style={{ fontSize: 11, color: '#e8e0f0', fontFamily: 'monospace' }}>{report.subChunkId}</div>
            </div>
          )}

          {/* Admin notes */}
          <div style={{ marginTop: 14 }}>
            <div style={{ fontSize: 10, color: '#8b7fa0', fontFamily: 'Cinzel, serif', marginBottom: 4 }}>ADMIN NOTES</div>
            <textarea
              value={notes}
              onChange={e => setNotes(e.target.value)}
              rows={2}
              placeholder="Internal notes (not shown to user)…"
              style={{
                width: '100%', boxSizing: 'border-box', resize: 'vertical',
                background: '#0e0c1e', border: '1px solid #2e2850',
                borderRadius: 7, padding: '8px 10px',
                fontSize: 12, color: '#e8e0f0', fontFamily: 'inherit',
                lineHeight: 1.5,
              }}
            />
          </div>

          {/* Action row */}
          <div style={{ display: 'flex', gap: 8, marginTop: 10, flexWrap: 'wrap' }}>
            {(['NEW', 'REVIEWED', 'RESOLVED'] as Status[]).map(s => (
              <button
                key={s}
                disabled={saving || report.status === s}
                onClick={() => setStatus(s)}
                style={{
                  padding: '5px 12px', borderRadius: 6, fontSize: 11,
                  fontFamily: 'Cinzel, serif', cursor: report.status === s ? 'default' : 'pointer',
                  background: report.status === s ? STATUS_META[s].bg : 'transparent',
                  color: STATUS_META[s].color,
                  border: `1px solid ${STATUS_META[s].border}`,
                  opacity: saving ? 0.6 : 1,
                }}
              >
                {STATUS_META[s].label}
              </button>
            ))}
            <button
              disabled={saving}
              onClick={saveNotes}
              style={{
                marginLeft: 'auto', padding: '5px 14px', borderRadius: 6, fontSize: 11,
                fontFamily: 'Cinzel, serif', cursor: 'pointer',
                background: 'rgba(139,92,246,.12)', color: '#c4b5fd',
                border: '1px solid rgba(139,92,246,.3)',
                opacity: saving ? 0.6 : 1,
              }}
            >
              {saving ? 'Saving…' : 'Save Notes'}
            </button>
          </div>
        </div>
      )}
    </div>
  )
}

export default function AdminStuckReportsPage() {
  const [reports, setReports] = useState<StuckReport[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [filter, setFilter] = useState<Status | 'ALL'>('ALL')

  useEffect(() => {
    adminStuckReportApi.list(0, 100)
      .then(r => setReports(r.content))
      .catch(() => setError('Failed to load stuck reports'))
      .finally(() => setLoading(false))
  }, [])

  function handleUpdate(updated: StuckReport) {
    setReports(prev => prev.map(r => r.id === updated.id ? updated : r))
  }

  const filtered = filter === 'ALL' ? reports : reports.filter(r => r.status === filter)
  const counts = {
    NEW: reports.filter(r => r.status === 'NEW').length,
    REVIEWED: reports.filter(r => r.status === 'REVIEWED').length,
    RESOLVED: reports.filter(r => r.status === 'RESOLVED').length,
  }

  if (loading) return <div style={{ color: '#8b7fa0', fontSize: 14 }}>Loading reports…</div>
  if (error)   return <div style={{ color: '#f87171', fontSize: 14 }}>{error}</div>

  return (
    <div>
      <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#c9a227', marginBottom: 4 }}>
        Stuck Reports
      </h1>
      <p style={{ color: '#8b7fa0', fontSize: 13, marginBottom: 24 }}>
        Learner help requests, captured when they hit a dead end.
      </p>

      {/* Summary cards */}
      <div style={{ display: 'flex', gap: 12, marginBottom: 24, flexWrap: 'wrap' }}>
        {(['NEW', 'REVIEWED', 'RESOLVED'] as Status[]).map(s => {
          const m = STATUS_META[s]
          return (
            <div key={s} style={{
              display: 'flex', alignItems: 'center', gap: 10,
              background: '#16132b', border: `1px solid ${m.border}`,
              borderTop: `2px solid ${m.color}`,
              borderRadius: 10, padding: '12px 18px', minWidth: 120,
            }}>
              {s === 'NEW' && <AlertTriangle size={16} color={m.color} strokeWidth={1.75} />}
              {s === 'REVIEWED' && <Clock size={16} color={m.color} strokeWidth={1.75} />}
              {s === 'RESOLVED' && <CheckCircle2 size={16} color={m.color} strokeWidth={1.75} />}
              <div>
                <div style={{ fontSize: 20, fontWeight: 700, color: m.color, fontFamily: 'Cinzel, serif' }}>
                  {counts[s]}
                </div>
                <div style={{ fontSize: 11, color: '#8b7fa0' }}>{m.label}</div>
              </div>
            </div>
          )
        })}
      </div>

      {/* Filter bar */}
      <div style={{ display: 'flex', gap: 6, marginBottom: 18 }}>
        {(['ALL', 'NEW', 'REVIEWED', 'RESOLVED'] as const).map(f => (
          <button
            key={f}
            onClick={() => setFilter(f)}
            style={{
              padding: '5px 14px', borderRadius: 6, fontSize: 11,
              fontFamily: 'Cinzel, serif', cursor: 'pointer',
              background: filter === f ? 'rgba(139,92,246,.18)' : 'transparent',
              color: filter === f ? '#c4b5fd' : '#8b7fa0',
              border: filter === f ? '1px solid rgba(139,92,246,.45)' : '1px solid #2e2850',
              transition: 'all .15s',
            }}
          >
            {f === 'ALL' ? `All (${reports.length})` : `${STATUS_META[f].label} (${counts[f]})`}
          </button>
        ))}
      </div>

      {/* Report list */}
      {filtered.length === 0 ? (
        <div style={{
          display: 'flex', alignItems: 'center', gap: 8,
          color: '#4ade80', fontSize: 13, padding: '24px 0',
        }}>
          <CheckCircle2 size={16} color="#4ade80" strokeWidth={1.75} />
          {filter === 'ALL' ? 'No stuck reports yet.' : `No ${filter.toLowerCase()} reports.`}
        </div>
      ) : (
        filtered.map(r => (
          <ReportRow key={r.id} report={r} onUpdate={handleUpdate} />
        ))
      )}
    </div>
  )
}
