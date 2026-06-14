---
id: fe-lea-m4-01
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m4
moduleTitle: "Module 4: Product Thinking"
moduleGlyph: "🎯"
moduleSortOrder: 4
topicSlug: user_needs
topicTitle: "User Needs"
topicSortOrder: 1
lesson: user_needs
title: "User Needs"
sortOrder: 1
difficulty: 4
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-lea-m3-04]
integrationDomains: [psychology, design, philosophy]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Correctly distinguishes functional, emotional, and social user needs
    - Applies the Jobs-to-be-Done framework to identify the underlying motivation behind a feature request
    - Explains why stated preferences often differ from observed behaviour
    - Demonstrates how to validate user needs through qualitative and quantitative methods
    - Connects user needs research to concrete frontend architecture or feature decisions
  keywords:
    - Jobs-to-be-Done
    - functional need
    - emotional need
    - social need
    - user research
    - qualitative
    - quantitative
    - observation
    - stated preference
    - revealed preference
    - empathy
    - persona
    - interview
    - validation
  modelAnswer: |
    Users rarely ask for what they actually need. They ask for solutions to problems they have already diagnosed — often incorrectly. A user who says "I need a faster search" may actually need to find information with less cognitive effort; a faster search that returns 200 results does not solve that need. The engineer who ships faster search has delivered what was requested but not what was needed.

    Jobs-to-be-Done reframes this: users "hire" a product to do a job in their life. The job has functional dimensions (what must be done), emotional dimensions (how it should feel), and social dimensions (how the user wants to be perceived). A project management tool is hired not just to track tasks (functional) but to reduce anxiety about forgotten work (emotional) and to demonstrate professional organisation to managers (social).

    Stated vs revealed preference: users report what they believe they prefer, but their behaviour reveals actual preferences. A user who says "I want more features" but only ever uses three features is revealing that breadth is not their actual need. Observational research (session recordings, clickstream analysis, usability testing) surfaces revealed preferences that interviews cannot.

    Validation methods: qualitative (user interviews, contextual inquiry, usability testing) reveals the why and surfaces unexpected jobs. Quantitative (A/B tests, funnel analysis, cohort retention) validates at scale. Neither alone is sufficient — qualitative without quantitative is anecdote; quantitative without qualitative misses the story behind the numbers.

    Engineering implication: understanding user needs shapes architectural decisions. If a core need is "reduce anxiety about progress," the frontend should surface progress indicators, completion confirmations, and undo options — not just ship the feature. The engineering team that understands the job builds a different product than the team that implements the specification.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      A product manager brings you this feature request: "Users are asking for a dark mode." Apply Jobs-to-be-Done thinking to identify the functional, emotional, and social jobs that might be behind this request — and explain how different underlying jobs would lead to different implementations.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [functional, emotional, social, job, need, why, eye, strain, preference, identity]
      rejectedFeedback: "Functional jobs: reduce eye strain in low-light environments; reduce battery consumption on OLED displays; improve readability for users with certain visual conditions (photophobia, migraines). Emotional jobs: feel that the product respects their preferences and environments; reduce discomfort during extended evening use. Social jobs: for developer tools — signal identity ('real developers use dark mode'); align with the aesthetic norms of a professional peer group. Different jobs → different implementations: If the functional job is eye strain, the priority is contrast ratios and avoiding pure white backgrounds in the dark theme — not just inverting colours. If the social/identity job is primary (common in dev tools), the dark mode should feel premium and be prominently discoverable. If the emotional job is comfort, automatic switching based on system preference (without requiring manual toggle) matters most. Shipping 'dark mode' without understanding which job it serves risks implementing the wrong version."
    hint: "Every feature request is a symptom. Jobs-to-be-Done asks: what is the user trying to accomplish, and what does it need to feel like?"
    reflectionPrompt: "Dark mode is a good example of a request that sounds simple but hides complex underlying needs. The implementation varies significantly depending on which job you are solving."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Your analytics shows that 60% of users who complete onboarding do not return after day 7. A product manager says: "We need to add more features to make the product stickier." How would you challenge this assumption, and what user needs research would you run to understand the actual problem?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [interview, research, qualitative, quantitative, why, churn, need, value, activate, understand]
      rejectedFeedback: "Challenge the assumption: '60% churn by day 7' is a behaviour, not an explanation. 'Need more features' is one hypothesis among many. The problem could be: (1) users did not experience the product's core value in the first 7 days — an activation problem, not a feature gap; (2) users achieved their immediate goal and have no recurring need — a product-market fit problem; (3) the onboarding did not successfully connect features to user jobs — a communication problem. Research: (1) Exit survey — survey users who haven't returned by day 10 ('what were you trying to do?', 'did you achieve it?'). (2) Session recordings — watch sessions 1-7 to identify where users drop or fail to progress. (3) Cohort analysis — which users who complete X action in days 1-3 have >50% retention at day 30? X is probably the activation event. (4) User interviews with churned users — 5-10 qualitative interviews to understand the job they hired the product to do and whether it was done. Adding features before diagnosing the job is speculation that may add complexity without solving the real need."
    hint: "Churn is a symptom. Research identifies whether the cause is feature gaps, activation failures, wrong audience, or product-market fit issues."
    reflectionPrompt: "Adding features to solve a retention problem is the most common expensive mistake in product development. Diagnosis before prescription."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You observe that users say they want a "simpler" interface, but your session recordings show that power users regularly use 12 different keyboard shortcuts and 6 advanced filter combinations. How do you reconcile stated preference with revealed behaviour, and what does this mean for your redesign strategy?
    inputConfig:
      minWords: 40
    markingRule:
      matchMode: CONTAINS
      accepted: [segment, stated, revealed, behaviour, power, novice, different, need, progressive, disclosure]
      rejectedFeedback: "Stated vs revealed preference: users who say 'simpler' are reporting their experience of the interface, not requesting feature removal. 'Simpler' often means 'less cluttered' or 'easier to find things' rather than 'fewer capabilities'. The power user behaviour (12 shortcuts, 6 filters) reveals that advanced capability is genuinely used — removing it would harm a segment that creates high retention and high referral value. Reconciliation: you likely have two distinct user segments with different needs. Novice/occasional users find the interface overwhelming — they need progressive disclosure and better defaults. Power users are already adapted — they need their workflows preserved. A redesign that serves both: simplified default view with progressive disclosure to advanced features; better discoverability of shortcuts (tooltip hints, searchable command palette) rather than eliminating them. The stated preference for 'simpler' was real — the solution was reducing noise, not reducing capability."
    hint: "What users say they want and what they do are often compatible if you interpret them correctly. 'Simpler' is an experience description, not a feature removal request."
    reflectionPrompt: "Revealed behaviour is evidence; stated preference is a signal about experience. A good researcher treats both as data to synthesise, not as contradictions to resolve by picking one."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Jobs-to-be-Done theory argues that users:"
    options:
      - "Always know what feature they need and request it directly"
      - "'Hire' products to accomplish underlying jobs that have functional, emotional, and social dimensions"
      - "Make rational purchasing decisions based on feature comparison"
      - "Prefer products with the most features available"
    correctIndex: 1
    tier: RECALL
    feedback: "JTBD: users don't buy products — they hire them to do jobs in their lives. A job has functional dimensions (what must get done), emotional dimensions (how should it feel), and social dimensions (how does it affect how others see me). Understanding the job — not just the stated feature request — leads to significantly better product decisions."

  - type: MULTIPLE_CHOICE
    question: "Why do qualitative user interviews sometimes contradict quantitative analytics data?"
    options:
      - "Users lie in interviews"
      - "Stated preferences (what users say) often differ from revealed preferences (what users do), and small interview samples may not represent the full user population"
      - "Analytics data is always more reliable than interview data"
      - "Qualitative methods are only useful for new products without analytics"
    correctIndex: 1
    tier: APPLICATION
    feedback: "Stated preferences reflect what users believe they prefer, want others to think they prefer, or think they should prefer — not necessarily what they actually do. Revealed preference (behaviour) is more reliable for predicting future behaviour. But analytics alone misses the 'why' — it shows that users drop off on step 3 but not that step 3 requires creating an account before users have seen any value. Both methods together are stronger than either alone."

