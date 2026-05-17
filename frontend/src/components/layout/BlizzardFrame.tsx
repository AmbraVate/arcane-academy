// Ornate Lich-King chrome frame: top/bottom bars, corner emblems, chains, icicles, side pillars with crystal sconces.
// Renders as fixed-position overlay; pure decoration.
import React from 'react'

/* ── CornerEmblem (top-left/right) ────────────────────────────────────────── */
function CornerEmblem({ corner }: { corner: 'tl' | 'tr' }) {
  const isLeft = corner === 'tl'
  const wrapStyle: React.CSSProperties = {
    position: 'absolute',
    top: -6,
    [isLeft ? 'left' : 'right']: -6,
    width: 130,
    height: 130,
    zIndex: 2,
  }
  return (
    <div style={wrapStyle}>
      <svg viewBox="0 0 130 130" width="130" height="130">
        <defs>
          <radialGradient id={`emblemBg-${corner}`} cx="50%" cy="50%" r="50%">
            <stop offset="0" stopColor="#1a3057" />
            <stop offset=".7" stopColor="#0a1224" />
            <stop offset="1" stopColor="#04060e" />
          </radialGradient>
          <radialGradient id={`crystalGlow-${corner}`} cx="50%" cy="50%" r="50%">
            <stop offset="0" stopColor="var(--accent-bright)" stopOpacity=".95" />
            <stop offset=".5" stopColor="var(--accent)" stopOpacity=".7" />
            <stop offset="1" stopColor="var(--accent-deep)" stopOpacity="0" />
          </radialGradient>
          <filter id={`emblemShadow-${corner}`} x="-20%" y="-20%" width="140%" height="140%">
            <feDropShadow dx="0" dy="3" stdDeviation="3" floodOpacity=".7" />
          </filter>
        </defs>
        {/* Outer ring */}
        <g filter={`url(#emblemShadow-${corner})`}>
          <circle cx="65" cy="65" r="56" fill="#0a1224" />
          <circle cx="65" cy="65" r="56" fill="none" stroke="#2a4274" strokeWidth="2" />
          <circle cx="65" cy="65" r="52" fill="none" stroke="rgba(155,183,224,.5)" strokeWidth="1" />
          <circle cx="65" cy="65" r="48" fill={`url(#emblemBg-${corner})`} />
        </g>
        {/* Rivets around */}
        {Array.from({ length: 8 }).map((_, i) => {
          const a = (i / 8) * Math.PI * 2 - Math.PI / 2
          return (
            <circle
              key={i}
              cx={65 + Math.cos(a) * 54}
              cy={65 + Math.sin(a) * 54}
              r="2.5"
              fill="#04060e"
              stroke="rgba(155,183,224,.6)"
              strokeWidth=".6"
            />
          )
        })}
        {/* Ornate corner spikes pointing into the room */}
        <path d="M65 6 L70 16 L60 16 Z" fill="#2a4274" stroke="rgba(155,183,224,.4)" strokeWidth=".5" />
        <path
          d={isLeft ? 'M6 65 L16 70 L16 60 Z' : 'M124 65 L114 70 L114 60 Z'}
          fill="#2a4274"
          stroke="rgba(155,183,224,.4)"
          strokeWidth=".5"
        />
        {/* Inner ring of runes */}
        <circle cx="65" cy="65" r="42" fill="none" stroke="rgba(93,198,255,.25)" strokeWidth=".8" strokeDasharray="2 4" />
        {/* Central crystal sconce */}
        <circle cx="65" cy="65" r="22" fill={`url(#crystalGlow-${corner})`} />
        <polygon points="65,46 76,65 65,84 54,65" fill="var(--accent-deep)" stroke="var(--accent-bright)" strokeWidth="1" opacity=".95" />
        <polygon points="65,52 72,65 65,78 58,65" fill="var(--accent)" opacity=".7" />
        <line x1="65" y1="48" x2="65" y2="82" stroke="var(--accent-bright)" strokeWidth=".5" opacity=".8" />
        {/* Frost cracks */}
        <path d="M30 50 Q40 55 50 50" stroke="rgba(216,230,255,.3)" strokeWidth=".6" fill="none" />
        <path d="M85 80 Q95 85 105 80" stroke="rgba(216,230,255,.3)" strokeWidth=".6" fill="none" />
      </svg>
    </div>
  )
}

