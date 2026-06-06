---
id: fe-jun-m9-12
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: eslint
topicTitle: "ESLint"
topicSortOrder: 4
lesson: eslint_in_workflow
title: "ESLint in Your Workflow"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-10, fe-jun-m9-11]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains how ESLint integrates with VS Code"
    - "Explains what --fix flag does"
    - "Describes how pre-commit hooks can run ESLint automatically"
    - "Explains why ESLint should also run in CI"
  keywords: [VS Code, extension, fix, pre-commit, husky, lint-staged, CI, automatic, workflow, save]
  modelAnswer: |
    ESLint integrates with VS Code via the ESLint extension — it shows errors as red underlines in the editor and can fix auto-fixable issues on save. The --fix flag (npm run lint -- --fix) auto-corrects a subset of issues (formatting, simple rule violations). Pre-commit hooks (via husky + lint-staged) run ESLint only on staged files before each commit — preventing lint errors from ever entering the repo. CI runs ESLint on every PR to catch anything that bypassed local tools. The layers work together: editor (instant feedback), pre-commit (last local gate), CI (team-wide enforcement).
guidedSteps:
  - id: fe-jun-m9-12-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A developer runs `npm run lint -- --fix`. What happens?"
    inputConfig:
      options:
        - "ESLint removes all lint rules and starts fresh"
        - "ESLint auto-corrects issues it knows how to fix (e.g. missing semicolons, unused imports)"
        - "ESLint prompts the developer to manually fix each issue"
        - "ESLint fixes type errors as well as lint errors"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["ESLint auto-corrects issues it knows how to fix (e.g. missing semicolons, unused imports)"]
      rejectedFeedback: "The --fix flag applies auto-fixes for rules that support it. Some rules (like eqeqeq: prefer === over ==) are auto-fixable; others (like no-unused-vars: what should be done with the variable?) require human judgment and are only reported, not fixed. --fix saves time for mechanical issues but can't replace understanding the root cause."
    hint: "Not all ESLint rules can be automatically fixed — some require human decision-making."
    reflectionPrompt: "Auto-fixable rules handle the mechanical parts of code quality. Rules that require judgment (no-unused-vars: should this be removed or is it intentional?) are reported but not auto-fixed — the developer must decide."
  - id: fe-jun-m9-12-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A developer runs ESLint in their editor but forgets to run it before pushing. The PR fails CI. How would a pre-commit hook have prevented this?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [commit, hook, automatic, staged, husky, git, prevent, before, push, block]
      rejectedFeedback: "A pre-commit hook (configured with husky + lint-staged) runs ESLint automatically when `git commit` is issued — before the commit is created. If lint fails, the commit is aborted. The developer is forced to fix the issue before they can even commit, let alone push. This turns 'I forgot to lint' from a CI-breaking mistake into an impossible mistake."
    hint: "At what point in the git workflow does a pre-commit hook run?"
    reflectionPrompt: "Pre-commit hooks add a gate that requires no developer discipline — it's automatic. The best safety systems don't depend on people remembering to do things."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why should ESLint also run in CI even if developers have it in their editor and pre-commit hooks?"
    options:
      - "CI has a faster computer and can run ESLint more accurately"
      - "Developers can bypass pre-commit hooks; CI is the team-wide, unskippable enforcement"
      - "The editor version of ESLint may be outdated"
      - "ESLint rules are different in CI environments"
    correctIndex: 1
    feedback: "Pre-commit hooks can be bypassed with `git commit --no-verify`. Editor plugins can be disabled. CI is the last, unskippable gate — it runs the same check on every PR regardless of what local tools are installed or bypassed. Editor → pre-commit → CI is a layered defence: each layer catches what the previous one might miss."
retrieval:
  recall: "Name the three places where ESLint can run in a typical frontend workflow."
  explain: "What is lint-staged and why is it useful compared to running ESLint on the entire codebase in a pre-commit hook?"
  mistakeId:
    code: |
      # pre-commit hook that runs ESLint on everything
      npm run lint  # Lints all 500 files in the project
    answer: "Running ESLint on all 500 files on every commit makes commits slow (10+ seconds) — developers will bypass the hook with --no-verify. Use lint-staged instead: it runs ESLint only on files staged for the commit. If you changed 2 files, ESLint checks 2 files — taking milliseconds. Fast hooks are respected hooks."
