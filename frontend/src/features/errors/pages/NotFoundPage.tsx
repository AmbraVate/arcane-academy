import { useNavigate } from 'react-router-dom'

const STYLES = `
@keyframes drift {
  0%,100% { transform: translateY(0px) rotate(0deg); opacity: 0.7; }
  33%      { transform: translateY(-14px) rotate(3deg); opacity: 1; }
  66%      { transform: translateY(-6px) rotate(-2deg); opacity: 0.85; }
}
@keyframes starPulse {
  0%,100% { opacity: 0.3; transform: scale(1); }
  50%     { opacity: 1;   transform: scale(1.4); }
}
@keyframes fogDrift {
  0%   { transform: translateX(-10%) scaleX(1.1); opacity: 0.5; }
  50%  { transform: translateX(5%)  scaleX(0.95); opacity: 0.7; }
  100% { transform: translateX(-10%) scaleX(1.1); opacity: 0.5; }
}
@keyframes voidGlow {
  0%,100% { box-shadow: 0 0 60px 20px #8b5cf630, 0 0 120px 40px #8b5cf615; }
  50%     { box-shadow: 0 0 80px 30px #8b5cf640, 0 0 160px 60px #8b5cf620; }
}
`

const STARS = [
  { top: '8%',  left: '12%', size: 6,  delay: '0s',    dur: '3.1s' },
  { top: '14%', left: '78%', size: 4,  delay: '0.7s',  dur: '2.4s' },
  { top: '5%',  left: '45%', size: 8,  delay: '1.2s',  dur: '3.8s' },
  { top: '22%', left: '88%', size: 5,  delay: '0.3s',  dur: '2.9s' },
  { top: '30%', left: '5%',  size: 4,  delay: '1.8s',  dur: '3.3s' },
  { top: '18%', left: '58%', size: 3,  delay: '0.5s',  dur: '4.1s' },
  { top: '40%', left: '92%', size: 6,  delay: '2.1s',  dur: '2.7s' },
  { top: '55%', left: '3%',  size: 3,  delay: '0.9s',  dur: '3.6s' },
]

const VOID_RUNES = ['ᚱ', 'ᚢ', 'ᚾ', 'ᛖ', 'ᛋ', 'ᚦ', 'ᚨ', 'ᚷ']

export default function NotFoundPage() {
  const navigate = useNavigate()

  return (
    <div style={{
      minHeight: '100vh',
      background: 'var(--bg)',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      position: 'relative',
      overflow: 'hidden',
      padding: '40px 24px',
    }}>
      <style>{STYLES}</style>

      {/* Floating stars */}
      {STARS.map((s, i) => (
        <div key={i} style={{
          position: 'absolute',
          top: s.top,
          left: s.left,
          width: s.size,
          height: s.size,
          borderRadius: '50%',
          background: i % 2 === 0 ? 'var(--gold)' : 'var(--purple-light)',
          animation: `starPulse ${s.dur} ${s.delay} ease-in-out infinite`,
        }} />
      ))}

      {/* Drifting void runes in background */}
      {VOID_RUNES.map((r, i) => (
        <div key={r} style={{
          position: 'absolute',
          top: `${10 + i * 11}%`,
          left: i % 2 === 0 ? `${3 + i * 4}%` : `${75 + (i % 4) * 5}%`,
          fontSize: 18,
          color: 'var(--purple-dim)',
          fontFamily: 'serif',
          animation: `drift ${2.5 + i * 0.4}s ${i * 0.3}s ease-in-out infinite`,
          userSelect: 'none',
          pointerEvents: 'none',
        }}>{r}</div>
      ))}

      {/* Void portal glow */}
      <div style={{
        width: 200,
        height: 200,
        borderRadius: '50%',
        background: 'radial-gradient(circle, #8b5cf615 0%, #8b5cf608 50%, transparent 70%)',
        border: '1px solid #8b5cf630',
        animation: 'voidGlow 4s ease-in-out infinite',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        marginBottom: 40,
      }}>
        {/* Owl in the void */}
        <div style={{
          fontSize: 72,
          animation: 'drift 4s ease-in-out infinite',
          filter: 'drop-shadow(0 0 12px #8b5cf6)',
        }}>
          🦉
        </div>
      </div>

      {/* Fog at feet of owl */}
      <div style={{
        position: 'absolute',
        top: '38%',
        left: '-10%',
        right: '-10%',
        height: 60,
        background: 'linear-gradient(to right, transparent, #8b5cf610 30%, #8b5cf618 50%, #8b5cf610 70%, transparent)',
        filter: 'blur(12px)',
        animation: 'fogDrift 8s ease-in-out infinite',
        pointerEvents: 'none',
      }} />

      {/* 404 */}
      <div style={{
        fontFamily: 'var(--font-cinzel)',
        fontSize: 'clamp(64px, 12vw, 100px)',
        fontWeight: 900,
        lineHeight: 1,
        background: 'linear-gradient(135deg, var(--gold) 0%, var(--purple-light) 100%)',
        WebkitBackgroundClip: 'text',
        WebkitTextFillColor: 'transparent',
        marginBottom: 16,
        letterSpacing: '0.05em',
      }}>
        404
      </div>

      <h1 style={{
        fontFamily: 'var(--font-cinzel)',
        fontSize: 'clamp(16px, 3vw, 22px)',
        color: 'var(--text)',
        marginBottom: 16,
        textAlign: 'center',
        letterSpacing: '0.08em',
      }}>
        The Scroll Was Not Found
      </h1>

      <p style={{
        color: 'var(--muted)',
        fontSize: 15,
        maxWidth: 420,
        textAlign: 'center',
        lineHeight: 1.8,
        marginBottom: 40,
        fontStyle: 'italic',
      }}>
        "This path leads only into the Void. Even the greatest Archmagi have
        wandered where no map exists — the mark of a true seeker."
      </p>

      {/* Decorative rune divider */}
      <div style={{
        display: 'flex',
        alignItems: 'center',
        gap: 12,
        marginBottom: 32,
        color: 'var(--border)',
        fontSize: 13,
        letterSpacing: '0.3em',
      }}>
        <span style={{ width: 40, height: 1, background: 'var(--border)' }} />
        ᚱ ᚢ ᚾ ᛖ ᛋ
        <span style={{ width: 40, height: 1, background: 'var(--border)' }} />
      </div>

      <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', justifyContent: 'center' }}>
        <button
          className="btn btn-primary"
          onClick={() => navigate('/')}
          style={{ padding: '12px 28px' }}
        >
          ✦ Return to the Academy
        </button>
        <button
          className="btn btn-ghost"
          onClick={() => navigate(-1)}
          style={{ padding: '12px 24px' }}
        >
          ← Go Back
        </button>
      </div>
    </div>
  )
}
