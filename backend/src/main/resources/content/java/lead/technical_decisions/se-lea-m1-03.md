---
id: se-lea-m1-03
school: engineering
domainId: java
tier: LEAD
moduleId: se-lea-m1
moduleTitle: "Module 1: Technical Leadership"
moduleGlyph: "🎓"
moduleSortOrder: 1
topicSlug: technical_decisions
topicTitle: "Technical Decision Making"
topicSortOrder: 3
lesson: technical-decision-making
title: "Technical Decision Making"
sortOrder: 3
difficulty: 4
estimatedMinutes: 38
xpReward: 160
practiceType: NONE
questType: MASTERY
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [economics, psychology, philosophy]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Correctly applies the reversibility framework to categorise decisions and explains the asymmetric risk of treating a one-way door as a two-way door
    - Uses DACI or RACI framework with appropriate role distinctions and explains when each is most useful
    - Frames technical debt as a financial instrument with explicit interest, principal, and carrying cost — not simply as bad code
    - Demonstrates ability to translate technical complexity for non-technical stakeholders without losing essential precision
    - Addresses the decision quality versus outcome distinction and explains why bad decisions can produce good outcomes and vice versa
  keywords:
    - reversible decisions
    - two-way door
    - one-way door
    - DACI
    - RACI
    - technical debt
    - option value
    - carrying cost
    - opportunity cost
    - decision quality
    - decision velocity
    - escalation
    - framing
    - cognitive bias
    - sunk cost
  modelAnswer: |
    Technical decision-making at the lead level involves a different set of concerns than at the individual contributor level. The questions shift from "what is the correct technical answer" to "how do we make this decision in a way that is fast enough, inclusive enough, documented well enough, and reversible enough to survive the inevitable change in our understanding of the problem."

    Jeff Bezos's two-by-two of reversible versus irreversible decisions is deceptively simple but deeply useful. Two-way doors — decisions that can be undone at reasonable cost — should be made quickly, by the people closest to the work, without extensive consultation. One-way doors — decisions whose consequences are very expensive to reverse — warrant more rigour, more stakeholder involvement, and more documentation. The asymmetric risk is treating a one-way door as a two-way door: moving fast on a database choice, a public API contract, or a data model that will be very expensive to change later. The reverse mistake — treating a two-way door as a one-way door — slows down good teams unnecessarily.

    Technical debt, properly understood, is not simply bad code. It is a financial instrument with specific properties: a principal (the work deferred), an interest rate (the overhead added to every future change by the existing debt), and a carrying cost that compounds over time. This framing changes the conversation. "We have a lot of messy code" is not actionable. "We have $40k of principal in our authentication module, carrying 20% interest per sprint, and it will be more expensive to carry it than retire it in Q3" is a business decision. Technical leaders who can translate debt into these terms unlock conversations with finance and product that are otherwise inaccessible.

    Communicating technical decisions to non-technical stakeholders requires a different kind of precision than communicating to engineers. The goal is not to simplify until accuracy is lost, but to find the level of abstraction at which the stakeholder can engage with the actual tradeoffs. "We can move fast now and pay more later, or move slightly slower now and reduce that future cost significantly" is usually accurate enough to support a real decision. The three-level explanation — technical, business, and risk — gives stakeholders multiple entry points to the same decision. The mistake is either over-simplifying to reassurance ("we've got it handled") or over-detailing to paralysis.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      Your team is deciding between two options for a new service: (A) use the existing shared authentication service, or (B) build a lightweight bespoke authentication module. Classify this decision as a two-way or one-way door, explain your reasoning, and describe how that classification should change the decision-making process.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [reversible, one-way, two-way, door, cost, change, classify, migration, dependency, coupling]
      rejectedFeedback: "The reversibility classification depends on what would be required to change the decision later. Deep coupling to a shared service creates a one-way door dynamic; a well-bounded bespoke module is more reversible. The key question is: what is the cost of migrating from this decision in 18 months? That cost determines whether the decision warrants extensive deliberation or quick resolution."
    hint: "What would it cost to change this decision in 18 months? What dependencies would it create? That cost determines its classification."
    reflectionPrompt: "The reversibility framework is most powerful when used before the decision is made, not after. Retroactively classifying bad decisions as 'one-way doors we should have deliberated more' is common but useless."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Your engineering team has accumulated significant technical debt in the payment processing module. You need to present the case for prioritising debt repayment over new features to the VP of Product, who measures success by feature velocity. How do you frame this conversation, and what information do you need to make the case compellingly?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [debt, interest, cost, velocity, risk, incident, slow, business, quantify, tradeoff]
      rejectedFeedback: "The framing must connect to outcomes the VP of Product cares about. 'The code is messy' is not compelling. 'This module caused three incidents last quarter, takes four times longer to change than comparable modules, and is slowing feature delivery by approximately X sprints per quarter' directly connects debt to the VP's concerns. Quantification, even approximate, changes the conversation from opinion to decision."
    hint: "What metrics would a VP of Product use to evaluate this argument? How can you connect technical debt to those metrics?"
    reflectionPrompt: "The most effective technical leaders are bilingual: they can reason precisely in technical terms and translate accurately to business terms. The translation is not simplification — it is finding the appropriate level of abstraction for the audience."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You are leading a design review for a significant architectural decision. Three senior engineers have strong opposing views. Describe how you would structure the decision-making process using a DACI or RACI framework, including who holds which role, how you would handle the disagreement, and how you would document the outcome.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [DACI, RACI, driver, approver, contributor, informed, role, decision, document, accountability]
      rejectedFeedback: "DACI (Driver, Approver, Contributors, Informed) makes decision accountability explicit before the decision is made. The Driver owns the process; the Approver makes the final call; Contributors provide input; Informed parties receive the outcome. For technical decisions with disagreement, the framework ensures everyone knows their role and that the decision will be made even without consensus. The outcome should be documented in an ADR that explicitly captures the alternative options and the reasoning for rejecting them."
    hint: "What is the difference between a Contributor and an Approver in DACI? Who should be the Approver for a significant architectural decision?"
    reflectionPrompt: "DACI does not resolve disagreement — it provides a legitimate process for making decisions despite disagreement. The Approver role makes accountability explicit so that decisions are not paralysed by the absence of consensus."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An engineering team is deciding which logging library to use for a new service. According to the reversibility framework, this decision should be:"
    options:
      - "Treated as a one-way door, requiring full architecture review board approval"
      - "Treated as a two-way door, decided quickly by the team without escalation"
      - "Escalated to the CTO because logging is a cross-cutting concern"
      - "Deferred until the system is in production and the real requirements are clear"
    correctIndex: 1
    feedback: "Switching logging libraries is generally a low-cost migration — it is a two-way door. Treating it as a one-way door wastes decision-making overhead. The reversibility framework suggests that decision-making rigour should be proportional to the cost of reversing the decision. For genuinely reversible technical choices, teams should move quickly and adjust based on evidence."

  - type: MULTIPLE_CHOICE
    question: "A principal engineer describes technical debt to the CFO as: 'We borrowed against future development speed to ship faster now. The interest is approximately two additional days of engineering time per feature in this module, compounding. If we retire the debt now it costs four weeks; in six months it will cost twelve.' This framing is most effective because:"
    options:
      - "It uses financial metaphors the CFO already understands, making the tradeoff concrete and time-sensitive"
      - "It overstates the precision of engineering estimates to appear authoritative"
      - "It removes the technical complexity entirely so the CFO can make the decision alone"
      - "It shifts blame for the debt to previous engineering decisions"
    correctIndex: 0
    feedback: "The financial framing of technical debt works because it translates an abstract technical concept into concrete business terms the CFO can evaluate: principal, interest rate, and the cost of deferring versus retiring. The precision need not be exact — 'approximately two days' is sufficient for a strategic conversation. The framing makes the compounding cost visible and creates urgency that pure technical descriptions do not."
