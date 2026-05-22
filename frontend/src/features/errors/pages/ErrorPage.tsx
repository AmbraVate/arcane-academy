import { useState, useCallback, useRef } from 'react'
import { useNavigate } from 'react-router-dom'

/* ── Styles ─────────────────────────────────────────────────────────────── */

const STYLES = `
@keyframes orbPulse {
  0%,100% { box-shadow: 0 0 20px #8b5cf6, 0 0 40px #8b5cf640; }
  50%     { box-shadow: 0 0 40px #c4b5fd, 0 0 80px #8b5cf660, 0 0 120px #8b5cf630; }
}
@keyframes runeActivate {
  0%   { transform: scale(1); }
  40%  { transform: scale(1.18); }
  100% { transform: scale(1); }
}
@keyframes wrongShake {
  0%,100% { transform: translateX(0); }
  20%,60% { transform: translateX(-5px); }
  40%,80% { transform: translateX(5px); }
}
@keyframes matrixDrift {
  0%   { opacity: 0; transform: translateY(-4px); }
  20%  { opacity: 0.6; }
  80%  { opacity: 0.6; }
  100% { opacity: 0; transform: translateY(4px); }
}
`

/* ── Rune game ───────────────────────────────────────────────────────────── */

const RUNES = [
  { glyph: 'ᚱ', name: 'Raidho',  color: '#c9a227' },
  { glyph: 'ᚢ', name: 'Uruz',    color: '#8b5cf6' },
  { glyph: 'ᚾ', name: 'Nauthiz', color: '#2dd4bf' },
  { glyph: 'ᛖ', name: 'Ehwaz',   color: '#f87171' },
  { glyph: 'ᛋ', name: 'Sowilo',  color: '#4ade80' },
]

type GameStatus = 'idle' | 'showing' | 'input' | 'correct' | 'gameover'

