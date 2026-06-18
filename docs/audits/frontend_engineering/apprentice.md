# Audit — Frontend Engineering · Apprentice

**Auditor lens:** Staff Frontend Engineer reviewing a foundational tier intended to onboard complete beginners to web development
**Tier mandate:** Produce learners who understand the web, can write correct semantic HTML, style with CSS (box model, flexbox, grid), author basic JavaScript (variables, functions, control flow, DOM manipulation), apply responsive design, and hold core accessibility principles — sufficient to start a junior bootcamp or structured self-study.
**Scope:** 73 lessons across 18 topics.

---

## 1. Verdict at a glance

This is a genuinely strong foundations tier. The sequence is logical (web → HTML → CSS → responsive → JS → accessibility → code quality → project), lesson quality is consistent, and the instructional design is above average: every lesson has a solo rubric, guided steps, retrieval prompts, and a `mistakeId` worked example on many of the later lessons. The integration of real engineering principles (separation of concerns, box-sizing reset, `alt` text, semantic elements) at this level is a particular strength — these are not simplified away. The mini project (Personal Portfolio, fe-app-m8-01) is impressively well-scoped with a clear rubric that would hold up at a junior interview screen. The only material weaknesses are: the absence of CSS cascade as a first-class lesson, no coverage of CSS custom properties (variables) as their own lesson despite being used in the mini project scaffold, functions are not explicitly isolated as a topic inside `javascript_basics`, and the `code_quality` topic feels slightly disconnected from the rest of the module flow.

**Coverage: 4/5 | Rigor/Depth: 4/5 | Sequencing: 4/5 | Practice quality: 5/5**

---

## 2. KEEP — strengths to preserve

