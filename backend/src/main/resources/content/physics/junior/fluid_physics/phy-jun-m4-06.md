---
id: phy-jun-m4-06
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m4
moduleTitle: "Module 4: Applied Physics"
moduleGlyph: "🔧"
moduleSortOrder: 4
topicSlug: fluid_physics
topicTitle: "Fluid Physics"
topicSortOrder: 2
title: "Flowing Fluids"
sortOrder: 6
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Apply continuity — narrower pipe, faster flow
  - Use Bernoulli's insight — faster flow, lower pressure — qualitatively
  - Describe viscosity and drag and their dependence on speed
integrationDomains: [engineering, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Applies continuity (A₁v₁ = A₂v₂) to pipes, rivers, and nozzles
    - Uses faster-flow-lower-pressure to explain one real phenomenon
    - Describes viscosity as fluid friction and drag's growth with speed
    - Distinguishes smooth (laminar) from churning (turbulent) flow
  keywords: [continuity, Bernoulli, faster lower pressure, viscosity, drag, laminar, turbulent]
  modelAnswer: |
    Moving fluid obeys continuity: what flows in must flow out, so narrowing a pipe speeds the
    flow — A₁v₁ = A₂v₂ — the thumb-on-hose law. Bernoulli's insight follows from energy
    bookkeeping: where flow is faster, pressure is lower — speeding up must be paid for, and
    pressure is the account. It explains the shower curtain's clingy lunge, roofs lifting in
    gales, atomisers, and (with circulation and deflection) the lift of wings. Viscosity is
    fluid friction — honey's reluctance — and drag on moving bodies grows steeply with speed
    (roughly v²), which is why streamlining matters and raindrops fall at terminal velocity.
    Slow, syrupy flows slide in smooth laminar layers; fast ones break into churning
    turbulence — the difference between a tap's glassy column and its sputtering rush.
guidedSteps:
  - id: phy-jun-m4-06-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      Continuity: water flows at 1 m/s through a pipe of area 8 cm². Through a narrowed section of 2 cm², its speed becomes ________ m/s.
    inputConfig:
      placeholder: "4"
    markingRule:
      matchMode: CONTAINS
      accepted: ["4"]
      rejectedFeedback: "A₁v₁ = A₂v₂: 8 × 1 = 2 × v₂ → v₂ = 4 m/s. Quarter the channel, quadruple the speed — your thumb on the hose has always known."
    hint: "Area × speed stays constant for an incompressible flow."
    reflectionPrompt: "Where else have you seen this law: river narrows, crowd bottlenecks, motorway lane closures?"
  - id: phy-jun-m4-06-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A hot shower's curtain billows INWARD toward the spray. The accepted explanation:
    inputConfig:
      options:
        - "Water attracts plastic"
        - "The spray drives a fast air-and-droplet flow inside; faster flow means lower pressure there, and the calmer outside air pushes the curtain in"
        - "Steam melts the curtain"
        - "The curtain shrinks when damp"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The spray drives a fast air-and-droplet flow inside; faster flow means lower pressure there, and the calmer outside air pushes the curtain in"]
      rejectedFeedback: "Bernoulli's signature: the jet entrains a fast downdraught inside the curtain; fast = lower pressure; the still air outside (full pressure) wins the pushing contest. The same imbalance lifts roofs in gales (fast wind OVER, still air UNDER — the roof is pushed up from inside) and drives perfume atomisers."
    hint: "Which side has the fast-moving air? Which side therefore pushes harder?"
    reflectionPrompt: "Why do gale-stripped roofs often blow OUTWARD/upward rather than being pressed in?"
  - id: phy-jun-m4-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A raindrop falls from 2 km yet lands at a harmless ~8 m/s. Explain its journey using drag's growth with speed and the idea of terminal velocity. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [drag, grows, speed, terminal, balance, weight, constant, resultant zero]
      rejectedFeedback: "Falling accelerates the drop; drag grows steeply with speed (≈v²) until it equals the drop's weight — resultant zero, acceleration zero: terminal velocity, held for the rest of the fall (your Apprentice skydiver, miniaturised). Without air, 2 km of free fall would deliver ~200 m/s — rain would be artillery. Drag's quadratic appetite is why drizzle is survivable."
    hint: "What force grows as it speeds up, and what happens when it matches the weight?"
    reflectionPrompt: "Why do bigger drops land faster than mist? (Compare how weight and drag scale with size.)"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Viscosity measures a fluid's:"
    options:
      - "Density"
      - "Internal friction — its resistance to flowing and being sheared (honey high, water low, air tiny)"
      - "Temperature"
      - "Compressibility"
    correctIndex: 1
    feedback: "Viscosity is flow-friction: layers dragging on layers. It thins with heat for liquids (warm honey pours; cold engines need multigrade oil) and sets the price of pumping everything from pipelines to blood."
  - type: MULTIPLE_CHOICE
    question: "Laminar versus turbulent flow:"
    options:
      - "Two names for the same thing"
      - "Laminar slides in smooth ordered layers (slow, viscous, narrow); turbulent churns and mixes (fast, large, eddying) — drag and mixing rise sharply with the change"
      - "Laminar is only for gases"
      - "Turbulent flow is always slower"
    correctIndex: 1
    feedback: "The tap shows both: glassy column at a trickle, sputtering chaos opened up. Turbulence multiplies drag and mixing — pipeline engineers fight it; your lungs and teaspoons exploit it."
---

# Hook

Put your thumb over a garden hose and the lazy dribble becomes a jet that crosses the lawn. Stand on a station platform as the express passes and feel yourself *tugged toward* the train. Watch a gale peel a roof — not crush it inward, but lift it *off, from inside*. And ask why a raindrop, falling two kilometres, arrives as a tap on your hood rather than a bullet.

Still fluids took two lessons; moving fluid is a richer character with three rules of its own. **Continuity**: what flows in must flow out — narrow the channel, the flow *must* speed up. **Bernoulli's bargain**: that extra speed is paid for from the pressure account — *faster flow pushes softer*, the single most counterintuitive sentence in everyday physics and the explanation for the train-tug, the roof, and the clingy shower curtain. And **drag**: the fluid's revenge on whatever moves through it, growing with the square of speed. Today, the Mechanica goes to the mill-race — bring the apple.

# Lore Introduction

The race runs quick and green in the morning light, and Vex walks you along its banks to where the Academy's engineers, generations back, built their teaching reach: the channel narrows to half its width between dressed stones, then opens again. "The apple, junior." You drop it in upstream: it ambles, then — entering the narrows — *surges* forward, hurrying between the stones, then relaxes to an amble in the broad reach beyond. "Again," says Vex, and this time he has you watch the water's level: visibly LOWER through the narrows, where it runs fastest. "Mark that well — it troubles every junior. The water hurries where the channel pinches — it must, or where would it pile? — and where it hurries..." he taps the lowered surface with his cane, "...it presses less. Speed is bought from somewhere. Find me the account it's drawn from, and the wheel, the wing, and the shower curtain are all yours by lunch."

# Core Learning

## Concept Introduction

**Continuity — the conservation of flow.** Incompressible fluid in a channel can't pile up or vanish:

```
A₁ v₁ = A₂ v₂        (area × speed = constant flow rate)
```

Half the channel, double the speed: the hose-thumb law, the river-narrows surge, nozzles, and the misleading vigour of canyon rapids (same river, pinched). It's charge-conservation's cousin — your series-circuit current rule, wearing water.

**Bernoulli — speed is bought from pressure.** Energy bookkeeping along a streamline: a parcel of fluid that speeds up gained kinetic energy, and (level flow, no pump in sight) the only account available is its pressure — so **where flow is faster, pressure is lower**. The narrows' lowered surface was the receipt. Consequences, once seen, everywhere:

- **The platform tug & the clingy curtain**: fast air beside you = soft push; still air behind = full push; you (and the curtain) are pushed *toward* the rush
- **Roofs in gales**: wind races OVER the roof (fast, soft); attic air sits still (full pressure) — the house inflates its own lid off
- **Atomisers & carburettors**: a fast air jet across a tube's mouth drops the pressure; liquid climbs and shreds into mist
- **Wings (the honest version)**: a wing's shape and tilt deflect air *downward*; the air above flows faster (lower pressure), below slower (higher) — the pressure difference, equivalently the reaction to all that down-thrown air (Newton's third, never absent), is lift. (The full aerodynamic story is Senior-tier; both bookkeepings already agree.)

