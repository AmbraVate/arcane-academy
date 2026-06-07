---
id: fe-lea-m5-04
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m5
moduleTitle: "Module 5: Strategic Frontend Architecture"
moduleGlyph: "🏛️"
moduleSortOrder: 5
topicSlug: technology_evaluation
topicTitle: "Technology Evaluation"
topicSortOrder: 4
lesson: technology_evaluation
title: "Technology Evaluation"
sortOrder: 4
difficulty: 5
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-lea-m5-03]
integrationDomains: [software_engineering, economics, philosophy]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Distinguishes between hype-driven adoption and evidence-based evaluation
    - Applies a structured evaluation framework with defined criteria and weightings
    - Addresses total cost of ownership beyond initial build cost (maintenance, hiring, migration risk)
    - Demonstrates how to run a proof of concept that produces actionable decision data
    - Explains how to communicate a technology recommendation to non-technical stakeholders
  keywords:
    - evaluation
    - proof of concept
    - total cost of ownership
    - migration risk
    - ecosystem
    - maturity
    - hiring
    - adoption
    - hype cycle
    - trade-off
    - criteria
    - benchmark
    - lock-in
    - community
    - maintenance
  modelAnswer: |
    Technology evaluation is a structured decision process, not a popularity contest. The failure mode is hype-driven adoption: choosing a technology because it is new, well-marketed, or used by visible companies rather than because it solves a specific problem better than alternatives.

    Evaluation criteria framework: (1) Problem fit — does this technology solve the specific problem better than alternatives? Define the problem first. A technology chosen before the problem is defined is reverse-engineered justification. (2) Ecosystem maturity — how old is the project? What is the release cadence? What is the contributor count? Is it backed by an organisation with a long-term interest in maintaining it? (3) Total cost of ownership — initial implementation is the smallest cost. Ongoing costs: maintenance as the library evolves, breaking changes in major versions, hiring engineers who know it, training engineers who don't, documentation maintenance, community support availability. (4) Migration risk — if this technology fails or is deprecated, what does migration look like? Libraries with narrow, well-defined APIs are easier to swap than frameworks that pervade all code. (5) Team capability — does the team have experience? If not, what is the learning curve and how does that affect delivery timelines?

    Proof of concept design: a PoC answers a specific technical question, not "is this a good library?" Define the decision-critical question upfront ("Can this handle 10,000 concurrent WebSocket connections on our infrastructure?"). Build only what is needed to answer that question. Set a time box (1-2 weeks). Exit criteria: yes/no/maybe with evidence. A PoC that produces "it seems pretty good" is not a PoC — it is exploration without a decision.

    Communication to stakeholders: translate technical criteria into business language. "React vs Vue" becomes "the React choice gives us access to a larger hiring pool (60% of UK frontend developers vs 20%), reduces onboarding time for new engineers, and has a longer established track record — at the cost of higher initial bundle size (80kb vs 30kb), which we can mitigate with code splitting." The stakeholder decision is: which business risks are acceptable given the trade-offs.

    The lock-in question: every technology choice is a bet on the future. The question is not "will this still be the right choice in 5 years?" (unknowable) but "if this turns out to be wrong, what is the cost of migration?" Technologies with narrow integration surface (a date formatting library) are low lock-in. Technologies with deep integration (a state management paradigm, an SSR framework, a component system) are high lock-in and warrant proportionally more evaluation rigour.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      Your team is debating whether to adopt a new React meta-framework (Next.js vs Remix). A junior engineer says "Next.js has more GitHub stars and bigger companies use it." How do you structure the evaluation, and why is the GitHub stars argument insufficient?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [criteria, problem, requirement, fit, SSR, routing, data, team, PoC, specific, use case, evaluate]
      rejectedFeedback: "GitHub stars are a popularity signal, not a fitness signal. A library can be widely adopted for use cases entirely different from yours. Evaluation structure: (1) Define your specific requirements first — do you need server-side rendering? What are your data-fetching patterns? Do you need streaming? Edge deployment? What pages are static vs dynamic? (2) Evaluate both against your requirements, not against each other in the abstract — Next.js and Remix make different architectural bets (Next.js: file-based routing, React Server Components; Remix: form-based mutations, web fundamentals); which bet matches your use case? (3) Assess team familiarity — if the team has strong Next.js experience, switching to Remix incurs a learning cost; the reverse trade-off must be worth it. (4) Run a PoC for the decision-critical question — if your biggest concern is data mutation complexity, build the same form flow in both and compare developer experience. (5) Check ecosystem for your specific needs — third-party integrations, deployment targets, official support for edge runtimes if required. The GitHub stars argument fails because: stars measure discovery and exploration, not production fitness; a framework used at massive scale by many companies may have a very different performance and DX profile at your team size and use case."
    hint: "Stars measure popularity, not fitness. What specific questions would tell you which is the right choice for your use case?"
    reflectionPrompt: "Technology evaluation starts with requirements, not tools. A tool that solves the wrong problem well is worse than a less popular tool that solves the right problem."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Your team wants to adopt a new state management library (Zustand) to replace Redux in a large codebase. Define the total cost of ownership analysis for this migration, and what decision criteria would determine whether the migration is worth it.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [migration, cost, time, team, learning, hiring, existing, codebase, incremental, risk, benefit, worth]
      rejectedFeedback: "Total cost of ownership for a major library migration: (1) Migration cost — how many components use Redux? How deeply is Redux coupled to application logic vs just state management? A rough estimate: multiply component count by average hours per component migration. This is the floor cost. (2) Risk cost — what is the probability of regressions? What is the test coverage of state-dependent components? Low coverage = high risk = higher effective cost. (3) Learning cost — what is the team's Zustand experience? Factor in documentation reading, pair programming, PR review overhead during the transition period. (4) Ongoing benefit — what problem does Zustand solve better? If the answer is 'less boilerplate,' quantify it: developer time saved per feature built × features per year × engineer cost. If the answer is 'performance,' measure the current Redux performance overhead and compare. Decision criteria: migration is worth it if (ongoing benefit per year) > (migration cost ÷ expected years before next migration). If Zustand saves 2 hours per new feature and you build 50 features per year at £100/hour = £10,000/year benefit, and migration costs £25,000, payback is 2.5 years. Is the library likely to still be the right choice in 2.5 years? Incremental migration strategy: the strangler fig pattern — new features use Zustand, existing Redux features migrate opportunistically. This spreads cost and validates the approach before full commitment."
    hint: "Total cost of ownership includes the migration itself, the risk of regressions, the learning curve, and the ongoing maintenance burden — not just 'how long will it take to rewrite.'"
    reflectionPrompt: "A migration that produces a cleaner codebase but costs more engineering time than it saves is not an improvement — it is preference dressed as engineering."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You are evaluating whether to adopt Web Components as the basis for a new component library to replace your React-specific one. The argument is "framework-agnostic, works everywhere." What are the risks this argument underweights, and how would you structure a PoC to produce an actionable decision?
    inputConfig:
      minWords: 40
    markingRule:
      matchMode: CONTAINS
      accepted: [PoC, proof, DX, React, interop, shadow DOM, ecosystem, test, slot, form, specific, question]
      rejectedFeedback: "'Framework-agnostic' is a genuine benefit only if you have multiple frameworks to support. If your organisation uses React exclusively, the interoperability benefit does not apply. Risks underweighted by the argument: (1) DX gap — Web Components have more verbose syntax than React components; no JSX, no hooks, no context; the developer experience for complex interactive components is significantly worse. (2) React interop is imperfect — passing non-primitive props (objects, functions) to Web Components from React requires workarounds; event handling is different; React 19 improved this, but earlier versions have known rough edges. (3) Shadow DOM complexity — while Shadow DOM provides style encapsulation, it creates barriers for theming (CSS custom properties work, but complex cascading does not) and form participation (Web Components as form elements require extra implementation). (4) Testing tooling — Jest/React Testing Library has excellent Web Component support but it requires additional configuration; some patterns are less ergonomic. PoC design — answer the specific critical question: if the team's concern is framework lock-in, the question is 'can we build our five most complex components as Web Components with equivalent DX and no React interop issues?' Build those five. Time-box to 2 weeks. Exit criteria: yes (proceed), no (stay with React), or 'yes for simple components, no for complex ones' (hybrid approach)."
    hint: "A PoC tests a specific hypothesis, not a general impression. What is the decision-critical question that the PoC must answer?"
    reflectionPrompt: "Framework-agnostic is a property of the technology. Whether that property has value depends on your context. Evaluate the benefit against your actual requirements, not hypothetical future ones."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The 'total cost of ownership' of a technology choice includes:"
    options:
      - "Only the initial implementation and setup time"
      - "Initial implementation, ongoing maintenance, team learning curve, migration risk, and hiring impact"
      - "The licensing cost and infrastructure cost of running the technology"
      - "The time taken to evaluate and decide on the technology"
    correctIndex: 1
    tier: APPLICATION
    feedback: "Total cost of ownership covers the full lifecycle: initial build (often the smallest component), ongoing maintenance as the library evolves (breaking changes, API updates, security patches), team learning and onboarding costs, the risk and cost if migration becomes necessary, and the impact on hiring (a technology used by few engineers narrows the candidate pool). Technologies chosen based on initial build speed often prove expensive over 3-5 years when accumulated maintenance costs are counted."

  - type: MULTIPLE_CHOICE
    question: "A well-designed proof of concept (PoC) for a technology decision:"
    options:
      - "Builds a full prototype of the intended product to test the technology end-to-end"
      - "Answers a specific pre-defined technical question within a fixed time box with clear exit criteria"
      - "Demonstrates all the features of the new technology to the team"
      - "Compares the new technology against the existing solution on all dimensions"
    correctIndex: 1
    tier: APPLICATION
    feedback: "A PoC answers a specific question: the one technical risk or unknown that is decision-critical. 'Can this handle our throughput requirements?' 'Does the DX hold up for our most complex component pattern?' 'Is the React interop acceptable?' The PoC builds only what is needed to answer that question, within a fixed time box, with pre-defined exit criteria (yes/no/conditional). A PoC that produces 'it seems good' is exploration without a decision outcome — valuable for learning, but not for a technology adoption decision."

