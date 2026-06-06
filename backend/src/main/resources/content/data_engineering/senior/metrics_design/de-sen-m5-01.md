---
id: de-sen-m5-01
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m5
moduleTitle: "Module 5: Analytics Engineering"
moduleGlyph: "📊"
moduleSortOrder: 5
topicSlug: metrics_design
topicTitle: "Metrics Design"
topicSortOrder: 1
lesson: 1
title: "Metrics Design: Measuring What Actually Matters"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-sen-m4-04
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes between vanity metrics and actionable metrics"
    - "Explains the metric definition components: numerator, denominator, filters, dimensions"
    - "Describes the risks of metric gaming (Goodhart's Law)"
    - "Identifies how metric trees connect business goals to operational data"
  keywords:
    - vanity metric
    - actionable metric
    - metric definition
    - Goodhart's Law
    - metric tree
    - north star metric
    - denominator
  modelAnswer: |
    A vanity metric looks impressive but doesn't guide decisions — total page views, registered users, total revenue without context. An actionable metric changes behaviour: week-over-week active user retention, revenue per cohort, lesson completion rate by difficulty level.
    A complete metric definition specifies: numerator (what is counted), denominator (the base population), time window, filters (which segment), and dimensions (how to slice). Ambiguous definitions produce inconsistent numbers across teams — "active user" means different things to different people without an explicit definition.
    Goodhart's Law: "When a measure becomes a target, it ceases to be a good measure." Optimising for a metric causes agents to find the shortest path to the number, not the underlying goal. Lesson completion rate optimised in isolation → shorter, easier lessons. Fix: metric pairs (completion rate paired with satisfaction score) or outcome metrics (downstream user success) that are harder to game.
    Metric trees decompose a north star metric into contributing factors, creating a hierarchy where each leaf can be measured and driven by a team. The tree makes clear which operational metrics drive business outcomes.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "The Consortium measures 'Daily Active Users' as any user who logged in. The product team optimises for DAU and adds a daily login notification. DAU goes up 40% but lesson completions drop 15%. What happened?"
    options:
      - "The metric improved because DAU is the correct north star"
      - "Goodhart's Law — the metric was gamed; login is not the actual goal (learning)"
      - "The notification feature had a bug that caused completions to fail"
      - "Users became more active because they were reminded, showing the feature worked"
    correctIndex: 1
    explanation: "This is a textbook Goodhart's Law example. The team optimised for the metric (logins) rather than the underlying goal (learning). Users clicked the notification, logged in, and immediately left — boosting DAU without improving learning outcomes. The metric should have been 'Daily Active Learners' defined as users who completed at least one lesson or learning action."
  - type: FILL_BLANK
    question: "A metric's ___ defines the base population the numerator is expressed against. Without it, the numerator is a vanity count."
    answer: "denominator"
    explanation: "'1,000 lesson completions this week' is a vanity count. '1,000 completions out of 5,000 active learners = 20% completion rate' is actionable — it can be compared across cohorts, time periods, and segments. The denominator anchors the number in context."
  - type: SHORT_TEXT
    question: "Define the metric 'lesson completion rate' rigorously, including numerator, denominator, time window, and at least one dimension."
    modelAnswer: "Numerator: count of distinct (user_id, lesson_id) pairs where status = 'COMPLETED'. Denominator: count of distinct (user_id, lesson_id) pairs where the lesson was started (at least one view event). Time window: rolling 7-day or calendar month. Filters: exclude test users, exclude lessons marked as deprecated. Dimensions: by tier (junior/senior/lead), by domain (data_engineering/software_engineering), by cohort (user join week). This definition is unambiguous — every team computing it will get the same number."
microCheckpoint:
  question: "What is Goodhart's Law and why is it a risk in metric design?"
  answer: "Goodhart's Law: 'When a measure becomes a target, it ceases to be a good measure.' Making a metric a target incentivises optimising the number directly (gaming) rather than the underlying goal. The metric diverges from what it was supposed to measure."
retrieval:
  recall: "What four components make a metric definition unambiguous?"
  explain: "Explain the difference between a vanity metric and an actionable metric with an example."
  mistakeId: "metric-no-denominator"
