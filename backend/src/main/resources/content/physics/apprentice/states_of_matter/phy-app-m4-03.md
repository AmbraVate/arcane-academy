---
id: phy-app-m4-03
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m4
moduleTitle: "Module 4: Matter and Heat"
moduleGlyph: "🔥"
moduleSortOrder: 4
topicSlug: states_of_matter
topicTitle: "States of Matter"
topicSortOrder: 1
title: "Density"
sortOrder: 3
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Calculate density using ρ = m/V
  - Predict floating and sinking by comparing densities
  - Measure the volume of irregular objects by displacement
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Computes ρ = m/V with correct units (kg/m³ or g/cm³)
    - Predicts float/sink by comparing with the fluid's density
    - Describes Archimedes' displacement method for irregular volumes
  keywords: [density, mass, volume, float, sink, displacement, kg/m³, water, "1000"]
  modelAnswer: |
    Density is mass packed per unit volume: ρ = m/V, in kg/m³ (water: 1000 kg/m³, i.e.
    1 g/cm³). An object floats in a fluid less dense than itself sinks — cork (240) floats on
    water, iron (7870) sinks, yet an iron SHIP floats because its shape encloses air, making
    the average density of the hull-plus-air less than water's. Irregular volumes yield to
    displacement: submerge the object and the volume of fluid pushed aside equals the object's
    volume — Archimedes' bathtub insight. Density is a property of the material, not the
    amount: a chip and a block of the same steel share one density.
guidedSteps:
  - id: phy-app-m4-03-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A stone has mass 540 g and, by displacement, volume 200 cm³. Its density is ________ g/cm³.
    inputConfig:
      placeholder: "2.7"
    markingRule:
      matchMode: CONTAINS
      accepted: ["2.7"]
      rejectedFeedback: "ρ = m/V = 540 ÷ 200 = 2.7 g/cm³ (2700 kg/m³ — granite-ish). Denser than water's 1.0, so it sinks."
    hint: "Divide mass by volume."
    reflectionPrompt: "Will it sink or float in water — and in mercury (13.6 g/cm³)?"
  - id: phy-app-m4-03-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Solid iron (7870 kg/m³) sinks instantly, yet iron ships float. Why?
    inputConfig:
      options:
        - "Ship iron is specially lightened"
        - "The hull's shape encloses air, so the ship's AVERAGE density (iron + air) is below water's 1000 kg/m³"
        - "Paint seals out the water"
        - "Engines push the ship upward"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The hull's shape encloses air, so the ship's AVERAGE density (iron + air) is below water's 1000 kg/m³"]
      rejectedFeedback: "Floating is decided by AVERAGE density of the whole object — hull plus enclosed air. Flood the hull (replace air with water) and the average soars past 1000: the ship sinks, as ships do."
    hint: "What fills most of a hull's volume?"
    reflectionPrompt: "Explain a submarine's dive and surface in exactly these terms."
  - id: phy-app-m4-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe, step by step, how you would measure the density of an irregular brass ornament using kitchen scales, a measuring jug, and water. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [weigh, scales, submerge, displace, rise, volume, divide, jug]
      rejectedFeedback: "Weigh the ornament (mass m). Part-fill the jug, note the level; fully submerge the ornament and note the new level — the RISE is its volume V. Density = m ÷ V. Displacement converts awkward shape into honest volume."
    hint: "Mass from the scales; volume from how much the water level rises."
    reflectionPrompt: "What are the two biggest error sources in your kitchen version, and which Module One trick reduces them?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Density is best described as:"
    options:
      - "How heavy something is"
      - "Mass per unit volume — how tightly matter is packed"
      - "The same as weight"
      - "How large something is"
    correctIndex: 1
    feedback: "ρ = m/V: a material property, independent of sample size. A tonne of feathers and a tonne of lead share mass — not density."
  - type: MULTIPLE_CHOICE
    question: "An object floats in water when its average density is:"
    options:
      - "Greater than 1000 kg/m³"
      - "Less than 1000 kg/m³"
      - "Exactly zero"
      - "Irrelevant — only shape matters"
    correctIndex: 1
    feedback: "Below the fluid's density → float; above → sink; equal → hover. Shape matters only by enclosing air and changing the AVERAGE."
---

# Hook

