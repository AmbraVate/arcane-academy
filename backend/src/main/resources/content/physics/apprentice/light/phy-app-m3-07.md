---
id: phy-app-m3-07
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m3
moduleTitle: "Module 3: Waves and Light"
moduleGlyph: "🌊"
moduleSortOrder: 3
topicSlug: light
topicTitle: "Light"
topicSortOrder: 3
title: "Light Rays, Shadows, and Seeing"
sortOrder: 7
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - State that light travels in straight lines at enormous speed
  - Explain shadows and eclipses using straight-line rays
  - Describe seeing as light entering the eye from sources or reflectors
integrationDomains: [biology, history]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States that light travels in straight lines (in a uniform medium) at ~3×10⁸ m/s
    - Explains shadow formation from straight-line propagation
    - Distinguishes luminous sources from reflectors
    - Describes vision as light entering the eye, not rays leaving it
  keywords: [straight line, ray, shadow, luminous, reflect, eye, speed of light, source]
  modelAnswer: |
    Light travels in straight lines through a uniform medium, at about 300,000 km per second —
    fast enough to circle Earth seven times in a second. Straight-line travel explains shadows:
    an opaque object blocks the rays behind it, leaving a light-starved region shaped by simple
    geometry, which is also how eclipses work at planetary scale. We see luminous objects (the
    Sun, flames, screens) by their own light, and everything else by reflected light. Vision is
    light ENTERING the eye — the ancient idea of eye-beams reaching out is backwards, as the
    darkness of a cave instantly proves.
guidedSteps:
  - id: phy-app-m3-07-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You see this lesson's text because:
    inputConfig:
      options:
        - "Your eyes send out beams that scan the screen"
        - "The screen emits light that travels in straight lines into your eyes"
        - "The text vibrates the air between you"
        - "Your brain projects the image outward"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The screen emits light that travels in straight lines into your eyes"]
      rejectedFeedback: "Vision is incoming, never outgoing: luminous objects (like screens) emit light; everything else reflects it; your eye is a detector. If eyes emitted beams, you could read in a pitch-black cave."
    hint: "What single observation about dark rooms settles this ancient debate?"
    reflectionPrompt: "The eye-beam theory survived for centuries. What everyday experience made it FEEL right?"
  - id: phy-app-m3-07-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A solar eclipse occurs when:
    inputConfig:
      options:
        - "The Earth's shadow falls on the Moon"
        - "The Moon's shadow falls on the Earth — the Moon blocks the Sun's straight-line rays"
        - "The Sun briefly dims"
        - "Clouds align with the Sun"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The Moon's shadow falls on the Earth — the Moon blocks the Sun's straight-line rays"]
      rejectedFeedback: "Solar eclipse: Moon between Sun and Earth, casting its shadow on us. (Earth's shadow on the Moon is a LUNAR eclipse.) Both are shadows at astronomical scale — straight-line light made majestic."
    hint: "Whose shadow lands on whom?"
    reflectionPrompt: "Why does a total solar eclipse only darken a narrow strip of the Earth at a time?"
  - id: phy-app-m3-07-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Walking toward a streetlamp at night, your shadow changes. Describe how (length and direction) as you approach, pass under, and walk away — and explain each change using straight-line rays. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [shorter, longer, behind, front, straight, angle, under, rays]
      rejectedFeedback: "Approaching: the shadow stretches behind you, shortening as the light angle steepens. Directly beneath: a small puddle of shadow at your feet (rays come from above). Walking away: it flips ahead of you and lengthens as the rays arrive ever more obliquely. All pure straight-line geometry."
    hint: "Draw the lamp, your head, and one straight ray grazing your head to the ground."
    reflectionPrompt: "Why are noon shadows short and sunset shadows enormous, by the same geometry?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Light's speed in a vacuum is approximately:"
    options: ["340 m/s", "300,000 m/s", "300,000 km/s", "Infinite"]
    correctIndex: 2
    feedback: "About 3 × 10⁸ m/s = 300,000 km/s — Earth to Moon in 1.3 s, Sun to Earth in 8 minutes. Vast, but finite, and that finiteness has consequences."
  - type: MULTIPLE_CHOICE
    question: "Which list contains only LUMINOUS objects (emitting their own light)?"
    options:
      - "The Sun, a candle flame, a lit screen"
      - "The Moon, a mirror, a white wall"
      - "A book, the Moon, a cat's eyes"
      - "A diamond, snow, the sky"
    correctIndex: 0
    feedback: "Sun, flames, and screens generate light. The Moon, mirrors, walls, snow — and famously 'glowing' cat's eyes — are all reflectors, visible only by borrowed light."
