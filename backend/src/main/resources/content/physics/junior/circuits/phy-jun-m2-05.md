---
id: phy-jun-m2-05
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m2
moduleTitle: "Module 2: Electricity and Magnetism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: circuits
topicTitle: "Circuits"
topicSortOrder: 2
title: "Series and Parallel Circuits"
sortOrder: 5
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - State the current and voltage rules for series and parallel circuits
  - Compute equivalent resistance for simple series and parallel combinations
  - Explain why homes are wired in parallel
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Applies series rules (same current; voltages share; resistances add)
    - Applies parallel rules (same voltage; currents share; combined R drops)
    - Explains domestic parallel wiring via independence and full-voltage operation
  keywords: [series, parallel, same current, same voltage, share, add, independence]
  modelAnswer: |
    In series — one loop — the current is identical everywhere, the supply's voltage is shared
    among components, and resistances simply add, so each added lamp dims them all and one
    failure darkens everything. In parallel — side-by-side branches — every branch enjoys the
    full supply voltage, currents divide among branches (totalling at the junctions), and the
    combined resistance is LESS than the smallest branch, since each new branch opens another
    road. Homes wire in parallel for exactly these reasons: every appliance gets its full 230 V,
    runs at design power, switches independently, and survives its neighbours' failures.
guidedSteps:
  - id: phy-jun-m2-05-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      Three resistors — 2 Ω, 3 Ω, and 7 Ω — are wired in SERIES. Their combined resistance is ________ Ω.
    inputConfig:
      placeholder: "12"
    markingRule:
      matchMode: CONTAINS
      accepted: ["12"]
      rejectedFeedback: "Series resistances add: 2 + 3 + 7 = 12 Ω. One road, all obstacles in line — every electron runs the full gauntlet."
    hint: "One after another: just add."
    reflectionPrompt: "What current flows if this chain is put across 24 V — and is it the same in all three resistors?"
  - id: phy-jun-m2-05-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Two identical 6 Ω resistors are wired in PARALLEL. The combination's resistance is:
    inputConfig:
      options:
        - "12 Ω"
        - "6 Ω"
        - "3 Ω — two equal roads side by side carry twice the flow, halving the effective resistance"
        - "36 Ω"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["3 Ω — two equal roads side by side carry twice the flow, halving the effective resistance"]
      rejectedFeedback: "Parallel roads multiply paths: two equal 6 Ω branches pass twice one branch's current, so the pair behaves as 3 Ω. Parallel combinations are always LESS than the smallest branch — adding roads never impedes traffic."
    hint: "Does adding a second open road increase or decrease the town's total congestion?"
    reflectionPrompt: "Three such resistors in parallel — what now? Spot the pattern for n equal branches."
  - id: phy-jun-m2-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Old fairy lights were wired in series; one blown bulb killed the whole string. Modern homes wire everything in parallel. Give THREE distinct advantages of parallel wiring for a house. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [full voltage, independent, switch, one fails, others, design power, 230]
      rejectedFeedback: "Parallel gives: (1) every appliance the FULL supply voltage, so each runs at its design power; (2) independence — each device switches on/off without affecting others; (3) fault tolerance — one failure leaves every other branch lit. Series would share the voltage, chain the switches, and die together."
    hint: "Think voltage received, switching, and what happens when the toaster dies."
    reflectionPrompt: "Why DID old fairy-light makers choose series anyway? (Count the wire.)"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In a series loop, which quantity is the same through every component?"
    options: ["Voltage", "Current", "Resistance", "Power"]
    correctIndex: 1
    feedback: "One loop, one flow: charge conservation leaves current nowhere else to go. The supply VOLTAGE, meanwhile, is shared out across the components in proportion to their resistances."
  - type: MULTIPLE_CHOICE
    question: "In a parallel arrangement, which is the same across every branch?"
    options: ["Current", "Voltage", "Charge", "Brightness"]
    correctIndex: 1
    feedback: "Each branch spans the same two supply points, so each feels the full supply voltage; the CURRENT divides among branches (and recombines at the junctions, totals intact)."
---

# Hook

A riddle from the age of your grandparents' Christmas tree: one bulb dies, and *forty* go dark — then someone spends the evening swapping bulbs one by one to find the corpse. Meanwhile, in the same house, the kitchen light blows and... nothing else even flickers. The kettle boils on; the radio plays.

