---
id: fe-lea-m3-03
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m3
moduleTitle: "Module 3: UX Psychology"
moduleGlyph: "🧠"
moduleSortOrder: 3
topicSlug: decision_making
topicTitle: "Decision Making"
topicSortOrder: 3
lesson: decision_making
title: "Decision Making"
sortOrder: 3
difficulty: 4
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-lea-m3-02]
integrationDomains: [psychology, philosophy, economics]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Correctly identifies System 1 (fast, automatic) and System 2 (slow, deliberate) thinking and their implications for interface design
    - Applies at least two cognitive biases (e.g. anchoring, status quo, loss aversion) to concrete UI decisions
    - Distinguishes between reducing friction for correct decisions and nudging users toward decisions they might regret
    - Addresses the ethical boundary between helpful defaults and dark patterns
    - Proposes specific design patterns that reduce decision fatigue without removing user agency
  keywords:
    - System 1
    - System 2
    - cognitive bias
    - anchoring
    - status quo bias
    - loss aversion
    - decision fatigue
    - defaults
    - nudge
    - dark pattern
    - choice architecture
    - friction
    - autonomy
    - heuristics
    - Kahneman
  modelAnswer: |
    Kahneman's dual-process theory distinguishes System 1 (fast, automatic, heuristic-driven) from System 2 (slow, deliberate, effortful). Most user interactions run on System 1 — users scan, pattern-match, and act without deep analysis. UI design that requires System 2 engagement for routine tasks creates friction and errors.

    Applied to interface design: anchoring means the first number a user sees shapes their reference frame. A pricing page that shows the highest tier first makes mid-tier look affordable; showing lowest tier first makes mid-tier look expensive. Neither is neutral — both are choice architecture decisions. Status quo bias means users stick with defaults even when alternatives are better; this makes defaults a high-stakes design decision, not an afterthought. Loss aversion (losses feel ~2x worse than equivalent gains) explains why "cancel subscription" flows that emphasise what users will lose see higher cancellation abandonment than flows that merely present a confirm button.

    Decision fatigue: repeated decisions deplete capacity for careful subsequent choices. Onboarding flows that ask 15 sequential preference questions produce worse outcomes (hasty final answers, higher abandonment) than flows that set smart defaults and let users adjust over time. Progressive disclosure of settings reduces fatigue without hiding functionality.

    The ethical boundary: helpful defaults reduce friction for decisions users would have made anyway with more information. Dark patterns use the same psychological mechanisms to extract decisions users would have refused with more information. The test: would a user feel helped or manipulated if they understood the mechanism? Friction-reduction is ethical; deliberate exploitation of cognitive bias is not.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      A travel booking site shows prices like this: the first listed option is £450, the second is £280, the third is £195. Users consistently rate the £195 option as "good value." Explain which cognitive bias is operating and how you would design the pricing display to help users make decisions based on their actual needs rather than anchoring to the first price they see.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [anchor, anchoring, reference, first, frame, order, sort, user, need, comparison]
      rejectedFeedback: "Anchoring: the first price (£450) sets a reference frame. Every subsequent price is evaluated relative to it — £195 feels cheap because it is 57% less than the anchor, not because it meets the user's actual needs. The user may be comparing against an anchor rather than against their budget or requirements. Design alternatives: (1) show prices sorted by 'most popular' or 'best for you' (personalised) rather than descending; (2) lead with user requirements ('How many nights? How many guests?') before showing prices, so the user has their own reference frame; (3) show the range transparently ('Prices from £195 to £450') at the top before listing options. The goal is to help users compare options against their own needs, not against an arbitrary first number."
    hint: "The first price shown creates a reference point that colours how every subsequent price feels — even if the first price is entirely irrelevant to the user's budget."
    reflectionPrompt: "Anchoring is unavoidable — some option will always appear first. The question is not whether you anchor, but whether you anchor helpfully or arbitrarily."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Your analytics shows that 80% of users never change the notification settings in your app — they keep the defaults. A product manager proposes defaulting all notifications to ON to maximise engagement metrics. Argue for and against this approach using psychology and ethics, then propose what a well-designed default should look like.
    inputConfig:
      minWords: 60
    markingRule:
      matchMode: CONTAINS
      accepted: [default, status quo, opt-in, opt-out, engagement, trust, ethical, friction, consent]
      rejectedFeedback: "For the proposal: status quo bias means most users will not change defaults — defaulting ON maximises notifications delivered and engagement metrics in the short term. Against: users who did not actively choose notifications and feel intruded upon will disengage or uninstall. Short-term engagement metrics can mask long-term trust erosion. Defaulting ON for notifications is also legally constrained (GDPR opt-in requirements for marketing communications). Ethically: a default should represent the choice a well-informed user with the product's best interests in mind would make — not the choice that maximises one company metric. A well-designed default: critical notifications ON (account security, transaction confirmations), optional engagement notifications OFF with clear in-app prompts at contextually appropriate moments ('Never miss a sale — enable deal alerts?'). This respects user attention while making opting-in frictionless at the moment of highest relevance."
    hint: "Status quo bias means defaults are not neutral — they are choices. The ethical question is whose interests those choices serve."
    reflectionPrompt: "Every default is a product decision masquerading as a technical default. Owning it explicitly leads to better outcomes for users and for long-term product trust."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A SaaS product's cancellation flow is: click 'Cancel' → modal asks 'Are you sure?' → second modal says 'You will lose access to 47 saved reports, 12 integrations, and 200GB of data' → final step shows a 'pause instead' option prominently with cancel buried in small text. Is this a legitimate friction pattern or a dark pattern? Where is the line?
    inputConfig:
      minWords: 60
    markingRule:
      matchMode: CONTAINS
      accepted: [dark pattern, friction, legitimate, loss aversion, inform, manipulate, hide, bury, ethical, autonomy]
      rejectedFeedback: "The first two steps are legitimate: confirming intent prevents accidental cancellations (genuine user protection). Showing what will be lost is informative — loss aversion is a real psychological effect, and users genuinely may not realise what they'd lose. The line is crossed at the final step: burying 'cancel' in small text while making 'pause' prominent uses UI manipulation (not information) to obstruct a decision the user has already made twice. A legitimate final step would present both options with equal prominence, letting the user choose based on the information provided. The test: if the user understands the psychology being applied, would they feel helped or manipulated? The data-loss disclosure helps. The UI hierarchy manipulation exploits. One is choice architecture in service of the user; the other is dark pattern territory — using cognitive bias to frustrate a legitimate decision."
    hint: "Informing users about consequences is legitimate. Manipulating visual hierarchy to obstruct a decision already made twice crosses into dark pattern territory."
    reflectionPrompt: "The line between nudge and dark pattern is consent and information. A nudge presents accurate information and a genuine alternative. A dark pattern obscures, hides, or exploits — even if technically the option exists somewhere on the page."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "System 1 thinking in Kahneman's model is best described as:"
    options:
      - "Slow, effortful, and deliberate — used for complex decisions"
      - "Fast, automatic, and heuristic-driven — used for most everyday interactions"
      - "Only activated when users are under stress"
      - "The conscious decision-making system that evaluates all options"
    correctIndex: 1
    tier: RECALL
    feedback: "System 1 is fast, automatic, and pattern-based. It handles the vast majority of daily decisions without conscious effort — which is why UI that requires System 2 engagement for routine tasks (reading instructions, evaluating options) creates friction and errors. Good interface design works with System 1 for common paths and reserves System 2 engagement for genuinely important decisions."

  - type: MULTIPLE_CHOICE
    question: "Decision fatigue in UX means:"
    options:
      - "Users give up on forms because they are too long"
      - "Repeated decisions in a session deplete users' capacity for careful subsequent choices, increasing errors and abandonment"
      - "Users feel tired after using a complex product"
      - "The interface makes decisions on behalf of the user without consent"
    correctIndex: 1
    tier: APPLICATION
    feedback: "Decision fatigue is the depletion of decision-making quality after repeated choices. An onboarding flow with 15 consecutive preference questions produces worse outcomes than one that sets smart defaults and allows gradual adjustment — the last few answers are less considered, and abandonment increases. The design response is to reduce unnecessary decision points, use progressive disclosure, and reserve explicit user choices for genuinely meaningful preferences."

