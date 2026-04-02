import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { questApi } from '../api/services'
import type { QuestSummary } from '../types'
import styles from './TopicSelectPage.module.css'

interface Topic {
  id: string
  icon: string
  name: string
  description: string
  available: boolean
  questCount: number
}

interface TopicProgress {
  completed: number
  total: number
  xpEarned: number
}

const TOPICS: Topic[] = [
  {
    id: 'java',
    icon: '☕',
    name: 'Java',
    description: 'From Hello World to design patterns. A complete journey to professional Java development.',
    available: true,
    questCount: 25,
  },
  {
    id: 'python',
    icon: '🐍',
    name: 'Python',
    description: 'Scripting, data science, and automation. Coming soon.',
    available: false,
    questCount: 0,
  },
  {
    id: 'mathematics',
    icon: '📐',
    name: 'Mathematics',
    description: 'Algebra, calculus, statistics — the language of the universe. Coming soon.',
    available: false,
    questCount: 0,
  },
  {
    id: 'history',
    icon: '📜',
    name: 'History',
    description: 'Ancient civilisations to modern era. Coming soon.',
    available: false,
    questCount: 0,
  },
  {
    id: 'physics',
    icon: '⚛️',
    name: 'Physics',
    description: 'Mechanics, electromagnetism, and quantum theory. Coming soon.',
    available: false,
    questCount: 0,
  },
  {
    id: 'chemistry',
    icon: '⚗️',
    name: 'Chemistry',
    description: 'Elements, reactions, and molecular structures. Coming soon.',
    available: false,
    questCount: 0,
  },
]

function ProgressWheel({ pct }: { pct: number }) {
  const r = 22
  const circumference = 2 * Math.PI * r
  const offset = circumference * (1 - pct / 100)
  return (
    <svg className={styles.wheel} viewBox="0 0 52 52" aria-label={`${pct}% complete`}>
      <circle className={styles.wheelTrack} cx="26" cy="26" r={r} />
      <circle
        className={styles.wheelFill}
        cx="26"
        cy="26"
        r={r}
        strokeDasharray={circumference}
        strokeDashoffset={offset}
      />
      <text className={styles.wheelText} x="26" y="26" textAnchor="middle" dominantBaseline="central">
        {pct}%
      </text>
    </svg>
  )
}

function MasteredEmblem() {
  return (
    <div className={styles.masteredEmblem} aria-label="Course mastered">
      <div className={styles.masteredRibbon}>
        <span className={styles.masteredStar}>✦</span>
        <span className={styles.masteredText}>Mastered</span>
      </div>
      <div className={styles.masteredTail} />
    </div>
  )
}

export default function TopicSelectPage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [progressMap, setProgressMap] = useState<Record<string, TopicProgress>>({})
  const activeTopic = localStorage.getItem('arcane_topic') ?? ''

  useEffect(() => {
    questApi.getAll().then((quests: QuestSummary[]) => {
      // The quest `topic` field is a sub-topic label ("If / Else", "Arrays", etc.),
      // not the parent course ID. All current quests belong to Java, so we
      // bucket them all under 'java'. When new courses are added this will need
      // a proper course-level field from the backend.
      const java: TopicProgress = { completed: 0, total: 0, xpEarned: 0 }
      quests.forEach(q => {
        java.total++
        if (q.completed) {
          java.completed++
          java.xpEarned += q.xpReward
        }
      })
      setProgressMap({ java })
    }).catch(() => {/* silently ignore — progress is non-critical */})
  }, [])

  function handleSelect(topic: Topic) {
    if (!topic.available) return
    localStorage.setItem('arcane_topic', topic.id)
    navigate('/')
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.glyph}>✦</div>
        <h1 className={styles.title}>Welcome, {user?.username}</h1>
        <p className={styles.sub}>Choose your discipline. Your journey begins here.</p>
      </div>

      <div className={styles.grid}>
        {TOPICS.map(topic => {
          const prog = progressMap[topic.id] ?? null
          const pct = prog && prog.total > 0 ? Math.round((prog.completed / prog.total) * 100) : 0
          const isComplete = prog != null && prog.completed > 0 && prog.completed >= prog.total
          const isStarted = prog != null && prog.completed > 0 && !isComplete
          const isCurrent = topic.id === activeTopic

          let cardClass = styles.card
          if (!topic.available) cardClass += ` ${styles.locked}`
          else if (isComplete) cardClass += ` ${styles.available} ${styles.cardComplete}`
          else if (isCurrent) cardClass += ` ${styles.available} ${styles.cardCurrent}`
          else cardClass += ` ${styles.available}`

          return (
            <div key={topic.id} className={cardClass} onClick={() => handleSelect(topic)}>
              <div className={styles.cardTop}>
                <div className={styles.cardIcon}>{topic.icon}</div>
                {topic.available && isComplete && <MasteredEmblem />}
                {topic.available && isStarted && <ProgressWheel pct={pct} />}
              </div>

              <div className={styles.cardName}>{topic.name}</div>
              <div className={styles.cardDesc}>{topic.description}</div>

              {topic.available && isStarted && (
                <div className={styles.progressBar}>
                  <div className={styles.progressFill} style={{ width: `${pct}%` }} />
                </div>
              )}

              <div className={styles.cardFooter}>
                {topic.available ? (
                  <>
                    <div className={styles.footerLeft}>
                      <span className="chip chip-green">{topic.questCount} quests</span>
                      {isStarted && prog && (
                        <span className={styles.questsDone}>{prog.completed} / {prog.total} done</span>
                      )}
                      {isComplete && prog && (
                        <span className={styles.xpEarned}>+{prog.xpEarned} XP earned</span>
                      )}
                    </div>
                    <span className={`chip chip-purple ${styles.startChip}`}>
                      {isComplete ? 'Review →' : isStarted ? 'Continue →' : 'Begin →'}
                    </span>
                  </>
                ) : (
                  <span className="chip chip-gray">Coming soon</span>
                )}
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}
