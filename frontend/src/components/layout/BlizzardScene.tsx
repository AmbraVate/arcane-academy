import { useRef, useEffect } from 'react'
import type { SceneKind } from '@/hooks/useTheme'

/* ── Snow Field ──────────────────────────────────────────────────────────── */
export function SnowField({ enabled = true, density = 80 }: { enabled?: boolean; density?: number }) {
  const canvasRef = useRef<HTMLCanvasElement>(null)
  const rafRef = useRef<number>(0)

  useEffect(() => {
    const canvas = canvasRef.current
    if (!canvas || !enabled) return
    const ctx = canvas.getContext('2d')!
    let W = (canvas.width = canvas.offsetWidth)
    let H = (canvas.height = canvas.offsetHeight)

    const onResize = () => {
      W = canvas.width = canvas.offsetWidth
      H = canvas.height = canvas.offsetHeight
    }
    window.addEventListener('resize', onResize)

    const flakes = Array.from({ length: density }).map(() => ({
      x: Math.random() * W,
      y: Math.random() * H,
      r: 0.6 + Math.random() * 2.4,
      vy: 0.2 + Math.random() * 0.9,
      vx: -0.3 + Math.random() * 0.6,
      a: 0.4 + Math.random() * 0.6,
      t: Math.random() * Math.PI * 2,
    }))

    let last = performance.now()
    const tick = (now: number) => {
      const dt = Math.min(40, now - last); last = now
      ctx.clearRect(0, 0, W, H)
      for (const f of flakes) {
        f.t += dt * 0.002
        f.y += f.vy * dt * 0.06
        f.x += f.vx * dt * 0.06 + Math.sin(f.t) * 0.3
        if (f.y > H + 4) { f.y = -4; f.x = Math.random() * W }
        if (f.x < -4) f.x = W + 4
        if (f.x > W + 4) f.x = -4
        ctx.beginPath()
        ctx.arc(f.x, f.y, f.r, 0, Math.PI * 2)
        ctx.fillStyle = `rgba(216,230,255,${f.a})`
        ctx.shadowColor = 'rgba(216,230,255,0.8)'
        ctx.shadowBlur = 6
        ctx.fill()
      }
      ctx.shadowBlur = 0
      rafRef.current = requestAnimationFrame(tick)
    }
    rafRef.current = requestAnimationFrame(tick)

    return () => {
      cancelAnimationFrame(rafRef.current)
      window.removeEventListener('resize', onResize)
    }
  }, [enabled, density])

  if (!enabled) return null
  return <canvas ref={canvasRef} className="snow" />
}

/* ── Castle Scene ────────────────────────────────────────────────────────── */
export function CastleScene() {
  return (
    <svg className="scene-castle" viewBox="0 0 1600 900" preserveAspectRatio="xMidYMax slice">
      <defs>
        <linearGradient id="mountainFar" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor="#0c1730" />
          <stop offset="1" stopColor="#040810" />
        </linearGradient>
        <linearGradient id="mountainMid" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor="#0a1428" />
          <stop offset="1" stopColor="#030610" />
        </linearGradient>
        <linearGradient id="castleGrad" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor="#08111f" />
          <stop offset="1" stopColor="#000305" />
        </linearGradient>
        <radialGradient id="moonGlow" cx="50%" cy="50%" r="50%">
          <stop offset="0" stopColor="rgba(var(--accent-glow),.25)" />
          <stop offset="1" stopColor="rgba(var(--accent-glow),0)" />
        </radialGradient>
      </defs>

      {/* moon / aurora glow behind castle */}
      <circle cx="800" cy="320" r="320" fill="url(#moonGlow)" />

      {/* Far mountain range */}
      <path
        d="M0 600 L120 400 L240 500 L380 350 L500 480 L620 380 L760 460 L880 360 L1020 480 L1160 380 L1300 470 L1440 380 L1600 480 L1600 900 L0 900 Z"
        fill="url(#mountainFar)" opacity=".75"
      />

      {/* Mid mountain range with castle */}
      <g fill="url(#mountainMid)">
        <path d="M0 700 L160 540 L300 620 L460 480 L600 600 L760 460 L900 580 L1060 480 L1220 600 L1380 500 L1540 580 L1600 540 L1600 900 L0 900 Z" />
      </g>

      {/* Central frozen castle */}
      <g fill="url(#castleGrad)" stroke="rgba(155,183,224,.08)" strokeWidth="1">
        <rect x="720" y="420" width="160" height="280" />
        <polygon points="800,260 770,420 830,420" />
        <rect x="780" y="380" width="40" height="60" />
        <rect x="640" y="500" width="80" height="200" />
        <polygon points="680,400 650,510 710,510" />
        <rect x="880" y="490" width="80" height="210" />
        <polygon points="920,390 890,500 950,500" />
        <rect x="540" y="580" width="50" height="120" />
        <polygon points="565,510 545,590 585,590" />
        <rect x="1010" y="570" width="50" height="130" />
        <polygon points="1035,490 1015,580 1055,580" />
      </g>

      {/* gothic arch window glows on castle */}
      <g fill="rgba(var(--accent-glow),.55)">
        <ellipse cx="800" cy="500" rx="6" ry="14" />
        <ellipse cx="800" cy="560" rx="6" ry="14" />
        <ellipse cx="680" cy="600" rx="5" ry="12" />
        <ellipse cx="920" cy="600" rx="5" ry="12" />
      </g>

      {/* atmospheric mist over base */}
      <rect x="0" y="640" width="1600" height="260" fill="rgba(10,20,40,.45)" />
      <rect x="0" y="780" width="1600" height="120" fill="rgba(60,90,140,.10)" />
    </svg>
  )
}

