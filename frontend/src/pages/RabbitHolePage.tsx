import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { rabbitHoleApi, codeApi } from '../api/services'
import { useAuth } from '../hooks/useAuth'
import type { RabbitHoleModule, PracticeResult, CodeRunResponse, StoryBeat } from '../types'
import StoryPanel from '../components/quest/StoryPanel'
import CodeEditor from '../components/quest/CodeEditor'
import OutputPanel from '../components/quest/OutputPanel'
import AiMentorPanel from '../components/quest/AiMentorPanel'

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
    setRunning(true); setMentorFeedback(null)
    setOutput([{ text: '// Running...', type: 'system' }])
    try {
      const result: CodeRunResponse = await codeApi.run(code)
      const lines: OutputLine[] = []
      if (result.status === 'SUCCESS' && result.output)
        result.output.split('\n').forEach(l => lines.push({ text: l, type: 'normal' }))
      else if (result.error)
        result.error.split('\n').forEach(l => lines.push({ text: l, type: 'error' }))
      else lines.push({ text: '// No output produced.', type: 'system' })
      setOutput(lines)
    } catch { setOutput([{ text: 'Error connecting to server.', type: 'error' }]) }
    finally { setRunning(false) }
  }

  async function handleSubmit() {
    if (!id || running) return
    setRunning(true); setMentorFeedback(null)
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
    } catch { setOutput([{ text: 'Error submitting.', type: 'error' }]) }
    finally { setRunning(false) }
  }

  if (loading) return <div className="flex items-center justify-center h-[60vh] text-muted"><p>Loading module...</p></div>
  if (!mod) return null

  const storyBeats: StoryBeat[] = mod.storyBeats ?? []

  return (
    <div className="flex flex-col flex-1 overflow-hidden min-h-0">
      {/* Header */}
      <div className="flex items-center gap-3 px-4 py-2.5 border-b border-border bg-card max-[480px]:px-3 max-[480px]:py-2">
        <button className="btn btn-ghost text-[12px]" onClick={() => navigate(-1)}>← Back</button>
        <div className="text-[16px] font-bold text-gold max-[480px]:text-[14px]">🐇 {mod.title}</div>
      </div>

      {/* Split content */}
      <div className="flex flex-1 overflow-hidden max-[768px]:flex-col">
        {/* Left — story + description */}
        <div className="w-[40%] overflow-y-auto p-4 border-r border-border max-[768px]:w-full max-[768px]:border-r-0 max-[768px]:border-b max-[768px]:max-h-[40vh] max-[480px]:max-h-[35vh] max-[480px]:p-2.5">
          {storyBeats.length > 0 && <StoryPanel beats={storyBeats} />}
          <div
            className="text-[13px] leading-[1.7] text-text mt-3 [&_pre]:bg-surface [&_pre]:border [&_pre]:border-border [&_pre]:rounded-md [&_pre]:p-2.5 [&_pre]:overflow-x-auto"
            dangerouslySetInnerHTML={{ __html: mod.contentHtml }}
          />
          {solved && (
            <div className="mt-4 p-3.5 bg-[rgba(0,200,83,0.08)] border border-teal rounded-[8px] text-teal text-[14px]">
              ✦ Module complete! Return to explore more.
              <button className="btn btn-ghost mt-2 block text-[12px]" onClick={() => navigate('/')}>Dashboard</button>
            </div>
          )}
        </div>

        {/* Right — editor */}
        <div className="flex-1 flex flex-col overflow-hidden">
          <div className="flex justify-between items-center px-3 py-2 border-b border-border bg-card max-[480px]:px-2.5 max-[480px]:py-1.5">
            <span className="text-[12px] text-muted">☽ {mod.filename}</span>
            <div className="flex gap-2">
              <button className="btn btn-ghost" onClick={handleRun} disabled={running} style={{ fontSize: 12, padding: '5px 14px' }}>
                {running ? '⟳ Running…' : '▶ Run'}
              </button>
              <button
                className={solved ? 'bg-teal text-bg border-none rounded-md cursor-default text-[12px] px-3.5 py-[5px]' : 'btn btn-primary'}
                onClick={handleSubmit} disabled={running || solved}
                style={{ fontSize: 12, padding: '5px 14px' }}
              >
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
