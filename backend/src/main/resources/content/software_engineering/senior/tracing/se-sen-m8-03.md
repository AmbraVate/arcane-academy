---
id: se-sen-m8-03
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m8
moduleTitle: "Module 8: Observability"
moduleGlyph: "🔭"
moduleSortOrder: 8
topicSlug: tracing
topicTitle: "Tracing"
topicSortOrder: 3
lesson: tracing
title: "Tracing"
sortOrder: 3
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [metrics]
integrationDomains: [design, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains the trace/span model correctly"
    - "Describes how trace context is propagated between services"
    - "Names OpenTelemetry as the standard and Jaeger/Zipkin as backends"
    - "Explains sampling and why 100% tracing is impractical at high volume"
    - "Describes how tracing helps diagnose latency in microservices"
  keywords: [trace, span, context, propagate, opentelemetry, jaeger, sample, parent, child, latency]
  modelAnswer: |
    Trace: represents a complete request journey across all services.
    Span: a single unit of work within a trace (one service call, one DB query).

    Trace model:
    Trace: abc-xyz
    ├── Span 1: API Gateway (10ms)
    │   └── Span 2: OrderService (150ms)
    │       ├── Span 3: Database query (120ms)  ← bottleneck
    │       └── Span 4: PaymentService (25ms)

    Propagation: each service passes trace context in HTTP headers.
    Standard headers: traceparent (W3C Trace Context)
    Library: OpenTelemetry auto-instrumentation handles this.

    Sampling: 100% tracing at 10k req/s generates too much data.
    Head sampling: decide at trace start whether to sample.
    Tail sampling: decide after completion (capture slow/errored traces).

    Use case: p99 latency spike traced to Span 3 (DB query) —
    shows the query causing the degradation without log archaeology.
guidedSteps:
  - id: trace-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user request passes through API Gateway → OrderService → PaymentService.
      Each generates spans. What is the relationship between these spans?
    inputConfig:
      options:
        - "Each span is independent with no relationship"
        - "They form a tree: API Gateway span is the root; OrderService and PaymentService spans are children"
        - "They form a queue processed in order"
        - "Only the first span matters for tracing"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["They form a tree: API Gateway span is the root; OrderService and PaymentService spans are children"]
      rejectedFeedback: "Spans form a tree (DAG) within a trace. The first service creates the root span. Each subsequent service creates a child span with a reference to its parent. The complete tree shows the call hierarchy, timing, and which service contributed what latency."
    hint: "Think about the parent-child relationship between callers and callees."
    reflectionPrompt: "The parent-child span model gives you a causal timeline. You can see: 'the total request took 200ms; 150ms was in OrderService; of that, 120ms was a database query.' The bottleneck is instantly visible."
  - id: trace-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Trace context is propagated between services in HTTP headers. The W3C standard
      header for this is called ___.
    inputConfig:
      placeholder: "HTTP header name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["traceparent", "W3C traceparent", "traceparent header"]
      rejectedFeedback: "The W3C Trace Context standard defines the `traceparent` header (format: `00-traceId-parentSpanId-flags`). OpenTelemetry supports this and also the older B3 format (used by Zipkin). The header carries the trace ID and parent span ID across service boundaries."
    hint: "It's the W3C standard header name for distributed trace context."
    reflectionPrompt: "OpenTelemetry auto-instrumentation automatically injects and extracts `traceparent` from HTTP requests and responses — you don't have to do it manually. For async messaging (Kafka), context is propagated in message headers."
  - id: trace-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Why is sampling 100% of traces impractical at high request volumes? Describe the two main sampling strategies and when each is appropriate.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [storage, cost, volume, head, tail, sample, rate, slow, error, representative, decision]
      rejectedFeedback: "100% tracing at 10k req/s generates massive data volume — storage cost and ingest overhead are prohibitive. Head sampling: decide at request start (fast, cheap, but may miss important traces). Tail sampling: decide after the trace completes (can prioritise slow/errored traces, but requires buffering all spans until the trace finishes)."
    hint: "How much storage would 100% of traces require at 10,000 requests per second? What's the alternative?"
    reflectionPrompt: "Tail-based sampling is more intelligent: 'capture all traces with errors, all traces over 1s, and 1% of the rest.' This ensures the interesting traces are never dropped. Tools like Grafana Tempo and the OpenTelemetry Collector support tail sampling."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is a 'span' in distributed tracing?"
    options:
      - "The total duration of a request"
      - "A single unit of work within a trace, representing one service call or operation"
      - "An error encountered during a request"
      - "The HTTP header used to propagate trace context"
    correctIndex: 1
    feedback: "A span represents one unit of work: a service processing a request, a database query, an external API call. Spans have a start time, duration, status, and attributes. Multiple spans form a tree within a trace."
  - type: MULTIPLE_CHOICE
    question: "If a request takes 500ms and you have distributed tracing, what does the waterfall view show you that logs and metrics cannot?"
    options:
      - "The total request count"
      - "The exact breakdown of where each millisecond was spent across all services and operations"
      - "The user who made the request"
      - "The response body"
    correctIndex: 1
    feedback: "The trace waterfall shows the hierarchical timeline: Service A took 50ms, within which Database query took 30ms, Service B took 440ms, within which three downstream calls ran. You can see parallelism, sequential dependencies, and exactly which operation was the bottleneck."

retrieval:
  recall: "What is a trace? What is a span? How do they relate to each other?"
  explain: "Explain to a junior engineer why you'd use distributed tracing to investigate a slow API endpoint rather than just reading logs."
  mistakeId:
    code: |
      // Service A calls Service B:
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<String> response = restTemplate.getForEntity(
          "http://service-b/api/data", String.class);
      // No trace context propagated — Service B starts a new unrelated trace
    answer: "Without trace context propagation, Service B creates a disconnected trace. You cannot link the Service A trace to the Service B trace. Fix: use OpenTelemetry auto-instrumentation which injects `traceparent` automatically into `RestTemplate` and `WebClient` calls. Or configure a `TraceContext` interceptor for RestTemplate."
---

# Hook

Your API's p99 latency is 2 seconds. The error logs are clean. The metrics show the degradation started after last Tuesday's deployment. But which service is slow? Which database query? Which downstream dependency?

Distributed tracing answers these questions by following a request across every service it touches, recording timing at each step, and presenting it as a single timeline.

From a 2-second mystery to "specifically the `getUserPreferences` database query in `ProfileService` is taking 1.8 seconds" — in minutes.

> Have you ever had to investigate a latency issue across multiple services using only logs and metrics? How long did it take?

# Lore Introduction

The Academy's tracking crystals follow enchantment components across every chamber. When a spell takes too long to complete, the tracker shows: binding rune (10ms) → mana channel (50ms) → convergence ward (1,840ms) → final inscription (5ms). The ward is the bottleneck.

*"Without the tracker,"* Archmage Veylan says, *"we would guess. With it, we know. Distributed systems require distributed sight."*

# Core Learning

## Concept Introduction

**Distributed tracing** follows a request across multiple services, recording a timeline of every operation.

**Trace** = one complete request journey (has a unique trace ID)
**Span** = one unit of work within the trace (has a span ID and parent span ID)

```
Trace ID: abc-xyz-123
│
├── Span: api-gateway [0ms → 5ms]
│   └── Span: order-service [5ms → 200ms]
│       ├── Span: db.query SELECT orders [5ms → 170ms]  ← bottleneck
│       └── Span: payment-service [170ms → 195ms]
│           └── Span: stripe-api [170ms → 193ms]
```

**OpenTelemetry** is the industry standard for instrumentation.

## Why It Matters

Tracing enables:
- **Latency attribution** — see exactly where time was spent across the full call chain
- **Bottleneck identification** — the slowest span is immediately visible
- **Root cause correlation** — connect a deployment to a specific latency regression
- **Cross-service debugging** — one view of all services involved in a request
- **Error attribution** — which service first threw the exception?

## Worked Examples

**Spring Boot + OpenTelemetry (Java agent approach):**
```bash
# Run with the OTel Java agent — auto-instruments Spring, JDBC, HTTP clients
java -javaagent:opentelemetry-javaagent.jar \
     -Dotel.service.name=order-service \
     -Dotel.exporter.otlp.endpoint=http://collector:4317 \
     -jar order-service.jar
```

**Custom spans for business operations:**
```java
@Service
public class OrderService {
    private final Tracer tracer;

    public Order placeOrder(OrderRequest request) {
        Span span = tracer.spanBuilder("order.place")
            .setAttribute("order.userId", request.getUserId())
            .startSpan();
        try (Scope scope = span.makeCurrent()) {
            Order order = processOrder(request);
            span.setAttribute("order.id", order.getId());
            span.setStatus(StatusCode.OK);
            return order;
        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, e.getMessage());
            throw e;
        } finally {
            span.end();
        }
    }
}
```

**Sampling strategy (OTel Collector):**
```yaml
processors:
  tail_sampling:
    decision_wait: 10s
    policies:
      - name: errors
        type: status_code
        status_code: {status_codes: [ERROR]}
      - name: slow-traces
        type: latency
        latency: {threshold_ms: 1000}
      - name: sample-otherwise
        type: probabilistic
        probabilistic: {sampling_percentage: 1}
```

## Common Mistakes

- **Not propagating context to async operations** — threads, Kafka consumers, and async methods lose trace context unless explicitly propagated.
- **100% sampling in production** — unsustainable at high volume; configure sampling.
- **Too many custom spans** — instrumentation overhead adds latency; instrument meaningful operations, not every method.
- **Not correlating traces with logs** — include trace ID in MDC so logs and traces link up.
- **Ignoring trace context in new threads** — `CompletableFuture`, `@Async`, and `ExecutorService` need context propagation wrappers.

## Mental Model

Distributed tracing is an **X-ray of a request**. You can't see the internals of a running system by looking at it from outside (metrics). Tracing is the X-ray: it reveals the internal structure — which bones (services) are involved, which are healthy, which are fractured.

## Mini Summary

- ✔ Trace = complete request journey; Span = one unit of work; linked by parent-child IDs
- ✔ Context propagated in HTTP headers (`traceparent`) — OpenTelemetry handles this automatically
- ✔ Waterfall view shows exact latency breakdown across all services
- ✔ Use sampling (tail-based for slow/error traces) to manage storage at high volume
- ✔ Correlate trace IDs in logs for combined log+trace investigation

# Guided Practice Quest

**The Tracking Crystal**

A request is taking 2 seconds. Analyse the trace waterfall to identify the bottleneck and propose a fix.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Design the tracing strategy for a microservices platform with these services:
- `ApiGateway` → `UserService` → `AuthService` (sync)
- `OrderService` → `InventoryService` (sync) + `NotificationService` (async via Kafka)
- `OrderService` → `PaymentService` (sync)

Address:
1. How does trace context flow through each call path (sync and async)?
2. For the Kafka-based notification (async), how do you ensure the trace connects OrderService to NotificationService?
3. What sampling strategy would you configure if the system handles 5,000 requests/second?
4. What custom span attributes would you add to `OrderService` spans for business context?
5. If you can only choose one observability tool (logs, metrics, or tracing) for debugging latency issues, which would you choose and why?

# Integration

**Connecting to Psychology — Cognitive Mapping and Mental Models**

Psychologist Edward Tolman's research on rats in mazes (1948) showed that subjects develop "cognitive maps" — internal representations of spatial relationships — even without explicit training on paths. Experts in a domain have rich cognitive maps; novices have sparse ones.

Distributed tracing externalises the cognitive map of a complex distributed system. In a monolith, a developer's mental model of a request is accurate because all code is in one place. In microservices, even an experienced developer cannot hold the full request flow in working memory. The trace visualisation provides an external cognitive map — the actual request flow, not the assumed one.

This matters because debugging relies on mental models. Incorrect mental models lead to incorrect hypotheses. Hours of debugging in the wrong service. Distributed tracing corrects the mental model in seconds: "I assumed UserService was the bottleneck; the trace shows it's AuthService."

The design implication: observability tools are not just for production incidents. They're cognitive tools that extend and correct engineers' understanding of how their systems actually behave. Regularly browsing traces during normal operation builds the accurate mental model that makes incident response faster.

How might you incorporate distributed tracing into normal development workflows, not just incident response?

# Lore Conclusion

The tracker shows the complete picture. Every component's time is measured. The bottleneck is found, fixed, verified.

*"Distributed sight costs something,"* Archmage Veylan says. *"Instrumentation, storage, sampling configuration. But distributed blindness costs more — in incidents that take hours to diagnose when minutes were possible."*

Instrument your traces. Understand your system as it actually behaves, not as you imagine it does.
---
