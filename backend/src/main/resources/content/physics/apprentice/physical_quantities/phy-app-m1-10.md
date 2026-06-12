---
id: phy-app-m1-10
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m1
moduleTitle: "Module 1: Foundations of Physics"
moduleGlyph: "🔭"
moduleSortOrder: 1
topicSlug: physical_quantities
topicTitle: "Physical Quantities"
topicSortOrder: 4
title: "Base and Derived Quantities"
sortOrder: 10
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - Distinguish base quantities from derived quantities
  - Build derived units (m/s, m/s², kg/m³) from base units
  - Recognise named derived units (newton, joule, watt) as combinations of base units
integrationDomains: [mathematics, chemistry]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Lists the base quantities used in mechanics (length, mass, time)
    - Builds at least three derived units from base units
    - Explains that named units like the newton are shorthand for base-unit combinations
  keywords: [base, derived, newton, joule, watt, combination, speed, density, unit]
  modelAnswer: |
    Base quantities — length, mass, time (plus temperature, current and others) — are the
    fundamental currencies of measurement. Every other quantity is derived by combining them:
    speed is length per time (m/s), acceleration is speed change per time (m/s²), density is
    mass per volume (kg/m³). Some combinations are so useful they earn names: the newton is
    kg·m/s² and the joule is a newton-metre (kg·m²/s²). Knowing what a derived unit unpacks
    into reveals what the quantity actually is.
guidedSteps:
  - id: phy-app-m1-10-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of these is a **derived** quantity rather than a base quantity?
    inputConfig:
      options:
        - "Mass"
        - "Time"
        - "Speed"
        - "Length"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Speed"]
      rejectedFeedback: "Speed = distance ÷ time — built from two base quantities. Mass, time, and length are themselves base quantities; nothing simpler underlies them."
    hint: "Which one is defined as a combination of the others?"
    reflectionPrompt: "Could physics have chosen speed as a base quantity and derived length instead? What would that change?"
  - id: phy-app-m1-10-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Density is mass divided by volume. Its derived unit is kg/________.
    inputConfig:
      placeholder: "m³"
    markingRule:
      matchMode: CONTAINS
      accepted: ["m³", "m3", "m^3", "cubic metre", "cubic meter"]
      rejectedFeedback: "Volume is length × length × length = m³, so density's unit is kg/m³ — kilograms packed into each cubic metre."
    hint: "Volume is a length cubed."
    reflectionPrompt: "Water's density is 1000 kg/m³. Does that number feel right for a 1 m × 1 m × 1 m cube of water?"
  - id: phy-app-m1-10-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      The newton (unit of force) unpacks into kg·m/s². In 2–3 sentences, explain what this combination suggests about what force *does*, before you've even studied forces formally.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [mass, accelerat, m/s², change, speed, per second]
      rejectedFeedback: "kg·m/s² reads as 'mass × acceleration': a force is whatever it takes to change a kilogram's velocity by one metre per second, each second. The unit IS the law F = ma in disguise."
    hint: "m/s² is the unit of acceleration. So a newton is a kilogram times... what?"
    reflectionPrompt: "What does it suggest that the unit of energy (joule) is a newton × metre?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The unit m/s² belongs to which quantity?"
    options: ["Speed", "Acceleration", "Density", "Force"]
    correctIndex: 1
    feedback: "Metres per second, per second — how much the speed (m/s) changes each second. That is acceleration."
  - type: MULTIPLE_CHOICE
    question: "Why do some derived units get their own names (newton, joule, watt)?"
    options:
      - "To honour scientists, with no practical purpose"
      - "Because writing kg·m²/s³ every time is clumsy — names are shorthand for frequently-used combinations"
      - "Because those quantities are base quantities"
      - "Because the combinations are secret"
    correctIndex: 1
    feedback: "Exactly — a watt IS kg·m²/s³, but 'watt' is easier to say and write. The name honours a scientist AND compresses a combination used constantly."
---

# Hook

Walk through a market and you'll hear a dozen units: kilos of apples, litres of milk, minutes until closing. Now here's the surprising claim physics makes: underneath this babel, *nearly everything measurable in mechanics is built from just three ingredients* — length, mass, and time.

Speed? Length divided by time. Density? Mass divided by length-cubed. Force, energy, power — combinations of the same three, stacked a little deeper. The universe, it turns out, runs on a startlingly small set of currencies, and everything else is exchange rates.

Once you can *unpack* a unit — see the kg·m/s² hiding inside a newton — units stop being arbitrary labels and start being X-rays of the quantities themselves.

# Lore Introduction

In the Observatory's counting-house, Thorne opens a ledger of the Academy's stores: rope by the span, grain by the stone, lamp-oil by the flask, watch-shifts by the candle. "Forty units in this book," he says, "and the old quartermasters drowned in conversions between them." He turns to the ledger's final page, where some long-dead reformer has ruled three columns only: *length, mass, time*. "Then someone noticed: rope is length. Grain is mass. Candles are time. Oil is length-cubed of volume. Every unit in the book is these three, dressed differently." He closes the ledger. "The universe keeps tidier books than the quartermasters did. Three currencies, apprentice. Learn the exchange rates."

