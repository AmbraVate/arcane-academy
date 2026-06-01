import { useNavigate } from 'react-router-dom'
import { ArrowLeft, BookOpen, PlayCircle } from 'lucide-react'
import { usePreferences } from '@/hooks/usePreferences'
import { useTutorial } from '@/features/tutorial/context/TutorialContext'

export default function SettingsPage() {
  const navigate = useNavigate()
  const { loreEnabled, setLoreEnabled } = usePreferences()
  const { restart } = useTutorial()

  function handleRestartTutorial() {
    restart()
    // restart() navigates to / and activates the tutorial
  }

  return (
    <div className="max-w-[640px] mx-auto px-5 py-8 pb-[60px]">
      {/* Back */}
      <button className="btn btn-ghost text-[12px] flex items-center gap-1 mb-6" onClick={() => navigate(-1)}>
        <ArrowLeft size={13} strokeWidth={1.75} /> Back
      </button>

      <h1 className="font-cinzel text-[20px] text-gold tracking-wide mb-6">Settings &amp; Preferences</h1>

      {/* ── Lore toggle ─────────────────────────────────────────────────────── */}
      <section className="bg-card border border-border rounded-[12px] px-5 py-4 mb-4">
        <div className="flex items-start gap-3 mb-1">
          <BookOpen size={18} strokeWidth={1.75} className="text-purple-light flex-shrink-0 mt-0.5" />
          <div className="flex-1">
            <div className="font-cinzel text-[14px] text-gold tracking-wide">Lore Mode</div>
            <div className="text-[12px] text-muted mt-0.5 leading-[1.65]">
              When enabled, lessons open with a narrative <strong className="text-text">Lore Introduction</strong> and close with a
              thematic <strong className="text-text">Lore Conclusion</strong> that frames the concept within the Arcane Academy story.
              When disabled, these are replaced with plain analogies and standard summaries.
            </div>
          </div>
        </div>

        <div className="flex items-center justify-between mt-4">
          <span className="text-[13px] text-text">
            {loreEnabled ? '✦ Lore is enabled' : '— Lore is disabled'}
          </span>
          <button
            className={`relative w-[44px] h-[24px] rounded-full transition-colors duration-200 flex-shrink-0 focus:outline-none`}
            style={{ background: loreEnabled ? 'var(--purple)' : 'var(--border)' }}
            onClick={() => setLoreEnabled(!loreEnabled)}
            aria-label="Toggle lore mode"
          >
            <span
              className="absolute top-[3px] left-[3px] w-[18px] h-[18px] bg-white rounded-full shadow transition-transform duration-200"
              style={{ transform: loreEnabled ? 'translateX(20px)' : 'translateX(0)' }}
            />
          </button>
        </div>

        <p className="text-[11px] text-muted mt-3 italic">
          Changes apply to newly opened lessons.
        </p>
      </section>

      {/* ── Tutorial ────────────────────────────────────────────────────────── */}
      <section className="bg-card border border-border rounded-[12px] px-5 py-4 mb-4">
        <div className="flex items-start gap-3 mb-4">
          <PlayCircle size={18} strokeWidth={1.75} className="text-teal flex-shrink-0 mt-0.5" />
          <div className="flex-1">
            <div className="font-cinzel text-[14px] text-gold tracking-wide">App Tutorial</div>
            <div className="text-[12px] text-muted mt-0.5 leading-[1.65]">
              The tutorial walks you through selecting a School, browsing Disciplines, and completing your first demo lesson.
              It runs automatically on first login and can be replayed here any time.
            </div>
          </div>
        </div>

        <button className="btn btn-ghost text-[13px] flex items-center gap-1.5" onClick={handleRestartTutorial}>
          <PlayCircle size={14} strokeWidth={1.75} /> Restart Tutorial
        </button>
      </section>
    </div>
  )
}
