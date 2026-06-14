---
id: fe-jun-m9-15
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: prettier
topicTitle: "Prettier"
topicSortOrder: 5
lesson: formatting_workflow
title: "Formatting in Your Workflow"
sortOrder: 3
difficulty: 4
estimatedMinutes: 20
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-13, fe-jun-m9-14]
integrationDomains: [psychology, sociology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains format on save and how to configure it in VS Code"
    - "Explains how Prettier integrates with pre-commit hooks"
    - "Explains why Prettier should run in CI"
    - "Describes the complete ESLint + Prettier workflow"
  keywords: [format on save, pre-commit, lint-staged, CI, check, prettier --check, workflow, consistent]
  modelAnswer: |
    Format on save: configure VS Code with editor.formatOnSave and [typescript] defaultFormatter set to esbenp.prettier-vscode. Pre-commit: add Prettier to lint-staged alongside ESLint — staged files get formatted automatically before commit. CI: run prettier --check (exits with 1 if any file differs from formatted) to block PRs where someone committed unformatted code. The complete workflow: format on save catches most issues instantly; pre-commit formats staged files; CI is the final gate. Combined with ESLint, the toolchain handles both formatting (Prettier) and code quality (ESLint) automatically.
guidedSteps:
  - id: fe-jun-m9-15-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What does `prettier --check` do in CI, compared to `prettier --write`?"
    inputConfig:
      options:
        - "--check formats files; --write only shows what would change"
        - "--check exits with code 1 if files are not formatted; --write formats files in place"
        - "--check runs faster than --write"
        - "They are identical — both format files"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["--check exits with code 1 if files are not formatted; --write formats files in place"]
      rejectedFeedback: "Use --write locally to format files. Use --check in CI to verify files are already correctly formatted. --check doesn't modify files — it only reports whether they match Prettier's output. If they don't match (someone committed unformatted code), --check exits with code 1, failing CI. This is what you want: CI should not modify files, only verify them."
    hint: "In CI, should your pipeline be modifying files or verifying they're already correct?"
    reflectionPrompt: "CI should verify, not fix. If a developer commits unformatted code, CI should reject it and tell them to fix it. This preserves the principle that CI is a verification gate, not an automated fixer."
  - id: fe-jun-m9-15-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Describe the ideal moment in a developer's workflow for Prettier to run automatically, without requiring manual intervention."
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [save, commit, automatically, format, hook, pre-commit, on save, staged]
      rejectedFeedback: "Two ideal moments: 1) On save (format-on-save in VS Code) — files are formatted the instant you save them, before you even think about committing. 2) Pre-commit hook (lint-staged) — any staged file is formatted before the commit. These two together mean unformatted code almost never makes it into a commit. CI --check is the final verifier for the edge cases."
    hint: "When does a developer usually stop thinking about their code and move on? That's when formatting should happen."
    reflectionPrompt: "The best workflow integrations are the ones you don't have to think about. Format on save is invisible — code is formatted before you notice it needed formatting."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You add Prettier to lint-staged. Which files does it format on `git commit`?"
    options:
      - "All files in the project"
      - "Only the files staged for the current commit"
      - "Only files that have Prettier errors"
      - "Files in the last 10 commits"
    correctIndex: 1
    feedback: "lint-staged runs configured tools only on staged files — the files being committed. Formatting all files on every commit would be slow and create huge diffs. Formatting only staged files means each commit's files are guaranteed to be formatted, without touching unrelated files."
retrieval:
  recall: "What is the difference between `prettier --write` and `prettier --check`?"
  explain: "Why is format-on-save preferable to running Prettier manually?"
  mistakeId:
    code: |
      // CI workflow — Prettier auto-formats files in CI
      - name: Format code
        run: npx prettier --write .
      - name: Commit formatted files
        run: git add . && git commit -m "ci: format code"
    answer: "CI should not modify and commit code. This pattern: makes CI non-deterministic (the repo changes after each run), obscures who is responsible for formatting (CI, not the developer), and can cause git conflicts. The correct approach: run prettier --check in CI. If it fails, the developer is told to format their code before pushing. The fix happens locally, not in CI."
---

# Hook

Developer A has format-on-save in VS Code. Developer B doesn't use VS Code. Developer C forgets to format before committing. The codebase has three different formatting styles across three contributors.

The solution isn't hoping everyone remembers — it's automating formatting at every stage.

# Lore Introduction

*"The printing press did not ask each scribe to remember the type settings,"* the Master Printer says. *"The settings were fixed in the press itself. Every page printed from the same press looked the same — regardless of who operated it."*

She sets the Prettier configuration. *"This is the press. Every save, every commit, every push — the same formatting. Regardless of which developer touches the file."*

# Core Learning

## Concept Introduction

**Format on save (VS Code):**
```json
// .vscode/settings.json
{
  "editor.formatOnSave": true,
  "editor.defaultFormatter": "esbenp.prettier-vscode",
  "[typescript]": {
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  },
  "[typescriptreact]": {
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  }
}
```

Commit `.vscode/settings.json` — all team members get the same editor settings.

**Pre-commit (lint-staged):**
```json
// package.json
"lint-staged": {
  "*.{ts,tsx,js,jsx,json,css,md}": ["prettier --write", "git add"]
}
```

Staged files are formatted before commit. Non-staged files untouched.

**CI (verify, don't fix):**
```yaml
- name: Check formatting
  run: npx prettier --check .
```

`--check` exits with 1 if any file differs from Prettier's output.

**The complete ESLint + Prettier workflow:**

```
Save file
  → Prettier formats (format on save)
  → ESLint highlights remaining issues (editor)

git commit
  → lint-staged: prettier --write (format staged)
  → lint-staged: eslint --fix (fix auto-fixable lint)

CI
  → prettier --check (verify formatting)
  → npm run lint (verify lint)
  → tsc --noEmit (verify types)
  → npm run test (verify tests)
  → npm run build (verify build)
```

## Why It Matters

At every stage, formatting is handled automatically. By the time code reaches CI, it's already formatted (by save and pre-commit). CI `--check` is just verification — it should almost never fail. The developer experience is frictionless: write code, save, commit, push. Formatting just happens.

## Common Mistakes

- **Using `prettier --write` in CI.** CI should verify, not fix. Use `--check`.
- **Not committing `.vscode/settings.json`.** Team members who clone without this file won't get format-on-save.
- **Forgetting CSS/JSON/markdown.** Prettier formats these too. Include them in lint-staged.
- **Large diffs when first adopting Prettier.** Running Prettier on an existing codebase creates a large formatting commit. Do it in a single dedicated PR, not mixed with feature work.

## Mini Summary

- Format on save: VS Code + Prettier extension (commit `.vscode/settings.json`)
- Pre-commit: lint-staged runs Prettier on staged files automatically
- CI: `prettier --check` verifies formatting without modifying files
- Combined ESLint + Prettier workflow handles both quality and formatting automatically

# Guided Practice Quest

Work through the two guided steps to confirm you understand why `--check` is for CI and how format-on-save fits in the workflow.

# Solo Practice Quest

Draw (or describe) the complete quality toolchain for a React TypeScript project. Include: ESLint, Prettier, TypeScript, Vitest, Vite. For each, describe: when it runs, what it checks, and what failure means. Show how they layer from development to CI.

# Integration

**Sociology — Collective Action and Automated Governance**

Collective action problems arise when individual rational behaviour leads to collectively suboptimal outcomes. Each developer individually might prefer their own formatting style — but collectively, mixed styles harm the team. Automation resolves collective action problems by making the individually rational behaviour the same as the collectively optimal one. When format-on-save is the default and pre-commit hooks enforce it, the easiest path for each developer is also the team-optimal path. This mirrors Elinor Ostrom's work on common pool resources: shared resources (the codebase) require governance mechanisms to prevent degradation. Automated tooling is governance without politics — rules encoded in configuration, enforced without human intervention, applied equally to every contributor.

# Lore Conclusion

*"The press is set,"* the Master Printer says, watching the formatted output appear. *"Every developer who uses it produces consistent pages. Those who forget to use it are reminded at the gate. The archive is clean — not because every scribe is disciplined, but because the process is."*

Module 9 is complete. Your tooling foundation is solid.

---
