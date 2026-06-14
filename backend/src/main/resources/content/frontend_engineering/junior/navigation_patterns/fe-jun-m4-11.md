---
id: fe-jun-m4-11
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m4
moduleTitle: "Module 4: Routing and Navigation"
moduleGlyph: "🗺️"
moduleSortOrder: 4
topicSlug: navigation_patterns
topicTitle: "Navigation Patterns"
topicSortOrder: 4
lesson: redirects
title: "Redirects"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses Navigate component for declarative redirects"
    - "Uses useNavigate for imperative redirects"
    - "Knows when to use replace vs push for history management"
    - "Handles legacy URL migrations with redirects"
  keywords: [Navigate, useNavigate, redirect, replace, push, legacy, declarative, imperative, history]
  modelAnswer: |
    Declarative redirects use the Navigate component in JSX — rendered when a condition
    is met. Imperative redirects use useNavigate() — called after an event (login, form
    submit). replace=true replaces the current history entry (cleaner back navigation);
    the default push adds a new entry. Redirects handle: auth guards, old URL migrations,
    alias routes, post-action navigation.
guidedSteps:
  - id: fe-jun-m4-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You move a page from /old-about to /about. How do you handle users with old bookmarks?
    inputConfig:
      options:
        - "Let them 404 — it's their problem to update bookmarks"
        - "<Route path='/old-about' element={<Navigate to='/about' replace />} />"
        - "Delete the old route entry"
        - "Add both /old-about and /about as separate pages"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["<Route path='/old-about' element={<Navigate to='/about' replace />} />"]
      rejectedFeedback: "Redirect old URLs to new ones for backward compatibility. replace=true is important: the old URL is replaced in history so back navigation doesn't cycle between old and new."
    hint: "You can add a Route that just redirects."
    reflectionPrompt: "URL stability is a quality-of-service commitment. Once a URL is published (shared, bookmarked, indexed), it should work forever — or redirect to its new location. Broken links damage trust and SEO."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "After a user deletes their account, you navigate to /goodbye. Which history option is correct?"
    options:
      - "navigate('/goodbye') — push a new entry"
      - "navigate('/goodbye', { replace: true }) — replace current entry"
      - "navigate(-1) — go back"
      - "window.location.href = '/goodbye'"
    correctIndex: 1
    feedback: "replace: the user shouldn't be able to go back to the account page after deletion. replace removes the previous history entry. Similarly for login redirects — you don't want the user to navigate back to the login page once they're authenticated."

retrieval:
  recall: "List three scenarios where you would use replace: true vs default push navigation."
  explain: "What is the difference between declarative (Navigate) and imperative (useNavigate) redirects?"
  mistakeId:
    code: "navigate('/dashboard') after login — user can press Back to return to login page"
    answer: "Use navigate('/dashboard', { replace: true }) — replaces the login page in history so Back goes to wherever they were before login, not back to the login page."
---

# Hook

Redirects handle URL migration, auth gates, and post-action navigation. The choice between replace and push is a UX decision — it determines whether the user can go back.

# Lore Introduction

*"The Academy's corridor restructuring,"* says Master Aelindra, *"moved the library from Wing B to Wing A. Every old Wing B sign now points to Wing A. The old path is preserved in memory but redirects instantly. No visitor is lost; no bookmark is broken."*

# Core Learning

## Concept Introduction

```jsx
// Declarative — rendered when condition is true
function Dashboard() {
  const { isAuthenticated } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  return <div>Dashboard content</div>;
}

// Alias/legacy URL redirect
<Route path="/old-contact" element={<Navigate to="/contact" replace />} />

// Imperative — called after events
function DeleteAccountForm() {
  const navigate = useNavigate();
  async function handleDelete() {
    await deleteAccount();
    navigate('/goodbye', { replace: true });
  }
}

// Back navigation
navigate(-1);  // go back
navigate(1);   // go forward
navigate(-2);  // go back 2 steps
```

## Common Mistakes

- **Using `<Navigate>` when `useNavigate()` is more appropriate**: `<Navigate>` renders in JSX and fires on every render of that branch. For redirects that should happen after a user action (button click, form submit), use `useNavigate()` inside the handler instead.
- **Confusing `replace` and `push`**: The default `navigate('/path')` pushes to the history stack — back is available. Using `replace: true` replaces the current entry — back is not available. Use `replace` for login redirects and alias routes where the replaced URL should never appear in history.
- **Navigating by index (negative number) without knowing the stack**: `navigate(-1)` goes back one step, but if the user arrived directly (new tab, bookmark), `-1` may leave the app entirely. Guard with checking history length before using negative navigation.
- **Placing a `<Navigate>` component outside a route context**: `<Navigate>` must be rendered inside a `<BrowserRouter>` context — placing it in a component that can render outside the router (e.g., error boundaries at the root) will throw.

## Mini Summary
- ✔ Navigate component: declarative redirect in JSX
- ✔ useNavigate(): imperative redirect after events
- ✔ replace: true — replace history (no back to previous route)
- ✔ Default push — adds to history (back is available)

# Solo Practice Quest

Add redirects to: (1) /home → / (alias), (2) /old-pricing → /pricing (legacy URL), (3) after form submission, navigate to /thank-you with replace. Test that the browser back button behaves correctly in each case.

# Integration

**Mathematics — Graph Transformations:** Redirects transform the navigation graph — adding edges that map old nodes to new nodes. URL migration is a graph rewrite rule. The replace vs push distinction determines whether the redirect node is preserved in the traversal history or eliminated.

# Lore Conclusion

*"No valid request should go unanswered. Redirect what cannot be served where it was requested. Keep the old paths alive."*

---
