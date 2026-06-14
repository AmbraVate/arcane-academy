---
id: phy-app-m2-10
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: work_and_energy
topicTitle: "Work and Energy"
topicSortOrder: 4
title: "Work and Power"
sortOrder: 10
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Calculate work done as force × distance moved in the force's direction
  - Explain when a force does no work
  - Calculate power as work done per unit time
integrationDomains: [mathematics, biology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Computes W = F × d with correct units (joules)
    - Identifies a case where a force does zero work
    - Computes power P = W/t in watts
  keywords: [work, joule, force, distance, power, watt, direction, per second]
  modelAnswer: |
    Work done = force × distance moved in the direction of the force: lifting a 50 N box
    through 2 m does 100 J of work. A force does no work when nothing moves (holding a wall)
    or when motion is perpendicular to the force (carrying a bag horizontally — the upward
    hold does no work on it). Power is the rate of working: P = W/t in watts. Two students
    doing identical work climbing identical stairs do the same joules; the one who runs does
    them in less time and so develops more watts.
guidedSteps:
  - id: phy-app-m2-10-g1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A removal worker lifts a 200 N box from the floor to a shelf 1.5 m up. Work done = ________ J.
    inputConfig:
      placeholder: "300"
    markingRule:
      matchMode: CONTAINS
      accepted: ["300"]
      rejectedFeedback: "W = F × d = 200 N × 1.5 m = 300 J. Newtons times metres are joules — energy transferred to the box's height."
    hint: "Multiply the force by the distance moved along it."
    reflectionPrompt: "Where did those 300 J come from, and where are they now?"
  - id: phy-app-m2-10-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A weightlifter holds 800 N stationary above his head for 10 gruelling seconds. The work he does ON THE BAR during the hold is:
    inputConfig:
      options:
        - "8000 J"
        - "800 J"
        - "Zero — the bar doesn't move, so no work is done on it"
        - "Impossible to say"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Zero — the bar doesn't move, so no work is done on it"]
      rejectedFeedback: "Work needs distance: W = F × d, and d = 0. His muscles burn energy internally (they twitch and strain), but none of it is transferred to the bar. Physics' definition of work is stricter than the gym's."
    hint: "What is d for the bar during the hold?"
    reflectionPrompt: "Why does it FEEL like hard work? Where is his energy actually going?"
  - id: phy-app-m2-10-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Two identical 60 kg students climb identical 5 m staircases. Aisha takes 5 s; Ben takes 20 s. In 2–3 sentences, compare the work each does and the power each develops (use g ≈ 10 N/kg).
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["3000", "600", "150", same work, more power, watt]
      rejectedFeedback: "Both do the same work: W = mgh = 60 × 10 × 5 = 3000 J. Power differs: Aisha 3000/5 = 600 W; Ben 3000/20 = 150 W. Same joules, different rate — power is work per second."
    hint: "Work depends on force and height only. Power brings in the time."
    reflectionPrompt: "Which of the two numbers — joules or watts — does a stopwatch change?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The unit of work (and energy) is the:"
    options: ["Watt", "Newton", "Joule", "Kilogram"]
    correctIndex: 2
    feedback: "Work transfers energy, measured in joules: 1 J = 1 N·m. The watt measures POWER — joules per second."
  - type: MULTIPLE_CHOICE
    question: "A 2 kW kettle runs for 90 seconds. Energy transferred:"
    options: ["2,000 J", "90 J", "180,000 J", "22 J"]
    correctIndex: 2
    feedback: "E = P × t = 2000 W × 90 s = 180,000 J. Rearranging P = E/t — the balance rule again."
---

# Hook

In physics, you can strain every muscle in your body for a full minute and accomplish — by the official definition — *zero work*. Hold a heavy suitcase motionless: zero. Push with all your strength against a wall that doesn't budge: zero. Your muscles disagree, your sweat disagrees, but the ledger is firm.

Why such a strict definition? Because physics needed a precise way to count *energy changing hands* — and energy only changes hands when a force actually moves something through a distance. That count, *work*, turns out to be one of the most useful currencies in science. And its companion, *power*, settles a different question entirely: not "how much?" but "how fast?" — the difference between strolling upstairs and sprinting, between a kettle and a power station.

# Lore Introduction

The Academy's water comes from a well in the lower courtyard, and the well has a famous brass plaque. Thorne reads it to you: *"Raising one pail to the rim: 2,000 newton-metres. No magic, lever, nor pulley shall lessen it."* He winds the crank; the pail rises. "Generations of clever apprentices have tried to cheat this plaque," he says. "Longer crank handles — easier each turn, but more turns. Pulley systems — half the force, twice the rope. The well takes its two thousand, every time, in some mixture of harder and longer." He gestures to a second, smaller plaque below, added centuries later: *"But HOW FAST you pay it is your own affair."* Thorne smiles. "Two plaques, two quantities. The first we call work. The second, power. Today you learn to keep both accounts."

# Core Learning

## Concept Introduction

**Work** is energy transferred by a force moving its point of application:

```
W = F × d
work (J) = force (N) × distance moved in the force's direction (m)
```

The **joule** (J) = 1 newton-metre — Module One's unit-unpacking pays off again. Lifting 200 N through 1.5 m: 300 J transferred to the box (it now sits higher; that stored height is next lesson's business).

**The strictness clauses:**

