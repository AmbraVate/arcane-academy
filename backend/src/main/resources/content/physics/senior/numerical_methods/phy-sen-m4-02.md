---
id: phy-sen-m4-02
domainId: physics
tier: SENIOR
moduleId: phy-sen-m4
moduleTitle: "Module 4: Computational Physics"
moduleGlyph: "💻"
moduleSortOrder: 4
topicSlug: numerical_methods
topicTitle: "Numerical Methods"
topicSortOrder: 2
title: "Numerical Methods: Marching Through Unsolvable Equations"
sortOrder: 2
xpReward: 120
practiceType: NONE
questType: INVESTIGATION
feynmanPrompt: "Explain to a junior student how stepping forward in tiny time increments can solve an equation that has no formula solution, and why step size is a bargain between accuracy and cost."
learningObjectives:
  - Explain why most realistic physics equations have no closed-form solution and how time-stepping circumvents this
  - Execute Euler-method steps by hand for simple motion and describe how smaller steps improve accuracy at higher cost
  - Identify sources of numerical error (truncation, accumulation) and basic checks for trustworthy results (step-halving, conservation laws)
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains that most real equations (three-body problem, large-angle pendulum, drag) have no closed-form solution, and that this is a mathematical fact rather than a skill gap"
    - "Describes the Euler time-stepping idea: from current state, use the physics to find rates of change, step forward a small Δt, repeat — and executes or describes at least one correct hand-worked step"
    - "Explains the step-size bargain: smaller steps reduce per-step (truncation) error but cost more computation, and errors accumulate over many steps"
    - "Names at least one trust check: halving the step and comparing, monitoring conserved quantities (energy drift), or validating against a known solvable case"
  keywords: [closed form, time step, Euler, truncation, accumulate, step size, energy drift, convergence]
  modelAnswer: |
    Most of the equations physics writes down cannot be solved by formula. The
    three-body problem has no general closed-form solution — proven, not merely
    unsolved — and even the humble pendulum at large angles or a cannonball with
    realistic drag exceeds what algebra can hand back. The equations are correct;
    they simply do not yield formulas. For three centuries that was a wall. Numerical
    methods turn the wall into a staircase.

    The core idea is the time step. I may not know the whole trajectory, but at this
    instant I know the state — position and velocity — and the physics tells me the
    rates of change: velocity says how position is changing, force over mass says how
    velocity is changing. So I advance everything by a tiny step Δt as if those rates
    held constant: new position = position + velocity × Δt; new velocity = velocity +
    acceleration × Δt. Then I recompute the rates at the new state and step again.
    Ten thousand honest little steps trace the curve that no formula describes. That
    is Euler's method, the ancestor of every simulation.

    Each step commits a small sin: the rates do NOT hold constant across the step —
    that is truncation error, and it shrinks as Δt shrinks. But smaller steps mean
    more of them for the same journey, costing computation, and the small errors
    accumulate as they go. Step size is therefore a bargain: accurate enough, cheap
    enough. The working tests of trust are practical: halve the step and rerun — if
    the answer barely moves, it has converged; watch conserved quantities — if the
    simulated pendulum's energy drifts upward, the integrator is lying, because the
    physics says energy stays put; and validate the method first on a problem with a
    known formula answer before aiming it at the unknown.
