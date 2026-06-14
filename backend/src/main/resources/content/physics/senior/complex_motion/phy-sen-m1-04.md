---
id: phy-sen-m1-04
domainId: physics
tier: SENIOR
moduleId: phy-sen-m1
moduleTitle: "Module 1: Advanced Dynamics"
moduleGlyph: "🌀"
moduleSortOrder: 1
topicSlug: complex_motion
topicTitle: "Complex Motion"
topicSortOrder: 4
title: "Projectiles, Relative Motion, and Coupled Systems"
sortOrder: 4
xpReward: 120
practiceType: NONE
questType: INVESTIGATION
feynmanPrompt: "Explain to a first-year apprentice why a cannonball's horizontal and vertical motions don't interfere with each other, and why 'how fast is it moving?' has no answer until you say who is measuring."
learningObjectives:
  - Decompose projectile motion into independent horizontal and vertical components and solve for range, height, and time of flight
  - Transform velocities between reference frames and explain why velocity is frame-dependent
  - Describe qualitatively how coupled oscillators exchange energy and what normal modes are
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains that projectile motion separates into independent components: constant velocity horizontally, constant acceleration vertically"
    - "Uses or describes a worked projectile calculation (time of flight from vertical motion, range from horizontal motion)"
    - "Explains relative velocity with a concrete example and states that velocity depends on the observer's frame"
    - "Describes coupled oscillators exchanging energy and identifies normal modes as patterns where the whole system oscillates at one frequency"
  keywords: [component, independent, frame, relative, coupled, normal mode]
  modelAnswer: |
    A projectile's motion looks complicated, but it is two simple motions running at the same
    time without talking to each other. Horizontally there is no force (ignoring air), so the
    horizontal velocity never changes. Vertically, gravity accelerates it at 9.8 m/s²
    regardless of how fast it moves sideways. To solve any projectile problem I split the
    initial velocity into components, use the vertical motion to find the time of flight,
    then feed that time into the horizontal motion to find the range. For a ball launched at
    20 m/s at 30°: vertical component 10 m/s gives a flight time of about 2.0 s, and the
    horizontal component 17.3 m/s carried for 2.0 s gives a range of about 35 m.

    Velocity has no absolute value — it depends on who measures it. A passenger walking at
    1 m/s toward the front of a barge moving at 4 m/s is moving at 5 m/s relative to the
    riverbank but 1 m/s relative to the barge. Both numbers are correct; they answer
    different questions. To transform between frames you add the frame's velocity as a
    vector.

    Coupled systems are oscillators connected so they can trade energy. Two pendulums joined
    by a weak spring will pass their swing back and forth: one dies down as the other grows,
    then the energy returns. Underneath that exchange are normal modes — special patterns
    (both swinging together, or exactly opposite) in which the whole system oscillates at a
    single pure frequency. Any motion of the coupled system is a mix of its normal modes.
