---
id: fe-jun-m9-08
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
lesson: ci_for_frontend
title: "CI for Frontend"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-07]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what CI is and why frontend teams use it"
    - "Lists what typically runs in a frontend CI pipeline"
    - "Explains what fails a CI run"
    - "Describes a basic GitHub Actions workflow structure"
  keywords: [CI, continuous integration, GitHub Actions, workflow, lint, test, build, type-check, fail, push]
  modelAnswer: |
    Continuous Integration (CI) automatically runs checks on every code push or pull request. For frontend teams, CI typically runs: ESLint (lint errors fail the build), TypeScript type checking (type errors fail), tests (failing tests fail), and the production build (build errors fail). This catches regressions before they're merged. GitHub Actions workflows are defined in .github/workflows/*.yml files. A workflow runs on a trigger (push, pull_request), uses a runner (ubuntu-latest), and has steps (checkout, setup node, install, lint, test, build). If any step fails, the PR is blocked.
guidedSteps:
  - id: fe-jun-m9-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A developer pushes code with a TypeScript type error. CI is configured with `tsc --noEmit`. What happens?"
    inputConfig:
      options:
        - "The type error is logged as a warning but CI passes"
        - "CI fails — the type error causes tsc to exit with a non-zero code"
        - "TypeScript errors never fail CI — they're only shown in the IDE"
        - "The build automatically fixes the type error"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["CI fails — the type error causes tsc to exit with a non-zero code"]
      rejectedFeedback: "tsc exits with code 1 when there are type errors. CI systems treat non-zero exit codes as failures — any step that exits with a non-zero code fails the workflow. This is the mechanism by which lint, test, and type-check failures block merging: each tool exits with code 1 on failure."
    hint: "CI runners detect failure by exit code — 0 means success, anything else means failure."
    reflectionPrompt: "This is why CI is automated: it doesn't get tired, doesn't skip steps, and doesn't make exceptions. Every push is checked with the same rigor."
  - id: fe-jun-m9-08-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Without CI, what are two ways that broken code can reach the main branch, even if the developer tested locally?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [forget, local, environment, different, version, machine, skip, human, error, different machine]
      rejectedFeedback: "1) The developer forgot to run tests before pushing (human error — easy to do under time pressure). 2) Something that works on their machine fails on others — different Node version, different OS, different environment variable. CI runs in a clean, consistent environment that catches both: it always runs checks, and it always runs them in the same environment."
    hint: "Think about what a developer might skip when busy, and about 'works on my machine'."
    reflectionPrompt: "CI removes the human reliability requirement. You no longer depend on every developer to remember every check. The pipeline remembers — always."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which GitHub Actions trigger would run CI on every pull request to main?"
    options:
      - "on: push: branches: [main]"
      - "on: pull_request: branches: [main]"
      - "on: merge: to: main"
      - "on: review: approved"
    correctIndex: 1
    feedback: "on: pull_request: branches: [main] triggers the workflow on any PR targeting the main branch. This is the most common frontend CI trigger — it runs checks before code is merged, not after. You can also combine with on: push to catch direct pushes to main."
retrieval:
  recall: "What four checks does a typical frontend CI pipeline run?"
  explain: "Why does CI run in a clean environment, and what problems does this solve?"
  mistakeId:
    code: |
      # GitHub Actions workflow — missing a critical step
      - name: Run tests
        run: npm run test
      # Missing: npm install before running tests
    answer: "CI runners start with a clean environment — no node_modules. Without `npm install` (or `npm ci`) before running tests, the test command fails because there are no packages. The workflow should be: checkout → setup Node → npm ci (faster than npm install in CI) → lint → test → build. npm ci installs exactly what's in package-lock.json and is faster and more deterministic than npm install in CI environments."
---

# Hook

Your colleague merges a PR. It breaks the main branch. They tested locally but forgot to run the linter. The type error they introduced only shows up when TypeScript is run with strict mode — which is on in CI but not their personal config.

CI exists to catch what humans miss. Every time, without exception.

# Lore Introduction

*"The Guild does not trust a single inspector,"* the Quality Master explains. *"One inspector tires. One inspector has a bad day. One inspector forgets a step. The Guild uses a machine — it checks every piece, every time, in the same way."*

She points to the CI pipeline. *"This is the machine. It does not forget. It does not get tired. It checks every push."*

# Core Learning

## Concept Introduction

**CI (Continuous Integration)** automatically runs checks when code is pushed or a PR is opened. For frontend teams:

**A typical CI pipeline runs:**
1. `npm ci` — clean install from lock file
2. `npm run lint` — ESLint check
3. `tsc --noEmit` — TypeScript type check
4. `npm run test` — Vitest/Jest tests
5. `npm run build` — Production build check

If any step exits with a non-zero code, CI fails and the PR cannot be merged.

**GitHub Actions workflow (.github/workflows/ci.yml):**
```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  check:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Setup Node.js
        uses: actions/setup-node@v4
        with:
          node-version: '20'
          cache: 'npm'

      - name: Install dependencies
        run: npm ci

      - name: Lint
        run: npm run lint

      - name: Type check
        run: npx tsc --noEmit

      - name: Test
        run: npm run test -- --run

      - name: Build
        run: npm run build
```

## Why It Matters

CI enforces quality gates that every developer runs, every time. No more "I forgot to run the linter" or "it works on my machine." CI runs in a clean, consistent environment — catching issues that local setups might mask.

## Common Mistakes

- **Using npm install instead of npm ci in CI.** `npm ci` is faster, cleaner, and strictly follows the lock file — designed for CI environments.
- **Missing the type check step.** Vite builds don't type-check. Add `tsc --noEmit` separately.
- **Long feedback loops.** If CI takes 10 minutes, developers push less often and batch changes — hiding the source of failures. Aim for CI under 5 minutes.
- **Not caching node_modules.** Use `cache: 'npm'` in the Node setup action to cache installed packages between runs.

## Mini Summary

- CI runs lint, type-check, tests, and build automatically on every push/PR
- If any step fails, the PR is blocked
- Use `npm ci` (not `npm install`) in CI for fast, deterministic installs
- Define workflows in `.github/workflows/*.yml`

# Guided Practice Quest

Work through the two guided steps to verify you understand why CI catches what local testing misses.

# Solo Practice Quest

Write the steps (not YAML) for a complete CI pipeline for a React + TypeScript + Vitest project. List each step in order, what command runs, and what failure means. Include: install, lint, type-check, test, build.

# Integration

**Psychology — Error Detection and Feedback Timing**

Research in cognitive psychology and systems engineering shows that the cost of fixing a defect grows exponentially the later it's discovered. A type error caught in CI (before merge) takes minutes to fix. The same error caught in production takes hours (debug, hotfix, deploy, communicate). CI moves error detection as early in the feedback loop as possible — consistent with the psychological finding that immediate feedback produces faster learning and behaviour change. The engineer who sees a CI failure 2 minutes after pushing has full context of what they changed; the engineer debugging a production issue 3 days later has lost that context. CI is a feedback system optimised for learning speed and defect cost minimisation.

# Lore Conclusion

*"The machine never sleeps,"* the Quality Master says, watching another PR trigger the workflow. *"Every piece inspected. Every check run. The Guild's standards are upheld — not by willpower, but by design."*

---
