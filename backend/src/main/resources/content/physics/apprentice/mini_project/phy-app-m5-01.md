---
id: phy-app-m5-01
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m5
moduleTitle: "Module 5: Apprentice Project"
moduleGlyph: "🏗️"
moduleSortOrder: 5
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
title: "The Pendulum Trials"
sortOrder: 1
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
learningObjectives:
  - Design and run a complete experimental investigation independently
  - Apply measurement, fair-testing, graphing, and uncertainty analysis to one system
  - Draw and defend a conclusion limited to the evidence collected
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Investigation has a clear, falsifiable question with identified variables (independent, dependent, controls)
    - Timing method reduces random error (multiple swings per reading, repeats averaged)
    - Results table has units in headers and includes all raw readings
    - Graph is correctly constructed with line/curve of best fit
    - Conclusion states the relationship found, the tested range, and uncertainty
    - At least one systematic error is identified, with its likely direction of bias
    - The energy story of the pendulum (PE ↔ KE, dissipation) is correctly told
    - Reflection honestly identifies the investigation's weakest point and an improvement
  keywords: [pendulum, period, length, fair test, average, uncertainty, graph, conclusion, energy]
  modelAnswer: |
    A complete investigation asks one falsifiable question — typically "how does a pendulum's
    period depend on its length?" — and isolates it: length varied, period measured, mass and
    swing-size controlled. Random timing error is tamed by timing 10 swings per reading and
    averaging repeats; results are tabled with units and graphed (period against length gives
    a curve; period against √length, a straight line through the origin for the ambitious).
    The classic findings: period grows with length (roughly doubling for quadruple length),
    while mass and modest swing-size leave it unchanged. The conclusion claims only the tested
    range, quotes the spread as uncertainty, names the reaction-time and length-measurement
    biases, and tells the energy story — PE and KE trading each swing, the total slowly
    dissipating to air and pivot. The reflection names the weakest link and one concrete
    improvement.
guidedSteps:
  - id: phy-app-m5-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You want to test how a pendulum's length affects its period. Which design is a FAIR test?
    inputConfig:
      options:
        - "Vary the length AND use heavier bobs for longer pendulums"
        - "Vary only the length; keep the bob, release angle, and counting method identical; repeat each length several times"
        - "Time one swing once at each of two lengths"
        - "Vary the release angle while keeping length fixed"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Vary only the length; keep the bob, release angle, and counting method identical; repeat each length several times"]
      rejectedFeedback: "One independent variable (length), everything else controlled, with repeats to tame random error — the fair-test recipe from Module One, now bearing the whole project's weight."
    hint: "How many things may change at once in a fair test?"
    reflectionPrompt: "Why is 'time 10 swings and divide' part of nearly every good design here?"
  - id: phy-app-m5-01-g2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      State your project hypothesis as a falsifiable prediction about length and period (and, if you wish, about mass). One or two sentences.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [longer, period, increase, length, mass, same, unchanged]
      rejectedFeedback: "A falsifiable form: 'Increasing length from 20 cm to 80 cm will increase the period; doubling the bob's mass at fixed length will leave the period unchanged (within uncertainty).' Specific, testable, riskable."
    hint: "Name the variable, the direction of effect you expect, and what result would prove you wrong."
    reflectionPrompt: "What result would genuinely surprise you — and what would you do upon finding it?"
  - id: phy-app-m5-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Before you build: tell the pendulum's energy story for one complete swing, and explain why real swings shrink over minutes. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [potential, kinetic, convert, highest, lowest, drag, friction, dissipat, heat]
      rejectedFeedback: "At the swing's ends: maximum PE, zero KE; at the bottom: maximum KE, minimum PE — the two accounts trade fully twice per cycle. Air drag and pivot friction skim a little energy to heat each pass, so amplitude decays while (usefully for you) the period barely changes."
    hint: "Where is it highest? Fastest? And where do the missing joules go?"
    reflectionPrompt: "Why does the dissipation NOT ruin your period measurements?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Timing 10 complete swings and dividing by 10 beats timing one swing because:"
    options:
      - "Pendulums need a warm-up"
      - "Your fixed reaction-time error is spread across ten periods, shrinking its effect on each by tenfold"
      - "Ten swings are more accurate for the pendulum"
      - "It looks more scientific"
    correctIndex: 1
    feedback: "The ±0.2 s human error lands once on a 15-second measurement instead of once per 1.5-second swing — design beating reflexes, exactly as Module One promised."
  - type: MULTIPLE_CHOICE
    question: "Your period-vs-length graph curves upward-but-flattening. The honest conclusion is:"
    options:
      - "Period is proportional to length"
      - "Period increases with length, but less than proportionally, over the tested range"
      - "The experiment failed because the line isn't straight"
      - "Length has no effect"
    correctIndex: 1
    feedback: "Report the shape you found, bounded by your range. (The deeper truth — period ∝ √length — is exactly what such a curve hints at; plotting against √L and finding a straight line through the origin is the project's optional triumph.)"
