import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { adminChunkApi, adminTopicApi, type AdminChunk, type AdminTopic } from '@/shared/api/adminServices'

const TIERS = ['FOUNDATION', 'PRACTITIONER', 'EXPERT']

const BLANK_CHUNK: Partial<AdminChunk> = {
  title: '', glyph: '📦', sortOrder: 0, tier: 'FOUNDATION', topicId: '', prerequisiteIds: [],
}

function ChunkForm({
  initial, topics, onSave, onCancel,
}: {
  initial: Partial<AdminChunk>
  topics: AdminTopic[]
  onSave: (c: Partial<AdminChunk>) => void
  onCancel: () => void
}) {
  const [form, setForm] = useState<Partial<AdminChunk>>(initial)
  const set = (k: keyof AdminChunk, v: unknown) => setForm(prev => ({ ...prev, [k]: v }))

  return (
    <div style={{ background: '#1e1a35', border: '1px solid #2e2850', borderRadius: 10, padding: 20, marginBottom: 20 }}>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
        <label style={labelStyle}>
          Title
          <input style={inputStyle} value={form.title ?? ''} onChange={e => set('title', e.target.value)} />
        </label>
        <label style={labelStyle}>
          Glyph
          <input style={inputStyle} value={form.glyph ?? ''} onChange={e => set('glyph', e.target.value)} />
        </label>
        <label style={labelStyle}>
          Topic
          <select style={inputStyle} value={form.topicId ?? ''} onChange={e => set('topicId', e.target.value)}>
            <option value="">Select topic…</option>
            {topics.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
          </select>
        </label>
        <label style={labelStyle}>
          Tier
          <select style={inputStyle} value={form.tier ?? 'FOUNDATION'} onChange={e => set('tier', e.target.value)}>
            {TIERS.map(t => <option key={t} value={t}>{t}</option>)}
          </select>
        </label>
        <label style={labelStyle}>
          Sort Order
          <input style={inputStyle} type="number" value={form.sortOrder ?? 0} onChange={e => set('sortOrder', parseInt(e.target.value))} />
        </label>
      </div>
      <div style={{ display: 'flex', gap: 10, marginTop: 16 }}>
        <button className="btn btn-primary" style={{ fontSize: 12, padding: '6px 16px' }} onClick={() => onSave(form)}>
          Save
        </button>
        <button className="btn btn-ghost" style={{ fontSize: 12, padding: '6px 16px' }} onClick={onCancel}>
          Cancel
        </button>
      </div>
    </div>
  )
}

export default function AdminChunksPage() {
  const navigate = useNavigate()
  const [topics, setTopics] = useState<AdminTopic[]>([])
  const [chunks, setChunks] = useState<AdminChunk[]>([])
  const [topicFilter, setTopicFilter] = useState<string>('')
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editChunk, setEditChunk] = useState<AdminChunk | null>(null)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const load = (tid?: string) => {
    setLoading(true)
    Promise.all([
      adminTopicApi.list(),
      adminChunkApi.list(tid || undefined),
    ]).then(([ts, cs]) => {
      setTopics(ts)
      setChunks(cs)
    }).catch(() => setError('Failed to load data'))
      .finally(() => setLoading(false))
  }

  useEffect(() => { load(topicFilter) }, [topicFilter])

  const handleSave = async (form: Partial<AdminChunk>) => {
    setSaving(true)
    try {
      if (editChunk) {
        const updated = await adminChunkApi.update(editChunk.id, form)
        setChunks(prev => prev.map(c => c.id === updated.id ? updated : c))
      } else {
        const created = await adminChunkApi.create(form)
        setChunks(prev => [...prev, created])
      }
      setShowForm(false)
      setEditChunk(null)
    } catch {
      setError('Failed to save chunk')
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async (id: string) => {
    if (!confirm('Delete this chunk and all its sub-chunks?')) return
    try {
      await adminChunkApi.delete(id)
      setChunks(prev => prev.filter(c => c.id !== id))
    } catch {
      setError('Failed to delete chunk')
    }
  }

  const tierColor: Record<string, string> = {
    FOUNDATION: '#4ade80', PRACTITIONER: '#c9a227', EXPERT: '#8b5cf6',
  }
  const topicName = (id: string) => topics.find(t => t.id === id)?.name ?? id

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#c9a227' }}>Content</h1>
          <p style={{ color: '#8b7fa0', fontSize: 13, marginTop: 4 }}>Manage chunks and sub-chunks</p>
        </div>
        <button className="btn btn-primary" style={{ fontSize: 12 }} onClick={() => { setEditChunk(null); setShowForm(true) }}>
          + New Chunk
        </button>
      </div>

      {error && <div style={{ color: '#f87171', fontSize: 13, marginBottom: 12, padding: '8px 12px', background: 'rgba(248,113,113,.1)', borderRadius: 6 }}>{error}</div>}

      {/* Topic filter */}
      <div style={{ display: 'flex', gap: 8, marginBottom: 20 }}>
        <button
          onClick={() => setTopicFilter('')}
          style={{ ...filterBtn, background: topicFilter === '' ? 'rgba(139,92,246,.2)' : 'transparent', color: topicFilter === '' ? '#c4b5fd' : '#8b7fa0', borderColor: topicFilter === '' ? '#8b5cf6' : '#2e2850' }}
        >
          All Topics
        </button>
        {topics.map(t => (
          <button
            key={t.id}
            onClick={() => setTopicFilter(t.id)}
            style={{ ...filterBtn, background: topicFilter === t.id ? 'rgba(139,92,246,.2)' : 'transparent', color: topicFilter === t.id ? '#c4b5fd' : '#8b7fa0', borderColor: topicFilter === t.id ? '#8b5cf6' : '#2e2850' }}
          >
            {t.glyph} {t.name}
          </button>
        ))}
      </div>

      {/* New / edit form */}
      {(showForm || editChunk) && (
        <ChunkForm
          initial={editChunk ?? { ...BLANK_CHUNK, topicId: topicFilter || (topics[0]?.id ?? '') }}
          topics={topics}
          onSave={handleSave}
          onCancel={() => { setShowForm(false); setEditChunk(null) }}
        />
      )}

      {loading ? (
        <div style={{ color: '#8b7fa0', fontSize: 14 }}>Loading…</div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {chunks.length === 0 && <div style={{ color: '#8b7fa0', fontSize: 13 }}>No chunks found.</div>}
          {chunks.map(chunk => (
            <div key={chunk.id} style={{
              background: '#16132b',
              border: '1px solid #2e2850',
              borderRadius: 9,
              padding: '14px 18px',
              display: 'flex',
              alignItems: 'center',
              gap: 14,
            }}>
              <span style={{ fontSize: 22, minWidth: 28 }}>{chunk.glyph}</span>
              <div style={{ flex: 1, minWidth: 0 }}>
                <div style={{ fontWeight: 600, fontSize: 14, color: '#e8e0f0' }}>{chunk.title}</div>
                <div style={{ fontSize: 11, color: '#8b7fa0', marginTop: 2 }}>
                  {topicName(chunk.topicId)} · {chunk.subChunkCount} sub-chunks · order {chunk.sortOrder}
                </div>
              </div>
              <span style={{ fontSize: 10, fontFamily: 'Cinzel, serif', padding: '2px 8px', borderRadius: 4, border: `1px solid ${tierColor[chunk.tier] ?? '#2e2850'}`, color: tierColor[chunk.tier] ?? '#8b7fa0' }}>
                {chunk.tier}
              </span>
              <div style={{ display: 'flex', gap: 6 }}>
                <button
                  className="btn btn-ghost"
                  style={{ fontSize: 11, padding: '4px 12px' }}
                  onClick={() => navigate(`/admin/chunks/${chunk.id}/subchunks`)}
                >
                  Sub-chunks
                </button>
                <button
                  className="btn btn-ghost"
                  style={{ fontSize: 11, padding: '4px 10px' }}
                  onClick={() => { setEditChunk(chunk); setShowForm(false) }}
                >
                  ✏️
                </button>
                <button
                  className="btn btn-ghost"
                  style={{ fontSize: 11, padding: '4px 10px', borderColor: 'rgba(248,113,113,.3)', color: '#f87171' }}
                  onClick={() => handleDelete(chunk.id)}
                >
                  🗑️
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

const filterBtn: React.CSSProperties = {
  fontFamily: 'Cinzel, serif',
  fontSize: 11,
  padding: '5px 12px',
  borderRadius: 6,
  border: '1px solid',
  cursor: 'pointer',
  transition: 'all .15s ease',
}

const labelStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 5,
  fontSize: 11,
  fontFamily: 'Cinzel, serif',
  color: '#8b7fa0',
}

const inputStyle: React.CSSProperties = {
  background: '#16132b',
  border: '1px solid #2e2850',
  borderRadius: 6,
  color: '#e8e0f0',
  fontSize: 13,
  padding: '7px 10px',
  outline: 'none',
  width: '100%',
}
