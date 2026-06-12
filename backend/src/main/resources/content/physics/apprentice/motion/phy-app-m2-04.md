---
id: phy-app-m2-04
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: motion
topicTitle: "Motion"
topicSortOrder: 2
title: "Describing Motion: Speed and Velocity"
sortOrder: 4
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - Calculate average speed from distance and time
  - Distinguish average speed from instantaneous speed
  - Use velocity (speed with direction) correctly in descriptions of motion
integrationDomains: [mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Calculates average speed with correct units
    - Explains the difference between average and instantaneous speed
    - Uses velocity with a direction in at least one example
  keywords: [average, instantaneous, speed, velocity, distance, time, direction, m/s]
  modelAnswer: |
    Average speed is total distance divided by total time: a 150 km journey in 2 hours averages
    75 km/h, even if the car was sometimes stopped and sometimes at 110 km/h. Instantaneous
    speed is the speed right now — what the speedometer shows. Velocity adds direction: 75 km/h
    north. The distinction matters because changing direction changes velocity even at constant
    speed, which is why circular motion at fixed speed still counts as changing motion.
guidedSteps:
  - id: phy-app-m2-04-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A train covers 240 km in 3 hours. Its average speed is ________ km/h.
    inputConfig:
      placeholder: "80"
    markingRule:
      matchMode: CONTAINS
      accepted: ["80"]
      rejectedFeedback: "Average speed = total distance ÷ total time = 240 ÷ 3 = 80 km/h."
    hint: "Divide the distance by the time."
    reflectionPrompt: "Was the train necessarily moving at 80 km/h at any particular moment?"
  - id: phy-app-m2-04-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A car's speedometer reads 50 km/h as it rounds a bend at constant speed. Which is true?
    inputConfig:
      options:
        - "Its speed and velocity are both constant"
        - "Its speed is constant but its velocity is changing"
        - "Its velocity is constant but its speed is changing"
        - "Neither speed nor velocity can be constant on a bend"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Its speed is constant but its velocity is changing"]
      rejectedFeedback: "Velocity includes direction. On a bend the direction changes continuously, so velocity changes even though the speedometer (speed) holds steady."
    hint: "What does velocity include that speed doesn't?"
    reflectionPrompt: "What must be acting on the car for its velocity to change? (Think back to resultant forces.)"
  - id: phy-app-m2-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Your journey to a friend's house: 1.2 km in 15 minutes, including a 3-minute wait at a crossing. In 2–3 sentences: calculate your average speed in m/s, and explain why your instantaneous speed was sometimes higher.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["1.3", "1.33", average, instantaneous, wait, stopped, faster]
      rejectedFeedback: "1200 m ÷ 900 s ≈ 1.3 m/s average. While stopped your instantaneous speed was 0, so while walking it must have exceeded the average to compensate."
    hint: "Convert to metres and seconds first. The wait drags the average down."
    reflectionPrompt: "When is the average a useful summary, and when does it hide what matters?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Average speed is defined as:"
    options:
      - "The speed at the journey's midpoint"
      - "Total distance divided by total time"
      - "The highest speed reached"
      - "Half of the top speed"
    correctIndex: 1
    feedback: "Average speed = total distance ÷ total time, stops and slow patches included."
  - type: MULTIPLE_CHOICE
    question: "Which describes a velocity?"
    options: ["15 m/s", "15 m/s due west", "15 metres", "15 seconds"]
    correctIndex: 1
    feedback: "Velocity is speed plus direction. '15 m/s' alone is just a speed."
---

# Hook

A speed camera and a satnav disagree about your journey. The satnav says your *average* speed was a saintly 38 km/h. The camera says that at one particular lamppost you were doing 65. Both are right — they are answering different questions.

Average speed summarises a whole journey: total distance over total time, with every red light and traffic jam folded in. Instantaneous speed is the now — what the speedometer needle shows this exact moment. Confuse the two and you'll misread everything from sports statistics to physics problems. Add direction to the mix and you get velocity, the quantity that motion *really* runs on.

This lesson nails down the vocabulary that the whole of mechanics will be written in.

# Lore Introduction

The Observatory's courier service is the stuff of legend: relay riders carrying star-charts between academies. Thorne shows you the dispatch ledger. "Rider Calla, the eastern run: ninety leagues in three days. The Guild calls her our fastest rider. Rider Bren, the mountain run: forty leagues in three days. Slower?" He raises an eyebrow. "Bren's road climbs switchbacks where a trot is reckless; on the flat he outpaces Calla two strides to one. The ledger's 'leagues per day' tells you about *journeys*. It is silent about *moments*." He hands you the ledger and a fresh page. "Mechanics needs both numbers, apprentice — the journey's average and the moment's truth — and it needs to know which way the rider was pointing. Today we learn to keep all three straight."

# Core Learning

## Concept Introduction

**Average speed** compresses a whole journey into one number:

```
average speed = total distance ÷ total time
```

A 240 km trip in 3 h averages 80 km/h — including fuel stops. The average can be a speed the traveller *never actually moved at*.

**Instantaneous speed** is the speed at a single moment — the speedometer reading. Formally it's the average over a vanishingly short interval; practically, it's "how fast, right now". (Speed cameras measure something close to this; average-speed-check zones measure the other one. Drivers who confuse them collect fines.)

**Velocity** is speed with direction attached — a vector: 80 km/h *north*. Two consequences you must internalise:

1. Velocity uses **displacement**, not distance: average velocity = displacement ÷ time. A round trip has positive average speed but zero average velocity.
2. Velocity changes whenever **either** the speed **or** the direction changes. A car circling a roundabout at a steady 30 km/h has constant speed and continuously changing velocity — and from last lesson, changing motion needs an unbalanced force. (Feel that click into place.)

Typical magnitudes to anchor your intuition: walking ≈ 1.5 m/s; sprinting ≈ 10 m/s; city traffic ≈ 14 m/s; motorway ≈ 30 m/s; passenger jet ≈ 250 m/s; sound ≈ 340 m/s.

## Why It Matters

- Every kinematics formula coming in the next lessons assumes you know exactly which speed it means; mixing average and instantaneous quietly wrecks calculations.
- The velocity/speed distinction is the doorway to understanding circular motion, orbits, and why satellites accelerate without speeding up.
- Transport planning, athletics coaching, and delivery logistics all live on the average-vs-instantaneous distinction: the marathon is won on average speed, the photo-finish on instantaneous.

## Worked Examples

**Example 1: The deceptive average**
A cyclist rides 20 km out at 20 km/h (1 hour), then back at 30 km/h (40 min). Average speed: total 40 km ÷ 1.67 h ≈ **24 km/h** — not the tempting (20+30)/2 = 25. More time was spent at the slower speed, so it weighs more in the average. Average velocity for the round trip: displacement zero → **0 km/h**.

**Example 2: From ledger to moment**
A falling stone covers 5 m in its 1st second, 15 m in its 2nd. Average over two seconds: 20 m ÷ 2 s = 10 m/s. But it was clearly slower than that early and faster late — the average hides the speeding-up. To expose it, take shorter intervals: average over the final tenth of a second ≈ instantaneous speed at the end ≈ 20 m/s. Shrinking the window turns averages into instants.

**Example 3: Direction earning its keep**
Two ferries each cruise at 18 km/h, one heading east, one west. Same speed; velocities of +18 and −18 km/h (taking east positive). Their *relative* velocity is 36 km/h — which is the number that matters if they're heading at each other. Direction isn't a garnish; it changes the safety calculation.

## Common Mistakes

- **Averaging the speeds instead of the journey** — (20+30)/2 fails whenever the times spent differ; always go back to total distance ÷ total time.
- **Reading the average as a moment** — "averaged 80, so was doing 80 at noon" doesn't follow.
- **Dropping direction from velocity answers** — a velocity without direction is just a speed wearing the wrong name.
- **"Constant speed = constant velocity"** — false on any curve.
- **Unit chaos** — km with minutes, m with hours; convert first (recall: ÷3.6 turns km/h into m/s).

## Mental Model

Think of a journey as a **film, and the two speeds as two ways of describing it**. Average speed is what you get from the running time and the credits — one number for the whole picture, smoothing every car chase and every still scene into uniform porridge. Instantaneous speed is a single *frame*, examined closely. Velocity is that frame plus the arrow showing where the camera is pointed. No single description is "the right one" — but you must always know which one you're being asked for.

## Mini Summary

- ✔ Average speed = total distance ÷ total time — stops included, never an average of speeds
- ✔ Instantaneous speed = the moment's reading; shrink the time window to find it
- ✔ Velocity = speed + direction; computed from displacement, not distance
- ✔ Changing direction changes velocity, even at constant speed
- ✔ Anchor your intuition: walk 1.5, sprint 10, motorway 30, jet 250 (all m/s)

# Guided Practice Quest

Work through the guided steps to compute a train's average, catch a cornering car changing velocity at constant speed, and audit your own journey including its red-light pause.

# Solo Practice Quest

Time three real journeys this week (or reconstruct recent ones): a walk, a ride or drive, and any third. For each: total distance (map app), total time, average speed in m/s and km/h. Then, for one journey, estimate your *maximum* instantaneous speed and sketch — roughly — how your speed varied over the journey's duration. Finish with the velocity question: for one journey, state your average velocity (magnitude and direction), and explain in a sentence why it differs from your average speed if your route wasn't a straight line.

# Integration

**Mathematics**: "Shrink the interval until average becomes instantaneous" is the founding move of differential calculus — you have just informally met the derivative. When you eventually meet dx/dt formally, it will be this lesson in symbols.

# Lore Conclusion

You re-enter the courier ledger as Thorne dictates: Calla — ninety leagues east in three days, average thirty leagues per day *eastward*; Bren — forty leagues *south-by-mountain*, average thirteen, peak pace on the flats far higher. The Guild master reads your double-entry version and grunts approval: routes can now be compared honestly. "Averages for planning, instants for riding, directions always," Thorne summarises. "But we have only described motion that *is*. The far more interesting question —" he smiles, and you sense the module pivoting, "— is motion that is *becoming*. Faster, slower, turning. Tomorrow: acceleration, the quantity forces actually talk to."
