---
id: fe-lea-m2-03
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m2
moduleTitle: "Module 2: Design System Governance"
moduleGlyph: "🏛️"
moduleSortOrder: 2
topicSlug: versioning
topicTitle: "Versioning"
topicSortOrder: 3
lesson: versioning
title: "Versioning"
sortOrder: 3
difficulty: 4
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-lea-m2-01]
integrationDomains: [psychology, sociology, philosophy]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - "Correctly applies semantic versioning to design system changes with concrete examples of major/minor/patch"
    - "Distinguishes between breaking and non-breaking changes in component APIs specifically"
    - "Proposes a deprecation strategy with specific timelines and communication patterns"
    - "Addresses the challenge of coordinating upgrades across multiple consuming teams"
    - "Recognises that multi-version support has a real cost and proposes when it is and is not worthwhile"
  keywords:
    - semver
    - breaking
    - deprecation
    - migration
    - coordination
    - consumers
    - upgrade
    - patch
  modelAnswer: |
    Semantic versioning for a design system maps MAJOR.MINOR.PATCH to: major for breaking changes (changed prop names, removed components, changed behaviour), minor for additive changes (new components, new optional props, new variants), and patch for bug fixes and documentation updates that do not change external behaviour.

    The challenge with design system versioning is that "breaking" is more nuanced than in a standard library. A visual change — even without an API change — can be functionally breaking if consuming teams have visual regression tests. A new required prop is obviously breaking. But a behaviour change triggered by a new default value is also breaking even if no prop names changed.

    A deprecation strategy should give consuming teams predictable windows for migration. A well-functioning design system deprecation looks like: deprecation notice in version N (warning in console, note in docs), removal in version N+1 with a minimum 6-month window between. Each deprecated item should have a documented migration path.

    Coordinating upgrades across ten consuming teams requires communication infrastructure: a changelog that clearly categorises changes by impact, a migration guide for each major version, and office hours where teams can ask questions. Without this, each team discovers breaking changes in their own CI — a frustrating and expensive way to communicate.

    Multi-version support — maintaining v2 while shipping v3 — has real costs: backporting bug fixes to both versions, maintaining two sets of documentation, and the cognitive overhead of support teams knowing which version a consumer is on. Multi-version support is worth it when the migration cost for a major version is high enough that teams need 12+ months of runway. For most design systems, maintaining two active major versions is the practical maximum.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      You are planning the 3.0 release of your design system. The changes include: renaming the `variant` prop to `intent` on the Button component (breaking), adding a new `Skeleton` loading component (additive), fixing a colour contrast bug in the Alert component (bugfix), removing the deprecated `LegacyModal` component (breaking), and updating the Button's default size from `md` to `sm` (potentially breaking).

      Determine the semver bump for each change type, explain which changes are breaking and why, and describe how you would communicate the 3.0 release to consuming teams.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [major, breaking, patch, migration, changelog, guide, announce, deprecation]
      rejectedFeedback: "Walk through each change type and classify it. Note that the default size change is a particularly interesting case — it does not change the prop API but changes the visual output for all existing usages. Consider whether that is breaking."
    hint: "A breaking change is anything that requires a consuming team to change their code or tests after upgrading. Work through each change from a consumer's perspective."
    reflectionPrompt: "What is the difference between a technically breaking change and a practically breaking change? Does the distinction matter for versioning?"
  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      You shipped a 2.0 major version three months ago. You now discover that the Form component has a significant bug that causes data loss in certain edge cases. The bug exists in both 2.0 and 1.x. You have 6 teams on 2.0 and 4 teams still on 1.x.

      What is your patching strategy? Do you release patches for both versions? How do you communicate the severity and urgency? How does this situation affect your case for those four teams to migrate to 2.0?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [backport, patch, communication, urgent, both, migrate, severity, urgency]
      rejectedFeedback: "This is a data loss bug affecting multiple versions — severity matters for communication strategy. Consider: what obligation do you have to the teams on the old version? Does this create an opportunity to accelerate migration?"
    hint: "A data loss bug in a production component is a high-severity incident, not just a maintenance task. How does incident-level communication differ from normal release communication?"
    reflectionPrompt: "What is your obligation to consumers who are still on old versions? Does the reason they haven't upgraded affect your answer?"
  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Your design system is on version 2.5. You want to make a significant API redesign in 3.0 that will require every consuming team to update their code. Estimates suggest the migration will take 2-5 days per team, and you have 12 consuming teams. The redesign would significantly improve the developer experience and enable several new capabilities.

      Design the upgrade coordination strategy. How do you minimise the aggregate pain across all 12 teams? What tooling, documentation, and support structures would you put in place? How do you sequence the rollout?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [codemods, guide, pilot, sequence, support, office hours, tooling, coordination]
      rejectedFeedback: "Strong responses invest heavily in the migration tooling and support infrastructure, not just the documentation. Codemods that automate the mechanical parts of migration are high-leverage. Consider also the sequencing: which teams go first and why?"
    hint: "What is the most expensive part of the migration for consuming teams? Can you automate it? Can you pair with early adopters to refine the process before everyone else has to go through it?"
    reflectionPrompt: "What have been your experiences with major library upgrades that required significant migration effort? What made the process better or worse?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A design system changes the Button component's default `size` from `md` to `sm` without changing the prop API. This change should be versioned as:"
    options:
      - "A patch release — no props were changed"
      - "A minor release — it is additive"
      - "A major release — it changes visual output for all existing usages without code changes"
      - "No version bump needed — it is a design decision, not a code change"
    correctIndex: 2
    feedback: "Visual changes that affect existing usage without any code change are functionally breaking for teams with visual regression tests, and may break user expectations. The semver rule is: if upgrading requires a consumer to review and potentially change something, it is a major version bump."
  - type: MULTIPLE_CHOICE
    question: "The minimum recommended window between deprecating a component and removing it in a design system is:"
    options:
      - "One sprint (2 weeks)"
      - "One quarter (3 months)"
      - "Six months to one year"
      - "Two years to ensure all teams have migrated"
    correctIndex: 2
    feedback: "Six months is a practical minimum that gives teams with release cycles, freezes, and competing priorities time to plan and execute migrations. Shorter windows create pressure that damages trust; much longer windows delay the system's evolution and maintain maintenance burden."