Around 250 BC, King Hiero of Syracuse suspected his goldsmith of cutting the royal crown's gold with cheap silver — but the crown weighed exactly right, and melting it down to check was not an option. He handed the puzzle to Archimedes, who solved it, legend says, in the public baths: lowering himself in, watching the water rise, and realising — *the rise measures my volume*. He ran home through the streets shouting "Eureka!", possibly under-dressed.

The insight: weight alone says nothing about *what stuff is*. Weight **per volume** — density — is a fingerprint. Gold packs 19.3 grams into every cubic centimetre; silver only 10.5. Measure the crown's volume by the water it displaces, divide its mass by that, and the goldsmith's secret is out (it was — bad day for the goldsmith). Twenty-two centuries later, the same one-line formula floats ships, flies balloons, and layers the entire ocean and atmosphere.

# Lore Introduction

Calde sets the Foundry's morning riddle on the bench: two ingots, identical in size and sheen — one from the iron stocks, one a lightweight casting-alloy for the Observatory's instruments. "The eye cannot tell them apart, and the scales—" she weighs them, one then the other, "—*can*. But here is the apprentice-breaker." She produces a third piece: a crumpled, irregular lump of off-cut metal, no two edges alike. "The scales give me its mass in a heartbeat. But density is mass *per volume*, and what, pray, is the volume of *that*?" She drops it in your palm — jagged, unmeasurable by any ruler in the room. Then she nods, with a grin, at the great quenching-trough of still water beside the forge. "An old bath-house philosopher solved it without a single straight edge. Solve it the way he did, and the Foundry will trust your numbers forever."

# Core Learning

## Concept Introduction

**Density** is mass packed per unit volume:

```
ρ = m / V
(kg/m³) = (kg) ÷ (m³)        [equivalently g/cm³; 1 g/cm³ = 1000 kg/m³]
```

It is a **material's** property, not a sample's: a steel filing and a steel girder both run ~7,870 kg/m³. Anchor values worth owning: water **1,000**, air ~**1.2**, wood ~400–900, ice **917** (note: *less* than water — rare among solids, and why ice floats on ponds rather than choking them from below), aluminium 2,700, iron 7,870, gold 19,300 kg/m³.

**Floating and sinking** is a density comparison with the surrounding fluid:

- average density **<** fluid's → floats (riding high in proportion)
- **>** → sinks
- **=** → hovers, neutrally buoyant (the submariner's and the fish's art)

**Average** carries the cargo: an iron ship floats because hull + enclosed air averages below 1,000; holed and flooded, the average climbs and the sea reclassifies it. Hot-air balloons float in air by the same ledger — heating thins the air inside (fewer particles per m³), dropping the average density of balloon-plus-air below the cold air outside. (Density connects back to the particle model: same particles, spaced differently — which is also why most materials expand and *thin* when heated.)

