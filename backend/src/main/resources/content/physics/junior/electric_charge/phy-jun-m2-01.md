---
id: phy-jun-m2-01
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m2
moduleTitle: "Module 2: Electricity and Magnetism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: electric_charge
topicTitle: "Electric Charge"
topicSortOrder: 1
title: "Charge and Static Electricity"
sortOrder: 1
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Describe the two kinds of charge and their interactions
  - Explain charging by friction as electron transfer
  - Explain static phenomena — sparks, clinging clothes, lightning — from charge separation
integrationDomains: [chemistry, earth_science]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States like charges repel, unlike attract, with charge in coulombs
    - Explains friction-charging as electron transfer (never creation)
    - Explains attraction of neutral objects via induced charge separation
    - Connects sparks and lightning to sudden discharge of separated charge
  keywords: [positive, negative, electron, transfer, repel, attract, induction, discharge, coulomb]
  modelAnswer: |
    Charge comes in two kinds: positive (protons) and negative (electrons); like charges repel,
    unlike attract, and charge is measured in coulombs. Rubbing transfers electrons — a balloon
    on hair strips electrons onto the balloon, leaving hair positive and balloon negative;
    charge is never created, only separated. Charged objects attract neutral ones by induction:
    the balloon's electrons push the wall's electrons slightly away, leaving nearer positive
    charge to attract. A spark is separated charge violently rejoining through ionised air —
    door-handle zaps at hundreds of volts, lightning at hundreds of millions, the same
    phenomenon at two scales.
guidedSteps:
  - id: phy-jun-m2-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A balloon rubbed on hair sticks to a neutral wall. The best explanation:
    inputConfig:
      options:
        - "The balloon's charge attracts the wall's opposite charge that was always there"
        - "The balloon's negative charge repels electrons in the wall's surface slightly away, inducing a nearer positive layer that attracts the balloon"
        - "Rubbing makes the balloon sticky"
        - "The wall becomes magnetised"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The balloon's negative charge repels electrons in the wall's surface slightly away, inducing a nearer positive layer that attracts the balloon"]
      rejectedFeedback: "Induction: the neutral wall's charges rearrange — electrons nudged away, leaving the closer surface relatively positive. Attraction to NEUTRAL objects is static electricity's signature trick, and induced separation is the mechanism."
    hint: "The wall is neutral overall. What can the balloon's charge do to the wall's internal charges?"
    reflectionPrompt: "Why does the balloon eventually fall — where does its charge go?"
  - id: phy-jun-m2-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Charging by friction transfers ________ from one surface to the other; the charge total of the pair is unchanged.
    inputConfig:
      placeholder: "electrons"
    markingRule:
      matchMode: CONTAINS
      accepted: [electrons]
      rejectedFeedback: "Electrons — light, outer, and loosely held — are the movers. Protons stay locked in nuclei. Friction separates charge; it never mints any: conservation of charge is as strict as conservation of energy."
    hint: "Which particle is light and on the atom's outside?"
    reflectionPrompt: "If the cloth gains electrons, what charge is the rod left carrying, and why exactly equal?"
  - id: phy-jun-m2-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Trace lightning's story in charge language: how does a storm cloud become charged, what builds between cloud and ground, and what IS the strike? (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [separat, collision, ice, negative, induced, ground, discharge, ionis, spark]
      rejectedFeedback: "Colliding ice and hail inside the cloud transfer electrons, separating charge — typically negative cloud-base, positive top. The negative base induces positive charge on the ground beneath. When the separation's pull exceeds air's insulation (~3 million V/m), air ionises into a conducting channel and the stored charge rejoins violently: the strike is a giant spark — your door-handle zap, scaled a millionfold."
    hint: "Friction-charging (ice collisions) → induction (on the ground) → breakdown (the spark)."
    reflectionPrompt: "Why do lightning rods end in sharp points, and why connect them to the ground?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Two charged rods repel each other. You can conclude:"
    options:
      - "Both are negative"
      - "Both are positive"
      - "They carry the same kind of charge — which kind needs another test"
      - "One is charged, one neutral"
    correctIndex: 2
    feedback: "Repulsion is the definitive test FOR like charges (attraction can be unlike charges OR induction on a neutral body) — but it cannot tell you which kind without a known reference."
  - type: MULTIPLE_CHOICE
    question: "Charge is conserved means:"
    options:
      - "Charges always stay where they are"
      - "Charge can be separated and moved but never created or destroyed — totals always balance"
      - "Only positive charge exists"
      - "Rubbing creates new electrons"
    correctIndex: 1
    feedback: "Every negative charge gained somewhere is a negative charge lost elsewhere. Friction, sparks, and circuits all reshuffle a fixed ledger."
