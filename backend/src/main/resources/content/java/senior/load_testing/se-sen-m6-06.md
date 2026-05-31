---
id: se-sen-m6-06
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m6
moduleTitle: "Module 6: Performance Engineering"
moduleGlyph: "🚀"
moduleSortOrder: 6
topicSlug: load_testing
topicTitle: "Load Testing"
topicSortOrder: 6
lesson: load_testing
title: "Load Testing"
sortOrder: 6
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [caching_strategies]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Distinguishes load, stress, soak, and spike testing and explains the specific failure modes each targets"
    - "Defines SLO components (p99 latency, error rate, throughput) and explains why percentiles matter more than averages"
    - "Explains the purpose of finding the 'breaking point' and how throughput degrades after it"
    - "Describes why production-like test data is critical to load test validity"
    - "Names at least one load testing tool (Gatling, k6) and describes its scripting model"
  keywords: [load testing, stress testing, soak testing, spike testing, SLO, p99 latency, throughput, error rate, Gatling, k6, breaking point, capacity planning, production-like data]
  modelAnswer: |
    Load testing validates that a system meets its performance requirements under expected and exceptional traffic conditions. Four test types address different failure modes. Load testing applies expected production load sustained over time — it validates that the system meets SLOs under normal conditions. Stress testing applies load beyond expected peak to find the breaking point — the load level at which latency or error rates breach SLOs. Soak testing (endurance testing) applies sustained normal load for hours or days — it reveals memory leaks, connection pool exhaustion, and degradation over time that do not appear in short tests. Spike testing applies sudden, extreme load increases — it validates autoscaling responses and whether the system recovers after a traffic spike.

    SLOs are defined in terms of measurable signals. Latency p99 means 99% of requests complete within the threshold — the 99th percentile of the latency distribution. Averages are misleading: a service averaging 100ms may have 10% of requests at 1,000ms (an unacceptable user experience). The p99 captures the "tail latency" that affects 1% of users — on a service handling 1,000 requests/second, 1% is 10 users per second experiencing degradation. Error rate is the fraction of requests that fail with 5xx or timeout — typically SLO'd at <0.1% for critical services. Throughput is requests/second sustained at acceptable latency and error rate.

    The breaking point is the load level at which the system transitions from healthy performance to degraded performance. Below the breaking point, adding load increases throughput linearly. At the breaking point, a resource saturates (CPU, database connections, memory) and further load causes latency to increase superlinearly while throughput plateaus. Beyond the breaking point, queue buildup causes latency to spike and error rate to increase. Finding the breaking point through stress testing reveals the headroom between production traffic and failure — the safety margin.

    Production-like test data is critical because query plans, cache hit rates, and index effectiveness depend on data distribution. A test dataset with 1,000 uniform records produces different performance characteristics than a production dataset with 10 million records and skewed access patterns (the Pareto principle: 20% of products get 80% of views). A load test that "passes" with small uniform data may fail catastrophically in production with real data. Test data must reflect: volume (realistic record counts), distribution (skewed access patterns matching production), and relationship complexity (realistic join depths and association cardinality).

    Gatling is a Scala-based load testing tool that models scenarios as protocols (HTTP, WebSocket) with simulations defining user populations and injection profiles. k6 is a JavaScript-based tool with a simpler scripting model, better CI/CD integration, and a SaaS cloud run option. Both generate detailed latency percentile reports. k6 threshold configuration allows tests to fail the CI pipeline if SLOs are breached.
