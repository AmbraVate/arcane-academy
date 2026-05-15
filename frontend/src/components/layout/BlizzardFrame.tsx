import { memo } from 'react'

// Pre-computed icicle data: [leftPct, widthPx, heightPx, opacity]
// Packed across full viewport width for a dense, natural-looking ice fringe.
const ICICLES: [number, number, number, number][] = [
  [0.5, 8, 48, 0.80], [2.0, 5, 28, 0.65], [3.2, 11, 65, 0.88], [4.5, 7, 35, 0.73],
  [5.8, 9, 52, 0.84], [7.0, 5, 20, 0.57], [8.0, 13, 70, 0.91], [9.5, 6, 40, 0.78],
  [11,  8, 46, 0.82], [12.5, 5, 24, 0.61], [13.5, 10, 58, 0.87], [15.0, 7, 33, 0.72],
  [16.5, 9, 50, 0.84], [17.5, 5, 22, 0.59], [19.0, 11, 63, 0.89], [20.0, 6, 36, 0.76],
  [21.5, 8, 45, 0.82], [23.0, 5, 26, 0.63], [24.0, 10, 55, 0.86], [25.5, 7, 30, 0.70],
  [27.0, 9, 48, 0.83], [28.0, 5, 20, 0.56], [29.5, 12, 68, 0.91], [30.5, 6, 38, 0.77],
  [32.0, 8, 44, 0.82], [33.5, 5, 24, 0.62], [34.5, 10, 56, 0.86], [36.0, 7, 32, 0.72],
  [37.5, 9, 50, 0.84], [38.5, 5, 22, 0.59], [40.0, 11, 62, 0.89], [41.0, 6, 36, 0.76],
  [42.5, 8, 46, 0.82], [44.0, 5, 24, 0.62], [45.0, 10, 54, 0.86], [46.5, 7, 30, 0.70],
  [48.0, 9, 48, 0.83], [49.0, 5, 20, 0.57], [50.5, 13, 70, 0.91], [51.5, 6, 38, 0.77],
  [53.0, 8, 44, 0.82], [54.5, 5, 26, 0.63], [55.5, 10, 56, 0.86], [57.0, 7, 32, 0.72],
  [58.5, 9, 50, 0.84], [59.5, 5, 22, 0.59], [61.0, 11, 62, 0.89], [62.0, 6, 36, 0.76],
  [63.5, 8, 46, 0.82], [65.0, 5, 24, 0.62], [66.0, 10, 54, 0.86], [67.5, 7, 30, 0.70],
  [69.0, 9, 48, 0.83], [70.0, 5, 20, 0.56], [71.5, 12, 66, 0.90], [72.5, 6, 38, 0.77],
  [74.0, 8, 44, 0.82], [75.5, 5, 26, 0.63], [76.5, 10, 56, 0.86], [78.0, 7, 32, 0.72],
  [79.5, 9, 50, 0.84], [80.5, 5, 22, 0.59], [82.0, 11, 63, 0.89], [83.0, 6, 36, 0.76],
  [84.5, 8, 46, 0.82], [86.0, 5, 24, 0.62], [87.0, 10, 55, 0.86], [88.5, 7, 30, 0.70],
  [90.0, 9, 48, 0.83], [91.0, 5, 20, 0.57], [92.5, 12, 68, 0.91], [93.5, 6, 38, 0.77],
  [95.0, 8, 44, 0.82], [96.5, 5, 26, 0.63], [97.5, 10, 56, 0.86], [99.0, 7, 32, 0.72],
]

const CHAIN_COUNT = 26

