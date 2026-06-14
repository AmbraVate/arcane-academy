---
id: phy-jun-m3-02
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m3
moduleTitle: "Module 3: Thermodynamics"
moduleGlyph: "♨️"
moduleSortOrder: 3
topicSlug: gas_laws
topicTitle: "Gas Laws"
topicSortOrder: 1
title: "Boyle's Law and Charles's Law"
sortOrder: 2
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Apply Boyle's law (PV = constant at fixed T)
  - Apply Charles's law (V/T = constant at fixed P)
  - Choose the correct law from a scenario's pinned variable
integrationDomains: [mathematics, biology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Applies P₁V₁ = P₂V₂ correctly
    - Applies V₁/T₁ = V₂/T₂ with kelvin temperatures
    - Identifies which variable a scenario pins and selects the matching law
  keywords: [Boyle, Charles, PV, V/T, constant, inverse, proportional, kelvin]
  modelAnswer: |
    Boyle's law: at constant temperature, pressure and volume trade inversely — PV = constant,
    so halving a gas's volume doubles its pressure (P₁V₁ = P₂V₂). Charles's law: at constant
    pressure, volume is proportional to kelvin temperature — V/T = constant, so warming a
    free-to-expand gas from 300 K to 600 K doubles its volume (V₁/T₁ = V₂/T₂). Choosing the
    law is reading the scenario: a sealed syringe squeezed slowly is Boyle's (T pinned by slow
    exchange); a balloon over a radiator is Charles's (P pinned by the open atmosphere). A
    rising bubble doubles in volume from 10 m depth; a hot-air balloon flies on Charles.
guidedSteps:
  - id: phy-jun-m3-02-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      Boyle's law: a syringe holds 60 cm³ of air at 100 kPa. Sealed and squeezed (at steady temperature) to 20 cm³, its pressure becomes ________ kPa.
    inputConfig:
      placeholder: "300"
    markingRule:
      matchMode: CONTAINS
      accepted: ["300"]
      rejectedFeedback: "P₁V₁ = P₂V₂: 100 × 60 = P₂ × 20 → P₂ = 300 kPa. A third of the room, three times the drumming."
    hint: "The PRODUCT of P and V stays fixed."
    reflectionPrompt: "What does your thumb feel as you approach 20 cm³, and why does it get rapidly worse?"
  - id: phy-jun-m3-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Charles's law: a balloon holds 2.0 L at 300 K. Warmed at constant pressure to 360 K, its volume becomes ________ L.
    inputConfig:
      placeholder: "2.4"
    markingRule:
      matchMode: CONTAINS
      accepted: ["2.4"]
      rejectedFeedback: "V₁/T₁ = V₂/T₂: 2.0/300 = V₂/360 → V₂ = 2.4 L. Volume rides kelvin temperature in direct proportion — 20% hotter, 20% bigger."
    hint: "Volume scales with the kelvin ratio: 360/300."
    reflectionPrompt: "What would the same warming do inside a RIGID flask instead — and which dial answers there?"
  - id: phy-jun-m3-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A scuba bubble released at 10 m depth (200 kPa absolute) rises to the surface (100 kPa) with temperature roughly constant. What happens to its volume, by which law, and why does this make 'never hold your breath while ascending' the diver's first commandment? (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [Boyle, double, halve, pressure, volume, lungs, expand]
      rejectedFeedback: "Boyle's law (T pinned): pressure halves from 200 to 100 kPa, so the bubble's volume DOUBLES on the way up. Lungs full of 10-metre air would likewise try to double at the surface — rupturing tissue. Divers exhale continuously on ascent so the expanding gas escapes; the rule is Boyle's law with a mortality table."
    hint: "Which dial is pinned? What does halving P do to V?"
    reflectionPrompt: "Why is the LAST 10 m of ascent the most dangerous stretch, proportionally?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Boyle's law describes a gas at constant:"
    options: ["Pressure", "Temperature", "Volume", "Mass only"]
    correctIndex: 1
    feedback: "Boyle pins T: then P and V trade inversely (PV = constant). Charles pins P: then V rides T. Name the pinned dial first, always."
  - type: MULTIPLE_CHOICE
    question: "A graph of V against T (kelvin) for a gas at constant pressure is:"
    options:
      - "A curve through the origin"
      - "A straight line through the origin — direct proportionality"
      - "A horizontal line"
      - "An inverse curve"
    correctIndex: 1
    feedback: "Charles's law: V ∝ T(K) — straight through (0,0). Extrapolating real gas data backward to V = 0 is historically how −273 °C announced itself as nature's floor."
---

# Hook

