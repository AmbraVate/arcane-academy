---
id: phy-app-m3-01
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m3
moduleTitle: "Module 3: Waves and Light"
moduleGlyph: "🌊"
moduleSortOrder: 3
topicSlug: wave_fundamentals
topicTitle: "Wave Fundamentals"
topicSortOrder: 1
title: "What Is a Wave?"
sortOrder: 1
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - Define a wave as a transfer of energy without transfer of matter
  - Distinguish transverse from longitudinal waves
  - Identify the medium (or its absence) for common waves
integrationDomains: [music, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States that waves carry energy, not matter
    - Gives a demonstration that the medium stays put (cork on water, crowd wave)
    - Distinguishes transverse (vibration across travel) from longitudinal (vibration along travel)
    - Classifies sound and light correctly, including light needing no medium
  keywords: [wave, energy, matter, medium, transverse, longitudinal, vibration, transfer]
  modelAnswer: |
    A wave is a travelling disturbance that carries energy from place to place without
    permanently moving matter: a cork on a pond bobs up and down as ripples pass but goes
    nowhere, proving the water itself stays put. In transverse waves (water ripples, light,
    a shaken rope) the vibration is perpendicular to the direction of travel; in longitudinal
    waves (sound, a pushed slinky) it is back-and-forth along the travel direction. Mechanical
    waves need a medium to vibrate; light is the famous exception, crossing the vacuum of
    space with no medium at all.
guidedSteps:
  - id: phy-app-m3-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A seagull floats far out at sea as waves pass beneath it toward the shore. Over the next minute the seagull mainly:
    inputConfig:
      options:
        - "Surfs steadily toward the beach with the waves"
        - "Bobs up and down, staying in roughly the same place"
        - "Drifts out to sea against the waves"
        - "Spins in circles"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Bobs up and down, staying in roughly the same place"]
      rejectedFeedback: "The wave SHAPE travels shoreward; the water (and gull) mostly oscillates in place. Waves move energy, not the medium — this single observation is the heart of the lesson."
    hint: "Does the water itself travel to the beach, or does the disturbance?"
    reflectionPrompt: "If the water did travel with the waves, what would happen to the sea behind them?"
  - id: phy-app-m3-01-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      In a stadium crowd-wave ('Mexican wave'), what travels around the stadium?
    inputConfig:
      options:
        - "The spectators"
        - "The pattern of standing-and-sitting — a disturbance carrying energy and information"
        - "The seats"
        - "Nothing actually travels"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The pattern of standing-and-sitting — a disturbance carrying energy and information"]
      rejectedFeedback: "Each person just stands and sits in place (the medium oscillates); the travelling thing is the PATTERN. Every wave in physics works exactly like this."
    hint: "Each individual person ends up back in their own seat..."
    reflectionPrompt: "Is the crowd-wave transverse or longitudinal? (Which way do people move relative to the wave's travel?)"
  - id: phy-app-m3-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A slinky can demonstrate both wave types. In 2–3 sentences, describe how you'd move your hand to create (a) a transverse wave and (b) a longitudinal wave along it, and state which everyday wave each one models.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [side to side, up and down, push, pull, along, perpendicular, sound, light]
      rejectedFeedback: "Transverse: flick the hand side-to-side (or up-down), across the slinky's length — models light and water ripples. Longitudinal: push-pull along the slinky, making compressions travel — models sound."
    hint: "Across the slinky for one type; along it for the other."
    reflectionPrompt: "In the longitudinal case, what exactly travels down the slinky?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "All waves transfer:"
    options: ["Matter", "Energy", "Both matter and energy", "Neither"]
    correctIndex: 1
    feedback: "Energy (and information) travels; the medium merely oscillates about its home position. That's the defining property of a wave."
  - type: MULTIPLE_CHOICE
    question: "Sound waves in air are:"
    options:
      - "Transverse — air vibrates across the travel direction"
      - "Longitudinal — air vibrates back and forth along the travel direction"
      - "Neither — sound is not a wave"
      - "Transverse in cold air, longitudinal in warm"
    correctIndex: 1
    feedback: "Sound is a chain of compressions and rarefactions: air molecules shuttle back-and-forth along the direction the sound travels."
---

# Hook

When an earthquake strikes Japan, sensors in California feel it minutes later. Did rock travel five thousand miles across the Pacific? Of course not — every atom of seafloor is, give or take, where it always was. Yet something undeniably *arrived*: enough energy to twitch needles, sometimes enough to raise tsunamis.

This is the strange and beautiful trick of waves: they move energy and information across the world while the stuff they travel through merely *jiggles in place*. The voice reaching your ear moves no parcel of air from speaker to listener; the light reaching your eye from a star has crossed trillions of kilometres of literally nothing.

One idea — a travelling disturbance — explains ripples, music, earthquakes, wifi, and sunlight. This module is that idea, unfolded. Today: what a wave is, and the two shapes it comes in.

# Lore Introduction

Module Three begins at the Resonance Hall, the Observatory's strangest wing — a long gallery containing a stretched silver rope, a great shallow reflecting pool, and rack upon rack of tuning forks. The Hall's keeper, Magus Liora — Thorne's opposite in temperament, quick and bright-eyed — greets you by dropping a single pebble into the pool's exact centre. "Watch the leaf," she says. A fallen leaf floats halfway across; the rings spread, reach it, and the leaf bobs — once, twice — and settles, *not one finger-width closer to the edge*. "The ring reached the wall," Liora says. "The leaf went nowhere. So tell me, apprentice of Thorne's, you who can audit energy —" her eyes glitter, "— *what travelled?*"

# Core Learning

## Concept Introduction

A **wave** is a disturbance that travels, carrying **energy** (and information) from one place to another **without any net transport of matter**. The substance it travels through — the **medium** — oscillates about its home position and stays put.

Evidence is everywhere: the bobbing cork, the seagull riding swells, the stadium crowd-wave in which every spectator ends the evening in their own seat though "the wave" lapped the ground six times.

**Two families, by direction of vibration:**

| Type | Medium vibrates... | Examples |
|------|--------------------|----------|
| **Transverse** | *Across* (perpendicular to) the travel direction | Water ripples, shaken rope, light, all EM waves |
| **Longitudinal** | *Along* the travel direction (push–pull) | Sound, compression pulses in a slinky, some earthquake waves |

A slinky shows both: flick sideways → humps travel (transverse); push-pull along it → bunched-up *compressions* travel, separated by stretched *rarefactions* (longitudinal). Sound in air is exactly the slinky's second trick: travelling crowds and thinnings of air molecules.

**Mediums — and the great exception.** Mechanical waves (water, sound, seismic) *need* a medium; no air, no sound — a bell in a vacuum jar rings silently. But **light needs no medium**: it crosses empty space for billions of years untroubled. (What, then, is *waving*? Hold that question — it has one of the best answers in physics, several lessons away.)

## Why It Matters

- Waves are physics' delivery service: nearly all the energy and *all* the information you receive — sound, sight, radio, wifi — arrives by wave.
- The matter-stays/energy-moves principle explains real phenomena from tsunami behaviour to why floating debris doesn't wash ashore with each swell.
- Transverse-vs-longitudinal is not taxonomy for its own sake: it determines what waves can do (only transverse waves can be *polarised* — the trick behind sunglasses and 3D cinema).

## Worked Examples

**Example 1: The whisper across the library**
A whisper crosses a silent library to a listener 10 m away. No air travels — a draught would be felt! Instead, the whisperer's vocal cords nudge nearby air; those molecules shove their neighbours and bounce back; the *shove* relays at ~340 m/s. What arrives is rhythm and energy: pattern, not parcel.

**Example 2: Earthquake forensics**
Earthquakes emit both families at once: P-waves (longitudinal, fast) and S-waves (transverse, slower). Seismometers feel the P first, the S after a gap — and the gap's length reveals the distance to the quake. Three stations, three distances, one located epicentre. Wave classification, saving lives via geometry.

**Example 3: The silent bell**
Classic demonstration: an electric bell sealed in a jar, hammer visibly striking. Pump the air out — the ringing fades to nothing while the hammer still strikes. Sound's medium has been confiscated. Let light from a lamp shine through the same evacuated jar: it passes utterly unbothered. One experiment, both halves of the medium story.

## Common Mistakes

- **Believing the medium travels** — the sea does not move to the beach; "the wave came to me" means the *pattern* did.
- **Thinking waves carry no "stuff" at all, ever** — they carry energy and momentum, which is real cargo: waves erode cliffs and sunburn skin.
- **Classifying sound as transverse** — sound in air is always longitudinal; the squiggle drawn on screens is a *graph* of pressure, not a picture of the wave's shape.
- **"All waves need a medium"** — light (and the whole electromagnetic family) does not; this exception is load-bearing for the rest of the module.
- **Confusing the wave's speed with the medium's jiggle speed** — the ripple crosses the pond far faster than any water molecule moves.

## Mental Model

Think of a wave as **a rumour passing down a line of people**. Each person leans to their neighbour, whispers, and returns upright — nobody walks anywhere, yet the rumour crosses the hall in seconds, carrying information and (judging by the gasps) energy. Transverse rumours: each person sways side-to-side as it passes. Longitudinal rumours: each person nudges forward and back. The line is the medium; the rumour is the wave; and the rumour's speed depends on how the line is built — not on how dramatic the original whisper was.

## Mini Summary

- ✔ A wave = travelling disturbance: energy and information move, matter stays
- ✔ Transverse: vibration across travel (light, ripples); longitudinal: along travel (sound)
- ✔ Mechanical waves need a medium; light famously does not
- ✔ Compressions and rarefactions are the longitudinal wave's humps and dips
- ✔ Wave speed belongs to the medium, not to the jiggle of any one particle

# Guided Practice Quest

Work through the guided steps to keep a seagull honest, anatomise a stadium wave, and conduct both wave families down one slinky.

# Solo Practice Quest

Run the pond experiment at home: a basin, bowl or bath of still water, a floating marker (cork, bottle-cap, leaf), and a single drip from a height. Observe and record: (1) what the marker does as rings pass — does it migrate?; (2) estimate the ripple speed (distance to edge ÷ time — Module One skills); (3) drip twice in quick succession and describe what the marker does. Then classify five waves from your day (a sound you heard, light you saw, plus three of your choosing — a flag's flutter? a queue shuffling forward?) as transverse, longitudinal, or not-actually-a-wave, with one-line justifications.

# Integration

**Music**: Every instrument is a machine for making a medium oscillate on purpose — strings transversely, air columns longitudinally — and what reaches the audience is always the longitudinal relay through air. Musicianship is, physically, the craft of sculpting waves.

**Biology**: Your senses are wave receivers: the cochlea is a longitudinal-wave decoder, the retina a transverse-wave camera. Medicine listens to waves too — ultrasound images babies with sound's reflections, the topic of a coming lesson.

# Lore Conclusion

You give Liora your answer at the pool's edge: *the energy travelled; the water only danced in place*. She fishes the leaf out and presents it to you with mock ceremony, perfectly dry on top. "Thorne's audits have their uses after all." She crosses to the racks and takes down a single tuning fork, strikes it on her palm, and holds it to the pool's surface — instantly the water blooms into rings, the Hall fills with a pure soft tone. "But energy alone is a dull ledger. Waves have *character* — this fork sings one note and no other, forever. Tomorrow you learn the three numbers that give every wave its voice: how tall, how long, and how often."
