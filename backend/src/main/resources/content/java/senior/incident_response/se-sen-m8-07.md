---
id: se-sen-m8-07
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m8
moduleTitle: "Module 8: Observability"
moduleGlyph: "🔭"
moduleSortOrder: 8
topicSlug: incident_response
topicTitle: "Incident Response"
topicSortOrder: 7
lesson: incident_response
title: "Incident Response"
sortOrder: 7
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [reliability_engineering]
integrationDomains: [psychology, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Describes the incident lifecycle: detect, triage, mitigate, resolve, review"
    - "Explains the incident commander role and why a single coordinator matters"
    - "Describes appropriate communication during an incident (status page, stakeholders)"
    - "Explains what a post-mortem should contain (timeline, contributing factors, action items)"
    - "Articulates the blameless culture principle and why it enables learning"
  keywords: [incident, commander, triage, mitigate, resolve, timeline, postmortem, communicate, escalate, blameless]
  modelAnswer: |
    Incident lifecycle:
    1. Detect: alert fires or user reports
    2. Triage: assess impact and severity (P1/P2/P3)
    3. Mitigate: stop the bleeding (rollback, disable feature, scale up)
    4. Resolve: fix root cause
    5. Review: blameless post-mortem, action items

    Incident Commander (IC) role:
    - Single coordinator; others execute
    - IC doesn't fix; IC coordinates
    - Assigns responders, tracks progress, communicates to stakeholders
    - Reduces cognitive load for technical responders

    Communication:
    - Status page update within 15 minutes of P1
    - Slack incident channel for async coordination
    - Executive summary every 30 minutes during major incidents
    - Never speculate publicly — only confirmed facts

    Post-mortem:
    - Timeline: what happened when
    - Contributing factors: why (systems, processes, not people)
    - Action items: concrete, assigned, with deadlines
guidedSteps:
  - id: ir-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A payment service is down. Three engineers are simultaneously investigating
      different theories about the cause. No one is coordinating. What role is missing?
    inputConfig:
      options:
        - "A database administrator"
        - "An incident commander who coordinates response and avoids duplicated effort"
        - "A security analyst"
        - "A product manager"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["An incident commander who coordinates response and avoids duplicated effort"]
      rejectedFeedback: "The Incident Commander (IC) role is a coordinator, not a fixer. The IC assigns investigators to specific theories, tracks progress, communicates to stakeholders, and ensures the team isn't duplicating effort. Without an IC, chaos degrades response speed significantly."
    hint: "Three people investigating independently without coordination is inefficient. What's needed?"
    reflectionPrompt: "The IC's job: 'You investigate the database. You look at the latest deployment. I'll update the status page and talk to the VP. Check back in 10 minutes.' This division of labour reduces cognitive load for technical responders and ensures nothing is missed."
  - id: ir-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      During an incident, the first priority is not to find the root cause but to
      ___ the impact — stop the harm to users as quickly as possible.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["mitigate", "reduce", "stop", "limit"]
      rejectedFeedback: "**Mitigate** first: stop the user-facing harm immediately, even without knowing why it happened. Rollback the deployment, disable the feature flag, scale up the database. Root cause investigation comes *after* users are no longer affected. Don't wait to understand everything before acting."
    hint: "What's the most important thing to do first for users? (It's not finding the root cause)"
    reflectionPrompt: "Mitigation vs resolution distinction: mitigation stops the immediate harm (often fast — rollback). Resolution fixes the underlying cause (often slower — requires understanding). Users care about mitigation. Engineers care about both."
  - id: ir-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      During a major incident, how and when should you communicate with stakeholders (users, management, affected teams)? What should you never say publicly during an incident?
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [status, page, update, speculate, facts, regular, timeline, acknowledge, communicate, confirmed]
      rejectedFeedback: "Communicate early and often: status page update within 15 minutes of P1 detection, regular updates every 30 minutes. Never speculate about cause publicly — share only confirmed facts. Acknowledge the problem immediately; uncertainty is OK to communicate. Silence is worse than 'we're investigating'."
    hint: "What do users and stakeholders need to know? What would make things worse to say publicly?"
    reflectionPrompt: "Communication during incidents is about trust maintenance. Users need to know you know about the problem. Stakeholders need to know someone is in charge. Regular updates signal competence. Silence signals chaos."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the correct order of the incident response lifecycle?"
    options:
      - "Detect → Fix → Communicate → Review"
      - "Detect → Triage → Mitigate → Resolve → Review"
      - "Alert → Rollback → Post-mortem → Deploy"
      - "Triage → Detect → Mitigate → Deploy"
    correctIndex: 1
    feedback: "Detect (alert fires) → Triage (assess severity/impact) → Mitigate (stop the harm) → Resolve (fix root cause) → Review (post-mortem). Mitigation comes before resolution — stop the user pain first, then investigate properly."
  - type: MULTIPLE_CHOICE
    question: "A post-mortem says: 'Root cause: Engineer X deployed without proper testing.' Is this a good post-mortem finding?"
    options:
      - "Yes — clearly identifies who is responsible"
      - "No — it blames an individual rather than identifying the systemic failure that allowed this to happen"
      - "Yes — it gives clear action items"
      - "No — it doesn't include a timeline"
    correctIndex: 1
    feedback: "Blaming an individual is not a root cause — it's a symptom. The systemic question: why was it possible to deploy without proper testing? Were there missing CI checks? Insufficient staging? Unclear deploy process? Fix the system so the next person cannot make the same error."

retrieval:
  recall: "Describe the five phases of incident response. What is the incident commander responsible for?"
  explain: "Explain to a new engineer why the first priority during an incident is mitigation (stopping the harm), not understanding the root cause."
  mistakeId:
    code: |
      // Post-incident communication to users:
      "We experienced a brief hiccup due to a misconfigured database query
       introduced by a junior developer. We believe this is likely a one-off
       and probably won't happen again. Service should be back to normal now,
       we think."
    answer: "Multiple problems: (1) Blames a 'junior developer' publicly — inappropriate. (2) 'We believe', 'likely', 'probably', 'we think' — speculative language undermines confidence. (3) 'Brief hiccup' minimises a real outage. Better: 'We experienced a service disruption from [time] to [time] affecting [what]. Service has been restored and is fully operational. We are investigating the root cause and will provide a follow-up. We apologise for the impact.'"
---

# Hook

It's 3pm on a Friday. The payment service is down. Error rate is 100%. Money isn't moving. Three engineers are in Slack, all investigating different theories. No one is talking to users. No one is updating the status page. The VP of Engineering is asking "what's happening?" in a different thread. Meanwhile, five more engineers are joining the channel and asking "should I look at the database?"

This is an incident without incident management. The technical problem may be solvable in 20 minutes. The coordination failure makes it take three hours.

Incident response is a skill. Like all skills, it must be deliberately practised.

> Think about the last time something went wrong in a system you worked on. Who was coordinating? What wasn't communicated that should have been?

# Lore Introduction

When the Academy's protective ward network fails, there is always an Incident Warden. Not the most skilled artificer — the most experienced coordinator. While other artificers diagnose and repair, the Warden communicates to the Grand Council, assigns tasks, tracks progress, and ensures the right people are working the right problem.

*"Competent individuals working without coordination,"* Archmage Veylan says, *"often produce a worse outcome than a lesser team with good coordination. Incidents require both technical skill and operational discipline."*

# Core Learning

## Concept Introduction

**Incident Response** is the structured process for detecting, managing, and learning from system failures.

**Incident lifecycle:**
1. **Detect** — alert fires or user reports
2. **Triage** — assess severity (P1/P2/P3) and impact scope
3. **Mitigate** — stop user-facing harm immediately (rollback, disable, scale)
4. **Resolve** — fix root cause
5. **Review** — blameless post-mortem, action items

**Severity levels:**
| Level | Definition | Response |
|-------|-----------|----------|
| P1 | Critical: service down, data loss | Immediate, all hands |
| P2 | Significant: major feature broken | Urgent within 30 min |
| P3 | Minor: degraded, not blocking | Business hours |

## Why It Matters

Good incident response:
- Reduces time-to-mitigation (user impact minimised)
- Maintains trust through consistent communication
- Produces learning through systematic post-mortems
- Builds a culture that handles incidents calmly rather than chaotically
- Turns failure into system improvement

## Worked Examples

**Incident Commander checklist:**
```
1. Declare incident severity (P1/P2/P3)
2. Create incident Slack channel: #incident-2026-01-15
3. Assign roles: investigator(s), comms lead
4. Update status page: "Investigating issues with payment processing"
5. Coordinate investigation — one hypothesis at a time
6. Update stakeholders every 30 minutes
7. Declare mitigation when user impact stops
8. Declare resolution when root cause fixed
9. Schedule post-mortem within 48 hours
```

**Status page communication template:**
```
Status: Investigating
Updated: 14:45 UTC

We are aware of issues affecting payment processing. Users may experience 
failures when attempting to complete purchases.

We are actively investigating and will provide an update by 15:15 UTC.
Impact: Approximately 30% of checkout attempts are failing.
```

**Post-mortem timeline section:**
```markdown
| Time  | Event |
|-------|-------|
| 14:32 | Alert: payment error rate > 5% |
| 14:37 | On-call acknowledges |
| 14:45 | IC declared; investigation assigned |
| 15:02 | Root cause identified: OOM kill |
| 15:10 | Mitigation: service restarted |
| 15:12 | Alert resolved; user impact ends |
| 17:45 | Root cause fixed: connection pool limit added |
```

## Common Mistakes

- **No incident commander** — everyone investigates, nobody coordinates; chaos.
- **Fixing before mitigating** — trying to understand why before stopping the harm.
- **No status page update** — users and stakeholders are left in the dark.
- **Public speculation** — "we think it might be the database" before confirmation damages trust.
- **Blame in post-mortems** — removes psychological safety; honest reporting stops.
- **No action items** — post-mortem produces insight but nothing changes.

## Mental Model

Incident response is **emergency medicine triage**. A&E triage doesn't diagnose before treating — it stabilises the patient first (mitigation), then investigates the cause. The incident commander is the attending physician coordinating the team. Specialists handle specific systems. Communication to family (stakeholders) is separate from treatment. And after every significant case, there's a case review to learn.

## Mini Summary

- ✔ Incident lifecycle: Detect → Triage → Mitigate → Resolve → Review
- ✔ Incident Commander coordinates; technical responders investigate — never conflate these roles
- ✔ Mitigate (stop the harm) before investigating root cause
- ✔ Communicate early and often: status page, stakeholders, regular updates — no speculation
- ✔ Blameless post-mortems fix systems; action items must be assigned and tracked

# Guided Practice Quest

**The Incident Warden**

A payment outage is in progress. Take the IC role: prioritise the timeline, assign responders, draft communications, and structure the post-mortem.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

At 09:15 on a Wednesday, your monitoring alerts fire:
- p99 latency on `/api/checkout` spiked from 200ms to 8 seconds
- Error rate on checkout is 45%
- Your most recent deployment was at 08:50

You are the first engineer to see the alert.

Write out:
1. Your immediate actions in the first 5 minutes (exact Slack messages, commands you'd run, decisions you'd make)
2. How you'd decide whether this is P1, P2, or P3 and what that decision triggers
3. What you'd put on the status page at 09:20 (draft the exact text)
4. At 09:30, you've confirmed the deployment caused the issue. Should you roll back before understanding why, or investigate first? Justify your answer.
5. After resolving at 10:00, write the post-mortem structure (timeline, contributing factors, 3 action items — without blaming anyone)

# Integration

**Connecting to Psychology — Naturalistic Decision Making**

Gary Klein's research on how experts make decisions under pressure (1999) found that experienced professionals don't evaluate options like a decision tree — they pattern-match to prior experience and mentally simulate outcomes. A seasoned on-call engineer sees "high latency after deployment" and immediately thinks "rollback candidate" based on pattern recognition, not analysis.

This explains why incident response should be practised and scripted (runbooks, playbooks) for known patterns, while remaining flexible for novel situations. The runbook externalises the pattern: "when you see X, do Y." This reduces cognitive load during the high-stress incident window.

It also explains why blameless culture matters for learning: engineers only share honest accounts of their decision-making when psychological safety is high. Klein's studies showed that expert intuition is built from experience — including experience with failures and near-misses. Suppressing honest incident reporting (through blame) destroys the team's collective ability to build expert intuition.

The engineering implication: document your incidents, run post-mortems, and build a library of patterns. This is the team's collective memory that turns individual experience into shared expertise.

How would you build a culture where incident post-mortems are treated as valuable learning opportunities rather than uncomfortable reviews?

# Lore Conclusion

The incident is resolved. The ward is restored. The post-mortem is complete. Three action items are on the board, assigned, with due dates.

*"The incident is not the failure,"* Archmage Veylan says. *"The failure is the incident that produces no learning. If you investigate, if you document, if you improve — the incident was, in a strange way, a gift."*

Build systems that handle failure gracefully. Build teams that learn from it. That is the full cycle of reliability engineering.
---