- **No distance, no work.** Holding, pressing, straining against the immovable: W = 0, however it feels. (Muscles burn fuel internally to maintain tension — real energy, spent inside you, none delivered to the bar.)
- **Direction counts.** Only distance moved *along the force* counts. Carry a bag on a level walk: your upward hold is perpendicular to the horizontal motion — zero work done on the bag by that hold. Friction, dragging backwards on a sliding crate, does *negative* work: it drains energy out.

**Power** is the rate of doing work:

```
P = W / t
power (W) = work (J) ÷ time (s)
```

One **watt** = one joule per second. Useful anchors: a phone charger ~5 W, a human climbing stairs briskly ~500 W, a kettle ~2,000 W, a car engine ~100,000 W (≈ 134 horsepower; one horsepower ≈ 746 W — James Watt needed to sell steam engines to people who owned horses).

Same work, different powers: 3,000 J of stair-climbing is 600 W done in 5 s, or 150 W done in 20 s. Machines and bodies are usually limited by their *power* (rate), not by the total work they can do.

## Why It Matters

- Work is how mechanics connects to energy — the master currency of the next two lessons and, frankly, of all physics.
- Power ratings run your life: every appliance label, car spec, and electricity bill (the kilowatt-hour is power × time = energy) is this lesson applied.
- The well's plaque is real physics: no machine reduces the work a job requires — levers and gears only trade force against distance. Knowing this immunises you against perpetual-motion sales pitches forever.

## Worked Examples

**Example 1: The lever's honest bargain**
A crowbar lets you lift a 600 N slab with only 150 N of push. Cheating the plaque? Check the distances: your end sweeps 40 cm while the slab rises 10 cm. Your work: 150 × 0.4 = 60 J. Slab's gain: 600 × 0.1 = 60 J. The lever traded *force for distance* at constant work — every machine's only trick.

**Example 2: Negative work slows the sled**
A sled slides 20 m across snow against 30 N of friction. Friction's work: −30 × 20 = −600 J — six hundred joules *removed* from the sled's motion (and delivered, as you'll learn, into warmth of the snow and runners). Negative work is how things slow down, in energy language.

**Example 3: Sizing a motor by power**
A lift must raise 5,000 N through 30 m in 20 s. Work: 150,000 J. Power: 150,000/20 = 7,500 W. The engineer specs a 10 kW motor (margins for friction and acceleration) — a real design calculation, done with two formulas you now own.

## Common Mistakes

- **Paying work for effort** — sweat is not the unit; W = F × d, and d is non-negotiable.
- **Confusing work with power** — joules say *how much*, watts say *how fast*. "A 2 kW kettle" states no amount of energy until you say for how long.
- **Counting perpendicular distance** — carrying horizontally does no work *against gravity*; only the lifting parts of a journey do.
- **Expecting machines to reduce work** — they reduce *force* (or increase speed), never the product F × d; if anything, friction makes the total slightly worse.
- **Mixing units** — minutes into P = W/t, kilonewtons into W = Fd; convert first (Module One never stops paying).

## Mental Model

Picture energy as **water, work as the act of pouring, and power as the width of the funnel**. The well's plaque states the pour required: two thousand units, no negotiation. A lever or pulley reshapes your jug — many small pours instead of one heavy one — but the total poured is fixed. Power is how fast your funnel lets it through: a kettle is a firehose, a phone charger a drinking straw, your legs on a staircase somewhere between, and every machine's price tag quotes its funnel, not its water.

## Mini Summary

- ✔ W = F × d (joules): energy transferred by a force through a distance — along the force only
- ✔ No movement, or perpendicular movement → zero work, whatever the effort feels like
- ✔ Friction does negative work: it drains motion's energy away
- ✔ P = W/t (watts): the rate of working; kettles ~2 kW, humans ~hundreds of W
- ✔ Machines trade force for distance; the work itself can never be discounted

# Guided Practice Quest

Work through the guided steps to lift a box honestly, acquit a straining weightlifter of any work, and split a staircase race into joules and watts.

# Solo Practice Quest

Measure your own engine: find a staircase, estimate its vertical height (count steps × ~18 cm), and time yourself climbing it twice — once at a comfortable walk, once as fast as is safe. Using your mass: (1) compute the work per climb (W = mgh — should it differ between runs?); (2) compute your power for each run in watts; (3) compare your sprint power with a kettle's 2,000 W and a phone charger's 5 W; (4) write two sentences on why you cannot sustain your sprint figure for an hour, in the language of power versus work. Show units at every line.

# Integration

**Mathematics**: Work as force × distance is your first *accumulation* of a quantity along a path — the idea that becomes the integral ∫F dx. The lever's constant-work trade is conservation expressed as algebra: F₁d₁ = F₂d₂, a proportionality you can now read fluently.

**Biology**: Muscles are ~25% efficient engines: for every joule of mechanical work you deliver, roughly three more leave as heat — why exercise warms you fourfold beyond the work done. Your sustainable output (~100 W) versus sprint output (~1,000 W) is the tale of two metabolic funnels: aerobic and anaerobic.

# Lore Conclusion

You crank the full pail to the rim and, at Thorne's nod, read the great plaque aloud like every apprentice before you. "Two thousand, paid in full," he confirms. "But here is the question the plaque does not answer: paid *to whom*? The pail sits at the rim now, quiet and still. Where did your two thousand newton-metres *go*?" He taps the pail's rope, then lets it slip an inch — the crank spins, the rope hums with sudden eagerness. "It is still here, apprentice. Stored. Waiting. Tomorrow we open the universe's bank — and I will show you the two great accounts in which all motion keeps its savings."
