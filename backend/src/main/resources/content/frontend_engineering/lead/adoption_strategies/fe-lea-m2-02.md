---
id: fe-lea-m2-02
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m2
moduleTitle: "Module 2: Design System Governance"
moduleGlyph: "🏛️"
moduleSortOrder: 2
topicSlug: adoption_strategies
topicTitle: "Adoption Strategies"
topicSortOrder: 2
lesson: adoption_strategies
title: "Adoption Strategies"
sortOrder: 2
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
    - "Identifies the real reasons teams resist design system adoption (not just surface-level objections)"
    - "Distinguishes between migration strategy and greenfield adoption strategy"
    - "Describes at least two evangelism strategies with concrete mechanisms"
    - "Explains the contribution model and its role in building adoption through ownership"
    - "Addresses the 'path of least resistance' principle — making adoption easier than building independently"
  keywords:
    - resistance
    - migration
    - greenfield
    - evangelism
    - contribution
    - friction
    - ownership
    - adoption
  modelAnswer: |
    Teams resist design system adoption for real reasons, not ignorance. The most common causes are: the system does not yet cover their use cases, past experience with the system was poor (bugs, breaking changes, poor documentation), the adoption migration cost is high relative to perceived benefit, and a feeling that the system constrains their autonomy without compensating value.

    Understanding the real reason for resistance is prerequisite to addressing it. Treating resistance as irrationality or political obstruction misses the signal: the system is not yet good enough to justify the adoption cost for this team.

    The greenfield strategy — new projects adopt from day one — is the easiest path to adoption. There is no migration cost; teams can evaluate the system without disrupting existing work. Greenfield teams also become champions who can honestly report their experience. Migration strategy — convincing existing codebases to adopt — is harder and must be approached incrementally: adopt the system for new components, then gradually replace legacy components as they are touched for other reasons.

    Effective evangelism strategies include: embedding a design system team member in a consuming team for a sprint (paired adoption), running workshops on the system's most valuable capabilities, creating "adoption success stories" that make the ROI concrete for sceptics, and maintaining a public roadmap that shows the system is actively being invested in.

    The contribution model is a powerful adoption mechanism: teams that contribute to the system feel ownership over it. Converting a reluctant adopter into a contributor transforms their relationship to the system from "imposed constraint" to "shared asset."

    The foundational adoption principle is: make it easier to use the system than to build independently. This means excellent documentation, a fast contribution process, office hours, and proactive support. If bypassing the system is easier than using it, teams will always bypass it.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      Team A has been explicitly asked to adopt the company's design system for their new product. Three months in, their adoption rate is 15%. When you investigate, you find that the system does not include the data visualisation components they need, the button component has a known accessibility bug that nobody has fixed for two months, and the documentation for the form components is incomplete.

      How do you respond? What is your diagnosis of the adoption failure, and what would you need to do — both technically and relationally — to get Team A genuinely on board?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [gap, bug, documentation, fix, trust, partnership, roadmap, priority]
      rejectedFeedback: "Strong responses recognise that this is a system quality problem, not an adoption attitude problem. Team A's resistance is entirely rational given the system's state. What needs to change in the system before adoption is a reasonable ask?"
    hint: "What would Team A need to see before they could trust the system enough to build their product on it? Be specific."
    reflectionPrompt: "How do you recover trust with a team after a poor experience? What makes an apology and a commitment credible?"
  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      You have been asked to develop a migration strategy for five existing product teams to adopt the design system over the next 12 months. Each team has an existing codebase with their own component implementations. Forcing a big-bang migration would disrupt delivery significantly. The VP of Engineering wants all five teams at 80% adoption by end of year.

      Design the migration strategy. How do you sequence it, what incentives or support do you offer, and how do you measure progress without creating adversarial tracking dynamics?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [incremental, pilot, champion, support, sequencing, migration, measure, friction]
      rejectedFeedback: "Strong responses propose an incremental migration model with early adopter teams, concrete support structures, and progress measures that feel enabling rather than policing."
    hint: "Which teams would be your best early adopters — those who will have the easiest experience and become your champions? How do you use their success to pull the others forward?"
    reflectionPrompt: "What is the difference between a migration target and a migration journey? How does the framing affect team behaviour?"
  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You are preparing a presentation to five product engineering leads to convince them to prioritise adopting the design system in the next quarter. You know that two are enthusiastic, two are sceptical (they have had bad experiences with the previous version of the system), and one is actively hostile (they believe the system constrains their team's creative freedom).

      How do you structure this presentation? How do you address the different audiences? What specific evidence or demonstrations would be most persuasive?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [evidence, hostile, sceptic, story, demo, acknowledge, specific, tailor]
      rejectedFeedback: "Strong responses segment the audience and tailor the approach. The hostile lead needs something different from the enthusiast. Consider acknowledging the legitimate past grievances directly rather than ignoring them."
    hint: "The hostile lead and the sceptics have real grievances. Acknowledging them directly and specifically — rather than cheerleading past them — is more persuasive than presenting to the people who already agree with you."
    reflectionPrompt: "Have you ever been in the room as a sceptic while a presenter ignored your concerns? How did it land?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A team has a 15% design system adoption rate despite being asked to adopt. The most likely root cause is:"
    options:
      - "The team lacks the technical skill to use the system correctly"
      - "The team's leads are politically resistant to sharing standards"
      - "The system does not meet the team's needs well enough to justify the adoption cost"
      - "The team has not received enough training on the system"
    correctIndex: 2
    feedback: "Low adoption almost always reflects a value gap — the system does not cover enough of the team's use cases, has quality issues they cannot accept, or the adoption cost exceeds perceived benefit. Treating it as an attitude problem misses the signal."
  - type: MULTIPLE_CHOICE
    question: "The 'path of least resistance' principle in design system adoption means:"
    options:
      - "Making adoption optional so teams do not feel coerced"
      - "Making it structurally easier to use the system than to build independently"
      - "Reducing the number of components so the system is simpler to learn"
      - "Starting with the easiest components to implement"
    correctIndex: 1
    feedback: "Adoption accelerates when using the system is the lowest-friction path for a developer. This requires excellent documentation, quick contribution cycles, and active support — making bypass the harder option."
retrieval:
  recall: "What are the most common real reasons engineering teams resist design system adoption?"
  explain: "Describe an incremental migration strategy for moving an existing codebase to a design system, and explain why a big-bang migration is usually the wrong approach."
  mistakeId:
    code: |
      Anti-pattern: Adoption by Mandate
      The VP of Engineering announces that all teams must achieve 80% design system adoption by Q3.
      Progress is tracked in a monthly report. Teams that miss targets are asked to explain why
      in the all-hands meeting.
    answer: "Mandate-without-support creates compliance theatre rather than genuine adoption. Teams under pressure will adopt the system formally (updating imports, using component names) without actually benefiting from it — or will game the metrics. The adversarial dynamic poisons the relationship between the design system team and consuming teams. Adoption by persuasion and support, not mandate and measurement, produces durable adoption."
---

# Hook

The adoption dashboard shows 23%. Six months into the new design system. Eight product teams. The VP of Engineering messages me: "This isn't working."

She's right. It isn't. But not for the reason she thinks. She thinks the teams are being resistant. I know they are being rational. The system doesn't cover three of the most common use cases in the product. The documentation for the form components was written as if the reader already knows how the system works. Two teams tried to contribute components and were told to wait for the core team to build them — and the wait has been three months.

I don't have an adoption problem. I have a product problem. And I cannot fix the adoption problem until I fix the product problem.

# Lore Introduction

When the Unified Component Registry was first opened to the Guilds, less than 20% adopted it. The Registry's keepers were frustrated. They had built something excellent. Why were the Guilds not using it?

The Keeper of Records, Maeven Thral, spent a month embedded with three of the reluctant Guilds. She came back with a simple report: "The Registry does not include the enchantment matrices the northern Guilds use. The documentation assumes southern Guild training. The contribution process takes fourteen weeks. The Guilds are not wrong to wait."

Thral's report triggered three years of intensive improvement. Adoption reached 85% within a year of the reforms completing. The Guilds had not changed their minds. The Registry had earned their trust.

# Core Learning

## Concept Introduction

Adoption is not a communication problem. It is a product quality problem and a friction problem. Teams adopt design systems when the system meets their needs well enough to justify the cost of adoption, and when the path to adoption is clearly easier than the path to building independently.

Understanding resistance requires understanding its real causes. The most common causes are:

1. **Coverage gaps** — the system does not include the components this team needs
2. **Quality issues** — past bugs, breaking changes, or accessibility failures have eroded trust
3. **Poor documentation** — it is easier to build from scratch than to learn the system
4. **Slow contribution** — the team tried to contribute and the process was too slow or too closed
5. **Autonomy concerns** — the team believes the system will constrain their creative latitude

Each cause requires a different response. Coverage gaps require product investment. Quality issues require demonstrated improvement and trust rebuilding. Poor documentation requires investment in the documentation itself. Slow contribution requires process reform. Autonomy concerns require a contribution model that gives teams a genuine voice.

## Why It Matters

Adoption is the only outcome that matters for a design system. A system used by 20% of teams provides 20% of its potential value. The return on the investment in building and maintaining the system scales directly with adoption. Low adoption means the system is costing more to maintain than it is saving teams in effort.

More subtly, low adoption produces a spiral: teams that do not adopt build their own components; those components diverge from the standard; new engineers learn to work around the system; the gap between system and reality grows; adoption becomes even harder. High adoption produces the opposite spiral: more teams using means more investment, which means better coverage, which means more adoption.

## Worked Examples

**Example 1: Embedded adoption**
A design system team member spends one sprint embedded with a hesitant product team. They provide real-time support, hear the specific friction points first-hand, and fix or triage issues immediately. The product team's experience improves dramatically; they become adoption advocates. The embedded sprint costs one engineer for two weeks; the return is a genuine champion team and a list of the highest-priority improvements.

**Example 2: Reducing migration friction**
A new migration guide for Team B's specific tech stack is produced, showing exactly how to swap their legacy Button component for the system's Button component, including the prop mapping and any behaviour differences. The guide reduces the migration time from two days to two hours. Adoption for that component jumps from 10% to 70% within a sprint.

**Example 3: The contribution model in action**
Team C needs a complex Table component that the design system does not yet have. Instead of waiting 3 months for the core team to build it, they build it themselves following the contribution guidelines, with a core team engineer as reviewer. The component enters the system within 4 weeks. Team C moves from sceptic to advocate — they now own a piece of the system.

## Common Mistakes

**Treating adoption as a communications problem.** "We just need to do more evangelism" is a common response to low adoption. If the system is not good enough to justify adoption, evangelism produces one-time trial followed by abandonment. The right response to low adoption is to ask "what would make this good enough?" not "how do we convince people it is good enough already?"

**Big-bang migration requirements.** Asking teams to migrate all existing components at once creates an adoption tax that is too high. Incremental adoption — new components from the system, legacy components migrated opportunistically — is the path to durable growth.

**Hostile contribution processes.** If contributing a component takes more than two weeks or requires approval from a committee, most teams will not contribute. The contribution process should be as frictionless as possible.

**Measuring adoption as a percentage of components.** Teams can achieve 80% component adoption while using the components incorrectly or having a terrible experience. Supplement adoption metrics with satisfaction metrics and qualitative feedback.

## Mental Model

Think of adoption as **water flowing downhill**. Water does not choose the path it thinks is best — it takes the lowest-friction path available. If the path to your design system is higher-friction than the path to building independently, teams will build independently. Your job is to lower the terrain so the system is the lowest-friction option — better documentation, faster contribution, proactive support, and the credibility that comes from being reliable.

## Mini Summary

- Adoption resistance is almost always rational — it reflects real gaps in the system, not attitude problems
- Address the root cause of resistance (coverage, quality, documentation, contribution speed) not just the symptom (non-adoption)
- Incremental migration is more durable than big-bang adoption requirements
- The contribution model converts resisters into owners
- The foundational principle: make it easier to use the system than to build independently

# Guided Practice Quest

Work through the three guided steps in sequence. Each asks you to reason through the social and structural dimensions of design system adoption.

# Solo Practice Quest

Write a "Design System Adoption Playbook" (approximately 250 words) for your organisation. This is a practical guide that a product team engineering lead would read to understand how to adopt the design system and what support is available. Cover: the adoption path (what steps, in what order), what support is available from the design system team, how to handle use cases the system does not yet cover, and how to contribute components back to the system.

# Integration

**Psychology:** Rogers' Diffusion of Innovations describes how new ideas spread through a social system via innovators, early adopters, early majority, late majority, and laggards. A design system adoption strategy that only works for early adopters will plateau at 20-30% — the natural size of the early adopter population. Growing beyond that requires addressing the specific concerns of the early majority, who are more cautious and more motivated by proven value than novelty.

**Sociology:** The sociology of organisational change (Kotter's change model, Lewin's unfreeze-change-refreeze) consistently shows that change that bypasses the unfreezing stage — which requires creating a felt need for change and lowering the psychological resistance to it — fails. Teams that do not feel the pain of not having a shared system are not motivated to pay the cost of adopting one. The adoption strategy must make the cost of fragmentation visible before asking teams to invest in coherence.

**Philosophy:** The utilitarian calculation for adoption is clear: the system is worth adopting when the aggregate value to all consuming teams exceeds the maintenance cost. But the political reality is that individual teams make local decisions, not aggregate ones. A team that would benefit collectively but not individually from adoption requires either an organisational incentive or a system so good that local benefit is obvious. This is the fundamental tension in platform adoption.

# Lore Conclusion

Maeven Thral's legacy in the Arcane Academy is not the Registry itself — it is the principle she inscribed above the Registry's entrance:

"A tool unused is a tool wasted. The measure of this Registry is not what we have built. It is what the Guilds can build because we built it well."

The inscription is read by every new Registry keeper before they begin their tenure. It has never failed to calibrate their priorities.

---
