---
id: phy-jun-m2-09
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m2
moduleTitle: "Module 2: Electricity and Magnetism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: magnetism
topicTitle: "Magnetism"
topicSortOrder: 3
title: "The Motor Effect"
sortOrder: 9
xpReward: 50
practiceType: NONE
questType: PRACTICE
learningObjectives:
  - Describe the force on a current-carrying wire in a magnetic field
  - Use Fleming's left-hand rule to find the force direction
  - Explain how a simple DC motor produces continuous rotation
integrationDomains: [engineering, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - States F = BIL conditions — force greatest when current is perpendicular to the field
    - Applies Fleming's left-hand rule correctly
    - Explains the split-ring commutator's role in continuous rotation
  keywords: [motor effect, Fleming, left-hand, force, BIL, commutator, coil, rotation]
  modelAnswer: |
    A current-carrying wire in a magnetic field feels a force — the motor effect — because the
    wire's own circular field and the external field overlap, crowding lines on one side and
    thinning them on the other; the wire is pushed from crowded toward thin. The force is
    largest when current and field are perpendicular (F = BIL), zero when parallel, and its
    direction follows Fleming's left-hand rule: First finger Field, seCond finger Current,
    thuMb Motion. A coil in the field feels opposite forces on its two sides — a turning pair —
    and the split-ring commutator reverses the current every half-turn so the torque never
    changes direction: continuous rotation, the DC motor, the muscle of the electric age.
guidedSteps:
  - id: phy-jun-m2-09-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Fleming's left-hand rule assigns: First finger, seCond finger, thuMb to —
    inputConfig:
      options:
        - "Force, Current, Mass"
        - "Field, Current, Motion (force) — mutually at right angles"
        - "Field, Charge, Magnetism"
        - "Friction, Current, Momentum"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Field, Current, Motion (force) — mutually at right angles"]
      rejectedFeedback: "First finger = Field (N→S), seCond = Current (conventional), thuMb = Motion. Set the first two along the situation's field and current, and the thumb reports which way the wire is shoved."
    hint: "F-irst finger Field, se-C-ond Current, thu-M-b Motion."
    reflectionPrompt: "What happens to the force direction if you reverse the current? The field? Both at once?"
  - id: phy-jun-m2-09-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A horizontal wire carries current due north, in a field pointing straight down into the ground. The wire is pushed:
    inputConfig:
      options:
        - "Upward"
        - "Due west — perpendicular to both current and field, by the left-hand rule"
        - "Due north"
        - "Nowhere — parallel arrangements give no force"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Due west — perpendicular to both current and field, by the left-hand rule"]
      rejectedFeedback: "First finger down (field), second finger north (current): the thumb points west. The motor force is always perpendicular to BOTH players — that sideways character is what makes rotation possible."
    hint: "Physically arrange your left hand: first finger down, second finger away from you (north)."
    reflectionPrompt: "Why must the force be perpendicular to the current for a motor to extract WORK along a circle?"
  - id: phy-jun-m2-09-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A simple DC motor's coil would swing to vertical and stop — yet real motors spin continuously. Explain the split-ring commutator's trick. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [commutator, reverses, half turn, current direction, torque, same direction, momentum]
      rejectedFeedback: "The two coil sides feel opposite forces — a turning pair — but after a half-turn each side has swapped position, so unchanged current would now turn it BACK. The split-ring commutator reverses the coil's current every half-turn (the gaps passing the brushes exactly at the dead point), so the torque always drives the same way; momentum carries the coil through the switchover instant. Result: continuous one-way spin."
    hint: "What must change about the current each half-turn, and what mechanical part does it?"
    reflectionPrompt: "What do the brushes physically do, and why do old motors' brushes wear out?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The motor-effect force on a wire is ZERO when:"
    options:
      - "The current is large"
      - "The current runs parallel to the field lines"
      - "The wire is long"
      - "The field is strong"
    correctIndex: 1
    feedback: "F = BIL applies to the perpendicular arrangement; align current WITH the field and the force vanishes. Maximum push needs the right-angle geometry — motor designers arrange exactly that."
  - type: MULTIPLE_CHOICE
    question: "To increase a simple motor's turning force you could:"
    options:
      - "Use thinner wire only"
      - "Increase the current, strengthen the field, add more turns to the coil"
      - "Spin it faster by hand"
      - "Remove the commutator"
    correctIndex: 1
    feedback: "Torque scales with B, I, and the coil's turns (each turn is another BIL pair) — the same three levers as the electromagnet, now buying rotation."
---

# Hook

Count the motors within ten metres of you. Phone (vibration), laptop fan, fridge compressor, washing machine, hair dryer, electric toothbrush, the car outside (a petrol car carries forty-plus; an EV, one magnificent one). Civilisation runs on perhaps *fifty billion* electric motors — and every single one, from a watch's wristwatch-whisper to a locomotive's 6-megawatt bellow, exploits one fact you can demonstrate with a battery, a wire, and a fridge magnet:

**A current-carrying wire in a magnetic field gets pushed.** Sideways. Hard in proportion to the current and the field. That push — the *motor effect* — is what happens when two magnetic fields (the wire's own circles, the magnet's lines) are forced to share the same space and disagree about it. Arrange the disagreement around a loop with one fiendishly clever switching trick, and the push becomes endless rotation. Today you build civilisation's muscle.

# Lore Introduction

Hale unveils the cloth-covered kit: a heavy horseshoe magnet, a swing-cradle of stiff copper wire hanging between its poles, a battery and switch. The Tower juniors call this bench "the kick". "Yesterday your current made its own field," she says. "Today we make it stand inside MINE." She nods you to the switch. You close it — and the wire cradle *kicks* sideways, smartly, like something alive. Reverse the battery: it kicks the other way. Hale's grin is at full charge. "Three thousand years to discover the rivers were one; then five years — five! — from Ørsted's flinch to a machine that spins. The Tower's founders wept, junior. Genuinely. They had pushed things all their lives — with hands, oxen, wind, and steam — and here was push *from wire*, silent, obedient, reversible with a battery's flip." She rights the cradle. "Learn the kick's direction-rule, then we trap it on an axle and never let it stop."

# Core Learning

## Concept Introduction

**The motor effect.** Place a current-carrying wire across a magnetic field and it experiences a force. The mechanism, in field-line language: the wire's own circular field adds to the magnet's on one side (lines crowd) and opposes it on the other (lines thin); the wire is pushed from the crowded side toward the thin — *catapulted off the compressed field*. Magnitude:

```
F = B I L    (field strength × current × length in the field — when current ⊥ field)
```

The force is **largest at right angles, zero when current runs parallel to the field**, and always perpendicular to both — a sideways push, which is precisely what circular motion needs (Module One nods).

**Direction: Fleming's left-hand rule.** Thumb and first two fingers at mutual right angles: **F**irst finger = **F**ield (N→S), se**C**ond = **C**urrent (conventional), thu**M**b = **M**otion. Reverse either current or field and the force flips; reverse both and it stays.

**From kick to spin: the DC motor.** A rectangular coil in the field: its two long sides carry current in *opposite* directions, so they're pushed *opposite ways* — a turning pair (a couple of moments; Module One again). Left alone, the coil would swing to vertical and stick there (forces now pulling outward, not around). The fix is the **split-ring commutator**: the coil's terminals are two half-rings on the axle, swept by stationary **brushes** — and the gaps are timed so the coil's current *reverses every half-turn*, exactly at the dead point (momentum coasts it through). The torque therefore always drives the same way: **continuous rotation**. Strengthen it with yesterday's levers — more current, stronger field, more turns — and add iron cores, multiple coils, and curved pole-pieces, and you have every DC motor from toy to tram.

## Why It Matters

- Motors consume ~45% of the world's electricity; the motor effect is, energetically, the most important sentence in this module.
- Fleming's rule and F = BIL are the working tools for everything that pushes with fields — loudspeakers (a coil kicking a cone thousands of times a second), galvanometer needles, maglev, and particle-beam steering.
- The commutator's reverse-at-the-dead-point trick is a masterclass in mechanism design — and understanding it makes AC motors, brushless drives, and generators (next topic) fall into place.

## Worked Examples

**Example 1: Pricing the kick**
The kick bench: B = 0.2 T (a strong horseshoe), I = 5 A, wire length in field L = 0.1 m: F = 0.2 × 5 × 0.1 = **0.1 N** — a feather's weight, but applied to a near-frictionless cradle, hence the lively jump. A real motor stacks the trick: 200 turns = 200 wire-pairs, fatter currents, iron-boosted fields — the same 0.1 N multiplied into useful torque.

**Example 2: The loudspeaker — a motor that never rotates**
A speaker's voice-coil sits in a ring magnet's field, attached to the paper cone. Audio current flows — coil kicks (F = BIL); current reverses — coil kicks back. Feed it music (current wiggling thousands of times per second) and the cone's kicks ARE the sound, pressure-wave by pressure-wave. Every headphone driver is the kick bench, miniaturised and singing — and run *backwards* (cone moved by sound making current) it becomes a microphone, a whisper of the next topic.

**Example 3: Why your drill slows when you lean on it**
Load a spinning motor and it slows; slowed, it draws MORE current (the full story involves back-voltage — induction's preview); more current, more F = BIL torque: the motor *automatically* fights harder. Stall it completely and current soars to the windings' peril — which is why stalled drills smell of hot varnish and why motor circuits carry thermal cut-outs. The motor effect, self-regulating until abused.

## Common Mistakes

- **Using the right hand for the motor rule** — left hand for motors (Fleming's), right for generators (coming); crossing hands crosses answers.
- **Forgetting conventional current** — Fleming's seCond finger follows + → − convention, not electron drift; mixing them flips every prediction.
- **Expecting force along the field or current** — the push is perpendicular to BOTH; that sideways nature is the rotation's whole secret.
- **Omitting the commutator's timing** — reversal must happen AT the vertical dead point; mistimed brushes make a motor that judders or locks.
- **"More voltage always means more spin"** — torque follows current and field; real motors are current-limited, thermally bounded, and (preview) generate their own opposing voltage as they speed up.

## Mental Model

Picture the magnet's field as **a fast river flowing N to S, and the current-carrying wire as a canoe paddle dipped across it**. Dip the paddle square-on (current ⊥ field) and the stream shoves it hard sideways; angle it parallel with the flow and the stream slides past harmlessly — F = BIL's geometry, felt in the wrists. A motor is two paddles on one crankshaft, dipped on opposite sides of the river so the shoves *turn* the crank — and the commutator is the canoeist's trick of flipping each paddle's face at the top of every stroke, so the river's one-way flow drives an endless rotation. Loudspeakers paddle in time to music; galvanometers paddle against a hairspring; locomotives paddle with the strength of a waterfall. One river, one grip rule, fifty billion paddles.

## Mini Summary

- ✔ Current across a field ⇒ sideways force: F = BIL (max ⊥, zero ∥)
- ✔ Fleming's LEFT hand: Field, Current, Motion — reverse either input, the kick flips
- ✔ A coil's opposite sides kick opposite ways: a turning pair
- ✔ The split-ring commutator reverses current each half-turn at the dead point: endless one-way torque
- ✔ Speakers, meters, maglev, and fifty billion motors: one effect, three levers (B, I, turns)

# Guided Practice Quest

Work through the guided steps to load Fleming's fingers correctly, kick a northbound wire due west, and keep a motor spinning past its dead point with one ring, split.

# Solo Practice Quest

Build the Tower's simplest motor (the classic homopolar): a AA battery upright, a neodymium magnet stuck under its base, and a bent copper-wire frame balanced on the battery's top terminal with its arms brushing the magnet's sides — the wire spins continuously the moment the circuit closes. (1) *Document it*: diagram the current path and field direction; apply Fleming's left hand at one point of the wire and show your predicted spin direction matches reality. (2) *Reverse engineering*: flip the magnet, then the battery — record and explain both reversals. (3) *Audit the levers*: vary what you can (two magnets stacked, different frame lengths) and report effects honestly, kitchen-table uncertainties included. Close with a one-paragraph teardown of any dead toy/appliance motor you can open: identify brushes, commutator, coils, magnets — and write the half-turn story their geometry tells.

# Integration

**Engineering**: Real motor engineering is this lesson optimised: brushless DC motors replace the commutator with electronic switching (longer life, no sparks — your drone's secret), induction motors (Tesla's gift) get the rotor's current *induced* rather than brushed in, and traction motors regenerate (run as generators when braking — the next topic arriving early). Motor efficiency standards are climate policy in disguise: a percentage point across fifty billion motors is power stations' worth.

**Mathematics**: F = BIL is your first triple product of physical factors, and Fleming's rule is a hand-held version of the vector cross product — the formal right-angles machinery you'll meet at Senior tier. The coil's turning pair is Module One's moment arithmetic, now electrically funded: torque = force × width, summed over turns.

# Lore Conclusion

Your homopolar spinner whirls on its battery — lopsided, sparking faintly, utterly alive — and Hale watches it with the unguarded fondness she otherwise reserves for the lodestone. "Every junior's first motor goes in the registry," she says, and sketches it there herself, beside a century of cousins. "Push from wire. The founders' dream, on a AA cell." She stills the spinner with one finger and looks at you over it. "Now attend, junior, because tomorrow inverts the world. Today: current, placed in a field, *makes motion*. Every law in this Tower has so far run that one direction." She spins the dead motor's coil idly with her finger — and nods at the meter beside it, whose needle, you suddenly notice, is *twitching in time with her flicks*. "Tomorrow: motion, in a field, makes CURRENT. The river runs uphill. Faraday's revenge — and with it, every power station on Earth."
