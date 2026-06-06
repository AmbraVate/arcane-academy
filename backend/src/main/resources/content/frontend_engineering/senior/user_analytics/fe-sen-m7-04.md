---
id: fe-sen-m7-04
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m7
moduleTitle: "Module 7: Frontend Observability"
moduleGlyph: "📊"
moduleSortOrder: 7
topicSlug: user_analytics
topicTitle: "User Analytics"
topicSortOrder: 4
lesson: user_analytics
title: "User Analytics"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Distinguishes vanity metrics from actionable metrics
    - Explains the HEART framework and gives examples of each dimension
    - Describes privacy considerations in frontend analytics (GDPR, tracking consent)
    - Explains event tracking design — naming conventions and what to track
    - Synthesises an analytics instrumentation plan for a product feature
  keywords: [HEART, Happiness, Engagement, Adoption, Retention, Task success, vanity, actionable, consent, GDPR, event, conversion, funnel, privacy, anonymise]
  modelAnswer: |
    Vanity metrics look good but don't drive decisions: total page views, total registered users, app downloads. Actionable metrics reflect user success and drive product decisions: task completion rate, time-to-first-value, feature activation rate, retention cohorts, funnel conversion.

    The HEART framework (Google): Happiness (user satisfaction — NPS, CSAT), Engagement (interaction frequency and depth — daily active users, sessions per user), Adoption (new feature uptake — % of users using a feature), Retention (continued usage over time — 30-day retention), Task success (completion rate, time on task, error rate for key tasks).

    Privacy: GDPR requires explicit consent for tracking cookies and analytics that can identify individuals. Inform users what you collect. Provide opt-out. Anonymise where possible — session IDs, not user IDs tied to PII. Avoid fingerprinting. Privacy-preserving analytics (Plausible, Fathom) can provide aggregate insights without individual tracking.

    Event tracking design: use a consistent naming convention (noun_verb: button_clicked, form_submitted, page_viewed). Track: page views (automatic), feature activations, key user actions, funnel steps, errors. Don't track: every click, mouse movements, PII in properties. Analytics events should represent user intent, not implementation details.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "Your homepage has 1 million page views per month. Why is this a vanity metric?"
    options:
      - "Page views are difficult to measure accurately"
      - "Page views don't indicate whether users accomplished anything — a million bounces looks the same as a million conversions"
      - "Google Analytics counts page views incorrectly"
      - "1 million is too small a number to be meaningful"
    correctIndex: 1
    feedback: "A million page views could mean: 1 million engaged users who then converted, or 1 million users who immediately left without engaging. Page views say nothing about user success. Actionable equivalents: conversion rate (of those 1M, how many signed up?), feature adoption rate, task completion rate. These metrics tell you whether the product is working — page views tell you only that people arrived."
  - type: SHORT_TEXT
    prompt: "Your team wants to track how users discover a new feature and whether they return to use it. Which HEART dimensions are relevant, and what specific metrics would you measure?"
    hint: "Think about first use vs continued use — which HEART dimensions cover these?"
  - type: FILL_BLANK
    prompt: "GDPR requires ___ before setting analytics cookies. Anonymised analytics (without user identification) may not require consent but should still be disclosed in the ___."
    answer: "explicit consent; privacy policy"
    hint: "Consent is required for tracking; transparency is always required."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which event name follows best practices for analytics event naming?"
    options:
      - "'click_button_2'"
      - "'user_engagement_event_3'"
      - "'checkout_button_clicked'"
      - "'event_15_type_b'"
    correctIndex: 2
    feedback: "Good event names: semantic (describe what happened), consistent (noun_verb pattern), self-describing (no need to look up 'event_15'). 'checkout_button_clicked' tells you the context (checkout), the element (button), and the action (clicked). 'click_button_2' tells you nothing about context. 'event_15' requires a legend to understand."
  - type: MULTIPLE_CHOICE
    question: "A developer tracks analytics.track('Search', { query: searchTerm, userId: user.email }). What is the problem?"
    options:
      - "The event name should be lowercase"
      - "Tracking user.email as a property sends PII to your analytics service — a GDPR violation"
      - "The query parameter should not be tracked"
      - "Analytics should not be called from search components"
    correctIndex: 1
    feedback: "Email is PII. Sending it to an analytics service (which may store it, process it, or transfer it across borders) without explicit consent and a legal basis is a GDPR violation. Use anonymised identifiers: userId: user.anonymisedId. Search queries may also be sensitive — consider whether they should be logged at all, or only aggregate search patterns."
