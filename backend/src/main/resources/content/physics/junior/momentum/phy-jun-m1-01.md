---
id: phy-jun-m1-01
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: momentum
topicTitle: "Momentum"
topicSortOrder: 1
title: "Momentum and Impulse"
sortOrder: 1
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Calculate momentum as p = mv
  - Relate impulse (F × t) to change in momentum
  - Explain crumple zones and follow-through using impulse
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Computes p = mv with correct units (kg·m/s)
    - States impulse = FΔt = Δp and uses it both ways
    - Explains one safety device via extending collision time
  keywords: [momentum, impulse, kg m/s, force, time, change, crumple, FΔt]
  modelAnswer: |
    Momentum is mass in motion: p = mv, in kg·m/s, a vector pointing with the velocity. To
    change momentum you must apply a force for a time — the impulse FΔt equals the change Δp.
    The same momentum change can be bought with a huge force over a moment or a gentle force
    over longer: crumple zones, airbags, helmet padding, and bending your knees on landing all
    stretch Δt to shrink F. A cricketer's follow-through works the other way round, lengthening
    contact time to deliver more impulse and more outgoing momentum.
guidedSteps:
  - id: phy-jun-m1-01-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A 70 kg sprinter runs at 9 m/s. Her momentum is p = mv = ________ kg·m/s.
    inputConfig:
      placeholder: "630"
    markingRule:
      matchMode: CONTAINS
      accepted: ["630"]
      rejectedFeedback: "p = 70 × 9 = 630 kg·m/s, directed along her motion. Mass times velocity — the quantity of motion itself."
    hint: "Multiply mass by velocity."
    reflectionPrompt: "A 7000 kg lorry creeping at 0.09 m/s has the same momentum. What does that comparison teach?"
  - id: phy-jun-m1-01-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      An egg dropped onto concrete breaks; the same egg dropped from the same height onto a thick cushion survives. Both stops destroy the same momentum. The cushion saves the egg because it:
    inputConfig:
      options:
        - "Reduces the egg's momentum before impact"
        - "Stretches the stopping time, so the same Δp needs a much smaller force"
        - "Absorbs the egg's mass"
        - "Pushes the egg back upward harder"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Stretches the stopping time, so the same Δp needs a much smaller force"]
      rejectedFeedback: "Δp is fixed by the fall; FΔt = Δp means F = Δp/Δt. Concrete stops the egg in ~1 ms (huge F); the cushion takes ~100 ms (F a hundred times smaller). Time is the egg's only friend."
    hint: "Same momentum change. What does the cushion change in FΔt = Δp?"
    reflectionPrompt: "List three human inventions (or instincts) that exploit exactly this trick."
  - id: phy-jun-m1-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A 0.16 kg cricket ball arrives at 30 m/s and is struck back at 40 m/s. Find the ball's change in momentum (mind the directions!), and the average force if bat-ball contact lasts 0.001 s. (2–3 sentences of working.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["11.2", "11200", "11,200", direction, opposite]
      rejectedFeedback: "Take 'toward the bat' as negative: Δp = 0.16×40 − (0.16×(−30)) = 6.4 + 4.8 = 11.2 kg·m/s. F = Δp/Δt = 11.2/0.001 = 11,200 N — over a tonne-force, which is why bats crack and follow-through matters."
    hint: "The velocity REVERSES — the change is bigger than either momentum alone."
    reflectionPrompt: "Why does following through (longer contact) send the ball away faster for the same peak force?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Impulse is:"
    options:
      - "Force × time, equal to the change in momentum it produces"
      - "Mass × velocity"
      - "Force × distance"
      - "A sudden desire to do physics"
    correctIndex: 0
    feedback: "FΔt = Δp — the bridge between Newton's second law and momentum. (Force × distance is work; mass × velocity is momentum itself.)"
  - type: MULTIPLE_CHOICE
    question: "Airbags reduce injury primarily by:"
    options:
      - "Reducing the passenger's momentum change"
      - "Increasing the stopping time, lowering the peak force for the same Δp"
      - "Adding a forward force"
      - "Making the car lighter"
    correctIndex: 1
    feedback: "The Δv (and so Δp) is set by the crash. The bag's gift is milliseconds — and F = Δp/Δt falls in proportion."
