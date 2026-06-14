---
id: phy-jun-m1-11
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: mechanical_systems
topicTitle: "Mechanical Systems"
topicSortOrder: 4
title: "Pulleys, Gears, and Machines"
sortOrder: 11
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Analyse pulley systems via rope count and the force-distance trade
  - Analyse gear pairs via tooth ratios for force and speed
  - Compute the efficiency of real machines
integrationDomains: [engineering, history]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Determines pulley advantage from supporting rope count and applies the distance penalty
    - Uses gear ratios for speed/torque trades
    - Computes efficiency = useful work out / work in for a real machine
  keywords: [pulley, ropes, gear ratio, teeth, torque, efficiency, work in, trade]
  modelAnswer: |
    Every machine strikes the same bargain: force traded against distance, work never
    discounted. A pulley block with four supporting ropes lifts a load with one quarter the
    effort — while the hauler pulls four metres of rope per metre risen. Gear pairs trade
    through teeth: a small gear driving one with three times the teeth turns three times for
    each output turn, tripling torque while the speed drops to a third. Real machines pay
    friction's tax, measured by efficiency = useful work out ÷ work in: a block-and-tackle at
    75% needs its hauler to supply a third more work than the lift itself requires, the
    remainder warming the sheaves.
guidedSteps:
  - id: phy-jun-m1-11-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A block-and-tackle supports its load on 4 rope sections. Ignoring friction, lifting a 600 N load needs an effort of ________ N.
    inputConfig:
      placeholder: "150"
    markingRule:
      matchMode: CONTAINS
      accepted: ["150"]
      rejectedFeedback: "Four ropes share the 600 N: effort = 600/4 = 150 N. The invoice: you haul 4 m of rope for each metre the load rises — work conserved, as always."
    hint: "Each supporting rope carries an equal share."
    reflectionPrompt: "How much rope must you haul to raise the load 2 m, and how does that confirm the well-plaque's law?"
  - id: phy-jun-m1-11-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A 12-tooth gear drives a 36-tooth gear. The output gear turns:
    inputConfig:
      options:
        - "Three times faster, with one third the torque"
        - "Three times slower, with (ideally) three times the torque"
        - "At the same speed"
        - "Backwards only"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Three times slower, with (ideally) three times the torque"]
      rejectedFeedback: "Teeth mesh one-for-one: the 12-tooth must turn 3 times per output turn. Speed ÷3, torque ×3 (friction aside) — the rotational version of the lever's trade. This is a 'low gear': climbing power at crawling pace."
    hint: "Both gears pass the same teeth per second. Which must spin faster?"
    reflectionPrompt: "Which way does a bicycle's lowest gear arrange its chainring and sprocket sizes — and why?"
  - id: phy-jun-m1-11-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A winch raises a 900 N crate 4 m while its operator does 4,800 J of work at the handle. Compute the efficiency, name where the missing energy went, and state the work the operator would need with a frictionless winch. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["75", "3600", "3,600", friction, heat, bearings]
      rejectedFeedback: "Useful out = 900 × 4 = 3,600 J; efficiency = 3,600/4,800 = 75%. The missing 1,200 J became heat in bearings, rope, and gears. Frictionless, the operator would supply exactly 3,600 J — machines can only ever approach the plaque's price, never beat it."
    hint: "Useful work = weight × height. Efficiency = useful ÷ supplied."
    reflectionPrompt: "Why does a well-oiled machine still never reach 100%?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Every ideal machine — lever, pulley, gear — conserves:"
    options:
      - "Force"
      - "Speed"
      - "Work (energy): force × distance in = force × distance out"
      - "Distance"
    correctIndex: 2
    feedback: "Machines redistribute the F and the d; their product is untouchable. Real machines do worse, never better — friction's tax is one-way."
  - type: MULTIPLE_CHOICE
    question: "A bicycle's highest gear (large chainring, small rear sprocket) gives:"
    options:
      - "More force at the wheel for climbing"
      - "More wheel speed per pedal turn, at the cost of pedal force feeling harder"
      - "Less speed and less force"
      - "Free energy"
    correctIndex: 1
    feedback: "High gear = speed-buying: each pedal revolution drives many wheel revolutions, so the road's resistance is felt at full strength. Low gear reverses the trade for hills. Same legs, redistributed bargain."
---

# Hook

In 1586, engineer Domenico Fontana faced a pharaoh's problem: move a 327-tonne Egyptian obelisk across Rome and stand it upright — using nothing but men, horses, and rope. His solution: forty capstans, hundreds of pulleys, kilometres of hemp — and 900 men supplying what the arithmetic of this lesson promised they could. The obelisk stands in St Peter's Square today, a 327-tonne receipt for the force-distance trade.

