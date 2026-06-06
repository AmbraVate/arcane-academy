---
id: fe-lea-m3-01
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m3
moduleTitle: "Module 3: UX Psychology"
moduleGlyph: "🧠"
moduleSortOrder: 3
topicSlug: cognitive_load
topicTitle: "Cognitive Load"
topicSortOrder: 1
lesson: cognitive_load
title: "Cognitive Load"
sortOrder: 1
difficulty: 4
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, design, philosophy]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Correctly distinguishes intrinsic, extraneous, and germane cognitive load
    - Applies Sweller's framework to at least two concrete UI design decisions
    - Explains how component design and information architecture reduce extraneous load
    - Addresses the engineer's cognitive load as a first-class concern alongside user load
    - Proposes specific design patterns that reduce cognitive load without oversimplifying
  keywords:
    - intrinsic load
    - extraneous load
    - germane load
    - working memory
    - chunking
    - progressive disclosure
    - information architecture
    - complexity
    - Sweller
    - Miller's Law
    - 7±2
    - simplicity
    - reduction
  modelAnswer: |
    John Sweller's Cognitive Load Theory distinguishes three types of cognitive load. Intrinsic load is the inherent complexity of the task — you cannot reduce it without reducing the task itself. Learning to code has high intrinsic load; the load is the learning. Extraneous load is complexity caused by poor presentation — navigation that requires memorisation, form labels that don't match their fields, error messages that describe the system state rather than the user's next action. Germane load is the cognitive effort that produces learning — the productive struggle of synthesising new information into schemas. Good design minimises extraneous load, preserves enough intrinsic load to be meaningful, and creates conditions for germane load.

    UI design decisions through the CLT lens: progressive disclosure reduces extraneous load by showing users only what they need to act now (not all options simultaneously). Component APIs that hide implementation (Button with variant='primary' rather than Tailwind classes) reduce extraneous load for developers — they don't need to hold the class mapping in working memory. Consistent navigation patterns reduce extraneous load by building familiar schemas — users stop reading navigation and start using it.

    Miller's Law (7±2 items in working memory) applies directly: navigation menus with 12 items exceed working memory capacity; users must re-read them on every visit. Menus with 5-7 items become schematically processed — faster and less effortful.

    The engineer's cognitive load is as important as the user's. A component with 40 props has high extraneous load for the engineer using it — they must remember which props are relevant, which combinations are valid, and what the defaults are. Good API design reduces this load through good defaults, clear naming, and minimal necessary surface area.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      A checkout form asks users to enter their full address across 8 separate fields: house number, street name, city, county, postcode, country, phone, and email. All fields are required, all visible simultaneously. Apply Cognitive Load Theory to identify what is creating excessive extraneous load, and propose a redesign.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [extraneous, fields, simultaneous, group, progressive, label, working memory, chunk, reduce]
      rejectedFeedback: "Extraneous load sources: 8 simultaneous required fields exham working memory; unclear grouping makes users wonder which fields are related; no progressive disclosure; all validation errors visible at once. Redesign: (1) group related fields visually (address block, contact block), (2) use progressive disclosure — show only the current group fully expanded, (3) inline validation as users leave each field (not all at submission), (4) postcode lookup to auto-fill address fields (reducing intrinsic load by removing data retrieval from the task). The task's intrinsic load (user must provide their address) is unchanged; the extraneous load (fighting the form interface) is dramatically reduced."
    hint: "What cognitive resources is the user spending on the form interface itself, rather than on the task of providing their address?"
    reflectionPrompt: "Extraneous load is cognitive work that produces no value for the user. Every second a user spends figuring out the form is a second not spent thinking about their actual goal. Good design makes the interface disappear."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Your design system's Card component has 34 props. Junior engineers on consuming teams consistently misuse it — using wrong prop combinations, providing unnecessary overrides, and creating visual inconsistencies. How does cognitive load theory explain this, and what would you do about it?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [extraneous, working memory, props, combination, default, reduce, surface area, preset, variant, API]
      rejectedFeedback: "The Card's 34-prop surface area imposes extraneous cognitive load on consuming engineers — they must hold many possible values in working memory, understand which combinations are valid, and know which props are relevant to their use case. This load leads to errors and inconsistency. Solutions: (1) introduce preset variants (Card.Summary, Card.Hero, Card.Compact) that pre-configure common combinations, (2) smart defaults that cover 80% of cases without any props, (3) deprecate or remove rarely-used props, (4) documentation with decision trees rather than just prop lists. The goal: a junior engineer should be able to use the Card correctly with 1-2 props in the common case."
    hint: "What is the difference between a component that could be used correctly and one that is actually used correctly by engineers who are also solving product problems simultaneously?"
    reflectionPrompt: "The API surface area is a design decision with cognitive consequences. Every prop that must be understood and decided on is extraneous load for the consuming engineer. Maximum expressibility is not the goal — maximum correct usage is."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A product manager argues that adding a fourth step to an onboarding flow will reduce activation. You believe the step is necessary for long-term engagement. How would you frame the cognitive load argument for reducing the flow, and how would you design a test to verify whether the fourth step helps or hurts overall retention?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [working memory, task, drop-off, completion, retention, test, A/B, measure, germane, schema]
      rejectedFeedback: "The argument: each onboarding step has both intrinsic cost (the user must complete it) and extraneous cost (interface navigation, form filling). Adding a fourth step increases total cognitive load at the highest-friction moment — before the user has seen the product's value. The counterargument: if the step builds a schema that makes the product significantly more useful, the germane load of the fourth step produces value that exceeds the extraneous cost of completing it. The test: run the 4-step onboarding as an A/B test. Primary metric: 30-day retention (or a proxy for your core activation event). If 4-step produces significantly higher 30-day retention, the germane load investment is worthwhile even if completion rate drops slightly."
    hint: "Cognitive load at onboarding affects both completion (extraneous load) and long-term success (germane load). These are in tension. How would you measure which effect dominates?"
    reflectionPrompt: "Not all cognitive load is bad. Germane load — the productive struggle that builds durable understanding — is valuable. The question is not 'reduce all cognitive load' but 'maximise the ratio of germane to extraneous load.'"

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A navigation menu has 11 items. A user must re-read it on every visit to find what they need. What cognitive principle explains this?"
    options:
      - "Users have low engagement with navigation"
      - "11 items exceeds working memory capacity — users cannot build a reliable schema for the menu"
      - "The items are poorly labelled"
      - "Users prefer visual search over schematic memory"
    correctIndex: 1
    feedback: "Miller's Law: working memory holds approximately 7±2 items. 11 items cannot be reliably held as a schema — users must re-read each visit, treating navigation as a search task rather than a recall task. Reducing to 5-7 main items (with grouping for the rest) allows users to build an automatic schema — they stop reading and start navigating. The same information, less extraneous load."

  - type: MULTIPLE_CHOICE
    question: "Progressive disclosure in a complex form means:"
    options:
      - "Revealing all form fields at once to avoid confusion"
      - "Showing only the fields relevant to the user's current step, hiding future steps until needed"
      - "Using tooltips to explain each field"
      - "Disabling form validation until the final step"
    correctIndex: 1
    feedback: "Progressive disclosure reduces extraneous load by limiting the number of decisions a user must hold in working memory at one time. A 20-field form shown in groups of 4-5 is the same data as a 20-field form shown simultaneously — but the cognitive experience is fundamentally different. The user focuses on one manageable chunk at a time."

