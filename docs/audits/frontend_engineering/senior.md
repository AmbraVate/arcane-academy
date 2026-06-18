# Audit — Frontend Engineering · Senior

**Auditor lens:** Principal Frontend Engineer with experience in large-scale consumer products, frontend platform ownership, and hiring/levelling senior engineers at top-tier companies
**Tier mandate:** Produce engineers who can architect complex React applications, apply advanced state management, enforce performance budgets, implement and audit frontend security, achieve WCAG compliance, operate observability tooling, lead technical decisions, and pass senior-level frontend interviews.
**Scope:** 30 lessons across 29 topics (one lesson per topic throughout).

---

## 1. Verdict at a glance

This is the tier with the most impressive single-lesson depth in the curriculum. Each lesson is a substantive synthesis essay (SYNTHESIS questType with PATTERN_MATCH assessment), covering complex multi-dimensional topics at a level that would satisfy a rigorous staff-engineer interview prep standard. The security module (XSS, CSRF, secure storage, authentication flows) is genuinely excellent — technically accurate, covering the right edge cases (PKCE, SameSite cookies, HttpOnly vs localStorage trade-offs), and asking the right questions. The state management module (Redux, Zustand, state machines) is similarly strong: RTK Query is mentioned in the Redux lesson, the "impossible states" framing for XState is the canonical argument, and Zustand's selector performance gotcha is correctly identified. The performance module covers CWV metrics correctly including the FID→INP transition. The primary structural weakness is that every topic has exactly one lesson, which means breadth is sacrificed for depth in a tier that should ideally offer both. Key architectural topics (TypeScript, testing strategy, CI/CD integration, server-side rendering, React Query/TanStack Query as a server state abstraction) are either absent or underweighted. The senior project (fe-sen-m8-01, "The Guild Platform") is well-designed but very demanding for a single submission — and Cypress/Playwright e2e testing is required by implication without having been taught.

**Coverage: 3/5 | Rigor/Depth: 5/5 | Sequencing: 4/5 | Practice quality: 4/5**

---

## 2. KEEP — strengths to preserve

