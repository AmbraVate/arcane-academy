---
id: phy-jun-m1-04
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: circular_motion
topicTitle: "Circular Motion"
topicSortOrder: 2
title: "Describing Circular Motion"
sortOrder: 4
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Explain why circular motion at constant speed is accelerated motion
  - Use period, frequency, and speed (v = 2πr/T) for circular paths
  - Identify the direction of velocity and acceleration on a circular path
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - States that velocity is tangent to the circle and constantly changing direction
    - Computes orbital speed via v = 2πr/T
    - States the acceleration points toward the centre (centripetal)
  keywords: [tangent, centripetal, period, frequency, 2πr, toward the centre, direction]
  modelAnswer: |
    An object circling at constant speed is accelerating the whole time, because velocity
    includes direction and the direction never stops changing. The velocity points along the
    tangent — release the object and it flies off straight, never spiralling. Speed follows
    from the geometry: one circumference per period, v = 2πr/T, with frequency f = 1/T. The
    acceleration — and therefore the resultant force — points toward the centre at every
    instant: centripetal, 'centre-seeking'. Without that inward pull the circle cannot close;
    with it, the object perpetually falls toward the centre while its tangential motion
    perpetually carries it past.
guidedSteps:
  - id: phy-jun-m1-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A hammer-thrower whirls the hammer in a circle and releases it. The hammer flies off:
    inputConfig:
      options:
        - "Along the tangent — straight in the direction of its velocity at the moment of release"
        - "Spiralling outward"
        - "Radially outward, away from the centre"
        - "Continuing in a circle briefly before straightening"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Along the tangent — straight in the direction of its velocity at the moment of release"]
      rejectedFeedback: "Velocity is tangent to the circle; the instant the wire's inward pull ends, Newton's first law takes over — straight line, current direction. No outward force ever existed to spiral it."
    hint: "What direction was the velocity at the instant of release, and what does the first law say happens with no force?"
    reflectionPrompt: "Where in the swing must the thrower release to send the hammer down the field?"
  - id: phy-jun-m1-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A carousel horse rides a circle of radius 5 m, completing one revolution every 10 s. Its speed is v = 2πr/T ≈ ________ m/s (one decimal place).
    inputConfig:
      placeholder: "3.1"
    markingRule:
      matchMode: CONTAINS
      accepted: ["3.1", "3.14"]
      rejectedFeedback: "v = 2π × 5 / 10 = π ≈ 3.1 m/s — one circumference (≈31.4 m) per 10 s period."
    hint: "Circumference 2πr, divided by the period."
    reflectionPrompt: "A child on an inner horse (r = 2.5 m) has the same period. Same speed? Same anything?"
  - id: phy-jun-m1-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      "The Moon circles the Earth at constant speed, so it isn't accelerating." Correct this statement in 2–3 sentences, naming the direction of the Moon's acceleration and the force responsible.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [direction, velocity, changing, toward, centre, Earth, gravity, centripetal]
      rejectedFeedback: "Velocity includes direction, and the Moon's direction changes continuously — so it accelerates continuously, toward the Earth (centripetally). Earth's gravity supplies exactly that inward pull: the Moon is perpetually falling around us."
    hint: "Constant speed ≠ constant velocity on a curve. Where must the resultant point to bend the path?"
    reflectionPrompt: "If the Moon is always falling toward Earth, why does it never arrive?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "For an object in uniform circular motion, the velocity and acceleration are respectively:"
    options:
      - "Both toward the centre"
      - "Tangent to the circle; toward the centre"
      - "Toward the centre; tangent"
      - "Both tangent"
    correctIndex: 1
    feedback: "Velocity rides the tangent (the direction of travel this instant); acceleration points inward, perpetually bending the path. They stay perpendicular — which is why the SPEED never changes."
  - type: MULTIPLE_CHOICE
    question: "Frequency and period of circular motion are related by:"
    options: ["f = T", "f = 1/T", "f = 2πT", "f = T²"]
    correctIndex: 1
    feedback: "Two revolutions per second means half a second per revolution: f = 1/T — the same reciprocal pair you met with waves."
---

# Hook

Here is a sentence that sounds wrong and is perfectly true: *the Moon is accelerating toward the Earth right now, and has been for four billion years, without getting any closer.*