function RuneGame() {
  const [sequence, setSequence]       = useState<number[]>([])
  const [playerInput, setPlayerInput] = useState<number[]>([])
  const [status, setStatus]           = useState<GameStatus>('idle')
  const [activeRune, setActiveRune]   = useState<number | null>(null)
  const [score, setScore]             = useState(0)
  const [bestScore, setBestScore]     = useState(0)
  const timeouts = useRef<ReturnType<typeof setTimeout>[]>([])

  // playSequence only touches refs and state setters (both stable), so the
  // empty dependency array is intentional and correct.
  const playSequence = useCallback((seq: number[]) => {
    timeouts.current.forEach(clearTimeout)
    timeouts.current = []
    setStatus('showing')
    setPlayerInput([])
    setActiveRune(null)

    // schedule is scoped here so it can push to the same timeouts ref
    const schedule = (fn: () => void, ms: number) => {
      const t = setTimeout(fn, ms)
      timeouts.current.push(t)
    }

    let delay = 500
    seq.forEach((runeIdx) => {
      schedule(() => setActiveRune(runeIdx), delay)
      schedule(() => setActiveRune(null),    delay + 600)
      delay += 900
    })
    schedule(() => setStatus('input'), delay + 100)
  }, []) // state setters and refs are guaranteed stable by React

  const start = useCallback(() => {
    const firstRune = Math.floor(Math.random() * RUNES.length)
    const newSeq = [firstRune]
    setSequence(newSeq)
    setScore(0)
    playSequence(newSeq)
  }, [playSequence])

  const handleClick = useCallback((idx: number) => {
    if (status !== 'input') return

    const newInput = [...playerInput, idx]
    const pos = newInput.length - 1

    if (idx !== sequence[pos]) {
      timeouts.current.forEach(clearTimeout)
      timeouts.current = []
      setStatus('gameover')
      setBestScore(prev => Math.max(prev, score))
      return
    }

    if (newInput.length === sequence.length) {
      const newScore = score + 1
      setScore(newScore)
      setStatus('correct')
      const t = setTimeout(() => {
        const newSeq = [...sequence, Math.floor(Math.random() * RUNES.length)]
        setSequence(newSeq)
        playSequence(newSeq)
      }, 700)
      timeouts.current.push(t)
    } else {
      setPlayerInput(newInput)
    }
  }, [status, playerInput, sequence, score, playSequence])

  const isClickable = status === 'input'

  return (
    <div>
      <p style={{
        fontFamily: 'var(--font-cinzel)',
        fontSize: 12,
        letterSpacing: '0.12em',
        color: status === 'gameover' ? 'var(--red)' : status === 'correct' ? 'var(--green)' : 'var(--muted)',
        textAlign: 'center',
        marginBottom: 20,
        minHeight: 18,
        transition: 'color 0.3s',
      }}>
        {status === 'idle'     && 'REPEAT THE ANCIENT SEQUENCE'}
        {status === 'showing'  && 'OBSERVE THE RUNES CAREFULLY…'}
        {status === 'input'    && 'NOW REPEAT THE SEQUENCE'}
        {status === 'correct'  && '❆ SEQUENCE COMPLETE — NEXT RUNE ADDED'}
        {status === 'gameover' && `SEQUENCE BROKEN · YOU REACHED LEVEL ${sequence.length}`}
      </p>

      <div style={{
        display: 'flex',
        gap: 12,
        justifyContent: 'center',
        marginBottom: 20,
        animation: status === 'gameover' ? 'wrongShake 0.4s ease' : undefined,
      }}>
        {RUNES.map((rune, idx) => {
          const isActive = activeRune === idx
          return (
            <button
              key={rune.name}
              onClick={() => handleClick(idx)}
              disabled={!isClickable}
              title={rune.name}
              style={{
                width: 60,
                height: 60,
                borderRadius: 10,
                border: `2px solid ${isActive ? rune.color : isClickable ? '#2e2850' : '#1e1a35'}`,
                background: isActive
                  ? `${rune.color}22`
                  : status === 'correct'
                  ? '#4ade8010'
                  : 'var(--card)',
                color: isActive ? rune.color : isClickable ? 'var(--text)' : '#4a4570',
                fontSize: 26,
                cursor: isClickable ? 'pointer' : 'default',
                transition: 'border-color 0.15s, color 0.15s, background 0.15s',
                animation: isActive ? 'runeActivate 0.6s ease' : undefined,
                transform: isActive ? 'scale(1.15)' : 'scale(1)',
                boxShadow: isActive ? `0 0 16px ${rune.color}80` : 'none',
                fontFamily: 'serif',
                lineHeight: 1,
              }}
            >
              {rune.glyph}
            </button>
          )
        })}
      </div>

      <div style={{
        display: 'flex',
        justifyContent: 'center',
        gap: 32,
        marginBottom: 20,
        fontFamily: 'var(--font-cinzel)',
        fontSize: 12,
        letterSpacing: '0.1em',
      }}>
        {status !== 'idle' && (
          <span style={{ color: 'var(--gold)' }}>LEVEL {sequence.length}</span>
        )}
        {status !== 'idle' && (
          <span style={{ color: 'var(--purple-light)' }}>SCORE {score}</span>
        )}
        {bestScore > 0 && (
          <span style={{ color: 'var(--muted)' }}>BEST {bestScore}</span>
        )}
      </div>

      {(status === 'idle' || status === 'gameover') && (
        <div style={{ textAlign: 'center' }}>
          <button
            onClick={start}
            className="btn btn-ghost"
            style={{ fontSize: 12, padding: '8px 20px' }}
          >
            {status === 'idle' ? '⬡ Begin Divination' : '↺ Cast Again'}
          </button>
        </div>
      )}
    </div>
  )
}

/* ── Error page ─────────────────────────────────────────────────────────── */

interface ErrorPageProps {
  type?: 'crash' | 'server'
  onRetry?: () => void
}

const CONFIG = {
  crash: {
    orb:        '💥',
    title:      'An Arcane Anomaly Occurred',
    subtitle:   'An unexpected rupture in the arcane substrate has been detected.',
    detail:     'Our scribes have been notified. You may attempt to reseal the rift or return to the Academy.',
    retryLabel: '⟳ Reseal the Rift',
  },
  server: {
    orb:        '🔮',
    title:      'The Crystal Servers Are Meditating',
    subtitle:   'The arcane systems are temporarily lost in deep contemplation.',
    detail:     'This usually resolves within moments. While they gather their power, practise the Ancient Rune Sequence below.',
    retryLabel: '⟳ Retry Connection',
  },
}