- **fe-app-m2-01 (html_basics / what_is_html)** — The separation-of-concerns framing from lesson 1 is exactly right. Do not dilute it.
- **fe-app-m2-10 (semantic_html / why_semantics_matter)** — The screen-reader vs `<div>` comparison is the correct intuition pump. The rubric item connecting semantics to accessibility, SEO, and maintainability is professional-level thinking introduced early.
- **fe-app-m3-06 (layout / the_box_model)** — Box model arithmetic question plus the `border-box` reset lesson is correct. The reflectionPrompt noting this is "the first rule in almost every production CSS file" is exactly the kind of professional context that differentiates this platform.
- **fe-app-m6-04 (practical_accessibility / keyboard_navigation)** — `outline: none` as the canonical mistake, `tabindex` semantics, and the insight that semantic HTML provides keyboard accessibility for free without extra JS: all correct and at the right depth.
- **fe-app-m8-01 (mini_project / the_personal_portfolio)** — Exceptional capstone. The acceptance criteria are specific and testable, the scaffolding provides enough structure for beginners without doing the work for them, and the reflection prompt asks genuinely useful questions. The integration of psychology (Halo Effect, Ambady's research) is well-motivated. Keep this intact.
- **fe-app-m5-08 (decisions_and_loops / conditionals)** — The `=` vs `===` common mistake is exactly what every beginner writes; naming it explicitly at this stage prevents months of confusion.
- **fe-app-m1-01 (the_internet / what_is_the_internet)** — Distinguishing the Internet from the Web in the first lesson is the correct foundation. The packet analogy (tearing out pages and mailing them) is memorable.
- **fe-app-m2-05 (content_elements / headings)** — Exactly one `<h1>` per page, heading levels for structure not visual size, screen reader navigation: all canonical, all correct.
- **fe-app-m3-11 (visual_design / typography)** — WCAG 1.4.12 surfaced correctly in the reflectionPrompt. Ch units for line length is a professional touch.

---

## 3. CHANGE — restructure / resequence

- **fe-app-m3-05 (styling_basics / specificity)** — Specificity is currently the last lesson in `styling_basics`. It should be lesson 2 (after `what_is_css`), because the cascade and specificity rules determine the outcome of every multi-rule CSS file the learner writes from that point forward. Moving it earlier prevents the learner from forming incorrect mental models that have to be unwound later.
- **fe-app-m7-04 through fe-app-m7-07 (code_quality)** — The `code_quality` topic sits in Module 7, after JavaScript and before the project. Lessons here (debugging, indentation, naming) feel disconnected from the HTML/CSS/JS content that preceded them. Consider integrating code quality norms into the relevant modules rather than isolating them: naming conventions alongside `javascript_basics`, indentation alongside `html_basics`. If kept separate, rename the module "Engineering Habits" to signal that it applies across all three layers.
- **fe-app-m5-01–05 (javascript_basics)** — JavaScript functions do not appear to have their own dedicated lesson within the five lessons here. Variables, types, conditionals, loops, and DOM manipulation are all present. Functions (declaring, calling, parameters, return values) are a prerequisite for DOM event handlers and are invoked implicitly in mini-project JavaScript. Add a functions lesson between the types/variables block and decisions/loops, or confirm it exists embedded in another lesson.

---

## 4. UPDATE — depth / rigor / currency

- **fe-app-m3-02 (styling_basics / selectors)** — The cascade should be explicitly covered here, not just mentioned. The C in CSS is the source of most beginner CSS confusion. A dedicated lesson on cascade order (user-agent → author → inline) and how specificity interacts with cascade order would prevent the flood of "why isn't my style applying?" confusion that beginners invariably experience.
- **fe-app-m3-09 (layout / css_grid)** — CSS Grid deserves at least two lessons: one for the grid container (display:grid, grid-template-columns, grid-template-rows, gap) and one for grid placement (grid-column, grid-area, named lines). Five layout lessons spread across box model, positioning, flexbox, grid, and "bringing it together" is correct structurally, but grid may be one of the most complex topics here and may be thin.
- **fe-app-m4-04–07 (responsive_techniques)** — The responsive section does not appear to include CSS custom properties (variables), despite the mini-project scaffold including a full `:root` variable block. Learners who reach the portfolio and see `var(--color-primary)` without prior explanation will be confused. Add a lesson on CSS custom properties (declaration, usage, theming) — ideally in the `styling_basics` topic or `responsive_techniques`.
- **fe-app-m5-01–05 (javascript_basics)** — Arrow functions and the difference between `var`, `let`, and `const` are not explicitly visible in the lesson titles. Both are used in the mini-project JavaScript scaffold. Confirm these are covered; if not, add them. The `const` lesson is especially important to prevent `var` habituation.
- **fe-app-m7-01–03 (debugging)** — Browser DevTools usage (Elements panel, console, network tab, breakpoints) would be more useful than generic debugging concepts alone. Apprentice-level learners need to know how to inspect the DOM, read console errors, and understand what a 404 looks like in the network tab. Ensure these are concrete and hands-on.

---

## 5. REMOVE — cut or merge

- No topics warrant full removal. The tier coverage is appropriate for the mandate.
- **fe-app-m7-04–07 (code_quality)** — If the restructuring in section 3 is applied (integrating code quality into relevant modules), this topic becomes redundant as a standalone. Merge or eliminate the standalone module; distribute its lessons into html_basics, styling_basics, and javascript_basics respectively.

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| CSS Custom Properties (Variables) | Used in mini-project scaffold (`--color-primary`, `--space-4`); learners will encounter them immediately. The platform itself teaches token-based design at higher tiers — introduce the concept here. | New lesson in `styling_basics` after selectors |
| JavaScript Functions | Functions are the foundational abstraction of JS. Event handlers, DOM manipulation, and the mini-project all require them. Without a dedicated lesson the learner patches functions together from context. | New lesson in `javascript_basics` between types and decisions |
| CSS Cascade | The C in CSS. Without understanding cascade order, learners cannot reason about why a style applies or does not. Specificity (fe-app-m3-05) is present but cascade as a separate concept is not explicit. | New lesson in `styling_basics` immediately after the CSS rule structure lesson |
| HTML Forms basics | Forms are one of the most common HTML structures and a direct prerequisite for the junior tier's form handling module. Covered only implicitly in the mini-project requirements (contact form). A dedicated lesson on `<form>`, `<input>`, `<label>`, `<button>`, `<textarea>` with correct `for`/`id` pairing would close a gap. | New topic or additional lessons in `content_elements` |
| JavaScript Events (addEventListener) | Event listeners are used in the mini-project (nav toggle, aria-expanded). The `dom_manipulation` topic covers DOM reading/writing but event handling is not explicitly named as a lesson. Confirm coverage; if not present, add it before the mini-project. | New lesson in `dom_manipulation` |

---

## 7. PRACTICE & ASSESSMENT

The practice quality at this tier is the platform's strongest asset. Every lesson combines:
1. A `soloAssessment` rubric with 4–6 concrete, verifiable items
2. Guided steps with multiple choice, fill-blank, and short-text at appropriate ratios
3. `reflectionPrompt` on every guided step — connecting mechanics to professional rationale
4. `retrieval` blocks (recall/explain/mistakeId) visible in several lessons, adding spaced repetition scaffolding

Specific strengths: the `mistakeId` pattern (showing a concrete wrong code and requiring correction) is pedagogically excellent and should be confirmed present on all five-lesson topics. The `modelAnswer` blocks in `soloAssessment` are written at a quality that a human tutor would produce — specific, concise, technically correct.

One gap: the mini-project (fe-app-m8-01) has a rubric but no structured feedback mechanism or peer review model. At apprentice level, a learner may not be able to self-assess whether their CSS custom properties are "defined and used." A checklist-style self-review step (not just the rubric) would help learners audit their own output before submission.

---

## 8. Prioritized action list

1. **ADD** — CSS Custom Properties lesson in `styling_basics` (needed for mini-project scaffold; blocking gap)
2. **ADD** — JavaScript Functions lesson in `javascript_basics` (prerequisite for everything in JS module and mini-project)
3. **ADD** — CSS Cascade lesson in `styling_basics` (the missing C in CSS; high confusion source)
4. **ADD** — HTML Forms lesson in `content_elements` (direct prerequisite for junior tier)
5. **ADD** — JavaScript Events (addEventListener) lesson in `dom_manipulation` (used in mini-project)
6. **CHANGE** — Move `specificity` to lesson 2 of `styling_basics` (earlier than current position)
7. **UPDATE** — Expand `css_grid` to two lessons (container properties and item placement)
8. **UPDATE** — Add Browser DevTools hands-on content to `debugging` topic
9. **UPDATE** — Ensure `var`, `let`, `const` and arrow functions are explicitly named in `javascript_basics`
10. **CHANGE** — Integrate `code_quality` lessons into their relevant modules (`html_basics`, `styling_basics`, `javascript_basics`) rather than isolating them in Module 7
