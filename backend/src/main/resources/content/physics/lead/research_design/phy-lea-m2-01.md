---
id: phy-lea-m2-01
domainId: physics
tier: LEAD
moduleId: phy-lea-m2
moduleTitle: "Module 2: Research Physics"
moduleGlyph: "🧪"
moduleSortOrder: 2
topicSlug: research_design
topicTitle: "Research Design"
topicSortOrder: 1
title: "Research Design: Choosing Questions Worth Your Years"
sortOrder: 1
xpReward: 150
practiceType: NONE
questType: MASTERY
feynmanPrompt: "Explain to a senior student what makes a research question good — important, tractable, and falsifiable — how a literature review and pre-registration protect a project, and why Hamming's question ('why aren't you working on the important problems?') stings."
learningObjectives:
  - Evaluate research questions against the three-way test of importance, tractability, and falsifiability
  - Design a study skeleton: hypothesis, predictions, controls, power, and analysis plan fixed before data arrives
  - Explain how literature review, pre-registration, and kill criteria protect researchers from waste and self-deception
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Applies the three-way test: importance (does the answer change anything?), tractability (can it be attacked with reachable methods at this scale of resources?), falsifiability (what observation would refute it?)"
    - "Designs a sound skeleton: a hypothesis with stated predictions, controls that isolate the effect, a feasibility/power estimate, and an analysis plan committed before data collection"
    - "Explains the protective institutions: literature review (don't re-fight settled battles; find the live edge), pre-registration (commit predictions and analysis to prevent post-hoc story-fitting), kill criteria (defined conditions for abandoning the line)"
    - "Engages honestly with Hamming's challenge: important problems vs. merely available ones, and the courage/timing trade-off in problem choice"
  keywords: [falsifiable, tractable, important, hypothesis, control, pre-registration, literature, kill criteria]
  modelAnswer: |
    A research question earns years of a life only if it passes three tests at once.
    Importance: does any decision, theory, or capability change depending on the
    answer — and would anyone besides me care? Tractability: can the question be
    attacked with methods I can actually reach, at my scale of resources, in bounded
    time — is there a first experiment I could start this season? Falsifiability: what
    specific observation would prove me wrong? A question failing the first test wastes
    a career on trivia; failing the second wastes it on a wall; failing the third was
    never science. The art lives in the tension: the most important questions are often
    intractable, the most tractable trivial. Hamming's needle — why are you not working
    on the most important problem in your field? — is meant to sting, and the honest
    answer is to work where importance and tractability overlap: important problems
    with a newly opened door (a fresh instrument, method, or dataset) that makes them
    attackable THIS decade.

    Design then turns the question into a skeleton that can survive contact with data.
    State the hypothesis and derive its predictions — numbered, quantitative where
    possible — before measuring. Design controls that isolate the claimed effect: the
    comparison condition that differs in only the one variable. Estimate feasibility
    and power: given my expected noise (Senior data analysis), how many measurements
    until the effect, if real, stands clear of the error bars — and can I afford them?
    And fix the analysis plan in advance: which quantities, which cuts, which
    statistical tests. Pre-registration — committing predictions and analysis publicly
    before data arrives — exists because the alternative is the garden of forking
    paths: with enough post-hoc freedom, noise can always be dressed as discovery.

    Two more protections complete the design. The literature review is reconnaissance,
    not ritual: it finds what is settled (don't re-fight it), what failed (don't
    re-walk it blind), and where the live edge runs — most 'new' ideas are old ideas
    with the failure reports unread. And kill criteria, set at the start: the
    conditions under which I abandon this line — a milestone missed, an effect bounded
    below interest. Sunk costs recruit good scientists to dead projects every year;
    the version of me who sets the exit rules now, before attachment, is the version
    best qualified to set them.
