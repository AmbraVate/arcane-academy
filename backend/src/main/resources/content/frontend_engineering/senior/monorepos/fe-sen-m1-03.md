---
id: fe-sen-m1-03
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m1
moduleTitle: "Module 1: Frontend Architecture"
moduleGlyph: "🏛️"
moduleSortOrder: 1
topicSlug: monorepos
topicTitle: "Monorepos"
topicSortOrder: 3
lesson: monorepos
title: "Monorepos"
sortOrder: 3
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [economics, systems_thinking]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains what a monorepo is and how it differs from polyrepo and monolith
    - Describes the role of Turborepo or Nx in managing build caching and task orchestration
    - Articulates the benefits of shared packages and atomic cross-package commits
    - Discusses the real costs of monorepos (tooling overhead, CI complexity, onboarding)
    - Makes a reasoned recommendation for when monorepos are and are not the right choice
  keywords: [monorepo, Turborepo, Nx, workspace, package, caching, pipeline, atomic, polyrepo, shared]
  modelAnswer: |
    A monorepo is a single git repository containing multiple packages or applications. It is distinct from a monolith (one package, one deployable unit) and from polyrepos (multiple separate git repositories).

    The primary benefit is atomic commits: a change that touches shared UI components and the three applications that consume them can land in a single PR and commit. There are no "update the package version and then update all consumers" workflows. Refactoring across package boundaries is straightforward.

    Turborepo and Nx are build orchestration tools that make monorepos practical at scale. They implement remote caching (if nothing changed in a package, reuse the cached build output), parallel task execution, and dependency-aware pipeline ordering (build shared packages before the apps that depend on them).

    The costs are real: initial tooling setup is significant, CI pipelines become more complex, and developers need to understand workspace concepts (npm/pnpm workspaces, package.json `name` as the import alias). Large monorepos can have slow git operations on lower-spec machines.

    Monorepos are best justified when: multiple applications share substantial code, cross-team atomic commits are frequent, or you want to enforce consistent tooling (TypeScript config, ESLint, testing) across all packages. Polyrepos are simpler when teams and applications are truly independent.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A company has a React web app, a React Native mobile app, and a component library. All three are currently separate git repositories. Developers frequently make changes to the component library that require simultaneous updates to both apps. What monorepo benefit addresses this pain directly?"
    options:
      - "Remote caching of build artifacts"
      - "Atomic commits — one PR can change the library and all consuming apps simultaneously"
      - "Faster CI pipeline"
      - "Better TypeScript support"
    correctIndex: 1
    feedback: "The core pain described is cross-repository coordination: change library, update version, update consumers in separate PRs. Monorepos eliminate this by making all packages part of one repository — one atomic commit covers the library change and all consumer updates, and they are always in sync."
  - type: SHORT_TEXT
    prompt: "Turborepo's most impactful performance feature is its cache. Explain in one sentence what Turborepo caches and why this speeds up CI."
    hint: "Think about what happens when you run `turbo build` and nothing has changed in a package."
  - type: FILL_BLANK
    prompt: "In a pnpm/npm workspace monorepo, each package declares its name in package.json, which other packages use as the ___ path when importing."
    answer: "import"
    hint: "Instead of a relative path like `../../packages/ui`, you write the package name."
  - type: MULTIPLE_CHOICE
    prompt: "Your monorepo has packages: `@acme/ui`, `@acme/utils`, `web-app`, and `admin-app`. Both apps depend on both packages. In what order must Turborepo build them?"
    options:
      - "Build all four in parallel"
      - "Build `@acme/ui` and `@acme/utils` first (in parallel), then build `web-app` and `admin-app` (in parallel)"
      - "Build `web-app` first, then everything else"
      - "Build one at a time in alphabetical order"
    correctIndex: 1
    feedback: "Turborepo understands the dependency graph. Packages with no local dependencies build first (and can build in parallel with each other). Packages that depend on them build after. This maximises parallelism while respecting build order constraints."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What distinguishes a monorepo from a monolith?"
    options:
      - "A monorepo uses TypeScript; a monolith uses JavaScript"
      - "A monorepo is one git repo with multiple independently deployable packages; a monolith is one package that deploys as a single unit"
      - "A monorepo is smaller than a monolith"
      - "A monolith is a type of database, not a frontend concept"
    correctIndex: 1
    feedback: "A monolith is a single package or application — everything compiled and deployed together, no package boundaries. A monorepo is about source code organisation: one git repository, but multiple distinct packages that can be independently versioned and deployed. You can have a monorepo of monoliths."
  - type: MULTIPLE_CHOICE
    question: "Which Turborepo feature prevents a package from being rebuilt when its source files have not changed?"
    options:
      - "Pipeline parallelism"
      - "Remote caching"
      - "TypeScript project references"
      - "Workspace hoisting"
    correctIndex: 1
    feedback: "Remote caching stores build artifacts keyed by a hash of the inputs (source files, env vars, dependencies). If the hash matches a cached result, Turborepo skips the build entirely and restores the output. This is the primary performance benefit for large monorepos — most CI runs only rebuild the packages that actually changed."
