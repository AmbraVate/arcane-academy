---
id: phy-sen-m5-01
domainId: physics
tier: SENIOR
moduleId: phy-sen-m5
moduleTitle: "Module 5: Senior Project"
moduleGlyph: "🔬"
moduleSortOrder: 5
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
title: "The Simulation Forge"
sortOrder: 1
xpReward: 500
practiceType: NONE
questType: SYNTHESIS
feynmanPrompt: "Walk a junior student through your Forge project end to end: the system you chose, the physics you kept and discarded, how you marched the equations, how you knew the march was honest, and what happened when your simulation finally faced real measurements."
learningObjectives:
  - Design and execute a complete computational investigation of a physical system from model to validated conclusion
  - Apply the full Senior toolkit — dynamics, fields or modern physics, numerical marching, uncertainty-bearing data analysis — in one coherent project
  - Communicate a scientific investigation honestly: assumptions stated, verification shown, uncertainties carried, limitations owned
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines a clear investigation: a real physical system, a precise question about it, and a model with explicitly stated keep/discard decisions justified by magnitude"
    - "Executes a numerical march appropriate to the system and demonstrates verification: a known-answer check, a step-halving convergence test, or a conservation watchdog (ideally more than one)"
    - "Treats simulation output and any comparison measurements as data: means, uncertainties, and an agreement judgement based on overlapping ranges"
    - "Reports honestly: states the model's domain of validity, owns at least one limitation or surprise, and draws only conclusions the evidence supports"
  keywords: [model, assumption, march, verification, convergence, uncertainty, validation, limitation]
  modelAnswer: |
    For my Forge I investigated the large-angle pendulum — the exact system Magus Selka
    used to introduce the wall, because its true equation has no closed-form solution.
    My question: how does the period grow with release angle, and at what angle does
    the small-angle formula err by more than 5%?

    Model: point bob, rigid massless rod, no air drag, no pivot friction. I justified
    each discard by magnitude — drag and friction change the period far less than my
    measurement precision over a few swings, though they dominate amplitude over many
    minutes, so my model is valid only for period questions over short runs. That
    boundary went in the notebook before any computation.

    March: Euler steps proved untrustworthy — my energy watchdog showed steady drift —
    so I shortened the step until drift over ten swings fell below 0.1%, and confirmed
    with a step-halving test that the period had converged to four figures.
    Verification: at 5° release, my march reproduced the small-angle formula's period
    within 0.05% — a known-answer pass. Then the production runs: periods at release
    angles from 10° to 90°.

    Results: the period grows with amplitude — slowly at first, then steeply; by 90°
    it runs about 18% over the small-angle prediction, and the 5% error line falls
    near 50°. Validation: I timed the brass pendulum at three release angles, ten
    repeats each, reporting each mean ± its scatter. At 20° and 45° the simulation sat
    inside my error bars; at 70° it sat just outside — and rather than stretch the
    claim, I recorded the miss and its likely cause: at large angles the bob moves
    fastest, so the discarded drag does its greatest work there.

    My conclusion claims exactly what the evidence supports: a validated period-amplitude
    curve up to ~50°, a documented model boundary beyond it, and a refinement
    (add drag) queued for the next loop. The Forge taught me the lesson under the
    lesson: the notebook — assumptions, seeds, parameters, failures — IS the project;
    the curve is merely its receipt.
