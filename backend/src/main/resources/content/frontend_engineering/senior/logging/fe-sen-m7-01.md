---
id: fe-sen-m7-01
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m7
moduleTitle: "Module 7: Frontend Observability"
moduleGlyph: "📊"
moduleSortOrder: 7
topicSlug: logging
topicTitle: "Logging"
topicSortOrder: 1
lesson: frontend_logging
title: "Frontend Logging"
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
    - Explains the difference between console.log and structured logging
    - Describes log levels and when to use each
    - Explains what should and should not be included in frontend logs
    - Describes how logs are collected and sent to aggregation services
    - Synthesises a logging strategy appropriate for a production React application
  keywords: [structured, log level, debug, info, warn, error, PII, aggregation, Datadog, Sentry, console, transport, context]
  modelAnswer: |
    Frontend logging in production means structured, levelled logs sent to an aggregation service — not console.log (which is ephemeral, browser-only, and visible to users). Structured logs use JSON format with consistent fields: timestamp, level, message, context (userId, sessionId, page), and any relevant metadata.

    Log levels: DEBUG (development only — not sent to production), INFO (significant events: user logged in, feature used), WARN (unexpected but recoverable: API returned unexpected status), ERROR (failures requiring investigation: unhandled exception, API 500).

    What to include: event type, user context (anonymised userId — never email/name), page/route, relevant IDs (entityId, sessionId), error details. What NOT to include: PII (names, emails, addresses), passwords, payment data, tokens. GDPR and data minimisation principles require not logging data you don't need.

    Log transport: a client-side logger (like Pino or a custom logger) buffers logs and sends them in batches to a logging backend. Services: Datadog Logs, AWS CloudWatch, Loki. The logger should queue logs and send them on network idle or in batches — not on every event.

    Production logging strategy: suppress DEBUG in production, send INFO/WARN/ERROR to aggregation, ensure logs have enough context to reproduce issues without storing PII.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A developer logs `console.log('User logged in:', user.email)` in production code. What problems does this create?"
    options:
      - "console.log is too slow for production"
      - "The log is ephemeral (visible only in DevTools), exposes PII, and clutters user-visible output"
      - "console.log only works in Chrome"
      - "It increases bundle size significantly"
    correctIndex: 1
    feedback: "console.log: (1) only visible in the user's DevTools — not sent to your logging system, (2) exposing user.email is a PII violation (GDPR), (3) visible to users who open DevTools (unexpected disclosure). Production logs should use a structured logger that sends to an aggregation service, never logs PII, and can be filtered by level."
  - type: SHORT_TEXT
    prompt: "Design a structured log event for 'user adds item to cart'. What fields would you include, and what would you explicitly exclude?"
    hint: "Think about what helps debugging vs what is sensitive data."
  - type: FILL_BLANK
    prompt: "Structured logs use ___ format for consistent, queryable fields. Log levels from most verbose to most severe: ___, ___, ___, ___."
    answer: "JSON; DEBUG, INFO, WARN, ERROR"
    hint: "JSON enables querying by field. Four standard levels from development-only to critical."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A user reports an issue in production. The only logs available are console.logs that were captured in the browser — but only if the user happened to open DevTools. What is the primary problem?"
    options:
      - "console.log is too slow for capturing user issues"
      - "Production logs must be sent to a centralised service in real-time — you cannot rely on user-side DevTools"
      - "The user's browser may not support console.log"
      - "DevTools logs are encrypted and inaccessible"
    correctIndex: 1
    feedback: "console.log in production is useless for debugging user-reported issues. The log exists only in the user's browser, disappears on reload, and requires the user to open DevTools. Production logging requires sending structured logs to a centralised service (Datadog, CloudWatch) where engineers can query them after the fact, even for users who have already left the page."
  - type: MULTIPLE_CHOICE
    question: "Which log level is appropriate for 'API request failed with 503 — retry succeeded on attempt 2'?"
    options:
      - "DEBUG — a minor technical detail"
      - "WARN — unexpected but recovered; worth tracking"
      - "ERROR — any API failure is an error"
      - "INFO — a normal system event"
    correctIndex: 1
    feedback: "WARN is for unexpected but non-fatal events that recovered. A 503 retry that succeeded didn't break the user experience, but it's worth knowing about — it indicates server instability. ERROR would be if the retry failed. INFO would be for expected events. WARN signals 'something was wrong but we handled it — monitor this for frequency.'"
retrieval:
  recall: "What is the difference between a console.log and a structured log sent to an aggregation service?"
  explain: "Why is logging PII (personally identifiable information) like email addresses a problem beyond privacy concerns?"
  mistakeId:
    code: |
      // Production logging implementation
      function logEvent(event) {
        console.log(JSON.stringify({
          timestamp: new Date().toISOString(),
          user: {
            email: currentUser.email,  // PII
            name: currentUser.name,    // PII
          },
          event,
        }));
      }
    answer: "Three problems: (1) console.log is browser-only — not sent to your logging system. (2) Logging email and name violates GDPR data minimisation — log only what's necessary for debugging (anonymised userId, not name/email). (3) These logs are visible in DevTools to anyone who opens them. Fix: use a logging library with a transport that sends to your aggregation service; replace PII with anonymised IDs; add log level filtering."
---

# Hook

A user reports their cart emptied unexpectedly. You check your logs: `console.log('Cart updated')`. No timestamp, no user context, no cart contents, no error details.

You can't reproduce it. You can't investigate it. You don't even know if it happened once or a thousand times.

Logging is not `console.log`. Logging is structured, levelled, contextual information sent where engineers can find it.

# Lore Introduction