retrieval:
  recall: "Name two cognitive biases that affect user decision-making in interfaces. For each, give one example of how it manifests in a UI."
  explain: "A designer argues that making the 'upgrade' button larger and more prominent than 'stay on free' is just good design — you want users to see the better option. A product ethicist argues this is a dark pattern. Who is right, and why?"
  mistakeId:
    code: |
      // Onboarding flow design
      "Step 1: Enter your details
       Step 2: Choose your plan
       Step 3: Set notification preferences (12 toggles)
       Step 4: Configure integrations (8 options)
       Step 5: Set display preferences (6 options)
       Step 6: Configure privacy settings (9 toggles)"
    answer: "This onboarding flow front-loads 35 decisions before the user has experienced any value from the product. Decision fatigue means the quality of choices made in steps 4-6 is significantly worse than choices made in step 1. Privacy settings — arguably the most important — are last, when fatigue is highest. The redesign: collect only the minimum information needed to start (name, email, password). Set all other preferences to well-reasoned defaults. Surface preference configuration at contextually relevant moments — notification settings when the user first receives a notification, integration settings when they try to connect a tool. Users make better decisions about settings they have actually encountered."
---

# Hook

Your checkout has a 'Remember my card' checkbox. It's pre-ticked. 70% of users don't notice it. 

Is that good design — reducing friction for users who want the feature? Or is it a dark pattern — harvesting a decision users didn't consciously make?

Understanding how users actually decide — not how we imagine they decide — is the difference between designing for users and designing against them.

# Lore Introduction

*"The Academy's entrance examination has twenty questions,"* the assessor explains to the new intake. *"We always put the easiest ones last."*

