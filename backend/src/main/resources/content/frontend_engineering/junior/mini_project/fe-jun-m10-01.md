---
id: fe-jun-m10-01
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m10
moduleTitle: "Module 10: Junior Project"
moduleGlyph: "🏗️"
moduleSortOrder: 10
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
lesson: the_guild_dashboard
title: "The Guild Dashboard"
sortOrder: 1
difficulty: 5
estimatedMinutes: 180
xpReward: 300
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - fe-jun-m1-01
  - fe-jun-m2-01
  - fe-jun-m3-01
  - fe-jun-m4-01
  - fe-jun-m5-01
  - fe-jun-m6-01
  - fe-jun-m7-01
  - fe-jun-m8-01
  - fe-jun-m9-01
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "React components are used correctly with appropriate separation of concerns"
    - "State is managed with useState/useEffect; props flow down correctly"
    - "API data is fetched via a service module (not raw fetch in components)"
    - "Loading, success, and error states are handled and rendered"
    - "Tailwind CSS is used for responsive, consistent styling"
    - "At least three component tests pass using React Testing Library"
    - "ESLint passes with no errors; Prettier formatting is consistent"
    - "Written reflection addresses architectural decisions and what was hardest"
  keywords: [component, state, fetch, service, loading, error, Tailwind, test, RTL, ESLint, Prettier, responsive]
  modelAnswer: |
    A complete Guild Dashboard fetches and displays data from a public API, handles all three async states, uses a service module for API calls, applies Tailwind for responsive styling, includes meaningful component tests, and passes lint/format checks. The reflection demonstrates understanding of the component architecture and why each decision was made.
---

# Hook

You have learned components, state, routing, API integration, testing, Tailwind, and tooling as separate topics.

Now you assemble them into a complete application.

The Guild Dashboard is not a tutorial — there is no step-by-step walkthrough. You decide the architecture. You write the tests. You make it work.

> Before you start: draw your component tree on paper. What fetches data? What renders it? What handles state?

# Lore Introduction

The Guild Master sets a commission before you.

*"The guild requires a dashboard — a place where apprentices can view active quests, see which masters are available, and track the guild's recent activity."*

She slides three scrolls across the table: a list of API endpoints, a rough wireframe, and a set of requirements.

*"We do not want clever code. We want correct code. Code that handles errors, that is tested, that is readable. Code that another apprentice could pick up tomorrow and understand."*

She stands. *"The rest is yours. Earn your Junior badge."*

# Project Brief

Build a **React application** called the **Guild Dashboard** that displays data from a public REST API.

---

## Recommended API

Use the [JSONPlaceholder](https://jsonplaceholder.typicode.com/) public API — no authentication required:

| Endpoint | Data |
|---|---|
| `GET /users` | List of guild members |
| `GET /posts` | Recent activity/announcements |
| `GET /todos` | Active quests |
| `GET /users/{id}` | Individual member profile |

---

## Application Requirements

### Pages / Views

| View | Description |
|---|---|
| **Dashboard** | Overview: member count, recent activity summary |
| **Members** | Paginated list of guild members with name, email, and company |
| **Member Detail** | Individual member's profile and their associated posts |
| **Quests** | List of todos/quests filterable by completion status |

### Technical Requirements

| Requirement | Details |
|---|---|
| **Framework** | React 18+ with TypeScript |
| **Styling** | Tailwind CSS — responsive, consistent |
| **Routing** | React Router (at least 3 routes) |
| **API calls** | Via a service module — no fetch() in components |
| **State** | useState + useEffect for async data |
| **Loading states** | Spinner or skeleton while fetching |
| **Error states** | Error message when fetch fails |
| **Component tests** | At least 3 meaningful tests with React Testing Library |
| **Code quality** | ESLint passes, Prettier formatted, TypeScript strict |
| **Build** | `npm run build` completes without errors |

---

## Acceptance Criteria

- [ ] At least 3 routes work with React Router
- [ ] Data is fetched from JSONPlaceholder via a service module
- [ ] Loading spinner/skeleton appears during fetches
- [ ] Error message appears when a fetch fails (test by disabling network in DevTools)
- [ ] Members list is responsive — works on mobile and desktop
- [ ] At least one filter or search interaction works (e.g., filter quests by status)
- [ ] 3+ component tests pass (test a card, a filter, or a loading state)
- [ ] `npm run lint` passes with no errors
- [ ] `npm run build` completes successfully
- [ ] TypeScript has no errors (`tsc --noEmit`)

---

## Architecture Scaffolding

```
src/
├── components/           # Reusable UI components (Button, Card, Spinner)
├── features/
│   ├── members/          # Member list, member detail
│   ├── quests/           # Quest list with filter
│   └── dashboard/        # Overview page
├── services/
│   └── guildService.ts   # All API calls
├── types/
│   └── index.ts          # TypeScript interfaces (Member, Post, Quest)
├── App.tsx               # Routes
└── main.tsx              # Entry point
```

**Type example:**
```ts
// types/index.ts
export interface Member {
  id: number;
  name: string;
  email: string;
  phone: string;
  company: { name: string };
}

export interface Quest {
  id: number;
  title: string;
  completed: boolean;
  userId: number;
}
```

**Service example:**
```ts
// services/guildService.ts
const BASE = 'https://jsonplaceholder.typicode.com';

export const guildService = {
  getMembers: (): Promise<Member[]> =>
    fetch(`${BASE}/users`).then(r => r.json()),

  getMember: (id: number): Promise<Member> =>
    fetch(`${BASE}/users/${id}`).then(r => r.json()),

  getQuests: (): Promise<Quest[]> =>
    fetch(`${BASE}/todos`).then(r => r.json()),
};
```

---

## Reflection Prompt

After completing the dashboard, write **5–7 sentences** addressing:

1. What component structure did you choose, and why?
2. Where does state live in your app, and why there?
3. What was the hardest part of wiring the pieces together?
4. How did you test your components — what did you test and why?
5. If you were adding this to a production system, what would you add next?

---

# Integration

**Psychology — Cognitive Load in Application Design**

Building a complete application from scratch exercises your working memory differently from exercises that guide each step. The project forces you to hold multiple concerns simultaneously: component structure, data flow, API design, styling, and testing — all while making decisions that affect each other. This is the cognitive challenge of real software development. Sweller's Cognitive Load Theory distinguishes between intrinsic load (the inherent complexity of the task) and extraneous load (complexity caused by poor learning design). By this point in the curriculum, many React, API, and testing patterns are familiar — they have become chunks, processed as single units. This project assembles those chunks into a coherent whole, practising the integration skill that separates junior engineers from those who can work independently.

**Mathematics — Composition of Functions**

The application is, at its core, a composition of transformations: raw API data → typed interfaces → UI components → rendered DOM → user interaction → state change → re-render. Each transformation is a function. The service transforms HTTP responses into domain types. Components transform domain types into JSX. React transforms JSX into DOM operations. The data flows through this pipeline as a series of function applications. Thinking of your application this way — as a composition of pure transformations interrupted by side effects (fetch, setState) — helps reason about where bugs can occur and how to test each layer in isolation.

# Lore Conclusion

The Guild Master reviews the completed dashboard.

She checks the member list — responsive, loads gracefully. She disables her network — an error message appears, not a crash. She checks the test results — green. She reviews the code — no lint errors, consistent formatting, clear component boundaries.

*"Tests: present. Errors: handled. Layers: separated. Tooling: correct."*

She affixes the Junior Frontend Engineer seal to your work.

*"You have not just learned the pieces. You have assembled them. The next tier teaches you to question whether the pieces themselves are the right ones."*

The Guild Dashboard is live.

---
