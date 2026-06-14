---
id: phy-app-m1-12
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m1
moduleTitle: "Module 1: Foundations of Physics"
moduleGlyph: "🔭"
moduleSortOrder: 1
topicSlug: physical_quantities
topicTitle: "Physical Quantities"
topicSortOrder: 4
title: "Dimensional Thinking: The Unit Detective"
sortOrder: 12
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Check equations for dimensional consistency
  - Use units to reconstruct half-remembered formulas
  - Convert compound units such as km/h to m/s
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Checks both sides of an equation carry identical units
    - Uses unit analysis to identify a wrong formula
    - Converts a compound unit correctly, showing the working
    - Reflects on a case where dimensional thinking caught an error
  keywords: [dimension, consistent, unit, convert, check, both sides, km/h, m/s]
  modelAnswer: |
    Dimensional thinking checks that every equation balances in units as well as numbers. Both
    sides of d = v×t carry metres (m/s × s = m ✓), so it can be true; "d = v/t" yields m/s²,
    an acceleration, so it cannot be a distance formula no matter how confident you feel.
    Compound conversions chain simple factors: 72 km/h = 72,000 m / 3600 s = 20 m/s. The
    skill doubles as a memory aid: a half-remembered formula whose units don't balance is
    misremembered, guaranteed.
guidedSteps:
  - id: phy-app-m1-12-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You half-remember kinetic energy as either E = ½mv or E = ½mv². Energy is measured in joules (kg·m²/s²). Which formula survives the unit check?
    inputConfig:
      options:
        - "E = ½mv, because it is simpler"
        - "E = ½mv², because kg × (m/s)² = kg·m²/s² matches the joule"
        - "Both work"
        - "Neither — units cannot decide this"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["E = ½mv², because kg × (m/s)² = kg·m²/s² matches the joule"]
      rejectedFeedback: "kg × m/s = kg·m/s (momentum's unit, not energy's). Only the v² version produces kg·m²/s² = joule. The units recovered the formula for you."
    hint: "Write out the units of each candidate and compare with kg·m²/s²."
    reflectionPrompt: "Why can units rule a formula OUT but never fully prove it right (think of the ½)?"
  - id: phy-app-m1-12-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Convert: 36 km/h = ________ m/s.
    inputConfig:
      placeholder: "10"
    markingRule:
      matchMode: CONTAINS
      accepted: ["10"]
      rejectedFeedback: "36 km/h = 36,000 m per 3600 s = 10 m/s. Shortcut: divide km/h by 3.6 to get m/s."
    hint: "How many metres in 36 km? How many seconds in an hour?"
    reflectionPrompt: "A sprinter runs 10 m/s. How does that compare with city traffic at 36 km/h — does the equality surprise you?"
  - id: phy-app-m1-12-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A classmate's homework concludes: "the energy of the ball is 6 m/s." In 2–3 sentences, explain how you know — without redoing any arithmetic — that something has gone wrong, and what kind of mistake to hunt for.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [unit, joule, speed, wrong quantity, energy, m/s, dimension]
      rejectedFeedback: "Energy comes in joules; m/s is a speed unit. A result carrying the wrong unit means a wrong or incomplete formula was used — the units flag the error without any recalculation."
    hint: "What unit must an energy carry? What quantity does m/s belong to?"
    reflectionPrompt: "How might carrying units through every line have caught this before the final answer?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An equation's two sides carry different units. The equation is:"
    options:
      - "Possibly fine if the numbers agree"
      - "Definitely wrong — no exceptions"
      - "Fine for small values"
      - "Fine if both sides are positive"
    correctIndex: 1
    feedback: "Dimensional consistency is non-negotiable: metres can never equal seconds, whatever the numbers say. A unit mismatch is a certificate of error."
  - type: MULTIPLE_CHOICE
    question: "To convert m/s to km/h you multiply by:"
    options: ["3.6", "60", "1000", "0.36"]
    correctIndex: 0
    feedback: "1 m/s = 3600 m/h = 3.6 km/h. So multiply m/s by 3.6 (and divide km/h by 3.6 to go back)."
---

# Hook

Imagine a detective who can prove a suspect innocent without leaving the office — no fingerprints, no interviews, just one decisive question. Physics has such a detective, and you've already met the evidence it reads: units.

Here's the power move: any equation whose two sides carry different units is *wrong*. Not "probably wrong". Wrong — with the certainty of arithmetic, before you check a single number. Can't remember if kinetic energy is ½mv or ½mv²? The units convict one and acquit the other in ten seconds. Suspicious of a final answer? If a supposed energy turned out in m/s, the case is closed.

This lesson turns the unit bookkeeping you've built across Module One into an active investigative tool — the cheapest error-detector and memory-prosthetic in all of science.

# Lore Introduction

The final chamber of Module One is the Observatory's proofing room, where every calculation bound for the great charts is checked before inking. You expect rows of magi redoing arithmetic. Instead, a single elderly proofreader sits running a finger along submitted derivations *without reading the numbers at all*. "Master Imra reads only the units," Thorne murmurs. "Numbers lie charmingly — a slipped digit looks like any other digit. Units cannot lie. If a line's units do not balance, she strikes the page, and she has not been wrong in forty years." Imra, without looking up, strikes a page. "Energy," she says dryly, "in metres per second. Again." Thorne smiles. "Today, apprentice, you learn her trick."

# Core Learning

## Concept Introduction

**The consistency law.** Every valid physics equation balances dimensionally: both sides reduce to the same combination of base units, and only like units may be added or subtracted. This gives a three-step audit you can run on anything:

