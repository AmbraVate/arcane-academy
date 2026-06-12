---
id: phy-jun-m3-01
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m3
moduleTitle: "Module 3: Thermodynamics"
moduleGlyph: "♨️"
moduleSortOrder: 3
topicSlug: gas_laws
topicTitle: "Gas Laws"
topicSortOrder: 1
title: "Pressure, Volume, and Temperature: The Gas Triangle"
sortOrder: 1
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Describe gas state via the three linked variables P, V, and T
  - Explain each pairwise relationship from the particle model
  - Use kelvin temperatures in all gas reasoning
integrationDomains: [chemistry, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Names the three state variables and their units (Pa, m³, K)
    - Explains each pairwise link via particle bombardment
    - Converts correctly to kelvin and explains why Celsius fails for gas laws
    - Predicts qualitative outcomes for everyday gas scenarios
  keywords: [pressure, volume, temperature, kelvin, particle, bombardment, state]
  modelAnswer: |
    A fixed mass of gas is described by three linked variables: pressure (Pa), volume (m³),
    and temperature (K). The particle model links each pair: squeeze the volume and particles
    strike the walls more often (P up); heat at fixed volume and they strike harder and more
    often (P up); heat at fixed pressure and the gas must expand to keep the drumming constant
    (V up). All gas reasoning runs on the kelvin scale, because only kelvin is proportional to
    average particle energy — doubling 20 °C does not double anything physical, but doubling
    293 K does. Tyres firm up on motorways, aerosols warn against fire, and balloons shrivel
    in freezers: the triangle, everywhere.
guidedSteps:
  - id: phy-jun-m3-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A sealed, rigid flask of gas is heated. Using the particle model, its pressure rises because the particles:
    inputConfig:
      options:
        - "Expand"
        - "Strike the walls harder AND more often"
        - "Multiply"
        - "Stick to the walls"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Strike the walls harder AND more often"]
      rejectedFeedback: "Heating raises average particle speed: each wall-strike delivers more push, and strikes come more frequently. Same particles, same volume — hotter drumming. (Apprentice particle theory, now quantitative.)"
    hint: "Temperature is particle speed. What does speed do to the drumming?"
    reflectionPrompt: "Why does the flask need to be rigid for this question to be clean?"
  - id: phy-jun-m3-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Gas-law calculations demand kelvin. 27 °C = ________ K.
    inputConfig:
      placeholder: "300"
    markingRule:
      matchMode: CONTAINS
      accepted: ["300"]
      rejectedFeedback: "K = °C + 273: 27 + 273 = 300 K. Kelvin's zero sits at motion's floor, making temperature truly proportional to particle energy — Celsius's arbitrary zero wrecks every ratio."
    hint: "Add 273."
    reflectionPrompt: "What goes wrong if you compute 'doubling 27 °C' in Celsius?"
  - id: phy-jun-m3-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A balloon is taken from a warm room (300 K) into winter air (270 K). Predict what happens and explain with the particle model — pressure inside, outside, and the balloon's response. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [shrink, slower, less, drumming, contract, outside wins, smaller]
      rejectedFeedback: "Cooling slows the inside particles: their drumming weakens, so the steady outside bombardment momentarily wins and squeezes the balloon smaller. Shrinking crowds the inside particles until their strike-rate rebalances the pressure at a smaller volume. The balloon visibly deflates — count on roughly a 10% volume loss for this 10% kelvin drop."
    hint: "Whose drumming weakened? Who wins until balance returns?"
    reflectionPrompt: "Why does the proportional reasoning need kelvin (270/300), not Celsius (−3/27)?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The three state variables of a fixed mass of gas are:"
    options:
      - "Mass, weight, density"
      - "Pressure, volume, temperature"
      - "Speed, energy, momentum"
      - "Colour, smell, taste"
    correctIndex: 1
    feedback: "P, V, T — three dials, one gas, all linked through the particle model's drumming. Fix one, and the other two negotiate."
  - type: MULTIPLE_CHOICE
    question: "Why do gas laws fail with Celsius temperatures?"
    options:
      - "Celsius degrees are too small"
      - "Celsius's zero is arbitrary (ice's melting point), so Celsius values aren't proportional to particle energy — ratios come out meaningless"
      - "Celsius only works for liquids"
      - "They don't — either scale works"
    correctIndex: 1
    feedback: "Gas laws are proportionality statements, and only kelvin (zeroed at motion's floor) makes temperature proportional to energy. 0 °C is not 'no temperature' — but 0 K is."
---

# Hook

Check your car's tyre pressure after a long motorway run and it reads higher than when you left — though not a wisp of air was added. Leave a sealed water bottle in a car overnight in winter and find it crumpled by morning — though nothing touched it. Watch a hot-air balloon swell taut as its burner roars — though the envelope's mouth hangs open the whole time.

Three mysteries, one machine: a trapped gas is a system with exactly **three dials** — pressure, volume, temperature — and the dials are *linked*. Turn any one and the others must answer, following rules the particle model wrote for you back in the Apprentice tier and which this module now makes quantitative. The Foundry calls it the gas triangle, and learning to read it is the price of admission to thermodynamics: the physics of fire, engines, and the deep one-way arrow of the universe.

# Lore Introduction

Calde welcomes you back to the Foundry with a crushing handshake and visible pride: the lower vault has been rebuilt since your apprentice days, and at its heart stands new apparatus — a gleaming brass cylinder with a sliding piston, jacketed for heating and cooling, fitted with the Tower's own gauges. "The Tower teaches rivers," she says, with the old rivalry warm in her voice. "The Foundry teaches *breath*." She seals a charge of air into the cylinder. "One trapped breath of air, junior. Three things I can do to it: squeeze it—" she leans on the piston; the pressure gauge climbs, "—heat it—" she opens the jacket's valve; gauge climbs again, the piston straining, "—or let it stretch." She releases the piston; it glides out as the gas swells. "Three dials. One breath. The old smiths knew the links by burnt thumbs; you will know them by law. And mind—" she taps the thermometer, which reads in a scale starting at 273 below zero, "—in this vault, we count temperature from the TRUE floor. Celsius is for kitchens."

# Core Learning

## Concept Introduction

**The three dials.** A fixed mass of trapped gas is fully described by:

| Variable | Symbol | SI unit | Particle meaning |
|----------|--------|---------|------------------|
| Pressure | P | pascal (Pa) | Wall-drumming rate × strength |
| Volume | V | m³ | Room available between strikes |
| Temperature | T | **kelvin (K)** | Average particle kinetic energy |

**The pairwise links** — each one pure particle bookkeeping:

- **Squeeze (V↓ at fixed T)** → same-speed particles cross a smaller box more often → strike rate up → **P↑**. (Bike pump's resistance.)
- **Heat at fixed V** → faster particles strike harder *and* more often → **P↑**. (The aerosol can's "do not incinerate"; tyres warm on motorways.)
- **Heat at fixed P** → to keep the drumming matched to the constant outside pressure, the gas must **expand**: **V↑**. (Hot-air balloons; bread rising; the shrivelling winter balloon in reverse.)

**Kelvin or nothing.** Gas laws are *proportionality* laws, and proportionality needs a true zero. Kelvin (K = °C + 273) zeroes at motion's floor, so T in kelvin IS proportional to average particle energy: 600 K is honestly "twice 300 K". Celsius ratios are numerology — "doubling 20 °C" doubles nothing physical. Convert first, always, before any gas arithmetic.

(Next lesson the pairwise links get their famous names — Boyle, Charles — and their equations; today is for owning the machine they describe.)

## Why It Matters

- The triangle governs every pressurised thing in your life: tyres, aerosols, lungs, weather systems, espresso machines, and the cylinders of every engine.
- It's the bridge between the Apprentice particle model and real thermodynamics — engines and entropy (this module's destination) are the triangle, worked hard.
- Kelvin discipline here prevents the most common quantitative errors in the entire subject.

## Worked Examples

**Example 1: The motorway tyre, audited**
Tyre at 290 K reads 220 kPa (above atmospheric: total ~320 kPa). An hour's driving warms it to 320 K — fixed volume, so pressure scales with T: 320 kPa × 320/290 ≈ 353 kPa. The gauge rises ~30 kPa "by itself". Manufacturers specify *cold* inflation pressures for exactly this reason — and you now check yours before driving, like a Foundry junior.

**Example 2: The crumpled bottle, forgiven**
A bottle sealed warm (300 K) cools overnight to 270 K. Volume wants to fall in proportion (to 90%) — and the flimsy bottle, unable to hold a pressure difference, simply caves until the inside drumming rebalances. A rigid flask in the same night would instead hold volume and *drop pressure* by 10%. Same triangle, different dial pinned — the container decides which variable yields.

**Example 3: The diver's golden rule**
A diver at 10 m depth (2 atmospheres total) fills her lungs from the tank, then — the cardinal sin — holds her breath while ascending. At the surface (1 atm), the trapped breath wants *double* the volume: lungs cannot stretch twofold. Divers are drilled to breathe out continuously while ascending; the gas triangle is written into certification law because it has been written, historically, into autopsies.

## Common Mistakes

- **Celsius in calculations** — the cardinal arithmetic sin of the module; convert to kelvin before any ratio.
- **Letting two dials drift at once** — clean reasoning pins one variable (rigid container pins V; free piston pins P); always name what's held fixed.
- **"The gas particles expand/heat up in size"** — particles are unchanged; speed and spacing carry all the physics (Apprentice lesson, still load-bearing).
- **Forgetting gauge vs absolute pressure** — tyre gauges read pressure *above* atmospheric; the physics uses total (absolute). Add ~100 kPa when the triangle demands honesty.
- **Treating the links as mysterious** — every one is the drumming model from the Apprentice tier; if confused, return to particles striking walls.

## Mental Model

A trapped gas is **a crowd of perfectly tireless handball players sealed in a room, eternally rebounding off every wall**. *Pressure* is how hard the walls are being collectively pummelled. *Volume* is the room's size. *Temperature* is how energetically the players are running. Now the links read themselves: shrink the room and the same players hit walls more often (P↑). Feed the players (heat) and they pummel harder (P↑) — unless one wall is a sliding partition, in which case the strengthened pummelling shoves it outward until the room is big enough for balance (V↑). And the kelvin rule is just honesty about the players: a scale that claims "zero" while they're still jogging (Celsius) cannot price their energy in ratios.

## Mini Summary

- ✔ Three dials: P (Pa), V (m³), T (K) — one trapped gas, all linked
- ✔ Squeeze → more frequent strikes (P↑); heat at fixed V → harder, more frequent strikes (P↑); heat at fixed P → expansion (V↑)
- ✔ Kelvin only: K = °C + 273; gas laws are proportionalities and need the true zero
- ✔ The container decides which dial yields: rigid pins V, free pistons pin P, flimsy bottles cave
- ✔ Gauge pressure + ~100 kPa = absolute; the physics uses absolute

# Guided Practice Quest

Work through the guided steps to heat a rigid flask's drumming, convert to the only honest temperature scale, and shrivel a winter balloon in proportional style.

# Solo Practice Quest

Three triangle exhibits (gather, observe, explain): (1) *The crumple*: seal an empty plastic bottle in a warm room, refrigerate it an hour; measure or estimate the volume change, compute the kelvin ratio, and compare. (2) *The pump*: block a bicycle pump's outlet and compress slowly — describe what your thumb feels (P at shrinking V) and what the barrel feels after ten strokes (where did that warmth come from? — flag it as a question for the energy lesson). (3) *Tyres or aerosol*: check a real tyre cold vs warm (or read an aerosol's warnings) and write the triangle's account of the difference, in kelvin. For each exhibit: name which dial was pinned, which turned, and which answered. Close with the diver's rule explained to a beginner in two sentences.

# Integration

**Chemistry**: The triangle plus particle counting becomes the ideal gas equation PV = nRT — chemistry's workhorse for reacting gases, molar volumes, and stoichiometry. Every chemistry lab's gas syringe experiment is this lesson with moles attached.

**Mathematics**: Three linked variables with one pinned is your introduction to multivariable thinking — and each pinned-variable relationship is a clean proportionality (direct or inverse) from your Module One toolkit. The triangle is also a first taste of state space: the gas's condition as a point moving on a P–V–T surface.

# Lore Conclusion

By the session's end you have worked the brass cylinder through all three manoeuvres and written each as a particle story in the vault's new ledger — and Calde, reading them, grants the Foundry's highest opening-day praise: "Not one Celsius in the arithmetic." She racks the piston and pats the cylinder's flank. "The triangle is the grammar, junior. Tomorrow, the famous sentences." From the vault's shelf she takes two battered notebooks — facsimiles, but lovingly made: one marked *Boyle, 1662*, the other *Charles, 1787*. "An Irish alchemist who squeezed air with mercury, and a French balloonist who heated it to fly. Two pinned dials, two perfect laws, two names every engineer still mutters. Tomorrow you re-run both their notebooks — and the triangle starts doing arithmetic."
