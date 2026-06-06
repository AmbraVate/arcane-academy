---
id: fe-lea-m2-04
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m2
moduleTitle: "Module 2: Design System Governance"
moduleGlyph: "🏛️"
moduleSortOrder: 2
topicSlug: change_management
topicTitle: "Change Management"
topicSortOrder: 4
lesson: change_management
title: "Change Management"
sortOrder: 1
difficulty: 4
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, sociology, philosophy]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Articulates the difference between technical change (breaking vs non-breaking) and organisational change (adoption friction)
    - Describes a deprecation strategy that gives consuming teams sufficient runway and clear guidance
    - Explains how to communicate design system changes across a large organisation
    - Addresses the sociotechnical dimension — why teams resist updates even when technically superior
    - Proposes measurable indicators of successful change management
  keywords:
    - deprecation
    - migration guide
    - changelog
    - semver
    - consuming team
    - breaking change
    - codemods
    - communication
    - office hours
    - friction
    - resistance
    - adoption
    - runway
    - backward compatible
  modelAnswer: |
    Design system change management operates at two levels: technical (how to introduce changes without breaking consumers) and organisational (how to move teams to new patterns without coercion or abandonment).

    Technical change management uses semver as the contract: patches fix bugs, minors add features backward-compatibly, majors introduce breaking changes. The discipline is minimising majors. For every planned breaking change, ask: can we introduce the new API while keeping the old one (adding, not replacing)? Can we provide a deprecation warning that runs for two major versions before removal? Can we write a codemod that automates the migration? Major bumps that require consuming teams to rewrite components are the highest-friction events in a design system's lifecycle. They should be rare, well-telegraphed, and well-supported.

    The organisational dimension is harder. Teams resist design system updates for rational reasons: the update requires engineering time they don't have, the new API doesn't quite fit their use case, they've built workarounds that the new version would break. Understanding this resistance is empathetic engineering — the design system team must treat consuming teams as users with real constraints.

    Communication strategy: automated changelog generation (commit conventions → changelog), migration guides with before/after examples, office hours for the migration period, a migration tracker showing team progress. The goal is making the upgrade path so well-documented that a mid-level engineer can complete it without support.

    Success indicators: time from release to 80% adoption across consuming teams, number of support requests during migration, proportion of migrations completed without design system team involvement.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      A design system is planning to rename its primary Button props from `type='submit'` (conflicts with HTML's type attribute) to `intent='submit'`. This affects 340 usages across 15 teams. How do you manage this migration? Describe your complete strategy — from the decision to remove the old prop to the last team completing the migration.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [deprecation, migration, codemod, changelog, warning, runway, period, announce, guide, teams]
      rejectedFeedback: "A complete migration strategy includes: (1) deprecation period — keep type and add intent, warn on type usage in development, (2) write a codemod that automatically renames type to intent across codebases, (3) communicate via changelog, email, and Slack before the major release, (4) provide a migration guide with before/after examples, (5) run office hours during the migration window, (6) track which teams have completed migration, (7) remove type only after adoption reaches >95% or after an agreed end-of-support date."
    hint: "How long should you support both the old and new API? Who runs the migration for teams that can't do it themselves?"
    reflectionPrompt: "The codemod is often more valuable than the migration guide. An automated tool that makes the change correctly in seconds is worth weeks of documentation. When planning breaking changes, always ask: can we write a codemod for this?"

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Three months into a migration to a new design system version, three teams haven't started. Their engineering leads say they "don't have bandwidth." The old version will be unsupported in 6 weeks. What do you do? What should you have done differently from the start?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [runway, deadline, codemod, pair, extension, communication, earlier, notice, dependencies, blocker]
      rejectedFeedback: "Immediate options: (1) extend the deadline if the business impact of 3 teams being on old code is low, (2) offer to pair with each team for a 2-hour migration session — often the actual change is fast, the 'no bandwidth' is uncertainty, (3) run the codemod for them and raise the PR — removing the friction of starting. Systemic fix: the migration runways were too short, or the 'end of support' date wasn't real enough to prioritise. Better practice: announce breaking changes 3-6 months in advance, create a migration tracker visible to leadership, make the business impact of staying on old versions concrete."
    hint: "Sometimes the most effective thing is to just do the migration for the team — a PR takes less time than negotiating bandwidth."
    reflectionPrompt: "The hardest lesson in design system change management: consuming teams will always have 'higher priority' work. If the migration is truly important, make it easy enough that it's no longer a bandwidth question — make it a 10-minute PR."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A senior engineer in a consuming team says: "Your redesigned Modal is missing three features we rely on. We'll have to build our own or stay on the old version." How do you respond, and what does this situation tell you about your change management process?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [listen, contribution, RFC, feedback, feature, parity, gap, process, earlier, alpha, beta, discovery]
      rejectedFeedback: "Immediate response: understand which three features are missing — are they use-case gaps (should be in the design system) or implementation details (work differently in the new version but achieve the same goal)? If real gaps: add them before the migration deadline, or provide a clear roadmap. If workarounds exist: document them in the migration guide. Systemic: this team should have been consulted during the design of the new Modal. Change management starts before the change — alpha releases, RFC periods, and consuming team involvement surface gaps before 340 usages are affected."
    hint: "The best time to discover missing features is before the major release, not after. What process change would have revealed this earlier?"
    reflectionPrompt: "An RFC (Request for Comments) period before major API changes — even 2 weeks — frequently surfaces use cases the design system team hadn't considered. One PR review from a consuming team is worth months of trying to add features to a released version."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A codemod for a major API migration reduces a consuming team's migration effort from 8 hours to 15 minutes. What does this illustrate about breaking change strategy?"
    options:
      - "Codemods make breaking changes acceptable — use them freely"
      - "The majority of migration cost is mechanical work that can be automated; reducing this friction dramatically increases adoption"
      - "Teams should never need to change their usage — all changes should be backward compatible"
      - "Codemod maintenance is the design system team's responsibility forever"
    correctIndex: 1
    feedback: "The friction of a breaking change is mostly mechanical (finding usages, making the rename). Codemods automate the mechanical part, leaving only the cases too complex to automate — typically <5% of usages. This changes the migration from a days-long project to a review of automated changes. Adoption improves proportionally because the cost drops."

  - type: MULTIPLE_CHOICE
    question: "A consuming team builds a wrapper around a design system component to add features the design system doesn't have. 6 months later, the design system adds those features. What should have happened instead?"
    options:
      - "The wrapper is fine — teams should extend the design system for their needs"
      - "The team should have contributed the feature to the design system or proposed it via RFC before building the wrapper"
      - "The design system should prohibit wrappers"
      - "The wrapper should be rewritten to use the new design system feature immediately"
    correctIndex: 1
    feedback: "Wrappers create divergence. The correct process: when a team needs a feature the design system doesn't have, they contribute it (via PR) or propose it (via RFC/issue). This builds the feature for all teams, not just one. Governance must create a clear, low-friction contribution path — otherwise teams default to wrappers, which become unmaintained forks."

retrieval:
  recall: "Explain the difference between a deprecation warning and a breaking change. Why is a long deprecation runway important?"
  explain: "A design system team is planning 3 major releases in one year. What would you advise them about the organisational impact of this cadence?"
  mistakeId:
    code: |
      # Design system major release process
      # v2.0.0 released
      # Breaking changes: 47 components renamed, 23 props removed
      # Migration guide: "See the CHANGELOG for details"
      # Support window: old version deprecated immediately
      # Timeline: teams have 4 weeks to migrate
    answer: "Every aspect of this approach maximises friction and minimises adoption. 47 renamed components and 23 removed props is an extreme number of simultaneous breaking changes — batch them into smaller releases. 'See the CHANGELOG' is not a migration guide — write before/after examples for each change. Deprecating the old version immediately removes the safety net — support both for at least 6 months. 4 weeks is insufficient for large organisations — 3-6 months minimum. Better: release changes incrementally (multiple minors before a single focused major), write codemods for mechanical changes, provide migration guides with examples, run a 6-month support window, and track team progress."
---

# Hook

Your design system's v3.0 release drops on Monday. By Friday, you have 12 Slack messages from teams asking about migration. Two teams have abandoned the migration and forked the old version. One senior engineer has written a public blog post criticising the migration experience.

The components are excellent. The migration experience was not designed.

Change management is as important as the change itself.

# Lore Introduction

A new Archmage was appointed to the Academy. She issued new standards for spell documentation — clearer, more structured, better for apprentices. But she announced the change on a Tuesday, required compliance by Friday, provided no templates, and deprecated the old format immediately.

The spellbooks were excellent. The transition was chaos. Half the Guild reverted to the old format.

*"The best standard,"* her predecessor noted, *"is the one that gets adopted. The format must be right. The migration must be supported. Both."*

# Core Learning

## Concept Introduction

Design system change management operates at two levels:

**Technical change management:**
- Semver as the change contract (breaking = major, additive = minor, fix = patch)
- Deprecation warnings before removal (at minimum one major version's notice)
- Codemods for mechanical migrations (the codemod is often more valuable than the migration guide)
- Alpha/beta releases to surface gaps before GA

**Organisational change management:**
- Long runways — announce breaking changes 3-6 months before the deadline
- Migration guides with before/after examples, not just API documentation
- Office hours during migration windows
- Migration trackers visible to engineering leadership
- Contribution process for consuming teams to add missing features before migrating

## Why It Matters

A technically superior new version that 30% of teams adopt is worse than a technically inferior version that 90% of teams use. Consistency across a product suite depends on adoption. Change management is the engineering discipline that converts a release into adoption.

## Worked Examples

**Example 1: A Good Breaking Change Process**
- 4 months before release: RFC period — consuming teams review the proposed API
- 3 months before release: alpha release — early adopters find gaps
- 2 months before release: codemod available for all mechanical changes
- 1 month before release: GA + migration guide + office hours begin
- 6 months post-release: old version reaches end-of-support
- 12 months post-release: old version removed from registry

**Example 2: The Contribution Model**
A team needs a feature the design system doesn't have. Options:
1. **Best:** Contribute the feature via PR — benefits all teams
2. **Acceptable:** File an RFC — design system team adds it with input
3. **Last resort:** Build a local wrapper — creates divergence, gets stale

**Example 3: Migration Tracker**
```
Component migration tracker:
Team           | v2 Components | Migration Status
---------------|---------------|----------------
Checkout       | 8/8 complete  | ✅ Done
Auth           | 4/8 complete  | 🔄 In progress
Profile        | 0/8 complete  | ❌ Not started (blocker: Modal feature gap)
```
Visible to engineering leadership — creates accountability without coercion.

## Common Mistakes

- **Too many simultaneous breaking changes.** Each breaking change has adoption cost. Batch them into one release only when necessary.
- **No codemod.** 90% of migrations are mechanical. A codemod turns a day into 15 minutes.
- **No RFC period.** Features get finalised without consuming team input. Gap discovered post-release. Teams build wrappers.
- **Immediate deprecation.** Consuming teams have no runway. They fork instead of migrating.

## Mental Model

Treat the migration as a product. Consuming teams are users. What would you do if 15% of users of your product refused to upgrade? You'd improve the upgrade experience. Same logic applies to design system migrations.

## Mini Summary

- Treat change management as a product discipline — the migration experience is as important as the change itself
- Semver + deprecation warnings + codemods are the technical tools
- RFC periods, long runways, migration guides, and office hours are the organisational tools
- Migration trackers create visibility without micromanagement
- Measure success by adoption rate and time to 80% adoption, not by release date

# Guided Practice Quest

Work through the three guided steps, each presenting a real change management scenario. Your responses should demonstrate both technical and organisational thinking.

# Solo Practice Quest

You are leading the design system team. You've identified that the current Button component needs a complete API rework — the existing prop names are confusing, the variant model is inconsistent with the rest of the system, and it lacks accessibility features that 40% of teams have asked for. 23 teams use the current Button, totalling approximately 800 usages. Design your complete change management plan from today through to full adoption. Include: technical strategy, communication plan, timeline, migration support, and how you'll know if it worked.

# Integration

Design system change management connects to organisational psychology's research on change resistance (Lewin's change model, Kotter's 8-step change process) — people resist change not because the change is bad, but because the cost of changing exceeds the perceived benefit. Reducing migration friction (codemods, clear guides, long runways) directly reduces the cost side of the equation, making adoption rational. Sociology of technology studies how standards propagate through communities of practice — adoption accelerates through social proof (early adopters), social pressure (migration trackers), and network effects (once most teams are on the new version, the remaining teams face increasing interoperability pressure). The philosophical dimension is contractual: a major semver bump is a broken promise to consuming teams. Each breaking change should be weighed not only by technical necessity but by the trust it costs with the engineering community that depends on the design system.

# Lore Conclusion

The Academy's documentation transition was re-run with a different approach: 4 months' notice, before/after examples for every change, a scripted conversion tool, and a support period where the old format was still accepted. Three months in: 89% adoption. The remaining 11% received one-on-one support. Six months in: 100% adoption.

*"The standard is only as good as its adoption,"* the Archmage's successor concluded. *"The best format in the world, abandoned by half the Guild, is worse than an imperfect one everyone uses."*

---
