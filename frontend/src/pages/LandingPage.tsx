import { useEffect, useRef, useState, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import * as THREE from 'three'
import { useAuth } from '../hooks/useAuth'

/**
 * Landing — Variant Grimoire (production port).
 *
 * Faithful re-implementation of the design system's
 * `landing/VariantGrimoire.jsx` — a floating WebGL grimoire on the left that
 * idles in slow drift and clicks open to reveal Encode + Recall pages, paired
 * with editorial copy and four sections (How a chunk works, The Difference,
 * Topics, Final CTA).
 *
 * Aesthetic source of truth: arcane-academy-design-system (Cinzel + Crimson Pro,
 * gold/purple/teal/parchment, midnight spellbook).
 */

// ── Scroll-reveal hook (no library) — toggles `.in` once visible ──────────
function useReveal<T extends HTMLElement>() {
  const ref = useRef<T | null>(null)
  useEffect(() => {
    const el = ref.current
    if (!el || typeof IntersectionObserver === 'undefined') return
    const obs = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          el.classList.add('in')
          obs.unobserve(el)
        }
      },
      { threshold: 0.12, rootMargin: '0px 0px -8% 0px' },
    )
    obs.observe(el)
    return () => obs.disconnect()
  }, [])
  return ref
}

// ── Make a parchment-page texture for the inner pages of the open book ────
function makePageTexture(side: 'left' | 'right'): THREE.CanvasTexture {
  const c = document.createElement('canvas')
  c.width = 512
  c.height = 680
  const g = c.getContext('2d')!

  // parchment radial gradient
  const grad = g.createRadialGradient(256, 340, 80, 256, 340, 400)
  grad.addColorStop(0, '#f0e2c0')
  grad.addColorStop(0.6, '#e0cea0')
  grad.addColorStop(1, '#b89760')
  g.fillStyle = grad
  g.fillRect(0, 0, 512, 680)

  // grain noise
  for (let i = 0; i < 1200; i++) {
    g.fillStyle = `rgba(${100 + Math.random() * 60}, ${70 + Math.random() * 40}, ${30 + Math.random() * 30}, ${Math.random() * 0.08})`
    g.fillRect(Math.random() * 512, Math.random() * 680, 1 + Math.random() * 2, 1 + Math.random() * 2)
  }

  // border decoration
  g.strokeStyle = '#8a6510'
  g.lineWidth = 2
  g.strokeRect(30, 30, 452, 620)
  g.lineWidth = 0.5
  g.strokeRect(40, 40, 432, 600)

  // corner ornaments
  g.fillStyle = '#8a6510'
  ;([[40, 40], [472, 40], [40, 640], [472, 640]] as const).forEach(([x, y]) => {
    g.beginPath()
    g.arc(x, y, 6, 0, Math.PI * 2)
    g.fill()
  })

  g.fillStyle = '#3a2208'
  g.font = 'bold 32px Cinzel, serif'
  g.textAlign = 'center'

  if (side === 'left') {
    g.fillText('✦  ENCODE  ✦', 256, 110)
    g.font = 'italic 16px "Crimson Pro", serif'
    const lines = [
      'Each concept arrives wrapped',
      'in story. Velan, your mentor,',
      'sets the scene. You meet the',
      'idea through metaphor —',
      'then write the code.',
      '',
      'Multi-sensory encoding makes',
      'memory five times more',
      'durable than passive reading.',
    ]
    lines.forEach((ln, i) => g.fillText(ln, 256, 180 + i * 26))

    g.font = '40px serif'
    g.fillStyle = '#c9a227'
    g.fillText('᛫ ᛉ ᛏ ᛒ ᛬', 256, 500)
    g.font = 'italic 14px "Crimson Pro", serif'
    g.fillStyle = '#5a3a1a'
    g.fillText('— from the Encoding Codex —', 256, 600)
  } else {
    g.fillText('✦  RECALL  ✦', 256, 110)
    g.font = 'italic 16px "Crimson Pro", serif'
    const lines = [
      'Tomorrow, the Grimoire asks',
      'you to retrieve. No notes.',
      'No re-reading. Active recall,',
      'graded honestly: Again, Hard,',
      'Good, Easy.',
      '',
      'Spaced repetition surfaces',
      'each card just as it begins',
      'to fade. Forgetting is the gift.',
    ]
    lines.forEach((ln, i) => g.fillText(ln, 256, 220 + i * 32))

    g.font = '40px serif'
    g.fillStyle = '#c9a227'
    g.fillText('☉ ☽ ✦ ☉ ☽', 256, 500)
    g.font = 'italic 14px "Crimson Pro", serif'
    g.fillStyle = '#5a3a1a'
    g.fillText('— the second seal —', 256, 600)
  }

  return new THREE.CanvasTexture(c)
}

// ── Small canvas-rendered rune sprite (gold glyph on transparent bg) ──────
function makeRuneTexture(char: string): THREE.CanvasTexture {
  const c = document.createElement('canvas')
  c.width = 128
  c.height = 128
  const g = c.getContext('2d')!
  g.fillStyle = 'rgba(0,0,0,0)'
  g.fillRect(0, 0, 128, 128)
  g.fillStyle = '#f5d189'
  g.font = '96px serif'
  g.textAlign = 'center'
  g.textBaseline = 'middle'
  g.fillText(char, 64, 64)
  return new THREE.CanvasTexture(c)
}

