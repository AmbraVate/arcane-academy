import { useEffect, useState } from 'react'
import MonacoCodeEditor from '../MonacoCodeEditor'

interface Props {
  value: string
  onChange: (v: string) => void
  disabled?: boolean
  config: Record<string, unknown>
}

export default function CodeStepInput({ value, onChange, disabled, config }: Props) {
  const language = (config?.language as string) ?? 'java'
  const starterCode = (config?.starterCode as string) ?? ''

  // Seed starter code on first render if value is empty
  const [seeded, setSeeded] = useState(false)
  useEffect(() => {
    if (!seeded && !value && starterCode) {
      onChange(starterCode)
      setSeeded(true)
    }
  }, [seeded, value, starterCode, onChange])

  return (
    <div className="rounded-[8px] border border-border overflow-hidden" style={{ minHeight: 180 }}>
      <MonacoCodeEditor
        value={value}
        onChange={onChange}
        language={language}
        disabled={disabled}
        height="180px"
        minHeight="180px"
      />
    </div>
  )
}
