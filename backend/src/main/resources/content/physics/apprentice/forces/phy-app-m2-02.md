---
id: phy-app-m2-02
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: forces
topicTitle: "Forces"
topicSortOrder: 1
title: "The Family of Forces"
sortOrder: 2
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - Distinguish contact forces from non-contact forces
  - Identify weight, normal force, friction, tension, and air resistance in scenarios
  - Distinguish mass (kg) from weight (N) and relate them via W = mg
integrationDomains: [chemistry, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Sorts forces into contact and non-contact categories with examples
    - Identifies the forces acting in a described scenario
    - Distinguishes mass from weight and uses W = mg correctly
    - Explains why an astronaut's mass is unchanged on the Moon while weight differs
  keywords: [contact, non-contact, weight, mass, normal, friction, tension, gravity, W = mg]
  modelAnswer: |
    Contact forces require touching: the normal force from surfaces, friction opposing sliding,
    tension along ropes, air resistance from moving through air. Non-contact forces act across
    empty space: gravity, magnetism, and electrostatic attraction. Weight is the gravitational
    force on an object, W = mg — on Earth a 10 kg mass weighs about 98 N. Mass measures how
    much matter an object contains and never changes with location; weight depends on local
    gravity, so the same astronaut has identical mass but one-sixth the weight on the Moon.
guidedSteps:
  - id: phy-app-m2-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which list contains only **non-contact** forces?
    inputConfig:
      options:
        - "Gravity, magnetism, electrostatic attraction"
        - "Friction, tension, gravity"
        - "Normal force, air resistance, magnetism"
        - "Tension, friction, normal force"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Gravity, magnetism, electrostatic attraction"]
      rejectedFeedback: "Gravity, magnetism, and electrostatic forces all act across empty space — no touching required. Friction, tension, normal force, and air resistance all require contact."
    hint: "Which forces can act on an object through a vacuum?"
    reflectionPrompt: "What everyday evidence shows gravity needs no contact?"
  - id: phy-app-m2-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Using W = mg with g ≈ 10 N/kg: a 6 kg cat weighs about ________ N on Earth.
    inputConfig:
      placeholder: "60"
    markingRule:
      matchMode: CONTAINS
      accepted: ["60"]
      rejectedFeedback: "W = mg = 6 kg × 10 N/kg = 60 N. The kilograms measure the cat's matter; the newtons measure Earth's pull on it."
    hint: "Multiply the mass by g ≈ 10 N/kg."
    reflectionPrompt: "What would the same cat weigh on the Moon, where g is about 1.6 N/kg? What stays the same?"
  - id: phy-app-m2-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A sledge is pulled across snow by a rope at steady speed. List the four main forces acting on the sledge, classifying each as contact or non-contact. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [tension, friction, weight, gravity, normal, contact]
      rejectedFeedback: "On the sledge: tension from the rope (contact), friction from the snow opposing sliding (contact), weight from Earth's pull (non-contact), and the normal force from the snow pushing up (contact)."
    hint: "One pulls it forward, one resists its slide, one pulls it down, one holds it up."
    reflectionPrompt: "Which of the four would change if the snow turned to ice? To gravel?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An astronaut travels from Earth to the Moon. Which statement is true?"
    options:
      - "Both her mass and weight decrease"
      - "Her mass is unchanged; her weight decreases to about one-sixth"
      - "Her weight is unchanged; her mass decreases"
      - "Both are unchanged"
    correctIndex: 1
    feedback: "Mass — the amount of matter — travels with her unchanged. Weight = mg depends on local gravity, and lunar g is about 1/6 of Earth's."
  - type: MULTIPLE_CHOICE
    question: "A book slides across a table and gradually stops. The force most responsible is:"
    options: ["Gravity", "The normal force", "Friction", "Tension"]
    correctIndex: 2
    feedback: "Friction acts between the sliding surfaces, opposing the motion until the book halts. Gravity and the normal force act vertically and balance each other here."
---

# Hook

Hold this thought: the force pinning you to your seat right now is the *same* force steering the Moon around the Earth and the Earth around the Sun. Gravity doesn't need to touch you — it reaches across empty space, across a quarter of a million miles to the Moon, across ninety-three million miles from the Sun.

Meanwhile, far humbler forces run your daily life by direct contact: friction lets your shoes grip the pavement (try running on ice to appreciate it), tension in cables holds elevators, and the so-called normal force from every chair and floor is the only thing standing between you and the centre of the Earth.

Physics sorts this crowd into a small family tree — and clears up, once and for all, the most commonly confused pair in science: *mass* and *weight*. They are not the same thing. One of them changes if you stand on the Moon. The other never changes anywhere in the universe.

# Lore Introduction

In the Hall of Mechanisms, Thorne has arranged a row of exhibits like suspects in a line-up. A lodestone holding an iron ring aloft across empty air. A rope strung taut over a pulley, a stone hanging from it. A polished ramp beside a rough one, with identical blocks resting on each. "Forces," he announces, "but not one family — two. Watch." He passes a sheet of parchment between the lodestone and the ring: the ring doesn't so much as tremble. "This one needs no touch. It would work across a vacuum, across the void between worlds." He plucks the taut rope, which hums. "This one dies the instant the rope is cut. Touch, or nothing." He turns to you. "Tonight you learn the family names — and then I will weigh you, and tell you why the number on the scale is not what you think it is."

# Core Learning

## Concept Introduction

**Non-contact forces** act across empty space:

- **Gravity** — every mass pulls every other mass; for everyday purposes, Earth pulls everything toward its centre.
- **Magnetism** — magnets attract or repel certain metals and each other.
- **Electrostatic force** — rubbed balloons, static-charged hair: charges attract or repel.

**Contact forces** require touching surfaces or links:

| Force | What it does | Direction |
|-------|--------------|-----------|
| **Normal (support) force** | A surface pushes on whatever presses on it | Perpendicular to the surface |
| **Friction** | Opposes sliding (or attempted sliding) between surfaces | Along the surface, against the motion |
| **Tension** | A stretched rope, cable, or string pulls on what it's attached to | Along the rope, inward |
| **Air resistance (drag)** | Air pushes back on anything moving through it | Opposite to the motion |

**Mass versus weight** — the crucial distinction:

- **Mass** (kg) measures *how much matter* an object contains. It is a scalar and is the same everywhere — Earth, Moon, deep space.
- **Weight** (N) is the *gravitational force* on that mass: **W = mg**, where g ≈ 9.8 N/kg on Earth (≈10 for quick work). Weight is a vector pointing toward the planet's centre, and it changes with location: lunar g ≈ 1.6 N/kg, so everything weighs about one-sixth as much there.

Your bathroom scale, strictly, measures the force you press on it (newtons) and *displays* an inferred mass — a distinction that becomes vivid in an accelerating lift, as you'll see in coming lessons.

## Why It Matters

- Naming forces correctly is step one of every mechanics problem; misnaming them (or inventing nonexistent ones) is the main source of wrong answers.
- The mass/weight distinction underpins space flight, engineering specs, and even shopping: goods are sold by mass, scales measure force, and the law cares about the difference.
- Friction and drag dominate real-world motion — vehicle design, sports, and footwear are largely applied friction management.

## Worked Examples

**Example 1: The hanging lamp**
A lamp hangs from a ceiling cord. Forces on the lamp: weight (Earth's pull, non-contact, downward) and tension (cord's pull, contact, upward). It hangs at rest, so the two balance: tension = weight. Cut the cord — tension vanishes instantly; weight remains; motion follows.

**Example 2: Mass and weight on three worlds**
A 12 kg toolbox: on Earth, W = 12 × 9.8 ≈ 118 N. On the Moon (g = 1.6): ≈ 19 N. Drifting in deep space, far from any planet: weight ≈ 0 N. In all three places, mass = 12 kg, and shoving the toolbox sideways feels equally hard — resisting a push is mass's job, not weight's.

**Example 3: Why you can walk at all**
Walking: your foot pushes backward against the ground; friction from the ground grips your sole and pushes you *forward*. On ice, friction is feeble — your foot slides backward and you go nowhere (or down). Friction is not the enemy of motion here; it is the enabler.

## Common Mistakes

- **Using mass and weight as synonyms** — "I weigh 70 kg" conflates them; 70 kg is mass, the weight is ~700 N.
- **Thinking heavier objects always have more of *everything*** — on the Moon your weight drops; your mass and your difficulty stopping a rolling cart do not.
- **Forgetting friction can point forward** — it opposes *sliding between surfaces*, which for a walking foot or a driven wheel means pushing the mover forward.
- **Inventing a "force of motion"** — a coasting object has motion, not a force; the only forces on it are the family members above.
- **Assuming the normal force always equals weight** — true for a book on a level table; false on slopes and in lifts. Check each situation.

## Mental Model

Picture every object as wearing **a name-tag for each force currently acting on it**, written as *agent → receiver*. A parked car wears two tags: "Earth → car (weight, down)" and "road → car (normal, up)". Pull away and the tags multiply: "engine-driven wheels grip road → forward friction", "air → car (drag, backward)". An object's behaviour at any instant is decided entirely by reading its current tags — never by tags it wore in the past.

## Mini Summary

- ✔ Non-contact forces (gravity, magnetism, electrostatic) act across empty space
- ✔ Contact forces: normal, friction, tension, drag — each with a definite direction
- ✔ Mass (kg) = amount of matter, same everywhere; weight (N) = mg, location-dependent
- ✔ Friction opposes sliding — which can mean pushing a walker or a wheel *forward*
- ✔ Identify forces by naming agent → receiver; never invent a "force of motion"

# Guided Practice Quest

Work through the guided steps to sort the force family, weigh a cat in proper units, and audit the forces on a moving sledge.

# Solo Practice Quest

Choose three scenes: an object at rest on a surface, an object hanging from something, and an object sliding or rolling. For each, draw the object alone and add labelled arrows for every force acting on it — name each force's type, its agent, and whether it's contact or non-contact. Then compute your own weight in newtons from your mass (g ≈ 9.8 N/kg), and write two sentences on what your scale would read on the Moon and why the supermarket still sells flour "by the kilogram" rather than by the newton.

# Integration

**Chemistry**: Zoom in far enough and every contact force dissolves into non-contact ones — the normal force and friction are electrostatic repulsions and attractions between the outer electrons of atoms that never truly "touch". Contact, at the atomic scale, is a useful illusion.

**Biology**: Bone density, muscle strength, and balance all calibrate themselves to your weight, not just your mass — which is why astronauts in microgravity lose bone and muscle despite their mass being unchanged, and why their exercise regimes are a medical necessity.

# Lore Conclusion

Thorne sets the Academy's great steelyard balance before you and weighs you, calling the number in newtons — which sounds alarmingly large until he chalks the conversion. "The scale reads the force between you and the Earth," he says. "Sail to the Moon and the scale will flatter you sixfold. But the *you* that must be pushed, stopped, and turned —" he taps your shoulder, "— that travels unchanged, and tomorrow we put it to the test." He extinguishes the hall's lamps until only the lodestone exhibit glows faintly in the dark, iron ring still hovering. "Tomorrow: what happens when the forces on a thing do *not* agree."
