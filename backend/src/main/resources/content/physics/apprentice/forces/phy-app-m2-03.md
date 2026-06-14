---
id: phy-app-m2-03
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: forces
topicTitle: "Forces"
topicSortOrder: 1
title: "Balanced and Unbalanced Forces"
sortOrder: 3
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Compute the resultant of forces acting along a line
  - Predict motion from balanced versus unbalanced forces
  - Draw and read a simple free-body diagram
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Calculates resultant force using signed addition
    - States correctly that balanced forces mean no CHANGE in motion (not necessarily no motion)
    - Draws a free-body diagram with labelled, sensibly-sized arrows
    - Predicts the direction of acceleration from the resultant
  keywords: [resultant, balanced, unbalanced, net force, free-body, equilibrium, accelerate]
  modelAnswer: |
    The resultant (net) force is the vector sum of all forces on an object — along a line,
    signed addition. If the resultant is zero the forces are balanced and the object's motion
    does not change: at rest it stays at rest, and if moving it continues at the same speed in
    the same line. An unbalanced resultant changes motion in the resultant's direction. A
    free-body diagram shows one object as a dot or box with one labelled arrow per force,
    lengths roughly proportional to sizes — it is the standard tool for finding the resultant.
guidedSteps:
  - id: phy-app-m2-03-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A rowing boat: crew drives it forward with 900 N while water drag pushes back with 700 N. The resultant force is ________ N forward.
    inputConfig:
      placeholder: "200"
    markingRule:
      matchMode: CONTAINS
      accepted: ["200"]
      rejectedFeedback: "Resultant = 900 N − 700 N = 200 N in the forward direction. Opposing forces subtract; the leftover is what changes the motion."
    hint: "Take forward as positive: +900 + (−700) = ?"
    reflectionPrompt: "What happens to the boat's speed while this 200 N resultant persists?"
  - id: phy-app-m2-03-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A cyclist rides along a flat road at a constant 25 km/h. What do you know about the forces on her?
    inputConfig:
      options:
        - "The forward force is larger than the backward forces — that's why she moves"
        - "All forces balance: the resultant is zero, so her motion isn't changing"
        - "There are no forces acting, since her speed is constant"
        - "Only gravity acts on her"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["All forces balance: the resultant is zero, so her motion isn't changing"]
      rejectedFeedback: "Constant velocity = no CHANGE in motion = zero resultant. Her pedalling force exactly matches drag and friction. Balanced forces permit steady motion — they only forbid CHANGES to it."
    hint: "Is her motion changing? What does that say about the resultant?"
    reflectionPrompt: "Why do most people instinctively think constant motion needs a winning forward force?"
  - id: phy-app-m2-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A skydiver falls at terminal (constant) velocity. Describe her free-body diagram — what forces, what directions, what relative sizes — and explain what 'terminal' means in force terms. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [weight, drag, air resistance, equal, balanced, zero, constant]
      rejectedFeedback: "Two arrows: weight downward and air resistance upward, drawn EQUAL in length. At terminal velocity drag has grown to match weight, the resultant is zero, and the speed stops changing."
    hint: "Two forces only. At terminal velocity, how must their sizes compare?"
    reflectionPrompt: "Just after she jumps, before drag builds up, what does her diagram look like?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Balanced forces act on a moving hockey puck. The puck will:"
    options:
      - "Slow down and stop"
      - "Continue at the same speed in the same direction"
      - "Speed up"
      - "Immediately stop"
    correctIndex: 1
    feedback: "Zero resultant means zero change in motion. Moving objects under balanced forces keep their velocity — rest is just the special case where that velocity is zero."
  - type: MULTIPLE_CHOICE
    question: "Forces of 40 N east and 65 N west act on a crate. The resultant is:"
    options: ["105 N east", "25 N west", "25 N east", "105 N west"]
    correctIndex: 1
    feedback: "65 − 40 = 25 N in the direction of the larger force: west. Signed addition, then state the direction."
---

# Hook

Question: a jet airliner cruises at 900 km/h, engines roaring at full thrust. How big is the total force pushing it forward, compared with the drag pushing it back?

Most people guess the thrust must be winning — surely *something* has to win, to keep all that metal moving? The answer: they are **exactly equal**. Dead even. The resultant force on a plane at steady cruise is zero — the same as on a parked car. That feels wrong, and the feeling has a two-thousand-year pedigree: Aristotle himself taught that motion requires a winning force.

He was wrong, and this lesson is where you find out what forces *actually* decide. Spoiler: they don't decide whether things move. They decide whether motion *changes*.

# Lore Introduction

Thorne has rigged a curious contest in the Hall of Mechanisms: a heavy chest with rope handles on opposite sides, two apprentices hauling against each other like a tug-of-war. Both are straining, faces red; the chest sits serenely still. "Behold," says Thorne, "violence achieving nothing. Two large forces, one stationary chest." He signals; one apprentice eases off just slightly. The chest begins to creep, then slide, toward the stronger side. "Now — the interesting question. The chest moved when the forces *disagreed*. But notice what kind of thing changed: not its position merely. Its *motion*. Forces are not the cause of movement, apprentice. They are the cause of *changes* in movement. Write that down twice; you will spend weeks believing otherwise."

# Core Learning

## Concept Introduction

The **resultant force** (or net force) on an object is the vector sum of every force acting on it. Along a single line: pick a positive direction, sign the forces, add.

```
Forward 900 N, backward 700 N  →  resultant = +200 N (forward)
Up 500 N, down 500 N           →  resultant = 0
```

