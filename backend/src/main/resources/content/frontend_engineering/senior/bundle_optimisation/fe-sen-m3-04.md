---
id: fe-sen-m3-04
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m3
moduleTitle: "Module 3: Performance Engineering"
moduleGlyph: "🚀"
moduleSortOrder: 3
topicSlug: bundle_optimisation
topicTitle: "Bundle Optimisation"
topicSortOrder: 4
lesson: bundle_optimisation
title: "Bundle Optimisation"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains code splitting and its impact on initial bundle size
    - Correctly uses React.lazy and Suspense for route-based splitting
    - Explains what a bundle visualiser reveals and how to act on it
    - Describes tree-shaking and when it fails to remove dead code
    - Synthesises a bundle optimisation strategy for a large React application
  keywords: [code splitting, lazy, Suspense, dynamic import, bundle, chunk, tree-shake, visualiser, initial load, route]
  modelAnswer: |
    Bundle optimisation reduces the amount of JavaScript users must download before the app becomes interactive. Code splitting via React.lazy and dynamic import() creates separate chunks that load on demand rather than all at once. Route-based splitting is the most impactful: the user downloads code only for the page they're visiting, not every page.

    A bundle visualiser (rollup-plugin-visualizer, vite-bundle-visualizer) shows each dependency's size contribution as a treemap. Typical findings: a date library contributes 50KB, a charting library contributes 200KB, only 10% is the app's own code. This guides replacement decisions (date-fns vs date-fns/fp, recharts vs a lighter alternative) and identifies candidates for lazy loading.

    Tree-shaking removes unused exports — but fails when libraries use CommonJS (require/module.exports), when barrel files (index.ts that re-exports everything) prevent static analysis, or when side effects prevent elimination. Configure sideEffects: false in library package.json to enable aggressive tree-shaking.

    Strategy: (1) measure current bundle with visualiser, (2) route-based code split immediately, (3) identify the 3 largest dependencies and evaluate lazy-loading or replacing them, (4) re-measure. Target: initial JS bundle under 150KB gzipped for a first-paint performance budget.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A React app has 5 routes. Without code splitting, what does a user who only visits the home page download?"
    options:
      - "Only the home page code"
      - "The code for all 5 routes — one bundle"
      - "The home page code plus routing library"
      - "Nothing — React lazy-loads automatically"
    correctIndex: 1
    feedback: "Without code splitting, `npm run build` produces a single bundle containing all routes. A user visiting only the home page downloads code for the dashboard, settings, profile, and admin pages they may never see. Route-based splitting creates separate chunks: the home page chunk loads immediately, others load only when navigated to."
  - type: SHORT_TEXT
    prompt: "You open the bundle visualiser and see a charting library accounts for 300KB of your 400KB bundle, but charts are only shown on the Analytics page (visited by 5% of users). What would you do?"
    hint: "Should all users pay the 300KB cost? When should the charting library load?"
  - type: FILL_BLANK
    prompt: "React.lazy takes a function that returns a ___ import. The component must be wrapped in ___ to handle the loading state."
    answer: "dynamic (import()); React.Suspense"
    hint: "dynamic import() returns a Promise. Suspense provides the fallback UI while the chunk loads."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Tree-shaking removes unused exports. When does it fail for a dependency?"
    options:
      - "When the dependency is listed in devDependencies"
      - "When the dependency uses CommonJS (require) instead of ES modules"
      - "When the dependency has more than 100 exports"
      - "When the dependency is installed with npm instead of yarn"
    correctIndex: 1
    feedback: "Tree-shaking relies on static analysis of ES module import/export statements. CommonJS require() is dynamic — the bundler can't statically determine which exports are used. Libraries using CommonJS include everything. Prefer ESM-first libraries; check if a library has an ESM build."
  - type: MULTIPLE_CHOICE
    question: "What is the primary benefit of route-based code splitting?"
    options:
      - "Pages load with zero JavaScript"
      - "Users download only the code for the page they're visiting, not the entire app"
      - "Bundles are smaller because React.lazy is more efficient"
      - "Code runs faster because chunks are smaller"
    correctIndex: 1
    feedback: "Route splitting means a user on the home page downloads only the home page chunk. The admin dashboard chunk (maybe 100KB) never downloads for users who never navigate there. Initial Time to Interactive improves proportionally to how much of the bundle was deprioritised."
retrieval:
  recall: "What does React.lazy() do and what is required to use it?"
  explain: "Explain why barrel files (index.ts that re-exports all components) can harm tree-shaking."
  mistakeId:
    code: |
      // Admin dashboard uses a PDF generation library (500KB)
      import PDFDocument from 'pdfmake';

      // Imported at the top of App.tsx — always loaded on startup
      export default function App() {
        return <Router><Routes>...</Routes></Router>;
      }
    answer: "The 500KB PDF library loads on every page visit, even for users who never generate a PDF. Fix: move the import inside the AdminDashboard component and use dynamic import: const AdminDashboard = lazy(() => import('./AdminDashboard')). The PDF library only loads when AdminDashboard is mounted. Users who never visit the admin page save 500KB."