- **fe-sen-m4-01 (xss)** — The three XSS types, React's automatic escaping, `dangerouslySetInnerHTML` as the primary vector, DOMPurify usage, and CSP as defence-in-depth: complete, accurate, and at the right depth for a senior engineer. The attack scenario (malicious comment body) is concrete. Keep everything in this lesson.
- **fe-sen-m4-02 (csrf)** — CSRF via automatic cookie sending, SameSite cookie attributes, CSRF tokens, and the JWT Bearer header advantage: all correct. The distinction "why SPAs using Bearer tokens are less vulnerable to CSRF" is a nuanced point that most training materials get wrong. This is exceptional.
- **fe-sen-m4-04 (secure_storage)** — The recommendation (access tokens in memory, refresh tokens in HttpOnly cookies) is the current industry consensus and correctly reasoned. The third-party analytics script attack vector is a realistic and well-chosen scenario. The "not everything needs the most secure storage" point about theme preference is the right counterweight.
- **fe-sen-m2-03 (state_machines)** — The "impossible states" argument (2^5 = 32 combinations, only 5 valid) is the canonical justification for XState and is presented correctly. The double-submit prevention example is concrete and memorable.
- **fe-sen-m2-01 (redux)** — RTK Query is mentioned alongside Redux, the Immer explanation is correct, and the "when is Redux justified" decision tree is sound. The selector memoisation/stale closure pitfalls section is at exactly the right depth for a senior.
- **fe-sen-m3-02 (memoisation)** — "Start with no memoisation. Profile. Apply the minimum fix." is the correct philosophy and is stated explicitly. The referential equality explanation (arrow function in JSX creates a new reference every render) is the most common memoisation mistake and is well-handled.
- **fe-sen-m3-01 (rendering_performance)** — Reconciliation algorithm, commit phase vs render phase, three causes of unnecessary re-renders, React DevTools Profiler usage: all correct and at senior depth. The "0.1ms re-render is not a problem" counterbalance is important.
- **fe-sen-m6-01 (wcag_compliance)** — POUR principles, AA vs AAA distinction, legal requirements (EU Accessibility Act, ADA, Section 508), and specific criteria by number: this is the level of precision needed for a senior accessibility discussion.
- **fe-sen-m1-01 (feature_based_architecture)** — The `index.ts` barrel/public API pattern, `shared/` vs cross-feature imports, and the ESLint enforcement mechanism are all correct. The "when is it worth the complexity?" calibration is the right senior question.
- **fe-sen-m7-02 (error_tracking)** — Source maps as critical (without them, minified traces are useless), error grouping quality (dynamic message = many issues), PII exclusion (GDPR), alert thresholds (don't alert on every error): all production-accurate.

---

## 3. CHANGE — restructure / resequence

- **fe-sen-m5-02 (components)** — A "components" topic at the senior level (fe-sen-m5-02) inside a "Design Systems" module feels misplaced. The lesson presumably covers component API design and variant patterns. Its placement in Module 5 after state management and before security is disconnected. Move this to Module 1 (Architecture) where it can follow domain organisation and inform the public API pattern discussion.
- **fe-sen-m5-03 (documentation)** — Documentation is important but feels isolated as a standalone senior lesson. At this tier, documentation is best discussed in the context of maintaining large codebases — move to a "maintainability" framing or combine with architecture lessons about ADRs and decision records.
- **fe-sen-m6-02 (complex_interactions)** and **fe-sen-m6-04 (inclusive_ux)** — These two accessibility topics sit alongside `wcag_compliance` and `accessibility_testing` in Module 6 but seem broader than accessibility alone. `complex_interactions` (custom components, ARIA patterns) belongs with WCAG compliance. `inclusive_ux` belongs with behavioural design patterns. Consider whether Module 6 needs tighter scoping.

---

## 4. UPDATE — depth / rigor / currency

- **fe-sen-m2-01 (redux)** — RTK Query is mentioned but not demonstrated. A single worked example showing `createApi` with `providesTags`/`invalidatesTags` would make the lesson actionable. Without it, "use RTK Query for server state" is a correct directive with no implementation path. The contrast between client state (Redux slices) and server state (RTK Query) should be more explicit.
- **fe-sen-m2-02 (zustand)** — `subscribeWithSelector` middleware is mentioned in a guided step hint but not fully covered. The `immer` middleware pattern for complex nested state updates deserves a worked example. Zustand's persistence with `persist` middleware is correct; add the caveat about state version migration when the stored shape changes.
- **fe-sen-m3-04 (bundle_optimisation)** — The lesson covers code splitting, tree-shaking, and bundle visualisation well. Missing: (1) the `<link rel="preload">` technique for critical resources, (2) image optimisation (WebP/AVIF, `<picture>`, responsive images) as a significant bundle-adjacent concern, and (3) dynamic `import()` without React.lazy for non-component modules. At the senior level, a comprehensive bundle strategy covers these.
- **fe-sen-m3-05 (core_web_vitals)** — The FID→INP transition is correctly noted. Add explicit coverage of `scheduler.postTask` and `setTimeout` chunking as the primary INP improvement techniques. The "Lighthouse vs field data (CrUX)" distinction is mentioned in a checkpoint question but deserves more prominence — seniors need to know why a passing Lighthouse score doesn't mean passing CWV for real users.
- **fe-sen-m4-03 (authentication_flows)** — PKCE is correctly described. Add coverage of: (1) how to implement token refresh interceptors in axios/fetch (the standard pattern), (2) handling 401 responses and the retry-after-refresh flow, and (3) the logout flow (revoke tokens, clear memory state, clear HttpOnly cookies). The current lesson covers the theory well but stops short of implementation.
- **fe-sen-m7-04 (user_analytics)** — This lesson should address privacy considerations explicitly: GDPR, cookie consent, and the distinction between analytics (aggregate, anonymised) and tracking (individual user). Privacy-by-design at the senior level is an expected competency. Product analytics tooling (Amplitude, Mixpanel, PostHog) should be named rather than described generically.

---

## 5. REMOVE — cut or merge

- **fe-sen-m5-02 (components)** — If this lesson covers what component API design looks like at the senior level (as expected), its placement and title are ambiguous. If it is reviewing React component fundamentals, it is below senior level and should be removed. Audit the content: if it is about design system component API design (compound components, polymorphic `as` prop, Radix-style composition), keep but move to Module 5 under a clearer title. If it is about React functional component syntax, remove — this was covered in the junior tier.
- No other removals recommended — at one lesson per topic, no topic is overweighted.

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **TypeScript** (advanced) | TypeScript is the language of senior React development. Generic types, discriminated unions, conditional types, utility types (Omit, Pick, Partial), and typing third-party libraries are all senior interview questions. The curriculum uses TypeScript in examples (`.ts` files referenced) but has no dedicated senior TypeScript lesson. | New topic in Module 1 (Architecture) or as a standalone module |
| **Server State Management** (TanStack Query / RTK Query dedicated) | React Query/TanStack Query is the dominant pattern for server state in 2025. RTK Query is mentioned in the Redux lesson but not as a standalone pattern. A dedicated lesson on `useQuery`, `useMutation`, cache invalidation, and optimistic updates would directly address the gap every junior-to-senior transition hits. | New topic in Module 2 (State Management) |
| **E2E and Integration Testing** | The senior tier covers no end-to-end testing. Cypress and Playwright are the industry standards and are expected knowledge at the senior level (writing tests, debugging flaky tests, CI integration, visual regression). This is absent entirely. | New topic in a Testing module (Module 2 or 3) |
| **CI/CD and Performance Budgets** | Integrating Lighthouse CI into a pipeline, setting performance budgets that fail builds, and automating accessibility audits in CI are senior-level operational skills. Monitoring is covered (fe-sen-m7-03) but CI integration is not. | New lesson in Module 3 (Performance) or Module 7 (Observability) |
| **Server-Side Rendering / Next.js concepts** | SSR, SSG, ISR, and hydration trade-offs are discussed in every senior frontend interview at companies using Next.js (the dominant production React framework). A single lesson on when to use SSR vs CSR, what hydration costs, and the architectural implications would address a significant real-world gap. | New topic in Module 1 (Architecture) |
| **Accessibility Testing toolchain** | `accessibility_testing` exists (fe-sen-m6-03) but the ARIA authoring patterns for common components (modal, combobox, tabs, accordion, tree) are absent. At the senior level, building ARIA-conformant custom components from scratch — not just running axe — is an expected skill. | Expand `accessibility_testing` or add `aria_patterns` topic |
| **Frontend CI/CD Pipeline** | Understanding and configuring a frontend CI pipeline (lint → type-check → unit tests → build → e2e → deploy) is expected at senior level. No lesson covers this end to end. | New topic in a tooling or platform module |

---

## 7. PRACTICE & ASSESSMENT

The shift to `PATTERN_MATCH` assessment (open-ended synthesis evaluated against rubric) is correct for this tier — the questions require genuine reasoning, not recall. The guided steps consistently present realistic, high-stakes scenarios (payment flow CSRF, token storage in localStorage, bundle at 400KB from a charting library). These are not toy problems.

The `modelAnswer` blocks in senior lessons are genuinely expert-quality prose: the Redux model answer covers RTK slices, RTK Query, and normalization trade-offs in one coherent paragraph that a senior engineer would be satisfied with producing.

Three practice gaps:
1. **No e2e or integration test practice** — the senior project requires "at least 3 component tests" but at this tier the expectation should include e2e tests with Playwright or Cypress.
2. **No architectural decision practice** — the project (fe-sen-m8-01) requires a feature-based architecture, but there is no lesson or practice that walks through writing an ADR or comparing architectural options with explicit trade-offs. The `feature_based_architecture` lesson covers the pattern but not the decision-making process.
3. **Observability module (Module 7) has no capstone exercise** — the four observability topics (logging, error tracking, monitoring, user analytics) are isolated lessons with no integration exercise. A scenario-based exercise ("a spike in error rates at 2am — trace the issue from alert to root cause to fix using the tools from this module") would consolidate these topics.

---

## 8. Prioritized action list

1. **ADD** — TypeScript (advanced) topic: generic types, discriminated unions, utility types, React typing patterns — directly addresses the largest real-world gap
2. **ADD** — TanStack Query / server state management as a dedicated topic separate from Redux
3. **ADD** — E2E Testing topic (Playwright or Cypress: writing tests, debugging flaky tests, CI integration)
4. **ADD** — Next.js/SSR concepts lesson: CSR vs SSR vs SSG trade-offs, hydration cost, when to choose each
5. **ADD** — CI/CD pipeline lesson: Lighthouse CI integration, performance budgets that fail builds, automated accessibility audits
6. **UPDATE** — `authentication_flows` to include token refresh interceptor pattern and logout flow implementation
7. **UPDATE** — `bundle_optimisation` to add image optimisation (WebP/AVIF, responsive images) and `<link rel="preload">`
8. **UPDATE** — `user_analytics` to cover GDPR, cookie consent, and privacy-by-design explicitly
9. **CHANGE** — Move `components` (fe-sen-m5-02) to Module 1 and clarify its scope (design system API design, not React basics)
10. **ADD** — Observability integration scenario exercise tying logging, error tracking, monitoring, and analytics together
