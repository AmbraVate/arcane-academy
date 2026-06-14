---
id: phy-sen-m4-04
domainId: physics
tier: SENIOR
moduleId: phy-sen-m4
moduleTitle: "Module 4: Computational Physics"
moduleGlyph: "💻"
moduleSortOrder: 4
topicSlug: scientific_computing
topicTitle: "Scientific Computing"
topicSortOrder: 4
title: "Scientific Computing: The Third Pillar"
sortOrder: 4
xpReward: 120
practiceType: NONE
questType: INVESTIGATION
feynmanPrompt: "Explain to a junior student how simulation became science's third pillar beside theory and experiment, what the full computational workflow looks like, and what disciplines make a computation trustworthy enough to base decisions on."
learningObjectives:
  - Describe the full computational science workflow from model to validated, reproducible result
  - Distinguish verification (solving the equations right) from validation (solving the right equations)
  - Explain reproducibility practices and why simulation constitutes a third pillar of science alongside theory and experiment
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes the workflow as a connected loop: model → discretise/implement → verify → simulate → analyse output as data → validate against experiment → refine"
    - "Correctly distinguishes verification (does the code solve the equations correctly — convergence tests, known-answer cases) from validation (do the equations describe reality — comparison with experiment)"
    - "Explains reproducibility: recording code versions, parameters, and seeds so others can rerun and confirm, and why an unreproducible result is weak evidence"
    - "Argues for (or thoughtfully qualifies) simulation as the third pillar with examples of science only possible computationally (climate, galaxy formation, molecular dynamics, supernovae)"
  keywords: [workflow, verification, validation, reproducibility, simulation, third pillar, convergence]
  modelAnswer: |
    Computational physics is not a bag of tricks; it is a workflow that closes into a
    loop. It begins with the modeller's triage: choose what physics to keep and state
    the assumptions. The model is then discretised — continuous equations turned into
    steppable arithmetic — and implemented as code. Before believing anything the code
    says, it is VERIFIED: aimed at problems with known answers, subjected to
    step-halving convergence tests, watched for conservation drift. Verification asks
    'am I solving the equations right?' Only then comes the production run, whose
    output is itself a dataset demanding last lesson's craft — uncertainties, fits,
    honest outlier handling. Finally VALIDATION asks the deeper question — 'am I
    solving the right equations?' — by confronting the simulation with real
    measurements. Where they disagree beyond the error bars, the model is refined and
    the loop runs again.

    Verification and validation are different duties and both are mandatory. Code can
    be perfectly correct about wrong physics (a beautifully converged frictionless
    pendulum will never match the bench), and right physics can be betrayed by buggy
    or unconverged code. Trust requires passing both gates.

    Reproducibility is the third discipline: a result that cannot be rerun is an
    anecdote with graphics. Record the code version, every parameter, every input,
    and any random seeds, so that another scientist — or you, in six months — can
    regenerate the result exactly. Modern science has been embarrassed by famous
    results that evaporated when rerun; the cure is bookkeeping, not brilliance.

    Why call simulation a third pillar? Because it answers questions neither theory
    nor experiment can reach alone. No formula solves a galaxy's formation, and no
    laboratory can hold one — yet simulated galaxies grow spiral arms matching
    telescope surveys. Climate projection, supernova cores, protein folding, reactor
    accidents we must understand without causing: all are sciences conducted in
    silico, validated against whatever fragments of reality we can measure. Theory
    proposes, experiment disposes — and simulation explores where neither can walk.
