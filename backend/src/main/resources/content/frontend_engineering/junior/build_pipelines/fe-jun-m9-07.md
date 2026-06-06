---
id: fe-jun-m9-07
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: build_pipelines
topicTitle: "Build Pipelines"
topicSortOrder: 3
lesson: what_is_a_build
title: "What is a Build Pipeline?"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-06]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what a build pipeline does"
    - "Lists the typical steps in a frontend build pipeline"
    - "Distinguishes between the source and the output"
    - "Explains why a build step is necessary"
  keywords: [pipeline, transpile, bundle, minify, tree-shake, source, output, dist, TypeScript, JSX]
  modelAnswer: |
    A build pipeline is a sequence of transformations that converts your source code (TypeScript, JSX, modern CSS) into output files that browsers can understand and load efficiently. The steps typically include: type-checking (TypeScript), transpiling (JSX → JS, TS → JS), bundling (many files → fewer files), tree-shaking (removing unused code), minifying (removing whitespace, shortening names), and code splitting (separating into chunks). The source lives in src/; the output lives in dist/. The build step is necessary because browsers cannot natively execute TypeScript or JSX, and unbundled, unoptimised code loads slowly.
guidedSteps:
  - id: fe-jun-m9-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Why can't a browser directly execute a `.tsx` file?"
    inputConfig:
      options:
        - "Browsers only support files with .html extensions"
        - "Browsers support JavaScript but not TypeScript syntax or JSX syntax"
        - "tsx files are too large for browsers to parse"
        - "Browsers require files to be compressed with gzip first"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Browsers support JavaScript but not TypeScript syntax or JSX syntax"]
      rejectedFeedback: "Browsers execute standard JavaScript (ES2020+). TypeScript adds static types (not in the JS spec), and JSX is a syntax extension for writing HTML-like elements in JS (also not native JS). A build step transpiles both to standard JavaScript that any browser can run. Without this step, the browser would encounter unknown syntax and throw errors."
    hint: "Think about what language browsers actually understand — is it TypeScript or JavaScript?"
    reflectionPrompt: "TypeScript and JSX are developer conveniences that require a compilation step. This is a fundamental distinction: write in a rich language, compile to a universal one."
  - id: fe-jun-m9-07-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A project has 200 JavaScript modules in src/. After the build, dist/ has 3 files. What happened during the build and why is fewer files better for the browser?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [bundle, combine, HTTP, request, fewer, network, download, load, merge]
      rejectedFeedback: "Bundling combined 200 separate files into 3. Without bundling, the browser would need to make 200 separate HTTP requests to load the app — each with its own round-trip latency. Bundling reduces this to 3 requests. With HTTP/2, many parallel requests are possible, but a single well-optimised bundle still beats many small files for initial load. Fewer files = fewer round trips = faster load."
    hint: "What does the browser have to do to load each separate file?"
    reflectionPrompt: "The web is a distributed system. Every file the browser needs requires a network request. Reducing file count reduces network round trips — a key lever for page load performance."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the correct order of a typical frontend build pipeline?"
    options:
      - "Bundle → Transpile → Minify → Tree-shake"
      - "Minify → Bundle → Transpile → Tree-shake"
      - "Transpile → Tree-shake → Bundle → Minify"
      - "Tree-shake → Minify → Transpile → Bundle"
    correctIndex: 2
    feedback: "Transpile first (TypeScript/JSX → JS), then tree-shake (remove unused code from the module graph), then bundle (combine into fewer files), then minify (compress the output). In practice, tools like Vite/Rollup do these steps together, but conceptually this is the order: transform → analyse → combine → compress."
retrieval:
  recall: "Name four transformations that happen in a frontend build pipeline."
  explain: "Why does the build output live in dist/ rather than src/?"
  mistakeId:
    code: |
      // Deploying by zipping src/ and uploading to web server
      zip -r app.zip src/
      # Upload app.zip to server
    answer: "Deploying source files (TypeScript, JSX, un-minified JS) means the browser must execute code it can't understand and download massive unoptimised files. The correct approach: run npm run build to produce dist/, then deploy dist/ (or its contents). The dist/ folder contains browser-compatible, optimised files. Source files are for developers; built files are for browsers."
