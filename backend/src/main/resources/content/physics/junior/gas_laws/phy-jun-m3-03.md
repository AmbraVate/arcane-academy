---
id: phy-jun-m3-03
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m3
moduleTitle: "Module 3: Thermodynamics"
moduleGlyph: "♨️"
moduleSortOrder: 3
topicSlug: gas_laws
topicTitle: "Gas Laws"
topicSortOrder: 1
title: "The Combined Gas Law in Action"
sortOrder: 3
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Apply the combined gas law P₁V₁/T₁ = P₂V₂/T₂
  - Solve multi-variable gas problems from weather balloons to engine cylinders
  - Recognise the ideal-gas model's range of validity
integrationDomains: [engineering, earth_science]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Applies P₁V₁/T₁ = P₂V₂/T₂ with absolute units throughout
    - Recovers each pairwise law by pinning one variable
    - Solves one real multi-change scenario (balloon ascent, engine compression)
  keywords: [combined, P₁V₁/T₁, all three, absolute, weather balloon, compression]
  modelAnswer: |
    The combined gas law joins the pairwise laws into one statement: PV/T = constant for a
    fixed mass of gas, so P₁V₁/T₁ = P₂V₂/T₂ — kelvin and absolute pressures mandatory. Pin any
    one variable and the familiar pairs reappear: fixed T gives Boyle, fixed P gives Charles,
    fixed V gives the pressure law. Real problems turn all three dials at once: a weather
    balloon rising into cold thin air expands enormously because pressure's fall outweighs
    temperature's; an engine's compression stroke squeezes AND heats its charge. The model is
    ideal-gas — excellent for air at everyday conditions, unreliable near liquefaction.
guidedSteps:
  - id: phy-jun-m3-03-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A gas occupies 2.0 L at 100 kPa and 300 K. Compressed to 150 kPa at 450 K, its volume becomes V₂ = P₁V₁T₂/(T₁P₂) = ________ L.
    inputConfig:
      placeholder: "2"
    markingRule:
      matchMode: CONTAINS
      accepted: ["2", "2.0"]
      rejectedFeedback: "P₁V₁/T₁ = P₂V₂/T₂: (100×2.0)/300 = (150×V₂)/450 → V₂ = (100×2.0×450)/(300×150) = 2.0 L. The pressure rise (×1.5) and temperature rise (×1.5) exactly cancelled — all three dials, one equation."
    hint: "Rearrange for V₂; keep everything absolute."
    reflectionPrompt: "Could you have spotted the cancellation before computing? What does that train you to look for?"
  - id: phy-jun-m3-03-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A weather balloon launched at 100 kPa and 288 K rises to where pressure is 25 kPa and temperature 223 K. Its volume:
    inputConfig:
      options:
        - "Shrinks — it's colder up there"
        - "Roughly triples — the 4× pressure drop outweighs the ~23% kelvin cooling"
        - "Stays the same"
        - "Exactly quadruples"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Roughly triples — the 4× pressure drop outweighs the ~23% kelvin cooling"]
      rejectedFeedback: "V₂/V₁ = (P₁/P₂) × (T₂/T₁) = (100/25) × (223/288) = 4 × 0.77 ≈ 3.1. Both dials turned; pressure's factor dominated. This is why balloons launch flabby and burst at altitude."
    hint: "Compute both ratios and multiply — let the bigger factor win on paper, not by vibe."
    reflectionPrompt: "Why are weather balloons launched only part-filled, looking half-empty?"
  - id: phy-jun-m3-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      An engine's compression stroke squeezes the air-fuel charge from 500 cm³ to 50 cm³, and the gas's temperature soars from 300 K to about 700 K in the process. Find the pressure ratio P₂/P₁, and explain why diesel engines exploit exactly this effect. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: ["23", "23.3", diesel, ignite, compression, hot, no spark]
      rejectedFeedback: "P₂/P₁ = (V₁/V₂) × (T₂/T₁) = 10 × (700/300) ≈ 23 — the charge ends ~23× its starting pressure AND hot enough that diesel fuel, injected at that moment, ignites spontaneously: no spark plug needed. Compression-ignition IS the combined gas law employed as a lighter. Petrol engines compress less precisely to AVOID this self-ignition (knock)."
    hint: "Two ratios multiply. Then ask what 700 K does to injected fuel."
    reflectionPrompt: "Where did the gas's new thermal energy come from during compression? (Tomorrow's ledger opens with exactly this.)"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The combined gas law P₁V₁/T₁ = P₂V₂/T₂ requires:"
    options:
      - "Celsius temperatures and gauge pressures"
      - "Kelvin temperatures, absolute pressures, and a fixed mass of gas"
      - "Constant volume"
      - "An ideal vacuum"
    correctIndex: 1
    feedback: "Three honesty conditions: absolute T (kelvin), absolute P, and the same trapped gas throughout. Violate any one and the ratios lie."
  - type: MULTIPLE_CHOICE
    question: "Pinning temperature constant in the combined law recovers:"
    options:
      - "Charles's law"
      - "Boyle's law — P₁V₁ = P₂V₂"
      - "Ohm's law"
      - "Nothing recognisable"
    correctIndex: 1
    feedback: "Set T₁ = T₂ and the T's cancel: Boyle reappears. Pin P: Charles. Pin V: the pressure law. The pairwise laws were always one law wearing three masks."