Circular motion is where Apprentice-tier vectors pay their debts. Velocity has direction; on a circle the direction changes every instant; therefore the velocity changes every instant; therefore — at rock-steady speed — the object *accelerates continuously*. And acceleration needs a force. Every circle in the universe — carousel, cornering car, spinning washing machine drum, orbiting moon — is held closed by a force pointing relentlessly at its centre. Let go, and the circle ends not in an outward fling but in a perfectly straight Newtonian line down the tangent. The hammer-throw, the slingshot, and the sparks off a grinding wheel all testify: *there is no outward force.* There never was.

# Lore Introduction

Vex's flywheel was the overture; today the Mechanica's rotunda is open — a circular hall housing the Academy's whirling apparatus: governors, carousels of brass, a great hand-cranked turntable set into the floor. Vex stands you on the turntable's edge with a ball in your hand. "Walk to the centre and stand still. When I crank, you ride the circle. The ball rides with you. And when I say *release* — before you do it — chalk on the floor where you believe the ball will land." You ride; the rotunda smears past; you chalk your guess — somewhere outward, surely — and at "release!" you open your hand. The ball does not fly outward. It departs in a clean straight line down the tangent, bounces, and rolls to rest nowhere near your chalk mark. Vex lets the turntable spin down. "Everyone chalks outward," he says. "Everyone, for two hundred years. The circle has been lying to your instincts since you first rode a roundabout. Today we put the lie under arithmetic."

# Core Learning

## Concept Introduction

**The kinematics of going round.** For uniform circular motion (constant speed, radius r):

- **Period T** — time for one revolution. **Frequency f = 1/T** — revolutions per second (the reciprocal pair from your wave lessons, returned).
- **Speed**: one circumference per period —

```
v = 2πr / T = 2πr f
```

- **Velocity** points along the **tangent** — the direction of travel *this instant*. Released objects (hammers, slingshot stones, mud off a tyre) depart straight along it: first law, instantly resumed.

**The acceleration nobody feels coming.** Speed constant, direction forever turning: the velocity *vector* changes continuously, so there is continuous acceleration. Its direction — work it out with two velocity arrows a moment apart — points **toward the centre**: *centripetal* ("centre-seeking"). Because it stays perpendicular to the velocity, it bends the path without ever changing the speed — the one geometry where acceleration and constant speed coexist forever.

**Therefore: a force.** Newton's second law demands a resultant along the acceleration: every circular path requires a **net inward force**. Not a new kind of force — a *job description* filled by ordinary forces: tension (hammer wire), friction (cornering tyres), gravity (orbits), normal force (banked walls). Cut the supplier and the circle opens into a tangent on the spot.

**And the "outward fling"?** What you feel on a roundabout is your own inertia trying to go *straight* while the circle's hardware hauls you inward. The outward sensation is real; the outward *force* is fiction — physics calls the fiction "centrifugal" and files it under reference-frame illusions (a Senior-tier story).

## Why It Matters

- Every cornering vehicle, banked road, and rated bend is a centripetal calculation — next lesson makes it quantitative.
- Orbits are this lesson with gravity as supplier: satellites, the Moon, and planetary years all run on v = 2πr/T.
- Rotating machinery — turbines, centrifuges, hard drives, washing machines — lives and dies by the inward forces its materials can supply; engineers size them with these equations.

## Worked Examples

