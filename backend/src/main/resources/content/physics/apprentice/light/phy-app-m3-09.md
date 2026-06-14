---
id: phy-app-m3-09
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m3
moduleTitle: "Module 3: Waves and Light"
moduleGlyph: "🌊"
moduleSortOrder: 3
topicSlug: light
topicTitle: "Light"
topicSortOrder: 3
title: "Colour and the Visible Spectrum"
sortOrder: 9
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Explain dispersion — white light splitting into the spectrum
  - Connect colour to wavelength/frequency of light
  - Explain object colour via selective absorption and reflection
integrationDomains: [biology, art]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States that white light is a mixture, split by a prism because refraction varies with wavelength
    - Orders the spectrum (red long-wavelength to violet short-wavelength)
    - Explains object colour as wavelengths reflected versus absorbed
    - Predicts an object's appearance under coloured light
  keywords: [dispersion, prism, spectrum, wavelength, absorb, reflect, red, violet, white]
  modelAnswer: |
    White light is a mixture of all visible wavelengths. A prism disperses it because glass
    slows (and so bends) short violet waves slightly more than long red ones — the spectrum
    runs red, orange, yellow, green, blue, violet from least-bent to most-bent. Raindrops do
    the same to sunlight, making rainbows. Objects have no colour of their own: a red apple
    reflects red wavelengths and absorbs the rest, so under pure blue light it appears black —
    nothing it can reflect is arriving. Colour is a conversation between the light supplied
    and the surface's reflective preferences, judged by the eye.
guidedSteps:
  - id: phy-app-m3-09-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A prism splits white light into a spectrum because:
    inputConfig:
      options:
        - "The glass adds colours to the light"
        - "Different wavelengths refract by different amounts — violet bends most, red least"
        - "The prism's paint separates inside the glass"
        - "Light slows equally but spreads by diffraction"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Different wavelengths refract by different amounts — violet bends most, red least"]
      rejectedFeedback: "Dispersion: glass slows short wavelengths (violet) a touch more than long ones (red), so each colour exits at a slightly different angle. The colours were in the white light all along — the prism only sorts them."
    hint: "The colours come OUT of the white light. What sorts them?"
    reflectionPrompt: "What does a second, inverted prism do to the spread-out spectrum? (Newton tried it.)"
  - id: phy-app-m3-09-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In the visible spectrum, ________ light has the longest wavelength, and violet the shortest.
    inputConfig:
      placeholder: "red"
    markingRule:
      matchMode: CONTAINS
      accepted: [red]
      rejectedFeedback: "Red ~700 nm is the long-wavelength end; violet ~400 nm the short. (Frequency runs the other way: violet highest.)"
    hint: "Think of the rainbow's outer edge."
    reflectionPrompt: "Which end of the spectrum carries more energy per wave, and what lies just beyond it?"
  - id: phy-app-m3-09-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A theatre swaps its white stage lights for pure red ones. Predict the appearance under red light of: (a) a white shirt, (b) a red scarf, (c) a green dress. Explain using absorption and reflection. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [red, black, absorb, reflect, appears, green]
      rejectedFeedback: "White shirt: reflects everything offered → appears red. Red scarf: reflects red → still red. Green dress: reflects only green, but no green arrives — it absorbs the red and appears BLACK. Objects can only reflect what they're given."
    hint: "Ask of each surface: what does it reflect, and is any of that on offer?"
    reflectionPrompt: "Why do clothes bought under shop lighting sometimes disappoint in daylight?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A banana looks yellow in sunlight because it:"
    options:
      - "Emits yellow light"
      - "Reflects yellow wavelengths and absorbs the others"
      - "Absorbs yellow and reflects the rest"
      - "Contains yellow light inside"
    correctIndex: 1
    feedback: "Non-luminous colour = selective reflection: the skin's pigments soak up most wavelengths and bounce back the yellow band to your eye."
  - type: MULTIPLE_CHOICE
    question: "A rainbow appears when sunlight is:"
    options:
      - "Reflected by clouds"
      - "Refracted, reflected, and dispersed inside raindrops — each drop a tiny prism"
      - "Absorbed by rain and re-emitted"
      - "Diffracted around the Sun"
    correctIndex: 1
    feedback: "Each drop refracts incoming sunlight (dispersing the colours), reflects it off the back surface, and refracts again on exit — millions of drops, sorted by angle, paint the bow."
