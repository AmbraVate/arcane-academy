---
id: phy-jun-m1-08
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: gravitation
topicTitle: "Gravitation"
topicSortOrder: 3
title: "Gravitational Fields and g"
sortOrder: 8
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Define gravitational field strength as force per unit mass (g = F/m)
  - Compute g for different worlds via g = GM/r²
  - Distinguish true weightlessness from free-fall 'weightlessness'
integrationDomains: [mathematics, biology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Defines field strength g = F/m (N/kg) and connects it to free-fall acceleration
    - Computes another world's g from its mass and radius
    - Explains that orbiting astronauts are in continuous free fall, not beyond gravity
  keywords: [field, N/kg, g = GM/r², free fall, weightless, varies, surface gravity]
  modelAnswer: |
    A gravitational field maps the pull a mass would feel at every point: field strength
    g = F/m, in newtons per kilogram, numerically equal to the free-fall acceleration there.
    For any world, g = GM/r²: Earth's 9.8 N/kg follows from its mass and radius, the Moon's
    1.6 from its smaller mass and radius, Jupiter's ~25 from its bulk. The field weakens with
    altitude by the inverse square. Orbiting astronauts float not because they have escaped
    gravity — at 400 km, g is still about 8.7 N/kg — but because they and their station are
    falling together: free fall removes the supporting forces that weight-sensation is made
    of.
guidedSteps:
  - id: phy-jun-m1-08-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      Field strength is force per unit mass. A 60 kg explorer on Mars weighs 222 N, so Martian surface gravity is g = F/m = ________ N/kg.
    inputConfig:
      placeholder: "3.7"
    markingRule:
      matchMode: CONTAINS
      accepted: ["3.7"]
      rejectedFeedback: "g = 222/60 = 3.7 N/kg — about 38% of Earth's. The field strength is a property of the location; her mass is unchanged."
    hint: "Divide the weight by the mass."
    reflectionPrompt: "What would the same explorer weigh back on Earth, and what stayed constant between worlds?"
  - id: phy-jun-m1-08-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Astronauts aboard the ISS (altitude 400 km) float freely. The BEST explanation:
    inputConfig:
      options:
        - "There is no gravity in space"
        - "Gravity there is nearly zero"
        - "They, and their station, are in continuous free fall around the Earth — falling together, so nothing presses on anything"
        - "Their suits cancel gravity"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["They, and their station, are in continuous free fall around the Earth — falling together, so nothing presses on anything"]
      rejectedFeedback: "At 400 km, g ≈ 8.7 N/kg — almost 90% of surface strength! The station is a projectile perpetually falling around the planet; occupants fall with it, so floors never press on feet. Weight-SENSATION is the supporting force, and free fall has none."
    hint: "Compute g at r = 6,770 km before accepting 'no gravity'. Then ask what weight-sensation actually is."
    reflectionPrompt: "Why do you feel the same floating moment on a trampoline's descent or a dropped lift?"
  - id: phy-jun-m1-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      The Moon has 1/81 of Earth's mass but g on its surface is 1/6 of Earth's, not 1/81. Resolve this using g = GM/r². (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [radius, smaller, r², closer, surface, compensat]
      rejectedFeedback: "Surface g depends on BOTH mass and radius: g = GM/r². The Moon's radius is ~3.7× smaller, so its surface sits much closer to its centre — the r² in the denominator boosts the field ~13.7×, partially offsetting the 81× mass deficit: 13.7/81 ≈ 1/6."
    hint: "The Moon's surface is much CLOSER to its centre. What does r² do with that?"
    reflectionPrompt: "Could a tiny, ultra-dense world out-pull Earth at its surface? What would that require?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Gravitational field strength is measured in:"
    options: ["Newtons", "N/kg (equivalently m/s²)", "Kilograms", "Joules"]
    correctIndex: 1
    feedback: "g = F/m: newtons of pull per kilogram placed there — numerically identical to the local free-fall acceleration in m/s². One number, two readings."
  - type: MULTIPLE_CHOICE
    question: "Climbing to twice Earth's radius from the centre, your weight becomes:"
    options: ["Half", "A quarter — inverse square", "Zero", "Unchanged"]
    correctIndex: 1
    feedback: "g = GM/r²: at r = 2R, the field is GM/4R² — one quarter of surface strength. Gravity fades with altitude but never reaches zero at any finite distance."
---

# Hook

Here's a number that breaks most people's mental model of space: at the International Space Station's altitude, Earth's gravity is still **almost 90% of full strength**. The astronauts you've watched somersaulting in "zero gravity" are being pulled toward Earth nearly as hard as you are right now.

So why do they float? Because floating isn't the absence of gravity — it's the absence of *resistance* to gravity. They are falling. Continuously, at 8.7 m/s², around the planet — and their station falls with them, perfectly in step, so no floor ever rises to press against their feet. What you call "weight" — the sensation — was never gravity itself. It was always the chair pushing back.

To untangle this properly, physics invented one of its most powerful ideas: the **field** — a map of pull, drawn over all of space. Learn to read it, and the Moon's gentle 1.6, Jupiter's crushing 25, and the astronaut's paradoxical float all become entries on one chart.

# Lore Introduction

Thorne unrolls across the Observatory's great table a chart you mistake at first for a sea-map: concentric rings about a drawn Earth, each ring labelled with a number that shrinks outward — 9.8 near the coastline of the planet, 2.5 further out, 0.0027 at a ring marked with the Moon's sigil. "The old navigators charted winds and currents — what the sea would *do* to a ship at every point, before any ship sailed there," he says. "This is the Academy's chart of the banker's territory. At every point of empty space, one question is answered in advance: *if a kilogram sat here, how hard would it be pulled?*" He sets a brass 1 kg standard from the vault gently on the chart's Earth-coast. "Nine-point-eight newtons, payable instantly. We call the chart a *field*, junior — and once you can read it, you will never again say anything so careless as 'there is no gravity in space'."

# Core Learning

## Concept Introduction

**The field idea.** Rather than computing force pair-by-pair, map the influence: a **gravitational field** assigns to every point in space the pull that *would* act on each kilogram placed there:

```
g = F / m        (newtons per kilogram)
```

Numerically, g in N/kg equals the free-fall acceleration in m/s² (because a = F/m too) — one number, two readings: *pull per kilogram* and *fall rate*. Field arrows point toward the mass that sources them; field strength fades with distance.

**Computing any world's surface field:**

```
g = G M / r²
```

| World | M (relative) | r (relative) | Surface g |
|-------|--------------|--------------|-----------|
| Earth | 1 | 1 | 9.8 N/kg |
| Moon | 1/81 | 1/3.7 | 1.6 |
| Mars | 0.11 | 0.53 | 3.7 |
| Jupiter | 318 | 11 | ~24.8 |

Note the Moon's lesson: surface gravity is a *competition* between mass (less pull) and radius (closer surface) — 1/81 of the mass but 1/6 of the g, because r² rewards compactness. Altitude obeys the same formula: at r = 2R, g is a quarter; at the ISS (r = 1.06R), still 8.7.

**Weight-sensation, finally dissected.** Your weight (the force) is mg. But what you *feel* is the **supporting force** — floor on feet, chair on body. Remove the support and the sensation vanishes *while gravity continues unabated*: that is **free fall**. A dropped lift, the top of a trampoline bounce, a parabolic-flight aircraft, and every orbiting spacecraft are all the same state: gravity as the only force, everything falling together, nothing pressing on anything. "Weightlessness" in orbit is *permanent free fall around the planet* — the field there is strong; the support is simply never needed.

**True zero-field** exists only in idealisation — infinitely far from all masses, or at balance points between bodies. Everywhere real, you are in *somebody's* field.

## Why It Matters

- Field thinking is physics' standard tool from here on: electric and magnetic fields (your next module!) reuse this exact template — map first, place objects second.
- Mission design lives on field maps: launch energies, transfer orbits, landing burns, and the surface kit for Moon vs Mars all start from local g.
- The free-fall insight corrects a near-universal misconception and explains microgravity science, astronaut physiology, and why "artificial gravity" means spinning (a floor that perpetually accelerates into you).

## Worked Examples

**Example 1: Computing Jupiter from the catalogue**
M = 1.9×10²⁷ kg, r = 7.0×10⁷ m: g = 6.67×10⁻¹¹ × 1.9×10²⁷ / (7×10⁷)² ≈ **26 N/kg**. A 70 kg visitor would weigh ~1,800 N — like carrying a piano, permanently. (Their mass, as ever, travels unchanged; the chart's entry is what differs.)

**Example 2: The mineshaft surprise**
Descend INTO the Earth and g does not keep rising — only the mass *beneath you* still pulls inward (the spherical shell above cancels itself out, a beautiful theorem). g falls roughly linearly to zero at the centre, where you'd float in the world's most expensive basement. The field chart inside a planet looks nothing like the outside's inverse square — maps must be read, not assumed.

**Example 3: The vomit comet's arithmetic**
A parabolic-flight aircraft flies the exact trajectory of a thrown ball for ~25 s: engines trimmed so the plane *is* a projectile. Inside, passengers and cabin fall identically — free fall, full float, while g outside the window remains a sturdy 9.8. Astronaut training, film shoots, and microgravity experiments rent this state by the parabola; no altitude was harmed in producing the weightlessness.

## Common Mistakes

- **"No gravity in space"** — at every real location, some field reigns; orbital float is free fall, not field absence.
- **Conflating mass and weight again at higher stakes** — mass is yours forever; weight is mg, the field's local levy. (Junior tier upgrade: now you can compute the levy anywhere.)
- **Assuming g is universal at 9.8** — it's Earth-surface-specific; every formula with g inherits the local chart entry.
- **Linear altitude reasoning** — g fades with the *square* of distance from the centre, and barely changes over human altitudes (Everest: −0.3%); both extremes mislead.
- **"Astronauts float because they're beyond the pull"** — at ISS height the pull is 90%; without it they'd leave on a tangent (the first law never sleeps).

## Mental Model

A gravitational field is **a tax map published in advance**. Every point of space carries a posted rate — so many newtons per kilogram, payable by any mass that stands there. Earth's surface posts 9.8; the Moon's provinces post 1.6; deep space posts fractions of a penny. Your *mass* is your taxable estate, identical in every jurisdiction; your *weight* is the local bill. And the strangest clause in the code: the tax is only ever *felt* through whatever resists it — floors, chairs, rocket seats. Fall freely, and though the full rate is levied every second (your trajectory pays it, bending earthward), nothing presses, nothing aches, and the body — that naive accountant — declares itself weightless in a province taxing at ninety percent.

## Mini Summary

- ✔ Field strength g = F/m (N/kg) ≡ local free-fall acceleration (m/s²) — a map of pull-per-kilogram
- ✔ g = GM/r²: any world's surface entry from its mass and radius; compactness counts (the Moon's 1/6)
- ✔ Altitude fades g by inverse square; ISS altitude still posts ~8.7
- ✔ Weight-sensation = supporting force; free fall removes support, not gravity
- ✔ Orbital "weightlessness" is perpetual free fall around the planet — the field is strong, the floor just falls too

# Guided Practice Quest

Work through the guided steps to read Mars's posted rate from one explorer's bill, acquit the ISS of "zero gravity", and let r² explain the Moon's generous sixth.

# Solo Practice Quest

Three field surveys: (1) *Build the chart*: compute g at Earth's surface, at aircraft altitude (12 km), at the ISS (400 km), and at the Moon's distance (60R) — tabulate, then write one sentence on where "space begins" according to gravity (trick question — defend your answer). (2) *Foreign postings*: compute surface g for Mars and for a neutron star (M = 1.4 solar masses = 2.8×10³⁰ kg, r = 12 km) — then compute your weight at each and one sentence per world on the practical consequences. (3) *Free-fall fieldwork*: stand on bathroom scales and watch the needle as you drop into a quick crouch (the start of the drop is a moment of partial free fall) — describe the dip, then explain a lift's scale-readings on starting down and braking, in supporting-force language. Close by drafting the two-sentence correction you'd give a news article that says astronauts "escape gravity".

# Integration

**Mathematics**: A field is a function over space — your first. Gravitational field strength is a vector field (magnitude and direction at every point); contour-style "equipotential" maps and field-line drawings are graphical calculus, and the shell theorem behind the mineshaft example is integration wearing its Sunday best. Electric fields next module will reuse every habit.

**Biology**: Bodies are built against a 9.8 levy: bone density, heart strength, fluid distribution. In orbital free fall, the supports vanish and biology misreads it as holiday — bones shed ~1% density per month, hearts decondition, fluids drift headward (the puffy-faced astronaut). Two hours of daily exercise against elastic "gravity substitutes" is the ISS's tax-compliance programme.

# Lore Conclusion

By candle-end you have extended Thorne's chart in your own hand: the aircraft ring, the station ring with its scandalous 8.7, the Moon's province, and — at Vex's insistence, in the margin — a neutron star's entry so monstrous the ink seems heavier there. Thorne reviews the additions like a master cartographer accepting a journeyman's work. "The territory is mapped. The rates are posted." He rolls the chart and hands it — to your surprise — to *you*. "Keep it. Tomorrow you stop surveying the banker's territory and start doing business in it." Vex, by the door, is already turning a small brass model in his fingers: a tiny cannon atop a tiny mountain, aimed parallel to the curve of a globe. "Newton drew this thought before anyone could build it," he says. "Fire faster, fall further, miss the ground entirely. Tomorrow, junior: orbits — by arithmetic, on purpose. We are going to put something *around* a world."