guidedSteps:
  - id: phy-sen-m1-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A cannonball is fired horizontally at 40 m/s from a cliff. At the same instant, an
      identical ball is simply dropped from the same height. Ignoring air resistance, which
      ball hits the ground first?
    inputConfig:
      options:
        - "The dropped ball — it has no horizontal speed slowing its fall"
        - "The fired ball — its speed carries it down faster"
        - "Both hit at the same time — vertical motion is independent of horizontal motion"
        - "It depends on the mass of each ball"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Both hit at the same time — vertical motion is independent of horizontal motion"]
      rejectedFeedback: "Gravity only acts vertically, and the horizontal motion has no vertical component to contribute. Both balls start with zero vertical velocity and accelerate downward identically — they land together."
    hint: "Ask what force acts on each ball, and in which direction. Does the 40 m/s sideways motion change anything about the downward pull?"
    reflectionPrompt: "Why do so many people intuitively expect the fired ball to stay up longer?"
  - id: phy-sen-m1-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A ball is launched at 20 m/s at 30° above the horizontal. Its vertical velocity
      component is 20 × sin(30°) = 10 m/s. Using g = 10 m/s² for simplicity, the time to
      reach the top of its arc is 10 ÷ 10 = 1 s, so the total flight time is 2 s.

      The horizontal component is 20 × cos(30°) ≈ 17.3 m/s, unchanged for the whole flight.

      Range = horizontal velocity × flight time = 17.3 × 2 = ______ m (round to the nearest whole number).
    inputConfig:
      placeholder: "Range in metres"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["35", "35m", "35 m", "34.6", "34.6m", "34.6 m", "34.64"]
      rejectedFeedback: "Multiply the constant horizontal velocity (17.3 m/s) by the total time in the air (2 s): 17.3 × 2 ≈ 35 m. The vertical motion sets the clock; the horizontal motion covers the ground."
    hint: "The vertical motion decides how long the ball is airborne. The horizontal velocity, which never changes, decides how far it travels in that time."
    reflectionPrompt: "Why does the vertical calculation come first in almost every projectile problem?"
  - id: phy-sen-m1-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A barge glides downstream at 4 m/s relative to the riverbank. A deckhand walks toward
      the stern (the back) at 1 m/s relative to the barge.

      What is the deckhand's velocity relative to the riverbank, in m/s? Give the number and
      state the direction (downstream or upstream).
    inputConfig:
      placeholder: "e.g. 7 m/s upstream"
    markingRule:
      matchMode: CONTAINS
      accepted: ["3"]
      rejectedFeedback: "Walking toward the stern opposes the barge's motion, so subtract: 4 − 1 = 3 m/s downstream relative to the bank. Velocities in the same frame add as vectors — opposite directions subtract."
    hint: "The deckhand's bank-frame velocity is the barge's velocity plus the deckhand's velocity relative to the barge. Walking sternward means the second vector points upstream."
    reflectionPrompt: "If the deckhand walked at exactly 4 m/s toward the stern, what would an observer on the bank see?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "At the very top of a projectile's arc (no air resistance), which statement is true?"
    options:
      - "Its velocity is zero"
      - "Its acceleration is zero"
      - "Its vertical velocity is zero, but its horizontal velocity and its downward acceleration are unchanged"
      - "Both its velocity and acceleration point horizontally"
    correctIndex: 2
    feedback: "Only the vertical velocity passes through zero at the peak. The horizontal velocity persists untouched, and gravity never stops pulling — acceleration stays 9.8 m/s² downward throughout."
  - type: MULTIPLE_CHOICE
    question: "Two identical pendulums are connected by a weak spring. You set only the first one swinging. What happens next?"
    options:
      - "The first pendulum swings forever; the spring is too weak to matter"
      - "Energy gradually transfers to the second pendulum, which grows as the first dies down — then flows back"
      - "Both pendulums immediately stop because they interfere destructively"
      - "The second pendulum swings at double the frequency of the first"
    correctIndex: 1
    feedback: "The weak coupling lets energy leak across, beat by beat. The swing migrates entirely to the second pendulum and then returns — the signature behaviour of coupled oscillators, built from a mix of the system's two normal modes."
---

# Hook

Fire a cannonball horizontally off a cliff and, at the same instant, drop a second ball from your hand at the same height. Which one hits the ground first?

Nearly everyone says the dropped ball. The fired one is moving so fast, surely it stays up longer. But they land at exactly the same moment — and the reason is one of the most quietly powerful ideas in mechanics. The cannonball's sideways flight and its downward fall are two separate stories that never read each other's pages. Master that separation, and the most tangled motion in the world unravels into pieces you already know how to solve.

# Lore Introduction

Magus Selka has cleared the largest bench in the Deep Laboratories and bolted a brass launching ramp to one end. Beside it sits a release gate rigged so that pulling one lever fires a ball horizontally off the bench *and* drops a twin ball from the same height at the same instant.

"You have spent three lessons on motions that stay put," she says. "Spinning wheels. Swinging bobs. Standing waves humming between fixed walls. Today, motion that *goes somewhere* — and motion seen from somewhere else." She rests her hand on the lever. "Apprentices learn forces. Juniors learn momentum. Seniors learn that complicated motion is almost never new physics. It is old physics, running in layers, at the same time. Your task is to learn to see the layers."

She pulls the lever. Two clicks — and one single, simultaneous *tak* as both balls strike the floor together.

# Core Learning

## Concept Introduction

**Projectile motion: two problems wearing one trajectory.** When a ball flies through the air (ignoring air resistance), only one force acts on it: gravity, straight down. That has a remarkable consequence — the horizontal and vertical motions are completely independent:

- **Horizontally:** no force, so no acceleration. The horizontal velocity component stays constant: x = vₓt.
- **Vertically:** constant downward acceleration g = 9.8 m/s², exactly as if the object were simply dropped or thrown straight up. The vertical motion obeys the same equations you mastered in Apprentice motion lessons.

The curved parabola you see is just these two straight-line stories overlaid. To solve any projectile problem: split the launch velocity into components (vₓ = v·cos θ, v_y = v·sin θ), use the **vertical** motion to find the time of flight, then feed that time into the **horizontal** motion to find the range. The vertical motion sets the clock; the horizontal motion covers the ground.

**Relative motion: velocity needs an owner.** Ask "how fast is the deckhand moving?" and the only honest answer is *relative to what?* A walker on a moving barge has one velocity relative to the deck and a different one relative to the riverbank — both perfectly real. To transform between **reference frames**, add velocities as vectors:

> velocity of A relative to ground = velocity of A relative to barge + velocity of barge relative to ground

A frame moving at constant velocity is called an **inertial frame**, and Newton's laws hold identically in every one of them. There is no experiment you can do inside a smoothly moving ship that reveals whether it is moving — a principle Galileo articulated four centuries ago, and the seed from which (as you'll see in Module 3) Einstein grew something extraordinary.

**Coupled systems: oscillators that talk.** Connect two pendulums with a weak spring and set one swinging. Slowly, its swing fades — while the second pendulum, untouched, grows. Then the energy flows back. The coupling lets the oscillators trade energy.

Hidden inside this exchange are the system's **normal modes**: special patterns of motion in which *every* part oscillates at one single frequency. For two coupled pendulums there are exactly two — swinging together in phase (the spring never stretches) and swinging in exact opposition (the spring works hardest, giving a slightly higher frequency). Any motion the system can perform, however complicated it looks, is a recipe mixing these modes. Start one pendulum alone and you've mixed both modes equally; their slightly different frequencies drift in and out of step, and that drifting *is* the energy exchange — the same beat phenomenon you met in the sound lessons, now happening in hardware.

## Why It Matters

Component independence is how artillery tables, basketball shots, and spacecraft trajectories are computed: every "where will it land?" question is solved by splitting, solving the pieces, and recombining. Relative motion is daily bread for navigation — pilots fly through moving air, ferries cross flowing rivers, and orbital rendezvous is performed almost entirely in the frame of the target spacecraft, where a closing speed of centimetres per second matters more than the 7.7 km/s both vehicles share. And normal modes run far deeper than pendulums: engineers compute the normal modes of bridges, aircraft wings, and skyscrapers because resonance with wind or footfall finds the modes first. The vibrations of molecules, the oscillations of the power grid, even the quantised fields of particle physics are analysed as coupled oscillators in normal modes. This lesson is the door between simple mechanics and the physics of systems.

## Worked Examples

**Example 1 — The cliff shot.** A ball is fired horizontally at 40 m/s from a 45 m cliff. (Use g = 10 m/s².)

*Vertical (sets the clock):* starts at 0 m/s vertically, falls 45 m: 45 = ½ × 10 × t², so t² = 9, **t = 3 s**.
*Horizontal (covers the ground):* range = 40 × 3 = **120 m**.
Note the dropped-ball twin would also land in 3 s — the 40 m/s never enters the vertical calculation.

**Example 2 — Launch at an angle.** A ball leaves the ground at 20 m/s at 30° above horizontal (g = 10 m/s²).

*Components:* v_y = 20 sin 30° = 10 m/s; vₓ = 20 cos 30° ≈ 17.3 m/s.
*Time of flight:* rises until v_y = 0, taking 10/10 = 1 s; symmetric descent gives total **t = 2 s**.
*Maximum height:* average vertical speed 5 m/s × 1 s = **5 m**.
*Range:* 17.3 × 2 ≈ **35 m**.

**Example 3 — Crossing the river.** A ferry aims straight across a river at 4 m/s (relative to the water); the current flows at 3 m/s. Relative to the bank the ferry moves with both velocities at once — a vector at right angles: speed = √(4² + 3²) = **5 m/s**, angled downstream. If the river is 80 m wide, the crossing still takes 80/4 = 20 s — the current, like gravity in Example 1, does nothing to the crossing component. Independence of components again, in a new costume.

