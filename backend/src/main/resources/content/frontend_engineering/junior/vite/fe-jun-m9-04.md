---
id: fe-jun-m9-04
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: vite
topicTitle: "Vite"
topicSortOrder: 2
lesson: what_is_vite
title: "What is Vite?"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-03]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what Vite is and what it replaces"
    - "Explains why Vite is faster than webpack for development"
    - "Describes what HMR is and why it matters"
    - "Identifies what Vite does differently in development vs production"
  keywords: [Vite, ESM, HMR, bundle, webpack, dev server, fast, native, browser, production]
  modelAnswer: |
    Vite is a modern frontend build tool. In development, it serves source files as native ES modules — the browser imports them directly, without bundling. This makes the dev server start nearly instantly regardless of project size. HMR (Hot Module Replacement) updates only the changed module in the browser without a full page reload. In production, Vite uses Rollup to bundle and optimise the app. Compare to webpack: webpack bundles everything upfront on dev server start, which gets slow as projects grow. Vite defers work to the browser in development, making it consistently fast.
guidedSteps:
  - id: fe-jun-m9-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Why does Vite start the development server faster than webpack as a project grows?"
    inputConfig:
      options:
        - "Vite uses faster hardware than webpack"
        - "Vite bundles files on demand as the browser requests them, not all upfront"
        - "Vite skips TypeScript checking on startup"
        - "Vite caches all files in the browser's service worker"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Vite bundles files on demand as the browser requests them, not all upfront"]
      rejectedFeedback: "webpack builds a complete bundle before the dev server is ready. As your project grows, this initial build gets slower. Vite doesn't pre-bundle — it serves native ES modules directly, only transforming files when the browser requests them. The 10th file loaded takes the same time as the 1st. Dev server startup stays fast regardless of project size."
    hint: "Think about what has to happen before you can see the app in the browser with each tool."
    reflectionPrompt: "Vite's design insight: the browser can import ES modules natively. Don't bundle before serving — serve modules and let the browser handle the graph. Only bundle for production, where optimisation matters."
  - id: fe-jun-m9-04-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "You are editing a CSS file. With HMR, what happens in the browser? Compare to what happens without HMR."
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [reload, update, instant, page, refresh, state, module, replace, live]
      rejectedFeedback: "Without HMR: the page fully reloads — you lose React state (form inputs, scroll position, modal open state) and wait for the full page to re-render. With HMR: Vite hot-swaps just the CSS module. The styles update instantly. No page reload. React state is preserved. For CSS changes especially, HMR is nearly instant because styles don't require JavaScript re-execution."
    hint: "What is the difference between swapping a module and reloading the whole page?"
    reflectionPrompt: "HMR turns 'edit → save → reload → navigate back to where you were' into 'edit → save → see the change'. Over a day of development, this saves hours of reload cycles."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Vite uses different strategies for development and production. Which is correct?"
    options:
      - "Development: bundles with Rollup. Production: serves native ESM."
      - "Development: serves native ESM via a dev server. Production: bundles with Rollup."
      - "Both development and production use Rollup bundling."
      - "Both development and production serve native ESM directly."
    correctIndex: 1
    feedback: "Development: Vite serves native ES modules without bundling — fast startup, instant HMR. Production: Vite uses Rollup to bundle, tree-shake, minify, and split code into optimised chunks for delivery. The two environments have different priorities: speed of iteration (dev) vs performance for users (production)."
retrieval:
  recall: "What does HMR stand for and what does it do?"
  explain: "Why does Vite's dev server start faster than webpack's as a project grows in size?"
  mistakeId:
    code: |
      // Developer uses Create React App for a new project in 2025
      npx create-react-app my-app
    answer: "Create React App uses webpack under the hood and is no longer maintained (archived in 2023). For new projects in 2025, use Vite: `npm create vite@latest my-app -- --template react-ts`. Vite provides: faster dev server startup (native ESM), instant HMR, TypeScript support out of the box, and active maintenance. CRA was the right choice in 2018; Vite is the right choice now."
---

# Hook

