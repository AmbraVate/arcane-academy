import { safe } from '@/lib/sanitize'
import { Check, X } from 'lucide-react'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import type { QuestionDto, QuestionResultDto } from '@/shared/types'

const OPTION_LETTERS = ['A', 'B', 'C', 'D', 'E']

interface Props {
  question: QuestionDto
  index: number
  answer: string
  onChange: (v: string) => void
  result?: QuestionResultDto
  disabled?: boolean
}

export default function QuestionCard({ question, index, answer, onChange, result, disabled }: Props) {
  const tierVariant =
    question.tier === 'RECALL' ? 'recall' :
    question.tier === 'APPLICATION' ? 'application' :
    question.tier === 'DISCRIMINATION' ? 'discrimination' : undefined

  const isChoice = question.type === 'MULTIPLE_CHOICE' || question.type === 'TRUE_FALSE'
  const options = question.type === 'TRUE_FALSE' ? ['True', 'False'] : (question.options ?? [])

  return (
    <div
      className={cn(
        'bg-card border rounded-[14px] p-5 pb-[22px] px-[22px] mb-4 relative transition-[border-color,box-shadow] duration-200',
        'before:absolute before:inset-0 before:rounded-[14px] before:bg-gradient-to-br before:from-purple/[0.04] before:to-transparent before:pointer-events-none',
        'focus-within:border-purple focus-within:shadow-[0_0_0_3px_rgba(139,92,246,0.12)]',
        result
          ? result.correct
            ? 'border-[rgba(74,222,128,0.4)] bg-gradient-to-b from-[rgba(74,222,128,0.04)] to-card'
            : 'border-[rgba(248,113,113,0.4)] bg-gradient-to-b from-[rgba(248,113,113,0.04)] to-card'
          : 'border-border'
      )}
    >
      {/* Header */}
      <div className="flex items-center gap-2 mb-3.5 flex-wrap">
        <span className="text-[11px] font-[800] tracking-[0.08em] text-muted uppercase min-w-[28px]">
          Q{index + 1}
        </span>
        {tierVariant && <Badge variant={tierVariant as 'recall' | 'application' | 'discrimination'}>{question.tier.charAt(0) + question.tier.slice(1).toLowerCase()}</Badge>}
        <span className="text-[10px] text-muted px-2 py-0.5 rounded-full border border-border bg-surface ml-auto max-[480px]:ml-0">
          {formatType(question.type)}
        </span>
      </div>

      {/* Question text */}
      <div
        className="text-[16px] leading-[1.7] text-text mb-4 font-medium
          [&_strong]:text-gold [&_em]:text-purple-light [&_em]:italic
          [&_code]:bg-[rgba(139,92,246,0.15)] [&_code]:text-purple-light [&_code]:px-1.5 [&_code]:py-px
          [&_code]:rounded [&_code]:text-[14px] [&_code]:font-mono [&_code]:border [&_code]:border-[rgba(139,92,246,0.25)]
          max-[600px]:text-[15px]"
        dangerouslySetInnerHTML={safe(question.questionHtml)}
      />

      {/* Code snippet */}
      {question.codeSnippet && (
        <div className="rounded-[10px] overflow-hidden mb-[18px] border border-[rgba(139,92,246,0.25)] shadow-[0_4px_16px_rgba(0,0,0,0.4)]">
          <div className="flex items-center gap-1.5 px-3.5 py-2 bg-[#0a0814] border-b border-[rgba(139,92,246,0.2)]">
            <span className="w-2.5 h-2.5 rounded-full bg-[#ff5f57]" />
            <span className="w-2.5 h-2.5 rounded-full bg-[#febc2e]" />
            <span className="w-2.5 h-2.5 rounded-full bg-[#28c840]" />
            <span className="ml-auto text-[10px] text-muted uppercase tracking-[0.1em] font-mono">Java</span>
          </div>
          <pre className="bg-[#0d0b1a] px-[18px] py-4 text-[13.5px] leading-[1.7] font-mono overflow-x-auto text-[#c9b8f0] m-0 max-[600px]:text-[12px] max-[600px]:px-3.5 max-[600px]:py-3">
            <code>{question.codeSnippet}</code>
          </pre>
        </div>
      )}

      {/* Multiple choice */}
      {isChoice && (
        <div className="flex flex-col gap-2">
          {options.map((opt, i) => {
            const letter = OPTION_LETTERS[i] ?? opt
            const selected = answer === opt
            const isCorrect = result && opt === result.correctAnswer
            const isWrong   = result && selected && !result.correct

            return (
              <label
                key={opt}
                className={cn(
                  'flex items-center gap-3 px-4 py-3 bg-surface border-[1.5px] border-border rounded-[10px]',
                  'cursor-pointer transition-[border-color,background,transform,box-shadow] duration-150 relative select-none',
                  'max-[600px]:px-3 max-[600px]:py-2.5 max-[600px]:gap-2.5',
                  selected && !isCorrect && !isWrong && 'border-purple bg-[rgba(139,92,246,0.1)] shadow-[0_0_0_3px_rgba(139,92,246,0.1),inset_0_0_0_1px_rgba(139,92,246,0.2)]',
                  isCorrect && '!border-green !bg-[rgba(74,222,128,0.08)] !shadow-[0_0_0_3px_rgba(74,222,128,0.12)]',
                  isWrong   && '!border-red !bg-[rgba(248,113,113,0.08)] !shadow-[0_0_0_3px_rgba(248,113,113,0.12)]',
                )}
              >
                <input
                  type="radio"
                  name={question.id}
                  value={opt}
                  checked={selected}
                  onChange={() => !disabled && onChange(opt)}
                  disabled={disabled}
                  className="hidden"
                />
                <span
                  className={cn(
                    'w-7 h-7 rounded-[7px] flex items-center justify-center text-[12px] font-[800] flex-shrink-0',
                    'bg-card border-[1.5px] border-border text-muted transition-[background,color,border-color] duration-150',
                    selected && !isCorrect && !isWrong && 'bg-purple border-purple text-white',
                    isCorrect && '!bg-green !border-green !text-[#0a1a0a]',
                    isWrong   && '!bg-red !border-red !text-white',
                  )}
                >
                  {letter}
                </span>
                <span className={cn('text-[14px] text-text flex-1 leading-[1.5] max-[600px]:text-[13px]', selected && 'font-medium')}>
                  {opt}
                </span>
                {isCorrect && <Check size={15} strokeWidth={2.5} className="text-green flex-shrink-0" />}
                {isWrong   && <X    size={15} strokeWidth={2.5} className="text-red   flex-shrink-0" />}
              </label>
            )
          })}
        </div>
      )}

      {/* Free text */}
      {!isChoice && (
        <div className="mt-1">
          <div className="text-[11px] font-bold text-purple-light tracking-[0.06em] mb-2 font-mono">
            {question.type === 'WHATS_THE_OUTPUT' ? '⟩ What does this output?' :
             question.type === 'DEBUGGING'        ? '⟩ What is the bug / fix?' :
             question.type === 'CODE_COMPLETION'  ? '⟩ Complete the code:' :
             '⟩ Your answer:'}
          </div>
          <textarea
            className={cn(
              'w-full bg-[#0d0b1a] border-[1.5px] border-border rounded-[10px] px-3.5 py-3 text-[14px] text-text font-mono',
              'resize-y leading-[1.6] transition-[border-color,box-shadow] duration-150',
              'focus:outline-none focus:border-purple focus:shadow-[0_0_0_3px_rgba(139,92,246,0.12)]',
              'disabled:opacity-70 disabled:cursor-not-allowed',
              result?.correct && '!border-[rgba(74,222,128,0.5)]',
              result && !result.correct && '!border-[rgba(248,113,113,0.5)]',
            )}
            placeholder="Type your answer here..."
            value={answer}
            onChange={e => !disabled && onChange(e.target.value)}
            disabled={disabled}
            rows={3}
            spellCheck={false}
            autoComplete="off"
          />
        </div>
      )}

      {/* Result — incorrect */}
      {result && !result.correct && (
        <div className="mt-4 p-3.5 px-4 bg-[rgba(248,113,113,0.06)] border border-[rgba(248,113,113,0.25)] rounded-[10px]">
          <div className="text-[13px] font-bold text-red mb-2.5 flex items-center gap-1.5">
            <X size={14} strokeWidth={2.5} /> Incorrect
          </div>
          <div className="flex gap-2.5 items-baseline mb-1.5 text-[13px] flex-wrap">
            <span className="text-[11px] text-muted uppercase tracking-[0.05em] flex-shrink-0">Your answer:</span>
            <span className="text-red font-mono">{result.userAnswer || '(no answer)'}</span>
          </div>
          <div className="flex gap-2.5 items-baseline mb-1.5 text-[13px] flex-wrap">
            <span className="text-[11px] text-muted uppercase tracking-[0.05em] flex-shrink-0">Correct answer:</span>
            <span className="text-green font-mono font-semibold">{result.correctAnswer}</span>
          </div>
          {result.explanationHtml && (
            <div
              className="mt-2.5 pt-2.5 border-t border-[rgba(248,113,113,0.2)] text-[13px] text-muted leading-[1.6]
                [&_code]:bg-[rgba(139,92,246,0.15)] [&_code]:text-purple-light [&_code]:px-1 [&_code]:py-px [&_code]:rounded [&_code]:text-[12px]"
              dangerouslySetInnerHTML={safe(result.explanationHtml)}
            />
          )}
        </div>
      )}

      {/* Result — correct */}
      {result?.correct && (
        <div className="mt-3.5 px-3.5 py-2.5 bg-[rgba(74,222,128,0.06)] border border-[rgba(74,222,128,0.2)] rounded-[10px] text-[13px] font-semibold text-green">
          <Check size={14} strokeWidth={2.5} className="inline mr-1" /> Correct
        </div>
      )}
    </div>
  )
}

function formatType(type: string): string {
  const map: Record<string, string> = {
    MULTIPLE_CHOICE:  'Multiple Choice',
    TRUE_FALSE:       'True / False',
    FILL_BLANK:       'Fill in the Blank',
    CODE_COMPLETION:  'Code Completion',
    WHATS_THE_OUTPUT: "What's the Output?",
    DEBUGGING:        'Find the Bug',
    SCENARIO:         'Scenario',
    COMPARE_CONTRAST: 'Compare & Contrast',
  }
  return map[type] ?? type
}
