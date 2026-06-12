---
id: phy-app-m4-12
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m4
moduleTitle: "Module 4: Matter and Heat"
moduleGlyph: "🔥"
moduleSortOrder: 4
topicSlug: everyday_physics
topicTitle: "Everyday Physics"
topicSortOrder: 4
title: "Physics of Getting Around"
sortOrder: 12
xpReward: 30
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Apply forces, energy, and friction to walking, cycling, and driving
  - Explain braking distances using kinetic energy and reaction time
  - Trace a vehicle's energy chain from fuel to dissipation
integrationDomains: [engineering, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains propulsion via friction and the third law (pushing backward on the ground)
    - Uses KE's v² law to explain why braking distance grows steeply with speed
    - Traces a full vehicle energy chain ending in dissipated heat
    - Applies at least three different module concepts to one journey
  keywords: [friction, third law, kinetic, v squared, braking, drag, energy chain, reaction]
  modelAnswer: |
    Every journey is the whole apprentice syllabus in motion. Propulsion is the third law via
    friction: feet, tyres, and wheels push backward on the ground, which pushes the traveller
    forward — no grip, no go. Cruising means balanced forces, with the engine's push exactly
    cancelling drag and rolling resistance. Stopping means destroying kinetic energy, and
    KE = ½mv² makes the bill quadratic: double the speed, four times the braking distance —
    plus a reaction-time gap travelled at full speed before the brakes even engage. The energy
    chain runs chemical (food or fuel) → kinetic → and, at every brake and bump, into heat:
    every journey ends with its energy dissipated and the universe slightly warmer.
guidedSteps:
  - id: phy-app-m4-12-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A car doubles its speed from 30 km/h to 60 km/h. Ignoring reaction time, its braking distance roughly:
    inputConfig:
      options:
        - "Doubles"
        - "Quadruples — kinetic energy grows with v², and the brakes must dissipate all of it"
        - "Stays the same with good brakes"
        - "Triples"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Quadruples — kinetic energy grows with v², and the brakes must dissipate all of it"]
      rejectedFeedback: "Brakes remove energy at a roughly fixed rate per metre, and the account to empty is ½mv² — quadruple the energy at double the speed means quadruple the metres. The v² law is the entire case for speed limits."
    hint: "What does doubling v do to ½mv²?"
    reflectionPrompt: "Why do small speed reductions in towns buy outsized safety gains?"
  - id: phy-app-m4-12-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      When you walk, your foot pushes backward on the ground; by Newton's third law the ground pushes you forward. The contact force that makes this possible is ________.
    inputConfig:
      placeholder: "friction"
    markingRule:
      matchMode: CONTAINS
      accepted: [friction]
      rejectedFeedback: "Friction — the grip between sole and surface. On ice it nearly vanishes, and with it your forward push: walking is friction-powered, third-law transportation."
    hint: "The force ice steals from you."
    reflectionPrompt: "Why do racing cars want MORE friction while engineers fight to reduce it inside the engine?"
  - id: phy-app-m4-12-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Trace the energy chain of a cyclist's commute: breakfast → pedalling → cruising at steady speed → braking at the destination. Name each store/transfer and state where ALL the energy has gone by journey's end. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [chemical, kinetic, drag, friction, heat, thermal, dissipated, brakes]
      rejectedFeedback: "Chemical (food) → muscles (kinetic + plenty of body heat) → at steady speed, all pedal work goes to fighting drag and rolling friction (continuous dissipation to warm air and road) → braking converts the remaining KE to heat in the rims/discs. By arrival: every joule dissipated as slightly warmer air, road, and cyclist. Conservation holds; usefulness doesn't."
    hint: "At constant speed, where is the pedal work going each second? At the final stop, where does ½mv² go?"
    reflectionPrompt: "What does regenerative braking on an e-bike change in this chain?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A car cruises at a steady 100 km/h on a level motorway. The resultant force on it is:"
    options:
      - "Large and forward"
      - "Zero — driving force exactly balances drag and rolling resistance"
      - "Backward"
      - "Equal to its weight"
    correctIndex: 1
    feedback: "Constant velocity = balanced forces (the first law, on wheels). The engine's effort is spent entirely on cancelling resistive forces — which is also why fuel burns fastest at high speed: drag grows steeply with v."
  - type: MULTIPLE_CHOICE
    question: "Total stopping distance = thinking distance + braking distance. Doubling speed does what to each?"
    options:
      - "Doubles both"
      - "Doubles thinking distance (same reaction time at twice the speed) and quadruples braking distance (v² energy)"
      - "Quadruples both"
      - "Leaves thinking distance unchanged"
    correctIndex: 1
    feedback: "Reaction time is fixed, so distance-at-speed doubles; the energy account quadruples. Road-safety charts are this arithmetic, painted on signs."
---

# Hook

Here is the most expensive equation in everyday life: **½mv²**. It is why a 30 km/h bump dents bumpers while a 60 km/h crash — *only twice the speed* — carries four times the destructive energy. It is why stopping distances on the driving-test chart grow so alarmingly down the page. It is why every speed limit sign is, secretly, an energy budget.

And the rest of your journey is physics too, end to end: you walk by throwing the planet backward (it doesn't notice; Newton does), cruise by fighting an invisible drag war to a perfect draw, corner by borrowing friction, and arrive — every single time — by converting your hard-won kinetic energy into a puff of warmth in the brakes. The apprentice syllabus you've spent four modules earning is not classroom furniture. It is the operating manual for getting home. Today: the final inspection.

# Lore Introduction

For your last day in the rotation, Calde and — to your surprise — Magus Thorne walk you down the high-road to the city gate, where the morning traffic streams: carters, riders, the gleaming steam-omnibus hissing at its stop, apprentice couriers weaving on their absurd tall-wheeled cycles. The two masters exchange a glance of long custom. "The final examination of the apprentice rotation," says Thorne, "is conducted here, and has been for four hundred years." Calde grins. "Everything on this road is yours now. The courier leaning into the bend. The carter's straining team. The omnibus, screaming its brakes down the gate-hill —" as if cued, it does, brake-blocks smoking faintly. "Walk the road with us," says Thorne, "and read it aloud. All of it. Forces, energy, heat, particles. Miss nothing — and mind, as ever, the gap between *seeing* a thing and *establishing* it." It is, you realise, the most dangerous exam format of all: the world itself, unedited.

# Core Learning

## Concept Introduction

**Going: the third law on the ground.** Walking, cycling, driving — all propulsion through ground contact is the same transaction: push the ground *backward* (via friction at sole or tyre), and the ground's reaction pushes you *forward*. No friction, no transaction: ice strands you mid-stride; spinning wheels on mud burn fuel into noise. Friction here is the *enabler* — which is why tyres are engineered to grip, while the same engineers wage war on friction *inside* the machine (oil, bearings) where it only taxes.

**Cruising: the drag war's stalemate.** At constant speed the resultant is zero (first law): every newton of drive is consumed by **air drag** (growing steeply with speed — roughly with v²) plus **rolling resistance**. Consequences: fuel/effort per kilometre climbs brutally at high speed (the drag bill), cyclists crouch and lorries convoy (shrinking the bill), and "cruising" is not rest but a perfectly balanced, continuously-paid fight.

**Stopping: the v² reckoning.** Brakes are energy-conversion devices: pads grip discs and *dissipate* ½mv² as heat (smell a coach's brakes at the bottom of a pass). The account is quadratic in speed — **double v, quadruple the braking distance** — and on top rides **thinking distance**: a fixed reaction time (~1 s) travelled at full speed before any conversion begins. Total stopping distance = v × t_react + v²/(2a_brake): the driving-chart's two-part arithmetic. Wet roads cut friction's conversion rate (a_brake falls); the quadratic stretches further.

**The full energy chain**, every journey: chemical (food/fuel/battery) → kinetic (+ unavoidable engine/body heat — engines ~30%, muscles ~25% efficient) → continuously dissipated against drag and rolling resistance → final KE dumped as brake heat at arrival. Conservation's audit closes the same way always: journey's energy, fully dissipated, world imperceptibly warmer. (Regenerative braking's trick: divert the last step into the battery instead of the disc — the chain's only refundable link.)

## Why It Matters

- The v² law is the most consequential piece of public physics: speed limits, following distances ("two-second rule" — a thinking-distance buffer), and crash outcomes all price by it.
- Fuel economy is drag economics: the difference between 90 and 120 km/h on the motorway is largely the v²-ish drag bill — measurable on any trip computer.
- Transport engineering is this lesson industrialised: ABS (keeping tyres in their best friction regime), crumple zones (stretching collision time, Module Two), brake cooling, aerodynamic design.

## Worked Examples

**Example 1: The stopping-distance chart, derived**
At 13 m/s (~47 km/h): thinking (1 s) = 13 m; braking (a ≈ 6.5 m/s²) = 13²/13 = 13 m; total 26 m. At 26 m/s (~94 km/h): thinking 26 m; braking 52 m... no — 26²/13 = **52 m**; total 78 m: *three times* the distance for twice the speed (double + quadruple, summed). Print it on a sign and you have the highway code's chart; run it in rain (a ≈ 3 m/s²) and the braking term more than doubles again.

**Example 2: Why the omnibus brakes smoke**
A 10-tonne omnibus descending the 40 m gate-hill at steady speed must dissipate its *potential* energy continuously: mgh = 10,000 × 10 × 40 = 4 MJ if taken in one conversion — into brake blocks of perhaps 20 kg of iron (c = 450). Unrelieved, ΔT = E/(mc) ≈ 440 °C: smoking, fading brakes. Hence engine braking, crawler lanes, and runaway escape ramps (beds of deep gravel converting KE to heat and deformation in metres). Mountain roads are mcΔT problems with scenery.

**Example 3: The cyclist's drag crouch, costed**
A commuter holding 25 km/h spends ~150 W, two-thirds against air drag. Dropping into a crouch cuts drag area ~20%: same speed for ~120 W, or ~27 km/h for the original effort. Multiply by an hour a day and the crouch is worth a week of commute-energy a year — aerodynamics, settled at handlebar level.

## Common Mistakes

- **"Good brakes stop you instantly"** — brakes convert energy at a finite rate; no pad repeals ½mv², and reaction time spends metres before they even engage.
- **Doubling speed, doubling caution** — caution must *quadruple*; the chart, not intuition, sets the gap.
- **"Friction is the enemy"** — it is the only reason you can start, steer, or stop; the enemy is friction *in the wrong places* (and its heat where unwanted).
- **Treating cruising as effortless** — constant speed on the flat is a continuously funded stalemate against drag; the fuel gauge knows.
- **Forgetting the chain's end** — every journey's energy ends as dissipated heat (unless regeneratively pocketed); "where did it go?" always has an answer.

## Mental Model

A journey is **a financial round trip with three accounts and a strict auditor**. Departure: you draw down chemical savings (breakfast, petrol) and buy kinetic capital — at a poor exchange rate, most lost to the body's or engine's furnace. En route: a continuous *drag tax* on speed, levied quadratically — cruise twice as fast, pay the taxman quadrupled per kilometre. Arrival: whatever kinetic capital remains must be surrendered — at the brake-teller's window, converted to heat, non-refundable (unless your e-bike banks it). The auditor — conservation — closes every journey's books to the joule. Safe travel is simply solvency: never carry more kinetic capital than the road ahead can convert in the distance available.

## Mini Summary

- ✔ Propulsion = third law via friction: push the world backward, be pushed forward
- ✔ Cruising = balanced forces; the drag bill grows ~v² — speed is expensive
- ✔ Braking distance quadruples when speed doubles (½mv²); thinking distance doubles on top
- ✔ Brakes, hills, and crashes are energy-conversion problems: mcΔT and crumple time decide outcomes
- ✔ Every journey's chain ends in dissipated heat — conservation closes the books

# Guided Practice Quest

Work through the guided steps to quadruple a braking bill, found locomotion on friction's grip, and audit a cyclist's commute to the last dissipated joule.

# Solo Practice Quest

The road exam, three readings: (1) *Your own stopping*: walking briskly, have a friend call "stop" at a random moment — estimate your reaction distance and total stopping distance; then estimate the same for yourself on a bicycle at two speeds, using the two-part formula with honest numbers. (2) *The drag bill*: find (online or from a vehicle's trip computer) fuel-consumption figures for one car at two motorway speeds; compute the percentage cost of the extra speed and attribute it. (3) *One full chain*: choose any real journey you made this week and write its complete energy audit — source store, conversion losses, cruising dissipation, final braking — ending with the Bursar's line: "all accounted." Close with one sentence on which single behaviour change your numbers most recommend.

# Integration

**Engineering**: Vehicle engineering is this lesson's arms race: ABS and traction control manage the friction budget, aerodynamicists shave the drag tax, brake engineers manage mcΔT (carbon-ceramic discs tolerate 1,000 °C), and crash structures stretch collision time to shrink force. Regenerative braking — the chain's one refund — is why electric vehicles thrive in stop-start cities.

**Biology**: Your body runs the same books: muscles at ~25% efficiency (the rest is the warmth of exercise), tendons as elastic energy-returns (a kangaroo's regenerative braking), reaction time as biology's fixed ~0.2–1 s overhead — trainable downward only slightly, which is why the safety margin must be engineered into following distance, not hoped into reflexes.

# Lore Conclusion

You walk the gate-road between the two masters and read it aloud, end to end: the courier's lean priced in friction, the omnibus brakes diagnosed by mcΔT and prescribed a crawler lane, the carter's team auditied — *audited* — from oats to axle-heat, the rain beginning and with it, recalculated aloud, every stopping distance on the hill. At the gate itself Thorne halts, and for a long moment says nothing at all. Then: "Four hundred years of this examination, and the road never once repeats itself. That is why it works." Calde claps your shoulder with a smith's delicacy, which staggers you slightly. "The rotation is complete, apprentice. Measurement to mechanics, waves to weather, and home again." Thorne produces, from his satchel, a familiar object: your own logbook from the first morning — and beside your long-ago entry for the brass rod, he writes the rotation's closing line in the Observatory's careful hand: *Ready for the Trials.* "Rest," he says. "Then come hungry. The Apprentice Project waits — and this time, no one walks the road ahead of you."