retrieval:
  recall: "What is the rule for determining whether a design system change is a major, minor, or patch version bump?"
  explain: "Describe a complete deprecation process for a component in a design system — from the decision to deprecate through to removal. What does each stage communicate, and to whom?"
  mistakeId:
    code: |
      Anti-pattern: Silent Breaking Changes
      A design system team ships v2.5 which renames several props for consistency.
      The changelog reads "v2.5: Various component improvements and API tidying."
      Consuming teams discover the breaking changes when their CI pipelines fail.
    answer: "Silent or obscured breaking changes are a trust-destroying pattern. Consuming teams experience them as surprises in production pipelines, which creates the impression that the design system is unreliable. Every breaking change must be explicitly documented in the changelog, with the before-and-after API change clearly described and a migration path provided. The changelog is a contract with your consumers."
---

# Hook

I open the Slack message from the Platform Team lead and feel a familiar sinking feeling. "Hey — we just upgraded to your 2.0 and everything's broken. Button size is wrong everywhere, the Modal props don't match the docs, and I can't find a migration guide anywhere." 

We shipped 2.0 three days ago. I know exactly what happened. We renamed three props for consistency, changed a default value, and removed two components we deprecated 8 weeks ago. We wrote it all up in the GitHub release notes. We assumed that was enough.

It wasn't. Teams depend on our system to be predictable. We made it unpredictable. And the cost is not just the debugging time — it is the trust we spent years building, lost in a single upgrade.

# Lore Introduction

The Arcane Academy's Spell Repository has been in use for two centuries. It has been versioned since the first major reform — when Keeper Maeven Thral introduced the Covenant of Stable Names: any spell that consuming scholars depended on must not change its interface without advance notice, a migration path, and a grace period.

The Covenant seems obvious in retrospect. Before it, Guilds would arrive for an annual summit to find that half their apprentice work was broken because the Repository had been "improved." After it, Guilds could plan their own work on a stable foundation, and trust that the Repository's evolution would never ambush them.

The Covenant of Stable Names is the philosophical foundation of what we now call semantic versioning.

# Core Learning

## Concept Introduction

Versioning a design system is a contract with your consumers. Semantic versioning (semver: MAJOR.MINOR.PATCH) provides the vocabulary for that contract:

- **MAJOR** — breaking changes. Existing consuming code must be updated after upgrading.
- **MINOR** — additive changes. New capabilities are added without breaking existing usage.
- **PATCH** — bug fixes and documentation updates. Behaviour improves without any interface changes.

In a design system, the definition of "breaking" is more nuanced than in a standard library. Breaking changes include:
- Renamed, removed, or behaviour-changed props
- Removed components
- Changed default values that affect visual output
- Changed token names or values
- Any change that causes visual regression tests in consuming codebases to fail

## Why It Matters

Predictable versioning is the foundation of consuming team trust. Teams that have been surprised by breaking changes in an unannounced minor release will pin their versions and stop upgrading — which means they stop benefiting from improvements and accumulate a migration debt that grows harder to pay.

