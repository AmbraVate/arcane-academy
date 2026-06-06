---
id: fe-sen-m1-01
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m1
moduleTitle: "Module 1: Frontend Architecture"
moduleGlyph: "🏛️"
moduleSortOrder: 1
topicSlug: feature_based_architecture
topicTitle: "Feature-Based Architecture"
topicSortOrder: 1
lesson: feature_based_architecture
title: "Feature-Based Architecture"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, systems_thinking]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Accurately describes feature-based folder structure and its benefits over layer-based organisation
    - Explains the co-location principle and why it reduces cognitive overhead for developers
    - Articulates the public API pattern per feature and why index.ts barrel files matter
    - Discusses module boundaries and how to handle cross-feature dependencies
    - Synthesises the tradeoff between feature locality and shared code reuse
    - Addresses when feature-based architecture is overkill vs essential
  keywords: [feature, co-location, module, boundary, ownership, cohesion, coupling, shared, barrel, index, public API]
  modelAnswer: |
    Feature-based architecture organises code by business domain rather than technical layer. Instead of folders like `/components`, `/hooks`, `/services`, you have `/features/checkout`, `/features/user-profile`, each containing all its own components, hooks, types, and services.

    The co-location principle means related code lives together. When a developer works on checkout, they open one folder and find everything they need — no mental map of which global folder holds checkout's API calls vs checkout's components. This reduces context-switching and makes deletions safe (delete the folder, nothing orphaned).

    Each feature exposes a public API via an `index.ts` barrel. Other features import only from `features/checkout` — never from `features/checkout/internal/PaymentForm`. This creates an enforceable boundary. ESLint rules (like `import/no-internal-modules`) can enforce it automatically.

    Cross-feature dependencies are handled through a `shared/` folder containing genuinely reusable primitives (Button, Modal, useDebounce). If two features need to share business logic, extract it to `shared/` deliberately — not by importing directly across feature boundaries.

    Feature-based architecture earns its complexity cost when: you have 5+ developers, distinct business domains with separate ownership, or features that need to be independently deletable. For a solo developer with 3 routes, layer-based is perfectly fine.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A team organises their React codebase as `/components`, `/hooks`, `/services`, `/types`. A developer working on the checkout flow has to touch files in all four folders for a single change. Which architectural problem does this illustrate?"
    options:
      - "Too many components in the components folder"
      - "Low cohesion — related code is scattered across technical layers"
      - "Too many hooks being used"
      - "The services folder is not needed"
    correctIndex: 1
    feedback: "Layer-based organisation groups code by *what it is* (a hook, a component) rather than *what it does* (checkout). The result is low cohesion — a single feature's code is spread across the codebase, requiring developers to hold a mental map of multiple folders."
  - type: SHORT_TEXT
    prompt: "In a feature-based structure, what is the purpose of an `index.ts` file at the root of a feature folder (e.g., `features/checkout/index.ts`)?"
    hint: "Think about what other features are allowed to import from checkout."
  - type: FILL_BLANK
    prompt: "Genuinely reusable code shared across multiple features should live in a ___ folder, not be imported directly across feature boundaries."
    answer: "shared"
    hint: "It's a single word for the folder at the same level as `/features`."
  - type: MULTIPLE_CHOICE
    prompt: "Your `features/orders` module needs access to a `useCurrentUser` hook that also lives in `features/auth`. What is the correct approach?"
    options:
      - "Import directly: `import { useCurrentUser } from '../auth/hooks/useCurrentUser'`"
      - "Copy the hook into the orders feature"
      - "Move `useCurrentUser` to `shared/` and import from there"
      - "Merge the auth and orders features into one"
    correctIndex: 2
    feedback: "Direct cross-feature imports create hidden coupling. If the auth feature is refactored or deleted, orders silently breaks. The correct approach is to extract genuinely shared code to `shared/` — a deliberate, visible dependency — and import from there."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which folder structure best represents feature-based architecture?"
    options:
      - "`/components`, `/hooks`, `/services`, `/utils`"
      - "`/features/auth`, `/features/checkout`, `/features/dashboard`, `/shared`"
      - "`/pages`, `/layouts`, `/widgets`"
      - "`/containers`, `/presentational`, `/hocs`"
    correctIndex: 1
    feedback: "Feature-based architecture groups by business domain (auth, checkout, dashboard), not by technical layer. The `shared/` folder holds genuinely cross-cutting primitives."
  - type: MULTIPLE_CHOICE
    question: "What does it mean for a feature to have a 'public API'?"
    options:
      - "The feature has REST endpoints exposed to the internet"
      - "All internal files in the feature are importable by anyone"
      - "Other code only imports from the feature's `index.ts`, not from internal files"
      - "The feature must be open-source"
    correctIndex: 2
    feedback: "A feature's public API is defined by its `index.ts` barrel file. Only what is explicitly exported there is considered stable and importable. Internal implementation files are private and may change without notice."
