# Arcane Academy — Restructure Plan

## Vision

A degree-level education platform across any topic/pathway, with scientifically-backed memory
retention (spaced repetition), optional gamification, and optional lore — accessible, rigorous,
and catalogue-first.

---

## Lesson Flow (7 Steps)

| Step | Name | Description |
|------|------|-------------|
| 1 | Hook & Objectives | Narrative hook + explicit learning objectives |
| 2 | Learning Content | Core teaching with infographics to articulate concepts visually |
| 3 | Guided Practice | Step-by-step walkthrough — what to consider, how to approach |
| 4 | Solo Practice | Apply what was learned independently |
| 5 | Knowledge Check | 3–5 MCQs, no retake, silently scored 0–1 per question |
| 6 | Teach Back | Explain/teach a chosen concept in own words; marked by the application |
| 7 | Common Mistakes | Gotchas, edge cases, extra information (shown if content present) |

### Teach Back scoring strategy (per domain type)

| Domain type | Marking method |
|-------------|---------------|
| Coding | Code execution — output matched against expected result (Phase 4) |
| Science / Math | Structural: required concepts + relationships present |
| Humanities / Psychology | Keyword/phrase pattern matching against model answer |

Teach Back produces a completeness score (0.0–1.0):
- ≥ 0.90 → high retention contribution
- 0.60–0.89 → partial; retention score discounted
- < 0.60 → low; concept flagged for reinforcement

---

## Spaced Repetition & Retention System

### Intervals

Retrievals triggered at: **Day 1 → Day 7 → Day 14 → Day 30**

Shown to the user on login, **before** continuing new learning. Capped at **8 questions per
session**, prioritised by: lowest retention score first, then most overdue.

### Scoring model

Each question has:
- `retention_score` (0.0–1.0)
- `attempt_history[]` — last N attempts (1 = correct, 0 = wrong)
- `stability_state` — UNVERIFIED | UNSTABLE | STABLE | RETAINED | WEAKENED

#### State transitions

| Trigger | Transition |
|---------|-----------|
| Lesson MCQ answered | score set; state = UNVERIFIED |
| 3 consecutive correct retrievals | state = RETAINED; interval extends to 30-day maintenance |
| Retrieval wrong | reset interval to Day 1; state = WEAKENED; score −0.2 |
| Missed login day | reset interval to Day 1; state = WEAKENED; score −0.2 |
| High variance (1-0-1-0 over 4 attempts) | state = UNSTABLE; effective retention treated as 0.3 |
| Low variance + upward trend (0-1-1-1) | state = STABLE |

#### Guessing vs improvement

- 3+ attempts needed to distinguish patterns
- `UNSTABLE` pattern (1-0-1) = likely guessing; retention kept low until 3 consecutive correct
- `STABLE` upward pattern (0-1-1) = genuine improvement; score climbs
- Definitive confidence reached by Day 30 (3 spaced correct answers impossible by luck alone)

### Retention score vs Teach Back score

Both feed into the concept's overall retention. A concept is only marked `RETAINED` when:
- 3 consecutive correct retrievals **and**
- Teach Back score ≥ 0.90 (or ≥ 0.70 if Teach Back is structural/coding, which is harder)

---

## Data Model (Phase 2)

```
question_attempts
  id, user_id, question_id, lesson_id, score (0/1),
  attempted_at, interval_day (1|7|14|30), is_retrieval (bool)

concept_retention
  id, user_id, lesson_id,
  retention_score DECIMAL(3,2),
  stability_state ENUM(UNVERIFIED, UNSTABLE, STABLE, RETAINED, WEAKENED),
  attempt_history JSONB,          -- last 4 attempt scores
  next_review_date DATE,
  teach_back_score DECIMAL(3,2),
  consecutive_correct INT,
  created_at, updated_at
```

---

## Domain Tier Labels (Phase 3)

Internal enum stays `APPRENTICE / JUNIOR / SENIOR / LEAD`.
Display labels are domain-specific, stored as `tier_labels JSONB` on the `domains` table.

Example — Psychology:

```json
{
  "APPRENTICE": "Trainee Psychologist",
  "JUNIOR": "Qualified Practitioner",
  "SENIOR": "Senior / Highly Specialist Psychologist",
  "LEAD": "Principal / Consultant / Clinical Lead"
}
```

Default (tech domains): `{ "APPRENTICE": "Apprentice", "JUNIOR": "Junior", "SENIOR": "Senior", "LEAD": "Lead" }`

---

## Content Format Changes

### Current → New markdown structure

