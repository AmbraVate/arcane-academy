---
id: phy-app-m4-02
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m4
moduleTitle: "Module 4: Matter and Heat"
moduleGlyph: "🔥"
moduleSortOrder: 4
topicSlug: states_of_matter
topicTitle: "States of Matter"
topicSortOrder: 1
title: "Changes of State"
sortOrder: 2
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Name the six changes of state and the direction of energy flow in each
  - Explain why temperature stays constant during melting and boiling
  - Distinguish boiling from evaporation
integrationDomains: [chemistry, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Names melting, freezing, boiling/evaporating, condensing, subliming, depositing with energy directions
    - Explains the temperature plateau — energy is spent breaking bonds, not speeding particles
    - Distinguishes evaporation (surface, any temperature) from boiling (throughout, at boiling point)
    - Explains cooling by evaporation
  keywords: [melting, boiling, condensing, plateau, latent, bonds, evaporation, surface]
  modelAnswer: |
    Heating drives melting (solid→liquid), boiling and evaporation (liquid→gas), and
    sublimation (solid→gas directly); cooling reverses them as freezing, condensing, and
    deposition. During melting and boiling the temperature holds a plateau: the supplied energy
    is spent breaking particle bonds — buying freedom, not speed — and is called latent heat.
    Boiling happens throughout the liquid at one fixed temperature; evaporation happens only at
    the surface at any temperature, as the fastest particles escape. Their departure lowers
    the average energy of those left behind, which is why evaporation cools — the physics of
    sweat.
guidedSteps:
  - id: phy-app-m4-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A thermometer in a pan of melting ice reads 0 °C — and keeps reading 0 °C for ten full minutes while the hob blazes beneath it. The energy being supplied is:
    inputConfig:
      options:
        - "Escaping as light"
        - "Breaking the bonds of the ice lattice — buying particle freedom rather than particle speed"
        - "Being destroyed"
        - "Stored in the thermometer"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Breaking the bonds of the ice lattice — buying particle freedom rather than particle speed"]
      rejectedFeedback: "The plateau is the signature of a state change: every joule goes into dismantling the lattice (latent heat). Temperature — which tracks particle SPEED — cannot rise until the demolition is complete."
    hint: "Temperature measures how fast particles jiggle. What else could energy be spent on?"
    reflectionPrompt: "Why does the temperature start rising again the moment the last ice disappears?"
  - id: phy-app-m4-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Wet skin in a breeze feels cold because the fastest water particles escape as vapour, lowering the average energy of those left behind. This process is called ________.
    inputConfig:
      placeholder: "evaporation"
    markingRule:
      matchMode: CONTAINS
      accepted: [evaporation, evaporative]
      rejectedFeedback: "Evaporation — surface escape of the fastest particles at any temperature. Losing your fastest members lowers the average: the liquid (and your skin beneath it) cools."
    hint: "Surface escape, no boiling required."
    reflectionPrompt: "Why does a breeze make the cooling stronger?"
  - id: phy-app-m4-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Boiling and evaporation both turn liquid into gas. State two clear differences between them. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [surface, throughout, any temperature, boiling point, bubbles, fixed]
      rejectedFeedback: "Evaporation: surface only, at ANY temperature, no bubbles. Boiling: throughout the liquid (bubbles of vapour form inside it), only AT the boiling point. A puddle dries without ever boiling."
    hint: "Where in the liquid does each happen, and at what temperatures?"
    reflectionPrompt: "Which of the two dries laundry on a cold day — and what does that tell you?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which change of state RELEASES energy to the surroundings?"
    options: ["Melting", "Boiling", "Condensing", "Sublimation"]
    correctIndex: 2
    feedback: "Condensing (gas→liquid) re-forms bonds, releasing the latent heat that boiling once consumed — why steam scalds far worse than boiling water, and why clouds warm the air as they form."
  - type: MULTIPLE_CHOICE
    question: "Dry ice (solid CO₂) shrinks away into gas with no puddle. This is:"
    options: ["Melting", "Evaporation", "Sublimation", "Condensation"]
    correctIndex: 2
    feedback: "Sublimation: solid→gas directly, skipping the liquid state. Frost vanishing on a cold sunny morning does the same."
---

# Hook

Here is a small domestic miracle you've watched a hundred times without seeing it: put a pan of ice on a roaring hob with a thermometer in it, and for *minutes on end the temperature refuses to move*. Zero degrees. Still zero. The hob is pouring thousands of joules in — Module Two's bookkeeping says that energy cannot vanish — and yet the mercury stands like a sentry. Where is it all going?

The answer unlocks every change of state: that energy is being spent on *demolition*, not acceleration. It is tearing particles free of the lattice, bond by bond, and not one joule is left over for speed until the job is done. Physics calls the hidden payment *latent* — and the same hidden ledger explains why steam scalds so viciously, why sweat cools you, and why a puddle can dry on a freezing day.

# Lore Introduction

Calde's Foundry keeps its accounts not in coin but in charcoal, and she shows you the strangest page in the ledger. "Melting one ingot-weight of iron," she reads. "Cost to raise it from cold to glowing-soft: forty measures of charcoal. Cost of the *final step* — soft solid to flowing liquid, the temperature not rising one whisker —" she taps the entry, "— eleven more measures. Eleven! For nothing the thermometer can see!" The old founders, she tells you, called it the *toll* — paid at every border between states, refunded in full when you cross back. "New apprentices think the toll-keeper is cheating them. The thermometer says nothing happened. But watch the crucible, not the thermometer." She nods to the melt: solid iron sinking into its own shining liquid at one unmoving temperature. "Something is *very much* happening. Today you learn what the toll buys."

# Core Learning

## Concept Introduction

**Six border crossings**, three paid (energy in), three refunded (energy out):

| Energy IN (heating) | Energy OUT (cooling) |
|---|---|
| Melting: solid → liquid | Freezing: liquid → solid |
| Boiling/evaporating: liquid → gas | Condensing: gas → liquid |
| Subliming: solid → gas (dry ice, frost in sun) | Depositing: gas → solid (frost forming) |

**The plateau and the toll.** Temperature measures the particles' average *speed* of jiggling. During a state change, the supplied energy is spent instead on *breaking bonds* — buying freedom, not speed — so the thermometer flatlines until the change completes. This bond-payment is **latent heat** ("latent" = hidden). Crossing back refunds it: condensing steam dumps its latent heat into whatever it touches — the reason steam burns are so severe, and the reason a forming cloud warms the surrounding air.

A heating curve for water tells the whole story: rise (ice warming) → **plateau at 0 °C** (melting toll) → rise (water warming) → **plateau at 100 °C** (boiling toll, a much larger one) → rise (steam warming).

**Boiling versus evaporation** — both liquid→gas, profoundly different:

- **Boiling**: at one fixed temperature (the boiling point), *throughout* the liquid — vapour bubbles form inside it and rise.
- **Evaporation**: at *any* temperature, from the **surface only** — the fastest particles, by luck of collisions, gain enough energy to tear free and leave. No bubbles; puddles dry at 10 °C.

**Evaporation cools.** The escapees are precisely the fastest particles; removing them lowers the *average* speed of those remaining — the liquid cools. Sweating is engineered evaporation: your body donates latent heat to departing water. Wind helps by sweeping escapees away before they can return.

## Why It Matters

- Latent heat moves civilisation's energy: steam turbines, refrigeration, and air conditioning all run on charging and refunding the toll.
- Weather is latent heat at planetary scale: evaporation banks solar energy in ocean vapour; condensation refunds it as the engine of storms and hurricanes.
- Body temperature regulation (sweating, panting, the danger of humid heat) is this lesson applied to staying alive.

## Worked Examples

**Example 1: Why steam scalds worse than water**
100 °C water touching skin delivers heat as it cools — bad. 100 °C *steam* first **condenses**, refunding its enormous boiling toll (~2,300 J per gram — over five times the energy of cooling that gram of water all the way to body temperature), and *then* the hot condensate cools too. Same thermometer reading, several times the energy delivery. Kitchens and engine rooms respect steam for accounting reasons.

**Example 2: The refrigerator's circular toll-road**
A fridge pumps a special fluid around a loop: inside the cold cabinet it **evaporates**, paying its toll by *taking* latent heat from your food; pumped outside, it is compressed and **condenses**, refunding that heat into the kitchen through the warm rear grille. The fluid crosses the same border twice per lap, hauling energy outward each time. Air conditioners and heat pumps are the same toll-road with different signage.

**Example 3: The drying line on a cold day**
Laundry dries at 5 °C — no boiling within a hundred degrees. Surface evaporation alone: the fastest water molecules flee one by one, helped by wind (sweeping them clear) and low humidity (fewer returners). It's slower than a summer day — fewer fast particles in a cold liquid's lottery — but the lottery never closes. Boiling is a border crossing; evaporation is a constant trickle of escapees over the fence.

## Common Mistakes

- **"The plateau means energy stopped flowing"** — energy pours in throughout; it's spent on bonds, invisible to thermometers. (Conservation of energy holds; track the store.)
- **"Boiling and evaporation are synonyms"** — surface-vs-throughout and any-temperature-vs-fixed-point distinguish them; puddles dry unboiled.
- **"Freezing means cold is added"** — there is no "cold" substance: freezing *releases* energy (the refund), which is why orchard farmers spray water before a frost — freezing water donates heat to the fruit.
- **"Steam is visible white mist"** — true steam (water gas) is invisible; the white plume is already-condensed droplets. The invisible gap above a kettle spout is the dangerous part.
- **Forgetting sublimation** — solid→gas needs no liquid stopover: dry ice, frost vanishing, freeze-dried food.

## Mental Model

Each state border is a **toll bridge with a strict gatekeeper**. Heat is your money. On the plains before the bridge, money buys *speed* — particles jiggle faster, thermometer climbing. At the bridge, the gatekeeper halts the column: every particle must pay the full bond-breaking toll to cross, and *while the queue pays, nobody speeds up* — the thermometer waits at the bridge's posted temperature. Across the bridge, money buys speed again. And the bridges are honest both ways: re-cross toward order (condense, freeze) and the gatekeeper refunds every coin — into whatever stands nearby. Steam's refund booth is why it scalds; sweat's toll booth is why it cools.

## Mini Summary

- ✔ Six crossings: melt/freeze, boil/condense, sublime/deposit — energy in / energy out
- ✔ Plateaus: latent heat pays for bond-breaking; temperature (speed) waits
- ✔ Condensing and freezing refund the toll — steam burns, frost-spray protects orchards
- ✔ Boiling: throughout, fixed temperature; evaporation: surface, any temperature
- ✔ Evaporation cools by exporting the fastest particles — sweat is engineering

# Guided Practice Quest

Work through the guided steps to follow the missing joules through a 0 °C plateau, chill wet skin in the breeze, and put boiling and evaporation permanently in separate drawers.

# Solo Practice Quest

Plot a real heating curve: fill a pan or heatproof jug with ice (crushed if possible), insert a cooking thermometer, apply steady gentle heat, and record temperature every 30 seconds until a few minutes after boiling begins. Graph temperature against time (Module One graph rules: axes, units, best curve). Identify and label: both plateaus, the segments of rising temperature, and — using your heat source's steadiness as an assumption — compare the *lengths* of the two plateaus. The boiling plateau won't end (you can't easily overheat steam at home); explain why, and explain from your graph why the melting toll is visibly smaller than the boiling toll. Safety: steam respect throughout.

# Integration

**Chemistry**: Melting and boiling points are fingerprints — pure substances cross borders at sharp temperatures, mixtures smear (which is how distilleries separate alcohol and refineries split crude oil: boiling tolls differ per molecule). Impurities shift the bridges too: salted roads melt ice, salted pasta-water boils a touch hotter.

**Biology**: Sweating is humanity's superpower — few animals evaporative-cool as effectively, and humid air (blocking evaporation) is why "40 °C dry" is bearable and "35 °C humid" is dangerous. Plants run the same physics as transpiration; dogs run it through their tongues.

# Lore Conclusion

You show Calde your heating curve, both plateaus labelled in charcoal — *the tolls, paid in full while the thermometer slept*. She inks it into the Foundry ledger beside the iron entries, eleven measures and all. "The toll-keeper never cheats," she says. "He only keeps accounts the thermometer cannot read — and now, neither can he hide them from you." She racks the cooled crucible and weighs the morning's iron in her scarred hands, thoughtfully. "Which raises the Foundry's next riddle. Two ingots, same size to the eye — one floats out of the casting bath's slag, one sinks like a sin. Same volume, apprentice. *Different stuff.*" She tosses you a cork and an iron nail, one to each hand. "Tomorrow: density — the crowdedness of matter — and why the sea itself sorts all things into floaters and sinkers."