guidedSteps:
  - id: phy-sen-m4-02-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A ball falls from rest. Acceleration is a constant 10 m/s² downward. March it
      forward with Euler steps of Δt = 1 s, updating velocity first, then using the OLD
      velocity to update position:

      Step 1: v = 0 + 10×1 = 10 m/s;  fallen = 0 + 0×1 = 0 m
      Step 2: v = 10 + 10×1 = 20 m/s; fallen = 0 + 10×1 = 10 m
      Step 3: v = 20 + 10×1 = 30 m/s; fallen = 10 + 20×1 = ______ m (give the number)
    inputConfig:
      placeholder: "Distance fallen after step 3, in metres"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["30", "30m", "30 m"]
      rejectedFeedback: "Position update uses the velocity carried into the step: 10 + 20×1 = 30 m. The exact answer for 3 s of free fall is ½×10×3² = 45 m — the crude 1-second steps lag reality because velocity grew during each step while Euler held it frozen. That gap is truncation error, and it shrinks with smaller Δt."
    hint: "Take the position at the end of step 2 (10 m) and add velocity-entering-step-3 (20 m/s) times Δt (1 s)."
    reflectionPrompt: "The exact answer is 45 m and Euler gave 30 m. Why does this crude march UNDERestimate the fall?"
  - id: phy-sen-m4-02-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      You rerun the same falling-ball march with Δt = 0.1 s instead of 1 s. What happens
      to accuracy, and what does it cost?
    inputConfig:
      options:
        - "Accuracy improves; the march needs ten times as many steps"
        - "Accuracy worsens; small steps amplify rounding"
        - "Nothing changes; step size only affects how often results are printed"
        - "Accuracy improves and the computation also gets cheaper"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Accuracy improves; the march needs ten times as many steps"]
      rejectedFeedback: "Smaller steps mean the 'rates held frozen' lie is told over shorter intervals — per-step truncation error shrinks sharply. The bill: ten times the steps for the same three seconds. Step size is always this bargain — accuracy purchased with computation."
    hint: "Each Euler step pretends rates stay constant for Δt. Is that pretence more honest over 1 s or over 0.1 s? And how many 0.1 s steps fill 3 seconds?"
    reflectionPrompt: "Why can't you simply set Δt absurdly small and declare the problem solved forever?"
  - id: phy-sen-m4-02-g3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      Your simulated frictionless pendulum swings higher every cycle, gaining energy from
      nowhere. The code contains no force that could add energy. What is the correct
      diagnosis?
    inputConfig:
      options:
        - "The pendulum model is wrong — real pendulums do gain energy"
        - "Numerical error from the time-stepping is accumulating — the integrator injects spurious energy each step"
        - "The computer's arithmetic hardware is faulty"
        - "Energy conservation does not apply inside simulations"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Numerical error from the time-stepping is accumulating — the integrator injects spurious energy each step"]
      rejectedFeedback: "The physics conserves energy; the simulation does not — so the discrepancy is the method's signature, not nature's. Plain Euler systematically feeds energy into oscillations, each step's small overshoot compounding. Monitoring conserved quantities is the simulator's tripwire: when energy drifts, the integrator is lying."
    hint: "You proved in Module 1 that the frictionless pendulum conserves energy exactly. If the simulation's energy climbs, which is lying — the physics or the march?"
    reflectionPrompt: "Why are conserved quantities such powerful watchdogs for simulations, compared with just eyeballing the trajectory?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why do physicists use numerical methods at all, rather than solving equations exactly?"
    options:
      - "Numerical answers are more accurate than formulas"
      - "Most realistic equations provably have no closed-form solution — stepping is the only way through"
      - "Formulas went out of fashion with computers"
      - "Numerical methods avoid the need to understand the physics"
    correctIndex: 1
    feedback: "It is a theorem, not a temporary shortfall: the general three-body problem, realistic drag, large-angle pendulums — no formula exists to find. The equations remain perfectly correct; numerics is how we ask them questions anyway."
  - type: MULTIPLE_CHOICE
    question: "The cheapest practical test that a simulation result can be trusted is to..."
    options:
      - "Run it again with the same settings and confirm the same answer appears"
      - "Halve the time step, rerun, and check the answer barely changes"
      - "Increase the time step to finish faster"
      - "Compare it with a different programming language"
    correctIndex: 1
    feedback: "Identical reruns only prove the code is deterministic. Halving Δt probes the actual question — has the march converged toward the true solution? If the answer shifts noticeably, the previous step size was too coarse, and neither run deserves trust yet."
---

# Hook

In 1885, King Oscar II of Sweden offered a prize for solving the motion of three bodies under mutual gravity — the Sun, the Earth, and the Moon, say. Henri Poincaré won the prize in 1889 *without solving it* — by proving the thing everyone suspected and nobody wanted: **no general formula exists.** Not undiscovered. Nonexistent.

Three bodies. Newton's own law, written in one line. And the trajectory it commands cannot be expressed in any finite formula, ever. Yet today we fly spacecraft through that exact problem on billiard-table trajectories, threading gravitational keyholes years in advance. The escape from Poincaré's prison wasn't a cleverer formula — it was the humble discovery that an unsolvable curve can still be *walked*, one tiny step at a time. Today you learn to walk it.

# Lore Introduction

The brass difference engine has been wheeled to the centre bench, its counters gleaming, and beside it Selka has chalked an equation on the slate — the pendulum's true equation of motion, valid at any swing angle.

"Yesterday I promised you a theorem," she says. "Here it is. This equation — the *honest* pendulum, no small-angle mercy — has no closed-form solution in elementary functions. Neither does a cannonball with real drag. Neither do three mutually attracting bodies; a man named Poincaré collected a king's prize for proving that 'cannot' and shook determinism doing it." She lays the chalk down. "Physics spent three centuries pretending otherwise — choosing only the problems that yield formulas, the way a drunk searches under the lamppost. The territory beyond the lamppost is *most of physics*, Senior."