guidedSteps:
  - id: phy-sen-m4-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Your pendulum simulation reproduces the textbook small-angle period perfectly, with
      energy conserved to nine digits and answers unchanged under step-halving. But it
      still disagrees with the brass pendulum on the bench by far more than the
      measurement uncertainty. Which statement is correct?
    inputConfig:
      options:
        - "The code is verified (it solves its equations right) but the model fails validation — the equations omit physics the real pendulum has"
        - "The code must contain a bug — disagreement with reality always means broken code"
        - "The bench measurement must be wrong — verified code outranks experiment"
        - "Verification and validation are the same thing, so this situation is impossible"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The code is verified (it solves its equations right) but the model fails validation — the equations omit physics the real pendulum has"]
      rejectedFeedback: "Passing convergence tests, known-answer cases, and conservation checks establishes verification: the equations are being solved correctly. Disagreement with the bench is a validation failure: the equations themselves leave out something real — friction, large-angle effects, a stretching string. Right answers to the wrong question. The fix is the modelling loop, not the debugger."
    hint: "Separate the two questions: 'am I solving the equations right?' (convergence, conservation, known answers) and 'am I solving the right equations?' (matching reality). Which gate did this simulation pass, and which did it fail?"
    reflectionPrompt: "Describe the opposite failure: what would validated-but-unverified look like, and why is it dangerous luck?"
  - id: phy-sen-m4-04-g2
    sortOrder: 2
    inputType: SEQUENCE
    instruction: |
      Arrange the stages of the computational science workflow into working order:

      - "Validate simulation output against real experimental data"
      - "Verify the code on known-answer problems and convergence tests"
      - "Choose the model: keep dominant physics, state assumptions"
      - "Analyse the output as data, with uncertainties"
      - "Discretise the equations and implement them as code"
      - "Run the production simulation"
    inputConfig:
      items:
        - "Validate simulation output against real experimental data"
        - "Verify the code on known-answer problems and convergence tests"
        - "Choose the model: keep dominant physics, state assumptions"
        - "Analyse the output as data, with uncertainties"
        - "Discretise the equations and implement them as code"
        - "Run the production simulation"
    markingRule:
      matchMode: CONTAINS
      accepted: ['"choose the model: keep dominant physics, state assumptions","discretise the equations and implement them as code","verify the code on known-answer problems and convergence tests","run the production simulation","analyse the output as data, with uncertainties","validate simulation output against real experimental data"']
      rejectedFeedback: "The order is: model → discretise/implement → verify → run → analyse → validate (then refine and loop). Verification must precede the production run — testing the instrument before trusting its readings — and validation comes last because it compares finished, analysed output against reality."
    hint: "Modelling comes first (it decides everything downstream); verification comes BEFORE the production run — you test an instrument before you trust it; validation against reality is the final gate."
    reflectionPrompt: "Why is running production simulations before verification a false economy, even under deadline pressure?"
  - id: phy-sen-m4-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A rival academy publishes a spectacular simulation result. When your laboratory
      requests the materials to rerun it, the answer comes back: the code has changed
      since the run, nobody recorded which version produced the figures, and the
      parameter files are lost.

      In one or two sentences: what is this situation called, and why does it weaken the
      result as scientific evidence?
    inputConfig:
      placeholder: "Name the problem and say why it matters"
    markingRule:
      matchMode: CONTAINS
      accepted: ["reproduc"]
      rejectedFeedback: "The result is irreproducible — no one, including its authors, can regenerate it, so it cannot be checked, challenged, or built upon. Science's authority rests on independent repeatability; an unreproducible computation is an anecdote with graphics. The cure is bookkeeping: recorded code versions, parameters, inputs, and seeds."
    hint: "The question is whether anyone — including the authors — can run it again and get the same answer. What property of scientific results does that touch?"
    reflectionPrompt: "What would a complete 'rerun kit' for one of your own simulations have to contain?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Verification and validation, in one line each:"
    options:
      - "Verification: solving the equations right. Validation: solving the right equations."
      - "Verification: matching experiment. Validation: matching other codes."
      - "They are synonyms for testing"
      - "Verification: checking the hardware. Validation: checking the software"
    correctIndex: 0
    feedback: "The community's own slogan. Verification is internal — convergence, conservation, known answers — establishing the code faithfully solves its equations. Validation is external — confronting reality — establishing the equations faithfully describe the world. Both gates, always."
  - type: MULTIPLE_CHOICE
    question: "Which of these is a question that essentially ONLY simulation can address?"
    options:
      - "The period of a bench pendulum"
      - "The boiling point of water at sea level"
      - "How two galaxies merge over the next billion years"
      - "The resistance of a copper wire"
    correctIndex: 2
    feedback: "No formula solves a hundred billion gravitating bodies, and no experiment can hold two galaxies or wait a billion years. Simulated mergers — validated against snapshots of real colliding galaxies across the sky — are how this science is conducted at all. That reach is what earns simulation its pillar."
