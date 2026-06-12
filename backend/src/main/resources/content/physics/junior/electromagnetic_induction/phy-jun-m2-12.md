---
id: phy-jun-m2-12
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m2
moduleTitle: "Module 2: Electricity and Magnetism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: electromagnetic_induction
topicTitle: "Electromagnetic Induction"
topicSortOrder: 4
title: "Transformers and the Grid"
sortOrder: 12
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Explain transformer action via mutual induction on a shared core
  - Use the turns ratio Vs/Vp = Ns/Np
  - Explain why grids transmit at high voltage (minimising I²R losses)
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains operation — alternating primary current makes a changing core field that induces in the secondary
    - Applies the turns-ratio equation both ways (step-up and step-down)
    - Uses P = IV and P = I²R to justify high-voltage transmission
  keywords: [transformer, turns ratio, primary, secondary, step-up, step-down, I²R, grid]
  modelAnswer: |
    A transformer is two coils sharing an iron core: alternating current in the primary makes a
    continuously changing magnetic field in the core, which induces an alternating voltage in
    the secondary — induction with nothing moving. The voltages follow the turns:
    Vs/Vp = Ns/Np, so 100 primary turns at 230 V with 1000 secondary turns steps up to 2,300 V;
    power is (nearly) conserved, so current steps DOWN by the same factor. That trade runs the
    grid: stepping up to 400,000 V slashes transmission current, and cable heating I²R falls
    with the current's square — then local transformers step back down for safe use. DC won't
    transform (no change, no induction), which is why AC won the grid.
guidedSteps:
  - id: phy-jun-m2-12-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A transformer has 50 primary turns and 250 secondary turns. Fed with 230 V AC, its output is ________ V.
    inputConfig:
      placeholder: "1150"
    markingRule:
      matchMode: CONTAINS
      accepted: ["1150", "1,150"]
      rejectedFeedback: "Vs = Vp × Ns/Np = 230 × 250/50 = 1,150 V. Five times the turns, five times the voltage — a step-up transformer."
    hint: "Multiply the input voltage by the turns ratio."
    reflectionPrompt: "If this transformer is ideal (power conserved), what happens to the current from primary to secondary?"
  - id: phy-jun-m2-12-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      The national grid transmits at 400,000 V rather than 230 V because:
    inputConfig:
      options:
        - "Higher voltage travels faster"
        - "Delivering the same power at higher V needs far less current — and cable heating I²R falls with the SQUARE of that current"
        - "Pylons require high voltage to stay up"
        - "It deters birds"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Delivering the same power at higher V needs far less current — and cable heating I²R falls with the SQUARE of that current"]
      rejectedFeedback: "P = IV: same power, 1,700× the voltage → 1/1,700th the current → I²R losses cut by about 3 MILLION times. High-voltage transmission is the I² law exploited at national scale — the entire reason pylons hum with hundreds of kilovolts."
    hint: "P = IV fixes the I; then ask what I²R does with a tiny I."
    reflectionPrompt: "Why, then, doesn't the high voltage come straight into your house?"
  - id: phy-jun-m2-12-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A transformer fed with smooth DC outputs nothing (and may overheat). Explain why, using the conditions for induction. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [change, changing, steady, no induction, constant field, AC, alternat]
      rejectedFeedback: "Induction demands CHANGE. Steady DC makes a steady core field — and a steady field induces nothing in the secondary (Faraday's parked-magnet lesson). Worse, with no induced back-voltage the primary behaves as a bare low-resistance coil and gorges on current. Transformers are AC creatures by physical necessity — the deep reason the grid alternates."
    hint: "What did the parked magnet teach about steady fields?"
    reflectionPrompt: "What does this imply about why the 'War of the Currents' (AC vs DC) went the way it did?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An ideal transformer steps voltage UP five-fold. The current it delivers:"
    options:
      - "Also rises five-fold"
      - "Falls five-fold — power (IV) is conserved across the transformer"
      - "Is unchanged"
      - "Becomes DC"
    correctIndex: 1
    feedback: "No free power: Vp×Ip = Vs×Is (minus small losses). Voltage up, current down in proportion — exactly the trade the grid wants for its cables."
  - type: MULTIPLE_CHOICE
    question: "The clicking, humming boxes on poles and in substations are step-DOWN transformers. Their job is:"
    options:
      - "Generating local power"
      - "Reducing transmission voltage in stages to the safe domestic 230 V"
      - "Storing electricity overnight"
      - "Converting AC to DC"
    correctIndex: 1
    feedback: "The grid descends like a staircase: 400 kV → 132 kV → 33 kV → 11 kV → 230 V, a transformer at every step. The hum is the core's magnetisation flexing at grid frequency — the sound of induction working."
---

