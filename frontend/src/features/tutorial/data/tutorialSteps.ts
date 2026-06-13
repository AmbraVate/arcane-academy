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
    title: 'Welcome to Arcane Academy! 🏰',
    body: "A degree-level, self-paced learning platform for the polymath. Master Software Engineering, Psychology, Natural Sciences and more — guided by memory science.\n\nThis short tour shows you how the Academy works and ends with a taste of a real lesson.",
    position: 'center',
    ctaLabel: 'Begin Tour →',
  },
  {
    id: 'how-organised',
    title: 'The Structure of Knowledge',
    body: "The Academy is organised into layers:\n\n**School** — groups related disciplines\n**Pathway** — e.g. Software Engineering\n**Tier** — Apprentice → Junior → Senior → Lead\n**Module** — a cluster of related lessons\n**Lesson** — a 15–25 min deep-learning session\n\nYou choose a School, pick a Pathway, and work through it tier by tier.",
    position: 'center',
    ctaLabel: 'Next →',
  },
  {
    id: 'lesson-structure',
    title: 'How Every Lesson Works',
    body: "Every lesson follows the same science-backed 7-step structure:\n\n**Hook and Objectives** — spark curiosity with a story\n**Learning Content** — deep explanation with examples\n**Guided Practice** — apply it with scaffolding\n**Solo Practice** — recall from scratch\n**Knowledge Check** — test your understanding\n**Teach Back** — explain it in your own words\n**Common Mistakes** — learn what trips people up\n\nEarn XP and rank up from Novice to Lord Magus.",
    position: 'center',
    ctaLabel: 'Next →',
  },
  {
    id: 'why-it-works',
    title: 'Why This Works: The Forgetting Curve',
    body: "We lose roughly **70% of new knowledge within 24 hours** without reinforcement — Hermann Ebbinghaus proved this in the 1880s and modern neuroscience confirms it.\n\nArcane Academy's daily review system catches concepts **just before they fade**, reactivating the memory trace at the optimal moment. Studies show **90%+ long-term retention** with spaced practice vs 40–50% with traditional study.\n\nEvery review you complete is a deliberate strike against forgetting.",
    position: 'center',
    ctaLabel: 'Next →',
  },
  {
    id: 'ai-mentor',
    title: 'Meet Your AI Mentor: Archmage Veylan',
    body: "Archmage Veylan accompanies you through every lesson — answering questions, posing Socratic challenges, and evaluating your code.\n\nHe won't just hand you the answer. Ask him anything and he'll guide you to find it yourself — the same way the world's best tutors do.\n\nThe more you engage with him, the deeper the learning sticks.",
    position: 'center',
    ctaLabel: 'Next →',
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
    position: 'top',
    navigateTo: '/schools',
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
    navigateTo: '/schools',
    ctaLabel: 'Next →',
  },
  {
    id: 'capstone',
    title: 'Real Projects. Real Portfolio.',
    body: "Every tier ends with a **capstone project**. These aren't busywork — they're portfolio pieces you can share with the world.\n\nBy the end of each pathway you'll have a body of work that demonstrates what you actually know — not just what you've memorised.\n\nBuilt in public. Owned by you.",
    position: 'center',
    ctaLabel: 'Next →',
  },
  {
    id: 'pre-lesson',
    title: "Ready? Let's Try a Lesson",
    body: "You've seen the structure, the science, and your mentor. Now experience it firsthand.\n\nThe demo lesson gives you a taste of all 7 steps in a real learning session.",
    position: 'center',
    ctaLabel: 'Try Demo Lesson →',
    navigateTo: '/tutorial/lesson',
  },
]
