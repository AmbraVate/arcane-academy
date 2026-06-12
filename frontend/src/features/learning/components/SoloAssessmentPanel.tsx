import { safe } from '@/lib/sanitize'
/**
 * SoloAssessmentPanel — Phase 4
 *
 * Renders the appropriate solo-practice UI based on the lesson's soloAssessmentType:
 *   - RUBRIC_REFLECTION — text area + rubric checklist + confidence selector + exemplar reveal
 *   - PATTERN_MATCH     — text area + keyword-band feedback
 *   - AI_REVIEW         — text area + AI quota indicator + AI feedback
 *
 * DETERMINISTIC solo practice continues to use the existing code-editor path in EncodingPage.
 */
import { useState } from 'react'
import { encodingApi } from '@/shared/api/services'
import type { LessonEncoding, SoloAssessmentResult, KeywordBand } from '@/shared/types'
import { cn } from '@/lib/utils'
import {
  CheckSquare, Square, ChevronDown, ChevronUp, Sparkles,
  Loader2, Zap, CheckCircle2, AlertCircle, Brain,
} from 'lucide-react'

// ── types ─────────────────────────────────────────────────────────────────────

type Confidence = 'NOT_CONFIDENT' | 'SOMEWHAT' | 'CONFIDENT' | 'VERY_CONFIDENT'

const CONFIDENCE_LABELS: Record<Confidence, string> = {
  NOT_CONFIDENT:  "Not confident — I struggled",
  SOMEWHAT:       "Somewhat — I got most of it",
  CONFIDENT:      "Confident — I understand this",
  VERY_CONFIDENT: "Very confident — I could teach it",
}

interface Props {
  encoding: LessonEncoding
  onSolved: (xpEarned: number, badges: SoloAssessmentResult['newBadges']) => void
  onAdvance: () => void
}

// ── component ─────────────────────────────────────────────────────────────────

export default function SoloAssessmentPanel({ encoding, onSolved, onAdvance }: Props) {
  const type = encoding.soloAssessmentType

  if (type === 'RUBRIC_REFLECTION') {
    return (
      <RubricReflectionPanel encoding={encoding} onSolved={onSolved} onAdvance={onAdvance} />
    )
  }
  if (type === 'PATTERN_MATCH') {
    return (
      <PatternMatchPanel encoding={encoding} onSolved={onSolved} onAdvance={onAdvance} />
    )
  }
  if (type === 'AI_REVIEW') {
    return (
      <AiReviewPanel encoding={encoding} onSolved={onSolved} onAdvance={onAdvance} />
    )
  }
  // DETERMINISTIC or null — caller handles via code editor path
  return null
}

// ── RUBRIC_REFLECTION ─────────────────────────────────────────────────────────