She begins turning the engine's crank, slow and steady; the counters click forward, step by step by step. "But watch. I do not need the whole journey written as a formula. I need only to know — at this instant — which way, and how fast. Step. Ask again. Step. The engine never *solves* anything. It *marches*. And ten thousand honest steps trace what no formula will ever name."

# Core Learning

## Concept Introduction

**The wall: equations without formula solutions.** Writing the physics is one act; solving it is another. Newton's second law for a large-angle pendulum, a projectile with realistic drag, or three gravitating bodies produces equations that are perfectly correct and **provably without closed-form solutions** — no finite formula in standard functions expresses the answer. This is mathematics' verdict, not a skill shortage. Every tier of this Academy quietly steered you toward the solvable exceptions; they are a measure-zero aristocracy among real problems.

**The staircase: time-stepping.** What survives the wall is local knowledge. At any instant the **state** of a mechanical system is its position and velocity — and the physics yields the **rates of change**: position changes at the velocity; velocity changes at the acceleration (force/mass, from Newton). The **Euler method** advances the state across a small interval Δt as if those rates held constant:

> new position = position + velocity × Δt
> new velocity = velocity + acceleration × Δt

— then recomputes the rates at the new state and steps again. No formula for the journey; just the physics consulted afresh ten thousand times. Every simulation you have ever seen — orbital mechanics, weather, game physics, molecular dynamics — descends from this march.

