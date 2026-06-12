---
id: fe-sen-m1-04
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m1
moduleTitle: "Module 1: Frontend Architecture"
moduleGlyph: "🏛️"
moduleSortOrder: 1
topicSlug: micro_frontends
topicTitle: "Micro-Frontends"
topicSortOrder: 4
lesson: micro_frontends
title: "Micro-Frontends"
sortOrder: 4
difficulty: 5
estimatedMinutes: 40
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [systems_thinking, economics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains what micro-frontends are and how Module Federation enables them technically
    - Articulates the real costs (shared state, routing conflicts, styling isolation, shared dependencies)
    - Describes scenarios where micro-frontends are genuinely justified vs overkill
    - Discusses the shell/host and remote/plugin model
    - Addresses team autonomy and independent deployment as the core value proposition
  keywords: [micro-frontend, module federation, host, remote, shell, autonomous, deployment, routing, shared state, isolation]
  modelAnswer: |
    Micro-frontends apply microservices thinking to the UI layer: multiple independently deployable frontend applications are composed into a single user experience. Each application is owned by a different team, uses its own stack (potentially), and can be deployed independently.

    Module Federation (Webpack 5 / Vite) is the dominant technical approach. A host/shell application dynamically loads remote modules at runtime. The shell handles global concerns (navigation shell, auth shell). Each remote owns a business domain.

    The real costs are often underestimated: shared state between remotes is complex (you can't share React context across bundle boundaries without explicit effort). Routing must be negotiated between the shell and each remote. CSS can leak between remotes without careful scoping (CSS Modules or Shadow DOM). Shared dependencies (React, React DOM) must be carefully managed — loading React twice breaks hooks. Performance: the network overhead of loading multiple bundles adds to initial load time.

    Micro-frontends are justified when: independent deployment velocity is genuinely blocked by team coupling, teams are truly autonomous with separate CI/CD, or legacy migration requires running old and new code side by side. They are overkill for most applications. A well-structured monorepo with feature-based architecture achieves most organisational benefits without the runtime complexity.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "In Module Federation, what is the role of the 'host' (shell) application?"
    options:
      - "It serves as the backend API gateway"
      - "It loads and composes remote micro-frontend modules at runtime"
      - "It owns all the CSS and global styles"
      - "It is the only application that can access the database"
    correctIndex: 1
    feedback: "The host (shell) application is the container. It handles top-level concerns like the navigation shell, authentication context, and dynamic loading of remote modules. Each remote (micro-frontend) is a self-contained fragment that the shell composes into the overall user experience."
  - type: MULTIPLE_CHOICE
    prompt: "Two micro-frontend remotes both import React. Without any special configuration, what is the likely result?"
    options:
      - "Both use the same React instance, sharing state correctly"
      - "React is loaded twice, creating two separate instances — React hooks may break"
      - "The application throws a compile-time error"
      - "Performance improves because each remote is isolated"
    correctIndex: 1
    feedback: "React hooks depend on a single React instance. If two remotes load their own copy of React, hooks that cross the boundary (e.g., context) will silently fail or throw errors. Module Federation's `shared` configuration allows specifying that React should be treated as a singleton — loaded once and shared."
  - type: SHORT_TEXT
    prompt: "What is the key organisational justification for micro-frontends that cannot be achieved as effectively with a well-structured monorepo?"
    hint: "Think about deployment pipelines and team autonomy."
  - type: FILL_BLANK
    prompt: "In Module Federation, the `shared` configuration with `singleton: true` ensures that React is only loaded ___ even if multiple remotes declare it as a dependency."
    answer: "once"
    hint: "The whole point is to avoid duplicate React instances."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the following best describes when micro-frontends are genuinely justified?"
    options:
      - "Any application with more than 5 developers"
      - "Applications where multiple autonomous teams need independent deployment pipelines and deployment velocity is genuinely blocked by coupling"
      - "Any application that uses React"
      - "Applications that need to be very fast"
    correctIndex: 1
    feedback: "Micro-frontends solve an organisational problem — team autonomy and deployment independence — not a technical performance problem. The overhead is only justified when the organisational benefit (truly independent deployments, team autonomy across business domains) outweighs the complexity cost."
  - type: MULTIPLE_CHOICE
    question: "CSS leaking between micro-frontend remotes is a common problem. Which approach provides the strongest isolation?"
    options:
      - "Using class names longer than 20 characters"
      - "Shadow DOM with custom elements"
      - "Adding a reset.css to each remote"
      - "Using only inline styles"
    correctIndex: 1
    feedback: "Shadow DOM provides true CSS encapsulation — styles inside a shadow root do not affect the outer document and vice versa. CSS Modules (scoped class names) are the practical middle ground. Shadow DOM is more complete but comes with its own constraints for React-based micro-frontends."
retrieval:
  recall: "Name four operational challenges that arise when composing multiple micro-frontends into a single shell application."
  explain: "Explain why Module Federation's `singleton: true` for shared dependencies is critical for React applications."
  mistakeId:
    code: |
      // shell/webpack.config.js
      new ModuleFederationPlugin({
        name: 'shell',
        remotes: {
          checkout: 'checkout@http://localhost:3001/remoteEntry.js',
          profile: 'profile@http://localhost:3002/remoteEntry.js',
        },
        shared: ['react', 'react-dom'],
      })

      // checkout/webpack.config.js
      new ModuleFederationPlugin({
        name: 'checkout',
        exposes: {
          './CheckoutPage': './src/pages/CheckoutPage',
        },
        shared: ['react', 'react-dom'],
      })
    answer: "The `shared` configuration is missing `{ singleton: true, requiredVersion: '^18.0.0' }`. Without `singleton: true`, if the shell and checkout remote use different minor versions of React, they could load two separate React instances. React hooks (useState, useContext) depend on a single React runtime instance — dual instances cause silent failures and 'Invalid hook call' errors. The correct shared config is `shared: { react: { singleton: true, requiredVersion: '^18.0.0' }, 'react-dom': { singleton: true } }`."
---

# Hook

A large bank has twelve teams, each maintaining a piece of their web portal. The Accounts team is ready to release a major redesign, but they're blocked: the Investments team is mid-migration and the shared CI pipeline is frozen. They've been blocked for three weeks. Two of the twelve teams are holding up ten others. 

A technical architect proposes micro-frontends. "What if each team could deploy their piece of the portal independently?" It sounds like the answer. But three months later, the portal loads 2.4MB on startup, the CSS from the Accounts team overwrites the Investments team's button styles, and sharing auth state between remotes requires an architectural committee meeting. Micro-frontends solved the deployment problem and created six others.

# Lore Introduction

The Guild of Architects proposed the Grand Composition: every Guild would maintain its own tower, independently enchanted and deployed, yet each tower's façade would appear seamlessly joined when viewed from the city. Brilliant in theory. In practice, the Tower of Commerce's colours bled into the Tower of Finance's signage, the entrances used different door mechanisms that confused visitors moving between them, and the shared underground vault was now accessed by twelve different tunnel systems that constantly needed synchronisation. Autonomy had a price.

# Core Learning

## Concept Introduction

**Micro-frontends** decompose a frontend application into independently deployable fragments, each owned by a separate team. The dominant technical approach is **Module Federation** (Webpack 5 / Vite Federation), which allows one application to dynamically load components from another at runtime — not at build time.

```
                    ┌─────────────────────────────┐
                    │   Shell / Host Application   │
                    │   (global nav, auth, routing) │
                    └──────┬──────────────┬─────────┘
                           │              │
              ┌────────────▼──┐      ┌────▼────────────┐
              │  Checkout MFE │      │   Profile MFE   │
              │  (Team Alpha) │      │   (Team Beta)   │
              │  /checkout/*  │      │   /profile/*    │
              └───────────────┘      └─────────────────┘
```

**Webpack Module Federation configuration:**

```javascript
// shell/webpack.config.js
new ModuleFederationPlugin({
  name: 'shell',
  remotes: {
    checkout: 'checkout@https://checkout.acme.com/remoteEntry.js',
    profile: 'profile@https://profile.acme.com/remoteEntry.js',
  },
  shared: {
    react: { singleton: true, requiredVersion: '^18.0.0' },
    'react-dom': { singleton: true, requiredVersion: '^18.0.0' },
  },
})

// checkout/webpack.config.js
new ModuleFederationPlugin({
  name: 'checkout',
  filename: 'remoteEntry.js',
  exposes: {
    './CheckoutApp': './src/CheckoutApp',
  },
  shared: {
    react: { singleton: true, requiredVersion: '^18.0.0' },
    'react-dom': { singleton: true },
  },
})
```

**Consuming a remote in the shell:**

```typescript
// shell/src/App.tsx
import React, { Suspense, lazy } from 'react';

const CheckoutApp = lazy(() => import('checkout/CheckoutApp'));
const ProfileApp = lazy(() => import('profile/ProfileApp'));

export function App() {
  return (
    <Suspense fallback={<PageLoader />}>
      <Routes>
        <Route path="/checkout/*" element={<CheckoutApp />} />
        <Route path="/profile/*" element={<ProfileApp />} />
      </Routes>
    </Suspense>
  );
}
```

## Why It Matters

Micro-frontends are an organisational tool wearing an architecture costume — and knowing when that trade is worth it is the actual senior skill:

- The genuine win is team autonomy at scale: ten teams shipping one monolithic SPA serialise on releases, merge conflicts, and framework migrations; splitting the frontend lets each team deploy independently
- The costs are structural, not incidental — duplicated dependencies bloat what users download, cross-boundary UX consistency needs constant governance, and shared concerns (auth, routing, design system) require platform investment that small orgs can't amortise
- Incremental migration is the quieter use case: strangling a legacy frontend module by module beats a big-bang rewrite that history says will slip
- Adopted at the wrong scale, the pattern is pure overhead: three developers do not need module federation, and many famous adopters later consolidated

The interview-grade insight is Conway's Law in reverse: micro-frontends mirror your org chart onto your users' browsers. If the org chart isn't the problem, the architecture isn't the answer.

## Why It Matters (When It Does)

**Independent deployment**: the checkout team ships a hotfix at 2 PM without any coordination with the profile team. The checkout `remoteEntry.js` is updated; the shell picks it up on next page load. Genuine deployment autonomy.

**Technology independence**: a legacy team can run an older version of a library inside their remote without forcing an upgrade on the whole organisation (to a point — singleton shared dependencies constrain this).

**Parallel team scaling**: twelve teams can work on twelve remotes with zero merge conflicts at the application level.

## The Real Costs

**Shared state is hard**: React Context does not cross bundle boundaries. Auth state, theme, and feature flags need an explicit solution — typically a shared singleton store, custom events, or query params.

**Routing negotiation**: the shell and each remote need to agree on URL ownership. Nested routing is complex when two independent applications each want to manage `react-router`.

**CSS isolation**: without CSS Modules or Shadow DOM, class names from different remotes collide. One remote's `.btn-primary` overrides another's.

**Performance**: each remote loads a separate entry point. Even with shared dependencies, the sum of all `remoteEntry.js` files adds to initial load time and creates multiple network round-trips.

**Operational complexity**: instead of one build and deploy pipeline, you have N. Versioning compatibility between shell and remotes must be managed. Failures in one remote should not crash the shell (error boundaries are essential).

## Common Mistakes

**Mistake 1: Using micro-frontends for technical reasons only.** If the motivation is "cleaner code structure" — use a monorepo with feature-based architecture. Micro-frontends solve organisational problems (team autonomy, deployment independence), not code structure problems.

**Mistake 2: Forgetting singleton shared dependencies.** Not configuring `singleton: true` for React means each remote potentially loads its own React. The `Invalid hook call` error from duplicate React instances is notoriously difficult to debug.

**Mistake 3: Sharing too much through the shell.** As the shell accumulates shared state, utilities, and services, it becomes a bottleneck. Any change to the shell requires coordination. Keep the shell to global infrastructure only: navigation chrome, auth context provider, global error boundary.

**Mistake 4: No contract testing between shell and remotes.** If the checkout remote renames its exported component, the shell fails silently at runtime — not at build time. Contract tests or TypeScript type sharing across remotes are essential.

## Mental Model

Micro-frontends are like **food courts in a shopping mall**. The mall (shell) provides the building, shared facilities (toilets, security, navigation signage), and overall branding. Each restaurant (remote) operates independently — different menus, staff, suppliers, and operating hours. A restaurant can renovate without closing the mall. But the food court works because shared rules are agreed: opening hours, health standards, payment terminals. The rules that span the boundary require explicit agreements, not assumptions.

## Mini Summary

- Micro-frontends allow independent teams to deploy frontend fragments independently via Module Federation
- The shell/host composes remotes at runtime; each remote is a separately deployed bundle
- `singleton: true` for shared dependencies (React) is non-negotiable
- Real costs: shared state complexity, routing, CSS isolation, performance overhead, operational N+1 pipelines
- Justified when teams are truly autonomous with independent deployment needs; overkill for most applications

# Guided Practice Quest

You are evaluating whether a large e-commerce platform should adopt micro-frontends. The platform has five teams: Discovery, Cart, Checkout, Account, and Marketing. Currently all five contribute to a single React monorepo with separate Turborepo packages. Work through the decision framework.

# Solo Practice Quest

A CTO has asked you to evaluate micro-frontends for their SaaS dashboard. They have three product teams (Analytics, Collaboration, Integrations) and have been experiencing deployment bottlenecks. CI pipelines currently take 18 minutes and a flaky test in Integrations frequently delays Analytics releases.

Write your technical evaluation. Your answer should:

1. Identify whether the problem described (flaky test in one team blocking another) is a micro-frontends problem or a CI/testing problem — and why the distinction matters
2. If you recommend micro-frontends, design the shell and remote boundaries and explain what the shell is responsible for
3. If you recommend against, propose an alternative architecture that solves the deployment coupling problem
4. Explain the top three technical risks if they proceed with micro-frontends and how to mitigate each
5. Define the success metric you would use 6 months post-adoption to evaluate whether the decision was correct

# Integration

**Systems Thinking — Conway's Law:** Conway's Law states that organisations design systems that mirror their communication structures. Micro-frontends are the architectural embodiment of Conway's Law — if you have autonomous teams with independent deployment pipelines, your architecture should reflect that. If you don't have that organisational structure but adopt micro-frontends anyway, you create architectural complexity without the organisational benefit.

**Economics — Make vs Buy Boundary:** The micro-frontend decision is essentially a make-or-buy problem applied to coupling. Monorepo = highly integrated firm (low inter-unit transaction costs, high coordination overhead). Micro-frontends = loosely coupled market (higher inter-unit transaction costs via explicit contracts, lower internal coordination overhead). Choose based on which coordination cost dominates.

# Lore Conclusion

The Guild of Architects did not abandon the Grand Composition — they refined its rules. Each tower would expose only named passages at defined heights. The shared underground vault had a single agreed-upon protocol. The colour palette was negotiated once and enshrined. The towers remained autonomous; the rules of composition became explicit rather than assumed. Autonomy, it turned out, required more deliberate agreements — not fewer. The towers stood beautifully, but only because the rules were written down.
