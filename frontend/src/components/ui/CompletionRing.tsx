/**
 * Reusable SVG completion ring — green arc + optional check at 100%, used
 * throughout the module/topic/lesson hierarchy pages.
 *
 *  - pct          0–100 fill percentage
 *  - color        stroke colour for the filled arc
 *  - size         outer diameter in px (default 40)
 *  - strokeWidth  ring thickness (default 2.5)
 *  - showCheck    render a ✓ glyph at 100% instead of text (default false)
 *  - emptyLabel   text to show at 0%: '—' or '' (default '')
 *  - fillOpacity  opacity of the filled arc (default 1); use < 1 for inactive states
 */
export interface CompletionRingProps {
  pct: number
  color: string
  size?: number
  strokeWidth?: number
  showCheck?: boolean
  emptyLabel?: '—' | ''
  fillOpacity?: number
}

export default function CompletionRing({
  pct,
  color,
  size = 40,
  strokeWidth = 2.5,
  showCheck = false,
  emptyLabel = '',
  fillOpacity = 1,
}: CompletionRingProps) {
  const cx = size / 2
  const cy = size / 2
  const r = size * 0.4
  const circ = 2 * Math.PI * r
  const dash = (pct / 100) * circ
  const isComplete = pct >= 100

  // Scaled checkmark path: starts left-of-centre, ticks right then up-right
  const ck = size * 0.12
  const checkPath = `M ${cx - ck} ${cy} l ${ck} ${ck} l ${ck * 1.8} -${ck * 1.8}`

  return (
    <svg
      width={size}
      height={size}
      viewBox={`0 0 ${size} ${size}`}
      className="block flex-shrink-0"
    >
      {/* Track */}
      <circle cx={cx} cy={cy} r={r} fill="none" stroke="var(--border)" strokeWidth={strokeWidth} />

      {/* Filled arc */}
      {pct > 0 && (
        <circle
          cx={cx} cy={cy} r={r}
          fill="none"
          stroke={color}
          strokeWidth={strokeWidth}
          strokeLinecap="round"
          strokeDasharray={`${dash} ${circ}`}
          strokeDashoffset="0"
          transform={`rotate(-90 ${cx} ${cy})`}
          style={{ opacity: fillOpacity, transition: 'stroke-dasharray 0.5s ease' }}
        />
      )}

      {/* Centre content */}
      {isComplete && showCheck ? (
        <path
          d={checkPath}
          fill="none"
          stroke={color}
          strokeWidth={strokeWidth * 0.7}
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      ) : (
        <text
          x={cx} y={cy}
          dominantBaseline="central"
          textAnchor="middle"
          fontSize={size * 0.2}
          fill={pct > 0 ? color : 'var(--muted)'}
          fontWeight="700"
          fontFamily="'Cinzel', serif"
        >
          {pct > 0 ? `${Math.round(pct)}%` : emptyLabel}
        </text>
      )}
    </svg>
  )
}
