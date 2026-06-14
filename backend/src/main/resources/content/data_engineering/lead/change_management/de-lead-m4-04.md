---
id: de-lead-m4-04
school: DATA_ENGINEERING
domainId: data_engineering
tier: LEAD
moduleId: de-lead-m4
moduleTitle: "Module 4: Data-Driven Organisations"
moduleGlyph: "🔭"
moduleSortOrder: 4
topicSlug: change_management
topicTitle: "Change Management"
topicSortOrder: 4
lesson: 4
title: "Change Management: Leading Data Transformation"
sortOrder: 4
difficulty: 5
estimatedMinutes: 40
xpReward: 100
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-lead-m4-03
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes the stages of organisational change and the Lead's role at each"
    - "Identifies the stakeholder management responsibilities of a Lead Data Engineer"
    - "Explains resistance to change and differentiates legitimate from protective resistance"
    - "Describes the communication strategy for major data infrastructure changes"
  keywords:
    - change management
    - stakeholder management
    - change curve
    - resistance
    - coalition building
    - communication plan
    - early adopters
  modelAnswer: |
    Organisational change follows predictable stages. Kübler-Ross change curve: denial, frustration, depression, experimentation, acceptance. The Lead's role at each stage: denial (communicate evidence of why change is needed, not just the change itself); frustration (acknowledge concerns, create channels for feedback); depression (celebrate early wins, demonstrate progress); experimentation (support and enable); acceptance (document and scale success).
    Stakeholder management: identify all stakeholders (high power/high interest = manage closely; high power/low interest = keep satisfied; low power/high interest = keep informed; low power/low interest = monitor). Build a coalition of early adopters from every stakeholder group — they are the change agents who convert their peers.
    Resistance to change is not always irrational. Legitimate resistance: the change is technically wrong, creates problems not addressed, or has unintended consequences (valuable signal — investigate). Protective resistance: defending existing power structures, avoiding skill gaps, fear of accountability (acknowledge but do not capitulate). The Lead must distinguish between the two.
    Communication strategy: overcommunicate the why before the what. People resist change without understanding why it's necessary. The communication plan should: explain the problem being solved, the cost of not changing, how the change will work, what's in it for each stakeholder, and what they can expect during transition. Communication is not a single announcement — it is a sustained campaign across multiple channels and multiple interactions.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "The Consortium is migrating from a legacy data warehouse (15 years old) to a cloud-native platform. Twelve analysts have deep expertise in the old system. What change management failure is most common in this scenario?"
    options:
      - "Technical migration failure — legacy data doesn't migrate cleanly"
      - "Change management failure — analysts' expertise becomes worthless overnight, creating resistance; their concerns are dismissed as 'just resistance to change'"
      - "Security failure — cloud platforms have weaker security than on-premise"
      - "Timeline failure — migrations always take longer than planned"
    correctIndex: 1
    explanation: "Analysts with 15 years of expertise in the old system face a real loss: their skills become less valuable, their tribal knowledge (quirks of the old system) becomes irrelevant, and they face a steep relearning curve. This is rational protective resistance combined with legitimate concern (what about the complex queries they've built over 15 years?). Dismissing it as 'just resistance' fails to address the legitimate underlying issues (skill transition plan, migration of existing assets) and creates resentful non-adopters. A change management plan: involve key analysts in the migration design; provide retraining with clear career path; migrate their existing assets as a priority; celebrate their expertise in the new system."
  - type: FILL_BLANK
    question: "A ___ coalition is a group of influential, respected individuals from across the organisation who actively support the change initiative, converting their peers through credibility and example."
    answer: "change (or guiding)"
    explanation: "John Kotter's 8-step change model identifies building a guiding coalition as step 2. Coalition members are: influential (others follow their lead), credible (technically respected, not just senior), representative (from across affected teams), and actively committed (not passive supporters). They are the change's immune system: when resistance arises in their team, they address it from within. The Lead cannot single-handedly change a 60-person organisation; the coalition scales the change effort."
  - type: SHORT_TEXT
    question: "The Consortium is replacing Tableau with Looker. The commercial team has 40 Tableau dashboards built over 5 years. How do you structure the communication and transition plan?"
    modelAnswer: "Communication plan: 1. Announce 6 months in advance — 'We are transitioning to Looker. Here's why: [specific benefits — unified semantic layer, better self-serve capability, lower licence cost]. Here's what's in it for you: [Looker's features relevant to the commercial team]. Here's the timeline.' 2. Show, don't tell: deploy Looker alongside Tableau; build 2-3 flagship dashboards that are demonstrably better than their Tableau equivalents. 3. Migration support: 'We will migrate your 40 Tableau dashboards — you don't need to rebuild them from scratch. We'll prioritise the most-used 20 first.' 4. Training: Looker certification programme; dedicated training weeks; champions from within commercial team. 5. Sunset plan: Tableau access removed [date]; no exceptions but no surprises. 6. Feedback channel: 'If a Looker capability doesn't meet your need, tell us — we'll investigate.' Never: announce and disappear; remove Tableau without migrating assets; dismiss 'Tableau is better' as resistance without investigating."
