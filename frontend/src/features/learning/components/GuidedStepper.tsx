import { safe } from '@/lib/sanitize'
import { useState, useEffect, useCallback } from 'react'
import { cn } from '@/lib/utils'
import { guidedStepApi } from '@/shared/api/services'
import type { GuidedStepDto, GuidedStepCheckResponse } from '@/shared/types'
import { Eye, EyeOff, Check, X, Loader2, ChevronRight } from 'lucide-react'

import FillBlankInput  from './guided/FillBlankInput'
import McqInput        from './guided/McqInput'
import ShortTextInput  from './guided/ShortTextInput'
import CodeStepInput   from './guided/CodeStepInput'
import DragDropInput   from './guided/DragDropInput'
import SequenceInput   from './guided/SequenceInput'

interface Props {
  lessonId: string
  /** Called when every step has been passed — lets the parent advance the phase. */
  onAllComplete: () => void
}

// ── Step input dispatcher ─────────────────────────────────────────────────────

function StepInput({
  step, value, onChange, disabled,
}: {
  step: GuidedStepDto
  value: string
  onChange: (v: string) => void
  disabled: boolean
}) {
  const cfg = step.inputConfig ?? {}
  switch (step.inputType) {
    case 'FILL_BLANK':      return <FillBlankInput  value={value} onChange={onChange} disabled={disabled} config={cfg} />
    case 'MULTIPLE_CHOICE': return <McqInput        value={value} onChange={onChange} disabled={disabled} config={cfg} />
    case 'SHORT_TEXT':      return <ShortTextInput  value={value} onChange={onChange} disabled={disabled} config={cfg} />
    case 'CODE':            return <CodeStepInput   value={value} onChange={onChange} disabled={disabled} config={cfg} />
    case 'DRAG_DROP':       return <DragDropInput   value={value} onChange={onChange} disabled={disabled} config={cfg} />
    case 'SEQUENCE':        return <SequenceInput   value={value} onChange={onChange} disabled={disabled} config={cfg} />
    default:                return <ShortTextInput  value={value} onChange={onChange} disabled={disabled} config={cfg} />
  }
}

// ── Prose HTML class shared across step blocks ────────────────────────────────

const PROSE = [
  'text-[14px] leading-[1.75] text-text',
  '[&_p]:m-0 [&_p]:mb-2.5 [&_p:last-child]:mb-0',
  '[&_strong]:text-gold [&_strong]:font-semibold [&_em]:text-purple-light [&_em]:italic',
  '[&_code]:bg-[rgba(139,92,246,0.12)] [&_code]:border [&_code]:border-[rgba(139,92,246,0.2)] [&_code]:rounded [&_code]:px-1.5 [&_code]:py-px [&_code]:text-[12px] [&_code]:text-purple-light [&_code]:font-mono',
  '[&_pre]:bg-[#09070f] [&_pre]:border [&_pre]:border-[rgba(139,92,246,0.2)] [&_pre]:rounded-[8px] [&_pre]:overflow-x-auto [&_pre]:my-2.5',
  '[&_pre_code]:block [&_pre_code]:px-4 [&_pre_code]:py-3 [&_pre_code]:text-[13px] [&_pre_code]:leading-[1.7] [&_pre_code]:text-[#e2e8f0] [&_pre_code]:font-mono [&_pre_code]:bg-transparent [&_pre_code]:border-none',
].join(' ')

// ── Main component ────────────────────────────────────────────────────────────