retrieval:
  recall: "Name the three key benefits of co-locating a feature's code in one folder."
  explain: "Explain why importing directly from another feature's internal files (e.g., `features/auth/hooks/useCurrentUser`) creates architectural risk."
  mistakeId:
    code: |
      // features/orders/OrderList.tsx
      import { useCurrentUser } from '../auth/hooks/useCurrentUser';
      import { formatCurrency } from '../checkout/utils/formatCurrency';
      import { Button } from '../ui/Button';

      export function OrderList() { ... }
    answer: "Two cross-feature internal imports violate module boundaries. `useCurrentUser` should come from `shared/auth` or the auth feature's public `index.ts`. `formatCurrency` should live in `shared/formatting` if multiple features need it, not be imported from checkout's internals. Only the Button import (from shared UI) is acceptable assuming Button is a shared primitive."
---

# Hook

You're onboarding onto a codebase that's been running for three years. You open the repo and see: `/components` with 340 files, `/hooks` with 89 files, `/services` with 67 files, and `/types` with 201 files. You need to find and modify the checkout payment form. Forty minutes later, you've found 14 files spread across all four folders, you're not sure if you've found them all, and you're afraid to delete anything in case it's used elsewhere.

This is what low-cohesion, layer-based architecture does at scale.

# Lore Introduction

In the higher halls of the Arcane Academy, the Senior Architects debate not just how to build — but how to *organise* what they build. The most heated debates are not about algorithms but about folder structure. The old masters grouped their spells by *type* (offensive, defensive, restorative). The newer generation groups by *purpose* (combat-readiness, patient-recovery, fortress-maintenance). Both work for a single practitioner. At scale, only one survives.

# Core Learning

## Concept Introduction

Feature-based architecture organises code by **business domain** rather than technical role. Each feature (checkout, user-profile, notifications) is a self-contained module with its own components, hooks, services, types, and tests.

```
src/
  features/
    auth/
      components/
        LoginForm.tsx
        ProtectedRoute.tsx
      hooks/
        useCurrentUser.ts
        useLogin.ts
      services/
        authService.ts
      types/
        auth.types.ts
      index.ts          <- public API
    checkout/
      components/
        CheckoutForm.tsx
        OrderSummary.tsx
      hooks/
        useCheckout.ts
      services/
        paymentService.ts
      types/
        checkout.types.ts
      index.ts
  shared/
    components/
      Button/
      Modal/
    hooks/
      useDebounce.ts
    utils/
      formatting.ts
```

The `index.ts` at each feature root is the **public API**:

```typescript
// features/auth/index.ts
export { LoginForm } from './components/LoginForm';
export { ProtectedRoute } from './components/ProtectedRoute';
export { useCurrentUser } from './hooks/useCurrentUser';
export type { User, AuthState } from './types/auth.types';
// useLogin is NOT exported — it's an implementation detail
```

Other code imports only from the barrel:

```typescript
// Correct
import { useCurrentUser, ProtectedRoute } from '@/features/auth';

// Wrong — violates the boundary
import { useCurrentUser } from '@/features/auth/hooks/useCurrentUser';
```

## Why It Matters

**Cognitive locality**: when working on checkout, you look in one place. The entire mental model of a feature fits in one folder.

**Safe deletion**: if a feature is removed, delete its folder. Nothing outside the feature imports from its internals, so the build will tell you exactly what external dependencies exist via the `index.ts` exports.

**Team ownership**: feature boundaries map naturally to team ownership. The payments team owns `/features/checkout`. The identity team owns `/features/auth`. No accidental coupling.

**Dependency clarity**: cross-feature dependencies are explicit. If orders imports from auth's public API, that dependency is visible and intentional.