Robert Boyle squeezed air with a column of mercury in 1662 — before Newton's laws, before the word "physics" meant what it means — and found a relationship so clean it shocked him: halve the space, *exactly* double the pressure. Squash it to a third, exactly triple. The product P × V refused to budge. A century later, Jacques Charles — balloonist, showman, the second human ever to fly hydrogen — found the partner law: warm a gas that's free to expand and its volume climbs in *perfect proportion* to temperature... provided you measure temperature from a mysterious floor near −273 °C, a number his data pointed to like a compass needle, eighty years before anyone understood why.

Two pinned dials, two laws, three centuries of consequences: every breath a diver takes, every hot-air balloon, every engine stroke runs on this pair. Today, the triangle does arithmetic.

# Lore Introduction

Calde lays the two facsimile notebooks on the vault bench beside the brass cylinder, opened to their famous pages: Boyle's columns of mercury heights and trapped-air lengths; Charles's volumes against degrees, with his pencil line racing down toward an impossible cold. "Two pinned dials, two perfect notebooks," she says. "And the Foundry's rule is that no junior quotes a law they haven't re-run." She fits the cylinder for the first experiment — water jacket steady (temperature pinned), piston free for your hand, gauges polished. "Boyle this morning: squeeze, read, record, eight points, find what refuses to change. Charles after lunch: jacket heating, piston floating at fixed weight, volume against MY thermometer's honest kelvins." She taps Charles's racing pencil line where it dives toward the page's edge. "And when your own line points at the same impossible floor his did — and it will — you may stand where every junior stands, and feel the draught off absolute zero."

# Core Learning

## Concept Introduction

**Boyle's law** — temperature pinned (slow changes, heat exchanged freely):

```
P × V = constant        →        P₁V₁ = P₂V₂
```

