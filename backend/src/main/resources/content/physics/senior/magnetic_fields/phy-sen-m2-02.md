---
id: phy-sen-m2-02
domainId: physics
tier: SENIOR
moduleId: phy-sen-m2
moduleTitle: "Module 2: Electromagnetic Theory"
moduleGlyph: "🧲"
moduleSortOrder: 2
topicSlug: magnetic_fields
topicTitle: "Magnetic Fields"
topicSortOrder: 2
title: "Magnetic Fields and Moving Charges"
sortOrder: 2
xpReward: 120
practiceType: NONE
questType: INVESTIGATION
feynmanPrompt: "Explain to a junior student why magnetism only acts on moving charges, why the force points sideways to the motion, and why that sideways force can steer a particle in a circle without ever speeding it up."
learningObjectives:
  - Calculate the magnetic force on a moving charge using F = qvB and on a current-carrying wire using F = BIL
  - Use the right-hand rules to determine force and field directions, and explain why magnetic field lines form closed loops
  - Explain why magnetic forces do no work and how they produce circular motion in charged particle beams
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "States that magnetic force acts only on moving charges, is proportional to charge, speed, and field strength (F = qvB), and vanishes for motion parallel to the field"
    - "Correctly describes the force direction as perpendicular to both velocity and field, determined by a right-hand rule"
    - "Explains that magnetic field lines form closed loops with no start or end, unlike electric field lines"
    - "Explains why the magnetic force does no work and therefore steers charges into circles without changing their speed, with an application (e.g. particle accelerator, mass spectrometer, aurora)"
  keywords: [moving charge, perpendicular, right-hand rule, closed loops, circular, no work]
  modelAnswer: |
    Magnetism is electricity's strange twin. An electric field pushes on any charge,
    moving or still, along the field direction. A magnetic field ignores stationary
    charges completely — it grips only moving ones, with a force F = qvB (for motion
    perpendicular to the field) that grows with charge, speed, and field strength, and
    vanishes if the charge moves parallel to the field lines.

    Stranger still is the direction. The force is perpendicular to both the velocity and
    the field — sideways to everything, given by the right-hand rule: point fingers along
    the velocity, curl them toward the field, and the thumb gives the force on a positive
    charge. A current-carrying wire feels the same physics summed over its drifting
    charges: F = BIL for a wire of length L carrying current I across a field B.

    Magnetic field lines never start or end. Unlike electric lines, which run from
    positive to negative charges, magnetic lines form closed loops — out of a magnet's
    north pole, around, and back in through the south, continuing through the magnet's
    body. Cut a magnet in half and you get two smaller magnets, never an isolated pole.

    Because the force is always perpendicular to the motion, it can never do work: it
    changes the direction of a charge's velocity but never its speed. A charge fired
    across a uniform field is bent into a circle, with qvB supplying the centripetal force
    mv²/r. That is how particle accelerators steer their beams, how mass spectrometers
    sort atoms by mass, and why the aurora paints the poles — solar particles spiralling
    along Earth's field lines into the upper atmosphere.