Pulleys and gears are the lever's idea — divide the force, multiply the distance — *industrialised*: struck through ropes that can run hundreds of metres and teeth that can compound the trade stage after stage. A modern crane, a bicycle's gearset, a watch's escapement, and a wind turbine's gearbox are all the same single bargain in different denominations. Today you learn to read any of them at a glance — count ropes, count teeth — and to bill the one tax no machine evades: friction.

# Lore Introduction

Vex walks you down the machine-floor's full length like a quartermaster among siege engines: skeins of block-and-tackle from the Academy's building centuries, a windlass that raised the bell-tower's bells, gear-trains from mills and clocks in glass cases, and — pride of the floor — a working crane-model whose every sheave and cog is cut in brass. "Each of these defeated some impossible weight," he says. "And every one of them is the Beam wearing rope or teeth." He sets the crane-model's hook under a lead ingot no junior could lift barehanded, then hands you the haul-rope. You pull — easily — arm over arm over arm, and the ingot creeps upward a fraction of your hauling. "Feel the bargain in your shoulders, junior. You are paying in *distance*. The floor's three questions, for every machine on it: how is the trade struck? at what rate? and—" he touches the warm sheave-axle, "—what does friction skim?"

# Core Learning

## Concept Introduction

**Pulleys — the trade struck in rope.** A single fixed pulley merely redirects (advantage 1 — but pulling *down* beats lifting up). Compound a **block-and-tackle** and the trade begins:

```
mechanical advantage = number of rope sections supporting the load
effort = load / N        rope hauled = N × height risen
```

Four supporting ropes: quarter the effort, four times the haul. (Count honestly: only sections actually supporting the moving block.) Work in = work out, ideal case — the plaque's law in hemp.

**Gears — the trade struck in teeth.** Meshed gears pass teeth one-for-one, so:

```
speed ratio = teeth_driven / teeth_driver        torque ratio = the inverse (ideal)
```

Small driving large ("low gear"): output slower, *stronger* — hill-climbing, winching, the mill's grindstone. Large driving small ("high gear"): output faster, weaker per turn — speed-buying, the racer's flat-out sprint. Gear *trains* compound stage by stage: a watch trades one mainspring revolution into thousands of balance-wheel beats; a wind turbine's gearbox trades 15 rpm of blade into 1,500 rpm of generator.

**Efficiency — friction's skim.** Real machines deliver less than they're paid:

```
efficiency = useful work out / work in   (× 100%)
```

