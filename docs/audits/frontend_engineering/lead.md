# Audit — Frontend Engineering · Lead

**Auditor lens:** Principal/Staff Frontend Engineer who has been a tech lead, built and mentored teams, set frontend strategy at scale, and participated in principal-level system design discussions
**Tier mandate:** Produce engineers who can set technical direction for a frontend organisation, govern a design system, mentor engineers across levels, communicate technical decisions to business stakeholders, apply UX psychology to product decisions, and operate as a peer to engineering directors — the "Arcane Frontend Architect" title the capstone bestows.
**Scope:** 21 lessons across 22 topics (mini_project directory is empty — effectively 21 topics).

---

## 1. Verdict at a glance

This is the most intellectually ambitious and consistently excellent tier in the pathway. The shift to `AI_REVIEW` assessment is well-judged: these topics cannot be assessed with pattern-matching, and the scenario-based guided steps (write the email to the VP, leave the Socratic code review comment, diagnose the design system adoption failure) are exactly the kind of applied leadership challenges that distinguish a lead from a senior. The content across all five modules is substantive, accurate, and grounded in real organisational dynamics. The Cognitive Load Theory module (fe-lea-m3-01) applies Sweller and Miller's Law correctly to both UI and API design — a rare and valuable perspective. The stakeholder communication lesson (fe-lea-m1-04) includes the specific revenue statistics (Amazon's 100ms/$1%, Walmart 2%/1s) that a lead needs to make business cases. The capstone (fe-lea-m6-01, "The Arcane Frontend Architect") is genuinely demanding: a multi-part scenario with a 4-year-old monolith, degraded CWV, a bus factor of one, and a misaligned North Star Metric.

The weaknesses are: the `mini_project` directory exists as a topic in the directory listing but contains no files — this is a real gap (no integration project between the design system governance and the capstone). The tier covers UX psychology deeply but has limited coverage of engineering culture and hiring/interview strategy. The "product tradeoffs" and "metrics" lessons are strong but the tier's product thinking module (Module 4) is somewhat front-loaded with concepts and light on senior-to-lead application scenarios. Some lessons (change_management, versioning) overlap significantly in subject matter without explicit cross-referencing.

**Coverage: 4/5 | Rigor/Depth: 5/5 | Sequencing: 4/5 | Practice quality: 5/5**

---

## 2. KEEP — strengths to preserve

