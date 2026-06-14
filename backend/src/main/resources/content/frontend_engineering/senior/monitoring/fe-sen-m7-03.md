---
id: fe-sen-m7-03
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m7
moduleTitle: "Module 7: Frontend Observability"
moduleGlyph: "📊"
moduleSortOrder: 7
topicSlug: monitoring
topicTitle: "Monitoring"
topicSortOrder: 3
lesson: frontend_monitoring
title: "Frontend Monitoring"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Distinguishes RUM (Real User Monitoring) from synthetic monitoring
    - Explains what metrics RUM collects and how they differ from Lighthouse
    - Describes what synthetic monitoring provides that RUM cannot
    - Explains the concept of alerting on SLOs/SLIs for frontend
    - Synthesises a monitoring strategy covering both RUM and synthetic approaches
  keywords: [RUM, synthetic, real user, performance, LCP, CLS, INP, P75, P95, SLO, SLI, Datadog, Grafana, canary, error rate, availability]
  modelAnswer: |
    RUM (Real User Monitoring) collects performance and error data from actual users in production: Core Web Vitals (LCP, INP, CLS), page load times, JavaScript error rates, API latency as experienced by real users on real devices. RUM shows the P75 and P95 experience — the 75th and 95th percentile — capturing slow users that averages hide.

    Synthetic monitoring (also called canary monitoring) runs scripted browser tests on a schedule from fixed locations. It proactively detects availability and performance regressions before users report them. Unlike RUM (which requires real traffic), synthetic runs 24/7 even when traffic is low.

    The complementarity: RUM shows what real users experience (accurate to conditions, delayed reporting, no pre-user data). Synthetic shows what a controlled agent experiences (fast detection, proactive, but may not represent all users). Use both.

    SLIs (Service Level Indicators) for frontend: JS error rate, Core Web Vitals P75, API success rate from client. SLOs (Service Level Objectives): JS error rate < 0.5%, LCP P75 < 2.5s. Alert when SLO is at risk (error rate trending towards threshold, not just when it's exceeded).

    A monitoring strategy: Sentry for error tracking → DataDog RUM for performance metrics → synthetic canaries for availability → dashboard combining all signals → alert on SLO breach or significant trend.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "Your Lighthouse score is 95. Why might real users still experience poor performance?"
    options:
      - "Lighthouse scores are not calibrated to real-world conditions"
      - "Lighthouse simulates one device and network; real users have diverse devices, connections, and geographies"
      - "Lighthouse only measures server-side performance"
      - "The Lighthouse score includes non-performance factors"
    correctIndex: 1
    feedback: "Lighthouse runs on a simulated throttled device with a specific connection profile — useful for catching regressions but not representative of your user base. A user on a 3-year-old Android device in rural connectivity experiences something very different. RUM captures what those actual users experience, including the P75 and P95 tail — the users with the worst experience, who often represent the highest churn risk."
  - type: SHORT_TEXT
    prompt: "Your app has very low traffic on weekends (2% of weekday traffic). How does synthetic monitoring help where RUM is insufficient during these periods?"
    hint: "What can synthetic do that requires no actual users?"
  - type: FILL_BLANK
    prompt: "P75 performance means ___ of users have a performance at or better than this value. The remaining ___ experience worse."
    answer: "75%; 25%"
    hint: "P75 is the 75th percentile — better than 75% of experiences, worse than 25%."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Your SLO for LCP P75 is <2.5 seconds. Current P75 is 2.1 seconds. After a deployment it rises to 2.4 seconds. Should you alert?"
    options:
      - "No — it's still within the SLO threshold"
      - "Yes — the trend indicates an SLO breach is imminent; investigate before it exceeds 2.5s"
      - "No — alert only when the threshold is crossed"
      - "Yes — any deployment that changes metrics requires an alert"
    correctIndex: 1
    feedback: "Reactive alerting (alert when SLO is broken) means users are already impacted before you know. Trend-based alerting (alert when heading towards SLO breach) allows you to investigate and roll back before the breach occurs. 2.1→2.4 in one deployment is a 14% degradation — worth investigating even though the SLO isn't yet breached."
  - type: MULTIPLE_CHOICE
    question: "What does a synthetic monitoring canary test specifically check that RUM cannot?"
    options:
      - "Real user experience across geographic regions"
      - "Whether the app is available and functional when there are no real users"
      - "The exact JavaScript errors users encounter"
      - "Database query performance"
    correctIndex: 1
    feedback: "Synthetic runs scripted user journeys on a schedule — completely independent of real traffic. It detects: the login page returning 500, a critical flow (checkout, login) breaking, availability dropping. RUM requires real users to generate the data — it can't tell you what would happen if a user visited right now when no one is visiting."
retrieval:
  recall: "What is the difference between P50, P75, and P95 performance metrics? Why does monitoring the P75 and P95 matter more than the average?"
  explain: "Why do organisations use both RUM and synthetic monitoring rather than choosing one?"
  mistakeId:
    code: |
      // Monitoring strategy
      "We have Lighthouse set up in CI. It runs on every PR.
       We check the score doesn't drop below 90.
       This is our frontend performance monitoring."
    answer: "Lighthouse in CI is performance regression detection (lab data), not production monitoring. It catches changes before they ship. It does not tell you: what real users experience in production, what the P75/P95 performance is across diverse devices, whether a production incident has degraded performance, or whether the app is currently available. A complete monitoring strategy adds: RUM (real user performance data), error rate tracking (Sentry), and synthetic canaries (availability and critical path verification). Lighthouse CI is valuable — but it's one piece of a monitoring strategy, not the whole."
---

# Hook

Your Lighthouse CI score is 90+ on every PR. Then a user from a low-income country emails: "Your app has never loaded for me." You check RUM: P95 LCP in their region is 12 seconds. Your app is effectively unavailable for 5% of your users.

Lighthouse told you the happy path on a simulated device. RUM tells you what your actual users experience.

# Lore Introduction

*"The observatory tests the spellwork in a controlled chamber,"* the Observatory Director explains. *"But what happens when the spell is cast outside — in wind, in cold, with apprentices who have different backgrounds?"*

She shows two displays: the chamber test (perfect conditions) and the field readings (variable). *"Both are necessary. The chamber catches controlled failures. The field reveals what actually happens."*

# Core Learning

## Concept Introduction

**Two complementary monitoring approaches:**

**Real User Monitoring (RUM):**
- Collects data from actual users in production
- Captures Core Web Vitals (LCP, CLS, INP) as experienced
- Shows P50, P75, P95 — not just the average
- Includes error rates, API latency, page load times
- Tools: Datadog RUM, New Relic Browser, Grafana Faro

```js
// Datadog RUM setup
import { datadogRum } from '@datadog/browser-rum';

datadogRum.init({
  applicationId: '...',
  clientToken: '...',
  site: 'datadoghq.eu',
  service: 'frontend-app',
  sampleRate: 100,
  trackInteractions: true,
});
```

**Synthetic Monitoring (Canary):**
- Scripted user journeys run on a schedule
- Detects availability independently of real traffic
- Runs from multiple geographic locations
- Alerts immediately when journeys fail
- Tools: Datadog Synthetics, Checkly, AWS CloudWatch Synthetics

```
Canary script (runs every 5 min):
1. Navigate to /login
2. Enter test credentials
3. Assert dashboard loads within 3 seconds
4. Navigate to /checkout
5. Assert checkout form renders
Alert if any step fails or exceeds threshold
```

**SLO monitoring:**
```
SLI: JavaScript error rate (measured by Sentry)
SLO: Error rate < 0.5% of page views

SLI: LCP P75 (measured by RUM)
SLO: LCP P75 < 2.5 seconds

Alert condition: SLI trending towards SLO breach (not only on breach)
```

**Dashboard structure:**
- Error rate (current vs baseline)
- Core Web Vitals P75 (current vs previous week)
- API success rate from client
- Synthetic canary success rate
- Active incidents

## Why It Matters

Monitoring is what turns "is the frontend okay?" from a feeling into a dashboard — and the frontend is where user experience actually happens, so it deserves the same observability as any backend service:

- Real User Monitoring captures what synthetic tests can't: the 75th-percentile experience on mid-range Androids over cellular, which is routinely several times worse than your MacBook-on-fibre baseline
- Trends beat snapshots — a Core Web Vitals dashboard that degrades 5% per sprint is an early warning system; a one-off Lighthouse run is a photo of a moving train
- Alerting closes the loop: error-rate and vitals thresholds that page someone convert silent client-side degradation into incidents with owners and timelines
- Performance regressions are business regressions — conversion, bounce, and search ranking all track the metrics you'd be monitoring, which is what justifies the engineering time in any prioritisation debate

Teams without frontend monitoring learn about problems from app store reviews and support tickets — the most expensive, slowest, least actionable telemetry there is.

## Common Mistakes

- **Using only Lighthouse / synthetic.** Misses real user diversity.
- **Using only RUM.** Doesn't detect regressions before users are affected.
- **Alerting on averages.** Average hides the tail. Monitor P75 and P95.
- **No business metric correlation.** Connect LCP degradation to conversion rate — makes the business case for performance work.

## Mental Model

Frontend monitoring is the difference between a weather station network and stepping outside to check the sky. Looking out your window (testing on your own machine) tells you the weather in one privileged location — fast hardware, clean network, latest browser. Your users live across the whole map: phone-hardware climates, network microclimates, browser weather systems. RUM is the station network reporting conditions where people actually live; percentiles are reading the map honestly (the 75th percentile is "what's the weather like for the wetter quarter of the country", not the national average that hides the storms). Dashboards are the climate record — drift visible across months, regressions visible at deploy boundaries — and alerts are the severe-weather warnings that wake someone before the flood, not after. The cardinal sin in both fields is the same: forecasting the nation's weather from outside your own front door.

## Mini Summary

- ✔ RUM: real user experience, P75/P95 performance, actual error rates
- ✔ Synthetic: proactive availability testing, scheduled user journeys
- ✔ SLOs define acceptable thresholds; alert on trending before breach
- ✔ Dashboard combines error rate, performance, availability
- ✔ Both RUM and synthetic are needed — each catches what the other misses

# Guided Practice Quest

Work through the guided steps to understand P75/P95 percentiles and the complementarity of monitoring approaches.

# Solo Practice Quest

Design a monitoring strategy for a SaaS application with 50,000 daily active users. Define: what RUM metrics to collect, what synthetic canaries to run, what SLOs to define, what alert thresholds to set, and what your engineering on-call rotation would respond to.

# Integration

**Mathematics — Percentile Monitoring and Tail Latency**

Monitoring performance at the P75 and P95 is a tail latency discipline — the recognition that averages can hide catastrophically bad experiences for a significant user population. If median LCP is 1.5s and P95 is 8s, the average (say, 2.2s) understates the problem: 5% of users wait 8 seconds. For a site with 50,000 daily users, that's 2,500 users with a terrible experience every day. Percentile monitoring exposes this. The mathematical insight: the mean is dominated by the common case; the tail is dominated by edge cases. In web performance, edge cases are often: slow devices, poor networks, geographic distance to servers, or heavy resource contention. These users are not outliers to ignore — they're the users most likely to churn.

# Lore Conclusion

*"Both displays tell the truth,"* the Director says. *"The chamber shows what's possible. The field shows what's real. Together they tell you: where you need to improve, and whether you already have a problem."*

---
