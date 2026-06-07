---
id: fe-lea-m4-03
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m4
moduleTitle: "Module 4: Product Thinking"
moduleGlyph: "🎯"
moduleSortOrder: 4
topicSlug: metrics
topicTitle: "Metrics"
topicSortOrder: 3
lesson: metrics
title: "Metrics"
sortOrder: 3
difficulty: 4
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-lea-m4-02]
integrationDomains: [mathematics, psychology, economics]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Correctly distinguishes leading indicators from lagging indicators
    - Identifies the North Star Metric concept and applies it to a product context
    - Explains how to design metrics that resist gaming and proxy capture
    - Describes the relationship between frontend engineering decisions and measurable product metrics
    - Demonstrates understanding of statistical significance, sample size, and A/B testing validity
  keywords:
    - North Star Metric
    - leading indicator
    - lagging indicator
    - HEART framework
    - conversion rate
    - retention
    - activation
    - A/B test
    - statistical significance
    - sample size
    - metric gaming
    - proxy metric
    - vanity metric
    - cohort analysis
    - funnel
  modelAnswer: |
    Metrics are the feedback system for product decisions. Without metrics, decisions are opinion; with the wrong metrics, decisions are optimised for the wrong outcomes.

    Leading vs lagging: lagging indicators measure outcomes after the fact (revenue, churn). Leading indicators predict future outcomes and can be influenced now (activation rate, daily active engagement, feature adoption). A product team that only watches lagging indicators cannot act until damage is already done.

    The North Star Metric is the single metric that best captures the value delivered to users and predicts long-term business health. For Spotify: time spent listening. For Airbnb: nights booked. For a SaaS project tool: weekly active teams. The NSM should correlate with both user value and business value — not just one. It is a strategic anchor, not the only metric tracked.

    Metric gaming occurs when a metric is treated as a goal rather than a signal. Goodhart's Law: "When a measure becomes a target, it ceases to be a good measure." Engineers who are measured on deployment frequency deploy small meaningless changes. PMs measured on feature count ship undiscoverable features. The solution is to pair primary metrics with counter-metrics (increase conversion rate AND maintain satisfaction score; increase notification opens AND maintain notification retention permission rate).

    A/B testing validity: a result is only actionable if it is statistically significant (typically p < 0.05 — there is less than 5% probability the result occurred by chance) at sufficient sample size. An A/B test run for 3 days on 200 users that shows +2% conversion is not actionable — sample size is too small and 3 days may not capture weekly behavioural cycles. Calculate required sample size before running a test, not after.

    Frontend metrics connection: Core Web Vitals (LCP, FID/INP, CLS) are leading indicators for user experience quality, which leads to conversion and retention. An LCP above 2.5s is a predictor of bounce rate. A high CLS score is a predictor of mis-click rates. These are not abstract technical metrics — they are measurable causes of measurable business outcomes.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      A product team is measuring success by 'monthly active users' (MAU). The metric is growing 10% month-on-month, but revenue is flat. Identify two ways this metric could be growing without delivering real business value, and propose a better set of metrics that would tell a more accurate story.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [vanity, gaming, active, revenue, retention, conversion, engagement, meaningful, define]
      rejectedFeedback: "MAU growth without revenue growth: (1) 'Active' is undefined — if 'logged in once in 30 days' counts as active, the metric can grow by acquiring and losing users at the same rate (high churn masked by high acquisition). (2) The metric can be gamed by sending emails that drive logins without value (notification-triggered logins that result in immediate exits). Better metrics: (1) Weekly Active Users (WAU) to WAU ratio across cohorts — reveals retention quality of different acquisition channels. (2) Revenue per MAU — would immediately surface the flat revenue problem relative to user growth. (3) Activation rate — what % of new signups complete the core value action within 7 days? (4) 30-day and 90-day retention cohorts — are users who signed up 3 months ago still active? (5) Feature adoption rate — what % of MAU uses the features that drive value? Together these tell a complete story: acquiring users, but not activating or retaining them, resulting in flat revenue from an ever-churning base."
    hint: "A vanity metric looks good without being useful. What would MAU growth look like if the product was actually failing?"
    reflectionPrompt: "Metrics are only as useful as their definitions. 'Active' means nothing without specifying what activity, and over what window, constitutes meaningful engagement."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Your team runs an A/B test: Button colour changed from grey to blue. After 3 days and 400 sessions (200 per variant), the blue button shows 23% conversion vs 21% for grey — a 2 percentage point difference. A product manager says 'ship it.' What questions do you ask before agreeing?
    inputConfig:
      minWords: 40
    markingRule:
      matchMode: CONTAINS
      accepted: [statistical, significance, sample, size, days, cycle, p-value, confidence, valid, power]
      rejectedFeedback: "Questions to ask: (1) Is this result statistically significant? With 200 sessions per variant and a ~2pp difference in conversion rate, the standard error is high and this result is almost certainly not statistically significant (p-value likely well above 0.05). A significance test is required before acting. (2) Was the required sample size calculated before the test? A proper power calculation for detecting a 2pp difference in conversion rate at 80% power and p=0.05 typically requires thousands of sessions per variant — not 200. (3) Did the test run for at least one full weekly cycle? 3 days misses weekend behaviour, which may differ significantly. (4) Are there any confounders? Was any other change made during the test period? Did both variants receive the same traffic sources? (5) What does the confidence interval look like? A result that is 'positive' but includes 0 in the confidence interval is not actionable. Shipping a test result based on 400 sessions over 3 days is a common mistake that produces false confidence in decisions that revert to the null under proper testing conditions."
    hint: "Before asking 'is the result positive?', ask 'is the result valid?' — which requires checking sample size and statistical significance."
    reflectionPrompt: "An invalid A/B test doesn't just fail to give correct information — it actively misleads. The worse outcome is shipping the change convinced you have evidence when you don't."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Define a North Star Metric for a B2B document collaboration tool (like Google Docs for teams). Explain why you chose it, what leading indicators predict movement in it, and what counter-metric you would pair with it to prevent gaming.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [north star, collaborate, team, document, weekly, active, value, counter, game, retain]
      rejectedFeedback: "North Star candidate: 'Weekly Active Collaborating Teams' — defined as teams with at least 2 members editing or commenting on a shared document within a week. Why: this captures genuine collaborative value (not solo use), reflects the core product value proposition, and correlates with both user value (teams are getting work done together) and business value (teams that collaborate are retained and expand). 'Weekly' captures habitual use; 'Collaborating' prevents gaming by solo logins. Leading indicators: document creation rate in week 1 (predicts long-term activation); comment and reply rate (predicts collaboration depth); team invite rate (predicts network effects and expansion revenue). Counter-metric: 'weekly collaborating teams that paid or on free plan with meaningful document volume' — prevents gaming by counting test/empty teams. If the NSM grows but the counter-metric stagnates, the growth is likely noise (empty teams, auto-created documents) rather than genuine product adoption."
    hint: "A North Star Metric should capture the moment value is delivered to the user — not when they sign up, and not when the company earns revenue, but when the product actually does its job."
    reflectionPrompt: "The North Star Metric is a hypothesis about where value lives. Pair it with leading indicators and counter-metrics so you can tell the difference between real growth and metric gaming."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Goodhart's Law states that:"
    options:
      - "More data always produces better decisions"
      - "When a measure becomes a target, it ceases to be a good measure"
      - "Metrics should be reviewed quarterly to remain relevant"
      - "Leading indicators are always more reliable than lagging indicators"
    correctIndex: 1
    tier: RECALL
    feedback: "Goodhart's Law: optimising for a metric changes behaviour in ways that make the metric a poor proxy for the underlying goal. An engineer measured on deploys per week makes tiny trivial deploys. A PM measured on features shipped builds undiscoverable features. The solution is pairing primary metrics with counter-metrics that detect gaming — increase conversion AND maintain satisfaction; increase deploys AND maintain incident rate."

  - type: MULTIPLE_CHOICE
    question: "An A/B test result is considered statistically significant at p < 0.05, which means:"
    options:
      - "There is a 95% chance the result will hold in production"
      - "There is less than a 5% probability the observed difference occurred by random chance"
      - "The sample size was at least 1000 sessions"
      - "The test ran for at least 14 days"
    correctIndex: 1
    tier: APPLICATION
    feedback: "p < 0.05 means: if there were truly no difference between variants, there is less than a 5% probability of observing a difference this large by random sampling alone. It does not guarantee the result will hold in production (that depends on whether the test conditions reflect real usage), and it does not specify sample size or duration (those are determined by power calculations, not significance thresholds)."

