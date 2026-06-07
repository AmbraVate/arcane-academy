import { useState, type FormEvent } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import api from '@/shared/api/client'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { PasswordStrength } from '@/components/ui/PasswordStrength'
import { KeyRound, CheckCircle, AlertTriangle } from 'lucide-react'

type PageState = 'idle' | 'submitting' | 'success' | 'expired' | 'error'

export default function ResetPasswordPage() {
  const navigate = useNavigate()
  // Token is passed in the URL hash (#token=...) so it is never sent to servers
  // in Referer headers or recorded in access logs
  const token = new URLSearchParams(window.location.hash.slice(1)).get('token') ?? ''

  const [newPassword,     setNewPassword]     = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [pageState,       setPageState]       = useState<PageState>('idle')
  const [errorMsg,        setErrorMsg]        = useState('')

  const passwordsMatch = newPassword === confirmPassword
  const valid          = newPassword.length >= 20 && passwordsMatch

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    if (!valid || !token) return

    setPageState('submitting')
    try {
      await api.post('/api/auth/reset-password', { token, newPassword })
      setPageState('success')
    } catch (err: unknown) {
      const status = (err as { response?: { status?: number } })?.response?.status
      if (status === 410) {
        setPageState('expired')
      } else {
        setErrorMsg('Something went wrong. Please try again or request a new reset link.')
        setPageState('error')
      }
    }
  }

  /* ── No token in URL ──────────────────────────────────────────────────── */
  if (!token) {
    return (
      <AuthShell>
        <div className="text-center">
          <AlertTriangle size={40} className="mx-auto mb-4 text-gold opacity-70"/>
          <h1 className="font-cinzel text-[20px] font-bold text-text mb-2">Invalid Link</h1>
          <p className="text-[13px] text-muted mb-6">
            This reset link is missing its security token. Please use the exact link from your email.
          </p>
          <Link to="/login" className="font-cinzel text-[12px] text-teal hover:opacity-80">
            Back to sign in
          </Link>
        </div>
      </AuthShell>
    )
  }

  /* ── Success ───────────────────────────────────────────────────────────── */
  if (pageState === 'success') {
    return (
      <AuthShell>
        <div className="text-center">
          <CheckCircle size={44} className="mx-auto mb-4" style={{color: 'var(--teal)'}}/>
          <h1 className="font-cinzel text-[22px] font-bold text-text mb-2">Password Updated</h1>
          <p className="text-[13px] text-muted mb-6">
            Your password has been reset. You can now sign in with your new credentials.
          </p>
          <Button onClick={() => navigate('/login')} className="w-full">
            Sign in
          </Button>
        </div>
      </AuthShell>
    )
  }

  /* ── Expired ───────────────────────────────────────────────────────────── */
  if (pageState === 'expired') {
    return (
      <AuthShell>
        <div className="text-center">
          <AlertTriangle size={40} className="mx-auto mb-4 text-gold opacity-80"/>
          <h1 className="font-cinzel text-[20px] font-bold text-text mb-2">Link Expired</h1>
          <p className="text-[13px] text-muted mb-6">
            This reset link has expired (links are valid for 24 hours). Please ask your admin to send a new one.
          </p>
          <Link to="/login" className="font-cinzel text-[12px] text-teal hover:opacity-80">
            Back to sign in
          </Link>
        </div>
      </AuthShell>
    )
  }

  /* ── Form ──────────────────────────────────────────────────────────────── */
  return (
    <AuthShell>
      <div className="flex items-center gap-3 mb-6">
        <div className="w-10 h-10 rounded-[10px] flex items-center justify-center bg-purple-dim border border-purple">
          <KeyRound size={18} strokeWidth={1.75} style={{color: 'var(--purple-light)'}}/>
        </div>
        <div>
          <h1 className="font-cinzel text-[20px] font-bold text-text m-0 leading-tight">Set New Password</h1>
          <p className="text-[12px] text-muted m-0 mt-0.5">Choose a strong password for your account</p>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        <div>
          <label className="block font-cinzel text-[10px] tracking-[0.15em] text-muted mb-1.5">
            NEW PASSWORD
          </label>
          <Input
            type="password"
            value={newPassword}
            onChange={e => setNewPassword(e.target.value)}
            placeholder="e.g. purple wizard drinks coffee"
            autoFocus
            autoComplete="new-password"
          />
          <PasswordStrength password={newPassword} />
          {newPassword.length === 0 && (
            <p className="text-[11px] text-muted mt-0.5">A passphrase of 3+ words is easy to remember and very hard to crack.</p>
          )}
        </div>

        <div>
          <label className="block font-cinzel text-[10px] tracking-[0.15em] text-muted mb-1.5">
            CONFIRM PASSWORD
          </label>
          <Input
            type="password"
            value={confirmPassword}
            onChange={e => setConfirmPassword(e.target.value)}
            placeholder="Repeat your new password"
            autoComplete="new-password"
          />
          {confirmPassword.length > 0 && !passwordsMatch && (
            <p className="text-[11px] text-[#f87171] mt-1">Passwords do not match</p>
          )}
        </div>

        {(pageState === 'error') && (
          <div className="text-[12px] text-[#f87171] bg-[rgba(248,113,113,.08)] border border-[rgba(248,113,113,.25)] rounded-[8px] px-3 py-2">
            {errorMsg}
          </div>
        )}

        <Button
          type="submit"
          disabled={!valid || pageState === 'submitting'}
          className="w-full mt-1"
        >
          {pageState === 'submitting' ? 'Updating...' : 'Set Password'}
        </Button>
      </form>

      <p className="text-center mt-5 text-[12px] text-muted">
        <Link to="/login" className="text-teal hover:opacity-80 font-cinzel">
          Back to sign in
        </Link>
      </p>
    </AuthShell>
  )
}

/* ── Layout shell ────────────────────────────────────────────────────────── */

function AuthShell({children}: {children: React.ReactNode}) {
  return (
    <div className="min-h-screen flex items-center justify-center px-4"
      style={{background: 'var(--bg)'}}>
      <div className="w-full max-w-[400px]">
        {/* Academy wordmark */}
        <div className="text-center mb-8">
          <span
            className="font-cinzel text-[13px] tracking-[0.3em] font-bold"
            style={{
              background: 'linear-gradient(135deg, var(--gold), var(--purple-light))',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
            }}
          >
            THE ARCANE ACADEMY
          </span>
        </div>

        <div className="rounded-[16px] border border-border bg-card px-8 py-8">
          {children}
        </div>
      </div>
    </div>
  )
}
