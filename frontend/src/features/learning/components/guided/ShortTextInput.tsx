interface Props {
  value: string
  onChange: (v: string) => void
  disabled?: boolean
  config: Record<string, unknown>
}

export default function ShortTextInput({ value, onChange, disabled, config }: Props) {
  const minWords = (config?.minWords as number) ?? 0
  const wordCount = value.trim() ? value.trim().split(/\s+/).length : 0
  const belowMin = minWords > 0 && wordCount < minWords

  return (
    <div>
      <textarea
        value={value}
        onChange={e => onChange(e.target.value)}
        disabled={disabled}
        rows={4}
        placeholder="Write your answer here..."
        className="w-full bg-surface border border-border rounded-[8px] px-3 py-2.5 text-[14px] text-text placeholder:text-muted outline-none focus:border-purple transition-[border-color] resize-y disabled:opacity-60 disabled:cursor-not-allowed"
      />
      {minWords > 0 && (
        <div className={`text-[11px] mt-1 text-right ${belowMin ? 'text-muted' : 'text-teal'}`}>
          {wordCount} / {minWords}+ words
        </div>
      )}
    </div>
  )
}