/* ── CornerSconce (bottom-left/right) ─────────────────────────────────────── */
function CornerSconce({ corner }: { corner: 'bl' | 'br' }) {
  const isLeft = corner === 'bl'
  const wrapStyle: React.CSSProperties = {
    position: 'absolute',
    bottom: 40,
    [isLeft ? 'left' : 'right']: -4,
    width: 80,
    height: 220,
    zIndex: 2,
  }
  return (
    <div style={wrapStyle}>
      <svg viewBox="0 0 80 220" width="80" height="220">
        <defs>
          <linearGradient id={`sconceCrystal-${corner}`} x1="0" y1="0" x2="0" y2="1">
            <stop offset="0" stopColor="var(--accent-bright)" stopOpacity=".95" />
            <stop offset=".5" stopColor="var(--accent)" stopOpacity=".9" />
            <stop offset="1" stopColor="var(--accent-deep)" stopOpacity=".7" />
          </linearGradient>
          <radialGradient id={`sconceGlow-${corner}`} cx="50%" cy="60%" r="50%">
            <stop offset="0" stopColor="var(--accent-bright)" stopOpacity=".6" />
            <stop offset="1" stopColor="var(--accent)" stopOpacity="0" />
          </radialGradient>
        </defs>
        {/* glow halo behind */}
        <ellipse cx="40" cy="130" rx="50" ry="80" fill={`url(#sconceGlow-${corner})`} />
        {/* base plinth */}
        <rect x="14" y="190" width="52" height="22" fill="#0a1224" stroke="rgba(155,183,224,.4)" strokeWidth="1" />
        <rect x="10" y="186" width="60" height="6" fill="#182a4d" stroke="rgba(155,183,224,.5)" strokeWidth="1" />
        <rect x="20" y="170" width="40" height="20" fill="#0a1224" stroke="rgba(155,183,224,.3)" strokeWidth="1" />
        {/* Crystal cluster */}
        <polygon
          points="40,40 56,100 50,180 30,180 24,100"
          fill={`url(#sconceCrystal-${corner})`}
          stroke="var(--accent-bright)"
          strokeWidth="1"
        />
        <polygon points="40,60 50,110 46,170 34,170 30,110" fill="var(--accent)" opacity=".7" />
        <line x1="40" y1="42" x2="40" y2="178" stroke="var(--accent-bright)" strokeWidth=".7" opacity=".9" />
        {/* secondary smaller crystals */}
        <polygon points="20,110 24,140 16,170 10,150 8,130" fill={`url(#sconceCrystal-${corner})`} opacity=".85" stroke="var(--accent-bright)" strokeWidth=".5" />
        <polygon points="60,110 70,130 72,150 64,170 56,140" fill={`url(#sconceCrystal-${corner})`} opacity=".85" stroke="var(--accent-bright)" strokeWidth=".5" />
        {/* icy shards at top */}
        <polygon points="40,38 42,52 38,52" fill="var(--accent-bright)" opacity=".9" />
        <polygon points="36,42 38,56 34,56" fill="var(--accent-bright)" opacity=".7" />
        <polygon points="44,42 46,56 42,56" fill="var(--accent-bright)" opacity=".7" />
      </svg>
    </div>
  )
}

/* ── TopCrest ─────────────────────────────────────────────────────────────── */
function TopCrest() {
  return (
    <div style={{ position: 'absolute', top: 2, left: '50%', transform: 'translateX(-50%)', width: 70, height: 70, zIndex: 3 }}>
      <svg viewBox="0 0 70 70" width="70" height="70" style={{ animation: 'bz-shimmer 4s ease-in-out infinite' }}>
        <defs>
          <radialGradient id="crestGlow" cx="50%" cy="50%" r="50%">
            <stop offset="0" stopColor="var(--accent-bright)" stopOpacity=".8" />
            <stop offset="1" stopColor="var(--accent)" stopOpacity="0" />
          </radialGradient>
        </defs>
        <circle cx="35" cy="35" r="30" fill="url(#crestGlow)" />
        <circle cx="35" cy="35" r="18" fill="#0a1224" stroke="var(--accent)" strokeWidth="1.5" />
        <circle cx="35" cy="35" r="14" fill="none" stroke="rgba(155,183,224,.4)" strokeWidth=".6" />
        {/* snowflake */}
        <g stroke="var(--accent-bright)" strokeWidth="1.2" strokeLinecap="round" fill="none">
          <line x1="35" y1="22" x2="35" y2="48" />
          <line x1="24" y1="29" x2="46" y2="41" />
          <line x1="24" y1="41" x2="46" y2="29" />
          <path d="M35 24 L32 27 M35 24 L38 27" />
          <path d="M35 46 L32 43 M35 46 L38 43" />
          <path d="M26 30 L30 30 M26 30 L26 34" />
          <path d="M44 40 L40 40 M44 40 L44 36" />
          <path d="M26 40 L30 40 M26 40 L26 36" />
          <path d="M44 30 L40 30 M44 30 L44 34" />
        </g>
      </svg>
    </div>
  )
}