## Common Mistakes

- Believing horizontal speed delays the fall — fired and dropped balls land together; gravity ignores sideways motion entirely
- Setting velocity to zero at the top of the arc — only the vertical component is zero there; the horizontal component and the downward acceleration are untouched
- Using the full launch speed in vertical equations instead of the vertical component v·sin θ
- Treating velocity as absolute — every velocity is measured relative to a frame, and "relative to the ground" is a choice, not a default truth
- Adding relative velocities as plain numbers when the directions differ — they are vectors, and perpendicular ones combine by Pythagoras
- Expecting coupled oscillators to share energy evenly and settle — the energy flows back and forth periodically; it does not split and stop

## Mental Model

Picture a chess game played on a moving train. The bishop's diagonal is perfectly lawful on the board, and the board itself glides across the countryside — two descriptions, two frames, both true at once, never interfering. Projectiles play exactly this game: the vertical "board" runs its falling rules while the whole board slides sideways at constant speed. For coupled pendulums, picture two children on adjacent swings holding a rope between them: each push one child gives the rope steals a little of her swing and feeds the other, until the whole swing has migrated — and then the theft reverses.

## Mini Summary

- Projectile motion = constant-velocity horizontal motion + constant-acceleration vertical motion, running independently; solve the vertical for time, the horizontal for distance
- At the top of the arc, vertical velocity is zero but horizontal velocity and downward acceleration are unchanged
- Velocity is frame-dependent: transform between frames by vector-adding the frame's velocity; Newton's laws hold in every inertial frame
- Coupled oscillators exchange energy periodically; their motion decomposes into normal modes, patterns where the whole system oscillates at a single frequency

# Guided Practice Quest

Selka rolls the launching ramp to the bench edge and hands you the release lever. "Three trials," she says. "First, settle the cliff question your instincts keep getting wrong. Second, put numbers on a flight — clock from the vertical, distance from the horizontal. Third, step onto the barge and learn that 'how fast' is a question with a hidden second half." She taps the twin-ball gate. "Layers, Senior. Find the layers and the tangle disappears."

# Solo Practice Quest

Write a short investigation log (350–500 words) covering three threads. First, explain why projectile motion splits into two independent component motions, and demonstrate the method on a concrete example — launch speed and angle of your choosing — finding time of flight and range, showing which component answers which question. Second, explain relative velocity using your own example of a passenger moving within a moving vehicle, giving the velocity in both frames and stating why both answers are correct. Third, describe what happens when two identical pendulums are connected by a weak spring and one is set swinging, and explain what a normal mode is and how the energy-exchange behaviour arises from mixing two of them.

# Integration

**Mathematics:** Component decomposition is vector resolution — sine and cosine projecting one arrow onto two axes — and the projectile's path is a parabola because x is linear in t while y is quadratic: substitute one into the other and y becomes a quadratic function of x. Normal modes preview a profound mathematical idea (eigenvectors) you will meet head-on in computational physics: special directions in which a complicated system acts simply.

**Engineering:** Flight simulators, ballistics software, and game physics engines all integrate component motion exactly as you did by hand. Vibration engineers compute the normal modes of every aircraft wing and bridge deck before construction, because wind and resonance will find those modes whether or not anyone calculated them — a lesson written into the Tacoma Narrows wreckage.

# Lore Conclusion

The trials done, Selka racks the twin balls and wipes the bench slate clean — all but one corner, where the term's map is chalked. She strikes through *Module 1: Advanced Dynamics* with a single line.

"You can now take motion apart," she says. "Spin, swing, wave, flight — layers upon layers, each one simple. But everything you have touched this module, you could *see*." She opens a drawer and sets a small amber rod and a scrap of silk on the bench. She strokes the rod once, holds it over a heap of paper shavings — and they leap upward to cling to it, hauled by nothing visible at all.

"Next module, the Storm Tower's old lessons come down into my laboratories, and we treat them properly. Forces that reach across empty space. Fields, Senior." She chalks the new heading: *Module 2 — Electromagnetic Theory. First lesson: Electric Fields and Potential.* "Magus Hale showed you the spark. I will show you the architecture behind it."
