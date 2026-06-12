---
id: phy-app-m1-02
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m1
moduleTitle: "Module 1: Foundations of Physics"
moduleGlyph: "🔭"
moduleSortOrder: 1
topicSlug: scientific_measurement
topicTitle: "Scientific Measurement"
topicSortOrder: 1
title: "Units and the SI System"
sortOrder: 2
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - Name the SI base units for length, mass, time, and temperature
  - Convert between metric prefixes (milli, centi, kilo, mega)
  - Explain why an international system of units exists
integrationDomains: [mathematics, history]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Names the SI base units for length, mass, time, and temperature
    - Performs at least two correct prefix conversions
    - Explains in own words why shared standards matter
  keywords: [metre, kilogram, second, kelvin, SI, prefix, kilo, milli, convert, standard]
  modelAnswer: |
    The SI system gives every scientist the same standards: the metre for length, the kilogram
    for mass, the second for time, and the kelvin for temperature. Prefixes scale these units
    by powers of ten — kilo means a thousand times, centi a hundredth, milli a thousandth, so
    2.5 km is 2,500 m and 30 mm is 0.03 m. Before shared standards, every region measured with
    its own feet, pounds, and barrels, and trade and science suffered constant conversion
    errors. A single international system means a result measured in Tokyo can be used in
    Toronto without translation — which is precisely what science requires.
guidedSteps:
  - id: phy-app-m1-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which set lists **SI base units** only?
    inputConfig:
      options:
        - "metre, kilogram, second"
        - "mile, pound, hour"
        - "centimetre, gram, minute"
        - "foot, kilogram, day"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["metre, kilogram, second"]
      rejectedFeedback: "The SI base units include the metre (length), kilogram (mass), and second (time). Miles, pounds and feet are non-SI; centimetres and grams are derived from base units by prefixes."
    hint: "SI is the international metric system — which list contains its core units, without prefixes?"
    reflectionPrompt: "Why do you think the kilogram, not the gram, was chosen as the base unit of mass?"
  - id: phy-app-m1-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Convert: 3.2 kilometres = ________ metres.
    inputConfig:
      placeholder: "3200"
    markingRule:
      matchMode: CONTAINS
      accepted: ["3200", "3,200", "3200m", "3 200"]
      rejectedFeedback: "Kilo means one thousand, so 3.2 km = 3.2 × 1000 = 3200 m. To go from kilometres to metres, multiply by 1000."
    hint: "Kilo- means ×1000."
    reflectionPrompt: "Which direction of conversion (big unit → small unit, or small → big) do you find easier, and why?"
  - id: phy-app-m1-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A historical merchant sells cloth by the "ell" — the distance from his elbow to his fingertip. In 2–3 sentences, explain what problem this creates and how the SI system solves it.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [standard, same, different, agree, compare, everyone]
      rejectedFeedback: "Key idea: every merchant's ell is different, so quantities cannot be compared or trusted. SI defines units by universal constants so everyone measures with exactly the same standard."
    hint: "What happens when the buyer's elbow is longer than the seller's?"
    reflectionPrompt: "Can you name a modern situation where two unit systems still coexist and cause friction?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is 45 millimetres expressed in metres?"
    options: ["4.5 m", "0.45 m", "0.045 m", "45,000 m"]
    correctIndex: 2
    feedback: "Milli- means one thousandth, so 45 mm = 45 ÷ 1000 = 0.045 m."
  - type: MULTIPLE_CHOICE
    question: "Why is the modern metre defined using the speed of light rather than a metal bar in Paris?"
    options:
      - "The bar was stolen"
      - "Light is easier to see than metal"
      - "A physical bar can corrode, expand, or be damaged — a constant of nature is identical everywhere and forever"
      - "The French government sold the bar"
    correctIndex: 2
    feedback: "Exactly — physical artefacts drift and can be damaged. Constants of nature, like the speed of light, give every laboratory on Earth (or off it) the same standard."
---

# Hook

Until 2019, the kilogram was a physical object: a platinum-iridium cylinder locked in a vault near Paris. Every other kilogram on Earth was, ultimately, a copy of it. There was just one problem — when scientists compared the master cylinder with its official copies over a century, the masses had *drifted apart* by tens of micrograms. The world's definition of mass was changing, and nobody could say which object was "right".

So the world's measurement scientists did something radical: they redefined the kilogram using a constant of nature that can never corrode, drift, or be dropped. Today, every base unit — metre, second, kilogram, kelvin — is defined by the unchanging behaviour of the universe itself.

This lesson is about that system: what the units are, how prefixes scale them, and why all of science speaks this one shared language.

# Lore Introduction

Magus Thorne leads you down a spiral stair into the Standards Vault — a circular room ringed with sealed crystal cases. "The old masters kept rods, weights, and water-clocks in here. Pilgrims travelled months to compare their instruments against ours." He stops at an empty case, its velvet cushion bare. "We retired the last artefact years ago. The new standards are not objects at all." He taps his temple. "They are *definitions* — written in the constants of nature, the same in every kingdom, on every world. No thief can steal them and no fire can melt them. Tonight you learn to speak in them fluently, because every chart, every ledger, and every spell-measurement in this Academy is written in this one tongue."

