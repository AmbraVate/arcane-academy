import { useEffect, useRef, useState } from 'react'
import { adminChunkApi, adminContentApi, adminDomainApi, type AdminChunk, type AdminDomain } from '@/shared/api/adminServices'

export default function AdminImportExportPage() {
  const [topics, setTopics] = useState<AdminDomain[]>([])
  const [chunks, setChunks] = useState<AdminChunk[]>([])
  const [topicFilter, setTopicFilter] = useState('')
  const [loadingChunks, setLoadingChunks] = useState(false)
  const [importing, setImporting] = useState(false)
  const [importResult, setImportResult] = useState<{ status: string; moduleId: string; lessons: number } | null>(null)
  const [importError, setImportError] = useState<string | null>(null)
  const fileRef = useRef<HTMLInputElement>(null)

  useEffect(() => {
    adminDomainApi.list().then(setTopics).catch(() => {})
  }, [])

  useEffect(() => {
    setLoadingChunks(true)
    adminChunkApi.list(topicFilter || undefined)
      .then(setChunks)
      .catch(() => {})
      .finally(() => setLoadingChunks(false))
  }, [topicFilter])

  const handleExport = (moduleId: string, chunkTitle: string) => {
    const path = adminContentApi.exportChunk(moduleId)
    const a = document.createElement('a')
    a.href = path
    a.download = `chunk-${chunkTitle.toLowerCase().replace(/\s+/g, '-')}.json`
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
  }

  const handleImport = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (!file) return
    setImporting(true)
    setImportResult(null)
    setImportError(null)
    try {
      const result = await adminContentApi.importChunk(file)
      setImportResult(result)
    } catch {
      setImportError('Import failed. Ensure the file is a valid chunk export JSON.')
    } finally {
      setImporting(false)
      if (fileRef.current) fileRef.current.value = ''
    }
  }

  return (
    <div>
      <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#c9a227', marginBottom: 6 }}>
        Import / Export
      </h1>
      <p style={{ color: '#8b7fa0', fontSize: 13, marginBottom: 32 }}>
        Transfer content chunks as JSON files — great for backups and moving content between environments.
      </p>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>

        {/* Export */}
        <div style={{ background: '#16132b', border: '1px solid #2e2850', borderRadius: 10, padding: 24 }}>
          <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 15, color: '#c4b5fd', marginBottom: 6 }}>
            📤 Export Chunk
          </h2>
          <p style={{ color: '#8b7fa0', fontSize: 12, marginBottom: 18 }}>
            Download a full module (including all lessons, questions, and story beats) as a JSON file.
          </p>

          {/* Topic filter */}
          <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap', marginBottom: 14 }}>
            <button
              onClick={() => setTopicFilter('')}
              style={{ ...filterBtn, background: topicFilter === '' ? 'rgba(139,92,246,.2)' : 'transparent', color: topicFilter === '' ? '#c4b5fd' : '#8b7fa0', borderColor: topicFilter === '' ? '#8b5cf6' : '#2e2850' }}
            >
              All
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

          {loadingChunks ? (
            <div style={{ color: '#8b7fa0', fontSize: 13 }}>Loading modules…</div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6, maxHeight: 360, overflowY: 'auto' }}>
              {chunks.length === 0 && <div style={{ color: '#8b7fa0', fontSize: 13 }}>No chunks found.</div>}
              {chunks.map(chunk => (
                <div key={chunk.id} style={{
                  display: 'flex', alignItems: 'center', gap: 12,
                  padding: '10px 14px',
                  background: '#1e1a35',
                  border: '1px solid #2e2850',
                  borderRadius: 8,
                }}>
                  <span style={{ fontSize: 18 }}>{chunk.glyph}</span>
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <div style={{ fontSize: 13, color: '#e8e0f0', fontWeight: 600 }}>{chunk.title}</div>
                    <div style={{ fontSize: 11, color: '#8b7fa0' }}>{chunk.subChunkCount} lessons</div>
                  </div>
                  <button
                    className="btn btn-ghost"
                    style={{ fontSize: 11, padding: '4px 12px', flexShrink: 0 }}
                    onClick={() => handleExport(chunk.id, chunk.title)}
                  >
                    â¬‡ Export
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Import */}
        <div style={{ background: '#16132b', border: '1px solid #2e2850', borderRadius: 10, padding: 24 }}>
          <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 15, color: '#c4b5fd', marginBottom: 6 }}>
            📥 Import Chunk
          </h2>
          <p style={{ color: '#8b7fa0', fontSize: 12, marginBottom: 18 }}>
            Upload a previously exported JSON file to create or update a chunk. If a chunk with the same ID exists, it will be updated in place.
          </p>

          <div
            style={{
              border: '2px dashed #2e2850',
              borderRadius: 10,
              padding: '36px 24px',
              textAlign: 'center',
              cursor: 'pointer',
              transition: 'border-color .2s',
            }}
            onClick={() => fileRef.current?.click()}
            onDragOver={e => { e.preventDefault(); (e.currentTarget as HTMLDivElement).style.borderColor = '#8b5cf6' }}
            onDragLeave={e => { (e.currentTarget as HTMLDivElement).style.borderColor = '#2e2850' }}
            onDrop={e => {
              e.preventDefault()
              ;(e.currentTarget as HTMLDivElement).style.borderColor = '#2e2850'
              const file = e.dataTransfer.files[0]
              if (file && fileRef.current) {
                const dt = new DataTransfer()
                dt.items.add(file)
                fileRef.current.files = dt.files
                fileRef.current.dispatchEvent(new Event('change', { bubbles: true }))
              }
            }}
          >
            <div style={{ fontSize: 32, marginBottom: 10 }}>📂</div>
            <div style={{ fontFamily: 'Cinzel, serif', fontSize: 13, color: '#c4b5fd', marginBottom: 6 }}>
              {importing ? 'Importing…' : 'Click or drag JSON file here'}
            </div>
            <div style={{ fontSize: 11, color: '#8b7fa0' }}>Accepts .json chunk export files</div>
          </div>

          <input
            ref={fileRef}
            type="file"
            accept=".json"
            style={{ display: 'none' }}
            onChange={handleImport}
          />

          {importResult && (
            <div style={{
              marginTop: 16,
              background: 'rgba(74,222,128,.07)',
              border: '1px solid rgba(74,222,128,.3)',
              borderRadius: 8,
              padding: '12px 16px',
            }}>
              <div style={{ color: '#4ade80', fontFamily: 'Cinzel, serif', fontSize: 12, marginBottom: 4 }}>
                ✓ Import Successful
              </div>
              <div style={{ fontSize: 12, color: '#e8e0f0' }}>
                Module <strong>{importResult.moduleId}</strong> — {importResult.lessons} lessons imported
              </div>
            </div>
          )}

          {importError && (
            <div style={{
              marginTop: 16,
              background: 'rgba(248,113,113,.07)',
              border: '1px solid rgba(248,113,113,.3)',
              borderRadius: 8,
              padding: '12px 16px',
            }}>
              <div style={{ color: '#f87171', fontSize: 12 }}>{importError}</div>
            </div>
          )}

          <div style={{ marginTop: 20, padding: '14px 16px', background: 'rgba(139,92,246,.07)', border: '1px solid rgba(139,92,246,.2)', borderRadius: 8 }}>
            <div style={{ fontFamily: 'Cinzel, serif', fontSize: 11, color: '#c4b5fd', marginBottom: 6 }}>Format notes</div>
            <ul style={{ listStyle: 'disc', paddingLeft: 16, fontSize: 11, color: '#8b7fa0', display: 'flex', flexDirection: 'column', gap: 4 }}>
              <li>The JSON must contain a top-level <code style={{ background: '#2e2850', padding: '1px 4px', borderRadius: 3 }}>chunk</code> object and a <code style={{ background: '#2e2850', padding: '1px 4px', borderRadius: 3 }}>lessons</code> array</li>
              <li>Questions are included under each lesson as a <code style={{ background: '#2e2850', padding: '1px 4px', borderRadius: 3 }}>questions</code> array</li>
              <li>Story beats are stored as a JSON array in <code style={{ background: '#2e2850', padding: '1px 4px', borderRadius: 3 }}>storyBeats</code></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  )
}

const filterBtn: React.CSSProperties = {
  fontFamily: 'Cinzel, serif', fontSize: 11, padding: '4px 10px',
  borderRadius: 5, border: '1px solid', cursor: 'pointer', transition: 'all .15s ease',
}
