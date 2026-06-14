---
id: phy-jun-m4-05
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m4
moduleTitle: "Module 4: Applied Physics"
moduleGlyph: "🔧"
moduleSortOrder: 4
topicSlug: fluid_physics
topicTitle: "Fluid Physics"
topicSortOrder: 2
title: "Buoyancy and Archimedes' Principle"
sortOrder: 5
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - State Archimedes' principle and compute upthrust
  - Predict floating, sinking, and flotation depth from the principle
  - Apply buoyancy to ships, submarines, balloons, and density measurement
integrationDomains: [engineering, history]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - States upthrust = weight of fluid displaced and computes it
    - Applies the floating condition — displaced fluid's weight equals the floater's weight
    - Explains submarine/balloon control as buoyancy management
  keywords: [Archimedes, upthrust, displaced, weight of fluid, float, submarine, ballast]
  modelAnswer: |
    Archimedes' principle: a body in fluid receives an upthrust equal to the WEIGHT OF FLUID IT
    DISPLACES — the net result of pressure pushing harder on its deeper surfaces than its
    shallower ones. A 0.002 m³ stone in water displaces 2 kg of water: 20 N of upthrust,
    whatever the stone weighs. Floating bodies settle where displaced weight equals their own:
    a ship sinks to the waterline at which its hull has pushed aside exactly its own weight of
    sea. Submarines manage the balance with ballast tanks (flood to sink, blow to rise);
    balloons float in air by displacing more air-weight than they total; and the crown affair
    was solved by comparing displaced volumes. Load lines, lifejackets, and icebergs all keep
    the same accounts.
guidedSteps:
  - id: phy-jun-m4-05-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A 0.002 m³ stone is fully submerged in water (ρ = 1000 kg/m³, g = 10 N/kg). Upthrust = weight of water displaced = ________ N.
    inputConfig:
      placeholder: "20"
    markingRule:
      matchMode: CONTAINS
      accepted: ["20"]
      rejectedFeedback: "Displaced water: 0.002 × 1000 = 2 kg, weighing 20 N. The stone feels 20 N of lift regardless of its own weight — a 60 N stone reads 40 N on an underwater scale ('apparent weight')."
    hint: "Volume × fluid density × g."
    reflectionPrompt: "Why does the stone feel lighter underwater but not light ENOUGH to float?"
  - id: phy-jun-m4-05-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A loaded cargo ship floats LOWER in the water than when empty because:
    inputConfig:
      options:
        - "The cargo pushes the water away"
        - "Floating requires displaced water's weight to equal the ship's weight — more total weight demands a deeper waterline to push aside more water"
        - "The hull stretches"
        - "Salt sticks to the hull"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Floating requires displaced water's weight to equal the ship's weight — more total weight demands a deeper waterline to push aside more water"]
      rejectedFeedback: "The floating condition is an exact balance: upthrust (displaced weight) = ship's weight. Add cargo and the ship settles until the extra submerged hull displaces exactly the cargo's weight. The Plimsoll line on every hull marks the legal limit of this settling — Archimedes, enforced by maritime law."
    hint: "What must the displaced water weigh, for anything that floats?"
    reflectionPrompt: "Why does the same ship ride higher in salt water than fresh? (Check ρ.)"
  - id: phy-jun-m4-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain how a submarine dives, hovers, and surfaces using its ballast tanks — in Archimedes' language. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [ballast, flood, water, weight, upthrust, equal, blow, compressed air, average density]
      rejectedFeedback: "The hull's volume — hence its upthrust — is fixed; the submarine adjusts its WEIGHT. Flooding ballast tanks with seawater raises weight above upthrust: it sinks. Blowing the tanks with compressed air expels the water, weight drops below upthrust: it rises. Trimmed exactly equal, it hovers — neutrally buoyant, the fish's trick (swim bladders) done with valves."
    hint: "Upthrust is fixed by hull volume. Which side of the balance can the crew change, and how?"
    reflectionPrompt: "Why do submarines carry compressed air rather than relying on pumps alone — and what's the emergency value of 'blowing ballast'?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Archimedes' principle states the upthrust on a submerged body equals:"
    options:
      - "The body's weight"
      - "The weight of the fluid the body displaces"
      - "The body's volume"
      - "Atmospheric pressure"
    correctIndex: 1
    feedback: "Displaced fluid's WEIGHT — the net of deeper-harder pressure pushing up versus shallower-softer pushing down. It cares about the body's volume and the fluid's density; the body's own weight only decides whether the upthrust suffices."
  - type: MULTIPLE_CHOICE
    question: "An iceberg (ρ ≈ 917 kg/m³) floats in seawater (ρ ≈ 1025) with roughly what fraction submerged?"
    options:
      - "Half"
      - "About 90% — the density ratio 917/1025, hence 'tip of the iceberg'"
      - "10%"
      - "It depends on its shape"
    correctIndex: 1
    feedback: "Floating fraction submerged = ρ_body/ρ_fluid ≈ 0.89: about nine-tenths under the waterline, shape-independent. The Titanic's lookout saw only the tithe."