---

# Hook

Your app's initial bundle is 1.2MB. Time to Interactive is 8 seconds on a mobile 4G connection. Users on slow connections give up before the app loads.

The code is correct. The problem is how much of it the user must download before they can do anything.

Bundle optimisation is about paying only for what you use.

# Lore Introduction

*"A traveller does not carry every tool in the forge when setting out on a quest,"* the Quartermaster explains. *"They carry what the quest requires. The rest remains at the forge — available if needed, but not weighing down the journey."*

She gestures at the bundle visualiser. *"This shows you what every traveller carries. Your quest is to reduce the pack."*

# Core Learning

## Concept Introduction

**Code splitting** divides the bundle into chunks loaded on demand:

```tsx
// Without splitting — one large bundle
import AdminDashboard from './AdminDashboard';
import Analytics from './Analytics';
import Settings from './Settings';

// With route-based splitting
const AdminDashboard = lazy(() => import('./AdminDashboard'));
const Analytics = lazy(() => import('./Analytics'));
const Settings = lazy(() => import('./Settings'));

function App() {
  return (
    <Suspense fallback={<PageSpinner />}>
      <Routes>
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/analytics" element={<Analytics />} />
        <Route path="/settings" element={<Settings />} />
      </Routes>
    </Suspense>
  );
}
```

**Bundle analysis:**
```bash
npm install --save-dev rollup-plugin-visualizer
# Add to vite.config.ts plugins:
visualizer({ open: true })
```

The visualiser generates a treemap showing each dependency's size. Common findings:
- Date libraries: 50-100KB (use date-fns with tree-shaking, not moment)
- Chart libraries: 100-300KB (load only on analytics pages)
- Icon sets: 200KB+ (import individual icons, not the full set)
- Your own code: usually <10% of bundle

## Why It Matters

Performance budgets: initial JS bundle < 150KB gzipped for first load performance. Every 100KB of JS costs ~1 second on average 4G (~1MB/s). Code splitting + lazy loading is the single highest-impact optimisation for most React apps.

## Common Mistakes

- **Lazy-loading too aggressively.** Splitting every component creates many tiny chunks with network overhead. Route-level splitting is almost always the right granularity.
- **Not providing a Suspense fallback.** Without a fallback, lazy components show nothing while loading — users see blank space.
- **Importing from barrel files.** `import { Button, Icon, Chart } from '@ui/components'` may include the entire library even if you use 3 components. Import directly: `import { Button } from '@ui/components/Button'`.
- **Ignoring the vendor chunk.** Third-party libraries often dominate the bundle. A bundle with 900KB of lodash and 100KB of your code needs lodash replaced, not code-split.

## Mini Summary

- ✔ Route-based code splitting with React.lazy + Suspense is the highest-impact optimisation
- ✔ Bundle visualiser shows which dependencies dominate bundle size
- ✔ Tree-shaking removes unused exports — requires ESM libraries
- ✔ Lazy-load heavy dependencies (charts, PDF, editors) on the pages that use them
- ✔ Target initial bundle <150KB gzipped for first-load performance

# Guided Practice Quest

Work through the guided steps to understand how code splitting changes what users download and when.

# Solo Practice Quest

A React SPA has routes: Home, Product Catalogue, Product Detail, Shopping Cart, Checkout, and Admin (only for 2% of users). The admin route uses a rich text editor (350KB). Design the complete bundle splitting strategy: which routes to split, when to lazy-load the editor, what to put in the main chunk. Estimate the impact on initial load.

# Integration

**Mathematics — Bandwidth, Latency, and Parse Time**

JavaScript bundle cost is not just download time. It's: download time (bundle size / connection speed) + parse time (~1ms per KB on mid-range mobile) + execution time. A 500KB bundle on a 3G connection (1.5Mb/s = 187KB/s): download = 2.7s. Parse = 500ms. Execution = 200ms. Total = ~3.4s before the app is interactive. Code splitting to 150KB initial bundle: download = 0.8s, parse = 150ms, execution = 100ms = ~1.05s. The 3× reduction in bundle size produces a 3× reduction in TTI — a direct linear relationship. This is why performance budgets focus on bytes: the mathematics is simple and the impact is predictable.

# Lore Conclusion

*"The traveller returns with a lighter pack,"* the Quartermaster observes. *"The forge's heaviest tools were left behind — available at the outpost if needed, but not slowing the journey's start."*

---
