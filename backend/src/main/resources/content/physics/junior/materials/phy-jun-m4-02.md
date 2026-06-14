---
id: phy-jun-m4-02
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m4
moduleTitle: "Module 4: Applied Physics"
moduleGlyph: "🔧"
moduleSortOrder: 4
topicSlug: materials
topicTitle: "Materials"
topicSortOrder: 1
title: "Stress, Strain, and Strength"
sortOrder: 2
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Compute stress (force/area) and strain (extension/length)
  - Interpret stress–strain curves — stiffness, yield, strength, toughness
  - Distinguish brittle from ductile failure and their warning behaviours
integrationDomains: [engineering, chemistry]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Computes stress = F/A (Pa) and strain = x/L (dimensionless)
    - Reads a stress–strain curve — gradient (Young modulus), yield point, ultimate strength
    - Distinguishes brittle (no warning) from ductile (yields first) failure
  keywords: [stress, strain, F/A, Young modulus, yield, ultimate strength, brittle, ductile]
  modelAnswer: |
    Stress is force per cross-section area (F/A, in pascals) — the loading a material actually
    feels; strain is fractional deformation (extension/original length, dimensionless). Their
    graph is a material's biography: the initial gradient is the Young modulus (stiffness as a
    material property, sample-size removed); the yield point is where elastic gives way to
    permanent flow; the curve's peak is ultimate strength. Ductile metals yield visibly before
    breaking — structures sag and warn; brittle materials (glass, cast iron, ceramics) snap at
    their limit without notice. Steel's ~400 MPa strength against bone's ~150 and spider
    silk's ~1,000 explains a lot of engineering — and of zoology.
guidedSteps:
  - id: phy-jun-m4-02-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A 10,000 N load hangs from a steel rod of cross-section 0.0001 m² (1 cm²). Stress = F/A = ________ MPa.
    inputConfig:
      placeholder: "100"
    markingRule:
      matchMode: CONTAINS
      accepted: ["100"]
      rejectedFeedback: "Stress = 10,000/0.0001 = 10⁸ Pa = 100 MPa — a quarter of mild steel's ~400 MPa strength: safe, with margin. Stress, not force, is what materials feel: the same load on half the area doubles it."
    hint: "Divide force by area; 10⁶ Pa = 1 MPa."
    reflectionPrompt: "Why do engineers quote material limits in stress rather than force?"
  - id: phy-jun-m4-02-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A ductile steel beam in an overloaded building SAGS visibly before collapse; a cast-iron one SNAPS without warning. The difference:
    inputConfig:
      options:
        - "Steel is weaker"
        - "Ductile materials yield — flowing plastically past their elastic limit, absorbing energy and visibly deforming — while brittle ones fracture at their limit with no plastic stage"
        - "Cast iron is always thinner"
        - "Steel contains warning sensors"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Ductile materials yield — flowing plastically past their elastic limit, absorbing energy and visibly deforming — while brittle ones fracture at their limit with no plastic stage"]
      rejectedFeedback: "Ductility is the gift of warning: yield lets atomic planes slip, soaking up energy and bending the structure conspicuously before failure. Brittle materials have no slip mechanism — elastic, elastic, elastic, GONE. Building codes prize ductility for exactly the evacuation time it buys."
    hint: "Which biography has a long plastic chapter between elastic and broken?"
    reflectionPrompt: "Why do earthquake codes REQUIRE ductile structures even at equal strength?"
  - id: phy-jun-m4-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A climbing rope and a glass rod might both carry your weight. Using the stress–strain vocabulary (stiffness, strength, toughness, brittleness), explain why you'd trust only one with a FALL. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [tough, energy, absorb, brittle, stretch, area under, snap, ductile]
      rejectedFeedback: "A fall delivers ENERGY, and toughness — the area under the stress–strain curve — measures energy absorbed before failure. The rope is compliant and tough: huge strain at moderate stress soaks the fall's joules. Glass is stiff and strong-ish but utterly brittle: minuscule strain capacity, near-zero area under its curve — the fall's energy exceeds it instantly and it shatters. Strength holds loads; TOUGHNESS survives events."
    hint: "Static load versus energy delivery. Which curve encloses area?"
    reflectionPrompt: "Why is 'strong but brittle' such a dangerous combination in impact situations?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Strain is defined as:"
    options:
      - "Force per unit area"
      - "Extension divided by original length — a pure ratio, often quoted as a percentage"
      - "The breaking force"
      - "Stress times area"
    correctIndex: 1
    feedback: "Strain = x/L: deformation per unit of original size, dimensionless. 1% strain means every metre stretched a centimetre — the size-free partner to stress's force-per-area."
  - type: MULTIPLE_CHOICE
    question: "The Young modulus of a material is:"
    options:
      - "Its breaking stress"
      - "The gradient of the stress–strain line's elastic region — stiffness as a material property, independent of sample shape"
      - "Its density"
      - "The same as its spring constant"
    correctIndex: 1
    feedback: "E = stress/strain: steel ~200 GPa, aluminium ~70, bone ~15, rubber ~0.01. It's Hooke's k with the geometry divided out — the material's own voice. (A sample's k = EA/L: same material, different shapes, different k.)"
