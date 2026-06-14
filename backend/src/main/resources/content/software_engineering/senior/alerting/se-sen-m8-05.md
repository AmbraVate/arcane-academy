---
id: se-sen-m8-05
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m8
moduleTitle: "Module 8: Observability"
moduleGlyph: "🔭"
moduleSortOrder: 8
topicSlug: alerting
topicTitle: "Alerting"
topicSortOrder: 5
lesson: alerting
title: "Alerting"
sortOrder: 5
difficulty: 3
estimatedMinutes: 25
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [monitoring]
integrationDomains: [psychology, economics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains alert fatigue and how it causes incidents to be missed"
    - "Distinguishes symptom-based from cause-based alerts with examples"
    - "Describes burn-rate alerting as a solution to threshold tuning"
    - "Explains what a runbook is and why alerts should link to one"
    - "Names at least one technique for reducing noise (multi-window, inhibition, grouping)"
  keywords: [fatigue, symptom, cause, burn, rate, runbook, noise, threshold, page, actionable]
  modelAnswer: |
    Alert fatigue: too many alerts → on-call engineers stop trusting them →
    real incidents missed because engineer assumes it's another false alarm.

    Symptom-based alerting: alert on user-visible effects.
    "Error rate > 1% for 5 minutes" — users are experiencing failures.
    
    Cause-based alerting: alert on technical conditions.
    "CPU > 90%" — may or may not affect users.
    
    Prefer symptom-based: alert on what users experience.
    Use cause-based only where symptoms might come too late.

    Burn rate alerting (SLO-based):
    "If current error rate continues, SLO will be exhausted in < 1 hour"
    Fires when budget consumption is unsustainably fast.
    Fewer false alarms than static thresholds.

    Runbook: step-by-step guide for responding to each alert.
    Every alert should link to its runbook.
guidedSteps:
  - id: alert-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      An on-call engineer receives 50 alerts per day, but historically 90% are false alarms.
      What is the primary risk of this alert environment?
    inputConfig:
      options:
        - "The monitoring system will run out of memory"
        - "Engineers develop alert fatigue and start ignoring or dismissing alerts without investigating"
        - "The alerts will slow down the service"
        - "Engineers will over-react to every alert"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Engineers develop alert fatigue and start ignoring or dismissing alerts without investigating"]
      rejectedFeedback: "Alert fatigue is when high false-alarm rates cause engineers to stop trusting and thoroughly investigating alerts. When a real incident fires in this environment, it looks like all the other noisy alerts and gets dismissed. This is how silent outages happen for hours."
    hint: "What happens to your attention and trust when 90% of a signal is noise?"
    reflectionPrompt: "The goal is: every alert that fires should require human action. If an alert fires and the right response is 'acknowledge and wait' or 'probably fine', that alert should be eliminated or downgraded to a notification. Pages should page."
  - id: alert-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      An alert on "user-visible error rate > 1%" is called a ___-based alert because it
      fires when users are experiencing failures, not based on internal system metrics.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["symptom", "symptom-based"]
      rejectedFeedback: "**Symptom-based** alerts fire on user-visible effects. They're generally better than cause-based alerts (which fire on technical conditions like CPU > 90%). A symptom alert fires only when users are harmed. A cause alert fires on conditions that *might* lead to user harm — often prematurely."
    hint: "It's based on what users experience (the symptom) rather than the internal cause."
    reflectionPrompt: "Symptom-based alerting means: 'I will be paged only when users are actually experiencing degradation.' This reduces false positives dramatically. Cause-based alerts (CPU, memory, queue depth) still have a place as warnings, but shouldn't wake anyone up."
  - id: alert-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      What is a runbook, and why should every alert link to one? What should a good runbook contain?
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [runbook, step, diagnose, fix, action, procedure, response, on-call, triage, guide]
      rejectedFeedback: "A runbook is a step-by-step guide for responding to a specific alert. It contains: what the alert means, likely causes, diagnostic steps (what to check first), remediation steps (how to fix each cause), escalation path. Good runbooks mean a junior engineer at 2am can resolve a known incident without waking a senior."
    hint: "Think: what would you want to read at 2am when this alert fires and you've never seen it before?"
    reflectionPrompt: "Runbooks are knowledge capital. Writing them is the ops equivalent of writing documentation. Every incident whose runbook doesn't exist yet is an opportunity to write one during the post-incident review."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Burn-rate alerting fires when:"
    options:
      - "The error rate exceeds a fixed percentage"
      - "The current error rate would exhaust the SLO error budget within a defined time window"
      - "CPU utilisation exceeds 80%"
      - "The service has been running for more than 24 hours"
    correctIndex: 1
    feedback: "Burn-rate alerting is SLO-aware. It calculates: 'at the current error rate, how long until the error budget is exhausted?' and alerts when this projection is unsustainably short (e.g. 'budget exhausted in < 1 hour'). This avoids both false alarms (brief spikes don't drain the budget) and misses (sustained low-level degradation drains the budget)."
  - type: MULTIPLE_CHOICE
    question: "What is the best way to reduce alert noise from a flapping alert (one that alternates between firing and resolving rapidly)?"
    options:
      - "Increase the alert threshold"
      - "Delete the alert"
      - "Use a multi-window condition: alert must be triggered for N consecutive minutes"
      - "Reduce the scrape interval"
    correctIndex: 2
    feedback: "Multi-window conditions require the alert condition to be sustained over a time window before firing. This prevents brief transient spikes from generating pages. Prometheus supports this with `for: 5m` (condition must hold for 5 minutes before alerting)."

retrieval:
  recall: "What is alert fatigue and what are three techniques for reducing it?"
  explain: "Explain to a junior engineer why symptom-based alerting is generally better than cause-based alerting."
  mistakeId:
    code: |
      # Prometheus alert rules
      - alert: HighCPU
        expr: cpu_usage_percent > 80
        labels:
          severity: critical
          pagerduty: "true"
      - alert: HighMemory
        expr: memory_usage_percent > 75
        labels:
          severity: critical
          pagerduty: "true"
      - alert: HighDiskIO
        expr: disk_io_percent > 70
        labels:
          severity: critical
          pagerduty: "true"
    answer: "These are cause-based alerts with aggressive thresholds that will fire constantly during normal operations. CPU at 80% doesn't mean users are experiencing problems. Downgrades: set these as 'warning' (Slack notification, not page). Add a symptom-based critical alert: error rate > 1% or p99 latency > 1s. Page on symptoms; notify on potential causes."
---

# Hook

Your on-call rotation averages 30 pages per night. Most resolve themselves by the time you acknowledge. Your team has learned to silence the PagerDuty noise and sleep through it.

Then one night, a real outage fires. It looks exactly like every other alert. It gets dismissed. The database is down for three hours before the first support ticket arrives.

Alert fatigue is not a monitoring failure. It's a signal design failure. And it costs real outages.

> If you had to reduce your team's alert volume by 80%, what would you eliminate first?

# Lore Introduction

The Academy's alarm network once sounded for every anomaly — every minor ward fluctuation, every slightly-delayed spell completion. Within a month, the watch officers had learned to sleep through alarms.

*"We reduced the alarms to five,"* Archmage Veylan says. *"Apprentice endangered. Ward failed. External threat detected. Critical system offline. Mana reserves critical. Five alarms, each requiring immediate action. Since then, every alarm has been acted on within minutes."*

# Core Learning

## Concept Introduction

**Alerting** is the practice of automatically notifying engineers when a system needs human attention. The goal: every alert that fires requires immediate action. Every alert that doesn't need action should not fire.

**Alert quality principles:**
1. **Actionable** — every alert has a clear response
2. **Symptom-based** — alert on user-visible effects, not internal causes
3. **Not noisy** — false positives drain trust
4. **Linked to runbooks** — responders know exactly what to do

**Alert severity model:**
- `CRITICAL` / page: requires immediate human action (wake someone up)
- `WARNING` / ticket: investigate soon (not now, in business hours)
- `INFO` / noise: informational, no action needed

## Why It Matters

- Alert fatigue causes real incidents to be dismissed as noise
- Every minute of undetected outage has direct business cost
- Good alerts enable non-expert responders (on-call rotation)
- Poorly designed alerts are worse than no alerts (false confidence)

## Worked Examples

**Symptom-based vs cause-based:**
```yaml
 # Symptom-based (GOOD — alerts on user experience)
- alert: HighErrorRate
  expr: rate(http_requests_total{status=~"5.."}[5m]) / rate(http_requests_total[5m]) > 0.01
  for: 2m
  labels:
    severity: critical
  annotations:
    summary: "Error rate above 1%"
    runbook: "https://wiki.company.com/runbooks/high-error-rate"

 # Cause-based (lower severity — warning only)
- alert: HighCPU
  expr: cpu_usage_percent > 85
  for: 10m
  labels:
    severity: warning
  annotations:
    summary: "CPU sustained above 85% for 10 minutes"
```

**Burn-rate alerting (SLO-based):**
```yaml
 # Alert when error budget will be exhausted in < 1 hour at current rate
- alert: ErrorBudgetBurnRateHigh
  expr: |
    (
      rate(http_requests_total{status=~"5.."}[1h])
      /
      rate(http_requests_total[1h])
    ) > (0.001 * 14.4)  # 14.4x burn rate exhausts monthly budget in 2 days
  for: 5m
  labels:
    severity: critical
```

**Multi-window alerting (prevents flapping):**
```yaml
- alert: HighLatency
  expr: histogram_quantile(0.99, rate(http_request_duration_seconds_bucket[5m])) > 0.5
  for: 5m  # must hold for 5 minutes before firing
```

## Common Mistakes

- **Paging on cause-based metrics** — CPU, memory, disk IO at moderate thresholds wake people up for non-issues.
- **No `for` duration** — alerts that fire immediately on brief spikes cause constant noise.
- **No runbook** — responder stares at alert with no idea what to do.
- **Same severity for everything** — "critical" means nothing if it's used for everything.
- **Alerting on every metric you collect** — not every metric needs an alert; alerts are for conditions requiring human action.

## Mental Model

Alerting is a **fire alarm system**. A good fire alarm rings only when there is fire — not when someone burns toast, not when the humidity changes, not when a sensor glitches. A fire alarm that rings for toast is not a safety feature; it's trained negligence. The moment people start ignoring the alarm, the safety function is gone.

## Mini Summary

- ✔ Alert fatigue = too many false positives → engineers stop trusting alerts → real incidents missed
- ✔ Symptom-based alerts (user-visible effects) are more valuable than cause-based alerts
- ✔ Every critical alert must be actionable and link to a runbook
- ✔ Use `for: N minutes` to prevent flapping alerts
- ✔ Burn-rate alerting catches sustained degradation without over-firing on transient spikes

# Guided Practice Quest

**The Alarm Network**

Review five existing alert rules. Classify each as actionable/noisy, symptom/cause-based, and propose improvements.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

An e-commerce platform has the following alert setup (all set to CRITICAL/page):
1. `CPU > 70%`
2. `Memory > 80%`
3. `HTTP 5xx responses > 0`
4. `Database connections > 50`
5. `p99 latency > 200ms for 1 minute`
6. `Disk usage > 60%`
7. `Order failure rate > 0.1% for 10 minutes`
8. `External payment API response time > 500ms`

For each alert:
1. Is it symptom-based or cause-based?
2. Is the threshold appropriate? Why or why not?
3. What severity would you assign it (critical/warning/info)?
4. Would you keep, modify, or remove it?

Then design 3 new alerts they're missing that would significantly improve their monitoring coverage.

# Integration

**Connecting to Economics — The Boy Who Cried Wolf (Information Economics)**

George Akerlof's 1970 paper "The Market for Lemons" showed that information asymmetry — when one party knows more than another — degrades market quality until the market collapses. Low-quality sellers crowd out high-quality ones because buyers can't distinguish them.

Alert systems suffer the same problem. An alert system with many false alarms creates information asymmetry: the engineer doesn't know if this alert is "real" or "noise" until they investigate. If false alarms are frequent enough, engineers stop investigating — the alert market collapses (alert fatigue).

The economic remedy is signalling: mechanisms that credibly communicate quality. In markets, warranties and brands signal quality. In alerting, the signal is: "this alert fires rarely and always requires action." Teams that maintain this discipline build trust in their alerts. Engineers investigate every alert promptly because historical experience teaches that every alert was worth it.

This is a credibility investment. The payoff is response speed during real incidents. The cost of breaking it — one noisy week — can take months to rebuild.

How would you rebuild alert credibility in a system where alert fatigue has already set in?

# Lore Conclusion

Five alarms. Five actions. Every alarm acted on within minutes.

*"The Academy lost two apprentices before we reduced the alarm network,"* Archmage Veylan says quietly. *"Not because the alarms didn't fire. Because they had fired too many times for nothing, and the watch officers had learned not to trust them."*

Build fewer, better alerts. Your future on-call self will thank you.
---
