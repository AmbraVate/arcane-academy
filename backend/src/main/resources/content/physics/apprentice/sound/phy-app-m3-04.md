---
id: phy-app-m3-04
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m3
moduleTitle: "Module 3: Waves and Light"
moduleGlyph: "🌊"
moduleSortOrder: 3
topicSlug: sound
topicTitle: "Sound"
topicSortOrder: 2
title: "How Sound Travels"
sortOrder: 4
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - Describe sound as a longitudinal pressure wave produced by vibration
  - Compare the speed of sound in gases, liquids, and solids
  - Explain why sound cannot travel through a vacuum
integrationDomains: [biology, music]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States that all sound begins with a vibrating source
    - Describes compressions and rarefactions relayed through the medium
    - Orders sound speed correctly — fastest in solids, slowest in gases
    - Explains the vacuum's silence by the absence of a medium
  keywords: [vibration, compression, rarefaction, medium, longitudinal, vacuum, 340, solid]
  modelAnswer: |
    Every sound starts with something vibrating — a string, a cone, vocal cords. The vibration
    shoves nearby air molecules together (compressions) and apart (rarefactions), and these
    pressure ripples relay outward as a longitudinal wave at about 340 m/s in air. Sound needs
    a medium: in a vacuum there is nothing to compress, so there is silence. The stiffer and
    more connected the medium, the faster the relay — about 1500 m/s in water and 5000 m/s in
    steel, which is why a rail 'hears' a distant train before the air does.
guidedSteps:
  - id: phy-app-m3-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Sound travels FASTEST through:
    inputConfig:
      options:
        - "A vacuum"
        - "Air"
        - "Water"
        - "Steel"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Steel"]
      rejectedFeedback: "Tightly bonded particles relay the push fastest: steel ~5000 m/s, water ~1500 m/s, air ~340 m/s. A vacuum carries no sound at all — nothing to push."
    hint: "Whose particles are most strongly connected to their neighbours?"
    reflectionPrompt: "Why does putting your ear to a desk make distant taps so loud and quick?"
  - id: phy-app-m3-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      You see lightning, then hear thunder 6 seconds later. With sound at ~340 m/s (and light effectively instant), the storm is about ________ km away.
    inputConfig:
      placeholder: "2"
    markingRule:
      matchMode: CONTAINS
      accepted: ["2", "2.0", "2km"]
      rejectedFeedback: "d = v × t = 340 × 6 ≈ 2000 m ≈ 2 km. Rule of thumb: every 3 seconds of delay ≈ 1 km."
    hint: "Distance = speed × time. The light's travel time is negligible."
    reflectionPrompt: "Why is it safe to treat the light as arriving instantly in this calculation?"
  - id: phy-app-m3-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      An astronaut's drill slips and clangs against the space station's hull. Her crewmate floating two metres away OUTSIDE hears nothing, but a crewmate INSIDE, touching the hull, hears it clearly. Explain both facts. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [vacuum, no medium, no air, hull, metal, solid, conduct]
      rejectedFeedback: "Space is a vacuum — no medium between drill and floating crewmate, so no sound. The hull itself is an excellent solid conductor: the clang relays through the metal (and the station's air) to anyone in contact with it."
    hint: "Trace what the vibration can and cannot travel through in each case."
    reflectionPrompt: "How might two spacewalkers talk without radios, using this physics?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Every sound originates from:"
    options: ["A loudspeaker", "Something vibrating", "Moving air", "Electricity"]
    correctIndex: 1
    feedback: "No vibration, no sound — strings, reeds, cones, vocal folds, slammed doors: all are vibrating sources shoving their medium rhythmically."
  - type: MULTIPLE_CHOICE
    question: "Sound cannot cross a vacuum because:"
    options:
      - "It gets too cold"
      - "There are no particles to pass the compressions along"
      - "Vacuums absorb sound"
      - "Sound moves too slowly"
    correctIndex: 1
    feedback: "Sound IS a relay of particle collisions. Remove the particles and there is nothing to relay — space is silent, whatever the films say."
