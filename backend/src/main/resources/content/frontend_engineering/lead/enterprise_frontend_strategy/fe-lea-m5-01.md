---
id: fe-lea-m5-01
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m5
moduleTitle: "Module 5: Strategic Frontend Architecture"
moduleGlyph: "🏛️"
moduleSortOrder: 5
topicSlug: enterprise_frontend_strategy
topicTitle: "Enterprise Frontend Strategy"
topicSortOrder: 1
lesson: enterprise_frontend_strategy
title: "Enterprise Frontend Strategy"
sortOrder: 1
difficulty: 5
estimatedMinutes: 45
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-lea-m4-04]
integrationDomains: [software_engineering, psychology, economics]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Correctly explains the trade-offs between a single-SPA, micro-frontend, and multi-app frontend architecture
    - Applies Conway's Law to explain how team structure influences architectural decisions
    - Identifies the governance challenges of enterprise frontend at scale (design consistency, dependency management, cross-team coordination)
    - Proposes a frontend strategy for a multi-team organisation that addresses consistency, autonomy, and velocity
    - Demonstrates understanding of the role of a design system and shared platform in enterprise frontend
  keywords:
    - micro-frontend
    - monorepo
    - design system
    - Conway's Law
    - federation
    - governance
    - autonomy
    - consistency
    - platform team
    - cross-team
    - dependency
    - versioning
    - shared infrastructure
    - team topology
    - velocity
  modelAnswer: |
    Enterprise frontend strategy is fundamentally a people and organisation problem that manifests as a technical one. Conway's Law states that organisations design systems that mirror their communication structure. A company with 4 product teams building into a single frontend monolith will create a bottleneck at every shared component change. A company that fragments its frontend across 4 completely independent apps will create inconsistency, duplicated effort, and a fractured user experience.

    Architecture options: (1) Single SPA (monolith): all teams work in one codebase. Easy consistency, shared infrastructure, but deployments are coupled and teams block each other. Works well for small organisations (<3 teams); degrades at scale. (2) Micro-frontends: each team owns an independently deployable frontend module, composed at runtime or build time. Enables team autonomy and independent deployment; requires significant platform investment to maintain consistency and composition. (3) Multi-app (separate deployments): each product is a separate application. Maximum autonomy; user experience consistency and shared login/navigation require explicit architecture work. (4) Monorepo with independently deployable packages: multiple apps in one repo with shared tooling; teams own their apps but share dependencies through a managed monorepo.

    Conway's Law application: if the organisation has 4 independent product teams, the architecture should match — each team with its own deployable frontend, with shared platform (design system, authentication, analytics, error tracking) managed by a platform team. Forcing 4 independent teams into one codebase creates organisational friction that manifests as slow deployment pipelines, high PR conflict rates, and blocked releases.

    Design system role: the design system is the consistency layer across all frontend teams. It is owned by a platform team (not by product teams, who would deprioritise it under feature pressure). It enforces visual and interaction standards through code — components, tokens, and documented patterns — rather than through process. A design system without a dedicated platform team is a design system in decay.

    Governance: dependency versioning across teams is the most persistent governance challenge. A shared component library at v3.2 used by 6 teams becomes extremely hard to update — one team's migration blocks the library from being updated for all others. Semantic versioning, deprecation policies, and automated migration tooling (codemods) are the engineering solutions.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      A company has 5 product teams, all working in a single React monolith. The main complaints are: PRs take 3-5 days to review, deployments require coordinating all 5 teams, and one team's bug can block all teams from releasing. Apply Conway's Law to explain the root cause and propose an architectural strategy that addresses it.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [Conway, team, independent, deploy, micro, monorepo, autonomy, coordination, structure, decouple]
      rejectedFeedback: "Conway's Law: the system mirrors the organisation. 5 independent teams coupled into one deployment unit creates organisational tension — each team needs autonomy to ship, but they are coupled at the release level. Root cause: the architecture (monolith deployment) does not match the organisation (independent teams). Solutions: (1) Micro-frontend architecture — each team owns an independently deployable module. Composition happens at the shell level (a host app that mounts team modules). Teams deploy independently; one bug does not block others. (2) Monorepo with independently deployable apps — teams share tooling and can share components through a managed library, but each team has its own deployment pipeline. A failing deploy in Team A does not block Team B. The design system and shared infrastructure become explicit platform products owned by a platform team, not incidentally shared through the monolith. The key insight: the problem is not a code problem — it is a deployment coupling problem caused by organisation-architecture mismatch."
    hint: "Conway's Law predicts that a 5-team organisation forced into one deployment unit will experience coordination costs at the deployment boundary. The fix is architectural independence."
    reflectionPrompt: "The slowness is not laziness or poor process — it is the natural result of forcing an organisational structure onto an incompatible architecture. Fix the architecture to match the organisation."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Your company is starting a design system to serve 4 product teams. A product manager suggests "each team contributes components to the shared library and maintains their section." Why is this governance model likely to fail, and what governance model would you propose instead?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [platform, dedicated, team, ownership, product, pressure, priority, decay, governance, maintain]
      rejectedFeedback: "Why it fails: product teams are under constant feature pressure — their OKRs are product outcomes (conversion, retention, feature delivery), not design system quality. When there is a trade-off between shipping a feature and updating a shared component, product pressure wins every time. A design system maintained by product teams as a side responsibility will decay: components will diverge, documentation will be outdated, breaking changes will be introduced without coordination, and migration support will not exist. The result is a design system that exists in name but is ignored in practice. Governance model that works: (1) Dedicated platform team with design system as their primary product, measured on adoption rate, consumer satisfaction, and update velocity. (2) Product teams as consumers — they file issues, request components, and adopt updates on a defined migration schedule. (3) A steering committee (one representative per product team) that prioritises platform team work based on cross-team needs. (4) SLAs: new component requests responded to within X days; deprecation notices give Y weeks before breaking changes. The platform team treats product teams as their users — with the same attention to DX (developer experience) that product teams give to UX."
    hint: "Shared infrastructure maintained 'by everyone' is maintained by no one. Ownership requires dedicated responsibility and aligned incentives."
    reflectionPrompt: "A design system is a product with its own users (engineers and designers). It needs an owner with the autonomy and mandate to treat it as a first-class product."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A new product team at your company wants to use a different frontend framework (Vue instead of React) to build their section of the product. What are the legitimate arguments for and against allowing this, and what decision framework would you apply?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [framework, consistent, hiring, maintenance, skill, cost, autonomy, boundary, benefit, justify]
      rejectedFeedback: "Arguments for: micro-frontend architecture already decouples deployment — if the team's module is integrated via a framework-agnostic contract (web components, iframe, or module federation), the choice of internal framework does not affect other teams. The team may have specific Vue expertise that produces faster delivery and better quality than React. Technology diversity can be a hedge against ecosystem risk. Arguments against: (1) Hiring complexity — the organisation now needs to hire for two frontend stacks; (2) Shared infrastructure cost — CI/CD, linting, testing frameworks, and onboarding materials must support both stacks; (3) Cross-team movement becomes harder — engineers cannot easily move between teams; (4) The design system must now have Vue and React implementations (or be framework-agnostic web components); (5) Cognitive overhead for the platform team maintaining shared infrastructure. Decision framework: (1) Is there a clear, specific technical benefit that cannot be achieved in the existing stack? (2) Does the team have the expertise to maintain a divergent stack long-term? (3) Are the integration boundaries clean enough that the framework choice does not leak? (4) Is the platform team willing and able to extend shared infrastructure? If all four are yes, allow it with explicit costs documented. If any are no, the benefit is probably not worth the complexity."
    hint: "Micro-frontend architecture makes framework diversity technically possible — but possible and advisable are different questions."
    reflectionPrompt: "Technology diversity is a trade-off, not a principle. Evaluate it as a trade-off: specific benefit vs specific organisational cost."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Conway's Law states that:"
    options:
      - "Large systems are always more complex than small systems"
      - "Organisations design systems that mirror their communication structure"
      - "Shared codebases lead to better collaboration"
      - "Technology choices should follow team preferences"
    correctIndex: 1
    tier: RECALL
    feedback: "Conway's Law (Melvin Conway, 1967): 'Organisations which design systems are constrained to produce designs which are copies of the communication structures of these organisations.' The inverse (Inverse Conway Manoeuvre) is increasingly used deliberately: design the team structure to match the desired architecture. Want a micro-frontend architecture? Create independent product teams. Want a modular monolith? Create cross-functional feature teams with shared deployment ownership."

  - type: MULTIPLE_CHOICE
    question: "The primary risk of a design system maintained by product teams as a shared responsibility is:"
    options:
      - "The design system will have too many components"
      - "Product teams will deprioritise design system maintenance under feature pressure, leading to decay and inconsistency"
      - "The design system will be over-engineered"
      - "Product teams will create too many breaking changes"
    correctIndex: 1
    tier: APPLICATION
    feedback: "Product teams optimise for their own OKRs — feature delivery, conversion, retention. Design system maintenance does not appear in these OKRs, so it loses every time there is a trade-off. The result: components diverge between teams (each makes local copies with local modifications), documentation goes stale, migration support is non-existent. A design system needs a dedicated owner whose OKRs are design system quality, adoption rate, and consumer satisfaction."

