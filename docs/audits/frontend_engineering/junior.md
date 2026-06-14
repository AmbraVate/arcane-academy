# Audit — Frontend Engineering · Junior

**Auditor lens:** Staff Frontend Engineer assessing whether this tier produces job-ready junior React developers who can pass a technical screen and contribute to a production codebase within their first month
**Tier mandate:** Produce developers who can build React applications with components, hooks, routing, API integration, form handling, basic testing, Tailwind CSS, and professional tooling (Vite, npm, ESLint, Prettier) — sufficient to be hired as a junior frontend engineer.
**Scope:** 124 lessons across 42 topics.

---

## 1. Verdict at a glance

This is a thorough, well-constructed junior tier that covers the React ecosystem competently. The sequencing is logical: React foundations → state management → events/forms → routing → API integration → component architecture → testing → styling → tooling → capstone project. Coverage of the core hiring criteria (components, props, state, hooks, routing, fetch, testing, Tailwind) is solid. The instructional quality is high — guided steps use concrete code questions, solo rubrics are specific and verifiable, and retrieval blocks appear consistently. The Guild Dashboard capstone (fe-jun-m10-01) is well-designed: it uses a real API, requires architecture decisions, and tests integration of all modules. The significant gap is the absence of TypeScript as a dedicated topic — this is the dominant industry tool and is expected at junior interviews in 2025. `useEffect` is covered but buried inside `loading_states` rather than given its own topic, making it easy to miss. Custom hooks are touched in `separation_of_concerns` but not given dedicated depth. No React Query or server state library is introduced, leaving a gap that every junior developer will immediately encounter on the job.

**Coverage: 4/5 | Rigor/Depth: 4/5 | Sequencing: 4/5 | Practice quality: 4/5**

---

## 2. KEEP — strengths to preserve

- **fe-jun-m1-01 (why_react_exists / the_problem_react_solves)** — Starting with the problem (DOM synchronisation complexity) before the solution (React) is the pedagogically correct order. The declarative vs imperative framing is exact.
- **fe-jun-m2-07 (derived_state / computing_from_state)** — "If it can be computed from other state, don't store it" is one of the most valuable React lessons and is introduced early. The `mistakeId` example (`const [filteredItems, setFilteredItems] = useState(items)`) is a real-world mistake most juniors make. Keep this lesson and its placement.
- **fe-jun-m5-07 (loading_states / the_three_states)** — Three-state data fetching (loading/error/success) taught explicitly, with the TypeError crash as the motivating example: correct and important. The question "Why should isLoading start as true if you fetch immediately on mount?" is a good discriminating question.
- **fe-jun-m5-08 (loading_states / useeffect_for_fetching)** — useEffect coverage is solid: the async-inside-effect pattern, dependency array semantics, and AbortController cleanup are all present. The reflectionPrompt about React Strict Mode double-invocation is a professional-level insight.
- **fe-jun-m4-04 (react_router / setting_up_routes)** — React Router v6 (not v5) is covered, including the exact-by-default change. Specific and current. The wildcard 404 route is named explicitly.
- **fe-jun-m2-13 (context_api / what_is_context)** — The "context is not for everything" constraint (frequent re-renders) is stated clearly and with the correct counter-examples. The comparison of auth status vs a counter is the canonical example.
- **fe-jun-m3-07 (form_validation / validation_strategies)** — On-change vs on-blur vs on-submit validation timing is a nuanced UX question that most junior courses skip. Including it here with the "premature error message → abandonment" research is the right depth.
- **fe-jun-m5-11 (error_handling / ErrorBoundary)** — Error boundaries covered with class component syntax (correct, since they cannot be functional), fallback composition, and per-section isolation. This is production-quality advice at the junior level.
- **fe-jun-m10-01 (mini_project / the_guild_dashboard)** — The capstone is well-designed: real API (JSONPlaceholder), no step-by-step walkthrough, architecture decision required upfront ("draw your component tree on paper"), and rubric items that are specific enough to fail on. Service module pattern required, not raw fetch in components: professionally correct.
- **fe-jun-m7-01 (unit_testing / why_test_frontends)** — Testing pyramid introduced correctly with speed/cost trade-offs explicitly stated. The decision to include testing as a module (not a footnote) distinguishes this platform.
- **fe-jun-m5-04 (async_operations / promises)** — All three Promise states named, `.then`/`.catch` chaining, and callback hell as the motivation: complete and correct.