Pressure and volume trade *inversely*: halve V, double P. Particle reading: same-speed players, smaller room, proportionally more wall-strikes. Graph: P against V is the inverse curve; P against 1/V is a straight line through the origin (the linearising trick from your graph lessons — Boyle's own data, replotted, passes the test).

**Charles's law** — pressure pinned (gas free to expand against a constant load, e.g. the atmosphere):

```
V / T = constant        →        V₁/T₁ = V₂/T₂        (T in kelvin!)
```

Volume rides kelvin temperature in *direct* proportion: 300 K → 360 K is +20% volume. Particle reading: faster players would drum harder, so the floating piston must retreat until the bigger room dilutes the drumming back to match the constant outside load. Graph: V against T(K), straight through the origin — and extrapolating real data to V = 0 is how **−273 °C announced itself**: the temperature where (ideally) all the drumming would stop. Absolute zero was discovered as an *intercept* before it was understood as physics.

**Choosing the law = naming the pinned dial:**

| Scenario | Pinned | Law |
|----------|--------|-----|
| Sealed syringe, squeezed slowly | T (heat leaks keep it steady) | Boyle |
| Balloon over a radiator | P (open atmosphere) | Charles |
| Bubble rising from depth | T (water bath) | Boyle |
| Rigid flask heated | V | (pressure law — P/T constant; the triangle's third side, same method) |

(All three pairwise laws unify next lesson into one combined statement — and, with particle counting, into chemistry's PV = nRT.)

## Why It Matters

- Boyle's law is breathing (diaphragm drops → lungs' volume up → pressure below atmospheric → air flows in), diving tables, syringes, and pneumatics.
- Charles's law is hot-air ballooning, bread ovens, and engine intake design — and its extrapolated intercept handed physics absolute zero.
- "Name the pinned dial" is the discipline that turns gas problems from guesswork into two-line arithmetic.

## Worked Examples

**Example 1: Re-running Boyle on the bench**
Bench data: (100 kPa, 60 cm³), (150, 40), (200, 30), (300, 20). Products: 6,000 every time — the constant, found. Prediction for 24 cm³: P = 6,000/24 = 250 kPa; the gauge agrees within its needle's width. One column of products beats any amount of staring at curves: the law IS the constancy.

**Example 2: Sizing a hot-air balloon's lift (Charles in the field)**
Envelope volume 2,800 m³ at ambient 280 K. Burn to 380 K at constant (atmospheric) pressure: the heated air expands by 380/280 — but the envelope is full, so the excess *spills out the open mouth*: mass inside drops by the inverse ratio, 280/380 ≈ 0.74. Air density falls from 1.26 to ~0.93 kg/m³: lift = (1.26 − 0.93) × 2,800 ≈ **900 kg** — pilot, basket, and champagne. Charles's law, literally carrying people.

**Example 3: The diver's last ten metres**
Bubble at 30 m (400 kPa): rising to 20 m (300 kPa) grows by 4/3. From 10 m (200 kPa) to surface (100 kPa) it *doubles* — the single largest proportional jump of the whole ascent in the final, most innocent-looking stretch. Hence the commandment's emphasis: exhale especially near the surface. Boyle's inverse curve is steepest where the pressure is lowest.

## Common Mistakes

- **Celsius relapse** — Charles's ratios in °C are nonsense (the 2.0 L balloon "at 27° warmed to 54°" is NOT doubled-volume — convert: 300 K → 327 K, a mere 9%).
- **Using Boyle when pressure is pinned (or vice versa)** — read the scenario's container first; the pinned dial chooses the law.
- **Forgetting absolute pressure** — Boyle needs total pressure: a "200 kPa" tyre gauge reading is ~300 kPa absolute.
- **Expecting the laws to survive fast, violent changes** — rapid compression heats the gas (your bike pump's warm barrel — tomorrow's energy lesson); Boyle assumes slow, temperature-steady squeezing.
- **Real-gas worship** — these are *ideal* gas laws: superb for air at everyday conditions, bending near liquefaction. Know the tool's range.

## Mental Model

Think of the two laws as **the two honest merchants of the gas triangle**. Boyle runs the *space-for-pressure exchange*: his scales balance P against V so that their product never varies — pay him half your room, he pays you double your pressure, to the pascal. Charles runs the *warmth-for-room exchange*: his ledger rules a straight line from the true zero — every percent of kelvin warmth buys exactly a percent of volume. Each merchant trades only when his condition holds (Boyle in steady-temperature markets, Charles under open skies of constant pressure) — and the first question at any gas market is always the same: *which merchant's stall are we standing in?*

## Mini Summary

- ✔ Boyle (T pinned): PV = constant — inverse trade; P₁V₁ = P₂V₂
- ✔ Charles (P pinned): V/T = constant — direct proportion in kelvin; V₁/T₁ = V₂/T₂
- ✔ Name the pinned dial first; the scenario's container tells you
- ✔ Charles's extrapolation to V = 0 found absolute zero as an intercept
- ✔ Absolute pressures, kelvin temperatures, slow changes — the laws' honest operating range

# Guided Practice Quest

Work through the guided steps to keep Boyle's product constant under your thumb, grow a balloon by a kelvin ratio, and write the diver's first commandment in Boyle's own arithmetic.

# Solo Practice Quest

Re-run both notebooks: (1) *Boyle, kitchen edition*: a sealed syringe (or a capped pump) — record plunger positions (volume) for stacked weights or measured pushes if you can improvise pressure readings; otherwise verify qualitatively and complete this dataset instead: (100 kPa, 48 cm³), (120, 40), (160, 30), (240, 20) — find the constant, predict V at 300 kPa, plot P against 1/V. (2) *Charles, bathroom edition*: a balloon snug over a bottle's neck — move the bottle from iced water to hot tap water; describe the balloon, estimate the kelvin ratio, predict the volume change percentage. (3) *Choose-the-law gauntlet*: for five scenarios (breathing in; a crisp packet on a mountain; a sealed jar warming in the sun; a bubble in a beer rising; a piston engine's intake stroke), name the pinned dial, the law, and the predicted direction of change. Close with Charles's intercept: two sentences on what V = 0 extrapolation revealed, and why no real gas ever gets there.

# Integration

**Mathematics**: Boyle is your inverse proportion (xy = k, the hyperbola; linearised by plotting against 1/x) and Charles your direct proportion through the origin — the two canonical relationships of Module One, now wearing laboratory aprons. The absolute-zero intercept is extrapolation used with historic daring: trusting a straight line beyond all data, and being right.

**Biology**: Breathing is Boyle's law run by muscle: the diaphragm enlarges the chest, lung pressure dips below atmospheric, air flows in; exhale reverses it. Respiratory therapy, ventilators, and altitude physiology are gas-law clinical practice — and the bends (dissolved gas un-dissolving as pressure drops) is Boyle's cousin in the bloodstream, governing every dive table ever printed.

# Lore Conclusion

Your two re-run notebooks join the vault's shelf — products constant to the gauge's width, your Charles line ruled boldly backward to its intercept, where you've pencilled the draught-cold number like every junior before you: *−273*. Calde inspects both with a smith's eye for straightness. "Boyle would stand you a drink. Charles would take you ballooning." She racks the cylinder, then taps the two notebooks, side by side. "But notice the swindle in today's tidiness, junior. Boyle pinned temperature. Charles pinned pressure. The REAL world—" she nods upward, toward the weather, toward the city's engines, "—pins nothing. A storm squeezes, heats, and stretches the air all at once. An engine's cylinder does all three a thousand times a minute." She closes both books and stacks them into one pile. "Two laws, one gas. Tomorrow we marry the notebooks — one equation for all three dials turning together — and take it straight into the cylinders of every engine in the city."