retrieval:
  recall: "What is the difference between stated preference and revealed preference? Give an example of a product decision that would differ based on which you trust."
  explain: "A product manager says 'our users asked for X, so we should build it.' What questions would a product-thinking engineer ask before agreeing?"
  mistakeId:
    code: |
      // Feature prioritisation process
      "We ran a survey asking users to rate 20 potential features from 1-5.
       We sorted by average score and committed to building the top 5.
       Users gave 'offline mode' an average of 4.8/5."
    answer: "Survey ratings of hypothetical features are notoriously unreliable because users do not face real trade-offs — everything sounds good when free. A user who rates 'offline mode' 4.8/5 may never actually use it (their phone has reliable connectivity). The correct process: (1) Force ranking rather than individual ratings ('rank these 5 features by how much you'd miss them if removed') — forces real trade-offs. (2) Willingness-to-pay signal ('would you pay £5/month for offline mode?') — reveals actual value. (3) Observe usage patterns — what proportion of sessions occur in low-connectivity environments? (4) Interview users about workflow — 'tell me about the last time you couldn't use our product when you needed it.' The survey revealed a desired feature; research would reveal whether that desire corresponds to a real job."
---

# Hook

A user says: "I just want a button that does X."

You ship the button. They use it twice and stop returning.

Understanding what users actually need — not just what they say they need — is the skill that separates engineers who build features from engineers who build products.

