---
id: phy-jun-m1-02
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: momentum
topicTitle: "Momentum"
topicSortOrder: 1
title: "Conservation of Momentum"
sortOrder: 2
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - State the law of conservation of momentum for isolated systems
  - Apply momentum conservation to collisions and recoil
  - Explain why conservation follows from Newton's third law
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - States that total momentum of an isolated system is unchanged by internal interactions
    - Solves a one-dimensional collision or recoil problem with signs
    - Connects conservation to the third law's equal-and-opposite impulses
  keywords: [conservation, total, isolated, collision, recoil, before, after, third law]
  modelAnswer: |
    In any isolated system — no external resultant force — the total momentum before an
    interaction equals the total after, however violent the interaction. The law is Newton's
    third law in accounting form: colliding objects exert equal and opposite forces for the
    same contact time, so their impulses cancel and the pair's total Δp is zero. A 2 kg trolley
    at 3 m/s hitting a stationary 1 kg trolley and coupling moves off at v = 6/3 = 2 m/s; a
    gun's forward bullet momentum is exactly balanced by its backward recoil. Momentum
    conservation survives even where energy is mangled into heat and sound — which is what
    makes it the collision-solver's first tool.
guidedSteps:
  - id: phy-jun-m1-02-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A 2 kg trolley at 3 m/s collides with and couples to a stationary 1 kg trolley. Total momentum before = 6 kg·m/s; combined mass after = 3 kg; so the pair moves off at ________ m/s.
    inputConfig:
      placeholder: "2"
    markingRule:
      matchMode: CONTAINS
      accepted: ["2"]
      rejectedFeedback: "Conservation: 6 kg·m/s must survive. v = p/m = 6/3 = 2 m/s. The crunch redistributed momentum; it could not create or destroy any."
    hint: "Total p after = total p before. Divide by the new combined mass."
    reflectionPrompt: "Check the kinetic energy before and after — what happened to the difference, and why is that allowed?"
  - id: phy-jun-m1-02-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A stationary cannon (500 kg) fires a 5 kg ball forward at 100 m/s. The cannon recoils at:
    inputConfig:
      options:
        - "100 m/s backward"
        - "1 m/s backward — equal and opposite momentum, shared by a 100× larger mass"
        - "It does not move — it was stationary"
        - "5 m/s forward"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["1 m/s backward — equal and opposite momentum, shared by a 100× larger mass"]
      rejectedFeedback: "Total p was zero and must remain zero: ball +500 kg·m/s, so cannon −500 kg·m/s → v = 500/500 = 1 m/s backward. Recoil is conservation's signature."
    hint: "Before: zero. After: ball's momentum + cannon's momentum must still be zero."
    reflectionPrompt: "Why does the cannon get far less ENERGY than the ball, despite equal momentum?"
  - id: phy-jun-m1-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2–3 sentences WHY total momentum is conserved in a collision, starting from Newton's third law and impulse (FΔt = Δp).
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [third law, equal, opposite, same time, impulse, cancel, zero]
      rejectedFeedback: "During contact, A pushes B and B pushes A with EQUAL and OPPOSITE forces for the SAME contact time — so their impulses (FΔt) are equal and opposite, and the changes in momentum cancel exactly: the pair's total cannot change."
    hint: "Equal forces, opposite directions, identical contact time — what does that do to the two Δp's?"
    reflectionPrompt: "What would have to be true of the third law for momentum NOT to be conserved?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Conservation of momentum applies exactly when:"
    options:
      - "No external resultant force acts on the system during the interaction"
      - "The collision is gentle"
      - "Kinetic energy is also conserved"
      - "Objects bounce rather than stick"
    correctIndex: 0
    feedback: "Isolation is the only condition. Sticky, bouncy, explosive — internal violence is irrelevant; only outside forces can change the total."
  - type: MULTIPLE_CHOICE
    question: "Two skaters at rest push apart. Afterwards, the system's total momentum is:"
    options: ["Shared equally as speeds", "Zero — their momenta are equal and opposite", "Doubled", "Whatever the stronger skater chooses"]
    correctIndex: 1
    feedback: "It began at zero and no external force acted: the lighter skater moves faster, the heavier slower, in exact inverse proportion to mass — summing forever to zero."
---

# Hook