retrieval:
  recall: "What is the difference between a leading indicator and a lagging indicator? Give one example of each for a SaaS product."
  explain: "Your CEO asks you to 'improve the metrics' before the next board meeting. What is wrong with this framing, and what would you ask to turn it into a useful request?"
  mistakeId:
    code: |
      // Metrics dashboard at team weekly
      "Our top metrics this week:
       - Page views: +12%
       - Sessions: +8%
       - App downloads: +15%
       - Social media followers: +5%
       - Email list size: +3%"
    answer: "All five metrics are vanity metrics — they measure volume of activity without connecting to value or revenue. Page views grow when traffic grows, regardless of whether users find what they need. Downloads increase with advertising spend, regardless of whether users activate. None of these metrics predict retention, revenue, or user success. A metric dashboard that a healthy business and a failing business could both show as positive is not useful. Replace with: activation rate (new users who complete a core action within 7 days), 30-day retention cohort, revenue growth rate, and customer acquisition cost vs lifetime value ratio. These are metrics where a failing business and a healthy business diverge — which makes them useful signals."
---

# Hook

Every team has metrics. Most teams have too many, measure the wrong things, and draw wrong conclusions from valid measurements.

"Our app downloads are up 15%." The product is losing money. "Our page views grew 12%." Users are bouncing immediately after landing.

Metrics are a measurement of reality — but only if you choose the right measurements, interpret them correctly, and resist the temptation to optimise for the number rather than the outcome.