---

# Hook

In 1666, a 23-year-old Isaac Newton darkened his room, let one sunbeam through a hole in the shutters, and placed a prism in its path. Out came the famous ribbon of colours. Nothing new — "the prism colours the light," everyone said. Newton's genius was the *second* prism: he selected just the red band, passed it through again... and it stayed red. The prism couldn't colour anything. Then he caught the full spectrum in a second, inverted prism — and out came *white*.

Conclusion, scandalous at the time: **white light is not pure — it is every colour, mixed.** The prism merely un-mixes what was always there. From that single experiment flows everything in this lesson: rainbows, the redness of sunsets, why your jumper "changes colour" under shop lights, and the strange truth that no object actually *has* a colour at all.

# Lore Introduction

Liora draws the Hall of Optics' velvet curtains until one needle of sunlight remains, then sets the Academy's great prism in its path with the reverence of ritual. The far wall blooms: a tall, ordered banner of colour, red at one edge through gold and green to deep violet. "Apprentices have gasped at this for nine hundred years," she says, "and for the first seven hundred, every one of them drew the wrong conclusion. They thought the glass *painted* the light." She produces a slotted card and isolates the green band alone, then passes it through a second prism. Green in, green out — spread no further, painted no more. "The glass has no paint. It is a *sorter*. Which means —" she gestures at the needle of ordinary daylight, "— that everything you have ever called 'white' was a crowd in disguise. Today we interrogate the crowd."

# Core Learning

## Concept Introduction

**Dispersion: white light un-mixed.** White light is a blend of all visible wavelengths. In glass, light slows — but *short* wavelengths slow slightly more than long ones, so refraction bends violet most and red least. A prism's two angled surfaces compound the effect into the **spectrum**:

```
Red — Orange — Yellow — Green — Blue — Violet
(~700 nm, bent least)            (~400 nm, bent most)
```

(A nanometre is 10⁻⁹ m: visible waves are under a thousandth of a millimetre — recall why light snubs doorways while sound floods round them.) **Rainbows** are dispersion freelancing: each raindrop refracts, internally reflects, and re-refracts sunlight; millions of drops, sorted by angle, hang the bow — always opposite the Sun, always red on the outside.

**Colour of objects: selective reflection.** Non-luminous things are visible by reflected light (Lesson 7), and they are *picky*:

- A **red apple** reflects the red band, absorbs the rest (absorbed light becomes a whisper of warmth — conservation never sleeps)
- A **white** surface reflects nearly everything offered
- A **black** surface absorbs nearly everything (and warms accordingly — black cars in summer)

The consequence with teeth: **an object can only reflect what it is given.** A green dress under pure red light receives nothing it can reflect → black. A white shirt under red light → red. "What colour is it really?" has no answer deeper than: *here is its reflection preference; tell me the illumination.*

**Coloured filters** are the absorption trick in transmission: red glass passes red, absorbs the rest — a sorter that discards.

## Why It Matters

- Every display you own builds colour the other way round — mixing red, green, and blue *light* (additive) — while every printer mixes absorbing *pigments* (subtractive); both industries are this lesson, run forward and backward.
- Lighting design (shops, galleries, surgeries, film) is applied selective reflection: the illuminant chooses what colours are even possible.
- Dispersion is both nuisance and instrument: it blurs cheap lenses (chromatic aberration) and powers the spectroscope — the tool that, next tier, lets you read the chemistry of stars from their light.

## Worked Examples

**Example 1: The sunset machine**
Air molecules scatter short wavelengths (blue, violet) far more strongly than long ones — that's why the *sky* (scattered light) is blue. At sunset, sunlight crosses hundreds of kilometres of air; so much blue is scattered away en route that the surviving direct beam arrives stripped — red and gold. Same physics, two postcards: blue noon sky, red evening sun.

**Example 2: The disappointing jumper**
A jumper looks slate-blue under a shop's warm LEDs but garishly bright in daylight. The shop light was poor in some wavelengths; the jumper's true reflection-preferences never got a full audition. Galleries and clothing retailers specify lamps by "colour rendering" scores for exactly this reason — illumination is half of every colour.

