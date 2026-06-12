---
id: phy-sen-m2-03
domainId: physics
tier: SENIOR
moduleId: phy-sen-m2
moduleTitle: "Module 2: Electromagnetic Theory"
moduleGlyph: "🧲"
moduleSortOrder: 2
topicSlug: electromagnetic_waves
topicTitle: "Electromagnetic Waves"
topicSortOrder: 3
title: "Electromagnetic Waves: Fields That Carry Light"
sortOrder: 3
xpReward: 120
practiceType: NONE
questType: INVESTIGATION
feynmanPrompt: "Explain to a junior student how electric and magnetic fields can leapfrog each other through empty space, why that makes a wave that needs no medium, and how we know light is exactly that wave."
learningObjectives:
  - Explain how mutually regenerating electric and magnetic fields propagate as a self-sustaining wave requiring no medium
  - Describe the structure of an electromagnetic wave (perpendicular E and B fields, transverse, travelling at c) and apply c = fλ across the spectrum
  - Explain how accelerating charges generate electromagnetic waves and how antennas transmit and receive them
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the self-sustaining mechanism: a changing magnetic field induces an electric field, and a changing electric field induces a magnetic field, each regenerating the other"
    - "Describes the wave's structure: E and B perpendicular to each other and to the direction of travel, in step, moving at c ≈ 3 × 10⁸ m/s in vacuum"
    - "States that electromagnetic waves need no medium and explains why this distinguishes them from sound and water waves"
    - "Connects wave generation to accelerating charges (e.g. oscillating currents in antennas) and applies c = fλ correctly to at least one calculation"
  keywords: [changing field, induces, self-sustaining, perpendicular, no medium, speed of light, antenna]
  modelAnswer: |
    An electromagnetic wave is a chase that never ends. Faraday showed that a changing
    magnetic field creates an electric field — that is induction, the heartbeat of every
    generator. Maxwell's great insight was the symmetric partner: a changing electric
    field creates a magnetic field. Put the two rules together and fields can bootstrap
    themselves through empty space: the dying magnetic field births an electric field,
    whose own change births a fresh magnetic field, and so on — each regenerating the
    other, forever, with no charges and no medium needed anywhere along the path.

    The travelling structure is strict: the electric field oscillates in one plane, the
    magnetic field in the perpendicular plane, both at right angles to the direction of
    travel — a transverse wave — and in vacuum the leapfrog proceeds at exactly one speed,
    c ≈ 3 × 10⁸ m/s. When Maxwell computed that speed from purely electrical measurements
    and it matched the measured speed of light, the conclusion was unavoidable: light IS
    an electromagnetic wave. Radio, microwaves, infrared, visible light, ultraviolet,
    X-rays, and gamma rays are one phenomenon at different frequencies, all obeying
    c = fλ.

    The waves come from accelerating charges. A charge at rest makes a static field; a
    charge in steady motion makes a steady current's field; but shake a charge and the
    kink in its field ripples outward at c. An antenna is exactly that — electrons
    sloshed up and down a rod millions of times per second, broadcasting waves whose
    frequency matches the sloshing. A receiving antenna runs the trick backwards: the
    passing wave's electric field pushes the rod's electrons into a tiny oscillating
    current, which circuitry amplifies into signal.
