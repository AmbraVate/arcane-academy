---
id: phy-jun-m4-03
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m4
moduleTitle: "Module 4: Applied Physics"
moduleGlyph: "🔧"
moduleSortOrder: 4
topicSlug: materials
topicTitle: "Materials"
topicSortOrder: 1
title: "Choosing Materials"
sortOrder: 3
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Select materials by trading stiffness, strength, toughness, density, and cost
  - Use per-weight (specific) properties for transport applications
  - Explain composites as engineered property combinations
integrationDomains: [engineering, chemistry]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Matches materials to applications via the relevant property set
    - Uses specific (per-density) stiffness/strength where weight matters
    - Explains how composites combine virtues (fibres + matrix)
    - Includes cost, manufacturing, and failure mode in one real selection
  keywords: [selection, specific strength, density, composite, trade-off, cost, application]
  modelAnswer: |
    Material choice is negotiation among virtues that rarely travel together: stiffness,
    strength, toughness, density, cost, and manufacturability. Where weight rules — aircraft,
    bicycles, prosthetics — the deciding figures are specific properties: strength or stiffness
    PER kilogram, which is how aluminium displaced steel in airframes and carbon-fibre
    displaced both. Composites engineer combinations nature's pure materials refuse: stiff
    strong fibres (carbon, glass) carried in a tough binding matrix (resin), yielding
    bone-inspired materials with the fibres laid along the stresses. Every real selection
    also prices failure mode: brittle carbon forks fail rudely, steel bends first — sometimes
    the heavier, duller, kinder material is the professional choice.
guidedSteps:
  - id: phy-jun-m4-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Steel (strength 400 MPa, density 7,800 kg/m³) versus aluminium alloy (300 MPa, 2,700 kg/m³) for an aircraft part. Per KILOGRAM, the better load-carrier is:
    inputConfig:
      options:
        - "Steel — higher absolute strength"
        - "Aluminium — 300/2,700 beats 400/7,800: nearly twice the strength per unit weight"
        - "They tie"
        - "Neither can fly"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Aluminium — 300/2,700 beats 400/7,800: nearly twice the strength per unit weight"]
      rejectedFeedback: "Specific strength: aluminium 0.111 vs steel 0.051 (MPa per kg/m³) — the lighter metal carries almost 2.2× the load per kilogram of structure. Aircraft pay for every kilogram every flight: per-weight figures rule the sky. (Carbon-fibre, at ~600/1,600 ≈ 0.375, is why it now rules instead.)"
    hint: "Divide each strength by its density before comparing."
    reflectionPrompt: "Why does a BRIDGE often still choose steel, ignoring this very calculation?"
  - id: phy-jun-m4-03-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Carbon-fibre composite is stiff, strong, and light — yet bicycle makers still sell steel and titanium frames, and some couriers swear by them. The engineering case:
    inputConfig:
      options:
        - "Nostalgia only"
        - "Failure mode and toughness: composites fail suddenly (brittle, hidden damage after impacts), while metals dent, bend, and warn — plus cost and repairability differ hugely"
        - "Carbon is illegal in some races"
        - "Steel is lighter than carbon"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Failure mode and toughness: composites fail suddenly (brittle, hidden damage after impacts), while metals dent, bend, and warn — plus cost and repairability differ hugely"]
      rejectedFeedback: "Selection is never one axis. Carbon wins specific stiffness; steel wins toughness, damage tolerance (a crashed carbon frame may hide delamination; a steel frame shows its dents honestly), weld-repairability anywhere on Earth, and price. The courier's pothole-rich life weights those axes differently from the racer's weighed grams."
    hint: "Recall yesterday's manner-of-death lesson. Who warns? Who hides damage? Who's repairable?"
    reflectionPrompt: "Which axes would dominate for a child's bike? A war-zone ambulance? A record-attempt machine?"
  - id: phy-jun-m4-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what a composite material is and why carbon-fibre-in-resin outperforms both bare fibres and bare resin — using bone or another natural composite as your parallel. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [fibres, matrix, resin, combine, crack, toughness, bone, collagen, direction]
      rejectedFeedback: "A composite marries two materials' virtues: stiff, strong fibres (carbon, glass) carry the loads, while the surrounding matrix (resin) binds them, shares stress between fibres, and blunts cracks — bare fibre bundles fray, bare resin is weak and brittle, the marriage is stiff AND tough. Bone got there first: rigid mineral platelets in tough collagen protein. Bonus virtue: fibres can be LAID ALONG the load paths — material placed only where the stress will be."
    hint: "Two ingredients, two jobs: who carries, who binds and blunts?"
    reflectionPrompt: "Why is plywood — humble as it is — a composite masterclass? (Think direction.)"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "For transport applications, materials are ranked by SPECIFIC properties, meaning:"
    options:
      - "Properties measured very precisely"
      - "Properties divided by density — performance per kilogram carried"
      - "Properties unique to one material"
      - "Manufacturer's specifications"
    correctIndex: 1
    feedback: "Specific strength/stiffness = property ÷ density. Every kilogram of vehicle structure is paid for in fuel forever — the sky, the road, and the peloton all shop per-kilogram."
  - type: MULTIPLE_CHOICE
    question: "A complete professional material selection weighs:"
    options:
      - "Strength only"
      - "Mechanical properties AND density, cost, manufacturability, failure mode, environment (corrosion, temperature), and lifecycle"
      - "Whatever is shiniest"
      - "Density only"
    correctIndex: 1
    feedback: "The datasheet is necessary, never sufficient: a perfect material you cannot afford, form, join, inspect, or recycle loses to a good-enough one you can. Engineering selection is multi-axis negotiation with a budget."
