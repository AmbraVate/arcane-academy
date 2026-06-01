// ── Tutorial step definitions ──────────────────────────────────────────────────
//
// Each step spotlights a DOM element (via `data-tutorial-id`) and shows a
// callout box with a title + body. When `navigateTo` is set the overlay
// navigates there before showing the callout.
// ──────────────────────────────────────────────────────────────────────────────

export type CalloutPosition = 'top' | 'bottom' | 'left' | 'right' | 'center'

export interface TutorialStep {
  id: string
  /** data-tutorial-id of the DOM element to spotlight. Omit for centered modal steps. */
  targetId?: string
  title: string
  body: string
  /** Where to render the callout relative to the spotlight. Default: 'bottom'. */
  position?: CalloutPosition
  /** Navigate to this path before activating the step. */
  navigateTo?: string
  ctaLabel?: string
}

export const TUTORIAL_STEPS: TutorialStep[] = [
  {
    id: 'welcome',
    title: 'Welcome to Arcane Academy! 🧙',
    body: "I'll give you a quick tour of how the app works. This takes about 2 minutes and can be replayed any time from Settings.\n\nPress **Next** to begin, or **Skip** to jump straight in.",
    position: 'center',
    ctaLabel: 'Begin Tour →',
  },
  {
    id: 'nav-schools',
    targetId: 'nav-domains',
    title: 'Schools of Learning',
    body: 'The **Schools** section is your starting point. This is where you choose what you want to learn.',
    position: 'bottom',
    navigateTo: '/',
    ctaLabel: 'Next →',
  },
  {
    id: 'schools-grid',
    targetId: 'schools-grid',
    title: 'Choose Your School',
    body: 'Each **School** groups related disciplines. Browse the schools and pick one that calls to you — you can always explore others later.',
    position: 'bottom',
    navigateTo: '/domains',
    ctaLabel: 'Next →',
  },
  {
    id: 'domain-card',
    targetId: 'first-domain-card',
    title: 'Your Disciplines',
    body: 'Within each School you\'ll find **Disciplines** — structured learning paths broken into modules. Enrol in one to get a personalised starting point.',
    position: 'bottom',
    ctaLabel: 'Next →',
  },
  {
    id: 'nav-review',
    targetId: 'nav-review',
    title: 'Daily Reviews',
    body: 'As you learn, lessons are scheduled for **spaced-repetition review**. The badge shows how many are due today. Short daily reviews are how long-term memory is built.',
    position: 'bottom',
    navigateTo: '/domains',
    ctaLabel: 'Next →',
  },
  {
    id: 'pre-lesson',
    title: 'What a Lesson Looks Like',
    body: "Every lesson follows the same structure:\n\n**Hook** → spark curiosity\n**Explanation** → learn the concept\n**Guided Practice** → apply it with help\n**Solo Practice** → recall from memory\n**Retrieval Check** → answer questions\n\nLet's walk through a demo lesson now!",
    position: 'center',
    ctaLabel: 'Try Demo Lesson →',
    navigateTo: '/tutorial/lesson',
  },
]
