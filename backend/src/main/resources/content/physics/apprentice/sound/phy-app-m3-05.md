---
id: phy-app-m3-05
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m3
moduleTitle: "Module 3: Waves and Light"
moduleGlyph: "🌊"
moduleSortOrder: 3
topicSlug: sound
topicTitle: "Sound"
topicSortOrder: 2
title: "Pitch, Loudness, and Hearing"
sortOrder: 5
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - Connect pitch to frequency and loudness to amplitude
  - State the human hearing range and its boundaries (infrasound, ultrasound)
  - Explain how loud sound damages hearing
integrationDomains: [biology, music]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Maps pitch to frequency and loudness to amplitude, not vice versa
    - States the ~20 Hz to 20,000 Hz human range, naming infrasound and ultrasound beyond it
    - Describes one animal exceeding human range on either side
    - Explains hearing damage as overdriven hair cells and names a protective behaviour
  keywords: [pitch, frequency, loudness, amplitude, "20", "20000", ultrasound, infrasound, decibel, hair cells]
  modelAnswer: |
    Pitch is the ear's reading of frequency — more vibrations per second sounds higher.
    Loudness is the ear's reading of amplitude — bigger pressure swings sound louder, measured
    on the decibel scale. Humans hear roughly 20 Hz to 20,000 Hz; below is infrasound (felt by
    elephants and emitted by storms), above is ultrasound (bats navigate at 100 kHz, dogs hear
    whistles we cannot). Loud sound damages the cochlea's hair cells, which never regrow —
    damage scales with both level and exposure time, so volume limits and ear protection are
    physics-backed medicine.
guidedSteps:
  - id: phy-app-m3-05-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A guitarist frets a string shorter, making it vibrate at a higher frequency. The listener hears:
    inputConfig:
      options:
        - "A louder note"
        - "A higher-pitched note"
        - "A faster-travelling note"
        - "A longer note"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A higher-pitched note"]
      rejectedFeedback: "Frequency IS pitch, perceptually: more cycles per second = higher note. Loudness would need a bigger amplitude (harder pluck), and travel speed belongs to the air, not the string."
    hint: "Which perceptual quality tracks vibrations-per-second?"
    reflectionPrompt: "Two dials on every sound: f and A. Which everyday words do we attach to each?"
  - id: phy-app-m3-05-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The healthy young human ear hears frequencies from about 20 Hz up to about ________ Hz.
    inputConfig:
      placeholder: "20000"
    markingRule:
      matchMode: CONTAINS
      accepted: ["20000", "20,000", "20 000", "20k"]
      rejectedFeedback: "Roughly 20 Hz – 20,000 Hz (20 kHz). The ceiling falls with age and noise exposure — most adults top out well below 20 kHz."
    hint: "It's the famous 'twenty to twenty thousand'."
    reflectionPrompt: "Why might a teenager hear a shop's anti-loitering buzzer that an adult cannot?"
  - id: phy-app-m3-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A friend says: "Turning the volume up makes the music's notes higher as well as louder." In 2–3 sentences, untangle this using amplitude and frequency.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [amplitude, frequency, independent, loudness, pitch, separate]
      rejectedFeedback: "Volume controls amplitude only — bigger pressure swings, same frequencies. Pitch is set by frequency, which the volume knob does not touch. The two properties are independent dials."
    hint: "One knob, one wave property. Which one?"
    reflectionPrompt: "Can a sound be high-pitched AND quiet? Low AND deafening? Find examples."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Sound above 20,000 Hz is called:"
    options: ["Infrasound", "Ultrasound", "Supersound", "White noise"]
    correctIndex: 1
    feedback: "Ultrasound — above human hearing. Bats, dolphins, and hospital scanners live up there. Infrasound is the opposite end, below 20 Hz."
  - type: MULTIPLE_CHOICE
    question: "Prolonged loud sound causes permanent hearing loss by:"
    options:
      - "Stretching the eardrum permanently"
      - "Overdriving and destroying cochlear hair cells, which do not regrow"
      - "Filling the ear with compressed air"
      - "Loosening the ear bones"
    correctIndex: 1
    feedback: "The cochlea's microscopic hair cells are the irreplaceable part: driven too hard for too long, they die — and human hair cells never regenerate. Hence decibel limits and ear defenders."
