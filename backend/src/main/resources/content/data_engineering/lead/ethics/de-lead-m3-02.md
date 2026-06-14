---
id: de-lead-m3-02
school: DATA_ENGINEERING
domainId: data_engineering
tier: LEAD
moduleId: de-lead-m3
moduleTitle: "Module 3: Data Governance & Ethics"
moduleGlyph: "⚖️"
moduleSortOrder: 3
topicSlug: ethics
topicTitle: "Ethics"
topicSortOrder: 2
lesson: 2
title: "Ethics at Scale: Governing Data Use Across an Organisation"
sortOrder: 2
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
  - de-lead-m3-01
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between individual ethical judgment and organisational ethics infrastructure"
    - "Describes the components of an ethics review framework for data decisions"
    - "Identifies the Lead's role in creating ethical accountability structures"
    - "Explains how to embed ethics review into the engineering process"
  keywords:
    - ethics review
    - ethical accountability
    - data ethics board
    - impact assessment
    - duty to report
    - ethical refusal
    - stakeholder harm
  modelAnswer: |
    Individual ethical judgment is necessary but insufficient at scale. An organisation where ethics depends entirely on individual engineers to notice and raise problems will miss issues — individuals have incomplete information, face social pressure to comply, and may not recognise subtle harms. Organisational ethics infrastructure creates consistent processes, clear accountability, and safe channels for raising concerns.
    An ethics review framework for data decisions includes: harm identification (who is affected and how could they be harmed?), stakeholder representation (are affected communities consulted?), power asymmetry analysis (does this data use create or reinforce power imbalances?), consent adequacy (did affected parties genuinely understand and agree?), redress mechanism (can affected parties challenge or appeal?), and monitoring plan (how will outcomes be tracked post-deployment?).
    The Lead's accountability structure responsibilities: establish a data ethics review committee (with diverse membership including non-technical stakeholders); create clear criteria for when review is required; protect engineers who raise ethical concerns; model ethical decision-making publicly; and ensure ethics review is upstream of shipping, not downstream of complaints.
    Embedding ethics in the engineering process: require ethics review as a pre-condition for certain categories of data project (ML affecting individuals, large-scale monitoring, sensitive data processing); include ethics checklist in RFC/design doc templates; give engineers explicit authority to pause projects pending ethics review; and document ethical decisions alongside technical decisions.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "An engineer notices that a new A/B test design will show a control group a deliberately degraded experience (removing helpful features) to measure their worse outcomes. No data ethics review was conducted. What should the engineer do and what organisational structure should exist?"
    options:
      - "Ship it — A/B tests are standard practice and this is a business decision"
      - "Raise a concern in the PR comment and continue if no response within 24 hours"
      - "Use their authority to pause the project, escalate to the ethics review committee, and ensure the design is reviewed before shipping"
      - "Refuse to participate without written sign-off from legal"
    correctIndex: 2
    explanation: "Deliberately degrading users' experience for experimental control — without their knowledge or consent — raises significant ethical concerns (deception, harm without consent). The engineer has both a professional responsibility to raise this and, in a well-governed organisation, explicit authority to pause the project pending ethics review. A review committee should evaluate: is the harm proportionate to the scientific value? Was there a less harmful control design? Is there consent (or a lawful basis)? The organisation's ethics infrastructure should make this escalation path clear, safe, and normal — not an exception requiring personal courage."
  - type: FILL_BLANK
    question: "A ___ is a structured pre-deployment review for data projects that could significantly affect individuals or communities, examining potential harms, consent, fairness, and redress mechanisms."
    answer: "data ethics review (or Data Ethics Impact Assessment)"
    explanation: "A data ethics review (analogous to an Environmental Impact Assessment or DPIA) systematically evaluates whether a proposed data use is ethical — beyond just legal compliance. It requires identifying all affected stakeholders, potential harms (including indirect and long-term), whether consent is genuine, whether outcomes will be equitable, and what mechanisms exist for challenge and correction. It is conducted before deployment, not after a complaint."
  - type: SHORT_TEXT
    question: "A data science team argues that an ethics review 'slows us down and is just bureaucracy.' How do you respond as Lead Data Engineer?"
    modelAnswer: "Acknowledge the real cost: 'Yes, ethics review takes time — typically 2–4 days for a standard review. That's real.' Then reframe the trade-off: 'The alternative is discovering a harmful outcome after deployment — to real users. Fixing it then costs more engineering time, creates regulatory risk, and damages trust that takes years to rebuild. The 2-4 days is insurance against a much larger cost.' Make the process less bureaucratic: 'Let me show you the process — it's a structured conversation, not a committee of lawyers. If your project is low-risk, the review is 30 minutes. The onerous reviews are for genuinely high-risk projects.' Finally: 'Ethics review is not optional for projects in categories X, Y, Z — but I'll work with you to design the smallest, fastest review that gives us genuine confidence.'"
