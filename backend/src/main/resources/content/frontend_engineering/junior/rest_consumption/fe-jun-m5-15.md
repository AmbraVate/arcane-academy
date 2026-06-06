---
id: fe-jun-m5-15
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: rest_consumption
topicTitle: "REST Consumption"
topicSortOrder: 5
lesson: api_abstraction
title: "API Abstraction"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-13, fe-jun-m5-14]
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains why calling fetch directly from components is a problem"
    - "Describes what an API service module is and what it contains"
    - "Explains how abstraction makes components easier to test"
    - "Identifies what the component should and shouldn't know about the API"
  keywords: [abstraction, service, module, separation, concerns, fetch, component, test, mock]
  modelAnswer: |
    Calling fetch directly in components couples the component to the network layer — if the API URL changes, the authentication method changes, or you want to test the component in isolation, you must change the component itself. An API service module extracts all fetch calls into one place, exposing simple functions (getUsers(), createTask()) that the component calls. The component knows what data it needs, not how to get it. This makes components easier to test (mock the service, not the network), easier to change (update the service, not every component), and easier to read (no fetch boilerplate mixed into UI logic).
guidedSteps:
  - id: fe-jun-m5-15-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A component directly calls `fetch('/api/users')` inside useEffect. The API URL changes to `/v2/api/users`. What do you have to do?"
    inputConfig:
      options:
        - "Update only the API service module"
        - "Find and update every component that calls this URL"
        - "Nothing — fetch handles URL changes automatically"
        - "Update the browser cache"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Find and update every component that calls this URL"]
      rejectedFeedback: "When fetch is called directly in components, the URL is scattered across your codebase. Every component that calls that endpoint must be found and updated — a fragile, error-prone process. A service module centralises this to one change."
    hint: "If the URL is hardcoded in three different components, you need to find all three."
    reflectionPrompt: "This is the DRY principle applied to API calls. Every time you repeat a detail in multiple places, you create a future maintenance burden."
  - id: fe-jun-m5-15-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Describe what a `userService.js` module should export and what it should NOT export. What belongs inside it, and what should stay in the component?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [fetch, URL, header, component, data, request, response]
      rejectedFeedback: "The service module should export functions like `getUser(id)`, `createUser(data)`, `updateUser(id, data)`. It handles fetch, URLs, headers, and response parsing. The component should only call these functions and handle the returned data — it should never know about HTTP methods, headers, or raw fetch."
    hint: "Think about what knowledge belongs to 'how to talk to the API' vs 'what to show the user'."
    reflectionPrompt: "Each module should have one reason to change. The service changes when the API changes. The component changes when the UI changes. If they're the same file, both reasons cause you to touch the same code."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the primary reason to extract API calls into a service module?"
    options:
      - "To make the code run faster"
      - "To centralise network logic so components don't need to know about HTTP details"
      - "To avoid using async/await"
      - "To reduce the number of files in the project"
    correctIndex: 1
    feedback: "Service modules separate the 'how to fetch' from the 'what to display'. Components gain a simpler interface — call a function, get data — without dealing with URLs, headers, or fetch boilerplate."
retrieval:
  recall: "Name three things an API service module should handle that a component should not."
  explain: "How does extracting API calls into a service module make testing easier?"
  mistakeId:
    code: |
      // UserProfile.jsx
      useEffect(() => {
        fetch(`https://api.example.com/users/${id}`, {
          headers: { Authorization: `Bearer ${token}` }
        })
          .then(r => r.json())
          .then(setUser);
      }, [id]);
    answer: "The component knows the API base URL, the auth header format, and the response shape — all things that can change independently of the UI. Extract this into `userService.getUser(id)`. The component asks 'give me user data for this id'; the service decides how to get it. When the auth scheme changes from Bearer to API key, only the service needs updating — none of the components."
---

# Hook

You have 12 components, each with a `useEffect` that calls `fetch('/api/tasks')`. Then the backend team renames the endpoint. You spend an afternoon hunting through 12 files, fixing the same URL, hoping you didn't miss one.

This is the cost of no abstraction. A single service module would have meant one change, one minute.

# Lore Introduction

