import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { diagnosticApi } from '@/shared/api/services'

export default function OnboardingPage() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)

  async function handleNew() {
    setLoading(true)
    try { await diagnosticApi.skip(); navigate('/topics', { replace: true }) }
    catch { setLoading(false) }
  }

  function handleExperienced() { navigate('/topics', { replace: true }) }

  return (
    <div className="flex-1 flex items-center justify-center px-6 py-10">
      <div className="max-w-[640px] w-full text-center animate-[fade-up_0.5s_cubic-bezier(0.22,1,0.36,1)_both]">
        <div className="text-[56px] mb-4">🧙</div>
        <h1 className="text-[28px] font-bold text-gold m-0 mb-2.5">Welcome to Arcane Academy</h1>
        <p className="text-[15px] text-muted leading-[1.6] m-0 mb-8">
          A polymath doesn't specialise in one thing — they build deep mastery across many disciplines.
          Choose your first path and we'll guide you from there.
        </p>
        <p className="text-[20px] font-bold text-text m-0 mb-6">Have you studied here before?</p>

        <div className="flex gap-4 max-[520px]:flex-col">
          <button
            className="flex-1 flex flex-col items-center gap-2.5 px-5 py-7 bg-card border border-[rgba(0,200,150,0.3)]
              rounded-[16px] cursor-pointer shadow-[0_0_30px_rgba(0,200,150,0.06)] text-center
              transition-[transform,border-color,box-shadow] duration-150
              hover:enabled:-translate-y-[3px] hover:enabled:border-teal hover:enabled:shadow-[0_6px_30px_rgba(0,200,150,0.15)]
              disabled:opacity-60 disabled:cursor-not-allowed"
            onClick={handleNew}
            disabled={loading}
          >
            <span className="text-[36px]">🌱</span>
            <span className="text-[16px] font-bold text-teal">No, I'm brand new</span>
            <span className="text-[13px] text-muted leading-[1.5]">
              Choose a discipline and start from the very beginning — we'll guide you step by step.
            </span>
          </button>

          <button
            className="flex-1 flex flex-col items-center gap-2.5 px-5 py-7 bg-card border border-[rgba(139,92,246,0.3)]
              rounded-[16px] cursor-pointer shadow-[0_0_30px_rgba(139,92,246,0.06)] text-center
              transition-[transform,border-color,box-shadow] duration-150
              hover:enabled:-translate-y-[3px] hover:enabled:border-purple hover:enabled:shadow-[0_6px_30px_rgba(139,92,246,0.15)]
              disabled:opacity-60 disabled:cursor-not-allowed"
            onClick={handleExperienced}
            disabled={loading}
          >
            <span className="text-[36px]">⚡</span>
            <span className="text-[16px] font-bold text-purple-light">I have prior knowledge</span>
            <span className="text-[13px] text-muted leading-[1.5]">
              Pick a topic and take its diagnostic — we'll place you at the right level automatically.
            </span>
          </button>
        </div>
      </div>
    </div>
  )
}