---

# Hook

Every space battle you've ever watched lied to you. The roaring engines, the booming explosions — space is utterly, perfectly silent, and it has to be: sound is not a thing that flies through emptiness, it's a *relay race through matter*. No matter, no race.

But give sound something to run through and it becomes astonishing. Put your ear to a steel rail (carefully, historically) and you'd hear a train miles before the air delivers it — sound sprints through steel at fifteen times its airborne pace. Whales converse across entire ocean basins. And every word you've ever heard travelled to you as a microscopic stampede: trillions of air molecules shoving their neighbours, who shoved theirs, in a chain reaching from a vibrating something to the drum of your ear.

# Lore Introduction

Liora begins at the great bronze bell with a trick. She strikes it — *boommm* — then, while it still sings, claps a thick felted dome over it. The Hall goes quiet... but your palm, still flat on the bronze, buzzes on. "The bell never stopped," she says. "I only imprisoned the messenger." She lifts the dome; the note pours out again, fainter now. Then she leads you to the Hall's strangest apparatus: a glass vessel with a small chime inside, and a pump. The chime jangles merrily as she rings it by a lever. She works the pump — the air thins — and the jangle fades, fades, to *nothing*, the hammer still visibly striking. "Watch the hammer dance in silence, apprentice. Now tell me what sound truly needs."

# Core Learning

## Concept Introduction

**Sound begins with vibration.** A plucked string, a struck bell, your vocal folds — every sound source is something oscillating, typically tens to thousands of times per second.

