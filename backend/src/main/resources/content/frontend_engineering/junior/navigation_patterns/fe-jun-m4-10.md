---
id: fe-jun-m4-10
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
lesson: protected_routes
title: "Protected Routes"
sortOrder: 1
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
    - "Implements a ProtectedRoute wrapper that checks auth state"
    - "Redirects unauthenticated users to login with the original destination"
    - "Preserves the intended destination after login using location state"
    - "Applies ProtectedRoute to multiple routes without duplication"
  keywords: [ProtectedRoute, Navigate, auth, redirect, location-state, isAuthenticated, wrapper, from]
  modelAnswer: |
    Protected routes wrap route elements in an auth check. If unauthenticated, redirect
    to login with the original URL as state: <Navigate to='/login' state={{ from: location }} />.
    After login, the auth flow reads location.state.from and navigates there: navigate(from || '/dashboard').
    This preserves the user's intent — they go directly to the page they requested after logging in.
guidedSteps:
  - id: fe-jun-m4-10-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user visits /settings while logged out. After logging in, where should they go?
    inputConfig:
      options:
        - "Always /dashboard — the safe default"
        - "/settings — where they originally tried to go"
        - "/home — back to the start"
        - "The login page again to confirm"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["/settings — where they originally tried to go"]
      rejectedFeedback: "Preserving the intended destination respects user intent. Without it, users who click a link to /settings (e.g., from an email), log in, and land on /dashboard have to find /settings again — a frustrating extra step."
    hint: "What did the user intend to do before being interrupted by the login wall?"
    reflectionPrompt: "This pattern is called 'return to' or 'intended URL preservation'. It is a significant UX quality signal. Implement it by passing location.pathname as state to the Navigate redirect, then reading it in the login success handler."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which Navigate prop tells the browser to replace the history entry instead of pushing a new one?"
    options:
      - "type='replace'"
      - "replace (boolean prop)"
      - "history='replace'"
      - "mode='replace'"
    correctIndex: 1
    feedback: "<Navigate to='/login' replace /> replaces the current history entry. Without replace, the user has to press back twice to leave the login page (once to the protected route, once more to wherever they were before). replace gives cleaner back-navigation."

retrieval:
  recall: "Write a ProtectedRoute component that redirects to /login if user is not authenticated."
  explain: "How do you preserve and restore the original destination after authentication?"
  mistakeId:
    code: "Always redirecting to /dashboard after login regardless of intended URL"
    answer: "Pass location state on redirect: <Navigate to='/login' state={{ from: location.pathname }} />. In login handler: const from = location.state?.from ?? '/dashboard'; navigate(from);"
---

# Hook

Protected routes guard pages from unauthenticated access. The implementation matters — a crude guard that always drops users at the dashboard after login degrades UX. A good guard remembers where they were going.

# Lore Introduction

*"The Academy's restricted wing,"* says Master Aelindra, *"records your intended destination when you are turned away. When your credentials are verified, you are escorted directly there. The Academy respects your time."*

# Core Learning

## Concept Introduction

```jsx
function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    return (
      <Navigate
        to="/login"
        state={{ from: location.pathname }}
        replace
      />
    );
  }
  return children;
}

// Usage
<Route path="/settings" element={
  <ProtectedRoute><Settings /></ProtectedRoute>
} />

// In LoginPage — redirect to original destination
function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from ?? '/dashboard';

  async function handleLogin() {
    await login();
    navigate(from, { replace: true });
  }
}
```

## Common Mistakes

- **Redirecting to a hardcoded `/dashboard` after login**: If the user was trying to access `/settings` before being redirected to login, sending them to `/dashboard` on success loses their intended destination. Pass `state={{ from: location.pathname }}` and redirect back.
- **Not using `replace` on the post-login navigate call**: `navigate(from)` (without `replace`) adds the login page to the history stack — the user can press back to reach the login page while authenticated. Use `navigate(from, { replace: true })`.
- **Checking authentication inside the route component rather than in a wrapper**: Every protected route component would need to repeat the auth check. Centralise it in a `<ProtectedRoute>` wrapper that is reused across all protected routes.
- **Using a JWT from state or local storage without validating it**: Storing `isAuthenticated: true` in React state persists only for the session. A page refresh clears state — always derive authentication status from a persistent source (localStorage, a cookie, or a re-validation fetch).

## Mini Summary
- ✔ Check auth in a wrapper component; redirect if unauthenticated
- ✔ Pass `state={{ from: location.pathname }}` to preserve destination
- ✔ After login, read state.from and navigate there
- ✔ Use `replace` to prevent double back-press after login

# Solo Practice Quest

Implement protected routes for /dashboard, /profile, /settings. Test: log out, visit /settings directly, log in, and verify you land on /settings not /dashboard.

# Integration

**Mathematics — Predicate Guards:** Protected routes implement predicate guards — boolean conditions that must be true for execution to proceed. This is equivalent to precondition checks in formal program verification: if precondition fails (not authenticated), redirect (exceptional control flow); otherwise proceed normally.

# Lore Conclusion

*"Turn away the unauthorised, record their intent, welcome them back directly upon authorisation. That is the mark of a considerate system."*

---