**Viscosity and drag — the fluid fights back.** Viscosity is internal friction (honey ≫ water ≫ air; liquids thin when heated). Bodies moving through fluid pay **drag**: creeping motion pays viscous drag (∝ v); everyday speeds pay form drag (**∝ v²** — your cycling crouch and motorway fuel bills). Falling bodies accelerate until drag = weight: **terminal velocity** (raindrop ~8 m/s, skydiver ~55, dust ~mm/s — why clouds float: their droplets' terminal speed is near zero). And flow itself has two temperaments: **laminar** (smooth sliding layers — slow, narrow, viscous) versus **turbulent** (churning eddies — fast, large; drag and mixing soar). The tap demonstrates both for free.

## Why It Matters

- Continuity and Bernoulli run plumbing, blood flow, carburettors, chimney draw, sailing, and every flow meter (venturis measure speed by the pressure dip).
- Drag's v² is transport economics (you priced it in fuel) and weather's mercy (terminal velocities); streamlining is its negotiation.
- Laminar/turbulent literacy spans pipelines (pumping costs), aircraft (boundary layers), and medicine (the stethoscope hears turbulence where arteries narrow).

## Worked Examples

**Example 1: The wheel's water, audited**
The Academy's mill-race delivers 0.8 m³/s through a 1.6 m² channel: v = 0.5 m/s. The penstock narrows to 0.2 m²: v = 4 m/s (continuity). Kinetic energy per second arriving at the wheel: ½ρ(Av)v² = ½ × 1000 × 0.8 × 16 ≈ 6.4 kW — the Tower's evening lamps, riding the narrows' surge. Hydro engineering is continuity, Bernoulli, and the Foundry's energy ledger in one channel.

**Example 2: The doctor's Bernoulli**
A narrowed artery (stenosis): continuity speeds the blood through the pinch; Bernoulli drops the pressure there — which can partially collapse the vessel, then reopen it, flutter and all. The turbulence downstream is audible: the *bruit* a stethoscope hunts. Diagnosis by flow physics — and stents restore the area term in A×v.

**Example 3: Curveballs and free kicks**
A spinning football drags air around with its surface (viscosity's grip): one side's airflow speeds up (ball surface moving WITH the flow), the other slows. Faster side, lower pressure: the ball swerves toward it — the Magnus effect. Every banana free kick, slice, and topspin dip is Bernoulli refereed at speed; the table-tennis ball's extravagant curves are the same physics with less inertia to discipline it.

## Common Mistakes

- **"Faster flow pushes harder"** — the great inversion: faster flow pushes SOFTER (sideways, on its surroundings); what pushes harder is fluid *brought to a stop* (the firehose's blast is arrest, not flight).
- **Misapplying Bernoulli across different flows** — the bargain holds along a flow's own streamline; comparing unrelated jets and rooms invites nonsense. (The curtain and roof cases compare a flow with adjacent STILL fluid — the legitimate move.)
- **The schoolbook wing myth** — "air must rejoin at the trailing edge, so the longer top path forces faster flow": false (parcels don't rejoin, and flat wings fly upside down). Deflection + circulation is the honest account; Bernoulli and Newton are two ledgers of one event.
- **Ignoring continuity's premise** — gases at high speed compress (the rule bends near sonic speeds); rivers with tributaries change their flow total.
- **Linear drag intuition** — doubling speed quadruples form drag; every vehicle, cyclist, and falling conker negotiates v².

## Mental Model

Moving fluid is **a crowd hurrying through a building with one inviolable house rule: nobody stops, nobody piles up**. Corridors narrow? The crowd must jog (continuity). And here is the subtlety your instincts miss: a jogging crowd has no spare attention for shoving the walls — the harder they hurry forward, the softer their sideways press (Bernoulli's bargain: forward hustle is bought from wall-pushing). Stand in a doorway beside the rushing corridor and the still room behind you — pushing at full leisure — bundles you *into* the rush. Drag is what the crowd does to anyone walking against it (worse than linearly as you hurry); viscosity is the crowd's internal jostle-friction (treacle crowds versus air crowds); and turbulence is the corridor's temper finally breaking — eddies, elbows, and a roar the stethoscope of the world can hear.

## Mini Summary

- ✔ Continuity: A×v constant — narrows hurry the flow (hose thumbs, rapids, penstocks)
- ✔ Bernoulli: faster flow, lower sideways pressure — curtains lunge, roofs lift, atomisers mist, wings (with deflection) fly
- ✔ Viscosity = flow friction; drag grows ~v² — terminal velocity when drag meets weight
- ✔ Laminar slides, turbulent churns — drag, mixing, and audible bruits mark the change
- ✔ Fast fluid pushes softer; ARRESTED fluid pushes hardest — keep the inversion straight

# Guided Practice Quest

Work through the guided steps to quadruple a pipe's hurry by quartering its width, send the shower curtain lunging on Bernoulli's bargain, and land a two-kilometre raindrop at a survivable eight metres per second.

# Solo Practice Quest

Three experiments in moving fluid (all kitchen-safe): (1) *Bernoulli's trio*: blow OVER a strip of paper held to your lip (it rises); blow BETWEEN two hanging cans or apples on strings (they clash); try to blow a card off a cotton reel through the reel's hole (it clings) — explain each in faster-flow-lower-pressure language. (2) *Continuity at the tap*: open a tap to a smooth column and observe it NARROW as it falls — explain via continuity (the falling water accelerates; A must shrink); find the flow rate by timing a measured jug and compute the speed at two heights. (3) *Terminal velocity derby*: drop coffee filters (stackable for mass variation) and time their falls; show heavier stacks fall faster and explain via the drag-weight balance. Close with the honest two-ledger sentence on how wings lift — Newton's and Bernoulli's accounts in one breath.

# Integration

**Engineering**: Fluid dynamics is a profession's ocean: pipeline networks (pumping costs vs diameter — drag economics), venturi flow meters, aerofoil design and wind tunnels, turbomachinery (the penstock's surge through a Francis turbine), and CFD — computational fluid dynamics — where your Senior-tier numerical methods will one day churn these very equations.

**Biology**: Bodies are flow networks throughout: the heart's output obeying continuity through narrowing arteries, the bronchial tree laminar by design (turbulence wastes breath — wheezes are its symptom), swimming and flight as drag-and-lift negotiations refined for 400 million years, and the maple seed's helicopter fall — terminal velocity, weaponised for dispersal.

# Lore Conclusion

By lunch you bring Vex the account the narrows draw on — *pressure pays for speed; the lowered surface is the withdrawal slip* — and demonstrate with the paper strip rising off your lip and the two apples clashing on their strings. He receives it with the cane-tap that serves him for applause, then leads you to the race's end, where the channel's whole hurry breaks white against the Academy's water-wheel and the wheel turns, as it has for centuries, with the evening's lamplight on its shoulders. "Still water weighed, moving water read. The fluids are yours, junior." He turns and looks up — past the wheel, to the aqueduct that carries the race over the valley on its stone legs, to the crane on the Mechanica's roof, to the high bridge where the city road crosses. "And now, the synthesis. Everything from this module — materials, moments, fluids, forces — was only ever apprenticeship for THOSE. Structures that stand for centuries. Machines that transmit a river's strength. Vehicles that carry the realm. Three days, three arts, all of it physics you now own. Tomorrow we start with the oldest: why the bridge does not fall down."