retrieval:
  recall: "What is Conway's Law? Give one example of how an organisational structure has directly shaped an architecture decision you have seen or can imagine."
  explain: "A CEO asks: 'Should we adopt micro-frontends?' What questions would you ask before recommending for or against?"
  mistakeId:
    code: |
      // Enterprise frontend architecture decision
      "We have 8 product teams. We will build everything in a single 
       Next.js application in a monorepo. All teams share one 
       deployment pipeline. PR reviews are done by a central 
       frontend architecture committee."
    answer: "Architecture-organisation mismatch: 8 independent teams sharing one deployment pipeline and a central review committee will create predictable bottlenecks. A central committee reviewing all PRs becomes a constraint that slows all 8 teams; one team's failed deploy blocks all others; cross-team conflicts in shared files create high PR conflict rates. The architecture should match the organisation: 8 teams with 8 independently deployable units (or logically grouped by domain), a platform team owning shared infrastructure, and PR reviews owned by the team whose code is being changed — with cross-team components reviewed by the platform team. A central architecture committee for standards and guidance is appropriate; a central committee for all PR approvals is a bottleneck that will strangle velocity."
---

# Hook

Eight product teams. One codebase. One deployment pipeline. Deployments require all eight teams to coordinate.

Within six months: PRs take a week to review. Releases happen once a month. Every deploy is a high-risk event involving everyone.

