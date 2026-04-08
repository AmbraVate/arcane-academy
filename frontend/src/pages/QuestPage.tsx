import { useEffect, useState, useRef, useCallback } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { questApi, codeApi } from '../api/services'
import { useAuth } from '../hooks/useAuth'
import type { QuestDetail, SubmitResponse, CodeRunResponse, Badge } from '../types'
import StoryPanel from '../components/quest/StoryPanel'
import CodeEditor from '../components/quest/CodeEditor'
import OutputPanel from '../components/quest/OutputPanel'
import TestChips from '../components/quest/TestChips'
import AiMentorPanel from '../components/quest/AiMentorPanel'
import LevelUpModal from '../components/layout/LevelUpModal'
import BadgeToast from '../components/layout/BadgeToast'
import styles from './QuestPage.module.css'

type OutputLine = { text: string; type: 'normal' | 'success' | 'error' | 'system' }
type QuestStage = 'story' | 'coding' | 'complete'

export default function QuestPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { user, updateXp } = useAuth()

  const [quest, setQuest] = useState<QuestDetail | null>(null)
  const [loading, setLoading] = useState(true)
  const [code, setCode] = useState('')
  const [output, setOutput] = useState<OutputLine[]>([{ text: '// Cast your spell to run the code.', type: 'system' }])
  const [testResults, setTestResults] = useState<Map<string, boolean>>(new Map())
  const [running, setRunning] = useState(false)
  const [mentorFeedback, setMentorFeedback] = useState<string | null>(null)
  const [mentorLoading, setMentorLoading] = useState(false)
  const [mentorErrorType, setMentorErrorType] = useState<string | null>(null)
  const [toast, setToast] = useState<string | null>(null)
  const [levelUpInfo, setLevelUpInfo] = useState<{ level: number; rank: string } | null>(null)
  const [questStage, setQuestStage] = useState<QuestStage>('story')
  const [questPanelVisible, setQuestPanelVisible] = useState(false)
  const [activeTab, setActiveTab] = useState<'quest' | 'code'>('quest')
  const [isPracticing, setIsPracticing] = useState(false)
  const [showLesson, setShowLesson] = useState(false)
  const [newBadges, setNewBadges] = useState<Badge[]>([])
  const toastTimer = useRef<ReturnType<typeof setTimeout>>()
  const storyEndRef = useRef<HTMLDivElement>(null)
  const questPanelRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (!id) return
    questApi.getDetail(id)
      .then(q => {
        setQuest(q)
        setCode(q.starterCode)
        if (q.completed) setQuestStage('coding')
      })
      .catch(() => navigate('/'))
      .finally(() => setLoading(false))
  }, [id, navigate])

  useEffect(() => {
    if (!storyEndRef.current || questStage !== 'story') return
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) setQuestPanelVisible(true)
      },
      { threshold: 0.1 }
    )
    observer.observe(storyEndRef.current)
    return () => observer.disconnect()
  }, [quest, questStage])

  const showToast = useCallback((msg: string) => {
    setToast(msg)
    clearTimeout(toastTimer.current)
    toastTimer.current = setTimeout(() => setToast(null), 2600)
  }, [])

  async function handleRun() {
    if (!quest || running) return
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

  function startPractice() {
    setIsPracticing(true)
    setCode(quest!.starterCode)
    setOutput([{ text: '// Practice mode — XP already awarded for this quest.', type: 'system' }])
    setTestResults(new Map())
    setMentorFeedback(null)
  }

  async function handleSubmit() {
    if (!quest || running) return
    setRunning(true)
    setMentorFeedback(null)
    setOutput([{ text: '// Running all test cases...', type: 'system' }])
    setTestResults(new Map())

    try {
      const result: SubmitResponse = await codeApi.submit(quest.id, code)

      if (result.errorType === 'COMPILE_ERROR') {
        setOutput([{ text: '✗ Spell failed to compile — your code has a syntax error.', type: 'error' }])
        setTestResults(new Map())
        setMentorErrorType('COMPILE_ERROR')
        if (result.mentorFeedback) {
          setMentorLoading(true)
          setTimeout(() => { setMentorFeedback(result.mentorFeedback); setMentorLoading(false) }, 300)
        }
        return
      }

      if (result.errorType === 'RUNTIME_ERROR') {
        setOutput([
          { text: '✗ Spell compiled but crashed at runtime.', type: 'error' },
          { text: result.testResults?.[0]?.actualOutput || '', type: 'error' },
        ])
        setTestResults(new Map())
        setMentorErrorType('RUNTIME_ERROR')
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
        setQuestStage('complete')
        setIsPracticing(false)
        if (result.xpEarned > 0) {
          const prevRank = calculateRank(user?.totalXp ?? 0)
          const newTotalXp = (user?.totalXp ?? 0) + result.xpEarned
          const newRank = calculateRank(newTotalXp)
          updateXp(result.xpEarned, newRank)
          showToast(`✦ +${result.xpEarned} XP — Quest Complete!`)
          if (newRank !== prevRank) {
            const rankNames = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage']
            setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(newRank) + 1, rank: newRank }), 1200)
          }
          if (result.newBadges && result.newBadges.length > 0) {
            setNewBadges(result.newBadges)
          }
        } else {
          showToast('✓ Solution verified — no XP awarded twice')
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

  function handleBackToAcademy() {
    navigate('/')
    setTimeout(() => window.location.reload(), 50)
  }

  function handleSkipToQuest() {
    setQuestPanelVisible(true)
    questPanelRef.current?.scrollIntoView({ behavior: 'smooth' })
  }

  if (loading) {
    return (
      <div className={styles.loadingState}>
        <div className={styles.loadingGlyph}>🔮</div>
        <p>Opening the Grimoire...</p>
      </div>
    )
  }

  if (!quest) return null

  const isWon = (questStage === 'complete' || quest.completed) && !isPracticing
  const isSolved = isWon

  // ── Stage 1: Story View ──────────────────────────────────────────────────────
  if (questStage === 'story') {
    return (
      <div className={styles.storyView}>
        <div className={styles.storyViewHeader}>
          <div>
            <div className={styles.eyebrow}>{quest.eyebrow}</div>
            <div className={styles.questTitle}>{quest.title}</div>
          </div>
        </div>

        <div className={styles.storyContent}>
          <StoryPanel beats={quest.story} fullPage />
          <div ref={storyEndRef} style={{ height: 1 }} aria-hidden="true" />

          <div
            ref={questPanelRef}
            className={`${styles.questPanel} ${questPanelVisible ? styles.questPanelVisible : ''}`}
          >
            <div className={styles.problemLabel}>✦ Your Quest</div>
            <div
              className={styles.problemText}
              dangerouslySetInnerHTML={{ __html: quest.problemHtml }}
            />
            <TestChips labels={quest.testCaseLabels} results={testResults} />
            <HintToggle hint={quest.hint} />
          </div>

          {!questPanelVisible && (
            <button className={styles.skipToQuest} onClick={handleSkipToQuest}>
              Skip to Quest ↓
            </button>
          )}
        </div>

        {questPanelVisible && (
          <div className={styles.actionBar}>
            <button className="btn btn-ghost" onClick={handleBackToAcademy}>
              ← Back to Academy
            </button>
            <button className={styles.acceptBtn} onClick={() => setQuestStage('coding')}>
              {quest.completed ? 'Review Quest →' : '⚡ Accept Quest →'}
            </button>
          </div>
        )}

        {toast && <div className="toast">{toast}</div>}
      </div>
    )
  }

  // ── Stage 2 / 3: Coding View ─────────────────────────────────────────────────
  const editorButtons = (
    <>
      <button
        className={`btn btn-ghost ${running ? styles.btnRunning : ''}`}
        onClick={handleRun}
        disabled={running}
        style={{ fontSize: 12, padding: '5px 14px' }}
      >
        {running ? '⟳ Running…' : '▶ Run'}
      </button>
      <button
        className={isSolved ? styles.btnSolved : 'btn btn-primary'}
        onClick={handleSubmit}
        disabled={running || isSolved}
        style={{ fontSize: 12, padding: '5px 14px' }}
        aria-label={isSolved ? 'Quest solved' : 'Submit your solution'}
      >
        {isSolved ? '✓ Solved' : running ? '⟳ Running…' : isPracticing ? '⚡ Re-submit' : '⚡ Submit'}
      </button>
    </>
  )

  return (
    <div className={styles.codingView}>
      {/* Mobile tab bar */}
      <div className={styles.mobileTabBar}>
        <button
          className={`${styles.tab} ${activeTab === 'quest' ? styles.tabActive : ''}`}
          onClick={() => setActiveTab('quest')}
        >
          Quest
        </button>
        <button
          className={`${styles.tab} ${activeTab === 'code' ? styles.tabActive : ''}`}
          onClick={() => setActiveTab('code')}
        >
          Code
        </button>
      </div>

      {/* LEFT: Quest brief */}
      <div className={`${styles.leftPanel} ${activeTab === 'code' ? styles.mobileHidden : ''}`}>
        <div className={styles.questHeader}>
          <div className={styles.eyebrow}>{quest.eyebrow}</div>
          <div className={styles.questTitle}>{quest.title}</div>
          <div className={styles.pills}>
            <span className="chip chip-purple">{quest.topic}</span>
            <span className="chip chip-green">+{quest.xpReward} XP</span>
          </div>
          <button
            className={`btn btn-ghost ${styles.lessonToggle}`}
            onClick={() => setShowLesson(l => !l)}
          >
            📖 {showLesson ? 'Hide lesson' : 'View lesson'}
          </button>
        </div>

        <div className={styles.leftScroll}>
          {showLesson && (
            <div className={styles.lessonPanel}>
              <StoryPanel beats={quest.story} />
            </div>
          )}

          {isWon ? (
            <div className={styles.winPanel}>
              <div className={styles.resultTitle}>✦ Quest Complete!</div>
              <div className={styles.resultMsg}>{quest.winStory}</div>
              <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                <button className="btn btn-success" onClick={handleBackToAcademy}>
                  Return to Academy →
                </button>
                <button className="btn btn-ghost" style={{ fontSize: 12 }} onClick={startPractice}>
                  🔄 Practice Again
                </button>
              </div>
            </div>
          ) : (
            <div className={styles.problemBox}>
              <div className={styles.problemLabel}>✦ Your Quest</div>
              <div
                className={styles.problemText}
                dangerouslySetInnerHTML={{ __html: quest.problemHtml }}
              />
              <TestChips labels={quest.testCaseLabels} results={testResults} />
              <HintToggle hint={quest.hint} />
            </div>
          )}
        </div>

        <div className={styles.leftFooter}>
          <button className="btn btn-ghost" onClick={handleBackToAcademy} style={{ fontSize: 12 }}>
            ← Back to Academy
          </button>
        </div>
      </div>

      {/* RIGHT: Editor + Output + AI */}
      <div className={`${styles.rightPanel} ${activeTab === 'quest' ? styles.mobileHidden : ''}`}>
        <div className={styles.editorHeader}>
          <span className={styles.filename}>☽ {quest.filename}</span>
          <div className={`${styles.editorActions} ${styles.desktopOnly}`}>
            {editorButtons}
          </div>
        </div>

        <CodeEditor value={code} onChange={setCode} />
        <OutputPanel lines={output} />
        <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} errorType={mentorErrorType} />
      </div>

      {/* Mobile sticky footer */}
      <div className={styles.mobileStickyFooter}>
        {editorButtons}
      </div>

      {toast && <div className="toast">{toast}</div>}
      {levelUpInfo && (
        <LevelUpModal
          newLevel={levelUpInfo.level}
          newRank={levelUpInfo.rank}
          onClose={() => setLevelUpInfo(null)}
        />
      )}
      {newBadges.length > 0 && (
        <BadgeToast badges={newBadges} onDone={() => setNewBadges([])} />
      )}
    </div>
  )
}

function calculateRank(xp: number): string {
  if (xp >= 4000) return 'Archmage'
  if (xp >= 2000) return 'Mage'
  if (xp >= 1000) return 'Adept'
  if (xp >= 400)  return 'Apprentice'
  return 'Novice'
}

function HintToggle({ hint }: { hint: string }) {
  const [open, setOpen] = useState(false)
  return (
    <div style={{ marginTop: 10 }}>
      <button
        className="btn btn-ghost"
        style={{ fontSize: 12, padding: '4px 12px' }}
        onClick={() => setOpen(o => !o)}
      >
        💡 {open ? 'Hide hint' : 'Reveal hint'}
      </button>
      {open && (
        <div
          style={{
            marginTop: 8,
            background: '#12100a',
            border: '1px solid var(--gold-dim)',
            borderRadius: 6,
            padding: '9px 13px',
            fontSize: 13,
            lineHeight: 1.65,
            color: '#c9a45a',
          }}
          dangerouslySetInnerHTML={{ __html: hint }}
        />
      )}
    </div>
  )
}
