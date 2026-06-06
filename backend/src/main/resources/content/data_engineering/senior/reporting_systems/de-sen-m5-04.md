---
id: de-sen-m5-04
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m5
moduleTitle: "Module 5: Analytics Engineering"
moduleGlyph: "📊"
moduleSortOrder: 5
topicSlug: reporting_systems
topicTitle: "Reporting Systems"
topicSortOrder: 4
lesson: 4
title: "Reporting Systems: Delivering the Right Data to the Right People"
sortOrder: 4
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
  - de-sen-m5-03
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes scheduled reports from self-serve and embedded analytics"
    - "Explains the role of data freshness SLAs and how they drive architecture choices"
    - "Describes report parameterisation and how it prevents report sprawl"
    - "Identifies performance patterns for heavy reports (materialised views, pre-aggregation)"
  keywords:
    - scheduled report
    - self-serve analytics
    - embedded analytics
    - data freshness
    - materialised view
    - report parameterisation
    - SLA
  modelAnswer: |
    Scheduled reports run automatically at a cadence and deliver results to recipients (email, Slack, dashboard refresh). Self-serve analytics lets users explore data ad-hoc using BI tools. Embedded analytics integrates analytics directly into a product — the user sees charts without leaving the application.
    Data freshness SLA defines how stale data can be before stakeholders are impacted. Executive metrics with a 24-hour SLA can be served from a nightly batch refresh. Real-time monitoring requires sub-minute freshness, driving streaming or micro-batch architectures. The freshness SLA should be driven by business need, not technical capability — fresher data costs more to produce and serve.
    Report parameterisation replaces a forest of similar reports (one per region, one per tier) with a single parameterised report that accepts filter inputs. This reduces maintenance burden and ensures consistency. Without parameterisation, each region's report is separately maintained and diverges over time.
    Heavy reports that scan billions of rows create warehouse load and slow response times. Materialised views pre-aggregate and cache results; they are refreshed on schedule. Pre-aggregated summary tables in the warehouse eliminate repeated aggregation. Query result caches (BI tool or application-layer) serve repeated identical queries without hitting the warehouse.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "An executive dashboard refreshes every 5 minutes, querying a BigQuery fact table with 2 billion rows. The dashboard is viewed 20 times per hour by 5 people. Each refresh costs $0.50. What is the monthly cost and what is the better approach?"
    options:
      - "$4,320/month. Better: cache the query result for 24 hours since executives only need daily data"
      - "$1,440/month. Better: increase to hourly refresh since executives check less frequently"
      - "$4,320/month. Better: use a materialised view refreshed hourly, serving the dashboard from the pre-aggregated result"
      - "$2,160/month. Better: add an index on the timestamp column"
    correctIndex: 2
    explanation: "5-minute refresh = 12/hour × 24 × 30 = 8,640 refreshes/month × $0.50 = $4,320/month. A materialised view pre-aggregates the result once per hour ($0.50 × 24 × 30 = $360/month) and serves all 5 users from the cached aggregate — $3,960/month savings. The business need is daily or hourly freshness for executives; 5-minute refresh is over-engineering at enormous cost."
  - type: FILL_BLANK
    question: "A ___ view stores the result of an aggregation query physically and refreshes on a schedule, serving subsequent queries from the pre-computed result rather than re-scanning the source table."
    answer: "materialised"
    explanation: "A materialised view (or materialised query table in BigQuery, materialization in dbt) stores the output of a SELECT query. Unlike a regular view (which re-executes on every query), a materialised view serves reads from the stored result. It is refreshed periodically (on a schedule or on source change). This reduces query cost and latency for expensive aggregations."
  - type: SHORT_TEXT
    question: "The Consortium has 50 separate daily CSV email reports — one per learning domain. A new domain is added monthly, requiring a new report. How would you redesign this using report parameterisation?"
    modelAnswer: "Replace all 50 reports with one parameterised report template. The report accepts `domain` as a parameter. The scheduler runs the same template once per domain at the configured time, passing the domain parameter. The output (CSV, PDF, or dashboard link) is delivered to domain-specific recipients. Adding a new domain requires one scheduler config entry, not a new report. The same SQL/template serves all domains with consistent logic — no divergence risk."
microCheckpoint:
  question: "What is the difference between a regular database view and a materialised view?"
  answer: "A regular view is a stored SQL query that executes fresh each time it is queried — no performance benefit for expensive aggregations. A materialised view stores the query result physically and serves reads from the cached result, refreshed on a schedule. Materialised views dramatically reduce query cost and latency for repeated aggregate queries."