/* ── Chain ────────────────────────────────────────────────────────────────── */
function Chain({ side }: { side: 'left' | 'right' }) {
  const isLeft = side === 'left'
  const links = 12
  return (
    <svg
      viewBox="0 0 30 240"
      style={{
        position: 'absolute',
        top: 90,
        [isLeft ? 'left' : 'right']: 76,
        width: 22,
        height: 220,
        zIndex: 1,
        pointerEvents: 'none',
      }}
    >
      <defs>
        <linearGradient id={`chainGrad-${side}`} x1="0" x2="1" y1="0" y2="0">
          <stop offset="0" stopColor="#04060e" />
          <stop offset=".5" stopColor="#4e6ea8" />
          <stop offset="1" stopColor="#04060e" />
        </linearGradient>
      </defs>
      {Array.from({ length: links }).map((_, i) => (
        <ellipse key={i} cx="15" cy={10 + i * 18} rx="6" ry="7" fill="none" stroke={`url(#chainGrad-${side})`} strokeWidth="2.5" />
      ))}
      {/* End hook */}
      <circle cx="15" cy={10 + links * 18} r="4" fill="#0a1224" stroke="rgba(155,183,224,.5)" strokeWidth="1" />
    </svg>
  )
}

/* ── Icicles ──────────────────────────────────────────────────────────────── */
function Icicles() {
  const icicles: { x: number; h: number; w: number }[] = []
  for (let i = 0; i < 30; i++) {
    const x = 100 + i * 50 + (i % 3) * 12
    const h = 14 + ((i * 13) % 28)
    const w = 5 + ((i * 7) % 6)
    icicles.push({ x, h, w })
  }
  return (
    <svg
      style={{
        position: 'absolute',
        top: 60,
        left: 0,
        right: 0,
        width: '100%',
        height: 60,
        zIndex: 1,
        pointerEvents: 'none',
      }}
      preserveAspectRatio="none"
      viewBox="0 0 1600 60"
    >
      <defs>
        <linearGradient id="icicleGrad" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor="rgba(216,230,255,.85)" />
          <stop offset=".5" stopColor="rgba(155,200,240,.5)" />
          <stop offset="1" stopColor="rgba(155,200,240,0)" />
        </linearGradient>
      </defs>
      {icicles.map((ic, i) => (
        <polygon key={i} points={`${ic.x},0 ${ic.x + ic.w},0 ${ic.x + ic.w / 2},${ic.h}`} fill="url(#icicleGrad)" />
      ))}
    </svg>
  )
}