retrieval:
  recall: "What is the core principle behind Bezos's 'two-way door vs one-way door' framework for decision-making, and how should the classification change the decision-making process?"
  explain: "A colleague argues that all significant technical decisions should go through a formal review process regardless of reversibility, because 'we can never fully predict which decisions will be hard to change.' Construct a rebuttal that acknowledges the concern while defending the reversibility framework."
  mistakeId:
    code: |
      A tech lead documents a technical decision by writing: "We decided to use PostgreSQL for the new service because everyone on the team knows it and it's reliable."
    answer: "This documentation captures the decision but not the context, alternatives considered, or consequences — making it nearly useless for future decision-making. A complete ADR would capture: what was the problem requiring a data storage decision, what alternatives were evaluated (e.g., MySQL, MongoDB, DynamoDB) and why they were rejected, what constraints shaped the decision (team knowledge, existing infrastructure, data model), and what the expected consequences are, including known risks. 'Everyone knows it' is a legitimate consideration but should be named as 'operational knowledge' to be evaluated if team composition changes."
---

# Hook

The hardest decisions in your career will not be the ones with a clear right answer — they will be the ones with three reasonable options, three smart engineers who each favour a different one, and a deadline. You will be tempted to call a meeting, achieve approximate consensus, and document the outcome as "the team agreed." But what actually happened is that the most senior person's opinion prevailed, the reasoning was not captured, and in two years no one will know why the decision was made or whether it still applies. The lead engineer's job is not to make every decision — it is to create a system for making decisions that is fast enough, accountable enough, and legible enough to survive the people who made the original call.