retrieval:
  recall: "What three types of reporting systems exist in a typical organisation?"
  explain: "Explain why data freshness SLAs should be defined by business need rather than technical capability."
  mistakeId: "reporting-over-refreshing"
---

# The Report Factory

The data team managed 200 manually-run reports. Each Monday morning, an engineer spent 3 hours running queries, exporting CSVs, and emailing them to 40 people. "This is operational work that adds no value," the Lead Data Engineer said. "Every one of these should be automated, parameterised, and self-refreshing. Let's redesign this."

# Three Types of Reporting Systems

### Scheduled Reports
Automatically produced at a cadence and delivered to recipients.

```
Nightly at 02:00:
  → Run dbt + Spark jobs
  → Refresh BI dashboards
  → Email weekly digest to learner (personalised)
  → Post Slack summary to #data-ops channel
```

Low latency requirement (daily or weekly). High reliability requirement — stakeholders expect it in their inbox on time.

### Self-Serve Analytics
Users explore data on their own schedule using BI tools.

```
Analyst workflow:
  Opens Metabase / Looker
  Selects mart_lesson_engagement
  Filters: tier = 'Senior', domain = 'Data Engineering'
  Groups by: week
  Exports to CSV for slide deck
```

Requires accessible, well-documented mart tables. Users need some SQL or BI tool proficiency.

### Embedded Analytics
Analytics rendered inside a product UI — the user never leaves the application.

```
Learner profile page:
  ┌──────────────────────────────┐
  │ Your Progress This Month     │
  │  XP Earned: 1,250  ↑+12%    │
  │  Lessons Completed: 8        │
  │  Streak: 7 days 🔥           │
  │  [chart: XP by day this week]│
  └──────────────────────────────┘
```

The application calls an analytics API or embeds a BI iframe. Requires lower latency than batch (seconds, not minutes). Often powered by pre-aggregated summary tables or a cache layer.

## Data Freshness SLAs

The freshness SLA defines the maximum acceptable data lag for a report.

| Report | Audience | Freshness SLA | Architecture |
|---|---|---|---|
| Executive dashboard | C-suite | 24 hours | Nightly dbt run |
| Operational monitoring | Data team | 5 minutes | Micro-batch or streaming |
| Personal learner progress | Learners | 1 hour | Hourly materialised view refresh |
| Fraud alert | Fraud team | 30 seconds | Streaming pipeline |
| Monthly analytics report | Finance | T+1 day | Nightly batch |

**SLAs should be driven by business impact, not technical capability.** Building a 1-minute refresh for an executive report consumed monthly costs 1,440× more than a daily refresh and provides no business value. Always ask: "What is the worst case if this data is N hours old?"

## Report Parameterisation

```python
# BAD: 50 separate reports
class JuniorDataEngineeringReport: ...
class SeniorDataEngineeringReport: ...
class JuniorSoftwareEngineeringReport: ...
# ... 47 more

# GOOD: one parameterised report
class DomainTierProgressReport:
    def __init__(self, domain: str, tier: str, recipients: list[str]):
        self.domain = domain
        self.tier = tier
        self.recipients = recipients

    def generate(self) -> bytes:
        return warehouse.query(
            REPORT_SQL,
            domain=self.domain,
            tier=self.tier
        )
```

```yaml
# Scheduler config (one per combination, not one per codebase)
scheduled_reports:
  - report: DomainTierProgressReport
    params: {domain: data_engineering, tier: junior}
    cron: "0 8 * * 1"  # Monday 08:00
    recipients: [de-junior-lead@consortium.io]

  - report: DomainTierProgressReport
    params: {domain: software_engineering, tier: senior}
    cron: "0 8 * * 1"
    recipients: [se-senior-lead@consortium.io]
```

One template, zero code duplication, consistent logic across all variants.

## Performance Patterns for Heavy Reports

### Materialised Views

```sql
-- PostgreSQL materialised view — refreshed on schedule
CREATE MATERIALISED VIEW mv_daily_learner_stats AS
SELECT
    DATE_TRUNC('day', occurred_at) AS day,
    tier,
    domain,
    COUNT(DISTINCT user_id)        AS active_learners,
    SUM(xp_earned)                 AS total_xp,
    COUNT(*)                       AS lesson_completions
FROM fact_lesson_attempts
JOIN dim_user   USING (user_key)
JOIN dim_lesson USING (lesson_key)
WHERE occurred_at >= CURRENT_DATE - 90
GROUP BY 1, 2, 3;

-- Refresh nightly
SELECT cron.schedule('refresh-mv', '0 1 * * *',
    'REFRESH MATERIALISED VIEW CONCURRENTLY mv_daily_learner_stats');
```

### Pre-Aggregated Summary Tables
For warehouse queries that can't use materialised views, dbt builds summary tables:

```sql
-- dbt model: mart_daily_progress (materialised as table)
SELECT
    d.date_day,
    u.tier,
    l.domain_name,
    COUNT(DISTINCT la.user_key)   AS active_learners,
    SUM(la.xp_earned)             AS total_xp
FROM {{ ref('fact_lesson_attempts') }} la
JOIN {{ ref('dim_user')   }} u USING (user_key)
JOIN {{ ref('dim_lesson') }} l USING (lesson_key)
JOIN {{ ref('dim_date')   }} d USING (date_key)
GROUP BY 1, 2, 3
```

Dashboard queries `mart_daily_progress` instead of the raw 2-billion-row fact table — sub-second response times.

### Application-Layer Caching
For embedded analytics, cache API responses:

```java
@GetMapping("/learner/{userId}/progress")
@Cacheable(value = "learner-progress", key = "#userId", cacheManager = "redisCacheManager")
public LearnerProgressDto getProgress(@PathVariable String userId) {
    return analyticsService.computeLearnerProgress(userId);
}
// @CacheEvict triggered when new XP events arrive for the user
```

## Common Mistakes

> **Over-Refreshing**
> A 5-minute refresh for a daily metric costs 288× as much as a daily refresh and provides no value. Always match refresh cadence to the SLA.

> **Query-per-User at Scale**
> Embedded analytics that runs a warehouse query per user on page load will overwhelm the warehouse at 10,000 concurrent users. Pre-aggregate into user-level summary tables; cache results.

> **No Report Ownership**
> Reports with no owner are never updated when the underlying schema changes, never retired when no longer needed, and accumulate into a legacy swamp. Every report needs an owner and a review cadence.

## Mental Model

Think of reporting systems as **newspaper publishing**. Scheduled reports are the morning edition — printed once, delivered to subscribers. Self-serve analytics is the archive — anyone can look up old editions. Embedded analytics is the live ticker at the bottom of the screen — continuously updated, integrated into the reading experience. Materialised views are the printing plates — expensive to create, but once done, printing (serving) is cheap and fast. Publishing a newspaper every 5 minutes when readers only check it daily is wasteful; publishing once a day when there's a breaking story is inadequate.

**Mini Summary**: Reporting systems are scheduled (automatic delivery), self-serve (ad-hoc by analysts), or embedded (in-product). Data freshness SLAs should match business need — not technical capability. Parameterise reports to eliminate duplication. Heavy reports should query pre-aggregated materialised views or summary tables, not raw fact tables. Match refresh cadence to SLA; over-refreshing is expensive with no benefit.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium needs to deliver three types of reports:
1. A weekly personalised progress email to each learner showing their XP, lessons completed, and rank
2. A self-serve BI dashboard for the content team to explore lesson engagement
3. An embedded analytics widget in the learner profile showing real-time XP progress

For each:
1. Define the data freshness SLA (and justify it from business impact)
2. Describe the architecture that delivers that SLA (batch, materialised view, streaming, cache)
3. Identify one performance or scale risk and how to mitigate it

---

# Integration

**Mathematics**: Report scheduling optimisation is an application of **queuing theory**. Reports are jobs; the warehouse is the server. The average response time E[T] = E[S]/(1 - ρ) where E[S] is average service time and ρ = λ/μ is server utilisation. As ρ → 1 (warehouse near capacity), E[T] → ∞. Materialised views reduce E[S] (service time per query) by pre-computing results. Staggering report refresh times reduces λ (arrival rate) during peak periods. The key insight: a warehouse at 80% utilisation serves queries twice as fast as one at 90% utilisation — marginal capacity has super-linear value.

**Sciences**: The three report types mirror **sensory systems** in biology. Scheduled reports are **circadian rhythms** — regular, predictable biological cycles (morning cortisol, sleep/wake cycles) that don't require real-time triggers. Self-serve analytics is **conscious attention** — goal-directed, voluntary, initiated by the observer when needed. Embedded analytics is **proprioception** — continuous background awareness of state (body position, orientation) integrated seamlessly into the organism's model of itself. Each system has different latency, energy cost, and role in maintaining the organism's (organisation's) homeostasis.

---

# The Automated Monday

The 200 manual reports became 12 parameterised templates. The Monday morning routine — three hours, one engineer — was replaced by a scheduler job that ran at 07:50 and delivered 200 personalised emails before anyone arrived at work. The data team's Monday morning now started with coffee and analysis, not copy-paste. "The automation doesn't just save time," the Lead Data Engineer said. "It means the reports are always consistent. No human error, no forgotten edge cases." The Senior Engineer closed the old script. It had served its purpose. It would not be missed.