retrieval:
  recall: "What is the difference between a technology that is 'popular' and one that is a 'good fit'? Why can these diverge significantly?"
  explain: "A team wants to adopt a technology because it is used by large tech companies. What questions would you ask to determine whether their use case is relevant to yours?"
  mistakeId:
    code: |
      // Technology evaluation summary
      "We evaluated GraphQL vs REST for our new API layer.
       Decision: GraphQL.
       Reasons:
       - Used by Facebook, GitHub, and Shopify
       - Growing ecosystem
       - More flexible queries
       - Our developers are excited about it"
    answer: "Four weaknesses in this evaluation: (1) 'Used by Facebook, GitHub, and Shopify' — these organisations have hundreds of engineers, complex data graphs, and multiple API consumers. If your organisation has 5 engineers and a single frontend client, this reference is irrelevant. Evaluation should reference similar use cases, not prestigious ones. (2) 'Growing ecosystem' — ecosystem momentum is one signal of maturity and longevity, but it does not indicate fitness for your requirements. (3) 'More flexible queries' — flexible for whom? If you have one client making predictable queries, the flexibility provides no value and adds complexity (query depth attacks, N+1 problems, cache invalidation complexity). (4) 'Our developers are excited' — enthusiasm is not a technical criterion; it correlates with novelty, not fit. A rigorous evaluation would include: the specific data access patterns of your API consumers; the caching requirements (REST integrates more naturally with HTTP caching; GraphQL requires Apollo or similar); the team's operational experience (GraphQL adds new failure modes and debugging complexity); and a PoC that tests the decision-critical question for your use case."
