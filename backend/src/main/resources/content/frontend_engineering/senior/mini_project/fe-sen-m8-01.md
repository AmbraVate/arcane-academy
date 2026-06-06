---
id: fe-sen-m8-01
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m8
moduleTitle: "Module 8: Senior Project"
moduleGlyph: "🏗️"
moduleSortOrder: 8
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
lesson: the_guild_platform
title: "The Guild Platform"
sortOrder: 1
difficulty: 5
estimatedMinutes: 240
xpReward: 400
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - fe-sen-m1-01
  - fe-sen-m2-01
  - fe-sen-m3-01
  - fe-sen-m4-01
  - fe-sen-m5-01
  - fe-sen-m6-01
  - fe-sen-m7-01
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Application uses feature-based architecture (features/, shared/, services/ folders)"
    - "State is managed with Zustand or Redux Toolkit — justified in the reflection"
    - "Route-based code splitting with React.lazy + Suspense is implemented"
    - "WCAG AA compliance: keyboard navigation works, contrast passes, form labels are correct"
    - "Error tracking integrated (Sentry or equivalent) with source maps configured"
    - "At least 3 component tests pass with React Testing Library, including one async test"
    - "Core Web Vitals: LCP < 2.5s, CLS < 0.1 on a Lighthouse audit"
    - "Written reflection addresses each module's contribution to the architecture"
  keywords: [feature-based, Zustand, Redux, lazy, Suspense, WCAG, keyboard, Sentry, source map, test, LCP, CLS, architecture, reflection]
  modelAnswer: |
    A complete Guild Platform demonstrates: feature-based architecture, advanced state management with justification, performance optimisation through code splitting, WCAG AA accessibility, integrated error tracking, meaningful tests, and performance within Core Web Vitals targets. The reflection shows genuine understanding of why each architectural decision was made, not just what was done.
---

# Hook

You have studied frontend architecture. Advanced state management. Performance engineering. Security. Design systems. Accessibility. Observability.

Each module was a discipline. The Senior Project is where they become an architecture.

> Before you start: write out your architecture decisions. Why Zustand over Redux? Where are the feature boundaries? What goes in shared? You should be able to justify every structural choice.

# Lore Introduction

The Guild of Senior Engineers presents a commission of real consequence.

*"The Guild needs a platform,"* says the Senior Guild Master. *"Not a prototype — a platform. It must handle thousands of users. It must be maintainable by engineers who don't know your history. It must be accessible to every guild member, regardless of how they navigate."*

She slides a detailed specification across the table.

*"We will review the architecture, the performance, the accessibility, and the observability. We will also review what you chose NOT to include — and whether those omissions were deliberate and justified."*

*"This is not a feature sprint. This is an engineering decision."*

# Project Brief

Build a **Guild Management Platform** — a production-quality React application for managing guild members, projects, and resources.

---

## Domain Model

```
Member
├── id: string
├── name: string
├── role: 'apprentice' | 'journeyman' | 'master'
├── joinedAt: Date
└── skills: string[]

Project
├── id: string
├── title: string
├── status: 'planning' | 'active' | 'completed'
├── leadId: string (Member.id)
└── members: string[] (Member.id[])

Resource
├── id: string
├── name: string
├── type: 'equipment' | 'material' | 'space'
└── available: boolean
```

---

## Feature Requirements

| Feature | Description |
|---|---|
| **Member Directory** | Searchable, filterable list of members with profiles |
| **Project Board** | Kanban or list view of projects by status |
| **Project Detail** | Member assignments, status updates, activity log |
| **Resource Booking** | View and book available resources |
| **Admin Panel** | Manage members and projects (admin role only) |

---

## Technical Requirements

| Requirement | Standard |
|---|---|
| **Architecture** | Feature-based folders (`features/`, `shared/`, `services/`) |
| **State management** | Zustand or Redux Toolkit — justify the choice |
| **Code splitting** | Route-based with React.lazy + Suspense |
| **Performance** | Lighthouse LCP < 2.5s, CLS < 0.1 |
| **Accessibility** | WCAG AA: keyboard navigation, visible focus, correct ARIA |
| **Security** | No PII in localStorage; CSRF-safe API calls |
| **Error tracking** | Sentry (or equivalent) with source maps |
| **Design system** | At least 5 shared components (Button, Card, Badge, Input, Modal) |
| **Testing** | 3+ component tests including at least 1 async test |
| **TypeScript** | Strict mode, no `any`, typed API responses |
| **Build** | `npm run build` succeeds, bundle < 300KB gzipped initial |

