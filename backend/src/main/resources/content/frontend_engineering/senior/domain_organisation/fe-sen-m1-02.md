---
id: fe-sen-m1-02
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m1
moduleTitle: "Module 1: Frontend Architecture"
moduleGlyph: "🏛️"
moduleSortOrder: 1
topicSlug: domain_organisation
topicTitle: "Domain Organisation"
topicSortOrder: 2
lesson: domain_organisation
title: "Domain Organisation"
sortOrder: 2
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [philosophy, systems_thinking]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains bounded contexts and how they translate to frontend folder boundaries
    - Distinguishes between the shared kernel and feature modules
    - Articulates strategies for managing cross-domain dependencies without coupling
    - Discusses how to identify when a feature has grown into a sub-domain requiring further splitting
    - Addresses the role of an explicit dependency graph and why acyclic dependencies matter
  keywords: [bounded context, domain, shared kernel, cross-domain, dependency, coupling, acyclic, ubiquitous language, anti-corruption layer]
  modelAnswer: |
    Domain organisation applies Domain-Driven Design (DDD) concepts to frontend folder structure. A bounded context is a business area with its own language and model — in the frontend, this maps to a top-level feature or domain folder.

    The key insight is that different parts of a business use the same words differently. "User" in the auth domain means credentials and session state. "User" in the billing domain means payment methods and subscription tier. These are different models that should not be merged into one global User type.

    The shared kernel is the deliberately minimal intersection: primitives (Button, Modal), utility functions, and types so foundational that all domains depend on them. The shared kernel should be small and change rarely.

    Cross-domain dependencies are managed by: (1) importing only from a domain's public API (index.ts barrel), (2) extracting shared concepts to the shared kernel rather than coupling domains together, and (3) in complex cases, introducing an adapter or anti-corruption layer that translates between domain models.

    Dependencies must be acyclic. If auth depends on user-profile and user-profile depends on auth, neither can be independently modified or deleted. Detect cycles with tools like `madge`. Resolve cycles by extracting the shared concept to a lower-level shared module.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "Your e-commerce frontend has a `User` type used by both `auth` (containing `email`, `passwordHash`, `sessionToken`) and `shipping` (containing `name`, `defaultAddress`, `phoneNumber`). What is the DDD-informed approach?"
    options:
      - "Create one shared `User` type that contains all fields from both domains"
      - "Have `shipping` import its User data from the `auth` domain"
      - "Define separate `AuthUser` and `ShippingUser` types within each bounded context"
      - "Put the User type in a global types file"
    correctIndex: 2
    feedback: "This is the bounded context principle. The word 'User' means different things in different domains. Auth cares about authentication data; shipping cares about delivery data. Merging them creates a bloated type and couples two unrelated domains. Each domain should own its own model of the concepts it needs."
  - type: SHORT_TEXT
    prompt: "What tool or technique can you use to detect circular dependencies between domain modules in a JavaScript/TypeScript project?"
    hint: "Think about a tool that generates a dependency graph."
  - type: FILL_BLANK
    prompt: "The minimal, deliberately small set of shared code that all domains depend on is called the shared ___."
    answer: "kernel"
    hint: "A DDD term for the carefully curated intersection of multiple bounded contexts."
  - type: MULTIPLE_CHOICE
    prompt: "The `notifications` domain needs to display a user's name. The name lives in the `profile` domain. What is the cleanest approach?"
    options:
      - "Import `ProfileUser` from `features/profile` and access `.name`"
      - "Pass the user's name as a prop or via a shared context — don't import across domain boundaries"
      - "Copy the user name logic into the notifications domain"
      - "Merge notifications and profile into one domain"
    correctIndex: 1
    feedback: "Domains should not import each other's models directly. The correct pattern is to pass data through props, a shared event, or a thin shared type in `shared/`. This keeps domains decoupled — notifications does not need to know anything about the profile domain's internal structure."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does 'acyclic dependency' mean in the context of frontend domain organisation?"
    options:
      - "No domain should have any dependencies on other domains"
      - "Domain A can depend on B, but B must not depend on A (no circular dependency)"
      - "All domains must depend on a central orchestrator"
      - "Dependencies should be removed with lazy loading"
    correctIndex: 1
    feedback: "Acyclic means no cycles. If A depends on B and B depends on A, you have a circular dependency. Neither can be changed, tested, or deleted without affecting the other. The Acyclic Dependencies Principle (ADP) requires that dependencies flow in one direction only."
  - type: MULTIPLE_CHOICE
    question: "What is the shared kernel in domain organisation?"
    options:
      - "A Node.js kernel for server-side rendering"
      - "The largest, most important domain in the application"
      - "Deliberately minimal shared code (primitives, utilities) that all domains may depend on"
      - "A configuration file shared between all microservices"
    correctIndex: 2
    feedback: "The shared kernel is kept small intentionally. It contains only things so fundamental that every domain needs them — design primitives, utility functions, base types. It is owned collectively and changes slowly. Bloating the shared kernel re-introduces the coupling that domain boundaries were designed to prevent."