guidedSteps:
  - id: phy-sen-m2-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Sound cannot cross a vacuum; light crosses the vacuum from the Sun to Earth easily.
      What is it about an electromagnetic wave that lets it travel without any medium?
    inputConfig:
      options:
        - "It pushes off tiny particles of dust that exist even in space"
        - "Its changing electric and magnetic fields regenerate each other — the wave is its own medium"
        - "It travels so fast that it crosses the vacuum before it can die out"
        - "Space is filled with an invisible elastic substance that carries it"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Its changing electric and magnetic fields regenerate each other — the wave is its own medium"]
      rejectedFeedback: "Sound needs molecules to pass the vibration along. An electromagnetic wave carries its own machinery: the changing magnetic field induces the electric field, whose change induces the next magnetic field — a self-sustaining leapfrog needing nothing at all to run in."
    hint: "Recall the two induction rules: a changing B makes an E, and a changing E makes a B. What can those two rules do together, with no charges around?"
    reflectionPrompt: "Nineteenth-century physicists invented a medium called the 'luminiferous aether' for light to wave in. Why was it never needed?"
  - id: phy-sen-m2-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A radio station broadcasts at 100 MHz (1.0 × 10⁸ Hz). All electromagnetic waves
      travel at c = 3 × 10⁸ m/s in air (to excellent approximation).

      Wavelength λ = c ÷ f = (3 × 10⁸) ÷ (1.0 × 10⁸) = ______ m (give the number).
    inputConfig:
      placeholder: "Wavelength in metres"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["3", "3m", "3 m", "3.0", "3.0 m"]
      rejectedFeedback: "λ = c/f = (3 × 10⁸ m/s) ÷ (1 × 10⁸ Hz) = 3 m. FM radio waves are about the height of a door — which is why FM antennas are around 1.5 m, half a wavelength, the natural resonant length."
    hint: "Divide the speed by the frequency. The powers of ten nearly cancel."
    reflectionPrompt: "Visible light has wavelengths around 5 × 10⁻⁷ m. Roughly how many times higher is its frequency than this radio wave's?"
  - id: phy-sen-m2-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A charge sitting still makes a static electric field. A charge moving at constant
      velocity makes a steady field pattern that travels with it. Neither radiates a wave.

      What must a charge be doing to radiate electromagnetic waves? (One word — think of
      what an antenna does to its electrons.)
    inputConfig:
      placeholder: "One word"
    markingRule:
      matchMode: CONTAINS
      accepted: ["accelerat", "oscillat", "vibrat", "shak"]
      rejectedFeedback: "The charge must accelerate — speed up, slow down, or oscillate. Only an accelerating charge kinks its field lines, and the kink ripples outward at c. Antennas radiate precisely because they slosh electrons back and forth, accelerating them millions of times a second."
    hint: "Steady states make steady fields. Waves come from change. What kind of motion is constantly changing velocity?"
    reflectionPrompt: "Why does this explain both a radio mast and the glow of a hot poker, whose atoms jiggle thermally?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In an electromagnetic wave travelling forward, the electric and magnetic fields are..."
    options:
      - "Parallel to each other and to the direction of travel"
      - "Perpendicular to each other and both perpendicular to the direction of travel"
      - "Perpendicular to each other, with the electric field along the direction of travel"
      - "Randomly oriented, changing direction from moment to moment"
    correctIndex: 1
    feedback: "E and B oscillate in perpendicular planes, both transverse to the motion — and they rise and fall in step, each one's change feeding the other's existence."
  - type: MULTIPLE_CHOICE
    question: "Radio waves, visible light, and X-rays differ from one another in..."
    options:
      - "Their speed in vacuum"
      - "Whether they need a medium to travel"
      - "Their frequency and wavelength only — they are the same kind of wave"
      - "The kind of field they are made of: electric for radio, magnetic for X-rays"
    correctIndex: 2
    feedback: "One phenomenon, one vacuum speed c, one set of laws. From kilometre-long radio waves to gamma rays smaller than a nucleus, only frequency and wavelength change — exactly as your Apprentice spectrum lessons promised, and now you know why."
---

# Hook

Sound needs air. Ocean waves need water. A wave on a rope needs the rope. Every wave you studied in your Apprentice year was a disturbance *in something* — remove the medium and the wave is impossible. Now stand outside on a clear night and look up. Starlight has crossed trillions of kilometres of vacuum — the most perfect emptiness there is — to reach your eye.

A wave, waving in *nothing*. For two centuries that paradox tormented physics. The resolution is among the most beautiful arguments ever made: the wave carries its own medium with it, two fields locked in an eternal leapfrog, each one's dying breath giving birth to the other. You already know both rules of the leapfrog. Today you put them together.

# Lore Introduction

Selka has drawn the laboratory's heavy curtains, and the room is dark except for a single candle on the far bench. Between you and the flame: thirty metres of empty air.

"Account for what you are seeing," she says. "The flame is there. Your eye is here. Sound would need the air; cut the air and a bell falls silent — the Apprentice vacuum-jar lesson. But evacuate this room entirely and you would still see that candle." She lets the silence stretch. "Something is crossing the gap that does not need the gap to be anything at all."

She relights the lamps and pulls the slate forward. On it, from yesterday: *a changing magnetic field induces an electric field* — Faraday's needle slamming sideways. Beneath it she writes the partner she promised: *a changing electric field induces a magnetic field.*