*"A skilled enchanter,"* Master Aldric explains, *"does not re-cast the summoning spell every time they need a familiar. They craft a binding — a stable contract that invokes the spell once, reliably, whenever called."*

He gestures toward your sprawling spellbook, fetch calls scattered across every scroll.

*"You have copied the summoning incantation into fifty scrolls. When the incantation changes, you must find every copy. Instead: craft the binding once. Let every scroll invoke the binding."*

# Core Learning

## Concept Introduction

An **API service module** is a plain JavaScript/TypeScript file that contains all your fetch calls for a given resource. Components import functions from it — they never call fetch directly.

| Without abstraction | With abstraction |
|---|---|
| `fetch('/api/users')` in every component | `userService.getUsers()` in every component |
| API URL scattered across files | URL in one place |
| Auth headers duplicated | Auth headers in one place |
| Impossible to test in isolation | Mock the service, test the component |

## Why It Matters

**Separation of concerns:** Components are responsible for rendering and user interaction. They should not know whether data comes from a REST API, GraphQL, localStorage, or a mock. The service is the translation layer.

**Testability:** When components call fetch, tests must mock the browser's fetch API — awkward and brittle. When components call a service function, tests mock the service — simple and reliable.

**Change isolation:** The API changes in one place; components never notice.

## Worked Example

**Without abstraction:**
```jsx
// TaskList.jsx — knows too much about the API
useEffect(() => {
  fetch('https://api.arcane.academy/v1/tasks', {
    headers: { Authorization: `Bearer ${token}` }
  })
    .then(res => res.json())
    .then(setTasks);
}, []);
```

**With abstraction:**
```js
// services/taskService.js
const BASE = 'https://api.arcane.academy/v1';

function getAuthHeaders() {
  return { Authorization: `Bearer ${localStorage.getItem('token')}` };
}

export const taskService = {
  getTasks: () =>
    fetch(`${BASE}/tasks`, { headers: getAuthHeaders() })
      .then(res => res.json()),

  createTask: (data) =>
    fetch(`${BASE}/tasks`, {
      method: 'POST',
      headers: { ...getAuthHeaders(), 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(res => res.json()),
};
```

```jsx
// TaskList.jsx — knows nothing about the API
useEffect(() => {
  taskService.getTasks().then(setTasks);
}, []);
```

Now if the API moves to v2 or switches auth schemes: one file changes.

## Common Mistakes

- **Mixing UI logic into the service.** The service returns data; the component decides what to render. Never `document.querySelector` inside a service.
- **One giant service for everything.** Split by resource: `userService`, `taskService`, `authService`. Each service has one reason to change.
- **Not handling errors in the service.** Throw meaningful errors from the service so components can display appropriate messages.
- **Exporting the raw fetch call.** Export a named function with a clear purpose, not `export const fetchUsers = () => fetch(...)` — callers shouldn't know it uses fetch.

## Mini Summary

- Components should not call fetch directly — extract to service modules
- Service modules handle URLs, headers, request formatting, and response parsing
- Components call service functions and receive plain data
- This makes components testable (mock the service), maintainable (one URL to update), and readable (no HTTP boilerplate in JSX files)

# Guided Practice Quest

Work through the two guided steps to check your understanding of when abstraction helps and what belongs in each layer.

# Solo Practice Quest

Take a component that calls `fetch` directly (real or invented). Refactor it: create a service module for the resource, move all fetch logic there, and update the component to call the service. Write 3–4 sentences explaining what changed and how it would help if the API URL changed tomorrow.

# Integration

**Philosophy — Abstraction as Intellectual Economy**

Abstraction is the philosophical act of hiding irrelevant detail to reveal essential structure. Whitehead argued that civilisation advances by extending the operations we can perform without thinking about them. An API service module is exactly this: you extend the range of operations a component can perform (fetch users, create tasks) without thinking about how they happen. This frees component authors to focus on what matters: what to show the user. The cost of abstraction is indirection — you must look in two places instead of one. The benefit is that each place has a single, clear responsibility.

# Lore Conclusion

*"The binding is set,"* Aldric says, watching you extract the last fetch call into the service module. *"Every scroll now calls the binding. When the summoning incantation changes — and it will — you change the binding once. Your scrolls remain untouched."*

The module glows faintly. One source of truth.

---