# Hook

Between the power station's spinning heart and your phone charger stands a device with **no moving parts at all** — two coils of wire and a block of iron — that performs the grid's most impossible-sounding trick: it takes electricity in at one voltage and hands it on at *any other voltage you choose*, with 99% of the power intact. Volts for amps, amps for volts, exchanged silently at any scale from a wrist-watch charger to a substation the size of a bus.

Without it, electric civilisation stalls forty miles from every power station: at generator voltages, the cables' I²R heating (you priced it in the Resistance Gallery) would cook the countryside. With it, power rides cross-country at 400,000 volts and a wisp of current, then steps down — 132 kV, 33 kV, 11 kV, 230 V — through a staircase of humming boxes to your kitchen. The transformer is Faraday's twitch one last time, frozen into iron — and it is the single reason your sockets alternate. Today, the module's keystone.

# Lore Introduction

For your final morning in the Tower, Hale takes you neither up nor down but *out* — along the ridge road to where the Academy's supply line marches in from the distant city on pylons, and into the small fenced yard the juniors call the humming garden: the Academy's substation. Great grey tanks squat in rows, singing one low note. "No wheels," Hale says, laying a hand on the warm steel. "No shafts, no brushes, nothing that turns. The Tower's founders would have called it the least interesting machine on the hill." She unlocks the demonstration case bolted by the gate — inside, a cut-away: two copper coils, wound on opposite sides of one iron ring. "And yet, junior: the river arrives from the city at one hundred and thirty-two thousand volts — and crosses THIS, and leaves at the Tower's own gentle two-thirty. No contact between the coils. Nothing moves. Tell me — with everything the Tower has taught you — *what crosses the ring?*"

# Core Learning

## Concept Introduction

**The transformer: induction, frozen.** Two coils share an iron core — **primary** (fed) and **secondary** (output), electrically isolated, magnetically married:

1. AC in the primary drives a **continuously changing** magnetic field
2. The iron core pipes that changing field through the secondary (iron's domains, conducting flux like copper conducts current)
3. The changing field through the secondary's turns **induces** an alternating voltage — method three from your first induction lesson: nothing moves; the *field* does the travelling

**The turns law.** Each turn of either coil intercepts the same changing core field, so voltage scales with turn count:

```
Vs / Vp = Ns / Np
```

More secondary turns: **step-up** (grid-side); fewer: **step-down** (your charger). And since the transformer creates no power (better: loses only ~1%), **P = IV is conserved across it**: voltage ×5 means current ÷5. Volts and amps trade at par.

**Why this runs the world: the I² escape.** A town drawing 100 MW at 230 V would need ~430,000 A — cables thick as tunnels, melting anyway (P_loss = I²R). The same 100 MW at 400 kV needs just 250 A: loss falls by (430,000/250)² ≈ **3 million times**. So the grid's shape: generate (~25 kV) → **step up** (400 kV, cross-country on thin cables) → **step down** in stages (132 → 33 → 11 kV → 230 V) at substations and pole-boxes — a voltage staircase descending into every street.

**Why the grid alternates.** Transformers demand *changing* fields — **DC won't transform** (steady field, zero induction, plus a gorging primary). This single fact decided the 1880s "War of the Currents": Edison's DC couldn't change voltage, so couldn't escape the I² tax beyond a mile; Tesla and Westinghouse's AC could. The sockets alternate because iron rings demand it. (Modern epilogue: power electronics now build "solid-state transformers" and HVDC links for special routes — but the staircase remains AC's kingdom.)

## Why It Matters

- The transformer completes the module's grand arc: charge → current → fields → motors → generators → *delivery* — you can now trace a joule from spinning shaft to phone screen with every step explained.
- Turns-ratio arithmetic is everywhere: chargers, doorbells, microwave ovens, audio gear, welding sets — every brick and wall-wart is this lesson in plastic.
- The I²R-driven grid architecture explains your landscape: why pylons, why substations, why the humming boxes — infrastructure as applied physics.

## Worked Examples