microCheckpoint:
  question: "How do you distinguish legitimate resistance from protective resistance to change?"
  answer: "Legitimate resistance: the resister identifies a specific technical problem, unintended consequence, or gap in the change plan — investigate; they may be right. Protective resistance: defending existing power, avoiding skill gaps, or fearing accountability — acknowledge the concern but don't let it block necessary change. The test: does the resistance include a specific falsifiable objection? If yes, investigate. If it's general ('this won't work', 'change is always bad'), it's probably protective."
retrieval:
  recall: "What are the five stages of the Kübler-Ross change curve and what does each feel like?"
  explain: "Explain why a coalition of early adopters is more effective than top-down mandates for driving data platform adoption."
  mistakeId: "change-management-announce-disappear"
---

# The Migration Announcement

"We're migrating to the new cloud warehouse in 90 days. The legacy system will be shut down." The all-hands message was sent on a Monday morning. By Tuesday, the Lead Data Engineer had 23 emails in their inbox, 8 Slack messages, and had been pulled into three unscheduled meetings. The analysts were angry. The ops team was worried. Two senior managers were scheduled to meet the CDO to "discuss concerns." "We announced the what," the Lead Data Engineer said to the CDO. "We never explained the why. And we gave no one a voice in the transition." A 90-day migration had become a 90-day crisis.

# The Change Curve

Kübler-Ross's model, applied to organisational change:

```
       DENIAL         FRUSTRATION      DEPRESSION      EXPERIMENTATION    ACCEPTANCE
         ↓                 ↓               ↓                  ↓               ↓
  "This won't      "This is wrong,  "The new thing   "Let me try this  "I can do this
   affect us"       it's worse"      is too hard"      — it's OK"       and prefer it"

Lead's role at each stage:
  DENIAL:      Share evidence of why change is necessary; don't push
  FRUSTRATION: Acknowledge; create feedback channels; respond visibly
  DEPRESSION:  Celebrate small wins; show progress; check individual support
  EXPERIMENTATION: Enable; provide resources; reduce friction
  ACCEPTANCE:  Celebrate; document; scale; identify next wave of changers
```

The change curve is not linear — some people skip stages, some regress. The curve provides a diagnostic: which stage is each stakeholder group in? Different stages need different interventions.

## Stakeholder Management

```
Stakeholder mapping:

                        HIGH INTEREST
                              │
                   Manage closely│ Keep informed
   LOW POWER  ─────────────────────────────────  HIGH POWER
                   Monitor      │ Keep satisfied
                              LOW INTEREST

Consortium example:
  Manage closely:   Senior analysts (high power in their domain, high interest)
                    Direct manager of data team (high authority, high stakes)
  Keep satisfied:   CDO/board (high power, strategic interest not operational)
  Keep informed:    Junior analysts (directly affected, less influence)
  Monitor:          Adjacent teams (indirect interest)

For each stakeholder:
  Current position: Supporter | Neutral | Resistant
  Target position:  Supporter (ideally)
  Actions:          What specifically will move them toward supporter?
```

## Building the Change Coalition

```
Coalition criteria:
  ● Influential (peers follow their lead)
  ● Credible (technically respected)
  ● Representative (from the key stakeholder groups)
  ● Actively committed (willing to advocate, not just not oppose)

Coalition activation:
  ● Involve coalition members in design (they co-own the solution)
  ● Brief them before announcements (they can manage questions in their team)
  ● Give them resources (training, time, access)
  ● Make their advocacy visible (attribution and credit)
  ● Update them first on changes to the plan

Coalition size for Consortium (60 staff):
  6–8 members: 1 per major team + 1–2 from technical community
  Build consensus among the 8; they handle the remaining 52
```

## The Communication Plan

```
Communication calendar for warehouse migration:

  T-3 months: WHY announcement
    All-hands: "We're migrating. Here's why the current system costs us X.
               Here's what the new system enables. Here's what WON'T change."
    Department meetings: tailored "what this means for you" by team
    FAQ published: addresses top 20 anticipated concerns
    
  T-2 months: WHAT and HOW
    Technical briefing for analysts: migration plan, asset inventory
    Training schedule announced: Looker workshops start next month
    Demo: show the new system working with familiar data
    
  T-6 weeks: PARTICIPATION
    Migration task force launched: key analysts involved in priority decisions
    "What would make this better?" open question: feedback actively solicited
    First dashboards migrated: celebrate publicly
    
  T-2 weeks: COUNTDOWN
    Final reminders: specific dates, specific changes
    Last-call feedback: what concerns remain?
    Contingency plan communicated: what if we need more time?
    
  T+0: GO LIVE
    Real-time support channel
    Known issues communicated proactively
    Wins celebrated within hours of launch
    
  T+2 weeks: RETROSPECTIVE
    What worked? What didn't?
    Document learnings for next migration
```

