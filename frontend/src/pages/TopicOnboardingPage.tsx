import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { diagnosticApi } from '../api/services'
import { CSS_PREREQ_TOPICS, prereqStorageKey } from '../data/cssPrimer'

const TOPIC_META: Record<string, { name: string; glyph: string; question: string }> = {
  java:     { name: 'Java',         glyph: '☕', question: 'Have you written Java code before?' },
  tailwind: { name: 'Tailwind CSS', glyph: '🎨', question: 'Have you used Tailwind CSS before?' },
  react:    { name: 'React',        glyph: '⚛️', question: 'Have you built UIs with React before?' },
  sql:      { name: 'SQL',          glyph: '🗃️', question: 'Have you written SQL queries before?' },
}

export default function TopicOnboardingPage() {
  const { topicId } = useParams<{ topicId: string }>()
  const navigate = useNavigate()
  const { user } = useAuth()
  const [loading, setLoading] = useState(false)

  const meta = TOPIC_META[topicId ?? ''] ?? { name: topicId, glyph: '📖', question: `Have you studied ${topicId} before?` }

  /** Has this user already passed/completed the CSS prereq check for this topic? */
  function prereqAlreadyDone(): boolean {
    if (!user || !topicId || !CSS_PREREQ_TOPICS.has(topicId)) return true
    return !!localStorage.getItem(prereqStorageKey(user.userId, topicId))
  }

  async function handleNew() {
    setLoading(true)
    try {
      await diagnosticApi.skip(topicId!)
      // For CSS-dependent topics, route through the prereq check on first visit
      if (CSS_PREREQ_TOPICS.has(topicId!) && !prereqAlreadyDone()) {
        navigate(`/topic/${topicId}/prereq-check`)
      } else {
        navigate(`/topic/${topicId}`, { replace: true })
      }
    } catch { setLoading(false) }
  }

  function handleExperienced() { navigate(`/topic/${topicId}/diagnostic`) }

  return (
    <div className="flex-1 flex items-center justify-center px-6 py-10">
      <div className="max-w-[640px] w-full text-center animate-[fade-up_0.5s_cubic-bezier(0.22,1,0.36,1)_both]">
        <div className="text-[56px] mb-4">{meta.glyph}</div>
        <h1 className="text-[28px] font-bold text-gold m-0 mb-2.5">{meta.name}</h1>
        <p className="text-[15px] text-muted leading-[1.6] m-0 mb-8">
          Before we chart your path through {meta.name}, let us know where you stand.
        </p>
        <p className="text-[20px] font-bold text-text m-0 mb-6">{meta.question}</p>

        <div className="flex gap-4 max-[520px]:flex-col">
          <button
            className="flex-1 flex flex-col items-center gap-2.5 px-5 py-7 bg-card border border-[rgba(0,200,150,0.3)]
              rounded-[16px] cursor-pointer shadow-[0_0_30px_rgba(0,200,150,0.06)] text-center
              transition-[transform,border-color,box-shadow] duration-150
              hover:enabled:-translate-y-[3px] hover:enabled:border-teal hover:enabled:shadow-[0_6px_30px_rgba(0,200,150,0.15)]
              disabled:opacity-60 disabled:cursor-not-allowed"
            onClick={handleNew}
            disabled={loading}
          >
            <span className="text-[36px]">🌱</span>
            <span className="text-[16px] font-bold text-teal">No, I'm completely new</span>
            <span className="text-[13px] text-muted leading-[1.5]">
              Start from the very beginning. We'll guide you through every concept step by step.
            </span>
          </button>

          <button
            className="flex-1 flex flex-col items-center gap-2.5 px-5 py-7 bg-card border border-[rgba(139,92,246,0.3)]
              rounded-[16px] cursor-pointer shadow-[0_0_30px_rgba(139,92,246,0.06)] text-center
              transition-[transform,border-color,box-shadow] duration-150
              hover:enabled:-translate-y-[3px] hover:enabled:border-purple hover:enabled:shadow-[0_6px_30px_rgba(139,92,246,0.15)]
              disabled:opacity-60 disabled:cursor-not-allowed"
            onClick={handleExperienced}
            disabled={loading}
          >
            <span className="text-[36px]">⚡</span>
            <span className="text-[16px] font-bold text-purple-light">Yes, I have some experience</span>
            <span className="text-[13px] text-muted leading-[1.5]">
              Take a short diagnostic — we'll skip what you already know and focus on the gaps.
            </span>
          </button>
        </div>
      </div>
    </div>
  )
}
