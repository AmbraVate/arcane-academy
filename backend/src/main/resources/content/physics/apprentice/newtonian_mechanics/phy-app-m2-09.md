---
id: phy-app-m2-09
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m2
moduleTitle: "Module 2: Mechanics Fundamentals"
moduleGlyph: "🍎"
moduleSortOrder: 2
topicSlug: newtonian_mechanics
topicTitle: "Newtonian Mechanics"
topicSortOrder: 3
title: "Newton's Third Law: Action and Reaction"
sortOrder: 9
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - State Newton's third law precisely (equal, opposite, on different objects)
  - Identify correct action–reaction pairs
  - Explain why third-law pairs never cancel each other
integrationDomains: [biology, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States the law with all three conditions — equal size, opposite direction, acting on DIFFERENT objects
    - Identifies the correct reaction partner for at least three forces
    - Explains why a pair cannot cancel (different receivers)
    - Distinguishes third-law pairs from balanced forces on one object
  keywords: [action, reaction, pair, equal, opposite, different objects, cancel]
  modelAnswer: |
    Newton's third law: when object A exerts a force on object B, B simultaneously exerts a
    force on A of equal size and opposite direction. The pair always acts on different
    objects — foot pushes ground backward, ground pushes foot forward — so the two can never
    cancel each other; cancellation requires forces on the SAME object. The weight of a book
    and the table's upward push are NOT a third-law pair (both act on the book); the book's
    true partner forces are the book pulling the Earth up and the book pressing the table down.
guidedSteps:
  - id: phy-app-m2-09-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A swimmer pushes water backward with her feet. The third-law reaction is:
    inputConfig:
      options:
        - "The water resists her motion with drag"
        - "The water pushes her feet forward with an equal force"
        - "Her buoyancy holds her up"
        - "Gravity pulls her down equally"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The water pushes her feet forward with an equal force"]
      rejectedFeedback: "Swap the two objects and reverse the direction: she pushes water backward ⇒ water pushes HER forward. That forward push is what swimming IS. Drag, buoyancy, and gravity are different forces with their own partners."
    hint: "The reaction to 'A pushes B backward' is always 'B pushes A ...'?"
    reflectionPrompt: "Using the same logic: what do a rocket's exhaust gases push on?"
  - id: phy-app-m2-09-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A book rests on a table. Its weight (Earth pulling book down) and the table's upward push on it are equal and opposite. Are they a third-law pair?
    inputConfig:
      options:
        - "Yes — equal and opposite is exactly the definition"
        - "No — both act on the same object (the book); a third-law pair always acts on two different objects"
        - "Yes, but only while the book is stationary"
        - "No — they aren't really equal"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["No — both act on the same object (the book); a third-law pair always acts on two different objects"]
      rejectedFeedback: "The classic trap! Both forces act ON THE BOOK — that makes them balanced forces, not a pair. The weight's true partner is the book pulling the EARTH up; the table-push's partner is the book pressing the TABLE down."
    hint: "Check the receivers: a genuine pair never shares one."
    reflectionPrompt: "Why does the distinction matter? (What happens to the 'pair' in a falling lift, versus real pairs?)"
  - id: phy-app-m2-09-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      "If every action has an equal and opposite reaction, the forces cancel, so nothing should ever accelerate!" In 2–3 sentences, dismantle this argument.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [different objects, same object, cancel, each object, separate]
      rejectedFeedback: "The two forces act on DIFFERENT objects, and each object's motion is decided only by forces on ITSELF. The ground's forward push on the runner accelerates the runner; her backward push acts on the planet. Forces on different ledgers never cancel."
    hint: "Whose free-body diagram does each force of the pair appear in?"
    reflectionPrompt: "When you step off a boat onto a dock, what does the third law do to the boat — and why is it noticeable here but not when stepping off a continent?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A horse pulls a cart. The cart pulls back on the horse equally (third law). The pair moves forward because:"
    options:
      - "The horse pulls slightly harder than the cart pulls back"
      - "Each body responds only to forces on itself: the ground's forward push on the horse's hooves exceeds the cart's backward pull on the horse"
      - "The third law switches off for living things"
      - "The cart's pull acts with a delay"
    correctIndex: 1
    feedback: "The pair is exactly equal, always — but they act on different bodies. The horse accelerates because forces ON THE HORSE (ground push forward vs cart pull back) leave a forward resultant."
  - type: MULTIPLE_CHOICE
    question: "A rocket in empty space accelerates by:"
    options:
      - "Pushing against the air behind it"
      - "Pushing exhaust gas backward, so the gas pushes the rocket forward"
      - "Burning fuel to become lighter"
      - "It cannot accelerate without something to push on"
    correctIndex: 1
    feedback: "The rocket throws mass (exhaust) backward; the exhaust's reaction pushes the rocket forward. Nothing external is needed — which is precisely why rockets work in vacuum."
---

# Hook

You cannot touch without being touched. Punch a wall and the wall punches your knuckles with *precisely* the force you delivered — no more, no less, and at the exact same instant. The universe has never once, in any interaction ever observed, delivered a one-way force.

This sounds like poetry but it's bookkeeping, and its consequences run from the everyday to the interplanetary: it is why guns kick, why you can walk at all (you throw the Earth backward with every step — it just doesn't make the news), and why rockets — to the bafflement of a 1920 New York Times editorial that later had to be retracted during Apollo 11 — work *better* in empty space, with nothing to "push against".

