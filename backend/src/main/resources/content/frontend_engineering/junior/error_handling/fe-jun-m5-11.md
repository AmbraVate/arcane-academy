---
id: fe-jun-m5-11
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: error_handling
topicTitle: "Error Handling"
topicSortOrder: 4
lesson: error_boundaries_and_fallbacks
title: "Error Boundaries and Fallbacks"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-10]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what an Error Boundary is and what it prevents"
    - "Distinguishes between async errors (caught by try/catch) and render errors (caught by Error Boundaries)"
    - "Describes graceful degradation as a design principle"
    - "Explains a basic retry strategy"
  keywords: [ErrorBoundary, fallback, graceful, degradation, render, try, catch, retry, component]
  modelAnswer: |
    An Error Boundary is a React class component that catches JavaScript errors thrown during rendering of its child component tree, preventing the entire app from crashing. Async errors (in useEffect, event handlers) are not caught by Error Boundaries — they need try/catch. Graceful degradation means showing reduced but functional UI instead of a crash: a broken image shows a placeholder, a failed widget shows "content unavailable" rather than destroying the page. Retry logic gives users a way to recover: a "Try Again" button that re-triggers the fetch.
guidedSteps:
  - id: fe-jun-m5-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "An Error Boundary wraps your `<ProductList />` component. The fetch inside ProductList throws an unhandled error during rendering. What happens?"
    inputConfig:
      options:
        - "The entire app crashes with a white screen"
        - "The Error Boundary catches it and renders its fallback UI instead"
        - "The fetch is automatically retried"
        - "React ignores the error and renders an empty component"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The Error Boundary catches it and renders its fallback UI instead"]
      rejectedFeedback: "Error Boundaries catch render-time errors in their children and render a fallback UI instead — preventing the entire app from crashing."
    hint: "Error Boundaries act like a try/catch specifically for the React render cycle."
    reflectionPrompt: "Without an Error Boundary, what would the user see if a deeply nested component threw during rendering?"
  - id: fe-jun-m5-11-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "Does an Error Boundary catch errors thrown inside an async function in useEffect?"
    inputConfig:
      options:
        - "Yes — Error Boundaries catch all JavaScript errors"
        - "No — Error Boundaries only catch errors thrown during rendering, not async code"
        - "Only if the error is re-thrown from the catch block"
        - "Yes, but only in production builds"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["No — Error Boundaries only catch errors thrown during rendering, not async code"]
      rejectedFeedback: "Error Boundaries catch synchronous render-time errors. Async errors (in useEffect, event handlers, setTimeout) need their own try/catch."
    hint: "Error Boundaries intercept during the React render cycle — async operations happen outside that cycle."
    reflectionPrompt: "So if you need both: Error Boundary for render errors AND try/catch for async errors — where would each live in your component?"
  - id: fe-jun-m5-11-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Explain 'graceful degradation' in the context of a widget that fails to load. What should the UI show? Why is this better than crashing?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [fallback, placeholder, still, work, function, crash, available, content]
      rejectedFeedback: "Graceful degradation means the rest of the app still works even when one part fails. Show a placeholder: 'Content unavailable' or 'Try again later'. The user can still use other parts of the page."
    hint: "Think about a dashboard with six widgets — if one fails, should all six break? What should happen instead?"
    reflectionPrompt: "What's the minimum viable fallback you can show that still communicates something useful to the user?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the key feature of a React Error Boundary component?"
    options:
      - "It uses the useState hook to catch errors"
      - "It implements the componentDidCatch and getDerivedStateFromError lifecycle methods"
      - "It wraps fetch() calls in automatic try/catch blocks"
      - "It must be a functional component using hooks"
    correctIndex: 1
    feedback: "Error Boundaries must be class components implementing componentDidCatch (for logging) and getDerivedStateFromError (for showing fallback UI). They cannot be functional components."
retrieval:
  recall: "What types of errors does an Error Boundary catch, and what types does it NOT catch?"
  explain: "Describe the concept of graceful degradation and give a real example of it in a web application context."
  mistakeId:
    code: |
      function App() {
        return (
          <div>
            <Header />
            <ProductWidget />
            <NewsWidget />
            <Footer />
          </div>
        );
      }
      // ProductWidget throws during render — entire page crashes
    answer: "Wrap each widget in an Error Boundary. If ProductWidget crashes, only that widget shows a fallback; the rest of the page works. Place Error Boundaries around independent sections so failures are isolated."
---

# Hook

Your main page has six widgets: news, weather, stock prices, user recommendations, recent activity, and announcements. The stock prices API is down. The entire page crashes with a white screen. All six widgets fail because one did. Every user gets a broken page because of an unrelated, optional piece of content. This is the problem that Error Boundaries and graceful degradation solve: isolating failures so that one broken thing doesn't destroy everything else.

# Lore Introduction

*In the Academy's great Exhibition Hall, six enchanted displays show information from across the realm: weather from the North Tower, trade prices from the Market Ward, messages from the Courier Guild. When the Courier Guild's crystal goes dark, the old Exhibition Hall protocol was clear: all displays would dim, the hall would close. One failure, total shutdown. The new Magistra Evren designed something better: each display has its own containment rune. When one goes dark, it shows only a quiet notice — "Guild unreachable" — while the others shine on. The Hall remains open. Life continues.*

# Core Learning

## Concept Introduction

**Error Boundaries** are React class components that catch JavaScript errors thrown during rendering of their child component tree. Without them, an unhandled render error crashes the entire app.

An Error Boundary:
- Catches errors during **rendering**, **lifecycle methods**, and **constructors** of child components
- Does **NOT** catch errors in: event handlers, async code, server-side rendering, or the boundary itself

