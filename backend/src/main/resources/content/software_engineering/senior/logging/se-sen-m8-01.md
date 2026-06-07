---
id: se-sen-m8-01
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m8
moduleTitle: "Module 8: Observability"
moduleGlyph: "🔭"
moduleSortOrder: 8
topicSlug: logging
topicTitle: "Logging"
topicSortOrder: 1
lesson: logging
title: "Logging"
sortOrder: 1
difficulty: 3
estimatedMinutes: 25
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [design, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Correctly applies log levels (ERROR/WARN/INFO/DEBUG/TRACE)"
    - "Explains what structured logging is and why it's superior to plain text"
    - "Uses SLF4J with parameterised logging (not string concatenation)"
    - "Describes correlation IDs and how they enable request tracing"
    - "Names the cost of excessive logging and how to control it"
  keywords: [error, warn, info, debug, structured, json, correlation, slf4j, level, aggregate]
  modelAnswer: |
    // SLF4J parameterised logging (correct)
    log.info("Order placed: orderId={}, userId={}, amount={}", orderId, userId, amount);
    log.error("Payment failed: orderId={}, reason={}", orderId, reason, exception);

    // Log levels:
    // ERROR: system cannot continue or data may be lost
    // WARN: unexpected but recoverable; may need attention
    // INFO: significant business events (order placed, user registered)
    // DEBUG: developer context for diagnosis (not in production)
    // TRACE: very fine-grained; almost never in production

    // Structured logging (JSON format for log aggregators):
    {
      "level": "INFO",
      "message": "Order placed",
      "orderId": "12345",
      "correlationId": "trace-abc-xyz",
      "timestamp": "2026-01-01T10:00:00Z",
      "service": "order-service"
    }
guidedSteps:
  - id: log-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which log level should you use when the application encounters an unrecoverable
      error that requires immediate attention (e.g., database connection lost)?
    inputConfig:
      options:
        - "DEBUG"
        - "INFO"
        - "WARN"
        - "ERROR"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["ERROR"]
      rejectedFeedback: "ERROR is for unrecoverable situations or significant data loss. WARN is for unexpected but recoverable situations. INFO is for normal significant events. DEBUG is for developer context during diagnosis."
    hint: "When something is broken and needs fixing now, what level communicates that urgency?"
    reflectionPrompt: "ERROR should be rare and actionable — every ERROR log should be something an engineer could potentially respond to. An alert on ERROR rate that fires constantly means either the system is broken or ERROR is being used incorrectly."
  - id: log-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the SLF4J log statement using parameterised logging (not string concatenation):

      ```java
      log.info("User registered: userId=___, email=___", userId, email);
      ```

      What characters are used as placeholders?
    inputConfig:
      placeholder: "placeholder syntax"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["{}", "{ }", "curly braces", "{}"]
      rejectedFeedback: "SLF4J uses `{}` as placeholders. The arguments are only converted to strings if the log level is enabled — so `log.debug(\"data={}\", expensiveToString())` doesn't call `expensiveToString()` if DEBUG is disabled. This is why string concatenation (`+`) in log statements is wasteful."
    hint: "They're curly braces — the arguments fill them in order."
    reflectionPrompt: "Parameterised logging is both safer (avoids NPE from null.toString()) and more performant (lazy evaluation). Always prefer `log.info(\"msg={}\", value)` over `log.info(\"msg=\" + value)`."
  - id: log-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what a correlation ID is, how it gets attached to log statements, and why it makes debugging distributed systems dramatically easier.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [request, trace, id, mdc, propagate, service, filter, header, chain, link]
      rejectedFeedback: "A correlation ID is a unique ID (UUID) generated per request. It's placed in the MDC (Mapped Diagnostic Context) so SLF4J automatically includes it in every log statement made during that request. It's propagated to downstream services in HTTP headers. When debugging, filter logs by correlation ID to see the complete chain of events for one request across all services."
    hint: "What would allow you to find all log lines across 5 services that were caused by one user's request?"
    reflectionPrompt: "MDC (Mapped Diagnostic Context) is how SLF4J carries correlation IDs automatically. `MDC.put(\"correlationId\", id)` in a request filter makes the ID appear in every subsequent log statement without explicit passing."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why is structured logging (JSON format) preferred over plain text logs in production?"
    options:
      - "JSON logs are smaller than text logs"
      - "Structured logs can be searched, filtered, and aggregated by field in log management tools"
      - "JSON is required by most log aggregators"
      - "Plain text logs cannot be stored in the cloud"
    correctIndex: 1
    feedback: "Structured JSON logs allow queries like `level=ERROR AND service=payment AND amount>1000` in tools like Elasticsearch/Kibana, Grafana Loki, or CloudWatch. Plain text requires regex-based parsing which is fragile and slow."
  - type: MULTIPLE_CHOICE
    question: "What is the problem with this log statement? `log.debug(\"Processing: \" + expensiveObject.toDetailedString());`"
    options:
      - "DEBUG is the wrong log level"
      - "`toDetailedString()` is called even if DEBUG logging is disabled, wasting CPU"
      - "String concatenation is not allowed in log statements"
      - "The message is too short"
    correctIndex: 1
    feedback: "String concatenation evaluates `toDetailedString()` regardless of whether DEBUG is enabled. With `log.debug(\"Processing: {}\", expensiveObject)`, the toString() is only called if DEBUG is active. At INFO level in production, the expensive call is never made."

retrieval:
  recall: "What are the five log levels in order of severity? When should you use each?"
  explain: "Explain to a new developer why you should log at INFO for significant business events and ERROR for system failures — not DEBUG for everything."
  mistakeId:
    code: |
      catch (Exception e) {
          log.error("Error occurred");
          throw new ServiceException("Something went wrong");
      }
    answer: "Two problems: (1) The exception object is not logged — you lose the stack trace, exception type, and message. Fix: `log.error(\"Payment processing failed: orderId={}\", orderId, e)`. (2) The error message is too vague — 'Error occurred' tells you nothing. Include context (what was being done) and the exception."
---

# Hook

Something went wrong in production at 2am. There's an alert. You open the logs.

```
ERROR - Error occurred
ERROR - An unexpected error happened
ERROR - Something went wrong
```

No stack traces. No context. No request IDs. You have no idea what failed, why, or for which user.

Good logging is the difference between a 2am debugging nightmare and a 10-minute investigation. It doesn't happen by accident — it requires discipline applied consistently.

> Have you ever had to debug a production issue with insufficient logging? What would you have needed to solve it faster?

# Lore Introduction

The Academy's incident response team has a Chronicle — every significant event in the enchantment infrastructure is recorded, with context, severity, and enough detail to reconstruct what happened. When the north ward failed last winter, the Chronicle showed: *mana surge at 03:42:17, ward dampener failed to engage, protective rune overloaded.*

*"Without the Chronicle,"* Archmage Veylan says, *"we would have spent days investigating what happened in minutes. Logging is not optional. It is the system's memory."*

# Core Learning

## Concept Introduction

**Logging** is the practice of recording events from a running system with sufficient context to understand what happened.

**Log levels (SLF4J / Logback):**

| Level | Use for |
|-------|---------|
| ERROR | System failures, data loss, unrecoverable states — page someone |
| WARN  | Unexpected but recoverable; watch for patterns |
| INFO  | Significant business events: user registered, payment processed |
| DEBUG | Developer context for diagnosis; disabled in production |
| TRACE | Very fine-grained; almost never enabled |

**Basic SLF4J usage:**
```java
private static final Logger log = LoggerFactory.getLogger(OrderService.class);

public Order placeOrder(OrderRequest request) {
    log.info("Placing order: userId={}, items={}", request.getUserId(), request.getItemCount());
    try {
        Order order = processOrder(request);
        log.info("Order placed successfully: orderId={}", order.getId());
        return order;
    } catch (PaymentException e) {
        log.error("Payment failed for order: userId={}, reason={}", request.getUserId(), e.getMessage(), e);
        throw new ServiceException("Payment processing failed", e);
    }
}
```

## Why It Matters

- **Incident investigation** — understand what happened and why without a debugger
- **Performance analysis** — identify slow paths with timing in logs
- **Audit trail** — who did what and when (compliance)
- **Alerting** — trigger alerts on ERROR rate or specific patterns

## Worked Examples

**Structured JSON logging (Logback + logstash-logback-encoder):**
```xml
<!-- logback-spring.xml -->
<appender name="JSON" class="ch.qos.logback.core.ConsoleAppender">
  <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
</appender>
```
Output:
```json
{"@timestamp":"2026-01-01T10:00:00.000Z","level":"INFO",
 "message":"Order placed","orderId":"12345","correlationId":"abc-xyz",
 "logger":"OrderService","thread":"http-nio-80-exec-1"}
```

**Correlation ID with MDC:**
```java
// In a request filter (runs before every request):
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String correlationId = request.getHeader("X-Correlation-ID");
        if (correlationId == null) correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);
        response.setHeader("X-Correlation-ID", correlationId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
```

## Common Mistakes

- **Logging sensitive data** — passwords, tokens, PII, card numbers. Log IDs, not values.
- **Catching and swallowing exceptions** — `catch (Exception e) { log.error("Error"); }` loses the stack trace.
- **String concatenation in log statements** — use `{}` placeholders for lazy evaluation.
- **Logging too much at INFO** — noisy logs obscure signals; tune levels in production.
- **Not including context** — "Order failed" is useless; "Order 12345 failed: payment declined for user 789" is actionable.

## Mental Model

Logs are the **flight data recorder** for your service. You don't watch it in normal operation — but when something goes wrong, it's the only record of what happened. A good recorder captures enough detail to reconstruct the sequence of events. A poor one leaves investigators guessing.

## Mini Summary

- ✔ Five levels: ERROR (broken), WARN (degraded), INFO (business events), DEBUG (dev context), TRACE (ultra-fine)
- ✔ SLF4J parameterised logging: `log.info("msg={}", value)` — never `"msg=" + value`
- ✔ Structured JSON logging enables field-level search and aggregation in production
- ✔ Correlation IDs via MDC link all log statements from one request across services
- ✔ Never log sensitive data (passwords, tokens, PII)

# Guided Practice Quest

**The Chronicle**

Review three log implementations. Identify what's wrong with each and rewrite them following structured logging best practices.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Audit the logging in this `UserAuthenticationService`:

```java
public class UserAuthenticationService {
    public AuthResult authenticate(String username, String password) {
        System.out.println("Authenticating " + username + " with password " + password);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("User not found");
            return AuthResult.failure("User not found");
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            System.out.println("Wrong password for " + username);
            return AuthResult.failure("Wrong password");
        }
        System.out.println("Authentication successful");
        return AuthResult.success(generateToken(user));
    }
}
```

Produce a fully rewritten version with:
1. SLF4J logging at appropriate levels (no System.out)
2. No sensitive data in logs
3. Parameterised logging throughout
4. Useful context in each log statement
5. Explain what level you chose for each statement and why

# Integration

**Connecting to Psychology — Attention and Noise**

Nobel laureate Daniel Kahneman distinguishes System 1 (fast, automatic) and System 2 (slow, deliberate) thinking. Debugging under pressure forces System 2 — deliberate, analytical reasoning. But noise (irrelevant information) is System 2's worst enemy: it consumes limited working memory, crowds out relevant signals, and causes premature closure on wrong hypotheses.

Noisy logs are cognitive pollution. When every insignificant event is logged at INFO, the signal-to-noise ratio drops until engineers develop "log blindness" — they stop reading logs carefully because experience teaches them that most log entries are irrelevant. This blindness is precisely when critical ERROR entries get missed.

The design implication: logging discipline is not just technical best practice — it's cognitive ergonomics. Carefully curated logs respect the attention budget of the engineer who must read them at 2am. Every log statement is a claim on that engineer's attention. Make every claim count.

How would you audit an existing codebase's logging discipline and decide what to change?

# Lore Conclusion

The Chronicle is complete. Every significant event recorded. Noise eliminated. The next incident investigation takes ten minutes instead of four hours.

*"The Chronicle serves no one unless it can be read,"* Archmage Veylan says. *"A log that contains everything contains nothing useful. Discipline the recording, and the memory serves you."*

Logs are for humans. Write them that way.
---