---

# Hook

A weather balloon leaves the launch pad looking embarrassingly under-inflated — a flabby teardrop barely taut. Thirty kilometres up, it is a taut sphere the size of a house, and moments later it bursts, its instruments parachuting home. Nobody pumped anything: the balloon inflated *itself*, riding into air four times thinner and forty degrees colder, two dials turning against each other with pressure winning 4-to-1.

Real gas problems are like this: nothing politely holds still. Engines squeeze *and* heat in one stroke; storms cool *and* expand rising air; your bike pump warms as it compresses. The pairwise laws were training wheels. Today they fuse into one equation — **P₁V₁/T₁ = P₂V₂/T₂** — that handles all three dials at once, and we ride it from the launch pad into the cylinder of a diesel engine, which uses this law, quite literally, as its ignition system.

# Lore Introduction

Calde has stacked the two facsimile notebooks beneath a third, newer ledger stamped with the Foundry's own seal. "The marriage contract," she announces. "Boyle's product over Charles's ratio — one constant for the whole triangle." But today's lesson, she insists, happens away from the bench: she walks you up out of the vault, across the yard, to where the Academy's delivery wagon stands with its engine cover open — the new compression-ignition engine the carters are so proud of. "The Tower lights the Academy," she says, with relish, "but the Foundry MOVES it." She has the carter crank the engine slowly by hand, your palm resting on the cylinder head: with each compression stroke you feel the metal warm beneath your hand. "No spark in this engine, junior. None. It squeezes its breath so hard, so fast, that the breath itself becomes the match." She hands you the new ledger. "One equation governs that cylinder. Marry the notebooks, then come back and tell this engine why it runs."

# Core Learning

## Concept Introduction

**The combined gas law.** For a fixed mass of gas:

```
P V / T = constant        →        P₁V₁/T₁ = P₂V₂/T₂
```