guidedSteps:
  - id: phy-lea-m2-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Rate this proposed research question: "I will investigate whether the universe is
      fundamentally mathematical in nature."
    inputConfig:
      options:
        - "Excellent — it is profound and important"
        - "It fails the design test: however important, it implies no observation that could refute it and no tractable first experiment"
        - "It fails only because it would be expensive"
        - "It is a good question because nobody has answered it"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It fails the design test: however important, it implies no observation that could refute it and no tractable first experiment"]
      rejectedFeedback: "Profundity is not a design property. Ask the two working questions: what measurement would count AGAINST it (falsifiability), and what could you begin this season (tractability)? This question offers neither — it is philosophy, worth discussing over dinner and unworthy of a lab. Research questions must be refutable and attackable, not merely deep."
    hint: "Apply the three-way test in order. It may pass 'important' — what about 'what observation would refute it?' and 'what's the first experiment?'"
    reflectionPrompt: "Rewrite one 'deep' question you care about into a form that could actually fail. What did the rewrite cost, and what did it buy?"
  - id: phy-lea-m2-01-g2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      A researcher collects her data first, then explores it freely — trying different
      variable combinations, subgroup cuts, and statistical tests until something
      emerges below the significance threshold — and publishes that as her finding.

      In one or two sentences: what is wrong with this procedure, and what practice
      exists specifically to prevent it?
    inputConfig:
      placeholder: "The flaw, and the preventive practice..."
    markingRule:
      matchMode: CONTAINS
      accepted: ["pre-regist", "preregist", "in advance", "before the data", "before data", "forking"]
      rejectedFeedback: "With enough post-hoc analytic freedom — the garden of forking paths — pure noise will eventually clear any threshold somewhere; hunting until it does, then reporting only the catch, manufactures discoveries from scatter. Pre-registration prevents it: hypotheses, predictions, and the analysis plan are committed publicly BEFORE data arrives, so the data can only confirm or refute — not be re-interrogated until it confesses."
    hint: "Recall the Senior data lesson's cherry-picking warning, scaled up: if you try twenty analyses at the 1-in-20 significance level, what do you expect to 'find'? What commitment, made when, would block this?"
    reflectionPrompt: "Exploratory analysis is still legitimate science — what is the honest way to report a pattern you found by exploring?"
  - id: phy-lea-m2-01-g3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      Richard Hamming used to ask colleagues: "What is the most important problem in
      your field — and why aren't you working on it?" What is the productive resolution
      to the discomfort this question causes?
    inputConfig:
      options:
        - "Work only on the single most important problem, whatever the odds"
        - "Aim where importance and tractability overlap: important problems made newly attackable by a fresh door — an instrument, method, or dataset"
        - "Ignore importance — work on whatever is easiest to publish"
        - "Importance cannot be judged, so the question is meaningless"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Aim where importance and tractability overlap: important problems made newly attackable by a fresh door — an instrument, method, or dataset"]
      rejectedFeedback: "Hamming's own answer: great scientists work on important problems THAT HAVE BECOME ATTACKABLE — they watch for the moment a new tool or idea opens a door, and walk through early. Charging the most important wall regardless of doors wastes careers; so does a lifetime of safely publishable trivia. The skill is recognising when 'important' and 'tractable' have just begun to overlap."
    hint: "Hamming's colleagues who answered 'because it's too hard' and those who never asked 'is this important?' were making opposite errors. Where do the two tests point together?"
    reflectionPrompt: "Name one historical example where a new instrument suddenly made an old important question tractable — and who walked through the door first."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The primary purpose of a literature review at the design stage is..."
    options:
      - "To pad the eventual paper's bibliography"
      - "Reconnaissance: learn what is settled, what was tried and failed, and where the live edge of the question runs"
      - "To find quotations supporting your hypothesis"
      - "To identify competitors to outrun"
    correctIndex: 1
    feedback: "The review is military scouting, not ceremony: settled ground need not be re-fought, failed routes need not be re-walked blind, and the live edge — where the field's knowledge actually ends — is where your question must be aimed. Most 'novel' proposals are old ones with the failure reports unread."
  - type: MULTIPLE_CHOICE
    question: "Why set kill criteria — explicit conditions for abandoning a research line — at the project's START?"
    options:
      - "Funders require them"
      - "Because sunk costs and attachment grow with time: the start-of-project self is the most objective judge you will ever have of when to stop"
      - "To make the project easier to cancel by administrators"
      - "Good projects never need to be abandoned"
    correctIndex: 1
    feedback: "Two years in — career invested, identity entangled — every researcher is a biased judge of their own line. The cure is to let the unattached start-of-project self set the exit rules: 'if the effect is bounded below X by milestone Y, we stop.' It is the modelling cycle's honesty applied to the project itself."
---

# Hook