Block-and-tackle: 70–90% (rope flex, sheave bearings). Gear stages: ~95–98% *each* — but stages multiply: a 10-stage train at 97% per stage delivers 0.97¹⁰ ≈ 74%. The shortfall is always the same recipient: heat, in bearings and teeth — Calde's domain collecting its commission on Vex's floor. Design lore: fewer stages, better bearings, and (the great cheat that isn't) *no machine ever exceeds 100%* — the plaque is carved over this floor too.

## Why It Matters

- Cranes, lifts, winches, and rigging — construction's entire lifting vocabulary — are rope-count arithmetic with safety factors.
- Gear ratios run civilisation's rotating machinery: vehicles (gearboxes matching engine sweet-spots to road demands), turbines, mills, robotics actuators, clocks.
- Efficiency budgets decide real designs: a winch's motor must be sized for the work *plus* the skim; compounding losses is why direct-drive (gearless) wind turbines were worth inventing.

## Worked Examples

**Example 1: Rigging the obelisk (a flavour)**
Each of Fontana's capstans wound rope through multi-sheave blocks at advantage ~16: a team pushing 3,000 N at the capstan bars delivered ~48 kN per line, 40 lines sharing the 3.2 MN load. The price: kilometres of rope hauled metre by metre, for hours, to raise the stone degrees at a time. Force was never created — only *gathered*, from 900 backs, through distance.

**Example 2: The bicycle's ledger**
Lowest gear: 34-tooth chainring driving a 34-tooth sprocket (ratio 1:1) — one wheel-turn per pedal-turn; with crank (0.17 m) versus wheel radius (0.34 m) the *lever* part halves the force again... and the climb becomes spinnable. Highest gear: 50 driving 11 (4.5 wheel-turns per pedal-turn): each pedal stroke buys 9+ metres of road, and your legs feel every newton of the drag bill at full rate. Twenty-two gears = twenty-two pre-negotiated contracts between knees and hill.

**Example 3: Billing a gear train**
A winch motor supplies 500 W through three gear stages (96% each) and a rope drum (94%): overall efficiency 0.96³ × 0.94 ≈ 83% → 415 W of lifting. Raising 200 kg: v = P/F = 415/2,000 ≈ 0.21 m/s. The designer who forgot the skim would have promised 0.25 and under-delivered every shift — efficiency isn't pedantry, it's the difference between a spec met and a lawsuit.

## Common Mistakes

- **Counting pulley wheels instead of supporting ropes** — wheels redirect; only load-bearing rope sections divide the force.
- **Expecting gears to multiply speed AND torque** — strictly either/or (less friction's cut); any claim of both is a perpetual-motion pitch wearing a gearbox.
- **Forgetting the distance invoice** — every advantage N is repaid as N× the haul or N× the turns; check it whenever a deal looks free.
- **Treating efficiency as fixed** — it varies with load and speed; machines have sweet spots (why engines have rev ranges and cyclists cadences).
- **Compounding stages carelessly** — efficiencies *multiply*; ten "nearly perfect" stages are no longer nearly perfect.

## Mental Model

The machine-floor is **a currency exchange for the same two denominations everywhere: force and distance**. Levers exchange across a rigid bar, pulleys across lengths of rope, gears across counted teeth — but every booth posts the identical, regulated rate: *the product is invariant*. Want your burden in smaller force-coins? You'll receive proportionally more distance-coins to carry; the till always balances. And in the corner of every booth sits the same silent partner — friction — taking a fixed percentage on each transaction, payable in heat. The whole of mechanical engineering is choosing your booths wisely, chaining exchanges shrewdly, and keeping the partner's cut small.

## Mini Summary

- ✔ Pulley advantage = supporting rope count; effort ÷N, haul ×N
- ✔ Gear ratio = tooth ratio: small→large trades speed for torque ("low gear"), large→small the reverse
- ✔ Trains compound ratios — and compound friction's skim (efficiencies multiply)
- ✔ Efficiency = useful out / in; the shortfall is always heat in bearings and teeth
- ✔ No machine beats 100% — force is only ever gathered through distance, never created

# Guided Practice Quest

Work through the guided steps to quarter a load with four ropes, slow a gear into triple strength, and bill a winch for friction's honest skim.

# Solo Practice Quest

Three floor-commissions: (1) *Build a tackle*: with string, two smooth rods (or broom handles) and a weight, rig a 2- and then 4-rope advantage; verify both the force reduction (spring balance or feel, honestly described) and the rope-hauled-to-height ratio by measurement. (2) *Audit your gears*: count the teeth (or look up the ratios) on a bicycle, hand-drill, egg-beater, or any geared device you own; compute the speed and torque trades for the extreme settings, and write one sentence on what job each extreme was designed to win. (3) *Efficiency in the wild*: estimate efficiency for one real lifting task — a winch video with numbers, a gym cable machine, or your tackle from (1) loaded with a known weight (compare your measured effort with the ideal) — and name the skim's location. Close with one paragraph: design a machine (rope, gears, levers — your choice) for a stated impossible-feeling task, quoting advantage, distance invoice, and expected efficiency.

# Integration

**Engineering**: Real machine design adds materials limits to the bargain — rope tensions, tooth stresses, bearing pressure ratings — plus the dynamics of starting and stopping loads (Module One's impulse, returned). Gearboxes, harmonic drives, and continuously-variable transmissions are this lesson's trade made adjustable, compact, or stepless; the obelisk's modern descendant is the 3,000-tonne crawler crane, rope-count arithmetic intact.

**History**: The "simple machines" — lever, pulley, wheel, wedge, screw, inclined plane — were antiquity's complete physics toolkit, building everything from Stonehenge to cathedrals millennia before F = ma was written. The Renaissance's machine-books (Ramelli, Leonardo's notebooks) read as love letters to this lesson; the Industrial Revolution was, mechanically speaking, the moment gear-train compounding met a tireless prime mover.

# Lore Conclusion

You end the day having re-rigged the crane-model yourself — six supporting ropes now, the lead ingot rising to a fingertip pull, your other hand feeding out fathoms of the bargain's price. Vex inspects the rigging, tests the warm sheave-axle with practised fingers, and quotes your efficiency aloud (a deflating but fair 81%). "The floor's three questions, answered for every engine on it. Module One is yours, junior — motion, circles, the heavens, and the machines." He says it without ceremony, which from Vex is ceremony. Then he nods to the far end of the floor, where one last station waits, unlit: a tall, slender model tower, weighted blocks beside it, and a small placard you can't yet read. "One question remains before the module signs off, though. Everything we built today *moves*. Tomorrow's question is older and quieter: why does anything *stand*? Towers, bridges, the Academy itself. Stability, junior — and the precise, computable difference between leaning and falling."
