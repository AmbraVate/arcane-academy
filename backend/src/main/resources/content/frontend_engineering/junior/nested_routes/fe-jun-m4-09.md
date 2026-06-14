---
id: fe-jun-m4-09
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m4
moduleTitle: "Module 4: Routing and Navigation"
moduleGlyph: "🗺️"
moduleSortOrder: 4
topicSlug: nested_routes
topicTitle: "Nested Routes"
topicSortOrder: 3
lesson: index_routes
title: "Index Routes"
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
    - "Defines an index route as the default child when the parent path matches exactly"
    - "Knows the difference between Route index and Route path=''"
    - "Uses index routes to prevent blank layouts"
    - "Understands that index routes do NOT add to the URL"
  keywords: [index, default-child, blank-layout, exact-match, parent-path, fallback-child]
  modelAnswer: |
    An index route (Route index) is the default child rendered when the parent route's
    path matches exactly but no child path matches. It prevents the parent layout from
    showing a blank Outlet. Example: visiting /dashboard renders the index (Overview);
    visiting /dashboard/settings renders the Settings child. Index routes do not add
    a URL segment — they match the parent URL exactly.
guidedSteps:
  - id: fe-jun-m4-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What renders when a user visits /dashboard if there is an index route for Dashboard?
    inputConfig:
      options:
        - "A blank page — no child path is matched"
        - "The index route's element"
        - "A redirect to /dashboard/overview"
        - "A 404 page"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The index route's element"]
      rejectedFeedback: "Without an index route, visiting the parent path exactly (/dashboard) shows the parent layout with a blank Outlet — confusing. The index route fills the Outlet with default content when no more specific child path matches."
    hint: "Index route = default child for the parent path."
    reflectionPrompt: "Every layout route should have an index route or an explicit redirect. A blank Outlet (no child matched, no index) is a UX bug — the user sees the shell with empty content and no indication of what to do."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which is the correct syntax for an index route?"
    options:
      - "<Route path='' element={<Overview />} />"
      - "<Route index element={<Overview />} />"
      - "<Route path='/' element={<Overview />} />"
      - "<Route default element={<Overview />} />"
    correctIndex: 1
    feedback: "Route index (no path prop) is the React Router v6 syntax for an index route. It matches the parent URL exactly and doesn't add a URL segment. path='' would match the parent path but with a trailing slash difference."

retrieval:
  recall: "When is an index route rendered vs a regular child route?"
  explain: "Why should every layout route have an index route?"
  mistakeId:
    code: "Layout /dashboard shows blank Outlet when user visits /dashboard directly"
    answer: "Add <Route index element={<DashboardOverview />} /> as a child of the dashboard Route. Now /dashboard renders the overview; /dashboard/settings renders settings."
---

# Hook

Index routes solve the blank layout problem. Without them, visiting a parent path exactly shows a shell with an empty slot — confusing and broken. With them, there's always something meaningful to show.

# Lore Introduction

*"The Academy's main reading room,"* says Master Aelindra, *"shows a welcome display when first entered, before any shelf is selected. Without a default display, visitors stand in an empty room uncertain what to do."*

# Core Learning

## Concept Introduction

```jsx
<Routes>
  <Route path="/dashboard" element={<DashboardLayout />}>
    {/* Without index route, visiting /dashboard shows blank Outlet */}
    <Route index element={<DashboardOverview />} />  {/* /dashboard */}
    <Route path="users"    element={<Users />} />    {/* /dashboard/users */}
    <Route path="settings" element={<Settings />} /> {/* /dashboard/settings */}
  </Route>
</Routes>
```

**Index vs specific child:**
- User visits `/dashboard` → index route renders (Overview)
- User visits `/dashboard/users` → Users child renders
- User visits `/dashboard/unknown` → no match (404 if wildcard exists)

**Common patterns:**
```jsx
// Redirect from parent to default child (alternative to index)
<Route path="/dashboard" element={<Navigate to="overview" replace />} />
<Route path="/dashboard/overview" element={<Overview />} />
```

## Common Mistakes

- **Using `path=""` instead of the `index` prop**: An index route is declared with `<Route index element={<Overview />} />`, not `<Route path="" ...>`. Using an empty string path does not correctly create an index route.
- **Adding a path segment to an index route**: `<Route index path="overview" ...>` is invalid — index routes match the parent URL exactly and cannot have a path segment.
- **Expecting the index route to render when a child is active**: The index route renders only when the parent path is matched exactly. Visiting `/dashboard/users` renders `<Users />`, not the index route — they are mutually exclusive.
- **Omitting the index route and leaving a blank Outlet**: If the parent layout is visited directly (e.g., `/dashboard`) and there is no index route, the `<Outlet />` renders nothing. Always add an index route for layout routes.

## Mini Summary
- ✔ `<Route index>` = default child for the exact parent path
- ✔ Does not add a URL segment — matches parent URL
- ✔ Prevents blank Outlet when parent path is visited
- ✔ Alternative: Navigate component to redirect to default child

# Solo Practice Quest

Add index routes to: /products (shows featured products), /account (shows profile summary), /admin (shows stats dashboard). Verify that visiting the parent path directly shows meaningful content.

# Integration

**Psychology — Default and Choice Architecture:** Index routes implement a good default in choice architecture (Thaler & Sunstein's Nudge Theory). A good default reduces the decision burden: the user arrives at the parent route and immediately sees useful content without having to choose a child. Blank layouts force an unnecessary decision.

# Lore Conclusion

*"Always have a welcome for the first step through the door. The empty room is never acceptable."*

---