---

# Hook

Right now, all around you, conversations are happening that you are deaf to. A dog two streets away hears a whistle pitched far above your ceiling. Bats are screaming at 100,000 vibrations per second — *screaming*, at volumes that would be painful if you could hear them. Elephants rumble to each other across kilometres at frequencies beneath your floor; storms and volcanoes mutter in the same sub-bass band.

Human hearing is a window — 20 to 20,000 Hz — and everything in this lesson lives on the window's two dimensions. **Pitch** is your brain's name for frequency. **Loudness** is its name for amplitude. Two independent dials, two perceptual experiences — and one fragile, microscopic apparatus in your inner ear that reads both, takes a lifetime of punishment, and never grows back.

# Lore Introduction

Liora's apprentices call it the Gauntlet of Forks: forty tuning forks in a velvet-lined case, arranged smallest to largest. She strikes the great bass fork — you *feel* it in your sternum more than hear it. Then up the rank, fork by fork, the notes climbing... until, three forks from the end, something odd happens. Liora strikes; the fork visibly shivers; you hear nothing at all. "Still singing," she says, touching it to the water-pool — ripples bloom instantly. The last forks ring for dogs, for bats, for nobody human. Then she returns to a middle fork and strikes it twice: once gently, once hard. Same note — whisper, then shout. "Two questions, apprentice, never to be confused again: *how often* does it tremble, and *how hard*? Your ear answers the first with pitch and the second with loudness — and your ear's answers, you have just learned, have edges."

# Core Learning

## Concept Introduction

**Pitch = perceived frequency.** More oscillations per second → higher note. A bass string at 80 Hz growls; a soprano's top C at 1,047 Hz soars; doubling any frequency raises it exactly one octave. The frequency is set by the source (Lesson 2's law) and survives the journey to your ear unchanged.

**Loudness = perceived amplitude.** Bigger pressure swings → louder sound. Measured on the **decibel (dB)** scale, which is logarithmic: every +10 dB sounds roughly *twice* as loud and carries *ten times* the power. Anchors: whisper ~30 dB, conversation ~60 dB, busy traffic ~85 dB (the all-day safety boundary), rock concert ~110 dB, jet at 30 m ~140 dB (instant damage).

**The human window: ~20 Hz to ~20,000 Hz (20 kHz).**

| Band | Range | Inhabitants |
|------|-------|-------------|
| **Infrasound** | < 20 Hz | Elephants' long-range rumbles, storms, volcanoes, earthquakes |
| **Audible** | 20 Hz – 20 kHz | Human speech (~100–8,000 Hz), music, every sound you've ever heard |
| **Ultrasound** | > 20 kHz | Dog whistles (~25–50 kHz), bat sonar (up to ~120 kHz), hospital scanners (MHz) |

The ceiling sinks with age and with damage — most adults are nearer 15 kHz.

**Hearing and its fragility.** The cochlea's ~15,000 **hair cells** each respond to particular frequencies; loud sound is large amplitude, which whips them violently. Overdriven cells die, and in humans *they never regrow*. Damage compounds level × time: 85 dB is safe for a working day, 100 dB for fifteen minutes. Earplugs, distance (amplitude falls quickly with it), and volume limits are not fussiness — they are the only intervention that works, because there is no repair.

## Why It Matters

- The frequency/amplitude split is the foundation of all audio: equalisers sculpt frequencies, volume knobs scale amplitude, microphones and speakers translate between pressure and electricity.
- Ultrasound's usefulness (imaging, cleaning, ranging) comes directly from short wavelengths — high f means small λ = v/f, fine enough to resolve a fetus or a crack in steel.
- Noise-induced hearing loss is the world's most common preventable disability; the physics here is literally protective knowledge.

## Worked Examples

**Example 1: The piano as a frequency ruler**
A piano spans 27.5 Hz (bottom A) to 4,186 Hz (top C) — barely a quarter of your hearing window, yet it covers nearly all music. Above ~4 kHz, "notes" stop sounding musical and become whistles and hiss; your window's upper half is reserved for *timbre* — the sparkle and consonants that make a voice recognisable. Lose the top octaves (age, headphones) and the world sounds muffled even though speech "volume" is unchanged.