---

# Hook

The sunlight warming your face right now is eight minutes old — it left the Sun before you started reading this page and crossed 150 million kilometres of vacuum in a dead-straight line to find you. Look up at the night sky and you're reading even older mail: the light of some stars set out before the Roman Empire.

Light is the fastest thing the universe permits, and — in everyday circumstances — the straightest. Those two facts, speed and straightness, are nearly the whole of today's lesson, yet they explain an absurd amount: why shadows have crisp edges, why eclipses can be predicted to the second centuries ahead, how a pinhole can paint a picture, and what *seeing* actually is — a question humanity got embarrassingly wrong for a thousand years.

# Lore Introduction

The Hall of Optics opens off the Resonance Hall through a door Liora unlocks with evident ceremony — inside, all is black velvet and brass, and a single shuttered lantern. "Before mirrors, before lenses, before colour," she says, "the first masters established two facts, and everything in this hall is built upon them." She opens the shutter a crack: a blade of light crosses the dark room, ruler-straight, picked out by drifting dust. "Fact one." She places her hand in the beam; a crisp hand-shadow snaps onto the far wall. "Fact two: I cannot make this messenger swerve, and no one has ever caught it in transit. The old texts claimed our eyes send out feeler-rays to touch the world." She shuts the lantern fully; the darkness is total and instant. "If your eyes are beaming, apprentice — read me the inscription on the far wall."

# Core Learning

## Concept Introduction

**Light travels in straight lines** (in a uniform medium — bending needs a boundary, as refraction taught you). Physicists draw its paths as **rays**: arrows along the travel direction. The dust-picked sunbeam, the laser's line, the crisp edge of a shadow — all testimony.

**Light is staggeringly fast: c ≈ 3 × 10⁸ m/s** (300,000 km/s) in vacuum. Earth to Moon: 1.3 s. Sun to Earth: 8 min 20 s. Around the planet: 7 times per second. Finite, though — and the finiteness means *looking far is looking back in time*, from satellite-TV lag to ancient starlight.

**Shadows are geometry.** An opaque object blocks rays; the unlit region behind it is the shadow, its shape ruled by straight lines from the source past the object's edges:

- Small (point-like) source → sharp shadow
- Extended source → a dark core (**umbra**) fringed by partial shadow (**penumbra**) where only some of the source is blocked
- **Eclipses** are the same diagram at celestial scale: Moon shadowing Earth (solar) or Earth shadowing Moon (lunar) — predictable to the second *because* the lines are straight.

**Seeing = light entering the eye.** Two kinds of visible things:

| Type | Visible by... | Examples |
|------|---------------|----------|
| **Luminous** | Emitting their own light | Sun, flames, screens, LEDs |
| **Non-luminous** | Reflecting light from elsewhere | Moon, books, faces, everything else |

The ancient "eye-beam" (extramission) theory — sight as rays sent *out* by the eye — is refuted by every dark room: no light in, nothing seen, however hard you stare.

## Why It Matters

- Straight-line reasoning is the working tool of all optics: every mirror, lens, camera, and telescope diagram you'll ever draw is rays and geometry.
- Shadow geometry runs sundials, architecture (light planning), solar-panel siting, and eclipse science; historically it measured the Earth itself (Eratosthenes, two shadows, one summer noon).
- "Looking far = looking back in time" is foundational to astronomy: the finite c turns telescopes into time machines.

## Worked Examples

**Example 1: The pinhole camera**
A box with a pinhole in one face and tracing paper opposite: an upside-down image of the bright world appears on the paper. Why inverted? Rays from the scene's top travel straight *down* through the hole to the screen's bottom; bottom rays go up. No lens, no electronics — straight lines alone build an image. (Your eye is a pinhole camera that hired a lens; the image on your retina is upside-down too, and your brain quietly flips it.)

