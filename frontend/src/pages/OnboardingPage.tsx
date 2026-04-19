import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { diagnosticApi } from '../api/services'

export default function OnboardingPage() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)

  async function handleNew() {
    setLoading(true)
    try { await diagnosticApi.skip(); navigate('/topic/java', { replace: true }) }
    catch { setLoading(false) }
  }

  function handleExperienced() { navigate('/diagnostic') }

  return (
    <div className="flex-1 flex items-center justify-center px-6 py-10">
      <div className="max-w-[640px] w-full text-center animate-[fade-up_0.5s_cubic-bezier(0.22,1,0.36,1)_both]">
        <div className="text-[56px] mb-4">🧙</div>
        <h1 className="text-[28px] font-bold text-gold m-0 mb-2.5">Welcome to Arcane Academy</h1>
        <p className="text-[15px] text-muted leading-[1.6] m-0 mb-8">
          Before we chart your path through the Java Grimoire, tell us where you stand.
        </p>
        <p className="text-[20px] font-bold text-text m-0 mb-6">Have you programmed in Java before?</p>

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
            <span className="text-[16px] font-bold text-teal">No, I'm completely new</span>
            <span className="text-[13px] text-muted leading-[1.5]">
              Start from the very beginning. We'll guide you step by step from zero to job-ready.
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
            <span className="text-[16px] font-bold text-purple-light">Yes, I have some experience</span>
            <span className="text-[13px] text-muted leading-[1.5]">
              Take a short diagnostic — we'll skip what you already know and focus on the gaps.
            </span>
          </button>
        </div>
      </div>
    </div>
  )
}