retrieval:
  recall: "Name three types of packages commonly found in a frontend monorepo and give an example of each."
  explain: "Explain the difference between a monorepo and a polyrepo, and name one scenario where polyrepos are the better choice."
  mistakeId:
    code: |
      // turbo.json
      {
        "pipeline": {
          "build": {
            "dependsOn": [],
            "outputs": ["dist/**"]
          },
          "test": {
            "dependsOn": [],
            "outputs": []
          }
        }
      }

      // apps/web/package.json (depends on @acme/ui)
      {
        "dependencies": {
          "@acme/ui": "workspace:*"
        }
      }
    answer: "The `build` pipeline has `dependsOn: []`, meaning Turborepo will not wait for `@acme/ui` to be built before building `apps/web`. This can result in the web app being built with a stale version of the UI package. The `build` pipeline should declare `\"dependsOn\": [\"^build\"]` — the `^` prefix means 'build all local package dependencies first'."
---

# Hook

Your company has a design system, a web app, a marketing site, and an internal admin tool. They all live in four separate git repositories. The design system publishes to npm. When you fix a button accessibility bug, you: fix it, bump the version, publish, then open three separate PRs in three repos to update the dependency, wait for three separate CI pipelines, and merge three separate PRs. The fix takes three hours and three working days to land everywhere. And you did this last week too.

# Lore Introduction

The Academy's spell compendiums were once split across twelve separate vaults. The Fire spells vault, the Ice spells vault, the Combination spells vault — each managed by a different Guild. When the Synthesis Masters needed to update a foundational energy formula that every vault referenced, they sent twelve ravens and awaited twelve confirmations. Sometimes the vaults drifted out of sync. The Grand Council eventually merged all twelve vaults into one — the Great Monorepo of Arcane Knowledge. One vault. Many books. One truth.

# Core Learning

## Concept Introduction

A **monorepo** (monolithic repository) is a single git repository containing multiple related packages or applications. This contrasts with:

- **Monolith**: one package, one app, everything compiled together
- **Polyrepo**: multiple git repositories, one per package/app

A typical frontend monorepo structure:

```
acme-monorepo/
  apps/
    web/          # Main React application
    admin/        # Internal admin tool
    marketing/    # Marketing site (Next.js)
  packages/
    ui/           # Shared design system components
    utils/        # Shared utility functions
    tsconfig/     # Shared TypeScript configuration
    eslint-config/# Shared ESLint rules
  package.json    # Workspace root (pnpm/npm workspaces)
  turbo.json      # Turborepo pipeline configuration
  pnpm-workspace.yaml
```

Each `package.json` declares its name:

```json
// packages/ui/package.json
{
  "name": "@acme/ui",
  "version": "0.0.0",
  "main": "./dist/index.js"
}
```

Apps import using the package name:

```json
// apps/web/package.json
{
  "dependencies": {
    "@acme/ui": "workspace:*",
    "@acme/utils": "workspace:*"
  }
}
```

## Why It Matters

**Atomic commits**: a PR can simultaneously update the shared `ui` package and every app that uses it. No version bumping, no consumer lag.

**Dependency visibility**: the entire dependency graph is explicit and local. A change in `@acme/utils` can trigger automated tests in all packages that depend on it.

**Tooling consistency**: one `.eslintrc`, one `tsconfig.base.json`, one `.prettierrc` at the root. All packages inherit consistent standards.

**Code discovery**: developers can search the entire codebase in one place. No hunting through multiple repos to understand how things connect.

## Worked Examples

**Example 1: Turborepo pipeline configuration**

```json
// turbo.json
{
  "$schema": "https://turbo.build/schema.json",
  "pipeline": {
    "build": {
      "dependsOn": ["^build"],
      "outputs": ["dist/**", ".next/**"]
    },
    "test": {
      "dependsOn": ["^build"],
      "outputs": [],
      "cache": true
    },
    "lint": {
      "outputs": [],
      "cache": true
    },
    "dev": {
      "cache": false,
      "persistent": true
    }
  }
}
```