---

## 3. CHANGE — restructure / resequence

- **fe-jun-m5-08 (loading_states / useeffect_for_fetching)** — `useEffect` is the most important hook after `useState` and it is introduced here as lesson 2 inside `loading_states`. This buries a fundamental concept inside a narrower topic. `useEffect` deserves its own topic (`use_effect`) between `use_state` and `derived_state` in Module 2, covering: the side effect concept, the dependency array, cleanup, and the "async inside useEffect" pattern. The data-fetching application can then be a natural example in `loading_states` without being the primary introduction to the hook.
- **fe-jun-m6-05 (separation_of_concerns / custom_hooks)** — Custom hooks are a lesson inside `separation_of_concerns`. They deserve a dedicated topic (`custom_hooks`) in Module 6 or 2, with at least two lessons: what makes a hook extractable, the `use` naming convention, and two or three worked examples (`useFetch`, `useLocalStorage`, `useDebounce`). Custom hooks are asked about in virtually every junior technical interview.
- **fe-jun-m9-07–09 (build_pipelines)** — TypeScript is referenced in `build_pipelines` (fe-jun-m9-07 or m9-08 based on file evidence) but has no dedicated topic. Move or add TypeScript coverage to its own topic — this is not a build tool concern, it is a language concern. See Gaps section.

---

## 4. UPDATE — depth / rigor / currency

- **fe-jun-m2-04 (use_state / the_usestate_hook)** — The lesson correctly covers destructuring and the setter convention. It should also address the functional update form (`setCount(prev => prev + 1)`) — this is the pattern required when the new state depends on the previous state and is a common source of stale-closure bugs for juniors.
- **fe-jun-m5-13–15 (rest_consumption)** — The REST consumption lessons cover HTTP methods correctly. Add explicit coverage of: (1) how to handle non-2xx HTTP status codes (fetch does not reject on 404/500 — this must be checked manually), and (2) the pattern for attaching Authorization headers. Both are things juniors get wrong on day one at a real job.
- **fe-jun-m7-04–06 (component_testing)** — React Testing Library is the correct tool choice. Confirm that the lessons cover the most important RTL principle: test behaviour, not implementation. The `getByRole` query should be emphasized over `getByTestId` or `querySelector`. At least one lesson should show testing an async component (loading → success state).
- **fe-jun-m9-10–12 (eslint)** — ESLint lessons should mention `eslint-plugin-react-hooks` specifically — the `exhaustive-deps` rule is one of the most valuable tools for catching dependency array mistakes. Without this, the eslint content is generic rather than React-specific.
- **fe-jun-m4-07–09 (nested_routes)** — Nested routes with `<Outlet />` is a React Router v6 concept that many juniors struggle with. Confirm the Outlet pattern is explicitly taught with a parent/child route worked example (e.g., a settings page with sub-routes).

---

## 5. REMOVE — cut or merge

