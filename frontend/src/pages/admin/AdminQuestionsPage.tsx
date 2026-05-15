import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { adminQuestionApi, adminSubChunkApi, type AdminQuestion, type AdminSubChunk } from '../../api/adminServices'

const Q_TYPES = ['MULTIPLE_CHOICE', 'TRUE_FALSE', 'SHORT_ANSWER', 'CODE_OUTPUT']
const TIERS = ['FOUNDATION', 'PRACTITIONER', 'EXPERT']

const BLANK: Partial<AdminQuestion> = {
  type: 'MULTIPLE_CHOICE', tier: 'FOUNDATION',
  questionHtml: '', codeSnippet: null, options: ['', '', '', ''], correctAnswer: '', explanationHtml: '',
}

function QuestionForm({
  initial,
  subChunkId,
  onSave,
  onCancel,
}: {
  initial: Partial<AdminQuestion>
  subChunkId: string
  onSave: (q: Partial<AdminQuestion>) => void
  onCancel: () => void
}) {
  const [form, setForm] = useState<Partial<AdminQuestion>>({ ...initial, subChunkId })
  const set = (k: keyof AdminQuestion, v: unknown) => setForm(prev => ({ ...prev, [k]: v }))

  const setOption = (i: number, val: string) => {
    const opts = [...(form.options ?? ['', '', '', ''])]
    opts[i] = val
    set('options', opts)
  }

  const isMultiChoice = form.type === 'MULTIPLE_CHOICE'

  return (
    <div style={{ background: '#1e1a35', border: '1px solid #2e2850', borderRadius: 10, padding: 20, marginBottom: 16 }}>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginBottom: 12 }}>
        <label style={labelStyle}>
          Type
          <select style={inputStyle} value={form.type ?? 'MULTIPLE_CHOICE'} onChange={e => set('type', e.target.value)}>
            {Q_TYPES.map(t => <option key={t} value={t}>{t.replace('_', ' ')}</option>)}
          </select>
        </label>
        <label style={labelStyle}>
          Tier
          <select style={inputStyle} value={form.tier ?? 'FOUNDATION'} onChange={e => set('tier', e.target.value)}>
            {TIERS.map(t => <option key={t} value={t}>{t}</option>)}
          </select>
        </label>
      </div>

      <label style={{ ...labelStyle, marginBottom: 10 }}>
        Question HTML
        <textarea style={{ ...inputStyle, minHeight: 80, resize: 'vertical' }}
          value={form.questionHtml ?? ''}
          onChange={e => set('questionHtml', e.target.value)}
        />
      </label>

      <label style={{ ...labelStyle, marginBottom: 10 }}>
        Code Snippet (optional)
        <textarea style={{ ...inputStyle, minHeight: 60, resize: 'vertical', fontFamily: 'monospace', fontSize: 12 }}
          value={form.codeSnippet ?? ''}
          onChange={e => set('codeSnippet', e.target.value || null)}
        />
      </label>

      {isMultiChoice && (
        <div style={{ marginBottom: 10 }}>
          <div style={{ ...labelStyle, marginBottom: 6 }}>Options</div>
          {(form.options ?? ['', '', '', '']).map((opt, i) => (
            <input key={i} style={{ ...inputStyle, marginBottom: 6 }}
              placeholder={`Option ${i + 1}`}
              value={opt}
              onChange={e => setOption(i, e.target.value)}
            />
          ))}
        </div>
      )}

      <label style={{ ...labelStyle, marginBottom: 10 }}>
        Correct Answer {isMultiChoice ? '(exact text of the correct option)' : ''}
        <input style={inputStyle} value={form.correctAnswer ?? ''} onChange={e => set('correctAnswer', e.target.value)} />
      </label>

      <label style={{ ...labelStyle, marginBottom: 14 }}>
        Explanation HTML
        <textarea style={{ ...inputStyle, minHeight: 70, resize: 'vertical' }}
          value={form.explanationHtml ?? ''}
          onChange={e => set('explanationHtml', e.target.value)}
        />
      </label>

      <div style={{ display: 'flex', gap: 10 }}>
        <button className="btn btn-primary" style={{ fontSize: 12, padding: '6px 16px' }} onClick={() => onSave(form)}>Save</button>
        <button className="btn btn-ghost" style={{ fontSize: 12, padding: '6px 16px' }} onClick={onCancel}>Cancel</button>
      </div>
    </div>
  )
}

const TIER_COLOR: Record<string, string> = {
  FOUNDATION: '#4ade80', PRACTITIONER: '#c9a227', EXPERT: '#8b5cf6',
}