# Core Learning

## Concept Introduction

The **SI system** (Système International) is the shared measurement language of science. Four base units carry most of an apprentice's work:

| Quantity | SI base unit | Symbol |
|----------|-------------|--------|
| Length | metre | m |
| Mass | kilogram | kg |
| Time | second | s |
| Temperature | kelvin | K |

(There are seven in total — the ampere, mole, and candela complete the set.)

**Prefixes** scale any unit by powers of ten:

| Prefix | Symbol | Meaning | Example |
|--------|--------|---------|---------|
| kilo- | k | × 1,000 | 1 km = 1000 m |
| centi- | c | ÷ 100 | 1 cm = 0.01 m |
| milli- | m | ÷ 1,000 | 1 mm = 0.001 m |
| mega- | M | × 1,000,000 | 1 MJ = 1,000,000 J |

To convert, move the decimal point: going to a *smaller* unit multiplies the number; going to a *larger* unit divides it. 2.5 km → 2500 m. 30 mm → 0.03 m.

Since 2019, every SI unit is defined by **constants of nature**: the metre by the speed of light, the second by the vibration of a caesium atom, the kilogram by Planck's constant. Definitions built on the universe itself are identical in every laboratory, forever.

## Why It Matters

- Every equation in physics assumes consistent units. Mixing kilometres with metres inside one calculation is the most common error in early physics — and in real engineering.
- Medicine doses in milligrams, engineering loads in meganewtons, electricity bills in kilowatt-hours: prefix fluency is a daily-life skill, not just an exam skill.
- International science, trade, and aviation function only because everyone has agreed on the same standards — the SI system is one of the quietest, most successful treaties in human history.

## Worked Examples

**Example 1: Converting downwards (larger → smaller unit)**
A corridor is 0.85 km long. In metres: 0.85 × 1000 = **850 m**. The unit got smaller, so the number got bigger — there are more small steps in the same distance.

**Example 2: Converting upwards (smaller → larger unit)**
A beetle is 12 mm long. In metres: 12 ÷ 1000 = **0.012 m**. The unit got bigger, so the number got smaller.

**Example 3: Catching an absurd answer**
A student converts 5 cm to metres and writes 500 m. Sanity check: 5 cm is about the width of a matchbox — can that be half a kilometre? No. The student multiplied instead of dividing. *Always check whether the converted number should be bigger or smaller.*

## Common Mistakes

- **Multiplying when you should divide** — converting 250 g to kilograms gives 0.25 kg, not 250,000 kg. Sanity-check against an everyday object.
- **Confusing the symbol m (metre) with the prefix m (milli-)** — context and position matter: "mm" is millimetre, "m" alone is metre.
- **Dropping units mid-calculation** — carry units through every line; if the final unit is wrong, the calculation is wrong.
- **Treating centi- as a thousandth** — centi- is a hundredth (think *century* = 100 years).

## Mental Model

Picture the prefixes as **floors of a tower**, each floor ten times the one below. Converting units is just riding the lift: each floor you descend (towards smaller units) adds a zero to your number; each floor you ascend removes one. The quantity itself — the actual length of the corridor — never changes. Only the floor you describe it from changes.

## Mini Summary

- ✔ SI base units: metre (length), kilogram (mass), second (time), kelvin (temperature)
- ✔ Prefixes scale units by powers of ten: kilo ×1000, centi ÷100, milli ÷1000
- ✔ Smaller unit → bigger number; bigger unit → smaller number
- ✔ Modern units are defined by constants of nature, identical everywhere
- ✔ Sanity-check every conversion against an everyday object

# Guided Practice Quest

Work through the guided steps to identify SI base units, perform prefix conversions, and explain why humanity abandoned elbow-lengths for universal standards.

# Solo Practice Quest

Write down five measurements from your own life today — your height, the mass of something in your kitchen, the time of your commute, the distance to a place you visit, the temperature outside. Express each one twice: once in its natural everyday unit, and once converted to the pure SI base unit (metres, kilograms, seconds, kelvin — for temperature, kelvin = °C + 273). Then reflect in a few sentences: which conversion felt least natural, and why do you think everyday life often resists base units?

# Integration

**Mathematics**: Prefix conversion is applied powers of ten — the same place-value system you use in decimal arithmetic. Scientific notation (3.2 × 10³ m) and SI prefixes (3.2 km) are two notations for one idea, and fluency in one strengthens the other.

**History**: The metric system was born in the French Revolution as a deliberately *democratic* measure — units owned by no king, derived from the Earth itself. Tracing how the metre's definition moved from a brass bar to the speed of light is a compact history of precision itself.

# Lore Conclusion

Thorne seals the vault behind you. "Nine hundred years ago, an apprentice carrying a copy of our standard rod drowned crossing the eastern straits, and two kingdoms measured cloth differently for a generation." He climbs the stair ahead of you. "Now the standard is written into light itself. You could be shipwrecked on a shore with nothing, and still — in principle — reconstruct the metre exactly." At the top of the stairs he pauses. "That is what it means for knowledge to be *universal*. Tomorrow we measure things that wobble, drift, and refuse to hold still — and you learn the most honest word in science: *uncertainty*."
