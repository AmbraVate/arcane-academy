import { useEffect, useState } from 'react'
import { adminCapstoneApi, type AdminCapstone, type PagedResponse } from '@/shared/api/adminServices'
import { ChevronDown, ChevronUp } from 'lucide-react'

function timeAgo(iso: string) {
  const diff = Date.now() - new Date(iso).getTime()
  const m = Math.floor(diff / 60_000)
  if (m < 1) return 'just now'
  if (m < 60) return `${m}m ago`
  const h = Math.floor(m / 60)
  if (h < 24) return `${h}h ago`
  return `${Math.floor(h / 24)}d ago`
}

function CapstoneRow({ capstone, onUpdate }: { capstone: AdminCapstone; onUpdate: (c: AdminCapstone) => void }) {
  const [expanded, setExpanded] = useState(false)
  const [feedback, setFeedback] = useState(capstone.adminFeedback ?? '')
  const [saving, setSaving] = useState(false)

  async function saveFeedback() {
    setSaving(true)
    try {
      const updated = await adminCapstoneApi.addFeedback(capstone.id, feedback)
      onUpdate(updated)
    } finally { setSaving(false) }
  }

  return (
    <div style={{
      background: 'var(--color-card)',
      border: '1px solid var(--color-border)',
      borderRadius: 10,
      marginBottom: 8,
      overflow: 'hidden',
    }}>
      {/* Summary row */}
      <div
        style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '12px 16px', cursor: 'pointer' }}
        onClick={() => setExpanded(v => !v)}
      >
        <div style={{ flex: 1, minWidth: 0 }}>
          <div style={{ fontSize: 13, fontWeight: 600, color: 'var(--color-gold)', marginBottom: 2, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
            {capstone.title}
          </div>
          <div style={{ fontSize: 11, color: 'var(--color-muted)' }}>
            {capstone.moduleId} / User {capstone.userId.slice(0, 8)}… / {timeAgo(capstone.createdAt)}
          </div>
        </div>
        {capstone.reviewedAt
          ? <span style={{ fontSize: 10, padding: '2px 8px', borderRadius: 4, background: 'rgba(74,222,128,.08)', color: '#4ade80', border: '1px solid rgba(74,222,128,.25)', fontFamily: 'Cinzel, serif', fontWeight: 600 }}>Reviewed</span>
          : <span style={{ fontSize: 10, padding: '2px 8px', borderRadius: 4, background: 'rgba(248,113,113,.1)', color: '#f87171', border: '1px solid rgba(248,113,113,.3)', fontFamily: 'Cinzel, serif', fontWeight: 600 }}>Pending</span>
        }
        {expanded ? <ChevronUp size={14} color="var(--color-muted)" /> : <ChevronDown size={14} color="var(--color-muted)" />}
      </div>

      {/* Expanded detail */}
      {expanded && (
        <div style={{ padding: '0 16px 16px', borderTop: '1px solid var(--color-border)' }}>
          {capstone.description && (
            <p style={{ fontSize: 13, color: 'var(--color-text)', margin: '12px 0 8px', lineHeight: 1.6 }}>{capstone.description}</p>
          )}
          {capstone.githubUrl && (
            <a href={capstone.githubUrl} target="_blank" rel="noopener noreferrer" style={{ fontSize: 12, color: 'var(--color-purple-light)', display: 'inline-block', marginBottom: 8 }}>
              GitHub {'->'}
            </a>
          )}
          {capstone.codeContent && (
            <pre style={{ fontSize: 11, background: 'var(--color-surface)', padding: 12, borderRadius: 6, overflow: 'auto', maxHeight: 200, margin: '0 0 12px', color: 'var(--color-text)', border: '1px solid var(--color-border)' }}>
              {capstone.codeContent.slice(0, 2000)}{capstone.codeContent.length > 2000 ? '\n…(truncated)' : ''}
            </pre>
          )}

          {/* Feedback */}
          <div style={{ marginTop: 12 }}>
            <label style={{ fontSize: 11, color: 'var(--color-muted)', fontFamily: 'Cinzel, serif', display: 'block', marginBottom: 6, textTransform: 'uppercase', letterSpacing: '0.06em' }}>
              Instructor Feedback
            </label>
            <textarea
              value={feedback}
              onChange={e => setFeedback(e.target.value)}
              rows={4}
              style={{ width: '100%', background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 6, padding: '8px 12px', fontSize: 13, color: 'var(--color-text)', resize: 'vertical', outline: 'none', boxSizing: 'border-box', fontFamily: 'inherit' }}
              placeholder="Add feedback for the learner…"
            />
            <button
              onClick={saveFeedback}
              disabled={saving}
              style={{ marginTop: 8, padding: '6px 16px', borderRadius: 6, background: 'var(--color-purple)', color: '#fff', border: 'none', fontSize: 12, cursor: saving ? 'default' : 'pointer', opacity: saving ? 0.6 : 1, fontFamily: 'Cinzel, serif' }}
            >
              {saving ? 'Saving…' : 'Save Feedback'}
            </button>
          </div>
        </div>
      )}
    </div>
  )
}

export default function AdminCapstonesPage() {
  const [data, setData] = useState<PagedResponse<AdminCapstone> | null>(null)
  const [page, setPage] = useState(0)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    setLoading(true)
    adminCapstoneApi.list(page, 25)
      .then(setData)
      .finally(() => setLoading(false))
  }, [page])

  function updateCapstone(updated: AdminCapstone) {
    setData(prev => prev ? {
      ...prev,
      content: prev.content.map(c => c.id === updated.id ? updated : c),
    } : prev)
  }

  return (
    <div>
      <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 20, color: 'var(--color-gold)', marginBottom: 20 }}>
        Capstone Projects
      </h2>

      {loading && <p style={{ color: 'var(--color-muted)', fontStyle: 'italic' }}>Loading…</p>}

      {!loading && data && data.totalElements === 0 && (
        <p style={{ color: 'var(--color-muted)', fontStyle: 'italic' }}>No capstone projects submitted yet.</p>
      )}

      {data && data.content.map(c => (
        <CapstoneRow key={c.id} capstone={c} onUpdate={updateCapstone} />
      ))}

      {/* Pagination */}
      {data && data.totalPages > 1 && (
        <div style={{ display: 'flex', gap: 8, marginTop: 16, justifyContent: 'center' }}>
          <button
            onClick={() => setPage(p => Math.max(0, p - 1))}
            disabled={page === 0}
            style={{ padding: '4px 12px', borderRadius: 6, border: '1px solid var(--color-border)', background: 'var(--color-card)', color: 'var(--color-text)', cursor: page === 0 ? 'default' : 'pointer', opacity: page === 0 ? 0.4 : 1, fontSize: 12 }}
          >
            &lt;- Prev
          </button>
          <span style={{ fontSize: 12, color: 'var(--color-muted)', padding: '4px 8px' }}>
            {page + 1} / {data.totalPages}
          </span>
          <button
            onClick={() => setPage(p => p + 1)}
            disabled={page >= data.totalPages - 1}
            style={{ padding: '4px 12px', borderRadius: 6, border: '1px solid var(--color-border)', background: 'var(--color-card)', color: 'var(--color-text)', cursor: page >= data.totalPages - 1 ? 'default' : 'pointer', opacity: page >= data.totalPages - 1 ? 0.4 : 1, fontSize: 12 }}
          >
            Next {'->'}
          </button>
        </div>
      )}
    </div>
  )
}
