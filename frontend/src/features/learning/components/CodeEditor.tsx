/**
 * CodeEditor — Java code editor backed by Monaco.
 *
 * Preserves the original {value, onChange, readOnly} interface so all
 * existing call-sites continue to work without modification.
 */
import MonacoCodeEditor from './MonacoCodeEditor'

interface Props {
  value: string
  onChange: (v: string) => void
  readOnly?: boolean
  disabled?: boolean
}

export default function CodeEditor({ value, onChange, readOnly, disabled }: Props) {
  return (
    <MonacoCodeEditor
      value={value}
      onChange={onChange}
      language="java"
      readOnly={readOnly}
      disabled={disabled}
      minHeight="200px"
    />
  )
}
