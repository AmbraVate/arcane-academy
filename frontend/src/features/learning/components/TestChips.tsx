import { cn } from '@/lib/utils'
import type { TestCaseLabel } from '@/shared/types'

interface Props {
  labels: TestCaseLabel[]
  results: Map<string, boolean>
}

export default function TestChips({ labels, results }: Props) {
  if (!labels?.length) return null

  return (
    <div className="flex flex-wrap gap-1.5 mb-2.5">
      {labels.map(({ label }) => {
        const result = results.get(label)
        return (
          <span
            key={label}
            className={cn(
              'text-[11px] px-2.5 py-[3px] rounded-[10px] font-mono transition-all duration-300',
              result === undefined && 'bg-card border border-border text-muted',
              result === true      && 'bg-[#08200e] border border-green text-green',
              result === false     && 'bg-[#200808] border border-red text-red',
            )}
          >
            {result === true  && '✓ '}
            {result === false && '✗ '}
            {label}
          </span>
        )
      })}
    </div>
  )
}