"One rule was discovered with coils and galvanometers. The other was deduced by pure thought, by a Scotsman with the finest theoretical mind of his century. Together —" she draws an arrow chasing an arrow chasing an arrow, "— they let the fields run away from their charges entirely. Today, Senior, you learn what light *is*."

# Core Learning

## Concept Introduction

**The two induction rules.** You already own the first: Faraday's law — a *changing magnetic field induces an electric field*. It drives every generator: spin a magnet near a coil and the changing B conjures an E that pushes current. Maxwell supplied the mirror image: a *changing electric field induces a magnetic field*, just as a current does. (His reasoning: charge a capacitor and the growing field between the plates must complete the magnetic circuit that the charging current starts — the field's *change* acts like a current.)

**The leapfrog.** Now run the rules together, far from any charge. Suppose a magnetic field somewhere is dying away. Its change induces an electric field. But that electric field is itself *appearing* — changing — so it induces a magnetic field. Which is changing, so it induces an electric field... Each field perpetually regenerates the other. The disturbance needs no charges to sustain it and no medium to wave in: **the changing fields are the wave**. It bootstraps itself through perfect vacuum.

**The structure.** The self-sustaining solution is strict about its geometry:

- The electric field oscillates in one plane; the magnetic field in the perpendicular plane; both are perpendicular to the direction of travel — a **transverse wave**
- E and B rise and fall **in step**, each one's change feeding the other
- In vacuum the wave moves at exactly one speed, fixed by the electric and magnetic constants of free space: **c ≈ 3 × 10⁸ m/s**

When Maxwell computed that speed in 1864 from laboratory measurements of electricity and magnetism — no optics anywhere in the calculation — it came out equal to the measured speed of light. His conclusion stands among the greatest unifications in science: **light is an electromagnetic wave.** Radio, microwaves, infrared, visible light, ultraviolet, X-rays, gamma rays — the spectrum you memorised as an Apprentice is one phenomenon at different frequencies, all obeying **c = fλ**.

**Where the waves come from.** A static charge makes a static field; a uniformly moving charge drags a steady pattern along. Neither radiates. But **accelerate** a charge — shake it, oscillate it, slam it to a halt — and the field lines kink, and the kink ripples outward at c. An **antenna** is engineered acceleration: electrons sloshed up and down a conductor at, say, 100 million times per second, radiating a 100 MHz wave. Reception is the same physics reversed — the arriving wave's electric field grips the antenna's electrons and drives a tiny synchronized current, which amplifiers turn into music, data, or a picture.

## Why It Matters

You are soaking in this lesson: every photon of daylight, every Wi-Fi packet, every phone call, GPS fix, radar echo, microwave-heated meal, X-ray image, and fibre-optic bit is the field leapfrog at some frequency. Civilisation's entire wireless layer is applied Maxwell. The same physics sets hard limits engineers live by — antenna sizes scale with wavelength (half-wave dipoles: metre-scale for FM, centimetres for your phone), and the energy carried per photon scales with frequency, which is why radio passes through you harmlessly while X-rays demand a lead apron. And cosmically, electromagnetic waves are almost everything we know about the universe: until gravitational waves were detected, every scrap of astronomical knowledge had arrived by this one messenger.

## Worked Examples

**Example 1 — Radio wavelength.** An FM station broadcasts at 100 MHz.
λ = c/f = (3 × 10⁸) / (1.0 × 10⁸) = **3 m**.
A half-wave antenna is 1.5 m — door-height, which is why FM masts and old car aerials are the size they are.

**Example 2 — The frequency of green light.** Green light has λ ≈ 5 × 10⁻⁷ m (500 nm).
f = c/λ = (3 × 10⁸) / (5 × 10⁻⁷) = **6 × 10¹⁴ Hz** — six hundred trillion oscillations per second. Your eye is an antenna for fields leapfrogging that fast; no circuit could follow it, so the eye detects energy instead — a hint of the quantum story waiting in Module 3.

**Example 3 — Light's travel time.** The Sun is 1.5 × 10¹¹ m away.
t = d/c = (1.5 × 10¹¹) / (3 × 10⁸) = 500 s ≈ **8.3 minutes**.
Sunlight is always eight minutes old; the leapfrog crossed the entire vacuum gap unaided. The nearest star's light is four *years* old — every telescope is a time machine.

## Common Mistakes