| Current section | New location |
|----------------|--------------|
| `# Hook` | Step 1 (Hook) |
| YAML `learningObjectives` | Step 1 (Objectives, already frontmatter) |
| `## Concept Introduction` | Step 2 (Learning Content) |
| `## Why It Matters` | Step 2 (Learning Content) |
| `## Worked Examples` | Step 2 (Learning Content) |
| `# Guided Practice Quest` | Step 3 trigger (guidedSteps already in frontmatter) |
| `# Solo Practice Quest` | Step 4 |
| YAML `microCheckpoint` | Step 5 (Knowledge Check — expand to 3–5 Qs) |
| Teach Back | **NEW** — YAML `teachBack` block per lesson |
| `## Common Mistakes` | Step 7 — **move from inside Core Learning to `# Common Mistakes` H1** |
| `## Mental Model` | Step 2 (Learning Content) |
| `## Mini Summary` | Step 2 (Learning Content) |
| `# Integration` | Step 2 (connects to other domains) |
| `# Lore Introduction` / `# Lore Conclusion` | Shown only if lore toggle ON |

### Migration approach

- New lessons authored to the new spec immediately
- Existing lessons migrated incrementally (not a blocker for Phase 1)
- Validation test (`MarkdownLessonValidationTest`) updated per phase

---

## Phased Roadmap

### Phase 1 — Lesson flow restructure (UI)
**Goal:** reorder `EncodingPage` phases to match the 7-step flow

- [ ] Rename/reorder phases: Hook+Objectives → Learn → Guided → Solo → Knowledge Check → Teach Back → Common Mistakes
- [ ] Knowledge Check: discrete MCQ step, no retake, score stored silently (schema: simple table, no retention logic yet)
- [ ] Teach Back: text submission, PATTERN_MATCH for all domains (coding = same until Phase 4)
- [ ] Common Mistakes: conditional render if lesson has `# Common Mistakes` section
- [ ] Remove FSRS/self-rating confidence checklist
- [ ] Update `MarkdownLessonValidationTest` — add `# Common Mistakes` as optional (warn, not fail)

### Phase 2 — Retention + spaced repetition
**Goal:** daily retrieval queue, per-question scoring, stability detection

- [ ] Flyway migration — `question_attempts`, `concept_retention` tables
- [ ] Backend: `RetentionService` — score update, stability transitions, missed-login weakening
- [ ] Backend: `RetrievalQueueService` — build daily queue (cap 8, priority algorithm)
- [ ] Backend: login hook — queue injected into session response if items due
- [ ] Frontend: retrieval modal on login (shown before dashboard if queue non-empty)
- [ ] Frontend: teach back score feeds into concept_retention

### Phase 3 — Domain tier labels
**Goal:** per-domain professional tier names from DB

- [ ] Flyway migration — add `tier_labels JSONB` to `domains`
- [ ] Seed default labels for all existing domains
- [ ] Backend: expose tier labels on domain API response
- [ ] Frontend: replace hardcoded tier strings with domain-sourced labels
- [ ] Admin UI: edit tier labels per domain

### Phase 4 — Code execution sandbox (Teach Back, coding domains)
**Goal:** run learner-submitted code, diff output against expected

- [ ] Evaluate Piston API vs Judge0 (self-hosted) — decision needed
- [ ] Backend: `CodeExecutionAdapter` (calls Piston/Judge0)
- [ ] Backend: `TeachBackScoringService` — routing by domain type (code vs structural vs pattern)
- [ ] Frontend: code editor (Monaco or CodeMirror) in Teach Back step for coding domains
- [ ] Teach Back scores feed into Phase 2 retention model

### Phase 5 — Optional features (roadmap, not scheduled)
- [ ] Gamification toggle (XP, badges, streaks shown/hidden per user preference setting)
- [ ] Lore toggle (lore sections hidden if off — conditional render, no content change)
- [ ] Unlockable mini-games per module (lesson-linked, gated by module completion)
- [ ] Enhanced notes
- [ ] Remove rabbit hole feature
- [ ] Tutorial (origami lesson) — keep, review after flow changes

---

## Known Constraints

- Code execution sandbox is non-trivial infrastructure — keep in Phase 4, not before
- ~132 physics lessons + 350+ other lessons need `# Common Mistakes` section — migrate incrementally
- `microCheckpoint` currently has 2 questions; spec calls for 3–5 — new lessons should hit this, existing backfilled
- Lore and gamification toggles require user preferences fields in DB (coordinate with Phase 2 migration)
- Daily retrieval "before learning" UX needs careful design — must not feel punishing; progress indicator important

---

## Decisions Made

| Decision | Choice | Reason |
|----------|--------|--------|
| Retrieval question bank | Same questions as lesson MCQs | Authoring burden vs proven testing-effect science |
| Missed login handling | Reset to Day 1 + mark weakened | No stacking; clean slate |
| Teach Back for coding | Code execution (Phase 4) | Keyword matching insufficient for code correctness |
| Tier label storage | `tier_labels JSONB` on `domains` table | Admin-editable, domain-specific, future-proof |
| Daily retrieval cap | 8 questions | Prevent login punishment; prioritise weakest concepts |
| Guessing detection | Variance over last 4 attempts | 3 consistent correct needed for RETAINED |
| FSRS self-rating | Removed | Subjective; replaced with silent 0/1 scoring |

---

## Out of Scope (for now)

- AI-based marking (Teach Back marked by application rules, not LLM)
- Social/collaborative features
- Native mobile app
- External certification / accreditation