// ── The 3D grimoire scene — encapsulated ──────────────────────────────────
function GrimoireScene({ isOpen, onToggle }: { isOpen: boolean; onToggle: () => void }) {
  const mountRef = useRef<HTMLDivElement | null>(null)
  const isOpenRef = useRef(isOpen)
  isOpenRef.current = isOpen

  useEffect(() => {
    const mount = mountRef.current
    if (!mount) return

    const W = mount.clientWidth
    const H = mount.clientHeight

    // ── Scene setup ──────────────────────────────────────────────────────
    const scene = new THREE.Scene()
    scene.fog = new THREE.Fog(0x0e0c1a, 8, 22)

    const camera = new THREE.PerspectiveCamera(40, W / H, 0.1, 100)
    camera.position.set(0, 0.4, 8)

    const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true })
    renderer.setSize(W, H)
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2))
    renderer.shadowMap.enabled = true
    renderer.shadowMap.type = THREE.PCFSoftShadowMap
    mount.appendChild(renderer.domElement)

    // ── Lighting: candle-warm key + cool purple rim + book-inner glow ────
    scene.add(new THREE.AmbientLight(0x2a1f4a, 0.6))

    const keyLight = new THREE.DirectionalLight(0xffd58a, 1.6)
    keyLight.position.set(3, 4, 5)
    keyLight.castShadow = true
    keyLight.shadow.mapSize.set(1024, 1024)
    keyLight.shadow.camera.near = 0.5
    keyLight.shadow.camera.far = 20
    scene.add(keyLight)

    const rimLight = new THREE.DirectionalLight(0x8b5cf6, 0.8)
    rimLight.position.set(-3, 2, -2)
    scene.add(rimLight)

    const innerGlow = new THREE.PointLight(0xc9a227, 0, 4, 2)
    innerGlow.position.set(0, 0.2, 0.4)
    scene.add(innerGlow)

    // ── Book group ───────────────────────────────────────────────────────
    const book = new THREE.Group()
    scene.add(book)

    const coverW = 2.6
    const coverH = 3.4
    const coverT = 0.18

    const leatherMat = new THREE.MeshStandardMaterial({
      color: 0x2a1408,
      roughness: 0.85,
      metalness: 0.15,
      emissive: 0x1a0a04,
      emissiveIntensity: 0.2,
    })

    // Back cover (stays put)
    const backCover = new THREE.Mesh(new THREE.BoxGeometry(coverW, coverH, coverT), leatherMat)
    backCover.position.set(0, 0, -coverT * 0.6)
    backCover.castShadow = true
    backCover.receiveShadow = true
    book.add(backCover)

    // Front cover (the one that opens) — pivoted on the spine
    const frontCoverPivot = new THREE.Group()
    frontCoverPivot.position.set(-coverW / 2, 0, coverT * 0.6)
    book.add(frontCoverPivot)

    const frontCover = new THREE.Mesh(new THREE.BoxGeometry(coverW, coverH, coverT), leatherMat)
    frontCover.position.set(coverW / 2, 0, 0)
    frontCover.castShadow = true
    frontCover.receiveShadow = true
    frontCoverPivot.add(frontCover)

    // Gold foil emblem on front cover — ring + 8-point star
    const emblemMat = new THREE.MeshStandardMaterial({
      color: 0xc9a227,
      roughness: 0.25,
      metalness: 0.95,
      emissive: 0xc9a227,
      emissiveIntensity: 0.3,
    })

    const ring = new THREE.Mesh(new THREE.TorusGeometry(0.55, 0.03, 16, 64), emblemMat)
    ring.position.set(coverW / 2, 0, coverT / 2 + 0.001)
    frontCoverPivot.add(ring)

    const starShape = new THREE.Shape()
    const sp = 0.4
    starShape.moveTo(0, sp)
    for (let i = 0; i < 8; i++) {
      const a = (i / 8) * Math.PI * 2 + Math.PI / 2
      const r = i % 2 === 0 ? sp : sp * 0.4
      starShape.lineTo(Math.cos(a) * r, Math.sin(a) * r)
    }
    const starGeo = new THREE.ExtrudeGeometry(starShape, {
      depth: 0.025,
      bevelEnabled: true,
      bevelSize: 0.01,
      bevelThickness: 0.01,
      bevelSegments: 2,
    })
    const star = new THREE.Mesh(starGeo, emblemMat)
    star.position.set(coverW / 2, 0, coverT / 2 + 0.005)
    frontCoverPivot.add(star)

    // Gold foil corner studs
    for (const [cx, cy] of [[-1, 1], [1, 1], [-1, -1], [1, -1]] as const) {
      const corner = new THREE.Mesh(new THREE.BoxGeometry(0.4, 0.4, 0.02), emblemMat)
      corner.position.set(
        coverW / 2 + cx * (coverW / 2 - 0.25),
        cy * (coverH / 2 - 0.25),
        coverT / 2 + 0.001,
      )
      frontCoverPivot.add(corner)
    }

    // Pages stack between covers
    const pagesMat = new THREE.MeshStandardMaterial({
      color: 0xe8d9b8,
      roughness: 0.95,
      metalness: 0,
    })
    const pages = new THREE.Mesh(
      new THREE.BoxGeometry(coverW - 0.1, coverH - 0.1, coverT * 0.8),
      pagesMat,
    )
    book.add(pages)

    // Gold gilt edges on pages
    const giltMat = new THREE.MeshStandardMaterial({
      color: 0xd4a437,
      roughness: 0.3,
      metalness: 0.9,
      emissive: 0xc9a227,
      emissiveIntensity: 0.15,
    })
    const giltTop = new THREE.Mesh(
      new THREE.BoxGeometry(coverW - 0.1, 0.04, coverT * 0.8),
      giltMat,
    )
    giltTop.position.set(0, coverH / 2 - 0.04, 0)
    book.add(giltTop)

    const giltBot = giltTop.clone()
    giltBot.position.y = -coverH / 2 + 0.04
    book.add(giltBot)

    const giltRight = new THREE.Mesh(
      new THREE.BoxGeometry(0.04, coverH - 0.1, coverT * 0.8),
      giltMat,
    )
    giltRight.position.set(coverW / 2 - 0.04, 0, 0)
    book.add(giltRight)

    // ── Inner pages (visible only when open) ─────────────────────────────
    const innerPagesGroup = new THREE.Group()
    innerPagesGroup.visible = false
    book.add(innerPagesGroup)

    const innerLeftMat = new THREE.MeshStandardMaterial({
      map: makePageTexture('left'),
      roughness: 0.95,
      metalness: 0,
      emissive: 0xc9a227,
      emissiveIntensity: 0,
    })
    const innerRightMat = new THREE.MeshStandardMaterial({
      map: makePageTexture('right'),
      roughness: 0.95,
      metalness: 0,
      emissive: 0xc9a227,
      emissiveIntensity: 0,
    })

    const innerLeft = new THREE.Mesh(
      new THREE.PlaneGeometry(coverW - 0.15, coverH - 0.15),
      innerLeftMat,
    )
    innerLeft.position.set(-coverW / 2, 0, coverT * 0.5)
    innerPagesGroup.add(innerLeft)

    const innerRight = new THREE.Mesh(
      new THREE.PlaneGeometry(coverW - 0.15, coverH - 0.15),
      innerRightMat,
    )
    innerRight.position.set(coverW / 2, 0, coverT * 0.5)
    innerPagesGroup.add(innerRight)

    // ── Floating runes orbiting around the book ──────────────────────────
    const runesGroup = new THREE.Group()
    scene.add(runesGroup)

    const runeChars = ['✦', '☉', '☽', '◈', '✧', '⟡', '◇']
    type Rune = { sprite: THREE.Sprite; angle: number; radius: number; ySeed: number; baseScale: number }
    const runes: Rune[] = []

    for (let i = 0; i < 12; i++) {
      const tex = makeRuneTexture(runeChars[i % runeChars.length])
      const mat = new THREE.SpriteMaterial({
        map: tex,
        transparent: true,
        opacity: 0.7,
        blending: THREE.AdditiveBlending,
      })
      const sprite = new THREE.Sprite(mat)
      const angle = (i / 12) * Math.PI * 2
      const radius = 2.6 + (i % 3) * 0.4
      sprite.position.set(
        Math.cos(angle) * radius,
        Math.sin(angle * 1.3) * 1.4 + (i % 2 ? 0.3 : -0.3),
        Math.sin(angle) * radius - 0.5,
      )
      const baseScale = 0.3 + Math.random() * 0.25
      sprite.scale.set(baseScale, baseScale, 1)
      runesGroup.add(sprite)
      runes.push({ sprite, angle, radius, ySeed: i * 0.7, baseScale })
    }

    // ── Open / close interaction (click anywhere on the canvas) ──────────
    let openProgress = 0
    let targetOpen = isOpen ? 1 : 0

    const handleClick = () => {
      onToggle()
    }
    renderer.domElement.style.cursor = 'pointer'
    renderer.domElement.addEventListener('click', handleClick)

    // ── Animation loop ───────────────────────────────────────────────────
    const clock = new THREE.Clock()
    let raf = 0

    const animate = () => {
      const t = clock.getElapsedTime()

      // Sync target with React state
      targetOpen = isOpenRef.current ? 1 : 0

      // Idle drift
      book.position.y = Math.sin(t * 0.6) * 0.12
      book.rotation.y = -0.35 + Math.sin(t * 0.4) * 0.18
      book.rotation.x = Math.sin(t * 0.5) * 0.06 - 0.05
      book.rotation.z = Math.sin(t * 0.3) * 0.04

      // Open animation — smooth lerp toward target
      openProgress += (targetOpen - openProgress) * 0.06
      frontCoverPivot.rotation.y = -openProgress * (Math.PI * 0.83)

      // Reveal inner pages once cover lifts
      innerPagesGroup.visible = openProgress > 0.05
      innerLeftMat.emissiveIntensity = openProgress * 0.35
      innerRightMat.emissiveIntensity = openProgress * 0.35

      // Emblem glow pulse
      const pulse = 0.3 + Math.sin(t * 1.6) * 0.15 + openProgress * 0.4
      emblemMat.emissiveIntensity = pulse

      // Inner glow ramps up as it opens
      innerGlow.intensity = openProgress * 2.4 + Math.sin(t * 3) * 0.2 * openProgress

      // Floating runes
      runes.forEach((r, i) => {
        const a = r.angle + t * 0.08
        r.sprite.position.x = Math.cos(a) * r.radius
        r.sprite.position.z = Math.sin(a) * r.radius - 0.5
        r.sprite.position.y = Math.sin(t * 0.7 + r.ySeed) * 0.5 + (i % 2 ? 0.3 : -0.3)
        const opacity = 0.4 + Math.sin(t * 1.2 + r.ySeed) * 0.3 + openProgress * 0.3
        ;(r.sprite.material as THREE.SpriteMaterial).opacity = opacity
        const sc = r.baseScale * (0.85 + Math.sin(t * 1.5 + r.ySeed) * 0.15)
        r.sprite.scale.set(sc, sc, 1)
      })

      renderer.render(scene, camera)
      raf = requestAnimationFrame(animate)
    }
    animate()

    // ── Resize ───────────────────────────────────────────────────────────
    const onResize = () => {
      const w = mount.clientWidth
      const h = mount.clientHeight
      camera.aspect = w / h
      camera.updateProjectionMatrix()
      renderer.setSize(w, h)
    }
    window.addEventListener('resize', onResize)

    // ── Cleanup ──────────────────────────────────────────────────────────
    return () => {
      cancelAnimationFrame(raf)
      window.removeEventListener('resize', onResize)
      renderer.domElement.removeEventListener('click', handleClick)
      // Dispose textures
      innerLeftMat.map?.dispose()
      innerRightMat.map?.dispose()
      runes.forEach(r => (r.sprite.material as THREE.SpriteMaterial).map?.dispose())
      // Dispose materials + geometries
      scene.traverse(obj => {
        if (obj instanceof THREE.Mesh) {
          obj.geometry.dispose()
          if (Array.isArray(obj.material)) obj.material.forEach(m => m.dispose())
          else obj.material.dispose()
        }
      })
      renderer.dispose()
      if (mount.contains(renderer.domElement)) mount.removeChild(renderer.domElement)
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return <div ref={mountRef} className="w-full h-[720px] relative max-md:h-[440px]" />
}