# Lore Introduction

*"The Academy's Provost introduced a new measurement system,"* the statistician explains. *"Every instructor was evaluated on the number of scrolls their students produced. Within a year, scroll production tripled."*

*"Discovery slowed to a halt."*

*"Students produced more scrolls by writing smaller scrolls, redundant scrolls, scrolls about scrolls. The metric was gamed. The Provost had the data he wanted. The Academy was failing."*

*"Goodhart's Law,"* she says. *"When a measure becomes a target, it ceases to be a good measure."*

# Core Learning

## Concept Introduction

### Metric Taxonomy

**Vanity metrics:** Look impressive, don't predict outcomes. Page views, downloads, registered users, social followers.

**Actionable metrics:** Directly connect to decisions. Conversion rate, day-7 retention, activation rate, revenue per user.

**Leading indicators:** Predict future outcomes. High week-1 engagement predicts 30-day retention. High activation rate predicts revenue growth.

**Lagging indicators:** Measure outcomes that have already occurred. Revenue, churn, NPS. Useful for assessment; too slow for real-time decision-making.

### The North Star Metric

A single metric that captures the core value delivered to users — and correlates with long-term business health. It answers: "Is this product doing its job?"

| Product | North Star Metric |
|---|---|
| Spotify | Time spent listening |
| Airbnb | Nights booked |
| Slack | Daily active teams sending messages |
| GitHub | Pull requests merged per active developer |

The NSM is a strategic anchor — not the only metric, but the one that guides prioritisation.

### Google's HEART Framework

For product quality measurement:
- **Happiness:** User satisfaction (surveys, NPS)
- **Engagement:** Frequency and depth of use
- **Adoption:** New feature uptake
- **Retention:** Whether users return
- **Task Success:** Completion rate and efficiency

### A/B Testing Validity

Before running a test, determine:
1. **Minimum Detectable Effect:** The smallest improvement worth acting on
2. **Required sample size:** Calculated from MDE, baseline conversion rate, and desired statistical power
3. **Test duration:** Long enough to capture full weekly cycle; short enough to act on

A result is only actionable when: statistically significant (p < 0.05) at the pre-calculated sample size, run for at least one full weekly cycle, with no concurrent changes as confounders.

### Goodhart's Law in Engineering

Metrics that engineers are measured on become targets — and targets get gamed. Solutions:
- **Counter-metrics:** pair primary metric with a metric that detects gaming (conversion AND satisfaction; velocity AND incident rate)
- **Outcome metrics over output metrics:** measure user outcomes, not feature delivery

## Common Mistakes

- **Starting with metrics instead of goals.** Define what success looks like, then identify metrics that measure it.
- **Running A/B tests too short.** 3 days misses weekly cycles. 200 sessions misses statistical significance for small effects.
- **Reporting vanity metrics to leadership.** This trains leadership to evaluate success on the wrong signals.

## Mini Summary

- Leading indicators predict; lagging indicators confirm — track both, act on leading
- The North Star Metric captures the moment value is delivered; it anchors prioritisation
- Goodhart's Law: metrics that become targets are gamed — pair with counter-metrics
- A/B test validity requires pre-calculated sample size, sufficient duration, and statistical significance

# Guided Practice Quest

Diagnose a vanity MAU metric, validate an A/B test result, and define a North Star Metric with leading indicators.

# Solo Practice Quest

You are the lead engineer on a consumer fintech app. The current metrics dashboard shows: downloads (+18%), MAU (+12%), session length (+20%), and customer support tickets (+40%). Define: (1) what story these metrics collectively tell, (2) the three most important metrics you would add to the dashboard and why, (3) how you would use cohort analysis to understand the support ticket spike, and (4) the single leading indicator you would track daily to predict 90-day retention.

# Integration

The philosophy of measurement connects to epistemology — the study of knowledge and its limits. Campbell's Law (a precursor to Goodhart's) and measurement theory in social science have long recognised that quantification changes the thing being measured: a student tested frequently performs differently from a student not tested, regardless of instruction quality. In product engineering, this manifests as measurement reactivity — users who know they are in an A/B test (e.g. because the experiment is visible or discussed publicly) behave differently. The Hawthorne Effect (workers observed by researchers increased productivity regardless of the experimental variable) is the canonical case. The engineering response: measure unobtrusively, design experiments where users cannot distinguish variants from normal product behaviour, and triangulate — use multiple independent measurements that would converge on the same conclusion if the conclusion is true. Single metrics, like single witnesses, can be unreliable. The convergence of multiple independent measures is the closest thing to ground truth available.

# Lore Conclusion

*"The Provost abolished scroll counting,"* the statistician concludes. *"He replaced it with a single question asked of every wizard leaving the Academy: 'Did you discover something that matters?'"*

*"Scroll production fell 60% in three months. Discovery accelerated."*

*"The new metric could not be gamed — because it measured the actual thing, not a proxy for it."*

*"Choose your measurements,"* she says, *"the way you choose your questions. Carefully. Precisely. With full knowledge that what you measure is what you will build."*

---
