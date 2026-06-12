---
id: phy-sen-m2-01
domainId: physics
tier: SENIOR
moduleId: phy-sen-m2
moduleTitle: "Module 2: Electromagnetic Theory"
moduleGlyph: "🧲"
moduleSortOrder: 2
topicSlug: electric_fields
topicTitle: "Electric Fields"
topicSortOrder: 1
title: "Electric Fields and Potential"
sortOrder: 1
xpReward: 120
practiceType: NONE
questType: INVESTIGATION
feynmanPrompt: "Explain to a junior student what an electric field actually is, why physicists invented it instead of just talking about forces between charges, and what 'potential' adds to the picture."
learningObjectives:
  - Define the electric field as force per unit charge and calculate field strength from E = F/q and E = kQ/r²
  - Interpret field line diagrams and sketch the fields of point charges and parallel plates
  - Distinguish electric potential from potential energy and relate potential difference to work and field strength
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines the electric field as force per unit charge (E = F/q) and explains it as a property of space around a charge, present whether or not a test charge is there"
    - "Correctly describes field line conventions: direction a positive test charge would move, density indicating strength, lines from positive to negative"
    - "Distinguishes potential (energy per unit charge, property of a location) from potential energy (property of a charge at that location)"
    - "Relates potential difference to work done (W = qΔV) or to field strength in a uniform field (E = V/d) with a concrete example"
  keywords: [field, force per unit charge, field lines, potential, volt, work]
  modelAnswer: |
    Coulomb's law tells you the force between two charges, but it is silent about how the
    force crosses the gap. The field concept fills that silence: a charge modifies the space
    around it, and that modification — the electric field — is what pushes on any other
    charge placed nearby. The field at a point is defined as the force a small positive test
    charge would feel there, divided by that charge: E = F/q, measured in newtons per
    coulomb. For a point charge Q the field strength falls off as E = kQ/r², an
    inverse-square law inherited directly from Coulomb.

    Field lines make the invisible visible. They point the way a positive test charge would
    be pushed — outward from positive charges, inward to negative ones — and where the lines
    crowd together the field is strong. Between two oppositely charged parallel plates the
    lines run straight and evenly spaced: a uniform field.

    Potential is the energy bookkeeping of the field. The potential at a point is the
    potential energy per unit charge there, measured in volts (joules per coulomb). It
    belongs to the location; the potential energy belongs to the charge sitting at it.
    Moving a charge q through a potential difference ΔV takes (or releases) work W = qΔV —
    which is why a 12 V battery gives every coulomb 12 joules. In a uniform field the two
    pictures connect simply: E = V/d, so volts per metre and newtons per coulomb are the
    same unit. Charges roll downhill on the potential landscape, and the field is the slope.
guidedSteps:
  - id: phy-sen-m2-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A small test charge of +2 μC placed at point P feels an electric force of 6 × 10⁻³ N.
      What is the electric field strength at P?
    inputConfig:
      options:
        - "3000 N/C"
        - "12 N/C"
        - "1.2 × 10⁻⁸ N/C"
        - "6 × 10⁻³ N/C"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["3000 N/C"]
      rejectedFeedback: "E = F/q = (6 × 10⁻³ N) ÷ (2 × 10⁻⁶ C) = 3 × 10³ N/C. The field is force per unit charge — divide the force by the test charge, watching the powers of ten."
    hint: "Use E = F/q. Convert microcoulombs to coulombs first: 2 μC = 2 × 10⁻⁶ C."
    reflectionPrompt: "Why does the field strength at P not depend on the size of the test charge you used to measure it?"
  - id: phy-sen-m2-01-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      You double your distance from a point charge. What happens to the electric field
      strength you experience?
    inputConfig:
      options:
        - "It halves"
        - "It falls to one quarter"
        - "It stays the same — the field belongs to the charge, not the distance"
        - "It falls to one eighth"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It falls to one quarter"]
      rejectedFeedback: "E = kQ/r² is an inverse-square law. Doubling r makes r² four times larger, so the field drops to a quarter — the same geometry that governed gravitation in your Junior lessons."
    hint: "Look at where r appears in E = kQ/r². It's squared — what does doubling r do to r²?"
    reflectionPrompt: "Where have you met an inverse-square law before, and why do point sources keep producing them?"
  - id: phy-sen-m2-01-g3
    sortOrder: 3
    inputType: FILL_BLANK
    instruction: |
      Two parallel plates are connected to a 200 V supply and separated by 4 mm, producing a
      uniform field between them.

      Field strength E = V ÷ d = 200 ÷ 0.004 = ______ V/m (give the number).
    inputConfig:
      placeholder: "Field strength in V/m"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["50000", "50,000", "50000 V/m", "5e4", "5 x 10^4", "5x10^4", "50 000"]
      rejectedFeedback: "Convert 4 mm to 0.004 m, then divide: 200 V ÷ 0.004 m = 50,000 V/m. Small gaps at modest voltages produce ferociously strong fields — the principle behind spark plugs."
    hint: "Convert the gap to metres before dividing. 4 mm = 0.004 m."
    reflectionPrompt: "Why do sparks jump small gaps more easily than large ones at the same voltage?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Electric field lines around charges always run..."
    options:
      - "From negative charges to positive charges"
      - "In closed loops with no start or end"
      - "Out of positive charges and into negative charges, crowding where the field is strong"
      - "Parallel to the motion of the charges"
    correctIndex: 2
    feedback: "Field lines show the push on a positive test charge: away from positive, toward negative, with line density encoding strength. (Closed loops with no ends belong to magnetic fields — next lesson.)"
  - type: MULTIPLE_CHOICE
    question: "A potential difference of 12 V between two points means..."
    options:
      - "12 joules of work moves any amount of charge between the points"
      - "Each coulomb of charge gains or loses 12 joules of energy moving between the points"
      - "The electric field strength between the points is 12 N/C"
      - "12 coulombs of charge flow between the points each second"
    correctIndex: 1
    feedback: "The volt is a joule per coulomb. Potential difference is energy per unit charge — 12 V means 12 J for every coulomb moved, regardless of the path taken."