## Worked Examples

**Example 1: Enforcing boundaries with ESLint**

```json
// .eslintrc.json
{
  "rules": {
    "import/no-restricted-paths": [
      "error",
      {
        "zones": [
          {
            "target": "./src/features/orders",
            "from": "./src/features/auth",
            "except": ["./index.ts"],
            "message": "Import from features/auth public API only (index.ts)"
          }
        ]
      }
    ]
  }
}
```

This rule makes boundary violations a CI failure — not just a code review comment.

**Example 2: The shared/ folder decision**

A `formatCurrency` utility is used by both `checkout` and `invoices`. Where does it live?

```typescript
// shared/utils/currency.ts
export function formatCurrency(amount: number, currency: string): string {
  return new Intl.NumberFormat('en-GB', {
    style: 'currency',
    currency,
  }).format(amount);
}
```

Both features import from `shared/`, never from each other. The `shared/` folder is for code that has **no single owner** and **no business context** — pure utilities and design primitives.

## Common Mistakes

**Mistake 1: Treating `shared/` as a dumping ground.** When developers can't decide where something goes, it ends up in `shared/`. Over time `shared/` accumulates 300 files and becomes the same problem as the old `components/` folder. Discipline: if only one feature uses something, it stays in that feature.

**Mistake 2: Feature creep through shallow imports.** Teams adopt feature folders but keep importing from internal paths out of convenience. Without ESLint enforcement, boundaries erode within weeks. The architecture is only as strong as its tooling enforcement.

**Mistake 3: Over-featurising small apps.** A 3-route app with one developer doesn't need feature-based architecture. The ceremony of barrel files and boundary enforcement adds cognitive overhead without proportional benefit. Apply when the team or codebase has genuinely outgrown flat structure.

## Mental Model

Think of features as **microservices, but for folders**. Each microservice exposes a well-defined API and hides its internal implementation. You don't reach inside another service's database — you call its API. Feature folders work the same way: the `index.ts` is the API contract. Everything else is an internal implementation detail.

The `shared/` folder is like a shared library (npm package) that the microservices depend on — common infrastructure, no business logic.

## Mini Summary

- Feature-based architecture groups code by business domain, not technical layer
- Each feature exposes a public API via `index.ts`; internal files are private
- Cross-feature imports always go through the barrel, never through internal paths
- The `shared/` folder holds genuinely cross-cutting primitives with no single owner
- ESLint rules (import/no-restricted-paths) enforce boundaries automatically in CI

# Guided Practice Quest

You are auditing a codebase that has grown organically for two years. The current structure is flat and layer-based. Work through the guided steps to apply feature-based thinking.

# Solo Practice Quest

You are the lead frontend engineer at a mid-sized fintech startup. The product has five distinct areas: Authentication, Dashboard, Transactions, Budgets, and Settings. The current codebase has everything in flat `/components` and `/hooks` folders. Three developers are joining next quarter.

Design the feature-based folder structure for this codebase. Your answer should:

1. Show the top-level folder structure with at least two features expanded to their second level
2. Define the `index.ts` public API for the `transactions` feature — deciding what should and should not be exported
3. Identify at least three candidates for the `shared/` folder and justify each
4. Explain how you would enforce boundaries with tooling
5. Articulate one thing that should NOT move to feature-based structure in this codebase and why

# Integration

**Psychology — Cognitive Load Theory:** Feature-based architecture directly reduces extraneous cognitive load. When a developer must hold a mental map of four separate folders to understand one feature, working memory is consumed by navigation rather than problem-solving. Co-location is an application of the proximity principle: related information presented together reduces the effort required to integrate it.

**Systems Thinking — Bounded Contexts:** Domain-Driven Design's concept of bounded contexts maps directly to feature boundaries. Each feature is a bounded context with its own language and model. The `shared/` folder is the shared kernel — carefully curated, minimal, owned by everyone and therefore requiring consensus to change.

# Lore Conclusion

The Architect who organised by purpose — by the *outcome* the spell achieved — could hand a corridor of her library to an apprentice and say: "Everything you need for fortress-maintenance is here." The apprentice working on fortresses never needed to know how healing spells worked. Each domain was complete in itself. This is the wisdom of feature-based architecture: not cleverness, but organisation in service of human understanding.
