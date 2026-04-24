import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { adminChunkApi, adminSubChunkApi, type AdminChunk, type AdminSubChunk } from '../../api/adminServices'

const PRACTICE_TYPES = ['JAVA', 'TAILWIND', 'NONE']

const BLANK: Partial<AdminSubChunk> = {
  title: '', sortOrder: 0, xpReward: 100, practiceType: 'JAVA', filename: '',
  hookHtml: null, explanationHtml: null, storyBeats: null,
  guidedPracticeHtml: null, guidedPracticeStarterCode: null, guidedPracticeTests: null,
  soloPracticeHtml: null, feynmanPrompt: null,
}

function SubChunkRow({
  sc,
  onEdit,
  onDelete,
  onQuestions,
}: {
  sc: AdminSubChunk
  onEdit: () => void
  onDelete: () => void
  onQuestions: () => void
}) {
  return (
    <div style={{
      background: '#16132b',
      border: '1px solid #2e2850',
      borderRadius: 9,
      padding: '14px 18px',
      display: 'flex',
      alignItems: 'center',
      gap: 14,
    }}>
      <span style={{ fontFamily: 'Cinzel, serif', fontSize: 12, color: '#8b7fa0', minWidth: 26 }}>#{sc.sortOrder}</span>
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{ fontWeight: 600, fontSize: 14, color: '#e8e0f0' }}>{sc.title}</div>
        <div style={{ fontSize: 11, color: '#8b7fa0', marginTop: 2, display: 'flex', gap: 12 }}>
          <span>{sc.xpReward} XP</span>
          <span>{sc.practiceType}</span>
          <span>{sc.questionCount} questions</span>
          {sc.soloPracticeHtml ? <span style={{ color: '#4ade80' }}>✓ Solo</span> : <span style={{ color: '#8b7fa0' }}>— Solo</span>}
          {sc.feynmanPrompt ? <span style={{ color: '#4ade80' }}>✓ Feynman</span> : <span style={{ color: '#8b7fa0' }}>— Feynman</span>}
        </div>
      </div>
      <div style={{ display: 'flex', gap: 6 }}>
        <button className="btn btn-ghost" style={{ fontSize: 11, padding: '4px 12px' }} onClick={onQuestions}>
          ❓ Questions
        </button>
        <button className="btn btn-ghost" style={{ fontSize: 11, padding: '4px 10px' }} onClick={onEdit}>
          ✏️ Edit
        </button>
        <button
          className="btn btn-ghost"
          style={{ fontSize: 11, padding: '4px 10px', borderColor: 'rgba(248,113,113,.3)', color: '#f87171' }}
          onClick={onDelete}
        >
          🗑️
        </button>
      </div>
    </div>
  )
}

function InlineForm({
  initial,
  chunkId,
  onSave,
  onCancel,
}: {
  initial: Partial<AdminSubChunk>
  chunkId: string
  onSave: (sc: Partial<AdminSubChunk>) => void
  onCancel: () => void
}) {
  const [form, setForm] = useState<Partial<AdminSubChunk>>({ ...initial, chunkId })
  const set = (k: keyof AdminSubChunk, v: unknown) => setForm(prev => ({ ...prev, [k]: v }))

  return (
    <div style={{ background: '#1e1a35', border: '1px solid #2e2850', borderRadius: 10, padding: 20, marginBottom: 16 }}>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 12, marginBottom: 12 }}>
        <label style={labelStyle}>
          Title
          <input style={inputStyle} value={form.title ?? ''} onChange={e => set('title', e.target.value)} />
        </label>
        <label style={labelStyle}>
          Sort Order
          <input style={inputStyle} type="number" value={form.sortOrder ?? 0} onChange={e => set('sortOrder', parseInt(e.target.value))} />
        </label>
        <label style={labelStyle}>
          XP Reward
          <input style={inputStyle} type="number" value={form.xpReward ?? 100} onChange={e => set('xpReward', parseInt(e.target.value))} />
        </label>
        <label style={labelStyle}>
          Practice Type
          <select style={inputStyle} value={form.practiceType ?? 'JAVA'} onChange={e => set('practiceType', e.target.value)}>
            {PRACTICE_TYPES.map(p => <option key={p} value={p}>{p}</option>)}
          </select>
        </label>
        <label style={labelStyle}>
          Filename
          <input style={inputStyle} value={form.filename ?? ''} onChange={e => set('filename', e.target.value)} />
        </label>
      </div>
      <p style={{ fontSize: 11, color: '#8b7fa0', marginBottom: 8 }}>
        Use the full editor (✏️ Edit) to manage hook, explanation, story beats, and practice content.
      </p>
      <div style={{ display: 'flex', gap: 10 }}>
        <button className="btn btn-primary" style={{ fontSize: 12, padding: '6px 16px' }} onClick={() => onSave(form)}>Save</button>
        <button className="btn btn-ghost" style={{ fontSize: 12, padding: '6px 16px' }} onClick={onCancel}>Cancel</button>
      </div>
    </div>
  )
}