guidedSteps:
  - id: phy-sen-m2-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Four charges sit in a strong, uniform magnetic field. Which one feels NO magnetic
      force?
    inputConfig:
      options:
        - "A proton moving at right angles to the field"
        - "An electron moving at 45° to the field"
        - "A proton at rest"
        - "An electron moving perpendicular to the field at high speed"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A proton at rest"]
      rejectedFeedback: "Magnetic force requires motion: F = qvB, and v = 0 means F = 0. A stationary charge could sit in the world's strongest magnet forever and feel nothing. (A charge moving exactly parallel to the field would also feel nothing — but every moving option here has a perpendicular component.)"
    hint: "Look at the formula F = qvB. Which variable is zero for one of these charges?"
    reflectionPrompt: "What does this tell you about the difference between electric and magnetic fields as 'detectors' of charge?"
  - id: phy-sen-m2-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A proton (q = 1.6 × 10⁻¹⁹ C) moves at 2 × 10⁶ m/s perpendicular to a 0.5 T magnetic
      field.

      F = qvB = (1.6 × 10⁻¹⁹) × (2 × 10⁶) × 0.5 = 1.6 × 10⁻¹³ N.

      This force is perpendicular to the proton's velocity. It therefore changes the
      proton's ______ but never its speed. (One word.)
    inputConfig:
      placeholder: "One word"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["direction"]
      rejectedFeedback: "A force at right angles to motion steers without speeding up or slowing down — it changes the direction of the velocity only. The proton curves into a circle at constant speed, exactly like the circular motion you studied in Junior year."
    hint: "Think of the whirling conker from circular motion: the string's pull is always sideways to the motion. What did it change, and what did it leave alone?"
    reflectionPrompt: "Why does 'perpendicular force' automatically mean 'no work done'?"
  - id: phy-sen-m2-02-g3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      Electric field lines run from positive charges to negative charges. How do magnetic
      field lines differ?
    inputConfig:
      options:
        - "They run from south poles to north poles"
        - "They form closed loops with no start or end — there are no isolated magnetic poles"
        - "They only exist inside magnetic materials"
        - "They are straight lines, never curves"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["They form closed loops with no start or end — there are no isolated magnetic poles"]
      rejectedFeedback: "No one has ever found an isolated north or south pole. Cut a magnet in half and you get two complete magnets. Field lines loop: out of north, around, in at south, and onward through the magnet's body — closed curves, always."
    hint: "What happens when you cut a bar magnet in half? Do you ever get a lone north pole?"
    reflectionPrompt: "What would the discovery of a single, isolated magnetic pole mean for this picture?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A charged particle moves in a circle in a uniform magnetic field. What is the magnetic force doing to its kinetic energy?"
    options:
      - "Steadily increasing it"
      - "Steadily decreasing it"
      - "Nothing — the force is perpendicular to the motion, so it does no work"
      - "Increasing it on one half of the circle and decreasing it on the other"
    correctIndex: 2
    feedback: "Work requires a force component along the motion. The magnetic force is always exactly sideways, so the speed — and the kinetic energy — never changes. The field is a steering wheel, not an engine."
  - type: MULTIPLE_CHOICE
    question: "A wire of length 0.4 m carries 5 A at right angles across a 0.2 T field. The force on it is..."
    options:
      - "0.4 N"
      - "4 N"
      - "0.04 N"
      - "Zero — fields don't push on wires, only on free charges"
    correctIndex: 0
    feedback: "F = BIL = 0.2 × 5 × 0.4 = 0.4 N. The wire's drifting charges each feel qvB, and the wire feels their sum — the working principle of every electric motor."
---

# Hook

An electric field will push on a charge that sits perfectly still. A magnetic field will not — you could place a charge in the jaws of the strongest magnet ever built and it would feel *nothing*. But let that charge move, and the magnet seizes it with a force that grows with speed and points — bizarrely — sideways: not along the field, not along the motion, but perpendicular to both.

A force that ignores the stationary, grips the moving, and pushes sideways sounds like a riddle. It is actually a machine specification. That one strange rule steers every particle accelerator, spins every electric motor, and paints the aurora across polar skies.

# Lore Introduction

The lodestone from yesterday's closing sits at the centre of Selka's bench, the iron filings above it still frozen in their looping arcs. Beside it: a glass tube, faintly glowing, in which a thread of light — a beam of electrons — runs straight from one end toward the other.

"Hale's storm tower threw sparks," Selka says. "Sparks are charges *moving*. And moving charges live under a second law that stationary ones never feel." She lifts a horseshoe magnet and brings it toward the tube. The thread of light bends — smoothly, silently — into an arc.

"Note what you just saw, Senior. I did not touch the beam. The force is not along my magnet's field, and not along the beam. It is perpendicular to *both*." She moves the magnet; the arc tightens. "Electricity pushes. Magnetism *steers*. Today you learn the steering rule."

# Core Learning

## Concept Introduction

**The magnetic force law.** A charge q moving with speed v perpendicular to a magnetic field B feels a force:

> **F = qvB** — proportional to charge, speed, and field strength. Units of B: the tesla (T).

Two clauses make this law strange. First, *v* is in it: a stationary charge (v = 0) feels nothing, and only the component of velocity perpendicular to the field counts — motion parallel to field lines is ignored. Second, the direction: the force is perpendicular to both the velocity *and* the field, given by the **right-hand rule** — point your fingers along the velocity of a positive charge, curl them toward the field direction, and your thumb points along the force. (For negative charges, reverse it.)

**Wires feel it too.** A current is a parade of drifting charges, so a wire of length L carrying current I perpendicular to a field B feels the summed force:

> **F = BIL**

This is the working stroke of every electric motor: current across a field, force on the wire, rotation. Reverse any one of current, field, or geometry and the force reverses — which is why motors use commutators to flip the current every half-turn.

**Field lines that never end.** Magnetic field lines differ from electric ones in a deep way: they have no sources or sinks. They emerge from a magnet's north pole, sweep around to the south, and continue *through the magnet's interior* back to north — closed loops, always. Cut a magnet in half and you get two complete magnets; no experiment has ever isolated a lone magnetic pole. Where electric lines begin and end on charges, magnetic lines simply circulate. (Keep this asymmetry in mind — it becomes one of Maxwell's four pillars in two lessons' time.)

**The force that does no work.** Because the magnetic force is always perpendicular to the velocity, it has no component along the motion — so it does *no work*, ever. It cannot speed a charge up or slow it down; it can only redirect it. A charge fired across a uniform field is therefore bent into a **circle at constant speed**, with the magnetic force supplying the centripetal force you studied in Junior year:

> qvB = mv²/r, so **r = mv / (qB)**

Faster or heavier particles sweep wider circles; stronger fields and larger charges bend tighter. That single equation is the design principle of cyclotrons, mass spectrometers, and the magnetic bottles that trap fusion plasma.

## Why It Matters

The sideways force runs the modern world's machinery. Every electric motor — in trains, drills, disk drives, and a billion vehicles — is F = BIL harnessed to a shaft. Loudspeakers are the same force vibrating a coil thousands of times a second. In the laboratory, r = mv/(qB) is an instrument: mass spectrometers bend ionised atoms through a known field and read their masses off the radius of the curve, which is how chemists identify molecules and archaeologists date carbon. Particle accelerators from hospital cyclotrons to the LHC use magnets purely as steering — the magnetic force does no work, so electric fields provide the push and magnetic fields the bend. And the aurora is this lesson written across the sky: solar particles spiral helically along Earth's field lines, funnelled poleward until they strike the upper atmosphere and make it glow.

## Worked Examples

**Example 1 — Force on a beam particle.** A proton (q = 1.6 × 10⁻¹⁹ C) crosses a 0.5 T field at 2 × 10⁶ m/s, perpendicular.
F = qvB = 1.6 × 10⁻¹⁹ × 2 × 10⁶ × 0.5 = **1.6 × 10⁻¹³ N**.
Minuscule — but the proton's mass is 1.7 × 10⁻²⁷ kg, so the acceleration is nearly 10¹⁴ m/s². Fields herd particles ferociously.

**Example 2 — Radius of the circle.** The same proton (m = 1.7 × 10⁻²⁷ kg) curves with radius
r = mv/(qB) = (1.7 × 10⁻²⁷ × 2 × 10⁶) / (1.6 × 10⁻¹⁹ × 0.5) = **4.3 cm**.
A mass spectrometer reads this radius backwards: measure r, know q, v, and B — out comes the mass.

**Example 3 — Motor force.** A motor coil segment 0.4 m long carries 5 A perpendicular to a 0.2 T field.
F = BIL = 0.2 × 5 × 0.4 = **0.4 N** per wire segment.
A motor multiplies this by winding hundreds of turns: 200 turns gives 80 N — a serious pull from a modest current, repeated every rotation.

## Common Mistakes

