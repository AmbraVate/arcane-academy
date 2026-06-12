---
id: phy-app-m1-07
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m1
moduleTitle: "Module 1: Foundations of Physics"
moduleGlyph: "🔭"
moduleSortOrder: 1
topicSlug: mathematical_tools
topicTitle: "Mathematical Tools"
topicSortOrder: 3
title: "Equations as Sentences: Algebra for Physics"
sortOrder: 7
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Read a physics equation as a relationship between quantities
  - Rearrange simple equations to isolate any variable
  - Substitute values with units into an equation correctly
integrationDomains: [mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Reads an equation aloud as a relationship, not just symbols
    - Correctly rearranges speed = distance / time for each variable
    - Substitutes values with units and reports the answer with its unit
  keywords: [rearrange, equation, subject, substitute, balance, both sides, unit]
  modelAnswer: |
    An equation like v = d/t is a sentence: "speed is how much distance is covered per unit
    time." Rearranging keeps the equation balanced — whatever is done to one side must be done
    to the other. Multiplying both sides by t gives d = v × t; dividing that by v gives
    t = d/v. Substituting d = 150 m and t = 30 s gives v = 150 m ÷ 30 s = 5 m/s — and the unit
    arrives automatically if units are carried through the working.
guidedSteps:
  - id: phy-app-m1-07-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      The equation v = d / t (speed = distance ÷ time) is rearranged to make **d** the subject. Which is correct?
    inputConfig:
      options:
        - "d = v / t"
        - "d = v × t"
        - "d = t / v"
        - "d = v + t"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["d = v × t"]
      rejectedFeedback: "Multiply both sides of v = d/t by t: the t on the right cancels, leaving v × t = d. Sense-check: travelling at 5 m/s for 30 s covers 5 × 30 = 150 m."
    hint: "What operation undoes 'divide by t'? Apply it to BOTH sides."
    reflectionPrompt: "Check the rearrangement with easy numbers: v = 2, t = 3. Does d come out right?"
  - id: phy-app-m1-07-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A cyclist covers 240 metres in 60 seconds. Using v = d / t, her speed is ________ m/s.
    inputConfig:
      placeholder: "4"
    markingRule:
      matchMode: CONTAINS
      accepted: ["4"]
      rejectedFeedback: "v = d/t = 240 m ÷ 60 s = 4 m/s. Substitute the values, keep the units, and the answer's unit (m/s) emerges from the division."
    hint: "Divide the distance by the time."
    reflectionPrompt: "Is 4 m/s a sensible cycling speed? Roughly how fast is that in km/h?"
  - id: phy-app-m1-07-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A friend says: "I just memorise the triangle trick for v, d and t — I don't need to understand rearranging." In 2–3 sentences, explain one advantage of understanding the rearranging itself.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [any equation, other equations, works for, understand, balance, new, general]
      rejectedFeedback: "Key idea: the triangle only works for one shape of equation. Understanding 'do the same to both sides' works for EVERY equation physics will ever throw at you — including ones with squares, sums, and constants."
    hint: "What happens to the triangle trick when the equation becomes E = ½mv²?"
    reflectionPrompt: "Which equation do you already know from everyday life that you could now rearrange?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Rearranging an equation is allowed because:"
    options:
      - "Symbols can be moved anywhere as long as you're careful"
      - "Performing the same operation on both sides keeps the two sides equal"
      - "Physics equations are only approximate anyway"
      - "The subject of an equation doesn't really matter"
    correctIndex: 1
    feedback: "An equation asserts that two expressions are equal. Doing identical operations to both sides preserves that equality — that's the entire licence for rearrangement."
  - type: MULTIPLE_CHOICE
    question: "Using d = v × t with v = 3 m/s and t = 20 s gives:"
    options: ["60 m", "6.67 m", "23 m", "60 m/s"]
    correctIndex: 0
    feedback: "d = 3 m/s × 20 s = 60 m. Note the units: (m/s) × s = m — the seconds cancel, leaving metres, exactly as a distance should."
---

# Hook

Here is a secret that changes how physics feels: **an equation is not a maths problem. It is a sentence.**

v = d/t does not say "divide some numbers". It says: *speed is how much ground you cover for each second that passes.* Once you hear equations talking — this quantity grows when that one grows, shrinks when that other one grows — formulas stop being things to memorise and become things you could almost have guessed.

Physicists don't memorise dozens of formula variations. They learn a handful of relationships and *rearrange* on demand, using one rule so simple it fits in a sentence: do the same thing to both sides. This lesson gives you that fluency.

# Lore Introduction

In the Observatory's chart room, an apprentice is frantically copying formula after formula into a grimoire: distance from speed and time, time from distance and speed, speed from distance and time — three entries for one idea. Magus Thorne reads over her shoulder and winces. "You are memorising echoes." He wipes two of the three lines from her slate, leaving only v = d/t. "The Academy's first arithmetic masters carved their balance scales above every doorway — have you never wondered why? An equation *is* a balance. Keep it level, and it will hand you any form you ask for. Memorise one truth, and own all its disguises."

# Core Learning

## Concept Introduction

**Reading equations.** Every physics equation states how quantities relate:

- v = d/t — "speed is distance covered per unit time." Double the distance in the same time → double the speed. Same distance in double the time → *half* the speed.
- The position of a symbol tells you its influence: on top (numerator), it pushes the result up; on the bottom (denominator), it drags the result down.

**Rearranging.** One rule covers everything: **whatever you do to one side, do to the other.** The equation is a balance; identical operations keep it level.

To make d the subject of v = d/t:

```
v = d/t          (d is trapped, divided by t)
v × t = d/t × t  (multiply BOTH sides by t)
v × t = d        (the t's on the right cancel)
```

To make t the subject, start from d = v × t and divide both sides by v: t = d/v.

**Substituting.** Replace symbols with values *and their units*, then compute both:

```
d = 150 m, t = 30 s
v = 150 m / 30 s = 5 m/s
```

The unit m/s emerges from the arithmetic — units obey the same algebra as numbers. If your final unit is nonsense (say, s/m for a speed), the working is wrong somewhere, guaranteed.

## Why It Matters

- Every module after this one expresses its laws as equations — F = ma, E = ½mv², P = E/t. Reading and rearranging them is the literacy that everything else assumes.
- The "carry the units" habit is a free, automatic error detector that working scientists and engineers rely on daily.
- Understanding beats memorising at scale: physics involves hundreds of equation forms, but only one balance rule.

## Worked Examples

**Example 1: How long will the journey take?**
A walker moves at 1.5 m/s and must cover 900 m. Want t, have v and d. Rearrange: t = d/v = 900 m ÷ 1.5 m/s = 600 s = 10 minutes. Units check: m ÷ (m/s) = s ✔.

**Example 2: Reading before calculating**
The equation P = E/t says power is energy delivered per unit time. Two kettles transfer the same energy E to the water, but one takes half the time. Without any numbers, the equation already answers: half the t, double the P — the faster kettle is twice as powerful.

**Example 3: A two-step rearrangement**
Density: ρ = m/V. What volume does 2.4 kg of oak (ρ = 600 kg/m³) occupy? Make V the subject: multiply both sides by V (ρV = m), divide both sides by ρ (V = m/ρ). Substitute: V = 2.4 kg ÷ 600 kg/m³ = 0.004 m³ — four litres. Units: kg ÷ (kg/m³) = m³ ✔.

## Common Mistakes

- **Moving symbols by vibe** — "the t hops over and flips" works until it doesn't. Always know which operation you applied to both sides.
- **Operating on only one side** — the instant the balance tips, everything after is wrong.
- **Substituting numbers without units** — you lose the built-in error check and risk mixing metres with kilometres.
- **Rearranging after substituting** — isolate the symbol you want *first*, then plug in numbers; it's fewer chances to slip.
- **Ignoring what the equation says** — if doubling t should halve v, but your answer grew, stop and re-read.

## Mental Model

Picture every equation as an **old-fashioned balance scale**, perfectly level. The two pans hold expressions, not numbers. You may add to, remove from, multiply, or divide the pans — but only ever *both at once*, and the scale stays level. Rearranging is just shifting contents between pans while preserving balance, until the symbol you want stands alone in one pan, with everything else in the other.

## Mini Summary

- ✔ Equations are sentences about how quantities influence each other
- ✔ One rule rearranges everything: do the same to both sides
- ✔ Isolate the symbol first, substitute values (with units!) second
- ✔ Units follow the same algebra — a wrong final unit exposes a wrong calculation
- ✔ Understand one relationship instead of memorising its three disguises

# Guided Practice Quest

Work through the guided steps to rearrange the speed equation, substitute real values, and articulate why the balance rule beats the memorised triangle.

# Solo Practice Quest

Take the density equation ρ = m/V. (1) Write one sentence saying what it means in plain language. (2) Rearrange it to make m the subject, showing the operation applied to both sides at each step; then do the same for V. (3) Use it: a gold bar measures 0.2 m × 0.05 m × 0.03 m and gold's density is 19,300 kg/m³ — find the bar's mass, carrying units through every line. (4) Finish with a sense-check sentence: is your answer plausible for something you could (try to) lift?

# Integration

**Mathematics**: This lesson is algebra wearing work clothes. The balance rule is the field axiom that equality is preserved under identical operations — and "carry the units" is dimensional analysis, a uniquely physical extension of algebra that mathematicians don't get to enjoy.

# Lore Conclusion

The apprentice with the grimoire stops copying. Thorne sets a problem none of her memorised forms cover — a quantity squared, buried in a denominator — and talks her through it with nothing but the balance rule, step by patient step, until the symbol she needs stands alone. She stares at the slate, then at him. "It's the same move. It's *always* the same move." Thorne returns the chalk to its tray. "Nine hundred years of charts upstairs," he says, "and every calculation in them is that one move, repeated with care." He glances at you. "Tomorrow, the charts themselves. A good graph is worth a thousand numbers — and lies are easier to spot in pictures."
