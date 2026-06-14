---
id: phy-app-m2-01
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: forces
topicTitle: "Forces"
topicSortOrder: 1
title: "What Is a Force?"
sortOrder: 1
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - Define a force as a push or pull between two objects
  - Identify the agent and receiver of any force
  - Describe what forces can do — change motion or change shape
integrationDomains: [mathematics, biology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines a force as a push or pull measured in newtons
    - Names agent and receiver for at least three real forces
    - Distinguishes the two effects of forces (changing motion, changing shape)
    - Recognises that forces always involve two objects
  keywords: [force, push, pull, newton, agent, interaction, motion, deform]
  modelAnswer: |
    A force is a push or a pull exerted by one object on another, measured in newtons. Every
    force has an agent (the pusher) and a receiver (the pushed): the foot kicks the ball, the
    Earth pulls the apple, the table presses up on the book. Forces do two things: change an
    object's motion (speed it up, slow it down, change its direction) or change its shape
    (stretch, squash, bend). A force is never a property of one object alone — it is always
    an interaction between two.
guidedSteps:
  - id: phy-app-m2-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A football flies through the air after being kicked. Which statement about forces is correct *while it flies*?
    inputConfig:
      options:
        - "The kick force travels with the ball, slowly running out"
        - "The kick ended at contact; in flight the main forces on the ball are gravity and air resistance"
        - "The ball carries no forces at all once airborne"
        - "The foot's force keeps acting until the ball lands"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The kick ended at contact; in flight the main forces on the ball are gravity and air resistance"]
      rejectedFeedback: "Forces exist only during the interaction. Once contact ends, the kick is over — the ball flies on because of its motion, acted on now by gravity and air resistance. 'Force stored in the ball' is the single most common misconception in mechanics."
    hint: "A force needs two objects interacting. Once foot and ball separate, can the kick still act?"
    reflectionPrompt: "If the kick's force ended at contact, why doesn't the ball stop immediately?"
  - id: phy-app-m2-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "A book rests on a table. The Earth pulls the book down; the ________ pushes the book up."
    inputConfig:
      placeholder: "table"
    markingRule:
      matchMode: CONTAINS
      accepted: [table]
      rejectedFeedback: "The table pushes up on the book (the support or 'normal' force). Every force names two objects — agent and receiver."
    hint: "What is the book in direct contact with?"
    reflectionPrompt: "How do we know the table pushes at all? What would happen if it suddenly couldn't?"
  - id: phy-app-m2-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Forces can change motion or change shape. Give one everyday example of each effect, naming the agent and receiver in both. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [motion, shape, squash, stretch, bend, speed, push, pull]
      rejectedFeedback: "Examples: a cyclist's legs (agent) accelerate the bike (receiver) — motion change; hands (agent) squeeze a sponge (receiver) — shape change. Name who pushes and what gets pushed."
    hint: "Think of speeding up / slowing / turning for motion; stretching / squashing / bending for shape."
    reflectionPrompt: "Can one force do both at once? Picture a tennis racket meeting a ball in slow motion."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A force is best described as:"
    options:
      - "A property an object owns, like its mass"
      - "A push or pull exerted by one object on another"
      - "The energy inside a moving object"
      - "Anything measured in kilograms"
    correctIndex: 1
    feedback: "A force is an interaction — one object pushing or pulling another. It is never a possession of a single object."
  - type: MULTIPLE_CHOICE
    question: "Which is NOT something a force can do?"
    options:
      - "Speed an object up"
      - "Change an object's direction"
      - "Change an object's shape"
      - "Exist with only one object involved"
    correctIndex: 3
    feedback: "Forces always come from interactions between two objects. Speeding up, redirecting, and deforming are exactly what forces do."
---

# Hook

Right now, as you read this, you are being pushed and pulled by at least three forces — and you can't feel most of them. The entire Earth is pulling you downward. Your chair is pushing you upward with a force precisely tuned to match. The air is squeezing you from all sides with roughly the weight of a small car spread over your skin.

You notice none of it, because forces themselves are invisible. We only ever see what they *do*: the apple falls, the bowstring bends, the cyclist accelerates. For thousands of years, humans pushed and pulled without ever pinning down what a push *is* — and the moment physics did pin it down, machines, bridges, and eventually rockets followed.

The definition fits in a sentence. The consequences fill the rest of this pathway.

# Lore Introduction

Module Two begins not in the Observatory's towers but in its undercroft: the Hall of Mechanisms, where the Academy keeps nine centuries of machines. Thorne stops at the oldest exhibit — a simple wooden lever beside a boulder, unmoved for generations. "Every apprentice wants to begin with the orrery, the clockwork, the flying machines," he says. "They are all this lever, repeated cleverly." He sets your palm flat against the boulder. "Push." You push; nothing. "Now answer me precisely: what just happened between your hand and the stone? Not poetry — *mechanics*. What passed between them?" You have no word for it yet that survives his scrutiny. "Good," says Thorne. "Then we may begin properly. The word is *force*, and it is the most carefully defined push in history."

# Core Learning

## Concept Introduction

A **force** is a push or a pull exerted by one object on another. It is measured in **newtons (N)** — about the pull of Earth's gravity on a small apple. Force is a vector: 10 N *downward* and 10 N *upward* are different forces entirely.

Every force has two ends — it is an **interaction**, never a possession:

| The force | Agent (pushes/pulls) | Receiver (is pushed/pulled) |
|-----------|---------------------|------------------------------|
| Kick | Foot | Ball |
| Weight | The Earth | The apple |
| Support | Table | Book |
| Tension | Rope | Sledge |

If you cannot name *both* objects, you haven't identified a real force. ("The ball's force of motion" fails this test — motion names only one object, and indeed it is not a force at all.)

**What forces do.** Exactly two things:

1. **Change motion** — start it, stop it, speed it, slow it, or bend its direction.
2. **Change shape** — stretch, squash, bend, or twist the receiver.

Note what is *not* on the list: keeping an object moving. A flying ball needs no ongoing push — that discovery, and why it feels so wrong, is where the next lessons are headed.

A force acts **only during the interaction**. When foot leaves ball, that force is finished — it is not stored in the ball, does not travel with it, does not fade away gradually. In flight, the ball's companions are new forces: gravity and air resistance.

## Why It Matters

- Forces are the vocabulary of all mechanics — every machine, structure, joint, and vehicle is analysed as forces between pairs of objects.
- The agent–receiver discipline is the foundation of free-body diagrams (next lessons), the single most-used tool in physics and engineering.
- The "force is stored in moving objects" misconception is the most widespread error in physics; clearing it now unlocks Newton's laws later.

## Worked Examples

**Example 1: The audit of a resting book**
A book sits on a table. Forces on the book: Earth pulls it down (weight, ~2 N for a paperback); table pushes it up (support force, ~2 N). Two forces, two named agents. The book's rest is not the absence of forces — it is their *balance*.

**Example 2: Where is the force? (archery)**
Drawing a bow: your arm (agent) pulls the string (receiver) — the string and limbs change *shape*, storing the effort. Release: string (agent) pushes arrow (receiver) — violent *motion* change over a few centimetres. After separation: no more string force; the arrow flies under gravity and air resistance alone. Three phases, three different force stories.

**Example 3: Forces you forget exist**
Stand still on the floor. The Earth pulls you down (say 700 N). You don't accelerate downward through the floorboards — so the floor must push you *up* with 700 N. Surfaces push; this surprises everyone at first. Proof by removal: step off a diving board and note what changes — only the floor's push is gone, and motion changes instantly.

## Common Mistakes

- **"The throw's force keeps the ball moving"** — no; forces act only during contact. Motion continues on its own (Newton's first law, coming soon).
- **Naming forces with one object** — "the force of the ball" is meaningless; every force is *of* an agent *on* a receiver.
- **Forgetting that surfaces push** — tables, floors, and walls exert real upward/sideways forces; resting objects are in balance, not force-free.
- **Confusing force with energy or speed** — a fast ball has lots of motion (and energy), but not "lots of force"; force describes interactions, not states.

## Mental Model

Picture every force as a **handshake between exactly two objects** — firm, directional, and existing only while the hands are clasped. Some handshakes are obvious (foot–ball); some are subtle (table–book); one works at a distance with no visible touch at all (Earth–apple). To analyse any situation, walk in like a host at a party and ask: *who is shaking hands with whom, how firmly, and in which direction?* When the handshake ends, the force ends — whatever motion it caused is the guest's to keep.

## Mini Summary

- ✔ A force is a push or pull of one object on another, in newtons, with direction
- ✔ Every force names an agent and a receiver — no pair, no force
- ✔ Forces change motion or change shape; they are not needed to *maintain* motion
- ✔ Forces act only during the interaction — nothing is "stored in" a moving ball
- ✔ Surfaces push: a resting object is in balance, not force-free

# Guided Practice Quest

Work through the guided steps to retire the "stored force" myth, name the hidden pusher under a resting book, and catalogue the two things forces do.

# Solo Practice Quest

Conduct a force audit of one minute of your life — opening a door, pouring a drink, sitting down. Identify six distinct forces in that minute. For each, record: agent → receiver, push or pull, the direction, and the effect (motion change, shape change, or balance against another force). At least one must be a non-contact force, and at least one must be a surface pushing on something. Finish with two sentences: which force in your list would most people not realise exists, and how could you demonstrate that it's real?

# Integration

**Mathematics**: Forces are vectors, and everything from Lesson 11 applies: they add head-to-tail, cancel when opposed, and demand directions in every answer. The newton itself is a derived unit you can already unpack: kg·m/s².

**Biology**: Your body is a force laboratory — muscles only ever *pull* (never push!), bones act as levers, and the sensation of effort is your nervous system's force gauge. Biomechanics applies this lesson's vocabulary to every step you take.

# Lore Conclusion

In the Hall of Mechanisms, Thorne chalks two words above the unmoved boulder: *agent, receiver*. "Every machine in this hall — the cranes, the clockwork, the watermill models — is nothing but handshakes arranged in clever chains," he says. "Name the two ends of every handshake and no mechanism will ever mystify you again." He pauses at a case holding a lodestone and an iron ring, hovering a finger-width apart, touching nothing. "And some handshakes," he adds, "reach across empty air. Tomorrow we meet the family of forces — including the one that is, at this very moment, holding you to the world."
