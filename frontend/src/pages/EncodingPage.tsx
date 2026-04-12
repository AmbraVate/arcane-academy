import { useEffect, useState, useCallback, useRef } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { encodingApi, codeApi } from '../api/services'
import { useAuth } from '../hooks/useAuth'
import type { SubChunkEncoding, PracticeResult, RetrievalResultDto, FeynmanResultDto, AnswerEntry, Badge, CodeRunResponse } from '../types'
import StoryPanel from '../components/quest/StoryPanel'
import QuestionCard from '../components/quest/QuestionCard'
import CodeEditor from '../components/quest/CodeEditor'
import OutputPanel from '../components/quest/OutputPanel'
import TestChips from '../components/quest/TestChips'
import AiMentorPanel from '../components/quest/AiMentorPanel'
import LevelUpModal from '../components/layout/LevelUpModal'
import BadgeToast from '../components/layout/BadgeToast'
import styles from './EncodingPage.module.css'

type OutputLine = { text: string; type: 'normal' | 'success' | 'error' | 'system' }

export default function EncodingPage() {
  const { subChunkId } = useParams<{ subChunkId: string }>()
  const navigate = useNavigate()
  const { user, updateXp } = useAuth()

  const [encoding, setEncoding] = useState<SubChunkEncoding | null>(null)
  const [loading, setLoading] = useState(true)

  // Guided practice state
  const [code, setCode] = useState('')
  const [output, setOutput] = useState<OutputLine[]>([{ text: '// Cast your spell to run the code.', type: 'system' }])
  const [testResults, setTestResults] = useState<Map<string, boolean>>(new Map())
  const [running, setRunning] = useState(false)
  const [mentorFeedback, setMentorFeedback] = useState<string | null>(null)
  const [mentorLoading, setMentorLoading] = useState(false)
  const [mentorErrorType, setMentorErrorType] = useState<string | null>(null)
  const [practiceSolved, setPracticeSolved] = useState(false)

  // Retrieval check state
  const [answers, setAnswers] = useState<Record<string, string>>({})
  const [retrievalResult, setRetrievalResult] = useState<RetrievalResultDto | null>(null)
  const [submittingRetrieval, setSubmittingRetrieval] = useState(false)

  // Feynman state
  const [feynmanText, setFeynmanText] = useState('')
  const [feynmanResult, setFeynmanResult] = useState<FeynmanResultDto | null>(null)
  const [submittingFeynman, setSubmittingFeynman] = useState(false)

  // UI state
  const [toast, setToast] = useState<string | null>(null)
  const [levelUpInfo, setLevelUpInfo] = useState<{ level: number; rank: string } | null>(null)
  const [newBadges, setNewBadges] = useState<Badge[]>([])
  const toastTimer = useRef<ReturnType<typeof setTimeout>>()

  useEffect(() => {
    if (!subChunkId) return
    encodingApi.start(subChunkId)
      .then(enc => {
        setEncoding(enc)
        if (enc.starterCode) setCode(enc.starterCode)
      })
      .catch(() => navigate('/'))
      .finally(() => setLoading(false))
  }, [subChunkId, navigate])

  const showToast = useCallback((msg: string) => {
    setToast(msg)
    clearTimeout(toastTimer.current)
    toastTimer.current = setTimeout(() => setToast(null), 2600)
  }, [])

  async function handleAdvance() {
    if (!subChunkId) return
    const enc = await encodingApi.advance(subChunkId)
    setEncoding(enc)
    if (enc.starterCode) setCode(enc.starterCode)
    setAnswers({})
    setRetrievalResult(null)
    setFeynmanResult(null)
    setFeynmanText('')
    setPracticeSolved(false)
    setOutput([{ text: '// Cast your spell to run the code.', type: 'system' }])
    setTestResults(new Map())
    setMentorFeedback(null)
  }

  // ── Guided Practice ──────────────────────────────────────────────────────
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

  async function handleSubmitPractice() {
    if (!subChunkId || running) return
    setRunning(true)
    setMentorFeedback(null)
    setOutput([{ text: '// Running all test cases...', type: 'system' }])
    setTestResults(new Map())

    try {
      const result: PracticeResult = await encodingApi.submitPractice(subChunkId, code)

      if (result.errorType === 'COMPILE_ERROR' || result.errorType === 'RUNTIME_ERROR') {
        setOutput([{ text: `✗ ${result.errorType === 'COMPILE_ERROR' ? 'Spell failed to compile' : 'Spell crashed at runtime'}.`, type: 'error' }])
        setMentorErrorType(result.errorType)
        if (result.mentorFeedback) {
          setMentorLoading(true)
          setTimeout(() => { setMentorFeedback(result.mentorFeedback); setMentorLoading(false) }, 300)
        }
        return
      }

      const newResults = new Map<string, boolean>()
      const lines: OutputLine[] = []
      result.testResults.forEach(t => {
        newResults.set(t.label, t.passed)
        lines.push({
          text: `${t.passed ? '✓' : '✗'} ${t.label}: ${t.passed ? 'passed' : `got "${t.actualOutput}", expected "${t.expectedOutput}"`}`,
          type: t.passed ? 'success' : 'error',
        })
      })
      setTestResults(newResults)

      if (result.allPassed) {
        lines.push({ text: '✓ All test cases passed!', type: 'success' })
        setPracticeSolved(true)
        if (result.xpEarned > 0) {
          const prevRank = calculateRank(user?.totalXp ?? 0)
          const newTotalXp = (user?.totalXp ?? 0) + result.xpEarned
          const newRank = calculateRank(newTotalXp)
          updateXp(result.xpEarned, newRank)
          showToast(`✦ +${result.xpEarned} XP`)
          if (newRank !== prevRank) {
            const rankNames = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage', 'Magus', 'Lord Magus']
            setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(newRank) + 1, rank: newRank }), 1200)
          }
          if (result.newBadges && result.newBadges.length > 0) {
            setNewBadges(result.newBadges)
          }
        }
      } else {
        lines.push({ text: '✗ Some test cases failed.', type: 'error' })
        if (result.mentorFeedback) {
          setMentorLoading(true)
          setTimeout(() => { setMentorFeedback(result.mentorFeedback); setMentorLoading(false) }, 400)
        }
      }
      setOutput(lines)
    } catch {
      setOutput([{ text: 'Error submitting code.', type: 'error' }])
    } finally {
      setRunning(false)
    }
  }

  // ── Retrieval Check ──────────────────────────────────────────────────────
  async function handleSubmitRetrieval() {
    if (!subChunkId) return
    setSubmittingRetrieval(true)
    try {
      const answerList: AnswerEntry[] = Object.entries(answers).map(([questionId, answer]) => ({ questionId, answer }))
      const result = await encodingApi.submitRetrieval(subChunkId, answerList)
      setRetrievalResult(result)
      if (result.xpEarned > 0) {
        const prevRank = calculateRank(user?.totalXp ?? 0)
        const newTotalXp = (user?.totalXp ?? 0) + result.xpEarned
        const newRank = calculateRank(newTotalXp)
        updateXp(result.xpEarned, newRank)
        showToast(`✦ +${result.xpEarned} XP — ${result.passed ? 'Passed!' : 'Keep practicing'}`)
        if (newRank !== prevRank) {
          const rankNames = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage', 'Magus', 'Lord Magus']
          setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(newRank) + 1, rank: newRank }), 1200)
        }
      }
      if (result.newBadges && result.newBadges.length > 0) {
        setNewBadges(result.newBadges)
      }
    } catch {
      showToast('Error submitting answers')
    } finally {
      setSubmittingRetrieval(false)
    }
  }

  // ── Feynman ──────────────────────────────────────────────────────────────
  async function handleSubmitFeynman() {
    if (!subChunkId || !feynmanText.trim()) return
    setSubmittingFeynman(true)
    try {
      const result = await encodingApi.submitFeynman(subChunkId, feynmanText)
      setFeynmanResult(result)
      if (result.xpEarned > 0) {
        updateXp(result.xpEarned)
        showToast(`✦ +${result.xpEarned} XP — Feynman complete`)
      }
    } catch {
      showToast('Error submitting explanation')
    } finally {
      setSubmittingFeynman(false)
    }
  }

  if (loading) {
    return (
      <div className={styles.loadingState}>
        <div className={styles.loadingGlyph}>🔮</div>
        <p>Opening the Grimoire...</p>
      </div>
    )
  }

  if (!encoding) return null

  const phase = encoding.phase

  return (
    <div className={styles.page}>
      {/* Header */}
      <div className={styles.header}>
        <button className="btn btn-ghost" onClick={() => navigate(`/chunk/${encoding.chunkId}`)} style={{ fontSize: 12 }}>
          ← Back
        </button>
        <div className={styles.headerInfo}>
          <div className={styles.subTitle}>{encoding.title}</div>
          <div className={styles.phaseChips}>
            {(['HOOK', 'EXPLANATION', 'GUIDED_PRACTICE', 'RETRIEVAL_CHECK', 'COMPLETE'] as const).map(p => (
              <span key={p} className={`${styles.phaseChip} ${p === phase ? styles.phaseActive : phase === 'COMPLETE' || phaseOrder(p) < phaseOrder(phase) ? styles.phaseDone : ''}`}>
                {phaseLabel(p)}
              </span>
            ))}
          </div>
        </div>
      </div>

      {/* HOOK phase */}
      {phase === 'HOOK' && (
        <div className={styles.hookStage}>
          <div className={styles.hookCard}>
            <div className={styles.hookQuoteMark}>❝</div>
            <div className={styles.hookText} dangerouslySetInnerHTML={{ __html: encoding.hookHtml ?? '' }} />
          </div>
          <div className={styles.hookSubtitle}>{encoding.title}</div>
          <button className="btn btn-primary" style={{ marginTop: 32 }} onClick={handleAdvance}>
            Begin →
          </button>
        </div>
      )}

      {/* EXPLANATION phase */}
      {phase === 'EXPLANATION' && (
        <div className={styles.phaseContent}>
          {encoding.storyBeats && <StoryPanel beats={encoding.storyBeats} fullPage />}
          {encoding.explanationHtml && (
            <div className={styles.explanation} dangerouslySetInnerHTML={{ __html: encoding.explanationHtml }} />
          )}
          <button className="btn btn-primary" onClick={handleAdvance}>I understand — continue →</button>
        </div>
      )}

      {/* GUIDED_PRACTICE phase */}
      {phase === 'GUIDED_PRACTICE' && (
        <div className={styles.codingView}>
          <div className={styles.leftPanel}>
            <div className={styles.practiceLabel}>✦ Guided Practice</div>
            <div className={styles.practiceHtml} dangerouslySetInnerHTML={{ __html: encoding.guidedPracticeHtml ?? '' }} />
            {encoding.testCaseLabels && <TestChips labels={encoding.testCaseLabels} results={testResults} />}

            {practiceSolved && (
              <div className={styles.solvedPanel}>
                <div className={styles.solvedTitle}>✦ Practice Complete!</div>
                <button className="btn btn-primary" onClick={handleAdvance}>Continue to Retrieval Check →</button>
              </div>
            )}
          </div>

          <div className={styles.rightPanel}>
            <div className={styles.editorHeader}>
              <span className={styles.filename}>☽ {encoding.filename}</span>
              <div className={styles.editorActions}>
                <button className={`btn btn-ghost ${running ? styles.btnRunning : ''}`} onClick={handleRun} disabled={running} style={{ fontSize: 12, padding: '5px 14px' }}>
                  {running ? '⟳ Running…' : '▶ Run'}
                </button>
                <button className={practiceSolved ? styles.btnSolved : 'btn btn-primary'} onClick={handleSubmitPractice} disabled={running || practiceSolved} style={{ fontSize: 12, padding: '5px 14px' }}>
                  {practiceSolved ? '✓ Solved' : '⚡ Submit'}
                </button>
              </div>
            </div>
            <CodeEditor value={code} onChange={setCode} />
            <OutputPanel lines={output} />
            <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
          </div>
        </div>
      )}

      {/* RETRIEVAL_CHECK phase */}
      {phase === 'RETRIEVAL_CHECK' && (
        <div className={styles.phaseContent}>
          <div className={styles.retrievalTitle}>✦ Retrieval Check</div>
          <p className={styles.retrievalDesc}>Answer these questions to test your understanding.</p>

          {!retrievalResult ? (
            <>
              {encoding.retrievalQuestions?.map((q, i) => (
                <QuestionCard key={q.id} question={q} index={i} answer={answers[q.id] ?? ''} onChange={v => setAnswers(prev => ({ ...prev, [q.id]: v }))} />
              ))}
              <button className="btn btn-primary" onClick={handleSubmitRetrieval} disabled={submittingRetrieval} style={{ marginTop: 4 }}>
                {submittingRetrieval ? 'Submitting...' : '⚡ Submit Answers'}
              </button>
            </>
          ) : (
            <div className={styles.resultPanel}>
              <div className={styles.resultScore}>
                Score: {Math.round(retrievalResult.score * 100)}% ({retrievalResult.correct}/{retrievalResult.total})
              </div>
              <div className={`${styles.resultStatus} ${retrievalResult.passed ? styles.passed : styles.failed}`}>
                {retrievalResult.passed ? '✓ Passed!' : '✗ Needs more practice'}
              </div>
              {encoding.retrievalQuestions?.map((q, i) => (
                <QuestionCard
                  key={q.id} question={q} index={i}
                  answer={retrievalResult.results[i]?.userAnswer ?? ''}
                  onChange={() => {}}
                  result={retrievalResult.results[i]}
                  disabled
                />
              ))}
              <p className={styles.recommendation}>{retrievalResult.recommendation}</p>
              <button className="btn btn-primary" onClick={handleAdvance} style={{ marginTop: 12 }}>
                Continue →
              </button>
            </div>
          )}
        </div>
      )}

      {/* COMPLETE phase */}
      {phase === 'COMPLETE' && (
        <div className={styles.phaseContent}>
          <div className={styles.completePanel}>
            <div className={styles.completeGlyph}>✦</div>
            <h2 className={styles.completeTitle}>Concept Mastered!</h2>
            <p className={styles.completeMsg}>You've completed {encoding.title}. This concept will be reviewed via spaced repetition.</p>

            {/* Optional Feynman */}
            {encoding.feynmanPrompt && !feynmanResult && (
              <div className={styles.feynmanSection}>
                <div className={styles.feynmanTitle}>🧪 Feynman Challenge (Optional)</div>
                <p className={styles.feynmanPrompt}>{encoding.feynmanPrompt}</p>
                <textarea
                  className={styles.feynmanInput}
                  placeholder="Explain this concept in your own words..."
                  value={feynmanText}
                  onChange={e => setFeynmanText(e.target.value)}
                  rows={6}
                />
                <button className="btn btn-primary" onClick={handleSubmitFeynman} disabled={submittingFeynman || !feynmanText.trim()}>
                  {submittingFeynman ? 'Evaluating...' : '📝 Submit Explanation'}
                </button>
              </div>
            )}

            {feynmanResult && (
              <div className={styles.feynmanResult}>
                <div className={styles.feynmanScore}>Feynman Score: {Math.round(feynmanResult.overallScore * 100)}%</div>
                <div className={styles.feynmanBreakdown}>
                  <span>Accuracy: {Math.round(feynmanResult.accuracy * 100)}%</span>
                  <span>Completeness: {Math.round(feynmanResult.completeness * 100)}%</span>
                  <span>Simplicity: {Math.round(feynmanResult.simplicity * 100)}%</span>
                  <span>Connection: {Math.round(feynmanResult.connection * 100)}%</span>
                </div>
                <p className={styles.feynmanFeedback}>{feynmanResult.feedback}</p>
              </div>
            )}

            <div className={styles.completeActions}>
              <button className="btn btn-success" onClick={() => navigate(`/chunk/${encoding.chunkId}`)}>
                Return to Chunk →
              </button>
              <button className="btn btn-ghost" onClick={() => navigate('/')}>
                Dashboard
              </button>
            </div>
          </div>
        </div>
      )}

      {toast && <div className="toast">{toast}</div>}
      {levelUpInfo && <LevelUpModal newLevel={levelUpInfo.level} newRank={levelUpInfo.rank} onClose={() => setLevelUpInfo(null)} />}
      {newBadges.length > 0 && <BadgeToast badges={newBadges} onDone={() => setNewBadges([])} />}
    </div>
  )
}


function phaseOrder(p: string): number {
  return ['HOOK', 'EXPLANATION', 'GUIDED_PRACTICE', 'RETRIEVAL_CHECK', 'COMPLETE'].indexOf(p)
}

function phaseLabel(p: string): string {
  const labels: Record<string, string> = { HOOK: 'Hook', EXPLANATION: 'Learn', GUIDED_PRACTICE: 'Practice', RETRIEVAL_CHECK: 'Check', COMPLETE: 'Done' }
  return labels[p] ?? p
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
