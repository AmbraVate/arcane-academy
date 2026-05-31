---
id: se-sen-m5-06
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m5
moduleTitle: "Module 5: Security"
moduleGlyph: "🔐"
moduleSortOrder: 5
topicSlug: threat_modelling
topicTitle: "Threat Modelling"
topicSortOrder: 6
lesson: threat_modelling
title: "Threat Modelling"
sortOrder: 6
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [owasp_fundamentals]
integrationDomains: [psychology, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains the four questions of threat modelling (what/who/how/what to do)"
    - "Applies STRIDE categories to identify threats on a given system"
    - "Draws or describes a data flow diagram with trust boundaries"
    - "Prioritises threats by likelihood and impact"
    - "Proposes a concrete mitigation for each identified threat"
  keywords: [stride, spoofing, tampering, repudiation, disclosure, denial, elevation, trust, boundary, data flow]
  modelAnswer: |
    Threat modelling for a login endpoint using STRIDE:

    System: POST /auth/login accepts username + password, returns JWT

    S - Spoofing: attacker brute-forces passwords
      Mitigation: rate limiting, account lockout, MFA

    T - Tampering: attacker modifies the JWT payload to elevate privileges
      Mitigation: sign JWTs with RS256; verify signature on every request

    R - Repudiation: user denies making a login attempt
      Mitigation: log all auth attempts with IP, timestamp, outcome

    I - Information Disclosure: error message reveals whether email exists
      Mitigation: return same message for wrong email and wrong password

    D - Denial of Service: flood login endpoint to lock out legitimate users
      Mitigation: rate limiting per IP + CAPTCHA after N failures

    E - Elevation of Privilege: JWT with forged admin role claim
      Mitigation: validate role from database, not JWT claim alone
guidedSteps:
  - id: tm-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In STRIDE, what does the 'I' stand for and what type of attack does it represent?
    inputConfig:
      options:
        - "Integrity — modifying data in transit"
        - "Information Disclosure — exposing data to unauthorised parties"
        - "Impersonation — pretending to be another user"
        - "Injection — sending malicious data to an interpreter"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Information Disclosure — exposing data to unauthorised parties"]
      rejectedFeedback: "STRIDE: Spoofing, Tampering, **Repudiation**, **Information Disclosure**, Denial of Service, Elevation of Privilege. Information Disclosure covers any scenario where sensitive data is exposed to those who shouldn't have it — via insecure APIs, verbose errors, log exposure, etc."
    hint: "STRIDE = Spoofing, Tampering, Repudiation, Information Disclosure, Denial of Service, Elevation of Privilege."
    reflectionPrompt: "Each STRIDE category maps to a security property it violates: Spoofing→Authentication, Tampering→Integrity, Repudiation→Non-repudiation, Information Disclosure→Confidentiality, DoS→Availability, EoP→Authorisation."
  - id: tm-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In a Data Flow Diagram used for threat modelling, a ___ boundary is a line
      separating areas of different trust levels — for example, between the internet
      and your backend, or between your app and the database.
    inputConfig:
      placeholder: "two words"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["trust boundary", "trust", "trust boundary line"]
      rejectedFeedback: "A **trust boundary** separates zones of different trust. Data crossing a trust boundary must be validated and possibly authorised. Common trust boundaries: internet→API, API→database, human user→system, internal service→external service."
    hint: "It's where trust changes — between components that trust each other and those that don't."
    reflectionPrompt: "Every data flow crossing a trust boundary is a potential attack surface. Threat modellers focus on these crossings: what data flows across? Who controls the sender? What could an adversary do with the ability to influence that data?"
  - id: tm-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      When should threat modelling be done — at the beginning of a project, during development, or before release? Explain your answer and describe what changes if it's done too late.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [design, early, architecture, expensive, change, retrofit, requirements, before, build, costly]
      rejectedFeedback: "Threat modelling is most valuable at design time — before the system is built. Like all quality activities, security issues found late are exponentially more expensive to fix. A design-level threat ('we need MFA') is a requirement; a post-deployment threat is an incident. Retrofitting security controls into built systems is expensive and error-prone."
    hint: "When is it cheapest to change an architecture? When is it most expensive?"
    reflectionPrompt: "Security, like accessibility, is far cheaper to design in than to bolt on. A threat model during architecture identifies missing security controls before code is written. The same finding post-deployment requires patching a live system under time pressure."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What are the four questions that structure a threat modelling session?"
    options:
      - "Who, What, When, Where"
      - "What are we building, What can go wrong, What do we do about it, Did we do a good job?"
      - "Build, Test, Deploy, Monitor"
      - "Identify, Analyse, Respond, Review"
    correctIndex: 1
    feedback: "The four questions (Shostack's approach): (1) What are we building? (DFD); (2) What can go wrong? (STRIDE threats); (3) What do we do about it? (mitigations); (4) Did we do a good job? (retrospective). These structure any threat modelling session regardless of team or methodology."
  - type: MULTIPLE_CHOICE
    question: "In STRIDE, 'Elevation of Privilege' means an attacker can:"
    options:
      - "Crash the service to deny access to others"
      - "Gain capabilities or access beyond what they are authorised to have"
      - "Observe data they shouldn't be able to see"
      - "Deny having performed an action"
    correctIndex: 1
    feedback: "Elevation of Privilege (EoP): gaining access beyond authorisation — a regular user gaining admin access, forging tokens with higher-privilege claims, or exploiting bugs to execute code at a higher privilege level."

retrieval:
  recall: "What does STRIDE stand for and what security property does each category threaten?"
  explain: "Explain to a product manager why threat modelling should be on the agenda before development starts, not after."
  mistakeId:
    code: |
      // Threat model finding: "Users can tamper with their order total"
      // Decision: "We'll fix this after launch if anyone reports it"
      // Reasoning: "It's unlikely anyone will exploit this"
    answer: "This is the 'it won't happen to us' fallacy. Financial fraud is highly motivated — any exploitable price manipulation will be found and exploited, quickly. The mitigation is straightforward (validate order totals server-side, never trust client-submitted prices). Deferring known vulnerabilities without a fix date is unacceptable for financial data."
---

# Hook

You've built a feature. You've written tests. You've done a code review. But has anyone asked: "How could an adversary abuse this feature?"

That's threat modelling — systematically asking what could go wrong from an attacker's perspective, before the attacker does.

Most security incidents don't exploit zero-days. They exploit predictable vulnerabilities that would have been found in a thirty-minute threat modelling session.

> Think of a feature you've built recently. What are the three most obvious ways an attacker could abuse it?

# Lore Introduction

Before the Academy authorises a new enchantment for public use, the senior artificers gather for the Adversarial Review. Each enchantment is examined not for what it does, but for what it could be made to do against its intent.

*"Build as a friend,"* Archmage Veylan says. *"Review as an enemy. Only by thinking like an adversary can you build something they cannot corrupt."*

# Core Learning

## Concept Introduction

**Threat modelling** is a structured approach to identifying potential security threats, their impact, and appropriate mitigations — ideally during the design phase.

**Four core questions (Adam Shostack):**
1. What are we building? → Data Flow Diagram
2. What can go wrong? → STRIDE analysis
3. What do we do about it? → Mitigations
4. Did we do a good job? → Review

**STRIDE threat categories:**

| Letter | Threat | Violates |
|--------|--------|---------|
| S | Spoofing | Authentication |
| T | Tampering | Integrity |
| R | Repudiation | Non-repudiation |
| I | Information Disclosure | Confidentiality |
| D | Denial of Service | Availability |
| E | Elevation of Privilege | Authorisation |

## Why It Matters

- Finds architectural security gaps before code is written (cheapest fix point)
- Produces a shared security understanding across the team
- Generates testable security requirements, not vague "be secure"
- Ensures security controls are proportionate to actual risks
- Creates documented risk decisions for future maintainers

## Worked Examples

**Data Flow Diagram (textual):**
```
[User Browser] --HTTPS--> [Load Balancer] --HTTP--> [App Server]
                                                          |
Trust boundary: internet | internal                       v
                                              [Database (private subnet)]

Trust boundaries:
- Internet ↔ Load Balancer
- Load Balancer ↔ App Server (internal)
- App Server ↔ Database (internal)
```

**STRIDE analysis on password reset:**
```
S - Spoofing: attacker resets password for another user's account
  → Mitigation: tokens tied to specific user ID, short TTL

T - Tampering: token tampered to reference different user
  → Mitigation: HMAC-signed token (can't tamper without key)

R - Repudiation: user denies requesting reset
  → Mitigation: log reset requests with IP and timestamp

I - Info Disclosure: reset token in server logs or referer header
  → Mitigation: never log tokens; send only via email

D - DoS: flood reset endpoint to lock target account
  → Mitigation: rate limit per email and per IP

E - EoP: reset link used to access admin account
  → Mitigation: verify role at login time regardless of reset flow
```

## Common Mistakes

- **Threat modelling after release** — mitigations become incidents, not design changes.
- **Only developers in the room** — product, QA, and ops often see threats developers miss.
- **STRIDE becoming a checkbox** — the goal is mitigations, not a completed spreadsheet.
- **Ignoring low-likelihood, high-impact threats** — a single admin account compromise can be catastrophic.
- **No follow-up** — threats identified but never turned into actionable tickets.

## Mental Model

Threat modelling is **red team thinking at design time**. You sit at the table as both architect and attacker simultaneously — building the system while asking "if I wanted to break this, how would I?" The dual perspective reveals gaps invisible to purely constructive thinking.

## Mini Summary

- ✔ STRIDE: six threat categories covering authentication, integrity, repudiation, confidentiality, availability, authorisation
- ✔ Data Flow Diagrams identify trust boundaries — every crossing is a potential attack surface
- ✔ Four questions: what are we building, what can go wrong, what do we do, did we do well
- ✔ Threat model at design time — retrofitting security controls is expensive and incomplete
- ✔ Produce actionable mitigations, not just threat lists

# Guided Practice Quest

**The Adversarial Review**

A new password reset feature is up for Adversarial Review. Apply STRIDE to identify its threats and propose mitigations for each.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are designing a file upload service. Users can upload images for their profile. The service:
1. Accepts the file via `POST /api/profile/avatar`
2. Stores the file in a cloud storage bucket
3. Saves the storage URL in the database
4. Serves the file via `GET /api/files/{filename}`

Create a STRIDE threat model for this feature:
1. Draw (or describe) the data flow diagram with trust boundaries
2. For each STRIDE category, identify at least one specific threat against this feature
3. For each threat, propose a concrete mitigation
4. Identify which two threats you consider highest priority and explain why

# Integration

**Connecting to Psychology — Adversarial Thinking and the Red Team**

Psychologist Gary Klein's research on expert decision-making found that experts don't evaluate options linearly — they mentally simulate scenarios. Military planners use "Red Teams" — groups tasked with finding flaws in plans by thinking like the adversary. Intelligence agencies do "competitive analysis" — actively trying to disprove their own assessments.

Threat modelling is organised adversarial thinking. The psychological challenge is that it requires a mental perspective shift: from builder (motivated to see the system working) to attacker (motivated to find failure). Research suggests that teams are better at this with explicit permission — "your job right now is to find what's wrong." Without that permission, constructive framing dominates.

This is partly why external security audits find more than internal ones: the external team has no emotional investment in the system working. Internal threat modelling can compensate by deliberately assigning the adversarial role, giving team members explicit permission to attack their own work without professional judgment.

How might you run a threat modelling session that maximises the quality of adversarial thinking in your team?

# Lore Conclusion

The Adversarial Review ends. Three threats found, three mitigations designed. The enchantment is stronger for being challenged.

*"You cannot build defences against threats you have not named,"* Archmage Veylan says. *"And you cannot name threats you have not imagined. This is why we gather and think like adversaries — because the real adversaries are already doing so."*

Security is not the absence of vulnerabilities. It is the practice of finding them before others do.
---