**Example 3: Stage-light arithmetic**
Lighting a flag (red and white stripes) for theatre: under red light — red stripes red, white stripes red: the flag appears all-red, stripes gone. Under green light — white stripes green, red stripes *black*: maximum contrast. Lighting designers run this absorption arithmetic on every costume and set; you now can too.

## Common Mistakes

- **"The prism adds colours"** — Newton's second prism retires this; the prism sorts, never paints.
- **Believing colour is *in* the object** — objects hold reflection *preferences*; realised colour needs the illuminant's cooperation.
- **"Black is a colour of light"** — black is the absence of reflected light; there is no black wavelength.
- **Mixing up additive and subtractive mixing** — lights mix toward white (red+green light = yellow!); pigments mix toward black; confusing the two ruins both painting and pixel intuition.
- **Forgetting absorbed light becomes heat** — the energy ledger from Module Two patrols here: dark surfaces in sunshine are warm for bookkeeping reasons.

## Mental Model

Think of white light as **a choir of singers, every voice from bass-red to soprano-violet, singing at once** — what reaches your ear sounds like one note ("white") until something sorts the voices. A prism is a hall whose acoustics deflect each voice to a different seat: suddenly you hear the choir as individuals. A red apple is an audience door that admits only the bass section and turns the rest away (where their energy dissipates as warmth in the corridor). And asking "what colour is the apple, really?" is asking what the door sounds like with no choir singing: silence. Surfaces don't sing — they *select*.

## Mini Summary

- ✔ White light = all visible wavelengths mixed; prisms sort (dispersion), never paint
- ✔ Spectrum: red (~700 nm, least bent) → violet (~400 nm, most bent); raindrops = freelance prisms
- ✔ Object colour = wavelengths reflected; the rest are absorbed (and become warmth)
- ✔ No illumination band → nothing to reflect: green dress under red light is black
- ✔ Lights mix additively (toward white); pigments subtractively (toward black)

# Guided Practice Quest

Work through the guided steps to acquit the prism of painting, anchor the spectrum's two ends, and light a theatre's costumes by pure absorption arithmetic.

# Solo Practice Quest

Run Newton's programme at kitchen scale: (1) *Make a spectrum* — a glass of water on a sunny windowsill with white paper below, a CD's underside, or a phone-torch through a water-filled bottle; sketch and label the colour order. (2) *Selective reflection safari* — gather five strongly coloured objects and predict each one's appearance under a single coloured light (use a coloured-bulb app/filter or cellophane over a torch); test, and record predictions vs results. (3) *The illuminant audit* — photograph one colourful scene under daylight and again under your warmest indoor lighting; write three sentences on which surfaces changed most and why, in absorption-and-reflection language. Conclude: in one sentence, answer a child who asks "but what colour is it REALLY?"

# Integration

**Biology**: Your retina samples the spectrum with just three cone types (roughly red-, green-, and blue-centred) — every colour you experience is a three-number summary of a full spectrum, which is why screens get away with only three subpixels, and why some mixtures fool the eye perfectly. Most mammals run two cones; many birds run four, plus ultraviolet: the rainbow you see is a species-specific edit.

**Art**: Painters mastered subtractive mixing centuries before physics named it: pigment chemistry, complementary contrast, and the warm-light/cool-shadow grammar of the Impressionists are all selective absorption deployed by hand. Theatre and film lighting design is the additive counterpart — this lesson, with a budget and a mood board.

# Lore Conclusion

At dusk, Liora performs the closing rite of the topic: the full spectrum blazing on the wall, she sets the second great prism inverted in its path — and the banner of colours folds back, seamlessly, into a single beam of plain white on the velvet. The crowd, reassembled. "Three lessons ago you learned light travels straight and swift. Then: that it can be commanded — bounced, bent, gathered. Tonight: that it was never one thing, but a braided crowd." She extinguishes the beam and, in the dark, her voice drops as if sharing contraband. "Now the question the Academy whispered for centuries: the crowd you can see runs from red to violet. *What walks beyond the edges?* The forks sang notes your ear could not hear. Does the Sun, apprentice, shine colours your eye cannot see?" She relights the lamp, and her smile is all anticipation. "Tomorrow we go beyond the rainbow. Bring a thermometer."
