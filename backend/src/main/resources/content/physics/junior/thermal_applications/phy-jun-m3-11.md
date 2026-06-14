---
id: phy-jun-m3-11
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m3
moduleTitle: "Module 3: Thermodynamics"
moduleGlyph: "♨️"
moduleSortOrder: 3
topicSlug: thermal_applications
topicTitle: "Thermal Applications"
topicSortOrder: 4
title: "Refrigerators and Heat Pumps"
sortOrder: 11
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Explain the vapour-compression cycle as a heat engine run in reverse
  - Compute coefficients of performance for fridges and heat pumps
  - Explain why heat pumps beat direct heating
integrationDomains: [engineering, chemistry]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Traces the cycle — evaporate inside (absorb heat), compress, condense outside (reject heat), expand
    - Computes COP = useful heat moved / work input
    - Explains why a heat pump's COP of 3 beats any 100%-efficient direct heater
  keywords: [refrigerant, evaporate, compress, condense, COP, heat pump, reverse]
  modelAnswer: |
    A refrigerator is a heat engine run backwards: work is spent to drag heat uphill from cold
    to hot. The vapour-compression cycle does it with a refrigerant chosen to boil at low
    temperature: it EVAPORATES inside the cold box, its latent heat stolen from the food;
    the compressor squeezes the vapour hot (work in); it CONDENSES at the warm rear coils,
    refunding the stolen latent heat plus the compressor's work into the kitchen; expansion
    through a valve chills it for the next lap. Performance is the coefficient of performance,
    COP = heat moved / work spent — typically 3–4: a heat pump moving 3 J of warmth per 1 J
    of electricity 'beats' a 100%-efficient resistance heater threefold, because moving heat
    is cheaper than making it.