- Expecting magnetic fields to push on stationary charges — no motion, no force; that is the electric field's job
- Drawing the force along the field lines or along the velocity — it is perpendicular to *both*; use the right-hand rule every time
- Forgetting to reverse the rule for negative charges — an electron curves the opposite way to a proton
- Thinking the magnetic force speeds particles up — it does no work; speed and kinetic energy are untouched, only direction changes
- Drawing magnetic field lines starting on north poles and ending on south poles — they continue through the magnet and close the loop; there are no magnetic endpoints
- Using the full velocity in F = qvB when the motion is at an angle — only the component perpendicular to the field contributes

## Mental Model

Think of the magnetic field as a riverbed full of invisible rails, and a moving charge as a cart that engages the rails *only while rolling*. Stand still and the rails ignore you. Roll, and a guide-arm grips you from the side — never pushing you forward, never braking you, only turning your wheels. The faster you roll, the harder the sideways grip, so you carve a circle whose tightness is set by your speed, your load, and the strength of the rails. An electric field is a slope that makes carts roll; the magnetic field is the banking that decides where rolling carts go.

## Mini Summary

- Magnetic fields exert force only on moving charges: F = qvB (perpendicular motion), direction given by the right-hand rule, perpendicular to both v and B
- A current-carrying wire feels F = BIL — the principle of the electric motor
- Magnetic field lines form closed loops with no start or end; isolated magnetic poles have never been observed
- The magnetic force does no work: it bends charges into circles of radius r = mv/(qB) at constant speed — the steering principle of accelerators, mass spectrometers, and the aurora

# Guided Practice Quest

Selka wheels the electron tube to the centre of the bench and hands you the horseshoe magnet. "Three trials. First, decide which charges the field can grip at all — the law is strict about this. Second, compute the grip on a real proton and tell me what the force changes and what it leaves alone. Third, the field lines themselves: trace them with the filings card and tell me where they begin." She pauses. "Trick question, that last one. Watch carefully."

# Solo Practice Quest

Write an investigation log (350–500 words) on the magnetic force. Explain: why magnetism acts only on moving charges, including what F = qvB says quantitatively and what happens for motion parallel to the field; how the right-hand rule fixes the force direction, and why the same physics gives F = BIL for a wire; why magnetic field lines form closed loops and what cutting a magnet in half demonstrates; and why a force that does no work is still supremely useful — work through the circular-motion argument (qvB = mv²/r) and give one real application of magnetic steering, such as the mass spectrometer, the cyclotron, or the aurora.

# Integration

**Mathematics:** The magnetic force is your first serious encounter with the vector cross product — an operation that takes two vectors and returns a third perpendicular to both, with magnitude qvB sin θ. The right-hand rule *is* the cross product performed on your fingers. The closed-loop property of field lines has a precise mathematical name — zero divergence — which you will meet formally in Maxwell's equations.

**Engineering:** Motor design is the engineering of F = BIL: stacking turns, shaping pole pieces to concentrate field lines, and switching current at exactly the right moment. The same physics in reverse makes generators (next lesson's territory). Beamline engineers at accelerators chain dipole magnets (bending) and quadrupole magnets (focusing) like an optician arranging lenses — magnetic optics for particles instead of light.

# Lore Conclusion

Selka switches off the tube, and the thread of light dies. She sets the horseshoe magnet beside the amber rod from last lesson — the two artifacts side by side on the slate.

"Electricity. Magnetism. Hale taught them to you in his Junior storm tower as cousins who share a wing of the Academy." She looks at you over the rims of her spectacles. "They are not cousins. Watch."

She picks up a coil of copper wire connected to a sensitive needle-galvanometer — no battery anywhere in the circuit — and *thrusts* the lodestone into the coil. The needle slams sideways. She holds the magnet still: the needle falls dead. She withdraws it: the needle slams the other way.

"A *changing* magnetic field has just made an electric current out of nothing but motion. You met this trick in Junior year as induction — a fact, a recipe for generators. Tomorrow we ask the Senior question: if a changing magnetic field creates an electric field... what does a changing *electric* field create?" She chalks the next title: *Electromagnetic Waves.* "Think on it tonight. The answer is the reason you can see the chalkboard at all."