In the chaos of a car crash — metal folding, glass bursting, energy vanishing by the hundred kilojoule into heat and noise — one quantity sails through the violence *perfectly unchanged*: the total momentum of the wreckage. Add up every mv before; add it up after; the books balance to the decimal. Crash investigators reconstruct collisions from skid marks on exactly this faith, and the faith has never once been let down.

This is conservation of momentum: the first of physics' great conservation laws you'll meet as a working tool. Energy conservation (Apprentice tier) needed you to chase joules into heat. Momentum is blunter and kinder: in any isolated system, **total p before = total p after** — no leakage, no hidden accounts, no exceptions ever observed, from billiard balls to colliding galaxies.

# Lore Introduction

The Mechanica's collision track gleams under the workshop lamps, and Magus Vex performs the rite he clearly relishes most. Two carts, fitted with spring-buffers, scales, and the Mechanica's prized velocity-gates. He weighs each cart aloud, sets one rolling, and lets them crash — *clack* — reading the gates before and after. He does it again with coupling pins so they stick. Again with a violent spring-release between two stationary carts. Each time he chalks the arithmetic on the great board, two columns: *before* and *after*. And each time, the totals — whatever carnage occurred between the gates — agree. "A century of juniors has tried to break this board," Vex says, tapping the columns. "Sticky crashes, bouncy crashes, explosions, trick weights. The two columns have never once disagreed. Your task this morning is to try to break it — and in failing, to understand *why you must fail*. The reason fits in one line, and you already own it."

# Core Learning

## Concept Introduction

**The law.** For any **isolated system** — one with no external resultant force —

```
total momentum before = total momentum after
m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂
```

however violent, sticky, bouncy, or explosive the internal events. (u = velocities before, v = after; signs carry directions.)

**Why it must be true.** During contact, Newton's third law holds: A pushes B with exactly the force B pushes A, oppositely directed, for precisely the same contact time. Equal and opposite forces × identical Δt = **equal and opposite impulses** = equal and opposite Δp. The pair's changes cancel; the total cannot move. Conservation is the third law, run through the impulse contract.