The problem is not process. It is architecture-organisation mismatch. And the solution is not faster processes — it is aligning the architecture to the organisation.

# Lore Introduction

*"The Grand Academy grew,"* the senior architect explains. *"Five faculties became twelve. But the library remained one: one catalogue, one checkout system, one head librarian whose approval was needed for any change."*

*"By the time there were twelve faculties, the head librarian approved 400 requests per week. Nothing moved. The library became the constraint that stopped the entire Academy from learning."*

*"The solution was not a faster librarian. It was twelve distributed libraries with a shared catalogue — each faculty owning their section, coordinating through documented standards rather than through a single approval gate."*

This is enterprise frontend strategy.

# Core Learning

## Concept Introduction

### Architecture Patterns at Scale

| Pattern | Teams | Tradeoffs |
|---|---|---|
| **Single SPA (monolith)** | 1-3 | Simple, consistent; deployment coupling kills velocity at scale |
| **Micro-frontends** | 4+ | Team autonomy, independent deploys; requires platform investment for consistency |
| **Multi-app** | Any (separate products) | Maximum autonomy; user experience continuity requires explicit work |
| **Monorepo with independent apps** | 4+ | Shared tooling; team deployment independence; requires monorepo tooling investment |

### Conway's Law Applied

Design the architecture to match the desired team structure (Inverse Conway Manoeuvre):
- **Independent product teams** → independently deployable frontend units (micro-frontends or separate apps)
- **Platform team** → shared infrastructure (design system, auth, analytics, error tracking, CI/CD)
- **Domain teams** → domain-scoped codebases with shared boundaries through APIs and published packages