For async errors (fetch failures, useEffect errors), you need regular `try/catch`.

**Graceful degradation** is the principle of showing reduced-but-functional UI rather than crashing: instead of a white screen, show "this section is unavailable."

**Retry logic** gives users agency: a "Try Again" button that re-triggers the failed operation.

## Why It Matters

Real production apps have many independent sections. Isolating errors prevents cascade failures. A broken recommendation widget shouldn't take down the checkout flow. Error Boundaries are the mechanism for this isolation.

## Worked Example

```jsx
// Error Boundary class component
class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, info) {
    // Log to error tracking service (Sentry, etc.)
    console.error('Component error:', error, info.componentStack);
  }

  render() {
    if (this.state.hasError) {
      return this.props.fallback || (
        <div className="p-4 bg-gray-50 border border-gray-200 rounded text-gray-500">
          This section is currently unavailable.
        </div>
      );
    }
    return this.props.children;
  }
}

// Using Error Boundaries to isolate failures
function Dashboard() {
  return (
    <div className="grid grid-cols-2 gap-4 p-6">
      <ErrorBoundary fallback={<WidgetError name="Profile" />}>
        <ProfileWidget />
      </ErrorBoundary>

      <ErrorBoundary fallback={<WidgetError name="News" />}>
        <NewsWidget />
      </ErrorBoundary>

      <ErrorBoundary fallback={<WidgetError name="Stats" />}>
        <StatsWidget />
      </ErrorBoundary>
    </div>
  );
}

function WidgetError({ name }) {
  return (
    <div className="flex items-center justify-center h-32 bg-gray-50 rounded border border-gray-200">
      <p className="text-sm text-gray-400">{name} unavailable</p>
    </div>
  );
}
```

Retry logic pattern in an async context:
```jsx
function RetryableWidget() {
  const [isLoading, setIsLoading] = React.useState(true);
  const [data, setData] = React.useState(null);
  const [error, setError] = React.useState(null);
  const [retryCount, setRetryCount] = React.useState(0);

  React.useEffect(() => {
    async function load() {
      setIsLoading(true);
      setError(null);
      try {
        const res = await fetch('/api/widget-data');
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const d = await res.json();
        setData(d);
      } catch (err) {
        setError(err.message);
      } finally {
        setIsLoading(false);
      }
    }
    load();
  }, [retryCount]); // increment retryCount to trigger re-fetch

  if (isLoading) return <div className="animate-pulse h-24 bg-gray-100 rounded" />;

  if (error) return (
    <div className="p-4 bg-red-50 rounded">
      <p className="text-red-600 text-sm mb-2">Failed to load</p>
      <button
        onClick={() => setRetryCount(c => c + 1)}
        className="text-sm bg-red-100 hover:bg-red-200 px-3 py-1 rounded"
      >
        Try Again
      </button>
    </div>
  );

  return <div className="p-4">{data.content}</div>;
}
```

## Common Mistakes

**Mistake 1: Expecting Error Boundary to catch async errors**
```jsx
// Error Boundary won't catch this
function Widget() {
  React.useEffect(() => {
    throw new Error('oops'); // async context — not caught by boundary
  }, []);
}
// Solution: wrap with try/catch inside useEffect, set error state
```

**Mistake 2: One global Error Boundary for the whole app**
```jsx
// COARSE — any error takes down everything
<ErrorBoundary><App /></ErrorBoundary>

// FINE-GRAINED — errors are isolated to their component
<ErrorBoundary><ProfileWidget /></ErrorBoundary>
<ErrorBoundary><NewsWidget /></ErrorBoundary>
```

**Mistake 3: Not providing a user-facing fallback**
```jsx
// UNHELPFUL — renders nothing, user is confused
static getDerivedStateFromError() {
  return { hasError: true };
}
render() {
  if (this.state.hasError) return null; // blank!
}
```

## Mini Summary

- **Error Boundaries** are class components that catch render-time errors in children
- They do NOT catch errors in async code, event handlers, or themselves
- Async errors need `try/catch` inside `useEffect` or event handlers
- **Graceful degradation** — show "unavailable" fallback, not blank or crash
- **Retry logic** — let users re-trigger failed operations via a "Try Again" button
- Place Error Boundaries around independent sections for isolated failure containment

# Guided Practice Quest

Work through the guided steps to understand what Error Boundaries catch, what they don't, and how graceful degradation is implemented.

# Solo Practice Quest

Explain Error Boundaries: what they catch, what they don't, and why they must be class components. Describe graceful degradation and why it's better than letting errors crash whole pages. Write an example of an Error Boundary being used to isolate a widget failure.

# Integration

**Design — Fault Tolerance as UX:** Norman's concept of "error tolerance" in design systems states that products should tolerate mistakes and misuse without catastrophic failure. In UI terms, a single broken widget causing a full-page crash violates this principle dramatically. Error Boundaries are a technical implementation of design fault tolerance — designing for failure as a first-class scenario, not an afterthought.

**Psychology — Locus of Control:** Research by Julian Rotter on locus of control shows that people feel more positive about outcomes they can influence. A page that fails with a silent white screen creates an external locus — the user has no agency. A page that shows "Something went wrong — try again" with a retry button creates an internal locus — the user can act. Retry buttons aren't just UX politeness; they're psychologically significant signals that the user has power over the situation.

# Lore Conclusion

*The Exhibition Hall stands open. Five displays shine. One reads quietly: "Guild unreachable — check back soon." Visitors navigate the Hall, learn from the other displays, and make a mental note to check the Guild message later. The Academy has not failed them. One crystal is dark, but the Hall is alive. This is the discipline of isolation: knowing that one failure need not be everyone's failure.*

---