---

# Hook

Why is a jumbo jet aluminium, a bridge steel, a bicycle (sometimes) carbon, a hip implant titanium, and a wine cork still, after three thousand years of materials science, *cork*? Not because any of them is "the best material" — there is no such thing. Each is the winner of a different negotiation, fought across axes that refuse to cooperate: the stiffest materials are rarely tough, the strongest rarely cheap, the lightest rarely durable, and the one that aces every mechanical test may be impossible to weld, mould, or afford.

Materials selection is engineering's permanent auction, and the bidding currency changes by application: bridges bid in absolute strength per pound sterling; aircraft bid in strength per *kilogram* (every gram is fuel, forever); crash structures bid in toughness; implants bid in corrosion and biocompatibility. Today you learn to run the auction — and to recognise its greatest modern trick: when no single material wins, *engineer a marriage* and call it a composite.

# Lore Introduction

Vex empties the sample case across the bench — steel, aluminium, oak, carbon weave, bone, cork, glass, a lump of grey cast iron — and beside them deals out a stack of commission cards from the Mechanica's actual order-book: *a courier bicycle frame; a footbridge over the mill-race; a glider wing-spar for the Observatory's weather-watchers; a handle for the Foundry's biggest hammer; a cask bung*. "The Academy's standing game," he says. "Materials to the left, commissions to the right. Every junior plays; every junior loses at least one round to the cork." He flips the first card. "The rules: you must argue in PROPERTIES — stiffness, strength, toughness, weight, cost, manner of death — and the winning argument names not only why your choice serves, but why the obvious rival fails. Steel is not 'strong', junior. Steel is strong, heavy, cheap, tough, weldable, and rusts. Argue like that, or the cork takes another round."

# Core Learning

## Concept Introduction

**The axes of the auction** (no material wins them all):

| Axis | Champions | Paupers |
|------|-----------|---------|
| Stiffness (E) | Diamond, carbon fibre, steel | Rubber, foam |
| Strength | Carbon fibre, titanium, silk | Chalk, cork |
| Toughness | Steels, nylon, silk, bone | Glass, ceramics, cast iron |
| Lightness | Foams, wood, carbon, magnesium | Lead, steel |
| Cost | Concrete, mild steel, wood | Titanium, carbon, anything aerospace |
| Formability/joining | Thermoplastics, mild steel | Ceramics, exotic composites |

