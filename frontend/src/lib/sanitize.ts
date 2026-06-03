import DOMPurify from 'dompurify'

/**
 * Sanitises an HTML string before it is passed to `dangerouslySetInnerHTML`.
 * Strips all scripts, event handlers, and dangerous attributes while keeping
 * safe formatting tags (headings, bold, code, links, etc.).
 *
 * Use everywhere lesson/story/question HTML from the backend is rendered.
 */
export function sanitizeHtml(html: string | null | undefined): string {
  if (!html) return ''
  return DOMPurify.sanitize(html, {
    USE_PROFILES: { html: true },
    // Extra safety: strip data URIs and javascript: hrefs
    FORBID_ATTR: ['style'],
    ALLOW_DATA_ATTR: false,
  })
}

/** Convenience wrapper for use directly in JSX dangerouslySetInnerHTML */
export function safe(html: string | null | undefined): { __html: string } {
  return { __html: sanitizeHtml(html) }
}