Newton's third law is also the most misquoted law in physics. Getting it exactly right — equal, opposite, and *on different objects* — is what separates those who recite it from those who can use it.

# Lore Introduction

Thorne stages today's lesson on the mirror-pond's ice, with two wheeled chairs from the infirmary. He seats you in one, himself in the other, facing you, and hands you a pole. "Push me away. Hard." You plant the pole against his chair and shove — and both chairs glide apart, his *and yours*, in opposite directions. "Again," he says, "but this time only push me. Keep yourself still." You try. You fail. You try bracing, twisting, pushing faster — every shove you deliver, you receive. From across the widening ice, Thorne calls out, not unkindly: "Nine hundred years, apprentice, and no one in this Academy has ever managed it. The greatest swordsmen, the strongest smiths. Not once. The universe does not sell pushes singly. It only sells them in pairs."

# Core Learning

## Concept Introduction

**Newton's Third Law.** When object A exerts a force on object B, then B simultaneously exerts a force on A that is:

- **equal in magnitude**,
- **opposite in direction**,
- **of the same type** (gravity answers gravity, contact answers contact),
- and — the load-bearing clause — **acting on the other object**.

To find any force's partner, apply the swap rule: *"A pushes B [direction]"* becomes *"B pushes A [opposite direction]"*. Foot pushes ground backward ⇄ ground pushes foot forward. Earth pulls apple down ⇄ apple pulls Earth up (yes, really — the Earth rises to meet the apple by an immeasurably small amount; m is large, so a = F/m is tiny).

**Why pairs never cancel.** Each object's motion is governed *only by forces acting on it* (its own free-body diagram). The pair's two forces appear on two *different* diagrams — one on yours, one on the wall's — so they can never meet to cancel. Cancellation is the business of *balanced forces*, which act on one object.

**The booby-trap: balanced forces masquerading as pairs.** Book on table: weight down (Earth→book) and normal force up (table→book) are equal, opposite... and both on the book. **Not a pair** — just balance. The tell: a real pair survives any situation (drop the book — Earth still pulls book, book still pulls Earth, equally), while the "false pair" breaks (in free fall the table force is gone entirely).

**Propulsion is the third law monetised.** Walking, swimming, rowing, jet engines, rockets: throw something backward (ground, water, air, exhaust), and the reaction carries you forward. Rockets carry their own "something" — which is why vacuum is no obstacle.

## Why It Matters

- Misidentifying pairs is the most common conceptual error in mechanics exams and in popular science alike; the swap rule inoculates you.
- All propulsion engineering — tyres, propellers, turbines, ion drives — is third-law design: choose what to push backward, and how hard.
- Recoil management (firearms, fire hoses, spacecraft docking, even a basketball jump shot) is the law's practical bill arriving.

## Worked Examples

**Example 1: The gun and the kick**
A rifle fires a 10 g bullet. Rifle pushes bullet forward; bullet pushes rifle backward, *equally*. Equal forces — wildly unequal consequences: a = F/m gives the light bullet enormous acceleration and the heavy rifle (plus braced shoulder) a modest kick. The third law sets the forces equal; the *second* law shares out the drama by mass.

