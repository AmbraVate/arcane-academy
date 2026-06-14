---
id: phy-app-m3-03
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m3
moduleTitle: "Module 3: Waves and Light"
moduleGlyph: "🌊"
moduleSortOrder: 3
topicSlug: wave_fundamentals
topicTitle: "Wave Fundamentals"
topicSortOrder: 1
title: "Reflection, Refraction, and Diffraction"
sortOrder: 3
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Describe reflection and the equal-angles law
  - Explain refraction as bending caused by a speed change
  - Describe diffraction and when it is strongest
integrationDomains: [music, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States the law of reflection (angle in = angle out, measured from the normal)
    - Explains refraction via the change of wave speed between media
    - States that diffraction is strongest when gap size is comparable to wavelength
    - Matches each behaviour to one everyday phenomenon
  keywords: [reflection, refraction, diffraction, normal, angle, speed change, gap, bend]
  modelAnswer: |
    At a boundary a wave can reflect, refract, or diffract. Reflection bounces the wave back
    with the angle of incidence equal to the angle of reflection (both measured from the
    normal) — echoes and mirrors. Refraction bends a wave that crosses into a medium where its
    speed changes — light slowing into glass bends toward the normal, which is how lenses work
    and why pools look shallow. Diffraction spreads waves passing through gaps or around edges,
    strongest when the gap is about one wavelength wide — why you hear around a doorway
    (metre-long sound waves) but cannot see around it (light's waves are half a micrometre).
guidedSteps:
  - id: phy-app-m3-03-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A light ray strikes a flat mirror at 35° from the normal. It reflects at ________° from the normal.
    inputConfig:
      placeholder: "35"
    markingRule:
      matchMode: CONTAINS
      accepted: ["35"]
      rejectedFeedback: "Law of reflection: angle of incidence = angle of reflection, both measured from the normal (the perpendicular to the surface). 35° in, 35° out."
    hint: "The two angles are always equal — that IS the law."
    reflectionPrompt: "Why do physicists measure from the normal rather than from the surface?"
  - id: phy-app-m3-03-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A straw in a glass of water looks bent at the surface. The cause is:
    inputConfig:
      options:
        - "The water physically bends the straw"
        - "Light changes speed between water and air, so the rays bend — refraction"
        - "Light reflects off the straw twice"
        - "The eye cannot focus through glass"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Light changes speed between water and air, so the rays bend — refraction"]
      rejectedFeedback: "Light leaving water speeds up and bends away from the normal; your brain traces the rays back along straight lines to a false position. The straw is innocent — refraction did it."
    hint: "What happens to light's speed at the water-air boundary?"
    reflectionPrompt: "Why do pools and rivers look shallower than they are — and why is that worth knowing before diving?"
  - id: phy-app-m3-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You can hear a conversation through an open doorway before you can see the speakers. Using diffraction and the wavelengths involved, explain why sound bends around the corner but light effectively doesn't. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [diffract, wavelength, gap, metre, small, spread, comparable]
      rejectedFeedback: "Diffraction is strong when wavelength ≈ gap size. Speech waves are roughly a metre long — comparable to a doorway — so they spread widely. Light's wavelength is ~0.0000005 m, vastly smaller than the door, so it passes essentially straight."
    hint: "Compare each wave's wavelength with the width of a door."
    reflectionPrompt: "Bass notes flood around corners better than treble — predict why, using the same rule."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Refraction occurs because a wave entering a new medium changes its:"
    options: ["Frequency", "Speed", "Amplitude", "Direction of vibration"]
    correctIndex: 1
    feedback: "The speed change is the cause; the bend is the effect (when the wave arrives at an angle). Frequency is fixed by the source and survives the crossing unchanged."
  - type: MULTIPLE_CHOICE
    question: "Diffraction through a gap is most dramatic when the gap is:"
    options:
      - "Much larger than the wavelength"
      - "About the same size as the wavelength"
      - "Much smaller than anything"
      - "Painted black"
    correctIndex: 1
    feedback: "Gap ≈ λ gives maximum spreading — the wave exits as near-circular ripples. Much larger gaps let the wave pass nearly straight."
---

# Hook

Three puzzles, one lesson. Why can you hear someone around a corner but not see them? Why does a swimming pool's deep end look deceptively shallow? And how does a bat, in absolute darkness, catch a moth on the wing?

The answers are the three things every wave can do when it meets something: **bounce** (reflection), **bend at a crossing** (refraction), and **spread around edges** (diffraction). Every mirror, lens, echo, rainbow, lens-shaped raindrop, whispering gallery, and ultrasound scan is one of these three behaviours — usually wearing a costume.

Learn the three, and an enormous amount of the visible (and audible) world clicks into place.

# Lore Introduction

Liora calls it the Boundary Gauntlet: three stations along the Resonance Hall's great ripple-pool, and you must explain each before moving on. Station one: a brass wall in the water — ripples strike it slantwise and rebound slantwise, like billiards. Station two: a submerged glass shelf making the water suddenly shallow — ripples crossing onto it visibly *kink*, changing direction as they slow. Station three: a barrier with a narrow gate — ripples squeeze through and bloom out the far side in perfect semicircles, fanning into water the straight path would never touch. "Bounce, bend, bloom," Liora chants, tapping each station. "Master the gauntlet, and I will show you why your reflection waits for you in every still pond — and why no wall in this Academy can keep out a deep bass note."

# Core Learning

## Concept Introduction

**Reflection — the bounce.** A wave striking a boundary rebounds. The law: *angle of incidence = angle of reflection*, both measured from the **normal** (the line perpendicular to the surface at the strike point). Smooth surfaces reflect tidily (mirrors, echoes off cliffs); rough surfaces scatter the bounce every which way — which is why you can't see your face in a brick.

**Refraction — the bend at a crossing.** When a wave crosses into a medium where it travels at a *different speed*, and arrives at an angle, it changes direction:

- Slowing down → bends **toward** the normal (light entering glass or water)
- Speeding up → bends **away from** the normal (light leaving water)
- Arriving head-on (along the normal) → slows or speeds but doesn't bend
- The **frequency never changes** (the source set it); the wavelength adjusts: λ = v/f

The classic mechanism-picture: a marching column hitting mud at an angle — the first ranks to reach the mud slow while the rest still march quickly, so the whole column wheels. Lenses, your eye's cornea, mirages, and the bent straw are all refraction.

**Diffraction — the bloom.** Waves passing through a gap or around an edge spread into the shadow region. Strength depends on the comparison **gap size vs wavelength**:

- Gap ≈ λ → dramatic spreading (semicircular ripples)
- Gap ≫ λ → wave passes nearly straight, slight edge-fraying only

Sound (λ ~ 0.1–10 m) diffracts generously around doors and corners; light (λ ~ 0.0000005 m) barely diffracts at door scale — hence hearing without seeing.

## Why It Matters

- These three behaviours are the working principles of: mirrors and telescopes (reflection), every lens from spectacles to cameras (refraction), and antenna design, harbour engineering, and concert-hall acoustics (diffraction).
- Sonar, radar, ultrasound imaging, and seismic surveying are *engineered reflection*: send a wave, time the bounce, map the world.
- Refraction explains daily optical lies — shallow-looking pools, shimmering roads, stars twinkling — and the corrections (spear-fishers aim low; astronomers build above the atmosphere).

## Worked Examples

**Example 1: Depth by echo**
A ship's sonar pings the seabed: the echo returns 0.8 s later. Sound in seawater ≈ 1500 m/s. Distance there-and-back = 1500 × 0.8 = 1200 m, so depth = 600 m. Reflection plus a stopwatch maps the ocean floor — and the same arithmetic runs bat hunting, radar, and medical ultrasound.

**Example 2: The coin that appears**
Place a coin in an empty mug and step back until the rim just hides it. Pour in water *without moving*: the coin swims into view. Light from the coin now bends (away from the normal) as it exits the water surface, arcing over the rim into your eye. Your brain, trusting straight lines, sees the coin floated upward. Refraction, kitchen edition.

**Example 3: Why coastal walls have gaps that "leak"**
A harbour wall leaves a 20 m entrance. Storm swell of wavelength ~100 m arrives: gap ≪ λ — the long waves barely fit and mostly reflect, but what enters diffracts strongly, fanning around the inner harbour. Short wind-chop (λ ~ 5 m) streams through the same gap nearly straight, troubling only boats in line with the entrance. Harbour designers calculate berth positions from exactly this contrast.

## Common Mistakes

- **Measuring reflection angles from the surface** — always from the normal; examiners and optics both insist.
- **Saying refraction changes frequency** — frequency is the source's signature and survives every crossing; *speed and wavelength* change.
- **"Light bends because the medium is denser"** — nearly right but loose: the cause is the *speed change* (optical density), and head-on arrivals don't bend at all.
- **Expecting diffraction everywhere** — through gaps far wider than λ, waves run essentially straight; the effect needs comparable scales.
- **Forgetting roughness scatters** — the reflection law holds at every point, but rough surfaces aim each point's bounce differently; "diffuse reflection" is law-abiding chaos.

## Mental Model

A travelling wave is **a marching column of soldiers**. *Reflection*: the column reaches a cliff face and counter-marches away at the mirrored angle — drill-square tidy if the ground is smooth, a scramble if it's rubble. *Refraction*: the column crosses from parade ground into ploughed mud at a slant — inside ranks slow first, the line wheels toward the mud's normal; back onto firm ground, it wheels away again. *Diffraction*: the column funnels through a town gate — through a wide boulevard gate it re-forms and marches on straight, but through a gate barely wider than one rank it spills into the square beyond in a spreading fan, soldiers flooding left and right into streets the straight march would never have reached.

## Mini Summary

- ✔ Reflection: angle in = angle out, from the normal; smooth = mirror, rough = scatter
- ✔ Refraction: speed change at a crossing bends the path (toward normal when slowing); f never changes
- ✔ Diffraction: spreading through gaps/around edges; strongest when gap ≈ λ
- ✔ Sound diffracts around doors (λ ~ metres); light doesn't (λ ~ half a micron)
- ✔ Echo-timing (reflection + stopwatch) measures depths, distances, and babies

# Guided Practice Quest

Work through the guided steps to mirror a 35° ray, exonerate a bent straw, and let wavelength explain who hears what around a doorway.

# Solo Practice Quest

Run the gauntlet at home, one experiment per behaviour: (1) *Reflection*: bounce a torch beam off a mirror onto a wall; verify equal angles roughly with a protractor (or folded-paper angle). (2) *Refraction*: do the coin-in-the-mug trick; then look at a straw in water from the side and sketch the apparent vs real positions. (3) *Diffraction*: have someone speak from another room through a doorway; compare how well low hums vs sharp 's' sounds carry around the corner, and explain with gap-vs-λ reasoning (estimate both wavelengths via λ = 340/f). Write up all three with diagrams, each labelled with its behaviour and one sentence of mechanism.

# Integration

**Music**: Concert halls are buildings designed around this lesson — reflectors above the stage to bounce sound outward, diffusing surfaces to scatter harshness, and careful management of bass diffraction so low notes don't pool in corners. The "whispering gallery" of St Paul's is reflection played as a parlour trick.

**Engineering**: Fibre-optic cables imprison light by *total internal reflection* (refraction's extreme case — a Senior-tier treat); radar and 5G antenna engineers shape beams with diffraction mathematics; and harbour, breakwater, and flood-barrier design is applied water-wave diffraction with national budgets attached.

# Lore Conclusion

You clear the gauntlet — bounce, bend, bloom, each explained with angles, speeds, and wavelengths — and Liora grants you the Hall's odd little salute, two fingers tapped against a tuning fork. "Every harbour, every mirror, every lens in the Academy obeys those three," she says, leading you to the Hall's far door, behind which something vast and bronze gleams in the half-light: the Academy's great bell, twin to the tower's. "Wave fundamentals: complete. Next we give the rules a voice." She rests your palm flat against the cold bronze and strikes the bell softly with a leather mallet — and you feel the note arrive through your hand a heartbeat before you hear it. "The most human of all waves, apprentice. You've been making it since the day you were born. Tomorrow, we find out what sound actually *is*."