microCheckpoint:
  question: "Why is individual ethical judgment insufficient as the sole mechanism for ethical data use in an organisation?"
  answer: "Individuals have incomplete information about downstream consequences, face social and career pressure to comply with requests, may not recognise subtle harms, and have inconsistent ethical frameworks. Organisational ethics infrastructure creates consistent processes, safe escalation channels, diverse review perspectives, and accountability mechanisms that are more robust than individual judgment alone."
retrieval:
  recall: "What components should a data ethics review framework include?"
  explain: "Explain what it means to give engineers 'explicit authority to pause' a project and why this authority must be formal, not just cultural."
  mistakeId: "ethics-individual-only"
---

# The Degraded Control

The A/B test design arrived in the engineering sprint: Group A would see the standard learning interface; Group B would have three key features removed — no progress tracker, no XP display, no completion celebration. The purpose: measure how much worse engagement was without those features, to justify their continued development cost. The Lead Data Engineer read the spec. "We're deliberately harming users in Group B to run this experiment. Have we run an ethics review?" The product manager looked uncomfortable. "It's standard A/B testing."

# Individual Ethics vs Institutional Ethics

```
Individual ethics (necessary but insufficient):
  Relies on each engineer noticing and raising concerns
  Depends on personal courage to challenge senior requests
  Inconsistent — different engineers draw lines differently
  Invisible — ethical judgments made informally, undocumented

Institutional ethics (required at scale):
  Clear criteria for when review is required
  Safe, formal escalation channels
  Documented ethical decisions alongside technical ones
  Diverse review including non-technical perspectives
  Accountability for outcomes, not just intent
```

## When Ethics Review Is Required

Define categories of data project that always require ethics review:

```
Category A (mandatory review before any work begins):
  ├── ML systems making decisions that affect individuals' outcomes
  │   (loan approval, content ranking, flagging for human review)
  ├── Large-scale monitoring of individuals' behaviour
  ├── Processing of special-category data (health, biometric, political)
  ├── Data sharing with third parties outside the organisation
  ├── Research involving human subjects
  └── A/B tests that involve withholding beneficial features or
      deliberately degrading user experience

Category B (lightweight review):
  ├── New analytics uses of existing internal data
  ├── Expanding access to sensitive datasets
  └── Automated decision-support systems (recommendations)

Category C (ethics checklist, no formal review):
  └── Internal operational analytics and reporting
```

## The Ethics Review Framework

```
HARM IDENTIFICATION
  Who is directly affected by this data use?
  Who is indirectly affected (families, communities)?
  What harms could result from:
    - False positives? (wrongly flagged, restricted, denied)
    - False negatives? (missed warning, under-served)
    - Data misuse if accessed by bad actors?
    - Long-term cultural or social effects?

CONSENT ADEQUACY
  Did affected parties genuinely understand what they agreed to?
  Was consent freely given (no coercion, genuine alternative)?
  Does this use fall within what was agreed to?

POWER ASYMMETRY
  Does this create or reinforce power imbalances?
  Do affected parties have genuine recourse?
  Are vulnerable populations disproportionately exposed?

STAKEHOLDER REPRESENTATION
  Are affected communities represented in the design?
  Was the ethics review diverse (not just engineers)?
  Were domain experts in affected communities consulted?

FAIRNESS AUDIT
  Are outcomes equitable across protected groups?
  Has the model/system been tested for disparate impact?

REDRESS MECHANISM
  Can affected individuals challenge or appeal a decision?
  Is there a human review path for contested outcomes?
  Are errors corrected promptly and transparently?

MONITORING PLAN
  How will outcomes be tracked post-deployment?
  What triggers a re-evaluation?
  Who is accountable for ongoing ethical compliance?
```

## Embedding Ethics in Engineering Process

```yaml
 # Engineering RFC template addition
ethics_review:
  required: true  # if project is Category A or B
  reviewer: data-ethics-committee@consortium.io
  
  harm_assessment:
    directly_affected: "learners in Group B"
    potential_harms:
      - "Degraded learning experience during experimental period"
      - "Loss of engagement motivation without XP feedback"
    harm_proportionality: "LOW" # is the scientific value worth the harm?
    
  consent:
    basis: "Terms of service (§4.2 — Consortium may conduct user research)"
    adequacy: "UNCERTAIN — ToS consent for 'user research' may not cover
               deliberate feature degradation without disclosure"
    
  alternative_designs:
    considered:
      - "Compare new interface vs old interface (existing users vs new users)"
      - "Use historical data (no active harm to control group)"
    rejected_because: "Cross-cohort comparison has confounders"
    
  ethics_committee_decision: PENDING
```

