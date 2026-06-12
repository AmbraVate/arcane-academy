---
id: phy-jun-m1-07
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: gravitation
topicTitle: "Gravitation"
topicSortOrder: 3
title: "Universal Gravitation"
sortOrder: 7
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - State Newton's law of universal gravitation qualitatively and quantitatively
  - Explain the inverse-square law and its consequences
  - Connect the falling apple and the orbiting Moon as one phenomenon
integrationDomains: [history, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States that every mass attracts every other, proportional to both masses and inversely to distance squared
    - Uses the inverse-square rule (double the distance, quarter the force)
    - Explains why everyday objects' mutual attraction is unnoticeable (G is tiny)
    - Connects falling objects and orbits as the same force at work
  keywords: [universal, attract, inverse square, G, masses, distance, apple, Moon]
  modelAnswer: |
    Newton's law of universal gravitation: every mass attracts every other with a force
    F = G m₁m₂ / r², proportional to both masses and falling with the square of the distance
    between their centres. The constant G is minuscule (6.67 × 10⁻¹¹ N·m²/kg²), which is why
    two people feel no pull between them — only planet-sized masses make gravity obvious.
    Doubling the separation quarters the force; tripling it cuts the force to a ninth. The
    law's triumph was unification: the apple falling and the Moon circling are the SAME force,
    differing only in distance and in the Moon's sideways speed — the Moon falls toward Earth
    continuously and perpetually misses.
guidedSteps:
  - id: phy-jun-m1-07-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Two asteroids attract each other with force F. If the distance between them doubles, the force becomes:
    inputConfig:
      options:
        - "F/2"
        - "F/4 — inverse square: double the distance, quarter the force"
        - "2F"
        - "Unchanged"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["F/4 — inverse square: double the distance, quarter the force"]
      rejectedFeedback: "F ∝ 1/r²: distance enters squared and inverted. ×2 distance → ÷4 force; ×3 → ÷9; ×10 → ÷100. The inverse-square signature runs gravity, light intensity, and much else."
    hint: "r appears SQUARED in the denominator."
    reflectionPrompt: "At ten times the distance, what fraction of the force remains — and why does gravity nonetheless 'reach' across the solar system?"
  - id: phy-jun-m1-07-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      You and a friend (70 kg each) stand 1 m apart. The gravitational force between you is roughly:
    inputConfig:
      options:
        - "About 3 newtons — easily felt"
        - "About 0.0000003 N — a third of a millionth of a newton, hopelessly unnoticeable"
        - "Exactly zero"
        - "Equal to your weight"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["About 0.0000003 N — a third of a millionth of a newton, hopelessly unnoticeable"]
      rejectedFeedback: "F = Gm₁m₂/r² = 6.67×10⁻¹¹ × 70 × 70 / 1 ≈ 3×10⁻⁷ N — the weight of a few grains of dust. Real, universal, and utterly swamped: G's tininess is why gravity is the FEEBLEST force, noticeable only when one mass is planetary."
    hint: "G = 6.67 × 10⁻¹¹ — count those leading zeros."
    reflectionPrompt: "If gravity is so feeble, why does it end up ruling the cosmos? (What do the other forces do at large scales that gravity never does?)"
  - id: phy-jun-m1-07-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain Newton's great unification: in what sense are the falling apple and the orbiting Moon doing the SAME thing? Include why the Moon never lands. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [same force, falls, falling, sideways, tangential, misses, curve, Earth]
      rejectedFeedback: "Both are bodies accelerating toward Earth under the same gravitational pull, weakened for the Moon by distance (inverse square). The Moon, though, carries enormous sideways velocity: it falls toward Earth continuously, but its tangential motion carries it past — the surface curves away beneath as fast as the Moon falls. An orbit is a fall that perpetually misses."
    hint: "Both fall. One has no sideways speed; the other has a kilometre per second of it."
    reflectionPrompt: "What would happen to the Moon if Earth's gravity were switched off mid-orbit? (You know this law by name.)"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Newton's law of gravitation says the attractive force is proportional to:"
    options:
      - "The sum of the masses, divided by distance"
      - "The product of the masses, divided by the square of the distance"
      - "The larger mass only"
      - "The masses' difference"
    correctIndex: 1
    feedback: "F = Gm₁m₂/r² — product of masses (both matter, multiplicatively), inverse square of centre-to-centre distance."
  - type: MULTIPLE_CHOICE
    question: "The Earth pulls the Moon with force F. The Moon pulls the Earth with:"
    options:
      - "A much smaller force"
      - "Exactly F, oppositely directed — Newton's third law admits no exceptions for size"
      - "Nothing — moons don't pull planets"
      - "A force that varies with the tides"
    correctIndex: 1
    feedback: "Equal and opposite, always. The Moon's equal pull on Earth is no curiosity — it raises our tides and makes Earth wobble measurably around the pair's shared balance point."
---

# Hook

The story is so famous it's furniture: an apple falls in an orchard, and a young man asks the question that rebuilt the universe. But the genius wasn't noticing that apples fall — everyone knew that. It was the *second* glance, upward: the Moon hangs in the same sky. And the heretical thought: *what if the Moon is falling too?*

Falling — but also moving sideways at a kilometre per second, so that it eternally misses. Newton did the arithmetic: if the same Earth-pull that grips the apple weakens with the square of distance, then at the Moon's distance (sixty Earth-radii) it should be 3,600 times feebler — and the Moon's gentle curve toward Earth, computed from its orbit, matched *exactly*. One law, from orchard to sky: **every mass attracts every other**, including — right now — you attracting this screen, the screen attracting you, and both of you attracting Jupiter. The pull is universal. Only its *size* keeps secrets, and today you learn the formula that opens them.

# Lore Introduction

Before dawn, as promised, Vex leads you not to the Mechanica but up — to Thorne's territory, the Celestial Observatory itself, where your apprenticeship began. The old magus waits at the great telescope with two objects laid on velvet: an apple, slightly bruised, and a moon-chart. "Vex tells me you can price any circle in his workshop," says Thorne. "Tonight, one final invoice." He gestures to the eyepiece: the Moon, vast and grey and patient. "There hangs a boulder the size of a continent, circling at a kilometre a second. By your own Court of Demands, *something* must pay its centripetal bill — continuously, faultlessly, for four billion years. No wire. No wall. No banked timber." He places the apple in your hand. "The old Academy taught that the heavens ran on different laws than the orchard. One man ended that teaching with the question you are about to spend a morning answering: *what if it is the same pull?* Weigh the apple, junior. Then weigh the sky."

# Core Learning

## Concept Introduction

**The law.** Every mass in the universe attracts every other mass along the line joining their centres:

```
F = G m₁ m₂ / r²
```

- **Proportional to both masses** (product, not sum — double either mass, double the force)
- **Inverse-square in distance**: ×2 distance → ÷4 force; ×3 → ÷9; ×10 → ÷100. (Geometry's fingerprint: the same "influence spreading over spheres" pattern as light intensity.)
- **G = 6.67 × 10⁻¹¹ N·m²/kg²** — the universal constant, and it is *tiny*. Two friends a metre apart attract with ~10⁻⁷ N: real, measurable (Cavendish managed it in 1798 with a torsion balance — "weighing the Earth"), and utterly unnoticeable. Gravity only emerges from the noise when at least one mass is planetary.
- It acts **between centres** (for spheres), reaches across vacuum, cannot be screened, and — third law, no exceptions — pulls *both* parties equally: the apple pulls the Earth upward with its full weight; Earth's m makes its response invisible.

**Weight, re-derived.** Your weight IS this law with m₁ = you, m₂ = Earth, r = Earth's radius: W = GmM/R² — and comparing with W = mg reveals g = GM/R²: little-g is the law evaluated at the surface. (Why g ≈ 9.8 everywhere at sea level, why it's 1.6 on the smaller Moon, 24.8 on Jupiter — next lesson's business.)

**The unification — apple and Moon.** Both *fall* toward Earth. The apple, with no sideways speed, meets the ground. The Moon, sixty Earth-radii out, feels Earth's pull diluted 3,600-fold (inverse square) — and carries 1 km/s of tangential velocity, so each second it falls about 1.4 millimetres toward Earth *while the Earth's curve recedes 1.4 millimetres beneath it*. An orbit is a fall that never lands: gravity is the centripetal supplier, mv²/r = GmM/r², and the heavens joined the Mechanica's payroll.

## Why It Matters

- This is the founding example of *unification* — physics' deepest ambition: one law where there were two worlds. Electricity-and-magnetism, then light, followed the template (your Senior tier holds them).
- Every orbital mission, tide table, and planetary discovery (Neptune was *predicted* from gravitational bookkeeping before any telescope saw it) runs on this formula.
- Inverse-square reasoning transfers everywhere: light, sound intensity, radiation safety — learn it here, own it everywhere.

## Worked Examples

**Example 1: Newton's Moon test, re-run**
Surface fall rate: g = 9.8 m/s². Moon's distance: 60 Earth-radii → predicted fall rate 9.8/60² = 9.8/3600 ≈ 0.0027 m/s². Check against the orbit: a = v²/r with v = 1,022 m/s, r = 3.84×10⁸ m → 1022²/3.84×10⁸ ≈ **0.0027 m/s²**. The orchard's law, tested on the sky, to three figures. This single agreement is among the most consequential calculations ever performed.

**Example 2: Your weight on the law's own terms**
W = GmM/r² for m = 70 kg: at Earth's surface (r = 6.37×10⁶ m, M = 5.97×10²⁴ kg) → ~686 N ✓. Climb Everest (+9 km): r grows 0.14% → weight falls ~0.3% — about two newtons, the weight of an apple, lost to altitude. The inverse square is gentle at human scales and ruthless at astronomical ones.

**Example 3: The tides, sketched**
The Moon pulls Earth's near-side ocean *harder* than Earth's centre, and the centre harder than the far ocean (inverse square at three slightly different r's). Result: water heaps on BOTH sides — the near bulge pulled ahead of the planet, the far bulge left behind — and Earth rotates beneath two bulges: two high tides a day. Differential gravity, straight from the r² in the denominator. (The Sun plays the same game at half strength: spring and neap tides are the two players aligning and quarrelling.)

## Common Mistakes

- **"Gravity needs contact or air"** — it spans vacuum untroubled; it is the non-contact force par excellence (Apprentice Module Two's family tree, now with its law).
- **Sum instead of product** — F ∝ m₁ × m₂; doubling one mass doubles F, doubling both quadruples it.
- **Linear distance thinking** — twice as far is NOT half the pull; the square in r² is where most wrong answers are born.
- **"The Moon doesn't fall"** — it falls perpetually; it also perpetually misses. Orbiting IS falling with sufficient sideways speed.
- **"Earth pulls the Moon harder than the Moon pulls Earth"** — equal and opposite, by third law; the *accelerations* differ (a = F/m), not the forces.
- **Forgetting r is centre-to-centre** — standing on Earth, your r is the planet's radius, not zero.

## Mental Model

Imagine every mass in the universe holding **invisible elastic threads to every other mass** — every grain, person, planet, and star, all mutually threaded. Each thread's tension follows one rule: proportional to both holders' masses, fading with the square of the stretch. Between people the threads are gossamer beyond feeling; gather 6 × 10²⁴ kg into a ball beneath your feet and the bundled threads become the taut cable you've called *weight* all your life. And the Moon? It leans away at a kilometre per second, its thread-bundle to Earth perpetually taut, perpetually turning flight into circle: the heavens are not held UP — they are held *in*, by the same gossamer, bundled.

## Mini Summary

- ✔ F = Gm₁m₂/r² — every mass attracts every other, product of masses, inverse square of separation
- ✔ G is tiny: gravity is feeble until a mass is planetary; Cavendish measured it (and so weighed the Earth)
- ✔ g = GM/R²: surface gravity is the law evaluated at the surface
- ✔ Orbits are perpetual falls: gravity is the centripetal supplier (mv²/r = GmM/r²)
- ✔ Both parties are pulled equally (third law); differential pulls across a body raise tides

# Guided Practice Quest

Work through the guided steps to quarter a force by doubling a distance, weigh the gossamer between friends, and write the apple and the Moon into one sentence of law.

# Solo Practice Quest

Three audits of the universal banker: (1) *Re-run Newton's test yourself*: from g = 9.8 m/s² and the Moon at 60 Earth-radii, predict the Moon's centripetal acceleration; then compute it independently from v = 2πr/T (T = 27.3 days, r = 3.84×10⁸ m) and compare — show both chains fully. (2) *Personal gravity*: compute the attraction between you and (a) a friend at 1 m, (b) your house (~2×10⁵ kg) at 10 m, (c) the Moon overhead — and rank them against the weight of a single hair (~10⁻⁵ N). (3) *Inverse-square fluency*: a satellite at 2 Earth-radii from the centre — what fraction of surface weight does it feel? At what r would your weight halve? Close with two sentences on why this feeblest of forces nonetheless architects galaxies (consider: can mass be negative? can gravity be screened or neutralised like charge?).

# Integration

**History**: The 1687 *Principia* — the law plus the three laws of motion — is commonly ranked the most consequential scientific book ever printed; Halley funded it, predicted his comet with it, and Le Verrier later *computed Neptune into discovery* from Uranus's gravitational misbehaviour (1846): a planet found at the tip of a pen. The law reigned untouched until 1915, when Einstein re-described it as curved spacetime — your Senior tier holds that appointment.

**Mathematics**: The inverse square is geometry incarnate — influence spread over spheres of area 4πr² — and you'll meet its siblings in light, sound, and electric fields. Cavendish's G unlocks a chain of cosmic algebra: G plus g gives Earth's mass; orbits then weigh the Sun; binary stars weigh each other. One tiny constant, and the scales of the universe open.

# Lore Conclusion

Dawn comes up over the Observatory as your two calculations — orchard-law prediction and orbit-clock measurement — meet on the slate at 0.0027, agreeing to the width of a chalk line. Thorne regards the figure for a long moment, the way other men regard cathedrals. "Three figures of agreement, across a quarter-million miles, from an apple," he says. "I have shown that calculation to forty years of juniors and it has not once become ordinary." He returns the bruised apple to its velvet with full honours. Vex, who has watched the whole dawn in uncharacteristic silence, finally speaks: "The banker is identified. Tomorrow we audit his branch offices — why he pays 9.8 here, less on mountains, a sixth on the Moon; what his ledgers look like as *fields*. And then—" he glances at the telescope, and for once the dry voice carries something like hunger, "—we put our own coins in orbit."
