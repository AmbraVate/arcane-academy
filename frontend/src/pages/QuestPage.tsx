// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
import { useEffect, useState, useRef, useCallback } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { questApi, codeApi } from '../api/services'
import { useAuth } from '../hooks/useAuth'
import type { QuestDetail, SubmitResponse, CodeRunResponse } from '../types'
import StoryPanel from '../components/quest/StoryPanel'
import CodeEditor from '../components/quest/CodeEditor'
import OutputPanel from '../components/quest/OutputPanel'
import TestChips from '../components/quest/TestChips'
import AiMentorPanel from '../components/quest/AiMentorPanel'
import styles from './QuestPage.module.css'

type OutputLine = { text: string; type: 'normal' | 'success' | 'error' | 'system' }

export default function QuestPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { updateXp } = useAuth()

  const [quest, setQuest] = useState<QuestDetail | null>(null)
  const [loading, setLoading] = useState(true)
  const [code, setCode] = useState('')
  const [output, setOutput] = useState<OutputLine[]>([{ text: '// Cast your spell to run the code.', type: 'system' }])
  const [testResults, setTestResults] = useState<Map<string, boolean>>(new Map())
  const [running, setRunning] = useState(false)
  const [solved, setSolved] = useState(false)
  const [mentorFeedback, setMentorFeedback] = useState<string | null>(null)
  const [mentorLoading, setMentorLoading] = useState(false)
  const [toast, setToast] = useState<string | null>(null)
  const [winStoryVisible, setWinStoryVisible] = useState(false)
  const toastTimer = useRef<ReturnType<typeof setTimeout>>()

  useEffect(() => {
    if (!id) return
    questApi.getDetail(id)
      .then(q => {
        setQuest(q)
        setCode(q.starterCode)
        if (q.completed) setSolved(true)
      })
      .catch(() => navigate('/'))
      .finally(() => setLoading(false))
  }, [id, navigate])

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

  async function handleSubmit() {
    if (!quest || running || solved) return
    setRunning(true)
    setMentorFeedback(null)
    setOutput([{ text: '// Running all test cases...', type: 'system' }])
    setTestResults(new Map())

    try {
      const result: SubmitResponse = await codeApi.submit(quest.id, code)
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
        setSolved(true)
        setWinStoryVisible(true)
        if (result.xpEarned > 0) {
          updateXp(result.xpEarned)
          showToast(`✦ +${result.xpEarned} XP — Quest Complete!`)
        }
      } else {
        lines.push({ text: '✗ Some test cases failed.', type: 'error' })
        if (result.mentorFeedback) {
          setMentorLoading(true)
          // Small delay so the loading state is visible
          setTimeout(() => {
            setMentorFeedback(result.mentorFeedback)
            setMentorLoading(false)
          }, 400)
        }
      }

      setOutput(lines)
    } catch {
      setOutput([{ text: 'Error submitting code.', type: 'error' }])
    } finally {
      setRunning(false)
    }
  }

  function handleNextQuest() {
    navigate('/', { replace: true })
    setTimeout(() => window.location.reload(), 50)
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

  return (
    <div className={styles.layout}>
      {/* LEFT: Story + Problem */}
      <div className={styles.leftPanel}>
        <div className={styles.questHeader}>
          <div className={styles.eyebrow}>{quest.eyebrow}</div>
          <div className={styles.questTitle}>{quest.title}</div>
          <div className={styles.pills}>
            <span className="chip chip-purple">{quest.topic}</span>
            <span className="chip chip-green">+{quest.xpReward} XP</span>
          </div>
        </div>

        <div className={styles.storyScroll}>
          <StoryPanel beats={quest.story} />
        </div>

        <div className={styles.problemBox}>
          <div className={styles.problemLabel}>✦ Your Quest</div>
          <div
            className={styles.problemText}
            dangerouslySetInnerHTML={{ __html: quest.problemHtml }}
          />
          <TestChips labels={quest.testCaseLabels} results={testResults} />
          <HintToggle hint={quest.hint} />
        </div>

        {winStoryVisible && (
          <div className={styles.resultBanner}>
            <div className={styles.resultTitle}>Quest Complete!</div>
            <div className={styles.resultMsg}>{quest.winStory}</div>
            <button className="btn btn-success" onClick={handleNextQuest}>
              Return to Academy →
            </button>
          </div>
        )}
      </div>

      {/* RIGHT: Editor + Output + AI */}
      <div className={styles.rightPanel}>
        <div className={styles.editorHeader}>
          <span className={styles.filename}>☽ {quest.filename}</span>
          <div className={styles.editorActions}>
            <button
              className="btn btn-ghost"
              onClick={handleRun}
              disabled={running}
              style={{ fontSize: 12, padding: '5px 14px' }}
            >
              ▶ Run
            </button>
            <button
              className="btn btn-primary"
              onClick={handleSubmit}
              disabled={running || solved}
              style={{ fontSize: 12, padding: '5px 14px' }}
            >
              {solved ? '✓ Solved' : running ? 'Running...' : '⚡ Submit'}
            </button>
          </div>
        </div>

        <CodeEditor value={code} onChange={setCode} />

        <OutputPanel lines={output} />

        <AiMentorPanel feedback={mentorFeedback} loading={mentorLoading} />
      </div>

      {toast && <div className="toast">{toast}</div>}
    </div>
  )
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
