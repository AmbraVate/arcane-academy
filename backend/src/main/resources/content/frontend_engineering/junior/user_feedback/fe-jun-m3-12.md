---
id: fe-jun-m3-12
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
lesson: error_states
title: "Error States"
sortOrder: 3
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
    - "Shows a specific error message when an async operation fails"
    - "Provides a recovery action (retry button, alternative path)"
    - "Distinguishes between network errors and server validation errors"
    - "Uses error boundaries for unexpected component errors"
  keywords: [error, error-state, retry, recovery, error-boundary, network, server, catch, fallback]
  modelAnswer: |
    Error states must tell users what went wrong and offer a recovery path. Network errors
    ("Connection failed — check your internet") differ from server errors ("Email already
    in use"). Always offer a retry button or alternative action. React Error Boundaries
    catch rendering errors and show a fallback UI instead of a blank white screen.
    Error messages should be empathetic and actionable, not technical.
guidedSteps:
  - id: fe-jun-m3-12-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      An API call fails with a network error. What should the UI show?
    inputConfig:
      options:
        - "A blank page"
        - "\"Error 503\" — the exact HTTP status code"
        - "\"Couldn't connect — check your internet connection\" with a Retry button"
        - "Nothing — silently fail and let the user figure it out"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["\"Couldn't connect — check your internet connection\" with a Retry button"]
      rejectedFeedback: "Error states need: what went wrong (in plain language), what the user can do about it (retry, contact support, go back). HTTP status codes are developer vocabulary — not user vocabulary."
    hint: "What does the user need to know, and what can they do about it?"
    reflectionPrompt: "Never surface technical errors to users. Translate: 503 → 'Our service is temporarily unavailable', CORS error → nothing visible (it's a developer bug to fix). The error message is a conversation with the user, not a log entry."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does a React Error Boundary do?"
    options:
      - "Catches JavaScript syntax errors"
      - "Catches errors during rendering and shows a fallback UI instead of crashing"
      - "Validates props at the boundary component level"
      - "Prevents network errors from reaching components"
    correctIndex: 1
    feedback: "Error Boundaries catch rendering errors in their subtree and display a fallback UI. Without one, an unhandled rendering error crashes the entire React tree — white screen. With one, only the errored subtree is replaced with a fallback."

retrieval:
  recall: "Write the error state UI for a failed article fetch: message, icon, and retry button."
  explain: "How do you distinguish between network errors and server validation errors in a catch block?"
  mistakeId:
    code: "catch(err) { console.error(err) } — error handled in console only, user sees nothing"
    answer: "Set an error state and show it in the UI: catch(err) { setError(err.message || 'Something went wrong'); }. Users don't have browser consoles open."
---

# Hook

Something went wrong. What does the user see? If the answer is "a blank screen" or "a spinning loader forever," your error handling needs work. Errors are not exceptional — they are expected. Design for them.

# Lore Introduction

*"Every expedition,"* says Master Aelindra, *"has contingency plans. The map may be wrong. The bridge may be out. The message may not arrive. The experienced explorer plans for failure as carefully as for success."*

# Core Learning

## Concept Introduction

```jsx
// Error state in a data-fetching component
function ArticleList() {
  const [articles, setArticles] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  async function fetchArticles() {
    setLoading(true);
    setError(null);
    try {
      const data = await api.getArticles();
      setArticles(data);
    } catch (err) {
      // Distinguish error types
      if (!navigator.onLine) {
        setError('No internet connection. Please check your network.');
      } else if (err.status === 404) {
        setError('Articles not found.');
      } else {
        setError('Something went wrong. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  }

  if (error) return (
    <div role="alert" className="error-panel">
      <AlertIcon />
      <p>{error}</p>
      <button onClick={fetchArticles}>Try again</button>
    </div>
  );
}

// Error Boundary (class component)
class ErrorBoundary extends React.Component {
  state = { hasError: false };
  static getDerivedStateFromError() { return { hasError: true }; }
  render() {
    if (this.state.hasError) return <p>Something went wrong. <a href="/">Go home</a></p>;
    return this.props.children;
  }
}
```

## Mini Summary
- ✔ Show a specific, user-friendly error message
- ✔ Distinguish: network error vs server error vs not-found
- ✔ Always provide a recovery action (retry, go back, contact support)
- ✔ Use Error Boundaries to catch rendering errors

# Solo Practice Quest

Build a user profile page that fetches from an API. Handle: loading (skeleton), success (show profile), network error (with retry), 404 (profile not found — different message). Wrap the whole page in an Error Boundary.

# Integration

**Mathematics — Fault Tolerance and Redundancy:** Error states are the frontend implementation of fault tolerance. In distributed systems theory (Lamport, 1982), fault-tolerant systems degrade gracefully under failure rather than crashing. A component with a well-designed error state is fault-tolerant: it continues to function (showing a fallback) even when its data source fails. Error Boundaries extend this to rendering faults, ensuring a subtree failure doesn't cascade to the whole application.

# Lore Conclusion

*"Design for the worst case. The failure that has no plan becomes the crisis. The failure that has a plan becomes an inconvenience."*

---
