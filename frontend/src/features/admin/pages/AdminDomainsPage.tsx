import { useEffect, useState } from 'react'
import { adminDomainApi, type AdminDomain } from '@/shared/api/adminServices'

const BLANK_TOPIC: Partial<AdminDomain> = {
  id: '', name: '', glyph: '📚', tagline: '', accentColor: 'var(--purple)', sortOrder: 0, active: false,
}

function TopicForm({
  initial,
  onSave,
  onCancel,
}: {
  initial: Partial<AdminDomain>
  onSave: (t: Partial<AdminDomain>) => void
  onCancel: () => void
}) {
  const [form, setForm] = useState<Partial<AdminDomain>>(initial)
  const set = (k: keyof AdminDomain, v: unknown) => setForm(prev => ({ ...prev, [k]: v }))
  const isNew = !initial.id

  return (
    <div style={{ background: '#1e1a35', border: '1px solid #2e2850', borderRadius: 10, padding: 20, marginBottom: 20 }}>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
        {isNew && (
          <label style={labelStyle}>
            ID (slug)
            <input style={inputStyle} placeholder="e.g. java" value={form.id ?? ''} onChange={e => set('id', e.target.value)} />
          </label>
        )}
        <label style={labelStyle}>
          Name
          <input style={inputStyle} value={form.name ?? ''} onChange={e => set('name', e.target.value)} />
        </label>
        <label style={labelStyle}>
          Glyph
          <input style={inputStyle} value={form.glyph ?? ''} onChange={e => set('glyph', e.target.value)} />
        </label>
        <label style={{ ...labelStyle, gridColumn: '1 / -1' }}>
          Tagline
          <input style={inputStyle} value={form.tagline ?? ''} onChange={e => set('tagline', e.target.value)} />
        </label>
        <label style={labelStyle}>
          Accent Color (CSS var or hex)
          <input style={inputStyle} placeholder="var(--purple)" value={form.accentColor ?? ''} onChange={e => set('accentColor', e.target.value)} />
        </label>
        <label style={labelStyle}>
          Sort Order
          <input style={inputStyle} type="number" value={form.sortOrder ?? 0} onChange={e => set('sortOrder', parseInt(e.target.value))} />
        </label>
        <label style={{ ...labelStyle, flexDirection: 'row', alignItems: 'center', gap: 8 }}>
          <input type="checkbox" checked={form.active ?? false} onChange={e => set('active', e.target.checked)} />
          Active (visible to learners)
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

export default function AdminDomainsPage() {
  const [topics, setTopics] = useState<AdminDomain[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editTopic, setEditTopic] = useState<AdminDomain | null>(null)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const load = () => {
    setLoading(true)
    adminDomainApi.list()
      .then(setTopics)
      .catch(() => setError('Failed to load topics'))
      .finally(() => setLoading(false))
  }

  useEffect(() => { load() }, [])

  async function handleSave(form: Partial<AdminDomain>) {
    if (!form.name?.trim()) { setError('Name is required'); return }
    setSaving(true); setError(null)
    try {
      if (editTopic) {
        await adminDomainApi.update(editTopic.id, form)
      } else {
        if (!form.id?.trim()) { setError('ID is required for new topics'); setSaving(false); return }
        await adminDomainApi.create(form as AdminDomain)
      }
      setShowForm(false); setEditTopic(null); load()
    } catch (e: unknown) {
      const msg = (e as { response?: { data?: { message?: string } } })?.response?.data?.message
      setError(msg ?? 'Save failed')
    } finally {
      setSaving(false)
    }
  }

  async function handleDelete(topic: AdminDomain) {
    if (!confirm(`Delete topic "${topic.name}"? This will fail if it has modules.`)) return
    setError(null)
    try {
      await adminDomainApi.delete(topic.id)
      load()
    } catch (e: unknown) {
      const status = (e as { response?: { status?: number } })?.response?.status
      setError(status === 409 ? 'Cannot delete — this topic still has modules. Remove all modules first.' : 'Delete failed')
    }
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <div>
          <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#c9a227', margin: 0 }}>Topics</h1>
          <p style={{ color: '#8b7fa0', fontSize: 12, marginTop: 4 }}>Manage learning topic definitions</p>
        </div>
        <button
          className="btn btn-primary"
          style={{ fontSize: 13, padding: '8px 18px' }}
          onClick={() => { setEditTopic(null); setShowForm(true) }}
          disabled={saving}
        >
          + New Topic
        </button>
      </div>

      {error && (
        <div style={{ background: 'rgba(239,68,68,.12)', border: '1px solid rgba(239,68,68,.3)', borderRadius: 8, padding: '10px 14px', color: '#f87171', fontSize: 13, marginBottom: 16 }}>
          {error}
        </div>
      )}

      {(showForm && !editTopic) && (
        <TopicForm initial={BLANK_TOPIC} onSave={handleSave} onCancel={() => setShowForm(false)} />
      )}

      {loading ? (
        <div style={{ color: '#8b7fa0', textAlign: 'center', paddingTop: 40 }}>Loading…</div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
          {topics.map(topic => (
            <div key={topic.id}>
              {editTopic?.id === topic.id ? (
                <TopicForm
                  initial={editTopic}
                  onSave={handleSave}
                  onCancel={() => setEditTopic(null)}
                />
              ) : (
                <div style={{
                  background: '#100e1f', border: '1px solid #1e1a35', borderRadius: 10, padding: '14px 18px',
                  display: 'flex', alignItems: 'center', gap: 14,
                }}>
                  <span style={{ fontSize: 22, flexShrink: 0 }}>{topic.glyph}</span>
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                      <span style={{ fontFamily: 'Cinzel, serif', fontSize: 14, color: '#e2e8f0' }}>{topic.name}</span>
                      <span style={{ fontSize: 10, color: '#8b7fa0', fontFamily: 'monospace' }}>#{topic.id}</span>
                      <span style={{
                        fontSize: 10, padding: '2px 8px', borderRadius: 20, fontWeight: 600,
                        background: topic.active ? 'rgba(0,200,83,.12)' : 'rgba(255,255,255,.05)',
                        color: topic.active ? '#00c853' : '#8b7fa0',
                        border: `1px solid ${topic.active ? 'rgba(0,200,83,.3)' : 'rgba(255,255,255,.1)'}`,
                      }}>
                        {topic.active ? 'Active' : 'Coming Soon'}
                      </span>
                      <span style={{ fontSize: 10, color: '#8b7fa0', marginLeft: 'auto' }}>order: {topic.sortOrder}</span>
                    </div>
                    <div style={{ fontSize: 12, color: '#8b7fa0', marginTop: 3, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                      {topic.tagline}
                    </div>
                  </div>
                  <div style={{ display: 'flex', gap: 8, flexShrink: 0 }}>
                    <button
                      className="btn btn-ghost"
                      style={{ fontSize: 11, padding: '5px 12px' }}
                      onClick={() => { setEditTopic(topic); setShowForm(false) }}
                    >
                      Edit
                    </button>
                    <button
                      className="btn btn-ghost"
                      style={{ fontSize: 11, padding: '5px 12px', color: '#f87171', borderColor: 'rgba(248,113,113,.3)' }}
                      onClick={() => handleDelete(topic)}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              )}
            </div>
          ))}
          {topics.length === 0 && (
            <div style={{ color: '#8b7fa0', textAlign: 'center', paddingTop: 40 }}>No topics yet</div>
          )}
        </div>
      )}
    </div>
  )
}

const labelStyle: React.CSSProperties = {
  display: 'flex', flexDirection: 'column', gap: 5,
  fontSize: 12, color: '#8b7fa0', fontFamily: 'Cinzel, serif',
}
const inputStyle: React.CSSProperties = {
  background: '#0a0916', border: '1px solid #2e2850', borderRadius: 6,
  padding: '7px 10px', color: '#e2e8f0', fontSize: 13, width: '100%', boxSizing: 'border-box',
}
