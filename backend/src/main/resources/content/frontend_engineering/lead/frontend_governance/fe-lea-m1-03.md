---
id: fe-lea-m1-03
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m1
moduleTitle: "Module 1: Frontend Leadership"
moduleGlyph: "👑"
moduleSortOrder: 1
topicSlug: frontend_governance
topicTitle: "Frontend Governance"
topicSortOrder: 3
lesson: frontend_governance
title: "Frontend Governance"
sortOrder: 3
difficulty: 4
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-lea-m1-01]
integrationDomains: [psychology, sociology, philosophy]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - "Explains what an ADR is and why the decision-recording process is as valuable as the decision itself"
    - "Describes the RFC process for frontend changes and how it creates distributed ownership"
    - "Articulates when to standardise vs when to allow autonomy — with concrete criteria, not vague principles"
    - "Identifies the problem of frontend sprawl across teams and proposes structural mechanisms to prevent it"
    - "Demonstrates awareness that governance can create bureaucracy and proposes lightweight alternatives"
  keywords:
    - ADR
    - RFC
    - standardise
    - autonomy
    - sprawl
    - lightweight
    - consistency
    - distributed
  modelAnswer: |
    Frontend governance is the system of practices and processes that ensure technical consistency and quality across an engineering organisation without requiring a single person to review everything. Done well, it scales decision-making. Done poorly, it creates bottlenecks and resentment.

    Architectural Decision Records (ADRs) are short documents that capture significant technical decisions: what was decided, the context that made the decision necessary, the alternatives considered, and the rationale for the choice. Their primary value is not the document itself but the forcing function — the discipline of writing down why you made a decision while you remember why. Teams that record decisions reduce re-litigation of settled questions and onboard new engineers far faster.

    The RFC (Request for Comments) process applies the same idea to proposed changes. Before implementing a significant frontend change — a new pattern, a library adoption, a breaking architectural shift — the proposer writes a brief document and circulates it for comment. This distributes ownership: the decision belongs to the community that reviewed it, not just the person who proposed it.

    The hardest governance question is when to standardise vs when to allow autonomy. The principle I use: standardise things that create coordination costs when they diverge (component library, testing approach, API integration patterns) and allow autonomy in things that do not (internal state management for isolated features, CSS organisation within a team). The test is: does inconsistency here cost another team time or create onboarding confusion?

    Frontend sprawl — multiple teams making independent, incompatible frontend decisions — is the natural entropy of autonomous teams. The structural mechanisms to prevent it are: a shared frontend platform team, a regular cross-team frontend guild meeting, and a lightweight RFC process for any decision that affects more than one team.

    The governance failure mode is bureaucracy: so many processes that engineers spend more time documenting than building. Governance should be as lightweight as the problem requires, biased toward async communication, and reviewed regularly for whether it is still earning its cost.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      Your organisation has five product teams, each making independent frontend decisions. Over 18 months this has produced: three different component libraries, two different state management approaches, four different testing frameworks, and no shared design tokens. New engineers find the divergence deeply confusing.

      Design a governance intervention. What is the first thing you would do? What structures would you put in place, and in what order? How do you prevent the cure being worse than the disease (i.e., bureaucracy)?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [ADR, RFC, guild, standardise, lightweight, priority, migration, alignment]
      rejectedFeedback: "Strong responses distinguish between what to tackle first (highest coordination cost) and what to leave alone, and propose lightweight mechanisms rather than heavy process."
    hint: "You cannot fix everything at once. What is the divergence that costs teams the most time? Start there."
    reflectionPrompt: "What is the minimum governance that would be sufficient? How would you know if you had gone too far?"
  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      A team in your organisation wants to adopt a new frontend framework for their product area — one that is not used anywhere else in the company. The team's argument is compelling: the framework is genuinely better suited to their problem domain, they have expertise in it, and using the standard framework has been causing them friction.

      Walk through your decision process. What factors would determine whether you approve, require an RFC process, or decline? What are the longer-term implications of saying yes? Of saying no?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [isolation, shared, migration, RFC, risk, justification, precedent, cost]
      rejectedFeedback: "Consider the precedent-setting nature of the decision and the long-term maintenance implications. Strong responses reason through both the immediate case and the governance system implications."
    hint: "This is not just a technical question — it is a governance question. What happens to your standardisation strategy if you say yes? What happens to team autonomy and morale if you always say no?"
    reflectionPrompt: "Where is the line between healthy team autonomy and organisational fragmentation?"
  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You want to introduce an RFC process for significant frontend decisions across your organisation. You have written a short proposal, but you are aware that engineers at your company have a strong anti-bureaucracy culture and will resist anything that feels like overhead.

      How do you introduce this process in a way that gets genuine adoption rather than compliance-without-buy-in? What would the MVP of this process look like? How would you measure whether it is working?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [lightweight, async, adoption, pilot, feedback, outcome, measure, trust]
      rejectedFeedback: "Strong responses recognise that process adoption is a social challenge as much as a design challenge. Consider how you would involve the resisters in the design."
    hint: "What would make this process feel like it serves engineers rather than manages them? How do you earn trust for a process before you have evidence it works?"
    reflectionPrompt: "What processes in your own experience have felt valuable vs bureaucratic? What was the difference?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The primary value of an Architectural Decision Record (ADR) is:"
    options:
      - "Providing a compliance trail for audits"
      - "Capturing the context and rationale behind a decision so future teams can understand it and revisit it appropriately"
      - "Preventing engineers from making bad decisions by requiring sign-off"
      - "Creating documentation that satisfies stakeholder requirements"
    correctIndex: 1
    feedback: "ADRs capture the 'why' of a decision, not just the 'what'. Their value is in reducing re-litigation of settled questions and allowing future teams to understand under what conditions the decision should be revisited."
  - type: MULTIPLE_CHOICE
    question: "When deciding whether to standardise a frontend practice across teams or allow autonomous choice, the most important factor is:"
    options:
      - "The technical superiority of the proposed standard"
      - "Whether divergence in this area creates coordination costs or onboarding confusion for other teams"
      - "The seniority of the engineers proposing the standard"
      - "How many teams are currently using the proposed standard"
    correctIndex: 1
    feedback: "Standardisation earns its cost when divergence causes cross-team pain. Things that are genuinely isolated to one team generally do not need to be standardised — doing so adds overhead without benefit."
