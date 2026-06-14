---
id: phy-app-m3-02
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m3
moduleTitle: "Module 3: Waves and Light"
moduleGlyph: "🌊"
moduleSortOrder: 3
topicSlug: wave_fundamentals
topicTitle: "Wave Fundamentals"
topicSortOrder: 1
title: "Wavelength, Frequency, and the Wave Equation"
sortOrder: 2
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Define amplitude, wavelength, frequency, and period
  - Use the wave equation v = f × λ
  - Relate frequency and wavelength inversely at fixed wave speed
integrationDomains: [music, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Defines amplitude, wavelength (m), frequency (Hz), period (s)
    - Applies v = fλ to compute any one quantity from the other two
    - States the inverse trade-off between f and λ at fixed speed
  keywords: [amplitude, wavelength, frequency, hertz, period, "v = f", lambda, inverse]
  modelAnswer: |
    Amplitude is the maximum displacement from rest — the wave's height, carrying its loudness
    or brightness. Wavelength λ is the distance from one crest to the next, in metres.
    Frequency f is how many complete waves pass per second, in hertz; period T = 1/f is the
    time for one wave. They tie together as v = fλ: waves passing at 5 per second, each 2 m
    long, sweep past at 10 m/s. At fixed speed, frequency and wavelength trade inversely —
    sound at 340 m/s gives a 340 Hz tone a 1 m wavelength, and a 3400 Hz tone just 10 cm.
guidedSteps:
  - id: phy-app-m3-02-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      Ripples cross a tank at 0.6 m/s with wavelength 0.2 m. Their frequency is f = v/λ = ________ Hz.
    inputConfig:
      placeholder: "3"
    markingRule:
      matchMode: CONTAINS
      accepted: ["3"]
      rejectedFeedback: "f = v ÷ λ = 0.6 ÷ 0.2 = 3 Hz — three complete ripples pass any point each second."
    hint: "Rearrange v = fλ. The balance rule from Module One still applies."
    reflectionPrompt: "What is the period of these ripples?"
  - id: phy-app-m3-02-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Sound travels through air at a fixed ~340 m/s. A singer jumps an octave, doubling the frequency of her note. The wavelength of the sound:
    inputConfig:
      options:
        - "Doubles"
        - "Halves"
        - "Stays the same"
        - "Quadruples"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Halves"]
      rejectedFeedback: "v is fixed by the medium, so λ = v/f: doubling f must halve λ. Frequency and wavelength are inverse partners at constant speed."
    hint: "v = fλ with v locked. If f goes up, what must λ do?"
    reflectionPrompt: "Who sets the wave's speed — the singer, or the air?"
  - id: phy-app-m3-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Standing on a pier, you count 12 wave crests passing in 60 seconds, and you estimate crests are 8 m apart. In 2–3 sentences, find the waves' frequency and speed, showing your reasoning.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["0.2", "1.6", frequency, "v = f"]
      rejectedFeedback: "f = 12 ÷ 60 = 0.2 Hz. Then v = fλ = 0.2 × 8 = 1.6 m/s. Counting and a tape-estimate gave you everything."
    hint: "Frequency = crests per second. Then the wave equation."
    reflectionPrompt: "Which of your two measurements is less certain, and how would that uncertainty flow into v?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Frequency is measured in hertz (Hz), which means:"
    options:
      - "Metres per second"
      - "Complete waves (cycles) per second"
      - "Seconds per wave"
      - "Crests per metre"
    correctIndex: 1
    feedback: "1 Hz = one full cycle each second. Its reciprocal, the period (seconds per cycle), answers the same question from the other side."
  - type: MULTIPLE_CHOICE
    question: "Turning up a speaker's volume increases the sound wave's:"
    options: ["Wavelength", "Frequency", "Amplitude", "Speed"]
    correctIndex: 2
    feedback: "Loudness is carried by amplitude — bigger oscillations, more energy. Pitch is frequency, and speed belongs to the air."
---

# Hook

Tune an old radio and you're hunting waves by their length: "FM 98.5 MHz" means ninety-eight and a half million waves arriving *every second*, each about 3 metres long. Press a piano's lowest key: 27 vibrations per second, each sound wave stretching 12 metres — longer than the piano itself. The highest key: 4,186 per second, each wave just 8 cm.

Every wave in the universe — sound, light, ripple, quake — is described by the same three numbers: how *tall* (amplitude), how *long* (wavelength), how *often* (frequency). And one beautifully simple equation chains them to the speed: **v = fλ**. Master these and you hold the grammar of the entire wave world: every lesson from here to the electromagnetic spectrum speaks this language.

# Lore Introduction

In the Resonance Hall, Magus Liora has rigged the long silver rope to a crank-wheel. "Turn slowly," she instructs. You crank; broad lazy humps roll down the rope, far apart. "Faster." The humps come quicker — and *shorter*, crowding together, though each hump still races down the rope at exactly the same pace as before. Liora stops you mid-crank. "Notice what you could and could not choose. You chose how *often* — your cranking set that. The rope itself chose how *fast* the humps travel; you cannot hurry them by cranking harder, only make them taller." She produces a measuring cord. "Often, long, tall, fast — four numbers. Three of them, you'll find, are not independent. Find the law that chains them, and the Hall will sing for you."

# Core Learning

## Concept Introduction

Four numbers give any wave its identity:

| Quantity | Symbol | Meaning | Unit |
|----------|--------|---------|------|
| **Amplitude** | A | Maximum displacement from rest — the wave's height | m (or pressure, etc.) |
| **Wavelength** | λ | Distance from one crest to the next (one full pattern) | m |
| **Frequency** | f | Complete waves passing per second | Hz |
| **Period** | T | Time for one complete wave; T = 1/f | s |

**Amplitude carries the energy** — louder sound, brighter light, taller breaker. It does *not* affect speed or pitch.

**The wave equation.** In one second, f waves each of length λ march past — so the wave advances f × λ metres per second:

```
v = f × λ
(m/s) = (Hz) × (m)
```

(Unit check: Hz = 1/s, so (1/s)·m = m/s ✓ — Module One approves.)

**Who controls what.** The *source* sets frequency (your crank, a vocal cord, a radio transmitter). The *medium* sets speed (rope tension, air temperature, the fabric of space for light). Wavelength is the negotiated result: λ = v/f. Consequence: **at fixed speed, frequency and wavelength are inverse partners** — double one, halve the other. High pitches are short waves; long radio waves are slow flickers.

## Why It Matters

- v = fλ is the single most-used wave formula — radio licensing, musical acoustics, ultrasound design, and wifi channel planning all run on it.
- The source-sets-f / medium-sets-v division explains otherwise-confusing phenomena: why your voice's pitch survives travelling through walls, why helium changes your voice's *timbre* (speed changes, so wavelengths shift).
- Amplitude-as-energy underlies decibels, brightness scales, and earthquake magnitudes — all loudness-like measures are amplitude bookkeeping.

## Worked Examples

**Example 1: Sizing a sound**
Middle A on an orchestra's tuning note: f = 440 Hz, sound speed 340 m/s. λ = v/f = 340/440 ≈ 0.77 m. The wave pattern repeating through the concert hall is about an arm-span long. The lowest organ pipe note (16 Hz): λ ≈ 21 m — which is why great organs need cathedral-sized spaces to bloom.

**Example 2: Radio arithmetic**
A station broadcasts at 100 MHz (10⁸ Hz). Radio waves travel at light speed, 3 × 10⁸ m/s. λ = 3×10⁸ / 10⁸ = 3 m. Antennas work best around a quarter to a half of the wavelength — hence metre-scale FM aerials, and centimetre-scale antennas inside your phone (whose signals run at gigahertz).

**Example 3: Reading the sea**
Swell arrives with 10 s between crests (f = 0.1 Hz) and the crests look ~150 m apart. v = fλ = 15 m/s — fifty-odd km/h. Surfers, harbourmasters and coastal engineers genuinely make this calculation; long-period swell carries disproportionate energy (amplitude *and* length), which is why "groundswell" days are the dangerous, glorious ones.

## Common Mistakes

- **Thinking harder cranking speeds the wave up** — more force raises *amplitude*; the medium alone owns the speed.
- **Confusing amplitude with frequency** — loud ≠ high-pitched; bright ≠ blue. Height and rate are independent dials.
- **Measuring wavelength crest-to-trough** — that's half a wave; λ runs crest to *next crest*.
- **Forgetting the inverse trade** — students raise f and expect λ to rise too; at fixed v the equation forbids it.
- **Mixing units in v = fλ** — kHz with metres, MHz with centimetres; convert to Hz and m first.

## Mental Model

Picture a **goods train passing a level crossing**. Wagon length = wavelength. Wagons-per-second past your nose = frequency. The train's speed = (wagons per second) × (length of each) — that is v = fλ, watched from a deck chair. The railway (medium) enforces one speed limit for all trains; so a driver who couples shorter wagons must send more of them per second to keep pace. And amplitude? That's how heavily each wagon is *loaded* — it changes what the train delivers, not how fast it runs.

## Mini Summary

- ✔ Amplitude = height = energy; wavelength = crest-to-crest length; frequency = waves per second; T = 1/f
- ✔ v = fλ — the universal wave equation (check it with units)
- ✔ Source sets f; medium sets v; λ adjusts to fit
- ✔ At fixed speed, f and λ are strict inverse partners
- ✔ Loudness/brightness lives in amplitude — never in frequency

# Guided Practice Quest

Work through the guided steps to count ripples into hertz, halve a soprano's wavelength, and turn pier-watching into a full wave measurement.

# Solo Practice Quest

Three measurements, rising in ambition: (1) Bathtub or basin: make steady ripples with a finger-tap rhythm, estimate f (taps per second) and λ (by eye against a ruler), compute v — then check by timing a ripple crossing a known distance. Do the two values agree within your uncertainty? (2) Sound: compute λ for the lowest note you can hum and the highest you can whistle (estimate their frequencies with a tuner app; v = 340 m/s). (3) Light: your wifi runs at ~2.4 GHz and radio waves move at 3×10⁸ m/s — compute λ and comment on why the router's antennas are the size they are. Show v = fλ working each time.

# Integration

**Music**: An octave is exactly a doubling of frequency — the inverse-partner law means each octave up halves every wavelength, which is why organ pipes and guitar fret spacings shrink geometrically. Harmony itself is arithmetic between frequencies, a discovery old enough to be Pythagoras'.

**Mathematics**: f and λ's inverse trade at fixed v is the hyperbola xy = constant — your proportionality toolkit from Module One, now running a concert hall. The sine curve that draws every wave belongs to trigonometry, waiting at Senior tier.

# Lore Conclusion

You hand Liora your chained law — *speed equals often times long* — together with the rope-and-cord measurements that prove it. She tests you once, mercilessly: "Crank twice as fast. Tell me everything before you touch the wheel." Humps twice as frequent, each half as long, speed unchanged, you recite; taller only if I crank *harder*. The Hall's racked tuning forks seem to approve in faint sympathetic hums. "Grammar learned," Liora declares. "Now — what happens when a wave *arrives* somewhere? A wall, a doorway, a change of waters?" She flicks the silver rope; a hump races away, strikes the far anchor — and comes racing *back*. "Tomorrow: the three great manners of waves at a boundary. Every echo, every lens, and every rainbow is waiting there."