**The central rule of mechanics:**

| Resultant | Forces are... | The object... |
|-----------|---------------|----------------|
| Zero | Balanced | Keeps doing exactly what it was doing: stays at rest, or keeps moving at constant velocity |
| Non-zero | Unbalanced | Changes its motion — speeds up, slows down, or changes direction, in the resultant's direction |

Read the middle row again: **balanced does not mean stationary**. It means *no change*. A parked van and a van cruising at a steady 60 km/h have identical resultants: zero.

**Free-body diagrams** are how physicists organise this. Draw the object as a box or dot, alone — no scenery. Add one arrow per force acting *on it*, pointing the right way, labelled, with lengths roughly proportional to size. Then the resultant can be read off by combining arrows. The diagram forces three honest questions: Which forces? Which directions? Which is bigger?

## Why It Matters

- "Is the resultant zero or not?" is the first question of every mechanics problem from here to orbital dynamics.
- Free-body diagrams are the most-used diagnostic tool in physics and engineering — bridge design, vehicle crash analysis, and robotics all begin with them.
- The balanced-motion insight (zero resultant permits steady motion) is Newton's first law in embryo, arriving next module — meeting it here through forces makes the law feel inevitable rather than strange.

## Worked Examples

**Example 1: The takeoff roll**
A jet at the start of its runway: thrust 400 kN forward, drag and rolling resistance 150 kN backward. Resultant: 250 kN forward — unbalanced, so the plane gains speed. As speed builds, drag grows; if thrust stayed fixed and the plane stayed on the ground, drag would eventually match thrust — balanced forces, top speed reached. (It lifts off first.)

**Example 2: Reading a lift's motion from its cable**
A 5000 N lift car hangs from a cable. Cable tension 5000 N: balanced — the lift is stationary *or* gliding at constant speed (the forces can't tell you which; both are "no change"). Tension 5500 N: resultant 500 N up — the lift is gaining upward speed (or losing downward speed; both are upward *changes*). Tension 4600 N: resultant 400 N down — speeding up downward or braking while rising.

**Example 3: Free-body diagram of a sliding box**
A box slides rightward across a floor, no one pushing. Forces: weight down (say 50 N), normal force up (50 N), friction leftward (8 N). Vertical: balanced. Horizontal: resultant 8 N left — opposite the motion, so the box slows and eventually stops. Note what's *absent* from the diagram: no rightward arrow. Its motion needs no force; only its slowing does.

## Common Mistakes

- **"It's moving, so a forward force must be winning"** — constant velocity means *balanced* forces; only changing velocity needs a winner.
- **Drawing a "motion arrow" on free-body diagrams** — velocity is not a force; diagrams show forces only (sketch velocity beside the diagram if helpful, never on the object).
- **Forgetting a force or inventing one** — every arrow must answer "what object exerts this?"; arrows without agents are fiction.
- **Equal-length arrows drawn carelessly unequal (or vice versa)** — the diagram's proportions *are* its physics; sloppy lengths mislead your own reasoning.
- **Treating "balanced" as "stationary"** — rest is one special case of unchanging motion, not the definition.

## Mental Model

Think of the resultant force as a **committee verdict**. Every force on the object casts a vote with a strength and a direction. Votes in opposite directions cancel head-to-head. If the committee deadlocks — zero resultant — the standing policy continues unchanged, whatever it was: resting stays resting, cruising stays cruising. Only a *majority* (non-zero resultant) can change policy, and the change always goes the majority's way. Free-body diagrams are simply the committee's voting record, drawn honestly.

## Mini Summary

- ✔ Resultant = signed vector sum of all forces on one object
- ✔ Zero resultant (balanced): motion continues unchanged — including steady motion
- ✔ Non-zero resultant (unbalanced): motion changes, in the resultant's direction
- ✔ Free-body diagrams: one object, one labelled arrow per force, honest lengths
- ✔ Never draw velocity as a force; never let an arrow lack an agent

# Guided Practice Quest

Work through the guided steps to compute resultants, re-examine a cruising cyclist, and read a skydiver's terminal velocity straight from her force diagram.

# Solo Practice Quest

Draw free-body diagrams for four situations: (1) a mug resting on a desk; (2) the same mug while you push it across the desk at constant speed; (3) a ball the instant after leaving a thrower's hand, rising; (4) a parachutist the moment the canopy opens while falling fast. For each: label every force with its agent, state whether the forces balance, give the resultant's direction if not, and predict what happens next. Diagram (4) deserves a sentence of special care: the resultant points *opposite* to the motion — explain what that does and does not mean.

# Integration

**Mathematics**: Resultants are vector addition doing real work — and equilibrium (zero resultant) is your first taste of solving equations of the form "sum of contributions = 0", a pattern that recurs from circuit analysis to economics.

**Engineering**: Structural engineering is the discipline of arranging permanent deadlock — every beam, cable, and column of a stationary bridge has a zero resultant, by design, under every load the codes can imagine. A structural calculation is a free-body diagram with consequences.

# Lore Conclusion

Thorne chalks the day's verdict beneath the tug-of-war chest: *Forces do not cause motion. They cause changes in motion.* The strained apprentices, still catching their breath, copy it down with visible scepticism. "You don't believe it yet," Thorne says cheerfully. "Your muscles don't believe it either — they've spent your whole life pushing things through a world thick with friction, and they've drawn the wrong lesson from winning." He shoulders his satchel. "So next we strip the world back. Smooth ice. Empty space. Places where motion shows its true nature — and we finally give that nature its proper names: *velocity*, and *acceleration*."