**Example 2: Decibel arithmetic for a gig**
Front-of-stage at 110 dB: safe exposure is about *one minute* before damage begins accruing. Move back so the level drops to 100 dB: ~15 minutes. Add musician's earplugs (−20 dB): 80 dB — safe all night, music intact. Three decisions, all amplitude management; none affect pitch at all.

**Example 3: Why bats broadcast in ultrasound**
A bat needs to "see" moths. Resolution requires wavelengths smaller than the target: at 100 kHz, λ = 340/100,000 ≈ 3.4 mm — moth-sized. At human-audible 1 kHz, λ = 34 cm: the wave would diffract around the moth entirely (Lesson 3!), returning no echo. The bat's scream is pitched by physics, not preference.

## Common Mistakes

- **Welding loudness to pitch** — the volume knob does not transpose the song; the two dials are independent.
- **"Decibels add normally"** — two 60 dB conversations are not 120 dB (≈63 dB, in fact); the scale is logarithmic, and intuition needs retraining.
- **Believing hearing damage announces itself** — early loss is painless and gradual, starting at high frequencies you rarely test; ringing after a loud event is a damage report, not a quirk.
- **"Ultrasound is louder sound"** — it's *higher*, not louder; a sound can be ultrasonic and faint, or audible and deafening.
- **Thinking the limits are universal** — your 20–20k window is a human spec sheet, not a property of sound; the world is full of off-window broadcasts.

## Mental Model

Your hearing is **a radio receiver with a fixed dial range and a fragile speaker**. Frequency is *where on the dial* a sound broadcasts: speech and music transmit mid-band; elephants and bats broadcast off both ends of your dial, in perfect clarity to receivers built for those stations. Amplitude is the *signal strength* — and here's the design flaw: your receiver's speaker cones (hair cells) blow out when overdriven, one frequency band at a time, and the manufacturer stocks no spares. Treat the volume of the world as something you mix, with distance and plugs as your faders.

## Mini Summary

- ✔ Pitch tracks frequency; loudness tracks amplitude — independent dials
- ✔ Human window ≈ 20 Hz – 20 kHz; infrasound below, ultrasound above
- ✔ Decibels are logarithmic: +10 dB ≈ twice as loud, 10× the power
- ✔ Hair cells read the wave and never regrow — damage = level × time
- ✔ High frequency means short wavelength: the secret of bat sonar and medical imaging

# Guided Practice Quest

Work through the guided steps to fret a higher note, locate your species' ceiling, and dismantle the volume-knob-raises-pitch myth for good.

# Solo Practice Quest

Profile your own receiver: using a free tone-generator app or website, (1) find your personal upper frequency limit (sweep upward from 8 kHz at *modest, fixed* volume — note where the tone vanishes for you, and compare with someone older or younger); (2) hold frequency fixed at 440 Hz and vary only volume, then hold volume fixed and vary only frequency — write one sentence per experiment on which perception changed; (3) audit one day of your sound exposure against the decibel table (commute, headphones, kitchen) and identify your single largest level × time contributor, plus one realistic mitigation. Report all three with numbers.

# Integration

**Biology**: The cochlea is a frequency analyser built in flesh: high frequencies excite hair cells near its base, low frequencies near its tip — a logarithmic keyboard two centimetres long. Its damage pattern (high frequencies first) is why age-related loss steals birdsong and consonants before it touches vowels.

**Music**: Everything in this lesson is a musician's working material: octaves are doublings, dynamics are amplitude artistry, and orchestration is the craft of placing instruments across the audible window so they don't mask one another. Mixing engineers are applied psychoacousticians with faders.

# Lore Conclusion

At the Gauntlet's end, Liora has you strike the silent fork yourself — shiver, ripples, nothing — and then write your own ceiling in the Hall's ledger, beside centuries of apprentices' entries. You notice the column of numbers droops with the recorded ages; one entry, some long-dead percussionist of the Academy band, has a sadly low figure and the marginal note *"drums, forty years, no felt"*. "The ledger teaches better than I do," Liora says quietly. She closes the fork case and brightens. "But sound has one more trick for you — its most useful. Strike a note in the right place and the world *answers back*." She claps once, sharply, toward the Hall's far stone wall; a beat later, faint and perfect, the clap returns. "Tomorrow: echoes — and how sailors, surgeons, and bats turned them into eyes."