**The working recipe:**
1. Define the system and check isolation (external forces negligible during the brief event? Usually yes for collisions — friction hasn't time to matter).
2. Choose a positive direction; sign every velocity.
3. Sum p before; sum p after with unknowns; equate; solve.

**Three standard scenes:**
- **Coupling (sticky) collisions**: objects join; one shared v after. Kinetic energy is *lost* to deformation and heat — allowed! Momentum doesn't care.
- **Recoil/explosions**: total p = 0 before, so the parts depart with equal-and-opposite momenta — guns kick, rockets climb, skaters drift apart with speeds in inverse proportion to mass.
- **Bouncy collisions**: both momentum and (in the ideal "elastic" case) kinetic energy survive — Newton's cradle, billiards, atomic scattering. Real bounces sit between sticky and elastic.

**Momentum vs energy, the division of labour**: momentum is conserved in *every* isolated interaction; kinetic energy only in elastic ones. That's why p is the first tool out of the box at every crash scene.

## Why It Matters

- Collision reconstruction (forensics, insurance, sport) runs on this law: masses and final motions reveal initial speeds.
- Rocketry *is* recoil: throw exhaust backward, gain momentum forward — the only propulsion that works in vacuum, now computable.
- Conservation laws are physics' deepest organising principles; this one is your template for charge, energy, and stranger conserved quantities at higher tiers.

## Worked Examples

**Example 1: The crash investigator's classic**
A 1,500 kg car runs into the back of a stationary 1,000 kg car; they lock together and skid off at 9 m/s (known from skid analysis). Before: 1500 × u = after: 2500 × 9 = 22,500 → u = **15 m/s** (54 km/h). The driver's "I was doing thirty" meets a conservation law with no sympathy. Courts accept this arithmetic; it has Newton as an expert witness.

**Example 2: The astronaut's wrench**
An untethered 100 kg astronaut drifts motionless, 20 m from her craft, holding a 2 kg wrench. She hurls it away from the craft at 10 m/s: wrench −20 kg·m/s, so she gains +20 → 0.2 m/s toward home; 100 seconds to safety. Total momentum: zero throughout. (She throws the *wrench* and not her glove because more mv per throw is available in a dense object she can accelerate hard.)

**Example 3: Newton's cradle decoded**
Lift one ball, release: one ball swings out the far side at the same speed. Why never *two* balls at half speed? Check both ledgers: two-at-half conserves momentum (mv = 2×m×v/2 ✓) but not energy (½mv² vs 2×½m(v/2)² = half ✗). Only one-out-at-full-speed satisfies both books at once. The desk toy is a double-conservation theorem in brass.

## Common Mistakes

- **Dropping signs** — head-on problems are sign problems; one direction is positive, declared, forever.
- **Applying conservation across external pushes** — a collision *while braking hard*, or analysis extending into the long skid afterwards, leaks momentum to the road; conserve across the brief contact only.
- **Demanding kinetic energy also balance** — sticky collisions destroy KE by design (into heat and crumple); momentum alone is the universal survivor.
- **"The heavier object carries the interaction"** — both change momentum equally and oppositely; the *lighter* one just changes velocity more.
- **Forgetting the system can be chosen** — one cart isn't isolated (the other pushes it); the *pair* is. Conservation is a property of well-drawn boundaries.

## Mental Model

Picture momentum as **money in a sealed room**. The people inside (colliding carts, exploding fireworks, recoiling guns) may trade, rob, gamble, and brawl — money changes hands violently — but the room is sealed: count every pocket before and after, and the total is identical to the penny. Energy, in this metaphor, is the *furniture*: brawls genuinely destroy it (into splinters and heat). Only an outsider reaching through a window — an external force — can change the room's total. The auditor's first question at any crash scene is therefore always: *was the room sealed?* For the millisecond of contact, it almost always was.

## Mini Summary

- ✔ Isolated system: total p before = total p after — no exceptions ever observed
- ✔ The reason is the third law: equal, opposite impulses cancel pairwise
- ✔ Sticky collisions: one shared final v; KE dies, momentum survives
- ✔ Recoil: from zero, parts depart with equal-and-opposite momenta (guns, rockets, skaters)
- ✔ Signs carry the physics; system boundaries decide whether the law applies

# Guided Practice Quest

Work through the guided steps to couple two trolleys at the law's command, recoil a cannon by exact bookkeeping, and derive the whole law from a third-law handshake.

# Solo Practice Quest

Three engagements with the sealed room: (1) *Tabletop*: roll a heavy coin (or ball) into a stationary light one several times; observe and describe the outcomes qualitatively with conservation language (who speeds up, who slows, what the pair's total is doing). (2) *Compute*: a 60 kg skater at rest pushes a 90 kg skater; the lighter one drifts back at 1.5 m/s — find the heavier one's velocity, then compare their kinetic energies and explain the asymmetry. (3) *Reconstruct*: invent a two-vehicle coupling collision (choose masses and final shared speed), then play investigator — recover the moving vehicle's initial speed, and write the two-line court statement. Bonus: explain why a rocket in vacuum accelerates even though "there's nothing to push on", in exactly one sentence of conservation.

# Integration

**Mathematics**: Conservation laws are equations that *survive* processes — invariants. You'll meet the idea everywhere: invariants under transformation are the spine of higher mathematics, and (a Senior-tier whisper) each physical conservation law corresponds to a symmetry of nature — momentum's is the uniformity of space itself.

**Engineering**: Recoil management is a design discipline — artillery recoil absorbers, firearm actions cycled by conserved momentum, and spacecraft thrusters budgeted in momentum units (newton-seconds). Crash-test programmes validate the sealed-room arithmetic with instrumented dummies, frame by frame.

# Lore Conclusion

You spend the morning genuinely trying to break the board — trick masses, double collisions, a spring-explosion you rig yourself — and at each *clack* the velocity-gates report, and the two columns agree, and Vex's chalk does not hurry. At last you write beneath the columns the one-line reason: *equal and opposite impulses; the pair cannot change its total*. Vex reads it, nods once, and — the Mechanica's juniors will not believe you — almost smiles. "The board stands. It always stands." He unhooks the spring-buffers from the carts and replaces them with pads of clay and plates of sprung steel, side by side. "But you noticed, of course, the column we did NOT total this morning." He taps the energy ledger pointedly. "Sticky crashes bleed it; bouncy ones keep it. Tomorrow we sort collisions by what they do to the *furniture* — and you learn why a dropped ball never quite returns to your hand."
