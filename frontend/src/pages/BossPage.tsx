import { useEffect, useState, useRef, useCallback } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { bossApi } from '../api/services'
import { useAuth } from '../hooks/useAuth'
import type { BossData, BossQuestion, BossAnswerResponse } from '../types'
import styles from './BossPage.module.css'

type QuestionState = 'unanswered' | 'correct' | 'wrong'

interface AnsweredQuestion {
  state: QuestionState
  response: BossAnswerResponse | null
  givenAnswer: string
}

export default function BossPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { updateXp } = useAuth()

  const [boss, setBoss] = useState<BossData | null>(null)
  const [loading, setLoading] = useState(true)
  const [currentQ, setCurrentQ] = useState(0)
  const [answered, setAnswered] = useState<Map<string, AnsweredQuestion>>(new Map())
  const [checking, setChecking] = useState(false)
  const [fillValue, setFillValue] = useState('')
  // 'fighting' | 'won' | 'lost'
  const [battleState, setBattleState] = useState<'fighting' | 'won' | 'lost'>('fighting')
  const [toast, setToast] = useState<string | null>(null)
  const toastTimer = useRef<ReturnType<typeof setTimeout>>()

  useEffect(() => {
    if (!id) return
    bossApi.getById(id)
      .then(setBoss)
      .catch(() => navigate('/'))
      .finally(() => setLoading(false))
  }, [id, navigate])

  const showToast = useCallback((msg: string) => {
    setToast(msg)
    clearTimeout(toastTimer.current)
    toastTimer.current = setTimeout(() => setToast(null), 2600)
  }, [])

  async function submitAnswer(questionId: string, answer: string) {
    if (!boss || checking) return
    setChecking(true)
    try {
      const response = await bossApi.checkAnswer(boss.id, questionId, answer)
      const state: QuestionState = response.correct ? 'correct' : 'wrong'
      setAnswered(prev => new Map(prev).set(questionId, { state, response, givenAnswer: answer }))
    } catch {
      showToast('Error checking answer')
    } finally {
      setChecking(false)
      setFillValue('')
    }
  }

  async function handleNext() {
    if (!boss) return
    const q = boss.questions[currentQ]
    const result = answered.get(q.id)
    const isLast = currentQ >= boss.questions.length - 1

    if (result?.state === 'wrong') {
      // Wrong answer — show defeat screen
      setBattleState('lost')
      return
    }

    if (!isLast) {
      // Move to next question
      setCurrentQ(c => c + 1)
    } else {
      // Last question answered correctly — victory!
      try {
        const progress = await bossApi.defeat(boss.id)
        if (progress.xpEarned > 0) {
          updateXp(progress.xpEarned, progress.rank)
          showToast(`✦ +${progress.xpEarned} XP — Boss Defeated!`)
        }
      } catch {
        // Already defeated previously — still show win screen
      }
      setBattleState('won')
    }
  }

  function handleRetry() {
    setCurrentQ(0)
    setAnswered(new Map())
    setBattleState('fighting')
    setFillValue('')
  }

  // Navigate home AND force a page reload so progress is re-fetched from server
  function returnToAcademy() {
    navigate('/', { replace: true })
    // Small delay ensures navigation completes before reload
    setTimeout(() => window.location.reload(), 50)
  }

  if (loading) return (
    <div className={styles.loading}>
      <div className={styles.loadingGlyph}>⚔️</div>
      <p>Summoning the boss...</p>
    </div>
  )
  if (!boss) return null

  const totalQ = boss.questions.length
  const correctCount = Array.from(answered.values()).filter(a => a.state === 'correct').length
  // HP drains as correct answers accumulate
  const hpPct = battleState === 'won' ? 0 : Math.max(0, Math.round(100 - (correctCount / totalQ) * 100))
  const question = boss.questions[currentQ]
  const currentAnswer = answered.get(question?.id ?? '')

  return (
    <div className={styles.page}>
      <div className={styles.wrap}>
        <button className="btn btn-ghost" onClick={returnToAcademy} style={{ marginBottom: 20 }}>
          ← Retreat to Academy
        </button>

        {/* Boss arena */}
        <div className={styles.arena}>
          <div className={styles.bossGlyph}>{boss.glyph}</div>
          <div className={styles.bossName}>{boss.name}</div>
          <div className={styles.hpRow}>
            <span className={styles.hpLabel}>Boss HP</span>
            <div className={styles.hpBar}>
              <div className={styles.hpFill} style={{ width: `${hpPct}%` }} />
            </div>
            <span className={styles.hpLabel}>{hpPct}%</span>
          </div>
          <div className={styles.qProgress}>
            {battleState === 'fighting' ? `Question ${currentQ + 1} of ${totalQ}` : battleState === 'won' ? 'Defeated!' : 'Battle lost'}
          </div>
        </div>

        {/* Intro text — shown before first answer */}
        {currentQ === 0 && answered.size === 0 && battleState === 'fighting' && (
          <div className={styles.introBox}>{boss.intro}</div>
        )}

        {/* Victory screen */}
        {battleState === 'won' && (
          <div className={`${styles.resultBox} ${styles.win}`}>
            <div className={styles.resultTitle}>⚡ Boss Defeated!</div>
            <div className={styles.resultMsg}>
              You answered all {totalQ} questions correctly. The {boss.name} crumbles into dust.
              The next chapter is now unlocked.
            </div>
            <button className="btn btn-success" onClick={returnToAcademy}>
              Return to Academy →
            </button>
          </div>
        )}

        {/* Defeat screen */}
        {battleState === 'lost' && (
          <div className={`${styles.resultBox} ${styles.fail}`}>
            <div className={styles.resultTitle}>💀 Defeated!</div>
            <div className={styles.resultMsg}>
              The {boss.name} overpowers you. Study the explanation and try again.
              {currentAnswer?.response && (
                <div className={styles.explanation}>
                  Correct answer: <strong>{currentAnswer.response.correctAnswer}</strong><br />
                  {currentAnswer.response.explanation}
                </div>
              )}
            </div>
            <button className="btn btn-primary" onClick={handleRetry}>
              Try Again →
            </button>
          </div>
        )}

        {/* Active question */}
        {battleState === 'fighting' && question && (
          <div className={styles.questionCard}>
            <QuestionDisplay
              question={question}
              answered={currentAnswer}
              checking={checking}
              fillValue={fillValue}
              onFillChange={setFillValue}
              onAnswer={submitAnswer}
            />

            {currentAnswer && (
              <div className={`${styles.feedback} ${currentAnswer.state === 'correct' ? styles.feedbackWin : styles.feedbackLose}`}>
                <div className={styles.feedbackIcon}>
                  {currentAnswer.state === 'correct' ? '⚡' : '💀'}
                </div>
                <div>
                  <div className={styles.feedbackTitle}>
                    {currentAnswer.state === 'correct'
                      ? 'Correct!'
                      : `Wrong — the answer was: ${currentAnswer.response?.correctAnswer}`}
                  </div>
                  <div className={styles.feedbackExplain}>{currentAnswer.response?.explanation}</div>
                </div>
              </div>
            )}

            {currentAnswer && (
              <button className="btn btn-primary" onClick={handleNext} style={{ marginTop: 14 }}>
                {currentAnswer.state === 'wrong'
                  ? 'See defeat →'
                  : currentQ < totalQ - 1
                  ? 'Next question →'
                  : 'Claim victory →'}
              </button>
            )}
          </div>
        )}
      </div>

      {toast && <div className="toast">{toast}</div>}
    </div>
  )
}

