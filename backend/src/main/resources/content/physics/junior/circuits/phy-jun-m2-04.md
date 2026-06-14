---
id: phy-jun-m2-04
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m2
moduleTitle: "Module 2: Electricity and Magnetism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: circuits
topicTitle: "Circuits"
topicSortOrder: 2
title: "Resistance and Ohm's Law"
sortOrder: 4
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Define resistance and apply Ohm's law (V = IR)
  - Explain what determines a wire's resistance (length, thickness, material, temperature)
  - Interpret current-voltage graphs for ohmic and non-ohmic components
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Applies V = IR to find any of the three quantities
    - States how length, area, material, and temperature set resistance
    - Identifies ohmic behaviour (straight I–V line) versus filament-lamp curvature
  keywords: [resistance, ohm, V = IR, length, thickness, temperature, filament, graph]
  modelAnswer: |
    Resistance measures how much a component fights current: R = V/I, in ohms — one ohm passes
    one amp per volt of push. Ohm's law V = IR runs all circuit arithmetic: a 230 V supply
    through a 23 Ω element drives 10 A. A wire's resistance grows with length (longer gauntlet
    of collisions), shrinks with cross-sectional area (wider road), and depends on material
    (copper's sea flows freely) and temperature (hotter lattices jostle more). Ohmic components
    give straight I–V graphs through the origin; a filament lamp's curve bends because its own
    heating raises its resistance as current grows — the graph is the component's honest
    portrait.
guidedSteps:
  - id: phy-jun-m2-04-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A 12 V supply drives current through a 4 Ω resistor. Current: I = V/R = ________ A.
    inputConfig:
      placeholder: "3"
    markingRule:
      matchMode: CONTAINS
      accepted: ["3"]
      rejectedFeedback: "I = 12/4 = 3 A. Ohm's law rearranged — the balance rule's ten-thousandth appearance, and not its last."
    hint: "Current = voltage ÷ resistance."
    reflectionPrompt: "What current would the same supply drive through 12 Ω? Through 1 Ω — and why do short circuits melt things?"
  - id: phy-jun-m2-04-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which wire has the LOWEST resistance?
    inputConfig:
      options:
        - "Long and thin copper"
        - "Short and thick copper"
        - "Long and thin iron"
        - "Short and thin iron"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Short and thick copper"]
      rejectedFeedback: "Resistance grows with length, shrinks with thickness, and depends on material (copper ≪ iron). Short + thick + copper wins on all three counts — which is why jump leads and earth wires are fat copper."
    hint: "Shorter gauntlet, wider road, freer-flowing sea."
    reflectionPrompt: "Why are heating elements deliberately made the OPPOSITE way (long, thin, resistive alloy)?"
  - id: phy-jun-m2-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A filament lamp's I–V graph starts steep but bends flatter at higher voltages. Explain the bend using temperature and resistance — and why this makes the lamp 'non-ohmic'. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [hotter, temperature, resistance increases, lattice, vibrat, non-ohmic, curve]
      rejectedFeedback: "Higher voltage drives more current, which heats the filament white-hot; the hotter lattice vibrates harder and obstructs electrons more — resistance RISES with current. Each extra volt buys less extra current than the last: the line bends. Ohmic means constant R (straight line); the lamp's R changes with conditions, so: non-ohmic."
    hint: "What does the current do to the filament's temperature, and the temperature to R?"
    reflectionPrompt: "Why do filament bulbs usually blow at the moment of switch-on, when cold?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A component passes 0.5 A when 10 V is applied. Its resistance is:"
    options: ["5 Ω", "20 Ω", "0.05 Ω", "10.5 Ω"]
    correctIndex: 1
    feedback: "R = V/I = 10/0.5 = 20 Ω. Volts per amp — the definition itself."
  - type: MULTIPLE_CHOICE
    question: "An ohmic conductor's current–voltage graph is:"
    options:
      - "A curve that flattens"
      - "A straight line through the origin — constant gradient, constant R"
      - "A horizontal line"
      - "A circle"
    correctIndex: 1
    feedback: "Ohmic = R constant (at steady temperature): I ∝ V, straight through (0,0). Bends and kinks are components confessing that their resistance changes with conditions."
---

# Hook

Why doesn't your phone charger melt? It plugs into the same socket as a 2,000-watt kettle — the same 230 volts stands ready behind both. Yet the charger sips a trickle while the kettle gulps a torrent, from identical outlets. The socket clearly doesn't decide. *The appliance does* — and the deciding property is **resistance**: each device's built-in opposition to flow, the narrowness of its own electrical channel.

In 1827, Georg Ohm — then an obscure schoolteacher, his work dismissed as "a web of naked fancies" — measured the relationship that governs this: push, flow, and opposition, locked in the tidiest law in electricity: **V = IR**. Three letters that size every wire, rate every fuse, and design every heating element on Earth. His critics are forgotten; the unit of opposition bears his name. Today you learn to wield it — and to read the graphs where components confess whether they obey.

# Lore Introduction

Upstairs, the Tower's humming resolves into the Resistance Gallery: racks of wire spans stretched between brass terminals — copper and iron, long and short, hair-fine and finger-thick — each with its own little lamp and gauges. Hale walks the racks like a vintner among casks. "Same push on every rack — the Tower's standard twelve volts. Read me the flows." You walk the line: the short fat copper's lamp blazes (the gauge slams right); the long thin iron's lamp barely glows. "Every span fights the river differently," Hale says. "The fight has a number, the number has a law, and the law—" she taps a framed page, hand-copied, hanging at the gallery's end: a schoolteacher's graph, a perfectly straight line, "—was found by a man nobody believed, with apparatus poorer than this room's worst rack. Measure all twelve spans, junior. By the last one, you will have rediscovered him."

# Core Learning

## Concept Introduction

**Resistance — the fight, quantified.**

```
R = V / I        ohms (Ω) = volts per ampere
```

One ohm lets one volt drive one amp. Rearranged as **V = IR** or **I = V/R**, this is **Ohm's law** — the workhorse of all circuit arithmetic. Anchors: a metre of copper flex ~0.01 Ω; a filament lamp (hot) ~500 Ω; your dry skin ~100,000 Ω (wet: dramatically less — the bathroom lesson's arithmetic); a kettle element ~26 Ω, *deliberately*.