export default function AdminSubChunksPage() {
  const { chunkId } = useParams<{ chunkId: string }>()
  const navigate = useNavigate()
  const [chunk, setChunk] = useState<AdminChunk | null>(null)
  const [subChunks, setSubChunks] = useState<AdminSubChunk[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editSc, setEditSc] = useState<AdminSubChunk | null>(null)
  const [error, setError] = useState<string | null>(null)

  const load = () => {
    if (!chunkId) return
    setLoading(true)
    Promise.all([adminChunkApi.get(chunkId), adminSubChunkApi.list(chunkId)])
      .then(([c, scs]) => { setChunk(c); setSubChunks(scs) })
      .catch(() => setError('Failed to load data'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [chunkId])

  const handleSave = async (form: Partial<AdminSubChunk>) => {
    try {
      if (editSc) {
        const updated = await adminSubChunkApi.update(editSc.id, form)
        setSubChunks(prev => prev.map(s => s.id === updated.id ? updated : s))
      } else {
        const created = await adminSubChunkApi.create({ ...form, chunkId })
        setSubChunks(prev => [...prev, created])
      }
      setShowForm(false)
      setEditSc(null)
    } catch {
      setError('Failed to save sub-chunk')
    }
  }

  const handleDelete = async (id: string) => {
    if (!confirm('Delete this sub-chunk and all its questions?')) return
    try {
      await adminSubChunkApi.delete(id)
      setSubChunks(prev => prev.filter(s => s.id !== id))
    } catch {
      setError('Failed to delete sub-chunk')
    }
  }

  return (
    <div>
      {/* Breadcrumb */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 20, fontSize: 12, color: '#8b7fa0' }}>
        <span style={{ cursor: 'pointer', color: '#8b5cf6' }} onClick={() => navigate('/admin/chunks')}>Content</span>
        <span>›</span>
        <span style={{ color: '#e8e0f0' }}>{chunk?.title ?? 'Loading…'}</span>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#c9a227' }}>
            {chunk ? `${chunk.glyph} ${chunk.title}` : 'Sub-Chunks'}
          </h1>
          <p style={{ color: '#8b7fa0', fontSize: 13, marginTop: 4 }}>Manage learning steps for this chunk</p>
        </div>
        <button className="btn btn-primary" style={{ fontSize: 12 }} onClick={() => { setEditSc(null); setShowForm(true) }}>
          + New Sub-Chunk
        </button>
      </div>

      {error && <div style={{ color: '#f87171', fontSize: 13, marginBottom: 12, padding: '8px 12px', background: 'rgba(248,113,113,.1)', borderRadius: 6 }}>{error}</div>}

      {(showForm || editSc) && (
        <InlineForm
          initial={editSc ?? BLANK}
          chunkId={chunkId!}
          onSave={handleSave}
          onCancel={() => { setShowForm(false); setEditSc(null) }}
        />
      )}

      {loading ? (
        <div style={{ color: '#8b7fa0', fontSize: 14 }}>Loading…</div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {subChunks.length === 0 && <div style={{ color: '#8b7fa0', fontSize: 13 }}>No sub-chunks yet.</div>}
          {subChunks.map(sc => (
            <SubChunkRow
              key={sc.id}
              sc={sc}
              onEdit={() => { setEditSc(sc); setShowForm(false) }}
              onDelete={() => handleDelete(sc.id)}
              onQuestions={() => navigate(`/admin/subchunks/${sc.id}/questions`)}
            />
          ))}
        </div>
      )}
    </div>
  )
}

const labelStyle: React.CSSProperties = {
  display: 'flex', flexDirection: 'column', gap: 5,
  fontSize: 11, fontFamily: 'Cinzel, serif', color: '#8b7fa0',
}

const inputStyle: React.CSSProperties = {
  background: '#16132b', border: '1px solid #2e2850', borderRadius: 6,
  color: '#e8e0f0', fontSize: 13, padding: '7px 10px', outline: 'none', width: '100%',
}
