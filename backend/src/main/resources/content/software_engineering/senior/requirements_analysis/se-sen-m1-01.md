---
id: se-sen-m1-01
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m1
moduleTitle: "Module 1: System Design"
moduleGlyph: "🏗️"
moduleSortOrder: 1
topicSlug: requirements_analysis
topicTitle: "Requirements Analysis"
topicSortOrder: 1
lesson: requirements_analysis
title: "Requirements Analysis"
sortOrder: 1
difficulty: 3
estimatedMinutes: 35
xpReward: 80
practiceType: NONE
questType: INVESTIGATION
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, economics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Distinguishes functional requirements (capabilities) from non-functional requirements (quality attributes)
    - Explains capacity estimation techniques and why order-of-magnitude thinking matters
    - Articulates how SLAs translate into concrete technical constraints
    - Identifies the cost amplification of requirements discovered late versus early
    - Demonstrates stakeholder analysis techniques and how conflicting interests are resolved
  keywords: [functional requirements, non-functional requirements, SLA, SLO, capacity estimation, stakeholder, latency, throughput, availability, RACI]
  modelAnswer: |
    Requirements analysis is the discipline of transforming vague stakeholder wishes into precise, verifiable, and prioritised specifications before any design or code is written. The distinction between functional and non-functional requirements is foundational: functional requirements describe what a system must do — process an order, authenticate a user, send a notification — while non-functional requirements (NFRs) describe how well it must do it. NFRs include performance (latency, throughput), availability, security, scalability, maintainability, and compliance. The critical insight is that NFRs are architectural drivers: a requirement for sub-100ms p99 latency eliminates entire classes of architecture, whereas a requirement for 99.9% availability dictates redundancy strategies long before a single line of code is written.

    Capacity estimation grounds requirements in reality. A useful technique is back-of-envelope calculation: if we expect 10 million daily active users, each making 5 requests per day, that is 500 million requests per day, roughly 5,800 requests per second at peak. This single calculation drives decisions about load balancing, database connection pools, and caching layers. Engineers who skip this step design systems that cannot handle production load or, conversely, over-engineer for traffic that will never arrive.

    SLAs (Service Level Agreements) are contractual commitments to customers, while SLOs (Service Level Objectives) are internal targets engineers maintain to provide headroom before SLA breach. Understanding the distinction matters: if your SLA promises 99.9% monthly availability (about 44 minutes of acceptable downtime), your internal SLO might target 99.95% to maintain a buffer. Breaking an SLA has legal and financial consequences; missing an SLO is an internal signal to act before consequences materialise.

    Stakeholder analysis reveals whose requirements matter, how much authority they carry, and where conflicts exist. A RACI matrix (Responsible, Accountable, Consulted, Informed) surfaces who must approve decisions. The danger is requirements that are technically consistent with each other but reflect conflicting stakeholder interests — for example, a security team demanding full audit logging of every action and a performance team demanding sub-5ms response times. Resolving this requires explicit negotiation, not technical cleverness alone.

    The cost of getting requirements wrong follows the well-established "shift left" principle: a requirement error discovered during analysis costs one unit to fix; the same error discovered during design costs 5x; during development costs 10x; during testing costs 25x; in production costs 100x or more. This economic reality is why professional engineers invest heavily in requirements workshops, user story mapping, and rapid prototyping before committing to architectures.
