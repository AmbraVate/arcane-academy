---
id: phy-app-m2-08
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: newtonian_mechanics
topicTitle: "Newtonian Mechanics"
topicSortOrder: 3
title: "Newton's Second Law: F = ma"
sortOrder: 8
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Apply F = ma to find force, mass, or acceleration
  - Predict how acceleration changes when force or mass changes
  - Connect weight to the second law via W = mg
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Uses F = ma with the RESULTANT force, with correct units
    - Solves for each of the three variables given the other two
    - States the proportionalities — a doubles with F, halves with double m
  keywords: [resultant, F = ma, newton, mass, acceleration, proportional, double]
  modelAnswer: |
    Newton's second law: resultant force = mass × acceleration, F = ma. With F in newtons and
    m in kilograms, a comes out in m/s². The law works in all three directions: a 1200 kg car
    accelerating at 2.5 m/s² needs a 3000 N resultant; a 3000 N resultant on a 600 kg kart
    gives 5 m/s². Acceleration is proportional to force (double the push, double the response)
    and inversely proportional to mass (double the load, half the response). Weight is the
    second law applied to gravity: W = mg is F = ma with a = g.
guidedSteps:
  - id: phy-app-m2-08-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A 1000 kg car experiences a resultant forward force of 2500 N. Its acceleration is ________ m/s².
    inputConfig:
      placeholder: "2.5"
    markingRule:
      matchMode: CONTAINS
      accepted: ["2.5"]
      rejectedFeedback: "a = F/m = 2500 N ÷ 1000 kg = 2.5 m/s². Rearranging F = ma is the Module One balance rule at work."
    hint: "Rearrange F = ma to a = F/m."
    reflectionPrompt: "What happens to this acceleration when the same car tows a 1000 kg trailer?"
  - id: phy-app-m2-08-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      The same resultant force is applied to a tennis ball (0.06 kg) and a medicine ball (6 kg). Compared with the tennis ball, the medicine ball's acceleration is:
    inputConfig:
      options:
        - "100 times smaller"
        - "100 times larger"
        - "The same — equal forces give equal accelerations"
        - "10 times smaller"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["100 times smaller"]
      rejectedFeedback: "a = F/m: with F fixed, acceleration is inversely proportional to mass. The medicine ball has 100× the mass, so 1/100th the acceleration. Equal forces emphatically do NOT give equal accelerations."
    hint: "a = F/m. The mass went up by what factor?"
    reflectionPrompt: "Why then do all masses FALL with equal acceleration? (What else besides m changes in W = mg?)"
  - id: phy-app-m2-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A 70 kg sprinter accelerates at 4 m/s² off the blocks while friction and drag oppose her with 30 N. In 2–3 sentences, find the forward force her feet must obtain from the track. (Careful: F = ma uses the RESULTANT.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["310", resultant, "280", net]
      rejectedFeedback: "Resultant needed: ma = 70 × 4 = 280 N. The track must supply 280 N PLUS the 30 N being stolen by resistance: 310 N forward. F = ma talks only about the net force; real pushes must also pay the resistive tax."
    hint: "First find the resultant (ma). Then ask: forward force − 30 N = resultant."
    reflectionPrompt: "Why is 'F' in F = ma the most commonly misused letter in physics homework?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In F = ma, the F stands for:"
    options:
      - "Any one force acting on the object"
      - "The largest force acting"
      - "The resultant (net) of all forces acting on the object"
      - "The object's weight"
    correctIndex: 2
    feedback: "Always the resultant. A 500 N pull opposed by 200 N of friction accelerates the object with 300 N, not 500."
  - type: MULTIPLE_CHOICE
    question: "Doubling the resultant force on an object while halving its mass multiplies its acceleration by:"
    options: ["2", "4", "1 (no change)", "½"]
    correctIndex: 1
    feedback: "a = F/m: doubling F doubles a; halving m doubles it again. 2 × 2 = 4."
---

# Hook

Every acceleration you have ever felt — every takeoff, every braking train, every rollercoaster plunge — obeys one equation short enough to tattoo on a knuckle: **F = ma**.

It looks humble. It is arguably the most consequential line of mathematics ever written. It's how engineers size every engine and brake on Earth, how NASA computed the thrust to lift Saturn V's 2,900 tonnes, how crash investigators reconstruct collisions, and how this morning's weather forecast was calculated (the atmosphere is just a fluid obeying F = ma a trillion times over).

The second law answers the question the first law left open. First law: motion changes only when a resultant force acts. Second law: *and here is exactly how much*.

# Lore Introduction

The Hall of Mechanisms has a long brass track, and on it Thorne has staged a contest: identical spring-launchers, cocked to identical tension, behind two carts — one empty, one loaded with ingots. "Equal pushes," he announces to the gathered apprentices. "Place your wagers." The launchers fire as one: the empty cart leaps away; the laden one trundles with dignity. Thorne resets the experiment, this time doubling the springs on the laden cart. It now matches the empty cart's first run almost exactly. He chalks three columns on the slate — *push, burden, response* — and fills in the morning's numbers. "Nine hundred years of machines in this hall," he says, "and every one of them was designed by someone who could complete this table. The relationship has three letters. Find it before lunch."

# Core Learning

## Concept Introduction

**Newton's Second Law:**

```
F = m × a
resultant force (N) = mass (kg) × acceleration (m/s²)
```

This single line packs three statements:

1. **Acceleration is proportional to resultant force.** Double the net push → double the acceleration. (At fixed mass.)
2. **Acceleration is inversely proportional to mass.** Double the mass → half the acceleration. (At fixed push.) Mass here *is* the inertia of the first law, now quantified.
3. **The acceleration points along the resultant.** Vectors throughout.

And one definition for free: the **newton** is the force giving 1 kg an acceleration of 1 m/s² — which is why N unpacks to kg·m/s² (Module One promised this day would come).

**The critical fine print: F means the resultant.** Not the engine force, not the biggest force — the vector sum of *everything* acting. The workflow for every second-law problem:

1. Free-body diagram (one object, all forces).
2. Resultant (signed sum).
3. *Then* F = ma, solved for whichever letter is missing.

**Weight rejoins the story.** W = mg is just F = ma with gravity's acceleration plugged in. And it resolves yesterday's puzzle elegantly: a boulder has 100× the pebble's mass — but gravity pulls it with 100× the force. In a = F/m, the hundreds cancel. *That* is why everything falls together: gravity is the one force considerate enough to scale itself to each customer's mass.

## Why It Matters

- This is the working equation of mechanical engineering: engine sizing, brake design, elevator cables, crash testing, robotics — all are F = ma with professional paperwork.
- It converts the first law from philosophy into computation: not just "a force changes motion" but *this* force, *this* much change, *this* fast.
- The resultant-only rule is the difference between right and wrong answers from here to the end of physics; learning it now as reflex pays for years.

## Worked Examples

**Example 1: Sizing a rocket's nerve**
Saturn V at liftoff: mass ≈ 2.9 × 10⁶ kg, thrust ≈ 3.5 × 10⁷ N. Weight = mg ≈ 2.9×10⁶ × 9.8 ≈ 2.8 × 10⁷ N. Resultant = 3.5 − 2.8 = 0.7 × 10⁷ N upward. a = F/m ≈ 7×10⁶ / 2.9×10⁶ ≈ **2.4 m/s²** — a stately crawl off the pad, exactly as the films show. The thrust barely outvoted the weight; the second law explains the majesty.

**Example 2: The deceleration that saves you**
A 75 kg passenger in a crash goes from 20 m/s to rest. Without a seatbelt, stopping against the dashboard in ~0.02 s: a = 1000 m/s², F = ma = **75,000 N** — catastrophic. With belt and airbag stretching the stop to 0.2 s: a = 100 m/s², F = **7,500 N** — survivable. Same Δv; ten times the time, one-tenth the force. Safety engineering is the art of buying time.

**Example 3: Finding the hidden friction**
A 40 kg crate is pushed with 90 N and accelerates at 1.5 m/s². Resultant = ma = 60 N. But the push was 90 N — so friction must be eating 90 − 60 = **30 N**. The second law, run backwards, just *measured* an invisible force. This trick — inferring forces from accelerations — is how physicists weighed the Earth and found Neptune.

## Common Mistakes

- **Plugging a single applied force in as F** — F is the resultant; subtract the friction/drag first (the most common mechanics error, bar none).
- **Using weight where mass belongs** — m is in kilograms; if handed a weight in newtons, divide by g first.
- **Expecting equal forces to move all objects equally** — equal forces give equal *F*, not equal *a*; mass divides the result.
- **Forgetting direction** — acceleration points along the resultant; a backward resultant on a forward-moving object means braking, not reversing (yet).
- **Treating F = ma as needing motion** — a resultant on a stationary object accelerates it *from rest*; the law doesn't wait.

## Mental Model

Think of F = ma as an **exchange rate with a commission**. Force is the currency you pay; acceleration is what you receive; mass is the commission rate the object charges. A wheelbarrow charges little — your coins buy brisk acceleration. A loaded freight car charges brutally — the same coins barely move the needle. And the teller only accepts *net* payments: any friction in the room has its hand in the till before your force reaches the counter.

## Mini Summary

- ✔ F = ma: resultant force = mass × acceleration; 1 N = 1 kg·m/s²
- ✔ a ∝ F (double push, double response); a ∝ 1/m (double load, half response)
- ✔ F is always the *resultant* — free-body diagram first, law second
- ✔ W = mg is the second law's gravity special case, and explains universal free fall
- ✔ Run it backwards to measure hidden forces from observed accelerations

# Guided Practice Quest

Work through the guided steps to accelerate a car, humble a medicine ball, and pay a sprinter's friction tax — the resultant-only rule in action.

# Solo Practice Quest

Three second-law engagements: (1) A lift of mass 600 kg must accelerate upward at 1.2 m/s² — find the cable tension (free-body diagram first: tension up, weight down, resultant = ma). (2) Estimate the resultant force on YOU when a bus you ride brakes from 12 m/s to rest in 3 s — your mass, your numbers. (3) Design question: a delivery firm wants its 50 kg robot to reach 4 m/s within 2 m of corridor; using v² = 2as or stepwise reasoning, estimate the required acceleration and hence motor force, allowing 20 N for friction. Show free-body thinking in all three; mark each F you use as *resultant* or *applied*.

# Integration

**Mathematics**: F = ma is your first physical *equation of motion* — at Senior tier it becomes a differential equation (F = m·dv/dt) whose solutions are entire trajectories. The proportionality reasoning (double this, halve that) is the algebra of scaling, a tool that runs through all of science.

**Engineering**: Every engineering discipline keeps this law on its desk: civil engineers cap accelerations so lift passengers don't buckle; automotive engineers trade engine force against vehicle mass; aerospace lives and dies by thrust-minus-weight. Crumple zones are the second law negotiated with biology.

# Lore Conclusion

By lunch your slate carries the relationship — *response equals push shared out over burden* — and Thorne reads it without comment, which from him is applause. He pulls the morning's loaded cart to the track once more. "One law left," he says. "And it is the strangest. Tell me: when the spring pushed this cart —" he taps the launcher, "— what pushed *the spring*?" You open your mouth and find the question has no floor. The cart pushed back? Thorne watches the vertigo arrive. "Tomorrow," he says, with what is unmistakably relish, "we discover that the universe has never once delivered a push without taking delivery of one in return. Forces, apprentice, come only in pairs."
