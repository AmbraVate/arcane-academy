---
id: fe-jun-m4-04
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
lesson: setting_up_routes
title: "Setting Up Routes"
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
    - "Wraps the app in BrowserRouter"
    - "Defines routes with Routes and Route components"
    - "Matches paths correctly including exact matching"
    - "Provides a wildcard 404 route"
  keywords: [BrowserRouter, Routes, Route, path, element, wildcard, 404, exact, createBrowserRouter]
  modelAnswer: |
    React Router v6: wrap in BrowserRouter (or use createBrowserRouter for data APIs).
    Define routes inside Routes using Route path and element. Routes matches the most
    specific path automatically. Use path="*" for 404 — it matches anything not matched
    by other routes. Route path="/users/:id" creates a URL parameter accessible via useParams().
guidedSteps:
  - id: fe-jun-m4-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In React Router v6, `<Route path="/about" element={<About />} />` renders About when:
    inputConfig:
      options:
        - "The URL starts with /about (prefix match)"
        - "The URL is exactly /about"
        - "Any URL contains 'about'"
        - "The user clicks a link labelled 'About'"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The URL is exactly /about"]
      rejectedFeedback: "React Router v6 matches exactly by default. /about matches /about only, not /about/team. For nested paths, use Outlet with child routes. This is different from v5 which required exact prop."
    hint: "React Router v6 changed the matching behaviour from v5."
    reflectionPrompt: "React Router v6's exact-by-default is a significant improvement — it eliminates the common v5 bug where /users matched /users/profile unexpectedly. Nesting in v6 is explicit through child Routes and Outlet."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which route catches all unmatched URLs to show a 404 page?"
    options:
      - "<Route path='/404' element={<NotFound />} />"
      - "<Route path='*' element={<NotFound />} />"
      - "<Route path='any' element={<NotFound />} />"
      - "React Router shows a default 404 automatically"
    correctIndex: 1
    feedback: "path='*' is a wildcard — it matches any URL not matched by earlier routes. Place it last inside Routes. React Router processes routes in order and uses the first match — wildcard at the end catches everything remaining."

retrieval:
  recall: "Write the complete router setup: BrowserRouter wrapping App, Routes with Home (/), About (/about), and a 404 catch-all."
  explain: "Why should the 404 wildcard route be placed last in the Routes list?"
  mistakeId:
    code: "<Route path='*' element={<NotFound />} /> placed first in Routes"
    answer: "Wildcard matches everything, so every URL shows NotFound. Always place wildcard last."
---

# Hook

React Router is the standard routing library for React SPAs. Its API is small but its implications are significant — URL structure is part of your application architecture.

# Lore Introduction

*"The Academy directory,"* says Master Aelindra, *"maps every corridor by name. Enter a name, find the corridor. Enter an unknown name — the directory has a catch-all: 'destination not found, return to entrance.'"*

# Core Learning

## Concept Introduction

```jsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/"        element={<Home />} />
        <Route path="/about"   element={<About />} />
        <Route path="/users/:id" element={<UserProfile />} />
        <Route path="*"        element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

// Read URL parameter
function UserProfile() {
  const { id } = useParams();
  return <div>User: {id}</div>;
}
```

## Mini Summary
- ✔ BrowserRouter → Routes → Route (path + element)
- ✔ v6: exact match by default
- ✔ :paramName → read with useParams()
- ✔ path="*" → wildcard 404 route (must be last)

# Solo Practice Quest

Set up routing for a blog: home (/), posts list (/posts), individual post (/posts/:id), about (/about), 404 catch-all. Add navigation links between pages using Link.

# Integration

**Mathematics — Pattern Matching:** Route matching is pattern matching — a URL string is compared against route patterns. :id is a capture group (like a regex capture). path="*" is the universal match. React Router's matching algorithm is a simplified regex engine applied to URL paths.

# Lore Conclusion

*"Name your corridors precisely. Every unknown request deserves a clear 'not found' — not silence."*

---
