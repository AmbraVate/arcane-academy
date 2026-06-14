---
id: fe-jun-m4-03
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m4
moduleTitle: "Module 4: Routing and Navigation"
moduleGlyph: "🗺️"
moduleSortOrder: 4
topicSlug: spa_architecture
topicTitle: "SPA Architecture"
topicSortOrder: 1
lesson: spa_trade_offs
title: "SPA Trade-offs"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies scenarios where SPA is the wrong choice"
    - "Explains code splitting and lazy loading as solutions to bundle size"
    - "Describes how SSR/SSG addresses SPA's SEO weakness"
    - "Makes an informed architecture recommendation for a given scenario"
  keywords: [trade-off, code-splitting, lazy-loading, SSR, SSG, bundle, SEO, first-contentful-paint, architecture]
  modelAnswer: |
    SPAs are not always the right choice. Content sites (blogs, docs) benefit from SSR/SSG
    for SEO and performance. Large SPA bundles can be split with React.lazy() and Suspense,
    loading route components only when needed. First Contentful Paint suffers in SPAs
    without SSR because the user sees a blank page until JS loads. Server-Side Rendering
    (Next.js) or Static Site Generation (Gatsby, Astro) addresses this.
guidedSteps:
  - id: fe-jun-m4-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A marketing site needs fast initial load, good SEO, and rarely changes. Which is most appropriate?
    inputConfig:
      options:
        - "Create React App SPA — React is always best"
        - "Static Site Generation (Astro, Next.js static) — fast, SEO-friendly, no runtime server"
        - "Server-Side Rendering — render on every request"
        - "A jQuery site — no framework needed"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Static Site Generation (Astro, Next.js static) — fast, SEO-friendly, no runtime server"]
      rejectedFeedback: "SSG generates static HTML at build time — instant loads, perfect SEO, no server costs. For a marketing site that changes infrequently, SSG is ideal. SPA would hurt initial load and SEO."
    hint: "Match the architecture to the requirements: SEO, speed, update frequency."
    reflectionPrompt: "The architecture decision tree: Does it need SEO? → SSR or SSG. Does it change rarely? → SSG. Does it need real-time data? → SPA or SSR. Is it an authenticated dashboard? → SPA. There is no single correct answer — context decides."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "React.lazy() with Suspense does what?"
    options:
      - "Makes React components execute asynchronously"
      - "Splits route components into separate JS bundles loaded only when the route is visited"
      - "Renders components in a web worker"
      - "Defers all React rendering until after page load"
    correctIndex: 1
    feedback: "Code splitting with React.lazy: const Dashboard = lazy(() => import('./Dashboard')). The Dashboard bundle is not downloaded until the user navigates to /dashboard. This reduces initial bundle size — users only download what they see."

retrieval:
  recall: "Explain code splitting with React.lazy() and when it is most valuable."
  explain: "Why does a SPA hurt First Contentful Paint, and how does SSR solve this?"
  mistakeId:
    code: "Using a full SPA for a public documentation site — search engine can't index the content"
    answer: "Use SSG (Next.js, Astro) for documentation. Pre-render all pages as static HTML at build time. Perfect SEO, fast loads, no JavaScript required for content to be visible."
---

# Hook

Every architecture decision is a trade-off. SPAs excel in some scenarios and underperform in others. Knowing when NOT to use a SPA is as important as knowing how to build one.

# Lore Introduction

*"The finest spellbook,"* says Master Aelindra, *"is not always the right tool. Sometimes a simple scroll serves better. Architecture, like magic, requires choosing the right instrument for the task at hand."*

# Core Learning

## Concept Introduction

**When SPA is wrong:**
- Public content sites needing SEO → SSG or SSR
- Low-JS environments → MPA
- Simple pages needing instant load → static HTML

**Code splitting — reduce initial bundle:**
```jsx
import { lazy, Suspense } from 'react';
const Dashboard = lazy(() => import('./pages/Dashboard'));
const Settings  = lazy(() => import('./pages/Settings'));

function App() {
  return (
    <Suspense fallback={<Spinner />}>
      <Routes>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/settings"  element={<Settings />} />
      </Routes>
    </Suspense>
  );
}
```

**Rendering strategies:**

| Strategy | HTML generated | When | Use for |
|---|---|---|---|
| CSR (SPA) | Client | At request | Authenticated apps |
| SSR | Server | At request | Dynamic content + SEO |
| SSG | Build time | At deploy | Static content |

## Common Mistakes

- **Using `React.lazy()` without a `<Suspense>` boundary**: Lazy-loaded components require a parent `<Suspense fallback={...}>` — omitting it causes React to throw an error when the lazy component loads.
- **Not code-splitting large SPA bundles**: Shipping all route components in one bundle means every user downloads every page's code on first load. Lazy-loading routes reduces initial bundle size and improves Time to Interactive.
- **Choosing SSR when the content is static**: SSR re-renders the page on every request. If the content only changes at deploy time (a marketing page, a docs site), SSG is faster, cheaper, and simpler.
- **Conflating SSR and SSG**: Both produce server-rendered HTML, but SSG builds pages once at deploy time while SSR renders on every request. Using "SSR" as a synonym for "server-rendered" leads to architecture mismatches.

## Mini Summary
- ✔ SPA: authenticated apps, dashboards, editors
- ✔ SSR: dynamic content + SEO (Next.js)
- ✔ SSG: static content + SEO (Astro, Next.js static)
- ✔ Code splitting: React.lazy + Suspense reduces initial bundle

# Solo Practice Quest

Given three scenarios — a news site, a SaaS dashboard, a company landing page — write a one-paragraph architecture recommendation for each with justification.

# Integration

**Economics — Comparative Advantage:** No framework has absolute advantage in all scenarios. SPAs have comparative advantage in interactivity; SSG in SEO and performance. Choosing architecture is applying comparative advantage theory: use each tool where its strengths outweigh its costs relative to the alternative.

# Lore Conclusion

*"The master uses the right tool. The apprentice uses the only tool they know. Learn many tools; choose well."*

---