---

# Hook

In 1583, a nineteen-year-old medical student sat in Pisa Cathedral, bored by the sermon, watching a great bronze lamp swing on its chain. Long, lazy arcs at first; small tired ones later. Using the only timer he possessed — his own pulse — Galileo noticed something nobody had: *the swings all seemed to take the same time, large or small*. He went home, built pendulums, and tested it. That bored observation became the heartbeat of every precision clock for the next three centuries.

Now it's your turn in the cathedral. The Pendulum Trials are the Apprentice tier's consolidation project: one humble system — a weight on a string — investigated with *every tool you've earned*. Measurement and uncertainty. Fair-test design. Tables, graphs, and gradients. Forces, energy, and honest conclusions. No new physics is taught in this module. That's the point: this time, the physics is yours.

# Lore Introduction

The summons arrives written in three hands: Thorne's precise script, Liora's quick slanting one, Calde's charcoal block letters. *The Trials are convened.* In the pendulum hall the great bronze pendulum has been stilled, and beneath it stands a bare workbench: string, an assortment of bobs, a stand, a chronometer, a measuring scale, and a fresh logbook stamped with the Academy's seal. The three masters take seats in the gallery, and say nothing. That is the format of the Trials, Thorne explained the night before: *"We have talked for four modules. Now the apparatus talks, and you translate. We intervene only if you endanger the chronometer or yourself. Your question, your design, your numbers, your verdict — defended aloud at dusk."* Liora had added, grinning: "The pendulum has tricked finer minds than yours. It is honest, but it is *subtle*." And Calde: "Mind the knots. Effective length, apprentice. Effective length."

# Core Learning

## Concept Introduction

**The brief.** Investigate experimentally how a simple pendulum's **period** (time for one complete swing, there and back) depends on its **length** — and, if time allows, establish what the period does *not* depend on (mass; modest swing size). Deliver a written report and defend it.

**The toolkit assembled** — this project is the syllabus in concert:

| Module | What it contributes here |
|--------|--------------------------|
| Foundations | Units, uncertainty, repeats and averaging, fair-test design, tables, graphs, honest conclusions |
| Mechanics | Forces on the bob (weight, tension), the restoring force; PE ↔ KE each swing; dissipation to heat |
| Waves | Period and frequency (T = 1/f) — the pendulum as an oscillator, kin to every wave you studied |
| Matter & Heat | Where the "lost" energy goes; even thermal expansion of your string, if you're fastidious |

**Design notes from four centuries of pendulum-testers:**

