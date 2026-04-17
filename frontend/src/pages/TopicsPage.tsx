import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { dashboardApi } from '../api/services'
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
    id: 'tailwind',
    name: 'Tailwind CSS',
    glyph: '🎨',
    tagline: 'Compose beautiful interfaces with utility classes — no more naming paralysis.',
    status: 'active',
    chunks: 1,
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
    glyph: '🖌️',
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

function ProgressRing({ pct, active }: { pct: number; active: boolean }) {
  const r = 22
  const circ = 2 * Math.PI * r
  const dash = (pct / 100) * circ

  return (
    <svg className={styles.ring} width="56" height="56" viewBox="0 0 56 56">
      <circle cx="28" cy="28" r={r} className={styles.ringTrack} />
      <circle
        cx="28" cy="28" r={r}
        className={styles.ringFill}
        strokeDasharray={`${dash} ${circ}`}
        strokeDashoffset="0"
        transform="rotate(-90 28 28)"
        style={{ opacity: active ? 1 : 0.3 }}
      />
      <text x="28" y="28" className={styles.ringLabel} dominantBaseline="central" textAnchor="middle">
        {Math.round(pct)}%
      </text>
    </svg>
  )
}

export default function TopicsPage() {
  const navigate = useNavigate()
  const [javaProgress, setJavaProgress] = useState(0)

  const [tailwindProgress, setTailwindProgress] = useState(0)

  useEffect(() => {
    dashboardApi.get('java')
      .then(d => setJavaProgress(Math.round(d.overallProgress * 100)))
      .catch(() => {})
    dashboardApi.get('tailwind')
      .then(d => setTailwindProgress(Math.round(d.overallProgress * 100)))
      .catch(() => {})
  }, [])

  function progressFor(topic: Topic) {
    if (topic.id === 'java') return javaProgress
    if (topic.id === 'tailwind') return tailwindProgress
    return 0
  }

  function handleTopicClick(topic: Topic) {
    if (topic.status !== 'active') return
    if (topic.id === 'java') navigate('/')
    else navigate(`/topic/${topic.id}`)
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
              <div className={styles.cardTopRight}>
                <ProgressRing pct={progressFor(topic)} active={topic.status === 'active'} />
                {topic.status === 'active' ? (
                  <span className={`${styles.badge} ${styles.badgeActive}`}>Active</span>
                ) : (
                  <span className={`${styles.badge} ${styles.badgeSoon}`}>Coming Soon</span>
                )}
              </div>
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