guidedSteps:
  - id: lt-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A service runs perfectly in short load tests but crashes after 18 hours in production with OutOfMemoryError. Which load test type would have caught this?
    inputConfig:
      options:
        - "A. Spike test — tests sudden traffic increases"
        - "B. Stress test — tests above peak load"
        - "C. Soak test — sustained normal load over hours/days reveals memory leaks and resource exhaustion"
        - "D. Load test — validates SLOs under expected load"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["C"]
      rejectedFeedback: "Memory leaks and resource exhaustion are time-dependent failures — they accumulate during extended operation. A soak test (endurance test) runs sustained normal load for 8-24+ hours, long enough for a slow memory leak to manifest. Short load and stress tests cannot detect issues that only appear after sustained operation."
    hint: "Which test type is specifically designed for time-dependent failures?"
    reflectionPrompt: "Many production failures are time-dependent: they do not appear in short tests but accumulate over hours of operation."
  - id: lt-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In a load test, p99 latency of 850ms means ___ % of requests complete in 850ms or less.
    inputConfig:
      placeholder: "percentage"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["99", "99%", "ninety-nine"]
      rejectedFeedback: "p99 (99th percentile) means 99% of requests complete within 850ms. The remaining 1% take longer. On a service with 1,000 RPS, this means 10 users/second experience >850ms latency. Percentile metrics capture tail latency that averages hide."
    hint: "The p in p99 stands for percentile."
    reflectionPrompt: "Averages lie. A service with 100ms average and 2,000ms p99 is unacceptable despite the average looking fine."
  - id: lt-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A team runs a load test against their checkout service using a dataset of 100 products, all equally popular, with 50 test users. In production, the service handles 500,000 products with 80% of traffic on 1,000 "bestseller" products. Explain why the load test results may not represent production behaviour.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [data distribution, cache hit rate, index, Pareto, skewed, realistic, production-like, hot products]
      rejectedFeedback: "The test dataset does not reflect production data distribution. In production, 80% of traffic on 1,000 products creates a high cache hit rate for those products. The test dataset with 100 equally popular products may produce a different cache hit rate, different query plans, and different index effectiveness. Load tests with unrealistic data distributions are unreliable predictors of production performance."
    hint: "Consider what production-specific characteristics (cache behaviour, index effectiveness) depend on data distribution."
    reflectionPrompt: "Load test validity depends entirely on how well the test environment reflects production conditions — data is as important as load."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What happens to throughput beyond the 'breaking point' in a stress test?"
    options:
      - "A. Throughput continues to increase linearly with load"
      - "B. Throughput plateaus and then decreases as queuing and errors increase"
      - "C. Throughput doubles as the JVM's JIT optimises under load"
      - "D. Throughput becomes unpredictable but latency remains stable"
    correctIndex: 1
    feedback: "Beyond the breaking point, a saturated resource (CPU, connections, memory) causes queue buildup. Latency spikes superlinearly, error rates increase, and throughput actually decreases as the system spends resources on timeouts and retries rather than useful work. This is the classic 'hockey stick' latency curve."
  - type: MULTIPLE_CHOICE
    question: "Why is capacity planning based on average latency rather than p99 latency dangerous?"
    options:
      - "A. Averages always overestimate latency"
      - "B. A high average implies high p99"
      - "C. A low average can coexist with high p99 tail latency, hiding a significant fraction of users experiencing unacceptable performance"
      - "D. p99 is too expensive to compute in load testing tools"
    correctIndex: 2
    feedback: "A bimodal distribution (most requests fast, some very slow) can produce a low average while having a high p99. On high-traffic services, even 1% of requests at bad latency represents thousands of users per hour. SLOs based on p99 capture this; SLOs based on averages mask it."
retrieval:
  recall: "Name the four load test types and describe the specific failure mode each is designed to detect."
  explain: "Explain to a junior developer why p99 latency is a more meaningful SLO metric than average latency."
  mistakeId:
    code: |
      # k6 load test script
      import http from 'k6/http';

      export default function () {
          http.get('https://api.example.com/products/1');  // Always product ID 1
      }

      export let options = {
          vus: 100,
          duration: '30s',
      };
    answer: "This test only requests product ID 1, which will be cached immediately and served from cache for all 100 virtual users. The test measures cache hit performance, not realistic production load. In production, requests are distributed across thousands of product IDs. The test should use randomised or realistic product IDs drawn from a production-like distribution. Additionally, 30 seconds is too short for a soak test — it cannot detect memory leaks or connection pool exhaustion."
---

# Hook

The launch is three weeks away. The product team is confident — it worked perfectly in QA. The load test team runs a 30-minute test with 100 virtual users and 100 records: passes. They declare the system ready. On launch day, 50,000 real users with 2 million real records arrive. The database connection pool exhausts in 90 seconds. Response times climb to 30 seconds. The site goes dark during the biggest traffic event of the year. The load test passed — the system failed. The difference: production-like data, production-like scale, and a duration long enough to reveal the resource leak that had been accumulating slowly in every soak test never run.

# Lore Introduction

The Academy's performance masters test their spells not just for power but for endurance. A spell that works for 30 seconds under controlled conditions but fails after 18 hours of continuous casting is not a production-ready spell. Load testing is the art of simulating reality before reality arrives — creating the conditions under which the system will be stressed, sustained, and spiked, and observing its behaviour before the consequences are real. The SLO is the contract the system makes with its users. The load test is the proof that the contract is honoured.

