---
id: se-sen-m8-02
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m8
moduleTitle: "Module 8: Observability"
moduleGlyph: "🔭"
moduleSortOrder: 8
topicSlug: metrics
topicTitle: "Metrics"
topicSortOrder: 2
lesson: metrics
title: "Metrics"
sortOrder: 2
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [logging]
integrationDomains: [mathematics, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains the four metric types: counter, gauge, histogram, summary"
    - "Applies the RED method (Rate, Errors, Duration) to a service"
    - "Shows how to create a custom metric with Micrometer"
    - "Describes the cardinality problem with metric labels"
    - "Explains what Prometheus scraping is at a high level"
  keywords: [counter, gauge, histogram, red, rate, error, duration, micrometer, prometheus, cardinality]
  modelAnswer: |
    Four metric types:
    - Counter: monotonically increasing count (requests, errors)
    - Gauge: current value that can go up or down (active users, queue depth)
    - Histogram: distribution of values in configurable buckets (request latency p50/p95/p99)
    - Summary: similar to histogram but calculated client-side (less common)

    RED method for OrderService:
    - Rate: requests_total per second
    - Errors: requests_errors_total / requests_total (error rate %)
    - Duration: request_duration_seconds histogram → p99 latency

    Micrometer example:
    Counter orderCounter = registry.counter("orders.placed", "status", "success");
    orderCounter.increment();

    Timer orderTimer = Timer.builder("orders.processing.time")
        .description("Time to process order")
        .register(registry);
    orderTimer.record(() -> processOrder(order));

    Cardinality problem: using userId as a tag creates millions of unique time series.
    Use low-cardinality tags only: status, endpoint, method.
guidedSteps:
  - id: met-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You want to track the total number of HTTP requests your service has received.
      This number only ever increases. Which metric type is most appropriate?
    inputConfig:
      options:
        - "Gauge — because it shows the current request count"
        - "Counter — because it monotonically increases and represents a cumulative count"
        - "Histogram — because requests have different durations"
        - "Summary — because you need percentiles"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Counter — because it monotonically increases and represents a cumulative count"]
      rejectedFeedback: "**Counter** is for values that only ever increase (total requests, total errors, total bytes sent). Rate-of-change is calculated from counters by monitoring tools. Gauge is for values that can decrease (active connections, queue size)."
    hint: "Does the total request count ever decrease? That's your clue."
    reflectionPrompt: "Counters alone aren't very useful — what you care about is the rate of change (requests per second). Prometheus calculates this with `rate(counter[5m])`. The counter always increases; the rate function makes it useful."
  - id: met-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The RED method for service metrics stands for Rate, Errors, and ___.
    inputConfig:
      placeholder: "the third dimension"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Duration", "duration", "Latency", "latency"]
      rejectedFeedback: "RED: **Rate** (requests per second), **Errors** (error rate), **Duration** (latency — how long requests take). These three metrics describe the health and performance of any service from the perspective of its callers."
    hint: "Rate, Errors, and ___. How long does each request take?"
    reflectionPrompt: "RED is for request-driven services. The companion for resource metrics (CPU, memory, disk) is USE: Utilisation, Saturation, Errors. Use RED for your services; USE for your infrastructure."
  - id: met-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the "cardinality problem" in metrics. Give an example of a high-cardinality label that would cause it, and explain the consequence.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [cardinality, label, tag, user, id, unique, time, series, memory, cost, millions]
      rejectedFeedback: "High cardinality: a label with many unique values creates a separate time series for each value. Using `userId` as a label: 1 million users = 1 million time series for each metric. Prometheus stores each as a separate set of data points — this exhausts memory and crashes the monitoring system. Use low-cardinality labels only (status_code, endpoint, method — never userId, sessionId, requestId)."
    hint: "If a label has millions of possible values, how many different versions of the metric does your monitoring system need to store?"
    reflectionPrompt: "The cardinality rule: labels should have small, bounded sets of values (status codes: ~10; HTTP methods: ~5; endpoints: ~100). Never use IDs, tokens, timestamps, or any unbounded value as a label."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which metric type would you use to track the 99th percentile latency of API requests?"
    options:
      - "Counter"
      - "Gauge"
      - "Histogram"
      - "Log"
    correctIndex: 2
    feedback: "Histograms record the distribution of values in configurable buckets (e.g. <10ms, <50ms, <100ms, <500ms). From this distribution, Prometheus can calculate percentiles (p50, p95, p99). A p99 of 500ms means 99% of requests complete within 500ms."
  - type: MULTIPLE_CHOICE
    question: "In Micrometer, how do you register a custom counter with a tag?"
    options:
      - "`new Counter(\"name\")`"
      - "`registry.counter(\"name\", \"tagName\", \"tagValue\")`"
      - "`Metrics.counter(\"name\")`"
      - "`@Counted(\"name\")`"
    correctIndex: 1
    feedback: "`registry.counter(\"name\", \"tagKey\", \"tagValue\")` creates a counter with a dimensional tag. Micrometer supports Prometheus, CloudWatch, Datadog, and others — the same code works with any backend. `@Counted` (Spring Boot auto-configuration) works for method-level counting."

retrieval:
  recall: "What are the four Micrometer metric types and when is each used? Describe the RED method."
  explain: "Explain to a junior developer why p99 latency is more useful than average latency for understanding user experience."
  mistakeId:
    code: |
      // Tracking per-user request counts
      registry.counter("api.requests",
          "userId", String.valueOf(userId),
          "endpoint", endpoint)
          .increment();
    answer: "Using `userId` as a metric label is a cardinality bomb. With 1M users × 100 endpoints = 100M unique time series. Prometheus will run out of memory. Remove the `userId` tag. If per-user analytics are needed, use application-level logs or a separate analytics pipeline, not metrics."
---

# Hook

Your API is slow. Is it the database? The payment service? The authentication layer? A specific endpoint? Certain users?

Logs tell you about individual events. Metrics tell you about aggregate system behaviour over time. A histogram of request latencies shows you the p99 — the slowest 1% of requests that real users experience. A counter of error rates tells you whether you're degrading.

Metrics answer the "what is happening right now, at scale?" question that logs cannot.

> In a system you've worked on, what metrics would have immediately told you that something was wrong before users started complaining?

# Lore Introduction

The Academy's master artificers don't wait for failures to be reported. The Grand Observatory tracks hundreds of metrics for every ward, every seal, every enchantment relay across the building. When the north mana flow drops below threshold, the Observatory knows before any apprentice notices.

*"A failure you detect yourself is a failure you control,"* Archmage Veylan says. *"A failure a student reports is one you're already behind on."*

# Core Learning

## Concept Introduction

**Metrics** are numeric measurements of system state, collected over time.

**Four metric types (Micrometer):**

| Type | Description | Example |
|------|-------------|---------|
| Counter | Monotonically increasing count | Total requests, total errors |
| Gauge | Current value (can decrease) | Active connections, queue depth |
| Histogram | Distribution in buckets | Request latency (p50/p95/p99) |
| Timer | Duration + count | Method execution time |

**RED method** (for request-driven services):
- **R**ate — requests per second
- **E**rrors — error rate (%)
- **D**uration — request latency (p50, p95, p99)

## Why It Matters

Metrics enable:
- **Alerting** — trigger alerts when error rate exceeds threshold
- **Performance visibility** — identify which endpoint has degraded p99 latency
- **Capacity planning** — trend analysis shows when you'll exhaust capacity
- **SLO tracking** — measure whether you're meeting your commitments
- **Debugging** — correlate metric spikes with deployments or external events

## Worked Examples

**Micrometer with Spring Boot (auto-configured):**
```java
// Spring Boot auto-configures /actuator/prometheus endpoint
// Just add Micrometer Prometheus dependency
```

**Custom metrics:**
```java
@Service
public class OrderService {
    private final Counter orderCounter;
    private final Timer orderTimer;

    public OrderService(MeterRegistry registry) {
        this.orderCounter = Counter.builder("orders.placed")
            .tag("status", "success")
            .description("Total orders placed successfully")
            .register(registry);
        this.orderTimer = Timer.builder("orders.processing.duration")
            .description("Time to process an order")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
    }

    public Order placeOrder(OrderRequest request) {
        return orderTimer.record(() -> {
            Order order = processOrderInternal(request);
            orderCounter.increment();
            return order;
        });
    }
}
```

**Prometheus scrape config:**
```yaml
 # prometheus.yml
scrape_configs:
  - job_name: 'order-service'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['order-service:8080']
    scrape_interval: 15s
```

## Common Mistakes

- **High-cardinality labels** — using `userId`, `requestId`, or `sessionId` as labels creates millions of time series.
- **Only tracking averages** — averages hide outliers; use histograms and p99.
- **No business metrics** — infrastructure metrics alone don't tell you if the business is healthy.
- **Ignoring metric naming conventions** — `orders_placed_total` (counter), `queue_depth` (gauge), `request_duration_seconds` (histogram) — consistency aids discovery.
- **Not alerting on metrics** — collected metrics with no alerts provide visibility but no proactive response.

## Mental Model

Metrics are a **vital signs monitor** in a hospital ICU. Heart rate, blood pressure, oxygen saturation — all measuring continuously, trending over time, alerting on thresholds. You don't read each individual log of the patient's heartbeat; you look at the trend and the alert. Metrics are the system's vital signs.

## Mini Summary

- ✔ Four types: Counter (cumulative), Gauge (current), Histogram (distribution), Timer (duration)
- ✔ RED method: Rate + Errors + Duration — minimum viable service metrics
- ✔ Micrometer provides vendor-neutral metric instrumentation for Spring Boot
- ✔ Labels/tags must have low cardinality — never use IDs or unbounded values
- ✔ Use histograms for latency — p99 shows user impact that averages hide

# Guided Practice Quest

**The Grand Observatory**

Instrument an `OrderService` with RED metrics. Configure appropriate labels and percentile tracking.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are responsible for defining the metrics strategy for a new ride-sharing service. The service has these components:
- `MatchingService` — matches drivers to passengers
- `PaymentService` — processes fares
- `NotificationService` — sends push notifications

For each service, define:
1. The RED metrics (Rate, Errors, Duration) with specific metric names and units
2. Two business-level metrics specific to that service (not just infrastructure)
3. For each metric: what labels would you add, why, and what high-cardinality pitfalls to avoid
4. What alerts would you configure on these metrics?

Then explain: what does "p99 latency of 2 seconds for the MatchingService" tell you about user experience?

# Integration

**Connecting to Mathematics — Statistical Thinking for Operational Data**

Traditional software engineering focuses on correctness (does it work?). Operations engineering requires statistical thinking: systems behave probabilistically. A service is not "working" or "broken" — it's degraded to varying degrees for varying proportions of users.

The shift from averages to percentiles embodies this. An average request latency of 100ms can coexist with p99 latency of 5 seconds — 1% of users experiencing terrible performance, invisible in the average. Percentiles (p50, p95, p99) are order statistics that describe the shape of the distribution, not just its centre.

Extreme-value theory (Fisher and Tippett, 1928) studies the statistical behaviour of the worst outcomes in a distribution. For tail latencies, the key insight is that the worst cases are often driven by qualitatively different phenomena than the median: garbage collection pauses, lock contention, network retries. P99 is not "average + a bit" — it's a different regime requiring different investigation.

Understanding this mathematically changes how you instrument and alert. Alerting on averages misses tail degradations that affect real users. Alerting on p99 catches the regime that matters for experience.

How does statistical thinking about distributions change how you would design your alerting thresholds?

# Lore Conclusion

The Observatory glows with a hundred metric streams. Rate nominal. Error rate below threshold. P99 latency within SLO.

*"The Observatory sees what no individual could,"* Archmage Veylan says. *"Not individual events — patterns across time, across the whole system. That is what metrics provide: the aggregate truth that individual logs cannot."*

Instrument everything that matters. Alert on everything that degrades. Trust the data, not intuition.
---