**What sets a wire's R** — picture electrons running a gauntlet of vibrating lattice ions:

| Factor | Effect | Why |
|--------|--------|-----|
| **Length** ↑ | R ↑ (proportionally) | Longer gauntlet, more collisions |
| **Cross-section** ↑ | R ↓ (inversely) | Wider road, more parallel paths |
| **Material** | Copper ≪ iron ≪ nichrome | How free the electron sea runs |
| **Temperature** ↑ | R ↑ (in metals) | Hotter ions jostle harder |

Hence the two opposite design briefs: *transmission* wants short/fat/copper (waste nothing); *heating elements* want long/thin/nichrome coils (waste *everything*, on purpose, as glow and heat — "waste" is the product).

**Ohmic and non-ohmic — the I–V portrait.** Plot current against voltage:

- **Ohmic** (metals at steady temperature, standard resistors): straight line through the origin — R constant, gradient = 1/R.
- **Filament lamp**: curve flattening — its own heating raises R as current grows; each volt buys less than the last.
- **Diode** (a preview): near-zero current one way until a threshold, then a flood; reversed, almost nothing — a one-way valve, profoundly non-ohmic, and the seed of all electronics.

The graph is the component's character reference: linearity is obedience; bends are temperature confessions; asymmetry is valvework.

## Why It Matters

- V = IR sizes everything: cable gauges for a house, fuse ratings, resistor choices in every gadget, and the safety arithmetic of skin and shock.
- The resistance factors explain infrastructure at a glance — fat aluminium grid cables, thin nichrome toaster coils, gold-plated contacts (thin layers, but corrosion-free).
- I–V characterisation is the universal lab skill of electronics: every datasheet's first page is this lesson's graph.

## Worked Examples

**Example 1: Why the charger doesn't melt**
Kettle: R ≈ 26 Ω → I = 230/26 ≈ 9 A → a torrent, by design (heat is the job). Phone charger input: effective R in the kilohms → tens of milliamps. Same shelf, same 230 V; the components' own resistance writes their bills. The socket is a standing offer, not a command.

**Example 2: The extension-lead trap**
A cheap 10 m lead of thin wire (total R ≈ 0.5 Ω) feeds a 9 A heater. The lead itself drops V = IR = 4.5 V — dissipating I²R ≈ 40 W *in the cable*, coiled in its reel like a slow toaster. Fat-core leads and "fully unwind before use" warnings are this calculation printed on a tag. (Same physics, grid scale: transmission losses — and the reason for high-voltage lines, a story two lessons ahead.)