You join a large team. Their webpack-based React project takes 45 seconds to start the dev server. Every time you change a file, you wait 3–5 seconds for the page to reload.

A colleague's project uses Vite. Dev server starts in under a second. File changes appear in 50ms.

Same React code. Different tools. Massively different developer experience.

# Lore Introduction

*"The old forge took an hour to heat before any work could begin,"* Master Taevin explains. *"Every morning, an hour wasted. Every mistake, an hour to reheat."*

He gestures to a new forge that glows immediately. *"The new forge is ready the moment you strike. The heat is already there — waiting for your work, not the other way around."*

Vite is the new forge.

# Core Learning

## Concept Introduction

**Vite** (/viːt/, French for "fast") is a modern frontend build tool created by Evan You (creator of Vue.js). It serves two roles:
1. **Dev server** — serves your source code for development
2. **Production builder** — bundles your app for deployment

**Why it's fast:**

Traditional bundlers (webpack, Parcel) bundle all your code **before** serving it. As projects grow, this pre-bundling gets slower.

Vite's approach:
- **Dev:** Serve source files as **native ES modules** directly to the browser. The browser handles imports; Vite only transforms files on demand.
- **Production:** Bundle with **Rollup** for optimised delivery (code splitting, minification, tree-shaking).

**Hot Module Replacement (HMR):** When you change a file, Vite updates only that module in the browser — no full reload. React state is preserved.

## Why It Matters

Developer experience affects productivity. A 45-second startup and 3-second reload cycle vs 1-second startup and 50ms updates adds up to hours saved per developer per day. Vite made fast tooling the default, not a luxury.

## Worked Example

**Starting a Vite React project:**
```bash
npm create vite@latest my-app -- --template react-ts
cd my-app
npm install
npm run dev
```

**Project structure:**
```
my-app/
├── index.html          # Entry point (not in src/)
├── vite.config.ts      # Vite configuration
├── src/
│   ├── main.tsx        # App entry
│   └── App.tsx
└── package.json
```

**package.json scripts (auto-configured):**
```json
"scripts": {
  "dev":     "vite",          // Start dev server
  "build":   "vite build",    // Production build
  "preview": "vite preview"   // Preview production build locally
}
```

## Common Mistakes

- **Using Create React App for new projects.** CRA is archived. Use Vite.
- **Confusing dev and build output.** The dev server doesn't write files to disk — it serves in memory. Only `vite build` produces the dist/ folder.
- **Expecting TypeScript errors to block the dev server.** Vite transpiles TypeScript but doesn't type-check in the dev server (for speed). Run `tsc --noEmit` separately or in CI for type safety.
- **Treating the dev server as production.** NEVER serve the Vite dev server to real users. Build first, then serve the dist/ folder.

## Mini Summary

- Vite is a fast frontend build tool: instant dev server, lightning HMR
- Dev: serves native ES modules without bundling
- Production: uses Rollup for optimised bundles
- Use `npm create vite@latest` to start a new React project

# Guided Practice Quest

Work through the two guided steps to understand why Vite's architecture makes it faster and what HMR provides.

# Solo Practice Quest

Explain to a junior developer why they should use Vite instead of Create React App for their new project. Cover: startup speed, HMR, maintenance status, and TypeScript support. Aim for 3–4 sentences that would genuinely persuade them.

# Integration

**Mathematics — Amortised vs Eager Computation**

Vite's performance advantage can be explained with amortised analysis — a concept from algorithm design. webpack eagerly computes the full bundle upfront (O(n) work before any file is served). Vite amortises the work: it does a small amount of work per file on demand, only when the browser requests it. For a project with 500 modules where you're currently editing 3, webpack processes all 500; Vite processes 3 (plus their dependency chain). As project size grows, webpack's startup cost is O(total modules); Vite's is effectively O(current file + imports). This is the same principle that makes lazy loading, on-demand database queries, and lazy evaluation faster than eager alternatives at scale.

# Lore Conclusion

*"The forge is ready,"* Master Taevin says as the dev server starts in 800ms. *"Not in an hour. Not after much ceremony. Ready now. Begin your work."*

The Vite rune lights up — sharp, bright, immediate.

---