guidedSteps:
  - type: SHORT_TEXT
    prompt: "A new feature request states: 'The system should be fast and reliable.' Why is this insufficient as a requirement, and what questions would you ask to make it actionable?"
    hint: "Think about what 'fast' and 'reliable' mean in measurable terms — latency percentiles, error rates, uptime percentages."
  - type: MULTIPLE_CHOICE
    prompt: "Your system must handle 1 million users, each performing 10 actions per day. Approximately how many requests per second must you design for at peak load (assume 10x peak factor)?"
    options:
      - "About 12 requests/second"
      - "About 116 requests/second"
      - "About 1,200 requests/second"
      - "About 12,000 requests/second"
    correctIndex: 2
    feedback: "1M users × 10 actions = 10M actions/day ÷ 86,400 seconds ≈ 116 rps average. With a 10x peak factor ≈ 1,160 rps. Capacity estimation guides infrastructure sizing decisions early."
  - type: FILL_BLANK
    prompt: "An SLA promises customers 99.9% monthly uptime. Your internal engineers target 99.95%. The internal target is called an ___."
    answer: "SLO (Service Level Objective)"
    hint: "It is the internal version of the external commitment, providing a safety buffer."
  - type: SHORT_TEXT
    prompt: "Describe one real-world scenario where conflicting NFRs between two stakeholder groups would require an architectural tradeoff decision rather than a purely technical solution."
    hint: "Consider security vs. performance, consistency vs. availability, or compliance vs. simplicity."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the following is a non-functional requirement?"
    options: ["The system shall allow users to reset their password", "The system shall respond to search queries within 200ms at p99", "The system shall display a list of available products", "The system shall send an email confirmation after purchase"]
    correctIndex: 1
    feedback: "Non-functional requirements specify quality attributes like performance, reliability, and security — not capabilities. Sub-200ms p99 latency is a performance NFR that shapes architecture."
  - type: MULTIPLE_CHOICE
    question: "A system with a 99.9% monthly SLA has approximately how many minutes of acceptable downtime per month?"
    options: ["4.4 minutes", "44 minutes", "440 minutes", "4,400 minutes"]
    correctIndex: 1
    feedback: "99.9% uptime means 0.1% downtime. 30 days × 24h × 60min = 43,200 minutes × 0.001 ≈ 43.2 minutes. Senior engineers memorise these figures to quickly assess feasibility of commitments."
retrieval:
  recall: "Explain the difference between SLA, SLO, and SLI, and describe how they relate to each other in a production system."
  explain: "Why does the cost of fixing a requirements error increase so dramatically the later it is discovered? Use an architectural example to illustrate the compounding effect."
  mistakeId:
    code: |
      Requirements Document v1.0
      - The system must store user data
      - The system must be available
      - The system must handle high traffic
      - The system must be secure
      - The system must process payments
    answer: "Every requirement here is dangerously vague and unmeasurable. 'Available' — what SLA? 'High traffic' — how many requests per second? 'Secure' — against what threats? 'Store user data' — what retention policy, GDPR compliance, encryption at rest? These non-requirements will cause the team to build something, discover it doesn't meet unstated expectations, and spend 10x the cost fixing it post-launch. Every requirement must be specific, measurable, achievable, relevant, and time-bound (SMART). A senior engineer pushes back on vague requirements before writing a single line of design."
---

# Hook

You have just been handed a 300-page specification document for a new system. The deadline is in six months, the budget is fixed, and three different executive sponsors each believe their priorities are paramount. Sound familiar? Welcome to the reality of requirements analysis — the discipline where systems are won or lost before a single architectural decision is made. In this lesson, you will learn to separate the signal from the noise: translating vague stakeholder wishes into precise, testable requirements that drive defensible technical decisions.

# Lore Introduction

In the Arcane Academy, architects know that a blueprint drawn from flawed requirements is worse than no blueprint at all — it gives the illusion of direction while leading the entire guild toward a cliff. The most expensive bugs in software history are not coding errors; they are requirements errors discovered after a system ships. The engineers who distinguish themselves at the senior level are not those who write the most code, but those who ask the most precise questions before writing any. Requirements analysis is the architectural art of knowing what you are actually building before you build it.

# Core Learning

## Concept Introduction

Requirements analysis is the systematic process of eliciting, documenting, and validating what a system must do and how well it must do it. It sits at the intersection of business strategy, user needs, and technical feasibility — and it requires engineers who can reason in all three domains simultaneously.

