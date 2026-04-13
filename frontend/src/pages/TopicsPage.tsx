import { useNavigate } from 'react-router-dom'
import styles from './TopicsPage.module.css'

interface Topic {
  id: string
  name: string
  glyph: string
  tagline: string
  status: 'active' | 'coming_soon'
  chunks: number
  color: 'teal' | 'purple' | 'gold' | 'orange' | 'muted'
}

const TOPICS: Topic[] = [
  {
    id: 'java',
    name: 'Java',
    glyph: '☕',
    tagline: 'From zero to job-ready. The complete apprentice-to-archmage pathway.',
    status: 'active',
    chunks: 11,
    color: 'teal',
  },
  {
    id: 'html',
    name: 'HTML',
    glyph: '📄',
    tagline: 'The structure of the web. Learn to author the skeleton of every page.',
    status: 'coming_soon',
    chunks: 8,
    color: 'orange',
  },
  {
    id: 'css',
    name: 'CSS',
    glyph: '🎨',
    tagline: 'Craft beautiful, responsive interfaces from the ground up.',
    status: 'coming_soon',
    chunks: 10,
    color: 'purple',
  },
  {
    id: 'javascript',
    name: 'JavaScript',
    glyph: '⚡',
    tagline: 'Bring the web to life. Logic, events, async, and the DOM.',
    status: 'coming_soon',
    chunks: 14,
    color: 'gold',
  },
  {
    id: 'python',
    name: 'Python',
    glyph: '🐍',
    tagline: 'Versatile, readable, powerful. Data, scripts, and automation.',
    status: 'coming_soon',
    chunks: 12,
    color: 'teal',
  },
  {
    id: 'sql',
    name: 'SQL',
    glyph: '🗃️',
    tagline: 'Query, transform, and model data with precision.',
    status: 'coming_soon',
    chunks: 9,
    color: 'purple',
  },
  {
    id: 'typescript',
    name: 'TypeScript',
    glyph: '🔷',
    tagline: 'JavaScript with discipline. Types, interfaces, and confidence at scale.',
    status: 'coming_soon',
    chunks: 10,
    color: 'gold',
  },
  {
    id: 'react',
    name: 'React',
    glyph: '⚛️',
    tagline: 'Component-driven UIs. Hooks, state, and the modern frontend.',
    status: 'coming_soon',
    chunks: 12,
    color: 'teal',
  },
]

export default function TopicsPage() {
  const navigate = useNavigate()

  function handleTopicClick(topic: Topic) {
    if (topic.status === 'active') navigate('/')
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <h1 className={styles.title}>Choose Your Path</h1>
        <p className={styles.subtitle}>
          Every polymath starts somewhere. Select a discipline to begin mastering it — or continue where you left off.
        </p>
      </div>

      <div className={styles.grid}>
        {TOPICS.map(topic => (
          <div
            key={topic.id}
            className={`${styles.card} ${styles[`color${topic.color}`]} ${topic.status === 'coming_soon' ? styles.cardSoon : styles.cardActive}`}
            onClick={() => handleTopicClick(topic)}
          >
            <div className={styles.cardTop}>
              <span className={styles.topicGlyph}>{topic.glyph}</span>
              {topic.status === 'active' ? (
                <span className={`${styles.badge} ${styles.badgeActive}`}>Active</span>
              ) : (
                <span className={`${styles.badge} ${styles.badgeSoon}`}>Coming Soon</span>
              )}
            </div>

            <div className={styles.topicName}>{topic.name}</div>
            <div className={styles.topicTagline}>{topic.tagline}</div>

            <div className={styles.cardFooter}>
              <span className={styles.chunkCount}>{topic.chunks} knowledge chunks</span>
              {topic.status === 'active' && (
                <span className={styles.ctaArrow}>Continue →</span>
              )}
            </div>
          </div>
        ))}
      </div>

      <div className={styles.polymathNote}>
        <span className={styles.polymathGlyph}>✦</span>
        <p>
          A polymath doesn't specialise in one thing — they build deep mastery across many disciplines.
          Each topic you complete expands your ability to connect ideas across domains.
        </p>
      </div>
    </div>
  )
}
