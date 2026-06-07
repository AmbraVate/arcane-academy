---
id: se-sen-m4-06
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m4
moduleTitle: "Module 4: Distributed Systems"
moduleGlyph: "🌐"
moduleSortOrder: 4
topicSlug: distributed_failures
topicTitle: "Distributed Failures"
topicSortOrder: 6
lesson: distributed_failures
title: "Distributed Failures"
sortOrder: 6
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [service_communication]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Names the three failure types: crash, omission, Byzantine"
    - "Explains partial failure and why it is harder than total failure"
    - "Identifies at least three fallacies of distributed computing"
    - "Describes graceful degradation with a concrete example"
    - "Explains the principle of chaos engineering"
  keywords: [crash failure, omission failure, Byzantine, partial failure, fallacies, graceful degradation, chaos engineering, fault injection, latency, timeout]
  modelAnswer: |
    Three failure types:
    - Crash: node stops completely and is detectable
    - Omission: node fails to send/receive some messages (harder to detect)
    - Byzantine: node behaves arbitrarily — may send incorrect or malicious data

    Partial failure: some components fail while others continue. Unlike a single machine
    crash, partial failure means the system is in an unknown, mixed state — some requests
    succeed, some fail, some timeout. Harder than total failure because you cannot
    assume clean state.

    Fallacies of distributed computing (8 fallacies by Deutsch/Gosling):
    1. The network is reliable
    2. Latency is zero
    3. Bandwidth is infinite
    4. The network is secure
    5. Topology doesn't change
    6. There is one administrator
    7. Transport cost is zero
    8. The network is homogeneous

    Graceful degradation: serve reduced functionality rather than complete failure.
    Example: payment service down → show "Pay later" option; recommendation engine down
    → show top-10 global recommendations; search index stale → serve cached results.

    Chaos engineering: deliberately inject failures in production (or staging) to
    discover failure modes before real incidents expose them.
    Tools: Chaos Monkey, Gremlin, Chaos Toolkit.
guidedSteps:
  - id: df-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A service sends a response but it arrives corrupted, causing the recipient to make a wrong business decision based on bad data. Which failure type is this?
    inputConfig:
      options:
        - "Crash failure — the service effectively crashed"
        - "Omission failure — the message was not received properly"
        - "Byzantine failure — the node sends incorrect or arbitrary data"
        - "Timing failure — the response arrived too late"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Byzantine failure — the node sends incorrect or arbitrary data"]
      rejectedFeedback: "Byzantine failures occur when a node behaves arbitrarily — including sending incorrect, malformed, or malicious data. Unlike crash or omission failures, Byzantine failures are the hardest to detect because the node appears to be functioning (it responds) but its responses are wrong."
    hint: "The node is responding, but its response cannot be trusted."
    reflectionPrompt: "Byzantine failures require Byzantine Fault Tolerant (BFT) protocols to handle, which are expensive. Most enterprise systems only design for crash and omission failures."
  - id: df-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The widely-cited list of incorrect assumptions that developers make about distributed systems is known as the ___ of Distributed Computing.
    inputConfig:
      placeholder: "key word (plural)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["fallacies", "Fallacies"]
      rejectedFeedback: "The 8 Fallacies of Distributed Computing (originally by L. Peter Deutsch, extended by James Gosling) lists incorrect assumptions developers commonly make, starting with 'The network is reliable.'"
    hint: "These are incorrect beliefs that developers hold about distributed systems."
    reflectionPrompt: "Review all 8 fallacies. Each one represents a class of distributed system bugs that appear because code was written assuming the fallacy is true."
  - id: df-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Your recommendation engine is down. Describe how you would implement graceful degradation for a product page that relies on personalised recommendations, and explain the user experience trade-off.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [fallback, cached, static, popular, top, degraded, circuit breaker, partial, feature flag]
      rejectedFeedback: "Graceful degradation options: (1) Return globally popular products (cached top-10 list) instead of personalised recommendations; (2) Return cached recommendations from a previous successful call; (3) Hide the recommendations section entirely; (4) Show a static editorial list. The trade-off: users get a less personalised experience, but the page loads and is functional rather than failing entirely."
    hint: "What could you show instead of personalised recommendations that would still be better than an error page?"
    reflectionPrompt: "Graceful degradation is a spectrum — from full functionality to reduced functionality to informative degradation to complete but clean failure."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the 8 Fallacies of Distributed Computing does a developer violate when they write synchronous HTTP calls with no timeout?"
    options:
      - "The network is homogeneous"
      - "Topology doesn't change"
      - "The network is reliable and latency is zero"
      - "There is one administrator"
    correctIndex: 2
    feedback: "A no-timeout call assumes the network is reliable (will always respond) and latency is zero (will respond quickly). Both are fallacies — networks drop packets and remote services can be slow or unresponsive, hanging the calling thread indefinitely."
  - type: MULTIPLE_CHOICE
    question: "Chaos engineering at Netflix's Chaos Monkey specifically simulates which type of failure?"
    options:
      - "Byzantine failures — incorrect responses from services"
      - "Omission failures — lost network packets"
      - "Crash failures — random instance termination in production"
      - "Timing failures — network latency injection"
    correctIndex: 2
    feedback: "Chaos Monkey randomly terminates production instances to ensure that the system can tolerate instance crashes and that engineers build resilient services rather than depending on any single instance being continuously available."