A student frowns. *"Shouldn't the easiest ones come first — to build confidence?"*

*"Confidence, yes. But we found that students who answered twenty questions in order — hardest to easiest — chose the most careful answers to the final questions. When we reversed the order, students rushing through the easy final questions made more errors than they did on the hard early ones."*

She pauses. *"Decision fatigue. The mind depletes. The question is what depletes it — and what we design to protect against that depletion."*

# Core Learning

## Concept Introduction

**Kahneman's Dual-Process Theory** describes two cognitive systems:

| System | Characteristics | UI Implication |
|---|---|---|
| **System 1** | Fast, automatic, heuristic-based | Most user interactions run here |
| **System 2** | Slow, deliberate, effortful | Required for complex analysis; depletes quickly |

Good interface design routes common tasks through System 1 (pattern recognition, familiar flows) and reserves System 2 engagement for genuinely important decisions (consent, financial transactions, irreversible actions).

### Key Biases in UI Context

**Anchoring:** The first number (or option) seen creates a reference frame for all subsequent evaluation. A pricing table showing £500/mo first makes £200/mo feel cheap — regardless of absolute value.

**Status quo bias:** Users strongly prefer keeping things as they are. Defaults are therefore not neutral — they represent the choice most users will make. A default is a product decision.

**Loss aversion:** Losses feel approximately twice as bad as equivalent gains feel good. "You'll lose 3 months of history" outweighs "you'll save £20/month" in cancellation decisions. Informing users of what they'll lose is legitimate; exploiting this asymmetry to obstruct decisions is not.

**Decision fatigue:** Repeated choices deplete decision quality. An onboarding flow with 15 consecutive preference questions produces poor final answers and higher abandonment.

### Choice Architecture Principles

1. **Smart defaults:** Default to the option a well-informed user would choose, not the option that maximises one company metric.
2. **Progressive disclosure:** Surface decisions at the moment of relevance, not upfront in bulk.
3. **Friction calibration:** Add friction to irreversible/high-stakes decisions. Remove friction from low-stakes routine actions.
4. **The nudge test:** If a user understood the mechanism you are using, would they feel helped or manipulated?

### The Dark Pattern Line

A **nudge** presents accurate information and a genuine choice, structured to help users act in their interest.

A **dark pattern** uses visual hierarchy, pre-selection, hidden options, or false urgency to extract a decision the user would have refused with full information.

The test: **Would a reasonable user, if they understood the design decision, feel served or deceived?**

## Common Mistakes

- **Treating defaults as technical decisions.** Every default is a product decision with psychological consequences.
- **Confusing friction reduction with manipulation.** Removing unnecessary steps is good UX. Removing the user's ability to make a different decision is not.
- **Applying loss aversion to obstruct rather than inform.** Showing users what they'll lose on cancellation is informative. Hiding the cancel button is obstructive.

## Mini Summary

- System 1 handles most user interactions automatically; design should work with it, not fight it
- Anchoring, status quo bias, loss aversion, and decision fatigue are measurable forces in every interface
- Defaults are product decisions — they should reflect what a well-informed user would choose
- The nudge/dark-pattern line is consent and honesty: helping users decide vs exploiting their cognitive architecture against them

# Guided Practice Quest

Work through three scenarios: pricing anchoring, notification defaults, and a cancellation flow ethics analysis.

# Solo Practice Quest

You are the lead designer reviewing a new subscription upgrade flow. The current design: (1) shows the premium plan with a green 'Recommended' badge, (2) pre-selects annual billing ('Save 20%'), (3) on the payment confirmation screen, there is a pre-ticked 'Add priority support for £5/mo' checkbox in smaller text, (4) after purchase, an exit-intent modal offers a 'downgrade' option that requires filling a feedback form before confirming. Write a design review that identifies which elements are legitimate UX optimisations and which cross into dark pattern territory, with psychological justification for each.

# Integration

Kahneman's dual-process framework has deep roots in behavioural economics (Thaler and Sunstein's Nudge, Ariely's Predictably Irrational) and cognitive neuroscience. System 1 processing is correlated with amygdala activity and fast pattern-matching networks; System 2 with prefrontal cortex engagement. The philosophical implications are significant: if users are not fully rational agents — if their decisions are systematically shaped by presentation, framing, and depletion — then the designer holds real power over outcomes. This is not different from traditional product design; it is an explicit acknowledgment of something that was always true. The ethical obligation is not to eliminate influence (impossible) but to exercise it in the user's interest. The concept of informed consent from medical ethics applies directly: an interface that hides the mechanism of influence denies the user the information they would need to decide differently.

# Lore Conclusion

*"The examination order matters,"* the assessor concludes. *"Every wizard who designs a trial, a form, a ceremony — they are making decisions about how other minds will engage. The question is whether they make those decisions consciously, or leave them to accident."*

*"The Academy designs its examinations for accuracy — for the student to perform at their best. Not for the Academy to extract the answer it wants."*

*"That distinction,"* she says, *"is the entire difference between design and manipulation."*

---
