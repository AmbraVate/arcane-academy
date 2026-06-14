---
id: phy-app-m2-05
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: motion
topicTitle: "Motion"
topicSortOrder: 2
title: "Acceleration"
sortOrder: 5
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Calculate acceleration as change in velocity over time
  - Interpret negative acceleration (deceleration) correctly
  - Recognise free fall as constant acceleration of about 9.8 m/s²
integrationDomains: [mathematics, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Calculates acceleration using a = (v − u) / t with correct units
    - Interprets the sign of acceleration relative to the chosen direction
    - States that falling objects gain about 9.8 m/s of speed each second (ignoring drag)
    - Explains why turning at constant speed is acceleration
  keywords: [acceleration, m/s², change, velocity, deceleration, free fall, "9.8", gravity]
  modelAnswer: |
    Acceleration is the rate of change of velocity: a = (final velocity − initial velocity) ÷
    time, in m/s². A car going from rest to 24 m/s in 8 s accelerates at 3 m/s² — it gains
    3 m/s of speed every second. Negative acceleration (relative to the chosen positive
    direction) means slowing or speeding up the other way. In free fall, everything gains
    about 9.8 m/s per second regardless of mass (until air resistance interferes). Because
    velocity includes direction, turning at constant speed is also acceleration — which is
    why it requires a force.
guidedSteps:
  - id: phy-app-m2-05-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A motorbike accelerates from rest to 30 m/s in 6 seconds. Its acceleration is ________ m/s².
    inputConfig:
      placeholder: "5"
    markingRule:
      matchMode: CONTAINS
      accepted: ["5"]
      rejectedFeedback: "a = (v − u)/t = (30 − 0)/6 = 5 m/s². Each second, the bike gains 5 m/s of speed."
    hint: "Change in velocity divided by the time taken."
    reflectionPrompt: "List the bike's speed at t = 1, 2, 3 s. What pattern do you see?"
  - id: phy-app-m2-05-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A ball is dropped from a tall tower (ignore air resistance). One second after release its speed is about 10 m/s. After two seconds it is about:
    inputConfig:
      options:
        - "10 m/s — falling speed is constant"
        - "20 m/s — it gains about 10 m/s every second"
        - "40 m/s — it doubles every second"
        - "14 m/s — it gains less each second"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["20 m/s — it gains about 10 m/s every second"]
      rejectedFeedback: "Free fall is CONSTANT acceleration (~9.8, call it 10 m/s²): equal speed gains each second. 0 → 10 → 20 → 30 m/s. Not constant speed, not doubling."
    hint: "Constant acceleration means equal additions of speed per second."
    reflectionPrompt: "Why does a feather disobey this in air but obey it in a vacuum?"
  - id: phy-app-m2-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A tram slows smoothly from 12 m/s to rest in 4 s. In 2–3 sentences: calculate its acceleration (sign included, taking the direction of travel as positive), and explain what the sign tells a standing passenger about which way to brace.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["-3", "−3", "minus 3", negative, backward, forward, brace]
      rejectedFeedback: "a = (0 − 12)/4 = −3 m/s². The negative sign means the acceleration points backward, opposite to travel — passengers lurch forward and should brace backward."
    hint: "Final minus initial velocity — note which is bigger."
    reflectionPrompt: "Is 'deceleration' a different physical quantity, or just acceleration with a particular sign?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Acceleration measures:"
    options:
      - "How fast an object moves"
      - "How far an object travels each second"
      - "How quickly an object's velocity changes"
      - "The force an object carries"
    correctIndex: 2
    feedback: "Acceleration is the rate of change of velocity — m/s gained or lost (or redirected) per second, hence m/s²."
  - type: MULTIPLE_CHOICE
    question: "Ignoring air resistance, a heavy stone and a light pebble are dropped together. They:"
    options:
      - "Fall together — both gain ~9.8 m/s each second"
      - "The stone falls much faster"
      - "The pebble falls faster"
      - "Neither accelerates"
    correctIndex: 0
    feedback: "Free-fall acceleration is the same for all masses — Galileo's great discovery. Air resistance, not weight, is what separates feathers from hammers in everyday air."
---

# Hook

In 1971, Apollo 15 commander David Scott stood on the Moon holding a hammer in one hand and a falcon's feather in the other. On live television, he dropped both. No air, no drag — just gravity. The hammer and the feather struck the lunar dust **together**.

Four centuries earlier, Galileo had claimed exactly this would happen, against all common sense and twenty centuries of Aristotle. Falling is not about how heavy you are. It is an *acceleration* — and on any given world, it's the same acceleration for everything.

Acceleration is the most misunderstood word in motion: it doesn't mean "going fast", it means *velocity changing* — getting faster, getting slower, or turning. It's measured in the strange-looking unit m/s², and it is, as the next lessons will reveal, the one quantity in the universe that forces directly control.

# Lore Introduction

Thorne leads you to the Observatory's drop-shaft: a stone well plunging through all nine floors, built by some long-dead magus for exactly one purpose. Apprentices line the galleries with synchronised chronometers. "The old debate," Thorne says, hefting a cannonball and a marble. "Half the Academy once swore the cannonball would win the race to the bottom — twenty times the weight, twenty times the hurry, they reasoned." He hands you the marble. "On my mark, together." The two spheres fall — one second, past the third gallery; two seconds, past the seventh — and strike the sand below in a single thud. The gallery chronometers all agree. "Weight decides how hard the floor is hit," says Thorne. "It does not decide the *fall*. Today we measure what does."

# Core Learning

## Concept Introduction

**Acceleration** is the rate at which velocity changes:

```
a = (v − u) / t
```

where u = initial velocity, v = final velocity, t = time taken. Units: **m/s²** — read it as "metres per second, *per second*": how many m/s of velocity arrive (or depart) each second.

A car going 0 → 24 m/s in 8 s: a = 24/8 = 3 m/s². Its speedometer story: 3, 6, 9, 12... equal instalments of speed each second.

**Signs.** Choose a positive direction; then:
- a > 0 with motion: speeding up
- a < 0 with motion (often called *deceleration*): slowing down — e.g. 12 m/s to rest in 4 s gives a = −3 m/s²
- Deceleration is not a separate quantity — just acceleration pointing against the velocity.

**Free fall.** Near Earth's surface, ignoring air resistance, *everything* falls with the same constant acceleration:

```
g ≈ 9.8 m/s²  (≈ 10 for quick work)
```

Speeds in fall from rest: ~10 m/s after 1 s, ~20 m/s after 2 s, ~30 m/s after 3 s. Mass is irrelevant — hammer and feather, cannonball and marble. Air resistance is the spoiler that makes feathers flutter; remove the air and the race is always a tie.

**Turning is accelerating.** Velocity includes direction, so changing direction — even at constant speed — is acceleration. A roundabout, an orbit, a hammer-throw: all are accelerations, all need forces. This will matter enormously.

## Why It Matters

- Acceleration is the bridge quantity: forces (last topic) cause accelerations (this topic), and Newton's second law (next topic) is the exchange rate. You are one lesson from the centrepiece of mechanics.
- Vehicle safety lives here: crash harm scales with the acceleration your body undergoes; crumple zones and airbags exist purely to stretch the time t, shrinking a = Δv/t.
- g is the most-used constant in applied physics — from sports science to skyscraper lifts to planetary exploration (Moon g ≈ 1.6, Mars ≈ 3.7 m/s²).

## Worked Examples

**Example 1: Sprinter off the blocks**
A sprinter reaches 10 m/s in 2 s: a = 10/2 = 5 m/s² — half a g, briefly, from leg power alone. Mid-race, speed steady at 11 m/s: acceleration ≈ 0 despite furious effort (drag and internal losses are eating the work; the velocity isn't changing).

**Example 2: The braking calculation**
A car at 30 m/s (≈108 km/h) brakes at −7.5 m/s². Time to stop: t = (0 − 30)/(−7.5) = 4 s. Each second strips 7.5 m/s. This is why following distance is measured in seconds — the maths of stopping is acceleration maths.

**Example 3: Up, over, down**
Throw a ball straight up at 20 m/s (take up as positive; g ≈ 10). Its velocity: +20, +10, 0 (peak at 2 s), −10, −20 (back at hand, 4 s). One single constant acceleration −10 m/s² ran the whole show — including at the very top, where the ball is momentarily still *but still accelerating*. Velocity zero, acceleration not: hold that thought; it defeats most beginners.

## Common Mistakes

- **"Acceleration means going fast"** — a jet at steady 900 km/h has zero acceleration; a snail speeding up has some.
- **"At the top of the throw, acceleration is zero"** — velocity is momentarily zero; acceleration stays −9.8 m/s² throughout. If acceleration vanished at the top, the ball would hover.
- **"Heavier falls faster"** — only air resistance creates that illusion; free-fall acceleration is mass-blind.
- **Sign carelessness** — define positive once per problem and stay loyal; a stray sign flips your story from braking to crashing.
- **Confusing m/s² with m/s** — one is change-of-speed-per-second, the other speed; the units police from Module One are watching.

## Mental Model

Imagine velocity as **a bank balance and acceleration as the standing order that changes it**. A balance of 30 m/s with no standing order just sits there (cruising). A +3 m/s² order deposits 3 m/s every second; a −7.5 m/s² order withdraws 7.5 every second until the account empties (stopped) — or goes negative (moving backward). Free fall is the universe's fixed standing order: −9.8 m/s², applied to every account equally, billionaire cannonball and pauper feather alike.

## Mini Summary

- ✔ a = (v − u)/t, in m/s² — velocity gained or lost per second
- ✔ Deceleration = acceleration opposing the motion; handle with signs, not new words
- ✔ Free fall: everything accelerates at g ≈ 9.8 m/s², mass irrelevant (in vacuum)
- ✔ Momentarily-zero velocity does not mean zero acceleration (top of the throw!)
- ✔ Turning at constant speed is acceleration too — direction is part of velocity

# Guided Practice Quest

Work through the guided steps to compute a motorbike's gain, predict a dropped ball's speed ladder, and brief a tram passenger using nothing but a minus sign.

# Solo Practice Quest

Measure g yourself, roughly: drop a small object from a measured height (use 2 m if you can) and time the fall with your phone — repeat 10 times and average (Module One habits!). Then use h = ½gt² rearranged to g = 2h/t² to extract your value of g. Report: your raw times, mean, your g with units, and a percentage difference from 9.8 m/s². Finish with an honest error analysis: name your largest source of error, classify it (random or systematic — reaction time is which?), and propose the single best improvement.

# Integration

**Mathematics**: Acceleration is the rate-of-change of a rate-of-change — your first second derivative, informally. The speed ladder of free fall (10, 20, 30...) and the distance ladder (5, 20, 45 m...) hide the sequences and square laws you'll soon meet as kinematic equations.

**Biology**: Your inner ear contains accelerometers — fluid-filled canals that sense acceleration, not velocity. That's why smooth cruising in a plane feels like sitting still, while takeoff, turbulence, and lifts feel dramatic: your body, like physics, only notices *changes* in velocity.

# Lore Conclusion

At the drop-shaft's rim, Thorne shows you the dead magus's original logbook: columns of gallery timings, centuries old, the handwriting growing excited as the pattern emerges — *equal gains each second, for every object tried*. The final line is underscored three times: "The fall does not care what falls." Thorne closes it gently. "We can now describe motion completely — where, how fast, and how the fastness itself changes. Tomorrow we draw it: motion turned into pictures whose slopes and shapes you already know how to read." He glances down the dark shaft. "And after that, apprentice, we answer the question this whole module has been circling: *what decides the acceleration?* Three laws. One small book. A man named Newton."