guidedSteps:
  - id: phy-sen-m5-01-g1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      Choose your Forge system and stake your claim. In two or three sentences: name the
      real physical system you will simulate, state the precise question you will ask of
      it, and identify which piece of your Senior physics it exercises (advanced dynamics,
      fields, modern physics, or a system from an earlier tier now treated honestly —
      e.g. large-angle pendulum, projectile with drag, two-body orbit, charged particle
      in a field, radioactive decay statistics).
    inputConfig:
      placeholder: "System, question, and the physics it exercises..."
    markingRule:
      matchMode: CONTAINS
      accepted: ["?"]
      rejectedFeedback: "A Forge project needs all three stakes in the ground: a named system, a question precise enough to answer with a number or a curve (phrase it as an actual question, with a question mark), and the physics it draws on. 'Simulate a pendulum' is a hobby; 'at what release angle does the small-angle formula err by 5%?' is an investigation."
    hint: "The strongest questions are quantitative and falsifiable: 'how does X vary with Y?', 'at what value of A does B happen?' Make sure a literal question appears in your answer."
    reflectionPrompt: "Could a measurement, even in principle, prove your simulation wrong? If not, sharpen the question until it could."
  - id: phy-sen-m5-01-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Your march is implemented and producing smooth, plausible-looking trajectories.
      According to the Forge protocol, what must happen BEFORE you run production
      simulations and start answering your question?
    inputConfig:
      options:
        - "Verification: reproduce a known-answer case, pass a step-halving convergence test, and check conservation watchdogs"
        - "Nothing — smooth, plausible output is itself the evidence of correctness"
        - "Validation: compare immediately against bench measurements"
        - "Publication of preliminary results, to establish priority"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Verification: reproduce a known-answer case, pass a step-halving convergence test, and check conservation watchdogs"]
      rejectedFeedback: "Plausible is not verified — the eye forgives errors the bookkeeping exposes. Before production: aim the code at a case with a known answer (small-angle pendulum, drag-free projectile), halve the step and demand the answer hold still, and watch conserved quantities for drift. Validation against the bench comes after — it tests the model, and it can only do that once the code is above suspicion."
    hint: "Two gates, fixed order. Which gate is internal — establishing that the code solves its equations right — and where does it sit relative to the production runs?"
    reflectionPrompt: "If you skipped verification and your validation happened to succeed, what would you actually know?"
  - id: phy-sen-m5-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      The confrontation: your simulation predicts a period of 2.14 s at a 45° release.
      You time the brass pendulum ten times at 45° and get a mean of 2.16 s with scatter
      giving an uncertainty of ± 0.03 s.

      Deliver the verdict in one or two sentences: do simulation and experiment agree,
      and on what grounds?
    inputConfig:
      placeholder: "Verdict and grounds..."
    markingRule:
      matchMode: CONTAINS
      accepted: ["agree"]
      rejectedFeedback: "The measurement's range runs 2.13–2.19 s, and the prediction 2.14 s sits inside it: simulation and experiment agree — the difference of 0.02 s is smaller than the uncertainty, so it is noise, not discrepancy. Agreement is always a verdict about ranges, never about matching digits."
    hint: "Build the measurement's interval (mean ± uncertainty) and ask whether the prediction falls inside it. The grammar is from your data analysis lesson."
    reflectionPrompt: "Suppose the measurement had been 2.26 ± 0.03 s instead. What would your next three moves be — and in what order?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Your verified, converged simulation disagrees with your careful, repeated measurements by far more than the error bars. The Forge protocol says the most likely culprit and first move is..."
    options:
      - "The model — revisit your keep/discard decisions, because a discarded effect is probably dominant in this regime"
      - "Reality — measurements that disagree with verified code should be retaken until they comply"
      - "Despair — the project has failed"
      - "The error bars — widen them until agreement appears"
    correctIndex: 0
    feedback: "Verification cleared the code; honest analysis cleared the data; what remains under suspicion is the model's triage — some discarded physics (drag, friction, large-angle terms) is bigger than assumed in this regime. That is not failure; it is the refinement loop doing exactly its job. Options (b) and (d) are the two classic frauds, named so you recognise them from the inside."
  - type: MULTIPLE_CHOICE
    question: "Why does the Forge demand a notebook recording assumptions, parameters, code version, and failures — not just the final curve?"
    options:
      - "Tradition — the Academy has always required notebooks"
      - "Reproducibility: without the rerun kit, the result is an unrepeatable anecdote, and the failures are the evidence that the gates were actually kept"
      - "To prove time was spent on the project"
      - "Notebooks are only needed for experiments, not simulations"
    correctIndex: 1
    feedback: "The curve says what you found; the notebook says why anyone should believe it — and lets any rival, or your own future self, regenerate every figure. The recorded failures (the drifting Euler march, the step sizes that didn't converge) are not embarrassments; they are the audit trail of verification actually performed."
---

# Hook

Every Senior before you faced a Forge. Magus Selka's was a star's collapsing core, marched on an engine that filled a room. Hers before that — so the Deep Laboratories' ledger says — was the three-body waltz that Poincaré locked and numerics picked.

Yours begins now. One real system. One precise question. One model, triaged and confessed; one march, verified before it is believed; one confrontation with measured reality, judged by the only grammar that can judge it. No new physics will be taught today — you hold everything you need, and have for one lesson exactly. The Forge does not test whether you learned the four modules. It tests whether they have become one instrument in your hands.

# Lore Introduction

At dawn the Deep Laboratories carry a new name. Over the door, fresh-chiselled: **THE SIMULATION FORGE**. Inside, the room has been set for one occupant: the difference engine, humming and ready; the brass pendulum and a cabinet of apparatus — springs, charged spheres, the inclined launching ramp, a sealed and clicking sample; the forty-year ledger for reference; and your notebook, its first page bearing the protocol you wrote last night.

Selka stands by the door, already wearing her travelling cloak.

"The rules of the Forge are three," she says. "First: the question is yours to choose, but it must be precise enough that nature could refuse it. Second: the gates are not optional — I will read your verification before I look at a single result, and your error bars before any conclusion. Third —" she touches the notebook "— everything goes in the book. The failures especially. A Forge notebook with no failures in it is a notebook that is lying."