*"A scribe who records 'something happened at the forge' provides no value to the Guild Master investigating a fire,"* the Guild Records Keeper explains. *"The useful record names: who was present, what materials were in use, what sounds were heard, what was damaged. Specific, structured, contextual."*

She points to the logging schema. *"This is how production systems record events. Not 'something happened' — but exactly what, when, where, and who."*

# Core Learning

## Concept Introduction

**Frontend logging architecture:**

```
Browser Event
    ↓
Logger (level filter, context enrichment)
    ↓
Transport (batch, queue)
    ↓
Aggregation Service (Datadog Logs, CloudWatch, Loki)
    ↓
Query, Alert, Dashboard
```

**Structured log format:**
```json
{
  "timestamp": "2024-01-15T14:32:01.234Z",
  "level": "warn",
  "message": "API request failed — retrying",
  "context": {
    "userId": "u_8a92b4c1",     // anonymised
    "sessionId": "s_f7e3d2a9",
    "page": "/checkout",
    "correlationId": "req_4a2b1c"
  },
  "details": {
    "endpoint": "/api/orders",
    "statusCode": 503,
    "attempt": 1
  }
}
```

**Log levels:**
| Level | When | Production? |
|---|---|---|
| `DEBUG` | Detailed dev traces | ❌ No |
| `INFO` | Significant events (login, feature used) | ✅ Yes |
| `WARN` | Unexpected but recovered | ✅ Yes |
| `ERROR` | Failures requiring investigation | ✅ Yes |

**Logger implementation:**
```ts
// logger.ts
const logger = {
  debug: (msg: string, details?: object) => {
    if (process.env.NODE_ENV === 'development') console.debug(msg, details);
  },
  info: (msg: string, details?: object) => send({ level: 'info', msg, ...details }),
  warn: (msg: string, details?: object) => send({ level: 'warn', msg, ...details }),
  error: (msg: string, error?: Error, details?: object) => {
    send({ level: 'error', msg, stack: error?.stack, ...details });
  },
};

function send(log: LogEntry) {
  // Queue and batch to aggregation service
  logQueue.push({ ...log, ...globalContext, timestamp: Date.now() });
}
```

## Why It Matters

When a user says "it didn't work", frontend logging is the difference between asking them to describe their browser from memory and replaying what actually happened:

- The client is the least observable tier of the stack — server logs end at the API boundary, but the bug lives in a specific browser, with a specific extension, on hotel Wi-Fi
- Structured logs (event, context, severity — not `console.log("here 2")`) are what make client telemetry queryable at scale instead of a text swamp
- Production logging is a discipline of restraint: log levels, sampling, and batching keep the signal affordable, because shipping every debug line from a million browsers is a bandwidth bill and a privacy incident
- The privacy boundary is non-negotiable — tokens, passwords, and personal data must never enter the log pipeline, and redaction has to be designed in, not bolted on after the first leak

Support escalations, intermittent bugs, and "works on my machine" all dissolve faster when the application has been quietly writing its own diary. Senior engineers design that diary deliberately.

## Common Mistakes

- **Using console.log in production.** Ephemeral, browser-only, visible to users.
- **Logging PII.** Email, name, phone, payment data — never in logs. Use anonymised IDs.
- **Logging on every event.** Log significant events (page load, feature use, errors) — not every click.
- **No log context.** Logs without userId, sessionId, and page make debugging impossible.
- **Sending logs synchronously.** Log sending should be non-blocking and batched.

## Mental Model

Frontend logging is a ship's logbook, not a teenager's diary. A diary records whatever felt interesting in the moment ("got here!", "x is undefined??") and is unreadable to anyone else a week later. A logbook is *institutional*: standardised entries (structured fields — event name, context, severity), written at defined moments (navigation, API failure, feature use), at a detail level set by conditions (log levels — routine entries in calm seas, everything during a storm), and kept knowing others will read it — the admiralty (your analytics pipeline), insurers (compliance), and future captains (engineers debugging long after you). Two logbook rules carry the whole discipline: never record passengers' private letters (PII and secrets stay out), and don't log so obsessively that writing the book becomes the voyage (sampling and batching keep overhead invisible).

## Mini Summary

- ✔ Production logs must be structured (JSON), levelled, and sent to an aggregation service
- ✔ Log levels: DEBUG (dev only), INFO, WARN, ERROR
- ✔ Include context: userId (anonymised), sessionId, page, correlationId
- ✔ Never log PII — anonymise with internal IDs
- ✔ console.log is for development; a structured logger is for production

# Guided Practice Quest

Work through the guided steps to design structured log events and understand PII constraints.

# Solo Practice Quest

Design a logging strategy for a healthcare application (extra sensitivity around PII). Define: what events to log at each level, what context fields to include, what is explicitly prohibited from logs, how to anonymise user identifiers, and what aggregation service approach you'd use.

# Integration

**Mathematics — Signal-to-Noise Ratio in Logging**

Log design is fundamentally a signal-to-noise optimisation problem. Too few logs and you lack signal to diagnose issues. Too many logs and the signal drowns in noise — engineers stop looking. The log level system is the primary filter: DEBUG (noise in production), INFO (moderate signal), WARN (high signal), ERROR (critical signal). Structured format enables a secondary filter: query by field. The result: engineers can filter to `level=error AND page=/checkout AND date=today` and find exactly the signal they need. This is the same mathematical principle as bandwidth optimisation in communications: maximise meaningful information per unit of cost (storage, transport, engineer attention time). A well-designed logging system is a low-noise, high-signal channel from production to engineering.

# Lore Conclusion

*"The records are structured, levelled, and contextual,"* the Records Keeper says. *"When the Guild Master investigates, they query: 'All warnings from the forge wing, last 24 hours.' They find what they need. The logs are not noise — they are intelligence."*

---
