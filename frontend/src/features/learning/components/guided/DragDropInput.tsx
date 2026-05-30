import { useState, useEffect } from 'react'

interface Props {
  value: string   // JSON: { [target]: item }
  onChange: (v: string) => void
  disabled?: boolean
  config: Record<string, unknown>
}

/**
 * DragDropInput — select one item per target using a dropdown.
 *
 * The answer is a JSON object mapping target labels to chosen items,
 * e.g. `{"Slot A":"item1","Slot B":"item2"}`.
 *
 * A simplified drag-and-drop UX using <select> is sufficient for Phase 3;
 * a real DnD interaction can be added in a later phase.
 */
export default function DragDropInput({ value, onChange, disabled, config }: Props) {
  const items   = (config?.items   as string[]) ?? []
  const targets = (config?.targets as string[]) ?? []

  const [mapping, setMapping] = useState<Record<string, string>>(() => {
    try { return JSON.parse(value) as Record<string, string> }
    catch { return {} }
  })

  useEffect(() => {
    onChange(JSON.stringify(mapping))
  }, [mapping]) // eslint-disable-line react-hooks/exhaustive-deps

  function select(target: string, item: string) {
    if (disabled) return
    setMapping(prev => ({ ...prev, [target]: item }))
  }

  return (
    <div className="flex flex-col gap-2.5">
      {targets.map(target => (
        <div key={target} className="flex items-center gap-3">
          <span className="text-[13px] text-muted w-24 flex-shrink-0">{target}</span>
          <select
            value={mapping[target] ?? ''}
            onChange={e => select(target, e.target.value)}
            disabled={disabled}
            className="flex-1 bg-surface border border-border rounded-[8px] px-3 py-2 text-[13px] text-text outline-none focus:border-purple transition-[border-color] disabled:opacity-60 disabled:cursor-not-allowed"
          >
            <option value="">-- choose --</option>
            {items.map(item => (
              <option key={item} value={item}>{item}</option>
            ))}
          </select>
        </div>
      ))}
    </div>
  )
}
