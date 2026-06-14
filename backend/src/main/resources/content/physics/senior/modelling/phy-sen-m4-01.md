---
id: phy-sen-m4-01
domainId: physics
tier: SENIOR
moduleId: phy-sen-m4
moduleTitle: "Module 4: Computational Physics"
moduleGlyph: "💻"
moduleSortOrder: 4
topicSlug: modelling
topicTitle: "Modelling"
topicSortOrder: 1
title: "Physical Modelling: The Art of Honest Simplification"
sortOrder: 1
xpReward: 120
practiceType: NONE
questType: INVESTIGATION
feynmanPrompt: "Explain to a junior student what a physical model is, why every model must leave things out, and how physicists decide whether a model is good enough to trust."
learningObjectives:
  - Explain what a physical model is and why simplification is essential rather than a flaw
  - Apply the modelling cycle: simplify, formalise, predict, validate against reality, refine
  - Judge model adequacy by purpose, identify dominant effects, and recognise when a model breaks down
integrationDomains: [mathematics, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines a model as a deliberate simplification of reality built to answer a specific question, and explains why including everything is neither possible nor desirable"
    - "Describes the modelling cycle: identify the question, choose what to keep and discard, formalise mathematically, predict, validate against data, refine"
    - "Demonstrates judgement about dominant effects with a concrete example (e.g. when air resistance matters for a projectile and when it doesn't)"
    - "Explains validation and breakdown: a model is trusted within tested limits, and a mismatch with reality locates either a refinement need or new physics"
  keywords: [model, simplification, assumption, dominant, validate, refine, domain of validity]
  modelAnswer: |
    A physical model is a deliberately simplified copy of some piece of reality, built to
    answer a specific question. The simplification is not a regrettable compromise — it
    is the entire method. Reality has too many parts: a real pendulum has air swirling
    around it, friction in the pivot, a stretching string, a slightly aspherical bob in
    a slightly non-uniform gravitational field. A model that kept everything would be as
    intractable as the world itself. The skill is keeping what dominates the answer to
    YOUR question and discarding what does not.

    The cycle runs: pose the question; choose simplifications (point mass, rigid rod,
    no air); write the surviving physics as equations; extract predictions; test the
    predictions against reality; then refine if the mismatch matters. Every model I have
    used at this Academy lives somewhere on this cycle. The 'ignore air resistance'
    projectile model predicts a cricket ball's range to a few percent — good enough for
    the village green — but applied to a table-tennis ball it fails badly, because drag
    is a dominant effect for light, slow objects. Same model, different question,
    different verdict.

    A model is never true or false; it is adequate or inadequate for a purpose, within a
    domain of validity. Newton's mechanics is a model that is superb below a tenth of
    light speed and fails beyond it — the failure is not disgrace but boundary-marking.
    Indeed the most precious moments in physics are validated models breaking: Mercury's
    orbit refused Newton's model by 43 arcseconds per century, and the refusal was
    general relativity knocking. A good modeller states their assumptions out loud,
    quantifies what was discarded, trusts the model only inside its tested range, and
    treats every mismatch as information.
guidedSteps:
  - id: phy-sen-m4-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You must predict where a fired cannonball lands, to within a few percent. Reality
      offers you: gravity, air resistance, the Earth's rotation, the gravitational pull of
      the Moon, and the cannonball's slight asphericity. Which belongs in the model?
    inputConfig:
      options:
        - "All of them — accuracy demands including everything"
        - "Gravity alone — models should always be as simple as possible"
        - "Gravity, and air resistance if the required precision demands it — the rest are negligible at this scale"
        - "Whichever effects have the simplest equations"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Gravity, and air resistance if the required precision demands it — the rest are negligible at this scale"]
      rejectedFeedback: "Modelling is triage by magnitude. Gravity dominates utterly. Drag shifts a real cannonball's range by enough percent to matter at 'a few percent' precision. Earth's rotation, the Moon, and asphericity contribute orders of magnitude less than your tolerance — carrying them is cost without benefit. Keep what the question's precision requires; discard the rest, on the record."
    hint: "Estimate each effect's size against the precision you need. Which effects could shift the landing point by 'a few percent'?"
    reflectionPrompt: "Why is 'include everything' not merely impractical but actively bad modelling?"
  - id: phy-sen-m4-01-g2
    sortOrder: 2
    inputType: SEQUENCE
    instruction: |
      Arrange the stages of the modelling cycle into their working order:

      - "Validate predictions against real measurements"
      - "Pose a precise question about a real system"
      - "Extract predictions from the mathematics"
      - "Choose simplifications and state assumptions"
      - "Refine the model where mismatch matters"
      - "Formalise the kept physics as equations"
    inputConfig:
      items:
        - "Validate predictions against real measurements"
        - "Pose a precise question about a real system"
        - "Extract predictions from the mathematics"
        - "Choose simplifications and state assumptions"
        - "Refine the model where mismatch matters"
        - "Formalise the kept physics as equations"
    markingRule:
      matchMode: CONTAINS
      accepted: ['"pose a precise question about a real system","choose simplifications and state assumptions","formalise the kept physics as equations","extract predictions from the mathematics","validate predictions against real measurements","refine the model where mismatch matters"']
      rejectedFeedback: "The cycle runs: question → simplify → formalise → predict → validate → refine (and around again). The question comes first because it decides what may be discarded; validation comes before refinement because reality, not taste, decides what needs fixing."
    hint: "Start with the question — it governs every later choice. End with refinement, which loops back for another pass."
    reflectionPrompt: "Why is this drawn as a cycle rather than a one-way pipeline?"
  - id: phy-sen-m4-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Your frictionless, no-air pendulum model predicts a swing period of 2.00 s. The real
      pendulum on the bench measures 2.01 s — and its swing slowly dies away, which your
      model says cannot happen.

      Is the model wrong? Answer in one or two sentences, using the idea of *purpose* —
      what the model is for.
    inputConfig:
      placeholder: "Is the model wrong? It depends on..."
    markingRule:
      matchMode: CONTAINS
      accepted: ["purpose", "question", "good enough", "depends", "adequate", "precision"]
      rejectedFeedback: "A model is judged against its purpose, not against completeness. For predicting the period it errs by half a percent — adequate for almost any purpose. For predicting long-term amplitude it omits the dominant effect (friction) and is inadequate. Same model, two questions, two verdicts — neither of which is 'true' or 'false'."
    hint: "Half a percent error on the period; total failure on the dying swing. What single word decides whether each of those failures matters?"
    reflectionPrompt: "State one question about the bench pendulum for which the simple model is exactly the right tool, and one for which it must be refined."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Newton's mechanics fails for objects near light speed. What is the correct modelling attitude to this failure?"
    options:
      - "Newton's mechanics is false and should no longer be taught or used"
      - "It marks the model's domain of validity — inside it (v ≪ c), Newton remains superb and is the right tool"
      - "The failure shows that all physics models are unreliable"
      - "Relativity should be used for all calculations, including thrown cricket balls"
    correctIndex: 1
    feedback: "Models earn trust within tested limits. Below a tenth of light speed, relativistic corrections are smaller than almost any measurement — Newton is not 'wrong' there; it is the efficient, validated tool. Knowing a model's boundary is part of owning the model."
  - type: MULTIPLE_CHOICE
    question: "Mercury's orbit disagreed with Newtonian predictions by a tiny, persistent 43 arcseconds per century. What did this mismatch turn out to be?"
    options:
      - "A measurement error that better telescopes removed"
      - "Evidence of an undiscovered planet, as with Neptune"
      - "New physics — the first observed effect of general relativity"
      - "Proof that orbital mechanics cannot be modelled"
    correctIndex: 2
    feedback: "Astronomers first tried the Neptune trick — hunting a hidden planet ('Vulcan'). It wasn't there. The stubborn residual was spacetime curvature announcing itself. Validated models that break under good data are physics' most precious instruments: the crack is where the light gets in."
---

# Hook

Here is a sentence that sounds like a confession but is actually a boast: *every physics calculation you have ever done was wrong.* The frictionless pendulum, the airless cannonball, the point-mass planet, the rigid lever — none of these exist. You have spent three tiers calculating the behaviour of objects that are not real.

And the predictions came out right — bridges stand, probes reach Pluto, GPS finds your street. That is the deepest trick in physics' repertoire, deeper than any single law: the art of building a *false* thing that tells the *truth* about a specific question — and knowing exactly when to stop believing it. The trick has a name: modelling. Every law you have learned lives inside one. Today the trick itself becomes the subject.

# Lore Introduction

The Deep Laboratories have been rearranged one final time. The benches now face a wall of slate ruled into neat columns, and at the room's centre stands something new: a brass-framed engine of gears and counters — the Academy's *difference engine* — beside the familiar bench pendulum from Module 1.

"Before the engine, the question it serves," Selka says. "All term I showed you nature directly — muons, spectra, the clicking casket. But I cannot bench-mount a star. I cannot wire a galaxy to a galvanometer or pour a climate into a vacuum jar. Past a certain complexity, *showing* fails, and physics has one move left: build a small, false, honest copy of the thing, and interrogate the copy."

She sets the pendulum swinging. "You have been using such copies since your first week as an Apprentice — you were simply never told. 'Ignore friction.' 'Treat the bob as a point.' Each instruction quietly built a model. This module, the building becomes deliberate: what to keep, what to discard, how to confess your discards, and how to know when your beautiful copy has begun to lie. The engine computes. *You* must decide what is worth computing."

# Core Learning

## Concept Introduction

**What a model is.** A **physical model** is a deliberately simplified representation of a real system, built to answer a *specific question*. The simplification is not an embarrassment to be minimised — it is the method itself. A real pendulum involves air swirling in three dimensions, friction in the pivot, string stretch, bob asphericity, temperature drifts, and the gravitational influence of the Moon. A "model" that kept everything would be exactly as intractable as reality — a map the size of the territory. Modelling is the disciplined choice of what to *keep*.

**The triage of dominant effects.** What to keep is decided by *magnitude against purpose*. Estimate each effect's size; compare it to the precision your question demands; keep what exceeds the tolerance, discard what falls below it — *and write the discards down*. For a cannonball's range to a few percent: gravity (dominant), drag (percent-level — keep if precision demands), Earth's rotation, lunar gravity, asphericity (orders of magnitude below tolerance — discard, on the record). The same triage with a table-tennis ball reverses a verdict: drag becomes dominant for light, slow objects. **No simplification is valid in itself; it is valid for a question.**

**The modelling cycle.** Modelling is iterative:

1. **Pose the question** precisely — it governs everything after
2. **Simplify**: choose what to keep; state every assumption aloud
3. **Formalise**: write the surviving physics as mathematics
4. **Predict**: extract numbers, curves, behaviours
5. **Validate**: test the predictions against real measurements
6. **Refine**: where reality disagrees *and the disagreement matters*, improve the model — then loop

The loop matters: models are not built once and trusted forever; they are provisional instruments under permanent probation.

**Domain of validity and the precious failure.** A validated model earns trust only *within the range where it was tested* — its **domain of validity**. Newton's mechanics is not "wrong"; it is a superb model below ~0.1c that fails beyond, and the boundary is part of what you know when you know Newton. The failures, when they come under good data, are physics' treasure: Mercury's perihelion crept 43 arcseconds per century away from the best Newtonian model. Astronomers first played the Neptune card — a hidden planet, "Vulcan" — and it wasn't there. The stubborn residual was general relativity announcing itself decades early. *A validated model breaking cleanly is the most information-dense event in science.* The whole of last module — relativity, quanta — entered physics through exactly such cracks.

## Why It Matters

Modelling is the load-bearing skill of every quantitative profession. Engineering runs on it: every bridge is approved, every aircraft certified, every chip laid out on models whose assumptions someone chose and someone checked — and the famous failures (Tacoma Narrows among them) are almost always assumption failures, not arithmetic ones. Climate projection, epidemic forecasting, financial risk, drug design: all are modelling cycles wearing different uniforms, and public arguments about them are usually arguments about *what was kept and what was discarded* — which you can now read critically. Within physics, this lesson is the gateway to the entire computational module: the engine you are about to program can only execute a model; it cannot choose one. Garbage assumptions in, confident garbage out — at millions of operations per second.

## Worked Examples

**Example 1 — One pendulum, three models.** *Question A:* the period, to 1%? Model: point mass, rigid rod, no friction, small swings → T = 2π√(L/g). Bench test: predicted 2.00 s, measured 2.01 s. Adequate; stop. *Question B:* why does the swing die in twenty minutes? The frictionless model *cannot answer* — friction, the discarded term, is now the dominant effect. Refine: add a drag term. *Question C:* behaviour at very large swing angles? The small-angle assumption breaks; the period grows with amplitude. Same brass bob; three questions; three different models — purpose decides everything.

**Example 2 — The triumphant discard.** To compute Earth's orbit, model Earth — 6 × 10²⁴ kg, oceans, mountains, weather — as a *single point*. Outrageous, and superb: for gravity at orbital distances, a sphere acts exactly as a point (Newton proved it — Junior gravitation). The art is not timid simplification but *bold simplification justified by argument*.

**Example 3 — Triage by estimate.** Does air resistance matter for (a) a dropped cannonball, (b) a dropped sheet of parchment, (c) a falling raindrop? Estimate drag against weight: (a) percent-level — discard for rough work, keep for artillery tables; (b) drag *exceeds* weight within centimetres — it IS the physics; keep or model nothing; (c) drag grows until it *equals* weight — terminal velocity, your Apprentice everyday-physics lesson — and the model must keep both terms or miss the phenomenon entirely. One force, three verdicts, all by magnitude-against-purpose.

## Common Mistakes

- Believing more detail always means a better model — beyond the question's tolerance, detail adds cost, obscures insight, and multiplies ways to be wrong
- Leaving assumptions implicit — unstated simplifications are the ones that ambush you; professionals list them like surgeons counting instruments
- Trusting a model outside its validated range — extrapolation without new validation is guessing with confident formatting
- Treating model failure as disgrace — within physics, a clean failure under good data is discovery's doorbell (Mercury, the photoelectric effect)
- Validating against the data used to build the model — genuine tests use *new* data the model has never seen
- Confusing the model with reality after long use — the frictionless pendulum is so familiar it starts to feel true; the map is never the territory

## Mental Model

A model is a *caricature* drawn for a purpose. A good caricaturist does not draw every pore — they find the three lines that capture the face and discard the rest, and the right three lines depend on what the drawing is *for*: a passport sketch keeps different lines than a satirical cartoon of the same face. A model that keeps everything is a photograph — as complicated as the sitter and no easier to understand. The modeller's craft is choosing the lines; the modeller's honesty is admitting which were left out; and the modeller's wisdom is knowing the caricature will mislead precisely where a discarded line mattered.

## Mini Summary

- A model is a deliberate simplification of reality built to answer a specific question; keeping everything is neither possible nor desirable
- Triage effects by magnitude against the question's required precision; state every assumption explicitly
- The cycle: question → simplify → formalise → predict → validate → refine — looped, never finished
- Models are adequate-for-purpose within a domain of validity, never true or false; clean failures under good data are how new physics announces itself

# Guided Practice Quest

Selka chalks a cannon, a pendulum, and the planet Mercury across the columned slate. "Three trials in the modeller's craft. First, triage: a cannonball, five candidate effects, a few percent of tolerance — sort the keepers from the discards and justify each by size. Second, the cycle itself: six stages, one working order — assemble it, and tell me why it bites its own tail. Third, the bench pendulum against its frictionless ghost: two predictions, one half-percent of disagreement and one total failure — deliver the verdict, and make it depend on purpose, because nothing else can decide it."

# Solo Practice Quest

Write a modeller's memorandum (350–500 words). Choose any real physical system you have met in this Academy — a pendulum, a projectile, a circuit, a star, a falling raindrop — and model it twice for two *different* questions. For each: state the question and its required precision, list what you keep and what you discard with a magnitude-based justification for every discard, name the resulting mathematics (no need to solve it), and say how you would validate the predictions. Then identify where each model must break — its domain of validity — and close with a reflection on Mercury: why is a validated model failing cleanly worth more to physics than a model that never fails?

# Integration

**Mathematics:** Formalisation is translation into mathematics, and the triage step has a mathematical engine: orders-of-magnitude estimation and dimensional analysis — your Apprentice mathematical-tools lessons, now running strategy rather than checking homework. The refinement ladder (add the drag term, add the next correction…) is the physicist's version of series expansion: each term smaller, kept only while it matters.

**Engineering:** Engineering codifies modelling discipline into law: safety factors are quantified humility about discarded effects, certification regimes are institutionalised validation, and post-failure inquiries are refinement loops with legal force. The engineer's phrase "fit for purpose" is this lesson's entire content in three words — and the Tacoma Narrows inquiry, which you met at the resonance bench, was history grading a model's discarded term.

# Lore Conclusion

Selka stops the pendulum with one finger and looks from it to the brass difference engine, waiting in the room's centre with its counters all at zero.

"You now hold the craft's first half: *what* to compute, and what to confess. But notice what every model on today's slate had in common — I chose systems whose equations you could solve with chalk. The pendulum's period. The cannonball's arc. Closed forms, solvable by hand in an afternoon." She rests her hand on the engine's frame. "Now ask: what of the pendulum at *large* swing? Three planets pulling on one another? The equations write themselves easily enough — and then they refuse to be solved. Not by you, not by me, not by anyone, *ever*, in closed form. That is not ignorance, Senior; it is a theorem."

She turns the engine's first gear; the counters click over, one step. "When the equations cannot be solved, they can still be *marched* — one small step at a time, ten thousand patient steps an hour. Tomorrow: *Numerical Methods* — how to walk through a problem too hard to leap, and how to keep the small errors of each step from quietly conspiring into a lie."
