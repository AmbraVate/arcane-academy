---
id: phy-app-m1-11
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m1
moduleTitle: "Module 1: Foundations of Physics"
moduleGlyph: "🔭"
moduleSortOrder: 1
topicSlug: physical_quantities
topicTitle: "Physical Quantities"
topicSortOrder: 4
title: "Scalars and Vectors"
sortOrder: 11
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - Classify quantities as scalars or vectors
  - Distinguish distance from displacement and speed from velocity
  - Combine simple vectors along a line, respecting direction
integrationDomains: [mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines a scalar (size only) and a vector (size and direction) correctly
    - Sorts at least six quantities into the right categories
    - Explains the distance/displacement distinction with a concrete journey
    - Combines two opposing quantities correctly using signs
  keywords: [scalar, vector, magnitude, direction, displacement, velocity, distance, speed]
  modelAnswer: |
    A scalar has size only — mass, temperature, time, distance, speed, energy. A vector has
    size AND direction — displacement, velocity, acceleration, force. Walking 3 km to a shop
    and 3 km back covers a distance of 6 km but a displacement of zero: you end where you
    began. Vectors along a line combine with signs: a 50 N push right and a 30 N push left
    give 20 N right. Direction is not decoration — it changes the arithmetic.
guidedSteps:
  - id: phy-app-m1-11-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which list contains **only vectors**?
    inputConfig:
      options:
        - "Force, velocity, displacement"
        - "Mass, temperature, time"
        - "Speed, distance, energy"
        - "Force, mass, speed"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Force, velocity, displacement"]
      rejectedFeedback: "Force, velocity, and displacement all have directions ('10 N downward', '3 m/s east'). Mass, temperature, time, speed, distance and energy are sizes only — scalars."
    hint: "For each quantity ask: does 'which way?' make sense?"
    reflectionPrompt: "Why does 'a temperature of 30 °C north' make no sense, while 'a force of 30 N north' does?"
  - id: phy-app-m1-11-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A jogger runs 400 m east, then 400 m west, back to the start. Distance covered: 800 m. Displacement: ________ m.
    inputConfig:
      placeholder: "0"
    markingRule:
      matchMode: CONTAINS
      accepted: ["0", zero]
      rejectedFeedback: "Displacement is the straight-line change in position — finish minus start. Ending where you began means zero displacement, however far your legs worked."
    hint: "Displacement only cares about where you ended relative to where you started."
    reflectionPrompt: "Whose job cares about the 800 m (a fitness coach?) and whose cares about the 0 m (a navigator?)"
  - id: phy-app-m1-11-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Two children pull a toy: one with 50 N to the left, one with 70 N to the right. In 2–3 sentences, state the combined force (size and direction) and explain why simply adding 50 + 70 = 120 N would be wrong.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["20", right, direction, opposite, cancel, sign]
      rejectedFeedback: "Opposite directions partially cancel: 70 N right − 50 N left = 20 N to the right. Adding sizes while ignoring directions treats vectors as scalars — the toy is NOT pulled with 120 N."
    hint: "Give one direction a + sign and the other a − sign, then add."
    reflectionPrompt: "What happens if both children pull with exactly 60 N in opposite directions?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Speed and velocity differ because:"
    options:
      - "Velocity is always larger"
      - "Speed is a scalar (size only); velocity is a vector (size plus direction)"
      - "Speed is for cars, velocity for planets"
      - "They are exact synonyms"
    correctIndex: 1
    feedback: "A car on a roundabout at a steady 30 km/h has constant speed but constantly *changing* velocity — its direction is changing. The distinction will matter enormously when we meet acceleration."
  - type: MULTIPLE_CHOICE
    question: "A boat aims north at 4 m/s while a current pushes it east at 3 m/s. Its overall speed is:"
    options: ["7 m/s", "1 m/s", "5 m/s", "Cannot be combined"]
    correctIndex: 2
    feedback: "Perpendicular vectors combine by the Pythagorean rule: √(4² + 3²) = √25 = 5 m/s, pointing north-east-ish. Vectors at angles need geometry, not plain addition."
---

# Hook

A pilot files this report: "Flew 500 km." Useful? Not remotely — 500 km *which way*? Toward the destination, or in circles around it? Now compare: "Flew 500 km due north." Suddenly air traffic control can work with it.

Some quantities are complete the moment you state their size: a mass of 70 kg, a temperature of 20 °C, a film lasting 2 hours. Asking "70 kg in which direction?" is gibberish. But for other quantities — a push, a movement, a wind — the direction isn't extra detail. It's *half the information*, and ignoring it produces wrong answers, not just incomplete ones: two 60 N forces can add to 120 N, or to nothing at all, depending entirely on their directions.

This split — scalars versus vectors — runs through every chapter of physics you will ever study. Learn it now and everything downstream gets easier.

# Lore Introduction

In the Observatory's navigation hall hangs a famous disgrace: the log of the survey ship *Meridian*, which set out to chart the northern straits. Its log records faithfully, day by day: "made 40 leagues... made 35 leagues... made 42 leagues." Thorne lets you read a full page before asking, "So — where was the ship?" You look up. Nothing on the page says. Forty leagues *in what direction*? The log never recorded it; the captain trusted his memory. "Three hundred leagues of honest sailing," Thorne says, "and on day nine, in fog, his memory failed. They made landfall eighty leagues from where they believed — on rocks." He closes the log. "Size without direction told them how *hard* they had sailed. It could never tell them *where they were*. Learn which of your quantities carry arrows, apprentice. It is not pedantry. It is the rocks."

# Core Learning

## Concept Introduction

A **scalar** is a quantity fully described by a size (magnitude): mass, time, temperature, distance, speed, energy, volume. Scalars obey ordinary arithmetic: 2 kg + 3 kg = 5 kg, always.

A **vector** has magnitude *and* direction: displacement, velocity, acceleration, force. "10 N" is an incomplete vector; "10 N vertically downward" is complete. On diagrams, vectors are arrows — length showing size, orientation showing direction.

Two famous scalar/vector pairs:

| Scalar | Vector | The difference |
|--------|--------|----------------|
| Distance — total path length | Displacement — straight line from start to finish, with direction | Walk around a full city block: distance ≈ 400 m, displacement = 0 |
| Speed — how fast | Velocity — how fast, which way | A roundabout at steady 30 km/h: constant speed, *changing* velocity |

**Combining vectors.** Along a single line, use signs: choose a positive direction, attach − to anything pointing the other way, and add. 70 N right + (−50 N) left = +20 N → 20 N right. Opposing equal vectors sum to zero — which is how a tug-of-war can strain both teams and move nobody.

At right angles, vectors combine geometrically (the 3-4-5 boat-and-current triangle gives 5 m/s). The general toolkit comes later; for now: *never add vector magnitudes without checking directions first*.

## Why It Matters

- Every force problem in the next module begins by combining vectors; sign errors here become wrong-direction answers there.
- Navigation, GPS, and flight planning live entirely in vector-land — your phone computes displacement vectors continuously.
- The distinction explains otherwise-baffling statements you'll meet soon, like "a satellite in orbit accelerates constantly while its speed never changes" — impossible for scalars, routine for vectors.

## Worked Examples

**Example 1: The commute audit**
You travel 6 km east to work, then 6 km west home. Distance for the day: 12 km (fuel, fatigue, shoe leather care about this). Displacement: zero (your *position* is what it was). Both numbers are true; they answer different questions.

**Example 2: Signs doing the work**
An ascending lift: gravity pulls down with 7000 N; the cable pulls up with 7500 N. Take up as positive: +7500 + (−7000) = +500 N upward. The lift accelerates upward. Same numbers with the cable slackened to 6500 N: −500 N → acceleration downward. The sign *is* the physics.

**Example 3: When ignoring direction lies to you**
A swimmer crosses a river aiming straight across at 1 m/s while the current carries her downstream at 1 m/s. "Her speed is 1 + 1 = 2 m/s" — wrong. The vectors are perpendicular: √(1² + 1²) ≈ 1.4 m/s, angled downstream. Direction-blind addition overestimated her by 40%.

## Common Mistakes

- **Adding magnitudes of opposing vectors** — 50 N left + 70 N right is 20 N, not 120 N.
- **Using distance and displacement interchangeably** — they agree only when motion never changes direction.
- **"Constant speed means no acceleration"** — false on curves; velocity changes when *direction* changes, even at fixed speed.
- **Dropping the direction from an answer** — a vector answer without direction is half an answer; "20 N" should be "20 N to the right".
- **Assuming all quantities need direction** — "5 seconds north" is nonsense; don't vectorise scalars either.

## Mental Model

Picture every vector as an **arrow you can slide around but never rotate or stretch**. Combining vectors is laying arrows head-to-tail and drawing one new arrow from the first tail to the last head. Two arrows nose-to-nose cancel; two aligned arrows reinforce; perpendicular arrows make a triangle. Scalars, by contrast, are just *amounts in a jar* — pour them together and they only ever add. Ask of every new quantity: jar, or arrow?

## Mini Summary

- ✔ Scalars carry size only; vectors carry size and direction
- ✔ Distance/speed are scalars; displacement/velocity/force are vectors
- ✔ Along a line: set a positive direction and let signs do the cancelling
- ✔ Perpendicular vectors combine by the Pythagorean rule, not addition
- ✔ A vector answer without its direction is incomplete

# Guided Practice Quest

Work through the guided steps to sort quantities into jars and arrows, audit a round trip, and combine opposing forces with signs.

# Solo Practice Quest

Map a short real journey you make often (to a shop, a friend's house, around your home). (1) Sketch it roughly to scale. (2) Measure or estimate the *distance* along your actual path and the straight-line *displacement* (with its compass direction). (3) Time the journey and compute both your average speed (distance/time) and the magnitude of your average velocity (displacement/time) — they will differ; explain why in a sentence. (4) Invent one everyday situation where treating a vector as a scalar would give someone a badly wrong answer, and spell out the error.

# Integration

**Mathematics**: Vectors are your first genuinely new kind of number — objects that add by geometry instead of the number line. The head-to-tail rule, signed one-dimensional arithmetic, and the Pythagorean combination are the seeds of vector algebra and trigonometry, which mathematics develops into one of its most powerful branches.

# Lore Conclusion

Beneath the *Meridian*'s log, you notice, the navigation hall keeps a second book open: the log of her sister ship, which sailed the same straits a year later. Every entry reads like a prayer of atonement: "made 38 leagues, *bearing north by north-east*..." That ship charted the entire coastline and came home. Thorne taps the second log. "Same sea. Same fog. One added arrow." He leads you out of the hall. "You now hold all of Module One: measurement, method, mathematics, and the quantities themselves. One short lesson remains — a habit of checking that ties everything together. Then the foundations are poured, and we begin building."