### Design System Governance

A design system is a product with users (engineers and designers). It requires:
- **Dedicated ownership:** A platform team with design system as primary responsibility, not a side project
- **Published contracts:** Versioned APIs, deprecation notices, migration guides
- **Contribution process:** Product teams file issues and requests; platform team prioritises and builds
- **Adoption metrics:** Track which teams are on which version; drive migrations actively

### Dependency Management at Scale

The hardest governance problem in enterprise frontend: a shared library at version 3 used by 8 teams becomes impossible to update.

Solutions:
- **Semantic versioning:** Major/minor/patch with clear compatibility guarantees
- **Deprecation policy:** Features deprecated for X releases before removal
- **Automated migration tooling:** Codemods that auto-update consumers
- **Strangler Fig pattern:** Run old and new APIs in parallel; migrate incrementally

## Common Mistakes

- **Monolith + growing teams.** A single deployment unit with 8+ teams is an organisational constraint, not just a technical one.
- **Design system without a dedicated team.** Product teams deprioritise shared infrastructure; it decays.
- **Technology diversity without discipline.** Multiple frameworks are possible with micro-frontends; they multiply infrastructure and hiring costs.

## Mini Summary

- Conway's Law: architecture mirrors organisation; use the Inverse Conway Manoeuvre deliberately
- Scale the architecture to match team count: monolith for small teams, micro-frontends or multi-app for large organisations
- A design system needs a dedicated platform team; product team ownership leads to decay
- Dependency management at scale requires semantic versioning, deprecation policies, and migration tooling

# Guided Practice Quest

Apply Conway's Law to a 5-team monolith problem, design design system governance, and evaluate a framework diversity request.

# Solo Practice Quest

You are the lead architect for a company that has grown from 2 to 12 product teams over 18 months. The current architecture is a single React monolith with a shared UI library, one deployment pipeline, and no dedicated platform team. Releases are monthly. Define a 12-month architectural evolution plan: the target architecture, the migration strategy, the team structure changes required, and the governance model for the shared design system.

# Integration

Team Topologies (Skelton and Pais) provides the organisational framework that complements technical micro-frontend strategy: Stream-aligned teams own user-facing products end-to-end; Platform teams reduce cognitive load for stream teams by providing self-service internal products; Enabling teams help stream teams adopt new capabilities; Complicated subsystem teams own technically complex components. Applied to frontend: stream-aligned teams own their product frontends; a platform team owns the design system, shared infrastructure, and tooling; enabling teams help product teams adopt new testing frameworks or accessibility standards. The theory of cognitive load (Skelton/Pais) argues that inter-team dependencies create cognitive load that reduces team effectiveness — the primary benefit of micro-frontend architecture is not technical but cognitive: teams can reason about their own bounded context without holding the entire system in mind.

# Lore Conclusion

*"Twelve distributed libraries,"* the architect concludes. *"Each faculty owns their section. The shared catalogue is maintained by a dedicated archive team — their only responsibility. Standards are documented; changes are coordinated through the catalogue; each faculty deploys their acquisitions independently."*

*"The head librarian who approved 400 requests per week now sets standards for the whole system. One person with one job, well-matched to their role."*

*"The Academy moves faster now. Not because the librarian is faster. Because the architecture respects how twelve independent faculties actually work."*

---