---

# Hook

Two wires hang side by side, equal thickness: one steel, one spider silk. Which is stronger? Most people back the steel — and lose. Weight for weight, spider dragline silk outperforms high-grade steel several times over, and *toughness*-for-weight (energy absorbed before breaking — the property that stops a speeding bee) it isn't even close. Materials hold surprises like this everywhere: glass is *stiffer* than oak yet shatters at a tap; bone rivals concrete; chocolate has an engineering datasheet.

To compare materials honestly — across thicknesses, lengths, species, and centuries — physics needed to divide out the geometry: **stress** (force per area: what the material *feels*) against **strain** (fractional stretch: how it *answers*). The resulting curve is each material's biography — stiffness, yield, strength, toughness, and manner of death — and reading those biographies is the daily literacy of everyone who builds anything. Today, the Mechanica's wall of broken things teaches you to read.

# Lore Introduction

Vex's "wall of inquests" holds more than the snapped leaf-spring: a sheared rivet from a boiler case, a shattered glass insulator, a bent-but-unbroken bronze bracket tagged *honourable*, a frayed rope-end, a cracked porcelain knob. Each with its docket. Today the Mechanica's testing-frame stands ready beneath them — a screw-driven rack with a force gauge and an extension scale, the device every materials lab on earth descends from. "The library named springs by one number," Vex says. "Materials demand a richer confession." He clamps a wire of soft iron. "We pull until it tells us everything: how stiff, where it yields, what it carries at best, and how it dies." The screw turns; the gauges creep; the wire's story draws itself — straight, then bending, then a long stubborn plastic flow, then the snap. Vex mounts the two halves on a fresh docket, unsigned. "By tonight, junior, you sign it — in the four words of the trade: modulus, yield, strength, toughness."

# Core Learning

## Concept Introduction

**Dividing out the geometry.**

```
stress σ = F / A   (Pa; usually MPa)        strain ε = x / L   (dimensionless; often %)
```

Stress is what the material *feels* (same load, half the area, double the feel — your drawing-pin lesson, formalised); strain is its proportional answer. Quoting limits in stress/strain makes datasheets universal: one number per material, any sample size.

**The stress–strain biography** (pulling a ductile metal):
1. **Elastic straight line** — gradient = **Young modulus E** = σ/ε: stiffness as a *material property* (steel 200 GPa, aluminium 70, bone ~15, nylon ~3, rubber ~0.01). Hooke's law, geometry-free; a sample's spring constant rebuilds as k = EA/L.
2. **Yield point** — elastic ends; atomic planes begin slipping: permanent set begins. Design loads live *below* yield, with safety factors (typically 1.5–4).
3. **Plastic plateau/rise** — the metal flows, work-hardens, stretches dramatically (ductility). Energy is being soaked up.
4. **Ultimate strength** — the curve's peak stress (mild steel ~400 MPa; titanium alloys ~1,000; spider silk ~1,000+).
5. **Necking and fracture** — the wire thins locally and parts.

**Four words, four properties:**
- **Stiffness (E)** — slope: resistance to deforming at all
- **Yield strength** — where permanence begins
- **Ultimate strength** — the most it ever carries
- **Toughness** — *area under the whole curve*: energy absorbed before death — the property that survives impacts and falls

**Two manners of death:**
- **Ductile** (steels, copper, most alloys): long plastic chapter — structures *sag, bulge, and warn*. Codes love it: warning = evacuation time.
- **Brittle** (glass, ceramics, cast iron, chocolate): no plastic chapter — elastic then *gone*, often from a surface crack that concentrates stress (why glaziers score glass, and why one chip dooms a windscreen... eventually). Strong-but-brittle is the treacherous quadrant.