---

# Hook

You write TypeScript. Browsers speak JavaScript. You use JSX. Browsers don't know what JSX is. You import 200 modules. Browsers need to make 200 network requests.

The build pipeline is the translator, the optimiser, and the consolidator — transforming what you write into what the browser can efficiently execute.

# Lore Introduction

*"The master glassblower's studio is not where the finished product lives,"* explains the Guild Foreman. *"The studio is where molten glass is shaped, cooled, and polished. The finished piece — transparent, strong, ready for market — emerges from the kiln transformed."*

He gestures from the src folder to the dist folder. *"Your source is the molten glass. The pipeline is the kiln. The dist folder is the finished piece, ready for the world."*

# Core Learning

## Concept Introduction

A **build pipeline** is a sequence of automated transformations applied to source code:

```
Source (src/)
    ↓
[1] Type Check    — TypeScript compiler verifies types
    ↓
[2] Transpile     — TSX/JSX → JS, modern JS → compatible JS
    ↓
[3] Tree-shake    — Remove unused exports
    ↓
[4] Bundle        — 200 files → 3 files
    ↓
[5] Minify        — Remove whitespace, shorten names
    ↓
[6] Hash names    — index.Abc123.js for cache busting
    ↓
Output (dist/)
```

**What goes in vs what comes out:**

| Source (src/) | Output (dist/) |
|---|---|
| TypeScript (.ts, .tsx) | Plain JavaScript (.js) |
| JSX syntax | `React.createElement(...)` calls |
| 200+ module files | 2–5 bundle files |
| 50KB readable JS | 15KB minified JS |
| Dev comments & logs | None |

## Why It Matters

Without a build step:
- Browsers can't execute TypeScript or JSX
- 200 files = 200 HTTP requests = slow initial load
- Unminified code is 3-5× larger than necessary
- Unused libraries are shipped to users

## Worked Example

```bash
npm run build
# Output:
# vite v5.0.0 building for production...
# ✓ 847 modules transformed.
# dist/index.html                   0.46 kB
# dist/assets/index-Abc12def.css   12.34 kB │ gzip:  3.11 kB
# dist/assets/vendor-Xyz98abc.js  142.67 kB │ gzip: 45.23 kB
# dist/assets/index-Mno34pqr.js   38.12 kB │ gzip: 11.45 kB
```

847 source modules → 3 output files. 181 kB → ~60 kB gzipped.

## Common Mistakes

- **Confusing build output with source.** Never edit files in dist/ — they're generated and will be overwritten.
- **Skipping type-checking.** Vite's build doesn't type-check by default (for speed). Add `tsc --noEmit` to your build script or CI pipeline.
- **Not checking bundle size.** A single large dependency can double your bundle. Monitor with `rollup-plugin-visualizer`.

## Mini Summary

- Build pipelines transform source (TS/JSX) into browser-compatible, optimised output
- Steps: type-check → transpile → tree-shake → bundle → minify → hash
- Source files live in src/; generated output lives in dist/
- The build is mandatory before deployment

# Guided Practice Quest

Work through the two guided steps to confirm you understand why each step in the pipeline exists.

# Solo Practice Quest

Trace the journey of a single TypeScript React component from source file to what lands in the user's browser. Describe what changes at each pipeline step and why. Cover: TypeScript → JavaScript, JSX → createElement calls, imports resolved, output minified.

# Integration

**Mathematics — Pipeline as Function Composition**

A build pipeline is a composition of functions: `output = minify(bundle(treeShake(transpile(source))))`. Each transformation is a function that takes code and returns transformed code. This is function composition — a foundational concept in mathematics and functional programming. The pipeline is pure: the same source always produces the same output. The transformations are composable: you can add, remove, or reorder steps by changing the composition. This mathematical structure (function composition over a sequence) is also the foundation of Unix pipes (`cat file | grep pattern | sort | uniq`) — the same idea applied at the operating system level. Understanding pipelines as function composition helps reason about what each step does and how they interact.

# Lore Conclusion

*"The kiln has done its work,"* the Foreman says, examining the dist folder. *"Transparent, strong, ready. What came in as raw glass comes out as a window — letting light through to the user, without the mess of the studio."*

---