- **Effective length** runs from the pivot to the bob's *centre*, not to the knot. Measure accordingly; this is the classic systematic error.
- **Time 10 swings, divide by 10** — your ±0.2 s reflex lands once per 15 s, not once per 1.5 s. Count from the *centre* of the swing (fastest crossing — sharpest timing mark), starting the count at "zero", not "one" (the second classic error).
- **Repeats**: three timings per length minimum; mean ± half-range as your uncertainty.
- **Range**: five lengths spanning at least a factor of four (e.g. 20–80 cm) — wide enough for the shape of the relationship to show itself.
- **Controls**: same bob, same modest release angle (small swings — Galileo's regularity holds best there), same counting method, same observer.

**What you should find** (sealed orders — verify, don't assume): period *grows* with length, but gently — quadrupling the length roughly *doubles* the period. Mass: no effect, within uncertainty (the free-fall lesson's ghost: gravity scales its force to the customer). The ambitious plot: **T against √L** — if your curve straightens into a line through the origin, you have found the law's true shape, and your gradient is a number with a story (it hides g — the full theory is Senior-tier treasure, but your data can knock on its door today).

## Why It Matters

- This is the assessment format of real science: design, execute, analyse, defend — the cycle you'll repeat at every tier with grander apparatus.
- Pendulum physics is working heritage: clock escapements, seismometer hearts, structural-sway dampers in skyscrapers, and the measurement of g itself.
- The habits under test — honest uncertainty, systematic-error hunting, conclusions sized to evidence — are precisely what separates measurement from anecdote, in any career.

## Worked Examples

**Example 1: A model results table**
Length 0.40 m (pivot to bob-centre, ±0.005 m). Time for 10 swings: 12.6 s, 12.8 s, 12.7 s → mean 12.70 s → **T = 1.27 s ± 0.01 s**. Note everything recorded: the ±, the method, the raw trio preserved. Five such rows make the project's spine.

**Example 2: Reading the graph's shape**
Plot T against L: points at (0.2, 0.90), (0.4, 1.27), (0.6, 1.55), (0.8, 1.79) — rising, flattening. Not proportional (0.8 m is four times 0.2 m; 1.79 s is about *twice* 0.90 s — there's the quadruple-length-double-period signature). Re-plot T against √L: (0.45, 0.90), (0.63, 1.27), (0.77, 1.55), (0.89, 1.79) — a straight line through the origin, gradient ≈ 2.0 s per √m. The curve confessed under the right change of axes — Module One's finest trick, deployed.

**Example 3: The defence, done well**
Gallery question (Thorne, inevitably): "Your periods are all slightly long. Why?" Strong answer: "Two candidate systematic errors, both lengthening: I measured to the knot, not the bob's centre — understating L by ~1 cm, which actually *shortens* predicted periods, so that's the wrong sign; but my count started at 'one' on release for the first two trials — counting 9 swings as 10 would shorten, not lengthen... so the likeliest culprit is timing from the swing's *end* rather than its centre, where the bob lingers and my mark drifted late. I re-timed length 0.40 from the centre crossing: 12.62 s, closer to prediction." That — error hunting with directions and a retest — is what the gallery came to hear.

## Common Mistakes

- **Measuring to the knot** — effective length ends at the bob's centre; Calde warned you twice.
- **Counting "one" on release** — the first swing isn't complete at release; count "zero... one... two..." or you'll time nine swings as ten.
- **Large release angles** — the small-swing regularity frays beyond ~15–20°; keep releases modest and consistent.
- **A two-point "relationship"** — two lengths cannot reveal a shape; five points minimum, spread wide.
- **Conclusions beyond the range** — "period always doubles when..." — you tested 20–80 cm; claim 20–80 cm.
- **Hiding messy data** — anomalies are annotated, investigated, retested — never deleted. The gallery always asks.

## Mental Model

A project is **a voyage you charter yourself**. The four modules were sailing lessons in sheltered water — navigation (measurement), rigging (forces), reading weather (waves and heat). The Trials cast off the line: same boat, same skills, open water, *no hand but yours*. The pendulum is a kindly first sea — honest, repeatable, subtle enough to punish sloppiness but never treacherous. What's actually being examined is not the pendulum at all. It's whether the sailor and the boat have become one thing. Sail it like the logbook will outlive you — because in the Archive of Trials, it will.

## Mini Summary

- ✔ One falsifiable question; one variable freed; everything else controlled and repeated
- ✔ Time 10 swings from the centre crossing, count from zero, divide — design beats reflexes
- ✔ Effective length: pivot to bob-centre; the knot is the classic trap
- ✔ Five lengths, wide range, mean ± half-range, units in every header
- ✔ Expect: T grows ~with √L; mass irrelevant — and claim only your tested range
- ✔ The defence wants error directions, energy stories, and conclusions sized to evidence

# Guided Practice Quest

Work through the guided steps to lock your design, commit your hypothesis to falsifiable form, and tell the pendulum's energy story before the first swing — then build.

# Solo Practice Quest

**The Pendulum Trials.** Build a simple pendulum (string, a small dense bob, a firm pivot — a door frame, shelf bracket, or stand). Then deliver the full investigation as a written report: (1) **Question and hypothesis**, falsifiable, with variables classified. (2) **Method** a stranger could follow exactly — lengths chosen, controls fixed, the 10-swing centre-crossing protocol. (3) **Results**: the complete raw table, units in headers, anomalies annotated. (4) **Analysis**: T-vs-L graph with best curve; the T-vs-√L replot if you accept the challenge, with gradient. (5) **Conclusion**: the relationship, the tested range, the uncertainty — and the mass verdict if you tested it. (6) **Evaluation**: your two most serious error sources with *directions of bias*, the single best improvement, and the energy audit of one swing including where the amplitude's joules went. Sign and date it — the Archive expects it.

# Integration

**Mathematics**: The pendulum's √-law is your first encounter with a *power law* extracted from data by linearising axes — the working method of experimental science from Galileo to particle physics. The gradient of your straightened line is 2π/√g in disguise: your humble string has measured a property of the planet.

**Engineering**: Pendulum discipline built the longitude clocks that mapped the world's oceans, and its modern descendants damp the sway of the tallest towers (a 660-tonne pendulum rides inside Taipei 101). Every engineer's prototype test — vary one thing, log everything, distrust your own timing — is your Trials protocol, scaled up.

# Lore Conclusion

At dusk you stand beneath the gallery and defend: the question, the curve, the straightened line, the knot you nearly measured to and didn't, the count begun at zero, the energy of each swing audited down to the warm whisper of the pivot. The masters' questions come — Thorne on systematic bias, Liora on the period's kinship with her tuning forks, Calde, naturally, on where the heat went — and your logbook answers. There is a silence. Then Thorne descends, takes your report, and walks to the Archive of Trials, placing your logbook beside the scattered, honest slate displayed there for years. "Apprentice tier: complete," he says, and the words land heavier than any equation. Liora presses a small brass token into your hand: a pendulum bob, drilled and threaded on a cord, twin to the dosage-bead. "For the next gate," she says. Calde just grins. "Junior tier, apprentice. Momentum, machines, lightning in a wire. The Foundry's *good* furnaces." The great bronze pendulum, as you leave, has been set swinging again — keeping the Academy's time, as it has for nine hundred years, one honest period after another.