- Thinking electromagnetic waves need a medium ("the aether") — the mutual regeneration of E and B is the whole mechanism; vacuum is their natural habitat
- Believing different parts of the spectrum are different kinds of wave — radio, light, and X-rays differ only in frequency and wavelength, never in nature or vacuum speed
- Drawing E and B parallel to each other or along the travel direction — both are transverse, mutually perpendicular, and in step
- Saying steady currents or uniformly moving charges radiate — only *accelerating* charges launch waves
- Using c for the wave's speed in glass or water — c is the vacuum speed; in media the wave slows (the refraction you met in Apprentice light lessons)
- Mixing up frequency and wavelength when applying c = fλ — as one rises the other must fall; their product is pinned at c

## Mental Model

Picture two acrobats crossing a chasm with no bridge: the first leaps from the second's shoulders, and while airborne becomes the platform from which the second leaps in turn — each landing made possible by the other's jump, hand over hand through empty air. E and B cross the vacuum exactly that way: B's fading is E's birth, E's rising is B's birth, locked at right angles, advancing at the one speed the laws of induction permit. The antenna is the springboard that starts the first jump; your eye, the far ledge where the acrobats finally land.

## Mini Summary

- A changing magnetic field induces an electric field (Faraday); a changing electric field induces a magnetic field (Maxwell) — together the fields regenerate each other and propagate with no medium
- The wave is transverse: E ⊥ B, both perpendicular to travel, in step, moving at c ≈ 3 × 10⁸ m/s in vacuum
- Light is an electromagnetic wave; the whole spectrum is one phenomenon obeying c = fλ, differing only in frequency
- Electromagnetic waves are radiated by accelerating charges; antennas transmit by oscillating electrons and receive by letting the wave drive electrons

# Guided Practice Quest

Selka snuffs the candle and replaces it with a spark-gap transmitter at one end of the bench and an unconnected loop of wire with a microscopic gap at the other. When she fires the spark, a tiny answering spark snaps in the distant loop — across ten metres of nothing.

"Hertz's own experiment, Senior. The wave is real and it crosses the gap. Three trials: first, tell me why this wave needs no air when every Apprentice wave did. Second, take a broadcast frequency and find the size of its wave — sizes matter; they set the length of every antenna ever built. Third, tell me what a charge must *do* to radiate at all. Spark gap or candle flame, the answer is the same."

# Solo Practice Quest

Write an investigation log (350–500 words) explaining what light is. Cover: the two induction rules and how together they allow a self-sustaining wave with no medium; the anatomy of the wave — orientation of E and B, transversality, the fixed vacuum speed c, and what Maxwell's calculation of that speed revealed; one worked application of c = fλ at a frequency of your choosing, commenting on the physical size of the wavelength; and how accelerating charges generate the waves, using the antenna as your example for both transmission and reception. Close by reflecting on the Apprentice electromagnetic-spectrum lessons: what did you memorise then that you can now *derive*?

# Integration

**Mathematics:** The leapfrog argument becomes rigorous as a pair of coupled differential equations — each field's rate of change driving the other's curl — which combine into the wave equation, with speed c = 1/√(ε₀μ₀) emerging from the constants. The strict perpendicularity of E, B, and the travel direction is the cross product from last lesson, now frozen into the structure of light itself.

**Engineering:** Antenna engineering is resonance applied to radiation: a dipole near half a wavelength sloshes its electrons in sympathy with the passing wave, which is why every band of the spectrum has its own antenna scale, from kilometre longwave masts to millimetre-wave 5G arrays. Waveguides, radar, satellite links, and fibre optics are all field-leapfrog plumbing — steering the acrobats down chosen paths.

# Lore Conclusion

Selka fires the spark gap one last time, and the answering snap comes back from the far loop — call and response across the empty air.

"Faraday found one rule with his coils. The second rule was found with no apparatus at all — deduced, on paper, because the equations demanded symmetry and their author trusted them." She wipes the slate clean of everything except four short lines: the two source laws of fields, and the two induction laws. "Four statements, Senior. You have now met every one of them separately — charges make electric fields; magnetic poles never come alone; changing B makes E; changing E makes B. Tomorrow we do what James Clerk Maxwell did: write all four together, and watch the whole of electricity, magnetism, and optics collapse into a single page."

She sets the chalk down. "*Maxwell's Synthesis.* Bring your finest attention. There are perhaps five moments in the history of natural philosophy this beautiful, and tomorrow is one of them."
