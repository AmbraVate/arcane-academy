import { useState, type FormEvent } from 'react'
import { Link, useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Wand2, UserX, KeyRound } from 'lucide-react'

type LoginError =
  | { kind: 'no_account'; email: string }
  | { kind: 'wrong_password' }
  | { kind: 'oauth_account'; message: string }
  | { kind: 'blocked' }
  | { kind: 'generic'; message: string }
  | null

function parseError(err: unknown, submittedEmail: string): LoginError {
  const resp = (err as { response?: { data?: { code?: string; message?: string }; status?: number } })?.response
  const code = resp?.data?.code
  const status = resp?.status

  if (status === 404 || code === 'USER_NOT_FOUND') return { kind: 'no_account', email: submittedEmail }
  if (status === 403) return { kind: 'blocked' }

  const msg = resp?.data?.message ?? ''
  if (msg.toLowerCase().includes('google sign-in')) return { kind: 'oauth_account', message: msg }
  if (status === 401) return { kind: 'wrong_password' }

  return { kind: 'generic', message: msg || 'Something went wrong. Please try again.' }
}

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const blockedReason = searchParams.get('reason') === 'blocked'
  const [loginError, setLoginError] = useState<LoginError>(
    blockedReason ? { kind: 'blocked' } : null
  )
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setLoginError(null)
    setLoading(true)
    try {
      await login(email, password)
      navigate('/')
    } catch (err: unknown) {
      setLoginError(parseError(err, email))
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

          {loginError && <LoginErrorBanner error={loginError} />}

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

function LoginErrorBanner({ error }: { error: NonNullable<LoginError> }) {
  if (error.kind === 'no_account') {
    return (
      <div className="bg-[#1a1208] border border-[rgba(234,179,8,0.4)] rounded-[8px] px-4 py-3.5 text-left">
        <div className="flex items-center gap-2 mb-1.5">
          <UserX size={14} className="text-gold flex-shrink-0" />
          <span className="text-[13px] font-semibold text-gold">No account found</span>
        </div>
        <p className="text-[13px] text-muted leading-[1.5] mb-2.5">
          There's no wizard registered with <span className="text-text font-mono text-[12px]">{error.email}</span>.
        </p>
        <Link
          to={`/register`}
          className="inline-flex items-center gap-1.5 text-[13px] text-purple-light hover:underline font-medium"
        >
          ✦ Create an account instead
        </Link>
      </div>
    )
  }

  if (error.kind === 'wrong_password') {
    return (
      <div className="bg-[#2d0808] border border-[rgba(248,113,113,0.4)] rounded-[8px] px-4 py-3.5 text-left">
        <div className="flex items-center gap-2 mb-1.5">
          <KeyRound size={14} className="text-red flex-shrink-0" />
          <span className="text-[13px] font-semibold text-red">Incorrect password</span>
        </div>
        <p className="text-[13px] text-muted leading-[1.5] mb-2.5">
          The password you entered doesn't match this account.
        </p>
        <Link
          to="/forgot-password"
          className="inline-flex items-center gap-1.5 text-[13px] text-purple-light hover:underline font-medium"
        >
          🔑 Forgot your password?
        </Link>
      </div>
    )
  }

  if (error.kind === 'blocked') {
    return (
      <div className="bg-[#2d0808] border border-red rounded-[6px] px-[13px] py-[9px] text-[13px] text-red text-center">
        Your account has been blocked. Please contact support.
      </div>
    )
  }

  if (error.kind === 'oauth_account') {
    return (
      <div className="bg-[#1a1208] border border-[rgba(234,179,8,0.4)] rounded-[6px] px-[13px] py-[9px] text-[13px] text-gold text-center">
        {error.message}
      </div>
    )
  }

  return (
    <div className="bg-[#2d0808] border border-red rounded-[6px] px-[13px] py-[9px] text-[13px] text-red text-center">
      {error.message}
    </div>
  )
}
