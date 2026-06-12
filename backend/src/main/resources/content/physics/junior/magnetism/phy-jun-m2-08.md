---
id: phy-jun-m2-08
domainId: physics
tier: JUNIOR
moduleId: phy-jun-m2
moduleTitle: "Module 2: Electricity and Magnetism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: magnetism
topicTitle: "Magnetism"
topicSortOrder: 3
title: "Electromagnets"
sortOrder: 8
xpReward: 50
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Describe the magnetic field around a current-carrying wire and coil
  - State the three ways to strengthen an electromagnet
  - Compare electromagnets with permanent magnets and match each to applications
integrationDomains: [engineering, history]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Describes circular field lines around a straight current and the bar-magnet-like field of a coil
    - Lists the three strengtheners — more current, more turns, iron core
    - Gives applications exploiting switchability (scrapyard crane, relay, electric bell, MRI)
  keywords: [current, circular field, solenoid, coil, turns, iron core, switchable, relay]
  modelAnswer: |
    Oersted's accident revealed that every current makes a magnetic field: circular lines wrap
    a straight wire (direction by the right-hand grip), and winding the wire into a coil
    (solenoid) stacks those circles into a field shaped exactly like a bar magnet's — but
    switchable. Strengthen it three ways: more current, more turns, and an iron core, whose
    domains align and multiply the field hundreds of times. The electromagnet's gift over the
    permanent magnet is control — on, off, stronger, weaker, reversed — which powers scrapyard
    cranes that grip and release, relays where a small current switches a large one, electric
    bells, locks, and the superconducting coils inside MRI scanners.
guidedSteps:
  - id: phy-jun-m2-08-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Oersted's compass swung the moment current flowed in a nearby wire. The discovery this forced:
    inputConfig:
      options:
        - "Compasses are unreliable"
        - "Electric currents create magnetic fields — the two 'separate' phenomena are joined"
        - "Wires attract needles by gravity"
        - "Batteries are magnetic"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Electric currents create magnetic fields — the two 'separate' phenomena are joined"]
      rejectedFeedback: "The needle answered the CURRENT — moving charge makes a magnetic field. Three millennia of amber-and-lodestone separation ended in one lecture-hall accident: electromagnetism was born, and with it (eventually) motors, generators, and radio."
    hint: "What was the only new thing present when the needle moved?"
    reflectionPrompt: "Why is it fitting that the magnetism around a wire has no poles — just closed circles?"
  - id: phy-jun-m2-08-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which trio strengthens an electromagnet?
    inputConfig:
      options:
        - "Longer wire, thinner wire, plastic core"
        - "More current, more turns on the coil, an iron core"
        - "Less current, fewer turns, a copper core"
        - "Colder wire, shorter coil, a wooden core"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["More current, more turns on the coil, an iron core"]
      rejectedFeedback: "The three levers: current (each amp adds field), turns (each loop stacks its contribution), and an iron core (domains align with the coil's field and amplify it hundreds-fold). Scrapyard cranes pull all three levers at once."
    hint: "Two levers are about the coil's own field; the third recruits a material ally."
    reflectionPrompt: "Why does the core need to be iron rather than copper — and SOFT iron rather than steel, if you want it to switch off cleanly?"
  - id: phy-jun-m2-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A relay uses a small electromagnet to close the contacts of a separate, heavy-current circuit. Explain why this 'small current switches big current' trick is so useful, with one concrete example. (2–3 sentences.)
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [small current, large, isolat, switch, car starter, thermostat, safe, control]
      rejectedFeedback: "A relay lets a feeble, safe control signal (thin wires, low power — a dashboard switch, a thermostat, a microchip) command a heavy circuit (starter motor's 150 A, a heating bank) without the control side ever touching the dangerous current. Car ignition keys, thermostats, and every microcontroller driving real machinery work through exactly this electromagnetic handshake."
    hint: "Who gets to stay thin-wired and safe, and who does the heavy lifting?"
    reflectionPrompt: "What does a relay's clicking sound physically record?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The field around a straight current-carrying wire is best described as:"
    options:
      - "Straight lines parallel to the wire"
      - "Concentric circles wrapping the wire, direction given by the right-hand grip rule"
      - "A bar-magnet pattern"
      - "There is no field"
    correctIndex: 1
    feedback: "Circles, centred on the wire: thumb along the (conventional) current, fingers curl with the field. Coiling the wire is what stacks these circles into the bar-magnet (solenoid) pattern."
  - type: MULTIPLE_CHOICE
    question: "A scrapyard crane uses an electromagnet rather than a permanent magnet because:"
    options:
      - "Electromagnets are always stronger"
      - "It must RELEASE the load on command — switchability is the whole point"
      - "Permanent magnets are illegal"
      - "Iron only responds to coils"
    correctIndex: 1
    feedback: "A permanent magnet of that strength could lift the car — and never put it down. Current off, field off, load released: control is the electromagnet's superpower."