**Volume by displacement** (Archimedes' move): submerge the object; it shoves aside exactly its own volume of fluid. Read the rise in a measuring jug — or catch the overflow — and the unmeasurable lump confesses. Mass from scales, volume from water, divide: identity revealed.

## Why It Matters

- Density is the first tool of material identification — metallurgy, gemology, archaeology, and quality control all begin with ρ.
- Buoyancy engineering floats everything from cargo fleets to weather balloons to your own swimming; ballast, lifejackets, and submarine trim are applied averages.
- Density layering organises nature at every scale: oil over vinegar, cold dense air sliding under warm (weather fronts), ocean currents driven by salt-and-temperature density, the Earth itself sorted iron-core-first.

## Worked Examples

**Example 1: The crown audit, with numbers**
A crown of mass 1,930 g displaces 115 cm³ of water. ρ = 1930/115 ≈ 16.8 g/cm³. Pure gold runs 19.3 — this crown is partly something lighter (silver's 10.5 fits the books for a roughly 70/30 blend). No melting, no cutting: a jug of water just audited a king's treasury.

**Example 2: Designing a one-tonne boat**
A flat-pack steel boat masses 1,000 kg. To float, it must displace 1,000 kg of water before the gunwales dip — that is, enclose at least 1 m³ below the waterline. Build it as a 2 m × 1 m × 0.6 m open box (1.2 m³): it floats riding ~0.5 m deep, freeboard to spare. Naval architecture is ρ = m/V solved for V, with margins.

**Example 3: The drinks-can diagnostic**
Drop an unopened regular cola and a diet cola into a sink: regular sinks, diet floats. Same can, same volume — but ~35 g of dissolved sugar nudges regular's average density just past water's, while the sweetener's few milligrams leave diet just under. Density resolves differences your hand cannot feel: a party trick that is also exactly how brewers and oil refiners monitor concentration daily (the hydrometer).

## Common Mistakes

- **"Heavy things sink"** — a 200-tonne ship floats; a 1 g pin sinks. Only density-versus-fluid decides; mass alone decides nothing.
- **Confusing density with weight or "thickness"** — syrup is dense AND viscous, but the two are separate properties (mercury: very dense, runs like water).
- **Forgetting the average** — enclosed air is part of the object; flooding, holes, and trapped bubbles re-run the calculation instantly.
- **Displacement with a floating object** — a floater displaces its *weight*, not its volume; for volume you must fully submerge it (push it under with a pin, subtract the pin).
- **Unit chaos** — g with m³, kg with cm³: convert first; 1 g/cm³ = 1000 kg/m³ is the bridge worth memorising.

## Mental Model

Picture every material as a **crowd in a ballroom of fixed size**. Density is the crowd-count per square metre: gold packs the floor shoulder-to-shoulder with heavyweight dancers; cork scatters a few light ones; air is a near-empty hall. Floating is two crowds meeting at a doorway: the sparser crowd gets lifted atop the denser one — always. And a ship is a brilliant fraud: a thin shell of heavyweight dancers enclosing a vast empty dance floor, so the *average* crowd is sparse enough to ride on water's shoulders. Hole the shell, let water's dancers flood the floor, and the fraud is over.

## Mini Summary

- ✔ ρ = m/V — a material fingerprint, independent of sample size (water: 1000 kg/m³)
- ✔ Float/sink/hover = average density below/above/equal to the fluid's
- ✔ Enclosed air is cargo: ships, balloons, lifejackets, and submarines run on averages
- ✔ Irregular volumes surrender to displacement — the rise is the volume
- ✔ Ice floats because water is densest as a liquid (at 4 °C) — ponds freeze top-down

# Guided Practice Quest

Work through the guided steps to fingerprint a stone at 2.7, float a thousand tonnes of iron honestly, and re-run the bath-house audit with a kitchen jug.

# Solo Practice Quest

Mount your own crown investigation: choose three small objects (a coin, a stone, a key — at least one irregular), and for each measure mass (kitchen scales) and volume (displacement in a measuring jug — pushing floaters fully under). Compute each density in g/cm³ with uncertainties (Module One: half the jug's smallest division matters here!). Then: (1) identify each material by comparing against a density table, stating your confidence; (2) predict float-or-sink in water AND in cooking oil (~0.92 g/cm³), and test where practical; (3) close with the Archimedes question — which of your measurements would expose a fake "silver" coin of zinc (7.1 vs silver's 10.5), and is your jug precise enough to do it? Show the calculation that answers that honestly.

# Integration

**Mathematics**: Density is your proportionality toolkit compounded: ρ = m/V is the gradient of a mass-against-volume graph (plot several samples of one material — straight line through the origin, slope = ρ), and float/sink is inequality comparison doing physical work.

**Engineering**: Materials selection tables open with density: aerospace hunts strength-per-kilogram (aluminium, titanium, composites), shipbuilders trade hull weight against cargo displacement, and civil engineers weigh concrete's cheapness against the foundations its 2,400 kg/m³ demands. Half of design is deciding where you can afford to be dense.

# Lore Conclusion

The crumpled off-cut goes into the quenching-trough on a thread; the water line climbs; your charcoal arithmetic runs on the slate — and you name the lump *aluminium-bronze, eight and a half thousand, give or take two hundred*. Calde checks it against the Foundry's stock-book and lets out a short, delighted laugh: dead on. "The bath-house philosopher would stand you a drink." She chalks your figure onto the stock shelf itself, then bangs the great copper kettle that hangs by the forge — it rings, and goes on faintly singing. "States, tolls, and crowdedness: matter's standing portrait, complete. But the Foundry's true trade is not matter standing still." She swings the kettle over the coals. "It is matter *getting hotter*. Tomorrow we ask the question every smith, cook, and stargazer must answer: what IS heat — and why does the thermometer never tell you the whole of it?"