**The sin and the bargain.** Each step tells a small lie: rates do *not* stay constant across Δt (the falling ball's velocity grows mid-step; Euler holds it frozen). The lie is **truncation error**, and it shrinks as Δt shrinks — but smaller steps mean more steps for the same journey (compute cost), and per-step errors **accumulate** across the march. Step size is therefore a negotiated bargain: *small enough to be accurate, large enough to be affordable.* Refined integrators (Runge–Kutta and kin — better mid-step estimates of the rates) buy far more accuracy per step, but the bargain's logic never changes.

**Trust, but verify.** A simulation always produces numbers; the craft is knowing when to believe them.

- **Step-halving (convergence test):** halve Δt and rerun. If the answer barely moves, the march has converged; if it shifts, neither run deserved trust.
- **Conservation watchdogs:** the physics conserves energy and momentum; plain numerics doesn't have to. A frictionless pendulum whose simulated energy drifts upward is exposing its integrator — the lie made visible. Monitoring conserved quantities is the cheapest tripwire in the trade.
- **Validate on the solvable:** before aiming the method at the unknown, fire it at a problem whose formula answer you possess (constant-acceleration fall, small-angle pendulum). A method that cannot reproduce the known has no business reporting the unknown — last lesson's validation discipline, applied to the *method itself*.

## Why It Matters

Numerical methods are how physics escaped the lamppost. Spacecraft navigation is time-stepped gravity — every trajectory to Mars, every gravitational slingshot, every station-keeping burn is a march refined to metre precision across years. Weather forecasting steps the atmosphere's equations across a planetary grid in million-fold parallel; engineering's finite-element analysis steps stress through bridge decks and crash-test cars before metal is ever cut; molecular dynamics marches every atom in a protein to watch medicine work. The craft's failure modes matter as much as its successes: unstable integrators, accumulated drift, and unconverged grids have sunk real designs — which is why the verification rituals (step-halving, conservation watchdogs, validation cases) are professional obligations, not academic niceties. And next lesson stands on this one: real measurements come wrapped in noise, and extracting truth from them is its own numerical craft.

## Worked Examples

**Example 1 — The march, by hand.** Ball falls from rest, a = 10 m/s², Δt = 1 s, position updated with the velocity carried into each step:
After step 1: v = 10 m/s, fallen 0 m. After step 2: v = 20, fallen 10. After step 3: v = 30, fallen 30 m.
Exact answer: ½ × 10 × 9 = 45 m. The march lags because velocity grew *during* each step while Euler held it frozen at the entering value — systematic truncation, in plain sight.

**Example 2 — Buying accuracy.** Same problem, Δt = 0.1 s: thirty steps now span the fall, the frozen-rate lie is told over tenth-second intervals, and the march lands within ~3% of 45 m. Cost: 10× the arithmetic. At Δt = 0.01 s: ~0.3% error, 100× the arithmetic. The error of this simple march shrinks in proportion to Δt — halve the step, halve the error — while cost grows in inverse proportion. That trade rules every simulation budget on Earth.

**Example 3 — The watchdog barks.** A frictionless pendulum simulated with plain Euler swings visibly higher each cycle — energy climbing staircase-fashion, from nowhere. The physics forbids it (Module 1, energy conservation); therefore the *integrator* is the liar, its per-step overshoot compounding in one direction. Remedies: smaller Δt (palliative), or a smarter stepper — the trade's standard fix is an integrator that respects energy by construction. Diagnosis came free: one plot of energy versus time.

## Common Mistakes

- Treating "no closed-form solution" as "insoluble" — the equation still *determines* the motion completely; it just won't hand you a formula. The march extracts what the algebra cannot
- Updating position with the *new* velocity in an Euler step — the scheme defines which values belong to which step; mixing them changes the method (sometimes for the better — but know that you did it)
- Trusting a single run at a single step size — without a convergence check, a simulation is an anecdote
- Assuming smaller Δt is always available — cost scales, and below machine precision, rounding errors begin growing as truncation shrinks; there is a floor
- Ignoring conserved-quantity drift because the trajectory "looks right" — energy plots expose lies the eye forgives
- Believing the computer's confident decimals — output precision is typographical; *accuracy* is earned by verification

## Mental Model

You are walking a mountain path at night with a lantern that shows only the ground at your feet. No map of the whole trail exists — provably, none *can* exist. But at every point, the lantern shows the slope, and the slope tells you your next step. Small steps follow the true path faithfully and take all night; great strides are quick and walk you off cliffs at every bend. Your compass is conservation: the true path keeps a fixed altitude-plus-effort budget, so if your reckoning says you are somehow gaining altitude for free, you are not on the path — however confident each individual stride felt. Walk, check the compass, walk again.

## Mini Summary

- Most realistic physics equations provably lack closed-form solutions (Poincaré, the three-body prize); the equations remain correct and fully deterministic
- Euler's march: from the current state, compute rates from the physics, step forward by Δt, repeat — simulation's universal ancestor
- Each step's frozen-rate lie is truncation error; it shrinks with Δt but steps multiply and errors accumulate — accuracy is purchased with computation
- Trust is earned, not printed: halve the step and compare, watch conserved quantities for drift, and validate the method on known-answer problems first

# Guided Practice Quest

Selka sets the engine's counters to zero and hands you the crank. "Three trials at the staircase. First, march a falling stone by hand — three turns, one second each, and mind which velocity updates which position; the discipline is the method. Second, the bargain: tenth-second steps against full seconds — tell me what improves and what it bills you. Third, the watchdog: my pendulum simulation here is gaining energy like a miser finding coins — and you proved at this very bench that it must not. Deliver the diagnosis, Senior, and tell me which witness you trusted: the pretty trajectory, or the bookkeeping."

# Solo Practice Quest

Write an investigation log (350–500 words) on the numerical escape. Explain why time-stepping exists — what Poincaré proved and why the wall is mathematical, not technological. Demonstrate the Euler method by hand-marching a falling object for three steps (state your Δt and show each update), compare against the exact constant-acceleration answer, and explain the direction and origin of the discrepancy. Describe the step-size bargain quantitatively — what halving Δt buys and costs. Close with the verification creed: describe the three trust checks (step-halving convergence, conservation watchdogs, validation on solvable cases) and argue which you would never skip, and why a confident-looking trajectory is not evidence.

# Integration

**Mathematics:** Euler's march is calculus reversed: the derivative gives rates from curves; the march rebuilds curves from rates — numerical integration of differential equations. Truncation error is a Taylor-series remainder (the terms the frozen-rate lie discards), and the statement "error shrinks proportionally to Δt" is your first complexity-versus-accuracy scaling law. Runge–Kutta methods are simply better Taylor bookkeeping per step.

**Engineering:** Every simulation suite in engineering — finite-element stress, computational fluid dynamics, circuit simulators, crash codes — is an industrial-strength descendant of today's hand-cranked march, and every one ships with convergence criteria and conservation diagnostics because the failure modes you met today scale up with the hardware. Game physics engines make the opposite bargain: large steps, cheap integrators, stability over accuracy — fit for purpose, exactly as the modelling lesson taught.

# Lore Conclusion

Selka lets the engine's last step click home and reads the counters by lamplight. "Thirty metres, says the march. Forty-five, says the formula. And now you know precisely *why* they differ, *which* is lying, and *what* it costs to close the gap. That knowledge — not the crank — is the instrument."

She unlocks a cabinet beneath the engine and lifts out a thick ledger, its pages dense with handwritten columns — numbers, dates, marginal corrections in three different inks. "The marches are only half the new craft. Here is the other half: forty years of the Observatory's pendulum measurements. Real numbers, from real brass, taken by real hands on cold mornings — and not two of them agree. Scatter, drift, the occasional impossible entry where a tired scribe wrote a 7 for a 1." She sets the ledger before you. "Tomorrow: *Data Analysis* — how to make measurements confess what they know without torturing them into saying what you wish. Bring scepticism. The numbers will lie to you politely, and the first lie they tell is how sure they are."
