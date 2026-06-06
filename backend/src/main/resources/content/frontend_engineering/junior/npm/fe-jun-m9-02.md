---
id: fe-jun-m9-02
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: npm
topicTitle: "npm"
topicSortOrder: 1
lesson: semver_and_lockfiles
title: "Semver and Lockfiles"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-01]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what semantic versioning (MAJOR.MINOR.PATCH) means"
    - "Explains what the ^ prefix means in package.json"
    - "Explains what package-lock.json does and why it matters"
    - "Describes what happens when you run npm install on a fresh clone with a lock file"
  keywords: [semver, major, minor, patch, caret, tilde, lockfile, lock, reproducible, version]
  modelAnswer: |
    Semantic versioning uses MAJOR.MINOR.PATCH: patch = bug fix (safe to update), minor = new backwards-compatible feature (usually safe), major = breaking change (review before updating). The ^ prefix (caret) in package.json means "compatible with this version" — npm will install up to (but not including) the next major version. package-lock.json records the exact version of every installed package. When you run npm install from a fresh clone, npm uses the lock file to install exactly the same versions, not whatever is latest — ensuring every developer and CI server has identical packages.
guidedSteps:
  - id: fe-jun-m9-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Your package.json has `\"react\": \"^18.2.0\"`. A colleague runs npm install a month later. React 18.3.0 has been released. What version do they get?"
    inputConfig:
      options:
        - "Exactly 18.2.0 — the caret locks the exact version"
        - "18.3.0 — the caret allows any version in the 18.x range"
        - "19.0.0 — the caret allows any compatible version"
        - "The same as what package-lock.json recorded"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The same as what package-lock.json recorded"]
      rejectedFeedback: "If package-lock.json exists and is committed, npm install uses it to install exactly what was recorded — regardless of the ^ range. The lock file overrides the range. If there is no lock file, npm would install 18.3.0 (latest compatible). This is why committing the lock file is critical: it makes installs reproducible."
    hint: "What does the lock file do when it exists?"
    reflectionPrompt: "The caret range is what you're willing to accept; the lock file is what you actually installed. The lock file wins. This is why you should always commit it."
  - id: fe-jun-m9-02-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A library releases version 3.0.0. Your package.json says `\"mylib\": \"^2.4.1\"`. Should you update immediately? What does the major version bump tell you?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [breaking, major, change, review, migration, careful, backwards, incompatible]
      rejectedFeedback: "A major version bump (2.x → 3.0.0) signals breaking changes — the API is not backwards compatible. The caret will NOT automatically update to 3.0.0 (it stays below the next major). Before updating, read the changelog and migration guide. Test thoroughly. Major updates are deliberate choices, not automatic. Minor and patch updates are generally safe to accept."
    hint: "What does the MAJOR number in semver signal about backwards compatibility?"
    reflectionPrompt: "Semantic versioning is a promise from the library author. Patch: I fixed bugs. Minor: I added features, nothing broke. Major: something changed in a way that may require updates to your code."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the purpose of package-lock.json?"
    options:
      - "To prevent developers from updating packages"
      - "To record the exact version of every installed package for reproducible installs"
      - "To store your npm authentication token"
      - "To list only devDependencies"
    correctIndex: 1
    feedback: "The lock file captures the resolved dependency graph — the exact version of every package (including transitive dependencies). When any developer or CI server runs npm install, they get exactly the same packages, regardless of what new versions may have been published since. This is reproducibility."
retrieval:
  recall: "What do MAJOR, MINOR, and PATCH mean in semantic versioning?"
  explain: "Why should package-lock.json be committed to version control?"
  mistakeId:
    code: |
      # .gitignore
      node_modules
      package-lock.json  # ← Added by a developer to "avoid conflicts"
    answer: "Ignoring package-lock.json means every developer and CI server installs slightly different package versions — whatever npm resolves as 'compatible' on that day. Over time, subtle differences between versions cause 'works on my machine' bugs. The lock file exists specifically to prevent this. Merge conflicts in package-lock.json are resolved by running npm install after merging — not by ignoring the file."