# Core Learning

## Concept Introduction

**Test Type Matrix**

| Type | Load Level | Duration | Primary Goal |
|------|-----------|----------|--------------|
| Load | Expected production | 30-60 min | Validate SLOs at normal traffic |
| Stress | Above peak | Until failure | Find breaking point, headroom |
| Soak | Normal load | 8-24+ hours | Find memory leaks, resource exhaustion |
| Spike | Sudden extreme burst | Minutes | Test autoscaling, recovery |

**SLO Definitions**

- **Latency p99**: 99th percentile response time. "`≤500ms p99`" means 99% of requests complete in ≤500ms.
- **Error rate**: `(5xx responses + timeouts) / total requests`. Typically SLO'd at `<0.1%`.
- **Throughput**: requests/second sustained while meeting latency and error rate SLOs.

**The Breaking Point**

```
Throughput
     |            *
     |          **
     |        **
     |      **          ← Breaking point
     |    **________
     |  **           \
     | **             \  (system degrading)
     |**               \
     +------------------------→ Load (VUs)
```

At the breaking point, a resource saturates. Beyond it, queueing causes latency to spike and throughput to plateau or decrease. The distance between production traffic and the breaking point is the safety margin.

**k6 Example Script**

```javascript
import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '2m', target: 100 },  // Ramp up
        { duration: '5m', target: 100 },  // Sustain
        { duration: '1m', target: 0 },    // Ramp down
    ],
    thresholds: {
        http_req_duration: ['p(99)<500'],  // p99 < 500ms
        http_req_failed: ['rate<0.001'],   // Error rate < 0.1%
    },
};

export default function () {
    const productId = Math.floor(Math.random() * 50000) + 1; // Realistic distribution
    const res = http.get(`https://api.example.com/products/${productId}`);
    check(res, { 'status 200': (r) => r.status === 200 });
    sleep(1);
}
```

## Why It Matters

Performance SLOs are commitments to users. Load testing is the only way to verify those commitments before production traffic arrives. The cost of discovering performance problems in production — lost revenue, user trust, SLA penalties — vastly exceeds the cost of a load testing program. Load testing also enables capacity planning: knowing the breaking point allows engineering teams to provision infrastructure with appropriate headroom rather than guessing at peak traffic targets.

## Worked Examples

**Example 1: Gatling Scenario**

```scala
class CheckoutSimulation extends Simulation {
  val httpProtocol = http.baseUrl("https://api.example.com")
  
  val scn = scenario("Checkout")
    .exec(http("Add to cart")
      .post("/cart")
      .body(ElFileBody("cart_request.json")))
    .pause(2)
    .exec(http("Checkout")
      .post("/checkout")
      .check(status.is(200))
      .check(responseTimeInMillis.lt(500)))  // p99 assertion
  
  setUp(
    scn.inject(
      rampUsersPerSec(1) to 100 during (2 minutes),
      constantUsersPerSec(100) during (5 minutes)
    )
  ).protocols(httpProtocol)
   .assertions(
     global.responseTime.percentile3.lt(500),  // p99 < 500ms
     global.failedRequests.percent.lt(0.1)      // Error rate < 0.1%
   )
}
```

**Example 2: Finding the Breaking Point**

Step test: incrementally increase VUs (50, 100, 200, 400, 800) and measure throughput + p99 latency at each step. Plot the results:

```
VUs  | Throughput (RPS) | p99 Latency
-----|-----------------|------------
50   | 240             | 85ms
100  | 470             | 98ms
200  | 850             | 120ms
400  | 1,100           | 380ms    ← Breaking point beginning
800  | 900             | 4,200ms  ← System degrading
```

Breaking point: ~400 VUs. With production peak at 200 VUs, the safety margin is 2x. Target: provision to handle 400 VUs to allow 100% traffic growth before hitting the breaking point.

**Example 3: Soak Test Memory Leak Detection**

```bash
# Run k6 soak test with JVM monitoring
k6 run --duration=8h soak_test.js &

# Monitor JVM heap in parallel
watch -n 30 "jcmd <pid> VM.native_memory summary"