**Example 1: How fast is "stationary"?**
You, sitting "still", ride the Earth's daily rotation. At 45° latitude, r ≈ 4,500 km from the spin axis; T = 24 h. v = 2π × 4.5×10⁶ / 86,400 ≈ **330 m/s** — faster than an airliner, courtesy of the planet's turntable. (The required centripetal acceleration is a feeble 0.024 m/s² — gravity supplies it with 99.7% to spare, which is why you don't notice.)

**Example 2: The washing machine's arithmetic**
A drum of radius 0.25 m spins at 1,200 rpm: f = 20 Hz, T = 0.05 s. v = 2π × 0.25 / 0.05 ≈ **31 m/s** (112 km/h at the drum wall!). Your clothes circle because the drum wall pushes them inward; the *water* at the perforations gets no such push — it goes straight, out through the holes, down the tangent. "Spun outward"? No: *left behind, straight*. The dryer is a first-law machine.

**Example 3: Reading a year**
Earth's orbit: r ≈ 1.5 × 10¹¹ m, T = 1 year ≈ 3.15 × 10⁷ s. v = 2πr/T ≈ **30 km/s** — you are crossing space at thirty kilometres a second as you read, held to the circle by an inward pull the next topic will name and weigh. Every "v = 2πr/T" you compute henceforth is practice for celestial mechanics.

## Common Mistakes

- **The outward-force fiction** — nothing pulls released objects outward; they go straight down the tangent. Chalk it, test it, retire it.
- **"Constant speed means no acceleration"** — on curves, never; direction-change is acceleration. (This is the Apprentice vector lesson collecting its debt.)
- **Drawing acceleration along the tangent** — in uniform circular motion it is purely inward; tangential acceleration would mean changing *speed*.
- **Confusing period and frequency** — reciprocals, as with waves; check units before computing.
- **Forgetting r is to the axis, not the ground** — your circular radius standing on Earth is your distance from the spin *axis*, latitude-dependent.

## Mental Model

Uniform circular motion is **a perpetual argument between two stubborn parties**. Inertia speaks first, every instant: *"Straight ahead, as we are."* The centripetal supplier — wire, tyre, gravity — replies, every instant: *"One small correction inward."* Neither ever wins: the path bends exactly as fast as it tries to straighten, forever, and the result is the only closed compromise — a circle. Cut the inward voice mid-argument and inertia finishes its sentence: a straight line, tangent to the very point where the conversation died. Every circle you will ever analyse is this argument; your job is simply to identify who is speaking for the centre.

## Mini Summary

- ✔ Circling at constant speed = continuous acceleration (direction is part of velocity)
- ✔ Velocity rides the tangent; released objects depart straight — no outward force exists
- ✔ v = 2πr/T = 2πrf; period and frequency are reciprocals
- ✔ Acceleration (and the resultant force) point at the centre: centripetal — a job, not a new force
- ✔ The "outward fling" is inertia's straight-line protest, felt from inside the turn

# Guided Practice Quest

Work through the guided steps to release the hammer down its honest tangent, clock a carousel at π metres per second, and put the Moon's eternal fall into respectable English.

# Solo Practice Quest

Three rotations of your own: (1) *Tangent trial*: swing a small soft object on a string in a horizontal circle (outdoors, clear space) and release at marked points; chart where it actually lands against where the outward-fling instinct predicts, for three release positions. (2) *Compute your day*: find your latitude, estimate your distance to Earth's axis (r = 6,400 km × cos latitude), and compute your rotational speed via v = 2πr/T; compare it with a vehicle you know. (3) *Audit a spinner*: choose any rotating machine in your life (bike wheel, fan, salad spinner, hard drive if you're vintage) — estimate its r and T (or rpm), compute the rim speed, and identify *what physical thing supplies the inward force* holding the rim together. One sentence on what happens at the rim if that supplier ever fails.

# Integration

**Mathematics**: Circular motion is trigonometry set in motion — the circling point's shadow on a wall oscillates as a sine wave, which is why circles and waves share the symbols T, f, and 2π. The radian (arc length per radius) waits one tier ahead to make these formulas effortless.

**Engineering**: Rim speed and inward-force budgets govern real machine limits: flywheel energy storage chases high v while fearing rim failure (the tangent is where fragments go), centrifuges separate blood and uranium by making things "want" to go straight, and every cornering specification in vehicle engineering begins from this lesson's geometry.

# Lore Conclusion

Your chalk marks from the morning stay on the rotunda floor — Vex insists; the floor is generations deep in wrong guesses, a fresco of corrected instinct. Beside yours you chalk the correction: a clean tangent, labelled. "The circle's lie, retired," Vex says. "But notice what we have NOT yet done." He sets the great turntable spinning with you aboard once more, and this time hands you a spring balance tied to the centre-post, its needle trembling as it holds you in. "We have described the circle. We have not *priced* it. How hard must the centre pull, for this radius, at this speed, for this junior?" He reads the balance over your shoulder and notes the figure with theatrical care. "Tomorrow: the centripetal bill, itemised — and why every road engineer, fairground builder, and orbital mechanic keeps the same three-line formula above the desk."