**Example 2: Sizing the lamp-post shadow**
A 1.8 m person stands 4 m from the base of a 6 m lamp. Similar triangles (straight rays!): shadow length s satisfies s/(s+4) = 1.8/6 → s ≈ 1.7 m. Surveyors, film lighting crews, and solar architects run this triangle daily — it's also how ancient geometers measured pyramids without climbing them.

**Example 3: Predicting an eclipse**
Because rays are straight and orbits are known, the Moon's umbra can be projected onto Earth's surface years ahead: path width ~150 km, totality a few minutes, timed to seconds. The 1919 eclipse — used to test whether *gravity itself* can bend light (it can, very slightly; Einstein's fame dates from that headline) — was scheduled this way. The exception that proved the straight-line rule needed the rule to find it.

## Common Mistakes

- **Eye-beams** — vision is incoming light; the dark-room test ends the debate.
- **"The Moon shines"** — it reflects; lunar light is sunlight on a grey rock, redirected.
- **Drawing rays without direction arrows** — a ray's arrow records the travel direction; reversed arrows scramble mirror and lens diagrams later.
- **Expecting light to need a medium** — sound's rules don't transfer; vacuum is light's favourite road (Module lesson 1 flagged this exception, and it stays load-bearing).
- **Thinking shadows are "things"** — a shadow is an *absence*, a light-starved region; it has geometry but no substance, and it can move faster than light without breaking any law (nothing travels).

## Mental Model

Treat every light source as **a fountain spraying perfectly straight javelins in all directions, at the cosmic speed limit**. Whatever a javelin strikes, it either soaks in, bounces off (that object is now visible — it's relaying javelins to any eye in line), or passes through. A shadow is just the dry patch behind whatever caught the javelins. And your eye? A bucket, not a hose: it catches javelins, never throws them. All of optics — mirrors, lenses, rainbows — is learning the few honest tricks that can redirect a javelin in flight.

## Mini Summary

- ✔ Light travels in straight lines (rays) at c ≈ 3 × 10⁸ m/s
- ✔ Finite speed: to look far away is to look into the past
- ✔ Shadows and eclipses are straight-line geometry — umbra, penumbra, predictability
- ✔ Luminous objects emit; everything else is seen by reflection
- ✔ Vision is light entering the eye — the dark room settles it forever

# Guided Practice Quest

Work through the guided steps to retire the eye-beam theory, assign each eclipse its shadow, and choreograph your own streetlamp shadow with nothing but straight lines.

# Solo Practice Quest

Build the oldest camera in the world: a pinhole viewer (box or tube, foil with a pin-prick at one end, tracing/greaseproof paper screen at the other, eye-hood of dark cloth). Aim it at a bright window or lamp and record: (1) the image's orientation and a ray diagram explaining it; (2) what happens to brightness and sharpness when you enlarge the pinhole — explain with rays; (3) what happens with TWO pinholes. Then the geometry challenge: use shadow-and-stick similar triangles to measure something tall near you (tree, building, lamp-post) without leaving the ground; show the triangle, the measurements, and your result with an uncertainty estimate.

# Integration

**Biology**: The eye runs the pinhole principle with upgrades — an adjustable aperture (iris), a refracting lens for brightness-without-blur, and a sensor (retina) whose image is inverted exactly as your tracing paper's. Octopus and human eyes evolved this design independently: straight-line optics is so constraining that evolution found the same answer twice.

**History**: Shadow geometry is humanity's oldest precision science: Eratosthenes measured Earth's circumference (~240 BC) from two noon shadows; eclipse records anchor ancient chronology; the camera obscura seeded both modern art (tracing masters) and photography. The straight line was civilisation's first scientific instrument.

# Lore Conclusion

In the velvet dark, Liora reopens the shutter a crack and hands you a polished brass plate. "Two facts, you now own: straight, and swift." You angle the plate; the blade of light leaps obediently across the hall to wherever you aim it, and the dust swirls in its new path. Liora watches the redirected beam with the expression of a chess player revealing the next gambit. "Notice what you just did, apprentice. You could not bend the messenger — but with the right surface, you *commanded* it." She produces from a velvet case a curved mirror, a stack of glass discs, and a prism that scatters a tiny accidental rainbow across her sleeve. "Tomorrow: the honest tricks. Mirrors that lie usefully, glass that gathers fire — the bending of the unbendable."