guidedSteps:
  - id: phy-jun-m3-11-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Inside a refrigerator's cold compartment, the refrigerant absorbs heat from the food by:
    inputConfig:
      options:
        - "Conducting it to the compressor"
        - "EVAPORATING — boiling at low pressure and temperature, its latent heat demanded from the surroundings (the food)"
        - "Radiating cold"
        - "Freezing solid"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["EVAPORATING — boiling at low pressure and temperature, its latent heat demanded from the surroundings (the food)"]
      rejectedFeedback: "The Apprentice latent-heat toll, weaponised: the refrigerant is engineered to boil at, say, −25 °C, and boiling DEMANDS latent heat — extracted from everything nearby. Your spirits-soaked palm felt exactly this theft yesterday."
    hint: "Which state change ABSORBS energy, and from where must it come?"
    reflectionPrompt: "Why must the refrigerant's boiling point sit BELOW the compartment's target temperature?"
  - id: phy-jun-m3-11-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A heat pump delivers 9 kW of heat into a house while drawing 3 kW of electricity. Its coefficient of performance COP = ________.
    inputConfig:
      placeholder: "3"
    markingRule:
      matchMode: CONTAINS
      accepted: ["3"]
      rejectedFeedback: "COP = heat delivered / work input = 9/3 = 3. The 'extra' 6 kW was pumped uphill from the cold outdoors — moved, not made. The First Law is satisfied (3 in as work + 6 in from outside = 9 out); the Second is paid (work spent on the uphill pumping)."
    hint: "Useful heat out divided by electrical work in."
    reflectionPrompt: "Where exactly do the other 6 kW come from on a 2 °C day — and why doesn't this violate anything?"
  - id: phy-jun-m3-11-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Why does a heat pump (COP 3) heat a home three times more cheaply than an electric resistance heater, even though the resistance heater is '100% efficient'? (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [moves, pump, outdoor, ambient, 100%, three, convert, cheaper to move]
      rejectedFeedback: "The resistance heater CONVERTS: every joule of electricity becomes exactly one joule of heat — 100% is its ceiling. The heat pump MOVES: it spends each electrical joule dragging ~2 more joules of ambient heat uphill from outdoors, delivering 3 total. Moving heat is fundamentally cheaper than making it; '100% efficient' was never the top of the scale — it was the floor the heat pump steps over."
    hint: "Converting versus moving. Which job does each machine do with its electrical joule?"
    reflectionPrompt: "Why does a heat pump's COP fall as the outdoor temperature drops — and what does Carnot say about the limit?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A refrigerator's warm rear grille carries:"
    options:
      - "Only the compressor's waste"
      - "The heat removed from the food PLUS the compressor's work — both refunded to the kitchen at the condenser"
      - "Heat leaking in from the room"
      - "Nothing — it is decorative"
    correctIndex: 1
    feedback: "First Law at the boundary: everything extracted inside, plus everything the motor spent, exits at the grille. A fridge warms its kitchen, always — and a fridge with its door open warms it net."
  - type: MULTIPLE_CHOICE
    question: "Heat flowing cold→hot inside a heat pump does NOT violate the Second Law because:"
    options:
      - "The Second Law has exceptions for kitchens"
      - "Work is spent to drive it — the total entropy (cold side + hot side + power source) still rises"
      - "Refrigerants are exempt fluids"
      - "It actually flows hot to cold"
    correctIndex: 1
    feedback: "Unaided cold→hot is forbidden; PAID cold→hot is the law's explicit fee structure. The compressor's work (and its generation upstream) raises total entropy more than the uphill pumping lowers it. The Tribunal would stamp this machine LAWFUL."
---

# Hook

Your kitchen contains a machine that does the impossible-sounding thing every perpetual-motion fraud dreams of: it makes heat flow *from cold to hot* — out of near-frozen food into a warm room — and it has been doing it, quietly and lawfully, since before you were born. The trick is in the small print the frauds always omit: it *pays*. Every hour, its compressor spends work driving the heat uphill, and the Second Law — which forbids the free version absolutely — stamps the paid version approved.

And hiding in that same machine is the most under-appreciated number in home energy: run the cycle to pump heat *into* a house instead of out of a fridge, and each joule of electricity delivers *three or four joules* of warmth. Not 100% efficient — **300%**, honestly accounted. The heat pump is the rare technology where the physics sounds like the scam and isn't. Today: the cycle, the fee, and why "moving beats making" is rewiring the heating of nations.

# Lore Introduction

Calde has mounted a fridge's entire organs on a display board — compressor, two coils, a capillary valve, the loop charged with refrigerant — running live, frost feathering one coil while the other radiates warmth. "The engine run backwards," she says. "Yesterday's bridge, with the river driven uphill by paid labour." She presses your hand briefly to each coil in turn: the bite of the cold one, the glow of the warm. "Two latent-heat tolls, junior — one collected, one refunded — and a pump between them. You have known every part since the Apprentice tier: boiling steals heat; condensing repays it; compression heats; expansion chills. The founders had all four facts for centuries. The genius was plumbing them into a CIRCLE." She taps the compressor's hum. "Today: the circle, its price, and the arithmetic that is quietly replacing every boiler in the realm. And mind — when you can explain why this machine's '300 percent' is honest where yesterday's frauds were not, the module's teeth have done their work."

# Core Learning

## Concept Introduction

**The reverse scheme.** Run yesterday's diagram backwards — paying work to drag heat uphill:

```
COLD space (Tc) --Qc--> [PUMP, spending W] --Qh = Qc + W--> HOT space (Th)
```

- **First Law**: Qh = Qc + W (everything extracted plus everything spent exits at the hot side — the warm grille's full manifest)
- **Second Law**: unaided cold→hot is forbidden; *paid* cold→hot is lawful — W's expenditure (and its upstream generation) raises total entropy more than the uphill move lowers it.

**The vapour-compression cycle** — four stages, all old friends:

1. **Evaporator (inside, cold)**: low-pressure liquid refrigerant — engineered to boil at, say, −25 °C — **evaporates**, its latent-heat demand extracted from the food/room. (The spirits-on-skin bite, plumbed.)
2. **Compressor (the paid labour)**: squeezes the vapour — work in, temperature up (your fast-pump-stroke heating) — so it's now *hotter than the kitchen*.
3. **Condenser (outside, warm)**: the hot vapour **condenses**, refunding its latent heat (plus the compressor's work) into the room — heat flows hot→cold, lawfully, because compression made the refrigerant the hotter party.
4. **Expansion valve**: pressure drops abruptly; the liquid flash-cools (expansion cooling — the aerosol's chill) below the cold space's temperature, ready to steal again. The loop closes; the lap repeats.

**Performance: COP, not efficiency.** Moving machines are scored by **coefficient of performance**:

```
COP = useful heat moved / work input
```

Fridges: COP 2–4. **Heat pumps** (same cycle, aimed indoors): COP 3–4 in mild weather — *each electrical joule delivers 3–4 joules of warmth*, because 2–3 of them were lifted from the free outdoors. This lawfully exceeds "100%" because the machine *moves* rather than *makes*: resistance heating's 1:1 conversion was the floor, not the ceiling. Fine print: COP falls as the uphill climb steepens (colder outdoors, hotter delivery — Carnot's logic mirrored: COP_max = Th/(Th − Tc), generous for small lifts), which is why heat pumps love mild climates, underfloor heating's low delivery temperatures, and ground-source boreholes (stable ~10 °C all winter).

## Why It Matters

- Refrigeration is civilisation's cold chain — food, vaccines, data centres; air conditioning reshapes where humans live; together they draw ~10% of world electricity.
- Heat pumps are decarbonisation's workhorse: heating is the largest energy use in cold-country homes, and 300% beats every flame once the grid greens (yesterday's chain arithmetic, completed).
- COP-thinking — moving versus making — is the module's economic punchline: the Second Law charges for direction, and clever machines minimise the toll.

## Worked Examples

**Example 1: The fridge's full manifest**
A fridge extracts 60 W from its interior at COP 3 → compressor draws 20 W → grille emits 80 W into the kitchen, continuously. Door opened: warm air floods in, the cycle works harder, and the kitchen receives the *extra* extraction PLUS extra work — net warming. The "cool the kitchen with the fridge door" plan fails by exactly the First Law clause on the manifest.

**Example 2: Boiler versus heat pump, billed**
A house needs 12,000 kWh of heat per winter. Gas boiler (90%): 13,300 kWh of gas. Resistance heating: 12,000 kWh of electricity. Heat pump (seasonal COP 3.2): **3,750 kWh** of electricity. At typical tariffs the pump wins on cost despite electricity's price premium; on emissions, it wins on today's grids and routs as they green. One COP, one national policy.

**Example 3: Why your freezer defrosts itself with its own cycle**
Frost on the evaporator insulates it (trapped-air physics!), strangling extraction. Modern freezers periodically run brief reversed or heated cycles to melt it — spending a little to restore COP. The same reversal, scaled, is the heat pump's winter defrost — and the fully reversible machine is sold as "air conditioning + heating" in one: one loop, a four-way valve, both directions of the same paid river.

## Common Mistakes

- **"COP over 100% breaks conservation"** — the manifest balances: 1 in as work + 2 lifted from outdoors = 3 delivered. Moving ≠ making; the Tribunal approves.
- **Thinking fridges destroy heat** — they *relocate* it (plus their own work) to the grille; total kitchen heat rises, always.
- **Expecting constant COP** — it sags with the lift (cold snaps, hot delivery targets); seasonal averages, not brochure peaks, pay the bills.
- **"The refrigerant gets used up"** — the working fluid circulates sealed for the machine's life; leaks are faults (and, for older refrigerants, climate incidents — hence the chemistry of replacements).
- **Forgetting the upstream chain** — a heat pump's true merit multiplies its COP by the grid's efficiency; staple the Sankeys (the Bursar watches).

## Mental Model

A heat pump is **a water-bailiff with a bucket-chain, working a river that only flows downhill**. Heat's river runs hot→cold by law; the bailiff cannot repeal the law, but he may *carry* water uphill in buckets — provided he pays porters (the compressor's work) for every trip. The genius of his bucket: it's a latent-heat bucket — it *fills itself* by boiling at the cold riverbank (stealing the bank's warmth) and *empties itself* by condensing at the hilltop (refunding there), so each porter-joule escorts several stolen ones. The fridge is the bailiff draining a cellar; the heat pump is the same man filling a hilltop cistern; and his wage bill per bucket grows with the height of the hill — Carnot, auditing even the porters.

