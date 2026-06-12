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

## Mental Model

A good error state behaves like a well-trained flight attendant during turbulence, and the comparison supplies every design rule. First: calm, specific, immediate — "we're experiencing turbulence, please be seated; we expect it to pass in ten minutes" — never silence (the request failed and the UI shows nothing), never the cockpit's raw instrumentation shouted into the cabin (`ECONNREFUSED`, stack traces, error codes the passenger can't act on), and never a grinning "Oopsie!" while the oxygen masks deploy (cutesy copy at a consequence-bearing failure — mismatched tone reads as a company that doesn't grasp what just happened to you). Second: the announcement always contains *what to do now* — stay seated, belt on — because information without action is just sophisticated alarm; every error UI needs its equivalent verb: Retry for the transient, Fix-this-field for the correctable, Go back / Contact support for the dead end, each matched to what actually went wrong rather than one generic apology for all weather. Third: the crew protects what you'd lose in the chaos — your typed data, your place in the flow — because turbulence that costs passengers their belongings becomes the story they tell about the airline; an error that erases a half-completed form converts one failure into two. Fourth: announcements go over the *speakers*, not just the seatbelt sign — `role="alert"` and live regions are the audio channel, without which non-sighted passengers experience the turbulence with no announcement at all. And the deepest parallel: passengers don't rate airlines by whether turbulence ever happened — they rate them by how the crew behaved when it did. Calm specifics, a clear next action, belongings protected, everyone informed: error states as cabin crew training.

## Why It Matters

Error states are your product's behaviour at its worst moment — things have already gone wrong, the user is already frustrated, and what you render next either recovers them or loses them:

- Errors are not one state but a family, and conflating them is the root design failure: a network drop (retryable, not the user's fault), a validation rejection (fixable, specific), a permission wall (not fixable here, needs a path elsewhere), and a genuine crash (apologise, offer escape) each demand different copy, different actions, and different tone — the single generic "Something went wrong" treats a flat tyre and an engine fire identically
- Recovery is the design goal, not display: every error state needs an *action* — retry for transient failures, focus-the-field for fixable input, "go back" or "contact support" for dead ends — and must preserve the user's context and data, because an error screen that loses their work converts a hiccup into abandonment
- Honesty has limits in both directions: raw technical detail (`AxiosError 500 at /api/v2/orders`) frightens and helps no one, while over-cheerful vagueness ("Oops! 🙈") at a serious failure (their payment may or may not have gone through) destroys trust — the craft is plain language about impact and next step, with technical detail logged for you, not shown to them
- The mechanics complete this module's arc: error is a state alongside loading and success (the fetch trio), it must reach assistive technology (`role="alert"` — a visually obvious failure that screen readers never announce is a silent dead end), and component-level containment (error boundaries, scoped fallbacks) keeps one failed region from taking down the page

Users forgive failures surprisingly well when the failure *handles them* well. Error states are where that handling is designed — which makes them, quietly, a retention feature.

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