retrieval:
  recall: "What are ADRs and what problem do they solve in a growing engineering organisation?"
  explain: "Explain the difference between governance that scales decision-making and governance that creates bureaucracy. What structural features distinguish the two?"
  mistakeId:
    code: |
      Anti-pattern: The Standards Committee
      An organisation creates a Frontend Standards Committee of senior engineers who must review
      and approve all significant frontend decisions. The committee meets fortnightly. Teams must
      submit proposals two weeks in advance. The committee has veto power over technology choices.
    answer: "The Standards Committee creates a centralised bottleneck that slows teams without improving outcomes. It also removes ownership from the teams making decisions — they follow the standard because they have to, not because they understand it. Good governance distributes decision-making through clear principles and async review processes (RFCs), not centralised approval."
---

# Hook

I open the Figma file for the new engineer onboarding experience and see something that stops me. The component in the top-left corner — I recognise it. It is our button component. Sort of. It has been recreated from scratch by a different team, slightly different padding, slightly different hover state, slightly different accessibility attributes. We now have two button components in production. Neither team knows the other exists.

This is not a one-off. This is what happens when six product teams operate independently with no shared understanding of what is ours versus what is theirs to invent. It is frontend sprawl. And fixing it — without creating a governance apparatus so heavy it grinds teams to a halt — is one of the hardest problems a frontend lead faces.

# Lore Introduction

The Grand Council does not govern by decree. The great schools learned, after the wars of the Third Era, that centralised control over magical practice produced either rebellion or dependency — never flourishing. Instead, the Council established the Compendium of Recorded Reasoning: a living archive of why significant magical decisions had been made, what alternatives had been considered, and under what circumstances the reasoning might no longer hold.

Any Guild Master could review the Compendium. Any practitioner could propose an amendment through the Rite of Open Consideration — what the Academy now calls the RFC. The magic that resulted was more consistent, more resilient, and more understood than anything a decree could have produced.

This is frontend governance at its best: not control, but distributed ownership of a shared record.

# Core Learning

## Concept Introduction

Frontend governance is the set of practices that ensures technical consistency and quality across teams without requiring a single person to be the bottleneck. In small organisations it is often informal. As teams grow beyond one pizza box, informality produces divergence — and divergence produces the coordination costs and onboarding confusion that slow everyone down.

The tools of effective frontend governance are:

**Architectural Decision Records (ADRs)** — short documents capturing significant technical decisions, the context that prompted them, alternatives considered, and the rationale for the choice. Their value is the discipline of recording reasoning while it is still alive, so that future teams can understand not just what was decided but why — and under what conditions it should be revisited.

**The RFC Process** — Request for Comments. Before implementing a significant frontend change (adopting a library, introducing a new pattern, deprecating a standard), the proposer writes a brief document and distributes it for team comment. This distributes ownership: the decision belongs to the community that reviewed it.

**Standards vs Autonomy** — not everything should be standardised. The test: does divergence in this area create coordination costs or onboarding confusion for other teams? If yes, standardise it. If no, allow autonomy and document the decision boundary.

**The Frontend Guild** — a regular cross-team forum for sharing decisions, reviewing RFCs, and preventing sprawl through social cohesion rather than mandate.

## Why It Matters

Frontend sprawl — the natural entropy of autonomous teams — compounds over time. Each independent decision seems reasonable in isolation. Collectively they produce a codebase that no single engineer understands, onboarding experiences that vary wildly, and duplicated effort across every team.