---

# Hook

Shuffle across a carpet in dry winter air and reach for a door handle: *snap* — a spark leaps, briefly carrying several thousand volts. You have just performed, at knuckle scale, the same physics that builds thunderstorms: charge, separated by rubbing, storing energy in the separation, then rejoining the moment a path appears — violently.

Electricity's entire story begins here, with a fact hiding inside every atom you're made of: matter is stuffed with positive and negative charge in exquisite balance — about 10²⁸ electrons' worth in your body, matched proton for electron so perfectly you feel nothing. All of static electricity — clinging socks, crackling jumpers, lightning splitting the sky — is what happens when that balance is disturbed by even one part in a trillion. This module is the physics of disturbing it *on purpose*.

# Lore Introduction

The Storm Tower stands apart from the Academy's other halls, copper-roofed and rod-crowned, and Magus Hale receives you in a circular workroom that smells faintly of thunderstorms. She is brisk, bright-eyed, grey hair escaping its pins as if charged — which, you realise as she hands you a rod of amber and a square of fur, it may be. "The old word for amber is *elektron*," she says. "The Greeks knew this trick three thousand years ago and called it a curiosity. We now know it holds up the universe." She rubs the amber briskly and holds it over a dish of paper scraps: they leap upward like living things. Then she touches the rod to the Tower's great brass sphere, turns a crank — and your hair, gently and absurdly, begins to rise from your scalp. "Two kinds of charge, junior. One ancient trick to separate them. And everything in this Tower — every spark, motor, and bolt of indoor weather — follows from asking what the separated kinds *want*. Which is, simply: each other."

# Core Learning

## Concept Introduction

**Two charges, one rule.** Matter's charge comes in two kinds — **positive** (protons, locked in atomic nuclei) and **negative** (electrons, light and outer). The interaction law:

- **Like charges repel; unlike charges attract** — with force growing as charges grow and falling with distance (inverse-square again; the formal law is Coulomb's, gravitation's electric twin, with one upgrade: *two signs*, so repulsion exists).

Charge is measured in **coulombs (C)**; one electron carries a minuscule −1.6 × 10⁻¹⁹ C, so everyday static involves nano-to-microcoulombs — yet the forces are vivid because electric attraction is *titanically* stronger than gravity (≈10³⁹ times, charge for mass): atoms are held together by it, and your floor holds you up with it (the "contact" forces of Apprentice tier were electricity all along).

**Charging = separating, never creating.** Rub two different materials and **electrons transfer** from one surface to the other (which way depends on the materials — fur donates to amber; hair to balloons). Results: one body negative (electron surplus), the other equally positive (deficit). **Charge is conserved** as strictly as energy: every ledger balances.

**The neutral-attraction trick: induction.** A charged balloon attracts a *neutral* wall because its field rearranges the wall's charges — electrons nudged away (if balloon is negative), leaving the nearer surface relatively positive. Nearer attraction beats farther repulsion (inverse square!): net pull. The same induction lifts paper scraps, makes dust ambush screens, and primes the ground under a thundercloud.

**Discharge: the rejoining.** Separated charge stores energy (you did work separating it against attraction). Provide a path — or exceed air's insulating limit (~3 MV/m), ionising a channel — and the charge rejoins as a **spark**: door-handle zaps (kV, harmless nanocoulombs), fuel-depot hazards, and lightning (hundreds of MV, tens of coulombs — same physics, fatal scale).

## Why It Matters