---

## Acceptance Criteria

- [ ] Feature-based folder structure (not layer-based)
- [ ] Admin Panel loads only when navigated to (code-split)
- [ ] Member directory: keyboard navigation works completely (test with Tab only)
- [ ] At least one modal uses focus trapping
- [ ] Sentry DSN configured; source maps uploaded for a simulated release
- [ ] Lighthouse accessibility score > 90
- [ ] `npm run build` completes; `npm run lint` and `tsc --noEmit` pass with 0 errors
- [ ] 3+ RTL tests pass (include at least 1 with findBy for async)
- [ ] Written architectural reflection (minimum 300 words)

---

## Architecture Scaffolding

```
src/
├── features/
│   ├── members/          # MemberList, MemberProfile, hooks, services
│   ├── projects/         # ProjectBoard, ProjectDetail, hooks, services
│   ├── resources/        # ResourceList, ResourceBooking
│   └── admin/            # Admin panel (lazy-loaded)
├── shared/
│   ├── components/       # Button, Card, Badge, Input, Modal, Spinner
│   ├── hooks/            # useAuth, useToast, usePagination
│   └── utils/            # formatDate, truncate, api helpers
├── services/
│   └── api.ts            # Base API client, auth headers, error handling
├── store/
│   └── index.ts          # Zustand store (or Redux slices)
├── App.tsx               # Routes with lazy loading
└── main.tsx              # Sentry init, app entry
```

**State management decision (justify in reflection):**
- **Zustand:** simpler API, good for moderate state complexity
- **Redux Toolkit:** more structure, better for complex shared state, excellent DevTools

---

## Performance Targets

```
Initial bundle: < 300KB gzipped
Admin panel chunk: loads separately
LCP: < 2.5s (Lighthouse Fast 4G simulation)
CLS: < 0.1
Lighthouse performance score: > 70
```

Run: `npm run build && npx serve dist` + Lighthouse

---

## Architectural Reflection (minimum 300 words)

After completing the platform, write a structured reflection addressing:

1. **Architecture decision**: Why feature-based over layer-based? What did you put in `shared/` and why?
2. **State management choice**: Why Zustand or Redux? What would have changed your choice?
3. **Performance trade-offs**: What did you code-split? What did you intentionally NOT split? Why?
4. **Accessibility decisions**: What was hardest to make accessible? How did you verify it?
5. **Observability**: What does your Sentry setup capture? What would an on-call engineer have to understand the system state?
6. **What you'd do next**: If you had another week, what architectural concern would you address first?

---

# Integration

**Psychology — Senior Engineering and Metacognition**

The architectural reflection is not an afterthought — it is the most senior part of the project. Junior engineers build features. Senior engineers reason about why they built them the way they did. Metacognition (thinking about thinking) distinguishes senior engineers: the ability to evaluate your own decisions, identify their trade-offs, and articulate when a different context would lead to different choices. The reflection requirement is a metacognitive exercise. A reflection that only describes what you did ("I used Zustand because it's simpler") is junior. A reflection that analyses the decision space ("I chose Zustand for this moderate-state application — but if we added real-time collaboration, the event-sourcing model of Redux would have provided a better audit trail") is senior.

**Mathematics — Architectural Fitness Functions**

Neil Ford's concept of architectural fitness functions applies mathematics to architecture governance. A fitness function is a measurable criterion for architectural quality: bundle size < 300KB, Lighthouse score > 70, test coverage > 60%, TypeScript strict mode. These are not aspirational goals — they are automated checks that verify the architecture remains fit for purpose as the codebase evolves. The Senior Project introduces fitness functions explicitly (Lighthouse targets, bundle size targets, lint passing, type checking). This is the beginning of thinking about how to prevent architectural decay — the inevitable drift of a codebase away from its intended structure without continuous verification.

# Lore Conclusion

The Senior Guild Master reviews the platform.

She checks the code structure — feature-based, clearly bounded. She runs `npm run build` — succeeds, bundle within budget. She runs Lighthouse — LCP 2.1s, CLS 0.04. She disconnects the mouse and navigates the member directory by keyboard — complete. She reads the architectural reflection — it justifies, not merely describes.

*"Architecture: intentional. Performance: within targets. Accessibility: verified. Observability: integrated. Reflection: honest about trade-offs."*

She affixes the Senior Frontend Engineer seal.

*"A senior engineer does not just build what works. They build what can be understood, extended, and improved by engineers who come after them. You have done that. The Lead tier teaches you to guide others in doing the same."*

---
