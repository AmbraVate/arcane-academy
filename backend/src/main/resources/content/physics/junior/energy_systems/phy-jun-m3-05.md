---
id: phy-jun-m3-05
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m3
moduleTitle: "Module 3: Thermodynamics"
moduleGlyph: "♨️"
moduleSortOrder: 3
topicSlug: energy_systems
topicTitle: "Energy Systems"
topicSortOrder: 2
title: "Sankey Diagrams and Efficiency"
sortOrder: 5
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Draw and read Sankey diagrams with widths proportional to energy flows
  - Compute efficiency from any branch of a Sankey diagram
  - Chain efficiencies through multi-stage systems
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Draws a Sankey diagram with arrow widths proportional to energy values
    - Computes efficiency = useful out / total in from diagram branches
    - Chains stage efficiencies multiplicatively for a system
    - Identifies the largest loss branch as the improvement target
  keywords: [Sankey, width, proportional, efficiency, useful, dissipated, chain, branch]
  modelAnswer: |
    A Sankey diagram draws energy flow as a river whose WIDTH is proportional to the energy it
    carries: input enters as one broad arrow, useful output continues forward, and every loss
    branches away (almost always downward, almost always as heat). Efficiency reads directly
    off the picture: useful width over input width — a filament lamp's thin 5% thread of light
    against its fat 95% heat branch tells the whole story at a glance. Multi-stage systems
    chain multiplicatively: a 40% power station feeding 90% transmission feeding a 90% motor
    delivers 0.4 × 0.9 × 0.9 ≈ 32% end to end. The widest loss branch is always the
    engineer's first target.
guidedSteps:
  - id: phy-jun-m3-05-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      On a Sankey diagram, the energy carried by each arrow is represented by its:
    inputConfig:
      options:
        - "Colour"
        - "Width — drawn proportional to the energy flow"
        - "Length"
        - "Label font size"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Width — drawn proportional to the energy flow"]
      rejectedFeedback: "Width IS the data: a branch carrying half the energy is drawn half as wide. Total width is conserved at every split — the First Law, enforced by ruler."
    hint: "What must be conserved across every fork of the river?"
    reflectionPrompt: "Why is 'widths must sum' just the First Law wearing drawing clothes?"
  - id: phy-jun-m3-05-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A Sankey diagram shows 500 J entering a motor; 350 J continues as useful mechanical output, 150 J branches off as heat. Efficiency = ________ %.
    inputConfig:
      placeholder: "70"
    markingRule:
      matchMode: CONTAINS
      accepted: ["70"]
      rejectedFeedback: "Efficiency = useful/total = 350/500 = 70%. The diagram shows it geometrically: the forward arrow is 70% of the input's width."
    hint: "Useful width over input width."
    reflectionPrompt: "What does the 150 J branch's downward droop conventionally signify?"
  - id: phy-jun-m3-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      An electric kettle is ~90% efficient; a gas power station feeding it is ~40%; the grid between them ~92%. Compute the fuel-to-hot-water efficiency, and explain why boiling water on a gas hob (~50% direct) can beat the 'efficient' electric kettle. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: ["33", "0.33", chain, multiply, upstream, gas hob, station]
      rejectedFeedback: "Chain: 0.40 × 0.92 × 0.90 ≈ 33% fuel-to-water. The gas hob burns fuel AT the pan: one stage, ~50% — beating the electrically-virtuous kettle because the kettle inherits its power station's chimney. Efficiency claims mean little until the whole chain is drawn; the widest loss sits upstream, out of sight."
    hint: "Multiply the three stages, then compare with the hob's single stage."
    reflectionPrompt: "How does this calculation change as the grid adds wind and solar (what happens to the 40% stage)?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Three stages of 80% efficiency each give an overall efficiency of:"
    options: ["80%", "240%", "51% — efficiencies multiply: 0.8³", "60%"]
    correctIndex: 2
    feedback: "Chains multiply: 0.8 × 0.8 × 0.8 = 0.512. Many 'pretty good' stages compound into a mediocre system — the quiet tragedy of long energy chains."
  - type: MULTIPLE_CHOICE
    question: "On a lamp's Sankey diagram, the filament version shows 5% light/95% heat; the LED shows 40% light/60% heat. The diagrams' real value is:"
    options:
      - "Decoration for reports"
      - "Making the loss structure VISIBLE so the improvement target is obvious at a glance"
      - "Proving lamps break the First Law"
      - "Showing colour rendering"
    correctIndex: 1
    feedback: "A Sankey turns invoices of numbers into geometry: the fat heat branch of the filament lamp is an argument no table communicates as fast. Engineers draw them precisely to find where the width goes."
---

# Hook