# Lore Introduction

*"The apprentice wizard asked the visiting merchant what spell he needed,"* the instructor recounts. *"'Something to make my horse run faster,' the merchant said."*

*"The apprentice researched speed spells for three days. When they returned, the merchant had already bought a map."*

*"He didn't need a faster horse. He needed to arrive at the market before it closed. The horse was his proposed solution — not his actual need."*

Understanding the difference is everything.

# Core Learning

## Concept Introduction

### Three Levels of User Needs

| Level | What it is | Example |
|---|---|---|
| **Functional** | What the user needs to accomplish | Track time spent on tasks |
| **Emotional** | How they need to feel doing it | Feel in control, not overwhelmed |
| **Social** | How they want to be perceived | Look organised to their manager |

All three are real. All three influence product decisions. A time-tracking tool that nails functional need but feels punishing (emotional fail) will churn professional users.

### Jobs-to-be-Done (JTBD)

Users "hire" products to do jobs. The job is the unit of analysis — not the persona, not the feature.

**The Milkshake Example:** A fast food chain wanted to sell more milkshakes. They asked customers why they bought them. Surprisingly, most were bought in the morning, by commuters, alone. The job: something to hold and consume slowly during a long commute — filling, one-handed, and long-lasting. The milkshake competed not with other milkshakes but with bananas and bagels. Understanding the job led to a product decision (thicker milkshake, faster to purchase) that surveys never would have surfaced.

### Stated vs Revealed Preference

**Stated preference:** what users say they want (surveys, interviews, feature requests).
**Revealed preference:** what users actually do (session recordings, funnel analysis, usage data).

Neither is complete alone:
- Revealed preference shows *what*, not *why*
- Stated preference shows intent, but users cannot always predict their behaviour accurately

### Validation Methods

**Qualitative:** User interviews, contextual inquiry, usability testing, diary studies. Best for discovering jobs, the why behind behaviour, and unarticulated needs.

**Quantitative:** A/B tests, funnel analysis, cohort retention, usage frequency. Best for validating at scale and measuring impact.

## Why It Matters

User needs are the ground truth product decisions claim to rest on — and most teams are building on hearsay, because requests reached them stripped of the problems that motivated them:

- The request/need gap is the core hazard: users ask for features ("add an export button") when they have problems ("I can't get this data into my Monday report") — building the request as literally specified is how products accrete options while the underlying jobs stay painful
- Needs are discoverable, not guessable: interviews, support-ticket mining, and behavioural data each triangulate what users are actually trying to accomplish — and the team's confident intuition is reliably wrong precisely because the team is expert in the product and users are not
- Prioritisation inherits everything: rank by loudest request and you serve whoever emails most; rank by validated need-frequency-times-severity and you serve the silent majority who churn instead of complaining
- For leads the responsibility is structural — engineers who've watched a user struggle make better hundred-times-daily micro-decisions than engineers fed requirements through three layers of summary, so building that exposure *is* engineering management

Products fail far more often from solving unimportant problems well than from solving important problems imperfectly. Knowing which problems are important is not the PM's private job; it's the input quality for every technical decision you make.

## Common Mistakes

- **Building what was asked, not what is needed.** Feature requests are proposed solutions. Investigate the job beneath them.
- **Trusting survey ratings of hypothetical features.** Without real trade-offs, everything sounds good. Force ranking or willingness-to-pay reveals actual priority.
- **Interviewing users who are already happy.** Churned users, non-converts, and edge cases reveal needs the core audience doesn't surface.

## Mental Model

Treat user requests like symptoms reported to a doctor, never like prescriptions to fill. A patient says "I need antibiotics" — a vending-machine doctor dispenses them; a real doctor hears the request as *data about a problem*: what are the actual symptoms, when do they occur, what's the history? Often the diagnosis differs entirely from the self-prescription (the infection is viral; the requested export button is really a reporting-workflow problem that a scheduled email solves better). The discipline maps point for point: take the request seriously as evidence (patients know something is wrong; users always do), but interrogate it for the underlying condition — "what would you do with that export?" is the doctor's "where exactly does it hurt?". Beware the chronic complainers drowning out the silently sick (vocal requesters versus churning majority — epidemiology beats walk-ins, which is what analytics and ticket mining are). And like medicine, the cardinal sin is operating on the wrong diagnosis with excellent surgical technique: many beautifully engineered features are flawless treatments for diseases nobody had.

## Mini Summary

- User needs have functional, emotional, and social dimensions — all matter for product success
- JTBD: users hire products to do jobs; understand the job before designing the solution
- Stated ≠ revealed preference; use both qualitative (why) and quantitative (what) methods
- Validate before building: the cost of building the wrong thing is always higher than the cost of researching the right one

# Guided Practice Quest

Apply JTBD to a dark mode request, diagnose a retention problem without assuming the solution, and reconcile stated preference with power-user behaviour.

# Solo Practice Quest

You are the lead engineer for a B2B project management tool. Retention has plateaued at 60-day retention of 35%. The product manager wants to add AI-powered task suggestions as the next feature. Before committing engineering capacity, define: (1) the user research you would run to understand the retention problem, (2) the specific questions you would ask churned users, (3) how you would identify the activation event that predicts retention, and (4) whether AI task suggestions addresses any of the needs you hypothesise — or whether it solves a different problem entirely.

# Integration

JTBD connects to philosophy of mind and phenomenology: Heidegger's concept of *Zuhandenheit* (ready-to-hand) describes how a tool disappears from conscious awareness when it works perfectly — a hammer is not an object; it is an extension of will. When the tool fails, it becomes *Vorhandenheit* (present-at-hand) — an obstacle to examine. Users experience good products as invisible (the job gets done); bad products as present (the tool gets in the way). JTBD is the engineering translation of this phenomenological distinction: design for the job to be done invisibly, not for the features to be experienced consciously. The economic complement is revealed preference theory (Samuelson): observed choices in real markets reveal underlying utility functions more accurately than stated preference surveys. Both traditions converge on the same engineering conclusion: watch what users do, not just what they say.

# Lore Conclusion

*"The apprentice returned with a map spell,"* the instructor finishes. *"Not a speed spell. A way-finding enchantment that showed the merchant the fastest route to market — and flagged when the market gates would close."*

*"The merchant hired the wizard again for every journey after that."*

*"He hired them because they understood the job."*

---
