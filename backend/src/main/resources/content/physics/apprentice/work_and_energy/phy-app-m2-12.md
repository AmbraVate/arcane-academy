---
id: phy-app-m2-12
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: work_and_energy
topicTitle: "Work and Energy"
topicSortOrder: 4
title: "Conservation of Energy"
sortOrder: 12
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - State the law of conservation of energy
  - Track energy transfers through a chain of stores
  - Use efficiency to account for energy "lost" to heat
integrationDomains: [engineering, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States that energy is neither created nor destroyed, only transferred between stores
    - Traces a full energy chain for a real process
    - Uses efficiency = useful output / total input correctly
    - Explains that "lost" energy is dissipated (usually as heat), not destroyed
  keywords: [conservation, transferred, store, dissipated, efficiency, heat, useful, total]
  modelAnswer: |
    Energy is never created or destroyed — only transferred between stores: chemical to
    kinetic, kinetic to potential, anything to thermal. A pendulum trades PE and KE each swing,
    and the small shortfall each cycle is not destruction but dissipation: air resistance and
    pivot friction transfer energy to thermal stores, warming the surroundings imperceptibly.
    Efficiency = useful output ÷ total input: a 25%-efficient petrol engine turns a quarter of
    its fuel's chemical energy into motion and the rest into heat. Every energy audit must
    balance — if joules seem missing, find the store they leaked into.
guidedSteps:
  - id: phy-app-m2-12-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A pendulum swings lower each cycle until it stops. Its original energy has been:
    inputConfig:
      options:
        - "Destroyed by friction"
        - "Used up by the motion"
        - "Transferred to thermal energy in the air and pivot — the surroundings are very slightly warmer"
        - "Stored back in the hand that released it"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Transferred to thermal energy in the air and pivot — the surroundings are very slightly warmer"]
      rejectedFeedback: "Energy is never destroyed or 'used up'. Air resistance and pivot friction do negative work on the bob, transferring its energy to thermal stores. The joules all survive — just spread out and warm."
    hint: "The law forbids destruction. So where could the energy have GONE?"
    reflectionPrompt: "Why can't the warm air spontaneously give the energy back and restart the pendulum?"
  - id: phy-app-m2-12-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A motor receives 500 J of electrical energy and delivers 350 J of useful lifting work. Its efficiency is ________ %.
    inputConfig:
      placeholder: "70"
    markingRule:
      matchMode: CONTAINS
      accepted: ["70"]
      rejectedFeedback: "Efficiency = useful ÷ total = 350/500 = 0.70 = 70%. The other 150 J became heat and sound in the windings and bearings — transferred, not destroyed."
    hint: "Useful output divided by total input, as a percentage."
    reflectionPrompt: "Where exactly are the missing 150 J right now?"
  - id: phy-app-m2-12-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Trace the energy chain for a cyclist eating breakfast and then riding up a hill, naming at least four stores or transfers in order. Where does the energy end up when she brakes back down to a stop at the bottom? (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [chemical, kinetic, potential, thermal, heat, brake, food, muscle]
      rejectedFeedback: "Chain: chemical (food) → kinetic (muscles/bike, with much lost as body heat) → gravitational potential (height gained) → kinetic (descent) → thermal (brake pads, tyres, air) at the bottom. Every joule accounted for, most ending as slightly warmer surroundings."
    hint: "Food is a chemical store. Hills are a potential store. Brakes are heat factories."
    reflectionPrompt: "At what single point in the chain was the most energy wasted, and to which store?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The law of conservation of energy states:"
    options:
      - "Energy should be used sparingly"
      - "Energy cannot be created or destroyed, only transferred between stores"
      - "Machines always lose energy permanently"
      - "Energy always ends up as motion"
    correctIndex: 1
    feedback: "The total never changes — it only moves between stores (kinetic, potential, chemical, thermal, elastic...). 'Conservation' here is bookkeeping law, not environmental advice."
  - type: MULTIPLE_CHOICE
    question: "A 60%-efficient kettle element transfers 100,000 J. The energy that does NOT heat the water:"
    options:
      - "Vanishes"
      - "Stays in the element forever"
      - "Heats the kettle body, the air, and the worktop — dissipated but not destroyed"
      - "Returns to the power station"
    correctIndex: 2
    feedback: "40,000 J leak into surrounding stores as heat. Dissipated energy is spread-out and hard to reuse — but every joule still exists."
---

# Hook

Here is the most successful accounting rule in the history of thought: **in every process ever measured — every collision, explosion, heartbeat, and star — the total energy at the end equals the total at the start.** No exceptions. Not one, ever, anywhere.

When the books *seemed* not to balance, the law didn't fall — it made discoveries. Missing energy in radioactive decay? The law insisted something invisible was carrying it away; twenty-six years later, the neutrino was found, exactly as billed. Energy conservation is so reliable that physicists treat an apparent violation not as a crisis but as a treasure map.

Your friction-slowed sled, your cooling tea, your phone going flat — none of them *lose* energy. They relocate it, almost always into the great diffuse account called heat. Today you learn to balance the books.

# Lore Introduction

Once a year, Thorne tells you, the Academy's Bursar of Energies conducts the Great Audit — a ritual nine centuries old. Tonight you attend. In the audit hall, a model of the whole Observatory runs in miniature: the clock-weights, the well, the kitchens, the forge. The Bursar — an unsmiling woman with a ledger the size of a door — prices every transfer: the winding-apprentice's breakfast, the clock's ticking, the forge's glow, the warmth of the walls. At dawn she rules the final line and announces, as Bursars have announced for nine hundred years: *"Nothing created. Nothing destroyed. All accounted."* Thorne leans close. "Once a century, the line refuses to balance. Those years are remembered, apprentice — because each time, the missing energy was eventually found hiding in a store no one had thought to audit. The ledger is never wrong. Only incomplete."

# Core Learning

## Concept Introduction

**The Law of Conservation of Energy.** Energy can be transferred between stores and transformed between forms, but never created or destroyed. The universe's total is fixed; every process is a reallocation.

The main **stores** in your portfolio so far:

| Store | Held by | Example |
|-------|---------|---------|
| Kinetic | Moving things | A rolling ball |
| Gravitational potential | Raised things | The clock-weights |
| Elastic potential | Stretched/squashed things | A drawn bow |
| Chemical | Fuels, food, batteries | Breakfast |
| Thermal | Everything, as warmth | Brake pads after a stop |

**Energy chains.** Real processes are relay races between stores: food (chemical) → muscles (kinetic + much heat) → hill climbed (potential) → descent (kinetic) → brakes (thermal). Writing the chain — and checking the joules at each handover — is the physicist's audit.

**Dissipation, not destruction.** Friction, drag, and resistance transfer energy into *thermal* stores, spread thinly through surroundings. These joules still exist but are effectively unrecoverable — too dilute to gather back. That's the honest meaning of "wasted".

**Efficiency** prices the leak:

```
efficiency = useful energy out ÷ total energy in   (× 100 for %)
```

Petrol engines ~25–30%, electric motors ~90%, human muscles ~25%, old filament bulbs ~5% (a heater that incidentally glowed). No machine reaches 100% in practice, and none can exceed it — a claim of 110% efficiency is a claim to mint energy, and the Bursar's ledger has nine centuries of precedent against it.

## Why It Matters

- Conservation converts hard force-problems into easy bookkeeping (you saw the diver's speed fall out of mgh = ½mv² with no kinematics at all).
- Efficiency is economics: fuel bills, electricity costs, and climate policy are all questions of which chains leak how much into heat.
- The law is your fraud detector: every perpetual-motion machine, free-energy gadget, and miracle fuel pitch ever sold violates this lesson — and every single one has failed.

## Worked Examples

**Example 1: The rollercoaster audit**
A 500 kg car tops the first hill, 40 m up, nearly at rest: PE = 500×10×40 = 200,000 J. At the bottom, measurement finds KE = 180,000 J. Violation? No — audit the unlisted store: 20,000 J went to thermal (rails, wheels, air) via friction and drag. Designers bank on this: every later hill *must* be lower than the first, by at least the leakage.

**Example 2: Efficiency chaining**
A power station is 40% efficient; transmission lines pass on 90% of what they receive; an electric heater at home is ~100%. End-to-end: 0.4 × 0.9 × 1.0 = 36% of the fuel's energy heats your room. Chains multiply efficiencies — which is why moving any link upward (better turbines, fatter cables) matters at national scale.

**Example 3: Where your phone's charge goes**
A phone battery holds ~40,000 J. After a day: screen light (→ absorbed by your room as heat), radio waves (→ absorbed somewhere as heat), processor work (→ heat directly — feel the back of the phone), speaker sound (→ absorbed... as heat). Almost every joule of every gadget ends, eventually, as gentle warmth. The universe's energy story is a slow migration into the thermal account.

## Common Mistakes

- **"Energy is used up"** — it is *transferred*; "used up" always means "moved to a store I stopped tracking", usually thermal.
- **Auditing only the glamorous stores** — KE and PE balance only after friction's thermal take is counted; the missing-joules panic is almost always an unlisted heat entry.
- **Efficiency over 100%** — impossible; if a calculation yields it, an input was missed or an output double-counted.
- **Confusing conservation (physics) with conserving (policy)** — the law says totals are fixed; the policy says keep energy in *useful* stores longer. Related, not identical.
- **Thinking dissipated means destroyed** — diluted and disordered, yes; gone, never.

## Mental Model

Picture the universe's energy as **water in a sealed terrarium**. It pools in lakes (stores), flows in streams (transfers), gets pumped uphill (work), and races down (release) — but the glass is sealed: not a drop enters or leaves, ever. Friction and resistance are evaporation: the water isn't lost, it's *misted* into the air of the terrarium — present, measurable, and almost impossible to gather back into a usable lake. Efficiency asks, of any pump or chute: how much arrived as lake, how much as mist?

## Mini Summary

- ✔ Total energy never changes; processes only move it between stores
- ✔ Write the chain: chemical → kinetic → potential → ... → (almost always) thermal
- ✔ Friction and drag dissipate energy to heat — relocated, never destroyed
- ✔ Efficiency = useful/total; chains multiply; 100%+ is fiction
- ✔ Books that won't balance mean a store you forgot — find it

# Guided Practice Quest

Work through the guided steps to acquit a dying pendulum of destroying anything, price a motor's leak, and ride a full energy chain from breakfast to brake-heat.

# Solo Practice Quest

Conduct your own Great Audit on one everyday process, end to end — suggestions: making tea, a phone charging then discharging, a bounce of a ball from 1 m (measure the return height!). Produce: (1) the full chain of stores and transfers, in order; (2) numbers wherever you can estimate them (the ball's mgh before and after the bounce is very doable); (3) the efficiency of the step you could measure; (4) a closing line in the Bursar's format — "X joules in, Y useful, Z dissipated to [stores], all accounted." Conclude with one sentence on which single improvement would shrink your process's thermal leak.

# Integration

**Engineering**: Conservation plus efficiency is the master constraint of all engineering: engine design, building insulation, regenerative braking, and data-centre cooling are each a campaign against one particular thermal leak. The Sankey diagram — energy flows drawn as rivers of proportional width — is the Bursar's ledger as engineers draw it; you'll meet it at Junior tier.

**Biology**: You are a 100-watt chemical engine running on ~10 MJ of food per day, at roughly 25% mechanical efficiency — the other three-quarters keeps you at 37 °C. Every food-chain in ecology is an energy chain with brutal efficiencies (~10% per link), which is why pyramids of biomass narrow so fast and why large predators are rare.

# Lore Conclusion

The Bursar rules her line at dawn — *all accounted* — and the audit hall exhales. Thorne walks you out into the cold morning. "Module Two is yours," he says. "Forces and their pairs, the three laws, work, the two great accounts, and the iron rule that no joule is ever minted or melted." He stops at the foot of the Observatory's bell tower, where the dawn bell is about to ring, and looks up. "Forces you cannot see. Energy you can only count. But the bell —" the first stroke booms out across the courtyard, and he lets it wash over you before finishing, "— the bell you can *hear* arriving, wave upon wave. Module Three, apprentice: the physics of everything that ripples, rings, and shines."