---

# The Wrong Number

The quarterly review showed 2.1 million lesson views. The investors were pleased. The Lead Data Engineer looked at the learner retention data. "Views aren't the same as completions. And completions aren't the same as outcomes." The Senior Engineer pulled up the raw numbers. 2.1 million views. 300,000 completions. 45,000 users who returned the following week. "Which of these is the metric we should be optimising?" The Lead sat back. "That's exactly the right question to ask."

# The Cost of Vanity Metrics

A vanity metric is one that looks impressive in isolation but cannot guide a decision.

| Vanity Metric | Why It's Vanity |
|---|---|
| Total page views | Can increase by showing the same page twice |
| Total registered users | Does not distinguish active from dormant |
| Total content uploaded | No relationship to quality or engagement |
| Cumulative revenue | Doesn't account for churn or refunds |

The test: **"If this number goes up, what do we do differently?"** If the answer is nothing — it's vanity.

An **actionable metric** changes behaviour when it moves.

| Actionable Metric | What It Drives |
|---|---|
| Weekly lesson completion rate by tier | Content quality improvements in weak tiers |
| Day-7 learner retention by cohort | Onboarding improvements targeting drop-off |
| XP-per-session by difficulty | Difficulty calibration across the curriculum |
| Lesson re-attempt rate | Identification of confusing or unfair lessons |

## Metric Definition Components

A metric definition must be unambiguous enough that any analyst computing it from the same data arrives at the same number.

```
Metric: Lesson Completion Rate

Numerator:    COUNT DISTINCT (user_id, lesson_id)
              WHERE lesson_status = 'COMPLETED'

Denominator:  COUNT DISTINCT (user_id, lesson_id)
              WHERE lesson_status IN ('STARTED', 'IN_PROGRESS', 'COMPLETED')

Time window:  Rolling 28 days (or calendar month — must specify)

Filters:      EXCLUDE test_user = true
              EXCLUDE lesson_deprecated = true
              EXCLUDE users with account_age < 1 day (bots)

Dimensions:   tier (junior/senior/lead)
              domain (data_engineering, etc.)
              user_cohort (week of first lesson)
```

Without the denominator, you have a count. Without the filters, you have noise. Without the dimensions, you cannot diagnose which segment is driving a change.

## Metric Trees

A metric tree connects a top-level north star metric to the operational metrics that drive it.

```
North Star: Weekly Active Learners (WAL)
    │
    ├── New Learners Started This Week
    │       ├── Registration Conversion Rate
    │       └── Onboarding Completion Rate
    │
    ├── Retained Learners from Last Week
    │       ├── Day-7 Retention Rate
    │       ├── Avg. Sessions per Learner per Week
    │       └── Lesson Completion Rate
    │
    └── Reactivated Dormant Learners
            ├── Re-engagement Email Open Rate
            └── Email → Lesson Completion Funnel
```

Each leaf metric is owned by a team and directly measurable from data. Moving a leaf metric should be traceable to its contribution to WAL. Teams cannot directly change WAL — they change the leaves.

## Goodhart's Law

**"When a measure becomes a target, it ceases to be a good measure."** — Charles Goodhart

```
Goal:     Learners should develop deep understanding
Metric:   Lesson completion rate

Optimisation pressure on completion rate:
  → Make lessons shorter (5 minutes instead of 35)
  → Remove hard questions from assessments
  → Auto-complete on scroll-through
  
Result: completion rate ↑, understanding ↓
The metric was gamed; the goal was not served.
```

**Countermeasures:**
1. **Metric pairs**: pair completion rate with satisfaction score. Hard to game both simultaneously.
2. **Outcome metrics**: measure downstream results (week-2 skill assessment score) rather than proxies (completion).
3. **Counter-metrics**: define a counter-metric that should NOT go down (e.g. "average time-on-lesson should not fall below 20 minutes").
4. **Qualitative auditing**: regular human review of whether metric movements reflect genuine improvement.

## Semantic Layer

For metrics to be consistent across teams and tools (dashboards, analysts, data scientists), define them in a **semantic layer** — a single authoritative definition that all downstream tools query.

