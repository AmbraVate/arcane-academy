---
id: fe-lea-m6-01
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m6
moduleTitle: "Capstone Quest"
moduleGlyph: "👑"
moduleSortOrder: 6
topicSlug: capstone
topicTitle: "Capstone"
topicSortOrder: 1
lesson: capstone
title: "The Arcane Frontend Architect"
sortOrder: 1
difficulty: 10
estimatedMinutes: 1440
xpReward: 2000
practiceType: NONE
questType: MASTERY
retrievalWeight: high
questTypes: [solo]
prerequisites: [fe-lea-m5-04]
integrationDomains: [software_engineering, psychology, economics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Demonstrates mastery of performance engineering by diagnosing and solving a realistic Core Web Vitals problem with measurable evidence
    - Applies UX psychology principles (cognitive load, decision-making biases, Fogg Behaviour Model) to a concrete design critique and redesign proposal
    - Produces a complete enterprise frontend strategy for a multi-team organisation using Conway's Law, team topologies, and design system governance
    - Evaluates a technology adoption decision using a structured framework with total cost of ownership analysis and a designed PoC
    - Defines a long-term maintainability programme with measurable metrics, fitness functions, and a dependency health process
    - Synthesises product thinking — North Star Metric, A/B test validity, and trade-off communication — into a coherent leadership recommendation
    - Communicates all decisions in language appropriate for non-technical stakeholders alongside technical justifications
  keywords:
    - Core Web Vitals
    - LCP
    - CLS
    - cognitive load
    - Fogg Behaviour Model
    - Conway's Law
    - micro-frontend
    - design system
    - maintainability
    - fitness function
    - ADR
    - bus factor
    - technology evaluation
    - PoC
    - North Star Metric
    - A/B testing
    - trade-off
    - platform thinking
    - paved road
    - accessibility
  modelAnswer: |
    The capstone requires synthesis across five domains: performance, UX psychology, enterprise architecture, technology evaluation, and maintainability. A strong response demonstrates not just recall of individual concepts but the ability to reason across them — choosing the right tool for each problem rather than applying learned patterns uniformly.

    Performance: LCP problems trace to render-blocking resources, server response time, or large unoptimised images. The diagnosis path is Lighthouse CI → identify the LCP element → trace its loading path → instrument the specific bottleneck. Solutions are specific to cause: image LCP → next/image or lazy loading; font LCP → font-display: swap and preconnect; API-dependent LCP → streaming or skeleton screens.

    UX psychology: a good critique names the specific bias or principle being violated (not just "this is confusing"), proposes a concrete change, and explains the psychological mechanism. The Fogg model (B=MAP) predicts that a behaviour will not occur if motivation or ability is below threshold — so reducing friction (ability) and increasing immediate triggers are the levers, not just better copy.

    Enterprise architecture: the right architecture matches the organisation's communication structure (Conway's Law). A 10-team organisation in a monolith will experience coordination costs. The answer is not always micro-frontends — a monorepo with independently deployable apps may be simpler. The design system must have a dedicated owner; shared responsibility produces decay.

    Technology evaluation: the key discipline is defining the problem before choosing the tool, running a PoC with a specific question and exit criteria, and calculating total cost of ownership including migration risk and hiring impact.

    Maintainability: the measurable programme includes: onboarding time to first commit (the most honest maintainability measure), fitness functions in CI (no circular dependencies, max component lines, no cross-feature imports), ADRs for all significant decisions, dependency health via Renovate, bus factor improvement through documented knowledge distribution.
---

# The Arcane Frontend Architect

## The Final Trial

*"You have studied the arcane arts of the frontend,"* the Grand Architect announces to the assembled candidates. *"Performance, psychology, architecture, strategy, maintainability. Each as a separate discipline."*

*"The final trial does not separate them. The final trial presents a real organisation, with real problems, and asks you to reason across all of it — as a lead engineer does."*

*"The Academy does not award the title of Arcane Frontend Architect for knowing the answers. It awards it for knowing how to find them."*

---

## The Scenario

You have been appointed Lead Frontend Engineer at **Luminary**, a B2B SaaS platform serving 800 enterprise clients. Luminary provides project management tools for knowledge-intensive organisations (law firms, consultancies, research teams).

**Current state:**
- 8 product teams, all working in a 4-year-old React monolith
- A design system maintained by product teams "as a shared responsibility"
- 63 npm packages (last updated 14 months ago)
- Core Web Vitals scores: LCP 4.1s, CLS 0.22, INP 320ms
- No architectural fitness functions, no ADRs, no onboarding documentation
- One engineer (Sarah) who understands the authentication and billing modules
- Monthly releases requiring all 8 teams to coordinate
- A North Star Metric of "Monthly Active Users" that has been growing 12% MoM while revenue is flat

Your first week. The following problems land on your desk.

---

## Part 1: Performance Crisis

A client has threatened to terminate their contract — their users are experiencing a 4.1s LCP on the dashboard page. The business impact: this client represents £180,000 ARR.

**Your task:**
1. Describe your diagnostic process for identifying the LCP root cause. What tools would you use, in what order, and what would you look for at each step?
2. The investigation reveals: the LCP element is a hero image (1.4MB unoptimised PNG), loaded after a bundle.js of 2.8MB (containing three analytics libraries imported globally). Propose a remediation plan with an estimated timeline and explain why each step is prioritised in the order you choose.
3. How do you prevent this from recurring? Define the automated performance gates you would put in CI.

**Minimum:** 400 words covering all three parts with specific tools, techniques, and measurable targets.

---

