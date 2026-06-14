---
id: fe-jun-m4-05
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m4
moduleTitle: "Module 4: Routing and Navigation"
moduleGlyph: "🗺️"
moduleSortOrder: 4
topicSlug: react_router
topicTitle: "React Router"
topicSortOrder: 2
lesson: link_and_navlink
title: "Link and NavLink"
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
    - "Uses Link instead of <a> for internal navigation"
    - "Explains why Link prevents page reload"
    - "Uses NavLink to apply active styles to the current route"
    - "Uses useNavigate for programmatic navigation"
  keywords: [Link, NavLink, useNavigate, active, className, isActive, programmatic, navigate, href]
  modelAnswer: |
    Link renders an anchor tag but intercepts the click to use client-side routing —
    no page reload. NavLink extends Link with an isActive state — it applies an active
    class when the current URL matches its to prop. useNavigate() returns a navigate
    function for programmatic navigation (after form submission, auth redirect).
guidedSteps:
  - id: fe-jun-m4-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Why should you use `<Link to="/about">` instead of `<a href="/about">` in React Router?
    inputConfig:
      options:
        - "They are identical — Link is just a renamed anchor"
        - "Link uses client-side routing (no reload); <a> triggers a full page reload"
        - "Link works with TypeScript; <a> does not"
        - "<a> tags are not valid in React JSX"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Link uses client-side routing (no reload); <a> triggers a full page reload"]
      rejectedFeedback: "<a href> tells the browser to request a new page from the server — destroying React state and reloading all resources. Link intercepts the click, updates the URL via History API, and renders the new component — the SPA's core advantage."
    hint: "What does a browser do by default when clicking an anchor tag?"
    reflectionPrompt: "Every <a href> to an internal route in a React app is a bug. Always use Link for internal navigation. Use <a href> only for external links or file downloads."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "NavLink's className prop can accept a function. What argument does that function receive?"
    options:
      - "The link element's DOM reference"
      - "{ isActive, isPending } — whether the link matches the current route"
      - "The route path string"
      - "The navigation event object"
    correctIndex: 1
    feedback: "className={({ isActive }) => isActive ? 'active' : ''} applies 'active' class when the NavLink's route is current. isPending is true during navigation to a route with a data loader. This enables active link styling without manual URL comparisons."

retrieval:
  recall: "Write a NavLink that applies class 'nav-active' when it matches the current route."
  explain: "When would you use useNavigate() instead of Link?"
  mistakeId:
    code: "<a href='/dashboard'>Go to Dashboard</a> inside a React Router app"
    answer: "Use <Link to='/dashboard'>Go to Dashboard</Link> to prevent a full page reload."
---

# Hook

Link is how users navigate. NavLink adds awareness of which route is current. useNavigate handles navigation in code. Together, these three tools cover all navigation scenarios in a React app.

# Lore Introduction

*"The Academy directory has two types of pointers,"* says Master Aelindra, *"regular pointers that lead you to another room, and active pointers that glow when you are already in that room. Both serve different purposes."*

# Core Learning

## Concept Introduction

```jsx
// Link — client-side navigation, no reload
<Link to="/about">About us</Link>

// NavLink — adds active class/style for current route
<NavLink
  to="/about"
  className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
>
  About us
</NavLink>

// useNavigate — programmatic navigation
function LoginForm() {
  const navigate = useNavigate();
  
  async function handleSubmit() {
    await login(credentials);
    navigate('/dashboard');        // go to dashboard after login
    // navigate(-1);              // go back (like browser back button)
    // navigate('/home', { replace: true }); // replace history entry
  }
}
```

## Common Mistakes

- **Using `<a href>` for internal navigation**: A plain anchor tag causes a full page reload, destroying React state and losing the SPA's speed advantage. Use `<Link to="...">` for all internal routes.
- **Calling `useNavigate()` result during render**: `navigate('/home')` must be called inside an event handler or `useEffect` — calling it directly in the component body causes navigation on every render.
- **Using NavLink without a function for `className`**: `className="active"` applies the class statically. Use `className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}` to apply the active class conditionally.
- **Not using `{ replace: true }` after login redirects**: After a successful login, the back button should not return to the login page. Use `navigate('/dashboard', { replace: true })` to replace the login entry in the history stack.

## Mini Summary
- ✔ Link: internal navigation without page reload
- ✔ NavLink: Link + isActive prop for styling current route
- ✔ useNavigate: programmatic navigation after events
- ✔ Never use `<a href>` for internal routes

# Solo Practice Quest

Build a navigation bar with NavLink for all routes. Apply active styling to the current route. Add a logout button that uses useNavigate to redirect to /login after clearing auth state.

# Integration

**Psychology — Wayfinding and Signposting:** Active navigation links (NavLink) implement digital wayfinding — they tell the user where they are in the application. This reduces cognitive load (the user doesn't need to infer their location from the page content) and mirrors physical wayfinding cues like "You Are Here" markers in public spaces.

# Lore Conclusion

*"Show the user where they are. The glowing pointer is not decoration — it is orientation."*

---
