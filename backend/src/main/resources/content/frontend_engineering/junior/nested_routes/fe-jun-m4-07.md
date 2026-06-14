---
id: fe-jun-m4-07
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
lesson: nested_route_structure
title: "Nested Route Structure"
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
    - "Nests Route components to create hierarchical URL structures"
    - "Uses Outlet to render child route content inside a parent layout"
    - "Understands that child route paths are relative to parent paths"
    - "Creates layout routes (parent with no element, only children)"
  keywords: [nested, Outlet, child-route, parent-route, layout, relative-path, hierarchy]
  modelAnswer: |
    Nested routes in React Router v6 create hierarchical URL/component structures.
    A parent Route renders its Outlet where child routes should appear. Child paths are
    relative to the parent: a child path="profile" inside parent path="/dashboard" matches
    /dashboard/profile. Layout routes have no element themselves — they just wrap children
    with a shared layout (nav, sidebar) and render their Outlet.
guidedSteps:
  - id: fe-jun-m4-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does `<Outlet />` render inside a parent route component?
    inputConfig:
      options:
        - "The parent route's children prop"
        - "The matched child route's element"
        - "A loading spinner while the child loads"
        - "The 404 page if no child matches"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The matched child route's element"]
      rejectedFeedback: "Outlet is a placeholder that renders whichever child route currently matches the URL. Parent renders its layout + Outlet. The Outlet is replaced by the matching child's element."
    hint: "Think of Outlet as a slot where child route content appears."
    reflectionPrompt: "Outlet is what makes shared layouts work. The parent renders navigation, header, sidebar, and an Outlet. The Outlet changes based on which child route is active — the rest of the layout stays constant."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A Route path='/users' has a child Route path=':id'. What URL does the child match?"
    options:
      - "/id"
      - "/users/id"
      - "/users/:id (the literal string)"
      - "/users/42 (when :id is 42)"
    correctIndex: 3
    feedback: "/users/42, /users/123, /users/alice — any segment after /users. The child path is relative to the parent, so it matches /users + /:id = /users/42."

retrieval:
  recall: "Write nested routes for a dashboard: /dashboard (layout), /dashboard/overview, /dashboard/settings."
  explain: "What is a 'layout route' and how does it use Outlet?"
  mistakeId:
    code: "Child route path='/dashboard/profile' inside parent path='/dashboard'"
    answer: "Child paths are relative. Use path='profile' (without leading /dashboard). If you include the full path, it becomes an absolute path and nesting breaks."
---

# Hook

Nested routes create hierarchical URL and component structures — a shared layout wrapping different content pages. This pattern is the backbone of most dashboard applications.

# Lore Introduction

*"The Academy's wings share an entrance hall,"* says Master Aelindra, *"but each has its own chambers. The hall (parent) provides the context; the chambers (children) provide the specific content."*

# Core Learning

## Concept Introduction

```jsx
<Routes>
  {/* Layout route — renders navigation + Outlet */}
  <Route path="/dashboard" element={<DashboardLayout />}>
    <Route index     element={<Overview />} />       {/* /dashboard */}
    <Route path="settings" element={<Settings />} /> {/* /dashboard/settings */}
    <Route path="users"    element={<Users />} />    {/* /dashboard/users */}
    <Route path="users/:id" element={<UserDetail />} />{/* /dashboard/users/42 */}
  </Route>
</Routes>

// DashboardLayout renders Outlet where children appear
function DashboardLayout() {
  return (
    <div className="dashboard">
      <Sidebar />
      <main>
        <Outlet />  {/* ← child route renders here */}
      </main>
    </div>
  );
}
```

**index route** = the default child when parent path is matched exactly.

## Common Mistakes

- **Using absolute paths for child routes**: Child route paths should be relative (e.g., `path="settings"` not `path="/dashboard/settings"`). Absolute paths in child routes break the nesting hierarchy.
- **Forgetting `<Outlet />` in the parent component**: Without `<Outlet />`, nested routes are matched in the router config but nothing renders. The parent component must include `<Outlet />` at the position where the child should appear.
- **Nesting a route that should be at the top level**: Not every related route needs to be nested. Only routes that share the parent's layout (sidebar, header) should be nested — routes with completely different layouts should be top-level.
- **Visiting the parent path without an index route**: Without a `<Route index>`, visiting `/dashboard` renders the `DashboardLayout` with a blank `<Outlet />`. Always add an index route for layout routes.

## Mini Summary
- ✔ Nest `<Route>` inside `<Route>` for hierarchical routing
- ✔ `<Outlet />` in the parent renders the matching child
- ✔ Child paths are relative to the parent
- ✔ `index` route = default child for the parent path

# Solo Practice Quest

Build a settings layout (/settings) with nested routes: /settings/profile, /settings/security, /settings/notifications. The layout has a side nav for switching; the Outlet renders the active settings page.

# Integration

**Mathematics — Tree Structures:** Nested routes form a tree where each node is a URL segment. Traversal from root to leaf gives the full URL path. Component rendering mirrors the tree — each component renders its children via Outlet, composing the full UI from root to leaf.

# Lore Conclusion

*"Every chamber is part of a wing, every wing part of the Academy. Nest your routes as you nest your spaces."*

---
