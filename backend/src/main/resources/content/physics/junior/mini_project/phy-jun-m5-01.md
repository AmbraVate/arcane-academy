---
id: phy-jun-m5-01
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m5
moduleTitle: "Module 5: Junior Project"
moduleGlyph: "⚙️"
moduleSortOrder: 5
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
title: "The Mechanist's Gauntlet"
sortOrder: 1
xpReward: 300
practiceType: NONE
questType: SYNTHESIS
learningObjectives:
  - Design, build, and characterise an energy-converting machine end to end
  - Apply calibrated measurement and logging to your own apparatus
  - Close the books — energy chain, efficiency, and limits — and defend them
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - A working machine converts stored energy to useful motion or work (built, not described)
    - The full energy chain is traced with measured or honestly-estimated values at each stage
    - Overall efficiency is computed from measurements, with the largest loss located
    - At least one instrument was calibrated before use, with its correction applied
    - Measurements use fair-test discipline — repeats, uncertainties, one variable at a time
    - A design iteration is documented — change made, prediction, measured result
    - The machine's limiting physics (grip? friction? strength? power?) is identified and argued
    - The report could be rebuilt and re-audited by a stranger
  keywords: [machine, energy chain, efficiency, calibrate, iterate, losses, characterise, defend]
  modelAnswer: |
    A complete Gauntlet entry builds a real energy-converting machine — a launcher, vehicle,
    crane, or turbine — and treats it as both artefact and experiment. The energy chain is
    traced with numbers: stored (½kx², mgh, or a battery's QV) through transmission to useful
    output, with each stage's loss located and the overall efficiency computed from
    measurement, not hope. Instruments are calibrated first and corrections applied; tests
    follow fair-test law — one variable, repeats, uncertainties quoted. At least one design
    iteration runs the full loop: hypothesis, change, prediction, measurement, verdict. The
    report identifies the machine's true limiting physics and defends every claim with data
    a stranger could re-audit.
guidedSteps:
  - id: phy-jun-m5-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Choosing your Gauntlet machine, the WISEST selection criterion is:
    inputConfig:
      options:
        - "The most spectacular possible build"
        - "A machine whose energy chain you can MEASURE at multiple stages with instruments you can calibrate — characterisation is the project; the build is its vehicle"
        - "Whatever uses the most materials"
        - "The fastest thing buildable"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A machine whose energy chain you can MEASURE at multiple stages with instruments you can calibrate — characterisation is the project; the build is its vehicle"]
      rejectedFeedback: "The Gauntlet examines the mechanist, not the machine. A humble rubber-band car fully audited — stored ½kx² measured, losses located, efficiency computed, iteration tested — outscores a spectacular catapult with no books. Choose for measurability."
    hint: "What will the masters actually examine — the artefact, or its accounts?"
    reflectionPrompt: "Which stages of YOUR candidate machine's chain can you genuinely measure, and which only estimate?"
  - id: phy-jun-m5-01-g2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Write your machine's PREDICTED energy chain before building: source store and its formula, each conversion stage, where you expect the largest loss, and your target efficiency. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [stored, chain, loss, efficiency, predict, friction, convert]
      rejectedFeedback: "A worthy prediction names the store (e.g. twisted rubber's elastic energy ≈ measured by force-distance loading), the stages (band → axle → wheels → kinetic + rolling losses), the expected chief thief (likely bearing/road friction), and a falsifiable efficiency target ('I predict 20–40% of stored energy becomes kinetic energy at launch'). You will grade this prediction against measurement — that's the point."
    hint: "Store (with formula) → stages → predicted chief loss → numerical efficiency target."
    reflectionPrompt: "What measurement at launch would let you compute the kinetic energy actually delivered?"
  - id: phy-jun-m5-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Plan your iteration: name ONE design change you might make after first tests, the physics it exploits, and the single measured number that will judge it. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [change, predict, measure, compare, friction, ratio, mass, bearing]
      rejectedFeedback: "Iteration is hypothesis-driven engineering: 'Lubricating the axle (cutting friction's toll) should raise distance-per-stored-joule by ~15%; verdict by the mean of five runs before and after.' One change, one mechanism, one number — the fair-test law applied to your own design."
    hint: "One change, the lesson it invokes, one number for the verdict."
    reflectionPrompt: "Why must you change only ONE thing between test campaigns — even on your own invention?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Your machine's measured efficiency is 23%. The Gauntlet's next required move is:"
    options:
      - "Round it up to 50% for the report"
      - "LOCATE the missing 77% — measure or argue each loss's share (friction here, drag there, heat in the band) until the books balance to honest estimates"
      - "Declare the machine broken"
      - "Hide the number"
    correctIndex: 1
    feedback: "Efficiency without a loss map is half an audit. The Bursar's law: every joule accounted, to named stores. The hunt for the 77% is usually where the design insight (and the iteration) is found."
  - type: MULTIPLE_CHOICE
    question: "Why must instruments be calibrated BEFORE characterising the machine?"
    options:
      - "Tradition"
      - "Every downstream number inherits the instruments' errors — an uncalibrated scale or timer silently corrupts the entire energy audit (and the logger automates the corruption)"
      - "Calibration makes instruments faster"
      - "It is optional for home projects"
    correctIndex: 1
    feedback: "The chain of trust runs: references → instruments → measurements → conclusions. Yesterday's correction cards are the project's foundation — drift and bias propagate into every joule you claim."
---

# Hook

Every engineer remembers their first machine that *worked* — and every good engineer remembers the harder milestone: the first machine they could *account for*. Anyone can twist a rubber band and watch a cart skitter. The mechanist's question is the auditor's: how many joules went in? Where did each one go? What fraction arrived as motion, what did friction skim at the axle, what did drag take per metre — and if I change one thing, can I *predict* the books' response before measuring it?

The Mechanist's Gauntlet is the Junior tier's consolidation: one machine, built by you, characterised to the standards of every master who taught you — Vex's ratios and load paths, Hale's energy conversions, Calde's two regulators and their ledgers, the Bursar's rivers, and the measurement discipline running from Thorne's brass rod to yesterday's logger. No new physics. That's the point. This time, the whole curriculum reports to *you*.

# Lore Introduction

The summons bears four seals this time — Thorne's, Hale's, Calde's, Vex's — and the Mechanica's main floor has been cleared to a single bench under the gallery where, you now understand, the masters will sit. On the bench: nothing. "The Apprentice Trials gave you apparatus," says Vex, as the four take their places. "The Gauntlet gives you a commission and a deadline." Thorne, formal: "Build a machine that stores energy and spends it on motion or work." Hale, glittering: "Instrument it — calibrated captives, honest logs." Calde, arms folded: "Audit it — every joule to its grave, both regulators satisfied." And Vex: "Then improve it — one change, predicted, measured, judged — and defend the whole before this gallery." He sets on the empty bench a single object: the brass 1 kg standard from the vault, on loan, gleaming. "Your reference, junior. Everything you claim will trace to it, or to nature's own. Three days. The floor is yours."

# Core Learning

## Concept Introduction

**The commission.** Design, build, characterise, and iterate one **energy-converting machine**. Proven candidates (choose for *measurability*):

- **Rubber-band or spring vehicle** — ½kx² (or band's measured force-extension area) → kinetic energy → rolling/drag losses
- **Gravity machine** — falling-mass crane, trebuchet, or marble run: mgh → useful work/launch → losses
- **Water or wind turbine** — flow's kinetic/potential energy → shaft work (Hale's wheel, desktop scale)
- **Electromagnet crane or motor cart** — battery's QV → field/motion → the Tower's books

**The Gauntlet's five trials** (the rubric in working order):

1. **Calibrate first** — correction cards for every instrument (scale, timer, ruler, multimeter) against references; the brass standard's law: claims trace upward.
2. **Measure the store** — k from a force-extension plot (area = energy for non-Hookean bands); mgh by scale and rule; QV by ratings or measurement.
3. **Characterise the chain** — measure intermediate and output stages: launch speed (timed gates, video frames, or range-and-projectile arithmetic), work done (force × distance at the hook), losses by difference and by direct test (coast-downs for rolling resistance — your vehicle lesson's method).
4. **Close the books** — efficiency = useful/stored, *and* the loss map: every missing joule assigned a named store with measurement or honest estimate. Sankey it (the Bursar watches).
5. **Iterate by hypothesis** — one change (lubrication, gear ratio, mass, wheel diameter, fairing), mechanism named, prediction stated, five-run means before and after, verdict delivered. Fair-test law on your own invention.

**The defence** — the masters' four questions, predictable as sunrise: *Where did the rest of the energy go?* (loss map). *Why should we trust that number?* (calibration, uncertainty). *What limits this machine — really?* (grip? friction? band's elastic limit? buckling spars? the limiting-physics argument). *What would you do with three more days?* (the engineer's horizon).

## Why It Matters

- This is the working format of real engineering: prototype → instrument → characterise → iterate — the loop that builds everything from drones to turbines.
- The project converts your four modules from lessons into *capabilities*: the proof is a machine whose books balance.
- Senior tier assumes this competence: its computational projects will simulate what you here measure, and its labs will demand exactly this audit discipline at higher precision.

## Worked Examples

**Example 1: A band-car's books, done well (a previous entry, archived)**
Band characterised by loading plot: stored at full wind, 4.2 ± 0.3 J (area under curve — the band is honestly non-Hookean and the report says so). Launch speed by timing gates: 1.9 m/s with 0.31 kg → KE = 0.56 J. Efficiency: 13%. Loss map: axle friction (coast-down test: ~45%), wheel-slip at launch (skid marks, ~20% — grip budget exceeded!), band hysteresis (re-measured unload curve: ~15%), unaccounted (~7%, declared). Iteration: launch-gearing change to keep wheels inside the grip budget — predicted +30% range; measured +24 ± 6%. Verdict: hypothesis supported. The gallery's note: *"Books balanced; wheels-slip diagnosis exemplary."*

**Example 2: The trap of the spectacular (archived as warning)**
A magnificent trebuchet, two days of carpentry: range impressive, books empty — counterweight's mgh never measured (no scale could take it), projectile speed estimated by adjective. The gallery's note survives in the manual's margins: *"A fine machine and no project. The Gauntlet examines accounts, not splinters."* Its author rebuilt at desktop scale and passed with distinction; both entries are kept, deliberately, side by side.

**Example 3: The logger's cameo**
Strongest entries put yesterday's lesson to work: a phone's slow-motion camera as timing gates (frame-rate = known clock — calibrate against a stopwatch!); accelerometer logs of a cart's run revealing the launch spike, cruise decay, and collision artefact — four citizens, named in the report; a multimeter logging a motor-crane's current to integrate electrical energy in. The automated witness, sworn in for your defence.

## Common Mistakes

- **Building first, measuring never** — schedule the trials before the glue: a machine you can't instrument is a toy, splendid and inadmissible.
- **Efficiency without a loss map** — "13%" alone fails the Bursar; the 87%'s named graves are the audit's heart.
- **Uncalibrated foundations** — the kitchen scale's 8% bias propagates into every joule; correction cards first (the masters WILL ask for them).
- **Multi-variable "iterations"** — lubricating AND re-gearing AND lightening, then celebrating: which worked? The fair-test law has no project exemption.
- **Hiding the failed prediction** — a wrong prediction honestly judged scores above a right one un-tested; the Gauntlet grades the loop, not the luck.
- **No limiting-physics argument** — "it could be better" is not analysis; *"it is grip-limited at launch: here are the skid marks and the budget arithmetic"* is.

## Mental Model

The Gauntlet is **your first appointment as both Chief Engineer and Auditor General of a one-machine realm**. As Chief Engineer you design the realm's industry — the store, the transmission, the spending of energy on motion. As Auditor General you answer to every master who trained you: the Bursar demands the rivers drawn and balanced; Calde's two regulators stamp the books or void them; Hale requires the captives calibrated and the watch set wisely; Vex requires the load paths traced and friction's toll receipted; and Thorne — Thorne requires only what he required of a brass rod a tier ago: *a value, a standard, and the honesty to state your doubt*. The machine is the realm. The report is its constitution. Build both.

## Mini Summary

- ✔ One machine: store → convert → move; chosen for measurability, not spectacle
- ✔ Calibrate every instrument first — claims trace to references
- ✔ Measure store, output, and losses; efficiency WITH a loss map (Sankey it)
- ✔ Iterate by hypothesis: one change, prediction, five-run verdict
- ✔ Defend: where the joules went, why the numbers deserve trust, what physics truly limits — and what three more days would buy

# Guided Practice Quest

Work through the guided steps to choose a machine the books can love, predict its chain before the first joint is glued, and stake your iteration on one honest number.

# Solo Practice Quest

**The Mechanist's Gauntlet.** Build and deliver: (1) **The machine** — any energy-converting build from the candidates (or argue your own); photograph stages. (2) **Calibration annex** — correction cards for every instrument used, references named. (3) **The store's measurement** — force-extension plot (with area) for bands/springs, or mgh/QV arithmetic with measured inputs. (4) **Characterisation campaign** — output energy by measurement (timing gates, range arithmetic, lifted loads), losses by difference AND at least one direct loss test (coast-down, unload-curve, current log); five-run means, uncertainties throughout. (5) **The books** — energy chain Sankey, efficiency, loss map summing to ~100%, the limiting-physics argument. (6) **The iteration** — hypothesis, change, prediction, verdict. (7) **The defence document** — answers to the gallery's four questions, written as if asked. Sign, date, and file beside your Pendulum Trials — the Archive shelves the pair together.

# Integration

**Engineering**: You have just executed the V-model in miniature: requirements (the commission), design, build, verification (characterisation against prediction), and validation (the defence). Add version control to your iterations and uncertainty budgets to your annexes and this workflow IS professional test engineering — from kart teams to spacecraft.

**Mathematics**: The project is applied mathematics throughout: areas under measured curves (the band's energy — numerical integration by counting squares, a Senior-tier method met early), propagating uncertainties through the efficiency quotient, and the five-run means whose √n logic you've carried since Module One. Your books balance because algebra holds them; the Senior tier will teach the machine itself to compute them.

# Lore Conclusion

The defence runs long past the lamps' lighting — the gallery's four questions arriving exactly as predicted, and a fifth, from Thorne, that isn't a question at all: he sets the brass standard back on your bench and asks you to state, one last time as a Junior, what a complete measurement requires. *A value. A unit. An honest doubt.* The four masters confer in the brief, theatrical way of people who decided days ago. Then Calde stamps the books, Hale pins a logged trace of your machine's best run beside the registry's first motors, Vex chalks your name on the floor's slate among the wagon-masters' — and Thorne writes in your logbook, beneath the Apprentice rotation's closing line, the Junior tier's own: *"Accounts in order. Ready for the deep work."* "Senior tier, junior," he says, and almost smiles at the word's new redundancy. "Rotation, oscillation, the fields entire, the atom's interior — and machines that compute what these hands have measured. Rest first. The deep work keeps."