// ── Page ──────────────────────────────────────────────────────────────────
export default function LandingPage() {
  const navigate = useNavigate()
  const { user } = useAuth()
  const [isOpen, setIsOpen] = useState(false)

  const ctaDest = user ? '/topics' : '/register'
  const goCta = useCallback(() => navigate(ctaDest), [navigate, ctaDest])
  const goLogin = useCallback(() => navigate('/login'), [navigate])

  // Anchor-scroll for nav links
  const scrollTo = (id: string) => () => {
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }

  // Reveal hooks for each scroll section
  const ref1 = useReveal<HTMLElement>()
  const ref2 = useReveal<HTMLElement>()
  const ref3 = useReveal<HTMLElement>()
  const ref4 = useReveal<HTMLElement>()

  // Stable starfield positions
  const stars = Array.from({ length: 60 }).map((_, i) => ({
    x: (i * 137) % 100,
    y: (i * 73) % 100,
    r: 0.4 + ((i * 7) % 4) / 4,
    delay: (i * 0.13) % 4,
  }))

  return (
    <div
      className="absolute inset-0 overflow-y-auto overflow-x-hidden text-text font-crimson"
      style={{ background: 'var(--bg)' }}
    >
      {/* ── Candle-light background glow ─────────────────────────────────── */}
      <div
        className="fixed inset-0 pointer-events-none"
        style={{
          background:
            'radial-gradient(ellipse at 30% 40%, rgba(201,162,39,0.2) 0%, transparent 50%), radial-gradient(ellipse at 80% 70%, rgba(139,92,246,0.1) 0%, transparent 50%)',
          zIndex: 0,
        }}
      />

      {/* ── Twinkling stars ──────────────────────────────────────────────── */}
      <svg
        className="fixed inset-0 w-full h-full pointer-events-none"
        style={{ zIndex: 1 }}
        aria-hidden="true"
      >
        {stars.map((s, i) => (
          <circle
            key={i}
            className="star"
            cx={`${s.x}%`}
            cy={`${s.y}%`}
            r={s.r}
            fill="#f5d189"
            style={{ animationDelay: `${s.delay}s` }}
          />
        ))}
      </svg>

      {/* ── Sticky-feel header (not actually sticky to keep candle vibe) ── */}
      <header className="relative z-10 flex items-center justify-between px-12 py-[22px] max-md:px-6 max-md:py-4">
        <div className="brand-mark foil-on-dark cursor-pointer" onClick={() => navigate('/')}>
          ✦ ARCANE ACADEMY
        </div>
        <div className="flex items-center gap-3 max-md:hidden">
          <a
            className="font-cinzel text-[12px] tracking-[2px] text-muted cursor-pointer hover:text-text transition-colors"
            onClick={scrollTo('how')}
          >
            HOW IT WORKS
          </a>
          <a
            className="font-cinzel text-[12px] tracking-[2px] text-muted cursor-pointer hover:text-text transition-colors"
            onClick={scrollTo('difference')}
          >
            METHOD
          </a>
          <a
            className="font-cinzel text-[12px] tracking-[2px] text-muted cursor-pointer hover:text-text transition-colors"
            onClick={goLogin}
          >
            ENTER
          </a>
          <button className="btn-seal text-[12px]" onClick={goCta} style={{ padding: '10px 22px' }}>
            BEGIN
          </button>
        </div>
        <button
          className="btn-seal text-[12px] hidden max-md:inline-flex"
          onClick={goCta}
          style={{ padding: '8px 16px' }}
        >
          BEGIN
        </button>
      </header>

      {/* ── HERO — split: 3D book left, copy right ───────────────────────── */}
      <section className="relative z-[2] px-6 pt-10 pb-24 max-md:pt-4 max-md:pb-16">
        <div className="max-w-[1340px] mx-auto grid items-center gap-10 lg:grid-cols-[1.1fr_0.9fr] lg:min-h-[720px]">
          {/* LEFT — Three.js mount */}
          <div className="relative">
            <GrimoireScene isOpen={isOpen} onToggle={() => setIsOpen(o => !o)} />
            <div
              className="absolute bottom-7 left-1/2 -translate-x-1/2 pointer-events-none whitespace-nowrap text-center"
              style={{
                fontFamily: 'Cinzel, serif',
                fontSize: 11,
                letterSpacing: 4,
                color: 'var(--gold-foil)',
                opacity: isOpen ? 0.4 : 0.85,
                transition: 'opacity .5s',
              }}
            >
              {isOpen ? '✦ THE GRIMOIRE IS OPEN ✦' : '— CLICK THE GRIMOIRE —'}
            </div>
          </div>

          {/* RIGHT — copy */}
          <div>
            <div className="section-eyebrow" style={{ fontSize: 11, letterSpacing: 5, marginBottom: 20 }}>
              EST. IN THE AGE OF FORGETTING
            </div>
            <h1
              className="text-text"
              style={{
                fontFamily: 'Cinzel, serif',
                fontSize: 'clamp(40px, 7vw, 72px)',
                fontWeight: 700,
                lineHeight: 1.02,
                letterSpacing: '0.005em',
                margin: '0 0 24px',
              }}
            >
              Learn anything.<br />
              <span className="foil-on-dark italic">Forget</span> nothing.
            </h1>
            <p
              className="text-muted italic"
              style={{
                fontFamily: 'Crimson Pro, serif',
                fontSize: 21,
                lineHeight: 1.55,
                maxWidth: 480,
                margin: '0 0 28px',
              }}
            >
              Every other course you've taken poured information into a leaky bucket.
              We rebuild the bucket — using the six techniques cognitive scientists already know work.
            </p>

            {/* Three differentiator bullets */}
            <div className="flex flex-col gap-3.5 mb-9 max-w-[500px]">
              {(
                [
                  ['🧩', 'Story-encoded chunks', 'Concepts arrive wrapped in narrative — not lecture slides.'],
                  ['🎯', 'Recall, not re-read',  'The Grimoire schedules retrieval, not video re-watches.'],
                  ['🪶', 'Teach to prove it',    'Every chunk ends with you explaining it back, Feynman-style.'],
                ] as const
              ).map(([icon, title, body]) => (
                <div
                  key={title}
                  className="flex items-start gap-3.5"
                  style={{
                    padding: '14px 18px',
                    background: 'rgba(30, 26, 53, 0.6)',
                    border: '1px solid rgba(201,162,39,0.18)',
                    borderRadius: 6,
                    backdropFilter: 'blur(4px)',
                  }}
                >
                  <div style={{ fontSize: 22, marginTop: 1 }}>{icon}</div>
                  <div>
                    <div
                      style={{
                        fontFamily: 'Cinzel, serif',
                        fontSize: 14,
                        fontWeight: 600,
                        color: 'var(--gold-foil)',
                        letterSpacing: 1,
                        marginBottom: 3,
                      }}
                    >
                      {title}
                    </div>
                    <div
                      style={{
                        fontFamily: 'Crimson Pro, serif',
                        fontSize: 15,
                        color: 'var(--muted)',
                        lineHeight: 1.5,
                      }}
                    >
                      {body}
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <div className="flex flex-wrap items-center gap-3.5">
              <button className="btn-seal" onClick={goCta} style={{ fontSize: 14, padding: '16px 36px' }}>
                BEGIN YOUR JOURNEY
              </button>
              <a
                onClick={scrollTo('how')}
                className="cursor-pointer"
                style={{
                  fontFamily: 'Cinzel, serif',
                  fontSize: 12,
                  letterSpacing: 3,
                  color: 'var(--muted)',
                  borderBottom: '1px solid var(--gold-foil-deep)',
                  paddingBottom: 2,
                }}
              >
                SEE THE METHOD ↓
              </a>
            </div>

            <div
              className="mt-7"
              style={{
                fontFamily: 'Cinzel, serif',
                fontSize: 10,
                letterSpacing: 3,
                color: 'var(--gold-foil-deep)',
              }}
            >
              ✦ FREE TO ENROL · NO CREDIT CARD · NO TIMETABLE ✦
            </div>
          </div>
        </div>
      </section>

      {/* ── HOW A CHUNK WORKS — 4-stage flow ─────────────────────────────── */}
      <section
        ref={ref1}
        id="how"
        className="grim-reveal relative z-[2] py-[100px] max-md:py-16 px-6"
        style={{ borderTop: '1px solid var(--border)' }}
      >
        <div className="max-w-[1200px] mx-auto">
          <div className="text-center mb-16">
            <div className="ornate-divider">HOW A CHUNK WORKS</div>
            <h2 className="section-title-grimoire foil-on-dark mt-6">
              One concept.<br />Four rituals. One day.
            </h2>
            <p className="section-sub-grimoire mx-auto mt-5">
              Every topic is broken into <em>chunks</em> — bite-sized concepts that pass through four stages of encoding.
              Skip none. Skip nothing.
            </p>
          </div>

          <div className="grid grid-cols-4 gap-0 relative max-md:grid-cols-1 max-md:gap-10">
            {/* Horizontal connecting flow line — desktop only */}
            <div
              className="absolute top-[50px] hidden md:block"
              style={{
                left: '8%',
                right: '8%',
                height: 2,
                background:
                  'linear-gradient(90deg, transparent, var(--gold-foil-deep) 8%, var(--gold-foil-deep) 92%, transparent)',
                zIndex: 0,
              }}
            />

            {(
              [
                ['01', '📖', 'HOOK',       'Velan opens with a story. Why does this matter? When will you reach for it? Emotional encoding starts here.', '#c9a227'],
                ['02', '⚡', 'ENCODE',     'Visual + narrative + analogy. You meet the concept three ways before writing a single line of code.',          '#8b5cf6'],
                ['03', '🎯', 'PRACTISE',   'Now you write it. Failing tests are good. The struggle is the encoding.',                                       '#0d9488'],
                ['04', '🪶', 'TEACH BACK', 'Explain it in your own words. The Feynman seal closes the chunk — and proves you have it.',                     '#d4a437'],
              ] as const
            ).map(([n, icon, title, body, accent]) => (
              <div
                key={n}
                className="relative z-[1] px-4 text-center"
              >
                <div
                  className="mx-auto mb-6 flex items-center justify-center relative"
                  style={{
                    width: 92,
                    height: 92,
                    borderRadius: '50%',
                    background: 'radial-gradient(circle at 35% 30%, #2a1f4a, #16132b 70%)',
                    border: `2px solid ${accent}`,
                    fontSize: 38,
                    boxShadow: `0 0 32px ${accent}44`,
                  }}
                >
                  {icon}
                  <div
                    className="absolute flex items-center justify-center"
                    style={{
                      top: -10,
                      right: -10,
                      width: 32,
                      height: 32,
                      borderRadius: '50%',
                      background: accent,
                      color: '#16132b',
                      fontFamily: 'Cinzel, serif',
                      fontSize: 12,
                      fontWeight: 700,
                    }}
                  >
                    {n}
                  </div>
                </div>
                <div
                  className="foil-on-dark mb-2.5"
                  style={{
                    fontFamily: 'Cinzel, serif',
                    fontSize: 16,
                    fontWeight: 600,
                    letterSpacing: 3,
                    color: accent,
                  }}
                >
                  {title}
                </div>
                <div
                  className="italic"
                  style={{
                    fontFamily: 'Crimson Pro, serif',
                    fontSize: 15,
                    color: 'var(--muted)',
                    lineHeight: 1.6,
                  }}
                >
                  {body}
                </div>
              </div>
            ))}
          </div>

          <div
            className="mt-16 text-center italic"
            style={{
              fontFamily: 'Crimson Pro, serif',
              fontSize: 16,
              color: 'var(--muted)',
            }}
          >
            <span style={{ color: 'var(--gold-foil)' }}>+ Day 2:</span>{' '}
            The Grimoire returns with retrieval cards.
            <span style={{ margin: '0 12px', color: 'var(--gold-foil-deep)' }}>·</span>
            <span style={{ color: 'var(--gold-foil)' }}>+ Day 7, 16, 35:</span>{' '}
            Spaced repetition until it's permanent.
          </div>
        </div>
      </section>

      {/* ── WHY WE'RE DIFFERENT — comparison table ───────────────────────── */}
      <section
        ref={ref2}
        id="difference"
        className="grim-reveal relative z-[2] py-[100px] max-md:py-16 px-6"
        style={{
          background: 'var(--surface)',
          borderTop: '1px solid var(--border)',
          borderBottom: '1px solid var(--border)',
        }}
      >
        <div className="max-w-[1100px] mx-auto">
          <div className="text-center mb-14">
            <div className="ornate-divider">THE DIFFERENCE</div>
            <h2 className="section-title-grimoire foil-on-dark mt-6">Not another course platform.</h2>
            <p className="section-sub-grimoire mx-auto mt-5">
              Most learning platforms optimise for completion. We optimise for <em>retention</em>.
            </p>
          </div>

          {/* The comparison table */}
          <div
            className="grid overflow-hidden max-md:grid-cols-[1.4fr_1fr] max-md:[&_>_div:nth-child(3n)]:hidden"
            style={{
              background: 'var(--card)',
              borderRadius: 12,
              border: '1px solid var(--border)',
              gridTemplateColumns: '1.4fr 1fr 1fr',
            }}
          >
            {/* Header row */}
            <div
              style={{
                padding: '24px 28px',
                fontFamily: 'Cinzel, serif',
                fontSize: 11,
                letterSpacing: 3,
                color: 'var(--muted)',
                borderBottom: '1px solid var(--border)',
              }}
            >
              {' '}
            </div>
            <div
              style={{
                padding: '24px 28px',
                textAlign: 'center',
                borderBottom: '1px solid var(--border)',
                borderLeft: '1px solid var(--border)',
                background: 'rgba(0,0,0,0.2)',
              }}
            >
              <div
                style={{
                  fontFamily: 'Cinzel, serif',
                  fontSize: 11,
                  letterSpacing: 3,
                  color: 'var(--muted)',
                  marginBottom: 6,
                }}
              >
                TYPICAL
              </div>
              <div
                style={{
                  fontFamily: 'Cinzel, serif',
                  fontSize: 18,
                  color: 'var(--text)',
                  fontWeight: 600,
                }}
              >
                Course platform
              </div>
            </div>
            <div
              style={{
                padding: '24px 28px',
                textAlign: 'center',
                borderBottom: '1px solid var(--gold-foil)',
                borderLeft: '1px solid var(--gold-foil)',
                borderTop: '2px solid var(--gold-foil)',
                background: 'linear-gradient(180deg, rgba(201,162,39,0.12), rgba(201,162,39,0.04))',
              }}
            >
              <div
                className="foil-on-dark"
                style={{
                  fontFamily: 'Cinzel, serif',
                  fontSize: 11,
                  letterSpacing: 3,
                  marginBottom: 6,
                }}
              >
                ✦ THE ACADEMY ✦
              </div>
              <div
                className="foil-on-dark"
                style={{ fontFamily: 'Cinzel, serif', fontSize: 18, fontWeight: 600 }}
              >
                Arcane Academy
              </div>
            </div>

            {(
              [
                ['Delivery format',    'Long video lectures',          'Story-driven chunks'],
                ['Pacing',             'You watch on autoplay',        'You retrieve on schedule'],
                ['Progress signal',    '% video watched',              '% concept retained (tested)'],
                ['Memory model',       'Read once, hope',              'Active recall + spaced repetition'],
                ['Mentorship',         'Forum thread',                 'Velan, your live mentor in every chunk'],
                ['Proof of mastery',   'Completion badge',             'Teach-back + interleaved retrieval'],
                ['Across topics',      'Each course is a silo',        'Polymath ladder rewards breadth'],
              ] as const
            ).map(([row, them, us], i) => (
              <div className="contents" key={row}>
                <div
                  style={{
                    padding: '20px 28px',
                    fontFamily: 'Crimson Pro, serif',
                    fontSize: 16,
                    color: 'var(--text)',
                    borderBottom: i === 6 ? 'none' : '1px solid var(--border)',
                    fontWeight: 500,
                  }}
                >
                  {row}
                </div>
                <div
                  style={{
                    padding: '20px 28px',
                    textAlign: 'center',
                    fontFamily: 'Crimson Pro, serif',
                    fontSize: 15,
                    color: 'var(--muted)',
                    fontStyle: 'italic',
                    borderBottom: i === 6 ? 'none' : '1px solid var(--border)',
                    borderLeft: '1px solid var(--border)',
                  }}
                >
                  {them}
                </div>
                <div
                  style={{
                    padding: '20px 28px',
                    textAlign: 'center',
                    fontFamily: 'Crimson Pro, serif',
                    fontSize: 15,
                    color: 'var(--text)',
                    fontWeight: 500,
                    borderBottom: i === 6 ? 'none' : '1px solid rgba(201,162,39,0.3)',
                    borderLeft: '1px solid var(--gold-foil)',
                    background: 'rgba(201,162,39,0.04)',
                  }}
                >
                  {us}
                </div>
              </div>
            ))}
          </div>

          <div className="mt-10 max-w-[900px] mx-auto grid grid-cols-3 gap-4 max-md:grid-cols-1">
            {(
              [
                ['5×', 'retention vs. passive video'],
                ['3×', 'recall vs. re-reading'],
                ['↓60%', 'time-to-mastery on equivalent topics'],
              ] as const
            ).map(([n, l]) => (
              <div
                key={l}
                style={{
                  textAlign: 'center',
                  padding: 20,
                  background: 'var(--card)',
                  border: '1px solid var(--border)',
                  borderRadius: 8,
                }}
              >
                <div
                  className="foil-on-dark"
                  style={{
                    fontFamily: 'Cinzel, serif',
                    fontSize: 38,
                    fontWeight: 700,
                    marginBottom: 6,
                  }}
                >
                  {n}
                </div>
                <div
                  style={{
                    fontFamily: 'Cinzel, serif',
                    fontSize: 11,
                    letterSpacing: 2,
                    color: 'var(--muted)',
                    textTransform: 'uppercase',
                  }}
                >
                  {l}
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── TOPICS ─────────────────────────────────────────────────────── */}
      <section ref={ref3} className="grim-reveal relative z-[2] py-[100px] max-md:py-16 px-6">
        <div className="max-w-[1100px] mx-auto">
          <div className="text-center mb-14">
            <div className="ornate-divider">THE GRIMOIRES</div>
            <h2 className="section-title-grimoire foil-on-dark mt-6">Pick your first tower.</h2>
          </div>
          <div className="grid grid-cols-4 gap-4 max-md:grid-cols-2 max-md:gap-3">
            {(
              [
                ['☕',  'Java',         'OO mastery from `Hello, World!` to concurrent systems.', 'OPEN'],
                ['🎨', 'Tailwind CSS', 'Utility-first styling. Atomic spells, composed.',         'OPEN'],
                ['⚛️', 'React',        'Components, hooks, and the modern frontend grimoire.',    'OPEN'],
                ['🗃️', 'SQL',          'Read, filter, summarise — the language of data.',         'OPEN'],
              ] as const
            ).map(([icon, name, desc, status]) => (
              <button
                key={name}
                onClick={status === 'OPEN' ? goCta : undefined}
                disabled={status !== 'OPEN'}
                className="text-left relative transition-[transform,border-color,background] duration-200"
                style={{
                  background: 'var(--card)',
                  border: '1px solid var(--border)',
                  borderRadius: 10,
                  padding: 24,
                  opacity: status === 'OPEN' ? 1 : 0.65,
                  cursor: status === 'OPEN' ? 'pointer' : 'not-allowed',
                  fontFamily: 'inherit',
                }}
                onMouseEnter={e => {
                  if (status === 'OPEN') {
                    e.currentTarget.style.transform = 'translateY(-3px)'
                    e.currentTarget.style.borderColor = 'var(--gold-foil)'
                  }
                }}
                onMouseLeave={e => {
                  e.currentTarget.style.transform = ''
                  e.currentTarget.style.borderColor = 'var(--border)'
                }}
              >
                <div style={{ fontSize: 40, marginBottom: 14 }}>{icon}</div>
                <div
                  className="foil-on-dark"
                  style={{ fontFamily: 'Cinzel, serif', fontSize: 18, fontWeight: 600 }}
                >
                  {name}
                </div>
                <div
                  style={{
                    fontFamily: 'Crimson Pro, serif',
                    fontSize: 13,
                    color: 'var(--muted)',
                    lineHeight: 1.55,
                    marginTop: 8,
                    fontStyle: status === 'OPEN' ? 'normal' : 'italic',
                  }}
                >
                  {desc}
                </div>
                <div
                  className="absolute"
                  style={{
                    top: 14,
                    right: 14,
                    fontFamily: 'Cinzel, serif',
                    fontSize: 9,
                    letterSpacing: 2,
                    padding: '3px 8px',
                    borderRadius: 4,
                    background: status === 'OPEN' ? 'var(--teal-dim)' : 'var(--border)',
                    color: status === 'OPEN' ? 'var(--teal)' : 'var(--muted)',
                    border: status === 'OPEN' ? '1px solid var(--teal)' : 'none',
                  }}
                >
                  {status}
                </div>
              </button>
            ))}
          </div>
        </div>
      </section>

      {/* ── FINAL CTA ───────────────────────────────────────────────────── */}
      <section
        ref={ref4}
        className="grim-reveal relative z-[2] text-center"
        style={{ padding: '110px 24px 60px' }}
      >
        <div
          className="absolute inset-0 pointer-events-none"
          style={{
            background:
              'radial-gradient(ellipse at center, rgba(201,162,39,0.18) 0%, transparent 60%)',
          }}
        />
        <div className="relative max-w-[720px] mx-auto">
          <div className="ornate-divider">THE THRESHOLD</div>
          <h2
            className="section-title-grimoire foil-on-dark mt-6"
            style={{ fontSize: 'clamp(40px, 6vw, 56px)' }}
          >
            Open the Grimoire.
          </h2>
          <p className="section-sub-grimoire mx-auto" style={{ margin: '20px auto 36px' }}>
            Take the entry diagnostic. The Academy adapts to where you are — and the first chunk is always
            the easiest one you've already half-mastered.
          </p>
          <button className="btn-seal" onClick={goCta} style={{ fontSize: 16, padding: '18px 48px', marginBottom: 24 }}>
            BEGIN YOUR JOURNEY
          </button>
          <div
            className="mt-20"
            style={{
              fontFamily: 'Cinzel, serif',
              fontSize: 11,
              letterSpacing: 4,
              color: 'var(--muted)',
            }}
          >
            BUILT ON SCIENCE · WRAPPED IN STORY
          </div>
        </div>
      </section>
    </div>
  )
}