She opens the door, and pauses in it. "I will return in seven days to read what you have built. You will be tempted, around the fourth day, when the march misbehaves and the bench disagrees with the engine, to believe you have failed. Remember this: the disagreement is the most informative object in this room. Mercury disagreed by forty-three arcseconds, Senior. Build well."

The door closes. The Forge is yours.

# Core Learning

## Concept Introduction

**The Forge brief.** Design, execute, and report a complete computational investigation:

1. **Choose a system and a question.** A real physical system you can model *and* measure (or for which reference data exists). The question must be quantitative and falsifiable: *how does the period grow with release angle? where does the drag-laden projectile land? what curve do a thousand decay-clicks trace?* Strong candidates: the large-angle pendulum (the wall itself), a projectile with realistic drag, a two-body orbit, a charged particle steered through fields, decay statistics against the half-life law.
2. **Model it, out loud.** The triage: keep dominant physics, discard the rest *with magnitude-based justification, in the notebook*. State the domain of validity you expect — before computing, while you are still honest.
3. **March it.** Implement the time-stepping. Choose Δt by evidence, not hope.
4. **Verify before believing.** Known-answer case reproduced; step-halving convergence passed; conservation watchdogs quiet. All three if the system allows. *No production runs before the gate.*
5. **Run, and treat output as data.** Production runs recorded — parameters, code version, seeds. Results carry uncertainties where they have them.
6. **Validate.** Confront the bench: repeated measurements, mean ± scatter, agreement judged by overlapping ranges. A miss outside the bars is not failure — it is the loop's signal to refine, and *documenting it honestly scores higher than stretching a claim*.
7. **Report.** Question, model and confessed discards, verification evidence, results with uncertainties, the validation verdict, the model's measured boundary, and what the next refinement loop would do.

**What the Forge actually examines.** Not the polish of the final curve — the *integrity of the chain*: whether each craft hands honestly to the next, whether the gates were kept in order, whether the conclusion claims exactly what the evidence supports and not one decimal more. The classic Forge failures are not technical: they are production-before-verification, digits compared instead of ranges, the inconvenient run quietly omitted, the limitation discovered and unrecorded. Every one of them has a name in your notebook's protocol, written in your own hand, last night.

## Why It Matters

The Forge is the working shape of an entire profession in miniature: computational physicists, simulation engineers, climate and reactor modellers walk exactly this loop, under exactly these gates, with careers and certifications resting on the notebook discipline. It is also the Academy's hinge: everything below it taught you physics; everything above it — the Lead tier's research design, experimental methods, and scientific computing at scale — assumes you can already run this loop alone. And it is the final answer to a question planted in your first Apprentice measurement lesson: *how do we know?* You now own the full chain of custody — from law, through model, through march, through measurement, to a claim with its uncertainty attached — and you know where every link can break, because you have broken and mended each one yourself.

## Worked Examples

**Example 1 — A Forge walked end to end (the wall itself).** *Question:* at what release angle does the small-angle period formula err by 5%? *Model:* point bob, rigid rod, no drag — valid for period-over-few-swings only; boundary noted. *March:* Euler drifts (energy watchdog barks); step shortened and convergence confirmed by halving. *Verification:* 5° release reproduces the formula within 0.05%. *Production:* periods from 10° to 90°; the curve climbs to +18% at 90°, crossing 5% near 50°. *Validation:* bench timings at three angles, ten repeats each — inside the bars at 20° and 45°, just outside at 70°, recorded with its suspected cause (drag does most work where the bob moves fastest). *Conclusion:* validated curve to ~50°, boundary documented, drag-term refinement queued. Every claim sized to its evidence.

**Example 2 — Choosing well.** Three candidate questions: (a) "simulate the solar system" — no question, no falsifiability: rejected. (b) "does my decay march reproduce the half-life law from individually random decays?" — precise, exercises modern physics and statistics, validates against the clicking sample: strong. (c) "where does a drag-laden projectile land, and how does the answer move as launch speed grows?" — precise, validates against ramp-and-tape measurements, makes the Apprentice-to-Senior arc visible: strong. The choice itself is the first graded act of modelling.

**Example 3 — The productive miss.** A Forge of years past: orbit march, verified clean, yet the simulated two-body period missed the reference data by 2% — stubbornly, at every step size. The student's notebook traced it to the model: the "fixed" central body actually moves (both bodies orbit their common centre — Junior gravitation, fine print). One refinement loop later: agreement to four figures. The miss, honestly chased, *was* the project — and the examiner's highest mark that year went to that notebook.

## Common Mistakes

