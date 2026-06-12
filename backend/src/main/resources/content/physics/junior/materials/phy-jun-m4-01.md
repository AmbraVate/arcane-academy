---
id: phy-jun-m4-01
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m4
moduleTitle: "Module 4: Applied Physics"
moduleGlyph: "🔧"
moduleSortOrder: 4
topicSlug: materials
topicTitle: "Materials"
topicSortOrder: 1
title: "Elasticity and Hooke's Law"
sortOrder: 1
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Apply Hooke's law (F = kx) and interpret the spring constant
  - Identify the limit of proportionality and elastic limit
  - Calculate energy stored in stretched springs (E = ½kx²)
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Applies F = kx and reads k as stiffness with units N/m
    - Identifies proportional and elastic limits on a force-extension graph
    - Computes stored elastic energy via ½kx² (or graph area)
  keywords: [Hooke, F = kx, spring constant, elastic limit, proportionality, ½kx², extension]
  modelAnswer: |
    Hooke's law: within its limit, a spring's extension is proportional to the applied force,
    F = kx, where the spring constant k (N/m) measures stiffness — a 200 N/m spring stretches
    5 cm under 10 N. The force–extension graph runs straight to the limit of proportionality;
    beyond the elastic limit the material deforms permanently. The area under the line is the
    stored elastic energy, E = ½kx² — the bow's arrow-budget and the trampoline's bounce.
    Hooke's relation underlies springs, bonds between atoms, and every structure that flexes
    and returns.
guidedSteps:
  - id: phy-jun-m4-01-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A spring with k = 200 N/m is stretched 0.05 m. The force required: F = kx = ________ N.
    inputConfig:
      placeholder: "10"
    markingRule:
      matchMode: CONTAINS
      accepted: ["10"]
      rejectedFeedback: "F = 200 × 0.05 = 10 N. The constant k is the price per metre of stretch — stiff springs charge more."
    hint: "Multiply stiffness by extension."
    reflectionPrompt: "What extension would 25 N buy — while the law still holds?"
  - id: phy-jun-m4-01-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A spring stretched past its elastic limit:
    inputConfig:
      options:
        - "Returns to its original length when released"
        - "Stays permanently deformed — the atomic layers have slipped, not just stretched"
        - "Becomes stiffer forever"
        - "Snaps immediately"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Stays permanently deformed — the atomic layers have slipped, not just stretched"]
      rejectedFeedback: "Below the elastic limit, atomic bonds stretch and recover (elastic). Beyond it, planes of atoms slip past each other (plastic deformation): release the force and the new shape remains. The overstretched slinky never comes home."
    hint: "Elastic = recoverable; plastic = permanent. Which side of the limit are we?"
    reflectionPrompt: "Where does the work you did on a plastically-deformed spring END up? (The energy ledger never closes.)"
  - id: phy-jun-m4-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      An archer draws a bow (effective k = 400 N/m) back 0.5 m. Compute the stored energy, and estimate the arrow's launch speed if 80% transfers to a 0.03 kg arrow. (3–4 sentences of working.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["50", "40", "51", "52", kinetic, ½kx², transfer]
      rejectedFeedback: "E = ½kx² = ½ × 400 × 0.25 = 50 J stored. Arrow gets 40 J: ½mv² = 40 → v² = 2,667 → v ≈ 52 m/s. The draw's force–extension triangle IS the energy — archery is Hooke's law with feathers."
    hint: "½kx² first; then 80% into ½mv² and solve for v."
    reflectionPrompt: "Why does drawing the LAST 10 cm store more energy than the first 10 cm?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The spring constant k measures:"
    options:
      - "The spring's length"
      - "Stiffness — newtons of force per metre of extension"
      - "The maximum force a spring survives"
      - "The spring's mass"
    correctIndex: 1
    feedback: "k = F/x (N/m): the exchange rate between force and stretch. Car suspension ~50,000 N/m; a click-pen spring ~200; atomic bonds, modelled as springs, ~hundreds of N/m each."
  - type: MULTIPLE_CHOICE
    question: "Energy stored in a Hookean spring grows with the SQUARE of extension because:"
    options:
      - "Energy always squares"
      - "The force itself grows as you stretch — later centimetres are bought against bigger forces; the graph's triangle area is ½ × kx × x"
      - "Springs cheat"
      - "It doesn't — it's linear"
    correctIndex: 1
    feedback: "Each successive centimetre costs more than the last (F has risen): summing the growing cost = area under the F–x line = ½kx². Double the draw, quadruple the arrow's budget."
---

# Hook