# Watch for: heap growing consistently without returning to baseline
# Baseline: 512MB
# After 2h: 680MB
# After 4h: 850MB
# After 8h: 1.1GB → OOM imminent → memory leak confirmed
```

## Common Mistakes

- **Testing only the happy path**: real users cancel orders, submit invalid data, and retry. Test scenarios must include error paths and retries.
- **Ignoring warm-up**: the first minutes of a load test hit a cold JVM and cold cache. Measure performance after the system has stabilised, not during ramp-up.
- **Single-region testing**: testing from a single location measures network latency from that location, not production latency from globally distributed users.
- **Not testing with realistic concurrency**: 100 sequential requests is not the same as 100 concurrent requests. Concurrency exposes lock contention and pool exhaustion that sequential tests miss.
- **Ignoring infrastructure limits**: load testing must stress the component under test, not the load generator machine. If the k6 runner runs out of CPU or network bandwidth, results are invalid.

## Mental Model

Load testing is a rehearsal. The system is the performer. The load test is the audience, growing from polite to overwhelming. The SLO is the performance standard. The breaking point is where the performance degrades below standard. Soak testing is running the performance every night for a week to ensure it holds up over time. Production-like test data ensures the rehearsal resembles the actual performance venue — a concert hall rehearsal in a bathroom stall produces misleading results.

## Mini Summary

- ✔ Load, stress, soak, and spike tests serve different purposes: normal validation, breaking point, time-based degradation, and recovery respectively
- ✔ p99 latency captures tail latency; averages hide the worst user experiences
- ✔ The breaking point is the load level where throughput plateaus and latency spikes; the gap to production traffic is the safety margin
- ✔ Production-like test data (volume, distribution, access skew) is as important as load volume for test validity
- ✔ Load test thresholds in CI pipelines enforce SLOs automatically — tests fail if p99 or error rate breaches the defined threshold

# Guided Practice Quest

Work through the guided steps above. For the test data question, think beyond "more records" — consider what specific characteristics of production data (access distribution, relationship complexity) affect performance in ways that uniform test data cannot.

# Solo Practice Quest

You are responsible for load testing a new payment processing service before a major campaign launch. Expected peak: 5,000 concurrent users, 500 payments/second. SLOs: p99 latency <300ms, error rate <0.01%, throughput >450 payments/second sustained.

Design a complete load test program:
1. Which test types will you run and in what order?
2. Write a k6 test stage profile (ramp-up, sustain, ramp-down) for the load test
3. How will you construct production-like test data for the payment scenarios?
4. What monitoring will you run in parallel with the load test?
5. Define pass/fail criteria for each test type and what action each failure triggers

# Integration

**Connecting to Mathematics — Queuing Theory and Little's Law**

Load testing results are best interpreted through queuing theory. Little's Law: `L = λW` where L is the average number of requests in the system (in-flight), λ is the arrival rate (requests/second), and W is the average sojourn time (latency). At 100 RPS with 50ms average latency: L = 100 × 0.05 = 5 concurrent requests in the system. At the breaking point, W increases (queue builds up) causing L to increase, which further increases W — a feedback loop that explains the superlinear latency growth observed in stress tests. M/M/1 queuing models predict that utilisation above 80% leads to rapidly increasing queue lengths: at 90% utilisation, average queue length is 9; at 95%, it is 19; at 99%, it is 99. The safety margin from the breaking point is not optional comfort — it is a mathematical requirement for linear (non-queuing) behaviour. How does understanding Little's Law change how you set capacity planning targets?

**Connecting to Psychology — Cognitive Biases in Performance Estimation**

Performance engineers are subject to the same cognitive biases as all engineers. Optimism bias leads teams to test the minimum expected load rather than planning for the maximum. Confirmation bias leads engineers to interpret load test results as confirming readiness even when warning signals (p95 above threshold, slow creep in memory) are present. The "narrative fallacy" leads teams to explain away poor load test results with post-hoc rationalisations rather than treating them as evidence of real problems. Effective load test programs counter these biases through structured pass/fail criteria defined before the test, automated thresholds that fail the test objectively, and review processes where load test results are scrutinised by engineers who did not build the system. How does your team's review process for load test results guard against confirmation bias?

# Lore Conclusion

The load test is the controlled crisis before the real crisis. Every broken SLO discovered in load testing is a production incident that never happened. Every soak test that reveals a memory leak is an OOM restart that never woke an on-call engineer at 3AM. The senior engineer treats load testing with the same rigour as unit testing: it must be automated, it must use production-like conditions, and it must fail loudly when SLOs are breached. The cost of a thorough load test program is measured in hours. The cost of skipping it is measured in outages.