retrieval:
  recall: "Name the three failure types in distributed systems and explain why partial failure is more challenging than total node failure."
  explain: "Explain the concept of graceful degradation to a junior developer, using the example of a shopping website whose payment service is unavailable."
  mistakeId:
    code: |
      @GetMapping("/product/{id}")
      public ProductPage getProduct(@PathVariable String id) {
          Product product = productService.getById(id);
          List<Review> reviews = reviewService.getReviews(id);
          List<Product> recommendations = recommendationService.getRecommendations(id);
          // All three must succeed or the whole page fails
          return new ProductPage(product, reviews, recommendations);
      }
    answer: "All three service calls are in the critical path with no fallback. If reviewService or recommendationService is slow or down, the entire product page fails. Each non-critical component (reviews, recommendations) should have a timeout and graceful fallback (empty list, cached data, static defaults). Only productService should be required — if that fails, an error page is appropriate."
---

# Hook

A standard workshop can tolerate a broken hammer — the smith puts it down and picks up another. A distributed system cannot work this way. When one service fails, it may fail silently, partially, or deceptively. The system must continue producing value despite the malfunction. Understanding how distributed systems fail is the first step to designing ones that endure.

# Lore Introduction

The Academy's High Council of Durability does not ask "will the towers fail?" — they ask "when one fails, what happens to the rest?" The most junior mage knows to prepare for a burned-out rune. The senior mage prepares for a rune that flickers — sending corrupted signals that look valid to the untrained eye. Partial failure is the shadow that haunts distributed systems, and designing for it requires both technical rigour and philosophical acceptance of uncertainty.

# Core Learning

## Concept Introduction

Distributed systems fail in fundamentally different ways from single machines. The key insight: **partial failure** — some parts of the system fail while others continue — is the rule, not the exception.

### Failure taxonomy

| Failure type | Description | Detectability |
|---|---|---|
| **Crash** | Node stops completely | Easy — no response |
| **Omission** | Node fails to send/receive some messages | Moderate — timeouts |
| **Byzantine** | Node behaves arbitrarily (wrong data, malicious) | Hard — responses look valid |
| **Timing** | Node responds too slowly (exceeds SLA) | Easy — latency metrics |
| **Performance** | Degraded throughput | Moderate — monitoring |

## Why It Matters

Distributed systems cannot assume any component is always reliable. Code that ignores this produces systems where one slow service takes down everything connected to it. The 8 Fallacies of Distributed Computing describe the incorrect assumptions that cause this — treating remote calls as if they were local function calls.

## Worked Examples