Requirements split into two fundamental categories:

**Functional Requirements (FRs)** — capabilities the system must exhibit. A user must be able to log in. The system must process a payment. An admin must be able to suspend accounts. These describe behaviour.

**Non-Functional Requirements (NFRs)** — quality attributes the system must possess. The login endpoint must respond within 300ms at p99 under 1,000 concurrent users. The system must achieve 99.95% monthly availability. All data at rest must be AES-256 encrypted. These describe constraints and qualities that shape architecture more profoundly than FRs do.

The professional failure mode is treating NFRs as afterthoughts. An engineer who designs a database schema based on FRs alone, then learns the system needs sub-10ms reads at 100,000 QPS, must discard and redesign. NFRs are architectural drivers — they eliminate entire solution spaces before a line of code is written.

## Why It Matters

Wrong requirements are the highest-leverage failure point in software engineering. A coding bug costs hours to fix; a requirements bug discovered in production can cost months of rearchitecting plus the business consequences of degraded service. The economics are brutal and well-studied: fixing a requirements error during analysis costs roughly one unit of effort; the same error discovered post-deployment costs 50-100x that amount. This multiplier effect explains why mature engineering organisations invest heavily in requirements workshops, prototyping, and specification reviews before any design phase begins. Senior engineers earn their value not by coding faster, but by preventing entire categories of rework through rigorous upfront analysis.

## Worked Examples

**Example 1: Capacity Estimation**

A product manager states: "We expect 5 million users to use the feature in the first month." An inexperienced engineer nods and starts building. A senior engineer asks: "What is the peak concurrent load?"

Working through it:
- 5 million monthly users → roughly 167,000 daily active users (assuming ~3% daily conversion)
- 167,000 users × 10 requests/user/day = 1.67 million requests/day
- 1.67M ÷ 86,400 seconds = ~19 requests/second average
- Peak = 10x average → ~190 requests/second
- Add 3x safety buffer → design for ~600 requests/second

This single calculation tells you whether a single-instance deployment is viable, whether you need horizontal scaling from day one, and how large your database connection pool must be. Skipping it means discovering the answer in production.

**Example 2: SLA to Technical Constraint Translation**

A contract promises customers 99.9% monthly availability. What does this mean technically?

- 30 days × 24h × 60min = 43,200 minutes/month
- 0.1% downtime = 43.2 minutes of allowed downtime/month
- This means your deployment pipeline must complete in under 43 minutes total for the month, or you need zero-downtime deployment strategies (blue-green, rolling updates)
- Any single incident lasting more than 43 minutes exhausts the entire monthly budget
- Implication: you need automated alerting that fires within seconds of a failure, not minutes

The SLA immediately mandates architectural decisions: load balancers, health checks, automated failover, deployment strategies. None of this is optional — the contract demands it.

**Example 3: Stakeholder Conflict Resolution**

The security team mandates that every API call be logged with full request/response bodies for compliance auditing. The performance team mandates that API calls respond within 50ms at p99. These conflict: synchronous logging of large payloads to a centralised audit log adds 20-40ms of latency.

The resolution is not a technical trick — it is a business decision made explicit. Options:
1. Asynchronous audit logging (fire-and-forget, potential for log loss)
2. Accept 70-90ms p99 (renegotiate the performance requirement)
3. Selective logging (only log sensitive operations, accept reduced auditability)
4. Log to a local buffer with async flush (complexity tradeoff)

The senior engineer's role is to surface this tradeoff, quantify each option, and escalate to the stakeholders who have the authority to decide. The mistake is silently picking one without stakeholder awareness.

## Common Mistakes

