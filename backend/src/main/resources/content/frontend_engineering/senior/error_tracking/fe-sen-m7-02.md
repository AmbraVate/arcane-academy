---
id: fe-sen-m7-02
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m7
moduleTitle: "Module 7: Frontend Observability"
moduleGlyph: "📊"
moduleSortOrder: 7
topicSlug: error_tracking
topicTitle: "Error Tracking"
topicSortOrder: 2
lesson: error_tracking
title: "Error Tracking"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains what Sentry does and the key data it captures
    - Describes the role of source maps in error tracking
    - Explains how error grouping works and what affects grouping quality
    - Describes how to add user context and breadcrumbs to error reports
    - Synthesises a meaningful error alert threshold strategy
  keywords: [Sentry, source map, stack trace, error grouping, breadcrumb, context, release, alert, threshold, error boundary, unhandled, sampling]
  modelAnswer: |
    Sentry captures unhandled exceptions and rejected Promises in production, providing: stack traces with source-mapped line numbers, breadcrumbs (actions leading up to the error), user context (anonymised), release version, browser/OS info, and replay (optionally). Each error report is grouped by stack trace similarity into an "Issue" — the same bug from many users appears as one issue with a count.

    Source maps are critical: production JavaScript is minified (unreadable line numbers). Source maps translate minified locations back to original TypeScript/JSX source. Without them, stack traces show `bundle.min.js:1:45823` — useless. With them: `src/components/Checkout/PaymentForm.tsx:142` — actionable.

    Error grouping quality depends on consistent stack traces. Dynamic error messages ('Failed for user ID 12345') create many separate issues — one per user ID. Use the error type and message pattern without dynamic values: 'Failed for user ID {id}' → groups into one issue.

    User context: Sentry.setUser({ id: user.id }) (not email/name — GDPR). Breadcrumbs: auto-captured (console.log, network requests, navigation) plus manual: Sentry.addBreadcrumb({ message: 'Cart item added', data: { productId } }).

    Alert thresholds: don't alert on every error (noise). Alert when: error rate exceeds baseline by 3×, new error appears in a release, or an error affects >1% of sessions.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A production error shows stack trace: `bundle.Abc1.js:1:14523`. You cannot identify the source. What is missing?"
    options:
      - "Sentry needs to be updated to the latest version"
      - "Source maps are not uploaded to Sentry for this release"
      - "The error occurred in a third-party library"
      - "The browser blocked source map loading"
    correctIndex: 1
    feedback: "Minified JavaScript has unreadable line numbers. Source maps translate `bundle.Abc1.js:1:14523` → `src/components/PaymentForm.tsx:142`. Without source maps uploaded to Sentry (as part of the build/deploy), stack traces are useless. Source maps should be uploaded with every release: sentry-cli releases files upload-sourcemaps --url-prefix '~/' ./dist."
  - type: SHORT_TEXT
    prompt: "Your Sentry dashboard shows 847 separate issues for what appears to be the same error. Investigation reveals each has a different user ID in the error message: 'Failed to load profile for user 12345', 'Failed to load profile for user 67890'. How would you fix the grouping?"
    hint: "What makes these appear as different issues? What would make them group together?"
  - type: FILL_BLANK
    prompt: "Source maps translate ___ JavaScript locations to ___ source file locations. They must be uploaded to Sentry with each ___."
    answer: "minified; original; release/deployment"
    hint: "Source maps bridge the gap between what runs in production and what developers wrote."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Sentry 'breadcrumbs' capture what information?"
    options:
      - "The entire user session recording"
      - "A trail of actions and events leading up to the error (navigation, clicks, console messages, network requests)"
      - "The user's authentication credentials"
      - "A list of all JavaScript errors on the page regardless of Sentry"
    correctIndex: 1
    feedback: "Breadcrumbs are a chronological trail of events leading to the error — the digital equivalent of 'what was the user doing when it broke'. Auto-captured: navigation, console messages, network requests, DOM interactions. Custom breadcrumbs add domain-specific context: 'User added item to cart', 'Checkout step 2 reached'. They're invaluable for reproducing bugs."
  - type: MULTIPLE_CHOICE
    question: "Your app has 0.1% error rate normally. After a deployment, it spikes to 2%. When should you be alerted?"
    options:
      - "Only when error rate reaches 10%"
      - "When error rate exceeds the baseline by a significant factor (e.g. 3× or more)"
      - "After 100 users have been affected"
      - "Never — monitoring should be manual"
    correctIndex: 1
    feedback: "Threshold-based alerts: trigger when current error rate significantly exceeds the baseline. 2% vs 0.1% baseline is a 20× increase — a clear signal of regression. A static threshold (10%) would miss this spike. Relative thresholds (>3× baseline) catch deployment regressions while ignoring normal variation. Alert early enough to roll back before more users are affected."