---

# Hook

A new framework releases. The community is excited. The benchmark looks impressive. The conference talks are compelling.

Six months later, your team has migrated to it. Twelve months later, you are dealing with the breaking changes from version 2.0, the hiring pool has narrowed to engineers with this specific framework, and the original problem it was supposed to solve is still present.

Technology evaluation is not about following the industry. It is about solving your problem, with your team, in your context.

# Lore Introduction

*"The Academy once adopted a new enchantment system,"* the senior alchemist explains, *"because the Arcane Council had published a glowing endorsement. 'If it works for the Council, it will work for us,' the faculty said."*

*"The Council runs enchantments at continental scale. The Academy runs thirty. The system was designed for ten thousand simultaneous practitioners — we had eight."*

*"We spent two years adapting our teaching methods to the system's assumptions. We would have spent two months building what we actually needed."*

Prestigious adoption is not the same as appropriate adoption.

# Core Learning

## Concept Introduction

### The Hype Cycle Problem

Gartner's Hype Cycle describes a predictable pattern: new technologies pass through a Peak of Inflated Expectations before reaching a Plateau of Productivity. Adoption decisions made at the peak are based on promise rather than evidence.

The signal that a technology is ready for adoption: it has reached the Plateau. Other practitioners have solved the problems you will encounter. The ecosystem has matured. The failure modes are documented.

