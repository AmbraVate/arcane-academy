---
id: fe-jun-m3-11
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m3
moduleTitle: "Module 3: Events and Forms"
moduleGlyph: "📝"
moduleSortOrder: 3
topicSlug: user_feedback
topicTitle: "User Feedback"
topicSortOrder: 4
lesson: loading_states
title: "Loading States"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Shows a loading indicator during async operations"
    - "Disables interactive elements while loading to prevent state corruption"
    - "Uses skeleton screens for content that will replace a layout"
    - "Avoids showing loading state for instant operations (< 100ms)"
  keywords: [loading, spinner, skeleton, disabled, async, indicator, optimistic-UI, threshold]
  modelAnswer: |
    Loading states communicate that the application is working. Show a spinner or skeleton
    screen during async operations. Disable inputs and submit buttons during loading to
    prevent duplicate actions. Skeleton screens (placeholder shapes matching content)
    are less disorienting than spinners for content that replaces a layout. Avoid showing
    loading indicators for operations under ~100ms — they flash and feel broken.
guidedSteps:
  - id: fe-jun-m3-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A page fetches a list of articles. Which loading UI is best?
    inputConfig:
      options:
        - "Show nothing until data arrives"
        - "Show a spinner in the centre of the page"
        - "Show skeleton cards matching the shape of real article cards"
        - "Show a progress bar across the top"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Show skeleton cards matching the shape of real article cards"]
      rejectedFeedback: "Skeleton screens set the layout expectation before content arrives — reducing the visual 'pop' of content appearing. A spinner gives no sense of what will arrive. Showing nothing creates confusion about whether the page is working."
    hint: "Which option gives the user the most information about what's coming?"
    reflectionPrompt: "Skeleton screens are now standard practice for content loading in most major applications (LinkedIn, Facebook, YouTube) because they reduce perceived load time and layout shift. The brain processes the layout while the content loads."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When should you NOT show a loading indicator?"
    options:
      - "Never — always show loading for any async operation"
      - "When the operation takes less than ~100ms — the flash of loading UI feels broken"
      - "When the user is offline"
      - "For read operations — only write operations need loading states"
    correctIndex: 1
    feedback: "Operations under ~100ms complete before the user perceives any delay. A loading spinner that flashes for 50ms is more disorienting than no indicator. Add a minimum display threshold or delay showing the indicator until ~200ms has passed."

retrieval:
  recall: "Describe when to use a skeleton screen vs a spinner vs optimistic UI."
  explain: "What is optimistic UI and what are its risks?"
  mistakeId:
    code: "No loading state — user clicks submit, nothing happens for 3 seconds, clicks again"
    answer: "Add isLoading state. Show 'Submitting…' in the button, disable interactive elements. The user knows the system received their action and is working."
---

# Hook

A 3-second wait with no feedback feels like 10 seconds. The same wait with a loading indicator feels like 2. Loading states are not just UI decoration — they are the difference between "is this broken?" and "I know it's working."

# Lore Introduction

*"The Academy's messenger,"* says Master Aelindra, *"always acknowledges receipt before departing. 'I have your message. I am on my way.' The wait with acknowledgement is tolerable. The wait without it is anxiety."*

# Core Learning

## Concept Introduction

```jsx
// Loading state in a button
<button disabled={isLoading}>
  {isLoading ? <Spinner /> : 'Submit'}
</button>

// Skeleton screen (with CSS animation)
function ArticleCardSkeleton() {
  return (
    <div className="card skeleton">
      <div className="skeleton-line skeleton-title" />
      <div className="skeleton-line" />
      <div className="skeleton-line skeleton-short" />
    </div>
  );
}

// Conditional: skeletons while loading, real content when ready
{isLoading
  ? Array.from({ length: 6 }).map((_, i) => <ArticleCardSkeleton key={i} />)
  : articles.map(a => <ArticleCard key={a.id} article={a} />)
}

// Optimistic UI — update immediately, revert on error
function likePost(postId) {
  setLiked(true);  // instant feedback
  api.likePost(postId).catch(() => setLiked(false));  // revert if fails
}
```

**Loading patterns:**

| Pattern | Use when |
|---|---|
| Spinner | Action without known layout (submit, delete) |
| Skeleton | Content with known layout (lists, cards) |
| Progress bar | Uploads, step-by-step processes |
| Optimistic UI | Low-risk reversible actions (like, save) |