**The medium relays it.** Each push of the source compresses the air beside it; that compressed parcel shoves the next, and a **compression** travels outward, followed by a **rarefaction** (a stretched-thin region) on the source's backswing. The result is a **longitudinal pressure wave** — the slinky's push-pull, at molecular scale. Crucially (Lesson 1's law): no molecule travels from bell to ear; each only shuttles in place while the *pattern* races on.

**Speed depends on the medium** — specifically on how stiff and connected it is:

| Medium | Speed of sound |
|--------|----------------|
| Air (20 °C) | ~343 m/s |
| Water | ~1,480 m/s |
| Steel | ~5,000 m/s |
| Vacuum | — (no sound at all) |

Solids' tightly-bound particles relay the shove almost instantly; gases' loose, colliding molecules dawdle. Warmer air is slightly faster (brisker molecules). And the **vacuum is silent** by necessity: a relay with no runners.

**Sound vs light, for scale.** Light crosses a kilometre in 3 *millionths* of a second; sound takes 3 *whole* seconds. That gap is the lightning-to-thunder delay, the seen-before-heard fireworks, and the reason sprinters watch the starting pistol's smoke at championship level — the bang arrives last.

## Why It Matters

- The medium-dependence of sound runs sonar (water), stethoscopes (solids and tissue), and building acoustics (why your neighbour's bass penetrates walls that block voices).
- The speed of sound is an everyday measuring stick: storm distances, echo depths, and the engineering limit that names "supersonic".
- Understanding sound-as-relay demystifies hearing itself: your eardrum is simply the last runner in the chain, shoved ~20 to 20,000 times per second.

## Worked Examples

**Example 1: The two-message train**
A worker taps a long steel pipe. A listener at the far end, ear to the metal, hears *two* taps: one through steel (5000 m/s), one through the air inside (340 m/s). If the gap between them is 0.55 s, the pipe's length L satisfies L/340 − L/5000 = 0.55 → L ≈ 200 m. Two media, one tap, and the delay becomes a tape measure.

**Example 2: Thunder mathematics**
Flash... one-thousand-and-one... to six seconds: d = 340 × 6 ≈ 2 km. The rolling, grumbling character of thunder is geometry too: different parts of the kilometres-long lightning channel are at different distances, so their cracks arrive smeared across seconds.

**Example 3: Why helium squeaks**
Inhale helium (briefly, carefully) and your voice turns cartoonish — but your vocal folds still vibrate at the *same frequencies*. Sound travels ~3× faster in helium, which reshuffles which frequencies your throat's cavities amplify (resonance — a coming attraction), brightening the timbre. The pitch you produce hasn't changed; the *filter* has. Medium matters even inside your own mouth.

## Common Mistakes

- **Thinking air travels from speaker to ear** — only the pressure pattern travels; the air molecules merely jostle in place.
- **"Sound travels best in empty space because nothing blocks it"** — exactly backwards: sound *is* matter jostling; emptiness is silence.
- **Assuming denser always = faster** — it's stiffness (how strongly particles spring back) that dominates; that's why light gases like helium can still be fast and why steel beats lead.
- **Forgetting the light/sound speed gap** — synchronised events seen-and-heard at distance are never simultaneous; the eye's report comes first.
- **"Loud sounds travel faster"** — amplitude does not change wave speed (Lesson 2's law); a whisper and a shout cross the room neck-and-neck.

## Mental Model

Sound is **a stadium chant moving through a packed crowd**. One section starts the shove — each person bumps shoulders with their neighbour and rocks back — and the bump travels around the stands far faster than anyone could walk it. Pack the crowd shoulder-to-shoulder (steel) and the chant whips around almost instantly; scatter them across a half-empty stadium (air) and it lumbers; empty the stadium entirely (vacuum) and the chant simply cannot exist. And no fan ever leaves their seat: the *enthusiasm* travels, not the people.

## Mini Summary

- ✔ All sound starts with a vibrating source
- ✔ It travels as compressions and rarefactions — a longitudinal pressure relay
- ✔ Speed: solids (~5000) > liquids (~1500) > gases (~340 m/s); vacuum = silence
- ✔ ~3 seconds per kilometre in air: thunder, echoes, and fireworks all obey
- ✔ Loudness doesn't change speed; the medium alone sets the pace

# Guided Practice Quest

Work through the guided steps to rank the racing media, range a thunderstorm, and adjudicate an orbital mishap heard by exactly one of two crewmates.

# Solo Practice Quest

Three sound-relay investigations: (1) *Solid vs air*: have a partner gently tap one end of a long table while you listen first normally, then with your ear pressed to the wood — describe the difference in loudness and character, and explain via the speed/connectivity table. (2) *Range-finding*: next storm (or using a video with a clear flash-bang pair), time flash-to-thunder for three strikes and map their distances. (3) *Vacuum thought-experiment made real*: explain precisely why phone alarms in a sealed vacuum jar fall silent but remain visible — then identify the THREE media your alarm's sound crosses to reach you on an ordinary morning (hint: start inside the phone). Write each up in two to three sentences with numbers where possible.

# Integration

**Biology**: Your ear is the relay's final exchange: eardrum (air → solid), three tiny bones amplifying the shove (solid → solid), cochlear fluid (solid → liquid), and hair cells converting the last ripple to nerve signals. Four media in two centimetres — and hearing damage is almost always the relay's delicate last runners, the hair cells, being shoved too hard.

**Music**: Instruments are vibration-launchers tuned to a medium: a violin's body exists to grip more air than a bare string could; brass instruments are sculpted air columns. Orchestras tune on stage because sound speed (and so pitch in wind instruments) shifts with the hall's temperature — physics forcing musicianship to adapt, twice nightly.

# Lore Conclusion

The pump hisses in reverse; air whispers back into the glass vessel, and the imprisoned chime's jangle swells from nothing to bright clarity, the hammer never having paused. "Vibration, medium, relay," Liora says, ticking three fingers. "Strip away any one and the universe goes mute." She rests the mallet on the great bell's rim. "But you've noticed, surely, that the bell and the chime do not merely *sound* — they sound *different*. Deep and high. Grand and silly. Loud and faint." She strikes the bell's rim and its heart in turn — two notes, worlds apart. "Tomorrow: what makes a sound deep or shrill, a whisper or a roar — and why your ear, marvel that it is, has strict limits on the conversation."
