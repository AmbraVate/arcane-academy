---
id: se-sen-m8-04
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m8
moduleTitle: "Module 8: Observability"
moduleGlyph: "🔭"
moduleSortOrder: 8
topicSlug: monitoring
topicTitle: "Monitoring"
topicSortOrder: 4
lesson: monitoring
title: "Monitoring"
sortOrder: 4
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [tracing]
integrationDomains: [psychology, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains the difference between monitoring and observability"
    - "Describes the four golden signals and what they measure"
    - "Explains SLIs, SLOs, and SLAs with an example of each"
    - "Describes what a health endpoint provides and why it matters"
    - "Names at least one Grafana dashboard use case"
  keywords: [golden, signal, latency, traffic, error, saturation, slo, sli, sla, health, dashboard, grafana]
  modelAnswer: |
    Monitoring vs observability:
    Monitoring: watching predefined metrics for known failure modes.
    Observability: ability to understand arbitrary system behaviour from outputs.

    Four Golden Signals (Google SRE):
    1. Latency: how long requests take (p50, p99)
    2. Traffic: request rate (req/s)
    3. Errors: error rate (%)
    4. Saturation: resource usage (CPU, memory, queue depth)

    SLI: Service Level Indicator — the actual measured metric
      e.g. "availability = successful_requests / total_requests"
    SLO: Service Level Objective — the target
      e.g. "availability >= 99.9% over 30 days"
    SLA: Service Level Agreement — contractual commitment with consequences
      e.g. "99.5% uptime or customer receives service credit"

    Health endpoint: GET /health returns system status
    Kubernetes uses it for liveness + readiness probes.
guidedSteps:
  - id: mon-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Google's Site Reliability Engineering book defines four "Golden Signals" for monitoring services. Which of the following is NOT one of them?
    inputConfig:
      options:
        - "Latency"
        - "Traffic"
        - "Deployment frequency"
        - "Saturation"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Deployment frequency"]
      rejectedFeedback: "The four Golden Signals are: **Latency** (how long requests take), **Traffic** (request rate), **Errors** (error rate), **Saturation** (resource utilisation). Deployment frequency is a DORA metric, not a Golden Signal."
    hint: "Think about what describes the health of a running service from a user perspective."
    reflectionPrompt: "The Golden Signals cover the user experience completely: they're being served (traffic), served quickly (latency), served correctly (errors), and the service has capacity to serve them (saturation). These four are sufficient for most service health monitoring."
  - id: mon-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In SLO-based monitoring, a ___ (three letters) is the specific metric you measure,
      and an ___ (three letters) is the target value for that metric.
    inputConfig:
      placeholder: "SLI and SLO"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SLI and SLO", "SLI, SLO", "SLI/SLO"]
      rejectedFeedback: "**SLI** (Service Level Indicator): the actual measurement (e.g. 'availability = 99.95%'). **SLO** (Service Level Objective): the target (e.g. 'availability must be ≥ 99.9%'). An SLA is an external contract with consequences. You operate to SLOs; you commit to SLAs; SLIs tell you whether you're meeting them."
    hint: "The I is the measurement, the O is the objective (target)."
    reflectionPrompt: "SLOs are how teams make data-driven decisions about reliability investment. If you're burning your error budget (the allowed failures) too fast, you prioritise reliability. If you're far under budget, you can take more feature risk. It's engineering trade-offs made explicit."
  - id: mon-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between a liveness probe and a readiness probe in Kubernetes, and why both are needed.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [liveness, readiness, restart, traffic, healthy, serving, ready, alive, start, probe]
      rejectedFeedback: "Liveness probe: is the container alive? If it fails, Kubernetes restarts the container. Readiness probe: is the container ready to serve traffic? If it fails, the container is removed from the load balancer but not restarted. Both are needed: a container might be alive (not crashed) but not yet ready (still warming up caches, completing migrations)."
    hint: "One asks 'is it alive?' The other asks 'is it ready to serve requests?' These are different questions."
    reflectionPrompt: "A common mistake: using the same `/health` endpoint for both probes. Liveness should be a lightweight check (JVM running). Readiness should check database connectivity, cache warmup, etc. Using liveness for both causes unnecessary restarts when the database is temporarily unavailable."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between monitoring and observability?"
    options:
      - "Monitoring is for production; observability is for development"
      - "Monitoring watches predefined metrics for known failures; observability enables investigation of arbitrary, unknown failures"
      - "Observability is monitoring with better dashboards"
      - "They are the same concept with different names"
    correctIndex: 1
    feedback: "Monitoring is proactive: you define what to watch and alert on known failure modes. Observability is broader: the ability to understand any system behaviour from its outputs (logs, metrics, traces). A highly observable system can be debugged even for failure modes you didn't anticipate."
  - type: MULTIPLE_CHOICE
    question: "An SLO of 99.9% availability over 30 days allows approximately how many minutes of downtime?"
    options:
      - "About 1 minute"
      - "About 44 minutes"
      - "About 7 hours"
      - "About 3 days"
    correctIndex: 1
    feedback: "30 days × 24h × 60min = 43,200 minutes total. 0.1% of 43,200 = 43.2 minutes. This is the error budget: 43 minutes of downtime in 30 days. If you're burning it too fast, prioritise reliability; if you have plenty left, you can take more deployment risk."

retrieval:
  recall: "What are the four Golden Signals? Explain what each measures and why each matters."
  explain: "Explain the difference between SLI, SLO, and SLA using a concrete example for a login API."
  mistakeId:
    code: |
      @GetMapping("/health")
      public String health() {
          return "OK";
      }
      // Used for both Kubernetes liveness and readiness probes
    answer: "A health endpoint that always returns OK is useless for readiness. Readiness should check: can this instance actually serve requests? Check database connectivity, external service availability, cache warmup completion. A static 'OK' endpoint will route traffic to an instance that can't serve it (e.g. DB unavailable). Use Spring Boot Actuator's `/actuator/health` which checks actual dependencies."
---

# Hook

Your monitoring dashboard shows everything is green. Uptime 100%. Error rate 0%. Then a user posts on Twitter that they've been getting failures for 20 minutes.

The monitoring is measuring the wrong things. Or measuring the right things but not at the right granularity. Or measuring correctly but not alerting appropriately.

Monitoring is not installing Prometheus. It's ensuring that you will know about every problem before your users do.

> What would your current (or imagined) monitoring tell you within five minutes of a 10% error rate spike?

# Lore Introduction

The Academy's Grand Observatory has evolved over centuries. The first watchers simply walked the corridors and checked each ward by eye. Then came the monitoring crystals — preset to glow when specific wards failed. Then came the Observatory — a room of crystals showing aggregate patterns, not individual ward states.

*"We no longer monitor individual wards,"* Archmage Veylan says. *"We monitor the experience of those the wards protect. If a ward fails silently but no apprentice is harmed, the Observatory does not alert. If apprentices are harmed, the Observatory sounds regardless of which ward caused it."*

# Core Learning

## Concept Introduction

**Monitoring** is watching predefined metrics and alerting on threshold violations. **Observability** is the broader ability to understand any system behaviour from its outputs.

**The Four Golden Signals (Google SRE):**

| Signal | Measures | Why it matters |
|--------|----------|----------------|
| **Latency** | How long requests take (p50/p99) | Slow responses hurt user experience |
| **Traffic** | Request rate (req/s) | Baseline for detecting anomalies |
| **Errors** | Error rate (% of requests) | Failures directly impact users |
| **Saturation** | Resource utilisation (CPU, memory, queue depth) | Predicts future failures |

**SLI / SLO / SLA:**
- **SLI** (indicator): the actual measurement (`availability = 99.95%`)
- **SLO** (objective): the target (`availability ≥ 99.9%`)
- **SLA** (agreement): contractual commitment with consequences

## Why It Matters

Good monitoring enables:
- **Proactive detection** — know before users report problems
- **Error budgets** — data-driven decisions about reliability vs feature velocity
- **Root cause investigation** — dashboards show what changed when
- **Capacity planning** — trend analysis predicts saturation
- **Deployment confidence** — watch metrics during and after deployment

## Worked Examples

**Spring Boot Actuator health endpoint:**
```yaml
# application.properties
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=when_authorized
management.health.db.enabled=true
management.health.diskspace.enabled=true
```

**Kubernetes probes:**
```yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10

readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8080
  initialDelaySeconds: 10
  periodSeconds: 5
```

**Grafana dashboard query (PromQL):**
```promql
# Error rate for last 5 minutes
rate(http_requests_total{status=~"5.."}[5m])
/
rate(http_requests_total[5m])

# p99 latency
histogram_quantile(0.99, rate(http_request_duration_seconds_bucket[5m]))
```

## Common Mistakes

- **Monitoring synthetic health checks** — `/health` returns OK but the real user flow is broken.
- **Average-based alerts** — averages hide tail latency; use p99.
- **Alert fatigue** — too many alerts cause on-call engineers to ignore them. Every alert must be actionable.
- **Missing business metrics** — infrastructure green but orders are failing? Need business-level monitoring.
- **No dashboard for deployment** — always watch key metrics for 30 minutes after every deploy.

## Mental Model

Monitoring is a **security camera system**. Cameras (metrics collectors) are placed at key points. Alarms (alerts) trigger when pre-defined thresholds are crossed. The control room (dashboard) shows the current state of all cameras. The footage (historical metrics) lets you investigate what happened. The cameras only record what they're pointed at — you must know in advance what matters.

## Mini Summary

- ✔ Four Golden Signals: Latency, Traffic, Errors, Saturation — minimum viable service monitoring
- ✔ SLI = measurement; SLO = target; SLA = contractual commitment
- ✔ Liveness probe = is the container alive; Readiness probe = is it ready to serve traffic
- ✔ Every alert must be actionable — alert fatigue destroys on-call effectiveness
- ✔ Monitor the user experience (error rate, latency) not just infrastructure health

# Guided Practice Quest

**The Observatory Setup**

Configure monitoring for a new `OrderService`. Define Golden Signal dashboards, health probes, and SLOs.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

A SaaS platform has the following SLO targets for its `CheckoutService`:
- Availability: 99.9% over 30 days
- p99 latency: < 500ms over any 1-hour window
- Error rate: < 0.5% over any 5-minute window

Design the complete monitoring setup:
1. For each SLO: what SLI would you measure and what Prometheus query would calculate it?
2. What are the error budgets for each SLO over 30 days? (calculate them)
3. What alerting thresholds would you set to give advance warning before the SLO is violated?
4. What Grafana dashboard panels would you create for the on-call engineer?
5. What synthetic monitoring (simulated user flows) would you run to detect issues the metrics miss?

# Integration

**Connecting to Psychology — The Signal Detection Problem**

Signal detection theory (Green and Swets, 1966) describes the challenge of distinguishing a true signal from noise. It introduced the concepts of hits (true positives), misses (false negatives), false alarms (false positives), and correct rejections (true negatives). The balance between sensitivity and specificity depends on the costs of each error type.

Alerting is a signal detection problem. Too sensitive (low threshold): many false alarms, alert fatigue, on-call engineers start ignoring alerts — the monitoring system loses effectiveness. Not sensitive enough (high threshold): real incidents are missed until they're severe.

The cost asymmetry matters: a missed alert (user-facing outage undetected for 30 minutes) is usually more costly than a false alarm (10 minutes of investigation revealing nothing). This suggests alerts should lean toward sensitivity, but the threshold must be set so false alarms are rare enough to be taken seriously.

SLO-based alerting (burn rate alerts) is the modern solution: alert when the rate of error budget consumption is unsustainably high, not on individual threshold violations. This reduces false alarms while ensuring real degradations are caught.

How does understanding signal detection theory change how you would design and tune your alerting thresholds?

# Lore Conclusion

The Observatory is configured. The four golden crystals glow with the right metrics. The readiness probe correctly checks the mana relay connection.

*"Monitoring is the promise you make to your users,"* Archmage Veylan says, *"to know when they are harmed before they tell you. Make that promise specific. Measure whether you keep it."*

SLOs are promises made explicit. Error budgets are the consequence of breaking them. This is engineering accountability.
---