---

# Hook

On the 21st of April, 1820, physics professor Hans Christian Ørsted was midway through a lecture in Copenhagen when he connected a battery to a wire — and noticed, out of the corner of his eye, a compass needle on the bench *flinch*. His students later said he seemed more struck than the needle. He had reason: that flinch ended a three-thousand-year-old assumption. Electricity and magnetism — amber and lodestone, studied apart since antiquity — were *one subject*. Moving charge **makes** magnetism.

Within months, scientists across Europe were coiling wires; within five years, the electromagnet existed — a magnet you can switch off, dial up, or reverse with a knob. It now lifts wrecked cars, rings doorbells, locks doors, switches your car's starter, steers particle beams, and images your knee in an MRI tunnel. One accidental flinch; the whole electric age followed. Today you build the magnet with an off-switch.

# Lore Introduction

Hale re-stages the famous accident with ceremony: compass on the bench, wire above it, battery in her hand — and lets *you* make the connection. The needle swings smartly aside; breaks the circuit, it relaxes north again; reverse the battery, it swings the *other* way. "Three thousand years of separation, junior, ended by a man setting up his lecture demonstration in the wrong order." She produces the morning's real apparatus: a long copper wire, a stout iron nail, and a tray of the Tower's smallest test-weights. "The wire's magnetism is real but feeble — circles of influence wrapping it like ripples round a reed. The Tower's founders learned to *stack* the ripples." She begins winding the wire round the nail, turn after neat turn. "Wind with me. Count your turns. By the last one, you will hold the most obedient servant the Tower ever trained: a magnet that answers to a switch."

# Core Learning

## Concept Introduction

**Ørsted's law of the flinch: every current makes a magnetic field.** Around a straight wire, the field lines are **concentric circles** wrapping the wire — no poles, just closed loops (the monopole mystery from yesterday, answered: magnetism is made by *circulation*). Direction: the **right-hand grip rule** — thumb along the conventional current, curled fingers give the circles' direction. Reverse the current, the circles reverse.

**The solenoid: stacking the circles.** Wind the wire into a coil and each turn's circles add: inside the coil the field runs strong and straight; outside it loops round — *exactly a bar magnet's pattern*, with a north face and a south face set by the current's direction (grip rule again: fingers with the current's wind, thumb gives north). A coil is a bar magnet you can manufacture from any conductor, and unlike the bar magnet it answers to its wiring:

**The three levers:**
1. **More current** — each amp deepens every circle
2. **More turns** — each loop adds its full contribution
3. **An iron core** — the coil's field aligns the iron's domains, which add their own field: amplification by hundreds. (Soft iron for switchable service — its domains relax when the current stops; *steel* would stay magnetised: that's how permanent magnets are made on purpose.)

**The superpower: control.** Compare the two magnet families:

| | Permanent | Electromagnet |
|---|---|---|
| Field | Always on, fixed | On/off, variable, reversible |
| Needs | Nothing | Continuous current (power bill) |
| Best at | Compasses, fridge doors, motors' stators | Cranes, relays, bells, locks, MRI |

Applications are all the same sentence — *a field that obeys a circuit*: the **scrapyard crane** (grip on command, release on command); the **relay** (a whisper of current closes an iron armature, switching a torrent — every car key, thermostat, and microchip-commanding-machinery uses one); the **electric bell** (an electromagnet that breaks its own circuit as it strikes — automatic hammer-chatter); magnetic locks, maglev lift, beam-steering, and the MRI's superconducting coils (an electromagnet so strong it images you by your hydrogen).

## Why It Matters

- The current→field principle is half of electromagnetism's great engine (the other half — fields making currents — arrives in two lessons and powers civilisation).
- Electromagnets are the *interface between information and force*: every relay, solenoid valve, speaker coil, and actuator is a small current commanding the physical world — the nervous system of all machinery.
- Motors — the next lesson — are this lesson's coil plus one further trick; everything you build today is load-bearing tomorrow.

## Worked Examples

**Example 1: Your nail electromagnet, audited**
20 turns on the nail, one cell: lifts 3 paperclips. 40 turns: 6 clips (lever two — proportional). Two cells (double current): 12 (lever one). Slide the nail out and the bare coil manages... none (the core was lever three, and it was doing most of the lifting). Three levers, separately demonstrated — a complete experimental physics paper, conducted at a kitchen table.

**Example 2: The electric bell's self-interrupting genius**
Press the button: current flows, electromagnet pulls the iron hammer-arm toward the gong — *but the arm's motion breaks its own contact*, killing the current; the field collapses, the spring returns the arm, contact remakes, and the cycle repeats fifty times a second: *brrrrring*. A machine whose entire function is built from switching an electromagnet with its own output — 1830s logic circuitry in brass.

**Example 3: MRI — the electromagnet's cathedral**
An MRI's main coil carries hundreds of amps through superconducting wire (cooled near absolute zero — zero resistance, so the current circulates for *years* unpowered) to make a field ~50,000× Earth's. That field aligns the hydrogen nuclei in your body; radio pulses and detection coils do the imaging. The safety briefing — no steel anywhere near the room — is yesterday's materials lesson enforced by a field that cannot be switched off quickly (quenching one boils away thousands of litres of helium). The Tower's nail, at cathedral scale.

## Common Mistakes

- **"The wire becomes magnetic like iron"** — the *current* makes the field; stop the current, the copper is just copper. (Iron cores add material magnetism; copper never does.)
- **Forgetting the field's geometry** — straight wire: circles, no poles; coil: bar-magnet pattern with poles. Different shapes, one cause.
- **Steel cores for switchable magnets** — steel remembers (stays magnetised); soft iron forgets on demand. Choose by job.
- **"Electromagnets are stronger than permanent magnets"** — neither universally; the electromagnet's defining edge is *control*, not strength (though MRI superconductors do also win on strength).
- **Ignoring the power bill** — an electromagnet holds only while fed (I²R heating included); magnetic door-locks fail *unlocked* in a power cut by design — check which way your application should fail.

## Mental Model

A permanent magnet is **a trained guard dog that never sleeps and never lets go** — loyal, tireless, but utterly beyond instruction. An electromagnet is **a guard dog on a voice-command collar**: "grip" (current on), "release" (off), "harder" (more current), "other hand" (reverse) — and you can breed it stronger by feeding it more amps, training it longer (turns), or giving it an iron skeleton to multiply its muscle. Civilisation's machinery is run almost entirely by the commandable breed: every relay click, lock thunk, and bell ring is the collar speaking. The permanent breed still has its posts — wherever a field must *never* fail or no power is available — but wherever control matters, the collar wins.

## Mini Summary

- ✔ Every current makes a field: circles round a wire (right-hand grip), bar-magnet pattern from a coil
- ✔ Three strengtheners: more current, more turns, iron core (soft iron forgets; steel remembers)
- ✔ The electromagnet's superpower is control: on/off/variable/reversible
- ✔ Relays let whisper-currents command torrents — the interface of information and force
- ✔ Cranes, bells, locks, valves, MRI: one principle, dressed for every trade

# Guided Practice Quest

Work through the guided steps to re-live Ørsted's flinch, pull the three levers in order, and write the relay's job description as machinery's universal handshake.

# Solo Practice Quest

Build and audit the Tower's servant: wind an insulated wire (1–2 m) around a large iron nail or bolt, power it from a single 1.5 V cell (briefly — it warms; that's I²R saying hello). (1) *The three levers*: measure lifting capacity (paperclips) at two turn-counts and two currents (one vs two cells), and with/without the core — tabulate all and confirm each lever's effect. (2) *Polarity*: use a compass to identify your coil's north face; reverse the battery and re-test. (3) *Field map*: filings or compass-walk your energised coil and compare its portrait with yesterday's bar magnet. Close with a one-paragraph design: choose any door in your life and specify an electromagnetic mechanism for it (lock? bell? automatic release?), stating core material, fail-safe direction, and which of the three levers your budget would spend on.

# Integration

**Engineering**: The solenoid actuator — coil pulls iron plunger — is the electromagnet's workhorse form: fuel injectors, washing-machine valves, pinball flippers, and starter solenoids all run on it. Loudspeakers invert the geometry (coil moves, magnet stays) to turn currents into sound — your headphones are electromagnets whispering several thousand commands per second.

**History**: From Ørsted's 1820 flinch, the cascade was breathtaking: Ampère's mathematics within months, Sturgeon's electromagnet (1825), Henry's relay-grade coils, and by 1837 the electric telegraph — electromagnets clicking messages across continents two decades before Maxwell explained any of it. The lesson's nail-and-wire build is, quite literally, the 1820s replayed; treat the tingle of it accordingly.

# Lore Conclusion

Your nail-servant performs its full repertoire for Hale — grip, release, reverse, the three levers each demonstrated with tabulated clips — and she enters it in the Tower's registry of "trained iron", a ledger whose early pages, you notice, are signed by names from the history books. "Currents make fields. Fields that obey switches. With that, junior, you hold half of the great engine." She closes the registry and regards you with the particular glitter that precedes her best mornings. "Consider: the current MOVES, and magnetism appears. Tomorrow we seat a current-carrying wire INSIDE another magnet's field — two fields, overlapping, arguing — and discover that the argument produces something the Tower spent its first century dreaming of." She taps the workbench, where tomorrow's kit already waits under cloth: a horseshoe magnet, a cradle of wire, a battery. "Push, junior. Pure, controllable, electric *push*. Tomorrow we make the river do work: the motor effect."