- No topics warrant full removal at this tier. The 42-topic breadth is appropriate.
- **fe-jun-m6-07–09 (presentational_vs_container)** — The container/presentational pattern was popularised for class-component codebases (pre-hooks). With hooks, the pattern is largely obsolete — hooks replaced the need for container components. The lessons should be updated to reflect this history honestly: "this was the pattern; hooks replaced it with custom hooks." Three lessons on an obsolete pattern is excessive. Reduce to one historical-context lesson and redirect the remaining content toward custom hooks (see section 3).

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **TypeScript** | TypeScript is expected at virtually every junior frontend interview and is the default for new React projects in 2025. Without it, a graduate of this tier cannot contribute to most production codebases without a significant self-teaching gap. Minimum coverage: interfaces/types, prop typing, event typing, generics basics. | New topic in Module 9 (tooling) or Module 1 (React foundations) |
| **useEffect** (dedicated topic) | The most commonly misunderstood React hook. Currently buried in `loading_states`. Needs its own topic covering: what a side effect is, dependency array semantics, cleanup function pattern, async-inside-useEffect, and common pitfalls (infinite loops, stale closures). | New topic between `use_state` and `derived_state` in Module 2 |
| **Custom Hooks** (dedicated topic) | Custom hooks are the primary composition mechanism in modern React and appear in virtually every junior interview. Currently one lesson inside `separation_of_concerns`. Needs 2–3 dedicated lessons with worked examples. | New topic in Module 6 |
| **Server State / React Query basics** | Juniors using raw fetch + useEffect for data fetching are writing code that most teams have replaced with TanStack Query or SWR. A single introductory lesson showing the problem it solves and the `useQuery` pattern would prevent immediate mismatch with real codebases. | New lesson at end of Module 5 (API Integration) |
| **TypeScript with React** | Even a single lesson showing typed props, typed useState, and typed event handlers would close the gap between this curriculum and the real-world expectation. | New lesson in Module 1 (components) after basic props |
| **useReducer** | For complex local state (multi-field forms, state machines in simple components), `useReducer` is a natural next step from `useState`. It is a common interview question and appears in real codebases. | New lesson in Module 2 after `use_state` |

---

## 7. PRACTICE & ASSESSMENT

Practice quality is strong across this tier. The guided steps include realistic code questions (write the useState declaration, write the JSX for a component), not just recognition tasks. The distinction between fill-blank and short-text questions is well-applied — fill-blank for specific syntax, short-text for explanation. The `microCheckpoint` pattern (appearing in several lessons) adds a fast knowledge check mid-lesson, which is pedagogically sound.

The most significant practice gap is in testing: the component testing module should include a full worked example of writing a test from scratch, including the `render`, `userEvent`, `expect` pattern. Currently the lessons describe what RTL is and why it exists, but the learner may not write a complete test before the capstone.

The capstone (fe-jun-m10-01) correctly requires architecture decisions before coding ("draw your component tree"), which is exactly the right forcing function. The rubric item "API data is fetched via a service module (not raw fetch in components)" is an excellent professional standard introduced at the junior level.

One assessment gap: there is no mid-module checkpoint project. A learner completes 9 modules before the capstone. A small mid-point project (after Module 5, once API integration is covered) would consolidate learning before the final sprint.

---

## 8. Prioritized action list

1. **ADD** — TypeScript as a dedicated topic with 3+ lessons (interfaces, prop typing, event typing) — highest-impact gap relative to employability
2. **ADD** — `useEffect` as a dedicated topic in Module 2 (currently buried in `loading_states`)
3. **ADD** — Custom hooks as a dedicated topic with 2–3 worked examples (`useFetch`, `useLocalStorage`)
4. **ADD** — `useReducer` lesson in Module 2 (common interview question; prerequisite for Redux concepts at senior)
5. **ADD** — Server state introduction lesson (TanStack Query `useQuery` pattern) at end of Module 5
6. **UPDATE** — `use_state` lesson to cover functional update form (`setState(prev => ...)`)
7. **UPDATE** — `rest_consumption` to cover manual HTTP status checking and Authorization headers
8. **UPDATE** — `eslint` to mention `eslint-plugin-react-hooks` and `exhaustive-deps` rule
9. **CHANGE** — Reduce `presentational_vs_container` from 3 lessons to 1 historical-context lesson; redirect to custom hooks
10. **ADD** — Mid-module consolidation project after Module 5 (API integration complete, before architecture patterns)