### The 8 Fallacies of Distributed Computing

```
1. The network is reliable
   Reality: packets drop, connections reset, NIC cards fail

2. Latency is zero
   Reality: inter-DC calls = 50-200ms; inter-region = 100-300ms+

3. Bandwidth is infinite
   Reality: large payloads saturate links; serialise carefully

4. The network is secure
   Reality: assume hostile network; encrypt everything in transit

5. Topology doesn't change
   Reality: auto-scaling, deployments, and failures change IPs constantly

6. There is one administrator
   Reality: microservices have multiple teams; coordination is hard

7. Transport cost is zero
   Reality: egress costs money; cross-region traffic is expensive

8. The network is homogeneous
   Reality: services run on different OS, JVM versions, serialisation formats
```

### Graceful degradation with circuit breaker fallback

```java
@Service
public class ProductPageService {

    @CircuitBreaker(name = "recommendations", fallbackMethod = "popularProducts")
    @TimeLimiter(name = "recommendations")
    public CompletableFuture<List<Product>> getRecommendations(String productId, String userId) {
        return CompletableFuture.supplyAsync(
            () -> recommendationEngine.getPersonalised(productId, userId)
        );
    }

    // Fallback: return cached popular products instead of personalised ones
    private CompletableFuture<List<Product>> popularProducts(String productId,
                                                              String userId,
                                                              Exception ex) {
        log.warn("Recommendation engine unavailable, using popular products: {}", ex.getMessage());
        return CompletableFuture.completedFuture(catalogCache.getTopTen());
    }
}

// Product page: non-critical components degrade gracefully
@GetMapping("/products/{id}")
public ProductPage getProduct(@PathVariable String id) {
    Product product = productService.getById(id); // critical — no fallback

    List<Review> reviews = Try.of(() -> reviewService.getReviews(id))
        .getOrElse(List.of()); // non-critical — empty list fallback

    List<Product> recs = productPageService.getRecommendations(id, currentUser())
        .exceptionally(e -> catalogCache.getTopTen())
        .join();

    return new ProductPage(product, reviews, recs);
}
```

### Chaos engineering — Toxiproxy simulation

```java
// Toxiproxy: proxy layer for injecting network failures in tests
// Simulates 500ms latency to downstream service
ToxiproxyClient client = new ToxiproxyClient("localhost", 8474);
Proxy proxy = client.getProxy("inventory-service");
proxy.toxics().latency("slow", ToxicDirection.DOWNSTREAM, 500);

// Run your test — does the circuit breaker trip? Does the fallback work?
assertThat(inventoryService.getStock("sku-123")).isEqualTo(Stock.UNKNOWN);
proxy.toxics().get("slow").remove(); // clean up

// Chaos Monkey (Spring): randomly disables Spring beans in integration tests
@SpringBootTest
@EnableChaosMonkey
class CheckoutServiceChaosTest { ... }
```

### Designing for partial failure — timeout hierarchy

```java
// Service-level SLA: checkout must complete in 3s
// Therefore: each dependency gets a fraction of that budget

@Service
public class CheckoutService {

    private static final Duration INVENTORY_TIMEOUT = Duration.ofMillis(500);
    private static final Duration PAYMENT_TIMEOUT   = Duration.ofMillis(2000);
    private static final Duration NOTIFY_TIMEOUT    = Duration.ofMillis(200);

    public CheckoutResult checkout(Cart cart) {
        // Critical: must succeed
        InventoryResult inv = inventoryClient.reserve(cart)
            .orTimeout(INVENTORY_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
            .exceptionally(e -> { throw new CheckoutException("Inventory unavailable"); })
            .join();

        // Critical: must succeed
        PaymentResult payment = paymentClient.charge(cart.total(), cart.idempotencyKey())
            .orTimeout(PAYMENT_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
            .join();

        // Non-critical: fire and forget
        notificationClient.sendConfirmation(cart.userId(), payment)
            .orTimeout(NOTIFY_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
            .exceptionally(e -> { log.warn("Notification failed", e); return null; });

        return CheckoutResult.success(payment.transactionId());
    }
}
```