# Lore Introduction

In the arcane academies of old, the masters debated endlessly about the order of operations in a complex ritual. Some decisions they made once and sealed in stone; others they left open, revisitable, mutable. The wisest masters understood that the most dangerous decisions were not the ones they labelled as dangerous — they were the ones they made casually, under time pressure, that later turned out to have locked them into a path for decades. Technical leadership requires the same wisdom: not all decisions deserve the same rigour, and the skill is in distinguishing which deserve deep deliberation from which should be made quickly and adjusted empirically.

# Core Learning

## Concept Introduction

Jeff Bezos's decision framework distinguishes one-way doors (irreversible or very expensive to reverse) from two-way doors (easily reversed with low cost). The power of this framework is in its prescriptive implications: one-way doors warrant rigour, documentation, stakeholder involvement, and explicit consideration of alternatives; two-way doors should be made quickly, by the people closest to the problem, without escalation. Misclassification is the primary failure mode — treating a two-way door as a one-way door slows teams unnecessarily, while treating a one-way door as a two-way door produces decisions that calcify into permanent constraints.

In practice, the boundaries are blurry. A database choice might seem reversible until you have three years of production data in a specific schema. An API contract feels like a technical decision until external consumers depend on it. The working heuristic: what would it cost to change this decision in 18 months? If the cost is low, decide quickly. If the cost is high, invest in rigour proportional to that cost.

DACI and RACI frameworks make decision accountability explicit. In DACI: the Driver owns the decision process (not necessarily the final answer); the Approver makes the final call; Contributors provide input; Informed parties receive the outcome. RACI is structurally similar with Responsible, Accountable, Consulted, Informed. The critical discipline is defining roles before the discussion begins. Without explicit roles, decision discussions collapse into whoever argues most persistently winning, or into diffuse consensus-seeking that defers the actual decision.

Technical debt as a financial instrument transforms it from a morale argument into a resource allocation argument. Principal is the deferred work. Interest is the overhead debt adds to every future change in the affected area. Carrying cost is the total cost of maintaining the debt over time. This framing enables conversations that "the code is messy" cannot: "the interest on this authentication module is two days per feature; retiring the principal would cost four weeks now versus ten weeks in six months." These are numbers a product organisation can evaluate against its own priorities.

## Why It Matters

Decision velocity and decision quality are both essential. An organisation that makes decisions slowly loses competitive position; one that makes decisions without rigour accumulates irreversible mistakes that compound into architectural debt and organisational dysfunction. The lead engineer's job is to create asymmetric processes: fast and lightweight for reversible decisions, rigorous and documented for irreversible ones. This is not bureaucracy — it is resource allocation of decision-making attention, which is finite.

## Worked Examples

**The Accidentally Irreversible API.** A team decides quickly to return a list of user objects from an API endpoint, including the user's full name as a single field. Six months later, the team discovers that European users have names structured differently, and the field should have been given_name and family_name. The endpoint has 47 consumers. What seemed like a two-way door is now an 18-month migration project. The reversibility check should have included "who are the consumers of this API, and how expensive would a breaking change be?"

**The Debt Conversation.** A payments team's tech lead produces a one-page memo for the product VP: the checkout flow module has accumulated debt with an estimated interest cost of 3 additional days per feature. Current roadmap items are eight features, implying 24 extra engineering days this quarter — approximately six weeks of team capacity. Retiring the debt would cost four weeks. The product VP, seeing the arithmetic, asks for the debt retirement to be scheduled. Without the financial framing, the same request was denied three times as "not a priority."

**The DACI Review.** A cross-team architectural decision about service communication protocol (REST vs gRPC) has stalled for six weeks because three teams each have strong opinions and no one has authority to decide. A principal engineer defines the DACI: Driver is themselves, Approver is the VP of Engineering, Contributors are the three team tech leads, Informed are all engineering teams. The decision is made in one week with documented rationale that references the alternatives considered.

## Common Mistakes

**Consensus as a substitute for decision.** Waiting for all stakeholders to agree before proceeding. Consensus is rarely achievable on genuinely difficult decisions; it produces lowest-common-denominator outcomes or paralysis.

