---
id: phy-jun-m2-03
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m2
moduleTitle: "Module 2: Electricity and Magnetism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: electric_charge
topicTitle: "Electric Charge"
topicSortOrder: 1
title: "Current: Charge in Motion"
sortOrder: 3
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Define current as rate of charge flow (I = Q/t) in amperes
  - Distinguish electron drift from the near-instant propagation of the electrical push
  - Define voltage as energy per coulomb and relate both to a simple circuit
integrationDomains: [mathematics, biology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Uses I = Q/t with amperes as coulombs per second
    - Explains the slow electron drift versus instant signal apparent paradox
    - Defines voltage as joules per coulomb (energy given or spent per unit charge)
  keywords: [current, ampere, I = Q/t, drift, voltage, joules per coulomb, circuit]
  modelAnswer: |
    Current is the rate of charge flow: I = Q/t, one ampere being one coulomb per second. In a
    wire the carriers are free electrons, drifting astonishingly slowly — fractions of a
    millimetre per second — yet a lamp lights instantly because the electric push propagates
    near light-speed through the already-full wire, like water emerging the moment you open a
    full hose's tap. Voltage measures energy per charge: a 1.5 V cell gives each coulomb 1.5
    joules to spend; a 230 V supply gives 230. Current is the flow rate, voltage the energy
    rate per unit of flow — together they will price every circuit in the module.
guidedSteps:
  - id: phy-jun-m2-03-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A phone charger delivers 2 A for 60 seconds. Charge transferred: Q = I × t = ________ C.
    inputConfig:
      placeholder: "120"
    markingRule:
      matchMode: CONTAINS
      accepted: ["120"]
      rejectedFeedback: "Q = 2 × 60 = 120 coulombs — about 7.5 × 10²⁰ electrons. Rearranging I = Q/t, as the balance rule always allows."
    hint: "Charge = current × time."
    reflectionPrompt: "How many coulombs pass through a 10 A kettle element during a 3-minute boil?"
  - id: phy-jun-m2-03-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Electrons drift through a lamp's wires at well under a millimetre per second — yet the lamp lights the instant you flick the switch. Resolve the paradox:
    inputConfig:
      options:
        - "Some electrons are much faster than others"
        - "The wire is already FULL of free electrons; the switch applies a push that propagates near light-speed, setting the whole column moving at once"
        - "Light travels back from the switch"
        - "The first electrons sprint, then slow down"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The wire is already FULL of free electrons; the switch applies a push that propagates near light-speed, setting the whole column moving at once"]
      rejectedFeedback: "Like a full hosepipe: open the tap and water exits the far end immediately — not because tap-water raced the length, but because the pipe's standing water all moved together. The wire's electron sea needs only the push, and the push travels at nearly c."
    hint: "Is the wire empty before you flick the switch?"
    reflectionPrompt: "Whose electrons actually light your lamp tonight — the power station's, or the wire's own?"
  - id: phy-jun-m2-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A 9 V battery drives a circuit. Explain what '9 volts' means in energy-per-charge terms, and how much energy the battery gives 5 coulombs of charge. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["45", joules per coulomb, energy, per coulomb, each coulomb]
      rejectedFeedback: "Voltage = energy per unit charge: 9 V means each coulomb is loaded with 9 joules to spend around the circuit. Five coulombs carry 5 × 9 = 45 J. (E = QV — the module's energy backbone.)"
    hint: "A volt is a joule per coulomb."
    reflectionPrompt: "Where do those 45 joules end up after the charge completes its lap?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "One ampere equals:"
    options:
      - "One joule per second"
      - "One coulomb of charge passing per second"
      - "One volt per metre"
      - "One electron per second"
    correctIndex: 1
    feedback: "I = Q/t: amps are coulombs per second — a flow RATE, the electrical river's litres-per-minute."
  - type: MULTIPLE_CHOICE
    question: "Current in a simple series loop is the same everywhere because:"
    options:
      - "Charge is used up evenly"
      - "Charge is conserved and has nowhere else to go — what flows in must flow out of every component"
      - "The battery pushes equally on both ends"
      - "Electrons travel the loop instantly"
    correctIndex: 1
    feedback: "Charge is neither created nor consumed by components (it's the ENERGY that's spent). One loop, one flow: the ammeter reads the same on either side of the lamp."
---

# Hook

When you flick a light switch, the electrons in the wire by your finger begin to drift toward the lamp at roughly *eight centimetres per hour* — slower than a snail, slower than the minute hand of a large clock. At that pace, the power station's electrons would take years to reach your ceiling. And yet the light is on before your finger leaves the switch.

The resolution of this paradox is the single most useful picture in electricity: **the wire was never empty**. Every copper wire in your house is brim-full of free electrons, end to end, right now — a river standing ready. The switch doesn't *send* anything; it opens a tap, and the push races down the standing river at near light-speed, setting the whole column creeping at once. Slow water, instant flow. Today we learn to measure that river — its flow rate (current), and the energy each unit of it carries (voltage) — the two numbers on every battery, bulb, and warning sign you'll ever read.

# Lore Introduction

Behind Hale's humming door lies the Tower's River Room: a workbench landscape of brass channels, hand-pumps, and waterwheels — an entire circuit, built in water. "My predecessors built this to stop juniors electrocuting themselves through bad imagination," Hale says, priming the pump. Water fills the closed loop of channels; a small wheel sits idle mid-stream. She cranks the pump — and the wheel spins *instantly*, though dye dropped at the pump crawls along the channel at a finger's pace. "Watch the dye, junior. THAT is your electron: a creeping thing. Watch the wheel: THAT is your lamp — alight the moment I push, because the channel was full and the whole river moved as one." She hands you two brass gauges from the rack: one marked *flow*, one marked *push*. "Two numbers run this room, and every circuit in the world. Learn to read them apart, and you may follow me upstairs to the real river."

# Core Learning

## Concept Introduction

**Current — the flow rate.**

```
I = Q / t        amperes = coulombs per second
```

Current measures how much charge passes a point per second. Anchors: an LED sips ~0.02 A; a phone charger ~2 A; a kettle ~10 A; a lightning stroke ~30,000 A (briefly). In metals the carriers are the conductor's own free electrons — the sea from last lesson, now flowing. (Convention's quirk: circuit diagrams draw "conventional current" + → −, a choice made before the electron's discovery; the electrons actually drift the other way. The mathematics forgives; just be consistent.)

**Drift versus push — the full-pipe picture.** Electron *drift* is glacial (sub-mm/s: enormous numbers of carriers means each need barely move to make amps). The *push* — the electric field established by the source — propagates along the wire at near light-speed, mobilising the entire standing column at once. Your lamp lights on the wire's own resident electrons; the power station merely leans on the column.

**Voltage — the energy per unit flow.**

```
V = E / Q        volts = joules per coulomb
```

A source's voltage states the energy loaded onto each coulomb (a 1.5 V cell: 1.5 J per coulomb; mains: 230 J). Across a component, voltage states the energy each coulomb *spends* there (into light, heat, motion). Hence the pairing that will run the rest of this module: **current = how much charge flows; voltage = how much energy each unit of charge carries or drops**. Multiply them and you get power — but that pleasure is two lessons away.

**Conservation, twice.** Charge is conserved: in one loop, current is the same everywhere (components spend the charge's *energy*, never the charge). Energy is conserved: the source's joules-per-coulomb are fully spent around each lap. Two old friends from the Apprentice tier, now running electricity's books.

## Why It Matters

- Amps and volts are the two numbers on every device, fuse, charger, and danger sign; reading them correctly is both literacy and safety (it's the *current through you* that injures; voltage is what drives it).
- The full-pipe picture inoculates against electricity's worst misconceptions ("electrons get used up", "power stations ship electrons to my house") — and AC's revelation (the column merely *sloshes* 50 times a second; nothing ever arrives at all) lands gently if the picture is right.
- I = Q/t and V = E/Q are the axioms; Ohm's law, circuit rules, and power calculations (the next three lessons) are all built from them.

## Worked Examples

**Example 1: Counting an LED's electrons**
An indicator LED runs at 20 mA. Charge per minute: Q = 0.02 × 60 = 1.2 C — about 7.5 × 10¹⁸ electrons. Per second, ~10¹⁷: a hundred million billion electrons per second through a light you'd describe as "barely on". The coulomb is a big unit because the electron is an absurdly small one.

**Example 2: The battery's ledger**
A 1.5 V AA cell rated ~3,000 mAh (milliamp-hours): total charge = 3 A·h... careful — 3,000 mAh = 3 A for one hour = 3 × 3,600 = 10,800 C. Energy banked: E = QV = 10,800 × 1.5 ≈ **16,000 J** — enough to lift you about 25 m, stored in a finger-sized can. Battery shopping is literally Q × V arithmetic.

**Example 3: Why birds sit safely on power lines**
A pigeon grips a single 25,000 V cable: both feet sit at essentially the *same* voltage, so no energy-per-coulomb difference spans the bird — no push through the body, no current, no drama. Danger requires a *difference*: the same bird bridging cable-to-pylon (25,000 V of difference) completes a path, and the river takes it. Voltage is always *between* two points — the lesson's quiet, life-saving grammar.

## Common Mistakes

- **"Electrons race from the power station"** — they drift centimetres per hour; the wire's own full column does the work, pushed at near-c. (On AC they only slosh in place.)
- **"Current gets used up around the circuit"** — charge is conserved; one loop, one current everywhere. It's the ENERGY (voltage drops) that gets spent.
- **Confusing amps and volts** — flow rate versus energy-per-unit-flow; a static spark is huge volts at trifling charge, a car battery modest volts behind river-sized current capability. Danger lives in their product and the path.
- **Saying "voltage flows through"** — voltage doesn't flow; it is the push *across* two points. Current flows *through*; voltage sits *across*. (Examiners and electricians both check this grammar.)
- **Treating conventional current as an error** — it's a convention, self-consistent and universal in diagrams; note the electron reality once, then follow the convention.

## Mental Model

A circuit is **a closed loop of canal, already brim-full, with a tireless paddle-pump at one station**. The pump (battery) doesn't add water (charge) — it adds *push*, lifting each passing litre and loading it with energy. Current is the canal's flow rate, identical at every bridge on a single loop, because water neither vanishes nor accumulates. Voltage is the *height* the pump lifts each litre — and every wheel, sluice, and mill (component) along the canal is a place where the water steps back down, spending its height as work. The dye-drop drifts; the flow is everywhere at once; and nothing whatever is consumed except the height. Read every circuit, forever, as this canal — and you will never again ask where the electrons go at night.

## Mini Summary

- ✔ I = Q/t: current is charge-flow rate, in amperes (coulombs per second)
- ✔ Wires are pre-filled; the push travels near light-speed while electrons drift like glaciers
- ✔ V = E/Q: voltage is energy per coulomb — loaded by sources, spent across components
- ✔ Current flows *through*; voltage sits *across*; charge is conserved (one loop, one current)
- ✔ Danger needs a voltage *difference* and a path — ask the pigeon

# Guided Practice Quest

Work through the guided steps to bank 120 coulombs from a charger, open the full hosepipe on the snail paradox, and load nine joules onto every coulomb a battery serves.

# Solo Practice Quest

Three surveys of the invisible river: (1) *Read your chargers*: collect three charging bricks/devices and record their output ratings (V and A); for each compute the charge and energy delivered in one hour (Q = It, E = QV), and rank them. (2) *The battery audit*: find any battery's capacity (mAh) and voltage; compute its stored coulombs and joules, then express the energy in kettle-seconds (kettle ≈ 2,000 W) and phone-charges. (3) *Grammar drill*: write five sentences about a torch circuit using "through" and "across" with perfect physics grammar (current through the bulb, voltage across it...) — then rewrite two common misstatements you've heard ("the socket has current in it"; "240 volts went through him") into correct form, preserving the meaning. Close with the pigeon: two sentences to a worried child on why the birds are fine and the kite-flyer near pylons is not.

# Integration

**Mathematics**: I = Q/t is another rate (a derivative in waiting), and V = E/Q another per-unit quantity — physics keeps building meaning from division. The ampere-hour ↔ coulomb conversion is unit fluency from Module One earning money (literally: battery prices per joule vary fourfold across brands and chemistries).

**Biology**: Your body runs on current too — but with *ions* as carriers (sodium, potassium) and millivolts as pushes: nerve impulses are travelling voltage waves at ~100 m/s (the full-pipe principle, wet edition). Electric shock disrupts exactly this signalling, which is why current thresholds — 1 mA felt, ~30 mA breathing risk, ~100 mA cardiac — are so perilously low: the body mistakes the outside river for its own.

# Lore Conclusion

You leave the River Room able to read both gauges at a glance — flow and push, amps and volts, never again interchangeable — and able to explain, with the dye-drop's testimony, why the wheel never waits for the water. Hale locks the pump's crank with evident affection. "The river runs. But notice, junior, what the room would NOT let you do." She gestures at the brass channels: wide here, narrowed there, one section packed with gravel through which the water labours. "Some channels fight the flow. Every real wire, lamp, and element does — and the fight is where all the work gets done." She starts up the spiral stairs toward the humming. "Tomorrow: resistance — the friction of the electric river — and the one law, three letters long, that runs every circuit ever built. Ohm, junior. After him, the Tower is mostly arithmetic."
