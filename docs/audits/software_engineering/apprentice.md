# Audit — Software Engineering · Apprentice

**Auditor lens:** Principal Engineer reviewing onboarding / graduate-track curriculum for foundations adequacy
**Tier mandate:** Completing this tier should enable a learner to begin junior developer work — they can write, reason about, and debug basic Java programs using variables, control flow, loops, methods, OOP basics, and collections.
**Scope:** 83 lessons across 20 topics (plus 1 stray legacy file in `foundations_of_computation/`).

---

## 1. Verdict at a glance

The Apprentice tier is the strongest tier in the pathway in terms of lesson craft. Individual lesson quality is excellent: each lesson has a compelling hook, strong analogies (chef's counter for RAM, label printer for assignment), worked examples, common-mistakes sections, retrieval questions, and a well-structured solo rubric. The lore/gamification layer is consistent and motivating without being intrusive. Coverage of the core Apprentice mandate is solid — variables, control flow, loops, methods, basic collections, and OOP fundamentals are all present and sequenced logically. However, three structural problems reduce the overall score: (1) a stray legacy file sits in `foundations_of_computation/variables_and_state/` with a different schema (no `id` field, no `difficulty`, different front-matter format), (2) the topic `maps` is significantly under-developed at only 2 lessons compared to arrays (5) and lists (4), and (3) there is no explicit coverage of `String` methods/manipulation — a constant practical need — and no introduction to Java-specific type concepts (`null`, type casting, `instanceof`). Practice quality is high; the scaffolded guided steps, solo rubrics, and retrieval sections are the best in the entire pathway.

**Scores:** Coverage 4/5 · Rigor/Depth 4/5 · Sequencing 4/5 · Practice quality 5/5

---

## 2. KEEP — strengths to preserve

- **se-app-m2-01 / variables_and_state** — `Why Computers Need Memory`: exemplary lesson. RAM/state/volatility explained with precision, layered analogies, and a retrieval section that surfaces genuine misconceptions (RAM as permanent storage). The format should be the canonical template across all tiers.
- **se-app-m2-05 / variables_and_state** — `Assignment`: the `=` vs `==` distinction is handled with the nuance it deserves (performative vs constative), the worked examples trace execution step by step, and the regex marking rule on the code step is a good practice.
- **se-app-m1-01 / computational_thinking** — `What is Computation?`: the IPO model introduced correctly, avoids anthropomorphising the machine, and connects to mathematical tradition (Euclid's algorithm). An excellent tier-opener.
- **se-app-m6-04 / debugging** — `Reading Error Messages`: teaches stack-trace literacy precisely — exception type, message, first-own-code line — with a multi-step guided exercise on a real-looking trace. Debugging being treated as a first-class skill (not an afterthought) is exactly right.
- **se-app-m6-08 / engineering_habits** — `Naming Things`: camelCase/PascalCase/UPPER_SNAKE_CASE conventions covered with the `why` (readable code needs no comments). The philosophy integration (Austin's speech act theory) is a differentiator.
- **se-app-m5-09 / encapsulation** — `Why Hide Data?`: the BankAccount negative-balance example is a canonical motivation for encapsulation. Connects nicely to the OOP sequence.
- **se-app-m7-01 / mini_project** — `The Console Companion`: well-scoped integration project. The scaffolding (class skeleton, method signatures, switch dispatcher) is appropriately levelled — not too much hand-holding, forces learner to assemble the pieces. The acceptance checklist is rigorous.

---

## 3. CHANGE — restructure / resequence

- **`foundations_of_computation/variables_and_state/variables_intro.md`** — This file uses an older schema (`sw-eng-app-variables-intro` id, no `difficulty`, no `sortOrder`, different `soloAssessment` format). It is unreachable via the standard module navigation and conflicts with the proper `variables_and_state` lessons in `se-app-m2`. **Recommendation:** Delete or archive this stale file; it creates a duplicate with divergent metadata.
- **`maps` (2 lessons: se-app-m4-10, se-app-m4-11)** — `maps` is positioned as a peer collection to arrays and lists but receives less than half the lesson count. HashMap is practically important (it is used in the junior ORM and REST topics). **Recommendation:** Expand to at least 4 lessons to match the `lists` depth: (1) what is a Map / key-value model, (2) HashMap operations (put/get/remove/containsKey), (3) iterating over entries/keys/values, (4) common use cases (frequency count, lookup table).
- **`objects_in_the_real_world` (se-app-m5-01, -02, -03)** — These three lessons introduce OOP conceptually before `classes` introduces syntax. This is a reasonable pedagogical choice but the transition between the concept lessons and the Java syntax lessons in `classes` has a gap: there is no bridge lesson explaining how a Java class IS the "blueprint" concept introduced here. **Recommendation:** Add a short bridge lesson in `classes` that explicitly maps the conceptual vocabulary (blueprint, instance, field, behaviour) to Java keywords (`class`, `new`, fields, methods).
- **`errors` (se-app-m6-01, -02, -03)** and **`debugging` (se-app-m6-04, -05, -06, -07)** — Both topics live in Module 6 but "errors" (compile vs runtime vs logic) should precede "debugging" as a prerequisite. The current file IDs suggest they already are ordered this way, but the topic slugs do not make this dependency explicit in the frontmatter. **Recommendation:** Add `prerequisites: [se-app-m6-03]` to `se-app-m6-04`.

---

## 4. UPDATE — depth / rigor / currency

- **se-app-m4-01 to se-app-m4-05 / arrays** — Array lessons need one additional worked example showing what happens at the boundary: `ArrayIndexOutOfBoundsException` should be demonstrated and linked back to the `debugging` / `errors` lessons. Currently the exception is mentioned in `debugging` but not provoked and analysed in `arrays` itself.
- **se-app-m2-14 to se-app-m2-19 / control_flow** — The `switch` statement lessons should be updated to cover Java 14+ switch expressions (`switch (x) { case A -> ... }`) as the canonical modern form, not just the classic fall-through style. The mini_project already uses arrow-syntax switch, creating a consistency gap with what the learner was taught.
- **se-app-m5-04 to se-app-m5-08 / classes** — These lessons cover constructors and fields but there is no explicit treatment of `toString()`, `equals()`, and `hashCode()`. These are practically essential — they arise immediately when learners put objects into collections — and the omission will bite them in the junior tier. At minimum, a lesson on `toString()` override should be here.
- **se-app-m3-01 to se-app-m3-06 / methods** — Method overloading is likely covered but the lessons should also introduce Java's `var` keyword (Java 10+) as a readability tool for local variable type inference, since learners will encounter it in real codebases.
- **se-app-m1-06 to se-app-m1-10 / logic_foundations** — Logic/boolean content appears solid but should include De Morgan's Law explicitly (`!(A && B)` == `!A || !B`), which is a source of real bugs in conditional logic.

---

## 5. REMOVE — cut or merge

- **`foundations_of_computation/variables_and_state/variables_intro.md` (id: sw-eng-app-variables-intro)** — Stale legacy file with incompatible schema. Duplicates content covered properly in `se-app-m2-01` through `se-app-m2-08`. Should be removed entirely. No lesson ID in the proper module sequence references it.

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **String manipulation** (`substring`, `length`, `contains`, `split`, `toUpperCase`, `trim`) | Strings are the most common data type in real programs. Absent here, learners hit a wall immediately when writing anything interactive. Essential before the mini_project. | New topic in Module 2 or 4, after variables |
| **`null` — what it is, NullPointerException, null checks** | NullPointerException is the most common runtime error a learner will see. The concept of null reference is not covered at all in this tier despite being encountered in nearly every program. | New lesson in `errors` or `variables_and_state` |
| **Type casting and `instanceof`** | Basic type narrowing is required the moment learners work with collections of mixed types or polymorphism. Without it, the junior OOP content will be confusing. | New lesson in `classes` or `operators` |
| **Reading and writing files / Scanner keyboard input** | The mini_project uses `Scanner` but there is no dedicated lesson explaining it. Input/output appears conceptually in `inputs_and_outputs` but the Java API is not taught. | New lesson in `inputs_and_outputs` |
| **Basic algorithm thinking (counting, max/min, accumulator)** | The `problem_solving` topic covers decomposition and pseudocode but not the three classic accumulator patterns (sum, max, count). These underpin Big O intuition at the junior tier. | Expand `problem_solving` |

---

## 7. PRACTICE & ASSESSMENT

Practice quality in this tier is the highest in the pathway. Guided steps are well-calibrated — multiple choice for concept recognition, fill-blank for recall, CODE for application. Rubric items are specific and measurable. Retrieval sections include the high-value `mistakeId` pattern (show broken code, ask for diagnosis) which is pedagogically excellent.

Areas for improvement:
- **The mini_project (se-app-m7-01)** has no graded marking rubric for the code itself — only a reflection rubric. A learner who submits syntactically broken code that "runs without crashing" (rubric item 1) has no feedback mechanism on code quality. Adding a code quality rubric (naming, method extraction, single responsibility at class level) would close this gap.
- **Maps and encapsulation topics** have fewer practice steps than other topics; the guided steps feel thin relative to the lesson importance.
- **No cross-topic practice quests** — every solo assessment is scoped to one topic. A "bring it together" exercise before the mini_project (e.g., use a list of objects in a loop with conditional logic) would reduce the cognitive gap at the mini_project.

---

## 8. Prioritized action list

1. **REMOVE** `foundations_of_computation/variables_and_state/variables_intro.md` — stale file with incompatible schema causing potential system confusion.
2. **ADD** String manipulation topic (5 lessons) covering the core String API — the single biggest practical gap.
3. **ADD** `null` and NullPointerException lesson in `errors` or `variables_and_state`.
4. **EXPAND** `maps` from 2 to 4 lessons to match the depth of `lists`.
5. **ADD** bridge lesson in `classes` connecting OOP concepts to Java syntax.
6. **UPDATE** `control_flow` switch lessons to cover Java 14+ arrow-syntax switch expressions.
7. **ADD** `toString()`, `equals()`, `hashCode()` lesson to `classes`.
8. **ADD** type casting / `instanceof` lesson to `operators` or `classes`.
9. **UPDATE** `arrays` to include ArrayIndexOutOfBoundsException worked example linked to `debugging`.
10. **UPDATE** mini_project rubric to include code quality criteria (naming, method extraction) alongside the existing reflection rubric.