---

# Hook

Why does a boxer *ride* a punch, pulling his head back as it lands? Why do you instinctively bend your knees jumping off a wall? Why do cars — engineered by people who love them — have fronts designed to *collapse*?

One quantity answers all three, and it's the quantity Newton himself thought most fundamental: **momentum**, mass in motion, p = mv. You cannot argue with momentum; whatever arrives must be paid off in full to stop. But physics offers one mercy in the payment terms: the same momentum change can be settled with a savage force over an instant *or* a gentle force over time. Every crumple zone, catching technique, and gymnast's roll is a negotiation over Δt — and this lesson teaches you the contract: **FΔt = Δp**.

# Lore Introduction

The Junior rotation begins in a wing of the Academy you've never entered: the Mechanica — a cavernous workshop of test-rigs, rolling tracks, and suspended weights, presided over by Magus Vex, a spare, quick man with a reputation for letting apparatus make his arguments. He greets you by handing over a leather catching-mitt and, without warning, lobbing a heavy sandbag from the gallery. You catch it — badly, arms rigid — and it nearly puts you on the floor. "Again," says Vex, retrieving it. "This time, give with it." You catch it the second time with arms folding back, and the bag settles into your chest like a sleeping cat. "Same bag," Vex says. "Same speed. Same *quantity of motion* delivered into the same junior. One catch hurt. Tell me — in numbers I can write down — what you changed."

# Core Learning

## Concept Introduction

**Momentum** is the quantity of motion an object carries:

```
p = m × v        (kg·m/s, a vector — direction included)
```

A sprinting 70 kg athlete: 630 kg·m/s. A 7,000 kg lorry at walking-creep 0.09 m/s: the same. A 0.04 kg bullet at 400 m/s: just 16 — momentum is not damage-rating, it is *motion-accounting* (energy tells the damage story; the two ride together but answer different questions).

**Impulse: how momentum changes.** Newton's second law, rearranged into its deepest form — F = ma = m(Δv/Δt) = Δp/Δt — says force is *the rate of momentum change*. Multiply up:

```
F × Δt = Δp        impulse = change in momentum
```

A fixed Δp can be paid as **big force × short time** or **small force × long time**. That trade is the working principle of:

- **Protection** (stretch Δt, shrink F): crumple zones, airbags, helmet liners, gym mats, bent knees, riding the punch, crash barriers that deform
- **Delivery** (stretch Δt at your maximum F, maximise Δp): the follow-through in cricket, golf, and tennis; a long cannon barrel; a rocket burn

**Directions matter.** Momentum is a vector: a ball reversing direction changes momentum by *more* than it carried (from +mv to −mv′ is a change of m(v+v′)). The biggest forces in sport and crashes live in reversals.

## Why It Matters

- Impulse thinking is the entire mathematics of crash safety — the difference between a 1 ms concrete stop and a 100 ms airbag stop is a factor of 100 in force on your body.
- Sports technique is largely impulse engineering: catching, landing, striking, and throwing all optimise the FΔt contract.
- Next lesson's conservation law — momentum's superpower — only makes sense once p and Δp are fluent.

## Worked Examples

**Example 1: The two catches, audited**
Vex's 10 kg sandbag arrives at 5 m/s: Δp = 50 kg·m/s to absorb. Rigid arms: stop in 0.05 s → F = 50/0.05 = 1,000 N (a heavyweight's punch). Folding arms: 0.5 s → 100 N (a firm handshake). The bag didn't change; the contract did.

**Example 2: Crumple zones, quantified**
A 1,200 kg car hits a wall at 15 m/s: Δp = 18,000 kg·m/s. Rigid 1950s chassis: stop in ~0.05 s → average 360 kN on the structure (and savage decelerations on occupants). Modern crumple front collapsing over ~0.15 s: 120 kN — and the cabin's airbag-and-belt system stretches the *occupant's* personal Δt further still. Cars are engineered to break so that people don't.