Two rooms, two philosophies of wiring — and between them, the entire architecture of every circuit on Earth. **Series**: one single loop, everything strung on the same thread, sharing everything, failing together. **Parallel**: side-by-side branches, each with its own private connection to the source, independent in life and death. The rules of each fit on a postcard; the consequences wire your house, your car, your phone's interior, and the national grid. Today you learn both architectures — and why one of them won almost everything.

# Lore Introduction

Hale's two stubborn lamp-pairs from last night anchor a workbench now spread with the Tower's circuit kits: cells, lamps, brass connectors, and coils of wire. "The Tower's oldest argument," she says, tapping the dim pair (strung in one loop) and the bright pair (on parallel rungs like a ladder). "Same cells. Same lamps. The architecture alone divides them." She hands you gauges. "Measure everything — flow at every point, push across every lamp — in both architectures. You will find each obeys two small rules with perfect stubbornness. Then—" she gestures at the workroom's wall, where the Tower's own wiring runs in neat parallel ladders to every lamp and instrument, "—you may tell me why every house, every ship, and this very Tower chose the ladder. And why, knowing all that, the fuse-box still puts ONE thing in series with everything."

# Core Learning

## Concept Introduction

**Series — one loop, one thread.** Components strung end-to-end, single path:

- **Current: identical everywhere** (charge conservation; nowhere else to go)
- **Voltage: shared** — the supply's joules-per-coulomb are spent across the components in proportion to their resistances (ΣV = supply)
- **Resistance: adds** — R_total = R₁ + R₂ + ... (one gauntlet after another)

Consequences: every added component dims the rest (more total R, less current); one break kills all (the loop is severed); switches anywhere control everything. Uses: where exactly that behaviour is wanted — switches and fuses in series with what they guard, sensor chains, old fairy lights (cheap: one wire path).

**Parallel — the ladder.** Components on separate branches spanning the same two supply points:

- **Voltage: identical across every branch** (each spans the same two points — full supply each)
- **Current: divides** among branches (more through lower-R branches; totals at junctions: ΣI_branches = I_supply)
- **Resistance: combined is BELOW the smallest branch** — each branch opens another road. Equal pair: half of one. Formula for the curious: 1/R_total = 1/R₁ + 1/R₂ + ...

Consequences: each branch runs at full design power; independence (switch, fail, or remove one branch — others unmoved); the supply must source the *sum* of all branch currents (why house mains cables are fat and why too many heaters trip the breaker).

**Why homes are parallel** — three reasons, one sentence each: every appliance receives its full 230 V (design power, predictable behaviour); every appliance switches independently; every failure is private. And the deliberate exception: **fuses, breakers, and switches sit in series** with what they protect — *sharing the current* is precisely their job, since a guard must stand in the only doorway.

## Why It Matters

- This is the grammar of all practical wiring — domestic circuits, car looms, PCB design, and the grid are parallel ladders with series guards.
- Equivalent-resistance fluency is the entry skill of electronics: every network reduces, combination by combination, to one number.
- The architecture explains everyday mysteries: dimming headlights at ignition (shared internal resistance), why one socket's load doesn't dim another, what "tripping the circuit" actually trips.

## Worked Examples

**Example 1: The ladder fully audited**
A 12 V battery feeds parallel branches of 6 Ω, 4 Ω, and 12 Ω. Each branch: full 12 V → currents 2 A, 3 A, 1 A; supply total 6 A. Equivalent R = 12/6 = 2 Ω — below the smallest branch (4 Ω), as the law of added roads demands. Remove the 4 Ω branch: the others run *unchanged* (still 12 V each); the supply relaxes to 3 A. Independence, demonstrated by subtraction.

**Example 2: The voltage divider (series put to work)**
Series 2 Ω and 10 Ω across 12 V: one current I = 12/12 = 1 A, so drops of 2 V and 10 V — the supply shared in resistance's proportion. This "divider" is electronics' favourite trick: tap the junction and you've manufactured any intermediate voltage from a fixed supply. Volume knobs, sensor read-outs, and reference voltages are series law, monetised.