- This is the foundation stone of the entire module: current (next lessons) is charge *flowing*; voltage is the energy of separation; circuits are charge-rejoining, civilised.
- Static management is real engineering: fuel trucks ground themselves, electronics factories ban nylon, grain silos explode from sparks, photocopiers and air filters *exploit* induction.
- Lightning kills; understanding its charge story (and the rod, Franklin's gift) is literally protective knowledge.

## Worked Examples

**Example 1: The winter doorknob, audited**
Carpet shuffle: shoe soles and carpet exchange electrons; your body banks perhaps 10–50 nanocoulombs at 5,000–25,000 V (high voltage, tiny charge — energy in single millijoules: startling, not harmful). Reach for the handle: the field at your approaching knuckle exceeds air's 3 MV/m over the last millimetre, the air ionises, and the ledger settles in one bright crack. Touch a key to the handle first and the spark leaps from metal, not nerve-endings — same physics, outsourced pain.

**Example 2: Why dusting attracts dust**
Wiping a screen rubs it charged; the charged screen then *induces* separation in every passing neutral dust mote and pulls it in — the cleaning act recruits the dirt. Anti-static cloths and sprays work by making surfaces slightly conductive, letting separated charge drain away before it can hunt.

**Example 3: The lightning rod's two services**
Franklin's rod, sharp-tipped and thick-wired to buried earth, serves twice: routinely, its point quietly leaks charge (strong fields at sharp points ionise air gently), bleeding the local separation down; and when a strike comes anyway, it offers a low-resistance path so the tens of coulombs pass through copper to ground rather than through masonry (whose moisture explodes into steam) or people. It does not "attract" lightning so much as *manage* the inevitable.

## Common Mistakes

- **"Rubbing creates charge"** — it transfers electrons; the pair's total is forever zero. Conservation of charge has no known exceptions.
- **Thinking protons move in static phenomena** — nuclei are locked in place; electrons do essentially all everyday charge transport.
- **"Attraction proves opposite charges"** — neutral bodies are attracted too (induction); only *repulsion* is conclusive (proves like charges).
- **Fearing voltage alone** — your carpet-spark is 10,000+ V and harmless: tiny charge, tiny energy. Danger needs charge/current sustained (mains, lightning). Respect the full ledger, not one number.
- **"Static is a different electricity from current"** — same electrons, same laws; static is the *separated, waiting* phase; current (next) is the flow.

## Mental Model

Think of charge as **two populations in an ancient arranged marriage — perfectly paired, profoundly bound**. Every atom is a wedded household: positives settled in the nucleus-keep, negatives orbiting in attendance. Friction is a clumsy abduction: shuffle two materials and some electrons are carried off to the other side, leaving lonely positives behind. Now the drama is wound: separated partners pull toward each other across any distance (and will even *lean* the charges inside neutral bystanders — induction — to get nearer). A spark is the reunion: sudden, bright, all the separation-energy spent in a crack. The Storm Tower's whole curriculum, and every wire in your home, is this reunion — slowed, guided, and put to work.

## Mini Summary

- ✔ Two charges: positive (protons, fixed) and negative (electrons, mobile); like repel, unlike attract
- ✔ Friction transfers electrons — charge is separated, never created (conservation of charge)
- ✔ Charged bodies attract neutrals by induced separation (nearer-unlike beats farther-like)
- ✔ Separation stores energy; sparks and lightning are the violent rejoining through ionised air
- ✔ Static and current are one physics: waiting versus flowing

# Guided Practice Quest

Work through the guided steps to stick a balloon to a wall by rearranging the wall, keep the friction ledger balanced to the electron, and scale your door-handle zap up to the sky.

# Solo Practice Quest

Run the Storm Tower's entrance experiments at home (dry day or heated room — humidity is static's enemy): (1) *The amber rite*: charge a balloon/comb/plastic ruler on hair or wool and document three induction conquests (paper scraps, a thin stream of tap water bending — try it!, a wall). For the water: explain in writing which way the water's charges leaned. (2) *Repulsion, the true test*: charge two balloons identically, hang them on threads, and photograph/describe their mutual verdict; explain why this proves like charge where sticking-to-walls proved nothing. (3) *The audit*: list every static encounter of your week (clingy laundry, car-door zaps, crackling jumper) and for each name: what rubbed, who took the electrons (guess), where the energy went on discharge. Close with two sentences on why petrol stations post static warnings and refuelling aircraft are bonded by cable to the tanker.

# Integration

**Chemistry**: Why do materials charge differently? Atoms differ in electron-grip (electronegativity) — chemistry's whole drama of bonding is charge's attraction formalised: ionic bonds are electrostatic capture, covalent bonds shared custody. The triboelectric series (which materials rob which) is a chemistry table physics borrowed.

**Earth Science**: A thunderstorm is a charge-separation engine running on convection (your weather lesson) — updraughts hurling ice past hail, electrons hopping in each collision, kilometre-scale separation banking gigajoules. Earth's whole surface, meanwhile, carries a permanent faint negative charge balanced by the atmosphere — a planetary circuit in which every fair-weather field and storm participates.

# Lore Conclusion

By dusk you can lift paper without touching it, bend a stream of water with a comb, and — Hale's entrance exam — explain each conquest in the language of leaning charges, to her satisfaction and your hair's continued elevation. She discharges the great sphere with a contact rod (*crack* — you flinch; she doesn't) and regards you through the after-tingle. "Separation, attraction, reunion. The Greeks had all three and made amulets." Her eyes glitter. "We will make *rivers*. Tomorrow you learn which materials let the separated charge run home — and which dam it; whose hands the river may safely cross and whose it must never. Conductors and insulators, junior: the difference between a wire and a wick — and, on the wrong day, between a fright and a funeral."