In 1676, Robert Hooke — Newton's bitterest rival, curator of experiments at the Royal Society, a man so quarrelsome his portrait may have been deliberately lost — published his law as an anagram: *ceiiinosssttuv*. Two years later he revealed it: *Ut tensio, sic vis* — "as the extension, so the force." A spring fights back in exact proportion to how far you stretch it.

It sounds almost too humble to matter. It is arguably the most-used equation in mechanical engineering: it tunes every car's suspension and every guitar string, sets every bathroom scale and force gauge, stores every bow-shot and trampoline bounce — and, at the bottom of everything, it is how *atoms themselves* push back, which is why nearly every solid object behaves as a spring if you ask it gently. Module Four is the applied arts; its first tool is the oldest one: the law of the spring.

# Lore Introduction

The Mechanica again — and Vex, reading Calde's letter with a dry expression ("she says you can audit a joule to its grave; we shall see if you can catch one mid-flight"), leads you to a wall of the workshop you've never studied: the spring library. Hundreds of them, watch-hair fine to wagon-leaf massive, each tagged with a number and the Mechanica's stamp. "Every spring in this library has exactly one secret," Vex says, handing you a mid-sized coil, a hanger, and a box of standard masses, "and the secret is a single number. Load it. Measure it. Plot it." You hang mass after mass, reading extensions — and your points march up the slate in a line so straight it looks drawn with his ruler. "There," says Vex, tapping the gradient. "The spring's name, in newtons per metre. Every spring here answers to its number — up to the line's end. And what happens past the line's end, junior, is where engineering either earns its fee... or attends the inquest."

# Core Learning

## Concept Introduction

**Hooke's law.** Within limits, extension is proportional to applied force:

```
F = k x
force (N) = spring constant (N/m) × extension (m)
```

**k is the stiffness** — the price per metre of stretch: click-pen spring ~200 N/m, car suspension ~50,000, a railway buffer in the millions. Read it off a force–extension graph as the **gradient** of the straight region. (Compression obeys the same law in reverse; "extension" means deformation from natural length, either way.)

**The limits — where the law ends:**
- **Limit of proportionality**: the graph's straight section ends; F = kx no longer exact.
- **Elastic limit** (at or just beyond): the last point of full recovery. Below it: bonds *stretch* and spring back (elastic). Beyond: atomic planes *slip* — **plastic deformation**, permanent (the ruined slinky, the bent paperclip — which also *warms* as you flex it: the lost work dissipating, your energy ledger refusing to close quietly).
- Eventually: fracture. The full force–extension story of a material — straight, curving, yielding, breaking — is its mechanical biography (next lesson reads those biographies across materials).

**Stored elastic energy.** Work done stretching = area under the F–x graph. For the straight region, a triangle:

```
E = ½ k x²
```

The square matters: doubling a bow's draw quadruples the arrow's budget; the last centimetres of any stretch are the expensive, energy-rich ones. Springs are thus **energy banks** — bows, trampolines, clockwork, catapults, and the bonds in a stretched climbing rope all deposit on the way out and pay back on the return (minus, in real materials, a dissipative skim — why bounces decay).

**Why so universal: atoms are springs.** Interatomic bonds resist stretch and compression almost linearly for small displacements — so *every solid* is a vast spring network, and Hooke's law is the small-print of matter itself. (It's why solids transmit sound, why structures flex measurably under load, and why "rigid" is always a matter of degree.)

## Why It Matters

- Springs are engineering's measuring instruments (force gauges, scales), energy stores (clockwork to crash barriers), and motion controllers (suspension, valves, keyboards).
- The elastic/plastic boundary is the line between a structure that survives and one that's permanently bent — design lives below it with safety margins.
- ½kx² is your third energy account (after mgh and ½mv²): the complete classical trio that runs every mechanics problem from here on.

## Worked Examples

**Example 1: Reading a suspension spring**
A car corner carries 3,500 N and settles 70 mm on its spring: k = F/x = 3500/0.07 = **50,000 N/m**. Four passengers (+1,200 N total) settle it a further 1200/(4 × 50,000) = 6 mm. Suspension design is k-shopping: soft enough to swallow bumps (big x per pothole-F), stiff enough not to wallow — plus dampers to skim the bounce energy (Hooke stores; friction must spend, or the car pogoes).

**Example 2: The climbing rope's gentle arrest**
A falling climber must be stopped (Module One: Δp needs FΔt). A steel cable (huge k) stops her in centimetres — forces in the tens of kN: fatal. A dynamic rope (low effective k) stretches metres, storing ½kx² across a long, soft arrest — peak force a survivable ~5 kN. The rope is a spring *engineered for its energy triangle*: Hooke's law as a life-safety device.

