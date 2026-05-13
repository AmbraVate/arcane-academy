import { useEffect, useState, useCallback, useRef } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { encodingApi, codeApi, tailwindApi, reactApi, sqlApi, curiosityApi } from '@/shared/api/services'
import { useAuth } from '@/shared/hooks/useAuth'
import type { SubChunkEncoding, PracticeResult, RetrievalResultDto, FeynmanResultDto, AnswerEntry, Badge, CodeRunResponse } from '@/shared/types'
import StoryPanel from '@/features/learning/components/StoryPanel'
import QuestionCard from '@/features/learning/components/QuestionCard'
import CodeEditor from '@/features/learning/components/CodeEditor'
import TailwindEditor from '@/features/learning/components/TailwindEditor'
import ReactEditor, { type ReactEditorHandle, type ReactTestSpec } from '@/features/learning/components/ReactEditor'
import SqlEditor, { type SqlEditorHandle, type SqlTestSpec } from '@/features/learning/components/SqlEditor'
import OutputPanel from '@/features/learning/components/OutputPanel'
import TestChips from '@/features/learning/components/TestChips'
import AiMentorPanel from '@/features/learning/components/AiMentorPanel'
import LevelUpModal from '@/shared/components/layout/LevelUpModal'
import BadgeToast from '@/shared/components/layout/BadgeToast'
import { cn } from '@/lib/utils'

type OutputLine = { text: string; type: 'normal' | 'success' | 'error' | 'system' }

/** For SQL practice: pull the seed SQL out of the first test spec's `setup` field. */
function extractSqlSetup(specs: unknown): string | null {
  if (!Array.isArray(specs) || specs.length === 0) return null
  const first = specs[0] as { setup?: unknown }
  return typeof first?.setup === 'string' ? first.setup : null
}