## Handling Legitimate vs Protective Resistance

```
Analyst says: "The new system can't do recursive CTEs efficiently —
               our financial model breaks."

Diagnosis: Legitimate resistance — specific, technical, falsifiable
Response: Investigate immediately. Is this true? If yes:
  → Fix before migration (critical path)
  → Or: defer this workflow until fixed with explicit commitment
  → Involve the analyst in finding the solution (turns resister into contributor)
  
Senior manager says: "This migration is risky and unnecessary — 
                      the old system works fine."

Diagnosis: Likely protective (no specific technical objection)
Response: Acknowledge the concern; provide specific evidence of why change
  is necessary (cost, capabilities, support end-of-life, specific failures);
  offer a role in the migration governance;
  do not capitulate but do not dismiss.
```

## Common Mistakes

> **Announce and Disappear**
> A single announcement ("we're migrating in 90 days") with no sustained follow-up leaves people in denial with no conversion pathway. Change requires repeated communication across multiple channels over months.

> **Treating All Resistance as Obstruction**
> Analysts who raise technical objections may have discovered real problems. Dismissing resistance as "they just don't like change" misses valuable signals and creates resentment. Investigate every specific technical objection.

> **Starting with the What, Not the Why**
> People resist change whose necessity they don't understand. Lead with the problem being solved: "The legacy system costs €180k/year in maintenance; doesn't support our analytics use cases; and the vendor is ending support in 2025. Here's what we're building instead." The why must be compelling before the what is introduced.

## Mental Model

Think of change management as **running a political campaign for a policy you believe in**. You need: a clear case for why the policy is necessary (evidence-based), a coalition of influential supporters from across the electorate, a sustained communication campaign that reaches people multiple times through multiple channels, responses to specific objections, and a clear vision of what the future looks like. Announcing the policy once and expecting adoption is not how change works in democracies or organisations. The Lead is the campaign manager, not just the policy designer.

**Mini Summary**: Change follows the Kübler-Ross curve — denial, frustration, depression, experimentation, acceptance. The Lead's role differs at each stage. Stakeholder management: map by power vs interest; manage closely those with high power and high interest. Build a change coalition of 6–8 influential, representative early adopters. Communication plan: why before what, sustained over months, multiple channels. Distinguish legitimate resistance (investigate; they may be right) from protective resistance (acknowledge; don't capitulate). Never announce and disappear.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium is migrating from a centralised data team model (all analytics requests go to 4 data engineers) to a data mesh model (domain teams own their data products, supported by a platform team). This is a fundamental change in how every team works.

Design the change management plan:
1. Map the stakeholders — who gains, who loses, who has the highest resistance risk?
2. Build the coalition — who are the ideal 6 early adopters and why?
3. Write the communication plan — headline messages for T-6 months, T-3 months, T-1 month, and T+1 month.
4. What are the top 3 legitimate technical/operational concerns that could sink this change if not addressed?

---

# Integration

**Mathematics**: Change adoption follows the **Bass diffusion model** — the same model that describes technology product adoption. The probability a non-adopter adopts at time t: p + q × (N(t)/N) where p is the innovation coefficient (influenced by external communication), q is the imitation coefficient (influenced by peer adoption), and N(t)/N is current penetration. The change manager's levers: increase p by improving the quality and quantity of external communication (the why); increase q by building a visible coalition (making peer adoption visible). The model predicts a characteristic S-curve — slow start, accelerating growth, decelerating approach to full adoption. Understanding this curve prevents the common mistake of concluding "it's not working" during the slow initial phase when the model predicts slow growth.

**Sciences**: Organisational change mirrors **invasive species introduction ecology**. A new species (data platform) introduced to an ecosystem (organisation) faces: established competitors (legacy tools, existing workflows), limited resources (engineering time, stakeholder attention), and an immune response (resistance from the status quo). Successful invasive species have: a competitive advantage in their niche, faster reproduction (quick wins), and early colonisation of unoccupied space (teams with no existing data capability). The coalition of early adopters is the invasion's first wave — establishing a foothold from which the change propagates. Change management is applied invasion ecology.

---

# The Revised Plan

The migration timeline was extended by 30 days. The communication plan was rebuilt from the beginning: why the change was necessary, what analysts' assets would be preserved, how training would work, what the transition support would look like. Six analysts joined the migration task force. The first three dashboards migrated — chosen by the analysts themselves. The 23 emails became 3 emails, all with specific questions that had answers. Four months later, the last dashboard was migrated. The legacy system was decommissioned. "You can't mandate people into a new way of working," the Lead Data Engineer said. "You have to bring them."