## Part 2: UX Psychology Audit

The product team wants to increase user activation (percentage of new accounts that complete their first project within 7 days — currently 23%).

They have proposed the following design for the onboarding flow:
```
Screen 1: "Welcome! To get started, please:
  □ Set up your organisation profile
  □ Invite at least 3 team members  
  □ Connect your calendar
  □ Set your notification preferences
  □ Create your first project
  □ Complete your personal profile"
  
  [Get Started →]

Screen 2 (after clicking Get Started):
  "Before you create your first project, you'll need to:
   - Choose your project methodology (Agile, Waterfall, Hybrid, Kanban, OKR, Custom)
   - Configure your workspace settings
   - Set default permissions for team members
   
   Have questions? Read our 128-page Getting Started Guide →"
```

**Your task:**
1. Identify at least four specific UX psychology principles that this design violates. For each, name the principle and explain the specific mechanism by which it reduces activation.
2. Redesign the onboarding flow using the Fogg Behaviour Model (B=MAP). Describe — you do not need to produce visual designs — what a screen-by-screen redesign would look like and why each decision addresses a specific psychology principle.
3. Define the A/B test you would run to validate the redesign. What is your hypothesis, how would you calculate the required sample size, and what would constitute a valid positive result?

**Minimum:** 350 words covering all three parts with specific psychology concepts and a concrete test design.

---

## Part 3: Enterprise Architecture Strategy

The current state (8 teams in a monolith with coordinated monthly releases) has been identified by engineering leadership as a critical constraint on velocity. You have been asked to propose the target architecture.

**Your task:**
1. Apply Conway's Law to explain why the current architecture produces its current symptoms (slow PR review, monthly coordinated releases, blocking bugs). What is the specific architecture-organisation mismatch?
2. Propose a target architecture for 8 independent product teams. You do not need to recommend a specific technology — you need to define the deployment boundaries, the team ownership model, and the transition approach (how you get from here to there without stopping feature development).
3. The design system is currently maintained "by everyone." Define a governance model that will actually work: who owns it, how is work prioritised, how are breaking changes communicated, and how do you measure success?

**Minimum:** 350 words covering all three parts with specific references to team topology concepts.

---

## Part 4: Technology Evaluation

A senior engineer proposes migrating the state management layer from Redux to Zustand, and separately proposes adopting GraphQL for the API layer. Both are presented as "obviously better."

**Your task:**
1. What is wrong with the "obviously better" framing, and what process would you apply to evaluate each proposal on its merits?
2. Perform a total cost of ownership comparison for the Redux → Zustand migration. The codebase has 340 components that use Redux, moderate test coverage (60%), and 6 engineers on the team. What factors would you include, how would you estimate the cost, and what would need to be true for the migration to be worth it?
3. Design a PoC for the GraphQL adoption decision. What specific question would the PoC answer? What would you build? What are the exit criteria?

**Minimum:** 300 words covering all three parts with a specific PoC design and TCO framework.

---

## Part 5: Maintainability Programme

You have 6 months to transform the maintainability of the codebase before you hire three new engineers (who will join with no context on any of the existing code).

**Your task:**
1. Define the measurable baseline you would establish in week 1. What specific numbers would you measure that will let you demonstrate improvement in 6 months?
2. Prioritise and describe your top 5 maintainability interventions for the 6-month period. For each, explain: what problem it addresses, how you implement it, and how you know it has worked.
3. Address the Sarah problem: she is the only engineer who understands authentication and billing — two modules critical to all 8 product teams. Design a knowledge distribution programme for these two modules. What would you have in place before the three new engineers join?

**Minimum:** 350 words covering all three parts with specific practices and measurable outcomes.

---

## Part 6: Product Thinking Synthesis

The CEO has asked for your recommendation on Luminary's frontend strategy for the next 12 months. The current North Star Metric ("MAU growing at 12% while revenue is flat") suggests a measurement problem.

**Your task:**
1. Diagnose why "Monthly Active Users" is an inadequate North Star Metric for Luminary. Propose a better NSM with the leading indicators you would track and the counter-metric you would pair with it.
2. Q3 has three competing engineering priorities: (A) the full architecture migration to independent deployable units — 16 weeks; (B) a new AI-powered document summarisation feature — 12 weeks, high demand from sales; (C) the performance remediation programme — 4 weeks, affects 3 at-risk client accounts. You have 20 weeks of engineering capacity. Write a structured recommendation with explicit trade-off reasoning.
3. Write a one-page summary of your 12-month frontend strategy for the CEO. It should be written for a non-technical reader and cover: the current state diagnosis, the target state, the three most important investments, and how you will measure progress.

**Minimum:** 400 words covering all three parts, with the CEO summary written in non-technical language.

---

## Evaluation

Your response is evaluated on six dimensions:

| Dimension | What is assessed |
|---|---|
| **Technical depth** | Specific tools, techniques, and mechanisms — not vague principles |
| **Cross-domain synthesis** | Connecting decisions across performance, psychology, architecture, maintainability |
| **Measurability** | Concrete metrics, baselines, targets, and test designs |
| **Stakeholder communication** | Technical decisions translated into business language |
| **Trade-off reasoning** | Explicit acknowledgement of costs and alternatives |
| **Prioritisation** | Sequencing decisions intelligently given constraints |

---

*"The Arcane Frontend Architect does not specialise in one discipline and hope the others resolve themselves,"* the Grand Architect concludes. *"They hold the full system in mind — from the milliseconds of a render to the years of an organisation's architecture — and make decisions that serve both."*

*"Write your response. The Academy is listening."*

---