## Mini Summary

- ✔ Reverse the engine: spend W to move Qc uphill; Qh = Qc + W exits at the hot side
- ✔ The cycle: evaporate (steal, inside) → compress (pay, heat) → condense (refund, outside) → expand (chill, repeat)
- ✔ COP = heat moved / work spent — 3–4 typical; moving beats making
- ✔ COP sags with the lift (cold outdoors, hot delivery); design shrinks the hill (underfloor, ground-source)
- ✔ Lawful where frauds weren't: the books balance and the ratchet still clicks forward

# Guided Practice Quest

Work through the guided steps to let the refrigerant steal by boiling, score a 9-for-3 heat pump honestly, and retire '100% efficient' as the floor it always was.

# Solo Practice Quest

Three commissions with the bucket-chain: (1) *Map your fridge*: locate its evaporator (inside/freezer plates) and condenser (rear grille or skin), feel both (carefully), and write the four-stage story around your actual machine, including where the expansion device hides. (2) *The winter ledger*: for a home needing 10,000 kWh of heat, compute the purchased energy for a 90% boiler, resistance heating, and heat pumps at COP 4.0 (mild), 2.5 (cold snap); add your local tariffs and crown a winner with one caveat. (3) *COP versus lift*: using COP_max = Th/(Th − Tc), tabulate the ideal ceiling for delivering 35 °C (308 K) from outdoor air at +10, 0, −10 °C — then write two sentences on why underfloor heating (low Th) and boreholes (stable Tc) are COP strategy, not luxury. Close with the door-open fridge paradox, settled in one manifest line.