(absolute pressure, kelvin temperature, same trapped gas — the three honesty conditions). Every pairwise law is this one with a dial pinned: T fixed → Boyle; P fixed → Charles; V fixed → the pressure law (P/T constant — the sealed flask's rule). One law, three masks.

**The working method** for any scenario:
1. Tabulate state 1 and state 2 — all six values, converting to K and absolute Pa
2. Solve for the unknown via the equality
3. Sanity-check directionally: which ratios pulled which way, and who won?

**Two flagship applications:**

- **The weather balloon**: ascending into lower P (expansion factor P₁/P₂) and lower T (shrink factor T₂/T₁): V₂/V₁ = (P₁/P₂)(T₂/T₁). Pressure's factor dominates in the real atmosphere — balloons launch slack and burst huge at ~30 km.
- **The engine cylinder**: compression turns V down ~10–20× while T soars (fast squeezing does work on the gas — tomorrow's headline): pressures multiply twenty-fold-plus. **Diesel engines** compress so hard (~20:1) the charge reaches fuel's self-ignition temperature: the combined law *is* the spark. Petrol engines stay below (~10:1) to avoid uncontrolled knock and use a spark instead.

**The model's small print.** This is the **ideal gas** law family: particles as sizeless, non-clinging billiard balls. Superb for air, nitrogen, helium at everyday conditions; increasingly wrong near liquefaction (high P, low T) where molecular size and stickiness intrude. Know your tool's range — engineering's first commandment.

## Why It Matters

- This single equation runs meteorology (rising air parcels), engine design, compressed-gas industry (cylinders, regulators), aerospace cabin pressurisation, and the gas duties of chemistry.
- Compression-ignition demonstrates physics-as-mechanism: a law of nature employed as a component (the diesel's "spark plug" is an equation).
- The pin-one-dial discipline plus all-three fluency completes your gas toolkit — and tees up tomorrow's energy ledger, where the *work of squeezing* gets its own account.

## Worked Examples

**Example 1: The burst altitude, predicted**
Balloon: 4 m³ at launch (100 kPa, 288 K); bursts at 40 m³. Burst ratio 10 = (100/P₂)(T₂/288). Stratospheric T₂ ≈ 217 K: 10 = (100/P₂)(0.753) → P₂ ≈ 7.5 kPa — the pressure near 33 km. Forecasters size balloon film and payload parachutes from exactly this arithmetic, daily, worldwide.

**Example 2: The aerosol in the bonfire (why the warning is sincere)**
Can at 300 K holds propellant at 300 kPa (absolute), volume fixed (rigid can): tossed in a fire at 900 K, P₂ = 300 × (900/300) = **900 kPa** plus vaporising propellant pushing far higher — beyond seam strength: the can becomes a fragmenting pressure vessel. The label's small print is the pressure law with a casualty record.

**Example 3: The bike pump's confession**
Rapid stroke: 300 cm³ → 60 cm³ (×5 squeeze) and the barrel warms — say 300 K → 360 K. P₂/P₁ = 5 × 1.2 = 6. Slow stroke (heat leaking away, T pinned): only ×5. The fast pump fights you *harder* than Boyle predicts — your muscles are paying the extra, and the warmth in the barrel is the receipt. Tomorrow that receipt becomes the First Law's opening entry.

## Common Mistakes

- **Unit relapse** — one Celsius or one gauge-pressure slip wrecks the whole ratio; tabulate-and-convert before any algebra.
- **Letting intuition pick the winner** — "it's colder, so it shrinks" lost to the balloon's 4× pressure factor; compute both ratios, every time.
- **Forgetting fixed mass** — a leaking tyre or an open balloon mouth changes the gas itself; the law holds only for the same trapped particles.
- **Treating compression heating as a violation** — T rising during a fast squeeze isn't breaking the law; it's a third dial turning honestly (and energy arriving as work — name it, don't fear it).
- **Ideal-gas overreach** — near liquefaction (LPG cylinders, cryogenics) the model bends; real-gas corrections exist for exactly there.

## Mental Model

The combined law is **the gas triangle's exchange rate board, all currencies at once**. PV/T is the gas's fixed wealth-index: any transaction — squeeze, heat, stretch — may move all three currencies simultaneously, but the index must close unchanged. Reading a scenario is reading the board: pressure devalued fourfold (altitude), temperature marked down a quarter — the volume must revalue threefold to balance the index. Engines are day-traders running the board twenty times a second; weather is the board traded planet-wide; and the pinned-dial laws of yesterday were just market sessions where one currency happened to be frozen.

## Mini Summary

- ✔ P₁V₁/T₁ = P₂V₂/T₂ — one law for all three dials; pin any one to recover the pairwise laws
- ✔ Honesty conditions: kelvin, absolute pressure, fixed mass
- ✔ Method: tabulate both states, convert, solve, sanity-check the competing ratios
- ✔ Weather balloons (P-drop wins over T-drop) and diesel ignition (squeeze-heating as spark) are the flagship cases
- ✔ Ideal-gas range: everyday conditions excellent; near liquefaction, beware

# Guided Practice Quest

Work through the guided steps to balance a 1.5-against-1.5 cancellation, inflate a stratospheric balloon by arithmetic, and light a diesel engine with an equation.

# Solo Practice Quest

Three combined-law commissions: (1) *The full balloon flight*: launch 6 m³ at (101 kPa, 290 K); compute its volume at 5 km (54 kPa, 256 K), 15 km (12 kPa, 217 K), and 30 km (1.2 kPa, 227 K); plot V against altitude and mark a plausible burst point for a 50 m³ envelope. (2) *Engine paper-audit*: for a petrol engine (compression 10:1, T rising 290 → 600 K) and a diesel (20:1, 290 → 800 K), compute each compression's pressure ratio and write two sentences on why one needs a spark plug and the other must not have one. (3) *Domestic forensics*: choose one real gas event from your week (crisp packet on a hill drive, fridge-door resistance after closing, pump warming) — tabulate states honestly with estimates, run the law, and report. Close with one paragraph on where the ideal-gas model would start failing you, and how you'd know.

# Integration

**Engineering**: Add particle counting and the law becomes PV = nRT, the design equation for compressors, gas storage, pneumatic systems, and airbag inflators (a solid charge becoming a precisely-computed volume of gas in 30 ms). Engine designers run this lesson's arithmetic with combustion chemistry bolted on — the indicator diagram (P against V through a full cycle) awaits you two lessons hence.

**Earth Science**: Meteorology is the combined law applied to parcels of rising and sinking air: expansion cooling (rising parcels cool ~1 °C/100 m — your apprentice weather lesson's mechanism, now computable), Chinook/föhn warming on descent, and the balloon soundings that feed every forecast model. The atmosphere is the Foundry's cylinder with no walls.

# Lore Conclusion

You return to the wagon at dusk and deliver the engine's explanation to Calde and the gratified carter: *twenty-to-one squeeze, the charge driven past seven hundred kelvin, fuel meeting air already hot enough to burn — the law as lighter*. Calde signs your marriage-ledger with a flourish of charcoal. "The triangle is yours entire, junior: three dials, one constant, and an engine that runs on the arithmetic." She closes the engine cover, but keeps her hand on it, feeling the residual warmth — and her voice shifts into the register that, in the Foundry, means the syllabus is about to deepen. "Now the loose thread, properly. Your fast pump-strokes made heat from shoulder-work. This cylinder makes motion from fire. Work becoming heat; heat becoming work — the Foundry's whole trade, junior, and tonight you've felt both directions under your own palm." She taps the warm metal once. "Tomorrow we open the master ledger: internal energy, and the First Law of Thermodynamics. Energy's constitution. After that — fair warning — comes the Second, and the Second has teeth."