---

# Hook

A modern container ship is a quarter of a million tonnes of steel and cargo — steel, which sinks like the iron nut in your pocket — yet it floats, and floats *predictably to the centimetre*, so reliably that international law paints a line on the hull (the Plimsoll line) marking exactly how deep it may legally settle. The physics underwriting every voyage, every lifejacket, every submarine and hot-air balloon, was discovered in a bathtub twenty-three centuries ago by a man who reportedly forgot his towel in the excitement.

You met the crown story in your Apprentice density lesson. Today you get the *principle* behind it in full: the upthrust on anything in fluid equals **the weight of fluid it pushes aside** — a law that follows, beautifully, from yesterday's ρgh (deeper water pushes harder on a body's bottom than its top: the difference IS the lift). One sentence from Syracuse, and the seas, the skies, and the fish all keep its accounts.

# Lore Introduction

Vex has borrowed the Foundry's quenching trough and rigged above it the Mechanica's finest spring balance. Your homework trio — cork, apple, iron nut — wait on the bench beside a model hull, a bag of lead shot, and something under a cloth. "The shouting Greek's principle, properly weighed," he announces. He hangs the nut from the balance: 5.4 newtons in air. Lowers it into the water: the needle falls to 4.7. "The water lifts it by seven-tenths of a newton, junior. Why that number? Not less, not more?" He catches the overflow from a brimful vessel in a beaker, weighs the spilled water — seven-tenths of a newton, to the needle's width. The Mechanica goes quiet the way it does when an old law performs. "The lift equals the spill's weight. Always. For nut, ship, swimmer, and balloon. Today we run the books on everything that floats — and the cloth comes off when you've earned it."

# Core Learning

## Concept Introduction

**The principle and its origin.** A body in fluid displaces (pushes aside) its submerged volume's worth of fluid. Yesterday's ρgh acts harder on the body's deeper surfaces than its shallower ones; the net upward result — integrate the pushes — is exactly:

```
upthrust = weight of fluid displaced = ρ_fluid × V_submerged × g
```

Note whose properties appear: the *fluid's* density, the *submerged* volume — never the body's weight. The body's weight only decides the verdict:

- **Sinks**: weight > maximum upthrust (fully submerged still insufficient) — body denser than fluid
- **Floats**: body settles at the waterline where **displaced weight = body weight** — the floating condition, exact and self-adjusting (load cargo → settle deeper → displace more → re-balance: the Plimsoll line is this equation's legal maximum)
- **Hovers** (neutral): weights match at full submersion — the submarine's and the fish's trim

**Floating fraction**: a uniform floater submerges the fraction ρ_body/ρ_fluid (iceberg: ~90%; oak: ~70%; you, lungs full: ~98% — hence swimming's marginal arithmetic and the lifejacket's job: add volume, not subtract weight).

**Buoyancy managed — the applications:**
- **Ships**: hulls enclose air to make average density < water (your Apprentice insight), then settle to the load's waterline; stability adds yesterday's CG geometry (ballast low!)
- **Submarines**: fixed hull volume, adjustable *weight* — flood ballast tanks to dive, blow with compressed air to rise, trim to hover
- **Balloons**: Archimedes in air — hot air or helium makes total weight < displaced air's weight; altitude trims as air thins
- **The crown, completed**: equal weights, unequal volumes → unequal displaced weights → the scale dips underwater; fraud by upthrust differential

## Why It Matters

- Naval architecture is this lesson with paperwork: displacement tonnage, load lines, reserve buoyancy, and damage stability all cite Archimedes by name.
- Buoyancy management spans submarines, ROVs, divers' BCDs, fish bladders, weather balloons, and airships — one balance, many trims.
- The apparent-weight method (weigh in air, weigh submerged) remains a standard density/volume measurement from gemology to metallurgy — the crown method, in labs today.

## Worked Examples

**Example 1: Sizing a one-tonne boat, properly**
Your Apprentice estimate returns with the law behind it: a 1,000 kg boat must displace 1,000 kg of water = 1 m³ below the waterline. Hull 2 m × 1 m: it settles 0.5 m deep. Add 400 kg of passengers: settles 0.2 m deeper (0.4 m³ more displacement). Freeboard remaining decides the legal load — you have just derived a Plimsoll calculation.

**Example 2: The balloon's budget, in air**
Air at sea level weighs ~12 N per m³. An 800 m³ hot-air balloon displaces ~9,600 N of cool air; its heated contents weigh ~7,300 N (Charles's law thinned them), envelope and basket ~1,400 N: lift margin ~900 N — pilot and passengers. Climb thins the outside air (less displaced weight) until margin zero: the ceiling. Archimedes doesn't care that the fluid is air; the books are identical.

**Example 3: The Cartesian diver (the cloth comes off)**
Vex's hidden exhibit: a sealed bottle of water containing a small inverted vial with an air bubble — squeeze the bottle and the diver sinks; release and it rises. The mechanism is the whole module in miniature: squeezing pressurises the water (Pascal), compressing the bubble (Boyle), shrinking the diver's displaced volume (Archimedes) — upthrust falls below weight: sink. Three lessons, one toy, and the exact principle of a submarine's emergency physics.

## Common Mistakes

- **"Upthrust equals the body's weight"** — only for floaters at equilibrium; the principle says displaced FLUID's weight, always.
- **Forgetting upthrust acts in air** — you are buoyed ~0.8 N right now; helium balloons are Archimedes, not anti-gravity; precision weighing corrects for it.
- **Shape mysticism** — shape sets how much volume submerges at a given depth (and stability), but the floating fraction of a solid is pure density ratio; icebergs of any sculpture float nine-tenths under.
- **Submarines 'swimming' up and down** — primary control is weight (ballast), not propulsion; planes (fins) assist underway.
- **Ignoring the fluid's density changes** — salt vs fresh (ships ride higher at sea — the Plimsoll line has multiple marks!), cold vs warm, altitude-thinned air: ρ_fluid is a live variable in every account.

## Mental Model

Every object in fluid is **standing on an invisible set of scales operated by the fluid itself**. The fluid's offer is firm and impartial: *"I will push up with exactly the weight of whatever you shove aside — no more, no less."* A sinker shoves aside less than its own weight even fully under: offer insufficient; down it goes, the shortfall felt as (reduced) apparent weight. A floater takes the deal partway in: it settles until the shoved-aside water's weight matches its own — self-adjusting, to the gram, which is why loading settles ships by computable centimetres. The submarine renegotiates its own side of the deal (weight, via ballast); the balloon strikes the same bargain with the ocean of air. The fluid never cheats, never tires, and has honoured the same posted offer since Syracuse.

## Mini Summary

- ✔ Upthrust = weight of fluid displaced (ρ_fluid × V_submerged × g) — from ρgh's depth difference
- ✔ Floating condition: displaced weight = body weight; settles self-adjustingly (Plimsoll lines)
- ✔ Floating fraction = ρ_body/ρ_fluid (icebergs ~90% under)
- ✔ Trim the balance: ships (enclosed air), submarines (ballast weight), balloons (thinned air)
- ✔ Works in any fluid, air included; ρ_fluid is a live variable (salt, heat, altitude)

# Guided Practice Quest

Work through the guided steps to weigh a stone's twenty-newton lift, settle a cargo ship to its lawful line, and run a submarine's dive-and-surface on valves and Archimedes alone.

# Solo Practice Quest

Three voyages at the trough: (1) *Verify the Greek*: weigh a sinkable object with a hanging scale (or improvise with a rubber band's stretch) in air, then submerged; catch or compute the displaced volume and confirm the lift equals the displaced weight within your uncertainty. (2) *Build the diver*: construct a Cartesian diver (bottle, pen-cap with blu-tack, water) and write its three-law explanation (Pascal → Boyle → Archimedes) in four sentences. (3) *Load-line your own hull*: float a plastic tub, mark its empty waterline, add measured masses, and predict-then-mark each new line via displacement arithmetic; report the agreement. Close with the lifejacket's specification in one sentence of Archimedes — what does it add, and what must it NOT add?

# Integration

**Engineering**: Naval architecture builds on today's balance: reserve buoyancy and compartmentalisation (why one hole needn't sink a ship — and why the Titanic's contiguous floods did), stability curves marrying Archimedes to centre-of-gravity geometry, submarine trim systems, and offshore platforms ballasted like standing submarines. Hydrometers — the brewer's floating gauge from your density lesson — are Archimedes sold by the instrument.

**History**: The crown affair (c. 250 BC) is antiquity's most famous applied-physics commission, and Archimedes' *On Floating Bodies* survives as the first mathematical treatment of a physical law — propositions, proofs, and all. Galileo's first published work was a little balance based on it; the Plimsoll line (1876) made it labour law after decades of overloaded 'coffin ships'; the principle has been saving sailors for longer than most nations have existed.

# Lore Conclusion

By evening the trough's ledger is full: the nut's lift weighed and matched to its spill, the model hull load-lined to your chalk marks, the Cartesian diver — the cloth-borne exhibit, earned — bobbing to your squeeze while you recite its three-law confession. Vex stoppers the bottle and sets it on the shelf of honoured apparatus. "The Greek's accounts, audited and passed." He banks the lamps, but the mill-race's voice fills the pause — the water running quick and ceaseless over the weir outside. "We have weighed still water, junior. But water that MOVES plays by additional rules — it speeds through narrows, lifts wings, sings in pipes, and turns that wheel out there with the Academy's whole evening's light on its shoulders. Tomorrow: flow — and the strange, lovely fact that fast fluid pushes SOFTER. Bring the apple. We'll float it down the race and read its journey."