const RuneMedallion = memo(() => (
  <svg width="58" height="58" viewBox="0 0 58 58" fill="none" aria-hidden="true">
    {/* Outer ring */}
    <circle cx="29" cy="29" r="27" stroke="#1e4a7c" strokeWidth="2.5" fill="rgba(5,12,28,0.95)" />
    <circle cx="29" cy="29" r="22" stroke="#2a6aaa" strokeWidth="0.8" fill="none" opacity="0.65" />
    {/* Centre core */}
    <circle cx="29" cy="29" r="5.5" fill="#0a2040" stroke="#4a9eda" strokeWidth="1.5" />
    {/* Snowflake arms × 4 directions */}
    {[0, 45, 90, 135].map(deg => (
      <g key={deg} transform={`rotate(${deg} 29 29)`}>
        <line x1="29" y1="6"  x2="29" y2="52" stroke="#6ab4f0" strokeWidth="1.5" opacity="0.8" />
        <line x1="25" y1="12" x2="29" y2="6"  stroke="#90c8f0" strokeWidth="1"   opacity="0.7" />
        <line x1="33" y1="12" x2="29" y2="6"  stroke="#90c8f0" strokeWidth="1"   opacity="0.7" />
        <line x1="25" y1="46" x2="29" y2="52" stroke="#90c8f0" strokeWidth="1"   opacity="0.7" />
        <line x1="33" y1="46" x2="29" y2="52" stroke="#90c8f0" strokeWidth="1"   opacity="0.7" />
        <line x1="23" y1="29" x2="29" y2="23" stroke="#90c8f0" strokeWidth="0.8" opacity="0.5" />
        <line x1="23" y1="29" x2="29" y2="35" stroke="#90c8f0" strokeWidth="0.8" opacity="0.5" />
      </g>
    ))}
    {/* Ring accent dots */}
    {[0, 45, 90, 135, 180, 225, 270, 315].map((deg, i) => (
      <circle
        key={i}
        cx={29 + 20 * Math.cos((deg * Math.PI) / 180)}
        cy={29 + 20 * Math.sin((deg * Math.PI) / 180)}
        r="1.8"
        fill="#4a9eda"
        opacity="0.55"
      />
    ))}
  </svg>
))
RuneMedallion.displayName = 'RuneMedallion'

export default memo(function BlizzardFrame() {
  return (
    <div className="bz-frame" aria-hidden="true">
      {/* Torch glows — bleed out from the side edges into the content area */}
      <div className="bz-torch bz-torch-l" />
      <div className="bz-torch bz-torch-r" />

      {/* ── Top stone bar ─────────────────────────────── */}
      <div className="bz-bar bz-bar-top">
        <div className="bz-medallion">
          <RuneMedallion />
        </div>
        <div className="bz-icicle-row">
          {ICICLES.map(([l, w, h, o], i) => (
            <div
              key={i}
              className="bz-icicle"
              style={{ left: `${l}%`, width: `${w}px`, height: `${h}px`, opacity: o }}
            />
          ))}
        </div>
      </div>

      {/* ── Left stone bar with chains ────────────────── */}
      <div className="bz-bar bz-bar-left">
        <div className="bz-chain">
          {Array.from({ length: CHAIN_COUNT }, (_, i) => (
            <div key={i} className={`bz-link ${i % 2 === 0 ? 'bz-link-e' : 'bz-link-o'}`} />
          ))}
        </div>
      </div>

      {/* ── Right stone bar with chains ───────────────── */}
      <div className="bz-bar bz-bar-right">
        <div className="bz-chain">
          {Array.from({ length: CHAIN_COUNT }, (_, i) => (
            <div key={i} className={`bz-link ${i % 2 === 0 ? 'bz-link-e' : 'bz-link-o'}`} />
          ))}
        </div>
      </div>

      {/* ── Bottom stone bar ──────────────────────────── */}
      <div className="bz-bar bz-bar-bottom" />

      {/* ── Corner caps ───────────────────────────────── */}
      <div className="bz-corner bz-corner-tl" />
      <div className="bz-corner bz-corner-tr" />
      <div className="bz-corner bz-corner-bl" />
      <div className="bz-corner bz-corner-br" />
    </div>
  )
})
