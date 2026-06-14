---
id: phy-jun-m1-12
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m1
moduleTitle: "Module 1: Advanced Mechanics"
moduleGlyph: "🎯"
moduleSortOrder: 1
topicSlug: mechanical_systems
topicTitle: "Mechanical Systems"
topicSortOrder: 4
title: "Equilibrium and Stability"
sortOrder: 12
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - State the two conditions for equilibrium (zero resultant force and zero resultant moment)
  - Locate centres of gravity and use them to judge stability
  - Explain toppling via the line of action of weight and the support base
integrationDomains: [engineering, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States both equilibrium conditions (forces balance AND moments balance)
    - Locates the centre of gravity of simple and composite objects
    - Applies the toppling rule — weight's line of action outside the base means falling
    - Connects low centre of gravity and wide base to stability in real designs
  keywords: [equilibrium, centre of gravity, base, topple, line of action, stable, moments]
  modelAnswer: |
    Full equilibrium demands two balances: zero resultant force (no acceleration) and zero
    resultant moment (no rotation) — a beam can have balanced forces yet still spin. Every
    object behaves as if its weight acts at one point, the centre of gravity; an object on a
    surface stays standing while the vertical line through its centre of gravity passes inside
    its support base, and topples the moment that line crosses outside. Stability is therefore
    engineered by lowering the centre of gravity and widening the base — racing cars, standing
    toddlers, Bunsen burners, and cranes with outriggers all obey. The leaning tower stands
    because its plumb-line still lands inside its footprint, with metres to spare no longer.
guidedSteps:
  - id: phy-jun-m1-12-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A ladder leans against a wall, motionless. Which is TRUE of its equilibrium?
    inputConfig:
      options:
        - "Only the forces on it must balance"
        - "Both the resultant force AND the resultant moment about any point must be zero"
        - "Only the moments must balance"
        - "Ladders are never in equilibrium"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Both the resultant force AND the resultant moment about any point must be zero"]
      rejectedFeedback: "Equilibrium has two clauses: ΣF = 0 (no acceleration) and ΣM = 0 (no rotation). The ladder problem is famous precisely because both books must close at once — force balance alone wouldn't stop it rotating and sliding."
    hint: "Could something have balanced forces yet still start to rotate?"
    reflectionPrompt: "Which force stops the ladder's base sliding out — and what happens on a frictionless floor?"
  - id: phy-jun-m1-12-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A double-decker bus is tilt-tested. It stays upright at alarming angles because:
    inputConfig:
      options:
        - "The tyres grip the tilting platform"
        - "Its heavy chassis and engine sit low: the vertical line from its centre of gravity stays inside the wheelbase until a steep angle"
        - "Passengers lean the other way"
        - "Buses are bolted to the road"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Its heavy chassis and engine sit low: the vertical line from its centre of gravity stays inside the wheelbase until a steep angle"]
      rejectedFeedback: "Toppling is decided by geometry: weight's vertical line versus support base. Low-slung mass keeps that line inside the wheelbase to extreme tilts — regulators famously test double-deckers loaded upstairs-only to prove the worst case."
    hint: "Draw the plumb-line from the centre of gravity. Where must it land?"
    reflectionPrompt: "Why are standing passengers allowed downstairs but limited upstairs?"
  - id: phy-jun-m1-12-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You can stand with your heels and back against a wall, but you cannot then bend forward to touch your toes without falling. Explain with centre of gravity and base. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [centre of gravity, forward, base, feet, hips, backward, line, outside]
      rejectedFeedback: "Bending forward shifts your centre of gravity forward; normally your hips swing BACKWARD to keep the combined centre over your feet. The wall forbids the counterbalance — your centre of gravity's plumb-line crosses in front of your toes (the base), and the toppling rule does the rest."
    hint: "What does your body usually do behind you when you bow forward? What does the wall prevent?"
    reflectionPrompt: "Find two more body-movements that secretly exist to keep your plumb-line inside your feet."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An object topples when:"
    options:
      - "It gets too heavy"
      - "The vertical line through its centre of gravity passes outside its support base"
      - "Its base is wet"
      - "It is taller than it is wide"
    correctIndex: 1
    feedback: "Pure geometry: inside the base, the weight's moment rights the object; outside, the same weight's moment tips it further. Heaviness changes the drama, not the verdict."
  - type: MULTIPLE_CHOICE
    question: "Which combination is most stable?"
    options:
      - "High centre of gravity, narrow base"
      - "Low centre of gravity, wide base"
      - "High centre of gravity, wide base"
      - "Stability doesn't depend on these"
    correctIndex: 1
    feedback: "Low and wide maximises the tilt needed before the plumb-line escapes the base — the design rule behind racing cars, pyramids, camera tripods, and judo stances."
---

# Hook

The Leaning Tower of Pisa has been falling for eight hundred years — and never falls. Tilted a full four degrees, its top overhanging its base by nearly four metres, it stands because of a single geometric technicality: the vertical line dropped from its centre of gravity *still lands inside its foundation ring* — these days with about the margin of a dinner table. Engineers spent the 1990s removing soil from under the high side not to straighten it, but to claw back centimetres of that margin.

Everything that stands — towers, cranes, bottles, toddlers, you — stands by the same technicality, and everything that topples has violated it. After a module of things that *move*, this closing lesson is the physics of things that *refuse to*: the two-part contract of equilibrium, the strange fiction called the centre of gravity, and the plumb-line rule that separates leaning from lying down.

# Lore Introduction

The last station of the machine-floor is lit: the slender model tower, a crate of weighted blocks, a plumb-line on a brass reel — and the placard, which reads simply: *MAKE IT FALL. THEN EXPLAIN WHY IT TOOK YOU SO LONG.* Vex watches as you stack and lean and cantilever the blocks; the tower sways, leans alarmingly... and keeps not-falling, until one final block shifts the lean a hair further and the whole stack lets go at once. "Notice," says Vex, retrieving blocks, "that it did not fall *gradually*. It was perfectly content, and then it was rubble — nothing in between. There is a line, junior. Invisible, exact, computable. Buildings, buses, and grandmothers on icy steps all live on one side of it." He hands you the plumb-line. "Find the line. The Academy's architects have used this exact string for nine centuries; today you learn what they're checking."

# Core Learning

## Concept Introduction

**Equilibrium's two clauses.** A body is in full equilibrium only when:

1. **ΣF = 0** — resultant force zero (no acceleration; Apprentice tier's balance)
2. **ΣM = 0** — resultant moment zero about *any* point (no rotation; last lesson's currency)

Both, always. A see-saw with equal weights at unequal arms has balanced *forces* and an unbalanced *moment*: it turns. Ladders, shelves, cranes, and dams are analysed by closing both books simultaneously — choosing a clever pivot (through an unknown force's line of action) makes that force vanish from the moment ledger: the professional move.

**The centre of gravity (CG).** Gravity pulls every particle of a body, but the whole acts *as if* its entire weight concentrates at one point — the CG. Uniform symmetric objects: at the geometric centre. Composite or lopsided ones: shifted toward the mass (a hammer's CG hides up near the head). Low-tech location: hang the object from two points in turn; the CG lies where the plumb-lines cross. (It can even sit in empty space — a boomerang's or a high-jumper's arched body's CG lies outside the flesh, which is how a Fosbury flopper's body clears a bar their CG passes *under*.)

**The toppling rule.** For a body resting on a base:

- Plumb-line from CG lands **inside** the support base → the weight's own moment about the tilting edge acts to *right* it: stable, it returns.
- Plumb-line lands **outside** → the same weight now *tips* it onward: it topples. No intermediate state; the edge of the base is the cliff-edge of the verdict.

**Stability engineering** follows in two words: **low and wide**. Lower the CG (ballast, engines in the floor, sand in the bottle-base) and widen the base (outriggers, tripods, spread feet) — both increase the tilt angle needed before the plumb-line escapes. Add the dynamic version — moving bodies steering their base back under their CG (walking, cycling, cranes slewing slowly) — and you have the complete standing-up toolkit of civilisation and biology alike.

## Why It Matters

- Structural safety is this lesson with codes attached: tower cranes' counterweights and outriggers, dam cross-sections, retaining walls, and scaffold rules are all ΣF = 0, ΣM = 0, plumb-line-inside-base.
- Vehicle rollover physics (SUV design controversies, racing regulations, the bus tilt-test) is CG height versus track width, certified by exactly this geometry.
- Human balance — infancy's first triumph, old age's great hazard — is real-time toppling-rule management; physiotherapy and sports coaching both teach it explicitly.

## Worked Examples

**Example 1: The ladder, both books closed**
A 5 m ladder (200 N, CG at its middle) leans at 60° against a frictionless wall. Forces: weight down (200), wall's push horizontal (W), floor's push up (N) and friction (f). ΣF: N = 200; W = f. Moments about the floor-foot (the clever pivot — N and f vanish): wall's W × 5 sin 60° = weight's 200 × 2.5 cos 60° → W = 250/4.33 ≈ 58 N = f required. If the floor can't supply 58 N of friction (wet tiles!), no equilibrium exists: the base slides. Every window-cleaner's intuition, audited.

**Example 2: The crane's permissible load, by plumb-line**
A mobile crane (CG of machine ~2 m behind the front edge of its outrigger base) lifts a load at 10 m reach. The combined CG of machine-plus-load shifts toward the load as the load grows; the rated capacity is precisely the load that brings the combined plumb-line to the base's edge — divided by a safety factor. Overload alarms are CG calculators; tipped cranes on the news are this example with the alarm overridden.

**Example 3: The toddler and the master**
A standing toddler: high CG (proportionally huge head), narrow stance — the plumb-line lives millimetres from the base's edge, hence the satisfying frequency of sitting-down-suddenly. A martial artist's stance: knees bent (CG lowered), feet wide (base broadened), and trained responses that step the base back under any displaced CG. The same rule, two ends of mastery — and the elderly-fall-prevention industry teaches exactly the master's adjustments.

## Common Mistakes

- **Checking only force balance** — moments are half the contract; a body can be force-balanced and still rotate off its perch.
- **Thinking heavy = stable** — a heavy wardrobe with a high CG and shallow base topples readily (and tragically); geometry rules, mass only raises the stakes.
- **Placing the CG by size rather than mass** — it haunts the heavy end; a hammer balances near its head.
- **"It was leaning for ages, so it was fine"** — stability is binary at the base's edge; creeping CG shifts (soft soil, loading, snow) spend the margin invisibly until the last block.
- **Forgetting the base is the *outline* of support** — a four-legged table's base is the rectangle between feet; a one-legged stool's is a coin's worth of contact, however wide its seat.

## Mental Model

Every standing object is **a plumb-bob hanging inside an invisible fence**. The bob hangs from the centre of gravity; the fence is drawn around the support base. As long as the bob dangles anywhere inside the fence, gravity itself is the groundskeeper — every lean is charged a righting moment and ushered back. But the fence has no warnings and no buffer: let the bob cross it by a hair's breadth — one more block, one degree more tilt, one bag too many on the top deck — and the same groundskeeper switches sides instantly, charging tipping moments that grow with every degree. All of stability engineering is two jobs: hang the bob low, and build the fence wide.

## Mini Summary

- ✔ Equilibrium = ΣF = 0 AND ΣM = 0 — both books, always
- ✔ Weight acts at the centre of gravity; find it by symmetry, mass-bias, or hanging plumb-lines
- ✔ Toppling rule: CG's plumb-line inside the base → self-righting; outside → self-tipping; the edge is binary
- ✔ Stability = low CG + wide base (+ steering the base back under, for the living and the mobile)
- ✔ Clever pivot choice makes unknown forces vanish from the moment ledger

# Guided Practice Quest

Work through the guided steps to close both of a ladder's books, tilt a double-decker to its geometric verdict, and explain the wall-and-toes trap your own hips have been solving all your life.

# Solo Practice Quest

Three commissions with the plumb-line: (1) *Find three CGs*: locate the centre of gravity of a ruler, a broom (balance it on a finger — then explain why the balance point sits near the head), and one irregular flat object (cardboard cut-out, two-point hanging method, plumb-lines drawn). (2) *Measure a toppling angle*: take a cereal box or bottle, compute the tilt angle at which its CG's plumb-line reaches the base edge (from its dimensions), then tilt-test and compare. Repeat with the same container holding sand/water in the bottom third — quantify the improvement. (3) *The body audit*: try the wall-and-toes demonstration and the matching one (lifting a foot while shoulder-and-cheek against a wall); explain both failures with plumb-line language, and write three sentences on what walking actually is, stability-wise (hint: controlled falling, base perpetually re-planted). Close with one paragraph: redesign any unstable object you own.

# Integration

**Engineering**: This lesson is structural engineering's ground floor: load paths, overturning checks for walls and dams (moments about the toe), crane charts, and seismic design (where the base itself accelerates — equilibrium's hardest exam). The Pisa rescue — 70 tonnes of soil extracted, lead counterweights, the lean reduced half a degree — was a decade of applied plumb-line geometry on a beloved patient.

**Biology**: Bipedalism is the toppling rule adopted as a lifestyle: human walking is rhythmic falling caught by re-planted feet, infants spend a year learning the fence's location, and the inner ear plus pressure-sensing soles form a CG-tracking instrument cluster. Quadrupeds bought stability with a third and fourth fence-post; we spent it on free hands — and pay interest in lower-back moments and hip fractures.

# Lore Conclusion

By evening you can make the model tower lean like Pisa and stand, and fall on command with one block's nudge — and, more to the point, *call it* in advance each time, plumb-line in hand. Vex pockets the brass reel with the satisfaction of a man whose floor has done its work. "Forces, moments, centres, fences. The architects' string holds no more secrets from you." He extinguishes the machine-floor's lamps section by section, the engines of the Academy's centuries sinking into shadow, until you stand at the doors. "Module One complete, junior — motion priced, circles billed, heavens contracted, machines audited, and the standing world explained. Rest your shoulders." His dry voice carries, for the first time, something conspiratorial. "Next module belongs to Magus Hale and her Storm Tower. Charge, current, lightning in a bottle — physics you cannot see, doing work you cannot ignore. Mind her, junior: Calde forges with fire, but Hale—" he locks the great doors behind you, "—Hale keeps *weather* indoors."
