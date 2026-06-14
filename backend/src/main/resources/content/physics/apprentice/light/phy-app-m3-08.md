---
id: phy-app-m3-08
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m3
moduleTitle: "Module 3: Waves and Light"
moduleGlyph: "🌊"
moduleSortOrder: 3
topicSlug: light
topicTitle: "Light"
topicSortOrder: 3
title: "Mirrors and Lenses"
sortOrder: 8
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Describe the image in a plane mirror (virtual, upright, laterally inverted, equal distance)
  - Explain how converging lenses bend rays to a focus
  - Match mirrors and lenses to their applications (eyes, glasses, cameras, telescopes)
integrationDomains: [biology, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States the plane-mirror image properties (virtual, upright, same size, as far behind as the object is in front)
    - Explains lens focusing as refraction at curved surfaces
    - Distinguishes converging from diverging lenses and their uses
    - Connects short/long sight to the eye's focusing and the lens that corrects each
  keywords: [plane mirror, virtual, focus, converging, diverging, refraction, focal length, image]
  modelAnswer: |
    A plane mirror's image is virtual (no light actually passes behind the glass), upright,
    the same size as the object, and exactly as far behind the mirror as the object is in
    front; left and right appear swapped. Lenses use refraction at curved surfaces: a
    converging (convex) lens bends parallel rays inward to a focal point, gathering light for
    cameras, magnifiers, and the eye itself; a diverging (concave) lens spreads rays as if
    from a virtual focus. Short sight is corrected with diverging lenses, long sight with
    converging — eyeglasses are refraction prescribed by an optician.
guidedSteps:
  - id: phy-app-m3-08-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You stand 2 m in front of a flat mirror. Your image is:
    inputConfig:
      options:
        - "2 m behind the mirror, virtual, upright, your size"
        - "On the mirror's surface"
        - "2 m behind the mirror, real and touchable"
        - "4 m behind the mirror, inverted"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["2 m behind the mirror, virtual, upright, your size"]
      rejectedFeedback: "Plane-mirror image: as far behind as you are in front (2 m), upright, life-size, and VIRTUAL — the rays only appear to come from back there; none actually do. (That's why you can't project it onto a screen.)"
    hint: "Where do the reflected rays APPEAR to come from?"
    reflectionPrompt: "If you walk toward the mirror at 1 m/s, how fast does your image approach you?"
  - id: phy-app-m3-08-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A magnifying glass focuses the parallel rays of the Sun to a bright spot 15 cm from the lens. The lens's focal length is ________ cm.
    inputConfig:
      placeholder: "15"
    markingRule:
      matchMode: CONTAINS
      accepted: ["15"]
      rejectedFeedback: "Parallel rays converge at the focal point, so the spot-distance IS the focal length: 15 cm. (Sunlight's rays arrive effectively parallel — the Sun is far away.)"
    hint: "Parallel rays meet at the focal point, by definition."
    reflectionPrompt: "Why must the Sun's rays be treated as parallel, given its distance?"
  - id: phy-app-m3-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A short-sighted person sees nearby things sharply but distant things blurred — their eye focuses distant light IN FRONT of the retina. In 2–3 sentences, explain which lens type (converging or diverging) corrects this, and why.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [diverging, concave, spread, weaken, in front, retina]
      rejectedFeedback: "The eye converges too strongly for distant objects, so the fix is a DIVERGING (concave) lens: it spreads incoming rays slightly before they reach the eye, pushing the focus back onto the retina."
    hint: "The eye is focusing too soon. Should the spectacle lens add convergence or subtract it?"
    reflectionPrompt: "By the same logic, what does a long-sighted prescription do, and why do many people need it with age?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A converging (convex) lens takes parallel incoming rays and:"
    options:
      - "Reflects them straight back"
      - "Bends them inward to meet at the focal point"
      - "Spreads them apart"
      - "Stops them"
    correctIndex: 1
    feedback: "Refraction at the two curved surfaces tilts each ray inward; parallel rays all cross at one point — the focus. Focal length measures the lens's bending strength."
  - type: MULTIPLE_CHOICE
    question: "Why can't you project a plane mirror's image onto a screen?"
    options:
      - "It is too dim"
      - "It is virtual — no light actually passes through the image position behind the mirror"
      - "Screens don't work with reflected light"
      - "It is laterally inverted"
    correctIndex: 1
    feedback: "The rays only APPEAR to diverge from behind the mirror; nothing is actually there to project. Real images (like a projector's or a camera's) are made of genuinely converging light."
---

# Hook

Every photograph ever taken, every glimpse of your own face, every star seen through a telescope, and this very sentence — all of it reaches you through exactly two tricks played on straight-line light: the **bounce** and the **bend**.

The bounce (mirrors) creates a perfect impostor: a "you" standing behind the bathroom wall, in a space that doesn't exist, made of light that never went there. The bend (lenses) does something arguably more magical: it takes the chaos of rays flying off every point of the world and *re-sorts* them, point by point, into an image — on a camera sensor, on a cinema screen, on the living retina reading these words.

Mirrors that lie usefully and glass that gathers light: today you learn both tricks properly.

# Lore Introduction

In the Hall of Optics, Liora unveils the Academy's two oldest optical treasures. First, the Speculum — a flat silver mirror, polished by some forgotten master until, she claims, no eye can find its surface. You look in: an apprentice looks back, raising the wrong hand when you raise yours, standing in a duplicate hall that recedes *behind* the wall. Liora knocks on the silver. Solid. "Then where, precisely, is that hall?" Second treasure: a fat disc of flawless glass on an iron stand, aimed at the shuttered window. She opens the shutter; sunlight floods the disc — and gathers, on a slate held behind it, into a single furious point of brilliance that begins, gently, to smoke. "The first deceives your eye with perfect courtesy," she says. "The second herds ten thousand sunbeams into one. Bounce and bend, apprentice. Learn what each can and cannot do, and the seeing instruments of the world are yours."

# Core Learning

## Concept Introduction

**Plane mirrors — the courteous lie.** Reflection's equal-angles law, applied across a flat surface, makes every reflected ray travel *as if* it came from a point behind the mirror. The brain, trusting straight lines, sees an image with four fixed properties:

- **Virtual** — no light actually passes behind the glass; the image can't be caught on a screen
- **Upright** and **same size** as the object
- **As far behind** the mirror as the object is in front
- **Laterally inverted** — your raised right hand answers with its left (really a front-back flip; AMBULANCE is written mirrored so this flip un-flips it in rear-views)

**Lenses — refraction put to work.** A lens is glass curved so that refraction at its two surfaces tilts rays by *just the right amount at every height*:

| Lens | Shape | Effect on parallel rays | Jobs |
|------|-------|------------------------|------|
| **Converging (convex)** | Fatter in the middle | Bends them inward to a real **focal point** | Magnifiers, cameras, projectors, the eye, long-sight glasses |
| **Diverging (concave)** | Thinner in the middle | Spreads them as if from a virtual focus | Short-sight glasses, peepholes, some viewfinders |

**Focal length** = distance from lens to where parallel rays meet; shorter = stronger bending. Image behaviour follows from ray geometry: objects far beyond the focal length form **real, inverted** images (camera, projector); objects *inside* the focal length form **virtual, upright, magnified** ones (magnifying glass in action).

**The eye and its repairs.** Cornea plus lens converge light to a real (inverted) image on the retina. Short sight: convergence too strong → distant focus falls short → corrected by diverging lenses. Long sight: too weak → corrected by converging lenses. Spectacles are refraction, prescribed by the dioptre (1/focal length in metres).

## Why It Matters

- Cameras, projectors, microscopes, telescopes, spectacles, and your own eyes are assembled entirely from today's two tricks.
- The real/virtual distinction is the working grammar of optics: it decides what can be projected, photographed, or merely seen.
- Vision correction is the most widespread piece of personal physics on Earth — billions of faces carry this lesson on their noses.

## Worked Examples

**Example 1: The full-length mirror, half price**
To see your full height in a plane mirror, the mirror needs to be only *half* your height, at any distance — rays from your feet bounce at a mirror-point halfway up to your eyes (equal angles!). Mirror shops profit from customers who don't know the geometry.

**Example 2: A camera focusing**
A camera lens (f = 50 mm) photographs a distant hill: rays arrive parallel, image forms at 50 mm — sensor sits there. Now a face at 1 m: rays diverge slightly, focus falls *beyond* 50 mm, so the lens motors *forward* to keep the image on the sensor. The whirr of autofocus is focal geometry being solved in real time.

**Example 3: The magnifier's two personalities**
Hold a magnifying glass over print (inside the focal length): upright, enlarged, virtual — the reading-glass mode. Hold the same lens at arm's length toward a window: a tiny, upside-down, *real* image of the window hangs in the air (catch it on paper!). Same glass, two regimes, divided exactly at the focal length. Try it — it's the cheapest profound experiment in optics.

## Common Mistakes

- **Putting the mirror image "on the mirror"** — it sits as far behind as you stand in front; that's why focusing your camera on a mirror-image focuses *past* the glass.
- **Trying to project virtual images** — only real images (converging light) land on screens; virtual ones live only in the looking.
- **"Concave lens" / "concave mirror" confusion** — a concave *mirror* converges, a concave *lens* diverges; shape-words describe the surface, not the job.
- **Thinking lenses add light** — they only redirect it: a magnifier's bright spot is paid for by darkness around it (energy conservation patrols optics too).
- **Forgetting accommodation** — the eye changes its lens *shape* (not position) to refocus; cameras move glass, biology squeezes it.

## Mental Model

Mirrors and lenses are **traffic control for javelins of light**. A plane mirror is a perfectly disciplined wall of tiny deflectors: every javelin bounces with protocol-equal angles, and the crowd of deflected javelins is indistinguishable from one thrown from a phantom town behind the wall — hence the impostor you greet each morning. A converging lens is a roundabout engineer: it grabs the parallel motorway of javelins and tilts each lane inward — outer lanes hardest — so all converge on one junction (the focus). Every optical instrument ever built is just these two officers, posted in clever sequence.

## Mini Summary

- ✔ Plane mirror: virtual, upright, life-size image as far behind as you are in front
- ✔ Lateral inversion is a front-back flip — AMBULANCE knows
- ✔ Converging lens → real focus; diverging lens → virtual focus; focal length = strength
- ✔ Far objects → real inverted images (cameras); inside-focal-length → virtual magnified (magnifiers)
- ✔ Short sight ← diverging correction; long sight ← converging correction

# Guided Practice Quest

Work through the guided steps to place your mirror-twin precisely, measure a lens by sunlight, and prescribe spectacles from first principles.

# Solo Practice Quest

Three optical commissions: (1) *Mirror geometry*: with a small mirror, masking tape, and a wall, verify the half-height rule — mark the minimum mirror zone needed to see your full body, measure it, compare with your height. (2) *Lens metrology*: find any converging lens (magnifier, reading glasses, even a clear water bottle); focus a distant bright scene onto paper, measure the focal length, and describe the image's orientation. (3) *The two regimes*: with the same lens, write up the magnifier-vs-projector experiment from the worked example — sketch the ray situation in each regime and state where the boundary lies. Conclude with one paragraph: which instrument in your daily life uses each regime?

# Integration

**Biology**: The eye solves focus differently from every camera: ciliary muscles squeeze the lens fatter for near work (accommodation), a mechanism that stiffens with age — presbyopia — delivering most of humanity to the converging-lens counter by fifty. Squids, again, evolved the same optics independently; physics writes the spec, biology fills it twice.

**Engineering**: Real instruments stack today's parts: telescopes (big converger + eyepiece), microscopes (two-stage magnification), camera zooms (sliding lens groups solving focal geometry continuously). Precision lens-grinding — fractions of a wavelength — is among the most exacting manufacturing humans do; one smartphone carries six-plus aspheric lenses born of this lesson.

# Lore Conclusion

By lesson's end you have commanded both treasures: posted your phantom twin two metres into nonexistent space, and gathered the Sun to a smoking point you measured at a hand-span — *fifteen centimetres, the disc's true name*, as Liora puts it. She closes the shutter and, in the dimness, holds the great glass disc up against the white of her sleeve where earlier the prism had scattered its accidental rainbow. "One mystery remains in this Hall, and it has been winking at you for two days." She turns the prism slowly; a thin seam of colours crawls across the velvet — red, gold, green, violet. "White light, apprentice. Is it truly white? Tomorrow we take sunlight apart — and discover it was never one thing at all."