The compounding dynamic works in both directions. A system that versions predictably, communicates clearly, and provides migration tooling earns the trust that produces rapid adoption of upgrades. A system that surprises teams produces version pinning and adoption stagnation.

## Worked Examples

**Example 1: Major version communication package**
Version 3.0 ships with: a dedicated migration guide doc (not just release notes), a changelog that separates breaking changes from additions, an automated codemod for the mechanical prop renames, a 30-day migration support window with weekly office hours, and a blog post explaining the design rationale for the changes.

**Example 2: A deprecation timeline**
In version 2.3: `<LegacyModal>` is marked deprecated with a console warning and a docs banner. Migration path to `<Modal>` is documented. In version 2.5 (3 months later): warnings are elevated. In version 3.0 (6 months later): `<LegacyModal>` is removed. Total deprecation window: 9 months.

**Example 3: Codemod for breaking changes**
A codemod script automates renaming `variant="primary"` to `intent="primary"` across a codebase. Teams run one command and get a PR with all mechanical changes. The non-mechanical changes (behaviour differences, visual regressions) are clearly documented separately. The migration time drops from 2 days to 2 hours.

## Common Mistakes

**Treating documentation as sufficient communication.** Changelog entries and docs updates are necessary but not sufficient for breaking changes. Teams need proactive communication — Slack messages, email to team leads, migration guides linked from the component docs.

**Inconsistent semver application.** If your team ships breaking changes in minor versions "just this once" for pragmatic reasons, consuming teams lose the ability to upgrade safely. The semver contract must be consistent or it is worthless.

**Underinvesting in codemods.** Mechanical migrations (prop renames, import path changes) are the most time-consuming and least valuable part of an upgrade for consuming teams. Automating them is high-leverage investment that dramatically increases upgrade velocity.

**Multi-version support without limits.** Supporting three or more major versions simultaneously is an enormous maintenance burden. Define explicitly the maximum versions you will support concurrently (usually two) and communicate this clearly so teams know their upgrade runway.

## Mental Model

Think of semver as a **traffic light at an intersection**. Major is red: stop, significant change required. Minor is green: proceed without changes needed. Patch is a speed bump: barely perceptible, continuous improvement. When every change is green regardless of impact, drivers stop trusting the lights — they proceed cautiously through all signals, which defeats the purpose of having lights at all.

## Mini Summary

- Semver for design systems: major for breaking (including visual regressions), minor for additive, patch for fixes
- Deprecation windows should be 6+ months with a documented migration path from day one
- Codemods automate mechanical migrations and dramatically increase upgrade velocity
- The versioning contract is only worth keeping if it is kept consistently — no silent breaking changes
- Limit concurrent major version support to manage maintenance burden

# Guided Practice Quest

Work through the three guided steps in sequence. Each asks you to reason about the practical and communicative challenges of design system versioning.

# Solo Practice Quest

Write a "Versioning and Release Policy" for a design system (approximately 250 words). Cover: the semver classification rules (with examples specific to design systems), the deprecation policy (with timelines), the release communication process, the support policy for older versions, and the codemod expectation for major migrations. Write as a policy document that consuming team leads would use to plan their upgrade strategy.

# Integration

**Psychology:** The psychological concept of "expectation violation" explains much of the damage caused by unannounced breaking changes. When people develop a mental model (the system is stable when I upgrade minor versions), violations of that model are jarring and disproportionately damaging to trust. The severity of the trust damage is inversely proportional to how well the violation was communicated in advance. Surprise breaking changes are not just inconvenient — they violate the trust contract at a psychological level.

**Sociology:** Contract theory in sociology and law treats explicit agreements as different in kind from implicit agreements — explicit contracts create obligations that can be enforced and expected. Semver is an explicit contract. When a design system team violates it (intentionally or by accident), consuming teams experience it as a breach of contract, not just a mistake. The remedy — transparent acknowledgment, apology, and process improvement — mirrors contractual remedy.

**Philosophy:** The philosopher W.D. Ross identified "promise keeping" as one of several prima facie duties — obligations that hold unless overridden by stronger competing obligations. The versioning contract is a promise. Breaking it requires a reason good enough to override the obligation — and even then, the break should be acknowledged as a breach with appropriate remediation.

# Lore Conclusion

The Covenant of Stable Names was challenged once, in the Fifth Era, when a group of Keepers argued that it was slowing the Repository's evolution. They wanted to break the Covenant for a single major reform they believed would benefit everyone.

The Grand Council refused. "The Covenant is not valuable because it is always convenient," said the Archmage who chaired the hearing. "It is valuable because it is always kept. A covenant that bends when it becomes inconvenient is not a covenant at all. It is a preference."

The reform was delayed by a year, completed properly, and adopted by all Guilds within two seasons of its release.

---