## The Engineer's Authority to Pause

In a well-governed organisation, engineers have **explicit authority** to pause a project pending ethics review.

```
Why explicit authority matters:
  Cultural authority:  "I think we should review this"
    → Manager says "we're shipping Friday, review after"
    → Engineer faces social pressure; often complies
    
  Formal authority:    "This project meets Category A criteria.
                        I am pausing it pending ethics review.
                        Review committee has been notified."
    → Manager cannot override without formal escalation
    → Engineer is protected from retaliation
    → The decision is documented
    
How to implement:
  ✓ Define engineer authority in engineering policy (not just in culture docs)
  ✓ Create a formal "ethics pause" workflow in JIRA/Linear
  ✓ Protect engineers who use this authority from career consequences
  ✓ Ensure CDO/CTO explicitly backs this authority publicly
```

## Common Mistakes

> **Ethics Review After Shipping**
> Conducting an ethics review after a complaint has been filed means real users have already been affected. Ethics review must be upstream of shipping, not downstream of incidents.

> **Non-Diverse Review**
> An ethics committee composed entirely of white male engineers in their 30s will have systematic blind spots for harms affecting other demographics. Include legal, communications, customer success, and ideally external ethics advisors.

> **Treating Ethics as a Legal Minimum**
> "Legal says it's fine" is not an ethics review. Legal evaluates legality; ethics evaluates rightness. These are not the same. The legal team's sign-off closes the compliance question, not the ethics question.

## Mental Model

Think of organisational ethics as **building safety standards**. Individual architects and engineers have personal judgment about safety. But building codes, mandatory inspections, and certified structural reviews exist because individual judgment is insufficient — it's inconsistent, it can be overridden by business pressure, and it may miss failure modes outside individual experience. The Grenfell Tower fire happened in a regulated environment where individual judgments were made that seemed reasonable in isolation but failed systemically. Organisational data ethics infrastructure is the equivalent of building codes: structured, mandatory for high-risk decisions, diverse in its review, and protective of both users and engineers.

**Mini Summary**: Individual ethics is necessary but insufficient — organisational ethics infrastructure creates consistent, accountable, and safe processes. Define clear categories for when ethics review is required. The review framework covers: harm identification, consent adequacy, power asymmetry, stakeholder representation, fairness, redress, and monitoring. Embed ethics review in the engineering process (RFC templates, JIRA workflows). Give engineers explicit formal authority to pause projects — cultural authority alone is insufficient under social pressure.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium wants to implement a "learning risk score" that automatically flags learners predicted to disengage, routing them to a specialised intervention team. The score is computed from: engagement frequency, completion rates, XP trajectory, and inferred time-of-day patterns.

Conduct a full ethics review:
1. Classify the project (Category A, B, or C) and justify.
2. Complete the harm assessment: who is affected, what could go wrong for false positives and false negatives?
3. Assess consent adequacy: do learners know this scoring exists?
4. Propose redress mechanisms: what can a learner do if they believe the score is wrong?
5. What ongoing monitoring would you require post-deployment?

---

# Integration

**Mathematics**: Ethical risk assessment applies **expected harm analysis** — the moral equivalent of expected value calculation. Expected harm E[H] = Σ P(outcome_i) × severity(harm_i). For a learning risk score: P(false positive) × harm(wrongly flagged, intervention wasted) + P(false negative) × harm(missed intervention, learner disengages). The ethical question is not just whether E[H] is low but who bears the harm. If false positives disproportionately affect a protected group, the distribution of harm is unjust even if the aggregate E[H] is low. This is the mathematical foundation for fairness constraints: E[H | group=A] ≤ k × E[H | group=B] for some fairness constant k. Ethics review formalises this distributional analysis.

**Sciences**: Institutional ethics mirrors the **medical ethics framework** developed after historical research abuses (Nuremberg Code, Belmont Report). Research ethics evolved from individual physician judgment to institutional review boards (IRBs) with mandatory pre-study review, informed consent requirements, vulnerability protections, and ongoing monitoring. The evolution was driven by documented failures of individual judgment under institutional pressure. Data ethics is undergoing the same evolution — moving from "engineers deciding what's ethical individually" to institutional review, consent frameworks, and accountability structures. The Belmont Report's three principles (respect for persons, beneficence, justice) directly map to data ethics: consent adequacy, harm minimisation, and fairness across populations.

---

# The Redesigned Experiment

The A/B test was redesigned after the ethics review. The control group received the current interface; the treatment group received an enhanced interface with additional features. Users who gained features were not harmed. The scientific question — does XP tracking improve engagement? — was still answerable. The ethics committee had not blocked the experiment. It had improved it. "A well-designed ethics review makes you build better things," the Lead Data Engineer said. "Not fewer things."
