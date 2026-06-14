import { useState, useEffect } from 'react'
import { ArrowUp, ArrowDown } from 'lucide-react'

interface Props {
  value: string   // JSON array of current order, e.g. '["b","a","c"]'
  onChange: (v: string) => void
  disabled?: boolean
  config: Record<string, unknown>
}

/**
 * SequenceInput — arrange items into the correct order.
 * The answer submitted is the JSON-serialised array of item values in
 * the learner's chosen order (e.g. `["a","b","c"]`).
 */
export default function SequenceInput({ value, onChange, disabled, config }: Props) {
  const originalItems = (config?.items as string[]) ?? []

  const [items, setItems] = useState<string[]>(() => {
    try { return JSON.parse(value) as string[] }
    catch { return [...originalItems] }
  })

  useEffect(() => {
    onChange(JSON.stringify(items))
  }, [items]) // eslint-disable-line react-hooks/exhaustive-deps

  function move(idx: number, dir: -1 | 1) {
    if (disabled) return
    const next = [...items]
    const swap = idx + dir
    if (swap < 0 || swap >= next.length) return
    ;[next[idx], next[swap]] = [next[swap], next[idx]]
    setItems(next)
  }

  return (
    <div className="flex flex-col gap-2">
      {items.map((item, i) => (
        <div
          key={item}
          className="flex items-center gap-2 bg-surface border border-border rounded-[8px] px-3 py-2.5"
        >
          <span className="w-5 text-[11px] font-bold text-muted flex-shrink-0">{i + 1}.</span>
          <span className="flex-1 text-[14px] text-text">{item}</span>
          <div className="flex flex-col gap-0.5 flex-shrink-0">
            <button
              type="button"
              disabled={disabled || i === 0}
              onClick={() => move(i, -1)}
              className="p-0.5 text-muted hover:text-purple-light disabled:opacity-30 disabled:cursor-not-allowed"
            >
              <ArrowUp size={14} strokeWidth={2} />
            </button>
            <button
              type="button"
              disabled={disabled || i === items.length - 1}
              onClick={() => move(i, 1)}
              className="p-0.5 text-muted hover:text-purple-light disabled:opacity-30 disabled:cursor-not-allowed"
            >
              <ArrowDown size={14} strokeWidth={2} />
            </button>
          </div>
        </div>
      ))}
    </div>
  )
}
