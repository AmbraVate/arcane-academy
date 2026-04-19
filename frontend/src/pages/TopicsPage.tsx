import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { dashboardApi } from '../api/services'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'

interface Topic {
  id: string
  name: string
  glyph: string
  tagline: string
  status: 'active' | 'coming_soon'
  chunks: number
  accentStroke: string
}

const TOPICS: Topic[] = [
  { id: 'java',       name: 'Java',           glyph: '☕', tagline: 'From zero to job-ready. The complete apprentice-to-archmage pathway.',          status: 'active',      chunks: 11, accentStroke: 'var(--teal)' },
  { id: 'tailwind',   name: 'Tailwind CSS',   glyph: '🎨', tagline: 'Compose beautiful interfaces with utility classes — no more naming paralysis.',  status: 'active',      chunks: 1,  accentStroke: 'var(--teal)' },
  { id: 'html',       name: 'HTML',           glyph: '📄', tagline: 'The structure of the web. Learn to author the skeleton of every page.',          status: 'coming_soon', chunks: 8,  accentStroke: 'var(--orange)' },
  { id: 'css',        name: 'CSS',            glyph: '🖌️', tagline: 'Craft beautiful, responsive interfaces from the ground up.',                     status: 'coming_soon', chunks: 10, accentStroke: 'var(--purple)' },
  { id: 'javascript', name: 'JavaScript',     glyph: '⚡', tagline: 'Bring the web to life. Logic, events, async, and the DOM.',                     status: 'coming_soon', chunks: 14, accentStroke: 'var(--gold)' },
  { id: 'python',     name: 'Python',         glyph: '🐍', tagline: 'Versatile, readable, powerful. Data, scripts, and automation.',                 status: 'coming_soon', chunks: 12, accentStroke: 'var(--teal)' },
  { id: 'sql',        name: 'SQL',            glyph: '🗃️', tagline: 'Query, transform, and model data with precision.',                              status: 'coming_soon', chunks: 9,  accentStroke: 'var(--purple)' },
  { id: 'typescript', name: 'TypeScript',     glyph: '🔷', tagline: 'JavaScript with discipline. Types, interfaces, and confidence at scale.',       status: 'coming_soon', chunks: 10, accentStroke: 'var(--gold)' },
  { id: 'react',      name: 'React',          glyph: '⚛️', tagline: 'Component-driven UIs. Hooks, state, and the modern frontend.',                  status: 'coming_soon', chunks: 12, accentStroke: 'var(--teal)' },
]

function ProgressRing({ pct, active, stroke }: { pct: number; active: boolean; stroke: string }) {
  const r = 22
  const circ = 2 * Math.PI * r
  const dash = (pct / 100) * circ
  return (
    <svg width="56" height="56" viewBox="0 0 56 56" className="block overflow-visible">
      <circle cx="28" cy="28" r={r} fill="none" stroke="var(--border)" strokeWidth="3.5" />
      <circle
        cx="28" cy="28" r={r} fill="none"
        stroke={stroke} strokeWidth="3.5" strokeLinecap="round"
        strokeDasharray={`${dash} ${circ}`} strokeDashoffset="0"
        transform="rotate(-90 28 28)"
        style={{ opacity: active ? 1 : 0.3, transition: 'stroke-dasharray 0.6s ease' }}
      />
      <text x="28" y="28" dominantBaseline="central" textAnchor="middle"
        fontSize="9" fontFamily="'Cinzel', serif" fill="var(--muted)" fontWeight="600" letterSpacing="0.02em">
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
    dashboardApi.get('java').then(d => setJavaProgress(Math.round(d.overallProgress * 100))).catch(() => {})
    dashboardApi.get('tailwind').then(d => setTailwindProgress(Math.round(d.overallProgress * 100))).catch(() => {})
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
    <div className="max-w-[960px] mx-auto px-5 py-8 pb-[72px] overflow-y-auto max-[600px]:px-3 max-[600px]:py-5">
      <div className="text-center mb-10">
        <h1 className="font-cinzel text-[32px] font-bold text-gold m-0 mb-3 max-[600px]:text-[24px]">Choose Your Path</h1>
        <p className="text-[16px] text-muted leading-[1.7] max-w-[560px] mx-auto">
          Every polymath starts somewhere. Select a discipline to begin mastering it — or continue where you left off.
        </p>
      </div>

      <div className="grid gap-4 mb-12 max-[600px]:gap-2.5" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))' }}>
        {TOPICS.map(topic => {
          const active = topic.status === 'active'
          return (
            <div
              key={topic.id}
              className={cn(
                'bg-card border border-border rounded-[14px] px-5 py-6 pb-5 flex flex-col gap-2.5',
                'relative overflow-hidden transition-[border-color,transform,box-shadow] duration-200',
                'before:absolute before:top-0 before:left-0 before:right-0 before:h-0.5 before:opacity-0 before:transition-opacity before:duration-200',
                active
                  ? 'cursor-pointer hover:-translate-y-[3px] hover:shadow-[0_8px_32px_rgba(0,0,0,0.3)] hover:before:opacity-100'
                  : 'cursor-default opacity-55 saturate-50',
                active && topic.accentStroke === 'var(--teal)' && 'hover:border-[rgba(45,212,191,0.5)]',
              )}
              style={active ? { '--tw-before-bg': topic.accentStroke } as React.CSSProperties : undefined}
              onClick={() => handleTopicClick(topic)}
            >
              <div className="flex items-start justify-between mb-1">
                <span className="text-[36px] leading-none max-[600px]:text-[28px]">{topic.glyph}</span>
                <div className="flex flex-col items-end gap-1.5">
                  <ProgressRing pct={progressFor(topic)} active={active} stroke={topic.accentStroke} />
                  <Badge variant={active ? 'active' : 'soon'}>{active ? 'Active' : 'Coming Soon'}</Badge>
                </div>
              </div>

              <div className="font-cinzel text-[20px] font-bold text-text max-[600px]:text-[16px]">{topic.name}</div>
              <div className="text-[13px] text-muted leading-[1.6] flex-1">{topic.tagline}</div>

              <div className="flex items-center justify-between pt-2.5 border-t border-border mt-auto">
                <span className="text-[11px] text-muted font-cinzel">{topic.chunks} knowledge chunks</span>
                {active && <span className="text-[13px] text-teal font-semibold">Continue →</span>}
              </div>
            </div>
          )
        })}
      </div>

      <div className="flex items-start gap-4 bg-card border border-border border-l-[3px] border-l-gold rounded-[10px] px-6 py-5">
        <span className="text-[20px] text-gold flex-shrink-0 mt-0.5">✦</span>
        <p className="text-[14px] text-muted leading-[1.7] m-0">
          A polymath doesn't specialise in one thing — they build deep mastery across many disciplines.
          Each topic you complete expands your ability to connect ideas across domains.
        </p>
      </div>
    </div>
  )
}