export default function EncodingPage() {
  const { subChunkId } = useParams<{ subChunkId: string }>()
  const navigate = useNavigate()
  const { user, updateXp } = useAuth()

  const [encoding, setEncoding] = useState<SubChunkEncoding | null>(null)
  const [loading, setLoading] = useState(true)

  const [practiceView, setPracticeView] = useState<'brief' | 'code'>('brief')
  const [showTaskOverlay, setShowTaskOverlay] = useState(false)
  const [code, setCode] = useState('')
  const [output, setOutput] = useState<OutputLine[]>([{ text: '// Cast your spell to run the code.', type: 'system' }])
  const [testResults, setTestResults] = useState<Map<string, boolean>>(new Map())
  const [running, setRunning] = useState(false)
  const [mentorFeedback, setMentorFeedback] = useState<string | null>(null)
  const [mentorLoading, setMentorLoading] = useState(false)
  const [mentorErrorType, setMentorErrorType] = useState<string | null>(null)
  const [practiceSolved, setPracticeSolved] = useState(false)

  const [answers, setAnswers] = useState<Record<string, string>>({})
  const [retrievalResult, setRetrievalResult] = useState<RetrievalResultDto | null>(null)
  const [submittingRetrieval, setSubmittingRetrieval] = useState(false)

  const [feynmanText, setFeynmanText] = useState('')
  const [feynmanResult, setFeynmanResult] = useState<FeynmanResultDto | null>(null)
  const [submittingFeynman, setSubmittingFeynman] = useState(false)

  const [showHint, setShowHint] = useState(false)

  const [isSaved, setIsSaved] = useState(false)
  const [savingPin, setSavingPin] = useState(false)

  const [toast, setToast] = useState<string | null>(null)
  const [levelUpInfo, setLevelUpInfo] = useState<{ level: number; rank: string } | null>(null)
  const [newBadges, setNewBadges] = useState<Badge[]>([])
  const toastTimer = useRef<ReturnType<typeof setTimeout>>()
  const reactEditorRef = useRef<ReactEditorHandle>(null)
  const sqlEditorRef = useRef<SqlEditorHandle>(null)

  useEffect(() => {
    if (!subChunkId) return
    encodingApi.start(subChunkId)
      .then(async enc => {
        // If the sub-chunk has no hook content, skip straight to EXPLANATION
        if (enc.phase === 'HOOK' && !enc.hookHtml?.trim()) {
          enc = await encodingApi.advance(subChunkId)
        }
        setEncoding(enc); if (enc.starterCode) setCode(enc.starterCode)
      })
      .catch(() => navigate('/'))
      .finally(() => setLoading(false))
    curiosityApi.getAll()
      .then(items => setIsSaved(items.some(i => i.subChunkId === subChunkId)))
      .catch(() => {})
  }, [subChunkId, navigate])

  const showToast = useCallback((msg: string) => {
    setToast(msg)
    clearTimeout(toastTimer.current)
    toastTimer.current = setTimeout(() => setToast(null), 2600)
  }, [])

  async function handleTogglePin() {
    if (!subChunkId || savingPin) return
    setSavingPin(true)
    try {
      if (isSaved) {
        await curiosityApi.remove(subChunkId); setIsSaved(false); showToast('Removed from Curiosity Queue')
      } else {
        await curiosityApi.save(subChunkId); setIsSaved(true); showToast('📌 Saved to Curiosity Queue')
      }
    } catch { showToast('Could not update queue') } finally { setSavingPin(false) }
  }

  async function handleAdvance() {
    if (!subChunkId) return
    const enc = await encodingApi.advance(subChunkId)
    setEncoding(enc)
    setCode(enc.starterCode ?? '')
    setAnswers({}); setRetrievalResult(null); setFeynmanResult(null); setFeynmanText('')
    setPracticeSolved(false); setPracticeView('brief'); setShowTaskOverlay(false)
    setOutput([{ text: '// Cast your spell to run the code.', type: 'system' }])
    setTestResults(new Map()); setMentorFeedback(null); setShowHint(false)
  }

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

  async function handleSubmitPractice() {
    if (!subChunkId || running) return
    const written = encoding?.practiceType === 'NONE'
    setRunning(true); setMentorFeedback(null)
    setOutput([{ text: written ? '// Checking your written response...' : '// Running all test cases...', type: 'system' }]); setTestResults(new Map())
    try {
      const result: PracticeResult = await encodingApi.submitPractice(subChunkId, code)
      if (result.errorType === 'COMPILE_ERROR' || result.errorType === 'RUNTIME_ERROR') {
        setOutput([{ text: `✗ ${result.errorType === 'COMPILE_ERROR' ? 'Spell failed to compile' : 'Spell crashed at runtime'}.`, type: 'error' }])
        setMentorErrorType(result.errorType)
        if (result.mentorFeedback) { setMentorLoading(true); setTimeout(() => { setMentorFeedback(result.mentorFeedback); setMentorLoading(false) }, 300) }
        return
      }
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        if (written) {
          lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.actualOutput}`, type: t.passed ? 'success' : 'error' })
          return
        }
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.passed ? 'passed' : `got "${t.actualOutput}", expected "${t.expectedOutput}"`}`, type: t.passed ? 'success' : 'error' })
      })
      setTestResults(newResults)
      if (result.allPassed) {
        lines.push({ text: '✓ All test cases passed!', type: 'success' })
        setPracticeSolved(true); setShowTaskOverlay(false)
        if (result.xpEarned > 0) {
          const prevRank = calculateRank(user?.totalXp ?? 0)
          const newRank = calculateRank((user?.totalXp ?? 0) + result.xpEarned)
          updateXp(result.xpEarned, newRank); showToast(`✦ +${result.xpEarned} XP`)
          if (newRank !== prevRank) {
            const rankNames = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage', 'Magus', 'Lord Magus']
            setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(newRank) + 1, rank: newRank }), 1200)
          }
          if (result.newBadges?.length) setNewBadges(result.newBadges)
        }
      } else {
        lines.push({ text: '✗ Some test cases failed.', type: 'error' })
        if (result.mentorFeedback) { setMentorLoading(true); setTimeout(() => { setMentorFeedback(result.mentorFeedback); setMentorLoading(false) }, 400) }
      }
      setOutput(lines)
    } catch { setOutput([{ text: written ? 'Error submitting response.' : 'Error submitting code.', type: 'error' }]) }
    finally { setRunning(false) }
  }

  async function handleSubmitTailwind() {
    if (!subChunkId || running) return
    setRunning(true); setMentorFeedback(null)
    setOutput([{ text: '// Checking your Tailwind classes…', type: 'system' }]); setTestResults(new Map())
    try {
      const result: PracticeResult = await tailwindApi.submit(subChunkId, code)
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.passed ? 'passed' : `missing class — ${t.actualOutput}`}`, type: t.passed ? 'success' : 'error' })
      })
      setTestResults(newResults)
      if (result.allPassed) {
        lines.push({ text: '✓ All checks passed!', type: 'success' })
        setPracticeSolved(true); setShowTaskOverlay(false)
        if (result.xpEarned > 0) {
          const prevRank = calculateRank(user?.totalXp ?? 0)
          const newRank = calculateRank((user?.totalXp ?? 0) + result.xpEarned)
          updateXp(result.xpEarned, newRank); showToast(`✦ +${result.xpEarned} XP`)
          if (newRank !== prevRank) {
            const rankNames = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage', 'Magus', 'Lord Magus']
            setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(newRank) + 1, rank: newRank }), 1200)
          }
          if (result.newBadges?.length) setNewBadges(result.newBadges)
        }
      } else { lines.push({ text: '✗ Some checks failed — adjust your classes and try again.', type: 'error' }) }
      setOutput(lines)
    } catch { setOutput([{ text: 'Error submitting — check your connection.', type: 'error' }]) }
    finally { setRunning(false) }
  }

  // ── React (JSX) practice ───────────────────────────────────────────────────
  // Tests run in an iframe sandbox via ReactEditor.runTests(); per-test pass/fail
  // is then forwarded to the backend along with the source code. See
  // ReactPracticeService for the trust domain rationale.

  async function handleSubmitReact(solo: boolean) {
    if (!subChunkId || !encoding || running) return
    setRunning(true); setMentorFeedback(null)
    setOutput([{ text: '// Rendering and running tests in the sandbox…', type: 'system' }])
    setTestResults(new Map())
    try {
      const specs: ReactTestSpec[] = Array.isArray(encoding.testCaseLabels)
        ? (encoding.testCaseLabels as ReactTestSpec[])
        : []
      const clientResults = (await reactEditorRef.current?.runTests(specs)) ?? []
      const submitFn = solo ? reactApi.submitSoloPractice : reactApi.submit
      const result: PracticeResult = await submitFn(subChunkId, code, clientResults)
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({
          text: `${t.passed ? '✓' : '✗'} ${t.label}${t.passed ? '' : ` — ${t.actualOutput}`}`,
          type: t.passed ? 'success' : 'error',
        })
      })
      setTestResults(newResults)
      if (result.allPassed) {
        lines.push({ text: '✓ All tests passed!', type: 'success' })
        setPracticeSolved(true); setShowTaskOverlay(false)
        if (result.xpEarned > 0) {
          const prevRank = calculateRank(user?.totalXp ?? 0)
          const newRank = calculateRank((user?.totalXp ?? 0) + result.xpEarned)
          updateXp(result.xpEarned, newRank); showToast(`✦ +${result.xpEarned} XP`)
          if (newRank !== prevRank) {
            const rankNames = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage', 'Magus', 'Lord Magus']
            setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(newRank) + 1, rank: newRank }), 1200)
          }
          if (result.newBadges?.length) setNewBadges(result.newBadges)
        }
      } else {
        lines.push({ text: '✗ Some tests failed — adjust your code and try again.', type: 'error' })
      }
      setOutput(lines)
    } catch {
      setOutput([{ text: 'Error submitting — check your connection.', type: 'error' }])
    } finally {
      setRunning(false)
    }
  }

  // ── SQL submission — same client-trusted pattern as React (see SqlPracticeService).
  async function handleSubmitSql(solo: boolean) {
    if (!subChunkId || !encoding || running) return
    setRunning(true); setMentorFeedback(null)
    setOutput([{ text: '// Running query and tests in the SQLite sandbox…', type: 'system' }])
    setTestResults(new Map())
    try {
      const specs: SqlTestSpec[] = Array.isArray(encoding.testCaseLabels)
        ? (encoding.testCaseLabels as SqlTestSpec[])
        : []
      const clientResults = (await sqlEditorRef.current?.runTests(specs)) ?? []
      const submitFn = solo ? sqlApi.submitSoloPractice : sqlApi.submit
      const result: PracticeResult = await submitFn(subChunkId, code, clientResults)
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({
          text: `${t.passed ? '✓' : '✗'} ${t.label}${t.passed ? '' : ` — ${t.actualOutput}`}`,
          type: t.passed ? 'success' : 'error',
        })
      })
      setTestResults(newResults)
      if (result.allPassed) {
        lines.push({ text: '✓ All tests passed!', type: 'success' })
        setPracticeSolved(true); setShowTaskOverlay(false)
        if (result.xpEarned > 0) {
          const prevRank = calculateRank(user?.totalXp ?? 0)
          const newRank = calculateRank((user?.totalXp ?? 0) + result.xpEarned)
          updateXp(result.xpEarned, newRank); showToast(`✦ +${result.xpEarned} XP`)
          if (newRank !== prevRank) {
            const rankNames = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage', 'Magus', 'Lord Magus']
            setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(newRank) + 1, rank: newRank }), 1200)
          }
          if (result.newBadges?.length) setNewBadges(result.newBadges)
        }
      } else {
        lines.push({ text: '✗ Some tests failed — adjust your code and try again.', type: 'error' })
      }
      setOutput(lines)
    } catch {
      setOutput([{ text: 'Error submitting — check your connection.', type: 'error' }])
    } finally {
      setRunning(false)
    }
  }

  async function handleSubmitSoloPractice() {
    if (!subChunkId || running) return
    const written = encoding?.practiceType === 'NONE'
    setRunning(true); setMentorFeedback(null)
    setOutput([{ text: written ? '// Checking your independent response...' : '// Running all test cases...', type: 'system' }]); setTestResults(new Map())
    try {
      const result: PracticeResult = await encodingApi.submitSoloPractice(subChunkId, code)
      if (result.errorType === 'COMPILE_ERROR' || result.errorType === 'RUNTIME_ERROR') {
        setOutput([{ text: `✗ ${result.errorType === 'COMPILE_ERROR' ? 'Spell failed to compile' : 'Spell crashed at runtime'}.`, type: 'error' }])
        setMentorErrorType(result.errorType)
        if (result.mentorFeedback) { setMentorLoading(true); setTimeout(() => { setMentorFeedback(result.mentorFeedback); setMentorLoading(false) }, 300) }
        return
      }
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.passed ? 'passed' : `got "${t.actualOutput}", expected "${t.expectedOutput}"`}`, type: t.passed ? 'success' : 'error' })
      })
      setTestResults(newResults)
      if (result.allPassed) {
        lines.push({ text: '✓ All test cases passed! You built it from scratch!', type: 'success' })
        setPracticeSolved(true); setShowTaskOverlay(false)
      } else {
        lines.push({ text: '✗ Some test cases failed — keep going!', type: 'error' })
        if (result.mentorFeedback) { setMentorLoading(true); setTimeout(() => { setMentorFeedback(result.mentorFeedback); setMentorLoading(false) }, 400) }
      }
      setOutput(lines)
    } catch { setOutput([{ text: 'Error submitting code.', type: 'error' }]) }
    finally { setRunning(false) }
  }

  async function handleSubmitTailwindSolo() {
    if (!subChunkId || running) return
    setRunning(true); setMentorFeedback(null)
    setOutput([{ text: '// Checking your Tailwind classes…', type: 'system' }]); setTestResults(new Map())
    try {
      const result: PracticeResult = await tailwindApi.submitSoloPractice(subChunkId, code)
      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({ text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.passed ? 'passed' : `missing class — ${t.actualOutput}`}`, type: t.passed ? 'success' : 'error' })
      })
      setTestResults(newResults)
      if (result.allPassed) {
        lines.push({ text: '✓ All checks passed! Well done!', type: 'success' })
        setPracticeSolved(true); setShowTaskOverlay(false)
      } else { lines.push({ text: '✗ Some checks failed — adjust your classes and try again.', type: 'error' }) }
      setOutput(lines)
    } catch { setOutput([{ text: 'Error submitting — check your connection.', type: 'error' }]) }
    finally { setRunning(false) }
  }

  async function handleSubmitRetrieval() {
    if (!subChunkId) return
    setSubmittingRetrieval(true)
    try {
      const answerList: AnswerEntry[] = Object.entries(answers).map(([questionId, answer]) => ({ questionId, answer }))
      const result = await encodingApi.submitRetrieval(subChunkId, answerList)
      setRetrievalResult(result)
      if (result.xpEarned > 0) {
        const prevRank = calculateRank(user?.totalXp ?? 0)
        const newRank = calculateRank((user?.totalXp ?? 0) + result.xpEarned)
        updateXp(result.xpEarned, newRank)
        showToast(`✦ +${result.xpEarned} XP — ${result.passed ? 'Passed!' : 'Keep practicing'}`)
        if (newRank !== prevRank) {
          const rankNames = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage', 'Magus', 'Lord Magus']
          setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(newRank) + 1, rank: newRank }), 1200)
        }
      }
      if (result.newBadges?.length) setNewBadges(result.newBadges)
    } catch { showToast('Error submitting answers') }
    finally { setSubmittingRetrieval(false) }
  }

  async function handleSubmitFeynman() {
    if (!subChunkId || !feynmanText.trim()) return
    setSubmittingFeynman(true)
    try {
      const result = await encodingApi.submitFeynman(subChunkId, feynmanText)
      setFeynmanResult(result)
      if (result.xpEarned > 0) { updateXp(result.xpEarned); showToast(`✦ +${result.xpEarned} XP — Feynman complete`) }
    } catch { showToast('Error submitting explanation') }
    finally { setSubmittingFeynman(false) }
  }

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] text-muted">
        <div className="text-[48px] mb-3 animate-pulse">🔮</div>
        <p>Opening the Grimoire...</p>
      </div>
    )
  }
  if (!encoding) return null

  const phase = encoding.phase

  // Shared prose HTML classes
  const proseHtml = `text-[14px] leading-[1.75] text-text
    [&_p]:mb-2.5 [&_p:last-child]:mb-0
    [&_code]:bg-surface [&_code]:px-1 [&_code]:py-px [&_code]:rounded [&_code]:text-[12px] [&_code]:text-purple-light [&_code]:border [&_code]:border-border
    [&_pre]:bg-[#09070f] [&_pre]:border [&_pre]:border-[rgba(139,92,246,0.2)] [&_pre]:rounded-[8px] [&_pre]:overflow-x-auto [&_pre]:my-2.5 [&_pre]:mb-3.5
    [&_pre_code]:bg-transparent [&_pre_code]:border-none [&_pre_code]:p-0 [&_pre_code]:text-[13px] [&_pre_code]:leading-[1.7] [&_pre_code]:text-[#e2e8f0] [&_pre_code]:block [&_pre_code]:px-4 [&_pre_code]:py-4 [&_pre_code]:font-mono
    [&_strong]:text-gold [&_strong]:font-semibold [&_em]:text-purple-light [&_em]:italic`

  return (
    <div className="flex flex-col flex-1 overflow-hidden min-h-0">
      {/* Header */}
      <div className="flex items-center gap-3 px-4 py-2.5 border-b border-border bg-card flex-shrink-0 max-[480px]:px-2.5 max-[480px]:py-2 max-[480px]:gap-2">
        <button className="btn btn-ghost text-[12px]" onClick={() => navigate(`/chunk/${encoding.chunkId}`)}>← Back</button>
        <div className="flex-1 min-w-0">
          <div className="text-[16px] font-bold text-text truncate max-[480px]:text-[13px]">{encoding.title}</div>
          <div className="flex gap-1.5 mt-1 overflow-x-auto flex-nowrap scrollbar-none max-[480px]:gap-1">
            {(['HOOK', 'EXPLANATION', 'GUIDED_PRACTICE', 'SOLO_PRACTICE', 'RETRIEVAL_CHECK', 'COMPLETE'] as const).map(p => (
              <span key={p} className={cn(
                'text-[10px] px-2 py-0.5 rounded-[10px] bg-surface text-muted whitespace-nowrap flex-shrink-0',
                p === phase && 'bg-purple text-white',
                (phase === 'COMPLETE' || phaseOrder(p) < phaseOrder(phase)) && p !== phase && 'bg-teal-dim text-teal',
              )}>
                {phaseLabel(p)}
              </span>
            ))}
          </div>
        </div>
        <button
          className={cn('btn btn-ghost text-[11px] px-2.5 py-1 flex-shrink-0 opacity-70 hover:opacity-100', isSaved && 'text-gold opacity-100')}
          onClick={handleTogglePin} disabled={savingPin}
          title={isSaved ? 'Remove from Curiosity Queue' : 'Save for later'}
        >
          {isSaved ? '📌' : '🔖'} {isSaved ? 'Saved' : 'Save'}
        </button>
      </div>

      {/* HOOK */}
      {phase === 'HOOK' && (
        <div className="flex-1 flex flex-col items-center justify-center px-6 py-10 text-center overflow-y-auto animate-[fade-up_0.5s_cubic-bezier(0.22,1,0.36,1)_both] max-[480px]:px-3 max-[480px]:py-5">
          <div
            className="relative max-w-[640px] w-full px-14 py-12 rounded-[20px] overflow-hidden
              max-[480px]:px-[18px] max-[480px]:py-7 max-[480px]:rounded-[12px]"
            style={{
              background: 'radial-gradient(ellipse at 50% 0%, rgba(139,92,246,.18) 0%, transparent 65%), var(--card)',
              border: '1px solid rgba(139,92,246,.3)',
              boxShadow: '0 0 80px rgba(139,92,246,.14), 0 16px 48px rgba(0,0,0,.45)',
            }}
          >
            {/* Top & bottom gradient lines */}
            <div className="absolute top-0 left-0 right-0 h-0.5" style={{ background: 'linear-gradient(90deg, transparent 0%, var(--purple) 25%, var(--teal) 75%, transparent 100%)' }} />
            <div className="absolute bottom-0 left-0 right-0 h-0.5" style={{ background: 'linear-gradient(90deg, transparent 0%, var(--teal) 25%, var(--purple) 75%, transparent 100%)' }} />
            {/* Chapter eyebrow */}
            <div className="text-[10px] font-semibold tracking-[0.22em] uppercase text-muted mb-3 max-[480px]:text-[9px]">
              ✦ Arcane Academy
            </div>
            {/* Lesson title — the hero element */}
            <h1
              className="font-cinzel text-[22px] font-bold leading-[1.35] mb-2 max-[480px]:text-[17px]"
              style={{ color: '#c9a227', textShadow: '0 0 28px rgba(201,162,39,.35)' }}
            >
              {encoding.title}
            </h1>
            {/* Gradient rule */}
            <div className="w-24 h-px mx-auto mb-7 max-[480px]:mb-5" style={{ background: 'linear-gradient(90deg, transparent, var(--purple) 30%, var(--teal) 70%, transparent)' }} />
            {/* Hook prose */}
            <div
              className="text-[18px] leading-[1.85] text-text max-[480px]:text-[15px] max-[480px]:leading-[1.75]
                [&_p]:m-0 [&_p]:mb-4 [&_p:last-child]:mb-0
                [&_strong]:text-gold [&_strong]:font-bold
                [&_em]:text-purple-light [&_em]:italic"
              dangerouslySetInnerHTML={{ __html: encoding.hookHtml ?? '' }}
            />
          </div>
          <button className="btn btn-primary mt-9 px-8 py-2.5 text-[14px]" onClick={handleAdvance}>Begin →</button>
        </div>
      )}

      {/* EXPLANATION */}
      {phase === 'EXPLANATION' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          {encoding.storyBeats && <StoryPanel beats={encoding.storyBeats} fullPage />}
          {encoding.explanationHtml && (
            <div
              className={cn('text-[15px] leading-[1.8] text-text my-6',
                '[&_p]:m-0 [&_p]:mb-4 [&_p:last-child]:mb-0',
                '[&_strong]:text-gold [&_strong]:font-semibold [&_em]:text-purple-light [&_em]:italic',
                '[&_h3]:text-[17px] [&_h3]:font-bold [&_h3]:text-gold [&_h3]:mt-7 [&_h3]:mb-2.5 [&_h3]:pb-1.5 [&_h3]:border-b [&_h3]:border-[rgba(255,193,7,0.15)]',
                '[&_h4]:text-[14px] [&_h4]:font-bold [&_h4]:text-purple-light [&_h4]:mt-5 [&_h4]:mb-2',
                '[&_ul]:pl-5 [&_ul]:mb-4 [&_ol]:pl-5 [&_ol]:mb-4',
                '[&_li]:mb-2 [&_li]:leading-[1.65] [&_li::marker]:text-purple-light',
                '[&_code]:bg-[rgba(139,92,246,0.12)] [&_code]:border [&_code]:border-[rgba(139,92,246,0.2)] [&_code]:rounded [&_code]:px-1.5 [&_code]:py-px [&_code]:text-[12.5px] [&_code]:text-purple-light [&_code]:font-mono',
                '[&_pre]:relative [&_pre]:bg-[#09070f] [&_pre]:border [&_pre]:border-[rgba(139,92,246,0.2)] [&_pre]:rounded-[10px] [&_pre]:overflow-hidden [&_pre]:my-5 [&_pre]:mb-6 [&_pre]:shadow-[0_4px_20px_rgba(0,0,0,0.3)]',
                '[&_pre]:before:content-["●_●_●"] [&_pre]:before:block [&_pre]:before:px-4 [&_pre]:before:py-[9px] [&_pre]:before:text-[11px] [&_pre]:before:tracking-[4px] [&_pre]:before:text-[rgba(139,92,246,0.5)] [&_pre]:before:bg-[rgba(139,92,246,0.06)] [&_pre]:before:border-b [&_pre]:before:border-[rgba(139,92,246,0.12)]',
                '[&_pre_code]:block [&_pre_code]:px-5 [&_pre_code]:py-4 [&_pre_code]:text-[13px] [&_pre_code]:leading-[1.75] [&_pre_code]:text-[#e2e8f0] [&_pre_code]:font-mono [&_pre_code]:overflow-x-auto [&_pre_code]:bg-transparent [&_pre_code]:border-none',
                'max-[480px]:text-[14px] max-[480px]:[&_pre_code]:text-[12px]',
              )}
              dangerouslySetInnerHTML={{ __html: encoding.explanationHtml }}
            />
          )}
          <button className="btn btn-primary" onClick={handleAdvance}>I understand — continue →</button>
        </div>
      )}

      {/* GUIDED_PRACTICE — brief */}
      {phase === 'GUIDED_PRACTICE' && practiceView === 'brief' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-[13px] font-bold text-gold mb-2.5 tracking-[0.06em] uppercase">
            {encoding.practiceType === 'NONE' ? '📜 Study Material' : '✦ Guided Practice'}
          </div>
          <div className={proseHtml} dangerouslySetInnerHTML={{ __html: encoding.guidedPracticeHtml ?? '' }} />
          {encoding.practiceType === 'NONE' && encoding.starterCode && (
            <pre className="mt-5 p-4 rounded-[10px] bg-bg border border-border text-[12px] leading-[1.55] overflow-x-auto whitespace-pre">
              <code>{encoding.starterCode}</code>
            </pre>
          )}
          {encoding.testCaseLabels && encoding.practiceType !== 'NONE' && (
            <div className="mt-5"><TestChips labels={encoding.testCaseLabels} results={testResults} /></div>
          )}
          {encoding.practiceType === 'NONE' ? (
            <button className="btn btn-primary mt-6" onClick={() => setPracticeView('code')}>Start Writing →</button>
          ) : (
            <button className="btn btn-primary mt-6" onClick={() => setPracticeView('code')}>Start Coding →</button>
          )}
        </div>
      )}

      {/* GUIDED_PRACTICE — coding */}
      {phase === 'GUIDED_PRACTICE' && practiceView === 'code' && (
        <div className="flex flex-1 overflow-hidden min-h-0">
          {/* Mobile task overlay */}
          {showTaskOverlay && (
            <div className="fixed inset-0 bg-black/60 z-[100] hidden max-[768px]:flex items-end" onClick={() => setShowTaskOverlay(false)}>
              <div className="bg-card border-t border-border rounded-[16px_16px_0_0] px-4 py-5 pb-8 max-h-[70vh] overflow-y-auto w-full" onClick={e => e.stopPropagation()}>
                <div className="flex justify-between items-center mb-3.5">
                  <span className="text-[13px] font-bold text-gold uppercase tracking-[0.06em]">✦ Task</span>
                  <button className="btn btn-ghost text-[12px]" onClick={() => setShowTaskOverlay(false)}>✕</button>
                </div>
                <div className={proseHtml} dangerouslySetInnerHTML={{ __html: encoding.guidedPracticeHtml ?? '' }} />
                {encoding.testCaseLabels && <TestChips labels={encoding.testCaseLabels} results={testResults} />}
              </div>
            </div>
          )}

          {/* Left panel — desktop */}
          <div className="w-[38%] min-w-[260px] max-w-[380px] flex flex-col border-r border-border overflow-y-auto p-4 gap-3 flex-shrink-0 max-[768px]:hidden">
            <div className="text-[13px] font-bold text-gold uppercase tracking-[0.06em]">✦ Task</div>
            <div className={proseHtml} dangerouslySetInnerHTML={{ __html: encoding.guidedPracticeHtml ?? '' }} />
            {encoding.testCaseLabels && <TestChips labels={encoding.testCaseLabels} results={testResults} />}
            {practiceSolved && (
              <div className="p-3.5 bg-[rgba(0,200,83,0.08)] border border-teal rounded-[8px]">
                <div className="text-[14px] font-bold text-teal mb-2.5">✦ Practice Complete!</div>
                <button className="btn btn-primary" onClick={handleAdvance}>Continue →</button>
              </div>
            )}
          </div>

          {/* Right panel — editor */}
          <div className="flex-1 flex flex-col overflow-hidden min-w-0">
            <div className="flex justify-between items-center px-3 py-2 border-b border-border bg-card flex-shrink-0 gap-2 max-[480px]:px-2.5 max-[480px]:py-1.5">
              <div className="flex items-center gap-2.5 min-w-0">
                <span className="text-[12px] text-muted truncate">☽ {encoding.filename}</span>
                {/* Mobile task toggle */}
                <button
                  className="hidden max-[768px]:inline-flex items-center text-[11px] px-2.5 py-[3px] rounded-[10px] bg-purple-dim text-purple-light border border-[rgba(139,92,246,0.3)] cursor-pointer whitespace-nowrap flex-shrink-0"
                  onClick={() => setShowTaskOverlay(true)}
                >
                  📋 Task
                </button>
              </div>
              <div className="flex gap-2 flex-shrink-0">
                {encoding.practiceType === 'JAVA' && (
                  <button className={cn('btn btn-ghost text-[12px] px-3.5 py-[5px]', running && 'opacity-70')} onClick={handleRun} disabled={running}>
                    {running ? '⟳ Running…' : '▶ Run'}
                  </button>
                )}
                <button
                  className={cn(practiceSolved ? 'bg-teal text-bg border-none rounded-md cursor-default' : 'btn btn-primary', 'text-[12px] px-3.5 py-[5px]')}
                  onClick={
                    encoding.practiceType === 'TAILWIND' ? handleSubmitTailwind
                    : encoding.practiceType === 'REACT' ? () => handleSubmitReact(false)
                    : encoding.practiceType === 'SQL' ? () => handleSubmitSql(false)
                    : handleSubmitPractice
                  }
                  disabled={running || practiceSolved}
                >
                  {practiceSolved ? '✓ Solved' : '⚡ Submit'}
                </button>
              </div>
            </div>

            {encoding.practiceType === 'NONE' ? (
              <>
                <WrittenResponseEditor
                  value={code}
                  onChange={setCode}
                  disabled={practiceSolved}
                  placeholder="Write your guided response here. Follow the steps in the task panel, explain your reasoning, and use the lesson vocabulary."
                />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[768px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Guided Response Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
                <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
              </>
            ) : encoding.practiceType === 'TAILWIND' ? (
              <>
                <TailwindEditor value={code} onChange={setCode} disabled={practiceSolved} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[768px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Practice Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : encoding.practiceType === 'REACT' ? (
              <>
                <ReactEditor ref={reactEditorRef} value={code} onChange={setCode} disabled={practiceSolved} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[768px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Practice Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : encoding.practiceType === 'SQL' ? (
              <>
                <SqlEditor
                  ref={sqlEditorRef}
                  value={code}
                  onChange={setCode}
                  setup={extractSqlSetup(encoding.testCaseLabels)}
                  disabled={practiceSolved}
                />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[768px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Practice Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : (
              <>
                <CodeEditor value={code} onChange={setCode} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[768px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Practice Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
                <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
              </>
            )}
          </div>
        </div>
      )}

      {/* SOLO_PRACTICE — brief */}
      {phase === 'SOLO_PRACTICE' && practiceView === 'brief' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-[13px] font-bold mb-1 tracking-[0.06em] uppercase" style={{ color: 'var(--teal)' }}>🏹 Solo Challenge</div>
          <p className="text-muted text-[12px] mb-4 leading-[1.6]">
            Now rebuild this from a blank slate — no starter code. If you get stuck, peek at the guided practice for a hint.
          </p>
          <div className={proseHtml} dangerouslySetInnerHTML={{ __html: encoding.soloPracticeHtml ?? '' }} />
          {encoding.testCaseLabels && <div className="mt-5"><TestChips labels={encoding.testCaseLabels} results={testResults} /></div>}

          {/* Hint toggle */}
          <div className="mt-5">
            <button
              className="btn btn-ghost text-[12px] px-3 py-1.5"
              onClick={() => setShowHint(h => !h)}
            >
              {showHint ? '▲ Hide hint' : '👁 Peek at Guided Practice'}
            </button>
            {showHint && (
              <div className="mt-3 p-4 rounded-[10px] border border-dashed border-[rgba(139,92,246,0.35)] bg-[rgba(139,92,246,0.05)]">
                <div className="text-[11px] font-semibold text-muted uppercase tracking-[0.1em] mb-2.5">Guided Practice reference</div>
                <div className={proseHtml} dangerouslySetInnerHTML={{ __html: encoding.guidedPracticeHtml ?? '' }} />
              </div>
            )}
          </div>

          <button className="btn btn-primary mt-6" onClick={() => setPracticeView('code')}>Start Coding →</button>
        </div>
      )}

      {/* SOLO_PRACTICE — coding */}
      {phase === 'SOLO_PRACTICE' && practiceView === 'code' && (
        <div className="flex flex-1 overflow-hidden min-h-0">
          {/* Mobile task overlay */}
          {showTaskOverlay && (
            <div className="fixed inset-0 bg-black/60 z-[100] hidden max-[768px]:flex items-end" onClick={() => setShowTaskOverlay(false)}>
              <div className="bg-card border-t border-border rounded-[16px_16px_0_0] px-4 py-5 pb-8 max-h-[70vh] overflow-y-auto w-full" onClick={e => e.stopPropagation()}>
                <div className="flex justify-between items-center mb-3.5">
                  <span className="text-[13px] font-bold uppercase tracking-[0.06em]" style={{ color: 'var(--teal)' }}>🏹 Solo Challenge</span>
                  <button className="btn btn-ghost text-[12px]" onClick={() => setShowTaskOverlay(false)}>✕</button>
                </div>
                <div className={proseHtml} dangerouslySetInnerHTML={{ __html: encoding.soloPracticeHtml ?? '' }} />
                {encoding.testCaseLabels && <TestChips labels={encoding.testCaseLabels} results={testResults} />}
              </div>
            </div>
          )}

          {/* Left panel — desktop */}
          <div className="w-[38%] min-w-[260px] max-w-[380px] flex flex-col border-r border-border overflow-y-auto p-4 gap-3 flex-shrink-0 max-[768px]:hidden">
            <div className="text-[13px] font-bold uppercase tracking-[0.06em]" style={{ color: 'var(--teal)' }}>🏹 Solo Challenge</div>
            <p className="text-muted text-[11px] leading-[1.6] mt-[-4px]">No starter code — build it from memory.</p>
            <div className={proseHtml} dangerouslySetInnerHTML={{ __html: encoding.soloPracticeHtml ?? '' }} />
            {encoding.testCaseLabels && <TestChips labels={encoding.testCaseLabels} results={testResults} />}

            {/* Hint toggle */}
            <div className="border-t border-border pt-2.5 mt-1">
              <button className="btn btn-ghost text-[11px] px-2.5 py-1" onClick={() => setShowHint(h => !h)}>
                {showHint ? '▲ Hide hint' : '👁 Peek at Guided Practice'}
              </button>
              {showHint && (
                <div className="mt-2.5 p-3 rounded-[8px] border border-dashed border-[rgba(139,92,246,0.3)] bg-[rgba(139,92,246,0.05)]">
                  <div className="text-[10px] font-semibold text-muted uppercase tracking-[0.1em] mb-2">Guided reference</div>
                  <div className={cn(proseHtml, 'text-[12px]')} dangerouslySetInnerHTML={{ __html: encoding.guidedPracticeHtml ?? '' }} />
                </div>
              )}
            </div>

            {practiceSolved && (
              <div className="p-3.5 bg-[rgba(0,200,83,0.08)] border border-teal rounded-[8px]">
                <div className="text-[14px] font-bold text-teal mb-2.5">✦ Solo Challenge Complete!</div>
                <button className="btn btn-primary" onClick={handleAdvance}>Continue to Retrieval Check →</button>
              </div>
            )}
          </div>

          {/* Right panel — editor */}
          <div className="flex-1 flex flex-col overflow-hidden min-w-0">
            <div className="flex justify-between items-center px-3 py-2 border-b border-border bg-card flex-shrink-0 gap-2 max-[480px]:px-2.5 max-[480px]:py-1.5">
              <div className="flex items-center gap-2.5 min-w-0">
                <span className="text-[12px] text-muted truncate">☽ {encoding.filename}</span>
                <button
                  className="hidden max-[768px]:inline-flex items-center text-[11px] px-2.5 py-[3px] rounded-[10px] bg-purple-dim text-purple-light border border-[rgba(139,92,246,0.3)] cursor-pointer whitespace-nowrap flex-shrink-0"
                  onClick={() => setShowTaskOverlay(true)}
                >
                  📋 Task
                </button>
              </div>
              <div className="flex gap-2 flex-shrink-0">
                {encoding.practiceType === 'JAVA' && (
                  <button className={cn('btn btn-ghost text-[12px] px-3.5 py-[5px]', running && 'opacity-70')} onClick={handleRun} disabled={running}>
                    {running ? '⟳ Running…' : '▶ Run'}
                  </button>
                )}
                <button
                  className={cn(practiceSolved ? 'bg-teal text-bg border-none rounded-md cursor-default' : 'btn btn-primary', 'text-[12px] px-3.5 py-[5px]')}
                  onClick={
                    encoding.practiceType === 'TAILWIND' ? handleSubmitTailwindSolo
                    : encoding.practiceType === 'REACT' ? () => handleSubmitReact(true)
                    : encoding.practiceType === 'SQL' ? () => handleSubmitSql(true)
                    : handleSubmitSoloPractice
                  }
                  disabled={running || practiceSolved}
                >
                  {practiceSolved ? '✓ Solved' : '⚡ Submit'}
                </button>
              </div>
            </div>

            {encoding.practiceType === 'NONE' ? (
              <>
                <WrittenResponseEditor
                  value={code}
                  onChange={setCode}
                  disabled={practiceSolved}
                  placeholder="Write your solo response here. Use your own example or case, and explain what evidence would support your answer."
                />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[768px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Response Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
                <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
              </>
            ) : encoding.practiceType === 'TAILWIND' ? (
              <>
                <TailwindEditor value={code} onChange={setCode} disabled={practiceSolved} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[768px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : encoding.practiceType === 'REACT' ? (
              <>
                <ReactEditor ref={reactEditorRef} value={code} onChange={setCode} disabled={practiceSolved} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[768px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : encoding.practiceType === 'SQL' ? (
              <>
                <SqlEditor
                  ref={sqlEditorRef}
                  value={code}
                  onChange={setCode}
                  setup={extractSqlSetup(encoding.testCaseLabels)}
                  disabled={practiceSolved}
                />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[768px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
              </>
            ) : (
              <>
                <CodeEditor value={code} onChange={setCode} />
                <OutputPanel lines={output} />
                {practiceSolved && (
                  <div className="hidden max-[768px]:flex items-center justify-between px-3.5 py-2.5 bg-[rgba(0,200,83,0.1)] border-t border-teal text-[13px] font-semibold text-teal flex-shrink-0">
                    <span>✦ Solo Complete!</span>
                    <button className="btn btn-primary text-[12px] px-4 py-[5px]" onClick={handleAdvance}>Continue →</button>
                  </div>
                )}
                <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
              </>
            )}
          </div>
        </div>
      )}

      {/* RETRIEVAL_CHECK */}
      {phase === 'RETRIEVAL_CHECK' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-[20px] font-bold text-gold mb-1.5">✦ Retrieval Check</div>
          <p className="text-muted text-[13px] mb-5">Answer these questions to test your understanding.</p>

          {!retrievalResult ? (
            <>
              {encoding.retrievalQuestions?.map((q, i) => (
                <QuestionCard key={q.id} question={q} index={i} answer={answers[q.id] ?? ''} onChange={v => setAnswers(prev => ({ ...prev, [q.id]: v }))} />
              ))}
              <button className="btn btn-primary mt-1" onClick={handleSubmitRetrieval} disabled={submittingRetrieval}>
                {submittingRetrieval ? 'Submitting...' : '⚡ Submit Answers'}
              </button>
            </>
          ) : (
            <div className="mt-2">
              <div className="text-[20px] font-bold text-text mb-1.5">
                Score: {Math.round(retrievalResult.score * 100)}% ({retrievalResult.correct}/{retrievalResult.total})
              </div>
              <div className={cn('text-[16px] font-semibold mb-3.5', retrievalResult.passed ? 'text-green' : 'text-red')}>
                {retrievalResult.passed ? '✓ Passed!' : '✗ Needs more practice'}
              </div>
              {encoding.retrievalQuestions?.map((q, i) => (
                <QuestionCard key={q.id} question={q} index={i}
                  answer={retrievalResult.results[i]?.userAnswer ?? ''} onChange={() => {}}
                  result={retrievalResult.results[i]} disabled />
              ))}
              <p className="text-muted text-[13px] italic mt-2.5">{retrievalResult.recommendation}</p>
              <button className="btn btn-primary mt-3" onClick={handleAdvance}>Continue →</button>
            </div>
          )}
        </div>
      )}

      {/* COMPLETE */}
      {phase === 'COMPLETE' && (
        <div className="max-w-[700px] mx-auto px-5 py-7 pb-[60px] overflow-y-auto flex-1 w-full box-border max-[480px]:px-3 max-[480px]:py-4">
          <div className="text-center">
            <div className="text-[48px] text-gold mb-3">✦</div>
            <h2 className="text-[24px] font-bold text-gold m-0 mb-2">Concept Mastered!</h2>
            <p className="text-muted text-[14px] mb-6">You've completed {encoding.title}. This concept will be reviewed via spaced repetition.</p>

            {encoding.feynmanPrompt && !feynmanResult && (
              <div className="text-left mt-6 p-[18px] bg-card border border-border rounded-[10px]">
                <div className="text-[16px] font-bold text-purple mb-1.5">🧪 Feynman Challenge (Optional)</div>
                <p className="text-[13px] text-muted mb-3 italic">{encoding.feynmanPrompt}</p>
                <textarea
                  className="w-full bg-surface border border-border rounded-md px-3 py-3 text-[14px] text-text font-crimson resize-y mb-2.5 box-border focus:outline-none focus:border-purple"
                  placeholder="Explain this concept in your own words..."
                  value={feynmanText} onChange={e => setFeynmanText(e.target.value)} rows={6}
                />
                <button className="btn btn-primary" onClick={handleSubmitFeynman} disabled={submittingFeynman || !feynmanText.trim()}>
                  {submittingFeynman ? 'Evaluating...' : '📝 Submit Explanation'}
                </button>
              </div>
            )}

            {feynmanResult && (
              <div className="text-left mt-4 p-[18px] bg-card border border-teal rounded-[10px]">
                <div className="text-[18px] font-bold text-teal mb-2">Feynman Score: {Math.round(feynmanResult.overallScore * 100)}%</div>
                <div className="flex gap-3 flex-wrap text-[12px] text-muted mb-2 max-[480px]:gap-2">
                  <span>Accuracy: {Math.round(feynmanResult.accuracy * 100)}%</span>
                  <span>Completeness: {Math.round(feynmanResult.completeness * 100)}%</span>
                  <span>Simplicity: {Math.round(feynmanResult.simplicity * 100)}%</span>
                  <span>Connection: {Math.round(feynmanResult.connection * 100)}%</span>
                </div>
                <p className="text-[13px] text-text leading-[1.5]">{feynmanResult.feedback}</p>
              </div>
            )}

            <div className="flex gap-2.5 justify-center mt-5 max-[480px]:flex-col max-[480px]:items-center">
              <button className="btn btn-success" onClick={() => navigate(`/chunk/${encoding.chunkId}`)}>Return to Chunk →</button>
              <button className="btn btn-ghost" onClick={() => navigate(`/topic/${encoding.topicId ?? 'java'}`)}>
                Dashboard
              </button>
            </div>
          </div>
        </div>
      )}

      {toast && (
        <div className="toast fixed bottom-6 left-1/2 -translate-x-1/2 z-[200] bg-card border border-gold rounded-lg px-4 py-2.5 text-[13px] text-gold font-cinzel shadow-[0_4px_16px_rgba(0,0,0,0.4)] pointer-events-none animate-[toast-in_0.3s_ease]">
          {toast}
        </div>
      )}
      {levelUpInfo && <LevelUpModal newLevel={levelUpInfo.level} newRank={levelUpInfo.rank} onClose={() => setLevelUpInfo(null)} />}
      {newBadges.length > 0 && <BadgeToast badges={newBadges} onDone={() => setNewBadges([])} />}
    </div>
  )
}

function phaseOrder(p: string): number {
  return ['HOOK', 'EXPLANATION', 'GUIDED_PRACTICE', 'SOLO_PRACTICE', 'RETRIEVAL_CHECK', 'COMPLETE'].indexOf(p)
}
function phaseLabel(p: string): string {
  return ({ HOOK: 'Hook', EXPLANATION: 'Learn', GUIDED_PRACTICE: 'Practice', SOLO_PRACTICE: 'Solo', RETRIEVAL_CHECK: 'Check', COMPLETE: 'Done' })[p] ?? p
}
function WrittenResponseEditor({
  value,
  onChange,
  disabled,
  placeholder,
}: {
  value: string
  onChange: (value: string) => void
  disabled?: boolean
  placeholder: string
}) {
  const words = value.trim() ? value.trim().split(/\s+/).length : 0

  return (
    <div className="flex-1 flex flex-col min-h-0 bg-bg">
      <div className="flex items-center justify-between px-3 py-2 border-b border-border bg-card text-[12px] text-muted">
        <span>Written response</span>
        <span>{words} words</span>
      </div>
      <textarea
        className="flex-1 w-full resize-none bg-bg text-text font-crimson text-[15px] leading-[1.7] p-4 border-0 outline-none box-border disabled:opacity-70"
        value={value}
        onChange={e => onChange(e.target.value)}
        disabled={disabled}
        placeholder={placeholder}
      />
    </div>
  )
}
function calculateRank(xp: number): string {
  if (xp >= 11000) return 'Lord Magus'
  if (xp >= 8000) return 'Magus'
  if (xp >= 6500) return 'Archmage'
  if (xp >= 4000) return 'Mage'
  if (xp >= 2000) return 'Adept'
  if (xp >= 800) return 'Apprentice'
  return 'Novice'
}