**Example 3: Why headlights dim when the engine cranks**
The starter motor demands ~150 A. The battery's own internal resistance (~0.01 Ω, in series with EVERYTHING by nature) drops V = 150 × 0.01 = 1.5 V inside the battery itself — the terminals sag from 12.6 to ~11 V, and every parallel branch (headlights included) feels the sag. The parallel ladder's one shared vulnerability is the rail it hangs from: series resistance upstream of the ladder taxes all branches together.

## Common Mistakes

- **"Parallel resistors add"** — they *combine downward*; adding a branch always lowers total R. (Roads, not obstacles.)
- **"Current divides equally"** — only between equal branches; otherwise inversely with resistance (the easy road carries more).
- **Series voltage confusion** — components in series share the supply *in proportion to R*; the biggest resistor takes the biggest bite.
- **Treating switch position as mattering in series** — anywhere in the loop controls the whole loop; that's the point.
- **Forgetting the source's own resistance** — real batteries are a perfect source *in series with* a small internal R; heavy loads expose it (cranking dims, old cells sag).

## Mental Model

Series is **a single mountain pass with several toll-gates in file**: every traveller (coulomb) passes every gate, the gates' fees (voltage drops) sum to the journey's full purse, and one rockfall closes the route for everyone. Parallel is **a valley of separate bridges between the same two banks**: every bridge spans the full height (voltage), each carries its own traffic share by its own width, opening a new bridge only ever eases the crossing — and one bridge's collapse strands nobody else. Civilisation, having tried both, built its houses as valleys of bridges — and posted exactly one guard, in series, at each valley's single entrance, where every traveller must pass.

## Mini Summary

- ✔ Series: same I everywhere; V shares by resistance; R adds; one break kills all
- ✔ Parallel: same V on every branch; I divides and re-totals; combined R below the smallest branch
- ✔ Homes are parallel: full voltage, independence, private failures — with series guards (fuses, switches)
- ✔ Voltage dividers exploit series sharing; internal resistance is the supply's hidden series tax
- ✔ Reduce any network combination by combination to one equivalent R

# Guided Practice Quest

Work through the guided steps to chain twelve ohms in single file, halve six ohms by opening a second bridge, and write parallel wiring's three-line manifesto for every house since Edison.

# Solo Practice Quest

Three architectural commissions: (1) *Build both* (battery pack, two torch bulbs, wire — or a circuit simulator): wire the lamps in series, then parallel; record brightness, then unscrew one lamp in each architecture and report the survivor's fate. Explain all four observations with the postcard rules. (2) *Reduce a network*: a 12 V supply feeds a 2 Ω resistor in series with [a parallel pair: 6 Ω and 3 Ω]. Find the equivalent resistance, the supply current, the voltage across the parallel pair, and each branch's current — show the reduction steps. (3) *Map your home*: sketch (roughly) one room's wiring as a parallel ladder — sockets, lights, switches in their series positions — and mark where the breaker guards the entrance. Close with two sentences on why your kettle and toaster together trip the kitchen circuit, in junction-rule language.

# Integration

**Engineering**: Real electrical design is ladder management: ring mains, load balancing across phases, selective breaker coordination (the nearest guard trips first), and the automotive loom's fuse box. Electronics miniaturises the same grammar — every chip's power rails are parallel ladders; every signal divider is series law.

**Mathematics**: The parallel formula 1/R = Σ1/Rᵢ is your first harmonic combination — the same mathematics as combined work rates ("two painters together...") and lens equations later. Network reduction is algorithmic thinking: collapse, substitute, repeat — recursion with a screwdriver.

# Lore Conclusion

By lamplight you deliver the verdict Hale demanded: the ladder for the Tower — full push to every branch, each instrument its own master, no failure contagious — and the file for the guards, one fuse standing in every doorway it protects. She listens, then unlocks the workroom's final cabinet: inside, the Tower's master fuse-board, each ceramic guard labelled in generations of hands. "Architecture, chosen for reasons. Now —" she lifts down two of the labelled fuses, one slender, one stout, "— the reasons get numbers. Every guard here is rated in the one quantity we have not yet priced: how much WORK the river does as it flows. Power, junior — watts, bills, and the arithmetic that decides whether a wire warms a kettle or burns a house. Tomorrow we do the money."
