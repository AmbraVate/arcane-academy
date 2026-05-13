import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { adminSubChunkApi, type AdminSubChunk, type StoryBeat, type TestCase } from '@/shared/api/adminServices'

type Tab = 'meta' | 'hook' | 'explanation' | 'story' | 'guided' | 'solo' | 'feynman'

const TABS: { id: Tab; label: string }[] = [
  { id: 'meta',        label: 'Meta'         },
  { id: 'hook',        label: 'Hook'         },
  { id: 'explanation', label: 'Explanation'  },
  { id: 'story',       label: 'Story Beats'  },
  { id: 'guided',      label: 'Guided Prac.' },
  { id: 'solo',        label: 'Solo Prac.'   },
  { id: 'feynman',     label: 'Feynman'      },
]

const PRACTICE_TYPES = ['JAVA', 'TAILWIND', 'NONE']
const BEAT_TYPES = ['narration', 'dialogue', 'example']

// ── Story beat editor ─────────────────────────────────────────────────────────

function StoryBeatEditor({ beats, onChange }: { beats: StoryBeat[]; onChange: (b: StoryBeat[]) => void }) {
  const updateBeat = (i: number, key: keyof StoryBeat, val: unknown) => {
    const next = beats.map((b, idx) => idx === i ? { ...b, [key]: val } : b)
    onChange(next)
  }
  const addBeat = (type: StoryBeat['type']) => onChange([...beats, { type, text: '' }])
  const removeBeat = (i: number) => onChange(beats.filter((_, idx) => idx !== i))
  const moveBeat = (i: number, dir: -1 | 1) => {
    const next = [...beats]
    const j = i + dir
    if (j < 0 || j >= next.length) return
    ;[next[i], next[j]] = [next[j], next[i]]
    onChange(next)
  }

  return (
    <div>
      <div style={{ display: 'flex', gap: 8, marginBottom: 14 }}>
        {BEAT_TYPES.map(t => (
          <button
            key={t}
            className="btn btn-ghost"
            style={{ fontSize: 11, padding: '5px 14px' }}
            onClick={() => addBeat(t as StoryBeat['type'])}
          >
            + {t}
          </button>
        ))}
      </div>

      {beats.length === 0 && <div style={{ color: '#8b7fa0', fontSize: 13 }}>No beats yet. Add one above.</div>}

      {beats.map((beat, i) => (
        <div key={i} style={{
          background: '#16132b',
          border: `1px solid ${beat.type === 'narration' ? '#2e2850' : beat.type === 'dialogue' ? 'rgba(139,92,246,.3)' : 'rgba(45,212,191,.3)'}`,
          borderRadius: 8,
          padding: 14,
          marginBottom: 10,
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 10 }}>
            <span style={{
              fontSize: 10, fontFamily: 'Cinzel, serif', padding: '2px 8px', borderRadius: 4,
              background: beat.type === 'narration' ? '#2e2850' : beat.type === 'dialogue' ? 'rgba(139,92,246,.2)' : 'rgba(45,212,191,.2)',
              color: beat.type === 'narration' ? '#8b7fa0' : beat.type === 'dialogue' ? '#c4b5fd' : '#2dd4bf',
            }}>
              {beat.type.toUpperCase()}
            </span>
            <div style={{ flex: 1 }} />
            <button style={iconBtn} onClick={() => moveBeat(i, -1)}>↑</button>
            <button style={iconBtn} onClick={() => moveBeat(i, 1)}>↓</button>
            <button style={{ ...iconBtn, color: '#f87171' }} onClick={() => removeBeat(i)}>✕</button>
          </div>

          {beat.type === 'dialogue' && (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 8, marginBottom: 8 }}>
              <label style={labelStyle}>Speaker <input style={inputStyle} value={beat.speaker ?? ''} onChange={e => updateBeat(i, 'speaker', e.target.value)} /></label>
              <label style={labelStyle}>Avatar (emoji/url) <input style={inputStyle} value={beat.av ?? ''} onChange={e => updateBeat(i, 'av', e.target.value)} /></label>
              <label style={labelStyle}>Speaker CSS class <input style={inputStyle} value={beat.sCls ?? ''} onChange={e => updateBeat(i, 'sCls', e.target.value)} /></label>
            </div>
          )}

          {beat.type === 'narration' && (
            <label style={labelStyle}>
              CSS class <input style={inputStyle} value={beat.cls ?? ''} onChange={e => updateBeat(i, 'cls', e.target.value)} />
            </label>
          )}

          <label style={{ ...labelStyle, marginTop: 8 }}>
            {beat.type === 'example' ? 'Code' : 'HTML text'}
            <textarea
              style={{ ...inputStyle, minHeight: 80, resize: 'vertical', fontFamily: beat.type === 'example' ? 'monospace' : 'inherit' }}
              value={beat.text ?? ''}
              onChange={e => updateBeat(i, 'text', e.target.value)}
            />
          </label>
        </div>
      ))}
    </div>
  )
}