### Evaluation Framework

| Criterion | What to assess |
|---|---|
| **Problem fit** | Does it solve your specific problem better than alternatives? |
| **Ecosystem maturity** | Age, release cadence, contributor count, organisational backing |
| **Total cost of ownership** | Build + maintenance + learning + migration risk + hiring impact |
| **Team capability** | Current experience; learning curve; time to productive use |
| **Lock-in surface** | How deeply does it integrate? What does migration look like if wrong? |
| **Community health** | Support availability; issue resolution speed; documentation quality |

### Proof of Concept Design

A PoC answers a specific technical question, not a general impression.

```
Step 1: Define the decision-critical question
  "Can it handle X concurrent connections on our infrastructure?"
  "Does the DX hold for our most complex component pattern?"
  "Is the React interop acceptable for prop types we use?"

Step 2: Time-box
  1-2 weeks maximum. Longer PoCs become explorations.

Step 3: Define exit criteria before building
  Yes: adopt | No: don't adopt | Conditional: adopt for X cases only

Step 4: Build only what answers the question
  Not a prototype. Not a demo. An experiment with a hypothesis.
```

### Total Cost of Ownership

```
TCO = Initial build
    + Ongoing maintenance (version upgrades, breaking changes)
    + Team learning (current engineers + future hires)
    + Migration cost if wrong (low lock-in = low; high lock-in = high)
    + Hiring premium (narrow talent pool = higher cost or longer time-to-hire)
```

A technology that is 30% faster to build with but doubles hiring cost may not be net positive over 3 years.

### Lock-In Assessment

**Low lock-in:** The technology is used in one module; swapping it requires rewriting that module only. A date library. A utility function collection.

**High lock-in:** The technology pervades all code; swapping it requires a codebase-wide migration. A state management paradigm. An SSR framework. A component system.

High lock-in choices warrant proportionally more evaluation rigour before adoption.

## Common Mistakes

- **Prestige adoption.** "Facebook uses it" is not evidence it will work for your team of 6 with a different problem.
- **Recency bias.** New is not better; established is not obsolete. Stability is a feature.
- **PoC as exploration.** A PoC without a defined question and exit criteria produces "it feels good" — not a decision.

## Mini Summary

- Technology evaluation is structured decision-making, not popularity following
- Total cost of ownership includes build, maintenance, learning, migration risk, and hiring impact
- A PoC answers one specific question within a time box with defined exit criteria
- High lock-in technologies warrant more evaluation rigour proportional to the cost of being wrong

# Guided Practice Quest

Evaluate a meta-framework choice using structured criteria, perform a total cost of ownership analysis for a library migration, and design a PoC for a Web Components adoption decision.

# Solo Practice Quest

Your organisation currently uses a custom in-house component library built on raw HTML/CSS. A proposal has been made to migrate to a third-party design system (shadcn/ui). Define: (1) the evaluation criteria you would apply and how you would weight them, (2) the total cost of ownership analysis for both staying with the current library and migrating, (3) the PoC you would run to produce an actionable decision, (4) how you would present the recommendation to your engineering director, and (5) what conditions would change your recommendation.

# Integration

The philosophy of technology evaluation connects to epistemology — specifically, the distinction between evidence and testimony. A technology's reputation (what others say about it) is testimonial evidence: useful as a prior, but insufficient as a decision basis. Your PoC produces empirical evidence: direct observation of the technology's behaviour under your specific conditions. The epistemological lesson: strong testimony (major companies use it, community loves it) raises a technology's prior probability of being a good choice, but your specific context may differ from the conditions that produced the positive testimony. The risk management complement is the concept of regret minimisation (Bezos): when evaluating irreversible decisions, ask "which choice will I regret less if it turns out to be wrong?" A high-lock-in technology chosen hastily is the regret-maximising path. The technology lock-in problem is also an application of optionality theory from economics: low lock-in choices preserve future options; high lock-in choices foreclose them. Optionality has value — preserving the ability to change course without prohibitive cost is worth trading some immediate benefit.

# Lore Conclusion

*"The Academy eventually replaced the Council's enchantment system,"* the alchemist concludes. *"Two years of adaptation, then one year of migration. Three years total."*

*"The system we built ourselves took six months. It handled exactly what we needed — no more, no less. It has run without modification for four years."*

*"Prestige is about the Council's problems. We needed a solution for ours."*

*"Evaluate for your context. Build for your requirements. The Council's acclaim is not your specification."*

---