**Example 2: The horse-and-cart paradox, dissolved**
"The cart pulls back on the horse as hard as the horse pulls the cart — so they can't move!" Audit by object. Forces ON THE CART: horse's pull forward, friction backward → forward resultant → cart accelerates. Forces ON THE HORSE: cart's pull backward, *ground's push forward on its hooves* (reaction to hooves pushing ground backward) → forward resultant → horse accelerates. Every pair intact, everything moves. The paradox only ever lived in mixing two objects' ledgers.

**Example 3: Stepping off the boat**
You step from a small boat to the dock: your foot pushes the boat backward, the boat pushes you forward — and the boat, light and frictionless on water, glides treacherously away mid-step. Stepping off a *continent* involves the identical pair; the continent's m makes its a invisible. Same law, different masses, very different swim.

## Common Mistakes

- **"Equal and opposite forces on the book are a third-law pair"** — both act on one object; that's balance, not a pair. Check the receivers.
- **"The pair cancels, so no motion"** — the forces live on different objects' diagrams; they can't cancel each other.
- **"The stronger party pushes harder"** — never: truck vs fly, hammer vs nail, champion vs novice — contact forces are exactly mutual. Outcomes differ by mass and structure, not by force inequality.
- **"Rockets push on the air"** — they push on their own exhaust; vacuum suits them fine.
- **Forgetting reactions exist for non-contact forces** — you gravitationally pull the Earth upward right now, with your full weight.

## Mental Model

Forces are **transactions, never gifts**. Every push is a payment that debits both accounts at once: A's ledger records "+F from B", B's records "+F from A, opposite direction" — same amount, same instant, different books. You cannot pay someone without your own account being touched; you cannot push the world without the world pushing back through the same point of contact. To predict any object's motion, audit *its* book alone — but to understand where forces come from, follow the double-entry.

## Mini Summary

- ✔ Every force has a partner: equal size, opposite direction, same type, *other object*
- ✔ Swap rule finds partners: "A pushes B" ⇄ "B pushes A"
- ✔ Pairs never cancel — cancellation needs forces on one object (that's balance)
- ✔ Weight-vs-table is balance; the true partners involve the Earth and the table's surface
- ✔ All propulsion = throwing something backward and accepting the reaction

# Guided Practice Quest

Work through the guided steps to propel a swimmer, defuse the book-on-table trap, and dismantle the "everything cancels" paradox for good.

# Solo Practice Quest

Run a third-law audit on four scenes: (1) you standing still on the floor — list ALL forces present and arrange them into true pairs plus the balanced set on you; (2) a sprinter's start — trace the chain from muscle to ground to forward motion, naming both members of each pair; (3) a helicopter hovering — what is pushed which way, and what pushes back; (4) two ice skaters, one large and one small, pushing apart from rest — predict who moves faster and defend it with both the third law (equal forces) and the second (unequal masses). For each scene, one deliberate trap: write down one equal-and-opposite *non-pair* and label why it fails the pair test.

# Integration

**Biology**: Locomotion is biology's third-law portfolio: feet push earth, fins push water, wings push air. Watch slow-motion footage of a pigeon's downstroke or a sprinter's blocks and you are watching reaction forces being farmed. Even your heartbeat recoils — ballistocardiography measures the body's tiny third-law shudder with each ejection of blood.

**Engineering**: Jet and rocket propulsion are quantified by exactly how much mass is thrown backward how fast (you'll meet this as momentum next tier). Civil engineers, meanwhile, live the law statically: every newton a bridge presses into its foundations, the foundations press back — and the soil report better agree.

# Lore Conclusion

You and Thorne glide to opposite banks of the pond, the third law having distributed the morning's lesson equally between you. Back in the Hall of Mechanisms, he closes the module's ledger with rare ceremony. "Three laws," he says. "Motion persists unless forced to change. Change comes as F = ma, paid in resultants. And every force is half of a handshake." He shelves the ledger beside eight centuries of its predecessors. "Together they are sufficient — hear me, apprentice — *sufficient* to chart a cannonball, a cathedral, or a comet. Next we ask what all this pushing accomplishes — and we will need the Academy's oldest currency to count it. They call it *energy*, and unlike force, the universe never mints a newton-metre of it from nothing."