**Specific properties — the transport currency.** Where structure must *carry itself* (aircraft, vehicles, bikes, birds), divide by density:

```
specific strength = strength / density;  specific stiffness = E / density
```

This reranking rewrote history: aluminium (weaker than steel, 2.2× better per kg) took the skies in the 1930s; carbon composite (≈0.375 vs steel's 0.051) is taking them now. Bridges, anchored to the ground, keep buying absolute-strength-per-pound: steel and concrete reign.

**Composites — engineering the marriage.** Combine a **fibre** (stiff, strong, but fray-prone alone: carbon, glass, flax) with a **matrix** (tough binder that shares load between fibres and blunts cracks: resin) and the pair beats both parents: stiff AND tough, with two bonus powers — *anisotropy by design* (lay fibres along the computed load paths: material only where stress lives) and tailored everything (layup, weave, thickness per zone). Nature published first: **bone** (mineral platelets in collagen), **wood** (cellulose fibres in lignin — and plywood's crossed plies fix wood's weak direction), shell, silk. Modern aircraft are now ~50% composite by weight; wind-turbine blades couldn't exist without them.

**The neglected axes that decide real auctions:** corrosion (why marine fittings are bronze and stainless), temperature (engines forbid plastics), fatigue life, inspectability (carbon hides bruises; steel confesses dents), repairability (steel welds in any village on Earth), recyclability, and always — *manner of death*: where impacts and lives are involved, the tough, warning, duller material often wins the professional's bid over the brilliant brittle one.

## Why It Matters

- Every object you own is a settled auction; reading objects as property-arguments ("why is this part THIS?") is the fastest engineering education available.
- Specific-property thinking explains technological history — bronze→iron→steel→aluminium→composite is one long re-ranking — and predicts its next chapters.
- Composite literacy is current-affairs literacy: wind blades, EV bodies, sports equipment, and aerospace are all composite stories with economic stakes.

## Worked Examples

**Example 1: The wing spar commission, audited**
Glider spar: bending stiffness per kilogram rules. Candidates: spruce (the 1930s answer — superb specific stiffness, lovely to work), aluminium (the 1950s answer — consistent, jointable), carbon (the modern answer — specific stiffness triple aluminium's, laid along the span's computed stresses). Carbon wins the performance bid; spruce still wins the *budget* glider, and the argument-card must say both. (The cork abstains.)

**Example 2: Why the hammer handle is ash (or fibreglass), never steel**
The commission's hidden axis: **shock**. A steel handle transmits the strike's jolt to the smith's elbow (stiff = faithful transmission — here a vice); ash flexes, its toughness soaking the impact (Hooke storing, structure releasing gently), and it's grippy, light, cheap, and replaceable in any village. Fibreglass adds weatherproofing. The strongest material loses the round to the *kindest* — a recurring upset worth memorising.

**Example 3: The cork's undefeated round**
Cask bung: must seal (conformable — squashes to the hole, E near nothing), grip (high friction wet), resist liquid (closed cells, near-impermeable), not taint contents (chemistry), survive cycles of insertion (resilient), float (forgiveness for the clumsy), and cost pennies. Steel fails five axes, carbon six, and three millennia of materials science have produced exactly one serious rival (engineered polymer corks — which are imitations). Sometimes the auction was won in the Bronze Age; respect the incumbents.

## Common Mistakes

- **Crowning a "best material"** — every choice is application-relative; the question is always *best at what, weighted how, at what price*.
- **Absolute properties for transport** — divide by density first or the sky will correct you; conversely, specific properties for anchored structures over-pay for lightness nobody needed.
- **Ignoring manner of death** — spec-sheet heroes with brittle failure have killed; toughness and warning are properties, not sentiments.
- **Forgetting manufacture and repair** — a material you cannot form, join, inspect, or fix is a museum piece; half the auction is downstream.
- **Treating composites as magic** — they trade too: cost, impact-hidden damage, recycling headaches, and anisotropy punishes loads from unplanned directions.

## Mental Model

Material selection is **casting a play, not crowning an actor**. The commission card is the role: it specifies the lines (loads), the stage (environment), the run length (fatigue life), the budget, and — crucially — how the character may be permitted to die. Steel is the dependable lead who can play almost anything, works cheap, and always telegraphs the death scene. Carbon-fibre is the brilliant specialist: astonishing in the right role, ruinous to hire, and apt to exit without warning if bruised in rehearsal. Cork is the character actor who has owned one role since antiquity. The director's craft — engineering — is reading the script's true demands and resisting the urge to cast the famous name in every part.

## Mini Summary

- ✔ No best material — only best-for-this: negotiate stiffness, strength, toughness, weight, cost, formability
- ✔ Transport shops per-kilogram: specific strength/stiffness rewrote aviation twice
- ✔ Composites marry fibre (carry) and matrix (bind, blunt) — stiff AND tough, laid along the loads; bone and wood published first
- ✔ The deciding axes are often unglamorous: corrosion, fatigue, inspectability, repair, manner of death
- ✔ Read every object as a settled auction — and respect the cork

# Guided Practice Quest

Work through the guided steps to re-rank steel and aluminium per kilogram, defend the courier's steel frame against the racer's carbon, and marry fibres to matrix the way bone always has.

# Solo Practice Quest

Play the Mechanica's game at home: (1) *Five auctions*: for five objects within reach (a chair leg, a phone screen, a saucepan, a bike part, a shoe sole), name the material, list the three axes it wins, the axis it loses, and the rival it beat — one card each. (2) *Specific showdown*: compute specific strength for steel (400/7,800), aluminium (300/2,700), titanium (900/4,500), carbon composite (600/1,600), and oak (~100/700); rank, then write two sentences on why the bridge and the aircraft read your table oppositely. (3) *Composite forensics*: examine plywood, a fibreglass object, or a sports racket — identify fibre, matrix, and the direction logic of the layup. Close with your own commission: specify a product you wish existed, write its card (loads, environment, budget, permitted death), and cast the material with a three-sentence argument.

# Integration

**Engineering**: Professional selection uses Ashby charts — property-versus-property maps (E vs density, strength vs cost) where materials cluster by family and design guidelines slice diagonally; entire university courses and the CES software industry formalise today's auction. Aerospace certification adds the paperwork: every composite layup on a airliner has a traceable pedigree.

**Chemistry**: Properties are chemistry's emergent vote: metallic bonds (ductile, conductive), covalent networks (stiff, brittle), polymer chains (tough, temperature-shy), and the alloying/heat-treatment dark arts that turn one iron into ten steels. Materials chemistry's frontier — graphene, aerogels, self-healing polymers, high-entropy alloys — is tomorrow's auction upsets being bred in labs today.

# Lore Conclusion

The game runs late; you take the wing-spar and the hammer-handle rounds outright, fight the bicycle to an honourable draw conditional on the customer's potholes — and lose, as foretold, to the cork, conceding its round with the respect Vex's rules demand. He gathers the cards, shuffles the bung back into the case. "The auction never closes, junior — new materials enter, prices shift, and the commissions keep coming. But you argue in properties now, which is all the Mechanica asks." He stands, and through the workshop's high windows the mill-race glitters in the moonlight, the Academy's water-wheel turning steadily in its channel. "Dry land's materials are taught. Tomorrow the commissions float: pressure that climbs with depth, the upthrust that carried Archimedes shouting through the streets, and the moving water that turns that wheel. Fluids, junior — the physics of everything that pours."