export default function AdminQuestionsPage() {
  const { subChunkId } = useParams<{ subChunkId: string }>()
  const navigate = useNavigate()
  const [sc, setSc] = useState<AdminSubChunk | null>(null)
  const [questions, setQuestions] = useState<AdminQuestion[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editQ, setEditQ] = useState<AdminQuestion | null>(null)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!subChunkId) return
    Promise.all([adminSubChunkApi.get(subChunkId), adminQuestionApi.list(subChunkId)])
      .then(([s, qs]) => { setSc(s); setQuestions(qs) })
      .catch(() => setError('Failed to load data'))
      .finally(() => setLoading(false))
  }, [subChunkId])

  const handleSave = async (form: Partial<AdminQuestion>) => {
    try {
      if (editQ) {
        const updated = await adminQuestionApi.update(editQ.id, form)
        setQuestions(prev => prev.map(q => q.id === updated.id ? updated : q))
      } else {
        const created = await adminQuestionApi.create({ ...form, subChunkId })
        setQuestions(prev => [...prev, created])
      }
      setShowForm(false)
      setEditQ(null)
    } catch {
      setError('Failed to save question')
    }
  }

  const handleDelete = async (id: string) => {
    if (!confirm('Delete this question?')) return
    try {
      await adminQuestionApi.delete(id)
      setQuestions(prev => prev.filter(q => q.id !== id))
    } catch {
      setError('Failed to delete question')
    }
  }

  return (
    <div>
      {/* Breadcrumb */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 20, fontSize: 12, color: '#8b7fa0' }}>
        <span style={{ cursor: 'pointer', color: '#8b5cf6' }} onClick={() => navigate('/admin/chunks')}>Content</span>
        <span>›</span>
        {sc && <span style={{ cursor: 'pointer', color: '#8b5cf6' }} onClick={() => navigate(`/admin/chunks/${sc.chunkId}/subchunks`)}>Lessons</span>}
        {sc && <span>›</span>}
        <span style={{ color: '#e8e0f0' }}>{sc?.title ?? 'Questions'}</span>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#c9a227' }}>Questions</h1>
          <p style={{ color: '#8b7fa0', fontSize: 13, marginTop: 4 }}>{questions.length} questions for this lesson</p>
        </div>
        <div style={{ display: 'flex', gap: 10 }}>
          {sc && (
            <button className="btn btn-ghost" style={{ fontSize: 12 }} onClick={() => navigate(`/admin/subchunks/${subChunkId}/edit`)}>
              ✏️ Edit Content
            </button>
          )}
          <button className="btn btn-primary" style={{ fontSize: 12 }} onClick={() => { setEditQ(null); setShowForm(true) }}>
            + Add Question
          </button>
        </div>
      </div>

      {error && <div style={{ color: '#f87171', fontSize: 13, marginBottom: 12, padding: '8px 12px', background: 'rgba(248,113,113,.1)', borderRadius: 6 }}>{error}</div>}

      {(showForm || editQ) && (
        <QuestionForm
          initial={editQ ?? BLANK}
          subChunkId={subChunkId!}
          onSave={handleSave}
          onCancel={() => { setShowForm(false); setEditQ(null) }}
        />
      )}

      {loading ? (
        <div style={{ color: '#8b7fa0', fontSize: 14 }}>Loading…</div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {questions.length === 0 && <div style={{ color: '#8b7fa0', fontSize: 13 }}>No questions yet.</div>}
          {questions.map((q, idx) => (
            <div key={q.id} style={{
              background: '#16132b',
              border: '1px solid #2e2850',
              borderRadius: 9,
              padding: '14px 18px',
            }}>
              <div style={{ display: 'flex', alignItems: 'flex-start', gap: 12 }}>
                <span style={{ color: '#8b7fa0', fontSize: 12, minWidth: 22, paddingTop: 2 }}>#{idx + 1}</span>
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ display: 'flex', gap: 8, marginBottom: 6 }}>
                    <span style={{ fontSize: 10, fontFamily: 'Cinzel, serif', padding: '2px 7px', borderRadius: 4, background: '#2e2850', color: '#8b7fa0' }}>
                      {q.type.replace('_', ' ')}
                    </span>
                    <span style={{ fontSize: 10, fontFamily: 'Cinzel, serif', padding: '2px 7px', borderRadius: 4, border: `1px solid ${TIER_COLOR[q.tier] ?? '#2e2850'}`, color: TIER_COLOR[q.tier] ?? '#8b7fa0' }}>
                      {q.tier}
                    </span>
                  </div>
                  <div
                    style={{ fontSize: 13, color: '#e8e0f0', marginBottom: 4 }}
                    dangerouslySetInnerHTML={{ __html: q.questionHtml }}
                  />
                  {q.options && (
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6, marginTop: 6 }}>
                      {q.options.map((opt, oi) => (
                        <span key={oi} style={{
                          fontSize: 11,
                          padding: '2px 9px',
                          borderRadius: 4,
                          background: opt === q.correctAnswer ? 'rgba(74,222,128,.15)' : '#1e1a35',
                          border: `1px solid ${opt === q.correctAnswer ? '#4ade80' : '#2e2850'}`,
                          color: opt === q.correctAnswer ? '#4ade80' : '#8b7fa0',
                        }}>
                          {opt}
                        </span>
                      ))}
                    </div>
                  )}
                </div>
                <div style={{ display: 'flex', gap: 6, flexShrink: 0 }}>
                  <button className="btn btn-ghost" style={{ fontSize: 11, padding: '4px 10px' }} onClick={() => { setEditQ(q); setShowForm(false) }}>✏️</button>
                  <button className="btn btn-ghost" style={{ fontSize: 11, padding: '4px 10px', borderColor: 'rgba(248,113,113,.3)', color: '#f87171' }} onClick={() => handleDelete(q.id)}>🗑️</button>
                </div>
              </div>
            </div>
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