---

# Hook

Stroke an amber rod with silk, hold it above paper shavings, and they leap upward — pulled through empty air by nothing you can see. The ancients knew the trick three thousand years ago; the Greek word for amber, *elektron*, named the whole science. But the real question hid in plain sight all that time: the rod isn't touching the paper. *Nothing* is touching the paper. So what, exactly, reaches across the gap?

Physics' answer is one of its boldest inventions: the space itself is altered. Learn to see that alteration — the field — and you hold the concept that underwrites every circuit, every radio wave, and ultimately all of modern physics.

# Lore Introduction

The Deep Laboratories have changed overnight. The mechanics benches are gone; in their place stand insulated stools, racks of charged spheres on glass stems, and a shallow tray of oil in which tiny seeds float, waiting.

Magus Selka holds up the amber rod from yesterday's closing demonstration. "Magus Hale taught you, in your Junior year, that charges push and pull — Coulomb's law, action at a distance, numbers in, force out. Hale is an excellent storm-keeper and an impatient theorist." She smiles slightly. "He never told you *how* the force crosses the gap. Today we repair that omission."

She charges a sphere and lowers it toward the oil tray. The floating seeds swing and align, end to end, in sweeping curves from one side of the tray to the other — a hidden geometry suddenly made visible.

"The charge does not reach across space, Senior. The charge *changes* space. We call the change a field — and you are looking at one."

# Core Learning

## Concept Introduction

**The field: force with an address.** Coulomb's law gives the force between two charges, but the field concept reframes it in two steps. First: a charge Q modifies the space around it, creating an **electric field**. Second: any other charge q placed in that field feels a force from the field *at its own location*. The field at a point is defined operationally:

> **E = F / q** — the force a small positive test charge feels there, per unit of its charge. Units: newtons per coulomb (N/C).

For a point charge, E = kQ/r² — Coulomb's inverse-square law with the test charge divided out. Crucially, the field exists whether or not anything is there to feel it: it is a property of space, not of the interaction.

**Field lines: cartography of the invisible.** Field lines are drawn along the direction a positive test charge would be pushed — radiating out of positive charges, converging into negative ones, never crossing. Where lines crowd, the field is strong; where they spread, it is weak. Two configurations matter most: the **point charge** (radial spokes, inverse-square weakening) and **parallel plates** (straight, evenly spaced lines — a *uniform* field, the same strength everywhere between the plates).

**Potential: the energy landscape.** Pushing a positive charge toward another positive charge stores energy, like compressing a spring. The **electric potential** V at a point is the potential energy per unit charge there:

> **V = PE / q** — measured in volts (1 V = 1 joule per coulomb).

Note the division of labour: *potential* belongs to the location; *potential energy* belongs to the charge occupying it. The useful quantity is almost always the **potential difference** ΔV between two points, because it sets the work: **W = qΔV**. A 12 V battery is a machine that gives every coulomb passing through it 12 joules.

In a uniform field the two descriptions lock together: **E = V/d**, field strength equals potential difference per metre. That makes volts per metre and newtons per coulomb the same unit — and it licenses the best intuition in electrostatics: potential is a landscape of hills (positive charge) and valleys (negative charge), and the field is the *slope*. Positive charges roll downhill; the steeper the slope, the stronger the push.

## Why It Matters

The field concept is the single most consequential idea in this module — arguably in classical physics. Practically, it runs everything from capacitors (uniform fields storing energy between plates) to inkjet printers and old oscilloscope tubes (charged droplets and electron beams steered by deflection plates) to spark plugs (E = V/d explains why a few kilovolts across a millimetre gap tears air apart into a spark). Potential difference — voltage — is the quantity on every battery, every mains socket, every circuit diagram you have ever read; you now know precisely what it means: joules per coulomb. And conceptually, the field is the foundation stone: in three lessons' time you will see that fields are not bookkeeping conveniences but physical things that carry energy, travel as waves, and *are light itself*.

## Worked Examples

**Example 1 — Measuring a field.** A +2 μC test charge at point P feels a force of 6 × 10⁻³ N.
E = F/q = (6 × 10⁻³) / (2 × 10⁻⁶) = **3000 N/C**.
A +5 μC charge placed at the same point would feel F = qE = 5 × 10⁻⁶ × 3000 = 1.5 × 10⁻² N — different force, same field. The field is the property of the place.