---

# Hook

Your app worked perfectly yesterday. This morning CI is red. Nothing in your code changed. A transitive dependency released a patch that has a bug. Because you didn't have a lock file, CI installed a different version than you tested with.

"Works on my machine" — because your machine had the old version. CI had the new one.

Lock files solve this.

# Lore Introduction

*"The Codex of Ingredients must be precise,"* the Chief Alchemist explains. *"'A handful of moonflower' is not a recipe — it changes with the season, the harvest, the alchemist's hands. The Codex must say: 47.3 grams, batch reference 2024-Oct-12."*

She holds up two vials — identical appearance, different batches. *"One heals. One does nothing. The recipe must be locked to the batch that works."*

# Core Learning

## Concept Introduction

**Semantic Versioning (semver):** `MAJOR.MINOR.PATCH`

| Part | Meaning | Safe to auto-update? |
|---|---|---|
| `PATCH` (x.x.1 → x.x.2) | Bug fix, no API changes | ✅ Usually safe |
| `MINOR` (x.1.x → x.2.x) | New backwards-compatible features | ✅ Usually safe |
| `MAJOR` (1.x.x → 2.x.x) | Breaking API changes | ⚠️ Review carefully |

**Version ranges in package.json:**
```json
"react": "^18.2.0"    // Accept 18.x.x but not 19.x.x (caret)
"lodash": "~4.17.0"   // Accept 4.17.x but not 4.18.x (tilde)
"express": "4.18.2"   // Exact version only
```

**package-lock.json:** The lock file records the exact resolved version of every package (and their dependencies). It's created/updated on every install.

```json
{
  "react": {
    "version": "18.2.0",   // Exact version installed
    "resolved": "https://registry.npmjs.org/react/-/react-18.2.0.tgz",
    "integrity": "sha512-..."
  }
}
```

## Why It Matters

Without a lock file, two developers running `npm install` a week apart may get different patch versions of a shared library. One patch may have introduced a bug. Now their apps behave differently. This is "works on my machine" — caused by non-deterministic installs.

## Common Mistakes

- **Adding package-lock.json to .gitignore.** This defeats its entire purpose. Always commit it.
- **Automatically updating major versions.** Major bumps have breaking changes. Read the migration guide.
- **Using exact versions everywhere.** Overly restrictive versioning means you miss security patch updates. The caret (^) is usually right for most packages.
- **Running `npm install packagename` to update a package.** Use `npm update packagename` for minor/patch updates. Manual installs can change the lock file unexpectedly.

## Mini Summary

- Semver: MAJOR = breaking, MINOR = new features, PATCH = bug fixes
- `^` allows minor/patch updates; `~` allows patch only; exact version allows nothing
- package-lock.json records the exact installed versions — commit it
- Major version updates require deliberate review

# Guided Practice Quest

Work through the two guided steps to verify you understand semver semantics and when the lock file takes precedence over the version range.

# Solo Practice Quest

Look at these package.json entries: `"axios": "^1.6.0"`, `"date-fns": "~3.3.0"`, `"tailwindcss": "3.4.1"`. For each, describe: what versions could npm install? Under what circumstances would you see a different version than another developer?

# Integration

**Philosophy — The Problem of Identity Over Time**

Semantic versioning is a solution to the Ship of Theseus problem in software: if a library replaces all its APIs, is it still the same library? Semver answers: major version bumps signal discontinuity. The library is "new" in the sense that its contract has changed. This maps to philosophical discussions of identity through change — what makes something the same thing despite alterations? Semver provides a practical answer: sameness within a major version (contract preserved), non-sameness across major versions (contract broken). The lock file takes a different philosophical stance: it freezes identity at a moment in time — this exact configuration, no change. Both are valuable: ranges for flexibility, locks for reproducibility.

# Lore Conclusion

*"The Codex is locked to the batch,"* the Chief Alchemist says, reviewing the lock file. *"Every alchemist in the guild, every cauldron in the Academy — all use the same ingredients. The potion is consistent. The recipe is trustworthy."*

---