retrieval:
  recall: "What is a bounded context, and give a concrete frontend example of two domains that should NOT share the same User type."
  explain: "Explain why circular dependencies between domains are architecturally dangerous and how you would detect and resolve one."
  mistakeId:
    code: |
      // features/notifications/hooks/useNotifications.ts
      import { AuthUser } from '../../auth/types/auth.types';
      import { ProfileService } from '../../profile/services/profileService';
      import { BillingSubscription } from '../../billing/types/billing.types';

      export function useNotifications(user: AuthUser) {
        const profile = ProfileService.getProfile(user.id);
        const subscription = BillingSubscription.get(user.id);
        // ...
      }
    answer: "The notifications hook directly imports types and services from three separate domains (auth, profile, billing), creating three cross-domain couplings. Any change to those domains' internals breaks notifications. The fix: (1) accept only the minimal data needed as props/context (e.g., userId: string), (2) use each domain's public barrel API if imports are necessary, and (3) consider whether notifications needs its own thin service that aggregates data via domain APIs."
---

# Hook

Six months ago your team split the monolithic frontend into domain folders. Authentication, billing, profile, and notifications all have their own directories. Everything seemed clean. Then a developer adds a `User` type to the shared folder because billing and auth both need it. Three weeks later it has 40 fields, half of which are only relevant to one domain, and a change to add `billingTier` breaks the auth login tests. The shared `User` type has become a God Object.

Domain organisation solves not just *where* to put code, but how to prevent the shared spaces from becoming gravitational black holes.

# Lore Introduction

The Arcane Academy's great library was once organised by the colour of the tome's binding — practical when it held a hundred books. At ten thousand volumes, finding all spells that affected the human body required visiting every shelf. The Grand Archivist Vera re-organised not by *form* but by *domain of effect*: the body, the mind, the material world. Each domain had its own language. A "restoration" in the body domain meant healing flesh. In the material domain, it meant mending broken objects. Same word, different meaning. Vera's first rule: never assume the same word means the same thing across domains.

# Core Learning

## Concept Introduction

Domain organisation takes feature-based architecture one level deeper by applying **Domain-Driven Design (DDD)** principles. The key concepts are:

**Bounded Context**: a business area with its own consistent model and language. In a frontend codebase, each bounded context becomes a top-level domain folder.

**Ubiquitous Language**: within a bounded context, terms have precise, agreed-upon meanings. "Product" in a catalogue domain means display name, description, and image. "Product" in a cart domain means SKU, quantity, and unit price. These are genuinely different things.

**Shared Kernel**: the deliberate, minimal intersection. Contains only primitives and utilities that every domain needs. Changes to the shared kernel require agreement from all domain owners.

```
src/
  domains/
    auth/
      components/
      hooks/
      services/
      types/
        auth.types.ts   // AuthUser: { id, email, sessionExpiry }
      index.ts
    profile/
      types/
        profile.types.ts  // ProfileUser: { id, name, avatar, bio }
      index.ts
    billing/
      types/
        billing.types.ts  // BillingAccount: { id, plan, paymentMethods }
      index.ts
    notifications/
      index.ts
  shared/
    ui/           // Button, Modal, Toast — no business logic
    utils/        // formatDate, formatCurrency
    types/        // Pagination<T>, ApiResponse<T>
```

## Why It Matters

**Prevents model pollution**: each domain owns its view of an entity. No single bloated `User` type that serves ten masters and satisfies none.

**Independent evolution**: the billing domain can add a `enterpriseTier` field to `BillingAccount` without affecting AuthUser or ProfileUser.

**Team alignment**: domain boundaries often correspond to team boundaries. The identity team owns auth and profile. The payments team owns billing. Clear ownership, no accidental coupling.

## Worked Examples

**Example 1: Cross-domain data without direct coupling**

The notifications domain needs to show a personalised greeting using the user's display name (which lives in profile) and subscription tier (which lives in billing). Rather than importing from both domains:

```typescript
// shared/types/notification-context.types.ts
// A thin, shared type that only contains what notifications needs
export interface NotificationContext {
  userId: string;
  displayName: string;
  subscriptionTier: 'free' | 'pro' | 'enterprise';
}
```

```typescript
// domains/notifications/hooks/useNotifications.ts
import type { NotificationContext } from '@/shared/types/notification-context.types';

// Notifications receives what it needs — it doesn't know where it came from
export function useNotifications(context: NotificationContext) {
  // ...
}
```

The assembly of `NotificationContext` happens at the application level, not inside the notifications domain.

**Example 2: Detecting cycles with madge**

```bash
npx madge --circular src/domains/
 # Outputs: No circular dependency found!

 # Or if there are cycles:
 # Circular dependency found!
 # auth/services/authService.ts -> profile/services/profileService.ts -> auth/hooks/useCurrentUser.ts
```

Run this in CI to prevent circular dependencies from being merged.

## Common Mistakes

**Mistake 1: Premature domain splitting.** Splitting into domains before the boundaries are understood creates artificial seams. Start with feature-based, then elevate to domain organisation when a feature becomes large enough to have its own internal sub-features and team ownership.

**Mistake 2: Anti-patterns in the shared kernel.** Business logic — even logic used by many features — is not a shared kernel candidate. The shared kernel contains structural primitives (Button, Pagination). If `calculateTax` is used in billing and checkout, it belongs in the billing domain's public API, not shared/utils.

**Mistake 3: Tight coupling via events.** Some teams use custom events to "decouple" domains without realising they've just moved the coupling to string-based event names. Document event contracts explicitly and version them if they must cross domain boundaries.

## Mental Model

Think of domains as **countries** with their own official language. Within France, "pain" means bread. Within Spain, it means suffering. When you cross the border, you need a translator — an adapter or mapping function. The shared kernel is the international language: English in a business context — limited, but universally understood.

The dependency graph must be a directed acyclic graph (DAG). Water flows downhill. Lower-level domains (shared kernel) never depend on higher-level domains. Higher-level domains depend on the shared kernel. The flow is always one direction.

## Mini Summary

- Bounded contexts give each domain its own model and language for shared concepts
- The shared kernel is small, stable, and contains zero business logic
- Cross-domain dependencies are managed through thin shared types or passed data, not direct model imports
- Dependencies must be acyclic — use `madge` in CI to detect cycles
- Domain splitting is earned through complexity, not applied prematurely

# Guided Practice Quest

You are refactoring a large e-commerce frontend. The current `shared/types/user.ts` has grown to 60 fields serving auth, shipping, billing, and marketing contexts. Work through how to decompose this God Object into domain-appropriate models.

# Solo Practice Quest

You are the lead architect for a B2B SaaS platform with the following domains: Authentication, Organisation Management, Project Management, Billing, and Reporting. A new requirement arrives: the Reporting domain needs to display "active projects per billing tier" — information that combines data from both Project Management and Billing.

Design your approach to this cross-domain data requirement. Your answer should:

1. Explain why Reporting should NOT directly import from both Project Management and Billing domains
2. Design a thin data contract that Reporting can consume without coupling to either domain
3. Decide where the data assembly logic lives (which layer coordinates the cross-domain data fetch)
4. Address whether any new types go into the shared kernel, and justify the decision
5. Describe how you would test the Reporting feature in isolation from the other two domains

# Integration

**Philosophy — Ontology:** Domain organisation is fundamentally an ontological problem — how we categorise and name things. Aristotle's categories were the first attempt at a systematic ontology. In software, different domains have different ontologies for the same concepts. A "customer" in a CRM is a relationship to be nurtured; in billing it's a payment entity. Domain-driven design acknowledges that there is no single, universal categorisation — only locally consistent ones.

**Systems Thinking — Coupling and Cohesion:** The metrics Larry Constantine introduced in 1968 for structured design — maximise cohesion, minimise coupling — apply directly to domain organisation. High cohesion (related things together in one domain) and loose coupling (domains interacting only through defined interfaces) are the twin virtues. Every architectural decision can be evaluated against these two axes.

# Lore Conclusion

Vera's re-organised library took five years to complete. Apprentices complained that "restoration" now meant two different things depending on which wing you stood in. Veterans complained that it was confusing. But after a decade, every Archivist agreed: when you needed to understand healing, you went to one wing and understood it completely. The confusion about words was resolved by context. The domains spoke their own language, and everyone was the richer for it.