**Example 3: Weighing with a graph**
A force gauge is a spring with a scale: calibration IS Hooke's law (equal marks per newton — only legitimate while the graph stays straight, which is why gauges state a max load). Your bathroom scale: a stiff spring (or its strain-gauge descendant) reading x and printing kx. Every "weight" you've ever read was an extension, translated.

## Common Mistakes

- **Using F = kx beyond the straight region** — past the proportional limit the equation lies; real gauges and designs stay inside with margin.
- **Confusing stiff with strong** — k says how much force per stretch; strength says where it breaks. Glass is stiff and weak in tension; nylon is floppy and strong (next lesson formalises this).
- **E = kx² without the ½** — the triangle's area, not the rectangle: the force wasn't full-strength for the whole journey.
- **Forgetting natural length** — x is extension FROM unloaded length, not total length; misreading this doubles errors silently.
- **Expecting perfect energy return** — real springs skim a little to heat per cycle (hysteresis); bounce decay and warm squash-balls are the receipts.

## Mental Model

A spring is **an honest moneylender for mechanical energy**. His rate is fixed and posted — k, newtons per metre — and his ledger is the force–extension line. Borrowing (stretching) costs you increasingly: each successive centimetre is loaned against the *accumulated* pull, so your total deposit grows as the triangle ½kx². Repayment (release) is full and prompt... within his terms. But push the loan past the elastic limit and you've broken the contract: the lender's books (atomic planes) slip, the deposit is partly seized as heat, and the spring — like any lender betrayed — never quite trusts you with the old rate again.

## Mini Summary

- ✔ F = kx within the proportional limit; k = stiffness (N/m) = the graph's gradient
- ✔ Elastic (recovers) vs plastic (permanent slip) — the elastic limit divides them
- ✔ Stored energy = area under F–x = ½kx²; doubling stretch quadruples the bank
- ✔ Stiff ≠ strong; gauges and designs live inside the straight region with margins
- ✔ Atoms bond like springs — Hooke's law is the small print of every solid

# Guided Practice Quest

Work through the guided steps to price a stretch at ten newtons, retire an overstretched slinky with honour, and loose a fifty-joule arrow from a Hookean bow.

# Solo Practice Quest

Three commissions from the spring library: (1) *Name a spring*: rig any spring or elastic band with a hanger and known masses (coins, water bottles weighed on kitchen scales); record force vs extension for 6+ loads, plot, and report k from your gradient with uncertainty — note where (if anywhere) your band's line bends (bands are imperfect Hookeans; say so like a professional). (2) *Energy audit*: for your spring at maximum measured extension, compute ½kx²; then design on paper a launcher using it and predict a projectile's speed at 70% transfer. (3) *The biography*: take one sacrificial paperclip through its full life — gentle flex (elastic, returns), firm bend (plastic, stays), repeated bending to failure (notice the WARMTH at the bend before it snaps — report where that energy went). Close with two sentences on why your bathroom scale is a Hooke's-law instrument and what its maximum rating protects.

# Integration

**Engineering**: Spring design is a profession's corner: coil geometry setting k, fatigue life under cycling (the paperclip's fate, scheduled), wave springs and gas springs and torsion bars — and in structures, Hookean flex is *designed in*: skyscrapers sway metres, aircraft wings flap visibly, and bridges breathe, all within calculated elastic budgets.

**Mathematics**: F = kx is linearity itself — and ½kx² previews integration (area under a varying force). At Senior tier, the mass-on-spring becomes the *harmonic oscillator* — physics' single most reused model, from pendulums to molecules to circuits — and k's square root will set its rhythm. You have just met the protagonist of half of physics.

# Lore Conclusion

Your spring's name — forty-two hundred, plus or minus eighty, in newtons per metre — joins the library tag in your handwriting, beside the gradient-plot Vex inspects without comment (his highest grade). He racks the spring, then takes down instead the wall's grim centrepiece, which you'd taken for decoration: a massive leaf-spring, snapped clean across, its fracture face gleaming. "From the mail-coach inquest, sixty years past," he says. "It obeyed Hooke faithfully for two hundred thousand miles, and then it did not." He turns the broken face to the lamplight: crystalline, with a telltale thumbnail of fatigue creeping from one corner. "The law you learned today has limits, junior, and the limits have anatomy. Tomorrow: stress, strain, and strength — how materials carry, how they fail, and how engineers read a broken face like a confession. Bring respect. This wall's exhibits all earned their places the hard way."