The cost is not just technical. Engineers who cannot understand the organisation's frontend practices feel less capable, less confident, and less connected to the broader engineering culture. Good governance creates coherence — the feeling that this was built intentionally, by people who were talking to each other.

## Worked Examples

**Example 1: An ADR in practice**
"We chose Zustand over Redux for global state management in new features (ADR-FE-027). Context: Redux's boilerplate overhead was causing inconsistency in how teams implemented state. Alternatives: Jotai (rejected — too novel, smaller community), Redux Toolkit (rejected — still more overhead than benefit for our use cases), Context API (rejected — performance issues at scale). This decision should be revisited if our state complexity grows significantly or the Zustand ecosystem shows fragility."

**Example 2: RFC preventing divergence**
A team proposes adopting a second date library alongside the organisation standard. An RFC surfaces the proposal, inviting comment across all teams. Three teams respond noting they have the same need. Rather than four independent adoptions, the RFC produces a single decision that serves all four teams — and updates the standard.

**Example 3: Governance that was too heavy**
A company creates a Standards Board requiring two-week review cycles for any library adoption. Teams respond by making decisions informally and not surfacing them, or by framing choices to avoid triggering the process. The governance creates compliance-without-ownership and drives decisions underground.

## Common Mistakes

**Governing everything.** Standardising low-stakes choices (file naming conventions within a single repo, specific linting rules that do not affect other teams) creates busywork without preventing the divergence that actually matters.

**Governance by decree.** Standards imposed without rationale or participation are resisted or circumvented. Engineers who participated in a decision own it; those who received it comply with it until the first moment they can bypass it.

**No mechanism for change.** Governance that produces permanent standards without a clear process for revisiting them produces zombie rules — practices everyone ignores because they no longer apply, but which remain "official." Every standard needs a home and a process for deprecation.

**Async-only RFCs with no social reinforcement.** An RFC process that nobody reads or comments on is not a governance process — it is a documentation exercise. The Frontend Guild meeting exists to make the async process socially real.

## Mental Model

Think of frontend governance as **town planning, not traffic policing**. Town planning creates the street layout, zoning rules, and shared infrastructure that make individual buildings cohere into a coherent city. It works through design, not enforcement. Traffic policing — pulling over individual violators — is expensive, adversarial, and does not address the structural causes of congestion. Build the infrastructure that makes good decisions easy; do not spend your time catching bad ones.

## Mini Summary

- ADRs capture the reasoning behind decisions, enabling future teams to understand and appropriately revisit them
- RFCs distribute ownership of significant decisions to the community affected by them
- Standardise things that create coordination costs when they diverge; allow autonomy where divergence is genuinely isolated
- The Frontend Guild creates social coherence across teams without centralised control
- Good governance is as lightweight as the problem requires and biased toward async processes

# Guided Practice Quest

Work through the three guided steps in sequence. Each presents a governance scenario requiring you to balance consistency with autonomy, structure with speed.

# Solo Practice Quest

You are writing a "Frontend Governance Charter" for your organisation — a short document (approximately 300 words) that describes the principles and practices your teams will use to make and record frontend decisions. Cover: what requires an ADR, what requires an RFC, what is within team autonomy, how standards get created and deprecated, and the role of the Frontend Guild. Write it in the voice of someone who has thought carefully about the failure modes of both under-governance and over-governance.

# Integration

**Psychology:** Self-determination theory (Deci and Ryan) identifies autonomy, competence, and relatedness as the three core psychological needs that drive motivation. Governance that removes autonomy without building relatedness (shared ownership of the standards) produces disengagement. The RFC process is, in this frame, a way to meet the relatedness need — engineers feel connected to decisions that affect them.

**Sociology:** Robert Michels' "Iron Law of Oligarchy" describes the tendency of any organisation to concentrate decision-making power in a small elite over time. Frontend governance structures can either resist this tendency (by distributing ownership through RFCs and guilds) or accelerate it (by creating Standards Committees that become permanent bottlenecks). Governance design is sociological design.

**Philosophy:** The philosopher Michael Polanyi described "tacit knowledge" — the things we know but cannot fully articulate, embodied in practice rather than documentation. ADRs are an attempt to make tacit knowledge explicit. They are imperfect — the full context of a decision can never be fully captured. But they are better than the alternative: the reasoning dies with the people who were in the room.

# Lore Conclusion

The Compendium of Recorded Reasoning now fills seventeen volumes in the Grand Archive. But its most consulted entry is the shortest — a single paragraph ADR from the founding era explaining why the Academy chose to teach all disciplines to all students rather than specialising from the first year.

The reasoning is two hundred years old. It has been challenged a dozen times by guilds who believed specialisation would be more efficient. Each time, the challenger has read the original reasoning, understood the context it was written in, and either updated their challenge or withdrawn it.

This is what a record of reasoning does. It does not prevent change. It prevents the waste of rediscovering what was already known.

---
