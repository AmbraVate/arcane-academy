import { useState, useEffect, useRef } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { stuckReportApi } from '@/shared/api/services'
import { cn } from '@/lib/utils'

type Stage = 'idle' | 'capturing' | 'open' | 'submitting' | 'done'

/**
 * Floating "I'm Stuck" help button — shown globally for logged-in learners.
 * Captures a viewport screenshot (html2canvas), the current URL, and context
 * from URL params, plus an optional free-text description, then sends the
 * report and returns the learner to /topics.
 */
export default function StuckButton() {
  const navigate = useNavigate()
  const location = useLocation()
  const [stage, setStage] = useState<Stage>('idle')
  const [message, setMessage] = useState('')
  const [screenshotData, setScreenshotData] = useState<string | undefined>(undefined)
  const textareaRef = useRef<HTMLTextAreaElement>(null)

  // Parse context clues from the current URL
  function parseContext() {
    const url = window.location.href
    const match = location.pathname.match(/\/topic\/([^/]+)(?:\/.*\/([^/]+))?/)
    return {
      topicId: match?.[1] ?? undefined,
      subChunkId: undefined as string | undefined,
      currentPhase: undefined as string | undefined,
      currentUrl: url,
    }
  }

  /** Capture a screenshot of the current viewport via html2canvas (lazy-loaded). */
  async function captureScreenshot(): Promise<string | undefined> {
    try {
      const html2canvas = (await import('html2canvas')).default
      const canvas = await html2canvas(document.body, {
        useCORS: true,
        scale: 0.6,        // downsample to keep payload small
        logging: false,
        allowTaint: true,
      })
      return canvas.toDataURL('image/jpeg', 0.7)
    } catch {
      return undefined    // non-fatal — report still submits without screenshot
    }
  }

  // Focus textarea when dialog opens
  useEffect(() => {
    if (stage === 'open') textareaRef.current?.focus()
  }, [stage])

  // Close on Escape
  useEffect(() => {
    if (stage !== 'open') return
    const handler = (e: KeyboardEvent) => { if (e.key === 'Escape') setStage('idle') }
    document.addEventListener('keydown', handler)
    return () => document.removeEventListener('keydown', handler)
  }, [stage])

  async function handleOpen() {
    setStage('capturing')
    const screenshot = await captureScreenshot()
    setScreenshotData(screenshot)
    setStage('open')
  }

  async function handleSubmit() {
    setStage('submitting')
    try {
      const ctx = parseContext()
      await stuckReportApi.submit({
        ...ctx,
        userMessage: message.trim() || undefined,
        screenshotData,
      })
      setStage('done')
      setTimeout(() => navigate('/topics', { replace: true }), 2000)
    } catch {
      // Even if the request fails, don't strand the user — send them home
      setStage('done')
      setTimeout(() => navigate('/topics', { replace: true }), 2000)
    }
  }

  return (
    <>
      {/* ── Floating trigger button ── */}
      <button
        onClick={handleOpen}
        className={cn(
          'fixed bottom-6 right-5 z-[300]',
          'flex items-center gap-2 px-3.5 py-2 rounded-full',
          'bg-[#16132b] border border-[rgba(248,113,113,0.35)]',
          'text-[11px] font-cinzel tracking-wide text-[#fca5a5]',
          'shadow-[0_4px_20px_rgba(0,0,0,0.4)]',
          'transition-[border-color,box-shadow,transform] duration-200',
          'hover:border-[rgba(248,113,113,0.7)] hover:shadow-[0_4px_24px_rgba(248,113,113,0.15)] hover:-translate-y-[2px]',
          stage === 'capturing' && 'opacity-60 cursor-wait',
          (stage === 'open' || stage === 'submitting' || stage === 'done') && 'invisible pointer-events-none',
        )}
        disabled={stage === 'capturing'}
        aria-label="I'm stuck — get help"
      >
        <span className="text-[14px]">🚩</span>
        {stage === 'capturing' ? 'Capturing…' : "I'm Stuck"}
      </button>

      {/* ── Backdrop ── */}
      {(stage === 'open' || stage === 'submitting' || stage === 'done') && (
        <div
          className="fixed inset-0 z-[350] bg-black/60 backdrop-blur-sm"
          onClick={() => stage === 'open' && setStage('idle')}
        />
      )}

      {/* ── Dialog ── */}
      {(stage === 'open' || stage === 'submitting' || stage === 'done') && (
        <div
          className={cn(
            'fixed z-[360] inset-0 flex items-center justify-center px-4 pointer-events-none',
          )}
        >
          <div
            className={cn(
              'w-full max-w-[460px] bg-[#16132b] border border-[rgba(248,113,113,0.35)]',
              'rounded-[16px] shadow-[0_12px_40px_rgba(0,0,0,0.6)] p-6 pointer-events-auto',
              'animate-[fade-up_0.25s_cubic-bezier(0.22,1,0.36,1)_both]',
            )}
            onClick={e => e.stopPropagation()}
          >
            {stage === 'done' ? (
              /* ── Success state ── */
              <div className="text-center py-4">
                <div className="text-[44px] mb-3">✅</div>
                <h2 className="font-cinzel text-[18px] text-teal m-0 mb-2">Report Sent</h2>
                <p className="text-[13px] text-muted leading-[1.6] m-0">
                  We've noted the issue. Returning you to the topics page now…
                </p>
              </div>
            ) : (
              /* ── Input state ── */
              <>
                <div className="flex items-start gap-3 mb-4">
                  <span className="text-[28px] flex-shrink-0">🚩</span>
                  <div>
                    <h2 className="font-cinzel text-[17px] text-[#fca5a5] m-0 mb-1">Hit a wall?</h2>
                    <p className="text-[12px] text-muted leading-[1.55] m-0">
                      Tell us what's happening and we'll take a look. You'll be taken back to the topics page.
                    </p>
                  </div>
                </div>

                <textarea
                  ref={textareaRef}
                  value={message}
                  onChange={e => setMessage(e.target.value)}
                  placeholder="Optional: describe the problem (e.g. 'the code runner never responds', 'stuck on a loading screen')"
                  rows={4}
                  className={cn(
                    'w-full resize-none rounded-[10px] px-3.5 py-3',
                    'bg-[#0e0c1e] border border-[rgba(248,113,113,0.25)]',
                    'text-[13px] text-text leading-[1.6] placeholder:text-muted',
                    'focus:outline-none focus:border-[rgba(248,113,113,0.55)]',
                    'transition-[border-color] duration-150',
                  )}
                  disabled={stage === 'submitting'}
                />

                <div className="text-[11px] text-muted mt-2 mb-4 leading-[1.5]">
                  We'll also capture your current page and account details automatically.
                </div>

                <div className="flex gap-3">
                  <button
                    onClick={() => setStage('idle')}
                    disabled={stage === 'submitting'}
                    className={cn(
                      'flex-1 py-2.5 rounded-[9px] border border-border',
                      'text-[12px] font-cinzel text-muted',
                      'hover:border-[rgba(255,255,255,0.2)] hover:text-text',
                      'transition-[border-color,color] duration-150',
                      'disabled:opacity-50 disabled:cursor-not-allowed',
                    )}
                  >
                    Cancel
                  </button>
                  <button
                    onClick={handleSubmit}
                    disabled={stage === 'submitting'}
                    className={cn(
                      'flex-1 py-2.5 rounded-[9px]',
                      'bg-[rgba(248,113,113,0.12)] border border-[rgba(248,113,113,0.4)]',
                      'text-[12px] font-cinzel text-[#fca5a5]',
                      'hover:bg-[rgba(248,113,113,0.2)] hover:border-[rgba(248,113,113,0.65)]',
                      'transition-[background,border-color] duration-150',
                      'disabled:opacity-50 disabled:cursor-not-allowed',
                    )}
                  >
                    {stage === 'submitting' ? 'Sending…' : '🚩 Send Report'}
                  </button>
                </div>
              </>
            )}
          </div>
        </div>
      )}
    </>
  )
}