## Common Mistakes

- **Not disabling interactive elements during loading**: If the user can click "Submit" again while the first request is in flight, duplicate requests result. Disable buttons and inputs during `isLoading`.
- **Using a spinner where a skeleton fits better**: A spinner for a card grid shows nothing until data arrives. A skeleton screen shows the layout shape immediately, making the page feel faster and more stable.
- **Showing a loading state for operations that take less than 100ms**: Flash-of-loading for fast operations (instant local updates) is worse than no loading state — it creates unwanted visual noise. Only show loading UI when the operation genuinely takes perceptible time.
- **Applying optimistic updates without a revert on error**: Optimistic UI must always roll back if the server request fails. Permanently showing a liked state on a failed API call misleads the user about the actual state of their data.

## Mental Model

Loading states are restaurant service during the wait for food, and every pattern has a service equivalent. The order acknowledged immediately — "lovely, that'll be about ten minutes" — is the instant state change to *loading*: the kitchen may be slow, but the *acknowledgement* never is, and a user's click, like a diner's order, must visibly land. Bread on the table is the skeleton screen: not the meal, but shaped like sustenance, occupying the place the meal will fill — it manages expectation (food is coming, roughly this much, roughly here) and prevents the jarring rearrangement of a table being re-laid mid-meal (layout shift when real content lands in pre-shaped slots). The water glass kept full while one course runs late is *scoped* loading: a delayed dessert doesn't mean waiters stop serving the rest of the table — one slow data region gets its local indicator while the rest of the page stays interactive, never a whole-restaurant blackout (the global spinner) for one kitchen station's delay. Service timing has its thresholds too: a waiter who announces "your food is coming!" four seconds after you order, for a dish that arrives two seconds later, has made fast service feel fussy — the flash-of-spinner problem, solved by not announcing waits too short to matter (delayed indicators) and not whisking the bread away the instant it's touched (minimum display times, avoiding flicker). And for the genuinely long wait, diners need what passengers need: progress and honesty — "the soufflé takes twenty minutes" (progress indication, time expectation) beats a silent kitchen door, because uncertainty, not duration, is what makes people leave. Acknowledge instantly, bread for shape, serve around the late course, don't announce micro-waits, be honest about long ones: loading UX as table service.

## Why It Matters

Loading states are where your interface meets the speed of reality — networks are slow, variable, and unreliable, and what you render during the wait determines whether users perceive your app as fast, broken, or trustworthy:

- The perception numbers drive the patterns: sub-100ms needs no indicator (showing one adds flicker), beyond a second needs acknowledgement, beyond several needs progress or reassurance — and the classic mistake, a spinner that flashes for 80ms on a fast connection, makes a *fast* app feel janky, which is why minimum-display times and delayed-show thresholds exist
- Skeleton screens beat spinners for content loads because they manage *expectation*: shaped placeholders promise what's coming and prevent layout shift when it arrives — the difference between a page assembling itself smoothly and content jumping under the user's poised finger (a real Core Web Vitals cost, not just polish)
- Loading is per-region, not per-page: one slow widget shouldn't blank the whole screen behind a global spinner — scoping load states to the component that's actually waiting (this module's state patterns, applied locally) keeps the rest of the interface alive and useful
- The waiting states are also the *interaction-safety* states: search results arriving out of order, buttons that should disable during their action, content shifting as it loads — the bug family of async UI lives here, and deliberate loading design is the prevention

Every app is fast on the developer's machine. Loading states are engineering for everyone else's.

## Mini Summary
- ✔ Always show loading state for operations > 100ms
- ✔ Disable interactive elements during loading
- ✔ Skeleton screens for layout-replacing content
- ✔ Optimistic UI for instant-feeling interactions (with rollback on error)

# Solo Practice Quest

Build a dashboard that fetches 6 articles. Show skeleton cards while loading (CSS animated placeholder). When loaded, replace with real cards. Add a "Refresh" button that shows a loading state.

# Integration

**Psychology — Uncertainty and the Dread Effect:** Research on uncertainty aversion shows that waiting with no information is subjectively worse than waiting the same duration with a progress indicator — even if the indicator is inaccurate. The source of this effect is reduced uncertainty: any information (a spinner, a loading bar) signals that the system is working and the wait is finite.

# Lore Conclusion

*"Show the hourglass. Show the progress. Show the skeleton. Certainty — even partial — is more comfortable than silence."*

---