/* ── Aurora Scene ────────────────────────────────────────────────────────── */
export function AuroraScene() {
  return (
    <svg className="scene-castle" viewBox="0 0 1600 900" preserveAspectRatio="xMidYMid slice">
      <defs>
        <linearGradient id="auroraA" x1="0" y1="0" x2="1" y2="0">
          <stop offset="0" stopColor="rgba(var(--accent-glow),0)" />
          <stop offset=".5" stopColor="rgba(var(--accent-glow),.35)" />
          <stop offset="1" stopColor="rgba(var(--accent-glow),0)" />
        </linearGradient>
        <linearGradient id="auroraB" x1="0" y1="0" x2="1" y2="0">
          <stop offset="0" stopColor="rgba(var(--accent-glow),0)" />
          <stop offset=".5" stopColor="rgba(var(--accent-glow),.22)" />
          <stop offset="1" stopColor="rgba(var(--accent-glow),0)" />
        </linearGradient>
      </defs>
      <path d="M0 280 Q400 180 800 280 T1600 280 L1600 360 Q1200 480 800 360 T0 360 Z" fill="url(#auroraA)">
        <animate
          attributeName="d"
          dur="14s"
          repeatCount="indefinite"
          values="M0 280 Q400 180 800 280 T1600 280 L1600 360 Q1200 480 800 360 T0 360 Z;
                  M0 320 Q400 220 800 320 T1600 320 L1600 400 Q1200 520 800 400 T0 400 Z;
                  M0 280 Q400 180 800 280 T1600 280 L1600 360 Q1200 480 800 360 T0 360 Z"
        />
      </path>
      <path d="M0 480 Q400 380 800 480 T1600 480 L1600 540 Q1200 660 800 540 T0 540 Z" fill="url(#auroraB)">
        <animate
          attributeName="d"
          dur="18s"
          repeatCount="indefinite"
          values="M0 480 Q400 380 800 480 T1600 480 L1600 540 Q1200 660 800 540 T0 540 Z;
                  M0 440 Q400 420 800 440 T1600 440 L1600 500 Q1200 620 800 500 T0 500 Z;
                  M0 480 Q400 380 800 480 T1600 480 L1600 540 Q1200 660 800 540 T0 540 Z"
        />
      </path>
      {/* faint stars */}
      {Array.from({ length: 80 }).map((_, i) => {
        const x = (i * 173) % 1600
        const y = (i * 91) % 900
        const r = 0.4 + ((i * 7) % 6) / 6
        return <circle key={i} cx={x} cy={y} r={r} fill="rgba(216,230,255,.6)" />
      })}
    </svg>
  )
}

/* ── Void Scene ──────────────────────────────────────────────────────────── */
export function VoidScene() {
  return (
    <svg className="scene-castle" viewBox="0 0 1600 900" preserveAspectRatio="xMidYMid slice">
      {Array.from({ length: 180 }).map((_, i) => {
        const x = (i * 173) % 1600
        const y = (i * 91) % 900
        const r = 0.4 + ((i * 13) % 8) / 6
        const a = 0.3 + ((i * 7) % 10) / 18
        return <circle key={i} cx={x} cy={y} r={r} fill={`rgba(216,230,255,${a})`} />
      })}
      <defs>
        <radialGradient id="nebula" cx="50%" cy="50%" r="50%">
          <stop offset="0" stopColor="rgba(var(--accent-glow),.18)" />
          <stop offset="1" stopColor="rgba(var(--accent-glow),0)" />
        </radialGradient>
      </defs>
      <ellipse cx="800" cy="450" rx="600" ry="300" fill="url(#nebula)" />
    </svg>
  )
}

/* ── BlizzardBackground ──────────────────────────────────────────────────── */
export function BlizzardBackground({ scene, snow }: { scene: SceneKind; snow: boolean }) {
  return (
    <div className="scene">
      {scene === 'aurora' && <AuroraScene />}
      {scene === 'void' && <VoidScene />}
      {scene === 'castle' && <CastleScene />}
      <SnowField enabled={snow} />
    </div>
  )
}