One picture ended the filament light bulb. For a century, "5% efficient" sat in tables nobody felt. Then draw it: electricity enters as a broad river — and the *light* leaves as a thread one-twentieth the width, while a great fat branch labelled HEAT droops away carrying the other ninety-five percent. The filament lamp, drawn honestly, is a heater with a side-hustle in illumination. Once governments and engineers saw the picture, the technology's days were numbered.

That picture is a **Sankey diagram** — energy flow drawn as rivers whose *width is the data* — and it is the working language of energy engineering: power stations, factories, national grids, and climate policy all draw their books this way. (The form's masterpiece predates energy engineering: Minard's 1869 chart of Napoleon's Russian campaign, where the river's width is an army, thinning from 422,000 to 10,000 men — called by many the finest statistical graphic ever drawn.) Today you learn to draw the books, read them at a glance, and chain them — whereupon some 'efficient' appliances will be exposed as inheritors of distant chimneys.

# Lore Introduction

The Bursar of Energies — whom you last saw ruling the Great Audit's final line in your Apprentice days — is waiting in the Foundry's drawing office, and Calde, unusually deferential, hands you over. "The Bursar draws what I forge." The office walls are the Academy's energy ledgers made visible: great inked rivers flowing across vellum sheets — the kitchens' sheet, the Tower's, the Foundry's own, each input branching into useful work and drooping losses, every width ruled to scale. The Bursar sets a fresh sheet before you, with a ruler and the Foundry's brass dividers. "Numbers hide, junior," she says — the first words you've ever heard her speak. "Widths confess. The Foundry's new engine sends me its trial figures tomorrow, and you will draw its confession. Today, you learn the river-craft: one rule of widths, one rule of forks, and the discipline of hunting the fattest loss." She taps the kitchens' sheet, where one scandalously wide branch droops from the old bread-oven. "That branch paid for its own replacement. Drawn well, junior, a diagram is a budget weapon."

# Core Learning

## Concept Introduction

**The Sankey diagram: energy as a scaled river.**

- **One rule of widths**: every arrow's width is proportional to the energy (or power) it carries. Choose a scale (1 mm = 10 J) and rule it honestly.
- **One rule of forks**: at every split, widths must sum — output widths = input width. This *is* the First Law, enforced geometrically: no river gains or loses water at a fork.
- **Conventions**: input enters left as one broad arrow; useful output flows on rightward; losses branch away (downward by custom), almost always labelled as heat to surroundings. Multiple stages chain left to right, each stage forking off its losses.

**Efficiency, read off the picture:**

```
efficiency = useful output width / input width
```

A glance suffices: the filament lamp's thread-of-light vs the LED's substantial beam; the petrol engine's fat exhaust-and-coolant branches (~70% of the river) vs the electric motor's thin whisker of loss.

**Chains multiply.** Stage efficiencies compound: power station (0.40) × grid (0.92) × charger (0.90) × motor (0.85) ≈ 28% well-to-wheel. Corollaries: long chains are quietly brutal (three "good" 80% stages = 51%); the *system's* number can shame every individual stage; and upgrading the *widest-loss* stage buys the most — the engineer's first question before any improvement is "where is the fattest branch?"

**What the droop means.** Loss branches are not destruction (the First Law forbids) but *dissipation*: energy degraded to low-grade heat, spread into surroundings, practically unrecoverable. Why unrecoverable is the next lesson's revelation — the Sankey's drooping branches are, secretly, portraits of entropy.

## Why It Matters

- Sankey literacy is professional currency: energy audits, factory optimisation, national energy statistics, and IPCC reports all speak it.
- Chained efficiency exposes greenwash and guides real choices: electric vs gas heating, hydrogen vs battery vehicles — every such debate is a duel of Sankey chains.
- "Hunt the fattest branch" is transferable optimisation discipline — it works on time budgets and money as well as joules.

## Worked Examples

**Example 1: Drawing the petrol car**
100 J of fuel in: ~30 J to the wheels (forward arrow), ~35 J exhaust heat, ~30 J coolant/radiator heat, ~5 J friction (three drooping branches). The picture explains the hot bonnet, the radiator's existence, and why engine R&D fights for single percentage points. Beside it, the EV's sheet: 100 J from the battery → ~85 J at the wheels, slim losses — but the honest auditor staples the *grid's* sheet upstream, and the comparison becomes a chain-versus-chain argument (which the EV still wins on most grids — and increasingly so as the 0.40 stage greens).

**Example 2: The Bursar's bread-oven case**
Old oven: 100 units of fuel → 18 to bread, 50 up the flue, 32 through the walls. The flue branch is the fattest: fit a flue-gas heat recuperator (pre-warming intake air), re-draw: 100 → 30 to bread. The wall branch (insulation) was the SECOND target. Hunt order set by width, returns measured by re-drawing — capital allocation by ruler.