---

# Hook

For three and a half centuries, science stood on two pillars. **Theory**: write the laws, derive the consequences. **Experiment**: ask nature directly, and let her veto. Every lesson in your Academy career has leaned on one or both.

But consider the questions those pillars cannot hold. What will the climate do in eighty years? — no formula solves it, and there is no spare Earth to experiment on. How do two galaxies merge? — a hundred billion bodies, a billion years; no chalkboard, no laboratory. How does a star die from the inside? In one human lifetime, a third pillar has risen to carry exactly these questions: the validated, verified, reproducible *simulation*. Today you learn the full discipline — and tomorrow, in the Forge, you build one.

# Lore Introduction

The Deep Laboratories are lit end to end tonight. On the great slate, Selka has drawn the whole term in miniature: the spinning wheel, the swinging bob, the standing wave; the field lines and the four laws; the muon column, the dots of the double slit, the clicking casket; and finally the difference engine, the staircase of steps, the ledger's ±.

"Every craft of this module on one slate," she says. "The modeller's triage. The marcher's staircase. The confessor's grammar. Tonight I show you that they are not three crafts. They are one instrument, and my science was rebuilt around it within my own lifetime." She draws three columns and heads them: *Theory. Experiment. Simulation.*

"When I was a Senior at this bench, that third column did not exist — computation was theory's servant, a faster abacus. Then the questions outgrew the first two pillars. Stars we cannot enter. Climates we must not gamble. Reactors we must understand *without* melting them." She sets a fresh notebook beside the engine — blank, ruled, waiting. "Tomorrow, the Forge: you will build a simulation worthy of belief, end to end. Tonight, the discipline that makes 'worthy of belief' more than a phrase. There are two gates and a ledger, Senior. Miss any of them and your computation is theatre."

# Core Learning

## Concept Introduction

**The workflow: one loop, all three crafts.** Computational science runs as a connected cycle:

1. **Model** — the triage: keep dominant physics, discard and *document* the rest (lesson one)
2. **Discretise and implement** — turn continuous equations into steppable arithmetic; write the code (lesson two's staircase)
3. **Verify** — before trusting a single production number: known-answer cases, step-halving convergence, conservation watchdogs
4. **Run** — the production simulation, parameters recorded
5. **Analyse** — simulation output *is data*: means, uncertainties, fits, honest outlier handling (lesson three's grammar)
6. **Validate** — confront the analysed output with real measurements; agreement is judged by overlapping ranges
7. **Refine** — where reality disagrees beyond the bars, improve the *model* and loop again

**The two gates: verification versus validation.** The community compresses the distinction into one slogan worth memorising — **verification is solving the equations right; validation is solving the right equations.**

- *Verification* is internal. Does the code faithfully solve the mathematics it was given? Tests: aim it at problems with formula answers (constant-acceleration fall; small-angle pendulum); halve the step and demand convergence; watch conserved quantities for drift. A simulation that fails verification is broken machinery — debug it.
- *Validation* is external. Do those equations describe *reality*? Test: compare against experiment, with uncertainties on both sides. A verified code can be perfectly right about the wrong physics — a beautifully converged frictionless pendulum will never match the brass one on the bench, and no debugger will fix it, because *the model* is what's wrong. The fix is lesson one's refinement loop.

Both gates, always, in that order. Skipping verification risks attributing code bugs to physics; skipping validation risks decades of confident irrelevance.

**The ledger: reproducibility.** A scientific result must be repeatable by others — that is where its authority comes from. For computation this means recording the **rerun kit**: exact code version, every parameter and input file, and any random seeds, so that another scientist (or you, six months older) regenerates the result *exactly*. Modern science has been embarrassed repeatedly by celebrated results that evaporated on rerun; the cure is bookkeeping, not brilliance. An unreproducible computation is an anecdote with graphics.

**The third pillar.** Why grant simulation equal standing with theory and experiment? Because a class of questions yields to *nothing else*: climate projection (no formula, no spare planet), galaxy formation (10¹¹ bodies, 10⁹ years), supernova cores, protein folding, reactor accident scenarios that must be understood without being caused. The pillar's legitimacy rests entirely on tonight's disciplines — verified against mathematics, validated against every measurable fragment of reality, reproducible by rivals. Simulation without the gates is animation; with them, it is science exploring where neither theory nor experiment can walk.

## Why It Matters

This workflow is a profession. Computational physicists, climate modellers, aerospace simulation engineers, quantitative biologists, and reactor analysts spend their careers walking tonight's loop, and the gates have regulatory force: aircraft certification accepts simulation evidence only from verified-and-validated codes, nuclear safety cases live and die on V&V audits, and journals increasingly demand the rerun kit as a condition of publication. The reproducibility crisis — famous results across multiple sciences failing to rerun — has made the ledger discipline a live professional issue, and the habits you build tomorrow in the Forge are the same ones those fields hire for. Closer to home: this lesson is the Forge's blueprint. Every requirement of your Senior project — stated assumptions, verification suite, uncertainty-bearing analysis, validation against real data, a rerun kit — is one element of tonight's discipline, and your Lead tier (research methods, scientific computing at scale) builds directly on it.

## Worked Examples

**Example 1 — The two gates in action.** A drag-free cannonball code: verification aims it at the no-drag formula (range 35 m at 20 m/s, 30° — your Complex Motion arithmetic) and it converges cleanly. Validation fires a real ball: measured range 31 ± 1 m. Verified, yet invalid — drag, triaged away, is a 10% effect against a 3% measurement. Refine (add drag), re-verify (no formula now — use step-halving), revalidate: 31.2 m, inside the bars. The loop, walked once, honestly.

**Example 2 — The galaxy that fits in a box.** Simulating a galaxy merger models 10¹¹ stars as perhaps 10⁷ computational particles — a triage of *four orders of magnitude*, justified because gravity at galactic scales cares about mass distribution, not individual stars. Verification: two-particle orbits against Kepler's formulas. Validation: simulated mergers produce tidal tails and ring galaxies matching telescope photographs of real collisions caught mid-act across the sky. Science conducted entirely in the third pillar — and believed because both gates were passed.

**Example 3 — The evaporating result.** A published simulation claims a spectacular effect. The rerun kit is requested: code version unrecorded, parameters lost. The result cannot be checked, challenged, or extended — its evidentiary weight collapses regardless of whether it happened to be true. Contrast the gravitational-wave detections: code, data, and analysis pipelines published outright, so rivals worldwide could — and did — reproduce the signal. One is an anecdote; the other is a pillar bearing weight.

## Common Mistakes

- Running production simulations before verification — every number produced is unaccountable; deadline pressure makes this the field's most common sin
- Treating agreement with experiment as proof the code is bug-free — validation cannot substitute for verification; errors can cancel by luck, and luck runs out
- Treating verified code as true physics — convergence proves faithfulness to the equations, not the equations' faithfulness to the world
- Forgetting that simulation output is data — reporting a single run's numbers without uncertainty or sensitivity checks repeats the naked-number sin of last lesson
- Keeping no rerun kit — six months later, *you* are the rival laboratory who cannot reproduce the figures
- Believing the pillar metaphor exempts simulation from experiment — validation is a permanent obligation; a simulation never confronted with reality is theory with better graphics

## Mental Model

Think of a trustworthy simulation as a courtroom case. The *model* is the theory of the case — deliberately simplified, openly argued. *Verification* is checking your own evidence chain: no broken custody, no arithmetic slips — your house in order before trial. *Validation* is cross-examination by reality, the only witness who cannot be coached. *Uncertainty analysis* is honest counsel admitting exactly how strong the case is — no more. And *reproducibility* is the court record: any appellate judge, any rival, any future self can re-try the entire case from the transcript. Verdicts reached without any one of these are not justice, and numbers produced without them are not science.

## Mini Summary

- The workflow is one loop binding all three crafts: model → discretise/implement → verify → run → analyse-as-data → validate → refine
- Verification = solving the equations right (known answers, convergence, conservation); validation = solving the right equations (confrontation with experiment); both gates, in that order
- Reproducibility — recorded code versions, parameters, inputs, seeds — is what separates results from anecdotes with graphics
- Simulation earned its pillar by answering questions theory and experiment cannot reach — and keeps it only by passing the gates

# Guided Practice Quest

Selka stands you before the full slate — the term in miniature — and the blank notebook beside the engine. "Three trials before the Forge opens to you. First, my converged, conserving, beautiful pendulum code that will not match the brass: name which gate it passed, which it failed, and where the fix lives. Second, the loop entire: six stages on cards — assemble the working order, and justify why verification stands *before* the production run. Third, the rival academy's marvellous, unrepeatable result: name the disease and tell me what its cure would have cost. A notebook, Senior. The cure was always a notebook."

# Solo Practice Quest

Write the preface to your own Forge notebook (350–500 words): a personal protocol for trustworthy computation. Describe the full workflow loop in your own words, naming which earlier lesson supplies each stage. Define verification and validation precisely, give one concrete test you will perform for each, and describe one failure scenario for *each gate* — verified-but-invalid, and validated-by-luck-but-unverified — explaining what each failure would cost you. Specify your rerun kit: everything you commit to recording so that any rival could regenerate your results. Close with your own one-sentence answer to the question: what earns simulation its place as the third pillar, and what would forfeit it?

# Integration

**Mathematics:** Verification is applied numerical analysis — convergence orders, error bounds, the theorems that say *how fast* step-halving should improve answers (a code converging at the wrong rate is confessing a bug). Validation statistics reuse the grammar of agreement; sensitivity analysis — wiggling parameters to see what the answer depends on — is multivariable thinking in service of honesty.

**Engineering:** V&V is codified engineering practice with its own standards documents, audit trails, and legal weight in aerospace and nuclear licensing. Version control, automated test suites, and continuous verification — software engineering's daily disciplines — are the rerun kit industrialised; the best computational scientists are, by necessity, careful software engineers.

# Lore Conclusion

Selka takes the slate sponge and — column by column — washes the term away: the wheel, the wave, the four laws, the dots, the casket, the staircase, the ±. What remains is the heading she wrote at the start of the module, and beneath it, now, nothing but clean black slate.

"Theory proposes. Experiment disposes. Simulation explores where neither can walk — *when*, and only when, its gates are kept." She places the blank notebook in your hands. "Every craft is yours. Every gate has been named. Tomorrow the Deep Laboratories become the *Simulation Forge*: one real system, one model of your choosing, one march, one confrontation with measured data — end to end, alone, in your own hand. The notebook's first page is the protocol you will write tonight."

She turns the lamp down to an ember. "Four modules ago I told you that Seniors learn to see the layers. You have seen them, Senior: motion, fields, quanta, and the computation that binds them. The Forge will tell us whether you can *build* with them. Rest well. The fire is lit at dawn."