**Example 1: Inside your phone charger**
Mains 230 V must become 5 V: turns ratio 46:1 (e.g. 460:10). Output current can be 2 A while the primary sips 0.043 A — power balanced at ~10 W either side. (Modern chargers add electronic chopping at high frequency first — smaller cores transform faster-changing fields — but the turns law still rules the ratio. That's why chargers shrank and why they still faintly... hum.)

**Example 2: Auditing a transmission line**
50 km of cable, R = 5 Ω, delivering 50 MW. At 25 kV: I = 2,000 A → loss = I²R = 20 MW — *forty percent* gone as countryside heating. At 400 kV: I = 125 A → loss = 78 kW — 0.16%. One transformer at each end converts a 40% catastrophe into a rounding error. This single calculation IS the grid's existence.

**Example 3: The welding transformer — step-down weaponised**
A welder steps 230 V down to ~30 V — and current UP by the same factor: 200+ A through the work-piece, melting steel at the arc while the operator's supply-side draws a modest 30 A. Step-down isn't only about safety: it's the volts-for-amps trade run deliberately toward brute current. (Microwave ovens run the other way: step-up to ~2,000 V for the magnetron. Same iron, opposite errands.)

## Common Mistakes

- **"Transformers boost power"** — never: voltage and current trade at (nearly) constant power; claims otherwise are perpetual motion in a grey box.
- **Feeding DC and expecting output** — no change, no induction, no output, hot primary. AC-only by physics, not preference.
- **Inverting the ratio** — more secondary turns = more secondary volts; tie the V's and N's by subscript and the equation can't betray you.
- **Thinking high transmission voltage is for speed or strength** — it's purely the I²R escape: less current for the same power, quadratically less heating.
- **Forgetting the staircase** — 400 kV doesn't enter houses; transformation happens in stages, each humming box on your street one step of it.

## Mental Model

A transformer is **a gearbox for electricity, with magnetism as the gear teeth**. A gearbox trades torque for speed across meshed cogs at constant power; the transformer trades voltage for current across a shared magnetic flux at constant power — turns are the tooth-counts, and the ratio law is the same law. The grid, in this picture, is a drivetrain: the generator engine revs at its sweet spot; a tall "overdrive" gear (step-up) lets the power cruise the motorway turning barely any current; and a final cluster of reduction gears (the substation staircase) delivers gentle, usable rotation to every doorstep. And like any gearbox, it only engages while the shaft is *turning* — hold the input steady (DC) and the teeth simply stop meshing.

## Mini Summary

- ✔ Two coils, one iron core: changing primary field induces secondary voltage — induction, nothing moving
- ✔ Vs/Vp = Ns/Np; power conserved, so current trades inversely with voltage
- ✔ Grid logic: step up to slash current → I²R losses fall with the square → step down in stages for use
- ✔ DC won't transform (no change, no induction) — the deep reason mains is AC
- ✔ Every charger, doorbell, welder, and substation hum is this one device, re-wound

# Guided Practice Quest

Work through the guided steps to step 230 V up fivefold by turns alone, justify the pylons' 400,000 with one squared letter, and acquit the transformer of working for DC.

# Solo Practice Quest

Three final commissions from the humming garden: (1) *Charger census*: read the input/output plates of three adapters in your home; compute each turns ratio and each side's current at rated power; identify any that chop to high frequency first (the small, light ones). (2) *Design the line*: a village needs 2 MW over a 10 Ω line — compute losses at 11 kV and at 33 kV, choose, and specify the turns ratio of the village's step-down transformer to 230 V. (3) *Map the staircase*: walking or via maps, find your area's grid descent — pylons, substation, pole or pavement transformers — and sketch the voltage staircase from grid to your socket, labelling what you can. Close with the module's full chain, in one proud paragraph: shaft → induction → step-up → pylons → staircase → socket → device, with every concept from charge to Lenz making its cameo.

# Integration

**Engineering**: Transformer engineering hides deep craft: laminated cores (thin insulated sheets) to strangle eddy-current losses — Lenz's law turned saboteur inside the iron; oil cooling; tap-changers adjusting ratios live as demand shifts; and the grid's modern frontier — HVDC interconnects and solid-state conversion — built precisely where the classic iron ring's assumptions end.

**Mathematics**: The turns law is pure proportionality, and the transmission argument is a two-step composition: P = IV (inverse trade) into P = I²R (square punishment) — the same algebra of scaling that ran braking distances and kinetic energy, now sized at 400,000 volts. When one squared term explains pylons across a continent, you are seeing why physicists trust algebra over intuition.

# Lore Conclusion

You answer Hale's gate-question at last, beside the singing tanks: *what crosses the ring is change itself — the field's rise and collapse, fifty times a second, carrying power across an unbroken gap by induction alone.* She listens to the end, then unlocks the substation's log-cabinet and has you enter the day's readings yourself — line voltage, stepped voltage, load — your hand among a century of keepers'. "Module Two complete," she says, and stamps your card with the Tower's sigil: a needle, swinging. "Charge to current, current to field, field to force, motion to current, and the river delivered forty miles in a wisp. You came to me an apprentice of things that fall and spin, junior. You leave knowing why the lights are on." She walks you to the ridge gate, where the pylons march away toward the city, humming their one note. "Calde reclaims you next — she's been bragging about her new furnaces all term. Gas laws, engines, entropy: the physics of fire, properly grown up." Her grin flashes one last time. "Tell her the Tower sends its weather."