# Integration

**Engineering**: Refrigeration engineering tunes every stage: refrigerant chemistry (boiling points by design; ozone and climate constraints retiring old fluids — CFCs to HFCs to propane and CO₂), variable-speed compressors, and cascade systems reaching −80 °C for vaccines. District-scale heat pumps now mine rivers and sewage for city heat — the bailiff industrialised.

**Chemistry**: The refrigerant is applied phase-change chemistry: molecular design sets boiling point, latent heat, and stability. The CFC story — brilliant fluid, ozone catastrophe, Montreal Protocol, replacement generations — is science policy's greatest success and a standing lesson: the cycle is physics, but the working fluid is chemistry with consequences.

# Lore Conclusion

You close the day by writing the display-board fridge's full manifest — sixty stolen, twenty paid, eighty refunded at the grille — and beside it the hilltop version: the heat-pump ledger that delivers three for one, lawful to both regulators. Calde reads it, then does something she has never done: laughs aloud with pure pleasure. "Three for one, honestly booked! If the Tribunal's gallery could see it." She pins your manifest beside the machine. "The Foundry's thermal trade is nearly taught, junior. Engines downhill, pumps uphill — one trade remains, and it is the humblest: not moving heat, not converting it, but simply *refusing it passage*." She nods at the vault's ancient door, at the Foundry's thick walls, at your own coat on its hook. "Tomorrow: insulation — the architecture of slowness — and the quiet arithmetic by which a wall, a flask, or a duvet outwits the river without spending a single joule. Then the module sits its examination."
