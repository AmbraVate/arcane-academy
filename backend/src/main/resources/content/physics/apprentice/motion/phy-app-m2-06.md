---
id: phy-app-m2-06
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: motion
topicTitle: "Motion"
topicSortOrder: 2
title: "Motion Graphs"
sortOrder: 6
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Interpret distance–time graphs (gradient = speed)
  - Interpret velocity–time graphs (gradient = acceleration, area = distance)
  - Match graph shapes to journey stories and vice versa
integrationDomains: [mathematics, data_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Reads speed from a distance–time graph's gradient
    - Reads acceleration from a velocity–time graph's gradient
    - Computes distance from the area under a velocity–time graph
    - Translates between a written journey and its two graph forms
  keywords: [gradient, slope, area, distance-time, velocity-time, flat, steeper, journey]
  modelAnswer: |
    On a distance–time graph the gradient is the speed: flat means stopped, straight and
    sloped means steady speed, curving upward means speeding up. On a velocity–time graph the
    gradient is the acceleration and the area under the line is the distance travelled: flat
    means constant velocity, a rising line means steady acceleration, and a horizontal line at
    zero means at rest. The same journey tells one story in each language — a bus accelerating,
    cruising, then braking draws a rising-flat-falling velocity graph, and its distance graph
    curves up, runs straight, then flattens off.
guidedSteps:
  - id: phy-app-m2-06-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      On a **distance–time** graph, a horizontal (flat) section means the object is:
    inputConfig:
      options:
        - "Moving at constant speed"
        - "Stationary"
        - "Accelerating"
        - "Moving backwards"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Stationary"]
      rejectedFeedback: "Flat means distance isn't changing as time passes — the object is parked. Gradient = speed, and a flat line's gradient is zero."
    hint: "If the line is flat, is distance increasing at all?"
    reflectionPrompt: "What would 'moving backwards toward the start' look like on this graph?"
  - id: phy-app-m2-06-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A velocity–time graph shows a constant 8 m/s for 30 s — a flat line. The area under it, and hence the distance travelled, is ________ m.
    inputConfig:
      placeholder: "240"
    markingRule:
      matchMode: CONTAINS
      accepted: ["240"]
      rejectedFeedback: "Area = velocity × time = 8 m/s × 30 s = 240 m. The rectangle under a velocity–time line IS the distance — its unit check: (m/s)·s = m."
    hint: "The area of a rectangle: height (velocity) × width (time)."
    reflectionPrompt: "Why does this area trick still work when the line slopes (hint: triangles)?"
  - id: phy-app-m2-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A lift's velocity–time graph: rises steadily from 0 to 3 m/s in 2 s, stays flat at 3 m/s for 6 s, falls steadily to 0 in 2 s. In 2–3 sentences, narrate the lift's journey in words, including its acceleration in each phase.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [accelerat, constant, "1.5", brake, slow, decelerat, steady]
      rejectedFeedback: "Phase 1: accelerates at 3/2 = 1.5 m/s² from rest. Phase 2: cruises at constant 3 m/s (zero acceleration). Phase 3: decelerates at −1.5 m/s² to rest. Three slopes, three accelerations."
    hint: "Gradient in each phase = acceleration in that phase."
    reflectionPrompt: "Bonus: what distance did the lift cover in total? (Two triangles and a rectangle.)"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "On a velocity–time graph, the gradient represents:"
    options: ["Distance", "Speed", "Acceleration", "Time"]
    correctIndex: 2
    feedback: "Gradient = change in velocity ÷ change in time = acceleration. (On a distance–time graph the gradient is speed — keep the two graphs' rules separate.)"
  - type: MULTIPLE_CHOICE
    question: "On a velocity–time graph, the area between the line and the time axis gives:"
    options: ["Acceleration", "Average speed", "Distance travelled", "Nothing physical"]
    correctIndex: 2
    feedback: "Velocity × time = distance, and the area under the curve sums exactly that, slice by slice."
---

# Hook

Air-crash investigators, sports scientists, and traffic-camera prosecutors all read the same kind of evidence: motion graphs. From a single curve, a trained eye can reconstruct an entire journey — when the brakes were applied, how hard, whether the driver hesitated, the exact distance covered while reacting.

Two graphs do all this work. The **distance–time** graph, where steepness is speed and flatness is a parked car. And the **velocity–time** graph — the professional's favourite — where steepness is acceleration and, magically, the *area trapped under the line* is the distance travelled.

Same journey, two pictures, every secret of the motion on display. Today you learn to read them like an investigator — and to spot the classic trap that catches almost everyone the first time.

# Lore Introduction

In the Observatory's chart room, Thorne unrolls two strips of vellum side by side — outputs of the Academy's proudest gadget, a clockwork scribe-cart that rolls along corridors drawing its own motion. "Same journey down the Long Gallery," he says. "Two records." The first strip shows a line climbing steadily, flattening to a plateau, climbing again. The second shows a sequence of flat steps, like battlements. "One of these is distance against time; the other, velocity against time. They look nothing alike — yet they describe the *identical* trundle." He taps each in turn. "An apprentice who can say which is which, and *why*, can read any motion ever recorded. Take your time. The plateau is the clue."

# Core Learning

## Concept Introduction

**Distance–time graphs.** Time runs along x, distance along y. The gradient is the **speed** (m ÷ s):

| Shape | Story |
|-------|-------|
| Flat | Stationary |
| Straight, sloped | Steady speed (steeper = faster) |
| Curving upward (steepening) | Speeding up |
| Curving toward flat | Slowing down |

The line never goes back in time, and for "distance travelled" it never slopes downward (for *displacement*–time it can — returning home).

**Velocity–time graphs.** Time on x, velocity on y. Two readings:

1. **Gradient = acceleration** (m/s ÷ s = m/s²). Flat = constant velocity; rising = accelerating; falling = decelerating.
2. **Area under the line = distance travelled.** A rectangle of 8 m/s × 30 s is 240 m; a triangle from rest to 12 m/s over 6 s holds ½ × 6 × 12 = 36 m. Complicated journeys = rectangles + triangles, summed.

**The classic trap.** A *flat* line means: on distance–time, *parked*; on velocity–time, *cruising at constant speed*. Identical shapes, opposite stories. Always read the y-axis label before reading anything else — this single habit prevents the most common graph error in physics.

**Matched stories.** One bus journey, two tellings: accelerate from the stop (d–t curves upward; v–t rises), cruise (d–t straight; v–t flat), brake to the next stop (d–t flattens; v–t falls to zero).

## Why It Matters

- Velocity–time graphs are the standard working language of vehicle testing, athletics analysis, robotics, and accident reconstruction — the area-under-the-curve trick computes stopping distances used in court.
- The two gradient rules unify the last two lessons: speed and acceleration become *visible* as steepness, making problems solvable by eye before any algebra.
- Reading the axis label first is a transferable data-literacy reflex — dashboards and scientific papers punish those who skip it.

## Worked Examples

**Example 1: Which strip is which?**
Thorne's strips: the climbing-plateau-climbing line must be distance–time (the plateau = parked; distance never decreases). The battlement steps are velocity–time: each flat step a steady cruising speed, each (near-vertical) riser a quick change of speed. The plateau and the lowest step tell the same moment two ways: stopped.

**Example 2: Stopping distance from a graph**
A driver at 20 m/s sees a hazard. Reaction time 0.8 s (velocity flat at 20): area = 20 × 0.8 = 16 m of *thinking distance*. Then braking from 20 to 0 over 4 s (line falls): area = ½ × 4 × 20 = 40 m of *braking distance*. Total 56 m — read entirely from one graph's areas. This is precisely how road-safety stopping distances are derived.

**Example 3: The round trip on displacement–time**
Walk 100 m away in 100 s (line climbs), pause 50 s (flat), walk back in 80 s (line *descends* to zero). The descending section has negative gradient: velocity ≈ −1.25 m/s — the sign carries the direction, exactly as vectors demanded. Distance–time would instead keep climbing to 200 m; displacement–time comes home.

## Common Mistakes

- **Reading a flat velocity–time line as "stopped"** — flat means *constant velocity*; only a line *at zero* means rest. Axis label first, always.
- **Confusing the two gradient rules** — d–t gradient is speed; v–t gradient is acceleration. Mixing them is the classic exam disaster.
- **Forgetting the area rule** — students recompute distance with formulas when the graph is offering it as geometry.
- **Treating curved d–t lines as "moving in curves"** — the graph's bend is in *time*, not space; a curving d–t line is straight-line motion that's speeding up or slowing.
- **Ignoring negative regions** — on velocity–time, line below zero means moving backwards; its area counts as distance travelled but negative displacement.

## Mental Model

Treat the two graphs as **two dialects reporting on the same traveller**. The distance–time dialect is a *milestone diary*: "by 10 o'clock I'd come this far." Its slope betrays your pace. The velocity–time dialect is a *speedometer diary*: "at 10 o'clock the needle read this." Its slope betrays your acceleration — and summing up its entries (the area) reconstructs the milestone diary. Fluent physicists translate between dialects without thinking; the translation dictionary is just two rules: *gradient*, and *area*.

## Mini Summary

- ✔ Distance–time: gradient = speed; flat = parked; curve = changing speed
- ✔ Velocity–time: gradient = acceleration; area under line = distance
- ✔ Flat lines mean opposite things on the two graphs — read the y-axis label first
- ✔ Decompose v–t areas into rectangles and triangles to compute journeys
- ✔ Below-zero velocity = moving backwards; signs carry direction on graphs too

# Guided Practice Quest

Work through the guided steps to park a car with a flat line, extract 240 m from a rectangle, and narrate a lift's journey from its three slopes.

# Solo Practice Quest

Invent a five-phase journey (e.g. walk to a bus stop, wait, ride, brake at lights, walk again) with realistic speeds and times. Draw BOTH graphs for it: distance–time and velocity–time, axes labelled with units. Then verify your own consistency three ways: (1) each v–t area should match the distance gained on the d–t graph in that phase; (2) each v–t flat level should match the d–t gradient there; (3) total distance both ways should agree. Finish by writing the journey as a one-paragraph "investigator's report" reconstructed only from your velocity–time graph.

# Integration

**Mathematics**: You've just used calculus without the notation — gradients of curves are derivatives, areas under curves are integrals, and the fact that the v–t area rebuilds the d–t graph is the Fundamental Theorem of Calculus, wearing overalls.

**Data Engineering**: Time-series data — server load, sales per hour, sensor streams — obeys the same grammar: values, rates of change, and cumulative areas. A burn-down chart is a distance–time graph; a throughput dashboard is velocity–time. The translation skills transfer intact.

# Lore Conclusion

You hand Thorne your verdict on the two vellum strips — which is which, and the plateau decoded — and, showing off slightly, the cart's total journey computed from the battlement areas. He checks it against the corridor's flagstone count and nods slowly. "The scribe-cart has no mind, no memory, no purpose — yet its whole history is recoverable from one curve. Remember that feeling." He rolls the strips and ties them. "Description is now complete: you can chart any motion in this Academy. What you cannot yet do is *explain* one. Why this acceleration and not another? Why does the heavy cart need a stronger shove?" He smiles properly this time. "Module's heart, next. The three laws."
