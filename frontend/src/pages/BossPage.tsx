import { useEffect, useState, useRef, useCallback } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { bossApi } from '../api/services'
import { useAuth } from '../hooks/useAuth'
import type { BossData, BossQuestion, BossAnswerResponse } from '../types'
import LevelUpModal from '../components/layout/LevelUpModal'
import styles from './BossPage.module.css'

type QuestionState = 'unanswered' | 'correct' | 'wrong'

interface AnsweredQuestion {
  state: QuestionState
  response: BossAnswerResponse | null
  givenAnswer: string
}

function shuffle<T>(arr: T[]): T[] {
  for (let i = arr.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [arr[i], arr[j]] = [arr[j], arr[i]]
  }
  return arr
}

export default function BossPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { updateXp, user } = useAuth()

  const [boss, setBoss] = useState<BossData | null>(null)
  const [shuffledQuestions, setShuffledQuestions] = useState<BossData['questions']>([])
  const [showStudyMode, setShowStudyMode] = useState(false)
  const [loading, setLoading] = useState(true)
  const [currentQ, setCurrentQ] = useState(0)
  const [answered, setAnswered] = useState<Map<string, AnsweredQuestion>>(new Map())
  const [checking, setChecking] = useState(false)
  const [fillValue, setFillValue] = useState('')
  // 'fighting' | 'won' | 'lost'
  const [battleState, setBattleState] = useState<'fighting' | 'won' | 'lost'>('fighting')
  const [toast, setToast] = useState<string | null>(null)
  const [levelUpInfo, setLevelUpInfo] = useState<{level: number; rank: string} | null>(null)
  const toastTimer = useRef<ReturnType<typeof setTimeout>>()

  useEffect(() => {
    if (!id) return
    bossApi.getById(id)
      .then(b => {
        setBoss(b)
        setShuffledQuestions(shuffle([...b.questions]))
      })
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
    const q = activeQuestions[currentQ]
    const result = answered.get(q.id)
    const isLast = currentQ >= activeQuestions.length - 1

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
          const prevRank = calculateRank(user?.totalXp ?? 0)
          updateXp(progress.xpEarned, progress.rank)
          showToast(`✦ +${progress.xpEarned} XP — Boss Defeated!`)
          if (progress.rank !== prevRank) {
            const rankNames = ['Novice','Apprentice','Adept','Mage','Archmage']
            setTimeout(() => setLevelUpInfo({ level: rankNames.indexOf(progress.rank) + 1, rank: progress.rank }), 1200)
          }
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
    setShowStudyMode(false)
    if (boss) setShuffledQuestions(shuffle([...boss.questions]))
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

  const activeQuestions = shuffledQuestions.length > 0 ? shuffledQuestions : (boss?.questions ?? [])
  const totalQ = activeQuestions.length
  const correctCount = Array.from(answered.values()).filter(a => a.state === 'correct').length
  // HP drains as correct answers accumulate
  const hpPct = battleState === 'won' ? 0 : Math.max(0, Math.round(100 - (correctCount / totalQ) * 100))
  const question = activeQuestions[currentQ]
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
              The {boss.name} overpowers you. Review the concepts below, then try again.
              {currentAnswer?.response && (
                <div className={styles.explanation}>
                  Correct answer: <strong>{currentAnswer.response.correctAnswer}</strong><br />
                  {currentAnswer.response.explanation}
                </div>
              )}
            </div>
            <div style={{display:'flex',gap:10,flexWrap:'wrap',marginTop:4}}>
              <button className="btn btn-ghost" onClick={() => setShowStudyMode(s => !s)}>
                {showStudyMode ? 'Hide study guide' : '📖 Review concepts'}
              </button>
              <button className="btn btn-primary" onClick={handleRetry}>
                Try Again →
              </button>
            </div>
            {showStudyMode && boss && (
              <div className={styles.studyMode}>
                {activeQuestions.map((q, i) => (
                  <div key={q.id} className={styles.studyItem}>
                    <div className={styles.studyQ}>Q{i+1}: {q.question}</div>
                  </div>
                ))}
              </div>
            )}
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
      {levelUpInfo && (
        <LevelUpModal
          newLevel={levelUpInfo.level}
          newRank={levelUpInfo.rank}
          onClose={() => setLevelUpInfo(null)}
        />
      )}
    </div>
  )
}