**Example 3: Heat pumps break the picture (instructively)**
A heat pump's sheet looks illegal: 100 J of electricity in, 300 J of heat delivered to the house. Fraud? No — a second input river was missing: ~200 J drawn from the cold outdoors (the device pumps ambient heat uphill, spending electrical work as the pump-fee — your refrigerator's trick, reversed and domesticated). Drawn complete, widths sum perfectly. Moral: a Sankey that seems to break the First Law has a hidden river — find it. ("Efficiency" >100% gets renamed Coefficient of Performance, to keep the books polite.)

## Common Mistakes

- **Decorative widths** — a Sankey with unscaled arrows is a flowchart in costume; the width IS the content.
- **Forks that don't sum** — geometric First-Law violation; re-measure until conservation holds on the page.
- **Averaging chained stages** — 40% and 90% chain to 36%, never to 65%; multiplication, not arithmetic-mean charity.
- **Comparing appliances without their upstream chains** — the electric kettle's virtue depends on its power station; staple the sheets together before judging.
- **Reading loss as destruction** — drooping branches go SOMEWHERE (warmed rooms, exhausted air); the diagram tracks degradation, not disappearance.

## Mental Model

A Sankey diagram is **a river-system seen from above, where water is energy and the map is to scale**. The source river enters broad; every machine along its course is a weir that sends some flow onward down the main channel (useful) and bleeds some into marshes (losses — warm, shallow, unrowable). The First Law is the cartographer's oath: tributary widths always sum. Efficiency is the fraction of the headwater still in the navigable channel at the river's mouth. And energy policy, factory audits, and your own utility bills are all the same exercise: walk the mapped river upstream, find the widest marsh, and ask the only question that moves budgets — *can this weir be rebuilt?*

## Mini Summary

- ✔ Width = energy, to scale; forks must sum (the First Law, ruled)
- ✔ Efficiency = useful width / input width — readable at a glance
- ✔ Chains multiply: good stages compound into mediocre systems; staple upstream sheets before judging
- ✔ Hunt the fattest loss branch first — optimisation by ruler
- ✔ Drooping branches are dissipation, not destruction — and secretly, portraits of next lesson's entropy

# Guided Practice Quest

Work through the guided steps to rule widths that confess, read a motor's seventy percent at a glance, and staple a power station's chimney to an innocent-looking kettle.

# Solo Practice Quest

Three commissions from the drawing office: (1) *Draw three confessions*: rule honest Sankey diagrams (scale stated) for a filament lamp (5% light), an LED (40%), and a petrol car (30% to wheels, losses split as in the worked example) — one glance-verdict sentence under each. (2) *Chain audit*: build the full Sankey chain for charging your phone (station → grid → charger → battery → screen/radio losses) with researched-or-reasoned stage figures; compute end-to-end efficiency and circle the fattest branch of the whole chain. (3) *The hidden river*: draw a heat pump's complete diagram (electricity + outdoor heat in; indoor heat out) and write three sentences on why its "300%" is honest once all rivers are mapped. Close with your home's fattest energy branch (heating? hot water?) and the weir you'd rebuild first, with expected width-change.

# Integration

**Engineering**: Sankey diagrams are contractual documents in energy engineering: plant acceptance tests, ISO 50001 energy audits, and national energy balances (every country publishes an annual all-energy Sankey — find yours; it is one diagram that explains your nation's infrastructure). Pinch analysis — process-industry heat-recovery optimisation — is the fattest-branch hunt formalised into a discipline.

**Mathematics**: Sankey conservation is graph theory with weighted edges (flow networks — the same mathematics that routes internet packets and supply chains), and chained efficiency is the multiplication of fractions doing policy work. Minard's Napoleon chart is also your finest lesson in data visualisation ethics: width-as-data forbids the lie that decorated axes permit.

# Lore Conclusion

Your three sheets pass the Bursar's dividers — widths summing at every fork, scales declared, the lamp's confession ruled so starkly she holds it to the lamplight with what might be approval. "Fit for the wall," she says, which Calde later confirms is the drawing office's knighthood. Then she crosses to the office's great cabinet and unrolls the largest sheet you have ever seen: a Sankey the width of the wall, its headwaters labelled not in joules but in coal-fields, rivers, and wind — the energy ledger of the entire realm, from mines and dams to every city's lamps. "You can draw a lamp's confession now, and an engine's," she says. "Tomorrow you draw a NATION'S. Power stations, the grid's great arteries, storage against the calm and the dark — the widest rivers humans have ever channelled, and the marshes that drain a third of everything we burn." She weights the sheet's corners for the morning. "Sleep with your ruler close, junior. At this scale, a single percentage point of width is a city's worth of fire."
