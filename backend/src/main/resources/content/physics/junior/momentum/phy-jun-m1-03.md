---
id: phy-jun-m1-03
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: momentum
topicTitle: "Momentum"
topicSortOrder: 1
title: "Collisions and Explosions"
sortOrder: 3
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Classify collisions as elastic, inelastic, or perfectly inelastic
  - Track both momentum and kinetic energy through a collision
  - Analyse explosions as momentum-conserving energy releases
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes elastic (KE conserved) from inelastic (KE partly lost) and perfectly inelastic (objects couple)
    - Computes momentum and KE before/after for one collision, identifying the KE deficit's destination
    - Analyses an explosion — momentum zero throughout, KE created from a stored source
    - Chooses the correct collision type for real scenarios (crashes, billiards, atomic)
  keywords: [elastic, inelastic, perfectly inelastic, kinetic energy, deficit, explosion, stored, couple]
  modelAnswer: |
    All collisions conserve momentum; they differ in what happens to kinetic energy. Elastic
    collisions (ideal billiards, atomic scattering) conserve KE too. Inelastic ones lose some
    KE to heat, sound, and deformation; perfectly inelastic ones — objects sticking together —
    lose the most possible while still conserving momentum. Explosions run the film backwards:
    stored chemical or spring energy CREATES kinetic energy, while total momentum (zero for a
    body at rest) stays zero, so fragments carry equal-and-opposite momenta. Auditing both
    ledgers — p always balanced, KE traced to its destinations — solves any collision the
    Mechanica can stage.
