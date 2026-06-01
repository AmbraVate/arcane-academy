import { useNavigate } from 'react-router-dom'
import { LogIn, Sparkles } from 'lucide-react'

/**
 * Shown at the top of publicly-browsable pages (Domain, Module, Topic)
 * when no user is authenticated.
 */
export default function PublicBanner({ message }: { message?: string }) {
  const navigate = useNavigate()

  return (
    <div
      className="flex items-center gap-3 px-5 py-3 mb-6 rounded-[12px] flex-wrap"
      style={{
        background: 'linear-gradient(135deg, rgba(139,92,246,0.10) 0%, rgba(45,212,191,0.08) 100%)',
        border: '1px solid rgba(139,92,246,0.25)',
      }}
    >
      <Sparkles size={16} color="var(--purple-light)" strokeWidth={1.75} className="flex-shrink-0" />
      <p className="text-[13px] text-muted leading-[1.5] flex-1 m-0">
        {message ?? 'You\'re browsing the course structure. Sign up free to start learning and track your progress.'}
      </p>
      <div className="flex gap-2 flex-shrink-0">
        <button
          onClick={() => navigate('/login')}
          className="btn btn-ghost text-[12px] px-3 py-1.5 flex items-center gap-1.5"
        >
          <LogIn size={13} strokeWidth={1.75} />
          Sign In
        </button>
        <button
          onClick={() => navigate('/register')}
          className="btn text-[12px] px-3 py-1.5 font-cinzel font-semibold rounded-[8px]"
          style={{ background: 'var(--purple)', color: '#fff', border: '1px solid var(--purple)' }}
        >
          Join Free →
        </button>
      </div>
    </div>
  )
}