/* ── FrameSVG ─────────────────────────────────────────────────────────────── */
function FrameSVG() {
  return (
    <>
      {/* TOP edge — full-width strip */}
      <svg
        className="frame-top"
        viewBox="0 0 1600 100"
        preserveAspectRatio="none"
        style={{ position: 'absolute', top: 0, left: 0, right: 0, width: '100%', height: '92px' }}
      >
        <defs>
          <linearGradient id="topMetal" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0" stopColor="#1a2e52" />
            <stop offset=".4" stopColor="#0a1224" />
            <stop offset="1" stopColor="#04060e" />
          </linearGradient>
          <linearGradient id="topShine" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0" stopColor="rgba(155,183,224,.5)" />
            <stop offset="1" stopColor="rgba(155,183,224,0)" />
          </linearGradient>
        </defs>
        {/* main bar */}
        <path
          d="M0 0 L1600 0 L1600 70 Q1500 80 1400 70 Q1300 60 1200 75 Q1100 85 1000 70 Q900 60 800 76 Q700 60 600 70 Q500 80 400 70 Q300 60 200 75 Q100 80 0 70 Z"
          fill="url(#topMetal)"
        />
        {/* top highlight */}
        <rect x="0" y="0" width="1600" height="2" fill="url(#topShine)" />
        {/* etched rune line */}
        <path d="M120 36 L1480 36" stroke="rgba(93,198,255,.18)" strokeWidth="1" strokeDasharray="2 6" />
        {/* rivets */}
        {Array.from({ length: 16 }).map((_, i) => (
          <circle key={i} cx={100 + i * 94} cy="20" r="2.5" fill="#0a1224" stroke="rgba(155,183,224,.5)" strokeWidth=".6" />
        ))}
      </svg>

      {/* BOTTOM edge */}
      <svg
        className="frame-bottom"
        viewBox="0 0 1600 100"
        preserveAspectRatio="none"
        style={{ position: 'absolute', bottom: 0, left: 0, right: 0, width: '100%', height: '76px' }}
      >
        <defs>
          <linearGradient id="botMetal" x1="0" y1="1" x2="0" y2="0">
            <stop offset="0" stopColor="#04060e" />
            <stop offset=".5" stopColor="#0a1224" />
            <stop offset="1" stopColor="#1a2e52" />
          </linearGradient>
        </defs>
        <path
          d="M0 30 Q200 20 400 30 Q600 38 800 26 Q1000 36 1200 30 Q1400 22 1600 30 L1600 100 L0 100 Z"
          fill="url(#botMetal)"
        />
        <rect x="0" y="98" width="1600" height="2" fill="rgba(155,183,224,.3)" />
        {/* snow drift along bottom */}
        <path
          d="M0 38 Q200 28 400 38 Q600 46 800 34 Q1000 44 1200 38 Q1400 30 1600 38 L1600 50 Q1400 42 1200 50 Q1000 56 800 46 Q600 58 400 50 Q200 60 0 50 Z"
          fill="rgba(216,230,255,.18)"
        />
      </svg>

      {/* LEFT side pillar */}
      <svg
        className="frame-left"
        viewBox="0 0 60 1000"
        preserveAspectRatio="none"
        style={{ position: 'absolute', top: '92px', bottom: '76px', left: 0, width: '44px' }}
      >
        <defs>
          <linearGradient id="leftMetal" x1="0" x2="1" y1="0" y2="0">
            <stop offset="0" stopColor="#04060e" />
            <stop offset=".5" stopColor="#182a4d" />
            <stop offset="1" stopColor="#0a1224" />
          </linearGradient>
        </defs>
        <rect x="0" y="0" width="60" height="1000" fill="url(#leftMetal)" />
        <rect x="0" y="0" width="2" height="1000" fill="rgba(0,0,0,.6)" />
        <rect x="58" y="0" width="2" height="1000" fill="rgba(155,183,224,.3)" />
        <line x1="30" y1="20" x2="30" y2="980" stroke="rgba(93,198,255,.18)" strokeWidth="1" strokeDasharray="3 8" />
      </svg>

      {/* RIGHT side pillar */}
      <svg
        className="frame-right"
        viewBox="0 0 60 1000"
        preserveAspectRatio="none"
        style={{ position: 'absolute', top: '92px', bottom: '76px', right: 0, width: '44px' }}
      >
        <defs>
          <linearGradient id="rightMetal" x1="0" x2="1" y1="0" y2="0">
            <stop offset="0" stopColor="#0a1224" />
            <stop offset=".5" stopColor="#182a4d" />
            <stop offset="1" stopColor="#04060e" />
          </linearGradient>
        </defs>
        <rect x="0" y="0" width="60" height="1000" fill="url(#rightMetal)" />
        <rect x="0" y="0" width="2" height="1000" fill="rgba(155,183,224,.3)" />
        <rect x="58" y="0" width="2" height="1000" fill="rgba(0,0,0,.6)" />
        <line x1="30" y1="20" x2="30" y2="980" stroke="rgba(93,198,255,.18)" strokeWidth="1" strokeDasharray="3 8" />
      </svg>

      {/* CORNERS */}
      <CornerEmblem corner="tl" />
      <CornerEmblem corner="tr" />
      <CornerSconce corner="bl" />
      <CornerSconce corner="br" />

      {/* TOP CENTER CREST */}
      <TopCrest />

      {/* CHAINS hanging from upper corners */}
      <Chain side="left" />
      <Chain side="right" />

      {/* ICICLES along top */}
      <Icicles />
    </>
  )
}

/* ── BlizzardFrame (default export) ──────────────────────────────────────── */
export default function BlizzardFrame() {
  return (
    <div className="frame-layer" aria-hidden="true">
      <FrameSVG />
    </div>
  )
}