- Choosing a demonstration instead of a question — if no measurement could refuse it, it is not an investigation
- Production runs before verification — every subsequent number is unaccountable, and day-four debugging becomes archaeology
- Tuning Δt until the answer "looks right" against the bench — that is fitting the method to the data; convergence is established against the *mathematics*, validation against the *world*, and never the reverse
- Comparing digits instead of ranges at validation — the grammar of agreement is overlap of uncertainties; anything else is numerology
- Stretching the claim past the evidence — "validated to 50°" is a result; "validated" unqualified is a lie of omission once 70° has missed
- The unrecorded failure — the drifting march and the discarded model belong in the notebook; a Forge book without failures is testifying falsely
- Treating the miss as catastrophe — outside-the-bars disagreement, chased honestly, is the loop working; despair and data-widening are the two classic non-answers

## Mental Model

The Forge is a relay with four runners you have already trained separately. The *modeller* runs the first leg and hands off a confessed simplification; the *marcher* runs the second and hands off verified numbers; the *analyst* runs the third and hands off results wearing their ±; the *reporter* runs the anchor leg and must carry everything — including every stumble — across the line in public. The baton is the notebook. Drop it at any handoff and the race is void no matter how fast the legs were run — and the judges, here and everywhere after, read the baton, not the stopwatch.

## Mini Summary

- One investigation, end to end: precise falsifiable question → confessed model → verified march → uncertainty-bearing analysis → range-based validation → claim sized exactly to evidence
- The gates are ordered and mandatory: verification before production, validation before conclusion
- Misses outside the error bars are the loop's signal to refine — documented honestly, they are worth more than stretched agreement
- The notebook is the project: assumptions, parameters, code version, seeds, and especially the failures — the rerun kit that makes the result science

# Guided Practice Quest

The Forge is quiet; the engine hums; the protocol page waits. Three acts open your seven days. First, stake the claim: system, question, physics — precise enough that nature could refuse it; the notebook's second page is its home. Second, stand at the first gate: your march runs and its output is smooth and plausible — establish what plausible is worth, and what must be proven before production begins. Third, rehearse the confrontation: a predicted period, ten bench timings, one ± — deliver the verdict in the grammar of ranges, because in seven days you will deliver a real one, and Selka will read the grounds before the verdict.

# Solo Practice Quest

Execute your Forge and write the report (this is the project deliverable; 350–500 words for the report itself, with your notebook as its foundation). Required structure: the question and why it is falsifiable; the model with keep/discard decisions and magnitude justifications; the march and its parameters; verification evidence — known-answer case, convergence test, and watchdog results; production results with uncertainties; the validation verdict, judged by overlapping ranges against your measurements or reference data; the model's demonstrated domain of validity; at least one limitation, surprise, or failure, recorded honestly with its lesson; and the single next refinement the loop would take. Close with one sentence answering the question your Apprentice self asked at the first measurement bench: *how do we know?*

# Integration

**Mathematics:** The Forge is applied numerical analysis under statistical judgement: convergence rates certify the march, the 1/√N law sizes your bench campaign, and the agreement verdict is interval arithmetic doing epistemology. Every theorem you used arrived through a door some earlier lesson opened — the project's mathematics is the tier's mathematics, load-bearing at last.

**Engineering:** Seven days, fixed apparatus, ordered gates, and a deliverable audit trail: the Forge's constraints are a working simulation engineer's constraints at academy scale. The notebook discipline is version control and V&V documentation by their older names — and the habit of refusing production runs before verification is, in industry, sometimes the difference between a certification and an inquiry.

# Lore Conclusion

On the seventh evening, Selka returns with the frost still on her cloak. She does not ask what answer you found. She asks for the notebook.

She reads it the way you have learned to read data — without hurry, without mercy, dwelling longest on the pages where things went wrong: the drifting march, the angle that missed the bars, the discard that came back to matter. At last she closes it.

"Apprentices learn what the world does," she says. "Juniors learn the laws it does it by. Seniors —" she sets the notebook down between you, "— Seniors learn how we *know*, and what knowing costs, and how to keep the books so the knowing can be checked. You have kept honest books, Senior. The Forge finds them worthy."

From her cloak she draws a small iron key, old and plain. "Beyond the Deep Laboratories there is one more hall. No benches. No engine. Its instruments are questions no one has answered yet, and its examiners do not work for the Academy — they are the frontier itself: chaos and emergence, research and its ethics, technologies still being born, and the leading of those who will build them. The Lead tier does not teach physics, Senior. It teaches *physicists*." She places the key in your hand and, for the first time you can remember, bows. "The Guild will be watching your ascent with interest. I already am."

*— End of the Senior Tier. The Lead tier and the final hall await. —*