(And the wall's thumbnail-marked spring: **fatigue** — failure by thousands of small cycles below yield, cracks growing flight by flight. The inquest subject of countless engineering disasters, and the reason aircraft count their take-offs.)

## Why It Matters

- Every structure, vehicle, implant, and component is sized by these curves; safety factors against yield are the law of the built world.
- Material *selection* (next lesson) is shopping these four properties against weight and cost — the engineer's permanent quadrilemma.
- Failure literacy — ductile warning vs brittle surprise vs fatigue's patience — is how inquests are read and how the next disaster is designed out.

## Worked Examples

**Example 1: Sizing a lift cable**
Cabin + passengers: 20,000 N. Steel yield: 400 MPa; mandated safety factor 10 (lifts are unforgiving): working stress 40 MPa. Area needed = F/σ = 20,000/(4×10⁷) = 5×10⁻⁴ m² = 5 cm² — a bundle of ropes ~26 mm across. Add fatigue allowances (every journey is a cycle) and you've reproduced the elevator code's logic from two divisions.

**Example 2: Why bone, why not steel skeletons**
Bone: E ≈ 15 GPa, strength ~150 MPa, density 1,900 kg/m³ — and *tough*, with collagen blunting cracks (composite design: mineral stiffness + protein toughness). A steel skeleton would carry more — at triple the weight and metabolic cost, with worse fatigue repair (bone *heals*; steel files for retirement). Evolution shops the same datasheets, with maintenance contracts.

**Example 3: Reading the broken faces**
The wall's exhibits, decoded: the bronze bracket bent double but whole — ductile yield, honourable warning, occupants evacuated. The glass insulator's clean shell-pattern fracture — brittle, instant, originating at a chip. The leaf-spring's thumbnail of smooth rings then crystalline tear — fatigue: a crack growing per flexure for months, then one final overload. Three deaths, three confessions, all legible — forensic engineering is this lesson with a courtroom.

## Common Mistakes

- **Strong/stiff/tough as synonyms** — they are independent axes: glass (stiff, brittle), nylon (floppy, tough), biscuit (none of the above). Specify which you mean; engineering English is exact here.
- **Quoting force limits instead of stress** — "this steel holds 10 tonnes" is meaningless without area; materials feel stress.
- **Designing to ultimate strength** — design lives below *yield*, divided by safety factor; ultimate is the autopsy number.
- **Ignoring stress concentration** — holes, notches, and scratches multiply local stress (×3 or worse); cracks in brittle materials are detonators. Round your corners; polish your shafts.
- **Forgetting fatigue** — cyclic loads kill below yield, given patience; anything that vibrates, rotates, or pressurises repeatedly has a counted life.

## Mental Model

A material under load is **a workforce answering ever-increasing demands**. Stress is the workload per worker (force per area of atomic bonds on the job); strain is the overtime visibly accruing. The elastic region is sustainable effort — release the demand and the workforce springs back rested (gradient E: how grudgingly they give overtime at all). Yield is the breaking of morale: workers start permanently relocating (planes slipping) — output continues, but the organisation is never the same shape again. Ultimate strength is peak heroics; necking is the department everyone quietly abandoned; fracture is the resignation letter. Ductile workforces grumble long and loud first — management (the building's occupants) gets warning. Brittle ones say nothing, ever, until the morning everyone has gone — and the cracks in their culture concentrated the load all along.

## Mini Summary

- ✔ σ = F/A (what's felt), ε = x/L (the answer) — geometry divided out; datasheets made universal
- ✔ The curve's chapters: E (gradient) → yield (permanence begins) → ultimate (peak) → fracture
- ✔ Four independent virtues: stiffness, yield strength, ultimate strength, toughness (area under curve)
- ✔ Ductile warns (sag before collapse); brittle surprises (cracks concentrate stress); fatigue counts cycles
- ✔ Design below yield ÷ safety factor; respect notches, holes, and history

# Guided Practice Quest

Work through the guided steps to load a rod to an honest hundred megapascals, award ductility its life-saving warning, and trust the tough rope over the strong glass with your falling weight.

# Solo Practice Quest

Three inquests of your own: (1) *Test to destruction*: pull spaghetti (brittle), a strip of plastic bag (ductile — watch it neck!), and an elastic band to failure by hand or hung weights; sketch each one's qualitative stress–strain biography and label the chapters you observed. (2) *Size a member*: a swing's rope must carry a 900 N adult with safety factor 6; for nylon (strength ~75 MPa), compute the required cross-section and diameter — then redo for steel wire (400 MPa) and write one sentence on why playgrounds still choose the rope (vocabulary: toughness, and Module One's FΔt). (3) *Forensics*: photograph or find images of one ductile failure (bent guardrail), one brittle (shattered screen), one fatigue (cracked metal with beach-marks if you can find it); caption each with its confession. Close with the four-word datasheet (E, yield, ultimate, toughness — rough values or rankings) for a material of your choice.

# Integration

**Engineering**: This lesson is the gateway to structural and mechanical engineering proper: beam bending (stress varying across a section), pressure-vessel codes, fracture mechanics (Griffith's cracks — why brittle strength is really flaw statistics), and the fatigue S–N curves that schedule aircraft retirement. Materials testing labs run your wire-pull daily, standardised to the decimal.

**Chemistry**: The curve's chapters are bonding chemistry enacted: E from bond stiffness, yield from dislocation motion through the lattice (alloying elements pin them — why bronze beats copper and carbon makes steel), brittleness from bonding that offers no slip (ionic/covalent ceramics), and toughness engineered by composite chemistry — silk's sacrificial hydrogen bonds, bone's mineral-protein laminate, carbon-fibre's resin matrix.

# Lore Conclusion

You sign the soft-iron docket at dusk — *modulus low and honest; yield early; long ductile testimony; tough for its station; died necking, with full warning* — and Vex mounts it on the wall among the inquests, your first authorship there. He then takes from a drawer not a broken thing but a small case of pristine samples: steel, aluminium, oak, carbon-fibre weave, a sliver of bone, cork. "Reading biographies is the coroner's art," he says. "Tomorrow, the living art: CHOICE. A wing, a bridge, a hip-joint, a bicycle — each asks for virtues in conflict: stiff but light, strong but cheap, tough but stiff. No material wins every axis; engineering is the negotiation." He closes the case and almost — almost — smiles. "Tomorrow you shop with the whole datasheet, junior. And the day after, we leave dry land entirely: pressure, buoyancy, and the physics of things that float and fly."
