---
id: phy-app-m2-07
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: newtonian_mechanics
topicTitle: "Newtonian Mechanics"
topicSortOrder: 3
title: "Newton's First Law: Inertia"
sortOrder: 7
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - State Newton's first law in your own words
  - Use inertia to explain everyday experiences (seatbelts, lurching buses)
  - Explain why friction long disguised the law from humanity
integrationDomains: [history, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States the law — objects keep their velocity unless acted on by a resultant force
    - Explains one everyday event using inertia correctly
    - Explains why everyday experience (friction everywhere) hides the law
    - Identifies inertia with mass, not with motion or force
  keywords: [inertia, first law, resultant, constant velocity, friction, mass, rest]
  modelAnswer: |
    Newton's first law: an object stays at rest, or moves at constant velocity, unless a
    resultant force acts on it. Motion needs no maintainer — only changes of motion need
    causes. Standing passengers lurch forward when a bus brakes because their bodies continue
    at the old velocity while the bus slows under them; seatbelts supply the force our bodies
    otherwise lack. Everyday life hides the law because friction and drag act on everything,
    so unpushed objects stop — remove them (ice rinks, space) and endless coasting is revealed.
    Inertia is measured by mass: the more mass, the more force a given change requires.
guidedSteps:
  - id: phy-app-m2-07-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A spacecraft in deep space, engines off, far from any planet, is drifting at 5 km/s. What happens over the next year?
    inputConfig:
      options:
        - "It gradually slows and eventually stops"
        - "It continues at 5 km/s in a straight line"
        - "It slowly speeds up"
        - "It stops as soon as its leftover engine force runs out"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It continues at 5 km/s in a straight line"]
      rejectedFeedback: "With no resultant force — no drag, no gravity worth counting, no thrust — there is nothing to change its motion. It coasts at 5 km/s indefinitely. Voyager 1 has been doing exactly this since 1977."
    hint: "Is there any force present to change the motion?"
    reflectionPrompt: "Why does this answer feel wrong to human intuition, and what does friction have to do with that?"
  - id: phy-app-m2-07-g2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      A standing bus passenger lurches FORWARD when the driver brakes hard. Using inertia — not 'a force threw them forward' — explain what actually happened. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [inertia, continue, keeps moving, bus slows, no force, velocity]
      rejectedFeedback: "No forward force acted on the passenger. The bus slowed; the passenger's body, with nothing slowing IT, continued at the original velocity — the bus decelerated out from under them. Lurching is the absence of a force, not the presence of one."
    hint: "Whose motion changed — the bus's, or the passenger's? Which one had a force acting?"
    reflectionPrompt: "What do seatbelts actually provide, in the language of forces?"
  - id: phy-app-m2-07-g3
    sortOrder: 3
    inputType: FILL_BLANK
    instruction: |
      The resistance of an object to changes in its motion is called inertia, and it is measured by the object's ________.
    inputConfig:
      placeholder: "mass"
    markingRule:
      matchMode: CONTAINS
      accepted: [mass]
      rejectedFeedback: "Mass is the measure of inertia — a loaded trolley resists starting AND stopping more than an empty one. Not weight: inertia works the same in orbit, where weight is absent."
    hint: "Which property makes a full shopping trolley harder to start and stop than an empty one?"
    reflectionPrompt: "An astronaut shoves a 500 kg crate in weightless orbit. Easy or hard? Why?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Newton's first law says an object moving at constant velocity must have:"
    options:
      - "A steady forward force maintaining it"
      - "Zero resultant force acting on it"
      - "No forces at all acting on it"
      - "A large mass"
    correctIndex: 1
    feedback: "Constant velocity = no change in motion = zero RESULTANT. Forces may well be acting (engine vs drag) — they just cancel. 'No forces at all' is stronger than required."
  - type: MULTIPLE_CHOICE
    question: "Why did it take humanity two thousand years to discover this law?"
    options:
      - "Nobody watched moving objects before Newton"
      - "On Earth, friction and drag act on everything, so unpushed motion always dies away — hiding the rule"
      - "The mathematics was too advanced"
      - "Objects behaved differently in ancient times"
    correctIndex: 1
    feedback: "Earthly experience is rigged: invisible resistive forces stop everything, teaching the false lesson 'motion needs a pusher'. Galileo's genius was imagining the frictionless case."
---

# Hook

Voyager 1 left Earth in 1977 with a tank of fuel it mostly spent in the first hours. For over four decades since, its engines silent, it has crossed billions of kilometres — past Jupiter, past Saturn, out beyond the edge of the solar wind — *coasting*. Nothing pushes it. Nothing needs to.

That single fact demolishes the oldest, most natural belief in physics: that motion requires a mover, that things stop unless something keeps them going. Aristotle taught it; your muscles believe it; every cart and ball you've ever pushed seems to confirm it. And it is wrong. Things stop on Earth because something *stops them* — friction, drag, the great hidden brakes of ordinary life.

Newton's first law is the correction, and it reframes every question in mechanics: stop asking what keeps things moving. Ask what *changes* their motion.

# Lore Introduction

Thorne takes you at dawn to the Academy's frozen mirror-pond, polished black ice from edge to edge. He sets an iron puck on the ice and gives it the gentlest push. It glides... and glides... and is still gliding when it finally taps the far bank, two hundred paces away. "On the flagstones of the Long Gallery, that push buys three paces," he says. "On this ice, two hundred. Tell me, apprentice — what would it buy on a pond with *no* roughness at all? A pond polished beyond perfection?" You hesitate at the edge of the answer, because it sounds like madness: *it would never stop.* Thorne reads your face. "Say it aloud. The flagstones have lied to you all your life. This morning the ice tells the truth."

# Core Learning

## Concept Introduction

**Newton's First Law.** An object at rest stays at rest, and an object in motion continues at constant velocity (same speed, same direction), *unless acted on by a resultant force*.

The law's real content is a redirection of attention:

- Motion is **not** a process needing maintenance. It is a *state* — as natural and self-sustaining as rest.
- Only **changes** of motion (speeding, slowing, turning) require explanation, and the explanation is always a resultant force.
- Rest and constant velocity are physically equivalent: both are "no change", both mean zero resultant. (This equivalence runs deep — a smoothly cruising train's interior behaves exactly like a stationary room, which is why you can pour tea on one.)

**Inertia** is the name for matter's reluctance to change its motion, and its measure is **mass**. A 2 kg melon and a 200 kg piano can both sit still or both coast — but changing the piano's motion takes a hundred times more force for the same effect. Note: inertia is *not* weight. In orbit, a massive crate is weightless yet just as hard to shove, and just as dangerous to be caught between when moving.

**Why the law stayed hidden.** Earth's surface is saturated with resistive forces — friction, drag, rolling resistance — which act on every moving object, unbidden and invisible. So everything unpushed slows and stops, and twenty centuries drew the natural, wrong conclusion. Galileo's ramps and thought experiments stripped the friction away in imagination; Newton made it law.

## Why It Matters

- The first law defines the *question structure* of all mechanics: see a change of motion → hunt the resultant force; see constant velocity → conclude the forces balance.
- Vehicle safety is applied inertia: seatbelts, headrests, airbags, and crumple zones all exist because passengers continue at the old velocity when the vehicle's changes.
- The law is the reason space travel is cheap *once you're moving*: probes coast between planets for years on zero fuel.

## Worked Examples

**Example 1: The tablecloth trick**
A swift tug snatches the cloth; the crockery barely stirs. The plates' inertia resists sudden change, and the brief, slick contact transmits too little friction-force for too little time to accelerate them much. Pull *slowly* and friction acts longer — and dinner is on the floor. The trick is pure first law plus timing.

**Example 2: Reading a cruising cyclist (now with the law)**
Last topic you concluded a constant-velocity cyclist has zero resultant. The first law upgrades this from observation to principle: constant velocity is the *default*; her pedalling isn't "causing motion", it's cancelling drag and friction so that the default can continue. Stop pedalling and the default doesn't fail — the resultant goes backward and *changes* her motion.

**Example 3: The headrest's job**
Your car is rear-ended. The seat shoves your torso forward (a real force, from the seat); your head, attached only by your neck, tends to *stay put* — so it lags backward relative to your accelerating body: whiplash. The headrest exists to push your head along with everything else. Inertia injuries are treated by giving the body part its own force-provider.

## Common Mistakes

- **"A forward force keeps it cruising"** — at constant velocity the forward and backward forces cancel; nothing is winning.
- **"The passengers were thrown forward by a force"** — they were *not pushed*; they merely continued while the vehicle changed. Hunt the force and you'll find none — that's the point.
- **Confusing inertia with momentum or weight** — inertia is mass: the resistance to change. It exists fully in weightless orbit and in objects at rest.
- **"Objects eventually run out of motion"** — motion isn't a consumable; only forces (friction, drag) take it away.
- **Treating rest as special** — rest is just constant velocity at zero; the law covers both in one breath.

## Mental Model

Think of every object as a **stubborn ledger-keeper of its own velocity**. Whatever entry stands in the ledger — "0 m/s" or "5 km/s north-east" — the keeper preserves it indefinitely and changes it only when presented with a signed warrant: a resultant force. Friction and drag are warrants too, just ones nobody remembers issuing. The heavier the ledger (mass), the more grudging each amendment. Earth's surface is a bureaucracy thick with unnoticed warrants; deep space is an office where no paperwork ever arrives, and the old entry stands forever.

## Mini Summary

- ✔ No resultant force → velocity unchanged: rest stays rest, motion coasts on
- ✔ Only *changes* of motion need causes; motion itself needs none
- ✔ Inertia = resistance to change, measured by mass — independent of weight
- ✔ Friction and drag are why Earth-life teaches the wrong lesson
- ✔ Constant velocity and rest are the same physics: zero resultant

# Guided Practice Quest

Work through the guided steps to coast with Voyager, acquit the braking bus of "throwing" anyone, and pin inertia to its true measure.

# Solo Practice Quest

Collect three pieces of first-law evidence from your own day, one of each kind: (1) an object that "stopped by itself" — identify the hidden force that actually stopped it; (2) a lurch — bus, car, lift, or train — where your body's inertia was exposed; describe whose motion changed and whose didn't; (3) one situation where something *kept moving* embarrassingly well (ice, a rolling jar, a sliding phone). For each, write the first-law account in two sentences, explicitly naming every real force. End with one sentence on how a friction-free world would change one daily activity.

# Integration

**History**: The first law marks one of thought's great pivots — from Aristotle's "motion requires a mover" through Galileo's inclined-plane reasoning to Newton's *Principia* (1687). It is a case study in how civilisations can be systematically misled by universal-but-invisible conditions, and what it takes to see past them.

**Biology**: Your vestibular system and your reflexes evolved on the friction-rich savannah, not in Newton's vacuum — which is why your instincts brace wrongly in cars and lifts, and why astronauts need months to retrain movement in orbit. Intuition is calibrated to a special case.

# Lore Conclusion

On the walk back from the pond, Thorne is quiet until the Observatory gates. "The puck taught you the law's first half — motion coasting on. Your boots taught the second." You look down: ice still clinging to the soles that gripped so poorly all morning. "Every slip was a moment without the friction your stride assumes. Your legs are first-law engines, apprentice; they just never knew it." Inside, he chalks tomorrow's title on the board and underlines one symbol twice: **F = ma**. "We now know *when* motion changes — when a resultant acts. Tomorrow, the masterstroke: exactly *how much* it changes. One short equation. It will carry you, if you let it, all the way to the planets."
