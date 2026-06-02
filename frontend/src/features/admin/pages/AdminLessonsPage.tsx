import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { adminChunkApi, adminLessonApi, type AdminChunk, type AdminLesson } from '@/shared/api/adminServices'

const PRACTICE_TYPES = ['JAVA', 'TAILWIND', 'NONE']

const BLANK: Partial<AdminLesson> = {
  title: '', sortOrder: 0, xpReward: 100, practiceType: 'JAVA', filename: '',
  hookHtml: null, explanationHtml: null, storyBeats: null,
  guidedPracticeHtml: null, guidedPracticeStarterCode: null, guidedPracticeTests: null,
  soloPracticeHtml: null, feynmanPrompt: null,
}

function LessonRow({
  sc,
  onEdit,
  onDelete,
  onQuestions,
}: {
  sc: AdminLesson
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
        <div style={{ fontSize: 11, color: '#8b7fa0', marginTop: 2, display: 'flex', flexWrap: 'wrap', gap: 8 }}>
          <span>{sc.xpReward} XP</span>
          <span>{sc.practiceType}</span>
          <span>{sc.questionCount} questions</span>
          {sc.soloPracticeHtml ? <span style={{ color: '#4ade80' }}>✓ Solo</span> : <span>— Solo</span>}
          {sc.feynmanPrompt ? <span style={{ color: '#4ade80' }}>✓ Feynman</span> : <span>— Feynman</span>}
        </div>
        <div style={{ fontSize: 10, marginTop: 4, display: 'flex', flexWrap: 'wrap', gap: 6 }}>
          {sc.learningObjectives?.length ? (
            <span style={metaBadge('#2dd4bf')}>{sc.learningObjectives.length} objectives</span>
          ) : null}
          {sc.challenge ? (
            <span style={metaBadge('#fb923c')}>⚡ challenge</span>
          ) : null}
          {sc.miniProject ? (
            <span style={metaBadge('#8b5cf6')}>🏗 mini project</span>
          ) : null}
          {sc.commonMistakes?.length ? (
            <span style={metaBadge('#f87171')}>{sc.commonMistakes.length} mistakes</span>
          ) : null}
          {sc.assessmentCriteria?.length ? (
            <span style={metaBadge('#c9a227')}>{sc.assessmentCriteria.length} criteria</span>
          ) : null}
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
  moduleId,
  onSave,
  onCancel,
}: {
  initial: Partial<AdminLesson>
  moduleId: string
  onSave: (sc: Partial<AdminLesson>) => void
  onCancel: () => void
}) {
  const [form, setForm] = useState<Partial<AdminLesson>>({ ...initial, moduleId })
  const set = (k: keyof AdminLesson, v: unknown) => setForm(prev => ({ ...prev, [k]: v }))

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

export default function AdminLessonsPage() {
  const { moduleId } = useParams<{ moduleId: string }>()
  const navigate = useNavigate()
  const [chunk, setChunk] = useState<AdminChunk | null>(null)
  const [lessons, setLessons] = useState<AdminLesson[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editSc, setEditSc] = useState<AdminLesson | null>(null)
  const [error, setError] = useState<string | null>(null)

  const load = () => {
    if (!moduleId) return
    setLoading(true)
    Promise.all([adminChunkApi.get(moduleId), adminLessonApi.list(moduleId)])
      .then(([c, scs]) => { setChunk(c); setLessons(scs) })
      .catch(() => setError('Failed to load data'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [moduleId])

  const handleSave = async (form: Partial<AdminLesson>) => {
    try {
      if (editSc) {
        const updated = await adminLessonApi.update(editSc.id, form)
        setLessons(prev => prev.map(s => s.id === updated.id ? updated : s))
      } else {
        const created = await adminLessonApi.create({ ...form, moduleId })
        setLessons(prev => [...prev, created])
      }
      setShowForm(false)
      setEditSc(null)
    } catch {
      setError('Failed to save lesson')
    }
  }

  const handleDelete = async (id: string) => {
    if (!confirm('Delete this lesson and all its questions?')) return
    try {
      await adminLessonApi.delete(id)
      setLessons(prev => prev.filter(s => s.id !== id))
    } catch {
      setError('Failed to delete lesson')
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
            {chunk ? `${chunk.glyph} ${chunk.title}` : 'Lessons'}
          </h1>
          <p style={{ color: '#8b7fa0', fontSize: 13, marginTop: 4 }}>Manage lessons for this module</p>
        </div>
        <button className="btn btn-primary" style={{ fontSize: 12 }} onClick={() => { setEditSc(null); setShowForm(true) }}>
          + New Lesson
        </button>
      </div>

      {error && <div style={{ color: '#f87171', fontSize: 13, marginBottom: 12, padding: '8px 12px', background: 'rgba(248,113,113,.1)', borderRadius: 6 }}>{error}</div>}

      {(showForm || editSc) && (
        <InlineForm
          initial={editSc ?? BLANK}
          moduleId={moduleId!}
          onSave={handleSave}
          onCancel={() => { setShowForm(false); setEditSc(null) }}
        />
      )}

      {loading ? (
        <div style={{ color: '#8b7fa0', fontSize: 14 }}>Loading…</div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {lessons.length === 0 && <div style={{ color: '#8b7fa0', fontSize: 13 }}>No lessons yet.</div>}
          {lessons.map(sc => (
            <LessonRow
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

const metaBadge = (color: string): React.CSSProperties => ({
  padding: '1px 6px', borderRadius: 4,
  border: `1px solid ${color}55`,
  color, background: `${color}18`,
  fontFamily: 'Cinzel, serif',
})