**Example 3: Why long jumps end in sandpits**
A long-jumper lands carrying ~600 kg·m/s of horizontal momentum. Sand extends the stop over ~0.3 s (and a sliding distance): ~2,000 N spread through legs built for it. The same landing on concrete: tens of milliseconds, tens of kilonewtons — career-ending. Sandpits, gym mats, and foam pits are all Δt, institutionalised.

## Common Mistakes

- **Confusing momentum with energy** — both grow with motion, but p = mv is direction-carrying motion-accounting; E = ½mv² is the damage budget. A reversal doubles |Δp| while energy may be similar.
- **Forgetting the sign in reversals** — from +4.8 to −6.4 kg·m/s is a change of 11.2, not 1.6; the bounce is the expensive part.
- **"Airbags reduce the momentum change"** — Δp is fixed by the crash; bags only buy time. (They cannot make the stop gentler than physics' FΔt allows.)
- **Treating impulse as exotic** — it's just Newton's second law multiplied by time; you've owned it since the Apprentice tier.
- **Quoting p without direction** — momentum problems live and die by chosen positive directions; pick one, declare it, stay loyal.

## Mental Model

Momentum is **a debt that motion carries, denominated in kg·m/s, and every stop is a repayment**. The debt is non-negotiable — but the *schedule* is yours. Concrete is the loan shark: full repayment now, at crushing interest (force). A cushion, a crumple zone, a folding catch — these are instalment plans: same total, gentle payments, more time. And striking is lending in reverse: the longer your bat stays on the ball at full force, the more momentum you load into the loan. FΔt = Δp is the contract; read it before signing anything with your body.

## Mini Summary

- ✔ p = mv (kg·m/s, vector) — the quantity of motion
- ✔ F = Δp/Δt: force is the rate of momentum change; FΔt = Δp is the payment contract
- ✔ Stretch Δt to shrink F: crumple zones, airbags, bent knees, riding the punch
- ✔ Stretch Δt at max force to deliver more: the follow-through
- ✔ Reversals are the expensive case — signs and directions are not optional

# Guided Practice Quest

Work through the guided steps to price a sprinter's motion, save an egg with milliseconds, and bill a cricket bat eleven thousand newtons honestly.

# Solo Practice Quest

Three impulse engagements: (1) Compute your own walking and sprinting momenta, and the average force your legs absorb stopping from each in 0.5 s versus stumbling to a stop against a wall in 0.05 s. (2) The egg challenge (do it!): design and test a landing system for an egg dropped from 1 m using household materials; explain your design's Δt strategy before testing, and report the outcome with the FΔt contract. (3) Find slow-motion footage of one sporting strike or catch, and write its impulse analysis: where Δt is being stretched, what Δp is changing, and roughly what forces flow. Show units throughout.

# Integration

**Mathematics**: F = Δp/Δt is your first rate-of-change law stated in change-quantities — the difference-quotient form of a derivative, and the version of Newton's second law that survives into rocketry (where mass changes) and relativity (where mv needs correcting). The area under a force–time graph is impulse: your graph-area toolkit from Apprentice motion graphs, redeployed.

**Engineering**: Impact engineering is professional Δt-stretching: vehicle crash structures, packaging design (drop-test specs), playground surfacing standards, and dock fenders all carry legal force-versus-time requirements. The discipline's units — and its court cases — are this lesson's equation.

# Lore Conclusion

You write the contract on Vex's slate — *the bag's debt fixed; my arms renegotiated the schedule* — with both catches priced in newtons. Vex examines it, then does something the Mechanica juniors warned you about: he says nothing at all, which is his highest grade. He racks the sandbag and rolls two gleaming carts to the head of the long brass track — the collision track, its buffers polished by a century of impacts. "Tonight you learned what one body's motion costs to change," he says, setting the carts nose to nose. "Tomorrow, the Mechanica's founding miracle: set two bodies arguing, and watch what the *pair* refuses to change, no matter how violent the argument. We call it conservation — and it is the closest thing this workshop has to a sacred law."