// ── Test case editor ───────────────────────────────────────────────────────────

function TestCaseEditor({ tests, onChange }: { tests: TestCase[]; onChange: (t: TestCase[]) => void }) {
  const update = (i: number, key: string, val: string) => {
    const next = tests.map((t, idx) => idx === i ? { ...t, [key]: val } : t)
    onChange(next)
  }
  const add = () => onChange([...tests, { label: '', input: '', expected: '' }])
  const remove = (i: number) => onChange(tests.filter((_, idx) => idx !== i))

  return (
    <div>
      <button className="btn btn-ghost" style={{ fontSize: 11, padding: '5px 14px', marginBottom: 12 }} onClick={add}>
        + Add Test Case
      </button>
      {tests.length === 0 && <div style={{ color: '#8b7fa0', fontSize: 13 }}>No test cases.</div>}
      {tests.map((t, i) => (
        <div key={i} style={{ background: '#16132b', border: '1px solid #2e2850', borderRadius: 8, padding: 12, marginBottom: 8 }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr auto', gap: 8, alignItems: 'end' }}>
            <label style={labelStyle}>Label <input style={inputStyle} value={t.label ?? ''} onChange={e => update(i, 'label', e.target.value)} /></label>
            <label style={labelStyle}>Input (vars) <input style={inputStyle} value={t.input ?? ''} onChange={e => update(i, 'input', e.target.value)} /></label>
            <label style={labelStyle}>Expected <input style={inputStyle} value={t.expected ?? ''} onChange={e => update(i, 'expected', e.target.value)} /></label>
            <button style={{ ...iconBtn, color: '#f87171', marginBottom: 1 }} onClick={() => remove(i)}>✕</button>
          </div>
        </div>
      ))}
    </div>
  )
}

// ── Main page ─────────────────────────────────────────────────────────────────

export default function AdminSubChunkEditorPage() {
  const { subChunkId } = useParams<{ subChunkId: string }>()
  const navigate = useNavigate()
  const [sc, setSc] = useState<AdminSubChunk | null>(null)
  const [form, setForm] = useState<Partial<AdminSubChunk>>({})
  const [activeTab, setActiveTab] = useState<Tab>('meta')
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [saved, setSaved] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!subChunkId) return
    adminSubChunkApi.get(subChunkId)
      .then(data => { setSc(data); setForm(data) })
      .catch(() => setError('Failed to load sub-chunk'))
      .finally(() => setLoading(false))
  }, [subChunkId])

  const set = (k: keyof AdminSubChunk, v: unknown) => setForm(prev => ({ ...prev, [k]: v }))

  const handleSave = async () => {
    if (!subChunkId) return
    setSaving(true)
    setSaved(false)
    try {
      const updated = await adminSubChunkApi.update(subChunkId, form)
      setSc(updated)
      setForm(updated)
      setSaved(true)
      setTimeout(() => setSaved(false), 2000)
    } catch {
      setError('Failed to save')
    } finally {
      setSaving(false)
    }
  }

  if (loading) return <div style={{ color: '#8b7fa0', fontSize: 14 }}>Loading…</div>
  if (!sc) return <div style={{ color: '#f87171', fontSize: 14 }}>{error ?? 'Sub-chunk not found'}</div>

  return (
    <div>
      {/* Breadcrumb */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 20, fontSize: 12, color: '#8b7fa0' }}>
        <span style={{ cursor: 'pointer', color: '#8b5cf6' }} onClick={() => navigate('/admin/chunks')}>Content</span>
        <span>›</span>
        <span style={{ cursor: 'pointer', color: '#8b5cf6' }} onClick={() => navigate(`/admin/chunks/${sc.chunkId}/subchunks`)}>Sub-chunks</span>
        <span>›</span>
        <span style={{ color: '#e8e0f0' }}>{sc.title}</span>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 20, color: '#c9a227' }}>
          Edit: {sc.title}
        </h1>
        <div style={{ display: 'flex', gap: 10, alignItems: 'center' }}>
          {saved && <span style={{ color: '#4ade80', fontSize: 12 }}>✓ Saved</span>}
          {error && <span style={{ color: '#f87171', fontSize: 12 }}>{error}</span>}
          <button className="btn btn-primary" style={{ fontSize: 12 }} onClick={handleSave} disabled={saving}>
            {saving ? 'Saving…' : 'Save Changes'}
          </button>
        </div>
      </div>

      {/* Tabs */}
      <div style={{ display: 'flex', gap: 2, marginBottom: 20, borderBottom: '1px solid #2e2850', paddingBottom: 0 }}>
        {TABS.map(tab => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            style={{
              fontFamily: 'Cinzel, serif',
              fontSize: 11,
              padding: '8px 14px',
              border: 'none',
              borderBottom: activeTab === tab.id ? '2px solid #8b5cf6' : '2px solid transparent',
              background: 'transparent',
              color: activeTab === tab.id ? '#c4b5fd' : '#8b7fa0',
              cursor: 'pointer',
              transition: 'all .15s',
              marginBottom: -1,
            }}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Tab content */}
      <div style={{ background: '#16132b', border: '1px solid #2e2850', borderRadius: 10, padding: 24 }}>

        {activeTab === 'meta' && (
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 14 }}>
            <label style={labelStyle}>Title <input style={inputStyle} value={form.title ?? ''} onChange={e => set('title', e.target.value)} /></label>
            <label style={labelStyle}>Sort Order <input style={inputStyle} type="number" value={form.sortOrder ?? 0} onChange={e => set('sortOrder', parseInt(e.target.value))} /></label>
            <label style={labelStyle}>XP Reward <input style={inputStyle} type="number" value={form.xpReward ?? 100} onChange={e => set('xpReward', parseInt(e.target.value))} /></label>
            <label style={labelStyle}>
              Practice Type
              <select style={inputStyle} value={form.practiceType ?? 'JAVA'} onChange={e => set('practiceType', e.target.value)}>
                {PRACTICE_TYPES.map(p => <option key={p} value={p}>{p}</option>)}
              </select>
            </label>
            <label style={labelStyle}>Filename <input style={inputStyle} value={form.filename ?? ''} onChange={e => set('filename', e.target.value)} /></label>
          </div>
        )}

        {activeTab === 'hook' && (
          <label style={labelStyle}>
            Hook HTML
            <textarea
              style={{ ...inputStyle, minHeight: 260, resize: 'vertical' }}
              value={form.hookHtml ?? ''}
              onChange={e => set('hookHtml', e.target.value || null)}
            />
          </label>
        )}

        {activeTab === 'explanation' && (
          <label style={labelStyle}>
            Explanation HTML
            <textarea
              style={{ ...inputStyle, minHeight: 260, resize: 'vertical' }}
              value={form.explanationHtml ?? ''}
              onChange={e => set('explanationHtml', e.target.value || null)}
            />
          </label>
        )}

        {activeTab === 'story' && (
          <StoryBeatEditor
            beats={form.storyBeats ?? []}
            onChange={beats => set('storyBeats', beats.length > 0 ? beats : null)}
          />
        )}

        {activeTab === 'guided' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
            <label style={labelStyle}>
              Guided Practice HTML (instructions / brief)
              <textarea
                style={{ ...inputStyle, minHeight: 140, resize: 'vertical' }}
                value={form.guidedPracticeHtml ?? ''}
                onChange={e => set('guidedPracticeHtml', e.target.value || null)}
              />
            </label>
            <label style={labelStyle}>
              Starter Code
              <textarea
                style={{ ...inputStyle, minHeight: 140, resize: 'vertical', fontFamily: 'monospace', fontSize: 12 }}
                value={form.guidedPracticeStarterCode ?? ''}
                onChange={e => set('guidedPracticeStarterCode', e.target.value || null)}
              />
            </label>
            <div>
              <div style={{ ...labelStyle, marginBottom: 8 }}>Test Cases</div>
              <TestCaseEditor
                tests={form.guidedPracticeTests ?? []}
                onChange={tests => set('guidedPracticeTests', tests.length > 0 ? tests : null)}
              />
            </div>
          </div>
        )}

        {activeTab === 'solo' && (
          <label style={labelStyle}>
            Solo Practice HTML (challenge prompt — no starter code)
            <textarea
              style={{ ...inputStyle, minHeight: 200, resize: 'vertical' }}
              value={form.soloPracticeHtml ?? ''}
              onChange={e => set('soloPracticeHtml', e.target.value || null)}
            />
          </label>
        )}

        {activeTab === 'feynman' && (
          <label style={labelStyle}>
            Feynman Prompt
            <textarea
              style={{ ...inputStyle, minHeight: 120, resize: 'vertical' }}
              value={form.feynmanPrompt ?? ''}
              onChange={e => set('feynmanPrompt', e.target.value || null)}
            />
          </label>
        )}
      </div>
    </div>
  )
}

const labelStyle: React.CSSProperties = {
  display: 'flex', flexDirection: 'column', gap: 5,
  fontSize: 11, fontFamily: 'Cinzel, serif', color: '#8b7fa0',
}

const inputStyle: React.CSSProperties = {
  background: '#1e1a35', border: '1px solid #2e2850', borderRadius: 6,
  color: '#e8e0f0', fontSize: 13, padding: '7px 10px', outline: 'none', width: '100%',
}

const iconBtn: React.CSSProperties = {
  background: 'transparent', border: '1px solid #2e2850', borderRadius: 4,
  color: '#8b7fa0', cursor: 'pointer', padding: '2px 7px', fontSize: 12,
}