retrieval:
  recall: "Define intrinsic, extraneous, and germane cognitive load. Give one example of each from a UI context."
  explain: "A senior engineer argues that simplifying a UI reduces cognitive load. A product manager argues that simplification removes features users need. How do you adjudicate this with Cognitive Load Theory?"
  mistakeId:
    code: |
      // UI design decision
      "To reduce cognitive load, we removed all advanced settings from
       the dashboard. Users now see only 3 options. Advanced users
       can use the API."
    answer: "This reduces all three types of load — including the intrinsic load of the task, effectively removing functionality rather than improving its presentation. CLT's goal is to reduce extraneous load (the friction of the interface) while preserving the task's intrinsic load (the actual work) and enabling germane load (productive learning). A better approach: progressive disclosure (show 3 options by default, expand to show advanced settings on demand) or personalisation (show common settings based on role). Removing features from the UI to advanced users is a product decision, not a cognitive load optimisation."
---

# Hook

Your product team adds a new analytics dashboard. It has 47 metrics, 12 filters, 6 chart types, and 3 date ranges — all visible simultaneously. User testing shows users feel overwhelmed and abandon the dashboard.

The data is correct. The complexity is real. But the extraneous cognitive load — the interface's demand on working memory — is destroying usability.

Understanding cognitive load is understanding why good data can be made unusable by poor presentation.

