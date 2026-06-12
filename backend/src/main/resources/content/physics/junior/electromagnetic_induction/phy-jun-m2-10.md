---
id: phy-jun-m2-10
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m2
moduleTitle: "Module 2: Electricity and Magnetism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: electromagnetic_induction
topicTitle: "Electromagnetic Induction"
topicSortOrder: 4
title: "Inducing a Voltage"
sortOrder: 10
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Describe electromagnetic induction — voltage from changing magnetic exposure
  - State the factors that increase induced voltage
  - Explain why only CHANGE induces, and the direction rule (opposition)
integrationDomains: [history, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - States that a voltage is induced when a conductor's magnetic exposure changes (relative motion or changing field)
    - Lists the boosters — faster change, stronger field, more turns
    - States that steady fields induce nothing; only change counts
    - Describes the opposition rule (induced effects fight the change causing them)
  keywords: [induction, induced voltage, change, relative motion, turns, faster, oppose, Faraday]
  modelAnswer: |
    Electromagnetic induction is the motor effect's inverse: move a magnet near a coil — or a
    conductor through a field — and a voltage is induced, driving current if the circuit is
    closed. Only CHANGE induces: a magnet resting inside a coil, however strong, induces
    nothing; thrust it in or out and the meter kicks. Induced voltage grows with the speed of
    change, the field's strength, and the coil's turn count. The direction always opposes the
    change that caused it (Lenz's law) — push a magnet in and the coil's induced current makes
    a field that pushes back — which is energy conservation enforcing that electrical energy
    must be paid for by the work of whoever is doing the pushing.
guidedSteps:
  - id: phy-jun-m2-10-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A strong magnet rests motionless inside a coil connected to a sensitive meter. The meter reads:
    inputConfig:
      options:
        - "A large steady current"
        - "Zero — only CHANGING magnetic exposure induces; rest induces nothing"
        - "A small steady current"
        - "Alternating current"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Zero — only CHANGING magnetic exposure induces; rest induces nothing"]
      rejectedFeedback: "Induction's iron precondition is CHANGE: motion of magnet or coil, growth or collapse of a field. A stationary magnet — however mighty — parked in a coil forever generates precisely nothing. Faraday's first great surprise."
    hint: "What single word governs all of induction?"
    reflectionPrompt: "What are the three distinct ways you could make the meter kick without touching the circuit?"
  - id: phy-jun-m2-10-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      To induce a LARGER voltage when plunging a magnet into a coil, you can:
    inputConfig:
      options:
        - "Plunge faster, use a stronger magnet, wind more turns"
        - "Plunge slower and use thinner wire"
        - "Warm the coil"
        - "Use a copper magnet"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Plunge faster, use a stronger magnet, wind more turns"]
      rejectedFeedback: "The three boosters: rate of change (speed), field strength, and turns (each loop adds its share). Generators pull all three levers — and add iron cores and clever geometry on top."
    hint: "Mirror of the electromagnet's three levers — with 'speed of change' replacing 'current'."
    reflectionPrompt: "Why does plunging the magnet OUT induce just as well as in — and what differs about the result?"
  - id: phy-jun-m2-10-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Push a magnet's north pole toward a coil and the coil's induced current flows so that its near face becomes a NORTH pole — repelling your push. Explain why it must be this way, using energy conservation. (3–4 sentences.)
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [oppose, Lenz, work, energy, free, conservation, repel, pay]
      rejectedFeedback: "Suppose the coil ATTRACTED your magnet instead: the magnet would accelerate in, inducing more current, attracting harder — electrical energy AND kinetic energy multiplying from nothing. Conservation forbids it. The induced current must OPPOSE the change (Lenz's law), so you must do work against the repulsion — and that work is precisely the electrical energy generated. Generators are hard to turn for the same honest reason."
    hint: "Imagine the opposite (attraction). What free lunch would that serve?"
    reflectionPrompt: "What does Lenz's law predict you'll FEEL when cranking a generator under heavy load versus none?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Electromagnetic induction produces a voltage when:"
    options:
      - "A conductor sits in a strong steady field"
      - "The magnetic exposure of a conductor CHANGES — by relative motion or a changing field"
      - "A wire is coiled tightly"
      - "Current flows through a resistor"
    correctIndex: 1
    feedback: "Change is everything: move the magnet, move the coil, or grow/collapse the field (an electromagnet switching nearby) — all induce. Stillness, however strong, induces nothing."
  - type: MULTIPLE_CHOICE
    question: "Lenz's law (the opposition rule) is really a statement of:"
    options:
      - "Friction"
      - "Energy conservation — induced effects oppose their cause so that electrical energy is paid for by work"
      - "Ohm's law"
      - "Magnetic fatigue"
    correctIndex: 1
    feedback: "If induction AIDED its cause, magnets would self-accelerate into coils minting free energy. Opposition makes the pusher pay — every joule out of a generator is a joule of somebody's work in."
---

# Hook

In 1831, Michael Faraday — a blacksmith's son, bookbinder's apprentice, owner of no university degree — wrapped two coils of wire around an iron ring, connected one to a battery and the other to a meter, and noticed something everyone else would have dismissed: the meter *twitched*. Only at the moments of connection and disconnection — never in between. Steady current nearby: nothing. *Changing* current nearby: a kick.

He chased that twitch for weeks — magnet into coil, kick; magnet at rest, nothing; magnet out, opposite kick — until the law stood clear: **change in magnetic exposure creates voltage**. It is, by economic measure, plausibly the most valuable single discovery ever made: every power station on Earth — coal, nuclear, hydro, wind — is a machine for moving magnets past coils. Asked (legend says) by a politician what use this electricity might be, Faraday replied: *"One day, sir, you may tax it."* Today, the twitch that lights the world.

# Lore Introduction

Hale's bench bears the inverse of yesterday's kit, as promised: the same coil, the same horseshoe magnet — but no battery anywhere. Only the Tower's most sensitive meter, its needle resting at dead centre. "Yesterday, current made motion. The Tower's founders spent a DECADE trying to run the miracle backwards — magnetism into current. They parked the mightiest lodestones in the deepest coils and watched needles sit still for months." She hands you the bar magnet. "They failed because they parked. Go on — *park* it in the coil." You do: nothing, exactly as history promised. "Now," says Hale, and her voice drops to the register she saves for the Tower's true secrets, "*move* it." The needle leaps. You freeze; it dies. Out — it leaps the other way. In, out, in: the needle dances to your hand. "The river runs uphill, junior — but only for those who keep moving. Welcome to Faraday's twitch."

# Core Learning

## Concept Introduction

**Induction: the inverse miracle.** When a conductor's **magnetic exposure changes** — formally, the field through a circuit's loop — a voltage is **induced** across it (driving a current, if the circuit is closed). Three equivalent ways to change exposure:

1. **Move the magnet** (in/out of the coil)
2. **Move the conductor** (wire swept through a field)
3. **Change a nearby field's strength** (switching an electromagnet — Faraday's iron ring; no motion at all!)

**The iron precondition: CHANGE.** Steady fields induce nothing — not weakly: *nothing*. The decade of parked lodestones failed by design. (Deep symmetry alert: currents *make* fields; *changing* fields make currents. The two facts will marry spectacularly at Senior tier — their child is light.)

**The three boosters** (mirror of the electromagnet's levers):
- **Faster change** — speed of plunge, rate of field growth
- **Stronger field** — bigger magnet
- **More turns** — each loop contributes its share to the total voltage

**The opposition rule (Lenz's law).** The induced current always flows so as to **oppose the change creating it**: push a north pole in, and the coil's face becomes north to repel you; pull it out, the face flips south to cling. Why *must* it? Run the alternative: an attracting coil would suck the magnet in faster, inducing more, attracting harder — kinetic *and* electrical energy minted from nothing. Forbidden. Opposition makes the mover *pay*: the work you do against the magnetic pushback **is** the electrical energy you generate. (Feel it: a hand-cranked torch cranks easy with the lamp off, stiff with it on — the load reaches back up the wires into your wrist.)

**Direction tool:** Fleming's **right** hand (generators) mirrors yesterday's left (motors): First finger Field, thuMb Motion, seCond finger reports the induced Current.

## Why It Matters

- Essentially all grid electricity — coal, gas, nuclear, hydro, wind — is induction: something spins magnets past coils. Tomorrow's generator lesson is this one with an axle.
- Induction's gadget empire: dynamos, microphones, electric-guitar pickups, induction hobs, wireless chargers, card-swipe readers, metal detectors, regenerative braking.
- Lenz's law is the deepest everyday demonstration that energy conservation has *teeth*: nature audits even your wrist on a crank handle.

## Worked Examples

**Example 1: The shake torch, audited**
A "shake flashlight": magnet sliding through a coil with each shake, charging a capacitor. One shake = one in-kick + one out-kick (opposite signs — the electronics rectify them). Shake twice as fast: bigger voltage (booster one). Premium model: more turns, stronger magnet (boosters two, three). Note your wrist's report: shaking feels *stiffer* with the circuit charging — Lenz invoicing you per shake.

**Example 2: The guitar pickup — induction as art**
Under each steel guitar string sits a small magnet wound with thousands of turns. The magnet magnetises the string's nearest segment; when the string vibrates, that moving magnetised metal changes the coil's exposure hundreds of times a second — inducing a voltage that *is* the note, electrically. No battery in the guitar: the string's vibration (your strum's energy) pays for every millivolt, Lenz-style. Amplifiers merely enlarge what induction transcribed.

**Example 3: The induction hob's sealed sorcery**
Beneath the glass, a coil driven with rapidly alternating current — a field growing and collapsing thousands of times per second. Set an iron pan on top: the changing field induces swirling currents *in the pan's own metal* (eddy currents), and the pan's resistance turns them to heat — the pan IS the element. Glass stays cool (insulator — nothing induced), copper pans disappoint (wrong magnetic character), and the demonstration of method three — induction with nothing moving — boils your pasta.

## Common Mistakes

- **Expecting strength to substitute for change** — the mightiest magnet at rest induces zero; a modest one in brisk motion lights LEDs. Rate rules.
- **Forgetting the sign flips** — in-stroke and out-stroke induce opposite ways; a steadily-shaken magnet makes AC, not DC (tomorrow's plot point).
- **Mixing the hands** — LEFT for motor (current→force), RIGHT for generator (motion→current); Fleming's fingers are job-specific.
- **Treating Lenz as optional flavour** — opposition is the energy ledger itself; "easy-cranking loaded generator" is a perpetual-motion claim in overalls.
- **"Induction needs a magnet moving"** — method three (changing a nearby field electrically) involves no motion whatever: transformers (two lessons hence) live entirely there.

## Mental Model

A coil is **a turnstile that charges admission on change itself**. Stand still inside it — magnet parked, field steady — and the turnstile sleeps; residency is free. But *pass through* — magnet entering, leaving, field rising, collapsing — and the turnstile spins, generating with every click... and pushing back against your passage in exact proportion (Lenz the gatekeeper), so that every electrical click is paid for in muscle. Generators are turnstiles spun by waterfalls and steam; pickups are turnstiles ticked by trembling strings; induction hobs install the turnstile inside the pan itself. And the only customer the gate never charges — never even notices — is the one who stands still.

## Mini Summary

- ✔ Changing magnetic exposure induces voltage: move the magnet, move the wire, or change the field
- ✔ Stillness induces nothing — the change is the commodity
- ✔ Boosters: faster change, stronger field, more turns
- ✔ Lenz's law: induced effects oppose their cause — conservation collecting payment from the mover
- ✔ Fleming's RIGHT hand for generators; in/out strokes alternate sign (AC foreshadowed)

# Guided Practice Quest

Work through the guided steps to let a parked magnet earn its zero, pull the three boosters in order, and prove from a forbidden free lunch that the coil must always push back.

# Solo Practice Quest

Replay Faraday's month, kitchen edition: (1) *The twitch*: wind 50+ turns of insulated wire and connect to the most sensitive meter you can manage (a multimeter on mV, or an LED for drama with vigorous shakes); plunge a strong magnet in and out — document in-kick, out-kick, park-silence, and the speed effect. (2) *Boosters*: double your turns and repeat; compare honestly (uncertainties — Module One forever). (3) *Lenz felt*: drop a strong magnet through a copper or aluminium tube (non-magnetic!) beside an identical drop outside the tube — time both, marvel, and explain the slow fall via induced eddy currents opposing the magnet's motion. Close with a paragraph: choose any induction gadget from your life (hob, wireless charger, bike dynamo, card reader) and tell its story in the lesson's grammar — what changes, what's induced, who pays.

# Integration

**History**: Faraday's 1831 notebooks — and his insistence on picturing fields as real lines, mocked at the time — set physics' direction for a century: Maxwell mathematised Faraday's lines into the equations that predicted radio. The bookbinder outran every credentialed rival; the Tower keeps his portrait, Hale will tell you, "to remind juniors that apparatus respects nobody's certificates."

**Engineering**: Induction engineering spans twelve orders of magnitude: gigawatt alternators, regenerative braking recapturing a tram's momentum into the grid, eddy-current brakes (Lenz as a silent retarder on lorries and rollercoasters), metal detectors pinging on induced ghosts, and the wireless charger's million-times-a-second handshake with your phone. All of it: turnstiles, variously spun.

# Lore Conclusion

By evening your kitchen-coil dances on command — in-kick, out-kick, the doubled-turns booster verified, and the magnet's eerie slow-motion fall through Hale's copper tube explained to her standards ("the tube grieves its passage, junior — and grief, in this Tower, is measurable"). She enters your twitch-log into the registry beside the founders' decade of failures, which she insists stay displayed: "The parked years teach more than the leap." Then she crosses to the window and pulls back the curtain — and you realise the Tower's evening hum, the sound you've lived inside for weeks, has a source: below, in the yard, a water-wheel turns a shaft that disappears into the Tower's foundations. "You've met the twitch," Hale says. "Tomorrow we harness it to that wheel and never let it stop. Continuous induction, junior — the generator — and the small, splendid problem of what KIND of current a spinning coil insists on making. Sleep well. Tomorrow the Tower shows you its heart."