**Example 3: Reading a filament's biography from its graph**
A lamp's data: at 2 V, 1.0 A (R = 2 Ω); at 12 V, 2.0 A (R = 6 Ω). The resistance *tripled* — the filament climbed from warm to ~2,500 °C white-hot, its lattice trembling proportionally. Cold R (room temperature): a mere 0.5 Ω — so switch-on slams ~24× the running current through it for milliseconds. That inrush is why filaments die at the flick, with a *tink*, after years of quiet evenings.

## Common Mistakes

- **Treating Ohm's law as universal** — it's a *behaviour* some components exhibit (constant R), not a law of nature; lamps, diodes, and thermistors disobey informatively.
- **"High resistance is bad"** — resistance is a design choice: transmission hates it, heaters are made of it, and without it every circuit would be a short.
- **Confusing the directions** — R = V/I defines resistance; V = IR predicts the push needed; I = V/R predicts the flow. One law, three grips.
- **Forgetting temperature** — quoting a filament's "resistance" without its state (cold? glowing?) is meaningless; R is conditions-dependent.
- **Ignoring the wire's own R** — leads and cables are components too; long thin runs silently tax the circuit (and warm the carpet).

## Mental Model

Every component is **a stretch of road, and resistance is its traffic-fighting character**. Copper flex is empty motorway — six lanes, no lights. A resistor is a calibrated contraflow: precisely so many cones per mile, by design. A heating element is a deliberate gravel track where the *friction itself* is being farmed — the glow of brake-pads-as-product. And Ohm's law is the traffic equation: flow = push ÷ obstruction, holding lane by lane. The I–V graph is each road's confession under inspection: straight-line roads keep their cones fixed whatever the traffic; the filament-lamp road *adds cones as it heats* under heavy flow; the diode is a one-way street with a stiff gate. Circuit design, from here on, is town planning.

## Mini Summary

- ✔ R = V/I (ohms); V = IR runs all circuit arithmetic
- ✔ R grows with length, shrinks with thickness; material and temperature set the rest
- ✔ Transmission wants low R (short, fat, copper); heaters farm high R (long, thin, nichrome)
- ✔ Ohmic = straight I–V through origin; filament curves (self-heating raises R); diodes valve
- ✔ Wires are components too — long thin leads quietly bill the circuit (I²R, foreshadowed)

# Guided Practice Quest

Work through the guided steps to drive three amps through four ohms, pick the easiest road in the gallery, and read a filament's temperature confession straight off its bending graph.

# Solo Practice Quest

Three gallery commissions: (1) *Ohm's table*: a 6 V battery feeds, in turn, components of 2 Ω, 10 Ω, 60 Ω, and 600 Ω — tabulate the currents, then invert the game: what R passes exactly 1 mA? (2) *The factor audit*: explain, using the four factors, the design of (a) jump leads, (b) a toaster element, (c) the gold plating on HDMI contacts, (d) a fuse wire (thin, low-melting alloy — what's the plan?). Two sentences each. (3) *Graph forensics*: sketch (or plot from invented-but-consistent data) the I–V portraits of a fixed resistor, a filament lamp, and a diode; annotate each curve's tell-tale feature with its physical cause. Close with the kettle-versus-charger paradox explained to a housemate in three sentences, V = IR doing the talking.

# Integration

**Mathematics**: Ohm's law is proportionality with a constant you can buy in a shop — and the I–V graph's gradient IS 1/R, your Module One graph-reading earning its keep. The resistance factors compose into R = ρL/A (resistivity ρ as the material's intrinsic fight), a formula one tier of algebra away and worth meeting early.

**Engineering**: Resistor selection, cable sizing tables, fuse coordination, and PCB trace widths are this lesson industrialised — and thermistors (R falls with temperature) plus strain gauges (R rises with stretch) turn the "nuisance factors" into *sensors*: your thermostat and your bathroom scales both measure the world by its effect on resistance.

# Lore Conclusion

Twelve spans measured, twelve resistances chalked, and your own graph — current against voltage for the gallery's standard resistor — runs straight as a chalk-line through the origin, gradient dutifully reporting 1/R. Hale holds your page beside the framed, hand-copied original at the gallery's end. The lines agree to the width of an old man's pen-stroke. "A schoolteacher with home-made wire, mocked by every learned society of his day," she says. "And every circuit since is his arithmetic." She snuffs the gallery's racks one by one until just two lamps remain lit — wired, you notice, quite differently: one pair strung in a single loop, the other side by side on parallel rungs. One pair glows dim; the other blazes. "Same battery, junior. Same lamps." Her eyebrow arches in the half-dark. "Tomorrow: WHY. The two great architectures of every circuit ever wired — and which one keeps your house alight when one bulb dies."
