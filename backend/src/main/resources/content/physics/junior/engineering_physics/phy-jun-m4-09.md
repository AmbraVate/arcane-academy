---
id: phy-jun-m4-09
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m4
moduleTitle: "Module 4: Applied Physics"
moduleGlyph: "🔧"
moduleSortOrder: 4
topicSlug: engineering_physics
topicTitle: "Engineering Physics"
topicSortOrder: 3
title: "The Physics of Vehicles"
sortOrder: 9
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Integrate grip, drag, power, and energy into one vehicle analysis
  - Compute power demands at speed and on gradients
  - Evaluate vehicle design choices (mass, aerodynamics, drivetrain) with full-system physics
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Computes the power balance at cruise (drag + rolling resistance) and on hills (+ mgh per second)
    - Uses the grip budget (friction circle) for acceleration, braking, and cornering
    - Traces the full energy chain including regenerative options
    - Argues one design trade (mass vs efficiency, aero vs practicality) with numbers
  keywords: [drag, rolling resistance, grip, power, gradient, mgh, friction circle, regenerative]
  modelAnswer: |
    A vehicle is the course in motion: grip (friction at four palm-sized contact patches) buys
    every acceleration, braking, and cornering force; the power bill at cruise is drag (rising
    ~v²) plus rolling resistance, so high speed is quadratically expensive; hills add mgh per
    second — a 1,500 kg car climbing a 5% grade at 20 m/s pays an extra 15 kW just for
    altitude. The grip budget is shared: demanding full braking AND full cornering at once
    overdraws the friction circle, which is why braking happens before bends. Mass taxes
    acceleration, climbing, and rolling resistance but not aerodynamic drag; streamlining
    taxes nothing but design effort. Electric drivetrains close the ledger by regenerating
    braking energy that engines must burn as heat.