// ── Sub-components for each question type ──────────────────────────

interface QuestionDisplayProps {
  question: BossQuestion
  answered: AnsweredQuestion | undefined
  checking: boolean
  fillValue: string
  onFillChange: (v: string) => void
  onAnswer: (qId: string, answer: string) => void
}

function QuestionDisplay({ question, answered, checking, fillValue, onFillChange, onAnswer }: QuestionDisplayProps) {
  const isAnswered = !!answered

  return (
    <div>
      <div className={styles.questionType}>
        {question.type === 'multiple_choice' && '🔮 Multiple Choice'}
        {question.type === 'be_the_compiler' && '💻 Be the Compiler'}
        {question.type === 'fill_blank' && '✍️ Fill the Rune'}
      </div>

      <div className={styles.questionText}>{question.question}</div>

      {question.code && (
        <pre className={styles.codeBlock}><code>{question.code}</code></pre>
      )}

      {(question.type === 'multiple_choice' || question.type === 'be_the_compiler') && question.options && (
        <div className={styles.options}>
          {question.options.map(opt => {
            let cls = styles.option
            if (isAnswered) {
              if (opt === answered?.givenAnswer && answered.state === 'correct') cls += ` ${styles.optCorrect}`
              else if (opt === answered?.givenAnswer && answered.state === 'wrong') cls += ` ${styles.optWrong}`
              else if (opt === answered?.response?.correctAnswer && answered.state === 'wrong') cls += ` ${styles.optCorrect}`
              else cls += ` ${styles.optDisabled}`
            }
            return (
              <button
                key={opt}
                className={cls}
                disabled={isAnswered || checking}
                onClick={() => onAnswer(question.id, opt)}
              >
                {opt}
              </button>
            )
          })}
        </div>
      )}

      {question.type === 'fill_blank' && (
        <div className={styles.fillWrap}>
          <input
            className={`input ${styles.fillInput}`}
            type="text"
            placeholder="Type your answer..."
            value={fillValue}
            onChange={e => onFillChange(e.target.value)}
            disabled={isAnswered}
            onKeyDown={e => {
              if (e.key === 'Enter' && !isAnswered && fillValue.trim()) {
                onAnswer(question.id, fillValue.trim())
              }
            }}
          />
          {!isAnswered && (
            <button
              className="btn btn-primary"
              disabled={!fillValue.trim() || checking}
              onClick={() => onAnswer(question.id, fillValue.trim())}
            >
              {checking ? '...' : 'Submit'}
            </button>
          )}
        </div>
      )}
    </div>
  )
}