# Core Learning

## Concept Introduction

**Base quantities** are the irreducible currencies of measurement — defined by standards, not by other quantities. In mechanics you need three:

| Base quantity | SI unit | Symbol |
|---------------|---------|--------|
| Length | metre | m |
| Mass | kilogram | kg |
| Time | second | s |

(Temperature in kelvin and electric current in amperes join later; the full SI has seven.)

**Derived quantities** are built from base quantities by multiplication and division — and their units assemble in exactly the same way:

| Derived quantity | Built from | Unit |
|------------------|-----------|------|
| Area | length × length | m² |
| Volume | length³ | m³ |
| Speed | length ÷ time | m/s |
| Acceleration | speed ÷ time | m/s² |
| Density | mass ÷ volume | kg/m³ |

Some combinations occur so often they earn **names**:

- **newton** (N) = kg·m/s² — force
- **joule** (J) = N·m = kg·m²/s² — energy
- **watt** (W) = J/s = kg·m²/s³ — power

The names are shorthand, nothing more. Unpacking them is informative: a newton being kg·m/s² whispers F = ma before you've met the law; a watt being joules-per-second tells you power is the *rate* of energy transfer.

## Why It Matters

- Unit-unpacking is a free comprehension tool: meet any new quantity, unpack its unit, and you learn what the quantity *is*.
- Checking that both sides of an equation carry the same units (you cannot equate m/s with kg!) catches algebra errors before they cost marks or money.
- Chemistry, engineering, and medicine all build on the same base-and-derived structure — concentration (mol/m³), pressure (N/m²), dose rate (J/kg per s) all unpack the same way.

## Worked Examples

**Example 1: Building a unit from its definition**
Pressure is force spread over area: P = F/A. Unit: N ÷ m² = N/m² (named the pascal). Unpacked fully: kg·m/s² ÷ m² = kg/(m·s²). You now know what a weather report's "1013 hectopascals" is made of.

**Example 2: Unpacking to understand**
You meet "power: 60 W" on a lightbulb. Unpack: 60 joules per second — the bulb transfers 60 J of energy every second it's on. Leave it on for an hour (3600 s): 216,000 J. The unit told you how to compute the energy bill.

**Example 3: Catching an impossible formula**
A student derives speed = distance × time, units: m × s. But speed must be m/s. The units refuse the formula — no numbers needed to know it's wrong. Dimensional checking is proofreading for physics.

## Common Mistakes

- **Treating named units as mysterious** — newton, joule, pascal, watt are all just base-unit combinations wearing coats.
- **Adding quantities with different units** — 5 m + 3 s is meaningless; only like units add. (Multiplying different units is fine — that's how derived quantities are born.)
- **Confusing m/s² with (m/s)²** — the first is acceleration; the second (m²/s²) is something else entirely.
- **Forgetting volume scales as length cubed** — doubling a box's sides multiplies its volume by 8, not 2; this trips even professionals.

## Mental Model

Think of base units as **primary colours**. Three pigments — length-red, mass-blue, time-yellow — and every quantity in mechanics is a mixture: speed is red-over-yellow, density is blue-over-three-reds. Named units are like paint-shop names ("ochre", "teal") for mixtures used so often they earned labels — convenient, but always mixable from the primaries, and always unmixable back into them when you want to see what something is made of.

## Mini Summary

- ✔ Base quantities (length, mass, time + friends) are measurement's irreducible currencies
- ✔ Derived quantities combine base quantities; their units combine identically
- ✔ newton = kg·m/s², joule = N·m, watt = J/s — names are shorthand for combinations
- ✔ Unpack any unfamiliar unit to learn what the quantity is
- ✔ Units must agree across =, +, and − ; disagreement exposes a wrong formula

# Guided Practice Quest

Work through the guided steps to sort base from derived, assemble density's unit, and read the physics hidden inside the newton.

# Solo Practice Quest

Hunt unit combinations in the wild: find five derived units in your home — on appliance labels (watts), food packets (kJ), tyre pressures (psi or kPa), speedometers (km/h), medicine doses (mg/kg). For each: (1) write the unit, (2) unpack it into base units as far as you can, and (3) write one sentence on what the unpacked form reveals about the quantity. Finish with the reverse game: invent a plausible unit for "coffee consumption rate of a household" from base-style ingredients, and defend your construction.

# Integration

**Mathematics**: Units form an algebra — they multiply, divide, and cancel exactly like symbols. Dimensional analysis (matching base-unit powers across an equation) can even *predict* the form of physical laws, a technique you'll meet properly at Senior tier.

**Chemistry**: The mole — chemistry's signature base unit — extends the same system to counting particles, and every concentration, reaction rate, and gas-law constant in chemistry is a derived combination including it. The bookkeeping you learned here transfers wholesale.

# Lore Conclusion

Thorne sets the old quartermaster's ledger back on its shelf. "Three currencies and their combinations — that is the entire vocabulary of the mechanical world." He pauses at the counting-house door. "Almost. There is one more distinction, and it is not about *how much* but *which way*. A cartload of grain doesn't care what direction it weighs. A push very much does." He snuffs the lamp. "Tomorrow: quantities with arrows."
