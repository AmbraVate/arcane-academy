import { useState, type FormEvent } from 'react'
import { Link, useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Wand2 } from 'lucide-react'

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const blockedReason = searchParams.get('reason') === 'blocked'
  const [error, setError] = useState(blockedReason ? 'Your account has been blocked. Please contact support.' : '')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login(email, password)
      navigate('/topics')
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })
        ?.response?.data?.message ?? 'Invalid credentials.'
      setError(msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex-1 flex items-center justify-center p-6 overflow-y-auto">
      <div className="bg-card border border-border rounded-[14px] p-9 px-8 w-full max-w-[400px] text-center">
        <div className="flex items-center justify-center mb-3">
          <Wand2 size={48} className="text-gold" strokeWidth={1.5} />
        </div>
        <h1 className="font-cinzel text-[22px] text-gold tracking-[1px] mb-2">Arcane Academy</h1>
        <p className="text-[15px] text-muted italic mb-7">Enter your credentials to continue your journey</p>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4 text-left">
          <div className="flex flex-col gap-1.5">
            <label className="font-cinzel text-[11px] tracking-[1px] text-muted">Email</label>
            <Input type="email" value={email} onChange={e => setEmail(e.target.value)}
              placeholder="wizard@academy.com" required />
          </div>
          <div className="flex flex-col gap-1.5">
            <label className="font-cinzel text-[11px] tracking-[1px] text-muted">Password</label>
            <Input type="password" value={password} onChange={e => setPassword(e.target.value)}
              placeholder="••••••••" required />
          </div>
          {error && (
            <div className="bg-[#2d0808] border border-red rounded-[6px] px-[13px] py-[9px] text-[13px] text-red text-center">
              {error}
            </div>
          )}
          <Button variant="primary" type="submit" disabled={loading} className="w-full py-[10px]">
            {loading ? 'Entering...' : '✦ Enter the Academy'}
          </Button>
        </form>

        <div className="flex items-center gap-3 my-6 text-muted text-[13px] italic
          before:flex-1 before:h-px before:bg-border
          after:flex-1 after:h-px after:bg-border">
          <span>or</span>
        </div>

        <a
          href="/oauth2/authorization/google"
          className="flex items-center justify-center gap-2.5 w-full py-[10px] border border-border rounded-[8px]
            bg-surface text-text font-cinzel text-[13px] cursor-pointer transition-[border-color,background] duration-200
            hover:border-purple hover:bg-purple-dim no-underline"
        >
          <svg width="18" height="18" viewBox="0 0 48 48"><path fill="#EA4335" d="M24 9.5c3.54 0 6.71 1.22 9.21 3.6l6.85-6.85C35.9 2.38 30.47 0 24 0 14.62 0 6.51 5.38 2.56 13.22l7.98 6.19C12.43 13.72 17.74 9.5 24 9.5z"/><path fill="#4285F4" d="M46.98 24.55c0-1.57-.15-3.09-.38-4.55H24v9.02h12.94c-.58 2.96-2.26 5.48-4.78 7.18l7.73 6c4.51-4.18 7.09-10.36 7.09-17.65z"/><path fill="#FBBC05" d="M10.53 28.59a14.5 14.5 0 010-9.18l-7.98-6.19a24.1 24.1 0 000 21.56l7.98-6.19z"/><path fill="#34A853" d="M24 48c6.48 0 11.93-2.13 15.89-5.81l-7.73-6c-2.15 1.45-4.92 2.3-8.16 2.3-6.26 0-11.57-4.22-13.47-9.91l-7.98 6.19C6.51 42.62 14.62 48 24 48z"/></svg>
          Continue with Google
        </a>

        <p className="mt-5 text-[14px] text-muted">
          New apprentice? <Link to="/register" className="text-purple-light hover:underline">Register here</Link>
        </p>
      </div>
    </div>
  )
}