```yaml
# dbt metrics (or MetricFlow)
metrics:
  - name: lesson_completion_rate
    label: Lesson Completion Rate
    model: ref('fct_lesson_attempts')
    calculation_method: ratio
    expression: completed_count / started_count
    timestamp: completed_at
    time_grains: [day, week, month]
    dimensions:
      - tier
      - domain
      - user_cohort
    filters:
      - field: is_test_user
        operator: '!='
        value: 'true'
```

Every dashboard, every SQL query, every ML feature referencing `lesson_completion_rate` reads from the same definition. No more "why does marketing's dashboard show a different number than finance's?"

## Common Mistakes

> **No Denominator**
> "Lesson completions increased by 200 this week" — is this good? Bad? We enrolled 5,000 new users this week. The denominator tells the story. Always express metrics as rates, ratios, or per-unit when the base population changes.

> **Undifferentiated Dimensions**
> An average metric hides the distribution. Average lesson completion rate = 50% might mean all tiers are 50%, or junior is 80% and lead is 5%. Always slice by the most actionable dimensions before drawing conclusions.

> **Metric Proliferation**
> A team tracking 200 metrics is tracking none of them. A metric without an owner, a target, and a review cadence is noise. Curate a small set of owned, tracked, and acted-upon metrics.

## Mental Model

Think of metrics as **navigation instruments on a ship**. Vanity metrics are the odometer (total distance travelled — impressive, meaningless without context). Actionable metrics are the compass heading, fuel efficiency, and ETA — each drives a decision. Goodhart's Law is what happens when the captain optimises for fuel efficiency alone: they slow to 2 knots, never arrive, and technically win. The metric tree is the full instrument panel: each gauge connects to overall voyage success, and no single gauge can be optimised in isolation.

**Mini Summary**: Vanity metrics look impressive but don't drive decisions. Every metric needs a numerator, denominator, time window, filters, and dimensions. Metric trees connect north stars to operational leaves. Goodhart's Law: targets corrupt measures — use metric pairs and outcome metrics to guard against gaming. Define metrics once in a semantic layer to ensure consistency across all consumers.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium's product team proposes a new north star metric: "Total XP Earned Per Week." They argue it captures both engagement (lessons completed) and difficulty (harder lessons give more XP).

Critically evaluate this metric:
1. Apply the actionability test: if Total XP Per Week increases, what do you do differently?
2. How could this metric be gamed by a product change that looks good in the number but harms learners?
3. Propose an alternative (or companion) metric that addresses the gaming risk, and write its full definition (numerator, denominator, time window, filters, dimensions).

---

# Integration

**Mathematics**: A **metric tree** is a decomposition of a composite metric into a **product of ratios** — precisely the **chain rule of conditional probability**. P(WAL) = P(new learner started) × P(onboarding completed | started) × P(retained | completed onboarding). Each factor in the product is an independently measurable metric owned by a team. The chain rule ensures that improving any factor improves the north star, and the contribution of each factor is directly quantifiable. This is why metric trees are the standard tool for assigning team OKRs: each ratio has a clear owner and causal relationship to the north star.

**Sciences**: Goodhart's Law has a direct analogue in **evolutionary biology**: sexual selection can evolve traits that increase reproductive success at the cost of survival (the peacock's tail). The tail is the "metric" (attractiveness to mates); the underlying goal (fitness) is decoupled when the metric is gamed by evolution. Zahavian handicap theory argues that costly signals remain honest precisely because they're hard to fake — the equivalent of outcome metrics that can't be easily gamed. The design principle in both contexts: tie the metric as closely as possible to the underlying goal, and make it costly to decouple them.

---

# The Real North Star

"Total XP Per Week is gameable," the Senior Engineer said. "We could add a hundred 5-minute lessons worth 75 XP each and the number would skyrocket. But learners wouldn't be getting better." The Lead Data Engineer nodded. "So our north star is 'learners who pass their week-2 skill check at the tier they're studying'. You can't game that with short lessons." They opened the semantic layer definition file. Building the right metric was harder than building the dashboard. But it was the only work that mattered.