---

# Hook

You have ESLint configured. Your editor shows errors. But your colleague pushes a PR with 12 lint errors. They use vim — no ESLint plugin. They forgot to run lint manually.

The question isn't just how to configure ESLint — it's how to make it impossible to skip.

# Lore Introduction

*"A posted notice in the library is read by those who choose to read it,"* the Head Librarian explains. *"But a gate that will not open until the correct form is completed — that enforces the rule regardless of choice."*

She gestures at the three enforcement layers. *"The notice is your editor plugin. The locked gate is your pre-commit hook. The final seal is CI. Together, they make compliance automatic."*

# Core Learning

## Concept Introduction

ESLint integrates at three points in the workflow:

**1. Editor (VS Code extension)**
- Install `dbaeumer.vscode-eslint`
- Errors appear as red underlines immediately
- Configure auto-fix on save:
```json
// .vscode/settings.json
{
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": "explicit"
  }
}
```

**2. Pre-commit hooks (lint-staged + husky)**
```bash
npm install --save-dev husky lint-staged
npx husky init
```
```json
// package.json
"lint-staged": {
  "*.{ts,tsx}": ["eslint --fix", "git add"]
}
```
Adds `.husky/pre-commit`:
```bash
npx lint-staged
```
Now lint runs automatically on staged files before every commit.

**3. CI (GitHub Actions)**
```yaml
- name: Lint
  run: npm run lint
```
The final, unskippable gate. Blocks PRs with lint errors.

**npm run lint -- --fix:**
Auto-corrects fixable issues:
```bash
npm run lint -- --fix
```

## Why It Matters

Enforcement at three layers means different failure modes are caught at different costs:
- Editor: caught instantly (zero cost)
- Pre-commit: caught before commit (seconds)
- CI: caught before merge (minutes)

Each layer is faster and cheaper than the next. Most issues are caught at the editor level; nothing escapes CI.

## Common Mistakes

- **Only editor integration.** Developers using different editors (or vim) won't have it. Use pre-commit hooks.
- **Pre-commit hooks on the whole project.** Slow and bypassed. Use lint-staged — only lint changed files.
- **No CI lint step.** Pre-commit hooks can be skipped (`--no-verify`). CI cannot.
- **Auto-fix without reviewing.** `--fix` can make changes. Always review auto-fixed code before committing.

## Mini Summary

- Editor: VS Code extension shows errors inline; configure auto-fix on save
- Pre-commit: lint-staged + husky run ESLint only on staged files automatically
- CI: ESLint as a required step blocks PRs with errors
- `npm run lint -- --fix` auto-corrects fixable issues

# Guided Practice Quest

Work through the two guided steps to understand pre-commit hooks and the role of CI as the final gate.

# Solo Practice Quest

Describe the complete ESLint workflow for a 5-person team. Where does each developer encounter lint errors? What happens if someone uses an editor without ESLint support? What prevents lint errors from being merged despite bypassing pre-commit hooks?

# Integration

**Mathematics — Defence in Depth**

Layered enforcement mirrors the mathematical concept of redundant systems in reliability engineering. A single lint check with probability p of catching a violation has failure probability (1-p). Two independent checks have failure probability (1-p)². Three checks: (1-p)³. If each layer catches 90% of violations that escape the previous layer: one layer misses 10%, two layers miss 1%, three layers miss 0.1%. This is defence in depth — a strategy from security engineering (also called layered security or Swiss cheese model) where multiple independent barriers each catch what others miss. Editor + pre-commit + CI is a three-layer system where the product of individual failure probabilities is very small. No single layer needs to be perfect.

# Lore Conclusion

*"The three gates are set,"* the Head Librarian says. *"The notice in the editor. The gate at the commit. The seal at the merge. A rule that can be bypassed in one place is enforced at the next. The library's standards are maintained — not by trust, but by architecture."*

---
