interface Props {
  value: string
  onChange: (v: string) => void
  disabled?: boolean
  config: Record<string, unknown>
}

export default function FillBlankInput({ value, onChange, disabled, config }: Props) {
  const placeholder = (config?.placeholder as string) ?? 'Your answer...'
  return (
    <input
      type="text"
      value={value}
      onChange={e => onChange(e.target.value)}
      disabled={disabled}
      placeholder={placeholder}
      spellCheck={false}
      autoComplete="off"
      className="w-full bg-surface border border-border rounded-[8px] px-3 py-2.5 text-[14px] font-mono text-text placeholder:text-muted outline-none focus:border-purple transition-[border-color] disabled:opacity-60 disabled:cursor-not-allowed"
    />
  )
}