guidedSteps:
  - id: phy-jun-m1-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Two railway wagons couple on impact and roll away together. This collision is:
    inputConfig:
      options:
        - "Elastic — momentum is conserved"
        - "Perfectly inelastic — they stick; maximum possible KE is lost while momentum is still conserved"
        - "Not a collision"
        - "Energy-conserving because nothing broke"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Perfectly inelastic — they stick; maximum possible KE is lost while momentum is still conserved"]
      rejectedFeedback: "Sticking = perfectly inelastic, the maximum-KE-loss case. Momentum conservation holds regardless (it always does in isolation); 'elastic' is a claim about KE, not p."
    hint: "The classification asks what happened to kinetic energy, not momentum."
    reflectionPrompt: "Where, physically, did the missing kinetic energy go in the coupling?"
  - id: phy-jun-m1-03-g2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      A 4 kg cart at 3 m/s couples with a stationary 2 kg cart. Show: (a) the shared final speed, (b) KE before and after, (c) the energy deficit and where it went. (3–4 sentences of working.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: ["2", "18", "12", "6", heat, sound, deform]
      rejectedFeedback: "p: 12 kg·m/s → v = 12/6 = 2 m/s. KE before: ½×4×9 = 18 J; after: ½×6×4 = 12 J. Deficit 6 J → heat, sound, and deformation in the coupling. Momentum intact, energy redistributed — both ledgers closed."
    hint: "Momentum first for v; then ½mv² on each side; then name the deficit's destination."
    reflectionPrompt: "Could a coupling collision ever lose ALL the kinetic energy? (What would momentum say?)"
  - id: phy-jun-m1-03-g3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      A firework shell at rest explodes into two unequal fragments. Which is TRUE immediately after?
    inputConfig:
      options:
        - "The fragments share the explosion energy equally"
        - "The fragments carry equal and opposite momenta; the lighter one moves faster and takes MORE of the kinetic energy"
        - "The heavier fragment moves faster"
        - "Total momentum is now large"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The fragments carry equal and opposite momenta; the lighter one moves faster and takes MORE of the kinetic energy"]
      rejectedFeedback: "From rest, total p stays zero: m₁v₁ = m₂v₂ oppositely. Equal momenta but KE = p²/2m — the SMALLER mass holds more energy. (Same reason the bullet, not the rifle, does the damage.)"
    hint: "Equal p; KE = p²/(2m). Which fragment divides by the smaller m?"
    reflectionPrompt: "Apply this to a rifle and bullet: equal momenta — why such unequal danger?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which quantity is conserved in EVERY isolated collision, elastic or not?"
    options: ["Kinetic energy", "Momentum", "Speed", "Force"]
    correctIndex: 1
    feedback: "Momentum is unconditional in isolation. KE survives only the elastic ideal; speed and force aren't conserved quantities at all."
  - type: MULTIPLE_CHOICE
    question: "In an explosion, kinetic energy:"
    options:
      - "Is conserved from before"
      - "Appears, converted from a stored source (chemical, spring), while momentum stays conserved"
      - "Is destroyed"
      - "Equals the momentum"
    correctIndex: 1
    feedback: "Explosions are energy conversions (stored → kinetic + heat + sound) under momentum's unbroken supervision — the inelastic collision run in reverse."
---

# Hook

Drop a steel ball-bearing on a steel plate: it rebounds to nearly its full height, clicking merrily, almost nothing lost. Drop a ball of putty: *splat* — total surrender, zero bounce. Drop a tennis ball: somewhere between, and forever less than you gave it.

All three impacts conserve momentum impeccably (count the Earth's share and the books close). What separates them is the *other* ledger — kinetic energy — and how much of it each impact launders into heat, sound, and dents. Physics sorts every collision in the universe along this one axis, from the perfect elastic click of colliding atoms to the perfect inelastic embrace of coupling wagons. And explosions? They're the same accounting run backwards: energy bursting *out* of storage while momentum, unimpressed, holds the total at zero. Master both ledgers and no impact — sporting, vehicular, or celestial — is beyond your audit.

# Lore Introduction

Vex has rebuilt the collision track into what the juniors call the Gallery of Impacts: stations along the brass rails, each staging a different argument between carts. At one, sprung steel plates — carts rebound with a ringing *ping*, gates reading speeds barely diminished. At another, clay pads — carts thud, stick, and crawl away as one. At a third, no collision at all: a single cart with a cocked spring and a release pin, ready to split itself in two. "The board taught you what never changes," Vex says, walking the line. "Today, what *does*. Same track, same law of momentum at every station — yet the ping and the thud are different events, and the difference is worth a fortune in the right industries." He hands you the gates' logbook. "Audit all three stations. Two ledgers each: the one that must balance, and the one that tells the story."

# Core Learning

## Concept Introduction

**The classification — by the fate of kinetic energy** (momentum is conserved throughout; that's settled law):

| Type | KE after vs before | Signature | Examples |
|------|--------------------|-----------|----------|
| **Elastic** | Equal (ideal) | Full rebound, no deformation | Atomic/molecular collisions, ideal billiards, steel ping |
| **Inelastic** | Reduced | Partial rebound, some heat/sound/denting | Almost every real macroscopic impact |
| **Perfectly inelastic** | Maximum possible loss | Objects couple, move as one | Coupling wagons, putty splat, tackles |

Perfectly elastic is an *ideal* at human scale (steel bearings come close); it is *routine* at atomic scale — gas molecules collide elastically trillions of times a second, which is why air never "wears out" its motion.

**The two-ledger method** (your complete collision toolkit):
1. **Momentum ledger** — always balances in isolation; use it first to find unknown velocities.
2. **Energy ledger** — compute KE before and after; the deficit is real energy, gone to heat, sound, and deformation. (Zero deficit? Elastic. Maximum deficit consistent with momentum? Perfectly inelastic.)

**Explosions: the reverse film.** A body at rest holds p = 0 and some *stored* energy (chemical, compressed spring). On release: fragments fly with momenta summing to zero — equal and opposite for two fragments — while KE *appears* from storage. The cruel arithmetic of recoil: equal momenta, but KE = p²/2m, so the **lighter fragment carries more energy** — bullets over rifles, payloads over launchers, the wrench over the astronaut.

## Why It Matters

- Vehicle safety engineering deliberately *chooses* inelasticity: crumple zones convert KE to deformation precisely so passengers don't keep it.
- Sports equipment is tuned restitution: golf-ball cores, trampoline beds, and "fast" cricket bats are all engineered positions on the elastic–inelastic axis (regulated by sporting law!).
- Particle physics reads collision ledgers to discover new particles: missing energy and momentum at a detector once revealed the neutrino.

## Worked Examples

**Example 1: The ping station, audited**
Two 1 kg carts, one at 2 m/s, one at rest, sprung-steel buffers. After: the mover stops dead; the target departs at 2 m/s. Momentum: 2 = 2 ✓. KE: 2 J = 2 J ✓ — elastic (the equal-mass special case: full velocity handover, the billiard player's stop-shot). Real gates read 1.97 m/s: 3% of energy paid as the audible *ping* — even rings cost joules.

**Example 2: Ballistics by putty**
A classic measurement: a 10 g bullet fires into a 2 kg clay block on a frictionless trolley; block-and-bullet roll off at 2.5 m/s. Momentum: 0.01 × u = 2.01 × 2.5 → u ≈ **500 m/s** — a bullet speed measured with kitchen apparatus. Check the energy ledger: KE before ≈ 1,250 J; after ≈ 6.3 J. *99.5% of the energy* became heat and clay-deformation — and that is normal for perfectly inelastic capture. The momentum ledger alone made the measurement possible.

**Example 3: The split cart**
Vex's spring-cart (3 kg total) splits into 1 kg and 2 kg halves; the light half exits at 4 m/s. Momentum: 0 = 1×(−4) + 2×v → v = 2 m/s opposite. Energy: appeared from the spring — KE = ½×1×16 + ½×2×4 = 12 J, of which the light half holds 8 J (two-thirds, on one-third of the mass). The spring's stored 12 J (plus a little sound) is the explosion's full budget: created kinetic energy, supervised momentum.

## Common Mistakes

- **"Momentum wasn't conserved — they stopped"** — count every participant (the Earth is a popular hiding place for momentum in wall-and-floor problems); in isolation, the total never moves.
- **Expecting energy conservation to fix velocities in sticky crashes** — KE genuinely *leaves the motion ledger*; only momentum survives to do algebra with.
- **Classifying by violence** — a gentle putty-touch is perfectly inelastic; a savage steel ping is near-elastic. The axis is energy fate, not drama.
- **Splitting explosion energy equally** — equal *momenta*, not energies; the light fragment takes the lion's share (p²/2m).
- **Forgetting elastic is real at atomic scale** — gas pressure's eternal drumming requires it; "ideal" doesn't mean "nowhere".

## Mental Model

Every collision is **a transaction watched by two accountants**. The momentum accountant is incorruptible and bored: whatever happens — embrace, rebound, detonation — her two columns match, and she initials the page without looking up. The energy accountant is the interesting one: he tracks where the motion-money *went*. Elastic deals return every coin to motion (he initials too). Inelastic deals pay commissions to heat and sound — he follows the leakage and names the recipients. Sticky mergers pay the maximum commission the momentum accountant will permit. And explosions are inheritance days: a vault (spring, powder) opens, motion-money floods out — and still, *still*, her columns match: the fragments' momenta cancel to the old total, every time.

## Mini Summary

- ✔ All isolated collisions conserve momentum; the classification is about kinetic energy's fate
- ✔ Elastic: KE survives (atomic collisions, steel pings); inelastic: KE leaks; coupling: maximum leak
- ✔ Two-ledger method: momentum finds velocities; energy names the losses
- ✔ Explosions create KE from storage under momentum's zero-sum supervision
- ✔ Equal momenta ≠ equal energies: the lighter fragment carries more KE (p²/2m)

# Guided Practice Quest

Work through the guided steps to classify a railway embrace, close both ledgers on a coupling, and divide a firework's inheritance the way physics actually does.

# Solo Practice Quest

Curate your own Gallery of Impacts: (1) *Measure restitution* — drop three different balls (steel/glass marble, tennis, putty or plasticine if you have it) from 1 m onto a hard floor; measure rebound heights, compute the fraction of energy retained per bounce (h₂/h₁), and place each ball on the elastic–inelastic axis. (2) *Two-ledger audit* — for the tennis ball's first bounce, write both ledgers explicitly: momentum (where did the Earth come in?) and energy (deficit and destinations). (3) *Explosion analysis* — film or imagine two stacked balls dropped together (basketball + tennis ball, the famous demo): describe the energy handover that fires the light ball skyward, and check it against the lighter-fragment-takes-the-energy rule. Conclude with one paragraph: which industry would pay most for mastery of YOUR gallery's physics, and why?

# Integration

**Engineering**: Restitution is a designed, regulated quantity: golf's governing bodies cap club-face "spring", crash standards mandate energy-absorption profiles, and railway buffers, ship fenders, and packaging foams are all purchased positions on the elasticity axis. The coefficient of restitution — rebound speed over arrival speed — is this lesson as a catalogue number.

**Mathematics**: The two-ledger method is simultaneous equations with physical meaning — and the elastic case's neat results (equal masses swap velocities) fall out of solving p and KE conservation together. The p²/2m form of kinetic energy, met here, becomes indispensable in quantum and nuclear physics, where momentum is often what you measure and energy what you infer.

# Lore Conclusion

Your audit of the three stations goes into the gates' logbook: the ping's 3% sound-tax, the clay's 99.5% laundering, the split cart's lopsided inheritance — every page double-initialled in the two-accountant style you've adopted without quite noticing. Vex reads it through, and this time he does speak: "Industry would hire you off this page alone. Resist them; we're not done." He clears the track entirely, and from the Mechanica's deep stores wheels out something new: a heavy gimballed flywheel on a tether, humming faintly as it spins, refusing — as you reach to steady it — to tilt the way your hand insists. "Straight-line motion is mastered," says Vex, with the air of a man saving the best for last. "But the universe, junior, is *built round*. Wheels, orbits, storms, atoms. Tomorrow we take momentum in a circle — and you will meet the gentle, relentless force that holds every circle closed."