- **Accepting vague NFRs without challenge.** "The system should be fast" is not a requirement. Every NFR must have a measurable threshold, a percentile or time window, and a load condition. Push back until you have numbers.
- **Conflating user wishes with verified requirements.** Stakeholders often describe solutions, not problems. "We need a mobile app" may actually mean "we need our customers to have offline access" — two very different requirements that produce very different architectures.
- **Ignoring operational requirements.** Deployment frequency, rollback time, monitoring, alerting, and on-call burden are requirements too. A system that is functionally correct but operationally unmaintainable fails in production.
- **Treating all requirements as equally binding.** Not all requirements are equal. MoSCoW (Must Have, Should Have, Could Have, Won't Have) or weighted priority matrices help distinguish non-negotiable constraints from nice-to-haves that can be deferred.
- **Stopping at the happy path.** Requirement workshops typically surface what the system does when everything works. Senior engineers explicitly probe failure scenarios, error conditions, and edge cases — because that is where systems actually fail.

## Mental Model

Think of requirements as a funnel with three stages:

1. **Elicitation** — drawing out what stakeholders need (interviews, workshops, shadowing)
2. **Analysis** — resolving conflicts, estimating feasibility, identifying gaps
3. **Specification** — writing requirements that are specific, measurable, and testable

Every requirement should answer: What must the system do or exhibit? Under what conditions? How will we verify it has been met? If a requirement cannot be tested, it is a wish, not a requirement.

## Mini Summary

- ✔ Functional requirements describe capabilities; non-functional requirements describe quality attributes that drive architecture
- ✔ Capacity estimation converts user numbers into concrete requests-per-second, storage, and bandwidth figures
- ✔ SLAs are contractual commitments; SLOs are internal targets providing headroom before SLA breach
- ✔ Stakeholder conflicts are resolved through explicit tradeoff analysis and escalation — not silent technical compromise
- ✔ The cost of requirements errors compounds dramatically with discovery time; shift-left is an economic argument
- ✔ Every requirement must be specific, measurable, and testable — vague requirements are future technical debt with interest

# Guided Practice Quest

Work through the guided steps above. For each step, engage with the requirement as if you were in a real design session — ask what is missing, what is ambiguous, and what the measurable threshold should be.

# Solo Practice Quest

You are the lead engineer for a new e-commerce checkout service. You have been given the following one-line brief: "Build a checkout service that handles our Black Friday traffic and never loses an order."

Write a requirements analysis covering:
1. At least four functional requirements with acceptance criteria
2. At least four non-functional requirements with measurable thresholds
3. A capacity estimation (assume 100,000 daily users with 50x peak on Black Friday)
4. One stakeholder conflict you anticipate and how you would resolve it
5. The two SLOs you would set and why

# Integration

**Mathematics connection:** Capacity estimation is applied probability and statistics. The peak-to-average ratio (often called the peak factor) requires understanding of traffic distribution over time — typically modelled as a Poisson process for random arrivals or a Gaussian distribution for predictable daily peaks. Little's Law (L = λW) gives you a direct relationship between arrival rate (λ), average time in system (W), and the number of concurrent requests in flight (L). Senior engineers who understand the statistical underpinnings of load can make more accurate estimates and design appropriate queuing strategies.

**Economics connection:** Requirements analysis is fundamentally a cost-of-information problem. The economic argument for investing in requirements is the same as the argument for any information-gathering activity: the cost of gathering the information must be less than the expected cost of acting without it. The "shift left" multiplier (1x to 100x cost increase) is a direct application of expected value calculation: if there is a 30% chance of a requirements error and fixing it in production costs $1M, the expected cost of skipping analysis is $300,000 — almost certainly more than the cost of thorough analysis. How do you balance the cost of perfect requirements against the value of shipping earlier? Is there an optimal stopping point?

# Lore Conclusion

The architect who spends two weeks on requirements and saves two months of rework is not a slow engineer — they are the most productive person in the room. Requirements analysis is where wisdom lives in software engineering. Every system that has failed expensively in production has, somewhere in its history, a moment where a vague requirement was accepted without challenge. Do not be that engineer. Be the one who asks the hard questions before the foundation is poured.
