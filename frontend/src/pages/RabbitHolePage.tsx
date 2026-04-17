import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { rabbitHoleApi, codeApi } from '../api/services'
import { useAuth } from '../hooks/useAuth'
import type { RabbitHoleModule, PracticeResult, CodeRunResponse, StoryBeat } from '../types'
import StoryPanel from '../components/quest/StoryPanel'
import CodeEditor from '../components/quest/CodeEditor'
import OutputPanel from '../components/quest/OutputPanel'
import AiMentorPanel from '../components/quest/AiMentorPanel'
import styles from './RabbitHolePage.module.css'

type OutputLine = { text: string; type: 'normal' | 'success' | 'error' | 'system' }

export default function RabbitHolePage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { updateXp } = useAuth()

  const [mod, setMod] = useState<RabbitHoleModule | null>(null)
  const [loading, setLoading] = useState(true)
  const [code, setCode] = useState('')
  const [output, setOutput] = useState<OutputLine[]>([{ text: '// Explore the rabbit hole...', type: 'system' }])
  const [running, setRunning] = useState(false)
  const [mentorFeedback, setMentorFeedback] = useState<string | null>(null)
  const [mentorLoading, setMentorLoading] = useState(false)
  const [mentorErrorType, setMentorErrorType] = useState<string | null>(null)
  const [solved, setSolved] = useState(false)

  useEffect(() => {
    if (!id) return
    rabbitHoleApi.getModule(id)
      .then(m => { setMod(m); setCode(m.starterCode) })
      .catch(() => navigate('/'))
      .finally(() => setLoading(false))
  }, [id, navigate])

  async function handleRun() {
    if (running) return
    setRunning(true)
    setMentorFeedback(null)
    setOutput([{ text: '// Running...', type: 'system' }])
    try {
      const result: CodeRunResponse = await codeApi.run(code)
      const lines: OutputLine[] = []
      if (result.status === 'SUCCESS' && result.output) {
        result.output.split('\n').forEach(l => lines.push({ text: l, type: 'normal' }))
      } else if (result.error) {
        result.error.split('\n').forEach(l => lines.push({ text: l, type: 'error' }))
      } else {
        lines.push({ text: '// No output produced.', type: 'system' })
      }
      setOutput(lines)
    } catch {
      setOutput([{ text: 'Error connecting to server.', type: 'error' }])
    } finally {
      setRunning(false)
    }
  }

  async function handleSubmit() {
    if (!id || running) return
    setRunning(true)
    setMentorFeedback(null)
    setOutput([{ text: '// Running tests...', type: 'system' }])
    try {
      const result: PracticeResult = await rabbitHoleApi.submit(id, code)
      if (result.errorType) {
        setOutput([{ text: `✗ ${result.errorType}`, type: 'error' }])
        setMentorErrorType(result.errorType)
        if (result.mentorFeedback) {
          setMentorLoading(true)
          setTimeout(() => { setMentorFeedback(result.mentorFeedback); setMentorLoading(false) }, 300)
        }
        return
      }
      const lines: OutputLine[] = result.testResults.map(t => ({
        text: `${t.passed ? '✓' : '✗'} ${t.label}`,
        type: t.passed ? 'success' as const : 'error' as const,
      }))
      if (result.allPassed) {
        lines.push({ text: '✓ All tests passed!', type: 'success' })
        setSolved(true)
        if (result.xpEarned > 0) updateXp(result.xpEarned)
      }
      setOutput(lines)
    } catch {
      setOutput([{ text: 'Error submitting.', type: 'error' }])
    } finally {
      setRunning(false)
    }
  }

  if (loading) return <div className={styles.loading}><p>Loading module...</p></div>
  if (!mod) return null

  const storyBeats: StoryBeat[] = mod.storyBeats ?? []

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <button className="btn btn-ghost" onClick={() => navigate(-1)} style={{ fontSize: 12 }}>← Back</button>
        <div className={styles.title}>🐇 {mod.title}</div>
      </div>

      <div className={styles.content}>
        <div className={styles.left}>
          {storyBeats.length > 0 && <StoryPanel beats={storyBeats} />}
          <div className={styles.contentHtml} dangerouslySetInnerHTML={{ __html: mod.contentHtml }} />
          {solved && (
            <div className={styles.solvedMsg}>
              ✦ Module complete! Return to explore more.
              <button className="btn btn-ghost" onClick={() => navigate('/')} style={{ marginTop: 8 }}>Dashboard</button>
            </div>
          )}
        </div>
        <div className={styles.right}>
          <div className={styles.editorHeader}>
            <span className={styles.filename}>☽ {mod.filename}</span>
            <div className={styles.editorActions}>
              <button className="btn btn-ghost" onClick={handleRun} disabled={running} style={{ fontSize: 12, padding: '5px 14px' }}>
                {running ? '⟳ Running…' : '▶ Run'}
              </button>
              <button className={solved ? styles.btnSolved : 'btn btn-primary'} onClick={handleSubmit} disabled={running || solved} style={{ fontSize: 12, padding: '5px 14px' }}>
                {solved ? '✓ Solved' : '⚡ Submit'}
              </button>
            </div>
          </div>
          <CodeEditor value={code} onChange={setCode} />
          <OutputPanel lines={output} />
          <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
        </div>
      </div>
    </div>
  )
}
