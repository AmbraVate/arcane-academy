// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import styles from './TopicSelectPage.module.css'

interface Topic {
  id: string
  icon: string
  name: string
  description: string
  available: boolean
  questCount: number
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

export default function TopicSelectPage() {
  const { user } = useAuth()
  const navigate = useNavigate()

  function handleSelect(topic: Topic) {
    if (!topic.available) return
    // Store chosen topic and go to the learning path
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
        {TOPICS.map(topic => (
          <div
            key={topic.id}
            className={`${styles.card} ${topic.available ? styles.available : styles.locked}`}
            onClick={() => handleSelect(topic)}
          >
            <div className={styles.cardIcon}>{topic.icon}</div>
            <div className={styles.cardName}>{topic.name}</div>
            <div className={styles.cardDesc}>{topic.description}</div>
            <div className={styles.cardFooter}>
              {topic.available ? (
                <>
                  <span className="chip chip-green">{topic.questCount} quests</span>
                  <span className={`chip chip-purple ${styles.startChip}`}>Begin →</span>
                </>
              ) : (
                <span className="chip chip-gray">Coming soon</span>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
