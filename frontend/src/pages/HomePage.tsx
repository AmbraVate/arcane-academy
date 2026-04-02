import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { questApi, bossApi } from '../api/services'
import type { QuestSummary, BossData } from '../types'
import styles from './HomePage.module.css'

const CHAPTER_META = [
  { number: 1, title: 'Chapter I — The First Rune',        subtitle: 'Variables & arithmetic',          icon: '🔮' },
  { number: 2, title: 'Chapter II — The Control Tome',     subtitle: 'Conditions, loops & logic',        icon: '📜' },
  { number: 3, title: 'Chapter III — Arcane Structures',   subtitle: 'Arrays, lists & methods',          icon: '🗝' },
  { number: 4, title: 'Chapter IV — The Grand Grimoire',   subtitle: 'Classes, objects & OOP',           icon: '📖' },
  { number: 5, title: "Chapter V — The Master's Path",     subtitle: 'Exceptions, generics & patterns',  icon: '⚡' },
  { number: 6, title: 'Chapter VI — The Capstone Project', subtitle: 'Build a real Java application',    icon: '🏗️' },
  { number: 7, title: 'Chapter VII — Interview Gauntlet',  subtitle: 'FizzBuzz to HashMap — get hired',  icon: '💼' },
]

export default function HomePage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [quests, setQuests] = useState<QuestSummary[]>([])
  const [bosses, setBosses] = useState<BossData[]>([])
  const [openChapters, setOpenChapters] = useState<Set<number>>(new Set([1]))
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([questApi.getAll(), bossApi.getAll()])
      .then(([q, b]) => {
        setQuests(q)
        setBosses(b)
        // Auto-open chapter with the first unlocked incomplete quest
        const firstActiveChapter = q.find(quest => !quest.completed && !quest.locked)?.chapterNumber
        if (firstActiveChapter) setOpenChapters(new Set([firstActiveChapter]))
      })
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [])

  const questsDone = quests.filter(q => q.completed).length
  const bossesDone = bosses.filter(b => b.completed).length
  const totalItems = quests.length + bosses.length
  const progressPct = totalItems > 0 ? Math.round(((questsDone + bossesDone) / totalItems) * 100) : 0

  function toggleChapter(n: number) {
    setOpenChapters(prev => {
      const next = new Set(prev)
      next.has(n) ? next.delete(n) : next.add(n)
      return next
    })
  }

  function getChapterStatus(chapterNum: number): 'done' | 'active' | 'locked' {
    const chapterQuests = quests.filter(q => q.chapterNumber === chapterNum)
    const boss = bosses.find(b => b.chapterNumber === chapterNum)
    if (chapterQuests.length === 0) return 'locked'
    const allQuestsDone = chapterQuests.every(q => q.completed)
    const bossDone = boss?.completed ?? false
    if (allQuestsDone && bossDone) return 'done'
    const allLocked = chapterQuests.every(q => q.locked)
    if (allLocked) return 'locked'
    return 'active'
  }

  return (
    <div className={styles.page}>
      <div className={styles.hero}>
        <div className={styles.glyph}>🧙</div>
        <h1 className={styles.heroTitle}>The Java Grimoire</h1>
        <p className={styles.heroSub}>Your path from apprentice to Archmage</p>
        <div className={styles.overallProgress}>
          <div className={styles.progressLabel}>
            <span>Overall progress</span>
            <span>{progressPct}%</span>
          </div>
          <div className={styles.progressBar}>
            <div className={styles.progressFill} style={{ width: `${progressPct}%` }} />
          </div>
        </div>
      </div>

      <div className={styles.statsRow}>
        <div className={styles.statCard}><div className={styles.statVal}>{questsDone}</div><div className={styles.statLbl}>quests done</div></div>
        <div className={styles.statCard}><div className={styles.statVal}>{bossesDone}</div><div className={styles.statLbl}>bosses slain</div></div>
        <div className={styles.statCard}><div className={styles.statVal}>{user?.totalXp ?? 0}</div><div className={styles.statLbl}>total xp</div></div>
        <div className={styles.statCard}><div className={styles.statVal}>{user?.rank ?? '—'}</div><div className={styles.statLbl}>rank</div></div>
      </div>

      <div className={styles.pathWrap}>
        <div className={styles.pathLabel}>Learning path</div>

        {loading ? (
          <div className={styles.loading}>Consulting the Grimoire...</div>
        ) : (
          CHAPTER_META.map(meta => {
            const chapterQuests = quests
              .filter(q => q.chapterNumber === meta.number)
              .sort((a, b) => a.orderInChapter - b.orderInChapter)
            const boss = bosses.find(b => b.chapterNumber === meta.number)
            const status = getChapterStatus(meta.number)
            const isOpen = openChapters.has(meta.number)

            return (
              <div key={meta.number} className={`${styles.chapter} ${isOpen ? styles.open : ''}`}>
                <div className={styles.chHead} onClick={() => toggleChapter(meta.number)}>
                  <div className={`${styles.chIcon} ${styles[status]}`}>{meta.icon}</div>
                  <div className={styles.chMeta}>
                    <div className={styles.chTitle}>{meta.title}</div>
                    <div className={styles.chSub}>{meta.subtitle}</div>
                  </div>
                  <div className={styles.chRight}>
                    <span className={`chip ${status === 'done' ? 'chip-teal' : status === 'active' ? 'chip-purple' : 'chip-gray'}`}>
                      {status === 'done' ? 'Complete' : status === 'active' ? 'In progress' : 'Locked'}
                    </span>
                    <span className={styles.chevron}>{isOpen ? '▲' : '▼'}</span>
                  </div>
                </div>

                {isOpen && (
                  <div className={styles.lessons}>
                    {chapterQuests.map(q => (
                      <div
                        key={q.id}
                        className={`${styles.lessonRow} ${q.locked ? styles.lockedRow : ''}`}
                        onClick={() => !q.locked && navigate(`/quest/${q.id}`)}
                        title={q.locked ? 'Complete previous quests to unlock' : ''}
                      >
                        <div className={`${styles.dot}
                          ${q.completed ? styles.dotDone : q.locked ? styles.dotLocked : styles.dotActive}`}
                        />
                        <div className={styles.lessonInfo}>
                          <div className={styles.lessonName}>{q.title}</div>
                          <div className={styles.lessonTopic}>{q.topic}</div>
                        </div>
                        <div className={styles.lessonRight}>
                          {q.locked && <span className={styles.lockIcon}>🔒</span>}
                          {q.completed && <span className={styles.doneIcon}>✓</span>}
                          {!q.locked && !q.completed && <span className="chip chip-green" style={{ fontSize: 10 }}>+{q.xpReward} xp</span>}
                        </div>
                      </div>
                    ))}

                    {/* Boss row */}
                    {boss && (
                      <div
                        className={`${styles.lessonRow} ${styles.bossRow} ${boss.locked ? styles.lockedRow : ''}`}
                        onClick={() => !boss.locked && navigate(`/boss/${boss.id}`)}
                        title={boss.locked ? 'Complete all quests in this chapter to unlock the boss' : ''}
                      >
                        <div className={`${styles.dot} ${boss.completed ? styles.dotDone : boss.locked ? styles.dotLocked : styles.dotBoss}`} />
                        <div className={styles.lessonInfo}>
                          <div className={styles.lessonName}>
                            <span style={{ marginRight: 6 }}>{boss.glyph}</span>{boss.name}
                          </div>
                          <div className={styles.lessonTopic}>Boss Battle</div>
                        </div>
                        <div className={styles.lessonRight}>
                          {boss.locked && <span className={styles.lockIcon}>🔒</span>}
                          {boss.completed && <span className={styles.doneIcon}>✓</span>}
                          {!boss.locked && !boss.completed && <span className="chip chip-red" style={{ fontSize: 10 }}>+{boss.xpReward} xp</span>}
                        </div>
                      </div>
                    )}

                    {chapterQuests.length === 0 && !boss && (
                      <div className={styles.comingSoon}>Content coming soon...</div>
                    )}
                  </div>
                )}
              </div>
            )
          })
        )}
      </div>
    </div>
  )
}