const MATRIX_RUNES = ['ᚱ', 'ᚢ', 'ᚾ', 'ᛖ', 'ᛋ', 'ᚦ', 'ᚨ', 'ᚷ', 'ᚹ', 'ᚺ', 'ᛁ', 'ᛃ']

export default function ErrorPage({ type = 'server', onRetry }: ErrorPageProps) {
  const navigate = useNavigate()
  const cfg = CONFIG[type]

  return (
    <div style={{
      minHeight: '100vh',
      background: 'var(--bg)',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '40px 24px',
      position: 'relative',
      overflow: 'hidden',
    }}>
      <style>{STYLES}</style>

      <div style={{
        position: 'absolute',
        inset: 0,
        overflow: 'hidden',
        pointerEvents: 'none',
        userSelect: 'none',
      }}>
        {MATRIX_RUNES.map((r, i) => (
          <div key={i} style={{
            position: 'absolute',
            top:  `${5 + i * 8}%`,
            left: i % 2 === 0 ? `${3 + i * 3}%` : `${72 + (i % 5) * 5}%`,
            fontSize: 20,
            color: '#2e2850',
            fontFamily: 'serif',
            animation: `matrixDrift ${3 + i * 0.5}s ${i * 0.4}s ease-in-out infinite`,
          }}>{r}</div>
        ))}
      </div>

      <div style={{
        width: 120,
        height: 120,
        borderRadius: '50%',
        background: 'radial-gradient(circle at 35% 35%, #2e1a5e, #0e0c1a)',
        border: '1px solid #8b5cf640',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: 52,
        animation: 'orbPulse 3s ease-in-out infinite',
        marginBottom: 32,
        flexShrink: 0,
      }}>
        {cfg.orb}
      </div>

      <h1 style={{
        fontFamily: 'var(--font-cinzel)',
        fontSize: 'clamp(18px, 4vw, 26px)',
        fontWeight: 700,
        color: 'var(--text)',
        textAlign: 'center',
        marginBottom: 12,
        letterSpacing: '0.06em',
      }}>
        {cfg.title}
      </h1>

      <p style={{
        color: 'var(--purple-light)',
        fontSize: 15,
        textAlign: 'center',
        marginBottom: 8,
        maxWidth: 460,
      }}>
        {cfg.subtitle}
      </p>

      <p style={{
        color: 'var(--muted)',
        fontSize: 14,
        textAlign: 'center',
        maxWidth: 440,
        lineHeight: 1.7,
        marginBottom: 32,
        fontStyle: 'italic',
      }}>
        {cfg.detail}
      </p>

      <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', justifyContent: 'center', marginBottom: 48 }}>
        {onRetry && (
          <button className="btn btn-primary" onClick={onRetry} style={{ padding: '11px 28px' }}>
            {cfg.retryLabel}
          </button>
        )}
        <button className="btn btn-ghost" onClick={() => navigate('/')} style={{ padding: '11px 24px' }}>
          ✦ Return to the Academy
        </button>
      </div>

      <div style={{
        display: 'flex',
        alignItems: 'center',
        gap: 14,
        marginBottom: 28,
        width: '100%',
        maxWidth: 420,
      }}>
        <div style={{ flex: 1, height: 1, background: 'var(--border)' }} />
        <span style={{
          fontFamily: 'var(--font-cinzel)',
          fontSize: 11,
          letterSpacing: '0.2em',
          color: 'var(--muted)',
          whiteSpace: 'nowrap',
        }}>
          WHILE YOU WAIT
        </span>
        <div style={{ flex: 1, height: 1, background: 'var(--border)' }} />
      </div>

      <p style={{
        fontFamily: 'var(--font-cinzel)',
        fontSize: 13,
        color: 'var(--gold)',
        letterSpacing: '0.1em',
        marginBottom: 24,
        textAlign: 'center',
      }}>
        ᚱ Ancient Rune Sequence ᚱ
      </p>

      <div style={{
        background: 'var(--card)',
        border: '1px solid var(--border)',
        borderRadius: 12,
        padding: '28px 32px',
        width: '100%',
        maxWidth: 420,
      }}>
        <RuneGame />
      </div>

      <p style={{
        color: '#3a3560',
        fontSize: 11,
        marginTop: 24,
        fontFamily: 'var(--font-cinzel)',
        letterSpacing: '0.15em',
      }}>
        ✦ ARCANE ACADEMY ✦
      </p>
    </div>
  )
}