**Undocumented implicit decisions.** Making a decision verbally, in Slack, or in a meeting without an ADR or equivalent record. The decision exists but the reasoning does not, making it impossible to revisit appropriately.

**Binary debt framing.** Describing technical debt as good or bad rather than as a financial position with specific carrying costs and a rational repayment analysis.

**Over-escalating reversible decisions.** Creating overhead for decisions that could be safely made and adjusted empirically. This signals distrust, creates bottlenecks, and slows the team.

**Under-escalating irreversible decisions.** Moving fast on database choices, public contracts, or organisational commitments that will be very expensive to change.

**Decision by authority.** Using seniority rather than argument to resolve technical disagreements. Produces resentment, drives out dissenting views, and makes the team's decision quality dependent on the senior person's correctness.

## Mental Model

Think of decision-making bandwidth as a scarce resource, like money. Spending it uniformly across all decisions wastes it on low-stakes choices while under-investing in high-stakes ones. The reversibility framework is a resource allocation heuristic: invest more decision-making capital in decisions that are expensive to undo, invest less in decisions you can adjust quickly. DACI/RACI is the accounting system that tracks who owes what in the decision process. Technical debt is the financial liability on your engineering balance sheet — not a moral failing, but a position that has to be actively managed.

## Mini Summary

- Reversible decisions (two-way doors) should be made quickly and adjusted empirically; irreversible decisions warrant rigour proportional to their reversal cost.
- DACI/RACI makes decision accountability explicit before the conversation — prevents diffuse consensus-seeking from replacing actual decisions.
- Technical debt has principal, interest, and carrying cost; translating debt into these terms enables resource allocation conversations with non-engineering stakeholders.
- Decision quality and decision outcome are independent — a well-made decision can produce a bad outcome; a badly-made decision can get lucky.
- Documentation of alternatives considered is as valuable as documentation of the decision made — it prevents future engineers from revisiting already-rejected options.
- The lead engineer's highest-leverage role in decision-making is often creating the process, not making the decision.

# Guided Practice Quest

Work through the three guided steps above in sequence, providing detailed responses grounded in the frameworks covered. Each response should demonstrate understanding of the underlying principles, not just recite the frameworks.

# Solo Practice Quest

Your organisation is evaluating a migration from a monolithic Java application to a microservices architecture. This is a significant multi-year initiative that will affect every engineering team. Design a decision-making process for this initiative. Your response should address: how you classify this decision using the reversibility framework and what that implies for the process, what DACI roles you would assign and to whom, what information you would need before making the decision, how you would manage the dissenting opinion of a senior engineer who argues the migration is premature, how you would document the decision and the alternatives considered, and how you would communicate the outcome to non-technical stakeholders including the board.

# Integration

Technical decision-making connects deeply to behavioural economics and the psychology of judgment under uncertainty. Kahneman and Tversky's work on cognitive biases — anchoring, availability heuristic, sunk cost fallacy, overconfidence — describes the systematic ways in which human intuition produces poor decisions under uncertainty. Technical leaders who understand these biases can design decision processes that mitigate them: structured consideration of alternatives (reduces anchoring on the first option), explicit acknowledgement of what we do not know (reduces overconfidence), and time-bounded deliberation (prevents the sunk cost dynamic of continuing to invest in a decision simply because investment has already been made).

From economics, option value theory explains why reversible decisions are worth more than irreversible ones even when the immediate expected value is equivalent. A decision that preserves your ability to change course has embedded option value — the value of future flexibility. Technical leaders who frame architecture decisions in terms of option value — "this approach preserves our ability to migrate to a different approach in 18 months; this one closes that option" — are applying a precise financial concept that changes the calculus of technical choices.

From philosophy of action, the distinction between decision quality and outcome quality has deep roots in the ethics of decision-making under uncertainty. A decision made with all available information, following a sound process, can produce a bad outcome through no fault of the decision-maker. Conversely, a poor decision process can produce a good outcome by luck. High-performing engineering organisations evaluate decision quality separately from decision outcomes, creating a culture where good process is rewarded even when outcomes are adverse, and where bad outcomes are investigated for process failures rather than used as evidence of personal incompetence.

# Lore Conclusion

The master tactician does not win every engagement — they win the war by making more good decisions than their opponent, compounding over time. In technical leadership, the discipline of decision-making is less about individual correctness than about creating a system that generates consistently good decisions across hundreds of engineers and thousands of choices. The one-way door framework, the DACI process, and the financial framing of debt are not silver bullets — they are instruments of deliberate practice, applied systematically over years, that shift an organisation's decision quality from random to reliable.
---
