---
id: phy-jun-m3-04
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m3
moduleTitle: "Module 3: Thermodynamics"
moduleGlyph: "♨️"
moduleSortOrder: 3
topicSlug: energy_systems
topicTitle: "Energy Systems"
topicSortOrder: 2
title: "Internal Energy and the First Law"
sortOrder: 4
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Define internal energy as the sum of particles' kinetic and potential energies
  - State the First Law — internal energy changes by heat in and work done
  - Account for compression heating and expansion cooling
integrationDomains: [chemistry, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines internal energy (particle KE + particle PE) and what raises it
    - States the First Law as ΔU = heating + work done ON the system
    - Explains compression heating and expansion cooling with the law
    - Distinguishes heat, work, and internal energy as three different things
  keywords: [internal energy, first law, ΔU, heat, work, compression, expansion]
  modelAnswer: |
    Internal energy U is the energy stored inside matter itself: the sum of all its particles'
    kinetic energy (jiggling — read by temperature) and potential energy (bonds and
    separations — changed during state changes). The First Law of Thermodynamics is energy
    conservation written for such systems: ΔU = energy in by heating + work done on the system.
    Squeeze a gas quickly and your work raises U — the gas heats with no flame (the bike
    pump's warm barrel); let gas expand and push outward and it pays for that work from its
    own U — it cools (aerosol cans chill in use). Heat and work are TRANSFERS; internal energy
    is the ACCOUNT. The First Law is the rule that the account only changes by what crosses
    the boundary.
guidedSteps:
  - id: phy-jun-m3-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A bike pump's barrel grows hot during vigorous use — no flame anywhere. The First Law's account:
    inputConfig:
      options:
        - "Friction alone heats the barrel"
        - "Your compression strokes do WORK on the trapped air, raising its internal energy — the gas heats, and the barrel conducts it to your hand"
        - "Heat flows in from the cool surrounding air"
        - "Pumps contain heating elements"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Your compression strokes do WORK on the trapped air, raising its internal energy — the gas heats, and the barrel conducts it to your hand"]
      rejectedFeedback: "ΔU = heating + work ON the gas: rapid squeezing delivers work faster than heat can leak away, so U — and with it temperature — climbs. (A little friction helps, but compress a sealed syringe and feel the same heating with no rubbing parts.) Work became heat under your hand: the Foundry's founding fact."
    hint: "What crossed into the gas during each stroke — heat, or work?"
    reflectionPrompt: "Why does SLOW pumping stay cool? Which term of the First Law gets time to act?"
  - id: phy-jun-m3-04-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      An aerosol can grows cold as you spray. Why?
    inputConfig:
      options:
        - "The propellant was refrigerated at the factory"
        - "The escaping gas expands and does work pushing into the atmosphere — paid from its own internal energy, so it cools (plus evaporation of liquid propellant, same ledger)"
        - "Metal cans absorb heat when shaken"
        - "Cold is released from the nozzle"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The escaping gas expands and does work pushing into the atmosphere — paid from its own internal energy, so it cools (plus evaporation of liquid propellant, same ledger)"]
      rejectedFeedback: "Expansion is work done BY the gas: ΔU goes negative, temperature falls. Add the latent-heat bill of liquid propellant evaporating and the can chills sharply. Compression heats; expansion cools — one law, two signs."
    hint: "Who did work on whom as the gas pushed out — and from which account was it paid?"
    reflectionPrompt: "Where else have you felt expansion cooling? (Letting air out of a tyre valve counts.)"
  - id: phy-jun-m3-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Distinguish the three terms — heat, work, and internal energy — including which are transfers and which is a store, with one example of each. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [transfer, store, account, boundary, heating, work, internal]
      rejectedFeedback: "Internal energy is a STORE — the system's own account (particle KE + PE): a hot flask HAS high U. Heat and work are TRANSFERS — energy crossing the boundary: heat flows because of temperature difference (flame under flask); work is forced, organised transfer (piston squeezing gas). Systems HAVE internal energy; they never 'have' heat or work — those exist only in the crossing."
    hint: "One is a balance; two are transactions. Which is which?"
    reflectionPrompt: "Why is the everyday phrase 'this object contains a lot of heat' physics-illegal, and what should it say?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Internal energy of a gas consists of:"
    options:
      - "Only the heat added to it"
      - "The total kinetic energy of its particles plus the potential energy of their interactions"
      - "Its pressure times volume"
      - "The energy of its container"
    correctIndex: 1
    feedback: "U = all the jiggling (KE — temperature's domain) plus all the bond/separation energy (PE — state changes' domain). It's the inside-the-matter account that heating curves from the Apprentice tier were secretly tracking."
  - type: MULTIPLE_CHOICE
    question: "The First Law of Thermodynamics is, at heart:"
    options:
      - "Heat always flows hot to cold"
      - "Energy conservation, written for heat and work crossing a system's boundary: ΔU = Q + W"
      - "Engines cannot be built"
      - "Temperature equals energy"
    correctIndex: 1
    feedback: "The Apprentice conservation law, upgraded with bookkeeping symbols: the internal account changes only by what crosses the boundary as heat (Q) or work (W). (Hot-to-cold is the SECOND law — teeth arriving soon.)"
---

# Hook

In the 1840s, a Manchester brewer named James Joule — yes, *that* Joule — performed one of history's most quietly radical experiments: he churned water with a paddle-wheel driven by falling weights, and measured the water warming. No flame, no fire, no contact with anything hot. Pure mechanical work, vanishing into water — and reappearing, with exquisite bookkeeping consistency, as temperature: always the same temperature rise per unit of work. Work and heat, the experiment said, are *the same currency*.

The scientific establishment took years to believe him (he announced early results at a public lecture because journals weren't interested). Today his name is on the unit, and his discovery — energy's full constitution, the **First Law of Thermodynamics** — runs every engine, refrigerator, and living cell. You have already felt it twice this week without naming it: the bike pump that warms, the aerosol that chills. Today we open the master ledger.

# Lore Introduction

Calde has built Joule's apparatus from the original drawings: a brass paddle-wheel in an insulated barrel of water, driven by cords running over pulleys to two of the Foundry's standard weights. The Foundry's finest thermometer waits in the barrel. "The brewer's churn," she says with reverence. "When I was a junior, my master made me crank it for an hour. You get weights and gravity — count your blessings." She has you raise the weights and let them fall, again and again, the paddles churning, the cords humming. Ten descents. Twenty. The thermometer creeps — barely, but unmistakably — upward. "No flame, junior. No hot thing touched. Only falling weights, stirring." She taps the master ledger she's chalked on the vault wall — one line, three symbols, ΔU = Q + W. "Energy's constitution. Everything in this Foundry — engines, furnaces, your own warm hands at the crank — is governed under it. Churn until you believe it; then we read the law together."

# Core Learning

## Concept Introduction

**Internal energy (U): the account inside matter.** Every object's particles hold energy two ways:
- **Kinetic** — the jiggling/flying (this part IS what temperature reads)
- **Potential** — bonds and separations between particles (this part is what latent heat pays into during melting/boiling — the plateau mystery, finally named)

U is their total: the system's *internal account*. Hot, massive, or loosely-bonded systems hold more.

**The First Law: the constitution.**

```
ΔU = Q + W
change in internal energy = heat transferred IN + work done ON the system
```

It is the Apprentice conservation law, written for systems with boundaries: *the account changes only by what crosses the border*. Two distinct border-crossings:
- **Heat (Q)** — disorganised transfer, driven by temperature difference (flame, radiator, ice bath)
- **Work (W)** — organised, forced transfer (piston compressing, paddle churning, current driving a resistor)

Signs by sense: energy in (heating, being squeezed) is positive; energy out (cooling, the gas pushing outward) negative.

**The two signature phenomena, finally explained:**
- **Compression heating**: squeeze fast (Q ≈ 0, no time to leak) → W > 0 → ΔU > 0 → temperature climbs. Bike pumps, diesel ignition (yesterday's lighter, today's ledger).
- **Expansion cooling**: gas pushes outward, doing work on the world (W on gas < 0; Q ≈ 0) → ΔU < 0 → cools. Aerosols chill, released tyre air feels cold, rising air parcels cool (your weather lesson's engine, now constitutional).

**Three words, never to blur again:** systems *have* internal energy; heat and work *happen at boundaries*. "This flask contains much heat" is illegal; "much internal energy" is law-abiding.

## Why It Matters

- The First Law is the accounting framework for everything thermal: engine efficiency, metabolism, climate models, and refrigeration all submit their books to it.
- Joule's equivalence (work ↔ heat) is why one unit — his — measures food, fuel, electricity, and effort alike.
- Compression/expansion thermodynamics runs weather, engines, and cryogenics — and you've now felt both signs in your hands.

## Worked Examples

**Example 1: Joule's churn, audited**
Two 10 kg weights fall 2 m, ten times: W = mgh total = 2×10×10×2×10 = 4,000 J churned into the barrel's 5 kg of water. ΔT = Q/(mc) = 4,000/(5×4,200) ≈ **0.19 °C** — the creeping needle, predicted. Joule chased tenths of a degree with home-built thermometers for a decade; the constitution was ratified in increments of one-fifth of a degree.

**Example 2: The diesel stroke on the ledger**
Compression does ~600 J of work on the trapped charge in 20 ms — far too fast for leakage (Q ≈ 0): ΔU = +600 J, temperature soaring past 700 K, fuel igniting on contact. Power stroke: hot gas expands, doing work on the piston — paying from U, cooling as it pushes. An engine cycle is the First Law run round a loop: heat in from fuel, work out at the crankshaft, the difference exhausted. (HOW MUCH must be exhausted is the Second Law's business — next topic, with teeth.)

**Example 3: Why rising air makes weather**
A parcel of air ascending expands into lower pressure — pushing outward against its surroundings: work done BY the parcel, Q ≈ 0 (air is a poor conductor): ΔU falls, parcel cools ~1 °C per 100 m. Cooled enough → condensation → clouds, rain, and the latent-heat engine of storms (your Apprentice weather audit, now signed into constitutional law). Sinking air runs the ledger backwards: compression-warmed föhn winds melt alpine snow in hours.

## Common Mistakes

- **"Heat" as a possession** — heat exists only in transit; the stored quantity is internal energy. (Examiners, and physics, insist.)
- **Missing the work term** — gases warm with no flame and cool with no fridge; W is a full citizen of the ledger, not a footnote.
- **Confusing temperature with internal energy (again, at higher stakes)** — melting ice gains U with NO temperature change: the gain is all particle-PE. The heating-curve plateaus were ΔU's potential half all along.
- **Sign chaos** — declare your convention (ON the system positive) and audit every term against it; thermodynamics punishes sloppy signs ruthlessly.
- **Expecting Q = 0 always or never** — fast processes are nearly adiabatic (no heat exchange); slow ones nearly isothermal (temperature pinned). Rate decides which idealisation serves.

## Mental Model

Every system is **a walled bank with one account (U) and two tellers' windows**. The *Q window* handles disorganised cash — loose energy shuffling across whenever the neighbourhood outside is hotter or colder than the vault. The *W window* handles organised transfers — armoured deliveries: pistons, paddles, currents, forced and directed. The constitution (First Law) is the audit rule: *the vault's balance changes by exactly what passed the two windows, nothing else, ever*. Compression heating is an armoured delivery with the Q window shut (too fast); expansion cooling is the vault paying out a delivery of its own. And temperature? That's just the *jiggling half* of the vault's balance — which is why the balance can grow (melting) while the thermometer, watching only the jiggle, sees nothing.

## Mini Summary

- ✔ U = particle KE (temperature's half) + particle PE (latent heat's half) — the internal account
- ✔ First Law: ΔU = Q + W — the account changes only by boundary crossings
- ✔ Heat = disorganised transfer (needs ΔT); work = organised transfer (pistons, paddles, currents)
- ✔ Fast squeeze: W in, Q ≈ 0 → heats. Expansion: work out → cools. Two signs, one law
- ✔ Systems HAVE internal energy; heat and work only ever HAPPEN

# Guided Practice Quest

Work through the guided steps to convict the bike pump's shoulder-work, refund the aerosol's chill from its own account, and sort the three great words into stores and transfers forever.

# Solo Practice Quest

Three constitutional cases: (1) *Re-run Joule, bench scale*: vigorously shake a small sealed jar of water (or sand) for two timed minutes; estimate your mechanical input (shakes × force × distance — honest guesses) and predict the temperature rise for the contents' mass and c; measure if you can, and report the audit including where unmeasured joules went. (2) *Sign drills*: for six events — gas squeezed slowly; squeezed fast; expanding into a balloon; water boiling on a hob; coffee cooling; battery charging a phone — write the First Law with each term's sign and one-line justification. (3) *The weather parcel*: explain to a hill-walker, in three sentences with the ledger, why the summit breeze is colder than the valley air that rose to make it, and why descending föhn wind arrives warm. Close with the corrected version of "close the door, you're letting the heat out" — which, you'll find, survives the constitution intact.

# Integration

**Chemistry**: Chemical thermodynamics is this ledger with reactions as boundary events: bond energies are the PE half of U, exothermic reactions are vault payouts, and calorimetry — chemistry's bread and butter — is the First Law with a thermometer. Enthalpy, met at Senior tier, is U with a PV convenience-clause for open-air chemistry.

**Mathematics**: ΔU = Q + W is bookkeeping algebra: signed quantities, conservation as an invariant sum. The fast/slow idealisations (adiabatic/isothermal) are limiting cases — mathematical boundary behaviour you'll meet formally in calculus, and the P–V diagrams of the coming engine lesson will turn the ledger's entries into *areas under curves*: your graph toolkit's grandest assignment yet.

# Lore Conclusion

Your churn-audit closes within honest uncertainty of the thermometer's creep, and Calde countersigns it beneath the wall-ledger's three chalked symbols — then, in the Foundry tradition you only now learn exists, has you re-chalk the constitution's line yourself, fresh over the fading strokes of juniors before you. "Ratified," she says simply. "Energy: conserved, transferable by two windows, stored in one account. Joule's churn says no exceptions; a century and a half of engines agrees." She bars the vault for the night but pauses, lamp in hand, at the top of the stair. "And yet, junior — notice what the constitution does NOT say. It permits heat to become work, joule for joule, both directions equal." Her lamp throws her grin into shadow. "Then why does the Foundry's best engine waste two joules in three up its chimney? Why does your tea never UN-cool? The First Law permits it all. Something else — something deeper — forbids. Two lessons hence, you meet the forbidder. First: tomorrow we learn to DRAW the ledger — flows, widths, and losses — the engineer's way. Bring a ruler."
