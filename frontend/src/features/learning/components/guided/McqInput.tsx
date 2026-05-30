import { cn } from '@/lib/utils'

interface Props {
  value: string
  onChange: (v: string) => void
  disabled?: boolean
  config: Record<string, unknown>
}

export default function McqInput({ value, onChange, disabled, config }: Props) {
  const options = (config?.options as string[]) ?? []

  return (
    <div className="flex flex-col gap-2">
      {options.map((opt, i) => {
        const selected = value === opt
        return (
          <button
            key={i}
            type="button"
            disabled={disabled}
            onClick={() => !disabled && onChange(opt)}
            className={cn(
              'w-full text-left px-4 py-3 rounded-[8px] border text-[14px] leading-[1.55] transition-[border-color,background-color] duration-150',
              'disabled:cursor-not-allowed',
              selected
                ? 'border-purple bg-[rgba(139,92,246,0.12)] text-purple-light font-medium'
                : 'border-border bg-surface text-text hover:border-purple/60 hover:bg-[rgba(139,92,246,0.06)]',
            )}
          >
            <span className={cn(
              'inline-flex items-center justify-center w-5 h-5 rounded-full border text-[11px] font-bold mr-3 flex-shrink-0',
              selected ? 'border-purple bg-purple text-white' : 'border-border text-muted',
            )}>
              {String.fromCharCode(65 + i)}
            </span>
            {opt}
          </button>
        )
      })}
    </div>
  )
}