- **fe-lea-m1-01 (technical_leadership)** — "A lead who writes the most code is often the bottleneck" is the central truth of this role, stated in the opening paragraph. The influence-without-authority section, the business-language translation requirement ("performance is not a PageSpeed number; it is revenue per second of latency"), and the scenario ("PM wants two weeks; you need four; board pressure exists") are all pitch-perfect for this level. This is the best lesson on technical leadership I have seen in any curriculum.
- **fe-lea-m1-02 (team_mentoring)** — Scaffolding vs enablement distinction, the Socratic method in code review, the "Answer Machine" and "Phantom Mentor" anti-patterns, and the path from senior to lead through different-experience (not more-experience): all correct and well-articulated. The guided step (write the Socratic PR comment AND explain when you'd switch to being directive) is the right complexity for this tier.
- **fe-lea-m1-03 (frontend_governance)** — ADRs, RFC process, when to standardise vs allow autonomy, and the "governance failure mode is bureaucracy" warning are all essential lead engineer knowledge, correctly framed. The test for standardisation ("does inconsistency here cost another team time?") is exactly the right heuristic.
- **fe-lea-m1-04 (stakeholder_communication)** — The specific revenue statistics, the "capability debt" framing for technical debt, and the "accessibility as market reach" pivot are all tools a lead engineer genuinely needs. This lesson is more actionable than most leadership training materials.
- **fe-lea-m3-01 (cognitive_load)** — Applying CLT to both UI design (progressive disclosure) and component API design (40 props = high extraneous load) is the insight that distinguishes a frontend architect from a senior frontend engineer. Rare to see this connection made explicitly. Preserve this framing.
- **fe-lea-m3-03 (decision_making)** — Kahneman's dual-process theory applied to interface design, anchoring on pricing pages, loss aversion in cancellation flows, and the ethical dark-pattern boundary: this is the kind of cross-disciplinary synthesis that justifies the platform's "university-equivalent" claim. The "would a user feel helped or manipulated if they understood the mechanism?" test is the right ethical heuristic.
- **fe-lea-m4-03 (metrics)** — North Star Metric, leading vs lagging indicators, Goodhart's Law with the concrete deployment frequency gaming example, A/B test statistical significance requirements, and CWV as leading indicators for business outcomes: complete, accurate, and directly applicable.
- **fe-lea-m5-01 (enterprise_frontend_strategy)** — Conway's Law applied to frontend architecture decisions, the four architecture options with their trade-offs, the design system as a dedicated platform team product, and the dependency versioning challenge: this covers the canonical enterprise frontend questions a principal would face.
- **fe-lea-m5-04 (technology_evaluation)** — "Define the problem before choosing the tool" is the right opening. Total cost of ownership including hiring impact, migration risk, and the PoC design framework with specific exit criteria: exactly the discipline a principal engineer applies. The "lock-in question" calibration (narrow integration surface vs deep integration surface) is excellent.
- **fe-lea-m6-01 (capstone / the_arcane_frontend_architect)** — The scenario (Luminary: 8 teams, 4-year monolith, LCP 4.1s, CLS 0.22, INP 320ms, Sarah as single point of failure, NSM growing while revenue is flat) is realistic and multi-dimensional. The requirement to synthesise across performance, UX psychology, enterprise architecture, technology evaluation, and maintainability in a single submission is the correct capstone for a lead tier. The "bus factor" and NSM-revenue misalignment signals are senior-level diagnostic challenges.

---

## 3. CHANGE — restructure / resequence

- **fe-lea-m2-03 (versioning)** and **fe-lea-m2-04 (change_management)** — These two topics overlap significantly. Both cover semver, deprecation strategies, migration guides, and consuming team communication. The distinction between them is subtle and not clearly established. Consider merging into one lesson ("Design System Change Management") that covers semver, breaking vs non-breaking, deprecation timelines, communication patterns, and codemods in a single coherent treatment. The current duplication dilutes both.
- **fe-lea-m4-01 (user_needs)** → prerequisite chain — `user_needs` has `fe-lea-m3-04` (behavioural_design) as a prerequisite. This means the entire UX psychology module (Module 3) is a prerequisite for the product thinking module (Module 4). This is a strong prerequisite chain that delays `user_needs`, `business_goals`, `metrics`, and `product_tradeoffs` until the learner has completed four psychology lessons. Consider whether Module 4 lessons can stand independently, with Module 3 lessons as enrichment rather than hard prerequisites.
- **Module ordering** — The current module sequence is: Leadership → Design System Governance → UX Psychology → Product Thinking → Strategic Architecture → Capstone. An argument exists for placing Strategic Architecture (Module 5) earlier — before UX Psychology — since `enterprise_frontend_strategy` and `platform_thinking` inform how the lead engineer frames the design system governance and product thinking decisions. Consider: Leadership → Strategic Architecture → Design System Governance → UX Psychology → Product Thinking → Capstone.

---

## 4. UPDATE — depth / rigor / currency

- **fe-lea-m2-01 (system_ownership)** — The 30-50% time-to-market reduction statistic is cited without a source. At this tier, the learner needs to be able to make the ROI case in a board meeting. Replace the unsourced statistic with the Forrester/Zeroheight/Figma industry reports on design system ROI, or frame it as a model calculation rather than a claimed fact. The distinction between product team model and library model is excellent and should be retained.
- **fe-lea-m2-02 (adoption_strategies)** — The "path of least resistance" principle is correct. This lesson would benefit from a concrete case study format: a real (or realistic) organisation that achieved high design system adoption and what specifically they did. The "embedded team member for a sprint" evangelism strategy is the most actionable item here and could be expanded with implementation detail.
- **fe-lea-m4-02 (business_goals)** — Amazon's 100ms/$1% and Walmart's 2%/1s statistics are correct and well-cited in industry practice, but the lesson should note these are from studies conducted 10+ years ago and should be treated as directional rather than precise. Also: add explicit coverage of how to construct a business case when you don't have published benchmarks (A/B test design for performance experiments).
- **fe-lea-m5-02 (platform_thinking)** — The "paved road" / golden path concept is correctly described. Add explicit coverage of Developer Experience (DX) metrics: time-to-first-PR for a new engineer, number of times engineers contact platform team for help per week, and SPACE framework metrics. Without these, "measure platform success through product team outcomes" remains vague.
- **fe-lea-m5-03 (long_term_maintainability)** — Architectural fitness functions are introduced correctly (as executable rules rather than wiki guidelines). Add specific examples of what these look like in CI: ESLint `import/no-internal-modules` rule, Danger.js for PR checks, `dependency-cruiser` for visualising and enforcing boundaries. The lesson currently describes what fitness functions are; it should show what they look like in practice.

---

## 5. REMOVE — cut or merge

- **fe-lea-m2-03 (versioning) + fe-lea-m2-04 (change_management)** — As noted in section 3, these two lessons have significant overlap. Merge into one definitive lesson on design system change management. The combined content fits in a single lesson at this tier's depth.
- **mini_project directory** — The `mini_project` topic appears in the directory listing but contains no files. Either populate it with a meaningful integration exercise or remove the directory entirely to avoid confusion. See Gaps section for what this exercise should be.

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **Lead mini_project** (the directory is empty) | There is no integration exercise between the design system governance module (Module 2) and the capstone. A mid-pathway project — "Draft an ADR, an RFC, and a migration plan for a breaking change in your design system's Button component" — would consolidate governance, versioning, change management, and stakeholder communication before the full capstone. | Populate `mini_project` with a design system governance scenario |
| **Engineering Culture and Hiring** | A frontend lead is frequently involved in hiring decisions: defining what "senior frontend" means at their organisation, writing job descriptions, designing technical screens, and calibrating levelling. This is absent. A single lesson on hiring philosophy, structured interviews, and levelling frameworks would directly address a real lead responsibility. | New topic in Module 1 (Leadership) |
| **Technical Strategy Documents** | Writing and presenting a technical strategy document (not an ADR for a single decision, but a 12-month technical direction document for the frontend) is a core lead deliverable. The content implies this skill but no lesson teaches the structure, format, and communication strategy for such a document. | New topic in Module 5 (Strategic Architecture) or Module 1 |
| **Remote/Distributed Team Leadership** | The mentoring and stakeholder communication lessons assume in-person or synchronous contexts. Most lead engineers manage distributed teams. Async communication, documentation-first culture, and building trust across time zones are distinct skills. | New lesson in Module 1 (team_mentoring extension) or Module 5 |
| **Incident Management and Postmortems** | A lead engineer is expected to lead incident response for high-severity frontend issues and write blameless postmortems. This connects to the observability content in the senior tier but is absent here. The capstone scenario (LCP 4.1s, single point of failure) implies this knowledge without teaching it. | New topic in Module 5 or a cross-tier connection to senior observability |
| **OKR and Roadmap Planning** | How to translate engineering priorities into OKRs, how to protect technical health work in a product-driven planning process, and how to communicate a frontend roadmap to leadership: absent. The `business_goals` and `metrics` lessons approach this but don't address the planning process directly. | New lesson in Module 4 (Product Thinking) |

---

## 7. PRACTICE & ASSESSMENT

The `AI_REVIEW` assessment model is the right choice for this tier. The scenario-based guided steps (not just questions, but realistic workplace situations requiring nuanced written responses) are this curriculum's highest-quality practice content. The Luminary capstone scenario is genuinely excellent — multi-dimensional, realistic, and requiring synthesis across five modules with no single "correct" answer.

Specific strengths:
- The "write the email to the VP" scenario (fe-lea-m1-04) is exactly the kind of deliverable a lead must produce. Asking them to write it (not describe it) is the right exercise.
- The "write the Socratic PR comment and explain when you'd switch to directive" scenario (fe-lea-m1-02) requires both the content and the reasoning — a two-layer assessment that is appropriate for this level.
- The ADR/RFC scenario (fe-lea-m1-03) requiring a governance proposal for an organisation with five diverging teams is realistic enough to be directly applicable.

Three practice gaps:
1. **No mid-module project** — The `mini_project` directory is empty. Between the design system governance module and the capstone, there is no integration exercise. The learner completes 20 individual synthesis lessons and then faces the capstone without a prior integration attempt. A design system governance scenario (write an ADR, draft a migration guide, design an RFC process) would serve as a rehearsal.
2. **No peer review simulation** — At the lead level, the ability to give structured, growth-oriented feedback on engineering work is as important as producing it. A scenario requiring the learner to review a (provided) technical design document and write structured feedback would test this.
3. **UX psychology module (Module 3) is concept-heavy without application exercises** — The four psychology lessons (cognitive_load, user_behaviour, decision_making, behavioural_design) are excellent but the guided steps are abstract. A concrete product critique exercise ("here is a checkout flow — identify three UX psychology violations and propose specific fixes with named principles") would make the module more immediately applicable.

---

## 8. Prioritized action list

1. **ADD** — Populate `mini_project` with a design system governance scenario (ADR, RFC, migration guide for a breaking change) — the most critical gap as the only missing integration exercise
2. **ADD** — Engineering hiring and levelling lesson (defining "senior," structured screens, levelling rubrics) — direct lead responsibility currently absent
3. **ADD** — Technical strategy document lesson (how to write a 12-month frontend technical direction) — core lead deliverable not taught
4. **MERGE** — Combine `versioning` and `change_management` into one lesson; eliminate duplication
5. **UPDATE** — `system_ownership` ROI statistics: cite specific sources or frame as model calculation rather than stated fact
6. **UPDATE** — `platform_thinking` to include specific DX metrics (time-to-first-PR, platform NPS, SPACE framework)
7. **UPDATE** — `long_term_maintainability` to show fitness functions in practice (dependency-cruiser, import/no-internal-modules ESLint rule, Danger.js)
8. **ADD** — Incident management and blameless postmortem lesson — connects senior observability to lead-level operational responsibility
9. **CHANGE** — Reconsider module order: move Strategic Architecture (Module 5) before Design System Governance (Module 2) to provide strategic framing before governance specifics
10. **ADD** — OKR/roadmap planning lesson in Module 4 — how to protect technical health work in a product-driven planning cycle