**Example 2 — Field of a point charge.** What is the field 30 cm from a +4 μC charge? (k = 9 × 10⁹ N·m²/C²)
E = kQ/r² = (9 × 10⁹ × 4 × 10⁻⁶) / (0.3)² = 36 × 10³ / 0.09 = **4 × 10⁵ N/C**, pointing radially away.
At 60 cm — double the distance — the field is a quarter: 1 × 10⁵ N/C.

**Example 3 — Work from voltage.** An electron (charge 1.6 × 10⁻¹⁹ C) is accelerated through a potential difference of 5000 V in an old display tube.
W = qΔV = 1.6 × 10⁻¹⁹ × 5000 = **8 × 10⁻¹⁶ J** of kinetic energy gained.
Tiny in joules — enormous for an electron, flinging it to about 4% of the speed of light. This calculation, charge times volts, is so common that physicists named a unit after it: the electron-volt.

## Common Mistakes

- Thinking the field only exists when a test charge is present — the field is there regardless; the test charge merely reveals it
- Confusing field strength E (force per charge, N/C) with force F (newtons) — a strong field exerts no force at all until a charge is placed in it
- Treating field lines as actual paths charges follow — they show force direction at each point, not trajectories
- Confusing potential (volts, property of a location) with potential energy (joules, property of a charge at that location)
- Believing a high potential always means a strong field — the field is the *change* of potential with distance; a bird on a 25,000 V wire feels almost no field because everything nearby is at the same potential
- Forgetting to convert units (μC to C, mm to m) before applying formulas — the powers of ten carry the physics

## Mental Model

Picture the potential as a stretched rubber landscape: every positive charge pins the sheet up into a hill, every negative charge pulls it down into a funnel-shaped valley. A positive test charge is a marble on the sheet — it rolls downhill, away from peaks and into valleys. The electric field at any point is simply the steepness and direction of the slope under the marble. Volts measure altitude on this landscape; field strength measures gradient. Parallel plates make a perfectly even ramp; a point charge makes a peak that flattens out by the inverse square as you walk away.

## Mini Summary

- A charge alters the space around it; the electric field E = F/q is that alteration, defined as force per unit positive test charge (N/C)
- A point charge's field follows the inverse-square law E = kQ/r²; field lines run from positive to negative, crowding where the field is strong
- Potential V is potential energy per unit charge (volts = joules/coulomb); it belongs to the location, and W = qΔV gives the work to move charge q across a potential difference
- In a uniform field, E = V/d — the field is the slope of the potential landscape, and charges roll downhill

# Guided Practice Quest

Selka sets a charged sphere, a tray of test charges on glass handles, and a pair of brass plates wired to a humming supply before you. "Three measurements," she says. "First, take a known charge to a marked point and deduce the field from the force — the field is force per charge, always. Second, walk away from a point charge and learn what the inverse square does to your readings. Third, the plates: two hundred volts across four millimetres — find the field in the gap, and treat the answer with respect." She taps the bench. "Hale gave you sparks. I am giving you the reason for sparks."

# Solo Practice Quest

Write an investigation log (350–500 words) explaining the field picture of electrostatics. Cover: why physicists replaced pure action-at-a-distance with the field concept, and what E = F/q means operationally; how field lines encode both direction and strength, contrasting the radial field of a point charge with the uniform field between parallel plates; and what potential adds — define the volt, distinguish potential from potential energy, and work one concrete calculation of your choosing using either W = qΔV or E = V/d. Close with one sentence on what it would mean for the field to be a real physical object rather than a calculational device.

# Integration

**Mathematics:** The field is a vector field — an arrow attached to every point of space — and the potential is a scalar field, a single number everywhere. The statement "the field is the slope of the potential" is your first meeting with the gradient, the workhorse of multivariable calculus. Inverse-square laws and equipotential contours are pure geometry: field lines and equipotentials always cross at right angles, like steepest-descent paths and contour lines on a map.

**Engineering:** Capacitor design is applied uniform-field theory: energy stored per volume scales as E², so engineers race to thin the gap and raise the breakdown field. High-voltage insulation, shielded cables, and the corona rings on pylon hardware are all field-line management — reshaping conductors so the lines never crowd densely enough to ionise the air.

# Lore Conclusion

Selka lifts the charged sphere away from the oil tray, and the seed-patterns relax into randomness — the geometry gone, the space ordinary again.

"Or is it?" she asks, watching you. "You measured the field today with test charges. Crutches. The field did not need them — it was there in the empty gap, holding its pattern, storing energy in pure arrangement." She places the amber rod back in its drawer and takes out something else: a black lodestone, and a scattering of iron filings on a card.

"Tomorrow, the field's twin. It does not push on charges that sit still — only on charges that *move*, and by a rule so strange the force points sideways." She sets the lodestone beneath the card and the filings spring into arcs — loops with no beginning and no end. "*Magnetic Fields and Moving Charges.* Sleep well, Senior. The sideways force waits for no one."