# Lore Introduction

*"The Grand Grimoire contains every known spell,"* the Academy's Head Instructor explains. *"Every wizard who has visited it reports leaving confused, having found nothing. The knowledge is there. The access to it is not."*

She sets down two volumes — a curated selection and the full Grimoire. *"The selection has thirty spells. The Grimoire has thirty thousand. The selection is more useful — not because it contains more, but because it fits within what a wizard can hold."*

Working memory is the wizard's capacity. Cognitive load is the design of the Grimoire.

# Core Learning

## Concept Introduction

**Sweller's Cognitive Load Theory** identifies three load types:

| Load Type | What it is | Design Goal |
|---|---|---|
| **Intrinsic** | Inherent task complexity | Cannot eliminate — preserve it |
| **Extraneous** | Interface/presentation friction | Minimise aggressively |
| **Germane** | Productive processing that builds schemas | Protect and enable |

**Miller's Law:** Working memory holds approximately 7±2 items simultaneously. Beyond this, users must resort to external storage (re-reading, writing down) — dramatically increasing task time and error rate.

**Applied to UI design:**
- **Navigation:** 5-7 primary items → schematic processing. 12+ items → search task every visit.
- **Forms:** Progressive disclosure (one section at a time) → focused processing. All fields simultaneously → overwhelmed working memory.
- **Dashboards:** Hierarchy and grouping → users find signal. Flat walls of metrics → users give up.
- **Error messages:** "Field required" (extraneous: system state). "Enter your email address" (reduced: user's next action).
- **Component APIs:** Smart defaults + variant presets → correct usage. 40 configurable props → engineers make mistakes.

## Common Mistakes

- **Confusing complexity with richness.** A rich product can have low extraneous load if well-designed. Removing features to reduce cognitive load is a content decision, not a design decision.
- **Applying CLT only to users.** Developers using your component library, API, or codebase are also users with limited working memory. Code organisation, naming, and API design are cognitive load decisions.
- **Optimising for minimum features.** The goal is minimum extraneous load, not minimum functionality. These are different.

## Mini Summary

- Intrinsic load = inherent task complexity (preserve); extraneous load = interface friction (minimise); germane load = productive learning (enable)
- Miller's Law: 7±2 items in working memory — design to fit within this limit
- Progressive disclosure, chunking, and clear hierarchy are the primary extraneous-load reduction patterns
- Component API design is a cognitive load decision — apply CLT to developer experience too

# Guided Practice Quest

Work through the three guided steps applying CLT to concrete scenarios: a complex form, a component API, and a product onboarding trade-off.

# Solo Practice Quest

You are reviewing designs for a new enterprise reporting tool. The initial design shows 15 configurable widgets on a single dashboard, a sidebar with 22 filter options, and a global date picker with 12 presets. Using Cognitive Load Theory, write a design review that: (1) identifies the sources of extraneous cognitive load, (2) estimates the impact on user success, and (3) proposes specific redesign recommendations with CLT justification.

# Integration

The psychological research base for CLT extends into neuroscience: Baddeley's model of working memory provides the biological substrate (phonological loop, visuospatial sketchpad, central executive) that CLT describes functionally. The 7±2 limit is not arbitrary — it reflects the capacity of the phonological loop and the rate at which unrehearsed items decay from working memory (~2 seconds without rehearsal). This biological grounding has philosophical implications: human cognitive limits are not bugs to work around but fundamental features of the kind of beings we are. Good interface design is anthropocentric in the deepest sense — it is shaped by the structure of human cognition. The engineer who understands CLT is designing not just for users who prefer certain interfaces, but for beings with specific and well-studied cognitive architecture.

# Lore Conclusion

*"The curated selection has thirty spells,"* the Head Instructor says, watching students navigate it efficiently. *"They find what they need within seconds. The Grimoire has thirty thousand — and they leave empty-handed."*

*"The knowledge is identical. The cognitive architecture differs. This is why design is as important as content."*

---