export default function GuidedStepper({ lessonId, onAllComplete }: Props) {
  const [steps,       setSteps]       = useState<GuidedStepDto[]>([])
  const [currentIdx,  setCurrentIdx]  = useState(0)
  const [answer,      setAnswer]      = useState('')
  const [result,      setResult]      = useState<GuidedStepCheckResponse | null>(null)
  const [showHint,    setShowHint]    = useState(false)
  const [checking,    setChecking]    = useState(false)
  const [loadError,   setLoadError]   = useState(false)

  // Load steps on mount; resume at first incomplete step
  useEffect(() => {
    guidedStepApi.getSteps(lessonId)
      .then(loaded => {
        setSteps(loaded)
        const firstIncomplete = loaded.findIndex(s => !s.completed)
        setCurrentIdx(firstIncomplete >= 0 ? firstIncomplete : loaded.length - 1)
      })
      .catch(() => setLoadError(true))
  }, [lessonId])

  const currentStep = steps[currentIdx]

  // Reset answer/result when moving to a different step
  const goToStep = useCallback((idx: number) => {
    setCurrentIdx(idx)
    setAnswer('')
    setResult(null)
    setShowHint(false)
  }, [])

  const markStepComplete = useCallback((stepId: string) => {
    setSteps(prev => prev.map(s => s.id === stepId ? { ...s, completed: true } : s))
  }, [])

  async function handleCheck() {
    if (!currentStep || checking) return
    setChecking(true)
    try {
      const res = await guidedStepApi.checkStep(lessonId, currentStep.id, answer)
      setResult(res)
      if (res.passed) {
        markStepComplete(currentStep.id)
      }
    } catch {
      setResult({ passed: false, feedback: 'Error connecting — please try again.', reflectionPrompt: null, nextStepId: null })
    } finally {
      setChecking(false)
    }
  }

  function handleNext() {
    if (currentIdx < steps.length - 1) {
      goToStep(currentIdx + 1)
    } else {
      onAllComplete()
    }
  }

  function handleKeyDown(e: React.KeyboardEvent) {
    if (e.key === 'Enter' && !e.shiftKey && currentStep?.inputType !== 'CODE' && !result?.passed) {
      e.preventDefault()
      handleCheck()
    }
  }

  // ── Edge cases ──────────────────────────────────────────────────────────────

  if (loadError) {
    return (
      <div className="p-5 bg-card border border-border rounded-[12px] text-center">
        <p className="text-muted text-[13px]">Could not load guided steps. Please refresh the page.</p>
      </div>
    )
  }

  if (steps.length === 0) {
    return (
      <div className="p-5 flex items-center justify-center">
        <Loader2 size={20} className="animate-spin text-muted" />
      </div>
    )
  }

  if (!currentStep) return null

  const allDone = steps.every(s => s.completed)
  const isPassed = result?.passed === true

  return (
    <div className="flex flex-col gap-4 max-w-[700px] mx-auto" onKeyDown={handleKeyDown}>

      {/* ── Progress bar ─────────────────────────────────────────────────────── */}
      <div className="flex items-center gap-2">
        <div className="flex gap-1.5 flex-1">
          {steps.map((s, i) => (
            <button
              key={s.id}
              type="button"
              onClick={() => s.completed && goToStep(i)}
              className={cn(
                'h-1.5 rounded-full flex-1 transition-colors duration-200',
                s.completed ? 'bg-teal cursor-pointer' :
                i === currentIdx ? 'bg-purple' : 'bg-border cursor-default',
              )}
              title={s.completed ? `Step ${i + 1} — completed` : `Step ${i + 1}`}
            />
          ))}
        </div>
        <span className="text-[11px] text-muted whitespace-nowrap flex-shrink-0">
          {currentIdx + 1} / {steps.length}
        </span>
      </div>

      {/* ── Step card ──────────────────────────────────────────────────────────── */}
      <div className="bg-card border border-border rounded-[12px] p-5 flex flex-col gap-4">

        {/* Instruction */}
        <div
          className={cn(PROSE, 'mb-1')}
          dangerouslySetInnerHTML={safe(currentStep.instructionHtml ?? '')}
        />

        {/* Input */}
        <StepInput
          step={currentStep}
          value={answer}
          onChange={setAnswer}
          disabled={isPassed || currentStep.completed}
        />

        {/* Feedback */}
        {result && (
          <div className={cn(
            'p-3.5 rounded-[8px] border text-[13px] leading-[1.6]',
            result.passed
              ? 'bg-[rgba(45,212,191,0.06)] border-teal text-teal'
              : 'bg-[rgba(248,113,113,0.06)] border-[rgba(248,113,113,0.4)] text-[#f87171]',
          )}>
            <div className="flex items-start gap-2">
              {result.passed
                ? <Check size={15} strokeWidth={2.5} className="mt-0.5 flex-shrink-0" />
                : <X     size={15} strokeWidth={2.5} className="mt-0.5 flex-shrink-0" />}
              <span>{result.feedback}</span>
            </div>
          </div>
        )}

        {/* Reflection (on pass) */}
        {isPassed && result.reflectionPrompt && (
          <div className="px-4 py-3 rounded-[8px] border border-[rgba(139,92,246,0.25)] bg-[rgba(139,92,246,0.06)]">
            <div className="text-[11px] font-bold text-purple-light uppercase tracking-[0.08em] mb-1.5">
              Reflect
            </div>
            <div
              className={PROSE}
              dangerouslySetInnerHTML={safe(result.reflectionPrompt)}
            />
          </div>
        )}

        {/* Hint toggle (shown when not yet passed) */}
        {currentStep.hintHtml && !isPassed && !currentStep.completed && (
          <div>
            <button
              type="button"
              className="btn btn-ghost text-[12px] px-2.5 py-1.5 flex items-center gap-1.5"
              onClick={() => setShowHint(h => !h)}
            >
              {showHint
                ? <><EyeOff size={13} strokeWidth={1.75} /> Hide hint</>
                : <><Eye    size={13} strokeWidth={1.75} /> Show hint</>}
            </button>
            {showHint && (
              <div className="mt-2 p-3.5 rounded-[8px] border border-dashed border-[rgba(139,92,246,0.35)] bg-[rgba(139,92,246,0.05)]">
                <div
                  className={PROSE}
                  dangerouslySetInnerHTML={safe(currentStep.hintHtml)}
                />
              </div>
            )}
          </div>
        )}

        {/* Action buttons */}
        <div className="flex items-center gap-2.5 flex-wrap">
          {!isPassed && !currentStep.completed ? (
            <button
              className="btn btn-primary flex items-center gap-1.5 text-[13px]"
              onClick={handleCheck}
              disabled={checking || !answer.trim()}
            >
              {checking
                ? <><Loader2 size={13} className="animate-spin" /> Checking…</>
                : 'Check answer'}
            </button>
          ) : (
            <button
              className="btn btn-primary flex items-center gap-1.5 text-[13px]"
              onClick={handleNext}
            >
              {currentIdx < steps.length - 1 ? (
                <><ChevronRight size={14} strokeWidth={2} /> Next step</>
              ) : (
                <><Check size={13} strokeWidth={2.5} /> {allDone ? 'All steps done — continue' : 'Continue'}</>
              )}
            </button>
          )}
        </div>
      </div>
    </div>
  )
}
