import { useState } from 'react'
import { createPortal } from 'react-dom'
import { Sparkles, ZoomIn, X } from 'lucide-react'

interface Props {
  lessonId: string
  title: string
}

/**
 * Maps a lesson ID prefix to the public infographics folder for that domain.
 * Returns null for domains that don't have an infographics folder yet.
 */
function resolveInfographicUrl(lessonId: string): string | null {
  if (lessonId.startsWith('se-')) return `/infographics/software%20engineering/${lessonId}.jpeg`
  if (lessonId.startsWith('fe-')) return `/infographics/frontend%20engineering/${lessonId}.jpeg`
  if (lessonId.startsWith('de-')) return `/infographics/data%20engineering/${lessonId}.jpeg`
  return null
}

/**
 * Renders a visual summary infographic for the current lesson if one exists.
 * Uses onError to silently hide itself when no image is found — no 404 noise.
 * Clicking the image opens a full-screen lightbox for easy reading on mobile.
 */
export default function InfographicPanel({ lessonId, title }: Props) {
  const [exists, setExists] = useState(true)
  const [lightboxOpen, setLightboxOpen] = useState(false)

  const url = resolveInfographicUrl(lessonId)
  if (!url || !exists) return null

  return (
    <>
      <div className="my-6 rounded-[12px] border border-[rgba(45,212,191,0.2)] bg-[rgba(45,212,191,0.04)] overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between px-4 py-2.5 border-b border-[rgba(45,212,191,0.15)]">
          <div className="flex items-center gap-1.5">
            <Sparkles size={12} strokeWidth={2} className="text-teal" />
            <span className="text-[11px] font-bold text-teal uppercase tracking-[0.08em]">Visual Summary</span>
          </div>
          <span className="text-[10px] text-muted">Tap to expand</span>
        </div>

        {/* Image — click opens lightbox */}
        <button
          type="button"
          aria-label={`Expand ${title} infographic`}
          className="relative w-full block group cursor-zoom-in focus:outline-none focus-visible:ring-2 focus-visible:ring-teal"
          onClick={() => setLightboxOpen(true)}
        >
          <img
            src={url}
            alt={`${title} — visual summary`}
            onError={() => setExists(false)}
            className="w-full h-auto block"
            loading="lazy"
          />
          {/* Hover overlay — desktop hint */}
          <div className="absolute inset-0 bg-black/0 group-hover:bg-black/25 transition-colors duration-150 flex items-center justify-center">
            <div className="opacity-0 group-hover:opacity-100 transition-opacity duration-150 bg-black/60 rounded-full p-2.5">
              <ZoomIn size={22} color="white" strokeWidth={1.75} />
            </div>
          </div>
        </button>
      </div>

      {/* Lightbox portal */}
      {lightboxOpen && createPortal(
        <div
          role="dialog"
          aria-modal="true"
          aria-label={`${title} infographic — full size`}
          className="fixed inset-0 z-[200] bg-black/92 flex flex-col items-center justify-center p-4 max-[480px]:p-2"
          onClick={() => setLightboxOpen(false)}
        >
          {/* Close button */}
          <button
            type="button"
            aria-label="Close infographic"
            className="absolute top-4 right-4 bg-white/10 hover:bg-white/25 active:bg-white/30 rounded-full p-2.5 transition-colors z-10"
            onClick={() => setLightboxOpen(false)}
          >
            <X size={20} color="white" strokeWidth={1.75} />
          </button>

          {/* Caption */}
          <p className="text-white/50 text-[11px] uppercase tracking-[0.1em] mb-3 font-semibold">
            Visual Summary — {title}
          </p>

          {/* Full-size image — stops click-through to backdrop */}
          <img
            src={url}
            alt={`${title} — visual summary`}
            className="max-w-full max-h-[85vh] w-auto h-auto rounded-[8px] object-contain shadow-[0_8px_60px_rgba(0,0,0,0.7)] max-[480px]:rounded-[4px]"
            onClick={e => e.stopPropagation()}
          />

          <p className="text-white/30 text-[11px] mt-3">Tap outside to close</p>
        </div>,
        document.body,
      )}
    </>
  )
}