retrieval:
  recall: "Name the five dimensions of the HEART framework. Give one metric for each."
  explain: "Why do product teams benefit from tracking task completion rate rather than feature usage count?"
  mistakeId:
    code: |
      // Analytics instrumentation
      // Track every user interaction for maximum data
      document.addEventListener('click', (e) => {
        analytics.track('click', {
          element: e.target.tagName,
          x: e.clientX,
          y: e.clientY,
          timestamp: Date.now(),
          page: window.location.href,
        });
      });
    answer: "Tracking every click creates: massive data volume (storage and processing cost), noise that drowns actionable signal, and potential privacy issues (tracking exact click coordinates can enable reconstruction of form entries). Analytics should capture user intent at a meaningful level — feature activations, funnel steps, key decisions — not implementation-level events. Replace with specific, intentional event tracking: analytics.track('add_to_cart', { productId, quantity }). Less data, more meaning."
---

# Hook

Your monthly metrics meeting: "Page views: up 40%. Downloads: 12,000. Time on site: up 15%."

Nobody knows if the product is working. These metrics say users arrived — not that they succeeded.

Analytics is not about collecting data. It's about answering questions that drive decisions.

# Lore Introduction

*"The census says the population of the city grew by 20%,"* the Civic Planner explains. *"But are citizens more prosperous? More connected? More capable of participating in civic life? The headcount doesn't tell us."*

She places the HEART framework on the table. *"These are the questions that matter. Happiness. Engagement. Adoption. Retention. Task success. These measure outcomes — not arrivals."*

# Core Learning

## Concept Introduction

**The HEART Framework** (Google, Kerry Rodden):

| Dimension | Question | Example Metric |
|---|---|---|
| **Happiness** | Do users like using it? | NPS, CSAT, satisfaction survey |
| **Engagement** | How deeply do they interact? | Sessions/week, features used per session |
| **Adoption** | Are users discovering and starting? | New feature activation rate, % of users using feature |
| **Retention** | Do they come back? | 30-day retention, churn rate |
| **Task success** | Can users accomplish goals? | Task completion rate, error rate, time on task |

**Event tracking design:**
```ts
// Naming: noun_verb (context_action)
analytics.track('search_performed', {
  resultCount: results.length,
  queryLength: query.length, // not the query itself — potentially sensitive
});

analytics.track('checkout_step_completed', {
  step: 2,
  paymentMethod: 'card', // not card number
  cartTotal: cart.totalCents, // not PII
});

analytics.track('feature_activated', {
  featureName: 'bulk_export',
  trigger: 'sidebar_button',
});
```

**Privacy-first analytics:**
```ts
// Consent required before tracking
if (consent.analytics) {
  analytics.init();
  analytics.track('page_viewed');
}

// Always anonymise
analytics.identify(user.anonymisedId, {
  // NOT: email, name, phone
  accountType: user.plan,
  region: user.country, // coarse-grained
});
```

**Funnel analysis example:**
```
Signup funnel:
Step 1: Landing page viewed → 100%
Step 2: CTA clicked → 42%
Step 3: Email entered → 31%
Step 4: Email verified → 22%
Step 5: Onboarding complete → 14%

Largest drop: Step 1→2 (58%). Investigate the CTA.
```

## Common Mistakes

- **Tracking everything.** Data volume without signal. Track meaningful events, not every click.
- **PII in event properties.** Email, name in analytics events = GDPR violation.
- **Not requiring consent.** Analytics cookies require explicit consent in GDPR jurisdictions.
- **Vanity metrics in dashboards.** Remove them. They give false confidence and waste time in meetings.
- **No clear questions.** Analytics should answer specific product questions, not collect data speculatively.

## Mini Summary

- ✔ HEART framework: Happiness, Engagement, Adoption, Retention, Task success
- ✔ Track intentional events (noun_verb): funnel steps, feature activations, task completions
- ✔ Never track PII in event properties — use anonymised IDs
- ✔ GDPR requires consent for analytics cookies — implement a consent gate
- ✔ Actionable metrics drive decisions; vanity metrics look good but don't

# Guided Practice Quest

Work through the guided steps to identify vanity vs actionable metrics and design GDPR-compliant event tracking.

# Solo Practice Quest

Design an analytics instrumentation plan for a new feature: a "bulk export" button in a data management tool. Define: which HEART dimensions to measure, what events to track, what properties each event should include, what privacy considerations apply, and what questions the data will answer.

# Integration

**Philosophy — Measurement and the Observer Effect**

Heisenberg's uncertainty principle in physics states that observing a particle changes its state. A softer version applies to analytics: how you measure user behaviour influences it. Showing users that they can see their own usage data may change their patterns. Designing features to generate specific metrics may incentivise the metric at the expense of actual value. Goodhart's Law again: when a measure becomes a target, it ceases to be a good measure. Teams optimising for "daily active users" may generate gamified engagement that isn't genuine value. The philosophical antidote: anchor metrics to user outcomes (task completion, return visits, satisfaction) rather than platform behaviour (page views, clicks). Outcome metrics are harder to game because they require the user to genuinely succeed — not just to interact.

# Lore Conclusion

*"The census now counts more than arrivals,"* the Civic Planner says. *"It counts those who found work, those who connected with community, those who returned after leaving. These are the measures that tell us if the city is serving its citizens — not just housing them."*

---