function RubricReflectionPanel({ encoding, onSolved, onAdvance }: Props) {
  const [answer, setAnswer] = useState('')
  const [checked, setChecked] = useState<Set<string>>(new Set())
  const [confidence, setConfidence] = useState<Confidence | null>(null)
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SoloAssessmentResult | null>(null)
  const [showModel, setShowModel] = useState(false)

  const rubric = encoding.rubricItems ?? []

  function toggleItem(item: string) {
    setChecked(prev => {
      const next = new Set(prev)
      next.has(item) ? next.delete(item) : next.add(item)
      return next
    })
  }

  async function handleSubmit() {
    if (submitting || !confidence) return
    setSubmitting(true)
    try {
      const res = await encodingApi.submitSoloAssessment(encoding.lessonId, {
        answer,
        checkedRubricItems: [...checked],
        confidence,
      })
      setResult(res)
      onSolved(res.xpEarned, res.newBadges)
    } catch {
      /* ignore */
    } finally {
      setSubmitting(false)
    }
  }

  if (result) {
    return (
      <div className="space-y-4">
        {/* Feedback */}
        <div className="p-4 rounded-[10px] border border-teal/30 bg-teal/5">
          <div className="flex items-center gap-2 mb-2">
            <CheckCircle2 size={16} className="text-teal flex-shrink-0" />
            <span className="text-[13px] font-bold text-teal">Solo Practice Complete!</span>
          </div>
          {result.feedback && (
            <p className="text-[13px] text-text leading-[1.65] italic">{result.feedback}</p>
          )}
        </div>

        {/* Model answer reveal */}
        {result.modelAnswerHtml && (
          <div className="border border-border rounded-[10px] overflow-hidden">
            <button
              className="w-full flex items-center justify-between px-4 py-3 bg-surface text-[13px] font-semibold text-text hover:bg-surface/80 transition-colors"
              onClick={() => setShowModel(v => !v)}
            >
              <span className="flex items-center gap-2">
                <Brain size={14} strokeWidth={2} className="text-purple-light" />
                Model Answer
              </span>
              {showModel ? <ChevronUp size={14} /> : <ChevronDown size={14} />}
            </button>
            {showModel && (
              <div
                className="px-4 py-4 text-[13px] leading-[1.75] text-text border-t border-border
                  [&_p]:mb-3 [&_p:last-child]:mb-0
                  [&_strong]:text-gold [&_strong]:font-semibold [&_em]:text-purple-light
                  [&_code]:bg-[rgba(139,92,246,0.12)] [&_code]:rounded [&_code]:px-1.5 [&_code]:py-px [&_code]:text-[12px] [&_code]:text-purple-light [&_code]:font-mono
                  [&_pre]:bg-[#09070f] [&_pre]:border [&_pre]:border-[rgba(139,92,246,0.2)] [&_pre]:rounded-[8px] [&_pre]:overflow-x-auto [&_pre]:my-3
                  [&_pre_code]:block [&_pre_code]:px-4 [&_pre_code]:py-3 [&_pre_code]:text-[13px] [&_pre_code]:leading-[1.7] [&_pre_code]:text-[#e2e8f0] [&_pre_code]:font-mono [&_pre_code]:bg-transparent [&_pre_code]:border-none
                  [&_ul]:pl-4 [&_ul]:mb-3 [&_li]:mb-1.5"
                dangerouslySetInnerHTML={safe(result.modelAnswerHtml)}
              />
            )}
          </div>
        )}

        <button className="btn btn-primary" onClick={onAdvance}>
          Continue to Retrieval Check →
        </button>
      </div>
    )
  }

  return (
    <div className="space-y-5">
      {/* Answer textarea */}
      <div>
        <label className="block text-[12px] font-semibold text-muted mb-1.5">Your solution</label>
        <textarea
          className={cn(
            "w-full h-[160px] p-3 rounded-[8px] bg-bg border border-border text-text text-[13px] leading-[1.7] resize-y focus:outline-none focus:border-purple/60",
            encoding.practiceType !== 'NONE' && "font-mono"
          )}
          placeholder={encoding.practiceType === 'NONE' ? "Type your response here…" : "Paste or type your code here…"}
          value={answer}
          onChange={e => setAnswer(e.target.value)}
        />
      </div>

      {/* Rubric checklist */}
      {rubric.length > 0 && (
        <div>
          <div className="text-[12px] font-bold text-muted uppercase tracking-[0.08em] mb-2.5 flex items-center gap-1.5">
            <CheckSquare size={12} strokeWidth={2} /> Self-check — mark what you completed
          </div>
          <div className="space-y-2">
            {rubric.map(item => (
              <button
                key={item}
                className={cn(
                  'w-full flex items-start gap-2.5 px-3 py-2.5 rounded-[8px] text-left transition-colors',
                  'border text-[13px] leading-[1.5]',
                  checked.has(item)
                    ? 'border-teal/40 bg-teal/8 text-teal'
                    : 'border-border bg-surface text-text hover:border-border/70',
                )}
                onClick={() => toggleItem(item)}
              >
                {checked.has(item)
                  ? <CheckSquare size={14} strokeWidth={2} className="flex-shrink-0 mt-0.5 text-teal" />
                  : <Square size={14} strokeWidth={1.5} className="flex-shrink-0 mt-0.5 text-muted" />}
                {item}
              </button>
            ))}
          </div>
        </div>
      )}

      {/* Confidence selector */}
      <div>
        <div className="text-[12px] font-bold text-muted uppercase tracking-[0.08em] mb-2.5">
          How confident do you feel?
        </div>
        <div className="grid grid-cols-2 gap-2 max-[480px]:grid-cols-1">
          {(Object.entries(CONFIDENCE_LABELS) as [Confidence, string][]).map(([val, label]) => (
            <button
              key={val}
              className={cn(
                'px-3 py-2.5 rounded-[8px] text-[12px] text-left leading-[1.4] border transition-colors',
                confidence === val
                  ? 'border-purple/50 bg-purple/10 text-purple-light font-semibold'
                  : 'border-border bg-surface text-muted hover:border-border/70',
              )}
              onClick={() => setConfidence(val)}
            >
              {label}
            </button>
          ))}
        </div>
      </div>

      <button
        className="btn btn-primary flex items-center gap-1.5"
        disabled={submitting || !confidence}
        onClick={handleSubmit}
      >
        {submitting ? <><Loader2 size={14} className="animate-spin" /> Submitting…</> : <><Zap size={14} /> Submit & reveal model answer</>}
      </button>
    </div>
  )
}

// ── PATTERN_MATCH ─────────────────────────────────────────────────────────────

function PatternMatchPanel({ encoding, onSolved, onAdvance }: Props) {
  const [answer, setAnswer] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SoloAssessmentResult | null>(null)
  const [showModel, setShowModel] = useState(false)

  async function handleSubmit() {
    if (submitting || !answer.trim()) return
    setSubmitting(true)
    try {
      const res = await encodingApi.submitSoloAssessment(encoding.lessonId, { answer })
      setResult(res)
      if (res.passed) onSolved(res.xpEarned, res.newBadges)
    } catch {
      /* ignore */
    } finally {
      setSubmitting(false)
    }
  }

  const bandColor = (band: KeywordBand | null) => {
    if (band === 'EXCELLENT') return 'text-teal border-teal/30 bg-teal/5'
    if (band === 'GOOD')      return 'text-purple-light border-purple/30 bg-purple/5'
    return 'text-gold border-gold/30 bg-gold/5'
  }

  if (result) {
    return (
      <div className="space-y-4">
        {/* Band chip */}
        {result.band && (
          <div className={cn('inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-[12px] font-bold border', bandColor(result.band))}>
            {result.band === 'EXCELLENT' && <CheckCircle2 size={13} />}
            {result.band === 'GOOD'      && <CheckCircle2 size={13} />}
            {result.band === 'WEAK'      && <AlertCircle  size={13} />}
            {result.band} — {result.passed ? 'Passed' : 'Try again'}
          </div>
        )}

        {/* Feedback */}
        {result.feedback && (
          <div className={cn('p-4 rounded-[10px] border',
            result.passed ? 'border-teal/30 bg-teal/5' : 'border-gold/30 bg-gold/5')}>
            <p className="text-[13px] text-text leading-[1.65] italic">{result.feedback}</p>
          </div>
        )}

        {/* Matched keywords */}
        {result.matchedKeywords && result.matchedKeywords.length > 0 && (
          <div>
            <div className="text-[11px] font-semibold text-muted uppercase tracking-[0.08em] mb-1.5">
              Keywords found in your answer
            </div>
            <div className="flex flex-wrap gap-1.5">
              {result.matchedKeywords.map(kw => (
                <span key={kw} className="px-2.5 py-0.5 rounded-full text-[11px] bg-teal/10 border border-teal/25 text-teal font-medium">
                  {kw}
                </span>
              ))}
            </div>
          </div>
        )}

        {/* Model answer reveal */}
        {result.modelAnswerHtml && (
          <div className="border border-border rounded-[10px] overflow-hidden">
            <button
              className="w-full flex items-center justify-between px-4 py-3 bg-surface text-[13px] font-semibold text-text hover:bg-surface/80 transition-colors"
              onClick={() => setShowModel(v => !v)}
            >
              <span className="flex items-center gap-2">
                <Brain size={14} strokeWidth={2} className="text-purple-light" />
                Model Answer
              </span>
              {showModel ? <ChevronUp size={14} /> : <ChevronDown size={14} />}
            </button>
            {showModel && (
              <div
                className="px-4 py-4 text-[13px] leading-[1.75] text-text border-t border-border
                  [&_p]:mb-3 [&_p:last-child]:mb-0 [&_strong]:text-gold [&_strong]:font-semibold
                  [&_code]:bg-[rgba(139,92,246,0.12)] [&_code]:rounded [&_code]:px-1.5 [&_code]:py-px [&_code]:text-[12px] [&_code]:text-purple-light [&_code]:font-mono
                  [&_pre]:bg-[#09070f] [&_pre]:border [&_pre]:border-[rgba(139,92,246,0.2)] [&_pre]:rounded-[8px] [&_pre]:overflow-x-auto [&_pre]:my-3
                  [&_pre_code]:block [&_pre_code]:px-4 [&_pre_code]:py-3 [&_pre_code]:text-[13px] [&_pre_code]:leading-[1.7] [&_pre_code]:text-[#e2e8f0] [&_pre_code]:font-mono [&_pre_code]:bg-transparent [&_pre_code]:border-none"
                dangerouslySetInnerHTML={safe(result.modelAnswerHtml)}
              />
            )}
          </div>
        )}

        {result.passed
          ? <button className="btn btn-primary" onClick={onAdvance}>Continue to Retrieval Check →</button>
          : <button className="btn btn-primary" onClick={() => { setResult(null); setAnswer('') }}>Try again</button>}
      </div>
    )
  }

  return (
    <div className="space-y-4">
      <div>
        <label className="block text-[12px] font-semibold text-muted mb-1.5">Your written answer</label>
        <textarea
          className="w-full h-[200px] p-3 rounded-[8px] bg-bg border border-border text-text text-[13px] leading-[1.7] resize-y focus:outline-none focus:border-purple/60"
          placeholder="Write your explanation in your own words…"
          value={answer}
          onChange={e => setAnswer(e.target.value)}
        />
      </div>
      <button
        className="btn btn-primary flex items-center gap-1.5"
        disabled={submitting || !answer.trim()}
        onClick={handleSubmit}
      >
        {submitting ? <><Loader2 size={14} className="animate-spin" /> Checking…</> : <><Zap size={14} /> Submit answer</>}
      </button>
    </div>
  )
}

// ── AI_REVIEW ─────────────────────────────────────────────────────────────────

function AiReviewPanel({ encoding, onSolved, onAdvance }: Props) {
  const [answer, setAnswer] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [result, setResult] = useState<SoloAssessmentResult | null>(null)
  const [showModel, setShowModel] = useState(false)

  const quotaRemaining = result?.aiReviewsRemaining ?? encoding.aiReviewsRemaining

  async function handleSubmit() {
    if (submitting || !answer.trim()) return
    setSubmitting(true)
    try {
      const res = await encodingApi.submitSoloAssessment(encoding.lessonId, { answer })
      setResult(res)
      if (res.passed) onSolved(res.xpEarned, res.newBadges)
    } catch {
      /* ignore */
    } finally {
      setSubmitting(false)
    }
  }

  if (result) {
    return (
      <div className="space-y-4">
        {/* AI badge */}
        {result.usedAi && (
          <div className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-[11px] bg-purple/10 border border-purple/30 text-purple-light font-medium">
            <Sparkles size={11} /> Reviewed by Master Velan (AI)
          </div>
        )}

        {/* Feedback */}
        <div className={cn('p-4 rounded-[10px] border',
          result.passed ? 'border-teal/30 bg-teal/5' : 'border-gold/30 bg-gold/5')}>
          <div className="flex items-center gap-2 mb-2">
            {result.passed
              ? <CheckCircle2 size={15} className="text-teal" />
              : <AlertCircle size={15} className="text-gold" />}
            <span className={cn('text-[13px] font-bold', result.passed ? 'text-teal' : 'text-gold')}>
              {result.passed ? 'Passed' : 'More depth needed'}
            </span>
          </div>
          {result.feedback && (
            <p className="text-[13px] text-text leading-[1.65] italic">{result.feedback}</p>
          )}
        </div>

        {/* Quota indicator */}
        {result.usedAi && (
          <p className="text-[11px] text-muted">
            AI reviews remaining this month: <strong className="text-text">{result.aiReviewsRemaining}</strong> / 3
          </p>
        )}

        {/* Model answer */}
        {result.modelAnswerHtml && (
          <div className="border border-border rounded-[10px] overflow-hidden">
            <button
              className="w-full flex items-center justify-between px-4 py-3 bg-surface text-[13px] font-semibold text-text hover:bg-surface/80 transition-colors"
              onClick={() => setShowModel(v => !v)}
            >
              <span className="flex items-center gap-2">
                <Brain size={14} strokeWidth={2} className="text-purple-light" /> Model Answer
              </span>
              {showModel ? <ChevronUp size={14} /> : <ChevronDown size={14} />}
            </button>
            {showModel && (
              <div
                className="px-4 py-4 text-[13px] leading-[1.75] text-text border-t border-border
                  [&_p]:mb-3 [&_p:last-child]:mb-0 [&_strong]:text-gold [&_strong]:font-semibold
                  [&_code]:bg-[rgba(139,92,246,0.12)] [&_code]:rounded [&_code]:px-1.5 [&_code]:py-px [&_code]:text-[12px] [&_code]:text-purple-light [&_code]:font-mono
                  [&_pre]:bg-[#09070f] [&_pre]:border [&_pre]:border-[rgba(139,92,246,0.2)] [&_pre]:rounded-[8px] [&_pre]:overflow-x-auto [&_pre]:my-3
                  [&_pre_code]:block [&_pre_code]:px-4 [&_pre_code]:py-3 [&_pre_code]:text-[13px] [&_pre_code]:leading-[1.7] [&_pre_code]:text-[#e2e8f0] [&_pre_code]:font-mono [&_pre_code]:bg-transparent [&_pre_code]:border-none"
                dangerouslySetInnerHTML={safe(result.modelAnswerHtml)}
              />
            )}
          </div>
        )}

        {result.passed
          ? <button className="btn btn-primary" onClick={onAdvance}>Continue to Retrieval Check →</button>
          : <button className="btn btn-primary" onClick={() => { setResult(null); setAnswer('') }}>Revise and resubmit</button>}
      </div>
    )
  }

  return (
    <div className="space-y-4">
      {/* Quota indicator */}
      <div className="flex items-center gap-2 p-3 rounded-[8px] border border-purple/20 bg-purple/5 text-[12px]">
        <Sparkles size={13} className="text-purple-light flex-shrink-0" />
        <span className="text-muted">
          Master Velan will review your answer.{' '}
          <strong className="text-text">{quotaRemaining} AI review{quotaRemaining !== 1 ? 's' : ''}</strong> remaining this month.
          {quotaRemaining === 0 && ' — will use self-check rubric instead.'}
        </span>
      </div>

      <div>
        <label className="block text-[12px] font-semibold text-muted mb-1.5">Your written answer</label>
        <textarea
          className="w-full h-[220px] p-3 rounded-[8px] bg-bg border border-border text-text text-[13px] leading-[1.7] resize-y focus:outline-none focus:border-purple/60"
          placeholder="Write a thorough response in your own words…"
          value={answer}
          onChange={e => setAnswer(e.target.value)}
        />
      </div>

      <button
        className="btn btn-primary flex items-center gap-1.5"
        disabled={submitting || !answer.trim()}
        onClick={handleSubmit}
      >
        {submitting
          ? <><Loader2 size={14} className="animate-spin" /> Consulting Master Velan…</>
          : <><Sparkles size={14} /> Submit for AI review</>}
      </button>
    </div>
  )
}