## Common Mistakes

1. **No timeouts on remote calls.** Without timeouts, a slow downstream holds your thread indefinitely, cascading to thread pool exhaustion.

2. **Treating all failures as fatal.** Not all component failures should fail the request. Non-critical dependencies (recommendations, analytics, notifications) should degrade gracefully.

3. **Not testing failure modes.** Systems that have never been run with a dependency missing inevitably have untested code paths in their fallbacks. Chaos engineering forces this.

4. **Byzantine failure blindness.** Most systems only defend against crash failures. Byzantine failures (wrong data, malformed responses) require input validation and response schema validation on all external calls.

5. **Over-broad catch blocks hiding failure.** `catch (Exception e) { return null; }` in a fallback silently degrades in ways that are invisible to monitoring. Log and track degradation explicitly.

## Mental Model

Think of a distributed system like a ship compartmentalised with watertight bulkheads. When one compartment floods (partial failure), the bulkheads (circuit breakers, timeouts, fallbacks) prevent the flooding from spreading. Without bulkheads, one leak sinks the whole ship. Chaos engineering is like intentionally flooding compartments during a drill — to verify the bulkheads work before you are at sea.

## Mini Summary

- Three failure types: crash (stops), omission (silent), Byzantine (corrupt/arbitrary)
- Partial failure is the normal mode — some components fail while others continue
- The 8 Fallacies remind us: networks are not reliable, fast, infinite, or secure
- Graceful degradation: serve reduced functionality rather than total failure
- Chaos engineering deliberately injects failures to discover and fix failure modes

# Guided Practice Quest

Work through the guided steps to practise classifying failure types and designing graceful degradation strategies.

# Solo Practice Quest

Perform a failure mode analysis for a `SearchService` that depends on: an Elasticsearch cluster, a Redis cache for recent queries, and a personalisation service. For each dependency:
1. Classify the most likely failure modes (crash, omission, timing)
2. Define the graceful degradation behaviour
3. Specify timeouts and fallback logic in pseudocode

Then propose one chaos engineering experiment to validate your design.

# Integration

**Connecting to Mathematics — Probability and Reliability Theory and Philosophy — Epistemology Under Uncertainty**

Distributed system failures are studied formally through reliability theory. The reliability of a system with N independent serial dependencies is R = r1 * r2 * ... * rN. For N=5 services each at 99.9% availability, the end-to-end availability is 0.999^5 ≈ 99.5%. This exponential degradation motivates architectural decisions: minimise serial dependencies, introduce parallel fallbacks, and use techniques like bulkhead isolation to cap failure blast radius. The mathematics of failure rates and MTTR (Mean Time to Recovery) directly informs decisions about SLA commitments and the investment in resilience infrastructure.

Philosophically, designing for distributed failure requires what epistemologists call "calibrated uncertainty" — acknowledging that you cannot know with certainty what state a remote system is in. When a service call times out, you do not know whether the service received your request, processed it, or failed before processing. This is the fundamental epistemic challenge of distributed systems: partial knowledge, not complete knowledge. The design response is to make operations safe under uncertainty — idempotent, fencing-tokened, timeout-bounded. Engineers who accept uncertainty as a feature of the domain rather than a defect to be eliminated build better systems: they design for it explicitly, test for it deliberately, and operate for it calmly. Those who assume certainty build fragile systems that collapse the moment the assumption breaks.

# Lore Conclusion

The Council of Durability reviews the incident report. Three towers went dark simultaneously, yet the Academy's services continued — degraded, but alive. The senior mages' designs held: fallback runes activated, non-critical enchantments faded gracefully, and chaos drills had already validated the recovery paths. The junior mage who built the notification system without a fallback caused only a momentary gap in the alumni scrolls — not a full catastrophe. The lesson is inscribed: in a distributed realm, failure is not the exception. Design for it first.

---
