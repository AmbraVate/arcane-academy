---
id: fe-jun-m4-08
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
lesson: outlet
title: "Outlet"
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
    - "Uses Outlet correctly in a layout component"
    - "Passes context from parent to child via useOutletContext"
    - "Understands that Outlet location controls child rendering position"
    - "Combines multiple layouts using nested Outlets"
  keywords: [Outlet, useOutletContext, layout, position, context, parent-to-child, nested]
  modelAnswer: |
    Outlet renders in the parent component at the position it is placed — determining
    where child content appears within the parent layout. useOutletContext() passes
    data from parent to child without props drilling through route components.
    Multiple layouts can be nested: App layout (global nav) → Dashboard layout
    (sidebar) → Page content (Outlet of Outlet).
guidedSteps:
  - id: fe-jun-m4-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You place `<Outlet />` inside a `<main>` tag in the layout. Where does child route content appear?
    inputConfig:
      options:
        - "At the top of the page, before the layout"
        - "Inside the <main> tag — where you placed Outlet"
        - "In a separate browser window"
        - "After the layout, outside the component"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Inside the <main> tag — where you placed Outlet"]
      rejectedFeedback: "Outlet renders at its exact position in the parent component. Place it inside <main> → child content renders in the main area. Place it inside a <aside> → child content renders in the sidebar. The layout position is intentional."
    hint: "Outlet is a placeholder — it renders exactly where you put it."
    reflectionPrompt: "This is the power of Outlet: you control the layout in the parent, and child routes only provide their specific content. The parent never knows which child is rendering — it just exposes the slot."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How do you pass user data from a layout route to its child routes?"
    options:
      - "Use props on Route element"
      - "<Outlet context={user} /> in parent; useOutletContext() in child"
      - "Global state only"
      - "Child routes cannot access parent data"
    correctIndex: 1
    feedback: "<Outlet context={user} /> passes any value to the child. The child reads it with: const user = useOutletContext(). This is useful for passing fetched data (current user, organization) to all child routes without Context."

retrieval:
  recall: "Write a DashboardLayout that passes the current user to children via Outlet context."
  explain: "Why is the position of Outlet in a layout component significant?"
  mistakeId:
    code: "Expecting Outlet to always render at the bottom of the parent component"
    answer: "Outlet renders exactly where you place it. If you want content in the main area, place <Outlet /> inside <main>. Layout is fully controlled by the parent."
---

# Hook

Outlet is the slot mechanism of React Router — it makes layouts composable without the parent knowing what content it wraps. Understanding where to place it is understanding how layouts compose.

# Lore Introduction

*"The Academy's rooms have window slots,"* says Master Aelindra, *"through which different exhibits are displayed. The room stays the same; the exhibit changes. The slot position determines where the exhibit appears."*

# Core Learning

## Concept Introduction

```jsx
// Parent layout — Outlet can be anywhere
function AppLayout() {
  return (
    <>
      <Header />
      <div className="page-layout">
        <Sidebar />
        <main className="content">
          <Outlet context={{ user: currentUser }} />  {/* child renders HERE */}
        </main>
        <Footer />
      </div>
    </>
  );
}

// Child route — reads context from parent
function ProfilePage() {
  const { user } = useOutletContext();
  return <div>Welcome, {user.name}</div>;
}

// Nested layouts (two levels)
<Route element={<AppLayout />}>
  <Route path="dashboard" element={<DashboardLayout />}>
    {/* DashboardLayout has its own Outlet */}
    <Route path="analytics" element={<Analytics />} />
  </Route>
</Route>
```

## Mini Summary
- ✔ Outlet renders at its exact position in the parent
- ✔ `<Outlet context={data} />` passes data to children
- ✔ Children read with `useOutletContext()`
- ✔ Layouts can be nested — each with its own Outlet

# Solo Practice Quest

Build a multi-level layout: AppShell (global nav + Outlet) → DashboardLayout (sidebar + Outlet) → StatsPage. Pass the current user from AppShell through each level using Outlet context.

# Integration

**Mathematics — Slots and Templates:** Outlet implements the Template Method pattern from design — a template defines fixed structure with slots for variable content. The parent is the template; Outlet is the slot; child routes are the variable content. This is equivalent to HTML template literals (`${variable}`) at the component composition level.

# Lore Conclusion

*"The slot determines the position. The parent controls the frame; the child fills the canvas."*

---