Richard Hamming, who shared a building with Nobel laureates at Bell Labs, made lunch tables miserable with one question: *"What is the most important problem in your field — and why aren't you working on it?"* Colleagues stopped sitting with him. The question would not go away.

It stings because every researcher knows the quiet truth: most scientific effort goes not to the important questions but to the *available* ones — questions sized for the next paper, shaped like the last project, safe from failure because they were safe from mattering. And yet the opposite vice is just as fatal: charging the deepest wall in the field with bare hands wastes careers as surely as trivia does. Between them runs a narrow, learnable craft — finding questions important enough to deserve your years, tractable enough to yield them, and sharp enough to *fail*. Every tool you own now answers questions. Today is about aiming.

# Lore Introduction

The second ring's first tablet bears its chalked question mark and, beneath it, the palimpsest — a list written, struck out, and rewritten in generations of hands. Vael lets you read it. Some entries you recognise as triumphs from your own lessons: *why do the elements glow in lines?* — struck through, with a date. *What carries light through emptiness?* — struck through. Others are crossed out differently, with a small Guild sigil you haven't seen before.

"The strikes with dates are answers," Vael says. "The sigils are *abandonments* — questions the Guild concluded were unanswerable as posed, or not worth their cost. Both kinds of strike are victories. The tablet's failures are the entries with neither mark." She points to several faded lines, never struck, never resolved, the chalk simply aging. "Forty years, some of those. Good minds. The questions were deep, the holders devoted — and nothing in the design could ever have *failed*, so nothing could ever conclude. The Frontier Hall's unkindest exhibit, Lead: careers spent on questions that could not lose."

She hands you a fresh slate. "Selka certified that you can answer. This module asks the prior thing — whether you can *ask*. There is a three-way test, a skeleton every sound study shares, and a set of protections the Guild adopted after learning, expensively, how researchers deceive themselves. We begin with the test."

# Core Learning

## Concept Introduction

**The three-way test.** A question deserves serious resources only if it passes all three:

- **Important:** Does the answer *change* anything — a theory's fate, a capability, a decision? The working probe: write the two possible outcomes and ask who acts differently in each case. If nobody does, the question is decoration.
- **Tractable:** Is there a credible attack with methods you can reach, at your scale of resources, in bounded time? The probe: *what is the first experiment, and could it start this season?* Importance without a route is a wall, not a project.
- **Falsifiable:** What specific observation would prove the idea *wrong*? A question no data could refute is not yet science — and a career spent on one can never conclude. (The tablet's saddest entries all failed here.)

The tests fight: the deepest questions are often intractable; the most tractable, trivial. **Hamming's resolution** is the field's best career advice: aim where importance and tractability *newly overlap* — important problems that a fresh instrument, method, or dataset has just made attackable. Great careers are largely the craft of noticing doors as they open (spectroscopy opened the atom; the CCD opened the dark sky; cheap computation opened complexity — your own Module 1) and walking through *early*.

**The skeleton: design before data.** A question passing the test is then built into a study whose verdict will mean something:

1. **Hypothesis → predictions.** State the claim; derive what it implies, numbered and quantitative where possible — *before* measurement.
2. **Controls.** Design the comparison that isolates the effect: conditions differing in only the variable under test. (The control arm is what separates "we saw X after Y" from "Y causes X" — and its absence is the first thing a referee checks.)
3. **Feasibility and power.** From expected noise (your Senior ± craft) and the 1/√N law: how many measurements until a real effect stands clear of the error bars? Can you afford them? An *underpowered* study — too few measurements to detect even a true effect — is a machine for producing ambiguity at full cost.
4. **Analysis plan, fixed in advance.** Which quantities, which exclusions, which tests. The plan is part of the design, not a decision to make once you've seen what the data "wants" to show.

**The protections.** Three institutions exist because researchers — honest, careful researchers — reliably deceive themselves without them:

- **Literature review as reconnaissance.** Find what is settled (don't re-fight it), what was tried and failed (don't re-walk it blind — failure reports are the cheapest experience money can't buy), and where the *live edge* runs, because that is where your question must sit. Most "novel" ideas are old ones with the failure literature unread.
- **Pre-registration.** Commit hypotheses, predictions, and the analysis plan publicly *before* data arrives. The threat it answers is the **garden of forking paths**: given post-hoc freedom over variables, subgroups, cuts, and tests, pure noise will eventually clear any significance threshold somewhere — and reporting only that path manufactures discovery from scatter (the Senior cherry-picking warning, scaled to careers). Exploration remains legitimate — but is *reported as exploration*, generating hypotheses for the next pre-registered test, not as confirmation.
- **Kill criteria.** Set, at the start, the conditions under which the line is abandoned: a milestone missed, an effect bounded below interest, a cost ceiling crossed. Two years in, sunk costs and identity make every researcher a biased judge of their own project; the unattached start-of-project self is the most objective referee you will ever have access to. Hire them.

## Why It Matters

Question choice is the highest-leverage decision in a research career — it bounds everything later excellence can achieve, and it is the explicit business of grant panels, lab directors, and funding agencies, all of whom run versions of the three-way test on every proposal (Module 4's leadership lessons inherit this directly: choosing problems *for a team* is this lesson with stakes multiplied). The protections are not academic etiquette: the replication crisis — celebrated findings across multiple sciences evaporating on retest — traces substantially to forking-path analysis and missing pre-registration, and physics' own escapes (blind analysis at LIGO and the particle collaborations, next lesson's territory) are this lesson institutionalised. And the kill-criteria discipline compounds: the field's cautionary tales are not usually wrong answers but *unfalsifiable commitments* — decades-long programmes that no observation could end. The researcher who can ask "what would change my mind?" of their own life's work is rarer than talent, and worth more.

## Worked Examples

**Example 1 — The test applied three ways.** *Candidate A:* "Is the universe fundamentally mathematical?" Important, arguably; falsifiable, no — no observation counts against it; *fails*. *Candidate B:* "Does our cantilever's resonant frequency drift with humidity?" Falsifiable and supremely tractable; important — only if anything depends on it (it might, for a sensor product; it doesn't, as physics); *fails or passes on context*. *Candidate C:* "Do neutrino masses follow the normal or inverted ordering?" Important (it gates theories of matter's origin), falsifiable (the orderings predict different oscillation signatures), tractable *now* (current detectors are reaching the sensitivity) — *passes*, and is in fact a live frontier programme. The test is a sieve, and most questions die in it: that is the sieve working.

**Example 2 — Power, on the back of an envelope.** Hypothesis: a coating shifts a pendulum's period by ~0.1%. Your timing scatter is 0.5% per measurement. From 1/√N: to push the mean's uncertainty to 0.05% (so a real 0.1% shift stands two uncertainties clear) needs N ≈ (0.5/0.05)² = 100 timings per condition — an afternoon; *proceed*. Same arithmetic with scatter 5%: N = 10,000 — a month of timing for a marginal verdict; *redesign* (reduce scatter first) *or decline*. Ten minutes of Senior arithmetic, run before commitment, is the difference between a study and a gamble.

**Example 3 — The door that opened.** Gravitational waves were *important* from 1916 (Einstein's prediction) and *falsifiable* always — but intractable for seventy years: no instrument approached the required 10⁻²¹ strain sensitivity. Laser interferometry's maturation through the 1990s opened the door; the physicists who had spent decades readying for that moment (designing LIGO while the technology grew toward feasibility) walked through it in 2015, and the discovery now anchors a new astronomy. Hamming's overlap, on the grandest scale: the question waited; the *tractability* arrived; the prepared walked through first.

## Common Mistakes

- Choosing by availability — questions shaped like the last project, sized for the next paper; Hamming's sting names exactly this
- Choosing by depth alone — unfalsifiable profundity spends careers without ever being able to lose; if no observation counts against it, it cannot conclude
- Skipping the failure literature — re-walking documented dead ends because the review only searched for support
- Running underpowered — too few measurements to detect even a true effect: full cost, guaranteed ambiguity; do the 1/√N arithmetic *before* committing
- Letting the data choose the analysis — the forking-path garden converts noise into findings; fix the plan first, and report exploration as exploration
- Designing without controls — "X happened after Y" is an anecdote until the comparison that isolates Y exists
- No exit rules — projects without kill criteria are ended by exhaustion or funding, never by evidence; set the rules while you are still objective

## Mental Model

Designing research is fitting out a ship before an unmapped crossing. The three-way test picks the destination: somewhere worth reaching (important), reachable by a hull you can actually build (tractable), and *locatable* — defined precisely enough that you will know whether you arrived or wrecked (falsifiable). The skeleton is the ship itself: hypothesis as heading, controls as the keel that keeps wind from being mistaken for current, power analysis as provisions arithmetic — enough water for the whole crossing or none of it matters. Pre-registration is the sealed logbook: course declared before departure, so storms cannot be renarrated as the plan. And kill criteria are the turn-back line, drawn on the chart in harbour — because the captain mid-ocean, provisions sunk and pride aboard, is the worst-placed person in the world to decide when the crossing has failed.

## Mini Summary

- The three-way test: important (the answer changes something), tractable (a reachable first attack exists), falsifiable (a specific observation could refute it) — all three, simultaneously
- Hamming's overlap: aim at important problems newly made attackable by fresh doors — instruments, methods, datasets — and walk through early
- The skeleton: hypothesis → numbered predictions, isolating controls, power arithmetic before commitment, analysis plan fixed before data
- The protections: literature as reconnaissance (settled ground, failure reports, the live edge), pre-registration against forking paths, kill criteria set by your most objective self — the one who hasn't started yet

# Guided Practice Quest

Vael props the palimpsest tablet where you can see the unstruck entries while you work. "The Guild's design examination. First: a candidate question — *whether the universe is fundamentally mathematical* — grade it against the three tests, and be precise about which it fails and why the failure is fatal. Second: a researcher who collects first and hypothesises afterward, touring the garden of forking paths until something confesses — name the flaw and the institution built to prevent it. Third: Hamming's needle — resolve it, not with comfort, but with the working rule about doors and overlap. Then look once more at the faded entries, Lead. Every one of them passed somebody's test for *deep*. Not one was ever able to fail."

# Solo Practice Quest

Write a research prospectus (350–500 words) for a question you might genuinely pursue — physics or any field your Module 1 toolkit reaches. State the question and grade it honestly against the three-way test, including what observation would refute your hypothesis and what makes the question attackable *now* (name the door). Build the skeleton: hypothesis with at least two numbered predictions, the control that isolates your effect, and a back-of-envelope power estimate using the 1/√N law with stated noise assumptions. Add the protections: three things your literature reconnaissance must establish before you commit, the analyses you will pre-register, and two explicit kill criteria with dates. Close with one paragraph answering Hamming directly: why is *this* the most important problem you can tractably attack — and if it is not, what stops you from attacking that one?

# Integration

**Mathematics:** Power analysis is statistics run forward — the 1/√N law and significance thresholds composed into a feasibility theorem before any data exists — and the forking-paths problem is the multiple-comparisons theorem wearing a trench coat: test twenty hypotheses at the one-in-twenty level and expect one false discovery by construction. Decision theory formalises kill criteria as optimal stopping; Bayesian design formalises "what would change my mind?" as expected information gain.

**Engineering:** Engineering institutionalised design-before-build long ago — requirements documents, design reviews, and stage gates are the skeleton and kill criteria with legal standing — and research borrows back the discipline: big-science projects (detectors, telescopes, fusion experiments) pass formal design reviews where the three-way test is applied by committees with budgets. The power estimate is the researcher's version of the engineer's margin calculation: done on paper, before the metal is cut.

# Lore Conclusion

Vael takes your prospectus slate and reads it twice — the second time, you notice, checking only the falsifiability clause and the kill criteria.

"A question that can fail, held by a researcher who has named the day they'd let it," she says. "The Guild has funded careers on thinner evidence of wisdom." She sets the slate beside the palimpsest, where the contrast does its own teaching. "But a question well-chosen is a private virtue, Lead. Science is not private. Your design will be judged by referees, your claim weighed by rivals, your finding used — or misused — by people who will never read your methods section. The frontier's next craft is older than any of its instruments: *being understood, precisely, by people you cannot supervise*."

She uncovers the second tablet of the ring. On it, chalked in a fine hand: a single sentence, then the same sentence revised, then revised again — seven drafts, each shorter than the last. "Tomorrow: *Scientific Communication* — papers, talks, figures, and the peer review that makes claims trustworthy. The discovery that cannot be communicated, Lead, functionally never happened. Bring the prospectus. We are going to teach it to speak."