1. Replace each symbol with its units.
2. Simplify each side using ordinary algebra (units multiply and cancel like symbols).
3. Compare. Mismatch ⇒ the equation is wrong, full stop. Match ⇒ it *may* be right (units can't see pure numbers like ½ or 2π).

**Formula recovery.** Half-remembered formula? Audit the candidates:

- d = v·t → (m/s)·s = m ✓ a distance
- d = v/t → (m/s)/s = m/s² ✗ that's an acceleration

The units *reconstruct* the right form — an exam-day and engineering-day superpower.

**Compound conversions.** Convert each part of the unit, then combine:

```
72 km/h = 72 × 1000 m / 3600 s = 20 m/s
```

Worth memorising once: **m/s × 3.6 = km/h** (and ÷3.6 the other way). Sanity anchors: brisk walk ≈ 1.5 m/s; sprinter ≈ 10 m/s; motorway car ≈ 30 m/s.

**Answer auditing.** Carry units through *every* line of working. The moment a line's units stop making sense, the error lives on that line — no need to re-derive everything.

## Why It Matters

- This is the proofreading layer that catches the Mars-Orbiter class of error — in homework, lab reports, spreadsheets, and engineering documents alike.
- Dimensional thinking scales with you: at Senior tier it becomes dimensional analysis, capable of *predicting* the form of unknown laws (how does a pendulum's period depend on its length? The units almost answer alone).
- Professionals lean on it daily: a structural engineer seeing a beam-deflection in kg has found a bug; a data analyst seeing revenue-per-user in user² has found a bad join.

## Worked Examples

**Example 1: Auditing a derivation line by line**
A student computes braking distance: d = v²/(2a) with v = 20 m/s, a = 5 m/s². Units: (m/s)² ÷ (m/s²) = (m²/s²) × (s²/m) = m ✓. The form is dimensionally sound; now (and only now) is the arithmetic worth doing: 400/10 = 40 m.

**Example 2: The impostor formula**
Period of a pendulum — was it T = 2π√(L/g) or T = 2π√(g/L)? Units of L/g: m ÷ (m/s²) = s², and √(s²) = s ✓ a time. Units of g/L: 1/s², square root 1/s ✗ a frequency, not a period. The first form is the law; the units never forgot.

**Example 3: Conversion chain with a compound unit**
Water flows at 0.3 m³/min. In litres per second: 0.3 m³ = 300 L, and per minute → per second divides by 60: 300/60 = 5 L/s. Two clean factor steps; no formula memorised.

## Common Mistakes

- **Auditing only the final answer** — carry units through *every* line, so errors are localised the moment they occur.
- **Converting one part of a compound unit** — km/h → m/h is halfway; finish the job.
- **Believing a unit match proves correctness** — dimensional consistency is necessary, not sufficient; the dimensionless ½ in ½mv² is invisible to units.
- **Treating the check as optional under time pressure** — pressure is exactly when slips multiply; the audit takes seconds.
- **Forgetting the 3.6** — km/h ↔ m/s confusions are the most common conversion error in early mechanics.

## Mental Model

Think of units as **a passport that every quantity must carry through every border of your calculation**. Each line of working is a checkpoint: multiply, divide, cancel — fine, passports get stamped. But try to add metres to seconds, or smuggle a speed through a checkpoint marked "energy", and the guard stops you *right there*, at that line — not three pages later when the final answer comes out absurd. Calculations done without checking passports aren't faster; they just relocate the delay to the worst possible moment.

## Mini Summary

- ✔ Both sides of every true equation carry identical units — mismatch means wrong
- ✔ Audit candidates' units to recover half-remembered formulas
- ✔ Compound conversions: convert each part, then combine (m/s × 3.6 = km/h)
- ✔ Carry units through every line to localise errors instantly
- ✔ A unit match permits a formula; it can never fully prove it

# Guided Practice Quest

Work through the guided steps to acquit the true kinetic-energy formula, convert traffic-speed units, and diagnose a wrong answer from its unit alone.

# Solo Practice Quest

Play Master Imra. Below are four "submitted" results — audit each one using units only, and write a verdict (sound / wrong, with the reason): (1) "pressure = force × area"; (2) "average speed of the journey: 54 km/h = 15 m/s"; (3) "the spring stores energy E = ½kx, where k is in N/m and x in m"; (4) "fuel use: 8 litres per 100 km = 0.08 L/km". Then write your own one-line reflection: which audit was fastest, and what does that tell you about when this tool shines?

# Integration

**Mathematics**: Unit algebra is literal algebra — the cancellation in (m/s)·s = m is the same operation as x/y·y = x. Dimensional analysis later becomes a genuine proof technique: the Buckingham π theorem derives the *shape* of physical laws from dimensions alone.

**Engineering**: Every engineering discipline institutionalises this lesson — checklists, typed unit systems in CAD and simulation software, and code review rules exist because unit errors have sunk spacecraft, grounded aircraft (the Gimli Glider ran out of fuel over Canada from a pounds/kilograms mix-up), and collapsed structures.

# Lore Conclusion

Master Imra finally looks up from her pages — at you. She slides a single line of derivation across the desk: someone's claim, units and all. You trace it: kg times m/s² times m... a newton-metre... a joule, claimed as a joule. "Sound," you say, and then, because the habit has already taken root: "though the units can't vouch for the constant." Something that might be a smile crosses her face. Thorne collects you at the door. "Module One is yours," he says, and for once there is no next assignment in his hand. "Measurement, method, mathematics, quantities — the foundations are poured and proofed. Rest tonight. Tomorrow we push on something and find out what the universe does about it. Tomorrow, *mechanics*."
