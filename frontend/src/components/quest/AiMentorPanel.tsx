import { cn } from '@/lib/utils'

interface Props {
  feedback: string | null
  loading: boolean
  errorType?: string | null
}

export default function AiMentorPanel({ feedback, loading, errorType }: Props) {
  if (!loading && !feedback) return null

  const icon = errorType === 'COMPILE_ERROR' ? '📜' : errorType === 'RUNTIME_ERROR' ? '⚡' : '🧙'
  const label = loading
    ? 'Master Velan is reading your spell...'
    : errorType === 'COMPILE_ERROR'
    ? 'Master Velan spots a syntax problem:'
    : errorType === 'RUNTIME_ERROR'
    ? 'Master Velan sees what went wrong:'
    : 'Master Velan says:'

  return (
    <div className="flex-shrink-0 border-t border-border bg-[#08060e] max-h-[140px] flex flex-col">
      <div className="px-3.5 py-1.5 font-cinzel text-[9px] tracking-[1px] text-purple-light flex items-center gap-[7px] flex-shrink-0">
        <span
          className={cn(
            'w-1.5 h-1.5 rounded-full bg-purple flex-shrink-0',
            loading ? 'animate-pulse' : 'opacity-50',
          )}
        />
        <span>{icon} {label}</span>
      </div>
      <div className="flex-1 overflow-y-auto px-3.5 pb-2.5 text-[13px] leading-[1.7] text-purple-light italic">
        {loading
          ? <span className="animate-[blink_1s_step-end_infinite]">▌</span>
          : feedback
        }
      </div>
    </div>
  )
}
