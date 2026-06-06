---
id: fe-jun-m9-06
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
lesson: production_builds
title: "Production Builds"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-04, fe-jun-m9-05]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what npm run build produces"
    - "Explains what tree-shaking is"
    - "Explains what code splitting is and why it helps"
    - "Describes the dist/ folder structure after a build"
  keywords: [build, dist, bundle, tree-shake, code split, minify, chunk, production, Rollup]
  modelAnswer: |
    npm run build runs Vite (using Rollup under the hood) which transpiles TypeScript/JSX, bundles all JavaScript and CSS, minifies the output, tree-shakes unused code, and splits the bundle into chunks. The output goes to dist/: index.html plus JS/CSS files with hashed names (for cache busting). Tree-shaking removes exported functions/components that are never imported anywhere — reducing bundle size. Code splitting creates separate chunks (e.g., per route) that load on demand rather than all at once, improving initial page load time. The dist/ folder is what gets deployed to a CDN or web server.
guidedSteps:
  - id: fe-jun-m9-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Your app imports `import { formatDate, parseDate } from 'date-fns'` but only uses `formatDate`. What does tree-shaking do?"
    inputConfig:
      options:
        - "Removes date-fns entirely from the bundle"
        - "Includes only formatDate in the bundle, excluding parseDate and all other unused exports"
        - "Warns you that parseDate is unused, but includes it anyway"
        - "Replaces date-fns with a smaller alternative"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Includes only formatDate in the bundle, excluding parseDate and all other unused exports"]
      rejectedFeedback: "Tree-shaking analyses your imports statically and excludes any exported code that is never imported. Since you import { formatDate } specifically, only the code for formatDate (and its dependencies) is included. parseDate and all other date-fns functions you never import are excluded. This can dramatically reduce bundle size for large utility libraries."
    hint: "Tree-shaking is named after the process of shaking a tree — dead leaves (unused code) fall out."
    reflectionPrompt: "Tree-shaking works best with ES module syntax (import/export). CommonJS (require/module.exports) is harder to tree-shake because imports are resolved at runtime. One reason to prefer ESM libraries."
  - id: fe-jun-m9-06-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A user visits your app for the first time. With code splitting by route, what advantage does this give over loading everything in one bundle?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [load, initial, route, page, visit, download, split, chunk, faster, deferred]
      rejectedFeedback: "Without code splitting, the user downloads all JS for every route on first visit — including routes they may never visit. With code splitting, only the code for the current route loads initially. Code for other routes loads when the user navigates to them. This reduces initial bundle size and page load time. For large apps, the difference can be seconds of load time."
    hint: "What does a user who only visits the home page need to download?"
    reflectionPrompt: "Code splitting is a performance optimisation that matches download cost to usage. Users pay only for the code they need, when they need it — not upfront for the entire application."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "After running `npm run build`, where do the production files appear?"
    options:
      - "In the src/ folder, overwriting your source files"
      - "In a dist/ folder at the project root"
      - "Directly in node_modules"
      - "In a build/ folder configured by webpack"
    correctIndex: 1
    feedback: "Vite outputs production files to dist/ by default (configurable via build.outDir). The dist/ folder contains index.html and optimised JS/CSS files with hashed names. This is what gets deployed — not your src/ folder. The dist/ folder should be in .gitignore."
retrieval:
  recall: "Name three optimisations that happen during `npm run build` that don't happen in development."
  explain: "Why do built files have hashed names like `index.Abc12def.js` instead of just `index.js`?"
  mistakeId:
    code: |
      # Deploying by serving the Vite dev server in production
      npm run dev &
      # Expose port 5173 to the internet
    answer: "The Vite dev server is not for production. It's unoptimised, serves unminified source files, has no security hardening, and is designed for speed-of-development, not stability under load. For production: run npm run build to create optimised files in dist/, then serve dist/ with a static file server (nginx, Netlify, Vercel, AWS S3+CloudFront). The dev server is a development tool — never expose it externally."
---

# Hook

Your app works perfectly in development. You deploy it — the static files, straight from your src folder. Users report the app takes 8 seconds to load. Your images aren't optimised. All your JavaScript is unminified. There's code for every page loaded upfront.

You needed a production build. Not your dev files.

# Lore Introduction

*"The draft copy is for editing,"* the Scribe explains. *"Full of notes, corrections, unfinished margins. You do not distribute the draft. You produce a clean copy — condensed, clear, ready for the reader."*

She gestures to the production build. *"npm run build is the clean copy. Trim the fat, seal the ink, bind the pages. Then distribute."*

# Core Learning

## Concept Introduction

`npm run build` runs `vite build`, which:

| Step | What it does |
|---|---|
| **Transpile** | Converts TypeScript and JSX to plain JavaScript |
| **Bundle** | Combines modules into fewer files |
| **Tree-shake** | Removes unused exports |
| **Minify** | Removes whitespace, shortens variable names |
| **Code split** | Creates separate chunks per route |
| **Hash filenames** | Adds content hash for cache busting |

**Output (dist/ folder):**
```
dist/
├── index.html
├── assets/
│   ├── index.Abc12def.js   # Main bundle (hashed)
│   ├── vendor.Xyz98abc.js  # Third-party libs (hashed)
│   └── index.Mno34pqr.css  # Styles (hashed)
```

**Why hashed filenames?** When you deploy a new version, browser caches may serve old files. Hash-named files change when content changes, forcing browsers to download the new version. Unchanged files keep their hash — browsers serve them from cache (free performance).

**Previewing the build locally:**
```bash
npm run build
npm run preview  # Serves dist/ on a local port
```

## Why It Matters

Production builds are 3–10× smaller than development files (minification removes whitespace and comments). Code splitting can reduce initial load time by 50%+ for large apps. A developer who ships their dev server's output is shipping an app that is orders of magnitude slower and larger than it needs to be.

## Common Mistakes

- **Not building before deploying.** Always `npm run build` first.
- **Deploying the dist/ folder contents, not the folder itself.** The index.html must be at the server root.
- **Adding dist/ to git.** It's generated — add to .gitignore, build in CI.
- **Not checking bundle size.** Run `npx vite-bundle-visualizer` or use the rollup-plugin-visualizer to see what's largest.

## Mini Summary

- `npm run build` produces optimised production files in dist/
- Optimisations: tree-shaking, minification, code splitting, hash filenames
- Preview with `npm run preview` before deploying
- Never serve the dev server in production

# Guided Practice Quest

Work through the two guided steps to verify you understand tree-shaking and code splitting benefits.

# Solo Practice Quest

Explain the lifecycle of your code from source to production: from your .tsx source file to what the user's browser actually downloads. Walk through: TypeScript compilation, JSX transformation, bundling, minification, and the user's browser cache. Aim for 4–5 steps.

# Integration

**Mathematics — Information Theory and Compression**

Minification is a form of lossless compression. Shannon's information theory tells us that any sequence of symbols contains information proportional to its entropy — the number of bits required to represent it. JavaScript source code contains much redundant information (whitespace, long variable names, comments) that adds no semantic content. Minification removes this redundancy — the same program is expressed in fewer characters. Tree-shaking is a form of dead code elimination: code never reached by execution is removed. Together, these techniques reduce bundle size by removing information-theoretically redundant content. The resulting file conveys identical information (the same program behaviour) in a smaller payload — a direct application of compression principles.

# Lore Conclusion

*"The clean copy is ready,"* the Scribe says, reviewing the dist/ folder. *"Condensed, hashed, optimised. The reader receives exactly what they need, nothing they don't. This is what we distribute."*

The Vite tome closes. You are ready to build.

---