retrieval:
  recall: "What is an 'Issue' in Sentry and how does error grouping work?"
  explain: "Why are source maps critical for meaningful error tracking in production?"
  mistakeId:
    code: |
      // Sentry user context setup
      Sentry.setUser({
        email: currentUser.email,
        name: currentUser.name,
        phone: currentUser.phone,
      });
    answer: "Email, name, and phone are PII — setting them in Sentry creates a GDPR violation. Sentry stores error data in its cloud (potentially outside the EU by default). PII in error reports can lead to: regulatory fines, breach notification obligations if Sentry is compromised, and violations of data minimisation principles. Fix: Sentry.setUser({ id: currentUser.anonymisedId }). If email is needed for contacting affected users, store a mapping server-side. Never put PII in error reports."
---

# Hook

Production is broken. You have Sentry. You check the dashboard: 500 errors/minute. Stack trace: `bundle.min.js:1:45823`. You have no source maps. The error is unreadable.

You have error tracking, but not error intelligence. The difference is source maps, context, and grouping.

# Lore Introduction

*"An incident report that reads 'something broke somewhere' is useless,"* the Incident Commander explains. *"The useful report names the location, the sequence of events leading to it, and how many were affected."*

She pulls up the Sentry dashboard. *"This is your incident report system. But it only works if you give it what it needs: source maps for location, breadcrumbs for sequence, context for impact."*

# Core Learning

## Concept Introduction

**Sentry** captures unhandled errors in production and provides:
- **Stack trace** (with source maps: readable file/line references)
- **Breadcrumbs** (chronological trail of events before the error)
- **User context** (anonymised user ID, browser, OS)
- **Issue grouping** (same bug from 500 users = one issue with count: 500)
- **Release tracking** (which release introduced the error)
- **Session Replay** (optional: see what the user was doing)

**Setup:**
```ts
// main.tsx
import * as Sentry from '@sentry/react';

Sentry.init({
  dsn: import.meta.env.VITE_SENTRY_DSN,
  integrations: [Sentry.browserTracingIntegration()],
  tracesSampleRate: 0.1, // 10% sampling for performance
  release: import.meta.env.VITE_RELEASE_VERSION,
  environment: import.meta.env.MODE,
});
```

**User context (no PII):**
```ts
Sentry.setUser({ id: user.anonymisedId }); // NOT email or name
```

**Custom breadcrumbs:**
```ts
Sentry.addBreadcrumb({
  message: 'Payment step reached',
  level: 'info',
  data: { step: 3, amount: order.totalCents }, // no card details
});
```

**React Error Boundary with Sentry:**
```tsx
const SentryErrorBoundary = Sentry.withErrorBoundary(App, {
  fallback: <ErrorPage />,
  showDialog: false,
});
```

**Source map upload (Vite):**
```bash
npm install --save-dev @sentry/vite-plugin
# vite.config.ts: add sentryVitePlugin({ org, project, authToken })
```

**Alert strategy:**
- New error in latest release: immediate alert
- Error rate > 3× baseline: alert
- Error affecting > 1% of sessions: alert
- Known noise/third-party errors: mute

## Common Mistakes

- **No source maps.** Stack traces in minified code are unreadable.
- **PII in Sentry.setUser().** Email, name, phone in error reports — GDPR violation.
- **Alerting on every error.** Alert fatigue. Use thresholds and grouping.
- **Not configuring sampling.** Sending every event to Sentry for high-traffic apps is expensive. Use `tracesSampleRate` and `sampleRate`.
- **Dynamic values in error messages.** 'Failed for user 12345' = 10,000 separate issues. Use error type and type message pattern.

## Mini Summary

- ✔ Sentry captures unhandled exceptions with stack trace, breadcrumbs, and user context
- ✔ Source maps are essential — upload with every release for readable stack traces
- ✔ Error grouping by stack trace similarity — avoid dynamic values in error messages
- ✔ User context: anonymised IDs only — never PII in error reports
- ✔ Alert on significant changes vs baseline, not on every error

# Guided Practice Quest

Work through the guided steps to understand source maps and error grouping quality.

# Solo Practice Quest

Design the complete error tracking setup for a production React application. Cover: Sentry initialisation, source map upload, user context (with GDPR considerations), custom breadcrumbs for key user flows, Error Boundary integration, and alert thresholds. Justify each choice.

# Integration

**Mathematics — Sampling Theory and Observability**

Full fidelity error tracking (capturing 100% of errors) is expensive for high-traffic applications. Sampling theory provides the solution: capture a representative sample (1%, 10%) that preserves statistical properties of the full population. This works because error rates are meaningful as rates, not absolute counts — a 2% error rate is equally bad whether you see 1,000 errors (10% sample) or 10,000 errors (100% sample). The sample rate provides unbiased estimates of the true rate. Exception: always capture 100% of errors that are new or have high severity — sampling should never miss novel issues. This is stratified sampling: ensure the rare but important category (new error types) is always captured; apply sampling only to the common category (known error types).

# Lore Conclusion

*"The incident report is complete,"* the Commander says. *"Stack trace: readable. Breadcrumbs: clear sequence of events. Context: 847 sessions affected. Source: PaymentForm.tsx:142 — a null check missing. The fix is deployed. The alert threshold catches any recurrence."*

---