guidedSteps:
  - id: phy-jun-m4-09-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A car climbing a hill gains 1 m of height per second (mass 1,500 kg, g = 10). The EXTRA power demanded by the climb alone: P = mgh per second = ________ kW.
    inputConfig:
      placeholder: "15"
    markingRule:
      matchMode: CONTAINS
      accepted: ["15"]
      rejectedFeedback: "P = mgΔh/Δt = 1500 × 10 × 1 = 15,000 W = 15 kW — banked as potential energy (your Apprentice account), recoverable on descent only by a drivetrain that can bank it (regen) rather than burn it (brakes)."
    hint: "Energy per second = weight × height gained per second."
    reflectionPrompt: "Where does that 15 kW go on the way DOWN, in a petrol car versus an EV?"
  - id: phy-jun-m4-09-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Racing drivers brake BEFORE a bend, not through it, because:
    inputConfig:
      options:
        - "Tradition and superstition"
        - "Grip is one shared budget: the tyres' total friction must cover braking AND cornering demands; spending it all on one leaves nothing for the other (the friction circle)"
        - "Brakes don't work while turning"
        - "It saves fuel"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Grip is one shared budget: the tyres' total friction must cover braking AND cornering demands; spending it all on one leaves nothing for the other (the friction circle)"]
      rejectedFeedback: "Each tyre's contact patch supplies a maximum total force in ANY direction — the friction circle. Full braking uses the whole budget longitudinally; demand cornering simultaneously and the sum exceeds the circle: the tyre lets go (understeer/lockup). Brake in a straight line, release, then spend the budget on the turn — trail-braking is the artful overlap at the circle's edge."
    hint: "One budget, two demands. What happens when their vector sum exceeds the tyre's maximum?"
    reflectionPrompt: "How does this same budget explain why accelerating hard OUT of a wet corner spins the wheels?"
  - id: phy-jun-m4-09-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A car needs ~15 kW to cruise at 90 km/h but ~40 kW at 130 km/h. Explain the disproportion, and compute the energy cost per 100 km at each speed (divide power by speed). Which design lever attacks this bill? (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [drag, v², quadratic, aero, streamlin, "60", "111", per 100]
      rejectedFeedback: "Drag grows ~v² (and drag POWER ~v³): 130/90 = 1.44, so drag force ~2.1× and its power ~3× — the bill races ahead of the speed. Energy per 100 km: 15 kW ÷ 25 m/s = 600 J/m = 60 MJ/100 km; 40 kW ÷ 36 m/s ≈ 1,110 J/m ≈ 111 MJ/100 km — nearly double per kilometre. The lever is aerodynamics (drag area and shape), since mass doesn't appear in the aero term at all."
    hint: "Force ~v² means power ~v³; energy per km is force, effectively."
    reflectionPrompt: "Why do trucks gain so much from trailer skirts and gap fairings while city buses gain little?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which resistance does NOT depend on vehicle mass?"
    options:
      - "Rolling resistance"
      - "Hill climbing (mgh)"
      - "Aerodynamic drag — it depends on shape, frontal area, and v², not mass"
      - "Acceleration demand (ma)"
    correctIndex: 2
    feedback: "Drag is the air's argument with your SHAPE and SPEED; mass is irrelevant to it. Hence the design split: city vehicles (stop-start = ma and rolling) fight mass; motorway cruisers fight shape. EVs' regen partially refunds the mass taxes — never the aero one."
  - type: MULTIPLE_CHOICE
    question: "A vehicle's entire ability to accelerate, brake, and corner is ultimately limited by:"
    options:
      - "Engine power alone"
      - "The friction available at its tyre contact patches — four areas roughly the size of your palms"
      - "The driver's strength"
      - "The horn"
    correctIndex: 1
    feedback: "Every demand routes through those palm-prints (your third-law walking lesson, at speed). More power than grip just makes smoke; better brakes than grip just makes flat spots. Tyre engineering is the quiet monarch of vehicle dynamics."
---

# Hook

Everything your two-tonne car can do — every launch, every emergency stop, every motorway bend — is transmitted through four patches of rubber, each about the size of your palm. Not the engine's heroics, not the brake discs' bite: those only *request*; the contact patches *grant*, and their grant is a strict budget that bends, brakes, and wet Tuesdays all draw against. Meanwhile the fuel gauge keeps a different set of books: a bill that creeps at city speeds and *races* on the motorway, because the air's objection to your shape grows with the square of your hurry — and a third ledger opens on every hill, where altitude is banked at mgh and refunded, in most cars, as brake heat.

The physics of vehicles is your whole curriculum convened: forces and energy (Apprentice), momentum and circles (Junior mechanics), drag (yesterday), transmission (this week). Today the Mechanica's test-cart rolls, and you keep all three books at once.

# Lore Introduction

The instrumented test-cart waits in the yard — spring balances at its tow-hitch, a drop-weight power source, swappable wheels (slick, grooved, iron-rimmed), clip-on fairings, and ballast pigs — and beside it Vex has chalked three columns on the yard's slate: GRIP, BILL, BANK. "The rotation's last apparatus, junior, and its first honest vehicle. Every cart, wagon, and steam-omnibus in the realm answers to these three columns." He loads a ballast pig. "Grip: what the road grants — measure it, never assume it. Bill: what motion costs per yard — the air and the axles collect. Bank: what the hills hold on deposit." He hands you the tow-balance. "By dusk I want the cart's full accounts: its grip budget on dry stone and on the wet flags by the race; its drag bill at a walk and at a run; and the cost of the yard's ramp, banked and — unless you can show me otherwise — burned. The wagon-masters' guild examines on exactly this. So does the road."

# Core Learning

## Concept Introduction

**Book one — GRIP: the contact-patch budget.** All horizontal vehicle forces (drive, brake, cornering) are friction at the tyres — maximum roughly μ × weight-on-wheel (dry tarmac μ ≈ 0.9; wet ≈ 0.5; ice ≈ 0.1). The budget is *shared and vectorial* — the **friction circle**: longitudinal (brake/drive) and lateral (cornering) demands add as vectors and must fit inside the circle. Hence: brake before bends; gentle throttle out of wet corners; and the electronic guardians (ABS, traction and stability control) exist purely to keep demands inside the circle when humans overdraw. More engine than grip is theatre; tyre choice and condition are the real sovereigns.

**Book two — BILL: the cost of constant speed.**

```
P_cruise = (drag + rolling resistance) × v
drag ≈ ½ ρ Cd A v²  (shape's objection — quadratic in speed, blind to mass)
rolling ≈ Crr × m g  (tyre flexing — proportional to weight, near-flat with speed)
```

Crossover around 50–70 km/h: below, rolling and stop-start dominate (city); above, aero owns the bill (motorway), and since drag *power* grows ~v³, the 90→130 km/h jump near-doubles energy per kilometre. Design levers split cleanly: **mass** taxes rolling, climbing, and acceleration (city, trucks, anything stop-start); **shape** (Cd × A) taxes cruising (motorway, records, range anxiety).

**Book three — BANK: hills and stops as energy accounts.** Climbing banks mgh (15 kW for 1,500 kg at 1 m/s of rise); every stop must dispose of ½mv². Conventional brakes burn both as heat (your mountain mcΔT lesson); **regenerative braking** runs the motor as generator (the Tower's reversible machine) and refunds the mass-taxes to the battery — the deep reason EVs excel in cities and mountains, and why no regen can ever refund the *aero* bill (that energy left with the swirling air).

**The synthesis audit** — any vehicle, any journey: grip budget vs demands; cruise bill by speed regime; bank deposits and their fate. Three columns, whole curriculum.

## Why It Matters

- Road safety is books one and three: stopping distances (grip × reaction), the v² severity law, and why winter tyres outrank winter prayers.
- Efficiency policy is book two: speed limits as energy policy, truck aero regulation, the EV transition's city-vs-motorway arithmetic.
- The three-book audit is transferable to every mover — bicycles, trains (steel-on-steel: tiny rolling, tiny grip), ships (all drag), aircraft (drag vs *induced* drag — Senior fare).

## Worked Examples

**Example 1: The emergency stop, fully booked**
1,500 kg at 30 m/s, dry road (μ = 0.9): max braking force = 0.9 × 15,000 = 13.5 kN → a = 9 m/s² → stops in 50 m (plus reaction's 30 m). Wet (μ = 0.5): 90 m braking. The energy disposed: ½ × 1500 × 900 = 675 kJ into four discs — about 160 °C of temperature rise in 20 kg of iron (mcΔT, again). ABS's whole job: hold each wheel at the circle's edge instead of past it (a sliding tyre's μ drops AND steering dies).

**Example 2: Train versus truck — the rolling-resistance verdict**
Steel wheel on steel rail: Crr ≈ 0.001; truck tyre on tarmac: ≈ 0.007. Per tonne moved, the train's rolling bill is ~7× smaller (and its drag is shared along one long shape) — the physics under every freight-modal-shift argument. The price: steel-on-steel grip is also tiny (μ ≈ 0.15–0.3), so trains brake over kilometres and climb only gentle grades — books one and two trading against each other, visible from any platform.

**Example 3: The EV's city paradox**
A petrol car: ~30% efficient, and every stop burns its ½mv² in the discs — city economy is dreadful. An EV: ~85% motor efficiency AND regen refunds most stop-energy — city range often *exceeds* motorway range (where aero, unrefundable, reigns and v³ collects). Same physics, opposite optima: read any vehicle's natural habitat straight from the three books.

## Common Mistakes

- **Power worship** — acceleration is grip-limited long before engine-limited in most road cars; the patches grant, the engine only asks.
- **Halving speed, halving the bill** — the aero term is quadratic (energy/km) and cubic (power): speed is the most expensive habit per unit saved.
- **Mass blamed for everything** — mass is innocent of aerodynamic drag; a heavy streamliner out-cruises a light brick (then loses in town: regime matters).
- **Treating brakes as energy destroyers by necessity** — they're heat converters by *default*; regen makes the bank refundable wherever a battery waits.
- **One μ for all weather** — the grip budget is a live variable (wet halves it, ice decimates it) while reaction times and v² stay constant: margins must move with the sky.

## Mental Model

A vehicle in motion is **a small business with three sets of books, audited continuously by the road**. The *grip ledger* is its credit line — four palm-sized guarantors underwrite every manoeuvre, jointly, to a strict ceiling that rain renegotiates downward without notice; overdraw and the business skids into receivership mid-corner. The *operating bill* is the cost of staying open at speed — a modest fixed rent (rolling, by weight) plus a marketing spend on pushing through the crowd of air that grows with the *square* of ambition. The *capital account* banks every hill and every burst of speed — assets recoverable in full only by firms (EVs) whose machinery runs backwards; all others write them off as brake-heat the moment the lights turn red. Good driving, and good vehicle design, is just honest triple-entry bookkeeping at 90 kilometres per hour.

## Mini Summary

- ✔ Grip: μ × weight at four palm-prints, shared vectorially (friction circle) — brake before bends; weather rewrites the budget
- ✔ Bill: rolling (∝ mass, flat with speed) + drag (∝ v², blind to mass); power ~v³ — speed is quadratically expensive per km
- ✔ Bank: hills deposit mgh, stops hold ½mv² — brakes burn it, regen refunds it (never the aero spend)
- ✔ Design by habitat: city fights mass (and refunds it), motorway fights shape
- ✔ Trains: tiny rolling, tiny grip — both books traded; every mover audits the same three

# Guided Practice Quest

Work through the guided steps to bill a hill at fifteen kilowatts, defend the friction circle at the bend's entrance, and let v³ explain the motorway's appetite.

# Solo Practice Quest

The wagon-masters' examination, three parts: (1) *Audit a real vehicle*: for any car you know (or look up: mass, rated power, fuel/energy economy at two speeds), produce its three books — estimated grip budget dry/wet, cruise bills at 50 and 110 km/h (work back from economy figures), and the bank account for a local hill plus a 0–50 km/h stop. (2) *The cart trial at home*: with a bicycle, skateboard, or toy cart, measure coast-down distances from a fixed speed on flat ground (rolling+drag bill) and the push needed to skid a loaded wheel (grip); report with uncertainties. (3) *Design brief*: specify the physics priorities (mass? Cd×A? regen? tyres?) for THREE vehicles — a city delivery van, a motorway coach, a mountain shuttle bus — one paragraph each, books cited. Close with the two-sentence safety memo you'd post by every wet roundabout, friction circle included.

# Integration

**Engineering**: Vehicle dynamics professionalises today's books: tyre models (the friction circle measured into ellipses and load-sensitivity), suspension keeping patches pressed and level (your Hooke + damping), aero development in wind tunnels and CFD (Cd budgets fought in counts of 0.001), and the electronic court of ABS/ESC arbitrating the circle a hundred times a second. Motorsport is the same syllabus with the margins shaved to zero.

**Mathematics**: The three books are three function families — constant (rolling), quadratic (drag force), and the v³ power law — and reading their crossover is curve-comparison literacy. The friction circle is vector addition with a constraint (a disc in force-space); optimisation under it (the racing line, trail-braking) is your first taste of constrained optimisation, solved daily by anyone who corners well.

# Lore Conclusion

Dusk finds the yard slate full: the cart's grip measured dry and wet (the wet flags' budget barely half, as the guild's manuals warn), the drag bill rising unmistakably faster than the run-speed, the ramp's deposit computed — and, in your final flourish, partially *refunded*, by rigging the cart's drop-weight to rewind on the descent: a crude regeneration that makes Vex stop mid-stride. He examines the rig in silence, then chalks your name on the slate's corner — where, you now notice, a century of names precede you. "The wagon-masters would take you. Decline them; the rotation isn't finished." He sheets the cart against the night. "You can make the world move and stop and stand and pay its bills, junior. One art remains — the one beneath all of them, the one Thorne started you on with a brass rod a tier ago: *knowing what is actually so*. Sensors, instruments, calibration, the automated watch. Tomorrow we teach the Mechanica to read its own dials — and then the Gauntlet takes you."