// ── Java syntax highlighter ────────────────────────────────────────────────
const JAVA_KEYWORDS = new Set([
  'abstract','assert','boolean','break','byte','case','catch','char','class',
  'const','continue','default','do','double','else','enum','extends','final',
  'finally','float','for','if','implements','import','instanceof','int',
  'interface','long','native','new','null','package','private','protected',
  'public','return','short','static','super','switch','synchronized','this',
  'throw','throws','transient','true','false','try','void','volatile','while',
])

function escHtml(s: string): string {
  return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
}

function highlightJava(code: string): string {
  let out = ''
  let i = 0
  while (i < code.length) {
    const ch = code[i]
    // Line comment
    if (ch === '/' && code[i + 1] === '/') {
      const nl = code.indexOf('\n', i)
      const end = nl === -1 ? code.length : nl
      out += `<span class="jhc">${escHtml(code.slice(i, end))}</span>`
      i = end; continue
    }
    // Block comment
    if (ch === '/' && code[i + 1] === '*') {
      const end = code.indexOf('*/', i + 2)
      const stop = end === -1 ? code.length : end + 2
      out += `<span class="jhc">${escHtml(code.slice(i, stop))}</span>`
      i = stop; continue
    }
    // String literal
    if (ch === '"') {
      let j = i + 1
      while (j < code.length && code[j] !== '"') { if (code[j] === '\\') j++; j++ }
      j = Math.min(j + 1, code.length)
      out += `<span class="jhs">${escHtml(code.slice(i, j))}</span>`
      i = j; continue
    }
    // Char literal
    if (ch === "'") {
      let j = i + 1
      while (j < code.length && code[j] !== "'") { if (code[j] === '\\') j++; j++ }
      j = Math.min(j + 1, code.length)
      out += `<span class="jhs">${escHtml(code.slice(i, j))}</span>`
      i = j; continue
    }
    // Annotation
    if (ch === '@') {
      let j = i + 1
      while (j < code.length && /[A-Za-z]/.test(code[j])) j++
      out += `<span class="jha">${escHtml(code.slice(i, j))}</span>`
      i = j; continue
    }
    // Number
    if (ch >= '0' && ch <= '9') {
      let j = i
      while (j < code.length && /[0-9._xXaAbBcCdDeEfFlL]/.test(code[j])) j++
      out += `<span class="jhn">${escHtml(code.slice(i, j))}</span>`
      i = j; continue
    }
    // Identifier / keyword / class name
    if (/[A-Za-z_$]/.test(ch)) {
      let j = i
      while (j < code.length && /[A-Za-z0-9_$]/.test(code[j])) j++
      const word = code.slice(i, j)
      if (JAVA_KEYWORDS.has(word)) {
        out += `<span class="jhk">${escHtml(word)}</span>`
      } else if (/^[A-Z]/.test(word)) {
        out += `<span class="jhcl">${escHtml(word)}</span>`
      } else {
        out += escHtml(word)
      }
      i = j; continue
    }
    out += escHtml(ch)
    i++
  }
  return out
}

function calculateRank(xp: number): string {
  if (xp >= 4000) return 'Archmage'
  if (xp >= 2000) return 'Mage'
  if (xp >= 1000) return 'Adept'
  if (xp >= 400)  return 'Apprentice'
  return 'Novice'
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
        <pre className={styles.codeBlock}><code dangerouslySetInnerHTML={{ __html: highlightJava(question.code) }} /></pre>
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
