---
id: phy-app-m2-11
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: work_and_energy
topicTitle: "Work and Energy"
topicSortOrder: 4
title: "Kinetic and Potential Energy"
sortOrder: 11
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Calculate kinetic energy using KE = ½mv²
  - Calculate gravitational potential energy using PE = mgh
  - Explain why doubling speed quadruples kinetic energy
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Computes KE = ½mv² correctly with units
    - Computes PE = mgh correctly with units
    - Explains the v² consequence — double speed, four times the energy
  keywords: [kinetic, potential, "½mv²", mgh, joule, stored, squared, height]
  modelAnswer: |
    Kinetic energy is the energy of motion: KE = ½mv². A 1000 kg car at 10 m/s carries
    ½ × 1000 × 100 = 50,000 J; at 20 m/s it carries 200,000 J — double the speed, four times
    the energy, because v is squared. Gravitational potential energy is stored height:
    PE = mgh, so a 2 kg book lifted 3 m gains 2 × 10 × 3 = 60 J. Both are measured in joules
    and both are accounts into which work can deposit energy — lifting fills the height
    account, accelerating fills the motion account.
guidedSteps:
  - id: phy-app-m2-11-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A 0.5 kg football moves at 4 m/s. Its kinetic energy is KE = ½ × 0.5 × 4² = ________ J.
    inputConfig:
      placeholder: "4"
    markingRule:
      matchMode: CONTAINS
      accepted: ["4"]
      rejectedFeedback: "KE = ½mv² = ½ × 0.5 × 16 = 4 J. Square the speed FIRST, then multiply."
    hint: "4² = 16. Then halve the mass and multiply."
    reflectionPrompt: "What would the same ball carry at 8 m/s? (Don't double 4 J — recompute!)"
  - id: phy-app-m2-11-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A car's speed rises from 30 km/h to 60 km/h. Its kinetic energy:
    inputConfig:
      options:
        - "Doubles"
        - "Triples"
        - "Quadruples"
        - "Increases by half"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Quadruples"]
      rejectedFeedback: "KE depends on v². Doubling v multiplies the energy by 2² = 4. This is why motorway crashes are so much worse than city ones — and why braking distances quadruple, not double."
    hint: "v appears squared: what does doubling v do to v²?"
    reflectionPrompt: "What does tripling the speed do to the kinetic energy?"
  - id: phy-app-m2-11-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A 60 kg rock climber ascends 12 m of cliff (g ≈ 10 N/kg). In 2–3 sentences: how much potential energy does she gain, where did it come from, and what could release it?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["7200", "7,200", mgh, muscles, work, fall, stored]
      rejectedFeedback: "PE = mgh = 60 × 10 × 12 = 7,200 J — deposited by the work her muscles did against gravity. A fall would convert it back: stored height becoming speed."
    hint: "PE = mgh. Energy is never created — trace its source."
    reflectionPrompt: "Why do climbers respect heights in exact proportion to mgh?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which has the most kinetic energy?"
    options:
      - "A 2 kg ball at 1 m/s"
      - "A 1 kg ball at 2 m/s"
      - "A 0.5 kg ball at 3 m/s"
      - "They are all equal"
    correctIndex: 2
    feedback: "KE = ½mv²: (a) ½×2×1 = 1 J; (b) ½×1×4 = 2 J; (c) ½×0.5×9 = 2.25 J. Speed, being squared, punches above its weight."
  - type: MULTIPLE_CHOICE
    question: "Gravitational potential energy depends on:"
    options:
      - "Mass, gravity, and height"
      - "Speed and mass only"
      - "Height only"
      - "The path taken while climbing"
    correctIndex: 0
    feedback: "PE = mgh. Notably path-independent: stairs, ladder, or helicopter — same m, g, and h means same stored energy."
---

# Hook

Why is a car crash at 60 km/h not twice as bad as one at 30, but *four times* as bad? Why do falling coconuts kill while falling raindrops merely annoy? Why does a slingshot need so little rubber to be dangerous?

One answer covers all three: energy — and specifically, the two great forms it takes in mechanics. **Kinetic energy** is the energy of motion, and it grows with the *square* of speed: ½mv². **Potential energy** is motion-in-waiting, banked as height: mgh. Between them they run every playground swing, every rollercoaster, every hydroelectric dam, and every safety statistic on the motorway.

Yesterday you learned how energy is transferred (work). Today you learn where it *lives*.

# Lore Introduction

Thorne unlocks the Observatory's clock tower, where the great timekeeper's secret turns out to be embarrassingly simple: two enormous stone weights on chains, wound to the top of the tower each Monday. "No furnace, no spring, no spell," says Thorne. "The winding-apprentice climbs four hundred steps cranking these stones upward — a week's worth of work, deposited in one morning." The weights, he shows you, descend a few feet each day, their stored week paying out tick by tick into the pendulum and gears. "The old masters called the high stone *patient energy* and the falling stone *eager energy*." He sets his hand on the cold granite. "Patient and eager. Stored and spending. Today we learn to count both — and the counting has one surprise in it that flattens most apprentices. It concerns speed."

# Core Learning

## Concept Introduction

**Kinetic energy** — the energy an object has because it is moving:

```
KE = ½ m v²
(J)   (kg)(m/s)²
```

A 1,000 kg car: at 10 m/s, KE = ½ × 1000 × 10² = 50,000 J. At 20 m/s: 200,000 J. **Double the speed → four times the energy**, because v is *squared*. Triple the speed → nine times. This single mathematical fact underwrites speed limits, braking distances, and the difference between a bruise and a fracture.

**Gravitational potential energy** — energy stored by being high up, ready to convert:

```
PE = m g h
(J)  (kg)(N/kg)(m)
```

Lift a 2 kg book 3 m: PE gained = 2 × 10 × 3 = 60 J — *exactly* the work you did lifting it (W = F×d = 20 N × 3 m). Work is the deposit; PE is the account balance. Two fine prints: h is measured from whatever reference level you choose (the floor, sea level — only *differences* matter), and the path doesn't matter — stairs or ladder, same mgh.

**The two accounts trade.** Drop the book and the height account drains into the motion account: PE → KE. Throw a ball upward: KE → PE, the ball slowing as its motion is banked into height. The exchange rate is perfect and the bookkeeping is next lesson's grand law; today, learn to compute each balance.

Other energy stores exist — elastic (stretched bows, compressed springs), chemical (fuel, food, batteries), thermal — but kinetic and gravitational-potential are mechanics' working pair.

## Why It Matters

- The v² law is public-safety mathematics: small speed increases buy outsized increases in destructive energy (and in the braking distance needed to remove it).
- Energy storage is civilisation's hard problem — dams (mgh of a lake), flywheels (½mv² of a spinning disc), and pumped-storage power stations are these two formulas at national scale.
- Sports, ballistics, and engineering design all start from "how much energy does it carry, and where will that energy go?"

## Worked Examples

**Example 1: The motorway multiplier**
Same 1,200 kg car, city vs motorway: 13 m/s (47 km/h) → ½×1200×169 ≈ 101,000 J. 30 m/s (108 km/h) → ½×1200×900 = 540,000 J. The motorway car must shed *five times* the energy to stop — through the same brakes, into the same crumple zones. Energy, not speed, is what collisions spend.

**Example 2: The high dive, audited**
A 50 kg diver on a 10 m platform: PE = 50 × 10 × 10 = 5,000 J relative to the water. Stepping off, that balance converts steadily to KE; just before entry, ½mv² ≈ 5,000 J → v² = 200 → v ≈ 14 m/s (50 km/h). Computed without a single force-and-acceleration step — energy bookkeeping just shortcut the entire kinematics problem. (Feel the power of that.)

**Example 3: Why the hammer beats the feather pillow**
A 1 kg hammer swung at 8 m/s: 32 J. A 1 kg pillow at the same speed: also 32 J! The difference at impact isn't the energy carried — it's how *quickly and locally* each delivers it (the hammer's joules arrive in milliseconds on a centimetre; the pillow's spread gently). Energy says how much; force and time say how it lands. Keep the concepts distinct.

## Common Mistakes

- **Doubling KE when speed doubles** — the square is not optional: 2× speed = 4× energy, always.
- **Squaring m instead of v** (or the whole ½mv) — only the speed is squared; compute v² first, then multiply.
- **Treating PE as absolute** — only height *differences* matter; choose a reference level and say so.
- **Thinking the path changes mgh** — switchbacks make a climb easier per step, never smaller in total deposit.
- **Confusing energy with force at impact** — same joules can arrive as a tap over seconds or a spike over milliseconds; damage cares about both.

## Mental Model

Mechanics runs on **two bank accounts that every object holds**. The *motion account* (kinetic) pays interest on speed at a punishing squared rate — modest deposits of velocity swell the balance disproportionately. The *height account* (gravitational potential) is a plain savings ledger: balance = mass × g × altitude, path of deposit irrelevant. Work (yesterday's lesson) is how outsiders pay money in; falling and accelerating are the accounts trading with each other. Learn to glance at any object and read both balances — that glance is half of mechanics.

## Mini Summary

- ✔ KE = ½mv², in joules — square the speed first
- ✔ Double speed → 4× kinetic energy; triple → 9× (hence braking-distance law)
- ✔ PE = mgh — stored by working against gravity; path-independent; reference level your choice
- ✔ Work deposits energy into the accounts; falling/launching trades between them
- ✔ Energy says how much is carried — force and time decide how it's delivered

# Guided Practice Quest

Work through the guided steps to weigh a football's motion, quadruple a car's stakes, and bank a climber's twelve metres honestly.

# Solo Practice Quest

Audit three energies from your own world: (1) yourself walking (~1.5 m/s) and sprinting (estimate!) — compute both KEs and the ratio; (2) an object in your home on a shelf — estimate m and h, compute its PE, and name what would happen to those joules if it fell; (3) a vehicle you use, at two realistic speeds — compute both KEs and write one sentence connecting the ratio to braking distance. Then the synthesis question: rank all five energies you computed on one list, and note the largest surprise in the ranking.

# Integration

**Mathematics**: The v² in kinetic energy is your first nonlinear law, and its consequences (quadrupling, the parabola of PE↔KE exchange) are the square function's fingerprints. Choosing a PE reference level is your first taste of gauge freedom — physics caring only about differences.

**Engineering**: Energy storage engineering is a tour of these formulas: pumped-storage dams bank gigawatt-hours as mgh; flywheels in buses and grid stations bank ½mv² (spun fast, because of that lovely square); regenerative brakes in electric cars run the KE account *backwards* into the battery instead of into heat.

# Lore Conclusion

In the clock tower, the Monday winding-apprentice arrives, sets his shoulder to the crank, and begins the week's deposit — four hundred steps of work flowing link by link into the patient stones. Thorne watches with you. "You can now price his morning exactly: mass, gravity, height. And you can price the stone's descent into every tick." He turns to go, then pauses at the stair. "But here is the question that should keep you up tonight: across all those exchanges — crank to chain, stone to pendulum, pendulum to gear — does any of it ever go *missing*? Count, and you will find pennies unaccounted for. Tomorrow I show you where the universe hides them — and the iron law that says it may hide, but never destroy."