The `^build` in `dependsOn` means: "before building me, build all my local workspace dependencies". Turborepo resolves this to the correct build order automatically.

**Example 2: Shared TypeScript config**

```json
// packages/tsconfig/base.json
{
  "compilerOptions": {
    "target": "ES2020",
    "lib": ["dom", "dom.iterable", "esnext"],
    "strict": true,
    "moduleResolution": "bundler"
  }
}
```

```json
// apps/web/tsconfig.json
{
  "extends": "@acme/tsconfig/base.json",
  "compilerOptions": {
    "outDir": "dist"
  },
  "include": ["src"]
}
```

## Common Mistakes

**Mistake 1: Not configuring `dependsOn` correctly.** Without `"dependsOn": ["^build"]`, packages build in parallel without waiting for dependencies. Apps may build against stale package outputs, causing subtle runtime errors that only appear after a clean build.

**Mistake 2: Treating the monorepo root as an app.** Installing application dependencies at the workspace root pollutes all packages. Application-specific dependencies belong in the application's `package.json`, not the root.

**Mistake 3: Over-packaging.** Creating a new workspace package for every small utility adds overhead. A package makes sense when it has genuine reuse across multiple apps, a clear public API, and independent lifecycle needs. Two hundred packages in a small team's monorepo is usually a sign of premature decomposition.

**Mistake 4: Ignoring git performance.** Very large monorepos with thousands of files can have slow `git status` and `git log` operations. Tools like `git sparse-checkout` and `.gitignore` tuning become necessary at extreme scale.

## Mental Model

A monorepo is like an **office building**. Each company (package) has its own floor, its own team, its own purpose. But they all share the lobby (workspace root), the elevator (build tooling), and the address (single git repo). When a tenant on the 3rd floor needs something from the 1st floor, they walk down the stairs — no external shipping, no waiting for delivery. It is fast and reliable.

Polyrepos are like companies in separate buildings across the city. Independence is total, but collaboration requires external coordination, shipping delays, and version drift.

## Mini Summary

- A monorepo is one git repository containing multiple packages/apps; it is not a monolith
- Turborepo and Nx provide build caching, parallel execution, and dependency-aware pipeline ordering
- Shared packages eliminate cross-repository versioning ceremonies
- `dependsOn: ["^build"]` is the critical configuration that ensures correct build order
- Monorepos are justified by frequent cross-package changes and shared tooling needs; polyrepos are simpler for truly independent teams

# Guided Practice Quest

Your company currently has three polyrepos: a React component library, a web application, and a mobile application. Sketch the migration strategy to a monorepo, including how you would structure the workspace, what Turborepo pipeline steps you need, and what the first atomic commit would look like.

# Solo Practice Quest

You are the lead engineer tasked with setting up a monorepo for a startup that currently has one React web app. The product roadmap shows a React Native app, a marketing site, and an internal analytics dashboard within 12 months.

Design the monorepo setup. Your answer should:

1. Define the workspace structure (`apps/` and `packages/`) with justification for each package
2. Write the key `turbo.json` pipeline configuration and explain each field
3. Explain how TypeScript configuration is shared and how individual apps extend it
4. Describe the CI strategy: which commands run on which changes, and how Turborepo's caching reduces CI time
5. Identify two genuine risks of adopting a monorepo at this stage and how you would mitigate them

# Integration

**Economics — Transaction Costs:** The economist Ronald Coase argued that firm boundaries exist to minimise transaction costs. Polyrepos have high inter-repo transaction costs (versioning, coordination, sync lag). A monorepo reduces these internal transaction costs at the expense of higher intra-repo coordination costs (shared tooling decisions, common CI pipelines). The optimal choice depends on which transaction costs dominate — exactly as Coase predicted for firm boundaries.

**Systems Thinking — Feedback Loops:** A key monorepo benefit is tightening feedback loops. In polyrepos, the loop is: change -> publish -> update -> CI -> merge — multiple hops, potentially days. In a monorepo: change -> CI -> merge — one loop, same day. Tighter feedback loops catch integration problems faster and reduce the cost of rework.

# Lore Conclusion

The Great Monorepo of Arcane Knowledge was not without its challenges. When one Guild changed the foundational energy formula, every other Guild felt the tremor immediately — no drift, but also no isolation. They had to develop new disciplines: clear API boundaries between chapters, agreed-upon ownership for shared formulae, and tooling to understand what changed and what it affected. The price of unity was discipline. The reward was truth.
